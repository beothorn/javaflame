// get al thread names
// for each thread join
// filter function

let threadNames = Array.from(new Set(data.flatMap(e => e.map(ee => ee.thread))));



data.flatMap(e => e.filter(ee => ee.thread == "main"))

// join last child, first child if same start and name