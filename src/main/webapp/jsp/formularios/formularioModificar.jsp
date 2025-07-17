<%@page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%@page import="java.util.Vector" %>
<%@ page import="es.altia.agora.business.util.ElementoListaValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>

<html:html>
<head>
<jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE> CONSULTA DE ASIENTOS</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<%
    int idioma=1;
    int apl=15;
    int munic=0;
    int orgCod = 0;
    int entCod = 0;
    String css="";
    UsuarioValueObject usuario=new UsuarioValueObject();
    FichaFormularioForm fichaForm=new FichaFormularioForm();
    if (session!=null){
      if (usuario!=null) {
        usuario = (UsuarioValueObject)session.getAttribute("usuario");
        fichaForm = (FichaFormularioForm)session.getAttribute("FichaFormularioForm");
        idioma = usuario.getIdioma();
        apl = usuario.getAppCod();
        munic = usuario.getOrgCod();
        orgCod = usuario.getOrgCod();
        entCod = usuario.getEntCod();
         css = usuario.getCss();
      }
    }


	Config m_Conf = ConfigServiceHelper.getConfig("formulariosPdf");
	String Carga_XML = m_Conf.getString("XML.precargas");
	String xml = "visible";
	if("no".equals(Carga_XML)) xml = "hidden";

        String origenPlantilla = m_Conf.getString(orgCod+"/origenPlantillas");
	boolean ficheroObligatorio = true;
	if (origenPlantilla.equals("DIRECTORIO")) {
            ficheroObligatorio=false;
        }
String opcion=request.getParameter("opcion");
String fAlta = "";
SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
String userAgent = request.getHeader("user-agent");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<!-- Estilos -->
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>

<SCRIPT type="text/javascript">

// Listas de valores.
var nombres_precargas= new Array();
var cod_tipo= new Array();
var desc_tipo= new Array();
var cod_formRel= new Array();
var desc_formRel= new Array();
var cod_proc= new Array();
var desc_proc= new Array();
var codUnidadRegOrigen = new Array();
var descUnidadRegOrigen = new Array();
var datosUnidadRegOrigen = new Array();
var codUnidadRegDestino = new Array();
var descUnidadRegDestino = new Array();
var datosUnidadRegDestino = new Array();
var unidadesRegHabilitadas = false;
var cod_area= new Array();
var desc_area= new Array();
var cod_area_defecto= new Array();
var cod_proc_defecto= new Array();
var cod_tipDem= new Array();
var desc_tipDem= new Array();
var cod_tipFirma= new Array();
var desc_tipFirma= new Array();
var uors = new Array();
var uorcods = new Array();
var desc_uors = new Array();
var cargos = new Array();
var cargoscods = new Array();
var desc_cargos = new Array();
var consultando = false;
var cadForms = new Array();
var cadForms2 = new Array();
var procForms = new Array();
var cod_relacionTramite = new Array('A','B','C','D');
var desc_relacionTramite = new Array('TRÁMITE A GENERAR AL ENVIAR EL FORMULARIO',
                                    'TRAMITE QUE DEBE EXISTIR ABIERTO',
                                    'TRÁMITE AL QUE SE ASOCIA EL FORMULARIO',
                                    'TRÁMITE EN EL QUE SE FIRMA EL FORMULARIO');

var cod_relacionTramite_A = new Array('A','D');
var desc_relacionTramite_A = new Array('TRÁMITE A GENERAR AL ENVIAR EL FORMULARIO','TRÁMITE EN EL QUE SE FIRMA EL FORMULARIO');

var cod_relacionTramite_AB = new Array('A','B','D');
var desc_relacionTramite_AB = new Array('TRÁMITE A GENERAR AL ENVIAR EL FORMULARIO',
                                    'TRAMITE QUE DEBE EXISTIR ABIERTO',
                                    'TRÁMITE EN EL QUE SE FIRMA EL FORMULARIO');
var cod_relacionTramite_C = new Array('C', 'D');
var desc_relacionTramite_C = new Array('TRÁMITE AL QUE SE ASOCIA EL FORMULARIO','TRÁMITE EN EL QUE SE FIRMA EL FORMULARIO');

var cod_tramiteProcedimiento = new Array();
var desc_tramiteProcedimiento = new Array();

var tramites = new Array();
var tramitesOriginal = new Array();
var restricciones = new Array();
var restriccionesOriginal = new Array();

var validaEdicion ='nulo';
var validaImpresion = 'nulo';
var opcionAction = '<%=opcion%>';



function crearListaPrecargas() {

  var listaXml = "";
  for (i=0; i < nombres_precargas.length; i++) {
    var caja = "box" + i;
    if(eval("document.forms[0]." + caja + ".checked") == true) {
	  listaXml +=nombres_precargas[i][1]+'§¥';
	}
  }
	document.forms[0].listaPrecargasXml.value=listaXml;
  return;
}

function comprobarObligatoriosTablaTramites() {
  if(document.forms[0].descTramiteProcedimiento.value == '' ||
     document.forms[0].descRelacionTramite.value == '') {
       return false;
  } else {
    return true;
  }
}

function pulsarAltaTablaTramites() {
  var cod = document.forms[0].codTramiteProcedimiento.value;
  var yaExiste = 0;
  if(comprobarObligatoriosTablaTramites()) {
    for(l=0; l < tramitesOriginal.length; l++){
      if ((tramitesOriginal[l][1]) == cod ){
        yaExiste = 1;
      }
    }

    if(yaExiste == 0) {
      var lineas = tab.lineas;
      i = lineas.length;

      if ((document.forms[0].codRelacionTramite.value=="A") || (document.forms[0].codRelacionTramite.value=="B")) {
          if (document.forms[0].estado[0].checked) {
              estado ="Abierto";
              codEstado="A";
          } else {
              estado ="Cerrado";
              codEstado="C";
          }
      } else {
          estado="";
          codEstado=" ";
      }

      tramites[i]=[document.forms[0].codProcedimiento.value, document.forms[0].descTramiteProcedimiento.value, document.forms[0].codRelacionTramite.value, estado];
      tramitesOriginal[i]=[document.forms[0].codProcedimiento.value,
                                    document.forms[0].codTramiteProcedimiento.value,
                                    document.forms[0].codRelacionTramite.value,
                                    codEstado];
      tab.lineas=tramites;
      refresca();
      borrarDatostablaTramites();
    } else {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
    }
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosTramite")%>');
  }
}

function pulsarModificarTablaTramites() {
    if(tab.selectedIndex != -1) {
        var cod = document.forms[0].codTramiteProcedimiento.value;
        var yaExiste = 0;
        if (comprobarObligatoriosTablaTramites()) {
          for(l=0; l < tramitesOriginal.length; l++){
            if(l != tab.selectedIndex) {
              if ((tramitesOriginal[l][1]) == cod ){
                yaExiste = 1;
              }
            }
          }
          if(yaExiste == 0) {
            var j = tab.selectedIndex;

            if ((document.forms[0].codRelacionTramite.value=="A") || (document.forms[0].codRelacionTramite.value=="B")) {
                if (document.forms[0].estado[0].checked) {
                    estado ="Abierto";
                    codEstado="A";
                } else {
                    estado ="Cerrado";
                    codEstado="C";
                }
            } else {
                estado="";
                codEstado=" ";
            }
            tramites[j]=[document.forms[0].codProcedimiento.value, document.forms[0].descTramiteProcedimiento.value, document.forms[0].codRelacionTramite.value, estado];
            tramitesOriginal[j]=[document.forms[0].codProcedimiento.value,
                                            document.forms[0].codTramiteProcedimiento.value,
                                            document.forms[0].codRelacionTramite.value,
                                            codEstado];
            tab.lineas=tramites;
            refresca();
            borrarDatostablaTramites();
          } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
          }
        } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosTramite")%>');
        }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');

}

function pulsarEliminarTablaTramites() {
   if(tab.selectedIndex != -1) {
       if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarTramite")%>')) {
       var list = new Array();
       var listOrig = new Array();
       tamIndex=tab.selectedIndex;
       tamLength=tab.lineas.length;
       for (i=tamIndex - 1; i < tramites.length - 1; i++){
         if (i + 1 <= tramites.length - 2){
           tramites[i + 1]=tramites[i + 2];
           tramitesOriginal[i + 1]=tramitesOriginal[i + 2];
         }
       }
       for(j=0; j < tramites.length-1 ; j++){
         list[j] = tramites[j];
         listOrig[j] = tramitesOriginal[j];
         listOrig[j][5] = j+1;
       }
       tab.lineas=list;
       refresca();
       borrarDatostablaTramites();
       tramites=list;
       tramitesOriginal = listOrig;
       } else {
         tab.selectLinea(tab.selectedIndex);
         tab.selectedIndex = -1;
         borrarDatostablaTramites();
       }
   } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarLimpiarTablaTramites() {
  borrarDatostablaTramites();
  if(tab.selectedIndex != -1 ) {
    tab.selectLinea(tab.selectedIndex);
    tab.selectedIndex = -1;
  }
}

function borrarDatostablaTramites() {
    document.forms[0].codTramiteProcedimiento.value="";
    document.forms[0].descTramiteProcedimiento.value="";
    document.forms[0].codRelacionTramite.value="";
    document.forms[0].descRelacionTramite.value="";
  document.forms[0].estado[0].disabled=false;
  document.forms[0].estado[0].checked=true;
  document.forms[0].estado[1].disabled=false;
}


function comprobarObligatoriosTablaRestricciones() {
  if ((document.forms[0].descUORs.value == '') || ((document.forms[0].descCargo.value == '') && !(document.forms[0].todos.checked))){
       return false;
  } else {
    return true;
  }
}

function pulsarAltaTablaRestricciones() {
  onchangeCodUOR();
  onchangeCodCargo();
  var codUOR = document.forms[0].codUOR.value;
  var codCargo = document.forms[0].codigoCargo.value;
  var yaExiste = 0;

  if(!document.forms[0].accesible.checked){
  if(comprobarObligatoriosTablaRestricciones()) {
    for(l=0; l < restriccionesOriginal.length; l++){
      if ((restriccionesOriginal[l][0] == codUOR) && (restriccionesOriginal[l][1] == codCargo) ){
        yaExiste = 1;
      }
      if ((restriccionesOriginal[l][0] == codUOR) && (restriccionesOriginal[l][1] == 0) ){
        yaExiste = 1;
      }
    }
    if(yaExiste == 0) {
      if (codCargo == 0) {
          var list = new Array();
          var listOrig = new Array();
          var j=0;
          for(l=0; l < restriccionesOriginal.length; l++){
            if (restriccionesOriginal[l][0] != codUOR){ //Eliminar las que ya fueron insertadas con la misma UOR, y ahora se quiere insertar todos los cargos
               list[j] = restricciones[l];
               listOrig[j] = restriccionesOriginal[l];
               j++;
            }
         }
         tabRestricciones.lineas=list;
         refrescaRestricciones();
         restricciones=list;
         restriccionesOriginal = listOrig;
      }
      var lineas = tabRestricciones.lineas;
      i = lineas.length;
      if (document.forms[0].codigoCargo.value=="") {document.forms[0].codigoCargo.value="0";};
      restricciones[i]=[document.forms[0].codUORs.value, document.forms[0].descUORs.value, document.forms[0].codCargo.value, document.forms[0].descCargo.value];
      restriccionesOriginal[i]=[document.forms[0].codUOR.value, document.forms[0].codigoCargo.value];
      tabRestricciones.lineas=restricciones;
      refrescaRestricciones();
      borrarDatostablaRestricciones();
    } else {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
    }
  } else {
    jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosTramite")%>');
  }
  }else{
  	jsp_alerta('A','<%=descriptor.getDescripcion("msjAccesibleTodos")%>');
  }
}

