<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<html:html>
<head><jsp:include page="/jsp/infDireccion/tpls/app-constants.jsp" />
<TITLE>::: Informes de Direccion:::</TITLE>
<%
  int idioma=1;
  int apl=1;
  int codOrg = 0;
  int codEnt = 0;
  String organizacion = "";
  String entidad = "";
  String ico = "";
  String css = "";
  String idSesion = null;
  if (session!=null){
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    if (usuario!=null) {
      idioma = usuario.getIdioma();
      apl = usuario.getAppCod();
	  codOrg = usuario.getOrgCod();
	  organizacion = usuario.getOrg();
	  codEnt = usuario.getEntCod();
	  entidad = usuario.getEnt();
	  ico = usuario.getOrgIco();
	  css = usuario.getCss();
    }
    idSesion = session.getId();
  }	
  
  Config m_Config = ConfigServiceHelper.getConfig("common");
  String statusBar = m_Config.getString("JSP.StatusBar");
%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />


<!-- Ficheros JavaScript -->

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>

<SCRIPT type="text/javascript">

function inicializar() {
    tp1.setSelectedIndex(0);
}


function pulsarRegistro(opcionAqui) {
  document.forms[0].opcion.value = opcionAqui;  
  document.forms[0].target="oculto"; 
  document.forms[0].action="<%=request.getContextPath()%>/infDireccion/InformesDireccion.do";
  document.forms[0].submit();
}

//function pulsarConsultaEstadisticas() {
//  document.forms[0].opcion.value = "estadisticasPadron";
//  document.forms[0].target="oculto";
//  document.forms[0].action="<%=request.getContextPath()%>/infDireccion/InformesDireccion.do";
//  document.forms[0].submit();
//}

//function pulsarConsultaPiramides(){
//  document.forms[0].opcion.value = "consultaPiramide";
//  document.forms[0].target="oculto";
//  document.forms[0].action="<%=request.getContextPath()%>/infDireccion/InformesDireccion.do";
//  document.forms[0].submit();
//}
//
function pulsarConsultaExpedientes() {
  document.forms[0].opcion.value = "consultaExpedientes";
  document.forms[0].target="oculto";

  document.forms[0].action="<%=request.getContextPath()%>/infDireccion/InformesDireccion.do";
  document.forms[0].submit();
}

function irAExpedientes() {
  var ancho = 992;
  var alto = 540;
  x = screen.availWidth/2 - ancho/2;
  y = screen.availHeight/2 - alto/2;
  
  var argumentos = new Array();
  argumentos['informesDireccion'] = true;
  argumentos['ventanaPadre'] = self;
  
  var source = '<%=request.getContextPath()%>/sge/ConsultaExpedientes.do?opcion=inicio&ventana=true';
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source, argumentos,
	'width='+ancho+',height='+alto+',top='+y+',left='+x+',status='+ '<%=statusBar%>',function(){});
}

function pulsarExpAreaProcedimiento() {
  document.forms[0].opcion.value = "ExpAreaProcedimiento";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/infDireccion/InformesDireccion.do";
  document.forms[0].submit();
}

