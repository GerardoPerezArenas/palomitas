<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@ page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DefinicionProcedimientosForm"%>

<%
UsuarioValueObject usuarioVO = new UsuarioValueObject();
int idioma=1;
int apl=4;
int codOrg = 0;
int codEnt = 1;
if (session.getAttribute("usuario") != null){
usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
  idioma = usuarioVO.getIdioma();
  codOrg = usuarioVO.getOrgCod();
  codEnt = usuarioVO.getEntCod();
}
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Lista de Unidades Tramitadoras</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
	<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
    <script type="text/javascript">

    var uors = new Array();       // Lista de todas las unidades existentes
    var listaUors = new Array();  // Lista de codigos de unidades seleccionadas
    var listaTabla = new Array(); // Lista de filas que se muestran en la tabla

    function inicializar() {
        window.focus();
        uors = self.parent.opener.xanelaAuxiliarArgs['uordtos'];
        listaUors = self.parent.opener.xanelaAuxiliarArgs['utrs'];
        mostrarListaTabla();
    }

    function mostrarListaTabla() {
        listaTabla = new Array();
        for (var i = 0; i<listaUors.length; i++) {
            var uor = buscarUorPorCod(listaUors[i]);
            listaTabla[i] = [uor.uor_cod_vis, uor.uor_nom];
        }
        tablaUnidades.lineas=listaTabla;
        refresca();
    }

    function desactivarBotones() {
      document.forms[0].cmdAlta.disabled = true;
      document.forms[0].cmdModificar.disabled = true;
      document.forms[0].cmdEliminar.disabled = true;
      document.forms[0].cmdLimpiar.disabled = true;
      tablaUnidades.readOnly = true;
    }

    function pulsarLimpiar() {
      document.forms[0].codUnidadInicio.value = "";
      document.forms[0].codVisibleUnidadInicio.value = "";
      document.forms[0].descUnidadInicio.value = "";
    }

    function pulsarAlta() {
			onchangeCodUnidadInicio();
			var codUnidad = document.forms[0].codUnidadInicio.value;
			var yaExiste = 0;
			if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
        for(var i=0; i < listaUors.length; i++){
			  if (listaUors[i] == codUnidad){
				  yaExiste = 1;
				}
			  }
			  if(yaExiste == 0) {
          listaUors[listaUors.length] = codUnidad;
				mostrarListaTabla();
				pulsarLimpiar();
			  } else {
				jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
			  }
			} else {
			  jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
			}
		}

    function pulsarModificar() {
      onchangeCodUnidadInicio();
      var codUnidad = document.forms[0].codUnidadInicio.value;
      var yaExiste = 0;
      if(tablaUnidades.selectedIndex != -1) {
        if (validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
          var lineaSeleccionada = tablaUnidades.selectedIndex;
          for(var i=0; i < listaUors.length; i++){
            if(i != lineaSeleccionada && listaUors[i] == codUnidad){
                yaExiste = 1;
              }
          }
          if(yaExiste == 0) {
            listaUors[tablaUnidades.selectedIndex] = codUnidad;
            mostrarListaTabla();
            pulsarLimpiar();
          } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
          }
        } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
        }
      } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }

    function pulsarEliminar() {
       if(tablaUnidades.selectedIndex != -1) {
         if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
           if(jsp_alerta('', '<%=descriptor.getDescripcion("desEliminarUTR")%>')) {
               var listaNueva = new Array();
               var j = 0;
               for (var i=0; i<listaUors.length; i++) {
                   if (i != tablaUnidades.selectedIndex) {
                       listaNueva[j++] = listaUors[i];
                   }
               }
               listaUors = listaNueva;
               mostrarListaTabla();
               pulsarLimpiar();
           } else {
               tablaUnidades.selectedIndex = -1;
               pulsarLimpiar();
           }
         } else {
           jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
         }
       } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }

    function pulsarSalir() {
      self.parent.opener.retornoXanelaAuxiliar(listaUors);
    }

    function onClickHrefUnidadInicio() {
        var datos;
        var argumentos = new Array();
        argumentos[0] = document.forms[0].codVisibleUnidadInicio.value;
        var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalUOR" +
                        "&codOrganizacion=" + <%=codOrg%> + "&codEntidad=" + <%=codEnt%>;

        abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=750,height=450',function(datos){
                            if(datos != null) {
                                document.forms[0].codUnidadInicio.value = datos[2];
                                document.forms[0].descUnidadInicio.value = datos[1];
                                document.forms[0].codVisibleUnidadInicio.value = datos[0];
                            }
                            if((document.forms[0].codVisibleUnidadInicio.value != '') && (document.forms[0].descUnidadInicio.value == '')) {
                                document.forms[0].codUnidadInicio.value = '';
                                document.forms[0].codVisibleUnidadInicio.value = '';
                            }
                    });
}

    function mostrarListaUnidadInicio(){
        var condiciones = new Array();
        condiciones[0]='UOR_NO_VIS'+'зе';
        condiciones[1]='0';
        condiciones[2]='UOR_OCULTA'+'зе';
        condiciones[3]='N';
        muestraListaTabla('UOR_COD_VIS','UOR_NOM','A_UOR',condiciones,'codVisibleUnidadInicio','descUnidadInicio', 'botonUnidadInicio','100');
    }

    function onClickDescUnidadInicio(){
            divSegundoPlano=false;
            mostrarListaUnidadInicio();
    }

    function onchangeCodUnidadInicio() {
            var uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codVisibleUnidadInicio.value.toUpperCase(), 'A'); 
            if(uor != null) {
                document.forms[0].codUnidadInicio.value = uor.uor_cod;
                document.forms[0].descUnidadInicio.value = uor.uor_nom;
            }
            else { // ha dado null para alta, buscamos de baja
                uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codVisibleUnidadInicio.toUpperCase(), 'B');
                if(uor != null) {
                    document.forms[0].codUnidadInicio.value = uor.uor_cod;
                    document.forms[0].descUnidadInicio.value = uor.uor_nom;
                }
            }
            if(uor == null) {
                document.forms[0].codUnidadInicio.value = '';
                document.forms[0].descUnidadInicio.value = '';
                document.forms[0].codVisibleUnidadInicio.value = '';
            }
    }


