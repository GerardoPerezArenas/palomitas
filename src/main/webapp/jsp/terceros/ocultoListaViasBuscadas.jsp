<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.agora.business.terceros.mantenimiento.ViaEncontradaVO" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html>
<head><title>Oculto Lista Vias Buscadas</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
    <%
        UsuarioValueObject usuarioVO;
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
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">

    <c:if test="${not empty requestScope.errores}">
        var msjErrores = new Array();
        var i = 0;
        <c:forEach items="${requestScope.errores}" var="error">
            msjErrores[i++] = '<fmt:message key="BusquedaTercero/falloBusqueda"/> <c:out value="${error}"/>';
        </c:forEach>
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/erroresBusquedaTercero.jsp', msjErrores,
	'width=420,height=300,status=no',function(){});
    </c:if>
                
<%
    MantenimientosTercerosForm bForm =(MantenimientosTercerosForm)session.getAttribute("MantenimientosTercerosForm");

    Vector listaVias = new Vector();
    if (bForm != null && bForm.getListaViasBuscadas() != null){
        listaVias = bForm.getListaViasBuscadas();
    }
    int lengthVias = listaVias.size();
    String codProvincia="";
    String descProvincia="";
    String codMunicipio="";
    String descMunicipio="";
    String codVia="";
    String descVia="";
	String codTipoVia="";
	String descTipoVia="";
    String codECO="";
    String codESI="";
    String viaAlta = escape((String)request.getAttribute("descripcionViaAlta"));
    
	if(lengthVias >0) {
        ViaEncontradaVO via = (ViaEncontradaVO)listaVias.get(0);
        codVia = Integer.toString(via.getCodigoVia());

	  if(lengthVias ==1) {	   
            codProvincia = Integer.toString(via.getCodigoProvincia());
            descProvincia = escape(via.getDescProvincia());
            codMunicipio = Integer.toString(via.getCodigoMunicipio());
            descMunicipio = escape(via.getDescMunicipio());
            descVia = escape(via.getNombreVia());
            descTipoVia = escape(via.getDescTipoVia());
            codECO = "";
            codESI = "";
            codTipoVia = Integer.toString(via.getCodigoTipoVia());
	  }
	}

	%>
  var frame = parent.mainFrame;
  frame.cargarListaViasBuscadas("<%=lengthVias%>","<%=codVia%>","<%=codVia%>","<%=descVia%>","<%=descTipoVia%>","<%=codECO%>","<%=codESI%>","<%=codTipoVia%>","<%=codProvincia%>","<%=descProvincia%>","<%=codMunicipio%>","<%=descMunicipio%>","<%=viaAlta%>");


    </script>
</head>
<body></body>
</html>
