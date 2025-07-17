package es.altia.agora.business.administracion.mantenimiento;

import java.io.Serializable;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;

/**
 *
 * @author juan.jato
 */

public class DirectivaVO implements Serializable, ValueObject {

    private boolean isValid;

    private String codigo;  // Codigo de la directiva
    private String aplCod;
    private String aplDesc;
    private String mensaje;

     /**
      * El usuario tiene permiso para la directiva que indica el atributo
      * codigo si este atributo es distinto de null o "".
      */
    private String permiso;
    
    public DirectivaVO() {
        super();
    }

    public void validate(String idioma) throws ValidationException {
        Messages errors = new Messages();

        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    public boolean IsValid() { return isValid; }

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}

    public String getAplCod() {
        return aplCod;
    }

    public void setAplCod(String aplCod) {
        this.aplCod = aplCod;
    }

    public String getAplDesc() {
        return aplDesc;
    }

    public void setAplDesc(String aplDesc) {
        this.aplDesc = aplDesc;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public String toString() {
        return codigo + " - " + aplCod + " - " + aplDesc + " - " + mensaje + " - " + permiso;
    }

}