function pulsarModificarTablaRestricciones() {
    if(tabRestricciones.selectedIndex != -1) {
        var codUOR = document.forms[0].codUOR.value;
        var codCargo = document.forms[0].codigoCargo.value;
        var yaExiste = 0;
        if(comprobarObligatoriosTablaRestricciones()) {
          for(l=0; l < restriccionesOriginal.length; l++){
              if(l != tabRestricciones.selectedIndex) {
                if ((restriccionesOriginal[l][0] == codUOR) && (restriccionesOriginal[l][1] == codCargo) ){
                  yaExiste = 1;
                }
              }
          }
          if(yaExiste == 0) {
              if (codCargo == 0) {
                  var list = new Array();
                  var listOrig = new Array();
                  var j=0;
                  var k = tabRestricciones.selectedIndex;
                  for(l=0; l < restriccionesOriginal.length; l++){
                    if ((restriccionesOriginal[l][0] != codUOR) && (l!=k)){ //Eliminar las que ya fueron insertadas con la misma UOR, y ahora se quiere insertar todos los cargos
                       list[j] = restricciones[l];
                       listOrig[j] = restriccionesOriginal[l];
                       j++;
                    }
                  }
                  tabRestricciones.lineas=list;
                  refrescaRestricciones();
                  restricciones=list;
                  restriccionesOriginal = listOrig;
                  j = tabRestricciones.lineas.length;
                  restricciones[j]=[document.forms[0].codUORs.value, document.forms[0].descUORs.value, document.forms[0].codCargo.value, document.forms[0].descCargo.value];
                  restriccionesOriginal[j]=[document.forms[0].codUOR.value, document.forms[0].codigoCargo.value];
                  tabRestricciones.lineas=restricciones;
                  refrescaRestricciones();
                  borrarDatostablaRestricciones();
              } else {
                  var j = tabRestricciones.selectedIndex;
                  restricciones[j]=[document.forms[0].codUORs.value, document.forms[0].descUORs.value, document.forms[0].codCargo.value, document.forms[0].descCargo.value];
                  restriccionesOriginal[j]=[document.forms[0].codUOR.value, document.forms[0].codigoCargo.value];
                  tabRestricciones.lineas=restricciones;
                  refrescaRestricciones();
                  borrarDatostablaRestricciones();
              }
          } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjCodExiste")%>');
          }
        } else {
          jsp_alerta('A','<%=descriptor.getDescripcion("msjFaltaDatosTramite")%>');
        }
    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarEliminarTablaRestricciones() {
   if(tabRestricciones.selectedIndex != -1) {
       if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarRestriccion")%>')) {
       var list = new Array();
       var listOrig = new Array();
       tamIndex=tabRestricciones.selectedIndex;
       tamLength=tabRestricciones.lineas.length;
       for (i=tamIndex - 1; i < restricciones.length - 1; i++){
         if (i + 1 <= restricciones.length - 2){
           restricciones[i + 1]=restricciones[i + 2];
           restriccionesOriginal[i + 1]=restriccionesOriginal[i + 2];
         }
       }
       for(j=0; j < restricciones.length-1 ; j++){
         list[j] = restricciones[j];
         listOrig[j] = restriccionesOriginal[j];
         listOrig[j][5] = j+1;
       }
       tabRestricciones.lineas=list;
       refrescaRestricciones();
       borrarDatostablaRestricciones();
       restricciones=list;
       restriccionesOriginal = listOrig;
       } else {
         tabRestricciones.selectLinea(tabRestricciones.selectedIndex);
         tabRestricciones.selectedIndex = -1;
         borrarDatostablaRestricciones();
       }
   } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
}

function pulsarLimpiarTablaRestricciones() {
  borrarDatostablaRestricciones();
  if(tabRestricciones.selectedIndex != -1 ) {
    tabRestricciones.selectLinea(tabRestricciones.selectedIndex);
    tabRestricciones.selectedIndex = -1;
  }
}

function borrarDatostablaRestricciones() {
  document.forms[0].codUOR.value="";
  document.forms[0].codUORs.value="";
  document.forms[0].descUORs.value="";
  document.forms[0].codigoCargo.value="";
  document.forms[0].codCargo.value="";
  document.forms[0].descCargo.value="";
  document.forms[0].todos.checked=false;
  comboCargos.activate();
}


