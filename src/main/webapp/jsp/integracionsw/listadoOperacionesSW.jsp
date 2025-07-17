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
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor" property="idi_cod" value="<%= idioma%>"/>
<jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript">

    dojo.require("dojo.widget.FilteringTable");
    dojo.require("dojo.io.*");

    <c:if test="${not empty requestScope.error}">
    jsp_alerta('A', '<c:out value="${requestScope.error}"/>');
    </c:if>

    var listaOperaciones = new Array();
    var conOps = 0;
    <logic:iterate id="op" name="ListadoOperacionesSWForm" property="listaOperaciones">
    listaOperaciones[conOps] = '<bean:write name="op" property="codigoDefinicionOp"/>';
    conOps++;
    </logic:iterate>


    function pulsarAlta() {

        var ruta  =DOMAIN_NAME+ "<c:url value='/integracionsw/AltaOperacionSW.do?opcion=inicioAltaOperacionSW&codigoSW="+
            document.forms[1].codigoSW.value+"'/>";
        
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/integracionsw/mainVentana.jsp?source='+ruta, 'ventana',
	'width=540,height=300,scrollbars=no,titlebar=no,status='+ '<%=statusBar%>',function(datosConsulta){
                        if (datosConsulta == undefined) {
                            document.forms[1].opcion.value = "iniciaCargaOperaciones";
                            document.forms[1].action = DOMAIN_NAME+ "<c:url value='/integracionsw/ListadoOperacionesSW.do'/>";
                            document.forms[1].submit();
                        } else {
                            document.forms[1].opcion.value = datosConsulta[0];
                            document.forms[1].codigoOpDef.value = datosConsulta[1];
                            document.forms[1].action = DOMAIN_NAME+ "<c:url value='/integracionsw/MantenimientoOperacionSW.do'/>";
                            document.forms[1].submit();
                        }
                });
    }

    function pulsarVolver() {
        document.forms[1].opcion.value = 'cargarListadoSW';
        document.forms[1].action = DOMAIN_NAME+ "<c:url value='/integracionsw/ListadoSW.do'/>";
        document.forms[1].submit();
    }

    function getValue(w) {
        var data = w.store.get();
        var codigoSW = -1;
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                codigoSW = listaOperaciones[i];
            }
        }
        return codigoSW;
    }

    function pulsarModificar() {
        var m = dojo.widget.byId("tablaOps");
        if (m) {
            var s = getValue(m);
            if (s != -1) {
                document.forms[1].codigoOpDef.value = s;
                document.forms[1].opcion.value = "cargaMantenimientoOp";
                document.forms[1].action = DOMAIN_NAME+ "<c:url value='/integracionsw/MantenimientoOperacionSW.do'/>";
                document.forms[1].submit();
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msj_noFilaSelecc")%>');
            }
        }
    }

    function pulsarPublicar(){
        var w=dojo.widget.byId("tablaOps");
        if (w) {
            var s=getValue(w);
            if (s != -1) {
                actualizarOperaciones(s, "true");
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msj_noFilaSelecc")%>');
            }
        }
    }

    function pulsarDespublicar(){
        var w=dojo.widget.byId("tablaOps");
        if (w) {
            var s=getValue(w);
            if (s != -1) {
                actualizarOperaciones(s, "false");
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msj_noFilaSelecc")%>');
            }
        }
    }

    function actualizarOperaciones(key, opcion) {
        var params = new Array();
        params['codOperacion'] = key;
        params['publicar'] = opcion;
        params['codServicioWeb'] = <bean:write name="ListadoOperacionesSWForm" property="codigoSW"/>
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: DOMAIN_NAME+ "<c:url value='/integracionsw/ListadoOperacionesSW.do?opcion=publicarInforme'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "populateDiv");
    }

    function populateDiv(type, data, evt) {
        if (data) {
            var error = data.msjError;
            if (error.length == 1) jsp_alerta('A', error[0]);
            clearData("tablaOps");
            var theJSONData=[];
            var cods=data.listaOperacionesCod;
            var names=data.listaOperacionesNombre;
            var pubs=data.listaOperacionesPub;
            for(var j=0; j<cods.length; j++){
                var o ={
                    Id:cods[j],
                    Name:names[j],
                    Publicado:pubs[j]
                };
                theJSONData.push(o);
            }
            var w=dojo.widget.byId("tablaOps");
            w.store.setData(theJSONData);
        }
    }

    function clearData(key){
        dojo.widget.byId(key).store.clearData();
    }

    function pulsarTest() {
        var w=dojo.widget.byId("tablaOps");
        if (w) {
            var s=getValue(w);
            if (s != -1) {
                var ruta = '<%=request.getContextPath()%>/integracionsw/TestOperacionSW.do?opcion=cargaTest&codigoOpDef=' + s;
                abrirXanelaAuxiliar(ruta, 'ventana', 
                    'width=800,height=700,status='+ '<%=statusBar%>',function(){});
            } else {
                jsp_alerta('A', "NO HA SELECCIONADO NINGUNA FILA");
            }
        }
    }

</script>

<form name="form" action="/integracionsw/ListadoOperacionesSW.do" target="_self">

    <input type="hidden" name="opcion" id="opcion" value="">
    <input type="hidden" name="codigoOpDef" id="codigoOpDef" value="">
    <html:hidden property="codigoSW" name="ListadoOperacionesSWForm"/>

    <div class="filtroGestionInformes" style="height:60px;">
        <div class="filtroIntegracionSWCampos">
            <table border="0" cellspacing="0" cellpadding="2">
            <tr>
                <td><span class="etiq"><b><%=descriptor.getDescripcion("iswEtiqTit")%></b></span></td>
                <td>
                <html:text property="tituloSW" styleClass="inputTextoObligatorio" readonly="true" size="80"
                           name="ListadoOperacionesSWForm"/>
                </td>
            </tr>
            <tr>
                <td>
                    <span class="etiq"><b><%=descriptor.getDescripcion("iswEtiqWSDL")%></b></span>
                </td>
                <td>
                <html:text property="wsdlSW" styleClass="inputTextoObligatorio" readonly="true" size="80"
                           name="ListadoOperacionesSWForm"/>
                </td>
            </tr>
            </table>
        </div>
    </div>
    <div class="tablaIntegracionSW">
        <table class="xTabla" dojoType="filteringTable" id="tablaOps" multiple="false"
               alternateRows="true" maxSortable="2"
               cellpadding="0" cellspacing="0" border="0">
            <thead>
                <tr>
                    <th field="Name" dataType="String" width="80%"><%=descriptor.getDescripcion("iswEtiqOps")%></th>
                    <th field="Publicado" dataType="String" width="20%" align="center"><%=descriptor.getDescripcion("iswEtiqPub")%></th>
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="op" name="ListadoOperacionesSWForm" property="listaOperaciones">
                    <tr value="<bean:write name="op" property="codigoDefinicionOp"/>">
                        <td>
                            <bean:write name="op" property="nombreDefinicionOp"/>
                        </td>
                        <!-- PREGUNTAR QUE PROPIEDADES TIENE OPERACION -->
                        <td align="center">
                            <c:if test="${op.publicada}">
                                <%=descriptor.getDescripcion("iswEtiqSi")%>
                            </c:if>
                            <c:if test="${!op.publicada}">
                                <%=descriptor.getDescripcion("iswEtiqNo")%>
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
