<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.*"%>
<%@page import="java.util.Vector"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html><head>
<title>Oculto Busqueda Terceros</title>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<%
    UsuarioValueObject usuarioVO;
    ParametrosTerceroValueObject ptVO = null;
    int idioma = 1;
    int apl = 1;
    if ((session.getAttribute("usuario") != null)) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
        apl = usuarioVO.getAppCod();
    }
    if (session.getAttribute("parametrosTercero") != null){
        ptVO = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    } else {
        ptVO = new ParametrosTerceroValueObject();
        ptVO.setPais("108"); //Código España
        ptVO.setProvincia("99"); //codigo_provincia_desconocido
        ptVO.setMunicipio("999"); //codigo_municipio_desconocido
    }
    String posTerc = request.getParameter("posTerc");
    String posDom = request.getParameter("posDom");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">    
<%BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");%>
var ventana = "<bean:write name="BusquedaTercerosForm" property="ventana"/>";
var opcion="<%=request.getParameter("opcion")%>";
var codMunicipios = new Array();
var descMunicipios = new Array();
var array_cod = new Array();
var codPostales = new Array();
var frame;
var frameOculto;
frame=(ventana=="true")?parent.mainFrame:parent.mainFrame;
frameOculto=(ventana=="true")?"oculto":"oculto";
/* Anadir ECO/ESI */
var codESIs = new Array();
var descESIs = new Array();
var listaECOESIs = new Array()
var idVias = new Array();
var codVias = new Array();
var descVias = new Array();

function cargarListaEsis(){
<%
Vector listaEsis = bForm.getListaESIs();
int lengthEsis = listaEsis.size();
int i2;
String codESIs="";
String descESIs="";
String listaECOESIs="";
if (lengthEsis > 0) {
        for(i2 = 0; i2 < lengthEsis-1; i2++){
                GeneralValueObject esiss = (GeneralValueObject)listaEsis.get(i2);
                codESIs += "\""+esiss.getAtributo("codEntidadSingular")+"\",";
                descESIs += "\""+esiss.getAtributo("nombreOficial")+"\",";
                listaECOESIs +="[\""+esiss.getAtributo("codEntidadColectiva")+"\",\""
                                                + esiss.getAtributo("descEntidadColectiva")+"\"],";
        }
        GeneralValueObject esiss = (GeneralValueObject)listaEsis.get(i2);
        codESIs += "\""+esiss.getAtributo("codEntidadSingular")+"\"";
        descESIs += "\""+esiss.getAtributo("nombreOficial")+"\"";
        listaECOESIs +="[\""+esiss.getAtributo("codEntidadColectiva")+"\",\""
                                                + esiss.getAtributo("descEntidadColectiva")+"\"]";
}	// Fin if (length > 0) 
%>
    frame.codESIs = [<%=codESIs%>];
    frame.descESIs = [<%=descESIs%>];
    frame.listaECOESIs = [<%=listaECOESIs%>];
    frame.auxCombo = 'comboESI';
    frame.cargarComboBox(frame.codESIs,frame.descESIs);
}