function inicializar() {

   habilitarImagenCal("calFechaAlta",true);
   habilitarImagenCal("calFechaBaja",true);
   window.focus();
   consultando = true;
   <%GeneralValueObject formVO = (GeneralValueObject)fichaForm.getFormularioVO();
   String codigo = formVO.getAtributo("codigo").toString();
   String nombreFormulario = formVO.getAtributo("nombre").toString();
   String codVisible = formVO.getAtributo("codVisible").toString();
   int numeroVersion = Integer.parseInt(formVO.getAtributo("version").toString())+1;
   String version = formVO.getAtributo("version").toString();
   String validacionFicheroEdicion=(String)formVO.getAtributo("validacionFicheroEdicion");
   String validacionFicheroImpresion=(String)formVO.getAtributo("validacionFicheroImpresion");

   %>
   validaEdicion='<%=validacionFicheroEdicion%>';
   validaImpresion='<%=validacionFicheroImpresion%>';
   <%

Date aux = new Date();
   if (opcion.equals("nuevaVersion")){

    version = String.valueOf(numeroVersion);
   fAlta = formato.format(aux);
} else if (opcion.equals("nuevaVersion")){
    aux = (Date) formVO.getAtributo("fAlta");
   fAlta = formato.format(aux);
} else {
   fAlta = formato.format(aux);
   version ="1";
}


   String fBaja="";
   if (formVO.getAtributo("fBaja").toString()!=null && formVO.getAtributo("fBaja").toString()!=""){
      aux = (Date) formVO.getAtributo("fBaja");
      fBaja = formato.format(aux);
   }
   String tipo = formVO.getAtributo("tipo").toString();
   String codRel = formVO.getAtributo("codRel").toString();%>

   <%String procedimiento = formVO.getAtributo("procedimiento").toString();
   String area = formVO.getAtributo("area").toString();
   String instruc = formVO.getAtributo("instruc").toString();
   String dTramite = formVO.getAtributo("dTramite").toString();
   String checkeddTramite="";
   if (dTramite.equals("1")){
        checkeddTramite=" checked";
   }
   String gTramite = formVO.getAtributo("gTramite").toString();
   String checkedgTramite="";
   if (gTramite.equals("1")){
        checkedgTramite=" checked";
   }
   String cerrarT1 = formVO.getAtributo("cerrarT1").toString();
   String checkedCerrarT1="";
   if (cerrarT1.equals("1")){
        checkedCerrarT1=" checked";
   }
   String instancia = formVO.getAtributo("instancia").toString();
   String checkedgInstancia="";
   if (instancia.equals("1")){
        checkedgInstancia=" checked";
   }
   String registro = formVO.getAtributo("registro").toString();
   String checkedgRegistro="";
   if (registro.equals("1")){
        checkedgRegistro=" checked";
    }
   String unidades = formVO.getAtributo("unidades").toString();
   String checkedAccesible="";
   if (unidades.equals("1")){
        checkedAccesible=" checked";
   }
   String visible = formVO.getAtributo("visible").toString();
   String checkedVisible="";
   if (visible.equals("1")){
        checkedVisible=" checked";
   }
   String dmte = formVO.getAtributo("dmte").toString();
   String tipoFirma = formVO.getAtributo("tipoFirma").toString();
   %>

   //COMBO DE TIPOS
   <%Vector listaTipos = fichaForm.getListaTipos();%>
   var j=0;
   <%for(int i=0;i<listaTipos.size();i++){
        GeneralValueObject tipos = (GeneralValueObject)listaTipos.get(i);%>
      	cod_tipo[j] = '<%=(String) tipos.getAtributo("codigo")%>';
      	desc_tipo[j] = '<%=(String) tipos.getAtributo("descripcion")%>';
        <%if (tipo.equals((String) tipos.getAtributo("codigo"))){%>
            document.forms[0].codTipo.value=cod_tipo[j];
            document.forms[0].descTipo.value=desc_tipo[j];
            tipoChange();
        <%}%>
        j++;
   <%}%>

comboTipologia.addItems(cod_tipo, desc_tipo);
   //COMBO DE FORMULARIOS TIPO0
   <%
   Vector listaFormularios = fichaForm.getListaFormulariosTipo0();
   if (listaFormularios == null) listaFormularios = new Vector();%>
   var j=0;
   <%for(int i=0;i<listaFormularios.size();i++){
        GeneralValueObject forms = (GeneralValueObject)listaFormularios.get(i);%>
        procForms['<%=(String) forms.getAtributo("codVisibleForm")%>']= '<%=(String) forms.getAtributo("procedimiento")%>';
        cadForms[<%=(String) forms.getAtributo("codForm")%>]= '<%=(String) forms.getAtributo("codVisibleForm")%>';
        cadForms2['<%=(String) forms.getAtributo("codVisibleForm")%>']= '<%=(String) forms.getAtributo("codForm")%>';
        cod_formRel[j] = '<%=(String) forms.getAtributo("codVisibleForm")%>';
        desc_formRel[j] = '<%=(String) forms.getAtributo("descForm")%>';
        <%if (codRel!="") {
            if (codRel.equals((String) forms.getAtributo("codForm"))) {%>
                document.forms[0].codFormRel.value=cod_formRel[j];
                document.forms[0].descFormRel.value=desc_formRel[j];
            <%}
        }%>
        j++;
   <%}%>
   comboFormularioRelacionado.addItems(cod_formRel, desc_formRel);
   formRelChange();
   <%
   //COMBO DE PROCEDIMIENTOS
   Vector listaProcedimientos = fichaForm.getListaProcedimientos();
   %>
   var j=0;
   <%for(int i=0;i<listaProcedimientos.size();i++){
          ElementoListaValueObject elvo=(ElementoListaValueObject) listaProcedimientos.get(i);%>
          cod_proc[j] = '<%=elvo.getCodigo()%>';
        desc_proc[j] = '<%=elvo.getDescripcion()%>';
        <%if (procedimiento.equals(elvo.getCodigo())){%>
            <%if (!tipo.equals("0")){%> //Solicitud
                document.forms[0].codProcedimiento.value=cod_proc[j];
                document.forms[0].descProcedimiento.value=desc_proc[j];
                deshabilitarIconos(document.getElementsByName("botonCargo"),false)
                comboProcedimiento.activate();
                procedimientoChange();
                comboProcedimiento.deactivate();
            <%} else {%>
            document.forms[0].codProcedimiento.value=cod_proc[j];
            document.forms[0].descProcedimiento.value=desc_proc[j];
            procedimientoChange();
        <%}%>
        <%}%>
        j++;
   <%}%>
comboProcedimiento.addItems(cod_proc, desc_proc);
    <%
   // COMBO DE LISTAS DE UNIDADES ORGANICAS DE REGISTRO  ORIGEN
   Vector listaUnidadesRegistroOrigen = fichaForm.getListaUORSRegistroOrigen();
   %>
   var j=0;
   <%for(int i=0;i<listaUnidadesRegistroOrigen.size();i++){
        UORDTO uorRegistroOrigen = (UORDTO) listaUnidadesRegistroOrigen.get(i); %>
            codUnidadRegOrigen[j] = '<%=uorRegistroOrigen.getUor_cod_vis()%>';
            descUnidadRegOrigen[j] = '<%=uorRegistroOrigen.getUor_nom()%>';
            datosUnidadRegOrigen[j] = ['<%=uorRegistroOrigen.getUor_cod()%>','<%=uorRegistroOrigen.getUor_cod_vis()%>','<%=uorRegistroOrigen.getUor_nom()%>' ];
        j++;

   <%}%>

comboRegistroOrigen.addItems(codUnidadRegOrigen,descUnidadRegOrigen);
   <%
   // COMBO DE LISTAS DE UNIDADES ORGANICAS DE REGISTRO  DESTINO
   Vector listaUnidadesRegistroDestino = fichaForm.getListaUORSRegistroDestino();
   %>
   var j=0;
   <%for(int i=0;i<listaUnidadesRegistroDestino.size();i++){
        UORDTO uorRegistroDestino = (UORDTO) listaUnidadesRegistroDestino.get(i); %>
            codUnidadRegDestino[j] = '<%=uorRegistroDestino.getUor_cod_vis()%>';
            descUnidadRegDestino[j] = '<%=uorRegistroDestino.getUor_nom()%>';
            datosUnidadRegDestino[j] = ['<%=uorRegistroDestino.getUor_cod()%>','<%=uorRegistroDestino.getUor_cod_vis()%>','<%=uorRegistroDestino.getUor_nom()%>'];
        j++;

   <%}%>
   comboRegistroDestino.addItems(codUnidadRegDestino,descUnidadRegDestino);

  <%
  if (registro.equals("1")){ %>
        habilitarCombosUnidadesRegistro(true);

        // si el formulario genera registro, entonces cargamos las unidaes de registro que tenia asignadas
        document.forms[0].codUorRegOrigen.value = '<%=formVO.getAtributo("codVisRegOrigen")%>';
        document.forms[0].descUorRegOrigen.value = '<%=formVO.getAtributo("descVisRegOrigen")%>';
        document.forms[0].codUorRegDestino.value = '<%=formVO.getAtributo("codVisRegDestino")%>';
        document.forms[0].descUorRegDestino.value = '<%=formVO.getAtributo("descVisRegDestino")%>';
   <% } else {%>
        habilitarCombosUnidadesRegistro(false);
   <% } %>

   <%
   //COMBO DE AREAS
   Vector listaAreas = fichaForm.getListaAreas();
   %>
   var j=0;
   <%for(int i=0;i<listaAreas.size();i++){
        ElementoListaValueObject elvo=(ElementoListaValueObject) listaAreas.get(i);%>
        cod_area[j] = '<%=elvo.getCodigo()%>';
        desc_area[j] = '<%=elvo.getDescripcion()%>';
        <%if (area.equals(elvo.getCodigo())){%>
            document.forms[0].codArea.value=cod_area[j];
            document.forms[0].descArea.value=desc_area[j];
        <%}%>
        j++;
   <%}%>
comboAreasProcedimiento.addItems(cod_area,desc_area);
   <%
   //AREAS POR DEFECTO
   Vector listaAreasPorDefecto = fichaForm.getListaAreasPorDefecto();
   %>
   var j=0;
   <%for(int i=0;i<listaAreasPorDefecto.size();i++){
        GeneralValueObject gVO=(GeneralValueObject) listaAreasPorDefecto.get(i);%>
        cod_area_defecto[j] = "<%=gVO.getAtributo("area")%>";
        cod_proc_defecto[j] = "<%=gVO.getAtributo("procedimiento")%>";
        j++;
   <%}%>

    cod_tipDem[0]="0";
    cod_tipDem[1]="1";
    desc_tipDem[0]="USUARIO";
    desc_tipDem[1]="DEPARTAMENTO/SERVICIO";
    <%if (dmte.equals("0")) {%>
        document.forms[0].codTipoDemandante.value="0";
        document.forms[0].descTipoDemandante.value="USUARIO";
    <%} else if (dmte.equals("1")){%>
        document.forms[0].codTipoDemandante.value="1";
        document.forms[0].descTipoDemandante.value="DEPARTAMENTO/SERVICIO";
    <%}%>

	comboTipoTercero.addItems(cod_tipDem, desc_tipDem);

    cod_tipFirma[0]="0";
    cod_tipFirma[1]="1";
    desc_tipFirma[0]="SECUENCIAL";
    desc_tipFirma[1]="TRÁMITE";
    <%if (tipoFirma.equals("0")) {%>
        document.forms[0].codTipoFirma.value="0";
        document.forms[0].descTipoFirma.value="SECUENCIAL";
    <%} else if (tipoFirma.equals("1")){%>
        document.forms[0].codTipoFirma.value="1";
        document.forms[0].descTipoFirma.value="TRÁMITE";
    <%}%>
comboTiposFirma.addItems(cod_tipFirma, desc_tipFirma);
    //COMBO DE TRAMITES
    <%Vector tramitesProcedimiento = fichaForm.getListaTramitesProcedimiento();
    %>
    var j=0;
   <%for(int i=0;i<tramitesProcedimiento.size();i++){
           ElementoListaValueObject elvo=(ElementoListaValueObject) tramitesProcedimiento.get(i);%>
           cod_tramiteProcedimiento[j] = '<%=elvo.getIdentificador()%>';
           desc_tramiteProcedimiento[j++] = '<%=elvo.getDescripcion()%>';
   <%}%>

comboTramitesDisponibles.addItems(cod_tramiteProcedimiento,desc_tramiteProcedimiento);

    <%Vector listaUORDTOs = fichaForm.getListaUORs();
    for (int j=0; j<listaUORDTOs.size(); j++) {
        UORDTO dto = (UORDTO)listaUORDTOs.get(j);%>
        // array con los objetos tipo uor mapeados por el array de arriba
        uors[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
        // array con los códigos visibles
        uorcods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
desc_uors[<%=j%>] = '<%=dto.getUor_nom()%>';
        comboUnidades.addItems(uorcods, desc_uors);
    <%}%>
    // === Cargos
    <%Vector listaCargosDTOs = fichaForm.getListaCargos();
    for (int j=0; j<listaCargosDTOs.size(); j++) {
        UORDTO dto = (UORDTO)listaCargosDTOs.get(j);%>
        // array con los objetos tipo uor mapeados por el array de arriba
        cargos[<%=j%>] = new Uor<%=dto.toJavascriptArgs()%>;
        // array con los códigos visibles
        cargoscods[<%=j%>] = '<%=dto.getUor_cod_vis()%>';
        desc_cargos[<%=j%>] = '<%=dto.getUor_nom()%>';
        comboCargos.addItems(cargoscods,desc_cargos);
    <%}%>

//TABLA DE TRAMITES
<%Vector tramites = fichaForm.getListaTramites();
    for (int i=0; i<tramites.size(); i++) {
        GeneralValueObject t = (GeneralValueObject) tramites.elementAt(i);
        String estado="";
        if (t.getAtributo("estado").equals("A")) estado="Abierto";
        else if (t.getAtributo("estado").equals("C")) estado="Cerrado";
        else estado="";%>
tramites[<%=i%>] = ["<%= (String) t.getAtributo("procedimiento")%>",
            "<%= (String) t.getAtributo("tramite")%>",
            "<%= (String) t.getAtributo("relacion")%>",
            "<%= estado%>"];
tramitesOriginal[<%=i%>] = ["<%= (String) t.getAtributo("procedimiento")%>",
                            "<%= (String) t.getAtributo("codTramite")%>",
                            "<%= (String) t.getAtributo("relacion")%>",
                            "<%= (String) t.getAtributo("estado")%>"];
 <% } %>
    tab.lineas=tramites;
    refresca();

    // Tipo de restriccion de los formularios
    var tipoRestriccionFomulario = "<%=formVO.getAtributo("tipoRestriccionFormulario").toString() %>";
    if (tipoRestriccionFomulario == "0"){
       document.forms[0].tipoRestriccion[0].checked = true ;
       document.forms[0].tipoRestriccion[1].checked = false ;
    }else if (tipoRestriccionFomulario == "1"){
       document.forms[0].tipoRestriccion[1].checked = true;
       document.forms[0].tipoRestriccion[0].checked = false ;
    }

    //TABLA DE RESTRICCIONES
    <%Vector restricciones = fichaForm.getListaRestricciones();
	 for (int i=0; i<restricciones.size(); i++) {
	 	GeneralValueObject t = (GeneralValueObject) restricciones.elementAt(i);%>
	    restricciones[<%=i%>] = ["<%= (String) t.getAtributo("cod_visible_uor")%>",
                        "<%= (String) t.getAtributo("desc_uor")%>",
                        "<%= (String) t.getAtributo("cod_visible_cargo")%>",
                        "<%= (String) t.getAtributo("desc_cargo")%>"];
        restriccionesOriginal[<%=i%>] = ["<%= (String) t.getAtributo("cod_uor")%>",
                                    "<%= (String) t.getAtributo("cod_cargo")%>"];
     <% } %>
    tabRestricciones.lineas=restricciones;
    refrescaRestricciones();

    controlGInstancia();


       <%
   //TABLA DE PRECARGAS DE DATOS XML
   Vector listaPrecargas = fichaForm.getListaPrecargas();
   Vector precargasSeleccionadas = fichaForm.getPrecargasSeleccionadas();
   %>
   var j=0;
   var t=0;
   var estado="";
   <%for(int i=0;i<listaPrecargas.size();i++){%>
   estado="";

		<%GeneralValueObject precarga = (GeneralValueObject)listaPrecargas.get(i);%>
		<%for (int t=0;t<precargasSeleccionadas.size();t++){
			GeneralValueObject seleccion = (GeneralValueObject)precargasSeleccionadas.get(t);
			if(precarga.getAtributo("descripcion").equals(seleccion.getAtributo("descripcion"))){%>
				estado="checked";
				<%}%>
		<%}%>

		nombres_precargas[j]= ["<input type='checkbox' class='check' name='box" + j + "' value='SI' " + estado + ">",
								'<%=(String) precarga.getAtributo("descripcion")%>'];
		j++;

   <%}%>

  tabPrecargas.readOnly=true;
  tabPrecargas.lineas = nombres_precargas;
  refrescaPrecargas();

    tp1.setSelectedIndex(0);
}

function validarCampos(){
    var formCod = document.forms[0].formCod.value;
    var formVersion = document.forms[0].formVersion.value;
    var codTipo = document.forms[0].codTipo.value;
    var codFormRel = document.forms[0].codFormRel.value;
    var codTipoFirma = document.forms[0].codTipoFirma.value;
    var okFormRel = true;
    if (codTipo!="0" && codFormRel=="") okFormRel=false;
    var codProcedimiento = document.forms[0].codProcedimiento.value;
    var okProcedimiento = true;
    if (codTipo!="0" && codProcedimiento=="") okProcedimiento=false;
    var okArea = true;
    if (document.forms[0].codArea.value=="") okArea=false;
    var fechaAlta = document.forms[0].fechaAlta.value;

    var okUnidadesRegistro = true;
    if (document.forms[0].gRegistro.checked){
        if  (document.forms[0].codUorRegOrigen.value=="" || document.forms[0].codUorRegDestino.value==""){
            okUnidadesRegistro = false;
        }
    }

    if(formCod!="" && formVersion!="" && codTipo!="" && codTipoFirma!="" && fechaAlta!="" && okFormRel && 
        okProcedimiento && okArea && okUnidadesRegistro)
        return true;
  return false;
}

function validarTramites(){
    //Comprobar q por lo menos tiene untramite asociado.
    if (tramitesOriginal.length==0 && (document.forms[0].codTipo.value=="2"||document.forms[0].codTipo.value=="3"||document.forms[0].codTipo.value=="4")){
        return "<%=descriptor.getDescripcion("etiqNoTramites")%>";
    } else {
        //Comprobar Anexo con tramite (obligatorio un tramíte con tipo de relacion B)
        var resul="";
        if (document.forms[0].codTipo.value=="2") {
            var ok = false;
            for (i=0; i < tramitesOriginal.length; i++) {
                if (tramitesOriginal[i][2]=="B") {
                    ok = true;
                    break;
                }
            }
            if (!ok) resul = "<%=descriptor.getDescripcion("etiqInfoTram2")%>";
        }

        if (resul=="" && document.forms[0].gTramite.checked){
            var ok = false;
            if (tramitesOriginal.length>0) {
                for (i=0; i < tramitesOriginal.length; i++) {
                    if (tramitesOriginal[i][2]=="A") {
                        ok = true;
                        break;
                    }
                }
            }
            else ok = false;
            if (!ok) resul = "<%=descriptor.getDescripcion("etiqInfoGenTramite")%>";
        }
        return resul;
    }
}

function validarPDF(){
    var fich = document.forms[0].fichero.value;
    var aux = fich.split(".");
    var ext=aux[aux.length-1];
    if ((ext.toUpperCase()!="PDF") && (<%=ficheroObligatorio%>)) return false;

    else return true;
}

function validarPDF2(){
    var fich2 = document.forms[0].fichero2.value;
    var aux2 = fich2.split(".");
    var ext2=aux2[aux2.length-1];
    if ((ext2.toUpperCase()!="PDF") && (<%=ficheroObligatorio%>)) return false;
    else return true;
}

function habilitarCombosUnidadesRegistro(opcion){
    if (opcion){
        unidadesRegHabilitadas = true;

        document.forms[0].codUorRegOrigen.value="";
        document.forms[0].descUorRegOrigen.value="";
        document.forms[0].codUorRegDestino.value="";
        document.forms[0].descUorRegDestino.value="";

        comboRegistroOrigen.activate();
        comboRegistroDestino.activate();
    }else{
        unidadesRegHabilitadas = false;

        document.forms[0].codUorRegOrigen.value="";
        document.forms[0].descUorRegOrigen.value="";
        document.forms[0].codUorRegDestino.value="";
        document.forms[0].descUorRegDestino.value="";

        comboRegistroOrigen.deactivate();
        comboRegistroDestino.deactivate();
    }

}

function asignaCodigosUnidadesRegistro(){
    for (i=0; i < datosUnidadRegOrigen.length; i++) {
        if (datosUnidadRegOrigen[i][1] == document.forms[0].codUorRegOrigen.value){
            document.forms[0].UorRegOrigen.value = datosUnidadRegOrigen[i][0];
        }
    }

    for (j=0; j < datosUnidadRegDestino.length; j++) {
        if (datosUnidadRegDestino[j][1] == document.forms[0].codUorRegDestino.value){
            document.forms[0].UorRegDestino.value = datosUnidadRegDestino[j][0] ;
        }
    }

}


function crearListasTramites() {
  var listaTramProc = "";
  var listaTramCod = "";
  var listaTramRel = "";
  var listaTramEst = "";
  for (i=0; i < tramites.length; i++) {
    listaTramProc +=tramitesOriginal[i][0]+'§¥';
    listaTramCod +=tramitesOriginal[i][1]+'§¥';
    listaTramRel +=tramitesOriginal[i][2]+'§¥';
    listaTramEst +=tramitesOriginal[i][3]+'§¥';
  }
  document.forms[0].listaTramProc.value = listaTramProc;
  document.forms[0].listaTramCod.value = listaTramCod;
  document.forms[0].listaTramRel.value = listaTramRel;
  document.forms[0].listaTramEst.value = listaTramEst;
  return;
}

function crearListasRestricciones() {
  var listaRestriccionesUOR = "";
  var listaRestriccionesCargo = "";
  for (i=0; i < restricciones.length; i++) {
    listaRestriccionesUOR +=restriccionesOriginal[i][0]+'§¥';
    if (restriccionesOriginal[i][1]=="") {listaRestriccionesCargo += '0§¥';}  //Sin cargo --> Cargo = 0
    else {listaRestriccionesCargo += restriccionesOriginal[i][1]+'§¥';}
  }
  document.forms[0].listaRestriccionesUOR.value = listaRestriccionesUOR;
  document.forms[0].listaRestriccionesCargo.value = listaRestriccionesCargo;
  return;
}

function pulsarAceptar() {
  if (validarCampos()){
      var ok =true;
      crearListaPrecargas();
      var fichero = "";
      if (<%=ficheroObligatorio%>) fichero=document.forms[0].fichero.value;
      if (fichero!="")
          if (!validarPDF()) {
              jsp_alerta("A",'<%=descriptor.getDescripcion("gEtiqPlantilla")%> : <%=descriptor.getDescripcion("msjFicheroPDF")%>');
              ok=false;
          }
      var fichero2 = "";
      if (<%=ficheroObligatorio%>) fichero2=document.forms[0].fichero2.value;
      if ((fichero2!="") && ok)
          if (!validarPDF2()) {
              jsp_alerta("A",'<%=descriptor.getDescripcion("gEtiqPlantillaConsul")%> : <%=descriptor.getDescripcion("msjFicheroPDF")%>');
              ok=false;
          }
      if (ok) {
        resul = validarTramites();
        if (resul=="") {
            if (document.forms[0].codFormRel.value!=""){
                document.forms[0].codFormRel.value=cadForms2[document.forms[0].codFormRel.value];
            } else {
                document.forms[0].codFormRel.value=0;
            }
            crearListasTramites();
            crearListasRestricciones();
            asignaCodigosUnidadesRegistro();

            comboProcedimiento.activate();

            // tipo de restriccion del formulario
            if (document.forms[0].tipoRestriccion[0].checked){
                document.forms[0].tipoRestriccionForm.value = document.forms[0].tipoRestriccion[0].value;
            }else if (document.forms[0].tipoRestriccion[1].checked){
                document.forms[0].tipoRestriccionForm.value = document.forms[0].tipoRestriccion[1].value;
            }
            
            if (opcionAction=="cargar") document.forms[0].opcion.value = 'modificar';
            else document.forms[0].opcion.value = 'grabar';
            document.forms[0].target = "mainFrame";
            document.forms[0].action = '<%=request.getContextPath()%>/formularios/FichaFormulario.do';
            document.forms[0].submit();
        } else {
            jsp_alerta("A",resul);
        }
      }
  }
  else
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
}

function pulsarCancelar(){
  pleaseWait('on');
  document.forms[0].opcion.value="inicio";
  document.forms[0].target="mainFrame";
  document.forms[0].action = '<%=request.getContextPath()%>/formularios/Formularios.do';
  document.forms[0].submit();
}

function mostrarCalFechaAlta(evento) {
     if(window.event) evento = window.event;
            if (document.getElementById("calFechaAlta").className.indexOf("fa-calendar") != -1) {
                showCalendar('forms[0]', 'fechaAlta', null, null, null, '', 'calFechaAlta', '',null,null,null,null,null,null,null,null,evento);
            }
}
function mostrarCalFechaBaja(evento) {
    if(window.event) evento = window.event;
            if (document.getElementById("calFechaBaja").className.indexOf("fa-calendar") != -1) {
                showCalendar('forms[0]', 'fechaBaja', null, null, null, '', 'calFechaBaja', '',null,null,null,null,null,null,null,null,evento);
            }
}

function comprobarData(inputFecha) {
  if (Trim(inputFecha.value)!='') {
    if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
      jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
      if (navigator.appName.indexOf("Explorer") == -1) {
        inputFecha.value='';
      }
      return false;
    }
  }
 return true;
}
function comprobarFecha(inputFecha) {
  var formato = 'dd/mm/yyyy';
  if (Trim(inputFecha.value)!='') {
    if (consultando) {
      var validas = true;
      var fechaFormateada=inputFecha.value;
      var pos=0;
      var fechas = Trim(inputFecha.value);
      var fechas_array = fechas.split(/[:|&<>!=]/);
      for (var loop=0; loop < fechas_array.length; loop++) {
        f = fechas_array[loop];
        formato = formatoFecha(Trim(f));
        var D = ValidarFechaConFormato(f,formato);
        if (!D[0]) validas=false;
        else {
          if (fechaFormateada.indexOf(f,pos) != -1) {
            var toTheLeft = fechaFormateada.substring(0, fechaFormateada.indexOf(f));
            var toTheRight = fechaFormateada.substring(fechaFormateada.indexOf(f)+f.length, fechaFormateada.length);
            pos=fechaFormateada.indexOf(f,pos);
            fechaFormateada = toTheLeft + D[1]+ toTheRight;
          }
        }
      }
      if (!validas) {
        jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
        inputFecha.focus();
        if (navigator.appName.indexOf("Explorer") == -1) {
            inputFecha.value='';
        }
        return false;
      } else {
        inputFecha.value = fechaFormateada;
        return true;
      }
    } else { // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
      var D = ValidarFechaConFormato(inputFecha.value,formato);
      if (!D[0]){
        jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
        if (navigator.appName.indexOf("Explorer") == -1) {
            inputFecha.value='';
         }
        inputFecha.focus();
        return false;
      } else {
        inputFecha.value = D[1];
        return true;
      }
    }
  }
  return true;
}
function ValidarFechaConFormato(fecha, formato) {
  if (formato==null) formato ="dd/mm/yyyy";
  if (formato=="mm/yyyy")
      fecha = "01/"+fecha;
  else if (formato=="yyyy")
      fecha ="01/01/"+fecha;
  else if (formato =="mmyyyy")
      fecha = "01"+fecha;

  var D = DataValida(fecha);
  if (formato == "dd/mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr() : fecha;
  else if (formato == "mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  else if (formato == "yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(6) : fecha;
  else if (formato == "mmyyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  return D;
}

function onClickPDF(codigo) {
    var source = "<html:rewrite page='/VerPDFForm'/>?tipo=p1&codigo=" + codigo + "&codOrg=" + <%=orgCod%>;
    if (validaEdicion=='noNulo')
        window.open(source ,"ventana1","left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=no")
    else jsp_alerta ('A','<%=descriptor.getDescripcion("ficheroNulo")%>');
}

function onClickPDF2(codigo) {
    var source = "<html:rewrite page='/VerPDFForm'/>?tipo=p2&codigo=" + codigo + "&codOrg=" + <%=orgCod%>;
if(validaImpresion=='noNulo')
    window.open(source ,"ventana1","left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=no")
else jsp_alerta ('A','<%=descriptor.getDescripcion("ficheroNulo")%>');
}

function mostrarListaUOR(){
    var condiciones = new Array();
    condiciones[0]='UOR_NO_VIS'+'§¥';
    condiciones[1]='0';
    condiciones[2]='UOR_OCULTA'+'§¥';
    condiciones[3]='N';
    muestraListaTabla('UOR_COD_VIS','UOR_NOM','A_UOR',condiciones,'codUORs','descUOR', 'botonUOR','100');
}

function mostrarListaCargo(){
    var condiciones = new Array();
    muestraListaTabla('CAR_COD_VIS','CAR_NOM','A_CAR',condiciones,'cod_visible_cargo','descCargo', 'botonCargo','100');
}

function onClickDescUOR(){
        divSegundoPlano=false;
	    mostrarListaUOR();
}

function onClickDescCargo(){
        divSegundoPlano=false;
	    mostrarListaCargo();
}

function onClickHrefUOR() {
    var argumentos = new Array();
    argumentos[0] = document.forms[0].codUORs.value;
    var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalUOR" +
                    "&codOrganizacion=" + <%=orgCod%> + "&codEntidad=" + <%=entCod%>;

    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=600,height=450',function(datos){
                        if(datos != null) {
                            document.forms[0].codUOR.value = datos[2];
                            document.forms[0].descUORs.value = datos[1];
                            document.forms[0].codUORs.value = datos[0];
                        }
                        if((document.forms[0].codUORs.value != '') && (document.forms[0].descUORs.value == '')) {
                            document.forms[0].codUOR.value = '';
                            document.forms[0].codUORs.value = '';
                        }
                    });
}

function onClickHrefCargo() {
    var argumentos = new Array();
    argumentos[0] = document.forms[0].codCargo.value;
    var source = APP_CONTEXT_PATH + "/administracion/UsuariosGrupos.do?opcion=modalCargo" +
                    "&codOrganizacion=" + <%=orgCod%> + "&codEntidad=" + <%=entCod%>;

    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + source, argumentos,
	'width=610,height=450',function(datos){
                        if(datos != null) {
                            document.forms[0].codigoCargo.value = datos[2];
                            document.forms[0].descCargo.value = datos[1];
                            document.forms[0].codCargo.value = datos[0];
                        }
                        if((document.forms[0].codCargo.value != '') && (document.forms[0].descCargo.value == '')) {
                            document.forms[0].codigoCargo.value = '';
                            document.forms[0].codCargo.value = '';
                        }
                 });
}

