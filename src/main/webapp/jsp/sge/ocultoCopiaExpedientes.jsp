<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto lista de procedimientos </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

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
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

    <script>
    var datos = new Array();

    function redirecciona(){
        var error = '<c:out value="${requestScope.error}"/>';

        if (error != '') {
            if(error=='0') jsp_alerta('A', 'NO SE HA PODIDO INICIAR EL EXPEDIENTE');
            if(error=='1'){
                jsp_alerta('A', 'NO SE HAN PODIDO COPIAR LOS INTERESADOS');
                var datos = '<c:out value="${requestScope.expedienteCreado}"/>';
                parent.mainFrame.recibirNumero(datos);
            }
            if(error=='2'){
                jsp_alerta('A', 'NO SE HAN PODIDO COPIAR TODOS LOS DATOS SUPLEMENTARIOS');
                var datos = '<c:out value="${requestScope.expedienteCreado}"/>';
                parent.mainFrame.recibirNumero(datos);
            }
            if(error=='3'){
                jsp_alerta('A', 'NO SE HAN PODIDO COPIAR LOS INTERESADOS NI LOS DATOS SUPLEMENTARIOS');
                var datos = '<c:out value="${requestScope.expedienteCreado}"/>';
                parent.mainFrame.recibirNumero(datos);
            }
            
            pleaseWait('off');
        } else {
            var datos = '<c:out value="${requestScope.expedienteCreado}"/>';
            parent.mainFrame.recibirNumero(datos);
      }
    }
    </script>
</head>
<body onLoad="redirecciona();">
    <form>
    <input type="hidden" name="opcion" value="">
    </form>
    <p>&nbsp;<p><center>
</body>
</html>
