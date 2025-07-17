<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ROWSET">
  	<xsl:choose>
    	<xsl:when test="ROW">
<table width="100%" align="center" cellborder="0" cellpadding="0">	
				<xsl:apply-templates/>
</table>	
	    </xsl:when>
  	  <xsl:otherwise>
<table width="100%" cellborder="0" cellpadding="4" align="center">
	<tr><td></td></tr>
</table>
    	</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
    
	<xsl:template match="ROW">
	<tr>
		<td>
			<table width="100%">
				<tr>
					<td width="50%" style="font-size:12;font-weight:bold;">
						<xsl:value-of select="CTE"/>
					</td>
					<td align="right" width="50%" style="font-size:12;font-weight:bold;">
						Hoja de inscripción o modificación
					</td>
				</tr>
				<tr>
					<td width="50%" style="font-size:12;font-weight:bold;">
						Padrón Municipal
					</td>
					<td align="right" width="50%">
						Hoja: <pagenumber/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table cellborder="1" width="100%" cellpadding="0">
				<tr>				
					<td width="55%">
						<table cellborder="0" width="100%">
							<tr>
								<td style="font-size:6;">
									Calle,plaza,etc
								</td>
								<td style="font-size:6;">
									Nombre vía
								</td>
							</tr>
							<tr>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="ABR"/> 
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="VIA"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									N.Desde
								</td>
								<td style="font-size:6;">
									L.Desde
								</td>
								<td style="font-size:6;">
									N.Hasta
								</td>
								<td style="font-size:6;">
									L.Hasta
								</td>
								<td style="font-size:6;">
									Km.
								</td>
								<td style="font-size:6;">
									Hm.
								</td>
								<td style="font-size:6;">
									Bloque
								</td>
								<td style="font-size:6;">
									Portal
								</td>
								<td style="font-size:6;">
									Esc.
								</td>
								<td style="font-size:6;">
									Planta
								</td>
								<td style="font-size:6;">
									Puerta
								</td>
							</tr>
							<tr>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="NUD"/> 
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="LED"/> 
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="NUH"/>
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="LEH"/>
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="KMT"/>
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="HMT"/>
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="BLQ"/>
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="POR"/>
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="ESC"/>
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="PLT"/>
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="PTA"/>
								</td>
							</tr>
						</table>
					</td>
					<td rowspan="2" width="30%">
						<table cellborder="0" width="100%">
							<tr>
								<td colspan="2" style="font-size:6;font-weight:bold;" align="right">
									A cumplimentar por el
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Provincia:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="PRV"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Municipio:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="MUN"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Entidad colectiva:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="ECO"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Entidad singular:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="ESI"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Nucleo / Diseminado:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="NUC"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Tipo de vivienda:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="TVV"/>
								</td>
							</tr>
						</table>
					</td>
					<td rowspan="2" width="15%">
						<table cellborder="0" width="100%">
							<tr>
								<td colspan="2" style="font-size:6;font-weight:bold;">
									Ayuntamiento
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Distrito:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="DIS"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Sección:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="SEC"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Manzana:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="MAN"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Familia:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="FAM"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Código de vía:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="VIACOD"/>
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									Inscripción:
								</td>
								<td style="font-size:6;font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="NUM"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table cellborder="0" width="100%">
							<tr>
								<td align="center" style="font-size:12;font-weight:bold;" colspan="4">
									Información Voluntaria
								</td>
							</tr>
							<tr>
								<td style="font-size:6;" colspan="4">
									Autorizamos a los mayores de edad empadronados en esta hoja para comunicar al Ayuntamiento las futuras variaciones de nuestros y obtener
								</td>
							</tr>
							<tr>
								<td style="font-size:6;">
									certificaciones o volantes de empadronamiento.
								</td>
								<td style="font-size:6;">
									SI		
								</td>
								<td style="font-size:6;">
									NO		
								</td>
								<td style="font-size:6;">
									Teléfono: <xsl:value-of select="TEL"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</xsl:template>

</xsl:stylesheet>

