<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<html:html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<TITLE>::: ADMINISTRACION  Datos de Usuario:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css>">
<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
        }
  }%>
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<SCRIPT type="text/javascript">
var cod_idioma = new Array();
var desc_idioma = new Array();
var cont = 0;
var cont1 = 0;
var cont2 = 0;
var cont3 = 0;
var loginOriginal = "";

function inicializar() {
  var h = 0;
  <% 
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
  %>    
  var argVentana = self.parent.opener.xanelaAuxiliarArgs;
  if(argVentana != null ) {
    loginOriginal = argVentana[0];
  } 
  tp1.setSelectedIndex(0);
  <%
    String modo = "";
    modo = (String) session.getAttribute("modo");
    session.removeAttribute("modo");
    Log m_log = LogFactory.getLog(this.getClass().getName());
    if(m_log.isDebugEnabled()) m_log.debug("El modo es: " + modo);
    
    UsuariosGruposForm bForm =(UsuariosGruposForm)session.getAttribute("UsuariosGruposForm");
    Vector listaIdiomas = bForm.getListaIdiomas();
    int lengthIdiomas = listaIdiomas.size();
    int m=0;
  %>       
    var n=0;
  <%for(m=0;m<lengthIdiomas;m++){
    GeneralValueObject idiomas = (GeneralValueObject)listaIdiomas.get(m);%>
    cod_idioma[n] = '<%=(String)idiomas.getAtributo("codigo")%>';
    desc_idioma[n] = '<%=(String)idiomas.getAtributo("descripcion")%>';
    n++;
  <%}%>

  comboIdioma.addItems(cod_idioma,desc_idioma);
}

function pulsarSalir() {
  self.parent.opener.retornoXanelaAuxiliar();
}

var JS_DEBUG_LEVEL = 50;

function validarNifObligatorio() {
    if (JS_DEBUG_LEVEL >= 80) alert("datosUsuarios.validarNifObligatorio() BEGIN");
    var edtNif = document.forms[0].nif;
    var cmbFirmante = document.forms[0].firmante;
    if (edtNif && cmbFirmante && (cmbFirmante.selectedIndex >= 0) ) {
        var nif = edtNif.value;
        var optionFirmante = cmbFirmante.options[cmbFirmante.selectedIndex];
        var firmante = ( (optionFirmante!=null) ? (optionFirmante.value) : ('0') );
        if (JS_DEBUG_LEVEL >= 80) alert("datosUsuarios.validarNifObligatorio() nif="+nif+";firmante="+firmante+";");
        if (firmante == '1') {
            var result = ( (nif!=null) && (nif.length > 0 ) );
            if (!result) {
                jsp_alerta("A","Debe rellenar el NIF para los usuarios con capacidad de firma");
            } else {
                result = (validarNif(edtNif,0));
            }
            return result;
        } else {
            if ( (nif!=null) && (nif.length > 0 ) ) return validarNif(edtNif,0); else return true;
        }
    } else {
            if (JS_DEBUG_LEVEL >= 80) alert("datosUsuarios.validarNifObligatorio() nif="+edtNif+";firmante="+cmbFirmante+";");
    }
    return true;
}

function pulsarAceptar () {
    var desc = document.forms[0].login.value;
    var contrasena = document.forms[0].contrasena.value;
    var contrasena2 = document.forms[0].contrasena2.value;
    if (contrasena == contrasena2) {
	    if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
	      if (validarNifObligatorio() && validarEmails()) {
            document.forms[0].opcion.value="modificarDatosBasicosUsuario";
            document.forms[0].idioma.value=document.forms[0].codIdioma.value;
            document.forms[0].target="_top";
            document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
            document.forms[0].submit();
	      }
	    }
    } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjContrDif")%>');
    }
}

function devolver () {
  var retorno = new Array();
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}

function ventanaPopUpModal() {
    var datosAEnviar = new Array();
    var source = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=ficheroFirma";
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/administracion/mainVentana.jsp?source="+source,datosAEnviar
	'width=475,height=200,status=no,scrollbars=no',function(res){
                        if (res=="OK") actualizarFicheros();
                   });
}

</SCRIPT>

</head>

<body class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>


<html:form action="/administracion/UsuariosGrupos.do" target="_self"  enctype="multipart/form-data">

<html:hidden  property="opcion" value=""/>
<html:hidden  property="codUsuario"/>
<html:hidden  property="nombreEntidad"/>
<html:hidden  property="login"/>
<input type="hidden" name="idioma" value="">
<input type="hidden" name="listaOrgan" value="">
<input type="hidden" name="lOrganizaciones" value="">
<input type="hidden" name="lEntidades" value="">
<input type="hidden" name="lAplicaciones" value="">
<input type="hidden" name="lGrupos" value="">

<input type="hidden" name="lOrganizacionesUOR" value="">
<input type="hidden" name="lEntidadesUOR" value="">
<input type="hidden" name="lUnidOrganicas" value="">
<input type="hidden" name="lCargosUOR" value="">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_datUsu")%></div>
    <div class="contenidoPantalla">
        <div class="tab-pane" id="tab-pane-1">
        <script type="text/javascript">
          tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
        </script>
          <!-- CAPA 1: DATOS GENERALES
          ------------------------------ -->
          <div class="tab-page" id="tabPage1">
          <h2 class="tab"><%=descriptor.getDescripcion("gEtiq_datGen")%></h2>
          <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
          <TABLE id ="tablaDatosGral" width="600px" cellspacing="4px" cellpadding="3px" border="0" bgcolor="#FFFFFF">
              <tr>
                <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Nombre")%>:</td>
                <td width="80%" class="columnP">
                  <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="nombreUsuario" size="50" maxlength="40"
                    onkeypress="javascript:PasaAMayusculas(event);"/>
                </td>
              </tr>
              <tr>
                <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_cont")%>:</td>
                <td width="80%" class="columnP">
                  <html:password styleId="obligatorio" styleClass="inputTextoObligatorio" property="contrasena" size="25" maxlength="15"/>
                </td>
              </tr>
              <tr>
              <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_cont2")%>:</td>
              <td width="80%" class="columnP">
                <html:password styleId="obligatorio" styleClass="inputTextoObligatorio" property="contrasena2" size="25" maxlength="15"/>
              </td>
              </tr>
              <tr>
                <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Idioma")%>:</td>
                <td width="80%" class="columnP">
                  <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="codIdioma" size="2"
                    onkeypress="javascript:return SoloDigitos(event);"/>
                  <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descIdioma" size="30px" readonly="true"/>
                  <A href="" id="anchorIdioma" name="anchorIdioma">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdioma" name="botonIdioma"></span>
                  </A>
                </td>
              </tr>
              <tr>
                <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_mail")%>:</td>
                <td width="80%" class="columnP">
                  <html:text styleClass="inputTexto" property="email" size="50" maxlength="100"/>
                </td>
              </tr>
              <tr>
                <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_nif")%>:</td>
                <td width="80%" class="columnP">
                  <html:text styleClass="inputTexto" property="nif" size="15" maxlength="10" onkeypress="javascript:PasaAMayusculas(event);"/>
                </td>
              </tr>      
      </table>
      </div>
     </div>
    <div class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGuardar")%> name="cmdAceptar"  onClick="pulsarAceptar();">
    </div>
</div>
</html:form>
<script type="text/javascript">
document.onmouseup = checkKeys;
var comboIdioma = new Combo("Idioma");
</script></BODY></html:html>
