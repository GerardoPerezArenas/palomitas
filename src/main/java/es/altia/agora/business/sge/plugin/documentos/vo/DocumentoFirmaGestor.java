package es.altia.agora.business.sge.plugin.documentos.vo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DocumentoFirmaGestor implements DocumentoFirma{
    private int codMunicipio; //Se corresponde con el campo CRD_MUN
    private String codProcedimiento; //Se corresponde con el campo CRD_PRO
    private int ejercicio; //Se corresponde con el campo CRD_EJE
    //Propiedades de documento
    private int codDocumento; //Se corresponde con el campo CRD_NUD
    private Date fechaAltaDocumento; //Se corresponde con el campo CRD_FAL
    private Date fechaModDocumento; //Se corresponde con el campo CRD_FMO
    private int codUsuarioAltaDoc; //Se corresponde con el campo CRD_USC
    private String nomUsuarioAltaDoc; // Se corresponde con USU_NOM haciendo una join con A_USU por CRD_USC
    private int codUsuarioModDoc; //Se corresponde con el campo CRD_USM
    private String nomUsuarioModDoc; // Se corresponde con USU_NOM haciendo una join con A_USU por CRD_USM
    private byte[] fichero; //Se corresponde con el campo CRD_FIL
    private String nombreDocumento; //Se corresponde con el campo CRD_DES
    private int numeroDocumento; //Se corresponde con el campo CRD_DOT
    // Documento de tramitacion
    private String numExpediente; //Se corresponde con el campo CRD_NUM
    private int codTramite; //Se corresponde con el campo CRD_TRA
    private String codTramiteVisible;
    private int ocurrenciaTramite; //Se corresponde con el campo CRD_OCU
    //Propiedades de firma
    private long idMetadatos; //Se corresponde con el campo CRD_ID_METADATO
    private String estadoFirma = null; //Se corresponde con el campo CRD_FIR_EST
    private String expFd = null; //Se corresponde con el campo CRD_EXP_FD
    private String docFd = null; //Se corresponde con el campo CRD_DOC_FD
    private Integer firFd = null; //Se corresponde con el campo CRD_FIR_FD
    private Date fechaInforme = null; //Se corresponde con el campo CRD_FINF
    // Datos referentes a la plantilla del documento
    private String porInteresado = "N"; // Se corresponde con PLT_INT
    private String paraRelacion = "N"; // Se corresponde con PLT_REL
    private boolean docRelacion;
    private String editorTexto; // Se corresponde con PLT_EDITOR_TEXTO
    private String extension;
    private String tipoMimeContenido;
    private String codificacionContenido;
    // Otras propiedades
    private String[] params;
    private ArrayList<String> listaCarpetas;
    
    private String fechaInformeAsString;
    private String fechaAltaDocumentoAsString;
    private String fechaModDocumentoAsString;

    /**
     * @return the codMunicipio
     */
    public int getCodMunicipio() {
        return codMunicipio;
    }

    /**
     * @param codMunicipio the codMunicipio to set
     */
    public void setCodMunicipio(int codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    /**
     * @return the codProcedimiento
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * @param codProcedimiento the codProcedimiento to set
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    /**
     * @return the ejercicio
     */
    public int getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the codDocumento
     */
    public int getCodDocumento() {
        return codDocumento;
    }

    /**
     * @param codDocumento the codDocumento to set
     */
    public void setCodDocumento(int codDocumento) {
        this.codDocumento = codDocumento;
    }

    /**
     * @return the fechaAltaDocumento
     */
    public Date getFechaAltaDocumento() {
        return fechaAltaDocumento;
    }

    /**
     * @param fechaAltaDocumento the fechaAltaDocumento to set
     */
    public void setFechaAltaDocumento(Date fechaAltaDocumento) {
        this.fechaAltaDocumento = fechaAltaDocumento;
    }

    /**
     * @return the fechaModDocumento
     */
    public Date getFechaModDocumento() {
        return fechaModDocumento;
    }

    /**
     * @param fechaModDocumento the fechaModDocumento to set
     */
    public void setFechaModDocumento(Date fechaModDocumento) {
        this.fechaModDocumento = fechaModDocumento;
    }

    /**
     * @return the codUsuarioAltaDoc
     */
    public int getCodUsuarioAltaDoc() {
        return codUsuarioAltaDoc;
    }

    /**
     * @param codUsuarioAltaDoc the codUsuarioAltaDoc to set
     */
    public void setCodUsuarioAltaDoc(int codUsuarioAltaDoc) {
        this.codUsuarioAltaDoc = codUsuarioAltaDoc;
    }

    /**
     * @return the codUsuarioModDoc
     */
    public int getCodUsuarioModDoc() {
        return codUsuarioModDoc;
    }

    /**
     * @param codUsuarioModDoc the codUsuarioModDoc to set
     */
    public void setCodUsuarioModDoc(int codUsuarioModDoc) {
        this.codUsuarioModDoc = codUsuarioModDoc;
    }

    /**
     * @return the fichero
     */
    public byte[] getFichero() {
        return fichero;
    }

    /**
     * @param fichero the fichero to set
     */
    public void setFichero(byte[] fichero) {
        this.fichero = fichero;
    }

    /**
     * @return the nombreDocumento
     */
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    /**
     * @param nombreDocumento the nombreDocumento to set
     */
    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    /**
     * @return the numeroDocumento
     */
    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * @return the codTramite
     */
    public int getCodTramite() {
        return codTramite;
    }

    /**
     * @param codTramite the codTramite to set
     */
    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    /**
     * @return the ocurrenciaTramite
     */
    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    /**
     * @param ocurrenciaTramite the ocurrenciaTramite to set
     */
    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    /**
     * @return the idMetadatos
     */
    public long getIdMetadatos() {
        return idMetadatos;
    }

    /**
     * @param idMetadatos the idMetadatos to set
     */
    public void setIdMetadatos(long idMetadatos) {
        this.idMetadatos = idMetadatos;
    }

    /**
     * @return the estadoFirma
     */
    public String getEstadoFirma() {
        return estadoFirma;
    }

    /**
     * @param estadoFirma the estadoFirma to set
     */
    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    /**
     * @return the expFd
     */
    public String getExpFd() {
        return expFd;
    }

    /**
     * @param expFd the expFd to set
     */
    public void setExpFd(String expFd) {
        this.expFd = expFd;
    }

    /**
     * @return the docFd
     */
    public String getDocFd() {
        return docFd;
    }

    /**
     * @param docFd the docFd to set
     */
    public void setDocFd(String docFd) {
        this.docFd = docFd;
    }

    /**
     * @return the firFd
     */
    public Integer getFirFd() {
        return firFd;
    }

    /**
     * @param firFd the firFd to set
     */
    public void setFirFd(Integer firFd) {
        this.firFd = firFd;
    }

    /**
     * @return the fechaInforme
     */
    public Date getFechaInforme() {
        return fechaInforme;
    }

    /**
     * @param fechaInforme the fechaInforme to set
     */
    public void setFechaInforme(Date fechaInforme) {
        this.fechaInforme = fechaInforme;
    }

    /**
     * @return the nomUsuarioAltaDoc
     */
    public String getNomUsuarioAltaDoc() {
        return nomUsuarioAltaDoc;
    }

    /**
     * @param nomUsuarioAltaDoc the nomUsuarioAltaDoc to set
     */
    public void setNomUsuarioAltaDoc(String nomUsuarioAltaDoc) {
        this.nomUsuarioAltaDoc = nomUsuarioAltaDoc;
    }

    /**
     * @return the nomUsuarioModDoc
     */
    public String getNomUsuarioModDoc() {
        return nomUsuarioModDoc;
    }

    /**
     * @param nomUsuarioModDoc the nomUsuarioModDoc to set
     */
    public void setNomUsuarioModDoc(String nomUsuarioModDoc) {
        this.nomUsuarioModDoc = nomUsuarioModDoc;
    }

    /**
     * @return the porInteresado
     */
    public String getPorInteresado() {
        return porInteresado;
    }

    /**
     * @param porInteresado the porInteresado to set
     */
    public void setPorInteresado(String porInteresado) {
        this.porInteresado = porInteresado;
    }

    /**
     * @return the paraRelacion
     */
    public String getParaRelacion() {
        return paraRelacion;
    }

    /**
     * @param paraRelacion the paraRelacion to set
     */
    public void setParaRelacion(String paraRelacion) {
        this.paraRelacion = paraRelacion;
    }

    /**
     * @return the editorTexto
     */
    public String getEditorTexto() {
        return editorTexto;
    }

    /**
     * @param editorTexto the editorTexto to set
     */
    public void setEditorTexto(String editorTexto) {
        this.editorTexto = editorTexto;
    }

    public String getFechaInformeAsString() {
        return fechaInformeAsString;
    }

    public void setFechaInformeAsString(String fechaInformeAsString) {
        this.fechaInformeAsString = fechaInformeAsString;
    }

    public String getFechaAltaDocumentoAsString() {
        return fechaAltaDocumentoAsString;
    }

    public void setFechaAltaDocumentoAsString(String fechaAltaDocumentoAsString) {
        this.fechaAltaDocumentoAsString = fechaAltaDocumentoAsString;
    }

    public String getFechaModDocumentoAsString() {
        return fechaModDocumentoAsString;
    }

    public void setFechaModDocumentoAsString(String fechaModDocumentoAsString) {
        this.fechaModDocumentoAsString = fechaModDocumentoAsString;
    }
    
    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String getTipoMimeContenido() {
        return tipoMimeContenido;
    }

    @Override
    public void setTipoMimeContenido(String tipoMimeContenido) {
        this.tipoMimeContenido = tipoMimeContenido;
    }

    /**
     * @return the codTramiteVisible
     */
    public String getCodTramiteVisible() {
        return codTramiteVisible;
    }

    /**
     * @param codTramiteVisible the codTramiteVisible to set
     */
    public void setCodTramiteVisible(String codTramiteVisible) {
        this.codTramiteVisible = codTramiteVisible;
    }

    /**
     * @return the codificacionContenido
     */
    public String getCodificacionContenido() {
        return codificacionContenido;
    }

    /**
     * @param codificacionContenido the codificacionContenido to set
     */
    public void setCodificacionContenido(String codificacionContenido) {
        this.codificacionContenido = codificacionContenido;
    }

    /**
     * @return the params
     */
    public String[] getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(String[] params) {
        this.params = params;
    }

    /**
     * @return the listaCarpetas
     */
    public ArrayList<String> getListaCarpetas() {
        return listaCarpetas;
    }

    /**
     * @param listaCarpetas the listaCarpetas to set
     */
    public void setListaCarpetas(ArrayList<String> listaCarpetas) {
        this.listaCarpetas = listaCarpetas;
    }

    /**
     * @return the docRelacion
     */
    public boolean isDocRelacion() {
        return docRelacion;
    }

    /**
     * @param docRelacion the docRelacion to set
     */
    public void setDocRelacion(boolean docRelacion) {
        this.docRelacion = docRelacion;
    }
    
    
}
