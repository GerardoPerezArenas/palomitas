package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TramiteModuloIntegracionVO {
    private int codOrganizacion;
    private int codTramite;
    private int ocurrenciaTramite;
    private String codProcedimiento;
    private String numExpediente;
    private int ejercicio;
    private Calendar fechaInicio;
    private Calendar fechaFin;
    private Calendar fechaInicioPlazo;
    private Calendar fechaLimite;
    private Calendar fechaFinPlazo;
    private int codUsuarioInicio;
    private int codUsuarioFin;    
    private int codUnidadTramitadora;
    private String descripcion;
    private String observaciones;

    /**
     * @return the codOrganizacion
     */
    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    /**
     * @param codOrganizacion the codOrganizacion to set
     */
    public void setCodOrganizacion(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
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
     * @return the fechaInicio
     */
    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaInicioAsString(){
        String salida = null;
        if(fechaInicio!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            salida = sf.format(fechaInicio.getTime());
        }
        return salida;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFin
     */
    public Calendar getFechaFin() {
        return fechaFin;
    }

    public String getFechaFinAsString(){
        String salida = null;
        if(fechaFin!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            salida = sf.format(fechaFin.getTime());
        }
        return salida;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(Calendar fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * @return the fechaInicioPlazo
     */
    public Calendar getFechaInicioPlazo() {
        return fechaInicioPlazo;
    }

    public String getFechaInicioPlazoAsString(){
        String salida = null;
        if(fechaInicioPlazo!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            salida = sf.format(fechaInicioPlazo.getTime());
        }
        return salida;
    }

    /**
     * @param fechaInicioPlazo the fechaInicioPlazo to set
     */
    public void setFechaInicioPlazo(Calendar fechaInicioPlazo) {
        this.fechaInicioPlazo = fechaInicioPlazo;
    }

    /**
     * @return the fechaLimite
     */
    public Calendar getFechaLimite() {
        return fechaLimite;
    }

    public String getFechaLimiteAsString(){
        String salida = null;
        if(fechaLimite!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            salida = sf.format(fechaLimite.getTime());
        }
        return salida;
    }

    /**
     * @param fechaLimite the fechaLimite to set
     */
    public void setFechaLimite(Calendar fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    /**
     * @return the fechaFinPlazo
     */
    public Calendar getFechaFinPlazo() {
        return fechaFinPlazo;
    }

    public String getFechaFinPlazoAsString(){
        String salida = null;
        if(fechaFinPlazo!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            salida = sf.format(fechaFinPlazo.getTime());
        }
        return salida;
    }

    /**
     * @param fechaFinPlazo the fechaFinPlazo to set
     */
    public void setFechaFinPlazo(Calendar fechaFinPlazo) {
        this.fechaFinPlazo = fechaFinPlazo;
    }

    /**
     * @return the codUsuarioInicio
     */
    public int getCodUsuarioInicio() {
        return codUsuarioInicio;
    }

    /**
     * @param codUsuarioInicio the codUsuarioInicio to set
     */
    public void setCodUsuarioInicio(int codUsuarioInicio) {
        this.codUsuarioInicio = codUsuarioInicio;
    }

    /**
     * @return the codUsuarioFin
     */
    public int getCodUsuarioFin() {
        return codUsuarioFin;
    }

    /**
     * @param codUsuarioFin the codUsuarioFin to set
     */
    public void setCodUsuarioFin(int codUsuarioFin) {
        this.codUsuarioFin = codUsuarioFin;
    }

    /**
     * @return the cerrado
     */
    public boolean isCerrado() {
        if(fechaFin!=null)
            return true;
        else
            return false;
    }


    /**
     * @return the codUnidadTramitadora
     */
    public int getCodUnidadTramitadora() {
        return codUnidadTramitadora;
    }

    /**
     * @param codUnidadTramitadora the codUnidadTramitadora to set
     */
    public void setCodUnidadTramitadora(int codUnidadTramitadora) {
        this.codUnidadTramitadora = codUnidadTramitadora;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * @param observaciones the observaciones to set
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}