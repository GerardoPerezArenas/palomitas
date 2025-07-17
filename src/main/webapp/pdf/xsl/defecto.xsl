<?xml version = "1.0" encoding = "ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="/">
        <xsl:apply-templates/>      
  </xsl:template>

    <xsl:template match="ROWSET">
      <table width="100%" border="0" cellpadding="4" align="center">
      <xsl:choose>
        <xsl:when test="ROW">
	  <xsl:for-each select="ROW[1]">
	    <tr style="color:black;">
	      <xsl:for-each select="*[not(starts-with(name(.),&apos;H_&apos;))]">
		<th align="center" style="font-family:Helvetica;font-size:14;font-weight:bold;border-bottom:4px solid;">
		  <xsl:attribute name="class">
		    <xsl:choose>
		      <xsl:when test="position() mod 2 = 1">colodd</xsl:when>
		      <xsl:when test="position() mod 2 = 0">coleven</xsl:when>
		    </xsl:choose>
		  </xsl:attribute>
		  <xsl:value-of select="name(.)"/>
		</th>
	      </xsl:for-each>
	    </tr>
	  </xsl:for-each>
	  <xsl:apply-templates/>
        </xsl:when>
        <xsl:otherwise>
				<table width="100%" border="0" cellpadding="4" align="center">
							<tr><td></td></tr>
			      </table> 
        </xsl:otherwise>
      </xsl:choose>
      </table>
     
    </xsl:template>

    <xsl:template match="ROW">
       <tr style="border-bottom:1px solid #DCDCAC;">
       <xsl:for-each select="*[not(starts-with(name(.),&apos;H_&apos;))]">
         <td align="left" style="border-bottom:1px solid #DCDCAC;">
	      <xsl:attribute name="class">
		<xsl:choose>
		  <xsl:when test="position() mod 2 = 1">colodd</xsl:when>
		  <xsl:when test="position() mod 2 = 0">coleven</xsl:when>
		</xsl:choose>
	      </xsl:attribute>
            
            <xsl:apply-templates select="."/>
            </td>
          </xsl:for-each>
          </tr>
  </xsl:template>

</xsl:stylesheet>
