<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/LISTADO_EXPEDIENTES">

        <xsl:if test=" ( (//CRITERIOS/PROCEDIMIENTO!= '') and (//CRITERIOS/PROCEDIMIENTO!= 'VARIOS'))
                or (//CRITERIOS/ESTADO != '') or (//CRITERIOS/TITULAR != '')
                or (//CRITERIOS/LOCALIZACION != '') or (//CRITERIOS/CLASIFICACION_TRAMITE != '')">
          
        </xsl:if>

        <xsl:choose>
            <xsl:when test="(//CRITERIOS/ESTADO = '')">
                <xsl:choose>
                    <xsl:when test="(//CRITERIOS/PROCEDIMIENTO= '') or (//CRITERIOS/PROCEDIMIENTO= 'VARIOS')">
                        <xsl:choose>
                            <xsl:when test="(//CRITERIOS/TITULAR= '') ">
                                <xsl:call-template name = "todas" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:call-template name = "todas" />
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:choose>
                            <xsl:when test="(//CRITERIOS/TITULAR= '') ">
                                <xsl:call-template name = "todas" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:call-template name = "todas" />
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="(//CRITERIOS/PROCEDIMIENTO= '') or (//CRITERIOS/PROCEDIMIENTO= 'VARIOS')">
                        <xsl:choose>
                            <xsl:when test="(//CRITERIOS/TITULAR= '') ">
                                <xsl:call-template name = "todas" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:call-template name = "todas" />
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:choose>
                            <xsl:when test="(//CRITERIOS/TITULAR= '') ">
                                <xsl:call-template name = "todas" />
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:call-template name = "todas" />
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


  

    <xsl:template name = "todas" >
        <xsl:call-template name = "etiquetaNumExpediente" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaEstado" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaProcedimiento" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaTitular" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaAsunto" />
        <xsl:text>;</xsl:text>
        <xsl:if test="(//CRITERIOS/TIPO_LISTADO = 0)">
            <xsl:call-template name = "etiquetaTramites" />

        </xsl:if>
        <xsl:if test="(//CRITERIOS/TIPO_LISTADO = 1)">
            <xsl:call-template name = "etiquetaTramitesFinalizados" />
        </xsl:if>
        <xsl:text>;</xsl:text> 

        <xsl:call-template name = "fechaInicio" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "fechaFin" />
        <xsl:text>&#10;</xsl:text>

        <xsl:for-each select="EXPEDIENTE">
            <xsl:value-of select="NUMERO"/><xsl:text>;</xsl:text>
			<xsl:value-of select="FECHA_INICIO"/><xsl:text>;</xsl:text>
            <xsl:value-of select="FECHA_FIN"/><xsl:text>;</xsl:text>
            <xsl:value-of select="DESCRIPCION_PROCEDIMIENTO"/><xsl:text>;</xsl:text>
            <xsl:if test="( TITULAR != 'null')">
                <xsl:value-of select="TITULAR"/>
                <xsl:text>;</xsl:text>
            </xsl:if>
            <xsl:value-of select="ASUNTO"/><xsl:text>;</xsl:text>

            <xsl:for-each select="./TRAMITE">
                <xsl:variable name="pos" select="position()"/>
                <xsl:if test="($pos = 1)">
                    <xsl:value-of select="NOMBRE"/><xsl:text>;</xsl:text>
                    <xsl:value-of select="FECHA_INICIO"/><xsl:text>;</xsl:text>
                    <xsl:value-of select="FECHA_FIN"/>
                </xsl:if>

                <xsl:if test="($pos > 1)">
                    <xsl:text>&#10;</xsl:text>
                    <xsl:value-of select="../NUMERO"/><xsl:text>;</xsl:text>
                    <xsl:value-of select="../FECHA_INICIO"/><xsl:text>;</xsl:text>
                    <xsl:value-of select="../FECHA_FIN"/><xsl:text>;</xsl:text>
                    <xsl:value-of select="../DESCRIPCION_PROCEDIMIENTO"/><xsl:text>;</xsl:text>
                    <xsl:if test="( ../TITULAR != 'null')">
                        <xsl:value-of select="../TITULAR"/>
                        <xsl:text>;</xsl:text>
                    </xsl:if>
                    <xsl:value-of select="../ASUNTO"/><xsl:text>;</xsl:text>
                    <xsl:value-of select="NOMBRE"/><xsl:text>;</xsl:text>
                    <xsl:value-of select="FECHA_INICIO"/><xsl:text>;</xsl:text>
                    <xsl:value-of select="FECHA_FIN"/>
                </xsl:if>
            </xsl:for-each>
            <xsl:text>&#10;</xsl:text>
        </xsl:for-each>

    </xsl:template>

    <xsl:template name = "etiquetaNumExpediente" > N� EXPTE</xsl:template>
    <xsl:template name = "etiquetaInicio" > DATA INICIO</xsl:template>
    <xsl:template name = "etiquetaEstado" > DATA FIN</xsl:template>
    <xsl:template name = "etiquetaProcedimiento" > PROCEDEMENTO</xsl:template>
    <xsl:template name = "etiquetaTitular" > TITULAR</xsl:template>
    <xsl:template name = "etiquetaAsunto" > ASUNTO</xsl:template>
    <xsl:template name = "etiquetaTramites" > TR�MITES PENDENTES</xsl:template>
    <xsl:template name = "etiquetaTramitesFinalizados" > TRAMITES REMATADOS</xsl:template>
    <xsl:template name = "etiquetaLocalizacion" > LOCALIZACI�N</xsl:template>
    <xsl:template name = "etiquetaClasificacionTramite" > CLASIFICACI�N DE TR�MITE</xsl:template>
    <xsl:template name = "fechaInicio" > DATA INICIO TR�MITE</xsl:template>
    <xsl:template name = "fechaFin" > DATA FIN TR�MITE</xsl:template>

</xsl:stylesheet>
