
dependencies = {
    layers:[ {
            name:"../custom/storeDemo.js",
            resourceName:"custom.storeDemo",
            dependencies: [ "custom.storeDemo" ]
        }, {
            name:"../custom/jsonRpcDemo.js",
            resourceName:"custom.jsonRpcDemo",
            dependencies: [ "custom.jsonRpcDemo" ]
        }
    ],

    prefixes:[
        [ "dijit", "../dijit" ],
        [ "dojox", "../dojox" ],
        [ "custom", "../custom" ]
    ]
};
