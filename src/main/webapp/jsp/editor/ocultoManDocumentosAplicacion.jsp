<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>

<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.Map" %>

<%@page import="es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>

<%
	DocumentosAplicacionForm daForm =(DocumentosAplicacionForm)session.getAttribute("EditorDocumentosAplicacionForm");
	Vector vectorDocumentos = daForm.getListaDocumentos();
	String codAplicacion = daForm.getCodAplicacion();
	String opcion=(String)request.getParameter("opcion");
	String codDocumento = daForm.getCodDocumento();
        Config m_Config = ConfigServiceHelper.getConfig("common");	
        Config m_Documentos=ConfigServiceHelper.getConfig("documentos");	
        Boolean visibleAppExt=false;	
        try{	
            visibleAppExt=m_Documentos.getString("VISIBLE_EXT").toUpperCase().equals("SI");	
        }catch(Exception e){	
            visibleAppExt=false;	
        }

%>

<html:html locale="true">
<fmt:setLocale value='${sessionScope["org.apache.struts.action.LOCALE"]}' scope="session"/>
<head><jsp:include page="/jsp/editor/tpls/app-constants.jsp" />
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
    <title> Oculto para el Mantenimiento de Documentos de Aplicaciones </title>
    <jsp:include page="/jsp/plantillas/Metas.jsp" />
    <script>
    function getEstadoFirmaVisual( codigoDocumento, codigoEstadoFirma ) {
        var result = "";
        if (codigoDocumento) {
            var paramCodigoDocumento=''+codigoDocumento+'';
            if ( (!codigoEstadoFirma) || (codigoEstadoFirma=='') ) {
                result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.Null"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
            } else {
                if (codigoEstadoFirma == 'O') {
                    result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.O"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
                } else if (codigoEstadoFirma == 'T') {
                    result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.T"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
                } else if (codigoEstadoFirma == 'L') {
                    result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.L"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
                } else if (codigoEstadoFirma == 'U') {
                    result = '<fmt:message key="Sge.DefinicionTramitesForm.EstadoFirma.U"/> - <span class="fa fa-edit" style="cursor:hand;" onclick="pulsarModificarFirmaPlantilla('+paramCodigoDocumento+', this);"></span>';
                } else {
                    result = codigoEstadoFirma;
                }
            }
        }
        return result;
    }

    var lista = new Array();
    var listaOriginal = new Array();
    var opcion='<%=opcion%>';
    <%
	Log m_log = LogFactory.getLog(this.getClass().getName());
    if(m_log.isDebugEnabled())m_log.debug("La opcion es : " + opcion);
    %>

    function cargaListaDocumentos(){
        <%
                if (vectorDocumentos != null)
                {
                    while(!vectorDocumentos.isEmpty())
                    {
                        GeneralValueObject gVO = (GeneralValueObject)vectorDocumentos.remove(0);
                        if(codAplicacion.equals("4"))
                        {
    %>
                            var k = lista.length;
                            var rel = "";
                            var relac = '<%=(String)gVO.getAtributo("relacion")%>';
                            if(relac.trim() == "S") {
                              rel = "<%=m_Config.getString("constante.nombre.relacion").toUpperCase()%>";
                            } else {
                              rel = "EXPEDIENTE";
                            }
                            
                            lista[k] = [
                                '<%=(String)gVO.getAtributo("codigo")%>'
                                ,'<%=(String)gVO.getAtributo("descripcion")%>'
                                ,'<%=(String)gVO.getAtributo("interesado")%>'
                                ,getEstadoFirmaVisual('<%=(String)gVO.getAtributo("codigoVisible")%>'
                                ,'<%=(String)gVO.getAtributo("firma")%>')
                                ,'<%=(String)gVO.getAtributo("docActivo")%>'
                                ,rel
                                ,'<%=(String)gVO.getAtributo("codigoVisible")%>'
                                ,'<%=(String)gVO.getAtributo("editorTexto")%>'
                                <%if(visibleAppExt){%> ,'<%=(String)gVO.getAtributo("visibleExt")%>'  <%}%>
                            ];
                            
                            listaOriginal[k] = [
                                '<%=(String)gVO.getAtributo("codigo")%>'
                                ,'<%=(String)gVO.getAtributo("descripcion")%>'
                                ,'<%=(String)gVO.getAtributo("visibleInternet")%>'
                                ,'<%=(String)gVO.getAtributo("plantilla")%>'
                                ,'<%=(String)gVO.getAtributo("codPlantilla")%>'
                                ,'<%=(String)gVO.getAtributo("interesado")%>'
                                ,'<%=(String)gVO.getAtributo("firma")%>'
                                ,'<%=(String)gVO.getAtributo("docActivo")%>'
                                ,'<%=(String)gVO.getAtributo("relacion")%>'
                                ,'<%=(String)gVO.getAtributo("nomeTramite")%>'
                                ,'<%=(String)gVO.getAtributo("codigoTramite")%>'
                                <%if(visibleAppExt){%> ,'<%=(String)gVO.getAtributo("visibleExt")%>'  <%}%>
                            ];	
    <% 
                        }
                        else  if(codAplicacion.equals("1"))
                        {
    %>
                            lista[lista.length] = ['<%=(String)gVO.getAtributo("codigo")%>', '<%=(String)gVO.getAtributo("descripcion")%>', '','','','<%=(String)gVO.getAtributo("editorTexto")%>'];
    <% 
                        }
                        else
                        {
    %>
                            lista[lista.length] = ['<%=(String)gVO.getAtributo("codigo")%>', '<%=(String)gVO.getAtributo("descripcion")%>'];
    <%
                        }
                    }
                }
    %>
                parent.mainFrame.recuperaDatos(lista,listaOriginal);
    }

    function eliminaDocumento(){
        parent.mainFrame.eliminarDocumento();
    }
    
     function visibleExterior(){	
        parent.mainFrame.cargaDocumentos();	
    }

    function redireccionaOpcion(){
            if(opcion=="cargarDocumentos") cargaListaDocumentos();
            else if(opcion=="eliminarDocumento") eliminaDocumento();
            else if(opcion=="visibleExterior") visibleExterior();
    }
    </script>
</head>
<body onLoad="redireccionaOpcion();">
</body>
</html:html>
