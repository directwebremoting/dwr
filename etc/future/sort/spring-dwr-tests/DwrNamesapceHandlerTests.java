package org.directwebremoting.spring;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletConfig;

import junit.framework.TestCase;

import org.directwebremoting.extend.Converter;
import org.directwebremoting.extend.ConverterManager;
import org.directwebremoting.extend.InboundContext;
import org.directwebremoting.extend.InboundVariable;
import org.directwebremoting.extend.MarshallException;
import org.directwebremoting.impl.ContainerUtil;
import org.directwebremoting.impl.StartupUtil;
import org.directwebremoting.util.FakeServletConfig;
import org.directwebremoting.util.FakeServletContext;
import org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.beans.factory.xml.PluggableSchemaResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.xml.sax.InputSource;

/**
 * @author Brendan Grainger
 */
public class DwrNamesapceHandlerTests extends TestCase
{
    
    private SpringContainer container;
    private StaticWebApplicationContext webappContext;
    
    protected void setUp() throws Exception {
       
        ServletConfig servletConfig = new FakeServletConfig("test", new FakeServletContext());
        
        webappContext = new StaticWebApplicationContext();
        webappContext.setServletConfig(servletConfig);
        
        NamespaceHandlerResolver resolver = new DefaultNamespaceHandlerResolver(getClass().getClassLoader());
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(webappContext);
        reader.setNamespaceHandlerResolver(resolver);
        reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_XSD);
        reader.setEntityResolver(new DummySchemaResolver());
        reader.loadBeanDefinitions(getResource());
        
        container = new SpringContainer();
        container.setBeanFactory(webappContext);
        
        container.addParameter("debug", "true");
        ContainerUtil.setupDefaultContainer(container, servletConfig);

        StartupUtil.initWebContext(servletConfig, servletConfig.getServletContext(), null, container);
        StartupUtil.initServerContext(servletConfig, servletConfig.getServletContext(), container);
        
        // Configure default converters etc
        ContainerUtil.configureFromSystemDwrXml(container);
          
        ContainerUtil.configure(container, Collections.singletonList(webappContext.getBean(DwrNamespaceHandler.DEFAULT_SPRING_CONFIGURATOR_ID)));
        ContainerUtil.publishContainer(container, servletConfig);

