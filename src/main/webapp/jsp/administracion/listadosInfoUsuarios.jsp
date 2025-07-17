<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="es.altia.agora.technical.ConstantesDatos"%>
<%@page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html:html>
    <head>
        <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />

        <TITLE>::: ADMINISTRACION Listas de Usuarios:::</TITLE>

        <%
                UsuarioValueObject usuario = new UsuarioValueObject();
                int idioma = 1;
                int apl = 1;
                int munic = 0;
                String css="";
                String idSesion="";
                ArrayList<GeneralValueObject> listaOrg = null;
                if (session.getAttribute("usuario") != null) {
                    idSesion = session.getId();
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    munic = usuario.getOrgCod();
                    css=usuario.getCss();

                    if(request.getAttribute("ListaOrganizaciones") != null)
                        listaOrg = (ArrayList<GeneralValueObject>) request.getAttribute("ListaOrganizaciones");
                }
                Config m_Config = ConfigServiceHelper.getConfig("common");
                String statusBar = m_Config.getString("JSP.StatusBar");
        %>


        <!-- Estilos -->

        <jsp:useBean id="descriptor" scope="request"
                class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor" property="idi_cod" value="<%= idioma%>" />
        <jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>" />


        <!-- Ficheros JavaScript -->

        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
        <script type="text/javascript">
            var url = '<c:url value="/administracion/ConsultarUsuarios.do"/>';
            var apl = '<%=apl%>';
            var APP_ADMINISTRADOR_LOCAL = '<%=ConstantesDatos.APP_ADMINISTRADOR_LOCAL%>';
            var organizacion = '<%=munic%>';
            var codOrganizaciones = new Array();
            var descOrganizaciones = new Array();
            var codVisUnidades;
            var codUnidades;
            var descUnidades;
            var codOrgProcs;
            var codProcs;
            var descProcs;
            var listaUsusUORs;
            var listaUsusProcs;
            var codOrgUsusUORs;
            var codOrgUsusProcs;
            var codUOR;
            var codProc;
            //Mete en un array los paneles, de forma que en la posición 0 está el activo
            var paneles = new Array();
            
            function inicializar(){
                pleaseWait('off');
                // Inicialmente el panel de usuario por unidad es el activo y el de usuarios por procedimiento está deshabilitado. 
                // Así mismo el botón de búsqueda de usuarios por uor aparece desactivado mientras no se haya seleccionado una organizacion y una uor
                paneles = ['UsusUORs','UsusProcs'];
                deshabilitarPanel(obtenerRefPanel(0));
                deshabilitarBotonera(obtenerRefPanel(1));    
                cargarOrganizaciones();
                comprobarAplicacion(obtenerRefPanel(1));
            }
            
            function cargarOrganizaciones(){
                var cont = 0;
                <%
                    if(listaOrg!=null){
                        for(GeneralValueObject orgVO : listaOrg){
                %>
                            codOrganizaciones[cont] = '<%=(String) orgVO.getAtributo("codigo")%>';
                            descOrganizaciones[cont++] = '<%=(String) orgVO.getAtributo("descripcion")%>';
                <%
                        }
                    }
                %>
                comboOrgUsusUORs.addItems(codOrganizaciones, descOrganizaciones);
                comboOrgUsusProcs.addItems(codOrganizaciones, descOrganizaciones);
            }
            
            // FUNCIONES DE RESPUESTA A EVENTOS
            function activarDesactivarPanel(valor){
                if(valor==2) {
                    paneles = ['UsusProcs','UsusUORs'];
                } else {
                    paneles = ['UsusUORs','UsusProcs'];
                }
                limpiarPanel(obtenerRefPanel(0));
                deshabilitarPanel(obtenerRefPanel(0));
                habilitarPanel(obtenerRefPanel(1),true);
                comprobarAplicacion(obtenerRefPanel(1));
            }
            
            function cargarUORs(){
                if(comboOrgUsusUORs.selectedIndex!=-1){
                    codOrgUsusUORs = $('input[name=codOrgUsusUORs]').val();
                    
                    pleaseWait('on');
                    try{
                        $.ajax({
                            url:  url,
                            type: 'POST',
                            async: true,
                            data: {'codOrg':codOrgUsusUORs,'opcion':'recuperarUORsPorOrg'},
                            success: procesarRespuestaCargarUORs,
                            error: mostrarErrorRespuestaCargarUORs
                        });           
                    }catch(Err){
                        pleaseWait('off');
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                    }
                }
            }            
            
            function cargarProcs(){
                if(comboOrgUsusProcs.selectedIndex!=-1){
                    codOrgUsusProcs = $('input[name=codOrgUsusProcs]').val();

                    pleaseWait('on');
                    try{
                        $.ajax({
                            url:  url,
                            type: 'POST',
                            async: true,
                            data: {'codOrg':codOrgUsusProcs,'opcion':'recuperarProcsPorOrg'},
                            success: procesarRespuestaCargarProcs,
                            error: mostrarErrorRespuestaCargarProcs
                        });           
                    }catch(Err){
                        pleaseWait('off');
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                    }
                }
            }            
            
            function buscarUsuariosUORs(){
                if(codOrgUsusUORs!="" && codUOR!=""){
                    pleaseWait('on');
                    deshabilitarPanel("UsusUORs",true);
                    try{
                        $.ajax({
                            url:  url,
                            type: 'POST',
                            async: true,
                            data: {'codOrg':codOrgUsusUORs,'codUOR':codUOR,'opcion':'recuperarUsuariosFiltrados'},
                            success: procesarRespuestaBuscarUsuariosUORs,
                            error: mostrarErrorRespuestaBuscarUsuariosUORs
                        });           
                    }catch(Err){
                        pleaseWait('off');
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                    }
                } 
            }
            
            function buscarUsuariosProcs(){
                if(codOrgUsusProcs!="" && codProc!=""){
                    pleaseWait('on');
                    deshabilitarPanel("UsusProcs",true);
                    try{
                        $.ajax({
                            url:  url,
                            type: 'POST',
                            async: true,
                            data: {'codOrg':codOrgUsusProcs,'codProc':codProc,'opcion':'recuperarUsuariosFiltrados'},
                            success: procesarRespuestaBuscarUsuariosProcs,
                            error: mostrarErrorRespuestaBuscarUsuariosProcs
                        });           
                    }catch(Err){
                        pleaseWait('off');
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                    }
                }
            }
            
            function pulsarLimpiar(panel){
                limpiarPanel(panel);
                habilitarPanel(panel,true);
                comprobarAplicacion(obtenerRefPanel(1));
            }
            
            function pulsarImprimir(panel){
                var codOrg, boton;
                if(panel=='UsusUORs'){
                    codOrg = codOrgUsusUORs;
                    boton = $('#btnImprimirUsusUORs');
                } else if(panel=='UsusProcs'){
                    codOrg = codOrgUsusProcs;
                    boton = $('#btnImprimirUsusProcs');
                } else return false;
                
                if(codOrg!="" && ((panel=="UsusUORs" && codUOR!="") || (panel=="UsusProcs" && codProc!=""))){
                    pleaseWait('on');
                    deshabilitarBotones([boton]);
                    var datos = {'codOrg': codOrg, 'codUOR': codUOR, 'codProc': codProc, 'opcion': 'recuperarUsuariosFiltrados', 'imprimir': 'si', 'panel': panel};
                    
                    try{
                        $.ajax({
                            url:  url,
                            type: 'POST',
                            async: true,
                            data: datos,
                            success: procesarRespuestaImprimirResultados,
                            error: mostrarErrorRespuestaImprimirResultados
                        });           
                    }catch(Err){
                        pleaseWait('off');
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
                    }
                }
            }
            
            // FUNCIONES DE RESPUESTA AJAX
            function procesarRespuestaCargarUORs(result){
                pleaseWait('off');
                
                var datos;
                if(result){
                    datos = JSON.parse(result);
                    if(datos.length>0){
                        codVisUnidades = new Array();
                        codUnidades = new Array();
                        descUnidades = new Array();
                        for(var cont=0;cont<datos.length;cont++){
                            var unidad = datos[cont];
                            codUnidades[cont] = unidad.uor_cod;
                            codVisUnidades[cont] = unidad.uor_cod_vis;
                            descUnidades[cont] = unidad.uor_nom;
                        }
                        comboUORs.selectItem(-1);
                        comboUORs.addItems(codVisUnidades, descUnidades);
                        
                    }
                } else jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            function mostrarErrorRespuestaCargarUORs(){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            function procesarRespuestaBuscarUsuariosUORs(result){
                pleaseWait('off');
                
                var resultado;
                if(result){
                    listaUsusUORs = new Array();
                    resultado = JSON.parse(result);
                    if((resultado.tabla)!=undefined && (resultado.tabla).error!=undefined && (resultado.tabla).error.length>0)
                        jsp_alerta("A",(resultado.tabla).error)
                    else if(resultado.length>0){
                        for(var iter=0; iter<resultado.length; iter++){
                            var item = resultado[iter].tabla;
                            var uniOrg = item.elemento;
                            var usuarios = item.usuarios;
                            var filasUsuarios = "";
                            if(usuarios.length>0){
                                for(var iterUsus=0; iterUsus<usuarios.length; iterUsus++){
                                    var usu =  usuarios[iterUsus];
                                    filasUsuarios += "<p>"+usu+"</p>";
                                }
                            }
                            listaUsusUORs[iter] = [uniOrg,filasUsuarios];
                        }
                    }
                    refresca(tablaUsusUORs,listaUsusUORs);
                    mostrarElemento('tablaUsusUORs');
                    habilitarBotones([$('#btnLimpiarUsusUORs'),$('#btnImprimirUsusUORs')]);
                } else jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            function mostrarErrorRespuestaBuscarUsuariosUORs(){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            function procesarRespuestaCargarProcs(result){
                pleaseWait('off');
                
                var datos;
                if(result){
                    datos = JSON.parse(result);
                    if(datos.length>0){
                        codOrgProcs = new Array();
                        codProcs = new Array();
                        descProcs = new Array();
                        for(var cont=0;cont<datos.length;cont++){
                            var proc = datos[cont];
                            codProcs[cont] = proc.txtCodigo;
                            codOrgProcs[cont] = proc.codMunicipio;
                            descProcs[cont] = proc.txtDescripcion;
                        }
                        comboProcs.selectItem(-1);
                        comboProcs.addItems(codProcs, descProcs);
                        
                    }
                } else jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            function mostrarErrorRespuestaCargarProcs(){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            function procesarRespuestaBuscarUsuariosProcs(result){
                pleaseWait('off');
                if(result){
                    listaUsusProcs = new Array();
                    var resultado = JSON.parse(result);
                    if((resultado.tabla)!=undefined && (resultado.tabla).error!=undefined && (resultado.tabla).error.length>0)
                        jsp_alerta("A",(resultado.tabla).error)
                    else if(resultado.length>0){
                        for(var iter=0; iter<resultado.length; iter++){
                            var item = resultado[iter].tabla;
                            var proc = item.elemento;
                            var usuarios = item.usuarios;
                            var filasUsuarios = "";
                            if(usuarios.length>0){
                                for(var iterUsus=0; iterUsus<usuarios.length; iterUsus++){
                                    var usu =  usuarios[iterUsus];
                                    filasUsuarios += "<p>"+usu+"</p>";
                                }
                            }
                            listaUsusProcs[iter] = [proc,filasUsuarios];
                        }
                    }
                    refresca(tablaUsusProcs,listaUsusProcs);
                    mostrarElemento('tablaUsusProcs');
                    habilitarBotones([$('#btnLimpiarUsusProcs'),$('#btnImprimirUsusProcs')]);
                } else jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            function mostrarErrorRespuestaBuscarUsuariosProcs(){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            function procesarRespuestaImprimirResultados(result){
                pleaseWait('off');
                if(result){
                    var resultado = JSON.parse(result);
                    var datos = resultado.tabla;
                    if(datos!=undefined && datos.error!=undefined && datos.error.length>0)
                        jsp_alerta("A",datos.error)
                    else {
                        var panel = datos.panel;
                        var jsonFichero = datos.fichero;

                        var inputs = '';
                        for(var i=0;i<jsonFichero.length;i++){
                            inputs += '<input type="hidden" name="jsonFichero" value="'+jsonFichero[i]+'" />'
                        }

                        if($('input[name=jsonFichero]').length>0){
                            $('input[name=jsonFichero]').each(function(index){
                               $(this).remove();
                            });
                        }   
                        $('#form'+panel).append(inputs);
                        $('#form'+panel).attr("action",url+"?opcion=descargarFichero");
                        $('#form'+panel).submit();
                    }
                } else jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            function mostrarErrorRespuestaImprimirResultados(){
                jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
            }
            
            // OTRAS FUNCIONES NECESARIAS
            function obtenerRefPanel(tipo){
                // El parametro indica que referencia de panel queremos recuperar, 1: panel activo, 0: panel no activo
                if(paneles.length>0){
                    if(tipo==1) return paneles[0];
                    else if(tipo==0) return paneles[1];
                }
                return "";
            }
            
            function comprobarAplicacion(panel){
                if(apl==APP_ADMINISTRADOR_LOCAL){
                    if(panel=='UsusUORs'){
                        codOrgUsusUORs = organizacion;
                        comboOrgUsusUORs.buscaCodigo(codOrgUsusUORs);
                        comboOrgUsusUORs.deactivate();
                    } else if(panel=='UsusProcs'){
                        codOrgUsusProcs = organizacion;
                        comboOrgUsusProcs.buscaCodigo(codOrgUsusProcs);
                        comboOrgUsusProcs.deactivate();
                    } 
                }
            }
            
            function deshabilitarPanel(panel,nonAgachar){
                if(panel=="") return false;
                
                deshabilitarBotonera(panel);
                if(panel=='UsusUORs'){
                    comboOrgUsusUORs.deactivate();
                    comboUORs.deactivate();
                    if (!nonAgachar) {
                        document.getElementById("capaUsusUORs").style.display='none';
                        document.getElementById("tablaUsusUORs").style.display='none';
                    }
                } else if(panel=='UsusProcs') {
                    comboOrgUsusProcs.deactivate();
                    comboProcs.deactivate();
                    if (!nonAgachar) {
                        document.getElementById("capaUsusProcs").style.display='none';
                        document.getElementById("tablaUsusProcs").style.display='none';
                    }
                }
            }
            
            function habilitarPanel(panel,nonAgacharTaboa){
                if(panel=="") return false;
                
                if(panel=='UsusUORs') {
                    comboOrgUsusUORs.activate();
                    comboUORs.activate();
                    document.getElementById("capaUsusUORs").style.display='';
                    if (!nonAgacharTaboa) {
                        document.getElementById("tablaUsusUORs").style.display='';
                    }
                } else if(panel=='UsusProcs'){
                    comboOrgUsusProcs.activate();
                    comboProcs.activate();
                    document.getElementById("capaUsusProcs").style.display='';
                    if (!nonAgacharTaboa) {
                        document.getElementById("tablaUsusProcs").style.display='';
                    }
                }
            }
            
            function habilitarBotonera(botonera){
                $('#botonera'+botonera+' input').each(function(index){
                    $(this).removeAttr('disabled');
                });
            }
            
            function deshabilitarBotonera(botonera){
                $('#botonera'+botonera+' input').each(function(index){
                    $(this).attr('disabled','disabled');
                });
            }
            
            function habilitarBotones(botones){
                $.each(botones,function(index,element){
                    $(element).removeAttr('disabled');
                });
            }
            
            function deshabilitarBotones(botones){
                $.each(botones,function(index,element){
                    $(element).attr('disabled','disabled');
                });
            }
            
            function limpiarPanel(panel){
                var tabla;
                if(panel=='UsusUORs'){
                    comboOrgUsusUORs.selectItem(-1);
                    comboUORs.selectItem(-1);
                    listaUsusUORs = new Array();
                    tabla = tablaUsusUORs;
                } else {
                    comboOrgUsusProcs.selectItem(-1);
                    comboProcs.selectItem(-1);
                    listaUsusProcs = new Array();
                    tabla = tablaUsusProcs;
                }
                limpiarTabla(tabla);
                ocultarElemento('tabla'+panel);
                deshabilitarBotonera(panel);
            }
        </script>
    </head>

    <body class="bandaBody" onload="inicializar();">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        

        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_UnidOrgUsu")%></div>
        <div class="contenidoPantalla">
            <TABLE id="tablaDatosGral" style="width: 100%;">
                <tr>
                    <td class="etiqueta">
                        <div style="width:100%">
                            <input type="radio" name="panelActivo" value="1" onclick="activarDesactivarPanel(this.value);" checked="true" />
                            <%=descriptor.getDescripcion("etiqBuscarUsusPorUOR")%>
                        </div>
                        <div style="width:100%">
                            <input type="radio" name="panelActivo" value="2" onclick="activarDesactivarPanel(this.value);" />
                            <%=descriptor.getDescripcion("etiqBuscarUsusPorProc")%>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="capaUsusUORs" style="width: 100%">
                            <form id="formUsusUORs" method="post">
                                <table id="panelUsusUORs" style="width: 97%">
                                    <tr>
                                        <td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Organizacion")%>:</td>
                                        <td class="columnP">
                                            <input type=text style="width:14%;" id="obligatorio" class="inputTextoObligatorio"
                                                   name="codOrgUsusUORs" size="8" maxlength="5"
                                                   onkeyup="return SoloDigitosNumericos(this);"/>
                                            <input type=text style="width:78%;"
                                                   class="inputTextoObligatorio" name="descOrgUsusUORs"
                                                   readonly="true"/> 
                                            <A href="" id="anchorOrgUsusUORs" name="anchorOrgUsusUORs"> 
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                     id="botonOrgUsusUORs" name="botonOrgUsusUORs"
                                                     style="cursor:hand;"></span> 
                                            </A>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_unidOrg")%>:</td>
                                        <td class="columnP">
                                            <input type="hidden" name="codUnidadOrganica" id="codUnidadOrganica"/> 
                                            <input type="text" style="width:14%;" class="inputTextoObligatorio" name="codUOR"
                                                   size="8" onkeyup="return xAMayusculas(this);"/>
                                            <input type="text" style="width:78%"
                                                   class="inputTextoObligatorio" name="descUOR" readonly/>
                                            <A href="javascript:{onClickHrefUOR()}" id="anchorUOR" name="anchorUOR"> 
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                     id="botonUOR" name="botonUOR"></span>
                                            </A>
                                        </td>
                                    </tr>            
                                </table>
                            </form>
                            <p id="botoneraUsusUORs" style="float: right; margin-right: 24px">
                                <INPUT type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbBuscar")%>' name="btnBuscarUsusUORs" id="btnBuscarUsusUORs" onClick="buscarUsuariosUORs();" />
                                <INPUT type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbImprimir")%>' name="btnImprimirUsusUORs" id="btnImprimirUsusUORs" onClick="pulsarImprimir(obtenerRefPanel(1));" />
                                <INPUT type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="btnLimpiarUsusUORs" id="btnLimpiarUsusUORs" onClick="pulsarLimpiar(obtenerRefPanel(1));" />
                            </p>
                        </div>
                        <div id="capaUsusProcs" style="width: 100%">
                            <form id="formUsusProcs" method="post">
                                <table id="panelUsusProcs" style="width: 97%">
                                    <tr>
                                        <td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Organizacion")%>:</td>
                                        <td class="columnP">
                                            <input type=text style="width:14%;" id="obligatorio" class="inputTextoObligatorio"
                                                   name="codOrgUsusProcs" size="8" maxlength="5"
                                                   onkeyup="return SoloDigitosNumericos(this);"/>
                                            <input type=text style="width:78%;"
                                                   class="inputTextoObligatorio" name="descOrgUsusProcs"
                                                   readonly="true"/> 
                                            <A href="" id="anchorOrgUsusProcs" name="anchorOrgUsusProcs"> 
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                     id="botonOrgUsusProcs" name="botonOrgUsusProcs"
                                                     style="cursor:hand;"></span> 
                                            </A>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%" class="etiqueta"><%= descriptor.getDescripcion("gb_etiqProcedimiento")%>:</td>
                                        <td class="columnP">
                                            <input type="text" style="width:14%;" class="inputTextoObligatorio" name="codProc"
                                                   size="8" onkeyup="return xAMayusculas(this);"/>
                                            <input type="text" style="width:78%;"
                                                   class="inputTextoObligatorio" name="descProc" readonly/>
                                            <A href="" id="anchorProc" name="anchorProc"> 
                                                <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                     id="botonProc" name="botonProc"></span>
                                            </A>
                                        </td>
                                    </tr>            
                                </table>
                            </form>
                            <p id="botoneraUsusProcs" style="float: right; margin-right: 24px">
                                <INPUT type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbBuscar")%>' name="btnBuscarUsusProcs" id="btnBuscarUsusProcs" onClick="buscarUsuariosProcs();" />
                                <INPUT type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbImprimir")%>' name="btnImprimirUsusProcs" id="btnImprimirUsusProcs" onClick="pulsarImprimir(obtenerRefPanel(1));" />
                                <INPUT type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbCancelar")%>' name="btnLimpiarUsusProcs" id="btnLimpiarUsusProcs" onClick="pulsarLimpiar(obtenerRefPanel(1));" />
                            </p>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="tablaUsusUORs"></div>
                        <div id="tablaUsusProcs"></div>
                    </td>
                </tr>
            </table>
        </div>                        
        <script type="text/javascript">
            var comboOrgUsusUORs = new Combo("OrgUsusUORs");
            var comboOrgUsusProcs = new Combo("OrgUsusProcs");
            var comboUORs = new Combo("UOR");
            var comboProcs = new Combo("Proc");

            comboOrgUsusUORs.change = cargarUORs;
            comboUORs.change = function() {
                habilitarBotones([$('#btnBuscarUsusUORs')]);
                // Guardamos en el input hidden codUnidadOrganica el codigo interno correspondiente al codigo visible seleccionado
                var codVis = $('input[name=codUOR]').val();
                var pos = codVisUnidades.indexOf(codVis);
                $('#codUnidadOrganica').val(codUnidades[pos]);
                codUOR = $('#codUnidadOrganica').val();
            }
            
            comboOrgUsusProcs.change = cargarProcs;
            comboProcs.change = function() {
                habilitarBotones([$('#btnBuscarUsusProcs')]);
                codProc = $('input[name=codProc]').val();
            }
            
            var tablaUsusUORs = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUsusUORs'));

            // Tabla con columnas ordenables.
            // Anadir tipo de dato a cada columna;
            tablaUsusUORs.addColumna('70','left','<%= descriptor.getDescripcion("gEtiq_unidOrg")%>','String');
            tablaUsusUORs.addColumna('80','center','Usuarios','String');
            tablaUsusUORs.displayCabecera=true;
            
            var tablaUsusProcs = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaUsusProcs'));

            // Tabla con columnas ordenables.
            // Anadir tipo de dato a cada columna;
            tablaUsusProcs.addColumna('80','left','<%= descriptor.getDescripcion("gb_etiqProcedimiento")%>','String');
            tablaUsusProcs.addColumna('70','center','<%= descriptor.getDescripcion("tit_usuar")%>','String');
            tablaUsusProcs.displayCabecera=true;

            function refresca(tabla,datos){
                tabla.lineas = datos;
                tabla.displayTabla();
            }
            
            function limpiarTabla(tabla){
                var lineas = new Array();
                tabla.lineas = lineas;
                tabla.displayTabla();
            }
            
            function ocultarElemento(id){
                document.getElementById(id).style.display='none';
            }
            
            function mostrarElemento(id){
                document.getElementById(id).style.display='';
            }
        </script>
    </BODY>
</html:html>
