<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>


<%@page contentType="text/html; charset=iso-8859-1" language="java"%>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%
    UsuarioValueObject usuarioVO = null;
    int idioma = 1;
    int apl=1;
    Vector vectorAplicaciones;
    Vector vectorProcedimientos;
    Vector vectorTramites;
    Vector vectorPlantillaActiva;
    DocumentosAplicacionForm daForm=null;
    String css = null;
    int municipio = -1;
    if (session!=null) {
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma =  usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
        css = usuarioVO.getCss();	
        municipio = usuarioVO.getOrgCod();
        daForm =(DocumentosAplicacionForm)session.getAttribute("EditorDocumentosAplicacionForm");
    }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
      String editor = m_Config.getString("editorPlantillas");

    Config m_Conf = ConfigServiceHelper.getConfig("error");	
    Config m_Documentos=ConfigServiceHelper.getConfig("documentos");	
    Boolean visibleAppExt=false;	
    try{	
    visibleAppExt=m_Documentos.getString("VISIBLE_EXT").toUpperCase().equals("SI");	
    }catch(Exception e){	
        visibleAppExt=false;	
    }


%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html locale="true">
<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
<head>
    <title> PLANTILLAS </title>
    <jsp:include page="/jsp/editor/tpls/app-constants.jsp" />
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>

