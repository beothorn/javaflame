const tests = {
    "Simple data merge": (assert) => {
        /*
        If exit time is -1, it means that the span has not exited.
        The main function never exits, so the exit time is -1.
        This is an execution where the functionMain calls functionA.
        Then 2 snapshots are taken.
        Then functionA end and functionA is called again.
        */
        const data = [
            [
                {
                    "thread": "main",
                    "snapshotTime": 0,
                    "span": {
                        "id": "1",
                        "name": "functionMain",
                        "entryTime":1000,
                        "exitTime":-1, // -1 means that the span has not exited.
                        "value":0, // while not finished, the value is 0. value is duration.
                        "children":[
                            {
                                "id": "2",
                                "name": "functionA",
                                "entryTime":1000,
                                "exitTime":-1, // -1 means that the span has not exited.
                                "value":0,
                                "children":[
                                    {
                                        "id": "3",
                                        "name": "functionAA",
                                        "entryTime":1000,
                                        "exitTime":1100, // >-1 means that the span has exited.
                                        "value":100,
                                    }
                                ]
                            }
                        ]
                    }
                }
            ],[
                {
                    "thread": "main",
                    "snapshotTime": 1101,
                    "span": {
                        "id": "1",
                        "name": "functionMain",
                        "entryTime":1000,
                        "exitTime":-1, // program will always exit with the main function unfinished.
                        "value":0,
                        "children":[
                            {
                                "id": "2",
                                "name": "functionA",
                                "entryTime":1000, // same as the previous entry time, so still executing functionA.
                                "exitTime":1250, // >1 means that the span has exited.
                                "value":250,
                                "children":[
                                    {
                                        "id": "4",
                                        "name": "functionAB",
                                        "entryTime":1100,
                                        "exitTime":1250, // >-1 means that the span has exited.
                                        "value":150,
                                    }
                                ]
                            }
                        ]
                    }
                },
            ]
        ];

        const expected = [
            {
                "thread": "main",
                "snapshotTime": 1101, // snapshot 1101 is the same as the last snapshot.
                "span": {
                    "id": "1",
                    "name": "functionMain",
                    "entryTime":1000,
                    "exitTime":1250, // exit time is the same as the last child exit time.
                    "value":250, // exit time minus entry time
                    "children":[
                        {
                            "id": "2",
                            "name": "functionA",
                            "entryTime":1000,
                            "exitTime":1250, // exit time is the same as the last child exit time.
                            "value":250,
                            "children":[
                                {
                                    "id": "3",
                                    "name": "functionAA",
                                    "entryTime":1000,
                                    "exitTime":1100,
                                    "value":100,
                                },{
                                    "id": "4",
                                    "name": "functionAB",
                                    "entryTime":1100,
                                    "exitTime":1250,
                                    "value":150,
                                },
                            ]
                        }
                    ]
                }
            }
        ];
        
        assert.deepEquals("Merged snapshots", expected, mergeSnapshots(data));
    },
    "Merge childen": (assert) => {
        const actual = mergeChildren([{
            "name": "functionMain",
            "entryTime":0,
            "exitTime":50,
            "value":0
        }], [{
            "name": "functionMain",
            "entryTime":0,
            "exitTime":200,
            "value":150
        }]);

        const expected = [{
            "name": "functionMain",
            "entryTime":0,
            "exitTime":200,
            "value":150
        }];
        
        assert.deepEquals("Merged children", expected, actual);
    },
    "Merge of two snapshots with empty children": (assert) => {
        const actual = mergeChildren([{
            "name": "functionMain",
            "entryTime":0,
            "exitTime":50,
            "value":50
        }], []);

        const expected = [{
            "name": "functionMain",
            "entryTime":0,
            "exitTime":50,
            "value":50
        }];
        
        assert.deepEquals("Merged children", expected, actual);
    },
    "No zero valued entries": (assert) => {
        const subject = [
            {
                "thread": "main",
                "snapshotTime": 1000,
                "span": {
                    "name": "functionMain",
                    "entryTime":1000,
                    "exitTime":1000,
                    "value":0,
                    "children":[
                        {
                            "name": "functionA",
                            "entryTime":1000,
                            "exitTime":1000,
                            "value":0,
                            "children":[
                                {
                                    "name": "functionAA",
                                    "entryTime":1000,
                                    "exitTime":1000,
                                    "value":0,
                                },{
                                    "name": "functionAB",
                                    "entryTime":1000,
                                    "exitTime":1000,
                                    "value":0,
                                },
                            ]
                        }
                    ]
                }
            }
        ];

        const expected = [
            {
                "thread": "main",
                "snapshotTime": 1000,
                "span": {
                    "name": "functionMain",
                    "entryTime":1000,
                    "exitTime":1000,
                    "value":2,
                    "children":[
                        {
                            "name": "functionA",
                            "entryTime":1000,
                            "exitTime":1000,
                            "value":2,
                            "children":[
                                {
                                    "name": "functionAA",
                                    "entryTime":1000,
                                    "exitTime":1000,
                                    "value":1,
                                },{
                                    "name": "functionAB",
                                    "entryTime":1000,
                                    "exitTime":1000,
                                    "value":1,
                                },
                            ]
                        }
                    ]
                }
            }
        ];

        assert.deepEquals("Data with replaced values", expected, increaseZeroValues(subject));
    },
    "Filter entries": (assert) => {
        const subject = [
            {
                "thread": "main",
                "snapshotTime": 1000,
                "span": {
                    "name": "functionMain",
                    "entryTime":1000,
                    "exitTime":1000,
                    "value":0,
                    "children":[
                        {
                            "name": "functionA",
                            "entryTime":1000,
                            "exitTime":1000,
                            "value":0,
                            "children":[
                                {
                                    "name": "functionAA",
                                    "entryTime":1000,
                                    "exitTime":1000,
                                    "value":0,
                                    "return": {
                                        "type": "int",
                                        "value": 99
                                    }
                                },{
                                    "name": "functionAB",
                                    "entryTime":1000,
                                    "exitTime":1000,
                                    "value":0,
                                    "return": {
                                        "type": "String",
                                        "value": "ababa"
                                    }
                                },
                            ]
                        },
                        {
                            "name": "functionShouldBeFiltered",
                            "entryTime":1000,
                            "exitTime":1000,
                            "value":0,
                        }
                    ]
                }
            },{
                "thread": "keepIt",
                "snapshotTime": 1000,
                "span": {
                    "name": "functionKeep",
                    "entryTime":1000,
                    "exitTime":1000,
                    "value":0,
                }
            },
        ];

        const expected = [
            {
                "thread": "main",
                "snapshotTime": 1000,
                "span": {
                    "name": "functionMain",
                    "entryTime":1000,
                    "exitTime":1000,
                    "value":0,
                    "children":[
                        {
                            "name": "functionA",
                            "entryTime":1000,
                            "exitTime":1000,
                            "value":0,
                            "children":[
                                {
                                    "name": "functionAB",
                                    "entryTime":1000,
                                    "exitTime":1000,
                                    "value":0,
                                    "return": {
                                        "type": "String",
                                        "value": "ababa"
                                    }
                                },
                            ]
                        }
                    ]
                }
            },{
                "thread": "keepIt",
                "snapshotTime": 1000,
                "span": {
                    "name": "functionKeep",
                    "entryTime":1000,
                    "exitTime":1000,
                    "value":0,
                }
            }
        ];

        const filterString = 'span.return && span.return.type === "String" && span.return.value === "ababa"';

        assert.deepEquals("Filtered values with no change", subject, filterDataByLambda(subject, "main", ""));
        assert.deepEquals("Filtered values", expected, filterDataByLambda(subject, "main", filterString));
    },
    "Enrich node info": (assert) => {
        const actual = [{
            "thread": "main",
            "snapshotTime": 1101, // snapshot 1101 is the same as the last snapshot.
            "span": {
                "id": "1",
                "name": "functionMain",
                "entryTime":1000,
                "exitTime":1250, // exit time is the same as the last child exit time.
                "value":250, // exit time minus entry time
                "children":[
                    {
                        "id": "2",
                        "name": "functionA",
                        "entryTime":1000,
                        "exitTime":1250, // exit time is the same as the last child exit time.
                        "value":250,
                        "children":[
                            {
                                "id": "3",
                                "name": "functionAA",
                                "entryTime":1000,
                                "exitTime":1100,
                                "value":100,
                            },{
                                "id": "4",
                                "name": "functionAB",
                                "entryTime":1100,
                                "exitTime":1250,
                                "value":150,
                            },
                        ]
                    }
                ]
            }
        }];
        enrichNodes(actual);
        const expected = [{
            "thread": "main",
            "snapshotTime": 1101, // snapshot 1101 is the same as the last snapshot.
            "span": {
                "id": "1",
                "parent": "",
                "thread": "main",
                "name": "functionMain",
                "entryTime":1000,
                "exitTime":1250, // exit time is the same as the last child exit time.
                "value":250, // exit time minus entry time
                "children":[
                    {
                        "id": "2",
                        "parent": "1",
                        "thread": "main",
                        "name": "functionA",
                        "entryTime":1000,
                        "exitTime":1250, // exit time is the same as the last child exit time.
                        "value":250,
                        "children":[
                            {
                                "id": "3",
                                "parent": "2",
                                "thread": "main",
                                "name": "functionAA",
                                "entryTime":1000,
                                "exitTime":1100,
                                "value":100,
                            },{
                                "id": "4",
                                "parent": "2",
                                "thread": "main",
                                "name": "functionAB",
                                "entryTime":1100,
                                "exitTime":1250,
                                "value":150,
                            },
                        ]
                    }
                ]
            }
        }];
        assert.deepEquals("Enriched noses", expected, actual);
    },
};

