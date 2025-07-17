<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<html>
<head>
<title> Error Alta</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/jquery/jquery-1.9.1.min.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript">

function redirecciona(){
	
	var opcion = '<bean:write name="MantAnotacionRegistroForm" property="respOpcion"/>';
	var retramitarDocumentosDetalleOPeracion = '<bean:write name="MantAnotacionRegistroForm" property="retramitarDocumentosDetalleOPeracion"/>';

	if (opcion== 'error'){
            pleaseWait('off');
		jsp_alerta("A", '<%=descriptor.getDescripcion("msjErrorAltaReg")%>');
                parent.mainFrame.activarFormulario();
            parent.mainFrame.modificando('S');
	} else if (opcion == 'errorModify') {
            pleaseWait('off');
            jsp_alerta("A", '<%=descriptor.getDescripcion("msjErrorModifReg")%>');
            parent.mainFrame.activarFormulario();
            parent.mainFrame.modificando('S');
	} else if(opcion == 'errorFinDigit'){
            pleaseWait('off');
            jsp_alerta("A","Error al cambiar de contexto los Documentos asociados de Registro a Trámitación");
            parent.mainFrame.activarFormulario();
            parent.mainFrame.modificando('S');
            parent.mainFrame.$("[name='cmdFinDigitalizarMod']").show();
        } else if(opcion == 'errorModificarProcedimiento'){
            pleaseWait('off');
            jsp_alerta("A","Error al modificar el procedimiento");
            parent.mainFrame.activarFormulario();
            parent.mainFrame.modificando('S');
        } else if (opcion == 'errorModifyRetramitar') {
            pleaseWait('off');
            jsp_alerta("A", '<%=descriptor.getDescripcion("msjErrorModifRegRetramitar")%>' + ". " + retramitarDocumentosDetalleOPeracion);
            parent.mainFrame.activarFormulario();
            parent.mainFrame.modificando('S');
        } //else if (opcion == 'errorAltaSalidaSIR') {
//            pleaseWait('off');
//            jsp_alerta("A", '< %=descriptor.getDescripcion("msgErrorAltaSalidaSIR")%>' + ". " + retramitarDocumentosDetalleOPeracion);
//            parent.mainFrame.activarFormulario();
//            parent.mainFrame.modificando('S'); 
//	}
}
</script>

</head>
<body onLoad="redirecciona();">

<p>&nbsp;<p><center/>
        

</body>
</html>
