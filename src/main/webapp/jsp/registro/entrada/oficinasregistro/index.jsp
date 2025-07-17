<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<html>
<head>
<title>Búsqueda</title>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<%
    int idioma = 1;
    String opcion = "";
    String css = "";
    if (session != null) {
        UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            css = usuario.getCss();
        }
    }

    String idSesion = session.getId();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
%>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp"/>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="1" />

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%><%=css%>" type="text/css"> 
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript">
    var comboOficinaOrigen;
    var comboOficinaDestino;
    var comboEstado;
    var listaCodigosOficina = new Array();
    var listaCodigosVisiblesOficina = new Array();
    var listaDescripcionesOficina = new Array();
    var  codigosEstado         = new Array();
    var descripcionesEstado = new Array();
    var columnasListados     = new Array();
    var filasListado              = new Array();
    var classRows               = new Array();
    var tabAnotaciones        = new Array();
    var anotacionesActuales = new Array();
    var numRelacionAnotaciones = 0;
    var lineasPagina                   = 10;
    var paginaActual                  =1;
    var anotacionesCheck=new Array();
    var valoresAnotacionesCheck=new Array();
    var valoresAnotacionesEstado=new Array();

    var codigoError              = '<bean:write name="AnotacionesOficinasRegistroForm" property="codigoError" scope="request" ignore="true"/>';
    var codigoOficinaUsuario = '<bean:write name="AnotacionesOficinasRegistroForm" property="codigoOficinaUsuario" scope="request" ignore="true"/>';

    function cargarCombos(){
        var contador = 1;
        listaCodigosVisiblesOficina[0] = "-1";
        listaDescripcionesOficina[0]    = "TODAS";
        listaCodigosOficina[0]            = "-1";

        <logic:iterate id="oficina" name="AnotacionesOficinasRegistroForm" property="oficinasOrigen" scope="request">
            listaCodigosOficina[contador]                 = ['<bean:write name="oficina" property="codOficina" />'];
            listaCodigosVisiblesOficina[contador]   = ['<bean:write name="oficina" property="codVisibleOficina" />'];
            listaDescripcionesOficina[contador]         = ['<bean:write name="oficina" property="nombreOficina" />'];
            contador++;
        </logic:iterate>

        /** Códigos de los estados */
        codigosEstado[0] = -1;
        codigosEstado[1] = 0;
        codigosEstado[2] = 1;
        codigosEstado[3] = 2;
        codigosEstado[4] = 3;

        /** Descripciones de los estados **/
        descripcionesEstado[0]   = "<%=descriptor.getDescripcion("etiqEstTodos")%>";
        descripcionesEstado[1]   = "<%=descriptor.getDescripcion("etiqEstRegistrada")%>";
        descripcionesEstado[2]   = "<%=descriptor.getDescripcion("etiqEstAsociadaExpediente")%>";
        descripcionesEstado[3]   = "<%=descriptor.getDescripcion("etiqEstIncorrecta")%>";
        descripcionesEstado[4]   = "<%=descriptor.getDescripcion("etiqEstAceptadaDestino")%>";

        comboOficinaOrigen = new Combo("OficinaOrigen");
        comboOficinaOrigen.addItems(listaCodigosVisiblesOficina, listaDescripcionesOficina);
        comboOficinaOrigen.change = cambioOficinaOrigen;

       comboOficinaDestino = new Combo("OficinaDestino");
       comboOficinaDestino.addItems(listaCodigosVisiblesOficina, listaDescripcionesOficina);

       comboEstado = new Combo("Estado");
       comboEstado.addItems(codigosEstado,descripcionesEstado);
       comboEstado.selectItem(0);

       deshabilitarImagenCal("calDesde",false);
       deshabilitarImagenCal("calHasta",false);

       cargarListado();
       tratarMensajeError();
    }

    function cargarListado(){
        tabAnotaciones = new Tabla(false,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>',
                '<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>',
                '<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', 
                '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>',
                '<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',
                document.getElementById('tabAnotaciones'));

        tabAnotaciones.addColumna('18','center','');
        tabAnotaciones.addColumna('65','center','<%=descriptor.getDescripcion("etiqOfiFechaEntrada")%>');
        tabAnotaciones.addColumna('90','center','<%=descriptor.getDescripcion("etiqOfiNumEntrada")%>');
        tabAnotaciones.addColumna('140','center','<%=descriptor.getDescripcion("etiqOfiExtracto")%>');
        tabAnotaciones.addColumna('165','center','<%=descriptor.getDescripcion("etiqOfiInteresado")%>');
        tabAnotaciones.addColumna('105','center','<%=descriptor.getDescripcion("etiqOficinaOrigen")%>');            
        tabAnotaciones.addColumna('105','center','<%=descriptor.getDescripcion("etiqOficinaDestino")%>');
        tabAnotaciones.addColumna('105','center','<%=descriptor.getDescripcion("gEtiqUnidDestino")%>');
        tabAnotaciones.addColumna('85','center', '<%=descriptor.getDescripcion("etiqOfiEstado")%>');
        tabAnotaciones.lineas = filasListado;
        tabAnotaciones.displayCabecera=true;
        tabAnotaciones.displayTabla();
        cargarComboFilasPagina();
    }


