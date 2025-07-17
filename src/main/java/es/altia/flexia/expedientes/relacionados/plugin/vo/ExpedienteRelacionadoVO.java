package es.altia.flexia.expedientes.relacionados.plugin.vo;

import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author oscar.rodriguez
 */
public class ExpedienteRelacionadoVO {
    private Connection connection;
    private String codigoOrganizacion;
    private String codigoUsuario;
    private String numExpedienteRelacionado;
    private String numExpedienteAntiguo;
    private String[] params;
    private String codigoDepartamento;
    private String codigoUnidadRegistro;
    private String tipoRegistro;
    private String ejercicioRegistro;
    private String numeroRegistro;
    private String codigoTercero;
    private String versionTercero;
    private String codigoDomicilio;
    private String codProcedimiento;
    private String origen;
    private boolean cerrarConexionFlexia;
    private String nombreProcedimiento;
    
    private ArrayList<String> numExpedientesRelacionados;
    private ArrayList<String> codProcedimientoExpedientesRelacionados;
    
    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * @return the codigoOrganizacion
     */
    public String getCodigoOrganizacion() {
        return codigoOrganizacion;
    }

    /**
     * @param codigoOrganizacion the codigoOrganizacion to set
     */
    public void setCodigoOrganizacion(String codigoOrganizacion) {
        this.codigoOrganizacion = codigoOrganizacion;
    }

    /**
     * @return the codigoUsuario
     */
    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    /**
     * @param codigoUsuario the codigoUsuario to set
     */
    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
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
     * @return the numExpedienteRelacionado
     */
    public String getNumExpedienteRelacionado() {
        return numExpedienteRelacionado;
    }

    /**
     * @param numExpedienteRelacionado the numExpedienteRelacionado to set
     */
    public void setNumExpedienteRelacionado(String numExpedienteRelacionado) {
        this.numExpedienteRelacionado = numExpedienteRelacionado;
    }

    /**
     * @return the codigoDepartamento
     */
    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    /**
     * @param codigoDepartamento the codigoDepartamento to set
     */
    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    /**
     * @return the codigoUnidadRegistro
     */
    public String getCodigoUnidadRegistro() {
        return codigoUnidadRegistro;
    }

    /**
     * @param codigoUnidadRegistro the codigoUnidadRegistro to set
     */
    public void setCodigoUnidadRegistro(String codigoUnidadRegistro) {
        this.codigoUnidadRegistro = codigoUnidadRegistro;
    }

    /**
     * @return the tipoRegistro
     */
    public String getTipoRegistro() {
        return tipoRegistro;
    }

    /**
     * @param tipoRegistro the tipoRegistro to set
     */
    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    /**
     * @return the ejercicioRegistro
     */
    public String getEjercicioRegistro() {
        return ejercicioRegistro;
    }

    /**
     * @param ejercicioRegistro the ejercicioRegistro to set
     */
    public void setEjercicioRegistro(String ejercicioRegistro) {
        this.ejercicioRegistro = ejercicioRegistro;
    }

    /**
     * @return the numeroRegistro
     */
    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    /**
     * @param numeroRegistro the numeroRegistro to set
     */
    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    /**
     * @return the codigoTercero
     */
    public String getCodigoTercero() {
        return codigoTercero;
    }

    /**
     * @param codigoTercero the codigoTercero to set
     */
    public void setCodigoTercero(String codigoTercero) {
        this.codigoTercero = codigoTercero;
    }

    /**
     * @return the versionTercero
     */
    public String getVersionTercero() {
        return versionTercero;
    }

    /**
     * @param versionTercero the versionTercero to set
     */
    public void setVersionTercero(String versionTercero) {
        this.versionTercero = versionTercero;
    }

    /**
     * @return the codigoDomicilio
     */
    public String getCodigoDomicilio() {
        return codigoDomicilio;
    }

    /**
     * @param codigoDomicilio the codigoDomicilio to set
     */
    public void setCodigoDomicilio(String codigoDomicilio) {
        this.codigoDomicilio = codigoDomicilio;
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
     * @return the numExpedienteAntiguo
     */
    public String getNumExpedienteAntiguo() {
        return numExpedienteAntiguo;
    }

    /**
     * @param numExpedienteAntiguo the numExpedienteAntiguo to set
     */
    public void setNumExpedienteAntiguo(String numExpedienteAntiguo) {
        this.numExpedienteAntiguo = numExpedienteAntiguo;
    }

    /**
     * @return the origen
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * @param origen the origen to set
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * @return the cerrarConexionFlexia
     */
    public boolean isCerrarConexionFlexia() {
        return cerrarConexionFlexia;
    }

    /**
     * @param cerrarConexionFlexia the cerrarConexionFlexia to set
     */
    public void setCerrarConexionFlexia(boolean cerrarConexionFlexia) {
        this.cerrarConexionFlexia = cerrarConexionFlexia;
    }

    /**
     * @return the nombreProcedimiento
     */
    public String getNombreProcedimiento() {
        return nombreProcedimiento;
    }

    /**
     * @param nombreProcedimiento the nombreProcedimiento to set
     */
    public void setNombreProcedimiento(String nombreProcedimiento) {
        this.nombreProcedimiento = nombreProcedimiento;
    }

    public ArrayList<String> getCodProcedimientoExpedientesRelacionados() {
        return codProcedimientoExpedientesRelacionados;
    }//getCodProcedimientoExpedientesRelacionados
    public void setCodProcedimientoExpedientesRelacionados(ArrayList<String> codProcedimientoExpedientesRelacionados) {
        this.codProcedimientoExpedientesRelacionados = codProcedimientoExpedientesRelacionados;
    }//setCodProcedimientoExpedientesRelacionados

    public ArrayList<String> getNumExpedientesRelacionados() {
        return numExpedientesRelacionados;
    }//getNumExpedientesRelacionados
    public void setNumExpedientesRelacionados(ArrayList<String> numExpedientesRelacionados) {
        this.numExpedientesRelacionados = numExpedientesRelacionados;
    }//setNumExpedientesRelacionados
    
    private Boolean dejarAnotacionBuzonEntrada = false;
    public Boolean getDejarAnotacionBuzonEntrada() {
        return dejarAnotacionBuzonEntrada;
    }//getDejarAnotacionBuzonEntrada
    public void setDejarAnotacionBuzonEntrada(Boolean dejarAnotacionBuzonEntrada) {
        this.dejarAnotacionBuzonEntrada = dejarAnotacionBuzonEntrada;
    }//setDejarAnotacionBuzonEntrada

}//class