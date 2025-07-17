<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>


<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<title> Oculto Autorizacion Externas </title>

<%

        int idioma=1;
        int apl=4;
        UsuarioValueObject usuarioVO = new UsuarioValueObject();

        if (session.getAttribute("usuario") != null){
                usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
        }

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script src="<%=request.getContextPath()%>/scripts/general.js"></script>

<script>

var listaEntidades = new Array();
var listaEntidadesOriginal = new Array();
var listaAplicaciones = new Array();
var listaAplicacionesOriginal = new Array();
var listaEnt = new Array();
var listaEntOriginal = new Array();
var cont = 0;
var cont1 = 0;
var cont2 = 0;
  
  function redirecciona(){
    var respOpcion ='<bean:write name="AutorizacionesExternasForm" property="respOpcion"/>';
    if ( "buscarEntidades"==respOpcion ){
	  <logic:iterate id="elemento" name="AutorizacionesExternasForm" property="listaEntidades">
        var aut = '<bean:write name="elemento" property="autorizacion" />';
	    var checkEntreg1 = "<input type='checkbox' class='check' name='boxE" + cont + "' value='SI' ";
        if(aut == "si") {
	      checkEntreg1 += "checked ";
	    }
	    checkEntreg1 += ">";
        listaEntidades[cont] = [checkEntreg1,
	                             '<bean:write name="elemento" property="nombreOrganizacion" />',
								 '<bean:write name="elemento" property="nombreEntidad" />',
								 '<bean:write name="elemento" property="baseDeDatos" />'];
        listaEntidadesOriginal[cont] =[checkEntreg1,
	                             '<bean:write name="elemento" property="nombreOrganizacion" />',
								 '<bean:write name="elemento" property="codOrganizacion" />',
								 '<bean:write name="elemento" property="nombreEntidad" />',
								 '<bean:write name="elemento" property="codEntidad" />',
								 '<bean:write name="elemento" property="autorizacion" />',
								 '<bean:write name="elemento" property="baseDeDatos" />'];
        cont++;
      </logic:iterate>
	  parent.mainFrame.actualizarTablaEntidades(listaEntidades,listaEntidadesOriginal);
    } else if( "buscarAplicacionesUsuarios"==respOpcion ){
	  <logic:iterate id="elemento" name="AutorizacionesExternasForm" property="listaAplicaciones">
        var aut = '<bean:write name="elemento" property="autorizacion" />';
	    var checkEntreg1 = "<input type='checkbox' class='check' name='boxA" + cont1 + "' value='SI' ";
        if(aut == "si") {
	      checkEntreg1 += "checked ";
	    }
	    checkEntreg1 += ">";
        listaAplicaciones[cont1] = [checkEntreg1,
	                             '<bean:write name="elemento" property="nombreAplicacion" />'];
        listaAplicacionesOriginal[cont1] =[checkEntreg1,
	                             '<bean:write name="elemento" property="codAplicacion" />',
								 '<bean:write name="elemento" property="codUsuario" />',
								 '<bean:write name="elemento" property="nombreAplicacion" />',
								 '<bean:write name="elemento" property="autorizacion" />'];
        cont1++;
      </logic:iterate>
	  parent.mainFrame.actualizarTablaAplicaciones(listaAplicaciones,listaAplicacionesOriginal);
	} else if( "buscarEntidadesUsuarios"==respOpcion ){
	  <logic:iterate id="elemento" name="AutorizacionesExternasForm" property="listaEnt">
        var aut = '<bean:write name="elemento" property="autorizacion" />';
	    var checkEntreg1 = "<input type='checkbox' class='check' name='boxEU" + cont2 + "' value='SI' ";
        if(aut == "si") {
	      checkEntreg1 += "checked ";
	    }
	    checkEntreg1 += ">";
        listaEnt[cont2] = [checkEntreg1,
	                             '<bean:write name="elemento" property="nombreOrganizacion" />',
								 '<bean:write name="elemento" property="nombreEntidad" />'];
        listaEntOriginal[cont2] =[checkEntreg1,
	                             '<bean:write name="elemento" property="nombreOrganizacion" />',
								 '<bean:write name="elemento" property="codOrganizacion" />',
								 '<bean:write name="elemento" property="nombreEntidad" />',
								 '<bean:write name="elemento" property="codEntidad" />',
								 '<bean:write name="elemento" property="autorizacion" />'];
        cont2++;
      </logic:iterate>
	  parent.mainFrame.actualizarTablaEnt(listaEnt,listaEntOriginal);
	} else if( "grabarListas"==respOpcion ){
	  parent.mainFrame.listasGrabadas();
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
