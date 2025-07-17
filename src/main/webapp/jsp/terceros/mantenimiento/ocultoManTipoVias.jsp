<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Vias</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script type="text/javascript">
// VARIABLES GLOBALES
var lista = new Array();
var datosTiposVias = new Array();
// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
function inicializar(){
    cargaTablaTiposVias();
}
function cargaTablaTiposVias(){
<%MantenimientosTercerosForm bForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
  Vector listaTiposVias = bForm.getListaTipoVias();
  int lengthTiposVias = listaTiposVias.size();
  int i = 0;%>
var j=0;
<%for(i=0;i<lengthTiposVias;i++){
    GeneralValueObject tiposVias = (GeneralValueObject)listaTiposVias.get(i);%>
    datosTiposVias[j] = ["<%=(String)tiposVias.getAtributo("codTipoVia")%>",
      "<%=(String)tiposVias.getAtributo("abreviatura")%>",
      "<%=(String)tiposVias.getAtributo("descTipoVia")%>"];
    lista[j] = datosTiposVias[j];
    j++;
<%}%>
  parent.mainFrame.recuperaDatos(lista);
}
</script></head><body onload="inicializar();"></body></html>
