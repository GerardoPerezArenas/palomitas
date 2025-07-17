<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- 
	Desde aquí se lanza la ejecución del certificado para la primera familia. 
	El resto de las familias se imprimen utilizando "recursión".
	Al menos habrá una familia.
-->

	<xsl:template match="//certificado" > 
		<xsl:call-template name="df">
			<xsl:with-param name="nf">1</xsl:with-param> 
		</xsl:call-template>
  </xsl:template> 


		<xsl:template name="df">
			<xsl:param name="nf">1</xsl:param> 
			<xsl:param name="tpg"><xsl:value-of select="ceiling( count(familia[position()=$nf]/datosHabitante/habitante)div 5)"/></xsl:param> 

			<xsl:call-template name="pagina">
				<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param> 
				<xsl:with-param name="tpg"><xsl:value-of select="$tpg"/></xsl:with-param> 
			</xsl:call-template>

			<!-- familia siguiente -->		
			<xsl:if test="count(familia) &gt; ($nf)">
				<xsl:call-template name="df">
					<xsl:with-param name="nf"><xsl:value-of select="$nf + 1"/></xsl:with-param> 
				</xsl:call-template>
			</xsl:if>
		</xsl:template>

			<xsl:template name="pagina">
				<xsl:param name="nf">1</xsl:param> 
				<xsl:param name="pg">0</xsl:param> 
				<xsl:param name="tpg">1</xsl:param> 
		
				<table border="0" align="center" width="200mm" height="12mm">
				<tr>
					<td class="cabecera1I" width="140mm" align="center"><b>Padrón Municipal - <xsl:value-of select="ayto"/> (<xsl:value-of select="concello"/>)</b></td>
					<td class="cabecera1D" width="60mm" border="1"><p align="center"><b><xsl:value-of select="titulo"/></b></p></td>
				</tr>
				</table>

				<table border="0" align="center" width="200mm">
				<tr><td height="8mm"></td></tr>
				<tr>
                    <xsl:choose>
                        <xsl:when test="tipoCertificado = 'individual'">
                            <td class="textoDiligencia" > No Padrón Municipal de Habitantes deste municipio aparece, no día da
                                 data e na folla que se indica, a inscripción cos datos que se recollen neste volante:</td>
                        </xsl:when>
                        <xsl:otherwise>
                            <td class="textoDiligencia" > No Padrón Municipal de Habitantes deste municipio aparecen, no día da
						 data e na folla que se indica, as inscripcións cos datos que se recollen neste volante:</td>
                        </xsl:otherwise>
				    </xsl:choose>

				</tr>
				<tr><td height="2mm"></td></tr>
				<tr>
					<td valign="bottom">DATOS DO PADRÓN MUNICIPAL
				</td>
				</tr>
				<tr>
					<td>
						<table width="200mm">
							<tr>
								<td width="66mm" class="textoFijo1_ai">
									<table>
										<tr>
											<td height="3mm">Entidade Colectiva</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosPadron/entidadColectiva"/></td>
										</tr>
									</table>
								</td>
								<td width="66mm" class="textoFijo1_ai">
									<table>
										<tr>
											<td height="3mm">Entidade Singular</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosPadron/entidadSingular"/></td>
										</tr>
									</table>
								</td>
								<td width="68mm" class="textoFijo1_ad">
									<table>
										<tr>
											<td height="3mm">Núcleo / Diseminado</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosPadron/nucleo"/></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="66mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Distrito</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosPadron/distrito"/></td>
										</tr>
									</table>
								</td>
								<td width="66mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Sección</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosPadron/seccion"/></td>
										</tr>
									</table>
								</td>
								<td width="68mm" class="textoFijo1_bd">
									<table>
										<tr>
											<td height="3mm">Folla Padroal</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosPadron/hojaPadronal"/></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="bottom">DATOS DA VIVENDA</td>
				</tr>
				<tr>
					<td>
						<table width="200mm">
							<tr>
								<td width="26mm" colspan="2" class="textoFijo1_ai">
									<table>
										<tr>
											<td height="3mm">Tipo de Vía</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/tipoVia"/></td>
										</tr>
									</table>
								</td>
								<td width="174mm" colspan="9" class="textoFijo1_ad">
									<table>
										<tr>
											<td height="3mm">Nome da Vía</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/nombreVia"/></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td width="20mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Número</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/numero1"/></td>
										</tr>
									</table>
								</td>
								<td width="12mm" class="textoFijo1_bi" colspan="2">
									<table>
										<tr>
											<td height="3mm">Letra</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/letra1"/></td>
										</tr>
									</table>
								</td>
								<td width="20mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Número</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/numero2"/></td>
										</tr>
									</table>
								</td>
								<td width="12mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Letra</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/letra2"/></td>
										</tr>
									</table>
								</td>
								<td width="22mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Km</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/km"/></td>
										</tr>
									</table>
								</td>
								<td width="22mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Bloque</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/bloque"/></td>
										</tr>
									</table>
								</td>
								<td width="22mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Portal</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/portal"/></td>
										</tr>
									</table>
								</td>
								<td width="22mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Escaleira</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/escalera"/></td>
										</tr>
									</table>
								</td>
								<td width="22mm" class="textoFijo1_bi">
									<table>
										<tr>
											<td height="3mm">Planta</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/piso"/></td>
										</tr>
									</table>
								</td>
								<td width="22mm" class="textoFijo1_bd">
									<table>
										<tr>
											<td height="3mm">Porta</td>
										</tr>
										<tr>
											<td class="datos"><xsl:value-of select="datosVivienda/puerta"/></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="bottom">DATOS DO HABITANTE</td>
				</tr>
				<tr>
					<td>
						<xsl:call-template name="dh">
							<xsl:with-param name="nh">1</xsl:with-param>
							<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param>
							<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
                              <xsl:choose>
								<xsl:when test="tipoCertificado = 'individual'">
                                   <tr><td height="80mm"></td></tr>
								</xsl:when>
								<xsl:otherwise>
                <tr>
					<td>
						<xsl:call-template name="dh">
								<xsl:with-param name="nh">2</xsl:with-param>
								<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param>
								<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:call-template name="dh">
								<xsl:with-param name="nh">3</xsl:with-param>
								<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param>
								<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:call-template name="dh">
								<xsl:with-param name="nh">4</xsl:with-param>
								<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param>
								<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:call-template name="dh">
								<xsl:with-param name="nh">5</xsl:with-param>
								<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param>
								<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
                       </xsl:otherwise>
							</xsl:choose>

                    <xsl:choose>
                               <xsl:when test="count(familia[position() = $nf]/datosHabitante/habitante)- (($pg + 1) * 5) &lt; 1">

                <tr>
					<td>
						<table width="200mm">
							<tr>
								<td class="textoFijo2">
									Esta é a folla <xsl:value-of select="$pg+1"/> da presente certificación extendida en <xsl:value-of select="$tpg"/> hojas.
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td valign="bottom">EFECTO PARA O QUE SE EXPIDE</td>
				</tr>
				<tr>
					<td>
						<table width="200mm">
							<tr>
								<td width="128mm" border="1">
										<xsl:if test="not(efectoExpedicion='')"><xsl:value-of select="efectoExpedicion"/><br/></xsl:if>
										<xsl:value-of select="observaciones"/>
								</td>
								<td width="10mm"></td>
								<td width="47mm"><p align="left">NÚMERO DE PERSOAS QUE COMPRENDE ESTA CERTIFICACIÓN</p></td>
								<td width="15mm" border="1">
									<xsl:value-of select="count(familia[position() = $nf]/datosHabitante/habitante)"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="3mm"></td>
				</tr>

				<tr>
					<td align="left">En <xsl:value-of select="ayto"/>, a <xsl:value-of select="fechaDocumento"/>.</td>
				</tr>
				<tr>
					<td height="12mm"></td>
				</tr>
				<tr>
					<td>
						<table width="300mm" height="20mm">
							<tr>
								<td >
									NOTA: Este documento ten carácter informativo en relación coa residencia e o domicilio habitual neste Municipio, de acordo co establecido no artigo do R.D. 2612/1996, de 20 de dicembro polo que se modifica o Regulamento de Poboación e Demarcación Territorial das Entidades Locais aprobado por R.D. 1690/1986, de 11 de xuño.
								</td>
							</tr>
						</table>
					</td>
				</tr>
                               </xsl:when>
                               <xsl:otherwise>



                                  <tr>
                                   <td height="65mm"></td>
                                  </tr>

                                               </xsl:otherwise>

                                            </xsl:choose>
                    
            </table>

			<!--
			Comprobar  si hay que presentar más páginas para esa familia.
			Para ello, comprobamos si el número de habitantes que contiene esa familia es superior al número de habitantes presentado.

				 (número_de_habitantes > (pg + 1) * 5)

			El número de página comienza en 0, por eso, tenemos que sumarle 1, y multiplicar por 5 (se presentan 5 habitantes por página).
			En el caso de que aún no se hayan presentado todos los habitantes, se llama de nuevo a la plantilla pasando como parámetro, el número de página incrementado en 1.
			-->

				<xsl:if test="count(familia[position() = $nf]/datosHabitante/habitante) &gt; (($pg + 1) * 5)">
					<xsl:call-template name="pagina">
					<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param>
					<xsl:with-param name="pg"><xsl:value-of select="$pg + 1"/></xsl:with-param>
					</xsl:call-template>
				</xsl:if>

			</xsl:template>

			<!--
			Con este template se presentan los datos de un determinado habitante.
			El indice del habitante que hay que presentar viene determinado por 3 parámetros:
					- La familia que se está formando (parámetro nf).
					- La página que se está formando (parámetro pg).
					- La posición dentro de la página (parámetro nh).

			El número de página a presentar comienza en 0, para facilitar los cálculos.
			Se presentan 5 habitantes en cada página.
			El índice del habitante que hay que presentar, se forma con la siguiente
			operación:

					- indice = nh + (pg * 5) (Dentro de la correspondiente familia)

			-->
			<xsl:template name="dh">
				<xsl:param name="nf"/>
				<xsl:param name="nh" />
				<xsl:param name="pg" />

				<table width="200mm" border="1">
					<tr>
						<td width="12mm" class="textoFijo1_ai2">
							<table>
								<tr>
									<td height="3mm">Nº Orde</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/numOrden"/></td>
								</tr>
							</table>
						</td>
						<td width="104mm" colspan="3" class="textoFijo1_ai2">
							<table>
								<tr>
									<td height="3mm">Nome</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/nombre"/></td>
								</tr>
							</table>
						</td>
						<td width="32mm" class="textoFijo1_ai2">
							<table>
								<tr>
									<td height="3mm">Sexo</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/sexo"/></td>
								</tr>
							</table>
						</td>
						<td width="52mm" colspan="2" class="textoFijo1b">
							Tipo de Documento de Identidade
							<table width="47mm" align="bottom">
								<tr>
									<td width="8mm">D.N.I.</td>
									<td width="5mm" border="1" align="center" padding-bottom="0">
										<xsl:if test="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/tipoDocumento = '1'">
											<p class="datos">X</p>
										</xsl:if>
									</td>
									<td width="12mm">Pasaporte</td>
									<td width="5mm" border="1" align="center" padding-bottom="0">
										<xsl:if test="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/tipoDocumento = '2'">
											<p class="datos">X</p>
										</xsl:if>
									</td>
									<td width="12mm">Tarx. Extr.</td>
									<td width="5mm" border="1" align="center" padding-bottom="0">
										<xsl:if test="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/tipoDocumento = '3'">
											<p class="datos">X</p>
										</xsl:if>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td width="30mm" colspan="2" class="textoFijo1_bi2">
							<table>
								<tr>
									<td height="3mm">Data de Nacemento</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/fechaNacimiento"/></td>
								</tr>
							</table>
						</td>
						<td width="52mm" class="textoFijo1_bi2">
							<table>
								<tr>
									<td height="3mm">Lugar de Nacemento</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/lugarNacimiento"/></td>
								</tr>
							</table>
						</td>
						<td width="34mm" class="textoFijo1_bi2">
							<table>
								<tr>
									<td height="3mm">Provincia de Nacemento</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/provinciaNacimiento"/></td>
								</tr>
							</table>
						</td>
						<td width="32mm" class="textoFijo1_bi2">
							<table>
								<tr>
									<td height="3mm">País de Nacionalidade</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/paisNacionalidad"/></td>
								</tr>
							</table>
						</td>
						<td width="35mm" class="textoFijo1b">
							Número
							<table width="32mm" height="6mm" border="1">
								<tr>
									<td align="right" padding-bottom="0"><p class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/numeroDocumento"/></p></td>
								</tr>
							</table>
						</td>
						<td width="17mm" class="textoFijo1b">
							Letra
							<table width="9mm" height="6mm" border="1">
								<tr>
									<td align="center" padding-bottom="0"><p class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/letraDocumento"/></p></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</xsl:template>

</xsl:stylesheet>