var operadorConsulta = '|&:<>!=';
var operadorConsultaNulo=operadorConsulta+'#';

function contieneOperadoresConsulta(campo,cjtoOp){
    var contiene=false;
    if(campo != null) {
        var v = campo.value;
        for (i = 0; i < v.length; i++){
            var c = v.charAt(i);
            if (cjtoOp.indexOf(c) != -1)  contiene=true;
        }
    }
    return contiene;
}

function onchangeCodUOR() {
        var uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codUORs.value, 'A');
        if(uor != null) {
            document.forms[0].codUOR.value = uor.uor_cod;
            document.forms[0].descUORs.value = uor.uor_nom;
        }
        else { // ha dado null para alta, buscamos de baja
            uor = buscarUorPorCodVisibleEstado(uors, document.forms[0].codUORs.value, 'B');
            if(uor != null) {
                document.forms[0].codUOR.value = uor.uor_cod;
                document.forms[0].descUORs.value = uor.uor_nom;
            }
        }
        if(uor == null) {
            document.forms[0].codUOR.value = '';
            document.forms[0].descUORs.value = '';
            document.forms[0].codUORs.value = '';
        }
}

function onchangeCodCargo() {
        var cargo = buscarUorPorCodVisibleEstado(cargos, document.forms[0].codCargo.value, 'A');
        if(cargo != null) {
            document.forms[0].codigoCargo.value = cargo.uor_cod;
            document.forms[0].descCargo.value = cargo.uor_nom;
        }
        else { // ha dado null para alta, buscamos de baja
            cargo = buscarUorPorCodVisibleEstado(cargos, document.forms[0].codCargo.value, 'B');
            if(cargo != null) {
                document.forms[0].codigoCargo.value = cargo.uor_cod;
                document.forms[0].descCargo.value = cargo.uor_nom;
            }
        }
        if(cargo == null) {
            document.forms[0].codigoCargo.value = '';
            document.forms[0].descCargo.value = '';
            document.forms[0].codCargo.value = '';
        }
}

