// get all thread names
// for each thread join
// filter function
// join last child, first child if same start and name

function mergeSnapshots(data) {
    let threadNames = new Set(
        data.map(d => 
            Array.from(new Set(d.flatMap(s => s.thread)))
        ).flatMap(s => s)
    );

    let result = [];
    
    for(let threadName of threadNames){
        const snapshotsFromThread = data.map(d => d.filter(s => s.thread === threadName)).flatMap(s => s);
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
    if(spanA.id!== spanB.id){
        throw new Error(`Different spans can't be merged.
            Function ids are different '${spanA.name}:${spanA.id}' and '${spanB.span.name}:${spanB.span.id}'`);
    }
    let finalSpan = {
        ...spanA,
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
    if(childrenB.length == 0) return childrenA;
    // if last children from childrenA name and entry time is the same as first children from childrenB
    // merge spans
    const lastSpanOnChildrenA = childrenA[childrenA.length - 1];
    const firstSpanOnChildrenB = childrenB[0];
    if(lastSpanOnChildrenA.id === firstSpanOnChildrenB.id){
        const intersection = mergeSpans(lastSpanOnChildrenA, firstSpanOnChildrenB);
        const childrenAExceptLast = childrenA.slice(0, childrenA.length - 1);
        const childrenBExceptFirst = childrenB.slice(1);
        return childrenAExceptLast.concat(intersection).concat(childrenBExceptFirst);
    }

    return childrenA.concat(childrenB);
}

function increaseZeroValues(data) {
    return data.map( d => ({
      ...d,
      "span": increaseSpanValue(d.span)
    }));
}

function increaseSpanValue(span) {
    let newSpan = {...span};
    if(span.children && span.children.length > 0){
        newSpan.children = span.children.map(increaseSpanValue);
        newSpan.value = newSpan.children.map(c => c.value).reduce((a, b) => a + b);
        if ( newSpan.exitTime == -1) {
            newSpan.exitTime = newSpan.children[newSpan.children.length -1].exitTime;
        }
    } else {
        if(newSpan.value == 0) newSpan.value = 1;
    }
    return newSpan;
}

function filterDataByLambda(data, threadName, filterLambda){
    if(filterLambda.trim() === "") return data;
    const filterFunction = new Function('span', `return ${filterLambda};`);
    return data.map( d => {
        if(d.thread !== threadName) return d;
        return {
            ...d,
            "span": filterSpans(d.span, filterFunction)
        };
    });
}

function filterSpans(span, filterFunction) {
    let newSpan = {...span};
    const shouldBeIncluded = filterFunction(span);
    const newChildren = [];
    if(span.children && span.children.length > 0){
        newSpan.children = span.children.map( c => filterSpans(c, filterFunction)).filter(c => c);
    }

    if(shouldBeIncluded){
        return newSpan;
    }

    if(newSpan.children && newSpan.children.length > 0){
        return newSpan;
    }

    return null;
}