<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/historial">
	    
		<table width="255mm" padding="1" valign="top" align="center">
			<tr>
				
				<td class="titulo" width="30%"><xsl:value-of select="nombre"/></td>
				<td border = "1" class="titulo" align="center" width="40%">HISTORIAL DE OPERACIÓNS</td>
				<td class="titulo" width="15%" align="right">D.E.&nbsp;<xsl:value-of select="fecEmpadronamiento"/></td>																
				<td class="titulo" width="15%" align="right">N.I.A.&nbsp;<xsl:value-of select="nia"/></td>																
			</tr>
		</table>
		<table width="255mm" padding="1" border="0"  align="center">
	  		<tr>
	  			<td>
					<table width="255mm" padding="1" border="0" valign="middle" align="center">
						<tr>
							<td class="etiqueta" width="85%">Operación</td>
							<td class="etiqueta" width="15%">Data da Operación</td>																
						</tr>
					</table>		  			
	  			</td>
	  		</tr>
			<tr>					
				<td>		
					<table width="255mm" padding="0" border="1" valign="middle" align="center">
						<tr>
							<td class="etiqueta" width="5%">Distrito</td>
							<td class="etiqueta" width="15%">Apelido1</td>
							<td class="etiqueta" width="25%">Teléfono</td>
							<td class="etiqueta" width="15%">Data Nacimiento</td>
							<td class="etiqueta" width="30%">Enderezo</td>
							<td class="etiqueta" width="10%">Procedencia/Destino</td>			   
						</tr>
						<tr>
							<td class="etiqueta" width="5%">Sección</td>
							<td class="etiqueta" width="15%">Apelido2</td>
							<td class="etiqueta" width="25%">N.I.E.</td>
							<td class="etiqueta" width="15%">Provincia Nacimiento</td>
							<td class="etiqueta" width="30%">Entidade Singular</td>
							<td class="etiqueta" width="10%">Provincia</td>			   
						</tr>
						<tr>
							<td class="etiqueta" width="5%">Folla</td>
							<td class="etiqueta" width="15%">Nome</td>
							<td class="etiqueta" width="25%">Titulación</td>
							<td class="etiqueta" width="15%">Concello Nacimiento</td>
							<td class="etiqueta" width="30%">Entidade Colectiva</td>
							<td class="etiqueta" width="10%">Municipio</td>			   
						</tr> 
						<tr>
							<td class="etiqueta" width="5%">Orde</td>
							<td class="etiqueta" width="15%">N.I.F.</td>
							<td class="etiqueta" width="25%">Sexo</td>
							<td class="etiqueta" width="15%">Nacionalidade</td>
							<td class="etiqueta" width="30%">Nucleo</td>
							<td class="etiqueta" width="10%">Consulado</td>			   
						</tr>																																																		
					</table>
				</td>
			</tr>
		</table>		
		<xsl:for-each select="operacion">
		<table width="255mm" padding="1" border="0"  align="center">
	  		<tr>
	  			<td>
					<table width="255mm" padding="1" border="0" valign="middle" align="center">
						<tr>
							<td style="font-weight:bold;" width="85%"><xsl:value-of select="desOpe"/></td>
							<td style="font-weight:bold;" width="15%"><xsl:value-of select="fecOpe"/></td>																
						</tr>
					</table>		  			
	  			</td>
	  		</tr>
			<tr>	
				<td>
					<table width="255mm" padding="0" border="1" valign="middle" align="center">
						<tr>
							<td  width="5%"><xsl:value-of select="hojPad/distrito"/></td>
							<td  width="15%"><xsl:value-of select="datPer/apellido1"/></td>
							<td  width="25%"><xsl:value-of select="datPer/telefono"/></td>
							<td  width="15%"><xsl:value-of select="datPer/fecnac"/></td>
							<td  width="30%"><xsl:value-of select="hojPad/domicilio"/></td>
							<xsl:if test="opcion='1'">
							   <td style="font-weight:bold;" width="10%">PROCEDENCIA</td>
							</xsl:if>
							<xsl:if test="opcion='2'">
							   <td style="font-weight:bold;" width="10%">DESTINO</td>
							</xsl:if>
							<xsl:if test="opcion='0'">
							   <td width="10%">&nbsp;</td>
							</xsl:if>																								   
						</tr>
						<tr>
							<td  width="5%"><xsl:value-of select="hojPad/seccion"/></td>
							<td  width="15%"><xsl:value-of select="datPer/apellido2"/></td>
							<td  width="25%"><xsl:value-of select="datPer/nie"/></td>
							<td  width="15%"><xsl:value-of select="datPer/provincia"/></td>
							<td  width="30%"><xsl:value-of select="hojPad/entSingular"/></td>
							<xsl:if test="opcion='1'">
							   <td width="10%"><xsl:value-of select="datPro/provincia"/></td>
							</xsl:if>
							<xsl:if test="opcion='2'">
							   <td width="10%"><xsl:value-of select="datDes/provincia"/></td>
							</xsl:if>
							<xsl:if test="opcion='0'">
							   <td width="10%">&nbsp;</td>
							</xsl:if>									   
						</tr>
						<tr>
							<td  width="5%"><xsl:value-of select="hojPad/hoja"/></td>
							<td  width="15%"><xsl:value-of select="datPer/nombre"/></td>
							<td  width="25%"><xsl:value-of select="datPer/titulacion"/></td>
							<td  width="15%"><xsl:value-of select="datPer/municipio"/></td>
							<td  width="30%"><xsl:value-of select="hojPad/entColectiva"/></td>
							<xsl:if test="opcion='1'">
							   <td width="10%"><xsl:value-of select="datPro/municipio"/></td>
							</xsl:if>
							<xsl:if test="opcion='2'">
							   <td width="10%"><xsl:value-of select="datDes/municipio"/></td>
							</xsl:if>
							<xsl:if test="opcion='0'">
							   <td width="10%">&nbsp;</td>
							</xsl:if>		   
						</tr>
						<tr>
							<td  width="5%"><xsl:value-of select="hojPad/orden"/></td>
							<td  width="15%"><xsl:value-of select="datPer/valDoc"/></td>
							<td  width="25%"><xsl:value-of select="datPer/sexo"/></td>
							<td  width="15%"><xsl:value-of select="datPer/nacionalidad"/></td>
							<td  width="30%"><xsl:value-of select="hojPad/nucleo"/></td>
							<xsl:if test="opcion='1'">
							   <td width="10%"><xsl:value-of select="datPro/consulado"/></td>
							</xsl:if>
							<xsl:if test="opcion='2'">
							   <td width="10%"><xsl:value-of select="datDes/consulado"/></td>
							</xsl:if>
							<xsl:if test="opcion='0'">
							   <td width="10%">&nbsp;</td>
							</xsl:if>		   
						</tr>																																																								
					</table>
				</td>
			</tr>
		  </table>
		  </xsl:for-each>		  
	</xsl:template>
</xsl:stylesheet>