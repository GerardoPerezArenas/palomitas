package es.altia.agora.business.integracionsw;

import java.io.Serializable;
import java.util.Vector;

public class TipoArrayVO extends TipoServicioWebVO implements Serializable {

	private Vector<TipoServicioWebVO> array = null;
    private TipoServicioWebVO tipoContenido = null;


    public TipoArrayVO(String nombreTipo, TipoServicioWebVO tipoContenido) {
		super(nombreTipo,TIPO_ARRAY);
		this.array = new Vector<TipoServicioWebVO>();
        this.tipoContenido = tipoContenido;
    }

	public TipoArrayVO() {
		super(TIPO_ARRAY);
	}

    public TipoArrayVO copy() {
        TipoArrayVO tipoArray = new TipoArrayVO(this.nombreTipo, tipoContenido.copy());
        tipoArray.setArray(this.getArray());
        return tipoArray;
    }

    public TipoArrayVO(String nombreTipo) {
		super(nombreTipo,TIPO_ARRAY);
		this.array = new Vector<TipoServicioWebVO>();
	}

	public Vector<TipoServicioWebVO> getArray() {
		return array;
	}

	public void setArray(Vector<TipoServicioWebVO> array) {
		this.array = array;
	}

    public TipoServicioWebVO getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(TipoServicioWebVO tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public String toString() {
        return "Tipo Array --> nombreTipo: " + this.nombreTipo + " | Contenido Array: " + this.array + " | Tipo Contenido: " + tipoContenido;
    }
}