<script type="text/javascript">
    var editorPlantillas = '<%=editor%>';
    var ultimo = false;
    var lista= new Array();
    var listaN= new Array();
    var codigosAplicaciones = new Array();
    var nombresAplicaciones = new Array();
    var codigosProc = new Array();
    var nombresProc = new Array();
    var codigosProcTramite = new Array();
    var codigosTramite = new Array();
    var nombresTramite = new Array();
    var codigosVisibleTramites = new Array();
    var codigosActivo = new Array();
    var nombresActivo = new Array();
    var modoOperacion;
    var filtrar_tramites = false;
    <%
        int i=0;
        vectorAplicaciones=daForm.getListaAplicaciones();
        String codapli=daForm.getCodAplicacion();
        while(!vectorAplicaciones.isEmpty()){
                    GeneralValueObject gVO = (GeneralValueObject)vectorAplicaciones.remove(0);
    %>
    codigosAplicaciones[<%=i%>]=['<%=gVO.getAtributo("codigoAplicacion")%>'];
    nombresAplicaciones[<%=i%>]=['<%=gVO.getAtributo("nombreAplicacion")%>'];
    <%i++;}%>
    <%
        i=0;
        vectorProcedimientos=daForm.getListaProcedimientos();
        while(!vectorProcedimientos.isEmpty()){
            GeneralValueObject gVO = (GeneralValueObject)vectorProcedimientos.remove(0);
    %>
    codigosProc[<%=i%>]=['<%=gVO.getAtributo("codigoProc")%>'];
    nombresProc[<%=i%>]=['<%=gVO.getAtributo("nombreProc")%>'];
    <%i++;}%>
    <%
        i=0;
        vectorTramites=daForm.getListaTramites();
        while(!vectorTramites.isEmpty()){
            GeneralValueObject gVO = (GeneralValueObject)vectorTramites.remove(0);
    %>
    codigosProcTramite[<%=i%>]=['<%=gVO.getAtributo("codigoProc")%>'];
    codigosTramite[<%=i%>]=['<%=gVO.getAtributo("codigoTramite")%>'];
    nombresTramite[<%=i%>]=['<%=gVO.getAtributo("nombreTramite")%>'];
    codigosVisibleTramites[<%=i%>] = ['<%=gVO.getAtributo("codigoTramiteVisible")%>'];
    <%i++;}%>

    procDefecto    = '<%=daForm.getCodProcedimiento()%>';
    tramiteDefecto = '<%=daForm.getCodTramite()%>';
    activoDefecto = 'S';

        <%
            i=0;
            vectorPlantillaActiva=daForm.getListaActivos();
            while(!vectorPlantillaActiva.isEmpty()){
                GeneralValueObject gVO = (GeneralValueObject)vectorPlantillaActiva.remove(0);
                String aux1Activo = (String) gVO.getAtributo("nombreActivo");
                String []traduccActivos = aux1Activo.split("\\|");
                String activoTraducido = traduccActivos[0];
                if (idioma == 4)
                    activoTraducido = traduccActivos[1];
        %>
        codigosActivo[<%=i%>]=['<%=gVO.getAtributo("codigoActivo")%>'];
        nombresActivo[<%=i%>]=['<%=activoTraducido%>'];
        <%i++;}%>

    function inicializar(){  
        window.focus();
        comboAplicacion = new Combo("Aplicacion");
        comboAplicacion.change = function(){ 
            codigoAplicacionSeleccionada=document.forms[0].codAplicacion.value;
            visibilidadProc();
            if(codigoAplicacionSeleccionada==4){
                deshabilitarBoton(document.getElementById("nuevoWord"));
                deshabilitarBoton(document.getElementById("botonProcurar"));
                document.getElementById("eliminarWord").className = document.getElementById("eliminarWord").className.replace("General","Largo");
                document.getElementById("eliminarWord").value = '<%=descriptor.getDescripcion("etiqElimin")%>/<%=descriptor.getDescripcion("recuperar")%>';
                comboProc.addItems(codigosProc,nombresProc);
                if(procDefecto != 'null' && Trim(procDefecto) != ""){
                    comboProc.buscaCodigo(procDefecto);
                }
                document.getElementById("cmdAdjuntarAppDoc").style.display = "";
                document.getElementById("cmdDescargarAppDoc").style.display = "";
                document.getElementById("cmdEtiquetasAppDoc").style.display = "";
                deshabilitarBoton(document.getElementById("cmdAdjuntarAppDoc"));
                deshabilitarBoton(document.getElementById("cmdDescargarAppDoc"));
                habilitarBoton(document.getElementById("cmdEtiquetasAppDoc"));
                comboActivo = new Combo("Activo");
                comboActivo.addItems(codigosActivo,nombresActivo);

            } else { 
                deshabilitarBoton(document.getElementById("cmdAdjuntarAppDoc"));
                document.getElementById("eliminarWord").className = document.getElementById("eliminarWord").className.replace("Largo","General");
                document.getElementById("eliminarWord").value = '<%=descriptor.getDescripcion("etiqElimin")%>';
                document.getElementById("cmdAdjuntarAppDoc").style.display = "";
                document.getElementById("cmdDescargarAppDoc").style.display = "";
                deshabilitarBoton(document.getElementById("cmdAdjuntarAppDoc"));
                deshabilitarBoton(document.getElementById("cmdDescargarAppDoc"));
                if (codigoAplicacionSeleccionada == "") {
                    deshabilitarBoton(document.getElementById("nuevoWord"));
                    deshabilitarBoton(document.getElementById("botonProcurar"));
                    deshabilitarBoton(document.getElementById("cmdDescargarAppDoc"));  
                } else {
                    if(codigoAplicacionSeleccionada==1){ 
                        document.getElementById("cmdEtiquetasAppDoc").style.display = "";
                    habilitarBoton(document.getElementById("botonProcurar"));
                        habilitarBoton(document.getElementById("cmdEtiquetasAppDoc"));
                    } else {
                        habilitarBoton(document.getElementById("nuevoWord"));
                        
                        deshabilitarBoton(document.getElementById("cmdEtiquetasAppDoc"));
                    }
                    habilitarBoton(document.getElementById("cmdAdjuntarAppDoc"));	
                }
            }
        }

        comboProc = new Combo("Procedimiento");
        comboProc.change = function(){ 
            if (document.forms[0].descProcedimiento.value=="") {
                deshabilitarBoton(document.getElementById("botonProcurar"));
                deshabilitarBoton(document.getElementById("nuevoWord"));
                
            } else {
                habilitarBoton(document.getElementById("botonProcurar"));
                habilitarBoton(document.getElementById("nuevoWord"));
                 habilitarBoton(document.getElementById("cmdAdjuntarAppDoc"));	

            }
            filtraTramites();
            if(tramiteDefecto != 'null' && Trim(tramiteDefecto) != ""){
                comboTramites.buscaCodigo(tramiteDefecto);
            } else
                comboTramites.buscaCodigo("");
            if(activoDefecto != 'null' && Trim(activoDefecto) != ""){
                comboActivo.buscaCodigo(activoDefecto);
            } else
                comboActivo.buscaCodigo("S");
            procDefecto='';
            tramiteDefecto='';
        }

        comboTramites = new Combo("Tramite");

        comboAplicacion.addItems(codigosAplicaciones,nombresAplicaciones);
        <%if(codapli != null && !"".equals(codapli)){%>
            comboAplicacion.buscaCodigo('<%=codapli%>');
        <%}%>
        nombreAplicacionSeleccionada=document.forms[0].descAplicacion.value;
        deshabilitarBoton(document.getElementById("botonProcurar"));
        deshabilitarBoton(document.getElementById("nuevoWord"));
        deshabilitarBoton(document.getElementById("modificarWord"));
        deshabilitarBoton(document.getElementById("eliminarWord")) ;
          <%if(visibleAppExt){%>	
            deshabilitarBoton(document.getElementById("btnVisibleExt"));	
        <%}%>
    }

    function limpar(){
        comboAplicacion.activate();
        comboProc.activate();
        comboTramites.activate();
        comboActivo.activate();
        if(activoDefecto != 'null' && Trim(activoDefecto) != ""){
            comboActivo.buscaCodigo(activoDefecto);
        } else
            comboActivo.buscaCodigo("S");
        deshabilitarBoton(document.getElementById("botonProcurar"));
        deshabilitarBoton(document.getElementById("nuevoWord"));
        deshabilitarBoton(document.getElementById("modificarWord"));
        deshabilitarBoton(document.getElementById("eliminarWord"));

          <%if(visibleAppExt){%>	
            deshabilitarBoton(document.getElementById("btnVisibleExt"));	
        <%}%>
        
        comboAplicacion.selectItem(-1);
        document.forms[0].codTramiteSeleccionado.value = "";
        document.forms[0].codProcedimiento.value = "";
        document.forms[0].descProcedimiento.value = "";
        document.forms[0].codTramite.value = "";
        document.forms[0].descTramite.value = "";
        document.getElementById("TablaPlantillas").innerHTML="";
        document.getElementById("subtSelec").style.display="none";
        /*comboProc.cleanItems();
        comboTramites.cleanItems();*/
        visibilidadProc();
    }

    
    function pulsarVerEtiquetas(){
        var codAplicacion = document.forms[0].codAplicacion.value;
        var url = APP_CONTEXT_PATH + "/editor/DocumentosAplicacion.do";
        if(codAplicacion == 4){
            var codProc = document.forms[0].codProcedimiento.value;
            var codVisTram = document.forms[0].codTramite.value;
            if(codProc == '' || codVisTram == ''){
                mostrarErrorCargarEtiquetas(4);
            } else {
                var codsTramites = recuperarCodigos();
                var codTram = codsTramites[comboTramites.selectedIndex];
                var source = APP_CONTEXT_PATH + '/jsp/editor/definicionDocumentoTramite.jsp';
                abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source ,null,
                    'width=700,height=500,scrollbars=no,status=no',function(arrayResp){
                        if (arrayResp != undefined && arrayResp != null){
                            var datos = {
                                'codAplicacion' : codAplicacion,
                                'codProcedimiento' : codProc,
                                'codTramite' : codTram[0],
                                'codDocumento' : "",
                                'nombreDocumento' : "",
                                'docActivo' : "SI",
                                'relacion' : arrayResp[0],
                                'interesado' : arrayResp[1],
                                'editorTexto' : arrayResp[2],
                                'opcion' : "verEtiquetasDocumento"
                            };
                            recuperarEtiquetas(url,datos);
                       }
               });
           }
        } else if (codAplicacion == 1){
            var datos = {
                'codAplicacion' : codAplicacion,
                'opcion' : "verEtiquetasDocumento"
            };
            recuperarEtiquetas(url,datos);
        }
    }
    
    function recuperarEtiquetas(url,datos){
        pleaseWait('on');
        try{
            $.ajax({
                url:  url,
                type: 'POST',
                async: true,
                data: datos,
                success: procesarRespuestaRecuperarEtiquetasDoc,
                error: mostrarErrorRespuestaRecuperarEtiquetasDoc
            });           
        }catch(Err){
            mostrarErrorCargarEtiquetas(3);
        }
    }
    
