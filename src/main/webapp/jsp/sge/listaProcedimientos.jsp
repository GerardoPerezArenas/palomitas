<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm" %>
<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: LISTA PROCEDIMIENTOS :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<%int idioma=1;
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
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<SCRIPT type="text/javascript">
<%
    String desdeBuzonEntrada = "no";
    if(request.getAttribute("desdeBuzonEntrada")!=null)
        desdeBuzonEntrada = (String) request.getAttribute("desdeBuzonEntrada");
%>
    
var datos = new Array();
var datosOriginal = new Array();
var listaRemarcar = new Array();
var codUOR = null;
var cont = 0;
var cont1 = 0;

var UTR = null; 
var UORtramite = null;

var desdeBuzonEntrada = '<%=desdeBuzonEntrada%>';
var registroOrigen = '<c:out value="${requestScope.registroOrigen}"/>';
var hayListaTerc = '<c:out value="${requestScope.hayListaTerc}"/>';


function inicializar() {
  // Recuperamos los datos del procedimiento indicado en el asiento (si hay)
  var codProc = '<bean:write name="TramitacionForm" property="codProcedimiento"/>';
  var codMun = '<bean:write name="TramitacionForm" property="codMunicipio"/>';
  // Creamos listado
  <logic:iterate id="elemento" name="TramitacionForm" property="listaProcedimientos">
    datos[cont] = [ '<bean:write name="elemento" property="txtCodigo"/>', 
                      '<bean:write name="elemento" property="txtDescripcion"/>'];      
    datosOriginal[cont]=['<bean:write name="elemento" property="codMunicipio"/>',datos[cont][0],datos[cont][1]];
    if (datosOriginal[cont][0] == codMun && datosOriginal[cont][1] == codProc) listaRemarcar[cont] = true;
    else listaRemarcar[cont] = false;
    
    cont++;    
  </logic:iterate> 
  tab.observaciones=listaRemarcar;
  tab.lineas=datos;
  tab.displayTabla();
  window.focus();
}

function seleccionarUTR(i,lista) {
   
  var posicion=i;
  if (lista.length > 0 ) {
    if (lista.length == 1){
        UTR = lista[0][2];
        prepararSeleccionarUORTramiteInicio(posicion,UTR);
    } else {
      var source = "<c:url value='/jsp/sge/listaUORsUsuario.jsp'/>?tipoSel=1";
      abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,lista,
                    'width=625,height=430,status='+ '<%=statusBar%>',function(resp){ 
      	if (resp != undefined) {
        	UTR = resp;
            
                prepararSeleccionarUORTramiteInicio(posicion,UTR);
                
      	} 
       
      });
      
     
    } 
  }
}
function seleccionarUORTramiteInicio(i,uorProc,lista) {

  var posicion=i;
  if (lista.length > 0 ) {
    if (lista.length == 1){
        UORtramite = lista[0][2];
        seleccionarProcedimiento(posicion, uorProc, UORtramite);
    } else {
      var source = "<c:url value='/jsp/sge/listaUORsUsuario.jsp'/>?tipoSel=2"
      abrirXanelaAuxiliar(source,lista,
                    'width=625,height=365,status='+ '<%=statusBar%>',function(resp){ 
      	if (resp != undefined) {
        	UORtramite = resp;  
                if (UORtramite == '') UORtramite = uorProc;
                if (uorProc != null && UORtramite != null)
                {
                    seleccionarProcedimiento(posicion, uorProc, UORtramite);
                }
      	} else return null;
      });
    }
  } else{
        UORtramite = uorProc;
        if (uorProc != null && UORtramite != null)
        {
          seleccionarProcedimiento(posicion, uorProc, UORtramite);
        }
  } 
  return UORtramite;
}

function seleccionarProcedimiento(i, uor, uorTramiteInicio) {    
  document.forms[0].codMunicipio.value = datosOriginal[i][0];
  document.forms[0].codProcedimiento.value = datosOriginal[i][1];
  var retorno = ["si",document.forms[0].codMunicipio.value,document.forms[0].codProcedimiento.value,uor,
      uorTramiteInicio]; 
  self.parent.opener.retornoXanelaAuxiliar(retorno);
}
 
 // #260881: pulsarAceptar() hace lo mismo que pulsarAceptarFila(i) pero obteniendo i que es la fila seleccionada, 
 //se simplifica el código haciendo que una llame a la otra
function pulsarAceptarFila(i) {    
    if (i >= 0) {
        if(desdeBuzonEntrada=="si"){
            var procedimiento = tab.lineas[i][0];
            comprobarProcedimiento(i,procedimiento);
        } else {
            iniciarExpediente(i);
        }
    } else {
        jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }
}

