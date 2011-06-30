
function init() {
  dwr.util.useLoadingMessage();
  Tabs.init('tabList', 'tabContents');
  dwr.util.setValue("filter", "");
  addSingleRow("peoplebody", "Please enter a search filter");
}

var peopleCache = [ ];
var lastFilter = "";

function fillTable(people) {
  var filter = dwr.util.getValue("filter");
  var pattern = new RegExp("(" + filter + ")", "i");
  var filtered = [];
  for (i = 0; i < people.length; i++) {
    if (pattern.test(people[i].name)) {
      filtered.push(people[i]);
    }
  }
  dwr.util.removeAllRows("peoplebody");
  if (filtered.length == 0) {
    addSingleRow("peoplebody", "No matches");
  }
  else {
    dwr.util.addRows("peoplebody", filtered, [
      function(person) { return person.name.replace(pattern, "<span class='highlight'>$1</span>"); },
      function(person) { return person.age; },
      function(person) { return person.address; }
    ], { escapeHtml:false });
  }
  peopleCache = people;
}

function filterChanged() {
  var filter = dwr.util.getValue("filter");
  if (filter.length == 0) {
    dwr.util.removeAllRows("peoplebody");
    addSingleRow("peoplebody", "Please enter a search filter");
  }
  else {
    if (filter.charAt(0) == lastFilter.charAt(0)) {
      fillTable(peopleCache);
    }
    else {
      People.getMatchingFromLargeCrowd(filter.charAt(0), fillTable);
    }
  }
  lastFilter = filter;
}

function addSingleRow(id, message) {
  dwr.util.addRows(id, [1], [
    function(data) { return message; }
  ], {
    cellCreator:function() {
      var td = document.createElement("td");
      td.setAttribute("colspan", 3);
      return td;
    }
  });
}
