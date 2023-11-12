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
            }
        ];

        const filterString = 'span.return && span.return.type === "String" && span.return.value === "ababa"';

        assert.deepEquals("Filtered values", expected, filterDataByLambda(subject, filterString));
    }
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


let assertObj = {
    isTrue: (msg, val) => {
        if(!val){
            const outputMsg = `${msg} is not true`;
            failure(outputMsg);
            throw new Error(outputMsg);
        }
    },
    deepEquals: (msg, expected, actual) => {
        if(JSON.stringify(actual) !== JSON.stringify(expected)){
            const outputMsg = `${msg} is not equal to expected. 
                expected: ${JSON.stringify(expected)}
                actual  : ${JSON.stringify(actual)}`;
            failure(outputMsg);
            throw new Error(outputMsg);
        }
    }
};

for (let testName of Object.keys(tests)) {
    try {
        info(`Running ${testName}`);
        tests[testName](assertObj);
    } catch (error) {
        failure(`'${testName}' failed with ${error}`);
        continue;
    }
    pass(`'${testName}' passed`);
}
