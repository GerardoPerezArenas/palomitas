<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/">
		<xsl:apply-templates select="ROWSET"/>
	</xsl:template>

	<xsl:template match="ROWSET">
  	<xsl:choose>
    	<xsl:when test="ROW">
<table cellpadding="0mm" padding-bottom="0mm" padding-left="0mm" padding-right="0mm" width="255mm" border="0px" align="center" >	
				<xsl:apply-templates select="ROW"/>
</table>	
	    </xsl:when>
  	  <xsl:otherwise>
    	</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
    
	<xsl:template match="ROW">
  <tr>
  	<td width="255mm" align="left">

    		<table padding-bottom="0mm" width="255mm" border="0" align="center" cellborder="0">
				<tr>				
					<xsl:if test="IMG != ''"> <!-- Hay escudo -->				
						<td width="30mm" align="center">
							<img src="{IMG}" width="100%" height="30px"/>
						</td>
						<xsl:if test="ENT != ''"> <!-- Hay nombre -->
							<td width="80mm" align="center" valign="middle">
								<h4><xsl:value-of select="ENT"/></h4>
							</td>
							<td width="125mm">
								<table width="125mm" border="0" align="center">
		   						<tr>
										<td align="center" valign="middle">
											<h3><xsl:value-of select="TIP"/></h3>
		     						</td>
									</tr>
									<tr>
										<td align="center" valign="middle">
		     							<h4><xsl:value-of select="UOR"/></h4>
										</td>
									</tr>
								</table>
							</td>							
						</xsl:if>
						<xsl:if test="ENT = ''"> <!-- No hay nombre -->
							<td width="205mm">
								<table width="205mm" border="0" align="center">
		   						<tr>
										<td align="center" valign="middle" width="205mm">
											<h3><xsl:value-of select="TIP"/></h3>
		     						</td>
									</tr>
									<tr>
										<td align="center" valign="middle" width="205mm">
		     							<h4><xsl:value-of select="UOR"/></h4>
										</td>
									</tr>
								</table>
							</td>							
						</xsl:if>							
					</xsl:if>
					<xsl:if test="IMG = ''"> <!-- No hay escudo -->				
						<xsl:if test="ENT != ''"> <!-- Hay nombre -->
							<td width="80mm" align="center" valign="middle">
								<h4><xsl:value-of select="ENT"/></h4>
							</td>
							<td width="155mm">
								<table width="155mm" border="0" align="center">
		   						<tr>
										<td align="center" valign="middle" width="155mm">
											<h3><xsl:value-of select="TIP"/></h3>
		     						</td>
									</tr>
									<tr>
										<td align="center" valign="middle" width="155mm">
		     							<h4><xsl:value-of select="UOR"/></h4>
										</td>
									</tr>
								</table>
							</td>							
						</xsl:if>
						<xsl:if test="ENT = ''"> <!-- No hay nombre -->
							<td width="235mm">
								<table width="235mm" border="0" align="center">
		   						<tr>
										<td align="center" valign="middle" width="235mm">
											<h3><xsl:value-of select="TIP"/></h3>
		     						</td>
									</tr>
									<tr>
										<td align="center" valign="middle" width="235mm">
		     							<h4><xsl:value-of select="UOR"/></h4>
										</td>
									</tr>
								</table>
							</td>							
						</xsl:if>							
					</xsl:if>
			</tr>
			</table>
		</td>
	</tr>
	<tr style="color:black;">
  		<td width="255mm" align="left" padding="1mm">
		<xsl:if test="OPC = 1">
	 		<table width="255mm" padding="1" border="1" cellborder="0" valign="middle" align="center">
				<tr>
					<td width="15mm" align="left" style="font-family:Times;font-size:10;font-weight:bold;">
						NUM
					</td>
					<td width="25mm" align="left" style="font-family:Times;font-size:10;font-weight:bold;">
						FECHA
					</td>
					<td width="120mm" align="left" style="font-family:Times;font-size:10;font-weight:bold;">
						EXTRACTO
					</td>
					<td width="95mm" align="left" style="font-family:Times;font-size:10;font-weight:bold;">
						REMITENTE
					</td>
				</tr>
			</table>
		</xsl:if>
    </td>
	</tr>
	</xsl:template>

</xsl:stylesheet>
