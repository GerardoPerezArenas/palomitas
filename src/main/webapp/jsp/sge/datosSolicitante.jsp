<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.DatosSolicitanteForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: EXPEDIENTES  Datos del Solicitante:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->




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
var cod_tiposIdInteresado = new Array();
var desc_tiposIdInteresado = new Array();
var cod_provincia = new Array();
var desc_provincia = new Array();
var cod_pais = new Array();
var desc_pais = new Array();

function inicializar() {
  <logic:iterate id="elemento" name="DatosSolicitanteForm" property="listaDepartamentos">
    cod_departamento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
    desc_departamento['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
  </logic:iterate>
  <logic:iterate id="elemento" name="DatosSolicitanteForm" property="listaTiposDocumentos">
    cod_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
    desc_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
  </logic:iterate>
  <%
      DatosSolicitanteForm datosSolForm =(DatosSolicitanteForm)session.getAttribute("DatosSolicitanteForm");
      Vector listaProv = datosSolForm.getListaProvincias();
      Vector listaPais = datosSolForm.getListaPaises();
      int lengthProv = listaProv.size();
      int lengthPais = listaPais.size();
      int i = 0;
    %>
      var j=0;
    <%for(i=0;i<lengthProv;i++){%>
        cod_provincia[j] = '<%=(String)((GeneralValueObject)listaProv.get(i)).getAtributo("codProvincia")%>';
        desc_provincia[j] = '<%=(String)((GeneralValueObject)listaProv.get(i)).getAtributo("descProvincia")%>';
        j++;
    <%}%>
    j=0;
    <%for(i=0;i<lengthPais;i++){%>
        cod_pais[j] = '<%=(String)((GeneralValueObject)listaPais.get(i)).getAtributo("codPais")%>';
        desc_pais[j] = '<%=(String)((GeneralValueObject)listaPais.get(i)).getAtributo("descPais")%>';
        j++;
    <%}%>
}

function pulsarVolver() {
  document.forms[0].opcion.value="inicio";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/ReejecucionEnlaces.do'/>";
  document.forms[0].submit();
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
  condiciones[0]='PRO_DEP'+'зе';
  condiciones[1]= document.forms[0].codDepartamento.value ;
  muestraListaTabla('PRO_COD','PRO_NOM','E_PRO',condiciones,'codProcedimiento','descProcedimiento','botonProcedimiento','100');
 }
}

function onBlurMunicipio(){
  document.forms[0].codMunicipio.value="";
  document.forms[0].descMunicipio.value="";
}

function mostrarListaMunicipios(){
if(!(Trim(document.forms[0].codProvincia.value)=='')){
  var condiciones = new Array();
  condiciones[0]='MUN_PAI'+'зе';
  condiciones[1]= 108 + 'зе';
  condiciones[2]='MUN_PRV'+'зе';
  condiciones[3]= document.forms[0].codProvincia.value+'зе';
  muestraListaTabla('MUN_COD','MUN_NOM','T_MUN',condiciones,'codMunicipio',
                    'descMunicipio','botonMunicipio','60');
}else{
  document.forms[0].codMunicipio.value='';
  document.forms[0].descMunicipio.value='';
}
}

function pulsarESRelacionadas() {
  document.forms[0].opcion.value="inicio";
  document.forms[0].target="mainFrame";
  document.forms[0].action="<c:url value='/sge/RelacionEntradasSalidas.do'/>";
  document.forms[0].submit();
}

</SCRIPT>

</head>

<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<html:form action="/sge/DatosSolicitante.do" target="_self">

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
  <TD>&nbsp;</TD>
</TR>
<tr>
  <TD class="titulo" colspan="4"><%=descriptor.getDescripcion("tit_iniExp")%></TD>
</tr>

<TR>
  <TD>

