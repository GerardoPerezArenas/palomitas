package es.altia.agora.business.escritorio;

import java.io.Serializable;

import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;
import java.util.List; 

public class UsuarioValueObject implements Serializable, ValueObject {

	private int idUsuario;
	private int idioma;
    private boolean multipleIdioma;
    private String nombreUsu;
	private int appCod;
	private String app;
	private String appIco;
	private int orgCod;
	private String css;
	private String org;
	private int orgCodINE;
	private String orgIco;
	private int entCod;
	private String ent;
	private int unidadOrgCod;
	private String unidadOrg;
	private int depCod;
	private String dep;
    private String dtr;
    private String existeTramero;
	private String[] paramsCon;
	private boolean mantenerEntrada;
	private boolean mantenerSalida;
	private String soloConsultarExp;
	private String soloConsultarPad;
	private String caminoContexto;
	private List<String> listaIdsCargo;
	private int procedimCod;
	private boolean cambioIdioma;

    public UsuarioValueObject() {
        super();
    }

	public UsuarioValueObject(UsuarioEscritorioValueObject usuarioEscritorio) {
		idUsuario = usuarioEscritorio.getIdUsuario();
		nombreUsu = usuarioEscritorio.getNombreUsu();
		idioma = usuarioEscritorio.getIdiomaEsc();
                css=usuarioEscritorio.getCss();

        setDefault();
	}

	public void setDefault() {
		setAppCod(-1);
		setApp("");
		setOrgCod(-1);
		setOrg("");
		setOrgCodINE(-1);
		setEntCod(-1);
		setOrgIco("");
		setEnt("");
		setUnidadOrgCod(-1);
		setUnidadOrg("");
		setDepCod(-1);
		setDep("");
        setDtr("");
        setExisteTramero("");
		setParamsCon(null);
		setMantenerEntrada(true);
		setMantenerSalida(true);
		setSoloConsultarExp("no");
        setMultipleIdioma(false);
    }

	/**
	 * Identificador del usuario activo.
	 */
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

	/**
	 * Codigo de idioma del usuario activo.
	 */
    public int getIdioma() { return idioma; }
    public void setIdioma(int idioma) { this.idioma = idioma; }

    public boolean isMultipleIdioma() { return multipleIdioma; }
    public void setMultipleIdioma(boolean idiomaMultiple) { this.multipleIdioma = idiomaMultiple;}

	/**
	 * Nombre del usuario activo.
	 */
    public String getNombreUsu() { return nombreUsu; }
    public void setNombreUsu(String nombreUsu) { this.nombreUsu = nombreUsu; }

	/**
	 * Codigo de la aplicacion que esta usando el usuario.
	 */
	public int getAppCod() { return appCod; }
    public void setAppCod(int appCod) { this.appCod = appCod; }

	public String getApp() { return app; }
    public void setApp(String app) { this.app = app; }

    public String getAppIco() {
        return appIco;
    }

    public void setAppIco(String appIco) {
        this.appIco = appIco;
    }

	public int getOrgCod() { return orgCod; }
    public void setOrgCod(int orgCod) { this.orgCod = orgCod; }

    public String getCss() { return css; }
    public void setCss(String css) { this.css = css; }

	public String getOrgIco() { return orgIco; }
    public void setOrgIco(String orgIco) { this.orgIco = orgIco; }

	/** 
	 * Organizacion o esquema de base de datos utilizado por el usuario activo.
	 */
	public String getOrg() { return org; }
    public void setOrg(String org) { this.org = org; }

	public int getEntCod() { return entCod; }
    public void setEntCod(int entCod) { this.entCod = entCod; }

	public String getEnt() { return ent; }
    public void setEnt(String ent) { this.ent = ent; }

	/**
	 * Codigo de la unidad organica del usuario del usuario activo.
	 */
	public int getUnidadOrgCod() { return unidadOrgCod; }
    public void setUnidadOrgCod(int unidadOrgCod) { this.unidadOrgCod = unidadOrgCod; }

	public String getUnidadOrg() { return unidadOrg; }
    public void setUnidadOrg(String unidadOrg) { this.unidadOrg = unidadOrg; }

	/**
	 * Codigo del departamento del usuario activo. 
	 */
	public int getDepCod() { return depCod; }
    public void setDepCod(int depCod) { this.depCod = depCod; }

	/**
	 * Nombre del departamento del usuario activo.
	 */
	public String getDep() { return dep; }
    public void setDep(String dep) { this.dep = dep; }

	public String getDtr() { return dtr; }
    public void setDtr(String dtr) { this.dtr = dtr; }

    public String getExisteTramero() { return existeTramero; }
    public void setExisteTramero(String existeTramero) { this.existeTramero = existeTramero; }

	/**
	 * Parametros de conexion a la base de datos del usuario activo.
	 */
	public String[] getParamsCon() { return paramsCon; }
    public void setParamsCon(String[] paramsCon) { this.paramsCon = paramsCon; }

	public boolean getMantenerEntrada() { return mantenerEntrada; }
    public void setMantenerEntrada(boolean mantenerEntrada) { this.mantenerEntrada = mantenerEntrada; }

	public boolean getMantenerSalida() { return mantenerSalida; }
    public void setMantenerSalida(boolean mantenerSalida) { this.mantenerSalida = mantenerSalida; }

    public void setOrgCodINE(int orgCodINE) { this.orgCodINE = orgCodINE; }
	public int getOrgCodINE() { return (this.orgCodINE); }

	public String getSoloConsultarExp() { return soloConsultarExp; }
    public void setSoloConsultarExp(String soloConsultarExp) { this.soloConsultarExp = soloConsultarExp; }

  	public String getSoloConsultarPad() { return soloConsultarPad; }
    public void setSoloConsultarPad(String soloConsultarPad) { this.soloConsultarPad = soloConsultarPad; }

	public String getCaminoContexto() { return caminoContexto; }
	public void setCaminoContexto(String s) { this.caminoContexto= s; }

    public List<String> getListaIdsCargo() {
        return listaIdsCargo;
    }

    public void setListaIdsCargo(List<String> listaIdsCargo) {
        this.listaIdsCargo = listaIdsCargo;
    }

  
    public int getProcedimCod() { return procedimCod;}

    public void setProcedimCod(int procedimCod) { this.procedimCod = procedimCod;}

    public boolean isCambioIdioma() {
        return cambioIdioma;
    }

    public void setCambioIdioma(boolean cambioIdioma) {
        this.cambioIdioma = cambioIdioma;
    }
   
    

	public UsuarioValueObject(UsuarioValueObject other) {
		if(this != other) {
			this.idUsuario = other.idUsuario;
			this.idioma = other.idioma;
			this.nombreUsu = other.nombreUsu;
			this.appCod = other.appCod;
			this.app = other.app;
			this.orgCod = other.orgCod;
			this.org = other.org;
			this.orgCodINE = other.orgCodINE;
			this.orgIco = other.orgIco;
			this.entCod = other.entCod;
			this.ent = other.ent;
			this.unidadOrgCod = other.unidadOrgCod;
			this.unidadOrg = other.unidadOrg;
			this.depCod = other.depCod;
			this.dep = other.dep;
            this.dtr = other.dtr;
            this.existeTramero = other.existeTramero;
			this.paramsCon = other.paramsCon;
			this.mantenerEntrada = other.mantenerEntrada;
			this.mantenerSalida = other.mantenerSalida;
			this.soloConsultarExp = other.soloConsultarExp;
			this.soloConsultarPad = other.soloConsultarPad;
            this.multipleIdioma = other.multipleIdioma;
        }
	}

	public void validate(String idioma) throws ValidationException {}
}