function procesarRespuestaRecuperarEtiquetasDoc(ajaxResult){
        pleaseWait('off');
        var codAplicacion = document.forms[0].codAplicacion.value;
        if(ajaxResult && ajaxResult != ''){
            var respuesta = JSON.parse(ajaxResult);
            var datos = respuesta.tabla; 
            var error = datos.error;
            var paraInteresado = datos.paraInteresado;
            if(error===0){
                var source = '<%=request.getContextPath()%>/jsp/sge/documentos/relacionEtiquetas.jsp?codApp='+codAplicacion+"&paraInteresado="+paraInteresado;
                abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source ,datos.etiquetas,
                    'width=1100,height=650,scrollbars=no,status=no',function(){
                });
            } else {
                mostrarErrorCargarEtiquetas(error);
            }
        }
    }

    function mostrarErrorRespuestaRecuperarEtiquetasDoc(){
        mostrarErrorCargarEtiquetas(3);
    }
    
    function mostrarErrorCargarEtiquetas(codError){
        pleaseWait('off');
        if(codError===1){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrSinEtiquetas")%>');
        } else if(codError===2){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrObtenerEtiquetas")%>');
        } else if(codError===3){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
        } else if(codError===4){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjEtiquetaObligatorios")%>');
        }
    }
    function filtraTramites(){ 
        var codigos = new Array();
        var nombres = new Array();
        var inicio = false;

        for(var i=0; i < codigosProcTramite.length; i++ ){
            var cod = codigosProcTramite[i]; 
            if (cod == document.forms[0].codProcedimiento.value) {
                inicio=true;
                codigos[codigos.length] = codigosVisibleTramites[i];
                nombres[nombres.length] = nombresTramite[i];
            } else if (inicio){
                break;
            }
        }
        comboTramites.addItems(codigos,nombres);
    }

    function visibilidadProc(){
        var trProc = document.getElementById('trProc');
        var trTramite = document.getElementById('trTramite');
        if (document.forms[0].codAplicacion.value == 4) {
            trProc.style.display='';
            trTramite.style.display='';
            trActivo.style.display = '';
        } else {
            trProc.style.display='none';
            trTramite.style.display='none';
            trActivo.style.display = 'none';
            comboProc.selectedIndex=-1;
            comboTramites.selectedIndex=-1;
        }
    }

    function recuperarCodigos(){ 
       var codigos = [[""]];
       var inicio = false;

       for(var i=0; i < codigosProcTramite.length; i++ ){
           var cod = codigosProcTramite[i];
           if (cod == document.forms[0].codProcedimiento.value) {
               inicio=true;
               codigos[codigos.length] = codigosTramite[i];
           } else if(inicio){
               break;
           }
       }
       return codigos;
    }

    //Carga el vector de documentos que traemos del action
    //en una variable javascript para insertarlos en la tabla
    function cargaDocumentos(){ 
        if(document.forms[0].codAplicacion.value == "4" && document.forms[0].codProcedimiento.value == ''){
            jsp_alerta('A','<%=descriptor.getDescripcion("msjIntrodCodProc")%>');
        } else {
            var codsTramites = recuperarCodigos();
            document.forms[0].codTramiteSeleccionado.value = codsTramites[comboTramites.selectedIndex];            
            document.forms[0].opcion.value = 'cargarDocumentos';
            document.forms[0].target = "oculto";
            document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
            document.forms[0].submit();
        }
    }//de la funcion

    function pulsarEliminar(){

       if (document.forms[0].codAplicacion.value != "" && TablaPlantillas.selectedIndex != -1){
            if (document.forms[0].codAplicacion.value=="4" || jsp_alerta('C','<%=descriptor.getDescripcion("deseaEliminarPlantilla")%>')){
                if (document.forms[0].codAplicacion.value=="4") {
                    document.forms[0].codTramiteSeleccionado.value = lista[TablaPlantillas.selectedIndex][6];
                    document.forms[0].docActivo.value = (lista[TablaPlantillas.selectedIndex][4]=="NO")?"SI":"NO";
                } else 
                   document.forms[0].codTramiteSeleccionado.value = "";

                comboAplicacion.activate();
                comboProc.activate();
                document.forms[0].codDocumento.value=lista[TablaPlantillas.selectedIndex][0];
                document.forms[0].nombreDocumento.value=lista[TablaPlantillas.selectedIndex][1];
                document.forms[0].opcion.value = 'eliminarDocumento';
                document.forms[0].target = "oculto";
                document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
                document.forms[0].submit();
                comboAplicacion.deactivate();
                comboProc.deactivate();
                document.forms[0].codDocumento.value='';
                document.forms[0].nombreDocumento.value='';
                document.forms[0].docActivo.value='';
        }
       } else
           jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }//de la funcion
    
     //Funci?n que controla si un documento es visible o no desde una App Externa	
     function pulsarVisibleExt(){	
        if(document.forms[0].codAplicacion.value!="" && TablaPlantillas.selectedIndex !=-1){	
            if(document.forms[0].codAplicacion.value=="4"){	
                document.forms[0].codTramiteSeleccionado.value=lista[TablaPlantillas.selectedIndex][6];	
                document.forms[0].visibleExt.value = (lista[TablaPlantillas.selectedIndex][8]=="NO")?"SI":"NO";	
            }else{	
                document.forms[0].codTramiteSeleccionado.value="";	
            }	
            comboAplicacion.activate();	
            comboProc.activate();	
            document.forms[0].codDocumento.value=lista[TablaPlantillas.selectedIndex][0];	
            document.forms[0].nombreDocumento.value=lista[TablaPlantillas.selectedIndex][1];	
            document.forms[0].opcion.value="visibleExterior";	
            document.forms[0].target="oculto";	
            document.forms[0].action="<c:url value='/editor/DocumentosAplicacion.do'/>";	
            document.forms[0].submit();	
            comboAplicacion.deactivate();	
            comboProc.deactivate();	
            document.forms[0].codDocumento.value='';	
            document.forms[0].nombreDocumento.value='';	
            document.forms[0].docActivo.value='';	
        }else{	
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');	
     }	
    }	
    

    //Funcion que controla el DobleClik en la tabla,
    //esto hará una cosa u otra dependiendo del modo
    //Funcionará como si hubiésemos pulsado el boton
    //Aceptar ó el de Modificar
    function callFromTableTo(rowID, tabla){
       pulsarModificarWord()
    }//de la funcion

    function pulsarModificarWord(){
       if (document.forms[0].codAplicacion.value != "" && TablaPlantillas.selectedIndex != -1){
           var codigoplantilla=lista[TablaPlantillas.selectedIndex][0];
           var textoplantilla=lista[TablaPlantillas.selectedIndex][1];
           var editorFila=lista[TablaPlantillas.selectedIndex][5];
            if(document.forms[0].codAplicacion.value == "4") {
                if(document.forms[0].codProcedimiento.value == ''){
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjIntrodCodProc")%>');
                    return;
                }
                document.forms[0].codTramiteSeleccionado.value = lista[TablaPlantillas.selectedIndex][6];
                // Recuperamos si es por interesado o no.
                document.forms[0].interesado.value=lista[TablaPlantillas.selectedIndex][2];
                // Recuperamos si es para relación o no.
                if (lista[TablaPlantillas.selectedIndex][3] == "EXPEDIENTE") 
                    document.forms[0].relacion.value = "N";
                else 
                    document.forms[0].relacion.value = "S";
           }
           document.forms[0].codDocumento.value = codigoplantilla;
           document.forms[0].nombreDocumento.value = textoplantilla;
           document.forms[0].docActivo.value='SI';
           if (editorFila=="WORD"){
                document.forms[0].opcion.value = 'verDocumento';
           }else if(editorFila=="ODT"){   	
                var codsTramites = recuperarCodigos();	
                var codTramite = codsTramites[comboTramites.selectedIndex];
                document.forms[0].codTramiteSeleccionado.value = codTramite;	
                document.forms[0].modificando.value = true;	
                document.forms[0].opcion.value="documentoModificar";	
                
                var codProcedimiento = document.forms[0].codProcedimiento.value;
                var codAplicacion= document.forms[0].codAplicacion.value;
                var interesado=document.forms[0].interesado.value;
                var relacion=document.forms[0].relacion.value;
                console.log("webapp...documentosAplicacion.jspcodTramite = " + lista[TablaPlantillas.selectedIndex][6]);
                var source = "<html:rewrite page='/editor/DocumentosAplicacion.do'/>?opcion=documentoModificar&codProcedimiento="+codProcedimiento+"&codTramite="+ lista[TablaPlantillas.selectedIndex][6] +"&codAplicacion="+ codAplicacion;
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,'ventana1',
                    'width=700,height=500,status='+ '<%=statusBar%>',function(listaDocsExternosJSON){
                    if(listaDocsExternosJSON!=undefined){
                        // Se refresca la tabla de documentos
                        refrescarTablaDocumentos();
                    }
                  });
            }else  
                document.forms[0].opcion.value = 'verDocumentoOOffice';

           document.forms[0].target = "oculto";
           document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
           document.forms[0].submit();
       } else 
           jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }//de la funcion

 function descargarDoc(){
        if(TablaPlantillas.selectedIndex !=-1){
            var textoplantilla=lista[TablaPlantillas.selectedIndex][1];
            var codsTramites = recuperarCodigos();
            var codTramite = codsTramites[comboTramites.selectedIndex];
            document.forms[0].codTramiteSeleccionado.value = codTramite;
            document.forms[0].codDocumento.value=lista[TablaPlantillas.selectedIndex][0];
            document.forms[0].nombreDocumento.value=textoplantilla;
            if(lista[TablaPlantillas.selectedIndex][5]=="ODT"){
               document.forms[0].editorTexto.value=lista[TablaPlantillas.selectedIndex][5];
               document.forms[0].opcion.value = 'descargarDocumento';
               document.forms[0].target = "mainFrame";
               document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
               document.forms[0].submit();

           } 

        } else{

            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');	
         }
	
    }



    function pulsarNuevoWord(){
        var codAplicacion = document.forms[0].codAplicacion.value;
        var codProc = document.forms[0].codProcedimiento.value;
        if (codAplicacion!=""){
            if (codAplicacion=="4") {
                if(codProc == ''){
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjIntrodCodProc")%>');
                    return;
                }
                if(comboTramites.selectedIndex <= 0){
                    comboTramites.activate();
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjIntrodCodTram")%>');
                    return;
                }
                var codsTramites = recuperarCodigos();
                document.forms[0].codTramiteSeleccionado.value = codsTramites[comboTramites.selectedIndex];
                
                 var source = '<%=request.getContextPath()%>/jsp/editor/definicionDocumentoTramite.jsp';	
                abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source,null,
                    'width=550,height=350,status='+ '<%=statusBar%>',function(arrayResp){
                          if (arrayResp != undefined && arrayResp != null){
                                document.forms[0].relacion.value = arrayResp[0];
                                document.forms[0].interesado.value =arrayResp[1];
                                document.forms[0].editorTexto.value =arrayResp[2];
                                
                                if ("WORD" == arrayResp[2])
                                    document.forms[0].opcion.value = 'verDocumento';
                                else 
                                    document.forms[0].opcion.value = 'verDocumentoOOffice';
                                
                                document.forms[0].docActivo.value='SI';
                                document.forms[0].codDocumento.value = "";
                                document.forms[0].nombreDocumento.value = "";
                                document.forms[0].target = "oculto";                    
                                document.forms[0].action = '<c:url value='/editor/DocumentosAplicacion.do'/>';
                                document.forms[0].submit();
                          }
                      });
            } else {
                document.forms[0].editorTexto.value = editorPlantillas;
                
                if (editorPlantillas == 'WORD') 
                    document.forms[0].opcion.value = 'verDocumento';
                else
                    document.forms[0].opcion.value = 'verDocumentoOOffice';
                
                document.forms[0].relacion.value = '';
                document.forms[0].interesado.value = '';
                document.forms[0].codProcedimiento.value = '';
                document.forms[0].codTramite.value = '';
                document.forms[0].codTramiteSeleccionado.value = '';

                document.forms[0].docActivo.value='SI';
                document.forms[0].codDocumento.value = "";
                document.forms[0].nombreDocumento.value = "";
                document.forms[0].target = "oculto";                    
                document.forms[0].action = '<c:url value='/editor/DocumentosAplicacion.do'/>';
                document.forms[0].submit();
            }
        } else 
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }//de la funcion

    function pulsarSalir(){
        var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');
        if (resultado == 1){
            document.forms[0].target = '_top';
            document.forms[0].action = "<c:url value='/SalirApp.do?app='/><%= usuarioVO.getAppCod()%>";
            document.forms[0].submit();
        }
    }//de la funcion

    function recuperaDatos(lista1,lista2) {
        if (lista2 != '') { <%-- La aplicacion seleccionada es la de gestion de expediente --%>
            var listaPintar = new Array();
            for(var i=0;i<lista1.length;i++) {
                var strInteresado;
                if(lista1[i][2] == "S") strInteresado = "SE GENERARÁ POR INTERESADO";
                else strInteresado = "NO SE GENERARÁ POR INTERESADO";

                listaPintar[i] = [
                     lista1[i][0]
                    ,lista2[i][9]
                    ,lista1[i][6]
                    ,lista1[i][1]
                    ,strInteresado
                    ,lista1[i][5]
                    ,lista2[i][7]
                    ,lista1[i][7]
                    ,lista1[i][3]
                    <%if(visibleAppExt){%> ,lista1[i][8] <%}%>
                ];

                lista[i] = [
                     lista1[i][0]
                    ,lista1[i][1]
                    ,lista1[i][2]
                    ,lista1[i][5]
                    ,lista2[i][7]
                    ,lista1[i][7]
                    ,lista2[i][10]
                    ,lista1[i][3]
                    <%if(visibleAppExt){%> ,lista1[i][8] <%}%>
                ];

              <%if(visibleAppExt){%>	
                    if(listaPintar[i][9]== 'SI'){
                        listaPintar[i][9]='<span class="fa fa-check 2x"</span>';
                    }else{
                        listaPintar[i][9]='<span class="fa fa-close 2x"></span>';
                    }
                <%}%>   	
                    

                if (listaPintar[i][6] == "SI") {
                    listaPintar[i][6] = '<span class="fa fa-check 2x"></span>';
                    lista[i][4] = 'SI';
                } else {
                    listaPintar[i][6] = '<span class="fa fa-close 2x"></span>';
                    lista[i][4] = 'NO';
                }
              }
            TablaPlantillas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('TablaPlantillas'));
            TablaPlantillas.addColumna('0','left','');
            TablaPlantillas.addColumna('100','left','<%= descriptor.getDescripcion("tramite")%>');
            TablaPlantillas.addColumna('70','left','<%= descriptor.getDescripcion("codigo")%>');
            TablaPlantillas.addColumna('250','left','<%= descriptor.getDescripcion("nombre")%>');
            TablaPlantillas.addColumna('150','center','<%= descriptor.getDescripcion("porInteresado")%>');
            TablaPlantillas.addColumna('100','center','<%= descriptor.getDescripcion("relacion")%>/<%= descriptor.getDescripcion("expediente")%>');
            TablaPlantillas.addColumna('50','center','<%= descriptor.getDescripcion("activo")%>');
            TablaPlantillas.addColumna('80','center','<%= descriptor.getDescripcion("editorTexto")%>');
            TablaPlantillas.addColumna('150','center','<%= descriptor.getDescripcion("gEtiq_firma")%>');
            <% if(visibleAppExt){%>	
               TablaPlantillas.addColumna('70','center','<%= descriptor.getDescripcion("visibleAppExt")%>');
            <%}%>
            
            TablaPlantillas.displayCabecera=true;
            TablaPlantillas.lineas=listaPintar;
            TablaPlantillas.displayTabla();
       } else {
            TablaPlantillas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('TablaPlantillas'));
             TablaPlantillas.addColumna('100','left','<%= descriptor.getDescripcion("codigo")%>');
            TablaPlantillas.addColumna('220','left','<%= descriptor.getDescripcion("nombre")%>');
            TablaPlantillas.addColumna('0','left','');
            TablaPlantillas.addColumna('0','left','');
            TablaPlantillas.addColumna('0','left','');
             TablaPlantillas.addColumna('80','center','<%= descriptor.getDescripcion("editorTexto")%>');            
            TablaPlantillas.displayCabecera=true;
            TablaPlantillas.displayTabla();
            TablaPlantillas.lineas= lista1;
            lista = lista1;
            if(document.forms[0].codAplicacion.value!=4){
                 document.forms[0].codProcedimiento.value="";
                 document.forms[0].codTramite.value="";
                 document.forms[0].descProcedimiento.value="";
                 document.forms[0].descTramite.value="";
            } 
            TablaPlantillas.displayTabla();
        }
        document.getElementById("subtSelec").style.display = "";
        // comboAplicacion.deactivate();
        // comboProc.deactivate();
        // comboTramites.deactivate();
        // deshabilitarBoton(document.getElementById("botonProcurar"));
    }
    
    function eliminarDocumento(){
        var aviso = '<%=descriptor.getDescripcion("docEliminado")%>';
        
        if (document.getElementById("codAplicacion").value == "4") {
            var linha = TablaPlantillas.getLinea();
            if (lista[TablaPlantillas.selectedIndex][4] == 'SI'){
                lista[TablaPlantillas.selectedIndex][4] = 'NO';
                linha[6] = '<span class="fa fa-close 2x"></span>';
            } else {
                lista[TablaPlantillas.selectedIndex][4] = 'SI';
                linha[6] = '<span class="fa fa-check 2x"></span>';
                aviso = '<%=descriptor.getDescripcion("docRecuperado")%>';
            }
            
            TablaPlantillas.setLinea(linha);
        } else {
            TablaPlantillas.removeLinea(TablaPlantillas.selectedIndex);
        }
        document.forms[0].codDocumento.value='';
        document.forms[0].nombreDocumento.value='';

        jsp_alerta('A',aviso);

        deshabilitarBoton(document.getElementById("botonProcurar"));
        deshabilitarBoton(document.getElementById("nuevoWord"));
        deshabilitarBoton(document.getElementById("modificarWord"));
        if (document.getElementById("codAplicacion").value == "4")
            deshabilitarBotonLargo(document.getElementById("eliminarWord"));
        else
            deshabilitarBoton(document.getElementById("eliminarWord"));
    }//de la funcion

     function pulsarNuevaPlantilla(){
        var codAplicacion = document.forms[0].codAplicacion.value;
        var codProc = document.forms[0].codProcedimiento.value;
        if (codAplicacion!="" && codAplicacion=="4") {
            if(codProc == ''){
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjIntrodCodProc")%>');
                return;
            }
            if(comboTramites.selectedIndex <= 0){
                comboTramites.activate();
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjIntrodCodTram")%>');
                return;
            }
            var codsTramites = recuperarCodigos();
            var codTram = codsTramites[comboTramites.selectedIndex];
        
            document.forms[0].codTramiteSeleccionado.value = codTram;
            document.forms[0].modificando.value=false;
            document.forms[0].codDocumento.value = "";
            document.forms[0].nombreDocumento.value = "";
            document.forms[0].docActivo.value = "SI";
            document.forms[0].target = "oculto";
            document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
            
            var source = "<html:rewrite page='/editor/DocumentosAplicacion.do'/>?opcion=documentoNuevo&codProcedimiento="+codProc+"&codTramite="+ codTram+"&codAplicacion="+ codAplicacion;      
            abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source,null,
                'width=700,height=500,scrollbars=no,status='+ '<%=statusBar%>',function(listaDocsExternosJSON){
                if(listaDocsExternosJSON!=undefined){
                    // Se refresca la tabla de documentos
                    refrescarTablaDocumentos();
                }
           });
        }else
        {
            document.forms[0].relacion.value = '';
                document.forms[0].interesado.value = '';
                document.forms[0].codProcedimiento.value = '';
                document.forms[0].codTramite.value = '';
                document.forms[0].codTramiteSeleccionado.value = '';

                document.forms[0].docActivo.value='SI';
                document.forms[0].codDocumento.value = "";
                document.forms[0].nombreDocumento.value = "";
                document.forms[0].target = "oculto";                    
                document.forms[0].action = '<c:url value='/editor/DocumentosAplicacion.do'/>';
                 var source = "<html:rewrite page='/editor/DocumentosAplicacion.do'/>?opcion=documentoNuevo&codProcedimiento="+codProc+"&codTramite="+ codTram+"&codAplicacion="+ codAplicacion;      
            abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source,null,
                'width=700,height=500,scrollbars=no,status='+ '<%=statusBar%>',function(listaDocsExternosJSON){
                if(listaDocsExternosJSON!=undefined){
                    // Se refresca la tabla de documentos
                   
                }
           });
        }

    }//FIN pulsarNuevaPlantilla()
    
    function refrescarTablaDocumentos(){
        $.post("<html:rewrite page='/editor/DocumentosAplicacion.do'/>",{'opcion':'cargarDocumentos','ajax':'si'},function(ajaxResult){
           var listaDocs = new Array();
           var listaDocsOriginal = new Array();
           var datos = JSON.parse(ajaxResult);
           if(datos.length>0){
               for(var i=0; i<datos.length; i++){
                    var plantilla = datos[i].tabla;
                    var rel = "";
                    var relac = plantilla.relacion;
                    if(relac.trim() == "S") rel = "RELACION";
                    else rel = "EXPEDIENTE";
                    
                    listaDocs[i] = [
                        plantilla.codigo, plantilla.descripcion, plantilla.interesado, getEstadoFirmaVisual(plantilla.codigoVisible,plantilla.firma),
                        plantilla.docActivo,rel,plantilla.codigoVisible, plantilla.editorTexto
                        <%if(visibleAppExt){%>, plantilla.visibleExt <%}%>
                    ];
                    listaDocsOriginal[i] = [
                        plantilla.codigo, plantilla.descripcion, plantilla.visibleInternet,plantilla.plantilla,
                        plantilla.codPlantilla,plantilla.interesado, plantilla.firma, plantilla.docActivo, 
                        plantilla.relacion, plantilla.nomeTramite, plantilla.codigoTramite
                        <%if(visibleAppExt){%>, plantilla.visibleExt <%}%>
                    ];
               }
               recuperaDatos(listaDocs,listaDocsOriginal);
           }
        });
    }
    
    function getEstadoFirmaVisual( codigoDocumento, codigoEstadoFirma ) {
        var result = "";
        if (codigoDocumento) {
            var paramCodigoDocumento=''+codigoDocumento+'';
            if ( (!codigoEstadoFirma) || (codigoEstadoFirma=='') ) {
                result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.Null"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
            } else {
                if (codigoEstadoFirma == 'O') {
                    result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.O"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
                } else if (codigoEstadoFirma == 'T') {
                    result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.T"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
                } else if (codigoEstadoFirma == 'L') {
                    result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.L"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
                } else if (codigoEstadoFirma == 'U') {
                    result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.U"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
                } else {
                    result = codigoEstadoFirma;
                }
            }
        }
        return result;
    }


