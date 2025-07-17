<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="//resumenFicheroINE">
    <xsl:call-template name="pagina"/>
  </xsl:template>
  <!-- Plantilla de la página -->
  <xsl:template name="pagina">
    <xsl:call-template name="cuerpo"/>
  </xsl:template>
  <!-- Plantilla del cuerpo -->
  <xsl:template name="cuerpo">
    <xsl:for-each select="//resumenFicheroINE/linea">
    	<xsl:sort select = "concat(codigoVariacion,causaVariacion)" /> 
    	    
       <table width="100%" border="0" align="left" >
            <tr>
              <td width="100%">
                <table width="100%" border="0" cellborder="0" cellpadding="0">
                  <tr>
                   <td>
                      <table width="100%" border="0" cellpadding="0">
                        <tr>
                          <td width="10%" align="center" class="etiqueta-EsquinaSuperior">
                             	<xsl:choose>
									<xsl:when test="codigoVariacion='A'">
									  	<xsl:value-of select="idAyuntamiento"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="idAyuntamientoOld"/>
									</xsl:otherwise>
								</xsl:choose>                          
                          </td>
						  <td width="40%" align="left" class="etiqueta-Superior">
                            	<xsl:choose>
									<xsl:when test="codigoVariacion='A'">
										<xsl:value-of select="particula1"/>&nbsp;<xsl:value-of select="apellido1"/>&nbsp;<xsl:value-of select="particula2"/>&nbsp;<xsl:value-of select="apellido2"/>,&nbsp;<xsl:value-of select="nombre"/>
									</xsl:when>
									<xsl:otherwise>
									  	<xsl:value-of select="particula1Old"/>&nbsp;<xsl:value-of select="apellido1Old"/>&nbsp;<xsl:value-of select="particula2Old"/>&nbsp;<xsl:value-of select="apellido2Old"/>,&nbsp;<xsl:value-of select="nombreOld"/>
									</xsl:otherwise>
								</xsl:choose>                            						  
						  </td>
                          <td width="8%" align="center" class="etiqueta-Superior">
					            <xsl:value-of select="codigoVariacion"/>(<xsl:value-of select="causaVariacion"/>)                          
                          </td>
                          <td width="10%" align="center" class="etiqueta-Superior">
                          	<xsl:value-of select="fechaVariacion"/>
                          </td>
                          <td width="32%" align="left" class="etiqueta-Superior">
							    &nbsp;<xsl:call-template name="errores"/>                          
                          </td>
                        </tr>
                      </table>
                    </td>                    
                  </tr>
                </table>
              </td>
            </tr>
          </table>
    </xsl:for-each>
  </xsl:template>
  
  <!-- Plantilla de errores -->
  <xsl:template name="errores">
    <xsl:for-each select="errores/error">
           <xsl:value-of select="codigo"/>    
     		<xsl:if test = "not(position()=last())" > 
            	<xsl:text >, </xsl:text> 
             </xsl:if> 
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
