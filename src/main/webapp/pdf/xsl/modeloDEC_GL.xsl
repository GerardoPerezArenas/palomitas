<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--
Desde aquí se lanza la ejecución de la primera página. 
El resto de páginas se imprimen utilizando recursividad.
El número de página a presentar comienza en 0, para facilitar los cálculos.
-->

	<xsl:template match="//hojaPadronal" > 
		<xsl:call-template name="pagina">
			<xsl:with-param name="tpg"><xsl:value-of select="ceiling( count(datosHabitante/habitante)div 5)"/></xsl:with-param> 
		</xsl:call-template>
	</xsl:template> 


		<xsl:template name="pagina">
			<xsl:param name="pg">0</xsl:param> 
			<xsl:param name="tpg">1</xsl:param>
			<table border="0"  align="center" width="200mm" cellpadding="0mm">
			<tr>
				<td width="200mm"  border="0" cellpadding="0mm"> 							
							<xsl:call-template name="datosCabecera">
								<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
								<xsl:with-param name="tpg"><xsl:value-of select="$tpg"/></xsl:with-param> 
							</xsl:call-template>
				</td>			
			</tr>				
			<tr>
				<td width="200mm"  border="0">
					<table cellborder="1px" width="100%" cellpadding="0" border="1px">
								<xsl:call-template name="dh">	
									<xsl:with-param name="nh">1</xsl:with-param> 
									<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
								</xsl:call-template>
								<xsl:call-template name="dh">	
									<xsl:with-param name="nh">2</xsl:with-param> 
									<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
								</xsl:call-template>								
								<xsl:call-template name="dh">	
									<xsl:with-param name="nh">3</xsl:with-param> 
									<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
								</xsl:call-template>								
								<xsl:call-template name="dh">	
									<xsl:with-param name="nh">4</xsl:with-param> 
									<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
								</xsl:call-template>								
								<xsl:call-template name="dh">	
									<xsl:with-param name="nh">5</xsl:with-param> 
									<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
								</xsl:call-template>																
					</table>
				</td>
			</tr>
			<tr>
				<td width="200mm"  border="0" height="1mm"></td>
			</tr>
			<tr>
				<td width="200mm"  border="0" cellpadding="0mm"> 
						<xsl:call-template name="datosPie"/>		
				</td>
			</tr>
			<tr>
				<td width="200mm"  border="0"  style="font-size:10;" valign="bottom" height="4mm">(1) Códigos ó dorso.</td>
			</tr>
			<tr>
				<td width="200mm"  border="0">
					<xsl:call-template name="reversoPagina"/>
				</td>
			</tr>
			</table>			
			

			<!--
			Aquí vamos a realizar las comprobaciones necesarias para saber si hay que 
			presentar más páginas.

			Para saber que hay que presentar más páginas, comprobamos si el número de
			habitantes que contiene el fichero xml es superior al número de habitantes
			que hemos presentado. Esta comparación es la siguiente:

				- Si (número_de_habitantes > (pg + 1) * 5) ...
				
			El número de página comienza en 0, por eso, tenemos que sumarle 1, y 5 debido
			a que se presentan 5 habitantes por página.

			En el caso de que aún no se hayan presentado todos los habitantes, se
			llama de nuevo a la plantilla pasando como parámetro, el número de página
			incrementado en 1.
			-->
		
			<xsl:if test="count(datosHabitante/habitante) &gt; (($pg + 1) * 5)">
				<xsl:call-template name="pagina">
				<xsl:with-param name="pg"><xsl:value-of select="$pg + 1"/></xsl:with-param> 
				<xsl:with-param name="tpg"><xsl:value-of select="$tpg"/></xsl:with-param> 
				</xsl:call-template>
			</xsl:if>
		
	  </xsl:template> 


			<xsl:template name="datosCabecera">
				<xsl:param name="pg">0</xsl:param> 
				<xsl:param name="tpg">1</xsl:param>

				<table cellborder="0" width="100%" cellpadding="0" border="0">
					<tr>
						<td width="100%">
							<table width="100%" border="0" cellborder="0">
								<tr>
									<td width="40%" style="font-size:12;"></td>
									<td width="20%" style="font-size:12;"></td>
									<td width="40%" style="font-size:12;" align="right">Folla de inscripción ou modificación</td>
								</tr>
								<tr>
									<td width="40%" style="font-size:12;"></td>
									<td width="20%" style="font-size:12;font-weight:bold;">Padrón Municipal</td>
									<td width="40%" style="font-size:12;" align="right">Folla <xsl:value-of select="($pg+1)"/> de <xsl:value-of select="($tpg)"/></td>
								</tr>
								<tr>
									<td width="40%" style="font-size:10;" valign="bottom">Concello de <xsl:value-of select="datosCabecera/CTE"/><p> </p></td>
									<td colspan="2" width="60%">
											<table width="100%" border="1px" cellborder="1px" cellpadding="0">											
											<tr>
												<td width="100%" colspan="2" align="center" style="font-size:8;" height="4mm" valign="middle">
													A cumprimentar polo Concello
												</td>
											</tr>											
											<tr>
												<td width="60%" margin-right="0mm">

													<table border="0" cellborder="0" width="100%">
														<tr><td colspan="2" width="100%" height="1mm"></td></tr>
														<tr>
															<td width="35%" style="font-size:8;">
																Provincia:
															</td>
															<td width="65%" style="font-size:8;font-weight:bold;font-variant:small-caps;">
																<xsl:value-of select="datosCabecera/PRV"/>
															</td>
														</tr>
														<tr>
															<td width="35%" style="font-size:8;">
																Concello:
															</td>
															<td width="65%"  style="font-size:8;font-weight:bold;font-variant:small-caps;">
																<xsl:value-of select="datosCabecera/MUN"/>
															</td>
														</tr>
														<tr>
															<td width="35%" style="font-size:8;">
																Entidade Colectiva:
															</td>
															<td width="65%"  style="font-size:8;font-weight:bold;font-variant:small-caps;">
																<xsl:value-of select="datosCabecera/ECO"/>
															</td>
														</tr>
														<tr>
															<td width="35%" style="font-size:8;">
																Entidade Singular:
															</td>
															<td width="65%"  style="font-size:8;font-weight:bold;font-variant:small-caps;">
																<xsl:value-of select="datosCabecera/ESI"/>
															</td>
														</tr>
														<tr>
															<td width="35%" style="font-size:8;">
																Núcleo/Disem.:
															</td>
															<td width="65%" style="font-size:8;font-weight:bold;font-variant:small-caps;">
																<xsl:value-of select="datosCabecera/NUC"/>
															</td>
														</tr>
														<tr>
															<td width="35%" style="font-size:8;">
																Tipo de vivenda:
															</td>
															<td width="65%" style="font-size:8;font-weight:bold;font-variant:small-caps;">
																<xsl:value-of select="datosCabecera/TVV"/>
															</td>
														</tr>															
														<tr><td colspan="2" width="100%" height="1mm"></td></tr>														
													</table>
												</td>
												<td width="40%"  margin-left="0mm">
													<table cellborder="0" width="100%"  >
													<tr><td colspan="2" width="100%" height="1mm"></td></tr>													
													<tr>
														<td width="35%" style="font-size:8;">
															Distrito:
														</td>
														<td width="65%" style="font-size:8;font-weight:bold;font-variant:small-caps;">
															<xsl:value-of select="datosCabecera/DIS"/>
														</td>
													</tr>
													<tr>
														<td width="35%" style="font-size:8;">
															Sección:
														</td>
														<td width="65%" style="font-size:8;font-weight:bold;font-variant:small-caps;">
															<xsl:value-of select="datosCabecera/SEC"/>
														</td>
													</tr>
													<tr>
														<td width="35%" style="font-size:8;">
															Quinteiro:
														</td>
														<td width="65%" style="font-size:8;font-weight:bold;font-variant:small-caps;">
															<xsl:value-of select="datosCabecera/MAN"/>
														</td>
													</tr>
													<tr>
														<td width="35%" style="font-size:8;">
															Cód. Vía:
														</td>
														<td width="65%" style="font-size:8;font-weight:bold;font-variant:small-caps;">
															<xsl:value-of select="datosCabecera/VIACOD"/>
														</td>
													</tr>
													<tr>
														<td width="35%" style="font-size:8;">
															Inscripción:
														</td>
														<td width="65%" style="font-size:8;font-weight:bold;font-variant:small-caps;">
															<xsl:value-of select="datosCabecera/NUM"/>
														</td>
													</tr>
													<tr><td colspan="2" width="100%" height="1mm"></td></tr>													
												</table>
												</td>
											</tr>																						
										</table>
									</td>
								</tr>
								<tr>
									<td colspan="3" width="100%" style="font-size:8;font-weight:bold;">IMPORTANTE: Antes de cumprimentar este impreso lea as instruccións ó dorso.</td>
								</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td width="100%">
								<table width="100%" border="1px" cellborder="1px">								
								<tr>
									<td width="100%" colspan="8">
										<table cellborder="0" width="100%" border="0">
											<tr>
												<td style="font-size:8;">Rúa, plaza, etc</td>
												<td style="font-size:8;">Nome vía</td>
											</tr>
											<tr>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/ABR"/> 
												</td>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/VIA"/>
												</td>
											</tr>
											<tr>
												<td style="font-size:8;">Número</td>
												<td style="font-size:8;">Letra</td>
												<td style="font-size:8;">Km.</td>
												<td style="font-size:8;">Bloque</td>
												<td style="font-size:8;">Portal</td>
												<td style="font-size:8;">Escaleira</td>
												<td style="font-size:8;">Planta</td>
												<td style="font-size:8;">Porta</td>
											</tr>
											<tr>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/NUD"/> 
													<xsl:choose>
														<xsl:when test="not(datosCabecera/NUH='')">
															-<xsl:value-of select="datosCabecera/NUH"/>
														</xsl:when>
													<xsl:otherwise></xsl:otherwise>
													</xsl:choose>
												</td>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/LED"/> 
													<xsl:choose>
														<xsl:when test="not(datosCabecera/LEH='')">
															-<xsl:value-of select="datosCabecera/LEH"/>
														</xsl:when>
													<xsl:otherwise></xsl:otherwise>
													</xsl:choose>													
												</td>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/KMT"/>
												</td>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/BLQ"/>
												</td>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/POR"/>
												</td>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/ESC"/>
												</td>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/PLT"/>
												</td>
												<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
													<xsl:value-of select="datosCabecera/PTA"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td width="3%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%">Nº</td></tr>
											<tr><td width="100%"></td></tr>
											<tr><td width="100%">O</td></tr>
											<tr><td width="100%">R</td></tr>
											<tr><td width="100%">D</td></tr>
											<tr><td width="100%">E</td></tr>
										</table>
									</td>
									<td width="24%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%">Nome</td></tr>
											<tr><td width="100%">Apelido 1</td></tr>
											<tr><td width="100%">Apelido 2</td></tr>
										</table>
									</td>
									<td width="3%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%" align="center">S</td></tr>
											<tr><td width="100%" align="center">E</td></tr>
											<tr><td width="100%" align="center">X</td></tr>
											<tr><td width="100%" align="center">O</td></tr>
											<tr><td width="100%">(1)</td></tr>
										</table>									
									</td>
									<td width="17%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%">NACEMENTO:</td></tr>
											<tr><td width="100%">Data</td></tr>
											<tr><td width="100%">Concello (ou País).</td></tr>
											<tr><td width="100%">Provincia.</td></tr>
											<tr><td width="100%">País de Nacionalidade</td></tr>
										</table>
									</td>
									<td width="20%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%" align="center">Si é alta por traslado</td></tr>
											<tr><td width="100%" align="center">indique procedencia:</td></tr>
											<tr><td width="100%" height="4px"></td></tr>											
											<tr border="1px"><td width="100%"></td></tr>
											<tr><td width="100%" height="4px"></td></tr>																							
											<tr><td width="100%">Concello (ou Consulado)</td></tr>
											<tr><td width="100%">Provincia (ou País)</td></tr>
										</table>									
									</td>	
									<td width="8%" style="font-size:9;font-weight:normal;" >
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%">Nivel de</td></tr>
											<tr><td width="100%">estudios</td></tr>
											<tr><td width="100%">terminados</td></tr>
											<tr><td width="100%" align="center">(1)</td></tr>
										</table>
									</td>
									<td width="12%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%">Documento</td></tr>
											<tr><td width="100%">de identidade</td></tr>
											<tr><td width="100%" align="center">(1)</td></tr>
											<tr><td width="100%">Número</td></tr>
										</table>									
									</td>
									<td width="13%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%">Firma dos</td></tr>
											<tr><td width="100%">maiores de idade</td></tr>
											<tr><td width="100%">inscritos nesta</td></tr>
											<tr><td width="100%">folla.</td></tr>
										</table>									
									</td>
								</tr>																
							</table>
						</td>
					</tr>
					</table>
		</xsl:template>

		<xsl:template name="dh">
			<xsl:param name="pg">0</xsl:param> 
			<xsl:param name="nh">1</xsl:param> 

					<tr>
						<td width="3%" style="font-size:9;font-weight:normal;" height="28mm">
							<table cellborder="0" width="100%" cellpadding="1mm">
								<tr><td width="100%" ><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/ORD"/></td></tr>
							</table>
						</td>
						<td width="24%" style="font-size:9;font-weight:normal;">
							<table cellborder="0" width="100%" cellpadding="1mm">
								<tr><td width="100%"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/NOM"/></td></tr>
								<tr><td width="100%"><xsl:apply-templates select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PA1"/><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/AP1"/></td></tr>
								<tr><td width="100%"><xsl:apply-templates select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PA2"/><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/AP2"/></td></tr>
							</table>
						</td>
						<td width="3%" style="font-size:9;font-weight:normal;">
							<table cellborder="0" width="100%" cellpadding="1mm">
								<tr><td width="100%" align="center" valign="middle"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/SEX"/></td></tr>
							</table>									
						</td>
						<td width="17%" style="font-size:9;font-weight:normal;">
							<table cellborder="0" width="100%"   cellpadding="1mm">
								<tr><td width="100%"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/FNA"/></td></tr>
								<tr>
									<td width="100%">
											<xsl:choose>
												<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PRN = ''"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PAN"/> </xsl:when>
												<xsl:otherwise><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/MNA"/> </xsl:otherwise>
											</xsl:choose>
									</td>
								</tr>
								<tr><td width="100%"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PRN"/></td></tr>
								<tr><td width="100%"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PNA"/></td></tr>
							</table>
						</td>
						<td width="20%" style="font-size:9;font-weight:normal;">
							<table cellborder="0" width="100%"   cellpadding="1mm">
								<tr><td width="100%"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/MNO"/></td></tr>
								<tr><td width="100%"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PRO"/></td></tr>
							</table>									
						</td>	
						<td width="8%" style="font-size:9;font-weight:normal;" >
							<table cellborder="0" width="100%" cellpadding="1mm">
								<tr><td width="100%" align="center"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TIT"/> </td></tr>
							</table>
						</td>
						<td width="12%" style="font-size:9;font-weight:normal;">
							<table cellborder="0" width="100%" cellpadding="1mm">
								<tr>
									<td width="100%">
										<xsl:choose>
											<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 1">DNI</xsl:when>
											<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 2">Pasaporte</xsl:when>
											<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 3">Tarj.Res.</xsl:when>
											<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 0"></xsl:when>
											<xsl:otherwise> </xsl:otherwise>
										</xsl:choose>
									</td>
								</tr>
								<tr><td width="100%" align="center" height="4mm"></td></tr>
								<tr><td width="100%">
										<xsl:choose>
											<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 0">
											</xsl:when>											
											<xsl:otherwise> 
												<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/DOC"/>
											</xsl:otherwise>
										</xsl:choose>								
									</td>
								</tr>
							</table>									
						</td>
						<td width="13%" style="font-size:9;font-weight:normal;">
							<table cellborder="0" width="100%" cellpadding="0">
								<tr><td width="100%" valign="middle"></td></tr>
							</table>									
						</td>
					</tr>																

		</xsl:template>

		<xsl:template name="datosPie">
			<table width="100%" border="0px" cellpadding="0" cellborder="0px">
			<tr>
				<td width="100%" style="font-size:8;">
					Declaramos que as persoas relacionadas nesta folla residen no domicilio indicado e que os seus datos son correctos.
				</td>
			</tr>
			</table>
						
			<table width="100%" border="1px" cellpadding="0" cellborder="1px">
			<tr>
				<td width="100%" colspan="3" align="center">
					<table cellborder="0" cellpadding="0" width="96%">
						<tr><td width="100%" height="4px"></td></tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;font-weight:bold;">
								Información voluntaria
							</td>
						</tr>
						<tr>
							<td width="100%" align="left" style="font-size:8;">						
								Autorizamos ós maiores de idade empadroados nesta folla para comunicar ó Concello as vindeiras variacións dos nosos datos e para obter certificacións ou volantes de empadroamento. SI   NO   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Teléfono: <xsl:value-of select="datosCabecera/TEL"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>			
			<tr>
				<td width="20%" >
					<table cellborder="0" cellpadding="0" width="100%">
						<tr><td width="100%" height="4px"></td></tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								Número total de
							</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								persoas inscritas
							</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								nesta folla
							</td>
						</tr>
						<tr>
							<td width="100%" style="font-weight:bold; font-size:8;" height="10mm" align="center" valign="middle">
								<xsl:value-of select="datosPie/CON"/>
							</td>
						</tr>
					</table>				
				</td>
				<td width="50%" >
					<table cellborder="0" cellpadding="0" width="100%">
						<tr><td width="100%" height="4px"></td></tr>					
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								(Para altas en inscripcións existentes)
							</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								Sinatura do Director do establecemento colectivo
							</td>
						</tr>
					</table>				
				</td>
				<td width="30%" >
					<table cellborder="0" cellpadding="0" width="100%">
						<tr><td width="100%" height="4px"></td></tr>					
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								A cumprimentar polo Concello
							</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								Data de recepción da folla
							</td>
						</tr>
						<tr>
							<td width="100%" align="center" valign="middle" height="10mm" style="font-weight:bold; font-size:8;">
								<xsl:value-of select="datosPie/FEC"/>
							</td>
						</tr>
					</table>				
				</td>				
			</tr>						
			</table>							

		</xsl:template>

	<xsl:template name="reversoPagina">
		<BR/>
		<table border="0" align="center" width="200mm"  cellpadding="0">
		<tr>
			<td width="100%" height="3mm" class="tituloReverso">
			PADRÓN MUNICIPAL DE HABITANTES
			</td>
		</tr>
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			ESTABLECEMENTO COLECTIVO
			</td>
		</tr>
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			NORMATIVA LEGAL
			</td>
		</tr>
		<tr>
			<td width="100%" class="textoReverso">
			A Lei 4/1996, de 10 de xaneiro, pola que se modifica a Lei 7/1985, de 2 de abril, reguladora das Bases de Réxime Local, en relación co Padrón municipal, e o Real Decreto 2612/1996, de 20 de dicembro, polo que se modifica o Reglamento de Poboación e Demarcación Territorial das Entidades Locais aprobado por Real Decreto 1690/1986, de 11 de xullo.
			<br/>O Padrón é o rexistro que acredita a residencia e o domicilio dos veciños do municipio a todos os efectos administrativos.
			<br/>Toda persoa que viva en España está obrigada a inscribirse no Padrón do municipio onde reside habitualmente. Quen viva en máis dun municipio se inscribirá no que resida durante máis tempo ó ano.
			<br/>O veciño ten dereito a coñecer a información que consta no Padrón sobre a súa persona e a esixir a súa rectificación cando sexa errónea ou incorrecta.
			<br/>Os datos que se fagan constar no anverso desta folla permitirán a actualización do Padrón do seu municipio.
			</td>
		</tr>
		<tr>
			<td width="100%" height="2mm" class="tituloReverso"></td>
		</tr>			
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			CÓDIGOS DE NIVEL DE INSTRUCCIÓN
			</td>
		</tr>
		<tr>
			<td width="100%" class="textoReverso">
				11. Non sabe ler nin escribir.
				<br/>21. Sen estudios.
				<br/>22. Ensinanza primaria incompleta, cinco cursos de EXB, Certificado de escolaridade ou equivalente.
				<br/>31. ESO, Bacharel elemental, Graduado escolar, EXB completa, Primaria completa ou equivalente.
				<br/>32. Formación Profesional primero grado. Oficialía industrial.
				<br/>41. Formación Profesional de segundo grado. Maestría industrial.
				<br/>42. Bacharel superior, BUP.
				<br/>43. Outros titulados medios (Auxiliar de clínica, Secretariado, Programador informático, Auxiliar de voo, Diplomado en artes e oficios, etc.).
				<br/>44. Diplomado de Escolas universitarias (Empresariais, Profesorado de EXB, ATS e similares).
				<br/>45. Arquitecto ou Enxenieiro técnico.
				<br/>46. Licenciado universitario, Arquitecto ou Enxenieiro superior.
				<br/>47. Titulados de estudios superiores non universitarios.
				<br/>48. Doctorado e estudios de postgrado ou especialización para licenciados.
			</td>
		</tr>
		<tr>
			<td width="100%" height="2mm" class="tituloReverso"></td>
		</tr>			
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			INSTRUCCIÓNS PARA CUMPRIMENTAR A FOLLA
			</td>
		</tr>
		<tr>
			<td width="100%" class="textoReverso">
				- Escriba con pluma ou bolígrafo e en letras maiúsculas. Indique con claridade todos os datos que corresponden a cada unha das persoas que se inscriben na folla.
				<br/>- Si se solicita a inscripción por traslado de residencia, indique na casilla correspondente o municipio e provincia de procedencia. Si procede do extranxeiro, indique o páis e, no seu caso, o Consulado español donde estaba inscrito. No caso de traslado de domicilio dentro deste mesmo municipio indique nesta casilla: MESMO MUNICIPIO.
				<br/>- Si se trata da inscripción dunha persona que non estivera empadroada con anterioridade en ningún municipio indique na casilla de municipio de procedencia: NINGÚN. Na mesma forma se cumplimentará esta casilla para a inscripción de recén nacidos.
				<br/>- Para a corrección e actualización dos datos que constan na súa inscripción padroal, tache o dato incorrecto e escriba o correcto no  espacio ou casilla situado debaixo del.
				<br/>- A folla cumprimentada debe ser asinada pola persoa que realiza o trámite, se o seu obxecto é a corrección ou actualización de datos. Cando se trate dunha nova folla de inscripción, asinarán todos os maiores de idade que se inscriban. Si se incorporan novos habitantes a unha folla existente, ademáis deberán asinar a autorización das novas inscripcións o director do establecemento colectivo.
				<br/>- Cumprimente a casilla "Número total de personas inscritas nesta folla", para garantizar a inalterabilidade dos espacios que puideran quedar en branco.
				<br/>- Se ten algunha dúbida lle será resolta no momento de presentar a folla no seu Concello.
				<br/>- Se utilizará un cuestionario de vivendas colectivas para cada hotel, pensión, fonda, parador, casa de hóspedes, residencia, hostal, albergue e similares, así como para establecemento relixioso, militar, sanitario, penitenciario, etc. no que se atope, ó menos unha persona.
				<br/>- Non obstante, si dentro destes establecimientos colectivos existen vivendas de carácter familiar destinadas ó persoal directivo, administrativo ou de servicios, as persoas que na data de referencia se atopen nelas se inscribirán aparte nunha folla de vivenda familiar.
				<br/><b>&nbsp;&nbsp;A presentación desta folla cumprimentada no seu Concello implica a súa conformidade para actualizar o Censo Electoral en consonancia cos datos reflexados nela.
				</b>
			</td>
		</tr>
		<tr>
			<td width="100%" height="2mm" class="tituloReverso"></td>
		</tr>			
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			DOCUMENTACIÓN A PRESENTAR PARA REALIZAR O EMPADROAMENTO
			</td>
		</tr>
		<tr>
			<td width="100%" class="textoReverso">
				- Folla padroal cumprimentada e asinada.
				<br/>- Documento que acredite a identidade das personas inscritas (D.N.I., Tarxeta de extranxeiro, Pasaporte, etc. Libro de familia para os menores de 15 anos).
				<br/>- No caso de rectificación ou actualización de datos, documento acreditativo do dato correcto. 
			</td>
		</tr>
		</table>
	</xsl:template>

	<xsl:template match="PA1">
		<xsl:if test=". != ''">
			<xsl:value-of select="."/> 
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="PA2">
		<xsl:if test=". != ''">
			<xsl:value-of select="."/> 
		</xsl:if>
	</xsl:template>


</xsl:stylesheet>

