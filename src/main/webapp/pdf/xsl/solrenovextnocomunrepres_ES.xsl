<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--
	Desde aqu� se lanza la ejecuci�n del certificado para la primera familia.
	El resto de las familias se imprimen utilizando "recursi�n".
	Al menos habr� una familia.
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
						<td class="cabecera1I" width="120mm"><b>Padr�n Municipal - <xsl:value-of select="ayto"/> (<xsl:value-of select="concello"/>)</b></td>
						<td class="cabecera1D" width="80mm" border="1"><p align="center">
                            <b>SOLICITUD DE RENOVACI�N DE LA INSCRIPCI�N PADRONAL PARA EXTRANJEROS NO COMUNITARIOS SIN
                                AUTORIZACI�N DE RESIDENCIA PERMANENTE, PRESENTADA POR REPRESENTANTE
                            <!--<xsl:value-of select="titulo"/>-->
                            </b></p></td>
					</tr>
				</table>

				<table border="0" align="center" width="200mm">
					<tr>
						<td align="center" valign="bottom">DATOS DE IDENTIFICACI�N DEL INTERESADO, SOLICITANTE DE LA RENOVACI�N</td>
					</tr>
                </table>
                <table align="center" width="200mm">
                    <tr>
						<td>
							<xsl:call-template name="dh">
								<xsl:with-param name="nh">1</xsl:with-param>
								<xsl:with-param name="nf"><xsl:value-of select="$nf"/></xsl:with-param>
								<xsl:with-param name="pg"><xsl:value-of select="$pg"/></xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
                    <tr>
                        <td height="10mm"></td>
                    </tr>
                    <tr>
                        <td>
                        <table border="1" align="center" width="200mm">
					        <tr>
                                <td align="center" valign="bottom">DATOS DE IDENTIFICACI�N DEL REPRESENTANTE</td>
                            </tr>
                        </table>
                        <table border="1" align="center" width="200mm">
                            <tr>
                                <td  class="textoFijo1_bi2" colspan="3"> NOMBRE:</td>
                            </tr>
                            <tr>
                                <td  class="textoFijo1_bi2" width="100mm"> 1� APELLIDO:</td>
                                <td  class="textoFijo1_bi2" width="100mm" colspan="2"> 2� APELLIDO:</td>
                            </tr>
                            <tr>
                                <td  class="textoFijo1_bi2" colspan="3"> DOMICILIO:</td>
                            </tr>
                            <tr>
                                <td  class="textoFijo1_bi2" width="35mm"> MUNICIPIO: </td>
                                <td  class="textoFijo1_bi2" width="35mm"> PROVINCIA: </td>
                                <td  class="textoFijo1_bi2" width="30mm"> CP: </td>
                            </tr>
                            <tr>
                                <td  class="textoFijo1_bi2" colspan="3"> DNI, Tarjeta de extranjero o Pasaporte:</td>
                            </tr>
                        </table>
                        </td>
                    </tr>

                    <tr>
                        <td height="10mm"></td>
                    </tr>
					<tr>
						<td>
							<table width="200mm" border="0">
								<tr>
									<td class="textoDiligencia">
								    MANIFIESTA:
                                    <p>1� Que ostenta la reprentaci�n de la persona arriba referenciada para
                                    cumplimientar el presente tr�mite, en virtud de (**):____________________________________________________________________________________________________________</p>
                                    <p>____________________________________________________________________________________________________________</p>
                                    <p>____________________________________________________________________________________________________________</p>
                                    <p>
                                    2� Que mi representado contin�a residiendo habitualmente en este municipio y que, a
                                        efectos de dar cumplimiento a lo dispuesto en el art�culo 16.1, 2� p�rrafo, de
                                        la Ley 7/1985, de 2 de abril, Reguladora de las Bases de R�gimen Local, insta
                                        la renovaci�n de la inscripci�n padronal;
                                    </p>
                                    </td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
					<td height="10mm"></td>
				   </tr>
					<tr>
						<td align="center" class="textoDiligencia">En <xsl:value-of select="ayto"/>, a <xsl:value-of select="fechaDocumento"/>.</td>
					</tr>
					<tr>
						<td height="15mm"></td>
					</tr>
					<tr>
						<td>
							<table width="200mm">
								<tr>
									<td width="110mm">Ilmo. Sr. Alcalde del Ayuntamiento de <xsl:value-of select="ayto"/></td>

								</tr>
							</table>
						</td>
					</tr>
                    <tr>
						<td height="10mm"></td>
					</tr>
                    <tr border="1" height="2mm"></tr>
                    <tr>
                        <td>
                            <p>* N�mero de identificaci�n de extranjero que conste, en vigor, expedido por las autoridades Espa�olas</p>
                            <p>** Especificar documento acreditativo de la representaci�n conforme al apartado 5 de la Resolucion</p>
                        </td>
                    </tr>
                </table>

				<!--
				Comprobar  si hay que presentar m�s p�ginas para esa familia.
				Para ello, comprobamos si el n�mero de habitantes que contiene esa familia es superior al n�mero de habitantes presentado.

					 (n�mero_de_habitantes > (pg + 1) * 5)

				El n�mero de p�gina comienza en 0, por eso, tenemos que sumarle 1, y multiplicar por 5 (se presentan 5 habitantes por p�gina).
				En el caso de que a�n no se hayan presentado todos los habitantes, se llama de nuevo a la plantilla pasando como par�metro, el n�mero de p�gina incrementado en 1.
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
			El indice del habitante que hay que presentar viene determinado por 3 par�metros:
					- La familia que se est� formando (par�metro nf).
					- La p�gina que se est� formando (par�metro pg).
					- La posici�n dentro de la p�gina (par�metro nh).

			El n�mero de p�gina a presentar comienza en 0, para facilitar los c�lculos.
			Se presentan 5 habitantes en cada p�gina.
			El �ndice del habitante que hay que presentar, se forma con la siguiente
			operaci�n:

					- indice = nh + (pg * 5) (Dentro de la correspondiente familia)

			-->
			<xsl:template name="dh">
				<xsl:param name="nf"/>
				<xsl:param name="nh" />
				<xsl:param name="pg" />

				<table width="200mm" border="1">
					<tr>
                        <td width="100mm"  class="textoFijo1_bi2" >
							<table>
								<tr>
									<td height="3mm">Nombre</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/nombreSimple"/></td>
								</tr>
							</table>
						</td>
						<td width="100mm" class="textoFijo1_bi2">
							<table>
								<tr>
									<td height="3mm">Sexo</td>
								</tr>
								<tr>
                                    <td width="10mm" border="1" valign="top" align="center" padding-bottom="0">
										<xsl:if test="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/sexo = 'H'">
											<p class="datos">X</p>
										</xsl:if>
									</td>
                                    <td width="40mm">HOMBRE</td>

									<td width="10mm" border="1" align="center" padding-bottom="0">
										<xsl:if test="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/sexo = 'M'">
											<p class="datos">X</p>
										</xsl:if>
									</td>
                                    <td width="40mm">MUJER</td>
                                </tr>
                                <tr>
									<td height="1mm"></td>
								</tr>
                            </table>
						</td>
                    </tr>
                    <tr>
                        <td width="100mm" class="textoFijo1_bi2" >
							<table>
								<tr>
									<td height="3mm">1� Apellido</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/apellido1"/></td>
								</tr>
							</table>
						</td>
						<td width="100mm" class="textoFijo1_bi2">
							<table>
								<tr>
									<td height="3mm">2� Apellido</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/apellido2"/></td>
								</tr>
							</table>
						</td>
                    </tr>
                    <tr>
                        <td width="200mm" colspan="2" class="textoFijo1_bi2">
							<table>
								<tr>
									<td height="3mm">Fecha de Nacimiento</td>
                                </tr>
                                <tr>
                                    <td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/fechaNacimiento"/></td>
								</tr>
							</table>
						</td>
                    </tr>
                    <tr>
                        <td width="100mm" colspan = "2" class="textoFijo1_bi2">
							<table>
								<tr>
									<td height="3mm">Pais de Nacimiento</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/lugarNacimiento"/></td>
								</tr>
							</table>
                        </td>

                     </tr>
                     <tr>
                         <td width="200mm" colspan="2" class="textoFijo1_bi2" >
                            <table>
								<tr>
									<td height="3mm">Nacionalidad</td>
								</tr>
								<tr>
									<td class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/paisNacionalidad"/></td>
								</tr>
							</table>
						</td>
                    </tr>
                    <tr>
                        <td width="100mm"  class="textoFijo1_bi2" border="1">
							Tipo de Documento de Identidad
							<table width="100mm" align="bottom">
								<tr>
                                    <td width="30mm"></td>

                                    <td width="5mm" border="1" align="center" padding-bottom="0">
										<xsl:if test="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/tipoDocumento = '2'">
											<p class="datos">X</p>
										</xsl:if>
									</td>
									<td width="30mm">Pasaporte</td>

                                    <td width="5mm" border="1" align="center" padding-bottom="0">
										<xsl:if test="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/tipoDocumento = '3'">
											<p class="datos">X</p>
										</xsl:if>
									</td>
                                    <td width="30mm">NIE(*)</td>
								</tr>
                                <tr>
									<td height="1mm"></td>
								</tr>
                            </table>
						</td>

						<td width="100mm" class="textoFijo1_bi2" valign="bottom">
							<table width="100mm" align="bottom">
								<tr>
	    							<td width="25mm">NUMERO</td>
	    							<td width="30mm" align="left" border="1" padding-bottom="0"><p class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/numeroDocumento"/></p></td>

									<td width="15mm">LETRA</td>
									<td width="10mm" align="left" border="1" padding-bottom="0"><p class="datos"><xsl:value-of select="familia[position() = $nf]/datosHabitante/habitante[position() = ($nh + ($pg * 5))]/letraDocumento"/></p></td>

                                    <td width="20mm"></td>
                                </tr>
                                <tr>
									<td height="1mm"></td>
								</tr>
                            </table>
						</td>
					</tr>
				</table>
			</xsl:template>
</xsl:stylesheet>
