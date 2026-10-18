# Trace explorer

Run `node <skill-directory>/javaflameDataExplorer.js <output-directory>/data.js <command> [options]`. Node.js 18+ is required; no npm dependencies. `--help` prints the interface without loading a trace.

| Command | Result |
| --- | --- |
| `summary` | Snapshot/call/thread counts, unfinished calls, depth and time range |
| `threads` | Thread names, call counts and synthetic root IDs |
| `list --depth 2` | Compact entries at absolute depth 2 (synthetic root = 0) |
| `search --query OrderService` | Case-insensitive literal search in class, method and display name |
| `search --query customer-123 --field arguments` | Search captured arguments; `return`, `stackTrace` or `all` also work |
| `show --id 42` | Metadata, arguments and return for one call, without descendants |
| `show --id 42 --field arguments.0.value` | A single captured value; dotted paths support array indices |
| `parent --id 42` | Immediate recorded caller, or null for a root |
| `ancestors --id 42` | Caller chain from root to immediate parent |
| `children --id 42` | Immediate recorded callees |
| `subtree --id 42 --max-depth 2` | Call plus descendants up to relative depth 2 |
| `hot --query com.example --limit 10` | Methods ranked by total inclusive duration; count, maximum, unfinished count |
| `diagram --id 42 --max-depth 3 --limit 30` | Bounded Mermaid call-tree source and supporting rows, in JSON |
| `session` | Load once and accept JSON-line queries on stdin |

## Bounds and selectors

All result lists default to 20 rows, maximum 100. Use `--limit N --offset N`; `total` counts matching rows and `nextOffset` is null when complete. `--thread NAME` selects an exact thread. `--query TEXT`, `--depth N` and `--min-ms N` restrict list/search, navigation lists, summaries and aggregation. `--max-depth N` limits absolute depth for list/search, relative depth for subtree/diagram. `show` and `parent` select by ID/thread, not by search filters. `--min-ms` excludes incomplete calls. `hot` excludes synthetic roots and aggregates all matching calls before paging.

String previews default to 160 characters, with `--text-limit` up to 4000. Nested arrays show at most 20 elements and deeply nested metadata is bounded. Output also has a shared string budget; `outputTruncated` flags clipping. Inspect a specific array element via `show --field arguments.21.value`. To read long strings in slices, use `show --field return.value --text-offset 160 --text-limit 160`, following `nextTextOffset`. Strings are sliced in JavaScript UTF-16 code units. Do not interpret a clipped preview as a complete captured value. Mermaid source has its own 20,000-character limit within the shared budget; if output is truncated, request fewer nodes before rendering.

IDs are strings from the capture. Merge uses thread plus ID; a repeated ID in a different thread requires `--thread` to disambiguate. Repeated snapshot spans retain completed metadata and union child calls. Duration uses final `exitTime - entryTime` in milliseconds; unfinished calls report null. Synthetic thread roots generally remain unfinished. Timings are inclusive and instrumentation adds overhead: summing nested durations does not measure request elapsed time or CPU cost.

## Reuse the index

Each one-shot invocation parses the file again. Use a session for multiple queries on a large capture:

```bash
node "$SKILL_DIR/javaflameDataExplorer.js" "$OUT/data.js" session <<'QUERIES'
{"command":"summary"}
{"command":"search","query":"OrderService","limit":5}
{"command":"list","depth":1,"limit":5}
QUERIES
```

With an interactive process tool, send additional JSON lines after inspecting the preceding response. Session keys match CLI names without `--`, including `max-depth` and `text-limit`. Each request produces one JSON line; invalid requests return an error and the session continues. The session indexes a fixed capture, not a live-updating file; restart after recapturing. One-shot failures exit nonzero.

## Diagrams and interpretation

The `mermaid` property is source text, not a rendered image. Save its decoded value as a `.mmd` file or include it in a Mermaid code block. Labels contain class/method names, not argument values. Edges appear only when both endpoints are in the selected page. Pagination and query filtering can leave isolated nodes; use a small focused subtree with offset 0 for a coherent diagram. Cite call IDs and describe omitted branches. Cross-thread causality cannot be inferred from tree nesting alone.

## File and memory behavior

The parser accepts the agent's `var data = [...]` wrapper (also let/const), optional final semicolon, outer trailing comma, and raw JSON snapshot arrays. It does not evaluate/import JavaScript. Snapshot arrays are parsed one at a time; malformed/incomplete snapshots and executable suffixes are rejected. Analyze a completed capture or stable copy when the application is still running.

Memory is proportional to unique calls plus the largest snapshot. There is no disk-backed index or constant-memory guarantee. Tighten recording filters and reproduction scope first for huge captures; if machine memory permits, a deliberate `node --max-old-space-size=4096 ...` run can raise the heap limit. Do not solve large traces by printing them into context.
