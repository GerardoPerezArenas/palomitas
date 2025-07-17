<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<!-- Página por habitante, sólo se considera el primero.	-->

	<xsl:template match="//inscripcion" >
		<pbr size="A4-landscape"/>
		<xsl:call-template name="pagina"/>
	</xsl:template>


		<xsl:template name="pagina">
			<xsl:param name="nh">1</xsl:param>
				<table><tr><td height="3mm"></td></tr></table>
				<table border="0" align="center" width="270mm" cellpadding="0mm">
					<tr>
						<td align="left">
								<table border="0" align="center" width="135mm" height="9mm" cellpadding="0">
								<tr>
									<td width="15mm" class="cabecera" align="left">
										<b> I N E </b>
									</td>
									<td width="120mm" class="cabecera" align="left">
										<b>DOCUMENTO DE <xsl:value-of select="titulo"/><br/>EN EL PADRÓN MUNICIPAL DE HABITANTES</b>
									</td>
								</tr>
								</table>
						</td>
					</tr>
					<tr>
						<td>
								<table border="0" align="center" width="270mm"  cellpadding="0">
								<tr>
									<td width="130mm" class="textoDiligencia">
										<xsl:choose>
											<xsl:when test="titulo='BAJA POR CAMBIO DE RESIDENCIA'">
                          Declaración triplicada que, a efectos de lo dispuesto en el articulo 15 de la Ley Reguladora de Bases de Regimen Local 7/1995 del 2 de abril y de acordo con lo establecido en el art. 56 del Reglamento de Población y Demarcación Territorial de las Entidades Locales, el/la/los vecinos firmantes en número de 1 solicitan la baja en el Padrón Municipal de Habitantes en el ayuntamiento de
						  <xsl:value-of select="datosPadron/descMunicipio"/>, provincia de <xsl:value-of select="datosPadron/descProvincia"/>.
											</xsl:when>
										<xsl:otherwise></xsl:otherwise>
										</xsl:choose>
									</td>
									<td width="140mm" align="right">
											<xsl:call-template name="datosAytoBaja"/>
									</td>
								</tr>
								</table>
						</td>
					</tr>
					<tr>
						<td height="4mm">
							Relación de personas que se dan de baja
						</td>
					</tr>
					<tr>
						<td height="11mm">
								<table border="1" align="center" width="270mm">
								<tr>
									<td width="72mm" class="textoFijo_r">Nombre y apellidos</td>
									<td width="10mm" class="textoFijo_r">Sexo</td>
									<td width="95mm" class="textoFijo_r">Lugar, fecha de nacimiento y nacionalidad</td>
									<td width="50mm" class="textoFijo_r">Título escolar</td>
									<td width="43mm" class="textoFijo_r">Documento</td>
								</tr>
								</table>
						</td>
					</tr>
					<tr>
						<td height="35mm">
							<xsl:call-template name="dh">
								<xsl:with-param name="nh">1</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				<tr>
					<td height="60mm">
						<xsl:choose>
						<xsl:when test="titulo='BAJA POR CAMBIO DE RESIDENCIA' and datosHabitante/habitante[position() = ($nh)]/codProvinciaDestino!= '' and datosHabitante/habitante[position() = ($nh)]/codMunicipioDestino!= ''">
							<xsl:call-template name="certificacionAlta">
									<xsl:with-param name="nh">1</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise></xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</table>
		</xsl:template>


			<xsl:template name="dh">
				<xsl:param name="nh" />

					<table width="270mm" border="1">
						<tr>
							<td width="72mm" class="textoFijo_r" >
								<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/nombre"/>
							</td>
							<td width="10mm" class="textoFijo_r" align="center">
								<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/sexo"/>
							</td>
							<td width="95mm" class="textoFijo_r1" ><!-- Lugar, fecha de nacimiento y nacionalidad -->
								<table width="91mm" border="0" cellpadding="2">
									<tr>
										<td width="72mm"> <!-- Lugar-->
											<table width="72mm" border="0" cellpadding="0mm">
												<tr>
													<td width="7mm" class="textoFijo1" ><!-- Codigo Provincia Nacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codProvinciaNacimiento"/>
													</td>
													<td width="65mm" class="textoFijo1"> <!-- Provincia Nacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/provinciaNacimiento"/>
													</td>
												</tr>
												<tr>
													<td width="7mm" class="textoFijo1" ><!-- Codigo MunicipioNacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codLugarNacimiento"/>												</td>
													<td width="65mm" class="textoFijo1"> <!-- Municipio Nacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/lugarNacimiento"/>
													</td>
												</tr>
											</table>
										</td>
										<td width="19mm" valign="middle" class="textoFijo1">
											<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/fechaNacimiento"/>
										</td>
									</tr>
									<tr>
										<td colspan="2"> <!-- Nacionalidad -->
											<table width="91mm" border="0" cellpadding="0mm">
												<tr>
													<td width="7mm" class="textoFijo1" > <!-- Cod. Pais Nacionalidad-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codPaisNacionalidad"/>
													</td>
													<td width="84mm" class="textoFijo1"> <!-- Desc. Pais Nacionalidad -->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/paisNacionalidad"/>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
							<td width="50mm"  class="textoFijo_r1" > <!-- Titulo escolar -->
									<table width="46mm" border="0" cellpadding="2">
										<tr>
											<td width="7mm" class="textoFijo1" > <!-- Cod. Titulacion -->
												<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codTitulacion"/>
											</td>
											<td width="39mm" class="textoFijo1"> <!-- Desc. Titulacion -->
												<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/titulacion"/>
											</td>
										</tr>
									</table>
							</td>
							<td width="43mm" class="textoFijo_r1"> <!-- Documento -->
									<table width="41mm" border="0" cellpadding="2" >
								<tr>
									<td>
										<table width="39mm" border="0" cellpadding="0">
										<tr>
											<td width="6mm" class="textoFijo2" valign="middle" height="3mm">D.N.I.</td>
											<td width="4mm" class="textoFijo1" valign="left">
												<xsl:if test="datosHabitante/habitante[position() = ($nh)]/tipoDocumento = '1'">
													X
												</xsl:if>
											</td>
											<td width="10mm" class="textoFijo2" valign="middle">Pasaporte </td>
											<td width="4mm" class="textoFijo1" valign="left">
												<xsl:if test="datosHabitante/habitante[position() = ($nh)]/tipoDocumento = '2'">
													X
												</xsl:if>
											</td>
											<td width="11mm" class="textoFijo2" valign="middle">Tarj. Resid.</td>
											<td width="4mm" class="textoFijo1" valign="left">
												<xsl:if test="datosHabitante/habitante[position() = ($nh)]/tipoDocumento = '3'">
													X
												</xsl:if>
											</td>
										</tr>
										</table>
									</td>
								</tr>
							<tr>
									<td class="textoFijo1">
											<xsl:choose>
											<xsl:when test="datosHabitante/habitante[position() = ($nh)]/tipoDocumento = '0'">
											SIN DOCUMENTO
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="datosDocumento">
												<xsl:with-param name="nh">1</xsl:with-param>
												<xsl:with-param name="nhpg">1</xsl:with-param>
												<xsl:with-param name="pg">0</xsl:with-param>
												</xsl:call-template>
											</xsl:otherwise>
											</xsl:choose>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</xsl:template>


			<xsl:template name="datosAytoBaja">
				<table border="0" width="135mm" cellpadding="0">
				<tr>
					<td height="5mm">
						Datos del ayuntamiento de BAJA
					</td>
				</tr>
				<tr>
					<td class="textoFijo1" height="15mm">
						<table border="1" width="135mm" cellpadding="3">
						<tr >
							<td width="30mm" class="textoFijo1">
								Provincia o país:
							</td>
							<td width="95mm" class="textoFijo1">
								<xsl:value-of select="datosPadron/descProvincia"/>
							</td>
							<td width="10mm" class="textoFijo1">
								<xsl:value-of select="datosPadron/codProvincia"/>
							</td>
						</tr>
						<tr>
							<td width="30mm" class="textoFijo1">
								Ayuntamiento:
							</td>
							<td width="95mm" class="textoFijo1">
								<xsl:value-of select="datosPadron/descMunicipio"/>
							</td>
							<td width="10mm" class="textoFijo1">
								<xsl:value-of select="datosPadron/codMunicipio"/>
							</td>
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td  height="20mm" class="textoDiligencia">
							Diligencia: queda anotada y registrada la baja en el Padrón, como residentes en este ayuntamiento, de las personas a las que se refiere esta declaración y que figuran relacionadas en este documento.
					</td>
				</tr>
				<tr>
					<td class="textoFijo1" height="11mm">
							<xsl:value-of select="datosAyto/ayto"/>, a <xsl:value-of select="fechaDocFor"/>
					</td>
				</tr>
				<tr>
					<td class="textoFijo1" height="9mm">
							<table border="0" width="135mm" cellpadding="0">
							<tr>
								<td width="65mm">
									(Sello del ayuntamiento)
								</td>
								<td width="65mm" align="right">
										El/La Secretario/a
								</td>
							</tr>
							</table>
					</td>
				</tr>
				</table>
		</xsl:template>


		<xsl:template name="datosDocumento">
			<xsl:param name="nh" />

				<table width="39mm" border="0" cellpadding="0">
				<tr>
					<td width="17mm" class="textoFijo1" valign="middle">
						<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/numeroDocumento"/>
					</td>
					<td width="22mm" class="textoFijo1" valign="middle">
						<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/letraDocumento"/>
					</td>
				</tr>
				</table>

		</xsl:template>


		<xsl:template name="certificacionAlta">
			<xsl:param name="nh"/>
			<table border="0" align="center" width="270mm" cellpadding="0">

				<tr>
					<td width="135mm">
						<xsl:call-template name="dNuevoAytoAlta2"/>
					</td>
					<td width="135mm" align="right">
						<xsl:call-template name="dNuevoAytoAlta1">
							<xsl:with-param name="nh">1</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</xsl:template>


			<xsl:template name="dNuevoAytoAlta1">
				<xsl:param name="nh"/>
				<table border="0" align="center" width="135mm" cellpadding="0">
					<tr>
						<td width="75mm"></td>
						<td width="60mm"></td>
					</tr>
					<tr>
						<td colspan="2" height="5mm">
							Datos del ayuntamiento de ALTA
						</td>
					</tr>
					<tr>
						<td colspan="2" class="textoFijo1" height="15mm">
							<table border="1" width="135mm" cellpadding="3">
								<tr>
									<td width="40mm" class="textoFijo1">
										Provincia o país:
									</td>
									<td width="85mm" class="textoFijo1">
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/descProvinciaDestino"/>
									</td>
									<td width="10mm" class="textoFijo1">
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codProvinciaDestino"/>
									</td>
								</tr>
								<tr>
									<td width="40mm" class="textoFijo1">
										Ayuntamiento:
									</td>
									<td width="85mm" class="textoFijo1">
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/descMunicipioDestino"/>
									</td>
									<td width="10mm" class="textoFijo1">
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codMunicipioDestino"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>
			</xsl:template>

			<xsl:template name="dNuevoAytoAlta2">

			</xsl:template>

</xsl:stylesheet>
