<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<!-- Página por habitante, sólo se considera el primero.	-->

	<xsl:template match="//inscripcion">
	<pbr size="A4"/>
		<xsl:call-template name="pagina_1"/>
		<xsl:call-template name="lista"/>
		<xsl:call-template name="pagina_2"/>
	</xsl:template>


	<xsl:template name="pagina_1">
		<table border="0" align="center" width="180mm" cellpadding="0mm">
		<tr>
			<td>
				<xsl:call-template name="datosAytoBaja"/>
			</td>
		</tr>
		<tr><td height="40mm"></td></tr>
		<tr>
			<td>
				<table border="0" align="center" width="180mm"  cellpadding="0">
					<tr>
						<td width="180mm" align="left" class="textoDiligencia" >
							De acordo co artº 70 do R.D. 2612/1996 do 20 de Decembro de 1996, publicado no B.O.E. nº 14 do 16 de Xaneiro de 1997, polo que se modifica o Regulamento de Poboación e Demarcación Territorial das Entidades Locais, comunícolle que deberán causar BAIXA nese concello as persoas que se relacionan, por cumplimentaren a súa Alta no Padrón Municpial de Habitantes deste Concello.
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr><td height="12mm"></td></tr>
		<tr>
			<td height="4mm">
				Relación:
			</td>
		</tr>
		<tr><td height="10mm"></td></tr>
		</table>
	</xsl:template>

	<xsl:template name="lista">
		<table border="0" align="center" width="180mm" cellpadding="0mm">
			<thead>
				<tr>
					<td width="70mm" class="textoDiligencia"  height="3mm">Nome e apelidos</td>
					<td width="40mm" class="textoDiligencia" align="center"  height="3mm">Data de nacemento</td>
					<td width="35mm" class="textoDiligencia" align="center"  height="3mm">Documento</td>
					<td width="35mm" class="textoDiligencia" align="center"  height="3mm">Data de Alta</td>
				</tr>
				<tr>
					<td width="70mm" style="border-top-width:1px" height="4mm"></td>
					<td width="40mm" style="border-top-width:1px" height="4mm"></td>
					<td width="35mm" style="border-top-width:1px" height="4mm"></td>
					<td width="35mm" style="border-top-width:1px" height="4mm"></td>
				</tr>
			</thead>
			<tbody>
			  <xsl:for-each select="datosHabitante/habitante">
				<tr>
							<td width="70mm" class="textoFijo1"  height="3mm">
								<xsl:value-of select="nombre"/>
							</td>
							<td width="40mm" class="textoFijo1" align="center" height="3mm">
								<xsl:value-of select="fechaNacimiento"/>
							</td>
							<td width="35mm" class="textoFijo1" align="center" height="3mm">
											<xsl:choose>
											<xsl:when test="tipoDocumento = '0'">
											SIN DOCUMENTO
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="numeroDocumento"/><xsl:value-of select="letraDocumento"/>
											</xsl:otherwise>
											</xsl:choose>
							</td>
							<td width="35mm" class="textoFijo1"  align="center" height="3mm">
								<xsl:value-of select="fechaEmpadronamiento"/>
							</td>

            	</tr>
			  </xsl:for-each>
			  </tbody>
			  <tfoot>
			  <tr>
					<td width="70mm" style="border-bottom-width:1px" height="6mm"></td>
					<td width="40mm" style="border-bottom-width:1px" height="6mm"></td>
					<td width="35mm" style="border-bottom-width:1px" height="6mm"></td>
					<td width="35mm" style="border-bottom-width:1px" height="6mm"></td>
				</tr>
				<tr>
					<td width="70mm" height="5mm"></td>
					<td width="40mm" height="5mm"></td>
					<td width="35mm" height="5mm"></td>
					<td width="35mm" height="5mm"></td>
				</tr>
				</tfoot>
		</table>
	</xsl:template>

	<xsl:template name="pagina_2">
		<table border="0" align="center" width="180mm" cellpadding="0mm">
			<tr>
				<td align ="left">
					<xsl:call-template name="datosAytoAlta"/>
				</td>
			</tr>
		</table>
	</xsl:template>

			<xsl:template name="datosAytoBaja">

				<table border="0" width="180mm" cellpadding="0">
				<tr height="25mm">
				<td height="25mm">
				</td>
				</tr>
								<tr>
					<td align ="right" class="cabecera">
						D. ALCALDE DO CONCELLO DE <xsl:value-of select="datosHabitante/habitante[position() = 1]/descMunicipioProcedencia"/>
					</td>
				</tr>
				<tr>
					<td align ="right" class="cabecera">
						<xsl:value-of select="datosHabitante/habitante[position() = 1]/descProvinciaProcedencia"/>
					</td>
				</tr>
				<tr>
					<td  align ="right" height="5mm" class="textoFijo1">
							Negociado de Estatística - Padrón de Habitantes
					</td>
				</tr>
				</table>
			</xsl:template>

			<xsl:template name="datosAytoAlta">
				<table border="0" width="180mm" cellpadding="0">
				<tr>
					<td class="textoFijo1" height="10mm" align="center">
							<xsl:value-of select="datosAyto/ayto"/>, a <xsl:value-of select="fechaDocumento"/>
					</td>
				</tr>
				<tr>
					<td class="textoFijo1" align="center" height="20mm" width="110mm">O <xsl:value-of select="cargoAlcalde"/></td>
				</tr>

				<tr>
					<td class="textoFijo1" height="10mm" align="center" width="110mm"> Asdo.: <xsl:value-of select="nombreAlcalde"/></td>
				</tr>
				</table>
			</xsl:template>

</xsl:stylesheet>


