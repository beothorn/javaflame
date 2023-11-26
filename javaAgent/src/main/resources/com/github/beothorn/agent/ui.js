
    // If data file is not loaded data is undefined
    // check if data is a declared variable
    if (typeof data === 'undefined') {
        const error = document.createElement("h1");
        error.textContent = "No data loaded. Maybe nothing matches the filters?";
        document.body.appendChild(error);
    }

    const argumentsField = document.getElementById("arguments");
    argumentsField.innerHTML = executionMetadata;

    const firstSnapshot = document.getElementById("startTimestamp");
    const firstSnapshotTimestamp = data[0][0].snapshotTime;
    const firstSnapshotDate = new Date(firstSnapshotTimestamp);
    const firstSnapshotTimestampFormatted =
        `${firstSnapshotDate.getFullYear()}-`+
        `${firstSnapshotDate.getMonth()+1}-`+
        `${firstSnapshotDate.getDate()} `+
        `${firstSnapshotDate.getHours()}:`+
        `${firstSnapshotDate.getMinutes()}:`+
        `${firstSnapshotDate.getSeconds()}.`+
        `${firstSnapshotDate.getMilliseconds()}`;
    firstSnapshot.appendChild(document.createTextNode(firstSnapshotTimestampFormatted));

    const finalSnapshot = document.getElementById("finalTimestamp");
    const finalSnapshotTimestamp = data[data.length-1][0].snapshotTime;

    const lastSnapshot = document.getElementById("finalTimestamp");
    const lastSnapshotTimestamp = data[data.length-1][0].snapshotTime;
    const lastSnapshotDate = new Date(lastSnapshotTimestamp);
    const lastSnapshotTimestampFormatted =
        `${lastSnapshotDate.getFullYear()}-`+
        `${lastSnapshotDate.getMonth()+1}-`+
        `${lastSnapshotDate.getDate()} `+
        `${lastSnapshotDate.getHours()}:`+
        `${lastSnapshotDate.getMinutes()}:`+
        `${lastSnapshotDate.getSeconds()}.`+
        `${lastSnapshotDate.getMilliseconds()}`;
    lastSnapshot.appendChild(document.createTextNode(lastSnapshotTimestampFormatted));

    const charts = document.getElementById("charts");

    // join data and filter here
    const mergedData = mergeSnapshots(data);

    const dataForGraph = increaseZeroValues(mergedData);

