package es.altia.agora.business.portafirmas.documentofirma.vo;

import es.altia.util.commons.DateOperations;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class DocumentoCustomVO implements Serializable {

    public static final int DOCUMENTO_EXPEDIENTE = 0;
    public static final int DOCUMENTO_RELACION = 1;
    public static final int DOCUMENTO_EXTERNO = 2;
    public static final int DOCUMENTO_PROCEDIMIENTO = 3;
    public static final int DOCUMENTO_EXTERNO_NOTIFICACION = 4;

    private int pIdMunicipio;
    private String pIdProcedimiento;
    private int pIdEjercicio;
    private String pIdNumeroExpediente;
    private int pIdTramite;
    private int pIdOcurrenciaTramite;
    private int pIdNumeroDocumento;
    private int pIdPresentado;
    private int pUsuarioFirmante;
    private String pEstadoFirma;
    private byte[] pFirma;
	private Calendar pFechaFirma;
	private String pObservaciones;
    private Integer pIdUsuarioDelegadoFirmante;
    private String pNombreProcedimiento;
    private String pNombreTramite;
    private String pNombreDocumento;
    private Calendar pFechaEnvioFirma;
    private int pTipoDocumento;
    private int pIdNumFirma;
    private String firmaBase64;
    private String finalizaRechazo;
    private String editorTexto;
    private String tipoMime;

    public DocumentoCustomVO() {}


    public DocumentoCustomVO(int pIdMunicipio, String pIdProcedimiento, int pIdEjercicio, String pIdNumeroExpediente,
                             int pIdTramite, int pIdOcurrenciaTramite, int pIdNumeroDocumento, int pUsuarioFirmante,
                             String pEstadoFirma, byte[] pFirma, Calendar pFechaFirma, String pObservaciones,
                             Integer pIdUsuarioDelegadoFirmante, String pNombreProcedimiento, String pNombreTramite,
                             String pNombreDocumento, Calendar pFechaEnvioFirma, int pTipoDocumento) {

        this.pIdMunicipio = pIdMunicipio;
        this.pIdProcedimiento = pIdProcedimiento;
        this.pIdEjercicio = pIdEjercicio;
        this.pIdNumeroExpediente = pIdNumeroExpediente;
        this.pIdTramite = pIdTramite;
        this.pIdOcurrenciaTramite = pIdOcurrenciaTramite;
        this.pIdNumeroDocumento = pIdNumeroDocumento;
        this.pUsuarioFirmante = pUsuarioFirmante;
        this.pEstadoFirma = pEstadoFirma;
        this.pFirma = pFirma;
        this.pFechaFirma = pFechaFirma;
        this.pObservaciones = pObservaciones;
        this.pIdUsuarioDelegadoFirmante = pIdUsuarioDelegadoFirmante;
        this.pNombreProcedimiento = pNombreProcedimiento;
        this.pNombreTramite = pNombreTramite;
        this.pNombreDocumento = pNombreDocumento;
        this.pFechaEnvioFirma = pFechaEnvioFirma;
        this.pTipoDocumento = pTipoDocumento;
    }
    
     public DocumentoCustomVO(int pIdMunicipio, String pIdProcedimiento, int pIdEjercicio, String pIdNumeroExpediente,
                             int pIdTramite, int pIdOcurrenciaTramite, int pIdNumeroDocumento, int pUsuarioFirmante,
                             String pEstadoFirma, byte[] pFirma, Calendar pFechaFirma, String pObservaciones,
                             Integer pIdUsuarioDelegadoFirmante, String pNombreProcedimiento, String pNombreTramite,
                             String pNombreDocumento, Calendar pFechaEnvioFirma, int pTipoDocumento, String finalizaRechazo ) {

        this.pIdMunicipio = pIdMunicipio;
        this.pIdProcedimiento = pIdProcedimiento;
        this.pIdEjercicio = pIdEjercicio;
        this.pIdNumeroExpediente = pIdNumeroExpediente;
        this.pIdTramite = pIdTramite;
        this.pIdOcurrenciaTramite = pIdOcurrenciaTramite;
        this.pIdNumeroDocumento = pIdNumeroDocumento;
        this.pUsuarioFirmante = pUsuarioFirmante;
        this.pEstadoFirma = pEstadoFirma;
        this.pFirma = pFirma;
        this.pFechaFirma = pFechaFirma;
        this.pObservaciones = pObservaciones;
        this.pIdUsuarioDelegadoFirmante = pIdUsuarioDelegadoFirmante;
        this.pNombreProcedimiento = pNombreProcedimiento;
        this.pNombreTramite = pNombreTramite;
        this.pNombreDocumento = pNombreDocumento;
        this.pFechaEnvioFirma = pFechaEnvioFirma;
        this.pTipoDocumento = pTipoDocumento;
        this.finalizaRechazo = finalizaRechazo;
    }

    public int getIdNumFirma() {
        return pIdNumFirma;
    }

    public void setIdNumFirma(int idFirma) {
        this.pIdNumFirma = idFirma;
    }

    public int getIdPresentado() {
        return pIdPresentado;
    }

    public void setIdPresentado(int pIdPresentado) {
        this.pIdPresentado = pIdPresentado;
    }

        
    public int getIdMunicipio() {
        return pIdMunicipio;
    }

    public void setIdMunicipio(int pIdMunicipio) {
        this.pIdMunicipio = pIdMunicipio;
    }

    public String getIdProcedimiento() {
        return pIdProcedimiento;
    }

    public void setIdProcedimiento(String pIdProcedimiento) {
        this.pIdProcedimiento = pIdProcedimiento;
    }

    public int getIdEjercicio() {
        return pIdEjercicio;
    }

    public void setIdEjercicio(int pIdEjercicio) {
        this.pIdEjercicio = pIdEjercicio;
    }

    public String getIdNumeroExpediente() {
        return pIdNumeroExpediente;
    }

    public void setIdNumeroExpediente(String pIdNumeroExpediente) {
        this.pIdNumeroExpediente = pIdNumeroExpediente;
    }

    public int getIdTramite() {
        return pIdTramite;
    }

    public void setIdTramite(int pIdTramite) {
        this.pIdTramite = pIdTramite;
    }

    public int getIdOcurrenciaTramite() {
        return pIdOcurrenciaTramite;
    }

    public void setIdOcurrenciaTramite(int pIdOcurrenciaTramite) {
        this.pIdOcurrenciaTramite = pIdOcurrenciaTramite;
    }

    public int getIdNumeroDocumento() {
        return pIdNumeroDocumento;
    }

    public void setIdNumeroDocumento(int pIdNumeroDocumento) {
        this.pIdNumeroDocumento = pIdNumeroDocumento;
    }

    public int getUsuarioFirmante() {
        return pUsuarioFirmante;
    }

    public void setUsuarioFirmante(int pUsuarioFirmante) {
        this.pUsuarioFirmante = pUsuarioFirmante;
    }

    public String getEstadoFirma() {
        return pEstadoFirma;
    }

    public void setEstadoFirma(String pEstadoFirma) {
        this.pEstadoFirma = pEstadoFirma;
    }

    public byte[] getFirma() {
        return pFirma;
    }

    public void setFirma(byte[] pFirma) {
        this.pFirma = pFirma;
    }

    public Calendar getFechaFirma() {
        return pFechaFirma;
    }

    public void setFechaFirma(Calendar pFechaFirma) {
        this.pFechaFirma = pFechaFirma;
    }

    public String getObservaciones() {
        return pObservaciones;
    }

    public void setObservaciones(String pObservaciones) {
        this.pObservaciones = pObservaciones;
    }

    public Integer getIdUsuarioDelegadoFirmante() {
        return pIdUsuarioDelegadoFirmante;
    }

    public void setIdUsuarioDelegadoFirmante(Integer pIdUsuarioDelegadoFirmante) {
        this.pIdUsuarioDelegadoFirmante = pIdUsuarioDelegadoFirmante;
    }

    public String getNombreProcedimiento() {
        return pNombreProcedimiento;
    }

    public void setNombreProcedimiento(String pNombreProcedimiento) {
        this.pNombreProcedimiento = pNombreProcedimiento;
    }

    public String getNombreTramite() {
        return pNombreTramite;
    }

    public void setNombreTramite(String pNombreTramite) {
        this.pNombreTramite = pNombreTramite;
    }

    public String getNombreDocumento() {
        return pNombreDocumento;
    }

    public void setNombreDocumento(String pNombreDocumento) {
        this.pNombreDocumento = pNombreDocumento;
    }

    public Calendar getFechaEnvioFirma() {
        return pFechaEnvioFirma;
    }

    public void setFechaEnvioFirma(Calendar pFechaEnvioFirma) {
        this.pFechaEnvioFirma = pFechaEnvioFirma;
    }

    public int getTipoDocumento() {
        return pTipoDocumento;
    }

    public void setTipoDocumento(int pTipoDocumento) {
        this.pTipoDocumento = pTipoDocumento;
    }

    public Date getFechaEnvioFirmaAsDate() {
        return DateOperations.toDate(pFechaEnvioFirma);
    }

    public Date getFechaFirmaAsDate() {
        return DateOperations.toDate(pFechaFirma);
    }

    public String getFirmaBase64() {
        return firmaBase64;
    }

    public void setFirmaBase64(String firmaBase64) {
        this.firmaBase64 = firmaBase64;
    }

    public String getFinalizaRechazo() {
        return finalizaRechazo;
    }

    public void setFinalizaRechazo(String finalizaRechazo) {
        this.finalizaRechazo = finalizaRechazo;
    }

    public String getEditorTexto() {
        return editorTexto;
    }

    public void setEditorTexto(String editorTexto) {
        this.editorTexto = editorTexto;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }
    
}
