<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

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
    <TITLE>::: EXPEDIENTES  Definición de Tramitación Externa:::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

        
    
    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>

    <SCRIPT language="JavaScript">
    var cod_departamento = new Array();
    var desc_departamento = new Array();

    function inicializar() {
      <logic:iterate id="elemento" name="DefinicionTramitacionExternaForm" property="listaDepartamentos">
        cod_departamento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_departamento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
      </logic:iterate>
    }

    function pulsarSalir() {
       window.location = "<c:url value='/jsp/sge/presentacionExped.jsp'/>";
    }

    function onBlurDepartamento(){
      document.forms[0].codProcedimiento.value='';
      document.forms[0].descProcedimiento.value='';
    }

    function mostrarListaProcedimientos(){
     if ( Trim(document.forms[0].codDepartamento.value) != '') {
      var condiciones = new Array();
      condiciones[0]='PRO_DEP'+'§¥';
      condiciones[1]= document.forms[0].codDepartamento.value ;
      muestraListaTabla('PRO_COD','PRO_NOM','E_PRO',condiciones,'codProcedimiento','descProcedimiento','botonProcedimiento','100');
     }
    }

    function actualizarDescripcion(campoCodigo, campoDescripcion, listaCodigos, listaDescripciones, bConsulta) {
      cargarDesc(campoCodigo,campoDescripcion, listaCodigos, listaDescripciones);
    }

    function pulsarLimpiar() {
      document.forms[0].codDepartamento.value="";
      document.forms[0].descDepartamento.value="";
      document.forms[0].codProcedimiento.value="";
      document.forms[0].descProcedimiento.value="";
      document.forms[0].nomTramiteExterno.value="";
      document.forms[0].codTramiteExterno.value="";
      document.forms[0].tramiteRelacionado.value="";
      document.forms[0].tipoAbrir[0].checked = true;
    }

    </SCRIPT>
</head>

<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <html:form action="/sge/DefinicionTramitacionExterna.do" target="_self">

    <html:hidden  property="tipo_select"   value=""/>
    <html:hidden  property="col_cod"   value=""/>
    <html:hidden  property="col_desc"   value=""/>
    <html:hidden  property="nom_tabla"   value=""/>
    <html:hidden  property="input_cod"   value=""/>
    <html:hidden  property="input_desc"   value=""/>
    <html:hidden  property="column_valor_where" value=""/>
    <html:hidden  property="whereComplejo" value=""/>
    <html:hidden  property="opcion" value=""/>

    <CENTER>

    <TABLE id ="tabla1" class="tablaP" width="730px" cellspacing="0" cellpadding="0">
    <TR>
      <TD>&nbsp;</TD>
    </TR>
    <tr>
      <TD class="titulo" colspan="4"><%=descriptor.getDescripcion("tit_defTramExt")%></TD>
    </tr>

    <TR>
      <TD>

    <TABLE width="100%" cellspacing="7px" cellpadding="1px" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
      <tr>
        <TD width="25%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDepto")%>:</TD>
        <TD width="75%" valign="top" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codDepartamento" size="5" maxlength="3"
                onkeypress="javascript:PasaAMayusculas(event);"
                onfocus="this.select();"
                onblur="actualizarDescripcion('codDepartamento','descDepartamento',cod_departamento,desc_departamento);onBlurDepartamento();"/>
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descDepartamento" style="width:284;height:17" readonly="true"
                onclick="javascript:{divSegundoPlano=false;inicializarValores('codDepartamento', 'descDepartamento',cod_departamento,desc_departamento);}"
                onblur="onBlurDepartamento();"
                onfocus="javascript:{divSegundoPlano=true;inicializarValores('codDepartamento', 'descDepartamento',cod_departamento,desc_departamento);}"/>
            <A href="javascript:{divSegundoPlano=false;inicializarValores('codDepartamento','descDepartamento',cod_departamento,desc_departamento);}" name ="anchorDepartamento" style="text-decoration:none;" id="anclaDD" >
                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="despD" name="botonDepartamento" style="cursor:hand;"></span>
            </A>
        </TD>
      </tr>
      <tr>
        <TD width="25%" class="etiqueta"><%=descriptor.getDescripcion("res_etiqProc")%>:</TD>
        <TD width="75%" valign="top" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codProcedimiento" size="5"
                onkeypress="javascript:PasaAMayusculas(event);"
                onfocus="this.select();"/>
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descProcedimiento" style="width:284;height:17" readonly="true"
                onclick="javascript:{divSegundoPlano=false;mostrarListaProcedimientos();}"
                onfocus="javascript:{divSegundoPlano=true;mostrarListaProcedimientos();}"/>
            <A href="javascript:{divSegundoPlano=false;mostrarListaProcedimientos();}" name ="anchorProcedimiento" style="text-decoration:none;" id="anclaDD" >
                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonProcedimiento" style="cursor:hand;"></span>
            </A>
        </TD>
      </tr>
      <tr>
        <td width="100%" colspan="2" class="etiqueta" valign="top"><%=descriptor.getDescripcion("gEtiq_SelTraExt")%>:&nbsp;&nbsp;&nbsp;&nbsp;
          <span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Trámites" style="cursor:hand;"
                   onclick="javascript:pulsarBuscarTramites();"></span>
        </td>
      </tr>
      <tr>
        <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_NomTraExt")%>:</td>
        <td width="75%" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="nomTramiteExterno" size="40" maxlength="40" readonly="true"/>
        </td>
      </tr>
      <tr>
        <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_CodTraExt")%>:</td>
        <td width="75%" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="codTramiteExterno" size="20" maxlength="20" readonly="true"/>
        </td>
      </tr>
      <tr>
        <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_TraRela")%>:</td>
        <td width="75%" class="columnP" valign="top">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="tramiteRelacionado" size="20" maxlength="20"
            onkeypress="javascript:PasaAMayusculas(event);"/>
          <span class="fa fa-search" aria-hidden="true"  name="botonT" alt="Buscar Trámite Relacionado" style="cursor:hand;"
            onclick="javascript:busquedaTramiteRelacionado();"></span>
        </td>
      </tr>
      <tr>
        <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_AbrirEn")%>:</td>
        <td width="75%" class="etiqueta">
          <input type="radio" name="tipoAbrir" class="textoSuelto" value="nueva" CHECKED>
          <%=descriptor.getDescripcion("gEtiq_NueVent")%>
          <input type="radio" name="tipoAbrir" class="textoSuelto" value="misma">
          <%=descriptor.getDescripcion("gEtiq_MisVent")%>
        </td>
      </tr>


    </table>

      </TD>
    </TR>
    <TR>
      <TD>

    &nbsp;
    &nbsp;

    <TABLE class="tablaBotones" width="100%">
      <TR>
        <TD  width="20%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbAlta")%> name="cmdAlta" onclick="pulsarAlta();">
        </TD>
        <TD width="20%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbModificar")%> name="cmdModificar"  onclick="pulsarModificar();">
        </TD>
        <TD width="20%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbBaja")%> name="cmdBaja" onclick="pulsarBaja();">
        </TD>
        <TD width="20%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbLimpiar")%> name="cmdLimpiar"  onclick="pulsarLimpiar();">
        </TD>
        <TD width="20%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir"  onclick="pulsarSalir();">
        </TD>
      </TR>
    </TABLE>

      </TD>
    </TR>
    </TABLE>

    </center>
    </html:form>



    <script language="JavaScript1.2">


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

        if (teclaAuxiliar== 40){
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