</script>
</head>

<body class="bandaBody" onload="inicializar();">
    <form name="formulario" method="post">

    <html:hidden  property="tipo_select" value=""/>
    <html:hidden  property="col_cod" value=""/>
    <html:hidden  property="col_desc" value=""/>
    <html:hidden  property="nom_tabla" value=""/>
    <html:hidden  property="input_cod" value=""/>
    <html:hidden  property="input_desc" value=""/>
    <html:hidden  property="column_valor_where" value=""/>
    <input  type="hidden"  name="opcion" id="opcion">

    <div id="titulo" class="txttitblanco">&nbsp;<%=descriptor.getDescripcion("titListadoUtrs")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td id="tabla"></td>
            </tr>
            <tr>
                <TD class="columnP">
                    <input type="hidden" name="codUnidadInicio" id="codUnidadInicio"/>
                    <input type=text class="inputTextoObligatorio" name="codVisibleUnidadInicio" style="width:20%" onkeypress="javascript:PasaAMayusculas(event);" onfocus="javascript:this.select();" onchange="javascript:{onchangeCodUnidadInicio();}"/>
                    <input type=text class="inputTextoObligatorio" name="descUnidadInicio" style="width:72%" readonly="true" onclick="javascript:{onClickDescUnidadInicio();}"/>
                    <A href="javascript:{onClickHrefUnidadInicio();}" style="text-decoration:none;" id="anclaD2" name="anchorUnidadInicio" >
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonUnidadInicio" style="cursor:hand;"></span>
                    </A>
                </TD>
            </tr>
        </table>
        <DIV class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta" onclick="pulsarAlta();" accesskey="A">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificar" onclick="pulsarModificar();" accesskey="M">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminar" onclick="pulsarEliminar();" accesskey="E">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiar" onclick="pulsarLimpiar();" accesskey="L">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onclick="pulsarSalir();" accesskey="S">
        </DIV>
    </DIV>
</form>

    <script language="JavaScript1.2">
      var tablaUnidades = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

      tablaUnidades.addColumna('150','center','<%=descriptor.getDescripcion("gEtiq_codigo")%>');
      tablaUnidades.addColumna('600','center','<%=descriptor.getDescripcion("gEtiq_desc")%>');
      tablaUnidades.displayCabecera=true;

      tablaUnidades.displayDatos = pintaDatos;

      function refresca() {
        tablaUnidades.displayTabla();
      }

      function pintaDatos(rowID){
        var i = tablaUnidades.selectedIndex;
        if(i != -1) {
          document.forms[0].codVisibleUnidadInicio.value = listaTabla[i][0];
          document.forms[0].descUnidadInicio.value = listaTabla[i][1];
        } else {
          document.forms[0].codVisibleUnidadInicio.value = "";
          document.forms[0].descUnidadInicio.value = "";
        }
      }

      document.onmouseup = checkKeys;
      function checkKeysLocal(event,tecla){
        if(window.event)
            event = window.event;

        var teclaAuxiliar;
        if(window.event)
            teclaAuxiliar  =event.keyCode;
        else
            teclaAuxiliar  =event.which;

        keyDel(event);

        if ((event.button == 1)||(event.button == 2)){
          if(layerVisible) setTimeout("ocultarDiv()",30);
          if(divSegundoPlano)	divSegundoPlano = false;
        }
        if (teclaAuxiliar == 1){
            if(layerVisible) setTimeout('ocultarDivNoFocus()',40);
            if(divSegundoPlano)	divSegundoPlano = false;
        }
        if (teclaAuxiliar == 9){
            if(layerVisible) ocultarDiv();
            if(divSegundoPlano) divSegundoPlano = false;
            return false;
        }
        
        //if (event.keyCode == 38 || event.keyCode == 40) upDownTable(tablaUnidades,listaTabla);
        if (teclaAuxiliar == 38 || teclaAuxiliar == 40) upDownTable(tablaUnidades,listaTabla,teclaAuxiliar);
      }
    </script>

    <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</body>
</html>

