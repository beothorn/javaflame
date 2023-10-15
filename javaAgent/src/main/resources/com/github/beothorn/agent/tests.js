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
            {
                "thread": "main",
                "snapshotTime": 0,
                "span": {
                    "name": "functionMain",
                    "entryTime":0,
                    "exitTime":-1, // -1 means that the span has not exited.
                    "value":0, // while not finished, the value is 0. value is duration.
                    "children":[
                        {
                            "name": "functionA",
                            "entryTime":0,
                            "exitTime":-1, // -1 means that the span has not exited.
                            "value":0,
                            "children":[
                                {
                                    "name": "functionAA",
                                    "entryTime":0,
                                    "exitTime":100, // >-1 means that the span has exited.
                                    "value":100,
                                }
                            ]
                        }
                    ]
                }
            },{
                "thread": "main",
                "snapshotTime": 101,
                "span": {
                    "name": "functionMain",
                    "entryTime":0,
                    "exitTime":-1, // program will always exit with the main function unfinished.
                    "value":0,
                    "children":[
                        {
                            "name": "functionA",
                            "entryTime":123, // same as the previous entry time, so still executing functionA.
                            "exitTime":1, // 1 means that the span has exited.
                            "value":1,
                            "children":[
                                {
                                    "name": "functionAB",
                                    "entryTime":100,
                                    "exitTime":250, // >-1 means that the span has exited.
                                    "value":150,
                                }
                            ]
                        }
                    ]
                }
            },
        ];

        const expected = [
            {
                "thread": "main",
                "snapshotTime": 101, // snapshot 101 is the same as the last snapshot.
                "span": {
                    "name": "functionMain",
                    "entryTime":0,
                    "exitTime":250, // exit time is the same as the last child exit time.
                    "value":250, // exit time minus entry time
                    "children":[
                        {
                            "name": "functionA",
                            "entryTime":0,
                            "exitTime":250, // exit time is the same as the last child exit time.
                            "value":250,
                            "children":[
                                {
                                    "name": "functionAA",
                                    "entryTime":0,
                                    "exitTime":100,
                                    "value":100,
                                },{
                                    "name": "functionAB",
                                    "entryTime":100,
                                    "exitTime":250,
                                    "value":150,
                                },
                            ]
                        }
                    ]
                }
            }
        ];
        
        assert.deepEquals("Merged snapshots", expected, mergeSnapshots(data));
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
    deepEquals: (msg, actual, expected) => {
        if(JSON.stringify(actual) !== JSON.stringify(expected)){
            const outputMsg = `${msg} is not equal to ${JSON.stringify(expected)}`;
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
