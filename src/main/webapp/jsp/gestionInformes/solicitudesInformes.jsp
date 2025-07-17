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
<style type="text/css">
  .dojoDialog.dialogCriterios {
    background : #ddd;
    border : 1px solid #888;
    padding:2px;
    height:500px;
    width:700px;
    text-align:right;
    top:0px;
    left:0px;
}


</style>

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript">
    dojo.require("dojo.widget.SortableTable");
    dojo.require("dojo.widget.FilteringTable");
    dojo.require("dojo.widget.Dialog");

    var listaSolicitudes = new Array();
    var conSol=0;
    <logic:iterate id="solicitud" name="SolicitudesInformesForm" property="listaSolicitudes">
        listaSolicitudes[conSol] = '<bean:write name="solicitud" property="codigo"/>'
        conSol++;
    </logic:iterate>

    var listaAmbitos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="SolicitudesInformesForm" property="listaAmbitos">
      listaAmbitos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />'];
      cont++;
    </logic:iterate>

    var listaProcedimientos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="SolicitudesInformesForm" property="listaProcedimientos">
      listaProcedimientos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />'];
      cont++;
    </logic:iterate>


    var dlg;
    var dlg2;
    var dlgDetalles;
    function init(e) {
        dlg = dojo.widget.byId("DialogContent");
        var btn = document.getElementById("cancelarDialog");
        dlg.setCloseControl(btn);
        dlgDetalles = dojo.widget.byId("DialogDetalles");
        var btnDetalles = document.getElementById("cancelarDialogDetalles");
        dlgDetalles.setCloseControl(btnDetalles);
        dlg2 = dojo.widget.byId("DialogContent3");
        var btn2 = document.getElementById("cancelarDialog3");
        dlg2.setCloseControl(btn2);
    }
    dojo.addOnLoad(init);

    setTimeout("actualizarSolicitudes()", 15000);
</script>

