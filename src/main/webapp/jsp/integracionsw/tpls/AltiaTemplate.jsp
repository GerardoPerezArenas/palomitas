<%@ page import="es.altia.agora.interfaces.user.web.util.CargaMenu"%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<div style="height:100%;width:100%">
    <!-- CABECERA PAGINA -->
    <div name="menu" class="iframeMenu">
        <jsp:include page="/jsp/menu.jsp" flush="true"/>
    </div>

    <!-- CUERPO PÁGINA -->
    <div name="mainFrame" class="iframeMainFrame">
        <div class="txttitblanco"><tiles:insert attribute="altia-app-form-title"/></div>
        <div class="contenidoPantalla">
            <tiles:insert attribute="altia-app-form-content"/>
        </div>
    </div>
</div>
