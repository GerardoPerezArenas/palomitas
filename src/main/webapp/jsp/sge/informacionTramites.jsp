<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: Información sobre los trámites :::</TITLE>
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
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>



<SCRIPT type="text/javascript">

var htmlString = "";

function inicializar() {
  var argVentana = self.parent.opener.xanelaAuxiliarArgs;
  var lista = new Array();
  var j = 0;
  lista[0] = argVentana[0];
  for(i=1; i<argVentana.length; i++) {
    if(argVentana[i][9] != argVentana[i-1][9]) {
      domlay('capaEscritura',1,0,0,escribir(lista));
      lista = new Array();
    }
    j = lista.length;
    lista[j] = argVentana[i];
  }
  domlay('capaEscritura',1,0,0,escribir(lista));
}

function escribir(lista) {    
  if(htmlString == "") {
    htmlString = '<table border="0px" cellpadding="4" cellspacing="0" align="center">';
  } else {
    htmlString += '<table border="0px" cellpadding="4" cellspacing="0" align="center">';
  }

  var exp = " <%=descriptor.getDescripcion("gEtiq_exped")%> ";
  exp = exp.toUpperCase();
  htmlString += '<tr><td class="etiqueta" colspan="3">' + exp + lista[0][0] + '</td></tr>';
  htmlString += '<tr><td class="etiqueta" colspan="3">' + "* <%=descriptor.getDescripcion("msgNoIniTram")%> " + lista[0][2] + " <%=descriptor.getDescripcion("msgxq")%>:";
  htmlString += '</td></tr>';
  for(var k=0; k<lista.length; k++) {
    if (lista[k][3]=="ESTADO_TRAMITE") {
        htmlString += '<tr><td class="columnP" width="10%"></td><td colspan="2" class="etiqueta" width="90%">' +
            "- El trámite " + lista[k][4] + " no está " + lista[k][5] + ".";

    }
    else if (lista[k][3]=="EXPRESION") {
        htmlString += '</td></tr>';
        var nombresCampos = new Array();
        nombresCampos = lista[k][6];
        if (nombresCampos.length > 1) {
            htmlString += '<tr><td class="columnP" width="10%"></td><td colspan="2" class="etiqueta" width="90%">' +
                "- <%=descriptor.getDescripcion("msgNoCondEnts")%>:";
        } else {
            htmlString += '<tr><td class="columnP" width="10%"></td><td colspan="2" class="etiqueta" width="90%">' +
                "- <%=descriptor.getDescripcion("msgNoCondEnt")%>:";
        }
        for (var x = 0; x < nombresCampos.length; x++) {
          htmlString += '<tr><td colspan="2" class="columnP" width="20%"></td><td class="etiqueta" width="80%">' +
                '· ' + nombresCampos[x] + '</td></tr>';
        }
    }
    else if (lista[k][3]=="FIRMA") {
        htmlString += '<tr><td class="columnP" width="10%"></td><td colspan="2" class="etiqueta" width="90%">' +
            "- El documento " + lista[k][7] + " del trámite " + lista[k][4] + " no está " + lista[k][8] + ".";

    }
    htmlString += '</td></tr>';

  }
  htmlString += '<tr><td height="2px" colspan="2"></td></tr>';
  htmlString += '</table>';
  return htmlString;
}

function pulsarAceptar() {
  var retorno = new Array();
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function mostrarCapasBotones(nombreCapa) {
  document.getElementById('capaResolucion').style.visibility='hidden';
  document.getElementById(nombreCapa).style.visibility='visible';
}

</SCRIPT>

</head>

<BODY scroll="auto" class="bandaBody" onload="javascript:{inicializar();}">

<html:form action="/sge/TramitacionExpedientes.do" target="_self">

<html:hidden  property="opcion" value=""/>

<div id="titulo" style="width: 100%; height: 25px" class="txttitblanco"><%=descriptor.getDescripcion("titInfTram")%></div>
<div class="contenidoPantalla" valign="top">
    <TABLE style="width:100%">
        <TR>
            <TD class="etiqueta" align="center">
                <div id="capaEscritura" class="text"></div>
            </TD>
        </TR>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onclick="pulsarAceptar();">
    </div>                                       
</div>                                       
</html:form>

<script type="text/javascript">

document.onmousedown = checkKeys;

function checkKeysLocal(tecla) {
  keyDel();
}


</script>
</BODY>

</html:html>
