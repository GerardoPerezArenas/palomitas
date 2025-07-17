package es.altia.agora.business.gestionInformes;

import es.altia.technical.ValueObject;
import es.altia.technical.ValidationException;
import es.altia.technical.Messages;

import java.io.Serializable;
import java.util.Vector;

public class SolicitudInformeValueObject implements Serializable, ValueObject {

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getCodSolicitud() {
        return codSolicitud;
    }

    public void setCodSolicitud(String codSolicitud) {
        this.codSolicitud = codSolicitud;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(String codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }


    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(String fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }
    //AÑADIDO PARA EL MODO DE VISUALIZACION DIRECTO O BUZON
    public String getModoVisualizacion() {
        return formato;
    }

    public void setModoVisualizacion(String formato) {
        this.formato = formato;
    }
    //FIN AÑADIDO
    public String getFichero() {
        return fichero;
    }

    public void setFichero(String fichero) {
        this.fichero = fichero;
    }

    public Vector getListaCriterios() {
        return listaCriterios;
    }

    public void setListaCriterios(Vector listaCriterios) {
        this.listaCriterios = listaCriterios;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getCadenaCriterios() {
        return cadenaCriterios;
    }

    public void setCadenaCriterios(String cadenaCriterios) {
        this.cadenaCriterios = cadenaCriterios;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public boolean getTieneCriterioInteresado() {
        return tieneCriterioInteresado;
    }

    public void setTieneCriterioInteresado(boolean tieneCriterioInteresado) {
        this.tieneCriterioInteresado = tieneCriterioInteresado;
    }

    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception es.altia.technical.ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma)) sufijo="_eu";
        boolean correcto = true;
        Messages errors = new Messages();
        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    private String opcion;
    private String codSolicitud;
    private String codUsuario;
    private String Usuario;
    private String codPlantilla;
    private String descripcion;
    private String fechaSolicitud;
    private String fechaGeneracion;
    private String tiempo;
    private String estado;
    private String formato;
    private String fichero;
    private Vector listaCriterios;
    private String procedimiento;
    private String cadenaCriterios;
    private String origen;
    private boolean tieneCriterioInteresado;

/* ******************************************************** */
}
