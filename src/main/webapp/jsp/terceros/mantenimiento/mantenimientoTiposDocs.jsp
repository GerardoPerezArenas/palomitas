<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.common.service.config.Config"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title> Mantenimiento de Tipos de Identificadores de Documentos </title>
        <jsp:include page="/jsp/terceros/tpls/app-constants.jsp" />
     
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            ParametrosTerceroValueObject ptVO = null;
            int idioma = 1;
            int apl = 3;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                css=usuarioVO.getCss();
            }
            Config m_Conf = ConfigServiceHelper.getConfig("common");
            String accede = m_Conf.getString("aytos.registro");
        %>
        
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <!-- Ficheros JavaScript -->
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
           <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript">
            
            /**************  VARIABLES GLOBALES ***********************/
                 <%
            MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) session.getAttribute("MantenimientosTercerosForm");
    %>  
        var ventana = "<%=mantForm.getVentana()%>";
        //Vectores para albergar los tipos de dígitos de los identificadores (estáticos) 
        var tipos = ["N","X","A"];
        //Vectores para albergar los datos de la lista de los Tipos de Identificadores de Documentos, tabla (Dinámicos)
        var listaTipoDocs = new Array();
        //Variable para direccionamiento al frame
        var frame;
        //Vectores para el Almacenado de Objetos y datos
        var vectorCamposRejilla = ["codTipoDoc","codigoINE","codigoAccede","duplicado","persFJ","normalizado","descTipoDoc",
            "grupo1","tipo1","grupo2","tipo2","grupo3","tipo3","grupo4","tipo4","grupo5","tipo5",
            "longitudMaxima","validacion"];
        //Vector de Valores de Limpieza para pasarlo a la función rellenar, es un vector donde cada posicion es un
        //vector que contiene: En la posicion 0 el valor que se asignará al campo en la función de limpieza, si el
        //campo es un checkbox, además, contendrá las posiciones 2 y 3 que contendrá los valores que se asignarán 
        //cuando no esté chequeado (pos 1) ó cuando lo esté (pos 2)
        var vectorValoresResetRejilla = [[""],[""],[""],["0","0","1"],["0","0","1"],["0","0","1"],
            [""],[""],[""],[""],[""],[""],[""],[""],[""],[""],[""],[""],[""]];															 
        var vectorBotones = new Array();
        var vectorCamposRejilla1 = new Array();
        var vectorAnchorsRejilla = new Array();
        var vectorBotonesRejilla = new Array();
        
        /**************  FUNCIONES DE CARGA E INICIALIZACION DE DATOS ***********************/
        function recuperaDatosIniciales()
        {
    <%
            Vector listaTipoDocs = mantForm.getListaTipoDocs();
            int i = 0;
    %>
        var j=0;
    <%for (i = 0; i < listaTipoDocs.size(); i++) {
                GeneralValueObject tipoDoc = (GeneralValueObject) listaTipoDocs.elementAt(i);
                         %>
                             listaTipoDocs[j] = ["<%=(String) tipoDoc.getAtributo("codTipoDoc")%>",
                                 "<%=(String) tipoDoc.getAtributo("codigoINE")%>",
                                 "<%=(String) tipoDoc.getAtributo("codigoAccede")%>",
                                 "<%=(String) tipoDoc.getAtributo("duplicado")%>",
                                 "<%=(String) tipoDoc.getAtributo("persFJ")%>",
                                 "<%=(String) tipoDoc.getAtributo("normalizado")%>",
                                 "<%=(String) tipoDoc.getAtributo("descTipoDoc")%>",
                                 "<%=(String) tipoDoc.getAtributo("grupo1")%>",
                                 "<%=(String) tipoDoc.getAtributo("tipo1")%>",
                                 "<%=(String) tipoDoc.getAtributo("grupo2")%>",
                                 "<%=(String) tipoDoc.getAtributo("tipo2")%>",
                                 "<%=(String) tipoDoc.getAtributo("grupo3")%>",
                                 "<%=(String) tipoDoc.getAtributo("tipo3")%>",
                                 "<%=(String) tipoDoc.getAtributo("grupo4")%>",
                                 "<%=(String) tipoDoc.getAtributo("tipo4")%>",
                                 "<%=(String) tipoDoc.getAtributo("grupo5")%>",
                                 "<%=(String) tipoDoc.getAtributo("tipo5")%>",
                                 "<%=(String) tipoDoc.getAtributo("longitudMaxima")%>",
                                 "<%=(String) tipoDoc.getAtributo("validacion")%>"];
                                 j++;
                                 <%}%>
                             }//de la funcion
                             
                             
                             function redireccionaFrame()
                             {
                                 frame=(ventana=="true")?top1.mainFrame1:top.mainFrame;	
                             }//de la funcion
                             
                             
                             function inicializar() {
                                 refresca(tablaTipoDocs);
                                 recuperaDatosIniciales();
                                 redireccionaFrame();
                                 //Vectores para los campos, imágenes y enlaces de la REJILLA
                                 vectorCamposRejilla1 = [document.forms[0].codTipoDoc,document.forms[0].codigoINE,document.forms[0].codigoAccede,
                                     document.forms[0].duplicado,document.forms[0].persFJ,document.forms[0].normalizado,
                                     document.forms[0].descTipoDoc,document.forms[0].grupo1,document.forms[0].tipo1,
                                     document.forms[0].grupo2,document.forms[0].tipo2,document.forms[0].grupo3,document.forms[0].tipo3,
                                     document.forms[0].grupo4,document.forms[0].tipo4,document.forms[0].grupo5,document.forms[0].tipo5,
                                     document.forms[0].longitudMaxima,document.forms[0].validacion];
                                 vectorAnchorsRejilla = [document.all.anchorTipo1,document.all.anchorTipo2,document.all.anchorTipo3,
                                     document.all.anchorTipo4,document.all.anchorTipo5];
                                 vectorBotonesRejilla = [document.forms[0].botonTipo1,document.forms[0].botonTipo2,document.forms[0].botonTipo3,
                                     document.forms[0].botonTipo4,document.forms[0].botonTipo5];
                                 //Vector de Botones para la realización de las OPERACIONES con la Ent Singular
                                 vectorBotones = [document.forms[0].botonAlta,document.forms[0].botonModificar,
                                     document.forms[0].botonBorrar,document.forms[0].botonLimpiar];
                                 //Inicializo el Formulario
                                 inicializarFormulario();
                                 //Cargamos la lista de Documentos en la Tabla
                                 cargarListaTipoDocs(listaTipoDocs);			
                                 if(ventana=="false"){
                                     pleaseWait1("off",frame);
                                 }else{
                                 pleaseWait1("off",frame);
                                 var parametros = self.parent.opener.xanelaAuxiliarArgs;
                                 pulsarBuscar(); 
                             } 
                         }//de la funcion
                         
                         
                         function inicializarFormulario()
                         {
                             limpiarFormulario("todo");
                         }//de la funcion
                         
                         
                         /**************  FUNCIONES PARA EL RELLENADO DE CAMPOS ***********************/
                         function rellenar(vectorCampos,vectorDatos)
                         {
                             var longDatos = vectorDatos.length;
                             var longCampos = vectorCampos.length;
                             if(longDatos==longCampos)
                                 {
                                     for(i=0;i<longCampos;i++)
                                         {
                                             var campo =	eval("document.forms[0]."+vectorCampos[i]);
                                             var tipoCampo = campo.type;
                                             var vectorValorCampo = vectorDatos[i];//vector de valores es un vector que puede tener longitud 1 ó 3
                                             var valorCampo=vectorValorCampo[0];
                                             //Asignamos el valor al campo, que es la posicion 0 del vector vectorValorCampo, sólo si es campo de texto,
                                             //si es checkbox no puesto que el valor ya lo tiene asignado estáticamente en la definición del campo	
                                             if (tipoCampo.toLowerCase() == "text" || tipoCampo.toLowerCase() == "hidden")
                                                 {
                                                     campo.value=valorCampo;
                                                 }
                                                 else if (tipoCampo.toLowerCase() == "checkbox") //Es un checkbox
                                                     {
                                                         if (vectorValorCampo.length==3)
                                                             {
                                                                 var checkMIN=vectorValorCampo[1];
                                                                 var checkMAX=vectorValorCampo[2];
                                                                 if (valorCampo==checkMIN)
                                                                     campo.checked=false;
                                                                 else if (valorCampo==checkMAX)
                                                                     campo.checked=true;
                                                                 else
                                                                     {
                                                                         //alert("Hay un campo de texto cuyo valor no se corresponde con los valores asociados al checked y no checked");													
                                                                         campo.checked=false;
                                                                     }//del if
                                                                 }
                                                                 else {
                                                                     //alert("Hay un campo checkbox y la longitud de su vector tipo es distinto de 3");						
                                                                 }
                                                             }//del if						
                                                         }//del for
                                                     }
                                                     else {
                                                         //alert("La longitud del vector de nombres de Campos no se corresponde con la del de Datos");
                                                     }
                                                 }//de la funcion
                                                 
                                                 
                                                 /**************  FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS ***********************/
                                                 function limpiarFormulario(opcion_limpieza)
                                                 {
                                                     if (opcion_limpieza=="todo") //limpiamos todo el formulario
                                                         {
                                                             var vector = [document.forms[0].codTipoDoc];
                                                             habilitarGeneral(vector); 
                                                             rellenar(vectorCamposRejilla,vectorValoresResetRejilla);
                                                             tablaTipoDocs.lineas = new Array();
                                                             refresca(tablaTipoDocs);
                                                         }
                                                         else if (opcion_limpieza=="tabla") //limpiamos sólo la tabla
                                                             {
                                                                 tablaTipoDocs.lineas = new Array();
                                                                 refresca(tablaTipoDocs);
                                                             }
                                                             else if (opcion_limpieza=="campos_rejilla") //limpiamos solo los campos de la rejilla
                                                                 {
                                                                     var vector = [document.forms[0].codTipoDoc];
                                                                     habilitarGeneral(vector);
                                                                     rellenar(vectorCamposRejilla,vectorValoresResetRejilla);
                                                                 }//del if
                                                                 else if (opcion_limpieza=="rejilla/tabla") //limpiamos los campos de la rejilla y la tabla
                                                                     {
                                                                         var vector = [document.forms[0].codTipoDoc];
                                                                         habilitarGeneral(vector);
                                                                         tablaTipoDocs.lineas = new Array();
                                                                         refresca(tablaTipoDocs);
                                                                         rellenar(vectorCamposRejilla,vectorValoresResetRejilla);
                                                                     }
                                                                 }//de la funcion
                                                                 
                                                                 
                                                                 /**************  FUNCIONES DE VALIDACION DE CAMPOS ***********************/
                                                                 function validarCamposRejilla() {
                                                                     //Insertamos en un array los valores de los campos obligatorios, pero sólo los de los campos de texto
                                                                     //puesto que los checkbox siempre tendrán un valor, el valor que se le ha puesto en el VALUE si está
                                                                     //activado ó null si no lo está
                                                                     var valoresCamposRejillaObligatorios = [document.forms[0].codTipoDoc.value,
                                                                         document.forms[0].codigoINE.value,document.forms[0].descTipoDoc.value,document.forms[0].grupo1.value,
                                                                         document.forms[0].tipo1.value,document.forms[0].validacion.value];
                                                                     var validacionCorrecta=true;
                                                                     for(var i=0;i<valoresCamposRejillaObligatorios.length;i++)
                                                                         {
                                                                             if (valoresCamposRejillaObligatorios[i]=="")
                                                                                 return false;
                                                                         }//del for
                                                                         return true;
                                                                     }//de la funcion
                                                                     
                                                                     function noEsta(indice) {
                                                                         var cod = document.forms[0].codTipoDoc.value;
                                                                         for(i=0;(i<listaTipoDocs.length);i++){
                                                                             if(i!=indice){
                                                                                 if((listaTipoDocs[i][0]) == cod)
                                                                                     return false;
                                                                             }
                                                                         }
                                                                         return true;
                                                                     }//de la funcion
                                                                     
                                                                     function filaSeleccionada(tabla) {
                                                                         var i = tabla.selectedIndex;
                                                                         if((i>=0)&&(!tabla.ultimoTable))
                                                                             return true;
                                                                         return false;
                                                                     }//de la funcion
                                                                     
                                                                     
                                                                     /**************  FUNCIONES DE CARGA DE DATOS DINAMICA ***********************/
                                                                     function cargarListaTipoDocs(lista)
                                                                     {
                                                                         tablaTipoDocs.lineas = lista;
                                                                         refresca(tablaTipoDocs);
                                                                         //Oculto la imagen de carga de datos de la pantalla
                                                                         pleaseWait1("off",frame);
                                                                         
                                                                     }//de la funcion
                                                                     
                                                                     
                                                                     /**************  FUNCIONES DE PULSACION DE BOTONES ***********************/
                                                                     function pulsarAlta()
                                                                     {
                                                                         if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
                                                                             {
                                                                                 if(noEsta()) //Si el código está en la lista
                                                                                     {
                                                                                         document.forms[0].opcion.value="alta";
                                                                                         document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                                                         document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/TipoDocumentos.do";
                                                                                         document.forms[0].submit();
                                                                                         limpiarFormulario("campos_rejilla");
                                                                                     }
                                                                                     else
                                                                                         jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                                                                         }
                                                                                         else
                                                                                             jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                                                             }//de la funcion
                                                                                             
                                                                                             
                                                                                             function pulsarModificar()
                                                                                             {
                                                                                                 if(filaSeleccionada(tablaTipoDocs)) //Si  ha seleccionado alguna fila
                                                                                                     {
                                                                                                         if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
                                                                                                             {
                                                                                                                 if(noEsta(tablaTipoDocs.selectedIndex)) //Si el código está en la lista
                                                                                                                     {
                                                                                                                         var vector = [document.forms[0].codTipoDoc];
                                                                                                                         habilitarGeneral(vector);		
                                                                                                                         document.forms[0].opcion.value="modificar";
                                                                                                                         document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                                                                                         document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/TipoDocumentos.do";
                                                                                                                         document.forms[0].submit();
                                                                                                                         deshabilitarGeneral(vector);
                                                                                                                         limpiarFormulario("campos_rejilla");
                                                                                                                     }
                                                                                                                     else
                                                                                                                         jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                                                                                                         }
                                                                                                                         else
                                                                                                                             jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                                                                                             }
                                                                                                                             else
                                                                                                                                 jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
                                                                                                                                 }//de la funcion
                                                                                                                                 
                                                                                                                                 
                                                                                                                                 function pulsarBorrar()
                                                                                                                                 {
                                                                                                                                     if(filaSeleccionada(tablaTipoDocs)) //Si  ha seleccionado alguna fila
                                                                                                                                         {
                                                                                                                                             if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
                                                                                                                                                 {
                                                                                                                                                     if(noEsta(tablaTipoDocs.selectedIndex)) //Si el código está en la lista
                                                                                                                                                         {
                                                                                                                                                             if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimTipDoc")%>') ==1) {
                                                                                                                                                                 var vector = [document.forms[0].codTipoDoc];
                                                                                                                                                                 habilitarGeneral(vector);
                                                                                                                                                                 document.forms[0].opcion.value="eliminar";
                                                                                                                                                                 document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                                                                                                                                 document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/TipoDocumentos.do";
                                                                                                                                                                 document.forms[0].submit();
                                                                                                                                                                 deshabilitarGeneral(vector);
                                                                                                                                                                 limpiarFormulario("campos_rejilla");
                                                                                                                                                                 if(tablaTipoDocs.selectedIndex != -1 ) {
                                                                                                                                                                     tablaTipoDocs.selectLinea(tablaTipoDocs.selectedIndex);
                                                                                                                                                                     tablaTipoDocs.selectedIndex = -1;
                                                                                                                                                                 }
                                                                                                                                                             }
                                                                                                                                                         }
                                                                                                                                                         else
                                                                                                                                                             jsp_alerta("A","<%=descriptor.getDescripcion("msjCodExiste")%>"); 
                                                                                                                                                             }
                                                                                                                                                             else
                                                                                                                                                                 jsp_alerta("A","<%=descriptor.getDescripcion("msjObligTodos")%>");
                                                                                                                                                                 }
                                                                                                                                                                 else
                                                                                                                                                                     jsp_alerta("A","<%=descriptor.getDescripcion("msjNoSelecFila")%>");					
                                                                                                                                                                     }//de la funcion
                                                                                                                                                                     
                                                                                                                                                                     function noEliminarTDoc() {
                                                                                                                                                                         jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimTDoc")%>");
                                                                                                                                                                         }
                                                                                                                                                                         
                                                                                                                                                                         
                                                                                                                                                                         function pulsarLimpiar()
                                                                                                                                                                         {
                                                                                                                                                                             limpiarFormulario("campos_rejilla");
                                                                                                                                                                             if(tablaTipoDocs.selectedIndex != -1 ) {
                                                                                                                                                                                 tablaTipoDocs.selectLinea(tablaTipoDocs.selectedIndex);
                                                                                                                                                                                 tablaTipoDocs.selectedIndex = -1;
                                                                                                                                                                             }
                                                                                                                                                                         }//de la funcion
                                                                                                                                                                         
                                                                                                                                                                         function pulsarSalir()
                                                                                                                                                                         {
                                                                                                                                                                             if(ventana=="false"){
                                                                                                                                                                                 document.forms[0].target = "mainFrame";
                                                                                                                                                                                 document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
                                                                                                                                                                                 document.forms[0].submit();
                                                                                                                                                                             }else{
                                                                                                                                                                             var datosRetorno;
                                                                                                                                                                             if(indice>-1)
                                                                                                                                                                                 datosRetorno = listaTipoDocs[indice];
                                                                                                                                                                             self.parent.opener.retornoXanelaAuxiliar(datosRetorno);
                                                                                                                                                                         }
                                                                                                                                                                     }//de la funcion
                                                                                                                                                                     
                                                                                                                                                                     
        </script>
    </head>
    
    <body class="bandaBody" onLoad="inicializar();">
        
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        
        
        <form action="" method="get" name="formulario" target="_self">
            <input type="hidden" name="opcion">
            <input type="hidden" name="target1"  value="">
            <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">
            <input type="hidden" name="codTipoDocAntiguo" value="">
            
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_ManTiposDocs")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td id="tablaTipoDocs"></td>
            </tr> 
            <tr> <!-- Este tr 1 -->
                <td style="width: 100%"> <!-- Este td 1 -->
                        <%
                    String cabTipoDoc = "160";
                    if (accede.equals("si")) {
                        cabTipoDoc = "120";
                    }
                        %>
                    <input name="codTipoDoc" type="text" class="inputTextoObligatorio" maxlength=4 
                           style="width:4%" onkeyup="return SoloDigitosNumericos(this);">
                    <input name="codigoINE" type="text" class="inputTextoObligatorio"  maxlength=1
                           style="width:4%" onkeyup="return SoloDigitosNumericos(this);">
                    <%if (accede.equals("si")) {%>
                        <input name="codigoAccede" type="text" class="inputTextoObligatorio" maxlength=4
                               style="width:4%" onkeyup="return xAMayusculas(this);">
                    <%} else {%>
                        <input name="codigoAccede" type="hidden">
                    <%}%>
                    <input name="duplicado" type="checkbox" class="inputTexto" value="1" style="margin: 0 1.5%">
                    <input name="persFJ" type="checkbox" class="inputTexto" value="1" style="margin: 0 1.5%">
                    <input name="normalizado" type="checkbox" class="inputTexto" value="1" style="margin: 0 1.5%">
                    <input name="descTipoDoc" type="text" class="inputTextoObligatorio" style="width:18.5%"maxlength=30
                           onblur="return xAMayusculas(this);">
                    <input name="grupo1" type="text" class="inputTextoObligatorio" maxlength=2
                           style="width:3.75%" onkeyup="return SoloDigitosNumericos(this);">
                    <input class="inputTextoObligatorio" type="text" name="tipo1" style="width:1.5%;padding-left: 2px" readonly
                           onfocus="javascript:{divSegundoPlano=true;inicializarValores('tipo1','tipo1',tipos,tipos);}"
                           onclick="javascript:{divSegundoPlano=false;inicializarValores('tipo1','tipo1',tipos,tipos);}">
                    <a name="anchorTipo1" href="javascript:{divSegundoPlano=false;inicializarValores('tipo1','tipo1',tipos,tipos);}"
                       style="text-decoration:none;" onfocus="javascript:this.focus();">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonTipo1" style="cursor:hand;"></span>
                    </a>
                    <input name="grupo2" type="text" class="inputTexto" maxlength=2
                           style="width:3.75%" onkeyup="return SoloDigitosNumericos(this);">
                    <input class="inputTexto" type="text" name="tipo2" style="width:1.5%;padding-left: 2px" readonly
                           onfocus="javascript:{divSegundoPlano=true;inicializarValores('tipo2','tipo2',tipos,tipos);}"
                           onclick="javascript:{divSegundoPlano=false;inicializarValores('tipo2','tipo2',tipos,tipos);}">
                    <a name="anchorTipo2" href="javascript:{divSegundoPlano=false;inicializarValores('tipo2','tipo2',tipos,tipos);}"
                       style="text-decoration:none;" onfocus="javascript:this.focus();">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonTipo2" style="cursor:hand;"></span>
                    </a>
                    <input name="grupo3" type="text" class="inputTexto" maxlength=2
                           style="width:3.75%" onkeyup="return SoloDigitosNumericos(this);">
                    <input class="inputTexto" type="text" name="tipo3" style="width:1.5%;padding-left: 2px" readonly
                           onfocus="javascript:{divSegundoPlano=true;inicializarValores('tipo3','tipo3',tipos,tipos);}"
                           onclick="javascript:{divSegundoPlano=false;inicializarValores('tipo3','tipo3',tipos,tipos);}">
                    <a name="anchorTipo3" href="javascript:{divSegundoPlano=false;inicializarValores('tipo3','tipo3',tipos,tipos);}"
                       style="text-decoration:none;" onfocus="javascript:this.focus();">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonTipo3" style="cursor:hand;"></span>
                    </a>
                    <input name="grupo4" type="text" class="inputTexto" style="width:3.75%" maxlength=2
                           onkeyup="return SoloDigitosNumericos(this);">
                    <input class="inputTexto" type="text" name="tipo4" style="width:1.5%;padding-left: 2px" readonly
                           onfocus="javascript:{divSegundoPlano=true;inicializarValores('tipo4','tipo4',tipos,tipos);}"
                           onclick="javascript:{divSegundoPlano=false;inicializarValores('tipo4','tipo4',tipos,tipos);}">
                    <a name="anchorTipo4" href="javascript:{divSegundoPlano=false;inicializarValores('tipo4','tipo4',tipos,tipos);}"
                       style="text-decoration:none;" onfocus="javascript:this.focus();">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonTipo4" style="cursor:hand;"></span>
                    </a>
                    <input name="grupo5" type="text" class="inputTexto" style="width:3.75%" maxlength=2
                           onkeyup="return SoloDigitosNumericos(this);">
                    <input class="inputTexto" type="text" name="tipo5" style="width:1.5%;padding-left: 2px" readonly
                           onfocus="javascript:{divSegundoPlano=true;inicializarValores('tipo5','tipo5',tipos,tipos);}"
                           onclick="javascript:{divSegundoPlano=false;inicializarValores('tipo5','tipo5',tipos,tipos);}">
                    <a name="anchorTipo5" href="javascript:{divSegundoPlano=false;inicializarValores('tipo5','tipo5',tipos,tipos);}"
                       style="text-decoration:none;" onfocus="javascript:this.focus();">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonTipo5" style="cursor:hand;"></span>
                    </a>
                    <input name="longitudMaxima" type="text" class="inputTexto" style="width:3.75%" maxlength=2
                           onkeyup="return SoloDigitosNumericos(this);">
                    <input name="validacion" type="text" class="inputTextoObligatorio" size=1 style="width:3.75%" 
                          onkeyup="return xAMayusculas(this);">
            </td> <!-- Este td 1 -->
        </tr> <!-- Este tr 1 -->
        <!-- Engadido -->	     		
    </table>
    <div class="sub3titulo"><%=descriptor.getDescripcion("etiqLey")%></div>
    <table>
        <tr>
            <td style="width: 33%" class="etiqueta">&nbsp;N:  <%=descriptor.getDescripcion("etiqNumer")%></td>
            <td style="width: 33%"  class="etiqueta">&nbsp;X:  <%=descriptor.getDescripcion("etiqAlfanum")%></td>
            <td style="width: 33%"  class="etiqueta">&nbsp;A:  <%=descriptor.getDescripcion("etiqAlfab")%></td>
        </tr>
    </table>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral"  name="botonAlta" onClick="pulsarAlta();" accesskey="A" value="<%=descriptor.getDescripcion("gbAlta")%>">
        <input type="button" class="botonGeneral"  name="botonModificar" onClick="pulsarModificar();" accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>">
        <input type="button" class="botonGeneral" name="botonBorrar" onClick="pulsarBorrar();" accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>">
        <input type="button" class="botonGeneral"  name="botonLimpiar" onClick="pulsarLimpiar();" accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
        <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
    </div>             
