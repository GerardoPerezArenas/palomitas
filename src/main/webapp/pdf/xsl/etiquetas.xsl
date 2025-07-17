<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="ROWSET">
  	<xsl:choose>
    	<xsl:when test="ROW">
			<table width="100%" align="center" border="0" >
				<xsl:for-each select="ROW">
					<xsl:if test="@ID mod 2 = 1">
						<xsl:choose>
							<xsl:when test="ROW[last()]">
								<tr padding-bottom="10mm" border="0">
									<td width="100%" colspan="2" height="27mm" cellpadding="10" border="0">	
										<xsl:apply-templates select="."/>
									</td>
								</tr>
							</xsl:when>
							<xsl:when test="(@ID +1) mod 16 = 0">
								<tr padding-bottom="0mm" padding-top="3mm" border="0" >
									<td width="50%" height="27mm" cellpadding="10" border="0">	
										<xsl:apply-templates select="."/>
									</td>
									<td width="50%" height="27mm" cellpadding="10" border="0">	
										<xsl:apply-templates select="following-sibling::ROW[1]"/>
									</td>
								</tr>
							</xsl:when>							
				  	  		<xsl:otherwise>
								<tr padding-bottom="10mm" border="0">
									<td width="50%" height="27mm" cellpadding="10" border="0">	
										<xsl:apply-templates select="."/>
									</td>
									<td width="50%" height="27mm" cellpadding="10" border="0">	
										<xsl:apply-templates select="following-sibling::ROW[1]"/>
									</td>
								</tr>
							</xsl:otherwise>
				    	</xsl:choose>
					</xsl:if>
				</xsl:for-each>
			</table>
	    </xsl:when>
  	  	<xsl:otherwise>
			<table width="100%" border="0" cellpadding="4" align="center">
				<tr><td></td></tr>
			</table>
    	</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
	<xsl:template match="ROW">
			<strong>
				<xsl:apply-templates select="PA1"/><xsl:value-of select="AP1"/><xsl:apply-templates select="PA2"/>
				<xsl:apply-templates select="AP2"/>,&nbsp;<xsl:value-of select="normalize-space(NOM)"/>
			</strong>
			<BR/>
				<xsl:if test = "normalize-space(ABR) = ''" ><xsl:value-of select="ESI"/>&nbsp;</xsl:if>
				<xsl:if test = "normalize-space(ABR) != ''" >
                <xsl:value-of select="TVI"/>&nbsp;<xsl:value-of select="VIANOM"/></xsl:if><xsl:apply-templates select="NUD"/>
				<xsl:apply-templates select="LED"/><xsl:apply-templates select="NUH"/><xsl:apply-templates select="LEH"/>
                <xsl:apply-templates select="BLQ"/><xsl:apply-templates select="POR"/><xsl:apply-templates select="ESC"/>
                <xsl:apply-templates select="PLT"/><xsl:apply-templates select="PTA"/>
				<xsl:if test = "normalize-space(ABR) != ''" ><BR/><xsl:value-of select="ESI"/>&nbsp;<xsl:value-of select="ECO"/></xsl:if>
                <xsl:if test = "normalize-space(ABR) = ''" ><BR/><xsl:value-of select="ECO"/></xsl:if>
            <BR/>
                <xsl:value-of select="MUN"/><xsl:apply-templates select="CPO"/>
            <BR/>
                (<xsl:value-of select="PRV"/>)
	</xsl:template>
	<xsl:template match="PA1">
		<xsl:if test="normalize-space(.) != ''"><xsl:value-of select="normalize-space(.)"/>&nbsp;</xsl:if>
	</xsl:template>
	<xsl:template match="PA2">
		<xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
	</xsl:template>
	<xsl:template match="AP2">
		<xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
	</xsl:template>
	<xsl:template match="NUD">
		<xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
	</xsl:template>
	<xsl:template match="LED">
		<xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
	</xsl:template>
	<xsl:template match="NUH">
		<xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
	</xsl:template>
    <xsl:template match="LEH">
        <xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
    </xsl:template>
    <xsl:template match="BLQ">
        <xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
    </xsl:template>
    <xsl:template match="POR">
        <xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
    </xsl:template>
    <xsl:template match="ESC">
        <xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
    </xsl:template>
    <xsl:template match="PLT">
        <xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
    </xsl:template>
    <xsl:template match="PTA">
		<xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
	</xsl:template>
    <xsl:template match="KMT">
        <xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
    </xsl:template>
    <xsl:template match="HMT">
		<xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
	</xsl:template>
    <xsl:template match="CPO">
		<xsl:if test="normalize-space(.) != ''">&nbsp;<xsl:value-of select="normalize-space(.)"/></xsl:if>
	</xsl:template>
</xsl:stylesheet>