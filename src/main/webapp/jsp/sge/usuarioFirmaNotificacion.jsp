<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
    
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
<head>
    <jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: LISTA TRAMITES DEL FLUJO DE SALIDA :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
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
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>

<SCRIPT>
var datos = new Array();
var cont = 0;

function inicializar() {
        var tipoUsuarioFirma='<bean:write name="DefinicionTramitesForm" property="tipoUsuarioFirma"/>';
        var codigoOtroUsuarioFirma='<bean:write name="DefinicionTramitesForm" property="codigoOtroUsuarioFirma"/>';
        var nombreOtroUsuarioFirma='<bean:write name="DefinicionTramitesForm" property="nombreOtroUsuarioFirma"/>';

        document.forms[0].codFirma.value=tipoUsuarioFirma;

        var vector = new Array(document.forms[0].edtIdFirmanteDoc,document.forms[0].nombreUsuarioFirmante);

        if(tipoUsuarioFirma=='1')
        {
                comboFirma.selectItem(1);
                document.forms[0].edtIdFirmanteDoc.value=codigoOtroUsuarioFirma;
                document.forms[0].nombreUsuarioFirmante.value=nombreOtroUsuarioFirma;
                activaBusquedaUsuario();
        }
        else
        {
              if(tipoUsuarioFirma=='0') comboFirma.selectItem(0);
              else  comboFirma.selectItem(-1);

              document.forms[0].edtIdFirmanteDoc.value='';
              document.forms[0].nombreUsuarioFirmante.value='';
              desActivaBusquedaUsuario();
              
        }
    
}
function activaBusquedaUsuario()
{
    var vector = new Array(document.forms[0].edtIdFirmanteDoc,document.forms[0].nombreUsuarioFirmante);
    habilitarGeneral(vector);
    document.forms[0].btnUsuariosFirmantes.disabled = false;
    document.forms[0].btnUsuariosFirmantes.style.cursor = 'pointer';
}

function desActivaBusquedaUsuario()
{
    var vector = new Array(document.forms[0].edtIdFirmanteDoc,document.forms[0].nombreUsuarioFirmante);
    deshabilitarGeneral(vector);
    document.forms[0].btnUsuariosFirmantes.disabled = true;
    document.forms[0].btnUsuariosFirmantes.style.cursor = 'default';
    document.forms[0].edtIdFirmanteDoc.value='';
    document.forms[0].nombreUsuarioFirmante.value='';
}

function modificarCertificadoOrganismo(){
    var checkOblig = document.getElementsByName("certificadoOrganismoFirmaNotificacion");
    if (checkOblig && checkOblig[0]){
        document.getElementsByName("certificadoOrganismoFirmaNotificacionValue")[0].value = (checkOblig[0].checked)?"on":"off";
    }
}

function pulsarBuscarUsuario() {
    var source = '<c:url value="/portafirmas/delegacionfirma/PopUpSeleccionUsuario.do"/>';
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,null,
        'width=700,height=500,status=no',function(rslt){
                    if (rslt != undefined) {
                        setPopUpResult(rslt[0], rslt[1]);
                    }
            });
}

function setPopUpResult(r1,r2)
{
    document.forms[0].edtIdFirmanteDoc.value=r1;
    document.forms[0].nombreUsuarioFirmante.value=r2;
}

function pulsarAceptar() {
    var s1 = document.getElementById("codFirma").value;
    if ((s1=='1') && (document.forms[0].edtIdFirmanteDoc.value=='') ) {
        jsp_alerta("A","<fmt:message key='Sge.PlantillaFirmaForm.RequiereFirma.Message.MustSelectUser'/>","SIGP");
    } else {
        self.parent.opener.retornoXanelaAuxiliar([s1, document.forms[0].edtIdFirmanteDoc.value,document.forms[0].nombreUsuarioFirmante.value,document.forms[0].certificadoOrganismoFirmaNotificacionValue.value]);
    }
}
function pulsarSalir() {
    self.parent.opener.retornoXanelaAuxiliar();
}

</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{inicializar();}">
<html:form action="/sge/DefinicionTramites.do">
<html:hidden  property="certificadoOrganismoFirmaNotificacionValue"   />
<div class="txttitblanco"><%=descriptor.getDescripcion("tit_usuariosFirmaNotifElect")%></div>
<div class="contenidoPantalla">
    <table class="contenidoPestanha">
        <tr>
            <td style="width: 25%" class="etiqueta"><%= descriptor.getDescripcion("etiq_requiereFirma")%>:</td>
            <td style="width: 75%">
                <input name="requiereFirma" type="hidden" class="inputTextoObligatorio" id="codFirma"/>
                <input name="requiereFirmaDesc" type="text" class="inputTextoObligatorio" id="descFirma" style="width:84%" onselect="" readonly/>
                <a id="anchorFirma" name="anchorFirma">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonFirma" name="botonFirma"></span>
                </a>
            </td>
        </tr>
        <tr>
            <td style="width: 25%; padding-top: 10px" class="etiqueta"><%= descriptor.getDescripcion("etiq_usuFirmantes")%>:</td>
            <td style="width: 75%; padding-top: 10px">
                <input name="edtIdFirmanteDoc" type="text" style="width:8%" class="inputTextoObligatorio" id="edtIdFirmanteDoc" readOnly>
                <input name="nombreUsuarioFirmante" type="text" class="inputTextoObligatorio" id="nombreUsuarioFirmante" style="width:75%" readOnly>
                 <a onclick="pulsarBuscarUsuario();">
                    <span class="fa fa-search" aria-hidden="true"  id="btnUsuariosFirmantes" name="btnUsuariosFirmantes"></span>
                </a>
            </td>
        </tr>
        <tr>
            <td style="width: 25%; padding-top: 10px" class="etiqueta"><%= descriptor.getDescripcion("certificadoNotif")%></td>
            <td style="width: 75%; padding-top: 10px">
                <html:checkbox style="margin-right: 5px" property="certificadoOrganismoFirmaNotificacion" onclick="modificarCertificadoOrganismo()"> </html:checkbox>
            </td>
        </tr>
    </table>
    <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" value="<%= descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" id="cmdAceptar" onClick="pulsarAceptar();">
        <INPUT type= "button" class="botonGeneral" value="<%= descriptor.getDescripcion("gbSalir")%>" name="cmdSalir"  onClick="pulsarSalir();">
    </DIV>
</div>
 </html:form>
    <script type="text/javascript">
        var comboFirma = new Combo("Firma");
        var listaCod = [["0"], ["1"]];
        var listaDesc = [["<%= descriptor.getDescripcion("etiq_firmaTramitador")%>"],["<%= descriptor.getDescripcion("etiq_firmaOtroUsu")%>"]];
        var cont = 0;
        comboFirma.change = function() {

            if( document.forms[0].codFirma.value=='1') activaBusquedaUsuario();
                else desActivaBusquedaUsuario();
        }
        comboFirma.addItems(listaCod, listaDesc);

<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;

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
                if (comboFirma.base.style.visibility == "visible" && isClickOutCombo(comboFirma,coordx,coordy)) setTimeout('comboFirma.ocultar()',20);
            }
            if (teclaAuxiliar == 9){
                comboFirma.ocultar();
            }
        }
        
        //updateFrmMantenimientoDocTramite();
    </script>


</BODY>

</html:html>
