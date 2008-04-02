<%@ page isELIgnored="false" import="java.util.*, uk.ltd.getahead.abc.*" %><%
//Actions actions = Factory.getActions();
String actionid = request.getParameter("actionid");
//Map map = actions.readAction(actionid);
//request.setAttribute("map", map);
request.setAttribute("now", new Date());
%>From: Redmond Peel <red@air-band.info>
To: ${requestScope.map.fullname} <${requestScope.map.email}>
Date: ${requestScope.now}
Subject: Payment Reminder for ${requestScope.map.servname}

Dear ${requestScope.map.fullname},

You are late paying for ${requestScope.map.servname}.
We will cut you off soon if you don't pay up.

Your mate,

Red.
