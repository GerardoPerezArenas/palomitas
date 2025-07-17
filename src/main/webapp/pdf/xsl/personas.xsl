<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:ora="http://www.oracle.com/XSL/Transform/java" version="1.0">
    <xsl:output method="fo"/>
    <xsl:include href="estilosInformes.xsl"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="ROWSET">
        <fo:table table-layout="fixed" width="100%">
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-column column-width="10mm"/>
            <fo:table-column column-width="10mm"/>
            <fo:table-column column-width="10mm"/>
            <fo:table-column column-width="10mm"/>
            <fo:table-column column-width="15mm"/>
            <fo:table-column column-width="55mm"/>
            <fo:table-column column-width="60mm"/>
            <fo:table-column column-width="65mm"/>
            <fo:table-column column-width="25mm"/>
            <fo:table-column column-width="proportional-column-width(1)"/>
            <fo:table-body>
                <xsl:choose>
                    <xsl:when test="ROW">
                        <xsl:for-each select="ROW">
                            <xsl:variable name="rowStyle">
                                <xsl:choose>
                                    <xsl:when test="position() mod 2 = 1">odd</xsl:when>
                                    <xsl:when test="position() mod 2 = 0">even</xsl:when>
                                </xsl:choose>
                            </xsl:variable>
                            <fo:table-row>
                                <xsl:apply-templates select=".">
                                    <xsl:with-param name="rowStyle" select="$rowStyle"/>
                                </xsl:apply-templates>
                            </fo:table-row>
                        </xsl:for-each>
                    </xsl:when>
                    <xsl:otherwise>
                        <!--                <table width="100%" border="0" cellpadding="4" align="center">
                            <tr><td></td></tr>
                        </table>-->
                    </xsl:otherwise>
                </xsl:choose>
            </fo:table-body>
        </fo:table>
        <fo:table table-layout="fixed" width="270mm">
            <fo:table-column column-width="10mm"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell text-align="right">
                        <fo:block font-weight="bold" font-family="Times" font-size="8pt">
                            TOTAL: <xsl:value-of select = "count(ROW)" />
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>

    </xsl:template>

    <xsl:template match="ROW">
        <xsl:param name="rowStyle"/>
        <xsl:call-template name="rowStyle">
            <xsl:with-param name="rowStyle" select="$rowStyle"/>
            <xsl:with-param name="content">
                <xsl:value-of select="DIS"/>
            </xsl:with-param>
            <xsl:with-param name="align">right</xsl:with-param>
            <xsl:with-param name="columnNumber">2</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="rowStyle">
            <xsl:with-param name="rowStyle" select="$rowStyle"/>
            <xsl:with-param name="content">
                <xsl:value-of select="SEC"/>
            </xsl:with-param>
            <xsl:with-param name="align">right</xsl:with-param>
            <xsl:with-param name="columnNumber">3</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="rowStyle">
            <xsl:with-param name="rowStyle" select="$rowStyle"/>
            <xsl:with-param name="content">
                <xsl:value-of select="HOJ"/>
            </xsl:with-param>
            <xsl:with-param name="align">right</xsl:with-param>
            <xsl:with-param name="columnNumber">4</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="rowStyle">
            <xsl:with-param name="rowStyle" select="$rowStyle"/>
            <xsl:with-param name="content">
                <xsl:value-of select="ORD"/>
            </xsl:with-param>
            <xsl:with-param name="align">right</xsl:with-param>
            <xsl:with-param name="columnNumber">5</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="rowStyle">
            <xsl:with-param name="rowStyle" select="$rowStyle"/>
            <xsl:with-param name="content">
                <xsl:value-of select="NIA"/>
            </xsl:with-param>
            <xsl:with-param name="align">right</xsl:with-param>
            <xsl:with-param name="columnNumber">6</xsl:with-param>
            <xsl:with-param name="border-right-width">2px</xsl:with-param>
            <xsl:with-param name="border-right-color">black</xsl:with-param>
            <xsl:with-param name="border-right-style">solid</xsl:with-param>
            <xsl:with-param name="padding-right">2px</xsl:with-param>
            <xsl:with-param name="padding-left">2px</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="rowStyle">
            <xsl:with-param name="rowStyle" select="$rowStyle"/>
            <xsl:with-param name="content">
                <xsl:apply-templates select="PA1"/><xsl:value-of select="AP1"/><xsl:apply-templates select="PA2"/><xsl:apply-templates select="AP2"/>, <xsl:value-of select="NOM"/>
                <xsl:if test="IMP_NAC">
                    <fo:block></fo:block><xsl:value-of select="NACION"/>
                </xsl:if>
            </xsl:with-param>
            <xsl:with-param name="align">left</xsl:with-param>
            <xsl:with-param name="columnNumber">7</xsl:with-param>
            <xsl:with-param name="border-right-width">2px</xsl:with-param>
            <xsl:with-param name="border-right-color">black</xsl:with-param>
            <xsl:with-param name="border-right-style">solid</xsl:with-param>
            <xsl:with-param name="padding-right">2px</xsl:with-param>
            <xsl:with-param name="padding-left">2px</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="rowStyle">
            <xsl:with-param name="rowStyle" select="$rowStyle"/>
            <xsl:with-param name="content">
                <!--                <table border="0"><tr><td width="56mm"><xsl:value-of select="DOMICILIO"/></td></tr></table>-->
                <xsl:value-of select="DOMICILIO"/>
            </xsl:with-param>
            <xsl:with-param name="align">left</xsl:with-param>
            <xsl:with-param name="columnNumber">8</xsl:with-param>
            <xsl:with-param name="border-right-width">2px</xsl:with-param>
            <xsl:with-param name="border-right-color">black</xsl:with-param>
            <xsl:with-param name="border-right-style">solid</xsl:with-param>
            <xsl:with-param name="padding-right">2px</xsl:with-param>
            <xsl:with-param name="padding-left">2px</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="rowStyle">
            <xsl:with-param name="rowStyle" select="$rowStyle"/>
            <xsl:with-param name="content">
                <!--                <table border="0"><tr><td width="61mm"><xsl:value-of select="ECOESI"/></td></tr></table>-->
                <xsl:value-of select="ECOESI"/>
            </xsl:with-param>
            <xsl:with-param name="align">left</xsl:with-param>
            <xsl:with-param name="columnNumber">9</xsl:with-param>
            <xsl:with-param name="border-right-width">2px</xsl:with-param>
            <xsl:with-param name="border-right-color">black</xsl:with-param>
            <xsl:with-param name="border-right-style">solid</xsl:with-param>
            <xsl:with-param name="padding-right">2px</xsl:with-param>
            <xsl:with-param name="padding-left">2px</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="rowStyle">
            <xsl:with-param name="rowStyle" select="$rowStyle"/>
            <xsl:with-param name="content">
                <xsl:value-of select="FNA"/>
            </xsl:with-param>
            <xsl:with-param name="align">left</xsl:with-param>
            <xsl:with-param name="columnNumber">10</xsl:with-param>
            <xsl:with-param name="padding-left">2px</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="PA1">
        <xsl:if test=". != ''">
            <xsl:value-of select="."/> 
        </xsl:if>
    </xsl:template>

    <xsl:template match="PA2">
        <xsl:if test=". != ''">
             <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="AP2">
        <xsl:if test=". != ''">
             <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="NUD">
        <xsl:if test=". != ''">
             <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="LED">
        <xsl:if test=". != ''">
             <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="NUH">
        <xsl:if test=". != ''">
             <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="LEH">
        <xsl:if test=". != ''">
             <xsl:value-of select="."/>
        </xsl:if>
    </xsl:template>

    <xsl:template name="rowStyle">
        <xsl:param name="rowStyle"/>
        <xsl:param name="content"/>
        <xsl:param name="columnNumber"/>
        <xsl:param name="align"/>
        <xsl:param name="border-right-width">0px</xsl:param>
        <xsl:param name="border-right-color">white</xsl:param>
        <xsl:param name="border-right-style">solid</xsl:param>
        <xsl:param name="padding-left">0px</xsl:param>
        <xsl:param name="padding-right">0px</xsl:param>
        <xsl:choose>
            <xsl:when test="$rowStyle='even'">
                <xsl:element name="fo:table-cell" use-attribute-sets="even">
                    <xsl:attribute name="display-align">center</xsl:attribute>
                    <xsl:attribute name="padding-bottom">0.5px</xsl:attribute>
                    <xsl:attribute name="padding-top">0.5px</xsl:attribute>
                    <xsl:attribute name="text-align"><xsl:value-of select="$align"/></xsl:attribute>
                    <xsl:attribute name="column-number"><xsl:value-of select="$columnNumber"/></xsl:attribute>
                    <xsl:attribute name="border-right-width"><xsl:value-of select="$border-right-width"/></xsl:attribute>
                    <xsl:attribute name="border-right-color"><xsl:value-of select="$border-right-color"/></xsl:attribute>
                    <xsl:attribute name="border-right-style"><xsl:value-of select="$border-right-style"/></xsl:attribute>
                    <xsl:attribute name="padding-left"><xsl:value-of select="$padding-left"/></xsl:attribute>
                    <xsl:attribute name="padding-right"><xsl:value-of select="$padding-right"/></xsl:attribute>
                    <fo:block>
                        <xsl:value-of select="$content"/>
                    </fo:block>
                </xsl:element>
            </xsl:when>
            <xsl:when test="$rowStyle='odd'">
                <xsl:element name="fo:table-cell" use-attribute-sets="odd">
                    <xsl:attribute name="display-align">center</xsl:attribute>
                    <xsl:attribute name="padding-bottom">0.5px</xsl:attribute>
                    <xsl:attribute name="padding-top">0.5px</xsl:attribute>
                    <xsl:attribute name="text-align"><xsl:value-of select="$align"/></xsl:attribute>
                    <xsl:attribute name="column-number"><xsl:value-of select="$columnNumber"/></xsl:attribute>
                    <xsl:attribute name="border-right-width"><xsl:value-of select="$border-right-width"/></xsl:attribute>
                    <xsl:attribute name="border-right-color"><xsl:value-of select="$border-right-color"/></xsl:attribute>
                    <xsl:attribute name="border-right-style"><xsl:value-of select="$border-right-style"/></xsl:attribute>
                    <xsl:attribute name="padding-left"><xsl:value-of select="$padding-left"/></xsl:attribute>
                    <xsl:attribute name="padding-right"><xsl:value-of select="$padding-right"/></xsl:attribute>
                    <fo:block>
                        <xsl:value-of select="$content"/>
                    </fo:block>
                </xsl:element>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
