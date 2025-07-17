<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/listado">
	   <table width="255mm" padding="1" border="1" valign="middle" align="center">  
		   <xsl:for-each select="fila">

			 <tr style="color:black;" width="255mm"> 
				<td width="25mm" align="left">
				  <table>
      					<tr><td>
						<xsl:value-of select="ejercicio"/> 
				    	</td></tr>
				    	<tr><td style="color:red;">
						<xsl:value-of select="estado"/>
				  	</td></tr>
				  </table> 	
				</td>
				<td width="25mm" align="left">
				  <table>
				    <tr><td>
					<xsl:value-of select="fecha"/> 
				    </td></tr>
				    <tr><td>
					<xsl:value-of select="hora"/> 
				    </td></tr>	
				  </table>			
				</td>
				<td width="100mm" align="left">
				  <table>
				    <tr><td>
					<xsl:value-of select="remitente"/> 
				    </td></tr>
				    <tr><td>
					<xsl:value-of select="asunto"/> 
				    </td></tr>	
				  </table>
				</td>	
				<td width="40mm" align="left">
				  <table>
				  <tr><td>
					<xsl:value-of select="estadoAnotacion"/>
				  </td></tr>
				  <tr><td style="text-align:left">
					<xsl:value-of select="expedientes"/>
				  </td></tr>
				  </table> 
				</td>
				<td width="65mm" align="left">
				  <table><tr><td>
					<xsl:value-of select="destino"/> 
				  </td></tr></table> 
				</td>
			 </tr>	
		   </xsl:for-each>		  
	   </table>
	</xsl:template>
</xsl:stylesheet>



