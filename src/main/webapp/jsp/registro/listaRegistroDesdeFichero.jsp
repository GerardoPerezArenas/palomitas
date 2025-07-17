<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.Vector"%>

<html>
<head>
<TITLE>LISTA REGISTRO DESDE FICHERO</TITLE>

<!-- Estilos -->

<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();    
  int idioma=1;
  int apl=1;
  if (session.getAttribute("usuario") != null){
    	usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    	idioma = usuarioVO.getIdioma();
    	apl = usuarioVO.getAppCod();
  }

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<!-- Ficheros JavaScript -->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >

<SCRIPT language="JavaScript">

var registros = new Array();

function inicializar() {
  registros = self.parent.opener.xanelaAuxiliarArgs;
  tab.lineas=registros; 
  tab.displayTabla();
  window.focus();
}


function pulsarAceptar(i) {
  self.parent.opener.retornoXanelaAuxiliar();
}

</SCRIPT>

</head>

<body class="bandaBody" onload="javascript:{inicializar();}">

<form>

<table width="100%" cellpadding="0px" cellspacing="0px">
	<tr>
		<td height="15px"></td>
	</tr>
</table>

<table id="tabla1" width="440px" height="250px" align="center" cellpadding="0px" cellspacing="0px">
    <tr>
            <td width="440px" height="250px">
                    <table width="100%" height="100%" cellpadding="1px" valign="middle" cellspacing="0px" border="0px" bgcolor="#666666">
                            <tr>
                                    <td>
                                            <table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                                    <tr>
                                                            <td width="100%" height="30px" bgcolor="#7B9EC0" class="titulo">&nbsp;<%=descriptor.getDescripcion("etiqFichErr")%></td>
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
</div>

<form>

<script language="JavaScript1.2">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('110','center','<%= descriptor.getDescripcion("etiqEjNum")%>');
tab.addColumna('120','center','<%= descriptor.getDescripcion("etiqExp")%>');
tab.addColumna('180','center','<%= descriptor.getDescripcion("etiqInfract")%>');
tab.displayCabecera=true;
tab.displayTabla();

function refresca() {
  tab.displayTabla();
}

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar;
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

  keyDel(evento);
  if ( (teclaAuxiliar == 40) || (teclaAuxiliar == 38)){
    upDownTable(tab,registros,teclaAuxiliar);
  }
}

</script>



<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>





</BODY>

</html>
