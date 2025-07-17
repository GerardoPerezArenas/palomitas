<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html:html>

<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />

<TITLE>::: ADMINISTRACION  Grupo:::</TITLE>

<%
  int idioma=1;
  int apl=1;
  int munic = 0;
  String css="";
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
          css=usuario.getCss();
        }
  }%>
  
  
<!-- Estilos -->

  
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<SCRIPT type="text/javascript">
var cod_organizaciones = new Array();
var desc_organizaciones = new Array();
var cod_entidades = new Array();
var desc_entidades = new Array();
var cod_aplicaciones = new Array();
var desc_aplicaciones = new Array();
var cod_grupos = new Array();
var desc_grupos = new Array();
var cod_usuarios = new Array();
var desc_usuarios = new Array();
var cont = 0;
var cont1 = 0;
var cont2 = 0;



function inicializar() {
  var argVentana = self.parent.opener.xanelaAuxiliarArgs;
  document.forms[0].codOrganizaciones.value = argVentana[0];
  document.forms[0].codEntidades.value = argVentana[1];
  document.forms[0].codAplicaciones.value = argVentana[2];
  document.forms[0].codGrupos.value = argVentana[3];
  document.forms[0].descGrupos.value = argVentana[4];
  document.forms[0].descOrganizaciones.value = argVentana[5];
  document.forms[0].descEntidades.value = argVentana[6];
  document.forms[0].descAplicaciones.value = argVentana[7];
  <%  String modo = (String)session.getAttribute("modo");
      session.removeAttribute("modo");
  
      UsuariosGruposForm bForm =(UsuariosGruposForm)session.getAttribute("UsuariosGruposForm");
	  Vector listaOrganizaciones = bForm.getListaOrganizaciones();
	  Vector listaOrg = bForm.getListaOrg();
	  int lengthOrg = listaOrg.size();
	  int lengthOrganizaciones = listaOrganizaciones.size();
	  //Vector listaAplicaciones = bForm.getListaAplicaciones();
	  //int lengthAplicaciones = listaAplicaciones.size();
	  Vector listaGrupos = bForm.getListaGrupos();
	  int lengthGrupos = listaGrupos.size();
	  int m=0;
	%>
	  var n=0;
	<%for(m=0;m<lengthOrganizaciones;m++){
      int t=0;
	  GeneralValueObject organizaciones = (GeneralValueObject)listaOrganizaciones.get(m);
	  String o = (String)organizaciones.getAtributo("codigo");
	  for(t=0;t<lengthOrg;t++) {
	    String org = (String)listaOrg.get(t);
		if(org.equals(o)) {%>
		  cod_organizaciones[n] = '<%=(String)organizaciones.getAtributo("codigo")%>';
	      desc_organizaciones[n] = '<%=(String)organizaciones.getAtributo("descripcion")%>';
		  n++;
		  <%t = lengthOrg;
		}
	  }%>
    <%}%>
	n=0;
	<%for(m=0;m<lengthGrupos;m++){
      GeneralValueObject grupos = (GeneralValueObject)listaGrupos.get(m);%>
      cod_grupos[n] = '<%=(String)grupos.getAtributo("codGrupo")%>';
	  desc_grupos[n] = '<%=(String)grupos.getAtributo("nombreGrupo")%>';
      n++;
    <%}%> 

  comboOrganizaciones.addItems(cod_organizaciones,desc_organizaciones);
  comboAplicaciones.addItems(cod_aplicaciones,desc_aplicaciones);
  comboGrupos.addItems(cod_grupos,desc_grupos);
  if(document.forms[0].codEntidad.value != "") {
    comboOrganizaciones.buscaLinea(0);
	comboEntidades.addItems([document.forms[0].codEntidad.value],[document.forms[0].nombreEntidad.value]);
	comboEntidades.buscaLinea(0);
	listaAplicaciones();
	comboOrganizaciones.deactivate();
    comboEntidades.deactivate();
  }
}

