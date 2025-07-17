<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html:html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
<TITLE>::: LISTA EXPEDIENTES RELACIONADOS :::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<%
  int idioma=1;
  int apl=4;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
        }
  }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>

<script type="text/javascript">
var codigoCampo            = '<c:out value="${requestScope.codigo}"/>';
var tipoCampo              = '<c:out value="${requestScope.tipo}"/>';
var nombreCampo            = '<c:out value="${requestScope.nombre}"/>';
var esCampoSuplementario   = '<c:out value="${requestScope.esCampoSuplementario}"/>';
var tipoCampoSuplementario = '<c:out value="${requestScope.tipoCampoSuplementario}"/>';
var tipoCampoFijo          = '<c:out value="${requestScope.tipoCampoFijo}"/>';
var codigoDesplegable      = '<c:out value="${requestScope.codigoDesplegable}"/>';
var operador               = '<c:out value="${requestScope.operador}"/>';

<%!
  // Funcion para escapar strings para javascript
  private String escape(String str) {
      return StringEscapeUtils.escapeJavaScript(str);
    }
%>

function inicializar() {
  window.focus();  
}

function pulsarCancelar() {
  self.parent.opener.retornoXanelaAuxiliar();
}

function comprobarFecha(inputFecha) { 
  var formato = 'dd/mm/yyyy';
  if (Trim(inputFecha.value)!='') {      
      var D = ValidarFechaConFormato(inputFecha.value,formato);      
      if (!D[0]){
        jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
        document.getElementById(inputFecha.name).focus();
        document.getElementById(inputFecha.name).select();
        return false;
      } else {          
        inputFecha.value = D[1];
        return true;
      }
  }
  return true;
}

