<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="//estadisticasOperaciones">
    <xsl:call-template name="pagina"/>
  </xsl:template>
  <!-- Plantilla de la página -->
  <xsl:template name="pagina">
    <xsl:call-template name="cuerpo"/>
  </xsl:template>
  <!-- Plantilla del cuerpo -->
  <xsl:template name="cuerpo">
    <xsl:for-each select="//estadisticasOperaciones/linea">
    	<xsl:sort select = "ano" /> 
    	    
       <table width="100%" border="0" align="left" >
            <tr>
              <td width="100%">
                <table width="100%" border="0" cellborder="0" cellpadding="0">
                  <tr>
                   <td>
                      <table width="100%" border="0" cellpadding="0">
                        <tr>
                          <td width="7%" align="center" class="etiqueta-EsquinaSuperior">
	                          <xsl:value-of select="ano"/>
                          </td>
                          <td width="9%" align="center" class="etiqueta-Superior">
	                          <xsl:value-of select="AOM"/>
                          </td>
                          <td width="9%" align="center" class="etiqueta-Superior">
	                          <xsl:value-of select="ANA"/>
                          </td>
                          <td width="9%" align="center" class="etiqueta-Superior">
	                          <xsl:value-of select="ACR"/>
                          </td>
                          <td width="8%" align="center" class="etiqueta-Superior">
							<xsl:value-of select="ACR+ANA+AOM"/>
                          </td>
                          <td width="9%" align="center" class="etiqueta-Superior">
	                          <xsl:value-of select="BDE"/>
                          </td>
                          <td width="9%" align="center" class="etiqueta-Superior">
	                          <xsl:value-of select="BCR"/>
                          </td>
                          <td width="9%" align="center" class="etiqueta-Superior">
	                          <xsl:value-of select="BII"/>
                          </td>
                          <td width="9%" align="center" class="etiqueta-Superior">
	                          <xsl:value-of select="BDU"/>
                          </td>
                          <td width="8%" align="center" class="etiqueta-Superior">
							<xsl:value-of select="BCR+BDE+BII+BDU"/>
                          </td>                          
                          <td width="14%" align="center" class="etiqueta-Superior">
							<xsl:value-of select="numeroEmpadronados"/>
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
  
</xsl:stylesheet>
