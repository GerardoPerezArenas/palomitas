<!-- JSP de definicion de asunto -->

<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="java.util.Vector" %>
<%@page import="java.util.ArrayList"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<html:html>
    <head>
        <TITLE>Definici�n de Asunto</TITLE>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        
        
        <%
            int idioma = 1;
            int apl = 1;
            String css = "";
            if (session != null && session.getAttribute("usuario") != null) {
                UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    css = usuario.getCss();
                }
            }
        %>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" 
                     type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <style type="text/css">
            /* Override styles from webfxlayout */
            /* Para colocar las pesta�as correctamente */
            
            /** Este estilo es s�lo v�lido para navegador que no sean IE .
                 un margin.left, que FF e IE interpretan de forma distinta.  */
            
        </style>
        
<!-- *********************  FICHEROS JAVASCRIPT **************************    -->

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<html:rewrite page='/scripts/jquery/jquery-1.9.1.min.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<html:rewrite page='/scripts/TablaNueva.js'/>"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><%=css%>" media="screen" >

<script type="text/javascript" src="<html:rewrite page='/scripts/uor.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript">


    // El combo de unidad registro es obligatorio y sus indices se corresponden
    // con los de los vectores. En el caso de unidad tramitadora y procedimiento no
    // son obligatorios y los combos a�aden una posicion vacia al principio por lo
    // que los datos de los vectores comienzan en el indice 1 del combo.
    var cod_interno_unidadRegistro = new Array();
    var cod_unidadRegistro = new Array();
    var desc_unidadRegistro = new Array();
    
    var cod_interno_unidadTram = new Array();
    var cod_vis_unidadTram = new Array();
    var desc_unidadTram = new Array();
    var email_unidadTram = new Array();
    
    var mun_procedimientos = new Array();
    var cod_procedimientos =  new Array();
    var desc_procedimientos = new Array(); 
    
     //Para el combo de clasificacion de asuntos
    var codClasifAsunto=new Array();
    var descClasifAsunto=new Array();
    var codigoClasificacion='<bean:write name="MantAsuntosForm" property="codigoClasificacion"/>';
    
    // Para los documentos codigo=descripcion
    var listaDocs = new Array();

    // Lista de unidades organicas a notificar por correo
    var listaUorsCorreo = new Array();
    
    // Lista de codigos existentes para esta unidad de registro, asi como el codigo del asunto editado
    var listaCodigos = new Array();
    var listaUnidades = new Array();
    var codigoActual = '<bean:write name="MantAsuntosForm" property="codigo"/>';

    // Para saber si estamos dando de alta, modificando o consultando. Es necesaria pq las
    // consultas dirigidas al frame oculto pueden fijar otra opcion en el form.
    var alta = false;
    var consulta = false;
    var opcion = '<bean:write name="MantAsuntosForm" property="opcion"/>';
    if (opcion == 'alta') alta = true;
    if (opcion == 'consultar') consulta = true;
    
    // Para que al cargar el combo de procedimientos sepamos si es que estamos
    // cargando la pagina o tan solo hemos seleccionado unidad tramitadora.
    var cargando = false;

  function Inicio() {
  
    window.focus();
    cargando = true;
   
    // Se cargan la tabla de documentos
    cargaTablaDocs();
    
    // Cargar combos    
    cargarCombos();
    
    // Cargamos la lista de uors a notificar
    cargarUorsCorreo();
    onChangeNotificar();
    
    // #234108
    cargarValorChecks();
    
    
    // Desactivamos todos los elementos si es modo consulta
    if (consulta) modoConsulta();
    // Desactivamos el check de bloqueo procedimiento PAC si la clasificación no es RGI
    var clasifAsunto = document.forms[0].codigoClasificacion.value;
    <%-- var clasifBloqueoPAC = '<c:out value="${requestScope.codClasifRGI}"/>';--%>
    var clasifBloqueoPAC = 1;
    if(clasifAsunto!=clasifBloqueoPAC){
        $('#bloqueoPAC').prop('disabled',true);
    }
    // Recuperamos la aplicacion. Si estamos en alta y la aplicacion es admin desactivamos el combo de clasificaciones
    var apl = '<bean:write name="MantAsuntosForm" property="apl"/>';
    if (alta && apl === 'admin') {
        comboClasifAsunto.buscaCodigo(0);
        comboClasifAsunto.deactivate();
    }
    
    // Cargamos la lista de codigos existentes con sus unidades (para no repetirlos)
    var i=0;
    <logic:iterate id="asunto" name="MantAsuntosForm" property="asuntos">
       listaCodigos[i] = "<str:escape><bean:write name="asunto" property="codigo" filter="false"/></str:escape>";     
       listaUnidades[i] = '<bean:write name="asunto" property="unidadRegistro" filter="false"/>';
       i++;
    </logic:iterate>
            
    // Si accedemos desde el registro, el combo de unidad de registro debe tener el valor de
    // la unidad de registro actual y estar� deshabilitado. A no ser que estemos en modo
    // consulta visualizando un asunto para todos los registros (codigo -1).
    i=0;
    var apl = '<bean:write name="MantAsuntosForm" property="apl"/>';
    var unidadRegistro = '<bean:write name="MantAsuntosForm" property="unidadRegistro"/>';
    if (unidadRegistro == '-1') {
        document.forms[0].codUnidadRegistro.value = "<str:escape><%=descriptor.getDescripcion("etiq_Todos")%></str:escape>";
        document.forms[0].descUnidadRegistro.value = "<str:escape><%=descriptor.getDescripcion("etiq_TodosRegistros")%></str:escape>";
        comboUnidadRegistro.selectItem(0); 
        comboUnidadRegistro.deactivate();
    } else if (apl == 'registro') {
        // Buscamos el indice correspondiente a esta unidad
        i=0;
        var unidadActual = '<bean:write name="MantAsuntosForm" property="unidadRegistroActual"/>'; 
        while ((i<cod_interno_unidadRegistro.length) && (unidadActual != cod_interno_unidadRegistro[i])) i++;
        // Lo seleccionamos y bloqueamos
        if (i == cod_interno_unidadRegistro.length) { // No encontrada
            jsp_alerta('A','Unidad de registro no encontrada');
            comboUnidadRegistro.selectItem(0); 
        } else {
            comboUnidadRegistro.selectItem(i);
        }
        comboUnidadRegistro.deactivate();        
    } else {
        if (unidadRegistro == null || unidadRegistro == 'null' || unidadRegistro == '') { // Caso de un alta
            comboUnidadRegistro.selectItem(0);
        } else {
            i=0;
            while ((i<cod_interno_unidadRegistro.length) && (unidadRegistro != cod_interno_unidadRegistro[i])) i++;        
            if (i == cod_interno_unidadRegistro.length) { // No encontrada
                jsp_alerta('A','Unidad de registro no encontrada');
                comboUnidadRegistro.selectItem(0); 
            } else {
                comboUnidadRegistro.selectItem(i);
            }
        }
    }
    
    // Seleccion de la unidadTramitadora de la lista
    var unidadTram = '<bean:write name="MantAsuntosForm" property="unidadTram"/>';
    if (unidadTram == '-1') {
        comboUnidadTram.selectItem(0);
    } else {
        i=0;
        while ((i<cod_interno_unidadTram.length) && (unidadTram != cod_interno_unidadTram[i])) i++;        
        if (i == cod_interno_unidadTram.length) { // No encontrada
            jsp_alerta('A','Unidad tramitadora no encontrada');
            comboUnidadTram.selectItem(0); 
        } else {
            comboUnidadTram.selectItem(i+1);
        }    
    }
    // El procedimiento se carga mediante 'onChangeComboUnidadTram' al cambiar el combo de unidadTram.

    // Mostramos la primera pesta�a
    tp1.setSelectedIndex(0);
  }

  /* Prepara los datos de los combos y otros para el form y llama a a la accion para grabar*/
  function pulsarGrabar() {
    
    if (validarObligatorios()) {
      if (existeCodigoAsunto(document.forms[0].codigo.value, 
          cod_interno_unidadRegistro[comboUnidadRegistro.selectedIndex])) {
          jsp_alerta('A',"<str:escape><%=descriptor.getDescripcion("msjCodigoExiste")%></str:escape>");
      } else {
	    // Unidad de registro
	    document.forms[0].unidadRegistro.value = 
	        cod_interno_unidadRegistro[comboUnidadRegistro.selectedIndex];
	        
	    // Unidad tramitadora        
                      document.forms[0].unidadTram.value = recuperaUorCod(document.forms[0].codUnidadTram.value,
                                document.forms[0].descUnidadTram.value);
                                
	    // Procedimiento (como el codigo de proc es alfanumerico, usamos '' para indicar cualquiera)	  
                      document.forms[0].munProc.value =  recuperaMunPro(document.forms[0].codProcedimiento.value);
                      
               //Clasificacion asuntos        
	    if (comboClasifAsunto.selectedIndex > -1) {
	        document.forms[0].codigoClasificacion.value =
	            codClasifAsunto[comboClasifAsunto.selectedIndex];
                
               
	    } else {
               
	        document.forms[0].codigoClasificacion.value = '-1';
	    }
            
	    // Lista de documentos
	    txtListaDocs = "";
	    for (i=0; i<listaDocs.length; i++) {
	        txtListaDocs += listaDocs[i][0] + '��';
	    } 
	    document.forms[0].txtListaDocs.value = txtListaDocs;

            // Construimos string con las uors a notificar por correo
            txtListaUors='';
            for(i=0; i<listaUorsCorreo.length; i++) {
                txtListaUors += listaUorsCorreo[i];
                if (i<listaUorsCorreo.length - 1) txtListaUors += ',';
            }
             document.forms[0].txtListaUorsCorreo.value = txtListaUors;	
            if($('#bloquearDestino').is(':checked')){
                document.forms[0].bloquearDestino.value=true;	
            }
			if($('#bloquearProcedimiento').is(':checked')){
                document.forms[0].bloquearProcedimiento.value=true;	
            }
            if($('#bloqueoPAC').is(':checked')){
                console.log("Dentro del if $('#bloqueoPAC').is(':checked') = " + $('#bloqueoPAC').is(':checked'));
                $('#bloqueoPAC').prop('disabled',false); // Esta línea es para que funcione correctamente
                // cuando el check está deshabilitado, de lo contrario sólo funcionaría bien cuando está habilitado.
                document.forms[0].bloqueoPAC.value=true;
            }
	    
	    // Opcion (segun sea alta o modificar)
	    if (alta) {
	        document.forms[0].opcion.value = 'grabar_alta';
	    } else {
	        document.forms[0].opcion.value = 'grabar_modificar';
	    }
	    document.forms[0].target="mainFrame";
	    document.forms[0].submit();
       }
    } 
  }
  
  /* Abre un dialogo para introducir los datos de un nuevo documento y lo a�ade a la lista */
  function pulsarAltaDoc() {
    var source = "<html:rewrite page='/jsp/registro/mantenimiento/datosRolDoc.jsp?dummy='/>";
    abrirXanelaAuxiliar("<html:rewrite page='/jsp/registro/mainVentana.jsp?source='/>" + source,null,
	'width=900,height=370',function(doc){
                        if(doc!=undefined && doc!=''){
                            if (existeDoc(doc)) {
                                jsp_alerta('A',"<str:escape><%=descriptor.getDescripcion("msjDocExiste")%></str:escape>");
                            } else {
                                listaDocs[listaDocs.length] = [doc];
                                tabDocs.lineas=listaDocs;
                                tabDocs.displayTabla();
                            }
                        }
                    });
  }
  
  /* Elimina el documento seleccionado */
  function pulsarEliminarDoc() {
      if (tabDocs.selectedIndex == -1) {
          jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
      } else {
          var eliminado = tabDocs.selectedIndex;
          listaAuxDoc = new Array();          
          var j=0;
          for(i=0; i<listaDocs.length; i++) {
              if (i != eliminado) {                  
                  listaAuxDoc[j++] = listaDocs[i];
              }
          }
          listaDocs = listaAuxDoc;        
          tabDocs.lineas=listaDocs;
          tabDocs.displayTabla();
     }
  }

  /* Provoca el retorno a la ventana del listado de asuntos */
  function pulsarSalir() {    
    if (consulta || jsp_alerta('','<%=descriptor.getDescripcion("msjCabLanzarProc")%>')) {
      document.forms[0].opcion.value = 'cargar';
      document.forms[0].target="mainFrame";
      document.forms[0].submit();    
    }
  }
  
   /* Abre el dialogo para editar la lista de unidades organicas que se quieran notificar */
   function pulsarNotificar(){      
       
       var args = new Array();
       var codsvi = new Array();
       var cods = new Array();
       var uord = new Array();
       var uorviDest=document.forms[0].codUnidadTram.value;
       
       //Sacamos del listado de unidades notificables la que pertenece a la uor 
       //de destino y s�lo a�adimos las que tengan correo.
       var j=0;
       for(i=0;i<cod_vis_unidadTram.length;i++){
           if(cod_vis_unidadTram[i]!=uorviDest && email_unidadTram[i]!=null && email_unidadTram[i]!=''){
               codsvi[j] = cod_vis_unidadTram[i]; 
               cods[j] = cod_interno_unidadTram[i];
               uord[j] = desc_unidadTram[i];
               j++;
           }
       }
       
       args[0] = codsvi;
       args[1] = cods;
       args[2] = uord;
       args[3] = listaUorsCorreo;
       args[4] = consulta;   // Indica si estamos en modo consulta
       
       var source = "<html:rewrite page='/jsp/registro/entrada/listaUorsCorreo.jsp?dummy='/>";
       listaAntigua = listaUorsCorreo;
       abrirXanelaAuxiliar("<html:rewrite page='/jsp/registro/mainVentana.jsp?source='/>" + source,args,
	'width=535,height=350',function(listaUorsCorreo){
                        // Para el caso de que el usuario cierre la ventana con la X y se pierda la lista                                                
                        if (listaUorsCorreo == null || listaUorsCorreo == undefined) listaUorsCorreo = listaAntigua;
                    });
    }
  
  
  /* Comprueba si existe el nombre de documento en la lista de documentos */
  function existeDoc(doc) {
  
      for (i=0; i<listaDocs.length; i++) {
          if (listaDocs[i] == doc) return true;
      }
      return false;
  }

  /* Comprueba si existe el codigo de asunto en la lista */
  function existeCodigoAsunto(cod, unidad) {
      
      // Si no cambia -> estamos modificando -> se puede usar
      if (cod == codigoActual) return false;
      for(i=0; i<listaCodigos.length; i++) {
          if (listaCodigos[i] == cod) 
              if (unidad == listaUnidades[i] || '-1' == listaUnidades[i] || unidad == '-1')
                  return true;
      }
      return false;
  }
 
  /* Desactiva los botones de documentos */
  function desactivarDocs() {
      var vectorBotones = [document.forms[0].cmdAltaDoc, document.forms[0].cmdEliminarDoc];
      deshabilitarGeneral(vectorBotones);
  }
  
  /* Activa los botones de documentos */
  function activarDocs() {
      var vectorBotones = [document.forms[0].cmdAltaDoc, document.forms[0].cmdEliminarDoc];
      habilitarGeneral(vectorBotones);
  }
  
  /* Desactiva todo lo que permita editar el asunto */
  function modoConsulta() {
      // Combos y campos de texto
      comboUnidadRegistro.deactivate();
      comboUnidadTram.deactivate();
      comboProcedimiento.deactivate();  
      comboClasifAsunto.deactivate();
      document.forms[0].codigo.readOnly = true;
      document.forms[0].descripcion.readOnly = true;
      document.forms[0].extracto.readOnly = true;
      // #234108
      $('#tipoDocObligatorio').prop('disabled',true);
      $('#enviarCorreo').prop('disabled',true);
      $('#bloquearDestino').prop('disabled',true);
      $('#bloquearProcedimiento').prop('disabled',true);
      $('#bloqueoPAC').prop('disabled',true);

      // Tipo de registro
      for (var i=0; i<document.forms[0].elements.length; i++) {
         if (document.forms[0].elements[i].name == 'tipoRegistro') {
            document.forms[0].elements[i].disabled = true;
         }
      }
      
      // Notificaciones
      document.getElementById('checkBoxNotificar').disabled = true;
      if (listaUorsCorreo.length == 0)  {
          deshabilitarGeneral(new Array(document.forms[0].cmdNotificar));
      }
      
      // Botones
      desactivarDocs();
      deshabilitarGeneral(new Array(document.forms[0].cmdGrabar));
  }
   
   function activarNotificacion() {
       
       document.getElementById('checkBoxNotificar').className = 'etiqueta';
       document.forms[0].enviarCorreo.disabled = false;
   }
   
   function desactivarNotificacion() {
       
       document.getElementById('checkBoxNotificar').className = 'etiquetaDeshabilitada';
       document.forms[0].enviarCorreo.disabled = true;
       document.forms[0].enviarCorreo.checked = false;
       deshabilitarGeneral(new Array(document.forms[0].cmdNotificar));
   }
  
  /* Se llama al seleccionar/deseleccionar un procedimiento para ocultar/mostrar la pesta�a de otros datos */
  function onChangeComboProcedimiento() {
    // En caso de estar cargando ya han sido cargados los documentos
    if (!cargando) {
	    if (comboProcedimiento.selectedIndex > 0) {
	        // Se deshabilita la edicion de documentos, se cargan los del procedimiento.
	        //tp1.hideTabPage(1);             
	        desactivarDocs();
                 
                document.forms[0].munProc.value = recuperaMunPro(document.forms[0].codProcedimiento.value);
	        document.forms[0].opcion.value = "cargarProcedimiento";
	        document.forms[0].target="oculto";
	        document.forms[0].submit();
	    } else {
	        activarDocs();
	        
	        listaDocs = new Array();             
	        tabDocs.lineas=listaDocs;
	        tabDocs.displayTabla();
	    }
	} else {
	   // Siempre sucede este evento de ultimo al cargar la pagina (si sucede)
	   cargando = false;
	}
  }
  
