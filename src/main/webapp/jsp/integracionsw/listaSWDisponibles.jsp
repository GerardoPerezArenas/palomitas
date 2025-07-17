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
    dojo.require("dojo.io.*");

    <c:if test="${not empty requestScope.error}">
            jsp_alerta('A', '<c:out value="${requestScope.error}"/>');
    </c:if>
    var listaServiciosWeb = new Array();
    var conSW=0;
    <logic:iterate id="sw" name="ListadoSWForm" property="listaSW">
        var esPublicado = '<bean:write name="sw" property="estaPublicado"/>';
        if (esPublicado == 'true') esPublicado = 'SI';
        else esPublicado = 'NO';
        listaServiciosWeb[conSW] = ['<bean:write name="sw" property="codigoSW"/>',
                '<bean:write name="sw" property="tituloSW"/>', esPublicado];
        conSW++;
    </logic:iterate>


    function pulsarAlta() {
        var source = "<c:url value='/integracionsw/AltaSW.do?opcion=inicioAltaSW'/>";
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source=' + source, 'ventana', 
	'width=537,height=295,status='+ '<%=statusBar%>',function(datosConsulta){
                    if (datosConsulta != undefined) {
                        document.forms[1].opcion.value = datosConsulta[0];
                        document.forms[1].codigoSW.value = datosConsulta[1];
                        document.forms[1].action = "<c:url value='/integracionsw/ListadoOperacionesSW.do'/>";
                        document.forms[1].submit();
                    } else {
                        var params = new Array();
                        params['opcion'] = 'cargarListadoSW';
                        params['recargaPagina'] = 'false';
                        var bindArgs = {
                            url: "<c:url value='/integracionsw/ListadoSW.do'/>",
                            error: function(type, data, evt) {
                            },
                            mimetype: "text/json",
                            content: params
                        };
                        var req = dojo.io.bind(bindArgs);

                        // The "populateDiv" gets called as an event handler
                        dojo.event.connect(req, "load", this, "recargarSW");
                    }
                });
    }

    function getValue(w) {
        var data = w.store.get();
        var codigoSW = -1;
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                codigoSW = listaServiciosWeb[i][0];
            }
        }
        return codigoSW;
    }

    function getInfo(w) {
        var data = w.store.get();
        var infoSW = new Array();
        for (var i = 0; i < data.length; i++) {
            if (data[i].isSelected) {
                infoSW = listaServiciosWeb[i];
            }
        }
        return infoSW;
    }

    function pulsarModificar() {
        var w = dojo.widget.byId("tablaSW");
        if (w) {
            var s = getValue(w);
            if (s != -1) {
                document.forms[1].codigoSW.value = s
                document.forms[1].opcion.value = "iniciaCargaOperaciones";
                document.forms[1].action = "<c:url value='/integracionsw/ListadoOperacionesSW.do'/>";
                document.forms[1].submit();
            } else {
                jsp_alerta('A', "<%=descriptor.getDescripcion("msj_noFilaSelecc")%>");
            }
        }
    }

    function pulsarPublicar() {
        var w = dojo.widget.byId("tablaSW");
        if (w) {
            var s = getValue(w);
            if (s != -1) {
                actualizarListaServicios(s, "true");
            } else {
                jsp_alerta('A', "<%=descriptor.getDescripcion("msj_noFilaSelecc")%>");
            }
        }
    }

    function pulsarDespublicar() {
        var w = dojo.widget.byId("tablaSW");
        if (w) {
            var s = getValue(w);
            if (s != -1) {
                actualizarListaServicios(s, "false");
            } else {
                jsp_alerta('A', "<%=descriptor.getDescripcion("msj_noFilaSelecc")%>");
            }
        }
    }

    function actualizarListaServicios(key, opcion) {
        var params = new Array();
        params['codigoSW'] = key;
        params['publicar'] = opcion;
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: "<c:url value='/integracionsw/ListadoSW.do?opcion=publicarSW'/>",
            error: function(type, data, evt) {
            },
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "recargarSW");
    }

    function recargarSW(type, data, evt) {
        if (data) {
            clearData("tablaSW");
            var theJSONData = [];
            var error = data.jsonError;
            if (error.length == 1) jsp_alerta('A', error[0]);
            var cods = data.listaCodigosSW;
            var names = data.listaTitulosSW;
            var pubs = data.listaServiciosPub;
            listaServiciosWeb = new Array();
            for (var j = 0; j < cods.length; j++) {
                var strPubs = '';
                if (pubs[j]) strPubs = 'SI';
                else strPubs = 'NO';
                var o = {
                    Id:cods[j],
                    Name:names[j],
                    Publicado:strPubs
                };
                theJSONData.push(o);
                listaServiciosWeb[j] = [cods[j], names[j], strPubs];
            }
            var w = dojo.widget.byId("tablaSW");
            w.store.setData(theJSONData);
        }
    }

    function clearData(key) {
        dojo.widget.byId(key).store.clearData();
    }

    function pulsarEliminar() {
        var w = dojo.widget.byId("tablaSW");
        if (w) {
            var s = new Array();
            s = getInfo(w);
            if (s[0] != null) {
                if (s[2] == 'SI') {
                    jsp_alerta('A', "<%=descriptor.getDescripcion("msj_noPuedeEliminar")%>");
                    return null;
                }
                if (jsp_alerta('C', "<%=descriptor.getDescripcion("mjs_deseaEliminarSW")%> " + s[1]) + "?") {
                    var params = new Array();
                    params['codigoSW'] = s[0];
                    params['opcion'] = 'eliminarSW';
                    var bindArgs = {
                        url: "<c:url value='/integracionsw/ListadoSW.do'/>",
                        error: function(type, data, evt) {
                        },
                        mimetype: "text/json",
                        content: params
                    };
                    var req = dojo.io.bind(bindArgs);
                    dojo.event.connect(req, "load", this, "recargarSW");
                }
            } else {
                jsp_alerta('A', "<%=descriptor.getDescripcion("msj_noFilaSelecc")%>");
            }
        }
    }


</script>

<form name="form" action="/integracionsw/ListadoSW.do" target="_self">


    <input type="hidden" name="codigoSW" id="codigoSW" value="">
    <input type="hidden" name="opcion" id="opcion" value="">

    <div class="tablaIntegracionSW">
        <table class="xTabla" dojoType="filteringTable" id="tablaSW" multiple="false" alternateRows="true" maxSortable="2"
               cellpadding="0" cellspacing="0" border="0">
            <thead>
                <tr>
                    <th field="Name" dataType="String" width="80%"><%=descriptor.getDescripcion("iswEtiqDesc")%></th>
                    <th field="Publicado" dataType="String" width="20%" align="center"><%=descriptor.getDescripcion("iswEtiqPub")%></th>
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="sw" name="ListadoSWForm" property="listaSW">
                    <tr value="<bean:write name="sw" property="codigoSW"/>">
                        <td><bean:write name="sw" property="tituloSW"/></td>
                        <td align="center"><c:if test="${sw.estaPublicado}" >
                            <%=descriptor.getDescripcion("iswEtiqSi")%>
                            </c:if>
                            <c:if test="${!sw.estaPublicado}" >
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
