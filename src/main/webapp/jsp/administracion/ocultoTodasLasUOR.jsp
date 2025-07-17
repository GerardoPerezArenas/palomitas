<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>


<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<title> Oculto Datos Usuarios </title>

<%
        int idioma=1;
        int apl=4;
        UsuarioValueObject usuarioVO;

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
<script type="text/javascript">
var codigos = new Array();
var descripciones = new Array();
var codigos2 = new Array();
var descripciones2 = new Array();

function redirecciona(){
    var respOpcion ='<bean:write name="UsuariosGruposForm" property="respOpcion"/>';
    if ("cargarTodasLasUor"==respOpcion){
        
        var uorcods = new Array();
        var uordescs = new Array();
        var uorPad = new Array();
        
        var codOrganizacion = '<%=request.getAttribute("codOrganizacion")%>';
        var descOrganizacion = '<%=request.getAttribute("descOrganizacion")%>';
        var codEntidad = '<%=request.getAttribute("codEntidad")%>';
        var descEntidad = '<%=request.getAttribute("descEntidad")%>';
        var codCargo = '<%=request.getAttribute("codCargo")%>';
        var descCargo = '<%=request.getAttribute("descCargo")%>';
        var listaUorsCascada = '<%=request.getAttribute("listaUorsCascada")%>';
        var infoListasUors = '<%=request.getAttribute("infoListasUors")%>';
        var permisoCascadaChecked = '<%=request.getAttribute("permisoCascadaChecked")%>';
        var tieneDescendencia = '<%=request.getAttribute("tieneDescendencia")%>';
        var infoPadreListasUors = '<%=request.getAttribute("infoPadreListasUors")%>';


        <% 
	Vector listaUORDTOs = (Vector)request.getAttribute("listaUORs");
	if (listaUORDTOs == null) listaUORDTOs = new Vector();
	
	for (int j=0; j<listaUORDTOs.size(); j++) {
		UORDTO dto = (UORDTO)listaUORDTOs.get(j);%>                
                uorcods[<%=j%>] = '<%=dto.getUor_cod()%>';
		uordescs[<%=j%>] = '<%=StringEscapeUtils.escapeJavaScript(dto.getUor_nom())%>';
                uorPad[<%=j%>] = '<%=StringEscapeUtils.escapeJavaScript(dto.getUor_pad())%>';
	<%}%>

        parent.mainFrame.rellenarTodasLasUnidades(uorcods, uordescs, uorPad, codOrganizacion, descOrganizacion, codEntidad, descEntidad, codCargo,
                                                            descCargo, listaUorsCascada, infoListasUors, permisoCascadaChecked, tieneDescendencia,
                                                            infoPadreListasUors);
        
    }//redirecciona
}//redirecciona
</script>
</head>
<body onLoad="redirecciona();">
<form>
<input type="hidden" name="opcion" value="">
</form>
<p>&nbsp;<p>
</body>
</html>