package es.altia.agora.interfaces.user.web.sge;


import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;


/** Clase utilizada para capturar o mostrar los adjuntos de comunicaciones */
public class DescargaAdjuntoComunicacionForm extends ActionForm {

    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(DescargaAdjuntoComunicacionForm.class.getName());
    
    private String idAdjunto;
    private String idComunicacion;
    private String firma;
    private String opcion;

    public String getIdAdjunto() {
        return idAdjunto;
    }

    public void setIdAdjunto(String idAdjunto) {
        this.idAdjunto = idAdjunto;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getIdComunicacion() {
        return idComunicacion;
    }

    public void setIdComunicacion(String idComunicacion) {
        this.idComunicacion = idComunicacion;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }
      
    
    
}