function iniciarExpediente(i){
    if(jsp_alerta("C",'<%=descriptor.getDescripcion("msjIniExp")%>' + " " + datosOriginal[i][2] + " ?") ==1) {
       var codUOR = prepararSeleccionUTR(i);
    }
}

function pulsarAceptar() { 
    var i = tab.selectedIndex;
    pulsarAceptarFila(i);
}

function recuperarListaInteresadosAnotacion(){
    var listasInteresados = new Array();
    var codInteresados = '';
    var nvrInteresados = '';
    var docInteresados = '';
    var separador = "##";
      <%
        MantAnotacionRegistroForm anotacionForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
        ArrayList<GeneralValueObject> listaInt = new ArrayList<GeneralValueObject>(anotacionForm.getListaInteresados());
        for(int index=0; index<listaInt.size(); index++){
            GeneralValueObject gVO = listaInt.get(index);
      %>
            codInteresados += '<%=(String) gVO.getAtributo("codigoTercero")%>' + separador; 
            nvrInteresados += '<%=(String) gVO.getAtributo("versionTercero")%>' + separador;      
            docInteresados += '<%=(String) gVO.getAtributo("doc")%>' + separador;      
      <% } %>
    listasInteresados = [codInteresados, nvrInteresados, docInteresados];
    return listasInteresados;
}

function comprobarProcedimiento(fila,codProc){
    try{
        $.ajax({
            url:  "<%=request.getContextPath() %>" + "/sge/Tramitacion.do",
            type: 'POST',
            async: true,
            data: {'filaSelec':fila,'codProcedimiento':codProc,'opcion':'comprobarProcedimiento'},
            success: procesarRespuestaComprobarProcedimiento,
            error: mostrarErrorRespuestaComprobarProcedimiento
        });           
    }catch(Err){
        alert("Error.descripcion: " + Err.description);
    }
}

function comprobarExistenciaExpedientes(fila,codProc,interesados){
    try{
        $.ajax({
            url:  "<%=request.getContextPath() %>" + "/sge/Tramitacion.do",
            type: 'POST',
            async: true,
            data: {'filaSelec':fila,'codProcedimiento':codProc,'codsTerc':interesados[0],'nvrsTerc':interesados[1],'docusTerc':interesados[2],'opcion':'hayExpedientesIntYProc'},
            success: procesarRespuestaComprobarExistenExpedientes,
            error: mostrarErrorRespuestaComprobarExistenExpedientes
        });           
    }catch(Err){
        alert("Error.descripcion: " + Err.description);
    }
}

function prepararSeleccionUTR(i )
{
    var posicion=i;
    var ajax = getXMLHttpRequest();
    var codProcedimiento = datosOriginal[i][1];
    var codMunicipio     = datosOriginal[i][0];

    var codUOR=null;

    if(ajax!=null) { 
        var url = "<%=request.getContextPath() %>" + "/sge/Tramitacion.do"; 

         var parametros = "&opcion=seleccionarUor&codProc=" + escape(codProcedimiento) + "&codMun=" + escape(codMunicipio)

        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);

        try {            
            if (ajax.readyState==4 && ajax.status==200) {
                // En IE el XML viene en responseText y no en la propiedad responseXML
               var text = ajax.responseText; 

               var datosRespuesta = text.split("#");               
               var lUOR = new Array();               
               for (i = 0; i < datosRespuesta.length - 1 ; i++) {                   
                    var linea = datosRespuesta[i].split("|");
                    lUOR[i]=[linea[0],linea[1],linea[2]];
               }
               codUOR = seleccionarUTR(posicion,lUOR);
            }
        }catch(Err){
            alert("Error.descripcion: " + Err.description);
        }
        return codUOR;
    }
}


function prepararSeleccionarUORTramiteInicio(i,uorProc) {
    var posicion=i;
    var ajax = getXMLHttpRequest();
    var codProcedimiento = datosOriginal[i][1];
    var codMunicipio     = datosOriginal[i][0];

    var codUOR=null;

    if(ajax!=null) { 
        var url = "<%=request.getContextPath() %>" + "/sge/Tramitacion.do"; 

         var parametros = "&opcion=seleccionarUorTramiteInicio&codProc=" + escape(codProcedimiento) + "&codMun=" + escape(codMunicipio)

        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);

        try {            
            if (ajax.readyState==4 && ajax.status==200) {
                // En IE el XML viene en responseText y no en la propiedad responseXML
               var text = ajax.responseText; 
               var datosRespuesta = text.split("#");               
               var lUOR = new Array();
               for (i = 0; i < datosRespuesta.length - 1 ; i++) {
                    var linea = datosRespuesta[i].split("|");
                    lUOR[i]=[linea[0],linea[1],linea[2]];
               }
               codUOR = seleccionarUORTramiteInicio(posicion,uorProc,lUOR);
            }
        }catch(Err){
            alert("Error.descripcion: " + Err.description);
        }
        return codUOR;
    } 
}

