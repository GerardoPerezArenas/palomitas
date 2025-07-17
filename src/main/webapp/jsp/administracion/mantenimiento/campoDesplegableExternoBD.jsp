<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html> 
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Alta Valores de Campos Desplegables</title>

<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 1;
    int apl = 3;
    String css="";
    if (session.getAttribute("usuario") != null) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
        css=usuarioVO.getCss();
}%>

<!-- Estilos -->

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>


<script type="text/javascript">
var lista = new Array();
var datosCamposDesplegables = new Array();
var codDriver_JDBC= new Array();
var descDriver_JDBC=new Array();

// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
function inicializar(){
    window.focus();
    //carga la tabla
    recuperaDatosIniciales();

    //combo Driver_jdbc
    codDriver_JDBC[0]=0;
    codDriver_JDBC[1]=1;
    descDriver_JDBC[0]='oracle.jdbc.driver.OracleDriver';
    descDriver_JDBC[1]='com.microsoft.sqlserver.jdbc.SQLServerDriver';
    comboDriver_JDBC.activate();
    comboDriver_JDBC.addItems(codDriver_JDBC, descDriver_JDBC);



    }
function recuperaDatosIniciales(){
//Se obtiene los valores de bd de los campos desplegables externos
<%MantenimientosAdminForm bForm =(MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
Vector listaValores = bForm.getListaValoresCamposDesplegables();
GeneralValueObject otrosDatos = bForm.getOtrosDatos();
int lengthCampoDesplegables2 = listaValores.size();
int z = 0;
%>
<%for(z=0;z<lengthCampoDesplegables2;z++){
    GeneralValueObject CampoDesplegable2 = (GeneralValueObject)listaValores.get(z);%>
    document.forms[0].descDriver_JDBC.value='<%=(String)CampoDesplegable2.getAtributo("descDriver_JDBC")%>';
    document.forms[0].urlDriver.value='<%=(String)CampoDesplegable2.getAtributo("urlDriver")%>';
    document.forms[0].usuario.value='<%=(String)CampoDesplegable2.getAtributo("usuario")%>';
    document.forms[0].contrasena.value='<%=(String)CampoDesplegable2.getAtributo("contrasena")%>';
    document.forms[0].tabla.value='<%=(String)CampoDesplegable2.getAtributo("tabla")%>';
    document.forms[0].campoCodigo.value='<%=(String)CampoDesplegable2.getAtributo("campoCodigo")%>';
    document.forms[0].campoValor.value='<%=(String)CampoDesplegable2.getAtributo("campoValor")%>';
    document.forms[0].campoValorId2.value='<%=(String)CampoDesplegable2.getAtributo("campoValorId2")%>';
<%}%>
document.forms[0].codigo.value='<%=(String)otrosDatos.getAtributo("campo")%>';
document.forms[0].descripcion.value='<%=(String)otrosDatos.getAtributo("descCampo")%>';
<%if(otrosDatos.getAtributo("errores") != null){%>
      if ('<%=(String)otrosDatos.getAtributo("errores")%>'=="0"){
            jsp_alerta("A",'<%=descriptor.getDescripcion("msj_guardaDesplExternoError")%>');
      }else{
            jsp_alerta("A",'<%=descriptor.getDescripcion("msj_guardaDesplExternoSi")%>');
            }
<%}
 if(otrosDatos.getAtributo("conexion") != null){%>
         if ('<%=(String)otrosDatos.getAtributo("conexion")%>'=="0"){
             jsp_alerta("A",'<%=descriptor.getDescripcion("msj_ConexionNo")%>');
         }else{
             jsp_alerta("A",'<%=descriptor.getDescripcion("msj_ConexionSi")%>');
             }
<%}
 if(otrosDatos.getAtributo("insercion") != null){%>
         if ('<%=(String)otrosDatos.getAtributo("insercion")%>'=="0"){
             jsp_alerta("A",'<%=descriptor.getDescripcion("msj_insertaDesExtVNo")%>');
         }else{
             jsp_alerta("A",'<%=descriptor.getDescripcion("msj_insertaDesExtVSi")%>');
        }
<%} %>
 

}


// FUNCIONES DE PULSACION DE BOTONES
function pulsarGuardar(){
    document.forms[0].opcion.value = 'modifica';
    document.forms[0].target = "oculto";
    document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegablesExterno.do';
    document.forms[0].submit();
}

function pulsarProbarConexion(){
    document.forms[0].opcion.value = 'probarConexion';
    document.forms[0].target = "oculto";
    document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegablesExterno.do';
    document.forms[0].submit();
}

function pulsarImportar(){
    document.forms[0].opcion.value = 'importar';
    document.forms[0].target = "oculto";
    document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegablesExterno.do';
    document.forms[0].submit();
}

function pulsarVolver(){
    document.forms[0].opcion.value = 'cargar';
    document.forms[0].target = "mainFrame";
    document.forms[0].action = '<%=request.getContextPath()%>/administracion/mantenimiento/CamposDesplegablesExterno.do';
    document.forms[0].submit();
}



  </script>
</head>
<body class="bandaBody" onload="javascript:{pleaseWait('off');inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form name="formulario" method="post">
  <input  type="hidden"  name="opcion" id="opcion">
  <input  type="hidden"  name="identificador" id="identificador">

    <div id="titulo" class="txttitblanco"><%= descriptor.getDescripcion("gEtiq_AltaCamposDespExterno")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqCodigoCampoExterno")%></td>
                        <td>
                            <input type="text" class="inputTextoObligatorio" name="codigo" id="codigo" size="5" maxlength="4" onkeyup="return xAMayusculas(this);" readonly="realonly"/>
                        </td>
                        </tr>
                         <tr>

                    </tr>
                </table>

            </td>
        </tr>


        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqDescripCampoExterno")%></td>
                        <td>
                            <input name="descripcion" id="descripcion" type="text" class="inputTextoObligatorio" size="75" maxlength="75" onblur="return xAMayusculas(this);" readonly="realonly"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>


        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqDriverCampoExterno")%></td>
                        <td>
                            <input type="hidden" name="codDriver_JDBC"  id="codDriver_JDBC"/>
                            <input styleId="obligatorio" type="text" class="inputTextoObligatorioSinMayusculas" name="descDriver_JDBC"  id="descDriver_JDBC" class="inputTexto" style="width:90%" readonly="true" value=""/>
                            <a href="" id="anchorDriver_JDBC" name="anchorDriver_JDBC">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonDriver_JDBC" name="botonDriver_JDBC" style="border: 0"></span>
                            </a>
                    </td></tr>
                </table>
            </td>
        </tr>


        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqUrlJdbcCampoExterno")%></td>
                        <td>
                            <input id ="urlDriver" name="urlDriver" type="text" class="inputTextoObligatorioSinMayusculas" style="width:100%" maxlength="200">
                    </td></tr>
                </table>
            </td>
        </tr>

        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqUsuarioCampoExterno")%></td>
                        <td>
                            <input id ="usuario" name="usuario" type="text" class="inputTextoObligatorioSinMayusculas" style="width:100%" maxlength="200">
                    </td></tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqContrasenaCampoExterno")%></td>
                        <td>
                            <input id ="contrasena" name="contrasena" type="password" class="inputTextoObligatorioSinMayusculas" style="width:100%" maxlength="200">
                    </td></tr>
                </table>
            </td>
        </tr>



        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqTablaCampoExterno")%></td>
                        <td>
                            <input id ="tabla" name="tabla" type="text" class="inputTextoObligatorioSinMayusculas" size="60" maxlength="200">
                    </td></tr>
                </table>
            </td>
        </tr>

        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqCodTablaCampoExterno")%></td>
                        <td>
                            <input id ="campoCodigo" name="campoCodigo" type="text" class="inputTextoObligatorioSinMayusculas" size="60" maxlength="200">
                    </td></tr>
                </table>
            </td>
        </tr>

        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqValorTablaCampoExterno")%></td>
                        <td>
                            <input id ="campoValor" name="campoValor" type="text" class="inputTextoObligatorioSinMayusculas" size="60" maxlength="200">
                    </td></tr>
                </table>
            </td>
        </tr>
        
        <!--Campo para descripcion en segundo idioma-->
        <tr>
            <td>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr><td style="width: 15%" class="etiqueta"><%=descriptor.getDescripcion("etiqValorId2TablaCampoExterno")%></td>
                        <td>
                            <input id ="campoValorId2" name="campoValorId2" type="text" class="inputTextoSinMayusculas" size="60" maxlength="200">
                    </td></tr>
                </table>
            </td>
        </tr>
        </table>
        <div class="botoneraPrincipal"> 
            <input type= "button" class="botonMasLargo" value="<%=descriptor.getDescripcion("gbProbarConexion")%>" name="probarConexion" onClick="pulsarProbarConexion();">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbGuardar")%>" name="botonGuardar" onClick="pulsarGuardar();">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVolver")%>" name="botonVolver" onClick="pulsarVolver();">
        </div>
    </div>
</form>



<script type="text/javascript">
 
var comboDriver_JDBC = new Combo("Driver_JDBC");

  
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
           if (comboDriver_JDBC.base.style.visibility == "visible" && isClickOutCombo(comboDriver_JDBC,coordx,coordy)) setTimeout('comboDriver_JDBC.ocultar()',20);
          
        }
        if (teclaAuxiliar == 9){
           if (comboDriver_JDBC.base.style.visibility == "visible") setTimeout('comboDriver_JDBC.ocultar()',20);
          
        }
        
             

    keyDel(evento);
}

</script>

</body>
</html>
