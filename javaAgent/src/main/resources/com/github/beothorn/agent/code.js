// get al thread names
// for each thread join
// filter function
// join last child, first child if same start and name

function mergeSnapshots(data) {
    let threadNames = Array.from(new Set(data.flatMap(e => e.thread)));

    // data.flatMap(e => e.filter(ee => ee.thread == "main"))
    
    return [];
}