/* Se llama al seleccionar una unidad tramitadora para cargar los procedimientos de esa unidad */
function onChangeComboUnidadTram() {
    var utr = recuperaUorCod(document.forms[0].codUnidadTram.value,
                                document.forms[0].descUnidadTram.value);
  
    // Hay que comprobar si la unidad tiene email
    var email = '';
    if (utr > 0) {
        email = recuperaUorMail(document.forms[0].codUnidadTram.value,
                                document.forms[0].descUnidadTram.value);
    }
    
    if (utr > 0 && email!=null && email!='') {
        activarNotificacion();
        
        // Actualizamos la lista de unidades a notificar (si esta activada esta opci�n).
        if (document.forms[0].enviarCorreo.checked == true) {
            // Hay que sacar la nueva unidad seleccionada de la lista de unidades a notificar.
            var cod_uor = recuperaUorCod(document.forms[0].codUnidadTram.value,document.forms[0].descUnidadTram.value);
            var nuevaListaCorreos = new Array();
            var j=0;
            for(i=0; i<listaUorsCorreo.length; i++){
                if(listaUorsCorreo[i] != cod_uor){
                    nuevaListaCorreos[j] = listaUorsCorreo[i];                
                    j++;
                }
            }
            listaUorsCorreo = nuevaListaCorreos;
        }
     } else {
        listaUorsCorreo = new Array();
        desactivarNotificacion();
    }
    document.forms[0].unidadTram.value = recuperaUorCod(document.forms[0].codUnidadTram.value,document.forms[0].descUnidadTram.value);
    document.forms[0].opcion.value = "listaProcedimientos";
    document.forms[0].target="oculto";
    document.forms[0].submit();
}
  
   /* Activa o desactiva el boton 'Notificar a' seg�n este marcado el checkbox o no */
   function onChangeNotificar() {
       var vector = new Array(document.forms[0].cmdNotificar);
       if (document.forms[0].enviarCorreo.checked == true)
           habilitarGeneral(vector);
      else{        
         deshabilitarGeneral(vector);
   }
   }


  /* Carga el combo de procedimientos */
  function recuperaListaProcedimientos(mun_procs, cod_procs, desc_procs) {
     var proc = ''
    // En caso de estar cargando la pagina, seleccionamos el proc q viene en el form.
    if (cargando)     
        proc = '<bean:write name="MantAsuntosForm" property="codProcedimiento"/>';
    else
        proc = document.forms[0].codProcedimiento.value;

    mun_procedimientos = mun_procs;
    cod_procedimientos = cod_procs;
    desc_procedimientos = desc_procs;
    
    comboProcedimiento.clearItems();
    comboProcedimiento.addItems(cod_procs,desc_procs);
    
    if (proc == 'null' || proc == '') {
        document.forms[0].codProcedimiento.value = '';
        comboProcedimiento.selectItem(0);
    } else {
        var i=0;
        for (i=0;i<cod_procs.length;i++) {
            if (proc == cod_procs[i]){
                comboProcedimiento.selectItem(i+1);
                break;
            }
        }
        if (i >= (cod_procs.length)) { // No encontrado
            comboProcedimiento.selectItem(0); 
        } 
        desactivarDocs();
    }
  }
  
  /* Recarga la lista de documentos */
  function recuperaListaDocumentos(_listaDocs) {
      listaDocs = _listaDocs;

      tabDocs.lineas=listaDocs;
      tabDocs.displayTabla();
  }
  
  // #234108: Seg�n se ha marcado o no el check da un valor al input
    function obtenerValorCheck(id){
        if($('#'+id).is(':checked'))
            $('#'+id).val('1');
        else
            $('#'+id).val('0');
    }
    
    // #234108: Precarga en el check el valor obtenido en bbdd
    function cargarValorChecks(){
        var esOblig = '<bean:write name="MantAsuntosForm" property="tipoDocObligatorio"/>';
        if(esOblig=="1"){
            $('#tipoDocObligatorio').prop('checked', true);
        } else {
            $('#tipoDocObligatorio').prop('checked', false);
        }
        $('#tipoDocObligatorio').val(esOblig);
    }
  
