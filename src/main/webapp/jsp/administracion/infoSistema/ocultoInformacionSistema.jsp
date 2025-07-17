<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>




<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.SessionInfo"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.MyHttpSessionListener"%>


<%@ page import="es.altia.agora.interfaces.user.web.administracion.InformacionSistemaForm"%>


<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Oculto Informacion Sistem </title>

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

    <script type="text/javascript">

function redirecciona() {

    var memoria='<c:out value="${requestScope.InformacionSistemaForm.JVMUsedMemory}"/> MB./ <c:out value="${requestScope.InformacionSistemaForm.JVMTotalMemory}"/> MB.';
    var level='<c:out value="${requestScope.InformacionSistemaForm.nivelLog}"/> ';
    var resultado='<c:out value="${requestScope.InformacionSistemaForm.resultadoString}"/> ';
    var resultadoExp='<c:out value="${requestScope.InformacionSistemaForm.resultadoStringExp}"/> '; 
    var operacion='<c:out value="${requestScope.InformacionSistemaForm.operacion}"/>';

    if (operacion=="recolectarMemoria") parent.mainFrame.actualizarMemoria(memoria);
    else if (operacion=="invalidarOk"){
        jsp_alerta('A', '<%=descriptor.getDescripcion("msg_SesionInval")%>');
        rellenarSesiones();
    }
    else if (operacion=="invalidarNoOk")
    {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msg_NoSesionInval")%>');
    }
    else if(operacion=="modificarNivelLog")
    {
        parent.mainFrame.actualizarNivelLog(level);
    }
    else if (operacion=="envioEmailNoOk")
        {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msg_MailNoEnviado")%>');
        }
        
    
    
    parent.mainFrame.actualizarResultados(resultado);
    parent.mainFrame.actualizarResultadosExp(resultadoExp);
                  
 }

    function rellenarSesiones()
    {
        var j=0

        listaSesiones=new Array();
        <%

        HashMap mapa=MyHttpSessionListener.getInfoSessions();
        Collection coleccionSesiones=mapa.values();
        
        Iterator iter = coleccionSesiones.iterator();

          while (iter.hasNext()){

              SessionInfo infoSesion=(SessionInfo) iter.next();
               %>
                listaSesiones[j]=["<%=infoSesion.getId()%>","<%=infoSesion.getCreationTimeAsString()%>","<%=infoSesion.getLastAccesedTimeAsString()%>","<%=(infoSesion.getMaxInactiveInterval())/60%> min","<%=(infoSesion.getIdleTime())/1000%> sec.","<%=(infoSesion.getTTL())/1000%> sec."];
                j++;
                <%
            }
        %>
         parent.mainFrame.tabSesiones.lineas= listaSesiones;
         parent.mainFrame.tabSesiones.displayTabla();
    }
    
    
    function rellenarResultado()
    {
        
    }
    </script>
</head>
<body onLoad="redirecciona();">

</body>
</html>