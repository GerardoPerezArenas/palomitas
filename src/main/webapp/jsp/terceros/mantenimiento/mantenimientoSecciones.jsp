<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title> Mantenimiento de Distritos INE </title>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            ParametrosTerceroValueObject ptVO = null;
            int idioma = 1;
            int apl = 3;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                ptVO = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>" type="text/css">
        <script type="text/javascript">
            // VARIABLES GLOBALES
    <%
            MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
    %>  
        var ventana = "<%=mantForm.getVentana()%>";
        var codProvincias = new Array();
        var descProvincias = new Array();
        var codMunicipios = new Array();
        var descMunicipios = new Array();
        var codMunicipiosDefecto = new Array();
        var descMunicipiosDefecto = new Array();
        var codDistritos = new Array();
        var descDistritos = new Array();
        var codDistritosDefecto = new Array();
        var descDistritosDefecto = new Array();
        var listaSeccionesOriginal = new Array();
        var listaSecciones = new Array();
        
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
        function recuperaDatosIniciales(){
    <%
            Vector listaProvincias = mantForm.getListaProvincias();
            Vector listaMunicipios = mantForm.getListaMunicipios();
            Vector listaDistritos = mantForm.getListaDistritos();
            int lengthDis = listaDistritos.size();
            int lengthProvs = listaProvincias.size();
            int lengthMuns = listaMunicipios.size();
            int i = 0;
            // Provincias
            String codProvincias = "";
            String descProvincias = "";
            for (i = 0; i < lengthProvs - 1; i++) {
                GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
                codProvincias += "\"" + provincias.getAtributo("codigo") + "\",";
                descProvincias += "\"" + provincias.getAtributo("descripcion") + "\",";
            }
            if (lengthProvs > 0) {
                GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
                codProvincias += "\"" + provincias.getAtributo("codigo") + "\"";
                descProvincias += "\"" + provincias.getAtributo("descripcion") + "\"";
            }
            // Municipios
            String codMunicipios = "";
            String descMunicipios = "";
            for (i = 0; i < lengthMuns - 1; i++) {
                GeneralValueObject municipios = (GeneralValueObject) listaMunicipios.get(i);
                codMunicipios += "\"" + municipios.getAtributo("codMunicipio") + "\",";
                descMunicipios += "\"" + municipios.getAtributo("nombreOficial") + "\",";
            }
            if (lengthMuns > 0) {
                GeneralValueObject municipios = (GeneralValueObject) listaMunicipios.get(i);
                codMunicipios += "\"" + municipios.getAtributo("codMunicipio") + "\"";
                descMunicipios += "\"" + municipios.getAtributo("nombreOficial") + "\"";
            }
            // Distritos
            String codDistritos = "";
            String descDistritos = "";
            for (i = 0; i < lengthDis - 1; i++) {
                GeneralValueObject distritos = (GeneralValueObject) listaDistritos.get(i);
                codDistritos += "\"" + distritos.getAtributo("codDistrito") + "\",";
                descDistritos += "\"" + distritos.getAtributo("descDistrito") + "\",";
            }
            if (lengthDis > 0) {
                GeneralValueObject distritos = (GeneralValueObject) listaDistritos.get(i);
                codDistritos += "\"" + distritos.getAtributo("codDistrito") + "\"";
                descDistritos += "\"" + distritos.getAtributo("descDistrito") + "\"";
            }
      %>
          codProvincias = [<%=codProvincias%>];
              descProvincias = [<%=descProvincias%>];
                  codMunicipios = [<%=codMunicipios%>];
                      descMunicipios = [<%=descMunicipios%>];
                          codMunicipiosDefecto = codMunicipios;
                          descMunicipiosDefecto = descMunicipios;
                          codDistritos = [<%=codDistritos%>];
                              descDistritos = [<%=descDistritos%>];
                                  codDistritosDefecto = codDistritos;
                                  descDistritosDefecto = descDistritos;
                              }
                              
                              function inicializar(){
                                  recuperaDatosIniciales();
                                  valoresPorDefecto();
                                  pulsarCancelarBuscar();
                                  pleaseWait1("off",top.mainFrame);
                                  
                                      
                                  
                              }
                              
                              // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
                              var vectorCamposBusqueda = ["codDistrito","descDistrito"];
                              var vectorCamposRejilla = ["codSeccion","letraSeccion","descSeccion","nombreLargo","idObjetoGrafico",
                                  "contadorHojas"];
                              var vectorCombosBusqueda = ["comboDistrito"];
                              var vectorBotones = ["botonAlta","botonModificar","botonBorrar","botonLimpiar"];
                              
                              function habilitarCamposBusqueda(habilitar){
                                  habilitarGeneralCombos(vectorCombosBusqueda,habilitar);
                              }
                              
                              function habilitarCamposRejilla(habilitar){
                                  habilitarGeneralInputs(vectorCamposRejilla,habilitar);
                                  habilitarGeneralInputs(vectorBotones,habilitar);
                              }
                              
                              function limpiarFormulario(){
                                  limpiarCamposBusqueda();
                                  limpiarCamposRejilla();
                                  tablaSecciones.lineas = new Array();
                                  refresca(tablaSecciones);
                              }
                              
                              function limpiarCamposBusqueda(){
                                  limpiar(vectorCamposBusqueda);
                              }
                              
                              function limpiarCamposRejilla(){
                                  var vector = [document.forms[0].codSeccion,document.forms[0].letraSeccion];
                                  habilitarGeneral(vector);
                                  limpiar(vectorCamposRejilla);
                              }
                              
                              // FUNCIONES DE CARGA DE DATOS DINAMICA
                              function cargarListaMunicipios(){	
                                  document.forms[0].opcion.value="cargarMunicipios";
                                  document.forms[0].target="oculto";
                                  document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Secciones.do";
                                  document.forms[0].submit();
                              }
                              
                              function cargarListaDistritos(){
                                  document.forms[0].opcion.value="cargarDistritos";
                                  document.forms[0].target="oculto";
                                  document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Secciones.do";
                                  document.forms[0].submit();
                              }
                              
                              function cargarListaSecciones(lista){
                                  listaSecciones = lista;
                                  tablaSecciones.lineas = lista;
                                  refresca(tablaSecciones);
                              }
                              
                              function valoresPorDefecto(){
                                  document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
                                  document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
                                  document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
                                  document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
                                  document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
                                  codMunicipios = codMunicipiosDefecto;
                                  descMunicipios = descMunicipiosDefecto;
                                  codDistritos = codDistritosDefecto;
                                  descDistritos = descDistritosDefecto;
                                  //comboProvincia.addItems(codProvincias,descProvincias);
                                  //comboMunicipio.addItems(codMunicipios,descMunicipios);
                                  comboDistrito.addItems(codDistritos,descDistritos);
                              }
                              
                              function rellenarCamposBusqueda(datos){
                                  rellenar(datos,vectorCamposBusqueda);
                              }
                              
                              
                              // FUNCIONES DE VALIDACION DE CAMPOS
                              function validarCamposBusqueda(){
                                  var pais = document.forms[0].codPais.value;
                                  var provincia = document.forms[0].codProvincia.value;
                                  var municipio = document.forms[0].codMunicipio.value;
                                  var codDistrito = document.forms[0].codDistrito.value;
                                  if((pais!="")&&(provincia!="")&&(municipio!="")&&(codDistrito!=""))
                                      return true;
                                  return false;
                              }
                              
                              function validarCamposRejilla(){
                                  var codSeccion = document.forms[0].codSeccion.value;
                                  //var letraSeccion = document.forms[0].letraSeccion.value;
                                  if((codSeccion!="")) {
                                      document.forms[0].letraSeccion.value = " ";
                                      return true;
                                  }
                                  return false;
                              }
                              
                              function noEsta(indice){
                                  var cod = document.forms[0].codSeccion.value;
                                  var letra = document.forms[0].letraSeccion.value;
                                  for(i=0;(i<listaSecciones.length);i++){
                                      if(i!=indice){
                                          if(((listaSecciones[i][0]) == cod)&&((listaSecciones[i][1]) == letra))
                                              return false;
                                      }
                                  }
                                  return true;
                              }
                              
                              // FUNCIONES DE PULSACION DE BOTONES
                              function pulsarBuscar(){
                                  var botonBuscar = ["botonBuscar"];
                                  if(validarCamposBusqueda()){
                                      document.forms[0].opcion.value="cargarSecciones";
                                      document.forms[0].target="oculto";
                                      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Secciones.do";
                                      document.forms[0].submit();
                                      habilitarGeneralInputs(botonBuscar,false);
                                      habilitarCamposBusqueda(false);
                                      habilitarCamposRejilla(true);
                                  }else
                                  jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                                  }
                                  
                                  function pulsarAlta(){
                                      if(validarCamposRejilla()){
                                          if(noEsta()){
                                              habilitarCamposBusqueda(true);
                                              document.forms[0].contadorHojas.value="0";
                                              document.forms[0].opcion.value="alta";
                                              document.forms[0].target="oculto";
                                              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Secciones.do";
                                              document.forms[0].submit();
                                              habilitarCamposBusqueda(false);
                                              limpiarCamposRejilla();
                                          }else
                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                          }else
                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                                          }
                                          
                                          function pulsarModificar(){
                                              if(tablaSecciones.selectedIndex != -1) {
                                                  if(validarCamposRejilla()){
                                                      if(noEsta(tablaSecciones.selectedIndex)){
                                                          habilitarCamposBusqueda(true);
                                                          var vector = [document.forms[0].codSeccion,document.forms[0].letraSeccion];
                                                          habilitarGeneral(vector);
                                                          document.forms[0].opcion.value="modificar";
                                                          document.forms[0].target="oculto";
                                                          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Secciones.do";
                                                          document.forms[0].submit();
                                                          deshabilitarGeneral(vector);
                                                          habilitarCamposBusqueda(true);
                                                          limpiarCamposRejilla();
                                                      }else
                                                      jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                                      }else
                                                      jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                      } else {
                                                      jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                                                      }
                                                  }
                                                  
                                                  function pulsarBorrar(){
                                                      if(tablaSecciones.selectedIndex != -1) {
                                                          if(validarCamposBusqueda()){
                                                              if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimSec")%>') ==1) {
                                                                  habilitarCamposBusqueda(true);
                                                                  var vector = [document.forms[0].codSeccion,document.forms[0].letraSeccion];
                                                                  habilitarGeneral(vector);
                                                                  document.forms[0].opcion.value="eliminar";
                                                                  document.forms[0].target="oculto";
                                                                  document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Secciones.do";
                                                                  document.forms[0].submit();
                                                                  deshabilitarGeneral(vector);
                                                                  habilitarCamposBusqueda(false);
                                                                  limpiarCamposRejilla();
                                                                  if(tablaSecciones.selectedIndex != -1 ) {
                                                                      tablaSecciones.selectLinea(tablaSecciones.selectedIndex);
                                                                      tablaSecciones.selectedIndex = -1;
                                                                  }
                                                              }
                                                          }else
                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                          } else {
                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                                                          }
                                                      }
                                                      
                                                      function noEliminarSecc() {
                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimVial")%>");
                                                          }
                                                          
                                                          function pulsarCancelarBuscar(){
                                                              limpiarFormulario();
                                                              var botonBuscar = ["botonBuscar"];
                                                              habilitarGeneralInputs(botonBuscar,true);
                                                              habilitarCamposBusqueda(true);
                                                              habilitarCamposRejilla(false);
                                                              valoresPorDefecto();
                                                          }
                                                          
                                                          function pulsarLimpiar(){
                                                              limpiarCamposRejilla();
                                                              if(tablaSecciones.selectedIndex != -1 ) {
                                                                  tablaSecciones.selectLinea(tablaSecciones.selectedIndex);
                                                                  tablaSecciones.selectedIndex = -1;
                                                              }
                                                          }
                                                          
                                                          function pulsarSalir(){
                                                              document.forms[0].opcion.value="inicializarTerc";
                                                              document.forms[0].target="mainFrame";
                                                              document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                              document.forms[0].submit();
                                                          }
        </script>
    </head>
    
    <body class="bandaBody" onLoad="inicializar();">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form action="" method="get" name="formulario" target="_self">
    <input type="hidden" name="opcion">
    <input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
    <input type="hidden" name="codSeccionAntiguo" value="">
    <input type="hidden" name="letraSeccionAntigua" value="">
    <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
    <input type="hidden" name="codProvincia" value="<%=ptVO.getProvincia()%>">
    <input type="hidden" name="descProvincia" value="<%=ptVO.getNomProvincia()%>">
    <input type="hidden" name="codMunicipio" value="<%=ptVO.getMunicipio()%>">
    <input type="hidden" name="descMunicipio" value="<%=ptVO.getNomMunicipio()%>">


    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantSecciones")%></div>
    <div class="contenidoPantalla">
    <table>
        <tr>
            <td>
                <table width="100%">
                    <tr>
                        <td style="width: 70%" align="left">
                            <table style="width:100%">
                                <tr>
                                    <td class="etiqueta" style="width: 20%"><%=descriptor.getDescripcion("gEtiq_Distrito")%>:</td>
                                    <td>
                                        <input class="inputTextoObligatorio" type="text" id="codDistrito" 
                                               name="codDistrito" size="3"> 
                                        <input id="descDistrito" name="descDistrito" type="text" 
                                               class="inputTextoObligatorio"  
                                               style="width:150" readonly>
                                        <a id="anchorDistrito" name="anchorDistrito" href="">
                                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonDistrito"
                                                 name="botonDistrito" 
                                                 style="cursor:hand;"></span>
                                        </a>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td style="width: 30%" align="right"> 
                            <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                                   value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                   onClick="pulsarBuscar();" accesskey="B">
                            <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                                   value="<%=descriptor.getDescripcion("gbCancelar")%>"
                                   onClick="pulsarCancelarBuscar();" accesskey="C">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td id="tablaSecciones"></td>
        </tr>
        <tr>
            <td> 
                <table width="100%" border="0" cellpadding="0" cellspacing="2" align="center">
                    <tr>
                        <td style="width: 60px" align="center">
                            <input name="codSeccion" type="text" class="inputTextoObligatorio"
                                   size=3 maxlength=3 onkeyup = "return SoloDigitosNumericos(this);">
                        </td>
                        <td style="width: 60px" align="center">
                            <input name="letraSeccion" type="text" class="inputTexto" size=2 
                                   maxlength=1 onkeyup="return xAMayusculas(this);">
                        </td>
                        <td style="width: 280px" align="center">
                            <input name="descSeccion" type="text" class="inputTexto" size=35 maxlength=25
                                   onkeyup="return xAMayusculas(this);">
                        </td>
                        <td style="width: 280px" align="center">
                            <input name="nombreLargo" type="text" class="inputTexto" size=35 maxlength=50
                                   onkeyup="return xAMayusculas(this);">
                        </td>
                        <td style="width: 150px" align="center">
                            <input name="idObjetoGrafico" type="text" class="inputTexto" size=20 maxlength=14
                                   onkeyup="return xAMayusculas(this);">
                        </td>
                        <td style="width: 80px" align="center">
                            <input name="contadorHojas" type="text" class="inputTexto" size=4 maxlength=4
                                   onkeyup="return SoloDigitosNumericos(this);">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral"  name="botonAlta"	onClick="pulsarAlta();" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
        <input type="button" class="botonGeneral"  name="botonModificar"	onClick="pulsarModificar();" accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
        <input type="button" class="botonGeneral" name="botonBorrar"	onClick="pulsarBorrar();" accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>">
        <input type="button" class="botonGeneral"  name="botonLimpiar"	onClick="pulsarLimpiar();" accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
        <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
    </div>                        
