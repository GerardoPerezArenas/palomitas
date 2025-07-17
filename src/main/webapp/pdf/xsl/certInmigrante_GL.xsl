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
						<td class="cabecera1I" width="175mm" align="center"><b>Concello de <xsl:value-of select="concello"/></b></td>
					</tr>
					<tr><td class="cabecera1D" width="175mm" align="center" height="3mm"></td></tr>
					<tr>
						<td class="cabecera1D" width="175mm" align="center"><b><xsl:value-of select="titulo"/></b></td>
					</tr>
				</table>

				<table border="0" align="center" width="175mm">
					<tr><td height="3mm"></td></tr>
					<tr>
						<td><xsl:value-of select="tratamientoCertificador"/>   <xsl:value-of select="nombreCertificador"/>, <xsl:value-of select="cargoCertificador"/> DESTE CONCELLO,</td>
					</tr>
					<tr><td height="3mm"></td></tr>
					<tr>
						<td class="textoDiligencia"><b>CERTIFICO:</b> Que no Padrón Municipal deste concello figuran, no día da
								data e na folla que se indica, a/s seguinte/s inscripcion/s:</td>
					</tr>
					<tr><td height="3mm"></td></tr>
					<tr>
						<td>
							<table width="117mm">
							<tr>
								<td width="15mm" > <b>Distrito: </b> </td>
								<td width="10mm" > <xsl:value-of select="datosPadron/distrito"/></td>
								<td width="17mm" > <b>Sección: </b></td>
								<td width="10mm" > <xsl:value-of select="datosPadron/seccion"/></td>								
								<td width="15mm" > <b>Folla: </b></td>
								<td width="50mm" ><xsl:value-of select="datosPadron/hojaPadronal"/></td>								
							</tr>
							<tr>
								<td colspan="3" > <b>Entidade de Poboación: </b> </td>
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
								<td width="20mm" > <b>Número: </b> </td>
								<td width="40mm" ><xsl:value-of select="datosVivienda/numero1"/>
													<xsl:if test="not(datosVivienda/letra1='')">
														&nbsp;<xsl:value-of select="datosVivienda/letra1"/>
													</xsl:if>
													<xsl:if test="not(datosVivienda/numero2='')">
														-<xsl:value-of select="datosVivienda/numero2"/>
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
								<td width="15mm" > <b>Escaleira: </b> </td>
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
								<td width="15mm" > <b>Porta: </b> </td>
								<td width="10mm" ><xsl:value-of select="datosVivienda/puerta"/></td>
								<td width="15mm" > <b>C.P.: </b> </td>
								<td width="110mm"> <xsl:value-of select="datosVivienda/codigoPostal"/> </td>
							</tr>
							</table>
						</td>
					</tr>					
					<tr><td height="6mm"></td></tr>
			</table>
		
		</xsl:template> 
		
		<xsl:template name="pagina_2">
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
						<td><b>Observacións:</b></td>
					</tr>
					<tr>
						<td height="5mm" width="175mm" border="0">
							<xsl:value-of select="observaciones"/>
						</td>
					</tr>
					</xsl:if>
				    <tr>
						<td class="textoDiligencia">E para que conste, e ós efectos oportunos,
								expido a presente certificación en <xsl:value-of select="ayto"/>, <xsl:value-of select="fechaDocumento"/>.
						</td>
					</tr>
					<tr><td height="5mm"></td></tr>
					<tr>
						<td>
							<table width="110mm">
								<tr>
									<td colspan="2">Vº e pr.</td>
								</tr>
								<tr>
									<td height="15mm" width="110mm">
										<xsl:choose>
											<xsl:when test="tratamientoAlcalde='Dª' or  tratamientoAlcalde='Dª.' 
											or tratamientoAlcalde='DNA' or  tratamientoAlcalde='DNA.' 
											or tratamientoAlcalde='DÑA' or tratamientoAlcalde='DÑA.'
											or tratamientoAlcalde='DONA' or  tratamientoAlcalde='DONA.' 
											or tratamientoAlcalde='DOÑA' or tratamientoAlcalde='DOÑA.' ">	
											A </xsl:when> 
                    						<xsl:otherwise >O </xsl:otherwise> 
						               </xsl:choose> 									
									 <xsl:value-of select="cargoAlcalde"/></td>
								</tr>
								<tr>
									<td width="110mm">Asdo.:  <xsl:value-of select="nombreAlcalde"/></td>
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
				<td width="60mm" > <b>Nº Orde: </b> </td>
				<td width="115mm" ><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/numOrden"/></td>
			</tr>
			<tr>
				<td width="60mm" > <b>Nome: </b> </td>
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
								<!--SEN DOCUMENTO-->
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/descDocumento"/>: <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/numeroDocumento"/><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/letraDocumento"/>
							</xsl:otherwise>
						</xsl:choose>
				</td>
			</tr>
			<tr>
				<td width="60mm" > <b>Data Nacemento: </b> </td>
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/fechaNacimiento"/></td>
			</tr>
			<tr>
				<td width="60mm" > <b>Lugar Nacemento: </b> </td>
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
				<td width="60mm" > <b>Nacionalidade: </b> </td>
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/paisNacionalidad"/></td>
			</tr>
			<tr>
				<td width="60mm" > <b>Data Empadr.: </b> </td>
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/fechaLlegada"/></td>
			</tr>	
		</table>
	</xsl:template>

	<xsl:template name="observaciones">		
		
		<table width="175mm" border="0" cellpadding="0">						
		
			<tr><td height="2mm">OBSERVACIÓNS</td></tr>				
			<tr>
				<td width="175mm" border="0">
					<table width="175mm" border="1" cellborder="0px" cellpadding="2px" cellmargin="2px">						
				<tr>
							<td colspan="2" height="12mm" class="textoNota9 ">A persoa inscrita aporta para acreditar a sua estancia en España con anterioridade ó 8/8/2004, o/s documento/s marcado/s a continuación:</td>
				</tr>				
				<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" class="textoNota9 "> Copia da solicitude de empadronamento non resolta ou denegada debidamente rexistrada no municipio.</td>
				</tr>
				<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Tarxeta de asistencia sanitaria dun servicio público de saude na que conste a data de alta, ou no seu caso, certificación na que conste a data de antigüidade do alta.</td>
				</tr>				
				<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Copia da solicitude de escolarización de menores, debidamente rexistrada.</td>
				</tr>						
				<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Copia da solicitude debidamente rexistrada, certificación do informe dos Servicios Sociais ou notificación da resolución de percepción de axudas sociais.</td>
						</tr>								
						<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Documento de alta laboral ou certificación da mesma expedida pola Seguridade Social.</td>
						</tr>
						<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>							
							<td width="170mm" valign="top" class="textoNota9 "> Copia da solicitude de asilo debidamente rexistrada. </td>
        </tr>
				<tr>
							<td>
								<table width="2mm" height="2mm" border="1px" cellborder-right="1px" cellborder-bottom="2px" cellpadding="0" cellmargin="0">						
								<tr><td height="2mm" width="2mm"></td></tr>
								</table>									
							</td>
							<td width="170mm" valign="top" class="textoNota9 "> Notificación de Resolucións derivadas da Normativa de Estranxería emitidas polo Ministerio do Interior.</td>
						</tr>
					</table>
							</td>
        </tr>				
			<tr><td height="2mm"></td></tr>				
			<tr><td height="2mm">EFECTO PARA O QUE SE EXPIDE</td></tr>				
			  <tr>
				<td width="175mm" border="0">
					<table width="175mm" border="1" cellborder="0px" cellpadding="2px" cellmargin="2px">						
		  	<tr>
							<td colspan="2" height="12mm" class="textoNota9 ">A efectos do proceso de normalización establecido pola Disposición Transitoria Terceira do Real Decreto 2392/2004.</td>
				</tr>
					</table>
				</td>
				</tr>									
		</table>	      
	</xsl:template>
	
</xsl:stylesheet>

