<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.FusionDivisionForm" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<html>
<head>
  <title>Fusion y Division de secciones</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
  <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
  
  <%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma=1;
    int apl=3;
    if (session.getAttribute("usuario") != null){
      usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
      apl = usuarioVO.getAppCod();
      idioma = usuarioVO.getIdioma();
    }
  %>

  <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
  <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
  <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

  <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
  <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css">
  <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
  <script type="text/javascript">
    // VARIABLES GLOBALES
    <%
      FusionDivisionForm fdForm = (FusionDivisionForm)session.getAttribute("FusionDivisionForm");
    %>
    var listaTOrigen = new Array();
    var listaTOrigenOriginal = new Array();
    var listaTDestino = new Array();
    var listaTDestinoOriginal = new Array();
    var idVias = new Array();
    var codVias = new Array();
    var descVias = new Array();
    var codECOESIVias = new Array();
    var codTipoNumeraciones = new Array();
    var descTipoNumeraciones = new Array();
    var tipoOperacion = "";
    var datosProceso = new Array();
    var vectorDatosProceso = ['codProceso','descripcion','fechaOperacion','codDistritoOrigen',
      'descDistritoOrigen','codSeccionOrigen','descSeccionOrigen','letraOrigen','codLetraSeccionOrigen',
      'codDistritoDestino','descDistritoDestino','codSeccionDestino','descSeccionDestino',
      'letraDestino','codLetraSeccionDestino'];
    var camposBusqueda = [//'codVia','descVia','codTipoNumeracion','descTipoNumeracion',
						'codESI','codNUC'];
    var combosBusqueda = ['comboVia','comboTipoNumeracion'];
    var botonTraspasar = ['botonTraspasar','botonTraspasarTodo'];
    var botonAnular = ['botonAnular','botonAnularTodo'];
    /* DANI
      var botonTraspasar = ['botonTraspasar'];
      var botonAnular = ['botonAnular'];
    */
    var botonMostrar = ['botonMostrar'];

    // INICIALIZACIÓN Y CARGA DE DATOS
    function inicializar(){
      var argumentos = self.parent.opener.xanelaAuxiliarArgs;
     
        
      recuperarDatosIniciales();
      tipoOperacion = argumentos[0];
      datosProceso = [argumentos[1][0],argumentos[1][1],argumentos[1][2],argumentos[1][6],
        argumentos[1][7],
		argumentos[1][8],argumentos[1][9],argumentos[1][10],
		argumentos[1][8]+argumentos[1][10],
        argumentos[1][11],argumentos[1][12],
		argumentos[1][13],argumentos[1][14],argumentos[1][15],
		argumentos[1][13]+argumentos[1][15]];
      document.forms[0].tipoOperacion.value = tipoOperacion;
      rellenar(datosProceso,vectorDatosProceso);
      actualizarBotonesTablas();
      habilitarGeneralInputs(botonModificar,false);
      habilitarGeneralInputs(botonVerDomicilios,false);
    }

    function recuperarDatosIniciales(){
      <% 
        Vector listaVias = fdForm.getListaVias();
        Vector listaTipoNumeraciones = fdForm.getListaTipoNumeraciones();
        int lengthVias = listaVias.size();
        int lengthTipoNumeraciones = listaTipoNumeraciones.size();
        int i=0;
        String idVias="";
        String codVias="";
        String descVias="";
        String codECOESIVias="";
        if(lengthVias>0){
          for(i=0;i<lengthVias-1;i++){
            GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
            idVias+="\""+vias.getAtributo("idVia")+"\",";
            codVias+="\""+vias.getAtributo("codVia")+"\",";
            descVias+="'"+vias.getAtributo("descVia")+"',";
            codECOESIVias+="[\""+vias.getAtributo("codECO")+"\","+
              "\""+vias.getAtributo("descECO")+"\","+
              "\""+vias.getAtributo("codESI")+"\","+
              "\""+vias.getAtributo("descESI")+"\","+
              "\""+vias.getAtributo("codNUC")+"\","+
              "\""+vias.getAtributo("descNUC")+"\"],";
          }
          GeneralValueObject vias = (GeneralValueObject)listaVias.get(i);
          idVias+="\""+vias.getAtributo("idVia")+"\"";
          codVias+="\""+vias.getAtributo("codVia")+"\"";
          descVias+="'"+vias.getAtributo("descVia")+"'";
          codECOESIVias+="[\""+vias.getAtributo("codECO")+"\","+
            "\""+vias.getAtributo("descECO")+"\","+
            "\""+vias.getAtributo("codESI")+"\","+
            "\""+vias.getAtributo("descESI")+"\","+
            "\""+vias.getAtributo("codNUC")+"\","+
            "\""+vias.getAtributo("descNUC")+"\"]";
        }
        String codTipoNumeraciones = "";
        String descTipoNumeraciones = "";
        if(lengthTipoNumeraciones>0){
          for(i=0;i<lengthTipoNumeraciones-1;i++){
            GeneralValueObject tipoNumeraciones = (GeneralValueObject)listaTipoNumeraciones.get(i);
            codTipoNumeraciones+="\""+tipoNumeraciones.getAtributo("codigo")+"\",";
            descTipoNumeraciones+="\""+tipoNumeraciones.getAtributo("descripcion")+"\",";
          }
          GeneralValueObject tipoNumeraciones = (GeneralValueObject)listaTipoNumeraciones.get(i);
          codTipoNumeraciones+="\""+tipoNumeraciones.getAtributo("codigo")+"\"";
          descTipoNumeraciones+="\""+tipoNumeraciones.getAtributo("descripcion")+"\"";
        }
      %>
      idVias = [<%=idVias%>];
      codVias = [<%=codVias%>];
      descVias = [<%=descVias%>];
      codECOESIVias = [<%=codECOESIVias%>];
      codTipoNumeraciones = [<%=codTipoNumeraciones%>];
      descTipoNumeraciones = [<%=descTipoNumeraciones%>];
      // CARGAR COMBOS
      comboVia.addItems(codVias,descVias);
      comboTipoNumeracion.addItems(codTipoNumeraciones,descTipoNumeraciones);
    }

    function actualizarTablas(){
      tablaOrigen.lineas = listaTOrigen;
      tablaDestino.lineas = listaTDestino;
      refresca(tablaOrigen);
      refresca(tablaDestino);
      actualizarBotonesTablas();
    }

    function actualizarBotonesTablas(){
      if(tablaOrigen.lineas.length>0)
        habilitarGeneralInputs(botonTraspasar,true);
      else
        habilitarGeneralInputs(botonTraspasar,false);
      if(tablaDestino.lineas.length>0)
        habilitarGeneralInputs(botonAnular,true);
      else
        habilitarGeneralInputs(botonAnular,false);
    }

    // FUNCIONES DE PULSACION DE BOTONES
    function pulsarMostrar(){
      var codVia = document.forms[0].codVia.value;
      var codTipoNumeracion = document.forms[0].codTipoNumeracion.value;
      if((codVia!="")&&(codTipoNumeracion!="")){
        document.forms[0].opcion.value="buscarTramosProceso";
        document.forms[0].target="oculto";
        document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
        document.forms[0].submit();
        //habilitarGeneralCombos(combosBusqueda,false);
		desHabilitarCombos(true);
        habilitarGeneralInputs(botonMostrar,false);
      }else
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
    }

    function pulsarCancelarMostrar(){
      //habilitarGeneralCombos(combosBusqueda,true);
	  desHabilitarCombos(false);
	  habilitarGeneralInputs(botonMostrar,true);
      limpiar(camposTDestino);
      limpiar(camposBusqueda);
	  limpiarCombos();
      tablaOrigen.lineas = new Array();
      tablaDestino.lineas = new Array();
	  /* KR */
	  listaTOrigen = new Array();
      listaTOrigenOriginal = new Array();
      listaTDestino = new Array();
   	  listaTDestinoOriginal = new Array();
	  	  
      refresca(tablaOrigen);
      refresca(tablaDestino);
      actualizarBotonesTablas();
    }

    function moverElemento(listaOrigen,indice,listaDestino){
      var nuevaListaOrigen = new Array();
      var i = 0;
      var j = 0;
      for(i=0;i<listaOrigen.length;i++){
        if(i!=indice){
          nuevaListaOrigen[j] = listaOrigen[i];
          j++;
        }
      }
      listaDestino[listaDestino.length] = listaOrigen[indice];
      listaOrigen = nuevaListaOrigen;
      return [listaOrigen,listaDestino];
    }

