<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">


   <xsl:attribute-set name="section.level1.properties">
     <xsl:attribute name="break-before">page</xsl:attribute>
   </xsl:attribute-set>


    <xsl:template match="/">
	    <xsl:apply-templates/>
    </xsl:template>

<xsl:template match="ROWSET">

            <pbr/>

</xsl:template>



</xsl:stylesheet>