function controlarTodos() {    
    if (document.forms[0].todos.checked) {
        document.forms[0].codigoCargo.value="0";
        document.forms[0].codCargo.value="TD";
        document.forms[0].descCargo.value="TODOS";
        comboCargos.deactivate();
    } else {
        comboCargos.activate();
    }
}
</SCRIPT>
</head>
<BODY class="bandaBody" onload="javascript:{ pleaseWait('off');inicializar();}">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>

<html:form action="/formularios/FichaFormulario.do" target="_self" enctype="multipart/form-data">

<html:hidden  property="tipo_select" value=""/>
<html:hidden  property="col_cod" value=""/>
<html:hidden  property="col_desc" value=""/>
<html:hidden  property="nom_tabla" value=""/>
<html:hidden  property="input_cod" value=""/>
<html:hidden  property="input_desc" value=""/>
<html:hidden  property="column_valor_where" value=""/>

<input type="hidden" name="opcion" value=""/>
<input type="hidden" name="tipoRestriccionForm" value="0"/>
<input type="hidden" name="listaTramProc" value="" />
<input type="hidden" name="listaTramCod" value="" />
<input type="hidden" name="listaTramRel" value="" />
<input type="hidden" name="listaTramEst" value="" />
<input type="hidden" name="listaPrecargasXml" value="">
<input type="hidden" name="codigoInterno" value="<%=codigo%>">
<input type="hidden" name="listaRestriccionesUOR" value="" />
<input type="hidden" name="listaRestriccionesCargo" value="" />
<input type="hidden" name="formVersion" value="<%=version%>"/>
<input type="hidden" name="UorRegOrigen" value="" />
<input type="hidden" name="UorRegDestino" value="" />

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("etiq_altaFormulario")%></div>
<div class="contenidoPantalla">
<div class="tab-pane" id="tab-pane-1">
    <script type="text/javascript">
    tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
    </script>
