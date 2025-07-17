<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Vector" %>
<%@page import="es.altia.common.util.*"%>
<%	String opcion = (String)request.getAttribute("opcion");
	Registro parametros = null;	
	Vector vectorEtiquetas = null;
	if(opcion.trim().equals("VER_PLANTILLA")) {
		parametros = (Registro)request.getAttribute("datosPlantilla");
		vectorEtiquetas = (Vector)request.getAttribute("Etiquetas");
	}
	if (opcion == null) opcion ="";
	String host = parametros.getString("host");
	if(host == null) host = "";
	String sello = (String)request.getAttribute("sello");
	if(sello == null) sello = "";%>
<html><head><jsp:include page="/jsp/editor/tpls/app-constants.jsp" />
    <title>Oculto Generación de documentos Word</title>
    <%@ include file="/jsp/plantillas/Metas.jsp" %>
    <SCRIPT type="text/javascript">
    <% if(opcion.equals("VER_PLANTILLA")){%>
        function recuperarFormulario() {
            word = new ActiveXObject("word.application");
            var doc = word.documents.open('<%=host%>'+'documentos/temp/'+'<%=sello%>'+'.doc');
            <%-- Tenemos que sustituir las variables ya definidas con los nuevos valores. --%>
            <%-- Actualizamos las variables del documento recuperado --%>
            if (doc.Variables.Count  > 1) {
                var indices = (doc.Variables.Count -1 ) / 2;
                doc.Variables("tuplas").Value = '<%=vectorEtiquetas.size()%>';
                <%-- Primero limpiamos todos los valores de las variables que ya hay en el documento--%>
                var j=0;
                for(j=0;j < indices;j++) {
                    var nom = "nom"+j;
                    var desc = "desc"+j;
                    doc.Variables(desc).Value = "";
                    doc.Variables(nom).Value = "";
                }
            }
            word.Visible = true;
        }
    <%}%>
    </SCRIPT>
</head>
<body onLoad="recuperarFormulario();">
    <form action="" enctype="multipart/form-data" method="POST">
    </form>
</body></html>
