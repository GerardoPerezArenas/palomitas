<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.DefinicionTramitesForm"%>
<%@page import="java.util.Vector" %>

<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl = 2;
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }

  DefinicionTramitesForm dtForm =(DefinicionTramitesForm)session.getAttribute("DefinicionTramitesForm");
  //String opcion = dtForm.getOpcion();
  String tit = "titSelPlantTram";
  String preg= "msjSelPlantTram";
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Lista de plantillas</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <script type="text/vbscript" src="<c:url value='/scripts/documentos.vbs'/>"></script>

    <script type="text/javascript">
    var argVentana ;
    var plantillas = new Array();


    function cargarListas(){
      <logic:iterate id="elemento" name="DefinicionTramitesForm" property="listaPlantillas">
      plantillas['<bean:write name="elemento" property="orden"/>']= new Array();
      plantillas['<bean:write name="elemento" property="orden"/>'][0]='<bean:write name="elemento" property="codigo"/>';
      plantillas['<bean:write name="elemento" property="orden"/>'][1]='<bean:write name="elemento" property="descripcion"/>';
      </logic:iterate>
      cargaTablaPlantillas();
    }

    var lista = new Array();
    var listaOriginal = new Array();

    // FUNCIONES DE CARGA DE DATOS
    function cargaTablaPlantillas(){
      lista = new Array();
      for (var i=0; i<plantillas.length; i++){
        lista[i]=new Array();
          lista[i][0]=plantillas[i][1];
        listaOriginal[i]=new Array();
          listaOriginal[i][0]=plantillas[i][0];
        listaOriginal[i][1]=plantillas[i][1];
      }
      tablaPlantillas.lineas=lista;
      refresca(tablaPlantillas);
    }

    function desActivarBotones(desactivar) {
      with (document.forms[0]) {
          botonSeleccionar.disabled=desactivar;
          botonSalir.disabled=desactivar;
          botonVer.disabled=desactivar;
      }
    }

    function pulsarCerrar(datos){
      self.parent.opener.retornoXanelaAuxiliar(datos);
    }

    function pulsarSalir(){
      pulsarCerrar();
    }

    function pulsarVer() {
      if  (tablaPlantillas.selectedIndex!=-1){
          var cod = listaOriginal[tablaPlantillas.selectedIndex][0];
          var nombre = listaOriginal[tablaPlantillas.selectedIndex][1];
          ejecutaPlantilla(cod,nombre);
      } else {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjSelPlantTram")%>');
      }
    }

    function ejecutaPlantilla(codigoplantilla,textoplantilla) {
      document.forms[0].codDocumento.value = codigoplantilla;
      document.forms[0].nombreDocumento.value = textoplantilla;
      document.forms[0].opcion.value = 'verDocumento';
      document.forms[0].target = "oculto";
      document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
      document.forms[0].submit();
    }

    function pulsarSeleccionar(){
      var plantillaSelecc;
      datosAdevolver = new Array();
      if (tablaPlantillas.selectedIndex!=-1)
        datosAdevolver=[plantillas[tablaPlantillas.selectedIndex][1],plantillas[tablaPlantillas.selectedIndex][0]];
      else
        datosAdevolver=undefined;
      pulsarCerrar(datosAdevolver);
    }

    function inicializar() {
      argVentana = self.parent.opener.xanelaAuxiliarArgs;
      cargarListas();
    }
    </script>
</head>

<body class="bandaBody" onload="javascript:{inicializar();}" >
    <html:form method="post" action="/sge/DefinicionTramites.do" enctype="multipart/form-data" target="_self">

    <html:hidden property="opcion" value=""/>
    <html:hidden property="codAplicacion" />
    <html:hidden property="codClasifTramite" />
    <input type="hidden" name="plt_cod" value="">
    <input type="hidden" name="plt_apl" value="">
    <input type="hidden" name="codDocumento" value="">
    <input type="hidden" name="nombreDocumento" value="">
    
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion(tit)%></div>
    <div class="contenidoPantalla"><%=descriptor.getDescripcion(tit)%>
        <div id="divTablaPlantillas" width="100%">
        </div>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbVer")%>' name="botonVer" onClick="pulsarVer();">
            <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("gbSalir")%>' name="botonSalir" onClick="pulsarSalir();">
        </div>
    </div>
</html:form>

    <script type="text/javascript">
    var tablaPlantillas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('divTablaPlantillas'));
    tablaPlantillas.addColumna('400', 'left','Documentos');
    tablaPlantillas.displayCabecera=true;

    function refresca(tabla){ tabla.displayTabla(); }
    function callFromTableTo(rowID,tableName){
        if(tablaPlantillas.id == tableName){
          pulsarSeleccionar();
        }
    }
    </script>
</body>
</html:html>
