<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.Vector" %>

    <%
      int idioma=1;
      int apl=1;
        if (session!=null){
          UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
            if (usuario!=null) {
              idioma = usuario.getIdioma();
              apl = usuario.getAppCod();
            }
      }
        Config m_Config = ConfigServiceHelper.getConfig("common");
        String statusBar = m_Config.getString("JSP.StatusBar");
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

    <TITLE>::: N�mero de Expediente :::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />


    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <style type="text/css">

     html>body .bandaBody{
        /* background-image: url("../images/escritorio/fondo.jpg"); */
        position: fixed;
        width: 500px;
    }

    </style>
    <SCRIPT type="text/javascript">
    function inicializar() {
      window.focus();
      document.forms[0].numeroExpediente.value = '<c:out value="${requestScope.numeroExpediente}"/>';
      document.forms[0].hayListaTerc.value = '<c:out value="${requestScope.hayListaTerc}"/>';
      document.forms[0].registroOrigen.value = '<c:out value="${requestScope.registroOrigen}"/>';
    }
        
    function pulsarAceptar(){
        document.forms[0].opcion.value="comprobarAsociacionMultiple";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
        document.forms[0].submit();  
    }//pulsarAceptar
    
    //Funci�n que comprueba si es posible la asociaci�n o creaci�n de multiples expedientes sobre una misma entrada.
    function permanenciaBuzon(permanencia){
        if(permanencia == "true"){
            mensaje = "<%=descriptor.getDescripcion("permanecerAnotacionEnBuzon")%>";

            if(jsp_alerta("C",mensaje) ==1) {
                document.forms[0].valorOpcionPermanencia.value = 'true';
            }else{
                document.forms[0].valorOpcionPermanencia.value = 'false';
            }
        }else{
            document.forms[0].valorOpcionPermanencia.value = 'false';
        }

        document.forms[0].opcion.value="adjuntarNumeroInfo";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
        document.forms[0].submit();
    }//permanenciaBuzon

    function adjuntarExpedienteInteresadosInfo(numeroInteresados,hayInteresadosReg)
    {
         if (validarObligatorios("<%=descriptor.getDescripcion("msjObligTodos")%>")) {
            //Obtener los terceros principales
            var argumentos = new Array();
            var datos=new Array();
            if(numeroInteresados==0)
            {
                mensaje = "<%=descriptor.getDescripcion("desAdjuntar")%>" + " " + document.forms[0].numeroExpediente.value + "?";
                if(jsp_alerta("C",mensaje) ==1) {
                datos[0]=1;
                datos[1]=hayInteresadosReg;
                }
                else
                {
                datos[0]=0;
                datos[1]=hayInteresadosReg;
                }
                if(datos!=undefined){
                    if(datos[0] ==1) {
                        document.forms[0].opcionAdjuntar.value="noAltaTerceroDeRegistro";
                        if(datos[1] =="si") {
                            adjuntarInteresadosRegistro();
                        } else 
                            adjuntar(0);
                    }
                }  

            }else{
                var numeroExpediente = document.forms[0].numeroExpediente.value;  // Numero expediente
                var source = "<c:url value='/sge/Tramitacion.do?opcion=adjuntarExpedienteInteresadosInfo'/>";
                abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&numeroExpediente="+numeroExpediente,argumentos,
                        'width=750,height=400,status='+ '<%=statusBar%>',function(datosXanela){
                                if(datosXanela!=undefined){
                                    if(datosXanela[0] ==1) {
                                        document.forms[0].opcionAdjuntar.value="noAltaTerceroDeRegistro";
                                        if(datosXanela[1] =="si") {
                                            adjuntarInteresadosRegistro();
                                        } else 
                                            adjuntar(0);
                                    }
                                }  
                        });
            }
        }
    }


    function adjuntarExpedienteInteresadosInfoAux()
    {

            //Obtener los terceros principales
            document.forms[0].opcion.value="adjuntarExpedienteInteresadosInfoAux";
            document.forms[0].target="oculto";
            document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
            document.forms[0].submit();

    }

    function adjuntarExpedienteInfo()
    {
        if (validarObligatorios("<%=descriptor.getDescripcion("msjObligTodos")%>")) {
            mensaje = "<%=descriptor.getDescripcion("desAdjuntar")%>" + " " + document.forms[0].numeroExpediente.value + "?";
            if(jsp_alerta("C",mensaje) ==1) {
               adjuntarInteresadosRegistro();
            }
        }
    }

    function adjuntarInteresadosRegistro() {
        var argumentos = new Array();
        var numeroExpediente = document.forms[0].numeroExpediente.value;  // Numero expediente

        var source = "<c:url value='/sge/Tramitacion.do?opcion=adjuntarInteresadosSoloRegistro&numeroExpediente="+numeroExpediente+"'/>";
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&numeroExpediente="+numeroExpediente,argumentos,
	'width=750,height=400,status='+ '<%=statusBar%>',function(datos){
                            if(datos!=undefined) adjuntar(datos);
                  });
    }
    function adjuntar(option)
    {
        if(option==1)
        {
            document.forms[0].opcionAdjuntar.value="altaTerceroDeRegistro";
        }
        else
        {
            document.forms[0].opcionAdjuntar.value="noAltaTerceroDeRegistro";
        }

        document.forms[0].opcion.value="adjuntarNumero";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
        document.forms[0].submit();

    }

    function adjuntarExpediente(option){

        document.forms[0].opcion.value="adjuntarNumero";
        document.forms[0].target="oculto";
        document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
        document.forms[0].submit();

    }

    function recibirNumero(datos) {
      var retorno = new Array();
      retorno[0] = "si";
      retorno[1] = datos[0];
      retorno[2] = datos[1];
      retorno[3] = datos[2];
      retorno[4] = document.forms[0].numeroExpediente.value;
      self.parent.opener.retornoXanelaAuxiliar(retorno);
    }

    function existeExpediente() {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjNoExistExp")%>');
    }

    function noAdjuntar() {
      jsp_alerta('A','<%=descriptor.getDescripcion("msjNoAdjuntar")%>');
    }

    function pulsarCancelar() {
      self.parent.opener.retornoXanelaAuxiliar();
    }

    function pulsarConsultar() {
      document.forms[0].opcion.value="consultar";
      document.forms[0].target="oculto";
      document.forms[0].action="<c:url value='/sge/Tramitacion.do'/>";
      document.forms[0].submit();
    }

    function verExpediente(datos) {
      var argumentos = new Array();
      var codMun = datos[0];
      var codProc = datos[1];
      var nCS = datos[2];  // Ejercicio
      var codTram = datos[3];  // Numero expediente
      var source = "<c:url value='/sge/FichaExpediente.do?opcion=cargarEnVentana'/>";
      abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source+"&nCS="+nCS+
                    "&codMun="+codMun+"&codProc="+codProc+"&codTram="+codTram,argumentos,
                    'width=998,height=650,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                          pulsarAceptar();
                        }
                    });
    }

    function pulsarBuscar() {
      var argumentos = new Array();
      
      var source = "<c:url value='/sge/ConsultaExpedientes.do?opcion=inicioEnVentana'/>";
      abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,argumentos,
	'width=998,height=800,status='+ '<%=statusBar%>',function(datos){
                        if(datos!=undefined){
                          document.forms[0].numeroExpediente.value = datos[0];
                          pulsarAceptar();
                        }
                  });
    }
    </SCRIPT>
