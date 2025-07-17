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
                        L I B R O &nbsp; R E G I S T R O
                    </p>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="3" height="15mm">
                    <p class="encabezadoPortada">
                        d e
                    </p>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="3">
                    <p class="encabezadoPortada">
                        <xsl:choose>
                            <xsl:when test="TIPOREGISTRO='E'">
                                E N T R A D A &nbsp; D E &nbsp; D O C U M E N T O S
                            </xsl:when>
                            <xsl:otherwise>
                                S A L I D A &nbsp; D E &nbsp; D O C U M E N T O S
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
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;D I L I G E N C I A :&nbsp;Por medio de la presente se hace
                        constar que este Libro - Registro de &nbsp;
                        <xsl:choose>
                            <xsl:when test="TIPOREGISTRO='E'">
                                E N T R A D A &nbsp; D E
                            </xsl:when>
                            <xsl:otherwise>
                                S A L I D A &nbsp; D E &nbsp;
                            </xsl:otherwise>
                        </xsl:choose>
                        <br></br>D O C U M E N T O S &nbsp; consta de
                        <xsl:choose>
                            <xsl:when test="NUMPAGINAS='1'">
                                <xsl:value-of select="NUMPAGINAS"/> folio numerado y sellado 
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="NUMPAGINAS"/> folios numerados y sellados
                            </xsl:otherwise>
                        </xsl:choose>
                        con el de ...............................................,
                        cuya apertura se formalizará en cumplimento de lo dispuesto en la Legislación Vigente.
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