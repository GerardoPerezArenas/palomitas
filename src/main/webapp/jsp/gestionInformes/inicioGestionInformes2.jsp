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

<script type="text/javascript">
    function pulsarDetalles() {
        var retorno = new Array();
        retorno[0]= "D";
        retorno[1]= document.forms[0].codPlantilla.value;
        self.parent.opener.retornoXanelaAuxiliar(retorno);
    }

    function pulsarInformes() {
        var retorno = new Array();
        retorno[0]= "I";
        retorno[1]= "";
        self.parent.opener.retornoXanelaAuxiliar(retorno);
    }
</script>
<html:form action="/gestionInformes/FichaInforme.do" target="_self">
    <html:hidden property="codPlantilla" styleId="codPlantilla" />
    <div class="nuevoInforme">
       <div>
            <span class="etiqMaxi"><%=descriptor.getDescripcion("gbInformeCreadoOK")%></span>
       </div>
    </div>
</html:form>
