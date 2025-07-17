<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%
    response.setHeader("Cache-control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
 %>
<%@ page contentType="text/html;charset=ISO-8859-15" language="java" pageEncoding="ISO-8859-15"%>
<html>
 <head>
     <title></title>
<script type="text/javascript">

    var anotaciones = new Array();
    var contador = 0;

    function redirecciona(){ 
        var DOT_COMMA = ";";
        var codigoOficinaUsuario = parent.mainFrame.codigoOficinaUsuario;
        var codigoOficinaDestino = parent.mainFrame.document.forms[0].codOficinaDestino.value;
        var codigoOficinaOrigen  = parent.mainFrame.document.forms[0].codOficinaOrigen.value;
        var codUorOficinaOrigen = parent.mainFrame.getCodigoInternoOficina(codigoOficinaOrigen);
             
        <logic:iterate name="AnotacionesOficinasRegistroForm" property="anotaciones" id="anotacion" scope="request">

            var numero      = '<bean:write name="anotacion" property="numero" ignore="true"/>';
            var tipoEntrada = '<bean:write name="anotacion" property="tipoEntrada" ignore="true"/>';
            var ejercicio     = '<bean:write name="anotacion" property="ejercicio" ignore="true"/>';
            var uorOrigen   = '<bean:write name="anotacion" property="uor" ignore="true"/>';
            var codDep      = '<bean:write name="anotacion" property="codDepartamento" ignore="true"/>';
            var codOfiOrigen      = '<bean:write name="anotacion" property="codOficinaOrigen" ignore="true"/>';
            var codOfiDestino      = '<bean:write name="anotacion" property="codOficinaDestino" ignore="true"/>';

            var valorCheck = numero + DOT_COMMA + ejercicio + DOT_COMMA + tipoEntrada + DOT_COMMA + codDep + DOT_COMMA + uorOrigen +DOT_COMMA +codOfiOrigen+DOT_COMMA +codOfiDestino;            
           
            var numeroAnotacion = '<bean:write name="anotacion" property="numeroEntrada" ignore="true"/>';
            var cheq = "<input type='checkbox' onclick='pulsarCheck()'  name='checkAnotacion' value='" + valorCheck + "'/>";
            anotaciones[contador] = [cheq,'<bean:write name="anotacion" property="fechaEntrada" ignore="true"/>','<bean:write name="anotacion" property="numeroEntrada" ignore="true"/>',
                                                 '<str:escape><bean:write name="anotacion" property="extracto" ignore="true"/></str:escape>',
						'<bean:write name="anotacion" property="interesado" ignore="true"/>',
                                                 '<bean:write name="anotacion" property="descripcionOficinaOrigen" ignore="true"/>','<bean:write name="anotacion" property="descripcionOficinaDestino" ignore="true"/>','<bean:write name="anotacion" property="descripcionUORDestino" ignore="true"/>',
                                                 '<bean:write name="anotacion" property="estadoAnotacion" ignore="true"/>'];
            contador++;
        </logic:iterate>

        var paginaActual                  = '<bean:write name="AnotacionesOficinasRegistroForm" property="pagina" ignore="true"/>';
        var numRelacionAnotaciones = '<bean:write name="AnotacionesOficinasRegistroForm" property="numRelacionAnotaciones" ignore="true"/>';
        var lineasPagina                   = '<bean:write name="AnotacionesOficinasRegistroForm" property="lineasPaginas" ignore="true"/>';
        
        parent.mainFrame.numRelacionAnotaciones = numRelacionAnotaciones;
        parent.mainFrame.lineasPagina                  = lineasPagina;
        parent.mainFrame.paginaActual                  = paginaActual;        
        parent.mainFrame.actualizarListadoAnotaciones(anotaciones);
   }

</script>
 </head>
 <body onload="javascript:redirecciona();">
 </body>
</html>    