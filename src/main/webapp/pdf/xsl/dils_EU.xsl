<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="ROWSET">
      	<xsl:choose>
        	<xsl:when test="ROW">
						<xsl:apply-templates/>
	        </xsl:when>
  	      <xsl:otherwise>
						<table width="100%" border="0" cellpadding="4" align="center">
							<tr><td></td></tr>
			      </table>
    	    </xsl:otherwise>
      	</xsl:choose>
    </xsl:template>
    
	<xsl:template match="ROW">
		<p padding="1">
			<xsl:apply-templates select="FECHA"/> 
			<xsl:apply-templates select="ANOTACION"/>
		</p>
	</xsl:template>
	
	<xsl:template match="FECHA">
		<u><strong><em>
			EGUNEKO EGINBIDEAK <xsl:value-of select="."/>
		</em></strong></u>
		<br />
	</xsl:template>
	
	<xsl:template match="ANOTACION">
		<br />
		<em><xsl:value-of select="."/></em>
		<br />
	</xsl:template>

</xsl:stylesheet>

