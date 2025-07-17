<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES  Consulta de Expedientes:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%int idioma=1;
  int apl=1;
  int munic = 0;
  String soloConsulta = "no";
  if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
		  soloConsulta = usuario.getSoloConsultarExp();
      }
  }
  String municipio = Integer.toString(munic);
  String aplicacion = Integer.toString(apl);
  Config m_Config = ConfigServiceHelper.getConfig("common");
  String statusBar = m_Config.getString("JSP.StatusBar");%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
<SCRIPT type="text/javascript">
var cod_procedimiento = new Array();
var desc_procedimiento = new Array();
var uors_procedimiento = new Array();
var cod_tramite = new Array();
var cod_oculto_tramite = new Array();
var desc_tramite = new Array();
var cod_utr = new Array();
var cod_oculto_utr = new Array();
var desc_utr = new Array();
var listaE = new Array();
var listaOriginalE = new Array();

function inicializar() {
    consultando = true;
    var cont = 0;
    var cont1 = 0;
    <logic:iterate id="elemento" name="RelacionExpedientesForm" property="listaProcedimientos">
        cod_procedimiento[cont] = '<bean:write name="elemento" property="txtCodigo"/>';
        desc_procedimiento[cont] = '<bean:write name="elemento" property="txtDescripcion"/>';      
        cont++;
    </logic:iterate>
    borrarDatos();
    cargarCombos();
    refrescaExpedientes();
    var vectorBotones = new Array(document.forms[0].cmdCrearRelacion);
    deshabilitarGeneral(vectorBotones);
}

function marcarDesmarcar(){ 
  var table = $('#tb_tabla0').DataTable();
  if(document.forms[0].checkTodos.checked){
    $(':checkbox', table.rows().nodes()).prop('checked', true); 
  }else{
     $(':checkbox', table.rows().nodes()).prop('checked', false); 
  }
} 

function desmarcar(){
    for (var i=0;i<listaE.length;i++) { 
          if (!(eval("document.forms[0].exp"+i+".checked"))) {
             document.forms[0].checkTodos.checked = false;  
          }
    }
}

function pulsarCancelar() {
    borrarDatos();
    cargarCombos();
    refrescaExpedientes();
    var vectorBotones = new Array(document.forms[0].cmdCrearRelacion);
    deshabilitarGeneral(vectorBotones);
    vectorBotones = new Array(document.forms[0].cmdBuscar);
    habilitarGeneral(vectorBotones);
    //comboProcedimiento.activate();
    //comboTramite.activate();
}

function pulsarBuscar() {
    pleaseWait('on');
    if(document.forms[0].codProcedimiento.value != "" && document.forms[0].codTramite.value != "" 
        && document.forms[0].codUtr.value != "") {

        document.getElementById('checkTodosOculto').style.visibility='visible'; 
        document.getElementById('etMarcarOculto').style.visibility='visible';        
        
        for (var i=0;i<cod_tramite.length;i++) {
            if (cod_tramite[i]==document.forms[0].codTramite.value) {
                document.forms[0].codOcultoTramite.value=cod_oculto_tramite[i]
            }
        }
        
        for (var i=0;i<cod_utr.length;i++) {
            if (cod_utr[i]==document.forms[0].codUtr.value) {
                document.forms[0].codOcultoUtr.value=cod_oculto_utr[i]
            }
        }
        
        pleaseWait('on');
        document.forms[0].opcion.value="buscarExpedientes";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
        document.forms[0].submit();
        var vectorBotones = new Array(document.forms[0].cmdCrearRelacion);
        habilitarGeneral(vectorBotones);
        vectorBotones = new Array(document.forms[0].cmdBuscar);
        deshabilitarGeneral(vectorBotones);
        //comboProcedimiento.deactivate();
        //comboTramite.deactivate();
       
    } else {
        pleaseWait('off');
      jsp_alerta("A",'<%=descriptor.getDescripcion("msjBuscarExpedientes")%>');
    }
}

