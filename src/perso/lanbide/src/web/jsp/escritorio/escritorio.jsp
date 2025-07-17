<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="java.util.Vector,java.util.Iterator"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioEscritorioValueObject"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ResourceBundle"%>
<%@page import="es.altia.util.commons.DateOperations"%>
<%@ page import="java.text.MessageFormat"%>


<%
    Log _log = LogFactory.getLog(this.getClass());
    
    _log.debug("Entra en escritorio.jsp");
    int idioma = 1;
    String nomUsuario="";
    String login="";
    String fechaUltimoAcceso = "";
    boolean tipoLogin=false;
    String sesionCaduca="no";
    UsuarioEscritorioValueObject usuarioEscritorioVO = null;
    if (session!=null){
        _log.debug("Hay session");
        if(session.getAttribute("usuarioEscritorio")==null){
            _log.debug("UsuarioEscritorio es nulo en sesión");
        } else {
            _log.debug("existe UsuarioEscritorio en sesión");
        }
    
        usuarioEscritorioVO = (UsuarioEscritorioValueObject)session.getAttribute("usuarioEscritorio");
        if (usuarioEscritorioVO!=null){
        idioma =  usuarioEscritorioVO.getIdiomaEsc();
        nomUsuario=usuarioEscritorioVO.getNombreUsu();
        login=usuarioEscritorioVO.getLogin();
        tipoLogin=usuarioEscritorioVO.isTipoLogin();
        if (usuarioEscritorioVO.getFechaUltimoAcceso() != null) {
            fechaUltimoAcceso=DateOperations.toString(usuarioEscritorioVO.getFechaUltimoAcceso(), DateOperations.LATIN_DATE_24HOUR_WITHOUT_ZERO_FORMAT);
        }
            _log.debug("idioma: "+idioma+" - nomUsuario: "+nomUsuario+" - login: "+login+" - tipoLogin: "+tipoLogin+" - fechaUltAcceso: "+fechaUltimoAcceso);
        } else {
                sesionCaduca = "si";
        }
    } else {
        _log.debug("No hay session");
    }
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    ResourceBundle autProperties = ResourceBundle.getBundle("authentication");
    //Ojo, si o existe la propiedad en el .properties peta!
    String modoAcceso=autProperties.getString("Auth/accessMode");
    String propPaginaParaSalir;
    if(modoAcceso.equals("pass"))  propPaginaParaSalir = "Auth/"+modoAcceso+"/urlDesconexion";
    else propPaginaParaSalir = "Auth/urlDesconexion";
    
    String paginaParaSalir=autProperties.getString(propPaginaParaSalir);

    String ALTO_PANTALLA_DESCONEXION  = autProperties.getString("Auth/urlDesconexion/AlturaPantallaDesconexion");    
    String ANCHO_PANTALLA_DESCONEXION = autProperties.getString("Auth/urlDesconexion/AnchoPantallaDesconexion");

    if(ALTO_PANTALLA_DESCONEXION==null || ALTO_PANTALLA_DESCONEXION.equals(""))
        ALTO_PANTALLA_DESCONEXION = "300";

    if(ANCHO_PANTALLA_DESCONEXION==null || ANCHO_PANTALLA_DESCONEXION.equals(""))
        ANCHO_PANTALLA_DESCONEXION = "300";

    _log.debug("container.jsp: PaginaParaSalir antes de comprobar si viene a null:"+paginaParaSalir);
    if (paginaParaSalir!=null){
        if("".equals(paginaParaSalir)){
           paginaParaSalir="Y";
        }
    }else paginaParaSalir="Y";
    _log.debug("container.jsp: PaginaParaSalir después de comprobar si viene a null:"+paginaParaSalir);
    
    //vamos a poner apl=5, para la etiqueta del enlace de cierre de sesion,
    //necesitamos poner una apl cualquiera
    int apl=5;

    String selector = (String)request.getParameter("selector");
    selector = selector==null?"":selector;
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<%
    String changePasswordURL = "";
    Integer daysLeftCASPass = 0;
    String changeLinkText = descriptor.getDescripcion("etiqCambiarContrasena");
    String daysLeftText = "";
    if(modoAcceso.equals("cas")) {
        String service = autProperties.getString("Auth/"+modoAcceso+"/urlRedireccion");
        changePasswordURL = autProperties.getString("Auth/"+modoAcceso+"/urlCambioPass");
        changePasswordURL += "?service=" + service + "/";
        if(usuarioEscritorioVO != null) {
            daysLeftCASPass = usuarioEscritorioVO.getDaysLeftCASPass();
            if(daysLeftCASPass <= 60){
                changeLinkText = "[" + changeLinkText + "]"; 
                daysLeftText = MessageFormat.format(descriptor.getDescripcion("etiqDiasParaCaducidad"), new Object[] { daysLeftCASPass }) + " ";
            }
        }
    }
    _log.debug("En escritorio.jsp --> daysLeft = " + daysLeftCASPass);
