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
			<table border="0" align="center" width="280mm" cellpadding="0">
			<tr height="200mm">
				<td width="10mm" height="190mm" valign="middle" border=""> 							
					<img src="{datosImagen/impGL}" border="0" height="140mm" width="10mm"/>
				</td>
				<td width="270mm"> 
					<table border="0" align="center" width="270mm" cellpadding="0">
					<tr> 
						<td width="270mm"> <!-- Cabecera -->
							<xsl:call-template name="datosCabecera">
								<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
								<xsl:with-param name="tpg"><xsl:value-of select="$tpg"/></xsl:with-param> 
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td width="270mm" height="130mm" valign="middle"> <!-- Cuerpo: -->
							<table border="0" align="center" width="270mm" cellpadding="0">
								<tr>
									<td> <!-- Cuerpo: 1/5-->
										<xsl:call-template name="dh">	
										<xsl:with-param name="nh">1</xsl:with-param> 
										<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
										</xsl:call-template>
									</td>
								</tr>
								<tr>
									<td> <!-- Cuerpo: 2/5-->
										<xsl:call-template name="dh">
											<xsl:with-param name="nh">2</xsl:with-param> 
											<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
											</xsl:call-template>
									</td>
								</tr>
								<tr>
									<td> <!-- Cuerpo: 3/5-->
										<xsl:call-template name="dh">
											<xsl:with-param name="nh">3</xsl:with-param> 
											<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
											</xsl:call-template>
									</td>
								</tr>
								<tr>
									<td> <!-- Cuerpo: 4/5-->
										<xsl:call-template name="dh">
											<xsl:with-param name="nh">4</xsl:with-param> 
											<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
											</xsl:call-template>
									</td>
								</tr>
								<tr>
									<td> <!-- Cuerpo: 5/5-->
										<xsl:call-template name="dh">
											<xsl:with-param name="nh">5</xsl:with-param> 
											<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param> 
											</xsl:call-template>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				<tr>
						<td width="270mm"> <!-- Pie -->
							<xsl:call-template name="datosPie"/>		
						</td>
					</tr>
					</table>				
				</td>
			</tr>
			</table>			
			<xsl:call-template name="reversoPagina"/>

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

				<table cellborder="0" width="100%" cellpadding="0">
				<tr><td width="100%">
					<table width="100%">
						<tr>
							<td width="50%" style="font-size:12;font-weight:bold;">
								Concello de <xsl:value-of select="datosCabecera/CTE"/>
							</td>
							<td align="right" width="50%" style="font-size:12;font-weight:bold;">
								Folla de inscripción ou modificación
							</td>
						</tr>
						<tr>
							<td width="50%" style="font-size:12;font-weight:bold;">
								Padrón Municipal
							</td>
							<td align="right" width="50%">
								Folla <xsl:value-of select="($pg+1)"/> de <xsl:value-of select="($tpg)"/>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
				<table cellborder="1" width="100%" cellpadding="0">
					<tr>				
						<td width="55%">
							<table cellborder="0" width="100%" cellpadding="0">
								<tr>
									<td style="font-size:6;">
										&nbsp;Rúa,praza,etc
									</td>
									<td style="font-size:6;">
										Nome vía
									</td>
								</tr>
								<tr>
									<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
										&nbsp;<xsl:value-of select="datosCabecera/ABR"/> 
									</td>
									<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/VIA"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										&nbsp;N.Dende
									</td>
									<td style="font-size:6;">
										L.Dende
									</td>
									<td style="font-size:6;">
										N.Ata
									</td>
									<td style="font-size:6;">
										L.Ata
									</td>
									<td style="font-size:6;">
										Km.
									</td>
									<td style="font-size:6;">
										Hm.
									</td>
									<td style="font-size:6;">
										Bloque
									</td>
									<td style="font-size:6;">
										Portal
									</td>
									<td style="font-size:6;">
										Esc.
									</td>
									<td style="font-size:6;">
										Planta
									</td>
									<td style="font-size:6;">
										Porta
									</td>
								</tr>
								<tr>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										&nbsp;<xsl:value-of select="datosCabecera/NUD"/> 
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/LED"/> 
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/NUH"/>
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/LEH"/>
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/KMT"/>
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/HMT"/>
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/BLQ"/>
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/POR"/>
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/ESC"/>
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/PLT"/>
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/PTA"/>
									</td>
								</tr>
							</table>
						</td>
						<td rowspan="2" width="45%">
							<table cellborder="0" width="100%"  cellpadding="0">
								<tr>
									<td height="1mm"></td>
								</tr>
								<tr>
									<td style="font-size:6;font-weight:bold;" align="center">
									A cumprimentar polo Concello
									</td>
								</tr>
								<tr>
									<td height="1mm"></td>
								</tr>
								<tr><td>
									<table cellborder="1" width="100%"  cellpadding="0">
									<tr>							
											<td width="70%">						
							<table cellborder="0" width="100%" height="13mm">
								<tr>
									<td style="font-size:6;">
										Provincia:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/PRV"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Concello:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/MUN"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Entidade colectiva:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/ECO"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Entidade singular:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/ESI"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Núcleo / Diseminado:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/NUC"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Tipo de vivenda:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/TVV"/>
									</td>
								</tr>
							</table>
						</td>
									<td width="30%">							
							<table cellborder="0" width="100%"  height="15mm">
								<tr>
									<td style="font-size:6;">
										Distrito:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/DIS"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Sección:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/SEC"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Quinteiro:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/MAN"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Familia:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/FAM"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Código de vía:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/VIACOD"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Inscripción:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/NUM"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
									</table>
								</td></tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table cellborder="0" width="100%">
								<tr>
									<td align="center" style="font-size:12;font-weight:bold;" colspan="4">
										Información Voluntaria
									</td>
								</tr>
								<tr>
									<td style="font-size:6;" colspan="4">
										Autorizamos ós maiores de idade empadroados nesta folla para comunicar ó Concello as futuras variacións dos nosos datos e obter
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										certificacións ou volantes de empadroamento.
									</td>
									<td style="font-size:6;">
										SI		
									</td>
									<td style="font-size:6;">
										NON		
									</td>
									<td style="font-size:7;">
										Teléfono: <xsl:value-of select="datosCabecera/TEL"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>	</tr>
			</table>
		</xsl:template>

		<xsl:template name="dh">
			<xsl:param name="pg">0</xsl:param> 
			<xsl:param name="nh">1</xsl:param> 
			<table cellborder="1" width="100%" cellpadding="0">
			<tr>
				<td width="5%">
					<table cellpadding="1" width="100%" height="100%">
						<tr>
							<td align="center" height="8mm">
								NºOrde
							</td>
						</tr>
						<tr>
							<td style="font-size:12;font-weight:bold;" align="center" valign="middle" height="12mm">
								<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/ORD"/>
							</td>
						</tr>
					</table>
				</td>
				<td width="25%">
					<table cellpadding="0" width="100%">
						<tr>
							<td border-bottom="1">
								<table cellpadding="2" width="100%">
									<tr>
										<td height="4mm">
											Nome
										</td>
									</tr>
									<tr>
										<td style="font-size:10px;font-weight:bold;font-variant:small-caps;" height="4mm">
											<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/NOM"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td  border-bottom="1">
								<table cellpadding="2" width="100%">
									<tr>
										<td height="4mm">
											1º Apelido
										</td>
									</tr>
									<tr>
										<td style="font-size:10px;font-weight:bold;font-variant:small-caps;" height="4mm">
											<xsl:apply-templates select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PA1"/><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/AP1"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td style="border-bottom:0">
								<table cellpadding="2" width="100%">
									<tr>
										<td height="4mm">
											2º Apelido
										</td>
									</tr>
									<tr>
										<td style="font-size:10px;font-weight:bold;font-variant:small-caps;" height="4mm">
											<xsl:apply-templates select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PA2"/><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/AP2"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
				<td width="15%">
					<table cellpadding="0" width="100%">
						<tr>
							<td>
								<table cellpadding="1">
									<tr border-bottom="1">
										<td height="4mm" >
											Home
										</td>
										<td>
											<xsl:choose>
												<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/SEX = 'H'">x</xsl:when>
												<xsl:otherwise> </xsl:otherwise>
											</xsl:choose>
										</td>
										<td>
											Muller
										</td>
										<td>
											<xsl:choose>
												<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/SEX = 'M'">x</xsl:when>
												<xsl:otherwise> </xsl:otherwise>
											</xsl:choose>
										</td>
									</tr>
									<tr>
										<td colspan="4" height="4mm">
											Si se trata dun Alta por traslado de residencia indique:
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table width="100%" cellpadding="1">
									<tr>
										<td height="4mm">
											Concello (ou Consulado) de procedencia
										</td>
									</tr>
									<tr>
										<td style="font-size:10px;" height="4mm">
											<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/MNO"/>
										</td>
									</tr>
									<tr>
										<td height="4mm">
											Provincia (ou País) de procedencia
										</td>
									</tr>
									<tr>
										<td style="font-size:10px;" height="4mm">
											<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PRO"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
				<td width="35%">
					<table width="100%" cellpadding="0">
						<tr border-bottom="1">
							<td border-right="1">
								<table cellpadding="1" width="100%">
									<tr>
										<td height="4mm">					
											Data de Nacemento(día,mes,ano)
										</td>
									</tr>
									<tr>
										<td height="4mm" style="font-size:10px;font-weight:bold;font-variant:small-caps;">
											<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/FNA"/>
										</td>
									</tr>
								</table>
							</td>
							<td>
								<table cellpadding="1" width="100%">
									<tr>
										<td height="4mm">
											Provincia
										</td>
									</tr>
									<tr>
										<td height="4mm" style="font-size:10px;font-weight:bold;font-variant:small-caps;">
											<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PRN"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr  border-bottom="1">
							<td colspan="2">
								<table cellpadding="1" width="100%">
									<tr>
										<td height="4mm">
											Concello (ou País) de Nacemento
										</td>
									</tr>
									<tr>
										<td height="4mm" style="font-size:10px;font-weight:bold;font-variant:small-caps;">
											<xsl:choose>
												<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PRN = ''"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PAN"/> </xsl:when>
												<xsl:otherwise><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/MNA"/> </xsl:otherwise>
											</xsl:choose>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table cellpadding="1" width="100%">
									<tr>
										<td height="4mm">
											País de Nacionalidade
										</td>
									</tr>
									<tr>
										<td height="4mm" style="font-size:10px;font-weight:bold;font-variant:small-caps;">
											<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PNA"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
				<td width="15%">
					<table width="100%" height="100%">
						<tr>
							<td align="center" colspan="6">
								Tipo de documento de Identidade
							</td>
						</tr>
						<tr>
							<td>
								D.N.I.
							</td>
							<td style="font-size:10px;font-weight:bold;font-variant:small-caps;">
								<xsl:choose>
									<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 1">x</xsl:when>
									<xsl:otherwise> </xsl:otherwise>
								</xsl:choose>
							</td>
							<td>
								Pasaporte
							</td>
							<td style="font-size:10px;font-weight:bold;font-variant:small-caps;">
								<xsl:choose>
									<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 2">x</xsl:when>
									<xsl:otherwise> </xsl:otherwise>
								</xsl:choose>
							</td>
							<td>
								Tarx.Res.
							</td>
							<td style="font-size:10px;font-weight:bold;font-variant:small-caps;">
								<xsl:choose>
									<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 3">x</xsl:when>
									<xsl:otherwise> </xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>
						<tr>
							<td colspan="6" style="font-size:10px;font-weight:bold;font-variant:small-caps;">
								<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/DOC"/>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								Nivel de estudios rematados:
							</td>
							<td colspan="2" style="font-size:10px;font-weight:bold;font-variant:small-caps;">
								<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TIT"/> 
							</td>
						</tr>
						<tr>
							<td colspan="6">
								(ver códigos en reverso)
							</td>
						</tr>
					</table>
				</td>
			</tr>
			</table>
		</xsl:template>

		<xsl:template name="datosPie">
			<table cellborder="1" width="100%" cellpadding="0">
			<tr>
				<td width="85%" height="4mm" colspan= "3" style="font-style:italic" valign="middle">
					Declaramos que as persoas relacionadas nesta folla residen no domicilio indicado e que os seus datos son correctos
				</td>
				<td width="15%" rowspan="2">
					<table cellborder="0" cellpadding="1" width="100%">
						<tr>
							<td align="center">
								A cumplimentar polo Concello
							</td>
						</tr>
						<tr>
							<td align="center">
								Data de recepción da folla
							</td>
						</tr>
						<tr>
							<td align="center" valign="middle" height="10mm" style="font-size:10px;font-weight:bold;">
								<xsl:value-of select="datosPie/FEC"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td width="10%" height="20mm">
					<table cellborder="0" cellpadding="1" width="100%">
						<tr>
							<td align="center">
								Número total de
							</td>
						</tr>
						<tr>
							<td align="center">
								persoas inscritas 
							</td>
						</tr>
						<tr>
							<td align="center">
								nesta folla
							</td>
						</tr>
						<tr>
							<td style="font-size:10px;font-weight:bold;" height="10mm" align="center" valign="middle">
								<xsl:value-of select="datosPie/CON"/>
							</td>
						</tr>
					</table>
				</td>
				<td width="60%" align="center" valign="top">
					Sinatura dos maiores de idade que se inscriben nesta folla
				</td>
				<td width="15%">
					<table cellborder="0" cellpadding="1" width="100%">
						<tr>
							<td align="center">
								(Para altas en inscripcións existentes)
							</td>
						</tr>
						<tr>
							<td align="center">
								Sinatura de persoa maior de idade
							</td>
						</tr>
						<tr>
							<td align="center">
								anteriormente inscrita
							</td>
						</tr>
					</table>
				</td>
			</tr>
			</table>
		</xsl:template>

	<xsl:template name="reversoPagina">
		<table border="0" align="center" width="280mm" height="180mm" cellpadding="0">
		<tr>
			<td width="100%" height="3mm" class="tituloReverso">
			PADRÓN MUNICIPAL DE HABITANTES
			</td>
		</tr>
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			VIVENDA FAMILIAR
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
				<br/>- Documento que acredite a ocupación da vivenda (escritura de propiedade, contrato de arrendamento, contrato ou recibo actual expedido por Compañía suministradora de teléfono, auga, electricidade, etc.). Este documento non é necesario no caso de incorporación de novos habitantes a un grupo familiar xa empadroado.
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