</SCRIPT>
        
    </head>
    
    <BODY class="bandaBody" onload="javascript:{ pleaseWait('off'); 
       }">
        
        <!-- Mensaje de esperar mientras carga  -->

        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        <!-- Fin mensaje de esperar mientras carga  -->

<html:form action="MantAsuntos" method="post">

<html:hidden property="opcion"/>
<html:hidden property="apl"/>
<html:hidden property="txtListaDocs"/>
<html:hidden property="txtListaUorsCorreo"/>

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titDefAsu")%></div>                         
    <div class="contenidoPantalla">

        <div class="tab-pane" id="tab-pane-1" >

            <script type="text/javascript">
                tp1 = new WebFXTabPane( document.getElementById( "tab-pane-1" ) );
            </script>


            <!-- PESTA�A 1: DATOS GENERALES -->
            <div class="tab-page" id="tabPage1">
                <h2 class="tab"><%=descriptor.getDescripcion("res_pestana1")%></h2>
                <script type="text/javascript">tp1_p1 = tp1.addTabPage( document.getElementById( "tabPage1" ) );</script>

                <TABLE id ="tablaDatosGral"cellspacing="4px" cellpadding="1px" border="0" style="width:100%">

                    <!-- Unidad Organica Registro -->
                    <TR>
                        <TD style="width:16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidReg")%>:</TD>
                        <TD style="width:84%" valign="top">
                            <html:hidden property="unidadRegistro"/>
                            <html:text styleClass="inputTextoObligatorio" property="codUnidadRegistro" size="9" maxlength="8" 
                                       onkeyup="return xAMayusculas(this);"/>
                            <html:text styleClass="inputTextoObligatorio" property="descUnidadRegistro"  style="width:550;height:17" readonly="true"/>
                            <A href="" style="text-decoration:none;" id="anchorUnidadRegistro" name="anchorUnidadRegistro">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonUnidadRegistro" name="botonUnidadRegistro" style="cursor:hand;" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                            </A>
                        </TD>
                    </TR>

                    <!-- C�digo del asunto -->

                    <TR>
                        <TD style="width:16%" class="etiqueta"><%=descriptor.getDescripcion("etiq_codAsunto")%>:</TD> 
                        <TD style="width:84%">
                            <table>
                                <tr>
                                    <td class="etiqueta" style='width: 23%'>
                                        <html:text styleClass="inputTextoObligatorio" size="12" maxlength="10" property="codigo" readonly="false" tabindex="-1" onkeyup="return xAMayusculas(this);"/>
                                    </td>
                                    <td class="etiqueta">
                                        <html:checkbox property="tipoDocObligatorio" styleId="tipoDocObligatorio" onclick="javascript:obtenerValorCheck('tipoDocObligatorio')" />&nbsp;&nbsp;<%=descriptor.getDescripcion("etiq_tipoDocObligatorio")%>
                                    </td>
                                </tr>
                            </table>
                        </TD>
                    </TR>

                    <!-- Tipo del asunto -->
                    <TR>
                        <TD style="width:16%" class="etiqueta"><%=descriptor.getDescripcion("etiqTipoRegistro")%>:</TD>
                        <TD style="width:84%">
                            <table>
                                <tr>
                                    <td class="etiqueta">
                                        <html:radio property="tipoRegistro" value="E"><%=descriptor.getDescripcion("etiq_DatosEntrada")%></html:radio>              
                                    </td>
                                    <td class="etiqueta">&nbsp;&nbsp;&nbsp;
                                        <html:radio property="tipoRegistro" value="S"><%=descriptor.getDescripcion("etiq_DatosSalida")%></html:radio>
                                    </td>
                                    <td class="etiqueta">&nbsp;&nbsp;&nbsp;
                                        <html:radio property="tipoRegistro" value="A"><%=descriptor.getDescripcion("gbAmbos")%></html:radio>
                                    </td>
                                </tr>
                            </table>
                        </TD>
                    </TR>

                    <!-- Descripci�n -->
                    <TR>
                        <TD style="width:16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_desc")%>:</TD>
                        <TD style="width:84%">
                            <html:text styleClass="inputTextoObligatorio" size="99" maxlength="99" property="descripcion" readonly="false" tabindex="-1" onblur="return xAMayusculas(this);"/>
                        </TD>
                    </TR>

                    <!-- Extracto -->

                    <TR>
                        <TD style="width:16%" class="etiqueta"><%=descriptor.getDescripcion("etiq_Extracto")%>:</TD>
                        <TD style="width:84%" class="columnP">
                            <html:textarea styleClass="textareaTexto" property="extracto" cols="98" rows="5" maxlength="250" 
                                           onkeydown="return textCounter(this,254);" onblur="return xAMayusculas(this);"></html:textarea>
                        </TD>
                    </TR>

                    <!-- Unidad Organica Tramitadora -->

                    <TR>
                        <TD style="width:16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqUnidTram")%>:</TD>
                        <TD style="width:84%" valign="top">
                            <html:hidden property="unidadTram"/>
                            <html:text styleClass="inputTexto" property="codUnidadTram" size="9" maxlength="8"/>
                            <html:text styleClass="inputTexto"  property="descUnidadTram" style="width:550;height:17" readonly="true"/>
                            <A href="javascript:{}" style="text-decoration:none;" id="anchorUnidadTram" name="anchorUnidadTram">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonUnidadTram" style="cursor:hand;" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                            </A>
                        </TD>
                    </TR>

                    <!-- Envio de correos a unidades organicas -->
                    <TR>
                        <TD style="width:16%" class="etiqueta">&nbsp;</TD>
                        <TD style="width:84%" class="columnP" valign="top">
                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td id="checkBoxNotificar" class="etiquetaDeshabilitada" style="width:70%">   
                                        <html:checkbox disabled="true" property="enviarCorreo" value="true" onclick="javascript:{onChangeNotificar();}"><%=descriptor.getDescripcion("etiqEnviarNotif")%></html:checkbox>
                                    </td>
                                    <!-- Bot�n NOTIFICAR A -->
                                    <td style="width: 30%">
                                     <table cellspacing="0px" cellpadding="0px" border="0" align="center">
                                      <tr>
                                       <td align="right" width="100px" height="22px" bgcolor="#ffffff" >
                                        <input type="button" title="<%=descriptor.getDescripcion("altNotificar")%>" alt="<%=descriptor.getDescripcion("altNotificar")%>"
                                               class="botonLargo" value="<%=descriptor.getDescripcion("gbNotificar")%>"
                                               name="cmdNotificar" onClick="pulsarNotificar();return false;"/>
                                       </td>
                                      </tr>
                                     </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <!-- Fin Bot�n NOTIFICAR A -->
                    </tr>

                     <!-- Bloquear Destino -->	
                     <TR>	
                        <TD style="width:16%" class="etiqueta">&nbsp;</TD>	
                        <TD style="width:84%" class="columnP" valign="top">	
                            <table border="0" cellspacing="0" cellpadding="0">	
                                <tr>	
                                    <td id="checkBoxBloquear" class="etiqueta" style="width:70%">   	
                                        <html:checkbox  property="bloquearDestino" styleId="bloquearDestino"><%=descriptor.getDescripcion("etiqBloquearDestino")%></html:checkbox>
                                    </td>	
                                </tr>	
                            </table>	
                        </td>	
                    </tr>
					
					<TR>	
                        <TD style="width:16%" class="etiqueta">&nbsp;</TD>	
                        <TD style="width:84%" class="columnP" valign="top">	
                            <table border="0" cellspacing="0" cellpadding="0">	
                                <tr>	
                                    <td id="checkBoxProcedimiento" class="etiqueta" style="width:70%">   	
                                        <html:checkbox  property="bloquearProcedimiento" styleId="bloquearProcedimiento">Bloquear Procedimiento</html:checkbox>
                                    </td>	
                                </tr>	
                            </table>	
                        </td>	
                    </tr>

                    <TR>
                        <TD style="width:16%" class="etiqueta">&nbsp;</TD>
                        <TD style="width:84%" class="columnP" valign="top">
                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td id="checkBoxBloqueoPAC" class="etiqueta" style="width:70%">
                                        <html:checkbox  property="bloqueoPAC" styleId="bloqueoPAC">Bloqueo procedimiento PAC</html:checkbox>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
					
                    <!-- Procedimiento -->

                    <TR>
                        <TD style="width:16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqProc")%>:</TD>
                        <TD style="width:84%" valign="top">
                            <html:hidden property="munProc"/>            
                            <html:text styleClass="inputTexto" property="codProcedimiento" size="9" maxlength="5" 
                                       onkeyup="return xAMayusculas(this);"/>
                            <html:text styleClass="inputTexto" property="descProcedimiento" style="width:550;height:17" readonly="true"/>
                            <A href="javascript:{onClickDescProcedimiento();}" style="text-decoration:none;" id="anclaD" name="anchorProcedimiento">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonProcedimiento" style="cursor:hand;" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                            </A>
                        </TD>
                    </TR>


                     <!-- clasificacion asunto -->
                     <TR>
                     <BR>
                     <BR>
                     </TR>
                      <TR>
                        <TD style="width:16%" class="etiqueta"><%=descriptor.getDescripcion("gEtiqClasificacion")%>:</TD>
                        <TD style="width:84%" valign="top">
                            <html:hidden property="codigoClasificacion"/>
                            <html:text styleClass="inputTextoObligatorio" property="codClasifAsunto" size="9" maxlength="6" 
                                       onkeyup="return xAMayusculas(this);"/>
                            <html:text styleClass="inputTextoObligatorio" property="descClasifAsunto"  style="width:550;height:17" readonly="true"/>
                            <A href="" style="text-decoration:none;" id="anchorClasifAsunto" name="anchorClasifAsunto">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="botonClasifAsunto" name="botonClasifAsunto" style="cursor:hand;" alt="<%=descriptor.getDescripcion("altDesplegable")%>" title="<%=descriptor.getDescripcion("altDesplegable")%>"></span>
                            </A>
                        </TD>
                    </TR>

                </TABLE>
            </div>
            <!-- FIN DE PESTA�A 1: DATOS GENERALES -->

            <!-- PESTA�A 2: OTROS DATOS -->
            <div class="tab-page" id="tabPage2" style="height:415px">
                <h2 class="tab"><%=descriptor.getDescripcion("titOtrosDatos")%></h2>
                <script type="text/javascript">tp1_p2 = tp1.addTabPage( document.getElementById( "tabPage2" ) );</script>

                <TABLE id ="tablaDatosGral" cellspacing="4px" cellpadding="1px" border="0">

                    <TR>
                        <TD class="sub3titulo" colspan="2" >&nbsp;&nbsp;&nbsp;<%=descriptor.getDescripcion("gbDocumentos")%> </TD>
                    </TR>

                    <TR>
                        <td>   
                            <div id="tablaDocs"></div>          
                        </td> 
                        <td> 
                            <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                <tr>
                                    <td>
                                        <input type= "button" alt="<%=descriptor.getDescripcion("toolTip_AltaTitDoc")%>" title="<%=descriptor.getDescripcion("toolTip_AltaTitDoc")%>" class="botonGeneral" value='<%=descriptor.getDescripcion("gbAlta")%>' name="cmdAltaDoc" onClick="pulsarAltaDoc();">

                                    </td> 
                                </tr>
                                <tr>
                                    <TD style="height:15px"></TD>
                                </tr>
                                <tr>
                                    <td>
                                        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbEliminar")%> name="cmdEliminarDoc" onClick="pulsarEliminarDoc();" alt="<%=descriptor.getDescripcion("toolTip_Eliminar")%>" title="<%=descriptor.getDescripcion("toolTip_Eliminar")%>">

                                    </td>
                                </tr>
                            </table>

                        </td>


                    </TR>
                </TABLE>
            </div>
            <!-- FIN DE PESTA�A 2: OTROS DATOS -->
      </div>
