<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.technical.Fecha" %>
<%@page import="java.util.Date" %>

<HTML>
    
    <head>
        
        <%
            int idioma = 1;
            String css = "";
            if (session != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    css = usuario.getCss();
                }
            }            

        %>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="1" />
        
        <TITLE>
            <%=descriptor.getDescripcion("res_tituloE")%>
        </TITLE>
        <META http-equiv="expires" content="0">
        <META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <LINK rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

<script type="text/javascript">            
            
function pulsarCancelar() {
    document.formulario.opcion.value = "salirE";
    document.formulario.action = "<html:rewrite page='/ReservaOrden.do'/>";
    document.formulario.submit();
}
                        
function pulsarAceptar(){
    if(noCero() == "cero") return;
    var cantidad = document.forms[0].cantidad.value;    
    if(cantidad!=null && cantidad.length>=1){
        //if (!validarFormulario()) return;
    document.forms[0].opcion.value = "numeroE";
    document.forms[0].action = "<%=request.getContextPath()%>/ReservaOrden.do";
    document.forms[0].submit();
     }else
        jsp_alerta("A","Debe rellenar el campo obligatorio");
}

            function noCero() {
                if(document.forms[0].cantidad.value == "0") {
                    var texto1='<%=descriptor.getDescripcion("noReservaCero")%>';
                    jsp_alerta("A",texto1);
                    document.forms[0].cantidad.value = "1";
                    inicializar();
                    return "cero";
                }
                return "algo";
            }                        
                    
function inicializar() {
    var a='<bean:write name="ReservaOrdenForm" property="a"/>';
    if (a=="false" ) {
        jsp_alerta("A", '<%=descriptor.getDescripcion("noReservasRC")%>');
        deshabilitarDatos(formulario);
        document.formulario.cmdAceptar.disabled = true;
        document.formulario.opcion.value = "salirE";
        document.formulario.action = "<html:rewrite page='/ReservaOrden.do'/>";
        document.formulario.submit();
    }
}
                    
                    function checkKeysLocal(evento,tecla) {
                        if(window.event) evento = window.event;
                        if('Alt+C'==tecla)
                            pulsarAceptar();
                        if('Alt+N'==tecla)
                            pulsarCancelar();
                    }
                                                                                
        </script>
        
    </head>
    
    
<body class="bandaBody">
<FORM name="formulario" METHOD=POST target="_self">
    <INPUT type="hidden" name="opcion">
    <INPUT TYPE="hidden" NAME="txtDiaEntrada" value='<bean:write name="ReservaOrdenForm" property="txtDiaEntrada"/>'>
    <INPUT TYPE="hidden" NAME="txtMesEntrada" value='<bean:write name="ReservaOrdenForm" property="txtMesEntrada"/>'>
    <INPUT TYPE="hidden" NAME="txtAnoEntrada" value='<bean:write name="ReservaOrdenForm" property="txtAnoEntrada"/>'>
    <INPUT TYPE="hidden" class='inputTxtFechaObligatorio' size=1 maxlength=2 NAME="txtDiaAbrir">
    <INPUT TYPE="hidden" class='inputTxtFechaObligatorio' size=1 maxlength=2 NAME="txtMesAbrir">
    <INPUT TYPE="hidden" class='inputTxtFechaObligatorio' size=3 maxlength=4 NAME="txtAnoAbrir">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("res_numeroE")%></div>
    <div id="tabla" class="contenidoPantalla">
        <table style="width: 40%" align="center">
            <tr>
                <TD colspan="2" class="sub3titulo" style="width: 40%;text-align:center">
                    <%=descriptor.getDescripcion("res_pregunta1")%> <br>
                </TD>
            </tr>
            <TR>	
                <TD style="width:60%" class="etiqueta"><%=descriptor.getDescripcion("numDesRes")%>:</TD>
                <TD style="width:40%" class="columnP">
                    <INPUT TYPE="text" class='inputTextoObligatorio' id='obligatorio' SIZE=5 MAXLENGTH=4  NAME="cantidad" value="1" onkeyup = "return SoloDigitosNumericos(this);" onfocus = "this.select();">
                </TD>
            </TR>
        </TABLE>								
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbConfirmar")%> "
                name="cmdAceptar" onclick = "pulsarAceptar();" alt="<%=descriptor.getDescripcion("toolTip_bConfirmar")%>" 
                title="<%=descriptor.getDescripcion("toolTip_bConfirmar")%>">
          <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbcaNcelar")%>"   
                name="cmdSalir" onclick="pulsarCancelar();" alt="<%=descriptor.getDescripcion("toolTip_bCancelar")%>" title="<%=descriptor.getDescripcion("toolTip_bCancelar")%>">
        </div>
    </div>
    </form>
    <script>
        inicializar(); // Inicializaciones.
    </script>
    
</BODY>
</HTML>
