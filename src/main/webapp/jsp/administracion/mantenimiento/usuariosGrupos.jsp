<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<html:html>

    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <TITLE>::: ADMINISTRACIÓN: Mantenimiento de usuarios y grupos :::</TITLE>
        
        <%
            int maxUsersLicense = 0;
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

                maxUsersLicense = ((Integer)session.getAttribute("MAX-USERS-LICENSE")).intValue();
            }

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
        %>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >

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
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<style type="text/css">
   TR.gris TD {background-color:#C0C0C0;color:white;}
</style>
        
        <SCRIPT type="text/javascript">            
            <%
            /* Recupera el vector de usuarios de la sesion. */

            Vector relacionUsuarios = (Vector) session.getAttribute("RelacionUsuarios");
            int numRelacionUsuarios = 0;
            if (relacionUsuarios != null) {
                numRelacionUsuarios = relacionUsuarios.size();
            }

            // PARA Grupos
            Vector relacionGrupos = (Vector) session.getAttribute("RelacionGrupos");
            int numRelacionGrupos = 0;
            if (relacionGrupos != null) {
                numRelacionGrupos = relacionGrupos.size();
            }
%>
    
    var cod_organizaciones = new Array();
    var desc_organizaciones = new Array();
    var lista = new Array();
    var listaOriginal = new Array();
    var listaP = new Array();
    var listaSel = new Array();
    var fila;
    var ultimo = false;
    var pagin;
    
    /* Para navegacion Usuarios*/
    var lineasPagina   = 14;
    var paginaActual   = 1;
    var paginaSuperior;
    var numUsuarios = <%= numRelacionUsuarios %>;
    var maxUsersLicense = <%= maxUsersLicense %>;
    var numeroPaginas=Math.ceil(numUsuarios/lineasPagina);
    
    // Fin navegacion Usuarios
    
    var listaG = new Array();
    var listaOriginalG = new Array();
    var listaPG = new Array();
    var listaSelG = new Array();
    var filaG;
    var ultimoG = false;
    var paginG;
    
    /* Para navegacion Grupos*/
    var lineasPaginaG   = 14;
    var paginaActualG   = 1;
    var numGrupos = <%=numRelacionGrupos%>;
    
    var numeroPaginas=Math.ceil(numUsuarios/lineasPagina);
    var numeroPaginasG=Math.ceil(numGrupos/lineasPaginaG);
    
    // Fin navegacion grupos
    
    var tabUsuarios;
    var tabGrupos;
    var tableObject=tabUsuarios;
    
function inicializar() {
    <%UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");%>
        window.focus();
        tp1.setSelectedIndex(0);
        listaSel = lista;
        listaSelG = listaG;
        cargaPagina(1,1);
        <%
        UsuariosGruposForm bForm = (UsuariosGruposForm) session.getAttribute("UsuariosGruposForm");
        Vector listaOrganizaciones = bForm.getListaOrganizaciones();
        if (listaOrganizaciones == null) {
            listaOrganizaciones = new Vector();
        }
        %>
        
        var j=0;
        <%
        for (int i = 0; i < listaOrganizaciones.size(); i++) {
            GeneralValueObject aplicacion = (GeneralValueObject) listaOrganizaciones.get(i);
        %>
            cod_organizaciones[j] = '<%=(String) aplicacion.getAtributo("codigo")%>';
            desc_organizaciones[j++] = '<%=(String) aplicacion.getAtributo("descripcion")%>';
        <%
        }
        %>
        comboOrganizacion.addItems(cod_organizaciones, desc_organizaciones);
    }
                
                function cargaPagina(numeroPagina,numeroPaginaG){                        
                    lista = new Array();
                    listaOriginal= new Array();
                    listaG = new Array();
                    listaOriginalG= new Array();
                    document.forms[0].paginaListado.value = numeroPagina;
                    document.forms[0].numLineasPaginaListado.value = lineasPagina;
                    document.forms[0].paginaListadoG.value = numeroPaginaG;
                    document.forms[0].numLineasPaginaListadoG.value = lineasPaginaG;
                    document.forms[0].opcion.value="cargar_pagina";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                    document.forms[0].submit();
                }
                
                function inicializaLista(numeroPagina, numeroPaginaG,numeroUsuarios,numeroGrupos){
                    numUsuarios = numeroUsuarios;
                    numGrupos = numeroGrupos;
                    numeroPaginas=Math.ceil(numUsuarios/lineasPagina);
                    tableObject=tabUsuarios;
                    var j = 0;
                    var jG = 0;
                    
                    paginaActual = numeroPagina;
                    listaP = new Array();
                    paginaActualG = numeroPaginaG;
                    listaPG = new Array();
                    
                    inicio =0;
                    fin = lineasPagina;
                    listaP = listaSel;
                    inicioG =0;
                    finG = lineasPaginaG;
                    listaPG = listaSelG;
                    tabUsuarios.lineas=listaP;
                    refrescaUsuarios();
                    tabGrupos.lineas=listaPG;
                    refrescaGrupos();
                    
                    domlay('enlace',1,0,0,enlaces());
                    domlay('enlaceG',1,0,0,enlacesG());
                    
                }
                
                // JAVASCRIPT DE LA TABLA DE USUARIOS ************************************************************
                
function enlaces() {
    numeroPaginas = Math.ceil(numUsuarios /lineasPagina);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActual,numeroPaginas,'cargaPaginaB');
}

