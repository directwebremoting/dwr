
var count = 0;
var working = false;
var startTime = null;

function init() {
  DWRUtil.useLoadingMessage("Ping");

  DWREngine.setErrorHandler(function(message) {
    report("Global Error Handler: " + message);
  });

  DWREngine.setWarningHandler(function(message) {
    report("Global Warning Handler: " + message);
  });

  update();
}

function toggleTest() {
  if (working) {
    DWRUtil.setValue("toggle", "Start");
    working = false;
  }
  else {
    DWRUtil.setValue("toggle", "Stop");
    working = true;
  }
}

function update() {
  count++;

  if (working) {

    startTime = new Date().getTime();
    DWREngine.beginBatch();

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

    DWREngine.endBatch({
      errorHandler:function(message) {
        report("Batch Error Handler: " + message);
      },
      warningHandler:function(message) {
        report("Batch Warning Handler: " + message);
      }
    });
  }

  DWRUtil.setValue("heartMonitor", count);
  setTimeout(update, 1000);
}

function report(message) {
  var length = new Date().getTime() - startTime;
  var ele = $("status");
  ele.options[ele.options.length] = new Option("" + count + ": " + message + " (" + length + "ms)");
}
