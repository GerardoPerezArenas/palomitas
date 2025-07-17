package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject;
import es.altia.agora.business.sge.DefinicionCampoValueObject;
import java.util.Vector;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author david.caamano
 */
public class DefinicionCampoVistaForm extends ActionForm {
    
    //Reutilizamos
    Vector<DefinicionCampoValueObject> listaCampos = new Vector<DefinicionCampoValueObject>();
    Vector<DefinicionAgrupacionCamposValueObject> agrupacionesCampos = new Vector<DefinicionAgrupacionCamposValueObject>();
    Boolean errorCargando = false;

    public Vector<DefinicionAgrupacionCamposValueObject> getAgrupacionesCampos() {
        return agrupacionesCampos;
    }
    public void setAgrupacionesCampos(Vector<DefinicionAgrupacionCamposValueObject> agrupacionesCampos) {
        this.agrupacionesCampos = agrupacionesCampos;
    }

    public Vector<DefinicionCampoValueObject> getListaCampos() {
        return listaCampos;
    }
    public void setListaCampos(Vector<DefinicionCampoValueObject> listaCampos) {
        this.listaCampos = listaCampos;
    }

    public Boolean getErrorCargando() {
        return errorCargando;
    }
    public void setErrorCargando(Boolean errorCargando) {
        this.errorCargando = errorCargando;
    }
    
}//class
