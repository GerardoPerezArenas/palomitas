<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.text.MessageFormat"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: EXPEDIENTES  Definición de Campos:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->





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


%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />


    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/dojo/dojo.js"></script>

<script type="text/javascript">
	dojo.require("dojo.widget.FilteringTable");

    function disableOthersRadioBtns(value) {
        if(value == 0) { //Radio button seleccionado='Constante'
            document.getElementById("constante").disabled = false;
            document.getElementById("constante").className = "inputTextoObligatorio";
            document.getElementById("filaComboTipo").style.display = "inline";
            comboTipo.deactivate();
            document.getElementById("filaComboOtroValor").style.display = "none";
            comboOtroValor.deactivate();
        } else if(value == 1) { //Radio button seleccionado='Campo del expediente'
            document.getElementById("constante").disabled = true;
            document.getElementById("constante").className = "inputTextoDeshabilitado";
            document.getElementById("filaComboTipo").style.display = "inline";
            comboTipo.activate();
            document.getElementById("filaComboOtroValor").style.display = "none";
            comboOtroValor.deactivate();
        } else if(value == 2) { //Radio button seleccionado='Otro valor'
            document.getElementById("constante").disabled = true;
            document.getElementById("constante").className = "inputTextoObligatorio";
            document.getElementById("filaComboTipo").style.display = "none";
            comboTipo.deactivate();
            document.getElementById("filaComboOtroValor").style.display = "inline";
            comboOtroValor.activate();
            //TODO activate comboOtros: comboOtroValor.activate();
        }
    }

    function inicializar() {
        document.forms[0].nombreParametro.value = '<bean:write name="parametro" property="tituloParam" scope="request"/>';
        <logic:equal name="parametro" property="tipoValorPaso" scope="request" value="0">
            document.forms[0].codigoTipoPaso[0].checked="true";
        </logic:equal>
        <logic:equal name="parametro" property="tipoValorPaso" scope="request" value="1">
            document.forms[0].codigoTipoPaso[1].checked="true";
        </logic:equal>
        <logic:equal name="parametro" property="tipoValorPaso" scope="request" value="2">
            document.forms[0].codigoTipoPaso[2].checked="true";
        </logic:equal>
        <logic:equal name="parametro" property="ordenParam" scope="request" value="0">
            document.forms[0].codigoTipoPaso[0].disabled="true";
        </logic:equal>        
        disableOthersRadioBtns('<bean:write name="parametro" property="tipoValorPaso" scope="request"/>');
        document.forms[0].constante.value = '<bean:write name="parametro" property="valorConstante" scope="request"/>';
        
        inicializarComboTipo();
        inicializarComboOtroValor();
    }
    
    function inicializarComboTipo(){
        //Rellenar
        var titulosCampos = new Array();
        var datosCampos = new Array();
        var indice = 0;
        <logic:iterate id="campo" name="campos" scope="request">
            titulosCampos[indice] = '<bean:write name="campo" property="titulo"/>';
            datosCampos[indice] = ['<bean:write name="campo" property="origen"/>',
                    '<bean:write name="campo" property="titulo"/>',
                    '<bean:write name="campo" property="tabla"/>'];
            indice++;
        </logic:iterate>
        comboTipo.addItems(titulosCampos, titulosCampos);
        //Pre seleccionar
        <logic:notEmpty name="parametro" property="codCampoExp" scope="request">
            comboTipo.buscaLinea('<bean:write name="parametro" property="codCampoExp" scope="request"/>');
        </logic:notEmpty>
        <logic:empty name="parametro" property="codCampoExp" scope="request">
            comboTipo.buscaLinea('-1');
        </logic:empty>
    }
    
    function inicializarComboOtroValor(){
        //Rellenar
        var titulos = new Array();
        var codigos = new Array();
        var indice = 0;
        
        titulos[indice] = 'Login usuario conectado';
        codigos[indice++] = 'usu_conect_login';
        
        comboOtroValor.addItems(titulos, titulos);
        //Pre seleccionar
        <logic:notEmpty name="parametro" property="codCampoExp" scope="request">
            comboOtroValor.buscaLinea('<bean:write name="parametro" property="codCampoExp" scope="request"/>');
        </logic:notEmpty>
        <logic:empty name="parametro" property="codCampoExp" scope="request">
            comboOtroValor.buscaLinea('-1');
        </logic:empty>
    }
    
    function pulsarAceptar() {
        document.forms[0].opcion.value = "guardarCambiosParametro";
        var source = "<c:url value='/sge/ConfiguracionSWTramite.do'/>";

		document.forms[0].action = source;
        document.forms[0].submit();

        var datos = new Array();
        datos[0] = 'guardarCambiosParametro';
        if (document.getElementById("constante").disabled && document.getElementById("filaComboOtroValor").style.display == 'none'){
                datos[1] = document.forms[0].descTipo.value;
                datos[3] = '1';        	 
       	} else if(document.getElementById("constante").disabled){
            	datos[1] = document.forms[0].descOtroValor.value;
        	datos[3] = '2';
        } else {
            	datos[1] = document.forms[0].constante.value;
        	datos[3] = '0';
        }
        datos[2] = <bean:write name="selectedIndex" scope="request"/>;
        
        self.parent.opener.retornoXanelaAuxiliar(datos);
    }



