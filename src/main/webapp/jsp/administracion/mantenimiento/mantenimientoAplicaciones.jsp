<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Mantenimiento de Aplicaciones</title>
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                css=usuarioVO.getCss();
            }%>
            
             <!-- Estilos -->
            
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript">
            var lista = new Array();
            var datosAplicaciones = new Array();
            var vectorCamposRejilla = ['codigo','descripcion','ejecutable',
                'icono','diccionario','informes','version'];
            var cod_idioma = new Array();
            var desc_idioma = new Array();
            var titulosOriginal = new Array();
            var listaTitulos = new Array();
            var lTitulos = new Array();
            var numIdiomas = 0;
            
            
            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
            function inicializar(){
                window.focus();
                recuperaDatosIniciales();
                tp1.setSelectedIndex(0);
                comboIdioma.addItems(cod_idioma,desc_idioma);	
            }
            
            function recuperaDatosIniciales(){
            <%
            MantenimientosAdminForm bForm =
                    (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
            Vector listaAplicaciones = bForm.getListaAplicaciones();
            int lengthAplicaciones = listaAplicaciones.size();
            int i = 0;
%>
    var j=0;
         <%for (i = 0; i < lengthAplicaciones; i++) {
                GeneralValueObject aplicaciones = (GeneralValueObject) listaAplicaciones.get(i);
                Vector datosTitulos = (Vector) aplicaciones.getAtributo("titulos");
         %>lTitulos = new Array();<%
    for (int t = 0; t < datosTitulos.size(); t++) {
        GeneralValueObject tit = (GeneralValueObject) datosTitulos.elementAt(t);
        String codIdioma = (String) tit.getAtributo("codIdioma");
        String desIdioma = (String) tit.getAtributo("desIdioma");
        String titulo = (String) tit.getAtributo("titulo");
                 %>
                     lTitulos[<%=t%>] = ['<%=codIdioma%>','<%=desIdioma%>','<%=titulo%>'];
                         <%}%>
                         
                         datosAplicaciones[j] = ['<%=(String) aplicaciones.getAtributo("codigo")%>',
                             '<%=(String) aplicaciones.getAtributo("descripcion")%>',
                             '<%=(String) aplicaciones.getAtributo("ejecutable")%>',
                             '<%=(String) aplicaciones.getAtributo("seguridad")%>',
                             '<%=(String) aplicaciones.getAtributo("accesoDefecto")%>',
                             '<%=(String) aplicaciones.getAtributo("diccionario")%>',
                             '<%=(String) aplicaciones.getAtributo("informes")%>',
                             '<%=(String) aplicaciones.getAtributo("version")%>',
                             '<%=(String) aplicaciones.getAtributo("conEntidades")%>',
                             '<%=(String) aplicaciones.getAtributo("conEjercicios")%>',
                             '<%=(String) aplicaciones.getAtributo("icono")%>',lTitulos];
                             
                             lista[j] = [datosAplicaciones[j][0],datosAplicaciones[j][1]];
                             j++;
                             <%}%>
                             tablaAplicaciones.lineas = lista;
                             refresca(tablaAplicaciones);
                             
         <%Vector listaIdiomas = bForm.getListaIdiomas();
            if (listaIdiomas == null) {
                listaIdiomas = new Vector();
            }
            int lengthIdiomas = listaIdiomas.size();
         %>numIdiomas=<%=lengthIdiomas%>;<%
            int m = 0;
         %>
             var n=0;
         <%for (m = 0; m < lengthIdiomas; m++) {
                GeneralValueObject idiomas = (GeneralValueObject) listaIdiomas.get(m);%>
                    cod_idioma[n] = '<%=(String) idiomas.getAtributo("codigo")%>';
                    desc_idioma[n] = '<%=(String) idiomas.getAtributo("descripcion")%>';
                    n++;
                    <%}%>
                }
                
                function construirIdiomasTitulos(){
                    var cadena ="";
                    for (i=0; i < titulosOriginal.length; i++) {
                        cadena += titulosOriginal[i][0]+'§¥' +titulosOriginal[i][2]+'§¥';
                    }	
                    return cadena;
                }
                
                function cargarTablaAplicaciones(listaE){
                    lista = listaE;
                    tablaAplicaciones.lineas = lista;
                    refresca(tablaAplicaciones);
                }
                
                function cargarTablaIdiomas(listaE){
                    listaTitulos = listaE;
                    tablaIdiomas.lineas = listaTitulos;
                    refresca(tablaIdiomas);
                }
                
                // FUNCIONES DE LIMPIEZA DE CAMPOS
                function limpiarFormulario(){
                    tablaAplicaciones.lineas = new Array();
                    refresca(tablaAplicaciones);
                    limpiarCamposRejilla();
                }
                
                function limpiarCamposRejilla(){
                    limpiar(vectorCamposRejilla);
                }
                
                function validarTitulos(){
                    if (listaTitulos.length==numIdiomas) return true;
                    else return false;
                }		
                
                // FUNCIONES DE PULSACION DE BOTONES
                function pulsarSalir(){
                    document.forms[0].target = "mainFrame";
                    document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                    document.forms[0].submit();
                }
                
                function pulsarEliminar() {
                    if(filaSeleccionada(tablaAplicaciones)) {
                        document.forms[0].opcion.value = 'eliminar';
                        document.forms[0].target = "oculto";
                        document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Aplicaciones.do';
                        document.forms[0].submit();
                        limpiarCamposRejilla();
                        pulsarLimpiar();
                    }else 
                    jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                    }
                    
                    
                function pulsarModificar(){
                    if(filaSeleccionada(tablaAplicaciones)){
                        if(validarCamposRejilla()){
                            if (validarTitulos()){
                                document.forms[0].titulos_idiomas.value=construirIdiomasTitulos();
                                document.forms[0].opcion.value = 'modificar';
                                document.forms[0].target = "oculto";
                                document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Aplicaciones.do';
                                document.forms[0].submit();
                                limpiarCamposRejilla();
                                pulsarLimpiar();
                            }else{
                            jsp_alerta ("A",'<%=descriptor.getDescripcion("msjTitulosOblig")%>');
                            }
                        }else
                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
                        }
                        else
                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                            }

                            function pulsarAlta(){
                                if (validarCamposRejilla()){
                                    if(noEsta()){
                                        if (validarTitulos()){
                                            document.forms[0].titulos_idiomas.value=construirIdiomasTitulos();
                                            document.forms[0].opcion.value = 'alta';
                                            document.forms[0].target = "oculto";
                                            document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/Aplicaciones.do';
                                            document.forms[0].submit();
                                            limpiarCamposRejilla();
                                            pulsarLimpiar();
                                        } else{
                                        jsp_alerta("A",'<%=descriptor.getDescripcion("msjTitulosOblig")%>');
                                        }
                                    }
                                }else
                                jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
                                }

                                function pulsarLimpiar(){
                                    limpiarCamposRejilla();
                                    limpiarTablaTitulos();
                                }

                                function limpiarTablaTitulos(){
                                    comboIdioma.selectItem(-1);
                                    document.forms[0].tituloApl.value="";
                                    tablaIdiomas.lineas = new Array();
                                    refresca(tablaIdiomas);
                                }

                                // FUNCIONES DE VALIDACION Y COMPROBACION DEL FORMULARIO
                                function noEsta(indice){
                                    var cod = document.forms[0].codigo.value;
                                    for(i=0;(i<lista.length);i++){
                                        if(i!=indice){
                                            if((lista[i][0]) == cod)
                                                return false;
                                        }
                                    }
                                    return true;
                                }

                                function filaSeleccionada(tabla){
                                    var i = tabla.selectedIndex;
                                    if((i>=0)&&(!tabla.ultimoTable)) return true;
                                    return false;
                                }

                                function validarCamposRejilla(){
                                    var codigo = document.forms[0].codigo.value;
                                    var descrip = document.forms[0].descripcion.value;
                                    var ejecutable = document.forms[0].ejecutable.value;
                                    if((codigo!="")&&(descrip!="")&&(ejecutable!=""))
                                        return true;
                                    return false;
                                }

                                function recuperaDatos(lista2) {
                                    limpiarCamposRejilla();
                                    lista = lista2;
                                    tablaAplicaciones.lineas=lista;
                                    refresca(tablaAplicaciones);
                                }    

                                function pulsarEliminarTitulo(){
                                    var s = tablaIdiomas.selectedIndex;
                                    if((s>=0)&&!tableName.ultimoTable){
                                        if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarDesc")%>')) {
                                            var listaOriginal = new Array();
                                            var lista = new Array();
                                            for(i=0;i<s;i++){
                                                listaOriginal[i]=titulosOriginal[i];
                                                lista[i]=listaTitulos[i];
                                            }
                                            for(i=s+1;i<(titulosOriginal.length);i++){
                                                listaOriginal[i-1]=titulosOriginal[i];
                                                lista[i-1]=listaTitulos[i];
                                            }

                                            titulosOriginal = new Array();
                                            listaTitulos = new Array();
                                            titulosOriginal = listaOriginal;
                                            listaTitulos = lista;
                                            tablaIdiomas.lineas = listaTitulos;
                                            refresca(tablaIdiomas);
                                            comboIdioma.selectItem(-1);
                                            limpiar(['tituloApl']);
                                        }
                                    }
                                }



                                function pulsarNuevoTitulo(){
                                    if (validarCamposRejillaTitulos()){
                                        var existe = false;
                                        var cod = document.forms[0].codIdioma.value;
                                        var idesc=titulosOriginal.length;
                                        for(i=0;i<idesc;i++){
                                            if(titulosOriginal[i][0] == cod)
                                                existe = true;
                                        }
                                        if (existe) jsp_alerta("A",'<%=descriptor.getDescripcion("msjDescExiste")%>')
                                            else {
                                                titulosOriginal[idesc] = [document.forms[0].codIdioma.value,document.forms[0].descIdioma.value,document.forms[0].tituloApl.value];
                                                listaTitulos[idesc]=[document.forms[0].descIdioma.value,document.forms[0].tituloApl.value];
                                                tablaIdiomas.lineas = listaTitulos;
                                                refresca(tablaIdiomas);
                                                comboIdioma.selectItem(-1);
                                                limpiar(['tituloApl']);
                                            }		
                                        } else  jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>')
                                        }


                                        function pulsarModificarTitulo(){
                                            var s = tablaIdiomas.selectedIndex;
                                            if((s>=0)&&!tableName.ultimoTable){
                                                var yaExiste = 0;
                                                var idioma = document.forms[0].codIdioma.value;
                                                if (validarCamposRejillaTitulos()){
                                                    for(var l=0; l < titulosOriginal.length; l++){
                                                        if(l != s) {
                                                            if ((titulosOriginal[l][0]) == idioma) yaExiste = 1;
                                                        }
                                                    }

                                                    if(yaExiste == 0) {
                                                        titulosOriginal[s]=[document.forms[0].codIdioma.value,document.forms[0].descIdioma.value,document.forms[0].tituloApl.value];
                                                        listaTitulos[s]=[document.forms[0].descIdioma.value,document.forms[0].tituloApl.value];
                                                        tablaIdiomas.lineas=listaTitulos;
                                                        refresca(tablaIdiomas);
                                                        comboIdioma.selectItem(-1);
                                                        limpiar(['tituloApl']);
                                                    } else {
                                                    jsp_alerta('A','<%=descriptor.getDescripcion("msjDescExiste")%>');
                                                    }
                                                } else {
                                                jsp_alerta('A','<%=descriptor.getDescripcion("msjObligTodos")%>');
                                                }
                                            } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                                            }


                                            function validarCamposRejillaTitulos(){
                                                var cod = document.forms[0].codIdioma.value;
                                                var desc = document.forms[0].descIdioma.value;
                                                var tit = document.forms[0].tituloApl.value;
                                                if((cod!="")&&(desc!="")&&(tit!=""))
                                                    return true;
                                                return false;
                                            }
        </script>
    </head>
    
    <body class="bandaBody" onload="javascript:{pleaseWait('off');
        inicializar();}">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form name="formulario" method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="identificador" id="identificador">
    <input  type="hidden"  name="codigoAntiguo" value="">
    <input  type="hidden" name="titulos_idiomas" value="">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_MantApl")%></div>
    <div class="contenidoPantalla">
        <div class="tab-pane" id="tab-pane-1">
            <script type="text/javascript">
                tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
            </script>

            <!-- Inicialización de las pestañas -->                       
            <div class="tab-page" id="tabPage1">
                <h2 class="tab"><%=descriptor.getDescripcion("etiq_admin_apl1")%></h2>
                <script type="text/javascript">
                    tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );
                </script>
                <TABLE style="width:99%">
                    <tr>
                        <td id="tabla" style="padding-bottom: 4px"></td>                           
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" border="0" cellspacing="0" cellpadding="1">
                                <tr>
                                    <td>
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td style="width: 18%" class="etiqueta"><%= descriptor.getDescripcion("gEtiq_Codigo")%>:</td>
                                                <td style="width: 8%">
                                                    <input type="text" class="inputTextoObligatorio" name="codigo" size="5" maxlength="3" onkeyup="return SoloDigitosNumericos(this);">
                                                </td>
                                                <td style="width: 10%" class="etiqueta"><%= descriptor.getDescripcion("gEtiq_Descrip")%>:</td>
                                                <td>
                                                    <input name="descripcion" type="text" class="inputTextoObligatorio"size="89" maxlength="80" style="width:500px" onblur="return xAMayusculas(this);">
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td style="width: 18%" class="etiqueta"><%= descriptor.getDescripcion("gEtiq_Version")%>:</td>
                                                <td>
                                                    <input name="version" type="text" class="inputTexto" size="40" maxlength="30" onblur="return xAMayusculas(this);">
                                                </td>
                                            </tr>
                                        </table>		
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr><td style="width: 18%" class="etiqueta">
                                                    <%= descriptor.getDescripcion("gEtiq_InicioApl")%>:
                                                </td>
                                                <td>
                                                    <input name="ejecutable" type="text" class="inputTextoObligatorioSinMayusculas" size="116" maxlength="255" style="width:675px">
                                            </td></tr>
                                        </table>								
                                    </td>
                                </tr>
                                <tr>
                                    <td>

                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td style="width: 18%" class="etiqueta"><%= descriptor.getDescripcion("gEtiq_IconoApl")%>:</td>
                                                <td><input name="icono" type="text" class="inputTextoObligatorioSinMayusculas" size="116"
                                                           maxlength="255" style="width:675px"></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td>									
                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td style="width: 18%" class="etiqueta"><%= descriptor.getDescripcion("gEtiq_Diccionario")%>:</td>
                                                <td style="width: 32%"><input name="diccionario" type="text" class="inputTexto" size="40"
                                                                              maxlength="255" style="width:260px" onblur="return xAMayusculas(this);"></td>
                                                <td style="width: 16%" class="etiqueta"><%= descriptor.getDescripcion("gEtiq_UbicList")%>:</td>
                                                <td><input name="informes" type="text" class="inputTexto" size="40"
                                                           maxlength="255" style="width:260px" onblur="return xAMayusculas(this);"></td>
                                            </tr>
                                        </table>

                                    </td>
                                </tr>
                                <tr style="padding-top: 20px; padding-bottom: 5px">
                                    <td id="tablaI"></td>
                                </tr>
                                <tr> 
                                    <td>
                                        <input type="text" name="codIdioma" id="codIdioma" size="2" class="inputTextoObligatorioSinMayusculas" style="width:3%" value="">
                                        <input type="text" name="descIdioma"  id="descIdioma" size="14" class="inputTextoObligatorioSinMayusculas" style="width:10%" readonly="true" value="">
                                        <A href="" id="anchorIdioma" name="anchorIdioma" style="text-decoration:none;"> 
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdioma"
                                                 name="botonIdioma" style="cursor:hand;"></span>
                                        </A>
                                        <input name="tituloApl" type="text" size="120" 
                                               class="inputTextoObligatorioSinMayusculas" maxlength="32" 
                                               style="margin-left:10px;width:80%">
                                    </td>
                                </tr>			
                                <tr style="padding-top: 10px; padding-bottom: 10px"> 
                                    <td>
                                        <table width="99%" border="0" cellpadding="0" cellspacing="0">
                                            <tr align="right"><td>
                                                    <input name="botonAltaTitulo" type="button"  class="botonGeneral" id="botonAltaTitulo" 
                                                           value='<%=descriptor.getDescripcion("gbAnadirTit")%>'
                                                           onClick="pulsarNuevoTitulo();" accesskey="A">
                                                    <input name="botonModificarTitulo" type="button" class="botonGeneral" id="botonModificarTitulo"
                                                           value='<%=descriptor.getDescripcion("gbModificarTit")%>'
                                                           onClick="pulsarModificarTitulo();" accesskey="E">																											
                                                    <input name="botonEliminarTitulo" type="button" class="botonGeneral" id="botonEliminarTitulo"
                                                           value='<%=descriptor.getDescripcion("gbEliminarTit")%>'
                                                           onClick="pulsarEliminarTitulo();" accesskey="E">										
                                        </td></tr></table>																	
                                    </td>
                                </tr>                                                                                                
                            </TABLE>
                        </TD>
                    </TR>
                </TABLE>
            </DIV><!--FIN CAPA 1-->
            <!--CAPA 2: CHECKS DE CONFIGURACIÓN DE LA APLICACIÓN ------------------------------ -->
            <div class="tab-page" id="tabPage2">
                <h2 class="tab"><%=descriptor.getDescripcion("etiq_admin_apl2")%></h2>
                <script type="text/javascript">
                    tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );
                </script>
                <TABLE style="width:99%">
                    <tr>
                        <td>
                            <table width="100%" class="tablaPeque" rules="none" border="0" cellspacing="0" cellpadding="2">
                                <tr>
                                    <td class="subtituloFondoGris" style="height:12px"><%= descriptor.getDescripcion("gEtiq_TipSegPro")%></td>
                                </tr>
                                <tr>
                                    <td class="etiqueta"><label><input type="radio" name="Seguridad" value="0"><%= descriptor.getDescripcion("gEtiq_PorTipPro")%>
                                    </label></td>
                                </tr>
                                <tr>
                                    <td class="etiqueta"><label><input name="Seguridad" type="radio" value="1" checked><%= descriptor.getDescripcion("gEtiq_PorDesExpl")%>
                                    </label></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" class="tablaPeque" rules="none" border="0" cellspacing="0" cellpadding="2">
                                <tr>
                                    <td class="subtituloFondoGris" style="height:12px"><%= descriptor.getDescripcion("gEtiq_SinInfSeg")%></td>
                                </tr>
                                <tr>
                                    <td class="etiqueta"><label><input type="radio" name="Acceso" value="0"><%= descriptor.getDescripcion("gEtiq_PerAccesPro")%>
                                    </label></td>
                                </tr>
                                <tr>
                                    <td class="etiqueta"><label><input name="Acceso" type="radio" value="1" checked><%= descriptor.getDescripcion("gEtiq_RestAccesPro")%>
                                    </label></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" border="0" cellspacing="2" cellpadding="0">
                                <tr>
                                    <td style="width: 50%">
                                        <table width="100%" class="tablaPeque" rules="none" border="0" cellspacing="0" cellpadding="2">
                                            <tr>
                                                <td class="subtituloFondoGris" style="height:12px"><%= descriptor.getDescripcion("gEtiq_TrabEntida")%></td>
                                            </tr>
                                            <tr>
                                                <td class="etiqueta"><label><input name="Entidades" type="radio" value="1"><%= descriptor.getDescripcion("gEtiq_Si")%>
                                                </label></td>
                                            </tr>
                                            <tr>
                                                <td class="etiqueta"><label><input type="radio" name="Entidades" value="0" checked><%= descriptor.getDescripcion("gEtiq_No")%>
                                                </label></td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td style="width: 50%">
                                        <table width="100%" class="tablaPeque" rules="none" border="0" cellspacing="0" cellpadding="2">
                                            <tr>
                                                <td class="subtituloFondoGris" style="height:12px"><%= descriptor.getDescripcion("gEtiq_TrabEjer")%></td>
                                            </tr>
                                            <tr>
                                                <td class="etiqueta"><label><input name="Ejercicios" type="radio" value="1"><%= descriptor.getDescripcion("gEtiq_Si")%>
                                                </label></td>
                                            </tr>
                                            <tr>
                                                <td class="etiqueta"><label><input type="radio" name="Ejercicios" value="0" checked><%= descriptor.getDescripcion("gEtiq_No")%>
                                                </label></td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAlta")%>" name="botonAlta" onClick="pulsarAlta();" accesskey="A">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbModificar")%>" name="botonModificar" onClick="pulsarModificar();" accesskey="M">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="botonBorrar" onClick="pulsarEliminar();" accesskey="E">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="botonSalir" onClick="pulsarSalir();" accesskey="S">
        </div>               
    </div>               
