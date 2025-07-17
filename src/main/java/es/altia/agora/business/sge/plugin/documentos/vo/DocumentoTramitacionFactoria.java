package es.altia.agora.business.sge.plugin.documentos.vo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.ResourceBundle;

/**
 * Factoria de objetos Documento
 * @author oscar.rodriguez
 */
public class DocumentoTramitacionFactoria {
    private static DocumentoTramitacionFactoria instance=null;
    private String nombreGestor = null;
    private DocumentoTramitacionFactoria(){}
    
    public static final int TIPO_DOCUMENTO_BBDD     = 1;
    public static final int TIPO_DOCUMENTO_GESTOR = 2;
    
    
    public static final String GUION                           = "-";
    public static final String BARRA                          =  "/";
    public static final String REL                               = "REL_";

    public static DocumentoTramitacionFactoria getInstance(){
        if(instance==null) instance = new DocumentoTramitacionFactoria();
        return instance;
    }

    
    public Documento getDocumento(Hashtable<String,Object> datos,int tipoDocumento){
        Long idDocumento = (Long) datos.get("idDocumento");
        String codMunicipio = (String)datos.get("codMunicipio");
        String codProcedimiento = (String)datos.get("codProcedimiento");
        String ejercicio               = (String)datos.get("ejercicio");
        String numeroExpediente = (String)datos.get("numeroExpediente");
        String codTramite = (String)datos.get("codTramite");
        String ocurrenciaTramite = (String)datos.get("ocurrenciaTramite");
        String codDocumento = (String)datos.get("codDocumento");
        String codUsuario = (String)datos.get("codUsuario");
        String nombreDocumento = (String)datos.get("nombreDocumento");
        String numeroDocumento = (String)datos.get("numeroDocumento");
        String perteneceRelacion = (String)datos.get("perteneceRelacion");
        String opcionGrabar     = (String)datos.get("opcionGrabar");
        String numeroRelacion = (String)datos.get("numeroRelacion");
        byte[] fichero= (byte[])datos.get("fichero");
        String[] params = (String[])datos.get("params");
        String extension = (String)datos.get("extension");
        String codigoTramiteVisible = (String)datos.get("codigoVisibleTramite");
        String tipoMIME                 = (String)datos.get("tipoMime");
        // Para documentos de expediente
        String origen                     = (String)datos.get("origen");
        String eliminarSoloAdjunto = (String)datos.get("eliminarSoloAdjunto");
        String modificarAdjuntoDocExpediente = (String)datos.get("modificarAdjuntoDocExpediente");
        ArrayList<String> listaCarpetas = (ArrayList<String>)datos.get("listaCarpetas");
        String nuevoNombreFicheroCompleto = (String)datos.get("nuevoNombreFicheroCompleto");
        String nombreFicheroCompleto          = (String)datos.get("nombreFicheroCompleto");
        String codTipoDato = (String) datos.get("codTipoDato");
        Integer longitudDocumento = (Integer)datos.get("longitudDocumento");
        
        String observaciones = (String)datos.get("observaciones");
        String idFirma = (String)datos.get("idFirma");
        
        // Para documento de registro
        Boolean oDocumentoRegistro = (Boolean)datos.get("documentoRegistro");
        Integer identDepart = (Integer)datos.get("identDepart");
        Integer unidadOrgan = (Integer)datos.get("unidadOrgan");
        Integer anoReg = (Integer)datos.get("anoReg");
        Long numReg = (Long)datos.get("numReg");
        String tipoReg = (String)datos.get("tipoReg");        
        String tipoDoc = (String)datos.get("tipoDoc");        
        String fechaDoc = (String)datos.get("fechaDoc");
        String entregado = (String)datos.get("entregado");    
        Integer estadoDocumentoRegistro = (Integer)datos.get("estadoDocumentoRegistro");
        String rutaDocumento                  = (String)datos.get("rutaDocumento");
        String documentoProcedimientoSinValor = (String)datos.get("documentoProcedimientoSinValor");
        String expedienteHistorico = (String)datos.get("expedienteHistorico");
        
        // Metadatos de cotejo de los documentos de registro
        String versionNTIMetadatos = (datos.containsKey("versionNTIMetadatos")) 
                ? (String)datos.get("versionNTIMetadatos") : null;
        Long idDocumentoMetadatos = (datos.containsKey("idDocumentoMetadatos"))
                ? (Long)datos.get("idDocumentoMetadatos") : null;
        String organoMetadatos = (datos.containsKey("organoMetadatos"))
                ? (String)datos.get("organoMetadatos") : null;
        Calendar fechaCapturaMetadatos = (datos.containsKey("fechaCapturaMetadatos"))
                ? (Calendar)datos.get("fechaCapturaMetadatos") : null;
        Integer origenMetadatos = (datos.containsKey("origenMetadatos"))
                ? (Integer)datos.get("origenMetadatos") : null;
        Integer estadoElaboracionMetadatos = (datos.containsKey("estadoElaboracionMetadatos"))
                ? (Integer)datos.get("estadoElaboracionMetadatos") : null;
        String nombreFormatoMetadatos = (datos.containsKey("nombreFormatoMetadatos"))
                ? (String)datos.get("nombreFormatoMetadatos") : null;
        Integer tipoDocumentalMetadatos = (datos.containsKey("tipoDocumentalMetadatos"))
                ? (Integer)datos.get("tipoDocumentalMetadatos") : null;
        Integer tipoFirmaMetadatos = (datos.containsKey("tipoFirmaMetadatos"))
                ? (Integer)datos.get("tipoFirmaMetadatos") : null;

        // Metadatos de documentos: CSV
        Long idMetadatoDocumento = (datos.containsKey("idMetadatoDocumento"))
                ? (Long) datos.get("idMetadatoDocumento") : null;
        Boolean insertarMetadatosEnBBDD = (datos.containsKey("insertarMetadatosEnBBDD"))
                ? (Boolean) datos.get("insertarMetadatosEnBBDD") : null;
        String metadatoDocumentoCsv = (datos.containsKey("metadatoDocumentoCsv"))
                ? (String)datos.get("metadatoDocumentoCsv") : null;
        String metadatoDocumentoCsvAplicacion = (datos.containsKey("metadatoDocumentoCsvAplicacion"))
                ? (String)datos.get("metadatoDocumentoCsvAplicacion") : null;
        String metadatoDocumentoCsvUri = (datos.containsKey("metadatoDocumentoCsvUri"))
                ? (String)datos.get("metadatoDocumentoCsvUri") : null;

         //parametro que indica la procedencia de la llamada
        String desdeNotif = (String) datos.get("desdeNotificacion");
        
        if(tipoDocumento==TIPO_DOCUMENTO_BBDD){                  
            DocumentoBBDD doc = new DocumentoBBDD();
            if(idDocumento!=null) doc.setIdDocumento(idDocumento); // Indicamos el id del documento de base de datos
            if(codMunicipio!=null) doc.setCodMunicipio(Integer.parseInt(codMunicipio));
            doc.setCodProcedimiento(codProcedimiento);
            if(ejercicio!=null)  doc.setEjercicio(Integer.parseInt(ejercicio));
            doc.setNumeroExpediente(numeroExpediente);
            if(codTramite!=null && !" ".equals(codTramite)) doc.setCodTramite(Integer.parseInt(codTramite));
            if(ocurrenciaTramite!=null && !" ".equals(ocurrenciaTramite)) doc.setOcurrenciaTramite(Integer.parseInt(ocurrenciaTramite));           
            if(codDocumento!=null && codDocumento.length()>=1 && !" ".equals(codDocumento) && !"".equals(codDocumento))  doc.setCodDocumento(Integer.parseInt(codDocumento));
            if(codUsuario!=null && !" ".equals(codUsuario)) doc.setCodUsuario(Integer.parseInt(codUsuario));
            doc.setNombreDocumento(nombreDocumento);
            doc.setNumeroDocumento(numeroDocumento);
            doc.setFichero(fichero);
            doc.setParams(params);
            doc.setOpcionGrabar(opcionGrabar);
            doc.setNumeroRelacion(numeroRelacion);
            doc.setTipoMimeContenido(tipoMIME);
            doc.setExtension(extension);
            doc.setCodTipoDato(codTipoDato);
            doc.setObservaciones(observaciones);
            if(idFirma!=null && idFirma.length()>0) doc.setIdFirma(Integer.parseInt(idFirma));
                  
            if(!"true".equals(perteneceRelacion))
                doc.setDocRelacion(false); // Se indica que el documento no pertenece a una relación
            else
                doc.setDocRelacion(true);

            if(longitudDocumento!=null) doc.setLongitudDocumento(longitudDocumento.intValue());
            // Para documentos de expediente
            doc.setOrigen(origen);
            if(eliminarSoloAdjunto!=null && eliminarSoloAdjunto.equalsIgnoreCase("SI")){
                doc.setEliminarSoloAdjunto(true);
            }else
                doc.setEliminarSoloAdjunto(false);

            if(modificarAdjuntoDocExpediente!=null && modificarAdjuntoDocExpediente.equalsIgnoreCase("SI")){
                doc.setModificarAdjuntoDocExpediente(true);
            }else{
                doc.setModificarAdjuntoDocExpediente(false);
            }

            // Expediente en histórico
            doc.setExpHistorico(false);
            if(expedienteHistorico!=null && expedienteHistorico.equalsIgnoreCase("TRUE")) { 
                doc.setExpHistorico(true);                
            }
            
            doc.setRutaDocumento(rutaDocumento);
            
            // Metadatos de documentos:
            doc.setIdMetadatoDocumento(idMetadatoDocumento);
            doc.setInsertarMetadatosEnBBDD(insertarMetadatosEnBBDD);
            // Metadatos de documentos: CSV
            doc.setMetadatoDocumentoCsv(metadatoDocumentoCsv);
            doc.setMetadatoDocumentoCsvAplicacion(metadatoDocumentoCsvAplicacion);
            doc.setMetadatoDocumentoCsvUri(metadatoDocumentoCsvUri);
            
            // Para documentos de registro
            if(oDocumentoRegistro!=null && oDocumentoRegistro.booleanValue()==true){
                // Se trata de un documento de registro 
               doc.setDocumentoRegistro(true);
               if(identDepart!=null)  doc.setCodigoDepartamento(identDepart.intValue());
               if(unidadOrgan!=null)  doc.setCodigoUnidadOrganica(unidadOrgan);
               doc.setEjercicioAnotacion(anoReg);
               doc.setNumeroRegistro(numReg);
               doc.setTipoRegistro(tipoReg);
               doc.setTipoDocumento(tipoDoc);
               doc.setFechaDocumento(fechaDoc);
               doc.setEntregado(entregado);
               doc.setEstadoDocumentoRegistro(estadoDocumentoRegistro);
               
               doc.setDocumentoProcedimientoSinValor(false);
               if(documentoProcedimientoSinValor!=null && documentoProcedimientoSinValor.equals("true")){
                   doc.setDocumentoProcedimientoSinValor(true);
               }
               
               // Metadatos de cotejo de los documento
               doc.setVersionNTIMetadatos(versionNTIMetadatos);
               doc.setIdDocumentoMetadatos(idDocumentoMetadatos);
               doc.setOrganoMetadatos(organoMetadatos);
               doc.setFechaCapturaMetadatos(fechaCapturaMetadatos);
               doc.setOrigenMetadatos(origenMetadatos);
               doc.setEstadoElaboracionMetadatos(estadoElaboracionMetadatos);
               doc.setNombreFormatoMetadatos(nombreFormatoMetadatos);
               doc.setTipoDocumentalMetadatos(tipoDocumentalMetadatos);
               doc.setTipoFirmaMetadatos(tipoFirmaMetadatos);
            }
            
             //para indicar la procedencia de la peticion de descarga
            if(desdeNotif!=null && desdeNotif.equalsIgnoreCase("si")){
                doc.setDesdeNotificacion(true);
            } else { 
                doc.setDesdeNotificacion(false);
            }
            
            return doc;
        }

        if(tipoDocumento==TIPO_DOCUMENTO_GESTOR){
            ResourceBundle bundle = ResourceBundle.getBundle("documentos");

            String nombreOrganizacion   = (String)datos.get("nombreOrganizacion");
            String nombreProcedimiento = (String)datos.get("nombreProcedimiento");
            ArrayList<String> listaExpedientesRelacion = (ArrayList<String>)datos.get("listaExpedientesRelacion");
            
            String codificacionUTF8  = (String)datos.get("codificacion");


            DocumentoGestor doc = new DocumentoGestor();
            if(idDocumento!=null) doc.setIdDocumento(idDocumento); // Indicamos el id del documento gestor
            doc.setListaExpedientes(listaExpedientesRelacion);
            if(codMunicipio!=null) doc.setCodMunicipio(Integer.parseInt(codMunicipio));
            doc.setCodProcedimiento(codProcedimiento);
            if(ejercicio!=null && !"".equals(ejercicio))
                doc.setEjercicio(Integer.parseInt(ejercicio));
            doc.setNumeroExpediente(numeroExpediente);
            if(codTramite!=null && !" ".equals(codTramite)) doc.setCodTramite(Integer.parseInt(codTramite));
            if(ocurrenciaTramite!=null && !" ".equals(ocurrenciaTramite)) doc.setOcurrenciaTramite(Integer.parseInt(ocurrenciaTramite));
            if(codDocumento!=null && isInteger(codDocumento)) doc.setCodDocumento(Integer.parseInt(codDocumento));
            if(codUsuario!=null && !" ".equals(codUsuario)) doc.setCodUsuario(Integer.parseInt(codUsuario));
            doc.setNombreDocumento(nombreDocumento);
            doc.setNumeroDocumento(numeroDocumento);
            doc.setFichero(fichero);
            doc.setOpcionGrabar(opcionGrabar);
            doc.setNumeroRelacion(numeroRelacion);
            doc.setParams(params);
            doc.setExtension(extension);            
            if(!"true".equals(perteneceRelacion))
                doc.setDocRelacion(false); // Se indica que el documento no pertenece a una relación
            else
                doc.setDocRelacion(true);

            doc.setTipoMimeContenido(tipoMIME);
            doc.setCodificacionContenido(codificacionUTF8);
            doc.setNombreOrganizacion(nombreOrganizacion);
            doc.setNombreProcedimiento(nombreProcedimiento);                        
            if(codigoTramiteVisible!=null && isInteger(codigoTramiteVisible))
                doc.setCodTramiteVisible(Integer.parseInt(codigoTramiteVisible));

            // Para documentos de expediente
            doc.setOrigen(origen);
            if(eliminarSoloAdjunto!=null && eliminarSoloAdjunto.equalsIgnoreCase("SI")){
                doc.setEliminarSoloAdjunto(true);
            }else
                doc.setEliminarSoloAdjunto(false);

             if(modificarAdjuntoDocExpediente!=null && modificarAdjuntoDocExpediente.equalsIgnoreCase("SI")){
                doc.setModificarAdjuntoDocExpediente(true);
            }else{
                doc.setModificarAdjuntoDocExpediente(false);
            }
             
            // Expediente en histórico
            doc.setExpHistorico(false);
            if(expedienteHistorico!=null && expedienteHistorico.equalsIgnoreCase("TRUE")) { 
                doc.setExpHistorico(true);                
            }

            doc.setListaCarpetas(listaCarpetas);
            // EN PRINCIPIO SÓLO LO UTILIZARÁ EL PLUGIN DEL GESTOR DOCUMENTAL PARA LOS MÉTODOS DE PRUEBA PERO HABRÁ QUE
            // EXTENDERLO AL RESTO DE MÉTODOS
            doc.setNombreFicheroCompleto(nombreFicheroCompleto);
            doc.setNuevoNombreFicheroCompleto(nuevoNombreFicheroCompleto);
            if(longitudDocumento!=null) doc.setLongitudDocumento(longitudDocumento.intValue());
            doc.setCodTipoDato(codTipoDato);
            doc.setObservaciones(observaciones);            
            doc.setRutaDocumento(rutaDocumento);  
            if(idFirma!=null && idFirma.length()>0) doc.setIdFirma(Integer.parseInt(idFirma));
               
            // Metadatos de documentos:
            doc.setIdMetadatoDocumento(idMetadatoDocumento);
            doc.setInsertarMetadatosEnBBDD(insertarMetadatosEnBBDD);
            // Metadatos de documentos: CSV
            doc.setMetadatoDocumentoCsv(metadatoDocumentoCsv);
            doc.setMetadatoDocumentoCsvAplicacion(metadatoDocumentoCsvAplicacion);
            doc.setMetadatoDocumentoCsvUri(metadatoDocumentoCsvUri);
            
             // Para documentos de registro
            if(oDocumentoRegistro!=null && oDocumentoRegistro.booleanValue()==true){
                // Se trata de un documento de registro 
               doc.setDocumentoRegistro(true);
               if(identDepart!=null) doc.setCodigoDepartamento(identDepart.intValue());
               if(unidadOrgan!=null) doc.setCodigoUnidadOrganica(unidadOrgan);
               doc.setEjercicioAnotacion(anoReg);
               doc.setNumeroRegistro(numReg);
               doc.setTipoRegistro(tipoReg);
               doc.setTipoDocumento(tipoDoc);
               doc.setFechaDocumento(fechaDoc);
               doc.setEntregado(entregado);
               doc.setEstadoDocumentoRegistro(estadoDocumentoRegistro);
               
               doc.setDocumentoProcedimientoSinValor(false);
               if(documentoProcedimientoSinValor!=null && documentoProcedimientoSinValor.equals("true")){
                   doc.setDocumentoProcedimientoSinValor(true);
               }
               
               // Metadatos de cotejo de los documento
               doc.setVersionNTIMetadatos(versionNTIMetadatos);
               doc.setIdDocumentoMetadatos(idDocumentoMetadatos);
               doc.setOrganoMetadatos(organoMetadatos);
               doc.setFechaCapturaMetadatos(fechaCapturaMetadatos);
               doc.setOrigenMetadatos(origenMetadatos);
               doc.setEstadoElaboracionMetadatos(estadoElaboracionMetadatos);
               doc.setNombreFormatoMetadatos(nombreFormatoMetadatos);
               doc.setTipoDocumentalMetadatos(tipoDocumentalMetadatos);
               doc.setTipoFirmaMetadatos(tipoFirmaMetadatos);
            }
            
            //para indicar la procedencia de la peticion de descarga
            if(desdeNotif!=null && desdeNotif.equalsIgnoreCase("si"))
                doc.setDesdeNotificacion(true);
            else doc.setDesdeNotificacion(false);

            return doc;
        }

        return null;
    }
    