</head>

<BODY class="bandaBody" onload="javascript:{inicializar();}">
    <html:form action="/sge/Tramitacion.do" target="_self">

        <html:hidden  property="opcion" value=""/>
        <html:hidden  property="numeroExpedienteAntiguo"/>
        <input type="hidden" name="opcionAdjuntar">
        <input type="hidden" name="hayListaTerc">
        <input type="hidden" name="registroOrigen">
        <html:hidden  property="valorOpcionPermanencia"/>

    
    <div class="contenidoPantalla" valign="top" style="height: 100%; padding: 50px 5px 40px 5px">
        <table class="contenidoPestanha" border=0 cellspacing="0px" cellpadding="0px">
            <tr>
                <td style="width: 30%" class="etiqueta"><%=descriptor.getDescripcion("etiq_codExp")%>:</td>
                <td align="left"><html:text styleClass="inputTextoObligatorio" property="numeroExpediente" size="50" maxlength="30" onkeypress="javascript:xAMayusculas(this);"/></td>
            </tr>
        </table>
            
        <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();" accesskey="A">
            <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbConsultar")%> name="cmdConsultar" onclick="pulsarConsultar();" accesskey="C">
            <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbBuscar")%> name="cmdBuscar" onclick="pulsarBuscar();" accesskey="B">
            <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbcaNcelar")%> name="cmdCancelar" onclick="pulsarCancelar();" accesskey="N">
        </DIV>
    </div>
    </html:form>

        <script type="text/javascript">
            document.onmousedown = checkKeys;
    function checkKeysLocal(evento,tecla) {
        var aux=null;
        if(window.event)
            aux = window.event;
        else
            aux = evento;

        var tecla = 0;
        if(aux.keyCode)
            tecla = aux.keyCode;
        else
            tecla = aux.which;


        keyDel(aux);
    }
        </script>

    
        <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
    </BODY>
</html:html>
