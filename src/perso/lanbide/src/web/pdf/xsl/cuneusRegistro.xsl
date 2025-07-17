<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="//CUNEUS" >
		<xsl:call-template name="pagina"/>
	</xsl:template>

	<xsl:template name="pagina">
	    <xsl:apply-templates select = "//CUNEUS/COPIA" />
    </xsl:template>

	
    <xsl:template match = "//CUNEUS/COPIA" >
		<table border="0" align="center" width="100%"  cellpadding="0mm" height="100%">
			<tr>
				<xsl:choose>
					<xsl:when test="POSICION_CUNEUS= 'ARRIBAIZQA'">
						<td  align="left" vertical-align="top" width="100%" height="100%">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
                    <xsl:when test="POSICION_CUNEUS= 'ARRIBACENTRO'">
						<td  align="center" vertical-align="top" width="100%" height="100%">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
                    <xsl:when test="POSICION_CUNEUS= 'ARRIBADCHA'">
						<td  align="right" vertical-align="top" width="100%" height="100%">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
					<xsl:when test="POSICION_CUNEUS= 'ABAJOIZQA'">
						<td  align="left" vertical-align="bottom" width="100%" height="100%">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
                    <xsl:when test="POSICION_CUNEUS= 'ABAJOCENTRO'">
						<td  align="center" vertical-align="bottom" width="100%" height="100%">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:when>
                    <xsl:otherwise>
						<td  align="right" vertical-align="bottom" width="100%" height="100%">
							<xsl:call-template name="dibujarCuneus"/>
						</td>
					</xsl:otherwise>
				</xsl:choose>
			</tr>
		</table>
	</xsl:template>




	<xsl:template name="dibujarCuneus">
            <table width="205px" height="140px" border="1" cellpadding="0mm" align="center">
		<tr>			
                    <td>			
                        <table width="100%" height="140px" align="center">
                            <tr height="46px" >
                                <td align="left" vertical-align="top">                                            
                                    <img src="images/Lanbide_CL.jpg" width="110px" height="45px"/>
                                </td>
                            </tr>
                            <tr height="14px">
                                    <td align="center" >		
                                            <p class="textoCuneusLan">Bulegoa&nbsp;/&nbsp;Oficina </p>                                                    
                                    </td>
                            </tr>                                   
                            <tr height="34px">
                                    <td  align="center" vertical-align="middle" >		
                                            <p class="textoCuneusLan"><xsl:value-of select="NOMBREOFICINA"/></p>                                            
                                    </td>
                            </tr>
                             <tr height="14px">
                                    <td  align="center">		                                                    
                                            <p class="textoCuneusLan"><xsl:value-of select="substring(FECHA,0,11)"/></p>
                                    </td>
                            </tr>
                            <tr height="12px">
                                    <td  align="center" border-top="1" border-style="solid" vertical-align="middle" padding-top ="4" >
                                        <xsl:choose>
                                                    <xsl:when test="TIPO_REGISTRO = 'S'"> 
                                                        <p class="textoCuneusLan">IRTEERA&nbsp;/&nbsp;SALIDA</p>
                                                    </xsl:when>
                                                    <xsl:when test="TIPO_REGISTRO = 'E'"> 
                                                        <p class="textoCuneusLan">SARRERA&nbsp;/&nbsp;ENTRADA</p>
                                                    </xsl:when>
                                            </xsl:choose>                                                    
                                    </td>
                           </tr>
                           <tr height="12px" >
                                    <td  align="left"  border-top="1" border-style="solid" vertical-align="middle" padding-top ="4">
                                        <p class="textoCuneusLan">&nbsp;&nbsp;&nbsp;Zkia/Nº:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <xsl:value-of select="EJERCICIO"/>&nbsp;/&nbsp;<xsl:value-of select="NUMERO"/>
                                        </p>
                                    </td>                                            
                           </tr>                                  
                        </table>	
                    </td>
		</tr>
            </table>
	</xsl:template>
	
		
</xsl:stylesheet>		
				