<!-- ------------------------------------------------------------------ -->
<!--               FIN PESTA�AS                -->
<!-- ------------------------------------------------------------------ -->
    <div class="botoneraPrincipal">
        <input type= "button" title='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' alt='<%=descriptor.getDescripcion("toolTip_bGrabar")%>' class="botonGeneral" value='<%=descriptor.getDescripcion("gbGrabar")%>' name="cmdGrabar" onClick="pulsarGrabar();">  
        <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbSalir")%> name="cmdSalir" onClick="pulsarSalir();" title='<%=descriptor.getDescripcion("toolTip_bVolver")%>' alt='<%=descriptor.getDescripcion("toolTip_bVolver")%>'>
    </div>
</div>

</html:form>

<script type="text/javascript" src="<html:rewrite page='/scripts/listaComboBox.js'/>"></script>

<script type="text/javascript" language="JavaScript1.2">

// Foco por defecto en cada pesta�a
tp1_p1.setPrimerElementoFoco(document.forms[0].codigo);
tp1_p2.setPrimerElementoFoco(document.forms[0].cmdEliminar);

/////// Combos
var comboUnidadRegistro = new Combo("UnidadRegistro");
var comboUnidadTram = new Combo("UnidadTram");
var comboProcedimiento = new Combo("Procedimiento");
var comboClasifAsunto= new Combo("ClasifAsunto");

