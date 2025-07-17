<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/LISTADO">

    	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="1mm">  	   
		   <thead>
    			<tr>
    				<td width="275mm" class="titulo">CONSULTA DE REGISTROS DE CONVENIOS URBANÍSTICOS</td>
    			</tr>
  			</thead>
			</table>

			<xsl:apply-templates select = "CRITERIOS" />

       <table width="275mm"  border="0" valign="top" align="center" cellpadding="3mm">
           <thead>
                <tr>
                    <td width="20mm" class="cabecera" style="border-left:solid black 1px;">Núm. registro</td>
                    <xsl:if test="//REGISTRO/FECHA_APROBACION">
                        <td width="20mm" class="cabecera">Fecha Aprob.</td>
                    </xsl:if>
                    <xsl:if test="//REGISTRO/SUBSECCION">
                        <td width="55mm" class="cabecera">Subsección</td>
                    </xsl:if>
                    <xsl:if test="//REGISTRO/OBJETO_CONVENIO">
                        <td width="125mm" class="cabecera">Objeto del convenio</td>
                    </xsl:if>
                    <xsl:choose>
                        <xsl:when test="//REGISTRO/AMBITO">
                            <td width="55mm" class="cabecera" style="border-right:solid black 1px;">Ámbito</td>
                        </xsl:when>
                        <xsl:when test="//REGISTRO/PARCELA">
                            <td width="55mm" class="cabecera" style="border-right:solid black 1px;">Parcela</td>
                        </xsl:when>
                        <xsl:otherwise></xsl:otherwise>
                    </xsl:choose>
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
                         <td width="55mm" class="fila" >
                             <xsl:value-of select="SUBSECCION"/>
                         </td>
                     </xsl:if>
                     <xsl:if test="OBJETO_CONVENIO">
                         <td width="125mm" class="fila" >
                             <xsl:value-of select="OBJETO_CONVENIO"/>
                         </td>
                     </xsl:if>
                     <xsl:choose>
                         <xsl:when test="AMBITO">
                             <td width="55mm" class="fila" >
                                 <xsl:value-of select="AMBITO"/><br/><xsl:value-of select="PARCELA"/>
                             </td>
                         </xsl:when>
                         <xsl:when test="PARCELA">
                             <td width="55mm" class="fila" >
                                 <xsl:value-of select="PARCELA"/>
                             </td>
                         </xsl:when>
                         <xsl:otherwise></xsl:otherwise>
                     </xsl:choose>
                 </tr>
                   </xsl:for-each>
               </tbody>
       </table>

	</xsl:template>
		
    
    <xsl:template match = "CRITERIOS" > 
    	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="1mm">  	   
  			<tbody>
	   			<xsl:apply-templates select = "subseccion"/>
                <xsl:apply-templates select = "objetoConvenio"/>
                <xsl:apply-templates select = "ambito"/>
                <xsl:apply-templates select = "parcela"/>
                <xsl:apply-templates select = "firmante"/>
                <xsl:apply-templates select = "organoAprobacion"/>
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
	
    <xsl:template match = "objetoConvenio" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Objeto Convenio: </td>
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

    <xsl:template match = "firmante" >
        <xsl:if test="( . != '')">
            <tr>
                <td width="275mm" class="etiqueta" >
                <table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">
                <tr>
                <td width="28mm" class="etiqueta" >Firmante: </td>
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
                <td width="28mm" class="etiqueta" >Fecha de Aprobación: </td>
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
                <td width="28mm" class="etiqueta" >Fecha de Baja: </td>
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
                <td width="28mm" class="etiqueta" >Fecha de Publicación: </td>
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
                <td width="28mm" class="etiqueta" >Observaciones: </td>
                <td width="247mm" class="valor" ><xsl:value-of select="."/></td>
                </tr>
                </table>
                </td>
            </tr>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>





