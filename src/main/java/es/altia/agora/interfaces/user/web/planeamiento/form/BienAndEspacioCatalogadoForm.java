package es.altia.agora.interfaces.user.web.planeamiento.form;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import es.altia.agora.interfaces.user.web.planeamiento.form.mantenimiento.MantenimientoAmbitoForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.Iterator;
import java.util.Collection;

public class BienAndEspacioCatalogadoForm extends ActionForm {

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(BienAndEspacioCatalogadoForm.class.getName());


    private String tipoRegistro;
    private String numero;
    private String anho;
    private String numeroRegistro;
    private String fechaAprobacion;
    private String fechaBaja;
    private String denominacionBien;
    private String codigoDomicilio;
    private String codigoCatalogacion;
    private String catalogacion;
    private String codigoGradoProteccion;
    private String gradoProteccion;
    private String codigoRelacionBien;
    private String relacionBien;
    private String fechaPublicacion;
    private String numeroPublicacion;
    private String observaciones;
    private String archivo;
    private Collection anotaciones;
    private Collection rectificaciones;
    private Collection catalogaciones;
    private Collection gradosProteccion;
    private Collection relacionesBien;
    
    public BienAndEspacioCatalogadoForm() {
        reset();
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
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

    public String getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(String fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getDenominacionBien() {
        return denominacionBien;
    }

    public void setDenominacionBien(String denominacionBien) {
        this.denominacionBien = denominacionBien;
    }

    public String getCodigoDomicilio() {
        return codigoDomicilio;
    }

    public void setCodigoDomicilio(String codigoDomicilio) {
        this.codigoDomicilio = codigoDomicilio;
    }

    public String getCodigoCatalogacion() {
        return codigoCatalogacion;
    }

    public void setCodigoCatalogacion(String codigoCatalogacion) {
        this.codigoCatalogacion = codigoCatalogacion;
    }

    public String getCatalogacion() {
        return catalogacion;
    }

    public void setCatalogacion(String catalogacion) {
        this.catalogacion = catalogacion;
    }

    public String getCodigoGradoProteccion() {
        return codigoGradoProteccion;
    }

    public void setCodigoGradoProteccion(String codigoGradoProteccion) {
        this.codigoGradoProteccion = codigoGradoProteccion;
    }

    public String getGradoProteccion() {
        return gradoProteccion;
    }

    public void setGradoProteccion(String gradoProteccion) {
        this.gradoProteccion = gradoProteccion;
    }

    public String getCodigoRelacionBien() {
        return codigoRelacionBien;
    }

    public void setCodigoRelacionBien(String codigoRelacionBien) {
        this.codigoRelacionBien = codigoRelacionBien;
    }

    public String getRelacionBien() {
        return relacionBien;
    }

    public void setRelacionBien(String relacionBien) {
        this.relacionBien = relacionBien;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
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

    public Collection getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(Collection anotaciones) {
        this.anotaciones = anotaciones;
    }

    public Collection getRectificaciones() {
        return rectificaciones;
    }

    public void setRectificaciones(Collection rectificaciones) {
        this.rectificaciones = rectificaciones;
    }

    public Collection getCatalogaciones() {
        return catalogaciones;
    }

    public void setCatalogaciones(Collection catalogaciones) {
        this.catalogaciones = catalogaciones;
    }

    public Collection getGradosProteccion() {
        return gradosProteccion;
    }

    public void setGradosProteccion(Collection gradosProteccion) {
        this.gradosProteccion = gradosProteccion;
    }

    public Collection getRelacionesBien() {
        return relacionesBien;
    }

    public void setRelacionesBien(Collection relacionesBien) {
        this.relacionesBien = relacionesBien;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        reset();
    }

    public Object clone() {
        BienAndEspacioCatalogadoForm bienForm = new BienAndEspacioCatalogadoForm();

        bienForm.setTipoRegistro(tipoRegistro);
        bienForm.setNumero(numero);
        bienForm.setAnho(anho);
        bienForm.setNumeroRegistro(numeroRegistro);
        bienForm.setFechaAprobacion(fechaAprobacion);
        bienForm.setFechaBaja(fechaBaja);
        bienForm.setDenominacionBien(denominacionBien);
        bienForm.setCodigoDomicilio(codigoDomicilio);
        bienForm.setCodigoCatalogacion(codigoCatalogacion);
        bienForm.setCatalogacion(catalogacion);
        bienForm.setCodigoGradoProteccion(codigoGradoProteccion);
        bienForm.setGradoProteccion(gradoProteccion);
        bienForm.setCodigoRelacionBien(codigoRelacionBien);
        bienForm.setRelacionBien(relacionBien);
        bienForm.setFechaPublicacion(fechaPublicacion);
        bienForm.setNumeroPublicacion(numeroPublicacion);
        bienForm.setObservaciones(observaciones);
        bienForm.setAnotaciones(anotaciones);
        bienForm.setRectificaciones(rectificaciones);
        bienForm.setCatalogaciones(catalogaciones);
        bienForm.setGradosProteccion(gradosProteccion);
        bienForm.setRelacionesBien(relacionesBien);

        return bienForm;

    }

    private void reset() {
        tipoRegistro = null;
        numero = null;
        anho = null;
        numeroRegistro = null;
        fechaAprobacion = null;
        fechaBaja = null;
        denominacionBien = null;
        codigoDomicilio = null;
        codigoCatalogacion = null;
        catalogacion = null;
        codigoGradoProteccion = null;
        gradoProteccion = null;
        codigoRelacionBien = null;
        relacionBien = null;
        fechaPublicacion = null;
        numeroPublicacion = null;
        observaciones = null;
        archivo = null;
        anotaciones = null;
        rectificaciones = null;
        catalogaciones = null;
        gradosProteccion = null;
        relacionesBien = null;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        return errors;
    }

    /* Función que procesa los errores de validación a formato struts */
    private ActionErrors validationException(ValidationException ve,ActionErrors errors){
      Iterator iter = ve.getMessages().get();
      while (iter.hasNext()) {
        Message message = (Message)iter.next();
        errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
      }
      return errors;
    }
}
