<?xml version="1.0" encoding="ISO-8859-1" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

    <xsl:attribute-set name="page" >
        <xsl:attribute name="margin-right">0mm</xsl:attribute>
        <xsl:attribute name="margin-left">0mm</xsl:attribute>
        <xsl:attribute name="margin-bottom">0mm</xsl:attribute>
        <xsl:attribute name="margin-top">0mm</xsl:attribute>
        <xsl:attribute name="page-width">297mm</xsl:attribute>
        <xsl:attribute name="page-width">210mm</xsl:attribute>
        <xsl:attribute name="master-name">first</xsl:attribute>
        <xsl:attribute name="font-size">8mm</xsl:attribute>
        <xsl:attribute name="font-family">Times</xsl:attribute>
        <xsl:attribute name="padding-bottom">2mm</xsl:attribute>
        <xsl:attribute name="background-color">white</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="odd">
        <xsl:attribute name="font-size">8pt</xsl:attribute>
        <xsl:attribute name="font-family">Times</xsl:attribute>
        <xsl:attribute name="background-color">#FFFAFA</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="even">
        <xsl:attribute name="font-size">8pt</xsl:attribute>
        <xsl:attribute name="font-family">Times</xsl:attribute>
        <xsl:attribute name="background-color">#E5E5E5</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="white">
        <xsl:attribute name="font-size">8pt</xsl:attribute>
        <xsl:attribute name="font-family">Times</xsl:attribute>
        <xsl:attribute name="background-color">#FFFFFF</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="h3">
        <xsl:attribute name="font-size">10pt</xsl:attribute>
        <xsl:attribute name="font-family">Times</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="h4">
        <xsl:attribute name="font-size">10pt</xsl:attribute>
        <xsl:attribute name="font-family">Times</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="celdaCabeceraTabla">
        <xsl:attribute name="border-top-width">1px</xsl:attribute>
        <xsl:attribute name="border-top-color">black</xsl:attribute>
        <xsl:attribute name="border-top-style">solid</xsl:attribute>
        <xsl:attribute name="border-bottom-width">1px</xsl:attribute>
        <xsl:attribute name="border-bottom-color">black</xsl:attribute>
        <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
        <xsl:attribute name="padding">1px</xsl:attribute>
        <xsl:attribute name="font-family">Times</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="celdaInicioCabeceraTabla">
        <xsl:attribute name="border-top-width">1px</xsl:attribute>
        <xsl:attribute name="border-top-color">black</xsl:attribute>
        <xsl:attribute name="border-top-style">solid</xsl:attribute>
        <xsl:attribute name="border-bottom-width">1px</xsl:attribute>
        <xsl:attribute name="border-bottom-color">black</xsl:attribute>
        <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
        <xsl:attribute name="border-left-width">1px</xsl:attribute>
        <xsl:attribute name="border-left-color">black</xsl:attribute>
        <xsl:attribute name="border-left-style">solid</xsl:attribute>
        <xsl:attribute name="padding">1px</xsl:attribute>
        <xsl:attribute name="font-family">Times</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="celdaFinCabeceraTabla">
        <xsl:attribute name="border-top-width">1px</xsl:attribute>
        <xsl:attribute name="border-top-color">black</xsl:attribute>
        <xsl:attribute name="border-top-style">solid</xsl:attribute>
        <xsl:attribute name="border-bottom-width">1px</xsl:attribute>
        <xsl:attribute name="border-bottom-color">black</xsl:attribute>
        <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
        <xsl:attribute name="border-right-width">1px</xsl:attribute>
        <xsl:attribute name="border-right-color">black</xsl:attribute>
        <xsl:attribute name="border-right-style">solid</xsl:attribute>
        <xsl:attribute name="padding">1px</xsl:attribute>
        <xsl:attribute name="font-family">Times</xsl:attribute>
    </xsl:attribute-set>
</xsl:stylesheet>
