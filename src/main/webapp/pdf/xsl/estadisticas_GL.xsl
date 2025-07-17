<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ESTADISTICA">
		<table width="175mm" height="15mm" padding="1" valign="top" align="center">
			<tr>
				<td class="titulo1" align="center" width="100%">INFORME ESTADÍSTICO DE DATOS PADRONAIS</td>
			</tr>
		</table>
		<table width="175mm" padding="1" align="left">
			<tr>
				<td>
					<table width="175mm" padding="0" valign="middle" align="center">
						<tr>
							<td class="etiqueta" width="20%">Tipo Documento:</td>
							<td colspan = "3" class="notaPeq" width="80%"><xsl:value-of select="TIPO_DOC"/></td>
						</tr>
						<tr>
							<td class="etiqueta" width="20%">Titulación:</td>
							<td colspan = "3" class="notaPeq" width="80%"><xsl:value-of select="TITUL"/></td>
						</tr>
						<tr >
							<td class="etiqueta" width="20%">Nacionalidade:</td>
							<td colspan = "3" class="notaPeq" width="80%"><xsl:value-of select="NACION"/></td>
						</tr>
						<tr >
							<td class="etiqueta" width="20%">F. Nacemento:</td>
							<td class="notaPeq" width="30%"><xsl:value-of select="FEC_NAC"/></td>
							<td class="etiqueta" width="15%">Sexo:</td>
							<td class="notaPeq" width="35%"><xsl:value-of select="SEXO"/></td>
						</tr>
						<tr >
							<td class="etiqueta" width="20%">F. Empadronamento:</td>
							<td class="notaPeq" width="30%"><xsl:value-of select="FEC_EMP"/></td>
							<td class="etiqueta" width="15">Situación:</td>
							<td class="notaPeq" width="35%"><xsl:value-of select="SITUACION"/></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	    <TABLE width="150mm">
	    <tr>
	    	<td colspan="3"></td>
	    </tr>
		<xsl:call-template name="COLECTIVA">
			<xsl:with-param name="nf">1</xsl:with-param>
		</xsl:call-template>
		<TR width="150mm" style="border-bottom-width:1px;font-weight:bold;">
			<TD colspan="2" width="125mm" class="titulo"  height="10mm" valign="bottom">
				TOTAL 
			</TD>
			<TD width="25mm" style="font-weight:bold;"  height="10mm" valign="bottom" align="right">
				<xsl:value-of select="sum(//ENT_COLECTIVA/TOTAL)"/>
			</TD>
		</TR>
		</TABLE>
    </xsl:template>


	<xsl:template name="COLECTIVA">
		<xsl:for-each select = "//ENT_COLECTIVA" >

			<TR width="150mm" style="border-bottom-width:1px;font-weight:bold;">
				<TD colspan="2" width="125mm" class="titulo"  height="10mm" valign="bottom">
					<xsl:value-of select="ECO_NOM"/> 
				</TD>
				<TD width="25mm" style="font-weight:bold;"  height="10mm" valign="bottom" align="right">
					<xsl:value-of select="TOTAL"/>
				</TD>
			</TR>
			<xsl:call-template name="SINGULAR"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="SINGULAR">
		<xsl:for-each select = "ENT_SINGULAR" >

			<TR width="150mm">
				<TD width="25mm"></TD>
				<TD width="100mm" class="nota">
					<xsl:value-of select="ESI_NOM"/> 
				</TD>
				<TD width="25mm" class="nota" align="right">
					<xsl:value-of select="TOTAL"/>
				</TD>
			</TR>
			<xsl:call-template name="SINGULAR"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="ECO_COD">
		<xsl:param name="nf">1</xsl:param>
		<xsl:if test=". != ''">
			<xsl:value-of select="ROW[position() = $nf]/ECO_COD"/> 
		</xsl:if>
	</xsl:template>

	<xsl:template name="ECO_CIN">
		<xsl:param name="nf">1</xsl:param>
		<xsl:if test=". != ''">
			<xsl:value-of select="ROW[position() = $nf]/ECO_CIN"/> 
		</xsl:if>
	</xsl:template>

	<xsl:template name="ECO_NOM">
	    <xsl:param name="nf">1</xsl:param>
		<xsl:if test=". != ''">
			<xsl:value-of select="ROW[position() = $nf]/ECO_NOM"/> 
		</xsl:if>
	</xsl:template>

	<xsl:template name="ESI_COD">
		<xsl:param name="nf">1</xsl:param>
		<xsl:if test=". != ''">
			<xsl:value-of select="ROW[position() = $nf]/ESI_COD"/> 
		</xsl:if>
	</xsl:template>

	<xsl:template name="ESI_CIN">
		<xsl:param name="nf">1</xsl:param>
		<xsl:if test=". != ''">
			<xsl:value-of select="ROW[position() = $nf]/ESI_CIN"/> 
		</xsl:if>
	</xsl:template>

	<xsl:template name="ESI_NOM">
		<xsl:param name="nf">1</xsl:param>
		<xsl:if test=". != ''">
			<xsl:value-of select="ROW[position() = $nf]/ESI_NOM"/> 
		</xsl:if>
	</xsl:template>

	<xsl:template name="TOTAL">
		<xsl:param name="nf">1</xsl:param>
		<xsl:if test=". != ''">
			<xsl:value-of select="ROW[position() = $nf]/TOTAL"/> 
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>

