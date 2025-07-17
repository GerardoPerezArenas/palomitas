package es.altia.agora.business.registro;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;
import java.util.Date;
import es.altia.agora.technical.Fecha;

public class RegistroEntradaSalidaValueObject implements Serializable, ValueObject {

    /** Construye un nuevo registroEntradaSalida por defecto. */
    public RegistroEntradaSalidaValueObject() {
        super();
    }

	/** Construye un nuevo registroEntradaSalida por defecto. */
    public RegistroEntradaSalidaValueObject(String codDep, String codUni) {
        super();
        this.dep_cod=codDep;
        this.uni_cod=codUni;
    }

    public Date getReg_UAE(){
        return reg_UAE;
    }

	public Date getReg_UAS(){
        return reg_UAS;
    }

	public Date getReg_UCE(){
        return reg_UCE;
    }

	public Date getReg_UCS(){
        return reg_UCS;
    }

	public String getDep_cod(){
        return dep_cod;
    }

    public String getUni_cod(){
        return uni_cod;
    }

	// ------------------------------------------
	// Dias.
	public String getDia_reg_UAE() {
		if (reg_UAE != null) {
			Fecha f = new Fecha();
			String ff = f.obtenerString(reg_UAE);
			return ff.substring(0,2);
		}
		else return "";
	}

	public String getDia_reg_UAS() {
		if (reg_UAS != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UAS);
		return ff.substring(0,2);
		}
		else return "";

	}

	public String getDia_reg_UCE() {
		if (reg_UCE != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UCE);
		return ff.substring(0,2);
		}
		else return "";
	}

	public String getDia_reg_UCS() {
		if (reg_UCS != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UCS);
		return ff.substring(0,2);
		}
		else return "";
	}

	// Mes.
	public String getMes_reg_UAE() {
		if (reg_UAE != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UAE);
		return ff.substring(3,5);
		}
		else return "";
	}

	public String getMes_reg_UAS() {
		if (reg_UAS != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UAS);
		return ff.substring(3,5);
		}
		else return "";
	}

	public String getMes_reg_UCE() {
		if (reg_UCE != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UCE);
		return ff.substring(3,5);
		}
		else return "";
	}

	public String getMes_reg_UCS() {
		if (reg_UCS != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UCS);
		return ff.substring(3,5);
		}
		else return "";
	}
	// Años.
	public String getAno_reg_UAE() {
		if (reg_UAE != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UAE);
		return ff.substring(6,10);
		}
		else return "";
	}

	public String getAno_reg_UAS() {
		if (reg_UAS != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UAS);
		return ff.substring(6,10);
		}
		else return "";
	}

	public String getAno_reg_UCE() {
		if (reg_UCE != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UCE);
		return ff.substring(6,10);
		}
		else return "";
	}

	public String getAno_reg_UCS() {
		if (reg_UCS != null) {
		Fecha f = new Fecha();
		String ff = f.obtenerString(reg_UCS);
		return ff.substring(6,10);
		}
		else return "";
	}



   public void setReg_UAE(Date data){
    	this.reg_UAE=data;
    }

   public void setReg_UAE(String data){
		Fecha f = new Fecha();
    	this.reg_UAE=f.obtenerDate(data);
    }


    public void setReg_UAS(String data){
		Fecha f = new Fecha();
    	this.reg_UAS=f.obtenerDate(data);
    }

    public void setReg_UAS(Date data){
    	this.reg_UAS=data;
    }


    public void setReg_UCE(String data){
		Fecha f = new Fecha();
    	this.reg_UCE=f.obtenerDate(data);
    }

    public void setReg_UCE(Date data){
    	this.reg_UCE=data;
    }

    public void setReg_UCS(String data){

		Fecha f = new Fecha();
	    this.reg_UCS=f.obtenerDate(data);
    }

    public void setReg_UCS(Date data){
	    this.reg_UCS=data;
    }


    public void setDep_cod(String param){
        this.dep_cod=param;
    }

    public void setUni_cod(String param){
        this.uni_cod=param;
    }








	public String getOpcion() {
		return this.opcion;
	}

	public void setOpcion(String param) {
		this.opcion=param;
	}

	public Date getFec_apertura_cierre() {
			return this.fec_apertura_cierre;
	}

	public void setFec_apertura_cierre(String fecha) {
			Fecha f = new Fecha();
			this.fec_apertura_cierre = f.obtenerDate(fecha);
	}

	public String getDia_fec_apertura_cierre() {
			if (fec_apertura_cierre != null) {
					Fecha f = new Fecha();
					String ff = f.obtenerString(fec_apertura_cierre);
					return ff.substring(0,2);
			} else return "";
	}

	public String getMes_fec_apertura_cierre() {
			if (fec_apertura_cierre != null) {
					Fecha f = new Fecha();
					String ff = f.obtenerString(fec_apertura_cierre);
					return ff.substring(3,5);
			} else return "";
	}

	public String getAno_fec_apertura_cierre() {
			if (fec_apertura_cierre != null) {
					Fecha f = new Fecha();
					String ff = f.obtenerString(fec_apertura_cierre);
					return ff.substring(6,10);
			} else return "";
	}


	public int getTotalReservas() {
			return totalReservas;
	}

	public void setTotalReservas(int numReservas) {
		totalReservas = numReservas;
	}

    public void copy(RegistroEntradaSalidaValueObject other) {
        this.reg_UAE=other.reg_UAE;
        this.reg_UAS=other.reg_UAS;
        this.reg_UCE=other.reg_UCE;
        this.reg_UCS=other.reg_UCS;
        this.uni_cod=other.uni_cod;
        this.dep_cod=other.dep_cod;
        this.totalReservas = other.totalReservas;
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

    private String dep_cod;
    private String uni_cod;
    private Date reg_UAE;
    private Date reg_UAS;
    private Date reg_UCE;
    private Date reg_UCS;

    private String opcion; // abrir_entrada, abrir_salida, cerrar_entrada, cerrar_salida
    private Date fec_apertura_cierre; // Fecha para abrir o cerrar según opción.
	private int totalReservas=0;

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
}
