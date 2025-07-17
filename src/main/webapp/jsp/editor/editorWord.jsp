<!doctype html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="es.altia.common.service.config.*" %>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>

<%UsuarioValueObject usuarioVO = new UsuarioValueObject();
  GeneralValueObject paramsPadron = new GeneralValueObject();
  String codMunicipio = "";
  String parametro0 = "";
  String parametro6 = "";
  int idioma=1;
  DocumentosAplicacionForm daForm=null;
  String[] params=null;
  if (session!=null)
  {
	usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
	params = usuarioVO.getParamsCon();
	idioma =  usuarioVO.getIdioma();
    codMunicipio = Integer.toString(usuarioVO.getOrgCod());
    parametro0 = params[0];//oracle
    parametro6 = params[6];//jndi
	daForm =(DocumentosAplicacionForm)session.getAttribute("EditorDocumentosAplicacionForm");
  }
  String aplicacion=request.getParameter("codAplicacion");
  String proc = request.getParameter("codProcedimiento");
  String tramite = request.getParameter("codTramite");
  Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
  String macroPlantillas = m_ConfigTechnical.getString("macroPlantillas");
  String hostVirtual = m_ConfigTechnical.getString("hostVirtual");
  
    String dominioFinal = "";
    if(hostVirtual==null || hostVirtual.length()==0) {
        dominioFinal = StrutsUtilOperations.getProtocol(request) +"://" + macroPlantillas + request.getContextPath();
    } else {
        dominioFinal = hostVirtual+ request.getContextPath();
    }

  %>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="6" />
<html:html>
<head><jsp:include page="/jsp/editor/tpls/app-constants.jsp" />
    <title>Edición de Documentos</title>
    <%@ include file="/jsp/plantillas/Metas.jsp" %>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE10"/>
    
    <link rel="stylesheet" media="screen" type="text/css" href="<c:url value='/css/estilo.css'/>">
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/utilidadesXerador.js'/>"></script>
    <script type="text/vbscript" src="<c:url value='/scripts/documentos.vbs'/>"></script>
    <script type="text/javascript">

        function mensajeError(clave) {
            if (clave == 'ErrorMessages.Document.NotTemplate') {
                jsp_alerta('A', '<fmt:message key="ErrorMessages.Document.NotTemplate"/>');
            }
            if (clave == 'ErrorMessages.Document.NotOpen') {
                jsp_alerta('A', '<fmt:message key="ErrorMessages.Document.NotOpen"/>');
            }
            if (clave == 'ErrorMessages.Word.NotOpen') {
                jsp_alerta('A', '<fmt:message key="ErrorMessages.Word.NotOpen"/>');
            }
        }

    </script>
 
</head>
<body class="bandaBody" onload="Inicio()">
    <html:form enctype="multipart/form-data" action="/editor/DocumentosAplicacion" method="POST" target="oculto">
    <html:hidden property="codAplicacion"/>
    <html:hidden property="codProcedimiento"/>
    <html:hidden property="codTramite"/>
    <html:hidden property="codDocumento"/>
    <html:hidden property="nombreDocumento"/>
    <html:hidden property="interesado"/>
    <html:hidden property="relacion"/>
    <html:hidden property="docActivo"/>
    <html:hidden property="editorTexto" value="WORD"/>
    <input type="Hidden" name="sessionID" value="<%=session.getId()%>">
    </html:form>

    <script type="text/vbscript" language="VBScript">
    <%java.util.Vector etiquetas = daForm.getListaEtiquetas();
      String strEtiquetas = "";
      String strValores   = "";
      if(etiquetas!=null && etiquetas.size()>0)
      {
        GeneralValueObject gVO = (GeneralValueObject)etiquetas.get(0);
        strEtiquetas += gVO.getAtributo("nombre");
        strValores   += gVO.getAtributo("codigo");
        for(int i=1; i < etiquetas.size();i++)
        {
            gVO = (GeneralValueObject)etiquetas.get(i);
            strEtiquetas += ","+gVO.getAtributo("nombre");
            strValores   += ","+gVO.getAtributo("codigo");
        }
      }%>
    etiquetas = "<%=strEtiquetas.toUpperCase()%>"
    valores   = "<%=strValores%>"
    Sub Inicio()
        Dim plantilla

        If Not(document.forms(0).codDocumento.Value="") Then
            plantilla = "<%=dominioFinal%>/temp/documentos/<%=params[0]%>/<%=params[6]%>/Documento" & document.forms(0).codDocumento.value & ".doc"
        End If
        sURL = "<%=dominioFinal%>/GrabarDoc" & _
                  ";jsessionid=" & document.forms(0).sessionID.value & _
                  "?opcion=grabarDocumento&" & _
                  "codMunicipio=" & "<%=codMunicipio%>" & "&" & _
                  "codAplicacion=" & document.forms(0).codAplicacion.value & "&" & _
                  "codProcedimiento=" & document.forms(0).codProcedimiento.value & "&" & _
                  "codTramite=" & document.forms(0).codTramite.value & "&" & _
                  "codDocumento=" & document.forms(0).codDocumento.value & "&" & _
                  "interesado=" & document.forms(0).interesado.value & "&" & _
                  "docActivo=" & document.forms(0).docActivo.value & "&" & _
                  "parametro0=" & "<%=parametro0%>" & "&" & _
                  "parametro6=" & "<%=parametro6%>" & "&" & _
                  "relacion=" & document.forms(0).relacion.value    
        Call ejecutaPlantilla(plantilla,sURL,etiquetas,valores,"<%= dominioFinal %>")
    End Sub
    </script>
</body>
</html:html>