<!-- CAPA 1: DATOS GENERALES ------------------------------ -->
<div class="tab-page" id="tabPage1" >
    <h2 class="tab"><%=descriptor.getDescripcion("res_pestana1")%></h2>
    <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
    <table class="contenidoPestanha" id ="tablaDatosGral" >
        <tr>
            <td class="etiqueta" style="width:12%">
                <%=descriptor.getDescripcion("gEtiq_Codigo")%>:
            </td>
            <td>
                <span class="columnP">
                    <% if (opcion.equals("alta")) {%>
                    <input name="formCod" type="text" class="inputTextoObligatorio" id="formCod" maxlength="8" style="width:5%" onkeyup="return xAMayusculas(this);" tabindex="1"/>
                    <%} else {%>
                    <input name="formCod" type="text" class="inputTextoObligatorio" id="formCod" maxlength="8" style="width:5%" onkeyup="return xAMayusculas(this);" tabindex="1" value="<%=codVisible%>" disabled/>
                    <%}%>
                </span>
                <span class="etiqueta" style="margin-left:3%"><%=descriptor.getDescripcion("gbVersion")%>:</span>
                <span class="columnP">
                    <input type="text"  class="inputTextoObligatorio" style="width:8%" maxlength="3" 
                           name="formularioVersion" tabindex="3" value="<%=version%>" disabled/>
                </span>
                <span  class="etiqueta" style="margin-left:3%"><%=descriptor.getDescripcion("gEtiq_Descrip")%>:</span>
                <span class="columnP">
                    <input type="text" class="inputTexto" style="width:55%" maxlength="200"  name="formNombre" 
                           onkeyup="return xAMayusculas(this);" tabindex="2" value="<%=nombreFormulario%>"/>
                </span>
            </td>
        </tr>
        <tr>
            <td class="etiqueta" style="width:12%">
                <%=descriptor.getDescripcion("gbTipo")%>:
            </td>
            <td>
                <span class="columnP">
                    <input class="inputTexto" type="text" id="codTipo" name="codTipo" style="width:4%" onkeyup="return SoloDigitosNumericos(this);">
                    <input class="inputTexto" type="text" id="descTipo" name="descTipo" style="width:26%" readonly>
                    <a id="anchorTipo" name="anchorTipo"><span class="fa fa-chevron-circle-down" aria-hidden="true" 
                      id="botonTipo" name="botonTipo" style="cursor:hand;"></span></a>
                </span>
                <span class="etiqueta" style="margin-left:2%"><%=descriptor.getDescripcion("gbFormRelacionado")%>:</span>
                <span class="etiqueta">
                    <input type=text class="inputTextoObligatorio" name="codFormRel" style="width:5%" maxlength="8">
                    <input type=text class="inputTextoObligatorio" name="descFormRel" style="width:35%" readonly>
                    <A style="text-decoration:none;" id="anchorFormRel" name="anchorFormRel">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonFormRel"
                                name="botonFormRel" style="cursor:hand;"></span>
                    </A>
                </span>
            </td>
        </tr>
        <tr>
            <td class="etiqueta" style="width:12%">
                <%=descriptor.getDescripcion("gbFecAlta")%>:
            </td>
            <td>
                <span class="columnP">
                    <INPUT TYPE="text" id="obligatorio" class="inputTxtFechaObligatorio"
                           size="10" maxlength="10" name="fechaAlta"
                           onkeyup="javascript:return SoloCaracteresFecha(this);"
                           onblur="javascript:return comprobarFecha(this);"
                           onfocus="this.select();" value="<%=fAlta%>">
                    <A onClick="mostrarCalFechaAlta(event);return false;" 
                       onblur="ocultarCalendarioOnBlur(event); return false;">
                            <span class="fa fa-calendar" aria-hidden="true" name="calFechaAlta" id="calFechaAlta" 
                                  title="<%=descriptor.getDescripcion("altFecha")%>"></span>
                    </A>
                </span>
                <span style="margin-left:3%" class="etiqueta"> <%=descriptor.getDescripcion("gbFecBaja")%>:</span>
                <span class="columnP">
                    <INPUT TYPE="text" id="obligatorio" class="inputTxtFecha"
                            size="10" maxlength="10" name="fechaBaja"
                            onkeyup="javascript:return SoloCaracteresFecha(this);"
                            onblur="javascript:return comprobarFecha(this);"
                            onfocus="this.select();" value="<%=fBaja%>">
                    <A onClick="mostrarCalFechaBaja(event);return false;" 
                            onblur="ocultarCalendarioOnBlur(event); return false;">
                        <span class="fa fa-calendar" aria-hidden="true" name="calFechaBaja" id="calFechaBaja"
                             title="<%=descriptor.getDescripcion("altFecha")%>"></span>
                    </A>
                </span>
                <span style="margin-left:3%" class="etiqueta">
                    <%=descriptor.getDescripcion("gbTipoFirma")%>:
                </span>
                <span>
                    <input type=text class="inputTextoObligatorio" name="codTipoFirma" style="width:3%" maxlength="1">
                    <input type=text class="inputTextoObligatorio" name="descTipoFirma" style="width:18%" readonly>
                    <A style="text-decoration:none;" id="anchorTipoFirma" name="anchorTipoFirma">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoFirma" name="botonTipoFirma" style="cursor:hand;"></span>
                    </A>
                </span>
                <span style="margin-left:3%" class="etiqueta">
                    <%=descriptor.getDescripcion("gEtiqVisible")%>:
                </span>
                <span class="columnP">
                    <input type="checkbox" class="checkbox" name="visible" value="1" tabindex="19" <%=checkedVisible%>/>
                </span>
            </td>
        </tr>
        <TR>
            <TD class="sub3titulo" colspan="2"><%=descriptor.getDescripcion("gEtiqConfiguracion")%></TD>
        </TR>
        <tr>
            <td class="etiqueta" style="width:12%">
                <%=descriptor.getDescripcion("gbTipoDemand")%>:
            </td>
            <td>
                <span class="columnP">
                    <input type=text class="inputTexto" name="codTipoDemandante" style="width:3%" maxlength="1">
                    <input type=text class="inputTexto" name="descTipoDemandante" style="width:20%" readonly>
                    <A style="text-decoration:none;" id="anchorTipoDemandante" name="anchorTipoDemandante">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDemandant" name="botonTipoDemandante" style="cursor:hand;"></span>
                    </A>
                </span>
                <span class="etiqueta" style="margin-left:2%"> <%=descriptor.getDescripcion("gbProcedimiento")%>:</span>
                <span class="columnP">
                    <input type=text class="inputTextoObligatorio" name="codProcedimiento" style="width:6%" maxlength="5">
                    <input type=text class="inputTextoObligatorio" name="descProcedimiento" style="width:38%" readonly>
                    <A style="text-decoration:none;" id="anchorProcedimiento" name="anchorProcedimiento">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProcedimiento" name="botonProcedimiento" style="cursor:hand;"></span>
                    </A>
                </span>
            </td>
        </tr>
        <tr>
            <td class="etiqueta" style="width:12%">
                <%=descriptor.getDescripcion("gbArea")%>:
            </td>
            <td>
                <span class="columnP">
                    <input type=text class="inputTextoObligatorio" name="codArea" style="width:10%" maxlength="5">
                    <input type=text class="inputTextoObligatorio" name="descArea" style="width:40%" readonly>
                    <A style="text-decoration:none;" id="anchorArea" name="anchorArea">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonArea" name="botonArea" style="cursor:pointer;"></span>
                    </A>
                </span>
                <span style="margin-left:3%" class="etiqueta">
                    <%=descriptor.getDescripcion("gbGenInstancia")%>:
                </span>
                <span class="columnP">
                    <input type="checkbox"  class="checkbox" name="gInstancia" value="1" tabindex="16" <%=checkedgInstancia%>
                           onClick="controlGInstancia()"/>
                </span>
            </td>
        </tr>
        <TR height="8">
                <td height="8" colspan="2"></td>
         </TR>
        <tr>
            <td class="etiqueta" style="width:12%">
                <%=descriptor.getDescripcion("gbGenRegistro")%>:
            </td>
            <td>
                <span class="columnP">
                    <input type="checkbox"  class="checkbox" name="gRegistro" value="1" tabindex="15" <%=checkedgRegistro%>
                            onClick="controlGeneraRegistro()"/>
                </span>
                <span style="margin-left:4%" class="etiqueta"> <%=descriptor.getDescripcion("msjUnidadRegOrigen")%>:</span>
                <span class="columnP">
                    <input type=text class="inputTextoObligatorio" name="codUorRegOrigen" style="width:3%" maxlength="6">
                    <input type=text class="inputTextoObligatorio" name="descUorRegOrigen" style="width:33%" readonly>
                    <A style="text-decoration:none;" id="anchorUorRegOrigen" name="anchorUorRegOrigen">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonUorRegOrigen" name="botonUorRegOrigen" style="cursor:hand;"></span>
                    </A>
                </span>
                <span style="margin-left:3%" class="etiqueta"> <%=descriptor.getDescripcion("msjUnidadRegDestino")%>:</span>
                <span class="columnP">
                    <input type=text class="inputTextoObligatorio" name="codUorRegDestino" style="width:3%" maxlength="6">
                    <input type=text class="inputTextoObligatorio" name="descUorRegDestino" style="width:33%" readonly>
                    <A style="text-decoration:none;" id="anchorUorRegDestino" name="anchorUorRegDestino">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonUorRegDestino" name="botonUorRegDestino" style="cursor:hand;"></span>
                    </A>
                </span>
            </td>
        </tr>
        <tr>
            <td class="etiqueta" style="width:12%">
                <%=descriptor.getDescripcion("gbGenTramite")%>:
            </td>
            <td>
                <span class="columnP">
                    <input type="checkbox" class="checkbox" name="gTramite" value="1" tabindex="17" <%=checkedgTramite%>/>
                </span>
                <span style="margin-left:5%" class="etiqueta">
                    <%=descriptor.getDescripcion("gbCerrarT1")%>:
                </span>
                <span class="columnP">
                    <input type="checkbox" class="checkbox" name="cerrarT1" value="1" tabindex="19" <%=checkedCerrarT1%>/>
                </span>
                <span style="margin-left:5%" class="etiqueta">
                    <%=descriptor.getDescripcion("gbDesdeTramite")%>:
                </span>
                <span class="columnP">
                    <input type="checkbox" class="checkbox" name="dTramite" value="1" tabindex="19" <%=checkeddTramite%> />
                </span>
                <span style="margin-left:5%" class="etiqueta">
                    <%=descriptor.getDescripcion("gbAccesible")%>:
                </span>
                <span class="columnP">
                    <input type="checkbox"  class="checkbox" name="accesible" value="1" tabindex="10"
                           onClick="controlAccesibilidad()" <%=checkedAccesible%>/>
                </span>
            </td>
        </tr>
        <TR>
            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("etiq_instrucciones")%></TD>
        </TR>
        <tr>
            <td colspan="2" style="width:100%" class="columnP">
                <textarea class="textareaTexto" cols="150" rows="5" name="instrucciones" style="width:100%"
                          onkeyup="return xAMayusculas(this);" value=""><%=instruc%></textarea>
            </td>
        </tr>
        <TR>
            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("gEtiqFicheros")%></TD>
        </TR>
        <tr>
            <td class="etiqueta" style="width:12%">
                <%=descriptor.getDescripcion("gEtiqPlantilla")%>:
            </td>
            <td>
                <%if (ficheroObligatorio) {%>
                <span align=left class="columnP">
                    <html:file property="fichero" styleClass="inputTextoObligatorio" style="width:80%" />
                </span>
                <%} else {%>
                <span align=center class="columnP">
                    <%=descriptor.getDescripcion("gEtiqModPlantilla")%>
                </span>

                <%}%>
                <span style="float:right">
                    <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gEtiqVisualizar")%>'
                           name="cmdVer" onclick = "onClickPDF(<%=codigo%>);">
                </span>
            </td>
        </tr>
        <tr>
            <td style="width:10%" class="etiqueta">
                <%=descriptor.getDescripcion("gEtiqPlantillaConsul")%>:
            </td>
            <td>
                <%if (ficheroObligatorio) {%>
                <span class="columnP">
                    <html:file property="fichero2" styleClass="inputTextoObligatorio" style="width:80%" />
                </span>
                <%}
                %>
                <span style="float:right">
                    <INPUT type= "button" class="botonGeneral" value='<%=descriptor.getDescripcion("gEtiqVisualizar")%>'
                           name="cmdVer" onclick = "onClickPDF2(<%=codigo%>);">
                </span>
            </td>
        </tr>
        <%if (!ficheroObligatorio) {%>
        <tr>
            <td colspan="2">
                <table style="visibility:hidden;width:100%">
                    <tr><td><html:file property="fichero2"/></td>
                        <td><html:file property="fichero"/></td>
                </tr></table>
            </td>
        </tr>
        <%}%>
    </table>
</div>

