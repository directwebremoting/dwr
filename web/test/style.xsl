<?xml version="1.0"?>
<?altova_samplexml C:\Documents and Settings\Joe\Desktop\test\data.xml?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html"/>
<xsl:template match="/">
<div>
hello
<xsl:value-of select="/start/data"/>
</div>
</xsl:template>
</xsl:stylesheet>