        ContainerUtil.debugConfig(container);
    }
    
 
    /**
     * Test that includes are processed. 
     * 
     * Syntax is:
     * <pre>
     *  <dwr:convert type="bean" class="org.directwebremoting.spring.TestBeanIncludeMethods"  >
     *       <dwr:include method="includeMethod1" />
     *       <dwr:include method="includeMethod2" />
     *   </dwr:convert>
     * </pre>
     */
    public void testIncludesWithIncludedMethod() {
        ConverterManager converterManager = (ConverterManager) container.getBean(ConverterManager.class.getName());
        assertNotNull(converterManager);

        Converter converter = converterManager.getConverterByMatchString("org.directwebremoting.spring.TestIncludesBean");
        assertNotNull(converter);

        
        // Check that the included method 
        InboundContext inctx = new InboundContext();
        inctx.createInboundVariable(0, "c0-e1", "string", "included");
        inctx.createInboundVariable(0, "c0-e2", "string", "excluded");
        
        InboundVariable iv = new InboundVariable(inctx, "c0-param0", "Object_Object", "{includedProperty:reference:c0-e1,notIncludedProperty:reference:c0-e2}");
        try
        {
            TestIncludesBean tbi = (TestIncludesBean)converter.convertInbound(TestIncludesBean.class, iv, inctx);
            assertNotNull(tbi.getIncludedProperty());
            assertNull(tbi.getNotIncludedProperty());
        }
        catch (MarshallException ex)
        {
            fail("Included method failed to be marshalled correctly");
        }
    }
    
    
    /**
     * Test that excludes are processed. 
     * 
     * Syntax is:
     * <pre>
     *  <dwr:convert type="bean" class="org.directwebremoting.spring.TestBeanIncludeMethods"  >
     *       <dwr:exclude method="excludeMethod1" />
     *       <dwr:exclude method="excludeMethod2" />
     *   </dwr:convert>
     * </pre>
     */
    public void testExcludes() {
        ConverterManager converterManager = (ConverterManager) container.getBean(ConverterManager.class.getName());
        assertNotNull(converterManager);

        Converter converter = converterManager.getConverterByMatchString("org.directwebremoting.spring.TestExcludesBean");
        assertNotNull(converter);

        // Check that the included method 
        InboundContext inctx = new InboundContext();
        inctx.createInboundVariable(0, "c0-e1", "string", "excluded");
        inctx.createInboundVariable(0, "c0-e2", "string", "included");
        InboundVariable iv = new InboundVariable(inctx, "c0-param0", "Object_Object", "{excludedProperty:reference:c0-e1, notExcludedProperty:reference:c0-e2}");
        
        try
        {
            TestExcludesBean tbi = (TestExcludesBean)converter.convertInbound(TestExcludesBean.class, iv, inctx);
            assertNull(tbi.getExcludedProperty());
            assertNotNull(tbi.getNotExcludedProperty());
        }
        catch (MarshallException ex)
        {
            fail("Included method failed to be marshalled correctly");
        }
    }
    
    /**
     * Test that:
     * <pre>
     *  <dwr:signature class="com.example.Check" >
     *       <dwr:data>
     *           <![CDATA[
     *             import java.util.List;
     *             import org.directwebremoting.spring.Check;
     *             Check.setLotteryResults(List<Integer> nos);
     *             ]]>
     *       </dwr:data>
     *       
     *   </dwr:signature>
     * </pre>
     * is parsed correctly
     * 
     * @throws Exception
     */
    public void testParseSignatures() throws Exception {
        SpringConfigurator config = (SpringConfigurator)webappContext.getBean(DwrNamespaceHandler.DEFAULT_SPRING_CONFIGURATOR_ID);
        assertNotNull(config.getSignatures());
        
        InboundContext inctx = new InboundContext();
        
        inctx.createInboundVariable(0, "c0-e1", "Array", "[reference:c0-e2,reference:c0-e3]");
        inctx.createInboundVariable(0, "c0-e2", "number", "1");
        inctx.createInboundVariable(0, "c0-e3", "number", "2");
        InboundVariable iv = new InboundVariable(inctx, "c0-param0", "Object_Object", "{lotteryResults:reference:c0-e1}");
        
        try
        {
            ConverterManager converterManager = (ConverterManager) container.getBean(ConverterManager.class.getName());
            Converter converter = converterManager.getConverterByMatchString("org.directwebremoting.spring.Check");
            assertNotNull(converter);
            Check tbi = (Check)converter.convertInbound(Check.class, iv, inctx);
            assertEquals(Integer.class, tbi.getLotteryResults().get(0).getClass());
        }
        catch (MarshallException ex)
        {
            fail("Included method failed to be marshalled correctly");
        }
        
    }
    
    private Resource getResource() {
        return new ClassPathResource("dwr-beans.xml", getClass());
    }

    private class DummySchemaResolver extends PluggableSchemaResolver {

        /**
         * 
         */
        public DummySchemaResolver() {
            super(DwrNamesapceHandlerTests.this.getClass().getClassLoader());
        }

        public InputSource resolveEntity(String publicId, String systemId) throws IOException {
            InputSource source = super.resolveEntity(publicId, systemId);
            if (source == null) {
                Resource resource = new ClassPathResource("org/directwebremoting/spring/spring-dwr-2.0.xsd");
                source = new InputSource(resource.getInputStream());
                source.setPublicId(publicId);
                source.setSystemId(systemId);
            }
            return source;
        }
    }
}

