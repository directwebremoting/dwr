
function init() {
  dwr.engine.setReverseAjax(true);
}

function notifyTyping(ele) {
  LiveHelp.notifyTyping(ele.id, dwr.util.getValue(ele));
}

function notifyFocus(ele) {
  LiveHelp.notifyFocus(ele.id);
}

function notifyBlur(ele) {
  LiveHelp.notifyBlur(ele.id);
}
