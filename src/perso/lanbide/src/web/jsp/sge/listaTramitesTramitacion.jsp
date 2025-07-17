<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.technical.ConstantesDatos" %>
<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="java.util.Vector" %>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: LISTA TRAMITES DEL FLUJO DE SALIDA PARA TRAMITACION:::</TITLE>
<jsp:include page="/jsp/plantillas/Metas.jsp" />

<!-- Estilos -->


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
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
<script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>

<SCRIPT type="text/javascript">

var datos = new Array();
var datosOriginal = new Array();
var listaModoSeleccion = new Array();
var listaCodTramite = new Array();
var listaUtrTramite = new Array();
var listaDescTramite = new Array();
var listaTramitesNoCumpleCodEntrada = new Array();
var cont = 0;
var tipoObligatorio = "";
var postNotificacionEnviada = false;
var mensaje = "";
var botonAceptarInactivo=1;
var contadorChecks=0;

function inicializar() {
    window.focus();


    window.onbeforeunload  = function() {
        return '¿Realmente desea cerrar la pantalla?';
   }
      
      
    /* 
     Se recuperan los argumenos enviados a la ventana:
        - el argumento 0 indica: 0->accion tramite;1->accion resolucion/pregunta-respuesta favorable;2->accion resolucion/pregunta-respuesta desfavorable
        - el argumento 1 indica: 0->tramites salida opcionales;1->tramites salida obligatorios;2->tramites salida excluyentes
    */
    var argVentana = self.parent.opener.xanelaAuxiliarArgs;

  tipoObligatorio = argVentana[1];
  postNotificacionEnviada = argVentana[2];

  if(argVentana[1] == 0)
    mensaje = '<%=descriptor.getDescripcion("msjCondTram1")%>';
  else if(argVentana[1] == 1)
    mensaje = '<%=descriptor.getDescripcion("msjCondTram")%>';
  else if(argVentana[1] == 2)
    mensaje = '<%=descriptor.getDescripcion("msjCondTram2")%>';
  domlay('capaMensaje',1,0,0,mensaje);

   // Tramites opcionales o excluyentes
    if(argVentana[1] == 0 || argVentana[1] == 2) {
      // #212448 Se añade onclick a input
      var checkEntreg1 = "";
      var checkEntreg2 = "";
      var checkEntreg3 = "";
      if(argVentana[1] == 0) {
        checkEntreg1 = "<input type='checkbox' class='check' name='box";
        checkEntreg2 = "' id='box";
        checkEntreg3 = "' value='SI' onclick='comprobarCheck(this);'>";
      } else {
        checkEntreg1 = " <input type='radio' id='salida";
        checkEntreg3 = "' name='elegir' value='si' onclick='comprobarCheck(this);'> ";
      }

      var entreg;
       if (argVentana[0] == 0 || argVentana[0] == 1) { // Tramites o respuesta/pregunta favorable
      <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaTramitesFavorables">
          if(argVentana[1] == 0) {
                  entreg = checkEntreg1+cont+checkEntreg2+cont+checkEntreg3;
          } else {
                  entreg = checkEntreg1+cont+checkEntreg3;
          }
          datos[cont] = [entreg,'<bean:write name="elemento" property="codigoVisibleTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="descripcionTramiteFlujoSalida"/>'];
          datosOriginal[cont] = ['<bean:write name="elemento" property="codigoTramiteFlujoSalida"/>',
                                '<bean:write name="elemento" property="codigoVisibleTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="descripcionTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="modoSeleccionUnidad"/>'];
          cont++;
      </logic:iterate>
        } else if(argVentana[0] == 2) { // Respuesta/pregunta desfavorable
            <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaTramitesNoFavorables">
                if(argVentana[1] == 0) {
                  entreg = checkEntreg1+cont+checkEntreg2+cont+checkEntreg3;
    } else {
                  entreg = checkEntreg1+cont+checkEntreg3;
                }
                datos[cont] = [entreg,'<bean:write name="elemento" property="codigoVisibleTramiteFlujoSalida"/>',
                      '<bean:write name="elemento" property="descripcionTramiteFlujoSalida"/>'];
        datosOriginal[cont] = ['<bean:write name="elemento" property="codigoTramiteFlujoSalida"/>',
                      '<bean:write name="elemento" property="codigoVisibleTramiteFlujoSalida"/>',
                      '<bean:write name="elemento" property="descripcionTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="modoSeleccionUnidad"/>'];
        cont++;
      </logic:iterate>
    }
    } else { // Tramites obligatorios
        if (argVentana[0] == 0 || argVentana[0] == 1) { // Tramites o respuesta/pregunta favorable
            <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaTramitesFavorables">
              datos[cont] = ['','<bean:write name="elemento" property="codigoVisibleTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="descripcionTramiteFlujoSalida"/>'];
          datosOriginal[cont] = ['<bean:write name="elemento" property="codigoTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="codigoVisibleTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="descripcionTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="modoSeleccionUnidad"/>'];
          cont++;
      </logic:iterate>
        } else if(argVentana[0] == 2) { // Respuesta/pregunta desfavorable
      <logic:iterate id="elemento" name="TramitacionExpedientesForm" property="listaTramitesNoFavorables">
        datos[cont] = ['','<bean:write name="elemento" property="codigoVisibleTramiteFlujoSalida"/>',
                      '<bean:write name="elemento" property="descripcionTramiteFlujoSalida"/>'];
        datosOriginal[cont] = ['<bean:write name="elemento" property="codigoTramiteFlujoSalida"/>',
                      '<bean:write name="elemento" property="codigoVisibleTramiteFlujoSalida"/>',
                      '<bean:write name="elemento" property="descripcionTramiteFlujoSalida"/>',
                        '<bean:write name="elemento" property="modoSeleccionUnidad"/>'];
        cont++;
      </logic:iterate>
    }
  }
  tab.lineas=datos;
  refresca();
  
  // Comprueba cuantos trámites tiene la condición de salida para habilitar el boton de carga directa si solo hay uno  
  if(datos.length==1)
      document.forms[0].cmdTramite.disabled = false;
      
  //Si viene de notificacion abre acuse automaticamente 
  if (postNotificacionEnviada) {
      pulsarAceptar('ficha');
  }
  
}

