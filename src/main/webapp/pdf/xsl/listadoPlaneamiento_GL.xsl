<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/LISTADO">

    	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="1mm">  	   
		   <thead>
    			<tr>
    				<td width="275mm" class="titulo">CONSULTA DE REXISTROS DE INSTRUMENTOS DE PLANEAMENTO</td>
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
                    <xsl:if test="//REGISTRO/SUBSECCION">
                        <td width="59mm" class="cabecera">Subsección</td>
                    </xsl:if>
                    <xsl:if test="//REGISTRO/TIPO">
                        <td width="59mm" class="cabecera">Tipo</td>
                    </xsl:if>
                    <xsl:choose>
                        <xsl:when test="//REGISTRO/AMBITO">
                            <td width="59mm" class="cabecera">Ámbito</td>
                        </xsl:when>
                        <xsl:when test="//REGISTRO/PARCELA">
                            <td width="59mm" class="cabecera">Parcela</td>
                        </xsl:when>
                        <xsl:otherwise></xsl:otherwise>
                    </xsl:choose>
                    <xsl:if test="//REGISTRO/PROCEDIMIENTO">
                        <td width="58mm" class="cabecera" style="border-right:solid black 1px;">Procedemento</td>
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
                     <xsl:if test="SUBSECCION">
                         <td width="59mm" class="fila" >
                             <xsl:value-of select="SUBSECCION"/>
                         </td>
                     </xsl:if>
                     <xsl:if test="TIPO">
                         <td width="59mm" class="fila" >
                             <xsl:value-of select="TIPO"/>
                         </td>
                     </xsl:if>
                     <xsl:choose>
                         <xsl:when test="AMBITO">
                             <td width="59mm" class="fila" >
                                 <xsl:value-of select="AMBITO"/><br/><xsl:value-of select="PARCELA"/>
                             </td>
                         </xsl:when>
                         <xsl:when test="PARCELA">
                             <td width="59mm" class="fila" >
                                 <xsl:value-of select="PARCELA"/>
                             </td>
                         </xsl:when>
                         <xsl:otherwise></xsl:otherwise>
                     </xsl:choose>
                     <xsl:if test="PROCEDIMIENTO">
                         <td width="58mm" class="fila" >
                             <xsl:value-of select="PROCEDIMIENTO"/>
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
	   			<xsl:apply-templates select = "subseccion"/>
                <xsl:apply-templates select = "tipo"/>
                <xsl:apply-templates select = "procedimiento"/>
                <xsl:apply-templates select = "ambito"/>
                <xsl:apply-templates select = "parcela"/>
                <xsl:apply-templates select = "promotor"/>
                <xsl:apply-templates select = "organoAprobacion"/>
                <xsl:apply-templates select = "fechaAprobacion"/>
                <xsl:apply-templates select = "fechaVigencia"/>
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
  
	<xsl:template match = "subseccion" >
		<xsl:if test="( . != '')">
	    	<tr>
		    	<td width="275mm" class="etiqueta" >
	    		<table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">  	   	
    			<tr>
				<td width="28mm" class="etiqueta" >Subsección: </td>
				<td width="247mm" class="valor" ><xsl:value-of select="."/></td>
    			</tr>
				</table>	    	
				</td>
	    	</tr>
	    </xsl:if>
	</xsl:template> 
	
    <xsl:template match = "tipo" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Tipo: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "ambito" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Ámbito: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "parcela" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Parcela: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "procedimiento" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Procedemento: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "promotor" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Promotor: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

    <xsl:template match = "organoAprobacion" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Órgano de Aprobación: </td>
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

    <xsl:template match = "fechaVigencia" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Data de Vixencia: </td>
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





