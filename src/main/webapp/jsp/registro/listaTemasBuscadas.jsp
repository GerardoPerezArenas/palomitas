<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.mantenimiento.MantTemaForm"%>
<%@page import="es.altia.agora.business.registro.mantenimiento.MantTemasValueObject"%>
<%@page import="java.util.Vector"%>

<html:html>

<head>

<TITLE>::: LISTA TEMAS BUSCADAS:::</TITLE>

<!-- Estilos -->
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

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

<!-- Ficheros JavaScript -->

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">

<SCRIPT language="JavaScript">

var listaTemas = new Array();
var listaTemasOriginal = new Array();

function inicializar() {
  window.focus();
  <%
    MantTemaForm bForm =(MantTemaForm)session.getAttribute("MantTemaForm");
    Vector listaTemas = bForm.getCodigos();
    int lengthTemas = listaTemas.size();
    String lTemas="";
    String lTemasOriginal="";
	int i = 0;
	if(lengthTemas >0) {
	      for(i=0;i<lengthTemas-1;i++){
	        MantTemasValueObject temas = (MantTemasValueObject)listaTemas.get(i);
	        lTemas+="[\""+temas.getCodigo()+"\","+
	          		"\""+temas.getTxtNomeDescripcion()+"\"],";
			lTemasOriginal+="[\""+temas.getIde()+"\","+
			  				"\""+temas.getCodigo()+"\","+
	          				"\""+temas.getTxtNomeDescripcion()+"\"],";
	      }
	      MantTemasValueObject temas = (MantTemasValueObject)listaTemas.get(i);
	      lTemas+="[\""+temas.getCodigo()+"\","+
	        	  "\""+temas.getTxtNomeDescripcion()+"\"]";
		  lTemasOriginal+="[\""+temas.getIde()+"\","+
	        			  "\""+temas.getCodigo()+"\","+
	        			  "\""+temas.getTxtNomeDescripcion()+"\"]";
	  }
  %>
  
  listaTemas = [<%=lTemas%>];
  listaTemasOriginal = [<%=lTemasOriginal%>];

  tab.lineas=listaTemas;
  refresca();
}

function pulsarAceptar(i) {
  i = tab.selectedIndex;
  if (i >= 0){
	seleccionarTema(i);
  }
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function callFromTableTo(rowID,tableName){
  if(tab.id == tableName){
	seleccionarTema(rowID);
  }
}

function pulsarVolver() {
    document.forms[0].target="mainFrame";
    document.forms[0].action="<%=request.getContextPath()%>/jsp/registro/consultaTemas.jsp";
    document.forms[0].submit();
}

function seleccionarTema(i) {
  var retorno = new Array();
  retorno[0] = listaTemasOriginal[i][0];
  retorno[1] = listaTemasOriginal[i][1];
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}

</SCRIPT>

</head>

<BODY onload="javascript:{inicializar();}">

<html:form action="/MantTema.do" target="_self">

<input type="Hidden" name="opcion" value="">

<table width="100%" cellpadding="0px" cellspacing="0px">
	<tr>
		<td height="5px"></td>
	</tr>
</table>

<table id="tabla1" width="480px" height="185px" align="center" cellpadding="0px" cellspacing="0px">
    <tr>
            <td width="480px" height="185px">
                    <table width="100%" height="100%" cellpadding="1px" valign="middle" cellspacing="0px" border="0px" bgcolor="#666666">
                            <tr>
                                    <td>
                                            <table width="100%" height="100%" cellpadding="0px" cellspacing="1px" border="0px" bgcolor="#ffffff">
                                                    <tr>
                                                            <td width="100%" height="30px" bgcolor="#7B9EC0" class="titulo">&nbsp;<%=descriptor.getDescripcion("tit_listTemas")%></td>
                                                    </tr>
                                                    <tr>
                                                            <td width="100%" height="1px" bgcolor="#666666"></td>
                                                    </tr>
                                                    <tr>
                                                            <td id="tabla"></td>
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
    <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbVolver")%> name="cmdVolver" onclick="pulsarVolver();" accesskey="V">
    <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onclick="pulsarCancelar();" accesskey="C">
</div>

</html:form>

<script language="JavaScript1.2">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('100','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tab.addColumna('350','center','<%= descriptor.getDescripcion("gEtiq_Nombre")%>');
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
    upDownTable(tab,listaTemas,teclaAuxiliar);
  }
}
</script>

</BODY>

</html:html>
