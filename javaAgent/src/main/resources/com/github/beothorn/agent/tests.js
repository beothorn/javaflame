const tests = {
    "Simple data merge": (assert) => {
        const data = [
            
        ];
        
        assert.isTrue("Message", true);
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