function cargaPaginaB(numeroPagina){
    lista = new Array();
    listaOriginal= new Array();
    document.forms[0].paginaListado.value = numeroPagina;
    document.forms[0].numLineasPaginaListado.value = lineasPagina;
    document.forms[0].opcion.value="cargar_pagina";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
    document.forms[0].submit();
}
// FIN DEL JAVASCRIPT DE LA TABLA DE USUARIOS **************************************************************
                
// JAVASCRIPT DE LA TABLA DE GRUPOS ************************************************************
function enlacesG() {
    numeroPaginasG = Math.ceil(numGrupos /lineasPaginaG);
    return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActualG,numeroPaginasG,'cargaPaginaG');
}

function cargaPaginaG(numeroPaginaG){
    listaG = new Array();
    listaOriginalG= new Array();
    document.forms[0].paginaListadoG.value = numeroPaginaG;
    document.forms[0].numLineasPaginaListadoG.value = lineasPaginaG;
    document.forms[0].opcion.value="cargar_pagina";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
    document.forms[0].submit();
}
                
// FIN DEL JAVASCRIPT DE LA TABLA DE GRUPOS **************************************************************

function callFromTableTo(rowID,tableName){
        if(tabUsuarios.id == tableName){
            datosUsuarios(rowID);
        } else if(tabGrupos.id == tableName){
        datosGrupos(rowID);
    }
}

function pulsarSalir() {
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
    document.forms[0].submit();
}

function datosUsuarios(rowID) {
    var variables = new Array();
    variables[0] = lista[rowID][0];
    var primero = listaOriginal[rowID][0];
    var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=datosUsuarios&primero="+primero;
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,variables,
        'width=1010,height=580,status='+ '<%=statusBar%>',function(datos){
            if(datos!=undefined){
                document.forms[0].opcion.value="inicio";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                document.forms[0].submit();
            }
        });
}

function datosGrupos(rowID) {
    var variables = new Array();
    variables[0] = listaG[rowID][1];
    var primero = listaOriginalG[rowID][0];
    var segundo = listaOriginalG[rowID][1];
    var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=datosGrupos&primero="+primero+"&segundo="+segundo;
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,variables,
            'width=998,height=600,scrollbars=yes,status='+ '<%=statusBar%>',function(datos){
                    if(datos!=undefined){
                        document.forms[0].opcion.value="inicio";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                        document.forms[0].submit();
                    }
            });
    }

// Comprueba el límite de usuarios de la licencia si los hubiese
function comprobarLimiteUsuarios()
{
    var aux = numUsuarios + 1;                        
    if(maxUsersLicense>-1 && aux>maxUsersLicense){        
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjUsersLicExceed")%>' + " " + maxUsersLicense);
            return false;
        }

        // No se puede dar de alta usuarios
        if(maxUsersLicense==-1){
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjUsersNotPermited")%>' + " " + maxUsersLicense);
                return false;
            }

            return true;   
        }

