<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UserPreferences,
                java.util.Vector,
                java.util.Iterator" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioEscritorioValueObject" %>

<%
    int idioma = 1;
    UsuarioEscritorioValueObject usuarioEscritorioVO = null;
    UserPreferences userPreferences = null;

    if (session!=null){
        usuarioEscritorioVO = (UsuarioEscritorioValueObject)session.getAttribute("usuarioEscritorio");
        idioma =  usuarioEscritorioVO.getIdiomaEsc();
        userPreferences = (UserPreferences) usuarioEscritorioVO.getPreferences();
    }
    
    String busqueda = (String) request.getAttribute("busqueda");
    if (busqueda == null) busqueda = "";
    
    
    int numOrgs = ((Vector)session.getAttribute("org_ent")).size() / 7;
    // Calculamos unos 40 pixels por linea         
    int alturaTabla = numOrgs * 40;
    // Añadimos mas espacio para la barra de busqueda
    if (request.getAttribute("mostrarBarraBusqueda") != null) alturaTabla += 50;
    // Controlamos un min y max para la altura de la tabla
    if (alturaTabla<180) alturaTabla = 180;
    if (alturaTabla>400) alturaTabla = 400;
   %>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />

<html:html locale="true">
 <fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
 <head><jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
    <title>Selección de Organizacións e Entidades</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>" media="screen">

    <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
    <script language="JavaScript" type="text/javascript">
    function inicio() {
        self.parent.opener.retornoXanelaAuxiliar();
    }
    
    function buscar() {
        document.forms[0].opcion.value = 'busquedaOrgs';
        document.forms[0].codigo.value = "<%=request.getAttribute("codigo")%>";
        document.forms[0].aplicacion.value = "<%=request.getAttribute("aplicacion")%>";
        document.forms[0].action = '<c:url value='/GestEntrada.do'/>';
    }

    function retornoSeleccion(orgCod,org,entCod,ent,dtr,ico,ine) {
        var organizacion = new Object();
        organizacion.orgCod = orgCod;
        organizacion.org = org;
        organizacion.entCod = entCod;
        organizacion.ent = ent;
        organizacion.dtr = dtr;
        organizacion.ico = ico;
        organizacion.ine = ine;
        
        self.parent.opener.retornoXanelaAuxiliar(organizacion);
    }
    </script>
 </head>
 <BODY class="noappbody">
    <div class="txttitsmallblanco">
        <%=descriptor.getDescripcion("tit_Sel")%>
        <a href="javascript:inicio();">
            <span class="fa fa-sign-out" aria-hidden="true" title="<%= descriptor.getDescripcion("ico_SalirApp")%>"></span>
        </a>                                        
    </div>
    <div class="contenidoPantalla">
        <%if (request.getAttribute("mostrarBarraBusqueda") != null) { %>
            <form onsubmit="buscar();">
                <input type="hidden" name="opcion">
                <input type="hidden" name="codigo">
                <input type="hidden" name="aplicacion">
                 <table width="100%">
                     <tr>
                         <td width="60%" style="padding-left:30px">
                              <input class="inputTexto" type="text" name="busqueda" value="<%=busqueda%>" size="35" onkeypress="javascript:PasaAMayusculas(event);"/>
                         </td>
                         <td width="40%"> 
                            <input type="submit" title="<%=descriptor.getDescripcion("gbBuscar")%>" value="<%=descriptor.getDescripcion("gbBuscar")%>" class="botonGeneral">
                        </td>
                    </tr>
                </table>
            </form>
        <c:if test="${not empty requestScope.noResultadosBusqueda}">
            <div class="etiqueta" style="width: 100%; color: red; text-align: center"><%=descriptor.getDescripcion("msjNoResultados")%></div>
        </c:if>
        <% } %>                                    
            <table class="xTabla tablaGrande" width="100%">
                <thead>
                    <tr>
                        <th style="width:50%;text-align:center;" class="cabeceiraCol"><%=descriptor.getDescripcion("tit_Org")%></th>
                        <th style="width:50%;text-align:center;" class="cabeceiraCol"><%=descriptor.getDescripcion("tit_Ent")%></th>
                    </tr>
                </thead>
                <tbody>
                <%
                   Vector aplic = (Vector)session.getAttribute("org_ent");
                           Iterator iter = aplic.iterator();
                           while(iter.hasNext()){
                               String orgCod = (String)iter.next();
                               String org = (String)iter.next();
                               String entCod = (String)iter.next();
                               String ent = (String)iter.next();
                               String dtr = (String)iter.next();
                               String ico = (String)iter.next();
                               String ine = (String)iter.next();	                                                                                                        
               %>
                <!-- Aquí se meten n filas como esta, una para cada organizacion y entidad. -->
                    <tr>
                        <td style="width:50%;text-align:center;" class="fila">
                            <%=org%>
                        </td>
                        <td style="width:50%;text-align:center;">
                            <a class="enlace" href="javascript:retornoSeleccion('<%=orgCod%>','<%=org%>','<%=entCod%>','<%=ent%>','<%=dtr%>','<%=ico%>','<%=(ine==null?"":ine)%>')">
                                <%=ent%>
                            </a>
                        </td>
                    </tr>
                    <%
                        }
                        session.removeAttribute("org_ent");
                    %>
                </tbody>
            </table>
        </div>
    <!-- Fin lista organizaciones y entidades. -->
 </BODY>
</html:html>
