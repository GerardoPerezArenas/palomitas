<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

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

<html:html>

 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <TITLE>::: TABLAS INTERCAMBIADORAS :::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>

    <SCRIPT type="text/javascript">
    var datosPrimeraTablaOrig = new Array();
    var datosSegundaTablaOrig = new Array();
    var datosPrimeraTabla = new Array();
    var datosPrimeraTablaOriginal = new Array();
    var datosSegundaTabla = new Array();
    var datosSegundaTablaOriginal = new Array();
    var cont = 0;
    var cont1 = 0;
    var cont2 = 0;
    var tableObject;

    function inicializar() {
      window.focus();
      tableObject = tabPrimera;
      document.forms[0].codMunicipio.value = '<bean:write name="TablasIntercambiadorasForm" property="codMunicipio"/>';
      document.forms[0].codProcedimiento.value = '<bean:write name="TablasIntercambiadorasForm" property="codProcedimiento"/>';
      document.forms[0].codTramite.value = '<bean:write name="TablasIntercambiadorasForm" property="codTramite"/>';
      document.forms[0].numeroCondicionSalida.value = '<bean:write name="TablasIntercambiadorasForm" property="numeroCondicionSalida"/>';
      <logic:iterate id="elemento" name="TablasIntercambiadorasForm" property="listaTramitesTodos">
        datosPrimeraTabla['<bean:write name="elemento" property="orden"/>'] = ['<bean:write name="elemento" property="codigo"/>',
                          '<bean:write name="elemento" property="descripcion"/>'];
      </logic:iterate>
      <logic:iterate id="elemento" name="TablasIntercambiadorasForm" property="listaTramitesTodos">
        datosPrimeraTablaOriginal['<bean:write name="elemento" property="orden"/>'] = ['<bean:write name="elemento" property="identificador"/>',
                          '<bean:write name="elemento" property="codigo"/>',
                          '<bean:write name="elemento" property="descripcion"/>'];
      </logic:iterate>
      tabPrimera.lineas=datosPrimeraTabla;
      refrescaPrimera();
      <logic:iterate id="elemento" name="TablasIntercambiadorasForm" property="listaTramitesSeleccion">
        datosSegundaTabla[cont1] = ['<bean:write name="elemento" property="codTramiteFlujoSalida"/>',
                          '<bean:write name="elemento" property="nombreTramiteFlujoSalida"/>'];
        cont1++;
      </logic:iterate>
      <logic:iterate id="elemento" name="TablasIntercambiadorasForm" property="listaTramitesSeleccion">
        datosSegundaTablaOriginal[cont2] = ['<bean:write name="elemento" property="idTramiteFlujoSalida"/>',
                          '<bean:write name="elemento" property="codTramiteFlujoSalida"/>',
                          '<bean:write name="elemento" property="nombreTramiteFlujoSalida"/>'];
        cont2++;
      </logic:iterate>
      tabSegunda.lineas=datosSegundaTabla;
      refrescaSegunda();
      var obligatorio = '<bean:write name="TablasIntercambiadorasForm" property="obligatorio"/>';
      if(obligatorio == 0) {
        document.forms[0].obligatorio[0].checked = true;
      } else if(obligatorio == 1) {
        document.forms[0].obligatorio[1].checked = true;
      } else {
        document.forms[0].obligatorio[2].checked = true;
      }
      
      for(t=0;t<datosPrimeraTabla.length;t++){
          if(datosPrimeraTabla[t]!=',')
              datosPrimeraTablaOrig[t] = datosPrimeraTabla[t]
      }
      
      for(t=0;t<datosSegundaTabla.length;t++){
          if(datosSegundaTabla[t]!=',')
              datosSegundaTablaOrig[t] = datosSegundaTabla[t]
      }
  }

    function pulsarCancelar() {
      self.parent.opener.retornoXanelaAuxiliar();
    }

    function pulsarAceptar() {
      var listasFlujoSalida = crearListasFlujoSalida();
      var listaCodigos = listasFlujoSalida[0];
      var listaNombreCodigos = listasFlujoSalida[3];
      var listaDescripciones = listasFlujoSalida[2];
      var listaCodigosTramites = listasFlujoSalida[1];
      var obl;
      if(document.forms[0].obligatorio[0].checked == true) {
        obl = "0";
      } else if(document.forms[0].obligatorio[1].checked == true) {
        obl = "1";
      } else if(document.forms[0].obligatorio[2].checked == true) {
        obl = "2";
      }
      if(datosSegundaTabla.length !=0) {
        var retorno = new Array();
        retorno = ["si",listaCodigos,listaCodigosTramites,obl,listaDescripciones,listaNombreCodigos];
        self.parent.opener.retornoXanelaAuxiliar(retorno);
      } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoDatos")%>');
      }
    }

    function pulsarDerecha() {
      if(tabPrimera.selectedIndex != -1) {
        var lineas = tabSegunda.lineas;
        var tamIndex = tabPrimera.selectedIndex;
        for (m=0; m < lineas.length; m++) {
        }
        datosSegundaTabla[m]= datosPrimeraTabla[tamIndex];
        datosSegundaTablaOriginal[m] = datosPrimeraTablaOriginal[tamIndex];

        var list = new Array();
        var listOriginal = new Array();
        tamLength=tabPrimera.lineas.length;
        for (i=tamIndex - 1; i < datosPrimeraTabla.length - 1; i++){
          if (i + 1 <= datosPrimeraTabla.length - 2){
            datosPrimeraTabla[i + 1]=datosPrimeraTabla[i + 2];
            datosPrimeraTablaOriginal[i + 1]=datosPrimeraTablaOriginal[i + 2];
          }
        }
        for(j=0; j < datosPrimeraTabla.length-1 ; j++){
          list[j] = datosPrimeraTabla[j];
          listOriginal[j] = datosPrimeraTablaOriginal[j];
        }
        tabPrimera.lineas=list;
        datosPrimeraTabla = list;
        datosPrimeraTablaOriginal = listOriginal;
        refrescaPrimera();
        refrescaSegunda();
      } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
      }
    }

    function pulsarDerechaTodo() {
      var lineas = tabSegunda.lineas;
      var lineasPrimera = tabPrimera.lineas;
      for (m=0; m < lineas.length; m++) {
      }
      for (j=0; j < lineasPrimera.length; j++) {
        datosSegundaTabla[m+j]= datosPrimeraTabla[j];
        datosSegundaTablaOriginal[m+j]= datosPrimeraTablaOriginal[j];
      }
      datosPrimeraTabla = new Array();
      datosPrimeraTablaOriginal = new Array();
      tabPrimera.lineas = datosPrimeraTabla;
      refrescaPrimera();
      refrescaSegunda();
    }

    function pulsarIzquierda() {
      if(tabSegunda.selectedIndex != -1) {
        var lineas = tabPrimera.lineas;
        var tamIndex = tabSegunda.selectedIndex;
        for (m=0; m < lineas.length; m++) {
        }
        datosPrimeraTabla[m]= datosSegundaTabla[tamIndex];
        datosPrimeraTablaOriginal[m]= datosSegundaTablaOriginal[tamIndex];

        var list = new Array();
        var listOriginal = new Array();
        tamLength=tabSegunda.lineas.length;
        for (i=tamIndex - 1; i < datosSegundaTabla.length - 1; i++){
          if (i + 1 <= datosSegundaTabla.length - 2){
            datosSegundaTabla[i + 1]=datosSegundaTabla[i + 2];
            datosSegundaTablaOriginal[i + 1]=datosSegundaTablaOriginal[i + 2];
          }
        }
        for(j=0; j < datosSegundaTabla.length-1 ; j++){
          list[j] = datosSegundaTabla[j];
          listOriginal[j] = datosSegundaTablaOriginal[j];
        }
        tabSegunda.lineas=list;
        datosSegundaTabla = list;
        datosSegundaTablaOriginal = listOriginal;
        refrescaPrimera();
        refrescaSegunda();
      } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
      }
    }

    function pulsarIzquierdaTodo() {
      var lineas = tabPrimera.lineas;
      var lineasSegunda = tabSegunda.lineas;
      for (m=0; m < lineas.length; m++) {
      }
      for (j=0; j < lineasSegunda.length; j++) {
        datosPrimeraTabla[m+j]= datosSegundaTabla[j];
        datosPrimeraTablaOriginal[m+j]= datosSegundaTablaOriginal[j];
      }
      datosSegundaTabla = new Array();
      datosSegundaTablaOriginal = new Array();
      tabSegunda.lineas = datosSegundaTabla;
      refrescaPrimera();
      refrescaSegunda();
    }

    function crearListasFlujoSalida() {
      var listaCodTramitesFlujoSalida = "";
      var listaNumerosSecuenciaFlujoSalida = "";
      var listaDescTramitesFlujoSalida = "";
      var listaNombresTramitesFlujoSalida = "";
      for (i=0; i < datosSegundaTabla.length; i++) {
        listaCodTramitesFlujoSalida += datosSegundaTablaOriginal[i][0]+'зе';
        listaNombresTramitesFlujoSalida += datosSegundaTablaOriginal[i][1]+'зе';
        listaDescTramitesFlujoSalida += datosSegundaTablaOriginal[i][2]+'зе';
        listaNumerosSecuenciaFlujoSalida += (i+1)+'зе';
      }
      var listasFlujoSalida = new Array();
      listasFlujoSalida = [listaCodTramitesFlujoSalida,listaNumerosSecuenciaFlujoSalida,listaDescTramitesFlujoSalida,listaNombresTramitesFlujoSalida];
      return listasFlujoSalida;
    }

    function insercionRealizada() {
      var retorno = new Array();
      retorno = ["si"];
      self.parent.opener.retornoXanelaAuxiliar(retorno);
    }

    function insercionNoRealizada() {
      var retorno = new Array();
      retorno = ["no"];
      self.parent.opener.retornoXanelaAuxiliar(retorno);
    }
    
    function pulsarLimpiarTodo(){
        tabPrimera.lineas = datosPrimeraTablaOrig;
        tabSegunda.lineas = datosSegundaTablaOrig;
        refrescaPrimera();
        refrescaSegunda();
    }

    </SCRIPT>
 </head>

 <BODY class="bandaBody" onload="javascript:{inicializar();}">
    <html:form action="/sge/TablasIntercambiadoras.do" target="_self">

    <html:hidden  property="opcion" value=""/>
    <html:hidden  property="codMunicipio" />
    <html:hidden  property="codProcedimiento" />
    <html:hidden  property="codTramite" />
    <html:hidden  property="numeroCondicionSalida" />

    <input type="hidden" name="listaCodTramitesFlujoSalida" value="">
    <input type="hidden" name="listaNumerosSecuenciaFlujoSalida" value="">

    <div class="txttitblanco"><%=descriptor.getDescripcion("tit_consTramExt")%></div>
    <div class="contenidoPantalla">
        <!--Datos-->
        <table style="width:100%">
            <TR>
                <td style="width: 47%;vertical-align:top" id="tablaPrimera"></td>
                <td style="width: 6%;vertical-align:top;padding:5px">
                    <input type= "button" class="botonGeneral" value=">" name="cmdDerecha" onclick="pulsarDerecha();" style="width:100%;margin-bottom:5px">
                    <input type= "button" class="botonGeneral" value=">>" name="cmdDerechaTodo" onclick="pulsarDerechaTodo();" style="width:100%;margin-bottom:5px">
                    <input type= "button" class="botonGeneral" value="<" name="cmdIzquierda" onclick="pulsarIzquierda();" style="width:100%;margin-bottom:5px">
                    <input type= "button" class="botonGeneral" value="<<" name="cmdIzquierdaTodo" onclick="pulsarIzquierdaTodo();" style="width:100%;margin-bottom:5px">
                </td>
                <td style="width: 47%;vertical-align:top" id="tablaSegunda"></td>
            </tr>
            <tr>
                <td colspan="3">
                      <span style="margin-left:85%" class="columnP">
                        <input type="radio" name="obligatorio" value="0" CHECKED></input>
                      </span>
                      <span class="etiqueta" >
                        <%=descriptor.getDescripcion("opcionales")%>
                      <span>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                      <span style="margin-left:85%" class="columnP">
                        <input type="radio" name="obligatorio" value="1"></input>
                      </span>
                      <span class="etiqueta" >
                        <%=descriptor.getDescripcion("tramOblig")%>
                      <span>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                      <span style="margin-left:85%" class="columnP">
                        <input type="radio" name="obligatorio" value="2"></input>
                      </span>
                      <span class="etiqueta" >
                        <%=descriptor.getDescripcion("excluyente")%>
                      <span>
            </td>
          </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" accesskey="A" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();">
        <input type= "button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar"  onclick="pulsarCancelar();">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiar" onclick="pulsarLimpiarTodo();">
    </div>    
</div>    
</html:form>

<script type="text/javascript">
    // TABLA PRIMERA
    var tabPrimera = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaPrimera'));
    
    tabPrimera.addColumna('65','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
    tabPrimera.addColumna('360','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>'); 

    tabPrimera.displayCabecera=true;
    tabPrimera.displayTabla();

    function refrescaPrimera() {
      tabPrimera.displayTabla();
    }

    tabPrimera.displayDatos = pintaDatosPrimera;

    function pintaDatosPrimera() {
      tableObject = tabPrimera;
    }

    // TABLA SEGUNDA
    var tabSegunda = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaSegunda'));
    
    tabSegunda.addColumna('65','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
    tabSegunda.addColumna('360','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
    tabSegunda.displayCabecera=true;
    tabSegunda.displayTabla();

    function refrescaSegunda() {
      tabSegunda.displayTabla();
    }

    tabSegunda.displayDatos = pintaDatosSegunda;

    function pintaDatosSegunda() {
      tableObject = tabSegunda;
    }

    function checkKeysLocal(event,tecla) {
    }
    </script>
    <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
 </BODY>
</html:html>
