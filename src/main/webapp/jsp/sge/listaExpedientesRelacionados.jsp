<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: LISTA EXPEDIENTES RELACIONADOS :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%
  int idioma=1;
  int apl=4;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
        }
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript">
var datos = new Array();
var datosOriginal = new Array();
var cont = 0;
var cont1 = 0;
function inicializar() {
  <logic:iterate id="elemento" name="ConsultaExpedientesForm" property="listaExpedientesRel">
    datos[cont] = ["<bean:write name="elemento" property="numeroExpedienteRel"/>",
                   "<bean:write name="elemento" property="descProcedimiento"/>",
                   "<bean:write name="elemento" property="titular"/>",
                   "<bean:write name="elemento" property="fechaInicio"/>",
                   "<bean:write name="elemento" property="fechaFin"/>"];
  cont++;
  </logic:iterate>
  <logic:iterate id="elemento" name="ConsultaExpedientesForm" property="listaExpedientesRel">
    datosOriginal[cont1] = ["<bean:write name="elemento" property="codMunicipioRel"/>",
                   "<bean:write name="elemento" property="ejercicioRel"/>",
                   "<bean:write name="elemento" property="numeroExpedienteRel"/>",
                   "<bean:write name="elemento" property="codProcedimiento"/>",
                   "<bean:write name="elemento" property="descProcedimiento"/>",
                   "<bean:write name="elemento" property="titular"/>",
                   "<bean:write name="elemento" property="fechaInicio"/>",
                   "<bean:write name="elemento" property="fechaFin"/>",
                   "<bean:write name="elemento" property="codMunicipio"/>",
                   "<bean:write name="elemento" property="ejercicio"/>",
                   "<bean:write name="elemento" property="numeroExpediente"/>",
                   "<bean:write name="elemento" property="expHistorico"/>"];
  cont1++;
  </logic:iterate>
  tab.lineas=datos;
  tab.displayTabla();
  document.forms[0].modoConsulta.value = "<bean:write name="ConsultaExpedientesForm" property="modoConsulta"/>";
  document.forms[0].desdeConsulta.value = "<bean:write name="ConsultaExpedientesForm" property="modoConsulta"/>";
  document.forms[0].directivaNoRelacionarExp.value = "<bean:write name="ConsultaExpedientesForm" property="directivaNoRelacionarExp"/>";
    
  desdeAltaRE = "<%=request.getParameter("desdeAltaRE")%>";
  document.forms[0].porCampoSup.value="<%=request.getParameter("porCampoSup")%>";
  if (document.forms[0].desdeConsulta.value == "si" || desdeAltaRE == "si" || document.forms[0].directivaNoRelacionarExp.value=="si" ) {
    document.forms[0].cmdAnadir.disabled = true;
	document.forms[0].cmdEliminar.disabled = true;

    // Se cambia el estilo del botón Añadir
    var botonesAnadir = document.getElementsByName("cmdAnadir");
    if(botonesAnadir!=null && botonesAnadir.length==1){
       botonesAnadir[0].className = "botonGeneralDeshabilitado";
    }

    // Se cambia el estilo del botón Añadir
    var botonesEliminar = document.getElementsByName("cmdEliminar");
    if(botonesEliminar!=null && botonesEliminar.length==1){
       botonesEliminar[0].className = "botonGeneralDeshabilitado";
    }

  }
  window.focus();
}
function pulsarAceptar(i) {
  i = tab.selectedIndex;
  if (i >= 0)
    seleccionarFila(i);
}
function recibirNumero(datos) {
  document.forms[0].numero.value = datos[0];
  if(jsp_alerta("C","Desea iniciar expediente con numero de expediente " + document.forms[0].numero.value + " ?") ==1) {
    var retorno = new Array();
    retorno[0]= "si";
    retorno[1]= document.forms[0].codMunicipio.value;
    retorno[2]= document.forms[0].codProcedimiento.value;
    retorno[3]= datos[1];
    retorno[4]= document.forms[0].numero.value;
    
    self.parent.opener.retornoXanelaAuxiliar(retorno);
  }
}
function pulsarCancelar() {
      self.parent.opener.retornoXanelaAuxiliar();

}
function callFromTableTo(rowID,tableName){
  if(tab.id == tableName){
    seleccionarFila(rowID);
  }
}
function pulsarAnadir() { 
  document.forms[0].expRelacionado.value = "si";
  document.forms[0].opcion.value="inicio";
  document.forms[0].target="mainFrame";
  // Se indica que estamos en modo consulta en busqueda de expedientes relacionados
  document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do'/>" + "?modoConsultaExpRel=si";
  document.forms[0].submit();
}
function seleccionarFila(indice) {
    document.forms[0].codMunicipio.value = datosOriginal[indice][0];
    document.forms[0].ejercicio.value = datosOriginal[indice][1];
    document.forms[0].numero.value = datosOriginal[indice][2];
    document.forms[0].codProcedimiento.value = datosOriginal[indice][3];
    document.forms[0].codMunicipioIni.value = datosOriginal[indice][8];
    document.forms[0].ejercicioIni.value = datosOriginal[indice][9];
    document.forms[0].numeroIni.value = datosOriginal[indice][10];
    document.forms[0].expRelacionado.value = "si";
    document.forms[0].desdeExpRel.value = "si";
    document.forms[0].modoConsulta.value = "si";
    document.forms[0].opcion.value="cargar";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<c:url value='/sge/FichaExpediente.do'/>" + "?desdeAltaRE=" + desdeAltaRE + 
            "&expHistorico=" + datosOriginal[indice][11];
    document.forms[0].submit();
}
function pulsarEliminar() {
  if(tab.selectedIndex != -1) {
	document.forms[0].codMunicipio.value = datosOriginal[tab.selectedIndex][0];
    document.forms[0].ejercicio.value = datosOriginal[tab.selectedIndex][1];
    document.forms[0].numero.value = datosOriginal[tab.selectedIndex][2];
    document.forms[0].codMunicipioIni.value = datosOriginal[tab.selectedIndex][8];
    document.forms[0].ejercicioIni.value = datosOriginal[tab.selectedIndex][9];
    document.forms[0].numeroIni.value = datosOriginal[tab.selectedIndex][10];
  
    document.forms[0].opcion.value="eliminarExpedienteRelacionado";
    document.forms[0].target="oculto";
    document.forms[0].action="<c:url value='/sge/ConsultaExpedientes.do'/>";
    document.forms[0].submit(); 
  }
  else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}
