<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str" %>

<%@ page import="org.apache.struts.upload.FormFile"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="es.altia.agora.business.registro.RegistroValueObject"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>
<%@ page import="es.altia.agora.interfaces.user.web.registro.DocumentoRegistroForm"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.*"%>
<%@ page import="es.altia.agora.technical.Fecha"%>
<%@ page import="es.altia.agora.technical.ConstantesDatos"%>
<%@ page import="es.altia.util.commons.MimeTypes"%>


<html>
<head><jsp:include page="/jsp/formularios/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
<title>Oculto Gestión de Ficheros</title>
  <script type="text/javascript">

    var ESTADO_DOCUMENTO_REGISTRO_ELIMINADO = '<%=ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO%>';
        
    // FUNCIONES DE CARGA E INICIALIZACION DE DATOS
    function inicializar(){
        <%
        DocumentoRegistroForm fichaForm = (DocumentoRegistroForm)session.getAttribute("DocumentoRegistroForm");
        Log _log = LogFactory.getLog(this.getClass());
        String nombre = null;
        String tipo = null;        
        FormFile fichero = fichaForm.getFichero();
        String docEscaneado = fichaForm.getDocEscaneado();   
        String fecha= fichaForm.getfechaDocumento();
        byte[] datos = null;
        String titulo = fichaForm.getTituloFichero();
        String tipoGuardado = fichaForm.getTipoFichero();
        String entregado = fichaForm.getEntregado();
        int modoAlta = fichaForm.getModoAlta();
        String tipoDocumental = fichaForm.getTipoDocumental();
        String mostrarDigitalizar = (String) session.getAttribute("servicioDigitalizacionActivo");
        if (mostrarDigitalizar == null) {
            mostrarDigitalizar = "no";
        }
       
        if(titulo!=null && titulo.length()>=1)
        {          
            _log.debug("FICHERO: " + fichero);
            if (session.getAttribute("DocumentoRegistroForm") != null)
            {                               
                MantAnotacionRegistroForm regESForm = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");
                Vector docs = regESForm.getListaDocsAsignados();                
                RegistroValueObject reg = new RegistroValueObject();
                
              
                // Si se ha subido el fichero por POST
                if(fichero!=null && fichero.getFileData()!=null && fichero.getFileSize()>0 && (!"".equals(fichaForm.getFichero().getFileName()))){    
                    
                    // Si sube por POST un objeto de tipo File                        
                    //nombre = fichero.getFileName();
                    //reg.setNombreDoc(nombre);
                    _log.debug("Fichero no nulo");
                    tipo = fichero.getContentType();
                    // Si es octet-stream tratamos de deducir el mimetype a partir de 
                    // la extension
                    if (MimeTypes.BINARY[0].equals(tipo)) {
                        tipo = MimeTypes.guessMimeType(tipo, fichero.getFileName());
                    }
                    datos = fichero.getFileData();  
                   
                    reg.setFechaDoc(fecha);                                     
                    reg.setDoc(fichero.getFileData());                    
                    reg.setNombreDoc(titulo);
                    reg.setTipoDoc(tipo);
                    reg.setDocNormal(true);
                      // Se ha subido un fichero nuevo, por lo que descartamos los metadatos ya que ya no son validos	
                    reg.setCotejado("NO");	
                    reg.setMetadatosDoc(null);

                    /**** nuevo **/
                    reg.setEstadoDocumentoRegistro(ConstantesDatos.ESTADO_DOCUMENTO_NUEVO);
                    reg.setLongitudDocumento(datos.length);
                    reg.setRutaDocumentoRegistroDisco((String)request.getAttribute(ConstantesDatos.RUTA_FICHERO_SUBIDO_SERVIDOR));
                    _log.debug("Ruta documento: " + reg.getRutaDocumentoRegistroDisco());
                    /**** nuevo **/

                    if(modoAlta==1)reg.setEntregado("S");
                    else reg.setEntregado(entregado);

                    
                    reg.setCompulsado("NO");
                    reg.setDescripcionTipoDocumental(tipoDocumental);
                  
                    
                } else if (!fichaForm.isModificando()){
                    
                    // Caso fichero vacío (solo nombre de documento)
                    _log.debug("Sin fichero (fichero nulo)"); 
                    tipo = "";               
                    datos = new byte[0]; 
                    reg.setDoc(datos);
                    reg.setNombreDoc(titulo);
                    reg.setTipoDoc(tipo);
                    reg.setFechaDoc("");
                    reg.setCotejado("");
                    reg.setMetadatosDoc(null);
                    reg.setDocNormal(true);
                    if(modoAlta==1)reg.setEntregado("S");
                    else reg.setEntregado(entregado);
                    reg.setCompulsado("NO");
                    reg.setDescripcionTipoDocumental(" ");
                }
                
                if(docEscaneado!=null && docEscaneado.length()>=1){
                    
                    _log.debug("Fichero escaneado");
                    reg.setNombreDoc(titulo);
                    reg.setTipoDoc("image/jpeg");
                    reg.setFechaDoc(fecha);
                    sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
                    datos = decoder.decodeBuffer(fichaForm.getDocEscaneado().trim());
                    tipo  = reg.getTipoDoc();
                    reg.setDoc(datos); 
                    reg.setDocNormal(false);


                    /**** nuevo **/
                    reg.setEstadoDocumentoRegistro(ConstantesDatos.ESTADO_DOCUMENTO_NUEVO);
                    reg.setLongitudDocumento(datos.length);
                    reg.setRutaDocumentoRegistroDisco((String)request.getAttribute(ConstantesDatos.RUTA_FICHERO_SUBIDO_SERVIDOR));
                    _log.debug("Ruta documento: " + reg.getRutaDocumentoRegistroDisco());
                    /**** nuevo **/


                    if(modoAlta==1)reg.setEntregado("S");
                    else reg.setEntregado(entregado);
                    
                     reg.setCompulsado("NO");
                     reg.setDescripcionTipoDocumental(" ");
                    
                }
                
                if (fichaForm.isModificando()&& ("".equals(fichero.getFileName()))) {
                    
                    int codigo = Integer.parseInt((String)session.getAttribute("codigo"));      
                    Vector docsAux = regESForm.getListaDocsAsignados();
                    RegistroValueObject registroAux = new RegistroValueObject();
                    registroAux = (RegistroValueObject)docsAux.get(codigo);
                    datos = registroAux.getDoc();                                 
                    reg.setNombreDoc(titulo);
                    reg.setTipoDoc(tipoGuardado);
                    reg.setFechaDoc(fecha);
                    reg.setDoc(datos);
                    if(modoAlta==1)reg.setEntregado("S");
                    else reg.setEntregado(entregado);
                    
                     reg.setCompulsado("NO");

                     
                    fichaForm.setModificando(false);
                }
                
                // Si estamos modificando un fichero, sustituimos en la lista que se 
                // guarda en el form la version antigua por la nueva.
                String tituloModificando = fichaForm.getTituloModificando();                
                if (!tituloModificando.equals("")) {                
                    int posicion=0;
                    for(int h=0;h<docs.size();h++){
			RegistroValueObject doc = (RegistroValueObject)docs.get(h);
                        if (doc.getNombreDoc().equals(tituloModificando)) {
                            posicion=docs.indexOf(doc);
                            break;
                        }
                    }

                    if(regESForm.getNumero()!=null && regESForm.getNumero().length()>0){
                        // El objeto antiguo que se ha modificado, se marcará como eliminado y se añade al final
                        // de la lista de documentos
                        RegistroValueObject documentoModificado = (RegistroValueObject)docs.get(posicion);
                        documentoModificado.setEstadoDocumentoRegistro(ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO);                        
                        docs.add(documentoModificado);
                     }

                    // Se añade el documento recien modificado y cuyo estado sera el de nuevo a la posición que
                    // ocupaba el documento anterior, es decir, el modificado
                    reg.setEstadoDocumentoRegistro(ConstantesDatos.ESTADO_DOCUMENTO_NUEVO);
                    docs.set(posicion,reg);

                } else {
                    // Se trata de un alta, por tanto, se añade el documento al final 
                    // de la lista de documentos
                    docs.add(reg);
                }
                regESForm.setListaDocsAsignados(docs);                                
            }
         } 
         fichaForm.setModificando(false);
         session.removeAttribute("modificando");
         session.removeAttribute("DocumentoRegistroForm"); 
       %>            
       // Se vuelve a crear la lista para mostrar en la tabla                
        var listaDocs = new Array();
        cont=0;
        
        <logic:iterate id="elemento" name="MantAnotacionRegistroForm" property="listaDocsAsignados">
           var str='';
       
          if(('<bean:write name="elemento" property="entregado"/>'=='S')||(('<bean:write name="elemento" property="entregado"/>'=='SI'))) str='SI';
          else if(('<bean:write name="elemento" property="entregado"/>'=='N')||(('<bean:write name="elemento" property="entregado"/>'=='NO'))) str='NO';
          else str='';

          var estado = '<bean:write name="elemento" property="estadoDocumentoRegistro"/>';                    
          
          if(estado!=ESTADO_DOCUMENTO_REGISTRO_ELIMINADO){  
            if(<%=mostrarDigitalizar.equalsIgnoreCase("si")%>){  
				listaDocs[cont]= [ str,"<str:escape><bean:write name="elemento" property="nombreDoc" filter="false"/></str:escape>",
								   '<bean:write name="elemento" property="tipoDoc"/>', 
								   '<bean:write name="elemento" property="fechaDoc"/>',
								   '<bean:write name="elemento" property="compulsado"/>',
								   '<bean:write name="elemento" property="doc"/>',
									'<bean:write name="elemento" property="descripcionTipoDocumental"/>'];  


				cont= cont+1;
            
			}else{ 
			  listaDocs[cont]= [ str,"<str:escape><bean:write name="elemento" property="nombreDoc" filter="false"/></str:escape>",
								 '<bean:write name="elemento" property="tipoDoc"/>', 
								 '<bean:write name="elemento" property="fechaDoc"/>',
								 '<bean:write name="elemento" property="cotejado"/>',
								 '<bean:write name="elemento" property="doc"/>'];  



			  cont= cont+1;
			  }
			}
        </logic:iterate> 
       
        self.parent.opener.retornoXanelaAuxiliar(listaDocs);
    }
  </script>
</head>
<body onload="inicializar();">
</body>
</html>
