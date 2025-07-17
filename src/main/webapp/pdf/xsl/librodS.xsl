<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
<xsl:template match="/">
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="ROWSET"> 
<table cellpadding="0mm" padding-bottom="0mm" padding-left="0mm" padding-right="0mm" class="colwhite" width="255mm" border-bottom="2px" border-left="0px" border-right="0px" border-top="0px" align="center">
	<xsl:choose>
		<xsl:when test="ROW">
			<xsl:apply-templates/>
		</xsl:when>
		<xsl:otherwise> 
		<tr style="color:black;"> 
	  		<td width="255mm" align="left"></td>
		</tr>
		</xsl:otherwise>
	</xsl:choose>	
</table>
</xsl:template>


<xsl:template match="ROW"> 
	<tr style="color:black;"> 
  	  <td width="255mm" align="left">
			<table padding-bottom="1mm" class="colodd" width="255mm" align="center"  cellborder="0" border-bottom="0px" border-left="1px" border-right="2px" border-top="1px">
    		<tr> 
    	 			<td width="12mm" align="left"  padding-top="1mm">
	<xsl:choose>
		<xsl:when test="ESTADO&gt;'8'"> 
      <xsl:value-of select="NUM"/><br />
      			Anulada 
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="NUM"/>
		</xsl:otherwise> 
  </xsl:choose>
					</td>
	     			<td width="28mm" align="left"  padding-top="1mm" >
							<table width="18mm" cellpadding="1" border="0" align="left">
								<tr>
									<td width="18mm" align="center">
										<xsl:value-of select="FECHA"/>
									</td>
								</tr>
								<tr>
									<td width="18mm" align="center" height="3mm">
										<xsl:value-of select="HORA"/> 
									</td>
								</tr>																					
							</table>						
     			</td>
	<xsl:apply-templates select="ASUNTO"/> 
  			  		<td width="95mm" align="left" padding-right="15mm"  padding-top="1mm" >
	<xsl:choose>
		<xsl:when test="APELLIDOS=' '">
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="APELLIDOS"/>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:value-of select="REMITENTE"/> <br />
	<xsl:choose>
		<xsl:when test="normalize-space(CALLE1)">
			<xsl:value-of select="CALLE1"/>
		</xsl:when>
		<xsl:when test="normalize-space(CALLE2)">
			<xsl:value-of select="CALLE2"/>
		</xsl:when>
		<xsl:when test="normalize-space(CALLE3)">
			<xsl:value-of select="CALLE3"/>
		</xsl:when>
		<xsl:when test="normalize-space(CALLE4)">
			<xsl:value-of select="CALLE4"/>
		</xsl:when>
	</xsl:choose>
     			</td>
				</tr>
				<xsl:choose>
					<xsl:when test="ESTADO&gt;'8'"> 
					<tr style="color:black;"> 
						<td width="250mm" colspan="5" height="2mm"></td>
					</tr>			
					<tr style="color:black;"> 
						<td width="250mm" colspan="5" >
							Diligencia de Anulación: <xsl:value-of select="DILIGENCIA"/> 
						</td>
			 		</tr>
					</xsl:when>
				<xsl:otherwise></xsl:otherwise>
				</xsl:choose>	
				<tr>
				  <td colspan="5" width="100%" height="3mm">
				  </td>
				</tr>				
   		</table>
		</td>
 	</tr>
</xsl:template>

<xsl:template match="ASUNTO"> 
		<td width="120mm" align="left" padding-top="1mm" padding-left="0mm" padding-right="15mm">  <xsl:value-of select="."/></td>
</xsl:template>

</xsl:stylesheet> 