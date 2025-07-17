<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: Número de Expediente :::</TITLE>
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

var pregunta = "";

function inicializar() {
  var argVentana = self.parent.opener.xanelaAuxiliarArgs;

  if(argVentana[0] == "R") {
    mostrarCapasBotones('capaResolucion');
    domlay('capaPregunta',0,0,0,pregunta);
  } else {
    pregunta = argVentana[1];
    domlay('capaPregunta',1,0,0,pregunta);
  }
}

function pulsarAceptar() {
  bloquearBotonesTemporal('cmdAceptar');
  var retorno = new Array();
  if(document.forms[0].favorable[0].checked == true) {
    retorno[0] = "siFavorable";
  } else {
    retorno[0] = "noFavorable";
  }
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function mostrarCapasBotones(nombreCapa) {
   
  document.getElementById('capaResolucion').style.visibility='hidden';
  document.getElementById(nombreCapa).style.visibility='visible';
}

</SCRIPT>

</head>

<BODY onload="javascript:{inicializar();}" class="bandaBody">

<html:form action="/sge/TramitacionExpedientes.do" target="_self">

<html:hidden  property="opcion" value=""/>

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titPrgFavDesf")%></div>
<div class="contenidoPantalla" valign="top">
    <TABLE cellspacing="0px" cellpadding="0px" border="0px" style="margin-bottom:10px" align="CENTER">
        <TR>
            <TD class="etiqueta" align="center" colspan="2">
                <DIV id="capaResolucion" name="capaResolucion" STYLE="width:100%; height:0px; visibility:hidden; " >
                    <%=descriptor.getDescripcion("msjTramFavDesf")%>
                </DIV>
            </TD>
        </TR>
        <TR>
            <TD class="etiqueta" align="center" colspan="2">&nbsp;
                <div id="capaPregunta" class="text"></div>
            </TD>
        </TR>
        <TR>
            <TD style="width: 40%" class="columnP" align="right">
                <input type="radio" name="favorable" value="siFavorable" CHECKED ></input>
            </TD>
            <TD class="etiqueta" align="left">
                <%=descriptor.getDescripcion("msjSiFav")%>
            </TD>
        </TR>
        <TR>
            <TD class="columnP" align="right">
                <input type="radio" name="favorable" value="noFavorable" ></input>
            </TD>
            <TD class="etiqueta" align="left">
                <%=descriptor.getDescripcion("msjNoDesf")%>
            </TD>
        </TR>
    </TABLE>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" accesskey="A" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();">
        <input type= "button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar"  onclick="pulsarCancelar();">
    </div>       
</div>       
</html:form>


<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>

</BODY>

</html:html>
