
function testXOM() {
  dwr.util.setValue(currentTest.scratch, "<div id='replyXOM' class='xmlReply'> </div>", { escapeHtml:false });
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
}

function testJdom() {
  dwr.util.setValue(currentTest.scratch, "<div id='replyJDOM' class='xmlReply'> </div>", { escapeHtml:false });
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
}

function testDom() {
  dwr.util.setValue(currentTest.scratch, "<div id='replyDOM' class='xmlReply'> </div>", { escapeHtml:false });
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
}

function testDom4j() {
  dwr.util.setValue(currentTest.scratch, "<div id='replyDOM4J' class='xmlReply'> </div>", { escapeHtml:false });
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
