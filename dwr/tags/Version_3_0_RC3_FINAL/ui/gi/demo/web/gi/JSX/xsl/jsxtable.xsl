<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" version="1.0">

  

  <xsl:output method="xml" omit-xml-declaration="yes"/>

  <!-- default parameters managed by the class; most can be updated by calling instance methods in the class, jsx3.gui.Table -->
  <xsl:param name="jsxtabindex">0</xsl:param>
  <xsl:param name="jsxselectionbgurl">JSX/images/matrix/select.gif</xsl:param>
  <xsl:param name="jsxid"/>
  <xsl:param name="jsxapppath"/>
  <xsl:param name="jsxabspath"/>
  <xsl:param name="jsxsortpath"/>
  <xsl:param name="jsxsortdirection">ascending</xsl:param>
  <xsl:param name="jsxsorttype">text</xsl:param>
  <xsl:param name="jsxshallowfrom">jsxroot</xsl:param>
  <xsl:param name="jsxasyncmessage"/>
  <xsl:param name="jsxheaderheight"/>
  <xsl:param name="jsxrowstyle1"/>
  <xsl:param name="jsxrowclass1"/>
  <xsl:param name="jsxrowstyle2"/>
  <xsl:param name="jsxrowclass2"/>
  <xsl:param name="jsxcellstyle"/>
  <xsl:param name="jsxcellclass"/>
  <xsl:param name="jsxcellwrap"/>
  <xsl:param name="jsxtablestyles"/>
  <xsl:param name="jsx_img_resolve">1</xsl:param>

  <!-- Users can use these named parameters to further parameterize their templates with custom input parameters -->
  <xsl:param name="jsx_1"/>
  <xsl:param name="jsx_2"/>
  <xsl:param name="jsx_3"/>
  <xsl:param name="jsx_4"/>
  <xsl:param name="jsx_5"/>
  <xsl:param name="jsx_6"/>
  <xsl:param name="jsx_7"/>
  <xsl:param name="jsx_8"/>
  <xsl:param name="jsx_9"/>
  <xsl:param name="jsx_10"/>

  <!-- the root entry template -->
  <xsl:param name="jsxpath"/>
  <xsl:param name="jsxpathapps"/>
  <xsl:param name="jsxpathprefix"/>
<!-- Begin merge from jsxlib.xsl -->
<xsl:template match="* | @*" mode="uri-resolver">
    <xsl:param name="uri" select="."/>
    <xsl:choose>
      <xsl:when test="starts-with($uri,'JSX/')">
        <xsl:value-of select="concat($jsxpath, $uri)"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'JSXAPPS/')">
        <xsl:value-of select="concat($jsxpathapps, $uri)"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'GI_Builder/')">
        <xsl:value-of select="concat($jsxpath, $uri)"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsx:///')">
        <xsl:value-of select="concat($jsxpath, 'JSX/', substring($uri,7))"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsx:/')">
        <xsl:value-of select="concat($jsxpath, 'JSX/', substring($uri,5))"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsxapp:/')">
        <xsl:value-of select="concat($jsxpathapps, substring($uri,8))"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsxuser:///')">
        <xsl:value-of select="concat($jsxpathapps, substring($uri,11))"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsxuser:/')">
        <xsl:value-of select="concat($jsxpathapps, substring($uri,9))"/>
      </xsl:when>
      <xsl:when test="starts-with($uri,'jsxaddin://')">
        <!-- cannot resolve addin links in XSL -->
        <xsl:value-of select="$uri"/>
        <!---->
      </xsl:when>
      <xsl:when test="starts-with($uri,'/')">
        <xsl:value-of select="$uri"/>
      </xsl:when>
      <xsl:when test="contains($uri,'://')">
        <xsl:value-of select="$uri"/>
      </xsl:when>
      <xsl:when test="not($jsxpathprefix='') and not(starts-with($uri, $jsxpathprefix))">
        <xsl:apply-templates mode="uri-resolver" select=".">
          <xsl:with-param name="uri" select="concat($jsxpathprefix, $uri)"/>
        </xsl:apply-templates>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$uri"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
