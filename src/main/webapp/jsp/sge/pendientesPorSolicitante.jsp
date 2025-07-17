<%@page contentType="text/html; charset=iso-8859-1"	language="java" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<%
  String funcion = "";
  int idioma=1;
  int apl=1;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
        }
  }
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <TITLE>::: EXPEDIENTES  Expedientes pendientes por solicitante :::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    
    
        
    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaBusquedaTerceros.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>

    <SCRIPT language="JavaScript">
    var cod_tiposIdInteresado = new Array();
    var desc_tiposIdInteresado = new Array();

    function inicializar() {
      activarFormulario();
      <logic:iterate id="elemento" name="PendientesPorSolicitanteForm" property="listaTiposDocumentos">
        cod_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
      </logic:iterate>
    }

    function pulsarSalir() {
       window.location = "<c:url value='/jsp/sge/presentacionExped.jsp'/>";
    }

    function buscarDocTipoDoc() {
      if (!documentoNoValido("codTipoDoc","txtDNI",1)){
        list=new Array();
        tab.lineas=list;
        refresca();
        document.forms[0].txtDNI.value=document.forms[0].txtDNI.value.toUpperCase();
        document.forms[0].opcion.value="buscarTercero";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/PendientesPorSolicitante.do'/>";
        document.forms[0].submit();
      }
    }

    function pulsarBuscarTerceros(){
      var argumentos = new Array();
      argumentos =[new Array(),"SGE"];
      var source = "<c:url value='/BusquedaTerceros.do?opcion=inicializarBusquedaTerc&ventana=true'/>";
      abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/mainVentana.jsp?source='+source,argumentos,
	'width=700,height=500,status='+ '<%=statusBar%>',function(datos){
                        if(datos.length>0){
                          document.forms[0].txtNombre.value = datos[6];
                          document.forms[0].codTipoDoc.value = datos[4];
                          document.forms[0].txtDNI.value = datos[5];
                          mostrarDescripcionTipoDoc();
                          document.forms[0].identificadorTerc.value = datos[2];
                          document.forms[0].version.value = datos[3];
                        }
                  });
    }

    function mostrarDescripcionTipoDoc(){
      if (!(Trim(document.forms[0].codTipoDoc.value) == '')) {
        actualizarDescripcion('codTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);
      }
    }

    function recuperaNombreTercero(datos) {
      document.forms[0].txtNombre.value = datos[1];
      document.forms[0].version.value = datos[2];
      document.forms[0].identificadorTerc.value = datos[3];
    }

    function pulsarConsultar() {
      if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
        document.forms[0].opcion.value="consultarP";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/PendientesPorSolicitante.do'/>";
        document.forms[0].submit();
      }
    }

    function recuperaConsulta(list) {
      tab.lineas=list;
      refresca();
      lista = list;
    }

    function desactivarFormulario() {
      deshabilitarDatos(document.forms[0]);
      var vectorBoton = new Array(document.forms[0].botonTipoDoc);
      deshabilitarImagenBotonGeneral(vectorBoton, true);
      var vectorAnchor = new Array(document.getElementsByClassName("fa-chevron-circle-down"));
      deshabilitarImagen(vectorAnchor,true);
      vectorImg = new Array(document.forms[0].botonT);
      deshabilitarImagen(vectorImg, true);
    }

    function activarFormulario() {
      habilitarDatos(document.forms[0]);
      var vectorBoton = new Array(document.forms[0].botonTipoDoc);
      habilitarImagenBotonGeneral(vectorBoton, true);
      var vectorAnchor = new Array(document.getElementsByClassName("fa-chevron-circle-down"));
      deshabilitarImagen(vectorAnchor,false);
      vectorImg = new Array(document.forms[0].botonT);
      habilitarImagen(vectorImg, true);
    }

    function actualizarDescripcion(campoCodigo, campoDescripcion, listaCodigos, listaDescripciones, bConsulta) {
      cargarDesc(campoCodigo,campoDescripcion, listaCodigos, listaDescripciones);
    }

    function borrarDatos() {
      list=new Array();
      tab.lineas=list;
      refresca();
      document.forms[0].codTipoDoc.value = "";
      document.forms[0].descTipoDoc.value = "";
      document.forms[0].txtDNI.value = "";
      document.forms[0].txtNombre.value = "";
      document.forms[0].txtRegistros.value = "";
    }
    </SCRIPT>
</head>

