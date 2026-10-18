#!/usr/bin/env node
'use strict';

/**
 * Explore Javaflame snapshots without sending the trace to an AI context window.
 * No dependencies and no eval: data.js is treated as data, never executable code.
 * Snapshots are parsed one at a time; an ID index retains one merged record per
 * call. Memory is O(unique calls + largest snapshot), not constant memory.
 * Run `node javaflameDataExplorer.js --help` for the bounded query interface.
 */
const fs = require('node:fs');
const readline = require('node:readline');

const HELP = `Usage: node javaflameDataExplorer.js DATA.js COMMAND [options]
Commands:
  summary                 Capture counts, time range and depth
  threads                 Per-thread counts and root IDs
  list                    Compact calls (optionally at exact --depth)
  search --query TEXT     Case-insensitive literal search
  show --id ID            One call's metadata, arguments and return; no children
  parent --id ID          Immediate recorded caller
  ancestors --id ID       Caller chain, root first
  children --id ID        Immediate recorded callees
  subtree --id ID         Descendants including selected call
  hot                     Methods ranked by total completed duration
  diagram --id ID         Bounded Mermaid call tree (JSON envelope)
  session                 JSON-lines queries on stdin; load/index the file once
Options (use --key value, not --key=value):
  --thread NAME           Exact thread name
  --depth N               Exact absolute depth; synthetic thread root is 0
  --max-depth N           Maximum depth (relative for subtree/diagram)
  --query TEXT            Literal filter on class, method and display name
  --field PATH            search/show: dotted data path, e.g. arguments.0.value
                         search also accepts all (includes arguments/return)
  --min-ms N              Minimum completed inclusive duration
  --limit N              Page size, default 20, maximum 100
  --offset N             Matching row offset, default 0
  --text-limit N          String preview length, default 160, maximum 4000
  --text-offset N         show --field: character offset into a long string
All output is JSON, including diagram source. Rows and strings are bounded;
nextOffset paginates matches. IDs are strings; add --thread if ambiguous.
Session example: {"command":"search","query":"save","limit":5}
Use a completed/copied trace: partial writes are rejected, not silently ignored.
`;

/** Validate CLI and session options identically, so misspellings cannot widen queries. */
function options(input) {
  const allowed = new Set(['command', 'id', 'thread', 'depth', 'max-depth', 'query',
    'field', 'min-ms', 'limit', 'offset', 'text-limit', 'text-offset']);
  const result = { limit: 20, offset: 0, 'text-limit': 160, 'text-offset': 0, ...input };
  for (const key of Object.keys(result)) {
    if (!allowed.has(key)) throw new Error(`Unknown option: ${key}`);
    if (['depth', 'max-depth', 'min-ms', 'limit', 'offset', 'text-limit', 'text-offset'].includes(key)) {
      const n = Number(result[key]);
      if (!Number.isSafeInteger(n) || n < 0) throw new Error(`${key} must be a nonnegative integer`);
      result[key] = n;
    } else if (typeof result[key] !== 'string') throw new Error(`${key} must be a string`);
  }
  if (result.limit < 1 || result.limit > 100) throw new Error('limit must be 1..100');
  if (result['text-limit'] < 1 || result['text-limit'] > 4000) throw new Error('text-limit must be 1..4000');
  return result;
}

/**
 * Recognize the agent's `var data = [ SNAPSHOT, ... ];` wrapper and raw JSON.
 * Track strings/escapes while finding each snapshot's closing bracket, so commas
 * and brackets inside captured argument values cannot affect parsing. JSON.parse
 * validates each snapshot. Only the agent's outer trailing comma is permitted.
 */
