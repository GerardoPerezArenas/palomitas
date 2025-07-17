<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.AutorizacionesExternasForm" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html:html>

    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        
        <TITLE>::: ADMINISTRACIÓN: Autorizaciones Externas :::</TITLE>
        
      
        <%
            int idioma = 1;
            int apl = 1;
            String css="";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    css=usuario.getCss();
                }
            }
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");

        %>
        
        
          <!-- Estilos -->

        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
        
        
        <!-- Ficheros JavaScript -->
        
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
         <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <SCRIPT type="text/javascript">
            var cod_aplicaciones= new Array();
            var desc_aplicaciones = new Array();
            var listaEntidades = new Array();
            var listaEntidadesOriginal = new Array();
            var listaUsuarios = new Array();
            var listaUsuariosOriginal = new Array();
            var listaAplicaciones = new Array();
            var listaAplicacionesOriginal = new Array();
            var listaEnt = new Array();
            var listaEntOriginal = new Array();
            var lineaUsuario = -1;
            var lineaAplicacion = -1;
            
            function inicializar() {
   <%
            AutorizacionesExternasForm bForm = (AutorizacionesExternasForm) session.getAttribute("AutorizacionesExternasForm");
            Vector listaComboAplicaciones = bForm.getListaComboAplicaciones();
            if (listaComboAplicaciones == null) {
                listaComboAplicaciones = new Vector();
            }
    %>
        var j=0;
    <%for (int i = 0; i < listaComboAplicaciones.size(); i++) {
                GeneralValueObject aplicacion = (GeneralValueObject) listaComboAplicaciones.get(i);%>
                    cod_aplicaciones[j] = '<%=(String) aplicacion.getAtributo("codigo")%>';
                    desc_aplicaciones[j++] = '<%=(String) aplicacion.getAtributo("descripcion")%>';
    <%}
            Vector listaUsuarios = bForm.getListaUsuarios();
            if (listaUsuarios == null) {
                listaUsuarios = new Vector();
            }
    %>
        j=0;
         <%for (int i = 0; i < listaUsuarios.size(); i++) {
                GeneralValueObject aplicacion = (GeneralValueObject) listaUsuarios.get(i);%>
                    listaUsuarios[j] = ['<%=(String) aplicacion.getAtributo("nombreUsuario")%>'];
                        listaUsuariosOriginal[j++] = ['<%=(String) aplicacion.getAtributo("codUsuario")%>',
                            '<%=(String) aplicacion.getAtributo("nombreUsuario")%>'];
                            <%}%>
                            
                            
                            comboAplicacion.addItems(cod_aplicaciones, desc_aplicaciones);
                            
                            tabEntidades.lineas=listaEntidades;
                            refrescaEntidades();
                            /*
                            tabUsuarios.lineas=listaUsuarios;
                            refrescaUsuarios();
                            
                            tabAplicaciones.lineas=listaAplicaciones;
                            refrescaAplicaciones();
                            
                            tabEnt.lineas=listaEnt;
                            refrescaEnt();*/
                        }
                        
                        function pulsarSalir() {
                            document.forms[0].target = "mainFrame";
                            document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                            document.forms[0].submit();
                        }
                        
                        function pulsarCancelarBuscar() {
                            listaEntidades = new Array();
                            listaEntidadesOriginal = new Array();
                            tabEntidades.lineas=listaEntidades;
                            refrescaEntidades();
                            document.forms[0].codAplicacion.value = "";
                            document.forms[0].descAplicacion.value = "";
                            comboAplicacion.activate();
                            var botonBuscar = [document.forms[0].botonBuscar];
                            habilitarGeneral(botonBuscar);
                        }
                        
                        function pulsarBuscar() {
                            if(validarCamposBusqueda()){
                                document.forms[0].opcion.value="buscarEntidades";
                                document.forms[0].target="oculto";
                                document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesExternas.do";
                                document.forms[0].submit();
                                comboAplicacion.deactivate();
                                var botonBuscar = [document.forms[0].botonBuscar];
                                deshabilitarGeneral(botonBuscar);
                            } else jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
                            }
                            
                            function validarCamposBusqueda(){
                                var codOrg = document.forms[0].codAplicacion.value;
                                if(codOrg!="")
                                    return true;
                                return false;
                            }
                            
                            function actualizarTablaEntidades(lista1,lista2) {
                                listaEntidades = lista1;
                                listaEntidadesOriginal = lista2;
                                tabEntidades.lineas=listaEntidades;
                                refrescaEntidades();
                            }
                            
                            function callFromTableTo(rowID,tableName){
                                if(tabEntidades.id == tableName){
                                    baseDeDatos(rowID);
                                } else if(tabUsuarios.id == tableName){
                                buscarListaAplicacionesUsuarios(rowID);
                            } else if(tabAplicaciones.id == tableName){
                            buscarListaEntidadesUsuarios(rowID);
                        } else if(tabEnt.id == tableName){
                    }
                }
                
                function baseDeDatos(rowID) {
                    var datosAEnviar = new Array();
                    datosAEnviar[0] = document.forms[0].codAplicacion.value;
                    datosAEnviar[1] = listaEntidadesOriginal[rowID][2];
                    datosAEnviar[2] = listaEntidadesOriginal[rowID][4];
                    datosAEnviar[3] = listaEntidadesOriginal[rowID][6];
                    var source = "<%=request.getContextPath()%>/administracion/AutorizacionesExternas.do?opcion=inicioDatosEjercicio";
                    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,datosAEnviar,
                        'width=413,height=290,status='+ '<%=statusBar%>',function(datos){
                            if(datos!=undefined){
                                listaEntidadesOriginal[rowID][6] = datos[0];
                                listaEntidades[rowID][3] = datos[0];
                                tabEntidades.lineas=listaEntidades;
                                refrescaEntidades();
                            }
                        });
                    }
                    
                    function buscarListaAplicacionesUsuarios(rowID) {
                        listaEnt = new Array();
                        listaEntOriginal = new Array();
                        tabEnt.lineas=listaEnt;
                        refrescaEnt();
                        listaAplicaciones = new Array();
                        listaAplicacionesOriginal = new Array();
                        tabAplicaciones.lineas=listaAplicaciones;
                        refrescaAplicaciones();
                        if(lineaUsuario == rowID) {
                            lineaUsuario = -1;
                            lineaAplicacion = -1;
                        } else {
                        document.forms[0].codUsuario.value = listaUsuariosOriginal[rowID][0];
                        document.forms[0].opcion.value="buscarAplicacionesUsuarios";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesExternas.do";
                        document.forms[0].submit();
                        lineaUsuario = rowID;
                        lineaAplicacion = -1;
                    }
                }
                
                function actualizarTablaAplicaciones(lista1,lista2) {
                    listaAplicaciones = lista1;
                    listaAplicacionesOriginal = lista2;
                    tabAplicaciones.lineas=listaAplicaciones;
                    refrescaAplicaciones();
                }
                
                function buscarListaEntidadesUsuarios(rowID) {
                    var caja = "boxA" + rowID;
                    if(eval("document.forms[0]." + caja + ".disabled") == false) {
                        if(eval("document.forms[0]." + caja + ".checked") == true) {
                            listaEnt = new Array();
                            listaEntOriginal = new Array();
                            tabEnt.lineas=listaEnt;
                            refrescaEnt();
                            if(lineaAplicacion == rowID) {
                                lineaAplicacion = -1;
                                habilitarBoxAplicaciones(rowID);
                            } else {
                            document.forms[0].codAplic.value = listaAplicacionesOriginal[rowID][1];
                            document.forms[0].codUsuario.value = listaAplicacionesOriginal[rowID][2];
                            document.forms[0].opcion.value="buscarEntidadesUsuarios";
                            document.forms[0].target="oculto";
                            document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesExternas.do";
                            document.forms[0].submit();
                            lineaAplicacion = rowID;
                            deshabilitarBoxAplicaciones(rowID);
                        }
                    } else {
                    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoEstaMarc")%>');
                    }
                } else {
                jsp_alerta('A','<%=descriptor.getDescripcion("msjNoEstaActCaja")%>');
                }
            }
            
            function habilitarBoxAplicaciones(indice) {
                for(var k=0;k<listaAplicaciones.length;k++) {
                    if(k != indice) {
                        var caja = "boxA" + k;
                        eval("document.forms[0]." + caja + ".disabled = false")
                    }
                }
            }
            
            function deshabilitarBoxAplicaciones(indice) {
                for(var k=0;k<listaAplicaciones.length;k++) {
                    if(k != indice) {
                        var caja = "boxA" + k;
                        eval("document.forms[0]." + caja + ".disabled = true")
                    }
                }
            }
            
            function actualizarTablaEnt(lista1,lista2) {
                listaEnt = lista1;
                listaEntOriginal = lista2;
                tabEnt.lineas=listaEnt;
                refrescaEnt();
            }
            
            function crearListas() {
                var lOrganizaciones = "";
                var lEntidades = "";
                var lUsuarios = "";
                var lAplicaciones = "";
                var lUsuar = "";
                var lAplic = "";
                var lOrg = "";
                var lEnt = "";
                var lBD = "";
                if(document.forms[0].codAplicacion.disabled == false) {
                    document.forms[0].grabarPrimeraPest.value = "no";
                } else {
                document.forms[0].grabarPrimeraPest.value = "si";
            }
            for (i=0; i < listaEntidades.length; i++) {
                var caja = "boxE" + i;
                if(eval("document.forms[0]." + caja + ".checked") == true) {
                    lOrganizaciones +=listaEntidadesOriginal[i][2]+'§¥';
                    lEntidades +=listaEntidadesOriginal[i][4]+'§¥';
                    lBD += listaEntidadesOriginal[i][6]+'§¥';
                }
            }
            for (i=0; i < listaAplicaciones.length; i++) {
                var caja = "boxA" + i;
                lUsuarios +=listaAplicacionesOriginal[i][2]+'§¥';
                if(eval("document.forms[0]." + caja + ".checked") == true) {
                    lAplicaciones +=listaAplicacionesOriginal[i][1]+'§¥';
                }
            }
            for (i=0; i < listaEnt.length; i++) {
                var caja = "boxEU" + i;
                lUsuar += listaUsuariosOriginal[lineaUsuario][0]+'§¥';
                lAplic += listaAplicacionesOriginal[lineaAplicacion][1]+'§¥';
                if(eval("document.forms[0]." + caja + ".checked") == true) {
                    lOrg +=listaEntOriginal[i][2]+'§¥';
                    lEnt +=listaEntOriginal[i][4]+'§¥';
                }
            }
            if(listaAplicaciones.length == 0) {
                document.forms[0].grabarSegundaPest.value = "no";
            } else {
            document.forms[0].grabarSegundaPest.value = "si";
        }
        var listas = new Array();
        listas = [lOrganizaciones,lEntidades,lAplicaciones,lUsuarios,lOrg,lEnt,lUsuar,lAplic,lBD];
        return listas;
    }
    
    function pulsarGrabar() {
        var listas = crearListas();
        comboAplicacion.activate();
        document.forms[0].lOrganizaciones.value = listas[0];
        document.forms[0].lEntidades.value = listas[1];
        document.forms[0].lAplicaciones.value = listas[2];
        document.forms[0].lUsuarios.value = listas[3];
        document.forms[0].lOrg.value = listas[4];
        document.forms[0].lEnt.value = listas[5];
        document.forms[0].lUsuar.value = listas[6];
        document.forms[0].lAplic.value = listas[7];  
        document.forms[0].lBaseDatos.value = listas[8];
        document.forms[0].opcion.value="grabarListas";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesExternas.do";
        document.forms[0].submit();
        if(listas[0] != "") {
            comboAplicacion.deactivate();
        }
    }
    
    function listasGrabadas() {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjGrabCorrect")%>');
            /*listaEnt = new Array();
            listaEntOriginal = new Array();
            tabEnt.lineas=listaEnt;
            refrescaEnt();
            listaAplicaciones = new Array();
            listaAplicacionesOriginal = new Array();
            tabAplicaciones.lineas=listaAplicaciones;
            refrescaAplicaciones();
            */
            lineaUsuario = -1;
            lineaAplicacion = -1;
        }
        
        
        </SCRIPT>
        
    </head>
    
    <body class="bandaBody"  onload="javascript:{ pleaseWait('off'); inicializar()}">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        
        <html:form action="/administracion/AutorizacionesExternas.do" target="_self">
            
            <html:hidden  property="opcion" value=""/>
            <input  type="hidden" name="codUsuario">
            <input  type="hidden" name="codAplic">
            <input type="hidden" name="lOrganizaciones" value="">
            <input type="hidden" name="lEntidades" value="">
            <input type="hidden" name="lUsuarios" value="">
            <input type="hidden" name="lAplicaciones" value="">
            <input type="hidden" name="lUsuar" value="">
            <input type="hidden" name="lOrg" value="">
            <input type="hidden" name="lEnt" value="">
            <input type="hidden" name="lAplic" value="">
            <input type="hidden" name="lBaseDatos" value="">
            <input type="hidden" name="grabarPrimeraPest" value="">
            <input type="hidden" name="grabarSegundaPest" value="">

            <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_aplicEnt")%></div>
            <div class="contenidoPantalla">
                <table>
                    <tr>
                        <td>
                            <table border="0" width="100%" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td class="etiqueta" style="width: 10%"><%=descriptor.getDescripcion("gEtiq_Aplicacion")%>:</td>
                                    <td class="etiqueta" style="width: 53%">
                                        <input type="text" name="codAplicacion" id="codAplicacion" size="5" class="inputTextoObligatorio" value="">
                                        <input type="text" name="descAplicacion"  id="descAplicacion" size="65" style="width:360px" class="inputTextoObligatorio" readonly="true" value="">
                                        <A href="" id="anchorAplicacion" name="anchorAplicacion"> 
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                                 name="botonAplicacion" style="cursor:hand;"></span>
                                        </A>
                                    </td>
                                    <td align="right" style="width: 35%">
                                        <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                                               value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                               onClick="pulsarBuscar();" accesskey="B">
                                        <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                                               value='<%=descriptor.getDescripcion("gbCancelar")%>'
                                               onClick="pulsarCancelarBuscar();" accesskey="C">
                                    </td>                                                                                
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr style="padding-top: 15px">
                        <td id="tablaEntidades"></td>
                    </tr>
                </table>
                <div class="botoneraPrincipal">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGrabar")%>
                                               name="cmdGrabar" onClick="pulsarGrabar();" accesskey="G">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%>
                                               name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
                </div>
            </div>
        </html:form>
    <script type="text/javascript">
            
            // JAVASCRIPT DE LA TABLA ENTIDADES
            
            var tabEntidades = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaEntidades'), 900);
            
            tabEntidades.addColumna('125','center','<%= descriptor.getDescripcion("etiq_Autor")%>');
            tabEntidades.addColumna('300','left','<%= descriptor.getDescripcion("gEtiq_Organs")%>');
            tabEntidades.addColumna('300','left','<%= descriptor.getDescripcion("gEtiq_Entid")%>');
            tabEntidades.addColumna('150','left','<%= descriptor.getDescripcion("tit_datBD")%>');
                            
            tabEntidades.displayCabecera=true;
            
            function refrescaEntidades() {
                tabEntidades.displayTabla();
            }
            
            tabEntidades.displayDatos = pintaDatosEntidades;
            
            function pintaDatosEntidades() {
                tableObject = tabEntidades;
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
            document.onmouseup = checkKeys;
            
            function checkKeysLocal(evento, tecla){
                  var teclaAuxiliar="";
                  if(window.event){
                    evento = window.event;
                    teclaAuxiliar =evento.keyCode;
                  }else
                        teclaAuxiliar =evento.which;

                if(teclaAuxiliar == 40){
                    if(tabEntidades==tableObject) {
                        upDownTable(tabEntidades,listaEntidades,teclaAuxiliar);
                    }
                }
                if (teclaAuxiliar == 38){
                    if(tabEntidades==tableObject) {
                        upDownTable(tabEntidades,listaEntidades,teclaAuxiliar);
                    }
                }
                if(teclaAuxiliar == 13){
                    if((tabEntidades.selectedIndex>-1)&&(tabEntidades.selectedIndex < listaEntidades.length)&&(!ultimo)){
                        if(tabEntidades==tableObject)
                            callFromTableTo(tabEntidades.selectedIndex,tabEntidades.id);
                    }
                }
                if(teclaAuxiliar == 1){
                    if (comboAplicacion.base.style.visibility == "visible" && isClickOutCombo(comboAplicacion,coordx,coordy)) setTimeout('comboAplicacion.ocultar()',20);

                }
                if(teclaAuxiliar == 9){
                    comboAplicacion.ocultar();
                }
                keyDel(evento);
            }
            
            var comboAplicacion = new Combo("Aplicacion");
                                                    
        </script>
        
    </BODY>
    
    </html:html>
    
