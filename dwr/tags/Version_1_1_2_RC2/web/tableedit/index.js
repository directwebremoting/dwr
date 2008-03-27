function update() {
  Demo.getAllPeople(fillTable);
}

var getName = function(person) { return person.name };
var getDoB = function(person) { return person.address }; // if we return to using dates, add .toLocaleDateString()
var getSalary = function(person) { return person.salary };
var getEdit = function(person) {
  return '<input type="button" value="Edit" onclick="readPerson('+person.id+')"/>';
};
var getDelete = function(person) {
  return '<input type="button" value="Delete" onclick="deletePerson('+person.id+', \''+person.name+'\')"/>';
};
function fillTable(people) {
  DWRUtil.removeAllRows("peoplebody");
  DWRUtil.addRows("peoplebody", people, [ getName, getDoB, getSalary, getEdit, getDelete ])
}

function readPerson(id) {
  Demo.getPerson(fillForm, id);
}

function deletePerson(personid, name) {
  if (confirm("Are you sure you want to delete " + name + "?")) {
    Demo.deletePerson(update, { id:personid });
  }
}

function writePerson() {
  DWRUtil.getValues(person);
  Demo.addPerson(update, person);
}

var person = { id:-1, name:null, address:null, salary:null };

function clearPerson() {
  person = { id:-1, name:null, address:null, salary:null };
  DWRUtil.setValues(person);
}

function fillForm(aperson) {
  person = aperson;
  DWRUtil.setValues(person);
}

function init() {
  DWRUtil.useLoadingMessage();
  update();
}

callOnLoad(init);
