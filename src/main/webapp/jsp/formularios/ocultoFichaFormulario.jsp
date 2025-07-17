<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.formularios.FormulariosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.DateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="es.altia.agora.interfaces.user.web.formularios.FichaFormularioForm"%>
<%@ page import="es.altia.agora.business.util.ElementoListaValueObject"%>

<html>
    <head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Oculto Gestión de Formularios</title>
        <script type="text/javascript">
            // VARIABLES GLOBALES
            
            var cod_tramiteProcedimiento = new Array();
            var desc_tramiteProcedimiento = new Array();

            // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
            function inicializar(){
                cargaComboTramites();
            }

            function cargaComboTramites(){
        <%FichaFormularioForm fichaForm = (FichaFormularioForm) session.getAttribute("FichaFormularioForm");
        Vector listaTramites = fichaForm.getListaTramitesProcedimiento();%>
                var j=0;
        <%for (int i = 0; i < listaTramites.size(); i++) {
            ElementoListaValueObject elvo = (ElementoListaValueObject) listaTramites.get(i);%>
                    cod_tramiteProcedimiento[j] = '<%=elvo.getIdentificador()%>';
                    desc_tramiteProcedimiento[j++] = '<%=elvo.getDescripcion()%>';
        <%}%>
            parent.mainFrame.recuperarTramites(cod_tramiteProcedimiento,desc_tramiteProcedimiento);
                               
            }
        </script>
    </head>
    <body onload="inicializar();">
    </body>
</html>
