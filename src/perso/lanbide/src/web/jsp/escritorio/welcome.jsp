<%@page contentType="text/html; charset=iso-8859-1"	language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page import="org.apache.struts.Globals"%>

<fmt:setLocale value='${initParam["sLocaleInicial"]}' scope="session"/>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title><fmt:message key="logon.title"/></title>
    <jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
    <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<html:rewrite page='/css/estilo.css'/>">
    
    <script type='text/javascript'>
    
    function inicializar(){
        if(window.navigator.appVersion.indexOf("MSIE")!=-1){
            var n = navigator.userAgent;
            var MSIEVer=n.substr(n.indexOf("MSIE ")+("MSIE ").length,4);
            MSIEVer=parseFloat(MSIEVer);
            if (MSIEVer < 5) {
                jsp_alerta("A", '<fmt:message key="error.navegador.notVersion"/>');
            } else {
                abrirLogin();
            }
        } else {
            abrirLogin();
        }
    }

    function abrirLogin(){
        var ventana_welcome = self;
        ventana_welcome.opener = self;
       /* if(!(ventana_login = window.open("<c:url value='/welcome.do'/>?<c:out value='${pageContext.request.queryString}' />",
				'', 'location=no,directories=no,status=no,menubar=no,scrollbars=no,width=' +
				window.outerWidth + ',height=' + window.outerHeight + ',resizable=yes,fullscreen=no,top=' + 
                                                                        window.screenY + ',left=' + window.window.screenX))) throw "ErrPop"
					window.open("<html:rewrite page="/jsp/escritorio/close.html"/>", '_self');*/
       window.location="<c:url value='/welcome.do'/>?<c:out value='${pageContext.request.queryString}' />";
    }
    
    </script>
    </head>
    
    <BODY STYLE='background-repeat:no-repeat; background-position:center center' onLoad="inicializar();">
    <logic:messagesPresent>
        <html:messages id="msg">
            <span class="tituloLogin"><br/><c:out value="${msg}"/></span>
        </html:messages>
    </logic:messagesPresent>


    </BODY>
	
</html>
