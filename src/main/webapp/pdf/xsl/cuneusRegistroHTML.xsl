<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" />

    <xsl:template match="//CUNEUS" >
		    <xsl:call-template name="pagina"/>
    </xsl:template>

    <xsl:template name="pagina">
	    	<xsl:apply-templates select = "//CUNEUS/COPIA" />
    </xsl:template>

    <xsl:template match = "//CUNEUS/COPIA" >
		    <xsl:call-template name="dibujarCuneus"/>
	  </xsl:template>

  <xsl:template name="dibujarCuneus">   
    <html lang="ES">
    <head>      
      <style type="text/css" media="print">
          table.normal {width:65mm; text-align:center; vertical-align:middle; border:solid black 1px; margin-top:1px; 
                        padding:0px; font-size:10pt; font-family:"FontA11"; }
          table.oculta {display:none; }
          td.normal {width:100%; text-align:center; vertical-align:middle; border-bottom:solid black 1px; 
                     margin-top:1px; padding:none;}
          td.ultimo {width:100%; text-align:center; vertical-align:middle; border-bottom:solid white 1px;             
                     margin-top:1px; padding:none;}          
          p         {text-align:center; vertical-align:middle;}
          form      {display:none;} 
      </style>
      
      <style type="text/css" media="screen">
          table.normal {width:65mm; text-align:center; vertical-align:middle; border:solid black 1px; margin-top:1px; 
                        padding:0px; font-size:10pt;}
          table.oculta {width:65mm; text-align:center; vertical-align:middle; margin-top:1px; 
                        padding:0px; font-size:10pt;}             
          td.normal {width:100%; text-align:center; vertical-align:middle; border-bottom:solid black 1px; 
                     margin-top:1px; padding:none;}
          td.ultimo {width:100%; text-align:center; vertical-align:middle; border-bottom:0px;             
                     margin-top:1px; padding:none;}          
          p {text-align:center; vertical-align:middle;}
      </style>
    </head>
    <body>
		<table class="normal">
            <tr>
                <td class="normal" height="18mm" >
                    <p><xsl:value-of select="AYTO"/> - 
                    <xsl:value-of select="REGISTRO"/></p>		    		 
                </td>
            </tr>
            <tr>
                <td class="normal" height="9mm">
                    <xsl:choose>
                        <xsl:when test="TIPO_REGISTRO = 'S' and IDIOMA='1' ">
                        		SALIDA
                        </xsl:when>
                        <xsl:when test="TIPO_REGISTRO = 'S' and IDIOMA='2' ">
                        		SAIDA
                        </xsl:when>
                        <xsl:when test="TIPO_REGISTRO = 'E'">
                            ENTRADA
                        </xsl:when>
                    </xsl:choose>
                </td>
            </tr>
            <tr>
                <td class="normal" height="9mm">
                     <xsl:value-of select="CODREGISTRO"/> N <xsl:value-of select="EJERCICIO"/>/<xsl:value-of select="NUMERO"/>
                </td>
            </tr>
            <tr>
                <td class="ultimo" height="9mm">
                    <xsl:value-of select="FECHA"/>
                </td>
            </tr>
     </table>
     <table class="oculta">     
       <tr> 
         <td>  
           <form>
              <input type="button" value="Imprimir" onClick="window.print()"/>
           </form>
         </td>
       </tr>
     </table>
     </body></html>
	</xsl:template>
	
</xsl:stylesheet>
