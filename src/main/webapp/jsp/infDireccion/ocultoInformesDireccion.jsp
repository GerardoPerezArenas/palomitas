<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<% 
String opcion = (String) request.getParameter("opcion");
String opcionAnterior = (String)session.getAttribute("opcionSinUOR"); 
%>

<html>
<head><jsp:include page="/jsp/infDireccion/tpls/app-constants.jsp" />
<title> Oculto Informes Direccion </title>
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>

<script>
function redirecciona(){
    try{
      <%if("consultaExpedientes".equals(opcion)) { %>
        if ( parent.mainFrame.irAExpedientes)
            parent.mainFrame.irAExpedientes();
        else
            parent.mainFrame.irAExpedientes();
      <% } else if("ExpAreaProcedimiento".equals(opcion)) { %>
        if ( parent.mainFrame.recuperaExpAreaProcedimiento)
            parent.mainFrame.recuperaExpAreaProcedimiento();
        else
            parent.mainFrame.recuperaExpAreaProcedimiento();
      <% } else if("ExpUnidTramitadora".equals(opcion)) { %>
        if ( parent.mainFrame.recuperaExpUnidTramitadora)
            parent.mainFrame.recuperaExpUnidTramitadora();
        else
            parent.mainFrame.recuperaExpUnidTramitadora();
      <% } else if("ExpProcUnidTramit".equals(opcion)) { %>
        if ( parent.mainFrame.recuperaExpProcUnidTramit)
            parent.mainFrame.recuperaExpProcUnidTramit();
        else
            parent.mainFrame.recuperaExpProcUnidTramit();
      <% } else if("ExpProcTipoTramite".equals(opcion)) { %>
        if ( parent.mainFrame.recuperaExpProcTipoTramite)
            parent.mainFrame.recuperaExpProcTipoTramite();
        else
            parent.mainFrame.recuperaExpProcTipoTramite();
    <%--  <% } else if("consultaPiramide".equals(opcion)) { %>
        framePrincipal.recuperaPiramide();      
      <% } else if("estadisticasPadron".equals(opcion)) { %>
        framePrincipal.recuperaEstadisticasPadron();--%>
      <% } else if("registroLibroE".equals(opcion)||"registroLibroS".equals(opcion)
            ||"registroUORE".equals(opcion)||"registroUORS".equals(opcion)
            ||"registroTotales".equals(opcion)||"seleccionarUOR".equals(opcion) ) { %>
        if ( parent.mainFrame.recuperaRegistro)
            parent.mainFrame.recuperaRegistro('<%=opcion%>', '<%=opcionAnterior%>');
        else
            parent.mainFrame.recuperaRegistro('<%=opcion%>', '<%=opcionAnterior%>');        
      <% } %>
      } catch (error){
          alert("Error.descripcion: " + error.description);
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
