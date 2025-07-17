package es.altia.agora.business.registro.mantenimiento.persistence;

import java.util.Vector;


import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import es.altia.agora.business.registro.mantenimiento.persistence.manual.MantRegistroExternoDAO;
import es.altia.agora.business.util.GeneralValueObject;

public class MantRegistroExternoManager {

  	// Mi propia instancia usada en el metodo getInstance.
 	private static MantRegistroExternoManager instance = null;

  
  	protected static Config m_ConfigTechnical;
  
    protected static Config m_ConfigError;
    protected static Log m_Log;


    protected MantRegistroExternoManager() {        
    	m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");        
        m_ConfigError = ConfigServiceHelper.getConfig("error");        
        m_Log = LogFactory.getLog(this.getClass().getName());
    }


  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de MantTemaManager
    */
  public static MantRegistroExternoManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
      		// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
            synchronized(MantRegistroExternoManager.class) {
        	if (instance == null)
          		instance = new MantRegistroExternoManager();
        	}
      	}
        return instance;
    }

  public Vector loadOrganizacionesExternas(String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("Cargando organizaciones externas");

    Vector organizaciones = new Vector();

    try{
      organizaciones = MantRegistroExternoDAO.getInstance().loadOrganizacionesExternas(params);
    }catch (Exception te) {
    		organizaciones = new Vector();
            m_Log.error("JDBC Technical problem " + te.getMessage());
            te.printStackTrace();
    }finally{
      return organizaciones;
    }
  }

  public String altaOrganizacionExterna(GeneralValueObject orgExtVO, String[] params) {

	  //queremos estar informados de cuando este metodo es ejecutado
	  m_Log.debug("Alta organizaciones externas");

	  String resultado = "";

	  try{
		resultado = MantRegistroExternoDAO.getInstance().altaOrganizacionExterna(orgExtVO, params);
	  }catch (Exception te) {
			resultado="";
			m_Log.error("JDBC Technical problem " + te.getMessage());
			te.printStackTrace();
	  }finally{
		return resultado;
	  }
	}
  
  
	public String eliminarOrganizacionExterna(GeneralValueObject orgExtVO, String[] params) {

	  //queremos estar informados de cuando este metodo es ejecutado
	  m_Log.debug("Eliminar organizaciones externas");

	  String resultado = "";

	  try{
		resultado = MantRegistroExternoDAO.getInstance().eliminarOrganizacionExterna(orgExtVO, params);
	  }catch (Exception te) {
			  resultado= "no eliminada";
			  m_Log.error("JDBC Technical problem " + te.getMessage());
			  te.printStackTrace();
	  }finally{
		return resultado;
	  }
	}

	public String modificarOrganizacionExterna(GeneralValueObject orgExtVO, String[] params) {

	  //queremos estar informados de cuando este metodo es ejecutado
	  m_Log.debug("Modificar organizaciones externas");

	  String resultado = "";

	  try{
		resultado = MantRegistroExternoDAO.getInstance().modificarOrganizacionExterna(orgExtVO, params);
	  }catch (Exception te) {
			  resultado= "no modificada";
			  m_Log.error("JDBC Technical problem " + te.getMessage());
			  te.printStackTrace();
	  }finally{
		return resultado;
	  }
	}

	public Vector loadUnidadesRegistroExternas(GeneralValueObject orgExtVO, String[] params) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("Cargando unidades de registro externas");

		Vector organizaciones = new Vector();

		try{
		  organizaciones = MantRegistroExternoDAO.getInstance().loadUnidadesRegistroExternas(orgExtVO, params);
		}catch (Exception te) {
				organizaciones = new Vector();
				m_Log.error("JDBC Technical problem " + te.getMessage());
				te.printStackTrace();
		}finally{
		  return organizaciones;
		}
	  }

	public String altaUnidadRegistroExterna(GeneralValueObject uniRegExtVO, String[] params) {

		  //queremos estar informados de cuando este metodo es ejecutado
		  m_Log.debug("Alta unidades de registro externas");

		  String resultado = "";

		  try{
			resultado = MantRegistroExternoDAO.getInstance().altaUnidadRegistroExterna(uniRegExtVO, params);
		  }catch (Exception te) {
				resultado="";
				m_Log.error("JDBC Technical problem " + te.getMessage());
				te.printStackTrace();
		  }finally{
			return resultado;
		  }
		}

	public String modificarUnidadRegistroExterna(GeneralValueObject uniRegExtVO, String[] params) {

		  //queremos estar informados de cuando este metodo es ejecutado
		  m_Log.debug("Modificar unidades de registro externas");

		  String resultado = "";

		  try{
			resultado = MantRegistroExternoDAO.getInstance().modificarUnidadRegistroExterna(uniRegExtVO, params);
		  }catch (Exception te) {
				  resultado= "no modificada";
				  m_Log.error("JDBC Technical problem " + te.getMessage());
				  te.printStackTrace();
		  }finally{
			return resultado;
		  }
		}

	public String eliminarUnidadRegistroExterna(GeneralValueObject uniRegExtVO, String[] params) {

		  //queremos estar informados de cuando este metodo es ejecutado
		  m_Log.debug("Eliminar unidades de registro externas");

		  String resultado = "";

		  try{
			resultado = MantRegistroExternoDAO.getInstance().eliminarUnidadRegistroExterna(uniRegExtVO, params);
		  }catch (Exception te) {
				  resultado= "no eliminada";
				  m_Log.error("JDBC Technical problem " + te.getMessage());
				  te.printStackTrace();
		  }finally{
			return resultado;
		  }
		}

  }