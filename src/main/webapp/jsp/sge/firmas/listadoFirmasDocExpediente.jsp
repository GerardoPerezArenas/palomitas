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

            
            function inicializar(){
                
                var i=0;                
                var nombreUor = "";
                var nombreCargo = "";
                var nombreUsuario = "";
                var estado= "";
                var orden = "";
                var codigo = "";
                var listadoFirmas = new Array();
            <c:forEach var="campo" items="${requestScope.listaFirmas}">
                    codigo = '<bean:write name="campo" property="codigo" ignore="true"/>';
                    nombreUor = '<bean:write name="campo" property="nombreUnidad" ignore="true"/>';
                    nombreCargo = '<bean:write name="campo" property="nombreCargo" ignore="true"/>';
                    nombreUsuario = '<bean:write name="campo" property="nombreUsuario" ignore="true"/>';
                    estado = '<bean:write name="campo" property="estado" ignore="true"/>';              
                    
                    var url = '<a href="#" onclick="javascript:btnVerDatos_click('+codigo+');" />'+
                        '  (<fmt:message key='Portafirmas.SearchDocumentoPortafirmasForm.datosFirma'/>)</a>';
                    
                    if ('O'==estado){
                        estado = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaP")%>';
                    } else if ('F'==estado){
                        estado = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaF")%>';
                        estado += url;
                    } else if ('R'==estado){
                        estado = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaR")%>';
                        estado += url;
                    } else if ('X'==estado){
                        estado = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaX")%>';
                    } else if('S'==estado){
                        estado = '<%=descriptor.getDescripcion("gEtiq_estadoFirmaS")%>';                        
                    }
                                        
                    orden = '<bean:write name="campo" property="orden" ignore="true"/>';
                    listadoFirmas [i] = [orden,nombreUsuario,nombreCargo,nombreUor,estado];                        
                    i++;
            </c:forEach>
                    tablaFirmas.lineas = listadoFirmas;
                    tablaFirmas.displayTabla();
                }


                function btnVerDatos_click(codigo){
                    var finalURL = '<c:url value="/sge/documentofirma/ViewDocumentoFirma.do?"/>';
                    finalURL = finalURL + "tipoDocumento=3";
                    finalURL = finalURL + "&codigoFirmaDocProcedimiento=" + codigo;
                    abrirXanelaAuxiliar(finalURL, null,'width='+650+',height='+300+',status=no,resizable=no,scrollbars=no',function(){});
                }

                function pulsarSalir(){
                    top.close();
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
        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titListaFirmasExpediente")%></div>
        <div class="contenidoPantalla">
            <div  id="tabla"></div>
            <div class="botoneraPrincipal">
                <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>"
                       name="botonSalir" onClick="pulsarSalir();" accesskey="S">
            </div>
        </div>
</form>
<script type="text/javascript">
    //Creamos tablas donde se cargan las listas
    tablaFirmas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
    tablaFirmas.addColumna('50','center',"<str:escape><%=descriptor.getDescripcion("etiqOrdenFirma")%></str:escape>");    
    tablaFirmas.addColumna('200','center',"<str:escape><%=descriptor.getDescripcion("etiqUsuarioFirma")%></str:escape>");
    tablaFirmas.addColumna('200','center',"<str:escape><%=descriptor.getDescripcion("etiqCargoFirma")%></str:escape>");
    tablaFirmas.addColumna('200','center',"<str:escape><%=descriptor.getDescripcion("etiqUorFirma")%></str:escape>");
    tablaFirmas.addColumna('120','center',"<str:escape><%=descriptor.getDescripcion("etiqEstadoFirma")%></str:escape>");
    tablaFirmas.displayCabecera=true;
    tablaFirmas.displayTabla();
</script>                                               
</body>
</html>
