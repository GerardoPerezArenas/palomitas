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
  String statusBar = m_Config.getString("JSP.StatusBar");
  String userAgent = request.getHeader("user-agent");
  %>

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
<SCRIPT type="text/javascript">
var cod_procedimiento = new Array();
var desc_procedimiento = new Array();
var uors_procedimiento = new Array();
var cod_tramite = new Array();
var cod_oculto_tramite = new Array();
var desc_tramite = new Array();
var listaE = new Array();
var listaOriginalE = new Array();
var uors = new Array();

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
  refrescaRelaciones();
  var vectorBotones = new Array(document.forms[0].cmdDeshacerRelacion);
  deshabilitarGeneral(vectorBotones);
}

function pulsarCancelar() {
    borrarDatos();
    cargarCombos();
    refrescaRelaciones();
    var vectorBotones = new Array(document.forms[0].cmdDeshacerRelacion);
    deshabilitarGeneral(vectorBotones);
    vectorBotones = new Array(document.forms[0].cmdBuscar);
    habilitarGeneral(vectorBotones);
    //comboProcedimiento.activate();
    //comboTramite.activate();
}

function pulsarBuscar() {
    if(document.forms[0].codProcedimiento.value != "") {
        document.forms[0].codOcultoTramite.value="";
        for (var i=0;i<cod_tramite.length;i++) {
            if (cod_tramite[i]==document.forms[0].codTramite.value) {
                document.forms[0].codOcultoTramite.value=cod_oculto_tramite[i]
            }
        }
        pleaseWait('on');
        document.forms[0].opcion.value="buscarRelaciones";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
        document.forms[0].submit();
        var vectorBotones = new Array(document.forms[0].cmdDeshacerRelacion);
        habilitarGeneral(vectorBotones);
        vectorBotones = new Array(document.forms[0].cmdBuscar);
        deshabilitarGeneral(vectorBotones);
    } else {
      jsp_alerta("A",'<%=descriptor.getDescripcion("msjBuscarRelaciones")%>');
    }
}

function pulsarDeshacerRelacion() {
    if(tabRel.selectedIndex != -1) {
        var j = tabRel.selectedIndex;
        if (listaOriginalE[j][8]=="") {
            document.forms[0].numero.value=listaOriginalE[j][3];
            document.forms[0].codMunicipio.value=listaOriginalE[j][0];
            document.forms[0].ejercicio.value=listaOriginalE[j][2];
            if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjDeshacerRelExp")%>') ==1) {
              if (uors.length > 0 ) {
                    if (uors.length == 1){
                          if (uors[0][2] != undefined &&  uors[0][2] != null) {
                              if (listaE[j][5] != "") {
                                  if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjDeshacerBlqRel")%>') ==1) {
                                      document.forms[0].bloqueos.value="1";
                                  } else {
                                      document.forms[0].bloqueos.value="0";
                                  }
                              } else {
                                  document.forms[0].bloqueos.value="0";
                              }
                              document.forms[0].codUOR.value = uors[0][2];
                              document.forms[0].opcion.value="deshacer";
                              document.forms[0].target="oculto";
                              document.forms[0].action="<c:url value='/sge/FichaRelacionExpedientes.do'/>";
                              document.forms[0].submit();
                          }
                    } else {
                      var source = "<c:url value='/jsp/sge/listaUORsUsuario.jsp'/>";
                      abrirXanelaAuxiliar(source,uors,
                        'width=600,height=565,scrollbars=no,status='+ '<%=statusBar%>',function(codUOR){
                                  if (codUOR != undefined &&  codUOR != null) {
                                      if (listaE[j][5] != "") {
                                          if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjDeshacerBlqRel")%>') ==1) {
                                              document.forms[0].bloqueos.value="1";
                                          } else {
                                              document.forms[0].bloqueos.value="0";
                                          }
                                      } else {
                                          document.forms[0].bloqueos.value="0";
                                      }
                                      document.forms[0].codUOR.value = codUOR;
                                      document.forms[0].opcion.value="deshacer";
                                      document.forms[0].target="oculto";
                                      document.forms[0].action="<c:url value='/sge/FichaRelacionExpedientes.do'/>";
                                      document.forms[0].submit();

                                  }
                              });
                    }
              }
            }
        } else {
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjDesRelExpBloq")%>');
        }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function mostrar(respOpcion) {
    if (respOpcion == "grabado") {
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjRelExpCerrada")%>');
    listaExp = new Array();
    tabRel.lineas = listaExp;
    listaE = new Array();
    listaOriginalE = new Array();
    refrescaRelaciones();
    pulsarBuscar();
    } else if (respOpcion == "noGrabado"){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjDesRelExpNoGrab")%>');
    } else if (respOpcion == "bloqueado"){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjDesRelExpBloq")%>');
    }
}

