<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.*"%>
<%@page import="java.util.Vector"%>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>


<html>
<head>
<title>Oculto Alta Via Directa</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  int idioma=2;
  int apl=1;
  if ((session.getAttribute("usuario") != null)){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
  }
  %>

 <%!
    // Funcion para escapar strings para javascript
    private String escape(String str) {
        return StringEscapeUtils.escapeJavaScript(str);
    }
 %>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
<%
    MantenimientosTercerosForm bForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
    Vector listaVias = bForm.getListaViasBuscadas();
    int lengthVias = 0;
    if (listaVias!=null) lengthVias = listaVias.size();
    String idVia="";
    String codVia="";
    String descVia="";
	String codTipoVia="";
	String descTipoVia="";
    String codECO="";
    String codESI="";
    Log m_log = LogFactory.getLog(this.getClass().getName());
    if(m_log.isDebugEnabled()) m_log.debug(String.valueOf(lengthVias));
	if(lengthVias >0) { //Solo deberiamos tener una.
    if(m_log.isDebugEnabled()) m_log.debug("Una via encontrada");
	      GeneralValueObject vias = (GeneralValueObject)listaVias.get(0);
	      idVia=(String)vias.getAtributo("idVia");
	      codVia=(String)vias.getAtributo("codVia");
	      descVia=escape((String)vias.getAtributo("descVia"));
	      descTipoVia=escape((String)vias.getAtributo("descTipoVia"));
	      codECO=(String)vias.getAtributo("codECO");
	      codESI=(String)vias.getAtributo("codESI");
	      codTipoVia=(String)vias.getAtributo("codTipoVia");		
	}		
	String operacion = bForm.getOperacion();
  %>

  var frame = parent.mainFrame;  
  frame.respuestaAltaViaDirecta("<%=operacion%>","<%=idVia%>","<%=codVia%>","<%=descVia%>","<%=descTipoVia%>","<%=codECO%>","<%=codESI%>","<%=codTipoVia%>");

</script>
</head>

<body>

</body>
</html>
