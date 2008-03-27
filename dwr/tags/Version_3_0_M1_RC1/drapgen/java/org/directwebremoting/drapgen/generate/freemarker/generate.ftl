<#assign noDocumentation = ""> <#-- we sometimes use "(No documentation)"-->/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ${type.packageName};

import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.proxy.io.Context;

/**
 * ${type.documentation!noDocumentation}
 * @author Joe Walker [joe at getahead dot org]
 * @author DRAPGEN - Dwr Reverse Ajax Proxy GENerator
 */
public class ${type.name} <#if type.superClass??>extends ${type.superClass.fullName}</#if>
{
    /**
     * All reverse ajax proxies need context to work from
     * @param scriptProxy The place we are writing scripts to
     * @param extension The string to add to the parent to fetch this object
     * @param parent The script that got us to where we are now
     */
    public ${type.name}(Context parent, String extension, ScriptProxy scriptProxy)
    {
        super(parent, extension, scriptProxy);
    }

    <#list type.constructors as constructor>
    <@createProxyConstructor constructor=constructor/>
    </#list>

    <#list type.constants as constant>
    <@createConstant constant=constant/>
    </#list>

    <#list type.methods as method>
    <@createMethod method=method/>
    </#list>
}

<#macro createProxyConstructor constructor>
    <@subroutineDoc subroutine=constructor/>
    public ${type.name}(<#list constructor.parameters as parameter>${parameter.type} ${parameter.name}<#if parameter_has_next>, </#if></#list>)
    {
        super((Context) null, (String) null, (ScriptProxy) null);
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall("new ${type.name}"<#list constructor.parameters as parameter>, ${parameter.name}</#list>);
        setInitScript(script);
    }

</#macro>

<#macro createConstant constant>
    /**
     * ${constant.documentation!noDocumentation}
     */
    public static final ${constant.type} ${constant.name} = ${constant.value!'null'};

</#macro>

<#macro createMethod method>
  <#-- Simple case: no return, so the generated script is fire and forget -->
  <#if method.returnType.type == 'void'>
    <@createMethodNoReturn method=method/>

  <#-- Pseudo fire and forget, where the method returns 'this'. Detecting this
  can be tricky, but for now we are mandating that the name starts "set" to
  distinguish from cases like "createChildNode". This is a bit of a nasty rule,
  but all (or nearly all) GI setters return this... -->
  <#elseif method.returnType.type == type.fullName && method.name?starts_with('set')>
    <@createMethodReturnThis method=method/>

   <#-- When the return type is itself a proxy, we can postpone the problem of
   getting data from the browser until later. TODO: find a better way of
   detecting proxied classes - we might need to keep and external registry and
   call out to some custom function. -->
  <#elseif method.returnType.type?starts_with('jsx3')>
    <@createMethodProxy method=method/>

    <#-- If the return type has subclasses then we need a second version with an
    extra parameter so we can say something along the lines of "I'm expecting
    getJsxById() to give me a button, get me something that will act like one".
    Note: This is inside the xsl:when clause - if this is a superclass then we
    will get 2 output methods, one with the extra parameter -->
    <#if project.isSuperClass(method.returnType.type)>
      <@createMethodCastingProxy method=method/>
    </#if>

  <#-- When the return type is not a proxy, we call out to the browser -->
  <#else>
    <@createMethodCallback method=method/>

  </#if>
</#macro>

<#macro createMethodNoReturn method>
    <@subroutineDoc subroutine=method/>
    public void ${method.name}(<#list method.parameters as parameter>${parameter.type} ${parameter.name}<#if parameter_has_next>, </#if></#list>)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "${method.name}"<#list method.parameters as parameter>, ${parameter.name}</#list>);
        getScriptProxy().addScript(script);
    }
</#macro>

<#macro createMethodReturnThis method>
    <@subroutineDoc subroutine=method/>
    public ${method.returnType.type} ${method.name}(<#list method.parameters as parameter>${parameter.type} ${parameter.name}<#if parameter_has_next>, </#if></#list>)
    {
        ScriptBuffer script = new ScriptBuffer();
        script.appendCall(getContextPath() + "${method.name}"<#list method.parameters as parameter>, ${parameter.name}</#list>);
        getScriptProxy().addScript(script);
        return this;
    }
</#macro>

<#macro createMethodProxy method>
    <@subroutineDoc subroutine=method/>
    @SuppressWarnings("unchecked")
    public ${method.returnType.type} ${method.name}(<#list method.parameters as parameter>${parameter.type} ${parameter.name}<#if parameter_has_next>, </#if></#list>)
    {
        String extension = "${method.name}(<#list method.parameters as parameter>\"" + ${parameter.name} + "\"<#if parameter_has_next>, </#if></#list>).";
        try
        {
            java.lang.reflect.Constructor<${project.asObject(method.returnType.type)}> ctor = ${method.returnType.type}.class.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported type: " + ${method.returnType.type}.class.getName());
        }
    }
</#macro>

<#macro createMethodCastingProxy method>
    <@subroutineDoc subroutine=method extraTypeParam=true/>
    @SuppressWarnings("unchecked")
    public <T> T ${method.name}(<#list method.parameters as parameter>${parameter.type} ${parameter.name}, </#list>Class<T> returnType)
    {
        String extension = "${method.name}(<#list method.parameters as parameter>\"" + ${parameter.name} + "\"<#if parameter_has_next>, </#if></#list>).";
        try
        {
            java.lang.reflect.Constructor<T> ctor = returnType.getConstructor(Context.class, String.class, ScriptProxy.class);
            return ctor.newInstance(this, extension, getScriptProxy());
        }
        catch (Exception ex)
        {
            throw new IllegalArgumentException("Unsupported return type: " + returnType.getName());
        }
    }
</#macro>

<#macro createMethodCallback method>
    <@subroutineDoc subroutine=method returnInCallback=true/>
    @SuppressWarnings("unchecked")
    public void ${method.name}(<#list method.parameters as parameter>${parameter.type} ${parameter.name}, </#list>org.directwebremoting.proxy.Callback<${project.asObject(method.returnType.type)}> callback)
    {
        ScriptBuffer script = new ScriptBuffer();
        String callbackPrefix = "";

        if (callback != null)
        {
            callbackPrefix = "var reply = ";
        }

        script.appendCall(callbackPrefix + getContextPath() + "${method.name}"<#list method.parameters as parameter>, ${parameter.name}</#list>);

        if (callback != null)
        {
            String key = org.directwebremoting.extend.CallbackHelper.saveCallback(callback, ${project.asObject(method.returnType.type)}.class);
            script.appendCall("__System.activateCallback", key, "reply");
        }

        getScriptProxy().addScript(script);
    }
</#macro>

<#macro subroutineDoc subroutine returnInCallback=false extraTypeParam=false>
    /**
     * ${subroutine.documentation!noDocumentation}
     <#list subroutine.parameters as parameter>
     * @param ${parameter.name} ${parameter.documentation!noDocumentation}
     </#list>
     <#if extraTypeParam>
     * @param returnType The expected return type
     </#if>
     <#if subroutine.returnType?? && subroutine.returnType.documentation??>
     <#if returnInCallback>
     * @param callback ${subroutine.returnType.documentation!noDocumentation}
     <#else>
     * @return ${subroutine.returnType.documentation!noDocumentation}
     </#if>
     </#if>
     */
</#macro>
