<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="//procedimiento" > 
        <xsl:call-template name="pagina"/>
    </xsl:template> 
    
    <xsl:template name="pagina">
        
        <table border="0" align="center" width="200mm"  cellpadding="0mm">
            <tr><td height="3mm"></td></tr>
            <tr>
                <td>
                    <table border="0" align="center" width="180mm" height="2mm" cellpadding="0px">
                        <tr>
                            <td width="100%" class="cabecera" align="center"> EXPEDIENTE: <xsl:value-of select="numero"/></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr><td height="8mm"></td></tr>
        </table>
        <table border="0" align="center" width="200mm"  cellpadding="0mm">
            <tr><td width="100%" height="3mm"></td></tr>
        </table>
        <table border="0" align="center" width="200mm"  cellpadding="2mm">
            <tr>
                <td width="180mm" class="titulo1" align="left">DATOS GENERALES</td>
            </tr>           
        </table>
        <table border="0px" align="center" width="200mm"  cellpadding="1mm">
            <tr>
                <td width="200mm" colspan="3" height="2mm"></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Procedimiento:</td>
                <td width="150mm" align="left"><xsl:value-of select="procedimiento"/></td>
            </tr>
            <tr>
                <td width="200mm" colspan="3" height="2mm"></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Interesado:</td>
                <td width="150mm" align="left"><xsl:value-of select="interesado"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Documento:</td>
                <td width="150mm" align="left"><xsl:value-of select="documento"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Localización:</td>
                <td width="150mm" align="left"><xsl:value-of select="localizacion"/></td>
            </tr>
            <xsl:if test="ubicacionDoc">
                <tr>
                    <td width="15mm" class="etiqueta" align="left"></td>
                    <td width="35mm" class="etiqueta" align="left">Ubicación Doc.:</td>
                    <td width="150mm" align="left"><xsl:value-of select="ubicacionDoc"/></td>
                </tr>
            </xsl:if>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Fecha inicio:</td>
                <td width="150mm" align="left"><xsl:value-of select="fechaInicio"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Fecha fin:</td>
                <td width="150mm" align="left"><xsl:value-of select="fechaFin"/></td>
            </tr>
             <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Usuario:</td>
                <td width="150mm" align="left"><xsl:value-of select="usuario"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Unidad inicio:</td>
                <td width="150mm" align="left"><xsl:value-of select="unidadInicio"/></td>
            </tr>
          
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Asunto:</td>
                <td width="150mm" align="left"><xsl:apply-templates select = "asunto" /></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Observaciones:</td>
                <td width="150mm" align="left"><xsl:apply-templates select = "observaciones" /></td>
            </tr>
            <tr>
                <td width="100mm" colspan="3" height="2mm"></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="45mm" class="etiqueta" align="left">Exp. relacionados:</td>
                <td width="140mm" align="left"><xsl:apply-templates select = "//procedimiento/expedientesRelacionados" /></td>
            </tr>
            <tr>
                <td width="100mm" colspan="3" height="2mm"></td>
            </tr>
          
            <tr>
                <td width="200mm" colspan="3" height="3mm"></td>
            </tr>
        </table>  
        <xsl:apply-templates select = "//procedimiento/datosExpediente" />
		<xsl:apply-templates select = "//procedimiento/agrupacion"/>
		
		  
        
        <xsl:apply-templates select = "//procedimiento/registro" />
        
        <table border="0" align="center" width="200mm"  cellpadding="2mm">
            <tr>
                <td width="180mm" class="titulo1" align="left">TRÁMITES</td>
            </tr>
            <tr><td width="100%" height="2mm"></td></tr>
        </table>        
        <xsl:apply-templates select = "//procedimiento/tramite" />
        
    </xsl:template> 
    
    
    
	
	
	
	
	<xsl:template match = "//procedimiento/agrupacion" > 
 <xsl:if test="position()=1">
            <table border="0" align="center" width="200mm"  cellpadding="2mm">
                <tr>
                    <td width="180mm" class="titulo1" align="left">DATOS DEL EXPEDIENTE</td>
                </tr>
                <tr><td width="100%" height="2mm"></td></tr>
            </table>
            
        </xsl:if>	
        <table border="0px" align="center" width="200mm"  cellpadding="1mm">
            <tr>
                <td width="200mm" colspan="3" height="2mm"></td>
            </tr>
            <tr>
                <td width="180mm" class="titulo2" align="left">
                    <xsl:value-of select="nombreAgrupacion"/>
                </td>
            </tr>
        </table>
        
        <table border="0" align="center" width="200mm"  cellpadding="0mm">
            <tr><td width="100%" height="4mm"></td></tr>
        </table>
        <table border="0px" align="center" width="200mm"  cellpadding="1mm">
            <tr>
                <td width="200mm" colspan="3" height="2mm"></td>
            </tr>
            <xsl:for-each select='datosExpediente'>
           
			   <tr>				    
                    <td width="15mm" class="etiqueta" align="left"></td>					
                    <td width="50mm"  class="etiqueta" align="left"><xsl:value-of select="nombreDE"/>:</td>
                    <td width="135mm" align="left" class="valor"><xsl:apply-templates select = "valorDE" /></td>
					
                </tr>		
            </xsl:for-each>
        </table>
		
    </xsl:template>
    
    <xsl:template match = "//procedimiento/asunto" >
        <table align="center" width="130mm" height="3mm" cellpadding="1px" border="0px">
            <tr>
                <td width="130mm"><xsl:value-of select="linea"/></td>
            </tr>
            <tr><td width="130mm" height="1mm"></td></tr>
        </table>
    </xsl:template>
    
    <xsl:template match = "//procedimiento/observaciones" >
        <table align="center" width="130mm" height="3mm" cellpadding="1px" border="0px">
            <tr>
                <td width="130mm"><xsl:value-of select="linea"/></td>
            </tr>
            <tr><td width="130mm" height="1mm"></td></tr>
        </table>
    </xsl:template>
    
    <xsl:template match = "//procedimiento/expedientesRelacionados" >
        <table align="center" width="150mm" height="3mm" cellpadding="1px" border="0px">
            <tr>
                <td width="150mm"><xsl:value-of select="expRel"/></td>
            </tr>
            <tr><td width="150mm" height="1mm"></td></tr>
        </table>
    </xsl:template>
    
    <xsl:template match = "//procedimiento/tramite" > 		
        <table border="0px" align="center" width="200mm"  cellpadding="1mm">
            <tr>
                <td width="200mm" colspan="3" height="2mm"></td>
            </tr>
            <tr>
                <td width="180mm" class="titulo2" align="left">
                    <xsl:value-of select="nombreTramite"/>
                </td>
            </tr>
        </table>
        <table border="0px" align="center" width="200mm"  cellpadding="1mm">
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Fecha Inicio:</td>
                <td width="150mm" align="left"><xsl:value-of select="fechaInicioTramite"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Fecha Fin:</td>
                <td width="150mm" align="left"><xsl:value-of select="fechaFinTramite"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Unidad:</td>
                <td width="150mm" align="left"><xsl:value-of select="unidadTramite"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Clasificación:</td>
                <td width="150mm" align="left"><xsl:value-of select="clasificacionTramite"/></td>
            </tr>
	    <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Tramitado por:</td>
                <td width="150mm" align="left"><xsl:value-of select="usuarioTramite"/></td>
            </tr>
            <!--<tr>
                <td width="200mm" colspan="3" height="3mm"></td>
            </tr>-->
        </table>
        <table border="0" align="center" width="200mm"  cellpadding="0mm">
            <tr><td width="100%" height="4mm"></td></tr>
        </table>
        <table border="0px" align="center" width="200mm"  cellpadding="1mm">
            <tr>
                <td width="200mm" colspan="3" height="2mm"></td>
            </tr>
            <xsl:for-each select='campoSuplementario'>
                <tr>
                    <td width="15mm" class="etiqueta" align="left"></td>
                    <td width="50mm" class="etiqueta" align="left"><xsl:value-of select="nombreCS"/>:</td>
                    <td width="135mm" align="left" class="valor"><xsl:apply-templates select = "valorCS" /></td>
                </tr>
            </xsl:for-each>
        </table>
        
        
    </xsl:template>
    
    <xsl:template match = "//procedimiento/registro" >
        <table border="0" align="center" width="200mm"  cellpadding="2mm">
            <tr>
                <td width="180mm" class="titulo1" align="left">REGISTRO</td>
            </tr>
            <tr><td width="100%" height="2mm"></td></tr>
        </table>
        <table border="0px" align="center" width="200mm"  cellpadding="1mm">
            <tr>
                <td width="200mm" colspan="3" height="2mm"></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Ejer./Num:</td>
                <td width="150mm" align="left"><xsl:value-of select="numRegistro"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Tipo:</td>
                <td width="150mm" align="left"><xsl:value-of select="tipoRegistro"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Fecha:</td>
                <td width="150mm" align="left"><xsl:value-of select="fechaRegistro"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Remitente:</td>
                <td width="150mm" align="left"><xsl:value-of select="remitenteRegistro"/></td>
            </tr>
            <tr>
                <td width="15mm" class="etiqueta" align="left"></td>
                <td width="35mm" class="etiqueta" align="left">Asunto:</td>
                <td width="150mm" align="left"><xsl:value-of select="asuntoRegistro"/></td>
            </tr>
            <tr>
                <td width="200mm" colspan="3" height="3mm"></td>
            </tr>
        </table>
        <table border="0" align="center" width="200mm"  cellpadding="0mm">
            <tr><td width="100%" height="4mm"></td></tr>
        </table>
    </xsl:template>
    
    <xsl:template match = "valorCS" >
        <table align="center" width="125mm" height="3mm" cellpadding="1px" border="0px">
            <tr>
                <td width="125mm" align="left" class="valor"><xsl:value-of select="linea"/></td>
            </tr>
            <tr><td width="125mm" height="1mm"></td></tr>
        </table>
    </xsl:template>
    
    
    
    <xsl:template match = "valorDE" >
        <table align="center" width="125mm" height="3mm" cellpadding="1px" border="0px">
            <tr>
                <td width="125mm"><xsl:value-of select="linea"/></td>
            </tr>
            <tr><td width="125mm" height="1mm"></td></tr>
        </table>
    </xsl:template>
    
</xsl:stylesheet>


