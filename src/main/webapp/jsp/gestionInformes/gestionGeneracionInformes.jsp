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


</style>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript">

    dojo.require("dojo.widget.*");
    dojo.require("dojo.event.*");

	dojo.require("dojo.widget.SortableTable");
    dojo.require("dojo.widget.FilteringTable");
    dojo.require("dojo.widget.Dialog");




    var listaInformes = new Array();
    var listaProcedimientosInformes = new Array();
    var conInf=0;
    <logic:iterate id="informe" name="SolicitudesInformesForm" property="listaInformes">
        listaInformes[conInf] = '<bean:write name="informe" property="codigo"/>';
        listaProcedimientosInformes[conInf] = '<bean:write name="informe" property="codProcedimiento"/>';
        conInf++;
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




function actualizaDetalles(key) { 
//alert('origen'+document.forms[0].origen.value);

        var params = new Array();
        params['codInforme'] = key;
       	
       	params['origen']=document.getElementById("origen").value;
        params['listaCamposCriterio']=document.getElementById("listaCamposCriterio").value;
        params['listaValorOperCriterio']=document.getElementById("listaValorOperCriterio").value;
        params['listaValor1Criterio']=document.getElementById("listaValor1Criterio").value;
        params['listaValor2Criterio']=document.getElementById("listaValor2Criterio").value;
        params['tipoFichero']=document.getElementById("tipoFichero").value;
        params['modoVisualizar']=document.getElementById("modoVisualizar").value;
        params['descripcion']=document.getElementById("descripcion").value;
        params['dir']=document.getElementById("dir").value;
        params['codProcedimiento']=document.getElementById("codProcedimiento").value;
        params['codPlantilla']=document.getElementById("codPlantilla").value;
        
        // Perform remote operation using JSON as data format
        // that will be returned from the server

        var bindArgs = {
            url: DOMAIN_NAME+"<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=generar'/>",
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
        params['opcion'] = 'cargarSolicitudesPantalla';
        params['origen'] = '<bean:write name="SolicitudesInformesForm" property="origen"/>';
        // Perform remote operation using JSON as data format
        // that will be returned from the server
        var bindArgs = {
            url: DOMAIN_NAME+"<c:url value='/gestionInformes/SolicitudesInformes.do'/>",
            error: function(type, data, evt){},
            mimetype: "text/json",
            content: params
        };
        var req = dojo.io.bind(bindArgs);

        // The "populateDiv" gets called as an event handler
       dojo.event.connect(req, "load", this, "populateDivActualizar");
       
    }

    function populateDivActualizar(type, data, evt) {

        if (data) {
            var cods=data.listaInformesCod;
            var names=data.listaInformesNombre;
            var ambitos=data.listaInformesAmbito;
            var procs=data.listaInformesProc;
            var estado=data.listaInformesEstado;
              
            var fechas=data.listaInformesFecha;
            
          
		if(estado=="Finalizado"){             
			 window.open("<html:rewrite page='/VerInforme'/>?codigoSolicitud=" + cods + "&modo=directo",
                         "Informe");
			  var params = new Array();
        	  params['codInforme'] = cods;
       		  params['origen']=document.getElementById("origen").value;

              var bindArgs = {

	            url: DOMAIN_NAME+"<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=cargarSolicitudes'/>",
	            error: function(type, data, evt){},
	            mimetype: "text/json",
	            content: params
       		 };
       		 var req = dojo.io.bind(bindArgs);
			  document.getElementById("cancelarDialogDetalles").click();
                           setTimeout("cerrar()", 1000); 
		}else if((estado=="En espera") || (estado=="En ejecución")){			  
              
			  document.getElementById('valor4').innerHTML=(data.listaInformesEstado!=undefined?data.listaInformesEstado:''); 
			   setTimeout("actualizarSolicitudes()", 10000); 
		}else{	
		  document.getElementById('valor4').innerHTML=(data.listaInformesEstado!=undefined?data.listaInformesEstado:''); 
        	  var params = new Array();
        	  params['codInforme'] = cods;
       		  params['origen']=document.getElementById("origen").value;

       		 var bindArgs = {

	            url: DOMAIN_NAME+"<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=eliminarSolicitud'/>",
	            error: function(type, data, evt){},
	            mimetype: "text/json",
	            content: params
       		 };
       		 var req = dojo.io.bind(bindArgs);
        	 setTimeout("cerrar()", 1000); 	
        	  	
        	}
        }
    }



    function clearData(key){
        dojo.widget.byId(key).store.clearData();
    }
	
 function cerrar(){


        document.getElementById("cancelarDialogDetalles").click();
    }

    var dlgDetalles;
    var dlgProcedimientos;
    function init(e) {
        dlgProcedimientos = dojo.widget.byId("DialogContent");
        var btnProcedimientos = document.getElementById("cancelarDialog");
        dlgProcedimientos.setCloseControl(btnProcedimientos);
        dlg2 = dojo.widget.byId("DialogContent3");
        var btn2 = document.getElementById("cancelarDialog3");
        dlg2.setCloseControl(btn2);
        
        
        dlgDetalles = dojo.widget.byId("DialogDetalles");
        var btnDetalles = document.getElementById("cancelarDialogDetalles");
        dlgDetalles.setCloseControl(btnDetalles);
        
        
    }
    dojo.addOnLoad(init);
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

    function applyName(key) {
        pulsarLimpiar();
        if (dojo.byId('codAmbitoBusqueda').value != "") dojo.widget.byId(key).setFilter("Ambito", ambFilter);
        if (dojo.byId('codProcedimientoBusqueda').value != "") dojo.widget.byId(key).setFilter("Proced", procFilter);
        if (dojo.byId('nombreBusqueda').value != "") dojo.widget.byId(key).setFilter("Name", nameFilter);
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
                return listaInformes[i];
            }
        }
    }

    function getValueProc(w) {
        var data=w.store.get();
        for(var i=0; i<data.length; i++){
            if(data[i].isSelected){
                return listaProcedimientosInformes[i];
            }
        }
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

    function pulsarNuevo() { 

        nuevoW=dojo.widget.byId("parsedFromHtml");
        if(nuevoW){
            nuevoS=getValue(nuevoW);
            nuevoC=getValueProc(nuevoW);
            if (nuevoS != null) {
                var source = DOMAIN_NAME+"<c:url value='/gestionInformes/FichaInforme.do?opcion=cargarListaCriterios&codInforme='/>"+nuevoS;                
               abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/gestionInformes/mainVentana.jsp?source='+source,'ventana',
                    'width=800,height=800,status='+ '<%=statusBar%>',function(datosConsulta){
                        if(datosConsulta!=undefined) {
                           
                     
                            document.getElementById("origen").value='<bean:write name="SolicitudesInformesForm" property="origen"/>';
                            //document.forms[0].origen.value = '<bean:write name="SolicitudesInformesForm" property="origen"/>';                            alert("aa");
                            //document.forms[0].listaCamposCriterio.value = datosConsulta[0];  
                            
                         
                            document.getElementById("listaCamposCriterio").value=datosConsulta[0];  
                            document.getElementById("listaValorOperCriterio").value=datosConsulta[1]; 
                            document.getElementById("listaValor1Criterio").value=datosConsulta[2];  
                            document.getElementById("listaValor2Criterio").value=datosConsulta[3];  
                            document.getElementById("tipoFichero").value=datosConsulta[4];  
                            document.getElementById("modoVisualizar").value=datosConsulta[7];  
                             document.getElementById("descripcion").value=datosConsulta[5];  
                            

                          
                           var dir = window.location.href;
                           
                            if (dir.indexOf("generacionProvisional")!=-1) {
                               
                                 document.getElementById("dir").value="0";  
                            } else {
                                 document.getElementById("dir").value="1";  
                            }
                            //guardo los datos en variables para el dojo
                            
                            document.getElementById("descripcion").value=datosConsulta[5];
                         
                            if(datosConsulta[7]==2){
                                var w=dojo.widget.byId("parsedFromHtml");
                                if(w){
                                        var s=getValue(w);
                                        var c=getValueProc(w);
                                        if (s != null) {
                                                
                                                 document.getElementById("codProcedimiento").value=c;
                                                 document.getElementById("codPlantilla").value=s;
                                            
                                                actualizaDetalles(s);
                                                dlgDetalles.show();
                                        } else {
                                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                                }
                            }
                            }else{
                           
                             document.getElementById("codProcedimiento").value=nuevoC;
                             document.getElementById("codPlantilla").value=nuevoS;
                          
                            document.getElementById("opcion").value = "generar";
                           
                            
                            var form = document.getElementById("formPrincipal");	
                            form.action = DOMAIN_NAME+"<c:url value='/gestionInformes/SolicitudesInformes.do'/>";	
                            form.submit();
                           
                           }
                        }
                    });
            } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            }
        }
    }

    function ventanaProcBusqueda() {
        if (!document.getElementById("codProcedimientoBusqueda").disabled)
            dlgProcedimientos.show();
    }


