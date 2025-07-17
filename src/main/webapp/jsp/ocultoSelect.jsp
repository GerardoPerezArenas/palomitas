<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<html>
<head>
<title> Oculto </title>
<script type="text/javascript">
    var APP_CONTEXT_PATH="<%=request.getContextPath()%>";
    var array_cod  = new Array();
    var array_desc = new Array();
    var frame;
    if(parent.mainFrame){
        frame = parent.mainFrame;
    } else {
        frame = parent;
    }
    
    <logic:iterate id="elemento" name="SelectForm" property="lista_resultado">
        array_cod["<bean:write name="elemento" property="orden"/>"]  = "<bean:write name="elemento" property="codigo"/>";
        array_desc["<bean:write name="elemento" property="orden"/>"] = "<bean:write name="elemento" property="desc_c"/>";
    </logic:iterate>
    var marcoQActualiza='<bean:write name="SelectForm" property="target1"/>';
    if (marcoQActualiza==''){
        if(frame.cargarComboBox) {    
            frame.cargarComboBox(array_cod, array_desc);
        } else {            
            frame.inicializarValores('<bean:write name="SelectForm" property="input_cod"/>','<bean:write name="SelectForm" property="input_desc"/>', array_cod, array_desc);
        }
    }else{  
        if(eval(marcoQActualiza+".cargarComboBox")){
            eval(marcoQActualiza+".cargarComboBox(array_cod,array_desc)");
        } else{
            var a1 = '<bean:write name="SelectForm" property="input_cod"/>';
            var a2 = '<bean:write name="SelectForm" property="input_desc"/>';
            eval(marcoQActualiza+".inicializarValores('"+a1+"','"+a2+"',array_cod,array_desc)");
        }
    }
</script>
</head>
<body>
	<%session.removeAttribute("SelectForm");%>
	<P>&nbsp;</P><CENTER/>
</body>
</html>
