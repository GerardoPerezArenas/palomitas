<%@ page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%--
  Created by IntelliJ IDEA.
  User: daniel.sambad
  Date: 07-feb-2007
  Time: 16:40:51
  To change this template use File | Settings | File Templates.
--%>

<form name="form" action="/gestionInformes/GestionInformes.do" target="_self">
    <input type="hidden" name="opcion">
</form>


<script type="text/javascript">
function inicializar() {
    document.forms[1].opcion.value = "inicio";
    document.forms[1].action = DOMAIN_NAME + "<c:url value='/gestionInformes/GestionInformes.do'/>";
    document.forms[1].submit();
}

inicializar();    
</script>
