<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="//certificado" > 
			<xsl:call-template name="df">
			<xsl:with-param name="nf">1</xsl:with-param> 
		</xsl:call-template>
  	</xsl:template> 
	

	<xsl:template name="df">
			<xsl:param name="nf"/>				
			<xsl:call-template name="pagina">
				<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param> 			
			</xsl:call-template>			
			<!-- familia siguiente -->		
	</xsl:template>

	<xsl:template name="pagina">
		<xsl:param name="nf"/>		
			<xsl:call-template name="pagina_1"/>
			<xsl:call-template name="pagina_2">
				<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param> 			
			</xsl:call-template>
			<xsl:call-template name="pagina_3"/>
	</xsl:template>



			<xsl:template name="pagina_1">

				<table border="0" align="center" width="175mm" height="15mm">
					<tr>
						<td class="cabecera1I" width="175mm" align="center"><b>Ayuntamiento de <xsl:value-of select="concello"/></b></td>
					</tr>
					<tr><td class="cabecera1D" width="175mm" align="center" height="3mm"></td></tr>
					<tr>
						<td class="cabecera1D" width="175mm" align="center"><b><xsl:value-of select="titulo"/></b></td>
					</tr>
				</table>

				<table border="0" align="center" width="175mm">
					<tr><td height="3mm"></td></tr>
					<tr>
						<td><xsl:value-of select="tratamientoCertificador"/>   <xsl:value-of select="nombreCertificador"/>, <xsl:value-of select="cargoCertificador"/> DE ESTE AYUNTAMIENTO,</td>
					</tr>
					<tr><td height="3mm"></td></tr>
					<tr>
						<td class="textoDiligencia"><b>CERTIFICO:</b> Que en el Padr�n Municipal de este municipio figuran, en el d�a de la
								fecha y en la hoja que se indica, la/s siguiente/s inscripcion/es:</td>
					</tr>
					<tr><td height="3mm"></td></tr>
					<tr>
						<td>
							<table width="117mm">
							<tr>
								<td width="15mm" > <b>Distrito: </b> </td>
								<td width="10mm" > <xsl:value-of select="datosPadron/distrito"/></td>
								<td width="17mm" > <b>Secci�n: </b></td>
								<td width="10mm" > <xsl:value-of select="datosPadron/seccion"/></td>								
								<td width="15mm" > <b>Hoja: </b></td>
								<td width="50mm" ><xsl:value-of select="datosPadron/hojaPadronal"/></td>								
							</tr>
							<tr>
								<td colspan="3" > <b>Entidad de Poblaci�n: </b> </td>
								<td colspan="3" ><xsl:value-of select="datosPadron/entidadColectiva"/></td>
							</tr>
							<tr>
								<td colspan="3" > </td>
								<td colspan="3" ><xsl:value-of select="datosPadron/entidadSingular"/></td>
							</tr>
							<tr>
								<td colspan="3" ></td>
								<td colspan="3" ><xsl:value-of select="datosPadron/nucleo"/></td>
							</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table width="150mm" border="0">						
							<tr>
								<td width="15mm" > <b>Vial: </b> </td>
								<td width="10mm" ><xsl:value-of select="datosVivienda/tipoVia"/></td>
								<td width="65mm" ><xsl:value-of select="datosVivienda/nombreVia"/></td>
								<td width="20mm" > <b>N�mero: </b> </td>
								<td width="40mm" ><xsl:value-of select="datosVivienda/numero1"/>
													<xsl:if test="not(datosVivienda/letra1='')">
														&nbsp;<xsl:value-of select="datosVivienda/letra1"/>
													</xsl:if>
													<xsl:if test="not(datosVivienda/numero2='')">
														- <xsl:value-of select="datosVivienda/numero2"/>
													</xsl:if>
													<xsl:if test="not(datosVivienda/letra2='')">
														&nbsp;<xsl:value-of select="datosVivienda/letra2"/>
													</xsl:if>							
								</td>
							</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table width="150mm" border="0">						
							<tr>
								<td width="10mm" > <b>Km: </b> </td>
								<td width="10mm" ><xsl:value-of select="datosVivienda/km"/></td>
								<td width="15mm" > <b>Bloque: </b> </td>
								<td width="15mm" >  <xsl:value-of select="datosVivienda/bloque"/></td>
								<td width="15mm" > <b>Portal: </b> </td>
								<td width="20mm" >  <xsl:value-of select="datosVivienda/portal"/></td>
								<td width="15mm" > <b>Escalera: </b> </td>
								<td width="25mm" >  <xsl:value-of select="datosVivienda/escalera"/></td>
								<td width="15mm" > <b>Planta: </b> </td>
								<td width="10mm" >  <xsl:value-of select="datosVivienda/piso"/></td>								
							</tr>
							</table>
						</td>							
					</tr>
					<tr>
						<td>
							<table width="150mm" border="0">						
								<tr>
								<td width="15mm" > <b>Puerta: </b> </td>
								<td width="10mm" ><xsl:value-of select="datosVivienda/puerta"/></td>
								<td width="15mm" > <b>C.P.: </b> </td>
								<td width="110mm">  <xsl:value-of select="datosVivienda/codigoPostal"/></td>
							</tr>
							</table>
						</td>
					</tr>					
					<tr><td height="6mm"></td></tr>
			</table>
		
		</xsl:template> 
		
		<xsl:template name="pagina_2" >
			<xsl:param name="nf"/>
				<xsl:call-template name="individual">
				<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param> 			
				</xsl:call-template>
		</xsl:template> 
		
		<xsl:template name="pagina_3">

				<table border="0" align="center" width="175mm" height="12mm">				
				
					<tr><td height="2mm"></td></tr>				
					
					<tr><td> <xsl:call-template name="observaciones"/> </td></tr>				
					
					<tr><td height="2mm"></td></tr>				
					
					<xsl:if test="observaciones!='' and observaciones!='null' ">					
					<tr>
						<td><b>Observaciones:</b></td>
					</tr>
					<tr>
						<td height="5mm" width="175mm" border="0">
							<xsl:value-of select="observaciones"/>
						</td>
					</tr>
					</xsl:if>
				    <tr>
						<td class="textoDiligencia">Y para que conste, y a los efectos oportunos,
								expido la presente certificaci�n en <xsl:value-of select="ayto"/>, a <xsl:value-of select="fechaDocumento"/>.
						</td>
					</tr>
					<tr><td height="5mm"></td></tr>
					<tr>
						<td>
							<table width="110mm">
								<tr>
									<td colspan="2">V� B�</td>
								</tr>
								<tr>
									<td height="15mm" width="110mm">
									<xsl:choose>
											<xsl:when test="tratamientoAlcalde='D�' or  tratamientoAlcalde='D�.' 
											or tratamientoAlcalde='DNA' or  tratamientoAlcalde='DNA.' 
											or tratamientoAlcalde='D�A' or tratamientoAlcalde='D�A.'
											or tratamientoAlcalde='DONA' or  tratamientoAlcalde='DONA.' 
											or tratamientoAlcalde='DO�A' or tratamientoAlcalde='DO�A.' ">	
											LA </xsl:when> 
                    						<xsl:otherwise >EL </xsl:otherwise> 
						               </xsl:choose> 									
									<xsl:value-of select="cargoAlcalde"/></td>
								</tr>
								<tr>
									<td width="110mm"> Fdo.: <xsl:value-of select="nombreAlcalde"/></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
		
			</xsl:template>


			
	<xsl:template name="individual">			      
		<xsl:param name="nf"/>
		
		<table border="0" align="center" width="175mm" height="12mm">
			<tr>
				<td width="60mm" > <b>N� Orden: </b> </td>
				<td width="115mm" ><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/numOrden"/></td>
			</tr>
			<tr>
				<td width="60mm" > <b>Nombre: </b> </td>
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/nombre"/></td>
			</tr>
			<tr>
				<td width="60mm" > <b>Sexo: </b> </td>
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/sexo"/></td>
			</tr>
			<tr>
				<td width="60mm" > <b>Documento: </b> </td>
				<td width="115mm" > 
						<xsl:choose>
							<xsl:when test="familia[position() = $nf]/datosHabitante/habitante[position()=1]/tipoDocumento = '0'">
								<!--SIN DOCUMENTO-->
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/descDocumento"/>: <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/numeroDocumento"/><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/letraDocumento"/>
							</xsl:otherwise>
						</xsl:choose>
				</td>
			</tr>
			<tr>
				<td width="60mm" > <b>Fecha Nacimiento: </b> </td>
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/fechaNacimiento"/></td>
			</tr>
			<tr>
				<td width="60mm" > <b>Lugar Nacimiento: </b> </td>				
				<td width="115mm" > 
					<xsl:choose>
							<xsl:when test="familia[position() = $nf]/datosHabitante/habitante[position()=1]/provinciaNacimiento = 'EXTRANJERO'">
								<xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/lugarNacimiento"/>
							</xsl:when>
							<xsl:otherwise>				
								<xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/lugarNacimiento"/>, <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/provinciaNacimiento"/>
							</xsl:otherwise>
					</xsl:choose>							
				</td>
			</tr>
			<tr>
				<td width="60mm" > <b>Nacionalidad: </b> </td>
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/paisNacionalidad"/></td>
			</tr>			
			<tr>
				<td width="60mm" > <b>Fecha Empadr.: </b> </td>
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/fechaLlegada"/></td>
			</tr>
		</table>
	</xsl:template>
	
	<xsl:template name="observaciones">		
		
		<table width="175mm" border="0" cellpadding="0">						
		
			<tr><td height="2mm">OBSERVACIONES</td></tr>				
			<tr>
				<td width="175mm" border="0">
					<table width="175mm" border="1" cellborder="0px" cellpadding="2px" cellmargin="2px">						
						<tr>
							<td colspan="2" height="12mm" class="textoNota9 ">La persona inscrita aporta para acreditar su estancia en Espa�a con anterioridad al 8/8/2004, el/los documento/s marcado/s a continuaci�n:</td>
						</tr>
						<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" class="textoNota9 "> Copia de la solicitud de empadronamiento no resuelta o denegada debidamente registrada en el municipio.</td>
						</tr>								
						<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Tarjeta de asistencia sanitaria de un servicio p�blico de salud en la que conste la fecha del alta, o en su caso, certificaci�n en la que conste la fecha de antig�edad del alta.</td>
						</tr>						
						<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Copia de la solicitud de escolarizaci�n de menores, debidamente registrada.</td>
						</tr>															
						<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Copia de la solicitud debidamente registrada, certificaci�n del informe de los Servicios Sociales o notificaci�n de la resoluci�n de percepci�n de ayudas sociales.</td>
						</tr>								
						<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Documento de alta laboral o certificaci�n de la misma expedida por la Seguridad Social.</td>
						</tr>
						<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Copia de la solicitud de asilo debidamente registrada. </td>
						</tr>
						<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Notificaci�n de Resoluciones derivadas de la Normativa de Extranjer�a emitidas por el Ministerio del Interior.</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr><td height="2mm"></td></tr>				
			<tr><td height="2mm">EFECTO PARA EL QUE SE EXPIDE</td></tr>				
			<tr>
				<td width="175mm" border="0">
					<table width="175mm" border="1" cellborder="0px" cellpadding="2px" cellmargin="2px">						
						<tr>
							<td colspan="2" height="12mm" class="textoNota9 ">A efectos del proceso de normalizaci�n establecido por la Disposici�n Transitoria Tercera del Real Decreto 2392/2004.</td>
						</tr>
					</table>
				</td>
			</tr>			
		</table>
	</xsl:template>
	
</xsl:stylesheet>




