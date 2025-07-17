<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/">
			
		<table border="0" width="100%">
			<tr>
				<td width="50%"><xsl:value-of select="//cabecera/organizacion"/><br/>Padrón</td>
				<td width="50%" align="right"><xsl:value-of select="//cabecera/fecha"/><br/>Página: <pagenumber /></td>
			</tr>
		</table>

	</xsl:template>

</xsl:stylesheet>
