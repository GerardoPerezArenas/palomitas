<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="//CUNEUS" >
		<xsl:call-template name="pagina"/>
	</xsl:template>

	<xsl:template name="pagina">
	    <xsl:apply-templates select = "//CUNEUS/COPIA" />
    </xsl:template>

	
    <xsl:template match = "//CUNEUS/COPIA" >
		<table border="0" align="center" width="206mm"  cellpadding="0mm" height="270mm">
			<tr>
				<xsl:choose>
					<xsl:when test="POSICION_CUNEUS= 'ARRIBAIZQA'">
						<td  align="left" vertical-align="top" width="100%" height="270mm">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
                    <xsl:when test="POSICION_CUNEUS= 'ARRIBACENTRO'">
						<td  align="center" vertical-align="top" width="100%" height="270mm">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
                    <xsl:when test="POSICION_CUNEUS= 'ARRIBADCHA'">
						<td  align="right" vertical-align="top" width="100%" height="270mm">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
					<xsl:when test="POSICION_CUNEUS= 'ABAJOIZQA'">
						<td  align="left" vertical-align="bottom" width="100%" height="270mm">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
                    <xsl:when test="POSICION_CUNEUS= 'ABAJOCENTRO'">
						<td  align="center" vertical-align="bottom" width="100%" height="270mm">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
                    <xsl:otherwise>
						<td  align="right" vertical-align="bottom" width="100%" height="270mm">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:otherwise>
				</xsl:choose>
			</tr>
		</table>
	</xsl:template>




	<xsl:template name="dibujarCuneus">
		<table border="0" width="80mm" height="12mm" cellpadding="0mm" align="center">
		<tr>
			<td width="40mm" height="12mm" align="center" border-bottom="1" border-top="1" border-left="1" vertical-align="middle">
				<xsl:if test="ESCUDO != ''">
					<img src="{ESCUDO}" width="90px" height="45px"/>
				</xsl:if>					
			</td>
			<td width="40mm" height="12mm" align="center" >			
				<table border="1" width="40mm" height="12mm" cellpadding="2mm" align="center">
	                                         	
				<tr>
					<td width="100%" height="5mm" align="center">		
						<p class="textoCuneus"><xsl:value-of select="REGISTRO"/></p>
                                                Oficina
                                                <p class="textoCuneus"><xsl:value-of select="NOMBREOFICINA"/></p>
						<xsl:choose>
							<xsl:when test="TIPO_REGISTRO = 'S' and IDIOMA='1' "> 
								SALIDA
							</xsl:when>
							<xsl:when test="TIPO_REGISTRO = 'S' and IDIOMA='2' "> 
								SAIDA
							</xsl:when>					
							<xsl:when test="TIPO_REGISTRO = 'E'"> 
								ENTRADA 
							</xsl:when>
						</xsl:choose>
                                                
                                               
                                                											
						<xsl:value-of select="EJERCICIO"/>/<xsl:value-of select="NUMERO"/>
						
					</td>
				</tr>
				<tr>
					<td width="100%" height="2mm" align="center" border-top="1" border-style="dotted">
						<p class="textoCuneus"><xsl:value-of select="substring(FECHA,0,11)"/></p>
					</td>
				</tr>
				</table>	
			</td>
		</tr>
	</table>
	</xsl:template>
	
		
</xsl:stylesheet>		
				