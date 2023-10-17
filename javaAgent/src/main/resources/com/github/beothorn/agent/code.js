// get al thread names
// for each thread join
// filter function
// join last child, first child if same start and name

function mergeSnapshots(data) {
    let threadNames = Array.from(new Set(data.flatMap(s => s.thread)));

    // data.flatMap(e => e.filter(ee => ee.thread == "main"))

    let result = [];
    
    for(let threadName of threadNames){
        const snapshotsFromThread = data.filter(s => s.thread === threadName);
        const firstSnapshot = snapshotsFromThread[0];
        const lastSnapshot = snapshotsFromThread[snapshotsFromThread.length - 1];
        if(firstSnapshot.span.name !== lastSnapshot.span.name){
            throw new Error(`Root function should not change inside thread. 
            Function names are different '${firstSnapshot.snapshotTime}' and '${lastSnapshot.snapshotTime}'`);
        }
        // sum of all values from root function using reduce
        const mergedSnapshotForThread = snapshotsFromThread.reduce((snapA, snapB) => ({
            "thread": threadName,
            "snapshotTime": snapB.snapshotTime,
            "span": mergeSpans(snapA.span, snapB.span)
        }));
        result.push(mergedSnapshotForThread);
    }
    
    return result;
}

function mergeSpans(spanA, spanB) {
    if(spanA.name!== spanB.name){
        throw new Error(`Different spans can't be merged. 
            Function names are different '${spanA.name}' and '${spanB.span.name}'`);
    }
    let finalSpan = {
        "name": spanA.name,
        "entryTime":spanA.entryTime,
        "exitTime":spanB.exitTime,
        "value":spanA.value + spanB.value
    };
    
    if (spanA.children || spanB.children) {
        const childrenA = spanA.children || [];
        const childrenB = spanB.children || [];
        finalSpan.children = mergeChildren(childrenA, childrenB);
        if(spanB.exitTime == -1){
            finalSpan.value = finalSpan.children.map(c => c.value).reduce((a, b) => a + b);
            finalSpan.exitTime = spanA.entryTime + finalSpan.value;
        }
    }

    return finalSpan;
}

function mergeChildren(childrenA, childrenB){
    // concatenate lists

    // if last children from childrenA name and entry time is the same as first children from childrenB
    // merge spans
    const lastSpanOnChildrenA = childrenA[childrenA.length - 1];
    const firstSpanOnChildrenB = childrenB[0];
    if(lastSpanOnChildrenA.name === firstSpanOnChildrenB.name 
        && lastSpanOnChildrenA.entryTime === firstSpanOnChildrenB.entryTime){
            const intersection = mergeSpans(lastSpanOnChildrenA, firstSpanOnChildrenB);
        const childrenAExceptLast = childrenA.slice(0, childrenA.length - 1);
        const childrenBExceptFirst = childrenB.slice(1);
        return childrenAExceptLast.concat(intersection).concat(childrenBExceptFirst);
    }

    return childrenA.concat(childrenB);
}