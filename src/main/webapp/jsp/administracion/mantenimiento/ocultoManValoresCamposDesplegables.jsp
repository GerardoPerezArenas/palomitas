<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Campos Desplegables</title>
<%
    ArrayList listaIdiomas = (ArrayList) session.getAttribute("listaIdiomas");
%>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    var lista = new Array();
    var listaOriginal = new Array();
    var datos = new Array();
    var datosOriginales = new Array();
    var multiidioma = <%=listaIdiomas!= null && listaIdiomas.size() > 0%>;

    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
      cargaTablaValoresCamposDesplegables();
    }

    function cargaTablaValoresCamposDesplegables(){
        <%MantenimientosAdminForm bForm =
            (MantenimientosAdminForm)session.getAttribute("MantenimientosAdminForm");
        Vector listaValores = bForm.getListaValoresCamposDesplegables();
        int lengthValores = listaValores.size();
        int i = 0;%>
        var estadoValor;
        var j=0;
        <%for(i=0;i<lengthValores;i++){
            GeneralValueObject valor = (GeneralValueObject)listaValores.get(i);%>
            var codigoCampo = '<%=(String) valor.getAtributo("codigoCampo")%>';
            var codigoValor = '<%=(String) valor.getAtributo("codigoValor")%>';
            var descripcionValor = '<%=(String) valor.getAtributo("descripcionValor")%>';
            if (multiidioma){
               var descripciones = "";
               descripciones = descripcionValor.split("|");
               var descId1 = descripciones[0];
               var descId2 = "";
               if (descripcionValor.indexOf("|") != -1){
                   descId2 = descripciones[1];
               }
			if('<%=(String) valor.getAtributo("estadoValor")%>'=='A'){
                        estadoValor='<span class="fa fa-check 2x"></span>';
                    }else{
                        estadoValor='<span class="fa fa-close 2x"></span>';
                    } 
               datos[j] = [codigoValor,descId1,descId2,estadoValor];
               datosOriginales[j] = [codigoCampo,codigoValor,descId1,descId2,'<%=(String)valor.getAtributo("estadoValor")%>'];
           } else {
               datos[j] = [codigoValor,descripcionValor,estadoValor];
               datosOriginales[j] = [codigoCampo,codigoValor,descripcionValor,'<%=(String)valor.getAtributo("estadoValor")%>'];
           }

        j++;
       <%}%>
        parent.mainFrame.recuperaDatos(datos, datosOriginales);
    }

  </script>
</head>
<body onload="inicializar();">
</body>
</html>
