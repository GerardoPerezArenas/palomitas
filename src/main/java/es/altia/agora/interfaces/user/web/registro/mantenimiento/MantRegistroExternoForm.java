package es.altia.agora.interfaces.user.web.registro.mantenimiento;


import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


import java.util.Vector;

import org.apache.struts.action.ActionForm;


public class MantRegistroExternoForm extends ActionForm {
   
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(MantRegistroExternoForm.class.getName());
    
    private Vector listaOrganizacionesExternas;
	private Vector listaUnidadesRegistroExternas;
    private String resultado = "";
    
    public Vector getListaOrganizacionesExternas(){
        return listaOrganizacionesExternas;
    }
    public void setListaOrganizacionesExternas(Vector param){
    	this.listaOrganizacionesExternas=param;
    }
	public Vector getListaUnidadesRegistroExternas(){
			return listaUnidadesRegistroExternas;
	}
	public void setListaUnidadesRegistroExternas(Vector param){
			this.listaUnidadesRegistroExternas=param;
	}	
	public String getResultado(){
			return resultado;
		}
	public void setResultado(String param){
			this.resultado=param;
		}
    
}
