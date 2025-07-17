<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/" >
		<xsl:call-template name="pagina"/>
	</xsl:template>

    <xsl:template name="pagina">
	    <xsl:apply-templates select = "//PORTADA" />
    </xsl:template>

    <xsl:template match = "PORTADA" >
        <table border="0" align="center" width="250mm" height="180mm" padding-right="28mm"
               padding-top="12mm">
            <tr>
                <td align="center" colspan="3" height="20mm">
                    <p class="tituloPortada">
                        E R R E G I S T R O &nbsp; L I B U R U A
                    </p>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="3" height="15mm">
                    <p class="encabezadoPortada">
                        norena
                    </p>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="3">
                    <p class="encabezadoPortada">
                        <xsl:choose>
                            <xsl:when test="TIPOREGISTRO='E'">
                                D O K U M E N T U  &nbsp; S A R R E R A
                            </xsl:when>
                            <xsl:otherwise>
                                D O K U M E N T U &nbsp;  I R T E E R A
                            </xsl:otherwise>
                        </xsl:choose>
                    </p>
                </td>
            </tr>
            <tr height="25mm">
                <td width="45%" height="100%"></td>
                <td width="10%" height="100%" border-top-width="1px"></td>
                <td width="45%" height="100%"></td>
            </tr>
            <tr>
                <td colspan="3">
                    <p class="textoPortada">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;E G I N B I D E A :&nbsp;Honen bidez, aditzera ematen da liburu hau
                         - Erregistro norena &nbsp;
                        <xsl:choose>
                            <xsl:when test="TIPOREGISTRO='E'">
                                S A R R E R A &nbsp; 
                            </xsl:when>
                            <xsl:otherwise>
                                I R T E E R A  &nbsp; 
                            </xsl:otherwise>
                        </xsl:choose>
                        <br></br>D O K U M E N T U &nbsp; Honela osatuta dago
                        <xsl:choose>
                            <xsl:when test="NUMPAGINAS='un'">
                                <xsl:value-of select="NUMPAGINAS"/> folio zenbakitu eta zigilatua  
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="NUMPAGINAS"/> folio zenbakitu eta zigilatuak
                            </xsl:otherwise>
                        </xsl:choose>
                        honetako honekin ...............................................,
                        Horren irekiera indarrean dagoen legedian xedatutakoa betez formalizatuko da.
                    </p>
                    <p class="textoPortada">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Y para que así conste, se extiende la presente el día
                        .............................. de .......................................................
                        de ......................................................................
                    </p>
                </td>
            </tr>
            <tr>
                <td colspan="3" height="10mm"></td>
            </tr>
            <tr>
                <td align="center" width="30%" class="textoFirma">
                    V.º B.º
                </td>
                <td width="30%"></td>
                <td width="40%" align="left" class="textoFirma">
                	<xsl:value-of select="SECRETARIO"/>,
                </td>
            </tr>
            <tr>
                <td align="center" width="30%" class="textoFirma">
                    <xsl:value-of select="ALCALDE"/>,
                </td>
                <td width="30%"></td>
                <td width="40%"></td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>