package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.PendientesPorSolicitanteValueObject;
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
public class PendientesPorSolicitanteForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(PendientesPorSolicitanteForm.class.getName());

    //Reutilizamos
    PendientesPorSolicitanteValueObject pendSolVO = new PendientesPorSolicitanteValueObject();

    public PendientesPorSolicitanteValueObject getPendientesPorSolicitante() {
        return pendSolVO;
    }

    public void setPendientesPorSolicitante(PendientesPorSolicitanteValueObject pendSolVO) {
        this.pendSolVO = pendSolVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

public String getTxtNombre() {
  return pendSolVO.getTxtNombre();
}
public void setTxtNombre(String txtNombre) {
  pendSolVO.setTxtNombre(txtNombre);
}
public String getTxtDNI() {
  return pendSolVO.getTxtDNI();
}
public void setTxtDNI(String txtDNI) {
  pendSolVO.setTxtDNI(txtDNI);
}
public String getDescTipoDoc() {
  return pendSolVO.getDescTipoDoc();
}
public void setDescTipoDoc(String descTipoDoc) {
  pendSolVO.setDescTipoDoc(descTipoDoc);
}
public String getCodTipoDoc() {
  return pendSolVO.getCodTipoDoc();
}
public void setCodTipoDoc(String codTipoDoc) {
  pendSolVO.setCodTipoDoc(codTipoDoc);
}
public String getTxtRegistros() {
  return pendSolVO.getTxtRegistros();
}
public void setTxtRegistros(String txtRegistros) {
  pendSolVO.setTxtRegistros(txtRegistros);
}
public String getVersion() {
  return pendSolVO.getVersion();
}
public void setVersion(String version) {
  pendSolVO.setVersion(version);
}
public String getIdentificadorTerc() {
  return pendSolVO.getIdentificadorTerc();
}
public void setIdentificadorTerc(String identificadorTerc) {
  pendSolVO.setIdentificadorTerc(identificadorTerc);
}
public String getAno() {
  return pendSolVO.getAno();
}
public void setAno(String ano) {
  pendSolVO.setAno(ano);
}
public String getCodDepartamento() {
  return pendSolVO.getCodDepartamento();
}
public void setCodDepartamento(String codDepartamento) {
  pendSolVO.setCodDepartamento(codDepartamento);
}
public String getCodProcedimiento() {
  return pendSolVO.getCodProcedimiento();
}
public void setCodProcedimiento(String codProcedimiento) {
  pendSolVO.setCodProcedimiento(codProcedimiento);
}
public String getEstado() {
  return pendSolVO.getEstado();
}
public void setEstado(String estado) {
  pendSolVO.setEstado(estado);
}
public String getFechaFin() {
  return pendSolVO.getFechaFin();
}
public void setFechaFin(String fechaFin) {
  pendSolVO.setFechaFin(fechaFin);
}
public String getFechaInicio() {
  return pendSolVO.getFechaInicio();
}
public void setFechaInicio(String fechaInicio) {
  pendSolVO.setFechaInicio(fechaInicio);
}
public String getNumero() {
  return pendSolVO.getNumero();
}
public void setNumero(String numero) {
  pendSolVO.setNumero(numero);
}
public String getEntrada() {
  return pendSolVO.getEntrada();
}
public void setEntrada(String entrada) {
  pendSolVO.setEntrada(entrada);
}


public Vector getListaTiposDocumentos() {
  return pendSolVO.getListaTiposDocumentos();
}
public Vector getConsulta() {
  return pendSolVO.getConsulta();
}

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            pendSolVO.validate(idioma);
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
