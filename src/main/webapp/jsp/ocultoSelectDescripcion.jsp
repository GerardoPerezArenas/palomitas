<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<html>
<head>
<title> Oculto </title>
<script type="text/javascript">
   var APP_CONTEXT_PATH="<%=request.getContextPath()%>";
   descripcion = '<bean:write name="SelectForm" property="valor_desc"/>';
   var marcoQActualiza='<bean:write name="SelectForm" property="target1"/>';
   if (marcoQActualiza=='')
   	parent.mainFrame.updateDescripcion(descripcion,'<bean:write name="SelectForm" property="input_desc"/>','<bean:write name="SelectForm" property="input_cod"/>');
   else {   	
   	var a1 = '<bean:write name="SelectForm" property="input_desc"/>';
   	var a2 = '<bean:write name="SelectForm" property="input_cod"/>';
   	eval(marcoQActualiza+".updateDescripcion(descripcion,'"+a1+"','"+a2+"')");
   }
</script>
</head>
<body>
<p>&nbsp;</p><center/>
</body>
</html>