</form>
        
        
        <script type="text/javascript">
            
            var comboIdioma = new Combo("Idioma");
            
            var tablaAplicaciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            tablaAplicaciones.addColumna('120','center','<%= descriptor.getDescripcion("gEtiq_Codigo")%>');
                tablaAplicaciones.addColumna('780',null,'<%= descriptor.getDescripcion("gEtiq_Descrip")%>');
                    tablaAplicaciones.displayCabecera=true;
                    
                    function refresca(tabla){
                        tabla.displayTabla();
                    }
                    
                    function rellenarDatos(tableName,rowID){
                        comboIdioma.activate();
                        comboIdioma.selectItem(-1);
                        limpiar(['tituloApl']);
                        if(tableName==tablaAplicaciones){
                            var i = rowID;
                            limpiarCamposRejilla();
                            if((i>=0)&&!tableName.ultimoTable){
                                var vectorDatos = [datosAplicaciones[i][0],datosAplicaciones[i][1],datosAplicaciones[i][2],
                                    datosAplicaciones[i][10],datosAplicaciones[i][5],datosAplicaciones[i][6],datosAplicaciones[i][7]];
                                rellenar(vectorDatos,vectorCamposRejilla);
                                activaSeguridad(datosAplicaciones[i][3]);
                                activaAcceso(datosAplicaciones[i][4]);
                                activaEntidades(datosAplicaciones[i][8]);
                                activaEjercicios(datosAplicaciones[i][9]);
                                document.forms[0].codigoAntiguo.value = lista[i][0];
                                titulosOriginal = datosAplicaciones[i][11];
                                listaTitulos = new Array();
                                for (var j=0; j<titulosOriginal.length; j++)
                                    listaTitulos[j] = [titulosOriginal[j][1],titulosOriginal[j][2]];
                                cargarTablaIdiomas(listaTitulos);
                            }
                        }
                        else if(tablaIdiomas == tableName){
                            var i = rowID;
                            var apl =datosAplicaciones[tablaAplicaciones.selectedIndex][11];
                            if((i>=0)&&!tableName.ultimoTable){			
                                comboIdioma.buscaCodigo(apl[i][0]);
                                document.forms[0].tituloApl.value=apl[i][2];
                            }
                        }
                    } 
                    
                    
                    var tablaIdiomas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaI'));
                    tablaIdiomas.addColumna('150',null,'<%= descriptor.getDescripcion("gEtiq_Idioma")%>');
                        tablaIdiomas.addColumna('750',null,'<%= descriptor.getDescripcion("gEtiq_Titulo")%>');
                            tablaIdiomas.displayCabecera = true;  
                            tablaIdiomas.displayTabla();
                            
                            function activaSeguridad(valor){
                                var seguridad = document.forms[0].Seguridad;
                                if(valor=="0")
                                    seguridad[0].checked = true;
                                else
                                    seguridad[1].checked = true;
                            }
                            
                            function activaAcceso(valor){
                                var acceso = document.forms[0].Acceso;
                                if(valor=="0")
                                    acceso[0].checked = true;
                                else
                                    acceso[1].checked = true;
                            }
                            
                            function activaEntidades(valor){
                                var entidades = document.forms[0].Entidades;
                                if(valor=="1")
                                    entidades[0].checked = true;
                                else
                                    entidades[1].checked = true;
                            }
                            
                            function activaEjercicios(valor){
                                var ejercicios = document.forms[0].Ejercicios;
                                if(valor=="1")
                                    ejercicios[0].checked = true;
                                else
                                    ejercicios[1].checked = true;
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
                                var teclaAuxiliar = "";
                                if(window.event){
                                    evento = window.event;
                                    teclaAuxiliar =evento.keyCode;
                                }else
                                    teclaAuxiliar =evento.which;

                                keyDel(evento);
                                if(teclaAuxiliar == 40){
                                    upDownTable(tablaAplicaciones,lista,teclaAuxiliar);
                                }
                                if(teclaAuxiliar == 38){
                                    upDownTable(tablaAplicaciones,lista,teclaAuxiliar);
                                }  
                                
                                if (evento.button == 1){
                                    if(capaVisible) ocultarLista();
                                }	
                                if (teclaAuxiliar == 13) pushEnterTable(tablaAplicaciones,lista);
                                if (teclaAuxiliar == 1){
                                    if (comboIdioma.base.style.visibility == "visible" && isClickOutCombo(comboIdioma,coordx,coordy)) setTimeout('comboIdioma.ocultar()',20);
                                }
                                if (teclaAuxiliar == 9){
                                    comboIdioma.ocultar();
                                }
                            }
                            
        </script>
        
    </body>
</html>
