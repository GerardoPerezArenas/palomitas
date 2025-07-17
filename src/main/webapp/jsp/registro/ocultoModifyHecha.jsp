<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head>
<title> Modificación Hecha </title>

<%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma=1;
	int apl=1;

        if (session.getAttribute("usuario") != null){
                usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
        }

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
var frame;
if(parent.mainFrame){
    frame = parent.mainFrame;
} else {
    frame = parent;
}
function redirecciona(){
        var texto1='<%=descriptor.getDescripcion("modifyHecha")%>';
		var respModificacion = '<bean:write name="MantAnotacionRegistroForm" property="respOpcion"/>';
		var fechaComprobacion = '<bean:write name="MantAnotacionRegistroForm" property="fechaComprobacion"/>';
                var fechaComprobacionSinHora=(fechaComprobacion.substring(0,10));

		if (respModificacion == 'registrar_actualizacion_sin_confirmar'){
                    frame.confirmarModificacion();
                } else if (respModificacion == 'fecha_posterior_a_actual') {
                    frame.alertarFechaPosterior();
                } else if (respModificacion == 'actualizacion_sin_confirmar2'){
					frame.confirmarModificacion2();   
		} else if (respModificacion == 'registro_cerrado'){
					frame.registroCerradoAlModificar();   
		} else if (respModificacion == 'no_existe_expediente') {
                    frame.noExisteExpediente();
        } else if (respModificacion == 'proc_mal_relacionado') {
                    frame.procMalRelacionado();
        } else if (respModificacion == 'registrar_actualizacion_fecha_anterior_sin_confirmar') {
            frame.confirmarModificacionFechaAnteriorPosterior('<%=descriptor.getDescripcion("msjFechaAnterior")%> ' + fechaComprobacionSinHora);
        } else if (respModificacion == 'registrar_actualizacion_fecha_posterior_sin_confirmar') {
            frame.confirmarModificacionFechaAnteriorPosterior('<%=descriptor.getDescripcion("msjFechaAnterior")%> ' + fechaComprobacionSinHora);
        } else if (respModificacion == 'modify_realizado_duplicar') {
                frame.document.forms[0].duplicar.value="1";
            frame.document.forms[0].ano.value = "<c:out value="${requestScope.ano}"/>";
            frame.document.forms[0].numero.value ="<c:out value="${requestScope.num}"/>";
            frame.document.forms[0].fechaAnotacion.value = "<c:out value="${requestScope.fechaAnotacion}"/>";
            frame.document.forms[0].horaMinAnotacion.value = "<c:out value="${requestScope.horaAnotacion}"/>";
            frame.document.forms[0].fechaDocumento.value = "<c:out value="${requestScope.fechaAnotacion}"/>";
            frame.document.forms[0].horaMinDocumento.value = "<c:out value="${requestScope.horaAnotacion}"/>";
                frame.anotacionModificada();
		}else{
            frame.document.forms[0].ano.value = "<c:out value="${requestScope.ano}"/>";
            frame.document.forms[0].numero.value = "<c:out value="${requestScope.num}"/>";
                frame.document.forms[0].duplicar.value="0";
                frame.anotacionModificada();
        }
        // Al Modificar una Salida/Entrada.
        // Si falla o va OK, hay que mostrar al usuario y hacer que cargue los datos del registro que se acaba de de Modificar
        // Sin afectar el flujo Original
        var tipoRegistroES = '<bean:write name="MantAnotacionRegistroForm" property="tipoReg"/>';
        var requiereGestionSIR = '<bean:write name="MantAnotacionRegistroForm" property="requiereGestionSIR"/>';
        if(requiereGestionSIR=="true"){
            var mensajeSIR = '<bean:write name="MantAnotacionRegistroForm" property="descEstadoRespGestionSIR"/>';
            frame.mostrarMensajeAltaSIR(mensajeSIR);
        }
}
</script>
</head>
<body onLoad="redirecciona();">
<form>
<input type="hidden" name="opcion" value="">
</form>
<p>&nbsp;</p>
</body>
</html>
