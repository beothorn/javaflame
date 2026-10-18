'use strict';

// Behavioral CLI tests: exercise the actual file parser and public query output.
// Run from the repository root: node --test src/test/js/javaflameDataExplorer.test.cjs
const { test, after } = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const { spawnSync } = require('node:child_process');
const repo = path.resolve(__dirname, '../../..');
const script = path.join(repo, 'skill/javaflame/javaflameDataExplorer.js');
const temp = fs.mkdtempSync(path.join(os.tmpdir(), 'javaflame-explorer-test-'));
after(() => fs.rmSync(temp, { recursive: true, force: true }));

function span(id, exitTime, children = [], extra = {}) {
  return { id, name: `call ${id}`, className: 'example.Service', method: `m${id}`,
    entryTime: 10, exitTime, value: exitTime < 0 ? 0 : exitTime - 10, children, ...extra };
}
const longValue = 'brackets [ ] \\ " ,\n' + 'x'.repeat(150000) + 'NEEDLE';
const first = [{ thread: 'main', snapshotTime: 20,
  span: span('0', -1, [span('1', -1, [span('2', 20)], { arguments: [{ type: 'String', value: longValue }] })]) }];
const second = [{ thread: 'main', snapshotTime: 40,
  span: span('0', -1, [span('1', 40, [span('3', 30)], { return: { type: 'String', value: 'done' } })]) }];
const fixture = path.join(temp, 'data.js');
fs.writeFileSync(fixture, `var data = [${JSON.stringify(first)},\n${JSON.stringify(second)},\n];`);

function run(file, command, args = [], input) {
  return spawnSync(process.execPath, [script, file, command, ...args], { encoding: 'utf8', input, maxBuffer: 1024 * 1024 });
}
function query(command, args = [], file = fixture) {
  const result = run(file, command, args);
  assert.equal(result.status, 0, result.stderr);
  return JSON.parse(result.stdout);
}

test('merges snapshots once per call, retains children and completed metadata', () => {
  const summary = query('summary');
  assert.equal(summary.snapshots, 2);
  assert.equal(summary.calls, 4);
  assert.equal(summary.unfinished, 1);
  assert.equal(summary.maxDepth, 2);
  const call = query('show', ['--id', '1']);
  assert.equal(call.durationMs, 30);
  assert.equal(call.children, 2);
  assert.equal(call.data.return.value, 'done');
  assert.equal(call.outputTruncated, true);
  assert.equal(query('hot').rows.find(r => r.method === 'm1').totalMs, 30);
});

test('navigates recorded callers and children with correct absolute/relative depth', () => {
  assert.equal(query('parent', ['--id', '3']).row.id, '1');
  assert.equal(query('parent', ['--id', '0']).row, null);
  assert.deepEqual(query('ancestors', ['--id', '3']).rows.map(r => r.id), ['0', '1']);
  assert.deepEqual(query('children', ['--id', '1']).rows.map(r => r.id), ['2', '3']);
  assert.equal(query('list', ['--depth', '2']).total, 2);
  assert.equal(query('subtree', ['--id', '1', '--max-depth', '0']).total, 1);
});

test('searches full values while previews and pages remain bounded', () => {
  assert.equal(query('search', ['--query', 'needle', '--field', 'arguments']).rows[0].id, '1');
  const p = query('list', ['--limit', '2']);
  assert.equal(p.total, 4); assert.equal(p.nextOffset, 2);
  assert.equal(query('list', ['--limit', '2', '--offset', '2']).nextOffset, null);
  const value = query('show', ['--id', '1', '--field', 'arguments.0.value',
    '--text-offset', String(longValue.length - 6), '--text-limit', '6']);
  assert.equal(value.value, 'NEEDLE'); assert.equal(value.nextTextOffset, null);
  assert.equal(value.length, longValue.length);
});

test('produces bounded Mermaid with recorded edges', () => {
  const diagram = query('diagram', ['--id', '1', '--max-depth', '1']);
  assert.match(diagram.mermaid, /^flowchart TD/);
  assert.match(diagram.mermaid, /n0 --> n1/);
  assert.match(diagram.mermaid, /n0 --> n2/);
  assert.equal(diagram.rows.length, 3);
});

test('session reuses the index and recovers after an invalid query', () => {
  const result = run(fixture, 'session', [], '{"command":"summary"}\n{"command":"list","limit":101}\n{"command":"show","id":"1","field":"return.value"}\n');
  assert.equal(result.status, 0, result.stderr);
  const rows = result.stdout.trim().split('\n').map(JSON.parse);
  assert.equal(rows[0].calls, 4); assert.match(rows[1].error, /limit/);
  assert.equal(rows[2].value, 'done');
});

test('rejects incomplete data and executable suffixes without executing them', () => {
  for (const [name, content] of [
    ['partial', 'var data = [['],
    ['code', 'var data = []; process.stdout.write("EXECUTED")'],
    ['invalid', 'var data = [[{"secret":"DO_NOT_ECHO",}]];'],
  ]) {
    const file = path.join(temp, `${name}.js`); fs.writeFileSync(file, content);
    const result = run(file, 'summary');
    assert.equal(result.status, 1); assert.equal(result.stdout, '');
    assert.doesNotMatch(result.stderr, /DO_NOT_ECHO|EXECUTED/);
  }
});

test('supports raw JSON and requires thread disambiguation for reused IDs', () => {
  const file = path.join(temp, 'raw.json');
  fs.writeFileSync(file, JSON.stringify([[...first, { thread: 'other', span: span('0', -1) }]]));
  assert.equal(query('threads', [], file).total, 2);
  assert.equal(run(file, 'show', ['--id', '0']).status, 1);
  assert.equal(query('show', ['--id', '0', '--thread', 'other'], file).thread, 'other');
});

test('explores the repository demo trace in one query session', () => {
  const commands = [
    { command: 'summary' }, { command: 'threads' },
    { command: 'search', query: 'sort', limit: 3 }, { command: 'list', depth: 1, limit: 3 },
    { command: 'show', id: '0' }, { command: 'children', id: '0', limit: 3 },
    { command: 'subtree', id: '0', 'max-depth': 1, limit: 3 },
    { command: 'diagram', id: '0', 'max-depth': 1, limit: 3 }, { command: 'hot', limit: 3 },
  ];
  const result = run(path.join(repo, 'docs/data.js'), 'session', [], commands.map(c => JSON.stringify(c)).join('\n'));
  assert.equal(result.status, 0, result.stderr);
  const output = result.stdout.trim().split('\n').map(JSON.parse);
  assert.equal(output.length, commands.length);
  for (const response of output) assert.equal(response.error, undefined);
  assert.ok(output[0].calls > 0); assert.ok(output[2].total > 0);
  assert.ok(result.stdout.length < 20000);
});
