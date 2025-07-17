<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <TITLE>Oculto Buzon de Entrada</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma=2;
        int apl=4;

        if(session.getAttribute("usuario") != null){
            usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
        }
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
        type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor" property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>" />

    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
</head>
<BODY>
    <form method="post">
        <input type="hidden" name="opcion" id="opcion"/>
        <input type="hidden" name="recargar" id="recargar"/>
    </form>

    <script>
        function recuperaDatos(){
            document.forms[0].opcion.value = "cargar";
            document.forms[0].recargar.value = "true";
            document.forms[0].target = "oculto";
            document.forms[0].action="<c:url value='/sge/BuzonEntrada.do'/>";
            document.forms[0].submit();
        }

        <% 	String opcion = request.getParameter("opcion");
            String error =	request.getParameter("error");
            if ("rechazar".equals(opcion)){
                if ("true".equals(error)){  					%>
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoRech")%>');
        <%	    }else{  										%>
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjRech")%>');
                    recuperaDatos();
        <%  	}
            }else if ("aceptar".equals(opcion)){
                if ("true".equals(error)) {  					%>
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoAcept")%>');
        <%	    }else{  										%>
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjAcept")%>');
                    recuperaDatos();
        <%  	}
            }  													%>

    </script>
</BODY>
</html:html>
