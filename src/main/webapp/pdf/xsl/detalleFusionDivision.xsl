<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="//resumenFusionDivision">
    <xsl:call-template name="pagina"/>
  </xsl:template>
  <!-- Plantilla de la página -->
  <xsl:template name="pagina">
    <xsl:call-template name="cuerpo"/>
  </xsl:template>
  <!-- Plantilla del cuerpo -->
  <xsl:template name="cuerpo">
    <xsl:for-each select="//resumenFusionDivision/via">
      <table width="100%" border="0" cellpadding="0" align="left" class="linea">
        <tr>
          <td width="25%" align="center" class="etiqueta-EsquinaSuperior">
            <xsl:value-of select="codVia"/>
          </td>
          <td width="75%" align="center" class="etiqueta-Superior">
            <xsl:value-of select="nombreVia"/>
          </td>
        </tr>
      </table>
      <xsl:call-template name="numeraciones"/>
      <table width="100%" border="0" cellborder="0" align="left" class="linea">
        <tr>
          <td width="100%" height="1px"/>
        </tr>
      </table>
    </xsl:for-each>
  </xsl:template>
  <!-- Plantilla de numeraciones -->
  <xsl:template name="numeraciones">
    <xsl:for-each select="numeraciones/numeracion">
      <table width="85%" border="0" cellpadding="0" class="error" align="right">
        <tr>
          <td width="100%" align="left" class="etiqueta-EsquinaSuperior">
            <xsl:value-of select="tipoNumeracion"/>
          </td>
        </tr>
      </table>
      <xsl:call-template name="tramos"/>
    </xsl:for-each>
  </xsl:template>
  <!-- Plantilla de tramos -->
  <xsl:template name="tramos">
    <xsl:for-each select="tramo">
      <table width="75%" border="0" cellpadding="0" class="error" align="right">
        <tr>
          <td width="100%" align="center" class="etiqueta-EsquinaSuperior">
            <xsl:value-of select="descripcion"/>
          </td>
        </tr>
      </table>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
