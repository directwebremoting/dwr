
dependencies = {
    layers:[ {
            name:"../demo/layer.js",
            resourceName:"demo.layer",
            dependencies: [ "demo.layer" ]
        }
    ],

    prefixes:[
        [ "dijit", "../dijit" ],
        [ "dojox", "../dojox" ],
        [ "demo", "../demo" ]
    ]
};
