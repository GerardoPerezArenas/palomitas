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
				<td width="200mm"  border="0"  style="font-size:10;" valign="bottom" height="4mm">(1) Códigos al dorso.</td>
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
									<td width="40%" style="font-size:12;" align="right">Hoja de inscripción o modificación</td>
								</tr>
								<tr>
									<td width="40%" style="font-size:12;"></td>
									<td width="20%" style="font-size:12;font-weight:bold;">Padrón Municipal</td>
									<td width="40%" style="font-size:12;" align="right">Hoja <xsl:value-of select="($pg+1)"/> de <xsl:value-of select="($tpg)"/></td>
								</tr>
								<tr>
									<td width="40%" style="font-size:10;" valign="bottom">Ayuntamiento de <xsl:value-of select="datosCabecera/CTE"/><p> </p></td>
									<td colspan="2" width="60%">
											<table width="100%" border="1px" cellborder="1px" cellpadding="0">											
											<tr>
												<td width="100%" colspan="2" align="center" style="font-size:8;" height="4mm" valign="middle">
													A cumplimentar por el Ayuntamiento
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
																Municipio:
															</td>
															<td width="65%"  style="font-size:8;font-weight:bold;font-variant:small-caps;">
																<xsl:value-of select="datosCabecera/MUN"/>
															</td>
														</tr>
														<tr>
															<td width="35%" style="font-size:8;">
																Entidad Colectiva:
															</td>
															<td width="65%"  style="font-size:8;font-weight:bold;font-variant:small-caps;">
																<xsl:value-of select="datosCabecera/ECO"/>
															</td>
														</tr>
														<tr>
															<td width="35%" style="font-size:8;">
																Entidad Singular:
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
																Tipo de vivienda:
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
															Manzana:
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
									<td colspan="3" width="100%" style="font-size:8;font-weight:bold;">IMPORTANTE: Antes de cumplimentar este impreso lea las instrucciones al dorso.</td>
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
												<td style="font-size:8;">Calle, plaza, etc</td>
												<td style="font-size:8;">Nombre vía</td>
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
												<td style="font-size:8;">Escalera.</td>
												<td style="font-size:8;">Planta</td>
												<td style="font-size:8;">Puerta</td>
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
											<tr><td width="100%">N</td></tr>		
										</table>
									</td>
									<td width="24%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%">Nombre</td></tr>
											<tr><td width="100%">Apellido 1</td></tr>
											<tr><td width="100%">Apellido 2</td></tr>
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
											<tr><td width="100%">NACIMIENTO:</td></tr>
											<tr><td width="100%">Fecha</td></tr>
											<tr><td width="100%">Municipio (o País).</td></tr>
											<tr><td width="100%">Provincia.</td></tr>
											<tr><td width="100%">País de Nacionalidad</td></tr>
										</table>
									</td>
									<td width="20%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%" align="center">Si es alta por traslado</td></tr>
											<tr><td width="100%" align="center">indique procedencia:</td></tr>
											<tr><td width="100%" height="4px"></td></tr>											
											<tr border="1px"><td width="100%"></td></tr>
											<tr><td width="100%" height="4px"></td></tr>																							
											<tr><td width="100%">Municipio (o Consulado)</td></tr>
											<tr><td width="100%">Provincia (o País)</td></tr>
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
											<tr><td width="100%">de identidad</td></tr>
											<tr><td width="100%" align="center">(1)</td></tr>
											<tr><td width="100%">Número</td></tr>
										</table>									
									</td>
									<td width="13%" style="font-size:9;font-weight:normal;">
										<table cellborder="0" width="100%" cellpadding="0">
											<tr><td width="100%">Firma de los</td></tr>
											<tr><td width="100%">mayores de edad</td></tr>
											<tr><td width="100%">inscritos en esta</td></tr>
											<tr><td width="100%">hoja.</td></tr>
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
								</td></tr>
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
					Declaramos que las personas relacionadas en esta hoja residen en el domicilio indicado y que sus datos son correctos.
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
								Autorizamos a los mayores de edad empadronados en esta hoja para comunicar al Muncipicio las futuras variaciones de nuestros datos y para obtener las certificaciones o volantes de empadronamiento. SI   NO   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Teléfono: <xsl:value-of select="datosCabecera/TEL"/>
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
								personas inscritas en
							</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								esta hoja
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
								(Para altas en inscripciones existentes)
							</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								Firma de persona mayor de edad
							</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								anteriormente inscrita.
							</td>
						</tr>
					</table>				
				</td>
				<td width="30%" >
					<table cellborder="0" cellpadding="0" width="100%">
						<tr><td width="100%" height="4px"></td></tr>					
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								A cumplimentar por el Ayuntamiento
							</td>
						</tr>
						<tr>
							<td width="100%" align="center" style="font-size:8;">
								Fecha de recepción de la hoja
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
			PADRÓN MUNICIPAL
			</td>
		</tr>
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			VIVIENDA FAMILIAR
			</td>
		</tr>
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			NORMATIVA LEGAL
			</td>
		</tr>
		<tr>
			<td width="100%" class="textoReverso">
			La Ley 4/1996, de 10 de enero, por la que se modifica la Ley 7/1985, de 2 de abril, reguladora de las Bases del Régimen Local, en relación con el Padrón municipal, y el Real Decreto 2612/1996, de 20 de diciembre, por el que se modifica el Reglamento de Población y Demarcación Territorial de las Entidades Locales aprobado por Real Decreto 1690/1986, de 11 de julio.
			<br/>El Padrón es el registro que acredita la residencia y el domicilio de los vecinos del municipio a todos los efectos administrativos.
			<br/>Toda persona que viva en España está obligada a inscribirse en el Padrón del municipio donde reside habitualmente. Quienes vivan en más de un municipio se inscribirán en el que residan durante más tiempo al año.
			<br/>El vecino tiene derecho a conocer la información que consta en el Padrón sobre su persona y a exigir su rectificación cuando sea errónea o incorrecta.
			<br/>Los datos que se hagan constar en el anverso de esta hoja permitirán la actualización del Padrón de su municipio.
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
				11. No sabe leer ni escribir.
				<br/>21. Sin estudios.
				<br/>22. Enseñanza primaria incompleta, cinco cursos de EGB, Certificado de escolaridad o equivalente.
				<br/>31. ESO, Bachiller elemental, Graduado escolar, EGB completa, Primaria completa o equivalente.
				<br/>32. Formación Profesional primer grado. Oficialía industrial.
				<br/>41. Formación Profesional de segundo grado. Maestría industrial.
				<br/>42. Bachiller superior, BUP.
				<br/>43. Otros titulados medios (Auxiliar de clínica, Secretariado, Programador informático, Auxiliar de vuelo, Diplomado en artes y oficios, etc.).
				<br/>44. Diplomado de Escuelas universitarias (Empresariales, Profesorado de EGB, ATS y similares.
				<br/>45. Arquitecto o Ingeniero técnico.
				<br/>46. Licenciado universitario, Arquitecto o Ingeniero superior.
				<br/>47. Titulados de estudios superiores no universitarios.
				<br/>48. Doctorado y estudios de postgrado o especialización para licenciados.
			</td>
		</tr>
		<tr>
			<td width="100%" height="2mm" class="tituloReverso"></td>
		</tr>			
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			INSTRUCCIONES PARA CUMPLIMENTAR LA HOJA
			</td>
		</tr>
		<tr>
			<td width="100%" class="textoReverso">
				- Escriba con pluma o bolígrafo y en letras mayúsculas. Indique con claridad todos los datos que corresponden a cada una de las personas que se inscriben en la hoja.
				<br/>- Si se solicita la inscripción por traslado de residencia, indique en la casilla correspondiente el municipio y provincia de procedencia. Si procede del extranjero, indique el páis y, en su caso, el Consulado español donde estaba inscrito. En el caso de traslado de domicilio dentro de este mismo municipio indique en esta casilla: MISMO MUNICIPIO.
				<br/>- Si se trata de la inscripción de una persona que no estuviera empadronada con anterioridad en ningún municipio indique en la casilla de municipio de procedencia: NINGUNO. En la misma forma se cumplimentará esta casilla para la inscripción de recién nacidos.
				<br/>- Para la corrección y actualización de los datos que constan en su inscripción padronal, tache el dato incorrecto y escriba el correcto en el espacio o casilla situado debajo de él.
				<br/>- La hoja cumplimentada debe ser firmada por la persona que realiza el trámite, si su objeto es la corrección o actualización de datos. Cuando se trate de una nueva hoja de inscripción, firmarán todos los mayores de edad que se inscriban. Si se incorporan nuevos habitantes a una hoja existente, además deberá firmar la autorización de las nuevas inscripciones alguna persona mayor de edad que ya figuraba inscrita en la hoja.
				<br/>- Rellene la casilla "Número total de personas inscritas en esta hoja", para garantizar la inalterabilidad de los espacios que hayan podido quedar en blanco.
				<br/>- Si tiene alguna duda le será resuelta en el momento de presentar la hoja en su Ayuntamiento.
				<br/>&nbsp;&nbsp;<b>La presentación de esta hoja cumplimentada en su Ayuntamiento implica su conformidad para actualizar el Censo Electoral en consonancia con los datos reflejados en ella.</b>
			</td>
		</tr>
		<tr>
			<td width="100%" height="2mm" class="tituloReverso"></td>
		</tr>			
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			DOCUMENTACIÓN A PRESENTAR PARA REALIZAR EL EMPADRONAMIENTO
			</td>
		</tr>
		<tr>
			<td width="100%" class="textoReverso">
				- Hoja padronal cumplimentada y firmada.
				<br/>- Documento que acredite la identidad de las personas inscritas (D.N.I., Tarjeta de extranjero, Pasaporte, etc. Libro de familia para los menores de 15 años).
				<br/>- Documento que acredite la ocupación de la vivienda (escritura de propiedad, contrato de arrendamiento, contrato o recibo actual expedido por Compañía suministradora de teléfono, agua, electricidad, etc.). Este documento no es necesario en el caso de incorporación de nuevos habitantes a un grupo familiar ya empadronado.
				<br/>- En el caso de rectificación o actualización de datos, documento acreditativo del dato correcto. 
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

