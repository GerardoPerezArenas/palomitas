<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>


<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<title> Oculto Datos Usuarios </title>

<%
        int idioma=1;
        int apl=4;
        UsuarioValueObject usuarioVO;

        if (session.getAttribute("usuario") != null){
                usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
        }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
var codigos = new Array();
var descripciones = new Array();
var codigos2 = new Array();
var descripciones2 = new Array();

function refreshAfterDelUser() {
    document.forms[0].opcion.value="inicioAdmLocal";
    document.forms[0].target="oculto";
    document.forms[0].action="<%=request.getContextPath()%>/administracion/UsuariosGrupos.do";
    document.forms[0].submit();
}

function redirecciona(){
    var respOpcion ='<bean:write name="UsuariosGruposForm" property="respOpcion"/>';
    //alert("respOpcion="+respOpcion);
    if ( "insertarUsuario"==respOpcion ){
	  parent.mainFrame.devolver();
    } else if( "loginDuplicado"==respOpcion ){
        jsp_alerta('A','<%=descriptor.getDescripcion("msjLoginExist")%>');
    } else if( "errorTecnico"==respOpcion ){
        jsp_alerta('A','<%=descriptor.getDescripcion("msgErrorGenerico")%>');
        parent.mainFrame.devolver();
    } else if( "eliminarUsuario"==respOpcion ){
        refreshAfterDelUser();
	} else if( "modificarUsuario"==respOpcion ){
	  parent.mainFrame.devolver();
	} else if( "insertarGrupo"==respOpcion ){
	  parent.mainFrame.devolver();
	} else if( "eliminarGrupo"==respOpcion ){
	  parent.mainFrame.grupoEliminado();
	} else if( "modificarGrupo"==respOpcion ){
	  parent.mainFrame.devolver();
    } else if( "cargarUnidadesOrganicas"==respOpcion ){
      <logic:iterate id="elemento" name="UsuariosGruposForm" property="listaUnidadesOrganicas">
        codigos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
        descripciones['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
      </logic:iterate>
      parent.mainFrame.cargarUnidadesOrganicas(codigos,descripciones);
    } else if( "cargarUnidadesOrganicasYCargos"==respOpcion ){
        <logic:iterate id="elemento" name="UsuariosGruposForm" property="listaUnidadesOrganicas">
          codigos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
          descripciones['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
        </logic:iterate>
        parent.mainFrame.cargarUnidadesOrganicas(codigos,descripciones);
    } else if ( "errorNoSePuedeQuitarCapacidadFirmaDelegaciones" == respOpcion ) {
        jsp_alerta("A","<fmt:message key='Administracion.Mantenimiento.DatosUsuarios.ErrorNoSePuedeQuitarCapacidadFirmaDelegaciones'/>");
        parent.mainFrame.devolver();
    } else if ( "errorNoSePuedeQuitarCapacidadFirmaDocumentos" == respOpcion ) {
        jsp_alerta("A","<fmt:message key='Administracion.Mantenimiento.DatosUsuarios.ErrorNoSePuedeQuitarCapacidadFirmaDocumentos'/>");
        parent.mainFrame.devolver();
    } else if ( "errorNoSePuedeQuitarCapacidadFirmaPlantillas" == respOpcion ) {
        jsp_alerta("A","<fmt:message key='Administracion.Mantenimiento.DatosUsuarios.ErrorNoSePuedeQuitarCapacidadFirmaPlantillas'/>");
        parent.mainFrame.devolver();
    } else if (respOpcion == "errorValidacion") {
        <html:messages id="errorMessage" property="errorValidacion">
            jsp_alerta("A","<c:out value="${errorMessage}"/>");
        </html:messages>
    }else if ("errorTieneTramitacionRegistro" == respOpcion ) {
        jsp_alerta("A","<fmt:message key='Administracion.Mantenimiento.DatosUsuarios.ErrorTieneTramitacionRegistro'/>");
        refreshAfterDelUser();
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
