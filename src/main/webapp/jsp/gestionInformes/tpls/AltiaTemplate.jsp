<%@ page import="es.altia.agora.interfaces.user.web.util.CargaMenu"%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<script type="text/javascript">
    function lanzarProceso(proceso){

        if(proceso!=""){
          //proceso = "<%//=request.getContextPath()%>" + proceso;
          if (proceso == "<c:url value='/gestionInformes/GestionInformes.do?opcion=inicio'/>" ||
              proceso=="<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=generacion'/>" ||
              proceso=="<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=inicio'/>"||
              proceso=="<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=administracion'/>"||
              proceso=="<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=administracionModo'/>"||
              proceso=="<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=administracionCampos'/>"){
              document.location.href=proceso;
          } else if (proceso == "<c:url value='/sge/DefinicionProcedimientos.do?opcion=catalogoProcedimientos'/>") {
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+proceso,null,
                        'width=760,height=510,status='+ '<%=statusBar%>',function(){});
          } else {
                modificando = "N";
                document.forms[1].target = "_top";
                document.forms[1].action = "<c:url value='/jsp/escritorio/container.jsp?opcion='/>"+proceso;
                document.forms[1].submit();
          }
        }
    }
    VerCorrect=-1;
</script>
<div class="contedorGlobal">
    <div name="menu" class="iframeMenu">
        <jsp:include page="/jsp/menu.jsp" flush="true"/>
    </div>
    <div name="mainFrame" class="iframeMainFrame">
                <div class="txttitblanco">
                    <span>
                        <tiles:insert attribute="altia-app-form-title"/>
                    </span>
                </div>
        <div class="cuerpoAplicacion">
            <tiles:insert attribute="altia-app-form-content"/>
        </div>
    </div>
</div>
