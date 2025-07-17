<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.DatosSuplementariosFicheroForm"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<%@ page import="java.util.Vector"%>

<html:html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <title>::: Adjuntar Fichero :::</title>
        <jsp:include page="/jsp/plantillas/Metas.jsp" />
        <%
            String COMMA = ",";
            int idioma=1;
            int apl=4;
            String codUsu = "";

            UsuarioValueObject usuario=new UsuarioValueObject();
            Log _log = LogFactory.getLog(this.getClass());
            if (session!=null){
              if (usuario!=null) {
                usuario = (UsuarioValueObject)session.getAttribute("usuario");
                idioma = usuario.getIdioma();
                apl = usuario.getAppCod();
                        int cUsu = usuario.getIdUsuario();
                codUsu = Integer.toString(cUsu);
              }
            }
    
        %>

    <!-- Estilos -->
    <!-- Beans -->
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
    <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

    <style type="text/css">
        TR.rojo TD {background-color:red;color:white;}
        TR.gris TD {background-color:white;color:#a5a5a5;}
    </style>
</head>
<body class="bandaBody">
    <script type="text/javascript">

       function pulsarSalir(){
           top.close();
       }
    </script>    
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiqListadoExpedientes")%></div>
    <div class="contenidoPantalla">
        <table class="contenidoPestanha">                                                
            <tr>
                <td colspan="2">                                                        
                    <div id="tablaExpedientes"></div>
                </td>
            </tr>  
        </table>                                
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir" onclick="pulsarSalir();">
        </div>   
    </div>   
</body>

<script type="text/javascript">
    
    var tablaExpedientes = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaExpedientes'));
        
    tablaExpedientes.addColumna('180','center','<%=descriptor.getDescripcion("gEtiqExpGeneradoFichero")%>');
    tablaExpedientes.addColumna('220','center','<%=descriptor.getDescripcion("gEtiqSolicitante")%>');
    tablaExpedientes.addColumna('120','center','<%=descriptor.getDescripcion("gEtiqAnotRegistro")%>');
    tablaExpedientes.addColumna('120','center','<%=descriptor.getDescripcion("gEtiqFecPresRegistro")%>');
    tablaExpedientes.displayCabecera=true;
        
    var datos = new Array();
    var contador = 0;
    
    <logic:iterate name="lista_expedientes_documento" id="expediente">
    
        var interesado = "";
        var contInteresados = 0;
    
    datos[contador] = ['<c:out value="${expediente.numExpediente}"/>','<c:out value="${expediente.interesados}"/>','<c:out value="${expediente.numRegistroInicio}"/>','<c:out value="${expediente.fechaPresentacionRegistroInicio}"/>'];
        contador++;
    </logic:iterate>
    
    tablaExpedientes.lineas=datos;
    tablaExpedientes.displayTabla();
    
</script>
</html:html>
