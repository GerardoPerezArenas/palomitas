<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.escritorio.RegistroUsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.util.ValidarDocumento" %>
<%@page import="java.util.Vector" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@ page import="es.altia.util.conexion.AdaptadorSQLBD"%>
<%@ page import="es.altia.util.conexion.AdaptadorSQL"%>
<%@ page import="es.altia.agora.webservice.registro.pisa.regexterno.controller.AnotacionRegistroExternoForm" %>
<html:html>    
    <head>
        
        <TITLE>::: Registro de salida en Accede :::</TITLE>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

        <%
            //=================================================================================================
            MantAnotacionRegistroForm mantARForm;
            if (request.getAttribute("MantAnotacionRegistroForm") != null) {
                mantARForm = (MantAnotacionRegistroForm) request.getAttribute("MantAnotacionRegistroForm");
            } else {
                mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
            }
            //=================================================================================================
            //=================================================================================================
            AnotacionRegistroExternoForm anotacionForm;
            if (request.getAttribute("AnotacionRegistroExternoForm") != null) {
                anotacionForm = (AnotacionRegistroExternoForm) request.getAttribute("AnotacionRegistroExternoForm");
            } else {
                anotacionForm = (AnotacionRegistroExternoForm) session.getAttribute("AnotacionRegistroExternoForm");
            }
            //=================================================================================================            

            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            RegistroUsuarioValueObject regUsuarioVO = new RegistroUsuarioValueObject();
            Config m_Conf = ConfigServiceHelper.getConfig("common");
            String JSP_poblacion = m_Conf.getString("JSP.Poblacion");
            String JSP_autoridad = m_Conf.getString("JSP.Registro.Autoridad");
            String pv = "visible";
            if ("no".equals(JSP_poblacion)) {
                pv = "hidden";
            }
            String aut = "visible";
            if ("no".equals(JSP_autoridad)) {
                aut = "hidden";
            }


            int idioma = 1;
            int apl = 97;
            int cod_org = 0;
            int cod_dep = 1;
            int cod_ent = 1;
            int cod_unidOrg = 1;
            boolean permisoMantenimiento = false;
            boolean permisoMantenimientoSalida = false;
            String desc_org = "";
            String desc_ent = "";
            String respOpcion = "";
            String funcion = "";
            String dil = "";
            String deBuzonRechazadas = "";


            String idSesion = session.getId();

            if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                regUsuarioVO = (RegistroUsuarioValueObject) session.getAttribute("registroUsuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                cod_org = usuarioVO.getOrgCod();
                cod_dep = regUsuarioVO.getDepCod();
                cod_ent = usuarioVO.getEntCod();
                cod_unidOrg = regUsuarioVO.getUnidadOrgCod();
                desc_org = usuarioVO.getOrg();
                desc_ent = usuarioVO.getEnt();
            }
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
        %>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
           <script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/tabpane.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listaBusquedaTerceros.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<html:rewrite page='/css/estilo.css" media="screen'/>">
        <script type="text/javascript" src="<html:rewrite page='/scripts/altaRE.js'/>"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/uor.js"></script>
        <script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">
function salir(){
	var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');
	if (resultado == 1){
		document.forms[0].target = '_top';
      	document.forms[0].action = '<c:url value='/SalirApp.do?app='/><%= usuarioVO.getAppCod()%>';
      	document.forms[0].submit();
	}
}

function pulsarNuevaAlta() {
    document.forms[0].opcion.value = "cargarDefecto";
    document.forms[0].target="mainFrame";
    document.forms[0].action="<html:rewrite page='/registroExterno/RegistroExterno.do'/>";
    document.forms[0].submit();      
}
</script>
        
</head>    
<BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
       }">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>        
<html:form action="/registroExterno/AltaRegistroExterno.do" target="_self">
    <html:hidden  property="opcion" value=""/>
    <div class="txttitblancoder"><%=descriptor.getDescripcion("tit_AnotRealiz")%></div>
    <div class="contenidoPantalla">
        <table border ="1" height="35%" width="55%" align="center">
            <th style="background-color: #DCDCCC;font-family:Arial, Verdana, Helvetica, sans-serif">Datos de Alta</th>
            <tr>                                                        
                <td style="font-family:Arial, Verdana, Helvetica, sans-serif;padding-left: 20px;padding-right: 3px" width="100%" height="100%" bgcolor="#FFFFFF">
                    La anotación ha sido dada de alta correctamente en el registro de Accede con el número 
                    <bean:write name="AnotacionRegistroExternoForm" property="numeroAnotacion"/> y fecha <bean:write name="AnotacionRegistroExternoForm" property="fechaAnotacion"/>
                    a las <bean:write name="AnotacionRegistroExternoForm" property="horaMinAnotacion"/>
                </td>                                                                                                              
            </tr>
        </table>                                                                                                
        <div id="capaBotones2" name="capaBotones2" STYLE=" height:0px;" class="botoneraPrincipal">
            <input type="button" title='Alta'
                   class="botonGeneral" value='Alta'
                   name="cmdRegistrarAlta" onClick="pulsarNuevaAlta();return false;">
            <input type="button" title='Salir'
                   class="botonGeneral" value='Salir'
                   name="cmdCancelarAlta" onClick="salir();return false;"/>
        </div>
    </div>
</html:form>   
</body>     
</html:html>    
