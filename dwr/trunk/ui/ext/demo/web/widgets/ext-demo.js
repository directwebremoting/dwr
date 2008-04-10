
var colModel = new Ext.grid.ColumnModel([
  { id:'id', dataIndex:'id', header:"ID", width:30, sortable:true, locked:true },
  { dataIndex:'name', header:"Name", width:100, sortable:true },
  { dataIndex:'salary', header:"Salary", width:75, sortable:true, renderer:Ext.util.Format.usMoney },
  { dataIndex:'address', header:"Address", width:200, sortable:true }
  /*{ header:"Last Updated", width: 85, sortable: true, renderer: Ext.util.Format.dateRenderer('m/d/Y'), dataIndex: 'lastChange'}*/
]);

var recordDef = Ext.data.Record.create([
  { name:'id' },
  { name:'name' },
  { name:'salary' },
  { name:'address' }
]);

var reader = new Ext.data.JsonReader({ root:"root", id:"id" }, recordDef);

function init() {
  People.getAllPeople(function(people) {
    var source = new Ext.data.Store({
      proxy:new Ext.data.MemoryProxy({ root:people }),
      reader:reader
    });
    source.load();

    var grid = new Ext.grid.Grid('grid-example', { ds:source, cm:colModel });

    var layout = Ext.BorderLayout.create({
      center: {
        margins:{ left:3, top:3, right:3, bottom:3 },
        panels:[ new Ext.GridPanel(grid) ]
      }
    }, 'grid-panel');

    grid.render();
    grid.getSelectionModel().selectFirstRow();
  });
}