function pulsarCancelar() {
      confirmaSalida();
}


function confirmaSalida()  
{
    if(postNotificacionEnviada && tipoObligatorio==1){ // Es avance postNotificacion y de salida obligatoria
        pulsarAceptar('ficha');
    } else {
        self.parent.opener.retornoXanelaAuxiliar();
    } 
}

// Función que se ejecuta en el onclick del checkbox y el radio
function comprobarCheck(input){
    //comprueba si es checkbox o radio
    if(input.type=='checkbox'){
        //incrementa o decrementa el contador de checks marcados
        if(input.checked==true) contadorChecks++
        else contadorChecks--;
    } else contadorChecks=1;
    
    // Desmarcamos todos los radio button que esten marcados,
    //  ya que puede venir marcado de una pagina anterior de la lista de tramites
    if (input.type == 'radio') {
        comprobarRadio(input);
    }
    
    //en función del valor del contador habilita o deshabilita el botón de carga directa
    if(contadorChecks==1) document.forms[0].cmdTramite.disabled = false;
    else document.forms[0].cmdTramite.disabled = true;
}

function comprobarRadio(input) {
        var table = $('#'+tab.id).DataTable();
        var radiobuttons = $(":radio:checked", table.rows().nodes());
        for (var i = 0; i < radiobuttons.length; i++) {
            if (input.id != radiobuttons[i].id) {
                radiobuttons[i].checked = false;
            }
        }
}