/* Fin anadir ECO/ESI */
function cargarListaMunicipios(){
    <%	
    	String lcodMun = "";
		String ldescMun = "";	
		Vector listaMunicipios = bForm.getListaMunicipios();
      	int lengthMun = listaMunicipios.size();
    	int i;

		if(lengthMun>0) {	
	      for(i=0;i<lengthMun-1;i++){
	        GeneralValueObject  mun= (GeneralValueObject)listaMunicipios.get(i);
			lcodMun+= "'" + mun.getAtributo("codMunicipio")+"',";
			ldescMun+= "\""+ mun.getAtributo("nombreOficial")+"\",";
		  }
		  GeneralValueObject  mun = (GeneralValueObject)listaMunicipios.get(i);
		  lcodMun+= "'"+ mun.getAtributo("codMunicipio")+"'";
		  ldescMun+= "\""+ mun.getAtributo("nombreOficial")+"\"";
		}    	
    %>
    codMunicipios = [<%= lcodMun %>];
    descMunicipios = [<%= ldescMun %>];
    frame.codMunicipios = codMunicipios;
    frame.descMunicipios = descMunicipios;
    frame.auxCombo = 'comboMunicipio';
    frame.cargarComboBox(codMunicipios,descMunicipios);
    if (frame.document.forms[0].codMunicipio.value=="") {
        frame.comboMunicipio.selectItem(0); 
    }
    // Si se ha seleccionado la provincia por defecto, seleccionamos municipio por defecto
    if (frame.document.forms[0].codProvincia.value == "<%=ptVO.getProvincia()%>") {
        frame.document.forms[0].codMunicipio.value = "<%=ptVO.getMunicipio()%>";
        frame.document.forms[0].descMunicipio.value = "<%=ptVO.getNomMunicipio()%>";
    }
}
function cargarBusqueda(){
    <c:if test="${not empty requestScope.errores}">
        var msjErrores = new Array();
        var i = 0;
        <c:forEach items="${requestScope.errores}" var="error">
            msjErrores[i++] = "<fmt:message key="BusquedaTercero/falloBusqueda"/> <c:out value="${error}"/>";
        </c:forEach>
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/erroresBusquedaTercero.jsp',msjErrores,
	'width=420,height=300,status=no',function(datos){});
    </c:if>
    var Terceros = new Array();
	var t = 0;
    <%
    Vector listaTerceros = bForm.getListaTerceros();
    int lengthTerceros = listaTerceros.size();

    for(i=0;i<listaTerceros.size();i++){%>
      var Domicilio = new Array();
      var k = 0;
      <%
        TercerosValueObject tercero = (TercerosValueObject)listaTerceros.get(i);
        Vector listaDomicilios = tercero.getDomicilios();
        int lengthDomicilios = 0;
        if (listaDomicilios != null) {
          lengthDomicilios = listaDomicilios.size();
        } 

        for(int j=0;j<lengthDomicilios;j++){
          DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject)listaDomicilios.get(j);
          String pais = domicilio.getPais();
          if (pais==null)   pais = "";
          String provincia = domicilio.getProvincia();
          if (provincia==null)   provincia = "";
          String municipio = domicilio.getMunicipio();
          if (municipio==null)   municipio = "";
          String dom = domicilio.getDomicilio();
          if (dom==null)   dom = "";
          String codPostal = domicilio.getCodigoPostal();
          if (codPostal==null)   codPostal = "";
          String idDomicilio = domicilio.getIdDomicilio();
          if (idDomicilio==null)   idDomicilio = "";
          String idPais = domicilio.getIdPais();
          if (idPais==null)   idPais = "";
          String idProvincia = domicilio.getIdProvincia();
          if (idProvincia==null)   idProvincia = "";
          String idMunicipio = domicilio.getIdMunicipio();
          if (idMunicipio==null)   idMunicipio = "";
          String numDesde = domicilio.getNumDesde();
          if (numDesde==null)   numDesde = "";
          String letraDesde = domicilio.getLetraDesde();
          if (letraDesde==null)   letraDesde = "";
          String numHasta = domicilio.getNumHasta();
          if (numHasta==null)   numHasta = "";
          String letraHasta = domicilio.getLetraHasta();
          if (letraHasta==null)   letraHasta =  "";
          String bloque = domicilio.getBloque();
          if (bloque==null)   bloque = "";
          String portal = domicilio.getPortal();
          if (portal==null)   portal = "";
          String escalera = domicilio.getEscalera();
          if (escalera==null)   escalera = "";
          String planta = domicilio.getPlanta();
          if (planta==null)   planta = "";
          String puerta = domicilio.getPuerta();
          if (puerta==null)   puerta = "";
          String barriada = domicilio.getBarriada();
          if (barriada==null)   barriada =  "";
          String idTipoVia = domicilio.getIdTipoVia();
          if (idTipoVia==null)   idTipoVia = "";
          String tipoVia = domicilio.getTipoVia();
          if (tipoVia==null)   tipoVia = "";
          String codTipoUso = domicilio.getCodTipoUso();
          if (codTipoUso==null)   codTipoUso = "";
          String descTipoUso = domicilio.getDescTipoUso();
          if (descTipoUso==null)   descTipoUso = "";
          String codigoVia = domicilio.getCodigoVia();
          if (codigoVia==null)   codigoVia = "";
          String normalizado = domicilio.getNormalizado();
          if (normalizado==null)   normalizado = "";
          String idVia = domicilio.getIdVia();
          if (idVia==null)   idVia = "";
          String codECO = domicilio.getCodECO();
          if (codECO==null)   codECO = "";
          String codESI = domicilio.getCodESI();
          if (codESI==null)   codESI = "";
          String descESI = domicilio.getDescESI();
          if (descESI==null)   descESI = "";
          String descVia = domicilio.getDescVia();
          if (descVia==null)   descVia = "";
          String origen = domicilio.getOrigen();
          if (origen==null)   origen = "";

      %>
          Domicilio[k] = ["<%=pais%>","<%=provincia%>",
            "<%=municipio%>","<%=dom%>",
            "<%=codPostal%>","<%=idDomicilio%>",
            "<%=idPais%>","<%=idProvincia%>",
            "<%=idMunicipio%>","<%=numDesde%>",
            "<%=letraDesde%>","<%=numHasta%>",
            "<%=letraHasta%>","<%=bloque%>",
            "<%=portal%>","<%=escalera%>",
            "<%=planta%>","<%=puerta%>",
            "<%=barriada%>","<%=idTipoVia%>",
            "<%=tipoVia%>","<%=codTipoUso%>",
            "<%=descTipoUso%>","<%=codigoVia%>",
            "<%=normalizado%>","<%=idVia%>",
			"<%=codECO%>","<%=codESI%>",
            "<%=descESI%>", "<%=descVia%>",
            "<%=origen%>"];
          k++;
      <%
      }
      String nombreTercero = tercero.getNombre();
      nombreTercero = nombreTercero.replaceAll("\"", "'");
      %>
        Terceros[t] = ["<%=tercero.getIdentificador()%>","<%=tercero.getVersion()%>",
          "<%=tercero.getTipoDocumento()%>","<%=tercero.getDocumento()%>",
          "<%=nombreTercero%>","<%=tercero.getApellido1()%>",
          "<%=tercero.getApellido2()%>","<%=tercero.getPartApellido1()%>",
          "<%=tercero.getPartApellido2()%>","<%=tercero.getTelefono()%>",
          "<%=tercero.getEmail()%>","<%=tercero.getNormalizado()%>",
          "<%=tercero.getSituacion()%>","<%=tercero.getFechaAlta()%>",
          "<%=tercero.getUsuarioAlta()%>","<%=tercero.getModuloAlta()%>",
          "<%=tercero.getFechaBaja()%>","<%=tercero.getUsuarioBaja()%>",Domicilio,"<%=tercero.getOrigen()%>"];
        t++;
    <%}%>
    // Si venimos de eliminarTercero y la lista esta vacia, no es que no haya resultados de la busqueda,
    // es que hemos borrado el unico tercero que habia en la lista! (esta funcion la comparten 'buscar'
    // y 'eliminarTercero') Hay que evitar que salga el mensaje 'Desea dar de alta al interesado?'
    if ("<%=lengthTerceros%>" == "0" && opcion == 'eliminarTercero') {
      frame.limpiarTodo();
    // En este caso hay que borrar los datos que puedan quedar del tercero borrado antes de cargar la busqueda.
    } else if (opcion == 'eliminarTercero') {
      frame.limpiarTodo();
      frame.recuperaBusquedaTerceros(Terceros);
    } else {
      frame.recuperaBusquedaTerceros(Terceros);
    }
}

