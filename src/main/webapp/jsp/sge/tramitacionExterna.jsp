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
    <TITLE>::: EXPEDIENTES  Tramitación Externa:::</TITLE>
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
    var cod_tramiteExterno = new Array();
    var desc_tramiteExterno = new Array();

    function inicializar() {
      <logic:iterate id="elemento" name="TramitacionExternaForm" property="listaDepartamentos">
        cod_departamento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        desc_departamento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
      </logic:iterate>
    }

    function pulsarVolver() {
    }

    function actualizarDescripcion(campoCodigo, campoDescripcion, listaCodigos, listaDescripciones, bConsulta) {
      cargarDesc(campoCodigo,campoDescripcion, listaCodigos, listaDescripciones);
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
    </SCRIPT>
 </head>

 <BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

    <html:form action="/sge/TramitacionExterna.do" target="_self">

    <html:hidden  property="opcion" value=""/>
    <html:hidden  property="tipo_select"   value=""/>
    <html:hidden  property="col_cod"   value=""/>
    <html:hidden  property="col_desc"   value=""/>
    <html:hidden  property="nom_tabla"   value=""/>
    <html:hidden  property="input_cod"   value=""/>
    <html:hidden  property="input_desc"   value=""/>
    <html:hidden  property="column_valor_where" value=""/>
    <html:hidden  property="whereComplejo" value=""/>

    <CENTER>
    <TABLE id ="tabla1" class="tablaP" width="730px" cellspacing="0" cellpadding="0">
    <TR>
      <TD height="75px">&nbsp;</TD>
    </TR>
    <tr>
      <TD class="titulo" colspan="4"><%=descriptor.getDescripcion("tit_consTramExt")%></TD>
    </tr>

    <TR>
      <TD>

    <TABLE width="100%" cellspacing="7px" cellpadding="1px" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
      <tr>
        <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDepto")%>:</td>
        <td width="80%" class="columnP" valign="top">
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
        </td>
      </tr>
      <tr>
        <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("res_etiqProc")%>:</td>
        <td width="80%" valign="top" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codProcedimiento" size="5"
                onkeypress="javascript:PasaAMayusculas(event);"
                onfocus="this.select();"/>
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descProcedimiento" style="width:284;height:17" readonly="true"
                onclick="javascript:{divSegundoPlano=false;mostrarListaProcedimientos();}"
                onfocus="javascript:{divSegundoPlano=true;mostrarListaProcedimientos();}"/>
            <A href="javascript:{divSegundoPlano=false;mostrarListaProcedimientos();}" name ="anchorProcedimiento" style="text-decoration:none;" id="anclaDD" >
                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonProcedimiento" style="cursor:hand;"></span>
            </A>
        </td>
      </tr>
      <tr>
        <td width="100%" colspan="2" class="etiqueta" align="center" style="height:7px;background-color:#F7DAB0;"><%=descriptor.getDescripcion("etiq_tramExt")%></td>
      </tr>
      <tr>
        <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_tramExt")%>:</td>
        <td width="80%" class="columnP" valign="top">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codTramiteExterno" size="5" maxlength="3"
                onkeypress="javascript:PasaAMayusculas(event);"
                onfocus="this.select();"
                onblur="actualizarDescripcion('codTramiteExterno','descTramiteExterno',cod_tramiteExterno,desc_tramiteExterno);"/>
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descTramiteExterno" style="width:284;height:17" readonly="true"
                onclick="javascript:{divSegundoPlano=false;inicializarValores('codTramiteExterno','descTramiteExterno',cod_tramiteExterno,desc_tramiteExterno);}"
                onfocus="javascript:{divSegundoPlano=true;inicializarValores('codTramiteExterno','descTramiteExterno',cod_tramiteExterno,desc_tramiteExterno);}"/>
            <A href="javascript:{divSegundoPlano=false;inicializarValores('codTramiteExterno','descTramiteExterno',cod_tramiteExterno,desc_tramiteExterno);}" name ="anchorTramiteExterno" style="text-decoration:none;" id="anclaDD" >
                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="despD" name="botonTramiteExterno" style="cursor:hand;"></span>
            </A>
        </td>
      </tr>

    </table>

      </TD>
    </TR>
    <TR>
      <TD>
      &nbsp;&nbsp;
    <TABLE class="tablaBotones" width="100%">
      <TR>
        <TD  width="50%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();" accesskey="A">
        </TD>
        <TD width="50%" align="center">
          <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbVolver")%> name="cmdVolver"  onclick="pulsarVolver();" accesskey="V">
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
        }if (teclaAuxiliar == 1){
        if(layerVisible) setTimeout("ocultarDiv()",30);
          if(divSegundoPlano) divSegundoPlano = false;
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
            callFromTableTo(tab.selectedIndex,tab.id);
          }
        }

      if ((evento.button == 1)||(evento.button == 2)){
          if(layerVisible) setTimeout("ocultarDiv()",30);
          if(divSegundoPlano) divSegundoPlano = false;
        }
    }
    </script>


    <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
 </BODY>
</html:html>
