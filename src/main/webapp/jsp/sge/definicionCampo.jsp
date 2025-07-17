<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="java.text.MessageFormat"%>

<html:html>
<head>
<jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: EXPEDIENTES  Definición de Campos:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
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
  String municipio = Integer.toString(munic);
  String aplicacion = Integer.toString(apl);
  String modoInicio = "";
  if (session.getAttribute("modoInicio") != null) {
    modoInicio = (String) session.getAttribute("modoInicio");
    session.removeAttribute("modoInicio");
  }
  String lectura = "";
  if (session.getAttribute("lectura") != null) {
    lectura = (String) session.getAttribute("lectura");
    session.removeAttribute("lectura");
  }
  
  Config m_Config = ConfigServiceHelper.getConfig("common");
  String statusBar = m_Config.getString("JSP.StatusBar");  
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/JavaScriptUtil.js'/>"></script>
<SCRIPT type="text/javascript"> 
var cod_campoDesplegable = new Array();
var desc_campoDesplegable = new Array();
var  tam_campoDesplegable= new Array();
var cod_plantilla = new Array();
var desc_plantilla = new Array();
var cod_tipoDato = new Array();
var desc_tipoDato = new Array();
var cod_tD = new Array();
var cod_p = new Array();
var cod_rTD = new Array();
var cod_rM = new Array();
var cod_agrupacion = new Array();
var desc_agrupacion = new Array();

function habilitarPlazoAlarma(){
    document.forms[0].plazoFecha.disabled = false;
    document.forms[0].checkPlazoFecha.disabled = false;    
    document.getElementById("filaPlazo").style.display="";        
}

function deshabilitarPlazoAlarma(){
    document.forms[0].plazoFecha.disabled = true;
    document.forms[0].checkPlazoFecha.disabled = true;    
    document.getElementById("filaPlazo").style.display="none";     

    document.forms[0].plazoFecha.value = "";
    document.forms[0].checkPlazoFecha[0].checked=false;
    document.forms[0].checkPlazoFecha[1].checked=false;
    document.forms[0].checkPlazoFecha[2].checked=false;
}

function cargarComboAgrupaciones(cod_agrupacion, desc_agrupacion){
        //Anhadimos el grupo por defecto
        var listaCodAgrupacion = new Array();
        var listaDescAgrupacion = new Array();

        listaCodAgrupacion.push("DEF");
        listaDescAgrupacion.push('<%=descriptor.getDescripcion("getiq_grupodef")%>');

        for(var i=0; i<cod_agrupacion.length; i++){
            listaCodAgrupacion.push(cod_agrupacion[i]);
            listaDescAgrupacion.push(desc_agrupacion[i]);
        }//for(var i=0; i<cod_agrupacion.length; i++)

        //comboAgrupacion.addItems(cod_agrupacion, desc_agrupacion);
        comboAgrupacion.addItems(listaCodAgrupacion, listaDescAgrupacion);
        comboAgrupacion.activate();
    }//cargarComboExpedientesRelacionados

    function desactivarComboAgrupaciones(){
        comboAgrupacion.deactivate();
    }//cargarComboAgrupaciones

