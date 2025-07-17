package es.altia.agora.interfaces.user.web.sge;

import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.EstructuraCampoModuloIntegracionVO;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;

import java.util.ArrayList;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;


/** Clase utilizada para capturar o mostrar el estado de un RegistroEntrada */
public class ConsultaExpedientePorCampoSupForm extends ConsultaExpedientesForm {
   //Queremos usar el fichero de configuraci�n technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(ConsultaExpedientePorCampoSupForm.class.getName());

    protected Vector estructuraDatosSuplementarios;
    protected Vector estructuraDatosSuplementariosTramites;
    //Esto pq est� en el an�lisis
    protected ArrayList<ModuloIntegracionExterno> estructuraModulosExtensionCamposConsulta;
    //Esto partiendo de la base que un procedimiento, s�lo asociado a un m�dulo
    protected ArrayList<EstructuraCampoModuloIntegracionVO> camposConsulta;


    /**
     * Metodo que obtiene los campos suplementarios que han sido cubiertos en Consulta Expedientes por campos suplementarios con un valor
     * para poder obtenerlos en la consulta y ejecutarla
      */
public HashMap getCamposSuplementarios(){
    return consExpVO.getCamposSuplementarios();
}
public void setCamposSuplementarios(HashMap hm) {
    consExpVO.setCamposSuplementarios(hm);
}

public HashMap getCamposSuplementariosTramites(){
    return consExpVO.getCamposSuplementarios();
}
public void setCamposSuplementariosTramites(HashMap hm) {
    consExpVO.setCamposSuplementarios(hm);
}
    public Vector getEstructuraDatosSuplementarios(){ return estructuraDatosSuplementarios; }
    public void setEstructuraDatosSuplementarios(Vector estructuraDatosSuplementarios){ this.estructuraDatosSuplementarios=estructuraDatosSuplementarios; }

    public Vector getEstructuraDatosSuplementariosTramites(){ return estructuraDatosSuplementariosTramites; }
    public void setEstructuraDatosSuplementariosTramites(Vector estructuraDatosSuplementarios){ this.estructuraDatosSuplementariosTramites=estructuraDatosSuplementarios; }

    public ArrayList<ModuloIntegracionExterno> getEstructuraModulosExtensionCamposConsulta() {
        return estructuraModulosExtensionCamposConsulta;
    }

    public void setEstructuraModulosExtensionCamposConsulta(ArrayList<ModuloIntegracionExterno> estructuraModulosExtensionCamposConsulta) {
        this.estructuraModulosExtensionCamposConsulta = estructuraModulosExtensionCamposConsulta;
    }

    public ArrayList<EstructuraCampoModuloIntegracionVO> getCamposConsulta() {
        return camposConsulta;
    }

    public void setCamposConsulta(ArrayList<EstructuraCampoModuloIntegracionVO> camposConsulta) {
        this.camposConsulta = camposConsulta;
    }



    /* Funci�n que procesa los errores de validaci�n a formato struts */
    private ActionErrors validationException(ValidationException ve,ActionErrors errors){
      Iterator iter = ve.getMessages().get();
      while (iter.hasNext()) {
        Message message = (Message)iter.next();
        errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
      }
      return errors;
    }


}