    public DocumentoFirma getDocumentoFirma(Hashtable<String, Object> datos, int tipoDocumento) {
        DocumentoFirma doc = null;
        
        Integer codDocumento = (Integer) datos.get("codDocumento");
        Integer codMunicipio = (Integer) datos.get("codMunicipio");
        String codProcedimiento = (String) datos.get("codProcedimiento");
        Integer ejercicio = (Integer) datos.get("ejercicio");
        String numeroExpediente = (String) datos.get("numeroExpediente");
        Integer codTramite = (Integer) datos.get("codTramite");
        Integer ocurrenciaTramite = (Integer) datos.get("ocurrenciaTramite");
        String nombreDocumento = (String) datos.get("nombreDocumento");
        Integer codUsuario = (Integer) datos.get("codUsuario");
        byte[] fichero= (byte[])datos.get("fichero");
        String[] params = (String[])datos.get("params");
        String perteneceRelacion = (String)datos.get("perteneceRelacion");
        String extension = null;
        if(datos.get("extension") != null)
            extension = (String)datos.get("extension");
        String tipoMIME = null;
        if(datos.get("tipoMime") != null)
            extension = (String)datos.get("tipoMime");
        String codigoTramiteVisible = null;
        if(datos.get("codigoVisibleTramite") != null)
            codigoTramiteVisible = (String)datos.get("codigoVisibleTramite");
        ArrayList<String> listaCarpetas = null;
        if(datos.get("listaCarpetas") != null)
            listaCarpetas = (ArrayList<String>)datos.get("listaCarpetas");
        String codificacionUTF8  = null;
        if(datos.get("codificacion") != null)
            codificacionUTF8 = (String)datos.get("codificacion");
        
        if(tipoDocumento == TIPO_DOCUMENTO_BBDD) {
            doc = new DocumentoFirmaBBDD();
        } else if(tipoDocumento == TIPO_DOCUMENTO_GESTOR) {
            doc = new DocumentoFirmaGestor();
        }
        
        if(doc != null) {
            doc.setCodDocumento(codDocumento);
            doc.setCodMunicipio(codMunicipio);
            doc.setCodProcedimiento(codProcedimiento);
            doc.setEjercicio(ejercicio);
            doc.setNumExpediente(numeroExpediente);
            doc.setCodTramite(codTramite);
            doc.setOcurrenciaTramite(ocurrenciaTramite);
            doc.setNombreDocumento(nombreDocumento);
            doc.setCodUsuarioAltaDoc(codUsuario);
            doc.setFichero(fichero);
            doc.setParams(params);
            doc.setExtension(extension);
            doc.setTipoMimeContenido(tipoMIME);
            if(!"true".equals(perteneceRelacion))
                doc.setDocRelacion(false); // Se indica que el documento no pertenece a una relación
            else
                doc.setDocRelacion(true);
            
            if(tipoDocumento == TIPO_DOCUMENTO_GESTOR) {
                doc.setCodTramiteVisible(codigoTramiteVisible);
                doc.setCodificacionContenido(codificacionUTF8);
                doc.setListaCarpetas(listaCarpetas);
            }
        }
        
        return doc;
    }

    private boolean isInteger(String dato){
        boolean exito = false;
        try{
            Integer.parseInt(dato);
            exito = true;
        }catch(Exception e){
        }
        return exito;
    }
}