function cargarSeleccionar(){
    var Terceros = new Array();
    var t = 0;
    <%
      listaTerceros = bForm.getListaTerceros();

      for(i=0;i<listaTerceros.size();i++){%>
      var Domicilio = new Array();
      var k = 0;
      <%
        TercerosValueObject tercero = (TercerosValueObject)listaTerceros.get(i);
        Vector listaDomicilios = tercero.getDomicilios();
        int lengthDomicilios = 0;
        if (listaDomicilios != null) {
          lengthDomicilios = listaDomicilios.size();
        } 
        
        for(int j = 0;j<lengthDomicilios;j++){
          DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject)listaDomicilios.get(j);
          String pais = domicilio.getPais();
          if (pais==null)   pais = "";
          String provincia = domicilio.getProvincia();
          if (provincia==null)   provincia = "";
          String municipio = domicilio.getMunicipio();
          if (municipio==null)   municipio = "";
          String dom = domicilio.getDomicilio();
          if (dom==null)   dom = "";
          String codPostal = domicilio.getCodigoPostal();
          if (codPostal==null)   codPostal = "";
          String idDomicilio = domicilio.getIdDomicilio();
          if (idDomicilio==null)   idDomicilio = "";
          String idPais = domicilio.getIdPais();
          if (idPais==null)   idPais = "";
          String idProvincia = domicilio.getIdProvincia();
          if (idProvincia==null)   idProvincia = "";
          String idMunicipio = domicilio.getIdMunicipio();
          if (idMunicipio==null)   idMunicipio = "";
          String numDesde = domicilio.getNumDesde();
          if (numDesde==null)   numDesde = "";
          String letraDesde = domicilio.getLetraDesde();
          if (letraDesde==null)   letraDesde = "";
          String numHasta = domicilio.getNumHasta();
          if (numHasta==null)   numHasta = "";
          String letraHasta = domicilio.getLetraHasta();
          if (letraHasta==null)   letraHasta =  "";
          String bloque = domicilio.getBloque();
          if (bloque==null)   bloque = "";
          String portal = domicilio.getPortal();
          if (portal==null)   portal = "";
          String escalera = domicilio.getEscalera();
          if (escalera==null)   escalera = "";
          String planta = domicilio.getPlanta();
          if (planta==null)   planta = "";
          String puerta = domicilio.getPuerta();
          if (puerta==null)   puerta = "";
          String barriada = domicilio.getBarriada();
          if (barriada==null)   barriada =  "";
          String idTipoVia = domicilio.getIdTipoVia();
          if (idTipoVia==null)   idTipoVia = "";
          String tipoVia = domicilio.getTipoVia();
          if (tipoVia==null)   tipoVia = "";
          String codTipoUso = domicilio.getCodTipoUso();
          if (codTipoUso==null)   codTipoUso = "";
          String descTipoUso = domicilio.getDescTipoUso();
          if (descTipoUso==null)   descTipoUso = "";
          String codigoVia = domicilio.getCodigoVia();
          if (codigoVia==null)   codigoVia = "";
          String normalizado = domicilio.getNormalizado();
          if (normalizado==null)   normalizado = "";
          String idVia = domicilio.getIdVia();
          if (idVia==null)   idVia = "";
          String codECO = domicilio.getCodECO();
          if (codECO==null)   codECO = "";
          String codESI = domicilio.getCodESI();
          if (codESI==null)   codESI = "";
          String descESI = domicilio.getDescESI();
          if (descESI==null)   descESI = "";
          String descVia = domicilio.getDescVia();
          if (descVia==null)   descVia = "";
          String origen = domicilio.getOrigen();
          if (origen==null)   origen = "";
      %>
          Domicilio[k] = ["<%=pais%>","<%=provincia%>",
            "<%=municipio%>","<%=dom%>",
            "<%=codPostal%>","<%=idDomicilio%>",
            "<%=idPais%>","<%=idProvincia%>",
            "<%=idMunicipio%>","<%=numDesde%>",
            "<%=letraDesde%>","<%=numHasta%>",
            "<%=letraHasta%>","<%=bloque%>",
            "<%=portal%>","<%=escalera%>",
            "<%=planta%>","<%=puerta%>",
            "<%=barriada%>","<%=idTipoVia%>",
            "<%=tipoVia%>","<%=codTipoUso%>",
            "<%=descTipoUso%>","<%=codigoVia%>",
            "<%=normalizado%>","<%=idVia%>",
			"<%=codECO%>","<%=codESI%>",
            "<%=descESI%>", "<%=descVia%>",
            "<%=origen%>"];
          k++;
      <%}

      String nombreTercero = tercero.getNombre();
      nombreTercero = nombreTercero.replaceAll("\"", "'");

      %>
        Terceros[t] = ["<%=tercero.getIdentificador()%>","<%=tercero.getVersion()%>", "<%=tercero.getTipoDocumento()%>",
                "<%=tercero.getDocumento()%>", "<%=nombreTercero%>","<%=tercero.getApellido1()%>",
                "<%=tercero.getApellido2()%>","<%=tercero.getPartApellido1()%>", "<%=tercero.getPartApellido2()%>",
                "<%=tercero.getTelefono()%>", "<%=tercero.getEmail()%>","<%=tercero.getNormalizado()%>",
                "<%=tercero.getSituacion()%>","<%=tercero.getFechaAlta()%>", "<%=tercero.getUsuarioAlta()%>",
                "<%=tercero.getModuloAlta()%>", "<%=tercero.getFechaBaja()%>","<%=tercero.getUsuarioBaja()%>",
                Domicilio,"<%=tercero.getOrigen()%>"];
        t++;
    <%}%>
    var posTerc = 0;
    var posDom = 0;
    frame.recuperaBusquedaTercerosSeleccionar(Terceros,posTerc,posDom);
}
function grabar(){
    var ids = new Array();
    var i=0;
    <logic:iterate id="bucle" name="BusquedaTercerosForm" property="listaTerceros">
        ids[i] = "<bean:write name="bucle" property="identificador"/>";
        i++;
    </logic:iterate>
  	if(ids[0]!=0){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjTercGrab")%>');
		frame.grabado(ids[0]);
    }else{
        frame.grabado(0);
    }
    if (opcion == "grabarExt"){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjDomExtGrabar")%>');
        frame.cargandoTerceroExt=true;
        frame.actualizarBotonesDom();
    }
}
    
