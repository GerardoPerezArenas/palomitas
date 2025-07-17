<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/usuariosForms/tpls/app-constants.jsp" />
<title> Oculto Datos Usuarios </title>

<%
        int idioma=1;
        int apl=4;
        UsuarioValueObject usuarioVO = new UsuarioValueObject();

        if (session.getAttribute("usuario") != null){
                usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
        }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">

function redirecciona(){
    var respOpcion ='<bean:write name="UsuariosFormsForm" property="respOpcion"/>';
    if ( "insertarUsuario"==respOpcion ){
	  parent.mainFrame.devolver();
    } else if( "eliminarUsuario"==respOpcion ){
	  parent.mainFrame.usuarioEliminado();
	} else if( "grabar_modificacion"==respOpcion ){
	  parent.mainFrame.devolver();
	}
  }
</script>

</head>
	<body onLoad="redirecciona();">
		<form>
			<input type="hidden" name="opcion" value="">
		</form>
		<p>&nbsp;<p>
	</body>
</html>