<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <html:form action="/sge/PendientesPorSolicitante.do" target="_self">

    <html:hidden  property="opcion" value=""/>
    <html:hidden  property="version" value=""/>
    <html:hidden  property="identificadorTerc" value=""/>

    <CENTER>
    <TABLE id ="tabla1" class="tablaP" width="730px" cellspacing="0" cellpadding="0">
    <TR>
      <TD>&nbsp;</TD>
    </TR>
    <tr>
      <TD class="titulo" colspan="2"><%=descriptor.getDescripcion("tit_pendExpSol")%></TD>
    </tr>

    <TR>
      <TD>

    <TABLE width="100%" cellspacing="4px" cellpadding="1px" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
      <tr>
        <TD width="100%" colspan="2">
          <TABLE width="100%" cellspacing="0px" cellpadding="0px">
            <TR>
              <TD width="124px" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_TipoDocu")%>:</TD>
              <TD width="270px" valign="top" class="columnP">
                <html:text styleId="obligatorio" property="codTipoDoc" styleClass="inputTextoObligatorio" size="3"
                    onfocus="javascript:this.select();"
                    onchange="javascript:{inicializarValores('codTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);}"
                    onblur="actualizarDescripcion('codTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);"/>
               <html:text styleClass="inputTextoObligatorio" styleId="obligatorio" property="descTipoDoc" style="width:158;height:17" readonly="true"
                    onfocus="javascript:{divSegundoPlano=true;	inicializarValores('codTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);}"
                    onclick="javascript:{divSegundoPlano=false;	inicializarValores('codTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);}"/>
                <A href="javascript:{divSegundoPlano=false;	inicializarValores('codTipoDoc','descTipoDoc',cod_tiposIdInteresado,desc_tiposIdInteresado);}" style="text-decoration:none;" id="anclaD" name="anchorTipoDoc"
                    onclick="javascript:this.focus();">
                  <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonTipoDoc" style="cursor:hand;"></span>
                </A>
              <TD width="90px" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</TD>
              <TD width="90px" class="columnP">
                <html:text styleClass="inputTextoObligatorio" size="12" maxlength="9" property="txtDNI" onchange="javascript:buscarDocTipoDoc();"/>
              </TD>
              <TD width="60px" class="columnP" align="left">
                <span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Interesado"
                   onclick="javascript:pulsarBuscarTerceros();"></span>
              </TD>
            </TR>
          </TABLE>
        </TD>
      </tr>
      <tr>
        <TD width="19%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqNombre")%>:</TD>
        <TD width="81%" valign="top" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtNombre" size="30" maxlength="30"
            onkeypress="javascript:PasaAMayusculas(event);"/>
        </TD>
      </tr>
      <tr>
        <TD width="19%" class="etiqueta"><%=descriptor.getDescripcion("res_etiqReg")%>:</TD>
        <TD width="81%" class="columnP">
          <html:text styleClass="inputTexto" property="txtRegistros" size="6" maxlength="5"
            onkeypress="javascript:PasaAMayusculas(event);"/>
        </TD>
      </tr>
      <tr>
        <TD width="100%" height="5px" colspan="2"></TD>
      </tr>
      <TR>
        <TD class="etiqueta" colspan="2" style="height:6px;background-color:#F7DAB0;" align="center"> <%=descriptor.getDescripcion("res_etiqListSolic")%> </TD>
      </TR>
      <tr>
        <TD width="100%" class="columnP" colspan="4">
          <TABLE width="100%" cellspacing="7px" cellpadding="1px">
            <TR>
              <TD>
                <table width="100%" rules="cols" bordercolor="#7B9EC0" border="1" cellspacing="0" cellpadding="0" class="fondoCab">
                  <tr height="15">
                    <td width="18%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("etiq_numRex")%></td>
                    <td width="18%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("etiq_numExp")%></td>
                    <td width="18%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("gEtiqProc")%></td>
                    <td width="15%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("gEtiq_fecIni")%></td>
                    <td width="31%" bgcolor="#7B9EC0" class="txtverdebold" align="center"><%= descriptor.getDescripcion("etiq_tramEnc")%></td>
                  </tr>
                  <tr>
                    <td colspan="5">
                      <div id="tabla" class="text" style="HEIGHT:200px; WIDTH: 700px; overflow-y: auto; overflow-x: no; visibility: visible; BORDER: 0px">
                      </div>
                    </td>
                  </tr>
                </table>
              </TD>
            </TR>
          </TABLE>
        </TD>
      </tr>
    </TABLE>

      </TD>
    </TR>
    <TR>
      <TD>

    &nbsp;
    &nbsp;

    <TABLE class="tablaBotones" width="100%" border="0">
      <TR>
        <TD  width="50%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbConsultar")%> name="cmdConsultar" onclick="pulsarConsultar();" accesskey="C">
        </TD>
        <TD width="50%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onclick="pulsarSalir();" accesskey="S">
        </TD>
      </TR>
    </TABLE>

      </TD>
    </TR>
    </TABLE>

    </center>
    </html:form>

    <script language="JavaScript1.2">
    tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'),150);

    tab.addColumna('110','center');
    tab.addColumna('120','center');
    tab.addColumna('120','center');
    tab.addColumna('105','center');
    tab.addColumna('205','center');

    function refresca() {
      tab.displayTabla();
    }

    tab.displayDatos = pintaDatos;

    function pintaDatos() {
      var selRow = eval("document.all." + tab.id + "_Row" + tab.selectedIndex);
      selRow.bgColor = TB_Fondo;
    }

    document.onmouseup = checkKeys;

    function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }

      if('Alt+S'==tecla) {
        pulsarSalir();
      } else if('Alt+C'==tecla) {
        pulsarConsultar();
      }

      if((layerVisible)||(divSegundoPlano)) buscar();
      keyDel(evento);
      if (teclaAuxiliar == 9){
          if(layerVisible) ocultarDiv();
        if(divSegundoPlano) divSegundoPlano = false;
          return false;
        }
        if (teclaAuxiliar == 1){
            if(layerVisible) setTimeout('ocultarDivNoFocus()',40);
            if(divSegundoPlano)	divSegundoPlano = false;
        }

        if (teclaAuxiliar == 40){
          if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
          return false;
        }
        if (teclaAuxiliar == 38){
          if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
          return false;
        }

        if(teclaAuxiliar == 13){
          if((tab.selectedIndex>-1)&&(tab.selectedIndex < lista.length)&&(!ultimo)){
            if(document.forms[0].cmdNuevoTram.disabled == false) {
              callFromTableTo(tab.selectedIndex,tab.id);
            }
          }
        }

      if ((evento.button == 1)||(evento.button == 2)){
          if(layerVisible) setTimeout("ocultarDiv()",30);
        if(divSegundoPlano)	divSegundoPlano = false;
        }
    }
    </script>


    <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
 </BODY>

</html:html>
