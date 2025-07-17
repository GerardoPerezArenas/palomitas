package es.altia.agora.business.escritorio;

import java.io.Serializable;
import java.util.Vector;
import es.altia.technical.ValidationException;
import es.altia.technical.ValueObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UsuarioEscritorioValueObject implements Serializable, ValueObject {
	
	private int idUsuario;
	private int idiomaEsc;
	private String nombreUsu;
	private String login;
	private String css;
	private String password;
	private UserPreferences preferences;
	private Vector iconos;
	private boolean tipoLogin;
	private boolean multipleIdioma;
	private Calendar fechaUltimoAcceso = Calendar.getInstance();
        //Solo con login CAS
        private int daysLeftCASPass;


    /** Construye un nuevo UsuarioEscritorio por defecto. */
    public UsuarioEscritorioValueObject() {
        super();
    }

	public UsuarioEscritorioValueObject(int idUsuario, String nombreUsu, int idiomaEsc, String login, String password, UserPreferences preferences, Vector iconos) {
		this.idUsuario = idUsuario; 
		this.nombreUsu = nombreUsu;  
		this.idiomaEsc = idiomaEsc;
		this.login = login;
		this.password = password;
		this.preferences = preferences;
		this.iconos = iconos;
        this.multipleIdioma = false;

    }

    public boolean isTipoLogin() {
        return tipoLogin;
    }

    public void setTipoLogin(boolean tipoLogin) {
        this.tipoLogin = tipoLogin;
    }

    public boolean isMultipleIdioma() { return multipleIdioma; }
    public void setMultipleIdioma(boolean idiomaMultiple) { this.multipleIdioma = idiomaMultiple;}

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

	public int getIdiomaEsc() { return idiomaEsc; }
    public void setIdiomaEsc(int idiomaEsc) { this.idiomaEsc = idiomaEsc; }

	public String getNombreUsu() { return nombreUsu; }
    public void setNombreUsu(String nombreUsu) { this.nombreUsu = nombreUsu; }

	public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getCss() { return css; }
    public void setCss(String css) { this.css = css; }

	public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
	
	public UserPreferences getPreferences() { return preferences; }
    public void setPreferences(UserPreferences preferences) { this.preferences = preferences; }
	
	public Vector getIconos() { return iconos; }
    public void setIconos(Vector iconos) { this.iconos = iconos; }

    public Calendar getFechaUltimoAcceso() { return fechaUltimoAcceso; }
    public void setFechaUltimoAcceso(Calendar fechaUltimoAcceso) { this.fechaUltimoAcceso = fechaUltimoAcceso; }

    public String getFechaUltimoAcceso(String formato) {
        String fechaFormateada = null;
        
        if (fechaUltimoAcceso != null) {
            if (formato != null ) {
                SimpleDateFormat formatter = new SimpleDateFormat(formato);
                fechaFormateada = formatter.format(fechaUltimoAcceso.getTime());
            } else {
                fechaFormateada = fechaUltimoAcceso.getTime().toString();
            }
        }
        return fechaFormateada;
    }
    
	public void copy(UsuarioEscritorioValueObject other) {
		this.idUsuario = other.idUsuario;
		this.nombreUsu = other.nombreUsu;
		this.idiomaEsc = other.idiomaEsc;
		this.login = other.login;
		this.password = other.password;		
		this.preferences = other.preferences;
		this.iconos = other.iconos;
	}

	public void validate(String idioma) throws ValidationException {

	}

    /**
     * @return the daysLeftCASPass
     */
    public int getDaysLeftCASPass() {
        return daysLeftCASPass;
    }

    /**
     * @param daysLeftCASPass the daysLeftCASPass to set
     */
    public void setDaysLeftCASPass(int daysLeftCASPass) {
        this.daysLeftCASPass = daysLeftCASPass;
    }

}
