<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@page import="es.altia.agora.technical.ConstantesDatos"%>
<%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA;
            String css="";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
      Config m_Config = ConfigServiceHelper.getConfig("common");           
      String statusBar = m_Config.getString("JSP.StatusBar"); 
      
      
  
            //=================================================================================================
            MantAnotacionRegistroForm mantARForm;
            if (request.getAttribute("MantAnotacionRegistroForm") != null) {
                mantARForm = (MantAnotacionRegistroForm) request.getAttribute("MantAnotacionRegistroForm");
            } else {
                mantARForm = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
            }
            //=================================================================================================
      
%>
<jsp:useBean id="descriptor" scope="request"
             class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>" />
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp"/>
 <title>Asientos relacionados</title>
 <%@ include file="/jsp/plantillas/Metas.jsp" %>
 
 
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
 <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
 <link rel="stylesheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
 <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
 <script type="text/javascript">
     
     
     // Los combos añaden una posicion vacia al principio por lo
     // que los datos de los vectores comienzan en el indice 1 del combo.            
     var cod_tiposAsiento = new Array();
     var desc_tiposAsiento = new Array();
     
     // Array de relaciones, posiciones de cada elemento: 0-tipo, 1-ejercicio, 2-numero
     var relaciones = new Array();
     var modificando;
     var ventanaPadre;
     var anoAsientoActual;
     var numeroAsientoActual;
     var tipoAsientoActual;
     var permitirContestar;
     var directivaUorUsuario;
     
     //// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
     
     /* Realiza las funciones de carga de la pagina */
     function inicializar(){
         var args = self.parent.opener.xanelaAuxiliarArgs;
         modificando =args[0];
         relaciones  =args[1];
         ventanaPadre=args[2];
         anoAsientoActual   =args[3];
         numeroAsientoActual=args[4];
         tipoAsientoActual  =args[5];
         var desactivarNavegacion = args[6];
         permitirContestar = args[7];
         directivaUorUsuario = args[8];
         
         if (modificando!='S') ocultarEdicion();
         if (desactivarNavegacion) ocultarNavegacion();
         
		 if(modificando=='S')
            ocultarNavegacion();
         
         // Combo de tipos de asiento
         cod_tiposAsiento = ['E','S'];                         
         desc_tiposAsiento = ['<str:escape><%=descriptor.getDescripcion("etiqTipoAnotEntrada")%></str:escape>',
                              '<str:escape><%=descriptor.getDescripcion("etiqTipoAnotSalida")%></str:escape>'];
         cargarCombo();
         
         <c:if test="${requestScope.relaciones == 'cerrarVentana'}">
         relaciones = <c:out value="${requestScope.relaciones}"/>;
         </c:if>
         // Lista de asientos                                
         cargaTablaAsientos();
     }
     
     //// FUNCIONES DE LOS BOTONES
     
     /* Carga el asiento seleccionado en la tabla */
     function pulsarVer() {         
        if(modificando!='S')
        {
            if (tablaAsientos.selectedIndex == -1) {
               jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
            // Si estamos en modo edición se confirma que se perderan los datos no grabados.
            } else if (modificando!='S' || jsp_alerta('', '<%=descriptor.getDescripcion("msjCabLanzarProc")%>')) {
               var sel = tablaAsientos.selectedIndex;
               ventanaPadre.verAnotacion(relaciones[sel][0],relaciones[sel][1],relaciones[sel][2]); // altaRE.jsp
              self.parent.opener.retornoXanelaAuxiliar();
            }
        }
     }
     
     /* Añade a las relaciones el asiento indicado */
     function pulsarAlta() {
         // Comprobamos campos obligatorios
            var ano = document.forms[0].ano.value;
            var numero = document.forms[0].numero.value;
            var tipo =  document.forms[0].codTipoAsiento.value;
            
            //#292372: No se tiene en cuenta directivaUorUsuario
           //if((directivaUorUsuario)||(!permitirContestar &&(tipoAsientoActual!=tipo))){                  
            if(!permitirContestar &&(tipoAsientoActual!=tipo)){                  
                      jsp_alerta('A', '<%=descriptor.getDescripcion("msjOpNoPermitidaUsuAplic")%>'); 
            }else
            {

                if (esNumero(ano) && esNumero(numero) && tipo != '') {
                   // Forzamos conversión a enteros para eliminar posibles ceros a la izquierda
                   ano = ano - 0;
                   numero = numero - 0;

                   // Comprobamos que no sea el asiento actual
                   if (ano == anoAsientoActual && numero == numeroAsientoActual && tipo == tipoAsientoActual) {
                       jsp_alerta('A', '<str:escape><%=descriptor.getDescripcion("msjAsientoActual")%></str:escape>');

                   // Comprobamos que no este ya en la lista de relaciones         
                   } else if (estaEnLista(ano,numero,tipo)) {
                       jsp_alerta('A', '<str:escape><%=descriptor.getDescripcion("msjYaRelacionado")%></str:escape>');

                   // LLamamos a la action para comprobar que exista
                   } else {
                      document.forms[0].opcion.value = 'existeAsiento';
                      document.forms[0].target = 'oculto';
                      document.forms[0].submit();
                   }

                } else {
                   jsp_alerta('A', '<%=descriptor.getDescripcion("msjObligTodos")%>');
                }
         }
     }
     
     /* Elimina una relación de la tabla */
     function pulsarEliminar() {
           if (tablaAsientos.selectedIndex == -1) {
              jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');        
           } else {
              var sel = tablaAsientos.selectedIndex;
              
              //#292372: No se tiene en cuenta directivaUorUsuario
                //if((directivaUorUsuario)||(!permitirContestar &&(tipoAsientoActual!=relaciones[sel][0]))){                  
                if(!permitirContestar &&(tipoAsientoActual!=relaciones[sel][0])){                  
                       jsp_alerta('A', '<%=descriptor.getDescripcion("msjOpNoPermitidaUsuAplic")%>'); 
                  }else
                  {
              nuevas = new Array();
              cont = 0;
              for(i=0; i<relaciones.length; i++) {
                  if (tablaAsientos.selectedIndex != i) {
                      nuevas[cont] = relaciones[i];
                      cont++;
                  }
              }
              relaciones = nuevas;
              cargaTablaAsientos();
           }  
         }
     }
     
     /* Cierra la ventana de dialogo */
     function pulsarSalir() {
         self.parent.opener.retornoXanelaAuxiliar(String(relaciones));
     }
     
     //// FUNCIONES DEL FRAME OCULTO
     
     /* Inserta la relación actual en la lista */
     function insertarRelacion() {
         var ano = document.forms[0].ano.value;
         var numero = document.forms[0].numero.value;
         var tipo =  document.forms[0].codTipoAsiento.value;
         // Forzamos conversión a enteros para eliminar posibles ceros a la izquierda
         ano = ano - 0;
         numero = numero - 0;
         relaciones[relaciones.length] = [tipo, ano, numero];
         cargaTablaAsientos();
         // Limpiamos los campos del formulario
         document.forms[0].ano.value = '';
         document.forms[0].numero.value = '';
         comboTipoAsiento.selectItem(0);
     }
     
     
     /* Alerta de que un asiento no existe */
     function alertarAsientoInexistente() {
        jsp_alerta('A', '<str:escape><%=descriptor.getDescripcion("msjAsientoNoExiste")%></str:escape>');
     }
     
     //// FUNCIONES AUXILIARES
     
     /* Oculta los botones de edicion y los campos del formulario */
     function ocultarEdicion() { 
        // Campos del formulario
        document.all.nuevaRelacion.style.visibility = 'hidden';
        // Botones
        document.all.cmdAlta.style.visibility = 'hidden';
        document.all.cmdEliminar.style.visibility = 'hidden';
        document.all.cmdBuscar.style.visibility = 'hidden';        
     }

     /* Oculta el boton de ver anotacion */
     function ocultarNavegacion() {
        document.all.cmdVer.style.visibility = 'hidden';
     }

     /* Devuelve true si el String pasado es un numero entero positivo */
     function esNumero(x) {
        var RegExp = /^(\d+)$/;
        return(x.match(RegExp));
     }
     
     /* Comprueba si el asiento ya existe en los relacionados */
     function estaEnLista(ano, numero, tipo) {
        for(var i=0; i<relaciones.length; i++) {
            if (relaciones[i][0] == tipo && relaciones[i][1] == ano && relaciones[i][2] == numero)
                return true;
        }
        return false;
     }
     
     //// EVENTOS
     
     /* Controla la pulsacion de un doble clic sobre alguna de las filas de la tabla*/     
     function checkDobleClic(){         
         if(tablaAsientos.selectedIndex>-1 && !tablaAsientos.ultimoTable) pulsarVer();
     }
     
    function pulsarBuscar(){
        // Comprobamos campos obligatorios
        var ano = document.forms[0].ano.value;
        var numero = document.forms[0].numero.value;
        var tipo =  document.forms[0].codTipoAsiento.value;
        var datoAno= new Array();
        datoAno[0]=ano;
        datoAno[1]=numero;
        datoAno[2]=tipo;
        
        if (tipo != '') {
             //#292372: No se tiene en cuenta directivaUorUsuario
           //if((directivaUorUsuario)||(!permitirContestar &&(tipoAsientoActual!=tipo))){                  
            if(!permitirContestar &&(tipoAsientoActual!=tipo))  
                jsp_alerta('A', '<%=descriptor.getDescripcion("msjOpNoPermitidaUsuAplic")%>'); 
             else {
                // Forzamos conversión a enteros para eliminar posibles ceros a la izquierda
                ano = ano - 0;
                numero = numero - 0;

                // Comprobamos que no sea el asiento actual
                 if (ano == anoAsientoActual && numero == numeroAsientoActual && tipo == tipoAsientoActual && ano != '' && numero != '') 
                    jsp_alerta('A', '<str:escape><%=descriptor.getDescripcion("msjAsientoActual")%></str:escape>');
                // Comprobamos que no este ya en la lista de relaciones         
                else
                    //puedo buscar abrir ventana con los datos que me pasan
                    abrirXanelaAuxiliar(APP_CONTEXT_PATH + "/jsp/sge/mainVentana.jsp?source=" + 
                            APP_CONTEXT_PATH +"/MantAnotacionRegistro.do?opcion=busquedaEntradas&ano="+ano+
                            "&numero="+numero+"&tipo="+tipo,datoAno,
                            'width=999,height=600',function(datos){
                                if(datos!=undefined){
                                    datos[2] = document.forms[0].codTipoAsiento.value;
                                       if (estaEnLista(datos[0], datos[1], datos[2])) {
                                            jsp_alerta('A', '<str:escape><%=descriptor.getDescripcion("msjYaRelacionado")%></str:escape>');
                                       }else{
                                            relaciones[relaciones.length] = [datos[2], datos[0], datos[1]];
                                            cargaTablaAsientos();
                                            // Limpiamos los campos del formulario
                                            document.forms[0].ano.value = '';
                                            document.forms[0].numero.value = '';
                                            comboTipoAsiento.selectItem(0);
                                       }
                                }
                                recuperaForm();            
                            });
                }
        } else 
            jsp_alerta('A', '<str:escape><%=descriptor.getDescripcion("msjTipoAsientoObli")%></str:escape>');
    }
     
     function recuperaForm() {
         document.forms[0].opcion.value = 'cerrarBusquedaRelaciones';
         document.forms[0].action="<html:rewrite page='/MantAnotacionRegistro.do'/>";
         document.forms[0].target = 'oculto';
         document.forms[0].submit();    
     }
     
 </script>
