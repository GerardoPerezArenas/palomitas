<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--
	Desde aquí se lanza la ejecución del certificado para la primera familia. 
	El resto de las familias se imprimen utilizando "recursión".
	Al menos habrá una familia.
-->

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
			<xsl:if test="count(familia) &gt; ($nf)">
				<pbr size="A4"/>
				<xsl:call-template name="df">
					<xsl:with-param name="nf"><xsl:value-of select="$nf + 1"/></xsl:with-param> 
				</xsl:call-template>
			</xsl:if>
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

				<table border="0" align="center" width="175mm" height="25mm">
					<tr>
						<td class="cabecera1I" width="175mm" align="center"><b>Ayuntamiento de <xsl:value-of select="concello"/></b></td>
					</tr>
					<tr><td class="cabecera1D" width="175mm" align="center" height="3mm"></td></tr>
					<tr>
						<td class="cabecera1D" width="175mm" align="center"><b><xsl:value-of select="titulo"/></b></td>
					</tr>
				</table>

				<table border="0" align="center" width="175mm">					
					<tr><td height="4mm"></td></tr>
					<tr>
						</tr>
					<tr><td height="10mm"></td></tr>
					<tr>
						<td>
							<table width="117mm">
							<tr>
								<td width="15mm" > <b>Distrito: </b> </td>
								<td width="10mm" > <xsl:value-of select="datosPadron/distrito"/></td>
								<td width="17mm" > <b>Sección: </b></td>
								<td width="10mm" > <xsl:value-of select="datosPadron/seccion"/></td>								
								<td width="15mm" > <b>Hoja: </b></td>
								<td width="50mm" ><xsl:value-of select="datosPadron/hojaPadronal"/></td>								
							</tr>
							<tr>
								<td colspan="3" > <b>Entidad de Población: </b> </td>
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
								<td width="45mm" ><xsl:value-of select="datosVivienda/nombreVia"/></td>
								<td width="20mm" > <b>Número: </b> </td>
								<td width="60mm" ><xsl:value-of select="datosVivienda/numero1"/>
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
					<tr><td height="10mm"></td></tr>
			</table>
		
		</xsl:template> 
		
		<xsl:template name="pagina_2">
			<xsl:param name="nf"/>
							<xsl:choose>
								<xsl:when test="tipoCertificado = 'individual'">
									<xsl:call-template name="individual">
											<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param> 			
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="colectivo">
										<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param> 			
										<xsl:with-param name="nh"><xsl:value-of select="count(familia[position() = $nf]/datosHabitante/habitante)"/></xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
		</xsl:template> 
		
		<xsl:template name="pagina_3">

				<table border="0" align="center" width="175mm" height="12mm">								
					<xsl:if test="observaciones!='' and observaciones!='null' ">
					<tr><td height="2mm"></td></tr>					
					<tr>
						<td><b>Observaciones:</b></td>
					</tr>
					<tr>
						<td height="12mm" width="175mm" border="0">
							<xsl:value-of select="observaciones"/>
						</td>
					</tr>
					</xsl:if>
					<xsl:if test="observaciones='' or observaciones='null' ">					
				   		<tr><td height="10mm"></td></tr>					
					</xsl:if>	
					<tr><td height="10mm"></td></tr>					
				    <tr>
						<td>
						 <xsl:value-of select="ayto"/>, a <xsl:value-of select="fechaDocumento"/>.
						</td>
					</tr>
					<tr><td height="10mm"></td></tr>
					</table>
		
			</xsl:template>


			
	<xsl:template name="individual">			      
		<xsl:param name="nf"/>
		
		<table border="0" align="center" width="175mm" height="12mm">
			<tr>
				<td width="60mm" > <b>Nº Orden: </b> </td>
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
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/lugarNacimiento"/>, <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/provinciaNacimiento"/></td>
			</tr>
			<tr>
				<td width="60mm" > <b>Nacionalidad: </b> </td>
				<td width="115mm" > <xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position()=1]/paisNacionalidad"/></td>
			</tr>			
			<tr>
				<td colspan="2" width="175mm" height="8mm"></td>
			</tr>			
			
			
		</table>
	</xsl:template>

	<xsl:template name="colectivo">		
		<xsl:param name="nf"/>
		<xsl:param name="nh"/>
		<table border="0" align="center" width="175mm" height="10mm">
				<thead>
				<tr>
					<td width="15mm" height="4mm"></td>
					<td width="65mm" height="4mm"></td>
					<td width="25mm" height="4mm"></td>
					<td width="35mm" height="4mm"></td>
					<td width="40mm" height="4mm"></td>
				      </tr>				
                                </thead>
                                <tbody>
				<tr>
					<td width="15mm"  align="center"  height="3mm">Nº</td>
					<td width="65mm"  height="3mm">Nombre y Apellidos</td>
					<td width="25mm"  align="center"  height="3mm">Sexo</td>
					<td width="35mm"  align="center"  height="3mm">Documento</td>
					<td width="40mm"  align="center"  height="3mm">Fecha Nacimiento</td>
				</tr>
				<tr>
					<td width="15mm"  align="left"  height="3mm">Orden</td>
					<td width="65mm"  height="3mm">Lugar Nacimiento</td>
					<td width="25mm"  align="center"  height="3mm"></td>
					<td width="35mm"  align="center"  height="3mm"></td>
					<td width="40mm"  align="center"  height="3mm">Nacionalidad</td>
				</tr>				
				<tr>
					<td width="15mm" style="border-top-width:1px" height="4mm"></td>
					<td width="65mm" style="border-top-width:1px" height="4mm"></td>
					<td width="25mm" style="border-top-width:1px" height="4mm"></td>
					<td width="35mm" style="border-top-width:1px" height="4mm"></td>
					<td width="40mm" style="border-top-width:1px" height="4mm"></td>
				</tr>				
			  <xsl:for-each select="familia[position() = $nf]/datosHabitante/habitante">
			  	<tr><td colspan="5" width="175mm"><table width="175mm" border="0" cellborder="0" cellpadding="0" cellmargin="1">
				<tr>
							<td width="15mm"  height="3mm">
								<xsl:value-of select="numOrden"/>
							</td>
							<td width="65mm"  height="3mm">
								<xsl:value-of select="nombre"/>
							</td>
							<td width="25mm"  align="center" height="3mm">
								<xsl:value-of select="sexo"/>
							</td>							
							<td width="35mm" align="center" height="3mm">
											<xsl:choose>
											<xsl:when test="tipoDocumento = '0'">
											<!--SIN DOCUMENTO-->
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="descDocumento"/>: <xsl:value-of select="numeroDocumento"/><xsl:value-of select="letraDocumento"/>
											</xsl:otherwise>
											</xsl:choose>
							</td>
							<td width="40mm" align="center" height="3mm">
								<xsl:value-of select="fechaNacimiento"/>
							</td>
            	</tr>
				<tr>
							<td width="15mm" height="3mm"></td>
							<td width="65mm" height="3mm">
								<xsl:value-of select="lugarNacimiento"/>, <xsl:value-of select="provinciaNacimiento"/>
							</td>
							<td width="25mm" align="center" height="3mm"></td>							
							<td width="35mm" align="center" height="3mm"></td>
							<td width="40mm" align="center" height="3mm">
								<xsl:value-of select="paisNacionalidad"/>
							</td>
            	</tr>		
            	</table></td></tr>
			  </xsl:for-each>
			  <tr>
					<td width="15mm" style="border-bottom-width:1px" height="6mm"></td>
					<td width="65mm" style="border-bottom-width:1px" height="6mm"></td>
					<td width="25mm" style="border-bottom-width:1px" height="6mm"></td>
					<td width="35mm" style="border-bottom-width:1px" height="6mm"></td>
					<td width="40mm" style="border-bottom-width:1px" height="6mm"></td>
				</tr>
		  		<tr>	
                                        <td width="15mm"  height="6mm"></td>
					<td width="65mm" height="6mm"></td>
					<td width="25mm" height="6mm"></td>
					<td width="35mm" height="6mm"></td>
					<td width="40mm" height="6mm"></td>
				</tr>
		  		<tr>
					<td colspan="5" height="6mm"><b>Número de personas que comprenden esta solicitud: </b>
					<xsl:value-of select="$nh"/></td>
				</tr>									
			</tbody>					
		</table>	      
	</xsl:template>
</xsl:stylesheet>

