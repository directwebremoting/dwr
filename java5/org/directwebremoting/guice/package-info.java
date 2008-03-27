/**
 * <p>
 *   This package provides support for 
 *   <a href="http://code.google.com/p/google-guice/">Guice</a> 
 *   dependency injection in DWR-based web applications. 
 *   This documentation assumes you already understand Guice concepts.
 * </p>
 * <p>
 *   To use this support minimally,
 *   <ul>
 *   <li> install a concrete extension of 
 *     {@link org.directwebremoting.guice.DwrGuiceServletContextListener}
 *     as a {@code <listener>} in your web application's configuration
 *     file ({@code web.xml}), </li>
 *   <li> install {@link org.directwebremoting.guice.DwrGuiceServlet} for all 
 *     requests to {@code /dwr/*}. </li>
 *   </ul>
 *   For example:
 * </p>
 * <pre>
 *   &lt;listener&gt;
 *     &lt;listener-class&gt;org.myorg.myproj.MyServletContextListener&lt;/listener-class&gt;
 *   &lt;/listener&gt;
 *
 *   &lt;servlet&gt;
 *     &lt;servlet-name&gt;dwr-invoker&lt;/servlet-name&gt;
 *     &lt;servlet-class&gt;org.directwebremoting.guice.DwrGuiceServlet&lt;/servlet-class&gt;
 *   &lt;/servlet&gt;
 *
 *   &lt;servlet-mapping&gt;
 *     &lt;servlet-name&gt;dwr-invoker&lt;/servlet-name&gt;
 *     &lt;url-pattern&gt;/dwr/*&lt;/url-pattern&gt;
 *   &lt;/servlet-mapping&gt;
 * </pre>
 * <p>
 *   {@link org.directwebremoting.guice.DwrGuiceServletContextListener DwrGuiceServletContextListener}
 *   is also an abstract Guice module; it extends 
 *   {@link org.directwebremoting.guice.AbstractDwrModule AbstractDwrModule}, 
 *   which in turn extends Guice's {@link AbstractModule}.
 *   Your listener class must define
 *   {@link org.directwebremoting.guice.AbstractDwrModule#configure configure};
 *   this is where you do your Guice binding.
 *   You can also put binding code in a separate class or classes with
 *   {@link AbstractModule#install AbstractModule.install}.
 * </p>
 * <p>
 *   Use {@link org.directwebremoting.guice.GuiceCreator GuiceCreator}
 *   when annotating classes with {@code RemoteProxy}. When you use a 
 *   {@code GuiceCreator} to create your remoted objects, it gets an 
 *   instance from a Guice injector using your bindings. 
 * </p>
 * <p>
 *   For bind-time control over how JavaScript names map to Java targets, use the
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindRemoted(Class) bindRemoted}
 *   or
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindRemotedAs(String,Class) bindRemotedAs}
 *   methods. The target of the script can be an abstract class or interface 
 *   bound in the normal Guice way to a concrete class, instance, or provider. 
 *   In that case only the methods defined on the abstract class or 
 *   interface are accessible, even if the implementing class has other public 
 *   methods. You can supply different bindings for different script names, including
 *   using the same interface with different implementations for different script names,
 *   or different interfaces for different script names mapping to the same implementation
 *   type (assuming it implements both interfaces).
 * </p>
 * <p>
 *   You can bind a type or type pattern string to a custom converter with
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindConversion(Class) bindConversion},
 *   and you can put Ajax filters on scripts with
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindFilter(String) bindFilter}.
 *   Note, however, that you can achieve the same effect (and more flexibly) using Guice's 
 *   {@code bindInterceptors} method.
 * </p>
 * <p>
 *   You can install your own DWR configurator using
 *   {@code bind(Configurator.class).toInstance(yourConfigurator)}, 
 *   which then overrides any {@code dwr.xml} configuration. 
 *   You'll probably want to use a
 *   {@link org.directwebremoting.fluent.FluentConfigurator FluentConfigurator}
 *   for this purpose.
 * </p>
 * <p>
 *   You can still configure DWR's settings normally via {@code <init-param>} 
 *   directives in {@code web.xml}, but usually there is no need to. Most DWR 
 *   settings can be set with
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindParameter(ParamName) bindParameter}. 
 *   The {@link org.directwebremoting.guice.ParamName ParamName} 
 *   enum type lists the available parameters.
 * </p>
 * <p>
 *   To be able to use the DWR scopes for all your injected objects, not just
 *   DWR-remoted objects, your binding code should call 
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindDwrScopes() bindDwrScopes}
 *   at some point.
 * </p>
 * <p>
 *   For creating your own scopes where the instance injected depends on some
 *   run-time value, create a concrete extension of 
 *   {@link org.directwebremoting.guice.AbstractContextScope AbstractContextScope}.
 * </p>
 * <p>
 *   This example illustrates two ways to define remoted objects,
 *   calling {@code bindRemotedAs} and annotating with {@code @RemoteProxy};
 *   two ways to define conversions, using {@code bindConversion}
 *   and using a custom configurator; how to register annotated classes
 *   at bind-time; how to bind a script name to an {@code AjaxFilter}; and
 *   how to set a DWR parameter (debug, in this case) at bind-time.
 *   It does not use an {@code <init-param>} directive, and it doesn't have
 *   a {@code dwr.xml}.
 * </p>
 * <pre>
 *    public final class MyServletContextListener extends DwrGuiceServletContextListener
 *    {
 *        protected void configure()
 *        {
 *            bindRemotedAs("Hello", HelloService.class)
 *                .to(HelloServiceImpl.class)
 *                .in(DwrScopes.APPLICATION);
 *
 *            bindFilter("Hello")
 *                .to(TraceFilter.class);
 *
 *            bind(MessageService.class)
 *                .to(MessageServiceImpl.class)
 *                .in(DwrScopes.SCRIPT);
 *
 *            bindAnnotatedClasses(
 *                DomainService.class,   // @RemoteProxy(creator=GuiceCreator.class)/@RemoteMethod
 *                HelloRecordImpl.class  // @DataTransferObject/@RemoteProperty
 *            );
 *            
 *            // When converting HelloRecord, use existing converter for HelloRecordImpl.
 *            bindConversion(HelloRecord.class, HelloRecordImpl.class);
 *
 *            bindConversion(DateTime.class)
 *                .toInstance(DateTimeConverter.get("yyyy-MM-dd hh:mm a"));
 *
 *            bind(Configurator.class).toInstance(new FluentConfigurator()
 *            {
 *                public void configure() {
 *                    String localTime = "localTime";
 *                    withConverterType(localTime, DateTimeConverter.class.getName());
 *                    withConverter(localTime, LocalTime.class.getName())
 *                        .addParam("format", "yyyy-MM-dd");
 *                }
 *            });
 *
 *            bindParameter(DEBUG).to(true);
 *
 *            bindDwrScopes();
 *        }
 *    }
 * </pre>
 * <p>
 *   Note that because application scope is larger than script session scope, 
 *   {@code HelloServiceImpl} has an injected constructor (not shown here)
 *   that takes a {@code Provider<MessageService>} rather than a plain
 *   {@code MessageService}.
 * </p>
 * <p>
 *   There are four classes with names that start with "Internal". These classes
 *   have to be public with a parameterless constructor so the non-Guicy DWR
 *   machinery can create them. They are not meant to be used directly.
 * </p>
 * <p>
 *   The classes that handle DWR scopes are modeled on the classes in the
 *   {@code com.google.inject.servlet} package, but are independent of them.
 *   You do <em>not</em> need to install the Guice {@code ServletModule} and
 *   {@code GuiceFilter} to use the DWR scopes, but if you do, you have to be 
 *   careful to install the DWR scopes without creating conflicting bindings 
 *   for request, response, and session. Calling
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindDwrScopes(boolean) bindDwrScopes(false)}
 *   accomplishes this.
 * </p>
 * @author Tim Peierls [tim at peierls dot net]
 */
package org.directwebremoting.guice;
