<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="java.util.Vector"%>
<%@ page contentType="text/html;charset=iso-8859-1"	language="java" %>
<html>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<title>::: Depuracion Vías	:::</title>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>

<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int	idioma=1;
    int apl=1;
    if (session.getAttribute("usuario") != null){
        usuarioVO =	(UsuarioValueObject)session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl =	usuarioVO.getAppCod();
    }

%>
<jsp:useBean id="descriptor" scope="request"
             class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"	property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"	property="apl_cod" value="<%=apl%>"	/>

<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/general.js"></script>
<script type="text/javascript" src="<html:rewrite page='/javascript/listas/listaComboBox.js'/>"></script>
<script type="text/javascript">
    function inicializar(){
        var cont = 0;
        var listaCod = new Array();
        var listaDesc = new Array();
    <%
            Vector municipios = (Vector) request.getAttribute("municipios");
            for (int i=0;i<municipios.size();i++) {
                GeneralValueObject gVO = (GeneralValueObject) municipios.get(i);
    %>
        listaCod[cont] = "<%=(String)gVO.getAtributo("codMunicipio")%>";
        listaDesc[cont] = "<%=(String)gVO.getAtributo("nombreOficial")%>";
        cont = cont + 1;
    <%
            }
    %>
        parent.mainFrame.comboMunicipios.addItems(listaCod, listaDesc);
        <% if (request.getAttribute("actionOrigen")!=null && request.getAttribute("actionOrigen").equals("volver")) { %>
            parent.mainFrame.comboMunicipios.buscaCodigo(parent.mainFrame.document.forms[0].codMunicipio.value);
        <% } else { %>
            parent.mainFrame.comboMunicipios.buscaLinea(1);
        <% } %>
    }

</script>
<body onload="inicializar();">
<p>&nbsp;</p><center/>
</body>
</html>
