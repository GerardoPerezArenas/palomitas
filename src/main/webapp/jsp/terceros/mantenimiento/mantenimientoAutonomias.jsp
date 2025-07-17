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
    <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
    <title> Mantenimiento de Autonomias </title>
    <!-- Estilos -->

    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma = 1;
        int apl = 3;
        String css = "";
        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
            css = usuarioVO.getCss();

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
     <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <script type="text/javascript">
        // VARIABLES GLOBALES
             <%
        MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
%>  
    var codPais = new Array();
    var desPais = new Array();
    var listaAutonomias = new Array();
    var listaAutonomiasOriginal = new Array();

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS  
    function recuperaDatosIniciales()
    {
<%
        Vector listaPais = mantForm.getListaPaises();
        int lengthPais = listaPais.size();
        int i = 0;
        String codPais = "";
        String descPais = "";
        if (lengthPais > 0) {
            for (i = 0; i < lengthPais - 1; i++) {
                GeneralValueObject pais = (GeneralValueObject) listaPais.get(i);
                codPais += "\"" + (String) pais.getAtributo("codigo") + "\",";
                descPais += "\"" + (String) pais.getAtributo("descripcion") + "\",";
            }
            GeneralValueObject pais = (GeneralValueObject) listaPais.get(i);
            codPais += "\"" + (String) pais.getAtributo("codigo") + "\"";
            descPais += "\"" + (String) pais.getAtributo("descripcion") + "\"";
        }
  %>
      codPais = [<%=codPais%>];
          descPais = [<%=descPais%>];

          }//de la funcion



          function inicializar(){
              recuperaDatosIniciales();
              comboPais.addItems(codPais,descPais);
              deshabilitarFormulario1();
          }//de la funcion

          function pulsarBuscar() {
              if(validarCamposBusqueda()) {
                  document.forms[0].opcion.value="cargarAutonomias";
                  document.forms[0].target="oculto";
                  document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Autonomias.do";
                  document.forms[0].submit();
                  deshabilitarFormulario();
                  habilitarFormulario1();
              } else {
              jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
              }
          }

          function validarCamposBusqueda() {
              var cD = document.forms[0].codPais.value;
              if(cD != "") {
                  return true;
              }
              return false;
          }

          function deshabilitarFormulario() {
              comboPais.deactivate();
              var botonBuscar = [document.forms[0].botonBuscar];
              deshabilitarGeneral(botonBuscar);
          }

          function pulsarCancelarBuscar() {
              comboPais.activate();
              document.forms[0].codPais.value = "";
              document.forms[0].descPais.value = "";
              var botonBuscar = [document.forms[0].botonBuscar];
              habilitarGeneral(botonBuscar);
              deshabilitarFormulario1();
          }

          function deshabilitarFormulario1() {
              var vector = [document.forms[0].codAutonomia];
              habilitarGeneral(vector);
              document.forms[0].codAutonomia.value = "";
              document.forms[0].nombre.value = "";
              document.forms[0].nombreLargo.value = "";
              tablaAutonomias.lineas = new Array();
              refresca();
              var vector = [document.forms[0].botonAlta,document.forms[0].botonModificar,document.forms[0].botonBorrar,
                  document.forms[0].botonLimpiar];
              deshabilitarGeneral(vector);
              var vectorCampos = [document.forms[0].codAutonomia,document.forms[0].nombre,document.forms[0].nombreLargo];
              deshabilitarGeneral(vectorCampos);
          }

          function habilitarFormulario1() {
              var vector = [document.forms[0].botonAlta,document.forms[0].botonModificar,document.forms[0].botonBorrar,
                  document.forms[0].botonLimpiar];
              habilitarGeneral(vector);
              var vectorCampos = [document.forms[0].codAutonomia,document.forms[0].nombre,document.forms[0].nombreLargo];
              habilitarGeneral(vectorCampos);
          }

          function cargaListaAutonomias(list1,list2) {
              listaAutonomiasOriginal = list1;
              listaAutonomias = list2;
              tablaAutonomias.lineas = listaAutonomias;
              refresca();
              var vector = [document.forms[0].codAutonomia];
              habilitarGeneral(vector);
              document.forms[0].codAutonomia.value = "";
              document.forms[0].nombre.value = "";
              document.forms[0].nombreLargo.value = "";
          }

          function pulsarAlta() {
              if(validarCamposRejilla()) {
                  var cod = document.forms[0].codAutonomia.value;
                  var existe = 0;
                  for(i=0;(i<listaAutonomias.length);i++){
                      if((listaAutonomias[i][0]) == cod)  {
                          existe = 1;
                      }   
                  }
                  if(existe == 0) {
                      comboPais.activate();
                      document.forms[0].opcion.value="alta";
                      document.forms[0].target="oculto";
                      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Autonomias.do";
                      document.forms[0].submit();
                      comboPais.deactivate();
                  } else {
                  jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                  }
              } else {
              jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
              }
          }

          function pulsarModificar() {
              if(tablaAutonomias.selectedIndex != -1) {
                  if(validarCamposRejilla()) {
                      var cod = document.forms[0].codAutonomia.value;
                      var existe = 0;
                      for(i=0;(i<listaAutonomias.length);i++){
                          if((listaAutonomias[i][0]) == cod && i!=tablaAutonomias.selectedIndex)  {
                              existe = 1;
                          }   
                      }
                      if(existe == 0) {
                          comboPais.activate();
                          var vector = [document.forms[0].codAutonomia];
                          habilitarGeneral(vector);
                          document.forms[0].opcion.value="modificar";
                          document.forms[0].target="oculto";
                          document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Autonomias.do";
                          document.forms[0].submit();
                          deshabilitarGeneral(vector);
                          comboPais.deactivate();
                      } else {
                      jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>");
                      }
                  } else {
                  jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                  }
              } else {
              jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
              }
          }

          function validarCamposRejilla() {
              var c = document.forms[0].codAutonomia.value;
              if(c != "") {
                  return true;
              }
              return false;
          }

          function pulsarLimpiar() {
              var vector = [document.forms[0].codAutonomia];
              habilitarGeneral(vector);
              document.forms[0].codAutonomia.value = "";
              document.forms[0].nombre.value = "";
              document.forms[0].nombreLargo.value = "";
          }

          function pulsarLimpiar2() {
              pulsarLimpiar();
              if(tablaAutonomias.selectedIndex != -1 ) {
                  tablaAutonomias.selectLinea(tablaAutonomias.selectedIndex);
                  tablaAutonomias.selectedIndex = -1;
              }
          }

          function pulsarSalir()	{
              document.forms[0].target = "mainFrame";
              document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
              document.forms[0].submit();
          }//de la funcion

          function pulsarEliminar() {
              if(tablaAutonomias.selectedIndex != -1) {
                  if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimAuton")%>') ==1) {
                      comboPais.activate();
                      var vector = [document.forms[0].codAutonomia];
                      habilitarGeneral(vector);
                      document.forms[0].opcion.value="eliminar";
                      document.forms[0].target="oculto";
                      document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Autonomias.do";
                      document.forms[0].submit();
                      deshabilitarGeneral(vector);
                      comboPais.deactivate();
                      pulsarLimpiar();
                      if(tablaAutonomias.selectedIndex != -1 ) {
                          tablaAutonomias.selectLinea(tablaAutonomias.selectedIndex);
                          tablaAutonomias.selectedIndex = -1;
                      }
                  }
              } else {
              jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");
              }
          }

          function noEliminarAuton() {
              jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimAuton")%>");
              }


    </script>