%>
<!--[if IE 8]> <html lang="es" class="ie8"> <![endif]-->
<!--[if !IE]><!-->
<html lang="es">
<!--<![endif]-->
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
<jsp:include page="/jsp/escritorio/tpls/app-constants.jsp" />
<TITLE>APPS</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />

<!-- ================== BEGIN BASE CSS STYLE ================== -->
<link rel="stylesheet" href="<c:url value='/login/css/reseter.css'/>">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700" />
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/plugins/jquery-ui/themes/base/minified/jquery-ui.min.css'/>">
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/css/bootstrap.min.css'/>">
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/my/css/bootstrap.css'/>">
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/plugins/font-awesome/css/font-awesome.min.css'/>">
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/my/css/font-awesome.css'/>">
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/css/animate.min.css'/>">
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/css/style.min.css'/>">
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/my/css/style.css'/>">
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/css/style-responsive.min.css'/>">
<link rel="stylesheet" href="<c:url value='/login/css/color_admin/assets/css/theme/orange.css" id="theme'/>">
<link rel="stylesheet" href="<c:url value='/login/css/styles.css'/>">
<!-- ================== END BASE CSS STYLE ================== -->

<!-- ================== BEGIN PAGE LEVEL CSS ================== -->
<!-- ================== END PAGE LEVEL CSS ================== -->

<!-- ================== BEGIN BASE JS ================== -->
<!-- ================== END BASE JS ================== -->
<link rel="stylesheet" href="<c:url value='/css/estilo.css'/>">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/json2.js'/>"></script>    
<SCRIPT TYPE='text/javascript'>
var codigoSel = null;
var appSel = null;

function checkSession(){
    <c:set var="sesionCaduca"><%=sesionCaduca%></c:set>
    console.log('Ha caducado la sesión? <c:out value="${sesionCaduca}" />');
    <c:if test="${sesionCaduca eq 'si'}" >
        <jsp:forward page="/jsp/redirigeCaduca.jsp" />                  
    </c:if>
}

function abrirSelectorAux (datos) {
    var aux = datos.selector;
    if (aux == 'listaOrg') {
        var source = "<c:url value='/jsp/escritorio/listaOrg.jsp?codigo='/>"+codigoSel+
                '&aplicacion=' + appSel + '&mostrarBarraBusqueda=' + datos.mostrarBarraBusqueda;
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/escritorio/mainVentana.jsp?source='+source,'',
                'width=480,height=300,scrollbars=no,status='+ '<%=statusBar%>',function(result) {
                        if (result != undefined) {
                            try{
                                $.ajax({
                                    url: '<c:url value='/GestEntrada.do'/>',
                                    type: 'POST',
                                    async: true,
                                    contentType: 'application/x-www-form-urlencoded; charset=ISO-8859-15', 
                                    data: "orgCod=" +result.orgCod+"&org="+result.org+"&entCod="+result.entCod+
                                            "&ent="+result.ent+"&dtr="+result.dtr+"&ico="+result.ico+"&ine="+result.ine+
                                            "&opcion=orgSelec",
                                    success: procesarResposta,
                                    error: amosaErroResposta
                                });           
                            }catch(Err){
                                jsp_alerta("A",'Error: '+Err.description);
                            }
                        }
                    });
    } else if (aux == 'listaUor') {
        var source = "<c:url value='/jsp/escritorio/listaUor.jsp'/>";

        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/escritorio/mainVentana.jsp?source='+source,'',
                'width=480,height=300,scrollbars=no,status='+ '<%=statusBar%>',function(result) {
                        if (result != undefined) {
                            try{
                                $.ajax({
                                    url: '<c:url value='/GestEntrada.do'/>',
                                    type: 'POST',
                                    async: true,
                                    contentType: 'application/x-www-form-urlencoded; charset=ISO-8859-15', 
                                    data: "opcion=uorSelec&unidadOrgCod="+result.orgCod+"&unidadOrg="+result.org+
                                            "&opcion=uorSelec",
                                    success: procesarResposta,
                                    error: amosaErroResposta
                                });           
                            }catch(Err){
                                jsp_alerta("A",'Error: '+Err.description);
                            }
                        }
                    });
    }
}

