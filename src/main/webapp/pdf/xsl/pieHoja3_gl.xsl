<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">
	<xsl:apply-templates/>
</xsl:template>
<xsl:template match="ROWSET">
<table width="255mm" border="0" align="center" >
 
    <tr>

	<td width="65%">
	</td>
     	<td width="35%" height="4mm" border="0" class = "pie"  align = "right">          
        <br/>
         Recibí:<br/><br/>
         En ____________, a  _______________________<br/><br/><br/><br/>
         Asdo: ____________________________________
        </td>
    </tr>

     
</table>
  <xsl:choose>
      <xsl:when test="SALTO">
            <p  STYLE="page-break-after:  always"></p>
      </xsl:when>
  </xsl:choose>

</xsl:template>
<xsl:template match="ASUNTO">
		<td width="120mm" align="left" padding-top="1mm" padding-left="0mm" padding-right="15mm"><xsl:value-of select="."/> </td>
</xsl:template>
</xsl:stylesheet>