// N TRAMOS 180204
    function moverElementoOriginal(listaOrigen,origen,listaDestino){
      var nuevaListaOrigen = new Array();
      var i = 0;
      var j = 0;
      var indice = 0;
      for(i=0;i<listaOrigen.length;i++){
        var numD = listaOrigen[i][4]+"-"+listaOrigen[i][5];
        var numH = listaOrigen[i][6]+"-"+listaOrigen[i][7];
        
        if((numD!=origen[1])||(numH!=origen[2])){
          nuevaListaOrigen[j] = copiarArray(listaOrigen[i]);
          j++;
        }else{
          indice = i;
          listaOrigen[indice][1] = document.forms[0].codDistritoDestino.value;
          listaOrigen[indice][2] = document.forms[0].codSeccionDestino.value;
          listaOrigen[indice][3] = document.forms[0].letraDestino.value;
          listaDestino[listaDestino.length] = listaOrigen[indice];
        }
      }
      listaOrigen = nuevaListaOrigen;	  
      return [listaOrigen,listaDestino];
    }	
// 180204
    function cambiarEstadoOrigen(tramo,estado){
      var i=0;
      for(i=0;i<listaTOrigenOriginal.length;i++){
        if(listaTOrigenOriginal[i][0]==tramo){
          listaTOrigenOriginal[i][8]=estado;
        }
      }
    }

    function copiarArray(source){
      var i=0;
      var destino = new Array();
      for(i=0;i<source.length;i++){
        destino[i] = source[i];
      }
      return destino;
    }

    function actualizarDestino(tramo){
      var i=0;
      for(i=0;i<listaTOrigenOriginal.length;i++){
        if(listaTOrigenOriginal[i][0]==tramo){
          var tramoDestino = copiarArray(listaTOrigenOriginal[i]);
          tramoDestino[1] = document.forms[0].codDistritoDestino.value;
          tramoDestino[2] = document.forms[0].codSeccionDestino.value;
          tramoDestino[3] = document.forms[0].letraDestino.value;
          tramoDestino[8] = "C";
          var longDestino = listaTDestinoOriginal.length;
          listaTDestinoOriginal[longDestino] = tramoDestino;
        }
      }
    }

    function traspasarTodo(){
      var i=0;
      var listas = new Array();
      while(listaTOrigen.length>0){
        if(listaTOrigen[i][3]=="S"){
          var tramo = listaTOrigen[i][0];
          cambiarEstadoOrigen(tramo,"E"); // LO TENGO QUE ELIMINAR
          // ACTUALIZO LA LISTA DESTINO ORIGINAL
          actualizarDestino(tramo);
          //LO ELIMINO DE LA TABLA ORIGEN Y LO PONGO EN LA TABLA DESTINO
          listas = moverElemento(listaTOrigen,i,listaTDestino);
          listaTOrigen = listas[0];
          listaTDestino = listas[1];
          tablaOrigen.lineas = listaTOrigen;
          tablaDestino.lineas = listaTDestino;
          refresca(tablaOrigen);
          refresca(tablaDestino);
          actualizarBotonesTablas();
        } //N_TRAMOS 180204
        else if(listaTOrigen[i][3]=="C"){
          // ACTUALIZO LA LISTA DESTINO ORIGINAL
          var listas = moverElementoOriginal(listaTOrigenOriginal,listaTOrigen[i],listaTDestinoOriginal);
          listaTOrigenOriginal = listas[0];
          listaTDestinoOriginal = listas[1];
          //LO ELIMINO DE LA TABLA ORIGEN Y LO PONGO EN LA TABLA DESTINO
          listas = moverElemento(listaTOrigen,i,listaTDestino);
          listaTOrigen = listas[0];
          listaTDestino = listas[1];
          tablaOrigen.lineas = listaTOrigen;
          tablaDestino.lineas = listaTDestino;
          refresca(tablaOrigen);
          refresca(tablaDestino);
          actualizarBotonesTablas();
        }
      }
      
    }

    function pulsarTraspasar(){
      var indice = tablaOrigen.focusedIndex;
      limpiar(camposTDestino);
      if(indice>=0){
        if(listaTOrigen[indice][3]=="S"){
          var tramo = listaTOrigen[indice][0];
          cambiarEstadoOrigen(tramo,"E"); // LO TENGO QUE ELIMINAR
          // ACTUALIZO LA LISTA DESTINO ORIGINAL
          actualizarDestino(tramo);
          //LO ELIMINO DE LA TABLA ORIGEN Y LO PONGO EN LA TABLA DESTINO
	  listaTOrigen[indice][3]="C";
          var listas = moverElemento(listaTOrigen,indice,listaTDestino);
          listaTOrigen = listas[0];
          listaTDestino = listas[1];
          tablaOrigen.lineas = listaTOrigen;
          tablaDestino.lineas = listaTDestino;
          refresca(tablaOrigen);
          refresca(tablaDestino);
          actualizarBotonesTablas();
        } // N TRAMOS 180204
        else if(listaTOrigen[indice][3]=="C"){
          // ACTUALIZO LA LISTA DESTINO ORIGINAL
          var listas = moverElementoOriginal(listaTOrigenOriginal,listaTOrigen[indice],listaTDestinoOriginal);
          listaTOrigenOriginal = listas[0];
          listaTDestinoOriginal = listas[1];
          //LO ELIMINO DE LA TABLA ORIGEN Y LO PONGO EN LA TABLA DESTINO
          listas = moverElemento(listaTOrigen,indice,listaTDestino);
          listaTOrigen = listas[0];
          listaTDestino = listas[1];
          tablaOrigen.lineas = listaTOrigen;
          tablaDestino.lineas = listaTDestino;
          refresca(tablaOrigen);
          refresca(tablaDestino);
          actualizarBotonesTablas();
        }
      }
    }

    function actualizarAnularTramo(tramo){
      var i = 0;
      var j = 0;
      var numeracion = 0;
      var nuevaListaOriginal = new Array();
	  var tramoEntero = false;
	  
      for(i=0;i<listaTOrigenOriginal.length;i++){
	  	var numD = listaTOrigenOriginal[i][4] + "-" + listaTOrigenOriginal[i][5];
		var numH = listaTOrigenOriginal[i][6] + "-" + listaTOrigenOriginal[i][7];
		if ((numD != tramo[1]) || (numH != tramo[2]) ) {
		  nuevaListaOriginal[j++] = copiarArray(listaTOrigenOriginal[i]);
		} else {
	          if(listaTOrigenOriginal[i][8]=="E"){
			   	// EL tramo original que antes queriamos eliminar se restaura a S       
				tramo[3] ="S";
            	nuevaListaOriginal[j] = copiarArray(listaTOrigenOriginal[i]);
				nuevaListaOriginal[j++][8]="S";
				tramoEntero = true;
			  } else {
				if (!tramoEntero)
					nuevaListaOriginal[j++] = copiarArray(listaTOrigenOriginal[i]);
			  }
         }
      }
      listaTOrigenOriginal = nuevaListaOriginal;
    }


	function reconstruirTramos(){
      var i = 0;
      var j = 0;
	  var k = 0;
	  var l = 0;
      var numeracion = 0;
      var nuevaListaOriginal = new Array();
	  var nuevaLista = new Array();
	  var listaReconstruidos = new Array();
	  var tramoEntero = false;
	  
      for(i=0;i<listaTOrigenOriginal.length;i++){
		if(listaTOrigenOriginal[i][8]=="E"){
			 var tramoEnDestino = false;
			 j = 0;
			 while ( j<listaTDestinoOriginal.length && !tramoEnDestino ){
				if(listaTDestinoOriginal[j][0]== listaTOrigenOriginal[i][0]) {
					tramoEnDestino = true;
			 	} else j++;
			}
			if (!tramoEnDestino) {
				listaTOrigenOriginal[i][8] = "S";
				listaReconstruidos[l] = new Array();
				listaReconstruidos[l][0] = listaTOrigenOriginal[i][0];
				listaReconstruidos[l++][1] = i;
			} 
		} 
	 } // Fin for
		
     for(i=0;i<listaTOrigenOriginal.length;i++){
		if(listaTOrigenOriginal[i][8]=="C"){
			 var tramoReconstruido = false;
			 j = 0;
			 while ( j<listaReconstruidos.length && !tramoReconstruido ){
				if(listaReconstruidos[j][0]== listaTOrigenOriginal[i][0]) {
					tramoReconstruido = true;
			 	} else j++;
			}
			if (!tramoReconstruido)
				nuevaListaOriginal[k++] = copiarArray(listaTOrigenOriginal[i]);
		} else nuevaListaOriginal[k++] = copiarArray(listaTOrigenOriginal[i]);
	 }		
	 
	 // Para la tabla pintada	 
	 k=0;
	 for(i=0;i<listaTOrigen.length;i++){
		if(listaTOrigen[i][3]=="C"){
			 var tramoReconstruido = false;
			 j = 0;
			 while ( j<listaReconstruidos.length && !tramoReconstruido ){
				if(listaReconstruidos[j][0]== listaTOrigen[i][0]) {
					tramoReconstruido = true;
			 	} else j++;
			}
			if (!tramoReconstruido) 
				nuevaLista[k++] = copiarArray(listaTOrigen[i]);			
		} else nuevaLista[k++] = copiarArray(listaTOrigen[i]);
	 }	
	 
	 for(i=0;i<listaReconstruidos.length;i++){
	 	var nuevo = [listaTOrigenOriginal[listaReconstruidos[i][1]][0],
					listaTOrigenOriginal[listaReconstruidos[i][1]][4] +"-"+ listaTOrigenOriginal[listaReconstruidos[i][1]][5] ,
					listaTOrigenOriginal[listaReconstruidos[i][1]][6] +"-"+ listaTOrigenOriginal[listaReconstruidos[i][1]][7] ,
					"S"];
	 	nuevaLista[k++] = copiarArray(nuevo);
	 }				 
      listaTOrigen = nuevaLista;
	  tablaOrigen.lineas = listaTOrigen;
	  refresca(tablaOrigen);
      listaTOrigenOriginal = nuevaListaOriginal;
    }
	

    function anularTodo(){
      var i = 0;
      while(i<listaTDestino.length){
        if(listaTDestino[i][3]!="P"){		
			var listas = moverElementoOriginal(listaTDestinoOriginal, listaTDestino[i], listaTOrigenOriginal);
			listaTOrigenOriginal = listas[1];
			listaTDestinoOriginal = listas[0];		
          	var tramo = listaTDestino[i];
		  	actualizarAnularTramo(tramo);		  
          	var listas = moverElemento(listaTDestino,i,listaTOrigen);
          	listaTOrigen = listas[1];
          	listaTDestino = listas[0];
          	//alert(listaTOrigenOriginal);
          	//alert(listaTDestinoOriginal);
          	tablaOrigen.lineas = listaTOrigen;
          	tablaDestino.lineas = listaTDestino;
          	refresca(tablaOrigen);
          	refresca(tablaDestino);
          	actualizarBotonesTablas();
			i=0;
		} else i++;
      }
    }

    function pulsarAnular(){
      var indice = tablaDestino.focusedIndex;	  
      limpiar(camposTDestino);
      if((indice>=0)&&(listaTDestino[indice][3]!="P")){
        var tramo = listaTDestino[indice];
		var listas = moverElementoOriginal(listaTDestinoOriginal, listaTDestino[indice], listaTOrigenOriginal);
		listaTOrigenOriginal = listas[1];
		listaTDestinoOriginal = listas[0];
		actualizarAnularTramo(tramo);
        var listas = moverElemento(listaTDestino,indice,listaTOrigen);
        listaTOrigen = listas[1];
        listaTDestino = listas[0];

        //alert(listaTOrigenOriginal);
        //alert(listaTDestinoOriginal);
        tablaOrigen.lineas = listaTOrigen;
        tablaDestino.lineas = listaTDestino;
        refresca(tablaOrigen);
        refresca(tablaDestino);
        actualizarBotonesTablas();
      }
    }

 function buscarTramoOriginal(tramo){
      var i=0;
      for(i=0;i<listaTDestinoOriginal.length;i++){
        var numD = listaTDestinoOriginal[i][4]+"-"+listaTDestinoOriginal[i][5];
        var numH = listaTDestinoOriginal[i][6]+"-"+listaTDestinoOriginal[i][7];
        if((numD==tramo[1])&&(numH==tramo[2])){
          return listaTDestinoOriginal[i];
        }
      }
  }

    function esNumeroPar(numero){
      var resto = numero % 2;
      if(resto==0) return true;
      else
        return false;
    }

    function tramoValido(tramoNuevo,tramoOriginal){
      var numDTNuevo = parseInt(tramoNuevo[0]);
      var numHTNuevo = parseInt(tramoNuevo[2]);
      var numDTramo = parseInt(tramoOriginal[4]);
      var numHTramo = parseInt(tramoOriginal[6]);
      var tipoNumeracion = document.forms[0].codTipoNumeracion.value;
      if((tipoNumeracion=="1")&&(!esNumeroPar(numDTNuevo))&&(!esNumeroPar(numHTNuevo))){
        if((numDTNuevo>=numDTramo)&&(numDTNuevo<=numHTramo) &&
          (numHTNuevo>=numDTramo)&&(numHTNuevo<=numHTramo))
          return true;
      }else if((tipoNumeracion=="2")&&(esNumeroPar(numDTNuevo))&&(esNumeroPar(numHTNuevo))){
        if((numDTNuevo>=numDTramo)&&(numDTNuevo<=numHTramo) &&
          (numHTNuevo>=numDTramo)&&(numHTNuevo<=numHTramo))
          return true;
      }
      return false;
    }

    function actualizarOrigen(tramosNuevos){
      var i=0;
      var j=0;
      var nuevaListaOriginal = new Array();
      if(tramosNuevos.length>0){
      //alert("tramo 1: "+tramo1);
      //alert("tramo 2: "+tramo2);
      //alert("Actualizar Origen: "+listaTOrigenOriginal);
      for(i=0;i<listaTOrigenOriginal.length;i++){
        //alert("CODIGO: "+listaTOrigenOriginal[i][0]+"-"+tramo1[0]);
        if(listaTOrigenOriginal[i][0]==tramosNuevos[0][0]){
          //alert("Estado: "+listaTOrigenOriginal[i][8]);
          if(listaTOrigenOriginal[i][8]=="E"){
            nuevaListaOriginal[j] = copiarArray(listaTOrigenOriginal[i]);
            j++;
          }
        }else{
          nuevaListaOriginal[j] = copiarArray(listaTOrigenOriginal[i]);
          j++;
        }
      }
      var longOriginal = listaTOrigen.length;
      for(i=0;i<tramosNuevos.length;i++){
        nuevaListaOriginal[j] = copiarArray(tramosNuevos[i]);
        listaTOrigen[longOriginal] = new Array();
        listaTOrigen[longOriginal][0] = tramosNuevos[i][0];
        listaTOrigen[longOriginal][1] = tramosNuevos[i][4]+"-"+tramosNuevos[i][5];
        listaTOrigen[longOriginal][2] = tramosNuevos[i][6]+"-"+tramosNuevos[i][7];
        listaTOrigen[longOriginal][3] = "C";
        longOriginal++;
        j++;
      }
      listaTOrigenOriginal = nuevaListaOriginal;
      tablaOrigen.lineas = listaTOrigen;
      refresca(tablaOrigen);
      //alert("Actualizar Origen: "+listaTOrigenOriginal);
    }
    }

    function actualizarTramoDestino(tramo, tramoOriginal){
      var i=0;
      for(i=0;i<listaTDestinoOriginal.length;i++){
        //if(listaTDestinoOriginal[i][0]==tramo[0]){
		var numD = listaTDestinoOriginal[i][4]+"-"+listaTDestinoOriginal[i][5];
        var numH = listaTDestinoOriginal[i][6]+"-"+listaTDestinoOriginal[i][7];
		var numDTramo = tramoOriginal[4]+"-"+tramoOriginal[5];
        var numHTramo = tramoOriginal[6]+"-"+tramoOriginal[7];
        if((numD==numDTramo)&&(numH==numHTramo)){
          listaTDestinoOriginal[i] = tramo;
        }
      }
      // ACTUALIZO LA LISTA DE LA TABLA
      for(i=0;i<listaTDestino.length;i++){
        //if(listaTDestino[i][0]==tramo[0]){
		var numDTramo = tramoOriginal[4]+"-"+tramoOriginal[5];
        var numHTramo = tramoOriginal[6]+"-"+tramoOriginal[7];
        if((listaTDestino[i][1]==numDTramo)&&(listaTDestino[i][2]==numHTramo)){
          listaTDestino[i][1] = tramo[4]+"-"+tramo[5];
          listaTDestino[i][2] = tramo[6]+"-"+tramo[7];
        }
      }
      tablaDestino.lineas = listaTDestino;
      refresca(tablaDestino);
    }

    function calcularNuevosTramos(tramoNuevo,tramoOriginal){
      var numDTNuevo = parseInt(tramoNuevo[0]);
      var numHTNuevo = parseInt(tramoNuevo[2]);
      var numDTramo = parseInt(tramoOriginal[4]);
      var numHTramo = parseInt(tramoOriginal[6]);
      var tramoO1 = copiarArray(tramoOriginal);
      var tramoD1 = copiarArray(tramoOriginal);
      var tramoO2 = copiarArray(tramoOriginal);
      var tramosNuevos = new Array();
      var j = 0;
      if(numDTNuevo>numDTramo){
	    tramoO1[1] = document.forms[0].codDistritoOrigen.value;
        tramoO1[2] = document.forms[0].codSeccionOrigen.value;
        tramoO1[3] = document.forms[0].letraOrigen.value;
        tramoO1[4] = numDTramo;
        tramoO1[6] = (numDTNuevo>numDTramo)?numDTNuevo-2:numDTramo;
        tramoO1[8] = "C";
        tramosNuevos[j] = tramoO1;
        j++;
      }
      tramoD1[1] = document.forms[0].codDistritoDestino.value;
      tramoD1[2] = document.forms[0].codSeccionDestino.value;
      tramoD1[3] = document.forms[0].letraDestino.value;
      tramoD1[4] = numDTNuevo;
      tramoD1[6] = numHTNuevo;
      tramoD1[8] = "C";
      if(numHTNuevo<numHTramo){
	  	tramoO2[1] = document.forms[0].codDistritoOrigen.value;
        tramoO2[2] = document.forms[0].codSeccionOrigen.value;
        tramoO2[3] = document.forms[0].letraOrigen.value;
        tramoO2[4] = (numHTNuevo<numHTramo)?numHTNuevo+2:numHTramo;
        tramoO2[6] = numHTramo;
        tramoO2[8] = "C";
        tramosNuevos[j] = tramoO2;
        j++;
      }	  
      
      actualizarOrigen(tramosNuevos);
      actualizarTramoDestino(tramoD1,tramoOriginal);
      actualizarBotonesTablas();
      limpiar(camposTDestino);
      habilitarGeneralInputs(botonModificar,false);
      habilitarGeneralInputs(botonVerDomicilios,false);
    }

    function pulsarModificarFila(){
      var tipoNumeracion = document.forms[0].codTipoNumeracion.value;
      if(tipoNumeracion=="0"){
        // SIN NUMERACION
        jsp_alerta("A",'<%=descriptor.getDescripcion("msjTramSModNMod")%>');
      }else{
        var indice = tablaDestino.focusedIndex;
        if(indice>=0 && !tablaDestino.ultimoTable){
        var tramoOriginal = buscarTramoOriginal(listaTDestino[indice]);
        //alert(tramoOriginal);
        var numDesde = document.forms[0].numDesde.value;
        var letraDesde = document.forms[0].letraDesde.value;
        var numHasta = document.forms[0].numHasta.value;
        var letraHasta = document.forms[0].letraHasta.value;
        if((numDesde!="")&&(numHasta!="")){
          var tramoNuevo = [numDesde,letraDesde,numHasta,letraHasta];
          if(tramoValido(tramoNuevo,tramoOriginal)){
            //alert("Origen: "+listaTOrigenOriginal);
            //alert("Destino: "+listaTDestinoOriginal);
            calcularNuevosTramos(tramoNuevo,tramoOriginal);
            //alert("Origen: "+listaTOrigenOriginal);
            //alert("Destino: "+listaTDestinoOriginal);
          }else{
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjTramInv")%>');
          }
        }else{
          jsp_alerta("A",'<%=descriptor.getDescripcion("msjRellNumDH")%>');
        }
      }
    }
    }

    function pulsarVerDomicilios(){
      //habilitarGeneralCombos(combosBusqueda,true);
	  desHabilitarCombos(false);
      document.forms[0].opcion.value="inicializarVerDomicilios";
      document.forms[0].target="oculto";
      document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
      document.forms[0].submit();
      //habilitarGeneralCombos(combosBusqueda,false);
	  desHabilitarCombos(true);      
    }

    function construirArraySubmit(lista){
      var i=0;
      var j=0;
      var nuevaLista = new Array();
      for(i=0;i<lista.length;i++){
        nuevaLista[i] = new Array();
        for(j=0;j<lista[i].length;j++){
          nuevaLista[i][j] = lista[i][j]+"¥";
        }
        nuevaLista[i][j] = "§";
      }
      return nuevaLista;
    }

   
    function pulsarProcesar(){
	  
	  // Comprobaciones iniciales 
	  if ( document.forms[0].traspasarVial.checked && !document.forms[0].traspasarTodos.checked) {
	  	if (document.forms[0].codVia.value == "") {
			jsp_alerta("A",'<%=descriptor.getDescripcion("msjSeleccVia")%>');
			return;
		}
	  } else if (!document.forms[0].traspasarVial.checked && !document.forms[0].traspasarTodos.checked){	  
	  		if (!document.getElementById("botonMostrar").disabled ){
				jsp_alerta("A",'<%=descriptor.getDescripcion("msjSeleccViaT")%>');
				return;	  
			}
	  }
	  
	  var confirmar = true;
	  var msj = '<%=descriptor.getDescripcion("msjCVialTram")%>';
	  var msj2 = '<%=descriptor.getDescripcion("msjCSinOpT")%>';
	  
	  if (document.forms[0].traspasarTodos.checked) {
	  	msj = '<%=descriptor.getDescripcion("msjCSeccCompl")%>';
		msj2 = '<%=descriptor.getDescripcion("msjCSinOpS")%>';
	  } else if (document.forms[0].traspasarVial.checked) {
	  	msj = '<%=descriptor.getDescripcion("msjCVialCompl")%>';
		msj2 = '<%=descriptor.getDescripcion("msjCSinOpV")%>';
	  }
	  		

	 reconstruirTramos();
	  
	  if(jsp_alerta("C",msj) ==1) {	  
	  	if (!document.forms[0].generarOperaciones.checked) {
			if(jsp_alerta("C",msj2) ==1)
				confirmar = true;		
		} else confirmar = true;
			
		 if (confirmar) {			
		      //habilitarGeneralCombos(combosBusqueda,true);
			  desHabilitarCombos(false);
		      document.forms[0].tramosOrigen.value = construirArraySubmit(listaTOrigenOriginal);
		      document.forms[0].tramosDestino.value = construirArraySubmit(listaTDestinoOriginal);
		      document.forms[0].opcion.value="procesarProceso";
		      document.forms[0].target="oculto";
		      document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
		      document.forms[0].submit();			  
		      //habilitarGeneralCombos(combosBusqueda,false);
				desHabilitarCombos(true);
	 	 }
	 }	 
    }

    function pulsarCancelar(){
      self.parent.opener.retornoXanelaAuxiliar();
    }
	
	function volverAiniciar(){
		document.forms[0].opcion.value="procesar";
      	document.forms[0].target="mainFrame";
      	document.forms[0].action="<%=request.getContextPath()%>/territorio/FusionDivisionSecciones.do";
      	document.forms[0].submit();
	}
  </script>

