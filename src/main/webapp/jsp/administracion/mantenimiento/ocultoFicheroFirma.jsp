<%@ page import="org.apache.struts.upload.FormFile"%>
<%@ page import="java.util.Vector"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.UsuariosGruposForm"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject"%>

<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Gestión de Ficheros</title>
  <script type="text/javascript">

	// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
        <%UsuariosGruposForm uForm = new UsuariosGruposForm();
        uForm = (UsuariosGruposForm) session.getAttribute("UsuariosGruposForm");
        UsuariosGruposValueObject uVO = new UsuariosGruposValueObject();
        uVO = uForm.getUsuariosGrupos();
        Log m_Log = LogFactory.getLog(this.getClass());
        FormFile fichero = uVO.getFicheroFirma();

        uVO.setFicheroFirmaFisico(fichero.getFileData());
        uVO.setTipoFirma(fichero.getContentType());
        %>
        self.parent.opener.retornoXanelaAuxiliar("OK");
    }

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
