<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.administracion.mantenimiento.CamposListadosParametrizablesVO"%>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Vector"%>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-15">
        <title>Mantenimiento de Procesos </title>
<!-- Estilos -->
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 1;
    int apl = 5;
    String estilo="";
    if (session.getAttribute("usuario") != null) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
        estilo=usuarioVO.getCss();

    }
%>        
<link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=estilo%>">
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>

<script type="text/javascript">

var lista = new Array();
var lista2 = new Array();
var datos = new Array();
var porcentajeAcumulado = 0;

// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
function inicializar(){
    // Se recogen los campos ya seleccionados que puedan venir de la pantalla principal
    datos = self.parent.opener.xanelaAuxiliarArgs;    
    window.focus();
    cargaTablaCampos();
}

function pulsarModificar(){ 
    //esta funcion solo modifica el listado en javascript no la bbdd
    //el vector lista es el que se modifica para la vista
    //el vecto lista2 queda preparado para grabar en la bbdd
    if (!document.forms[0].activo.checked) {
        document.forms[0].tamanoCampo.value = '0';
    }

    var id=tablaCampos.selectedIndex;
    if(tablaCampos.selectedIndex != -1){
        var tamano = document.forms[0].tamanoCampo.value;
        if (tamano == '') tamano = 0;
        if (tamano == 0) document.forms[0].activo.checked = false;

        if(tamano<=100){
                lista[id][2] = tamano + '%';
                lista2[id][2] = tamano;

                var activo;
                if (document.forms[0].activo.checked){         
                    activo='SI';
                }else{
                    activo='NO';
                }
                lista[id][3]=activo;
                lista2[id][3]=activo;
                limpiarInputs();            
        }else{
            jsp_alerta('A', '<%=descriptor.getDescripcion("msjAnchuraMenor100")%>');
        }

    }else{
        jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
    }

    tablaCampos.lineas=lista;
    tablaCampos.displayTabla();
    tablaCampos.selectLinea(id);
    
    porcentajeAcumulado = 0;
    for(i=0;i<lista.length;i++){
        if(lista[i][3]=="SI"){
            var cantidad = lista[i][2];
            var dato = cantidad.substring(0,cantidad.length-1)            
            porcentajeAcumulado = parseInt(porcentajeAcumulado) + parseInt(dato);
        }
    }//for
    
    mostrarPorcentajeAcumulado();    
}

function mostrarPorcentajeAcumulado(){
    document.getElementById("divPorcentaje").innerHTML = "<%=descriptor.getDescripcion("etiqPorcentajeAcumulado")%>: " + porcentajeAcumulado + "%";
}

function pulsarAceptar(){
    if(porcentajeAcumulado>100){
        jsp_alerta("A","<%=descriptor.getDescripcion("msjAnchuraAcumMayor100")%>");
    }else{
        //Graba los valores almacenados en el vector lista en la bbdd
        var listas=crearListas();
        document.forms[0].listaCodCampo.value = listas[0];
        document.forms[0].listaNomCampo.value = listas[1];
        document.forms[0].listTamanoCampo.value = listas[2];
        document.forms[0].listaActCampo.value = listas[3];

        self.parent.opener.retornoXanelaAuxiliar(listas);
    }
}

function crearListas() { 
    var listaCodCampo = "";
    var listaNomCampo= "";
    var listTamanoCampoAnt = new Array();
    var listTamanoCampo = "";
    var listaActCampo = "";
    var listaTamano = "";
    var listaCampoSuplementario = "";

    //lista2 contiene todos los campos de la tabla
    //lo separamos para hacer el insert    
    for (i=0; i < lista2.length; i++) {              
       // Sólo se devuelven los campos activos
       if(lista2[i][3]!="" && lista2[i][1]!="" && lista2[i][1].length>0 && lista2[i][3]=="SI"){           
            listaCodCampo +=lista2[i][0]+'§¥';
            listaNomCampo +=lista2[i][1]+'§¥';
            if(lista2[i][3]=='NO'){
                listTamanoCampoAnt[i]='0';
            }else{
                listTamanoCampoAnt[i] = lista2[i][2];
            }

            listaTamano += lista2[i][2]+ '§¥';
            listaActCampo += lista2[i][3]+'§¥';
            listaCampoSuplementario += lista2[i][4]+'§¥';           
       }
    }
            
    listaCampos = [listaCodCampo,listaNomCampo,listaTamano,listaActCampo,listaCampoSuplementario];
    return listaCampos;
} 

