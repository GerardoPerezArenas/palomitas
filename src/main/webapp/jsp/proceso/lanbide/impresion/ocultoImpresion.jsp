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
    <title> Oculto impresion </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
      UsuarioValueObject usuarioVO = new UsuarioValueObject();
      int idioma=1;
      int apl=4;

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

    <script>
        
    function redirigir(){
            document.forms[0].opcion.value="impresionCEPAP";
            document.forms[0].target="mainFrame";
            document.forms[0].action = "<%=request.getContextPath()%>/lanbide/impresion/ImpresionExpedientesLanbide.do?";
            document.forms[0].submit();
    }
    
    function cargar() {
          var nombre ='<%=request.getAttribute("nombre")%>';
          var opcion ='<%=request.getAttribute("opcion")%>';
          var directorio='<%=request.getAttribute("dir")%>';
          var resInsertTable='<%=request.getAttribute("resInsertTable")%>';
          if (opcion=='exportarCEPAPCSV'){

            //ha ido bien la insercion en ambas tablas de impresion
            if (resInsertTable == 1){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjExitoInsertImpresion")%>');
                  //parent.mainFrame.abrirInformeCSV(nombre,directorio);
            }else{
                if (resInsertTable == 0){
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjErrorInsertImpresion")%>');
                }else{
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjErrorGeneralImpresion")%>');
                }

            }
            redirigir();
          }
    }
    </script>
</head>
<body onLoad="cargar();">
    <form>
    <input type="hidden" name="opcion" value="">
    </form>

    <p>&nbsp;<p><center>
</body>
</html>
