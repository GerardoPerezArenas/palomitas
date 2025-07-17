<%@page import="es.altia.agora.interfaces.user.web.terceros.FusionDivisionForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<html>
<head>
<title>Oculto Padron</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<script src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script language="javascript">
  <%
    FusionDivisionForm fdForm =(FusionDivisionForm)session.getAttribute("FusionDivisionForm");
    String opcion = fdForm.getOpcion();
  %>
  var datos = new Array();
  var frame = parent.mainFrame;
  var datosTerceros = new Array();

  function recargaConsultaPadron(){
    <%
      GeneralValueObject datosVO = fdForm.getDatosVO();
      Vector hojas = (Vector)datosVO.getAtributo("hojas");
      int i = 0;
      int j = 0;
    %>
      var j=0;
      var k=0;
      <%for(i=0;i<hojas.size();i++){
          GeneralValueObject hoja = (GeneralValueObject)hojas.get(i);
          Vector habitantes = (Vector)hoja.getAtributo("habitantes");
          int lengthTer = habitantes.size();
          for(j=0;j<lengthTer;j++){
            GeneralValueObject habitante = (GeneralValueObject)habitantes.get(j); %>
            datosTerceros[k] = ["<%=(String)habitante.getAtributo("idTercero")%>",// 0
              "<%=(String)habitante.getAtributo("nombre")%>",// 1
              "<%=(String)habitante.getAtributo("apellido1")%>",// 2
              "<%=(String)habitante.getAtributo("apellido2")%>",// 3
              "<%=(String)habitante.getAtributo("particula1")%>",// 4
              "<%=(String)habitante.getAtributo("particula2")%>",// 5
              "<%=(String)habitante.getAtributo("codTipoDoc")%>",// 6
              "<%=(String)habitante.getAtributo("descTipoDoc")%>",// 7
              "<%=(String)habitante.getAtributo("documento")%>",// 8
              "<%=(String)habitante.getAtributo("orden")%>",// 9
              "<%=(String)habitante.getAtributo("telefono")%>",// 10
              "<%=(String)habitante.getAtributo("operacion")%>",// 11
              "<%=(String)habitante.getAtributo("situacion")%>",// 12
              "<%=(String)habitante.getAtributo("sexo")%>",// 13
              "<%=(String)habitante.getAtributo("codigoOperacion")%>",// 14
              "<%=(String)habitante.getAtributo("familiaReal")%>",// 15
              "<%=(String)habitante.getAtributo("causaVariacion")%>",// 16
              "<%=(String)habitante.getAtributo("nia")%>"];// 17
            k++;
        <%}%>
          datos[j] = ["<%=(String)hoja.getAtributo("idDomicilio")%>",// 0
            "<%=(String)hoja.getAtributo("codDomicilio")%>",// 1
            "<%=(String)hoja.getAtributo("descDomicilio")%>",// 2
            "<%=(String)hoja.getAtributo("codECO")%>",// 3
            "<%=(String)hoja.getAtributo("descECO")%>",// 4
            "<%=(String)hoja.getAtributo("codESI")%>",// 5
            "<%=(String)hoja.getAtributo("descESI")%>",// 6
            "<%=(String)hoja.getAtributo("codNUC")%>",// 7
            "<%=(String)hoja.getAtributo("descNUC")%>",// 8
            "<%=(String)hoja.getAtributo("distrito")%>",// 9
            "<%=(String)hoja.getAtributo("seccion")%>",// 10
            "<%=(String)hoja.getAtributo("letraHoja")%>",// 11
            "<%=(String)hoja.getAtributo("hoja")%>",// 12
            "<%=(String)hoja.getAtributo("familia")%>",// 13
            "<%=(String)hoja.getAtributo("versionHoja")%>",// 14
            "<%=(String)hoja.getAtributo("numDesde")%>",// 15
            "<%=(String)hoja.getAtributo("letraDesde")%>",// 16
            "<%=(String)hoja.getAtributo("numHasta")%>",// 17
            "<%=(String)hoja.getAtributo("letraHasta")%>",// 18
            "<%=(String)hoja.getAtributo("bloque")%>",// 19
            "<%=(String)hoja.getAtributo("portal")%>",// 20
            "<%=(String)hoja.getAtributo("escalera")%>",// 21
            "<%=(String)hoja.getAtributo("planta")%>",// 22
            "<%=(String)hoja.getAtributo("puerta")%>",// 23
            "<%=(String)hoja.getAtributo("km")%>",// 24
            "<%=(String)hoja.getAtributo("hm")%>",// 25
            datosTerceros// 26
            ];
          j++;
          k=0;
          datosTerceros = new Array();
      <%}%>
    frame.recuperaBusqueda(datos);
  }

  recargaConsultaPadron();
</script>
</head>

<body>

</body>
</html>