function recibirNumero(datos) {
    document.forms[0].numero.value = datos[0];
    jsp_alerta("A",'<%=descriptor.getDescripcion("msjIniExp1")%>' + " " + document.forms[0].numero.value);
    var retorno = ["si",document.forms[0].codMunicipio.value,document.forms[0].codProcedimiento.value,datos[1],
        document.forms[0].numero.value,codUOR]; 

    self.parent.opener.retornoXanelaAuxiliar(retorno);
}
function noRecibirNumero() {
  jsp_alerta("A","No se pudo crear el numero de expediente");
}
function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function callFromTableTo(rowID,tableName){
  if(tab.id == tableName){
    pulsarAceptarFila(rowID);
  }
}

 function getXMLHttpRequest(){
        var aVersions = [ "MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp","Microsoft.XMLHttp"
          ];

        if (window.XMLHttpRequest){
                // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                return new XMLHttpRequest();
        }else if (window.ActiveXObject){
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                    try {
                        var oXmlHttp = new ActiveXObject(aVersions[i]);
                        return oXmlHttp;
                    }catch (error) {
                    //no necesitamos hacer nada especial
                    }
             }
        }
     }

// FUNCIONES DE RESPUESTAS AJAX
function procesarRespuestaComprobarProcedimiento(ajaxResult){
    var datos;
    if(ajaxResult){
        datos = JSON.parse(ajaxResult);
        datos = datos.tabla;
        if(datos.comprobar=="si"){
             var interesados = recuperarListaInteresadosAnotacion();
             comprobarExistenciaExpedientes(datos.filaSelec,datos.codProc,interesados);
        } else {
            iniciarExpediente(datos.filaSelec);
        }
    }
}

function mostrarErrorRespuestaComprobarProcedimiento(){
    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
}

function procesarRespuestaComprobarExistenExpedientes(ajaxResult){
    var datos;
    if(ajaxResult){
        datos = JSON.parse(ajaxResult);
        datos = datos.tabla;
        var listado = datos.relacionExpedientes;
        if(listado.length>0){
            var source = "<%=request.getContextPath()%>/jsp/sge/numeroExpedienteInteresadoPorProcedimiento.jsp";
            var datosAEnviar = new Array();
            datosAEnviar.push(listado);
            datosAEnviar.push(datos.codProcedimiento);
            datosAEnviar.push(registroOrigen);
            datosAEnviar.push(hayListaTerc);
            
            abrirXanelaAuxiliar("<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source="+source,datosAEnviar,
	'width=880,height=570,status=<%=statusBar%>,scrollbars=no',function(respuesta){
                if(respuesta!="undefined"){
                    if(respuesta[0]=="iniciarNuevo"){
                         var codUOR = prepararSeleccionUTR(datos.filaSelec);
                       
                    } else {
                        respuesta[0]="adjuntar";
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                    }
                }
            });
        } else {
            iniciarExpediente(datos.filaSelec);
        }
    }
}

function mostrarErrorRespuestaComprobarExistenExpedientes(){
    jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrGenServ")%>');
}

</SCRIPT>
</head>
<BODY  onload="javascript:{inicializar();}">
<html:form action="/sge/Tramitacion.do" target="_self">
    <html:hidden  property="opcion" value=""/>
    <html:hidden  property="codMunicipio" value=""/>
    <html:hidden  property="codProcedimiento" value=""/>
    <html:hidden  property="ejercicio" value=""/>
    <html:hidden  property="numero" value=""/>
    <html:hidden  property="uor" value=""/>
    <html:hidden  property="codTercero" value=""/>
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_listProc")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr>
                <td id="tabla"></td>
            </tr>
            <tr>
                <td>
                    <div id="enlace" class="dataTables_wrapper paxinacionDataTables"></div>
                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" accesskey="A" value=<%=descriptor.getDescripcion("gbAceptar")%> name="cmdAceptar" onclick="pulsarAceptar();">
            <input type="button" class="botonGeneral" accesskey="C" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar"  onclick="pulsarCancelar();">
        </div>
    </div>
</html:form>
<script type="text/javascript">
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
tab.addColumna('120','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tab.addColumna('355','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
tab.displayCabecera=true;
function rellenarDatos(tableName,rowID){
      tableObject = tableName;
      var selRow = eval("document.all." + tableName.id + "_Row" + tableName.selectedIndex);

}
function checkKeysLocal(evento,tecla) {
     var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

  keyDel(evento);
  if ( (teclaAuxiliar == 40) || (teclaAuxiliar == 38)){
    upDownTable(tab,datos,teclaAuxiliar);
  }
}
</script>

<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>
</BODY>
</html:html>