/** Se encarga de mostrar el mensaje de error correspondiente si es necesario */
function tratarMensajeError(){
    var error = false;
    var etiquetaError = "";


     if(codigoError=="PERMISO_MAS_UNA_OFICINA_REGISTRO"){
         error = true;
         etiquetaError = "<%=descriptor.getDescripcion("msgPermisoMasOfiRegistro")%>";
     }else
     if(codigoError=="NO_PERMISO_NINGUNA_OFICINA_REGISTRO"){
         error = true;
         etiquetaError = "<%=descriptor.getDescripcion("msgNoPermisoOfiRegistro")%>";
     }else
     if(codigoError=="ERROR_TECNICO_VERIFICACION_PERMISOS"){
         error = true;
         etiquetaError = "<%=descriptor.getDescripcion("msgErrorErrorVerifOfiRegistro")%>";
     }

     if(error){
        jsp_alerta("A",etiquetaError);
        // Se desactivan los campos pertinentes del formulario
        comboOficinaOrigen.deactivate();
        comboOficinaDestino.deactivate();
        comboEstado.deactivate();
        document.forms[0].fechaDesde.disabled = true;
        document.forms[0].fechaHasta.disabled = true;
        deshabilitarImagenCal("calDesde",true);
        deshabilitarImagenCal("calHasta",true);
     }
     return error;
}

function cambioOficinaOrigen(){
    var codOficinaOrigen = document.forms[0].codOficinaOrigen.value;

    comboOficinaDestino.activate();
    var codInternoUor = getCodigoInternoOficina(codOficinaOrigen);
    /** Si el código interno de la uor de origen coincide con la uor/oficina de registro del usuario => Se recuperan las anotaciones
         que haya creado esa unidad y que vayan dirigidas hacia el destino indicado en el combo destino */

    if(codInternoUor!=codigoOficinaUsuario && codOficinaOrigen!="-1"){
        /** Se escoge como oficina de origen una distinta al usuario, entonces se recuperan las anotaciones que tienen como destino
           * esta uor/oficina de registro y se desactiva el combo oficina de destino*/
        comboOficinaDestino.deactivate();
        comboOficinaDestino.selectItem(-1);
    }
}

function cargarPagina(pagina){ 
    var fechaDesde                   = document.forms[0].fechaDesde.value;
    var fechaHasta                    = document.forms[0].fechaHasta.value;
    var codVisibleOficinaOrigen  = document.forms[0].codOficinaOrigen.value;
    var codVisibleOficinaDestino = document.forms[0].codOficinaDestino.value;

    // Se comprueba si hay algún mensaje de error que mostrar que impida el envío del formulario.
    var hayMensaje = tratarMensajeError();
    if(!hayMensaje){
        var lenFechaDesde = fechaDesde.length;
        var lenFechaHasta  = fechaHasta.length;
        var lenCodVisibleOficinaOrigen  = codVisibleOficinaOrigen.length;
        var lenCodVisibleOficinaDestino = codVisibleOficinaDestino.length;

        var codInternoUorOrigen = getCodigoInternoOficina(codVisibleOficinaOrigen);

        if(lenFechaDesde==0 && lenFechaHasta==0 && lenCodVisibleOficinaOrigen==0 && lenCodVisibleOficinaDestino==0){
            jsp_alerta("A","<%=descriptor.getDescripcion("msjAbrirCond10")%>");
        }else
        if((lenFechaDesde>0 && lenFechaHasta>0 && lenCodVisibleOficinaOrigen>0 && lenCodVisibleOficinaDestino>0)
            || (lenFechaDesde>0 && lenFechaHasta>0 && lenCodVisibleOficinaOrigen>0 && lenCodVisibleOficinaDestino==0 && codInternoUorOrigen!=codigoOficinaUsuario))
        {
            document.forms[0].codigoOficinaRegistroOrigen.value = getCodigoInternoOficina(codVisibleOficinaOrigen);
            document.forms[0].codigoOficinaRegistroDestino.value = getCodigoInternoOficina(codVisibleOficinaDestino);
            document.forms[0].lineasPaginas.value = lineasPagina;
            document.forms[0].pagina.value = pagina;
            document.forms[0].target = "oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/EnvioAnotacionOficinasRegistroLanbide.do?opcion=busqueda";

            document.forms[0].submit();
        }else{
            jsp_alerta("A","<%=descriptor.getDescripcion("msjAbrirCond10")%>");
        }
    }
}// cargarPagina

