<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@page import="es.altia.agora.business.registro.RegistroValueObject" %>


<html>
<head>
<title> Oculto buscar un altaRE que está en la tabla de reserva </title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />

<script>

function redirecciona()
{

             var cod_tiposDocumentos = new Array();
             var desc_tiposDocumentos = new Array();
             var cod_tiposRemitentes = new Array();
             var desc_tiposRemitentes= new Array();
             var cod_tiposTransportes = new Array();
             var desc_tiposTransportes = new Array();
             var cod_actuaciones= new Array();
             var desc_actuaciones= new Array();
             var cod_temas = new Array();
             var desc_temas = new Array();

  var datos  = new Array();
  datos[1] = '<bean:write name="MantAnotacionRegistroForm" property="diaAnotacion"/>';
  datos[2] = '<bean:write name="MantAnotacionRegistroForm" property="mesAnotacion"/>';
  datos[3] = '<bean:write name="MantAnotacionRegistroForm" property="anoAnotacion"/>';
  datos[4] = '<bean:write name="MantAnotacionRegistroForm" property="txtHoraEnt"/>';
  datos[5] = '<bean:write name="MantAnotacionRegistroForm" property="txtMinEnt"/>';


  var resp = '<bean:write name="MantAnotacionRegistroForm" property="respOpcion"/>';

                    if(resp == 'modificar') {

                     <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposDocumentos">
                     cod_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                     desc_tiposDocumentos['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
                     </logic:iterate>

                     <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposRemitentes">
                     cod_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                     desc_tiposRemitentes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
                     </logic:iterate>

                     <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposTransportes">
                     cod_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                     desc_tiposTransportes['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
                     </logic:iterate>

                     <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaActuaciones">
                     cod_actuaciones['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                     desc_actuaciones['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
                     </logic:iterate>

                     <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTemas">
                     cod_temas['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
                     desc_temas['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
                     </logic:iterate>

					 var cod_tiposIdInteresado= new Array();
					 var desc_tiposIdInteresado= new Array();
					 <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaTiposIdInteresado">
					 cod_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
					 desc_tiposIdInteresado['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
					 </logic:iterate>

					 var cod_dpto = new Array();
					 var desc_dpto = new Array();
					 <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDepartamentos">
					 cod_dpto['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="codigo"/>';
					 desc_dpto['<bean:write name="elemento" property="orden"/>']='<bean:write name="elemento" property="descripcion"/>';
					 </logic:iterate>


                     } else {
                     var cod_tiposDocumentos = 0;
                     var desc_tiposDocumentos = 0;
                     var cod_tiposRemitentes = 0;
                     var desc_tiposRemitentes= 0;
                     var cod_tiposTransportes = 0;
                     var desc_tiposTransportes = 0;
                     var cod_actuaciones= 0;
                     var desc_actuaciones= 0;
                     var cod_temas = 0;
                     var desc_temas = 0;
					 var cod_tiposIdInteresado= 0;
					 var desc_tiposIdInteresado= 0;
					 var cod_dpto = 0;
					 var desc_dpto = 0;
                     }


  parent.mainFrame.recuperaDatos1(datos,cod_tiposDocumentos, desc_tiposDocumentos,cod_actuaciones, desc_actuaciones, cod_tiposTransportes, desc_tiposTransportes, cod_tiposRemitentes, desc_tiposRemitentes, cod_temas, desc_temas,cod_tiposIdInteresado, desc_tiposIdInteresado, cod_dpto, desc_dpto);

}
</script>

</head>
<body onLoad="redirecciona();">

<input type="hidden" name="opcion" >

<p>&nbsp;<p><center>


</body>
</html>
