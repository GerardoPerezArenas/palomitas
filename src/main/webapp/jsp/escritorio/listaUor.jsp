<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="java.util.Vector,java.util.Iterator" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioEscritorioValueObject" %>
<%  int idioma = 1;
    UsuarioEscritorioValueObject usuarioEscritorioVO = null;
    if (session!=null){
        usuarioEscritorioVO = (UsuarioEscritorioValueObject)session.getAttribute("usuarioEscritorio");
        idioma =  usuarioEscritorioVO.getIdiomaEsc();
    } %>
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

    <script type="text/javascript">
    function inicio() {
        self.parent.opener.retornoXanelaAuxiliar();
    }
    
    function retornoSeleccion(unidadOrgCod,unidadOrg) {
        var organizacion = new Object();
        organizacion.orgCod = unidadOrgCod;
        organizacion.org = unidadOrg;
        
        self.parent.opener.retornoXanelaAuxiliar(organizacion);
    }
    </script>
 </head>
 <body class="noappbody">
    <div class="txttitsmallblanco">
        <%= descriptor.getDescripcion("gEtiq_selOfiReg")%>
        <a href="javascript:inicio();">
            <span class="fa fa-sign-out fa-3x" aria-hidden="true" title="<%= descriptor.getDescripcion("ico_SalirApp")%>"></span>
        </a>                                        
    </div>
    <div class="contenidoPantalla">
        <table class="xTabla tablaGrande" width="100%">
            <tbody>
            <!-- Aquí se meten n filas como esta, una para cada departamento y unidad organica. -->
            <%
                Vector aplic = (Vector)session.getAttribute("vecUOR");
                Iterator iter = aplic.iterator();
                while(iter.hasNext()){
                    String unidadOrgCod = (String)iter.next();
                    String unidadOrg = (String)iter.next();
                    String rex_xeral = (String)iter.next();
            %>
                <tr>
                    <td class="<%=("0".equals(rex_xeral)?"listaUorRegistro":"listaUorRegistroXeral")%>">
                        <a class="enlace" href="javascript:retornoSeleccion('<%=unidadOrgCod%>','<%=unidadOrg%>')">
                            <%=unidadOrg%>
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
 </body>
</html:html>
