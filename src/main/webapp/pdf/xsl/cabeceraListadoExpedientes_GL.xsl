<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="//CABECERA">
        <table width="100%" border="0" cellborder="0" >
            <tr>
                <td align="right">
             Páxina
                    <pagenumber/>
                </td>
            </tr>
            <tr>
                <td>
                    Data da consulta: <xsl:value-of select="FECHA"/>
                </td>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