function crearListasExpedientes(listaExpSel) {
  var listaMunExp = "";
  var listaProExp = "";
  var listaEjeExp = "";
  var listaNumExp = "";
  for (i=0; i < listaExpSel.length; i++) {
    listaMunExp +=listaExpSel[i][0]+'зе';
    listaProExp +=listaExpSel[i][1]+'зе';
    listaEjeExp +=listaExpSel[i][3]+'зе';
    listaNumExp +=listaExpSel[i][4]+'зе';
  }
  var listasExpRet = new Array();
  listasExpRet = [listaMunExp,listaProExp,listaEjeExp,listaNumExp];
  return listasExpRet;
}

function pulsarCrearRelacion() {
  listaExpSelAux = new Array();
  var table = $('#tb_tabla0').DataTable();
  
  var checkboxes = $(":checkbox:checked", table.rows().nodes());
  var j=0;
  for (var i=0; i<checkboxes.length; i++){
      listaExpSelAux[j++]=listaOriginalE[checkboxes[i].id];
  }
  if (listaExpSelAux.length!=0) {
    if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjIniRelExp")%>') ==1) { 
    
      /* ini Comprobar los bloqueos de los expedientes seleccionados */
      var cont=0;
      for (var i=0;i< listaExpSelAux.length; i++) {
          if (listaExpSelAux[i][9] == "ON") { cont = cont + 1; }
      }
      if ((cont != listaExpSelAux.length) && (cont>0)) {
          if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjDeshacerBlqRel")%>') ==1) {
              document.forms[0].bloqueo.value="1";
          } else {
              document.forms[0].bloqueo.value="0";
          }
      }
      /* fin Comprobar los bloqueos de los expedientes seleccionados */

  if (uors.length > 0 ) {
    if (uors.length == 1){
        varUOR = uors[0][2];
        if (varUOR != null) {
            document.forms[0].codUOR.value = varUOR;
            var listasExp = crearListasExpedientes(listaExpSelAux);
            document.forms[0].listaMunExped.value = listasExp[0];
            document.forms[0].listaProExped.value = listasExp[1];
            document.forms[0].listaEjeExped.value = listasExp[2];
            document.forms[0].listaNumExped.value = listasExp[3];
            document.forms[0].opcion.value="iniciar";
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
            document.forms[0].submit();
          }
        
    } else {
      var source = "<c:url value='/jsp/sge/listaUORsUsuario.jsp'/>?tipoSel=1";
      abrirXanelaAuxiliar(source,uors,
	'width=600,height=565,scrollbars=no,status='+ '<%=statusBar%>',function(codUOR){
                        if (codUOR != undefined) {
                            if (codUOR != null) {
                                document.forms[0].codUOR.value = codUOR;
                                var listasExp = crearListasExpedientes(listaExpSelAux);
                                document.forms[0].listaMunExped.value = listasExp[0];
                                document.forms[0].listaProExped.value = listasExp[1];
                                document.forms[0].listaEjeExped.value = listasExp[2];
                                document.forms[0].listaNumExped.value = listasExp[3];
                                document.forms[0].opcion.value="iniciar";
                                document.forms[0].target="oculto";
                                document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
                                document.forms[0].submit();
                            }
                        }
                  });
                  }
          }       
  } else jsp_alerta("A",'<%=descriptor.getDescripcion("msjPrevIniRelExp")%>');
}
}

function recibirNumero(datos) {
  document.forms[0].numero.value = datos[0];
  document.forms[0].ejercicio.value = datos[1];
  document.forms[0].codMunicipio.value = datos[3];
  if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjIniRelExp1")%>' + " "+ "<%=m_Config.getString("constante.relacion")%>" + document.forms[0].numero.value + " " + '<%=descriptor.getDescripcion("msjIniRelExp2")%>') == 1) {
      pleaseWait('on');
      document.forms[0].opcion.value="cargar";
      document.forms[0].target="mainFrame";
      document.forms[0].action="<c:url value='/sge/FichaRelacionExpedientes.do'/>";
      document.forms[0].submit();
  } else {
      pleaseWait('on');
      document.forms[0].opcion.value="pendientes";
      document.forms[0].target="mainFrame";
      document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
      document.forms[0].submit();
  }
}

function borrarDatos() {
  document.getElementById('checkTodosOculto').style.visibility='hidden'; 
  document.getElementById('etMarcarOculto').style.visibility='hidden';
  comboProcedimiento.selectItem("-1");
  comboTramite.selectItem("-1");
  listaExp = new Array();
  tabExp.lineas = listaExp;
  listaE = new Array();
  listaOriginalE = new Array();
  refrescaExpedientes();
}