<!-- CAPA 2: trAMITES Y RESTRICCIONES
------------------------------ -->
<div class="tab-page" id="tabPage2">
    <h2 class="tab"><%=descriptor.getDescripcion("gEtiqTramRest")%></h2>
    <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
    <table id ="tablaTemas" style="width:100%">
        <TR>
            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("gEtiqTramites")%></TD>
        </TR>
        <tr>
            <td style="width:86%" id="tabla"></td>
            <td style="width:14%" align="center">
                <input type= "button" class="botonGeneral" style="margin-bottom:5%" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltatablaTramites" onclick="pulsarAltaTablaTramites();">
                <input type= "button" class="botonGeneral" style="margin-bottom:5%" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificartablaTramites" onclick="pulsarModificarTablaTramites();">
                <input type= "button" class="botonGeneral" style="margin-bottom:5%" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminartablaTramites" onclick="pulsarEliminarTablaTramites();">
                <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiartablaTramites" onclick="pulsarLimpiarTablaTramites();">
            </td>
        </tr>
        <tr style="padding-top:8px">
            <td colspan="2">
                <span class="etiqueta">
                    <%= descriptor.getDescripcion("gEtiqTramite")%>:
                </span>
                <span class="columnP">
                    <input type=text class="inputTextoObligatorio" name="codTramiteProcedimiento" style="width:3%" maxlength="5">
                    <input type=text class="inputTextoObligatorio" name="descTramiteProcedimiento" style="width:30%" readonly>
                    <A style="text-decoration:none;" id="anchorTramiteProcedimiento" name="anchorTramiteProcedimiento">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTramiteProcedimiento" name="botonTramiteProcedimiento" style="cursor:hand;"></span>
                    </A>
                </span>
                <span style="margin-left:2%" class="etiqueta">
                    <%= descriptor.getDescripcion("gEtiqRelacion")%>:
                </span>
                <span class="columnP">
                    <input type=text class="inputTextoObligatorio" name="codRelacionTramite" style="width:3%" maxlength="5">
                    <input type=text class="inputTextoObligatorio" name="descRelacionTramite" style="width:18%" readonly>
                    <A style="text-decoration:none;" id="anchorRelacionTramite" name="anchorRelacionTramite">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonRelacionTramite" name="botonRelacionTramite" style="cursor:hand;"></span>
                    </A>
                </span>
                <span style="margin-left:2%" class="etiqueta">
                    <%= descriptor.getDescripcion("gEtiqEstado")%>:
                </span>
                <span class="columnP">
                    <INPUT type="radio" name="estado" value="A" checked> <%= descriptor.getDescripcion("gEtiqAbierto")%>
                    <INPUT type="radio" name="estado" value="C"> <%= descriptor.getDescripcion("gEtiqCerrado")%>
                </span>
            </td>
        </tr>
        <tr style="padding-top:8px">
            <TD colspan="2" class="sub3titulo"><%=descriptor.getDescripcion("gEtiqRestricciones")%></TD>
        </TR>
        <tr>
            <td style="widht:84%" align="right" class="etiqueta">
                 <%= descriptor.getDescripcion("gEtiqTipoRestriccion")%>:
             </td>
             <td class="columnP" style="width: 16%">
                 <INPUT type="radio" name="tipoRestriccion" value="0" checked> <%= descriptor.getDescripcion("gEtiqRestringir")%>
                 <INPUT type="radio" name="tipoRestriccion" value="1"> <%= descriptor.getDescripcion("gEtiqPermitir")%>
             </td>
        </tr>
        <tr>
            <td style="width: 86%" id="tablaRestricciones"></td>
            <td style="width:14%" align="center">
                <input type= "button" class="botonGeneral" style="margin-bottom:5%" value="<%=descriptor.getDescripcion("gbAlta")%>" name="cmdAltatablaRestricciones" onclick="pulsarAltaTablaRestricciones();">
                <input type= "button" class="botonGeneral" style="margin-bottom:5%" value="<%=descriptor.getDescripcion("gbModificar")%>" name="cmdModificartablaRestricciones" onclick="pulsarModificarTablaRestricciones();">
                <input type= "button" class="botonGeneral" style="margin-bottom:5%" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminartablaRestricciones" onclick="pulsarEliminarTablaRestricciones();">
                <input type= "button" class="botonGeneral" style="margin-bottom:5%" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiartablaRestricciones" onclick="pulsarLimpiarTablaRestricciones();">
            </td>
        </tr>
        <tr style="padding-top:8px">
            <td colspan="2">
                <span class="etiqueta">
                    <%= descriptor.getDescripcion("etiq_UOR")%>:
                </span>
                <span >
                    <input type="hidden" name="codUnidadesOrganicas" id="codUOR"/>
                    <input type="text" class="inputTextoObligatorio" name="codUORs"
                            style="width:3%" onkeyup="return xAMayusculas(this);"/>
                    <input type="text" style="width:30%"
                           class="inputTextoObligatorioSinMayusculas" name="descUORs" readonly/>
                    <A href="javascript:{onClickHrefUOR()}" style="text-decoration:none;" id="anchorUORs" name="anchorUORs">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                             id="botonUORs" name="botonUORs"></span>
                    </A>
                </span>
                <span style="margin-left:3%" class="etiqueta">
                    <%= descriptor.getDescripcion("etiq_Cargo")%>:
                </span>
                <span>
                    <input type="hidden" name="codigoCargo" id="codigoCargo"/>
                    <input type=text class="inputTextoObligatorio" name="codCargo" style="width:3%"
                           onkeyup="return xAMayusculas(this);">
                    <input type=text class="inputTextoObligatorio" name="descCargo" style="width:25%" readonly>
                    <A href="javascript:{onClickHrefCargo();}" style="text-decoration:none;" id="anchorCargo" name="anchorCargo">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="desp" name="botonCargo" style="cursor:hand;"></span>
                    </A>
                </span>
                <span align="left" class="etiqueta" style="margin-left:3%">
                    <%= descriptor.getDescripcion("etiqTodos")%>:&nbsp;
                    <input type="checkBox" name="todos" id="todos" class="inputTexto" onCLick="controlarTodos();"/>
                </span>
            </td>
        </tr>
    </table>

</div>


<!-- CAPA 5: PRECARGAS DATOS XML
------------------------------ -->

<div style="visibility:<%=xml%>" class="tab-page" id="tabPage3" style="height:475px;widht:100%" >
    <h2 style="visibility:<%=xml%>" class="tab"><%=descriptor.getDescripcion("XML.precargas")%></h2>

    <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>


    <table id ="tablaTemas" style="width:100%">
        <tr>
            <td id="tablaPrecargas">
            </td>
    </tr></table>

    </div>
    <!---FIN PRECARGAS-->
    </div>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbGrabar")%>
               name="cmdAlta" onClick="pulsarAceptar();" accesskey="A">
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%>
               name="cmdModificar" onClick="pulsarCancelar();" accesskey="M">
    </div>
</div>
</html:form>

<script language="JavaScript1.2">

tp1_p1.setPrimerElementoFoco(document.forms[0].formNombre);
tp1_p2.setPrimerElementoFoco(document.forms[0].codTramiteProcedimiento);

var tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('130','center','<%= descriptor.getDescripcion("gbProcedimiento")%>');
tab.addColumna('500','left','<%= descriptor.getDescripcion("gEtiqTramite")%>');
tab.addColumna('85','center','<%= descriptor.getDescripcion("gEtiqRelacion")%>');
tab.addColumna('85','center','<%= descriptor.getDescripcion("gEtiqEstado")%>');
tab.displayCabecera=true;

var tabRestricciones = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaRestricciones'));

tabRestricciones.addColumna('130','center','');
tabRestricciones.addColumna('500','center','<%= descriptor.getDescripcion("etiq_UOR")%>');
tabRestricciones.addColumna('85','center','');
tabRestricciones.addColumna('85','left','<%= descriptor.getDescripcion("etiq_Cargo")%>');
tabRestricciones.displayCabecera=true;

function refresca() {
  tab.displayTabla();
}

var tabPrecargas = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaPrecargas'));
tabPrecargas.addColumna('50','center','<%= descriptor.getDescripcion("xml_activo")%>');
tabPrecargas.addColumna('900','center','<%= descriptor.getDescripcion("xml_nombre")%>');
tabPrecargas.displayCabecera=true;

var comboTipologia = new Combo("Tipo");
comboTipologia.change = function(){tipoChange();}

var comboFormularioRelacionado = new Combo("FormRel");
comboFormularioRelacionado.change = function(){formRelChange();}

var comboTiposFirma = new Combo("TipoFirma");

var comboTipoTercero = new Combo("TipoDemandante");

var comboProcedimiento = new Combo("Procedimiento");
comboProcedimiento.change = function(){procedimientoChange();}

var comboRegistroOrigen = new Combo("UorRegOrigen");

var comboRegistroDestino = new Combo ("UorRegDestino");

var comboAreasProcedimiento = new Combo("Area");
comboAreasProcedimiento.change = function(){areaChange();}

var comboTramitesDisponibles = new Combo("TramiteProcedimiento");

var comboRelacionTramite = new Combo ("RelacionTramite");
comboRelacionTramite.change = function(){relacionTramiteChange();}
var comboUnidades = new Combo ("UORs");
comboUnidades.anchor.onclick = function() {}

var comboCargos = new Combo ("Cargo");
comboCargos.anchor.onclick = function(){}

function refrescaPrecargas() {
  tabPrecargas.displayTabla();
}

function refrescaRestricciones() {
  tabRestricciones.displayTabla();
}


function comprobarCodigo2(lineas,codigo){
   for (i=0; i < lineas.length; i++){
   if(lineas[i][0]==(eval("document.forms[0]."+codigo+".value"))){
   jsp_alerta('A','<%=descriptor.getDescripcion("msjCodigoRep")%>');
   return false;
   }
   }
   return true;
}

var coordx=0;
var coordy=0;


<%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>
document.onmouseup = checkKeys;


function checkKeysLocal(evento, tecla){
   var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }
    if (teclaAuxiliar == 1){
            if (comboTipologia.base.style.visibility == "visible" && isClickOutCombo(comboTipologia,coordx,coordy)) setTimeout('comboTipologia.ocultar()',20);
            if (comboFormularioRelacionado.base.style.visibility == "visible" && isClickOutCombo(comboFormularioRelacionado,coordx,coordy)) setTimeout('comboFormularioRelacionado.ocultar()',20);
            if (comboTiposFirma.base.style.visibility == "visible" && isClickOutCombo(comboTiposFirma,coordx,coordy)) setTimeout('comboTiposFirma.ocultar()',20);
            if (comboTipoTercero.base.style.visibility == "visible" && isClickOutCombo(comboTipoTercero,coordx,coordy)) setTimeout('comboTipoTercero.ocultar()',20);
            if (comboProcedimiento.base.style.visibility == "visible" && isClickOutCombo(comboProcedimiento,coordx,coordy)) setTimeout('comboProcedimiento.ocultar()',20);
            if (comboRegistroOrigen.base.style.visibility == "visible" && isClickOutCombo(comboRegistroOrigen,coordx,coordy)) setTimeout('comboRegistroOrigen.ocultar()',20);
            if (comboRegistroDestino.base.style.visibility == "visible" && isClickOutCombo(comboRegistroDestino,coordx,coordy)) setTimeout('comboRegistroDestino.ocultar()',20);
            if (comboAreasProcedimiento.base.style.visibility == "visible" && isClickOutCombo(comboAreasProcedimiento,coordx,coordy)) setTimeout('comboAreasProcedimiento.ocultar()',20);
            if (comboTramitesDisponibles.base.style.visibility == "visible" && isClickOutCombo(comboTramitesDisponibles,coordx,coordy)) setTimeout('comboTramitesDisponibles.ocultar()',20);
            if (comboRelacionTramite.base.style.visibility == "visible" && isClickOutCombo(comboRelacionTramite,coordx,coordy)) setTimeout('comboRelacionTramite.ocultar()',20);
            if (comboUnidades.base.style.visibility == "visible" && isClickOutCombo(comboUnidades,coordx,coordy)) setTimeout('comboUnidades.ocultar()',20);
            if (comboCargos.base.style.visibility == "visible" && isClickOutCombo(comboCargos,coordx,coordy)) setTimeout('comboCargos.ocultar()',20);
            if(IsCalendarVisible) replegarCalendario(coordx,coordy);

        }

        if (teclaAuxiliar == 9){
            if (comboTipologia.base.style.visibility == "visible") comboTipologia.ocultar();
            if (comboFormularioRelacionado.base.style.visibility == "visible") comboFormularioRelacionado.ocultar();
            if (comboTiposFirma.base.style.visibility == "visible") comboTiposFirma.ocultar();
            if (comboTipoTercero.base.style.visibility == "visible") comboTipoTercero.ocultar();
            if (comboProcedimiento.base.style.visibility == "visible") comboProcedimiento.ocultar();
            if (comboRegistroOrigen.base.style.visibility == "visible") comboRegistroOrigen.ocultar();
            if (comboRegistroDestino.base.style.visibility == "visible") comboRegistroDestino.ocultar();
            if (comboAreasProcedimiento.base.style.visibility == "visible") comboAreasProcedimiento.ocultar();
            if (comboTramitesDisponibles.base.style.visibility == "visible") comboTramitesDisponibles.ocultar();
            if (comboRelacionTramite.base.style.visibility == "visible") comboRelacionTramite.ocultar();
            if (comboUnidades.base.style.visibility == "visible") comboUnidades.ocultar();
            if (comboCargos.base.style.visibility == "visible") comboCargos.ocultar();
            if(IsCalendarVisible) hideCalendar();
        }

    keyDel(evento);
}


