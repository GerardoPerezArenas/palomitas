<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html>
    <head>
<title>Oculto Modificar Tercero</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
// Funcion de esta jsp: cargar los combos de codigo postal y municipio al
// modificar un tercero.

var opcion="<%=request.getParameter("opcion")%>";
var frame=parent.mainFrame;

<%!
  // Funcion para escapar strings para javascript
  private String escape(String str) {
      return StringEscapeUtils.escapeJavaScript(str);
  }
%>

    <%
      BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
      Vector listaMunicipios = bForm.getListaMunicipios();
      int lengthMun = listaMunicipios.size();

      String lcodMun = "";
      String ldescMun = "";
      int i;

      if(lengthMun>0) {
        for(i=0;i<lengthMun-1;i++){
            GeneralValueObject  mun= (GeneralValueObject)listaMunicipios.get(i);
            lcodMun+= "'" + mun.getAtributo("codMunicipio")+"',";
            ldescMun+= "\""+ escape((String) mun.getAtributo("nombreOficial")) +"\",";
        }
        GeneralValueObject  mun = (GeneralValueObject)listaMunicipios.get(i);
        lcodMun+= "'"+ mun.getAtributo("codMunicipio")+"'";
        ldescMun+= "\""+ escape((String) mun.getAtributo("nombreOficial")) +"\"";
      }
    %>

    codMunicipios = [<%= lcodMun %>];
    descMunicipios = [<%= ldescMun %>];
    var j=0;
    codPostales = new Array();
    <%
      Vector listaCPostales = bForm.getListaCodPostales();
      int lengthCPostales = listaCPostales.size();
      for(i=0;i<lengthCPostales;i++){ %>
          codPostales[j] = "<%=((GeneralValueObject)listaCPostales.get(i)).getAtributo("codPostal")%>";
          j++;
    <%}%>

    frame.cargarCombosModificar(codMunicipios, descMunicipios, codPostales);

</script>

</head>
<body>
</body>

</html>
