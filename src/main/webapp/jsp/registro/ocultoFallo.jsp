<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head>
<title> Fallo en la busqueda en altaRE </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
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

<script src="<%=request.getContextPath()%>/scripts/general.js"></script>

<script>

function redirecciona(){

        var texto1='<%=descriptor.getDescripcion("txtAnotacInex")%>';
		jsp_alerta("A",texto1);

        // parent.mainFrame.pulsarCancelarAlta();
		parent.mainFrame.noRecuperaDatos();


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
