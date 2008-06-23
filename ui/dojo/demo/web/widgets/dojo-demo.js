
dojo.require("dojo.parser");
dojo.require("dojox.grid.Grid");
dojo.require("dojox.grid._data.model");
dojo.require("DwrStore");

function init() {
  var view = {
    cells: [[
      { name:'Name', field:"name" },
      { name:'Address', field:"address" },
      { name:'Age', field:"age" },
      { name:'Male', field:"male" }
    ]]
  };
  var store = new DwrStore("testServerData", { query:{}, subscribe:false });

  var model = new dojox.grid.data.DojoData();
  model.store = store;

  var grid = dijit.byId("grid");
  grid.setStructure([ view ]);
  grid.setModel(model);
  grid.refresh();

dwr.grid = grid;
  //People.getAllPeople(function(people) {
  //});
}

dojo.addOnLoad(init);
