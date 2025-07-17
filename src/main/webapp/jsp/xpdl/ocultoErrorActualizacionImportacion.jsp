<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.agora.business.sge.ErrorImportacionXPDL"%>
<%@ page import="java.util.ArrayList"%>
<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Gestión de Ficheros</title>
  <script type="text/javascript">

     // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
        window.close();
        var ruta = '<c:url value="/jsp/xpdl/errorActualizacionImportacion.jsp"/>';        
        window.opener.location.href='<c:url value="/jsp/xpdl/errorActualizacionImportacion.jsp"/>';

    }
  </script>
</head>
<body onload="inicializar();">
</body>
</html>
