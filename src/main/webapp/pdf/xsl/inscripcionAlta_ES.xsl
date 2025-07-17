<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<!-- 	Página por habitante (solo se considera el primero).	-->

	<xsl:template match="//inscripcion" >
		<pbr size="A4-landscape"/>
		<xsl:call-template name="pagina"/>
	</xsl:template>

		<xsl:template name="pagina">
			<xsl:param name="nh">1</xsl:param>
			    <table><tr><td height="3mm"></td></tr></table>
				<table border="0" align="center" width="270mm"  cellpadding="0mm">
					<tr>
						<td align="left">
							<table border="0" align="center" width="135mm" height="12mm" cellpadding="0">
							<tr>
								<td width="15mm" class="cabecera" align="left">
									<b> I N E </b>
								</td>
								<td width="120mm" class="cabecera" align="left">
									<b>DOCUMENTO DE <xsl:value-of select="titulo"/><br/>EN EL PADRÓN MUNICIPAL DE HABITANTES.</b>
								</td>
							</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
								<table border="0" align="center" width="270mm" height="45mm" cellpadding="0">
								<tr>
									<td width="130mm" class="textoDiligencia">
										En virtud de lo dispuesto en el articulo 70 del Reglamento de Población y Demarcación Territorial de las Entidades Locales, en su nueva redacción dada por el Real Decreto 2612/96, del 20 de diciembre, los vecinos firmantes solicitan el alta en el Padrón Municipal de Habitantes de este Ayuntamiento de <xsl:value-of select="datosAyto/concello"/>, así como de las otras personas que se relacionan, de las que tienen representación.
									</td>
									<td width="140mm" align="right">
										<xsl:choose>
										<xsl:when test="titulo='ALTA POR CAMBIO DE RESIDENCIA' and datosHabitante/habitante[position() = ($nh)]/codProvinciaProcedencia!= '' and datosHabitante/habitante[position() = ($nh)]/codMunicipioProcedencia!= ''">
											<xsl:call-template name="datosAytoBaja">
												<xsl:with-param name="nh">1</xsl:with-param>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise></xsl:otherwise>
									</xsl:choose>
									</td>
								</tr>
								</table>
						</td>
					</tr>
					<tr>
						<td height="15mm">
							Firma de todos los mayores de edad
						</td>
					</tr>
					<tr>
						<td height="4mm">
							Relación de personas que se inscriben
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
						<td height="30mm">
							<xsl:call-template name="dh">
								<xsl:with-param name="nh">1</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td height="60mm">
								<xsl:call-template name="certificacionAlta">
									<xsl:with-param name="nh">1</xsl:with-param>
								</xsl:call-template>
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
										<td width="71mm"> <!-- Lugar-->
											<table width="71mm" border="0" cellpadding="0mm">
												<tr>
													<td width="10mm" class="textoFijo1" ><!-- Codigo Provincia Nacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codProvinciaNacimiento"/>
													</td>
													<td width="61mm" class="textoFijo1"> <!-- Provincia Nacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/provinciaNacimiento"/>
													</td>
												</tr>
												<tr>
													<td width="10mm" class="textoFijo1" ><!-- Codigo MunicipioNacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codLugarNacimiento"/>												</td>
													<td width="61mm" class="textoFijo1"> <!-- Municipio Nacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/lugarNacimiento"/>
													</td>
												</tr>
											</table>
										</td>
										<td width="20mm" valign="middle" class="textoFijo1">
											<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/fechaNacimiento"/>
										</td>
									</tr>
									<tr>
										<td colspan="2"> <!-- Nacionalidad -->
											<table width="91mm" border="0" cellpadding="0mm">
												<tr>
													<td width="10mm" class="textoFijo1" > <!-- Cod. Pais Nacionalidad-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codPaisNacionalidad"/>
													</td>
													<td width="81mm" class="textoFijo1"> <!-- Desc. Pais Nacionalidad -->
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
											<td width="10mm" class="textoFijo1" > <!-- Cod. Titulacion -->
												<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codTitulacion"/>
											</td>
											<td width="36mm" class="textoFijo1"> <!-- Desc. Titulacion -->
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
			<xsl:param name="nh" />

			<table border="0" align="center" width="270mm" cellpadding="0">
				<tr>
					<td colspan="2" height="6mm" align="center"><b>CERTIFICACIÓN DE ALTA</b></td>
				</tr>
				<tr>
					<td width="135mm">
						<xsl:call-template name="dAytoAlta">
						</xsl:call-template>
					</td>
					<td width="135mm" align="right">
						<xsl:call-template name="diligenciaAlta">
							<xsl:with-param name="nh">1</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</xsl:template>

				<xsl:template name="dAytoAlta">
					<table border="0" width="132mm" cellpadding="0">
					<tr>
						<td height="5mm">
							Datos del ayuntamiento de ALTA
						</td>
					</tr>
					<tr>
						<td class="textoFijo1">
							<table border="1" width="132mm" cellpadding="3">
							<tr>
								<td width="13mm" height="1mm"></td>
								<td width="12mm" height="1mm"></td>
								<td width="30mm" height="1mm"></td>
								<td width="43mm" height="1mm"></td>
								<td width="24mm" height="1mm"></td>
								<td width="10mm" height="1mm"></td>
							</tr>
							<tr>
								<td width="13mm" class="textoFijo1">Distrito: </td>
								<td width="12mm" class="textoFijo1">
									<xsl:value-of select="datosPadron/distrito"/>
								</td>
								<td width="30mm" class="textoFijo1">Entidad colectiva: </td>
								<td colspan="2" class="textoFijo1">
									<xsl:value-of select="datosPadron/entidadColectiva"/>
								</td>
								<td width="10mm" class="textoFijo1">
									<xsl:value-of select="datosPadron/codEntidadColectiva"/>
								</td>
							</tr>
							<tr>
								<td width="13mm" class="textoFijo1">Sección: </td>
								<td width="12mm" class="textoFijo1">
									<xsl:value-of select="datosPadron/seccion"/>
								</td>
								<td width="30mm" class="textoFijo1">Entidad singular: </td>
								<td colspan="2" class="textoFijo1">
									<xsl:value-of select="datosPadron/entidadSingular"/>
								</td>
								<td width="10mm" class="textoFijo1">
									<xsl:value-of select="datosPadron/codEntidadSingular"/>
								</td>
							</tr>
							<tr>
								<td width="13mm" class="textoFijo1"></td>
								<td width="12mm" class="textoFijo1"> </td>
								<td width="30mm" class="textoFijo1">Núcleo/Diseminado: </td>
								<td colspan="2" class="textoFijo1">
									<xsl:value-of select="datosPadron/nucleo"/>
								</td>
								<td width="10mm" class="textoFijo1">
									<xsl:value-of select="datosPadron/codNucleo"/>
								</td>
							</tr>
							<tr>
								<td width="13mm" class="textoFijo1">Domicilio: </td>
								<td colspan="3" class="textoFijo1">
									<xsl:value-of select="datosVivienda/nombreVia"/>
								</td>
								<td colspan="2" class="textoFijo1">
									Nº: <xsl:if test="datosVivienda/numero1!=''">
											<xsl:value-of select="datosVivienda/numero1"/>
										</xsl:if>
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
							<tr>
								<td colspan="6">
									<table border="0" align="center" width="132mm" >
									<tr>
										<td width="1.5mm" class="textoFijo1"> </td>
										<td width="8mm" class="textoFijo1">Km: </td>
										<td width="10mm" class="textoFijo1">
											<xsl:value-of select="datosVivienda/km"/>
										</td>
										<td width="8mm" class="textoFijo1">Blq: </td>
										<td width="10mm" class="textoFijo1">
											<xsl:value-of select="datosVivienda/bloque"/>
										</td>
										<td width="8mm" class="textoFijo1">Prtl: </td>
										<td width="10mm" class="textoFijo1">
											<xsl:value-of select="datosVivienda/portal"/>
										</td>
										<td width="8mm" class="textoFijo1">Esc: </td>
										<td width="10mm" class="textoFijo1">
											<xsl:value-of select="datosVivienda/escalera"/>
										</td>
										<td width="8mm" class="textoFijo1">Plt: </td>
										<td width="10mm" class="textoFijo1">
											<xsl:value-of select="datosVivienda/planta"/>
										</td>
										<td width="9mm" class="textoFijo1">Prta: </td>
										<td width="10mm" class="textoFijo1">
											<xsl:value-of select="datosVivienda/puerta"/>
										</td>
										<td width="7mm" class="textoFijo1">CP: </td>
										<td width="14mm" class="textoFijo1"></td>
									</tr>
									</table>
								</td>
							</tr>
							<tr style="border-top:1"><td colspan="6" height="1mm"></td></tr>
							<tr>
								<td width="15mm" class="textoFijo1">Provincia: </td>
								<td colspan="4" class="textoFijo1">
									<xsl:value-of select="datosPadron/descProvincia"/>
								</td>
								<td width="10mm" class="textoFijo1">
									<xsl:value-of select="datosPadron/codProvincia"/>
								</td>
							</tr>
							<tr>
								<td width="15mm" class="textoFijo1">Ayuntamiento: </td>
								<td colspan="4" class="textoFijo1">
									<xsl:value-of select="datosPadron/descMunicipio"/>
								</td>
								<td width="10mm" class="textoFijo1">
									<xsl:value-of select="datosPadron/codMunicipio"/>
								</td>
							</tr>
							</table>
						</td>
					</tr>
					</table>
				</xsl:template>


				<xsl:template name="diligenciaAlta">
					<xsl:param name="nh" />
						<table border="0" align="center" width="135mm" cellpadding="0">
							<tr>
								<td width="75mm" height="6mm"></td>
								<td width="60mm" height="6mm"></td>
							</tr>
							<tr>
								<td colspan="2" height="32mm" class="textoDiligencia">
										Diligencia: para hacer constar que, por Resolución de Alcaldía la fecha  <xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/fechaOperacion"/> fueron dadas de alta y anotadas en el Padrón como residentes en este ayuntamiento, las personas en número de 1 que figuran relacionadas en este documento.
								</td>
							</tr>
							<tr>
								<td colspan="2" height="12mm">
									<xsl:value-of select="datosAyto/ayto"/>, a <xsl:value-of select="fechaDocFor"/>
								</td>
							</tr>
							<tr>
								<td width="75mm">
									(Sello del ayuntamiento)
								</td>
								<td width="60mm" align="right">
									El/La Secretario/a
								</td>
							</tr>
						</table>
				</xsl:template>


		<xsl:template name="datosAytoBaja">
			<xsl:param name="nh"/>
				<table border="0" width="135mm" cellpadding="0">
					<tr>
						<td  height="5mm">
							Datos del ayuntamiento de BAJA
						</td>
					</tr>
					<tr>
						<td class="textoFijo1" height="15mm">
							<table border="1" width="135mm" cellpadding="3">
								<tr>
									<td width="40mm" class="textoFijo1">
											Provincia o país:
									</td>
									<td width="85mm" class="textoFijo1">
												<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/descProvinciaProcedencia"/>
									</td>
									<td width="10mm" class="textoFijo1">
												<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codProvinciaProcedencia"/>
									</td>
								</tr>
								<tr>
									<td width="40mm" class="textoFijo1">
										Ayuntamiento o país:
									</td>
									<td width="85mm" class="textoFijo1">
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/descMunicipioProcedencia"/>
									</td>
									<td width="10mm" class="textoFijo1">
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codMunicipioProcedencia"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="3" height="10mm" class="textoDiligencia">
							Así mismo declaran: que estaban(n) inscritos en el Padrón Municipal de Habitantes del Ayuntamiento de <xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/descMunicipioProcedencia"/>, provincia de <xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/descProvinciaProcedencia"/>.
						</td>
					</tr>
				</table>
		</xsl:template>

</xsl:stylesheet>
