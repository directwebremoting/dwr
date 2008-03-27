
function init() {
  DWREngine.setReverseAjax(true);
}

function whiteboardClaim() {
  Whiteboard.claim(DWRUtil.getValue("whiteboardClaim"));
}

function whiteboardUpdate() {
  Whiteboard.update(DWRUtil.getValue("whiteboardSource"));
}
