package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

import es.altia.agora.technical.ConstantesDatos;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ParametrosForm extends ActionForm {

    public static final long serialVersionUID=0;
    
    private String obsObligatoriasRechazar;

    public String getObsObligatoriasRechazar() {
        return obsObligatoriasRechazar;
    }

    public void setObsObligatoriasRechazar(String obsObligatoriasRechazar) {
        this.obsObligatoriasRechazar = obsObligatoriasRechazar;
    }
    
    /**
     * Construye un HashMap con todos los parametros indexados por sus claves.
     * @return hashmap con todos los parametros con sus respectivas claves
     */
    public HashMap<String,String> getParametros() {
        
        HashMap<String,String> params = new HashMap<String,String>();
        params.put(ConstantesDatos.PARAMS_OBS_RECHAZAR_CLAVE, obsObligatoriasRechazar);
        
        return params;
    }
    
    /**
     * Puebla los atributos de esta clase (que son los parametros configurables)
     * con los parametros que se pasan en el hashmap.
     * @param params
     */
    public void setParametros (HashMap<String, String> params) {
        
        this.obsObligatoriasRechazar = params.get(ConstantesDatos.PARAMS_OBS_RECHAZAR_CLAVE);
    }
            
    // Necesario para que Struts reconozca cuando un checkbox no esta seleccionado.
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        //setMiCheckBox(false);
    }
}
