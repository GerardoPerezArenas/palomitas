package es.altia.agora.interfaces.user.web.registro;

import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.util.Vector;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/** Clase para dar de alta anotaciones en el registro, tanto de entrada como de salida */

public class RegistroDesdeFicheroForm extends ActionForm {

   
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");   
    protected static Log m_Log =
            LogFactory.getLog(RegistroDesdeFicheroForm.class.getName());

    /* Seccion donde metemos los metods get y set de los campos del formulario */
	private String opcion = "";	
	private String respOpcion = "";
	private FormFile ficheroUp;	
	private String ejReservaD = "";
	private String ejReservaH = "";
	private String numReservaD = "";
	private String numReservaH = "";
	private String tipoRegistro = ""; // E o S
	private Vector registros = null;
        private Vector listaUORs = new Vector();
        private String asunto = "";
        private String directivaUsuario="";

	
	public void setOpcion(String valor) {
			opcion  = valor;
	}

	public String getOpcion() {
			  return opcion ;
	}

	public void setRespOpcion(String valor) {
			respOpcion  = valor;
	}

	public String getRespOpcion() {
			  return respOpcion ;
	}

	public FormFile getFicheroUp() {
		return ficheroUp;
	}

	public void setFicheroUp(FormFile newFicheroUp) {
		ficheroUp = newFicheroUp;
	}

	public String getEjReservaD () {
			  return ejReservaD ;
	}
	
	public void setEjReservaD (String valor) {
			ejReservaD  = valor;
	}

	public String getEjReservaH () {
			  return ejReservaH ;
	}
	
	public void setEjReservaH (String valor) {
			ejReservaH  = valor;
	}

	public String getNumReservaD () {
			  return numReservaD ;
	}
	
	public void setNumReservaD (String valor) {
			numReservaD  = valor;
	}
	
	public String getNumReservaH () {		
				  return numReservaH ;
	}
	
	public void setNumReservaH (String valor) {
				numReservaH  = valor;
	}

	public String getTipoRegistro() {		
		return tipoRegistro;
	}
	
	public void setTipoRegistro(String valor) {
		tipoRegistro  = valor;
	}

	public Vector getRegistros() {		
			return registros;
	}
	
	public void setRegistros(Vector valor) {
			registros  = valor;
	}

        public Vector getListaUORs() {
            return listaUORs;
        }

        public void setListaUORs(Vector listaUORs) {
            this.listaUORs = listaUORs;
        }

        public String getAsunto() {
            return asunto;
        }

        public void setAsunto(String asunto) {
            this.asunto = asunto;
        }

    public String getDirectivaUsuario() {
        return directivaUsuario;
    }

    public void setDirectivaUsuario(String directivaUsuario) {
        this.directivaUsuario = directivaUsuario;
    }
        
        

	}
