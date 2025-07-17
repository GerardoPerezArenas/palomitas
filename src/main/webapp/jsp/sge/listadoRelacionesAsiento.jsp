<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 1;
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idioma = usuarioVO.getIdioma();
            }
%>
<jsp:useBean id="descriptor" scope="request"
             class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
             type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor" property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>" />
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
 <title>Asientos relacionados</title>
 <jsp:include page="/jsp/plantillas/Metas.jsp"/>
 <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
 <link rel="stylesheet" type="text/css" href="<c:url value='/css/estilo.css'/>">
 <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
 <script type="text/javascript">  
     
     var desc_tiposAsiento = new Array();
     
     // Array de relaciones, posiciones de cada elemento: 0-tipo, 1-ejercicio, 2-numero
     var relaciones = new Array();
     var modificando;
     var ventanaPadre;
     var modoConsulta;
     
     //// FUNCIONES DE CARGA E INICIALIZACION DE DATOS
     
     /* Realiza las funciones de carga de la pagina */
     function inicializar(){
         var args = top.self.parent.opener.xanelaAuxiliarArgs;
         relaciones  =args[0];
         ventanaPadre=args[1];
         modoConsulta=args[2];
         
         desc_tiposAsiento = ['<str:escape><%=descriptor.getDescripcion("etiqTipoAnotEntrada")%></str:escape>',
                              '<str:escape><%=descriptor.getDescripcion("etiqTipoAnotSalida")%></str:escape>'];
                       
         // Lista de asientos                                
         cargaTablaAsientos();
         // Desactivar boton si estamos en buzon de entrada
         if (!modoConsulta) document.all.cmdVer.disabled = true; document.getElementById('cmdVer').style.color = '#CCCCCC';
     }
     
     //// FUNCIONES DE LOS BOTONES
     
     /* Carga el asiento seleccionado en la tabla */
     function pulsarVer() {
        if (tablaAsientos.selectedIndex == -1) {
           jsp_alerta('A', '<%=descriptor.getDescripcion("msjNoSelecFila")%>');
        } else { 
           var sel = tablaAsientos.selectedIndex;
           ventanaPadre.verAnotacion(relaciones[sel][0],relaciones[sel][1],relaciones[sel][2]); // consultaAsiento.jsp
           self.parent.opener.retornoXanelaAuxiliar();
        }
     }
            
     /* Cierra la ventana de dialogo */
     function pulsarSalir() {
         self.parent.opener.retornoXanelaAuxiliar();
     }
     
     //// EVENTOS
     
     /* Controla la pulsacion de un doble clic sobre alguna de las filas de la tabla*/     
     function checkDobleClic(){         
         if (modoConsulta)         
           if(tablaAsientos.selectedIndex>-1 && !tablaAsientos.ultimoTable) pulsarVer();
     }
     
 </script>
</head>
    
<body class="bandaBody" onload="inicializar();">
    <html:form method="post" action="MantAnotacionRegistro">
        <input type="hidden" name="opcion"> 
        <div id="titulo" class="txttitblanco"><%=descriptor.getDescripcion("tit_Rel")%></div>
        <div class="contenidoPantalla">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px">
               <td width="70%">                                                                                        
                 <table>

                  <!-- Tabla de asientos relacionados -->
                  <tr>
                      <td height="70%" colspan="2" align="left" id="tabla" ondblclick="javascript:checkDobleClic();"></td>
                  </tr>

                 </table>                                                                                                                                       
               </td>

              <!-- BOTONES DE VER, AÑADIR, ELIMINAR -->
              <td width="30%" style="vertical-align:top;">
                <table cellpadding="0px" cellspacing="0px">
                  <tr><td height="20px"></td></tr>
                  <tr>
                     <td>
                         <input type= "button" class="botonGeneral" value="<%=descriptor.getDescripcion("gbVer")%>" name="cmdVer" id="cmdVer" onClick="pulsarVer();">
                     </td> 
                  </tr>                                                
                </table>
              </td>
            </table>
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
                
        </script>
    </body>
</html>
