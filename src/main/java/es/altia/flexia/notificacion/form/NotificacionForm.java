package es.altia.flexia.notificacion.form;

import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionForm;

import java.util.ArrayList;

import es.altia.flexia.notificacion.vo.*;
import org.apache.struts.upload.FormFile;

public class NotificacionForm extends ActionForm {

    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(NotificacionForm.class.getName());


    private String actoNotificado;
    private int caducidadNotificacion;
    private String textoNotificacion;
    private ArrayList<AdjuntoNotificacionVO> adjuntos=new ArrayList<AdjuntoNotificacionVO>();
    private ArrayList<AutorizadoNotificacionVO> autorizados=new ArrayList<AutorizadoNotificacionVO>();
    private ArrayList<AdjuntoNotificacionVO> adjuntosExternos = new ArrayList<AdjuntoNotificacionVO>();
    

    private String aplicacion;
    private String codigoTipoNotificacion;
    private String nombreExpediente;
    private String firma;
    private String numExpediente;
    private String codigoProcedimiento;
    private int ejercicio;
    private int codigoMunicipio ;
    private int codigoTramite;
    private int ocurrenciaTramite;
    private String estadoNotificacion;
    private String tipoNotificacion;
    private String descripcionProcedimiento;
    private FormFile ficheroExterno;
    private int codNotificacion;
    private boolean admiteFirmaCertificadoOrganismo;
    private String datosCertificado;
    private String asuntoCertificado;
    private String emisorCertificado;
    private String nombrePersona;
    private String nombreFirmante;
    private String nifFirmante;
    private String validezCertificado;
    private boolean firmaValida;
    private boolean extraccionDatosCertificado=false;
    

    public boolean getExtraccionDatosCertificado(){
        return this.extraccionDatosCertificado;
    }

    public void setExtraccionDatosCertificado(boolean flag){
        this.extraccionDatosCertificado =flag;
    }
    
    public boolean getAdmiteFirmaCertificadoOrganismo(){
        return this.admiteFirmaCertificadoOrganismo;
    }

    
    public void setAdmiteFirmaCertificadoOrganismo(boolean flag){
        this.admiteFirmaCertificadoOrganismo = flag;
    }
    
    public String getActoNotificado() {
        return actoNotificado;
    }

    public void setActoNotificado(String actoNotificado) {
        this.actoNotificado = actoNotificado;
    }

