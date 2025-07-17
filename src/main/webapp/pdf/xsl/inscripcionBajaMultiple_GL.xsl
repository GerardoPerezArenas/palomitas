<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<!-- Página por habitante, sólo se considera el primero.	-->

	<xsl:template match="//inscripcion" > 
		<xsl:call-template name="pagina"/>            
	</xsl:template> 


		<xsl:template name="pagina">			
      <xsl:param name="pg">0</xsl:param>
      <xsl:variable name = "nhpg" >
        <xsl:choose>
          <xsl:when test="titulo='BAIXA POR CAMBIO DE RESIDENCIA'">
              <xsl:value-of select="1"/>
          </xsl:when>
          <xsl:otherwise><xsl:value-of select="6"/></xsl:otherwise>
        </xsl:choose>								              
      </xsl:variable> 
      
				<table border="0" align="center" width="270mm" cellpadding="0mm">
<!--        
					<tr>
						<td align="left">
								<table border="0" align="center" width="135mm" height="12mm" cellpadding="0">
								<tr>
									<td width="15mm" class="cabecera" align="left">
										<b> I N E </b>	
									</td>
									<td width="120mm" class="cabecera" align="left">
										<b>DOCUMENTO DE <xsl:value-of select="titulo"/><br/>NO PADRÓN MUNICIPAL DE HABITANTES.</b>
									</td>
								</tr>
								</table>
						</td>
					</tr>
