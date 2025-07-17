<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<HTML><head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: LISTA UNIDADES ORGANICAS DEL USUARIO :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%int idioma=1;
  int apl=1;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }
  }
  String codPro  = request.getParameter("codPro");
  String tipoSeleccion = request.getParameter("tipoSel");
  %>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<SCRIPT type="text/javascript">
<% 
String msjTitulo = "";
if ("1".equals(tipoSeleccion)) msjTitulo = descriptor.getDescripcion("titUnidIniExp");
else if ("2".equals(tipoSeleccion)) msjTitulo = descriptor.getDescripcion("titSelUniTram");
else msjTitulo = descriptor.getDescripcion("tit_listUORS");
%>
    
var datosOriginal = new Array();
var codUOR = null;
function inicializar() {
  datosOriginal = self.parent.opener.xanelaAuxiliarArgs;
  tab.lineas=datosOriginal;
  tab.displayTabla();
  window.focus();
}

function seleccionarUTR(i) {
  self.parent.opener.retornoXanelaAuxiliar(datosOriginal[i][2]);
}

function pulsarAceptarFila(i) {  
  if (i >= 0) {
      seleccionarUTR(i);
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }
}

function pulsarAceptar(i) {
  i = tab.selectedIndex;
  if (i >= 0) {
      seleccionarUTR(i);  
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
  }
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function callFromTableTo(rowID,tableName){
  if(tab.id == tableName){
    pulsarAceptarFila(rowID);
  }
}


</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{inicializar();}">
    <FORM action="<c:url value='/sge/Tramitacion.do'/>" target="_self">
        <div id="titulo" class="txttitblanco"><%=msjTitulo%></div>
        <div class="contenidoPantalla">
            <table  style="padding-top: 10px; padding-bottom: 10px">
                <tr>
                    <td id="tabla" style="width: 100%" valign="middle"></td>
                </tr>                                                
            </table>
            <div class="botoneraPrincipal">
                <input type="button" class="botonGeneral" accesskey="A" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();" accesskey="A">
                <input type="button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar"  onclick="pulsarCancelar();" accesskey="C">
            </div>
        </div>
                    </form>
<script type="text/javascript">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
tab.addColumna('110','center', '<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tab.addColumna('460','center', '<%= descriptor.getDescripcion("gEtiq_nombre")%>');

//Poner columna oculta cuando vienen 3 valores en cada fila. Este valor oculto es el identificador de la UOR
if (self.parent.opener.xanelaAuxiliarArgs[0].length==3) {tab.addColumna('0','center', '');}
tab.displayCabecera=true;
function rellenarDatos(tableName,rowID){
      tableObject = tableName;
      var selRow = eval("document.all." + tableName.id + "_Row" + tableName.selectedIndex);

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
    upDownTable(tab,datos,teclaAuxiliar);
  }
}
            </script>

    </BODY>
</html>