</head>
<body class="bandaBody" onload="inicializar()">
<form action="" method="post" name="formulario" id="formulario">
    <input type="hidden" name="opcion" value="">
    <input type="hidden" name="codECO" value="">
    <input type="hidden" name="codESI" value="">
    <input type="hidden" name="codNUC" value="">
    <input type="hidden" name="idVia" value="">
    <input type="hidden" name="tipoOperacion" value="">
    <input type="hidden" name="codProceso" value="">
    <input type="hidden" name="codTramo" value="">
    <input type="hidden" name="numDesde1" value="">
    <input type="hidden" name="numHasta1" value="">
    <input type="hidden" name="tramosOrigen" value="">
    <input type="hidden" name="tramosDestino" value="">
    <input type="hidden" name="fechaOperacion" value="">
    
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titFusDivSecc")%></div>
    <div class="contenidoPantalla">
        <table align="center" width="90%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                        <td>
                                <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#ffffff" style="border-top: #666666 1px solid; border-bottom: #666666 1px solid;border-left: #666666 1px solid;border-right: #666666 1px solid;">
                                        <tr>
                                                <td>
                                                        <table align="center" width="90%" border="0" cellspacing="4" cellpadding="0">
                                                                <tr>
                                                                        <td width="35%" class="etiqueta"><%=descriptor.getDescripcion("etiqDescProc")%>:</td>
                                                                        <td>
                                                                                <input class="inputTexto" name="descripcion" 
                                                                                        type="text" size="70" maxlength="50" readonly>
                                                                        </td>
                                                                </tr>
                                                        </table>
                                                </td>
                                        </tr>
                                        <tr>
                                                <td>
                                                        <table align="center" width="90%" border="0" cellspacing="4" cellpadding="0">
                                                                <tr>
                                                                        <td width="50%"> 
                                                                                <table width="100%" border="0" cellspacing="2" cellpadding="0" style="border-top: #666666 1px solid; border-bottom: #666666 1px solid;border-left: #666666 1px solid;border-right: #666666 1px solid;">
                                                                                        <tr>
                                                                                                <td  class="sub3titulo"><%=descriptor.getDescripcion("etiqOrig")%></td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                                <td>
                                                                                                        <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                                                                                                                <tr>
                                                                                                                        <td class="etiqueta" width="20%"><%=descriptor.getDescripcion("gEtiq_Distrito")%>: </td>
                                                                                                                        <td width="80%">
                                                                                                                                <input name="codDistritoOrigen" type="text" class="inputTexto"
                                                                                                                                        id="codDistritoOrigen" size="3" readonly>
                                                                                                                                <input name="descDistritoOrigen" type="text" class="inputTexto"
                                                                                                                                        id="descDistritoOrigen" style="width:100" readonly>
                                                                                                                        </td>
                                                                                                                </tr>
                                                                                                        </table>
                                                                                                </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                                <td>
                                                                                                        <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                                                                                                                <tr>
                                                                                                                        <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_Seccion")%>: </td>
                                                                                                                        <td width="80%">
                                                                                                                                <input name="codSeccionOrigen" type="hidden" class="inputTexto"
                                                                                                                                        id="codSeccionOrigen" size="3" readonly>
                                                                                                                                <input name="letraOrigen" type="hidden" class="inputTexto"
                                                                                                                                        id="letraOrigen" readonly size="1">
                                                                                                                                <input name="codLetraSeccionOrigen" type="text" class="inputTexto"
                                                                                                                                        id="codLetraSeccionOrigen" size="4" readonly>
    <input name="descSeccionOrigen" type="text" class="inputTexto"
                                                                                                                                        id="descSeccionOrigen" style="width:100" readonly>

                                                                                                                        </td>
                                                                                                                </tr>
                                                                                                        </table>
                                                                                                </td>
                                                                                        </tr>
                                                                                </table> 
                                                                        </td>
                                                                        <td width="50%"> 
                                                                                <table width="100%" border="0" cellspacing="2" cellpadding="0" style="border-top: #666666 1px solid; border-bottom: #666666 1px solid;border-left: #666666 1px solid;border-right: #666666 1px solid;">
                                                                                        <tr>
                                                                                                <td class="sub3titulo"><%=descriptor.getDescripcion("etiqDest")%></td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                                <td>
                                                                                                        <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                                                                                                                <tr>
                                                                                                                        <td class="etiqueta" width="20%"><%=descriptor.getDescripcion("gEtiq_Distrito")%>: </td>
                                                                                                                        <td width="80%">
                                                                                                                                <input name="codDistritoDestino" type="text" class="inputTexto"
                                                                                                                                        id="codDistritoDestino" size="3" readonly>
                                                                                                                                <input name="descDistritoDestino" type="text" class="inputTexto"
                                                                                                                                        id="descDistritoDestino" style="width:100" readonly>
                                                                                                                        </td>
                                                                                                                </tr>
                                                                                                        </table>
                                                                                                </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                                <td>
                                                                                                        <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                                                                                                                <tr>
                                                                                                                        <td class="etiqueta" width="20%"><%=descriptor.getDescripcion("gEtiq_Seccion")%>: </td>
                                                                                                                        <td width="80%">
                                                                                                                                <input name="codSeccionDestino" type="hidden" class="inputTexto"
                                                                                                                                        id="codSeccionDestino" size="3" readonly>
                                                                                                                                <input name="letraDestino" type="hidden" class="inputTexto"
                                                                                                                                        id="letraDestino" readonly size="1">
                                                                                                                                <input name="codLetraSeccionDestino" type="text" class="inputTexto"
                                                                                                                                        id="codLetraSeccionDestino" size="4" readonly>
                                                                                                                        <input name="descSeccionDestino" type="text" class="inputTexto"
                                                                                                                                        id="descSeccionDestino" style="width:100" readonly>
                                                                                                                        </td>
                                                                                                                </tr>
                                                                                                        </table>
                                                                                                </td>
                                                                                        </tr>
                                                                                </table> 
                                                                        </td>
                                                                </tr>
                                                        </table>
                                                </td>
                                        </tr>
                                        <tr>
                                                <td>
                                                        <table align="center" width="90%" cellpadding="0" cellspacing="4" border="0">
                                                                <tr>
                                                                        <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("etiqVia")%>:</td>
    <td width="57%">
                                                                                <input type="text" class="inputTextoObligatorio" size=3 id="codVia" 
                                                                                        name="codVia">
                                                                                <input id="descVia" name="descVia" type="text" class="inputTextoObligatorio" 
                                                                                        readonly="true" style="width:200" maxlength=50>
                                                                                <a href="" name="anchorVia" id="anchorVia">
                                                                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                                                id="botonVia" name="botonVia" 
                                                                                                style="cursor:'hand'"></span>
                                                                                </a>
                                                                        </td>
    <td align="center">
        <!-- botón MOSTRAR -->
        <input type="button" class="botonGeneral" id="botonMostrar" 
        name="botonMostrar" value="<%=descriptor.getDescripcion("etiqMostrar")%>"
        onclick="pulsarMostrar();">
    </td>
                                                                </tr>
                                                        </table>
                                                </td>
                                        </tr>
                                        <tr>
                                                <td>
                                                        <table align="center" width="90%" cellpadding="0" cellspacing="4" border="0">
                                                                <tr>
                                                                        <td width="25%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_TipoNum")%>:</td>
                                                                        <td width="57%">
                                                                                <input type="text" class="inputTextoObligatorio" size=3 id="codTipoNumeracion" 
                                                                                        name="codTipoNumeracion">
                                                                                <input id="descTipoNumeracion" name="descTipoNumeracion" type="text" 
                                                                                        class="inputTextoObligatorio" readonly="true" style="width:200" 
                                                                                        maxlength=50>
                                                                                <a href="" name="anchorTipoNumeracion" id="anchorTipoNumeracion">
                                                                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" 
                                                                                                id="botonTipoNumeracion" name="botonTipoNumeracion" 
                                                                                                style="cursor:'hand'"></span>
                                                                                </a>
                                                                        </td>
                                                                        <td align="center">
                                                                            <!-- Botón CANCELAR -->
                                                                            <input name="botonCancelarMostrar" type="button" class="botonGeneral" id="botonCancelarMostrar"
                                                                            accesskey="C" value="<%=descriptor.getDescripcion("gbCancelar")%>"
                                                                            onclick="pulsarCancelarMostrar();">
                                                                        </td>
                                                                </tr>
                                                        </table>
                                                </td>
                                        </tr>
                                </table>
                        </td>
                </tr>
                <tr>
                        <td> 
                                <table width="100%" border="0" cellspacing="4" cellpadding="0">
                                        <tr>
                                                <td width="42%">
                                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr>
                                                                        <td id="tablaOrigen" align="center"></td>
                                                                </tr>
                                                        </table>
                                                </td>
                                                <td width="16%">
                                                        <table width="100%" border="0" cellspacing="4" cellpadding="0">
                                                                <tr>
                                                                        <td align="center">
                                                                                <!-- Botón TRASPASAR -->
                                                                            <TD bgcolor="#ffffff" width="100%" height="100%" align="center" valign="middle">
                                                                                    <input class="botonGeneral" name="botonTraspasar" type="button"     
                                                                                            id="botonTraspasar" value="&gt;" alt="Traspasar"
                                                                                            onclick="pulsarTraspasar();">
                                                                        </td>
                                                                </tr>
                                                                <tr>
                                                                        <td align="center">
                                                                                <!-- Botón TRASPASAR TODO -->
                                                                            <input class="botonGeneral" name="botonTraspasarTodo" type="button"     
                                                                                    id="botonTraspasarTodo" value="&gt;&gt;" alt="Traspasar Todo"
                                                                                    onclick="traspasarTodo();">
                                                                        </td>
                                                                </tr>
                                                                <tr>
                                                                        <td align="center">
                                                                            <!-- Botón ANULAR -->
                                                                            <input class="botonGeneral" name="botonAnular" type="button" id="botonAnular" 
                                                                                    value="&lt;" alt="Anular"
                                                                                    onclick="pulsarAnular();">
                                                                        </td>
                                                                </tr>
                                                                <tr>
                                                                        <td align="center">
                                                                            <!-- Botón ANULAR TODO-->
                                                                            <input class="botonGeneral" name="botonAnularTodo" type="button" id="botonAnularTodo" 
                                                                                        value="&lt;&lt;" alt="Anular Todo"
                                                                                        onclick="anularTodo();">
                                                                        </td>
                                                                </tr>
                                                        </table>
                                                </td>
                                                <td width="42%">
                                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr>
                                                                        <td id="tablaDestino" align="center"></td>
                                                                </tr>
                                                        </table>
                                                </td>
                                        </tr>
                                        <tr>
                                                <td width="42%"></td>
                                                <td width="16%"></td>
                                                <td width="42%" align="center">
                                                        <table width="250" border="1" rules="cols" bordercolor="#B0BDCD" cellspacing="0" cellpadding="0"
                                                                style="border-collapse:collapse;border-top: #B0BDCD 1px solid; border-bottom: #B0BDCD 1px solid;border-left: #B0BDCD 1px solid;border-right: #B0BDCD 1px solid;">
                                                                <tr>
                                                                        <td width="34%"></td>
                                                                        <td width="33%">
                                                                                <input name="numDesde" type="text" class="inputTexto" 
                                                                                        size="2"> -
                                                                                <input name="letraDesde" type="text" class="inputTexto" 
                                                                                        size="2" readonly>
                                                                        </td>
                                                                        <td width="33%">
                                                                                <input name="numHasta" type="text" class="inputTexto" 
                                                                                        size="2"> -
                                                                                <input name="letraHasta" type="text" class="inputTexto" 
                                                                                        size="2" readonly>
                                                                        </td>
                                                                </tr>
                                                        </table>
                                                </td>
                                        </tr>														
                                        <tr>
    <td width="42%" class="etiqueta">
    <table  width="100%" cellpadding="0px" cellspacing="0px">
    <tr>
    <td class="etiqueta">
    <input name="traspasarVial" type="checkbox" id="traspasarVial" value="SI">
    <%=descriptor.getDescripcion("etiqTrasViaComp")%>
    </td>
    </tr>
    <tr>
    <td class="etiqueta">
    <input name="traspasarTodos" type="checkbox" id="traspasarTodos" value="SI">
    <%=descriptor.getDescripcion("etiqTrasSeccComp")%>
    </td>
    </tr>
    <tr>
    <td class="etiqueta">
    <input name="generarOperaciones" type="checkbox" value="SI" checked>
    <%=descriptor.getDescripcion("etiqGenOper")%>
    </td>
    </tr>
    </table>
    </td>
                                                <td width="16%"></td>
                                                <td width="42%" align="center">
                                                    <!-- botón MODIFICAR -->																	<!-- Botón ANULAR -->
                                                    <input class="botonGeneral" name="botonModificarFila" type="button" id="botonModificarFila" value="<%=descriptor.getDescripcion("gbModificar")%>"
                                                            accesskey="F" onClick="pulsarModificarFila();">
                                                </td>
                                        </tr>
                                </table> 
                        </td>
                </tr>
        </table>