function recuperaRegistro(opcion, opcionAnterior) {
  var source = "";  
  
  if (opcion=="seleccionarUOR") { 
      var argumentos = new Array();
      argumentos['opcionAnterior'] = opcionAnterior;
      source ="<%=request.getContextPath()%>/jsp/registro/seleccionarUOR.jsp?opcionAnterior=" + opcionAnterior;
      abrirXanelaAuxiliar(source,argumentos,
	'width=350,height=440,status='+ '<%=statusBar%>',function(resp){
                        if (resp != undefined ) {
                                document.forms[0].opcion.value=resp[0];
                                document.forms[0].unidadOrgCod.value=resp[1];
                                document.forms[0].unidadOrg.value=resp[2];
                                document.forms[0].target="oculto";
                                document.forms[0].action="<%=request.getContextPath()%>/infDireccion/InformesDireccion.do";
                                document.forms[0].submit();
                            }
                    });
        } else {
          // borro seleccion
          document.forms[0].unidadOrgCod.value="";
          document.forms[0].unidadOrg.value="";

          var width='992px';
          var height='470px';

          if (opcion=="registroLibroE") {
              source ="<%=request.getContextPath()%>/jsp/registro/informeLibroRegistroES.jsp?tipo=E";
          } else if (opcion=="registroLibroS") {
              source ="<%=request.getContextPath()%>/jsp/registro/informeLibroRegistroES.jsp?tipo=S";
          } else if (opcion=="registroUORE") {
              source ="<%=request.getContextPath()%>/jsp/registro/RelUnOrgES.jsp?tipo=E";
              height='510px';
          } else if (opcion=="registroUORS") {
              source ="<%=request.getContextPath()%>/jsp/registro/RelUnOrgES.jsp?tipo=S";
              height='510px';
          } else if (opcion=="registroTotales") {
              source ="<%=request.getContextPath()%>/InformesRegistro.do?opcion=informeEstadisticas";
              width='800px';
              height='630px';
        }
        var argumentos = new Array();
        argumentos['informesDireccion'] = true;
        argumentos['ventanaPadre'] = self;

        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source, argumentos,
            'width='+width+',height='+height+',top=75,left=150,scrollbars=yes,status='+ '<%=statusBar%>',function(){});
    }
}

function recuperaExpAreaProcedimiento() {
  var source = "<%=request.getContextPath()%>/informes/Informes.do?opcion=cargarHTM&nCS=cargarHTM&"+
                "grupos=are,pro&filtros=are,-1,pro,-1"+
  			   "&eje=true&num=true&codTram=true&codClasifTram=HTM&nombreCodTram=ALL";
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source, '',
        'width=992,height=600,top=75,left=150,scrollbars=yes,status='+ '<%=statusBar%>',function(){});
}

function pulsarExpUnidTramitadora() {
  document.forms[0].opcion.value = "ExpUnidTramitadora";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/infDireccion/InformesDireccion.do";
  document.forms[0].submit();
}

function recuperaExpUnidTramitadora() {
    var source = "<%=request.getContextPath()%>/informes/Informes.do?opcion=cargarHTM&nCS=cargarHTM&grupos=utr&filtros=utr,-1"+
            "&eje=true&num=true&codTram=true&codClasifTram=HTM&nombreCodTram=ALL";
  abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source, '',
        'width=992,height=600,top=75,left=150,scrollbars=yes,status='+ '<%=statusBar%>',function(){});
}

function pulsarExpProcUnidTramit() {
  document.forms[0].opcion.value = "ExpProcUnidTramit";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/infDireccion/InformesDireccion.do";
  document.forms[0].submit();
}

function recuperaExpProcUnidTramit() {
    var source = "<%=request.getContextPath()%>/informes/Informes.do?opcion=cargarHTM&nCS=cargarHTM&grupos=pro,utr&filtros=pro,-1,utr,-1"+
            "&eje=true&num=true&codTram=true&codClasifTram=HTM&nombreCodTram=ALL";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source, '',
        'width=992,height=600,top=75,left=150,scrollbars=yes,status='+ '<%=statusBar%>',function(){});
}

function pulsarExpProcTipoTramite() {
  document.forms[0].opcion.value = "ExpProcTipoTramite";
  document.forms[0].target="oculto";
  document.forms[0].action="<%=request.getContextPath()%>/infDireccion/InformesDireccion.do";
  document.forms[0].submit();
}


        

function recuperaExpProcTipoTramite() {
    var source = "<%=request.getContextPath()%>/informes/Informes.do?opcion=cargarHTM&nCS=cargarHTM&grupos=pro,cls&filtros=pro,-1,cls,-1"+
            "&eje=true&num=true&codTram=false&codClasifTram=HTM&nombreCodTram=ALL";
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source, '',
        'width=992,height=600,top=75,left=150,scrollbars=yes,status='+ '<%=statusBar%>',function(){});
}

