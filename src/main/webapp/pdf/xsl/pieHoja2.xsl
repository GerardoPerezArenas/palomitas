<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">
	<xsl:apply-templates/>
</xsl:template>
<xsl:template match="ROWSET">
<table width="255mm" align="center">
    <tr>
        <td height="10mm">

        </td>
    </tr>
    <tr>
                  <td width="100%" height="4mm" colspan= "3" class = "pie" style="font-style:italic" valign="middle" align = "center">
                         <xsl:value-of select="FECHA"/>
      </td>
    </tr>
    <tr>
      <td align="center" class = "pie"  height="4mm" width="30%">
             Recibí Conforme (Nombre y Apellidos):
      </td>
      <td align="center" height="2mm" width="5%">
      </td>
      <td align="center"  class = "pie" height="2mm" width="65%">
              Observaciones:
      </td>
    </tr>
    <tr>
      <td align="center" border="1" height="20mm"></td>
      <td align="center" height="20mm"></td>
      <td align="center" border="1" height="20mm"></td>
    </tr>
    <tr><td height="2mm"></td></tr>
   
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