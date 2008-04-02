
var count = 0;
var working = false;
var startTime = null;

function init() {
  dwr.util.useLoadingMessage("Ping");

  dwr.engine.setErrorHandler(function(message) {
    report("Global Error Handler: " + message);
  });

  dwr.engine.setWarningHandler(function(message) {
    report("Global Warning Handler: " + message);
  });

  update();
}

function toggleTest() {
  if (working) {
    dwr.util.setValue("toggle", "Start");
    working = false;
  }
  else {
    dwr.util.setValue("toggle", "Stop");
    working = true;
  }
}

function update() {
  count++;

  if (working) {

    startTime = new Date().getTime();
    dwr.engine.beginBatch();

    Test.slowStringParam("ping", 500, {
      timeout:550,
      callback:function(data) {
        if (data == "ping") {
          report("Server replied OK");
        }
        else {
          report("Failure: Server returned: " + data);
        }
      },
      errorHandler:function(message) {
        report("Method Error Handler: " + message);
      },
      warningHandler:function(message) {
        report("Method Warning Handler: " + message);
      }
    });

    dwr.engine.endBatch({
      errorHandler:function(message) {
        report("Batch Error Handler: " + message);
      },
      warningHandler:function(message) {
        report("Batch Warning Handler: " + message);
      }
    });
  }

  dwr.util.setValue("heartMonitor", count);
  setTimeout(update, 1000);
}

function report(message) {
  var length = new Date().getTime() - startTime;
  var ele = $("status");
  ele.options[ele.options.length] = new Option("" + count + ": " + message + " (" + length + "ms)");
}
