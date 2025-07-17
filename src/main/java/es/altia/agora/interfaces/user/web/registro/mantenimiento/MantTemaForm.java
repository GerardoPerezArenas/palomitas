package es.altia.agora.interfaces.user.web.registro.mantenimiento;

import es.altia.agora.business.registro.mantenimiento.MantTemasValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** Clase utilizada para capturar o mostrar el estado de un RegistroEntrada */
public class MantTemaForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(MantTemaForm.class.getName());
    //Reutilizamos
    MantTemasValueObject temaVO = new MantTemasValueObject();
    private Vector codigos;
    private Vector utilizados;
    private Vector utilizadosCerrados;

/*
    //Create es el action por defecto
    private String action = "create";

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
*/
    /* Seccion donde metemos los metods get y set de los campos del formulario */


    public Vector getCodigos(){
        return codigos;
    }
    public void setCodigos(Vector param){
             this.codigos=param;
    }

    public Vector getUtilizados(){
        return utilizados;
    }
    public void setUtilizados(Vector util){
             this.utilizados=util;
    }

    public Vector getUtilizadosCerrados(){
        return utilizadosCerrados;
    }
    public void setUtilizadosCerrados(Vector util){
             this.utilizadosCerrados=util;
    }

    public String getTxtNomeDescripcion(){
    if (temaVO.getTxtNomeDescripcion()!= null)
      return temaVO.getTxtNomeDescripcion();
    else return "";
    }

    public void setTxtNomeDescripcion(String param){
      if (!"".equals(param.trim()))
      temaVO.setTxtNomeDescripcion(param);
    }

    public String getCodigo(){
    if (temaVO.getCodigo()!= null)
      return temaVO.getCodigo();
    else return "";
    }

    public void setCodigo(String param){
      if (!"".equals(param.trim()))
      temaVO.setCodigo(param);
    }
    public String getActivo(){
        if (temaVO.getActivo()!= null)
          return temaVO.getActivo();
        else return "";
        }

        public void setActivo(String param){
          if (!"".equals(param.trim()))
          temaVO.setActivo(param);
        }
   
    public String getEstado(){
        if(temaVO.getEstado()!=null){
            return temaVO.getEstado();
        }else{
            return "";
        }
    }
    
    public void setEstado(String param){
        if(!"".equals(param.trim())){
            temaVO.setEstado(param);
        }
    }
    
    public String getFecha(){
        if(temaVO.getFecha() != null){
            return temaVO.getFecha();
        }else{
            return "";
        }
    }
    
    public void setFecha(String param){
        if(!"".equals(param.trim())){
            temaVO.setFecha(param);
        }
    }

     
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            temaVO.validate(idioma);
        } catch (ValidationException ve) {
          //Hay errores...
          //Tenemos que traducirlos a formato struts
          errors=validationException(ve,errors);
        }
        return errors;
    }

    /* Función que procesa los errores de validación a formato struts */
    private ActionErrors validationException(ValidationException ve,ActionErrors errors){
      Iterator iter = ve.getMessages().get();
      while (iter.hasNext()) {
        Message message = (Message)iter.next();
        errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
      }
      return errors;
    }

    public String toString() {
        String sc = "\n"; // salto de carro

        StringBuffer resultado = new StringBuffer("");

        resultado.append("*** getCodigos: " + getCodigos() + sc);

       

        return resultado.toString();
    }

   // private String num_doc  = "";

}
