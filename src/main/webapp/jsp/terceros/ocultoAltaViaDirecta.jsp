<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html><head>
<title>Oculto Alta Via Directa</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<%UsuarioValueObject usuarioVO = new UsuarioValueObject();
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
<%  MantenimientosTercerosForm bForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");
    int i = 0;
    Vector listaTVias = bForm.getListaTipoVias();
    int lengthTVias = listaTVias.size();
	String codTipoVia="";
	String descTipoVia="";
    String descripcionVia = "";
	// Nombre dado a la via para pasarla a altaViaDirecta.jsp y cubrir los campos con dicho valor
    descripcionVia = escape((String)request.getAttribute("descripcionViaAlta"));

	if(lengthTVias >0) {
      for(i=0;i<lengthTVias-1;i++){
	      GeneralValueObject tvias = (GeneralValueObject)listaTVias.get(i);
	      codTipoVia+="\""+(String)tvias.getAtributo("codTipoVia")+"\",";
		  descTipoVia+="\""+escape((String)tvias.getAtributo("descTipoVia"))+"\",";
	  }
	  GeneralValueObject tvias = (GeneralValueObject)listaTVias.get(i);
      codTipoVia+="\""+tvias.getAtributo("codTipoVia")+"\"";
      descTipoVia+="\""+ escape((String) tvias.getAtributo("descTipoVia")) +"\"";
	}
%>
var frame = parent.mainFrame;
frame.altaViaDirectaIniciada([<%=codTipoVia%>],[<%=descTipoVia%>],'<%=descripcionVia%>');
</script></head><body></body></html>
