
var columnSet = new YAHOO.widget.ColumnSet([
  { key:"id", sortable:true, resizeable:true },
  { key:"name", sortable:true, resizeable:true, editor:"textbox" },
  { key:"salary", type:"currency", sortable:true, resizeable:true, editor:"textbox" },
  { key:"address", type:"text", sortable:true, resizeable:true, editor:"textbox" }
]);

var headerFields = [ "id", "name", "salary", "address" ];

var peopleCache = {};

function init() {
  People.getAllPeople(function(people) {
    var i, person;
    for (i = 0; i < people.length; i++) {
      person = people[i];
      peopleCache[person.id] = person;
    }
    var dataSource = new YAHOO.util.DataSource(people);
    dataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
    dataSource.responseSchema = { fields:headerFields };

    var table = new YAHOO.widget.DataTable("basic", columnSet, dataSource);
    table.subscribe("cellClickEvent", table.onEventEditCell);
    table.subscribe("cellMouseoverEvent", table.onEventHighlightCell);
    table.subscribe("cellMouseoutEvent", table.onEventUnhighlightCell);
    table.subscribe("cellEditEvent", function(data) {
      // The id of the edited person is stored in the first column
      var id = dwr.util.getValue(data.target.parentNode.childNodes[0].id);
      var person = peopleCache[id];
      // The value being edited is in the first row
      var field = headerFields[data.target.columnIndex];
      // Now we can update the cache and tell the server
      person[field] = data.newData;
      People.setPerson(person);
    });
  });
}
