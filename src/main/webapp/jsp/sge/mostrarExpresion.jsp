<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title>::: Mostrar Expresión:::</title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<!-- Estilos -->
<!-- Beans -->
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<style type="text/css">
   TR.rojo TD {background-color:red;color:white;}
   TR.gris TD {background-color:white;color:#a5a5a5;}
</style>
</head>
<body class="bandaBody">
<script type="text/javascript">
    var codCampos = new Array();
    var tiposCampos = new Array();
    var j=0;    
      
    function pulsarCancelar(){
        self.parent.opener.retornoXanelaAuxiliar(null);
    }       
</script>


<%String sql=request.getParameter("sql");
    if (sql==null) sql="";
    sql = sql.replace(";SUMA;","+");
    sql = sql.replace(";DIAS;"," DIAS ");
    sql = sql.replace(";MESES;"," MESES ");
    sql = sql.replace(";ANOS;"," AÑOS ");
%>
<form action="" name="f" target="_self">
    <input type="hidden" name="opcion" value=""/>

    <div class="txttitblanco">Expresión</div>
    <div class="contenidoPantalla">
        <textarea readonly="true" name="sql" class="textareaTexto" cols="63" rows="3" style="text-transform: none"></textarea>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="Cerrar" name="cmdCancelar" onclick="pulsarCancelar();">
        </div>
    </div>
</form>
<script type="text/javascript">               
    document.f.sql.value = "<%=sql%>";          
</script>
</body>
</html>
