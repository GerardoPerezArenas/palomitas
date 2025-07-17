<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
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
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>


<script type="text/javascript">


    function validarCampos(){
        var nombre = document.forms[1].nombre.value;
        if (nombre!="")
            return true;
        return false;
    }

    function pulsarAlta() {
        if (validarCampos()){
            document.forms[1].opcion.value = "altaSW";
            document.forms[1].action = "<c:url value='/integracionsw/IntegracionSW.do'/>";
            document.forms[1].submit();
        } else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');            
        }
    }
</script>
    <html:form action="/integracionsw/IntegracionSW.do" target="_self">
    	<div class="nuevoInforme">
        <div>
        	<span class="etiq">T&iacutetulo</span>
           	<html:text property="nombre" styleId="nombre" styleClass="inputTextoObligatorio" size="56" value=""  />
           	<span class="etiq">WSDL</span>
           	<html:text property="wsdl" styleId="wsdl" styleClass="inputTextoObligatorio" size="56" value="" />
        </div>
          
    </html:form>
