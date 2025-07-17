<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>

<%@ page contentType="text/html;charset=ISO_8859-1"%>
<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title> Mantenimiento de Textos Fijos </title>
        
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 3;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                apl = usuarioVO.getAppCod();
                css = usuarioVO.getCss();
            }%>

        <!-- Estilos -->
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">   
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript">
                 <%
            MantenimientosAdminForm mantForm = (MantenimientosAdminForm) session.getAttribute("MantenimientosAdminForm");
    %>  
        var ventana = '<%=mantForm.getVentana()%>';
        //Vectores para albergar los datos de Aplicaciones (estáticos)
        var codAplicaciones = new Array();
        var descAplicaciones = new Array();
        //Vectores para albergar los datos de Idiomas (estáticos)
        var codIdiomas = new Array();
        var descIdiomas = new Array();
        //Vectores para albergar los datos de los Textos Fijos (dinámicos)
        var listaTextosFijosOriginal = new Array();
        var listaTextosFijos = new Array();
        var frame;
        //Vectores para almacenamiento de los campos y objetos del formulario			
        //Campos, Combos y de Reset de busqueda
        var vectorCamposBusqueda = ['codApli','descApli'];
        var vectorCombosBusqueda = ['comboAplicaciones'];		
        var vectorValoresResetBusqueda = [[''],['']];															 
        //Campos, Combos  y de Reset de Rejilla
        var vectorCamposRejilla = ['codText','texto','estado','fechaCreMod'];
        var vectorCamposRejillaTodos = ['codText','codIdi','descIdi','texto','estado','fechaCreMod'];
        var vectorCombosRejilla = ['comboIdiomas'];		
        var vectorValoresResetRejilla = [[''],[''],[''],[''],[''],['']];															 
        //botones de la aplicación
        var vectorBotones = ['botonAlta','botonModificar','botonBorrar','botonLimpiar'];
        
        
        /**************  FUNCIONES DE CARGA E INICIALIZACION DE DATOS ***********************/
        function recuperaDatosIniciales()
        {
    <%
            Vector listaAplicaciones = mantForm.getListaAplicaciones();
            Vector listaIdiomas = mantForm.getListaIdiomas();
            int i = 0;
    %>
        var j=0;
    <%for (i = 0; i < listaAplicaciones.size(); i++) {
                GeneralValueObject aplicacion = (GeneralValueObject) listaAplicaciones.elementAt(i);
                         %>
                             codAplicaciones[j] = '<%=(String) aplicacion.getAtributo("codigo")%>';
                             descAplicaciones[j] = '<%=(String) aplicacion.getAtributo("descripcion")%>';
                             j++;
                             <%}%>
                             j=0;
    <%for (i = 0; i < listaIdiomas.size(); i++) {
                GeneralValueObject Idioma = (GeneralValueObject) listaIdiomas.elementAt(i);
                         %>
                             codIdiomas[j] = '<%=(String) Idioma.getAtributo("codigo")%>';
                             descIdiomas[j] = '<%=(String) Idioma.getAtributo("descripcion")%>';
                             j++;
                             <%}%> 
                         }//de la funcion
                         
                         
function redireccionaFrame()
{
    frame=(ventana=="true")?top1.mainFrame1:top.mainFrame;	
}//de la funcion


function inicializar(){
    window.focus();
    recuperaDatosIniciales();
    redireccionaFrame();
    cargaCombos();
    inicializarFormulario();
    if(ventana=="false")
        {
            pleaseWait1('off',frame);
        }
        else
            {
                pleaseWait1('off',frame);
                var parametros = self.parent.opener.xanelaAuxiliarArgs;
                pulsarBuscar();
            }
        }//de la funcion