<script type="text/javascript">

    function pulsarBuscar() {
        applyName('parsedFromHtml');
    }

    function ambFilter(amb) {
        return (amb == dojo.byId('descAmbitoBusqueda').value);
    }

    function procFilter(proc) {
        return (proc == dojo.byId('codProcedimientoBusqueda').value);
    }

    function nameFilter(name) {
        return (name.toLowerCase().indexOf(dojo.byId('nombreBusqueda').value.toLowerCase(),0)!=-1);
    }

    function estadoFilter(estado) {
        var res = "1==0";
        
        var espera='<%=descriptor.getDescripcion("etiqespera")%>';
        var ejecucion='<%=descriptor.getDescripcion("etiqejecucion")%>';
        var finalizado='<%=descriptor.getDescripcion("etiqfinalizado")%>';
        var generado='<%=descriptor.getDescripcion("etiqgenerado")%>';
        var sindatos= '<%=descriptor.getDescripcion("etiqsindatos")%>';
        if (dojo.byId('estado1Busqueda').checked) {res += " || estado == '"+espera+"'"};
        if (dojo.byId('estado2Busqueda').checked) {res += " || estado == '"+ejecucion+"'"};
        if (dojo.byId('estado3Busqueda').checked) {res += " || estado == '"+finalizado+"'"};
        if (dojo.byId('estado4Busqueda').checked) {res += " || estado == '"+generado+"'"};
        if (dojo.byId('estado5Busqueda').checked) {res += " || estado == '"+sindatos+"'"};
        return (eval(res));
    }

    function applyName(key) {
        pulsarLimpiar();
        if (dojo.byId('codAmbitoBusqueda').value != "")
            dojo.widget.byId(key).setFilter("Ambito", ambFilter);
        if (dojo.byId('codProcedimientoBusqueda').value != "")
            dojo.widget.byId(key).setFilter("Proced", procFilter);
        if (dojo.byId('nombreBusqueda').value != "")
            dojo.widget.byId(key).setFilter("Name", nameFilter);
        if ((!dojo.byId('estado1Busqueda').checked) || (!dojo.byId('estado2Busqueda').checked) || (!dojo.byId('estado3Busqueda').checked) ||
            (!dojo.byId('estado4Busqueda').checked) || (!dojo.byId('estado5Busqueda').checked))
            dojo.widget.byId(key).setFilter("Estado", estadoFilter);
    }

    function clearFilters(key) {
        dojo.widget.byId(key).clearFilters();
    }

    function pulsarLimpiar(){
        var key='parsedFromHtml';
        dojo.widget.byId(key).clearFilters(key);
    }

    function getValue(w) {
        var data=w.store.get();
        for(var i=0; i<data.length; i++){
            if(data[i].isSelected){
                return listaSolicitudes[i];
            }
        }
    }

    function getEstado(w) {
        var data=w.store.get();
        for(var i=0; i<data.length; i++){
            if(data[i].isSelected){
                return data[i].src.Estado;
            }
        }
    }

    function pulsarEliminar(){
        var w=dojo.widget.byId("parsedFromHtml");
        if(w){
            var s=getValue(w);
            if (s != null) {
                if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimSol")%>') ==1) {
                    eliminarSolicitud(s);
                }
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function eliminarSolicitud(key) {
        var params = new Array();
        params['codInforme'] = key;
        params['origen'] = '<bean:write name="SolicitudesInformesForm" property="origen"/>';
        params['opcion'] = 'eliminarSolicitud';
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: DOMAIN_NAME + "<c:url value='/gestionInformes/SolicitudesInformes.do'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "populateDivActualizar");
    }

    function actualizarSolicitudes() {
        var params = new Array();
        params['opcion'] = 'cargarSolicitudes';
        params['origen'] = '<bean:write name="SolicitudesInformesForm" property="origen"/>';
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: DOMAIN_NAME + "<c:url value='/gestionInformes/SolicitudesInformes.do'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "populateDivActualizar");
        setTimeout("actualizarSolicitudes()", 15000);
    }

    function populateDivActualizar(type, data, evt) {
        if (data) {
            actualizaListaSolicitudes(type,data,evt);
            clearData("parsedFromHtml");
            var theJSONData=[];

            var cods=data.listaInformesCod;
            var names=data.listaInformesNombre;
            var ambitos=data.listaInformesAmbito;
            var procs=data.listaInformesProc;
            var estado=data.listaInformesEstado;
            var fechas=data.listaInformesFecha;
            for(var j=0; j<cods.length; j++){
                var o ={
                    Id:cods[j],
                    Name:names[j],
                    Fecha:fechas[j],
                    Ambito:ambitos[j],
                    Proced:procs[j],
                    Estado:estado[j]
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

    function actualizaDetalles(key) {
        var params = new Array();
        params['codInforme'] = key;
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: DOMAIN_NAME + "<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=verDetallesSolicitud'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "populateDiv");
    }

    // Function call to populate the "bookInfo" div element that is
    // defined at the end of this file.
    function populateDiv(type, data, evt) {
        if (data) {
            document.getElementById('valor1').value=(data.titulo!=undefined?data.titulo:'');
            document.getElementById('valor2').value=(data.procedimiento!=undefined?data.procedimiento:'');
            document.getElementById('valor3').value=(data.formato!=undefined?data.formato:'');
            document.getElementById('valor4').value=(data.fechaSolicitud!=undefined?data.fechaSolicitud:'');
            document.getElementById('valor5').value=(data.fechaGeneracion!=undefined?data.fechaGeneracion:'');
            document.getElementById('valor6').value=(data.tiempo!=undefined?data.tiempo:'');
            document.getElementById('valor7').value=(data.usuario!=undefined?data.usuario:'');
            document.getElementById('valor8').value=(data.listaCriterios!=undefined?data.listaCriterios:'');
            document.getElementById('valor9').value=(data.origen!=undefined?data.origen:'');
        }
    }

    function pulsarDetalles(){
        var w=dojo.widget.byId("parsedFromHtml");
        if(w){
            var s=getValue(w);
            if (s != null) {
                actualizaDetalles(s);
                dlgDetalles.show();
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function pulsarVer(){
        var w=dojo.widget.byId("parsedFromHtml");
        if(w){
            var s=getValue(w);
            if (s != null) {
                e = getEstado(w);
                if (e=='Finalizado') {
                window.open("<html:rewrite page='/VerInforme'/>?codigoSolicitud=" + s,"Informe");
            } else {
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoVisualInforme")%>');
                }
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function pulsarSalir(){
        alert(dojo.widget.byId('procedimiento').combo.value);
    }

    function actualizarProcedimiento(){
        var w=dojo.widget.byId("parsedFromHtml2");
        if(w){
            var s=w.getValue();
            if(s.length>0) document.getElementById("codProcedimientoBusqueda").value=s;
            for (i=0;i<listaProcedimientos.length;i++) {
                if (s==listaProcedimientos[i][0]) {
                    document.getElementById("descProcedimientoBusqueda").value=listaProcedimientos[i][1];
                    break;
                }
            }
            document.getElementById("cancelarDialog").click();
        }
    }

    function cambiarProcedimiento(){
        var w=dojo.widget.byId("parsedFromHtml2");
        var s = document.getElementById("codProcedimientoBusqueda").value;
        var encontrado=false;
        for (i=0;i<listaProcedimientos.length;i++) {
            if (s==listaProcedimientos[i][0]) {
                document.getElementById("descProcedimientoBusqueda").value=listaProcedimientos[i][1];
                encontrado = true;
            }
        }
        if (!encontrado) {
            document.getElementById("codProcedimientoBusqueda").value="";
            document.getElementById("descProcedimientoBusqueda").value="";
        }
    }
    function actualizarAmbito(){
        var comboProcedBusqueda = [document.getElementById("codProcedimientoBusqueda"),document.getElementById("descProcedimientoBusqueda"),
                        document.getElementById("botonProcedimientoBusqueda")];
        var w=dojo.widget.byId("parsedFromHtml3");
        if(w){
            var s=w.getValue();
            if(s.length>0) document.getElementById("codAmbitoBusqueda").value=s;
            for (i=0;i<listaAmbitos.length;i++) {
                if (s==listaAmbitos[i][0]) {
                    document.getElementById("descAmbitoBusqueda").value=listaAmbitos[i][1];
                    if (s=="1") { // CASO ÄMBITO EXPEDIENTE
                        document.getElementById("botonProcedimientoBusqueda").style.color="#0B3090 !important";
                        habilitarGeneral(comboProcedBusqueda);
                    } else {
                        document.getElementById("botonProcedimientoBusqueda").style.color="#f6f6f6 !important";
                        deshabilitarGeneral(comboProcedBusqueda);
                        document.getElementById("codProcedimientoBusqueda").value="";
                        document.getElementById("descProcedimientoBusqueda").value="";
                    }
                    break;
                }
            }
            document.getElementById("cancelarDialog3").click();
        }
    }

    function cambiarAmbito(){
        var w=dojo.widget.byId("parsedFromHtml3");
        var s = document.getElementById("codAmbitoBusqueda").value;
        var encontrado=false;
        var comboProcedBusqueda = [document.getElementById("codProcedimientoBusqueda"),document.getElementById("descProcedimientoBusqueda"),
                        document.getElementById("botonProcedimientoBusqueda")];
        for (i=0;i<listaAmbitos.length;i++) {
            if (s==listaAmbitos[i][0]) {
                document.getElementById("descAmbitoBusqueda").value=listaAmbitos[i][1];
                encontrado = true;
                if (s=="1") { // CASO ÄMBITO EXPEDIENTE
                    document.getElementById("botonProcedimientoBusqueda").style.color="#0B3090 !important";
                    habilitarGeneral(comboProcedBusqueda);
                } else {
                    document.getElementById("botonProcedimientoBusqueda").style.color="#f6f6f6 !important";
                    deshabilitarGeneral(comboProcedBusqueda);
                    document.getElementById("codProcedimientoBusqueda").value="";
                    document.getElementById("descProcedimientoBusqueda").value="";
                }
            }
        }
        if (!encontrado) {
            document.getElementById("codAmbitoBusqueda").value="";
            document.getElementById("descAmbitoBusqueda").value="";
        }
    }

    function ventanaProcBusqueda() {
        if (!document.getElementById("codProcedimientoBusqueda").disabled)
            dlg.show();
    }

    function actualizaListaSolicitudes(type,data,evt){
        if (data) {
            var cods=data.listaInformesCod;
            listaSolicitudes = cods;
        }
    }


</script>

<form name="form" action="/gestionInformes/SolicitudesInformes.do" target="_self">
    <input type="hidden" name="codInforme" value="">
    <input type="hidden" name="codProcedimiento" value="">
    <input type="hidden" name="nombre" value="">
    <!-- TABLA DE AMBITOS -->
    <div class="dialogCombo" dojoType="dialog" id="DialogContent3" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
        <table class="xTabla" dojoType="SortableTable" widgetId="parsedFromHtml3" headClass="fixedHeader" tbodyClass="scrollContent"
               enableMultipleSelect="false" enableAlternateRows="true" rowAlternateClass="alternateRow" 
               onSelect="actualizarAmbito();">
             <thead>
                 <tr>
                     <th field="Id" dataType="String"><%=descriptor.getDescripcion("gbInformeCodigo")%></th>
                    <th field="Name" dataType="String"><%=descriptor.getDescripcion("gbInformeProc")%></th>
                 </tr>
             </thead>
            <logic:iterate id="elemento" name="SolicitudesInformesForm" property="listaAmbitos">
                 <tr>
                    <td><bean:write name="elemento" property="codigo" /></td>
                    <td><bean:write name="elemento" property="descripcion" /></td>
                 </tr>
            </logic:iterate>
        </table>
        <div style="margin-left:90px">
            <input type= "button" id="cancelarDialog3" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialog3">
        </div>
    </div>
    <!-- TABLA DE AMBITOS -->
    <!-- TABLA DE PROCEDIMIENTOS -->
    <div class="dialogCombo" dojoType="dialog" id="DialogContent" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
        <table class="xTabla" dojoType="SortableTable" widgetId="parsedFromHtml2" headClass="fixedHeader" tbodyClass="scrollContent"
               enableMultipleSelect="false" enableAlternateRows="true" rowAlternateClass="alternateRow" style="text-align:left"
               onSelect=actualizarProcedimiento();>
             <thead>
                 <tr>
                     <th field="Id" dataType="String"><%=descriptor.getDescripcion("gbInformeCodigo")%></th>
                    <th field="Name" dataType="String"><%=descriptor.getDescripcion("gbInformeProc")%></th>
                 </tr>
             </thead>
             <logic:iterate id="elemento" name="SolicitudesInformesForm" property="listaProcedimientos">
                 <tr>
                    <td><bean:write name="elemento" property="codigo"/></td>
                    <td><bean:write name="elemento" property="descripcion"/></td>
                 </tr>
            </logic:iterate>
        </table>
        <div style="margin-left:90px">
            <input type= "button" id="cancelarDialog" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialog">
        </div>
    </div>
    <!-- TABLA DE PROCEDIMIENTOS -->


    <input type="hidden" name="opcion" id="opcion" value="">
    <div class="filtroGestionInformes">
        <div class="filtroGestionInformesCampos">
            <div>
                <span class="etiq" style="float:left"><%=descriptor.getDescripcion("gbInformeAmbito")%></span>
                <input type="text" name="codAmbitoBusqueda" id="codAmbitoBusqueda" size="6" class="inputTexto" value=""
                       onkeyup="return xAMayusculas(this);" onChange="cambiarAmbito();">
                <input type="text" name="descAmbitoBusqueda"  id="descAmbitoBusqueda" size="45" class="inputTexto" readonly="true" value="">
                <a href="javascript:dlg2.show()"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAmbitoBusqueda"
                     name="botonAmbitoBusqueda" style="cursor:hand;"></span></a>
            </div>
            <div>
                <span class="etiq" style="float:left"><%=descriptor.getDescripcion("gbInformeProc")%></span>
                <input type="text" name="codProcedimientoBusqueda" id="codProcedimientoBusqueda" size="6" class="inputTexto" value=""
                       onKeyUp="xAMayusculas(this);" onChange="cambiarProcedimiento();">
                <input type="text" name="descProcedimientoBusqueda"  id="descProcedimientoBusqueda" size="45" class="inputTexto" readonly="true" value="">
                <a href="javascript:ventanaProcBusqueda()"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProcedimientoBusqueda"
                     name="botonProcedimientoBusqueda" style="cursor:hand;"></span></a>
            </div>
            <div>
                <span class="etiq" style="float:left"><%=descriptor.getDescripcion("gbInformeTitulo")%></span>
                <input type="text" id="nombreBusqueda" size="56" class="inputTexto" value="">
            </div>
            <div>
                <span class="etiq" style="float:left"><%=descriptor.getDescripcion("gbInformeEstado")%></span>
                <input type="checkbox" id="estado1Busqueda" class="inputTexto" value="1" checked>
                <span class="etiqueta"><%=descriptor.getDescripcion("gbInformeEstado1")%></span>
                <input type="checkbox" id="estado2Busqueda" class="inputTexto" value="1" checked>
                <span class="etiqueta"><%=descriptor.getDescripcion("gbInformeEstado2")%></span>
                <input type="checkbox" id="estado3Busqueda" class="inputTexto" value="1" checked>
                <span class="etiqueta"><%=descriptor.getDescripcion("gbInformeEstado3")%></span>
                <input type="checkbox" id="estado4Busqueda" class="inputTexto" value="1" checked>
                <span class="etiqueta"><%=descriptor.getDescripcion("gbInformeEstado4")%></span>
                <input type="checkbox" id="estado5Busqueda" class="inputTexto" value="1" checked>
                <span class="etiqueta"><%=descriptor.getDescripcion("gbInformeEstado5")%></span>
                
            <input type= "button" class="botonGeneral" value="Buscar"
                   name="botonBuscar" onClick="pulsarBuscar();" accesskey="B" style="position:relative;margin-left:70px">
        </div>
    </div>
        
    </div>
    <div class="tablaGestionInformes" style="margin-top: 15px">
        <table class="xTabla" dojoType="filteringTable" id="parsedFromHtml" multiple="false" alternateRows="true" maxSortable="2">
            <thead>
                <tr>
                    <th field="Name" dataType="String" width="44%"><%=descriptor.getDescripcion("gbInformeTitulo")%></th>
                    <th field="Fecha" dataType="String" width="14%" align="center"><%=descriptor.getDescripcion("gbInformeFecha")%></th>
                    <th field="Ambito" dataType="String" width="14%" align="center"><%=descriptor.getDescripcion("gbInformeAmbito")%></th>
                    <th field="Proced" dataType="String" width="14%" align="center"><%=descriptor.getDescripcion("gbInformeProc")%></th>
                    <th field="Estado" dataType="String" width="14%" align="center"><%=descriptor.getDescripcion("gbInformeEstado")%></th>
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="solicitud" name="SolicitudesInformesForm" property="listaSolicitudes">
                    <tr value="<bean:write name="solicitud" property="codigo"/>">
                        <td><bean:write name="solicitud" property="titulo"/></td>
                        <td align="center"><bean:write name="solicitud" property="fecha"/></td>
                        <td align="center"><bean:write name="solicitud" property="origen"/></td>
                        <td align="center"><bean:write name="solicitud" property="codProcedimiento"/></td>
                        <td align="center"><bean:write name="solicitud" property="estado" /></td>
                    </tr>
                </logic:iterate>
            </tbody>
        </table>
    </div>
</form>


<!-- TABLA DE CRITERIOS -->
<div class="dialogCriterios" dojoType="dialog" id="DialogDetalles" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
    <form name="criterios">
    <div class="tableContainerComboCriterios" style="text-align:left;">
        <table border="0" align="center">
        <tr>
            <td colspan="2">
            <span class="etiqBoldMaxi"><%=descriptor.getDescripcion("gbInformeDetSol")%></span>
            </td>            
        </tr>   
        <tr>
            <td style="width:120px;">
                <span class="etiq" style="width:175px;"><%=descriptor.getDescripcion("gbInformeTitulo")%></span>
            </td>
            <td>
                <input type="text" name="valor1" id="valor1" class="inputTexto" style="width:175px;float:left;" disabled>
            </td>
        </tr>
        <tr>
            <td style="width:120px;">
            <span class="etiq" style="width:175px"><%=descriptor.getDescripcion("gbInformeAmbito")%></span>
            </td>
            <td>
            <input type="text" name="valor9" id="valor9" class="inputTexto" style="width:175px" disabled>
            </td>
        </tr>
        <tr>
            <td style="width:120px;">
            <span class="etiq" style="width:175px"><%=descriptor.getDescripcion("gbInformeProc")%></span>
            </td>
            <td>
            <input type="text" name="valor2" id="valor2" class="inputTexto" style="width:175px" disabled>
            </td>
        </tr>   
        
        <tr>
            <td style="width:120px;">
            <span class="etiq" style="width:175px"><%=descriptor.getDescripcion("gbInformeFormato")%></span>
            </td>
            <td>
            <input type="text" name="valor3" id="valor3" class="inputTexto" style="width:175px" disabled>
            </td>
        </tr>   
        
        <tr>
            <td style="width:120px;">
            <span class="etiq" style="width:175px"><%=descriptor.getDescripcion("gbInformeFecSol")%></span>
            </td>
            <td>
            <input type="text" name="valor4" id="valor4" class="inputTexto" style="width:175px" disabled>
            </td>
        </tr>           
        <tr>
            <td style="width:120px;">
            <span class="etiq" style="width:175px"><%=descriptor.getDescripcion("gbInformeFecGen")%></span>
            </td>
            <td>
            <input type="text" name="valor5" id="valor5" class="inputTexto" style="width:175px" disabled>
            </td>
        </tr>
        
          <tr>
            <td style="width:120px;">
                <span class="etiq" style="width:175px"><%=descriptor.getDescripcion("gbInformeTiempo")%></span>
            </td>
            <td>
                <input type="text" name="valor6" id="valor6" class="inputTexto" style="width:175px" disabled>
            </td>
        </tr>
        <tr>
            <td style="width:120px;">
            <span class="etiq" style="width:175px"><%=descriptor.getDescripcion("gbInformeTiempo")%></span>
            </td>
            <td>
            <input type="text" name="valor6" id="valor6" class="inputTexto" style="width:175px" disabled>
            </td>
        </tr>
        <tr>
            <td style="width:120px;">
            <span class="etiq" style="width:175px"><%=descriptor.getDescripcion("gbInformeSolic")%></span>
            </td>
            <td>
            <input type="text" name="valor7" id="valor7" class="inputTexto" style="width:175px" disabled>
            </td>
        </tr>
        <tr>
            <td style="width:120px;">
            <span class="etiq" style="width:175px"><%=descriptor.getDescripcion("gbInformeCrit")%></span>
            </td>
            <td>
            <textarea class="textareaTexto" name="valor8" id="valor8" rows="5" cols="80" disabled></textarea>
            </td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="2" align="right">
                <input type= "button" id="cancelarDialogDetalles" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialogDetalles">
            </td>
        </tr>
        </table>               
    </div>
    </form>
</div>
<!-- TABLA DE CRITERIOS -->
<script type="text/javascript">
    var comboProcedBusqueda = [document.getElementById("codProcedimientoBusqueda"),document.getElementById("descProcedimientoBusqueda"),
                    document.getElementById("botonProcedimientoBusqueda")];
    document.getElementById("botonProcedimientoBusqueda").style.color="#f6f6f6 !important";
    deshabilitarGeneral(comboProcedBusqueda);
    
    $(document).ready(function() {
        $(".xTabla").not(".dialogCombo .xTabla").DataTable( {
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
