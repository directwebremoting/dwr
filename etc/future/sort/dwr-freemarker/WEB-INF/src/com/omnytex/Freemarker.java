package com.omnytex;


import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The Freemarker class is a class you remote with DWR which accepts input
 * data, and the name of a Freemarker template, and it generates output using
 * that template and data.  To use it, add this to your dwr.xml file:
 * <br><br>
 * &lt;allow&gt;<br>
 * &nbsp;&nbsp;...<br>
 * &nbsp;&nbsp;&lt;convert converter="bean" match="com.omnytex.FreemarkerData"
 * /&gt;<br>
 * &nbsp;&nbsp;&lt;create creator="new" javascript="Freemarker"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param name="class" value="com.omnytex.Freemarker"
 * /&gt;<br>
 * &nbsp;&nbsp;&lt;/create&gt;<br>
 * &nbsp;&nbsp;...<br>
 * &lt;/allow&gt;<br>
 * &lt;signatures&gt;<br>
 * &nbsp;&nbsp;&lt;![CDATA[<br>
 * &nbsp;&nbsp;import java.util.Map;<br>
 * &nbsp;&nbsp;import com.omnytex.FreemarkerData;<br>
 * &nbsp;&nbsp;FreemarkerData.setMapData(final Map&lt;String, Map&gt;
 * inMapData);<br>
 * &nbsp;&nbsp;]]&gt;<br>
 * &lt;/signatures&gt;<br>
 * <br><br>
 * Then, call it using this form:
 * <br><br>
 * var d =
 * &nbsp;&nbsp;{ template : "aaaaa",
 * &nbsp;&nbsp;&nbsp;&nbsp;rootData : { bbbbb : ccccc, ... },
 * &nbsp;&nbsp;&nbsp;&nbsp;mapData : {
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;xxxxx : { yyyyy : "zzzzz", ... }, ...
 * &nbsp;&nbsp;&nbsp;&nbsp;}
 * &nbsp;&nbsp;};
 * Freemarker.run(d, callback );
 * <br><br>
 * Where:
 * <br>
 * <ul>
 * <li><b>aaaaa</b> is the fully-qualified name of the Freemarker template to
 * execute.  This template must be in the classpath, and you must use slashes
 * to specify it rather than dot notation.  For example: com/omnytex/test/ftl
 * </li>
 * <li><b>bbbbb</b> is zero or more data elements to be placed at the root of
 * the Freemarker data model (<b>ccccc</b> is the value of the element)
 * </li>
 * <li>xxxxx</b> is the key for zero or more Map elements to be inserted into
 * the Freemarker data model (<b>yyyyy</b> is the key of an alement in the Map,
 * and <b>zzzzz</b> is the value fo that element)
 * </li>
 * </ul>
 * If you are unfamiliar with the Freemarker data model, please read the
 * Freemarker documentation. But, in short, it is a tree structure with leaves
 * directly off the root, and/or a collection of Maps off the root.
 *
 * @author <a href="mailto:fzammetti@omnytex.com">Frank W. Zammetti</a>
 */
public class Freemarker {


  /**
   * Log instance.
   */
  private static Log log = LogFactory.getLog(Freemarker.class);


  /**
    * The run() is called to execute a Freemarker template.
    *
    * @param  inData    The FreemarkerData object populated from the incoming
    *                   data from the client.
    * @return           The results of executing the template.
    * @throws Exception If anything goes wrong.
    */
  public String run(FreemarkerData inData) throws Exception {

    log.trace("Freemarker.run() entry");

    if (log.isDebugEnabled()) {
      log.debug(("inData = " + inData));
    }

    // Create our data model.
    Map root = new HashMap();

    // First, iterate over the data that will go directly into the root and
    // insert it now.
    Map rootData = inData.getRootData();
    for (Iterator it = rootData.keySet().iterator(); it.hasNext();) {
      String nextKey = (String)it.next();
      root.put(nextKey, rootData.get(nextKey));
    }
    if (log.isDebugEnabled()) {
      log.debug(("root = " + root));
    }

    // Next, iterate over the data that consists of Maps and insert it all
    // into the root.
    Map mapData = inData.getMapData();
    for (Iterator it = mapData.keySet().iterator(); it.hasNext();) {
      String nextKey = (String)it.next();
      root.put(nextKey, mapData.get(nextKey));
    }
    if (log.isDebugEnabled()) {
      log.debug(("root = " + root));
    }

    // Set up Freemarker.
    Configuration fmConfig = new Configuration();
    fmConfig.setObjectWrapper(new DefaultObjectWrapper());
    fmConfig.setTemplateLoader(new ClassTemplateLoader(
      new Freemarker().getClass(), "/"));

    // Read in the Freemarker template specified.
    Template template = null;
    try {
      template = fmConfig.getTemplate(inData.getTemplate());
    } catch (Exception e) {
      log.error("Could not load Freemarker template " +
        inData.getTemplate() + "... is it in the classpath in the " +
        "expected location?  Is the template value passed in " +
        "fully-qualified? (Error: " + e);
      return "error";
    }

    // Generate the output.
    Writer out = new StringWriter();
    template.process(root, out);
    out.flush();
    if (log.isDebugEnabled()) {
      log.debug("Generated output = " + out);
    }

    // Return output.
    log.trace("Freemarker.run() exit");
    return out.toString();

  } // End run().


} // End class.
