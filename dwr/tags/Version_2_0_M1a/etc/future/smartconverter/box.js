/* box.js
 *
 * Utility for boxing and unboxing Javascript values,
 * allowing the client to provide "hints" as to the actual
 * server-side Java classes that the SmartConverterManager
 * should convert to.
 *
 * @author  Tim Dwelle [tim at dwelle dot net]
 *
 */

	var JAVA_CLASS_PROPERTY = "$javaClass";
	var VALUE_PROPERTY      = "$value";
	var WRAPPER_TOKEN       = "uk.ltd.getahead.dwr.impl.SmartConverterManager$";


	/* box()
	 *
	 * Allows us to "box" a Javascript value.  The boxed
	 * version allows the client to give a "hint" as to the
	 * actual Java type that inbound variables should be
	 * converted to (when used in conjunction with DWR's
	 * SmartConverterManager functionality).
	 *
	 * This approach is useful when dealing with collections
	 * of heterogenous types, methods that take an interface
	 * instead of a concrete class, and the like.
	 *
	 * @param val         the js value to be boxed
	 *
	 * @param javaClass   java class as a string
	 *
	 * @return            the boxed js value
	 *
	 */
	function box(val, javaClass)
	{
		if (val != null && javaClass != null)
		{
			// set javaclass as a property
			// on the specified object
			if (typeof val == "object" &&
			    !DWRUtil._isArray(val))
			{
				val[JAVA_CLASS_PROPERTY] = javaClass;
			}

			// we have a built-in js type...
			// need to box it into a wrapper
			else
			{
				var wrapper = new Object();
				wrapper[JAVA_CLASS_PROPERTY] = WRAPPER_TOKEN + javaClass;
				wrapper[VALUE_PROPERTY] = val;
				val = wrapper;
			}
		}
		return val;
	}

	/* unbox()
	 *
	 * Takes a "boxed" Javascript value and unboxes it.
	 *
	 * @param val    the boxed js value
	 *
	 * @return       the unboxed js value
	 *
	 */
	function unbox(val)
	{
		if (val != null &&
		    typeof val == "object" &&
		    val[JAVA_CLASS_PROPERTY])
		{
			// we have an object... just remove the
			// javaClass property
			if (val["$javaClass"].indexOf(WRAPPER_TOKEN) != 0)
			{
				delete val[JAVA_CLASS_PROPERTY];
			}

			// we have a wrapper for a built-in js
			// type... need to unbox it by grabbing
			// the value from the wrapper
			else
			{
				val = val[VALUE_PROPERTY];
			}
		}

		return val;
	}

	/* example()
	 *
	 * An example of using box() in conjunction with DWR's
	 * SmartConverterManager.
	 *
	 * Lets say we have a Java implementation of the
	 * RemoteObject doSomething() method that takes in
	 * a single argument of type 'java.lang.Object'.
	 *
	 * Here, we call the doSomething() method with a variety
	 * of different types, including a list of heterogenous
	 * objects and have each treated as a different type of
	 * Java class by the server.
	 *
	 */
	function example()
	{
		var boy = new Object();
		boy.name = "Timmy";
		boy.age = 12;

		var dog = new Object();
		dog.name = "Lassie";
		dog.fleas = true;

		var str = "Hello world!";

		var num = 1.1;

		var bool = false;

		boy = box(boy, "com.example.bean.Person");
		dog = box(dog, "com.example.bean.Dog");
		str = box(str, "java.lang.String");
		num = box(num, "java.lang.BigDecimal");
		bool = box(bool, "java.lang.Boolean");

		var list = new Array(boy, dog, str, num, bool);
		list = box(list, "java.util.ArrayList");

		RemoteObject.doSomething(exampleCallback, boy);
		RemoteObject.doSomething(exampleCallback, dog);
		RemoteObject.doSomething(exampleCallback, str);
		RemoteObject.doSomething(exampleCallback, num);
		RemoteObject.doSomething(exampleCallback, bool);
		RemoteObject.doSomething(exampleCallback, list);
	}

	/* exampleCallback()
	 *
	 * On callback, throw up an alert displaying the
	 * returned value.
	 *
	 */
	function exampleCallback(data)
	{
		alert(DWRUtil.toDescriptiveString(data, 1));
	}