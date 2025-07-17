<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
            int idioma = 1;
            int apl = 1;
            String css = "";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    css = usuario.getCss();
                }
            }
%>
<html>
<head>
<title>::: Anulación Reserva :::</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/> 
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<!-- Ficheros JavaScript -->
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
    
    function pulsarCancelar() {
        var datos = new Array();
        datos['aceptar'] = false;
        self.parent.opener.retornoXanelaAuxiliar(datos);
    }
    
    function pulsarAnular(){
        if (validarObligatoriosAqui()) {
            var datos = new Array();
            datos['aceptar'] = true;
            datos['diligencia'] = document.forms[0].txtDiligenciasAnulacion.value;
            self.parent.opener.retornoXanelaAuxiliar(datos);
        }
    }
    
    function validarObligatoriosAqui() {
    // Longitud del textArea
    if (document.forms[0].txtDiligenciasAnulacion.value.length <= 0) {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjAbrirCond10")%>');
            document.forms[0].txtDiligenciasAnulacion.focus();
            return false;
        }
        if (document.forms[0].txtDiligenciasAnulacion.value.length > 4000) {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjDiligAnul")%>');
            document.forms[0].txtDiligenciasAnulacion.focus();
            return false;
        }
        return true;
    }
    
    function checkKeysLocal(evento,tecla){
        if(window.event) evento = window.event;
        if ('Alt+A' == tecla)
            pulsarAnular();
        else if ('Alt+C' == tecla)
            pulsarCancelar();
    }

</script>
</head>

<body class="bandaBody" onload="javascript:{};">
    <form>
        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titAnularReserva")%></div>
        <div class="contenidoPantalla">
            <table>
                <tr>
                    <td class="sub3titulo"><script> document.writeln(self.parent.opener.xanelaAuxiliarArgs); </script></td>
                </tr>
                <tr>	
                    <td style="width: 100%" class="etiqueta"><%=descriptor.getDescripcion("etiq_DiligAnul")%></td>
                </tr>
                <tr>	
                    <td class="columnP" style="width: 100%">
                        <textarea name="txtDiligenciasAnulacion" class='inputTextoObligatorio' cols="100" rows="16" value="" onblur="return xAMayusculas(this);"></textarea>
                    </td>
                </tr>
                <tr>
                    <td class="columnP" align="center" style="padding-bottom:50px"></td>
                </tr>
            </table>
            <div class="botoneraPrincipal">
                <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAnular")%> 
                                       name="cmdAnular" onClick="pulsarAnular();">
                <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> 
                                       name="cmdCancelar"  onClick="pulsarCancelar();">
        </div>
    </div>
</form>
</body>
</html>
