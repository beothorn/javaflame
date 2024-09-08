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
const mergedData = prepareData(data);

const dataForGraph = increaseZeroValues(mergedData);

function showNode(id, n) {
    let jsonString = "";
    console.log(n);
    for (const property in n) {
        if (property === "children") {
            jsonString += `<p> immediate children count: ${n["children"].length}</p>`;
        } else if  (property === "parent") {
            const parentSearchResult = searchId(n["parent"]);
            const parentName = (parentSearchResult && parentSearchResult.length > 0) ? parentSearchResult[0].name : 'n/a';
            jsonString += `<p> span.parentId: ${n["parent"]}</p>`;
            jsonString += `<p> span.parent: ${parentName}</p>`;
        } else {
            jsonString += `<p> span.${property}: ${JSON.stringify(n[property])}</p>`;
        }
    }
    document.getElementById(id).innerHTML = jsonString;
}

function buildVisualizationButton(text) {
    const newButton = document.createElement("button");
    newButton.appendChild(document.createTextNode(text));
    newButton.classList.add("topButton");
    newButton.classList.add("notSelectedButton");
    return newButton;
}

function buildVisualizationContainer() {
    const container = document.createElement("div");
    container.classList.add("container");
    container.style.display = "none";
    return container;
}

let id = 0;
function buildVisualization(visualizationContainer, title) {
    const label = document.createElement("h2");
    label.appendChild(document.createTextNode(title));
    visualizationContainer.appendChild(label);

    visualizationContainer.style.display = "block";
    let visualization = document.createElement("div");
    visualization.id = "visual"+id;
    id = id + 1;
    visualization.classList.add("flamegraph");
    visualizationContainer.appendChild(visualization);

    const extraDetailsOutput = document.createElement("div");
    extraDetailsOutput.id = "extraDetails"+id;
    visualizationContainer.appendChild(extraDetailsOutput);
    return {
        label,
        visualization,
        extraDetailsOutput
    };
}

function toggleVisualization(button, container, showText, hideText) {
    if (container.style.display === "block") {
        container.style.display = "none";
        button.textContent = showText;
        button.classList.remove("selectedButton");
        button.classList.add("notSelectedButton");
    } else {
        container.style.display = "block";
        button.textContent = hideText;
        button.classList.remove("notSelectedButton");
        button.classList.add("selectedButton");
    }
}

function addVisualization(config) {
    const button = buildVisualizationButton(config.turnOnText);
    const container = buildVisualizationContainer();
    let loadedVisualization;
    button.addEventListener("click", () => {
        if (!loadedVisualization) {
            const {
                label,
                visualization,
                extraDetailsOutput
            } = buildVisualization(container, config.title);
            loadedVisualization = visualization;

            config.loadVisualization(
                label,
                loadedVisualization,
                extraDetailsOutput
            );

            container.style.display = "none";
        }

        toggleVisualization(
            button,
            container,
            config.turnOnText,
            config.turnOffText
        );
    });

    return {
        button,
        container
    };
}

function appendChildrenOnHierarchicalView(liParent, children) {
    if (!children) return;
    const unorderedList = document.createElement("ul");
    unorderedList.classList.add("nested");
    for (let c of children) {
        const listItem = document.createElement("li");
        let args = "";
        if (c.arguments) {
            for (let arg of c.arguments) {
                args+= arg.type +" "+ arg.value+", ";
            }
        }
        const returnValue = (c.return) ? (c.return.type +" "+ c.return.value) : "no_return";
        const listItemName = document.createElement("span");
        listItemName.appendChild(document.createTextNode(c.className+"."+c.method+"("+args+") => "+returnValue));
        if (c.children) {
            listItemName.classList.add("caret");
            listItemName.addEventListener("click", () => {
                // maybe lazy load children here instead
                listItemName.classList.toggle("caret-down");
                listItemName.parentElement.querySelector(".nested").classList.toggle("active");
            });
        }
        listItem.appendChild(listItemName);

        appendChildrenOnHierarchicalView(listItem, c.children);

        unorderedList.appendChild(listItem);
    }
    liParent.appendChild(unorderedList);
}

function createHierarchicalView(treeData, visualizationDiv) {
    const unorderedList = document.createElement("ul");

    const listItem = document.createElement("li");

    const span = treeData.span;
    const listItemName = document.createElement("span");
    listItemName.classList.add("caret");
    listItemName.appendChild(document.createTextNode(span.className));
    listItemName.addEventListener("click", () => {
       listItemName.classList.toggle("caret-down");
       listItemName.parentElement.querySelector(".nested").classList.toggle("active");
    });
    listItem.appendChild(listItemName);
    unorderedList.appendChild(listItem);

    appendChildrenOnHierarchicalView(listItem, span.children);

    visualizationDiv.appendChild(unorderedList);
}