function cargarCombos() {
  comboProcedimiento.addItems(cod_procedimiento,desc_procedimiento);
}
</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include> 

<html:form action="/sge/RelacionExpedientes.do" target="_self">
<html:hidden property="opcion" value=""/>
<html:hidden property="numero" value=""/>
<html:hidden property="ejercicio" value=""/>
<html:hidden property="codMunicipio" value=""/>
<html:hidden property="codOcultoTramite" value=""/>
<html:hidden property="codOcultoUtr" value=""/>
<html:hidden property="codUOR" value=""/>
<html:hidden property="listaMunExped" value=""/>
<html:hidden property="listaProExped" value=""/>
<html:hidden property="listaEjeExped" value=""/>
<html:hidden property="listaNumExped" value=""/>
<html:hidden property="bloqueo" value="-"/>

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_crearRelExp")%></div>
<div class="contenidoPantalla">						
    <TABLE id ="tablaDatosGral">
        <tr>
            <td style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProc")%>:</td>
            <td valign="top" class="columnP">
                <html:text styleId="codProcedimiento" property="codProcedimiento" onkeyup=" return xAMayusculas(this);" styleClass="inputTextoObligatorio" size="8"/>
                <html:text styleId="descProcedimiento" property="descProcedimiento" styleClass="inputTextoObligatorio"  size="114" readonly="true" style="width:85%"/>
                <A href="" id="anchorProcedimiento" name="anchorProcedimiento" style="text-decoration:none;" >
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonProcedimiento" name="botonProcedimiento" style="cursor:hand; border: 0px none"></span>
                </A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_tramite")%>:</td>
            <td valign="top" class="columnP">
                <html:text styleId="codTramite" property="codTramite" onkeyup=" return xAMayusculas(this);" styleClass="inputTextoObligatorio" size="8"/>
                <html:text styleId="descTramite" property="descTramite" styleClass="inputTextoObligatorio"  size="114" readonly="true" style="width:85%"/>
                <A href="" id="anchorTramite" name="anchorTramite" style="text-decoration:none;" >
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTramite" name="botonTramite" style="cursor:hand; border: 0px none"></span>
                </A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidTramit")%>:</td>
            <td valign="top" class="columnP">
                <html:text styleId="codUtr" property="codUtr" onkeyup=" return xAMayusculas(this);" styleClass="inputTextoObligatorio" size="8"/>
                <html:text styleId="descUtr" property="descUtr" styleClass="inputTextoObligatorio"  size="114" readonly="true" style="width:85%"/>
                <A href="" id="anchorUtr" name="anchorUtr" style="text-decoration:none;" >
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonUtr" name="botonUtr" style="cursor:hand; border: 0px none"></span>
                </A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta" id="etMarcarOculto" style="visibility:hidden"> Marcar/Desmarcar: </td>
            <td valign="top" class="columnP">
                <input type="checkbox" id="checkTodosOculto" name="checkTodos"  style="visibility:hidden" value="true" onclick="marcarDesmarcar();" >
            </td>
        </tr>
        <tr>
            <td  id="tablaExpedientes" colspan="2">                                                            
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center" valign="top">
                <div id="resultadoBuscar" STYLE="position: relative; width:100%;"></div>
            </td>
        </tr>
    </table>
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" accesskey="C" value="<%=descriptor.getDescripcion("gbBuscar")%>" name="cmdBuscar"  onclick="pulsarBuscar();">
        <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbCrearRelacion")%>" name="cmdCrearRelacion"  onclick="pulsarCrearRelacion();">
        <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdCancelar"  onclick="pulsarCancelar();">
    </DIV>
</div>
</html:form>
<script type="text/javascript">
document.onmouseup = checkKeys;

var comboProcedimiento= new Combo("Procedimiento");
var comboTramite= new Combo("Tramite");
var comboUor= new Combo("Utr");
var uors = new Array();

function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
}

// JAVASCRIPT DE LA TABLA EXPEDIENTES
var tabExp = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaExpedientes'));

