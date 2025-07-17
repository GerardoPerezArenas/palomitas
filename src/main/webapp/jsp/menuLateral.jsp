<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>

<%
  Config m_Config = ConfigServiceHelper.getConfig("common");
  String statusBar = m_Config.getString("JSP.StatusBar");
  int codAplicacion = 0;
  UsuarioValueObject usuarioVO = null;

  if(session != null)
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");

  if (usuarioVO != null)
    codAplicacion = usuarioVO.getAppCod();
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/> 
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= usuarioVO.getIdioma()%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />

<html:html>
    <head>
        <title>Cabecera de Aplicación</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type='text/javascript'>
            var APP_CONTEXT_PATH="<c:out value='${pageContext.request.contextPath}'/>";
            var modificando = "N";
            function lanzarProceso(proceso){
                if(proceso!=""){
                <%if (codAplicacion==4) {%> 
                        if (proceso == "<c:url value='/sge/DefinicionProcedimientos.do?opcion=catalogoProcedimientos'/>") {
                            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+proceso, "win",
                                    'width=950,height=570,status='+ '<%=statusBar%>',function(){});
                        } else {
                    if (top.mainFrame.document.forms[0] && top.mainFrame.document.forms[0].existenCambiosSinGrabar!=undefined) {
                        var mensajeGuardarCambios='<%=descriptor.getDescripcion("msjExistenDatosGuardar")%>';
                        var existe = top.mainFrame.document.forms[0].existenCambiosSinGrabar.value;
                                if (irActionConCambios(existe,mensajeGuardarCambios)) {
                            top.mainFrame.document.forms[0].existenCambiosSinGrabar.value=0;
                                    submitProceso(proceso);
                    }
                            } else {
                                submitProceso(proceso);
                            }
                        }
                    <%} else {%> 
                        if(modificando != "S" || jsp_alerta('C','<%= descriptor.getDescripcion("msjCabLanzarProc")%>')){
                                // Desea salir de esta pantalla?
                            submitProceso(proceso);
                            }
                    <%}%>
                }
            }
            
            function submitProceso(proceso) {
                            modificando = "N";
                            document.forms[0].target = "mainFrame";
                            document.forms[0].action = proceso;
                            document.forms[0].submit();
            }
        </script>
    </head>
    <body class="bandaBody" style="width:100%;height:100%">
        <jsp:include page="/jsp/menu.jsp" flush="true"/>
    </body>
</html:html>