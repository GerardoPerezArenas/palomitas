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
              <td class="titulos" width="50%">Ayuntamiento de <xsl:value-of select="municipio"/>
              </td>
              <td class="titulos" width="25%" align="center">
                <xsl:value-of select="fecha"/>
              </td>
              <td width="25%" align="right">
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
							Nombre del fichero: <xsl:value-of select="nombreFichero"/>						  	 
						</xsl:when>
					</xsl:choose>


              </td>
              <td class="titulos" align="center" style="text-decoration: underline;">
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
              <td width="25%">
                <table width="100%" border="0" cellborder="0" cellpadding="0">
                  <tr>
                    <td height="6mm">
                      <table width="100%" border="0" cellpadding="0" class="cabecera">
                        <tr>
                          <td width="75%" align="center" class="etiqueta-EsquinaSuperior">NOMB</td>
                          <td width="25%" align="center" class="etiqueta-Superior">FNAC</td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td height="6mm">
                      <table width="100%" border="0" cellpadding="0" class="cabecera">
                        <tr>
                          <td width="10%" align="center" class="etiqueta-EsquinaSuperior">PART1</td>
                          <td width="40%" align="center" class="etiqueta-Superior">APE1</td>
                          <td width="10%" align="center" class="etiqueta-Superior">PART2</td>
                          <td width="40%" align="center" class="etiqueta-Superior">APE2</td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td height="6mm">
                      <table width="100%" border="0" cellpadding="0" class="cabecera">
                        <tr>
                          <td width="16%" align="center" class="etiqueta-EsquinaSuperior">TIDEN</td>
                          <td width="14%" align="center" class="etiqueta-Superior">LEXTR</td>
                          <td width="20%" align="center" class="etiqueta-Superior">IDEN</td>
                          <td width="14%" align="center" class="etiqueta-Superior">LIDEN</td>
                          <td width="20%" align="center" class="etiqueta-Superior">NDOCU</td>
                          <td width="16%" align="center" class="etiqueta-Superior">NIA</td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                </table>
              </td>
              <td width="75%">
                <table width="100%" border="0" cellborder="0" cellpadding="0">
                  <tr>
                    <td height="6mm">
                      <table width="100%" border="0" cellpadding="0" class="cabecera">
                        <tr>
                          <td width="15%" align="center" class="etiqueta-EsquinaSuperior">NOMB</td>
                          <td width="5%" align="center" class="etiqueta-Superior">PART1</td>
                          <td width="15%" align="center" class="etiqueta-Superior">APE1</td>
                          <td width="5%" align="center" class="etiqueta-Superior">PART2</td>
                          <td width="15%" align="center" class="etiqueta-Superior">APE2</td>
                          <td width="5%" align="center" class="etiqueta-Superior">SEXO</td>
                          <td width="5%" align="center" class="etiqueta-Superior">CPRON</td>
                          <td width="5%" align="center" class="etiqueta-Superior">CMUNN</td>
                          <td width="10%" align="center" class="etiqueta-Superior">FNAC</td>
                          <td width="5%" align="center" class="etiqueta-Superior">CNES</td>
                          <td width="5%" align="center" class="etiqueta-Superior">NACI</td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td height="6mm">
                      <table width="100%" border="0" cellpadding="0" class="cabecera">
                        <tr>
                          <td width="8%" align="center" class="etiqueta-EsquinaSuperior">DIST</td>
                          <td width="8%" align="center" class="etiqueta-Superior">SECC</td>
                          <td width="8%" align="center" class="etiqueta-Superior">LSECC</td>
                          <td width="8%" align="center" class="etiqueta-Superior">CUN</td>
                          <td width="8%" align="center" class="etiqueta-Superior">NIA</td>
                          <td width="8%" align="center" class="etiqueta-Superior">NIE</td>
                          <td width="8%" align="center" class="etiqueta-Superior">NHOP</td>
                          <td width="8%" align="center" class="etiqueta-Superior">TIDEN</td>
                          <td width="8%" align="center" class="etiqueta-Superior">LEXTR</td>
                          <td width="8%" align="center" class="etiqueta-Superior">IDEN</td>
                          <td width="8%" align="center" class="etiqueta-Superior">LIDEN</td>
                          <td width="12%" align="center" class="etiqueta-Superior">NDOCU</td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td height="6mm">
                      <table width="100%" border="0" cellpadding="0" class="cabecera">
                        <tr>
                          <td width="5%" align="center" class="etiqueta-EsquinaSuperior">CVIA</td>
                          <td width="5%" align="center" class="etiqueta-Superior">TVIA</td>
                          <td width="15%" align="center" class="etiqueta-Superior">NVIAC</td>
                          <td width="5%" align="center" class="etiqueta-Superior">NUMD</td>
                          <td width="5%" align="center" class="etiqueta-Superior">LETD</td>
                          <td width="5%" align="center" class="etiqueta-Superior">NUMH</td>
                          <td width="5%" align="center" class="etiqueta-Superior">LETH</td>
                          <td width="5%" align="center" class="etiqueta-Superior">KMT</td>
                          <td width="5%" align="center" class="etiqueta-Superior">HMT</td>
                          <td width="5%" align="center" class="etiqueta-Superior">BLOQ</td>
                          <td width="5%" align="center" class="etiqueta-Superior">PORT</td>
                          <td width="5%" align="center" class="etiqueta-Superior">ESCA</td>
                          <td width="5%" align="center" class="etiqueta-Superior">PLAN</td>
                          <td width="5%" align="center" class="etiqueta-Superior">PUER</td>
                          <td width="5%" align="center" class="etiqueta-Superior">TLOC</td>
                          <td width="5%" align="center" class="etiqueta-Superior">PRDP</td>
                          <td width="5%" align="center" class="etiqueta-Superior">MUDP</td>
                          <td width="5%" align="center" class="etiqueta-Superior">CODP</td>
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
