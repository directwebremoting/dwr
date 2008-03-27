
function sendMessage() {
  Battleships.addMessage(DWRUtil.getValue("text"));
  DWRUtil.setValue("text", "");
}

function createCell() {
  return "&nbsp";
}

function setName() {
  Battleships.setName(DWRUtil.getValue("name"));
}

function move() {
  Battleships.move({ row:DWRUtil.getValue("row"), col:DWRUtil.getValue("col") }, setPerson);
}

function init() {
  DWREngine.setReverseAjax(true);
  DWREngine.beginBatch();
  Battleships.getGridSize(function(grid) {
    var rows = new Array(grid.row);
    var cols = [];
    for (i = 0; i < grid.col; i++) { cols.push(createCell); }
    DWRUtil.addRows("map", rows, cols, {
      cellCreator:function(options) {
        var td = document.createElement("td");
        var rowNum = options.rowNum;
        var cellNum = options.cellNum;
        td.id = "r" + rowNum + "c" + cellNum;
        td.onclick = function() {
          startShot(rowNum, cellNum);
        };
        return td;
      }
    });
  });
  Battleships.initPerson(setPerson);
  DWREngine.endBatch();
}

function setPerson(person) {
  DWRUtil.setValue("name", person.name);
  DWRUtil.setValue("row", person.position.row);
  DWRUtil.setValue("col", person.position.col);
  var id = "r" + person.position.row + "c" + person.position.col;
  if (oldPosId) {
    $(oldPosId).className = "";
  }
  oldPosId = id;
  $(id).className = "home";
}

var oldPosId = null;

var shotRow;
var shotCol;
var countdown = 0;

function startShot(row, col) {
  shotRow = row;
  shotCol = col;
  DWRUtil.setValue("shotinner", "&nbsp;Shooting at: row=" + shotRow + ", col=" + shotCol);
  if (countdown == 0) {
    countdown = 180;
    processShot();
  }
}

function processShot() {
  countdown = countdown - 10;
  $("shotinner").style.width = countdown + "px";
  if (countdown < 1) {
    shoot();
    DWRUtil.setValue("shotinner", "");
  } else {
    setTimeout("processShot()", 100);
  }
};

function shoot() {
  Battleships.shoot({ row:shotRow, col:shotCol });
}

function showMessagesAndScores(messages, players) {
  DWRUtil.removeAllOptions("chatlog");
  DWRUtil.addOptions("chatlog", messages, function(message) {
    return "<span class='chatname'>" + message.author.name + ": </span><span class='chattext' style='color:" + message.author.color + ";'>" + message.text + "</span>";
  }, "id");

  DWRUtil.removeAllRows("scores");
  DWRUtil.addRows("scores", players, [
    function(person) {
      return "<span style='color:" + person.color + ";'>" + person.name + "</span>&nbsp;";
    },
    function(person) {
      return "<span style='color:" + person.color + ";'>" + person.score + "</span>";
    }
  ]);
}