function borrarDatos() {
  comboProcedimiento.selectItem("-1");
  comboTramite.selectItem("-1");
  listaExp = new Array();
  tabRel.lineas = listaExp;
  listaE = new Array();
  listaOriginalE = new Array();
  refrescaRelaciones();
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
<html:hidden property="codUOR" value=""/>
<html:hidden property="listaMunExped" value=""/>
<html:hidden property="listaProExped" value=""/>
<html:hidden property="listaEjeExped" value=""/>
<html:hidden property="listaNumExped" value=""/>
<html:hidden property="bloqueos" value=""/>

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_deshacerRelExp")%></div>
<div class="contenidoPantalla">
    <TABLE id ="tablaDatosGral"  cellspacing="5px" cellpadding="0px" border="0px">
        <tr>
            <td style="width: 10%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProc")%>:</td>
            <td valign="top" class="columnP">
                <html:text styleId="codProcedimiento" property="codProcedimiento"
                           onkeyup="return xAMayusculas(this);"
                           styleClass="inputTextoObligatorio" size="8"/>
                <html:text styleId="descProcedimiento" property="descProcedimiento"
                           styleClass="inputTextoObligatorio"  size="114" style="width:85%" readonly="true"/>
                <A href="" id="anchorProcedimiento" name="anchorProcedimiento" style="text-decoration:none;" >
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonProcedimiento" name="botonProcedimiento" style="cursor:hand; border: 0px none"/>
                </A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"></span><%=descriptor.getDescripcion("gEtiq_tramite")%>:</td>
            <td valign="top" class="columnP">
                <html:text styleId="codTramite" property="codTramite"
                           onkeyup="return xAMayusculas(this);"
                           styleClass="inputTexto" size="8"/>
                <html:text styleId="descTramite" property="descTramite"
                           styleClass="inputTexto"  size="114" style="width:85%" readonly="true"/>
                <A href="" id="anchorTramite" name="anchorTramite" style="text-decoration:none;" >
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTramite" name="botonTramite" style="cursor:hand; border: 0px none"/>
                </A>
            </td>
        </tr>
        <tr>
            <td colspan="2" id="tablaRelaciones"></span>                                                            
            </td>                                                        
        </tr>
    </table>
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" accesskey="C" value="<%=descriptor.getDescripcion("gbBuscar")%>" name="cmdBuscar"  onclick="pulsarBuscar();">
        <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbDeshacerRelacion")%>" name="cmdDeshacerRelacion"  onclick="pulsarDeshacerRelacion();">
        <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar"  onclick="pulsarCancelar();">
    </DIV>
</div>
</html:form>
<script type="text/javascript">
document.onmouseup = checkKeys;
var comboProcedimiento= new Combo("Procedimiento");
var comboTramite= new Combo("Tramite");

function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
}

// JAVASCRIPT DE LA TABLA EXPEDIENTES
var tabRel = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaRelaciones'));

tabRel.addColumna('185','center','<%= descriptor.getDescripcion("etiq_numRel")%>');
tabRel.addColumna('100','center','<%= descriptor.getDescripcion("etiqFechIni")%>');
tabRel.addColumna('185','left','<%= descriptor.getDescripcion("gEtiq_usuar")%>');
tabRel.addColumna('180','left','<%= descriptor.getDescripcion("tramInic")%>');
tabRel.addColumna('180','left','<%= descriptor.getDescripcion("tramActual")%>');
tabRel.addColumna('40','center','');
tabRel.displayCabecera=true;

var tableObject=tabRel;

function refrescaRelaciones() {
  tabRel.displayTabla();
}

tabRel.displayDatos = pintaDatosExpedientes;

function pintaDatosExpedientes() {
  tableObject = tabRel;
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
  }else{
      teclaAuxiliar = evento.which;
  }

  if(teclaAuxiliar == 40){
    if(tabRel==tableObject) {
      upDownTable(tabRel,listaE,teclaAuxiliar);
    }
  }

  if (teclaAuxiliar == 38){
    if(tabRel==tableObject) {
      upDownTable(tabRel,listaE,teclaAuxiliar);
    }
  }
   if (teclaAuxiliar == 1){
       if (comboProcedimiento.base.style.visibility == "visible" && isClickOutCombo(comboProcedimiento,coordx,coordy)) setTimeout('comboProcedimiento.ocultar()',20);
       if (comboTramite.base.style.visibility == "visible" && isClickOutCombo(comboTramite,coordx,coordy)) setTimeout('comboTramite.ocultar()',20);
   }
   if (teclaAuxiliar == 9){
        if (comboProcedimiento.base.style.visibility == "visible") comboProcedimiento.ocultar();
        if (comboTramite.base.style.visibility == "visible") comboTramite.ocultar();
    }

  keyDel(evento);
}

comboProcedimiento.change = function() {
    var vectorBotones = new Array(document.forms[0].cmdDeshacerRelacion);
    deshabilitarGeneral(vectorBotones);
    vectorBotones = new Array(document.forms[0].cmdBuscar);
    habilitarGeneral(vectorBotones);

    comboTramite.selectItem("-1");
    listaExp = new Array();
    tabRel.lineas = listaExp;
    listaE = new Array();
    listaOriginalE = new Array();
    refrescaRelaciones();

    document.forms[0].codMunicipio.value = <%=municipio%>;
    document.forms[0].opcion.value="cargarTramites";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/RelacionExpedientes.do'/>";
    document.forms[0].submit();
}

comboTramite.change = function() {
    var vectorBotones = new Array(document.forms[0].cmdDeshacerRelacion);
    deshabilitarGeneral(vectorBotones);
    vectorBotones = new Array(document.forms[0].cmdBuscar);
    habilitarGeneral(vectorBotones);

    listaExp = new Array();
    tabRel.lineas = listaExp;
    listaE = new Array();
    listaOriginalE = new Array();
    refrescaRelaciones();
    
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
