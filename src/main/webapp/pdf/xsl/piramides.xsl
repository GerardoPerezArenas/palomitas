<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:variable name="maximo">
		<xsl:call-template name="Max">
			<xsl:with-param name="list" select="//piramide/datosPiramide/dato/cantidad" /> 
			<xsl:with-param name="nMax" select="'1'" /> 
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="npro"><xsl:value-of select="ceiling ($maximo div 19)" /></xsl:variable>

	<xsl:template match="/">
		<xsl:variable name="nlineas"><xsl:value-of select="(//piramide/edadHasta - //piramide/edadDesde) div //piramide/intervalo + 1" /></xsl:variable>
			
		<table border="0" align="center"><tr><td><h1>Pirámide de población</h1></td></tr></table>

		<table><tr><td height="5mm"></td></tr></table>

		<table width="139mm" border="1" cellpadding="2" align="center">
			<tr>
				<td width="30mm">Rango de edades</td>
				<td width="37mm" align="right">Hombres</td>
				<td width="36mm" align="right">Mujeres</td>
				<td width="36mm" align="right">Total</td>
			</tr>
			<tr><td colspan="4" height="2mm"></td></tr>
			<xsl:call-template name="lineaNumerica"> 
				<xsl:with-param name="grupo">0</xsl:with-param> 
				<xsl:with-param name="numeroLineas"><xsl:value-of select="$nlineas"/></xsl:with-param> 
			</xsl:call-template>
			<tr><td colspan="4" height="2mm"></td></tr>
			<tr>
				<td>TOTALES</td>
				<td align="right"><xsl:value-of select="sum (//piramide/datosPiramide/dato/cantidad[../sexo='H'])"/></td>
				<td align="right"><xsl:value-of select="sum (//piramide/datosPiramide/dato/cantidad[../sexo='M'])"/></td>
				<td align="right"><xsl:value-of select="sum (//piramide/datosPiramide/dato/cantidad)"/></td>
			</tr>
		</table>

		<table><tr><td height="5mm"></td></tr></table>

		<table width="40mm" border="1" cellpadding="4" align="center">
			<tr>
				<td><img src="{//piramide/roja}"/></td>
				<td>Mujeres</td>
				<td><img src="{//piramide/azul}"/></td>
				<td>Hombres</td>
			</tr>
		</table>
		
		<table><tr><td height="5mm"></td></tr></table>

		<table width="178.2mm" border="1" cellpadding="2" align="left">
			<xsl:call-template name="linea"> 
				<xsl:with-param name="grupo">0</xsl:with-param> 
				<xsl:with-param name="numeroLineas"><xsl:value-of select="$nlineas"/></xsl:with-param> 
			</xsl:call-template>
		</table>
		<table width="178mm" border="0" cellpadding="0" align="left">
			<tr>
				<td width="18mm" height="2.1mm"></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
				<td width="8mm" height="2.1mm"><img src="{//piramide/regla}" width="7.8mm"/></td>
			</tr>
			<tr>
				<td></td>
				<xsl:call-template name="pintarEscala"> 
					<xsl:with-param name="indice">0</xsl:with-param> 
				</xsl:call-template>
			</tr>
		</table>
	</xsl:template>
		
	<xsl:template name="linea">
		<xsl:param name="grupo" /> 
		<xsl:param name="numeroLineas" /> 
		<xsl:variable name="auxEdadHasta"><xsl:value-of select="//piramide/edadDesde + (//piramide/intervalo * ($grupo + 1) - 1)"/></xsl:variable>
		<xsl:variable name="auxEdadDesde"><xsl:value-of select="//piramide/edadDesde + (//piramide/intervalo * $grupo)"/></xsl:variable>
		<xsl:if test="$grupo &lt; floor ($numeroLineas)">
			<tr>
				<td width="18mm" align="right" valign="middle" rowspan="2">
					<xsl:choose>
						<xsl:when test="$auxEdadHasta &gt; //piramide/edadHasta">
							<xsl:value-of select="$auxEdadDesde"/> - <xsl:value-of select="//piramide/edadHasta"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$auxEdadDesde"/> - <xsl:value-of select="$auxEdadHasta"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td width="160.2mm" class="centroGrafico">
					<xsl:call-template name="mostrarBarra"> 
						<xsl:with-param name="grupoBarra"><xsl:value-of select="$grupo"/></xsl:with-param>
						<xsl:with-param name="sexoBarra">H</xsl:with-param>
						<xsl:with-param name="barraBarra"><xsl:value-of select="//piramide/azul"/></xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td width="160.2mm" class="centroGrafico">
					<xsl:call-template name="mostrarBarra"> 
						<xsl:with-param name="grupoBarra"><xsl:value-of select="$grupo"/></xsl:with-param>
						<xsl:with-param name="sexoBarra">M</xsl:with-param>
						<xsl:with-param name="barraBarra"><xsl:value-of select="//piramide/roja"/></xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<xsl:call-template name="linea"> 
				<xsl:with-param name="grupo"><xsl:value-of select="$grupo + 1"/></xsl:with-param> 
				<xsl:with-param name="numeroLineas"><xsl:value-of select="$numeroLineas"/></xsl:with-param> 
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template name="lineaNumerica">
		<xsl:param name="grupo" /> 
		<xsl:param name="numeroLineas" /> 
		<xsl:variable name="auxEdadHasta"><xsl:value-of select="//piramide/edadDesde + (//piramide/intervalo * ($grupo + 1) - 1)"/></xsl:variable>
		<xsl:variable name="auxEdadDesde"><xsl:value-of select="//piramide/edadDesde + (//piramide/intervalo * $grupo)"/></xsl:variable>
		<xsl:if test="$grupo &lt; floor ($numeroLineas)">
			<tr>
				<td align="center">
					<xsl:choose>
						<xsl:when test="$auxEdadHasta &gt; //piramide/edadHasta">
							<xsl:value-of select="$auxEdadDesde"/> - <xsl:value-of select="//piramide/edadHasta"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$auxEdadDesde"/> - <xsl:value-of select="$auxEdadHasta"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td align="right">
					<xsl:value-of select="//piramide/datosPiramide/dato/cantidad[../sexo='H' and ../grupo_edad=$grupo]"/>
				</td>
				<td align="right">
					<xsl:value-of select="//piramide/datosPiramide/dato/cantidad[../sexo='M' and ../grupo_edad=$grupo]"/>
				</td>
				<td align="right">
					<xsl:value-of select="sum (//piramide/datosPiramide/dato/cantidad[../grupo_edad=$grupo])"/>
				</td>
			</tr>
			<xsl:call-template name="lineaNumerica"> 
				<xsl:with-param name="grupo"><xsl:value-of select="$grupo + 1"/></xsl:with-param> 
				<xsl:with-param name="numeroLineas"><xsl:value-of select="$numeroLineas"/></xsl:with-param> 
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template name="mostrarBarra">
		<xsl:param name="grupoBarra" /> 
		<xsl:param name="sexoBarra" /> 
		<xsl:param name="barraBarra" />

		<xsl:for-each select="//piramide/datosPiramide/dato">
			<xsl:if test="grupo_edad=$grupoBarra">
				<xsl:if test="sexo=$sexoBarra">
					<img>
						<xsl:attribute name="src"><xsl:value-of select="$barraBarra"/></xsl:attribute>
						<xsl:attribute name="width"><xsl:value-of select="cantidad * 8 div $npro - 1"/>mm</xsl:attribute>
					</img>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="mostrarNumero">
		<xsl:param name="grupoBarra" /> 
		<xsl:param name="sexoBarra" /> 
		<xsl:param name="barraBarra" /> 
		<xsl:for-each select="//piramide/datosPiramide/dato">
			<xsl:if test="grupo_edad=$grupoBarra">
				<xsl:if test="sexo=$sexoBarra">
					<xsl:value-of select="cantidad"/>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="Max">
		<xsl:param name="list" /> 
		<xsl:param name="nMax" select="'0'" /> 
		<xsl:choose>
			<xsl:when test="$list">
				<xsl:variable name="remainingList" select="$list[position() != 1]" /> 
				<xsl:variable name="nNewMax">
					<xsl:choose>
						<xsl:when test="$list[1] > $nMax">
							<xsl:value-of select="$list[1]" /> 
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$nMax" /> 
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="Max">
					<xsl:with-param name="list" select="$remainingList" /> 
					<xsl:with-param name="nMax" select="$nNewMax" /> 
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$nMax" /> 
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="pintarEscala">
		<xsl:param name="indice" /> 
		<xsl:if test="$indice &lt; 20">
			<td class="pieGrafico"><xsl:value-of select="$indice * $npro"/></td>
			<xsl:call-template name="pintarEscala"> 
				<xsl:with-param name="indice"><xsl:value-of select="$indice + 1"/></xsl:with-param> 
			</xsl:call-template>
		</xsl:if>

	</xsl:template>

</xsl:stylesheet>