-->          
					<tr>
						<td>
								<table border="0" align="center" width="270mm"  cellpadding="0">
								<tr>
									<td width="130mm">
                    <table border="0" align="center" width="100%" cellpadding="0">
                    <tr>
                      <td width="15mm" class="cabecera" align="left" height="11mm"><b> I N E </b></td>
                      <td width="120mm" class="cabecera" align="left">
                        <b>DOCUMENTO DE <xsl:value-of select="titulo"/><br/>NO PADRÓN MUNICIPAL DE HABITANTES.</b>
                      </td>
                    </tr>
								    <tr>
                      <td colspan="2" class="textoDiligencia" align="left">
                        <xsl:choose>
                          <xsl:when test="titulo='BAIXA POR CAMBIO DE RESIDENCIA'">
                          Declaración triplicada que, a efectos do dispoto no artigo 15 da Lei Reguladora de Bases de Rexime Local 7/1995 do 2 de abril e de acordo co establecido no art. 56 do Regulamento de Poboación e Demarcación Territorial das Entidades Locais, o/a/os veciños asinantes en número de 1 solicitan a baixa no Padrón Municipal de Habitantes no concello de	
                          <xsl:value-of select="datosPadron/descMunicipio"/>, 
                          Provincia de <xsl:value-of select="datosPadron/descProvincia"/>, polo seu traslado ó Concello de 						
                          <xsl:value-of select="datosHabitante/habitante[position() = 1]/descMunicipioDestino"/>, 
                          provincia de <xsl:value-of select="datosHabitante/habitante[position() = 1]/descProvinciaDestino"/>.
                        </xsl:when>
                        <xsl:otherwise></xsl:otherwise>
                        </xsl:choose>								
                      </td>
                    </tr>
                    </table>
                  </td>
									<td width="140mm" align="right">
											<xsl:call-template name="datosAytoBaja"/>														
									</td>
								</tr>							
								</table>
						</td>
					</tr>          
					<tr>
						<td height="4mm">
							Relación de persoas que se dan de baixa
						</td>
					</tr>
					<tr>
						<td height="7mm">
								<table border="1" align="center" width="270mm">
								<tr>
									<td width="72mm" class="textoFijo_r">Nome e apelidos</td>
									<td width="10mm" class="textoFijo_r">Sexo</td>
									<td width="95mm" class="textoFijo_r">Lugar, data de nacemento e nacionalidade</td>
									<td width="50mm" class="textoFijo_r">Título escolar</td>
									<td width="43mm" class="textoFijo_r">Documento</td>
								</tr>
								</table>
						</td>
					</tr>
          <tr>
						<td height="7mm">					
              <xsl:choose>
                <xsl:when test="titulo='BAIXA POR CAMBIO DE RESIDENCIA'">
                    <xsl:call-template name="dhs_CR">
                      <xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
                      <xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
                    </xsl:call-template>								
                </xsl:when>
                <xsl:otherwise>
                <xsl:call-template name="dhs">
                      <xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
                      <xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
                    </xsl:call-template>                  
                </xsl:otherwise>
							</xsl:choose>
              </td>
            </tr>
			</table>

      <!-- Comprobar si se tienen que presentar mas hojas.
            Esto es, comprobar si el número de habitantes que contiene el fichero 
            xml es superior al número de habitantes presentado. 
            Esta comparación es la siguiente:

            - Si (número_de_habitantes > (pg + 1) * nhpg) ...
	
            El número de página comienza en 0, por eso, hay que sumarle 1, y nhpg debido
            a que se presentan nhpg habitantes por página.

            En el caso de que aún no se hayan presentado todos los habitantes, se
            llama de nuevo a la plantilla pasando como parámetro, el número de página
            incrementado en 1.
      -->
		
		<xsl:if test="count(datosHabitante/habitante) &gt; (($pg + 1) * $nhpg)">
			<xsl:call-template name="pagina">
			<xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
			<xsl:with-param name="pg"><xsl:value-of select="$pg + 1"/></xsl:with-param> 
			</xsl:call-template>
		</xsl:if>

		</xsl:template> 

    <xsl:template name="dhs">				 
        <xsl:param name="nhpg"/> 
        <xsl:param name="pg" />        
        <table border="0" align="center" width="270mm" cellpadding="0mm">
        <tr>
						<td height="16mm">
							<xsl:call-template name="dh">
								<xsl:with-param name="nh">1</xsl:with-param>
                <xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
    						<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
                <xsl:with-param name="ph"><xsl:value-of select="1 + ($pg * $nhpg)"/></xsl:with-param>
							</xsl:call-template>              
						</td>
					</tr>
					<tr>
						<td height="16mm">
            <xsl:if test="count(datosHabitante/habitante)+1 &gt; ( 2 + ($pg * $nhpg))">
							<xsl:call-template name="dh">
              	<xsl:with-param name="nh">2</xsl:with-param>                
  							<xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
    						<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
                <xsl:with-param name="ph"><xsl:value-of select="2 + ($pg * $nhpg)"/></xsl:with-param>
							</xsl:call-template>
            
            </xsl:if>          
            </td>
					</tr>          
          <tr>
						<td height="16mm">
            <xsl:if test="count(datosHabitante/habitante)+1 &gt; ( 3 + ($pg * $nhpg))">
							<xsl:call-template name="dh">
								<xsl:with-param name="nh">3</xsl:with-param>
                <xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
    						<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
                <xsl:with-param name="ph"><xsl:value-of select="3 + ($pg * $nhpg)"/></xsl:with-param>
							</xsl:call-template>            
            </xsl:if>
						</td>            
					</tr>                  
				<tr>
					<td height="16mm">
            <xsl:if test="count(datosHabitante/habitante)+1 &gt; ( 4 + ($pg * $nhpg))">
						    <xsl:call-template name="dh">
								<xsl:with-param name="nh">4</xsl:with-param>
                <xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
    						<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
                <xsl:with-param name="ph"><xsl:value-of select="4 + ($pg * $nhpg)"/></xsl:with-param>
							</xsl:call-template>            
            </xsl:if>            								
					</td>
				</tr>
        <tr>
					<td height="16mm">						
            <xsl:if test="count(datosHabitante/habitante)+1 &gt; ( 5 + ($pg * $nhpg))">
                <xsl:call-template name="dh">
								<xsl:with-param name="nh">5</xsl:with-param>
                <xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
    						<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
                <xsl:with-param name="ph"><xsl:value-of select="5 + ($pg * $nhpg)"/></xsl:with-param>
							</xsl:call-template>
            </xsl:if>     								
					</td>
				</tr>
        <tr>
					<td height="16mm">
						<xsl:if test="count(datosHabitante/habitante)+1 &gt; ( 6 + ($pg * $nhpg))">    
              <xsl:call-template name="dh">
								<xsl:with-param name="nh">6</xsl:with-param>
                <xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
    						<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
                <xsl:with-param name="ph"><xsl:value-of select="6 + ($pg * $nhpg)"/></xsl:with-param>
							</xsl:call-template>                        
            </xsl:if>            
					</td>
				</tr>
        </table>        
		</xsl:template>

    <xsl:template name="dhs_CR"> 
        <xsl:param name="nhpg"/> 
        <xsl:param name="pg" />        
        <table border="0" align="center" width="270mm" cellpadding="0mm">
        <tr>
						<td height="16mm">
							<xsl:call-template name="dh">
								<xsl:with-param name="nh">1</xsl:with-param>
                <xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
    						<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
                <xsl:with-param name="ph"><xsl:value-of select="1 + ($pg * $nhpg)"/></xsl:with-param>
							</xsl:call-template>              
						</td>
					</tr>
