<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/LISTADO">

    	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="1mm">  	   
		   <thead>
    			<tr>
    				<td width="275mm" class="titulo">CONSULTA DE REXISTROS DE BENS E ESPAZOS CATALOGADOS</td>
    			</tr>
  			</thead>
			</table>

			<xsl:apply-templates select = "CRITERIOS" />

       <table width="275mm"  border="0" valign="top" align="center" cellpadding="3mm">
           <thead>
                <tr>
                    <td width="20mm" class="cabecera" style="border-left:solid black 1px;">Núm. rexistro</td>
                    <xsl:if test="//REGISTRO/FECHA_APROBACION">
                        <td width="20mm" class="cabecera">Data Aprob.</td>
                    </xsl:if>
                    <xsl:if test="//REGISTRO/DENOMINACION_BIEN">
                        <td width="180mm" class="cabecera">Denominación do Ben</td>
                    </xsl:if>
                    <xsl:if test="//REGISTRO/CATALOGACION">
                        <td width="55mm" class="cabecera" style="border-right:solid black 1px;">Catalogación</td>
                    </xsl:if>
                </tr>
              </thead>
               <tbody>
                   <xsl:for-each select="REGISTRO">
                 <tr >
                    <td width="20mm" class="fila" >
                        <xsl:value-of select="NUMERO"/>
                    </td>
                     <xsl:if test="FECHA_APROBACION">
                         <td width="20mm" class="fila" >
                             <xsl:value-of select="FECHA_APROBACION"/>
                         </td>
                     </xsl:if>
                     <xsl:if test="DENOMINACION_BIEN">
                         <td width="180mm" class="fila" >
                             <xsl:value-of select="DENOMINACION_BIEN"/>
                         </td>
                     </xsl:if>
                     <xsl:if test="CATALOGACION">
                         <td width="55mm" class="fila" >
                             <xsl:value-of select="CATALOGACION"/>
                         </td>
                     </xsl:if>
                 </tr>
                   </xsl:for-each>
               </tbody>
       </table>

	</xsl:template>
		
    
    <xsl:template match = "CRITERIOS" > 
    	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="1mm">  	   
  			<tbody>
	   			<xsl:apply-templates select = "denominacionBien"/>
                <xsl:apply-templates select = "domicilio"/>
                <xsl:apply-templates select = "catalogacion"/>
                <xsl:apply-templates select = "gradoProteccion"/>
                <xsl:apply-templates select = "relacionBien"/>
                <xsl:apply-templates select = "fechaAprobacion"/>
                <xsl:apply-templates select = "fechaBaja"/>
                <xsl:apply-templates select = "fechaPublicacion"/>
                <xsl:apply-templates select = "numeroPublicacion"/>
                <xsl:apply-templates select = "observaciones"/>
	   		<tr>
				<td width="275mm" class="etiqueta" ></td>
	    	</tr>	   			
  			</tbody>  		  			
  			
  		</table>
    
  </xsl:template> 
  
	<xsl:template match = "denominacionBien" >
		<xsl:if test="( . != '')">
	    	<tr>
		    	<td width="275mm" class="etiqueta" >
	    		<table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">  	   	
    			<tr>
				<td width="28mm" class="etiqueta" >Denominación do Ben: </td>
				<td width="247mm" class="valor" ><xsl:value-of select="."/></td>
    			</tr>
				</table>	    	
				</td>
	    	</tr>
	    </xsl:if>
	</xsl:template> 
	
    <xsl:template match = "domicilio" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Domicilio: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "catalogacion" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Catalogación: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "gradoProteccion" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Grado de Protección: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "relacionBien" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Relación do Ben: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "fechaAprobacion" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Data de Aprobación: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "fechaBaja" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Data de Baixa: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "fechaPublicacion" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Data de Publicación: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "numeroPublicacion" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Número de Publicación: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "observaciones" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Observacións: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>