<xsl:template match="* | @*" mode="disable-output-escp">
    <xsl:call-template name="disable-output-escp">
      <xsl:with-param name="value" select="."/>
    </xsl:call-template>
  </xsl:template>
<xsl:template name="disable-output-escp">
    <xsl:param name="value" select="."/>
    <xsl:choose>
      <xsl:when test="function-available('msxsl:node-set')">
        <xsl:value-of disable-output-escaping="yes" select="$value"/>
      </xsl:when>
      <xsl:otherwise>
        <span class="disable-output-escp">
          <xsl:value-of select="$value"/>
        </span>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
<xsl:template name="replace-all">
    <xsl:param name="value" select="."/>
    <xsl:param name="replace" select="''"/>
    <xsl:param name="with" select="''"/>

    <xsl:variable name="first" select="substring-before($value, $replace)"/>
    <xsl:variable name="rest" select="substring-after($value, $replace)"/>

    <xsl:value-of select="$first"/>

    <xsl:if test="$rest">
      <xsl:value-of select="$with"/>
      <xsl:call-template name="replace-all">
        <xsl:with-param name="value" select="$rest"/>
        <xsl:with-param name="replace" select="$replace"/>
        <xsl:with-param name="with" select="$with"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
<!-- End merge from jsxlib.xsl -->
<xsl:template match="/">
    <JSX_FF_WELLFORMED_WRAPPER>
      <xsl:choose>
        <xsl:when test="$jsxasyncmessage and $jsxasyncmessage!=''">
          <xsl:value-of select="$jsxasyncmessage"/>
        </xsl:when>
        <xsl:otherwise>
          <table cellpadding="0" cellspacing="0" class="jsx30table" style="top:{$jsxheaderheight}px;{$jsxtablestyles}">
            <xsl:for-each select="//*[@jsxid=$jsxshallowfrom]/record">
              <xsl:sort data-type="{$jsxsorttype}" order="{$jsxsortdirection}" select="@*[name()=$jsxsortpath]"/>
              <xsl:apply-templates mode="record" select=".">
                <xsl:with-param name="position" select="position()-1"/>
              </xsl:apply-templates>
            </xsl:for-each>
          </table>
        </xsl:otherwise>
      </xsl:choose>
    </JSX_FF_WELLFORMED_WRAPPER>
  </xsl:template>

  <!-- Called by the root template. Renders the TR/TD containers -->
  <xsl:template match="record" mode="record">
    <xsl:param name="position"/>
    <xsl:param name="myselectionbg">
      <xsl:if test="@jsxselected='1'">background-image:url(
        <xsl:value-of select="$jsxselectionbgurl"/>
        );
      </xsl:if>
    </xsl:param>
    <xsl:param name="jsxrowclass">
      <xsl:choose>
        <xsl:when test="$position mod 2 = 0">
          <xsl:value-of select="$jsxrowclass2"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$jsxrowclass1"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:param>
    <xsl:param name="jsxrowstyle">
      <xsl:choose>
        <xsl:when test="$position mod 2 = 0">
          <xsl:value-of select="$jsxrowstyle2"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$jsxrowstyle1"/>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:text>;</xsl:text>
    </xsl:param>

    <tr class="jsx30table {$jsxrowclass}" id="{$jsxid}_{@jsxid}" jsxid="{@jsxid}" jsxposition="{$position}" style="{$jsxrowstyle}" tabindex="{$jsxtabindex}">
      <xsl:if test="@jsxtip">
        <xsl:attribute name="title">
          <xsl:value-of select="@jsxtip"/>
        </xsl:attribute>
      </xsl:if>
    </tr>
  </xsl:template>
</xsl:stylesheet>