tabExp.addColumna('170','left','<%= descriptor.getDescripcion("etiq_numExp")%>');
tabExp.addColumna('250','left','<%= descriptor.getDescripcion("etiq_IntPrin")%>');
tabExp.addColumna('250','center','<%= descriptor.getDescripcion("gEtiq_Asunto")%>');
tabExp.addColumna('170','center','<%= descriptor.getDescripcion("gbCrearRelacion")%>');
tabExp.addColumna('40','center','');
tabExp.displayCabecera=true;

var tableObject=tabExp;


   function refrescaExpedientes(numFilas) {
        if(numFilas){
            var texto = '&nbsp;' + listaE.length  + '&nbsp;encontrados.';
            if(numFilas!="all" && listaE.length>=numFilas)
            texto+=' <span style="color:red">(Filtrado, mostrando las '+numFilas+' primeras filas.)</span>';
            var htmlString = '<center><font class="textoSuelto">'+texto+'</font></center>';
            domlay('resultadoBuscar', 1, 0, 0, htmlString);
        } else {
            if(document.getElementById('resultadoBuscar').innerHTML.length) 
                domlay('resultadoBuscar', 1, 0, 0, "");
        }
        tabExp.displayTabla();
}

tabExp.displayDatos = pintaDatosExpedientes;

function pintaDatosExpedientes() {
  tableObject = tabExp;
}


// FIN DE LOS JAVASCRIPT'S DE LA TABLA EXPEDIENTES
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
  var teclaAuxiliar = "";
  if(window.event){
      evento = window.event;
      teclaAuxiliar = evento.keyCode;
  }else teclaAuxiliar = evento.which;

  //if(event.keyCode == 40){
  if(teclaAuxiliar == 40){
    if(tabExp==tableObject) {
      upDownTable(tabExp,listaE,teclaAuxiliar);
    }
  }

  //if (event.keyCode == 38){
  if (teclaAuxiliar == 38){
    if(tabExp==tableObject) {
      upDownTable(tabExp,listaE,teclaAuxiliar);
    }
  }

  if (teclaAuxiliar == 1){
      if (comboProcedimiento.base.style.visibility == "visible" && isClickOutCombo(comboProcedimiento,coordx,coordy)) setTimeout('comboProcedimiento.ocultar()',20);
      if (comboTramite.base.style.visibility == "visible" && isClickOutCombo(comboTramite,coordx,coordy)) setTimeout('comboTramite.ocultar()',20);
      if (comboUor.base.style.visibility == "visible" && isClickOutCombo(comboUor,coordx,coordy)) setTimeout('comboUor.ocultar()',20);
  }
  if (teclaAuxiliar == 9){
      comboProcedimiento.ocultar();
      comboTramite.ocultar();
      comboUor.ocultar();
  }

  keyDel(evento);
}

comboProcedimiento.change = function() {
    var vectorBotones = new Array(document.forms[0].cmdCrearRelacion);
    deshabilitarGeneral(vectorBotones);
    vectorBotones = new Array(document.forms[0].cmdBuscar);
    habilitarGeneral(vectorBotones);

    comboTramite.selectItem("-1");
    listaExp = new Array();
    tabExp.lineas = listaExp;
    listaE = new Array();
    listaOriginalE = new Array();
    refrescaExpedientes();

    document.forms[0].codMunicipio.value = <%=municipio%>;
    document.forms[0].opcion.value="cargarTramites";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
    document.forms[0].submit();
}

comboTramite.change = function() {
    var vectorBotones = new Array(document.forms[0].cmdCrearRelacion);
    deshabilitarGeneral(vectorBotones);
    vectorBotones = new Array(document.forms[0].cmdBuscar);
    habilitarGeneral(vectorBotones);

    comboUor.selectItem("-1");
    listaExp = new Array();
    tabExp.lineas = listaExp;
    listaE = new Array();
    listaOriginalE = new Array();
    refrescaExpedientes();
    
    for (var i=0;i<cod_tramite.length;i++) {
        if (cod_tramite[i]==document.forms[0].codTramite.value) {
            document.forms[0].codOcultoTramite.value=cod_oculto_tramite[i]
        }
    }
    
    document.forms[0].codMunicipio.value = <%=municipio%>;
    document.forms[0].opcion.value="cargarUnidadesTramitadoras";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
    document.forms[0].submit();
}
</script>

<div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>
</BODY>
</html:html>