function ValidarFechaConFormato(fecha, formato) {
  if (formato==null) formato ="dd/mm/yyyy";
  if (formato=="mm/yyyy")
      fecha = "01/"+fecha;
  else if (formato=="yyyy")
      fecha ="01/01/"+fecha;
  else if (formato =="mmyyyy")
      fecha = "01"+fecha;

  var D = DataValida(fecha);
  if (formato == "dd/mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr() : fecha;
  else if (formato == "mm/yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  else if (formato == "yyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(6) : fecha;
  else if (formato == "mmyyyy") D[1] = D[0] ?  D[1].ISOlocaldateStr().substring(3) : fecha;
  return D;
}


function pulsarAceptar(){ 
    var datos = new Array();    
    if(tipoCampo=="N"){
        // Campo numérico
        datos = tratarDatosNumerico();        
    }else
    if(tipoCampo=="T" || tipoCampo=="E"){
        // Campo de tipo texto o nº de expsediente
        datos = tratarDatosTexto();
    }else
    if(tipoCampo=="D"){
        // Campo desplegable
        datos = tratarDatosDesplegable();
    }else
    if(tipoCampo=="F"){
        // Campo fecha
        datos = tratarDatosFecha();
    }else
    if(tipoCampo=="I"){
        // Campo interesado
        datos = tratarDatosInteresado();
    }else
    if(tipoCampo=="DN"){
        // Campo documento
        datos = tratarDatosDocumento();
    }
    
    if(datos!=null && datos!=undefined && datos.length>0){
        self.parent.opener.retornoXanelaAuxiliar(datos);
    }
}


function tratarDatosNumerico(){  
    var operador = document.getElementById("valorOperadorCriterio").value;
    var valor1   = document.getElementById("valorBusqueda1").value;
    var valor2   = document.getElementById("valorBusqueda2").value;
    var continuar = true;

    if(operador==""){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
        continuar = false;
    }else
    if(operador!=5 && (valor1==null || valor1=="")){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
        continuar = false;
    }else
    if(operador==5 && ((valor1.length==0 && valor2.length==0) || (valor1.length>0 && valor2.length==0) || (valor1.length==0 && valor2.length>0))){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
        continuar = false;
    }else
    if(operador==5 && (valor1.length>0 && valor2.length>0 && parseInt(valor2)<parseInt(valor1))){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgCriterioMaxMenorMinimo")%>");
        continuar = false;
    }

    if(continuar){      
        var datos = new Array();
        datos[0] = codigoCampo;        
        datos[1] = tipoCampo;
        /**/
        datos[2] = esCampoSuplementario;
        datos[3] = tipoCampoSuplementario;
        datos[4] = codigoDesplegable;
        /**/
        datos[5] = operador;
        datos[6] = valor1;
        if(valor2!=null && valor2!="" && operador==5)
           datos[7] = valor2;
    }

    return datos;
}


function tratarDatosTexto(){   
    var operador = document.getElementById("valorOperadorCriterio").value;
    var valor    = document.getElementById("valorBusqueda1").value;

    if(operador==""){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
        continuar = false;
    }else
    if(valor==null || valor==""){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
    }
    else{     
        var datos = new Array();
        datos[0] = codigoCampo;
        datos[1] = tipoCampo;        
        datos[2] = esCampoSuplementario;
        datos[3] = tipoCampoSuplementario;
        datos[4] = codigoDesplegable;        
        datos[5] = operador;        
        datos[6] = escape(valor);        
        return datos;
    }
}

function esFechaMenorIgualQue(fecha1,fecha2){
    var exito = false;
        
    if(fecha1==fecha2 || fecha1<fecha2)
        exito = true;

    if(fecha1!="" && fecha2!=""){
        var inicio = fecha1.split("/");
        var fin    = fecha2.split("/");

        var diaIni = inicio[0];
        var mesIni = inicio[1];
        var anhoIni = inicio[2];

        var diaFin  = fin[0];
        var mesFin  = fin[1];
        var anhoFin = fin[2];

        var dateInicio = new Date(anhoIni, mesIni, diaIni, 0,0,0,0);
        var dateFin   = new Date(anhoFin, mesFin, diaFin, 0,0,0,0);

        if(dateInicio== dateFin || dateInicio<dateFin)            
            exito = true;        
    }

    return exito;
}

function tratarDatosFecha(){ 
    var operador = document.getElementById("valorOperadorCriterio").value;
    var valor1   = document.getElementById("fechaInicio").value;
    var valor2   = document.getElementById("fechaFin").value;
    var continuar = true;

    if(operador==""){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
        continuar = false;
    }
    else
    if(operador!=5 && (valor1==null || valor1=="")){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
        continuar = false;
    }else
    if(operador==5 && ((valor1.length>0 && valor2.length==0) || (valor1.length==0 && valor2.length>0) || (valor1.length==0 && valor2.length==0))){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
        continuar = false;
    }else
    if(operador==5 && (valor1.length>0 && valor2.length>0 && !esFechaMenorIgualQue(valor1,valor2))){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgCriterioFechaMaxMenorMinimo")%>");
        continuar = false;
    }

    if(continuar){
        var datos = new Array();
        datos[0] = codigoCampo;
        datos[1] = tipoCampo;
        /**/
        datos[2] = esCampoSuplementario;
        datos[3] = tipoCampoSuplementario;
        datos[4] = codigoDesplegable;
        /**/
        datos[5] = operador;
        datos[6] = valor1;
        if(valor2!=null && valor2!="" && operador==5)
            datos[7] = valor2;

        return datos;
    }
}

function tratarDatosDesplegable(){
    var operador = document.getElementById("valorOperadorCriterio").value;
    var valor    = document.getElementById("valorBusqueda1").value;
    
    if(operador=="" || valor==""){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
    }
    else{
        var datos = new Array();
        datos[0] = codigoCampo;
        datos[1] = tipoCampo;
        /**/
        datos[2] = esCampoSuplementario;
        datos[3] = tipoCampoSuplementario;
        datos[4] = codigoDesplegable;
        /**/
        datos[5] = operador;
        datos[6] = valor;
        return datos;
    }
}

