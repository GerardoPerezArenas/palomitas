<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.interfaces.user.web.sge.TablasIntercambiadorasForm" %>
<%@page import="es.altia.agora.business.sge.TablasIntercambiadorasValueObject" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<title> Cargar listas de tramites en las condiciones de salida </title>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma=1;
        int apl=1;

        if (session.getAttribute("usuario") != null){
            usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
            idioma = usuarioVO.getIdioma();
            apl = usuarioVO.getAppCod();
        }
        TablasIntercambiadorasForm tabInterForm = (TablasIntercambiadorasForm) session.getAttribute("TablasIntercambiadorasForm");
        TablasIntercambiadorasValueObject tabInterTramSalVO = tabInterForm.getTramitesCondSal();
        TablasIntercambiadorasValueObject tabInterTramSSalVO = tabInterForm.getTramitesSCondSal();
        TablasIntercambiadorasValueObject tabInterTramNSalVO = tabInterForm.getTramitesNCondSal();
        String nCSTramite = tabInterTramSalVO.getNumeroCondicionSalida();
        String nCSTramiteS = tabInterTramSSalVO.getNumeroCondicionSalida();
        String nCSTramiteN = tabInterTramNSalVO.getNumeroCondicionSalida();
        String oblig = tabInterTramSalVO.getObligatorio();
        String obligS = tabInterTramSSalVO.getObligatorio();
        String obligN = tabInterTramNSalVO.getObligatorio();
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>

    <script type="text/javascript">

    function crearListasFlujoSalida(datosTabla) {
      var listaCodTramitesFlujoSalida = "";
      var listaNumerosSecuenciaFlujoSalida = "";
      var listaDescTramitesFlujoSalida = "";
      var listaNombresTramitesFlujoSalida = "";
      for (i=0; i < datosTabla.length; i++) {
        listaCodTramitesFlujoSalida += datosTabla[i][0]+'зе';
        listaNombresTramitesFlujoSalida += datosTabla[i][1]+'зе';
        listaDescTramitesFlujoSalida += datosTabla[i][2]+'зе';
        listaNumerosSecuenciaFlujoSalida += (i+1)+'зе';
      }
      var listasFlujoSalida = new Array();
      listasFlujoSalida = [listaCodTramitesFlujoSalida,listaNumerosSecuenciaFlujoSalida,listaDescTramitesFlujoSalida,listaNombresTramitesFlujoSalida];
      return listasFlujoSalida;
    }

    function redirecciona() {
    var cont1 = 0;
    var cont2 = 0;
    var cont3 = 0;
    var datosTramitesTabla = new Array();
    var datosTramitesSTabla = new Array();
    var datosTramitesNTabla = new Array();
    <%  if (nCSTramite!=null && nCSTramite.equals("0")) { %>
    <logic:iterate id="elemento" name="TablasIntercambiadorasForm" property="listaTramitesCondSalSeleccion">
      datosTramitesTabla[cont1] = ['<bean:write name="elemento" property="idTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="codTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="nombreTramiteFlujoSalida"/>'];
      cont1++;
    </logic:iterate>
    var listasFlujoSalida = crearListasFlujoSalida(datosTramitesTabla);
    var listaCodigos = listasFlujoSalida[0];
    var listaNombreCodigos = listasFlujoSalida[3];
    var listaDescripciones = listasFlujoSalida[2];
    var listaCodigosTramites = listasFlujoSalida[1];
    parent.mainFrame.document.forms[0].listaCodTramitesFlujoSalida.value = listaCodigos;
    parent.mainFrame.document.forms[0].listaNombreTramitesFlujoSalida.value = listaNombreCodigos;
    parent.mainFrame.document.forms[0].listaNumerosSecuenciaFlujoSalida.value = listaCodigosTramites;
    parent.mainFrame.document.forms[0].listaDescTramitesFlujoSalida.value = listaDescripciones;
    parent.mainFrame.document.forms[0].oblig.value = <%=oblig%>;
    parent.mainFrame.document.forms[0].numeroCondicionSalida.value = <%=nCSTramite%>;
    <% } else {%>
    <%  if (nCSTramiteS!=null && nCSTramiteS.equals("1")) { %>
    <logic:iterate id="elemento" name="TablasIntercambiadorasForm" property="listaTramitesCondSalSSeleccion">
      datosTramitesSTabla[cont2] = ['<bean:write name="elemento" property="idTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="codTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="nombreTramiteFlujoSalida"/>'];
      cont2++;
    </logic:iterate>
    var listasFlujoSalidaS = crearListasFlujoSalida(datosTramitesSTabla);
    var listaCodigosS = listasFlujoSalidaS[0];
    var listaNombreCodigosS = listasFlujoSalidaS[3];
    var listaDescripcionesS = listasFlujoSalidaS[2];
    var listaCodigosTramitesS = listasFlujoSalidaS[1];
    parent.mainFrame.document.forms[0].listaCodTramitesFlujoSalidaSiFavorable.value = listaCodigosS;
    parent.mainFrame.document.forms[0].listaNombreTramitesFlujoSalidaSiFavorable.value = listaNombreCodigosS;
    parent.mainFrame.document.forms[0].listaNumerosSecuenciaFlujoSalidaSiFavorable.value = listaCodigosTramitesS;
    parent.mainFrame.document.forms[0].listaDescTramitesFlujoSalidaSiFavorable.value = listaDescripcionesS;
    parent.mainFrame.document.forms[0].obligSiFavorable.value = <%=obligS%>;
    parent.mainFrame.document.forms[0].numeroCondicionSalidaSiFavorable.value = <%=nCSTramiteS%>;
    <% } %>

    <%  if (nCSTramiteN!=null && nCSTramiteN.equals("2")) { %>
    <logic:iterate id="elemento" name="TablasIntercambiadorasForm" property="listaTramitesCondSalNSeleccion">
      datosTramitesNTabla[cont3] = ['<bean:write name="elemento" property="idTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="codTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="nombreTramiteFlujoSalida"/>'];
      cont3++;
    </logic:iterate>
    var listasFlujoSalidaN = crearListasFlujoSalida(datosTramitesNTabla);
    var listaCodigosN = listasFlujoSalidaN[0];
    var listaNombreCodigosN = listasFlujoSalidaN[3];
    var listaDescripcionesN = listasFlujoSalidaN[2];
    var listaCodigosTramitesN = listasFlujoSalidaN[1];
    parent.mainFrame.document.forms[0].listaCodTramitesFlujoSalidaNoFavorable.value = listaCodigosN;
    parent.mainFrame.document.forms[0].listaNombreTramitesFlujoSalidaNoFavorable.value = listaNombreCodigosN;
    parent.mainFrame.document.forms[0].listaNumerosSecuenciaFlujoSalidaNoFavorable.value = listaCodigosTramitesN;
    parent.mainFrame.document.forms[0].listaDescTramitesFlujoSalidaNoFavorable.value = listaDescripcionesN;
    parent.mainFrame.document.forms[0].obligNoFavorable.value = <%=obligN%>;
    parent.mainFrame.document.forms[0].numeroCondicionSalidaNoFavorable.value = <%=nCSTramiteN%>;
    <% } %>
    <% } %>
    }
    </script>
</head>
<body onLoad="redirecciona();">

<form>
<input type="hidden" name="opcion" value="">
</form>

<p>&nbsp;<p><center>


</body>
</html>