</div>             
</form>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
<script type="text/javascript">
    var indice;
    var tablaTipoDocs = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaTipoDocs"));

    tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');//cod
        tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_INE")%>');//ine
            <%if (accede.equals("si")) {%>
            tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_Accede")%>');//ine
                <%} else {%>
                tablaTipoDocs.addColumna("0","center",'<%=descriptor.getDescripcion("gEtiq_Accede")%>');//ine
                    <%}%>
                    tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_duplicado")%>');//duplicado
                        tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_persFJ")%>');//pers FJ
                            tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_normalizado")%>');//noramlizado
                                tablaTipoDocs.addColumna("<%=cabTipoDoc%>","center",'<%=descriptor.getDescripcion("gEtiq_Descrip")%>');//descripcion
                                    tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_Grupo1")%>');//grupo1
                                        tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_Tipo1")%>');//tipo1
                                            tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_Grupo2")%>');//grupo2
                                                tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_Tipo2")%>');//tipo2
                                                    tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_Grupo3")%>');//grupo3
                                                        tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_Tipo3")%>');//tipo3
                                                            tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_Grupo4")%>');//grupo4
                                                                tablaTipoDocs.addColumna("38","center",'<%=descriptor.getDescripcion("gEtiq_Tipo4")%>');//tipo4
                                                                    tablaTipoDocs.addColumna("38","center",'<%=descriptor.getDescripcion("gEtiq_Grupo5")%>');//grupo5
                                                                        tablaTipoDocs.addColumna("38","center",'<%=descriptor.getDescripcion("gEtiq_Tipo5")%>');//tipo5
                                                                            tablaTipoDocs.addColumna("40","center",'<%=descriptor.getDescripcion("gEtiq_longMax")%>');//long Max
                                                                                tablaTipoDocs.addColumna("46","center",'<%=descriptor.getDescripcion("gEtiq_validacion")%>');//validacion
                                                                                    tablaTipoDocs.displayCabecera=true;

                                                                                    function refresca(tabla)
                                                                                    {
                                                                                        tabla.displayTabla();
                                                                                    }//de la funcion


                                                                                    function rellenarDatos(tableName,rowID)
                                                                                    {
                                                                                        if(tablaTipoDocs==tableName)
                                                                                            {
                                                                                                if(filaSeleccionada(tablaTipoDocs)) //Si  ha seleccionado alguna fila
                                                                                                    {
                                                                                                        var i=rowID;
                                                                                                        indice = rowID;	    			
                                                                                                        if(i>=0)
                                                                                                            {
                                                                                                                var vector = [document.forms[0].codTipoDoc];
                                                                                                                deshabilitarGeneral(vector);
                                                                                                                var vectorDatosRejilla = [[listaTipoDocs[i][0]],[listaTipoDocs[i][1]],[listaTipoDocs[i][2]],
                                                                                                                    [listaTipoDocs[i][3],"0","1"],[listaTipoDocs[i][4],"0","1"],
                                                                                                                    [listaTipoDocs[i][5],"0","1"],[listaTipoDocs[i][6]],[listaTipoDocs[i][7]],
                                                                                                                    [listaTipoDocs[i][8]],[listaTipoDocs[i][9]],[listaTipoDocs[i][10]],[listaTipoDocs[i][11]],
                                                                                                                    [listaTipoDocs[i][12]],[listaTipoDocs[i][13]],[listaTipoDocs[i][14]],
                                                                                                                    [listaTipoDocs[i][15]],[listaTipoDocs[i][16]],[listaTipoDocs[i][17]],
                                                                                                                    [listaTipoDocs[i][18]]];
                                                                                                                //Nos quedamos con el código antiguo por si se desea modificar
                                                                                                                document.forms[0].codTipoDocAntiguo.value=listaTipoDocs[i][0];
                                                                                                                rellenar(vectorCamposRejilla,vectorDatosRejilla);
                                                                                                            }
                                                                                                        }  
                                                                                                    }
                                                                                                } //de la funcion

                                                                                                // FUNCION DE CONTROL DE TECLAS
                                                                                                document.onmouseup = checkKeys; 
                                                                                                        
    function checkKeysLocal(evento, tecla) {
        var teclaAuxiliar="";
        if(window.event){
            evento = window.event;
            teclaAuxiliar = evento.keyCode;
        }else
            teclaAuxiliar = evento.which;

        if((layerVisible)||(divSegundoPlano)) buscar(evento);        
        keyDel(evento);
        if(teclaAuxiliar == 9){
            if(layerVisible) ocultarDiv();
            if(divSegundoPlano) divSegundoPlano = false;
        }
        if(teclaAuxiliar == 1){
            if(layerVisible) setTimeout('ocultarDivNoFocus()',40);
            if(divSegundoPlano)	divSegundoPlano = false;
        }
        if(teclaAuxiliar == 40){
            if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
            else upDownTable(tablaTipoDocs,listaTipoDocs,teclaAuxiliar);
        }
        if(teclaAuxiliar == 38){
            if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
            else upDownTable(tablaTipoDocs,listaTipoDocs,teclaAuxiliar);
        }
        if (evento.button == 1){
            if(layerVisible) setTimeout("ocultarDiv()",50);
            if(capaVisible) ocultarLista();
            if(divSegundoPlano) divSegundoPlano = false;
        }
    }//de la funcion

                                                                                                        
        </script>
    </body>
</html>