function noSePudoEliminar() {
  jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimExpRel")%>");
}
function cargarTabla(listaPr,listaSeg) {
  datos = listaPr;
  datosOriginal = listaSeg;
  tab.lineas=datos;
  tab.displayTabla();
}
</SCRIPT>
</head>
<BODY onload="javascript:{inicializar();}">
<html:form action="/sge/ConsultaExpedientes.do" target="_self">
<html:hidden  property="opcion" value=""/>
<html:hidden  property="codMunicipio"/>
<html:hidden  property="codigoProcedimiento"/>
<html:hidden  property="ejercicio"/>
<input type="hidden" name="numero" >
<input type="hidden" name="codProcedimiento" >
<input type="hidden" name="codMunicipioIni" >
<input type="hidden" name="ejercicioIni" >
<input type="hidden" name="numeroIni" >
<input type="hidden" name="expRelacionado" >
<input type="hidden" name="modoConsulta" >
<input type="hidden" name="desdeConsulta">
<input type="hidden" name="desdeExpRel">
<input type="hidden" name="porCampoSup">
<input type="hidden" name="directivaNoRelacionarExp">
    
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_ExpRel")%></div>
<div class="contenidoPantalla">
    <table width="100%">
        <tr>
            <td>
                <div id="tabla"></div>
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAnadir")%>" name="cmdAnadir" onclick="pulsarAnadir();">
        <input type= "button" class="botonGeneral" accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminar" onclick="pulsarEliminar();">
        <input type= "button"  class="botonGeneral" accesskey="C" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar"  onclick="pulsarCancelar();">
    </div>         
</div>         
</html:form>
<script type="text/javascript">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
tab.addColumna('165','center',"<%= descriptor.getDescripcion("etiq_numExp")%>");
tab.addColumna('210','center',"<%= descriptor.getDescripcion("gEtiq_desc")%>");
tab.addColumna('270','center',"<%= descriptor.getDescripcion("etiq_IntPrin")%>");
tab.addColumna('125','center',"<%= descriptor.getDescripcion("etiqFechIni")%>");
tab.addColumna('125','center',"<%= descriptor.getDescripcion("gEtiq_fecFin")%>");
tab.displayCabecera=true;
function rellenarDatos(tableName,rowID){
      tableObject = tableName;
      var selRow = eval("document.all." + tableName.id + "_Row" + tableName.selectedIndex);
}
function checkKeysLocal(evento,tecla) {
     var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;
  
  keyDel(evento);

  if ( (teclaAuxiliar == 40) || (teclaAuxiliar == 38)){
    upDownTable(tab,datos,teclaAuxiliar);
  }
  
  if(teclaAuxiliar == 13){
    callFromTableTo(tab.selectedIndex,tab.id);
  }
}
</script>
</BODY>
</html:html>
