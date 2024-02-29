// get all thread names
// for each thread join
// filter function
// join last child, first child if same start and name


function prepareData(data) {
    const result = mergeSnapshots(data);
    enrichNodes(result);
    return result;
}

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
        "entryTime":spanA.entryTime, // it is the same function on both spans, we get the start from the first
        "exitTime":spanB.exitTime, // and the end from the second, this may be -1 if it has not finished yet
        "value":spanA.value + spanB.value,
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
    // it means the first snapshot caught the function running and when the second snapshot happened,
    // the function was still running, so we need to join them to get the whole picture
    const lastSpanOnChildrenA = childrenA[childrenA.length - 1];
    const firstSpanOnChildrenB = childrenB[0];
    if(lastSpanOnChildrenA.id === firstSpanOnChildrenB.id){
        const intersection = mergeSpans(lastSpanOnChildrenA, firstSpanOnChildrenB);
        const childrenAExceptLast = childrenA.slice(0, childrenA.length - 1);
        const childrenBExceptFirst = childrenB.slice(1);
        return childrenAExceptLast.concat(intersection).concat(childrenBExceptFirst);
    }
    // if they are not the same, it was just one function call followed by another, so just concatenate
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

/*
Return a copy of data with only spans that match the filter.
*/
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

/*
Returns a copy of the span if it matches the filter or if it has children that match the filter.
Child nodes that don't match the filter are removed from the copy.
Returns null if neither span or children matches the filter.
*/
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

function search(searchPredicate) {
    const rootChildren = dataForGraph.flatMap(t => t.span.children);

    const result = [];
    for(let n of rootChildren){
        searchNodes(n, searchPredicate, result);
    }
    return result;
}

function searchNodes(node, searchPredicate, result) {
    if(searchPredicate(node)) {
        result.push(node);
    }
    if(node.children) {
        for(let cn of node.children){
            searchNodes(cn, searchPredicate, result);
        }
    }
}

function searchString(searchTerm) {
    return search((n) => n.name.includes(searchTerm));
}

function searchId(idToSearch) {
    return search((n) => n.id === idToSearch);
}

function enrichNodesRecursively(node, parent, threadName) {
    node.thread = threadName;
    node.parent = parent.id;
    if(node.children) {
        for(let cn of node.children){
            enrichNodesRecursively(cn, node, threadName);
        }
    }
}

function enrichNodes(dataToEnrich) {
    for(let t of dataToEnrich){
        const threadName = t.thread;
        const span = t.span;
        span.thread = threadName;
        span.parent = "";
        for(let cn of span.children){
            enrichNodesRecursively(cn, span, threadName);
        }
    }
}