<!--					
					<tr>
						<td height="16mm">
            <xsl:if test="count(datosHabitante/habitante)+1 &gt; ( 2 + ($pg * $nhpg))">
							<xsl:call-template name="dh">
              	<xsl:with-param name="nh">2</xsl:with-param>                
  							<xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
    						<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
                <xsl:with-param name="ph"><xsl:value-of select="2 + ($pg * $nhpg)"/></xsl:with-param>
							</xsl:call-template>
            
            </xsl:if>          
            </td>
					</tr>          
          <tr>
						<td height="16mm">
            <xsl:if test="count(datosHabitante/habitante)+1 &gt; ( 3 + ($pg * $nhpg))">
							<xsl:call-template name="dh">
								<xsl:with-param name="nh">3</xsl:with-param>
                <xsl:with-param name="nhpg"><xsl:value-of select="$nhpg"/></xsl:with-param> 
    						<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
                <xsl:with-param name="ph"><xsl:value-of select="3 + ($pg * $nhpg)"/></xsl:with-param>
							</xsl:call-template>            
            </xsl:if>
						</td>            
					</tr>                  
-->					
				<tr>
					<td height="30mm">
						<xsl:choose>
						<xsl:when test="titulo='BAIXA POR CAMBIO DE RESIDENCIA'  and datosHabitante/habitante[position() = 1]/codProvinciaDestino!= '' and datosHabitante/habitante[position() = 1]/codMunicipioDestino!= ''">
							<xsl:call-template name="certificacionAlta">  
									<xsl:with-param name="nh">1</xsl:with-param> 
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise></xsl:otherwise>
						</xsl:choose>								
					</td>
				</tr>        
        </table>
        
		</xsl:template>

			<xsl:template name="dh">
				<xsl:param name="nh" /> 
        <xsl:param name="nhpg"/> 
        <xsl:param name="pg" />
        <xsl:param name="ph"/>
		
				
					<table width="270mm" border="1">
						<tr>
							<td width="72mm" class="textoFijo_r" >
								<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/nombre"/>
							</td>
							<td width="10mm" class="textoFijo_r" align="center">
								<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/sexo"/>
							</td>						
							<td width="95mm" class="textoFijo_r1" ><!-- Lugar, fecha de nacimiento y nacionalidad -->
              
								<table width="91mm" border="0" cellpadding="2">
									<tr>
										<td width="72mm"> <!-- Lugar-->
											<table width="72mm" border="0" cellpadding="0mm">
												<tr>
													<td width="7mm" class="textoFijo1" ><!-- Codigo Provincia Nacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/codProvinciaNacimiento"/>
													</td>
													<td width="65mm" class="textoFijo1"> <!-- Provincia Nacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/provinciaNacimiento"/>
													</td>
												</tr>
												<tr>
													<td width="7mm" class="textoFijo1" ><!-- Codigo MunicipioNacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/codLugarNacimiento"/>												</td>
													<td width="65mm" class="textoFijo1"> <!-- Municipio Nacimiento-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/lugarNacimiento"/>
													</td>											
												</tr>
											</table>
										</td>
										<td width="19mm" valign="middle" class="textoFijo1">
											<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/fechaNacimiento"/>
										</td>
									</tr>
									<tr>
										<td colspan="2"> <!-- Nacionalidad -->
											<table width="91mm" border="0" cellpadding="0mm">
												<tr>
													<td width="7mm" class="textoFijo1" > <!-- Cod. Pais Nacionalidad-->
														<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/codPaisNacionalidad"/>
													</td>
													<td width="84mm" class="textoFijo1"> <!-- Desc. Pais Nacionalidad -->
														<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/paisNacionalidad"/>
													</td>												
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
							<td width="50mm"  class="textoFijo_r1" > <!-- Titulo escolar -->
									<table width="46mm" border="0" cellpadding="2">
										<tr>
											<td width="7mm" class="textoFijo1" > <!-- Cod. Titulacion -->
												<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/codTitulacion"/>
											</td>
											<td width="39mm" class="textoFijo1"> <!-- Desc. Titulacion -->
												<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/titulacion"/>
											</td>												
										</tr>
									</table>
							</td>
							<td width="43mm" class="textoFijo_r1"> <!-- Documento -->
									<table width="41mm" border="0" cellpadding="2" >								
								<tr>
									<td>
										<table width="39mm" border="0" cellpadding="0">								
										<tr>
											<td width="6mm" class="textoFijo2" valign="middle" height="3mm">D.N.I.</td>								
											<td width="4mm" class="textoFijo1" valign="left">
												<xsl:if test="datosHabitante/habitante[position() = ($ph)]/tipoDocumento = '1'">
													X
												</xsl:if>
											</td>								
											<td width="10mm" class="textoFijo2" valign="middle">Pasaporte </td>
											<td width="4mm" class="textoFijo1" valign="left">
												<xsl:if test="datosHabitante/habitante[position() = ($ph)]/tipoDocumento = '2'">
													X
												</xsl:if>
											</td>
											<td width="11mm" class="textoFijo2" valign="middle">Tarj. Resid.</td>
											<td width="4mm" class="textoFijo1" valign="left">
												<xsl:if test="datosHabitante/habitante[position() = ($ph)]/tipoDocumento = '3'">
													X
												</xsl:if>
											</td>
										</tr>
										</table>
									</td>
								</tr>
							<tr>
									<td class="textoFijo1">
											<xsl:choose>
											<xsl:when test="datosHabitante/habitante[position() = ($ph)]/tipoDocumento = '0'">
											SIN DOCUMENTO
											</xsl:when>
											<xsl:otherwise>
												<xsl:call-template name="datosDocumento">
												<xsl:with-param name="ph"><xsl:value-of select="$ph"/></xsl:with-param> 												 
												</xsl:call-template>
											</xsl:otherwise>
											</xsl:choose>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</xsl:template>


			<xsl:template name="datosAytoBaja">		
				<table border="0" width="135mm" cellpadding="0">
				<tr>
					<td height="5mm">
						Datos do concello de BAIXA
					</td>
				</tr>			
				<tr>
					<td class="textoFijo1" height="13mm">
						<table border="1" width="135mm" cellpadding="3">
						<tr >
							<td width="30mm" class="textoFijo1">
								Provincia ou país: 
							</td>
							<td width="95mm" class="textoFijo1"> 
								<xsl:value-of select="datosPadron/descProvincia"/>
							</td>
							<td width="10mm" class="textoFijo1"> 
								<xsl:value-of select="datosPadron/codProvincia"/>
							</td>
						</tr>
						<tr>
							<td width="30mm" class="textoFijo1">
								Concello: 
							</td>
							<td width="95mm" class="textoFijo1"> 
								<xsl:value-of select="datosPadron/descMunicipio"/>
							</td>
							<td width="10mm" class="textoFijo1"> 
								<xsl:value-of select="datosPadron/codMunicipio"/>
							</td>	
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td  height="15mm" class="textoDiligencia">
							Dilixencia: queda anotada e rexistrada a baixa no Padrón, coma residentes neste concello, das persoas ás que se refire esta declaración e que figuran relacionadas neste documento.
					</td>
				</tr>
				<tr>
					<td class="textoFijo1" height="9mm">
							<xsl:value-of select="datosAyto/ayto"/>, a <xsl:value-of select="fechaDocFor"/>
					</td>
				</tr>
				<tr>
					<td class="textoFijo1" height="7mm">
							<table border="0" width="135mm" cellpadding="0">
							<tr>
								<td width="65mm">
									(Selo do concello)
								</td>
								<td width="65mm" align="right">						
										O/A Secretario/a
								</td>
							</tr>
							</table>
					</td>
				</tr>
				</table>
        
		</xsl:template>


		<xsl:template name="datosDocumento">					
			<xsl:param name="ph" /> 

				<table width="39mm" border="0" cellpadding="0">								
				<tr>
					<td width="17mm" class="textoFijo1" valign="middle">
						<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/numeroDocumento"/>
					</td>	
					<td width="22mm" class="textoFijo1" valign="middle">
						<xsl:value-of select="datosHabitante/habitante[position() = ($ph)]/letraDocumento"/>
					</td>								
				</tr>
				</table>

		</xsl:template>


		<xsl:template name="certificacionAlta">
			<xsl:param name="nh"/> 
			<table border="0" align="center" width="270mm" cellpadding="0">
        <tr>
					<td colspan="2" height="8mm" align="center"></td>
				</tr>
				<tr>
					<td colspan="2" height="6mm" align="center"><b>CERTIFICACIÓN DE ALTA</b></td>
				</tr>
				<tr>
					<td width="135mm">
						<xsl:call-template name="dNuevoAytoAlta2"/>
					</td>
					<td width="135mm" align="right">
						<xsl:call-template name="dNuevoAytoAlta1">
							<xsl:with-param name="nh">1</xsl:with-param> 
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</xsl:template>


			<xsl:template name="dNuevoAytoAlta1">		
				<xsl:param name="nh"/> 
				<table border="0" align="center" width="135mm" cellpadding="0">
					<tr>
						<td width="75mm"></td>
						<td width="60mm"></td>
					</tr>
					<tr>
						<td colspan="2" height="5mm">
							Datos do Concello de ALTA
						</td>
					</tr>
					<tr>
						<td colspan="2" class="textoFijo1" height="15mm">
							<table border="1" width="135mm" cellpadding="3">
								<tr>
									<td width="30mm" class="textoFijo1">
										Provincia ou País: 
									</td>
									<td width="95mm" class="textoFijo1"> 
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/descProvinciaDestino"/>
									</td>
									<td width="10mm" class="textoFijo1"> 
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codProvinciaDestino"/>
									</td>
								</tr>
								<tr>
									<td width="30mm" class="textoFijo1">
										Concello: 
									</td>
									<td width="95mm" class="textoFijo1"> 
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/descMunicipioDestino"/>
									</td>
									<td width="10mm" class="textoFijo1"> 
										<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/codMunicipioDestino"/>
									</td>	
								</tr>				
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2" height="16mm" class="textoDiligencia">
							Dilixencia: para facer constar que, por Resolución do/da Alcalde/Alcaldesa a data <span class="subrayado">____</span>/<span class="subrayado">____</span>/<span class="subrayado">________</span> foron dadas de alta no Padrón coma residentes neste concello as persoas en número de 1 que figuran neste documento.
						</td>
					</tr>
					<tr>
						<td colspan="2" height="9mm">
							<xsl:value-of select="datosHabitante/habitante[position() = ($nh)]/descMunicipioDestino"/>, a <span class="subrayado">____</span> de <span class="subrayado">________________</span> de <span class="subrayado">________</span>
						</td>
					</tr>
					<tr>
						<td width="75mm">
							(Selo do concello)
						</td>
						<td width="60mm" align="right">
							O/A Secretario/a
						</td>
					</tr>
				</table>			
			</xsl:template>

			<xsl:template name="dNuevoAytoAlta2">						
				<table border="0" width="132mm" cellpadding="0">
					<tr>
						<td height="5mm">
							Datos do Concello de ALTA 
						</td>
					</tr>
					<tr>
						<td class="textoFijo1" height="32mm">
							<table border="1" width="132mm" cellpadding="3">
							<tr>
								<td width="13mm" height="1mm"></td>
								<td width="12mm" height="1mm"></td>
								<td width="30mm" height="1mm"></td>
								<td width="43mm" height="1mm"></td>
								<td width="24mm" height="1mm"></td>
								<td width="10mm" height="1mm"></td>
							</tr>
							<tr>
								<td width="13mm" class="textoFijo1">Distrito: </td>
								<td width="12mm" class="textoFijo1">
									<span class="subrayado">_____</span>
								</td>
								<td width="30mm" class="textoFijo1">Entidade colectiva: </td>
								<td colspan="2" class="textoFijo1">
									<span class="subrayado">____________________________________________</span>
								</td>
								<td width="10mm" class="textoFijo1"> 
									<span class="subrayado">____</span>
								</td>
							</tr>
							<tr>
								<td width="13mm" class="textoFijo1">Sección: </td>
								<td width="12mm" class="textoFijo1">
									<span class="subrayado">_____</span>
								</td>
								<td width="30mm" class="textoFijo1">Entidade singular: </td>
								<td colspan="2" class="textoFijo1">
									<span class="subrayado">____________________________________________</span>
								</td>
								<td width="10mm" class="textoFijo1"> 
									<span class="subrayado">____</span>
								</td>
							</tr>
							<tr>
								<td width="13mm" class="textoFijo1"></td>
								<td width="12mm" class="textoFijo1"></td>
								<td width="30mm" class="textoFijo1">Núcleo/Diseminado: </td>
								<td colspan="2" class="textoFijo1">
									<span class="subrayado">____________________________________________</span>
								</td>
								<td width="10mm" class="textoFijo1"> 
									<span class="subrayado">____</span>
								</td>
							</tr>
							<tr>
								<td width="13mm" class="textoFijo1">Domicilio: </td>
								<td colspan="3" class="textoFijo1"> 
									<span class="subrayado">_________________________________________________________</span>
								</td>
								<td colspan="2" class="textoFijo1"> 
									Nº: <span class="subrayado">_________________</span>
								</td>
							</tr>
							<tr>
								<td colspan="6">
									<table border="0" align="center" width="132mm" >
									<tr>
										<td width="1.5mm" class="textoFijo1"> </td>
										<td width="8mm" class="textoFijo1">Km:</td>
										<td width="10mm" class="textoFijo1">
											<span class="subrayado">____</span>
										</td>
										<td width="8mm" class="textoFijo1">Blq:</td>
										<td width="10mm" class="textoFijo1">
											<span class="subrayado">____</span>
										</td>
										<td width="8mm" class="textoFijo1">Prtl:</td>
										<td width="10mm" class="textoFijo1">
											<span class="subrayado">____</span>
										</td>
										<td width="8mm" class="textoFijo1">Esc:</td>
										<td width="10mm" class="textoFijo1">
											<span class="subrayado">____</span>
										</td>
										<td width="8mm" class="textoFijo1">Plnt:</td>
										<td width="10mm" class="textoFijo1">
											<span class="subrayado">____</span>
										</td>
										<td width="9mm" class="textoFijo1">Prta:</td>
										<td width="10mm" class="textoFijo1">
											<span class="subrayado">____</span>
										</td>
										<td width="7mm" class="textoFijo1">CP:</td>
										<td width="14mm" class="textoFijo1">
											<span class="subrayado">________</span>
										</td> 
									</tr>
									</table>
								</td>
							</tr>
							</table>
						</td>
					</tr>		

					<tr>
						<td height="5mm" class="textoDiligencia">
							IMPORTANTE: este documento deberá ser presentado polo interesado ó solicita-lo alta coma residente no novo concello dentro do plazo de 30 días contados a partires da data na que se lle ortorgase a baixa no concello de procedencia.
						</td>
					</tr>
				</table>				
			</xsl:template>

</xsl:stylesheet>
