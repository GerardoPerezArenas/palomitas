<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Oculto lista de expedientes relacionados </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma=1;
      int apl=1;
            if (session.getAttribute("usuario") != null){
                    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                    idioma = usuarioVO.getIdioma();
                    apl = usuarioVO.getAppCod();
            }
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript">
    var datos = new Array();
    var datosOriginal = new Array();
    var cont = 0;
    function redirecciona(){
      var respOpcion = "<bean:write name="ConsultaExpedientesForm" property="respOpcion"/>";
      if(respOpcion == "NoEliminadoExpRel") {
        parent.mainFrame.noSePudoEliminar();
      } else if(respOpcion == "eliminarExpRel") {
        <logic:iterate id="elemento" name="ConsultaExpedientesForm" property="listaExpedientesRel">
          datos[cont] = ["<bean:write name="elemento" property="numeroExpedienteRel"/>",
                       "<bean:write name="elemento" property="descProcedimiento"/>",
                       "<bean:write name="elemento" property="titular"/>",
                       "<bean:write name="elemento" property="fechaInicio"/>",
                       "<bean:write name="elemento" property="fechaFin"/>"];
          datosOriginal[cont] = ["<bean:write name="elemento" property="codMunicipioRel"/>",
                       "<bean:write name="elemento" property="ejercicioRel"/>",
                       "<bean:write name="elemento" property="numeroExpedienteRel"/>",
                       "<bean:write name="elemento" property="codProcedimiento"/>",
                       "<bean:write name="elemento" property="descProcedimiento"/>",
                       "<bean:write name="elemento" property="titular"/>",
                       "<bean:write name="elemento" property="fechaInicio"/>",
                       "<bean:write name="elemento" property="fechaFin"/>",
                       "<bean:write name="elemento" property="codMunicipio"/>",
                       "<bean:write name="elemento" property="ejercicio"/>",
                       "<bean:write name="elemento" property="numeroExpediente"/>"];
          cont++;
        </logic:iterate>
        parent.mainFrame.cargarTabla(datos,datosOriginal);
      }
    }
    </script>
</head>
<body onLoad="redirecciona();">
    <form>
    <input type="hidden" name="opcion" value="">
    </form>
    <p>&nbsp;<p>
</body>
</html>
