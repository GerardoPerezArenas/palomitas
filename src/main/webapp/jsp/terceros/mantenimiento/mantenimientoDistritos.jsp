<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
        <title> Mantenimiento de Distritos INE </title>
        <!-- Estilos -->
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
        var listaDistritosOriginal = new Array();
        var listaDistritos = new Array();
        
        // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
        function recuperaDatosIniciales(){
    <%
            Vector listaProvincias = mantForm.getListaProvincias();
            Vector listaMunicipios = mantForm.getListaMunicipios();
            int lengthProvs = listaProvincias.size();
            int lengthMuns = listaMunicipios.size();
            int i = 0;
            String codProvincias = "";
            String descProvincias = "";
            for (i = 0; i < lengthProvs - 1; i++) {
                GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
                codProvincias += "\"" + (String) provincias.getAtributo("codigo") + "\",";
                descProvincias += "\"" + (String) provincias.getAtributo("descripcion") + "\",";
            }
            GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
            codProvincias += "\"" + (String) provincias.getAtributo("codigo") + "\"";
            descProvincias += "\"" + (String) provincias.getAtributo("descripcion") + "\"";
            String codMunicipios = "";
            String descMunicipios = "";
            for (i = 0; i < lengthMuns - 1; i++) {
                GeneralValueObject municipios = (GeneralValueObject) listaMunicipios.get(i);
                codMunicipios += "\"" + (String) municipios.getAtributo("codMunicipio") + "\",";
                descMunicipios += "\"" + (String) municipios.getAtributo("nombreOficial") + "\",";
            }
            GeneralValueObject municipios = (GeneralValueObject) listaMunicipios.get(i);
            codMunicipios += "\"" + (String) municipios.getAtributo("codMunicipio") + "\"";
            descMunicipios += "\"" + (String) municipios.getAtributo("nombreOficial") + "\"";
      %>
          codProvincias = [<%=codProvincias%>];
              descProvincias = [<%=descProvincias%>];
                  codMunicipios = [<%=codMunicipios%>];
                      descMunicipios = [<%=descMunicipios%>];
                          codMunicipiosDefecto = codMunicipios;
                          descMunicipiosDefecto = descMunicipios;
                      }
                      
                      function inicializar(){
                          window.focus();
                          recuperaDatosIniciales();
                          valoresPorDefecto();
                          pulsarCancelarBuscar();
                          if(ventana=="false"){
                              pleaseWait1("off",top.mainFrame);
                              
                                  
                              
                              pulsarBuscar();
                          }else{
                          pleaseWait1("off",top1.mainFrame1);
                          var parametros = self.parent.opener.xanelaAuxiliarArgs;
                          pulsarBuscar();
                      }
                  }
                  
                  // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
                  //var vectorCamposBusqueda = ["codProvincia","descProvincia","codMunicipio","descMunicipio"];
                  var vectorCamposRejilla = ["codDistrito","descDistrito","nombreLargo","idObjetoGrafico"];
                  //var vectorCombosBusqueda = ["comboProvincia","comboMunicipio"];
                  var vectorBotones = ["botonAlta","botonModificar","botonBorrar","botonLimpiar"];
                  
                  /*function habilitarCamposBusqueda(habilitar){
                  habilitarGeneralCombos(vectorCombosBusqueda,habilitar);
                  }*/
                  
                  function habilitarCamposRejilla(habilitar){
                      habilitarGeneralInputs(vectorCamposRejilla,habilitar);
                      habilitarGeneralInputs(vectorBotones,habilitar);
                  }
                  
                  function limpiarFormulario(){
                      //limpiarCamposBusqueda();
                      limpiarCamposRejilla();
                      tablaDistritos.lineas = new Array();
                      refresca(tablaDistritos);
                  }
                  
                  /*function limpiarCamposBusqueda(){
                  limpiar(vectorCamposBusqueda);
                  }*/
                  
                  function limpiarCamposRejilla(){
                      var vector = [document.forms[0].codDistrito];
                      habilitarGeneral(vector);
                      limpiar(vectorCamposRejilla);
                  }
                  
                  // FUNCIONES DE CARGA DE DATOS DINAMICA
                  function cargarListaMunicipios(){	
                      document.forms[0].opcion.value="cargarMunicipios";
                      document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Trameros.do";
                      document.forms[0].submit();
                  }
                  
                  function cargarListaDistritos(lista){
                      listaDistritos = lista;
                      tablaDistritos.lineas = lista;
                      refresca(tablaDistritos);
                  }
                  
                  function valoresPorDefecto(){
                      document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
                      document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
                      document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
                      document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
                      document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
                      codMunicipios = codMunicipiosDefecto;
                      descMunicipios = descMunicipiosDefecto;
                      //comboProvincia.addItems(codProvincias,descProvincias);
                      //comboMunicipio.addItems(codMunicipios,descMunicipios);
                  }
                  
                  // FUNCIONES DE VALIDACION DE CAMPOS
                  function validarCamposBusqueda(){
                      var pais = document.forms[0].codPais.value;
                      var provincia = document.forms[0].codProvincia.value;
                      var municipio = document.forms[0].codMunicipio.value;
                      if((pais!="")&&(provincia!="")&&(municipio!=""))
                          return true;
                      return false;
                  }
                  
                  function validarCamposRejilla(){
                      var codDistrito = document.forms[0].codDistrito.value;
                      if(codDistrito!="")
                          return true;
                      return false;
                  }
                  
                  function noEsta(indice){
                      var cod = document.forms[0].codDistrito.value;
                      for(i=0;(i<listaDistritos.length);i++){
                          if(i!=indice){
                              if((listaDistritos[i][0]) == cod)
                                  return false;
                          }
                      }
                      return true;
                  }
                  
                  // FUNCIONES DE PULSACION DE BOTONES
                  function pulsarBuscar(){
                      //var botonBuscar = ["botonBuscar"];
                      if(validarCamposBusqueda()){
                          document.forms[0].opcion.value="cargarDistritos";
                          document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Distritos.do";
                          document.forms[0].submit();
                          //habilitarGeneralInputs(botonBuscar,false);
                          //habilitarCamposBusqueda(false);
                          habilitarCamposRejilla(true);
                      }else
                      jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                      }
                      
                      function pulsarAlta(){
                          if(validarCamposRejilla()){
                              if(noEsta()){
                                  //habilitarCamposBusqueda(true);
                                  document.forms[0].opcion.value="alta";
                                  document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                  document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Distritos.do";
                                  document.forms[0].submit();
                                  //habilitarCamposBusqueda(false);
                                  limpiarCamposRejilla();
                              }else
                              jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                              }else
                              jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>")
                              }
                              
                              function pulsarModificar(){
                                  if(tablaDistritos.selectedIndex != -1) {
                                      if(validarCamposRejilla()){
                                          if(noEsta(tablaDistritos.selectedIndex)){
                                              //habilitarCamposBusqueda(true);
                                              var vector = [document.forms[0].codDistrito];
                                              habilitarGeneral(vector);
                                              document.forms[0].opcion.value="modificar";
                                              document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Distritos.do";
                                              document.forms[0].submit();
                                              //habilitarCamposBusqueda(true);
                                              deshabilitarGeneral(vector);
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
                                          if(tablaDistritos.selectedIndex != -1) {
                                              if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimDist")%>') ==1) {
                                                  //habilitarCamposBusqueda(true);
                                                  var vector = [document.forms[0].codDistrito];
                                                  habilitarGeneral(vector);
                                                  document.forms[0].opcion.value="eliminar";
                                                  document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                  document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Distritos.do";
                                                  document.forms[0].submit();
                                                  //habilitarCamposBusqueda(false);
                                                  habilitarGeneral(vector);
                                                  limpiarCamposRejilla();
                                                  if(tablaDistritos.selectedIndex != -1 ) {
                                                      tablaDistritos.selectLinea(tablaDistritos.selectedIndex);
                                                      tablaDistritos.selectedIndex = -1;
                                                  }
                                              }
                                          }else
                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
                                          }
                                          
                                          function noEliminarDist() {
                                              jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimDist")%>");
                                              }
                                              
                                              function pulsarCancelarBuscar(){
                                                  limpiarFormulario();
                                                  //var botonBuscar = ["botonBuscar"];
                                                  //habilitarGeneralInputs(botonBuscar,true);
                                                  //habilitarCamposBusqueda(true);
                                                  habilitarCamposRejilla(false);
                                                  valoresPorDefecto();
                                              }
                                              
                                              function pulsarLimpiar(){
                                                  limpiarCamposRejilla();
                                                  if(tablaDistritos.selectedIndex != -1 ) {
                                                      tablaDistritos.selectLinea(tablaDistritos.selectedIndex);
                                                      tablaDistritos.selectedIndex = -1;
                                                  }
                                              }
                                              
                                              function pulsarSalir(){
                                                  if(ventana=="false"){
                                                      document.forms[0].opcion.value="inicializarTerc";
                                                      document.forms[0].target="mainFrame";
                                                      document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
                                                      document.forms[0].submit();
                                                  }else{
                                                  var datosRetorno;
                                                  if(indice>-1)
                                                      datosRetorno = listaDistritosOriginal[indice];
                                                  self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
                                              }
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
    <input type="hidden" name="codDistritoAntiguo" value="">
    <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
    <input type="hidden" name="codProvincia" value="<%=ptVO.getProvincia()%>">
    <input type="hidden" name="descProvincia" value="<%=ptVO.getNomProvincia()%>">
    <input type="hidden" name="codMunicipio" value="<%=ptVO.getMunicipio()%>">
    <input type="hidden" name="descMunicipio" value="<%=ptVO.getNomMunicipio()%>">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantDistritos")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr>
                <td id="tablaDistritos"></td>
            </tr>
            <tr>
                <td> 
                    <input name="codDistrito" type="text" class="inputTextoObligatorio" id="numero" size=6 maxlength=2 
                           onkeyup = "return SoloDigitosNumericos(this);" style="width:65px">
                    <input name="descDistrito" type="text" class="inputTexto" size=47 maxlength=25
                           onkeyup="return xAMayusculas(this);" style="width:295px">
                    <input name="nombreLargo" type="text" class="inputTexto" size=47 maxlength=50
                           onkeyup="return xAMayusculas(this);" style="width:295px">
                    <input name="idObjetoGrafico" type="text" class="inputTexto" size=20 maxlength=14
                           onkeyup="return xAMayusculas(this);" style="width:160px">
                </td>
            </tr>                                                                                    
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral"  name="botonAlta" onClick="pulsarAlta();" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
            <input type="button" class="botonGeneral"  name="botonModificar" onClick="pulsarModificar();" accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
            <input type="button" class="botonGeneral" name="botonBorrar" onClick="pulsarBorrar();" accesskey="B" value="<%=descriptor.getDescripcion("gbEliminar")%>">
            <input type="button" class="botonGeneral"  name="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
            <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
        </div>                         			            
    </div>                         			            
</form>
<script type="text/javascript">
            var indice;
            var tablaDistritos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaDistritos"));
            tablaDistritos.addColumna("60","center",'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
            tablaDistritos.addColumna("300","left",'<%=descriptor.getDescripcion("gEtiq_Descrip")%>');
            tablaDistritos.addColumna("300","left",'<%=descriptor.getDescripcion("gEtiq_NomLargo")%>');
            tablaDistritos.addColumna("150","left",'<%=descriptor.getDescripcion("gEtiq_IdObjGrafico")%>');
            tablaDistritos.displayCabecera=true;
            tablaDistritos.displayTabla();
                            
                            function refresca(tabla){
                                tabla.displayTabla();
                            }
                            
                            function rellenarDatos(tableName,rowID){
                                if(tablaDistritos==tableName){
                                    var i=rowID;
                                    indice = rowID;
                                    limpiarCamposRejilla();
                                    if(i>=0){
                                        var vector = [document.forms[0].codDistrito];
                                        deshabilitarGeneral(vector);
                                        var vectorDatosRejilla = [listaDistritosOriginal[i][3],listaDistritosOriginal[i][4],
                                            listaDistritosOriginal[i][5],listaDistritosOriginal[i][6]];
                                        rellenar(vectorDatosRejilla,vectorCamposRejilla);
                                        document.forms[0].codDistritoAntiguo.value = listaDistritosOriginal[i][3];
                                    }
                                }
                            } 
                            
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
                                    upDownTable(tablaDistritos,listaDistritos,teclaAuxiliar);
                                }
                                if(teclaAuxiliar == 38){
                                    upDownTable(tablaDistritos,listaDistritos,teclaAuxiliar);
                                }  
                            }
                            
                            // COMBOS
                            /*var comboProvincia = new Combo("Provincia");
                            var comboMunicipio = new Combo("Municipio");
                            
                            var auxCombo = "comboMunicipio";
                            
                            comboProvincia.change = 
                            function() { 
                            auxCombo="comboMunicipio"; 
                            limpiar(["codMunicipio","descMunicipio"]);
                            if(comboProvincia.des.value.length!=0){
                            cargarListaMunicipios();
                            }else{
                            comboMunicipio.addItems([],[]);
                            }		
                            } 
                            
                            function cargarComboBox(cod, des){
                            eval(auxCombo+".addItems(cod,des)");
                            }*/
                            
        </script>
    </body>
</html>
