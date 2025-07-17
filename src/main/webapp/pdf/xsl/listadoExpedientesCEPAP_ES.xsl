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
        <xsl:call-template name = "etiquetaNExpediente" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaNumExpediente" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaNif" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaApellido1Int" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaApellido2Int" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaNombreInt" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaSexo" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaFechNacimiento" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaFechExpedicion" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaRealDecreto" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaFechaRD" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaDecretoMod" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaFechaDecretoMod" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaCodigoCP" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaNombreCertEs" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaNombreCertEuskera" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaNivel" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaCodCentro" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaDesCentro" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaObservaciones" />
        <xsl:text>;</xsl:text>
        <xsl:call-template name = "etiquetaProvinciaInteresado" />
        <xsl:text>&#10;</xsl:text>
        <!--<xsl:call-template name = "etiquetaFechaExpediente" />
        <xsl:text>;</xsl:text>-->
        <!--<xsl:call-template name = "etiquetaTraduccionRD" />
        <xsl:text>;</xsl:text>-->

        <xsl:for-each select="EXPEDIENTE">
            <xsl:value-of select="CODEXPEDIENTE"/><xsl:text>;</xsl:text>
            <xsl:value-of select="CLAVEREGISTRAL"/><xsl:text>;</xsl:text>
            <xsl:value-of select="NIF"/><xsl:text>;</xsl:text>
            <xsl:value-of select="APELLIDO1INTERESADO"/><xsl:text>;</xsl:text>
            <xsl:value-of select="APELLIDO2INTERESADO"/><xsl:text>;</xsl:text>
            <xsl:value-of select="NOMBREINTERESADO"/><xsl:text>;</xsl:text>
            <xsl:value-of select="SEXO"/><xsl:text>;</xsl:text>
            <xsl:value-of select="FECHNACIMIENTO"/><xsl:text>;</xsl:text>
            <xsl:value-of select="FECHEXPEDICION"/><xsl:text>;</xsl:text>
            <xsl:value-of select="REALDECRETO"/><xsl:text>;</xsl:text>
            <xsl:value-of select="FECHARD"/><xsl:text>;</xsl:text>
            <xsl:value-of select="DECRETOMOD"/><xsl:text>;</xsl:text>
            <xsl:value-of select="FECHADECRETOMOD"/><xsl:text>;</xsl:text>
            <xsl:value-of select="CODIGOCP"/><xsl:text>;</xsl:text>
            <xsl:value-of select="NOMBRE_CERT_ES"/><xsl:text>;</xsl:text>
            <xsl:value-of select="NOMBRE_CERT_ESKERA"/><xsl:text>;</xsl:text>
            <xsl:value-of select="NIVEL"/><xsl:text>;</xsl:text>
            <xsl:value-of select="CODCENTRO"/><xsl:text>;</xsl:text>
            <xsl:value-of select="DESCENTRO"/><xsl:text>;</xsl:text>
            <xsl:value-of select="OBSERVACIONES"/><xsl:text>;</xsl:text>
            <xsl:value-of select="PROVINCIAINTERESADO"/><xsl:text>;</xsl:text>
            <!--<xsl:value-of select="FECHAEXPEDIENTE"/><xsl:text>;</xsl:text>-->
            <!--<xsl:value-of select="TRADUCCIONRD"/><xsl:text>;</xsl:text>-->
            <xsl:text>&#10;</xsl:text>
        </xsl:for-each>

    </xsl:template>


    <xsl:template name = "etiquetaNExpediente" >NUM EXPEDIENTE</xsl:template>
    <xsl:template name = "etiquetaNumExpediente" > CLAVE REGISTRAL</xsl:template>
    <xsl:template name = "etiquetaNif"> DNI/NIE </xsl:template>
    <xsl:template name = "etiquetaApellido1Int" > APELLIDO1 INTERESADO</xsl:template>
    <xsl:template name = "etiquetaApellido2Int" > APELLIDO2 INTERESADO</xsl:template>
    <xsl:template name = "etiquetaNombreInt" > NOMBRE INTERESADO</xsl:template>
    <xsl:template name = "etiquetaSexo" > SEXO</xsl:template>
    <xsl:template name = "etiquetaFechNacimiento" > FECHA NACIMIENTO</xsl:template>
    <xsl:template name = "etiquetaFechExpedicion" > FECHA EXPEDICION</xsl:template>
    <xsl:template name = "etiquetaRealDecreto" > REAL DECRETO</xsl:template>
    <xsl:template name = "etiquetaFechaRD" > FECHA RD</xsl:template>
    <xsl:template name = "etiquetaDecretoMod" > DECRETO MODIFICADO </xsl:template>
    <xsl:template name = "etiquetaFechaDecretoMod" > FECHA DECRETO MODIF</xsl:template>
    <xsl:template name = "etiquetaCodigoCP" > CODIGO CP</xsl:template>
    <xsl:template name = "etiquetaNombreCertEs" > NOMBRE CER CASTELLANO</xsl:template>
    <xsl:template name = "etiquetaNombreCertEuskera" > NOMBRE CER EUSKERA</xsl:template>
    <xsl:template name = "etiquetaNivel" > NIVEL</xsl:template>
    <xsl:template name = "etiquetaCodCentro" > COD DESTINO </xsl:template>
    <xsl:template name = "etiquetaDesCentro" > DESTINO </xsl:template>
    <xsl:template name = "etiquetaObservaciones" > OBSERVACIONES </xsl:template>
    <xsl:template name = "etiquetaProvinciaInteresado" > PROVINCIA</xsl:template>
    <!--<xsl:template name = "etiquetaFechaExpediente" >FECHA EXPEDIENTE</xsl:template>-->
    <!--<xsl:template name = "etiquetaTraduccionRD" > TRADUCCION RD</xsl:template>-->

</xsl:stylesheet>