function pulsarBuscar(){ 
   var fechaDesde                   = document.forms[0].fechaDesde.value;
   var fechaHasta                    = document.forms[0].fechaHasta.value;
   var codVisibleOficinaOrigen  = document.forms[0].codOficinaOrigen.value;
   var codVisibleOficinaDestino = document.forms[0].codOficinaDestino.value;
   var descOficinaDestino         = document.forms[0].descOficinaDestino.value;
   var estado                           = document.forms[0].codEstado.value;

   anotacionesCheck=new Array();
   valoresAnotacionesCheck=new Array();
   valoresAnotacionesEstado=new Array();

   // Se comprueba si hay algún mensaje de error que mostrar que impida el envío del formulario.
   var hayMensaje = tratarMensajeError();
   if(!hayMensaje){
       var lenFechaDesde = fechaDesde.length;
       var lenFechaHasta  = fechaHasta.length;
       var lenCodVisibleOficinaOrigen  = codVisibleOficinaOrigen.length;
       var lenCodVisibleOficinaDestino = codVisibleOficinaDestino.length;
       var lenEstado = estado.length;

       var codInternoUorOrigen = getCodigoInternoOficina(codVisibleOficinaOrigen);            
       if(lenFechaDesde==0 && lenFechaHasta==0 && lenCodVisibleOficinaOrigen==0 && lenCodVisibleOficinaDestino==0 && lenEstado==0){
           jsp_alerta("A","<%=descriptor.getDescripcion("msjAbrirCond10")%>");
       }else
       if((lenFechaDesde>0 && lenFechaHasta>0 && lenCodVisibleOficinaOrigen>0 && lenCodVisibleOficinaDestino>0 && lenEstado>0)
           || (lenFechaDesde>0 && lenFechaHasta>0 && lenCodVisibleOficinaOrigen>0 && lenCodVisibleOficinaDestino==0 && codInternoUorOrigen!=codigoOficinaUsuario && lenEstado>0 && codInternoUorOrigen!="-1")
           )
       {

           // Se vacía el contenido de la tabla
           var aux = new Array();
           tabAnotaciones.lineas = aux;
           tabAnotaciones.displayCabecera=true;
           tabAnotaciones.displayTabla();
           document.forms[0].codigoOficinaRegistroOrigen.value  = getCodigoInternoOficina(codVisibleOficinaOrigen);
           document.forms[0].codigoOficinaRegistroDestino.value = getCodigoInternoOficina(codVisibleOficinaDestino);
           document.forms[0].nombreOficinaRegistroDestino.value = descOficinaDestino;
           document.forms[0].estado.value = estado;
           document.forms[0].lineasPaginas.value = lineasPagina;
           document.forms[0].pagina.value = "";
           document.forms[0].target = "oculto";
           document.forms[0].action = "<%=request.getContextPath()%>/EnvioAnotacionOficinasRegistroLanbide.do?opcion=busqueda";
           pleaseWait('on');           
           document.forms[0].submit();
       }else{
           jsp_alerta("A","<%=descriptor.getDescripcion("msjAbrirCond10")%>");
       }
   }
}// pulsarBuscar

function actualizarListadoAnotaciones(anotaciones){ 
   pleaseWait('off');
   anotacionesActuales = anotaciones;
   tabAnotaciones.lineas = anotaciones;
   tabAnotaciones.displayTabla();
   domlay('enlace',1,0,0,enlaces());
   mostrarBotonera();
   actualizarChecks();
}