</div>                        
</form>
<script type="text/javascript">
            var indice;
            var tablaSecciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaSecciones"));
            tablaSecciones.addColumna("55","center","<%=descriptor.getDescripcion("gEtiq_Codigo")%>");
            tablaSecciones.addColumna("55","center","<%=descriptor.getDescripcion("gEtiq_Letra")%>");
            tablaSecciones.addColumna("270","center","<%=descriptor.getDescripcion("gEtiq_Descrip")%>");
            tablaSecciones.addColumna("270","center","<%=descriptor.getDescripcion("gEtiq_NomLargo")%>");
            tablaSecciones.addColumna("150","center","<%=descriptor.getDescripcion("gEtiq_IdObjGrafico")%>");
            tablaSecciones.addColumna("80","center","<%=descriptor.getDescripcion("gEtiq_Contador")%>");
            
            tablaSecciones.displayCabecera = true;
            tablaSecciones.displayTabla();
                                    
                                    function refresca(tabla){
                                        tabla.displayTabla();
                                    }
                                    
                                    function rellenarDatos(tableName,rowID){
                                        if(tablaSecciones==tableName){
                                            var i=rowID;
                                            indice = rowID;
                                            limpiarCamposRejilla();
                                            if((i>=0)&&!tableName.ultimoTable){
                                                var vector = [document.forms[0].codSeccion,document.forms[0].letraSeccion];
                                                deshabilitarGeneral(vector);
                                                var vectorDatosRejilla = [listaSeccionesOriginal[i][4],listaSeccionesOriginal[i][5],
                                                    listaSeccionesOriginal[i][6],listaSeccionesOriginal[i][7],listaSeccionesOriginal[i][8],
                                                    listaSeccionesOriginal[i][9]];
                                                rellenar(vectorDatosRejilla,vectorCamposRejilla);
                                                document.forms[0].codSeccionAntiguo.value = listaSeccionesOriginal[i][4];
                                                document.forms[0].letraSeccionAntigua.value = listaSeccionesOriginal[i][5];
                                            }
                                        }
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
                                    // FUNCION DE CONTROL DE TECLAS
                                    document.onmouseup = checkKeys; 
                                    
                                    function checkKeysLocal(evento,tecla){
                                        var teclaAuxiliar = "";
                                        if(window.event){
                                            evento         = window.event;
                                            teclaAuxiliar =  evento.keyCode;
                                        }else
                                            teclaAuxiliar =  evento.which;
                                        
                                        //** Esta funcion se debe implementar en cada JSP para particularizar  **//
                                        //** las acciones a realizar de las distintas combinaciones de teclas  **//
                                        keyDel(evento);
                                        if(teclaAuxiliar == 40){
                                            upDownTable(tablaSecciones,listaSecciones,teclaAuxiliar);
                                        }
                                        if(teclaAuxiliar == 38){
                                            upDownTable(tablaSecciones,listaSecciones,teclaAuxiliar);
                                        }
                                        if (teclaAuxiliar == 1){
                                            if (comboDistrito.base.style.visibility == "visible" && isClickOutCombo(comboDistrito,coordx,coordy)) setTimeout('comboDistrito.ocultar()',20);

                                        }
                                        if (teclaAuxiliar == 9){
                                            comboDistrito.ocultar();

                                        }
                                    }
                                    
                                    // COMBOS
                                    //var comboProvincia = new Combo("Provincia");
                                    //var comboMunicipio = new Combo("Municipio");
                                    var comboDistrito  = new Combo("Distrito");                                    
                                    var auxCombo = "comboMunicipio";
                                    
                                    /*comboProvincia.change = 
                                    function() { 
                                    auxCombo="comboMunicipio"; 
                                    limpiar(["codMunicipio","descMunicipio"]);
                                    if(comboProvincia.cod.value.length!=0){
                                    cargarListaMunicipios();
                                    }else{
                                    comboMunicipio.addItems([],[]);
                                    }		
                                    } 
                                    
                                    comboMunicipio.change = 
                                    function() { 
                                    auxCombo="comboDistrito"; 
                                    limpiar(["codDistrito","descDistrito"]);
                                    if(comboMunicipio.cod.value.length!=0){
                                    cargarListaDistritos();
                                    }else{
                                    comboDistrito.addItems([],[]);
                                    }		
                                    } */
                                    
                                    function cargarComboBox(cod, des){
                                        eval(auxCombo+".addItems(cod,des)");
                                    }
                                    
        </script>
    </body>
</html>