function cargarCombos() {

	/////// Valores para el combo de Unidad de Registro	
	var cont=0;
	// Si no es la aplicaci�n de registro, a�adimos la opci�n 'PARA TODOS LOS REGISTROS'
	var apl = '<bean:write name="MantAsuntosForm" property="apl"/>';
	if (apl == 'admin') {
	    cod_interno_unidadRegistro[cont] = '-1';
	    cod_unidadRegistro[cont] = "<str:escape><%=descriptor.getDescripcion("etiq_Todos")%></str:escape>";
	    desc_unidadRegistro[cont] = "<str:escape><%=descriptor.getDescripcion("etiq_TodosRegistros")%></str:escape>";
	    cont++;
	}
	// A�adimos el resto de UORs     
	<logic:iterate id="uor" name="MantAsuntosForm" property="uorsDeRegistro">
	  cod_interno_unidadRegistro[cont]='<bean:write name="uor" property="uor_cod"/>';
	  cod_unidadRegistro[cont]        ='<bean:write name="uor" property="uor_cod_vis"/>';
	  desc_unidadRegistro[cont]       ="<str:escape><bean:write name="uor" property="uor_nom" filter="false"/></str:escape>";
	  cont++;
	</logic:iterate>
        comboUnidadRegistro.addItems(cod_unidadRegistro,desc_unidadRegistro);
    
    
	/////// Valores para el combo de Unidad Tramitadora
	cont=0;     
	<logic:iterate id="uor" name="MantAsuntosForm" property="uors">
	  cod_interno_unidadTram[cont]='<bean:write name="uor" property="uor_cod"/>';
	  cod_vis_unidadTram[cont]    ='<bean:write name="uor" property="uor_cod_vis"/>';
	  desc_unidadTram[cont]       ="<str:escape><bean:write name="uor" property="uor_nom" filter="false"/></str:escape>";
          email_unidadTram[cont]      ="<str:escape><bean:write name="uor" property="uor_email" filter="false"/></str:escape>";
	  cont++;
	</logic:iterate>    
    comboUnidadTram.addItems(cod_vis_unidadTram, desc_unidadTram);


        /////// Valores para el combo de Clasificacion de asuntos
        cont=0;    
	<logic:iterate 	id="clasi" name="MantAsuntosForm" property="clasificaciones">
	  codClasifAsunto[cont]        ='<bean:write name="clasi" property="codigo"/>';
	  descClasifAsunto[cont]       ='<bean:write name="clasi" property="descripcion"/>';
	  cont++;
	</logic:iterate>
        comboClasifAsunto.addItems(codClasifAsunto, descClasifAsunto);
         //Si estamos en modificar deberiamos fijar el elemento
       
         if(codigoClasificacion>-1){
           comboClasifAsunto.buscaCodigo(codigoClasificacion);
         }

    let clasifBloqueoPAC = 1;
   function onChangeComboClasifAsunto() {
        console.log("onChangeComboClasifAsunto = " + codClasifAsunto[comboClasifAsunto.selectedIndex] + ", " + clasifBloqueoPAC);

        if (codClasifAsunto[comboClasifAsunto.selectedIndex] == clasifBloqueoPAC) {
                $('#bloqueoPAC').prop('disabled',false);
        }
        else {
                $('#bloqueoPAC').prop('disabled',true);
        }
   }
    
    // Se asigna una funcion definida arriba al evento de cambio del combo
    // para que recargue el combo de procedimientos
    comboUnidadTram.change = onChangeComboUnidadTram;
    comboProcedimiento.change = onChangeComboProcedimiento;
    comboClasifAsunto.change = onChangeComboClasifAsunto;
}

