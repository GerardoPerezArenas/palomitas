<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ page import="java.util.Vector"%>
<%@ page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<html>
    <head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <title>Oculto Gestión de Ficheros</title>
    <%        
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = 1;
        if (session != null) {
            usuario = (UsuarioValueObject) session.getAttribute("usuario");
            idioma = usuario.getIdioma();
        }      
    %> 
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="4" />        
        <script type="text/javascript">

        var listaDocumentosModificados = new Array();
        function inicializar(){ 
<%
            Log log = LogFactory.getLog(this.getClass());
            Vector listaDocumentos = new Vector();
            listaDocumentos = (Vector) request.getAttribute("documentosExpediente");

            String checkEntreg1 = "<input type='checkbox' class='check' name='documentoEntregado";
            String checkEntreg2 = "' value='SI'";
            String checkEntreg3 = " CHECKED ";
            String checkEntreg4 = " onclick='javascript:verificarDocumentosPresentados(this);'";
            String checkEntreg5 = "/>";

            String entreg;
            if (listaDocumentos != null) {

                for (int i = 0; i < listaDocumentos.size(); i++) {
                    GeneralValueObject gVO = (GeneralValueObject) listaDocumentos.elementAt(i);
                    String cD = (String) gVO.getAtributo("codigo");
                    String nD = (String) gVO.getAtributo("nombre");
                    String condD = (String) gVO.getAtributo("condicion");
                    String entregado = (String) gVO.getAtributo("ENTREGADO");
                    String tipoMimeAdjunto = (String)gVO.getAtributo("tipoMimeAdjuntoDocumentoExpediente");
                    String codDocumentoAdjuntoExpediente = (String)gVO.getAtributo("codDocumentoAdjuntoExpediente");
                    String nombreAdjunto   = (String)gVO.getAtributo("nombreAdjuntoDocumentoExpediente");
                    String fechaAltaAdjunto = (String)gVO.getAtributo("fechaAltaAdjuntoDocumentoExpediente");
                    String estadoFirmaAbr = (String)gVO.getAtributo("estadoFirma");
                    String estadoFirma = "";
                    if ("N".equals(estadoFirmaAbr)){
                            estadoFirma = descriptor.getDescripcion("gEtiq_estadoFirmaN");
                    } else if ("O".equals(estadoFirmaAbr)){
                            estadoFirma = descriptor.getDescripcion("gEtiq_estadoFirmaP");
                    } else if ("F".equals(estadoFirmaAbr)){
                            estadoFirma = descriptor.getDescripcion("gEtiq_estadoFirmaF");
                    } else if ("R".equals(estadoFirmaAbr)){
                            estadoFirma = descriptor.getDescripcion("gEtiq_estadoFirmaR");
                    } else if ("X".equals(estadoFirmaAbr)){
                            estadoFirma = descriptor.getDescripcion("gEtiq_estadoFirmaX"); 
                    } 
                    entreg = checkEntreg1 + i + checkEntreg2;
                    if ("SI".equals(entregado)) {
                        entreg += checkEntreg3;
                    }
                    entreg += checkEntreg4;
                    entreg += checkEntreg5;
%>        
                    var estadoFirmaDocumento = '<%=estadoFirmaAbr%>';
                    var enlaceVerificarFirma = '';
                    var codDocumentoAdjunto = '<%=codDocumentoAdjuntoExpediente%>';
                    var codigoDocumento = '<%=cD%>';
                    
                    if(estadoFirmaDocumento!=null && estadoFirmaDocumento!=undefined && estadoFirmaDocumento=='F')
                           enlaceVerificarFirma = getEnlaceVerificarFirmaDocumentoInicio(codDocumentoAdjunto,codigoDocumento) 
        
                    listaDocumentosModificados[<%= i %>]  = ["<%=entreg%>","<%= nD %>","<%= condD %>","<%=nombreAdjunto%>","<%=tipoMimeAdjunto%>","<%=fechaAltaAdjunto%>","<%=estadoFirma%>","<%=codDocumentoAdjuntoExpediente%>",enlaceVerificarFirma];
<%
                }
            }
%>   
         // Se actualiza la lista de documentos de la ficha del expediente
         parent.mainFrame.listaDocumentos = listaDocumentosModificados;
         parent.mainFrame.tabDocumentos.lineas = listaDocumentosModificados;
         parent.mainFrame.tabDocumentos.displayTabla();
    }//inicializar
    
    
    function getEnlaceVerificarFirmaDocumentoInicio(codDocumentoAdjuntoExpediente,codigoDocumento){                        
        return "<a href=\"javascript:verificarFirmaDoc('" + codDocumentoAdjuntoExpediente + "','"+ codigoDocumento + "');\">" + "<%=descriptor.getDescripcion("linkVerificarFirma")%>" + "</a>";
    }  


        </script>
    </head>
    <body onload="inicializar();">
    </body>
</html>