function existe (){
    // Este caso se usa para varias jsp
    frame.confirmarDocDuplicado();
}
function existeDoc (){
 jsp_alerta("A",'<%=descriptor.getDescripcion("msjDocDuplicado")%>');
 frame.getTerceroAntiguo();
}
function existeTerceroSinDoc (){
    frame.confirmarExisteTercero();
}

function grabarDomicilioExt(){
    var idDomicilio = new Array();
    var j=0;
    var domActual;
    <logic:iterate id="bucle" name="BusquedaTercerosForm" property="listaTerceros">
        <logic:present name="bucle" property="domicilios">
            <logic:iterate id="doms" name="bucle" property="domicilios">
                idDomicilio[j] = "<bean:write name="doms" property="idDomicilio"/>";
                domActual = "<bean:write name="doms" property="domActual"/>";
                j++;
            </logic:iterate>
        </logic:present>
    </logic:iterate>
    if (idDomicilio[0]==0) {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjDomNoGrab")%>');
    }
    frame.domicilioGrabadoExt(idDomicilio[0],domActual);
}

function grabarDomicilioExtTercAgora(){
    var idDomicilio = new Array();
    var j=0;
	<logic:iterate id="bucle" name="BusquedaTercerosForm" property="listaTerceros">
	  <logic:present name="bucle" property="domicilios">
	    <logic:iterate id="doms" name="bucle" property="domicilios">
		    idDomicilio[j] = "<bean:write name="doms" property="idDomicilio"/>";
			j++;
		</logic:iterate>
      </logic:present>
	</logic:iterate>
    if (idDomicilio[0]==0) {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjDomNoGrab")%>');
    }
    frame.domicilioGrabadoExtTercAgora(idDomicilio[0]);
}

