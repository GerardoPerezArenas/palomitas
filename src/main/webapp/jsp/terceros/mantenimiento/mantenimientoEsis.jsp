<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title> Mantenimiento de Entidades Singulares </title>
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
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
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
        var listaEntSingularesCompleta = new Array();
        //Variables para albergar los valores antiguos de país, provincia y municipio para
        //no tener que volver a cargar de la BD cuando el valor de los campos no ha cambiado
        var paisOld = "";
        var provinciaOld = "";
        var municipioOld = "";
        //Variable para direccionamiento al frame
        var frame;
        
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
            // Provincias
            String codProvincias = "";
            String descProvincias = "";
            for (i = 0; i < lengthProvs - 1; i++) {
                GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
                codProvincias += "\"" + (String) provincias.getAtributo("codigo") + "\",";
                descProvincias += "\"" + (String) provincias.getAtributo("descripcion") + "\",";
            }
            if (lengthProvs > 0) {
                GeneralValueObject provincias = (GeneralValueObject) listaProvincias.get(i);
                codProvincias += "\"" + (String) provincias.getAtributo("codigo") + "\"";
                descProvincias += "\"" + (String) provincias.getAtributo("descripcion") + "\"";
            }
            // Municipios
            String codMunicipios = "";
            String descMunicipios = "";
            for (i = 0; i < lengthMuns - 1; i++) {
                GeneralValueObject municipios = (GeneralValueObject) listaMunicipios.get(i);
                codMunicipios += "\"" + (String) municipios.getAtributo("codMunicipio") + "\",";
                descMunicipios += "\"" + (String) municipios.getAtributo("nombreOficial") + "\",";
            }
            if (lengthMuns > 0) {
                GeneralValueObject municipios = (GeneralValueObject) listaMunicipios.get(i);
                codMunicipios += "\"" + (String) municipios.getAtributo("codMunicipio") + "\"";
                descMunicipios += "\"" + (String) municipios.getAtributo("nombreOficial") + "\"";
            }
            //Entidades colectivas
            String codEntColectivas = "";
            String descEntColectivas = "";
            String codINE = "";
            for (i = 0; i < lengthEntColectivas - 1; i++) {
                GeneralValueObject ecos = (GeneralValueObject) listaEntColectivas.get(i);
                codEntColectivas += "\"" + (String) ecos.getAtributo("codECO") + "\",";
                descEntColectivas += "\"" + (String) ecos.getAtributo("descECO") + "\",";
                codINE += "\"" + (String) ecos.getAtributo("codINE") + "\",";
            }
            if (lengthEntColectivas > 0) {
                GeneralValueObject ecos = (GeneralValueObject) listaEntColectivas.get(i);
                codEntColectivas += "\"" + (String) ecos.getAtributo("codECO") + "\"";
                descEntColectivas += "\"" + (String) ecos.getAtributo("descECO") + "\"";
                codINE += "\"" + (String) ecos.getAtributo("codINE") + "\"";
            }
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
                                  
                                  
                                  function redireccionaFrame() {
                                      
                                      frame=(ventana=="true")?top1.mainFrame1:top.mainFrame;	
                                  }
                                  
                                  
                                  function inicializar()
                                  {
                                      recuperaDatosIniciales();
                                      comboEco.addItems(codEntColectivas,descEntColectivas);
                                      comboEntidadColectiva.addItems(codEntColectivas,descEntColectivas);
                                      redireccionaFrame();
                                      //Inicializamos los vectores para los campos, imágenes y enlaces de BUSQUEDA (provincia y municipio)
                                      //vectorCamposBusqueda1 = [document.forms[0].codProvincia,document.forms[0].descProvincia,
                                      //document.forms[0].codMunicipio,document.forms[0].descMunicipio];
                                      //vectorAnchorsBusqueda = [document.all.anchorProvincia,document.all.anchorMunicipio];
                                      //vectorBotonesBusqueda = [document.forms[0].botonProvincia,document.forms[0].botonMunicipio];
                                      //Vectores para los campos, imágenes y enlaces de la REJILLA
                                      vectorCamposRejilla1 = [document.forms[0].codEntidadSingular,document.forms[0].nombreOficial,
                                          document.forms[0].nombreLargo,document.forms[0].codEntidadColectiva,document.forms[0].descEntidadColectiva,
                                          document.forms[0].ine,document.forms[0].digitoControl,
                                          document.forms[0].situacion];
                                      //vectorAnchorsRejilla = [document.all.anchorEntColectivas];
                                      //vectorBotonesRejilla = [document.forms[0].botonEntColectivas];
                                      //Vector de Botones para la realización de las OPERACIONES con la Ent Singular
                                      vectorBotones = [document.forms[0].botonAlta,document.forms[0].botonModificar,
                                          document.forms[0].botonBorrar,document.forms[0].botonLimpiar, document.forms[0].botonModReg];
                                      //Inicializo el Formulario
                                      inicializarFormulario();
                                      if(ventana=="false"){
                                          pleaseWait1("off",frame);
                                          
                                              
                                          
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
                                  comboEntidadColectiva.deactivate();
                                  deshabilitarGeneral(vectorBotones);
                                  valoresPorDefecto();
                              }//de la funcion
                              
                              
                              // FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS
                              var vectorCamposBusqueda = ["codProvincia","descProvincia","codMunicipio","descMunicipio"];
                              var vectorCamposRejilla = ["codEntidadSingular","nombreOficial","nombreLargo","codEntidadColectiva","descEntidadColectiva","ine",
                                  "digitoControl","situacion"];
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
                                                      var nombreOficial = document.forms[0].nombreOficial.value;
                                                      var codINE = document.forms[0].ine.value;
                                                      var digitoControl = document.forms[0].digitoControl.value;
                                                      if(nombreOficial !="" && codINE !="" && digitoControl != "")
                                                          return true;
                                                      return false;
                                                  }//de la funcion
                                                  
                                                  function noExiste(indice)
                                                  {
                                                      var cod = document.forms[0].codEntidadSingular.value;
                                                      for(i=0;i<listaEntSingularesCompleta.length;i++){
                                                          if(i!=indice){			
                                                              if((listaEntSingularesCompleta[i][0]) == cod){
                                                                  if ((listaEntSingularesCompleta[i][0] == listaEntSingularesOriginal[indice][3])
                                                                      && (listaEntSingularesCompleta[i][1] == listaEntSingularesOriginal[indice][4])
                                                                      && (listaEntSingularesCompleta[i][2] == listaEntSingularesOriginal[indice][7])
                                                                      &&  (listaEntSingularesCompleta[i][3] == listaEntSingularesOriginal[indice][5])
                                                                      &&  (listaEntSingularesCompleta[i][4] == listaEntSingularesOriginal[indice][6]))
                                                                  return true; // Es el mismo
                                                                  else return false;
                                                              }
                                                          }
                                                      }
                                                      return true;
                                                  }//de la funcion
                                                  
                                                  function noEsta(indice)
                                                  {
                                                      var cod = document.forms[0].ine.value;
                                                      var codEco = document.forms[0].codEco.value;
                                                      if(codEco == "") {
                                                          codEco = document.forms[0].codEntidadColectiva.value;
                                                      }
                                                      for(i=0;i<listaEntSingularesOriginal.length;i++){
                                                          if(i!=indice){			
                                                              if((listaEntSingularesOriginal[i][6]) == cod && 
                                                                  (listaEntSingularesOriginal[i][4]) == codEco){
                                                              return false;
                                                          }
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
                                                  if(document.forms[0].codEco.value != "") {
                                                      var cECO = [document.forms[0].codEco.value];
                                                      var dECO = [document.forms[0].descEco.value];
                                                      comboEntidadColectiva.addItems(cECO,dECO);
                                                  }
                                                  var botonBuscar = [document.forms[0].botonBuscar];
                                                  if(validarCamposBusqueda())
                                                      {
                                                          document.forms[0].opcion.value="cargarEsisTodas";
                                                          document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Esis.do";
                                                          document.forms[0].submit();
                                                          comboEco.deactivate();
                                                          deshabilitarGeneral(botonBuscar);
                                                          habilitarGeneral(vectorBotones);
                                                          habilitarGeneral(vectorCamposRejilla1);
                                                          comboEntidadColectiva.activate();
                                                          limpiarFormulario("campos_rejilla");
                                                      }
                                                      else
                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                          }//de la funcion
                                                          
                                                          function pulsarAlta()
                                                          {
                                                              if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
                                                                  {
                                                                      if(noEsta()) //Si el código está en la lista
                                                                          {
                                                                              comboEco.activate();
                                                                              document.forms[0].situacion.value="A";
                                                                              //habilitarGeneral(vectorCamposBusqueda1);
                                                                              document.forms[0].opcion.value="alta";
                                                                              document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                                              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Esis.do";
                                                                              document.forms[0].submit();
                                                                              //deshabilitarGeneral(vectorCamposBusqueda1);
                                                                              limpiarFormulario("campos_rejilla");
                                                                              comboEco.deactivate();
                                                                          }
                                                                          else
                                                                              jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                                                              } else
                                                                              jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                                              }//de la funcion
                                                                              
                                                                              
                                                                              function pulsarModificar()	{
                                                                                  if(filaSeleccionada(tablaEntSingulares)) //Si  ha seleccionado alguna fila
                                                                                      {
                                                                                          if(listaEntSingulares[tablaEntSingulares.selectedIndex][6] != "B") {
                                                                                              if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
                                                                                                  {
                                                                                                      if(noEsta(tablaEntSingulares.selectedIndex)) //Si el código está en la lista
                                                                                                          {
                                                                                                              comboEco.activate();
                                                                                                              var vector = [document.forms[0].codEntidadSingular];
                                                                                                              habilitarGeneral(vector);
                                                                                                              //document.forms[0].situacion.value="";
                                                                                                              //habilitarGeneral(vectorCamposBusqueda1);
                                                                                                              document.forms[0].opcion.value="modificar";
                                                                                                              document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                                                                              document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Esis.do";
                                                                                                              document.forms[0].submit();
                                                                                                              //deshabilitarGeneral(vectorCamposBusqueda1);
                                                                                                              habilitarGeneral(vector);
                                                                                                              limpiarFormulario("campos_rejilla");
                                                                                                              comboEco.deactivate();
                                                                                                          } else
                                                                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                                                                                          } else
                                                                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                                                                          } else 
                                                                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjEstaBaja")%>");
                                                                                                          } else
                                                                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
                                                                                                          }//de la funcion
                                                                                                          
                                                                                                          
                                                                                                          function pulsarBorrar()
                                                                                                          {
                                                                                                              if(filaSeleccionada(tablaEntSingulares)) //Si  ha seleccionado alguna fila
                                                                                                                  {
                                                                                                                      if(validarCamposBusqueda()) //Si los campos de la rejilla son correctos (los obligatorios)
                                                                                                                          {
                                                                                                                              if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimESI")%>') ==1) {
                                                                                                                                  //habilitarGeneral(vectorCamposBusqueda1);
                                                                                                                                  comboEco.activate();
                                                                                                                                  var vector = [document.forms[0].codEntidadSingular];
                                                                                                                                  habilitarGeneral(vector);
                                                                                                                                  document.forms[0].opcion.value="eliminar";
                                                                                                                                  document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                                                                                                  document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Esis.do";
                                                                                                                                  document.forms[0].submit();
                                                                                                                                  //deshabilitarGeneral(vectorCamposBusqueda1);
                                                                                                                                  deshabilitarGeneral(vector);
                                                                                                                                  limpiarFormulario("campos_rejilla");
                                                                                                                                  if(tablaEntSingulares.selectedIndex != -1 ) {
                                                                                                                                      tablaEntSingulares.selectLinea(tablaEntSingulares.selectedIndex);
                                                                                                                                      tablaEntSingulares.selectedIndex = -1;
                                                                                                                                  }
                                                                                                                                  comboEco.deactivate();
                                                                                                                              }
                                                                                                                          } else
                                                                                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                                                                                          }	else
                                                                                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
                                                                                                                          }//de la funcion
                                                                                                                          
                                                                                                                          function noEliminarEsis() {
                                                                                                                              jsp_alerta("A", "<%=descriptor.getDescripcion("msjNoElimEsis")%>");
                                                                                                                              }
                                                                                                                              
                                                                                                                              
                                                                                                                              function pulsarCancelarBuscar()
                                                                                                                              {
                                                                                                                                  comboEntidadColectiva.addItems(codEntColectivas,descEntColectivas);
                                                                                                                                  limpiarFormulario("rejilla/tabla");
                                                                                                                                  var botonBuscar = [document.forms[0].botonBuscar];
                                                                                                                                  habilitarGeneral(botonBuscar);
                                                                                                                                  deshabilitarGeneral(vectorCamposRejilla1);
                                                                                                                                  comboEntidadColectiva.deactivate();
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
                                                                                                                          
                                                                                                                          function calcularDigitoControl() {
                                                                                                                              if(document.forms[0].codEco.value != "" || document.forms[0].codEntidadColectiva.value != "") {
                                                                                                                                  if(document.forms[0].ine.value != "") {
                                                                                                                                      var entCol = "";
                                                                                                                                      if(document.forms[0].codEco.value != "") {
                                                                                                                                          entCol = codINE[comboEco.selectedIndex-1];
                                                                                                                                      } else {
                                                                                                                                      entCol = codINE[comboEntidadColectiva.selectedIndex-1];
                                                                                                                                  }
                                                                                                                                  var entSing = document.forms[0].ine.value;
                                                                                                                                  var unidad = [0,3,8,2,7,4,1,5,9,6];
                                                                                                                                  var decena = [0,2,4,6,8,1,3,5,7,9];
                                                                                                                                  var total = entCol;
                                                                                                                                  total = eval(total*100)+eval(entSing);
                                                                                                                                  var resultado = 0;
                                                                                                                                  var aux1 = 0;
                                                                                                                                  var aux2 = 0;
                                                                                                                                  for(i=1;i<5;i++) {
                                                                                                                                      aux1 = total%10;
                                                                                                                                      if(i==1 || i==4) aux2 = unidad[aux1];
                                                                                                                                      if(i==2) aux2 = decena[aux1];
                                                                                                                                      if(i==3) aux2 = aux1;
                                                                                                                                      resultado = resultado + aux2;
                                                                                                                                      t = total%10;
                                                                                                                                      t1 = total - t;
                                                                                                                                      total = t1/10;
                                                                                                                                  }
                                                                                                                                  resultado = 10 - (resultado%10);
                                                                                                                                  if(resultado == 10 ) resultado = 0;
                                                                                                                                  document.forms[0].digitoControl.value = resultado;
                                                                                                                              } else document.forms[0].digitoControl.value = "";
                                                                                                                          } else document.forms[0].digitoControl.value = "";
                                                                                                                      }
                                                                                                                      
                                                                                                                      function haCambiadoValor(){
                                                                                                                          var i= tablaEntSingulares.focusedIndex;
                                                                                                                          var codINE = document.forms[0].ine.value;
                                                                                                                          var descESI = document.forms[0].nombreOficial.value;
                                                                                                                          if((codINE!=listaEntSingulares[i][1])||(descESI!=listaEntSingulares[i][3]))
                                                                                                                              return true;
                                                                                                                          return false;
                                                                                                                      }
                                                                                                                      
                                                                                                                      function pulsarModificarRegistro()	{
                                                                                                                          if(filaSeleccionada(tablaEntSingulares)) //Si  ha seleccionado alguna fila
                                                                                                                              {
                                                                                                                                  if(listaEntSingulares[tablaEntSingulares.selectedIndex][6] != "B") {
                                                                                                                                      if(haCambiadoValor()){
                                                                                                                                          if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
                                                                                                                                              {
                                                                                                                                                  if(noEsta(tablaEntSingulares.selectedIndex)) { //Si el código está en la lista
                                                                                                                                                        var source = "<%=request.getContextPath()%>/jsp/terceros/mantenimiento/datosConRegistro.jsp?opcion=null";
                                                                                                                                                        abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source="+source,'',
                                                                                                                                                                'width=640,height=380,status='+ '<%=statusBar%>',function(resp){
                                                                                                                                                                        if(resp!=undefined){
                                                                                                                                                                            document.forms[0].fechaOperacion.value = resp[0];
                                                                                                                                                                            document.forms[0].generarOperaciones.value= resp[1];
                                                                                                                                                                            comboEco.activate();
                                                                                                                                                                            var vector = [document.forms[0].codEntidadSingular];
                                                                                                                                                                            habilitarGeneral(vector);
                                                                                                                                                                            //document.forms[0].situacion.value="";
                                                                                                                                                                            //habilitarGeneral(vectorCamposBusqueda1);
                                                                                                                                                                            document.forms[0].opcion.value="modificarEsiTerritorio";
                                                                                                                                                                            document.forms[0].target="oculto";
                                                                                                                                                                            document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Esis.do";
                                                                                                                                                                            document.forms[0].submit();
                                                                                                                                                                            //deshabilitarGeneral(vectorCamposBusqueda1);
                                                                                                                                                                            habilitarGeneral(vector);
                                                                                                                                                                            limpiarFormulario("campos_rejilla");
                                                                                                                                                                            comboEco.deactivate();
                                                                                                                                                                        }
                                                                                                                                                                });
                                                                                                                                                  } else
                                                                                                                                                  jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                                                                                                                                  } else
                                                                                                                                                  jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                                                                                                                  }else{
                                                                                                                                                  jsp_alerta("A","No se ha producido ningún cambio"); 
                                                                                                                                              }			
                                                                                                                                          } else 
                                                                                                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjEstaBaja")%>");
                                                                                                                                          } else
                                                                                                                                          jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
                                                                                                                                          }//de la funcion
                                                                                                                                          
        </script>
    </head>
    
    <body class="bandaBody" onLoad="inicializar();">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <form action="" method="get" name="formulario" target="_self">
            <input type="hidden" name="opcion">
            <input type="hidden" name="codEntidadSingular" value="">
            <input type="hidden" name="codPais" size="3" value="<%=ptVO.getPais()%>">
            <input type="hidden" name="codMunicipioAntiguo" value="">
            <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
            <input type="hidden" name="codProvincia" value="<%=codProvincias%>">
            <input type="hidden" name="descProvincia" value="<%=descProvincias%>">
            <input type="hidden" name="codMunicipio" value="<%=codMunicipios%>">
            <input type="hidden" name="descMunicipio" value="<%=descMunicipios%>">
            <!-- Modificacion con registro -->
            <input type="hidden" name="fechaOperacion" value=""">
                   <input type="hidden" name="generarOperaciones" value="">
            
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_ModEsings")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td>
                    <table width="100%">                                                                                                            
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td style="width: 14%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_ECO")%>:</td>
                            <td style="width: 35%">
                                <input type="text" class="inputTexto" name="codEco" size="3">
                                <input type="text" class="inputTexto" name="descEco" style="width:200" readonly="true">
                                <A href="" name="anchorEco" id="anchorEco">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonEco" id="botonEco" style="cursor:hand;"></span>
                                </A>
                            </td>
                            <td align="right">
                                <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                                       value="<%=descriptor.getDescripcion("gbBuscar")%>"
                                       onClick="pulsarBuscar();" accesskey="B">
                                <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
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
            <tr>
                <td id="tablaEntSingulares">
                </td>
            </tr>
            <tr>
                <td> 
                    <input name="ine" type="text" class="inputTextoObligatorio" style="width: 60px" maxlength=2
                           onkeyup = "return SoloDigitosNumericos(this);"
                           onChange = "javascript:calcularDigitoControl();" style="width: 60px">
                    <input name="digitoControl" type="text" class="inputTextoObligatorio" maxlength=1
                           readonly style="width: 55px">
                    <input name="nombreOficial" type="text" class="inputTextoObligatorio" maxlength=25
                            onkeyup = "return xAMayusculas(this);" style="width: 260px">
                    <input name="nombreLargo" type="text" class="inputTexto" size=35 maxlength=50
                           onkeyup = "return xAMayusculas(this);" style="width: 225px">
                    <input type="text" class="inputTexto" name="codEntidadColectiva" size="3" style="width: 20px">
                    <input type="text" class="inputTexto" name="descEntidadColectiva" style="width:170px" readonly="true">                                                                                                        
                    <a href="" name="anchorEntidadColectiva" id="anchorEntidadColectiva" style="text-decoration:none; margin-right: 8px">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonEntidadColectiva" id="botonEntidadColectiva" style="cursor:hand;"></span>
                    </a>
                    <input name="situacion" type="text" class="inputTexto" size=4 readonly style="width: 60px">
                </td>
            </tr>                                                                                     
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral"  name="botonAlta"	onClick="pulsarAlta();" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>"> 
            <input type="button" class="botonGeneral"  name="botonModificar"	onClick="pulsarModificar();" accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
            <input type="button" class="botonGeneral" name="botonModReg" onClick="pulsarModificarRegistro();" accesskey="R" value="<%=descriptor.getDescripcion("gbModReg")%>">
            <input type="button" class="botonGeneral" name="botonBorrar"	onClick="pulsarBorrar();" accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>">
            <input type="button" class="botonGeneral"  name="botonLimpiar"	onClick="pulsarLimpiar();" accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
            <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>"> 
        </div>            				            
    </div>            				            
</form>
<script>
var indice;
var tablaEntSingulares = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaEntSingulares"));
tablaEntSingulares.addColumna("60","left",'<%=descriptor.getDescripcion("gEtiq_INE")%>');
tablaEntSingulares.addColumna("60","left",'<%=descriptor.getDescripcion("gEtiq_DigitControl")%>');
tablaEntSingulares.addColumna("250","left",'<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
tablaEntSingulares.addColumna("225","left",'<%=descriptor.getDescripcion("gEtiq_NomLargo")%>');
tablaEntSingulares.addColumna("225","left",'<%=descriptor.getDescripcion("gEtiq_ECO")%>');
tablaEntSingulares.addColumna("60","left",'<%=descriptor.getDescripcion("gEtiq_Situacion")%>');  
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
                            var vectorDatosRejilla = [listaEntSingularesOriginal[i][3],listaEntSingularesOriginal[i][7],
                                listaEntSingularesOriginal[i][8],listaEntSingularesOriginal[i][4],listaEntSingularesOriginal[i][13],
                                listaEntSingularesOriginal[i][6],listaEntSingularesOriginal[i][5],
                                listaEntSingularesOriginal[i][12]];
                            var vectorCamposRejilla = ["codEntidadSingular",
                                "nombreOficial","nombreLargo","codEntidadColectiva",
                                "descEntidadColectiva","ine","digitoControl","situacion"];

                            rellenar(vectorDatosRejilla,vectorCamposRejilla);
                            document.forms[0].codMunicipioAntiguo.value = listaEntSingularesOriginal[i][2];
                            comboEntidadColectiva.buscaCodigo(document.forms[0].codEntidadColectiva.value);
                        }
                    } 
                }
            } //de la funcion

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

            function checkKeysLocal(evento,tecla)
            {
                var teclaAuxiliar = "";
                if(window.event){
                    evento         = window.event;
                    teclaAuxiliar =  evento.keyCode;
                }else
                    teclaAuxiliar =  evento.which;

                keyDel(evento);
                if (teclaAuxiliar == 38) upDownTable(tablaEntSingulares,listaEntSingulares,teclaAuxiliar);
                if (teclaAuxiliar == 40) upDownTable(tablaEntSingulares,listaEntSingulares,teclaAuxiliar);
                if (teclaAuxiliar == 1){
                    if (comboEco.base.style.visibility == "visible" && isClickOutCombo(comboEco,coordx,coordy)) setTimeout('comboEco.ocultar()',20);
                    if (comboEntidadColectiva.base.style.visibility == "visible" && isClickOutCombo(comboEntidadColectiva,coordx,coordy)) setTimeout('comboEntidadColectiva.ocultar()',20);
                }
                if (teclaAuxiliar == 9){
                    comboEco.ocultar();
                    comboEntidadColectiva.ocultar();
                }

            }//de la funcion

            var comboEco = new Combo("Eco");
            var comboEntidadColectiva = new Combo("EntidadColectiva");

            comboEntidadColectiva.change = 
            function() { 
                calcularDigitoControl();		
            }
        </script>
    </body>
</html>
