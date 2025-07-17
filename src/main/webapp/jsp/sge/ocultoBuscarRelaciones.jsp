<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>


<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.sge.RelacionExpedientesValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.RelacionExpedientesForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.sge.TramitacionValueObject"%>
<%@ page import="es.altia.agora.interfaces.user.web.sge.TramitacionForm"%>
<%@ page import="es.altia.agora.business.sge.FichaRelacionExpedientesValueObject"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Cargar listas de tramites en el combo </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />


    <%UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma=1;
        int apl=1;
        if (session.getAttribute("usuario") != null){
            usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
        }
        Config m_Config = ConfigServiceHelper.getConfig("common");
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

    <script type="text/javascript">

    function redirecciona() {
        <%// RELACIONES
        Vector relaciones = (Vector) session.getAttribute("RelacionRelaciones");
        int numRelaciones = relaciones.size();
         if ( numRelaciones > 0 ) {
          for (int iE=0; iE< numRelaciones; iE++ ) {
            FichaRelacionExpedientesValueObject fichaRel = (FichaRelacionExpedientesValueObject) relaciones.elementAt(iE);
            String m = fichaRel.getCodMunicipio();
            String p = fichaRel.getCodProcedimiento();
            String e = fichaRel.getEjercicio();
            String n = fichaRel.getNumeroRelacion();
            String fI = fichaRel.getFechaInicio();
            String uI = fichaRel.getUsuarioInicio();
            String tI = fichaRel.getTramiteInicio();
            String tA = fichaRel.getTramiteActual();
            String bloqUsuCod = fichaRel.getCodBloqueo();
            String bloqUsuNom = fichaRel.getNomBloqueo();
            String bloqueo = "";
            String lock = "";
            if (!bloqUsuCod.equals("")){
                String img = "OFF";
                if (String.valueOf(usuarioVO.getIdUsuario()).equals(bloqUsuCod)) img = "ON";
                lock = "<img src=" + request.getContextPath() + "/images/lock" + img + ".jpg alt=" + bloqUsuNom + " />";
                if (!bloqUsuCod.equals(String.valueOf(usuarioVO.getIdUsuario()))) { //Si no está bloqueado por el usuario entonces bloquear expediente
                    bloqueo = "1";
                }
            }%>
            parent.mainFrame.listaOriginalE[<%= iE %>]  = ["<%= m %>","<%= p %>","<%= e %>","<%= n %>",
                                                                     "<%= fI %>","<%= uI %>","<%= tI %>","<%=tA%>","<%=bloqueo%>"];
            parent.mainFrame.listaE[<%= iE %>]  = ["<%=m_Config.getString("constante.relacion")%><%=n%>","<%=fI%>","<%=uI%>","<%=tI%>","<%=tA%>",
                                                             "<%=lock%>"];
         <%}
        }%>
        parent.mainFrame.tabRel.lineas=parent.mainFrame.listaE;
        parent.mainFrame.refrescaRelaciones();
    }
    </script>
</head>
<body onLoad="pleaseWait('off');redirecciona();">


</body>
</html>