</script>


</head>

<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
         inicializar();}">

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>



<html:form action="/sge/ConfiguracionSWTramite.do" target="_self">


<html:hidden  property="opcion" value=""/>
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_confParam")%></div>
<div class="contenidoPantalla">
    <table style="width:100%">
        <tr>
            <td id="nombreParam" width="90%" height="75px" align="center" valign="middle" bgcolor="#e6e6e6">
                <fieldset style="height:75px;">
                    <legend class="etiqueta">Parámetro</legend>
                    <br>
                    <div width="100%" heigth=50% align="center" valign="middle">
                        <input name="nombreParametro" class="inputTextoObligatorio" style="width:80%" readonly="readonly"/>
                    </div>
                </fieldset>
            </td>
        </tr>
        <tr>
        <td id="valorPaso" width="90%" height="280px" align="center" valign="middle" bgcolor="#e6e6e6">
            <fieldset style="height:280px;">
                <legend class="etiqueta">Valor de Paso</legend>
                <table width="100%" height="100%">
                  <tr>
                    <td id="tipoValor" width="90%" height="20%" align="center" valign="middle" bgcolor="#e6e6e6">
                       <fieldset style="height:38%;">
                         <legend class="etiqueta">Tipo de Valor</legend>
                         <table width="100%" height="100%" cellpadding="1px" valign="middle"  cellspacing="0px" border="0px">
                           <tr>
                             <td align="center">
                               <input type="radio" class="etiqueta" name="codigoTipoPaso" value="0" onClick="disableOthersRadioBtns(this.value);"/>
                               <span class="etiqueta">Constante</span>
                             </td>
                             <td align="center">
                               <input type="radio" class="etiqueta" name="codigoTipoPaso" value="1" onClick="disableOthersRadioBtns(this.value);"/>
                               <span class="etiqueta">Campo del Expediente</span>
                             </td>
                             <td align="center">
                               <input type="radio" class="etiqueta" name="codigoTipoPaso" value="2" onClick="disableOthersRadioBtns(this.value);"/>
                               <span class="etiqueta">Otro valor</span>
                             </td>
                           </tr>
                         </table>
                       </fieldset>
                    </td>
                  </tr>
                  <tr id="filaComboTipo" style="display: inline">
                    <td valign="middle" align="center">
                        <input type="text" class="inputTextoObligatorio" name="descTipo" id="descTipo" style="width:500px" readonly="true">
                        <A href="" id="anchorTipo" name="anchorTipo">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonTipo" name="botonTipo" style="cursor:hand;"></span></A>
                    </td>
                  </tr>
                  <tr id="filaComboOtroValor" style="display: none">
                    <td valign="middle" align="center">
                        <input type="text" class="inputTextoObligatorio" name="descOtroValor" id="descOtroValor" style="width:500px" readonly="true">
                        <A href="" id="anchorOtroValor" name="anchorOtroValor">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonOtroValor" name="botonOtroValor" style="cursor:hand;"></span></A>
                    </td>
                  </tr>
                  <tr style="display: inline">
                    <td valign="middle" align="center">
                          <input type="text" class="inputTextoObligatorio" name="constante" id="constante" style="width:530px">
                    </td>
                  </tr>
                </table>
            </fieldset>
            </td>
          </tr>
        </table>
        <DIV id="capaBotones1" name="capaBotones1"class="botoneraPrincipal" >
            <INPUT type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar"  onclick="pulsarAceptar();">
            <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir"  onclick="self.parent.opener.retornoXanelaAuxiliar();">
        </DIV>
    </div>
</html:form>

<script type="text/javascript">

	var comboTipo = new Combo("Tipo");
	var comboOtroValor = new Combo("OtroValor");

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
                if (comboTipo.base.style.visibility == "visible" && isClickOutCombo(comboTipo,coordx,coordy)) setTimeout('comboTipo.ocultar()',20);
            }
            if (teclaAuxiliar == 9){
               comboTipo.ocultar();
            }
        }

</script>

</BODY>

</html:html>

