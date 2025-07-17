<%@page contentType="text/html; charset=iso-8859-1"	language="java" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>	
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.*"%>
<%@page import="java.util.Vector"%>

<%
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
  ParametrosTerceroValueObject ptVO	= null;
  int	idioma=1;
  int	apl=3;
  int	cod_org=1;
  int	cod_dep=1;
  int	entCod = 1;
  String funcion = "";
  if (session.getAttribute("usuario") != null){
    usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    ptVO = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    idioma = usuarioVO.getIdioma();
    apl = usuarioVO.getAppCod();
    cod_org= usuarioVO.getUnidadOrgCod();
    cod_dep= usuarioVO.getDepCod();
    entCod = usuarioVO.getEntCod();    
  }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

<html:html>
 <head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <TITLE>::: EXPEDIENTES  Ver Tercero:::</TITLE>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
     <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
     <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
    <script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
    <SCRIPT type="text/javascript">
    var Terceros = new Array();
    var codTipoDocs = new Array();
    var descTipoDocs = new Array();

    function inicializar() {
      var t = 0;
        <%
         BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
         Vector listaTerceros = bForm.getListaTerceros();
         Vector listaDocs = bForm.getListaTipoDocs();
         int lengthTerceros = listaTerceros.size();
         int lengthDocs = listaDocs.size();
         int i=0;
        %>
        var m=0;
        <%for(int k=0;k<lengthDocs;k++){%>
        codTipoDocs[m] = '<%=(String)((GeneralValueObject)listaDocs.get(k)).getAtributo("codTipoDoc")%>';
        descTipoDocs[m] =	'<%=(String)((GeneralValueObject)listaDocs.get(k)).getAtributo("descTipoDoc")%>';
        m++;
        <%}%>
         var Domicilio = new Array();
         var k = 0;
         <%
           if(lengthTerceros > 0) {
               TercerosValueObject tercero = (TercerosValueObject)listaTerceros.lastElement();
               Vector listaDomicilios = tercero.getDomicilios();
               int lengthDomicilios = listaDomicilios.size();
               int j = 0;
               for(j=0;j<lengthDomicilios;j++){
                 DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject)listaDomicilios.get(j);
             %>
                 Domicilio[k] = ["<%=domicilio.getPais()%>","<%=domicilio.getProvincia()%>",
                   "<%=domicilio.getMunicipio()%>","<%=domicilio.getDomicilio()%>",
                   "<%=domicilio.getCodigoPostal()%>","<%=domicilio.getIdDomicilio()%>",
                   "<%=domicilio.getIdPais()%>","<%=domicilio.getIdProvincia()%>",
                   "<%=domicilio.getIdMunicipio()%>","<%=domicilio.getNumDesde()%>",
                   "<%=domicilio.getLetraDesde()%>","<%=domicilio.getNumHasta()%>",
                   "<%=domicilio.getLetraHasta()%>","<%=domicilio.getBloque()%>",
                   "<%=domicilio.getPortal()%>","<%=domicilio.getEscalera()%>",
                   "<%=domicilio.getPlanta()%>","<%=domicilio.getPuerta()%>",
                   "<%=domicilio.getBarriada()%>","<%=domicilio.getIdTipoVia()%>",
                   "<%=domicilio.getTipoVia()%>","<%=domicilio.getCodTipoUso()%>",
                   "<%=domicilio.getDescTipoUso()%>","<%=domicilio.getCodigoVia()%>",
                   "<%=domicilio.getNormalizado()%>","<%=domicilio.getIdVia()%>",
                   "<%=domicilio.getDescVia()%>","<%=domicilio.getDescESI()%>"];
                 k++;
             <%	}%>
               Terceros[t] = ["<%=tercero.getIdentificador()%>","<%=tercero.getVersion()%>",
                 "<%=tercero.getTipoDocumento()%>","<%=tercero.getDocumento()%>",
                 "<%=tercero.getNombre()%>","<%=tercero.getApellido1()%>",
                 "<%=tercero.getApellido2()%>","<%=tercero.getPartApellido1()%>",
                 "<%=tercero.getPartApellido2()%>","<%=tercero.getTelefono()%>",
                 "<%=tercero.getEmail()%>","<%=tercero.getNormalizado()%>",
                 "<%=tercero.getSituacion()%>","<%=tercero.getFechaAlta()%>",
                 "<%=tercero.getUsuarioAlta()%>","<%=tercero.getModuloAlta()%>",
                 "<%=tercero.getFechaBaja()%>","<%=tercero.getUsuarioBaja()%>",Domicilio];
        <% } %>
      if(Terceros.length >0) {
        cargar();
      }
    }

    function cargar() {
      var lCasas = new Array();
      var listaDoms = new Array();
      var domicilio = "";
      document.forms[0].documento.value = Terceros[0][3];
      document.forms[0].nombre.value = Terceros[0][4];
      if(Terceros[0][7] != "") {
        document.forms[0].apellidoPrim.value = Terceros[0][7] + " " + Terceros[0][5];
      } else {
        document.forms[0].apellidoPrim.value = Terceros[0][5];
      }
      if(Terceros[0][8] != "") {
        document.forms[0].apellidoSeg.value = Terceros[0][8] + " " + Terceros[0][6];
      } else {
        document.forms[0].apellidoSeg.value = Terceros[0][6];
      }
      document.forms[0].telefono.value = Terceros[0][9];
      if(Terceros[0][12]=="A"){
        lCasas = Terceros[0][18];
        for (var i=0; i <	lCasas.length; i++){
          var	domicilio =	"";
          domicilio = (lCasas[i][20]!="") ?lCasas[i][20]+" ":domicilio;
          if (lCasas[i][26]!='') {
            domicilio =	domicilio + " " + lCasas[i][26];
          } else if (lCasas[i][3]!='') {
//            domicilio	= (lCasas[i][20]!="") ?lCasas[i][20]+" ":domicilio;
            domicilio =	domicilio + " " + lCasas[i][3];
          }
          domicilio	= (lCasas[i][9]!=0) ? domicilio+" "+lCasas[i][9]:domicilio;
          domicilio	= (lCasas[i][10]!="") ?	domicilio+"	"+lCasas[i][10]+"	":domicilio;
          domicilio	= (lCasas[i][11]!=0) ? domicilio+" "+lCasas[i][11]:domicilio;
          domicilio	= (lCasas[i][12]!="") ?	domicilio+"	"+lCasas[i][12]:domicilio;
          domicilio	= (lCasas[i][13]!="") ?	domicilio+"	Bl. "+lCasas[i][13]:domicilio;
          domicilio	= (lCasas[i][14]!="") ?	domicilio+"	Portal "+lCasas[i][14]:domicilio;
          domicilio	= (lCasas[i][15]!="") ?	domicilio+"	Esc. "+lCasas[i][15]:domicilio;
          domicilio	= (lCasas[i][16]!="") ?	domicilio+"	"+lCasas[i][16]+"º ":domicilio;
          domicilio	= (lCasas[i][17]!="") ?	domicilio+lCasas[i][17]:domicilio;

          if (lCasas[i][27] != "null" && lCasas[i][27] != ""){
              domicilio = domicilio +", "+ lCasas[i][27];
          }
          listaDoms[i]  =	[lCasas[i][1],lCasas[i][2],domicilio,lCasas[i][4]];
        }
      }
      if(listaDoms.length != 0) {
        document.forms[0].provincia.value = listaDoms[0][0];
        document.forms[0].municipio.value = listaDoms[0][1];
        document.forms[0].codPostal.value = listaDoms[0][3];
        document.forms[0].domicilio.value = listaDoms[0][2];
      }
      for(var l=0;l < codTipoDocs.length;l++) {
        if(Terceros[0][2]==codTipoDocs[l]){
          document.forms[0].tipoDocumento.value = descTipoDocs[l];
          break;
        }
      }
    }

    function pulsarSalir() {
      self.close();
    }
    </SCRIPT>
 </head>

 <BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
        inicializar()}">
  
  
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
<html:form action="/BusquedaTerceros.do" target="_self">
    <html:hidden  property="opcion" value=""/>
    <div class="txttitblanco"><%=descriptor.getDescripcion("tit_verTerc")%></div>
    <div class="contenidoPantalla">
        <TABLE id ="tablaDatosGral">
            <tr>
                <td style="width: 21%" class="etiqueta"><%=descriptor.getDescripcion("res_tipoDoc")%>:</td>                                                                            
                <td>
                    <table width="100%" cellpadding="0px" cellspacing="0px">
                        <tr>
                            <td style="width: 30%" class="columnP">
                                <input id="tipoDocumento" name="tipoDocumento" type="text" class="inputTexto" size="15" readOnly>
                            </td>
                            <td style="width: 16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqDocumento")%>:</td>
                            <td valign="top" class="columnP">
                                <input id="documento" name="documento" type="text" class="inputTexto" size="15" readOnly>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="etiqueta"><%=descriptor.getDescripcion("gEtiq_nombre")%>:</td>
                <td class="columnP">
                    <input id="nombre" name="nombre" type="text" class="inputTexto" size="100" readOnly>
                </td>
            </tr>
            <tr>
                <td class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido1Part")%>:</td>
                <td class="columnP">
                    <input id="apellidoPrim" name="apellidoPrim" type="text" class="inputTexto" size="100" readOnly>
                </td>
            </tr>
            <tr>
                <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqApellido2Part")%>:</TD>
                <TD class="columnP">
                    <input id="apellidoSeg" name="apellidoSeg" type="text" class="inputTexto" size="100" readOnly>
                </TD>
            </tr>
            <tr>
                <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqTelef")%>:</TD>
                <TD class="columnP">
                    <input id="telefono" name="telefono" type="text" class="inputTexto" size="20" readOnly>
                </TD>
            </tr>
            <tr>
                <td class="etiqueta"><%=descriptor.getDescripcion("gEtiqProvincia")%>:</td>
                <td  class="columnP">
                    <input id="provincia" name="provincia" type="text" class="inputTexto" size="50" readOnly>
                </td>
            </tr>
            <tr>
                <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqMunicipio")%>:</TD>
                <TD valign="top" class="columnP">
                    <input id="municipio" name="municipio" type="text" class="inputTexto" size="50" readOnly>
                </TD>
            </tr>
            <tr>
                <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqCodPostal")%>:</TD>
                <TD valign="top" class="columnP">
                    <input id="codPostal" name="codPostal" type="text" class="inputTexto" size="10" readOnly>
                </TD>
            </tr>
            <tr>
                <TD class="etiqueta"><%=descriptor.getDescripcion("gEtiqDomicilio")%>:</TD>
                <TD valign="top" class="columnP">
                    <input id="domicilio" name="domicilio" type="text" class="inputTexto" size="100" readOnly>
                </TD>
            </tr>
        </table>
        <DIV id="capaBotones1" name="capaBotones1" class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" accesskey="S" value= '<%=descriptor.getDescripcion("gbSalir")%>' name="cmdSalir" onclick = "pulsarSalir();" >
        </DIV>      
    </DIV>      
</html:form>

    <script type="text/javascript">
    function checkKeysLocal(tecla) {
      keyDel();

      if ((event.button == 1)||(event.button == 2)){
        if(layerVisible) setTimeout("ocultarDiv()",30);
        if(divSegundoPlano)	divSegundoPlano = false;
      }
    }
    </script>


    <script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</BODY>

</html:html>
