package es.altia.agora.business.registro;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Vector;


public class BuzonWCValueObject implements Serializable, ValueObject {

    /** Construye un nuevo BuzonWC por defecto. */
    public BuzonWCValueObject() {
        super();
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

	
	public void setIsValid(boolean isValid) {
		this.isValid = isValid; 
	}

	public void setRespOpcion(String respOpcion) {
		this.respOpcion = respOpcion; 
	}

	public void setFecha(String fecha) {
		this.fecha = fecha; 
	}

	public void setNombre(String nombre) {
		this.nombre = nombre; 
	}

	public void setEstado(String estado) {
		this.estado = estado; 
	}

	public void setListaRegistroWC(Vector listaRegistroWC) {
		this.listaRegistroWC = listaRegistroWC; 
	}

	public boolean getIsValid() {
		return (this.isValid); 
	}

	public String getRespOpcion() {
		return (this.respOpcion); 
	}

	public String getFecha() {
		return (this.fecha); 
	}

	public String getNombre() {
		return (this.nombre); 
	}

	public String getEstado() {
		return (this.estado); 
	}

	public Vector getListaRegistroWC() {
		return (this.listaRegistroWC); 
	}
	public void setPaginaListado(String paginaListado) {
		this.paginaListado = paginaListado; 
	}

	public void setNumLineasPaginaListado(String numLineasPaginaListado) {
		this.numLineasPaginaListado = numLineasPaginaListado; 
	}

	public String getPaginaListado() {
		return (this.paginaListado); 
	}

	public String getNumLineasPaginaListado() {
		return (this.numLineasPaginaListado); 
	}
	public void setEjercicio(String ejercicio) {
		this.ejercicio = ejercicio; 
	}

	public void setNumero(String numero) {
		this.numero = numero; 
	}

	public String getEjercicio() {
		return (this.ejercicio); 
	}

	public String getNumero() {
		return (this.numero); 
	}

	
	public void setXML(String XML) {
		this.XML = XML; 
	}

	public String getXML() {
		return (this.XML); 
	}
	


    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    private String respOpcion;
    private String fecha;
    private String nombre;
    private String estado;
    private String ejercicio;
    private String numero;
    private String XML;
    
    private String paginaListado;
    private String numLineasPaginaListado;
    
    private Vector listaRegistroWC;
}
