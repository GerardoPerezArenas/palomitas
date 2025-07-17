<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>

<%@ page import="es.altia.agora.interfaces.user.web.util.*"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
        }
  }
%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod"   value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>

<html>
<head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<title>OcultoCalendario</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<script language="JavaScript">

function inicio(){

   <logic:equal name="GestionForm" property="accion" value="CalendarioGeneral.Grabar">
     jsp_alerta('A','<%=descriptor.getDescripcion("MsgCalLabGenGra")%>');
   </logic:equal>

   <logic:equal name="GestionForm" property="accion" value="CalendarioCentro.CambioCentro">
      var i=0;
      var anoHoy = new Date();

      <logic:iterate id="elemento" name="GestionForm" property="lista_festivos_generales">
         parent.festivos[i] = '<bean:write name="elemento"/>'; i++;
      </logic:iterate> i=0;

      <logic:iterate id="elemento" name="GestionForm" property="lista_festivos_centro">
         parent.festivosCentro[i] = '<bean:write name="elemento"/>'; i++;
      </logic:iterate> i=0;

      <logic:iterate id="elemento" name="GestionForm" property="lista_vacaciones_centro">
         parent.vacacionesCentro[i] = '<bean:write name="elemento"/>'; i++;
      </logic:iterate> i=0;

      <logic:iterate id="elemento" name="GestionForm" property="lista_dias_ocupados">
         parent.diasOcupados[i] = '<bean:write name="elemento"/>'; i++;
      </logic:iterate>

      parent.numFestivosCentro = 0;
      parent.numVacacionesCentro = 0;

      parent.mostrarMeses(anoHoy.getYear(), 'todos');
      parent.document.forms[0].ano.value = '2002';

      parent.document.forms[0].centroAnterior.value = parent.document.forms[0].centro.value;
   </logic:equal>

   <logic:equal name="GestionForm" property="accion" value="CalendarioCentro.Grabar">
      alert('<%=descriptor.getDescripcion("MsgCalLabCenGra")%>');
      parent.document.forms[0].dias_cambiados.value = '';
   </logic:equal>

   <logic:equal name="GestionForm" property="accion" value="HorarioCentro.CambioCentro">
      var i=0;
      var anoHoy = new Date();
      parent.anoCalendario = anoHoy.getYear();

      <logic:iterate id="elemento" name="GestionForm" property="horario_normal">
         parent.horario_normal[i] = '<bean:write name="elemento"/>';
         i++;
      </logic:iterate> i=0;

      <logic:iterate id="elemento" name="GestionForm" property="horario_especial">
         parent.horario_especial[i] = ['<bean:write name="elemento" property="fecha_inicio"/>', '<bean:write name="elemento" property="fecha_fin"/>', '<bean:write name="elemento" property="hora_inicio_m"/>', '<bean:write name="elemento" property="hora_fin_m"/>', '<bean:write name="elemento" property="hora_inicio_t"/>', '<bean:write name="elemento" property="hora_fin_t"/>'];
         i++;
      </logic:iterate> i=0;

      parent.tratarHorarioNormal();
      parent.tratarHorarioSabado();
      parent.cargarHorarioAnual(anoHoy.getYear());
      parent.cargaTabla();

      parent.document.forms[0].ano.value = anoHoy.getYear();
      parent.document.forms[0].anoAnterior.value = anoHoy.getYear();
      parent.document.forms[0].centroAnterior.value = parent.document.forms[0].centro.value;
   </logic:equal>

   <logic:equal name="GestionForm" property="accion" value="HorarioCentro.Grabar">
      alert('<%=descriptor.getDescripcion("MsgHorLabCenGra")%>');
      parent.document.forms[0].horarios_borrados0.value = '';
      parent.document.forms[0].horarios_borrados1.value = '';

   </logic:equal>
}

</script>
</head>

<body onload="inicio();">

</body>

</html>
