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
    
    var listaInformes = new Array();
    var conInf=0; 

    <logic:iterate id="informe" name="GestionInformesForm" property="listaInformes">
        listaInformes[conInf] = '<bean:write name="informe" property="codigo"/>'
        conInf++;
    </logic:iterate>

    var listaAmbitos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="GestionInformesForm" property="listaAmbitos">
      listaAmbitos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />'];
      cont++;
    </logic:iterate>

    var listaProcedimientos = new Array();
    var cont=0;
    <logic:iterate id="elemento" name="GestionInformesForm" property="listaProcedimientos">
      listaProcedimientos[cont] = ['<bean:write name="elemento" property="codigo" />', '<bean:write name="elemento" property="descripcion" />'];
      cont++;
    </logic:iterate>

    var dlg, dlg2;
    function init(e) {
        dlg = dojo.widget.byId("DialogContent");
        dlg2 = dojo.widget.byId("DialogContent3");
        var btn = document.getElementById("cancelarDialog");
        var btn2 = document.getElementById("cancelarDialog3");
        dlg.setCloseControl(btn);
        dlg2.setCloseControl(btn2);
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

    function ambFilter(amb) {
        return (amb == dojo.byId('descAmbitoBusqueda').value);
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
        if (dojo.byId('codAmbitoBusqueda').value != "") dojo.widget.byId(key).setFilter("Ambito", ambFilter);
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
        var source = DOMAIN_NAME+"<c:url value='/gestionInformes/FichaInforme.do?opcion=iniciarInforme'/>";
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana',
	'width=700,height=420,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined){
                            if (datosConsulta[0]=="D") {
                                document.forms[1].codPlantilla.value=datosConsulta[1];
                                document.forms[1].opcion.value = "modificarInforme";
                                document.forms[1].action =DOMAIN_NAME+ "<c:url value='/gestionInformes/FichaInforme.do'/>";
                                document.forms[1].submit();
                            } else {
                                document.forms[1].opcion.value = "inicio";
                                document.forms[1].action =DOMAIN_NAME+ "<c:url value='/gestionInformes/GestionInformes.do'/>";
                                document.forms[1].submit();
                            }
                        }
                });
    }

    function getCodPlantilla(w) {
        var data=w.store.get();
        for(var i=0; i<data.length; i++){
            if(data[i].isSelected){
                return listaInformes[i];
            }
        }
    }

    function getNombre(w) {
        var data=w.store.get();
        for(var i=0; i<data.length; i++){
            if(data[i].isSelected){
                return data[i].src.Name;
            }
        }
    }

    function getProcedimiento(w) {
        var data=w.store.get();
        for(var i=0; i<data.length; i++){
            if(data[i].isSelected){
                return data[i].src.Proced;
            }
        }
    }

    function getAmbito(w) {
        var data=w.store.get();
        for(var i=0; i<data.length; i++){
            if(data[i].isSelected){
                return data[i].src.Ambito;
            }
        }
    }

    function estaPublicado(w) {
        var data=w.store.get();
        for(var i=0; i<data.length; i++){
            if(data[i].isSelected){
                return data[i].src.Publicado=='SI';
            }
        }
    }

    function pulsarModificar() {
        var w=dojo.widget.byId("parsedFromHtml");
        if(w){
            var s=getCodPlantilla(w);
            if(s != null) {
            	if (!estaPublicado(w)){
                   document.forms[1].codPlantilla.value=s;
                   document.forms[1].opcion.value = "modificarInforme";
                   document.forms[1].action = DOMAIN_NAME+"<c:url value='/gestionInformes/FichaInforme.do'/>";                                      
                   document.forms[1].submit();
                }else {
                   jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoModifPubl")%>');
                }
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function pulsarImportar() {
        document.forms[1].opcion.value = "importarInforme";
        document.forms[1].action =DOMAIN_NAME+ "<c:url value='/gestionInformes/GestionInformes.do'/>";
        document.forms[1].submit();
    }

    function pulsarVerificar() {
        var w=dojo.widget.byId("parsedFromHtml");
        if(w){
            var s=getCodPlantilla(w);
            var n=getNombre(w);
            var p=getProcedimiento(w);
            var a=getAmbito(w);
            if(s != null) {
                verificacionInforme(s, n, p, 'verificarInforme',a);
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function verificacionInforme(codPlantilla, nombre, codProcedimiento, opcion, descAmbito) {
        var params = new Array();
        params['codPlantilla'] = codPlantilla;
        params['nombre'] = nombre;
        params['codProcedimiento'] = codProcedimiento;
        params['opcion'] = opcion;
        params['descAmbito'] = descAmbito;
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: DOMAIN_NAME+"<c:url value='/gestionInformes/FichaInforme.do'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "populateDivVerif");
    }

    function populateDivVerif(type, data, evt) {
        if (data.verificacion == 'correcta') {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjVerifCorrecta")%>');
        } else if (data.verificacion == 'camposNoDisponibles') {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjVerifNoCamposDisp")%>');
        } else if (data.verificacion == 'yaExiste') {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjPlantYaExiste")%>');
        }
    }

    function pulsarDescargar() {
        var w=dojo.widget.byId("parsedFromHtml");
        if(w){
            var s=getCodPlantilla(w);
            var n=getNombre(w);
            var p=getProcedimiento(w);
            var a=getAmbito(w);
            if(s != null) {
                descargarInforme(s, n, p, 'descargarInforme',a);
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function descargarInforme(codPlantilla, nombre, codProcedimiento, opcion, descAmbito) {
        var params = new Array();
        params['codPlantilla'] = codPlantilla;
        params['nombre'] = nombre;
        params['codProcedimiento'] = codProcedimiento;
        params['opcion'] = opcion;
        params['descAmbito'] = descAmbito;
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: DOMAIN_NAME+"<c:url value='/gestionInformes/FichaInforme.do'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "populateDivDescargar");
    }

    function populateDivDescargar(type, data, evt) {
        if (data.verificacion == 'importacionNoRealizada') {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjImpNoRealiz")%>');
        } else if (data.verificacion == 'importacionRealizada') {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjImpRealiz")%>');
                document.forms[1].opcion.value = "inicio";
                document.forms[1].action = DOMAIN_NAME+"<c:url value='/gestionInformes/GestionInformes.do'/>";
                document.forms[1].submit();
        } else if (data.verificacion == 'camposNoDisponibles') {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoImpNoCampDisp")%>');
        } else if (data.verificacion == 'yaExiste') {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoImpYaExiste")%>');
        }else if (data.verificacion == 'permisosNoDisponibles') {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoImpNoPermisos")%>');
        }
    }

    function pulsarCancelar() {
        document.forms[1].opcion.value = "inicio";
        document.forms[1].action = DOMAIN_NAME+"<c:url value='/gestionInformes/GestionInformes.do'/>";
        document.forms[1].submit();
    }

    function pulsarSalir(){
        alert(dojo.widget.byId('procedimiento').combo.value);
    }
    
    function pulsarPublicar(){
        var w=dojo.widget.byId("parsedFromHtml");
        if (w) {
            var s=getCodPlantilla(w);
            if (s != null) {
                if (!estaPublicado(w)) {
                    if(jsp_alerta("C",'<%=descriptor.getDescripcion("desPublicar")%>') ==1) {
                        actualizarInformes(s,"1");
                    }
                } else {
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjPlantYaPubl")%>');
                }

          } else {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
          }
        }
    }

    function pulsarDespublicar(){
        var w=dojo.widget.byId("parsedFromHtml");
        if (w) {
            var s=getCodPlantilla(w);
            if (s != null) {
                if (estaPublicado(w)) {
                if(jsp_alerta("C",'<%=descriptor.getDescripcion("desDespublicar")%>') ==1) {
                    actualizarInformes(s,"0");
                }
            } else {
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjPlantYaDesp")%>');
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
            url: DOMAIN_NAME+"<c:url value='/gestionInformes/GestionInformes.do?opcion=publicarInforme'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "populateDivActInformes");
    }

    function populateDivActInformes(type, data, evt) {
        var publicado = false;
        if (data) {
            actualizaListaInformes(type,data,evt);
            clearData("parsedFromHtml");
            var theJSONData=[];
            var cods=data.listaInformesCod;
            var origs=data.listaInformesOrig;
            var procs=data.listaInformesProc;
            var names=data.listaInformesNombre;
            var pubs=data.listaInformesPub;
            for(var j=0; j<cods.length; j++){
                var o ={
                    Id:cods[j],
                    Ambito:origs[j],
                    Proced:procs[j],
                    Name:names[j],
                    Publicado:pubs[j]
                };
                theJSONData.push(o);
                if (data.codPlantilla == cods[j]) {
                    publicado = (pubs[j] == 'SI');
                }
            }
            var w=dojo.widget.byId("parsedFromHtml");
            w.store.setData(theJSONData);
            if (data.publicar == '1' && !publicado) {
                if (data.hayPermiso == 'SI') {
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoPublPlant")%>');
                } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoPermPub")%>');
                
            }
        }else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoPermPub")%>');
    }

    function clearData(key){
        dojo.widget.byId(key).store.clearData();
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

    function eliminarInforme(key) {
        var params = new Array();
        params['codInforme'] = key;
        params['opcion'] = 'eliminarInforme';
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: DOMAIN_NAME+"<c:url value='/gestionInformes/GestionInformes.do'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
        dojo.event.connect(req, "load", this, "populateDivActInformes");
    }

    function pulsarEliminar(){
        var w=dojo.widget.byId("parsedFromHtml");
        if (w) {
            var s=getCodPlantilla(w);
            if (s != null) {
                if (!estaPublicado(w)) {
                    if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimSol")%>') ==1) {
                        eliminarInforme(s);
                    }
                }else jsp_alerta('A', '<%=descriptor.getDescripcion("noElimPub")%>');
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }

        var w=dojo.widget.byId("parsedFromHtml");
    }

    function actualizaListaInformes(type,data,evt){
        if (data) {
            var cods=data.listaInformesCod;
            listaInformes = cods;
        }
    }
</script>

<form name="form" action="/gestionInformes/GestionInformes.do" target="_self">
    <input type="hidden" name="codPlantilla" value="">
    <input type="hidden" name="codProcedimiento" value="">
    <input type="hidden" name="nombre" value="">
    <input type="hidden" name="descAmbito" value="">
    <!-- TABLA DE AMBITOS -->
    <div class="dialogCombo" dojoType="dialog" id="DialogContent3" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
        <table class="xTabla" dojoType="SortableTable" widgetId="parsedFromHtml3" headClass="fixedHeader" tbodyClass="scrollContent"
               enableMultipleSelect="false" enableAlternateRows="true" rowAlternateClass="alternateRow" 
               onSelect=actualizarAmbito();>
             <thead>
                 <tr>
                     <th field="Id" dataType="String"><%=descriptor.getDescripcion("gbInformeCodigo")%></th>
                    <th field="Name" dataType="String"><%=descriptor.getDescripcion("gbInformeProc")%></th>
                 </tr>
             </thead>
            <logic:iterate id="elemento" name="GestionInformesForm" property="listaAmbitos">
                 <tr>
                    <td><bean:write name="elemento" property="codigo" /></td>
                    <td><bean:write name="elemento" property="descripcion" /></td>
                 </tr>
            </logic:iterate>
        </table>
        <div>
            <input type= "button" id="cancelarDialog3" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialog3">
        </div>
    </div>
    <!-- TABLA DE AMBITOS -->
    <!-- TABLA DE PROCEDIMIENTOS -->
    <div class="dialogCombo" dojoType="dialog" id="DialogContent" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
        <table class="xTabla" dojoType="SortableTable" widgetId="parsedFromHtml2" headClass="fixedHeader" tbodyClass="scrollContent"
               enableMultipleSelect="false" enableAlternateRows="true" rowAlternateClass="alternateRow" 
               onSelect=actualizarProcedimiento();>
             <thead>
                 <tr>
                     <th field="Id" dataType="String"><%=descriptor.getDescripcion("gbInformeCodigo")%></th>
                    <th field="Name" dataType="String"><%=descriptor.getDescripcion("gbInformeProc")%></th>
                 </tr>
             </thead>
             <logic:iterate id="elemento" name="GestionInformesForm" property="listaProcedimientos">
                 <tr>
                    <td><bean:write name="elemento" property="codigo"/></td>
                    <td><bean:write name="elemento" property="descripcion"/></td>
                 </tr>
            </logic:iterate>
        </table>
        <div>
            <input type= "button" id="cancelarDialog" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialog">
        </div>
    </div>
    <!-- TABLA DE PROCEDIMIENTOS -->


    <input type="hidden" name="opcion" id="opcion" value="">
    <div class="filtroGestionInformes" style="height:105px;">
        <table border="0" style="width:90%;height:50%;" align="left">
            <tr>
                <td style="width:10%;">
                <span class="etiq"><%=descriptor.getDescripcion("gbInformeAmbito")%></span>
                </td>
                <td align="left">
                <input type="text" name="codAmbitoBusqueda" id="codAmbitoBusqueda" size="6" class="inputTexto" value=""
                       onkeyup="return xAMayusculas(this);" onChange="cambiarAmbito();">
                <input type="text" name="descAmbitoBusqueda"  id="descAmbitoBusqueda" size="45" class="inputTexto" readonly="true" value="">
                <a href="javascript:dlg2.show()"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAmbitoBusqueda"
                     name="botonAmbitoBusqueda" style="cursor:hand;"></span></a>
                </td>
            </tr>
            <tr>
                <td style="width:10%;">
                <span class="etiq"><%=descriptor.getDescripcion("gbInformeProc")%></span>
                </td>
                <td align="left">
                <input type="text" name="codProcedimientoBusqueda" id="codProcedimientoBusqueda" size="6" class="inputTexto" value=""
                       onkeyup="return xAMayusculas(this);" onChange="cambiarProcedimiento();">
                <input type="text" name="descProcedimientoBusqueda"  id="descProcedimientoBusqueda" size="45" class="inputTexto" readonly="true" value="">
                <a href="javascript:ventanaProcBusqueda()"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProcedimientoBusqueda"
                     name="botonProcedimientoBusqueda" style="cursor:hand;"></span></a>
                </td>
            </tr>
            <tr>
                <td style="width:10%;">
                <span class="etiq"><%=descriptor.getDescripcion("gbInformeNombre")%></span>
                </td>
                <td align="left">
                <input type="text" id="nombreBusqueda" size="56" class="inputTexto" value="">
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <table border="0" align="left" style="width:100%;">
                     <tr>
                         <td style="width:450px;">
                <input type="checkbox" id="pubBusqueda" class="inputTexto" value="1" checked>
                            <span class="etiq"><%=descriptor.getDescripcion("gbInformePub")%></span>
                <input type="checkbox" id="noPubBusqueda" class="inputTexto" value="1" checked>
                            <span class="etiq"><%=descriptor.getDescripcion("gbInformeNoPub")%></span>
                         </td>                     
                         <td align="right">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbBuscar")%>"
                   name="botonBuscar" onClick="pulsarBuscar();" accesskey="B">
                         </td>
                     </tr>
                    </table>
                </td>
            </tr>            
        </table>
        </div>

    <div class="tablaGestionInformes">
        <table class="xTabla" dojoType="filteringTable" id="parsedFromHtml" multiple="false" alternateRows="true" maxSortable="2">
            <thead>
                <tr>
                    <th field="Ambito" dataType="String" width="20%" align="center"><%=descriptor.getDescripcion("gbInformeAmbito")%></th>
                    <th field="Proced" dataType="String" width="10%" align="center"><%=descriptor.getDescripcion("gbInformeProc")%></th>
                    <th field="Name" dataType="String" width="50%"><%=descriptor.getDescripcion("gbInformeNombre")%></th>
                    <th field="Publicado" dataType="String" width="20%" align="center"><%=descriptor.getDescripcion("gbInformePub")%></th>
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="informe" name="GestionInformesForm" property="listaInformes">
                    <tr value="<bean:write name="informe" property="codigo"/>">
                        <td align="center"><bean:write name="informe" property="origen"/></td>
                        <td align="center"><bean:write name="informe" property="codProcedimiento"/></td>
                        <td><bean:write name="informe" property="titulo"/></td>
                        <td align="center"><bean:write name="informe" property="publicado" /></td>
                    </tr>
                </logic:iterate>
            </tbody>
        </table>
    </div>
</form>
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