function pulsarAltaUsuarios() {

    //if(true)
    if(comprobarLimiteUsuarios())  
        { 
            var segundo = "alta";
            var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=datosUsuarios&segundo="+segundo;
            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,null,
                    'width=1110,height=730,status='+ '<%=statusBar%>',function(datos){
                            if(datos!=undefined){
                                numUsuarios = numUsuarios + 1;
                                if(numUsuarios>(lineasPagina*paginaSuperior) ) {
                                    numeroPaginas = numeroPaginas + 1;
                                    paginaSuperior = paginaSuperior + 1;
                                }
                                comboOrganizacion.activate();
                                document.forms[0].paginaListado.value = 1;
                                document.forms[0].numLineasPaginaListado.value = lineasPagina;
                                document.forms[0].paginaListadoG.value = 1;
                                document.forms[0].numLineasPaginaListadoG.value = lineasPaginaG;
                                document.forms[0].opcion.value="buscarUsuariosOrganizacion";
                                document.forms[0].target="oculto";
                                document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                                document.forms[0].submit();
                                if(document.forms[0].codOrganizacion.value != "") {
                                    comboOrganizacion.deactivate();
                                }
                            }
                    });
            }
        }

        function pulsarEliminarUsuarios() {
            if(tabUsuarios.selectedIndex != -1) {
                if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarUsu")%>')) {
                    pleaseWait('on');
                    document.forms[0].codUsuario.value = listaOriginal[tabUsuarios.selectedIndex][0];
                    document.forms[0].opcion.value="eliminarUsuario";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                    document.forms[0].submit();
                }
            } else {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            } 
        }

        function usuarioEliminado(eliminado) {
            numUsuarios = numUsuarios - eliminado;
            if(numUsuarios==(lineasPagina*(paginaSuperior-eliminado)) ) {
                if(numeroPaginas != 1) 	numeroPaginas = numeroPaginas - eliminado;
                if(paginaSuperior !=1) paginaSuperior = paginaSuperior - eliminado;
            }
            comboOrganizacion.activate();
            document.forms[0].paginaListado.value = 1;
            document.forms[0].numLineasPaginaListado.value = lineasPagina;
            document.forms[0].paginaListadoG.value = 1;
            document.forms[0].numLineasPaginaListadoG.value = lineasPaginaG;
            document.forms[0].opcion.value="buscarUsuariosOrganizacion";
            document.forms[0].target="oculto";
            document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
            document.forms[0].submit();
            if(document.forms[0].codOrganizacion.value != "") {
                comboOrganizacion.deactivate();
            }
        }

        function pulsarAltaGrupos() {
            var tercero = "alta";
            var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=datosGrupos&tercero="+tercero;
            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,null,
                    'width=998,height=600,status='+ '<%=statusBar%>',function(datos){
                            if(datos!=undefined){
                                numGrupos = numGrupos + 1;
                                if(numGrupos>(lineasPaginaG*paginaSuperiorG) ) {
                                    numeroPaginasG = numeroPaginasG + 1;
                                    paginaSuperiorG = paginaSuperiorG + 1;
                                }
                                document.forms[0].opcion.value="inicio";
                                document.forms[0].target="oculto";
                                document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                                document.forms[0].submit();
                            }
                    });
            }

            function pulsarEliminarGrupos() {
                if(tabGrupos.selectedIndex != -1) {
                    if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarGrp")%>')) {
                        document.forms[0].codGrupo.value = listaOriginalG[tabGrupos.selectedIndex][0];
                        document.forms[0].opcion.value="eliminarGrupo";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                        document.forms[0].submit();
                    }
                } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                } 
            }

            function grupoEliminado() {
                numGrupos = numGrupos - 1;
                if(numGrupos==(lineasPaginaG*(paginaSuperiorG-1)) ) {
                    if(numeroPaginasG != 1) 	numeroPaginasG = numeroPaginasG - 1;
                    if(paginaSuperiorG !=1) paginaSuperiorG = paginaSuperiorG - 1;
                }
                document.forms[0].opcion.value="inicio";
                document.forms[0].target="oculto";
                document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                document.forms[0].submit();
            }

            function pulsarBuscar() {
                if(document.forms[0].codOrganizacion.value != "") {
                    comboOrganizacion.activate();   
                    document.forms[0].paginaListado.value = 1;
                    document.forms[0].numLineasPaginaListado.value = lineasPagina;
                    document.forms[0].paginaListadoG.value = 1;
                    document.forms[0].numLineasPaginaListadoG.value = lineasPaginaG;
                    document.forms[0].opcion.value="buscarUsuariosOrganizacion";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                    document.forms[0].submit();
                    comboOrganizacion.deactivate();                                                
                } else {
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecOrg")%>');
                }
            }

            function pulsarCancelarBuscar() {
                if(document.forms[0].codOrganizacion.value != "") {
                    document.forms[0].nombreT.value="";
                    document.forms[0].loginT.value="";
                    document.forms[0].codOrganizacion.value = "";
                    document.forms[0].paginaListado.value = 1;
                    document.forms[0].numLineasPaginaListado.value = lineasPagina;
                    document.forms[0].paginaListadoG.value = 1;
                    document.forms[0].numLineasPaginaListadoG.value = lineasPaginaG;
                    document.forms[0].opcion.value="buscarUsuariosOrganizacion";
                    document.forms[0].target="oculto";
                    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
                    document.forms[0].submit();                                                
                    comboOrganizacion.activate();
                    comboOrganizacion.buscaLinea(-1);   
                } 
            }

    </SCRIPT>
    </head>
    
    <body class="bandaBody" 
        onload="javascript:{ pleaseWait('off');
                 
                inicializar();
                }">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        
    
        <html:form action="/administracion/UsuariosGrupos.do" target="_self">
            
            <html:hidden  property="opcion" value=""/>
            <html:hidden property="paginaListado" value="1"/>
            <html:hidden property="numLineasPaginaListado" value="12"/>
            <html:hidden property="paginaListadoG" value="1"/>
            <html:hidden property="numLineasPaginaListadoG" value="12"/>
            
            <input type="hidden" name="codGrupo" value="">
            <input type="hidden" name="codUsuario" value="">
            <input type="hidden" name="idioma">
            
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_mantUsuGrup")%></div>
    <div class="contenidoPantalla">
        <div class="tab-pane" id="tab-pane-1">

            <script type="text/javascript">
                tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
            </script>

            <!-- CAPA 1: USUARIOS
