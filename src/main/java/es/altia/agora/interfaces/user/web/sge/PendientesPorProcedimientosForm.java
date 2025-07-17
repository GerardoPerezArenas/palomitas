package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.PendientesPorProcedimientosValueObject;
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
public class PendientesPorProcedimientosForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log = LogFactory.getLog(PendientesPorProcedimientosForm.class.getName());

    //Reutilizamos
    PendientesPorProcedimientosValueObject pendProcVO = new PendientesPorProcedimientosValueObject();

    public PendientesPorProcedimientosValueObject getPendientesPorProcedimientos() {
        return pendProcVO;
    }

    public void setPendientesPorProcedimientos(PendientesPorProcedimientosValueObject pendProcVO) {
        this.pendProcVO = pendProcVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

public String getCodProcedimiento() {
  return pendProcVO.getCodProcedimiento();
}
public void setCodProcedimiento(String codProcedimiento) {
  pendProcVO.setCodProcedimiento(codProcedimiento);
}
public String getCodMunicipio() {
  return pendProcVO.getCodMunicipio();
}
public void setCodMunicipio(String codMunicipio) {
  pendProcVO.setCodMunicipio(codMunicipio);
}
public String getDescProcedimiento() {
  return pendProcVO.getDescProcedimiento();
}
public void setDescProcedimiento(String descProcedimiento) {
  pendProcVO.setDescProcedimiento(descProcedimiento);
}
public String getTxtRegistros() {
  return pendProcVO.getTxtRegistros();
}
public void setTxtRegistros(String txtRegistros) {
  pendProcVO.setTxtRegistros(txtRegistros);
}
public String getAno() {
  return pendProcVO.getAno();
}
public void setAno(String ano) {
  pendProcVO.setAno(ano);
}
public String getEstado() {
  return pendProcVO.getEstado();
}
public void setEstado(String estado) {
  pendProcVO.setEstado(estado);
}
public String getFechaFin() {
  return pendProcVO.getFechaFin();
}
public void setFechaFin(String fechaFin) {
  pendProcVO.setFechaFin(fechaFin);
}
public String getFechaInicio() {
  return pendProcVO.getFechaInicio();
}
public void setFechaInicio(String fechaInicio) {
  pendProcVO.setFechaInicio(fechaInicio);
}
public String getNumero() {
  return pendProcVO.getNumero();
}
public void setNumero(String numero) {
  pendProcVO.setNumero(numero);
}
public String getTercero() {
  return pendProcVO.getTercero();
}
public void setTercero(String tercero) {
  pendProcVO.setTercero(tercero);
}
public String getVersion() {
  return pendProcVO.getVersion();
}
public void setVersion(String version) {
  pendProcVO.setVersion(version);
}
public String getNombreCompleto() {
  return pendProcVO.getNombreCompleto();
}
public void setNombreCompleto(String nombreCompleto) {
  pendProcVO.setNombreCompleto(nombreCompleto);
}
public String getEntrada() {
  return pendProcVO.getEntrada();
}
public void setEntrada(String entrada) {
  pendProcVO.setEntrada(entrada);
}

public Vector getListaProcedimientos() {
  return pendProcVO.getListaProcedimientos();
}
public Vector getConsulta() {
  return pendProcVO.getConsulta();
}

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            pendProcVO.validate(idioma);
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


}
