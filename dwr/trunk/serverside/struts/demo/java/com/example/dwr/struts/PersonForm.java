package com.example.dwr.struts;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author Satish Ramjee
 */
public class PersonForm extends ValidatorForm
{
    private String name;

    private String address;

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = super.validate(mapping, request);

        if (errors == null)
        {
            errors = new ActionErrors();
        }

        StringTokenizer tok = new StringTokenizer(name);
        if (tok.countTokens() < 2)
        {
            errors.add("name", new ActionMessage("error.fullname"));
        }

        return errors;
    }
}