function calcularTamano(listTamanoCampoAnt) { 
    var listTamanoCampo = "";
    var suma=0;
    for (i=0; i < listTamanoCampoAnt.length; i++) {
        suma=suma+parseInt(listTamanoCampoAnt[i]);
    }
    
    for (i=0; i < listTamanoCampoAnt.length; i++) {
        listTamanoCampo+=(100*parseInt(listTamanoCampoAnt[i]))/suma+'§¥';
    }
    
    return listTamanoCampo;
}

function pulsarCancelar(){
    self.parent.opener.retornoXanelaAuxiliar();
}
</script>
</head>

<body class="bandaBody" onload="javascript:{pleaseWait('off');
    inicializar();}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>

<form  method="post">
    <input  type="hidden"  name="opcion" id="opcion">
    <input  type="hidden"  name="codigo" value="">
    <input  type="hidden" name="descripcion" value="">
    <input  type="hidden" name="listaCodCampo" value="">
    <input  type="hidden" name="listaNomCampo" value="">
    <input  type="hidden" name="listTamanoCampo" value="">
    <input  type="hidden" name="listaActCampo" value="">
    <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("titSelCamposVistaPend")%></div>
    <div class="contenidoPantalla">
        <table style="width: 100%">
            <tr>
                <td id="tabla"></td>
            </tr>
            <tr>
                <td align="center">
                    <table >
                        <tr>
                            <td style="width:660px">
                                <input name="codCampo" type="hidden" class="inputTextoObligatorio" size="90" maxlength="80" readOnly>
                                <input name="nomCampo" type="text" class="inputTextoObligatorio" style="width:100%" maxlength="80" readOnly>
                            </td>
                            <td style="width:90px; padding-left:5px">
                                <input name="tamanoCampo" type="text" class="inputTextoObligatorio" size="13" maxlength="3"  onKeyPress="return SoloDigitosNumericos(this);">
                            </td>
                            <td style="width:90px; margin-left:10px;text-align:center;">
                                <!-- Activo -->
                                <input type="checkbox"  id="obligatorio" name="activo" onkeyup="return xAMayusculas(this);">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3" class="etiqueta" align="center">
                                <div id="divPorcentaje" style="width:300px;"></div>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </TABLE>
        <div class="botoneraPrincipal">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbModificar")%>
                   name="botonModificar" onClick="pulsarModificar();" accesskey="M">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbLimpiar")%>
                   name="botonLimpiar" onClick="limpiarInputs();" accesskey="L">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbAceptar")%>
                   name="botonGrabar" onClick="pulsarAceptar();" accesskey="g">
            <input type= "button" class="botonGeneral" value=<%=descriptor.getDescripcion("gbCancelar")%>
                   name="botonCancelar" onClick="pulsarCancelar();" accesskey="S">
        </div>
    </div>
</form>

<script type="text/javascript">

//Creamos tablas donde se cargan las listas
tablaCampos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));

// Anadir tipo de dato a cada columna;
tablaCampos.addColumna('0','center',"<str:escape><%=descriptor.getDescripcion("etiqCodigoAsunto")%></str:escape>");
tablaCampos.addColumna('675','left',"<str:escape><%=descriptor.getDescripcion("etiqNomCampo")%></str:escape>");
tablaCampos.addColumna('100','center',"<str:escape><%=descriptor.getDescripcion("etiqAnchura")%></str:escape>");
tablaCampos.addColumna('100','center',"<str:escape><%=descriptor.getDescripcion("etiqActivo")%></str:escape>");
tablaCampos.displayCabecera=true;
tablaCampos.displayTabla();

function cargaTablaCampos(){
    rellenarListas();
    actualizarListas();
    tablaCampos.lineas=lista;
    tablaCampos.displayTabla();
}


