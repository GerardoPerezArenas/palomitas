<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/">
		<xsl:apply-templates select="ROWSET"/>
	</xsl:template>

	<xsl:template match="ROWSET">
  	<xsl:choose>
    	<xsl:when test="ROW">
<table width="100%" align="center" cellborder="1" cellpadding="0">	
				<xsl:apply-templates select="ROW"/>
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
		<td width="5%">
			<table cellpadding="1" width="100%" height="100%">
				<tr>
					<td align="center" height="8mm">
						NºOrden
					</td>
				</tr>
				<tr>
					<td style="font-size:12;font-weight:bold;" align="center" valign="middle" height="12mm">
						<xsl:value-of select="ORD"/>
					</td>
				</tr>
			</table>
		</td>
		<td width="25%">
			<table cellpadding="0" width="100%">
				<tr>
					<td border-bottom="1">
						<table cellpadding="2" width="100%">
							<tr>
								<td height="4mm">
									Nombre
								</td>
							</tr>
							<tr>
								<td style="font-weight:bold;font-variant:small-caps;" height="4mm">
									<xsl:value-of select="NOM"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td  border-bottom="1">
						<table cellpadding="2" width="100%">
							<tr>
								<td height="4mm">
									1º Apellido
								</td>
							</tr>
							<tr>
								<td style="font-weight:bold;font-variant:small-caps;" height="4mm">
									<xsl:apply-templates select="PA1"/><xsl:value-of select="AP1"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td style="border-bottom:0">
						<table cellpadding="2" width="100%">
							<tr>
								<td height="4mm">
									2º Apellido
								</td>
							</tr>
							<tr>
								<td style="font-weight:bold;font-variant:small-caps;" height="4mm">
									<xsl:apply-templates select="PA2"/><xsl:value-of select="AP2"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td width="15%">
			<table cellpadding="0" width="100%">
				<tr>
					<td>
						<table cellpadding="1">
							<tr border-bottom="1">
								<td height="4mm" >
									Hombre
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="SEX = 'H'">x</xsl:when>
										<xsl:otherwise> </xsl:otherwise>
									</xsl:choose>
								</td>
								<td>
									Mujer
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="SEX = 'M'">x</xsl:when>
										<xsl:otherwise> </xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
							<tr>
								<td colspan="4" height="4mm">
									Si se trata de un Alta por traslado de residencia indique:
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" cellpadding="1">
							<tr>
								<td height="8mm">
									Municipio (o Consulado) de procedencia
								</td>
							</tr>
							<tr>
								<td height="8mm">
									Provincia (o País) de procedencia
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td width="35%">
			<table width="100%" cellpadding="0">
				<tr border-bottom="1">
					<td border-right="1">
						<table cellpadding="1" width="100%">
							<tr>
								<td height="4mm">					
									Fecha de Nacimiento(día,mes,año)
								</td>
							</tr>
							<tr>
								<td height="4mm" style="font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="FNA"/>
								</td>
							</tr>
						</table>
					</td>
					<td>
						<table cellpadding="1" width="100%">
							<tr>
								<td height="4mm">
									Provincia
								</td>
							</tr>
							<tr>
								<td height="4mm" style="font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="PRN"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr  border-bottom="1">
					<td colspan="2">
						<table cellpadding="1" width="100%">
							<tr>
								<td height="4mm">
									Municipio (o País) de Nacimiento
								</td>
							</tr>
							<tr>
								<td height="4mm" style="font-weight:bold;font-variant:small-caps;">
									<xsl:choose>
										<xsl:when test="PRN = ''"><xsl:value-of select="PAN"/> </xsl:when>
										<xsl:otherwise><xsl:value-of select="MNA"/> </xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table cellpadding="1" width="100%">
							<tr>
								<td height="4mm">
									País de Nacionalidad
								</td>
							</tr>
							<tr>
								<td height="4mm" style="font-weight:bold;font-variant:small-caps;">
									<xsl:value-of select="PNA"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
		<td width="15%">
			<table width="100%" height="100%">
				<tr>
					<td align="center" colspan="6">
						Tipo de documento de Identidad
					</td>
				</tr>
				<tr>
					<td>
						D.N.I.
					</td>
					<td style="font-weight:bold;font-variant:small-caps;">
						<xsl:choose>
							<xsl:when test="TID = 1">x</xsl:when>
							<xsl:otherwise> </xsl:otherwise>
						</xsl:choose>
					</td>
					<td>
						Pasaporte
					</td>
					<td style="font-weight:bold;font-variant:small-caps;">
						<xsl:choose>
							<xsl:when test="TID = 2">x</xsl:when>
							<xsl:otherwise> </xsl:otherwise>
						</xsl:choose>
					</td>
					<td>
						Tarj.Res.
					</td>
					<td style="font-weight:bold;font-variant:small-caps;">
						<xsl:choose>
							<xsl:when test="TID = 3">x</xsl:when>
							<xsl:otherwise> </xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<tr>
					<td colspan="6" style="font-weight:bold;font-variant:small-caps;">
						<xsl:value-of select="DOC"/>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						Nivel de estudios terminados:
					</td>
					<td colspan="2" style="font-weight:bold;font-variant:small-caps;">
						<xsl:value-of select="TIT"/> 
					</td>
				</tr>
				<tr>
					<td colspan="6">
						(ver códigos en reverso)
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</xsl:template>

	<xsl:template match="PA1">
		<xsl:if test=". != ''">
			<xsl:value-of select="."/> 
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="PA2">
		<xsl:if test=". != ''">
			<xsl:value-of select="."/> 
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>