function inicializar() { 
    var o = '<bean:write name="DefinicionCampoForm" property="obligat"/>';
    var v = '<bean:write name="DefinicionCampoForm" property="visible"/>';
    var oculto = '<bean:write name="DefinicionCampoForm" property="oculto"/>';
    var bloqueado = '<bean:write name="DefinicionCampoForm" property="bloqueado"/>';  
    var checkPlazoFecha = '<bean:write name="DefinicionCampoForm" property="checkPlazoFecha"/>';
    var cargaCampo="<%=request.getParameter("cargaCampo")%>";
    var plantilla='<bean:write name="DefinicionCampoForm" property="codPlantilla"/>';
    document.forms[0].desdeProcedimiento.value="<%=request.getParameter("desdeProcedimiento")%>";
    // Realizamos el siguiente replace para evitar que javascript nos reemplace automaticamente los signos + por espacios en blanco.
    document.forms[0].validacion.value = replaceAll(document.forms[0].validacion.value,";SUMA;","+");   
    document.forms[0].operacion.value =  replaceAll(document.forms[0].operacion.value,";SUMA;","+"); 

    operacion_rep = document.forms[0].operacion.value ;
    operacion_rep = replaceAll(operacion_rep," DIAS ",";DIAS;");
    operacion_rep = replaceAll(operacion_rep," MESES ",";MESES;");
    operacion_rep = replaceAll(operacion_rep," ANOS ",";ANOS;");
    document.forms[0].operacion.value = replaceAll(document.forms[0].operacion.value,";DIAS;"," DIAS ");
    document.forms[0].operacion.value = replaceAll(document.forms[0].operacion.value,";MESES;"," MESES ");
    document.forms[0].operacion.value = replaceAll(document.forms[0].operacion.value,";ANOS;"," ANOS "); 

    if (cargaCampo=="si") {       
        var retorno = new Array();
        var masc = "";
        if (document.forms[0].desdeProcedimiento.value=="no") {
            retorno = [document.forms[0].codCampo.value,
                        document.forms[0].descCampo.value,
                        document.forms[0].codPlantilla.value,
                        document.forms[0].codTipoDato.value,
                        document.forms[0].tamano.value,
                        masc,
                        o,
                        0,
                        document.forms[0].descPlantilla.value,
                        document.forms[0].descTipoDato.value,
                        document.forms[0].rotulo.value,
                        v,
                        oculto,
                        bloqueado,
                        document.forms[0].plazoFecha.value,
                        checkPlazoFecha,
                        document.forms[0].validacion.value ,
                        operacion_rep,
                        document.forms[0].codAgrupacion.value];
        } else {
            retorno = [document.forms[0].codCampo.value,
                        document.forms[0].descCampo.value,
                        document.forms[0].codPlantilla.value,
                        document.forms[0].codTipoDato.value,
                        document.forms[0].tamano.value,
                        masc,
                        0,
                        0,
                        document.forms[0].descPlantilla.value,
                        document.forms[0].descTipoDato.value,
                        document.forms[0].rotulo.value,
                        oculto,
                        bloqueado,
                        document.forms[0].plazoFecha.value,
                        checkPlazoFecha,
                        document.forms[0].validacion.value,
                        operacion_rep,
                        document.forms[0].codAgrupacion.value];
        }    
        self.parent.opener.retornoXanelaAuxiliar(retorno);
    } else {
        mostrarCapasBotones('capaBotones1');
        <% if("altaDesdeTramite".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) { %>
        if(o == 1) {
            document.forms[0].obligat[1].checked=true;
        } else {
            document.forms[0].obligat[0].checked=true;
        }
        if(v == 'S') {
            document.forms[0].visible[1].checked=true;
        } else {
            document.forms[0].visible[0].checked=true;
        }
        <% } %>

        if(oculto== 'SI') {
            document.forms[0].oculto[1].checked=true;
        } else {
            document.forms[0].oculto[0].checked=true;
        }
        if(bloqueado== 'SI') {
            document.forms[0].bloqueado[1].checked=true;
        } else {
            document.forms[0].bloqueado[0].checked=true;
        }

        if(checkPlazoFecha== 'D') {
            document.forms[0].checkPlazoFecha[0].checked=true;
        } else {
            if (checkPlazoFecha== 'M') {
                document.forms[0].checkPlazoFecha[1].checked=true;
            } else 
            if(checkPlazoFecha=="A"){ 
                document.forms[0].checkPlazoFecha[2].checked=true;
            }
        }

        <logic:iterate id="elemento" name="DefinicionCampoForm" property="listaCamposDesplegables">
            cod_campoDesplegable['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
            desc_campoDesplegable['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
            tam_campoDesplegable['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="tamano"/>';
        </logic:iterate>
        <logic:iterate id="elemento" name="DefinicionCampoForm" property="listaPlantillas">
            cod_plantilla['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
            desc_plantilla['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        </logic:iterate>
        <logic:iterate id="elemento" name="DefinicionCampoForm" property="listaTipoDato">
            cod_tipoDato['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
            desc_tipoDato['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        </logic:iterate>
        <logic:iterate id="elemento" name="DefinicionCampoForm" property="listaRelacionTipoDatoPlantillas">
            cod_tD['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
            cod_p['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        </logic:iterate>
        <logic:iterate id="elemento" name="DefinicionCampoForm" property="listaRelacionTipoDatoMascaras">
            cod_rTD['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
            cod_rM['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        </logic:iterate>    
        var cont = 0;
        <logic:iterate id="elemento" name="DefinicionCampoForm" property="listaAgrupaciones">
            cod_agrupacion[cont]='<bean:write name="elemento" property="codAgrupacion"/>';
            desc_agrupacion[cont]='<bean:write name="elemento" property="descAgrupacion"/>';
            cont++;
        </logic:iterate>

        comboPlantilla.addItems(cod_plantilla,desc_plantilla); 
        comboTipoDato.addItems(cod_tipoDato,desc_tipoDato);
        cargarComboAgrupaciones(cod_agrupacion,desc_agrupacion);

        if(document.forms[0].codTipoDato.value==""){        
            deshabilitarPlazoAlarma();
        }

        if (document.forms[0].codTipoDato.value!=2) comboPlantilla.deactivate();
        if (document.forms[0].codTipoDato.value!=2)
        if (document.forms[0].codTipoDato.value==1
        || document.forms[0].codTipoDato.value==2
        || document.forms[0].codTipoDato.value==4
        || document.forms[0].codTipoDato.value==8) document.forms[0].tamano.disabled=false;
        else document.forms[0].tamano.disabled=true;
        <%if ("modificarDesdeProcedimiento".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) {%>
            document.forms[0].codCampo.disabled = true;
            comboTipoDato.deactivate();
            if(document.forms[0].codAgrupacion.value != null && document.forms[0].codAgrupacion.value != ""){
                comboAgrupacion.buscaCodigo(document.forms[0].codAgrupacion.value);
            }//if(document.forms[0].codAgrupacion.value != null && document.forms[0].codAgrupacion.value != "")
        <%}%>
        <% if("si".equals(lectura)) { %>
            mostrarCapasBotones('capaBotones2');
            desactivarFormulario();
            var botonSalir = [document.forms[0].cmdSalir2];
            habilitarGeneral(botonSalir);
        <% } %>
    }
    if (document.forms[0].codTipoDato.value!="") {
        var aux = document.forms[0].codPlantilla.value;
        rellenarPlantilla();
        comboPlantilla.buscaLinea(aux);
    }
    else
        {
        mostrarCapaValidacion(false);       
        mostrarCapaOperacion(false); 
        }
}

function desactivarFormulario() {
  deshabilitarDatos(document.forms[0]);
  comboPlantilla.deactivate();
  comboTipoDato.deactivate();
  desactivarComboAgrupaciones();
  <% if("altaDesdeTramite".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) { %>
  document.forms[0].obligat[0].disabled = true;
  document.forms[0].obligat[1].disabled = true;
  document.forms[0].visible[0].disabled = true;
  document.forms[0].visible[1].disabled = true;
<% } %>
}

function pulsarSalir() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function pulsarAceptar () { 
  if( validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
    <% if("altaDesdeTramite".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) { %>
        document.forms[0].desdeProcedimiento.value="no";
    <% } %>
    comboPlantilla.activate();
    document.forms[0].codCampo.disabled = false;
    comboTipoDato.activate();
    document.forms[0].tamano.disabled=false;
    document.forms[0].target="oculto";
    document.forms[0].opcion.value="validarTamano";
    document.forms[0].action="<c:url value='/sge/DefinicionCampo.do'/>";
    document.forms[0].submit();
    }
}
function pulsarVer (){  
    var sql = document.forms[0].validacion.value    
    sql = replaceAll(sql,"+",";SUMA;");
    
    var opcion_pr = "";    
    var source = "";
    var datos = "";
    var codMunicipio = "<%=request.getParameter("codMunicipio")%>";
    var tramite =      "<%=request.getParameter("codTramite")%>";
    var tipo_campo = document.forms[0].codTipoDato.value;
    var codProcedimiento = "<%=request.getParameter("codProcedimiento")%>";    
    
    <% if("altaDesdeTramite".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) { %>
            opcion_pr = "TRAM"
    <%}else{%>
            opcion_pr = "PROC"
    <%}%>
        
    source = "<c:url value='/sge/DefinicionCampo.do?opcion=VerValida'/>";    

    source =source = source + "&codMunicipio=" + codMunicipio + "&codProcedimiento=" + codProcedimiento + "&tipo_campo=" + tipo_campo +
             "&tramite=" + tramite + "&opcion_pr=" + opcion_pr + "&sql=" + sql;	
         
    var pantalla = '<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source;
    abrirXanelaAuxiliar(pantalla,'',
	'width=985,height=430,status='+ '<%=statusBar%>',function(datos){
                        if (datos != null){
                            document.forms[0].validacion.value = datos;        
                        }              
                    });
}

function pulsarVerOper(){    
    var sql = document.forms[0].operacion.value
    sql = replaceAll(sql,"+",";SUMA;");
    
    var opcion_pr = "";    
    var source = "";
    var codMunicipio = "<%=request.getParameter("codMunicipio")%>";
    var tramite =      "<%=request.getParameter("codTramite")%>";
    var tipo_campo = document.forms[0].codTipoDato.value;
    var codProcedimiento = "<%=request.getParameter("codProcedimiento")%>";            
    
    <% if("altaDesdeTramite".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) { %>
            opcion_pr = "TRAM"
    <%}else{%>
            opcion_pr = "PROC"
    <%}%>
    
    if (document.forms[0].codTipoDato.value == 8)
        source = "<c:url value='/sge/DefinicionCampo.do?opcion=VerOperacionNum'/>";
    else
        source = "<c:url value='/sge/DefinicionCampo.do?opcion=VerOperacionFec'/>"; 

    source = source + "&codMunicipio=" + codMunicipio + "&codProcedimiento=" + codProcedimiento + "&tipo_campo=" + tipo_campo +
             "&tramite=" + tramite + "&opcion_pr=" + opcion_pr + "&sql=" + sql;	
         
    var pantalla = '<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source
    var argumentos;
    var ops;
    
    if (document.forms[0].codTipoDato.value == 8){
        argumentos = '';
        ops = 'width=985,height=430,status=' + '<%=statusBar%>';                
    }else{
        argumentos = null;
        ops = 'width=985,height=220,status=' + '<%=statusBar%>';
    }
    abrirXanelaAuxiliar(pantalla,argumentos,ops,function(datos){
        if (datos != null){        
            document.forms[0].operacion.value = datos;        
        }   
    });
}

function rellenarPlantilla() {
    var codTD = document.forms[0].codTipoDato.value;    
    var codP = new Array();
    var descP = new Array();
    var j=0;
    
    if (codTD==1) 
    {
        mostrarCapaValidacion(true);       
        mostrarCapaOperacion(false);        
    }
    else if (codTD == 8 || codTD == 9)
    {
        mostrarCapaOperacion(true);
        mostrarCapaValidacion(false);      
    }
    else
    {
        mostrarCapaOperacion(false);
        mostrarCapaValidacion(false);     
    }
    
    
    if (codTD==6)
    {// Si el campo es un CAMPO DESPLEGABLE EXTERNO
        
        comboPlantilla.activate();
        document.forms[0].tamano.value=tam_campoDesplegable[0];
        document.forms[0].tamano.disabled=true;
        document.forms[0].codPlantilla.value = cod_campoDesplegable[0];
        document.forms[0].descPlantilla.value = desc_campoDesplegable[0];
        comboPlantilla.addItems(cod_campoDesplegable,desc_campoDesplegable);
        //document.forms[0].plazoFecha.disabled=true;
        //document.forms[0].plazoFecha.value="";
        deshabilitarPlazoAlarma();
    }
    else if (codTD == 10)
    {      
      CargarCamposExternos();
    }
    else 
    {
        //ini HABILITAR O DESHABILITAR LAS PLANTILLAS Y EL TAMAÑO
        if (codTD==2) 
        {
              comboPlantilla.activate();              
              deshabilitarPlazoAlarma();
        }
        else 
        {
              comboPlantilla.deactivate();
        }
        
        if (codTD==1 || codTD==2 || codTD==4 || codTD==8) 
        {
            document.forms[0].tamano.disabled=false;            
            deshabilitarPlazoAlarma();
        }
        else 
        {      
            document.forms[0].tamano.disabled=true;
            if (codTD==3)
            {  
                document.forms[0].tamano.value=10;          
                habilitarPlazoAlarma();      
            }
            else if (codTD==9)
            {
                document.forms[0].tamano.value=10;
                deshabilitarPlazoAlarma();   
            }
            else
            {
                document.forms[0].tamano.value=0;            
                deshabilitarPlazoAlarma();        
            } 
        }
        //fin HABILITAR O DESHABILITAR LAS PLANTILLAS Y EL TAMAÑO


        for(i=0;i<cod_tD.length;i++) 
        {
          if(codTD == cod_tD[i]) {
            codP[j] = cod_p[i];
            j++;
          }
        }
        var z=0;
        for(m=0;m<cod_plantilla.length;m++) 
        {
          for(t=0;t<codP.length;t++) 
          {
            if(codP[t]==cod_plantilla[m]) {
              descP[z] = desc_plantilla[m];
              z++;
            }
          }
        }
        document.forms[0].codPlantilla.value = codP[0];
        document.forms[0].descPlantilla.value = descP[0];
        var cod_plant = codP;
        var desc_plant = descP;
        comboPlantilla.addItems(cod_plant,desc_plant);
    }
}

    
function SoloCaracterValidos(objeto) {
    var valores='1234567890ABCDEFGHIJKLMNÑOPQRSTUVWYXZ';
	var numeros='1234567890';
   xAMayusculas(objeto);

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<original.length;i++){
            if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1){
                var valido = true;
                if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1 && original.length==1) valido = false;
                if(valido) salida = salida + original.charAt(i);
            }
        }
        objeto.value=salida.toUpperCase();
   }
 }


function SoloCaracterValidosEspacio(objeto) {      

   var valores='1234567890ABCDEFGHIJKLMNÑOPQRSTUVWYXZ !$()=ÁÉÍÓÚáéíóúüÇç|+-*?¿¡';
   var numeros='1234567890';
   xAMayusculas(objeto);

    if (objeto){
        var original = objeto.value;        
        var salida = "";
        for(i=0;i<original.length;i++){
            if(valores.indexOf(original.charAt(i).toUpperCase())!=-1 || numeros.indexOf(original.charAt(i).toUpperCase())!=-1){
                salida = salida + original.charAt(i);
            }
	  }
        objeto.value=salida.toUpperCase();
	}
}


</SCRIPT>

</head>

<BODY class="bandaBody" onload="pleaseWait('off');inicializar()">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<html:form action="/sge/DefinicionCampo.do" target="_self">
<html:hidden  property="opcion" value=""/>
<input type="hidden" name="desdeProcedimiento">

<div class="txttitblanco"><%=descriptor.getDescripcion("tit_defCampo")%></div>
<div class="contenidoPantalla">
    <TABLE id ="tablaDatosGral" style="width:100%">
        <tr>
            <td style="width:18%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_codigo")%>:</td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="codCampo" style="width:25%" maxlength="20"
                           onkeyup="return SoloCaracterValidos(this);"/>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_rotulo")%>:</td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="rotulo" style="width:100%" maxlength="125"
                           onkeyup="return SoloCaracterValidosEspacio(this);"/>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_desc")%>:</td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descCampo" style="width:100%" maxlength="125"
                           onkeyup="return SoloCaracterValidosEspacio(this);"/>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_tipDato")%>:</td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codTipoDato" style="width:8%"
                           onkeyup="javascript:return SoloDigitos(this);"/>
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descTipoDato" style="width:85%" readonly="true"/>
                <A href="" id="anchorTipoDato" name="anchorTipoDato">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTipoDato" name="botonTipoDato"></span>
                </A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta">
                <%=descriptor.getDescripcion("getiq_camposgrupo")%>:
            </td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  
                            property="codAgrupacion" style="width:8%"
                            onkeyup="javascript:return SoloCaracterValidos(this);"/>
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" 
                            property="descAgrupacion" style="width:85%"
                            readonly="true"/>
                <A href="" id="anchorAgrupacion" name="anchorAgrupacion">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  
                            id="botonAgrupacion" name="botonAgrupacion"></span>
                </A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_comp")%>:</td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio"  property="codPlantilla" style="width:8%"
                            onkeyup="javascript:return SoloCaracterValidos(this);"/>
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="descPlantilla" style="width:85%" readonly="true"/>
                <A href="" id="anchorPlantilla" name="anchorPlantilla">
                    <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonPlantilla" name="botonPlantilla"></span>
                </A>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_tam")%>:</td>
            <td class="columnP">
                <html:text styleId="obligatorio" styleClass="inputTextoObligatorio" property="tamano" style="width:8%" maxlength="4"
                           onkeyup="javascript:return SoloDigitos(this);"/>
                <logic:present name="org.apache.struts.action.ERROR">
                    <%String mensaje = MessageFormat.format(descriptor.getDescripcion("msjTamMaximoCampo"), new Object[]{request.getAttribute("tamanoMaximo")});%>
                    <script>jsp_alerta("A",'<%=mensaje%>');</script>
                </logic:present>
            </td>
        </tr>                                                                                                                                                                                                                                                               
        <tr id="capaValidacion1" name="capaValidacion1">                                                        
            <td class="etiqueta">                                                            
                    <%=descriptor.getDescripcion("gEtiq_val")%>:                                                            
            </td>
            <td>
                    <html:text styleClass="inputTexto" readonly="true" property="validacion" size="54" maxlength="1000"/>                                                            
                    <input type="button" class="botonGeneral" value="Ver.." name="cmdVer" onclick="pulsarVer();">
            </td>
        </tr>     
        <tr id="capaOperacion1" name="capaOperacion1">                                                        
            <td class="etiqueta">                                                            
                    <%=descriptor.getDescripcion("gEtiq_ExpCal")%>:                                                            
            </td>
            <td>                                                            
                    <html:text styleClass="inputTexto" readonly="true" property="operacion"  size="54" maxlength="1000"/>                                                            
                    <input type="button" class="botonGeneral" value="Ver.." name="cmdVerOpe" onclick="pulsarVerOper();" style="width:50px;height:17px">                                                            
            </td>
        </tr>    
        <% if ("altaDesdeTramite".equals(modoInicio) || "modificarDesdeTramite".equals(modoInicio)) {%>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_oblig")%>:</td>
            <td class="columnP">
                <table width="100%">
                    <tr>
                        <td style="width: 25%" class="columnP">
                            <html:radio property="obligat" styleClass="textoSuelto" value="0" onclick=""/><%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                        </td>
                        <td class="columnP">
                            <html:radio property="obligat" styleClass="textoSuelto" value="1" onclick=""/><%=descriptor.getDescripcion("etiq_si")%>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_visExp")%>:</td>
            <td class="columnP">
                <table width="100%">
                    <tr>
                        <td style="width: 25%" class="columnP">
                            <html:radio property="visible" styleClass="textoSuelto" value="N" onclick=""/><%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                        </td>
                        <td class="columnP">
                            <html:radio property="visible" styleClass="textoSuelto" value="S" onclick=""/><%=descriptor.getDescripcion("etiq_si")%>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <% }%>
        <tr>
            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_oculto")%>:</td>
            <td class="columnP">
                <table width="100%">
                    <tr>
                        <td style="width: 25%" class="columnP">
                            <html:radio property="oculto" styleClass="textoSuelto" value="NO" onclick=""/><%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                        </td>
                        <td class="columnP">
                            <html:radio property="oculto" styleClass="textoSuelto" value="SI" onclick=""/><%=descriptor.getDescripcion("etiq_si")%>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
          <td class="etiqueta"><%=descriptor.getDescripcion("etiq_bloqueado")%>:</td>
            <td class="columnP">
                <table width="100%">
                    <tr>
                        <td style="width: 25%" class="columnP">
                            <html:radio property="bloqueado" styleClass="textoSuelto" value="NO" onclick=""/><%=descriptor.getDescripcion("etiq_no")%>&nbsp;
                        </td>
                        <td class="columnP">
                            <html:radio property="bloqueado" styleClass="textoSuelto" value="SI" onclick=""/><%=descriptor.getDescripcion("etiq_si")%>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
         <tr name="filaPlazo" id="filaPlazo">
            <td class="etiqueta"><%=descriptor.getDescripcion("etiq_plazo")%>:</td>
            <td class="columnP">
             <table width="70%">
             <tr>  
                <td width="36%">
                <html:text styleId="plazoFecha" styleClass="inputTexto" property="plazoFecha" size="8" maxlength="4"
                           onkeyup="javascript:return SoloDigitos(this);"/>
                 </td>
                <td class="columnP">
                    <html:radio property="checkPlazoFecha" styleClass="textoSuelto" value="D" onclick=""/><%=descriptor.getDescripcion("etiq_dias")%>&nbsp;
                </td>
                <td class="columnP">
                    <html:radio property="checkPlazoFecha" styleClass="textoSuelto" value="M" onclick=""/><%=descriptor.getDescripcion("etiq_meses")%>
                </td>
                <td class="columnP">
                    <html:radio property="checkPlazoFecha" styleClass="textoSuelto" value="A" onclick=""/><%=descriptor.getDescripcion("etiq_anos")%>
                </td>
              </tr>
              </table>
            </td>      
        </tr>
    </table>
<DIV id="capaBotones1" name="capaBotones1" STYLE="display:none" class="botoneraPrincipal">
    <INPUT type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar"  onclick="pulsarAceptar();">
    <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir"  onclick="pulsarSalir();">
</DIV>
<DIV id="capaBotones2" name="capaBotones2" STYLE="display:none" class="botoneraPrincipal">
    <INPUT type= "button" class="botonGeneral" accesskey="S" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir2"  onclick="pulsarSalir();">
</DIV>
</div>
</html:form>
<script>
function mostrarCapasBotones(nombreCapa) {
  document.getElementById('capaBotones1').style.display='none';
  document.getElementById('capaBotones2').style.display='none';  
  document.getElementById(nombreCapa).style.display='';  
}

function mostrarCapaValidacion(opcion){
    if (opcion == true){
        document.getElementById('capaValidacion1').style.display='';
    }
    else
    {
        document.getElementById('capaValidacion1').style.display='none';
    }
}

function mostrarCapaOperacion(opcion){
    if (opcion == true){
        document.getElementById('capaOperacion1').style.display='';
    }
    else
    {
        document.getElementById('capaOperacion1').style.display='none';
    }
}

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
           if (comboPlantilla.base.style.visibility == "visible" && isClickOutCombo(comboPlantilla,coordx,coordy)) setTimeout('comboPlantilla.ocultar()',20);
           if (comboTipoDato.base.style.visibility == "visible") comboTipoDato.ocultar();
        }
        if (teclaAuxiliar == 9){
           if (comboPlantilla.base.style.visibility == "visible") setTimeout('comboPlantilla.ocultar()',20);
           if (comboTipoDato.base.style.visibility == "visible") comboTipoDato.ocultar();
        }

    keyDel(evento);
}

var comboPlantilla = new Combo("Plantilla");
var comboTipoDato = new Combo("TipoDato");
var comboAgrupacion = new Combo("Agrupacion");

comboTipoDato.change = function() {
  rellenarPlantilla();
}

comboPlantilla.change = function() {   
  document.forms[0].tamano.value=tam_campoDesplegable[comboPlantilla.selectedIndex];
}


function CargarCamposExternos()
{
    var ajax = getXMLHttpRequest();
    var result ="";
    var i = 0;
    if(ajax!=null)
    {                 
                                    
        var url = "<%=request.getContextPath() %>" + "/sge/DefinicionCampo.do";                

        var parametros = "&opcion=VerExterno";                     
        
        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
        try
        {            
            if (ajax.readyState==4 && ajax.status==200)
            {                        
               var text = ajax.responseText;   
               
               comboPlantilla.activate();               
               var datos = text.split("#");          
               cod_plantilla = new Array();
               desc_plantilla = new Array();
               
               for (i=0;i<datos.length;i++) 
                {
                    var datos2 = datos[i].split("||")
                    cod_plantilla[i] = datos2[0];
                    desc_plantilla[i] = datos2[1];
                }
                comboPlantilla.activate();
                document.forms[0].tamano.value=0;
                document.forms[0].tamano.disabled=true;
                if (datos.length > 0)
                {                   
                    document.forms[0].codPlantilla.value = cod_plantilla[0];
                    document.forms[0].descPlantilla.value = desc_plantilla[0];
                }
                comboPlantilla.addItems(cod_plantilla,desc_plantilla);                
            }
        }catch(Err){ 
            alert("Error.descripcion: " + Err.description);
            return "";
        }
    }//if(ajax!=null) 
}
function getXMLHttpRequest()
{
    var aVersions = [ "MSXML2.XMLHttp.5.0","MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0","MSXML2.XMLHttp","Microsoft.XMLHttp"];

    if (window.XMLHttpRequest)
    {
        // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
        return new XMLHttpRequest();
    }
    else if (window.ActiveXObject)
    {
        // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
        for (var i = 0; i < aVersions.length; i++) 
        {
            try {
                var oXmlHttp = new ActiveXObject(aVersions[i]);
                return oXmlHttp;
            }catch (error) {
            //no necesitamos hacer nada especial
            }
         }
    }
}
</script>
</BODY>
</html:html>
