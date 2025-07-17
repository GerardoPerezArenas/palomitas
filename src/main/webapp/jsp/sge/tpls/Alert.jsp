<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<html>
    <head>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <link rel="stylesheet" href="<c:url value='/css/estilo.css'/>" type="text/css">
        <script language="JavaScript" SRC="<c:url value='/scripts/general.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
        <script language="JavaScript">
       
           function inicializar() {
            var argVentana = window.dialogArguments;
            var contenido='<textarea  styleClass="textareaTexto" style="text-transform: none;width:100%" cols="70" rows="35" font-size: .90em; border: none; overflow-y: scroll;" readonly="readonly">'
                        +argVentana.mensaje+'</textarea>';
            domlay('capaEscritura',1,0,0,contenido);
        }
        </script>
        
        <title><%=request.getParameter("TituloMSG")%></title>
    </head>
    <body onload="inicializar();">
        <div id="capaEscritura" style="width: 912; height: 465px; padding: 10px;  overflow-y: hidden;">
            
        </div>
            <div id="dialog_botonera" style="margin-top: 5px; margin-right: 5px; float: right">
                <input type="button" class="botonGeneral" value="Cerrar" onClick="window.close();">
            </div>
    </body>
</html>
