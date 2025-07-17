<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
    String idSesion = session.getId();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");

    int idioma = 1;
    UsuarioValueObject usuario = null;
    if (session != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) idioma = usuario.getIdioma();
    }
%>

<html>
<head>
    <title> Informe Registro </title>
    <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
    <script src="<c:url value='/scripts/general.js'/>"></script>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>"/>
    <jsp:setProperty name="descriptor" property="apl_cod" value="1"/>
    <script language="javascript">
    var Frame;
    var tipoVentana = 'normal';

    function cargar(){
      pleaseWait('off');
      var nombre ='<%=request.getAttribute("nombre")%>';      
      if(nombre!=null && nombre.length>0 && nombre!="" && nombre!="NO EXISTE"){
          var sourc = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre=" + nombre;
          ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + sourc, 'ventanaInforme', 'width=1000px,height=750px,status=' + '<%=statusBar%>' + ',toolbar=no');
          ventanaInforme.focus();
      }else 
          jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoAsientos")%>');
    }
    </script>
</head>
<body onLoad="cargar();">
</body>
</html>