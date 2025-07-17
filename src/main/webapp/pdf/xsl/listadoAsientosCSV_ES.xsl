<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/listado">
        <xsl:call-template name="etiquetaEjercicio"/>
        <xsl:text>;</xsl:text>
        <xsl:call-template name="etiquetaFecha"/>
        <xsl:text>;</xsl:text>
        <xsl:call-template name="etiquetaHora"/>
        <xsl:text>;</xsl:text>
        <xsl:call-template name="etiquetaAsunto"/>
        <xsl:text>;</xsl:text>
        <xsl:call-template name="etiquetaEstado"/>
        <xsl:text>;</xsl:text>
        <xsl:call-template name="etiquetaDestino"/>
        <xsl:text>;</xsl:text>
        <xsl:call-template name="etiquetaDocumento"/> 
        <xsl:text>;</xsl:text>
        <xsl:call-template name="etiquetaRemitente"/> 
        <xsl:text>;</xsl:text>
        <xsl:call-template name="etiquetaExpedientes"/> 
        <xsl:text>;</xsl:text>
        <xsl:call-template name="etiquetaUsuarioAlta"/> 
        <xsl:text>&#10;</xsl:text>
        <xsl:for-each select="fila">
            <xsl:value-of select="ejercicio"/>
            <xsl:text>;</xsl:text>
            <xsl:value-of select="fecha"/>
            <xsl:text>;</xsl:text> 
            <xsl:value-of select="hora"/>
            <xsl:text>;</xsl:text>
            <xsl:value-of select="asunto"/>
            <xsl:text>;</xsl:text>
            <xsl:value-of select="estadoAnotacion"/>
            <xsl:text>;</xsl:text>
            <xsl:value-of select="destino"/> 
            <xsl:text>;</xsl:text>
            <xsl:value-of select="documento"/> 
            <xsl:text>;</xsl:text>
            <xsl:value-of select="remitente"/> 
            <xsl:text>;</xsl:text>
            <xsl:value-of select="expedientes"/> 
            <xsl:text>;</xsl:text>
            <xsl:value-of select="usuarioAlta"/>
            <xsl:text>&#10;</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="etiquetaEjercicio">NÚMERO ANOTACIÓN</xsl:template>
    <xsl:template name="etiquetaFecha">FECHA</xsl:template>
    <xsl:template name="etiquetaHora">HORA</xsl:template>
    <xsl:template name="etiquetaAsunto">ASUNTO</xsl:template>
    <xsl:template name="etiquetaEstado">ESTADO ANOTACIÓN</xsl:template>
    <xsl:template name="etiquetaDestino">DESTINO</xsl:template>	 		  
    <xsl:template name="etiquetaDocumento">DOCUMENTO</xsl:template>	 		  
    <xsl:template name="etiquetaRemitente">REMITENTE</xsl:template>
    <xsl:template name="etiquetaExpedientes">EXPEDIENTES RELACIONADOS</xsl:template>
    <xsl:template name="etiquetaUsuarioAlta">USUARIO ALTA</xsl:template>	 		  
</xsl:stylesheet>




