package es.altia.agora.business.registro.mantenimiento;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import java.io.Serializable;

public class MantTemasValueObject implements Serializable, ValueObject {
    /** Construye un nuevo RegistroSaida por defecto. */
    public MantTemasValueObject() {
        super(); 
    }

    public MantTemasValueObject(int id, String desc, String act) {
		ide = id;
		txtNomeDescripcion = desc;
		activo = act;
    }
    
    public MantTemasValueObject(int id, String cod, String desc, String act, String acti) {
		ide = id;
		codigo = cod;
		txtNomeDescripcion = desc;
		activo=acti;
		actuacion = act;
    }
    public MantTemasValueObject(int id, String cod, String desc, String act) {
		ide = id;
		codigo = cod;
		txtNomeDescripcion = desc;
		actuacion = act;
    }
    
     public MantTemasValueObject(int id, String cod, String desc, String act, String acti, String est, String fec){	
                ide=id;	
                codigo=cod;	
                txtNomeDescripcion=desc;	
                activo=acti;	
                actuacion=act;	
                estado= est;	
                fecha=fec;	
    }

	public int getIde(){
		return ide;
	}
	public void setIde(int id){
		ide=id;
	}

    public String getCodigo(){
        return codigo;
    }
	public void setCodigo(String param){
    	codigo=param;
    }

    public String getTxtNomeDescripcion(){
        return txtNomeDescripcion;
    }
    public void setTxtNomeDescripcion(String param){
        txtNomeDescripcion=param;
    }
    public String getActivo(){
        return activo;
    }
    public void setActivo(String param){
    	activo=param;
    }

	public String getActuacion(){
        return actuacion;
    }
    public void setActuacion(String act){
        actuacion=act;
    }


    public void copy(MantTemasValueObject other) {
        codigo=other.codigo;
        txtNomeDescripcion=other.txtNomeDescripcion;
        activo=other.activo;
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

    /** Devuelve un booleano que representa si el estado de este mantenimientoTema es válido. */
    public boolean IsValid() { return isValid; }

    public String toString() {
        String sc = "\n"; // salto de carro

        StringBuffer resultado = new StringBuffer("");

        resultado.append("txtNomeDescripcion: " + getTxtNomeDescripcion() + sc);
        resultado.append("Activo: " + getActivo() + sc);
       

        return resultado.toString();
    }

    
    
    private int ide;
    private String codigo;
    private String txtNomeDescripcion;
	private String actuacion;
	private String activo;
      	
    private String estado;	
    private String fecha;

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    
    
    
}
