<%@page contentType="text/html; charset=iso-8859-1"	language="java" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: LISTA VIAS :::</TITLE>
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

<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<SCRIPT language="JavaScript">

var Vias = new Array();
var viasVisible = new Array();

function inicializar() {
  window.focus();
  <%
    BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
    Vector listaVias = bForm.getListaVias();
    int lengthVias = listaVias.size();
  %>
  var j=0;
  <%for(int i=0;i<lengthVias;i++){%>
    Vias[j] =['<%=(String)((GeneralValueObject)listaVias.get(i)).getAtributo("codVia")%>',
      '<%=(String)((GeneralValueObject)listaVias.get(i)).getAtributo("esiCod")%>',
      '<%=(String)((GeneralValueObject)listaVias.get(i)).getAtributo("descVia")%>',
      '<%=(String)((GeneralValueObject)listaVias.get(i)).getAtributo("codTipoVia")%>',
      '<%=(String)((GeneralValueObject)listaVias.get(i)).getAtributo("descTipoVia")%>',
      '<%=(String)((GeneralValueObject)listaVias.get(i)).getAtributo("lugar")%>',
	  '<%=(String)((GeneralValueObject)listaVias.get(i)).getAtributo("idVia")%>'];
    j++;
  <%}%>
  for (var i=0; i < Vias.length; i++) {
    var cod = Vias[i][0];
    var desc = Vias[i][2];
    var codTVia = Vias[i][3];
    var descTVia = (Vias[i][4]!="")?" ("+Vias[i][4]+")":"";
    var lugar = escape(Vias[i][5]);
    var descrip =((lugar!="")&&(cod==0))? desc+"-"+Vias[i][5]:desc;
    viasVisible[i] = [cod,descrip+descTVia];
  }
  tab.lineas=viasVisible;
  refresca();
  window.focus();
}

function seleccionarVia(i) {
  var retorno = new Array();
  retorno[0] = Vias[i][0];
  retorno[1] = Vias[i][2];
  retorno[2] = Vias[i][3];
  retorno[3] = Vias[i][4];
  retorno[4] = Vias[i][5];
  retorno[5] = Vias[i][6];
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function pulsarAceptar(i) {
  i = tab.selectedIndex;
  if (i >= 0)
    seleccionarVia(i);
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function callFromTableTo(rowID,tableName){
  if(tab.id == tableName){
    seleccionarVia(rowID);
  }
}

</SCRIPT>

</head>

<BODY class="bandaBody" onload="javascript:{inicializar();}">

<html:form action="/sge/TramitacionExpedientes.do" target="_self">

<html:hidden  property="opcion" value=""/>

<table width="100%" cellpadding="0px" cellspacing="0px">
	<tr>
		<td height="15px"></td>
	</tr>
</table>

<table id="tabla1" width="390px" height="450px" align="center" cellpadding="0px" cellspacing="0px">
<tr>
    <td width="390px" height="450px">
        <table width="100%" height="100%" cellpadding="1px" valign="middle" cellspacing="0px" border="0px" bgcolor="#666666">
            <tr>
                <td>
                    <table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                        <tr>
                                <td width="100%" height="30px" bgcolor="#7B9EC0" class="titulo">&nbsp;<%=descriptor.getDescripcion("tit_listVias")%></td>
                        </tr>
                        <tr>
                                <td width="100%" height="1px" bgcolor="#666666"></td>
                        </tr>
                        <tr>
                                <td id="tabla" width="100%" align="center" valign="middle" bgcolor="#e6e6e6"></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
<div class="botoneraPrincipal">
    <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();" accesskey="A">
    <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onclick="pulsarCancelar();" accesskey="C">
</div>

</html:form>

<script language="JavaScript1.2">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('80','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tab.addColumna('275','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
tab.displayCabecera=true;
tab.displayTabla();

function refresca() {
  tab.displayTabla();
}

function checkKeysLocal(evento,tecla) {
     var teclaAuxiliar="";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;
    keyDel(evento);
  
  if ( (teclaAuxiliar == 40) || (teclaAuxiliar == 38)){
    upDownTable(tab,viasVisible,teclaAuxiliar);
  }
}
</script>



<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>

</BODY>

</html:html>