function lanzar(cod, app) {
    codigoSel = cod;
    appSel = app;
    
    try{
        $.ajax({
            url: '<c:url value='/GestEntrada.do'/>',
            type: 'POST',
            async: true,
            contentType: 'application/x-www-form-urlencoded; charset=ISO-8859-15', 
            data: "opcion=abrirApp&codigo=" + cod + "&aplicacion=" + app +"&paramPru=probando",
            success: procesarResposta,
            error: amosaErroResposta
        });           
    }catch(Err){
        jsp_alerta("A",'Error: '+Err.description);
    }
}

function procesarResposta(ajaxResult){
    if (ajaxResult){
        var datos = JSON.parse(ajaxResult);
        if (datos.selector == 'selectApp')
            window.top.location = "<%= request.getContextPath()%>" + datos.url;
        else if (datos.selector == 'noApp'){
            //nonHai
        } else 
            abrirSelectorAux(datos);
        
    } else 
        jsp_alerta("A",'<%=descriptor.getDescripcion("msgNoResultBusq")%>');
}

function amosaErroResposta(){
    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
}

function cambiarContrasena(){
    <% if (tipoLogin){%>
    var datosAEnviar = new Array();
    datosAEnviar[0]="<%=login%>";
    var source = "<c:url value='/jsp/escritorio/cambiarClave.jsp?opcion=null'/>";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/escritorio/mainVentana.jsp?source='+source,datosAEnviar,
            'width=400,height=350,status='+ '<%=statusBar%>',function(){});
    <% } else if (modoAcceso.equals("cas")) {%>
        var urlCambio = '<%=changePasswordURL%>';
        console.log("urlCambioPass: " + urlCambio);
        window.location.href = urlCambio;
    <% }%>
}
    
function pulsarSalir(){
    var modoAcceso = "<%=modoAcceso%>";
    var paginaParaSalir="<%=paginaParaSalir%>";

   var ajax = new XMLHttpRequest();
   var CONTEXT_PATH = '<%=request.getContextPath()%>';
   var parametros="";
   if(ajax!=null){
        var url =  CONTEXT_PATH + "/logout.do";

        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-15");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);
        try{
        //Todo ben ok
         if (ajax.readyState==4 && ajax.status==200){
             if(paginaParaSalir!="Y"){
                 try{
                     if(modoAcceso=="pass"){
                         window.top.location = CONTEXT_PATH + "/" + paginaParaSalir;
                        } else if(modoAcceso=="cas"){
                            window.top.location = paginaParaSalir;
                     } else {
                        var params = "resizable=yes, directories=yes, location=yes, menubar=yes, scrollbars=yes, statusbar=no, tittlebar=no, width=" + "<%=ANCHO_PANTALLA_DESCONEXION%>" + "px,height=" + "<%=ALTO_PANTALLA_DESCONEXION%>" + "px";                                 
                        window.open(paginaParaSalir, "nuevo", params);
                        top.close();
                    }
                 }catch(Err){
                     alert("Error: " + Err.description);
                 }                                 
             } else{
                top.close();
             }

          }else{
              top.close();
           }
         }catch(Err){
             top.close();
        }           
    } //ajax !=null     
}

