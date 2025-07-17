<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">


	

	<xsl:template match="/">

	<table width="100%" border="0" cellpadding="1" align="center" valign="top">
				
		<xsl:for-each select="//informe/titulos/titulo">
		<tr>
			<td class="tituloU" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" 
			border-top="thin solid white">
			<xsl:value-of select="valor"/></td>
			</tr>
		</xsl:for-each>			
	
<tr></tr><tr></tr><tr></tr>
	</table>			
	


	<table width="100%" border="0" cellpadding="1" align="center" valign="top">
	
	<tr>		
		<td class="agrupacionA" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" 
			border-top="thin solid white">
			UNIDADES DE REXISTRO</td>
		<xsl:for-each select="//informe/uors/uor">
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" 
			border-top="thin solid white">
			<xsl:value-of select="nom"/></td>
		</xsl:for-each>			
	</tr>
	
	
			
								
		<xsl:for-each select="//informe/lineas/linea">
		<tr>
		<xsl:for-each select="./contador">
		
		<xsl:if test="(position()=1)">
					<td class="detallePar" valign="bottom" align="center" style="text-align:right" 
			border-left="thin solid white" border-top="thin solid white">
			<xsl:value-of select="valor"/>
			</td>

		
		</xsl:if>
		<xsl:if test="((position()!=1) and (position()!=last()))">
			<td class="detalleImpar" valign="bottom" align="center" style="text-align:right" 
			border-left="thin solid white" border-top="thin solid white">
			<xsl:value-of select="valor"/>
			</td>
		</xsl:if>

		<xsl:if test="(position()= last())">
			<td class="detalle1" valign="bottom" align="center" style="text-align:right" 
			border-left="thin solid white" border-top="thin solid white">
			<xsl:value-of select="valor"/>
			</td>
		</xsl:if>
		
		</xsl:for-each>
			
		</tr>
		
		</xsl:for-each>		
			
	

	
	
	</table>
	
	</xsl:template>				
						
						
</xsl:stylesheet>
