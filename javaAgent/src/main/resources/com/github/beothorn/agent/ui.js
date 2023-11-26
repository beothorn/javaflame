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
    let jsonString = "";
    for (const property in n) {
        jsonString += `<p> span.${property}: ${n[property]}</p>`;
    }
    document.getElementById(id).innerHTML = jsonString;
}

function buildGraph(dataToPlot){
    const colorPalette = ["#900C3F","#C70039","#F94C10","#F8DE22"];
    for(let i = 0; i < dataToPlot.length; i++){
        const box = document.createElement("div");
        box.id = "chartBox"+i;
        box.classList.add("box");
        box.classList.add("smallBox");

        const header = document.createElement("div");
        header.classList.add("boxHeader");

        const headerLeft = document.createElement("div");
        headerLeft.classList.add("headerLeft");
        const headerRight = document.createElement("div");
        headerRight.classList.add("headerRight");

        header.appendChild(headerLeft);
        header.appendChild(headerRight);

        const threadName = document.createElement("h1");
        threadName.classList.add("threadName");
        threadName.appendChild(document.createTextNode(dataToPlot[i].thread));

        const filter = document.createElement("div");
        filter.id = "filterBox"+i;
        filter.classList.add("filterBox");

        const filterInput = document.createElement("input");
        filterInput.id = "filterInput" + i;
        filterInput.type = "text";
        filterInput.placeholder = "ex: span.name.includes('sort') " 
        filter.appendChild(filterInput);

        const filterButton = document.createElement("button");
        filterButton.id = "filterButton" + i;
        filterButton.textContent = "Filter";
        filter.appendChild(filterButton);

        // Flamegraph
        const showGraph = document.createElement("button");
        showGraph.appendChild(document.createTextNode("Show Call Graph"));
        showGraph.classList.add("topButton");
        showGraph.classList.add("notSelectedButton");
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
                    colorPalette,
                    zoomOnClick: true,
                    onClick: (n) => showNode(flameGraphNodeOutput.id, n.node),
                    tooltip: (n) => `<div class="tooltip">${n.name}</div>`
                });

                flamegraphByChildrenCountContainer.style.display = "none";
            }

            if (flamegraphByChildrenCountContainer.style.display === "block") {
                flamegraphByChildrenCountContainer.style.display = "none";
                showGraph.textContent = "Show Call Graph";
                showGraph.classList.remove("selectedButton");
                showGraph.classList.add("notSelectedButton");
            } else {
                flamegraphByChildrenCountContainer.style.display = "block";
                showGraph.textContent = "Hide Call Graph";
                showGraph.classList.remove("notSelectedButton");
                showGraph.classList.add("selectedButton");
            }
        });

        // Execution Time

        const showExecTime = document.createElement("button");
        showExecTime.appendChild(document.createTextNode("Show Execution Time"));
        showExecTime.classList.add("topButton");
        showExecTime.classList.add("notSelectedButton");
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
                    colorPalette,
                    zoomOnClick: true,
                    onClick: (n) => showNode(byTimestampNodeOutput.id, n.node),
                    tooltip: (n) => `<div class="tooltip">Execution time(millis): ${n.node.exitTime - n.node.entryTime}<br>${n.name}</div>`
                });

                flamegraphByTimestampContainer.style.display = "none";
            }

            if (flamegraphByTimestampContainer.style.display === "block") {
                flamegraphByTimestampContainer.style.display = "none";
                showExecTime.textContent = "Show Execution Time";
                showExecTime.classList.remove("selectedButton");
                showExecTime.classList.add("notSelectedButton");
            } else {
                flamegraphByTimestampContainer.style.display = "block";
                showExecTime.textContent = "Hide Execution Time";
                showExecTime.classList.remove("notSelectedButton");
                showExecTime.classList.add("selectedButton");
            }

        });

        // ---------------------------

        filterButton.addEventListener("click", () => {
            const filterString = filterInput.value;
            const filtered = filterDataByLambda(dataForGraph, dataToPlot[i].thread, filterString);
            
            const oldFlameGraphByTime = document.getElementById("flamegraphByTimestamp"+i);
            if (oldFlameGraphByTime) {
                oldFlameGraphByTime.innerHTML = '';
                loadData({
                    elementId: oldFlameGraphByTime.id,
                    data: filtered[i].span,
                    graphType: "Timestamp",
                    colorPalette,
                    zoomOnClick: true,
                    onClick: (n) => showNode(byTimestampNodeOutput.id, n.node),
                    tooltip: (n) => `<div class="tooltip">Execution time(millis): ${n.node.exitTime - n.node.entryTime}<br>${n.name}</div>`      
                });
            } 
            
            const oldFlameGraphByCallCount = document.getElementById("flamegraphByChildrenCount"+i);
            if (oldFlameGraphByCallCount) {
                oldFlameGraphByCallCount.innerHTML = '';
                loadData({
                    elementId: oldFlameGraphByCallCount.id,
                    data: filtered[i].span,
                    graphType: "ChildrenCallCount",
                    colorPalette,
                    zoomOnClick: true,
                    onClick: (n) => showNode(flameGraphNodeOutput.id, n.node),
                    tooltip: (n) => `<div class="tooltip">${n.name}</div>`
                });
            }
        });

        headerLeft.appendChild(threadName);
        headerLeft.appendChild(filter);
        headerRight.appendChild(showGraph);
        headerRight.appendChild(showExecTime);
        box.appendChild(header);
        box.appendChild(flamegraphByChildrenCountContainer);
        box.appendChild(flamegraphByTimestampContainer);
        charts.appendChild(box);
    }
}

buildGraph(dataForGraph);
