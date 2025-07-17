<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<html>
<head>
<title> Datos Persona </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<script type="text/javascript">
var codProcedimientos = new Array();
var descProcedimientos = new Array();
var munProcedimientos = new Array();
var i = 0;
var frame;
if(parent.mainFrame){
    frame = parent.mainFrame;
} else {
    frame = parent;
}

function redirecciona(){
   <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaProcedimientos">
	 codProcedimientos[i]='<bean:write name="elemento" property="txtCodigo"/>';
     descProcedimientos[i]='<bean:write name="elemento" property="txtDescripcion"/>';
     munProcedimientos[i]='<bean:write name="elemento" property="codMunicipio"/>';
	 i++;
    </logic:iterate>
   frame.recuperaListaProcedimientosExpRel(codProcedimientos,descProcedimientos,munProcedimientos);
}
</script>
</head>
<body onLoad="redirecciona();">
<p>&nbsp;</p><center/>
</body>
</html>
