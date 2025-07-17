<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <TITLE>Oculto Buzon de Entrada</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
    <%@page import="es.altia.agora.business.sge.ConfInformeValueObject" %>
    <%@page import="es.altia.agora.interfaces.user.web.sge.ConfInformeForm" %>
    <%@page import="java.util.Vector"%>

    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma=2;
        int apl=4;

        if(session.getAttribute("usuario") != null){
            usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
        }

        ConfInformeForm informeForm = new ConfInformeForm();
        ConfInformeValueObject hVO = new ConfInformeValueObject();

        Vector opciones = new Vector();

        if(session.getAttribute("ConfInformeForm") != null){
            informeForm = (ConfInformeForm)session.getAttribute("ConfInformeForm");
            opciones = (Vector)informeForm.getOpciones();
        }
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
        type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor" property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor" property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
</head>
<BODY>
  <SCRIPT>
    var lStrIdCombo = parent.mainFrame.mIntCombo;

    var lObjCombo = eval('parent.mainFrame.document.getElementById("select' + lStrIdCombo + '")');


    var j = 0;
    var tipos = new Array();
    var tiposD = new Array();

<%	 
    for(int i=0; i < opciones.size(); i++){ %>
      tipos[j] = '<%=(String)((ConfInformeValueObject)opciones.get(i)).getTipoProcedimiento()%>';
      tiposD[j] = '<%=(String)((ConfInformeValueObject)opciones.get(i)).getTipoProcedimientoD()%>';
      j++;
<%  } %>

    // vaciar el combo -->

    if (lObjCombo.options.length > 0){
      for (var i = lObjCombo.options.length - 1; i >= 0; i--){
        lObjCombo.remove(i);
      }
    }

    // cargar el combo -->

    lObjCombo.appendChild(new Option('(todos)', -1, false, false));

    for (var i = 0; i < tiposD.length; i++){
      lObjCombo.appendChild(new Option(tiposD[i], tipos[i], false, false));
    }
    parent.mainFrame.pleaseWait('off');
  </SCRIPT>
</BODY>
</html:html>