function mostrarBotonera(){
   var codOficinaOrigen = document.forms[0].codOficinaOrigen.value;
   var codOficinaDestino= document.forms[0].codOficinaDestino.value;        
   var codInternoUorOrigen = getCodigoInternoOficina(codOficinaOrigen);
   var codInternoUorDestino= getCodigoInternoOficina(codOficinaDestino);

   document.getElementById("envio").style.display = 'none';
   document.getElementById("confirmacion").style.display = 'none';

   if(codInternoUorOrigen==codigoOficinaUsuario && codInternoUorOrigen!="" && codigoOficinaUsuario!=""){          
       // Oficina de origen es la del usuario. Este solo puede cambiar el estado de anotaciones de "Incorrectas" a "Enviadas"
       document.getElementById("envio").style.display = '';
       document.getElementById("confirmacion").style.display = 'none';
   }
   else
   if(codOficinaOrigen!="-1" && codInternoUorOrigen!=codigoOficinaUsuario && codInternoUorOrigen!="" && codigoOficinaUsuario!=""){            
       // Oficina de origen distnta a ldel usuario, se recuperan las anotaciones que se le hayan enviado a la ofi del usuario => Puede cambiar a estado "Aceptada", "Incorrecta"
       document.getElementById("envio").style.display = 'none';
       document.getElementById("confirmacion").style.display = '';
   }
   else
   if(codOficinaOrigen=="-1" && codInternoUorDestino==codigoOficinaUsuario && codInternoUorDestino!="" && codigoOficinaUsuario!=""){            
       // Oficina de origen =TODAS y oficina de destino la del usuario. Es el mismo caso que el else anterior
       document.getElementById("envio").style.display = 'none';
       document.getElementById("confirmacion").style.display = '';
   }
}

function enlaces() {
   numeroPaginas = Math.ceil(numRelacionAnotaciones/lineasPagina);
   return enlacesPaginacion('<%=descriptor.getDescripcion("mosPagDePags")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>',paginaActual,numeroPaginas,'cargarPagina');
}


   /** Recupera el código interna de la uor de origen a partir del codigo visible de la misma */
   function getCodigoInternoOficina(codOficinaOrigen){            
       for(i=0;i<listaCodigosVisiblesOficina.length;i++){
           if(listaCodigosVisiblesOficina[i]==codOficinaOrigen){
               break;
           }
       }
       return listaCodigosOficina[i];
   }


   function callFromTableTo(rowID,tableName){ 
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

       if('Alt+S'==tecla) pulsarSalir();

       if (teclaAuxiliar == 38 || teclaAuxiliar == 40) upDownTable(tabAnotaciones,anotaciones,teclaAuxiliar);
       if(teclaAuxiliar == 13){
           if(tab.selectedIndex>-1 && !tab.ultimoTable) callFromTableTo(tabAnotaciones.selectedIndex,tabAnotaciones.id);
       }
       keyDel(evento);

       if (teclaAuxiliar == 1){
           if (comboOficinaOrigen.base.style.visibility == "visible") setTimeout('comboOficinaOrigen.ocultar()',20);
           if (comboOficinaDestino.base.style.visibility == "visible") setTimeout('comboOficinaDestino.ocultar()',20);
           if (comboEstado.base.style.visibility == "visible") setTimeout('comboEstado.ocultar()',20);
           if(IsCalendarVisible) replegarCalendario(coordx,coordy);
       } // if

       if (teclaAuxiliar == 9){
           comboOficinaOrigen.ocultar();
           comboOficinaDestino.ocultar();
           comboEstado.ocultar();
           if(IsCalendarVisible) replegarCalendario(coordx,coordy);
       }// if

       if(teclaAuxiliar && teclaAuxiliar.button == 9){
           comboOficinaOrigen.ocultar();
           comboOficinaDestino.ocultar();
           comboEstado.ocultar();
           if(IsCalendarVisible) replegarCalendario(coordx,coordy);
       }
   }

           // Calendario.
   function mostrarCalDesde(evento) {
       if(window.event) evento = window.event;
       if (document.getElementById("calDesde").className.indexOf("fa-calendar") != -1 ){
           showCalendar('forms[0]','fechaDesde',null,null,null,'','calDesde','',null,null,null,null,null,null,null,null,evento);
       }
   }

   function mostrarCalHasta(evento) {
       if(window.event) evento = window.event;
       if (document.getElementById("calHasta").className.indexOf("fa-calendar") != -1) {
           showCalendar('forms[0]', 'fechaHasta', null, null, null, '', 'calHasta', '',null,null,null,null,null,null,null,null,evento);
       }
   }