<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>

function pulsarModificarFirmaPlantilla(dscCodigoPlantilla, context) {
    if (context) {
        var filaSeleccionada = $(context).parents('tr').attr('indice');
        var fila = lista[filaSeleccionada];

        var dscCodigoMunicipio = '<%= municipio %>';
        var dscCodigoProcedimiento = document.forms[0].codProcedimiento.value;
        var dscCodigoTramite = fila[6];
        var urlParams = "idMunicipio="+dscCodigoMunicipio;
        var urlParams = urlParams + "&idProcedimiento="+dscCodigoProcedimiento;
        urlParams = urlParams + "&idTramite="+dscCodigoTramite;
        urlParams = urlParams + "&idPlantilla="+dscCodigoPlantilla;

        var source = "<html:rewrite page='/sge/plantillafirma/PreparePlantillaFirma.do'/>?"+ urlParams;
        abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp'/>?source=" + source ,window,
                            'width=650,height=350,scrollbars=no,status='+ '<%=statusBar%>',function(result) {
                                if (result) {
                                    refrescarTablaDocumentos();
                                }
                            });
    }
}

    /////////////// Control de Teclado.
    function checkKeysLocal(evento,tecla){
        var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;

        if('Alt+M'==tecla) {
            if (modoOperacion=="2")	pulsarModificar();
        }
        if('Alt+N'==tecla) {
            if (modoOperacion=="2")	pulsarNuevo();
        }
        if('Alt+A'==tecla) {
            if (modoOperacion=="1") pulsarAceptar();
        }
        if('Alt+L'==tecla) {
            limpiarInputs();
        }
        if('Alt+S'==tecla) {
            pulsarSalir();
        }
        if('Alt+E'==tecla) {
            if (modoOperacion=="1")  pulsarEliminar();
        }
        keyDel(evento);
        if (teclaAuxiliar == 40 || teclaAuxiliar == 38 ){
            upDownTable(TablaPlantillas,lista,teclaAuxiliar);
            return false;
        }
         /*
         if (teclaAuxiliar == 1){
             if (comboAplicacion.base.style.visibility == "visible" && isClickOutCombo(comboAplicacion,coordx,coordy)) setTimeout('comboAplicacion.ocultar()',20);
             if (comboProc.base.style.visibility == "visible" && isClickOutCombo(comboProc,coordx,coordy)) setTimeout('comboProc.ocultar()',20);
             if (comboTramites.base.style.visibility == "visible" && isClickOutCombo(comboTramites,coordx,coordy)) setTimeout('comboTramites.ocultar()',20);
         }
         if (teclaAuxiliar == 9){
            comboAplicacion.ocultar();
            comboProc.ocultar();
            comboTramites.ocultar();
         }*/
    }//de la funcion

    document.onkeydown=checkKeys;
    document.onmouseup = checkKeys;
