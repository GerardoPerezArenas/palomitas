<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Mantenimiento de Tipos de Organizaciones</title>
  <script type="text/javascript">
		// VARIABLES GLOBALES
    var lista = new Array();
    var listaOrganizaciones = new Array();
    var datosOrganizaciones = new Array();
    var listaCss = new Array();
    var codCss = new Array();
    var descCss = new Array();
  
    <%
        MantenimientosAdminForm bForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
        Vector listaOrganizaciones = bForm.getListaOrganizaciones();
        Vector listaCss = bForm.getListaCss();
        int lengthOrganizaciones = listaOrganizaciones.size();
        int lengthCss = listaCss.size();
        int i = 0;
     %>
       //sacamos listado de estilos

    var z=0;
    <%for (i = 0; i < lengthCss; i++) {
        GeneralValueObject css = (GeneralValueObject) listaCss.get(i);%>
        listaCss[z]=['<%=(String) css.getAtributo("codigo")%>','<%=(String) css.getAtributo("ruta")%>','<%=(String) css.getAtributo("descripcion")%>','<%=(String) css.getAtributo("general")%>'];
        codCss[z]= listaCss[z][0];
         descCss[z]= listaCss[z][2];
        z++;

    <%}%>

      var j=0;
     var l=0;

     <%for (i = 0; i < lengthOrganizaciones; i++) {
                GeneralValueObject organizaciones = (GeneralValueObject) listaOrganizaciones.get(i);%>
                listaOrganizaciones[j] = ['<%=(String) organizaciones.getAtributo("codigo")%>', '<%=(String) organizaciones.getAtributo("descripcion")%>', '<%=(String) organizaciones.getAtributo("icono")%>', '<%=(String) organizaciones.getAtributo("css")%>'];
                //meto la descripcion del estilo en la columna de la tabla

                for(l=0; l < listaCss.length; l++){   
                 if(listaCss[l][0]==listaOrganizaciones[j][3]){
                      datosOrganizaciones[j]=[listaOrganizaciones[j][1],listaCss[l][2]];
                    }
                }
                lista[j] = datosOrganizaciones[j];
                j++;
            <%}%>
                 
            
			parent.mainFrame.recuperaDatos(lista,listaCss,codCss,descCss);
		

  </script>
</head>
<body>
</body>
</html>
