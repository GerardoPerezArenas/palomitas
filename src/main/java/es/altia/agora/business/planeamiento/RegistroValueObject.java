package es.altia.agora.business.planeamiento;

import java.io.Serializable;
import java.util.Calendar;

public class RegistroValueObject implements Serializable {

    private Character tipoRegistro;
    private String codigoSubseccion;
    private String codigoTipo;
    private Integer numero;
    private String anho;
    private String numeroRegistro;
    private Calendar fechaAlta;
    private Calendar fechaAprobacion;
    private Calendar fechaVigencia;
    private Calendar fechaBaja;
    private String codigoProcedimiento;
    private Integer numeroProcedimiento;
    private String codigoAmbito;
    private String parcela;
    private Character promotor;
    private String codigoOrganoAprobacion;
    private String objetoConvenio;
    private String denominacionBien;
    private Integer codigoDomicilio;
    private String codigoCatalogacion;
    private String codigoGradoProteccion;
    private String codigoRelacionBien;
    private Calendar fechaPublicacion;
    private String numeroPublicacion;
    private String observaciones;
    private String archivo;

    public RegistroValueObject() {
    }

    public RegistroValueObject(Character tipoRegistro, String codigoSubseccion, String codigoTipo, Integer numero,
                               String anho, String numeroRegistro, Calendar fechaAlta, Calendar fechaAprobacion,
                               Calendar fechaVigencia, Calendar fechaBaja, String codigoProcedimiento,
                               Integer numeroProcedimiento, String codigoAmbito, String parcela, Character promotor,
                               String codigoOrganoAprobacion, String objetoConvenio, String denominacionBien,
                               Integer codigoDomicilio, String codigoCatalogacion, String codigoGradoProteccion,
                               String codigoRelacionBien, Calendar fechaPublicacion, String numeroPublicacion,
                               String observaciones, String archivo) {
        this.tipoRegistro = tipoRegistro;
        this.codigoSubseccion = codigoSubseccion;
        this.codigoTipo = codigoTipo;
        this.numero = numero;
        this.anho = anho;
        this.numeroRegistro = numeroRegistro;
        this.fechaAlta = fechaAlta;
        this.fechaAprobacion = fechaAprobacion;
        this.fechaVigencia = fechaVigencia;
        this.fechaBaja = fechaBaja;
        this.codigoProcedimiento = codigoProcedimiento;
        this.numeroProcedimiento = numeroProcedimiento;
        this.codigoAmbito = codigoAmbito;
        this.parcela = parcela;
        this.promotor = promotor;
        this.codigoOrganoAprobacion = codigoOrganoAprobacion;
        this.objetoConvenio = objetoConvenio;
        this.denominacionBien = denominacionBien;
        this.codigoDomicilio = codigoDomicilio;
        this.codigoCatalogacion = codigoCatalogacion;
        this.codigoGradoProteccion = codigoGradoProteccion;
        this.codigoRelacionBien = codigoRelacionBien;
        this.fechaPublicacion = fechaPublicacion;
        this.numeroPublicacion = numeroPublicacion;
        this.observaciones = observaciones;
        this.archivo = archivo;
    }

    public Character getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Character tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getCodigoSubseccion() {
        return codigoSubseccion;
    }

    public void setCodigoSubseccion(String codigoSubseccion) {
        this.codigoSubseccion = codigoSubseccion;
    }

    public String getCodigoTipo() {
        return codigoTipo;
    }

    public void setCodigoTipo(String codigoTipo) {
        this.codigoTipo = codigoTipo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getAnho() {
        return anho;
    }

    public void setAnho(String anho) {
        this.anho = anho;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public Calendar getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Calendar getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Calendar fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Calendar getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Calendar fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public Calendar getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Calendar fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getCodigoProcedimiento() {
        return codigoProcedimiento;
    }

    public void setCodigoProcedimiento(String codigoProcedimiento) {
        this.codigoProcedimiento = codigoProcedimiento;
    }

    public Integer getNumeroProcedimiento() {
        return numeroProcedimiento;
    }

    public void setNumeroProcedimiento(Integer numeroProcedimiento) {
        this.numeroProcedimiento = numeroProcedimiento;
    }

    public String getCodigoAmbito() {
        return codigoAmbito;
    }

    public void setCodigoAmbito(String codigoAmbito) {
        this.codigoAmbito = codigoAmbito;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public Character getPromotor() {
        return promotor;
    }

    public void setPromotor(Character promotor) {
        this.promotor = promotor;
    }

    public String getCodigoOrganoAprobacion() {
        return codigoOrganoAprobacion;
    }

    public void setCodigoOrganoAprobacion(String codigoOrganoAprobacion) {
        this.codigoOrganoAprobacion = codigoOrganoAprobacion;
    }

    public String getObjetoConvenio() {
        return objetoConvenio;
    }

    public void setObjetoConvenio(String objetoConvenio) {
        this.objetoConvenio = objetoConvenio;
    }

    public String getDenominacionBien() {
        return denominacionBien;
    }

    public void setDenominacionBien(String denominacionBien) {
        this.denominacionBien = denominacionBien;
    }

    public Integer getCodigoDomicilio() {
        return codigoDomicilio;
    }

    public void setCodigoDomicilio(Integer codigoDomicilio) {
        this.codigoDomicilio = codigoDomicilio;
    }

    public String getCodigoCatalogacion() {
        return codigoCatalogacion;
    }

    public void setCodigoCatalogacion(String codigoCatalogacion) {
        this.codigoCatalogacion = codigoCatalogacion;
    }

    public String getCodigoGradoProteccion() {
        return codigoGradoProteccion;
    }

    public void setCodigoGradoProteccion(String codigoGradoProteccion) {
        this.codigoGradoProteccion = codigoGradoProteccion;
    }

    public String getCodigoRelacionBien() {
        return codigoRelacionBien;
    }

    public void setCodigoRelacionBien(String codigoRelacionBien) {
        this.codigoRelacionBien = codigoRelacionBien;
    }

    public Calendar getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Calendar fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getNumeroPublicacion() {
        return numeroPublicacion;
    }

    public void setNumeroPublicacion(String numeroPublicacion) {
        this.numeroPublicacion = numeroPublicacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String toString() {
        return "tipoRegistro: " + tipoRegistro + " | codigoSubseccion: " + codigoSubseccion + " | codigoTipo: " +
                codigoTipo + " | numero: " + numero + " | año: " + anho + " | numeroRegistro: " + numeroRegistro +
                " | fechaAlta: " + fechaAlta + " | fechaAprobacion: " + fechaAprobacion + " | fechaVigencia: " +
                " | fechaBaja: " + fechaBaja + " | codigoProcedimiento: " + codigoProcedimiento +
                " | numeroProcedimiento: " + numeroProcedimiento + " | codigoAmbito: " + codigoAmbito +
                " | parcela: " + parcela + " | promotor: " + promotor + " | codigoOrganoAprobacion: " +
                codigoOrganoAprobacion + " | objetoConvenio: " + objetoConvenio + " | denominacionBien: " +
                denominacionBien + " | codigoDomicilio: " + codigoDomicilio + " | codigoCatalogacion: " +
                codigoCatalogacion + " | codigoGradoProteccion: " + codigoGradoProteccion + " | codigoRelacionBien: " +
                codigoRelacionBien + " | fechaPublicacion: " + fechaPublicacion + " | numeroPublicacion: " +
                numeroPublicacion + " | observaciones: " + observaciones + " | archivo: " + archivo;
    }
}