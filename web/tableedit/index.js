
function init() {
  DWRUtil.useLoadingMessage();
  Tabs.init('tabList', 'tabContents');
  fillTable();
}

var peopleCache = { };
var viewed = -1;

function fillTable() {
  People.getAllPeople(function(people) {
    // Delete all the rows except for the "pattern" row
    DWRUtil.removeAllRows("peoplebody", { filter:function(tr) {
      return (tr.id != "pattern");
    }});
    // Create a new set cloned from the pattern row
    var person, id;
    people.sort(function(p1, p2) { return p1.name.localeCompare(p2.name); });
    for (var i = 0; i < people.length; i++) {
      person = people[i];
      id = person.id;
      DWRUtil.cloneNode("pattern", { idSuffix:id });
      DWRUtil.setValue("tableName" + id, person.name);
      DWRUtil.setValue("tableSalary" + id, person.salary);
      DWRUtil.setValue("tableAddress" + id, person.address);
      $("pattern" + id).style.display = "table-row";
      peopleCache[id] = person;
    }
  });
}

function editClicked(eleid) {
  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
  var person = peopleCache[eleid.substring(4)];
  DWRUtil.setValues(person);
}

function deleteClicked(eleid) {
  // we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
  var person = peopleCache[eleid.substring(6)];
  if (confirm("Are you sure you want to delete " + person.name + "?")) {
    DWREngine.beginBatch();
    People.deletePerson({ id:id });
    fillTable();
    DWREngine.endBatch();
  }
}

function writePerson() {
  var person = { id:viewed, name:null, address:null, salary:null };
  DWRUtil.getValues(person);

  DWREngine.beginBatch();
  People.setPerson(person);
  fillTable();
  DWREngine.endBatch();
}

function clearPerson() {
  viewed = -1;
  DWRUtil.setValues({ id:-1, name:null, address:null, salary:null });
}