</head>
    
<body class="bandaBody" onload="inicializar();">
<html:form method="post" action="MantAnotacionRegistro">
<input type="hidden" name="opcion"> 
<div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_Rel")%></div>
<div class="contenidoPantalla">
    <div style="float:left;width:80%">
          <!-- Tabla de asientos relacionados -->
        <div id="tabla" ondblclick="javascript:checkDobleClic();"></div>
          <!-- Nueva relacion -->                                               
          <div id="nuevaRelacion" name="nuevaRelacion" style="visibility:visible;">
            <span class="etiqueta"><%=descriptor.getDescripcion("etiqNuevaRel")%>:</span>                                              
            <!-- Campos ejercicio y numero -->
            <span class="columnP">
              <input type="text" class='inputTexto' style="width:15%" maxlength="4" name="ano"
                          onkeyup = "return SoloDigitosNumericos(this);"
                          onfocus="this.select();" />
              <input type="text" class='inputTexto' style="width:20%" maxlength="8" name="numero"
                          onkeyup = "return SoloDigitosNumericos(this);"
                          onfocus="this.select();" />
            </span>
            <!-- Combo de tipos de asiento -->
            <input type="hidden" name="codTipoAsiento"/>
            <input type="text" class="inputTexto" name="descTipoAsiento" style="width:25%" readonly="true"/>
            <A href="javascript:{}" style="text-decoration:none;" id="anchorTipoAsiento" name="anchorTipoAsiento">
                <span class="fa fa-chevron-circle-down" aria-hidden="true"  id="desp" name="botonTipoAsiento" style="cursor:hand;"></span>
            </A>
        </div>
    </div>
    <!-- BOTONES DE VER, AÑADIR, ELIMINAR -->
    <div style="float:left;margin-left:1%;width:19%">
        <input type= "button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbVer")%>" name="cmdVer" onClick="pulsarVer();">
        <input type= "button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbEliminar")%>" name="cmdEliminar" onClick="pulsarEliminar();">
        <input type="button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbAnadir")%>" name="cmdAlta" onClick="pulsarAlta();">
        <input type= "button" class="botonGeneral" style="margin-top:5px" value="<%=descriptor.getDescripcion("gbBuscar")%>" name="cmdBuscar" onClick="pulsarBuscar();">
    </div>
    <div class="botoneraPrincipal">
        <input type="button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbSalir")%>"
               name="cmdSalir" onClick="pulsarSalir();" accesskey="S">
    </div>   
