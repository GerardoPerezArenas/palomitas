<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

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

<html>
 <head>
     <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
     <jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
    <title> Oculto cambiar clave </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script type="text/javascript" language="JavaScript" src="<c:url value='/scripts/general.js'/>"></script>
    <script>
    function redirecciona(){
      parent.mainFrame.noClave();
    }
    </script>
 </head>
 <body onLoad="redirecciona();">
    <p>&nbsp;<p>
 </body>
</html>
