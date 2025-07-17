<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm"%>
<%@page import="es.altia.agora.business.terceros.TercerosValueObject"%>
<%@page import="es.altia.agora.business.terceros.DomicilioSimpleValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.technical.EstructuraCampo"%>
<%@page import="es.altia.agora.technical.CamposFormulario"%>
<html>
    <head>
<title>Oculto Busqueda Terceros</title>
<%
    int idioma = 1;
    int apl = 3;
    if (session.getAttribute("usuario") != null) {
        UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        idioma = usuarioVO.getIdioma();
    }
%>
<jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"	property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"	property="apl_cod" value="<%=apl%>"	/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">

var ventana = "<bean:write name="BusquedaTercerosForm" property="ventana"/>";
var opcion="<%=request.getParameter("opcion")%>";
var frame;
if (parent.mainFrame){
    frame=parent.mainFrame;
} else {
    frame=parent;
}



var terceros = new Array();
var t = 0;

<%
  BusquedaTercerosForm bForm =(BusquedaTercerosForm)session.getAttribute("BusquedaTercerosForm");
  String cargarTerceroExp = (String)request.getAttribute("cargarTerceroExp");
  request.setAttribute("cargarTerceroExp",cargarTerceroExp);
  Vector listaTerceros = new Vector();
  if ("altaDomicilioGrabada".equals((String)request.getParameter("opcion"))||"modificacionGrabada".equals((String)request.getParameter("opcion"))){
      listaTerceros = bForm.getListaTercerosModificados();
  }else {
      listaTerceros = bForm.getListaTerceros();
  }
%>

<%!
  // Funcion para escapar strings para javascript
  private String escape(String str) {
      return StringEscapeUtils.escapeJavaScript(str);
  }
%>
    
<%for(int i=0;i<listaTerceros.size();i++) { %>
    var domicilios = new Array();
    var datosSuplementarios = new Array();
    var contDatosSuplementarios =0;
    var k = 0;
<%
    TercerosValueObject tercero = (TercerosValueObject)listaTerceros.get(i);
    Vector listaDomicilios = tercero.getDomicilios();
    int lengthDomicilios = 0;
    if (listaDomicilios != null) {
      lengthDomicilios = listaDomicilios.size();
    }

    for(int j=0; j<lengthDomicilios; j++){
      DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject)listaDomicilios.get(j);

      String pais = (domicilio.getPais()==null) ? "" : escape(domicilio.getPais());
      String provincia = (domicilio.getProvincia()==null) ? "" : escape(domicilio.getProvincia());
      String municipio = (domicilio.getMunicipio()==null) ? "" : escape(domicilio.getMunicipio());
      String dom = (domicilio.getDomicilio()==null) ? "" : escape(domicilio.getDomicilio());
      String codPostal = (domicilio.getCodigoPostal()==null) ? "" : escape(domicilio.getCodigoPostal());
      String idDomicilio = (domicilio.getIdDomicilio()==null) ? "" : domicilio.getIdDomicilio();
      String idPais = (domicilio.getIdPais()==null) ? "" : domicilio.getIdPais();
      String idProvincia = (domicilio.getIdProvincia()==null) ? "" : domicilio.getIdProvincia();
      String idMunicipio = (domicilio.getIdMunicipio()==null) ? "" : domicilio.getIdMunicipio();
      String numDesde = (domicilio.getNumDesde()==null) ? "" : domicilio.getNumDesde();
      String letraDesde = (domicilio.getLetraDesde()==null) ? "" : escape(domicilio.getLetraDesde());
      String numHasta = (domicilio.getNumHasta()==null) ? "" : domicilio.getNumHasta();
      String letraHasta = (domicilio.getLetraHasta()==null) ? "" : escape(domicilio.getLetraHasta());
      String bloque = (domicilio.getBloque()==null) ? "" : escape(domicilio.getBloque());
      String portal = (domicilio.getPortal()==null) ? "" : escape(domicilio.getPortal());
      String escalera = (domicilio.getEscalera()==null) ? "" : escape(domicilio.getEscalera());
      String planta = (domicilio.getPlanta()==null) ? "" : escape(domicilio.getPlanta());
      String puerta = (domicilio.getPuerta()==null) ? "" : escape(domicilio.getPuerta());
      String barriada = (domicilio.getBarriada()==null) ? "" : escape(domicilio.getBarriada());
      String idTipoVia = (domicilio.getIdTipoVia()==null) ? "" : domicilio.getIdTipoVia();
      String tipoVia = (domicilio.getTipoVia()==null) ? "" : escape(domicilio.getTipoVia());
      String codTipoUso = (domicilio.getCodTipoUso()==null) ? "" : domicilio.getCodTipoUso();
      String descTipoUso = (domicilio.getDescTipoUso()==null) ? "" : escape(domicilio.getDescTipoUso());
      String codigoVia = (domicilio.getCodigoVia()==null) ? "" : domicilio.getCodigoVia();
      String normalizado = (domicilio.getNormalizado()==null) ? "" : domicilio.getNormalizado();
      String idVia = (domicilio.getIdVia()==null) ? "" : domicilio.getIdVia();
      String codECO = (domicilio.getCodECO()==null) ? "" : domicilio.getCodECO();
      String codESI = (domicilio.getCodESI()==null) ? "" : domicilio.getCodESI();
      String descESI = (domicilio.getDescESI()==null) ? "" : escape(domicilio.getDescESI());
      String descVia = (domicilio.getDescVia()==null) ? "" : escape(domicilio.getDescVia());
      String origen = (domicilio.getOrigen()==null) ? "" : domicilio.getOrigen();

  %>
      domicilios[k] = ["<%=pais%>","<%=provincia%>",
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
  } // fin domicilios

    /**** CAMPOS SUPLEMENTARIOS *******/



    Vector<EstructuraCampo> estructuraDatosSuplementariosAux = (Vector<EstructuraCampo>) bForm.getEstructuraCamposSuplementariosTercero();
    Vector valoresDatosSuplementariosAux = (Vector) tercero.getValoresCamposSuplementarios();

    if (estructuraDatosSuplementariosAux != null && valoresDatosSuplementariosAux!=null){

        int lengthEstructuraDatosSuplementariosAux = estructuraDatosSuplementariosAux.size();
        int lengthValoresDatosSuplementariosAux = valoresDatosSuplementariosAux.size();
        if (lengthEstructuraDatosSuplementariosAux > 0)
        {
            int j=0;
            int yy=0;

            for (int hh = 0; hh < lengthEstructuraDatosSuplementariosAux; hh++)
            {
                EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementariosAux.elementAt(hh);
                CamposFormulario cF = (CamposFormulario) valoresDatosSuplementariosAux.elementAt(hh);
                String nombre = eC.getCodCampo();
                String valor = cF.getString(nombre);
                String tipo = eC.getCodTipoDato();
                String activo = eC.getActivo();

%>
        datosSuplementarios[contDatosSuplementarios] = ['<%=tercero.getIdentificador()%>','<%=nombre%>','<%=tipo%>','<%=eC.getObligatorio()%>','<%=valor%>','<%=activo%>'];        
        contDatosSuplementarios++;
<%
            }// for
        }// if
    }//if


    /**** CAMPOS SUPLEMENTARIOS ********/
 %>    
    terceros[t] = [
        "<%=tercero.getIdentificador()%>",
        "<%=tercero.getVersion()%>",
        "<%=tercero.getTipoDocumento()%>",
        "<%=tercero.getDocumento()%>",
        "<%=escape(tercero.getNombre())%>",
        "<%=escape(tercero.getApellido1())%>",
        "<%=escape(tercero.getApellido2())%>",
        "<%=escape(tercero.getPartApellido1())%>",
        "<%=escape(tercero.getPartApellido2())%>",
        "<%=escape(tercero.getTelefono())%>",
        "<%=escape(tercero.getEmail())%>",
        "<%=tercero.getNormalizado()%>",
        "<%=tercero.getSituacion()%>",
        "<%=tercero.getFechaAlta()%>",
        "<%=tercero.getUsuarioAlta()%>",
        "<%=tercero.getModuloAlta()%>",
        "<%=tercero.getFechaBaja()%>",
        "<%=tercero.getUsuarioBaja()%>",
        domicilios,
        "<%=tercero.getOrigen()%>",
        "<%=tercero.getDomPrincipal()%>",
         "<%=tercero.getCodTerceroOrigen()%>",
        datosSuplementarios];

        
t++;
<%}%>