function showNode(id, n) {
    console.log(n);
    document.getElementById(id).innerHTML = `<pre>{
    name: ${n.node.name},
    entryTime: ${n.node.entryTime},
    exitTime: ${n.node.exitTime},
    value": ${n.node.value},
    arguments: ${JSON.stringify(n.node.arguments)},
}</pre>`;
}

    function buildGraph(dataToPlot){
        for(let i = 0; i < dataToPlot.length; i++){
            const box = document.createElement("div");
            box.classList.add("box");
            box.classList.add("smallBox");


            const header = document.createElement("div");

            box.id = "chartBox"+i;
            const threadName = document.createElement("h1");
            threadName.classList.add("threadName");
            threadName.appendChild(document.createTextNode(dataToPlot[i].thread));

            const filter = document.createElement("div");
            filter.id = "filterBox"+i;
            filter.innerHTML = `<input type="text"
                id="filterInput${i}"
                placeholder="span.return && span.return.value === '[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]'" />
                <button id="filterButton${i}">Filter</button>`;

            // Flamegraph
            const showGraph = document.createElement("button");
            showGraph.appendChild(document.createTextNode("Show Call Graph"));
            showGraph.classList.add("topButton");
            const flamegraphByChildrenCountContainer = document.createElement("div");
            flamegraphByChildrenCountContainer.classList.add("container");
            flamegraphByChildrenCountContainer.style.display = "none";
            let flamegraphByChildrenCount;
            showGraph.addEventListener("click", () => {
                if (!flamegraphByChildrenCount) {
                    flamegraphByChildrenCountContainer.style.display = "block";
                    flamegraphByChildrenCount = document.createElement("div");
                    flamegraphByChildrenCount.id = "flamegraphByChildrenCount"+i;
                    flamegraphByChildrenCount.classList.add("flamegraph");
                    const labelByChildrenCount = document.createElement("h2");
                    labelByChildrenCount.appendChild(document.createTextNode("CallGraph"));
                    flamegraphByChildrenCountContainer.appendChild(labelByChildrenCount);
                    flamegraphByChildrenCountContainer.appendChild(flamegraphByChildrenCount);
                    const flameGraphNodeOutput = document.createElement("div");
                    flameGraphNodeOutput.id = "flameGraphNodeOutput"+i;
                    flamegraphByChildrenCountContainer.appendChild(flameGraphNodeOutput);

                    loadData({
                        elementId: flamegraphByChildrenCount.id,
                        data: dataToPlot[i].span,
                        graphType: "ChildrenCallCount",
                        colorPalette: ["#FFBB5C", "#FF9B50", "#E25E3E", "#C63D2F"],
                        zoomOnClick: true,
                        onClick: (n) => showNode(flameGraphNodeOutput.id, n),
                        tooltip: (n) => `<div class="tooltip">${n.name}</div>`
                    });

                    flamegraphByChildrenCountContainer.style.display = "none";
                }

                if (flamegraphByChildrenCountContainer.style.display === "block") {
                    flamegraphByChildrenCountContainer.style.display = "none";
                    showGraph.textContent = "Show Call Graph";
                } else {
                    flamegraphByChildrenCountContainer.style.display = "block";
                    showGraph.textContent = "Hide Call Graph";
                }
            });

            // Execution Time

            const showExecTime = document.createElement("button");
            showExecTime.appendChild(document.createTextNode("Show Execution Time"));
            showExecTime.classList.add("topButton");
            const flamegraphByTimestampContainer = document.createElement("div");
            flamegraphByTimestampContainer.classList.add("container");
            flamegraphByTimestampContainer.style.display = "none";
            let flamegraphByTimestamp;
            showExecTime.addEventListener("click", () => {
                if (!flamegraphByTimestamp) {
                    flamegraphByTimestampContainer.style.display = "block";
                    flamegraphByTimestamp = document.createElement("div");
                    flamegraphByTimestamp.id = "flamegraphByTimestamp"+i;
                    flamegraphByTimestamp.classList.add("flamegraph");
                    const labelByTimestamp = document.createElement("h2");
                    const rootTime = dataToPlot[i].span.exitTime - dataToPlot[i].span.entryTime;
                    if (rootTime === 0) {
                        labelByTimestamp.appendChild(document.createTextNode(`Execution Time took less than 1ms`));
                    } else {
                        labelByTimestamp.appendChild(document.createTextNode(`Execution Time ${rootTime}ms (Values with 1ms or more)`));
                    }
                    flamegraphByTimestampContainer.appendChild(labelByTimestamp);
                    flamegraphByTimestampContainer.appendChild(flamegraphByTimestamp);
                    const byTimestampNodeOutput = document.createElement("div");
                    byTimestampNodeOutput.id = "byTimestampNodeOutput"+i;
                    flamegraphByTimestampContainer.appendChild(byTimestampNodeOutput);


                    loadData({
                        elementId: flamegraphByTimestamp.id,
                        data: dataToPlot[i].span,
                        graphType: "Timestamp",
                        colorPalette: ["#FFBB5C", "#FF9B50", "#E25E3E", "#C63D2F"],
                        zoomOnClick: true,
                        onClick: (n) => showNode(byTimestampNodeOutput.id, n),
                        tooltip: (n) => `<div class="tooltip">Execution time(millis): ${n.node.exitTime - n.node.entryTime}<br>${n.name}</div>`
                    });

                    flamegraphByTimestampContainer.style.display = "none";
                }

                if (flamegraphByTimestampContainer.style.display === "block") {
                    flamegraphByTimestampContainer.style.display = "none";
                    showExecTime.textContent = "Show Execution Time";
                } else {
                    flamegraphByTimestampContainer.style.display = "block";
                    showExecTime.textContent = "Hide Execution Time";
                }

            });

            // ---------------------------

            header.appendChild(threadName);
            header.appendChild(filter);
            header.appendChild(showGraph);
            header.appendChild(showExecTime);
            box.appendChild(header);
            box.appendChild(flamegraphByChildrenCountContainer);
            box.appendChild(flamegraphByTimestampContainer);
            charts.appendChild(box);
        }
    }

    buildGraph(dataForGraph);

    //filterDataByLambda(subject, filterString)
    const filterButton = document.getElementById("filterButton");
    filterButton.addEventListener("click", () => {
        document.getElementById("charts").innerHTML = '';
        const filterString = document.getElementById("filterInput").value;
        const filtered = filterDataByLambda(dataForGraph, filterString);
        buildGraph(filtered);
    });
