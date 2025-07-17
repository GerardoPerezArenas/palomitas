<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.DocumentosExpedienteForm"%>
<%@page import="es.altia.common.service.config.*" %>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>
<%@page import="org.apache.commons.logging.Log" %>
<%@page import="org.apache.commons.logging.LogFactory" %>

<%@page contentType="text/html; charset=utf-8"%>
<jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
      UsuarioValueObject usuarioVO = new UsuarioValueObject();
      GeneralValueObject paramsPadron = new GeneralValueObject();
      int idioma=1;
      DocumentosAplicacionForm daForm=null;
      DocumentosExpedienteForm daForm2=null;
      String[] params=null;
      String parametro0 = "";
    String parametro6 = "";
      if (session!=null)
      {
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        params = usuarioVO.getParamsCon();
        idioma =  usuarioVO.getIdioma();
        parametro0 = params[0];//oracle
        parametro6 = params[6];//jndi
        daForm =(DocumentosAplicacionForm)session.getAttribute("EditorDocumentosAplicacionForm");
        daForm2 =(DocumentosExpedienteForm)session.getAttribute("DocumentosExpedienteForm"); 
      }
      String aplicacion=request.getParameter("codAplicacion");
      String proc = request.getParameter("codProcedimiento");
      String tramite = request.getParameter("codTramite");
      String numExpediente = request.getParameter("numero");
      Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
      String xml="";

      if(request.getParameter("opcion").equals("crear")){
      xml=daForm2.getDatosXML();
            xml=xml.substring(xml.indexOf(">")+1);
      }
      
      String numDocumento = daForm2.getNumeroDocumento();
      if (numDocumento==null) numDocumento="";
      String ver="";
      if(request.getParameter("opcion").equals("visualizar")){ver="ver";}
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
      Log m_Log = LogFactory.getLog(DocumentosExpedienteForm.class.getName());
      m_Log.debug("Esto es el servlet......................" + servlet);
      m_Log.debug("Esto es el applet......................." + urlApplet);
       session.setAttribute("opcion", "grabarDocumentoOffice");
       session.setAttribute("codAplicacion", aplicacion);
      
    %>
   
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="6" />
    <!-- Ficheros JavaScript -->    
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

<body class="bandaBody">
<html:form enctype="multipart/form-data" action="/sge/DocumentosExpediente.do" method="POST" target="oculto">
    <html:hidden property="codProcedimiento"/>
    <html:hidden property="ejercicio"/>
    <html:hidden property="numeroExpediente"/>
    <html:hidden property="codTramite"/>
    <html:hidden property="ocurrenciaTramite"/>
    <html:hidden property="codDocumento"/>
    <html:hidden property="codPlantilla"/>
    <html:hidden property="codUsuario"/>
    <html:hidden property="numeroDocumento"/>
    <html:hidden property="nombreDocumento"/>
    <html:hidden property="opcion"/>
    <html:hidden property="datosXML"/>
    <html:hidden property="codMunicipio"/>

    <input type="Hidden" name="sessionID" value="<%=session.getId()%>">
    </html:form>


<applet name="appletOpenOffice" codebase='<%=urlApplet%>'
        code="es.altia.openOffice.AppletOpenOffice" archive="appletOOffice.jar" 
        width="1250" height="800">
        <param name ="estado" value="tramitacion"/>    
        <param name="ver" value='<%=ver%>'/>     
        <param name="xml" value='<%=xml%>'/>
        <param name="idioma" value='<%=idioma%>'/>
        <param name="codPlantilla" value='<%=daForm2.getCodPlantilla()%>'/> 
        <param name="urlServlet" value='<%=servlet%>'/>            
        <param name="parametro0" value='<%=parametro0%>'/>
        <param name="parametro6" value='<%=parametro6%>'/>   
        <param name="municipio" value='<%=daForm2.getCodMunicipio()%>'/>
        <param name="numExpediente" value='<%=numExpediente.replace("/","+")%>'/>
        <param name="codTramite" value='<%=daForm2.getCodTramite()%>'/>
        <param name="ocurrencia" value='<%=daForm2.getOcurrenciaTramite()%>'/>
        <param name="numDocumento" value='<%=numDocumento%>'/>
        <param name="nombreFichero" value='<%=daForm2.getNombreDocumento()%>'/>
        <param name="scriptable" value="true"/>
</applet>

</body>

<SCRIPT  type="text/javascript">

var x = 2;
var y = 1;
var t;
function comprobarApplet(){
x = x-y;    
t=setTimeout("comprobarApplet()", 1000);

    if(x==0){ 
        if ((document.appletOpenOffice.getExitApplet())=="si"){
            document.forms[0].opcion.value="actualizarTablaDocumentos";
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
            document.forms[0].submit();
            clearTimeout(t);
            window.close();
        }
    x=1;    
    }
}
comprobarApplet();
    
</SCRIPT>





