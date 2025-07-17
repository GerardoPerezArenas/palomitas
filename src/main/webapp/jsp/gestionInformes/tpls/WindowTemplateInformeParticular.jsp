<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<html:html locale="true" >
    <fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
    <jsp:useBean id="descriptor" scope="page" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>

    <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <title><c:out value="${requestScope.InformeParticularForm.tituloInforme}"/></title>
        <jsp:include page="/jsp/gestionInformes/tpls/app-constants.jsp" />
        <jsp:include page="/jsp/gestionInformes/tpls/Metas.jsp" />
        <script type="text/javascript" language="JavaScript" src="<c:url value='/scripts/general.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/sge_basica.css'/>" media="screen">
<style type="text/css">


  html>body.nuevo
{
    font-family: Verdana, Arial, Helvetica, Sans-serif;
    font-size: 10px;
    margin-top: 0px;
    margin-bottom: 0px;
    padding-top: 0px;
    padding-bottom: 0px;
    padding-left: 0px
    /* BCS background-image: url("../images/escritorio/fondo.jpg"); */
}

   html>body.nuevo div.cuerpoPagina
{
    width: 980px;
    height: 515px;
    border: 1px solid black;
    border-left: 1px solid  #666666;
    border-right: 2px solid  #666666;
    border-top: 1px solid  #666666;
    border-bottom: 2px solid  #666666;
    background-color: #ffffff;
    margin-bottom: 0px;
}



   html>body.nuevo div.cuerpoPagina div.cuerpoAplicacion
{
    height:89%;
    border: 1px solid #ffffff;
    border-top: 1px solid  #666666;
    background-color:  #fffaff;
    padding-left: 20px;
    padding-right: 20px;
    padding-top: 20px;
}

</style>

<script type='text/javascript'>
    function lanzarProceso(proceso){
        if(proceso!=""){
          //proceso = "<%//=request.getContextPath()%>" + proceso;
          if (proceso == "<c:url value='/gestionInformes/GestionInformes.do?opcion=inicio'/>" ||
              proceso=="<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=generacion'/>" ||
              proceso=="<c:url value='/gestionInformes/SolicitudesInformes.do?opcion=inicio'/>"||
              proceso=="<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=administracion'/>"||
              proceso=="<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=administracionModo'/>"||
              proceso=="<c:url value='/gestionInformes/MantenimientoInformes.do?opcion=administracionCampos'/>"){
              document.location.href=proceso;
          } else if (proceso == "<c:url value='/sge/DefinicionProcedimientos.do?opcion=catalogoProcedimientos'/>") {
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+proceso,null,
                        'width=760,height=510,status='+ 'no',function(){});
          } else {
                modificando = "N";
                document.forms[1].target = "_top";
                document.forms[1].action = "<c:url value='/jsp/escritorio/container.jsp?opcion='/>"+proceso;
                document.forms[1].submit();
          }
        }
    }
    VerCorrect=-1;
</script>
 </head>

 <body class="nuevo" onLoad="inicializar();">
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>
    <div id="lyrBodyContent" class="contedorGlobal">
        <div name="menu" class="iframeMenu">
            <jsp:include page="/jsp/menu.jsp" flush="true"/>
        </div>
        <div name="mainFrame" class="iframeMainFrame">
            <div class="txttitblanco">
                <span>
                    <c:out value="${requestScope.InformeParticularForm.tituloInforme}"/>
                </span>
            </div>
            <div class="contenidoPantalla">
                <tiles:insert attribute="altiaAppFormContent"/>
                <div class="botoneraPrincipal">
                    <tiles:insert attribute="altiaAppFormBotons"/>
                </div>
            </div>
        </div>
    </div>

    <script type="text/javascript" language="JavaScript">
        pleaseWait1("off",this);
        function inicializar()
        {
        }
    </script>

 </body>
</html:html>
