<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="es.altia.common.service.config.*" %>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>
<%@page import="org.apache.commons.logging.Log" %>
<%@page import="org.apache.commons.logging.LogFactory" %>



<%UsuarioValueObject usuarioVO = new UsuarioValueObject();
            GeneralValueObject paramsPadron = new GeneralValueObject();
            String codMunicipio = "";
            String parametro0 = "";
            String parametro6 = "";
            int idioma = 1;
            DocumentosAplicacionForm daForm = null;
            String[] params = null;
            if (session != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuarioVO.getParamsCon();
                idioma = usuarioVO.getIdioma();
                codMunicipio = Integer.toString(usuarioVO.getOrgCod());
                parametro0 = params[0];//oracle
                parametro6 = params[6];//jndi
                daForm = (DocumentosAplicacionForm) session.getAttribute("EditorDocumentosAplicacionForm");
                
            }
            String aplicacion = request.getParameter("codAplicacion");
            String proc = request.getParameter("codProcedimiento");
            String tramite = request.getParameter("codTramite");
            Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");           

      session.setAttribute("opcion", "grabarDocumentoOffice");
      Config m_Conf = ConfigServiceHelper.getConfig("common");    
      String hostVirtual = m_Conf.getString("hostVirtual");
      String servlet="";
      String urlApplet="";
      if ("".equals(hostVirtual)) {
          servlet= "http://"+request.getHeader("Host")+request.getContextPath();
          urlApplet =StrutsUtilOperations.getProtocol(request)+"://"+request.getHeader("Host")+request.getContextPath()+"/jsp/editor";
      }
      else {
          servlet= hostVirtual+request.getContextPath();
          urlApplet =hostVirtual+request.getContextPath()+"/jsp/editor";
      }
      Log m_Log = LogFactory.getLog(DocumentosAplicacionForm.class.getName());
      m_Log.debug("Esto es el servlet......................" + servlet);
      m_Log.debug("Esto es el applet......................." + urlApplet);
%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="6" />

<html:html>
    <head><jsp:include page="/jsp/editor/tpls/app-constants.jsp" />
        <title>Edición de Documentos</title>
        <%@ include file="/jsp/plantillas/Metas.jsp" %>
        <link rel="stylesheet" media="screen" type="text/css" href="<c:url value='/css/estilo.css'/>">
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/utilidadesXerador.js'/>"></script>
        <script type="text/javascript">
            
            
      <%java.util.Vector etiquetas = daForm.getListaEtiquetas();
            String strEtiquetas = "";
            String strValores = "";
            if (etiquetas != null && etiquetas.size() > 0) {
                GeneralValueObject gVO = (GeneralValueObject) etiquetas.get(0);
                strEtiquetas += gVO.getAtributo("nombre");
                strValores += gVO.getAtributo("codigo");
                for (int i = 1; i < etiquetas.size(); i++) {
                    gVO = (GeneralValueObject) etiquetas.get(i);
                    strEtiquetas += "|" + gVO.getAtributo("nombre");
                    strValores += "|" + gVO.getAtributo("codigo");
                }
            }

            String etiqs = strEtiquetas.toUpperCase();
            String valores = strValores;
             m_Log.debug(request.getAttribute("docActivo"));
                %>
                    
                
                 
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
    <body class="bandaBody">
    <html:form enctype="multipart/form-data" action="/editor/DocumentosAplicacion" method="POST"  target="oculto">
        <html:hidden property="codAplicacion"/>
        <html:hidden property="codProcedimiento"/>
        <html:hidden property="codTramite"/>
        <html:hidden property="codDocumento"/>
        <html:hidden property="nombreDocumento"/>
        <html:hidden property="interesado"/>
        <html:hidden property="opcion"/>
        <html:hidden property="relacion"/>
        <html:hidden property="docActivo"/>
        <html:hidden property="editorTexto" value="OOFFICE"/>
        <input type="Hidden" name="sessionID" value="<%=session.getId()%>">
    </html:form>
        
        
       <applet name="appletOpenOffice" codebase='<%=urlApplet%>'
        code="es.altia.openOffice.AppletOpenOffice" archive="appletOOffice.jar" 
        width="1250" height="800">
                <param name ="estado" value="definicion"/>
                <param name="codigos" value='<%=valores%>'/>
                <param name="etiquetas" value='<%=etiqs%>'/>
                <param name="idioma" value='<%=idioma%>'/>
                <param name="urlServlet" value='<%=servlet%>'/>   
                <param name="codDocumento" value='<%=daForm.getCodDocumento()%>'/>   
                <param name="parametro0" value='<%=parametro0%>'/>
                <param name="parametro6" value='<%=parametro6%>'/>  
                <param name="nombreFichero" value='<%=daForm.getNombreDocumento()%>'/>
                <param name="scriptable" value="true"/>
            </applet>
        
    </body>
    
    
 <script type="text/javascript">

var x = 2;
var y = 1;
var t;
var app = '<%=usuarioVO.getAppCod()%>';


function comprobarApplet(){
x = x-y;    
t=setTimeout("comprobarApplet()", 1000);

    if(x==0){
        if ((document.appletOpenOffice.getExitApplet())=="si"){
            if (app=='6') { //Desde el modulo de documentos
                document.forms[0].opcion.value = 'cargarDocumentos';
            }else if (app=='4'){ //Desde la definicion del tramite
                document.forms[0].opcion.value = 'cargarDocumentosDesdeDefinicion';
            }
            document.forms[0].target = "oculto";
            document.forms[0].action = "<c:url value='/editor/DocumentosAplicacion.do'/>";
            document.forms[0].submit();
            clearTimeout(t);
        }
    x=1;
    }
}
comprobarApplet();

</script>
</html:html>