function pulsarSalir() {
  var resultado = jsp_alerta("",'<%= descriptor.getDescripcion("g_confirmSalirApp")%>');
  if (resultado == 1){
    document.forms[0].target = '_top';
    document.forms[0].action = '<%=request.getContextPath()%>/SalirApp.do?app=10';
    document.forms[0].submit();
  }
}

// Se llama desde el popup para abrir el informe. Si se intenta abrir desde el popup falla el servlet verPdf.jsp

function verInforme(nombre) {
    var sourc = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre=" + nombre;
    ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + sourc, 'ventanaInforme', 'width=1000px,height=750px,status=' + '<%=statusBar%>' + ',toolbar=no');
    ventanaInforme.focus();
}

</SCRIPT>

</head>

<body class="bandaBody"  onload="javascript:{inicializar()}">

<form name="formInformes" id="formInformes" target="_self">
<!-- hay + d 1 reg -->
<input type="hidden" name="opcion" id="opcion" value="">	
<input type="hidden" name="unidadOrgCod" id="unidadOrgCod">
<input type="hidden" name="unidadOrg" id="unidadOrg">


<input type="hidden" name="codProc" id="codProc" value="">
<input type="hidden" name="codMun" id="codMun" value="">
<input type="hidden" name="codClasifTram" id="codClasifTram" value="">
<input type="hidden" name="nombreCodTram" id="nombreCodTram" value="">
<input type="hidden" name="eje" id="eje" value="">
<input type="hidden" name="num" id="num" value="">
<input type="hidden" name="codTram" id="codTram" value="">
<div class="txttitblanco">INFORMES DE DIRECCIÓN</div>
<div class="contenidoPantalla">
  <div class="tab-pane" id="tab-pane-1">
  <script type="text/javascript">
    tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
  </script>
  <!-- CAPA 1: REXISTRO
  ------------------------------ -->
  <div class="tab-page" id="tabPage1" style="height:400px">
    <h2 class="tab"><%= descriptor.getDescripcion("pestRegistro")%></h2>
    <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>
        <table class="contenidoPestanha" cellspacing="1" cellpadding="0">
          <tr>											  
            <td style="width: 67%" colspan="3" class="etiqueta">
                <%= descriptor.getDescripcion("titRegGeneral")%>
                </td>
          </tr>
          <tr>							
            <td style="width: 2%; background-color: #FFFFFF">&nbsp;</td>
            <td style="width: 98%" class="encabezadoGris" colspan="2" align="left">
                <%= descriptor.getDescripcion("titListados")%>
            </td>
          </tr>
          <tr>
            <td style="width: 2%" align="left" bordercolor="#FFFFFF" height="5">&nbsp;</td>
            <td width="65%" align="left" colspan="2" bordercolor="#FFFFFF" height="5">-
              <a href='javascript: pulsarRegistro("registroLibroE");' class="enlaceEtiqueta"><%= descriptor.getDescripcion("IDEncab1")%>:</a>&nbsp;
              <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto1")%></span>						  
                  </td>

          </tr>
          <tr>
            <td width="2%" align="left" bordercolor="#FFFFFF" height="5"></td>
            <td width="65%" align="left" colspan="2" bordercolor="#FFFFFF" height="5">-
              <a href='javascript: pulsarRegistro("registroLibroS");' class="enlaceEtiqueta"><%= descriptor.getDescripcion("IDEncab2")%>:</a>
              <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto2")%></span></td>
          </tr>
          <tr>
            <td width="2%" align="left" bordercolor="#FFFFFF" height="5"></td>
            <td width="65%" align="left" colspan="2" bordercolor="#FFFFFF" height="5">-
              <a href='javascript: pulsarRegistro("registroUORE");' class="enlaceEtiqueta"><%= descriptor.getDescripcion("IDEncab3")%>:</a>&nbsp;
              <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto3")%></span></td>
          </tr>
          <tr>
            <td width="2%" align="left" bordercolor="#FFFFFF" height="5"></td>
            <td width="65%" align="left" colspan="2" bordercolor="#FFFFFF" height="5">-
              <a href='javascript: pulsarRegistro("registroUORS");' class="enlaceEtiqueta"><%= descriptor.getDescripcion("IDEncab4")%>:</a>&nbsp; 
              <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto4")%></span></td>
          </tr>
          <tr>
                <td style="width: 2%; background-color: #FFFFFF">&nbsp;</td>
            <td style="width: 98%" class="encabezadoGris" colspan="2" align="left">
                <%= descriptor.getDescripcion("titEstadisticas")%>
            </td>
          </tr>
          <tr>
            <td width="2%" bordercolor="#FFFFFF" align="left" height="5">&nbsp;</td>
            <td width="65%" bordercolor="#FFFFFF" align="left" colspan="2" height="5">-
              <a href='javascript: pulsarRegistro("registroTotales");' class="enlaceEtiqueta"><%= descriptor.getDescripcion("IDEncab5")%>:</a>&nbsp;
              <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto5")%></span></td>
          </tr>
        </table>
    </div>

    <!-- CAPA 2: PADRON
    ------------------------------- -->

    <%--                 <div class="tab-page" id="tabPage2" style="width:700px;height:400px">

    <h2 class="tab">PADRÓN</h2>

    <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>
                  <table border="1" width="89%" height="400">
                        <tr>
                            <td width="67%" colspan="3" bgcolor="#B0BDCD" align="left" height="16"><font face="Arial" size="2">PADRÓN DE
                              HABITANTES</font></td>
                          </tr>
                          <tr>
                            <td width="67%" colspan="3" bgcolor="#e6e6e6" align="left" height="14"><font face="Arial" size="1">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          PIRÁMIDES&nbsp;</font></td>
                          </tr>
                          <tr>
                            <td width="8%" bordercolor="#FFFFFF" align="left" height="5">&nbsp;</td>
                            <td width="59%" bordercolor="#FFFFFF" align="left" colspan="2" height="5">-
                              <font face="Arial" size="2"> <a href='javascript: pulsarConsultaPiramides();' class="direccion">Pirámides de poboación</a>&nbsp;
                              </font><font face="Arial" size="1">Pirámides de poboación por rango de
                              idade, sexo e entidade colectiva.</font></td>
                          </tr>
                          <tr>
                            <td width="67%" colspan="3" bgcolor="#e6e6e6" align="left" height="18"><font face="Arial">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                              <font size="1">ESTADISTICAS</font></font></td>
                          </tr>
                          <tr>
                            <td width="8%" align="left" bordercolor="#FFFFFF" height="5">&nbsp;</td>
                            <td width="59%" align="left" colspan="2" bordercolor="#FFFFFF" height="5"><font face="Arial" size="1">-
          </font><font face="Arial" size="2">Habitantes
          empadroados:</font><font face="Arial" size="2">
          </font><font face="Arial" size="1">Número
                              total de persoas empadroadas no concello.</font></td>
                          </tr>
                          <tr>
                            <td width="8%" align="left" bordercolor="#FFFFFF" height="5">&nbsp;</td>
                            <td width="59%" align="left" colspan="2" bordercolor="#FFFFFF" height="5"><font face="Arial" size="1">-
          </font><font face="Arial" size="2">Altas padroais</font><font face="Arial" size="1"><b>:
          </b></font><font face="Arial" size="2"> </font><font size="1"><font face="Arial">Datos
                              estadísticos das altas anuais producidas no concello por cambios de
                              residencia, nacementos, etc</font></font></td>
                          </tr>

                            <td width="8%" align="left" bordercolor="#FFFFFF" height="5">&nbsp;</td>
                            <td width="59%" align="left" colspan="2" bordercolor="#FFFFFF" height="5"><font face="Arial" size="2">-
          Baixas&nbsp; padroais: </font><font size="1"><font face="Arial">Datos
                              estadísticos das baixas anuais producidas no&nbsp;
          concello por cambios de residencia, defuncións,
          duplicidades, etc.</font></font></td>


                          <tr>
                            <td width="8%" align="right" bordercolor="#FFFFFF" height="5">&nbsp;</td>
                            <td width="59%" align="right" colspan="2" bordercolor="#FFFFFF" height="5">
                              <a href='javascript:pulsarConsultaEstadisticas();' class="direccion"><font face="Arial" size="2">Ver
          cuadro estadístico</font></a></td>
                          </tr>




                        </table>

    </div>--%>



                  <!-- CAPA 3: EXPEDIENTES
    ------------------------------- -->

    <div class="tab-page" id="tabPage3" style="height:400px">

    <h2 class="tab"><%= descriptor.getDescripcion("pestGestExp")%></h2>

    <script type="text/javascript">tp1_p3 = tp1.addTabPage( document.getElementById( "tabPage3" ) );</script>
    <table class="contenidoPestanha">
      <tr>						
              <td width="67%" colspan="3" class="etiqueta">
                  <%= descriptor.getDescripcion("titGesExp")%>
              </td>
            </tr>					 
            <tr>
                <td width="2%" bgcolor="#FFFFFF">&nbsp;</td>
                <td style="width: 98%" class="encabezadoGris" colspan="2" align="left">					  
                    <%= descriptor.getDescripcion("titConsultas")%>
                </td>
            </tr>
            <tr>
              <td width="2%" bordercolor="#FFFFFF" align="left" height="5">&nbsp;</td>
              <td width="65%" bordercolor="#FFFFFF" align="left" colspan="2" height="5">-
                  <a href='javascript: pulsarConsultaExpedientes();' class="enlaceEtiqueta"><%= descriptor.getDescripcion("IDEncab6")%>:</a>
                  <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto6")%></span>
                  </td>
            </tr>
            <tr>
                <td width="2%" bgcolor="#FFFFFF">&nbsp;</td>
                <td style="width: 98%" class="encabezadoGris" colspan="2" align="left">
                    <%= descriptor.getDescripcion("titEstadisticas")%>
                </td>
            </tr>
            <tr>
              <td width="2%" bordercolor="#FFFFFF" align="left" height="5">&nbsp;</td>
              <td width="65%" bordercolor="#FFFFFF" align="left" colspan="2" height="5">-
                  <a href='javascript: pulsarExpAreaProcedimiento();' class="enlaceEtiqueta"><%= descriptor.getDescripcion("IDEncab7")%>:</a>
                  <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto7")%></span></td>
            </tr>
            <tr>
              <td width="2%" bordercolor="#FFFFFF" align="left" height="5"></td>
              <td width="65%" bordercolor="#FFFFFF" align="left" colspan="2" height="5">-
                          <a href='javascript: pulsarExpUnidTramitadora();' class="enlaceEtiqueta"><%= descriptor.getDescripcion("IDEncab8")%>:</a>
                  <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto8")%></span></td>
            </tr>
            <tr>
              <td width="2%" bordercolor="#FFFFFF" align="left" height="5"></td>
              <td width="65%" bordercolor="#FFFFFF" align="left" colspan="2" height="5">- 
                          <a href='javascript: pulsarExpProcUnidTramit();' class="enlaceEtiqueta"><%= descriptor.getDescripcion("IDEncab9")%>:</a>
                  <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto9")%></span></td>
            </tr>
            <tr>
              <td width="2%" bordercolor="#FFFFFF" align="left" height="5">&nbsp;</td>
              <td width="65%" bordercolor="#FFFFFF" align="left" colspan="2" height="5">- 
                          <a href='javascript: pulsarExpProcTipoTramite();' class="enlaceEtiqueta">
                                  <%= descriptor.getDescripcion("IDEncab10")%>:
                          </a>
                          <span class="etiquetaInforme"><%= descriptor.getDescripcion("IDTexto10")%></span></td>
            </tr>
    </table>
    </div>
    </div>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%= descriptor.getDescripcion("gbSalir")%>" name="cmdAlta2" onClick="pulsarSalir();">
        </div>
    </div>
</form>
</body>
</html:html>
