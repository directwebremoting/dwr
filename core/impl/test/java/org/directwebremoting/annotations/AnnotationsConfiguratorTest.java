package org.directwebremoting.annotations;

import java.util.List;

import org.directwebremoting.convert.BasicObjectConverter;
import org.directwebremoting.impl.DefaultContainer;
import org.directwebremoting.impl.DefaultConverterManager;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author David Marginian [david at butterdev dot com]
 * This needs to be expanded on a lot, but it's a start.
 */
public class AnnotationsConfiguratorTest
{

    @Test
    public void testConvert() throws IllegalAccessException, InstantiationException {
        AnnotationsConfigurator ac = new AnnotationsConfigurator();
        DefaultContainer dwrContainer = new DefaultContainer();
        DefaultConverterManager converterManager = new DefaultConverterManager();
        dwrContainer.addParameter("org.directwebremoting.extend.ConverterManager", converterManager);
        DataTransferObject convertAnn = AnnotatedBean.class.getAnnotation(DataTransferObject.class);
        ac.processConvert(AnnotatedBean.class, convertAnn, dwrContainer);
        BasicObjectConverter converter = (BasicObjectConverter) converterManager.getConverterByMatchString(AnnotatedBean.class.getName());
        List<String> inclusions = converter.getInclusions();
        Assert.assertTrue(inclusions.contains("convertMe"));
        Assert.assertTrue(inclusions.contains("convertMeFromBase"));
    }

}