</head>
    
<body class="bandaBody" onload="javascript:{ pleaseWait('off'); inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<form action="" method="get" name="formulario" target="_self">
    <input type="hidden" name="opcion">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantAuton")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td colspan="2" class="etiqueta"><%= descriptor.getDescripcion("gEtiq_Pais")%></td>
            </tr>
            <tr>
                <td class="etiqueta">
                    <input type="text" class="inputTextoObligatorio" name="codPais" size="3"
                           onkeyup = "return SoloDigitosNumericos(this);">
                    <input type="text" class="inputTextoObligatorio" name="descPais" style="width:300" readonly="true">
                    <A href="" name="anchorPais" id="anchorPais">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonPais" id="botonPais" style="cursor:hand;"></span>
                    </A>
                </td>
                <td style="width:40%;text-align:right">
                    <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                           value="<%=descriptor.getDescripcion("gbBuscar")%>"
                           onClick="pulsarBuscar();" accesskey="B">
                    <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                           value="<%=descriptor.getDescripcion("gbCancelar")%>"
                           onClick="pulsarCancelarBuscar();" accesskey="C">
                </td>
            </tr>
            <tr> 
                <td colspan="2" id="tablaAutonomias">
            </tr>
            <tr> 
                <td colspan="2"> 
                    <input name="codAutonomia" type="text" class="inputTextoObligatorio" maxlength=2 
                           style="width:10%" onkeyup = "return SoloDigitosNumericos(this);">
                    <input name="nombre" type="text" class="inputTexto" maxlength=25
                           style="width:39%" onblur = "return xAMayusculas(this);">
                    <input name="nombreLargo" type="text" class="inputTexto" maxlength=50
                           style="width:49.5%" onblur = "return xAMayusculas(this);">
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral"  name="botonAlta"	onClick="pulsarAlta();" 
                   accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>"> 
            <input type="button" class="botonGeneral"  name="botonModificar"	onClick="pulsarModificar();" 
                   accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
            <input type="button" class="botonGeneral" name="botonBorrar"	onClick="pulsarEliminar();" 
                   accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>">
            <input type="button" class="botonGeneral"  name="botonLimpiar"	onClick="pulsarLimpiar2();" 
                   accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
            <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" 
                   accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>"> 
        </div>                        
    </div>                        