function grabarDomicilio(){
    var idDomicilio = new Array();
    var j=0;
    <logic:iterate id="bucle" name="BusquedaTercerosForm" property="listaTerceros">
      <logic:present name="bucle" property="domicilios">
        <logic:iterate id="doms" name="bucle" property="domicilios">
            idDomicilio[j] = "<bean:write name="doms" property="idDomicilio"/>";
            j++;
        </logic:iterate>
      </logic:present>
    </logic:iterate>
    if(idDomicilio[0]!=0){
        frame.domicilioGrabado(idDomicilio[0]);
    }else jsp_alerta("A",'<%=descriptor.getDescripcion("msjDomNoGrab")%>');
    frame.habilitarSalir();
}
function modificar(){
    var ids = new Array();
    var i=0;
    <logic:iterate id="bucle" name="BusquedaTercerosForm" property="listaTerceros">
        ids[i] = "<bean:write name="bucle" property="identificador"/>";
        i++;
    </logic:iterate>
    if(ids[0]!=0 && ids[0]!=-1){
        var version =frame.document.forms[0].txtVersion.value;
        version++;
        frame.document.forms[0].txtVersion.value=version;
        frame.recuperarModificar();
    } else if(ids[0] == -1) {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjTercDuplic")%>');
        frame.getTerceroAntiguo();
    } else {
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjTercNMod")%>');
        frame.getTerceroAntiguo();
    }
}
function codigosPostales(){
    <%
      bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
      Vector listaCPostales = bForm.getListaCodPostales();
      int lengthCPostales = listaCPostales.size();
    %>
    var j=0;
    <%for(i=0;i<lengthCPostales;i++){%>
      codPostales[j] = "<%=((GeneralValueObject)listaCPostales.get(i)).getAtributo("codPostal")%>";
          j++;
    <%}%>
    frame.codPostales = codPostales;
    frame.auxCombo = "comboPostal";
    frame.cargarComboBox(codPostales,codPostales);
}

