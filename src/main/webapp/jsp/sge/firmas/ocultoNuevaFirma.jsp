<%@ page import="es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO"%>
<%@ page import="es.altia.agora.business.administracion.mantenimiento.UsuariosGruposValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Oculto Cargar Unidades Organicas</title>

        <script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>

        <script type="text/javascript">

            var usuarios= new Array();
            var codUsuarios= new Array();
            var descUsuarios= new Array();
            <%                     
                     Vector listaUsuarios = (Vector) request.getAttribute("listaUsuarios");
                     if (listaUsuarios == null) {
                         listaUsuarios = new Vector();
                     }

                     for (int j = 0; j < listaUsuarios.size(); j++) {
                         UsuariosGruposValueObject dto = (UsuariosGruposValueObject) listaUsuarios.get(j);%>
                             usuarios[<%=j%>] = ['<%=dto.getCodUsuario()%>','<%=dto.getNombreUsuario()%>'];
                             codUsuarios[<%=j%>] = '<%=dto.getCodUsuario()%>'      ;
                             descUsuarios[<%=j%>] = '<%=dto.getNombreUsuario()%>';
            <%}%>
	
                // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
                function inicializar(){ 
                    parent.mainFrame.cargarUsuarios(usuarios, codUsuarios, descUsuarios);
                }

        </script>
    </head>
    <body onload="inicializar();">
    </body>
</html>
