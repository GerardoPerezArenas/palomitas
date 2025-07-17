<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">


	

	<xsl:template match="/">
	<table width="100%" border="0" cellpadding="1" align="center" valign="top">
	

	<tr>	
	
		<xsl:element name="td">
			<xsl:attribute name="colspan"><xsl:value-of select="count(//informe/titulos/titulo)"/></xsl:attribute>			
		</xsl:element>
																																				
		<xsl:if test="//informe/verPend = 'true'">
			<td align="center" valign="bottom" class="encabezado" style="text-align:center; margin-top:1" border-left="thin solid white" colspan="3">
			Pendientes</td>
		</xsl:if>
		<xsl:if test="//informe/verFin = 'true'">
			<td align="center" valign="bottom" class="encabezado" style="text-align:center; margin-top:1" border-left="thin solid white" colspan="3">
			Realizadas
			<xsl:if test="(//informe/tiempo = 'sem.') or (//informe/tiempo = 'mes') or (//informe/tiempo = 'trim.')">
				&nbsp;<xsl:value-of select="//informe/tiempo"/>
			</xsl:if> 
			</td>		
		</xsl:if>
		<xsl:if test="//informe/verVol = 'true'">
			<td align="center" valign="bottom" class="encabezado" style="text-align:center; margin-top:1" border-left="thin solid white" colspan="3">
			Expedientes</td>	
		</xsl:if>	
	</tr>
	
	<tr>		
		<xsl:call-template name="titulos">
		</xsl:call-template>						
		
		<xsl:if test="//informe/verPend = 'true'">
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" border-top="thin solid white">
			Tareas</td>
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" border-top="thin solid white">
			Exped.</td>
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" border-top="thin solid white">
			Tiempos</td>
		</xsl:if>	
		<xsl:if test="//informe/verFin = 'true'">			
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" border-top="thin solid white">
			Tareas</td>
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" border-top="thin solid white">
			Exped.</td>
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" border-top="thin solid white">
			Tiempos</td>
		</xsl:if>		
		<xsl:if test="//informe/verVol = 'true'">
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" border-top="thin solid white">
			Final.</td>
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" border-top="thin solid white">
			En&nbsp;tram.</td>
			<td class="encabezado" valign="bottom" align="center" style="text-align:right" border-left="thin solid white" border-top="thin solid white">
			Totales</td>
		</xsl:if>
							
	</tr>

	
	<xsl:call-template name="lineas">
	</xsl:call-template>
	
	
	</table>
	
	
	</xsl:template>

		
	<xsl:template name="titulos">
		<xsl:for-each select="//informe/titulos/titulo">
			<xsl:choose>
				<xsl:when test="position()=1">
					<td class="titulo1"><xsl:value-of select="valor"/>&nbsp;&nbsp;</td>			
			    </xsl:when>
				<xsl:when test="position()=last()">
					<td class="tituloU"><xsl:value-of select="valor"/>&nbsp;&nbsp;</td>			
			    </xsl:when>
				<xsl:when test="(position() mod 2) = 0">
					<td class="tituloPar"><xsl:value-of select="valor"/>&nbsp;&nbsp;</td>			
				</xsl:when>
				<xsl:otherwise>
					<td class="tituloImpar"><xsl:value-of select="valor"/>&nbsp;&nbsp;</td>			
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:for-each>
	</xsl:template>

	
	
	<xsl:template name="lineas">
		<xsl:variable name="est"/>
		<xsl:for-each select="//informe/lineas/linea">
		<tr>		
			<xsl:for-each select="agrupaciones/agrupacion">
				
				<xsl:if test="( (valor!='####') and (valor!='') )">
				
				<xsl:call-template name="linea">
				
					<xsl:with-param name="colspan">
							<xsl:value-of select="last()-position()+1"/>
					</xsl:with-param>
				
				
					<xsl:with-param name="estilo">
						<xsl:choose>
							<xsl:when test="(position()=1) and ((starts-with(valor,'Tod'))or(starts-with(valor,'inf_Resumen')))">A</xsl:when>																											
							<xsl:when test="position()=last()">U</xsl:when>
							<xsl:when test="position()>1">
								<xsl:choose>
									<xsl:when test="(position() mod 2) = 0">Par</xsl:when>
									<xsl:otherwise>Impar</xsl:otherwise>										
								</xsl:choose>
							</xsl:when>																					
							<xsl:when test="valor!=''">1</xsl:when> 

						</xsl:choose>
					</xsl:with-param>
				
					<xsl:with-param name="valor">
						<xsl:choose>
							<xsl:when test="valor='inf_Resumen'">Resumen general</xsl:when>
							<xsl:when test="valor='999-inf_PendAsign'">999-PENDIENTE DE ASIGNACIÓN</xsl:when>
							<xsl:otherwise><xsl:value-of select="valor"/></xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>															
					
					<xsl:with-param name="tareasPendientes"><xsl:value-of select="../../tareasPendientes"/></xsl:with-param>
					<xsl:with-param name="expedientesPendientes"><xsl:value-of select="../../expedientesPendientes"/></xsl:with-param>
					<xsl:with-param name="tiemposPendientes"><xsl:value-of select="../../tiemposPendientes"/></xsl:with-param>
                                        <xsl:with-param name="tareasHistoricas"><xsl:value-of select="../../tareasHistoricas"/></xsl:with-param>
					<xsl:with-param name="expedientesHistoricos"><xsl:value-of select="../../expedientesHistoricos"/></xsl:with-param>
					<xsl:with-param name="tiemposHistoricos"><xsl:value-of select="../../tiemposHistoricos"/></xsl:with-param>
					<xsl:with-param name="expedientesCerrados"><xsl:value-of select="../../expedientesCerrados"/></xsl:with-param>
					<xsl:with-param name="expedientesEnTramitacion"><xsl:value-of select="../../expedientesEnTramitacion"/></xsl:with-param>
					<xsl:with-param name="expedientesTotales"><xsl:value-of select="../../expedientesTotales"/></xsl:with-param>
					
				</xsl:call-template>
							
				
							</xsl:if>
				
				<xsl:if test="valor=''"><td>&nbsp;</td></xsl:if>
												
			</xsl:for-each>												
																																																								
		</tr>    				
		</xsl:for-each>
	</xsl:template>
				
				
	<xsl:template name="linea">
		<xsl:param name="colspan">1</xsl:param>
		<xsl:param name="estilo">agrupacionU</xsl:param>
		<xsl:param name="valor"/>			
		<xsl:param name="tareasPendientes"/>
		<xsl:param name="expedientesPendientes"/>
		<xsl:param name="tiemposPendientes"/>
                <xsl:param name="tareasHistoricas"/>
		<xsl:param name="expedientesHistoricos"/>
		<xsl:param name="tiemposHistoricos"/>
		<xsl:param name="expedientesCerrados"/>
		<xsl:param name="expedientesEnTramitacion"/>
		<xsl:param name="expedientesTotales"/>
				
		<xsl:element name="td">
				<xsl:attribute name="colspan"><xsl:value-of select="$colspan"/></xsl:attribute>
				<xsl:attribute name="valign">middle</xsl:attribute>				
				<xsl:if test="($estilo='Par') or ($estilo='Impar')">
					<xsl:attribute name="valign">bottom</xsl:attribute>
				</xsl:if>
				<xsl:attribute name="class">agrupacion<xsl:value-of select="$estilo"/></xsl:attribute>
				<xsl:if test="$estilo='Par'">
					<xsl:attribute name="border-bottom">1 solid #993300</xsl:attribute>
				</xsl:if>
				<xsl:if test="$estilo='Impar'">
					<xsl:attribute name="border-bottom">1 solid #333399</xsl:attribute>
				</xsl:if>
				 <xsl:if test="$estilo='U'">
					<xsl:attribute name="border-left">1 solid Navy</xsl:attribute>
				</xsl:if>
									
				<xsl:value-of select="$valor"/>
		</xsl:element>						
		
		<xsl:call-template name="cifras">
			<xsl:with-param name="estilo">detalle<xsl:value-of select="$estilo"/></xsl:with-param>
			<xsl:with-param name="tareasPendientes"><xsl:value-of select="$tareasPendientes"/></xsl:with-param>
			<xsl:with-param name="expedientesPendientes"><xsl:value-of select="$expedientesPendientes"/></xsl:with-param>
			<xsl:with-param name="tiemposPendientes"><xsl:value-of select="$tiemposPendientes"/></xsl:with-param>
                        <xsl:with-param name="tareasHistoricas"><xsl:value-of select="$tareasHistoricas"/></xsl:with-param>
			<xsl:with-param name="expedientesHistoricos"><xsl:value-of select="$expedientesHistoricos"/></xsl:with-param>
			<xsl:with-param name="tiemposHistoricos"><xsl:value-of select="$tiemposHistoricos"/></xsl:with-param>
			<xsl:with-param name="expedientesCerrados"><xsl:value-of select="$expedientesCerrados"/></xsl:with-param>
			<xsl:with-param name="expedientesEnTramitacion"><xsl:value-of select="$expedientesEnTramitacion"/></xsl:with-param>
			<xsl:with-param name="expedientesTotales"><xsl:value-of select="$expedientesTotales"/></xsl:with-param>
		</xsl:call-template>
												
	</xsl:template>
				
				
				
	<xsl:template name="cifras">
		<xsl:param name="estilo"></xsl:param>
		<xsl:param name="tareasPendientes"></xsl:param>
		<xsl:param name="expedientesPendientes"/>
		<xsl:param name="tiemposPendientes"/>
                <xsl:param name="tareasHistoricas"></xsl:param>
		<xsl:param name="expedientesHistoricos"/>
		<xsl:param name="tiemposHistoricos"/>
		<xsl:param name="expedientesCerrados"/>
		<xsl:param name="expedientesEnTramitacion"/>
		<xsl:param name="expedientesTotales"/>

		<xsl:if test="//informe/verPend = 'true'">
			<xsl:call-template name="cifra">
				<xsl:with-param name="estilo"><xsl:value-of select="$estilo"/></xsl:with-param>
				<xsl:with-param name="valor"><xsl:value-of select="$tareasPendientes"/></xsl:with-param>
				<xsl:with-param name="borde">Si</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="cifra">
				<xsl:with-param name="estilo"><xsl:value-of select="$estilo"/></xsl:with-param>
				<xsl:with-param name="valor"><xsl:value-of select="$expedientesPendientes"/></xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="cifra">
				<xsl:with-param name="estilo"><xsl:value-of select="$estilo"/></xsl:with-param>
				<xsl:with-param name="valor"><xsl:value-of select="$tiemposPendientes"/></xsl:with-param>
                                <xsl:with-param name="bordeFinal">Si</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="//informe/verFin = 'true'">
			<xsl:call-template name="cifra">
				<xsl:with-param name="estilo"><xsl:value-of select="$estilo"/></xsl:with-param>
				<xsl:with-param name="valor"><xsl:value-of select="$tareasHistoricas"/></xsl:with-param>
				<xsl:with-param name="borde">Si</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="cifra">
				<xsl:with-param name="estilo"><xsl:value-of select="$estilo"/></xsl:with-param>
				<xsl:with-param name="valor"><xsl:value-of select="$expedientesHistoricos"/></xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="cifra">
				<xsl:with-param name="estilo"><xsl:value-of select="$estilo"/></xsl:with-param>
				<xsl:with-param name="valor"><xsl:value-of select="$tiemposHistoricos"/></xsl:with-param>
                                <xsl:with-param name="bordeFinal">Si</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="//informe/verVol = 'true'">
			<xsl:call-template name="cifra">
				<xsl:with-param name="estilo"><xsl:value-of select="$estilo"/></xsl:with-param>
				<xsl:with-param name="valor"><xsl:value-of select="$expedientesCerrados"/></xsl:with-param>
				<xsl:with-param name="borde">Si</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="cifra">
				<xsl:with-param name="estilo"><xsl:value-of select="$estilo"/></xsl:with-param>
				<xsl:with-param name="valor"><xsl:value-of select="$expedientesEnTramitacion"/></xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="cifra">
				<xsl:with-param name="estilo"><xsl:value-of select="$estilo"/></xsl:with-param>
				<xsl:with-param name="valor"><xsl:value-of select="$expedientesTotales"/></xsl:with-param>
                                <xsl:with-param name="bordeFinal">Si</xsl:with-param>
			</xsl:call-template>
		</xsl:if>		
	</xsl:template>
	
	
	<xsl:template name="cifra">
		<xsl:param name="estilo"/> 
		<xsl:param name="valor"/> 		
		<xsl:param name="borde"/>
                <xsl:param name="bordeFinal"/>
		<xsl:element name="td">
			<xsl:attribute name="class"><xsl:value-of select="$estilo"/></xsl:attribute>	
			<xsl:if test="$borde='Si'">		
				<xsl:attribute name="border-left">thin solid black</xsl:attribute>
			</xsl:if>
                        <xsl:if test="$bordeFinal='Si'">
				<xsl:attribute name="border-right">thin solid black</xsl:attribute>
			</xsl:if>
			<xsl:attribute name="valign">middle</xsl:attribute>
			<xsl:attribute name="align">right</xsl:attribute>
			<xsl:value-of select="$valor"/> 
		</xsl:element>
	</xsl:template>
	
						
						
						
</xsl:stylesheet>
