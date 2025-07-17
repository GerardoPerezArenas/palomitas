<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>	
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    int codOrganizacion = usuario.getOrgCod();
    int idioma = usuario.getIdioma();
    int apl = 1;
    String mensaje       = (String)request.getAttribute("ERROR_JUSTIFICANTE_REGISTRO_PERSONALIZADO");    
    String nombreFichero = (String)request.getAttribute("FICHERO_JUSTIFICANTE");    
%>
<html>
    <head>
        <title>Oculto justificante de registro en formato jasper</title>
        <jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>?<%=System.currentTimeMillis()%>"></script>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"
                 type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"/>
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <script type="text/javascript">
            var mensaje       = "<%=mensaje%>";                        
            var nombreFichero = "<%=nombreFichero%>";
            try{

                if(mensaje=="GENERACION_PDF_CORRECTA"){
                    pleaseWait('on');
                    refrescarTablaDocumentosRegistro();
                    // Se abre la ventana para mostrar el pdf
                    var url = "<%=request.getContextPath()%>/flexia/JustificanteRegistroPDF.do?fichero=" + nombreFichero;                    
                    window.open(url,'ventana',"left=10, top=10, width=610, height=800, scrollbars=no, menubar=no, location=no, resizable=yes");
                    pleaseWait('off');
                }else
                if(mensaje=="GENERACION_MASIVA_PDF_CORRECTA"){
                    pleaseWait('on');
                    // Se abre la ventana para mostrar el pdf
                    var url = "<%=request.getContextPath()%>/flexia/JustificanteRegistroPDF.do?fichero=" + nombreFichero;                    
                    window.open(url,'ventana',"left=10, top=10, width=610, height=800, scrollbars=no, menubar=no, location=no, resizable=yes");
                    pleaseWait('off');
                }else
                if(mensaje=="GENERACION_PDF_INCORRECTA"){
                    jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorCargarJustificante")%>");
                }else
                if(mensaje=="GENERACION_PDF_IMPL_CLASS_DESCONOCIDA"){
                    jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorImplClassJustif")%>");
                }else
                if(mensaje=="NO_EXISTE_PLANTILLA_ACTIVA"){
                    jsp_alerta("A","<%=descriptor.getDescripcion("msgErrorNoJustifActivo")%>");
                }
            }catch(err){
                jsp_alerta("A","Error: " + err.description);
            }
            
            function refrescarTablaDocumentosRegistro() {
                // Se refresca la tabla de documentos
                // Se vuelve a crear la lista para mostrar en la tabla                
                var listaDocs = new Array();
                var cont=0;
				
				<logic:present scope="request" name="listaDocsAsignados">
					<logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAsignados">
						var str='';

						if(('<bean:write name="elemento" property="entregado"/>'=='S')||(('<bean:write name="elemento" property="entregado"/>'=='SI'))) str='SI';
						else if(('<bean:write name="elemento" property="entregado"/>'=='N')||(('<bean:write name="elemento" property="entregado"/>'=='NO'))) str='NO';
						else str='';

						listaDocs[cont]= [ str,"<str:escape><bean:write name="elemento" property="nombreDoc" filter="false"/></str:escape>",
											'<bean:write name="elemento" property="tipoDoc"/>', 
											'<bean:write name="elemento" property="fechaDoc"/>',
											'<bean:write name="elemento" property="cotejado"/>',
											'<bean:write name="elemento" property="doc"/>'];
						cont = cont + 1;
					</logic:iterate>
				</logic:present>
                if (listaDocs.length > 0){
                parent.mainFrame.actualizaDocs(listaDocs);
            }
            }
            
        </script>
    </head>
    <body onload="pleaseWait('off')">        
    </body>
</html>