<%@ page import="es.altia.agora.interfaces.user.web.util.CargaMenu"%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%
	int aplicacionCod = 0;
   	String aplicacion = "";
   	String usu = "";
   	String entidad = "";
	int idioma = 0;
	UsuarioValueObject usuarioVO = null;

   	if(session != null)
    	usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");

	if (usuarioVO != null){
       	aplicacion = usuarioVO.getApp().toUpperCase();
       	usu = usuarioVO.getNombreUsu();
       	entidad = usuarioVO.getEnt();
		idioma = usuarioVO.getIdioma();
	}
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= usuarioVO.getIdioma()%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= usuarioVO.getAppCod()%>" />
<script type='text/javascript'>
    function lanzarProceso(proceso){
        if (proceso!="") {
            document.location.href=proceso;
        }
    }
    TargetLoc='menu';
    VerCorrect=-1;
</script>
<!-- ******************* CUERPO DE LA PLANTILLA *********************** -->
<div class="contedorGlobal">
    <div name="menu" class="iframeMenu">
        <jsp:include page="/jsp/menu.jsp" flush="true"/>
    </div>
    <div name="mainFrame" class="iframeMainFrame">
        <div class="txttitblanco"><tiles:insert attribute="altia-app-form-title"/></div>
        <div class="contenidoPantalla">
                <tiles:insert attribute="altia-app-form-content"/>
        </div>
        <div id="popupcalendar"></div>
    </div>
</div>
