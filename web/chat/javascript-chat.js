
function startPoll() {
  dwr.engine.setReverseAjax(true);
}

function stopPoll() {
  dwr.engine.setReverseAjax(false);
}

function sendMessage() {
  var text = dwr.util.getValue("text");
  dwr.util.setValue("text", "");
  JavascriptChat.addMessage(text);
}

function pingMe() {
  JavascriptChat.pingMe();
}

function receiveMessages(messages) {
  var chatlog = "";
  for (var data in messages) {
    chatlog = "<div>" + messages[data].text + "</div>" + chatlog;
  }
  dwr.util.setValue("chatlog", chatlog);
}
