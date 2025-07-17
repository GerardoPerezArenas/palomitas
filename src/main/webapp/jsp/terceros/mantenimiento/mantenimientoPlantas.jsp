<%@ taglib uri="/WEB-INF/struts/struts-html.tld"  prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.terceros.mantenimiento.MantenimientosTercerosForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.terceros.ParametrosTerceroValueObject" %>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title> Mantenimiento de Plantas </title>
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
        //Vectores para albergar los datos de la lista de Organismos Externos, tabla (Dinámicos)
        var listaPlantas = new Array();
        //Variable para direccionamiento al frame
        var frame;
        //Vectores para el Almacenado de Objetos y datos
        var vectorCamposRejilla = ["codigo","descripcion"];
        //Vector de Valores de Limpieza para pasarlo a la función rellenar, es un vector donde cada posicion es un
        //vector que contiene: En la posicion 0 el valor que se asignará al campo en la función de limpieza, si el
        //campo es un checkbox, además, contendrá las posiciones 2 y 3 que contendrá los valores que se asignarán 
        //cuando no esté chequeado (pos 1) ó cuando lo esté (pos 2)
        var vectorValoresResetRejilla = [[""],[""]];															 
        var vectorBotones = new Array();
        var vectorCamposRejilla1 = new Array();
        var vectorAnchorsRejilla = new Array();
        var vectorBotonesRejilla = new Array();
        
        /**************  FUNCIONES DE CARGA E INICIALIZACION DE DATOS ***********************/
        function recuperaDatosIniciales()
        {
    <%
            Vector listaPlantas = mantForm.getListaPlantas();
            int i = 0;
    %>
        var j=0;
    <%for (i = 0; i < listaPlantas.size(); i++) {
                GeneralValueObject planta = (GeneralValueObject) listaPlantas.elementAt(i);
                         %>
                             listaPlantas[j] = ["<%=(String) planta.getAtributo("codigo")%>",
                                 "<%=(String) planta.getAtributo("descripcion")%>"];
                                 j++;
                                 <%}%>
                             }//de la funcion
                             
                             
                             function redireccionaFrame()
                             {
                                 frame=(ventana=="true")?top1.mainFrame1:top.mainFrame;	
                             }//de la funcion
                             
                             
                             function inicializar()
                             {
                                 window.focus();
                                 recuperaDatosIniciales();
                                 redireccionaFrame();
                                 //Vectores para los campos, imágenes y enlaces de la REJILLA
                                 vectorCamposRejilla1 = [document.forms[0].codigo,document.forms[0].descripcion];
                                 vectorAnchorsRejilla = [];
                                 vectorBotonesRejilla = [];
                                 //Vector de Botones para la realización de las OPERACIONES con la Ent Singular
                                 vectorBotones = [document.forms[0].botonAlta,document.forms[0].botonModificar,
                                     document.forms[0].botonBorrar,document.forms[0].botonLimpiar];
                                 //Inicializo el Formulario
                                 inicializarFormulario();
                                 //Cargamos la lista de Documentos en la Tabla
                                 cargarListaPlantas(listaPlantas);			
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
                                             if (tipoCampo.toLowerCase() == "text")
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
                                                             var vector = [document.forms[0].codigo];
                                                             habilitarGeneral(vector);
                                                             rellenar(vectorCamposRejilla,vectorValoresResetRejilla);
                                                             tablaPlantas.lineas = new Array();
                                                             refresca(tablaPlantas);
                                                         }
                                                         else if (opcion_limpieza=="tabla") //limpiamos sólo la tabla
                                                             {
                                                                 tablaPlantas.lineas = new Array();
                                                                 refresca(tablaPlantas);
                                                             }
                                                             else if (opcion_limpieza=="campos_rejilla") //limpiamos solo los campos de la rejilla
                                                                 {
                                                                     var vector = [document.forms[0].codigo];
                                                                     habilitarGeneral(vector);
                                                                     rellenar(vectorCamposRejilla,vectorValoresResetRejilla);
                                                                 }//del if
                                                                 else if (opcion_limpieza=="rejilla/tabla") //limpiamos los campos de la rejilla y la tabla
                                                                     {
                                                                         var vector = [document.forms[0].codigo];
                                                                         habilitarGeneral(vector);
                                                                         tablaPlantas.lineas = new Array();
                                                                         refresca(tablaPlantas);
                                                                         rellenar(vectorCamposRejilla,vectorValoresResetRejilla);
                                                                     }
                                                                 }//de la funcion
                                                                 
                                                                 
                                                                 /**************  FUNCIONES DE VALIDACION DE CAMPOS ***********************/
                                                                 function validarCamposRejilla()
                                                                 {
                                                                     //Insertamos en un array los valores de los campos obligatorios, pero sólo los de los campos de texto
                                                                     //puesto que los checkbox siempre tendrán un valor, el valor que se le ha puesto en el VALUE si está
                                                                     //activado ó null si no lo está
                                                                     var valoresCamposRejillaObligatorios = [document.forms[0].codigo.value];
                                                                     var validacionCorrecta=true;
                                                                     for(var i=0;i<valoresCamposRejillaObligatorios.length;i++)
                                                                         {
                                                                             if (valoresCamposRejillaObligatorios[i]=="")
                                                                                 return false;
                                                                         }//del for
                                                                         return true;
                                                                     }//de la funcion
                                                                     
                                                                     function noEsta(indice)
                                                                     {
                                                                         var cod = document.forms[0].codigo.value;
                                                                         for(i=0;(i<listaPlantas.length);i++){
                                                                             if(i!=indice){
                                                                                 if((listaPlantas[i][0]) == cod)
                                                                                     return false;
                                                                             }
                                                                         }
                                                                         return true;
                                                                     }//de la funcion
                                                                     
                                                                     function filaSeleccionada(tabla)
                                                                     {
                                                                         var i = tabla.selectedIndex;
                                                                         if((i>=0)&&(!tabla.ultimoTable))
                                                                             return true;
                                                                         return false;
                                                                     }//de la funcion
                                                                     
                                                                     
                                                                     /**************  FUNCIONES DE CARGA DE DATOS DINAMICA ***********************/
                                                                     function cargarListaPlantas(lista)
                                                                     {
                                                                         tablaPlantas.lineas = lista;
                                                                         refresca(tablaPlantas);
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
                                                                                         document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Plantas.do";
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
                                                                                                 if(filaSeleccionada(tablaPlantas)) //Si  ha seleccionado alguna fila
                                                                                                     {
                                                                                                         if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
                                                                                                             {
                                                                                                                 if(noEsta(tablaPlantas.selectedIndex)) //Si el código está en la lista
                                                                                                                     {
                                                                                                                         var vector = [document.forms[0].codigo];
                                                                                                                         habilitarGeneral(vector);
                                                                                                                         document.forms[0].opcion.value="modificar";
                                                                                                                         document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                                                                                         document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Plantas.do";
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
                                                                                                                                     if(filaSeleccionada(tablaPlantas)) //Si  ha seleccionado alguna fila
                                                                                                                                         {
                                                                                                                                             if(validarCamposRejilla()) //Si los campos de la rejilla son correctos (los obligatorios)
                                                                                                                                                 {
                                                                                                                                                     if(noEsta(tablaPlantas.selectedIndex)) //Si el código está en la lista
                                                                                                                                                         {
                                                                                                                                                             if(jsp_alerta("C",'<%=descriptor.getDescripcion("desElimPlanta")%>') ==1) {
                                                                                                                                                                 var vector = [document.forms[0].codigo];
                                                                                                                                                                 habilitarGeneral(vector);
                                                                                                                                                                 document.forms[0].opcion.value="eliminar";
                                                                                                                                                                 document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                                                                                                                                                                 document.forms[0].action="<%=request.getContextPath()%>/terceros/mantenimiento/Plantas.do";
                                                                                                                                                                 document.forms[0].submit();
                                                                                                                                                                 deshabilitarGeneral(vector);
                                                                                                                                                                 limpiarFormulario("campos_rejilla");
                                                                                                                                                                 if(tablaPlantas.selectedIndex != -1 ) {
                                                                                                                                                                     tablaPlantas.selectLinea(tablaPlantas.selectedIndex);
                                                                                                                                                                     tablaPlantas.selectedIndex = -1;
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
                                                                                                                                                                     
                                                                                                                                                                     function noEliminarPlant() {
                                                                                                                                                                         jsp_alerta("A","<%=descriptor.getDescripcion("msjNoElimPlant")%>");
                                                                                                                                                                         }
                                                                                                                                                                         
                                                                                                                                                                         
                                                                                                                                                                         function pulsarLimpiar()
                                                                                                                                                                         {
                                                                                                                                                                             limpiarFormulario("campos_rejilla");
                                                                                                                                                                             if(tablaPlantas.selectedIndex != -1 ) {
                                                                                                                                                                                 tablaPlantas.selectLinea(tablaPlantas.selectedIndex);
                                                                                                                                                                                 tablaPlantas.selectedIndex = -1;
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
                                                                                                                                                                                 datosRetorno = listaPlantas[indice];
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
    <input type="hidden" name="codigoAntiguo" value="">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantPlantas")%></div>
    <div class="contenidoPantalla">
        <table width="100%">
            <tr> 
                <td> 
                    <div id="tablaPlantas" ></div>
                </td>
            </tr>
            <tr> 
                <td> 
                    <input name="codigo" type="text" class="inputTextoObligatorio" maxlength=3  
                           style="width:13%" onblur="return xAMayusculas(this);">
                    <input name="descripcion" type="text" class="inputTextoObligatorio" maxlength=60 
                            style="width:86%" onblur="return xAMayusculas(this);">
                </td>
            </tr>
        </TABLE>
        <div class="botoneraPrincipal">
            <input type="button" class="botonGeneral"  name="botonAlta" onClick="pulsarAlta();" accesskey="A" 
                   value="<%=descriptor.getDescripcion("gbAlta")%>">
            <input type="button" class="botonGeneral"  name="botonModificar" onClick="pulsarModificar();" 
                   accesskey="M" value="<%=descriptor.getDescripcion("gbModificar")%>"> 
            <input type="button" class="botonGeneral" name="botonBorrar" onClick="pulsarBorrar();" 
                    accesskey="E" value="<%=descriptor.getDescripcion("gbEliminar")%>">
            <input type="button" class="botonGeneral"  name="botonLimpiar" onClick="pulsarLimpiar();" 
                    accesskey="L" value="<%=descriptor.getDescripcion("gbLimpiar")%>">
            <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" 
                    accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>">
        </div>
    </div>
</form>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaCombo.js"></script>
<script type="text/javascript">
    var indice;
    var tablaPlantas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById("tablaPlantas"));
    tablaPlantas.addColumna("120","center",'<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
    tablaPlantas.addColumna("780","left",'<%=descriptor.getDescripcion("gEtiq_Descrip")%>');
    tablaPlantas.displayCabecera=true;
    tablaPlantas.displayTabla();
    function refresca(tabla) {
        tabla.displayTabla();
    }//de la funcion


    function rellenarDatos(tableName,rowID) {
        if(tablaPlantas==tableName) {
                if(filaSeleccionada(tablaPlantas)) {
                        var i=rowID;
                        indice = rowID;	    			
                        if(i>=0) {
                                var vector = [document.forms[0].codigo];
                                deshabilitarGeneral(vector);
                                var vectorDatosRejilla = [[listaPlantas[i][0]],[listaPlantas[i][1]]];
                                //Nos quedamos con el código antiguo por si se desea modificar
                                document.forms[0].codigoAntiguo.value=listaPlantas[i][0];
                                rellenar(vectorCamposRejilla,vectorDatosRejilla);
                            }
                        }  
                    }
                } //de la funcion

                // FUNCION DE CONTROL DE TECLAS
                document.onmouseup = checkKeys; 

                function checkKeysLocal(evento, tecla) {
                    var teclaAuxiliar = "";
                    if(window.event) {
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
                    if(teclaAuxiliar == 40){
                        if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
                        else upDownTable(tablaPlantas,listaPlantas,teclaAuxiliar);
                    }
                    if(teclaAuxiliar == 38){
                        if((layerVisible)||(divSegundoPlano)) upDown(teclaAuxiliar);
                        else upDownTable(tablaPlantas,listaPlantas,teclaAuxiliar);
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
