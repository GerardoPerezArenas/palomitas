<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="ROWSET">
	<table padding-bottom="0mm" padding-left="0mm" padding-right="0mm" class="colwhite" width="255mm" border="0" align="center">
		<tr style="color:black;"> 
  		<td width="255mm" align="left">
      	<xsl:choose>
        	<xsl:when test="ROW">
						<xsl:apply-templates/>
	        </xsl:when>
  	      <xsl:otherwise>
    	    </xsl:otherwise>
      	</xsl:choose>
		</td>	
		</tr>
	</table>
    </xsl:template>
    
	<xsl:template match="ROW">
		<p border="1" padding="1">
			<xsl:apply-templates select="FECHA"/> 
			<xsl:apply-templates select="ANOTACION"/>
		</p>
	</xsl:template>
	
	<xsl:template match="FECHA">
		<u><strong><em>
			DILIGENCIAS DEL DIA <xsl:value-of select="."/>
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




