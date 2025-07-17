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
    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
    <link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css">
    <script src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    
    
    <script>

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
                 if (teclaAuxiliar == 9){
                     if(IsCalendarVisible) hideCalendar();
                 }
                 if (teclaAuxiliar == 1){
                     if(IsCalendarVisible) replegarCalendario(coordx,coordy);
                 }
                 
            }




        var fechaHoy;
        
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
            
            if (comparaFechas(fechaCerrar,fechaUltC) == 0  || comparaFechas(fechaCerrar,fechaUltC) == 2) {
                jsp_alerta("A",'<%=descriptor.getDescripcion("msj_menorCerrado")%>');
                    return 0;
                } else if(comparaFechas(fechaCerrar,fechaHoy) == 1) {
                jsp_alerta("A",'<%=descriptor.getDescripcion("msj_noCerrarProx")%>');
                    return 0;
                }
                return 1;
            }
            
            
            function pulsarCerrar() {
                if (!validarObligatorios('<%=descriptor.getDescripcion("msjObligTodos")%>')) {
                    var msjObl = '<%=descriptor.getDescripcion("msjObligTodos")%>';
                    document.forms[0].fechaCerrar.focus();
                } else {
                if (comprobarFecha(document.forms[0].fechaCerrar) ){
                    if(validarFechaAqui() == 1 ) {			
                        document.forms[0].opcion.value="cerrar_entrada";
                        document.forms[0].target="oculto";
                        document.forms[0].action="<%=request.getContextPath()%>/AperturaCierreRegistro.do";
                        document.forms[0].submit();
                    } else {
                    document.forms[0].fechaCerrar.focus();
                }
            }else {
            jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');			
                document.forms[0].fechaCerrar.focus();		
            }
        }
    }
    
    function inicializar() {
        
        //document.all.tabla1.style.marginTop = 0;//calcularPosTabla(document.all.tabla1);
        //var fecha = obtenerFechaString();
        //document.forms[0].fechaCerrar.value=fecha;
        fechaHoy = new Date(document.forms[0].fechaCerrar.value.substring(6,10),
            eval(document.forms[0].fechaCerrar.value.substring(3,5)-1),
            document.forms[0].fechaCerrar.value.substring(0,2));
        habilitarImagenCal("calDesde",true);
        document.forms[0].fechaCerrar.focus();
    }
    
    
   
    
    function comprobarFecha(inputFecha) {
        if (Trim(inputFecha.value)!='') {
            if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                    inputFecha.focus();
                    return false;
                } 
            } 
            return true;
        }
        
    </script>
    
</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off'); 
   }">
    
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
<html:form action="/AperturaCierreRegistro.do">
<html:hidden property="opcion" />
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_crrRE")%></div>
<div class="contenidoPantalla">
    <TABLE id="tabla1" style="width:60%" >
        <TR>
                <TD style="width:60%" class="etiqueta"><%=descriptor.getDescripcion("ufc_Reg")%>:</TD>
                <TD style="width:40%" class="columnP">
                    <html:text property="fechaUCE" styleClass="inputTxtFechaDeshabilitado" size="12" maxlength="10" readonly="true"/>
                </TD>
            </TR>
            <TR>
                <TD style="width:60%" class="etiqueta"><%=descriptor.getDescripcion("fc_Reg")%>:</TD>
                <TD style="width:40%" class="columnP">
                    <html:text styleId="obligatorio"  styleClass="inputTxtFechaObligatorio" size="12" maxlength="20" property="fechaCerrar"
                                    onkeyup = "javascript:return SoloCaracteresFecha(this);"
                                    onblur = "javascript:return comprobarFecha(this);"
                                    onfocus = "this.select();"
                                    />
                <A href="javascript:calClick(event);return false;" onClick="mostrarCalDesde(event);return false;">
                    <span class="fa fa-calendar" aria-hidden="true" id="calDesde" name="calDesde" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>" ></span>
                </A>
            </TD>
        </TR>
    </TABLE>
    <div class="botoneraPrincipal">
        <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCerrar")%> name="cmdCerrar" onClick="pulsarCerrar();" alt="<%=descriptor.getDescripcion("toolTip_bCerrar")%>" title="<%=descriptor.getDescripcion("toolTip_bCerrar")%>">
        <INPUT type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar"  onClick="pulsarCancelar();" alt="<%=descriptor.getDescripcion("toolTip_bCerrar")%>" title="<%=descriptor.getDescripcion("toolTip_bCerrar")%>">
    </div>     
</div>     
</html:form>

<SCRIPT>
    inicializar(); // Inicializaciones.
</SCRIPT>

</BODY>
</HTML>
