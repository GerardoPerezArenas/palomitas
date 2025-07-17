<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="//resumenFusionDivision">
    <xsl:call-template name="cabecera"/>
  </xsl:template>
  <!-- Plantilla de la cabecera -->
  <xsl:template name="cabecera">
    <table width="100%" border="0" cellborder="0" align="left">
      <tr>
        <td>
          <table width="100%" border="0" cellborder="0" align="left">
            <tr>
              <td class="titulos" width="50%">Descripción : <xsl:value-of select="descripcion"/>
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
          <table width="100%" border="0" cellpadding="0" class="cabecera">
            <tr>
              <td width="25%" align="center" class="etiqueta-EsquinaSuperior">Código vía</td>
              <td width="75%" align="center" class="etiqueta-Superior">Nombre vía</td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </xsl:template>
</xsl:stylesheet>
