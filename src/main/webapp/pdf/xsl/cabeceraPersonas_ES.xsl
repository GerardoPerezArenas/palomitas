<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
    <xsl:output method="fo" encoding="ISO-8859-1"/>
    <xsl:include href="estilosInformes.xsl"/>

	<xsl:template match="/">
		<xsl:apply-templates select="ROWSET"/>
	</xsl:template>

	<xsl:template match="ROWSET">
  	<xsl:choose>
    	<xsl:when test="ROW">
                <fo:table table-layout="fixed" width="100%">
                    <fo:table-column column-width="proportional-column-width(1)"/>
                    <fo:table-body>
				<xsl:apply-templates select="ROW"/>
                    </fo:table-body>
                </fo:table>
	    </xsl:when>
  	  <xsl:otherwise>
                <!--                <table width="100%" cellborder="0" cellpadding="4" align="center">
	<tr><td></td></tr>
                </table>-->
    	</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
    
	<xsl:template match="ROW">
        <fo:table-row>
            <fo:table-cell text-align="center">
                <fo:block>
                    <fo:table table-layout="fixed" width="100%">
                        <fo:table-column column-width="proportional-column-width(1)"/>
                        <fo:table-column column-width="30mm"/>
                        <fo:table-column column-width="80mm"/>
                        <fo:table-column column-width="130mm"/>
                        <fo:table-column column-width="30mm"/>
                        <fo:table-column column-width="proportional-column-width(1)"/>
                        <fo:table-body>
                            <fo:table-row>
                                <fo:table-cell text-align="center" column-number="2">
                                    <fo:block>
		<xsl:if test="IMG != ''">
						<img src="{IMG}" width="30%" height="30%"/>
		</xsl:if>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="3">
                                    <fo:block xsl:use-attribute-sets="h4">
		<xsl:if test="ENT != ''">
                                            <xsl:value-of select="ENT"/>
		</xsl:if>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="4">
                                    <fo:block>
                                        <fo:table table-layout="fixed" width="130mm">
                                            <fo:table-column column-width="130mm"/>
                                            <fo:table-body>
                                                <fo:table-row>
                                                    <fo:table-cell text-align="center">
                                                        <fo:block xsl:use-attribute-sets="h3" font-style="italic" font-weight="bold">
                                                            PADRÓN: LISTADO DE PERSONAS
									<xsl:if test="CABEZA_FAMILIA">
									&nbsp;(CABEZAS DE FAMILIA)
									</xsl:if>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                <fo:table-row>
                                                    <fo:table-cell text-align="center">
                                                        <fo:block xsl:use-attribute-sets="h4">
                                                            <xsl:value-of select="UOR"/>
                                                        </fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="left" font-family="Times" font-size="8pt" column-number="5">
                                    <fo:block>
                                        Página <fo:page-number/>
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <fo:table-row>
            <fo:table-cell text-align="center">
                <fo:block>
                    <fo:table table-layout="fixed" width="100%" padding-top="1cm">
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
                            <fo:table-row>
                                <fo:table-cell text-align="center" column-number="2" xsl:use-attribute-sets="celdaInicioCabeceraTabla">
                                    <fo:block font-size="10pt" font-weight="bold">
						DIS
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="3" xsl:use-attribute-sets="celdaCabeceraTabla">
                                    <fo:block font-size="10pt" font-weight="bold">
						SEC
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="4" xsl:use-attribute-sets="celdaCabeceraTabla">
                                    <fo:block font-size="10pt" font-weight="bold">
						HOJ
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="5" xsl:use-attribute-sets="celdaCabeceraTabla">
                                    <fo:block font-size="10pt" font-weight="bold">
						ORD
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="6" xsl:use-attribute-sets="celdaCabeceraTabla">
                                    <fo:block font-size="10pt" font-weight="bold">
						NIA
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="7" xsl:use-attribute-sets="celdaCabeceraTabla">
                                    <fo:block font-size="10pt" font-weight="bold">
                                        APELLIDOS, NOMBRE
						<xsl:if test="IMP_NAC">
						<br/>NACIONALIDAD
						</xsl:if>						
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="8" xsl:use-attribute-sets="celdaCabeceraTabla">
                                    <fo:block font-size="10pt" font-weight="bold">
                                        DOMICILIO
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="9" xsl:use-attribute-sets="celdaCabeceraTabla">
                                    <fo:block font-size="10pt" font-weight="bold">
						ENT. COL. / ENT. SING.
                                    </fo:block>
                                </fo:table-cell>
                                <fo:table-cell text-align="center" column-number="10" xsl:use-attribute-sets="celdaFinCabeceraTabla">
                                    <fo:block font-size="10pt" font-weight="bold">
                                        FECHA NAC.
                                    </fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-body>
                    </fo:table>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
	</xsl:template>

</xsl:stylesheet>


     
