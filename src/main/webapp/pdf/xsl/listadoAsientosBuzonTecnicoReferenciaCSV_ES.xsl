<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/listado">
		<xsl:call-template name="etiquetaNumeroAnotacion"/>
		<xsl:text>;</xsl:text>
		<xsl:call-template name="etiquetaFecha"/>
		<xsl:text>;</xsl:text>
		<xsl:call-template name="etiquetaHora"/>
		<xsl:text>;</xsl:text>
		<xsl:call-template name="etiquetaDocumentoRemitente"/>
		<xsl:text>;</xsl:text>
		<xsl:call-template name="etiquetaNombreRemitente"/>
		<xsl:text>;</xsl:text>
		<xsl:call-template name="etiquetaExtracto"/>
		<xsl:text>;</xsl:text>
                   <xsl:call-template name="etiquetaObservaciones"/>
                   <xsl:text>;</xsl:text>
		<xsl:call-template name="etiquetaDestino"/>
		<xsl:text>;</xsl:text>
		<xsl:call-template name="etiquetaCodAsunto"/> 
		<xsl:text>;</xsl:text>
		<xsl:call-template name="etiquetaAsunto"/> 
		<xsl:text>;</xsl:text>
		<xsl:call-template name="tecnicoReferencia"/> 
		<xsl:text>&#10;</xsl:text>
		<xsl:for-each select="fila">
                       <xsl:value-of select="numeroAnotacion"/><xsl:text>;</xsl:text>
                       <xsl:value-of select="fecha"/><xsl:text>;</xsl:text> 
                       <xsl:value-of select="hora"/><xsl:text>;</xsl:text>
                       <xsl:value-of select="documentoRemitente"/><xsl:text>;</xsl:text>
                       <xsl:value-of select="nombreRemitente"/><xsl:text>;</xsl:text>
                       <xsl:value-of select="extracto"/> <xsl:text>;</xsl:text>
                       <xsl:value-of select="observaciones"/> <xsl:text>;</xsl:text>
                       <xsl:value-of select="destino"/> <xsl:text>;</xsl:text>
                       <xsl:value-of select="codAsunto"/><xsl:text>;</xsl:text>
                       <xsl:value-of select="asunto"/><xsl:text>;</xsl:text>
                       <xsl:value-of select="tecnicoReferencia"/><xsl:text>;</xsl:text>
			<xsl:text>&#10;</xsl:text>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="etiquetaNumeroAnotacion">NÚMERO ANOTACIÓN</xsl:template>
	<xsl:template name="etiquetaFecha">FECHA</xsl:template>
	<xsl:template name="etiquetaHora">HORA</xsl:template>
	<xsl:template name="etiquetaDocumentoRemitente">DOC. REMITENTE</xsl:template>
	<xsl:template name="etiquetaNombreRemitente">NOMBRE REMITENTE</xsl:template>
	<xsl:template name="etiquetaExtracto">EXTRACTO</xsl:template>
        <xsl:template name="etiquetaObservaciones">OBSERVACIONES</xsl:template>
	<xsl:template name="etiquetaDestino">DESTINO</xsl:template>	 
	<xsl:template name="etiquetaCodAsunto">COD. ASUNTO</xsl:template>
	<xsl:template name="etiquetaAsunto">ASUNTO</xsl:template>
	<xsl:template name="tecnicoReferencia">TÉCNICO REFERENCIA</xsl:template>		

</xsl:stylesheet>