function comprobarFecha(inputFecha) {
    var formato = 'dd/mm/yyyy';
    if (Trim(inputFecha.value)!='') {
    consultando = true;
        if (consultando) {
            var validas = true;
            var fechaFormateada=inputFecha.value;             
            var pos=0;
            var fechas = Trim(inputFecha.value);
            var fechas_array = fechas.split(/[:|&<>!=]/);


            for (var loop=0; loop < fechas_array.length; loop++)
                {
                    f = fechas_array[loop];
                    formato = formatoFecha(Trim(f));                     
                    var D = ValidarFechaConFormato(f,formato);                     
                    if (!D[0]) validas=false;
                    else {
                        if (fechaFormateada.indexOf(f,pos) != -1) {
                            var toTheLeft = fechaFormateada.substring(0, fechaFormateada.indexOf(f));
                            var toTheRight = fechaFormateada.substring(fechaFormateada.indexOf(f)+f.length, fechaFormateada.length);
                            pos=fechaFormateada.indexOf(f,pos);
                            fechaFormateada = toTheLeft + D[1]+ toTheRight;
                        }
                    }
                }

                if (!validas) {
                    jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                        tp1.setSelectedIndex(0);
                        inputFecha.focus();
                        return false;
                    } else {
                    inputFecha.value = fechaFormateada;
                    return true;
                }

            } else { // No consultando. Unico formato posible: dd/mm/yy o dd/mm/yyyy
               inputFecha.value = inputFecha.value.substring(0,10);
               var D = ValidarFechaConFormato(inputFecha.value,formato);
            if (!D[0]){
                jsp_alerta("A",'<%=descriptor.getDescripcion("fechaNoVal")%>');
                    tp1.setSelectedIndex(0);
                    inputFecha.focus();
                    return false;
                } else {
                inputFecha.value = D[1];
                return true;
            }
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

function pulsarRecepcion(){
    var salida        = "";
    var separador = '§¥';
    var contador   = 0;
    var contadorRecibidas = 0;    

    for(i=0;i<valoresAnotacionesCheck.length;i++){
        if(valoresAnotacionesCheck[i]!=0 && valoresAnotacionesCheck[i]!=null){
            salida = salida + valoresAnotacionesCheck[i];
            contador++;
            
            // Se comprueba que el estado de las anotaciones no sea actualmente Recibida
            if (valoresAnotacionesEstado[i]!="REGISTRADA")
                contadorRecibidas++;
            
            if(valoresAnotacionesCheck.length-i>1)
                salida = salida + separador
        }
    }// for

    if(contador>0 && contadorRecibidas==0){
        if(jsp_alerta("C","<%=descriptor.getDescripcion("msgMarcarRecibidas")%>")==1){
            document.forms[0].listaAnotaciones.value = salida;
            document.forms[0].target = "oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/EnvioAnotacionOficinasRegistroLanbide.do?opcion=aceptar";
            document.forms[0].pagina.value = paginaActual;
            document.forms[0].submit();
        }
    }else
    if(contador==0){
        // El usuario no ha seleccionado ninguna anotacion
        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorAnotacionSel")%>");
    }else
    if(contador>0 && contadorRecibidas>0){
        // Alguna de las anotaciones seleccionadas por el usuario ya estan en estado RECIBIDA
        jsp_alerta("A","<%=descriptor.getDescripcion("errorAnotRecibida")%>");
    }
}

function pulsarRechazar(){ 
    var salida        = "";
    var separador = '§¥';
    var contador   = 0;
    var contadorIncorrectas = 0;

    for(i=0;i<valoresAnotacionesCheck.length;i++){
        if(valoresAnotacionesCheck[i]!=0){
            salida = salida + valoresAnotacionesCheck[i];
            contador++;
            
            // Se comprueba que el estado de las anotaciones no sea actualmente Recibida
            if (valoresAnotacionesEstado[i]=="INCORRECTA")
                contadorRecibidas++;
            
            if(valoresAnotacionesCheck.length-i>1)
                salida = salida + separador
        }
    }// for

    if(contador>0 && contadorIncorrectas==0){

        if(jsp_alerta("C","<%=descriptor.getDescripcion("msgMarcarIncorrectas")%>")==1){
            document.forms[0].listaAnotaciones.value = salida;
            document.forms[0].target = "oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/EnvioAnotacionOficinasRegistroLanbide.do?opcion=rechazar";
            document.forms[0].pagina.value = paginaActual;
            document.forms[0].submit();
       }
    }else
    if(contador==0){
        // El usuario no ha seleccionado ninguna anotacion
        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorAnotacionSel")%>");
    }else
    if(contador>0 && contadorIncorrectas>0){
        // Alguna de las anotaciones seleccionadas por el usuario ya estan en estado INCORRECTA
        jsp_alerta("A","<%=descriptor.getDescripcion("errorAnotIncorrecta")%>");
    }
}


function pulsarEnviar(){ 
    var salida        = "";
    var separador = '§¥';
    var contador   = 0;
    var contadorIncorrectas = 0;

    for(i=0;i<valoresAnotacionesCheck.length;i++){
        if(valoresAnotacionesCheck[i]!=0){
            salida = salida + valoresAnotacionesCheck[i];
            contador++;
            
            // Se comprueba que el estado de las anotaciones no sea actualmente Recibida
            if (valoresAnotacionesEstado[i]=="INCORRECTA")
                contadorRecibidas++;
            
            if(valoresAnotacionesCheck.length-i>1)
                salida = salida + separador
        }
    }// for

    if(contador>0 && contador==contadorIncorrectas){
        if(jsp_alerta("C","<%=descriptor.getDescripcion("msgMarcarEnviadas")%>")==1){ 
            document.forms[0].listaAnotaciones.value = salida;
            document.forms[0].target = "oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/EnvioAnotacionOficinasRegistroLanbide.do?opcion=enviar";
            document.forms[0].pagina.value = paginaActual;
            document.forms[0].submit();
        }
    }else
    if(contador==0){
        // El usuario no ha seleccionado ninguna anotacion
        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorAnotacionSel")%>");
    }else
    if(contador!=contadorIncorrectas){
        // Alguna de las anotaciones seleccionadas por el usuario no se encuentra en estado INCORRECTA
        jsp_alerta("A","<%=descriptor.getDescripcion("errorAnotNoIncorrecta")%>");
    }    
    
}