</script>

<form name="form" id="formPrincipal" action="/gestionInformes/SolicitudesInformes.do" target="_self">
    <input type="hidden" name="origen"  id="origen"  value="">
    <input type="hidden" name="dir"  id="dir" value="">
    <input type="hidden" name="codPlantilla" id="codPlantilla"  value="">
    <input type="hidden" name="codProcedimiento" id="codProcedimiento"  value="">
    <input type="hidden" name="nombre" id="nombre" value="">
    <input type="hidden" name="listaCamposCriterio" id="listaCamposCriterio"  value="">
    <input type="hidden" name="listaValorOperCriterio" id="listaValorOperCriterio"  value="">
    <input type="hidden" name="listaValor1Criterio"   id="listaValor1Criterio" value="">
    <input type="hidden" name="listaValor2Criterio" id="listaValor2Criterio" value="">
    <input type="hidden" name="tipoFichero"  id="tipoFichero" value="">
     <input type="hidden" name="modoVisualizar"  id="modoVisualizar"  value="">
    <input type="hidden" name="descripcion"  id="descripcion"  value="">

    <!-- TABLA DE AMBITOS -->
    <div class="dialogCombo" dojoType="dialog" id="DialogContent3" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
        <table class="xTabla" dojoType="SortableTable" widgetId="parsedFromHtml3" headClass="fixedHeader" tbodyClass="scrollContent"
               enableMultipleSelect="false" enableAlternateRows="true" rowAlternateClass="alternateRow" cellpadding="0" cellspacing="0" border="0"
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
        <div>
            <input type= "button" id="cancelarDialog3" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialog3">
        </div>
    </div>
    <!-- TABLA DE AMBITOS -->
     <!-- TABLA DE PROCEDIMIENTOS -->
    <div class="dialogCombo" dojoType="dialog" id="DialogContent" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
        <table class="xTabla" dojoType="SortableTable" widgetId="parsedFromHtml2" headClass="fixedHeader" tbodyClass="scrollContent"
               enableMultipleSelect="false" enableAlternateRows="true" rowAlternateClass="alternateRow" 
               onSelect="actualizarProcedimiento();">
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
        <div>
            <input type= "button" id="cancelarDialog" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialog">
        </div>
    </div>
    <!-- TABLA DE PROCEDIMIENTOS -->

    <input type="hidden" name="opcion" id="opcion" value="">

    <table>
    <tr>
        <td width="10%"><span class="etiq"><%=descriptor.getDescripcion("gbInformeAmbito")%></span></td>
        <td>
            <input type="text" name="codAmbitoBusqueda" id="codAmbitoBusqueda" size="6" class="inputTexto" value=""
                       onkeyup="return xAMayusculas(this);" onChange="cambiarAmbito();">
            <input type="text" name="descAmbitoBusqueda"  id="descAmbitoBusqueda" size="45" class="inputTexto" readonly="true" value="">
                <a href="javascript:dlg2.show()"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAmbitoBusqueda"
                     name="botonAmbitoBusqueda" style="cursor:hand;"></span></a>
        </td>
    </tr>
    <tr>
        <td><span class="etiq"><%=descriptor.getDescripcion("gbInformeProc")%></span></td>
        <td>             
                <input type="text" name="codProcedimientoBusqueda" id="codProcedimientoBusqueda" size="6" class="inputTexto" value=""
                       onkeyup="return xAMayusculas(this);" onChange="cambiarProcedimiento();">
                <input type="text" name="descProcedimientoBusqueda"  id="descProcedimientoBusqueda" size="45" class="inputTexto" readonly="true" value="">
                <a href="javascript:ventanaProcBusqueda()"><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProcedimientoBusqueda"
                     name="botonProcedimientoBusqueda" style="cursor:hand;"></span></a>
        </td>
    </tr>
    <tr>
        <td class="etiqueta">
            <%=descriptor.getDescripcion("gbInformeNombre")%>
        </td>
        <td>
            <input type="text" id="nombreBusqueda" size="56" class="inputTexto" value="">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbBuscar")%>"
                   name="botonBuscar" onClick="pulsarBuscar();" accesskey="B" style="margin-left:50px">
        </td>
    </tr>
    </table>

    <br/>
    <div align="center">
    <div class="tablaGestionInformes">
        <table class="xTabla" dojoType="filteringTable" id="parsedFromHtml" multiple="false" alternateRows="true" maxSortable="2">
            <thead>
                <tr>
                    <th field="Ambito" dataType="String" width="20%" align="center"><%=descriptor.getDescripcion("gbInformeAmbito")%></th>
                    <th field="Proced" dataType="String" width="20%"><%=descriptor.getDescripcion("gbInformeProc")%></th>
                    <th field="Name" dataType="String" width="60%"><%=descriptor.getDescripcion("gbInformeNombre")%></th>
                </tr>
            </thead>
            <tbody>
                <logic:iterate id="informe" name="SolicitudesInformesForm" property="listaInformes">
                    <tr value="<bean:write name="informe" property="codigo"/>">
                        <td align="center"><bean:write name="informe" property="origen"/></td>
                        <td align="center"><bean:write name="informe" property="codProcedimiento"/></td>
                        <td><bean:write name="informe" property="titulo"/></td>
                    </tr>
                </logic:iterate>
            </tbody>
        </table>
    </div>
    </div>
</form>


<!-- TABLA DE CRITERIOS -->
<div class="dialogCriteriosPantalla" dojoType="dialog" id="DialogDetalles" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
    <form name="criterios">
        <table class="contenedorHidepage">
		<tr>
                <td>
                    <div class="textoHide">
                        <span  id="valor4" style="font-family: Arial, Verdana, Helvetica, sans-serif; font-style:normal; font-variant: normal; font-size: 12px; font-weight: bold; color: #000000;"></span>
                    </div>
                    <div class="imagenHide">
                        <span class="fa fa-spinner fa-spin" aria-hidden="true"></span>
                    </div>
                </td>
		</tr>
		<tr>
                <td height="10%">
			<div style="visibility:hidden">
        				<input type= "button" id="cancelarDialogDetalles" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cancelarDialogDetalles">
                    </div>
                </td>
		</tr>
	</table>
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