------------------------------ -->
            <div class="tab-page" id="tabPage1">

                <h2 class="tab"><%=descriptor.getDescripcion("tit_usuar")%></h2>

                <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>

                <TABLE id ="tablaDatosGralUsuarios" class="contenidoPestanha" style="margin-top: 3px; margin-bottom: 5px">
                    <tr>
                        <td style="width: 100%; padding-bottom: 5px" class="columnP">
                            <table border="0px" width="100%" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td class="etiqueta" align="center" style="width: 10%;margin-left:0px;"><%=descriptor.getDescripcion("gEtiq_Organizacion")%>:</td>
                                    <td class="etiqueta" style="width: 53%" align="left">
                                        <input type="text" name="codOrganizacion" id="codOrganizacion" size="3" class="inputTexto" value="">
                                        <input type="text" name="descOrganizacion"  id="descOrganizacion" size="50" class="inputTexto" readonly="true" value="">
                                        <A href="" id="anchorOrganizacion" name="anchorOrganizacion"> 
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonOrganizacion"
                                                 name="botonOrganizacion" style="cursor:hand;"></span>
                                        </A>
                                    </td>
                                    <td align="right">
                                        <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                                               value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                               onClick="pulsarBuscar();" accesskey="B">
                                        <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                                               value='<%=descriptor.getDescripcion("gbLimpiar")%>'
                                               onClick="pulsarCancelarBuscar();" accesskey="C">
                                    </td>                                                                            
                                </tr>
                                <tr>
                                <td colspan="3">

                        <!------------------------------------------------->

                        <table cellspacing="0px" border="0" style="padding-top: 5px; padding-bottom: 5px; padding-left: 5px" cellpadding="5px">  
                                   <tr>  
                                       <td align="left" class="etiqueta" width="75px" >
                                           <%=descriptor.getDescripcion("etiq_login")%>:
                                       </td> 
                                       <td style="padding-left: 7px">
                                           <input type="text" name="loginT" id="loginT"  maxlength="10" size="10"  class="inputTexto"/>                                                                                   
                                       </td>                                                                               
                                       <td align="left" class="etiqueta" width="50px" align="right">
                                          <%=descriptor.getDescripcion("gEtiq_Nombre")%>:
                                       </td>
                                       <td>
                                           <input type="text" name="nombreT" id="nombreT"  maxlength="50" size="50"  class="inputTexto"  />
                                       </td>
                                   </tr>
                          </table>                                                
                        <!-------------------------------------------------->




                                    </td>                                                                            
                                </tr>    
                            </table>
                        </td>
                    </tr>
                    <TR>
                        <TD style="width: 100%; padding-bottom: 5px" align="center">
                            <table width="100%" rules="cols" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td id="tablaUsuarios">
                                    </td>
                                </tr>
                            </table>
                        </TD>
                    </TR>
                    <tr>
                        <td>
                            <div id="enlace" class="dataTables_wrapper"></div>
                        </td>
                    </tr>
                </TABLE>

                <!-------------------------------------- BOTONES. ------------------------------------------>
                <div style="border: 0; text-align: center">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAltaUsuarios" onClick="pulsarAltaUsuarios();">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbBaja")%> name="cmdEliminarUsuarios" onClick="pulsarEliminarUsuarios();">
                </div>            
            </div>
            <!-- CAPA 2: EXPEDIENTES GRUPOS
