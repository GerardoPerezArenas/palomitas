<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--
Desde aqu� se lanza la ejecuci�n de la primera p�gina. 
El resto de p�ginas se imprimen utilizando recursividad.
El n�mero de p�gina a presentar comienza en 0, para facilitar los c�lculos.
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
					<img src="{datosImagen/impES}" border="0" height="140mm" width="10mm"/>
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
			Aqu� vamos a realizar las comprobaciones necesarias para saber si hay que 
			presentar m�s p�ginas.

			Para saber que hay que presentar m�s p�ginas, comprobamos si el n�mero de
			habitantes que contiene el fichero xml es superior al n�mero de habitantes
			que hemos presentado. Esta comparaci�n es la siguiente:

				- Si (n�mero_de_habitantes > (pg + 1) * 5) ...
				
			El n�mero de p�gina comienza en 0, por eso, tenemos que sumarle 1, y 5 debido
			a que se presentan 5 habitantes por p�gina.

			En el caso de que a�n no se hayan presentado todos los habitantes, se
			llama de nuevo a la plantilla pasando como par�metro, el n�mero de p�gina
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
								Ayuntamiento de <xsl:value-of select="datosCabecera/CTE"/>
							</td>
							<td align="right" width="50%" style="font-size:12;font-weight:bold;">
								Hoja de inscripci�n o modificaci�n
							</td>
						</tr>
						<tr>
							<td width="50%" style="font-size:12;font-weight:bold;">
								Padr�n Municipal
							</td>
							<td align="right" width="50%">
								Hoja <xsl:value-of select="($pg+1)"/> de <xsl:value-of select="($tpg)"/>
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
										&nbsp;Calle,plaza,etc
									</td>
									<td style="font-size:6;">
										Nombre v�a
									</td>
								</tr>
								<tr>
									<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
										&nbsp;<xsl:value-of select="datosCabecera/ABR"/>�
									</td>
									<td style="font-size:8;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/VIA"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										&nbsp;N.Desde
									</td>
									<td style="font-size:6;">
										L.Desde
									</td>
									<td style="font-size:6;">
										N.Hasta
									</td>
									<td style="font-size:6;">
										L.Hasta
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
										Puerta
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
									A cumplimentar por el Ayuntamiento
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
										Municipio:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/MUN"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Entidad colectiva:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/ECO"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Entidad singular:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/ESI"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										N�cleo / Diseminado:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/NUC"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Tipo de vivienda:
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
										Secci�n:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/SEC"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Manzana:
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
										C�digo de v�a:
									</td>
									<td style="font-size:7;font-weight:bold;font-variant:small-caps;">
										<xsl:value-of select="datosCabecera/VIACOD"/>
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										Inscripci�n:
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
										Informaci�n Voluntaria
									</td>
								</tr>
								<tr>
									<td style="font-size:6;" colspan="4">
										Autorizamos a los mayores de edad empadronados en esta hoja para comunicar al Ayuntamiento las futuras variaciones de nuestros datos y obtener
									</td>
								</tr>
								<tr>
									<td style="font-size:6;">
										certificaciones o volantes de empadronamiento.
									</td>
									<td style="font-size:6;">
										SI		
									</td>
									<td style="font-size:6;">
										NO		
									</td>
									<td style="font-size:7;">
										Tel�fono: <xsl:value-of select="datosCabecera/TEL"/>
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
								N�Orden
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
											Nombre
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
											1� Apellido
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
											2� Apellido
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
											Hombre
										</td>
										<td>
											<xsl:choose>
												<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/SEX = 'H'">x</xsl:when>
												<xsl:otherwise>�</xsl:otherwise>
											</xsl:choose>
										</td>
										<td>
											Mujer
										</td>
										<td>
											<xsl:choose>
												<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/SEX = 'M'">x</xsl:when>
												<xsl:otherwise>�</xsl:otherwise>
											</xsl:choose>
										</td>
									</tr>
									<tr>
										<td colspan="4" height="4mm">
											Si se trata de un Alta por traslado de residencia indique:
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
											Municipio (o Consulado) de procedencia
										</td>
									</tr>
									<tr>
										<td style="font-size:10px;" height="4mm">
											<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/MNO"/>
										</td>
									</tr>
									<tr>
										<td height="4mm">
											Provincia (o Pa�s) de procedencia
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
											Fecha de Nacimiento(d�a,mes,a�o)
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
											Municipio (o Pa�s) de Nacimiento
										</td>
									</tr>
									<tr>
										<td height="4mm" style="font-size:10px;font-weight:bold;font-variant:small-caps;">
											<xsl:choose>
												<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PRN = ''"><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/PAN"/>�</xsl:when>
												<xsl:otherwise><xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/MNA"/>�</xsl:otherwise>
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
											Pa�s de Nacionalidad
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
								Tipo de documento de Identidad
							</td>
						</tr>
						<tr>
							<td>
								D.N.I.
							</td>
							<td style="font-size:10px;font-weight:bold;font-variant:small-caps;">
								<xsl:choose>
									<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 1">x</xsl:when>
									<xsl:otherwise>�</xsl:otherwise>
								</xsl:choose>
							</td>
							<td>
								Pasaporte
							</td>
							<td style="font-size:10px;font-weight:bold;font-variant:small-caps;">
								<xsl:choose>
									<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 2">x</xsl:when>
									<xsl:otherwise>�</xsl:otherwise>
								</xsl:choose>
							</td>
							<td>
								Tarj.Res.
							</td>
							<td style="font-size:10px;font-weight:bold;font-variant:small-caps;">
								<xsl:choose>
									<xsl:when test="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TID = 3">x</xsl:when>
									<xsl:otherwise>�</xsl:otherwise>
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
								Nivel de estudios terminados:
							</td>
							<td colspan="2" style="font-size:10px;font-weight:bold;font-variant:small-caps;">
								<xsl:value-of select="datosHabitante/habitante[position() = ($nh + ($pg * 5))]/TIT"/>�
							</td>
						</tr>
						<tr>
							<td colspan="6">
								(ver c�digos en reverso)
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
					Declaramos que las personas relacionadas en esta hoja residen en el domicilio indicado y que sus datos son correctos
				</td>
				<td width="15%" rowspan="2">
					<table cellborder="0" cellpadding="1" width="100%">
						<tr>
							<td align="center">
								A cumplimentar por el Ayuntamiento
							</td>
						</tr>
						<tr>
							<td align="center">
								Fecha de recepci�n de la hoja
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
								N�mero total de
							</td>
						</tr>
						<tr>
							<td align="center">
								personas inscritas en
							</td>
						</tr>
						<tr>
							<td align="center">
								esta hoja
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
					Firma de los mayores de edad que se inscriben en esta hoja
				</td>
				<td width="15%">
					<table cellborder="0" cellpadding="1" width="100%">
						<tr>
							<td align="center">
								(Para altas en inscripciones existentes)
							</td>
						</tr>
						<tr>
							<td align="center">
								Firma de persona mayor de edad
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
			PADR�N MUNICIPAL DE HABITANTES
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
			La Ley 4/1996, de 10 de enero, por la que se modifica la Ley 7/1985, de 2 de abril, reguladora de las Bases del R�gimen Local, en relaci�n con el Padr�n municipal, y el Real Decreto 2612/1996, de 20 de diciembre, por el que se modifica el Reglamento de Poblaci�n y Demarcaci�n Territorial de las Entidades Locales aprobado por Real Decreto 1690/1986, de 11 de julio.
			<br/>El Padr�n es el registro que acredita la residencia y el domicilio de los vecinos del municipio a todos los efectos administrativos.
			<br/>Toda persona que viva en Espa�a est� obligada a inscribirse en el Padr�n del municipio donde reside habitualmente. Quienes vivan en m�s de un municipio se inscribir�n en el que residan durante m�s tiempo al a�o.
			<br/>El vecino tiene derecho a conocer la informaci�n que consta en el Padr�n sobre su persona y a exigir su rectificaci�n cuando sea err�nea o incorrecta.
			<br/>Los datos que se hagan constar en el anverso de esta hoja permitir�n la actualizaci�n del Padr�n de su municipio.
			</td>
		</tr>
		<tr>
			<td width="100%" height="2mm" class="tituloReverso"></td>
		</tr>			
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			C�DIGOS DE NIVEL DE INSTRUCCI�N
			</td>
		</tr>
		<tr>
			<td width="100%" class="textoReverso">
				11. No sabe leer ni escribir.
				<br/>21. Sin estudios.
				<br/>22. Ense�anza primaria incompleta, cinco cursos de EGB, Certificado de escolaridad o equivalente.
				<br/>31. ESO, Bachiller elemental, Graduado escolar, EGB completa, Primaria completa o equivalente.
				<br/>32. Formaci�n Profesional primer grado. Oficial�a industrial.
				<br/>41. Formaci�n Profesional de segundo grado. Maestr�a industrial.
				<br/>42. Bachiller superior, BUP.
				<br/>43. Otros titulados medios (Auxiliar de cl�nica, Secretariado, Programador inform�tico, Auxiliar de vuelo, Diplomado en artes y oficios, etc.).
				<br/>44. Diplomado de Escuelas universitarias (Empresariales, Profesorado de EGB, ATS y similares.
				<br/>45. Arquitecto o Ingeniero t�cnico.
				<br/>46. Licenciado universitario, Arquitecto o Ingeniero superior.
				<br/>47. Titulados de estudios superiores no universitarios.
				<br/>48. Doctorado y estudios de postgrado o especializaci�n para licenciados.
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
				- Escriba con pluma o bol�grafo y en letras may�sculas. Indique con claridad todos los datos que corresponden a cada una de las personas que se inscriben en la hoja.
				<br/>- Si se solicita la inscripci�n por traslado de residencia, indique en la casilla correspondiente el municipio y provincia de procedencia. Si procede del extranjero, indique el p�is y, en su caso, el Consulado espa�ol donde estaba inscrito. En el caso de traslado de domicilio dentro de este mismo municipio indique en esta casilla: MISMO MUNICIPIO.
				<br/>- Si se trata de la inscripci�n de una persona que no estuviera empadronada con anterioridad en ning�n municipio indique en la casilla de municipio de procedencia: NINGUNO. En la misma forma se cumplimentar� esta casilla para la inscripci�n de reci�n nacidos.
				<br/>- Para la correcci�n y actualizaci�n de los datos que constan en su inscripci�n padronal, tache el dato incorrecto y escriba el correcto en el espacio o casilla situado debajo de �l.
				<br/>- La hoja cumplimentada debe ser firmada por la persona que realiza el tr�mite, si su objeto es la correcci�n o actualizaci�n de datos. Cuando se trate de una nueva hoja de inscripci�n, firmar�n todos los mayores de edad que se inscriban. Si se incorporan nuevos habitantes a una hoja existente, adem�s deber� firmar la autorizaci�n de las nuevas inscripciones alguna persona mayor de edad que ya figuraba inscrita en la hoja.
				<br/>- Rellene la casilla "N�mero total de personas inscritas en esta hoja", para garantizar la inalterabilidad de los espacios que hayan podido quedar en blanco.
				<br/>- Si tiene alguna duda le ser� resuelta en el momento de presentar la hoja en su Ayuntamiento.
				<br/>&nbsp;&nbsp;<b>La presentaci�n de esta hoja cumplimentada en su Ayuntamiento implica su conformidad para actualizar el Censo Electoral en consonancia con los datos reflejados en ella.</b>
			</td>
		</tr>
		<tr>
			<td width="100%" height="2mm" class="tituloReverso"></td>
		</tr>			
		<tr>
			<td width="100%" height="5mm" class="tituloReverso">
			DOCUMENTACI�N A PRESENTAR PARA REALIZAR EL EMPADRONAMIENTO
			</td>
		</tr>
		<tr>
			<td width="100%" class="textoReverso">
				- Hoja padronal cumplimentada y firmada.
				<br/>- Documento que acredite la identidad de las personas inscritas (D.N.I., Tarjeta de extranjero, Pasaporte, etc. Libro de familia para los menores de 15 a�os).
				<br/>- Documento que acredite la ocupaci�n de la vivienda (escritura de propiedad, contrato de arrendamiento, contrato o recibo actual expedido por Compa��a suministradora de tel�fono, agua, electricidad, etc.). Este documento no es necesario en el caso de incorporaci�n de nuevos habitantes a un grupo familiar ya empadronado.
				<br/>- En el caso de rectificaci�n o actualizaci�n de datos, documento acreditativo del dato correcto. 
			</td>
		</tr>
		</table>
	</xsl:template>

	<xsl:template match="PA1">
		<xsl:if test=". != ''">
			<xsl:value-of select="."/>�
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="PA2">
		<xsl:if test=". != ''">
			<xsl:value-of select="."/>�
		</xsl:if>
	</xsl:template>


</xsl:stylesheet>

