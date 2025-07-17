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
  apl = usuarioVO.getAppCod();
  idioma = usuarioVO.getIdioma();
  codOrg = usuarioVO.getOrgCod();
  codEnt = usuarioVO.getEntCod();
}

String soloConsulta = "";
if (session.getAttribute("soloConsulta") != null) {
soloConsulta = (String) session.getAttribute("soloConsulta");
session.removeValue("soloConsulta");
}
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Lista de Unidades de Inicio</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
    <script type="text/javascript">

    // === UORs
    var uors = new Array();
    var uorcods = new Array();
    <%
        DefinicionProcedimientosForm defProc = (DefinicionProcedimientosForm)session.getAttribute("DefinicionProcedimientosForm");
        Vector listaUORDTOs = defProc.getListaUnidadInicio();
            for (int j=0; j<listaUORDTOs.size(); j++) {
                UORDTO dto = (UORDTO)listaUORDTOs.get(j);%>
                // array con los objetos tipo uor mapeados por el array de arriba
                uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
                // array con los códigos visibles
                uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
        <%}
    %>
    // === fin seccion UORs
    var codUnidadInicio = new Array();
    var descUnidadInicio = new Array();

    var lista = new Array();
    var listaOriginal = new Array();
    var cont = 0;

    function inicializar() {
        top.focus();
         // #265897: se recuperan los parametros de abrirXanelaAuxiliar y se rellena la tabla con ellos	
        var argVentana = self.parent.opener.xanelaAuxiliarArgs;
        var listaCods = arrayFromString(argVentana[0]);
        var listaCodsVis = arrayFromString(argVentana[1]);	
        var listaDescs = arrayFromString(argVentana[2]);	
        	
        if(listaCods!=null && listaCodsVis!=null && listaDescs!=null	
                && listaCods.length==listaCodsVis.length && listaCods.length==listaDescs.length){	
            for(var j=0; j<listaCods.length; j++){	
                listaOriginal[j] = [listaCods[j],listaDescs[j]];	
                lista[j] = [listaCodsVis[j],listaDescs[j]];	
            }	
        }
        tablaUnidades.lineas=lista;
        refresca();
        <% if("si".equals(soloConsulta)) { %>
            desactivarBotones();
        <% } %>
    }
    
    function arrayFromString(strDatos){
        var separador = "§¥";	
        var lista = null;
        if(strDatos!=null && strDatos!=""){	
           var listaAux = strDatos.split(separador);	
           lista = new Array();	
           for(var index=0; index<listaAux.length; index++){	
               if(index<listaAux.length-1 || listaAux[index]!="")	
                   lista.push(listaAux[index]);	
           }	
        }	
        	
        return lista;	
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
      var codUnidad = document.forms[0].codVisibleUnidadInicio.value;
      var yaExiste = 0;
      if(validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
        for(l=0; l < lista.length; l++){
        if ((lista[l][0]) == codUnidad){
            yaExiste = 1;
          }
        }
        if(yaExiste == 0) {
          var lineas = tablaUnidades.lineas;
          for (i=0; i < lineas.length; i++) {
          }
          listaOriginal[i]=[document.forms[0].codUnidadInicio.value,document.forms[0].descUnidadInicio.value];
          lista[i]=[document.forms[0].codVisibleUnidadInicio.value,document.forms[0].descUnidadInicio.value];
          tablaUnidades.lineas=lista;
          refresca();
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
      var codUnidad = document.forms[0].codVisibleUnidadInicio.value;
      var yaExiste = 0;
      if(tablaUnidades.selectedIndex != -1) {
        if (validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
          for(l=0; l < lista.length; l++){
            var lineaSeleccionada;
            lineaSeleccionada = tablaUnidades.selectedIndex;
            if(l == lineaSeleccionada) {
              l= l;
            } else {
              if ((lista[l][0]) == codUnidad ){
                yaExiste = 1;
              }
            }
          }
        if(yaExiste == 0) {
            var j = tablaUnidades.selectedIndex;
            listaOriginal[j]=[document.forms[0].codUnidadInicio.value,document.forms[0].descUnidadInicio.value];
            lista[j]=[document.forms[0].codVisibleUnidadInicio.value,document.forms[0].descUnidadInicio.value];
            tablaUnidades.lineas=lista;
            refresca();
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
           if(jsp_alerta('', '<%=descriptor.getDescripcion("desEliminarUIN")%>')) {
           var list = new Array();
           var list2 = new Array();
           tamIndex=tablaUnidades.selectedIndex;
           tamLength=tablaUnidades.lineas.length;
           for (i=tamIndex - 1; i < lista.length - 1; i++){
             if (i + 1 <= lista.length - 2){
               lista[i + 1]=lista[i + 2];
               listaOriginal[i + 1]=listaOriginal[i + 2];
             }
           }
           for(j=0; j < lista.length-1 ; j++){
             list[j] = lista[j];
             list2[j] = listaOriginal[j];
           }
           tablaUnidades.lineas=list;
           refresca();
           pulsarLimpiar();
           lista=list;
           listaOriginal=list2;
           } else {
             tablaUnidades.selectLinea(tablaUnidades.selectedIndex);
             tablaUnidades.selectedIndex = -1;
             pulsarLimpiar();
           }
         } else {
           jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosDoc")%>');
         }
       } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }

    function crearListas() {
      var listaCodUnidadesInicio = "";
      var listaCodVisibleUnidadesInicio = "";
      var listaDescUnidadesInicio= "";
      var vectorUnidades = new Array();
      for (i=0; i < lista.length; i++) {
        listaCodUnidadesInicio += listaOriginal[i][0]+'§¥';
        listaCodVisibleUnidadesInicio += lista[i][0]+'§¥';
        listaDescUnidadesInicio += lista[i][1]+'§¥';
        vectorUnidades[i]=[lista[i][0],lista[i][1]];
      }
      var listas = new Array();
      listas = [listaCodUnidadesInicio,listaDescUnidadesInicio,vectorUnidades,listaCodVisibleUnidadesInicio];
      return listas;
    }

    function pulsarSalir() {
      var listas = crearListas();
      var listaCodigos = listas[0];
      var listaVisibleCodigos = listas[3];
      var listaDescripciones = listas[1];
      var vectorUnidades = listas[2];

      var codPrimero = "";
      var descPrimero = "";
      if(lista.length >0) {
        codPrimero = lista[0][0];
        descPrimero = lista[0][1];
      }
      var retorno = new Array();
      retorno = [listaCodigos,listaDescripciones,codPrimero,descPrimero,vectorUnidades,listaVisibleCodigos];
      self.parent.opener.retornoXanelaAuxiliar(retorno);
    }


    function onClickHrefUnidadInicio() {
        var argumentos = new Array();
        argumentos[0] = document.forms[0].codVisibleUnidadInicio.value;
        var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalUOR" +
                        "&codOrganizacion=" + <%=codOrg%> + "&codEntidad=" + <%=codEnt%>;

        abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=750,height=490',function(datos){
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
        condiciones[0]='UOR_NO_VIS'+'§¥';
        condiciones[1]='0';
        condiciones[2]='UOR_OCULTA'+'§¥';
        condiciones[3]='N';
        muestraListaTabla('UOR_COD_VIS','UOR_NOM','A_UOR',condiciones,'codVisibleUnidadInicio','descUnidadInicio', 'botonUnidadInicio','100');
    }

    function onClickDescUnidadInicio(){
            divSegundoPlano=false;
            mostrarListaUnidadInicio();
    }


    function onchangeCodUnidadInicio() {
        //if (!contieneOperadoresConsulta(document.forms[0].codUnidadesOrganicas,operadorConsulta)) {
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
        //}
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

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titListUnidIni")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td id="tabla"></td>
            </tr>
            <tr>
                <TD class="columnP">
                    <input type="hidden" name="codUnidadInicio" id="codUnidadInicio"/>
                    <input type=text class="inputTextoObligatorio" name="codVisibleUnidadInicio" style="width:20%" onkeypress="javascript:xAMayusculas(this);" onfocus="javascript:this.select();" onchange="javascript:{onchangeCodUnidadInicio();}"/>
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
    </div>
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
          document.forms[0].codVisibleUnidadInicio.value = lista[i][0];
          document.forms[0].descUnidadInicio.value = lista[i][1];
        } else {
          document.forms[0].codVisibleUnidadInicio.value = "";
          document.forms[0].descUnidadInicio.value = "";
        }
      }

        document.onmouseup = checkKeys;
      function checkKeysLocal(event,tecla){
        var aux=null;
        if(window.event)
            aux = window.event;
        else
            aux = event;

        var teclaAux = 0;
        if(aux.keyCode)
            teclaAux = aux.keyCode;
        else
            teclaAux = aux.which;

        keyDel(aux);
        //if ((event.button == 1)||(event.button == 2)){
        if ((aux.button == 1)||(aux.button == 2)){
          if(layerVisible) setTimeout("ocultarDiv()",30);
          if(divSegundoPlano)	divSegundoPlano = false;
        }

          if (teclaAux == 38 || teclaAux == 40) upDownTable(tablaUnidades,lista,teclaAux);
        //  if (event.keyCode == 38 || event.keyCode == 40) upDownTable(tablaUnidades,lista);
         if (teclaAux == 1){
            if(layerVisible) setTimeout("ocultarDiv()",30);
            if(divSegundoPlano)	divSegundoPlano = false;
         }
          if (teclaAux == 9){

            if(layerVisible) ocultarDiv();
            if(divSegundoPlano) divSegundoPlano = false;
          }
      }
    </script>
    <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</body>
</html>