function pulsarSalir() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function pulsarAceptar () {
  if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
    comboOrganizaciones.activate();
    comboEntidades.activate();
    var retorno = new Array();
    retorno[0] = document.forms[0].codOrganizaciones.value;
    retorno[1] = document.forms[0].descOrganizaciones.value;
    retorno[2] = document.forms[0].codEntidades.value;
    retorno[3] = document.forms[0].descEntidades.value;
    if(document.forms[0].codAplicaciones.value == 999) {
      retorno[4] = cod_aplicaciones;
      retorno[5] = desc_aplicaciones;
    } else {
      retorno[4] = document.forms[0].codAplicaciones.value;
      retorno[5] = document.forms[0].descAplicaciones.value;	
    }
    retorno[6] = document.forms[0].codGrupos.value;
    retorno[7] = document.forms[0].descGrupos.value;	

    self.parent.opener.retornoXanelaAuxiliar(retorno);
  }
}

function listaEntidades() {
  document.forms[0].organizacion.value=document.forms[0].codOrganizaciones.value;
  document.forms[0].opcion.value="cargarEntidades";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesInternas.do";
  document.forms[0].submit();	
}

function cargarEntidades(codigos, descripciones) {
  comboEntidades.addItems(codigos,descripciones);
 }
 
 function listaAplicaciones() {
   document.forms[0].organizacion.value=document.forms[0].codOrganizaciones.value;
   document.forms[0].entidad.value=document.forms[0].codEntidades.value;
   document.forms[0].opcion.value="cargarAplicaciones";
   document.forms[0].target="oculto";
   document.forms[0].action="<%=request.getContextPath()%>/administracion/AutorizacionesInternas.do";
   document.forms[0].submit();	
 }
 
 function cargarAplicaciones(codigos, descripciones) {
  var longAplic = codigos.length;
  codigos[longAplic] = 999;
  descripciones[longAplic] = "TODAS";
  comboAplicaciones.addItems(codigos,descripciones);
  cod_aplicaciones = codigos;
  desc_aplicaciones = descripciones;
 }

</SCRIPT>

</head>

<body class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        


<html:form action="/administracion/UsuariosGrupos.do" target="_self">

<html:hidden  property="opcion" value=""/>
<html:hidden  property="codEntidad" />
<html:hidden  property="nombreEntidad" />
<input  type="hidden"  name="organizacion" id="organizacion">
<input  type="hidden"  name="entidad" id="entidad">

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_GrPertUsu")%></div>
<div class="contenidoPantalla">
    <TABLE id ="tablaDatosGral">
        <tr>
            <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Organizacion")%>:</td>
            <td class="columnP">
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="codOrganizaciones" size="2" onkeypress="javascript:return SoloDigitos(event);"/>
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="descOrganizaciones" style="width:500;height:17" readonly/>
                       <A href="" id="anchorOrganizaciones" name="anchorOrganizaciones">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonOrganizaciones" name="botonOrganizaciones"></span>
                </A>
            </td>
        </tr>
        <tr>
            <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Entidad")%>:</td>
            <td class="columnP">
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="codEntidades" size="2"
                       onkeypress="javascript:return SoloDigitos(event);"/>
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="descEntidades" style="width:500;height:17" readonly/>
                       <A href="" id="anchorEntidades" name="anchorEntidades">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonEntidades" name="botonEntidades"></span>
                </A>
            </td>
        </tr>
        <% if ("verUsuarios".equals(modo)) {%>
        <tr>
            <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_usur")%>:</td>
            <td class="columnP">
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="codUsuarios" size="2"
                       onkeypress="javascript:return SoloDigitos(event);"/>
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="descUsuarios" style="width:500;height:17" readonly/>
                       <A href="" id="anchorUsuarios" name="anchorUsuarios">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonUsuarios" name="botonUsuarios"></span>
                </A>
            </td>
        </tr>
        <% }
        if ("verGrupos".equals(modo)) {%>
        <tr>
            <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_grup")%>:</td>
            <td class="columnP">
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="codGrupos" size="2"
                       onkeypress="javascript:return SoloDigitos(event);"/>
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="descGrupos" style="width:500;height:17" readonly/>
                       <A href="" id="anchorGrupos" name="anchorGrupos">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonGrupos" name="botonGrupos"></span>
                </A>
            </td>
        </tr>
        <% }%>
        <tr>
            <td style="width: 20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Aplicacion")%>:</td>
            <td class="columnP">
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="codAplicaciones" size="2"
                       onkeypress="javascript:return SoloDigitos(event);"/>
                <input type="text" id="obligatorio" class="inputTextoObligatorio" name="descAplicaciones" style="width:500;height:17" readonly/>
                       <A href="" id="anchorAplicaciones" name="anchorAplicaciones">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicaciones" name="botonAplicaciones"></span>
                </A>
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal"> 
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar"  onClick="pulsarAceptar();">
            <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onClick="pulsarSalir();">
    </div>        