</script>
</head>

<body class="bandaBody" scroll=no onload="pleaseWait('off');">

    <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
    <form name="formulario" method="post">
    <input type="hidden"  name="opcion" id="opcion">
    <input type="hidden"  name="codDocumento" id="codDocumento">
    <input type="hidden"  name="nombreDocumento" id="nombreDocumento">
    <input type="hidden" name="relacion" value="">
    <input type="hidden"  name="interesado" id="interesado">
     <input type="hidden"  name="editorTexto" id="editorTexto">
    <input type="hidden"  name="docActivo" id="docActivo">
    <input type="hidden" name="codTramiteSeleccionado" id="codTramiteSeleccionado">
    <input type="hidden" name="visibleExt" id="visibleExt">
    <input type="hidden" name="modificando" value="">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("plantdoc")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr>
                <td width="75%">
                    <table width="100%">
                        <tr>
                            <td width="15%" class="etiqueta"><%=descriptor.getDescripcion("aplic")%>:&nbsp;&nbsp;</td>
                            <td width="68%">
                                <input class="inputTextoObligatorio" type="text" name="codAplicacion" size="5" id="codAplicacion" style="width:55">
                                <input class="inputTextoObligatorio" type="text" name="descAplicacion" style="width:450" readonly="true" id="descAplicacion">
                                <A href="" style="text-decoration:none;" name="anchorAplicacion" id="anchorAplicacion"><span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonAplicacion" style="cursor:hand;" id="botonAplicacion"></span></A>
                            </td>
                        </tr>
                        <tr id="trProc" style="display:none">
                            <td width="15%" class="etiqueta"><%=descriptor.getDescripcion("etiqProc")%>:&nbsp;&nbsp;</td>
                            <td width="68%">
                                <input class="inputTextoObligatorio" type="text" name="codProcedimiento" size="5" id="codProcedimiento" style="width:55" onkeyup="return xAMayusculas(this);">
                                <input class="inputTextoObligatorio" type="text" name="descProcedimiento" style="width:450" readonly="true" id="descProcedimiento">
                                <A href="" style="text-decoration:none;" name="anchorProcedimiento" id="anchorProcedimiento"><span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonProcedimiento" style="cursor:hand;" id="botonProcedimiento"></span></A>
                            </td>
                        </tr>
                        <tr id="trTramite" style="display:none">
                            <td width="15%" class="etiqueta"><%=descriptor.getDescripcion("etiqTram")%>:&nbsp;&nbsp;</td>
                            <td width="68%">
                                <input class="inputTexto" type="text" name="codTramite" size="5" id="codTramite" style="width:55">
                                <input class="inputTexto" type="text" name="descTramite" style="width:450" readonly="true" id="descTramite">
                                <A href="" style="text-decoration:none;" name="anchorTramite" id="anchorTramite"><span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonTramite" style="cursor:hand;" id="botonTramite"></span></A>
                            </td>
                        </tr>
                        <tr id="trActivo" style="display:none">
                            <td width="15%" class="etiqueta"><%=descriptor.getDescripcion("etiqAct")%>:&nbsp;&nbsp;</td>
                            <td width="68%">
                                <input class="inputTexto" type="text" name="codActivo" size="5" id="codActivo" style="width:55">
                                <input class="inputTexto" type="text" name="descActivo" style="width:450" readonly="true" id="descActivo">
                                <A href="" style="text-decoration:none;" name="anchorActivo" id="anchorActivo"><span class="fa fa-chevron-circle-down" aria-hidden="true"  name="botonActivo" style="cursor:hand;" id="botonActivo"></span></A>
                            </td>
                        </tr>
                    </table>
                </td>
                <td width="25%" style="vertical-align: bottom">
                    <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("buscar")%>' name="botonProcurar" id="botonProcurar" onclick="cargaDocumentos()">
                    <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("limpiar")%>' name="botonLimpar" id="botonLimpar" onClick="limpar();">
                </td>
            </tr>
            <tr>
                <td colspan="2"> 
                    &nbsp;                    
                </td>
            </tr>
            <tr>
                <td colspan="2" class="sub3titulo" id="subtSelec" style="display:none">
                        <%=descriptor.getDescripcion("selecplantdoc")%>
                </td>
            </tr>
            <tr>
                <td colspan="2" id="TablaPlantillas"></td>
            </tr>
        </table>
        <DIV id="capaBotonesConsulta" name="capaBotones2Word" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("etiqAlta")%>' name="nuevoWord" name="nuevoWord" id="nuevoWord" onclick="pulsarNuevoWord()" accesskey="A">
             <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAdjuntar")%>' name="cmdAdjuntarDoc" id="cmdAdjuntarAppDoc" onclick="pulsarNuevaPlantilla();" style="display: none">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("etiqModif")%>' name="modificarWord" id="modificarWord" onClick="pulsarModificarWord();" accesskey="M">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("etiqElimin")%>' name="eliminarWord" id="eliminarWord" onClick="pulsarEliminar();" accesskey="E">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbDescargar")%>' name="cmdDescargarDoc" id="cmdDescargarAppDoc" onclick="descargarDoc();" style="display: none">
            <input type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("btnVerEtiquetas")%>' name="cmdEtiquetasDoc" id="cmdEtiquetasAppDoc" onclick="pulsarVerEtiquetas();" style="display: none">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("etiqSalir")%>' name="salir2Word" id="salir2Word" onClick="pulsarSalir();" accesskey="S">
            <% if(visibleAppExt){%>	
                 <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("btnVisibleExt")%>' name="btnVisibleExt" id="btnVisibleExt" onclick="pulsarVisibleExt();">
            <%}%>
        </DIV>
    </div>
    </form>

    <div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>

    <script type="text/javascript">
    //Función que se llama cuando seleccionas una fila de una tabla,
    //bien sea de un combo ó tabla normal
    function rellenarDatos(tableName,rowID) { 
        if(TablaPlantillas==tableName) {
            if (TablaPlantillas.selectedIndex!=-1) {
                var codigoplantilla=lista[TablaPlantillas.selectedIndex][0];
                codigoPlantillaSeleccionada=codigoplantilla;
                habilitarBoton(document.getElementById("nuevoWord"));
                habilitarBoton(document.getElementById("modificarWord"));
                habilitarBoton(document.getElementById("cmdDescargarAppDoc"));
                if (document.getElementById("codAplicacion").value == "4") 
                    habilitarBotonLargo(document.getElementById("eliminarWord"));
                else
                    habilitarBoton(document.getElementById("eliminarWord"));
                 <%if(visibleAppExt){%>	
                    habilitarBoton(document.getElementById("btnVisibleExt"));	
                <%}%>
            }
        }
    } //de la funcion
    </script>

    <script type="text/javascript">
    inicializar();
    </script>
</body>
</html:html>
