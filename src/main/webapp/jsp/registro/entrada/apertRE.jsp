<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<HTML>
<head>
    
    <TITLE>::: REGISTRO DE ENTRADA - Cierre :::</TITLE>
    <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
    
    <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 1;
            String css = "";

            if (session != null) {
                if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    idioma = usuarioVO.getIdioma();
                    apl = usuarioVO.getAppCod();
                    css = usuarioVO.getCss();
                }
            }
            String userAgent = request.getHeader("user-agent");

    %>
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    
    <!-- Ficheros JavaScript -->    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
    <script src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>  
    <script>
        
        var fechaHoy;
        //Usado para el calendario
        var coordx=0;
        var coordy=0;


        <%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
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

                 if('Alt+C'==tecla)
                    pulsarCerrar();
                if('Alt+N'==tecla)
                    pulsarCancelar();

                if (teclaAuxiliar == 1){
                   if(IsCalendarVisible) replegarCalendario(coordx,coordy);
                }
                if (teclaAuxiliar == 9){
                   if(IsCalendarVisible) hideCalendar();
                }
                
            }
        
        function pulsarCancelar() {
            window.location = "<%=request.getContextPath()%>/MantAnotacionRegistro.do?opcion=E";
        }
        
        function mostrarCalDesde(evento) {
            if(window.event) evento = window.event;
            if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1 )
                showCalendar('forms[0]','fechaCerrar',null,null,null,'','calDesde','',null,null,null,null,null,null,null,null,evento);
        }
        
        function validarFechaAqui() {
            var fechaCerrar  = new Date(document.forms[0].fechaCerrar.value.substring(6,10),
                eval(document.forms[0].fechaCerrar.value.substring(3,5)-1),
                document.forms[0].fechaCerrar.value.substring(0,2));
            
            var fechaUltC= new Date(document.forms[0].fechaUCE.value.substring(6,10),
                eval(document.forms[0].fechaUCE.value.substring(3,5)-1),
                document.forms[0].fechaUCE.value.substring(0,2) );
            
            if (comparaFechas(fechaCerrar,fechaUltC) == 1) {
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoAbrirFecAb")%>');
                    return 0;
                }
                return 1;
            }
            
            
            function pulsarAbrir() {
                var fechaCerrar = document.forms[0].fechaCerrar.value;
                /*
                if (!validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                    var msjObl = '<%=descriptor.getDescripcion("msjObligTodos")%>';
                    document.forms[0].fechaCerrar.focus();
                    }*/
                if(fechaCerrar==null || fechaCerrar.length==0){
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
                    var msjObl = '<%=descriptor.getDescripcion("msjObligTodos")%>';
                    document.forms[0].fechaCerrar.focus();
                } else {
                if (comprobarFecha(document.forms[0].fechaCerrar) ){
                    if(validarFechaAqui() == 1 ) {			
                        document.forms[0].opcion.value="abrir_entrada";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/AperturaCierreRegistro.do";
                        document.forms[0].submit();
                    } else {
                    document.forms[0].fechaCerrar.focus();
                }
            }else {
            			
                document.forms[0].fechaCerrar.focus();		
            }
        }
    }
    
    function inicializar() {
        fechaHoy = new Date(document.forms[0].fechaCerrar.value.substring(6,10),
            eval(document.forms[0].fechaCerrar.value.substring(3,5)-1),
            document.forms[0].fechaCerrar.value.substring(0,2));
        habilitarImagenCal("calDesde",true);
        document.forms[0].fechaCerrar.focus();
    }
    
    
    
    
    function comprobarFecha(inputFecha) {
        if (Trim(inputFecha.value)!='') {
            if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>'); //usamos flag por si hace la comprobacion dos veces (pirde foco y al abrir) que no muestre la alerta las dos veces
                    inputFecha.focus();
                    return false;
                } 
            } 
            return true;
        }
        
    </script>
    
</head>

<body class="bandaBody" onload="javascript:{ 
   }">
    
    
    
    
<html:form action="/AperturaCierreRegistro.do">

    <html:hidden property="opcion" />

    <div id="titulo" class="txttitblanco"> <%=descriptor.getDescripcion("tit_abrRE")%></div>
    <div class="contenidoPantalla" valign="top">
        <TABLE id="tabla1"  ALIGN="center" style="height:75px;" >
           <TR>
               <TD class="etiqueta"><%=descriptor.getDescripcion("ufc_Reg")%>:</TD>
               <TD class="columnP">
                   <html:text property="fechaUCE" styleClass="inputTxtFechaDeshabilitado" size="12" maxlength="10" readonly="true"/>
               </TD>
           </TR>
           <TR>
               <TD class="etiqueta"><%=descriptor.getDescripcion("fa_Reg")%>:</TD>
               <TD class="columnP">
                   <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="12" maxlength="20" property="fechaCerrar"
                                   onkeyup = "javascript:return SoloCaracteresFecha(this);"
                                   onblur = "javascript:return comprobarFecha(this);"
                                   onfocus = "this.select();"
                                   />
               <A href="javascript:calClick(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;"  onClick="mostrarCalDesde(event);return false;">
                   <span class="fa fa-calendar" aria-hidden="true" id="calDesde" name="calDesde" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>" ></span>
               </A>
            </TD>
           </TR>
       </TABLE>
        <div class="botoneraPrincipal">
            <INPUT type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAbrir")%>" name="cmdAbrir" onClick="pulsarAbrir();" alt="<%=descriptor.getDescripcion("toolTip_bAbrir")%>" title="<%=descriptor.getDescripcion("toolTip_bAbrir")%>">
            <INPUT type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar"  onClick="pulsarCancelar();" alt="<%=descriptor.getDescripcion("toolTip_bCancelar")%>" title="<%=descriptor.getDescripcion("toolTip_bCancelar")%>">
        </div>
    </div>
    </html:form>
    <SCRIPT>
        inicializar(); // Inicializaciones.
    </SCRIPT>
    
    
    
</BODY>

</HTML>
