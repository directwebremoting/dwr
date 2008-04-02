<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:html="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="html jsp xml"
    >

<xsl:output
    method="xml"
    indent="yes"
    encoding="windows-1252"
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
    />

<xsl:template match="/jsp:root">
  <jsp:root version="1.2">
    <xsl:apply-templates/>
  </jsp:root>
</xsl:template>

<xsl:template match="html:html">

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <xsl:copy-of select="html:head/*"/>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    <link rel="stylesheet" type="text/css" href="generic.css"/>
  </head>
  <body>
    <xsl:copy-of select="html:body/@*"/>

    <a href="index.html"><div id="page-logo">DWR</div></a>
    <div id="page-title">Direct Web Remoting - AJAX and XMLHttpRequest made easy</div>

    <div id="page-content">
      <xsl:copy-of select="html:body/*"/>
    </div>

   </body>
</html>

</xsl:template>

</xsl:stylesheet>
