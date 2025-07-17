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
<%
    response.setHeader("Cache-control","no-cache");
    response.setHeader("Pragma","no-cache");
    response.setDateHeader ("Expires", 0);

    int idioma = 1;
    int apl = 1;
    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            apl = usuario.getAppCod();
        }
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript">

    <c:if test="${not empty requestScope.error}">
            jsp_alerta('A', '<c:out value="${requestScope.error}"/>');
    </c:if>
    <c:if test="${not empty requestScope.estado}">
            jsp_alerta('A', '<c:out value="${requestScope.estado}"/>');
    </c:if>
    <c:if test="${not empty requestScope.opcion}">

            <c:if test="${requestScope.opcion == 'cerrarVentana'}">
                cerrarVentana();
            </c:if>
    </c:if>

    function validarNombre(){
        var nombre = document.forms[1].titulo.value;
        if (nombre!="")
            return true;
        return false;
    }

    function validarWSDL(){
        var wsdl = document.forms[1].wsdl.value;
        if (wsdl!="")
            return true;
        return false;
    }



    function pulsarCrear(){

    	if (validarNombre() && validarWSDL()) {
	    	document.forms[1].opcion.value = "nuevoSW";
	        document.forms[1].action = "<c:url value='/integracionsw/AltaSW.do'/>";
	        document.forms[1].submit();
    	}
    	else
	    	jsp_alerta("A", '<%=descriptor.getDescripcion("msjNoCamposVacios")%>');
    }

    function cerrarVentana() {
        var datos = new Array();
        datos[0] = 'iniciaCargaOperaciones';
        datos[1] = <bean:write name="AltaSWForm" property="codigoSW"/>
        self.parent.opener.retornoXanelaAuxiliar(datos);

    }

</script>
    <html:form action="/integracionsw/AltaSW.do" target="_self">
        <input type="hidden" name="opcion" value="">

        <div class="nuevoSW">
        	<span class="etiqSW"><%=descriptor.getDescripcion("iswEtiqTit")%></span>
           	<html:text property="tituloNewSW" styleId="titulo" styleClass="inputTextoObligatorio" style="width:475px" size="80" value=""  />
           	<span class="etiqSW"><%=descriptor.getDescripcion("iswEtiqWSDL")%></span>
           	<html:text property="wsdlNewSW" styleId="wsdl" styleClass="inputTextoObligatorio" style="width:475px" size="80" value="" />

        </div>
          
    </html:form>
