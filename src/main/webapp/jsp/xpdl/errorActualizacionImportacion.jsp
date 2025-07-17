<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.lang.Integer"%>
<%@page import="es.altia.agora.business.util.LabelValueTO"%>
<%@page import="es.altia.agora.business.sge.ErrorImportacionXPDL"%>

<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<html:html>
 <%
    int idioma = 1;
    int aplicacion = 20;
    UsuarioValueObject usuario = null;
    String entidad = "";
    String usu = "";
    if (session != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            aplicacion = usuario.getAppCod();
            entidad = usuario.getEnt();
            usu = usuario.getNombreUsu();
        }
    }

    String parametros [] = usuario.getParamsCon();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    ArrayList idiomas = (ArrayList)session.getAttribute("listaIdiomas");

%>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= aplicacion %>" />

<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>


<link rel="stylesheet" type="text/css" href="<c:url value='/css/sge_basica.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';

    function refrescarLista(){
        location.reload(true);
    }

    function pulsarSiguiente(){
        window.top.location.href="<%=request.getContextPath()%>" + "/xpdl/DocumentoXPDL.do?opcion=cancelarImportacion";
    }


</script>
</head>
<title><%=descriptor.getDescripcion("ConvXPDLFormTitle")%></title>

<html:form method="POST" action="/xpdl/DocumentoXPDL.do">
<html:hidden property="listaCodigosUorsTramitadorasTramite"/>
<html:hidden property="codigoUorInicioManualTramite"/>
<html:hidden property="codigoTramite"/>

<BODY class="bandaBody" style="height:100%;width:100%">
    <div class="txttitblanco"><%=descriptor.getDescripcion("errActualizacionProcImportado")%></div>
    <div class="contenidoPantalla">
        <div style="overflow-y:auto; height:400px">
            <table width="100%">
             <tr>
                 <td>
                    <span class="etiqueta"><strong>
                     <%=descriptor.getDescripcion("subtituloErroresExportacion")%>
                    </strong>
                    </span>
                 </td>
             </tr>
             <!-- Se listan los warning producidos durante la importación del procedimiento -->
             <logic:iterate scope="session" name="erroresXPDL" id="error">
             <tr>
                 <td>
                        <li>
                        <span class="etiqueta">
                            <c:out value="${error.descripcionError}"/>
                        </span>
                        </li>
                 </td>
             </tr>
            </logic:iterate>
        </table>
    </div>
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSiguiente")%>"
                            name="botonSiguiente" onClick="pulsarSiguiente();">
    </DIV>
</div>
</BODY>
</html:form>
</html:html>
