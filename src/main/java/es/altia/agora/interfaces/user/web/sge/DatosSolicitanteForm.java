package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.DatosSolicitanteValueObject;
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
public class DatosSolicitanteForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(DatosSolicitanteForm.class.getName());

    //Reutilizamos
    DatosSolicitanteValueObject datosSolVO = new DatosSolicitanteValueObject();

    public DatosSolicitanteValueObject getDatosSolicitante() {
        return datosSolVO;
    }

    public void setDatosSolicitante(DatosSolicitanteValueObject datosSolVO) {
        this.datosSolVO = datosSolVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public String getCodDepartamento() {
        return datosSolVO.getCodDepartamento();
      }
      public void setCodDepartamento(String codDepartamento) {
        datosSolVO.setCodDepartamento(codDepartamento);
      }
      public String getCodMunicipio() {
        return datosSolVO.getCodMunicipio();
      }
      public void setCodMunicipio(String codMunicipio) {
        datosSolVO.setCodMunicipio(codMunicipio);
      }
      public String getCodPais() {
        return datosSolVO.getCodPais();
      }
      public void setCodPais(String codPais) {
        datosSolVO.setCodPais(codPais);
      }
      public String getCodProcedimiento() {
        return datosSolVO.getCodProcedimiento();
      }
      public void setCodProcedimiento(String codProcedimiento) {
        datosSolVO.setCodProcedimiento(codProcedimiento);
      }
      public String getCodProvincia() {
        return datosSolVO.getCodProvincia();
      }
      public void setCodProvincia(String codProvincia) {
        datosSolVO.setCodProvincia(codProvincia);
      }
      public String getCodTipoDoc() {
        return datosSolVO.getCodTipoDoc();
      }
      public void setCodTipoDoc(String codTipoDoc) {
        datosSolVO.setCodTipoDoc(codTipoDoc);
      }
      public String getDescDepartamento() {
        return datosSolVO.getDescDepartamento();
      }
      public void setDescDepartamento(String descDepartamento) {
        datosSolVO.setDescDepartamento(descDepartamento);
      }
      public String getDescMunicipio() {
        return datosSolVO.getDescMunicipio();
      }
      public void setDescMunicipio(String descMunicipio) {
        datosSolVO.setDescMunicipio(descMunicipio);
      }
      public String getDescPais() {
        return datosSolVO.getDescPais();
      }
      public void setDescPais(String descPais) {
        datosSolVO.setDescPais(descPais);
      }
      public String getDescProcedimiento() {
        return datosSolVO.getDescProcedimiento();
      }
      public void setDescProcedimiento(String descProcedimiento) {
        datosSolVO.setDescProcedimiento(descProcedimiento);
      }
      public String getDescProvincia() {
        return datosSolVO.getDescProvincia();
      }
      public void setDescProvincia(String descProvincia) {
        datosSolVO.setDescProvincia(descProvincia);
      }
      public String getDescTipoDoc() {
        return datosSolVO.getDescTipoDoc();
      }
      public void setDescTipoDoc(String descTipoDoc) {
        datosSolVO.setDescTipoDoc(descTipoDoc);
      }
      public String getTxtCodigoPostal() {
        return datosSolVO.getTxtCodigoPostal();
      }
      public void setTxtCodigoPostal(String txtCodigoPostal) {
        datosSolVO.setTxtCodigoPostal(txtCodigoPostal);
      }
      public String getTxtDNI() {
        return datosSolVO.getTxtDNI();
      }
      public void setTxtDNI(String txtDNI) {
        datosSolVO.setTxtDNI(txtDNI);
      }
      public String getTxtDomicilio() {
        return datosSolVO.getTxtDomicilio();
      }
      public void setTxtDomicilio(String txtDomicilio) {
        datosSolVO.setTxtDomicilio(txtDomicilio);
      }
      public String getTxtExpediente() {
        return datosSolVO.getTxtExpediente();
      }
      public void setTxtExpediente(String txtExpediente) {
        datosSolVO.setTxtExpediente(txtExpediente);
      }
      public String getTxtNombre() {
        return datosSolVO.getTxtNombre();
      }
      public void setTxtNombre(String txtNombre) {
        datosSolVO.setTxtNombre(txtNombre);
      }
      public String getTxtRegistro() {
        return datosSolVO.getTxtRegistro();
      }
      public void setTxtRegistro(String txtRegistro) {
        datosSolVO.setTxtRegistro(txtRegistro);
      }
      public String getTxtTelefono() {
        return datosSolVO.getTxtTelefono();
      }
      public void setTxtTelefono(String txtTelefono) {
        datosSolVO.setTxtTelefono(txtTelefono);
  }

  public Vector getListaDepartamentos() {
   return datosSolVO.getListaDepartamentos();
  }
  public Vector getListaTiposDocumentos() {
   return datosSolVO.getListaTiposDocumentos();
  }
  public Vector getListaPaises() {
   return datosSolVO.getListaPaises();
  }
  public Vector getListaProvincias() {
   return datosSolVO.getListaProvincias();
  }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            datosSolVO.validate(idioma);
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
