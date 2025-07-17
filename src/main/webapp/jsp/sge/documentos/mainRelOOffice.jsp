<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.DocumentosRelacionExpedientesForm"%>
<%@page import="es.altia.common.service.config.*" %>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>
<%@page import="org.apache.commons.logging.Log" %>
<%@page import="org.apache.commons.logging.LogFactory" %>

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Edición de Documentos</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
      UsuarioValueObject usuarioVO = new UsuarioValueObject();
      GeneralValueObject paramsPadron = new GeneralValueObject();
      int idioma=1;
      String parametro0="";
      String parametro6="";
      DocumentosAplicacionForm daForm=null;
      DocumentosRelacionExpedientesForm daForm2=null;
      String[] params=null;
      if (session!=null)
      {
        usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
        params = usuarioVO.getParamsCon();
        parametro0 = params[0];
        parametro6 = params[6];
        idioma =  usuarioVO.getIdioma();
        daForm2 = (DocumentosRelacionExpedientesForm)session.getAttribute("DocumentosRelacionExpedientesForm");                
      }
    
      String numDocumento = daForm2.getNumeroDocumento();
      if (numDocumento==null) numDocumento="";
      
      String xml="";
      if(request.getParameter("opcion").equals("crear")){
      xml=daForm2.getDatosXML();
      xml=xml.substring(xml.indexOf(">")+1);
      }
      
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
      Log m_Log = LogFactory.getLog(DocumentosRelacionExpedientesForm.class.getName());
      m_Log.debug("Esto es el servlet......................" + servlet);
      m_Log.debug("Esto es el applet......................." + urlApplet);
    %>
    

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="6" />
     <!-- Ficheros JavaScript -->
     <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
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
        
        
        function genera(){
        var doc = document.open("text/html","replace");
        var texto="<html><body>hola</html></body>";
        doc.write(texto);
        doc.close();
        
}
        
        

    </script>

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
    <html:hidden property="nombreDocumento"/>
    <html:hidden property="opcion"/>
    <html:hidden property="datosXML"/>
    <html:hidden property="codMunicipio"/>
    <html:hidden property="opcionGrabar"/>

    <input type="Hidden" name="sessionID" value="<%=session.getId()%>">
    </html:form>
    


<applet name="appletOpenOffice" codebase='<%=urlApplet%>'
        code="es.altia.openOffice.AppletOpenOffice" archive="appletOOffice.jar" 
        width="1250" height="800">
                <param name="estado" value="relacion"/>               
                <param name="ver" value='<%=ver%>'/>     
                <param name="xml" value='<%=xml%>'/>
                <param name="idioma" value='<%=idioma%>'/>
                <param name="codPlantilla" value='<%=daForm2.getCodPlantilla()%>'/> 
                <param name="urlServlet" value='<%=servlet%>'/>            
                <param name="parametro0" value='<%=parametro0%>'/>
                <param name="parametro6" value='<%=parametro6%>'/>   
                <param name="municipio" value='<%=daForm2.getCodMunicipio()%>'/>
                <param name="numExpediente" value='<%=daForm2.getNumeroRelacion().replace("/","+")%>'/>
                <param name="codTramite" value='<%=daForm2.getCodTramite()%>'/>
                <param name="ocurrencia" value='<%=daForm2.getOcurrenciaTramite()%>'/>
                <param name="numDocumento" value='<%=numDocumento%>'/>
                <param name="nombreFichero" value='<%=daForm2.getNombreDocumento()%>'/>
                <param name="scriptable" value="true"/>
            </applet>


 </body>
</html:html>

 <script type="text/javascript">

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
            document.forms[0].action="<c:url value='/sge/TramitacionRelacionExpedientes.do'/>";
            document.forms[0].submit();
            clearTimeout(t);
        }
    x=1;    
    }
}
comprobarApplet();
    
</script>