function modificarDomicilio(){
    var resultado = '<bean:write name="BusquedaTercerosForm" property="resOp"/>';
    frame.domicilioModificado(resultado);
}

/* Esta función alerta de la existencia de varios terceros que comparten un mismo domicilio. 
 * Llama a la función domicilioCompartido(respuesta) en 'mantTerceros.jsp' */
function alertarDomicilioCompartido(){     
    var respuesta = jsp_alerta('','<%=descriptor.getDescripcion("msjDomCompartido")%>');
    frame.domicilioCompartido(respuesta);
}

/* Esta funcion se lanza cuando al grabar un tercero externo, aparece otro en la base de datos del SGE con
 * datos del documento iguales */
function compararTerceros() {
    jsp_alerta('A', '<%=descriptor.getDescripcion("msjExisteTerceroId")%>');
    var terceros = new Array();
    var i = 0;
    terceros[i++] = '0';
    terceros[i++] = '0';
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.tipoDocumento}"/>";
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.tipoDocDesc}"/>";
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.documento}"/>";
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.nombre}"/>";
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.apellido1}"/>";
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.apellido2}"/>";
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.partApellido1}"/>";
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.partApellido2}"/>";
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.telefono}"/>";
    terceros[i++] = "<c:out value="${requestScope.terceroNuevo.email}"/>";
    <c:forEach items="${requestScope.tercerosRepetidos}" var="terceroViejo">
        terceros[i++] = "<c:out value="${terceroViejo.identificador}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.version}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.tipoDocumento}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.tipoDocDesc}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.documento}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.nombre}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.apellido1}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.apellido2}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.partApellido1}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.partApellido2}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.telefono}"/>";
        terceros[i++] = "<c:out value="${terceroViejo.email}"/>";
    </c:forEach>

    var source = "<%=request.getContextPath()%>/jsp/terceros/compararTerceros.jsp";
    abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source="+source, terceros,
	'width=795,height=415,status=no',function(terceroSelected){
                        if (terceroSelected != undefined) {
                            if(terceroSelected[0] != 0) {
                                if (terceroSelected[12] == 'false') {
                                    frame.grabado(terceroSelected[0]);
                                    frame.buscarPorId();
                                } else {
                                    frame.incorporarDomicilio(terceroSelected[0]);                      
                                }
                            }
                            else {
                                frame.grabado(0);
                                frame.recargaDatosTercero(terceroSelected[1], terceroSelected[2], terceroSelected[3],
                                        terceroSelected[4], terceroSelected[5], terceroSelected[6], terceroSelected[7],
                                        terceroSelected[8], terceroSelected[9], terceroSelected[10], terceroSelected[11]);
                                frame.cargandoTerceroExt=true;
                                frame.actualizarBotonesDom();
                            }
                        }
                });
}
//comprobamos que el alta y el domicilio fueron grabados 
function altaRapidaCorrecta(){  
     var idDomicilio = new Array();
    var j=0;
    <logic:iterate id="bucle" name="BusquedaTercerosForm" property="listaTerceros">
      <logic:present name="bucle" property="domicilios">
        <logic:iterate id="doms" name="bucle" property="domicilios">
            idDomicilio[j] = "<bean:write name="doms" property="idDomicilio"/>";
            j++;
        </logic:iterate>
      </logic:present>
    </logic:iterate>
    var ids = new Array();
    var i=0;
    <logic:iterate id="bucle" name="BusquedaTercerosForm" property="listaTerceros">
        ids[i] = "<bean:write name="bucle" property="identificador"/>";
        i++;
    </logic:iterate>
  	if((ids[0]!=0)){
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjTercGrab")%>');
		frame.altaCorrecta(ids[0],idDomicilio[0]);
    }else{
        frame.altaCorrecta(0);
    }
}


