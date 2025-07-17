<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="java.util.Vector"%>

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Edición de Documentos</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <!-- Estilos -->
      <link rel="StyleSheet" media="screen" type="text/css" href="<c:url value='/css/estilo.css'/>">
      
      <link rel="stylesheet" href="<c:url value='/css/calendar.css'/>" type="text/css">
    <%
      UsuarioValueObject usuarioVO = new UsuarioValueObject();
      GeneralValueObject paramsPadron = new GeneralValueObject();
      int idioma=1;
      DocumentosAplicacionForm daForm=null;
      if (session!=null)
      {
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        idioma =  usuarioVO.getIdioma();
        daForm =(DocumentosAplicacionForm)session.getAttribute("EditorDocumentosAplicacionForm");
      }
      String aplicacion=request.getParameter("codAplicacion");
      String proc = request.getParameter("codProcedimiento");
      String tramite = request.getParameter("codTramite");
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="6" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/utilidadesXerador.js'/>"></script>
    <script language="VBScript" src="<c:url value='/scripts/documentos.vbs'/>"></script>

 </head>
 <body class="bandaBody">
    <html:form enctype="multipart/form-data" action="/sge/DocumentosRelacionExpedientes.do" method="POST" target="oculto">
    <html:hidden property="codProcedimiento"/>
    <html:hidden property="ejercicio"/>
    <html:hidden property="numeroRelacion"/>
    <html:hidden property="numeroExpediente"/>
    <html:hidden property="codTramite"/>
    <html:hidden property="ocurrenciaTramite"/>
    <html:hidden property="codDocumento"/>
    <html:hidden property="codPlantilla"/>
    <html:hidden property="codUsuario"/>
    <html:hidden property="numeroDocumento"/>
    <html:hidden property="opcion"/>
    <html:hidden property="datosXML"/>
    <div id="titulo" class="txttitblanco">Documentos de Relación</div>
    <div class="contenidoPantalla">
        <table cellpadding="0px" cellspacing="0px">
            <tr>
                <td colspan="2" width="100%" height="10px"></td>
            </tr>
            <tr>
                <td width="22%" class="etiqueta" align="right">Nombre:&nbsp;&nbsp;</td>
                <td width="88%">
                    <html:text styleClass="inputTextoObligatorio" size="43" property="nombreDocumento"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" width="100%" height="10px"></td>
            </tr>
            <tr id="trProc">
                <td width="22%" class="etiqueta" align="right">Procedimiento:&nbsp;&nbsp;</td>
                <td width="88%">
                    <html:file property="ficheroWord" styleId="fichero" styleClass="inputTexto" size="43" />
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value='Aceptar' name="Actualizar" onclick="pulsarGuardar()" accesskey="A">
            <input type="button" class="botonGeneral" value='Cancelar' name="Volver" onClick="pulsarCerrar();" accesskey="C">
        </div>
    </div>
    </html:form>
    <!--
    Si hubiera una plantilla debería obtenerla y abrirla para editarla.
    Por ahora la creo nueva.
    -->
    <script type="text/javascript">

    function pulsarCerrar() { self.parent.opener.retornoXanelaAuxiliar(null); }

    function pulsarGuardar(){
        if(document.forms[0].nombreDocumento.value==''){
            jsp_alerta('A','El nombre del Documento es obligatorio.');
        }else if(document.forms[0].fichero.value==''){
            jsp_alerta('A','Debe de seleccionar un fichero con el boton "Examinar".');
        }else if (document.forms[0].fichero.value.lastIndexOf('.doc') == -1){
            jsp_alerta('A','El documento debe de ser un documento de Word.');
        }else{
            document.forms[0].tagert="oculto";
            document.forms[0].action = "<c:url value='/sge/DocumentosRelacionExpedientes.do'/>";
            document.forms[0].opcion.value="grabarDocumento";
            document.forms[0].submit();
        }
    }
    </script>
 </body>
</html:html>
