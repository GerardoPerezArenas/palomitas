package es.altia.agora.business.sge.plugin.documentos.vo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author oscar.rodriguez
 */
public interface Documento{

    public int getCodUsuario();
    public void setCodUsuario(int codUsuario);
    public int getOcurrenciaTramite();
    public void setOcurrenciaTramite(int ocurrenciaTramite);
    public int getEjercicio();
    public void setEjercicio(int ejercicicio);
    public int getCodMunicipio();
    public void setCodMunicipio(int codMunicipio);
    public String getCodProcedimiento();
    public void setCodProcedimiento(String codProcedimiento);
    public String getNumeroExpediente();
    public void setNumeroExpediente(String numeroExpediente);
    public String getNombreDocumento();
    public void setNombreDocumento(String nombreDocumento);
    public int getCodDocumento();
    public void setCodDocumento(int codDocumento);
    public byte[] getFichero();
    public void setFichero(byte[] fichero);
    public String[] getParams();
    public void setParams(String[] params);
    public String getNumeroDocumento();
    public void setNumeroDocumento(String numeroDocumento);
    public boolean isDocRelacion();
    public void setDocRelacion(boolean docRelacion);
    public String getNumeroRelacion();
    public void setNumeroRelacion(String numeroRelacion);
    public String getOpcionGrabar();
    public void setOpcionGrabar(String opcionGrabar);
    public String getExtension();
    public void setExtension(String extension);
    public int getCodTramite();
    public void setCodTramite(int codTramite) ;    
    public String getTipoMimeContenido() ;
    public void setTipoMimeContenido(String tipoMimeContenido) ;

    public ArrayList<String> getListaCarpetas();
    public void setListaCarpetas(ArrayList<String> carpetas);
    
    // Operaciones de documentos de expediente
    public String getOrigen();
    public void setOrigen(String origen);
    public boolean isEliminarSoloAdjunto();
    public void setEliminarSoloAdjunto(boolean flag);
    public boolean isModificarAdjuntoDocExpediente();
    public void setModificarAdjuntoDocExpediente(boolean flag);
    
    public String getRutaDocumento();
    public void setRutaDocumento(String rutaDocumento);
    
    // Campo en R_RED para almacenar el identificador del documento
    public long getIdDocumento();
    public void setIdDocumento(long idDocumento);
    
    // Método necesario para dar un documento de registro     
    public int getCodigoDepartamento();
    public void setCodigoDepartamento(int codigoDepartamento);
    public int getCodigoUnidadOrganica();            
    public void setCodigoUnidadOrganica(int codigoUnidadOrganica);        
    public int getEjercicioAnotacion();
    public void setEjercicioAnotacion(int ejercicioAnotacion);
    public long getNumeroRegistro();
    public void setNumeroRegistro(long numeroRegistro);        
    public String getTipoRegistro();        
    public void setTipoRegistro(String tipoRegistro);        
    public String getTipoDocumento();
    public void setTipoDocumento(String tipoDocumento);    
    public String getFechaDocumento();
    public void setFechaDocumento(String fechaDocumento);
    public String getEntregado();        
    public void setEntregado(String entregado);        
    public boolean isDocumentoRegistro();
    public void setDocumentoRegistro(boolean documentoRegistro);    
    public Integer getEstadoDocumentoRegistro();
    public void setEstadoDocumentoRegistro(Integer estadoDocumentoRegistro);        
    public int getLongitudDocumento();
    public void setLongitudDocumento(int longitud);    
    public String getCodTipoDato();
    public void setCodTipoDato(String codigo);    
    public String getObservaciones();
    public void setObservaciones(String observaciones);    
    public int getIdFirma();
    public void setIdFirma(int idFirma);
    
    public boolean esDocumentoProcedimientoSinValor();
    public void setDocumentoProcedimientoSinValor(boolean documentoRegistroProcedimiento);
    public boolean isExpHistorico();
    public void setExpHistorico(boolean expHistorico);
        
    
    // Campo en R_RED para almacenar el identificador del documento guardado en el gestor documental o nulo si se guarda en BBDD
    public String getIdDocGestor();
    public void setIdDocGestor(String idDocGestor);
    
    
        // Metadatos de cotejo del documento
    public String getVersionNTIMetadatos();
    public void setVersionNTIMetadatos(String versionNTIMetadatos);
    public Long getIdDocumentoMetadatos();
    public void setIdDocumentoMetadatos(Long idDocumentoMetadatos);
    public String getOrganoMetadatos();
    public void setOrganoMetadatos(String organoMetadatos);
    public Calendar getFechaCapturaMetadatos();
    public void setFechaCapturaMetadatos(Calendar fechaCapturaMetadatos);
    public Integer getOrigenMetadatos();
    public void setOrigenMetadatos(Integer origenMetadatos);
    public Integer getEstadoElaboracionMetadatos();
    public void setEstadoElaboracionMetadatos(Integer estadoElaboracionMetadatos);
    public String getNombreFormatoMetadatos();
    public void setNombreFormatoMetadatos(String nombreFormatoMetadatos);
    public Integer getTipoDocumentalMetadatos();
    public void setTipoDocumentalMetadatos(Integer tipoDocumentalMetadatos);
    public Integer getTipoFirmaMetadatos();
    public void setTipoFirmaMetadatos(Integer tipoFirmaMetadatos);
    
    // Metadatos de los documentos
    public Long getIdMetadatoDocumento();
    public void setIdMetadatoDocumento(Long idMetadatoDocumento);
    public Boolean isInsertarMetadatosEnBBDD();
    public void setInsertarMetadatosEnBBDD(Boolean insertarMetadatosEnBBDD);
    // Metadatos de los documentos: CSV
    public String getMetadatoDocumentoCsv();
    public void setMetadatoDocumentoCsv(String metadatoDocumentoCsv);
    public String getMetadatoDocumentoCsvAplicacion();
    public void setMetadatoDocumentoCsvAplicacion(String metadatoDocumentoCsvAplicacion);
    public String getMetadatoDocumentoCsvUri();
    public void setMetadatoDocumentoCsvUri(String metadatoDocumentoCsvUri);
}
