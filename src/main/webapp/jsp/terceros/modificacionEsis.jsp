<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
  <title> Mantenimiento de Entidades Singulares </title>
  <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
  
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  ParametrosTerceroValueObject ptVO = null;
  int idioma=1;
  int apl=3;
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
    ptVO = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
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
  <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    <% 
      MantenimientosTercerosForm mantForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
    %>  
    var ventana = "<%=mantForm.getVentana()%>";
    //Vectores para albergar los datos de Provincias (estáticos) 
    var codProvincias = new Array();
    var descProvincias = new Array();
    //Vectores para albergar los datos de Municipios (Dinámicos en funcion de la provincia)
    var codMunicipios = new Array();
    var descMunicipios = new Array();
    //Vectores para albergar los datos de Entidades Colectivas (Dinámicos en funcion del municipio)
    var codEntColectivas = new Array();
    var descEntColectivas = new Array();
    var codINE = new Array();
    //Vectores para albergar los datos de las Entidades Singulares (Dinámicos)
    var listaEntSingularesOriginal = new Array();
    var listaEntSingulares = new Array();
    //Variables para albergar los valores antiguos de país, provincia y municipio para
    //no tener que volver a cargar de la BD cuando el valor de los campos no ha cambiado
    var paisOld = "";
    var provinciaOld = "";
    var municipioOld = "";
    //Variable para direccionamiento al frame
    var frame;
      
    /**************  FUNCIONES PARA LA CARGA DE LOS CALENDARIOS ***********************/
    function mostrarCalendario(img,campoFecha){
      var indice = document.getElementById(img).src.indexOf('fa-calendar');
      if (indice!=-1)
        showCalendar('forms[0]',campoFecha,null,null,null,'',img,'',null,null,null,
          null,null,null,null,'');
    }//de la funcion

    //Funcion para la verificación de un campo fecha
    function comprobarFecha(inputFecha){
      if(Trim(inputFecha.value)!=''){
        if(!ValidarFechaConFormato(document.forms[0],inputFecha)){
          jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
          inputFecha.focus();
          return false;
        }
      }
      return true;
    }//de la funcion

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
    function recuperaDatosIniciales()
    {
    <%	
      Vector listaProvincias = mantForm.getListaProvincias();
      Vector listaMunicipios = mantForm.getListaMunicipios();
      Vector listaEntColectivas = mantForm.getListaEcos();
      int lengthProvs = listaProvincias.size();
      int lengthMuns = listaMunicipios.size();
      int lengthEntColectivas = listaEntColectivas.size();
      int i = 0;
      String codProvincias="";
      String descProvincias="";
      for(i=0;i<lengthProvs-1;i++){
        GeneralValueObject provincias = (GeneralValueObject)listaProvincias.get(i);
        codProvincias+="\""+(String)provincias.getAtributo("codigo")+"\",";
        descProvincias+="\""+(String)provincias.getAtributo("descripcion")+"\",";
      }
      GeneralValueObject provincias = (GeneralValueObject)listaProvincias.get(i);
      codProvincias+="\""+(String)provincias.getAtributo("codigo")+"\"";
      descProvincias+="\""+(String)provincias.getAtributo("descripcion")+"\"";
      String codMunicipios="";
      String descMunicipios="";
      for(i=0;i<lengthMuns-1;i++){
        GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
        codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\",";
        descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\",";
      }
      GeneralValueObject municipios = (GeneralValueObject)listaMunicipios.get(i);
      codMunicipios+="\""+(String)municipios.getAtributo("codMunicipio")+"\"";
      descMunicipios+="\""+(String)municipios.getAtributo("nombreOficial")+"\"";
      String codEntColectivas="";
      String descEntColectivas="";
    String codINE = "";
      for(i=0;i<lengthEntColectivas-1;i++){
        GeneralValueObject ecos = (GeneralValueObject)listaEntColectivas.get(i);
        codEntColectivas+="\""+(String)ecos.getAtributo("codECO")+"\",";
        descEntColectivas+="\""+(String)ecos.getAtributo("descECO")+"\",";
    codINE += "\""+(String)ecos.getAtributo("codINE")+"\",";
      }
      GeneralValueObject ecos = (GeneralValueObject)listaEntColectivas.get(i);
      codEntColectivas+="\""+(String)ecos.getAtributo("codECO")+"\"";
      descEntColectivas+="\""+(String)ecos.getAtributo("descECO")+"\"";
    codINE+="\""+(String)ecos.getAtributo("codINE")+"\"";
      %>
      codProvincias = [<%=codProvincias%>];
      descProvincias = [<%=descProvincias%>];
      codMunicipios = [<%=codMunicipios%>];
      descMunicipios = [<%=descMunicipios%>];
      codEntColectivas = [<%=codEntColectivas%>];
      descEntColectivas = [<%=descEntColectivas%>];
    codINE = [<%=codINE%>];
      codMunicipiosDefecto = codMunicipios;
      descMunicipiosDefecto = descMunicipios;
    }//de la funcion


    function redireccionaFrame()
    {
      //var ventana = "<%=mantForm.getVentana()%>";
      frame=top.mainFrame;	
    }//de la funcion
    

    function inicializar()
    {
      recuperaDatosIniciales();
      comboEco.addItems(codEntColectivas,descEntColectivas);
      redireccionaFrame();
      pleaseWait1("off",frame);
      //Inicializamos los vectores para los campos, imágenes y enlaces de BUSQUEDA (provincia y municipio)
      //vectorCamposBusqueda1 = [document.forms[0].codProvincia,document.forms[0].descProvincia,
        //document.forms[0].codMunicipio,document.forms[0].descMunicipio];
      //vectorAnchorsBusqueda = [document.all.anchorProvincia,document.all.anchorMunicipio];
      //vectorBotonesBusqueda = [document.forms[0].botonProvincia,document.forms[0].botonMunicipio];
      //Vectores para los campos, imágenes y enlaces de la REJILLA
      vectorCamposRejilla1 = [document.forms[0].codEntidadSingular,document.forms[0].nombreOficial,
        document.forms[0].ine];
      //vectorAnchorsRejilla = [document.all.anchorEntColectivas];
      //vectorBotonesRejilla = [document.forms[0].botonEntColectivas];
      //Vector de Botones para la realización de las OPERACIONES con la Ent Singular
      vectorBotones = [document.forms[0].botonModificar,document.forms[0].botonLimpiar];
      //Inicializo el Formulario
      inicializarFormulario();
      if(ventana=="false"){
        
           
          
      }else{
        pleaseWait1("off",frame);
        var parametros = self.parent.opener.xanelaAuxiliarArgs;
        rellenarCamposBusqueda(parametros);
        pulsarBuscar(); 
      } 
    }//de la funcion
    
    function inicializarFormulario()
    {
      limpiarFormulario("todo");
      var botonBuscar = [document.forms[0].botonBuscar];
      habilitarGeneral(botonBuscar);
      deshabilitarGeneral(vectorCamposRejilla1);
      deshabilitarGeneral(vectorBotones);
      valoresPorDefecto();
    }//de la funcion


    // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
    var vectorCamposBusqueda = ["codProvincia","descProvincia","codMunicipio","descMunicipio"];
    var vectorCamposRejilla = ["codEntidadSingular","nombreOficial","ine"];
    var vectorBotones = new Array();
    var vectorCamposBusqueda1 = new Array();
    var vectorCamposRejilla1 = new Array();
    var vectorAnchorsBusqueda = new Array();
    var vectorBotonesBusqueda = new Array();
    var vectorAnchorsRejilla = new Array();
    var vectorBotonesRejilla = new Array();
    
    function limpiarFormulario(opcion_limpieza)
    {
      if (opcion_limpieza=="todo") //limpiamos todo el formulario
      {
        limpiar(vectorCamposBusqueda);
        limpiar(vectorCamposRejilla);
        tablaEntSingulares.lineas = new Array();
        refresca(tablaEntSingulares);
      }
      else if (opcion_limpieza=="tabla") //limpiamos sólo la tabla
      {
        tablaEntSingulares.lineas = new Array();
        refresca(tablaEntSingulares);
      }
      else if (opcion_limpieza=="campos_busqueda") //limpiamos solo los campos de busqueda
      {
        limpiar(vectorCamposBusqueda);			
      }
      else if (opcion_limpieza=="campos_rejilla") //limpiamos solo los campos de la rejilla
      {
        var vector = [document.forms[0].codEntidadSingular];
              habilitarGeneral(vector);
        limpiar(vectorCamposRejilla);
      }//del if
      else if (opcion_limpieza=="rejilla/tabla") //limpiamos los campos de la rejilla y la tabla
      {
        tablaEntSingulares.lineas = new Array();
        refresca(tablaEntSingulares);
        limpiar(vectorCamposRejilla);
      }
      document.forms[0].fechaOperacion.value = "";
    }//de la funcion
        
    
    function cargarListaEntSingulares(lista)
    {
      listaEntSingulares = lista;
      tablaEntSingulares.lineas = lista;
      refresca(tablaEntSingulares);
      //Oculto la imagen de carga de datos de la pantalla
      pleaseWait1("off",frame);
      
    }//de la funcion

    function valoresPorDefecto()
    {
      document.forms[0].codPais.value ="<%=ptVO.getPais()%>";
      document.forms[0].codProvincia.value = "<%=ptVO.getProvincia()%>";
      document.forms[0].descProvincia.value = "<%=ptVO.getNomProvincia()%>";
      document.forms[0].codMunicipio.value ="<%=ptVO.getMunicipio()%>";
      document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
      paisOld = document.forms[0].codPais.value;
      provinciaOld = document.forms[0].codProvincia.value;
      municipioOld = document.forms[0].codMunicipio.value;
    }//de la funcion

    function rellenarCamposBusqueda(datos)
    {
      rellenar(datos,vectorCamposBusqueda);
    }//de la funcion
    
    
    // FUNCIONES DE VALIDACION DE CAMPOS
    function validarCamposBusqueda()
    {
      var pais = document.forms[0].codPais.value;
      var provincia = document.forms[0].codProvincia.value;
      var municipio = document.forms[0].codMunicipio.value;
      if((pais!="")&&(provincia!="")&&(municipio!=""))
        return true;
      return false;
    }//de la funcion

    function validarCamposRejilla()
    {
      var codEntidadSingular = document.forms[0].codEntidadSingular.value;
      var nombreOficial = document.forms[0].nombreOficial.value;
      var codINE = document.forms[0].ine.value;
      var generarOpe = document.forms[0].generarOperaciones.checked;
      var fechaOpe = document.forms[0].fechaOperacion.value;
      if((codEntidadSingular!="" && nombreOficial !="" && codINE !="")&&((generarOpe && fechaOpe!="")||!generarOpe))
        return true;
      return false;
    }//de la funcion

    function noEsta(indice)
    {
      var cod = document.forms[0].ine.value;
      for(i=0;(i<listaEntSingulares.length);i++){
        if(i!=indice){
          if((listaEntSingulares[i][1]) == cod)
            return false;
        }
      }
      return true;
    }//de la funcion

    function filaSeleccionada(tabla)
    {
      var i = tabla.selectedIndex;
      if((i>=0)&&(!tabla.ultimoTable))
          return true;
      return false;
    }//de la funcion
    
    // FUNCIONES DE PULSACION DE BOTONES
    function pulsarBuscar()
    {
      //Muestro la imagen de carga de datos de la pantalla
      pleaseWait1("on",frame);
      var botonBuscar = [document.forms[0].botonBuscar];
      if(validarCamposBusqueda())
      {
          document.forms[0].opcion.value="cargarEsis";
          document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Esis.do";
          document.forms[0].submit();
      comboEco.deactivate();
          deshabilitarGeneral(botonBuscar);
          habilitarGeneral(vectorBotones);
          habilitarGeneral(vectorCamposRejilla1);
        limpiarFormulario("campos_rejilla");
      }
      else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
    }//de la funcion
    

    function haCambiadoValor(){
      var i= tablaEntSingulares.focusedIndex;
      var codINE = document.forms[0].ine.value;
      var descESI = document.forms[0].nombreOficial.value;
      if((codINE!=listaEntSingulares[i][1])||(descESI!=listaEntSingulares[i][3]))
        return true;
      return false;
    }

    function pulsarModificar()
    {
      if(filaSeleccionada(tablaEntSingulares)) //Si  ha seleccionado alguna fila
      {
        if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
        {
          if(haCambiadoValor()){
            if(noEsta(tablaEntSingulares.selectedIndex)) //Si el código está en la lista
            {
            var vector = [document.forms[0].codEntidadSingular,document.forms[0].codEco];
                    habilitarGeneral(vector);
              document.forms[0].situacion.value="";
              //habilitarGeneral(vectorCamposBusqueda1);
              document.forms[0].opcion.value="modificarEsiTerritorio";
              document.forms[0].target="oculto";
              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Esis.do";
              document.forms[0].submit();
              //deshabilitarGeneral(vectorCamposBusqueda1);
              habilitarGeneral(vector);
              limpiarFormulario("campos_rejilla");
            }else{
              jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
            }
          }else{
            jsp_alerta("A","No se ha producido ningún cambio"); 
          }
        }else
          jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
      }
      else
        jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
    }//de la funcion


    function pulsarCancelarBuscar()
    {
      limpiarFormulario("rejilla/tabla");
      var botonBuscar = [document.forms[0].botonBuscar];
      habilitarGeneral(botonBuscar);
      deshabilitarGeneral(vectorCamposRejilla1);
    comboEco.activate();
      deshabilitarGeneral(vectorBotones);
      document.forms[0].codEco.value = "";
      document.forms[0].descEco.value = "";
    }//de la funcion
    
    function pulsarLimpiar()
    {
      limpiarFormulario("campos_rejilla");
      if(tablaEntSingulares.selectedIndex != -1 ) {
          tablaEntSingulares.selectLinea(tablaEntSingulares.selectedIndex);
          tablaEntSingulares.selectedIndex = -1;
      }
    }//de la funcion
    
    function pulsarSalir()
    {
      if(ventana=="false"){
        document.forms[0].opcion.value="inicializarTerc";
            document.forms[0].target="mainFrame";
            document.forms[0].action="<%=request.getContextPath()%>/BusquedaTerceros.do";
            document.forms[0].submit();
      }else{
        var datosRetorno;
        if(indice>-1)
          datosRetorno = listaEntSingularesOriginal[indice];
        self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
      }
    }//de la funcion
  </script>
