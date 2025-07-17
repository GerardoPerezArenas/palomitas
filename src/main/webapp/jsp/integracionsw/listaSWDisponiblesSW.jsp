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

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript">
   
    dojo.require("dojo.widget.FilteringTable");
   
	<c:if test="${not empty requestScope.error}">
            jsp_alerta('A', '<c:out value="${requestScope.error}"/>');
    </c:if>
    var listaServiciosWeb = new Array();
    var conSW=0;
    <logic:iterate id="sw" name="ListadoSWForm" property="listaSW">
        listaServiciosWeb[conSW] = '<bean:write name="sw" property="codigoSW"/>'
        conSW++;
    </logic:iterate>


function pulsarAlta() {
    var source = "<c:url value='/integracionsw/ListadoSW.do?opcion=altaSW'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
                    'width=537,height=295,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined){
                            document.forms[1].opcion.value = "cargarListadoSW";
                            document.forms[1].action = "<c:url value='/integracionsw/ListadoSW.do'/>";
                                    document.forms[1].submit();
                        }
                    });
}
</script>
<form name="form" action="/integracionsw/ListadoSW.do" target="_self">
   <!--   <input type="hidden" name="codPlantilla" value="">
    
    <input type="hidden" name="nombre" value="">
    
-->


    <input type="hidden" name="opcion" id="opcion" value="">

    <div class="tablaIntegracionSW">
        <table class="xTabla" dojoType="filteringTable" id="parsedFromHtml" multiple="false" alternateRows="true" maxSortable="2"
               cellpadding="0" cellspacing="0" border="0">
            <thead>
                <tr>
                    <th field="Name" dataType="String" width="80%">T&iacutetulo</th>
                    <th field="Publicado" dataType="String" width="20%" align="center">Publicado</th>				                        
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="sw" name="ListadoSWForm" property="listaSW">
                    <tr value="<bean:write name="sw" property="codigoSW"/>">
                        <td><bean:write name="sw" property="tituloSW"/></td>                                                
                        <td><c:if test="${sw.estaPublicado}" >
                            SI
                            </c:if>
                            <c:if test="${!sw.estaPublicado}" >
                            NO
                            </c:if>
                        </td>

                    </tr>
                </logic:iterate>
            </tbody>
        </table>
    </div>
</form>
<script>
    $(document).ready(function() {
        $(".xTabla").DataTable( {
            "sort" : false,
            "info" : false,
            "paginate" : false,
            "autoWidth": false,
            "language": {
                "search": "<%=descriptor.getDescripcion("buscar")%>",
                "zeroRecords": "<%=descriptor.getDescripcion("msgNoResultBusq")%>",
                "info": "<%=descriptor.getDescripcion("mosPagDePags")%>",
                "infoEmpty": "<%=descriptor.getDescripcion("noRegDisp")%>",
                "infoFiltered": "<%=descriptor.getDescripcion("filtrDeTotal")%>",
            }
        } );
    });
</script>