    public ArrayList<AdjuntoNotificacionVO> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(ArrayList<AdjuntoNotificacionVO> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public ArrayList<AutorizadoNotificacionVO> getAutorizados() {
        return autorizados;
    }

    public void setAutorizados(ArrayList<AutorizadoNotificacionVO> autorizados) {
        this.autorizados = autorizados;
    }

    public int getCaducidadNotificacion() {
        return caducidadNotificacion;
    }

    public void setCaducidadNotificacion(int caducidadNotificacion) {
        this.caducidadNotificacion = caducidadNotificacion;
    }

    
    public String getTextoNotificacion() {
        return textoNotificacion;
    }

    public void setTextoNotificacion(String textoNotificacion) {
        this.textoNotificacion = textoNotificacion;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public int getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(int codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    public String getCodigoProcedimiento() {
        return codigoProcedimiento;
    }

    public void setCodigoProcedimiento(String codigoProcedimiento) {
        this.codigoProcedimiento = codigoProcedimiento;
    }

    public String getCodigoTipoNotificacion() {
        return codigoTipoNotificacion;
    }

    public void setCodigoTipoNotificacion(String codigoTipoNotificacion) {
        this.codigoTipoNotificacion = codigoTipoNotificacion;
    }

    public int getCodigoTramite() {
        return codigoTramite;
    }

    public void setCodigoTramite(int codigoTramite) {
        this.codigoTramite = codigoTramite;
    }

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEstadoNotificacion() {
        return estadoNotificacion;
    }

    public void setEstadoNotificacion(String estadoNotificacion) {
        this.estadoNotificacion = estadoNotificacion;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getNombreExpediente() {
        return nombreExpediente;
    }

    public void setNombreExpediente(String nombreExpediente) {
        this.nombreExpediente = nombreExpediente;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    /**
     * @return the adjuntosExternos
     */
    public ArrayList<AdjuntoNotificacionVO> getAdjuntosExternos() {
        return adjuntosExternos;
    }

    /**
     * @param adjuntosExternos the adjuntosExternos to set
     */
    public void setAdjuntosExternos(ArrayList<AdjuntoNotificacionVO> adjuntosExternos) {
        this.adjuntosExternos = adjuntosExternos;
    }

    /**
     * @return the descripcionProcedimiento
     */
    public String getDescripcionProcedimiento() {
        return descripcionProcedimiento;
    }

    /**
     * @param descripcionProcedimiento the descripcionProcedimiento to set
     */
    public void setDescripcionProcedimiento(String descripcionProcedimiento) {
        this.descripcionProcedimiento = descripcionProcedimiento;
    }

    
    /**
     * @return the ficheroExterno
     */
    public FormFile getFicheroExterno() {
        return ficheroExterno;
    }

    /**
     * @param ficheroExterno the ficheroExterno to set
     */
    public void setFicheroExterno(FormFile ficheroExterno) {
        this.ficheroExterno = ficheroExterno;
    }

    /**
     * @return the codNotificacion
     */
    public int getCodNotificacion() {
        return codNotificacion;
    }

    /**
     * @param codNotificacion the codNotificacion to set
     */
    public void setCodNotificacion(int codNotificacion) {
        this.codNotificacion = codNotificacion;
    }

    /**
     * @return the asuntoCertificado
     */
    public String getAsuntoCertificado() {
        return asuntoCertificado;
    }

    /**
     * @param asuntoCertificado the asuntoCertificado to set
     */
    public void setAsuntoCertificado(String asuntoCertificado) {
        this.asuntoCertificado = asuntoCertificado;
    }

    /**
     * @return the emisorCertificado
     */
    public String getEmisorCertificado() {
        return emisorCertificado;
    }

    /**
     * @param emisorCertificado the emisorCertificado to set
     */
    public void setEmisorCertificado(String emisorCertificado) {
        this.emisorCertificado = emisorCertificado;
    }

    /**
     * @return the nombrePersona
     */
    public String getNombrePersona() {
        return nombrePersona;
    }

    /**
     * @param nombrePersona the nombrePersona to set
     */
    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    /**
     * @return the nifFirmante
     */
    public String getNifFirmante() {
        return nifFirmante;
    }

    /**
     * @param nifFirmante the nifFirmante to set
     */
    public void setNifFirmante(String nifFirmante) {
        this.nifFirmante = nifFirmante;
    }

    /**
     * @return the validezCertificado
     */
    public String getValidezCertificado() {
        return validezCertificado;
    }

    /**
     * @param validezCertificado the validezCertificado to set
     */
    public void setValidezCertificado(String validezCertificado) {
        this.validezCertificado = validezCertificado;
    }

    /**
     * @return the firmaValida
     */
    public boolean getFirmaValida() {
        return firmaValida;
    }

    /**
     * @param firmaValida the firmaValida to set
     */
    public void setFirmaValida(boolean firmaValida) {
        this.firmaValida = firmaValida;
    }

    /**
     * @return the datosCertificado
     */
    public String getDatosCertificado() {
        return datosCertificado;
    }

    /**
     * @param datosCertificado the datosCertificado to set
     */
    public void setDatosCertificado(String datosCertificado) {
        this.datosCertificado = datosCertificado;
    }

    /**
     * @return the nombreFirmante
     */
    public String getNombreFirmante() {
        return nombreFirmante;
    }

    /**
     * @param nombreFirmante the nombreFirmante to set
     */
    public void setNombreFirmante(String nombreFirmante) {
        this.nombreFirmante = nombreFirmante;
    }

   

}