function rellenarListas(){ 
    var auxiliar = new Array();
    var i=0;
    <c:forEach var="campo" items="${requestScope.lista_campos_pendientes_disponibles}">
       
       auxiliar[i] = ['<bean:write name="campo" property="codigo" ignore="true"/>',
                '<bean:write name="campo" property="nombreCampo" ignore="true"/>',
                '',
                '',
                '<bean:write name="campo" property="campoSuplementario" ignore="true"/>'];
       i++;
    </c:forEach>


    // Se comprueba de los campos de BD cuales no están entre los campos ya seleccionados previamente
    
    var camposNoSeleccionados = new Array();
    var contador=0;
    for(var z=0;z<auxiliar.length;z++){
        var codigoBD = auxiliar[z][0];
        if(!buscarCampo(codigoBD,datos)){
          camposNoSeleccionados[contador]= auxiliar[z];
          contador++;
        }// if
    }// for

/*
    <c:forEach var="campo" items="${requestScope.lista_campos_pendientes_disponibles}">
       lista[i] = ['<bean:write name="campo" property="codigo" ignore="true"/>','<bean:write name="campo" property="nombreCampo" ignore="true"/>','',''];
       lista2[i] = ['<bean:write name="campo" property="codigo" ignore="true"/>',
                '<bean:write name="campo" property="nombreCampo" ignore="true"/>',
                '',
                '',
                '<bean:write name="campo" property="campoSuplementario" ignore="true"/>'];
       i++;
    </c:forEach> */
    

    // Se componen los datos que se mostrarán al usuario. En primer lugar se añaden los campos ya seleccionados

    var listaFinal = new Array();
    for(var z=0;datos!=null && z<datos.length;z++){
        listaFinal[z]=[datos[z][0],datos[z][1],'','',datos[z][6]];
    }

    // Se añade a la lista final de datos los campos recuperados de la base de datos que no han sido seleccionados previamente
    for(var h=0;camposNoSeleccionados!=null && h<camposNoSeleccionados.length;h++){
        listaFinal[z] = camposNoSeleccionados[h];
        z++;
    }

    // En este momento se rellena los array lista y lista2
    for(i=0;listaFinal!=null && i<listaFinal.length;i++){
        lista[i]  = [listaFinal[i][0],listaFinal[i][1],'',''];
        lista2[i] = [listaFinal[i][0],listaFinal[i][1],'','',listaFinal[i][4]];
    }// for

}


/**
 * Compreuba si en "lista" existe un campo con el código "codigoCampo"
 * return: Un boolean
 */
function buscarCampo(codigoCampo,lista){
    var exito = false;
    for(var p=0;lista!=null && p<lista.length;p++){
        var codigoCampoSeleccionado = datos[p][0];
        if(codigoCampo==codigoCampoSeleccionado){
          exito = true;
          break;
        }
    }

    return exito;
}

function actualizarListas(){    
    for(i=0;i<lista.length;i++){
        var codigo = lista[i][0];
        var nombre = lista[i][1];

        for(j=0;datos!=null && j<datos.length;j++){
            var codigoAux = datos[j][0];
            var nombreAux = datos[j][1];

            if(codigo==codigoAux && nombre==nombreAux){
                // Tamaño o anchura del campo en el array listas
                lista[i][2] = datos[j][2] + '%';
                // Campo activo del array listas
                lista[i][3] = "SI";
                // Tamaño o anchura del campo en el array listas2
                lista2[i][2] = datos[j][2];
                // Campo activo del array listas2
                lista2[i][3] = "SI";
            }
        }//for
    }// for
    
    for(i=0;i<lista.length;i++){
        if(lista[i][3]=="SI"){
            var cantidad = lista[i][2];
            var dato = cantidad.substring(0,cantidad.length-1)
            porcentajeAcumulado = parseInt(porcentajeAcumulado) + parseInt(dato);
        }
    }//for

    mostrarPorcentajeAcumulado();   
}

function limpiarInputs(){
    document.forms[0].nomCampo.value = "";
    document.forms[0].tamanoCampo.value = "";
    document.forms[0].activo.checked=false;
}

function rellenarDatos(tableName,rowID){
    var i = rowID;
    limpiarInputs();

    if((i>=0)&& !tableName.ultimoTable){
        document.forms[0].codCampo.value = lista2[i][0];
        document.forms[0].nomCampo.value = lista2[i][1];
        document.forms[0].tamanoCampo.value = lista2[i][2];
        if(lista2[rowID][3]=='SI') {
            document.forms[0].activo.checked=true;
        }else{
            document.forms[0].activo.checked=false;
        }
    }
}
</script>
</body>
</html>
