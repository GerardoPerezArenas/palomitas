<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="ROWSET">
<table width="125mm" align="center">
    <tr>
      <td align="center" rowspan="4" valign="middle">DEVOLVER ESTA COPIA A:</td>
      <td align="center">EXCMO. AYTO. TOMELLOSO</td>
    </tr>
    <tr>
      <td align="center">PLAZA ESPAÑA 1</td>
    </tr>
    <tr>
      <td align="center">CP 13700</td>
    </tr>
    <tr>
      <td align="center">TOMELLOSO (CIUDAD REAL)</td>
    </tr>
    <tr><td height="2mm" colspan="2"></td></tr>
    <tr>
        <td align="center" colspan="3" ><xsl:text align="right"> Página&nbsp; </xsl:text><pagenumber/><xsl:text align="right"> de &nbsp; </xsl:text><totalpages/></td>
    </tr>
</table>
</xsl:template>



<xsl:template match="ASUNTO">
		<td width="120mm" align="left" padding-top="1mm" padding-left="0mm" padding-right="15mm"><xsl:value-of select="."/> </td>
</xsl:template>

</xsl:stylesheet>