</head>

<body class="bandaBody" onLoad="inicializar();">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
  
<form action="" method="get" name="formulario" target="_self">
<input type="hidden" name="opcion">
<input type="hidden" name="tipo_select"   value="">
<input type="hidden" name="col_cod"   value="">
<input type="hidden" name="col_desc"   value="">
<input type="hidden" name="nom_tabla"   value="">
<input type="hidden" name="input_cod"   value="">
<input type="hidden" name="input_desc"   value="">
<input type="hidden" name="target1"  value="">
<input type="hidden" name="column_valor_where" value="">
<input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
<input type="hidden" name="codMunicipioAntiguo" value="">
<input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
<input type="hidden" name="codProvincia" value="<%=codProvincias%>">
<input type="hidden" name="descProvincia" value="<%=descProvincias%>">
<input type="hidden" name="codMunicipio" value="<%=codMunicipios%>">
<input type="hidden" name="descMunicipio" value="<%=descMunicipios%>">
<input type="hidden" name="situacion" value="">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantEsings")%></div>
<div class="contenidoPantalla">
    <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>
          <table border="0" width="100%" cellspacing="0px" cellpadding="0px">
            <tr>
              <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td>&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_ECO")%>:</td>
                    <td width="40%" align="center">
                        <input type="text" class="inputTexto" name="codEco" size="3">
                                <input type="text" class="inputTexto" name="descEco" style="width:200" readonly="true">
                            </td>
                            <td width="5%" valign="bottom">
                                <A href="" name="anchorEco" id="anchorEco">
                                  <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonEco" id="botonEco" style="cursor:hand;"></span>
                                </A>
                            </td>
                                <td width="35%" align="right">
                                    <input name="botonBuscar" type="button"  class="boton" id="botonBuscar" 
                        value="<%=descriptor.getDescripcion("gbBuscar")%>"
                        onClick="pulsarBuscar();" accesskey="B">
                      <input name="botonCancelar" type="button" class="boton" id="botonCancelar"
                        value="<%=descriptor.getDescripcion("gbCancelar")%>"
                        onClick="pulsarCancelarBuscar();" accesskey="C">
                              </td>
                  </tr>	
                  <tr>
                    <td>&nbsp;</td>
                  </tr>	
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td>
          <table width="50%" align="center" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td> 
                <table width="100%" align="center" rules="cols"  border="0" cellspacing="0" cellpadding="0" class="fondoCab">
                  <tr>
                    <td id="tablaEntSingulares"></td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td> 
                <table width="100%" rules="cols"  border="0" align="center"  cellpadding="0" cellspacing="0">
                  <tr>
                    <td width="50px" align="right"> 
                      <input name="codEntidadSingular" type="text" class="inputTextoObligatorio" size="3" maxlength="5" 
                        onKeyPress = "javascript:return SoloDigitos(event);">	
                    </td>
                    <td width="50px" align="center">
                      <input name="ine" type="text" class="inputTextoObligatorio" size="2" maxlength="2"
                        onKeyPress = "javascript:return SoloDigitos(event);">
                    </td>
                    <td width="210px" align="center"> 
                      <input name="nombreOficial" type="text" 
                        class="inputTextoObligatorio" size="32" maxlength="25"
                        onKeyPress = "javascript:PasaAMayusculas(event);">
                    </td>
                  </tr>
                </table>   
              </td>
            </tr>
            <tr>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>
                <table width="100%">
                  <tr>
                    <td class="etiqueta">
                      <input name="generarOperaciones" type="checkbox" value="SI" checked>
                      Generar operaciones
                    </td>
                    <td>
                      <input NAME="fechaOperacion" TYPE="text" class='inputTxtFecha' id="fechaOperacion"
                        onfocus = "this.select();"
                        onkeypress = "javascript:return soloCaracteresFecha(event);" size=12>   
                      <span class="fa fa-calendar" aria-hidden="true" name="calendario2" 
                        onClick="calClick();mostrarCalendario('calendario2','fechaOperacion');return false;" 
                        style="cursor:hand"></span> 
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    <div id="tablaBotones" class="botoneraPrincipal">
        <input type="button" class="botonGeneral"  name="botonModificar"	onClick="pulsarModificar();" 
          accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
        <input type="button" class="botonGeneral"  name="botonLimpiar"	onClick="pulsarLimpiar();" 
          accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
        <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" 
          accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>"> 
    </div>				
