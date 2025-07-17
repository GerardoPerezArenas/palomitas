<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: EXPEDIENTES  Tramitación de Expedientes:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->





<%
  int idioma=1;
  int apl=1;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }
  }

%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>



<SCRIPT language="JavaScript">

var lista = new Array();

function inicializar() {
  cargaTabla();
  cargaDatos();
}

function cargaDatos() {

}

function cargaTabla() {

}

function pulsarVolver() {
  document.forms[0].opcion.value="inicio";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
  document.forms[0].submit();
}

function pulsarEnlaces() {
  document.forms[0].opcion.value="inicio";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/ReejecucionEnlaces.do'/>";
  document.forms[0].submit();
}

</SCRIPT>

</head>

<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="/sge/FormulTramitacionExpedientes.do" target="_self">

<html:hidden  property="opcion" value=""/>

<CENTER>

<TABLE id ="tabla1" class="tablaP" width="730px" cellspacing="0" cellpadding="0">
<TR>
  <TD>&nbsp;</TD>
</TR>
<tr>
  <TD class="titulo" colspan="4"><%=descriptor.getDescripcion("tit_selExp")%></TD>
</tr>

<TR>
  <TD>

<TABLE width="100%" cellspacing="5px" cellpadding="1px" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_exped")%>:</td>
    <td width="80%" class="columnP">
      <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtExpediente" size="30" maxlength="30"
            onkeypress="javascript:PasaAMayusculas(event);"/>
    </td>
  </tr>
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("res_etiqProc")%>:</td>
    <td width="80%" class="columnP">
      <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtProcedimiento" size="80" maxlength="80"
            onkeypress="javascript:PasaAMayusculas(event);"/>
    </td>
  </tr>
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_solicit")%>:</td>
    <td width="80%" class="columnP">
      <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtSolicitante" size="80" maxlength="80"
            onkeypress="javascript:PasaAMayusculas(event);"/>
    </td>
  </tr>
  <tr>
    <td width="100%" colspan="2" class="etiqueta" align="center" style="height:7px;background-color:#F7DAB0;"><%=descriptor.getDescripcion("etiq_tramEnc")%></td>
  </tr>
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_tramite")%>:</td>
    <td width="80%" class="columnP">
      <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtTramite" size="10" maxlength="9"
            onkeypress="javascript:PasaAMayusculas(event);"/>
    </td>
  </tr>
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_desc")%>:</td>
    <td width="80%" class="columnP">
      <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtDescripcion" size="80" maxlength="80"
            onkeypress="javascript:PasaAMayusculas(event);"/>
    </td>
  </tr>
  <tr>
    <td width="100%" colspan="2">
      <table border="0px" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="25%" align="center">
            <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbTramitar")%> name="cmdTramitar" onclick="pulsarTramitar();" accesskey="T">
          </td>
          <td width="25%" align="center">
            <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbEnlaces")%> name="cmdEnlaces" onclick="pulsarEnlaces();" accesskey="E">
          </td>
          <td width="25%" align="center">
            <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbRetroceder")%> name="cmdRetroceder" onclick="pulsarRetroceder();" accesskey="R">
          </td>
          <td width="25%" align="center">
            <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbVolver")%> name="cmdVolver" onclick="pulsarVolver();" accesskey="V">
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%" colspan="2" class="etiqueta" align="center" style="height:7px;background-color:#F7DAB0;"><%=descriptor.getDescripcion("res_TramReal")%></td>
  </tr>
  <tr>
    <td width="100%" colspan="2">
      <TABLE width="100%" cellspacing="7px" cellpadding="1px">
        <TR>
          <TD>
            <table width="100%" rules="cols" bordercolor="#7B9EC0" border="1" cellspacing="0" cellpadding="0" class="fondoCab">
              <tr height="15">
                <td width="20%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("gEtiq_entrada")%></td>
                <td width="30%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("gEtiq_exped")%></td>
                <td width="15%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("gEtiq_solicit")%></td>
                <td width="15%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("gEtiqFecha")%></td>
                <td width="20%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("res_etiqUnid")%></td>
              </tr>
              <tr>
                <td colspan="5">
                  <div id="tabla" class="text" style="HEIGHT:160px; WIDTH: 700px; overflow-y: auto; overflow-x: no; visibility: visible; BORDER: 0px">
                  </div>
                </td>
              </tr>
            </table>
          </TD>
        </TR>
      </TABLE>
    </td>
  </tr>

</table>

  </TD>
</TR>

</TABLE>

</center>
</html:form>



<script language="JavaScript1.2">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'),150);

tab.addColumna('210','center');
tab.addColumna('160','center');
tab.addColumna('160','center');
tab.addColumna('70','center');
tab.addColumna('50','center');

function refresca() {
  tab.displayTabla();
}


document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }

  if('Alt+S'==tecla) {
    pulsarSalir();
  } else if('Alt+C'==tecla) {
    pulsarConsultar();
  }

  if((layerVisible)||(divSegundoPlano)) buscar();

    keyDel(evento);

  if (teclaAuxiliar == 9){
      if(layerVisible) ocultarDiv();
    if(divSegundoPlano) divSegundoPlano = false;
      return false;
    }
    if (teclaAuxiliar == 1){
    if(layerVisible) setTimeout("ocultarDivNoFocus()",30);
    if(divSegundoPlano)	divSegundoPlano = false;
    }

    if (teclaAuxiliar == 40){
      if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
      return false;
    }
    if (teclaAuxiliar == 38){
      if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
      return false;
    }

    if(teclaAuxiliar == 13){
      if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)&&(!ultimo)){
        callFromTableTo(tab.selectedIndex,tab.id);
      }
    }

  if ((evento.button == 1)||(evento.button == 2)){
      if(layerVisible) setTimeout("ocultarDiv()",30);
    if(divSegundoPlano)	divSegundoPlano = false;
    }
}

</script>


<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>

</BODY>

</html:html>