<div class="botoneraPrincipal">
    <input name="botonVerDomicilios" type="button" class="botonGeneral" id="botonVerDomicilios"
            accesskey="A" value="<%=descriptor.getDescripcion("etiqVerDom")%>" style="width:100"
            onclick="pulsarVerDomicilios();">
    <input name="botonProcesar" type="button" class="botonGeneral" id="botonProcesar"
            accesskey="A" value="<%=descriptor.getDescripcion("etiqProc")%>"
            onclick="pulsarProcesar();">
    <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
            accesskey="C" value="<%=descriptor.getDescripcion("gbCancelar")%>"
            onclick="pulsarCancelar();">
</div>
</div>
</form>
<script type="text/javascript">
  var tablaOrigen = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaOrigen',250));

  var cabecera = "";
  tablaOrigen.addColumna('77','center','<%=descriptor.getDescripcion("gEtiq_Tramo")%>');
  tablaOrigen.addColumna('77','center','<%=descriptor.getDescripcion("etiqNLetDesd")%>');
  tablaOrigen.addColumna('77','center','<%=descriptor.getDescripcion("etiqNLetHast")%>');
  tablaOrigen.addColumna('0');
  tablaOrigen.displayCabecera = true;
  tablaOrigen.displayTabla();
  
  var tablaDestino = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDestino',250));

  var cabecera = "";
  tablaDestino.addColumna('77','center','<%=descriptor.getDescripcion("gEtiq_Tramo")%>');
  tablaDestino.addColumna('77','center','<%=descriptor.getDescripcion("etiqNLetDesd")%>');
  tablaDestino.addColumna('77','center','<%=descriptor.getDescripcion("etiqNLetHast")%>');
  tablaDestino.addColumna('0');
  tablaDestino.displayCabecera = true;
  tablaDestino.displayTabla();
  
  function refresca(tabla){
    tabla.displayTabla();
  }
  
  document.onmouseup = checkKeys; 
  
  function callFromTableTo(rowID,tableName){
    if(tablaOrigen.id == tableName){
    }
  }

  <%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>

  function checkKeysLocal(evento,tecla){
     var teclaAuxiliar;
    if(window.event){
        evento = window.event;
        teclaAuxiliar  = evento.keyCode;
    }else
        teclaAuxiliar  = evento.which;

    keyDel(evento);
    if (teclaAuxiliar == 38){
      upDownTable(tablaOrigen,listaOrigen,teclaAuxiliar);
      upDownTable(tablaDestino,listaDestino,teclaAuxiliar);
    }
    if (teclaAuxiliar == 40){
      upDownTable(tablaOrigen,listaOrigen,teclaAuxiliar);
      upDownTable(tablaDestino,listaDestino,teclaAuxiliar);
    }
    if (teclaAuxiliar == 13){
      pushEnterTable(tablaOrigen,listaOrigen);
      pushEnterTable(tablaDestino,listaDestino);
    comboTipoNumeracion
     if (teclaAuxiliar == 1){
      if (comboVia.base.style.visibility == "visible" && isClickOutCombo(comboVia,coordx,coordy)) setTimeout('comboVia.ocultar()',20);
      if (comboTipoNumeracion.base.style.visibility == "visible" && isClickOutCombo(comboTipoNumeracion,coordx,coordy)) setTimeout('comboTipoNumeracion.ocultar()',20);
    }
     if (teclaAuxiliar == 9){
      comboVia.ocultar();
      comboTipoNumeracion.ocultar();
    }
  }

  var botonModificar = ['botonModificarFila'];
  var botonVerDomicilios = ['botonVerDomicilios'];
  var camposTDestino = ['numDesde','letraDesde','numHasta','letraHasta'];
  var camposTDestino1 = ['numDesde1','numHasta1'];
  function rellenarDatos(tableName,listName){
    var i = tableName.focusedIndex;
    if(i>=0 && !tableName.ultimoTable){
      document.forms[0].codTramo.value = tableName.lineas[i][0];
    }
    limpiar(camposTDestino);
    limpiar(camposTDestino1);
    if(tablaDestino == tableName){
      if(i>=0 && !tableName.ultimoTable){
        var datos1 = [listaTDestinoOriginal[i][4],listaTDestinoOriginal[i][6]];
        rellenar(datos1,camposTDestino1);
        if(listaTDestino[i][3]!="P"){
          habilitarGeneralInputs(botonAnular,true);
          habilitarGeneralInputs(botonModificar,true);
          habilitarGeneralInputs(botonVerDomicilios,true);
          var datos = [listaTDestinoOriginal[i][4],listaTDestinoOriginal[i][5],
            listaTDestinoOriginal[i][6],listaTDestinoOriginal[i][7]];
          rellenar(datos,camposTDestino);
        }else{
          habilitarGeneralInputs(botonAnular,false);
          habilitarGeneralInputs(botonModificar,false);
          habilitarGeneralInputs(botonVerDomicilios,false);
          
        }
      }else{
        habilitarGeneralInputs(botonAnular,true);
        habilitarGeneralInputs(botonModificar,false);
        habilitarGeneralInputs(botonVerDomicilios,false);
      }
    }else if(tablaOrigen == tableName){
      habilitarGeneralInputs(botonModificar,false);
      habilitarGeneralInputs(botonVerDomicilios,false);
      if(i>=0 && !tableName.ultimoTable){
          habilitarGeneralInputs(botonTraspasar,true);
      }else{
        habilitarGeneralInputs(botonTraspasar,true);
      }
    }
    
  }

  // COMBOS
  var comboVia = new Combo("Via");
  var comboTipoNumeracion = new Combo("TipoNumeracion");
  var auxCombo = "";
  comboVia.change = 
    function() { 
      auxCombo='comboVia'; 
      if(comboVia.cod.value.length!=0){
        var i = comboVia.selectedIndex;
        if(i>=0){
          document.forms[0].codECO.value = codECOESIVias[i][0];
          document.forms[0].codESI.value = codECOESIVias[i][2];
          document.forms[0].codNUC.value = codECOESIVias[i][4];
          document.forms[0].idVia.value = idVias[i];
        }
      }
    }

  function cargarComboBox(cod, des){
    eval(auxCombo+".addItems(cod,des)");
  }
  
  function desHabilitarCombos(qHacer){
  	if (!qHacer) {
		comboVia.activate();
		comboTipoNumeracion.activate();
	} else {
		comboVia.deactivate();
		comboTipoNumeracion.deactivate();
	}
  }
  
  function limpiarCombos(){
		comboVia.selectItem(-1);
		comboTipoNumeracion.selectItem(-1);
	}
</script>
</body>
</html>
