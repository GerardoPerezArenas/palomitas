package es.altia.agora.interfaces.user.web.registro;

import es.altia.agora.business.registro.RegistroEntradaSalidaValueObject;
import es.altia.agora.technical.Fecha;
import es.altia.common.service.config.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/** Clase utilizada para capturar o mostrar el estado de un RegistroEntrada */
public class AperturaCierreRegistroForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(AperturaCierreRegistroForm.class.getName());

    //Reutilizamos
    RegistroEntradaSalidaValueObject elRegistroVO = new RegistroEntradaSalidaValueObject();

    public RegistroEntradaSalidaValueObject getRegistroEntradaSalida() {
        return elRegistroVO;
    }

    public void setRegistroEntradaSalida(RegistroEntradaSalidaValueObject registroVO) {
        this.elRegistroVO = registroVO;
    }
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


    public String getTxtDiaUAE(){
        return elRegistroVO.getDia_reg_UAE();
    }

    public String getTxtMesUAE(){
        return elRegistroVO.getMes_reg_UAE();
    }

    public String getTxtAnoUAE(){
        return elRegistroVO.getAno_reg_UAE();
    }

    public void setTxtDiaUAE(String param){
    	this.txtDiaUAE=param;
    }

    public void setTxtMesUAE(String param){
		this.txtMesUAE=param;
    }

    public void setTxtAnoUAE(String param){
		this.txtAnoUAE=param;
    }


    public String getTxtDiaUCE(){
        return elRegistroVO.getDia_reg_UCE();
    }

    public String getTxtMesUCE(){
        return elRegistroVO.getMes_reg_UCE();
    }

    public String getTxtAnoUCE(){
        return elRegistroVO.getAno_reg_UCE();
    }

	public void setTxtDiaUCE(String param){
    	this.txtDiaUCE= param;
    }

    public void setTxtMesUCE(String param){
		this.txtMesUCE=param;
    }

    public void setTxtAnoUCE(String param){
		this.txtAnoUCE=param;
    }



    public String getTxtDiaUAS(){
        return elRegistroVO.getDia_reg_UAS();
    }

    public String getTxtMesUAS(){
        return elRegistroVO.getMes_reg_UAS();
    }

    public String getTxtAnoUAS(){
        return elRegistroVO.getAno_reg_UAS();
    }
    public void setTxtDiaUAS(String param){
    	this.txtDiaUAS= param;
    }

    public void setTxtMesUAS(String param){
		this.txtMesUAS=param;
    }

    public void setTxtAnoUAS(String param){
		this.txtAnoUAS=param;
    }


    public String getTxtDiaUCS(){
        return elRegistroVO.getDia_reg_UCS();
    }

    public String getTxtMesUCS(){
        return elRegistroVO.getMes_reg_UCS();
    }

    public String getTxtAnoUCS(){
        return elRegistroVO.getAno_reg_UCS();
    }
    public void setTxtDiaUCS(String param){
    	this.txtDiaUCS= param;
    }

    public void setTxtMesUCS(String param){
		this.txtMesUCS=param;
    }

    public void setTxtAnoUCS(String param){
		this.txtAnoUCS=param;
    }


    public String getTxtDiaAbrir(){
		return elRegistroVO.getDia_fec_apertura_cierre();
	}

    public String getTxtMesAbrir(){
		return elRegistroVO.getMes_fec_apertura_cierre();
    }

    public String getTxtAnoAbrir(){
		return elRegistroVO.getAno_fec_apertura_cierre();
    }

    public void setTxtDiaAbrir(String param){
    	this.txtDiaAbrir=param;
    }

    public void setTxtMesAbrir(String param){
    	this.txtMesAbrir=param;
    }

    public void setTxtAnoAbrir(String param){
    	this.txtAnoAbrir=param;
    }



	public String getTxtDiaCerrar(){
        return elRegistroVO.getDia_fec_apertura_cierre();
    }

    public String getTxtMesCerrar(){
        return elRegistroVO.getMes_fec_apertura_cierre();
    }

    public String getTxtAnoCerrar(){
        return elRegistroVO.getAno_fec_apertura_cierre();
    }

    public void setTxtDiaCerrar(String param){
    	this.txtDiaCerrar=param;
    }

    public void setTxtMesCerrar(String param){
    	this.txtMesCerrar=param;
    }

    public void setTxtAnoCerrar(String param){
    	this.txtAnoCerrar=param;
    }


	// Fechas completas.
	// Entrada.
	public String getFechaUCE(){
		Date fUCE = elRegistroVO.getReg_UCE();
		if (fUCE != null) {
			Fecha f = new Fecha();
			String ff = f.obtenerString(fUCE);
			return ff;
		} else return "";
	}

	public void setFechaUCE(String param){
		elRegistroVO.setReg_UCE(param);
	}
	
	public String getFechaUCS(){
		Date fUCS = elRegistroVO.getReg_UCS();
		if (fUCS != null) {
			Fecha f = new Fecha();
			String ff = f.obtenerString(fUCS);
			return ff;
		} else return "";
	}
	public void setFechaUCS(String param){
		elRegistroVO.setReg_UCS(param);
	}

	public String getFechaCerrar(){
		Date fAC = elRegistroVO.getFec_apertura_cierre();
		if (fAC != null) {
			Fecha f = new Fecha();
			String ff = f.obtenerString(fAC);
			return ff;
		} else return "";
	}
	public void setFechaCerrar(String param){
		elRegistroVO.setFec_apertura_cierre(param);
	}


	// ----------------


	public String getOpcion(){
		return elRegistroVO.getOpcion();
	}

	public void setOpcion(String param){
		elRegistroVO.setOpcion(param);
	}

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            elRegistroVO.validate(idioma);
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


    private String txtDiaUAE;
    private String txtMesUAE;
    private String txtAnoUAE;
    private String txtDiaUAS;
    private String txtMesUAS;
    private String txtAnoUAS;
	private String txtDiaUCE;
    private String txtMesUCE;
    private String txtAnoUCE;
    private String txtDiaUCS;
    private String txtMesUCS;
    private String txtAnoUCS;

	private String txtDiaAbrir="";
	private String txtMesAbrir="";
	private String txtAnoAbrir="";

	private String txtDiaCerrar="";
	private String txtMesCerrar="";
	private String txtAnoCerrar="";

}