var comboFormRel = [document.forms[0].codFormRel,document.forms[0].descFormRel,
                document.forms[0].botonFormRel];
var comboProcedimientos = [document.forms[0].codProcedimiento,document.forms[0].descProcedimiento,
                document.forms[0].botonProcedimiento];

var comboAreas = [document.forms[0].codArea,document.forms[0].descArea,
                document.forms[0].botonArea];

var comboUnidadesRegOrigen = [document.forms[0].codUorRegOrigen,document.forms[0].descUorRegOrigen,
                document.forms[0].botonUorRegOrigen];

var comboUnidadesRegDestino = [document.forms[0].codUorRegDestino,document.forms[0].descUorRegDestino,
                document.forms[0].botonUorRegDestino];

function recuperarTramites (codigos, descripciones){
    document.forms[0].codTramiteProcedimiento.value="";
    document.forms[0].descTramiteProcedimiento.value="";

    var codigoAreaDefecto;
    for (var i=0;i<cod_proc_defecto.length;i++) {
        if (cod_proc_defecto[i]==document.forms[0].codProcedimiento.value) {
            codigoAreaDefecto=cod_area_defecto[i];
            break;
        }
    }
    if (codigoAreaDefecto) {
        document.forms[0].codArea.value = codigoAreaDefecto;
        divSegundoPlano=true;
        comboAreasProcedimiento.buscaLinea(codigoAreaDefecto);
    }
    comboTramitesDisponibles.clearItems();
    comboTramitesDisponibles.addItems(codigos,descripciones);
}

function procedimientoChange() {
    tramites = new Array();
    tramitesOriginal = new Array();
    tab.clearTabla();
    tramitesOriginal = new Array();

    document.forms[0].opcion.value="cargarTramitesProcedimiento";
    document.forms[0].target="oculto";
    document.forms[0].action = "<c:url value='/formularios/FichaFormulario.do'/>";
    document.forms[0].submit();



}

function areaChange() {
    if (document.forms[0].codArea.value=="") {
        var codigoAreaDefecto;
        for (var i=0;i<cod_proc_defecto.length;i++) {
            if (cod_proc_defecto[i]==document.forms[0].codProcedimiento.value) {
                codigoAreaDefecto=cod_area_defecto[i];
                break;
            }
        }
        if (codigoAreaDefecto) {
            document.forms[0].codArea.value = codigoAreaDefecto;
            divSegundoPlano=true;
            comboAreasProcedimiento.buscaLinea(codigoAreaDefecto);
        }
    } else {
        divSegundoPlano=true;
    }
}


function controlGInstancia() {
    if (document.forms[0].gInstancia.checked==true) {
        document.forms[0].cerrarT1.disabled=false;
    } else {
        document.forms[0].cerrarT1.disabled=true;
        document.forms[0].cerrarT1.checked=false;
    }
}

function controlGeneraRegistro(){
    // comprobamos si estan o no habilitados los combos
    if (unidadesRegHabilitadas){
        habilitarCombosUnidadesRegistro(false);
    }else{
        habilitarCombosUnidadesRegistro(true);
   }
}

function controlAccesibilidad(){
if(document.forms[0].accesible.checked==true && tabRestricciones.lineas.length>0){
    jsp_alerta('A','<%=descriptor.getDescripcion("msjExisteRestriccion")%>');
	document.forms[0].accesible.checked=false;
}
}

function tipoChange() {
    //Capar la opcion de genera trámites sino es la opción deseada
    if (document.forms[0].codTipo.value=="0" || document.forms[0].codTipo.value=="1" || document.forms[0].codTipo.value=="2") {
        document.forms[0].gTramite.disabled=false;
    } else {
        document.forms[0].gTramite.disabled=true;document.forms[0].gTramite.checked=false;
    }
    if (document.forms[0].codTipo.value=="0") {
        document.forms[0].dTramite.disabled=true;
        document.forms[0].dTramite.checked=false;
    } else {
        document.forms[0].dTramite.disabled=false;
    }

    if (document.forms[0].codTipo.value=="0") {
        document.forms[0].gInstancia.disabled=false;
        document.forms[0].gRegistro.disabled=false;
        comboFormularioRelacionado.deactivate();
        document.forms[0].codFormRel.value="";
        document.forms[0].descFormRel.value="";
        comboProcedimiento.activate();
        document.forms[0].codProcedimiento.value="";
        document.forms[0].descProcedimiento.value="";
        cod_tramiteProcedimiento="";
        desc_tramiteProcedimiento="";
    } else {
        document.forms[0].gInstancia.checked=false;
        document.forms[0].gInstancia.disabled=true;
        document.forms[0].gRegistro.checked=false;
        document.forms[0].gRegistro.disabled=true;
        document.forms[0].codUorRegOrigen.value="";
        document.forms[0].descUorRegOrigen.value="";
        document.forms[0].codUorRegDestino.value="";
        document.forms[0].descUorRegDestino.value="";
        comboRegistroOrigen.deactivate();
        comboRegistroDestino.deactivate();
        comboFormularioRelacionado.activate();
        comboProcedimiento.deactivate();
        deshabilitarGeneral(comboProcedimientos);
    }

     if (document.forms[0].codTipo.value=="3" || document.forms[0].codTipo.value=="4") {
        document.forms[0].visible.disabled=true;
        document.forms[0].visible.checked=false;
     } else{
         document.forms[0].visible.disabled=false;
     }

    controlarCargaRelacionTramite();
    document.forms[0].codRelacionTramite.value="";
    document.forms[0].descRelacionTramite.value="";
    tab.clearTabla();
    tramitesOriginal = new Array();
    tramites = new Array();
}

function controlarCargaRelacionTramite() {
    //COMBO DE RELACIONES DE LOS TRAMITES
    if (document.forms[0].codTipo.value=="0") { //Solicitud
        cod_relacionTramite = cod_relacionTramite_A;
        desc_relacionTramite = desc_relacionTramite_A;
    } else if (document.forms[0].codTipo.value=="1") { //Anexo
        cod_relacionTramite = cod_relacionTramite_A;
        desc_relacionTramite = desc_relacionTramite_A;
    } else if (document.forms[0].codTipo.value=="2") { //Anexo con tramite
        cod_relacionTramite = cod_relacionTramite_AB;
        desc_relacionTramite = desc_relacionTramite_AB;
    } else if (document.forms[0].codTipo.value=="3") { //Anexo UOR
        cod_relacionTramite = cod_relacionTramite_C;
        desc_relacionTramite = desc_relacionTramite_C;
    } else if (document.forms[0].codTipo.value=="4") { //Anexo UOR no visible
        cod_relacionTramite = cod_relacionTramite_C;
        desc_relacionTramite = desc_relacionTramite_C;
    } else {
        cod_relacionTramite = cod_relacionTramite;
        desc_relacionTramite = desc_relacionTramite;
    }
    comboRelacionTramite.clearItems();
    comboRelacionTramite.addItems(cod_relacionTramite,desc_relacionTramite);
}

function formRelChange() {
    if (document.forms[0].codFormRel.value!=null && document.forms[0].codFormRel.value!=""){
        
    habilitarGeneral(comboProcedimientos);
    document.forms[0].codProcedimiento.value=procForms[document.forms[0].codFormRel.value];
    divSegundoPlano=true;
    procedimientoChange();
    comboProcedimiento.buscaLinea(document.forms[0].codProcedimiento.value);
    comboProcedimiento.deactivate();
    deshabilitarGeneral(comboProcedimientos);
    } else{
        comboFormularioRelacionado.deactivate();
    }
}

function relacionTramiteChange(){
    if ((document.forms[0].codRelacionTramite.value != "A") && (document.forms[0].codRelacionTramite.value != "B")) {
        document.forms[0].estado[0].disabled=true;document.forms[0].estado[0].checked=false;
        document.forms[0].estado[1].disabled=true;document.forms[0].estado[1].checked=false;
    }
    else {
        document.forms[0].estado[0].disabled=false;document.forms[0].estado[0].checked=true;
        document.forms[0].estado[1].disabled=false;
    }
}

function rellenarDatos(tableObject, rowID){
    listaAux = tramites;
    tabAux = tableObject;
    if(tableObject==tab){
        if((rowID>-1)&&(!(tableObject.ultimoTable))){
          document.forms[0].codTramiteProcedimiento.value=tramitesOriginal[rowID][1];
          comboTramitesDisponibles.buscaLinea(document.forms[0].codTramiteProcedimiento.value);
          divSegundoPlano=true;
          controlarCargaRelacionTramite();
          document.forms[0].codRelacionTramite.value=tramitesOriginal[rowID][2];
          comboRelacionTramite.buscaLinea(document.forms[0].codRelacionTramite.value);
          relacionTramiteChange();
          if (tramitesOriginal[rowID][3]=="A") {
              document.forms[0].estado[0].checked = true;
          } else if (tramitesOriginal[rowID][3]=="C") {
              document.forms[0].estado[1].checked = true;
          }
          ocultarDiv();
        } else borrarDatosTablaTramites();
    } else if (tableObject==tabRestricciones) {
        if((rowID>-1)&&(!(tableObject.ultimoTable))){
          document.forms[0].codUOR.value=restriccionesOriginal[rowID][0];
          document.forms[0].codUORs.value=restricciones[rowID][0];
          document.forms[0].descUORs.value=restricciones[rowID][1];
          if (restriccionesOriginal[rowID][1]!=0) {
          document.forms[0].codigoCargo.value=restriccionesOriginal[rowID][1];
          document.forms[0].codCargo.value=restricciones[rowID][2];
          document.forms[0].descCargo.value=restricciones[rowID][3];
              document.forms[0].todos.checked=false;
              comboCargos.activate();
          } else {
              document.forms[0].codigoCargo.value=0;
              document.forms[0].codCargo.value=restricciones[rowID][2];
              document.forms[0].descCargo.value=restricciones[rowID][3];
              document.forms[0].todos.checked=true;
              comboCargos.deactivate();
          }
        } else borrarDatosTablaRestricciones();
    }
}
</script>
</BODY>

</html:html>