</div>   
</html:form>
<script type="text/javascript">
            // Tabla de asientos relacionados
            var tablaAsientos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tabla'));
            
            tablaAsientos.addColumna('200','center',"<str:escape><%=descriptor.getDescripcion("titNumeroRel")%></str:escape>");
            tablaAsientos.displayCabecera=true;
            tablaAsientos.lineas = new Array();
            tablaAsientos.displayTabla();
                
            function cargaTablaAsientos() {
                // Se construye la lista de descripciones de asientos
                var descsAsientos = new Array();
                var descripcion;
                for(i=0; i<relaciones.length; i++) {
                    descripcion = relaciones[i][1] + "/" + relaciones[i][2] + " - ";
                    if (relaciones[i][0] == 'E')
                        descripcion += desc_tiposAsiento[0];
                    else
                        descripcion += desc_tiposAsiento[1];
                    descsAsientos[i]=[descripcion];
                }
                // Se muestra la lista
                tablaAsientos.lineas=descsAsientos;
                tablaAsientos.displayTabla();
            }
                
            // Combo de unidades organicas
            var comboTipoAsiento = new Combo("TipoAsiento");
            //comboTipoAsiento.change = function() { alert(document.forms[0].codTipoAsiento.value); }
                
            function cargarCombo() {
                comboTipoAsiento.addItems(cod_tiposAsiento,desc_tiposAsiento);
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

            document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }


	if (teclaAuxiliar == 1){
            if (comboTipoAsiento.base.style.visibility == "visible" && isClickOutCombo(comboTipoAsiento,coordx,coordy)) setTimeout('comboTipoAsiento.ocultar()',20);
        }
        if (teclaAuxiliar == 9){
            comboTipoAsiento.ocultar();
        }
}
        </script>
    </body>
</html>
