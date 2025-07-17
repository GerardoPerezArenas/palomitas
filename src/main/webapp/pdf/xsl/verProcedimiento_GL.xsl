<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="//procedimiento" > 
		<xsl:call-template name="pagina"/>
	</xsl:template> 

	<xsl:template name="pagina">

		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td height="6mm"></td></tr>
			<tr>
				<td>
					<table border="0" align="center" width="180mm" height="3mm" cellpadding="0px">
						<tr>
							<td width="100%" class="cabecera" align="left"> PROCEDEMENTO: <xsl:value-of select="nombre"/></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr><td height="8mm"></td></tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
						<tr><td width="100%">
							<table width="180mm" border="0px" cellpadding="0px">
							<tr>
								<td width="30mm" class="etiqueta" align="left">Código:</td>
								<td width="150mm" class="valor" align="left"><xsl:value-of select="codigo"/></td>
							</tr>
							</table>
						</td></tr>
						<tr><td width="100%" height="2mm"></td></tr>
		</table>
		<table border="1px" align="center" width="200mm"  cellpadding="1mm">
			<tr>
				<td width="200mm" colspan="2" class="etiqueta" align="center"><u>Datos Xerais</u></td>
			</tr>
			<tr>
				<td width="200mm" colspan="2" height="2mm"></td>
			</tr>
			<tr>
				<td width="40mm" class="etiqueta" align="left">Data Vixencia Dende::</td>
				<td width="160mm" align="left"><xsl:value-of select="fechaDesde"/></td>
			</tr>
			<tr>
				<td width="40mm" class="etiqueta" align="left">Data Vixencia Ata:</td>
				<td width="160mm" align="left"><xsl:value-of select="fechaHasta"/></td>
			</tr>
			<tr>
				<td width="40mm" class="etiqueta" align="left">Área:</td>
				<td width="160mm" align="left"><xsl:value-of select="nombreArea"/></td>
			</tr>
			<tr>
				<td width="40mm" class="etiqueta" align="left">Tipo Procedemento:</td>
				<td width="160mm" align="left"><xsl:value-of select="nombreTipoProcedimiento"/></td>
			</tr>
			<tr>
				<td width="40mm" class="etiqueta" align="left">Unidade de Inicio:</td>
				<td width="160mm" align="left">
                                    <xsl:apply-templates select = "//procedimiento/unidadInicio" /></td>
			</tr>
			<tr>
				<td width="40mm" class="etiqueta" align="left">Tipo de Inicio:</td>
				<td width="160mm" align="left"><xsl:value-of select="tipoInicio"/></td>
			</tr>
			<tr>
				<td width="200mm" colspan="2" height="3mm"></td>
			</tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td width="100%" height="2mm"></td></tr>
		</table>
		<table border="1px" align="center" width="200mm"  cellpadding="1mm">
			<tr>
				<td width="200mm" colspan="2" class="etiqueta" align="left">Lingazóns:</td>
			</tr>
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" align="left"><xsl:apply-templates select = "//procedimiento/enlaces" /></td>
			</tr>
			<tr><td width="200mm" colspan="2" height="2mm"></td></tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
						<tr><td width="100%" height="2mm"></td></tr>
		</table> 
		<table border="1px" align="center" width="200mm"  cellpadding="1mm">
			<tr>
				<td width="200mm" colspan="2" class="etiqueta" align="left">Documentación Aportar:</td>
			</tr>
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" align="left"><xsl:apply-templates select = "//procedimiento/documento" /></td>
			</tr>
		    <tr><td width="200mm" colspan="2" height="2mm"></td></tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td width="100%" height="2mm"></td></tr>
		</table> 
		<table border="1px" align="center" width="200mm" cellpadding="1mm">
			<tr>
				<td width="200mm" colspan="2" class="etiqueta" align="left">Campos:</td>
			</tr>
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" align="left"><xsl:apply-templates select = "//procedimiento/campo" /></td>
			</tr>
			<tr><td width="200mm" colspan="2" height="3mm"></td></tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td width="100%" height="3mm"></td></tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="2mm">
			<tr>
				<td width="180mm" class="subtitulo" align="left">TRÁMITES</td>
			</tr>
			<tr><td width="100%" height="2mm"></td></tr>
		</table>	
		<xsl:apply-templates select = "//procedimiento/tramite" />	
	
	</xsl:template> 
	
	<xsl:template match = "//procedimiento/unidadInicio" > 		
		<table align="center" width="160mm" height="3mm" cellpadding="1px" border="0px">
		<tr>
			<td width="160mm">- <xsl:value-of select="nombreUnidadInicio"/></td>
		</tr>
		<tr><td width="160mm" height="1mm"></td></tr>
		</table>
    
	</xsl:template>        
	
	<xsl:template match = "//procedimiento/enlaces" > 		
		<table align="center" width="180mm" height="3mm" cellpadding="1px" border="0px">
		<tr>
			<td width="180mm">- <xsl:value-of select="nombreEnlace"/></td>
		</tr>
		<tr><td width="180mm" height="1mm"></td></tr>
		</table>
    
	</xsl:template>
	
	<xsl:template match = "//procedimiento/documento" > 		
		<table align="center" width="180mm" height="3mm" cellpadding="1px" border="0px">
		<tr>
			<td width="180mm">- <xsl:value-of select="nombreDoc"/></td>
		</tr>
		<tr><td width="180mm" height="1mm"></td></tr>
		</table>
    
	</xsl:template> 

	<xsl:template match = "//procedimiento/campo" > 		
		<table align="center" width="180mm" height="3mm" cellpadding="1px" border="0px">
		<tr>
			<td width="180mm">- <xsl:value-of select="nombreCampo"/></td>
	    </tr>
		<tr><td width="180mm" height="1mm"></td></tr>
		</table>
    
	</xsl:template>  
	
	<xsl:template match = "//procedimiento/tramite" > 		
		<table align="center" width="200mm" height="4mm" cellpadding="2px">
			<tr>
				<td width="200mm"  height="4mm" class="etiqueta" align="center"><xsl:value-of select="nombreTramite"/></td>
			</tr>
			<tr>
				<td width="200mm"  height="3mm"></td>
			</tr>
		</table>
		<table border="1px" align="center" width="200mm"  cellpadding="1mm">
			<tr>
				<td width="200mm" colspan="2" class="etiqueta" align="center"><u>Datos Xerais</u></td>
			</tr>
			<tr>
				<td width="200mm" colspan="2" height="2mm"></td>
			</tr>
			<tr>
				<td width="45mm" class="etiqueta" align="left">Clasificación Trámite:</td>
				<td width="155mm" align="left"><xsl:value-of select="nombreClasifTram"/></td>
			</tr>
			<tr>
				<td width="45mm" class="etiqueta" align="left">Prazo:</td>
				<td width="155mm" align="left"><xsl:value-of select="plazo"/></td>
			</tr>
			<tr>
				<td width="45mm" class="etiqueta" align="left">Unidade Inicio(Manual):</td>
				<td width="155mm" align="left"><xsl:value-of select="nombreUnidadInicio"/></td>
			</tr>
                        <tr>
				<td width="45mm" class="etiqueta" align="left">Unidade do trámite:</td>
				<td width="155mm" align="left">
                                    <xsl:apply-templates select = "unidadInicioTramite"/></td>
			</tr>
			<tr>
				<td width="200mm" colspan="2" height="3mm"></td>
			</tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td width="100%" height="2mm"></td></tr>
		</table>
		<table border="1px" align="center" width="200mm"  cellpadding="1mm">
			<tr>
				<td width="200mm" colspan="2" class="etiqueta" align="left">Lingazóns:</td>
			</tr>
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" align="left"><xsl:apply-templates select = "enlacesT" /></td>
			</tr>
			<tr><td width="200mm" colspan="2" height="2mm"></td></tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td width="100%" height="2mm"></td></tr>
		</table>
		<table border="1px" align="center" width="200mm"  cellpadding="1mm">
			<tr>
				<td width="200mm" colspan="2" class="etiqueta" align="left">Documentos:</td>
			</tr>
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" align="left"><xsl:apply-templates select = "documentoT" /></td>
			</tr>
			<tr><td width="200mm" colspan="2" height="2mm"></td></tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td width="100%" height="2mm"></td></tr>
		</table>
		<table border="1px" align="center" width="200mm"  cellpadding="1mm">
			<tr>
				<td width="200mm" colspan="2" class="etiqueta" align="left">Campos:</td>
			</tr>
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" align="left"><xsl:apply-templates select = "campoT" /></td>
			</tr>
			<tr><td width="200mm" colspan="2" height="2mm"></td></tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td width="100%" height="2mm"></td></tr>
		</table>
		<table border="1px" align="center" width="200mm"  cellpadding="1mm">
			<tr>
				<td width="200mm" colspan="2" class="etiqueta" align="left">Condicións Entrada:</td>
			</tr>
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" align="left"><xsl:apply-templates select = "condEntrada" /></td>
			</tr>
			<tr><td width="200mm" colspan="2" height="2mm"></td></tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td width="100%" height="2mm"></td></tr>
		</table>
		<table border="1px" align="center" width="200mm"  cellpadding="1mm">
		    <tr>
				<td width="200mm" colspan="3" class="etiqueta" align="left">Condicións Saida<xsl:value-of select="tipoCond"/><xsl:value-of select="obligatorio"/></td>
			</tr>
		<xsl:choose>
		  <xsl:when test="tipoCondicion = 'Tramite'">
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" colspan="2" align="left"><xsl:apply-templates select = "condSalidaT" /></td>
			</tr>
		  </xsl:when>
	    </xsl:choose>
		<xsl:choose>
		  <xsl:when test="tipoCondFav = 'FinalizacionSI'">
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" colspan="2" align="left" class="etiqueta" >Favorable(Fin Expedente)</td>
			</tr>
		  </xsl:when>
		</xsl:choose>
		<xsl:choose>
		  <xsl:when test="tipoCondFav = 'TramiteSI'">
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" colspan="2" align="left" class="etiqueta" >Favorable(Lista dos Trámites)<xsl:value-of select="obligatorioF"/></td>
			</tr>
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="160mm" align="left"><xsl:apply-templates select = "condSalidaF" /></td>
			</tr>
		  </xsl:when>
		</xsl:choose>
		<xsl:choose>
		  <xsl:when test="tipoCondDFav = 'FinalizacionNO'">
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" colspan="2" align="left" class="etiqueta" >Desfavorable(Fin do Expedente)</td>
			</tr>
		  </xsl:when>
		</xsl:choose>
		<xsl:choose>
		  <xsl:when test="tipoCondDFav = 'TramiteNO'">
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="180mm" colspan="2" align="left" class="etiqueta" >Desfavorable(Lista dos Trámites)<xsl:value-of select="obligatorioDF"/></td>
			</tr>
			<tr>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="20mm" class="etiqueta" align="left"></td>
				<td width="160mm" align="left"><xsl:apply-templates select = "condSalidaDF" /></td>
			</tr>
		  </xsl:when>
		</xsl:choose>
		    <tr>
				<td width="20mm" height="1mm"></td>
				<td width="20mm" height="1mm"></td>
				<td width="160mm" height="1mm"></td>
			</tr>
		</table>
		<table border="0" align="center" width="200mm"  cellpadding="0mm">
			<tr><td width="100%" height="4mm"></td></tr>
		</table>
    
	</xsl:template>
	
        <xsl:template match = "unidadInicioTramite" > 		
		<table align="center" width="160mm" height="3mm" cellpadding="1px" border="0px">
		<tr>
			<td width="160mm">- <xsl:value-of select="nombreUnidadTramite"/></td>
		</tr>
		<tr><td width="160mm" height="1mm"></td></tr>
		</table>
    
	</xsl:template>
        
	<xsl:template match = "enlacesT" > 		
		<table align="center" width="110mm" height="3mm" cellpadding="1px" border="0px">
			<tr>
				<td width="110mm" >- <xsl:value-of select="nombreEnlaceT"/></td>
			</tr>
		</table>
    
	</xsl:template>
	
	<xsl:template match = "documentoT" > 		
		<table align="center" width="110mm" height="3mm" cellpadding="1px" border="0px">
			<tr>
				<td width="110mm" >- <xsl:value-of select="nombreDocT"/></td>
			</tr>
		</table>
    
	</xsl:template>
	
	<xsl:template match = "campoT" > 		
		<table align="center" width="150mm" height="3mm" cellpadding="1px" border="0px">
			<tr>
				<td width="150mm" >- <xsl:value-of select="nombreCampoT"/></td>
			</tr>
		</table>
    
	</xsl:template>
	
    <xsl:template match = "condEntrada" > 		
      <table align="center" width="110mm" height="3mm" cellpadding="1px" border="0px">
        <tr>
         <xsl:choose>
           <xsl:when test="tipoCondicion = 'EXPRESION'">
             <td width="110mm" >- <xsl:value-of select="condicion"/></td>
           </xsl:when>
           <xsl:otherwise>
             <td width="110mm" >- <xsl:value-of select="descTramite"/> ten que estar <xsl:value-of select="estTramite"/></td>
           </xsl:otherwise>
         </xsl:choose>
        </tr>
      </table>
    
	</xsl:template>
	
	<xsl:template match = "condSalidaT" > 		
		<table align="center" width="110mm" height="3mm" cellpadding="1px" border="0px">
			<tr>
				<td width="110mm" >- <xsl:value-of select="descTramiteT"/></td>
			</tr>
		</table>
    
	</xsl:template>
	
	<xsl:template match = "condSalidaF" > 		
		<table align="center" width="110mm" height="3mm" cellpadding="1px" border="0px">
			<tr>
				<td width="110mm" >- <xsl:value-of select="descTramiteF"/></td>
			</tr>
		</table>
    
	</xsl:template>
	
	<xsl:template match = "condSalidaDF" > 		
		<table align="center" width="110mm" height="3mm" cellpadding="1px" border="0px">
			<tr>
				<td width="110mm" >- <xsl:value-of select="descTramiteDF"/></td>
			</tr>
		</table>
    
	</xsl:template>
	
	
    
	
</xsl:stylesheet>

