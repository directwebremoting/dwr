/**
 * 
 */
package com.example.dwr.stress;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.directwebremoting.Container;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.util.LocalUtil;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Settings
{
    /**
     * 
     */
    public String setValue(String name, String value)
    {
        StringBuffer reply = new StringBuffer();

        Container container = WebContextFactory.get().getContainer();
        Collection<String> beanNames = container.getBeanNames();
        for (String beanName : beanNames)
        {
            Object bean = container.getBean(beanName);

            Class<? extends Object> beanClass = bean.getClass();
            if (beanClass == String.class)
            {
                continue;
            }

            try
            {
                for (Field field : LocalUtil.getAllFields(beanClass))
                {
                    if (!field.getName().equals(name))
                    {
                        continue;
                    }

                    if (Modifier.isStatic(field.getModifiers()) ||
                        Modifier.isFinal(field.getModifiers()))
                    {
                        continue;
                    }

                    if (!LocalUtil.isTypeSimplyConvertable(field.getType()))
                    {
                        continue;
                    }

                    Object converted = LocalUtil.simpleConvert(value, field.getType());
                    field.setAccessible(true);
                    field.set(bean, converted);

                    reply.append("Set on " + beanClass.getName() + "\n");
                }
            }
            catch (Exception ex)
            {
                reply.append("Failed on " + beanClass.getName() + ": " + ex + "\n");
            }
        }

        return reply.toString();
    }

    /**
     * 
     */
    public Map<String, Setting> getSettingValues()
    {
        Map<String, Setting> reply = new HashMap<String, Setting>();

        Container container = WebContextFactory.get().getContainer();
        Collection<String> beanNames = container.getBeanNames();
        for (String beanName : beanNames)
        {
            Object bean = container.getBean(beanName);

            try
            {
                Class<? extends Object> beanClass = bean.getClass();
                if (beanClass == String.class)
                {
                    continue;
                }

                for (Field field : LocalUtil.getAllFields(beanClass))
                {
                    String name = field.getName();
                    if (name.equals("class"))
                    {
                        continue;
                    }

                    if (Modifier.isStatic(field.getModifiers()) ||
                        Modifier.isFinal(field.getModifiers()))
                    {
                        continue;
                    }

                    if (!LocalUtil.isTypeSimplyConvertable(field.getType()))
                    {
                        continue;
                    }

                    Setting setting = reply.get(name);
                    if (setting == null)
                    {
                        setting = new Setting(name);
                        reply.put(name, setting);
                    }

                    setting.getClassNames().add(beanClass.getName());
                    field.setAccessible(true);
                    Object value = field.get(bean);
                    Object oldValue = setting.getValue();

                    if (oldValue != null && value != null && !value.equals(oldValue))
                    {
                        setting.setWritable(false);
                        setting.setValue(oldValue.toString() + " / " + value);
                    }
                    else
                    {
                        setting.setValue(value);
                    }
                }
            }
            catch (Exception ex)
            {
                log.warn("Failed to introspect: " + beanName, ex);
            }
        }

        return reply;
    }

    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(Settings.class);
}