function tratarDatosInteresado(){    
    var nombre    = document.getElementById("nombre").value;
    var apellido1 = document.getElementById("apellido1").value;
    var apellido2 = document.getElementById("apellido2").value;
    var continuar = true;
    
    if(nombre.length==0 && apellido1.length==0 && apellido2.length==0){
        jsp_alerta("A","<%=descriptor.getDescripcion("msgValorBusquedaVacio")%>");
        continuar = false;
    }
    
    if(continuar){
        var datos = new Array();
        datos[0] = codigoCampo;
        datos[1] = tipoCampo;
        /**/
        datos[2] = esCampoSuplementario;
        datos[3] = tipoCampoSuplementario;
        datos[4] = codigoDesplegable;
        /**/
        datos[5] = nombre;
        datos[6] = apellido1;
        datos[7] = apellido2;
        return datos;
    }
}

function comprobarOperadorNumerico(){        
    var operador = document.getElementById("valorOperadorCriterio").value;
    if(operador!=5){
        document.getElementById("segundoCriterio").style.visibility = "hidden";
    }else{
        // Se muestra la segunda caja de texto para el número
        document.getElementById("segundoCriterio").style.visibility = "visible";
    }
}

function comprobarOperadorFecha(){    
    var operador = document.getElementById("valorOperadorCriterio").value;    
    if(operador!=5){
        document.getElementById("segundoCriterio").style.visibility = "hidden";
    }else{
        // Se muestra la segunda caja de texto para el segundo valor de la fecha
        document.getElementById("segundoCriterio").style.visibility = "visible";
    }
}

function mostrarCalFechaInicio(evento) { 
    if(window.event) evento = window.event;
    if (document.getElementById("calFechaInicio").className.indexOf("fa-calendar") != -1 )
      showCalendar('forms[0]','fechaInicio',null,null,null,'','calFechaInicio','',null,null,null,null,null,null,null,null,evento);
}

function mostrarCalFechaFin(evento) {
    if(window.event) evento = window.event;
    if (document.getElementById("calFechaFin").className.indexOf("fa-calendar") != -1 )
      showCalendar('forms[0]','fechaFin',null,null,null,'','calFechaFin','',null,null,null,null,null,null,null,null,evento);
}

function tratarDatosDocumento(){  
  var continuar = true;

  var tipo = document.getElementById("documento").value;  
  var documento = document.getElementById("txtDocumento").value;  
  var documento=documento.toUpperCase();

  if (documento == '' && tipo!="" && tipo!="0"){
      jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrDocumentoVacio")%>');
      continuar = false;
  }else
  if ((documento=="" && tipo=="") || (documento!="" && tipo=="")){
      jsp_alerta("A",'<%=descriptor.getDescripcion("msgErrDocumentoVacio")%>');
      continuar = false;
  }
  else{
      if(tipo=="4" || tipo=="5"){
          // Si se trata de un CIF
          var cifValido = validarCIF(documento);          
          if(!cifValido){
            jsp_alerta('A','<%=escape(descriptor.getDescripcion("msjDocIncorrecto"))%>');
              continuar = false;
          }          
      }else
      if(tipo=="1"){
        var nifValido= validarNif(documento);
        if(!nifValido){
            jsp_alerta('A','<%=escape(descriptor.getDescripcion("msjDocIncorrecto"))%>');
            continuar = false;
        }
      }else
      // Validamos el pasaporte.
      if (tipo=="2"){
          continuar = true;
      }else
      // Validamos la tarjeta de residencia
      if (tipo=="3") {
        var nieCorrecto = validarNie(documento);
        if (!nieCorrecto){
            jsp_alerta('A','<%=escape(descriptor.getDescripcion("msjDocIncorrecto"))%>');
            continuar = false;
        }
      }
  }//else

  if(continuar){    
    var datos = new Array();
    datos[0] = codigoCampo;
    datos[1] = tipoCampo;
    /**/
    datos[2] = esCampoSuplementario;
    datos[3] = tipoCampoSuplementario;
    datos[4] = codigoDesplegable;
    /**/
    datos[5] = tipo;
    datos[6] = documento;
    return datos;
  }
}