function pulsarAceptar(aCargar) {
    document.forms[0].cmdAceptar.disabled = true; 
    if(botonAceptarInactivo==1)
    {
        botonAceptarInactivo=0;

        document.forms[0].cmdAceptar.disabled = true;     
        listaModoSeleccion = new Array();
        listaCodTramite = new Array();
        listaUtrTramite = new Array();
        listaDescTramite = new Array();
        listaTramitesNoCumpleCodEntrada = new Array();

        tramitesSeleccionados();

        if (listaCodTramite.length != 0) { 
            pedirUnidadesTramitadoras(aCargar);

        } else {
            jsp_alerta('A','<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            botonAceptarInactivo==1;
            document.forms[0].cmdAceptar.disabled = false; 
        }
    }

}


    /**
        Devuelve un objeto XMLHttpRequest
    */
    function getXMLHttpRequest(){
    var aVersions = [ "MSXML2.XMLHttp.5.0",
    "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
    "MSXML2.XMLHttp","Microsoft.XMLHttp"
    ];

    if (window.XMLHttpRequest){
        // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
        return new XMLHttpRequest();
    }
    else
        if (window.ActiveXObject){
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


function ltrim(s) {
	return s.replace(/^\s+/, "");
}

function rtrim(s) {
	return s.replace(/\s+$/, "");
 }

function trim(s) {
	return rtrim(ltrim(s));
}

function validarEntero(valor){
  valor = parseInt(valor);
  //Compruebo si es un valor numérico
  if (isNaN(valor)) {
    //entonces (no es numero) devuelvo el valor cadena vacia
    return false;
  }else{
    //En caso contrario (Si era un número) devuelvo el valor
    return true;
  }
}

function vacio(){}


/**
 * Comprueba si un tramite cumple sus condiciones de entrada
 * @return boolean
 */
function comprobarCondicionesEntrada(codTramite){ 
   var salida = false;
   var ajax = getXMLHttpRequest();

   var url = "<%= request.getContextPath() %>" + "/sge/TramitacionExpedientes.do";
   var parametros = "opcion=comprobarCondicionesEntrada&codTramiteComp=" + codTramite;
   ajax.open("POST",url,false); // Llamada síncrona, mientras el servidor no de una respuesta no se continua con la ejecución
   ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
   ajax.send(parametros);

   var res = ajax.responseText;   
   return trim(res);
}

// Abre un popup para elegir utr para los tramites con modo seleccion 'Cualquiera' u 'Otras'
 function pedirUnidadesTramitadoras(opcCargar) { 
    var codigosTramites ="";      
   // Se comprueba para que trámites de los siguientes al actual cumplen las condiciones de entrada
   for(var j=0; j<listaCodTramite.length; j++) {
        listaTramitesNoCumpleCodEntrada[j] = comprobarCondicionesEntrada(listaCodTramite[j]);        
   }


   // Se agrupan todos los códigos de trámites en una variable para pasar al action y que recupere
   // para cada tramite, sus unidades tramitadoras, si las tiene.
   for(var i=0; i<listaCodTramite.length; i++) {
        codigosTramites += listaCodTramite[i] + "-";
   }
   
   var salir = false;   
   var ajax = getXMLHttpRequest();
    
   var url = "<%= request.getContextPath() %>" + "/sge/TramitacionExpedientes.do";
   var parametros = "opcion=recuperaUnidadesOrganicasTramite&codigos=" + codigosTramites;        
   ajax.open("POST",url,false); // Llamada síncrona, mientras el servidor no de una respuesta no se continua con la ejecución
   ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
   ajax.send(parametros);
      
   // Se recuperan las unidades organicas de cada trámite si los tiene
   var res = ajax.responseText;   
   var primerResultado = new Array();

   percorrerTramites(0,res.split("-"),opcCargar);
 } 

function percorrerTramites(indRec,primerResultado,opcCargar) {
    if (indRec >= listaCodTramite.length){
        var retorno = new Array();
        retorno = ["si", crearLista(listaCodTramite), crearLista(listaModoSeleccion), crearLista(listaUtrTramite), crearLista(listaTramitesNoCumpleCodEntrada),opcCargar];
        self.parent.opener.retornoXanelaAuxiliar(retorno);
    }else{
        if ((listaModoSeleccion[indRec] == '<%=ConstantesDatos.TRA_UTR_OTRAS%>' ||
            listaModoSeleccion[indRec] == '<%=ConstantesDatos.TRA_UTR_CUALQUIERA%>' ||
            listaModoSeleccion[indRec] == '<%=ConstantesDatos.TRA_UTR_INICIA%>') && listaTramitesNoCumpleCodEntrada[indRec]=="si") {
                var elemento = trim(primerResultado[indRec]);            
                if(validarEntero(elemento)){
                     listaUtrTramite[indRec] = elemento;
                     percorrerTramites(++indRec,primerResultado,opcCargar)
                } else if(!validarEntero(elemento) && elemento=="NO"){
                    listaUtrTramite[indRec] = '_';
                     percorrerTramites(++indRec,primerResultado,opcCargar);
                } else if(!validarEntero(elemento) && elemento=="VARIOS"){
                    var requestParam = "&codigoTramite=" + listaCodTramite[indRec] + "&modoSeleccion=" + listaModoSeleccion[indRec];
                    // #289948: pasamos como argumentos de la ventana el origen de finalizar tramite y el tipo de obligatoriedad de los
                    // tramites de la salida, ademas del nombre del tramite a iniciar que ya se pasaba
                    var datosAEnviar = {
                        "nombreTramite" : listaDescTramite[indRec],
                        "postNotificacionEnviada" : postNotificacionEnviada,
                        "tipoAvance" : tipoObligatorio
                    };
                    abrirXanelaAuxiliar("<html:rewrite page='/jsp/sge/mainVentana.jsp?source='/>" +
                           "<html:rewrite page='/sge/TramitacionExpedientes.do?opcion=listaUnidadesPosibles'/>" + requestParam,
                            datosAEnviar,'width=700,height=530',function(result){
                                    if (result != undefined) {
                                        listaUtrTramite[indRec] = result;
                                        percorrerTramites(++indRec,primerResultado,opcCargar)
                                    }
                                });
                }// if
        }else{            
            listaUtrTramite[indRec] = '_';
            percorrerTramites(++indRec,primerResultado,opcCargar)
        }
    }
}

// Crea listas de codigo, descripcion y modo de seleccion de los tramites que
// ha seleccionado el usuario
function tramitesSeleccionados() {
    var table = $('#'+tab.id).DataTable();
  var j=0;
  if(tipoObligatorio == 0) {
    var checkboxes = $(":checkbox:checked", table.rows().nodes());
    for (var i=0; i<checkboxes.length; i++){
        var filaCheck = checkboxes[i].id.substr(3);
        listaCodTramite[j] = datosOriginal[filaCheck][0];
        listaModoSeleccion[j] = datosOriginal[filaCheck][3];
        listaDescTramite[j] = datosOriginal[filaCheck][2];
        j++;
      }
  } else if(tipoObligatorio == 2) {    
        var radiobuttons = $(":radio:checked", table.rows().nodes());
        if (radiobuttons.length > 0) {
            var filaRadio = radiobuttons[0].id.substr(6);
            listaCodTramite[j] = datosOriginal[filaRadio][0];
            listaModoSeleccion[j] = datosOriginal[filaRadio][3];
            listaDescTramite[j] = datosOriginal[filaRadio][2];
          j++;
        }
  } else {
    for (i=0; i < datos.length; i++) {
     listaCodTramite[j] = datosOriginal[i][0];
     listaModoSeleccion[j] = datosOriginal[i][3];
     listaDescTramite[j] = datosOriginal[i][2];
     j++;
    }
  }
    }
    
function crearLista(lista) {
    var listaTxt = '';
    for (var i=0; i<lista.length; i++) {
        listaTxt += lista[i] + '§¥';
  }
    return listaTxt;
}


</SCRIPT>
</head>

<BODY  onload="javascript:{inicializar();}">

<html:form action="/sge/TramitacionExpedientes.do" target="_self">

    <html:hidden  property="opcion" value=""/>
    <input type="hidden" name="tipoAccion"   value="">

    <div id="titulo" class="txttitblanco">&nbsp;<%=descriptor.getDescripcion("tit_listTramSal")%></div>
    <div class="contenidoPantalla" valign="top">
        <TABLE cellspacing="10px" cellpadding="0px" border="0px" style="margin-bottom:10px">

            <TR>
                <TD align="center" valign="middle" class="etiqueta"><div id="capaMensaje" class="etiqueta"></div></TD>
            </TR>
            <TR>
                <TD id="tabla" align="left"></TD>
            </TR>
        </TABLE>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("etiqBtnIniciarCargar")%>" name="cmdTramite" onClick="window.onbeforeunload = null; pulsarAceptar('tramite');" disabled >
            <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("etiqBtnIniciarLista")%>" name="cmdAceptar" onClick="window.onbeforeunload = null; pulsarAceptar('ficha');">
            <input type="button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%> name="cmdCancelar" onClick="window.onbeforeunload = null; pulsarCancelar();">
        </div>
    </div>
</html:form>

<script type="text/javascript">

// TABLA PRIMERA
tab = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

tab.addColumna('20','center','');
tab.addColumna('70','center','<%= descriptor.getDescripcion("gEtiq_codigo")%>');
tab.addColumna('485','center','<%= descriptor.getDescripcion("gEtiq_nombre")%>');
tab.displayCabecera=true;
tab.displayTabla();

function refresca() {
  tab.displayTabla();
}

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else
        teclaAuxiliar = evento.which;

    if (teclaAuxiliar == 38 || teclaAuxiliar == 40){
		upDownTable(tab,datos,teclaAuxiliar);
	}
	keyDel(evento);
}

</script>



<script type="text/javascript" src="<c:url value='/scripts/listaCombo.js'/>"></script>

</BODY>

</html:html>
