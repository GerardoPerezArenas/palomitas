<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="//resumenFicheroINE">
    <xsl:call-template name="pagina"/>
  </xsl:template>
  
  <!-- Plantilla de la página -->
  <xsl:template name="pagina">
    <xsl:call-template name="cabecera"/>
    <xsl:call-template name="cuerpo"/>
  </xsl:template>
  
  <!-- Plantilla de la cabecera -->
  <xsl:template name="cabecera">
    <table border="0">
      <tr>
        <td height="40mm"></td>
      </tr>
    </table>
    <xsl:choose>
      <xsl:when test="tipo=1 or tipo=2">
        <table border="0" align="left">
          <tr>
            <td class="titulos">
              <xsl:value-of select="titulo"/>
            </td>
          </tr>
          <tr>
            <td>
              <table border="0" align="left">
                <tr>
                  <td class="titulos" width="110mm">HASTA <xsl:value-of select="hasta"/>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </xsl:when>
      <xsl:when test="tipo=3">
        <table border="0" align="left">
          <tr>
            <td class="titulos">
              <xsl:value-of select="titulo"/>
              <xsl:value-of select="hasta"/>
            </td>
          </tr>
        </table>
      </xsl:when>
      <xsl:when test="tipo=4">
        <table border="0" align="left">
          <tr>
            <td class="titulos">
              <xsl:value-of select="titulo"/>
              <xsl:value-of select="hasta"/>
            </td>
          </tr>
        </table>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  
  <!-- Plantilla del cuerpo -->
  <xsl:template name="cuerpo">
    <table border="0" align="left">
      <tr>
        <td height="10px"></td>
      </tr>
      <tr>
        <td>
          <table border="0">
            <tr>
              <td class="etiqueta">Provincia: <xsl:value-of select="provincia"/></td>
            </tr>
            <tr>
              <td class="etiqueta">Municipio: <xsl:value-of select="municipio"/></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="10px"></td>
      </tr>
      <tr>
        <td class="etiqueta">Nombre del fichero: <xsl:value-of select="nombreFichero"/></td>
      </tr>
      <tr>
        <td height="10px"></td>
      </tr>
      <tr>
        <td class="etiqueta">Total de registros: <xsl:value-of select="numeroRegistros"/></td>
      </tr>
      <tr>
        <td>
          <xsl:if test="tipo!=4">
            <blockquote>
            <table border="0">
              <tr>
                <td class="etiqueta">Altas: <xsl:value-of select="numeroAltas"/></td>
              </tr>
              <tr>
                <td class="etiqueta">Bajas: <xsl:value-of select="numeroBajas"/></td>
              </tr>
              <tr>
                <td class="etiqueta">Modificaciones: <xsl:value-of select="numeroModificaciones"/></td>
              </tr>
              <tr>
                <td class="etiqueta">Rechazos: <xsl:value-of select="numeroRegistrosRechazados"/></td>
              </tr>
            </table>
            </blockquote>
          </xsl:if>
        </td>
      </tr>
      <tr>
        <td height="10px"></td>
      </tr>
      <tr>
        <td class="etiqueta">A <xsl:value-of select="fecha"/></td>
      </tr>
    </table>
  </xsl:template>
</xsl:stylesheet>