</div>        

</html:form>

 <script type="text/javascript">
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

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }
    if (teclaAuxiliar == 1){
        if (comboOrganizaciones.base.style.visibility == "visible" && isClickOutCombo(comboOrganizaciones,coordx,coordy)) setTimeout('comboOrganizaciones.ocultar()',20);
        if (comboEntidades.base.style.visibility == "visible" && isClickOutCombo(comboEntidades,coordx,coordy)) setTimeout('comboEntidades.ocultar()',20);
        if (comboAplicaciones.base.style.visibility == "visible" && isClickOutCombo(comboAplicaciones,coordx,coordy)) setTimeout('comboAplicaciones.ocultar()',20);
        <% if("verUsuarios".equals(modo)) { %>
        if (comboUsuarios.base.style.visibility == "visible" && isClickOutCombo(comboUsuarios,coordx,coordy)) setTimeout('comboUsuarios.ocultar()',20);
        <% }
        if("verGrupos".equals(modo)) {%>
        if (comboGrupos.base.style.visibility == "visible" && isClickOutCombo(comboGrupos,coordx,coordy)) setTimeout('comboGrupos.ocultar()',20);
        <% } %>
    }
    if (teclaAuxiliar == 9){
        if (comboOrganizaciones.base.style.visibility == "visible") comboOrganizaciones.ocultar();
        if (comboEntidades.base.style.visibility == "visible") comboEntidades.ocultar();
        if (comboAplicaciones.base.style.visibility == "visible") comboAplicaciones.ocultar();
        <% if("verUsuarios".equals(modo)) { %>
        if (comboUsuarios.base.style.visibility == "visible") comboUsuarios.ocultar();
        <% }
        if("verGrupos".equals(modo)) {%>
        if (comboGrupos.base.style.visibility == "visible") comboGrupos.ocultar();
        <% } %>

    }
    keyDel(evento);
}


var comboOrganizaciones = new Combo("Organizaciones");
var comboEntidades = new Combo("Entidades");
var comboAplicaciones = new Combo("Aplicaciones");
<% if("verUsuarios".equals(modo)) { %>
var comboUsuarios = new Combo("Usuarios");
<% } 
if("verGrupos".equals(modo)) {%>
var comboGrupos = new Combo("Grupos");
<% } %>

comboOrganizaciones.change = function() { 
  auxCombo='comboEntidades'; 
  comboEntidades.selectItem(-1);			
  if(comboOrganizaciones.des.value.length != 0) {
	listaEntidades();			
  }else{
    comboEntidades.addItems([],[]);
  }	
}

comboEntidades.change = function() { 
  auxCombo='comboAplicaciones'; 
  comboAplicaciones.selectItem(-1);			
  if(comboEntidades.des.value.length != 0) {
	listaAplicaciones();			
  }else{
    comboAplicaciones.addItems([],[]);
  }	
}

</script>

</BODY>

</html:html>