if (opcion=="buscar"){
    cargarBusqueda();
}else if (opcion=="grabar"){
    grabar();
}else if (opcion=="grabarExt"){
    grabar();
}else if (opcion=="existe"){
    existe();
}else if (opcion=="existeDoc"){
    existeDoc();
}else if (opcion=="existeTerceroSinDoc"){
    existeTerceroSinDoc();
}else if (opcion=="cambiaSituacionTercero"){
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjTercMod")%>');
    frame.cerrar();
}else if (opcion=="modificarTercero"){
    modificar();
}else if (opcion=="grabarDomicilio"){
    grabarDomicilio();
  }else if (opcion=="grabarDomicilioExt"){
    grabarDomicilioExt();
}else if (opcion=="grabarDomicilioExtTercAgora"){
    grabarDomicilioExtTercAgora();
}else if (opcion=="cargarVias"){
    cargarListaVias();
}else if (opcion=="descripcionVias"){
    cargarListaVias();
}else if (opcion=="cargarCodPostales"){
    codigosPostales();
}else if (opcion=="cargarMunicipios"){
    cargarListaMunicipios();
    
}else if(opcion=="cargarListas"){
        cargarListaEsis();
}else if (opcion=="modificarDomicilio"){
    modificarDomicilio();
}else if (opcion=="alertarDomicilioCompartido"){
    alertarDomicilioCompartido();
}else if (opcion=="eliminarTercero"){
    cargarBusqueda();
}else if (opcion=="seleccionar") {
    cargarSeleccionar();
} else if (opcion == "existeExt") {
    compararTerceros();    
} else if (opcion == "altaRapidaCorrecta"){
    altaRapidaCorrecta();
} else if (opcion == "altaTerceroGrabada") {
    altaTerceroGrabada();
}else if(opcion== "ocultoAltaTerceroSinDomicilio"){
    ocultoAltaTerceroSinDomicilio();
}
</script>

</head>
<body>
</body>

</html>
