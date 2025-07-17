package es.altia.agora.business.sge;

import java.io.Serializable;

import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;

public class BuzonEntradaSGEValueObject implements Serializable, ValueObject {
	
	private String registro;
	private String nomProcedimiento;

	private String codMunicipio;
	private String codProcedimiento;
	private String numero;
	private String ejercicio;	

	private String departamentoRes;
	private String unidadOrgRes;
    private String ejercicioRes;
	private String numeroRes;
	private String tipoRes;
	private String fechaInicio;   

	private String tercero;
    private String version;
	private String domicilio;
	private String solicitante;
	
	/** Variable booleana que indica si el estado de la instancia de BuzonEntrada es válido o no */
    private boolean isValid;

    /** Construye un nuevo RegistroSaida por defecto. */
    public BuzonEntradaSGEValueObject() {
        super();
    }

    public BuzonEntradaSGEValueObject(String reg, String nom, String cmu, String cod, String eje, String num, String der, String uor, String ejr, String nur, String tip, String ini, String ter, String ver, String dom, String sol) {
		registro = reg;
		nomProcedimiento = nom;
		
		codMunicipio = cmu;
		codProcedimiento = cod;
		ejercicio = eje;
		numero = num;

		departamentoRes = der;
		unidadOrgRes = uor;
		ejercicioRes = ejr;
		numeroRes = nur;
		tipoRes = tip;
		fechaInicio = ini;
		
		tercero = ter;
		version = ver;		
		domicilio = dom;		
		solicitante = sol;
    }
	
	
	public String getRegistro(){ return registro; }
	public void setRegistro(String reg){ registro=reg; }

	public String getNomProcedimiento(){ return nomProcedimiento; }
	public void setNomProcedimiento(String nom){ nomProcedimiento=nom; }

	
	public String getCodMunicipio(){ return codMunicipio; }
	public void setCodMunicipio(String cmu){ codMunicipio=cmu; }
	
	public String getCodProcedimiento(){ return codProcedimiento; }
	public void setCodProcedimiento(String cod){ codProcedimiento=cod; }
			
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

	public String getFechaInicio(){ return fechaInicio; }
	public void setFechaInicio(String ini){ fechaInicio=ini; }


	public String getTercero(){ return tercero; }
	public void setTercero(String ter){ tercero=ter; }	
		
	public String getVersion(){ return version; }
	public void setVersion(String ver){ version=ver; }
	
	public String getIdDomicilio(){ return domicilio; }
	public void setIdDomicilio(String dom){ domicilio=dom; }

	public String getSolicitante(){ return solicitante; }
	public void setSolicitante(String sol){ solicitante=sol; }

    /**
     * Valida el estado de esta BuzonEntrada
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
