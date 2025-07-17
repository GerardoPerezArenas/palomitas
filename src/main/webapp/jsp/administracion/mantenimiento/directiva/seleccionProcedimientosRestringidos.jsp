<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Vector"%>
<%@page import="java.lang.Integer"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.business.administracion.mantenimiento.PermisoProcedimientosRestringidosVO"%>
<%@page import="es.altia.agora.business.util.LabelValueTO"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<html:html>
 <%
    
    int idioma = 1;
    int aplicacion = 20;
    UsuarioValueObject usuario = null;
    String entidad = "";
    String usu = "";
    if (session != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        if (usuario != null) {
            idioma = usuario.getIdioma();
            aplicacion = usuario.getAppCod();
            entidad = usuario.getEnt();
            usu = usuario.getNombreUsu();
        }
    }

    String parametros [] = usuario.getParamsCon();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    ArrayList idiomas = (ArrayList)session.getAttribute("listaIdiomas");
%>
<head>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%= aplicacion %>" />

    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/windows.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/sge_basica.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';    
    var comboOrganizacion;
    var comboEntidad;
    var comboProcedimientos;
    var tabProcedimientos;
    
    var listaProcedimientosTabla = new Array();
    var listaCodProcedimientos = new Array();
    var listaDescProcedimientos = new Array();
    var listaCodOrganizaciones = new Array();
    var listaDescOrganizaciones = new Array();
    var listaCodEntidades  = new Array();
    var listaDescEntidades = new Array();
    var codOrganizacionUsuario;

    // La siguiente variable contiene la lista de procedimientos seleccionados para el usuario
    var listaProcedimientosSeleccionados = new Array();
    var conProc=0;

    var argVentana;
    <%
        ArrayList<PermisoProcedimientosRestringidosVO> procedimientos = (ArrayList<PermisoProcedimientosRestringidosVO>)request.getAttribute("procedimientos_restringidos");
        Vector organizaciones  = (Vector)request.getAttribute("organizaciones_seleccion_procedimiento");

        for(int i=0;organizaciones!=null && i<organizaciones.size();i++)
        {
                GeneralValueObject org = (GeneralValueObject)organizaciones.get(i);
     %>
            listaCodOrganizaciones[conProc]  = ['<%=(String)org.getAtributo("codigo")%>'];
            listaDescOrganizaciones[conProc] = ['<%=(String)org.getAtributo("descripcion")%>'];
            conProc++;
    <%
        }// for
     %>


     conProc = 0;
     <%
        for(int i=0;procedimientos!=null && i<procedimientos.size();i++)
        {
            PermisoProcedimientosRestringidosVO org = (PermisoProcedimientosRestringidosVO)procedimientos.get(i);
            String descOrganizacion   = org.getDescOrganizacion();
            String descEntidad           = org.getDescEntidad();
            String descProcedimiento = org.getDescProcedimiento();
            String codProcedimiento   = org.getCodProcedimiento();
            String codEntidad             = org.getCodEntidad();
            String codOrganizacion     = org.getCodMunicipio();

     %>         
            listaProcedimientosSeleccionados[conProc] = ['<%=codOrganizacion%>','<%=descOrganizacion%>','<%=codEntidad%>','<%=descEntidad%>','<%=codProcedimiento%>','<%=descProcedimiento%>'];
            listaProcedimientosTabla[conProc] = ['<%=descOrganizacion%>','<%=descEntidad%>','<%=descProcedimiento%>'];
            conProc++;
    <%
        } // for
     %>


    function inicializar(){

        argVentana = self.parent.opener.xanelaAuxiliarArgs;
        tabProcedimientos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaProcedimientos'));

        tabProcedimientos.addColumna('200','center','<%=descriptor.getDescripcion("gEtiq_Organizacion")%>');
        tabProcedimientos.addColumna('200','center','<%=descriptor.getDescripcion("gEtiq_Entidad")%>');
        tabProcedimientos.addColumna('450','center','<%=descriptor.getDescripcion("gbProcRestringidos")%>');

        var aux = argVentana[0];
        var orgsUsuario = new Array();
        var operacion = argVentana[1];
        
        if(codOrganizacionUsuario!=""){
            orgsUsuario = aux.split("§¥");            
            if(orgsUsuario!=null && orgsUsuario.length==2){
                codOrganizacionUsuario = orgsUsuario[0];
                document.forms[0].codOrganizacion.value=codOrganizacionUsuario;
                // Se busca el código en la lista de organizaciones
                for(z=0;z<listaCodOrganizaciones.length;z++){
                    if(listaCodOrganizaciones[z][0]==codOrganizacionUsuario){
                      break;
                    }
                }

                document.forms[0].descOrganizacion.value = listaDescOrganizaciones[z];
                comboOrganizacion.deactivate();
                filtrarPorOrganizacion();
            }
        }



        
        
            // Si no se han recuperado de procedimientos de la base de datos, se actualizan las listas con los que vengan como argumentos de la ventana
        
        if(argVentana!=null && argVentana.length>0)
        {
            // Si se está modificando el usuario, se hace caso a los argumentos que lleguen de la ventana
            var contador = 0;
                    
            var arrayAuxiliarTabla = new Array();
            var arrayAuxiliarSeleccionados = new Array();

            for(i=2;operacion!=null && operacion=="M" && argVentana!=null && i<=argVentana.length;i++){
                                
                if(argVentana[i]!="" && argVentana[i]!=undefined && argVentana[i]!="undefined"){
                    var datosArgumentos = argVentana[i].split(";");
                    var codigoOrganizacion          = datosArgumentos[0];
                    var descripcionOrganizacion   = datosArgumentos[1];
                    var codigoEntidad                  = datosArgumentos[2];
                    var descripcionEntidad           = datosArgumentos[3];
                    var codigoProcedimiento        = datosArgumentos[4];
                    var descripcionProcedimiento = datosArgumentos[5];
                    arrayAuxiliarTabla[contador]    = [descripcionOrganizacion,descripcionEntidad,descripcionProcedimiento];
                    arrayAuxiliarSeleccionados[contador] = [codigoOrganizacion,descripcionOrganizacion,codigoEntidad,descripcionEntidad,codigoProcedimiento,descripcionProcedimiento];
                    contador++;
                }
            }// for

            //if(arrayAuxiliarTabla!=null && arrayAuxiliarTabla.length>=1)
                listaProcedimientosTabla = arrayAuxiliarTabla;

            //if(arrayAuxiliarSeleccionados!=null && arrayAuxiliarSeleccionados.length>=1)
                listaProcedimientosSeleccionados = arrayAuxiliarSeleccionados;

        }
        tabProcedimientos.height = 250;
        tabProcedimientos.lineas = listaProcedimientosTabla;
        tabProcedimientos.displayCabecera=true;
        tabProcedimientos.displayTabla();        
   }

    function callFromTableTo(rowID,tableName){
          fila=parseInt(rowID);
     }

    function refrescarProcedimientos() {
        tabProcedimientos.displayTabla();
    }

    function refrescarLista(){
        location.reload(true);
    }

    function rellenarDatos(tableName,listName){
    }


    /**
     * Se encarga de enviar la petición al servidor para recuperar los procedimientos restringidos de una determinada organización
     */
    function filtrarPorOrganizacion(){ 
        var codOrganizacion = document.getElementById("codOrganizacion").value;
        var descOrganizacion = document.getElementById("descOrganizacion").value;

        comboEntidad.selectItem(-1);
        comboProcedimientos.selectItem(-1);
    
        if(codOrganizacion!=null && codOrganizacion!=""){
            // Se envia la petición al servidor
            document.forms[0].target = "oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=cargarListadoEntidades&codOrganizacion=" + codOrganizacion;
            document.forms[0].submit();        
        }
    }

    function cargarComboEntidades(codigos,descripciones){        
        comboEntidad.addItems(codigos,descripciones);
        comboEntidad.change = filtrarPorEntidad;
    }

    function cargarComboProcedimientos(codigos,descripciones){
        comboProcedimientos.addItems(codigos,descripciones);
    }



    function cargarTablaProcedimientos(codigos,descripciones){
       tabProcedimientos.lineas = descripciones;
       tabProcedimientos.displayCabecera=true;
       tabProcedimientos.displayTabla();
    }

    function filtrarPorEntidad(){        
        var codOrganizacion = document.forms[0].codOrganizacion.value;
        var codEntidad         = document.forms[0].codEntidad.value;
        
        if(codEntidad!=null && codOrganizacion!=null && codOrganizacion.length>0 && codEntidad.length>0){
            document.forms[0].target = "oculto";
            document.forms[0].action = "<%=request.getContextPath()%>/administracion/UsuariosGrupos.do?opcion=cargarProcedimientosRestringidos&codOrganizacion=" + codOrganizacion
             + "&codEntidad=" + codEntidad;            
            document.forms[0].submit();
        }
    }

    function pulsarAceptar(){  
        self.parent.opener.retornoXanelaAuxiliar(tratarListaProcedimientosSeleccionados());
    }

    function tratarListaProcedimientosSeleccionados(){
        var salida = new Array();
        for(i=0;i<listaProcedimientosSeleccionados.length;i++){
            listaProcedimientosSeleccionados[i];
            salida[i] = listaProcedimientosSeleccionados[i][0] + ";" + listaProcedimientosSeleccionados[i][1] + ";" + listaProcedimientosSeleccionados[i][2]
                            + ";" + listaProcedimientosSeleccionados[i][3] + ";" + listaProcedimientosSeleccionados[i][4] + ";" + listaProcedimientosSeleccionados[i][5];
        }
        
        return salida;
    }

    function cerrar(){
        self.parent.opener.retornoXanelaAuxiliar();
    }


    function pulsarAnadirProcedimiento(){
        
        var codOrganizacion = document.forms[0].codOrganizacion.value;
        var codEntidad = document.forms[0].codEntidad.value;
        var codProcedimiento = document.forms[0].codProcedimiento.value;

        var descOrganizacion = document.forms[0].descOrganizacion.value;
        var descEntidad = document.forms[0].descEntidad.value;
        var descProcedimiento = document.forms[0].descProcedimiento.value;

        if(codProcedimiento!="" && codEntidad!="" && codOrganizacion!=""){            
            var longitud = listaProcedimientosTabla.length;

            var continuar = true;
            /** Se comprueba si ya existe el procedimiento entre los seleccionados */
            for(i=0;i<listaProcedimientosSeleccionados.length;i++){
                var auxCodOrg = listaProcedimientosSeleccionados[i][0];
                var auxCodEnt = listaProcedimientosSeleccionados[i][2];
                var auxCodPro = listaProcedimientosSeleccionados[i][4];
                if(auxCodOrg==codOrganizacion && auxCodEnt==codEntidad && auxCodPro==codProcedimiento){
                    continuar = false;
                    break;
                }
            }// for

            if(continuar){
                /** Se añade el nuevo procedimiento en la lista de procedimientos  seleccionados */
                //listaProcedimientosSeleccionados[longitud] = [codOrganizacion,codEntidad,codProcedimiento];
                listaProcedimientosSeleccionados[longitud] = [codOrganizacion,descOrganizacion,codEntidad,descEntidad,codProcedimiento,descProcedimiento];


                /** Se añade el nuevo procedimiento en la tabla **/
                listaProcedimientosTabla[longitud] = [descOrganizacion,descEntidad,descProcedimiento];
                tabProcedimientos.lineas = listaProcedimientosTabla;
                tabProcedimientos.displayCabecera=true;
                tabProcedimientos.displayTabla();
            }else
                jsp_alerta("A","<%=descriptor.getDescripcion("msgExisteProcRestringido")%>");
        }
    }


    function pulsarEliminarProcedimiento(){

       if(tabProcedimientos.selectedIndex != -1) {
           
           var aux = new Array();
           var contador = 0;
           
           // Se actualiza la lista de procedimientos seleccionados
           for(i=0;i<listaProcedimientosSeleccionados.length;i++){

                if(i!=tabProcedimientos.selectedIndex){
                    aux[contador] = listaProcedimientosSeleccionados[i];
                    contador++;
                }
            }// for

            listaProcedimientosSeleccionados = aux;

            // Se actualiza la lista de procedimientos de la tabla de procedimientos
            var auxt = new Array();
            contador = 0;
            for(i=0;i<listaProcedimientosTabla.length;i++){

                if(i!=tabProcedimientos.selectedIndex){
                    auxt[contador] = listaProcedimientosTabla[i];
                    contador++;
                }
            }// for
            listaProcedimientosTabla = auxt;
            tabProcedimientos.lineas = listaProcedimientosTabla;
            tabProcedimientos.displayCabecera=true;
            tabProcedimientos.displayTabla();
           
       }else
            jsp_alerta("A","<%=descriptor.getDescripcion("msgSeleccProcRestringido")%>");        
    }



    //document.onmouseup = checkKeys;
    
   function checkKeysLocal(evento,tecla){
    var aux=null;
    if(window.event)
        aux = window.event;
    else
        aux = evento;

    var tecla = 0;
    if(aux.keyCode)
    tecla = aux.keyCode;
    else
    tecla = aux.which;

    keyDel(aux);
    if (tecla == 1){
        if (comboOrganizacion.base.style.visibility == "visible") setTimeout('comboOrganizacion.ocultar()',20);
        if (comboEntidad.base.style.visibility == "visible") setTimeout('comboEntidad.ocultar()',20);
        if (comboProcedimientos.base.style.visibility == "visible") setTimeout('comboProcedimientos.ocultar()',20);

    } // if

    if (tecla == 9){
        comboOrganizacion.ocultar();
        comboEntidad.ocultar();
        comboProcedimientos.ocultar();        
    }// if


    if(aux.button == 9){
        comboOrganizacion.ocultar();
        comboEntidad.ocultar();
        comboProcedimientos.ocultar();
    }

    }



