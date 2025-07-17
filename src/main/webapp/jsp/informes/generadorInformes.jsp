<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.informes.GeneradorInformesForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

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
  }
  GeneradorInformesForm bForm =(GeneradorInformesForm)session.getAttribute("GeneradorInformesForm");
  String operacion = bForm.getOperacion();
  if(operacion == null) {
    operacion = "";
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>
<head>
    <TITLE>::: GENERADOR DE INFORMES:::</TITLE>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>" >
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
    <SCRIPT language="JavaScript">
    var cod_entidad = new Array();
    var desc_entidad = new Array();
    var cod_conector=['AND','OR'];
    var desc_conector=['E','OU'];
    var cod_sentido=['ASC','DESC'];
    var desc_sentido=['Asc.','Desc.'];
    var cod_condicion=['=','LIKE','<','>','<>','IS NOT NULL','IS NULL'];
    var desc_condicion=['Igual','Como','Menor','Maior','Distinto','Nulo','No Nulo'];
    var cod_camposOrdenacion= new Array();
    var desc_camposOrdenacion= new Array();
    var cod_camposFiltro= new Array();
    var desc_camposFiltro= new Array();

    var listaCamposDisponibles = new Array();
    var listaCamposDisponiblesOriginal = new Array();
    var listaCamposElegidos = new Array();
    var listaCamposElegidosOriginal = new Array();
    var listaCamposDispEntera = new Array();
    var listaCamposDispEnteraOriginal = new Array();

    function inicializar() {
      window.focus();
      tp1.setSelectedIndex(0);

      <logic:iterate id="elemento" name="GeneradorInformesForm" property="listaEntidades">
        cod_entidad['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_entidad['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
      </logic:iterate>
      document.forms[0].codAplicacion.value = '<bean:write name="GeneradorInformesForm" property="codAplicacion"/>';
      comboEntidad.addItems(cod_entidad,desc_entidad);

      comboOperador1.addItems(cod_conector,desc_conector);
      comboOperador2.addItems(cod_conector,desc_conector);
      comboOperador3.addItems(cod_conector,desc_conector);
      comboOperador4.addItems(cod_conector,desc_conector);
      comboOperador5.addItems(cod_conector,desc_conector);
      comboOperador6.addItems(cod_conector,desc_conector);

      comboSentidoOrdenacion1.addItems(cod_sentido,desc_sentido);
      comboSentidoOrdenacion2.addItems(cod_sentido,desc_sentido);
      comboSentidoOrdenacion3.addItems(cod_sentido,desc_sentido);
      comboSentidoOrdenacion4.addItems(cod_sentido,desc_sentido);
      comboSentidoOrdenacion5.addItems(cod_sentido,desc_sentido);

      comboCondicion1.addItems(cod_condicion,desc_condicion);
      comboCondicion2.addItems(cod_condicion,desc_condicion);
      comboCondicion3.addItems(cod_condicion,desc_condicion);
      comboCondicion4.addItems(cod_condicion,desc_condicion);
      comboCondicion5.addItems(cod_condicion,desc_condicion);
      comboCondicion6.addItems(cod_condicion,desc_condicion);
      comboCondicion7.addItems(cod_condicion,desc_condicion);

      desactivarCombosOrdenacionFiltro();

      <% if("modificar".equals(operacion)) { %>
          <% GeneralValueObject gVO = new GeneralValueObject();
          gVO = bForm.getGVO(); %>
          document.forms[0].nombreInforme.value = '<%= (String) gVO.getAtributo("nombreInforme") %>';
          document.forms[0].descripcion.value = '<%= (String) gVO.getAtributo("descInforme") %>';
          var cEntidad = '<%= (String) gVO.getAtributo("codEntidad") %>';
          comboEntidad.buscaCodigo(cEntidad);
          <% Vector listaCamposElegidos = new Vector();
          listaCamposElegidos = (Vector) gVO.getAtributo("listaCamposElegidos");
          if(listaCamposElegidos != null ) {  %>
              var j=0;
              <%for(int i=0;i<listaCamposElegidos.size();i++){
                GeneralValueObject campos = (GeneralValueObject)listaCamposElegidos.get(i);%>
                listaCamposElegidos[j] = ['<%=(String)campos.getAtributo("nombreCampo")%>'];
                listaCamposElegidosOriginal[j] = ['<%=(String)campos.getAtributo("codCampo")%>',
                                         '<%=(String)campos.getAtributo("nombreCampo")%>',
                                         '<%=(String)campos.getAtributo("tipo")%>',
                                         '<%=(String)campos.getAtributo("longitud")%>',
                                         '<%=(String)campos.getAtributo("campo")%>',
                                        '<%=(String)campos.getAtributo("nombreAs")%>'];
                j++;
              <%}%>
          <% } %>
          tabElegidos.lineas=listaCamposElegidos;
          refrescaElegidos();
          <% Vector listaCamposDisponibles = new Vector();
          listaCamposDisponibles = (Vector) gVO.getAtributo("listaCamposDisponibles");
          if(listaCamposDisponibles != null ) {  %>
              j=0;
              <%for(int i=0;i<listaCamposDisponibles.size();i++){
                GeneralValueObject campos = (GeneralValueObject)listaCamposDisponibles.get(i);%>
                listaCamposDisponibles[j] = ['<%=(String)campos.getAtributo("nombreCampo")%>'];
                listaCamposDisponiblesOriginal[j] = ['<%=(String)campos.getAtributo("codCampo")%>',
                                         '<%=(String)campos.getAtributo("nombreCampo")%>',
                                         '<%=(String)campos.getAtributo("tipo")%>',
                                         '<%=(String)campos.getAtributo("longitud")%>',
                                         '<%=(String)campos.getAtributo("campo")%>',
                                        '<%=(String)campos.getAtributo("nombreAs")%>'];
                j++;
              <%}%>
          <% } %>
          tabDisponibles.lineas=listaCamposDisponibles;
          refrescaDisponibles();
          <% Vector listaCamposDisponiblesEntera = new Vector();
          listaCamposDisponiblesEntera = (Vector) gVO.getAtributo("listaCamposDisponiblesEntera");
          if(listaCamposDisponiblesEntera != null ) {  %>
              j=0;
              <%for(int i=0;i<listaCamposDisponiblesEntera.size();i++){
                GeneralValueObject campos = (GeneralValueObject)listaCamposDisponiblesEntera.get(i);%>
                listaCamposDispEntera[j] = ['<%=(String)campos.getAtributo("nombreCampo")%>'];
                listaCamposDispEnteraOriginal[j] = ['<%=(String)campos.getAtributo("codCampo")%>',
                                         '<%=(String)campos.getAtributo("nombreCampo")%>',
                                         '<%=(String)campos.getAtributo("tipo")%>',
                                         '<%=(String)campos.getAtributo("longitud")%>',
                                         '<%=(String)campos.getAtributo("campo")%>',
                                        '<%=(String)campos.getAtributo("nombreAs")%>'];
                j++;
              <%}%>
          <% } %>
          for(var m=0; m < listaCamposDispEntera.length;m++) {
            cod_camposFiltro[m] = listaCamposDispEnteraOriginal[m][0];
            desc_camposFiltro[m] = listaCamposDispEntera[m][0];
          }
          comboCampo1.addItems(cod_camposFiltro,desc_camposFiltro);
          comboCampo2.addItems(cod_camposFiltro,desc_camposFiltro);
          comboCampo3.addItems(cod_camposFiltro,desc_camposFiltro);
          comboCampo4.addItems(cod_camposFiltro,desc_camposFiltro);
          comboCampo5.addItems(cod_camposFiltro,desc_camposFiltro);
          comboCampo6.addItems(cod_camposFiltro,desc_camposFiltro);
          comboCampo7.addItems(cod_camposFiltro,desc_camposFiltro);
          comboCampo1.activate();

          cargarCombosOrdenacion();

          var ordenacion1 = '<%= (String) gVO.getAtributo("codCampoOrdenacion1") %>';
          if (ordenacion1 != 'null')
          {
            comboCampoOrdenacion1.buscaCodigo(ordenacion1);
          }
          var ordenacion2 = '<%= (String) gVO.getAtributo("codCampoOrdenacion2") %>';
          if (ordenacion2 != 'null')
          {
            comboCampoOrdenacion2.buscaCodigo(ordenacion2);
          }
          var ordenacion3 = '<%= (String) gVO.getAtributo("codCampoOrdenacion3") %>';
          if (ordenacion3 != 'null')
          {
            comboCampoOrdenacion3.buscaCodigo(ordenacion3);
          }
          var ordenacion4 = '<%= (String) gVO.getAtributo("codCampoOrdenacion4") %>';
          if (ordenacion4 != 'null')
          {
            comboCampoOrdenacion4.buscaCodigo(ordenacion4);
          }
          var ordenacion5 = '<%= (String) gVO.getAtributo("codCampoOrdenacion5") %>';
          if (ordenacion5 != 'null')
          {
            comboCampoOrdenacion5.buscaCodigo(ordenacion5);
          }

          var Sordenacion1 = '<%= (String) gVO.getAtributo("codSentidoOrdenacion1") %>';
          if (Sordenacion1 != 'null')
          {
            comboSentidoOrdenacion1.buscaCodigo(Sordenacion1);
          }
          var Sordenacion2 = '<%= (String) gVO.getAtributo("codSentidoOrdenacion2") %>';
          if (Sordenacion2 != 'null')
          {
            comboSentidoOrdenacion2.buscaCodigo(Sordenacion2);
          }
          var Sordenacion3 = '<%= (String) gVO.getAtributo("codSentidoOrdenacion3") %>';
          if (Sordenacion3 != 'null')
          {
            comboSentidoOrdenacion3.buscaCodigo(Sordenacion3);
          }
          var Sordenacion4 = '<%= (String) gVO.getAtributo("codSentidoOrdenacion4") %>';
          if (Sordenacion4 != 'null')
          {
            comboSentidoOrdenacion4.buscaCodigo(Sordenacion4);
          }
          var Sordenacion5 = '<%= (String) gVO.getAtributo("codSentidoOrdenacion5") %>';
          if (Sordenacion5 != 'null')
          {
            comboSentidoOrdenacion5.buscaCodigo(Sordenacion5);
          }

          <!-- Combos operadores -->
          var operador1 = '<%= (String) gVO.getAtributo("codOperador1") %>';
          if (operador1 != 'null')
          {
            comboOperador1.buscaCodigo(operador1);
          }
          var operador2 = '<%= (String) gVO.getAtributo("codOperador2") %>';
          if (operador2 != 'null')
          {
            comboOperador2.buscaCodigo(operador2);
          }
          var operador3 = '<%= (String) gVO.getAtributo("codOperador3") %>';
          if (operador3 != 'null')
          {
            comboOperador3.buscaCodigo(operador3);
          }
          var operador4 = '<%= (String) gVO.getAtributo("codOperador4") %>';
          if (operador4 != 'null')
          {
            comboOperador4.buscaCodigo(operador4);
          }
          var operador5 = '<%= (String) gVO.getAtributo("codOperador5") %>';
          if (operador5 != 'null')
          {
            comboOperador5.buscaCodigo(operador5);
          }
          var operador6 = '<%= (String) gVO.getAtributo("codOperador6") %>';
          if (operador6 != 'null')
          {
            comboOperador6.buscaCodigo(operador6);
          }


          var campo1 = '<%= (String) gVO.getAtributo("codCampo1") %>';
          if (campo1 != 'null')
          {
            comboCampo1.buscaCodigo(campo1);
          }
          var campo2 = '<%= (String) gVO.getAtributo("codCampo2") %>';
          if (campo2 != 'null')
          {
            comboCampo2.buscaCodigo(campo2);
          }
          var campo3 = '<%= (String) gVO.getAtributo("codCampo3") %>';
          if (campo3 != 'null')
          {
            comboCampo3.buscaCodigo(campo3);
          }
          var campo4 = '<%= (String) gVO.getAtributo("codCampo4") %>';
          if (campo4 != 'null')
          {
            comboCampo4.buscaCodigo(campo4);
          }
          var campo5 = '<%= (String) gVO.getAtributo("codCampo5") %>';
          if (campo5 != 'null')
          {
            comboCampo5.buscaCodigo(campo5);
          }
          var campo6 = '<%= (String) gVO.getAtributo("codCampo6") %>';
          if (campo6 != 'null')
          {
            comboCampo6.buscaCodigo(campo6);
          }
          var campo7 = '<%= (String) gVO.getAtributo("codCampo7") %>';
          if (campo7 != 'null')
          {
            comboCampo7.buscaCodigo(campo7);
          }


          var condicion1 = '<%= (String) gVO.getAtributo("codCondicion1") %>';
          if (condicion1 != 'null')
          {
            comboCondicion1.buscaCodigo(condicion1);
          }
          var condicion2 = '<%= (String) gVO.getAtributo("codCondicion2") %>';
          if (condicion2 != 'null')
          {
            comboCondicion2.buscaCodigo(condicion2);
          }
          var condicion3 = '<%= (String) gVO.getAtributo("codCondicion3") %>';
          if (condicion3 != 'null')
          {
            comboCondicion3.buscaCodigo(condicion3);
          }
          var condicion4 = '<%= (String) gVO.getAtributo("codCondicion4") %>';
          if (condicion4 != 'null')
          {
            comboCondicion4.buscaCodigo(condicion4);
          }
          var condicion5 = '<%= (String) gVO.getAtributo("codCondicion5") %>';
          if (condicion5 != 'null')
          {
            comboCondicion5.buscaCodigo(condicion5);
          }
          var condicion6 = '<%= (String) gVO.getAtributo("codCondicion6") %>';
          if (condicion6 != 'null')
          {
            comboCondicion6.buscaCodigo(condicion6);
          }
          var condicion7 = '<%= (String) gVO.getAtributo("codCondicion7") %>';
          if (condicion7 != 'null')
          {
            comboCondicion7.buscaCodigo(condicion7);
          }

          if ('<%= (String) gVO.getAtributo("valor1") %>' != 'null')
          {
            document.forms[0].valor1.value = '<%= (String) gVO.getAtributo("valor1") %>';
          }
          if ('<%= (String) gVO.getAtributo("valor2") %>' != 'null')
          {
            document.forms[0].valor2.value = '<%= (String) gVO.getAtributo("valor2") %>';
          }
          if ('<%= (String) gVO.getAtributo("valor3") %>' != 'null')
          {
            document.forms[0].valor3.value = '<%= (String) gVO.getAtributo("valor3") %>';
          }
          if ('<%= (String) gVO.getAtributo("valor4") %>' != 'null')
          {
            document.forms[0].valor4.value = '<%= (String) gVO.getAtributo("valor4") %>';
          }
          if ('<%= (String) gVO.getAtributo("valor5") %>' != 'null')
          {
            document.forms[0].valor5.value = '<%= (String) gVO.getAtributo("valor5") %>';
          }
          if ('<%= (String) gVO.getAtributo("valor6") %>' != 'null')
          {
            document.forms[0].valor6.value = '<%= (String) gVO.getAtributo("valor6") %>';
          }
          if ('<%= (String) gVO.getAtributo("valor7") %>' != 'null')
          {
            document.forms[0].valor7.value = '<%= (String) gVO.getAtributo("valor7") %>';
          }
          document.forms[0].codEstructura.value = '<%= (String) gVO.getAtributo("codEstructura") %>';
          document.forms[0].codInforme.value = '<%= (String) gVO.getAtributo("codInforme") %>';
      <% } %>
    }

    function pulsarSalir() {
      self.parent.opener.retornoXanelaAuxiliar();
    }

    function cargarCamposDisponibles() {
      document.forms[0].opcion.value="cargarCamposDisponibles";
      document.forms[0].target="oculto";
      document.forms[0].action="<%=request.getContextPath()%>/informes/GeneradorInformes.do";
      document.forms[0].submit();
    }

    function cargarTablaDisponibles(list1,list2) {
      listaCamposDisponibles = list1;
      listaCamposDisponiblesOriginal = list2;
      tabDisponibles.lineas = listaCamposDisponibles;
      refrescaDisponibles();
      cargarCombosFiltro();
    }

    function pulsarDerecha() {
      if(tabDisponibles.selectedIndex != -1) {
        var lineas = tabElegidos.lineas;
        var tamIndex = tabDisponibles.selectedIndex;
        for (m=0; m < lineas.length; m++) {
        }
        listaCamposElegidos[m]= listaCamposDisponibles[tamIndex];
        listaCamposElegidosOriginal[m] = listaCamposDisponiblesOriginal[tamIndex];

        var list = new Array();
        var listOriginal = new Array();
        tamLength=tabDisponibles.lineas.length;
        for (i=tamIndex - 1; i < listaCamposDisponibles.length - 1; i++){
          if (i + 1 <= listaCamposDisponibles.length - 2){
            listaCamposDisponibles[i + 1]=listaCamposDisponibles[i + 2];
            listaCamposDisponiblesOriginal[i + 1]=listaCamposDisponiblesOriginal[i + 2];
          }
        }
        for(j=0; j < listaCamposDisponibles.length-1 ; j++){
          list[j] = listaCamposDisponibles[j];
          listOriginal[j] = listaCamposDisponiblesOriginal[j];
        }
        tabDisponibles.lineas=list;
        listaCamposDisponibles = list;
        listaCamposDisponiblesOriginal = listOriginal;
        refrescaDisponibles();
        tabElegidos.lineas=listaCamposElegidos;
        refrescaElegidos();
        cargarCombosOrdenacion();
      } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
      }
    }

    function pulsarDerechaTodos() {
      if(tabDisponibles.lineas.length >0) {
          var lineas = tabElegidos.lineas;
          var lineasPrimera = tabDisponibles.lineas;
          for (m=0; m < lineas.length; m++) {
          }
          for (j=0; j < lineasPrimera.length; j++) {
            listaCamposElegidos[m+j]= listaCamposDisponibles[j];
            listaCamposElegidosOriginal[m+j]= listaCamposDisponiblesOriginal[j];
          }
          listaCamposDisponibles = new Array();
          listaCamposDisponiblesOriginal = new Array();
          tabDisponibles.lineas = listaCamposDisponibles;
          refrescaDisponibles();
          tabElegidos.lineas=listaCamposElegidos;
          refrescaElegidos();
          cargarCombosOrdenacion();
      }
    }

    function pulsarIzquierda() {
      if(tabElegidos.selectedIndex != -1) {
        if(comprobarCamposOrdenacion()==1) {
            var lineas = tabDisponibles.lineas;
            var tamIndex = tabElegidos.selectedIndex;
            for (m=0; m < lineas.length; m++) {
            }
            listaCamposDisponibles[m]= listaCamposElegidos[tamIndex];
            listaCamposDisponiblesOriginal[m]= listaCamposElegidosOriginal[tamIndex];

            var list = new Array();
            var listOriginal = new Array();
            tamLength=tabElegidos.lineas.length;
            for (i=tamIndex - 1; i < listaCamposElegidos.length - 1; i++){
              if (i + 1 <= listaCamposElegidos.length - 2){
                listaCamposElegidos[i + 1]=listaCamposElegidos[i + 2];
                listaCamposElegidosOriginal[i + 1]=listaCamposElegidosOriginal[i + 2];
              }
            }
            for(j=0; j < listaCamposElegidos.length-1 ; j++){
              list[j] = listaCamposElegidos[j];
              listOriginal[j] = listaCamposElegidosOriginal[j];
            }
            tabElegidos.lineas=list;
            listaCamposElegidos = list;
            listaCamposElegidosOriginal = listOriginal;
            refrescaDisponibles();
            refrescaElegidos();
            cargarCombosOrdenacion();
        }
      } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
      }
    }

    function pulsarIzquierdaTodos() {
      if(tabElegidos.lineas.length >0) {
          if(comprobarCamposOrdenacion2()==1) {
              var lineas = tabDisponibles.lineas;
              var lineasSegunda = tabElegidos.lineas;
              for (m=0; m < lineas.length; m++) {
              }
              for (j=0; j < lineasSegunda.length; j++) {
                listaCamposDisponibles[m+j]= listaCamposElegidos[j];
                listaCamposDisponiblesOriginal[m+j]= listaCamposElegidosOriginal[j];
              }
              listaCamposElegidos = new Array();
              listaCamposElegidosOriginal = new Array();
              tabElegidos.lineas = listaCamposElegidos;
              refrescaDisponibles();
              refrescaElegidos();
              cargarCombosOrdenacion();
          }
      }
    }

    function comprobarCamposOrdenacion() {
      var tamIndex = tabElegidos.selectedIndex;
      if(listaCamposElegidosOriginal[tamIndex][0] == document.forms[0].codCampoOrdenacion1.value) {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0
        } else {
          comboCampoOrdenacion1.selectItem(-1);
          comboCampoOrdenacion2.deactivate();
          comboCampoOrdenacion2.selectItem(-1);
          comboCampoOrdenacion3.deactivate();
          comboCampoOrdenacion3.selectItem(-1);
          comboCampoOrdenacion4.deactivate();
          comboCampoOrdenacion4.selectItem(-1);
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion1.selectItem(-1);
          comboSentidoOrdenacion2.selectItem(-1);
          comboSentidoOrdenacion3.selectItem(-1);
          comboSentidoOrdenacion4.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      } else if(listaCamposElegidosOriginal[tamIndex][0] == document.forms[0].codCampoOrdenacion2.value) {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0
        } else {
          comboCampoOrdenacion2.selectItem(-1);
          comboCampoOrdenacion2.deactivate();
          comboCampoOrdenacion3.deactivate();
          comboCampoOrdenacion3.selectItem(-1);
          comboCampoOrdenacion4.deactivate();
          comboCampoOrdenacion4.selectItem(-1);
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion2.selectItem(-1);
          comboSentidoOrdenacion3.selectItem(-1);
          comboSentidoOrdenacion4.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      } else if(listaCamposElegidosOriginal[tamIndex][0] == document.forms[0].codCampoOrdenacion3.value) {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0
        } else {
          comboCampoOrdenacion3.deactivate();
          comboCampoOrdenacion3.selectItem(-1);
          comboCampoOrdenacion4.deactivate();
          comboCampoOrdenacion4.selectItem(-1);
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion3.selectItem(-1);
          comboSentidoOrdenacion4.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      } else if(listaCamposElegidosOriginal[tamIndex][0] == document.forms[0].codCampoOrdenacion4.value) {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0
        } else {
          comboCampoOrdenacion4.deactivate();
          comboCampoOrdenacion4.selectItem(-1);
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion4.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      } else if(listaCamposElegidosOriginal[tamIndex][0] == document.forms[0].codCampoOrdenacion5.value) {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0
        } else {
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      }
      return 1;
    }

    function comprobarCamposOrdenacion2() {
      if(document.forms[0].codCampoOrdenacion1.value != "") {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0
        } else {
          comboCampoOrdenacion1.selectItem(-1);
          comboCampoOrdenacion2.deactivate();
          comboCampoOrdenacion2.selectItem(-1);
          comboCampoOrdenacion3.deactivate();
          comboCampoOrdenacion3.selectItem(-1);
          comboCampoOrdenacion4.deactivate();
          comboCampoOrdenacion4.selectItem(-1);
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion1.selectItem(-1);
          comboSentidoOrdenacion2.selectItem(-1);
          comboSentidoOrdenacion3.selectItem(-1);
          comboSentidoOrdenacion4.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      } else if(document.forms[0].codCampoOrdenacion2.value != "") {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0;
        } else {
          comboCampoOrdenacion2.selectItem(-1);
          comboCampoOrdenacion2.deactivate();
          comboCampoOrdenacion3.deactivate();
          comboCampoOrdenacion3.selectItem(-1);
          comboCampoOrdenacion4.deactivate();
          comboCampoOrdenacion4.selectItem(-1);
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion2.selectItem(-1);
          comboSentidoOrdenacion3.selectItem(-1);
          comboSentidoOrdenacion4.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      } else if(document.forms[0].codCampoOrdenacion3.value != "") {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0
        } else {
          comboCampoOrdenacion3.deactivate();
          comboCampoOrdenacion3.selectItem(-1);
          comboCampoOrdenacion4.deactivate();
          comboCampoOrdenacion4.selectItem(-1);
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion3.selectItem(-1);
          comboSentidoOrdenacion4.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      } else if(document.forms[0].codCampoOrdenacion4.value != "") {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0
        } else {
          comboCampoOrdenacion4.deactivate();
          comboCampoOrdenacion4.selectItem(-1);
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion4.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      } else if(document.forms[0].codCampoOrdenacion5.value != "") {
        if(jsp_alerta('C', '<%=descriptor.getDescripcion("msjCampoOrd")%>') != 1) {
          return 0
        } else {
          comboCampoOrdenacion5.deactivate();
          comboCampoOrdenacion5.selectItem(-1);
          comboSentidoOrdenacion5.selectItem(-1);
          return 1;
        }
      }
      return 1;
    }

    function cargarCombosOrdenacion() {
      cod_camposOrdenacion = new Array();
      desc_camposOrdenacion = new Array();
      for(var m=0;m < listaCamposElegidos.length;m++) {
        cod_camposOrdenacion[m] = listaCamposElegidosOriginal[m][0];
        desc_camposOrdenacion[m] = listaCamposElegidos[m][0];
      }

      comboCampoOrdenacion1.addItems(cod_camposOrdenacion,desc_camposOrdenacion);
      comboCampoOrdenacion2.addItems(cod_camposOrdenacion,desc_camposOrdenacion);
      comboCampoOrdenacion3.addItems(cod_camposOrdenacion,desc_camposOrdenacion);
      comboCampoOrdenacion4.addItems(cod_camposOrdenacion,desc_camposOrdenacion);
      comboCampoOrdenacion5.addItems(cod_camposOrdenacion,desc_camposOrdenacion);

      comboCampoOrdenacion1.activate();
    }

    function cargarCombosFiltro() {
      cod_camposFiltro= new Array();
      desc_camposFiltro= new Array();
      for(var m=0; m < listaCamposDisponibles.length;m++) {
        cod_camposFiltro[m] = listaCamposDisponiblesOriginal[m][0];
        desc_camposFiltro[m] = listaCamposDisponibles[m][0];
      }

      comboCampo1.addItems(cod_camposFiltro,desc_camposFiltro);
      comboCampo2.addItems(cod_camposFiltro,desc_camposFiltro);
      comboCampo3.addItems(cod_camposFiltro,desc_camposFiltro);
      comboCampo4.addItems(cod_camposFiltro,desc_camposFiltro);
      comboCampo5.addItems(cod_camposFiltro,desc_camposFiltro);
      comboCampo6.addItems(cod_camposFiltro,desc_camposFiltro);
      comboCampo7.addItems(cod_camposFiltro,desc_camposFiltro);

      comboCampo1.activate();
    }

    function pulsarAceptar() {
      if(comprobarObligatorios()) {
        var listas = crearListas();
        document.forms[0].listaCodCamposElegidos.value = listas[0];
        document.forms[0].listaCampoCampos.value = listas[1];
        document.forms[0].listaNombreAs.value = listas[2];
        <% if("modificar".equals(operacion)) { %>
          document.forms[0].opcion.value="modificarInforme";
        <% } else { %>
          document.forms[0].opcion.value="altaInforme";
        <% } %>
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/informes/GeneradorInformes.do'/>";
        document.forms[0].submit();
      }
    }

    function comprobarObligatorios() {
      if(validarObligatorios()) {
        if(listaCamposElegidos.length == 0) {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoCampoEleg")%>');
          return false;
        } else if(document.forms[0].codCampo1.value == "" || document.forms[0].codCondicion1.value == "" || document.forms[0].valor1.value == "") {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjNingFilt")%>');
          return false;
        } else if(document.forms[0].codOperador1.value != "" &&(document.forms[0].codCampo2.value == "" || document.forms[0].codCondicion2.value == "" || document.forms[0].valor2.value == "")) {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjFiltMalDef")%>');
          return false;
        } else if(document.forms[0].codOperador2.value != "" &&(document.forms[0].codCampo3.value == "" || document.forms[0].codCondicion3.value == "" || document.forms[0].valor3.value == "")) {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjFiltMalDef")%>');
          return false;
        } else if(document.forms[0].codOperador3.value != "" &&(document.forms[0].codCampo4.value == "" || document.forms[0].codCondicion4.value == "" || document.forms[0].valor4.value == "")) {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjFiltMalDef")%>');
          return false;
        } else if(document.forms[0].codOperador4.value != "" &&(document.forms[0].codCampo5.value == "" || document.forms[0].codCondicion5.value == "" || document.forms[0].valor5.value == "")) {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjFiltMalDef")%>');
          return false;
        } else if(document.forms[0].codOperador5.value != "" &&(document.forms[0].codCampo6.value == "" || document.forms[0].codCondicion6.value == "" || document.forms[0].valor6.value == "")) {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjFiltMalDef")%>');
          return false;
        } else if(document.forms[0].codOperador6.value != "" &&(document.forms[0].codCampo7.value == "" || document.forms[0].codCondicion7.value == "" || document.forms[0].valor7.value == "")) {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjFiltMalDef")%>');
          return false;
        } else if(document.forms[0].codCampoOrdenacion1.value != "" && document.forms[0].codSentidoOrdenacion1.value == "") {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjOrdMalDef")%>');
          return false;
        } else if(document.forms[0].codCampoOrdenacion2.value != "" && document.forms[0].codSentidoOrdenacion2.value == "") {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjOrdMalDef")%>');
          return false;
        } else if(document.forms[0].codCampoOrdenacion3.value != "" && document.forms[0].codSentidoOrdenacion3.value == "") {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjOrdMalDef")%>');
          return false;
        } else if(document.forms[0].codCampoOrdenacion4.value != "" && document.forms[0].codSentidoOrdenacion4.value == "") {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjOrdMalDef")%>');
          return false;
        } else if(document.forms[0].codCampoOrdenacion5.value != "" && document.forms[0].codSentidoOrdenacion5.value == "") {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjOrdMalDef")%>');
          return false;
        } else return true;
      }
    }

    function desactivarCombosOrdenacionFiltro() {
      comboCampoOrdenacion1.deactivate();
      comboCampoOrdenacion2.deactivate();
      comboCampoOrdenacion3.deactivate();
      comboCampoOrdenacion4.deactivate();
      comboCampoOrdenacion5.deactivate();

      comboCampo1.deactivate();
      comboCampo2.deactivate();
      comboCampo3.deactivate();
      comboCampo4.deactivate();
      comboCampo5.deactivate();
      comboCampo6.deactivate();
      comboCampo7.deactivate();
    }

    function crearListas() {
      var listaCodCampos = "";
      var listaCampoCampos = "";
      var listaNombreAs = "";
      for (i=0; i < listaCamposElegidos.length; i++) {
        listaCodCampos += listaCamposElegidosOriginal[i][0]+'зе';
        listaCampoCampos += listaCamposElegidosOriginal[i][4]+'зе';
        listaNombreAs += listaCamposElegidosOriginal[i][5]+'зе';
      }
      var listas = new Array();
      listas = [listaCodCampos,listaCampoCampos,listaNombreAs];
      return listas;
    }

    function altaRealizada() {
      jsp_alerta('A', '<%=descriptor.getDescripcion("msjAltaRealiz")%>');
      var retorno = new Array();
      retorno = ["si"];
      self.parent.opener.retornoXanelaAuxiliar(retorno);
    }

    function altaNoRealizada() {
      jsp_alerta('A', '<%=descriptor.getDescripcion("msjAltaNoRealiz")%>');
    }

    function modificacionRealizada() {
      jsp_alerta('A', '<%=descriptor.getDescripcion("msjModifRealiz")%>');
      var retorno = new Array();
      retorno = ["si"];
      self.parent.opener.retornoXanelaAuxiliar(retorno);
    }

    function modificacionNoRealizada() {
      jsp_alerta('A', '<%=descriptor.getDescripcion("msjModifNoRealiz")%>');
    }

    </SCRIPT>
