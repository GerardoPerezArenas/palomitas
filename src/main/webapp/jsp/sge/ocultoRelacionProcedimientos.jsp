<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.sge.DefinicionProcedimientosValueObject" %>

<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title> Relación Procedimientos</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script>
    function redirecciona() {
    <% 	Vector relacionProcedimientos = (Vector) session.getAttribute("RelacionProcedimientos");
       int numRelacionProcedimientos = 0;
       int posProcedimiento = 0;
       DefinicionProcedimientosValueObject dFVO = new DefinicionProcedimientosValueObject();
       String codMunicipio="";
       String codProcedimiento="";

      if ( relacionProcedimientos != null ) {
        numRelacionProcedimientos = relacionProcedimientos.size();
        if (numRelacionProcedimientos >0){
          dFVO = (DefinicionProcedimientosValueObject) relacionProcedimientos.elementAt(posProcedimiento);
          codMunicipio = dFVO.getCodMunicipio();
          codProcedimiento = dFVO.getTxtCodigo();
        }
      }
    %>
       parent.mainFrame.procedimientoAprocedimiento(<%=numRelacionProcedimientos%>,'<%=codMunicipio%>','<%=codProcedimiento%>',<%=posProcedimiento+1%>)
    }
    </script>
</head>
<body onLoad="redirecciona();">
    <p>&nbsp;<p><center>
</body>
</html>
