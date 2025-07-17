package es.altia.agora.business.integracionsw;

import java.io.Serializable;

public abstract class TipoServicioWebVO implements Serializable {

	public static final int TIPO_BASE = 0;
	public static final int TIPO_COMPLEJO = 1;
	public static final int TIPO_ARRAY = 2;

    protected String nombreTipo;
    protected int complejidad;

    public TipoServicioWebVO(String nombreTipo,int complejidad) {
        this.nombreTipo = nombreTipo;
        this.complejidad = complejidad;
    }

    public TipoServicioWebVO(int complejidad) {
        this.complejidad = complejidad;
    }

    public TipoServicioWebVO(){
    	super();
    }

    public boolean esTipoBase() {
    	return (this.complejidad == TIPO_BASE);
    }

    public boolean esTipoComplejo() {
    	return (this.complejidad == TIPO_COMPLEJO);
    }

    public boolean esTipoArray() {
    	return (this.complejidad == TIPO_ARRAY);
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public int getComplejidad() {
		return complejidad;
	}

	public void setComplejidad(int complejidad) {
		this.complejidad = complejidad;
	}

    public abstract TipoServicioWebVO copy();
}
