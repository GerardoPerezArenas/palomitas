<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>

<div id="filtrosProcedentesOtraAdmin" style="display:none;">
    <table style="width:100%">
        <tr>
            <td colspan="4" class="sub3titulo">Filtros procedentes de otra administraci&oacute;n</td>
        </tr>
        <tr>
            <td class="etiqueta">Fecha origen desde:</td>
            <td class="columnP"><html:text property="fechaDesdeOrigen" styleClass="inputTxtFecha" size="10" maxlength="10"/></td>
            <td class="etiqueta">Fecha origen hasta:</td>
            <td class="columnP"><html:text property="fechaHastaOrigen" styleClass="inputTxtFecha" size="10" maxlength="10"/></td>
        </tr>
        <tr>
            <td class="etiqueta">Fecha env&iacute;o desde:</td>
            <td class="columnP"><html:text property="fechaDesdeEnvioLanbide" styleClass="inputTxtFecha" size="10" maxlength="10"/></td>
            <td class="etiqueta">Fecha env&iacute;o hasta:</td>
            <td class="columnP"><html:text property="fechaHastaEnvioLanbide" styleClass="inputTxtFecha" size="10" maxlength="10"/></td>
        </tr>
        <tr>
            <td class="etiqueta">Destino:</td>
            <td colspan="3" class="columnP">
                <html:select property="destinoSir" style="width:98%" styleClass="inputTexto">
                    <html:option value=""></html:option>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="etiqueta">N&ordm; registro:</td>
            <td class="columnP"><html:text property="numeroRegistroSir" styleClass="inputTexto" size="20" maxlength="50"/></td>
            <td class="etiqueta">Identificaci&oacute;n:</td>
            <td class="columnP"><html:text property="idRemitenteSir" styleClass="inputTexto" size="15" maxlength="50"/></td>
        </tr>
        <tr>
            <td class="etiqueta">Nombre:</td>
            <td class="columnP"><html:text property="nombreRemitenteSir" styleClass="inputTexto" size="20" maxlength="50"/></td>
            <td class="etiqueta">Apellidos:</td>
            <td class="columnP"><html:text property="apellidosRemitenteSir" styleClass="inputTexto" size="25" maxlength="100"/></td>
        </tr>
    </table>
</div>
