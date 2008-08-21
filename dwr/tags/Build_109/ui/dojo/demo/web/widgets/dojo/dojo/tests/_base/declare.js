dojo.provide("tests._base.declare");

tests.register("tests._base.declare",
	[
		function smokeTest(t){
			dojo.declare("tests._base.declare.tmp");
			var tmp = new tests._base.declare.tmp();
			dojo.declare("testsFoo");
			var tmp = new testsFoo();
		},
		function smokeTest2(t){
			dojo.declare("tests._base.declare.foo", null, {
				foo: "thonk"
			});
			var tmp = new tests._base.declare.foo();
			t.is("thonk", tmp.foo);

			dojo.declare("testsFoo2", null, {
				foo: "thonk"
			});
			var tmp2 = new testsFoo2();
			t.is("thonk", tmp2.foo);
		},
		function smokeTestWithCtor(t){
			dojo.declare("tests._base.declare.fooBar", null, {
				constructor: function(){
					this.foo = "blah";
				},
				foo: "thonk"
			});
			var tmp = new tests._base.declare.fooBar();
			t.is("blah", tmp.foo);
		},
		function smokeTestCompactArgs(t){
			dojo.declare("tests._base.declare.fooBar2", null, {
				foo: "thonk"
			});
			var tmp = new tests._base.declare.fooBar2();
			t.is("thonk", tmp.foo);
		},
		function subclass(t){
			dojo.declare("tests._base.declare.tmp3", null, {
				foo: "thonk"
			});
			dojo.declare("tests._base.declare.tmp4", tests._base.declare.tmp3);
			var tmp = new tests._base.declare.tmp4();
			t.is("thonk", tmp.foo);
		},
		function subclassWithCtor(t){
			dojo.declare("tests._base.declare.tmp5", null, {
				constructor: function(){
					this.foo = "blah";
				},
				foo: "thonk"
			});
			dojo.declare("tests._base.declare.tmp6", tests._base.declare.tmp5);
			var tmp = new tests._base.declare.tmp6();
			t.is("blah", tmp.foo);
		},
		function mixinSubclass(t){
			dojo.declare("tests._base.declare.tmp7", null, { 
				foo: "thonk" 
			});
			dojo.declare("tests._base.declare.tmp8", null, {
				constructor: function(){
					this.foo = "blah";
				}
			});
			var tmp = new tests._base.declare.tmp8();
			t.is("blah", tmp.foo);
			dojo.declare("tests._base.declare.tmp9",
				[
					tests._base.declare.tmp7, // prototypal
					tests._base.declare.tmp8  // mixin
				]);
			var tmp2 = new tests._base.declare.tmp9();
			t.is("blah", tmp2.foo);
		},
		function superclassRef(t){
			dojo.declare("tests._base.declare.tmp10", null, {
				foo: "thonk"
			});
			dojo.declare("tests._base.declare.tmp11", tests._base.declare.tmp10, {
				constructor: function(){
					this.foo = "blah";
				}
			});
			var tmp = new tests._base.declare.tmp11();
			t.is("blah", tmp.foo);
			t.is("thonk", tests._base.declare.tmp11.superclass.foo);
		},
		function inheritedCall(t){
			var foo = "xyzzy";
			dojo.declare("tests._base.declare.tmp12", null, {
				foo: "thonk",
				bar: function(arg1, arg2){
					if(arg1){
						this.foo = arg1;
					}
					if(arg2){
						foo = arg2;
					}
				}
			});
			dojo.declare("tests._base.declare.tmp13", tests._base.declare.tmp12, {
				constructor: function(){
					this.foo = "blah";
				}
			});
			var tmp = new tests._base.declare.tmp13();
			t.is("blah", tmp.foo);
			t.is("xyzzy", foo);
			tmp.bar("zot");
			t.is("zot", tmp.foo);
			t.is("xyzzy", foo);
			tmp.bar("trousers", "squiggle");
			t.is("trousers", tmp.foo);
			t.is("squiggle", foo);
		},
		function inheritedExplicitCall(t){
			var foo = "xyzzy";
			dojo.declare("tests._base.declare.tmp14", null, {
				foo: "thonk",
				bar: function(arg1, arg2){
					if(arg1){
						this.foo = arg1;
					}
					if(arg2){
						foo = arg2;
					}
				}
			});
			dojo.declare("tests._base.declare.tmp15", tests._base.declare.tmp14, {
				constructor: function(){
					this.foo = "blah";
				},
				bar: function(arg1, arg2){
					this.inherited("bar", arguments, [arg2, arg1]);
				},
				baz: function(arg1, arg2){
					tests._base.declare.tmp15.superclass.bar.apply(this, arguments);
				}
			});
			var tmp = new tests._base.declare.tmp15();
			t.is("blah", tmp.foo);
			t.is("xyzzy", foo);
			tmp.baz("zot");
			t.is("zot", tmp.foo);
			t.is("xyzzy", foo);
			tmp.bar("trousers", "squiggle");
			t.is("squiggle", tmp.foo);
			t.is("trousers", foo);
		},
		function inheritedMixinCalls(t){
			dojo.declare("tests._base.declare.tmp16", null, {
				foo: "",
				bar: function(){
					this.foo += "tmp16";
				}
			});
			dojo.declare("tests._base.declare.mixin16", null, {
				bar: function(){
					this.inherited(arguments);
					this.foo += ".mixin16";
				}
			});
			dojo.declare("tests._base.declare.mixin17", tests._base.declare.mixin16, {
				bar: function(){
					this.inherited(arguments);
					this.foo += ".mixin17";
				}
			});
			dojo.declare("tests._base.declare.tmp17", [tests._base.declare.tmp16, tests._base.declare.mixin17], {
				bar: function(){
					this.inherited(arguments);
					this.foo += ".tmp17";
				}
			});
			var tmp = new tests._base.declare.tmp17();
			tmp.bar();
			t.is("tmp16.mixin16.mixin17.tmp17", tmp.foo);
		},
		function mixinPreamble(t){
			var passed = false;
			dojo.declare("tests._base.declare.tmp16");
			new tests._base.declare.tmp16({ preamble: function(){ passed = true; } });
			t.t(passed);
		}
		// FIXME: there are still some permutations to test like:
		//	- ctor arguments
		//	- multi-level inheritance + L/R conflict checks
	]
);