<TABLE width="100%" cellspacing="7px" cellpadding="1px" style="border-top: #7B9EC0 1px solid; border-bottom: #7B9EC0 1px solid;border-left: #7B9EC0 1px solid;border-right: #7B9EC0 1px solid;">
  <tr>
    <td width="100%" colspan="2">
      <table border="0px" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("res_etiReg")%>:</td>
          <td width="25%" class="columnP">
            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtRegistro" size="20" maxlength="20"
              onkeypress="javascript:PasaAMayusculas(event);"/>
          </td>
          <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("etiq_numExp")%>:</td>
          <td width="25%" class="columnP">
            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtExpediente" size="20" maxlength="20"
              onkeypress="javascript:PasaAMayusculas(event);"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%" colspan="2" align="center">
      <input type= "button" class="boton" value=<%=descriptor.getDescripcion("bESRelac")%> name="cmdESRelac" style="width:120" onclick="pulsarESRelacionadas();">
    </td>
  </tr>
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
  </tr>
  <tr>
    <td width="100%" colspan="2" class="etiqueta" align="center" style="height:7px;background-color:#F7DAB0;"><%=descriptor.getDescripcion("gEtiq_solicit")%></td>
  </tr>
  <tr>
    <td width="100%" colspan="2">
      <table border="0px" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="151px" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_TipoDocu")%>:</td>
          <td width="255px" class="columnP">
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
          </td>
          <td width="110px" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
          <td width="210px" class="columnP">
                <html:text styleClass="inputTextoObligatorio" size="12" maxlength="9" property="txtDNI" />
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_nombre")%>:</td>
    <td width="80%" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtNombre" size="40" maxlength="40"
            onkeypress="javascript:PasaAMayusculas(event);"/>
    </td>
  </tr>
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDomicilio")%>:</td>
    <td width="80%" class="columnP">
          <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtDomicilio" size="40" maxlength="40"
            onkeypress="javascript:PasaAMayusculas(event);"/>
    </td>
  </tr>
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqPais")%>:</td>
    <td width="80%" class="columnP">
      <html:text styleClass="inputTextoObligatorio"  property="codPais" size="3" maxlength="3"
            onkeypress="javascript:PasaAMayusculas(event);"
            onfocus="this.select();"
            onblur="actualizarDescripcion('codPais','descPais',cod_pais,desc_pais);"/>
      <html:text styleClass="inputTextoObligatorio" property="descPais" style="width:300;height:16;" readonly="true"
            onclick="javascript:{divSegundoPlano=false;inicializarValores('codPais','descPais',cod_pais,desc_pais);}"
            onfocus="javascript:{divSegundoPlano=true;inicializarValores('codPais','descPais',cod_pais,desc_pais);}"/>
        <A href="javascript:{divSegundoPlano=false;inicializarValores('codPais','descPais',cod_pais,desc_pais);}" name ="anchorPais" style="text-decoration:none;" id="anclaDD" >
            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="despD" name="botonPais" style="cursor:hand;"></span>
        </A>
    </td>
  </tr>
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
    <td width="80%" class="columnP">
      <html:text styleClass="inputTextoObligatorio"  property="codProvincia" size="3" maxlength="3"
            onkeypress="javascript:PasaAMayusculas(event);"
            onfocus="this.select();"
            onblur="actualizarDescripcion('codProvincia','descProvincia',cod_provincia,desc_provincia);onBlurMunicipio();"/>
      <html:text styleClass="inputTextoObligatorio" property="descProvincia" style="width:300;height:16;" readonly="true"
            onclick="javascript:{divSegundoPlano=false;inicializarValores('codProvincia','descProvincia',cod_provincia,desc_provincia);}"
            onblur="onBlurMunicipio();"
            onfocus="javascript:{divSegundoPlano=true;inicializarValores('codProvincia','descProvincia',cod_provincia,desc_provincia);}"/>
        <A href="javascript:{divSegundoPlano=false;inicializarValores('codProvincia','descProvincia',cod_provincia,desc_provincia);}" name ="anchorProvincia" style="text-decoration:none;" id="anclaDD" >
            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="despD" name="botonProvincia" style="cursor:hand;"></span>
        </A>
    </td>
  </tr>
  <tr>
    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</td>
    <td width="80%" class="columnP">
      <html:text styleClass="inputTextoObligatorio"  property="codMunicipio" size="3" maxlength="3"
            onkeypress="javascript:PasaAMayusculas(event);"
            onfocus="this.select();"/>
      <html:text styleClass="inputTextoObligatorio" property="descMunicipio" style="width:300;height:16;" readonly="true"
            onclick="javascript:{divSegundoPlano=false;mostrarListaMunicipios();}"
            onfocus="javascript:{divSegundoPlano=true;mostrarListaMunicipios();}"/>
        <A href="javascript:{divSegundoPlano=false;mostrarListaMunicipios();}" name ="anchorMunicipio" style="text-decoration:none;" id="anclaDD" >
            <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="despD" name="botonMunicipio" style="cursor:hand;"></span>
        </A>
    </td>
  </tr>
  <tr>
    <td width="100%" colspan="2">
      <table border="0px" width="100%" cellspacing="0" cellpadding="0">
        <tr>
          <td width="151px" class="etiqueta"><%=descriptor.getDescripcion("gEtiqCodPostal")%>:</td>
          <td width="150px" class="columnP">
            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtCodigoPostal" size="15" maxlength="15"
              onkeypress="javascript:PasaAMayusculas(event);"/>
          </td>
          <td width="100px" class="etiqueta"><%=descriptor.getDescripcion("gEtiqTelef")%>:</td>
          <td width="325px" class="columnP">
            <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="txtTelefono" size="15" maxlength="15"
              onkeypress="javascript:PasaAMayusculas(event);"/>
          </td>
        </tr>
      </table>
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
    <TD  width="33%" align="center">
      <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbGrabar")%> name="cmdGrabar" onclick="pulsarGrabar();" accesskey="G">
    </TD>
    <TD width="34%" align="center">
      <input type= "button" class="boton" value=<%=descriptor.getDescripcion("gbRestablecer")%> name="cmdRestablecer"  onclick="pulsarRestablecer();" accesskey="R">
    </TD>
    <TD width="33%" align="center">
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

    keyDel();

  if (teclaAuxiliar == 9){
      if(layerVisible) ocultarDiv();
    if(divSegundoPlano) divSegundoPlano = false;
      return false;
    }
    if (teclaAuxiliar == 1){
      if(layerVisible) setTimeout("ocultarDiv()",30);
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
        callFromTableTo(tab.selectedIndex,tab.id);
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
