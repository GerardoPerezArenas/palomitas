<!doctype html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map,
                                                                             java.util.Vector" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.*" %>
<%@page import="es.altia.util.struts.StrutsUtilOperations" %>

<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=1;
  int apl=3;
  String[] params=null;

  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
    params = usuarioVO.getParamsCon();
  }

  String arrayJScript =null;

  String estado=((request.getParameter("op")!=null) && (request.getParameter("op").equals("A")))?"ENGADIR":((request.getParameter("op").equals("M"))?"MODIFICAR":"CONSULTA");
  String cod=request.getParameter("id");
  Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
  String macroPlantillas = m_ConfigTechnical.getString("macroPlantillas");

  String macroPlantillas = m_ConfigTechnical.getString("macroPlantillas");
  String hostVirtual = m_ConfigTechnical.getString("hostVirtual");
     String dominioFinal = "";
    if(hostVirtual==null || hostVirtual.length()==0) {
        dominioFinal = StrutsUtilOperations.getProtocol(request) +"://" + request.getHeader("Host") + request.getContextPath();
    } else {
        dominioFinal = hostVirtual+ request.getContextPath();
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />


<html:html>
<head>
    <title>Edición de plantillas</title>

    <jsp:include page="/jsp/informes/tpls/app-constants.jsp" />
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE10"/>
</head>
<body>  <!--onLoad="cargarFormulario();" -->
    <html:form enctype="multipart/form-data" action="/XeradorInformesAction.do" method="POST" target="oculto" styleId="XeradorInformesForm" >
         <html:hidden property="textOperacion" styleId="textOperacion" />
         <html:hidden property="COD_INFORMEXERADOROculto" styleId="COD_INFORMEXERADOROculto" />
         <input type="hidden" name="nombreDocumento" value="">
         <input type="Hidden" name="sessionID" value="<%=session.getId()%>">
    </html:form>
    <script type="text/vbscript" language="VBScript" src="<c:url value='/scripts/documentos.vbs'/>"></script>
    <script type="text/javascript">
    var vectorEtiquetas;

    function callbackpulsarGuardar(cod){
    }

    function Inicio(){
        cargaVectorEtiquetas();
        <%
        es.altia.util.HashtableWithNull tempTabla=new es.altia.util.HashtableWithNull();
        String urlDocumento="\"\"";
        String protocolo = StrutsUtilOperations.getProtocol(request);                       
        if (es.altia.agora.business.geninformes.utils.UtilidadesXerador.tienePlantillaInforme(params,cod))
            urlDocumento="\"" + protocolo + "://" + request.getHeader("Host") + request.getContextPath() + "/temp/documentosGenerador/"+params[0]+"/"+params[6]+"/Documento"+cod+".doc\"";
        %>
        //ejecutaPlantilla(<%=urlDocumento%>,vectorEtiquetas);
        document.forms[0].nombreDocumento.value = '<%=request.getParameter("nombrePlantilla")%>';
        callEjecutaPlantilla()
    }

    function cargaVectorEtiquetas(){
    // Contiene un array de [CENCOD, CENNOM ]
    <%
        arrayJScript =null;
        es.altia.util.conexion.Cursor cursorEtiquetas=null;
        try
        {
            // es.altia.util.Debug.crearDirectorio();
            //
            //
             tempTabla= new es.altia.util.HashtableWithNull();
            //String pestanaOrixe=request.getParameter(ConstantesXerador.COD_PESTANAORIXE);
            tempTabla.put("COD_INFORMEXERADOR",cod);//USUARIO.getCOD_PESTANA());
            tempTabla.put("PARAMS",params);//USUARIO.getCOD_PESTANA());
            //es.altia.util.Debug.println("EN JSPIII MNUCOD ES:"+pestanaOrixe+".");
            //arrayJScript =es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerArrayJSEtiquetasPlantillaInforme(tempTabla);
            cursorEtiquetas=es.altia.agora.business.geninformes.utils.UtilidadesXerador.ObtenerCursorEtiquetasPlantillaInforme(tempTabla);

        }
        catch (Exception e)
        {
            //es.altia.util.Debug.printException(e.getExcepcion());
        }
    %>
    //vectorEtiquetas=unescapeArray(<%=arrayJScript%>);
    }

    function cargaDocumentos(){
    }
    </script>
    <script type="text/vbscript" language="VBScript">
    Sub callEjecutaPlantilla()
    <%
        int n=0;
        int i=0;
        String strEtiquetas = "";
        String strValores   = "";
        if (cursorEtiquetas!=null) n=cursorEtiquetas.numTuplas();
        if (cursorEtiquetas!=null) {
            cursorEtiquetas.next();
            strEtiquetas += cursorEtiquetas.getString("NOME");
            strValores   += cursorEtiquetas.getString("CODIGO");
            while (cursorEtiquetas.next()) {
               strEtiquetas += ","+cursorEtiquetas.getString("NOME");
               strValores   += ","+cursorEtiquetas.getString("CODIGO");
            }
        }
    %>
        etiquetas = "<%=strEtiquetas%>"
        valores   = "<%=strValores%>"
        document.forms(0).textOperacion.value="GEP"
        document.forms(0).COD_INFORMEXERADOROculto.value="<%=cod%>"                      
        sURL = "<%=dominioFinal%>"  & "<html:rewrite page='/XeradorInformesAction.do'/>" & _
                  ";jsessionid=" & document.forms(0).sessionID.value & _
                  "?operacion=GEP&" & _
                  "textOperacion=" & document.forms(0).textOperacion.value & "&" & _
                  "COD_INFORMEXERADOROculto=" & document.forms(0).COD_INFORMEXERADOROculto.value & "&" & _
                  "nombreDocumento=" & document.forms(0).nombreDocumento.value
       // MsgBox sURL
        
        Call ejecutaPlantilla(<%=urlDocumento%>,sURL,etiquetas,valores,"<%= dominioFinal %>")
    End Sub
    </script>
    <script type="text/javascript">
    Inicio();
    </script>
</body>
</html:html>
