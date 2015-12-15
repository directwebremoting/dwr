<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright (c) 2001-2007, TIBCO Software Inc.
  ~ Use, modification, and distribution subject to terms of license.
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt" version="1.0">

  

  <xsl:output method="xml" omit-xml-declaration="yes"/>

  <xsl:variable name="upperCase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
  <xsl:variable name="lowerCase" select="'abcdefghijklmnopqrstuvwxyz'"/>
  <xsl:param name="jsxtabindex">0</xsl:param>
  <xsl:param name="jsxselectedimage"/>
  <xsl:param name="jsxtransparentimage"/>
  <xsl:param name="jsxdragtype">JSX_GENERIC</xsl:param>
  <xsl:param name="jsxselectedid">null</xsl:param>
  <xsl:param name="jsxsortpath"/>
  <xsl:param name="jsxsortdirection">ascending</xsl:param>
  <xsl:param name="jsxsorttype">text</xsl:param>
  <xsl:param name="jsxid">_jsx</xsl:param>
  <xsl:param name="jsxtext"/>
  <xsl:param name="jsxmode">0</xsl:param>
  <xsl:param name="jsxdisableescape">no</xsl:param>
  <xsl:param name="jsxshallowfrom"/>
  <xsl:param name="jsxcasesensitive">0</xsl:param>
  <xsl:param name="jsxnocheck">0</xsl:param>
  <xsl:param name="jsx_img_resolve">1</xsl:param>
  <xsl:param name="jsx_type">select</xsl:param> <!-- Set to "combo" for combo control XSL -->
  <xsl:param name="jsxtitle"/>
  <xsl:param name="jsxasyncmessage"/>

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
    <xsl:when test="$jsxshallowfrom">
      <xsl:for-each select="//*[@jsxid=$jsxshallowfrom]/record">
        <xsl:sort data-type="{$jsxsorttype}" order="{$jsxsortdirection}" select="@*[name()=$jsxsortpath]"/>
        <xsl:choose>
          <xsl:when test="$jsx_type='select'">
            <xsl:apply-templates mode="select" select="."/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates mode="combo" select="."/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:when>
    <xsl:otherwise>
      <xsl:for-each select="//record">
        <xsl:sort data-type="{$jsxsorttype}" order="{$jsxsortdirection}" select="@*[name()=$jsxsortpath]"/>
        <xsl:choose>
          <xsl:when test="$jsx_type='select'">
            <xsl:apply-templates mode="select" select="."/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates mode="combo" select="."/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:otherwise>
  </xsl:choose>
    </JSX_FF_WELLFORMED_WRAPPER>
  </xsl:template>

  <xsl:template match="record" mode="select">
    <xsl:param name="myjsxid" select="@jsxid"/>

    <div class="jsx30select_{$jsxmode}_option" id="{$jsxid}_{$myjsxid}" jsxid="{@jsxid}" jsxtype="Option" tabindex="{$jsxtabindex}" title="{@jsxtip}">
      <xsl:if test="@jsxstyle">
        <xsl:attribute name="style">
          <xsl:value-of select="@jsxstyle"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="$jsxnocheck != '1'">
        <xsl:choose>
          <xsl:when test="$jsxselectedid=@jsxid">
            <img class="jsx30select_check" src="{$jsxselectedimage}" unselectable="on"/>
          </xsl:when>
          <xsl:otherwise>
            <img class="jsx30select_check" src="{$jsxtransparentimage}" unselectable="on"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:if>
      <xsl:if test="@jsximg and @jsximg != ''">
        <xsl:variable name="src1">
          <xsl:choose>
            <xsl:when test="$jsx_img_resolve='1'">
              <xsl:apply-templates mode="uri-resolver" select="@jsximg"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="@jsximg"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
        <img class="jsx30select_icon" src="{$src1}" unselectable="on"/>
      </xsl:if>
      <span>
        <xsl:apply-templates mode="jsxtext" select="."/>
      </span>
    </div>
  </xsl:template>

  <xsl:template match="record" mode="combo">
    <xsl:variable name="mytext">
      <xsl:choose>
        <xsl:when test="@jsxtext">
          <xsl:value-of select="@jsxtext"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@jsxid"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:if test="(not($jsxcasesensitive = 1) and starts-with(translate($mytext, $lowerCase, $upperCase), translate($jsxtext, $lowerCase, $upperCase)))         or (starts-with($mytext, $jsxtext))">
      <div class="jsx30select_{$jsxmode}_option" id="{$jsxid}_{@jsxid}" jsxid="{@jsxid}" jsxtype="Option" tabindex="{$jsxtabindex}" title="{@jsxtip}">
        <xsl:if test="@jsxstyle">
          <xsl:attribute name="style">
            <xsl:value-of select="@jsxstyle"/>
          </xsl:attribute>
        </xsl:if>
        <xsl:if test="$jsxnocheck != '1'">
          <xsl:choose>
            <xsl:when test="$jsxselectedid=@jsxid">
              <img class="jsx30select_check" src="{$jsxselectedimage}" unselectable="on"/>
            </xsl:when>
            <xsl:otherwise>
              <img class="jsx30select_check" src="{$jsxtransparentimage}" unselectable="on"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:if>
        <xsl:if test="@jsximg and @jsximg != ''">
          <xsl:variable name="src1">
            <xsl:choose>
              <xsl:when test="$jsx_img_resolve='1'">
                <xsl:apply-templates mode="uri-resolver" select="@jsximg"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="@jsximg"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <img class="jsx30select_icon" src="{$src1}" unselectable="on"/>
        </xsl:if>
        <span>
          <xsl:apply-templates mode="jsxtext" select=".">
            <xsl:with-param name="value" select="$mytext"/>
          </xsl:apply-templates>
        </span>
      </div>
    </xsl:if>
  </xsl:template>

  <xsl:template match="record" mode="jsxtext">
    <xsl:param name="value" select="@jsxtext"/>

    <xsl:choose>
      <xsl:when test="$jsxdisableescape='yes'">
        <xsl:call-template name="disable-output-escp">
          <xsl:with-param name="value" select="$value"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$value"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
