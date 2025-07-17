<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@page import="java.text.SimpleDateFormat"%>

<%	UsuarioValueObject usuarioVO = new UsuarioValueObject();
	int idioma=1;
	int apl=4;
	if (session.getAttribute("usuario") != null){
		usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
		apl = usuarioVO.getAppCod();
		idioma = usuarioVO.getIdioma();
	}
    String fechaFormateada= null;
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    FichaExpedienteForm expForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
    if (expForm!= null && expForm.getComunicacionVO()!= null && expForm.getComunicacionVO().getFecha()!= null){
        Calendar fecha = expForm.getComunicacionVO().getFecha();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fechaFormateada =  sdf.format(fecha.getTime());
    }

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<html>
<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />
    <title>Comunicaci�n</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/estiloie9.css'/>" media="screen" >
    <script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaBusquedaTerceros.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>

    <script type="text/javascript">
        function cerrarVentana(){
          if(navigator.appName=="Microsoft Internet Explorer") {
                window.parent.window.opener=null;
                window.parent.window.close();
          } else if(navigator.appName=="Netscape") {
                top.window.opener = top;
                top.window.open('','_parent','');
                top.window.close();
           }else{
               window.close();
           }
        }

        function descargarFichero(idComunicacion,idAdjunto){
           if (idComunicacion!= null && idComunicacion!= undefined && idComunicacion!= "" && idAdjunto!=null && idAdjunto!=undefined && idAdjunto!=""){

                var source = "<html:rewrite page='/DescargarAdjuntoComunicacion'/>?opcion=descargar&idCom="+ idComunicacion + "&idAdj="+ idAdjunto;

                window.location =source;
           }else{
                jsp_alerta('A','<%=descriptor.getDescripcion("msjDocComuIncorrecto")%>');
           }
        }


       function verificarFirma(){
           var idComu = document.getElementsByName("comunicacionVO.id")[0].value;
           var source = "<%=request.getContextPath()%>/sge/FichaExpediente.do?opcion=verificarFirmaComunicacion" +
                     "&idCom="+idComu;
           abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,[],
	'width=600,height=320,status=no',function(){});
       }

        function verificarFirmaDocumento(idAdjunto,idComunicacion, idFirma){
          var source = "<%=request.getContextPath()%>/sge/FichaExpediente.do?opcion=verificarFirmaDocComunicacion" +
                     "&idAdj="+idAdjunto+"&idCom="+idComunicacion;
           abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,[],
	'width=600,height=320,status=no',function(){});
       }

       function descargarFicheroExterno(codigo){ 
           if(codigo!=null && codigo!=""){
                var url = "<%=request.getContextPath()%>/Notificacion.do?opcion=descargarArchivoExternoNotificacion&codigo=" + codigo + "&descargarFichaExpediente=1";
                // Se procede a eliminar el fichero de la lista de ficheros adjuntos externos de la notificaci�n
                document.forms[0].action = url;
                document.forms[0].submit();
           }
       }

       function descargarFicheroTramitacion(unidad,codTramite,ocuTramite,numExpediente,nombre){            
           if(unidad!="" && codTramite!="" && ocuTramite!="" && numExpediente!=""){
                var url = "<%=request.getContextPath()%>/Notificacion.do?opcion=descargarDocumentoTramitacion&unidad=" + unidad + "&codTramite=" + codTramite + "&ocuTramite=" + ocuTramite + "&numExpediente=" + numExpediente + "&nombre=" + escape(nombre);                
                // Se procede a eliminar el fichero de la lista de ficheros adjuntos externos de la notificaci�n
                document.forms[0].action = url;
                document.forms[0].submit();
           }
        }
    </script>
</head>

<body class="bandaBody" style="width: 100%">


