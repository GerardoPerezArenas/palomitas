package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.TablasIntercambiadorasValueObject;
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
public class TablasIntercambiadorasForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(TablasIntercambiadorasForm.class.getName());

    //Reutilizamos
    TablasIntercambiadorasValueObject tabInterVO = new TablasIntercambiadorasValueObject();
    TablasIntercambiadorasValueObject tabInterTramSalVO = new TablasIntercambiadorasValueObject();
    TablasIntercambiadorasValueObject tabInterTramSSalVO = new TablasIntercambiadorasValueObject();
    TablasIntercambiadorasValueObject tabInterTramNSalVO = new TablasIntercambiadorasValueObject();

    public TablasIntercambiadorasValueObject getTablasIntercambiadoras() {
        return tabInterVO;
    }

    public void setTablasIntercambiadoras(TablasIntercambiadorasValueObject tabInterVO) {
        this.tabInterVO = tabInterVO;
    }

    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public Vector getListaTramitesSeleccion() {
        return tabInterVO.getListaTramitesSeleccion();
    }
    public Vector getListaTramitesCondSalSeleccion() {
        return getTramitesCondSal().getListaTramitesSeleccion();
    }
    public Vector getListaTramitesCondSalSSeleccion() {
        return getTramitesSCondSal().getListaTramitesSeleccion();
    }
    public Vector getListaTramitesCondSalNSeleccion() {
        return getTramitesNCondSal().getListaTramitesSeleccion();
    }
    public Vector getListaTramitesTodos() {
        return tabInterVO.getListaTramitesTodos();
    }

    public TablasIntercambiadorasValueObject getTramitesCondSal() {
        return tabInterTramSalVO;
    }
    public void setTramitesCondSal(TablasIntercambiadorasValueObject tabInterTramSalVO) {
        this.tabInterTramSalVO = tabInterTramSalVO;
    }
    public TablasIntercambiadorasValueObject getTramitesSCondSal() {
        return tabInterTramSSalVO;
    }
    public void setTramitesSCondSal(TablasIntercambiadorasValueObject tabInterTramSSalVO) {
        this.tabInterTramSSalVO = tabInterTramSSalVO;
    }
    public TablasIntercambiadorasValueObject getTramitesNCondSal() {
        return tabInterTramNSalVO;
    }
    public void setTramitesNCondSal(TablasIntercambiadorasValueObject tabInterTramNSalVO) {
        this.tabInterTramNSalVO = tabInterTramNSalVO;
    }

    public String getCodMunicipio() {
        return tabInterVO.getCodMunicipio();
    }

    public void setCodMunicipio(String codMunicipio) {
        tabInterVO.setCodMunicipio(codMunicipio);
    }

    public String getCodProcedimiento() {
        return tabInterVO.getCodProcedimiento();
    }

    public void setCodProcedimiento(String codProcedimiento) {
        tabInterVO.setCodProcedimiento(codProcedimiento);
    }

    public String getCodTramite() {
        return tabInterVO.getCodTramite();
    }

    public void setCodTramite(String codTramite) {
        tabInterVO.setCodTramite(codTramite);
    }

    public String getNumeroCondicionSalida() {
        return tabInterVO.getNumeroCondicionSalida();
    }

    public void setNumeroCondicionSalida(String numeroCondicionSalida) {
        tabInterVO.setNumeroCondicionSalida(numeroCondicionSalida);
    }

    public String getObligatorio() {
        return tabInterVO.getObligatorio();
    }

    public void setObligatorio(String obligatorio) {
        tabInterVO.setObligatorio(obligatorio);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            tabInterVO.validate(idioma);
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

    private Vector listaTiposDocumentos;
    private Vector listaCausas;
    private Vector listaCertificados;

    private String seleccionar;


}