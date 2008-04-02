package com.example.dwr.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.example.dwr.asmg.Generator;


/**
 * @author Satish Ramjee
 */
public class PersonAction extends Action
{
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        PersonForm pf = (PersonForm) form;

        String data = generator.generateAntiSpamMailto(pf.getAddress());
        request.setAttribute("antispamlink", data);

        return mapping.findForward("success");
    }

    private Generator generator = new Generator();
}
