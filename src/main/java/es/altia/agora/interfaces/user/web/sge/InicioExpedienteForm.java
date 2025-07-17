package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.InicioExpedienteValueObject;
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

/** Clase utilizada para capturar o mostrar el estado del BuzonEntradaSGE */
public class InicioExpedienteForm extends ActionForm {
	//Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(BuzonEntradaSGEForm.class.getName());
    //Reutilizamos

	private Vector procedimientos;

	private InicioExpedienteValueObject inicioExpedienteVO = new InicioExpedienteValueObject();	

	public Vector getProcedimientos(){ return procedimientos; }
    public void setProcedimientos(Vector pro){ procedimientos=pro; }
	

	public String getRegistro(){ return inicioExpedienteVO.getRegistro(); }
	public void setRegistro(String reg){ inicioExpedienteVO.setRegistro(reg); }

	public String getCodProcedimiento(){ return inicioExpedienteVO.getCodProcedimiento(); }
	public void setCodProcedimiento(String cod){ inicioExpedienteVO.setCodProcedimiento(cod); }

	public String getTercero(){ return inicioExpedienteVO.getTercero(); }
	public void setTercero(String ter){ inicioExpedienteVO.setTercero(ter); }

	public String getVersion(){ return inicioExpedienteVO.getVersion(); }
	public void setVersion(String ver){ inicioExpedienteVO.setVersion(ver); }

	public String getFechaInicio(){ return inicioExpedienteVO.getFechaInicio(); }
	public void setFechaInicio(String ini){ inicioExpedienteVO.setFechaInicio(ini); }


	public String getDescProcedimiento(){ return inicioExpedienteVO.getDescProcedimiento(); }
	public void setDescProcedimiento(String desc){ inicioExpedienteVO.setDescProcedimiento(desc); }	

	public String getEjercicio(){ return inicioExpedienteVO.getEjercicio();	}
	public void setEjercicio(String eje){ inicioExpedienteVO.setEjercicio(eje); }	   

	public String getNumero(){ return inicioExpedienteVO.getNumero(); }
	public void setNumero(String num){ inicioExpedienteVO.setNumero(num); }
	 
		
	public String getDepartamentoRes(){ return inicioExpedienteVO.getDepartamentoRes(); }
	public void setDepartamentoRes(String dep){ inicioExpedienteVO.setDepartamentoRes(dep); }

	public String getUnidadOrgRes(){ return inicioExpedienteVO.getUnidadOrgRes(); }
	public void setUnidadOrgRes(String uor){ inicioExpedienteVO.setUnidadOrgRes(uor); }	

	public String getEjercicioRes(){ return inicioExpedienteVO.getEjercicioRes();	}
	public void setEjercicioRes(String eje){ inicioExpedienteVO.setEjercicioRes(eje); }	   

	public String getNumeroRes(){ return inicioExpedienteVO.getNumeroRes(); }
	public void setNumeroRes(String num){ inicioExpedienteVO.setNumeroRes(num); }

	public String getTipoRes(){ return inicioExpedienteVO.getTipoRes(); }
	public void setTipoRes(String tip){ inicioExpedienteVO.setTipoRes(tip); }
	

	public String getCodTipoDocumento() { return inicioExpedienteVO.getCodTipoDocumento();}
	public void setCodTipoDocumento(String cTDoc) { inicioExpedienteVO.setCodTipoDocumento(cTDoc); }

	public String getDesTipoDocumento() { return inicioExpedienteVO.getDesTipoDocumento();}
	public void setDesTipoDocumento(String dTDoc) { inicioExpedienteVO.setDesTipoDocumento(dTDoc); }
	
	public String getDocumento() { return inicioExpedienteVO.getDocumento();}
	public void setDocumento(String doc) { inicioExpedienteVO.setDocumento(doc); }

	public String getNombre() { return inicioExpedienteVO.getNombre();}
	public void setNombre(String nom) { inicioExpedienteVO.setNombre(nom); }

	public String getTelefono() { return inicioExpedienteVO.getTelefono();}
	public void setTelefono(String tel) { inicioExpedienteVO.setTelefono(tel); }

	public String getEmail() { return inicioExpedienteVO.getEmail(); }
	public void setEmail(String em) { inicioExpedienteVO.setEmail(em); }		
	
	
	public String getProvincia() { return inicioExpedienteVO.getProvincia(); }
	public void setProvincia(String pro) { inicioExpedienteVO.setProvincia(pro); }
	
	public String getMunicipio() { return inicioExpedienteVO.getMunicipio(); }
	public void setMunicipio(String mun) { inicioExpedienteVO.setMunicipio(mun); }
	
	public String getDomicilio() { return inicioExpedienteVO.getDomicilio(); }
	public void setDomicilio(String dom) { inicioExpedienteVO.setDomicilio(dom); }

	public String getPoblacion() { return inicioExpedienteVO.getPoblacion(); }
	public void setPoblacion(String pob) { inicioExpedienteVO.setPoblacion(pob); }
	
	public String getCodigoPostal() { return inicioExpedienteVO.getCodigoPostal(); }
	public void setCodigoPostal(String cP) { inicioExpedienteVO.setCodigoPostal(cP); }	


	public InicioExpedienteValueObject getInicioExpedienteVO(){ return inicioExpedienteVO; }
    public void setInicioExpedienteVO(InicioExpedienteValueObject inicioVO){ inicioExpedienteVO=inicioVO; }

	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
		m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //BuzonEntradaSGEValueObject hara el trabajo para nostros ...
        try {
			inicioExpedienteVO.validate(idioma);
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
    