</div>				
</form>

<script language="JavaScript1.2">
  var indice;
  var tablaEntSingulares = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaEntSingulares"));
  tablaEntSingulares.addColumna("50","center",'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
  tablaEntSingulares.addColumna("50","left",'<%=descriptor.getDescripcion("gEtiq_INE")%>');
  tablaEntSingulares.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_DigitControl")%>');
  tablaEntSingulares.addColumna("210","left",'<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
  tablaEntSingulares.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_NomLargo")%>');
  tablaEntSingulares.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_ECO")%>');
  tablaEntSingulares.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_KmtsCapital")%>');
  tablaEntSingulares.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_Altitud")%>');
  tablaEntSingulares.addColumna("0","left",'<%=descriptor.getDescripcion("gEtiq_imagen")%>');
  
  tablaEntSingulares.displayCabecera=true;
  tablaEntSingulares.displayTabla();
  
  function refresca(tabla)
  {
    tabla.displayTabla();
  }//de la funcion
  
  //Busca en el vector de Descripciones la descripción asociada a un determinado 
  //código que se le pasa como parametro
  function buscaDescripcion(codigo,vectorCodigos,vectorDescripciones)
  {
    var codigo_encontrado=false;
    var indice_busqueda=0;
    var codigo_actual;
    var descripcion=null;
    while (codigo_encontrado==false && indice_busqueda<vectorCodigos.length)
    {
      codigo_actual=vectorCodigos[indice_busqueda];		
      if (codigo_actual==codigo)
        codigo_encontrado=true;
      else
        indice_busqueda++;
    }//del while
    if (codigo_encontrado==true && indice_busqueda<vectorDescripciones.length)
      descripcion=vectorDescripciones[indice_busqueda];			
    return(descripcion);
  }//de la funcion

  function rellenarDatos(tableName,rowID)
  {
    if(tablaEntSingulares==tableName)
    {
      if(filaSeleccionada(tablaEntSingulares)) //Si  ha seleccionado alguna fila
      {
        var i=rowID;
        indice = rowID;
        limpiarFormulario("campos_rejilla");
        if(i>=0){
          var vector = [document.forms[0].codEntidadSingular];
          deshabilitarGeneral(vector);
      var vectorDatosRejilla = [listaEntSingularesOriginal[i][3],listaEntSingularesOriginal[i][7],listaEntSingularesOriginal[i][6]];
          rellenar(vectorDatosRejilla,vectorCamposRejilla);
          document.forms[0].codMunicipioAntiguo.value = listaEntSingularesOriginal[i][2];
      }
      } 
    }
  //calcularDigitoControl();
  } //de la funcion

  // FUNCION DE CONTROL DE TECLAS
  document.onmouseup = checkKeys; 

  function checkKeysLocal(evento,tecla)
  {
    var teclaAuxiliar;
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

      keyDel(evento);
      if (teclaAuxiliar == 38) upDownTable(tablaEntSingulares,listaEntSingulares,teclaAuxiliar);
      if (teclaAuxiliar == 40) upDownTable(tablaEntSingulares,listaEntSingulares,teclaAuxiliar);

  }//de la funcion
  
  var comboEco = new Combo("Eco");
  
</script>
</body>
</html>
