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
    <title>Comunicación</title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/domlay.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/><script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">
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
           var firma = document.getElementsByName("comunicacionVO.firma")[0].value;
           var idComu = document.getElementsByName("comunicacionVO.id")[0].value;
           
           var source = "<%=request.getContextPath()%>/sge/FichaExpediente.do?opcion=verificarFirmaComunicacion" +
                     "&idCom="+idComu;
           abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,[],
	'width=650,height=360,status=no',function(){});
       }
       
        function verificarFirmaDocumento(idAdjunto,idComunicacion, idFirma){
          var opt ="dialogHeight: 340px; dialogWidth: 650px; edge: Raised; center: Yes; resizable: No; status: No;help: No;";
          var source = "<%=request.getContextPath()%>/sge/FichaExpediente.do?opcion=verificarFirmaDocComunicacion" +
                     "&idAdj="+idAdjunto+"&idCom="+idComunicacion;
           abrirXanelaAuxiliar('<%=request.getContextPath()%>/jsp/sge/mainVentana.jsp?source='+source,[],
	'width=650,height=360,status=no',function(){});
       }
    </script>
</head>

<body class="bandaBody" style="width: 100%">
<html:form action="/sge/FichaExpediente.do" method="post">
    
    <html:hidden property="comunicacionVO.id" name="FichaExpedienteForm"/>
    <html:hidden property="comunicacionVO.firma" name="FichaExpedienteForm"/>
    
    <div class="txttitblanco"><%=descriptor.getDescripcion("gEtiq_comunicaciones")%></div>
    <div class="contenidoPantalla">
            <table  width="100%" >
                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_numRex")%></td>
                    <td width="30%">
                        <html:text property="comunicacionVO.numeroRegistro" name="FichaExpedienteForm" styleClass="inputTexto" readonly="true" style="width: 90%"/>
                    </td>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiqNumExpediente")%></td>
                    <td width="30%">
                        <html:text property="comunicacionVO.numeroExpediente" name="FichaExpedienteForm" styleClass="inputTexto" readonly="true" style="width: 90%"/>
                    </td>
                </tr>
                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiqFechaReg")%></td>
                    <td width="30%">
                        <input type="text" class="inputTexto"  value="<%=fechaFormateada%>" readonly="true" style="width: 90%"/>
                    </td>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiqIdentificacion")%></td>
                    <td width="30%">
                        <html:text property="comunicacionVO.documento" name="FichaExpedienteForm" styleClass="inputTexto" readonly="true" style="width: 90%"/>
                    </td>
                </tr>
                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("etiq_IntPrin")%></td>
                    <td width="80%" colspan="3">
                        <html:text property="comunicacionVO.nombre" name="FichaExpedienteForm" styleClass="inputTexto" readonly="true"  style="width: 96%"/>
                    </td>
                </tr>
                <tr>
                    <td width="20%" class="etiqueta"><%=descriptor.getDescripcion("gEtiq_asunto")%></td>
                    <td width="80%" colspan="3">
                        <html:text property="comunicacionVO.asunto" name="FichaExpedienteForm" styleClass="inputTexto" readonly="true"  style="width: 96%"/>
                    </td>
                </tr>
                <tr>
                    <td width="100%" colspan="4" class="etiqueta"><%=descriptor.getDescripcion("etiqTextoCom")%></td>
                </tr>
                <tr>
                     
                    <td width="100%" colspan="4">
                        <html:textarea property="comunicacionVO.texto" name="FichaExpedienteForm" styleClass="inputTexto" readonly="true" rows="10"  style="width: 97%"/>
                    </td>
                </tr>
                
                
                <c:choose>
                   <c:when test="${!empty FichaExpedienteForm.comunicacionVO.listaAdjuntos}">
                <tr>
                    <td width="100%" colspan="4" class="etiqueta"><%=descriptor.getDescripcion("etiqFichero")%></td>
                </tr>
                <tr>
                    <td width="100%" colspan="4">
                        
                        <table class="tablaP xTabla" cellSpacing="0" cellPadding="3" width="97%">
                            <thead class="xCabecera">
                                 <th class="xCabecera">
                                    <%=descriptor.getDescripcion("etiq_NomFichero")%>
                                </th>
                                 <th class="xCabecera">
                                    <%=descriptor.getDescripcion("etiq_tipFichero")%>
                                </th>
                                <th class="xCabecera">
                                </th>
                                 <th class="xCabecera" style="display:none">
                                </th>
                            </thead>
                            <tbody>
                                
                                <%--
                                <c:choose>
                                    <c:when test="${!empty FichaExpedienteForm.comunicacionVO.listaAdjuntos}">
                                    --%>  
                                        <c:forEach var="adjunto" items="${FichaExpedienteForm.comunicacionVO.listaAdjuntos}">                                         
                                            <tr>
                                               
                                                <td class="xCabeceraDiv"> <a href='javascript:descargarFichero("<c:out value='${adjunto.idComunicacion}'/>","<c:out value='${adjunto.id}'/>");'><c:out value="${adjunto.nombre}"/></a></td>
                                                <td class="xCabeceraDiv"><c:out value="${adjunto.tipoMime}"/></td>
                                                <td class="xCabeceraDiv">
                                                    <input type="button" class="botonMasLargo" value="<%=descriptor.getDescripcion("btn_VerfificarF")%>" name="cmdVerificarFirmaAdj" onclick='javascript:verificarFirmaDocumento("<c:out value='${adjunto.id}'/>","<c:out value='${adjunto.idComunicacion}'/>","<c:out value='${adjunto.firma}'/>");' />
                                                </td>

                                            </tr>
                                        </c:forEach>
                                            
                                            <%--
                                    </c:when>   
                                    <c:otherwise>
                                              <tr>
                                                <td colspan="4"></td>
                                            </tr>  
                                    </c:otherwise>
                                </c:choose>
                                            --%>
                            </tbody>
                        </table>
                       </c:when>
                  </c:choose>
                            
                        
                    </td>
                </tr>
            </table>
            <div class="botoneraPrincipal" >
                <input type="button" class="botonMasLargo" value="<%=descriptor.getDescripcion("btn_Cerrar")%>" name="cmdCerrar"  id="cmdCerrar" onClick="javascript:cerrarVentana();" >
                <input type="button" class="botonMasLargo" value="<%=descriptor.getDescripcion("btn_VerfificarF")%>" name="cmdVerificar"  id="cmdVerificar" onclick="javascript:verificarFirma();"  >
            </div>
        </div>
    </table>
</html:form>

</body>
</html>
