
function init(prefix) {
  dwr.engine.setActiveReverseAjax(true);
  var rand = Math.round(100 * Math.random());
  dwr.util.setValue("text", prefix + rand);
  sendPing();
}

function sendPing() {
  Control.pingFromClient(dwr.util.getValue("text"));
}

function addMessage(message) {
  messages.push(message);

  var chatlog = "";
  for (var data in messages) {
    chatlog = "<div>" + messages[data] + "</div>" + chatlog;
  }
  dwr.util.setValue("chatlog", chatlog, { escapeHtml:false });
}

var messages = [];

function buildClientTable(clientsByClientId) {
  dwr.util.removeAllRows("clients");
  dwr.util.addRows("clients", clientsByClientId, [
    function(client) { return client.id; },
    function(client) { return client.name; },
    function(client) { return "<span style='font-size:70%'>" + client.type + "<span>"; },
    function(client) { return client.page; },
    function(client) {
      return "<input type='text' id='input-" + client.id + "' name='' />" +
      "<input type='button' onclick='sendMessage(" + client.id + ")' value='Send'/>";
    }
  ], { escapeHtml:false });
}

function sendMessage(id) {
  var message = dwr.util.getValue("input-" + id);
  Control.sendToClient(id, message);
}