function validarCIF(cif)
{
    par = 0;
    non = 0;
    letras="ABCDEFGHJKLMNPQRSUVW";
    letrasInicio="KPQS";
    letrasFin="ABEH";
    letrasPosiblesFin="JABCDEFGHI";
    let=cif.charAt(0).toUpperCase();


    if (cif.length!=9)
    {
        return false;
    } else{
        caracterControl =cif.charAt(8).toUpperCase();
    }

    for (zz=2;zz<8;zz+=2)
    {
        par = par+parseInt(cif.charAt(zz));
    }

    for (zz=1;zz<9;zz+=2)
    {
        nn = 2*parseInt(cif.charAt(zz));
        if (nn > 9) nn = 1+(nn-10)
        non = non+nn
    }

    parcial = par + non;
    control = (10 - ( parcial % 10));
    if (control==10) control=0;


    /*
    * El valor del último carácter:
    * Será una LETRA si la clave de entidad es K, P, Q ó S.
    * Será un NUMERO si la entidad es A, B, E ó H.
    * Para otras claves de entidad: el dígito podrá ser tanto número como letra.
    * */

    if (letrasInicio.indexOf(let)!=-1){
        return (letrasPosiblesFin.charAt(control)==caracterControl);
    } else if (letrasFin.indexOf(let)!=-1){
        return (caracterControl==control);
    } else if (letras.indexOf(let)!=-1){
        return ((letrasPosiblesFin.charAt(control)==caracterControl)||(caracterControl==control));
    } else{
        return false;
    }
 }

function validarNif(dni) { 
    var LONGITUD = 9;
    var exito = false;
    
    if(dni!=null && dni.length==LONGITUD){
        numero = dni.substr(0,dni.length-1);
        let = dni.substr(dni.length-1,1);
        numero = numero % 23;
        letra='TRWAGMYFPDXBNJZSQVHLCKET';
        letra=letra.substring(numero,numero+1);
        
        if (letra.toUpperCase()==let.toUpperCase())
            exito = true;
    }//if
    
    return exito;
}


function validarNie(documento)
{
    var LONGITUD = 9;

    // Si se trata de un NIF
    // Primero comprobamos la longitud, si es distinta de la esperada, rechazamos.
    if (documento.length != LONGITUD) {       
        return false;
    }

    // Comprobas que el formato se corresponde con el de un NIE
    var primeraLetra = documento.substring(0,1).toUpperCase();
    var numero = documento.substring(1,8);
    var ultimaLetra = documento.substring(8,9).toUpperCase();
    if (!(isNaN(primeraLetra) && !isNaN(numero) && isNaN(ultimaLetra))) {        
        return false;
    }

    // Comprobamos que la primera letra es X, Y, o Z modificando el numero como corresponda.
    if (primeraLetra == "Y") numero = parseInt(numero,10) + 10000000;
    else if (primeraLetra == "Z") numero = parseInt(numero,10) + 20000000;
    else if (primeraLetra != "X") {                
        return false;
    }

    // Validamos el caracter de control.
    var letraCorrecta = getLetraNif(numero);
    if (ultimaLetra.toUpperCase() != letraCorrecta.toUpperCase()) {       
        return false;
    }
    return true;
}

function getLetraNif(dni) {
    var lockup = 'TRWAGMYFPDXBNJZSQVHLCKE';
    return lockup.charAt(dni % 23);
}

function comprobarOperadorDocumento(){
    var documento = document.getElementById("documento").value;    
    if(documento=="0"){
        document.getElementById("txtDocumento").value="";        
        document.getElementById("txtDocumento").style.visibility = "hidden";
    }else{                
        document.getElementById("txtDocumento").style.visibility = "visible";
    }    
}


//Usado para el calendario
var coordx=0;
var coordy=0;

