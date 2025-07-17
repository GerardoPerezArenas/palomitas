package es.altia.agora.business.geninformes;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;
import es.altia.agora.business.util.GeneralValueObject;

public class GeneradorInformesValueObject implements Serializable, ValueObject {

    /** Construye un nuevo registroEntradaSalida por defecto. */
    public GeneradorInformesValueObject() {
        super();
    }
    
    public Vector getListaEntidades() {
    	return listaEntidades;
    }
    
    public void setListaEntidades(Vector listaEntidades) {
    	this.listaEntidades = listaEntidades;
    }
    
    public Vector getListaCamposDisponibles() {
    	return listaCamposDisponibles;
    }
    
    public void setListaCamposDisponibles(Vector listaCamposDisponibles) {
    	this.listaCamposDisponibles = listaCamposDisponibles;
    }
    
    public String getCodAplicacion() {
    	return codAplicacion;
    }
    
    public void setCodAplicacion(String codAplicacion) {
    	this.codAplicacion = codAplicacion;
    }
    
    public String getOperacion() {
    	return operacion;
    }
    
    public void setOperacion(String operacion) {
    	this.operacion = operacion;
    }
    
    public GeneralValueObject getGVO() {
    	return gVO;
    }
    
    public void setGVO(GeneralValueObject gVO) {
    	this.gVO = gVO;
    }
    

    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
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

    /** Devuelve un booleano que representa si el estado de este RegistroSaida es válido. */
    public boolean IsValid() { return isValid; }

    private Vector listaEntidades;
    private Vector listaCamposDisponibles;
    private String codAplicacion;
    private String operacion;
    
    private GeneralValueObject gVO = new GeneralValueObject();
    
    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
}
