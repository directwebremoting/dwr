
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
 *   {@link org.directwebremoting.guice.DwrGuiceServletContextListener}
 *   is also an abstract Guice module; it extends
 *   {@link org.directwebremoting.guice.AbstractDwrModule},
 *   which extends {@link org.directwebremoting.guice.util.AbstractModule},
 *   which in turn extends Guice's {@link com.google.inject.AbstractModule}.
 *   Your listener class must define
 *   {@link org.directwebremoting.guice.DwrGuiceServletContextListener#configure configure};
 *   this is where you do your Guice binding.
 *   You can also put binding code in a separate class or classes with
 *   {@link org.directwebremoting.guice.util.AbstractModule#install AbstractModule.install}.
 * </p>
 * <p>
 *   Alternatively,
 *   if you already have an existing {@link com.google.inject.Injector Injector} instance
 *   with bindings from one or more {@link org.directwebremoting.guice.AbstractDwrModule}, use
 *   {@link org.directwebremoting.guice.CustomInjectorServletContextListener}
 *   by defining {@link org.directwebremoting.guice.CustomInjectorServletContextListener#createInjector createInjector}
 *   to return your existing {@link com.google.inject.Injector Injector}.
 * </p>
 * <p>
 *   To turn classes into remote DWR services, use the
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindRemoted(Class) bindRemoted}
 *   or
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindRemotedAs(String,Class) bindRemotedAs}
 *   methods. The binding instructs DWR to handle requests for the given type name (or script name,
 *   if you use the second variant) by getting a fully-injected instance of the given class from the
 *   Guice injector and calling the requested method on that instance.
 *   The target type of these bindings can be an abstract class or interface
 *   bound in the normal Guice way to a concrete class, instance, or provider;
 *   in that case, only the methods defined on the abstract class or
 *   interface are accessible on the remoted object, even if the implementing class has other public
 *   methods. You can supply different bindings for different script names, including
 *   using the same interface with different implementations for different script names,
 *   or different interfaces for different script names mapping to the same implementation
 *   type (assuming it implements both interfaces).
 * </p>
 * <p>
 *   Alternatively, annotate your remoted classes with
 *   {@link org.directwebremoting.annotations.RemoteProxy}, and remotely
 *   callable methods with {@link org.directwebremoting.annotations.RemoteMethod},
 *   using {@code creator=}{@link org.directwebremoting.guice.GuiceCreator}.
 *   When a request comes in for the remoted type, it is retrieved from the Guice injector
 *   using your bindings for that type.
 * </p>
 * <p>
 *   In either case, your data transfer objects should be bound with
 *   {@link org.directwebremoting.annotations.DataTransferObject}, and the
 *   remotely accessible properties with
 *   {@link org.directwebremoting.annotations.RemoteProperty}.
 *   Use {@link org.directwebremoting.guice.AbstractDwrModule#bindAnnotatedClasses bindAnnotatedClasses}
 *   to tell DWR which classes need to be scanned for annotations.
 * </p>
 * <p>
 *   You can bind a type or type pattern string to a custom converter with
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindConversion(Class) bindConversion},
 *   and you can put Ajax filters on scripts with
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindFilter(String) bindFilter}.
 *   Note, however, that you can achieve the effect of an Ajax filter more flexibly using
 *   {@link org.directwebremoting.guice.util.AbstractModule#bindInterceptor(Class,Class[]) bindInterceptor}.
 * </p>
 * <p>
 *   You can install your own DWR {@link org.directwebremoting.extend.Configurator} using
 *   {@code bind(Configurator.class).toInstance(yourConfigurator)},
 *   which then overrides any {@code dwr.xml} configuration.
 *   You'll probably want to use a {@link org.directwebremoting.fluent.FluentConfigurator} for this purpose.
 * </p>
 * <p>
 *   You can still configure DWR's settings via {@code <init-param>}
 *   directives in {@code web.xml}, but usually there is no need to. Most DWR
 *   settings can be set with
 *   {@link org.directwebremoting.guice.AbstractDwrModule#bindParameter(ParamName) bindParameter}.
 *   The {@link org.directwebremoting.guice.ParamName}
 *   enum type lists the available parameters.
 * </p>
 * <p>
 *   For creating your own scopes where the instance injected depends on some
 *   run-time value, create a concrete extension of
 *   {@link org.directwebremoting.guice.util.AbstractContextScope}.
 * </p>
 * <p>For example:</p>
 * <pre>
 *    public final class MyServletContextListener extends DwrGuiceServletContextListener
 *    {
 *        protected void configure()
 *        {
 *            bindRemotedAs("Hello", HelloService.class)
 *                .to(HelloServiceImpl.class)
 *                .in(ApplicationScoped.class);
 *
 *            bindInterceptor(
 *                HelloService.class,
 *                AuthenticationInterceptor.class);
 *
 *            bindFilter("Hello")
 *                .to(TraceFilter.class);
 *
 *            bind(MessageService.class)
 *                .to(MessageServiceImpl.class)
 *                .in(ScriptSessionScoped.class);
 *
 *            bindAnnotatedClasses(
 *                DomainService.class,   // @RemoteProxy(creator=GuiceCreator.class)/@RemoteMethod
 *                HelloRecordImpl.class  // @DataTransferObject/@RemoteProperty
 *            );
 *
 *            // When converting HelloRecord, use existing converter for HelloRecordImpl.
 *            bindConversion(
 *                HelloRecord.class,
 *                HelloRecordImpl.class);
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
 *        }
 *    }
 * </pre>
 * <p>
 *   This example illustrates:
 *   <ul>
 *     <li>two ways to define remoted objects,
 *       <ol>
 *         <li>calling {@link org.directwebremoting.guice.AbstractDwrModule#bindRemotedAs(String,Class) bindRemotedAs} and</li>
 *         <li>annotating with {@link org.directwebremoting.annotations.RemoteProxy};</li>
 *       </ol>
 *     </li>
 *     <li>two ways to define conversions,
 *       <ol>
 *         <li>using {@link org.directwebremoting.guice.AbstractDwrModule#bindConversion(Class) bindConversion} and</li>
 *         <li>using a custom {@link org.directwebremoting.extend.Configurator};</li>
 *       </ol>
 *     </li>
 *     <li>two ways to intercept remote method calls,
 *       <ol>
 *         <li>binding a MethodInterceptor with
 *           {@link org.directwebremoting.guice.util.AbstractModule#bindInterceptor(Class,Class[]) bindInterceptor(Class<?>, Class<?>...)}
 *           and</li>
 *         <li>binding a script name to an {@link org.directwebremoting.AjaxFilter},</li>
 *       </ol>
 *     <li>how to register annotated classes at bind-time; and</li>
 *     <li>how to set a DWR parameter
 *         ({@link org.directwebremoting.guice.ParamName#DEBUG DEBUG}, in this case) at bind-time.</li>
 *   </ul>
 *   It does not use an {@code <init-param>} directive, and it doesn't have
 *   a {@code dwr.xml}.
 * </p>
 * <p>
 *   Note that because application scope is larger than script session scope,
 *   {@code HelloServiceImpl} would have an injected constructor (not shown here)
 *   taking a {@code Provider<MessageService>} rather than a plain {@code MessageService}.
 * </p>
 * <p>
 *   There are several classes in this package with names that start with {@code Internal}.
 *   These classes have to be public with a parameterless constructor so the non-Guicy DWR
 *   machinery can create them. They are not meant to be used directly.
 * </p>
 * <p>
 *   The classes that handle DWR scopes are modeled on the classes in the
 *   {@code com.google.inject.servlet} package, but are independent of them.
 *   You do <em>not</em> need to install com.google.inject.servlet.GuiceFilter and
 *   the Guice com.google.inject.servlet.ServletModule to use the DWR scopes.
 *   There is the potential for binding conflict with this module, however, so this package creates
 *   bindings for the potentially conflicting types using a special {@link org.directwebremoting.guice.Dwr @Dwr}
 *   annotation. It also makes the standard binding if the Guice servlet module is not found in the
 *   class loader. The machinery to achieve this is called automatically with
 *   {@link org.directwebremoting.guice.DwrGuiceServletContextListener}.
 *   If you use
 *   {@link org.directwebremoting.guice.CustomInjectorServletContextListener},
 *   remember to call {@link org.directwebremoting.guice.AbstractDwrModule#bindDwrScopes() bindDwrScopes}
 *   before doing any other bindings.
 *   In both cases, the standard behavior can be overridden explicitly;
 *   see {@link org.directwebremoting.guice.AbstractDwrModule#bindDwrScopes(boolean) bindDwrScopes(boolean)}
 *   and {@link org.directwebremoting.guice.AbstractDwrModule#bindPotentiallyConflictingTypes(boolean) bindPotentiallyConflictingTypes}.
 * </p>
 * @author Tim Peierls [tim at peierls dot net]
 */
package org.directwebremoting.guice;