<html:form method="POST" action="/Notificacion.do" enctype="multipart/form-data">
      <div class="txttitblanco"> <%=descriptor.getDescripcion("msgDetalleComunicacion")%></div>        
        <div class="contenidoPantalla">
            <table  width="100%"  border="0">
                <tr>
                    <td class="sub3titulo" colspan="3"><%=descriptor.getDescripcion("etiqDatosExpediente")%></td>
                </tr>

                <tr>
                    <td width="15%" class="etiqueta"><strong><%=descriptor.getDescripcion("etiqProcedimientoNotif")%>:</strong></td>
                    <td width="80%" class="etiqueta">
                        <c:out value="${DetalleNotificacion.nombreExpediente}"/>
                    </td>

                </tr>
                <tr>
                    <td width="15%" class="etiqueta"><strong><%=descriptor.getDescripcion("etiq_numExp")%>:</strong></td>
                    <td width="80%" class="etiqueta">                        
                        <c:out value="${DetalleNotificacion.numExpediente}"/>                        
                    </td>                    
                </tr>
                <tr>
                    <td width="15%" class="etiqueta"><strong><%=descriptor.getDescripcion("gEtiq_tramiteNotif")%>:</strong></td>
                    <td width="80%" class="etiqueta">
                        <c:out value="${DetalleNotificacion.nombreTramite}"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>
                
                <tr>
                    <td class="sub3titulo" colspan="3"><%=descriptor.getDescripcion("etiqDatosNotif")%></td>
                </tr>
                <tr>
                    <td width="15%" class="etiqueta">
                        <strong>
                        <%=descriptor.getDescripcion("etiqActoNotificacion")%>
                        </strong>
                    </td>
                    <td width="80%" class="etiqueta">
                        <c:out value="${DetalleNotificacion.actoNotificado}"/>                        
                    </td>
                </tr>
                <tr>
                    <td width="15%" class="etiqueta">
                        <strong>
                        <%=descriptor.getDescripcion("etiqTextoNotificacion")%>
                        </strong>
                    </td>
                    <td width="80%" class="etiqueta">
                        <c:out value="${DetalleNotificacion.textoNotificacion}"/>
                    </td>
                </tr>
                <tr style="display: none;">
                    <td width="15%" class="etiqueta">
                        <strong>
                        <%=descriptor.getDescripcion("etiqCaducidadNotificacion")%>
                        </strong>
                    </td>
                    <td width="80%" class="etiqueta">
                        <c:out value="${DetalleNotificacion.caducidadNotificacion}"/>
                    </td>
                </tr>
                 <tr>
                    <td width="15%" class="etiqueta">
                        <strong>
                        <%=descriptor.getDescripcion("gEtiq_registro")%>
                        </strong>
                    </td>
                    <td width="80%" class="etiqueta">
                        <c:out value="${DetalleNotificacion.numeroRegistroTelematico}"/>
                    </td>
                </tr>
                <tr>
                    <td width="15%" class="etiqueta">
                        <strong>
                        <%=descriptor.getDescripcion("gEtiq_estadoNotif")%>
                        </strong>
                    </td>
                    <td width="80%" class="etiqueta">

                       <!--Nuevos estados devueltos por el SNE. Noviembre 2016-->
                        <c:if test="${DetalleNotificacion.estadoNotificacion eq '0'}">
                            <%=descriptor.getDescripcion("etiqEstadoNoLeida") %>
                        </c:if>

                        <c:if test="${DetalleNotificacion.estadoNotificacion eq '1'}">
                            <%=descriptor.getDescripcion("etiqEstadoLeida") %>
                        </c:if>

                        <c:if test="${DetalleNotificacion.estadoNotificacion eq '2'}">
                            <%=descriptor.getDescripcion("etiqEstadoRehusada") %>
                        </c:if>

                        <c:if test="${DetalleNotificacion.estadoNotificacion eq '3'}">
                            <%=descriptor.getDescripcion("etiqEstadoRechazo") %>
                        </c:if>
                         <c:if test="${DetalleNotificacion.estadoNotificacion eq '4'}">
                            <%=descriptor.getDescripcion("etiqEstadoLeidaRechazada") %>
                        </c:if>

                    </td>
                </tr>
                <tr>
                    <td width="15%" class="etiqueta">
                        <strong>
                        <%=descriptor.getDescripcion("etiqFecAcuseNotif")%>
                        </strong>
                    </td>
                    <td width="80%" class="etiqueta">
                        <c:out value="${DetalleNotificacion.fechaAcuseAsString}"/>
                    </td>
                </tr>
                <tr>
                    <td width="15%" class="etiqueta">
                        <strong>
                        <%=descriptor.getDescripcion("etiqResultNotif")%>
                        </strong>
                    </td>
                    <td width="80%" class="etiqueta">
                        <c:choose>
                        <c:when test="${DetalleNotificacion.resultado == 'A'}">
                            <%=descriptor.getDescripcion("etiqAcepNotif") %>
                        </c:when>
                        <c:when test="${DetalleNotificacion.resultado == 'R'}">
                            <%=descriptor.getDescripcion("etiqRechNotif") %>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${DetalleNotificacion.resultado}"/>
                        </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                 <tr>
                    <td width="15%" class="etiqueta">
                       <strong>
                         <%=descriptor.getDescripcion("Acusada_por")%>
                       </strong>
                         </td>
                            <td width="80%" class="etiqueta">
                               <c:out value="${DetalleNotificacion.personaAcuseDNI} - ${DetalleNotificacion.personaAcuseNombre}"/>
                            </td>
                         </tr>

              <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>

                <c:if test="${DetalleNotificacion.autorizados ne null}">
                <tr>
                    <td class="sub3titulo" colspan="3"><%=descriptor.getDescripcion("etiqAutorizados")%></td>
                </tr>
                <tr>
                    <td class="etiqueta" colspan="3">
                        <c:forEach var="tercero" items="${DetalleNotificacion.autorizados}">
                                <li><c:out value="${tercero.nombreCompleto}"/></li>
                            </a>
                            <br>
                        </c:forEach>
                    </td>
                </tr>
                </c:if>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>


                <c:if test="${DetalleNotificacion.adjuntosExternos ne null}">
                <tr>
                    <td class="sub3titulo" colspan="3"><%=descriptor.getDescripcion("etiqArchivosExternos")%></td>
                </tr>
                <tr>
                    <td class="etiqueta" colspan="3">
                        <c:forEach var="externo" items="${DetalleNotificacion.adjuntosExternos}">
                            <a href="#" onclick="javascript:descargarFicheroExterno('<c:out value="${externo.idDocExterno}"/>');" title="Descargar" alt="Descargar">
                                <li><c:out value="${externo.nombre}"/></li>
                            </a>
                            <br>
                        </c:forEach>
                    </td>
                </tr>
                </c:if>
                <tr>
                    <td colspan="2">&nbsp;</td>
                </tr>

                <c:if test="${DetalleNotificacion.adjuntos ne null}">
                <tr>
                    <td class="sub3titulo" colspan="3"><%=descriptor.getDescripcion("etiqArchivosTramitac")%></td>
                </tr>
                <tr>
                    <td class="etiqueta" colspan="3">
                        <c:forEach var="doc" items="${DetalleNotificacion.adjuntos}">

                            <a href="#" onclick="javascript:descargarFicheroTramitacion('<c:out value="${doc.numeroUnidad}"/>','<c:out value="${doc.codigoTramite}"/>','<c:out value="${doc.ocurrenciaTramite}"/>','<c:out value="${doc.numeroExpediente}"/>','<c:out value="${doc.nombre}"/>');" title="Descargar" alt="Descargar">
                                <li><c:out value="${doc.nombre}"/></li>
                            </a>

                            <br>
                        </c:forEach>
                    </td>
                </tr>
                </c:if>



            </table>
        </div>


      <div class="capaFooter">
        <div class="botoneraPrincipal">
                        <input type="button" class="botonMasLargo" value="<%=descriptor.getDescripcion("btn_Cerrar")%>" name="cmdCerrar"  id="cmdCerrar" onClick="javascript:cerrarVentana();" >
                        </div>
    </div>
</html:form>
</body>
</html>