function buildGraph(dataToPlot){
    const colorPalette = ["#900C3F","#C70039","#F94C10","#F8DE22"]; // TODO: light mode palette, change with theme
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
        let byTimestampNodeOutput = {};
        const {
            button: flamegraphByChildrenButton,
            container: flamegraphByChildrenCountContainer
        } = addVisualization({
            turnOnText: "Show Call Graph",
            turnOffText: "Hide Call Graph",
            title: "CallGraph",
            loadVisualization: (
                label,
                visualization,
                extraDetailsOutput
            ) => {
                flameGraphNodeOutput = extraDetailsOutput;
                loadData({
                    elementId: visualization.id,
                    data: dataToPlot[i].span,
                    graphType: "ChildrenCallCount",
                    colorPalette,
                    zoomOnClick: true,
                    onClick: (n) => showNode(extraDetailsOutput.id, n.node),
                    tooltip: (n) => `<div class="tooltip">${n.name}</div>`
                });
            }
        });

        // Execution Time
        const rootTime = dataToPlot[i].span.exitTime - dataToPlot[i].span.entryTime;
        const execTimeTitle = (rootTime === 0)?`Execution Time took less than 1ms`:`Execution Time ${rootTime}ms (Values with 1ms or more)`;

        let flameGraphNodeOutput = {};
        const {
            button: showExecTimeButton,
            container: flamegraphByTimestampContainer
        } = addVisualization({
            turnOnText: "Show Execution Time",
            turnOffText: "Hide Execution Time",
            title: execTimeTitle,
            loadVisualization: (
                label,
                visualization,
                extraDetailsOutput
            ) => {
                byTimestampNodeOutput = extraDetailsOutput;
                loadData({
                    elementId: visualization.id,
                    data: dataToPlot[i].span,
                    graphType: "Timestamp",
                    colorPalette,
                    zoomOnClick: true,
                    onClick: (n) => showNode(extraDetailsOutput.id, n.node),
                    tooltip: (n) => `<div class="tooltip">Execution time(millis): ${n.node.exitTime - n.node.entryTime}<br>${n.name}</div>`
                });
            }
        });

        // Hierarchical mode
        const {
            button: rawVisualizationButton,
            container: rawVisualizationContainer
        } = addVisualization({
            turnOnText: "Show Hierarchical",
            turnOffText: "Hide Hierarchical",
            title: "Hierarchical",
            loadVisualization: (
                label,
                visualization,
                extraDetailsOutput
            ) => {
                visualization.classList.add("hierarchicalView");
                createHierarchicalView(dataToPlot[i], visualization);
            }
        });

        // ---------------------------

        const doFilter = () => {
            const filterString = filterInput.value;
                const filtered = filterDataByLambda(dataForGraph, dataToPlot[i].thread, filterString);

                const oldFlameGraphByTime = document.getElementById("visual"+i);
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

                const oldFlameGraphByCallCount = document.getElementById("visual"+i);
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
        }

        filterInput.addEventListener('keydown', (event) => {
            if (event.key === 'Enter') {
                doFilter();
            }
        });
        filterButton.addEventListener("click", doFilter);


        headerLeft.appendChild(threadName);
        headerLeft.appendChild(filter);

        // Buttons
        headerRight.appendChild(rawVisualizationButton);
        headerRight.appendChild(flamegraphByChildrenButton);
        headerRight.appendChild(showExecTimeButton);

        box.appendChild(header);
        // containers,
        box.appendChild(rawVisualizationContainer);
        box.appendChild(flamegraphByChildrenCountContainer);
        box.appendChild(flamegraphByTimestampContainer);

        charts.appendChild(box);
    }
}

buildGraph(dataForGraph);

function createReverseHierarchyEntryListItem(entry) {
    const listItem = document.createElement("li");
    const listItemName = document.createElement("span");
    if (entry.parent !== '') {
        listItemName.classList.add("caret");
        listItemName.addEventListener("click", () => {
            console.log(entry);
            if (listItemName.parentElement.querySelector(".nested")) {
                listItemName.parentElement.querySelector(".nested").classList.toggle("active");
            } else {
                const parentNode = searchId(entry.parent);
                if(!parentNode) {
                    console.error(`${entry.parent} id not found! Parent is not found!`);
                    return;
                }
                const newLI = createReverseHierarchyEntryListItem(parentNode); 
                const newUnorderedList = document.createElement("ul");
                newUnorderedList.classList.add("nested");
                newUnorderedList.appendChild(newLI);    
                newUnorderedList.classList.toggle("active");
                listItem.appendChild(newUnorderedList);
            }
            listItemName.classList.toggle("caret-down");
        });
    }
    listItemName.appendChild(document.createTextNode(`${entry.thread}::${entry.name}`));
    listItem.appendChild(listItemName);
    return listItem;
}

function createReverseHierarchyView(entries, visualizationDiv) {
    const unorderedList = document.createElement("ul");

    for(let entry of entries) {
        const listItem = createReverseHierarchyEntryListItem(entry);
        unorderedList.appendChild(listItem);
    }

    visualizationDiv.appendChild(unorderedList);
}

function doSearch() {
    const searchInputValue = document.getElementById("searchInput").value;
    if (searchInputValue === '') {
        document.getElementById("searchResult").innerHTML = 'This will return all nodes, please improve your search.';
        return;
    }
    const result = searchString(searchInputValue);
    document.getElementById("searchResult").innerHTML = '';
    createReverseHierarchyView(result, document.getElementById("searchResult"));
}

document.getElementById("searchButton").addEventListener("click", doSearch);

document.getElementById('searchInput').addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        doSearch();
    }
});