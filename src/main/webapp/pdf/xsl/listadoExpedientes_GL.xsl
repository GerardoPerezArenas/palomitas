<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/LISTADO_EXPEDIENTES">

    	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="1mm">  	   
		   <thead>
    			<tr>
    				<td width="275mm" class="titulo">CONSULTA DE EXPEDIENTES</td>
    			</tr>
  			</thead>
			</table>

		<xsl:if test=" ( (//CRITERIOS/PROCEDIMIENTO!= '') and (//CRITERIOS/PROCEDIMIENTO!= 'VARIOS')) 
						or (//CRITERIOS/ESTADO != '') or (//CRITERIOS/TITULAR != '') 
						or (//CRITERIOS/LOCALIZACION != '') or (//CRITERIOS/CLASIFICACION_TRAMITE != '')">
			<xsl:apply-templates select = "//CRITERIOS" /> 
		</xsl:if>      	

    	<xsl:choose>    				
			<xsl:when test="(//CRITERIOS/ESTADO = '')">
				<xsl:choose>    				
					<xsl:when test="(//CRITERIOS/PROCEDIMIENTO= '') or (//CRITERIOS/PROCEDIMIENTO= 'VARIOS')">					
						<xsl:choose>    				
							<xsl:when test="(//CRITERIOS/TITULAR= '') ">								
								<xsl:call-template name = "todas" /> 
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name = "estadoProcedimiento" /> 
							</xsl:otherwise>
						</xsl:choose>					
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>    				
							<xsl:when test="(//CRITERIOS/TITULAR= '') ">								
								<xsl:call-template name = "estadoTitular" /> 
							</xsl:when>
							<xsl:otherwise>
								<xsl:call-template name = "estado" /> 
							</xsl:otherwise>
						</xsl:choose>										
					</xsl:otherwise>
				</xsl:choose>															
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>    				
					<xsl:when test="(//CRITERIOS/PROCEDIMIENTO= '') or (//CRITERIOS/PROCEDIMIENTO= 'VARIOS')">					
						<xsl:choose>    				
							<xsl:when test="(//CRITERIOS/TITULAR= '') ">								
								<xsl:call-template name = "procedimientoTitular" /> 
							</xsl:when>
							<xsl:otherwise>
								 <xsl:call-template name = "procedimiento" /> 
							</xsl:otherwise>
						</xsl:choose>					
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>    				
							<xsl:when test="(//CRITERIOS/TITULAR= '') ">								
								<xsl:call-template name = "titular" /> 
							</xsl:when>
							<xsl:otherwise>
									<xsl:call-template name = "ninguna" /> 
							</xsl:otherwise>
						</xsl:choose>										
					</xsl:otherwise>
				</xsl:choose>																		
			</xsl:otherwise>
		</xsl:choose>					
	</xsl:template> 
		
    
    <xsl:template match = "CRITERIOS" > 
    	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="1mm">  	   
		   	<tbody>
	   			<xsl:apply-templates /> 
	   		<tr>
				<td width="275mm" class="etiqueta" ></td>
	    	</tr>	   			
  			</tbody>  		  			
  			
  		</table>
    
  </xsl:template> 
  
	<xsl:template match = "PROCEDIMIENTO" > 
		<xsl:if test="( . != '') and ( . != 'VARIOS')">					
	    	<tr>
		    	<td width="275mm" class="etiqueta" >
	    		<table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">  	   	
    			<tr>
				<td width="28mm" class="etiqueta" ><xsl:call-template name = "etiquetaProcedimiento" />: </td>
				<td width="247mm" class="valor" ><xsl:value-of select="."/></td>
    			</tr>
				</table>	    	
				</td>

	    	</tr>
	    </xsl:if>
	</xsl:template> 
	
	<xsl:template match = "ESTADO" > 
		<xsl:if test="( . != '')">
		    <tr>
		    	<td width="275mm" class="etiqueta" >
	    		<table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">  	   	
    			<tr>
				<td width="15mm" class="etiqueta" >ESTADO: </td>
				<td width="260mm" class="valor" ><xsl:value-of select="."/></td>
    			</tr>
				</table>	    	
				</td>
		    </tr>
		</xsl:if>
	</xsl:template> 

	<xsl:template match = "TITULAR" > 
		<xsl:if test="( . != '')">
		    <tr>
		    	<td width="275mm" class="etiqueta" >
	    		<table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">  	   	
    			<tr>
				<td width="16mm" class="etiqueta" ><xsl:call-template name = "etiquetaTitular" />: </td>
				<td width="259mm" class="valor" ><xsl:value-of select="."/></td>
    			</tr>
				</table>	    	
				</td>
	    	</tr>
	    </xsl:if>
	</xsl:template> 

	<xsl:template match = "LOCALIZACION" > 
		<xsl:if test="( . != '')">
		    <tr>
		    	<td width="275mm" class="etiqueta" >
	    		<table width="275mm"  border="0" valign="top" align="center" cellpadding="0mm">  	   	
    			<tr>
				<td width="25mm" class="etiqueta" ><xsl:call-template name = "etiquetaLocalizacion" />: </td>
				<td width="250mm" class="valor" ><xsl:value-of select="."/></td>
    			</tr>
				</table>	    	
				</td>
	    	</tr>
	    </xsl:if>
	</xsl:template> 

	<xsl:template match = "CLASIFICACION_TRAMITE" > 
		<xsl:if test="( . != '')">
		    <tr>
		    	<td width="275mm" class="etiqueta" >
	    		<table width="275mm"  border="0"  valign="top" align="center" cellpadding="0mm">  	   	
    			<tr>
				<td width="45mm" class="etiqueta" valign="top"><xsl:call-template name = "etiquetaClasificacionTramite" />: </td>
				<td width="230mm" class="valor" >
								<table width="228mm"  border="0" valign="top" align="center" cellpadding="0mm">  	   												
						   		<xsl:for-each select="CLASE">		   
						   			<tr><td><xsl:value-of select="."/></td></tr>
						   		</xsl:for-each>
						   		</table>
				</td>
	    	</tr>
				</table>	    	
				</td>
		    </tr>
	    </xsl:if>
	</xsl:template>

    <xsl:template match = "TIPO_LISTADO" >
	</xsl:template>

    <xsl:template name = "todas" >
	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="3mm">  	   
		   <thead>
    			<tr>					
					<td width="25mm" class="cabecera" style="border-left:solid black 1px;"><xsl:call-template name = "etiquetaNumExpediente" /></td>
    				<td width="10mm" class="cabecera"><xsl:call-template name = "etiquetaEstado" /></td>		
					<td width="50mm" class="cabecera"><xsl:call-template name = "etiquetaProcedimiento" /></td>
    				<td width="50mm" class="cabecera"><xsl:call-template name = "etiquetaTitular" /></td>
    				<td width="70mm" class="cabecera"><xsl:call-template name = "etiquetaAsunto" /></td>
    				<xsl:if test="(//CRITERIOS/TIPO_LISTADO = 0)">
						<td width="70mm" class="cabecera" style="border-right:solid black 1px;"><xsl:call-template name = "etiquetaTramites" /></td>
					</xsl:if>
                    <xsl:if test="(//CRITERIOS/TIPO_LISTADO = 1)">
						<td width="70mm" class="cabecera" style="border-right:solid black 1px;"><xsl:call-template name = "etiquetaTramitesFinalizados" /></td>
					</xsl:if>
    			</tr>
  			</thead>
	   		<tbody>	   
		   		<xsl:for-each select="EXPEDIENTE">		   
			 	<tr > 
					<td width="25mm" class="fila" >
						<xsl:value-of select="NUMERO"/>
					</td>
					<td width="10mm" class="fila" >
						<xsl:choose>
						  <xsl:when test=" FECHA_FIN != ''">F</xsl:when>
						  <xsl:otherwise>P</xsl:otherwise>
						</xsl:choose>							
					</td>					
					<td width="50mm" class="fila" align="left">
						<xsl:value-of select="DESCRIPCION_PROCEDIMIENTO"/> 
					</td>
						<td width="50mm" class="fila" align="left">
							<table width="50mm" cellpadding="0mm" border="0">
								<xsl:for-each select="./TITULAR/LINEA">
									<tr>
										<td style="width:100%;align:left">
											<xsl:if test="( CONT != 'null')">
												<xsl:value-of select="CONT"/>
											</xsl:if>
										</td>
									</tr>
								</xsl:for-each>
							</table>
                    </td>	
					<td width="70mm" class="fila"  align="left">
						<xsl:value-of select="ASUNTO"/>
					</td>
					<td width="70mm" class="fila" align="left">
						<table width="60mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TRAMITE">		   
                                <tr>
                                    <td width="60mm">
                                        -<xsl:value-of select="NOMBRE"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="60mm">
                                        (Data inicio: <xsl:value-of select="FECHA_INICIO"/>)
                                    </td>
                                </tr>
                                <xsl:if test="(//CRITERIOS/TIPO_LISTADO = 1)">
						            <tr>
                                        <td width="60mm">
                                            (Data fin: <xsl:value-of select="FECHA_FIN"/>)
                                        </td>
                                    </tr>
					            </xsl:if>
                            </xsl:for-each>
						</table>
					</td>
				 </tr>	
		   		</xsl:for-each>		  
		   	</tbody>
	   </table>
	   
	</xsl:template>


	<xsl:template name = "ninguna" > 		
	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="3mm">  	   
		   <thead>
    			<tr>					
					<td width="25mm" class="cabecera" style="border-left:solid black 1px;"><xsl:call-template name = "etiquetaNumExpediente" /></td>
    				<td width="120mm" class="cabecera"><xsl:call-template name = "etiquetaAsunto" /></td>
    				<td width="130mm" class="cabecera" style="border-right:solid black 1px;"><xsl:call-template name = "etiquetaTramites" /></td>
    			</tr>
  			</thead>
	   		<tbody>	   
		   		<xsl:for-each select="EXPEDIENTE">		   
			 	<tr > 
					<td width="25mm" class="fila" >
						<xsl:value-of select="NUMERO"/>
					</td>
					<td width="120mm" class="fila"  align="left">
						<xsl:value-of select="ASUNTO"/>
					</td>
					<td width="130mm" class="fila" align="left">
						<table width="125mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TRAMITE">		   
                                <tr>
                                    <td width="100%">
                                        -<xsl:value-of select="NOMBRE"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%">
                                        (Data inicio: <xsl:value-of select="FECHA_INICIO"/>)
                                    </td>
                                </tr>
							</xsl:for-each>
						</table>
					</td>
				 </tr>	
		   		</xsl:for-each>		  
		   	</tbody>
	   </table>
	   
	</xsl:template>

	<xsl:template name = "estado" > 		
	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="3mm">  	   
		   <thead>
    			<tr>					
					<td width="25mm" class="cabecera" style="border-left:solid black 1px;"><xsl:call-template name = "etiquetaNumExpediente" /></td>
    				<td width="10mm" class="cabecera"><xsl:call-template name = "etiquetaEstado" /></td>		
    				<td width="120mm" class="cabecera"><xsl:call-template name = "etiquetaAsunto" /></td>
    				<td width="120mm" class="cabecera" style="border-right:solid black 1px;"><xsl:call-template name = "etiquetaTramites" /></td>
    			</tr>
  			</thead>
	   		<tbody>	   
		   		<xsl:for-each select="EXPEDIENTE">		   
			 	<tr > 
					<td width="25mm" class="fila" >
						<xsl:value-of select="NUMERO"/>
					</td>
					<td width="10mm" class="fila" >
						<xsl:choose>
						  <xsl:when test=" FECHA_FIN != ''">F</xsl:when>
						  <xsl:otherwise>P</xsl:otherwise>
						</xsl:choose>							
					</td>					
					<td width="120mm" class="fila"  align="left">
						<xsl:value-of select="ASUNTO"/>
					</td>
					<td width="120mm" class="fila" align="left">
						<table width="115mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TRAMITE">		   
                                <tr>
                                    <td width="100%">
                                        -<xsl:value-of select="NOMBRE"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%">
                                        (Data inicio: <xsl:value-of select="FECHA_INICIO"/>)
                                    </td>
                                </tr>
							</xsl:for-each>
						</table>
					</td>
				 </tr>	
		   		</xsl:for-each>		  
		   	</tbody>
	   </table>
	   
	</xsl:template>
	
	<xsl:template name = "procedimiento" > 		
	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="3mm">  	   
		   <thead>
    			<tr>					
					<td width="25mm" class="cabecera" style="border-left:solid black 1px;"><xsl:call-template name = "etiquetaNumExpediente" /></td>
					<td width="60mm" class="cabecera"><xsl:call-template name = "etiquetaProcedimiento" /></td>
    				<td width="95mm" class="cabecera"><xsl:call-template name = "etiquetaAsunto" /></td>
    				<td width="95mm" class="cabecera" style="border-right:solid black 1px;"><xsl:call-template name = "etiquetaTramites" /></td>
    			</tr>
  			</thead>
	   		<tbody>	   
		   		<xsl:for-each select="EXPEDIENTE">		   
			 	<tr > 
					<td width="25mm" class="fila" >
						<xsl:value-of select="NUMERO"/>
					</td>
					<td width="60mm" class="fila" align="left">
						<xsl:value-of select="DESCRIPCION_PROCEDIMIENTO"/> 
					</td>
					<td width="95mm" class="fila"  align="left">
						<xsl:value-of select="ASUNTO"/>
					</td>
					<td width="95mm" class="fila" align="left">
						<table width="90mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TRAMITE">		   
                                <tr>
                                    <td width="100%">
                                        -<xsl:value-of select="NOMBRE"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%">
                                        (Data inicio: <xsl:value-of select="FECHA_INICIO"/>)
                                    </td>
                                </tr>
							</xsl:for-each>
						</table>
					</td>
				 </tr>	
		   		</xsl:for-each>		  
		   	</tbody>
	   </table>
	   
	</xsl:template>

	<xsl:template name = "titular" > 		
	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="3mm">  	   
		   <thead>
    			<tr height="5mm">					
					<td width="25mm" class="cabecera" style="border-left:solid black 1px;"><xsl:call-template name = "etiquetaNumExpediente" /></td>
					<td width="60mm" class="cabecera"><xsl:call-template name = "etiquetaTitular" /></td>
    				<td width="95mm" class="cabecera"><xsl:call-template name = "etiquetaAsunto" /></td>
    				<td width="95mm" class="cabecera" style="border-right:solid black 1px;"><xsl:call-template name = "etiquetaTramites" /></td>
    			</tr>
  			</thead>
	   		<tbody>	   
		   		<xsl:for-each select="EXPEDIENTE">		   
			 	<tr > 
					<td width="25mm" class="fila" >
						<xsl:value-of select="NUMERO"/>
					</td>
					<td width="60mm" class="fila" align="left">
						<table width="60mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TITULAR/LINEA">
								<tr>
									<td style="width:100%;align:left">
										<xsl:if test="( CONT != 'null')">
											<xsl:value-of select="CONT"/>
										</xsl:if>
									</td>
								</tr>
							</xsl:for-each>
						</table>
                    </td>
					<td width="95mm" class="fila"  align="left">
						<xsl:value-of select="ASUNTO"/>
					</td>
					<td width="95mm" class="fila" align="left">
						<table width="90mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TRAMITE">		   
                                <tr>
                                    <td width="100%">
                                        -<xsl:value-of select="NOMBRE"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%">
                                        (Data inicio: <xsl:value-of select="FECHA_INICIO"/>)
                                    </td>
                                </tr>
							</xsl:for-each>
						</table>
					</td>
				 </tr>	
		   		</xsl:for-each>		  
		   	</tbody>
	   </table>
	   
	</xsl:template>

	<xsl:template name = "estadoProcedimiento" > 		
	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="3mm">  	   
		   <thead>
    			<tr>					
					<td width="25mm" class="cabecera" style="border-left:solid black 1px;"><xsl:call-template name = "etiquetaNumExpediente" /></td>
    				<td width="10mm" class="cabecera"><xsl:call-template name = "etiquetaEstado" /></td>		
					<td width="50mm" class="cabecera"><xsl:call-template name = "etiquetaProcedimiento" /></td>
    				<td width="95mm" class="cabecera"><xsl:call-template name = "etiquetaAsunto" /></td>
    				<td width="95mm" class="cabecera" style="border-right:solid black 1px;"><xsl:call-template name = "etiquetaTramites" /></td>
    			</tr>
  			</thead>
	   		<tbody>	   
		   		<xsl:for-each select="EXPEDIENTE">		   
			 	<tr > 
					<td width="25mm" class="fila" >
						<xsl:value-of select="NUMERO"/>
					</td>
					<td width="10mm" class="fila" >
						<xsl:choose>
						  <xsl:when test=" FECHA_FIN != ''">F</xsl:when>
						  <xsl:otherwise>P</xsl:otherwise>
						</xsl:choose>							
					</td>					
					<td width="50mm" class="fila" align="left">
						<xsl:value-of select="DESCRIPCION_PROCEDIMIENTO"/> 
					</td>
					<td width="95mm" class="fila"  align="left">
						<xsl:value-of select="ASUNTO"/>
					</td>
					<td width="95mm" class="fila" align="left">
						<table width="90mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TRAMITE">		   
                                <tr>
                                    <td width="100%">
                                        -<xsl:value-of select="NOMBRE"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%">
                                        (Data inicio: <xsl:value-of select="FECHA_INICIO"/>)
                                    </td>
                                </tr>
							</xsl:for-each>
						</table>
					</td>
				 </tr>	
		   		</xsl:for-each>		  
		   	</tbody>
	   </table>
	   
	</xsl:template>

	<xsl:template name = "estadoTitular" > 		
	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="1mm" >  	   
		   <thead>
    			<tr>					
					<td width="25mm" class="cabecera" style="border-left:solid black 1px;"><xsl:call-template name = "etiquetaNumExpediente" /></td>
    				<td width="10mm" class="cabecera"><xsl:call-template name = "etiquetaEstado" /></td>		
					<td width="50mm" class="cabecera"><xsl:call-template name = "etiquetaTitular" /></td>
    				<td width="95mm" class="cabecera"><xsl:call-template name = "etiquetaAsunto" /></td>
    				<td width="95mm" class="cabecera" style="border-right:solid black 1px;"><xsl:call-template name = "etiquetaTramites" /></td>
    			</tr>
  			</thead>
	   		<tbody>	   
		   		<xsl:for-each select="EXPEDIENTE">		   
			 	<tr > 
					<td width="25mm" class="fila" >
						<xsl:value-of select="NUMERO"/>
					</td>
					<td width="10mm" class="fila" >
						<xsl:choose>
						  <xsl:when test=" FECHA_FIN != ''">F</xsl:when>
						  <xsl:otherwise>P</xsl:otherwise>
						</xsl:choose>							
					</td>					
					<td width="50mm" class="fila" align="left">
						<table width="50mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TITULAR/LINEA">
								<tr>
									<td style="width:100%;align:left">
										<xsl:if test="( CONT != 'null')">
											<xsl:value-of select="CONT"/>
										</xsl:if>
									</td>
								</tr>
							</xsl:for-each>
						</table>
                    	</td>
					<td width="95mm" class="fila"  align="left">
						<xsl:value-of select="ASUNTO"/>
					</td>
					<td width="95mm" class="fila" align="left">
						<table width="90mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TRAMITE">		   
                                <tr>
                                    <td width="100%">
                                        -<xsl:value-of select="NOMBRE"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%">
                                        (Data inicio: <xsl:value-of select="FECHA_INICIO"/>)
                                    </td>
                                </tr>
							</xsl:for-each>
						</table>
					</td>
				 </tr>	
		   		</xsl:for-each>		  
		   	</tbody>
	   </table>
	   
	</xsl:template>
	
		<xsl:template name = "procedimientoTitular" > 		
	   <table width="275mm"  border="0" valign="top" align="center" cellpadding="3mm">  	   
		   <thead>
    			<tr>					
					<td width="25mm" class="cabecera" style="border-left:solid black 1px;"><xsl:call-template name = "etiquetaNumExpediente" /></td>
    				<td width="50mm" class="cabecera"><xsl:call-template name = "etiquetaProcedimiento" /></td>		
					<td width="50mm" class="cabecera"><xsl:call-template name = "etiquetaTitular" /></td>
    				<td width="80mm" class="cabecera"><xsl:call-template name = "etiquetaAsunto" /></td>
    				<td width="70mm" class="cabecera" style="border-right:solid black 1px;"><xsl:call-template name = "etiquetaTramites" /></td>
    			</tr>
  			</thead>
	   		<tbody>	   
		   		<xsl:for-each select="EXPEDIENTE">		   
			 	<tr > 
					<td width="25mm" class="fila" >
						<xsl:value-of select="NUMERO"/>
					</td>
					<td width="50mm" class="fila" align="left">
						<xsl:value-of select="DESCRIPCION_PROCEDIMIENTO"/> 
					</td>
					<td width="50mm" class="fila" align="left">
						<table width="50mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TITULAR/LINEA">
								<tr>
									<td style="width:100%;align:left">
										<xsl:if test="( CONT != 'null')">
											<xsl:value-of select="CONT"/>
										</xsl:if>
									</td>
								</tr>
							</xsl:for-each>
						</table>
                    </td>
					<td width="80mm" class="fila"  align="left">
						<xsl:value-of select="ASUNTO"/>
					</td>
					<td width="70mm" class="fila" align="left">
						<table width="65mm" cellpadding="0mm" border="0">
							<xsl:for-each select="./TRAMITE">		   
                                <tr>
                                    <td width="100%">
                                        -<xsl:value-of select="NOMBRE"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%">
                                        (Data inicio: <xsl:value-of select="FECHA_INICIO"/>)
                                    </td>
                                </tr>
							</xsl:for-each>
						</table>
					</td>
				 </tr>	
		   		</xsl:for-each>		  
		   	</tbody>
	   </table>
	   
	</xsl:template>
	
	<xsl:template name = "etiquetaNumExpediente" > Nº EXPTE</xsl:template>
	<xsl:template name = "etiquetaEstado" > EST</xsl:template>
	<xsl:template name = "etiquetaProcedimiento" > PROCEDEMENTO</xsl:template>
	<xsl:template name = "etiquetaTitular" > TITULAR</xsl:template>
	<xsl:template name = "etiquetaAsunto" > ASUNTO</xsl:template>
	<xsl:template name = "etiquetaTramites" > TRÁMITES PENDENTES</xsl:template>
    <xsl:template name = "etiquetaTramitesFinalizados" > TRAMITES FINALIZADOS</xsl:template>    
    <xsl:template name = "etiquetaLocalizacion" > LOCALIZACIÓN</xsl:template>
	<xsl:template name = "etiquetaClasificacionTramite" > CLASIFICACIÓN DE TRAMITE</xsl:template>
	
		  
</xsl:stylesheet>





