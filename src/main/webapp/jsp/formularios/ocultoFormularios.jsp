<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.formularios.FormulariosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.DateFormat"%>
<%@ page import="java.util.Date"%>

<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Gestión de Formularios</title>
  <script type="text/javascript">
	// VARIABLES GLOBALES
	var lista = new Array();
    var datosFormularios = new Array();
		
	// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
		cargaTablaFormularios();
	}
		
	function cargaTablaFormularios(){
	    <%FormulariosForm bForm = (FormulariosForm)session.getAttribute("FormulariosForm");
        Vector listaFormularios = bForm.getListaFormularios();
        int lengthFormularios = listaFormularios.size();%>
        var j=0;
        <%String imagen="";
        for(int i=0;i<lengthFormularios;i++){
            GeneralValueObject tipos = (GeneralValueObject)listaFormularios.get(i);
            
            if (tipos.getAtributo("fechaBajaForm") != null) 
                imagen = "fa-times";
            else 
                imagen = "fa-check";
        %>
            
            datosFormularios[j] = ['<%=(String)tipos.getAtributo("codForm")%>'
                    , '<%=(String)tipos.getAtributo("codVisibleForm")%>'
                    , '<%=(String)tipos.getAtributo("descForm")%>'
                    , '<%=(String)tipos.getAtributo("versionForm")%>'
                    , '<%=(String)tipos.getAtributo("fechaAltaForm")%>'
                    , '<span class="fa <%=imagen%>"></span>'];
	        lista[j] = datosFormularios[j];
            j++;
        <%}%>
        
        parent.mainFrame.cargarTablaFormularios(lista);
    }
  </script>
</head>
<body onload="inicializar();">
</body>
</html>
