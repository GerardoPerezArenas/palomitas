package es.altia.agora.business.administracion.mantenimiento;

import java.io.Serializable;
import java.util.Vector;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;
import java.util.ArrayList;
import org.apache.struts.upload.FormFile;

public class UsuariosGruposValueObject implements Serializable, ValueObject {

    /** Construye un nuevo registroEntradaSalida por defecto. */
    public UsuariosGruposValueObject() {
        super();
    }

    /**
     * Valida el estado de esta RegistroSaida
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
     */
    public void validate(String idioma) throws ValidationException {
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

	public void setPaginaListado(String paginaListado) {
		this.paginaListado = paginaListado; 
	}

	public void setNumLineasPaginaListado(String numLineasPaginaListado) {
		this.numLineasPaginaListado = numLineasPaginaListado; 
	}

	public void setPaginaListadoG(String paginaListadoG) {
		this.paginaListadoG = paginaListadoG; 
	}

	public void setNumLineasPaginaListadoG(String numLineasPaginaListadoG) {
		this.numLineasPaginaListadoG = numLineasPaginaListadoG; 
	}

	public boolean getIsValid() {
		return (this.isValid); 
	}

	public String getPaginaListado() {
		return (this.paginaListado); 
	}

	public String getNumLineasPaginaListado() {
		return (this.numLineasPaginaListado); 
	}

	public String getPaginaListadoG() {
		return (this.paginaListadoG); 
	}

	public String getNumLineasPaginaListadoG() {
		return (this.numLineasPaginaListadoG); 
	}

	
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario; 
	}

	public void setLogin(String login) {
		this.login = login; 
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena; 
	}
        
        
        public void setBuzonFirma (String buzonFirma) {
            this.buzonFirma = buzonFirma;
        }
        
        public String getBuzonFirma () {
            return this.buzonFirma;
        }
        

    public void setContrasena2(String contrasena2) {
        this.contrasena2 = contrasena2;
    }

	public void setCodIdioma(String codIdioma) {
		this.codIdioma = codIdioma; 
	}

	public void setDescIdioma(String descIdioma) {
		this.descIdioma = descIdioma; 
	}

	public String getNombreUsuario() {
		return (this.nombreUsuario); 
	}

	public String getLogin() {
		return (this.login); 
	}

	public String getContrasena() {
		return (this.contrasena); 
	}

    public String getContrasena2() {
        return (this.contrasena2);
    }

	public String getCodIdioma() {
		return (this.codIdioma); 
	}

	public String getDescIdioma() {
		return (this.descIdioma); 
	}

	
	public void setListaOrganizaciones(Vector listaOrganizaciones) {
		this.listaOrganizaciones = listaOrganizaciones; 
	}

	public Vector getListaOrganizaciones() {
		return (this.listaOrganizaciones); 
	}

	
	public void setListaIdiomas(Vector listaIdiomas) {
		this.listaIdiomas = listaIdiomas; 
	}

	public Vector getListaIdiomas() {
		return (this.listaIdiomas); 
	}

	
	public void setCodOrganizacion(String codOrganizacion) {
		this.codOrganizacion = codOrganizacion; 
	}