</script>
</head>
<title><%=descriptor.getDescripcion("gEtiqSelProcRestringidos")%></title>
<BODY  onload="inicializar();">
    <html:form action="/administracion/UsuariosGrupos.do" method="POST">
        <div class="txttitblanco"><%=descriptor.getDescripcion("gEtiqSelProcRestringidos")%></div>
        <div class="contenidoPantalla" valign="top" style="padding-top: 5px; padding-bottom: 30px;width:950px;" >
            <table width="100%">
             <tr>
                <td class="etiqueta" align="center">

                    <table border="0" align="center">
                    <tr class="etiqueta">
                        <td align="left"><%=descriptor.getDescripcion("gEtiq_Organizacion")%></td>
                        <td>
                            <input type="text" name="codOrganizacion" id="codOrganizacion" size="5" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descOrganizacion"  id="descOrganizacion" size="43" class="inputTextoObligatorio" readonly="true" value=""/>
                            <a href="" id="anchorOrganizacion" name="anchorOrganizacion">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                     name="botonAplicacion" style="cursor:hand;"></span>
                            </a>
                        </td>
                    </tr>
                   <!-- Combo entidad -->
                    <tr>
                        <td class="etiqueta" align="left">
                            <%=descriptor.getDescripcion("gEtiq_Entidad")%>
                            &nbsp;&nbsp;
                        </td>

                        <td colspan="2">
                            <input type="text" name="codEntidad" id="codEntidad" size="5" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descEntidad"  id="descEntidad" size="43" class="inputTextoObligatorio" readonly="true" value=""/>

                            <a href="" id="anchorEntidad" name="anchorEntidad">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                     name="botonAplicacion" style="cursor:hand;"></span>
                            </a>
                        </td>
                    </tr>
                    <!-- Combo procedimiento -->
                    <tr>
                        <td class="etiqueta" align="left">
                            <%=descriptor.getDescripcion("gb_etiqProcedimiento")%>
                            &nbsp;&nbsp;
                        </td>

                        <td>
                            <input type="text" name="codProcedimiento"   id="codProcedimiento" size="5" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descProcedimiento"  id="descProcedimiento" size="43" class="inputTextoObligatorio" readonly="true" value=""/>

                            <a href="" id="anchorProcedimiento" name="anchorProcedimiento">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                     name="botonAplicacion" style="cursor:hand;"></span>
                            </a>
                        </td>
                    </tr>                                                    
                    </table>


                </td>

             </tr>                                             
             <tr>
                 <td align="center" colspan="2">
                     <table align="center" >
                     <tr>
                           <td id="tablaProcedimientos" style="width: 100%"></td>
                     </tr>
                     </table>
                 </td>
             </tr>
             </tr>
            <tr>
                <td align="center">
                    <table border="0" style="width:868px;" align="center">
                    <tr>
                        <td align="right">
                            <input type="button" onclick="javascript:pulsarAnadirProcedimiento();" name="btnAceptar" class="botonGeneral" id="btnAceptar" value="<%=descriptor.getDescripcion("gb_etiqAceptar")%>"/>
                            &nbsp;
                            <input type="button" onclick="javascript:pulsarEliminarProcedimiento();" name="btnEliminar" class="botonGeneral" id="btnEliminar" value="<%=descriptor.getDescripcion("gb_etiqEliminar")%>"/>
                        </td>
                    </tr>
                    </table>


                </td>
            </tr>
        </table>
        <div class="botoneraPrincipal"> 
            <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbAceptar")%>"
                   name="botonGenerar" onClick="pulsarAceptar();">
           <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbCancelar")%>"
                      name="botonImportar" onClick="cerrar();">
         </div>
    </div>
<script type="text/javascript">
    comboOrganizacion = new Combo("Organizacion");
    comboOrganizacion.addItems(listaCodOrganizaciones, listaDescOrganizaciones);    
    comboOrganizacion.change = filtrarPorOrganizacion;

    comboEntidad = new Combo("Entidad");
    comboEntidad.addItems(listaCodEntidades,listaDescEntidades);

    comboProcedimientos = new Combo("Procedimiento");
    comboProcedimientos.addItems(listaCodProcedimientos,listaDescProcedimientos);

</script>

</html:form>
    </BODY>

</html:html>