function pulsarCheck(){
    var cheks = document.getElementsByName("checkAnotacion");

    for(i=0;i<cheks.length;i++){
        valoresAnotacionesEstado[((paginaActual-1)*lineasPagina)+i] = anotacionesActuales[i][8];
        if(cheks[i].checked){
           anotacionesCheck[((paginaActual-1)*lineasPagina)+i]=1;
           valoresAnotacionesCheck[((paginaActual-1)*lineasPagina)+i]=cheks[i].value;
        }//if
        else  {
            anotacionesCheck[((paginaActual-1)*lineasPagina)+i]=0;
            valoresAnotacionesCheck[((paginaActual-1)*lineasPagina)+i]=0;
        }
    }// for
    
}

function actualizarChecks()
{
   var cheks = document.getElementsByName("checkAnotacion");
   for(i=0;i<cheks.length;i++){
        if(anotacionesCheck[((paginaActual-1)*lineasPagina)+i]==1){            
            cheks[i].checked=1;       
        }else
            cheks[i].checked=0;
    }
}

function mostrarErrorOperacion(tipo){
    if(tipo=="A") // Error al aceptar una/s anotación/es
        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorAceptarAnotacion")%>");
    else
    if(tipo=="R") // Error al rechazar una/s anotación/es
        jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorRechazarAnotacion")%>");
}

function pulsarPrevioImprimir(){
    var salida        = "";
    var separador = '§¥';
    var contador   = 0;
   
    for(i=0;i<valoresAnotacionesCheck.length;i++){
        if(valoresAnotacionesCheck[i]!=0){
            
            salida = salida + valoresAnotacionesCheck[i];
            contador++;
           
            if(valoresAnotacionesCheck.length-i>1)
                salida = salida + separador
        }
        
    }// for


    if(contador>0 ){
        pulsarImprimir(salida);
    }else
    if(contador==0){
        // El usuario no ha seleccionado ninguna anotacion
        if(jsp_alerta("C","<%=descriptor.getDescripcion("msgErrorAnotacionSel")%>"+ " <%=descriptor.getDescripcion("msgImprimirTodoListado")%>"))
        {
            pulsarImprimir("");
        }
    }
}

function pulsarImprimir(salida){
        var fechaDesde                   = document.forms[0].fechaDesde.value;
        var fechaHasta                    = document.forms[0].fechaHasta.value;
        var codVisibleOficinaOrigen  = document.forms[0].codOficinaOrigen.value;
        var codVisibleOficinaDestino = document.forms[0].codOficinaDestino.value;
        var estado                           = document.forms[0].codEstado.value;

        document.forms[0].listaAnotaciones.value = salida;

        // Se comprueba si hay algún mensaje de error que mostrar que impida el envío del formulario.
        var hayMensaje = tratarMensajeError();
        if(!hayMensaje){
            var lenFechaDesde = fechaDesde.length;
            var lenFechaHasta  = fechaHasta.length;
            var lenCodVisibleOficinaOrigen  = codVisibleOficinaOrigen.length;
            var lenCodVisibleOficinaDestino = codVisibleOficinaDestino.length;
            var lenEstado = estado.length;

            var codInternoUorOrigen = getCodigoInternoOficina(codVisibleOficinaOrigen);

            if(lenFechaDesde==0 && lenFechaHasta==0 && lenCodVisibleOficinaOrigen==0 && lenCodVisibleOficinaDestino==0 && lenEstado==0){
                jsp_alerta("A","<%=descriptor.getDescripcion("msjAbrirCond10")%>");
            }else
            if((lenFechaDesde>0 && lenFechaHasta>0 && lenCodVisibleOficinaOrigen>0 && lenCodVisibleOficinaDestino>0 && lenEstado>0)
                || (lenFechaDesde>0 && lenFechaHasta>0 && lenCodVisibleOficinaOrigen>0 && lenCodVisibleOficinaDestino==0 && codInternoUorOrigen!=codigoOficinaUsuario && lenEstado>0))
            {
              
                document.forms[0].codigoOficinaRegistroOrigen.value  = getCodigoInternoOficina(codVisibleOficinaOrigen);
                document.forms[0].codigoOficinaRegistroDestino.value = getCodigoInternoOficina(codVisibleOficinaDestino);
                document.forms[0].estado.value = estado;
                document.forms[0].pagina.value = "";
                document.forms[0].target = "oculto";
                document.forms[0].action = "<%=request.getContextPath()%>/EnvioAnotacionOficinasRegistroLanbide.do?opcion=imprimir&nombre=0&escudo=0&pdfFile=registro";
                pleaseWait('on');
                document.forms[0].submit();
            }else{
                jsp_alerta("A","<%=descriptor.getDescripcion("msjAbrirCond10")%>");
            }
       }// if
}