if(navigator.appName.indexOf("Internet Explorer")==-1){
    window.addEventListener('mousemove', function(e) {
    coordx = e.clientX;
    coordy = e.clientY;
    }, true);
}

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    if('Alt+S'==tecla) pulsarCancelar();
   

    keyDel(evento);

    if (teclaAuxiliar == 1){        
        if(IsCalendarVisible) replegarCalendario(coordx,coordy);
    } // if

    if (teclaAuxiliar == 9){       
        if(IsCalendarVisible) replegarCalendario(coordx,coordy);
    }// if

    if(teclaAuxiliar && teclaAuxiliar.button == 9){
        if(IsCalendarVisible) replegarCalendario(coordx,coordy);
    }
}


function pulsarLimpiar(){    
    var datos = new Array();
    datos[0] = "LIMPIAR";
    self.parent.opener.retornoXanelaAuxiliar(datos);
}

</SCRIPT>
</head>

<BODY onload="javascript:{inicializar();}">
<html:hidden  property="opcion" value=""/>
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titCriterioBusqExpPendientes")%></div>
<div class="contenidoPantalla">
    <table width="100%">
        <tr>                                                       
            <td align="left" style="text-align:left;">

            <c:if test="${requestScope.tipo eq 'E'}">
                <%-- Si el campo es de tipo texto --%>
                <jsp:include page="criterioNumExpediente.jsp" flush="true">
                    <jsp:param name="nombre" value='<c:out value="${requestScope.nombre}"/>'/>
                </jsp:include>
            </c:if>


            <c:if test="${requestScope.tipo eq 'T'}">
                <%-- Si el campo es de tipo texto --%>                                                            
                <jsp:include page="criterioTexto.jsp" flush="true">
                    <jsp:param name="nombre" value='<c:out value="${requestScope.nombre}"/>'/>                                                               
                </jsp:include>
            </c:if>

            <c:if test="${requestScope.tipo eq 'N'}">
                <%-- Si el campo es de tipo texto --%>
                <jsp:include page="criterioNumerico.jsp" flush="true">                                                                
                    <jsp:param name="nombre" value='<c:out value="${requestScope.nombre}"/>'/>                                                                
                </jsp:include>
            </c:if>

            <c:if test="${requestScope.tipo eq 'D'}">
                <%-- Si el campo es de tipo texto --%>
                <jsp:include page="criterioDesplegable.jsp" flush="true">                                                                
                    <jsp:param name="nombre" value='<c:out value="${requestScope.nombre}"/>'/>
                </jsp:include>
            </c:if>

            <c:if test="${requestScope.tipo eq 'F'}">                                                            
                <%-- Si el campo es de tipo fecha --%>
                <jsp:include page="criterioFecha.jsp" flush="true">                                                                
                    <jsp:param name="nombre" value='<c:out value="${requestScope.nombre}"/>'/>                                                                
                </jsp:include>
            </c:if>

            <c:if test="${requestScope.tipo eq 'I'}">
                <%-- Si el campo es de tipo interesado --%>
                <jsp:include page="criterioInteresado.jsp" flush="true">                                                                
                    <jsp:param name="nombre" value='<c:out value="${requestScope.nombre}"/>'/>                                                                
                </jsp:include>
            </c:if>

            <c:if test="${requestScope.tipo eq 'DN'}">
                <%-- Si el campo es de tipo documento --%>
                <jsp:include page="criterioDocumento.jsp" flush="true">
                    <jsp:param name="nombre" value='<c:out value="${requestScope.nombre}"/>'/>
                </jsp:include>
            </c:if>

            <!-------------- Operador si lo hubiese ------------------------->
            </td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAnadir" onclick="pulsarAceptar();">
        <c:if test="${(requestScope.codCampoSeleccionado ne '') and (requestScope.codCampoSeleccionado eq requestScope.codigo)}">
                <input type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbLimpiar")%>" name="cmdLimpiar" onclick="pulsarLimpiar();">
        </c:if>
        <input type= "button"  class="botonGeneral" accesskey="C" value="<%=descriptor.getDescripcion("gbCancelar")%>" name="cmdCancelar"  onclick="pulsarCancelar();">
    </div>
</div>


<div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>

</BODY>
</html:html>