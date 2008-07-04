
dojo.require("dojo.parser");
dojo.require("dojox.grid.Grid");
dojo.require("dojox.grid._data.model");
dojo.require("DwrStore");

var grid;

function init() {
  var view = {
    cells: [[
      { name:'Name', field:'name', width:'120px', editor:dojox.grid.editors.Input },
      { name:'Address', field:'address', width:'200px', editor:dojox.grid.editors.Input },
      { name:'Age', field:'age', width:'30px', editor:dojox.grid.editors.Input },
      { name:'Male', field:'male', width:'40px', editor:dojox.grid.editors.Bool }
    ]]
  };
  var store = new DwrStore("testServerData", { subscribe:true });

  var model = new dojox.grid.data.DojoData();
  model.store = store;

  grid = dijit.byId("grid");
  grid.setStructure([ view ]);
  grid.setModel(model);
  grid.refresh();
}

dojo.addOnLoad(init);

addRow = function(){
  grid.addRow({ name:"Noname", address:"No Address", age:21, male:false });
}
