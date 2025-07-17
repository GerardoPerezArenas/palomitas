package es.altia.agora.business.sge.plugin.documentos.vo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public interface DocumentoFirma {
    public int getCodMunicipio();
    public void setCodMunicipio(int codMunicipio);
    public String getCodProcedimiento();
    public void setCodProcedimiento(String codProcedimiento);
    public int getEjercicio();
    public void setEjercicio(int ejercicio);
    //Propiedades de documento
    public int getCodDocumento();
    public void setCodDocumento(int codDocumento);
    public Date getFechaAltaDocumento();
    public void setFechaAltaDocumento(Date fechaAltaDocumento);
    public Date getFechaModDocumento();
    public void setFechaModDocumento(Date fechaModDocumento);
    public int getCodUsuarioAltaDoc();
    public void setCodUsuarioAltaDoc(int codUsuarioAltaDoc);
    public int getCodUsuarioModDoc();
    public void setCodUsuarioModDoc(int codUsuarioModDoc);
    public byte[] getFichero();
    public void setFichero(byte[] fichero);
    public String getNombreDocumento();
    public void setNombreDocumento(String nombreDocumento);
    public int getNumeroDocumento();
    public void setNumeroDocumento(int numeroDocumento);
    // Documento de tramitacion
    public String getNumExpediente();
    public void setNumExpediente(String numExpediente);
    public int getCodTramite();
    public void setCodTramite(int codTramite);
    public String getCodTramiteVisible();
    public void setCodTramiteVisible(String codTramiteVisible);
    public int getOcurrenciaTramite();
    public void setOcurrenciaTramite(int ocurrenciaTramite);
    //Propiedades de firma
    public long getIdMetadatos();
    public void setIdMetadatos(long idMetadatos);
    public String getEstadoFirma();
    public void setEstadoFirma(String estadoFirma);
    public String getExpFd();
    public void setExpFd(String expFd);
    public String getDocFd();
    public void setDocFd(String docFd);
    public Integer getFirFd();
    public void setFirFd(Integer firFd);
    public Date getFechaInforme();
    public void setFechaInforme(Date fechaInforme);
    // Datos referentes a la plantilla del documento
    public String getNomUsuarioAltaDoc();
    public void setNomUsuarioAltaDoc(String nomUsuarioAltaDoc);
    public String getNomUsuarioModDoc() ;
    public void setNomUsuarioModDoc(String nomUsuarioModDoc);
    public String getPorInteresado();
    public void setPorInteresado(String porInteresado);
    public String getParaRelacion();
    public void setParaRelacion(String paraRelacion);
    public boolean isDocRelacion();
    public void setDocRelacion(boolean docRelacion);
    public String getEditorTexto();
    public void setEditorTexto(String editorTexto);
    public String getExtension();
    public void setExtension(String extension);
    public String getTipoMimeContenido();
    public void setTipoMimeContenido(String tipoMimeContenido);
    public String getCodificacionContenido();
    public void setCodificacionContenido(String codificacionContenido);
    // Otras propiedades
    public String[] getParams();
    public void setParams(String[] params);
    public ArrayList<String> getListaCarpetas();
    public void setListaCarpetas(ArrayList<String> listaCarpetas);
    
    public String getFechaInformeAsString();
    public void setFechaInformeAsString(String fechaInformeAsString);
    public String getFechaAltaDocumentoAsString();
    public void setFechaAltaDocumentoAsString(String fechaAltaDocumentoAsString);
    public String getFechaModDocumentoAsString();
    public void setFechaModDocumentoAsString(String fechaModDocumentoAsString);
}
