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
    dojo.require("dojo.widget.Dialog");

    var listaInformes = new Array();
    var conInf=0;
    <logic:iterate id="sw" name="MantenimientoIntegracionSWForm" property="listaSW">
        listaInformes[conInf] = '<bean:write name="sw" property="codigoSW"/>'
        conInf++;
    </logic:iterate>


   

    var dlg;
    function init(e) {
        dlg = dojo.widget.byId("DialogContent");
        var btn = document.getElementById("cancelarDialog");
        dlg.setCloseControl(btn);
    }
    dojo.addOnLoad(init);
</script>

<script type="text/javascript">

    function pulsarBuscar() {
        applyName('parsedFromHtml');
    }

    function setVal(val) {
        dojo.byId('codProcedimientoBusqueda').value = val;
    }

    function procFilter(proc) {
        return (proc == dojo.byId('codProcedimientoBusqueda').value);
    }

    function nameFilter(name) {
        return (name.toLowerCase().indexOf(dojo.byId('nombreBusqueda').value.toLowerCase(),0)!=-1);
    }

    function pubFilter(pub) {
        if ((!dojo.byId('pubBusqueda').checked) && (!dojo.byId('noPubBusqueda').checked))
            return (pub.charAt(0) == '');
        else if (!dojo.byId('pubBusqueda').checked)
            return (pub.charAt(0) == 'N');
        else if (!dojo.byId('noPubBusqueda').checked)
            return (pub.charAt(0) == 'S');
    }

    function applyName(key) {
        pulsarLimpiar();
        if (dojo.byId('codProcedimientoBusqueda').value != "") dojo.widget.byId(key).setFilter("Proced", procFilter);
        if (dojo.byId('nombreBusqueda').value != "") dojo.widget.byId(key).setFilter("Name", nameFilter);
        if ((!dojo.byId('pubBusqueda').checked) ||(!dojo.byId('noPubBusqueda').checked)) dojo.widget.byId(key).setFilter("Publicado", pubFilter);
    }

    function clearFilters(key) {
        dojo.widget.byId(key).clearFilters();
    }

    function pulsarLimpiar(){
        var key='parsedFromHtml';
        dojo.widget.byId(key).clearFilters(key);
    }

    function pulsarAlta() {
        var source = "<c:url value='/integracionsw/MantenimientoIntegracionSW.do?opcion=iniciarSW'/>";
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
	'width=537,height=295,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined){
                            if (datosConsulta[0]=="D") {
                                document.forms[1].codPlantilla.value=datosConsulta[1];
                                document.forms[1].opcion.value = "modificarSW";
                                document.forms[1].action = "<c:url value='/integracionsw/MantenimientoIntegracionSW.do'/>";
                                document.forms[1].submit();
                            } else {
                                document.forms[1].opcion.value = "inicio";
                                document.forms[1].action = "<c:url value='integracionsw/MantenimientoIntegracionSW.do'/>";
                                document.forms[1].submit();
                            }
                        }
                    });
    }

    function getValue(w) {
        var data=w.store.get();
        for(var i=0; i<data.length; i++){
            if(data[i].isSelected){
                return listaInformes[i];
            }
        }
    }

    function pulsarModificar() {
        var w=dojo.widget.byId("parsedFromHtml");
        if(w){
            var s=getValue(w);
            if(s != null) {
                document.forms[1].codPlantilla.value=s;
                document.forms[1].opcion.value = "modificarInforme";
                document.forms[1].action = "<c:url value='/gestionInformes/FichaInforme.do'/>";
                document.forms[1].submit();
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function pulsarSalir(){
        alert(dojo.widget.byId('procedimiento').combo.value);
    }

    function pulsarPublicar(){
        var w=dojo.widget.byId("parsedFromHtml");
        if (w) {
            var s=getValue(w);
            if (s != null) {
                if(jsp_alerta("C",'<%=descriptor.getDescripcion("desPublicar")%>') ==1) {
                    actualizarInformes(s,"1");
                }
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function pulsarDespublicar(){
        var w=dojo.widget.byId("parsedFromHtml");
        if (w) {
            var s=getValue(w);
            if (s != null) {
                if(jsp_alerta("C",'<%=descriptor.getDescripcion("desDespublicar")%>') ==1) {
                    actualizarInformes(s,"0");
                }
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function actualizarInformes(key, opcion) {
        var params = new Array();
        params['codInforme'] = key;
        params['publicar'] = opcion;
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: "<c:url value='/gestionInformes/GestionInformes.do?opcion=publicarInforme'/>",
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
            clearData("parsedFromHtml");
            var theJSONData=[];
            var cods=data.listaInformesCod;
            var procs=data.listaInformesProc;
            var names=data.listaInformesNombre;
            var pubs=data.listaInformesPub;
            for(var j=0; j<cods.length; j++){
                var o ={
                    Id:cods[j],
                    Proced:procs[j],
                    Name:names[j],
                    Publicado:pubs[j]
                };
                theJSONData.push(o);
            }
            var w=dojo.widget.byId("parsedFromHtml");
            w.store.setData(theJSONData);
        }
    }

    function clearData(key){
        dojo.widget.byId(key).store.clearData();
    }

    function actualizarProcedimiento(){
        var w=dojo.widget.byId("parsedFromHtml2");
        if(w){
            var s=w.getValue();
            if(s.length>0) document.forms[1].codProcedimientoBusqueda.value=s;
            for (i=0;i<listaProcedimientos.length;i++) {
                if (s==listaProcedimientos[i][0]) {
                    document.forms[1].descProcedimientoBusqueda.value=listaProcedimientos[i][1];
                    break;
                }
            }
            document.getElementById("cancelarDialog").click();
        }
    }

    function cambiarProcedimiento(){
        var w=dojo.widget.byId("parsedFromHtml2");
        var s = document.forms[1].codProcedimientoBusqueda.value;
        var encontrado=false;
        for (i=0;i<listaProcedimientos.length;i++) {
            if (s==listaProcedimientos[i][0]) {
                document.forms[1].descProcedimientoBusqueda.value=listaProcedimientos[i][1];
                encontrado = true;
            }
        }
        if (!encontrado) {
            document.forms[1].codProcedimientoBusqueda.value="";
            document.forms[1].descProcedimientoBusqueda.value="";
        }
    }
</script>

<form name="form" action="/integracionsw/IntegracioSW.do" target="_self">
    <input type="hidden" name="opcion" id="opcion" value="">

    <div class="tablaGestionInformes">
        <table class="xTabla" dojoType="filteringTable" id="parsedFromHtml" multiple="false" alternateRows="true" maxSortable="2">
            <thead>
                <tr>
                    <th field="Name" dataType="String" width="80%">T&iacutetulo</th>
                    <th field="Publicado" dataType="String" width="20%" align="center">Publicado</th>				                        
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="sw" name="MantenimientoIntegracionSWForm" property="listaSW">
                    <tr value="<bean:write name="sw" property="codigoSW"/>">
                        <td><bean:write name="sw" property="tituloSW"/></td>
                        <td><bean:write name="sw" property="estaPublicado" /></td>
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