</SCRIPT>
</head>
<body class="pace-top bg-white" onload="checkSession();">        
    <div id="page-container">
        <form action="" name="formEntrada" id="formEntrada" method="post">
            <input type="hidden" name="opcion" id="opcion">
            <input type="hidden" name="codigo" id="codigo">
            <input type="hidden" name="aplicacion" id="aplicacion">
            <input type="hidden" name="orgCod" id="orgCod">
            <input type="hidden" name="org" id="org">
            <input type="hidden" name="entCod" id="entCod">
            <input type="hidden" name="ent" id="ent">
            <input type="hidden" name="dtr" id="dtr">
            <input type="hidden" name="ico" id="ico">
            <input type="hidden" name="ine" id="ine">
        </form>

        <div class="overflow-hidden p-t-40 p-b-40">
            <div id="datos" class="col-xs-10 col-xs-push-1">
                <div class="display-table-with-cells width-full">
                    <div id="logo">
                        <p class="m-0"><img src="<c:url value='/images/logo-login.png'/>" alt="Logo admin" /></p>
                    </div> <!-- /logo -->
                    <div id="logo_flexia">
                        <div class="pull-right">
                            <p class="m-0">
                                <img src="<c:url value='/images/logo_tr.png'/>" alt="Logo Flexia">
                            </p>
                        </div>
                    </div> 
                </div> <!-- /display-table-->
                    <div id="user" class="pull-right">
                        <p class="color-secondary m-0">
                            <span class="f-w-bold"><%=descriptor.getDescripcion("gEtiqFecha")%>:</span> 
                            <script type="text/javascript">
                                var d = new Date();
                                document.write(d.getDate() + "/" + (d.getMonth() + 1) + "/" + d.getFullYear());
                            </script>
                        </p>
                        <p class="color-secondary m-0">
                            <span class="f-w-bold"><%=descriptor.getDescripcion("gEtiqFechaUltimoAcceso")%>:</span> <%= fechaUltimoAcceso %>
                        </p>
                        <p class="color-secondary m-0">
                            <span class="f-w-bold"><%=descriptor.getDescripcion("etiqNombreUsuario")%>:</span> <a href="javascript: cambiarContrasena();" class="enlaceBoton" ><%=nomUsuario%></a>
                        </p>
                        <%
                            if(modoAcceso.equals("cas")) {
                        %>
                        <p class="color-secondary m-0">
                            <span class="f-w-bold"><%=daysLeftText%></span> <a href="javascript: cambiarContrasena();" class="enlaceBoton" ><%=changeLinkText%></a>
                        </p>
                        <% } %>
                        <p></p>
                        <p class="color-secondary m-0">
                            <a id="escritorioSalir" href="javascript: pulsarSalir();" class="enlaceBoton" ><i class="fa fa-power-off color-primary"></i> <%=descriptor.getDescripcion("enlaceCerrarSesion")%></a>									
                        </p>
                </div> <!-- /user -->
            </div> <!-- /datos -->
        </div>
        <div class="bg-f3 p-t-40" id="contedorAplicacions">

        <%  
            Iterator iter = (usuarioEscritorioVO.getIconos()).iterator();
            int i = 0;
            while(iter.hasNext()){
                String codApp = (String)iter.next();
                String nomApp = ((String)iter.next()).toLowerCase();
                String icoApp = (String)iter.next();
                String usuarioLoginNT = (String)session.getAttribute("usuarioLoginNT");
                if(usuarioLoginNT != null && "12".equals(codApp)){
                } else { 
                    if ( i % 4 == 0) {
        %>
            <div class="overflow-hidden width-full display-table-with-cells">
        <%
                    }
        %>
                <div class="col-xs-12 col-sm-6 col-md-3 text-center m-b-25">
                    <p class="m-b-10">
                        <a href="javascript: lanzar('<%=codApp%>', '<%=nomApp%>')">
                            <span><i class="fa <%= icoApp%> color-primary fa-5x"></i></span>
                        </a>
                    </p>
                    <p>
                        <a class="f-s-1-7-r color-secondary" href="javascript: lanzar('<%=codApp%>', '<%=nomApp%>')" title="<%= nomApp%>">
                            <%= nomApp%>
                        </a>
                    </p>
                </div>
         <% 
                    i++;
                    if (i % 4 == 0) {
        %>
            </div> <!-- /table/with-cells -->
        <%
                    }
                }
            } 
            if (i > 0 && i % 4 != 0) {
        %>
            </div> <!-- /table/with-cells -->
        <%
            }
        %>
        </div> <!-- /bg-f3 -->
    </div> <!-- /page-container -->


    <!-- ================== BEGIN BASE JS ================== -->
    <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/jquery/jquery-1.9.1.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/jquery/jquery-migrate-1.1.0.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/jquery-ui/ui/minified/jquery-ui.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/bootstrap/js/bootstrap.min.js'/>"></script>
    
    <script type="text/javascript" src="<c:url value='/login/css/color_admin/assets/plugins/slimscroll/jquery.slimscroll.min.js'/>"></script>
    <link rel="stylesheet" href="<c:url value='/css/estilo.css'/>">
    <!-- ================== END BASE JS ================== -->

    <!-- ================== BEGIN PAGE LEVEL JS ================== -->
    <!-- ================== END PAGE LEVEL JS ================== -->
</body>
</html>