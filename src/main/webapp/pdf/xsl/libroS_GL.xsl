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
         <xsl:when test="DILIGENCIAS">
            <xsl:apply-templates/>
        </xsl:when>
		<xsl:otherwise>
		<tr style="color:black;" > 
	  		<td width="255mm" align="left"></td>
		</tr>
		</xsl:otherwise>
	</xsl:choose>
</table>
</xsl:template>


<xsl:template match="ROW"> 
	<tr style="color:black;"> 
  		<td width="255mm" align="left">
               <xsl:choose>
                    <xsl:when test="COLOR=1">
            <table padding-bottom="0mm" class="colodd" width="255mm" align="center" cellborder="0" border-bottom="0px" border-left="1px" border-right="2px" border-top="1px">
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
  			  		<td width="75mm" align="left" padding-right="15mm"  padding-top="1mm" >
						<!-- Poner el primer apellido-->
                                    <xsl:choose>
                                        <xsl:when test="APELLIDO1=''"></xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="APELLIDO1"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <!-- Poner el segundo apellido-->
                                    <xsl:choose>
                                        <xsl:when test="APELLIDO2=''"></xsl:when>
                                        <xsl:otherwise>
                                            <xsl:choose>
                                                <xsl:when test="APELLIDO1=''">
                                                    <xsl:value-of select="APELLIDO2"/>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:text> </xsl:text><xsl:value-of select="APELLIDO2"/>
                                                </xsl:otherwise>
                                            </xsl:choose>                                                
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <!-- Poner el nombre-->
                                    <xsl:choose>
                                        <xsl:when test="APELLIDO1 = '' and APELLIDO2 = ''">
                                            <xsl:value-of select="NOMBRE"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>, </xsl:text><xsl:value-of select="NOMBRE"/>                                                                                         
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <xsl:if test="TIPONOMBRE = 'MEDIO' or TIPONOMBRE = 'LARGO'">
                                        <xsl:text> </xsl:text><xsl:value-of select="DOCUMENTO"/>                                        
                                    </xsl:if>
                                    <br /> 
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
					<xsl:apply-templates select="DESTINO"/>
				</tr>
				<xsl:choose>
					<xsl:when test="ESTADO&gt;'8'"> 
					<tr style="color:black;"> 
						<td width="250mm" colspan="5" height="2mm"></td>
					</tr>			
					<tr style="color:black;"> 
						<td width="250mm" colspan="5" >
							Dilixencia de Anulación: <xsl:value-of select="DILIGENCIA"/> 
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


                    </xsl:when>
                      <xsl:otherwise>
                   <table padding-bottom="0mm" class="colwhite" width="255mm" align="center" cellborder="0" border-bottom="0px" border-left="1px" border-right="2px" border-top="1px">
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
  			  		<td width="75mm" align="left" padding-right="15mm"  padding-top="1mm" >
						<!-- Poner el primer apellido-->
                                    <xsl:choose>
                                        <xsl:when test="APELLIDO1=''"></xsl:when>
                                        <xsl:otherwise>
                                            <xsl:value-of select="APELLIDO1"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <!-- Poner el segundo apellido-->
                                    <xsl:choose>
                                        <xsl:when test="APELLIDO2=''"></xsl:when>
                                        <xsl:otherwise>
                                            <xsl:choose>
                                                <xsl:when test="APELLIDO1=''">
                                                    <xsl:value-of select="APELLIDO2"/>
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <xsl:text> </xsl:text><xsl:value-of select="APELLIDO2"/>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <!-- Poner el nombre-->
                                    <xsl:choose>
                                        <xsl:when test="APELLIDO1 = '' and APELLIDO2 = ''">
                                            <xsl:value-of select="NOMBRE"/>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>, </xsl:text><xsl:value-of select="NOMBRE"/>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                    <xsl:if test="TIPONOMBRE = 'MEDIO' or TIPONOMBRE = 'LARGO'">
                                        <xsl:text> </xsl:text><xsl:value-of select="DOCUMENTO"/>
                                    </xsl:if>
                                    <br />
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
					<xsl:apply-templates select="DESTINO"/>
				</tr>
				<xsl:choose>
					<xsl:when test="ESTADO&gt;'8'">
					<tr style="color:black;">
						<td width="250mm" colspan="5" height="2mm"></td>
					</tr>
					<tr style="color:black;">
						<td width="250mm" colspan="5" >
							Dilixencia de Anulación: <xsl:value-of select="DILIGENCIA"/>
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

                 </xsl:otherwise>
                </xsl:choose>

        </td>
		</tr>
</xsl:template>


<xsl:template match="ASUNTO"> 
		<td width="90mm" align="left" padding-top="1mm" padding-left="0mm" padding-right="15mm"><xsl:value-of select="."/> </td>
</xsl:template>

<xsl:template match="DESTINO"> 
		<td width="50mm" align="left"  padding-top="1mm" padding-left="2mm" padding-right="6mm"> <xsl:value-of select="."/> </td>
</xsl:template>

      <xsl:template match="DILIGENCIA">
        <tr style="color:black;">
            <td width="255mm" align="left"> &nbsp;</td>
        </tr>
        <tr>
            <td padding-top="5mm" padding-left="5mm" padding-bottom="5mm">
                <table class="colwhite" width="55mm" align="left" cellborder="0"
                       border-bottom="2px" border-left="2px" border-right="2px" border-top="2px">
                    <tr border="1">
                        <td>
                            <xsl:apply-templates select="FECHA"/>
                        </td>
                    </tr>
                    <tr border="1">
                        <td>
                            <xsl:apply-templates select="ANOTACION"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
         <tr style="color:black;">
            <td width="255mm" align="left"> &nbsp;</td>
        </tr>
    </xsl:template>

    	<xsl:template match="FECHA">
		<u><strong><em>
			DILIXENCIAS DO DIA <xsl:value-of select="."/>
		</em></strong></u>
		<br />
	</xsl:template>

	<xsl:template match="ANOTACION">
		<br />
		<em>
			<xsl:variable name="str">
			  <xsl:call-template name="replace">
			    <xsl:with-param name="str" select="."/>
			    <xsl:with-param name="repl">[br/]</xsl:with-param>
			    <xsl:with-param name="target">&lt;br/></xsl:with-param>
			  </xsl:call-template>
			</xsl:variable>
		    <xsl:value-of select="$str" disable-output-escaping="yes"/>
		</em>
		<br />
	</xsl:template>

	<xsl:template name="replace">
		<xsl:param name="str"/>
		<xsl:param name="repl"/>
		<xsl:param name="target"/>
		<xsl:choose>
			<xsl:when test="contains($str,$repl)">
				<xsl:call-template name="replace">
					<xsl:with-param name="str" select="concat(substring-before($str,$repl),$target,substring-after($str,$repl))"/>
					<xsl:with-param name="repl" select="$repl"/>
					<xsl:with-param name="target" select="$target"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$str"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>