var contServicios= 0;

if (opcion == 'cargarBusqueda' || opcion == 'seleccionar') {
    frame.recuperaBusquedaTerceros(terceros);
} 
else if (opcion == 'demasiadosResultadosBusquedaTerceros') {    
   frame.errorDemasiadosTerceros();
} 
else if (opcion == 'altaTerceroGrabada') {    
    frame.altaTerceroGrabada(terceros);
} else if (opcion == 'altaDomicilioGrabada') {
    frame.altaDomicilioGrabada(terceros);
} else if (opcion == 'modificacionGrabada') {
    frame.modificacionGrabada(terceros);
} else if (opcion == 'cargarTerceroExpedientes') {
    frame.cargarTerceroExpedientes(terceros);
} else if (opcion == 'terceroBuscado') {
    frame.terceroBuscado(terceros);
} else if (opcion == 'terceroBuscadoDoms') {
    frame.mostrarListaDomicilios(terceros);
} else if (opcion == 'errorTecnicoAltaTercero') {    
    // Se muestra un mensaje de ha ocurrido un error genérico durante el alta del tercero
    frame.mostrarErrorAltaTercero('<%=descriptor.getDescripcion("msgErrTecAltaTercero")%>');    
} else if (opcion == 'altaTerceroFlexiaOKErrorSistemasExternos') {        
    frame.altaTerceroFlexiaNoSistemasExternos(terceros);

    <c:if test="${not empty sessionScope.BusquedaTercerosForm.erroresSistemaExterno}">
        var msj = new Array();
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/erroresAltaTerceroSistemaExterno.jsp',msj,
	'width=720,height=540,scrollbars=no,status=no',function(datos){});
    </c:if>
    
}else
if(opcion=='errorAltaTerceroTransaccionErroresSistemasExternos'){    
    frame.errorAltaTerceroTransaccionErroresSistemasExternos();
    <c:if test="${not empty sessionScope.BusquedaTercerosForm.erroresSistemaExterno}">
        var msj = new Array();
        abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/erroresAltaTerceroSistemaExterno.jsp',msj,
	'width=720,height=540,scrollbars=no,status=no',function(datos){});
    </c:if>

}else
if(opcion=="errorTransaccionAltaTercero"){    
    frame.errorTecnicoAltaTerceroExterno();
}else {
    alert('Opcion desconocida en ocultoBusquedaTerceros: ' + opcion);
}


<c:if test="${not empty requestScope.errores}">	
    var msjErrores = new Array();	
    var i = 0;	
    <c:forEach items="${requestScope.errores}" var="error">
        msjErrores[i++] = "<fmt:message key="BusquedaTercero/falloBusqueda"/> <c:out value="${error}"/>";	
    </c:forEach>	
    abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/terceros/erroresBusquedaTercero.jsp',msjErrores,
	'width=420,height=300,status=no',function(){});	
</c:if>
</script>

</head>
<body>
</body>

</html>