</head>

<BODY onload="javascript:{ pleaseWait('off'); 
        inicializar()}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <html:form action="/informes/GeneradorInformes.do" target="_self">

    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="listaCodCamposElegidos" value="">
    <input type="hidden" name="listaCampoCampos" value="">
    <input type="hidden" name="listaNombreAs" value="">
    <input type="hidden" name="codAplicacion" value="">
    <input type="hidden" name="codEstructura" value="">
    <input type="hidden" name="codInforme" value="">

    <!-- Datos. -->
    <div class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_GenInformes")%></div>
    <div class="contenidoPantalla">
              <div class="tab-pane" id="tab-pane-1">

              <script type="text/javascript">
                tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
              </script>

              <!-- CAPA 1: DATOS
              ------------------------------ -->

              <div class="tab-page" id="tabPage1" style="height:300px" >

              <h2 class="tab"><%=descriptor.getDescripcion("etiq_datosInf")%></h2>

              <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>


              <TABLE id ="tablaDatosGral" class="tablaP" width="650px" cellspacing="0px" cellpadding="10px" border="0">
                <tr>
                    <td width="100%" colspan="2" height="18px" bgcolor="#DCDCCC" class="etiqueta">&nbsp;&nbsp;<%=descriptor.getDescripcion("etiq_datosInf")%></td>
                </tr>
                <tr>
                    <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("gNome_Informe")%> :</td>
                    <td width="75%" class="textoc"><input type="Text" name="nombreInforme" id="obligatorio" size="20" maxlength="30" class="inputTextoObligatorio"
                                                    onkeypress="javascript:PasaAMayusculas(event);"></td>
                </tr>
                <tr>
                    <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("gDesc_Informe")%> :</td>
                    <td width="75%" class="textoc"><input type="text" name="descripcion" id="obligatorio" size="60" maxlength="256" class="inputTextoObligatorio"
                                                    onkeypress="javascript:PasaAMayusculas(event);"></td>
                </tr>
                <tr>
                    <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("gEnt_Informe")%> :</td>
                    <td width="75%" class="textoc">
                        <input type="Hidden" name="codEntidad" value="">
                        <input type="Text" name="descEntidad" id="obligatorio" class="inputTextoObligatorio" style="width:294;height:17" readonly="true">
                        <A href="" id="anchorEntidad" name="anchorEntidad">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonEntidad" name="botonEntidad"></span>
                        </A>
                    </td>
                </tr>
              </table>

              </div>

              <!-- CAPA 2: CAMPOS
               ------------------------------ -->

              <div class="tab-page" id="tabPage2" style="height:300px">

              <h2 class="tab"><%=descriptor.getDescripcion("etiqCampos")%></h2>

              <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>

              <TABLE id ="tablaTemas" width="650px" cellspacing="0px" cellpadding="10px" border="0">
                <tr>
                    <td width="100%" colspan="2" height="18px" bgcolor="#DCDCCC" class="etiqueta">&nbsp;&nbsp;<%=descriptor.getDescripcion("gEtiq_CampInforme")%></td>
                </tr>
                <tr>
                    <td width="100%" align="center" valign="top" >
                        <table width="100%" cellspacing="0px" cellpadding="0px" border="0px" bgcolor="#ffffff" valign="top">
                            <tr>
                              <td width="100%" colspan="4" height="10px"></td>
                            </tr>
                            <tr>
                                <TD id="tablaDisponibles" width="40%" align="center" bgcolor="#ffffff"></TD>
                                <td width="20%" class="textoc" align="center">
                                    <table border="0px" cellpadding="0px" cellspacing="0px"  width="100%">
                                        <tr>
                                            <td width="100%" align="center"><INPUT NAME="BotonAsignar" TYPE="button" class="botonGeneral" style="width:30;height:30;" ONCLICK="pulsarDerecha();" VALUE="&gt;"></td>
                                        </tr>
                                        <tr><td height="5"></td></tr>
                                        <tr>
                                            <td width="100%" align="center"><INPUT NAME="BotonAsignarTodos" TYPE="button" class="botonGeneral" style="width:30;height:30;" ONCLICK="pulsarDerechaTodos();" VALUE="&gt;&gt;"></td>
                                        </tr>
                                        <tr><td height="5"></td></tr>
                                        <tr>
                                            <td width="100%" align="center"><INPUT NAME="BotonDesAsignar" TYPE="button" class="botonGeneral" style="width:30;height:30;" ONCLICK="pulsarIzquierda();" VALUE="&lt"></td>
                                        </tr>
                                        <tr><td height="5"></td></tr>
                                        <tr>
                                            <td width="100%" align="center"><INPUT NAME="BotonDesAsignarTodos" TYPE="button" class="botonGeneral" style="width:30;height:30;" ONCLICK="pulsarIzquierdaTodos();" VALUE="&lt;&lt;"></td>
                                        </tr>
                                    </table>
                                </td>
                                <TD id="tablaElegidos" width="40%" align="center" bgcolor="#ffffff"></TD>
                            </tr>
                        </table>
                    </td>
                </tr>
              </TABLE>

              </div>

              <!-- CAPA 3: ORDENACION
               ------------------------------ -->

              <div class="tab-page" id="tabPage3" style="height:300px">

              <h2 class="tab"><%=descriptor.getDescripcion("etiqOrdenac")%></h2>

              <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>

              <TABLE id ="tablaE" width="650px" cellpadding="10px" cellspacing="0px" border="0px">
                <tr>
                    <td width="100%" colspan="2" height="18px" bgcolor="#DCDCCC" class="etiqueta">&nbsp;&nbsp;<%=descriptor.getDescripcion("gEtiq_OrdInforme")%></td>
                </tr>
                <tr>
                    <td width="60%" class="etiqueta" align="center"><%=descriptor.getDescripcion("etiqCampOrd")%></td>
                    <td width="40%" class="etiqueta" align="left"><%=descriptor.getDescripcion("etiqSentido")%></td>
                </tr>
                <tr>
                    <td width="60%" align="right">
                      <table width="100%" cellpadding="4px" cellspacing="0px" border="0px">
                        <tr>
                          <td width="100%" class="textoc" align="right">
                            <input type="Hidden" name="codCampoOrdenacion1" value="">
                            <input type="Text" name="descCampoOrdenacion1" class="inputTexto" style="width:275;height:17" readonly="true">
                            <A href="" id="anchorCampoOrdenacion1" name="anchorCampoOrdenacion1">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion1" name="botonCampoOrdenacion1"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" class="textoc" align="right">
                            <input type="Hidden" name="codCampoOrdenacion2" value="">
                            <input type="Text" name="descCampoOrdenacion2" class="inputTexto" style="width:275;height:17" readonly="true">
                            <A href="" id="anchorCampoOrdenacion2" name="anchorCampoOrdenacion2">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion2" name="botonCampoOrdenacion2"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" class="textoc" align="right">
                            <input type="Hidden" name="codCampoOrdenacion3" value="">
                            <input type="Text" name="descCampoOrdenacion3" class="inputTexto" style="width:275;height:17" readonly="true">
                            <A href="" id="anchorCampoOrdenacion3" name="anchorCampoOrdenacion3">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion3" name="botonCampoOrdenacion3"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" class="textoc" align="right">
                            <input type="Hidden" name="codCampoOrdenacion4" value="">
                            <input type="Text" name="descCampoOrdenacion4" class="inputTexto" style="width:275;height:17" readonly="true">
                            <A href="" id="anchorCampoOrdenacion4" name="anchorCampoOrdenacion4">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion4" name="botonCampoOrdenacion4"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" class="textoc" align="right">
                            <input type="Hidden" name="codCampoOrdenacion5" value="">
                            <input type="Text" name="descCampoOrdenacion5" class="inputTexto" style="width:275;height:17" readonly="true">
                            <A href="" id="anchorCampoOrdenacion5" name="anchorCampoOrdenacion5">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampoOrdenacion5" name="botonCampoOrdenacion5"></span>
                            </A>
                          </td>
                        </tr>
                      </table>
                    </td>
                    <td width="40%" class="textoc" align="right">
                      <table width="100%" cellpadding="4px" cellspacing="0px" border="0px">
                        <tr>
                          <td width="100%" class="textoc">
                            <input type="Hidden" name="codSentidoOrdenacion1" value="">
                            <input type="Text" name="descSentidoOrdenacion1" class="inputTexto" style="width:100;height:17" readonly="true">
                            <A href="" id="anchorSentidoOrdenacion1" name="anchorSentidoOrdenacion1">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion1" name="botonSentidoOrdenacion1"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" class="textoc">
                            <input type="Hidden" name="codSentidoOrdenacion2" value="">
                            <input type="Text" name="descSentidoOrdenacion2" class="inputTexto" style="width:100;height:17" readonly="true">
                            <A href="" id="anchorSentidoOrdenacion2" name="anchorSentidoOrdenacion2">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion2" name="botonSentidoOrdenacion2"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" class="textoc">
                            <input type="Hidden" name="codSentidoOrdenacion3" value="">
                            <input type="Text" name="descSentidoOrdenacion3" class="inputTexto" style="width:100;height:17" readonly="true">
                            <A href="" id="anchorSentidoOrdenacion3" name="anchorSentidoOrdenacion3">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion3" name="botonSentidoOrdenacion3"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" class="textoc">
                            <input type="Hidden" name="codSentidoOrdenacion4" value="">
                            <input type="Text" name="descSentidoOrdenacion4" class="inputTexto" style="width:100;height:17" readonly="true">
                            <A href="" id="anchorSentidoOrdenacion4" name="anchorSentidoOrdenacion4">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion4" name="botonSentidoOrdenacion4"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" class="textoc">
                            <input type="Hidden" name="codSentidoOrdenacion5" value="">
                            <input type="Text" name="descSentidoOrdenacion5" class="inputTexto" style="width:100;height:17" readonly="true">
                            <A href="" id="anchorSentidoOrdenacion5" name="anchorSentidoOrdenacion5">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonSentidoOrdenacion5" name="botonSentidoOrdenacion5"></span>
                            </A>
                          </td>
                        </tr>
                      </table>
                    </td>
                </tr>
              </table>

              </div>

              <!-- CAPA 4: FILTRO
               ------------------------------ -->

              <div class="tab-page" id="tabPage4" style="height:300px">

              <h2 class="tab"><%=descriptor.getDescripcion("etiqFiltro")%></h2>

              <script type="text/javascript">tp1_p4 = tp1.addTabPage( document.getElementById( "tabPage4" ) );</script>

              <TABLE id ="tablaTemas" width="650px" cellspacing="0px" cellpadding="10px" border="0">
                <tr>
                    <td width="100%" colspan="4" height="18px" bgcolor="#DCDCCC" class="etiqueta">&nbsp;&nbsp;<%=descriptor.getDescripcion("gEtiq_FiltInforme")%></td>
                </tr>
                <tr>
                    <td width="15%" class="etiqueta" align="center"></td>
                    <td width="40%" class="etiqueta" align="center"><%=descriptor.getDescripcion("etiqCamposFilt")%></td>
                    <td width="15%" class="etiqueta" align="center"><%=descriptor.getDescripcion("etiqCondicion")%></td>
                    <td width="30%" class="etiqueta" align="center"><%=descriptor.getDescripcion("etiqValor")%></td>
                </tr>
                <tr>
                    <td width="15%" align="center">
                      <table width="100%" cellpadding="3px" cellspacing="0px" border="0px">
                        <tr>
                          <td width="100%">&nbsp;</td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codOperador1" value="">
                            <input type="Text" name="descOperador1" class="inputTexto" style="width:40;height:17" readonly="true">
                            <A href="" id="anchorOperador1" name="anchorOperador1">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador1" name="botonOperador1"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codOperador2" value="">
                            <input type="Text" name="descOperador2" class="inputTexto" style="width:40;height:17" readonly="true">
                            <A href="" id="anchorOperador2" name="anchorOperador2">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador2" name="botonOperador2"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codOperador3" value="">
                            <input type="Text" name="descOperador3" class="inputTexto" style="width:40;height:17" readonly="true">
                            <A href="" id="anchorOperador3" name="anchorOperador3">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador3" name="botonOperador3"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codOperador4" value="">
                            <input type="Text" name="descOperador4" class="inputTexto" style="width:40;height:17" readonly="true">
                            <A href="" id="anchorOperador4" name="anchorOperador4">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador4" name="botonOperador4"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codOperador5" value="">
                            <input type="Text" name="descOperador5" class="inputTexto" style="width:40;height:17" readonly="true">
                            <A href="" id="anchorOperador5" name="anchorOperador5">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador5" name="botonOperador5"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codOperador6" value="">
                            <input type="Text" name="descOperador6" class="inputTexto" style="width:40;height:17" readonly="true">
                            <A href="" id="anchorOperador6" name="anchorOperador6">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOperador6" name="botonOperador6"></span>
                            </A>
                          </td>
                        </tr>
                      </table>
                    </td>
                    <td width="40%" align="center">
                      <table width="100%" cellpadding="3px" cellspacing="0px" border="0px">
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCampo1" value="">
                            <input type="Text" name="descCampo1" class="inputTexto" style="width:210;height:17" readonly="true">
                            <A href="" id="anchorCampo1" name="anchorCampo1">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo1" name="botonCampo1"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCampo2" value="">
                            <input type="Text" name="descCampo2" class="inputTexto" style="width:210;height:17" readonly="true">
                            <A href="" id="anchorCampo2" name="anchorCampo2">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo2" name="botonCampo2"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCampo3" value="">
                            <input type="Text" name="descCampo3" class="inputTexto" style="width:210;height:17" readonly="true">
                            <A href="" id="anchorCampo3" name="anchorCampo3">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo3" name="botonCampo3"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCampo4" value="">
                            <input type="Text" name="descCampo4" class="inputTexto" style="width:210;height:17" readonly="true">
                            <A href="" id="anchorCampo4" name="anchorCampo4">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo4" name="botonCampo4"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCampo5" value="">
                            <input type="Text" name="descCampo5" class="inputTexto" style="width:210;height:17" readonly="true">
                            <A href="" id="anchorCampo5" name="anchorCampo5">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo5" name="botonCampo5"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCampo6" value="">
                            <input type="Text" name="descCampo6" class="inputTexto" style="width:210;height:17" readonly="true">
                            <A href="" id="anchorCampo6" name="anchorCampo6">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo6" name="botonCampo6"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCampo7" value="">
                            <input type="Text" name="descCampo7" class="inputTexto" style="width:210;height:17" readonly="true">
                            <A href="" id="anchorCampo7" name="anchorCampo7">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCampo7" name="botonCampo7"></span>
                            </A>
                          </td>
                        </tr>
                      </table>
                    </td>
                    <td width="15%" align="center">
                      <table width="100%" cellpadding="3px" cellspacing="0px" border="0px">
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCondicion1" value="">
                            <input type="Text" name="descCondicion1" class="inputTexto" style="width:48;height:17" readonly="true">
                            <A href="" id="anchorCondicion1" name="anchorCondicion1">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion1" name="botonCondicion1"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCondicion2" value="">
                            <input type="Text" name="descCondicion2" class="inputTexto" style="width:48;height:17" readonly="true">
                            <A href="" id="anchorCondicion2" name="anchorCondicion2">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion2" name="botonCondicion2"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCondicion3" value="">
                            <input type="Text" name="descCondicion3" class="inputTexto" style="width:48;height:17" readonly="true">
                            <A href="" id="anchorCondicion3" name="anchorCondicion3">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion3" name="botonCondicion3"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCondicion4" value="">
                            <input type="Text" name="descCondicion4" class="inputTexto" style="width:48;height:17" readonly="true">
                            <A href="" id="anchorCondicion4" name="anchorCondicion4">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion4" name="botonCondicion4"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCondicion5" value="">
                            <input type="Text" name="descCondicion5" class="inputTexto" style="width:48;height:17" readonly="true">
                            <A href="" id="anchorCondicion5" name="anchorCondicion5">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion5" name="botonCondicion5"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCondicion6" value="">
                            <input type="Text" name="descCondicion6" class="inputTexto" style="width:48;height:17" readonly="true">
                            <A href="" id="anchorCondicion6" name="anchorCondicion6">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion6" name="botonCondicion6"></span>
                            </A>
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Hidden" name="codCondicion7" value="">
                            <input type="Text" name="descCondicion7" class="inputTexto" style="width:48;height:17" readonly="true">
                            <A href="" id="anchorCondicion7" name="anchorCondicion7">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonCondicion7" name="botonCondicion7"></span>
                            </A>
                          </td>
                        </tr>
                      </table>
                    </td>
                    <td width="30%" align="center">
                      <table width="100%" cellpadding="2px" cellspacing="0px" border="0px">
                        <tr>
                          <td width="100%" align="center">
                            <input type="Text" name="valor1" class="inputTexto" size="25" style="height:17" value=""
                            onkeypress="javascript:PasaAMayusculas(event);">
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Text" name="valor2" class="inputTexto" size="25" style="height:17" value=""
                            onkeypress="javascript:PasaAMayusculas(event);">
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Text" name="valor3" class="inputTexto" size="25" style="height:17" value=""
                            onkeypress="javascript:PasaAMayusculas(event);">
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Text" name="valor4" class="inputTexto" size="25" style="height:17" value=""
                            onkeypress="javascript:PasaAMayusculas(event);">
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Text" name="valor5" class="inputTexto" size="25" style="height:17" value=""
                            onkeypress="javascript:PasaAMayusculas(event);">
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Text" name="valor6" class="inputTexto" size="25" style="height:17" value=""
                            onkeypress="javascript:PasaAMayusculas(event);">
                          </td>
                        </tr>
                        <tr>
                          <td width="100%" align="center">
                            <input type="Text" name="valor7" class="inputTexto" size="25" style="height:17" value=""
                            onkeypress="javascript:PasaAMayusculas(event);">
                          </td>
                        </tr>
                      </table>
                    </td>
                </tr>
              </TABLE>

              </div>

              </div>
    <!-- Fin datos. -->
            <div class="botoneraPrincipal"> 
              <input type="button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar" onClick="pulsarAceptar();">
              <input type="button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onClick="pulsarSalir();">
            </div>
        </div>
    </html:form>

    <script language="JavaScript1.2">

    tabDisponibles = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDisponibles'));

    tabDisponibles.addColumna('275','left','<%= descriptor.getDescripcion("gCampos_Dispon")%>');
    tabDisponibles.displayCabecera=true;
    tabDisponibles.displayTabla();

    function refrescaDisponibles() {
      tabDisponibles.displayTabla();
    }

    tabElegidos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaElegidoss'));

    tabElegidos.addColumna('275','left','<%= descriptor.getDescripcion("gCampos_Elegid")%>');
    tabElegidos.displayCabecera=true;
    tabElegidos.displayTabla();

    function refrescaElegidos() {
      tabElegidos.displayTabla();
    }

    var comboEntidad = new Combo("Entidad");

    var comboCampoOrdenacion1 = new Combo("CampoOrdenacion1");
    var comboCampoOrdenacion2 = new Combo("CampoOrdenacion2");
    var comboCampoOrdenacion3 = new Combo("CampoOrdenacion3");
    var comboCampoOrdenacion4 = new Combo("CampoOrdenacion4");
    var comboCampoOrdenacion5 = new Combo("CampoOrdenacion5");

    var comboSentidoOrdenacion1 = new Combo("SentidoOrdenacion1");
    var comboSentidoOrdenacion2 = new Combo("SentidoOrdenacion2");
    var comboSentidoOrdenacion3 = new Combo("SentidoOrdenacion3");
    var comboSentidoOrdenacion4 = new Combo("SentidoOrdenacion4");
    var comboSentidoOrdenacion5 = new Combo("SentidoOrdenacion5");

    var comboOperador1 = new Combo("Operador1");
    var comboOperador2 = new Combo("Operador2");
    var comboOperador3 = new Combo("Operador3");
    var comboOperador4 = new Combo("Operador4");
    var comboOperador5 = new Combo("Operador5");
    var comboOperador6 = new Combo("Operador6");

    var comboCampo1 = new Combo("Campo1");
    var comboCampo2 = new Combo("Campo2");
    var comboCampo3 = new Combo("Campo3");
    var comboCampo4 = new Combo("Campo4");
    var comboCampo5 = new Combo("Campo5");
    var comboCampo6 = new Combo("Campo6");
    var comboCampo7 = new Combo("Campo7");

    var comboCondicion1 = new Combo("Condicion1");
    var comboCondicion2 = new Combo("Condicion2");
    var comboCondicion3 = new Combo("Condicion3");
    var comboCondicion4 = new Combo("Condicion4");
    var comboCondicion5 = new Combo("Condicion5");
    var comboCondicion6 = new Combo("Condicion6");
    var comboCondicion7 = new Combo("Condicion7");

    comboEntidad.change = function() {
      <% if("modificar".equals(operacion)) { %>
      <% } else { %>
        cargarCamposDisponibles();
      <% } %>
    }

    comboCampoOrdenacion1.change = function() {
      if(comboCampoOrdenacion1.des.value.length != 0) {
        comboCampoOrdenacion2.activate();
      } else {
        comboCampoOrdenacion2.deactivate();
        comboCampoOrdenacion2.selectItem(-1);
        comboCampoOrdenacion3.deactivate();
        comboCampoOrdenacion3.selectItem(-1);
        comboCampoOrdenacion4.deactivate();
        comboCampoOrdenacion4.selectItem(-1);
        comboCampoOrdenacion5.deactivate();
        comboCampoOrdenacion5.selectItem(-1);
        comboSentidoOrdenacion1.selectItem(-1);
        comboSentidoOrdenacion2.selectItem(-1);
        comboSentidoOrdenacion3.selectItem(-1);
        comboSentidoOrdenacion4.selectItem(-1);
        comboSentidoOrdenacion5.selectItem(-1);
      }
    }

    comboCampoOrdenacion2.change = function() {

      if(comboCampoOrdenacion2.des.value.length != 0) {
        comboCampoOrdenacion3.activate();
      } else {
        comboCampoOrdenacion3.deactivate();
        comboCampoOrdenacion3.selectItem(-1);
        comboCampoOrdenacion4.deactivate();
        comboCampoOrdenacion4.selectItem(-1);
        comboCampoOrdenacion5.deactivate();
        comboCampoOrdenacion5.selectItem(-1);
        comboSentidoOrdenacion2.selectItem(-1);
        comboSentidoOrdenacion3.selectItem(-1);
        comboSentidoOrdenacion4.selectItem(-1);
        comboSentidoOrdenacion5.selectItem(-1);
      }
    }

    comboCampoOrdenacion3.change = function() {

      if(comboCampoOrdenacion3.des.value.length != 0) {
        comboCampoOrdenacion4.activate();
      } else {
        comboCampoOrdenacion4.deactivate();
        comboCampoOrdenacion4.selectItem(-1);
        comboCampoOrdenacion5.deactivate();
        comboCampoOrdenacion5.selectItem(-1);
        comboSentidoOrdenacion3.selectItem(-1);
        comboSentidoOrdenacion4.selectItem(-1);
        comboSentidoOrdenacion5.selectItem(-1);
      }
    }

    comboCampoOrdenacion4.change = function() {

      if(comboCampoOrdenacion4.des.value.length != 0) {
        comboCampoOrdenacion5.activate();
      } else {
        comboCampoOrdenacion5.deactivate();
        comboCampoOrdenacion5.selectItem(-1);
        comboSentidoOrdenacion4.selectItem(-1);
        comboSentidoOrdenacion5.selectItem(-1);
      }
    }

    comboCampoOrdenacion5.change = function() {
      if(comboCampoOrdenacion5.des.value.length != 0) {

      } else {
        comboSentidoOrdenacion5.selectItem(-1);
      }
    }

    comboCampo1.change = function() {
      if(comboCampo1.des.value.length != 0) {
        comboCampo2.activate();
      } else {
        comboCampo2.deactivate();
        comboCampo2.selectItem(-1);
        comboCampo3.deactivate();
        comboCampo3.selectItem(-1);
        comboCampo4.deactivate();
        comboCampo4.selectItem(-1);
        comboCampo5.deactivate();
        comboCampo5.selectItem(-1);
        comboCampo6.deactivate();
        comboCampo6.selectItem(-1);
        comboCampo7.deactivate();
        comboCampo7.selectItem(-1);
        comboCondicion1.selectItem(-1);
        comboCondicion2.selectItem(-1);
        comboCondicion3.selectItem(-1);
        comboCondicion4.selectItem(-1);
        comboCondicion5.selectItem(-1);
        comboCondicion6.selectItem(-1);
        comboCondicion7.selectItem(-1);
        document.forms[0].valor1.value = "";
        document.forms[0].valor2.value = "";
        document.forms[0].valor3.value = "";
        document.forms[0].valor4.value = "";
        document.forms[0].valor5.value = "";
        document.forms[0].valor6.value = "";
        document.forms[0].valor7.value = "";
      }
    }

    comboCampo2.change = function() {
      if(comboCampo2.des.value.length != 0) {
        comboCampo3.activate();
      } else {
        comboCampo3.deactivate();
        comboCampo3.selectItem(-1);
        comboCampo4.deactivate();
        comboCampo4.selectItem(-1);
        comboCampo5.deactivate();
        comboCampo5.selectItem(-1);
        comboCampo6.deactivate();
        comboCampo6.selectItem(-1);
        comboCampo7.deactivate();
        comboCampo7.selectItem(-1);
        comboCondicion2.selectItem(-1);
        comboCondicion3.selectItem(-1);
        comboCondicion4.selectItem(-1);
        comboCondicion5.selectItem(-1);
        comboCondicion6.selectItem(-1);
        comboCondicion7.selectItem(-1);
        document.forms[0].valor2.value = "";
        document.forms[0].valor3.value = "";
        document.forms[0].valor4.value = "";
        document.forms[0].valor5.value = "";
        document.forms[0].valor6.value = "";
        document.forms[0].valor7.value = "";
        comboOperador1.selectItem(-1);
        comboOperador2.selectItem(-1);
        comboOperador3.selectItem(-1);
        comboOperador4.selectItem(-1);
        comboOperador5.selectItem(-1);
        comboOperador6.selectItem(-1);
      }
    }

    comboCampo3.change = function() {

      if(comboCampo3.des.value.length != 0) {
        comboCampo4.activate();
      } else {
        comboCampo4.deactivate();
        comboCampo4.selectItem(-1);
        comboCampo5.deactivate();
        comboCampo5.selectItem(-1);
        comboCampo6.deactivate();
        comboCampo6.selectItem(-1);
        comboCampo7.deactivate();
        comboCampo7.selectItem(-1);
        comboCondicion3.selectItem(-1);
        comboCondicion4.selectItem(-1);
        comboCondicion5.selectItem(-1);
        comboCondicion6.selectItem(-1);
        comboCondicion7.selectItem(-1);
        document.forms[0].valor3.value = "";
        document.forms[0].valor4.value = "";
        document.forms[0].valor5.value = "";
        document.forms[0].valor6.value = "";
        document.forms[0].valor7.value = "";
        comboOperador2.selectItem(-1);
        comboOperador3.selectItem(-1);
        comboOperador4.selectItem(-1);
        comboOperador5.selectItem(-1);
        comboOperador6.selectItem(-1);
      }
    }

    comboCampo4.change = function() {
      if(comboCampo4.des.value.length != 0) {
        comboCampo5.activate();
      } else {
        comboCampo5.deactivate();
        comboCampo5.selectItem(-1);
        comboCampo6.deactivate();
        comboCampo6.selectItem(-1);
        comboCampo7.deactivate();
        comboCampo7.selectItem(-1);
        comboCondicion4.selectItem(-1);
        comboCondicion5.selectItem(-1);
        comboCondicion6.selectItem(-1);
        comboCondicion7.selectItem(-1);
        document.forms[0].valor4.value = "";
        document.forms[0].valor5.value = "";
        document.forms[0].valor6.value = "";
        document.forms[0].valor7.value = "";
        comboOperador3.selectItem(-1);
        comboOperador4.selectItem(-1);
        comboOperador5.selectItem(-1);
        comboOperador6.selectItem(-1);
      }
    }

    comboCampo5.change = function() {
      if(comboCampo5.des.value.length != 0) {
        comboCampo6.activate();
      } else {
        comboCampo6.deactivate();
        comboCampo6.selectItem(-1);
        comboCampo7.deactivate();
        comboCampo7.selectItem(-1);
        comboCondicion5.selectItem(-1);
        comboCondicion6.selectItem(-1);
        comboCondicion7.selectItem(-1);
        document.forms[0].valor5.value = "";
        document.forms[0].valor6.value = "";
        document.forms[0].valor7.value = "";
        comboOperador4.selectItem(-1);
        comboOperador5.selectItem(-1);
        comboOperador6.selectItem(-1);
      }
    }

    comboCampo6.change = function() {
      if(comboCampo6.des.value.length != 0) {
        comboCampo7.activate();
      } else {
        comboCampo7.deactivate();
        comboCampo7.selectItem(-1);
        comboCondicion6.selectItem(-1);
        comboCondicion7.selectItem(-1);
        document.forms[0].valor6.value = "";
        document.forms[0].valor7.value = "";
        comboOperador5.selectItem(-1);
        comboOperador6.selectItem(-1);
      }
    }

    comboCampo7.change = function() {
      if(comboCampo7.des.value.length != 0) {

      } else {
        comboCondicion7.selectItem(-1);
        document.forms[0].valor7.value = "";
        comboOperador6.selectItem(-1);
      }
    }

    comboCondicion1.change = function() {
      if(comboCondicion1.des.value == "Nulo") {
        document.forms[0].valor1.value = "Nulo";
        document.forms[0].valor1.readOnly = true;
      } else if(comboCondicion1.des.value == "No Nulo") {
        document.forms[0].valor1.value = "No Nulo";
        document.forms[0].valor1.readOnly = true;
      } else {
        document.forms[0].valor1.readOnly = false;
      }
    }

    comboCondicion2.change = function() {
      if(comboCondicion2.des.value == "Nulo") {
        document.forms[0].valor2.value = "Nulo";
        document.forms[0].valor2.readOnly = true;
      } else if(comboCondicion2.des.value == "No Nulo") {
        document.forms[0].valor2.value = "No Nulo";
        document.forms[0].valor2.readOnly = true;
      } else {
        document.forms[0].valor2.readOnly = false;
      }
    }

    comboCondicion3.change = function() {
      if(comboCondicion3.des.value == "Nulo") {
        document.forms[0].valor3.value = "Nulo";
        document.forms[0].valor3.readOnly = true;
      } else if(comboCondicion3.des.value == "No Nulo") {
        document.forms[0].valor3.value = "No Nulo";
        document.forms[0].valor3.readOnly = true;
      } else {
        document.forms[0].valor3.readOnly = false;
      }
    }

    comboCondicion4.change = function() {
      if(comboCondicion4.des.value == "Nulo") {
        document.forms[0].valor4.value = "Nulo";
        document.forms[0].valor4.readOnly = true;
      } else if(comboCondicion4.des.value == "No Nulo") {
        document.forms[0].valor4.value = "No Nulo";
        document.forms[0].valor4.readOnly = true;
      } else {
        document.forms[0].valor4.readOnly = false;
      }
    }

    comboCondicion5.change = function() {
      if(comboCondicion5.des.value == "Nulo") {
        document.forms[0].valor5.value = "Nulo";
        document.forms[0].valor5.readOnly = true;
      } else if(comboCondicion5.des.value == "No Nulo") {
        document.forms[0].valor5.value = "No Nulo";
        document.forms[0].valor5.readOnly = true;
      } else {
        document.forms[0].valor5.readOnly = false;
      }
    }

    comboCondicion6.change = function() {
      if(comboCondicion6.des.value == "Nulo") {
        document.forms[0].valor6.value = "Nulo";
        document.forms[0].valor6.readOnly = true;
      } else if(comboCondicion6.des.value == "No Nulo") {
        document.forms[0].valor6.value = "No Nulo";
        document.forms[0].valor6.readOnly = true;
      } else {
        document.forms[0].valor6.readOnly = false;
      }
    }

    comboCondicion7.change = function() {
      if(comboCondicion7.des.value == "Nulo") {
        document.forms[0].valor7.value = "Nulo";
        document.forms[0].valor7.readOnly = true;
      } else if(comboCondicion7.des.value == "No Nulo") {
        document.forms[0].valor7.value = "No Nulo";
        document.forms[0].valor7.readOnly = true;
      } else {
        document.forms[0].valor7.readOnly = false;
      }
    }
    </script>
</BODY>
</html:html>
