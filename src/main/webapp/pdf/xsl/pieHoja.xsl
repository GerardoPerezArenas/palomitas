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
		<td width="85%" height="4mm" colspan= "3" style="font-style:italic" valign="middle">
			Declaramos que las personas relacionadas en esta hoja residen en el domicilio indicado y que sus datos son correctos
		</td>
		<td width="15%" rowspan="2">
			<table cellborder="0" cellpadding="1" width="100%">
				<tr>
					<td align="center">
						A cumplimentar por el Ayuntamiento
					</td>
				</tr>
				<tr>
					<td align="center">
						Fecha de recepción de la hoja
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle" height="10mm" style="font-weight:bold;">
						<xsl:value-of select="FEC"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td width="10%" height="20mm">
			<table cellborder="0" cellpadding="1" width="100%">
				<tr>
					<td align="center">
						Número total de
					</td>
				</tr>
				<tr>
					<td align="center">
						personas inscritas en
					</td>
				</tr>
				<tr>
					<td align="center">
						esta hoja
					</td>
				</tr>
				<tr>
					<td style="font-weight:bold;" height="10mm" align="center" valign="middle">
						<xsl:value-of select="CON"/>
					</td>
				</tr>
			</table>
		</td>
		<td width="60%" align="center" valign="top">
			Firma de los mayores de edad que se inscriben en esta hoja
		</td>
		<td width="15%">
			<table cellborder="0" cellpadding="1" width="100%">
				<tr>
					<td align="center">
						(Para altas en inscripciones existentes)
					</td>
				</tr>
				<tr>
					<td align="center">
						Firma de persona mayor de edad
					</td>
				</tr>
				<tr>
					<td align="center">
						anteriormente inscrita
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</xsl:template>

</xsl:stylesheet>