<%@ page language="java" session="false" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <title>Anti-spam mailto: Link Creator</title>
  <meta http-equiv="Content-Type" content="text/html; charset=us-ascii" />
</head>

<body>

  <h1>Anti-spam mailto: Link Creator.</h1>

  <html:form action="/person">
    <table>
      <tr>
        <td>Name:</td>
        <td><html:text property="name" size="16"/></td>
        <td><html:errors property="name"/></td>
      </tr>
      <tr>
        <td>EMail Address:</td>
        <td><html:text property="address" size="16"/></td>
        <td><html:errors property="address"/></td>
      </tr>
    </table>
    <html:submit>Submit Query</html:submit>
  </html:form>

</body>
</html>
