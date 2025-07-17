<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<html>
<head>
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Informe Registro </title>    
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    
<%    
    UsuarioValueObject usuario = new UsuarioValueObject();
    int idioma = 1;
    if (session != null) {
        if (usuario != null) {
            usuario = (UsuarioValueObject) session.getAttribute("usuario");            
            idioma = usuario.getIdioma();
        }
    }
%>    
    
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="4"/>
    
    <script language="javascript">
    function cargar() {
      var nombre ='<%=request.getAttribute("nombre")%>';
      var opcion ='<%=request.getAttribute("opcion")%>';
      var directorio='<%=request.getAttribute("dir")%>';
      var num = '<%=request.getAttribute("NUM_MAXIMO_EXPEDIENTES_EXPORTACION_EXCEDIDOS")%>';

      if (opcion=='exportarCSV'){
          if(num!=null && num!="null"){
             jsp_alerta("A","<%=descriptor.getDescripcion("msgErrLimiteMax1")%> " + num  + " <%=descriptor.getDescripcion("msgErrLimiteMax2")%>");
          }
          parent.mainFrame.abrirInformeCSV(nombre,directorio);
      }else {
          if(num!=null && num!="null"){
             jsp_alerta("A","<%=descriptor.getDescripcion("msgErrLimiteMax1")%> " + num  + " <%=descriptor.getDescripcion("msgErrLimiteMax2")%>");
          }
          parent.mainFrame.abrirInforme(nombre,directorio);
      }
    }
    </script>
</head>
<body onLoad="pleaseWait('off');cargar();">
</body>
</html>