// Tabla documentos
tabDocs = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaDocs'));
tabDocs.addColumna('675','center',"<str:escape><%=descriptor.getDescripcion("gEtiq_titDoc")%></str:escape>");
tabDocs.displayCabecera=true;
tabDocs.displayTabla();

function recuperaUorCod (codVis, nom) {
    for (var i = 0; i < cod_interno_unidadTram.length; i++) {
        if (cod_vis_unidadTram[i] == codVis && desc_unidadTram[i] == nom) 
            return cod_interno_unidadTram[i];
    }
    return '-1';
}
function recuperaUorMail (codVis, nom) {
    for (var i = 0; i < cod_interno_unidadTram.length; i++) {
        if (cod_vis_unidadTram[i] == codVis && desc_unidadTram[i] == nom) 
            return email_unidadTram[i];
    }
    return '-1';
}

function recuperaMunPro (codPro) {
    for (var i = 0; i < cod_procedimientos.length; i++) {
        if (cod_procedimientos[i] == codPro) 
            return mun_procedimientos[i];
    }
    return '0';
}

function cargaTablaDocs()
{ 
  // Para los documentos descripcion = codigo 
  var i=0;
  <logic:iterate id="doc" name="MantAsuntosForm" property="listaDocs">
     listaDocs[i] = ["<str:escape><bean:write name="doc" property="descripcion" filter="false"/></str:escape>"];         
     i++;
  </logic:iterate>
    
  tabDocs.lineas=listaDocs;
  tabDocs.displayTabla();
}