async function readSnapshots(file, consume) {
  let state = 'prefix', prefix = '', buffer = [], part = '';
  let nesting = 0, quoted = false, escaped = false;
  for await (const chunk of fs.createReadStream(file, { encoding: 'utf8' })) {
    for (const char of chunk) {
      if (state === 'prefix') {
        if (char === '[') {
          if (!/^\s*(?:(?:var|let|const)\s+data\s*=\s*)?$/.test(prefix)) throw new Error('Unsupported data.js wrapper');
          state = 'item';
        } else {
          prefix += char;
          if (prefix.length > 256) throw new Error('Missing data array');
        }
      } else if (state === 'item') {
        if (/\s/.test(char)) continue;
        if (char === ']') { state = 'end'; continue; }
        if (char !== '[') throw new Error('Expected a snapshot array');
        part = '['; nesting = 1; state = 'snapshot';
      } else if (state === 'snapshot') {
        part += char;
        if (quoted) {
          if (escaped) escaped = false;
          else if (char === '\\') escaped = true;
          else if (char === '"') quoted = false;
        } else if (char === '"') quoted = true;
        else if (char === '[' || char === '{') nesting++;
        else if (char === ']' || char === '}') nesting--;
        if (nesting === 0) {
          buffer.push(part); part = '';
          let snapshot;
          try { snapshot = JSON.parse(buffer.join('')); }
          catch { throw new Error('Invalid snapshot JSON (possibly an incomplete write)'); }
          consume(snapshot); buffer = []; state = 'separator';
        }
      } else if (state === 'separator') {
        if (/\s/.test(char)) continue;
        if (char === ',') state = 'item';
        else if (char === ']') state = 'end';
        else throw new Error('Expected comma or end of data array');
      } else if (state === 'end') {
        if (/\s/.test(char)) continue;
        if (char === ';') state = 'done';
        else throw new Error('Unexpected content after data array');
      } else if (!/\s/.test(char)) throw new Error('Unexpected executable content after data array');
    }
    // Accumulate chunks rather than repeatedly concatenating one huge snapshot.
    if (state === 'snapshot') { buffer.push(part); part = ''; }
  }
  if (!['end', 'done'].includes(state)) throw new Error('Incomplete trace; wait for capture completion or copy a stable snapshot');
}

/**
 * Merge calls by (thread, id), preserving the first parent and child order.
 * A running call occurs in several snapshots; later metadata completes it.
 * Duration is exitTime-entryTime in milliseconds, never a sum of repeated spans.
 * Iterative traversal handles deep call trees without recursive stack overflow.
 */
async function loadTrace(file) {
  const records = new Map();
  let snapshots = 0;
  await readSnapshots(file, snapshot => {
    snapshots++;
    for (const frame of snapshot) {
      if (!frame || typeof frame.thread !== 'string' || !frame.span) throw new Error('Invalid thread snapshot');
      const stack = [{ span: frame.span, parent: null, depth: 0 }];
      while (stack.length) {
        const { span, parent, depth } = stack.pop();
        if (!span || typeof span.id !== 'string' || !Number.isFinite(span.entryTime) ||
            !Number.isFinite(span.exitTime) || (span.children !== undefined && !Array.isArray(span.children))) {
          throw new Error('Invalid span schema');
        }
        const key = JSON.stringify([frame.thread, span.id]);
        let record = records.get(key);
        if (!record) {
          record = { key, thread: frame.thread, parent, depth, children: [], data: {} };
          records.set(key, record);
          if (parent) records.get(parent).children.push(key);
        } else if (record.parent !== parent) throw new Error('Span ID reused with a different parent');
        // Omit nested children from the index; edges are stored once as keys.
        const { children, ...metadata } = span;
        // Do not turn an already completed span back into an unfinished one.
        if (record.data.exitTime === undefined || record.data.exitTime === -1 || span.exitTime !== -1) {
          Object.assign(record.data, metadata);
        }
        for (let i = (children || []).length - 1; i >= 0; i--) {
          stack.push({ span: children[i], parent: key, depth: depth + 1 });
        }
      }
    }
  });
  return { records, snapshots };
}

/** Safe own-property lookup; query paths are data selectors, never JavaScript. */
function field(data, path) {
  let value = data;
  for (const key of path.split('.')) {
    if (value === null || typeof value !== 'object' || !Object.hasOwn(value, key)) return undefined;
    value = value[key];
  }
  return value;
}

/** Unfinished spans have unknown duration, even when the UI supplies a value. */
function duration(record) {
  const d = record.data;
  return d.exitTime < d.entryTime ? null : d.exitTime - d.entryTime;
}

/** Small navigation rows deliberately omit potentially enormous argument values. */
function compact(record, records) {
  return { id: record.data.id, thread: record.thread, depth: record.depth,
    parentId: record.parent ? records.get(record.parent).data.id : null,
    className: record.data.className, method: record.data.method,
    durationMs: duration(record), children: record.children.length };
}

