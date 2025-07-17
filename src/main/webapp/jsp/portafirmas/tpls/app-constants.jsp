<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ page import="es.altia.util.struts.StrutsUtilOperations" %>
<%@ page import="es.altia.common.service.config.*" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%
Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

String hostVirtual = m_ConfigTechnical.getString("hostVirtual");
String protocolo = StrutsUtilOperations.getProtocol(request);
String host="";
if (hostVirtual.equals("")) 
    host = protocolo + "://" + request.getHeader("host") + request.getContextPath();
else host = hostVirtual + request.getContextPath();

Config m_Docus = ConfigServiceHelper.getConfig("Portafirmas");
String organizacion = "0";
String clienteFirma = m_Docus.getString(organizacion+"/PluginPortafirmas");
if (session!=null){
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                if (usuario!=null) {
                        organizacion = String.valueOf(usuario.getOrgCod());
                        clienteFirma = m_Docus.getString(organizacion+"/PluginPortafirmas");
                }
        }


%>

<script type="text/javascript">
    var APP_TITLE="<fmt:message key='Portafirmas.Application.title'/>";
    var APP_CONTEXT_PATH="<c:out value='${pageContext.request.contextPath}'/>";
    var DOMAIN_NAME = '<%=host%>';
    var CLIENTE_FIRMA = '<%=clienteFirma%>';
    
</script>
