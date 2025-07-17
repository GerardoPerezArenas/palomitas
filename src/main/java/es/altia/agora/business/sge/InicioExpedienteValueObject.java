package es.altia.agora.business.sge;

import java.io.Serializable;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;

public class InicioExpedienteValueObject implements Serializable, ValueObject {
	
	/** Variable booleana que indica si el estado de la instancia de BuzonEntrada es válido o no */
    private boolean isValid;

	private String registro;
    private String descProcedimiento;    	
	private String tercero;
    private String version;	
	private String fechaInicio;

	private String codMunicipio;
	private String codProcedimiento;
	private String ejercicio;
    private String numero;

	private String departamentoRes;
	private String unidadOrgRes;
	private String ejercicioRes;
    private String numeroRes;
	private String tipoRes;

	private String codTipoDocumento;
    private String desTipoDocumento;
	private String documento;
    private String nombre;
    private String telefono;	
	private String email;

    private String provincia;
	private String municipio;
    private String domicilio;
    private String poblacion;	
	private String codigoPostal;	
	
	private String estado;
	private String iniciado;
	
    /** Construye un nuevo RegistroSaida por defecto. */
    public InicioExpedienteValueObject() {
        super();
    }

	public InicioExpedienteValueObject(String cod, String nom) {
		registro = "";
		descProcedimiento = nom;
		tercero = "";
		version = "";		
		fechaInicio = "";

		codMunicipio = "";
		codProcedimiento = cod;		
		ejercicio = "";
		numero = "";

		departamentoRes = "";
		unidadOrgRes = "";
		ejercicioRes = "";
		numeroRes = "";
		tipoRes = "";

		codTipoDocumento = "";
		desTipoDocumento = "";
		documento = "";
		nombre = "";
		telefono = "";	
		email = "";

		provincia = "";
		municipio = "";
		domicilio = "";
		poblacion = "";	
		codigoPostal = "";

		estado = "";
		iniciado = "";
    }
    		

	public String getRegistro(){ return registro; }
	public void setRegistro(String reg){ registro=reg; }

	public String getCodProcedimiento(){ return codProcedimiento; }
	public void setCodProcedimiento(String cod){ codProcedimiento=cod; }

	public String getTercero(){ return tercero; }
	public void setTercero(String ter){ tercero=ter; }

	public String getVersion(){ return version; }
	public void setVersion(String ver){ version=ver; }

	public String getFechaInicio(){ return fechaInicio; }
	public void setFechaInicio(String ini){ fechaInicio=ini; }


	public String getCodMunicipio(){ return codMunicipio; }
	public void setCodMunicipio(String cmu){ codMunicipio=cmu; }

	public String getDescProcedimiento(){ return descProcedimiento; }
	public void setDescProcedimiento(String desc){ descProcedimiento=desc; }	

	public String getEjercicio(){ return ejercicio;	}
	public void setEjercicio(String eje){ ejercicio=eje; }	   

	public String getNumero(){ return numero; }
	public void setNumero(String num){ numero=num; }
	 
		
	public String getDepartamentoRes(){ return departamentoRes; }
	public void setDepartamentoRes(String dep){ departamentoRes=dep; }

	public String getUnidadOrgRes(){ return unidadOrgRes; }
	public void setUnidadOrgRes(String uor){ unidadOrgRes=uor; }	

	public String getEjercicioRes(){ return ejercicioRes;	}
	public void setEjercicioRes(String eje){ ejercicioRes=eje; }	   

	public String getNumeroRes(){ return numeroRes; }
	public void setNumeroRes(String num){ numeroRes=num; }

	public String getTipoRes(){ return tipoRes; }
	public void setTipoRes(String tip){ tipoRes=tip; }
	

	public String getCodTipoDocumento() { return codTipoDocumento;}
	public void setCodTipoDocumento(String cTDoc) { codTipoDocumento = cTDoc; }

	public String getDesTipoDocumento() { return desTipoDocumento;}
	public void setDesTipoDocumento(String dTDoc) { desTipoDocumento = dTDoc; }
	
	public String getDocumento() { return documento;}
	public void setDocumento(String doc) { documento = doc; }

	public String getNombre() { return nombre;}
	public void setNombre(String nom) { nombre = nom; }

	public String getTelefono() { return telefono;}
	public void setTelefono(String tel) { telefono = tel; }

	public String getEmail() { return email; }
	public void setEmail(String em) { email = em; }		
	
	
	public String getProvincia() { return provincia; }
	public void setProvincia(String pro) { provincia = pro; }
	
	public String getMunicipio() { return municipio; }
	public void setMunicipio(String mun) { municipio = mun; }
	
	public String getDomicilio() { return domicilio; }
	public void setDomicilio(String dom) { domicilio = dom; }

	public String getPoblacion() { return poblacion; }
	public void setPoblacion(String pob) { poblacion = pob; }
	
	public String getCodigoPostal() { return codigoPostal; }
	public void setCodigoPostal(String cP) { codigoPostal = cP; }	


	public String getEstado() { return estado; }
	public void setEstado(String est) { estado = est; }	

	public String getIniciado() { return iniciado; }
	public void setIniciado(String ini) { iniciado = ini; }	


    /**
    * Valida el estado de esta InicioExpediente
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
	
    /** Devuelve un booleano que representa si el estado de este BuzonEntrada es válido. */
    public boolean IsValid() { return isValid; }    
}