------------------------------- -->

            <div class="tab-page" id="tabPage2" style="height:415px;" >

                <h2 class="tab"><%=descriptor.getDescripcion("tit_grup")%></h2>

                <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>

                <TABLE id ="tablaDatosGralGrupos" class="contenidoPestanha" style="margin-top: 10px; margin-bottom: 5px">
                    <tr>
                        <td style="width: 100%; padding-bottom: 5px" id="tablaGrupos">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div id="enlaceG" class="dataTables_wrapper"></div>
                        </td>
                    </tr>
                </TABLE>

                <!-------------------------------------- BOTONES. ------------------------------------------>
                <div style="width:100%; text-align: center">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAltaGrupos" onClick="pulsarAltaGrupos();">
                    <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminarGrupos" onClick="pulsarEliminarGrupos();">
                </div>
            </div>
        </div>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
        </div>
    </div>
    </html:form>
<script type="text/javascript">

// JAVASCRIPT DE LA TABLA USUARIOS

var tabUsuarios = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
        '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
        '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
        '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
        '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
        document.getElementById('tablaUsuarios'));

tabUsuarios.addColumna('225','left','<%= descriptor.getDescripcion("etiq_login")%>');
tabUsuarios.addColumna('650','left','<%= descriptor.getDescripcion("gEtiq_Nombre")%>');
tabUsuarios.addColumna('100','left','<%= descriptor.getDescripcion("etiq_situacion")%>');
tabUsuarios.displayCabecera=true;
tabUsuarios.colorLinea=function(rowID) { 
    var eliminado = listaOriginal[rowID][3];
    if(eliminado == '<%=descriptor.getDescripcion("gbBaja")%>')
    return 'gris';
}

function refrescaUsuarios() {
    tabUsuarios.displayTabla();
}

tabUsuarios.displayDatos = pintaDatosUsuarios;

function pintaDatosUsuarios() {
    tableObject = tabUsuarios;
}

// FIN DE LOS JAVASCRIPT'S DE LA TABLA USUARIOS

// JAVASCRIPT DE LA TABLA GRUPOS

var tabGrupos = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaGrupos'));

tabGrupos.addColumna('225','left','<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
tabGrupos.addColumna('650','left','<%= descriptor.getDescripcion("gEtiq_Nombre")%>');
tabGrupos.displayCabecera=true;

function refrescaGrupos() {
    tabGrupos.displayTabla();
}

tabGrupos.displayDatos = pintaDatosGrupos;

function pintaDatosGrupos() {
    tableObject = tabGrupos;
}

// FIN DE LOS JAVASCRIPT'S DE LA TABLA GRUPOS
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
                            
function checkKeysLocal(evento,tecla){
    var teclaAuxiliar="";
    if(window.event){
        evento = window.event;
        teclaAuxiliar =evento.keyCode;
    }else
        teclaAuxiliar =evento.which;


    if(teclaAuxiliar == 40){
        if(tabUsuarios==tableObject) {
            upDownTable(tabUsuarios,listaSel,teclaAuxiliar);
        } else{
            upDownTable(tabGrupos,listaSelG,teclaAuxiliar);
        }
    }
    if (teclaAuxiliar == 38){
        if(tabUsuarios==tableObject) {
            upDownTable(tabUsuarios,listaSel,teclaAuxiliar);
        } else{
            upDownTable(tabGrupos,listaSelG,teclaAuxiliar);
        }
    }
    if(teclaAuxiliar == 13){
        if((tabUsuarios.selectedIndex>-1)&&(tabUsuarios.selectedIndex < lista.length)&&(!ultimo)){
            if(tabUsuarios==tableObject)
                callFromTableTo(tabUsuarios.selectedIndex,tabUsuarios.id);
        }
        if((tabGrupos.selectedIndex>-1)&&(tabGrupos.selectedIndex < listaG.length)&&(!ultimo)){
            if(tabGrupos==tableObject)
                callFromTableTo(tabGrupos.selectedIndex,tabGrupos.id);
        }
    }
     if(teclaAuxiliar == 1){
         if (comboOrganizacion.base.style.visibility == "visible" && isClickOutCombo(comboOrganizacion,coordx,coordy)) setTimeout('comboOrganizacion.ocultar()',20);
     }
     if(teclaAuxiliar == 9){
          comboOrganizacion.ocultar();
     }
    keyDel(evento);
}

var comboOrganizacion = new Combo("Organizacion");
</script>
</BODY>
</html:html>
    