function abrirInforme(nombre) {
    // A otra página que contiene el fichero PDF.
    var informesDireccion = false;

    if (nombre == "NO EXISTE") {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoDatos")%>');
    } else if (nombre != '') {
        if (informesDireccion) { 
            ventanaPadre.verInforme(nombre);
        } else {        
            var sourc = "<%=request.getContextPath()%>/jsp/verPdf.jsp?opcion=null&nombre=" + nombre;
            ventanaInforme = window.open("<%=request.getContextPath()%>/jsp/mainVentana.jsp;jsessionid=<%=idSesion%>?source=" + sourc, 'ventanaInforme', 'width=1000px,height=750px,status=' + '<%=statusBar%>' + ',toolbar=no');
            ventanaInforme.focus();
        }
    } else {
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoPDF")%>');
    }
}
    
function cargarComboFilasPagina(){
    var selectorDeFilas = '<select name="filasPagina" id="filasPagina" class ="" onchange="cambiarFilasPagina();">' + 
                                    '<option value="10">10</option>' + 
                                    '<option value="25">25</option>' + 
                                    '<option value="50">50</option>' + 
                                    '<option value="100">100</option>' + 
                                '</select>';
    document.getElementById('contSelectPax').innerHTML = '<%=descriptor.getDescripcion("mosFilasPag")%>'.replace('_MENU_',selectorDeFilas); 
    document.getElementById('filasPagina').value= lineasPagina;
}

function cambiarFilasPagina(){ 
    lineasPagina = document.getElementById('filasPagina').value;

    cargarPagina(1);
}
</script>
</head>
<body class="bandaBody" onload="pleaseWait('off');cargarCombos()">
<jsp:include page="/jsp/hidepage.jsp" flush="true">
    <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
</jsp:include>
<html:form action="/EnvioAnotacionOficinasRegistroLanbide.do">
<html:hidden property="codigoOficinaRegistroOrigen"/>
<html:hidden property="codigoOficinaRegistroDestino"/>
<html:hidden property="nombreOficinaRegistroDestino"/>
<html:hidden property="pagina"/>
<html:hidden property="lineasPaginas"/>
<html:hidden property="estado"/>
<html:hidden property="listaAnotaciones"/>

