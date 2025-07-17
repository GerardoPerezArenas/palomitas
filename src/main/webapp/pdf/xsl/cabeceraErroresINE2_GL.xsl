<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="//resumenFicheroINE">
    <xsl:call-template name="cabecera"/>
  </xsl:template>
  <!-- Plantilla de la cabecera -->
  <xsl:template name="cabecera">
    <table width="100%" border="0" cellborder="0" align="left">
      <tr>
        <td>
          <table width="100%" border="0" cellborder="0" align="left">
            <tr>
              <td class="titulos" width="60%">Concello de <xsl:value-of select="municipio"/>
              </td>
              <td width="25%" align="center">
                <xsl:value-of select="fecha"/>
              </td>
              <td width="15%" align="right">
                Página <pagenumber/>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td>
          <table width="100%" border="0" cellborder="0" align="left">
            <tr>
              <td class="titulos">
			  		<xsl:choose>
						<xsl:when test="nombreFichero!='null'">
							Nome ficheiro:&nbsp;<xsl:value-of select="nombreFichero"/>	
						</xsl:when>
					</xsl:choose>
              </td>
              </tr>
              <tr>
		      	<td width="100%" height="2mm"/>
		      </tr>                                          
              <tr>
              <td class="titulos" align="center" style="text-decoration: none;">
                <xsl:value-of select="titulo"/>
              </td>
            </tr>
          </table>
        </td>
      </tr>      
      <tr>
        <td>
          <table width="100%" border="1" align="left" class="cabecera">
            <tr>
              <td width="100%">
                <table width="100%" border="0" cellborder="0" cellpadding="0">
                  <tr>
                   <td height="6mm">
                      <table width="100%" border="0" cellpadding="0" class="cabecera">
                        <tr>
                          <td width="10%" align="center" class="etiqueta-EsquinaSuperior">NIA</td>
						  <td width="40%" align="center" class="etiqueta-Superior">Apelidos, nome</td>
                          <td width="8%" align="center" class="etiqueta-Superior">Tipo</td>
                          <td width="10%" align="center" class="etiqueta-Superior">Data</td>
                          <td width="32%" align="center" class="etiqueta-Superior">Erros</td>
                        </tr>                        
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </xsl:template>
</xsl:stylesheet>