// Test logic

function out(str, color){
    const out = document.getElementById("output");
    out.innerHTML += `<p style="color:${color}">${str}</p>`;    
}

function info(str) {
    out(str, "black");
}

function pass(str) {
    out(str, "green");
}

function failure(str) {
    out(str, "red");
}

info("Starting tests.");

function orderedObject(obj) {
    const sorter = (a, b) =>JSON.stringify(a).localeCompare(JSON.stringify(b));
    if (Array.isArray(obj)) {
        return obj.map(orderedObject).sort(sorter);
    }
    if (typeof obj === 'object') {
        const sortedKeys = Object.keys(obj).sort();
        return sortedKeys.reduce((newObj, key) => {
            newObj[key] = orderedObject(obj[key]);
            return newObj;
        },{});
    }
    return obj;
}

let assertObj = {
    isTrue: (msg, val) => {
        if(!val){
            const outputMsg = `${msg} is not true`;
            failure(outputMsg);
            throw new Error(outputMsg);
        }
    },
    deepEquals: (msg, expected, actual) => {
        const expectedOrdered = orderedObject(expected);
        const actualOrdered = orderedObject(actual);

        if(JSON.stringify(actualOrdered) !== JSON.stringify(expectedOrdered)){
            const outputMsg = `<p style="color:red">${msg} is not equal to expected.</p>
                <p style="color:red">Expected:</p>
                <p style="color:red">${JSON.stringify(expectedOrdered)}</p>
                <p style="color:red">Actual:</p>
                <p style="color:red">${JSON.stringify(actualOrdered)}</p>`;
            failure(`${msg} is not equal to expected.`);
            failure('<h3 style="color:red">Expected:</h3>');
            failure(JSON.stringify(expectedOrdered));
            failure('<h3 style="color:red">Actual:</h3>');
            failure(JSON.stringify(actualOrdered));
            throw new Error(outputMsg);
        }
    }
};

for (let testName of Object.keys(tests)) {
    try {
        document.getElementById("output").innerHTML += `<hr><h2>${testName}</h2>`;
        tests[testName](assertObj);
    } catch (error) {
        console.error(error);
        failure(`<h3 style="color:red">'${testName}' failed with error:</h3><p style="color:red">${error}</p>`);
        continue;
    }
    pass(`'${testName}' passed`);
}
