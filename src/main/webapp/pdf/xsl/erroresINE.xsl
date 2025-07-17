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
      <table width="100%" border="0" cellborder="0" align="left" class="linea">
        <tr>
          <td>
            <table width="100%" border="1" cellborder="1" align="left" class="linea">
              <tr>
                <td width="25%">
                  <table width="100%" cellborder="0" cellpadding="0">
                    <tr>
                      <td height="6mm">
                        <table width="100%" border="0" cellpadding="0">
                          <tr>
                            <td width="75%" align="center" class="etiqueta-EsquinaSuperior">
                              <xsl:value-of select="nombreOld"/>
                            </td>
                            <td width="25%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="fechaNacimientoOld"/>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td height="6mm">
                        <table width="100%" border="0" cellpadding="0">
                          <tr>
                            <td width="10%" align="center" class="etiqueta-EsquinaSuperior">
                              <xsl:value-of select="particula1Old"/>
                            </td>
                            <td width="40%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="apellido1Old"/>
                            </td>
                            <td width="10%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="particula2Old"/>
                            </td>
                            <td width="40%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="apellido2Old"/>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td height="6mm">
                        <table width="100%" border="0" cellpadding="0">
                          <tr>
                            <td width="16%" align="center" class="etiqueta-EsquinaSuperior">
                              <xsl:value-of select="tipoDocumentoOld"/>
                            </td>
                            <td width="14%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="letraExtranjerosOld"/>
                            </td>
                            <td width="20%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="documentoOld"/>
                            </td>
                            <td width="14%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="letraDocumentoOld"/>
                            </td>
                            <td width="20%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="documentoExtranjerosOld"/>
                            </td>
                            <td width="16%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="idAyuntamientoOld"/>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
                <td width="75%">
                  <table width="100%" border="0" cellpadding="0">
                    <tr>
                      <td height="6mm">
                        <table width="100%" border="0" cellpadding="0">
                          <tr>
                            <td width="15%" align="center" class="etiqueta-EsquinaSuperior">
                              <xsl:value-of select="nombre"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="particula1"/>
                            </td>
                            <td width="15%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="apellido1"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="particula2"/>
                            </td>
                            <td width="15%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="apellido2"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="sexo"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="codProvinciaNacimiento"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="codMunicipioNacimiento"/>
                            </td>
                            <td width="10%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="fechaNacimiento"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="codTitulacion"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="codPaisNacionalidad"/>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td height="6mm">
                        <table width="100%" border="0" cellpadding="0">
                          <tr>
                            <td width="8%" align="center" class="etiqueta-EsquinaSuperior">
                              <xsl:value-of select="distrito"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="seccion"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="letraSeccion"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="codigoPoblacional"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="idAyuntamiento"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="idElectoral"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="numHojaPadronal"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="tipoDocumento"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="letraExtranjeros"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="documento"/>
                            </td>
                            <td width="8%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="letraDocumento"/>
                            </td>
                            <td width="12%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="documentoExtranjeros"/>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr>
                      <td height="6mm">
                        <table width="100%" border="0" cellpadding="0">
                          <tr>
                            <td width="5%" align="center" class="etiqueta-EsquinaSuperior">
                              <xsl:value-of select="codigoVia"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="tipoVia"/>
                            </td>
                            <td width="15%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="nombreVia"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="numDesde"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="letraDesde"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="numHasta"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="letraHasta"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="kilometro"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="hectometro"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="bloque"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="portal"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="escalera"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="planta"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="puerta"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="tipoLocal"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="codProvinciaDestinoProc"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="codMunicipioDestinoProc"/>
                            </td>
                            <td width="5%" align="center" class="etiqueta-Superior">
                              <xsl:value-of select="codConsuladoDestinoProc"/>
                            </td>
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
      <table width="100%" border="0" align="left" class="linea">
        <tr>
          <td align="right">
            <table width="75%" border="0">
              <tr>
                <td>
                  <table width="100%" border="0" cellpadding="0" class="cabecera">
                    <tr>
                      <td width="10%" align="center" class="etiqueta-EsquinaSuperior">TINF</td>
                      <td width="10%" align="center" class="etiqueta-Superior">CDEV</td>
                      <td width="15%" align="center" class="etiqueta-Superior">FVAR</td>
                      <td width="10%" align="center" class="etiqueta-Superior">CVAR</td>
                      <td width="10%" align="center" class="etiqueta-Superior">CAUV</td>
                      <td width="10%" align="center" class="etiqueta-Superior">CODE</td>
                      <td width="35%" align="center" class="etiqueta-Superior">DESCRIPCION ERROR</td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <xsl:call-template name="errores"/>
      <table width="100%" border="0" cellborder="0" align="left" class="linea">
        <tr>
          <td width="100%" height="1px"/>
        </tr>
      </table>
    </xsl:for-each>
  </xsl:template>
  <!-- Plantilla de errores -->
  <xsl:template name="errores">
    <xsl:for-each select="errores/error">
      <table width="75%" border="0" cellpadding="0" class="error" align="right">
        <tr>
          <td width="10%" align="center" class="etiqueta-EsquinaSuperior">
            <xsl:value-of select="tipoInformacion"/>
          </td>
          <td width="10%" align="center" class="etiqueta-Superior">
            <xsl:value-of select="causaDevolucion"/>
          </td>
          <td width="15%" align="center" class="etiqueta-Superior">
            <xsl:value-of select="fechaVariacion"/>
          </td>
          <td width="10%" align="center" class="etiqueta-Superior">
            <xsl:value-of select="codigoVariacion"/>
          </td>
          <td width="10%" align="center" class="etiqueta-Superior">
            <xsl:value-of select="causaVariacion"/>
          </td>
          <td width="10%" align="center" class="etiqueta-Superior">
            <xsl:value-of select="codigo"/>
          </td>
          <td width="35%" align="center" class="etiqueta-Superior">
            <xsl:value-of select="descripcion"/>
          </td>
        </tr>
      </table>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