function inicializarFormulario()
{
    limpiarFormulario('todo');
    var botonBuscar = ['botonBuscar'];
    //Desabilitamos el botón buscar
    habilitarGeneralInputs(botonBuscar,true);
    //Habilitamos el combo de busqueda
    comboAplicaciones.activate();		
    //Desabilitamos la rejilla
    habilitarGeneralInputs(vectorCamposRejilla,false);
    //Deshabilitamos el combo de la Rejilla
    habilitarGeneralCombos(vectorCombosRejilla,false);			
    //Desabilitamos los Botones
    habilitarGeneralInputs(vectorBotones,false);
    valoresPorDefecto();
}//de la funcion

function valoresPorDefecto()
{
    document.forms[0].codApli.value=codAplicaciones[0];
    document.forms[0].descApli.value=descAplicaciones[0];		
}//de la funcion

function cargaCombos()
{
    comboAplicaciones.addItems(codAplicaciones,descAplicaciones);
    comboIdiomas.addItems(codIdiomas,descIdiomas);
}//de la funcion

/**************  FUNCIONES DE LIMPIEZA, HABILITACION Y DESHABILITACION DE CAMPOS ***********************/
function limpiarFormulario(opcion_limpieza)
{
    if (opcion_limpieza=='todo') //limpiamos todo el formulario
        {
            rellenar(vectorCamposBusqueda,vectorValoresResetBusqueda);
            rellenar(vectorCamposRejillaTodos,vectorValoresResetRejilla);
            tablaTextosFijos.lineas = new Array();
            refresca(tablaTextosFijos);
        }
        else if (opcion_limpieza=='tabla') //limpiamos sólo la tabla
            {
                tablaTextosFijos.lineas = new Array();
                refresca(tablaTextosFijos);
            }
            else if (opcion_limpieza=='campos_busqueda') //limpiamos solo los campos de busqueda
                {
                    rellenar(vectorCamposBusqueda,vectorValoresResetBusqueda);			
                }
                else if (opcion_limpieza=='campos_rejilla') //limpiamos solo los campos de la rejilla
                    {
                        rellenar(vectorCamposRejillaTodos,vectorValoresResetRejilla);
                    }//del if
                    else if (opcion_limpieza=='rejilla/tabla') //limpiamos los campos de la rejilla y la tabla
                        {
                            tablaTextosFijos.lineas = new Array();
                            refresca(tablaTextosFijos);
                            rellenar(vectorCamposRejillaTodos,vectorValoresResetRejilla);
                        }
                    }//de la funcion

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
                    if (tipoCampo.toLowerCase() == 'text')
                        {
                            campo.value=valorCampo;
                        }
                        else if (tipoCampo.toLowerCase() == 'checkbox') //Es un checkbox
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
                                                alert("Hay un campo de texto cuyo valor no se corresponde con los valores asociados al checked y no checked");													
                                                campo.checked=false;
                                            }//del if
                                        }
                                        else
                                            alert("Hay un campo checkbox y la longitud de su vector tipo es distinto de 3");						
                                    }//del if						
                                }//del for
                            }
                            else
                                alert("La longitud del vector de nombres de Campos no se corresponde con la del de Datos");
                        }//de la funcion



                        /**************  FUNCIONES DE VALIDACION DE CAMPOS ***********************/
                        function validarCamposBusqueda()
                        {
                            var codApli = document.forms[0].codApli.value;
                            if(codApli!="")
                                return true;
                            return false;
                        }//de la funcion

                        function validarCamposRejilla()
                        {
                            var codText = document.forms[0].codText.value;
                            var codIdi = document.forms[0].codIdi.value;
                            var texto = document.forms[0].texto.value;
                            if((codText!="") && (codIdi!="") && (texto!=""))
                                return true;
                            return false;
                        }//de la funcion