<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titCtrEnvioOficinas")%></div>
<div class="contenidoPantalla">
    <table width="100%">
     <tr>
         <td style="width:18%" class="etiqueta">
                 <%=descriptor.getDescripcion("etiqOficinaOrigen")%>:
         </td>                        
         <%-- Lista desplegable con las oficina de registro de origen --%>                                                            
        <td>
            <input type="text" name="codOficinaOrigen" id="codOficinaOrigen" style="width:6%" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);"/>
            <input type="text" name="descOficinaOrigen"  id="descOficinaOrigen" style="width:80%" class="inputTextoObligatorio" readonly="true" value=""/>
            <a href="" id="anchorOficinaOrigen" name="anchorOficinaOrigen">
                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                     name="botonAplicacion" style="cursor:hand;"></span>
            </a>
        </td>
         <%-- Fin lista desplegable con las oficina de registro de origen --%>
     </tr>
     <tr>
         <td class="etiqueta">
                <%=descriptor.getDescripcion("etiqOficinaDestino")%>:
         </td>
          <%-- Lista desplegable con las oficina de registro de destino --%>
          <td>
            <input type="text" name="codOficinaDestino" id="codOficinaDestino" style="width:6%" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);"/>
            <input type="text" name="descOficinaDestino"  id="descOficinaDestino" style="width:80%" class="inputTextoObligatorio" readonly="true" value=""/>
            <a href="" id="anchorOficinaDestino" name="anchorOficinaDestino">
                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                     name="botonAplicacion" style="cursor:hand;"></span>
            </a>
        </td>
         <%-- Fin lista desplegable con las oficina de registro de destino --%>
     </tr>

     <!-- Fechas -->
      <tr>
         <td class="etiqueta">
                <%=descriptor.getDescripcion("etiqFechaDesde")%>:
        </td>
        <td>
              <html:text styleId="fechaDesde"  styleClass="inputTxtFechaObligatorio" size="8" property="fechaDesde"
                  onkeyup = "javascript:return SoloCaracteresFecha(this);"
                  onblur = "javascript:return comprobarFecha(this);"
                  onfocus = "this.select();"/>

              <A href="javascript:calClick(event);return false;" onClick="mostrarCalDesde(event);return false;" onblur="ocultarCalendarioOnBlur(event);return false;" style="text-decoration:none;">
                   <span class="fa fa-calendar" aria-hidden="true" id="calDesde" name="calDesde" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>" ></span>
              </A>
            <span class="etiqueta" style="margin-left:10%"><%=descriptor.getDescripcion("etiqFechaHasta")%>:</span>
            <html:text styleId="fechaHasta"  styleClass="inputTxtFechaObligatorio" size="8" property="fechaHasta"
                  onkeyup = "javascript:return SoloCaracteresFecha(this);"
                  onblur    = "javascript:return comprobarFecha(this);"
                  onfocus  = "this.select();"/>

              <A href="javascript:calClick(event);return false;" onClick="mostrarCalHasta(event);return false;" onblur="ocultarCalendarioOnBlur(event);return false;" style="text-decoration:none;">
                   <span class="fa fa-calendar" aria-hidden="true" id="calHasta" name="calHasta" alt="<%=descriptor.getDescripcion("altFecha")%>" title="<%=descriptor.getDescripcion("altFecha")%>" ></span>
              </A>
              <input type="button" class="botonGeneral" value='Buscar' style="margin-left:25%"
                    name='botonBuscar' onClick="javascript:pulsarBuscar();"
                    alt="Buscar" title="Buscar"/>
        </td>
    </tr>                                                                 
     <!-- fin de fechas -->
     <tr>
         <td class="etiqueta">
                <%=descriptor.getDescripcion("etiqOfiEstado")%>
         </td>
         <td>
             <input type="text" name="codEstado" id="codEstado" style="width:6%" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);"/>
             <input type="text" name="descEstado"  id="descEstado" style="width:55%" class="inputTextoObligatorio" readonly="true" value=""/>

            <a href="" id="anchorEstado" name="anchorEstado">
                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                     name="botonAplicacion" style="cursor:hand;"></span>
            </a>
         </td>
     </tr>
        <tr>
            <td colspan="2">
                <div class="dataTables_wrapper paxinacionDataTables">
                    <label id="contSelectPax"></label>
                </div>
            </td>
        </tr>            
     <tr>
         <td colspan="2" id="tabAnotaciones"></td>
      </tr>
        <tr>
            <td colspan="2" id="enlace" class="dataTables_wrapper paxinacionDataTables"></td>
        </tr>
    </table>
    <div id="confirmacion" class="botoneraPrincipal" style="display:none;">
        <input type="button" class="botonLargo2" value='<%=descriptor.getDescripcion("btnConfirmRecepAnotacion") %>'
        name="btnConfirmar" onClick="pulsarRecepcion();"
        alt="<%=descriptor.getDescripcion("btnConfirmRecepAnotacion")%>" title="<%=descriptor.getDescripcion("btnConfirmRecepAnotacion")%>">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("btnRechazarAnotacion") %>'
        name="btnRechazar" onClick="pulsarRechazar();"
        alt="<%=descriptor.getDescripcion("btnRechazarAnotacion")%>" title="<%=descriptor.getDescripcion("btnRechazarAnotacion")%>">
    </div>
    <div id="envio" class="botoneraPrincipal" style="display:none;">
        <input type="button" class="botonLargo2" value='<%=descriptor.getDescripcion("btnEnviarAnotacion") %>'
        name="btnConfirmar" onClick="pulsarEnviar();"
        alt="<%=descriptor.getDescripcion("btnEnviarAnotacion")%>" title="<%=descriptor.getDescripcion("btnEnviarAnotacion")%>">
    </div>
    <div id="imprimir" class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value='<%=descriptor.getDescripcion("btnImprimir") %>'
        name="btnImprimir" onClick="pulsarPrevioImprimir();"
        alt="<%=descriptor.getDescripcion("btnImprimir")%>" title="<%=descriptor.getDescripcion("btnImprimir")%>">
    </div>
</div>
</html:form>
<div id="desplegable" style="overflow-y: auto; overflow-x: no; visibility: hidden; BORDER: 0px"></div>

</body>
</html>