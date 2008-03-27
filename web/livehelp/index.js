
function init() {
  DWREngine.setReverseAjax(true);
}

function notifyTyping(ele) {
  LiveHelp.notifyTyping(ele.id, DWRUtil.getValue(ele));
}

function notifyFocus(ele) {
  LiveHelp.notifyFocus(ele.id);
}

function notifyBlur(ele) {
  LiveHelp.notifyBlur(ele.id);
}
