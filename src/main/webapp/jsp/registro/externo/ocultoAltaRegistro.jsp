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
        <link rel="stylesheet" type="text/css" href="<html:rewrite page='/css/estilo.css" media="screen'/>">
        
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
        %>
        <script type="text/javascript">
            var codProcs = new Array();
            var descProcs = new Array();
            function redirecciona() {
                var i = 0;
                <c:forEach var="elemento" items="${requestScope.comboProcs.elementosCombo}">
                    codProcs[i]='<c:out value="${elemento.codigoElemento}"/>';
                    descProcs[i]='<c:out value="${elemento.descripcionElemento}"/>';
                    i++;
                </c:forEach>
                parent.mainFrame.rellenaListasProcs(codProcs, descProcs);
                
            }
        </script>
    </head>
    <body onLoad="redirecciona();">

    <form>
    <input type="hidden" name="opcion" value="">
    </form>

    <p>&nbsp;<p><center>


    </body>    
</html:html>