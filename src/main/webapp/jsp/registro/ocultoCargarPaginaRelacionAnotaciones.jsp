<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.registro.RegistroValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="es.altia.agora.interfaces.user.web.util.FormateadorTercero" %>
<%@page import="com.google.gson.GsonBuilder" %>

<%@page import="java.util.Vector" %>

<html>
<head>
  <title> Oculto cargar pagina </title>
  <%
    UsuarioValueObject usuarioVO;
    int idioma=1;
    int apl=1;
    if ((session.getAttribute("usuario") != null) && (session.getAttribute("registroUsuario") != null)){
      usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
      idioma = usuarioVO.getIdioma();
      apl = usuarioVO.getAppCod();
    }
  %>
  <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
  <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
  <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
  <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

  <script>

    function redirecciona()
    {

      parent.mainFrame.listaOriginal = new Array();
      parent.mainFrame.lista= new Array();
      parent.mainFrame.listaSel = new Array();
      parent.mainFrame.listaAnotacionesCompleta = new Array();
      var DOT_COMMA = ";";

      var asunto;
      var estado;
      var ejer;
      var numAnot;
      var tipoAnot;
      var finDigitalizacion;
      var estadoExpediente;
      var digitalizacionActiva="<%= (String)session.getAttribute("DigitalizacionActiva") %>";


      <% /* Recuperar el vector de anotaciones de la sesion. */

         MantAnotacionRegistroForm f= (MantAnotacionRegistroForm)  session.getAttribute("MantAnotacionRegistroForm");
         RegistroValueObject arVO = f.getRegistro();
         String esConsultaSIR = "0";
        if (request.getAttribute("esConsultaSIR") != null) {
          esConsultaSIR = (String) request.getAttribute("esConsultaSIR");
        }

        int numPagina;
        if ("1".equals(esConsultaSIR)) {
            numPagina = 1;
        } else {
            numPagina = Integer.parseInt(arVO.getPaginaListado());
        }


         String nombreCompleto;
         String apellido1;
         String apellido2;
         String multiinteresado;
         String entrada="";
         String procedimiento="";
         String expediente="";
         String entrSal="";
         String estado2="";
         String finDigitalizacion="";
         String estadoExpediente="";
         Vector relacionAnotaciones = (Vector) session.getAttribute("RelacionAnotaciones");
         String relaciones = (String)session.getAttribute("relaciones");
         int numAnotaciones = ((Integer)session.getAttribute("NumRelacionAnotaciones")).intValue();

          if (relacionAnotaciones != null) {
              int j = 0;
              int z=0;
              for(int i=0;i<relacionAnotaciones.size();i++){
                  Vector cp_anotacion = (Vector)relacionAnotaciones.get(i);
                  nombreCompleto = (String) cp_anotacion.elementAt(8);
                  apellido1 = (String) cp_anotacion.elementAt(9);
                  apellido2 = (String) cp_anotacion.elementAt(10);
                  multiinteresado=(String) cp_anotacion.elementAt(15);
                  //System.out.println("\n AAAAAAAAAAA "+ cp_anotacion.elementAt(12));

                  //para dar formato al interesado
                  boolean masTerceros = false;
                  if (!multiinteresado.equals("1")) masTerceros = true;
                  String nombreFormateado = FormateadorTercero.getDescTercero(nombreCompleto, apellido1, apellido2, masTerceros);

      %>


      parent.mainFrame.listaAnotacionesCompleta[<%= j %>] = <%= new GsonBuilder().serializeNulls().create().toJson(cp_anotacion) %>;
      asunto = unescape("<%= cp_anotacion.elementAt(7) %>");
      ejer = "<%= cp_anotacion.elementAt(3) %>";
      numAnot = "<%= cp_anotacion.elementAt(4) %>";
      tipoAnot = "<%= cp_anotacion.elementAt(2) %>";
      <%-- Safely get optional indices 22 and 23 --%>
      <%
        String finDigitalizacionStr = "";
        if (cp_anotacion.size() > 22) {
            Object tmp22 = cp_anotacion.elementAt(22);
            if (tmp22 != null) {
                finDigitalizacionStr = tmp22.toString();
            }
        }
        String estadoExpedienteStr = "";
        if (cp_anotacion.size() > 23) {
            Object tmp23 = cp_anotacion.elementAt(23);
            if (tmp23 != null) {
                estadoExpedienteStr = tmp23.toString();
            }
        }
      %>
      finDigitalizacion = "<%= finDigitalizacionStr %>";
      estadoExpediente= "<%= estadoExpedienteStr %>";
      parent.mainFrame.listaOriginal[<%= j %>]  = [ejer,
        numAnot,"<%= cp_anotacion.elementAt(5) %>",asunto,
        "<%=StringEscapeUtils.escapeJavaScript(nombreFormateado)%>",
        "<%= cp_anotacion.elementAt(10) %>","<%= cp_anotacion.elementAt(11) %>"];
      if (asunto.length > 75 ) asunto = asunto.substring(0,27)+"</BR>"+asunto.substring(27,75)+"...";

      //tipo de entrada segun el codigo que recibo de la base de datos:
      if ("<%= cp_anotacion.elementAt(20) %>" == "0") {
        entrada='<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("entradaOrd"))%>';
      }else if("<%= cp_anotacion.elementAt(20) %>" == "1"){
        entrada=   '<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("DestOtroReg"))%>';
      }else if("<%= cp_anotacion.elementAt(20) %>" == "2"){
        entrada='<%=StringEscapeUtils.escapeJavaScript(descriptor.getDescripcion("procOtroReg"))%>';
      }
      console.log( "[hiddenLoadPage] numAnot=" , numAnot, "typeEntry=" , "<%= cp_anotacion.elementAt(20) %>" , "description=" , entrada);
//variable estado2 que es la que aparece en la columna de la tabla estado y necesita el estado pendiente
      // 1) finDigitalizacion
      if (finDigitalizacion=="0" && digitalizacionActiva=="si") {
        finDigitalizacion = '<b> Pend. fin digit. </b>';
      } else {
        finDigitalizacion = "";
      }

// 2) ¿viene estado SIR?
      <%
        String sirStateTmp = "";
        if (cp_anotacion.size() > 24) {
          Object tmpObj = cp_anotacion.elementAt(24);
          if (tmpObj != null) {
            sirStateTmp = tmpObj.toString();
          }
        }
      %>
      var sirState = "<%= sirStateTmp %>";  // 0..15 o vacío
      if (sirState) {
        switch (sirState) {
          case "0":
            estado  = '<FONT>Pendiente de envío</FONT><BR>';
            estado2 = 'Pendiente de envío ' + finDigitalizacion;
            break;
          case "1":
            estado  = '<FONT>Enviado</FONT><BR>';
            estado2 = 'Enviado ' + finDigitalizacion;
            break;
          case "2":
            estado  = '<FONT>Enviado y ACK</FONT><BR>';
            estado2 = 'Enviado y ACK ' + finDigitalizacion;
            break;
          case "3":
            estado  = '<FONT>Enviado y ERROR</FONT><BR>';
            estado2 = 'Enviado y ERROR ' + finDigitalizacion;
            break;
          case "4":
            estado  = '<FONT>Devuelto</FONT><BR>';
            estado2 = 'Devuelto ' + finDigitalizacion;
            break;
          case "5":
            estado  = '<FONT>Aceptado</FONT><BR>';
            estado2 = 'Aceptado ' + finDigitalizacion;
            break;
          case "6":
            estado  = '<FONT>Reenviado</FONT><BR>';
            estado2 = 'Reenviado ' + finDigitalizacion;
            break;
          case "7":
            estado  = '<FONT>Reenviado y ACK</FONT><BR>';
            estado2 = 'Reenviado y ACK ' + finDigitalizacion;
            break;
          case "8":
            estado  = '<FONT>Reenviado y ERROR</FONT><BR>';
            estado2 = 'Reenviado y ERROR ' + finDigitalizacion;
            break;
          case "9":
            estado  = '<FONT style="color:red">Anulado</FONT><BR>';
            estado2 = 'Anulado ' + finDigitalizacion;
            break;
          case "10":
            estado  = '<FONT>Recibido</FONT><BR>';
            estado2 = 'Recibido ' + finDigitalizacion;
            break;
          case "11":
            estado  = '<FONT>Rechazado</FONT><BR>';
            estado2 = 'Rechazado ' + finDigitalizacion;
            break;
          case "12":
            estado  = '<FONT>Rechazado y ACK</FONT><BR>';
            estado2 = 'Rechazado y ACK ' + finDigitalizacion;
            break;
          case "13":
            estado  = '<FONT>Rechazado y ERROR</FONT><BR>';
            estado2 = 'Rechazado y ERROR ' + finDigitalizacion;
            break;
          case "14":
            estado  = '<FONT>Validado</FONT><BR>';
            estado2 = 'Validado ' + finDigitalizacion;
            break;
          case "15":
            estado  = '<FONT>Reintentar validación</FONT><BR>';
            estado2 = 'Reintentar validación ' + finDigitalizacion;
            break;
          default:
            estado  = '';
            estado2 = 'Estado desconocido ' + finDigitalizacion;
        }
      } else {
        // 3) Si no viene estado SIR, uso el mapeo  sobre elementAt(13):
        var flexState = "<%= cp_anotacion.elementAt(13) %>";
        if (flexState=="0") {
          estado2 = 'Pendiente ' + finDigitalizacion;
          estado  = '<FONT>Pendiente</FONT><BR>';
        } else if (flexState=="1") {
          estado  = '<FONT style="color:green">Aceptada</FONT><BR>';
          estado2 = 'Aceptada ' + finDigitalizacion;
        } else if (flexState=="2") {
          estado  = '<FONT style="color:red">Rechazada</FONT><BR>';
          estado2 = 'Rechazada ' + finDigitalizacion;
        } else if (flexState=="3") {
          estado  = '<FONT style="color:green">Asociada a expediente</FONT><BR>';
          estado2 = 'Asociada a expediente ' + finDigitalizacion;
        } else if (flexState=="9") {
          estado  = '<FONT style="color:red">Anulada</FONT><BR>';
          estado2 = 'Anulada ' + finDigitalizacion;
        } else {
          estado  = '';
          estado2 = finDigitalizacion;
        }
      }



      // Se construye la listaObs para resaltar los campos con observaciones
      var observacion = '<%=StringEscapeUtils.escapeJavaScript((String) cp_anotacion.elementAt(16))%>';
      if (observacion!='null' && observacion.length>0) {
        parent.mainFrame.listaObs[<%=j%>] = true;
      } else {
        parent.mainFrame.listaObs[<%=j%>] = false;
      }

      if(estadoExpediente=="0"){
        estadoExpediente='<br><font color="blue"><b> Exp. Pendiente  </b><font>';
      }
      if(estadoExpediente=="1"){
        estadoExpediente='<br><font color="brown"><b> Exp. Anulado  </b><font>';
      }
      if(estadoExpediente=="9"){
        estadoExpediente='<br><font color="green"><b> Exp. Finalizado  </b><font>';
      }
      if(estadoExpediente=="null"){
        estadoExpediente='';
      }
      // si es nulo aparece como espacio en blanco
      tipoDoc = "<%= cp_anotacion.elementAt(1) %>";
      if (tipoDoc == 'null' || tipoDoc == null) tipoDoc = '';

      procedimiento = "<%= cp_anotacion.elementAt(17) %>";
      if (procedimiento == 'null' || procedimiento == null) {
        procedimiento = '';
      }
      expediente = "<%= cp_anotacion.elementAt(18)%>";
      if (expediente == 'null' || expediente == null)
        expediente="";
      else
        expediente= "<%= cp_anotacion.elementAt(18)%>" + estadoExpediente;

      // para que salga la palabra entera no solo E o S
      if (tipoAnot == 'E') {
        entrSal = 'Entrada';
      } else {
        entrSal = 'Salida';
      }

      <%-- Obtén el identificador SIR (índice 26) y la fecha del estado SIR (índice 25) --%>
      <%
        String idSir = "";
        if (cp_anotacion.size() > 26 && cp_anotacion.elementAt(26) != null) {
          idSir = cp_anotacion.elementAt(26).toString();
        }
        String fechaEstadoStr = "";
        if (cp_anotacion.size() > 25 && cp_anotacion.elementAt(25) != null) {
          fechaEstadoStr = cp_anotacion.elementAt(25).toString();
        }
      %>

      // 0) Llevamos a JS `idSir` y la fecha de estado
      var idSir        = "<%= idSir %>";
      var fechaEstado  = "<%= fechaEstadoStr %>";

      // 1) Construye la celda de estado con su fecha debajo
      var estadoCell = estado;
      if (fechaEstado) {
        estadoCell += "<br><span style='font-size:80%;color:#666;'>" + fechaEstado + "</span>";
      }

      // 2) Fechas de presentación y grabación
      var fechaPres = "<%= cp_anotacion.elementAt(5) %>";
      var fechaGrab = "<%= cp_anotacion.elementAt(6) %>";  // <-- añadido ;

      // 3) Número de anotación + idSir (columna 5)
      var numSirCell = ejer + "/" + numAnot +
              ("<%= cp_anotacion.elementAt(20) %>" === "1" && idSir
                              ? "<br><span style='font-size:80%;color:#666;'>" + idSir + "</span>"
                              : ""
              );

      // 4) Pinta la fila
      if ("<%= relaciones %>" === 'relaciones') {
        var filaTmp = [
          /* 0 */ estadoCell,
          /* 1 */ "<%= cp_anotacion.elementAt(5) %>",
          /* 2 */ "<%= StringEscapeUtils.escapeJavaScript(nombreFormateado) %> <br>" + asunto,
          /* 3 */ "<%= cp_anotacion.elementAt(14) %>",
          /* 4 */ "<%= cp_anotacion.elementAt(12) %>"
        ];
        filaTmp.DT_RowAttr = { 'data-ano': ejer, 'data-numero': numAnot };
        parent.mainFrame.lista[<%= z++ %>] = filaTmp;
      } else {
        var valorCheck = ejer + DOT_COMMA + numAnot + DOT_COMMA + tipoAnot + DOT_COMMA + finDigitalizacion;
        parent.mainFrame.lista[<%= j++ %>] = [
          /* 0 */ '<input type="checkbox" class="checkLinea" onclick="pulsarCheck()"'
          + ' value="' + valorCheck + '" />',
          /* 1 */ estadoCell,
          /* 2 */ procedimiento,
          /* 3 */ expediente,
          /* 4 */ "<%= cp_anotacion.elementAt(19) %>",    // Usuario Alta
          /* 5 */ numSirCell,                             // Número + idSir
          /* 6 */ entrSal,                                // Entrada/Salida
          /* 7 */ fechaPres,                              // Fecha de presentación
          /* 8 */ fechaGrab,                              // Fecha de grabación
          /* 9 */ "<%= StringEscapeUtils.escapeJavaScript(nombreFormateado) %>", // Remitente
          /*10*/ asunto,                                  // Extracto
          /*11*/ "<%= cp_anotacion.elementAt(12) %>",      // Unidad de destino
          /*12*/ entrada                                  // Tipo de entrada
        ];
      }

      <% 	   }// for
                  }

          %>


      var numPagina = "<%= numPagina%>";
      parent.mainFrame.listaSel =parent.mainFrame.lista;
      parent.mainFrame.numRelacionAnotaciones = <%=numAnotaciones%>;
      parent.mainFrame.inicializaLista(numPagina);
    }
    console.log("oculto: llamando a redirecciona()");
    redirecciona();
  </script>

</head>
<body onLoad="pleaseWait('off');redirecciona();">

<p>&nbsp;<p>

</body>
</html>


