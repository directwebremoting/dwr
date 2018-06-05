/**
 * <p>Provides annotations support for DWR. This can be used as a replacement as well as
 * in conjunction with {@code dwr.xml}.</p>
 *
 * <h3>Setup</h3>
 *
 * <p>To use DWR with annotations you need to specify a different DWR controller
 * servlet in your {@code web.xml}:</p>
 *
 * <p><pre>
 * {@literal <servlet>}
 *    {@literal <description>}DWR controller servlet{@literal </description>}
 *    {@literal <servlet-name>}DWR controller servlet{@literal </servlet-name>}
 *    {@literal <servlet-class>}{@linkplain org.directwebremoting.servlet.DwrServlet org.directwebremoting.servlet.DwrServlet}{@literal </servlet-class>}
 *    {@literal <init-param>}
 *       {@literal <param-name>}classes{@literal </param-name>}
 *       {@literal <param-value>}
 *          com.example.RemoteFunctions,
 *          com.example.RemoteBean
 *       {@literal </param-value>}
 *    {@literal </init-param>}
 * {@literal </servlet>}
 * </pre></p>
 *
 * <p>The {@code classes} servlet parameter must provide a comma-separated list of the
 * fully-qualified class names of all annotated classes to be used with DWR.</p>
 *
 * <h3>Remote Class Access</h3>
 *
 * <p>To make a simple class available for remote access, use the
 * {@link org.directwebremoting.annotations.RemoteProxy RemoteProxy} and
 * {@link org.directwebremoting.annotations.RemoteMethod RemoteMethod} annotations:</p>
 *
 * <p><pre>
 * {@literal @RemoteProxy}
 * public class RemoteFunctions {
 *    {@literal @RemoteMethod}
 *    public int calculateFoo() {
 *      return 42;
 *    }
 * }
 * </pre></p>
 *
 * <p>Any method not annotated with {@code RemoteMethod} will not be available
 * for remote access.</p>
 *
 * <p>To use a scripting name different from the class name, use the
 * {@code name} attribute of {@code RemoteProxy}:</p>
 *
 * <p><pre>
 * {@literal @RemoteProxy}(name="Functions")
 * public class RemoteFunctions {
 * }
 * </pre></p>
 *
 * <h3>Object Conversion</h3>
 *
 * <p>To make simple bean classes available for remote access, use the
 * {@link org.directwebremoting.annotations.DataTransferObject DataTransferObject} and
 * {@link org.directwebremoting.annotations.RemoteProperty RemoteProperty} annotations:</p>
 *
 * <p><pre>
 * {@literal @DataTransferObject}
 * public class Foo {
 *    {@literal @RemoteProperty}
 *    private int foo;
 *
 *    public int getFoo() {
 *       return foo;
 *    }
 *
 *    {@literal @RemoteProperty}
 *    public int getBar() {
 *       return foo * 42;
 *    }
 * }
 * </pre></p>
 *
 * <p>To use more sophisticated converters see the
 * {@link org.directwebremoting.annotations.DataTransferObject#converter converter} attribute of the
 * {@code DataTransferObject} annotation.</p>
 *
 * @author Maik Schreiber &lt;blizzy AT blizzy DOT de&gt;
 */
package org.directwebremoting.annotations;
