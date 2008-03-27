
function init() {
  callOnLoad(dwr.util.useLoadingMessage);
}

function failure(message) {
  alert(message);
}

function success(message) {
}

function testXML() {
  XOM.getDocument("textXOM", function(data) {
    dwr.util.setValue("replyXOM", data);
    XOM.debugDocument($("replyXOM"), function(data) {
      if (data < 30 || data > 40) {
        failure("XOM reply fail: " + data);
      }
      else {
        success();
      }
    });
  });

  JDOM.getDocument("textJDOM", function(data) {
    dwr.util.setValue("replyJDOM", data);
    JDOM.debugDocument($("replyJDOM"), function(data) {
      if (data < 30 || data > 40) {
        failure("JDOM reply fail: " + data);
      }
      else {
        success();
      }
    });
  });

  DOM.getDocument("textDOM", function(data) {
    dwr.util.setValue("replyDOM", data);
    DOM.debugDocument($("replyDOM"), function(data) {
      if (data < 30 || data > 40) {
        failure("DOM reply fail: " + data);
      }
      else {
        success();
      }
    });
  });

  DOM4J.getDocument("textDOM4J", function(data) {
    dwr.util.setValue("replyDOM4J", data);
    DOM4J.debugDocument($("replyDOM4J"), function(data) {
      if (data < 30 || data > 40) {
        failure("DOM4J reply fail: " + data);
      }
      else {
        success();
      }
    });
  });
}
