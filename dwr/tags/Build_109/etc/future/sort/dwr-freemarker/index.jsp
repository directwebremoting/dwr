<html>
  <head>
    <title>dwr</title>
    <script type="text/javascript" src="dwr/interface/Freemarker.js"></script>
    <script type="text/javascript" src="dwr/engine.js"></script>
    <script>
      function testFreemarker() {
        var d =
          { template : "com/omnytex/test.ftl",
            rootData : { rootElem1 : "val1", rootElem2 : "val2" },
            mapData : {
              person1 : { firstName : "Frank", lastName : "Zammetti", age : "34" },
              person2 : { firstName : "Jessica", lastName : "Alba", age : "24" }
            }
          };
        Freemarker.run(d,
          {
            callback : function(inResp) {
              document.getElementById("divTest").innerHTML = inResp;
            }
          }
        );
      }
    </script>
  </head>
  <body>
    <h1>DWR Freemarker Extension Test</h1>
    You are viewing the test webapp for the DWR Freemarker extension.
    With this extension, you can generate output to an Ajax request using the
    Freemarker library, based on data passed in from the client.
    <br><br>
    To run the test, simply click the following button.  You should see some
    text appear below it.
    <br><br>
    <input type="button" value="Click to test Freemarker"
      onClick="testFreemarker();">
    <br><br>
    <div id="divTest" style="width:500px;border:1px solid #000000;"></div>
    <h2>How it works</h2>
    To use it, add this to your dwr.xml file:
    <br><br>
    <div style="background-color:#ffffa0;width:500px;">
    &lt;allow&gt;<br>
    &nbsp;&nbsp;...<br>
    &nbsp;&nbsp;&lt;convert converter="bean" match="com.omnytex.FreemarkerData" /&gt;<br>
    &nbsp;&nbsp;&lt;create creator="new" javascript="Freemarker"&gt;<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&lt;param name="class" value="com.omnytex.Freemarker" /&gt;<br>
    &nbsp;&nbsp;&lt;/create&gt;<br>
    &nbsp;&nbsp;...<br>
    &lt;/allow&gt;<br>
    &lt;signatures&gt;<br>
    &nbsp;&nbsp;&lt;![CDATA[<br>
    &nbsp;&nbsp;import java.util.Map;<br>
    &nbsp;&nbsp;import com.omnytex.FreemarkerData;<br>
    &nbsp;&nbsp;FreemarkerData.setMapData(final Map&lt;String, Map&gt; inMapData);<br>
    &nbsp;&nbsp;]]&gt;<br>
    &lt;/signatures&gt;<br>
    </div>
    <br>
    Then, call it using this form:
    <br><br>
    <div style="background-color:#ffffa0;width:500px;">
    var d =<br>
    &nbsp;&nbsp;{ template : "aaaaa",<br>
    &nbsp;&nbsp;&nbsp;&nbsp;rootData : { bbbbb : ccccc, ... },<br>
    &nbsp;&nbsp;&nbsp;&nbsp;mapData : {<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;xxxxx : { yyyyy : "zzzzz", ... }, ...<br>
    &nbsp;&nbsp;&nbsp;&nbsp;}<br>
    &nbsp;&nbsp;};<br>
    Freemarker.run(d, callback );<br>
    </div>
    <br>
    ...where...
    <br>
    <ul>
    <li><b>aaaaa</b> is the fully-qualified name of the Freemarker template to
    execute.  This template must be in the classpath, and you must use slashes
    to specify it rather than dot notation.  For example: com/omnytex/test/ftl
    </li>
    <li><b>bbbbb</b> is zero or more data elements to be placed at the root of
    the Freemarker data model (<b>ccccc</b> is the value of the element)
    </li>
    <li><b>xxxxx</b> is the key for zero or more Map elements to be inserted into
    the Freemarker data model (<b>yyyyy</b> is the key of an alement in the Map,
    and <b>zzzzz</b> is the value fo that element)
    </li>
    </ul>
    Here's the exact JavaScript used to generate the test output above:
    <br><br>
    <div style="background-color:#ffffa0;width:500px;">
    var d =<br>
    &nbsp;&nbsp;{ template : "com/omnytex/test.ftl",<br>
    &nbsp;&nbsp;&nbsp;&nbsp;rootData : { rootElem1 : "val1", rootElem2 : "val2" },<br>
    &nbsp;&nbsp;&nbsp;&nbsp;mapData : {<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;person1 : { firstName : "Frank", lastName : "Zammetti", age : "34" },<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;person2 : { firstName : "Jessica", lastName : "Alba", age : "24" }<br>
    &nbsp;&nbsp;&nbsp;&nbsp;}<br>
    &nbsp;&nbsp;};<br>
    Freemarker.run(d,<br>
    &nbsp;&nbsp;{<br>
    &nbsp;&nbsp;&nbsp;&nbsp;callback : function(inResp) {<br>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;document.getElementById("divTest").innerHTML = inResp;<br>
    &nbsp;&nbsp;&nbsp;&nbsp;}<br>
    &nbsp;&nbsp;}<br>
    );<br>
    </div>
    <br>
    If you are unfamiliar with the Freemarker data model, please read the
    Freemarker documentation. But, in short, it is a tree structure with leaves
    directly off the root, and/or a collection of Maps off the root.  The data
    structure you see above is used to populate a FreemarkerData object, which
    is passed into the Freemarker.run() method.  Any elements in the rootData
    Map will be inserted as leaves in the root of the Freemarker data model.
    Any elements in the mapData Map (where each element is itself a Map) will
    be inserted into the data model as nodes.
    <br><br>
    Hope someone finds this useful!
    <br><br>
    <a href="mailto:fzammetti@omnytex.com">Frank W. Zammetti</a>, June 28, 2007
  </body>
</html>