// Lista de uors a notificar por correo
function cargarUorsCorreo() {
  var i=0;
  <logic:iterate id="uor" name="MantAsuntosForm" property="listaUorsCorreo">
     listaUorsCorreo[i] = <bean:write name="uor"/>;
     i++;
  </logic:iterate>
}

<%String userAgent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(userAgent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>



document.onmouseup = checkKeys;

    function checkKeysLocal(evento,tecla) {
        var teclaAuxiliar = "";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else{
            teclaAuxiliar = evento.which;
        }

        if (teclaAuxiliar == 9){
          
        }
        if (teclaAuxiliar == 1){
            if (comboUnidadTram.base.style.visibility == "visible" && isClickOutCombo(comboUnidadTram,coordx,coordy)) setTimeout('comboUnidadTram.ocultar()',20);
            if (comboProcedimiento.base.style.visibility == "visible" && isClickOutCombo(comboProcedimiento,coordx,coordy)) setTimeout('comboProcedimiento.ocultar()',20);
          

        }

        if(evento.button == 1){
              if (comboUnidadTram.base.style.visibility == "visible" && isClickOutCombo(comboUnidadTram,coordx,coordy)) setTimeout('comboUnidadTram.ocultar()',20);
            if (comboProcedimiento.base.style.visibility == "visible" && isClickOutCombo(comboProcedimiento,coordx,coordy)) setTimeout('comboProcedimiento.ocultar()',20);
          
        }
    }

</script>
<script> Inicio(); </script>
</BODY>
    
</html:html>
