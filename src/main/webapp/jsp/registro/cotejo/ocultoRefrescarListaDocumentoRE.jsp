<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>

<html>
<head>
    <jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
    <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
    <title>Oculto refrescar tablas de documentos de registro</title>
    <script type="text/javascript" src="<c:url value='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/miniapplet.js'/>"></script>
    <script type="text/javascript">
        function inicializar() {
            var ESTADO_DOCUMENTO_REGISTRO_ELIMINADO = '<%=ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO%>';
            // Se vuelve a crear la lista para mostrar en la tabla                
            var listaDocs = new Array();
            var cont=0;

            <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAsignados">
                var str='';

                if(('<bean:write name="elemento" property="entregado"/>'=='S')||(('<bean:write name="elemento" property="entregado"/>'=='SI'))) str='SI';
                else if(('<bean:write name="elemento" property="entregado"/>'=='N')||(('<bean:write name="elemento" property="entregado"/>'=='NO'))) str='NO';
                else str='';

                var estado = '<bean:write name="elemento" property="estadoDocumentoRegistro"/>';                    

                if(estado!==ESTADO_DOCUMENTO_REGISTRO_ELIMINADO){
                 listaDocs[cont]= [ str,"<str:escape><bean:write name="elemento" property="nombreDoc" filter="false"/></str:escape>",
                                    '<bean:write name="elemento" property="tipoDoc"/>', 
                                    '<bean:write name="elemento" property="fechaDoc"/>',
                    '<bean:write name="elemento" property="cotejado"/>',
                    '<bean:write name="elemento" property="doc"/>'];
                cont = cont + 1;
            }
            </logic:iterate>

            // Se actualiza la lista de documentos en la pantalla de registros
            self.parent.opener.actualizaDocs(listaDocs);
        }
    </script>
</head>
<body onload="inicializar();">
</body>
</html>
