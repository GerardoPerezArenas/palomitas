<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="//estadisticasOperaciones">
    <xsl:call-template name="cabecera"/>
  </xsl:template>
  <!-- Plantilla de la cabecera -->
  <xsl:template name="cabecera">
    <table width="100%" border="0" cellborder="0" align="left">
      <tr>
        <td>
          <table width="100%" border="0" cellborder="0" align="left">
            <tr>
              <td class="titulos" width="65%">Concello de <xsl:value-of select="municipio"/>
              </td>
              <td width="25%" align="right">
                <xsl:value-of select="fecha"/>
              </td>
              <td width="10%" align="right">
                Páxina <pagenumber/>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td>
          <table width="100%" border="0" cellborder="0" align="left">
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
						  <td width="7%" align="center" class="etiqueta-EsquinaSuperior-cabecera"></td>
                          <td width="35%" align="center" class="etiqueta-Superior-cabecera">ALTAS</td>
						  <td width="44%" align="center" class="etiqueta-Superior-cabecera">BAIXAS</td>
                          <td width="14%" align="center" class="etiqueta-Superior-cabecera">Nº Empadronados</td>
                        </tr>                        
                      </table>
                    </td>
                  </tr>
                  <tr>
                   <td height="6mm">
                      <table width="100%" border="0" cellpadding="0" class="cabecera">
                        <tr>
						  <td width="7%" align="center" class="etiqueta-EsquinaSuperior-cabecera">Ano</td>
                          <td width="9%" align="center" class="etiqueta-Superior-cabecera">Omisión</td>
						  <td width="9%" align="center" class="etiqueta-Superior-cabecera">Nacemento</td>
                          <td width="9%" align="center" class="etiqueta-Superior-cabecera">Cambio<br/>Residencia</td>
                          <td width="8%" align="center" class="etiqueta-Superior-cabecera">Total</td>
                          <td width="9%" align="center" class="etiqueta-Superior-cabecera">Defunción</td>
                          <td width="9%" align="center" class="etiqueta-Superior-cabecera">Cambio<br/> Residencia</td>
						  <td width="9%" align="center" class="etiqueta-Superior-cabecera">Inscripción<br/> Indebida</td>
                          <td width="9%" align="center" class="etiqueta-Superior-cabecera">Duplicidade</td>
                          <td width="8%" align="center" class="etiqueta-Superior-cabecera">Total</td>
                          <td width="14%" align="center" class="etiqueta-Superior-cabecera">Total</td>                          
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