/** Match metadata by default; value searches are opt-in because they cost more. */
function matches(record, opts) {
  if (opts.thread !== undefined && record.thread !== opts.thread) return false;
  if (opts.depth !== undefined && record.depth !== opts.depth) return false;
  if (opts['min-ms'] !== undefined && (duration(record) === null || duration(record) < opts['min-ms'])) return false;
  if (opts.query !== undefined) {
    const value = opts.field === 'all' ? record.data : opts.field ? field(record.data, opts.field) :
      [record.data.className, record.data.method, record.data.name];
    if (!(JSON.stringify(value) || '').toLowerCase().includes(opts.query.toLowerCase())) return false;
  }
  return true;
}

/** Resolve an ID without silently choosing between threads with colliding IDs. */
function select(records, opts) {
  if (opts.id === undefined) throw new Error('This command requires --id');
  const found = [...records.values()].filter(r => r.data.id === opts.id &&
    (opts.thread === undefined || r.thread === opts.thread));
  if (found.length !== 1) throw new Error(found.length ? 'Ambiguous ID; supply --thread' : 'Span ID not found');
  return found[0];
}

/** Walk descendants in call order, optionally stopping at a relative depth. */
function* descendants(root, records, maxDepth) {
  const stack = [root];
  while (stack.length) {
    const record = stack.pop();
    yield record;
    if (maxDepth === undefined || record.depth - root.depth < maxDepth) {
      for (let i = record.children.length - 1; i >= 0; i--) stack.push(records.get(record.children[i]));
    }
  }
}

/** Paginate while counting all matches; only the requested page is materialized. */
function page(iterable, opts, map = x => x) {
  let total = 0;
  const rows = [];
  for (const item of iterable) {
    if (total >= opts.offset && rows.length < opts.limit) rows.push(map(item));
    total++;
  }
  return { total, offset: opts.offset, nextOffset: opts.offset + rows.length < total ? opts.offset + rows.length : null, rows };
}

