<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Vector"%>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-15">
        <title>Mantenimiento de firmas </title>
        <!-- Estilos -->
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 4;
            String estilo = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
                estilo = usuarioVO.getCss();
            }
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
        %>        
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=estilo%>">
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript">
            var listaFirmasCompleta = new Array();
            var listadoFirmas = new Array();
            var argVentana = self.parent.opener.xanelaAuxiliarArgs;            
            var codDocumento;
            var codProcedimiento;
            var desdeDefinicion = true;
            var defTramitacionCorrecta;
            
            function inicializar(){            
                argVentana = self.parent.opener.xanelaAuxiliarArgs;
                document.forms[0].codMunicipio.value = argVentana[0];
                codDocumento=argVentana[1];
                codProcedimiento=argVentana[2];
                desdeDefinicion = argVentana[3];
                if (desdeDefinicion){
                    //No visualizaco los botones alta/eliminar
                    document.getElementById("botonDefinir").style.display = 'none';
                } else {
                    //No visualizaco el botón de definiir
                    document.getElementById("botonAlta").style.display = 'none';
                    document.getElementById("botonEliminar").style.display = 'none';
                }
                var i=0;
                var uorExpediente = "";
                var nombreUor = "";
                var nombreCargo = '--';
                var nombreUsuario = '--';
                var codCargo = null;
                var codUsuario = null;
                <c:forEach var="campo" items="${requestScope.listaFirmas}">
                    nombreCargo = '--';
                    nombreUsuario = '--';
                    codCargo = null;
                    codUsuario = null;
                    uorExpediente = '<bean:write name="campo" property="uor" ignore="true"/>';
                    if (uorExpediente==-999) {
                        nombreUor = '<%=descriptor.getDescripcion("gEtiq_txtUnidExp")%>';
                    } else if (uorExpediente==-888) {
                        nombreUor = '<%=descriptor.getDescripcion("gEtiq_defExp")%>';
                    } else { 
                        nombreUor =  '<bean:write name="campo" property="nomUor" ignore="true"/>';
                    }
                    if ('<bean:write name="campo" property="nomCargo" ignore="true"/>'!='') {
                        codCargo = '<bean:write name="campo" property="cargo" ignore="true"/>';
                        nombreCargo = '<bean:write name="campo" property="nomCargo" ignore="true"/>';
                    }
                    if ('<bean:write name="campo" property="nomUsuario" ignore="true"/>'!='') {
                        codUsuario = '<bean:write name="campo" property="usuario" ignore="true"/>';
                        nombreUsuario = '<bean:write name="campo" property="nomUsuario" ignore="true"/>';
                    }
                    listaFirmasCompleta[i] = [codDocumento,'<bean:write name="campo" property="orden" ignore="true"/>',
                        codUsuario,document.forms[0].codMunicipio.value,codProcedimiento,codCargo,
                        '<bean:write name="campo" property="uor" ignore="true"/>','<bean:write name="campo" property="finalizaRechazo" ignore="true"/>', '<bean:write name="campo" property="tramitar" ignore="true"/>' ];
                    listadoFirmas [i] = ['<bean:write name="campo" property="orden" ignore="true"/>',
                        nombreUsuario,nombreCargo,nombreUor];
                    i++;
                </c:forEach>
                tablaFirmas.lineas = listadoFirmas;
                tablaFirmas.displayTabla();
            }

            function pulsarCancelar(){
               self.parent.opener.retornoXanelaAuxiliar();
            }
                
                function pulsarAlta(){
                    var codMunicipio     = document.forms[0].codMunicipio.value;
                    var datosAEnviar = new Array();
                    datosAEnviar[0] = codMunicipio;                    
                    datosAEnviar[1] = true;  
                    datosAEnviar[2] = 0;  
                    var source = "<c:url value='/ListadoFirmasDocumentoProcedimiento.do?opcion=nuevaFirma'/>" +
                        "&codMunicipio=" + codMunicipio+ "&desdeDefinicionProcedimiento=true"
                        +"&ordenDefinido=0";                         
                    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,
                        datosAEnviar,'width=790,height=380,scrollbars=no,status='+ '<%=statusBar%>', 
                        function(datos){
                            if (datos!=undefined) {
                                incorporarATabla([datos[6],datos[7],datos[5],datos[2]],[codDocumento,datos[6],datos[8],
                                    document.forms[0].codMunicipio.value,codProcedimiento,datos[4],datos[0],datos[9],datos[10]]);
                            }
                        });
                }
                function incorporarATabla(nuevaFila,nuevaFilaListaCompleta) {
                    var ordenNuevaFila = nuevaFila[0];
                    var insertado = false;
                    if (nuevaFila[0]==null||nuevaFila[0]==''||listadoFirmas.length<ordenNuevaFila) {
                        nuevaFila[0]=listadoFirmas.length+1;
                        nuevaFilaListaCompleta[1]=listadoFirmas.length+1;
                        listadoFirmas.push(nuevaFila);
                        listaFirmasCompleta.push(nuevaFilaListaCompleta);
                    }
                    else {
                        for (var i=0;i<listadoFirmas.length;i++) {
                            if (!insertado) {
                                if (ordenNuevaFila==listadoFirmas[i][0]) {
                                    listadoFirmas.splice(i,0,nuevaFila);
                                    listaFirmasCompleta.splice(i,0,nuevaFilaListaCompleta);
                                    insertado=true;
                                }
                            }else {
                                listadoFirmas[i][0]++;
                                listaFirmasCompleta[i][1]++;
                            }
                        }
                    }
                    tablaFirmas.lineas = listadoFirmas;
                    tablaFirmas.displayTabla();
                }

                function pulsarEliminar() {
                    var indice = tablaFirmas.selectedIndex ;
                    if(indice != -1) {
                        if(jsp_alerta('', '<%=descriptor.getDescripcion("msjBorrarFirma")%>')) {
                            listaFirmasCompleta.splice(tablaFirmas.selectedIndex,1);
                            listadoFirmas.splice(tablaFirmas.selectedIndex,1);
                            for (var i=indice;i<listadoFirmas.length;i++) {
                                listadoFirmas[i][0]--;
                                listaFirmasCompleta[i][1]--;
                            }
                            tablaFirmas.lineas = listadoFirmas;
                            tablaFirmas.displayTabla();
                        }
                    } else jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                }

                function pulsarDefinir(){
                    var indice = tablaFirmas.selectedIndex ;
                    if(indice != -1) {
                        if (listaFirmasCompleta[indice][6]!='-888'){
                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjFirmaConfig")%>');
                        } else {
                            var codMunicipio     = document.forms[0].codMunicipio.value;
                            var datosAEnviar = new Array();
                            datosAEnviar[0] = codMunicipio;   
                            datosAEnviar[1] = false;
                            datosAEnviar[2] = listadoFirmas[indice][0];
                            var source = "<c:url value='/ListadoFirmasDocumentoProcedimiento.do?opcion=nuevaFirma'/>" +
                                "&codMunicipio=" + codMunicipio + "&desdeDefinicionProcedimiento=false"
                                +"&ordenDefinido="+listadoFirmas[indice][0];
                            abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,
                                datosAEnviar,'width=790,height=290,status='+ '<%=statusBar%>', 
                                function(datos){
                                    if (datos!=undefined) {
                                        //Eliminamos la definción no completa
                                        listaFirmasCompleta.splice(tablaFirmas.selectedIndex,1);
                                        listadoFirmas.splice(tablaFirmas.selectedIndex,1);
                                        for (var i=indice;i<listadoFirmas.length;i++) {
                                            listadoFirmas[i][0]--;
                                            listaFirmasCompleta[i][1]--;
                                        }
                                        tablaFirmas.lineas = listadoFirmas;
                                        tablaFirmas.displayTabla();                  

                                        incorporarATabla([datos[6],datos[7],datos[5],datos[2]],[codDocumento,datos[6],datos[8],
                                            document.forms[0].codMunicipio.value,codProcedimiento,datos[4],datos[0],datos[9]]);
                                    }
                                });
                        } 
                    } else {
                        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
                    }
                }

                function pulsarAceptar() {
                    var codMunicipio     = document.forms[0].codMunicipio.value;
                    var listaFirmasStr = crearListaParaGuardar(); 
                    
                    if (desdeDefinicion){
                        document.forms[0].target="oculto";
                        document.forms[0].action="<c:url value='/ListadoFirmasDocumentoProcedimiento.do?opcion=guardarCircuito'/>" +
                            "&codMunicipio="+ codMunicipio + "&codProcedimiento="+codProcedimiento+"&codDocumento="+codDocumento+
                            "&listaFirmasStr="+listaFirmasStr;                        
                        document.forms[0].submit();
                    } else {
                        if (defTramitacionCorrecta){
                            self.parent.opener.retornoXanelaAuxiliar(listaFirmasStr);
                        } else {
                            jsp_alerta('A', '<%=descriptor.getDescripcion("msjFaltaFirma")%>');
                        }                        
                    }                                        
                }

                
	function crearListaParaGuardar() {
            var resultado="";
            defTramitacionCorrecta = true;
            
            for (var i=0;i<listaFirmasCompleta.length;i++) {
                    //uor-cargo-usuario-orden
                    if (listaFirmasCompleta[i][6]=='-888')
                            defTramitacionCorrecta = false;

                    var auxFinalizar=false;
                    if (typeof listaFirmasCompleta[i][7] != 'undefined'){
                            if(listaFirmasCompleta[i][7]=='1' || listaFirmasCompleta[i][7]=='true'){
                                    auxFinalizar=true;
                            }
                    }                    
                    resultado += listaFirmasCompleta[i][6]+"¬¬"+listaFirmasCompleta[i][5]+"¬¬"+listaFirmasCompleta[i][2]+"¬¬"+
                            listaFirmasCompleta[i][1]+"¬¬"+ listaFirmasCompleta[i][7] + "¬¬" + auxFinalizar+"¬¬" + listaFirmasCompleta[i][8] + '§¥';
            }            
            return resultado;
        }

                function datosGuardados(exito, numeroFirmas) {
                    if (exito=='SI') {
                        jsp_alerta('A', '<%=descriptor.getDescripcion("circuitoGuardado")%>');
                        self.parent.opener.retornoXanelaAuxiliar(numeroFirmas);
                    } else {
                        jsp_alerta('A', '<%=descriptor.getDescripcion("circuitoNoGuardado")%>');
                        self.parent.opener.retornoXanelaAuxiliar();
                    }
                }
                
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{pleaseWait('off');
        
            inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form  method="post">
    <html:hidden property="codMunicipio" value="" />
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titListaFirmasProcedimiento")%></div>
    <div class="contenidoPantalla">    
        <div  id="tabla"></div>
        <div class="botoneraPrincipal">
            <input id="botonAlta" type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAlta")%>
                   name="botonLimpiar" onClick="pulsarAlta();" accesskey="L">
            <input id="botonEliminar" type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%>
                   name="botonLimpiar" onClick="pulsarEliminar();" accesskey="L">
            <input id="botonDefinir" type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbDefinir")%>
                   name="botonLimpiar" onClick="pulsarDefinir();" accesskey="d">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%>
                   name="botonGrabar" onClick="pulsarAceptar();" accesskey="g">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%>
                   name="botonCancelar" onClick="pulsarCancelar();" accesskey="S">
        </div>
    </div>
</form>
<script type="text/javascript">
    //Creamos tablas donde se cargan las listas
    tablaFirmas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
    tablaFirmas.addColumna('50','center',"<str:escape><%=descriptor.getDescripcion("etiqOrdenFirma")%></str:escape>");
    tablaFirmas.addColumna('200','center',"<str:escape><%=descriptor.getDescripcion("etiqUsuarioFirma")%></str:escape>");
    tablaFirmas.addColumna('200','center',"<str:escape><%=descriptor.getDescripcion("etiqCargoFirma")%></str:escape>");
    tablaFirmas.addColumna('300','center',"<str:escape><%=descriptor.getDescripcion("etiqUorFirma")%></str:escape>");
    
    tablaFirmas.height = 240;
    tablaFirmas.displayCabecera=true;
    tablaFirmas.displayTabla();
</script>                                               
</body>
</html>
