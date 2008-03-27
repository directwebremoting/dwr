
function init() {
  var prop;
  for (prop in window) {
    if (prop.match(/test/) && typeof window[prop] == "function" && prop != "testEquals") {
      dwr.util.addOptions("buttons", [ prop ], function(data) {
        return "<button onclick='dwr.util.setValue(\"output\", \"\"); " + prop + "()'>" + prop.substring(4) + "</button>";
        // return "<input name='submit' type='image' src='imagebutton.png' onclick='dwr.util.setValue(\"output\", \"\"); " + prop + "()' />";
      });
    }
  }
}

function success(message) {
  var output = $('output').innerHTML + "<br/>\n" + message;
  dwr.util.setValue('output', output);
}

function failure(message) {
  alert(message);
}

function testError1() {
  var counter = 0;
  var exceptionHandler = function(message, ex) {
    if (message == null) failure("message is null");
    else if (ex == null) failure("ex is null");
    else if (ex.message == null) failure("ex.message is null");
    else if (ex.message != message) failure("ex.message [" + ex.message + "] != message [" + message + "]");
    else if (ex.name != "java.lang.NullPointerException") failure("ex.name is not NPE [" + ex.name + "]");
    else counter++;
  };
  var warningHandler = function(data) {
    failure("Wrong call of warningHandler with data: " + data);
  };
  var callback = function(data) {
    failure("Wrong call of callback with data: " + data);
  };
  var errorHandler = function(data) {
    failure("Wrong call of errorHandler with data: " + data);
  };
  var callData = {
    callback:callback,
    errorHandler:errorHandler,
    warningHandler:warningHandler,
    exceptionHandler:exceptionHandler
  };

  callData.httpMethod = "POST";
  callData.rpcType = dwr.engine.XMLHttpRequest;
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.IFrame;
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.ScriptTag;
  Test.throwNPE(callData);

  callData.httpMethod = "GET";
  callData.rpcType = dwr.engine.XMLHttpRequest;
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.IFrame;
  Test.throwNPE(callData);

  callData.rpcType = dwr.engine.ScriptTag;
  callData.exceptionHandler = function(message, ex) {
    exceptionHandler(message, ex);
    if (counter != 5) failure("Some errors did not complete (Sync issue?) counter = " + counter);
    else success("Error test passed");
  };
  Test.throwNPE(callData);
}

function testAsyncNesting() {
  Test.slowStringParam("AsyncNesting-2", 100, function(data1) {
    success(data1);
    Test.slowStringParam("AsyncNesting-End", 200, function(data2) {
      success(data2);
    });
    success("AsyncNesting-3");
  });
  success("AsyncNesting-1");
}

function testSyncNesting() {
  dwr.engine.setAsync(false);
  Test.slowStringParam("SyncNesting-1", 100, function(data1) {
    success(data1);
    Test.slowStringParam("SyncNesting-2", 100, function(data2) {
      success(data2);
    });
    success("SyncNesting-3");
  });
  success("SyncNesting-End");
  dwr.engine.setAsync(true); 
}

function testSyncInCallMetaData() {
  Test.slowStringParam("SyncInCallMetaData-1", 100, {
    async:false,
    callback:function(data) {
      success(data);
    }
  });
  success("SyncInCallMetaData-End");
}

function testAsyncInCallMetaData() {
  Test.slowStringParam("AsyncInCallMetaData-End", 100, {
    async:true,
    callback:function(data) {
      success(data);
    }
  });
  success("AsyncInCallMetaData-1");
}

function testDefaultAsyncInCallMetaData() {
  Test.slowStringParam("DefaultAsyncInCallMetaData-End", 100, {
    callback:function(data) {
      success(data);
    }
  });
  success("DefaultAsyncInCallMetaData-1");
}