/** Execute a read-only query against the reusable index. */
function query(trace, opts) {
  const { records, snapshots } = trace;
  const row = r => compact(r, records);
  const all = () => records.values();
  const filtered = function* (source) { for (const r of source) if (matches(r, opts)) yield r; };
  if (opts.command === 'summary') {
    let calls = 0, unfinished = 0, maxDepth = 0, start = Infinity, end = -Infinity;
    const threads = new Set();
    for (const r of filtered(all())) {
      calls++; threads.add(r.thread); maxDepth = Math.max(maxDepth, r.depth);
      if (duration(r) === null) unfinished++;
      start = Math.min(start, r.data.entryTime);
      if (duration(r) !== null) end = Math.max(end, r.data.exitTime);
    }
    return { snapshots, calls, threads: threads.size, unfinished, maxDepth,
      startTime: Number.isFinite(start) ? start : null, endTime: Number.isFinite(end) ? end : null,
      note: 'Counts include synthetic thread roots; durations are instrumented wall-clock milliseconds.' };
  }
  if (opts.command === 'threads') {
    const groups = new Map();
    for (const r of filtered(all())) {
      if (!groups.has(r.thread)) groups.set(r.thread, { thread: r.thread, calls: 0, rootIds: [] });
      const g = groups.get(r.thread); g.calls++;
      if (!r.parent) g.rootIds.push(r.data.id);
    }
    return page(groups.values(), opts);
  }
  if (opts.command === 'hot') {
    const groups = new Map();
    for (const r of filtered(all())) {
      if (!r.parent) continue;
      const key = JSON.stringify([r.data.className, r.data.method]);
      if (!groups.has(key)) groups.set(key, { className: r.data.className, method: r.data.method, calls: 0, unfinished: 0, totalMs: 0, maxMs: 0 });
      const g = groups.get(key), ms = duration(r); g.calls++;
      if (ms === null) g.unfinished++;
      else { g.totalMs += ms; g.maxMs = Math.max(g.maxMs, ms); }
    }
    return { ...page([...groups.values()].sort((a, b) => b.totalMs - a.totalMs), opts),
      note: 'Inclusive recorded duration; nested calls overlap. Not CPU time or profiler accuracy.' };
  }
  if (['list', 'search'].includes(opts.command)) {
    if (opts.command === 'search' && opts.query === undefined) throw new Error('search requires --query');
    const source = function* () { for (const r of filtered(all())) {
      if (opts['max-depth'] === undefined || r.depth <= opts['max-depth']) yield r;
    } };
    return page(source(), opts, row);
  }
  if (!['show', 'parent', 'ancestors', 'children', 'subtree', 'diagram'].includes(opts.command)) throw new Error('Unknown command; use --help');
  const root = select(records, opts);
  if (opts.command === 'show') {
    if (opts.field) {
      const value = field(root.data, opts.field);
      if (value === undefined) throw new Error('Field not found');
      return { ...row(root), field: opts.field,
        ...(typeof value === 'string' ? { length: value.length, textOffset: opts['text-offset'],
          nextTextOffset: opts['text-offset'] + opts['text-limit'] < value.length ? opts['text-offset'] + opts['text-limit'] : null,
          value: value.slice(opts['text-offset'], opts['text-offset'] + opts['text-limit']) } : { value }) };
    }
    return { ...row(root), data: root.data };
  }
  if (opts.command === 'parent') return { row: root.parent ? row(records.get(root.parent)) : null };
  if (opts.command === 'ancestors') {
    const chain = [];
    for (let r = root; r.parent;) { r = records.get(r.parent); chain.push(r); }
    return page(filtered(chain.reverse()), opts, row);
  }
  const source = opts.command === 'children' ? root.children.map(k => records.get(k)) : descendants(root, records, opts['max-depth']);
  const result = page(filtered(source), opts, row);
  if (opts.command !== 'diagram') return result;
  // Diagram labels use a conservative alphabet: captured strings cannot inject
  // Mermaid syntax/HTML. Only edges whose endpoints are on this page are drawn.
  const ids = new Map(result.rows.map((r, i) => [r.id, `n${i}`]));
  const label = r => `${r.className}#${r.method}`.slice(0, 100).replace(/[^a-zA-Z0-9_.$# ()-]/g, '_');
  const lines = ['flowchart TD'];
  for (const r of result.rows) {
    lines.push(`  ${ids.get(r.id)}["${label(r)}"]`);
    if (ids.has(r.parentId)) lines.push(`  ${ids.get(r.parentId)} --> ${ids.get(r.id)}`);
  }
  return { ...result, mermaid: lines.join('\n'), note: 'Recorded calls only; filtered or paginated endpoints may omit edges.' };
}

/**
 * Last output boundary: cap strings, nested arrays and total string characters.
 * Truncation is explicit; show --field with text offsets retrieves long values.
 * Top-level result rows retain their requested page size. Nested collections are
 * capped at 20; select an individual array element using a dotted field path.
 */
function bounded(value, opts) {
  let budget = 24000, truncated = false;
  function visit(v, depth, key) {
    if (typeof v === 'string') {
      const size = Math.max(0, Math.min(v.length, key === 'mermaid' ? 20000 : opts['text-limit'], budget));
      budget -= size;
      if (size < v.length) { truncated = true; return v.slice(0, size) + '…[truncated]'; }
      return v;
    }
    if (v === null || typeof v !== 'object') return v;
    if (depth > 8) { truncated = true; return '[depth limit]'; }
    if (Array.isArray(v)) {
      const max = key === 'rows' ? opts.limit : 20;
      if (v.length > max) truncated = true;
      return v.slice(0, max).map(x => visit(x, depth + 1, ''));
    }
    return Object.fromEntries(Object.entries(v).map(([k, x]) => [k, visit(x, depth + 1, k)]));
  }
  const result = visit(value, 0, '');
  return { ...result, outputTruncated: truncated };
}

/** CLI mode exits nonzero on errors; session mode reports each failed query and continues. */
async function main() {
  const args = process.argv.slice(2);
  if (args.includes('--help') || !args.length) { process.stdout.write(HELP); return; }
  const [file, command = 'summary', ...rest] = args;
  const input = { command };
  for (let i = 0; i < rest.length; i += 2) {
    if (!rest[i].startsWith('--') || rest[i + 1] === undefined) throw new Error('Expected --option value');
    input[rest[i].slice(2)] = rest[i + 1];
  }
  const opts = options(input);
  const trace = await loadTrace(file);
  if (command === 'session') {
    const lines = readline.createInterface({ input: process.stdin, crlfDelay: Infinity });
    for await (const line of lines) {
      if (!line.trim()) continue;
      try {
        const request = options(JSON.parse(line));
        process.stdout.write(JSON.stringify(bounded(query(trace, request), request)) + '\n');
      } catch (error) { process.stdout.write(JSON.stringify({ error: error.message }) + '\n'); }
    }
  } else process.stdout.write(JSON.stringify(bounded(query(trace, opts), opts), null, 2) + '\n');
}

main().catch(error => {
  // Avoid echoing captured JSON or arbitrary trace contents in parse errors.
  process.stderr.write(JSON.stringify({ error: error.message }) + '\n');
  process.exitCode = 1;
});