	public void setNombreOrganizacion(String nombreOrganizacion) {
		this.nombreOrganizacion = nombreOrganizacion; 
	}

	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion; 
	}

	public String getCodOrganizacion() {
		return (this.codOrganizacion); 
	}

	public String getNombreOrganizacion() {
		return (this.nombreOrganizacion); 
	}

	public String getAutorizacion() {
		return (this.autorizacion); 
	}

	
	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo; 
	}

	public void setListaUsuariosGrupos(Vector listaUsuariosGrupos) {
		this.listaUsuariosGrupos = listaUsuariosGrupos; 
	}

	public String getNombreGrupo() {
		return (this.nombreGrupo); 
	}

	public Vector getListaUsuariosGrupos() {
		return (this.listaUsuariosGrupos); 
	}

	
	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario; 
	}

	public void setCodAplicacion(String codAplicacion) {
		this.codAplicacion = codAplicacion; 
	}

	public void setNombreAplicacion(String nombreAplicacion) {
		this.nombreAplicacion = nombreAplicacion; 
	}

	public void setCodEntidad(String codEntidad) {
		this.codEntidad = codEntidad; 
	}

	public void setNombreEntidad(String nombreEntidad) {
		this.nombreEntidad = nombreEntidad; 
	}

	public String getCodUsuario() {
		return (this.codUsuario); 
	}

	public String getCodAplicacion() {
		return (this.codAplicacion); 
	}

	public String getNombreAplicacion() {
		return (this.nombreAplicacion); 
	}

	public String getCodEntidad() {
		return (this.codEntidad); 
	}

	public String getNombreEntidad() {
		return (this.nombreEntidad); 
	}

	
	public void setCodGrupo(String codGrupo) {
		this.codGrupo = codGrupo; 
	}

	public String getCodGrupo() {
		return (this.codGrupo); 
	}

	
	public void setRespOpcion(String respOpcion) {
		this.respOpcion = respOpcion; 
	}

	public String getRespOpcion() {
		return (this.respOpcion); 
	}

	
	public void setListaGrupos(Vector listaGrupos) {
		this.listaGrupos = listaGrupos; 
	}

	public Vector getListaGrupos() {
		return (this.listaGrupos); 
	}

	
	public void setListaAplicaciones(Vector listaAplicaciones) {
		this.listaAplicaciones = listaAplicaciones; 
	}

	public Vector getListaAplicaciones() {
		return (this.listaAplicaciones); 
	}

	
	public void setListaOrganizacionesGrupos(Vector listaOrganizacionesGrupos) {
		this.listaOrganizacionesGrupos = listaOrganizacionesGrupos; 
	}

	public void setListaEntidadesGrupos(Vector listaEntidadesGrupos) {
		this.listaEntidadesGrupos = listaEntidadesGrupos; 
	}

	public void setListaAplicacionesGrupos(Vector listaAplicacionesGrupos) {
		this.listaAplicacionesGrupos = listaAplicacionesGrupos; 
	}

	public Vector getListaOrganizacionesGrupos() {
		return (this.listaOrganizacionesGrupos); 
	}

	public Vector getListaEntidadesGrupos() {
		return (this.listaEntidadesGrupos); 
	}

	public Vector getListaAplicacionesGrupos() {
		return (this.listaAplicacionesGrupos); 
	}

	
	public void setCodUnidOrganica(String codUnidOrganica) {
		this.codUnidOrganica = codUnidOrganica; 
	}

	public void setNombreUnidOrganica(String nombreUnidOrganica) {
		this.nombreUnidOrganica = nombreUnidOrganica; 
	}

	public String getCodUnidOrganica() {
		return (this.codUnidOrganica); 
	}

	public String getNombreUnidOrganica() {
		return (this.nombreUnidOrganica); 
	}

    public String getCodUnidadOrganicaPadre() {
        return codUnidadOrganicaPadre;
    }

    public void setCodUnidadOrganicaPadre(String codUnidadOrganicaPadre) {
        this.codUnidadOrganicaPadre = codUnidadOrganicaPadre;
    }

        

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public String getCodCargo() {
        return (this.codCargo);
    }

    public String getNombreCargo() {
        return (this.nombreCargo);
    }

	
	public void setListaUnidOrganicas(Vector listaUnidOrganicas) {
		this.listaUnidOrganicas = listaUnidOrganicas; 
	}

	public Vector getListaUnidOrganicas() {
		return (this.listaUnidOrganicas); 
	}

	
	public void setListaUnidadesOrganicas(Vector listaUnidadesOrganicas) {
		this.listaUnidadesOrganicas = listaUnidadesOrganicas; 
	}

	public Vector getListaUnidadesOrganicas() {
		return (this.listaUnidadesOrganicas); 
	}

	
	public void setListaOrganizacionesUOR(Vector listaOrganizacionesUOR) {
		this.listaOrganizacionesUOR = listaOrganizacionesUOR; 
	}

	public void setListaEntidadesUOR(Vector listaEntidadesUOR) {
		this.listaEntidadesUOR = listaEntidadesUOR; 
	}

    public void setListaCargosUOR(Vector listaCargosUOR) {
        this.listaCargosUOR = listaCargosUOR;
    }

	public Vector getListaOrganizacionesUOR() {
		return (this.listaOrganizacionesUOR); 
	}

	public Vector getListaEntidadesUOR() {
		return (this.listaEntidadesUOR); 
	}

    public Vector getListaCargosUOR() {
        return (this.listaCargosUOR);
    }

	
	public void setListaOrg(Vector listaOrg) {
		this.listaOrg = listaOrg; 
	}

	public Vector getListaOrg() {
		return (this.listaOrg); 
	}
	
	public Vector getListaUsuariosTodos() {
		return (this.listaUsuariosTodos); 
	}

	public void setListaUsuariosTodos(Vector listaUsuariosTodos) {
		this.listaUsuariosTodos = listaUsuariosTodos; 
	}
    

    /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
    private boolean isValid;
    
    private String paginaListado;
    private String numLineasPaginaListado;
    private String paginaListadoG;
    private String numLineasPaginaListadoG;
    
    private String nombreUsuario;
    private String login;
    private String contrasena;
    private String contrasena2;
    private String codIdioma;
    private String descIdioma;
    private String fechaEliminado;
    private String buzonFirma;
    
    private String codOrganizacion;
    private String nombreOrganizacion;
    private String autorizacion;
    private Vector listaOrganizaciones;
    private Vector listaIdiomas;
    
    private String codGrupo;
    private String nombreGrupo;
    private String codUsuario;
    private String codAplicacion;
    private String nombreAplicacion;
    private String codEntidad;
    private String nombreEntidad;
    private Vector listaUsuariosGrupos;
    
    private Vector listaGrupos;
    private Vector listaAplicaciones;
    
    private Vector listaOrganizacionesGrupos;
    private Vector listaEntidadesGrupos;
    private Vector listaAplicacionesGrupos;
    
    private String codUnidOrganica;
    private String nombreUnidOrganica;
    private String codUnidadOrganicaPadre;
    private Vector listaUnidOrganicas;
    
    private String codCargo;
    private String nombreCargo;

    private Vector listaUnidadesOrganicas;
    private Vector listaOrganizacionesUOR;
    private Vector listaEntidadesUOR;
    private Vector listaCargosUOR;
    
    private Vector listaOrg;
    
    private String respOpcion;
    
    private Vector listaUsuariosTodos;

    protected String pNif = null;
	protected int pEstado = 0;
    protected int pFirmante = 0;
    
    private Vector<DirectivaVO> directivas;
    
    private String cambioPantallaObligatorio = null;

    public String getCambioPantallaObligatorio(){
        return this.cambioPantallaObligatorio;
    }
    
    public void setCambioPantallaObligatorio(String cambioPantallaObligatorio){
        this.cambioPantallaObligatorio = cambioPantallaObligatorio;
    }

    // Contiene los procedimientos de tipo restringido sobre los que un usuario tiene permiso en el caso de tener activada la directiva de usuario correspondiente
    private ArrayList<PermisoProcedimientosRestringidosVO> procedimientosRestringidos = new ArrayList<PermisoProcedimientosRestringidosVO>();

    public String getFechaEliminado() {
        return fechaEliminado;
    }

    public void setFechaEliminado(String fechaEliminado) {
        this.fechaEliminado = fechaEliminado;
    }

    public String getNif() {
        return pNif;
    }
    public void setNif(String nif) {
        pNif = nif;
        if ( (nif!=null) && (nif.trim().length()==0) )
            pNif = null;
    }

	public int getEstado() {
		return pEstado;
	}

	public void setEstado(int estado) {
		pEstado = estado;
	}

    public int getFirmante() {
        return pFirmante;
    }
    public void setFirmante(int firmante) {
        pFirmante = firmante;
    }
    /* ******************************************************* */
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    /* ******************************************************* */
    private FormFile ficheroFirma;

    public void setFicheroFirma(FormFile ficheroFirma) {
        this.ficheroFirma = ficheroFirma;
    }

    public FormFile getFicheroFirma() {
        return this.ficheroFirma;
    }
    /* ******************************************************* */
    private byte[] ficheroFirmaFisico;

    public void setFicheroFirmaFisico(byte[] ficheroFirmaFisico) {
        this.ficheroFirmaFisico = ficheroFirmaFisico;
    }

    public byte[] getFicheroFirmaFisico() {
        return this.ficheroFirmaFisico;
    }
    /* ******************************************************* */
    private String tipoFirma;

    public void setTipoFirma(String tipoFirma) {
        this.tipoFirma = tipoFirma;
    }

    public String getTipoFirma() {
        return this.tipoFirma;
    }

    public Vector<DirectivaVO> getDirectivas() {
        return directivas;
    }

    public void setDirectivas(Vector<DirectivaVO> directivas) {
        this.directivas = directivas;
    }

    /**
     * @return the procedimientosRestringidos
     */
    public ArrayList<PermisoProcedimientosRestringidosVO> getProcedimientosRestringidos() {
        return procedimientosRestringidos;
    }

    /**
     * @param procedimientosRestringidos the procedimientosRestringidos to set
     */
    public void setProcedimientosRestringidos(ArrayList<PermisoProcedimientosRestringidosVO> procedimientosRestringidos) {
        this.procedimientosRestringidos = procedimientosRestringidos;
    }
}