</form>
<script type="text/javascript">
            
            var tablaAutonomias = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaAutonomias"));
            tablaAutonomias.addColumna("90","center",'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
                tablaAutonomias.addColumna("360","left",'<%=descriptor.getDescripcion("gEtiq_NomLargo")%>');
                    tablaAutonomias.addColumna("450","left",'<%=descriptor.getDescripcion("gEtiq_NomOficial")%>');
                        
                        tablaAutonomias.displayCabecera=true;
                        tablaAutonomias.displayTabla();
                        
                        
                        function refresca()	{
                            tablaAutonomias.displayTabla();
                        }//de la funcion
                        
                        function rellenarDatos(tableName,rowID)	{
                            if(rowID != -1){
                                var i=rowID;
                                if(i>=0){
                                    var vector = [document.forms[0].codAutonomia];
                                    deshabilitarGeneral(vector);
                                    document.forms[0].codAutonomia.value = listaAutonomias[i][0];
                                    document.forms[0].nombre.value = listaAutonomias[i][1];
                                    document.forms[0].nombreLargo.value = listaAutonomias[i][2];
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
                        var aux=null;
                        if(window.event)
                            aux = window.event;
                        else
                            aux = evento;

                        var tecla = 0;
                        if(aux.keyCode)
                            tecla = aux.keyCode;
                        else
                            tecla = aux.which;

                            keyDel(aux);
                            if (tecla == 38) upDownTable(tablaAutonomias,listaAutonomias,tecla);
                            if (tecla == 40) upDownTable(tablaAutonomias,listaAutonomias,tecla);
                            if (tecla == 1){if (comboPais.base.style.visibility == "visible" && isClickOutCombo(comboPais,coordx,coordy)) setTimeout('comboPais.ocultar()',20); }
                            if (tecla == 9)comboPais.ocultar();
                            
                        }//de la funcion
                        
                        var comboPais = new Combo("Pais");	
                        
                        
        </script>
    </body>
</html>