function noEsta(indice) {
    var codText = document.forms[0].codText.value;
    var codIdi = document.forms[0].codIdi.value;
    for(i=0;(i<listaTextosFijosOriginal.length);i++) {
        if(i!=indice) {
            if((listaTextosFijosOriginal[i][1] == codText) && (listaTextosFijosOriginal[i][2] == codIdi) ) {					
                return false;
            }
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
function cargarListaTextosFijos(lista)
{
    listaTextosFijos = lista;
    tablaTextosFijos.lineas = lista;
    refresca(tablaTextosFijos);
    //Oculto la imagen de carga de datos de la pantalla
    pleaseWait1('off',frame);
}//de la funcion


/**************  FUNCIONES DE PULSACION DE BOTONES ***********************/
function pulsarBuscar()
{
    //Muestro la imagen de carga de datos de la pantalla
    pleaseWait1('on',frame);
    var botonBuscar = ['botonBuscar'];
    if(validarCamposBusqueda())
        {
            document.forms[0].opcion.value="cargarTextosFijos";
            document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
            document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/TextosFijos.do";
            document.forms[0].submit();
            //Desabilitamos el botón Busar
            habilitarGeneralInputs(botonBuscar,false);
            //Desabilitamos el combo de busqueda
            comboAplicaciones.deactivate();
            //Habilitamos los botones
            habilitarGeneralInputs(vectorBotones,true);
            //Habilitamos los campos de la Rejilla, pero sólo los que no pertenecen a los combos
            habilitarGeneralInputs(vectorCamposRejilla,true);
            //Habilitamos los combos de la rejilla
            habilitarGeneralCombos(vectorCombosRejilla,true);
            //Limpiamos TODOS los campos de la Rejilla
            limpiarFormulario('campos_rejilla');
        }
        else
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
        }//de la funcion

function pulsarAlta()	{
    if(validarCamposRejilla()) {
        if(noEsta()) {
            comboAplicaciones.activate();
            document.forms[0].opcion.value="alta";
            document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
            document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/TextosFijos.do";
            document.forms[0].submit();
            comboAplicaciones.deactivate();
            limpiarFormulario('rejilla/tabla');
        }
        else jsp_alerta("A",'<%=descriptor.getDescripcion("msjCodExiste")%>'); 
        }
        else jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
}//de la funcion

function pulsarModificar() {
    if(filaSeleccionada(tablaTextosFijos)) { //Si  ha seleccionado alguna fila
        if(validarCamposRejilla()) {//Si los campos de la rejilla son correctos (los obligatorios)
            if(noEsta(tablaTextosFijos.selectedIndex)) {//Si el código está en la lista
                comboAplicaciones.activate();
                document.forms[0].opcion.value="modificar";
                document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/TextosFijos.do";
                document.forms[0].submit();
                comboAplicaciones.deactivate();
                limpiarFormulario('rejilla/tabla');
            } else
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjCodExiste")%>'); 
            } else
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
            }else
            jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');					
}//de la funcion

function pulsarBorrar(){
    if(filaSeleccionada(tablaTextosFijos)) //Si  ha seleccionado alguna fila
    {
        if(validarCamposBusqueda()) //Si los campos de la rejilla son correctos (los obligatorios)
            {
                comboAplicaciones.activate();
                document.forms[0].opcion.value="eliminar";
                document.forms[0].target=(ventana=="true")?"oculto1":"oculto";
                document.forms[0].action="<%=request.getContextPath()%>/administracion/mantenimiento/TextosFijos.do";
                document.forms[0].submit();
                comboAplicaciones.deactivate();
                limpiarFormulario('rejilla/tabla');
            }
            else
                jsp_alerta("A",'<%=descriptor.getDescripcion("msjObligTodos")%>');
                }
                else
                    jsp_alerta("A",'<%=descriptor.getDescripcion("msjNoSelecFila")%>');					
}//de la funcion

function pulsarCancelarBuscar()
{
    //Limpiamos TODOS los campos de la Rejilla 
    limpiarFormulario('rejilla/tabla');
    //Habilitamos el botón buscar
    var botonBuscar = ['botonBuscar'];
    habilitarGeneralInputs(botonBuscar,true);
    //habilitamos el combo de busqueda
    comboAplicaciones.activate();
    //Desabilitamos los campos de la Rejilla no afectados por los combos de la Rejilla
    habilitarGeneralInputs(vectorCamposRejilla,false);
    //Desabilitamos los combos de la Rejilla
    habilitarGeneralCombos(vectorCombosRejilla,false);
    //Desabilitamos los botones
    habilitarGeneralInputs(vectorBotones,false);
}//de la funcion

function pulsarLimpiar()
{
    limpiarFormulario('campos_rejilla');
    tablaTextosFijos.selectLinea(-1);
}//de la funcion

function pulsarSalir()
{
    if(ventana=="false")
        {

            document.forms[0].target = "mainFrame";
            document.forms[0].action = '<%=request.getContextPath()%>/jsp/administracion/presentacionADM.jsp';
            document.forms[0].submit();
        }
        else
            {
                var datosRetorno;
                if(indice>-1)
                    datosRetorno = listaTextosFijosOriginal[indice];
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
    <input type="hidden" name="codTextAntiguo" value="">
    <input type="hidden" name="codIdiAntiguo" value="">
    <input type="hidden" name="ventana" value="<%=mantForm.getVentana()%>">

    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_MantTexFijos")%></div>
    <div class="contenidoPantalla">
        <table>
            <tr>
                <td style="width: 60%" class="etiqueta">
                    <%=descriptor.getDescripcion("gEtiq_Aplicacion")%>
                    <input class="inputTextoObligatorio" type="text" name="codApli" id="codApli" size="3">
                    <input class="inputTextoObligatorio" type="text" name="descApli" id="descApli"  style="width:300" readonly>
                    <a name="anchorApli" href="" id="anchorApli">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonApli" style="cursor:hand;" id="botonApli"></span>
                    </a>
                </td>
                <td style="width: 40%;text-align:right">
                    <input name="botonBuscar" type="button"  class="botonGeneral" id="botonBuscar" 
                           value="<%=descriptor.getDescripcion("gbBuscar")%>"
                           onClick="pulsarBuscar();" accesskey="B">
                    <input name="botonCancelar" type="button" class="botonGeneral" id="botonCancelar"
                           value='<%=descriptor.getDescripcion("gbCancelar")%>'
                           onClick="pulsarCancelarBuscar();" accesskey="C">
                </td>
            </tr>
            <tr> 
                <td colspan="2" id="tablaTextosFijos"></td>
            </tr>
            <tr> 
                <td colspan="2"> 
                    <input name="codText" type="text" class="inputTextoObligatorio" maxlength=20 style="width:11%">
                    <input class="inputTextoObligatorio" type="text" name="codIdi" id="codIdi" maxlength="1" style="width:1.5%">
                    <input class="inputTextoObligatorio" type="text" name="descIdi" id="descIdi" style="width:6.5%" readonly>
                    <a name="anchorIdi" href="" id="anchorIdi" style="text-decoration: none">
                        <span class="fa fa-chevron-circle-down" aria-hidden="true" name="botonIdi"  id="botonIdi" style="cursor:hand;"></span>
                    </a>
                    <input name="texto" type="text" class="inputTextoObligatorio" maxlength=255 style="width:54%">
                    <input name="estado" type="text" class="inputTexto" maxlength=1
                            style="width:8.5%" onkeyup="return SoloDigitosNumericos(this);">
                    <input name="fechaCreMod" type="text" class="inputTexto" maxlength=12 
                           style="width:11%" readonly="">
                </td>
            </tr>
    </TABLE>								
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral"  name="botonAlta"	onClick="pulsarAlta();" 
               accesskey="A" value='<%=descriptor.getDescripcion("gbAlta")%>'>
        <input type="button" class="botonGeneral"  name="botonModificar"	onClick="pulsarModificar();" 
               accesskey="M" value='<%=descriptor.getDescripcion("gbModificar")%>'>
        <input type="button" class="botonGeneral" name="botonBorrar"	onClick="pulsarBorrar();" 
               accesskey="B" value='<%=descriptor.getDescripcion("gbBorrar")%>'>
        <input type="button" class="botonGeneral"  name="botonLimpiar"	onClick="pulsarLimpiar();" 
               accesskey="L" value='<%=descriptor.getDescripcion("gbLimpiar")%>'>
        <input type="button" class="botonGeneral" name="botonSalir" onClick="pulsarSalir();" 
               accesskey="S" value='<%=descriptor.getDescripcion("gbSalir")%>'>
    </div>
</div>
</form>
<script type="text/javascript">
    var indice;
    var tablaTextosFijos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaTextosFijos'));
    tablaTextosFijos.addColumna('100','center','<%=descriptor.getDescripcion("gEtiq_Codigo")%>');
    tablaTextosFijos.addColumna('110','left','<%=descriptor.getDescripcion("gEtiq_Idioma")%>');
    tablaTextosFijos.addColumna('490','left','<%=descriptor.getDescripcion("gEtiq_Descrip")%>');
    tablaTextosFijos.addColumna('80','left','<%=descriptor.getDescripcion("gEtiq_Estado")%>');
    tablaTextosFijos.addColumna('120','left','<%=descriptor.getDescripcion("gEtiq_FechaCreMod")%>');
    tablaTextosFijos.displayCabecera=true;
    tablaTextosFijos.displayTabla();

    function refresca(tabla) {
        tabla.displayTabla();
    }

function rellenarDatos(tableName,rowID) {
    if(tablaTextosFijos==tableName) {
        if(filaSeleccionada(tablaTextosFijos)) {
            var i=rowID;
            indice = rowID;
            limpiarFormulario('campos_rejilla');
            if(i>=0) {
                var vectorDatosRejilla = [[listaTextosFijosOriginal[i][1]],[listaTextosFijosOriginal[i][2]],
                    [listaTextosFijosOriginal[i][3]],[listaTextosFijosOriginal[i][4]],[listaTextosFijosOriginal[i][5]],
                    [listaTextosFijosOriginal[i][6]]];
                rellenar(vectorCamposRejillaTodos,vectorDatosRejilla);
                document.forms[0].codTextAntiguo.value = listaTextosFijosOriginal[i][1];
                document.forms[0].codIdiAntiguo.value = listaTextosFijosOriginal[i][2];
            } 
        }
    }
} //de la funcion

// FUNCION DE CONTROL DE TECLAS

<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
window.addEventListener('mousemove', function(e) {
    coordx = e.clientX;
    coordy = e.clientY;
}, true);
<%}%>
document.onmouseup = checkKeys; 

function checkKeysLocal(evento, tecla){
    var teclaAuxiliar="";
      if(window.event){
        evento = window.event;
        teclaAuxiliar =evento.keyCode;
      }else
            teclaAuxiliar =evento.which;

    keyDel(evento);


    if(teclaAuxiliar == 40)
                {
        upDownTable(tablaTextosFijos,listaTextosFijos,teclaAuxiliar);
                }
    if(teclaAuxiliar == 38)
                    {
        upDownTable(tablaTextosFijos,listaTextosFijos,teclaAuxiliar);
                    }
   if(teclaAuxiliar == 1)
  {
     if (comboAplicaciones.base.style.visibility == "visible" && isClickOutCombo(comboAplicaciones,coordx,coordy)) setTimeout('comboAplicaciones.ocultar()',20);
     if (comboIdiomas.base.style.visibility == "visible" && isClickOutCombo(comboIdiomas,coordx,coordy)) setTimeout('comboIdiomas.ocultar()',20);
  }
  if(teclaAuxiliar == 9)
  {
     comboAplicaciones.ocultar();
     comboIdiomas.ocultar();
  }

   }//de la funcion

  var comboAplicaciones = new Combo("Apli");	
  var comboIdiomas = new Combo("Idi");	
</script>
</body>
</html>
