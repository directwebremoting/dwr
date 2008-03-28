
function startPoll() {
  DWREngine.setReverseAjax(true);
}

function stopPoll() {
  DWREngine.setReverseAjax(false);
}

function sendMessage() {
  var text = DWRUtil.getValue("text");
  DWRUtil.setValue("text", "");
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
  DWRUtil.setValue("chatlog", chatlog);
}
