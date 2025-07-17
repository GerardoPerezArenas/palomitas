// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.integracionsw.AvanzarRetrocederSWVO;
import es.altia.agora.business.integracionsw.persistence.DefinicionOperacionesSWManager;
import es.altia.agora.business.integracionsw.persistence.manual.DefinicionOperacionesSWDAO;
import java.util.Vector;
import java.sql.Connection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.integracionsw.persistence.manual.DefinicionSWTramitacionDAO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.DepartamentoNotificacionSneVO;
import es.altia.agora.business.sge.FlujoSalidaTramiteVO;
import es.altia.agora.business.sge.TablasIntercambiadorasValueObject;
import es.altia.agora.business.sge.persistence.manual.DefinicionTramitesDAO;
import es.altia.agora.business.sge.persistence.manual.UnidadesTramitacionDAO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.exceptions.InternalErrorException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* <p>Título: Proyecto @gora</p>
* <p>Descripción: Clase DefinicionTramitesManager</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
* @author Jorge Hombre	Tuñas
* @version	1.0
*/

public class DefinicionTramitesManager {

	private static DefinicionTramitesManager instance =	null;
	protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
	protected static Config m_ConfigError; // Para los mensajes de error localizados
	protected static Log m_Log =
            LogFactory.getLog(DefinicionTramitesManager.class.getName());


    protected DefinicionTramitesManager() {
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");
	}
    
    public static DefinicionTramitesManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (DefinicionTramitesManager.class) {
            if (instance == null) {
                instance = new DefinicionTramitesManager();
            }

        }
        return instance;
    }

	public Vector getListaClasifTramites(String[]	params)
	throws AnotacionRegistroException{

	Vector res = null;

	m_Log.debug("getListaClasifTramites");

	try {

		m_Log.debug("Usando persistencia manual");
		res	= DefinicionTramitesDAO.getInstance().getListaClasifTramites(params);
		m_Log.debug("Tipos de	tramites obtenidos");
		//We want	to be	informed when this method has	finalized
		m_Log.debug("getListaClasifTramites");

	}	catch	(Exception ce) {
		res	= null;
		m_Log.error("JDBC Technical	problem " +	ce.getMessage());
		throw new	AnotacionRegistroException("Problema técnico de	JDBC " + ce.getMessage());
	}

	return res;
}

	public Vector getListaTramites(DefinicionTramitesValueObject dTVO,String[] params) throws AnotacionRegistroException{
		Vector	res =	null;
		
		m_Log.debug("getListaTramites");
		
		try {
		
			m_Log.debug("Usando persistencia manual");
			res = DefinicionTramitesDAO.getInstance().getListaTramites(dTVO,params);
			m_Log.debug("nombres de tramites obtenidos");
			//We	want to be informed when this	method has finalized
			m_Log.debug("getListaTramites");
		
		} catch (Exception ce) {
			res = null;
			m_Log.error("JDBC Technical problem " + ce.getMessage());
			throw new AnotacionRegistroException("Problema	técnico de JDBC "	+ ce.getMessage());
		}
		
		return res;
	}
	
	public Vector getListaTramitesFlujoSalidaTodos(TablasIntercambiadorasValueObject tIVO,String[] params) throws AnotacionRegistroException{
		Vector res = null;
		
		m_Log.debug("getListaTramitesFlujoSalidaTodos");
		
		try {
		
			m_Log.debug("Usando persistencia manual");
			res = DefinicionTramitesDAO.getInstance().getListaTramitesFlujoSalidaTodos(tIVO,params);
			m_Log.debug("nombres de tramites obtenidos");
			//We	want to be informed when this	method has finalized
			m_Log.debug("getListaTramitesFlujoSalidaTodos");
		
		} catch (Exception ce) {
			res = null;
			m_Log.error("JDBC Technical problem " + ce.getMessage());
			throw new AnotacionRegistroException("Problema	técnico de JDBC "	+ ce.getMessage());
		}
		
		return res;
	}
	
	
	public TablasIntercambiadorasValueObject getListaTramitesFlujoSalidaSeleccionada(TablasIntercambiadorasValueObject tIVO,String[] params) throws AnotacionRegistroException{
		
		m_Log.debug("getListaTramitesFlujoSalida");
		
		try {
		
			m_Log.debug("Usando persistencia manual");
			tIVO	= DefinicionTramitesDAO.getInstance().getListaTramitesFlujoSalidaSeleccionada(tIVO,params);
			m_Log.debug("nombres de tramites obtenidos");
			//We	want to be informed when this	method has finalized
			m_Log.debug("getListaTramitesFlujoSalida");
		
		} catch (Exception ce) {
			tIVO	= null;
			m_Log.error("JDBC Technical problem " + ce.getMessage());
			throw new AnotacionRegistroException("Problema	técnico de JDBC "	+ ce.getMessage());
		}
		
		return tIVO;
	}
        
    public void comprobarErrorDepartamentoNotificacion(DefinicionTramitesValueObject tramite, String[] params) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        m_Log.debug("comprobarErroresDepartamentoNotificacion");
        adapt = new AdaptadorSQLBD(params);
        try {
            con = adapt.getConnection();
        } catch (BDException ex) {
            m_Log.error("erros estableciendo la conexion : "+ ex.getMessage());
        }
        try {
            DefinicionTramitesDAO.getInstance().comprobarErrorDepartamentoNotificacion(tramite, con);
        } catch (SQLException ex) {
           m_Log.error("erros comprobando errores en los departamentos de notificacion : "+ ex.getMessage());
        }

    }
	
	public TablasIntercambiadorasValueObject getListaTramitesFlujoSalida(TablasIntercambiadorasValueObject tIVO,String[] params) throws AnotacionRegistroException{
		
		m_Log.debug("getListaTramitesFlujoSalida");
		
		try {
		
			m_Log.debug("Usando persistencia manual");
			tIVO	= DefinicionTramitesDAO.getInstance().getListaTramitesFlujoSalida(tIVO,params);
			m_Log.debug("nombres de tramites obtenidos");
			//We	want to be informed when this	method has finalized
			m_Log.debug("getListaTramitesFlujoSalida");
		
		} catch (Exception ce) {
			tIVO	= null;
			m_Log.error("JDBC Technical problem " + ce.getMessage());
			throw new AnotacionRegistroException("Problema	técnico de JDBC "	+ ce.getMessage());
		}
		
		return tIVO;
	}
	
	public int grabarFlujoSalida(TablasIntercambiadorasValueObject tabInterVO,String[] params) throws AnotacionRegistroException {
		//queremos estar informados de cuando	este metodo	es ejecutado
		m_Log.debug("grabarFlujoSalida");
		int i;
	
		try {
			m_Log.debug("Usando persistencia manual");
	
			i =	DefinicionTramitesDAO.getInstance().grabarFlujoSalida(tabInterVO,params);
	
			m_Log.debug("flujo salida insertado correctamente");
			//We want	to be	informed when this method has	finalized
			m_Log.debug("grabarFlujoSalida");
	
		}	catch	(Exception ce) {
			m_Log.error("JDBC Technical	problem " +	ce.getMessage());
			throw new	AnotacionRegistroException("Problema técnico de	JDBC " + ce.getMessage());
	
		}
		return i;
	}
	
	
	public Vector getListaCodTramites(DefinicionTramitesValueObject	defTramVO,String codDepartamento,String[]	params) throws AnotacionRegistroException{
		
		Vector res = null;
		
		m_Log.debug("getListaCodTramites");
		
		try {
		
			m_Log.debug("Usando persistencia manual");
			res	= DefinicionTramitesDAO.getInstance().getListaCodTramites(defTramVO,codDepartamento,params);
			m_Log.debug("Tipos de	codigos de tramites obtenidos");
			//We want	to be	informed when this method has	finalized
			m_Log.debug("getListaCodTramites");
		
		}	catch	(Exception ce) {
			res	= null;
			m_Log.error("JDBC Technical	problem " +	ce.getMessage());
			throw new	AnotacionRegistroException("Problema técnico de	JDBC " + ce.getMessage());
		}
		
		return res;
	}

    public DefinicionTramitesValueObject getTramite(DefinicionTramitesValueObject defTramVO, 
            String codDepartamento, String[] params) throws AnotacionRegistroException {
        return getTramite(defTramVO, codDepartamento, params, false);
    }
    
    public DepartamentoNotificacionSneVO getDepartamentoNotificacionSNEPorCodigo (String codigo, String[] params) {
    
          DepartamentoNotificacionSneVO departamento = new DepartamentoNotificacionSneVO ();
         AdaptadorSQLBD adapt = null;
         Connection con = null;

         try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            departamento = DefinicionTramitesDAO.getInstance().getDepartamentoNotificacionSNEPorCodigo(con, codigo);

         }catch(Exception e){
             e.printStackTrace();
         }finally{
             try{
                 adapt.devolverConexion(con);

             }catch(Exception e){
                 e.printStackTrace();
             }
         }

         return departamento;
    }

    public DefinicionTramitesValueObject getTramite(DefinicionTramitesValueObject defTramVO, 
            String codDepartamento, String[] params, 
            boolean devolverConDatosFirmaFlujo) throws AnotacionRegistroException {
        m_Log.debug("getTramites");

        Connection con = null;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            defTramVO = DefinicionTramitesDAO.getInstance().getTramite(
                    defTramVO, codDepartamento, con, params, devolverConDatosFirmaFlujo);
            if (defTramVO.getCodUnidadTramite().equals(ConstantesDatos.TRA_UTR_OTRAS)) {
                int codMun = Integer.parseInt(defTramVO.getCodMunicipio());
                String codProc = defTramVO.getTxtCodigo();
                int codTram = Integer.parseInt(defTramVO.getCodigoTramite());
                defTramVO.setUnidadesTramitadoras(
                    UnidadesTramitacionDAO.getInstance().getUTRByTramite(codMun,codProc,codTram, con));
            } else {
                defTramVO.setUnidadesTramitadoras(new Vector());
            }
            
        } catch (Exception ce) {
            ce.printStackTrace();
            m_Log.error("JDBC Technical	problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        } finally {
            try {
                if(con != null) con.close();
            } catch(SQLException e) {
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	e.getMessage());
            }
        }

        return defTramVO;
    }

    public Vector<UORDTO> getUTRByTramite(int codOrg, String codProc, int codTram, String[] params)
            throws AnotacionRegistroException {
        m_Log.debug("getUTRByTramite");

        Connection con = null;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Vector<UORDTO> uors = new Vector<UORDTO>();

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            uors = UnidadesTramitacionDAO.getInstance().getUTRByTramite(codOrg,codProc,codTram, con);

        } catch (Exception ce) {
            ce.printStackTrace();
            m_Log.error("JDBC Technical	problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de	JDBC " + ce.getMessage());
        } finally {
            try {
                if(con != null) con.close();
            } catch(SQLException e) {
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	e.getMessage());
            }
        }

        return uors;
    }
	
	public DefinicionTramitesValueObject getTramiteImportar(DefinicionTramitesValueObject defTramVO,String[] params) throws AnotacionRegistroException{
		m_Log.debug("********* DefinicionTramitesManager.getTramiteImportar");
		
        Connection con = null;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        DefinicionTramitesValueObject tramite;

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            
            tramite = DefinicionTramitesDAO.getInstance().getTramiteImportar(defTramVO, con, params);            
            Vector listaTramitesImportar = tramite.getListaTramitesImportar();
            
            for(int i=0;i<listaTramitesImportar.size();i++){
                DefinicionTramitesValueObject dtVO = (DefinicionTramitesValueObject)listaTramitesImportar.get(i);
                // Se recuperan las unidades tramitadoras de cada trámite a importar
                int codMun = Integer.parseInt(tramite.getCodMunicipio());
                String codProc = tramite.getTxtCodigo();
                int codTram = Integer.parseInt(dtVO.getCodigoTramite());
                if (dtVO.getCodUnidadTramite().equals(ConstantesDatos.TRA_UTR_OTRAS)) {
                    dtVO.setUnidadesTramitadoras(
                        UnidadesTramitacionDAO.getInstance().getUTRByTramite(codMun, codProc, codTram, con));
                }
                
                //Se recuperan los datos de la pestaña "Integraciones" para cada trámite
                try{
                    List<AvanzarRetrocederSWVO> listaConfSW = DefinicionOperacionesSWManager.getInstance().getOperacionesSWConParam(codMun,codProc,codTram,params);
                    dtVO.setListaConfSW((Vector<AvanzarRetrocederSWVO>) listaConfSW);
                } catch (TechnicalException te){
                    throw new AnotacionRegistroException("Error al recuperar operaciones SW de trámite del procedimiento a importar");
                }
               
            }// for                             
        } catch (Exception ce) {
            ce.printStackTrace();
            m_Log.error("JDBC Technical	problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        } finally {
            try {
                if(con != null) con.close();
            } catch(SQLException e) {
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	e.getMessage());
            }
        }
        return tramite;	
	}
	
	public int insert(DefinicionTramitesValueObject defTramVO,String[] params) throws AnotacionRegistroException {
		
            int i;
        Connection con = null;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
	
		try {
	        oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

			i =	DefinicionTramitesDAO.getInstance().insert(defTramVO,con,params);
            if (i!=1) {
                oad.rollBack(con);
                m_Log.debug("tramite no insertado");
            } else {
                int codMun = Integer.parseInt(defTramVO.getCodMunicipio());
                String codProc = defTramVO.getTxtCodigo();
                int codTram = Integer.parseInt(defTramVO.getCodigoTramite());
                UnidadesTramitacionDAO.getInstance().
                        insertUTR(codMun, codProc, codTram, defTramVO.getUnidadesTramitadoras(),con);
                oad.finTransaccion(con);
                m_Log.debug("tramite insertado correctamente");
            }
	
		} catch	(Exception ce) {
            try {
                oad.rollBack(con);
            } catch (Exception e) {
			    m_Log.error("JDBC Technical	problem " +	ce.getMessage());
            }
			throw new	AnotacionRegistroException("Problema técnico de	JDBC " + ce.getMessage());		
	    } finally {
            try {
                if(con != null) con.close();
            } catch(SQLException e) {
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	e.getMessage());
            }
        }

		return i;
	}
	
    public int insertImportar(Connection conexion, DefinicionTramitesValueObject defTramVO, String[] params) throws AnotacionRegistroException {

        int i;

        try {

            i = DefinicionTramitesDAO.getInstance().insertImportar(conexion, defTramVO, params);
            if (i > 0) {
                Vector listaTramitesImportar = defTramVO.getListaTramitesImportar();
                HashMap<String,Boolean> insercionOps = new HashMap<String, Boolean>();   
                for (int j = 0; j < listaTramitesImportar.size(); j++) {
                    DefinicionTramitesValueObject dtVO = (DefinicionTramitesValueObject) listaTramitesImportar.get(j);
                    int codMun = Integer.parseInt(defTramVO.getCodMunicipio());
                    String codProc = defTramVO.getTxtCodigo();
                    int codTram = Integer.parseInt(dtVO.getCodigoTramite());

                    Vector<UORDTO> listaUnidadesTramitadoras = dtVO.getUnidadesTramitadoras();
                    // Si el trámite a importar tiene como unidad de trámite el cero, se importan sus unidades tramitadoras

                    m_Log.debug("******* insertInmportar nº unidades tramitadoras: " + listaUnidadesTramitadoras.size());
                    if (dtVO.getCodUnidadTramite().equals(ConstantesDatos.TRA_UTR_OTRAS)) {
                        UnidadesTramitacionDAO.getInstance().
                                insertUTRImportacion(codMun, codProc, codTram, listaUnidadesTramitadoras, conexion);
                    }
                    
                    //Se duplican los datos de la pestaña "Integraciones" para cada trámite del nuevo procedimiento
                    ArrayList<AvanzarRetrocederSWVO> operaciones = new ArrayList<AvanzarRetrocederSWVO>(dtVO.getListaConfSW());
                    if(operaciones.size() > 0){
                        boolean insertaSW = true;
                        for(AvanzarRetrocederSWVO avRetVO : operaciones){
                            if(avRetVO != null){
                                insertaSW = DefinicionOperacionesSWDAO.getInstance().setOperacionSWConParam(avRetVO, codMun, codProc, codTram, conexion);
                                if(!insertaSW)
                                    break;
                            }
                        }
                        insercionOps.put(dtVO.getCodigoTramite(), insertaSW);
                    }
                }
            } else {
                m_Log.debug("tramite no insertado");
            }
            //We want to be informed when	this method	has finalized
            m_Log.debug("insertImportar");

        } catch (Exception ce) {
            ce.printStackTrace();
            m_Log.error("JDBC	Technical problem	" + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

        }
        return i;
    }
	
	public int insertPlantillasDocumentos(DefinicionTramitesValueObject defTramVO,String[] params) throws AnotacionRegistroException {
		//queremos estar informados de cuando	este metodo	es ejecutado
		m_Log.debug("insertPlantillasDocumentos");
		int i;
	
		try {
			m_Log.debug("Usando persistencia manual");
	
			i = DefinicionTramitesDAO.getInstance().insertPlantillasDocumentos(defTramVO,params);
	
			m_Log.debug("tramite insertado correctamente");
			//We want to be informed when	this method	has finalized
			m_Log.debug("insertPlantillasDocumentos");
	
		}	catch	(Exception ce) {
			m_Log.error("JDBC	Technical problem	" + ce.getMessage());
			throw	new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
	
		}
		return i;
	}

	/* Se prodria eliminar .... */
//	public int insertPlantillasDocumentosDuplicar(DefinicionTramitesValueObject defTramVO,String[] params) throws AnotacionRegistroException {
//		//queremos estar informados de cuando	este metodo	es ejecutado
//		m_Log.debug("insertPlantillasDocumentosDuplicar");
//		int i;
//	
//		try {
//			m_Log.debug("Usando persistencia manual");
//
//			i = DefinicionTramitesDAO.getInstance().insertPlantillasDocumentosDuplicar(defTramVO,params);
//
//			m_Log.debug("tramite insertado correctamente");
//			//We want to be informed when	this method	has finalized
//			m_Log.debug("insertPlantillasDocumentosDuplicar");
//
//		}	catch	(Exception ce) {
//			m_Log.error("JDBC	Technical problem	" + ce.getMessage());
//			throw	new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
//
//		}
//		return i;
//	}
	
	
	public int modify(DefinicionTramitesValueObject defTramVO,String[] params) throws AnotacionRegistroException {
		int i;
        Connection con = null;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
	
		try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
	
	i = DefinicionTramitesDAO.getInstance().modify(defTramVO,con,params);

            if (i!=1) {
                oad.rollBack(con);
                m_Log.debug("tramite no modificado");
            } else {
                int codMun = Integer.parseInt(defTramVO.getCodMunicipio());
                String codProc = defTramVO.getTxtCodigo();
                int codTram = Integer.parseInt(defTramVO.getCodigoTramite());
                // Borramos
                UnidadesTramitacionDAO.getInstance().
                        deleteUTRByTramite(codMun, codProc, codTram,con);
                // Insertamos nuevas
                UnidadesTramitacionDAO.getInstance().
                        insertUTR(codMun, codProc, codTram, defTramVO.getUnidadesTramitadoras(),con);
                oad.finTransaccion(con);
                m_Log.debug("tramite modificado correctamente");
            }
            
	
		} catch	(Exception ce) {
            try {
                oad.rollBack(con);
            } catch (Exception e) {
			    m_Log.error("JDBC Technical	problem " +	ce.getMessage());
		}
			throw new	AnotacionRegistroException("Problema técnico de	JDBC " + ce.getMessage());
	    } finally {
            try {
                if(con != null) con.close();
            } catch(SQLException e) {
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	e.getMessage());
            }
        }
        
		return i;
	}
	
	public int eliminar(DefinicionTramitesValueObject defTramVO,String[] params) throws AnotacionRegistroException {
		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.debug("eliminar");
		int i;
	
		try {
			m_Log.debug("Usando persistencia manual");
	
			i = DefinicionTramitesDAO.getInstance().eliminar(defTramVO,params);
	
			m_Log.debug("tramite eliminado correctamente");
			//We	want to be informed when this	method has finalized
			m_Log.debug("eliminar");
	
		} catch (Exception ce) {
			m_Log.error("JDBC Technical problem " + ce.getMessage());
			throw new AnotacionRegistroException("Problema	técnico de JDBC "	+ ce.getMessage());
	
		}
		return i;
	}
	
	public Vector getListaPlantillas(DefinicionTramitesValueObject defTramVO, String[] params) throws AnotacionRegistroException{
		
		Vector res = new Vector();
		m_Log.debug("getListaPlantillas");
		try	{
		
			m_Log.debug("Usando persistencia manual");
			res	= DefinicionTramitesDAO.getInstance().getListaPlantillas(defTramVO, params);
			m_Log.debug("Tipos de	tramites obtenidos");
			//We want	to be	informed when this method has	finalized
			m_Log.debug("getListaPlantillas");
		} catch (Exception ce) {
			res = new Vector();
			m_Log.error("JDBC Technical problem "	+ ce.getMessage());
			throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
		}
		return res;
	}
	
	public int eliminarFisicamente(DefinicionTramitesValueObject defTramVO,String[] params) throws AnotacionRegistroException {

        int i;
        Connection con = null;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);

		try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            
		    i = DefinicionTramitesDAO.getInstance().eliminarFisicamente(defTramVO,con,params);            
            //if (i!=1) {
            if (i==-1){
                oad.rollBack(con);
                m_Log.debug("tramite no eliminado");
            } else {

                oad.finTransaccion(con);
                m_Log.debug("tramite eliminado correctamente");
            }

		} catch	(Exception ce) {
            try {
                oad.rollBack(con);
            } catch (Exception e) {
			    m_Log.error("JDBC Technical	problem " +	ce.getMessage());
            }
			throw new	AnotacionRegistroException("Problema técnico de	JDBC " + ce.getMessage());
	    } finally {
            try {
                if(con != null) con.close();
            } catch(SQLException e) {
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("SQLException del finally: " +	e.getMessage());
            }
        }
			
		return i;
	}
	
	public int comprobarListasLlenas(DefinicionTramitesValueObject	desTramVO,String[] params)
	throws AnotacionRegistroException {
		//queremos estar informados de cuando	este metodo	es ejecutado
		m_Log.debug("comprobarListasLlenas");
		int i;
	
		try {
			m_Log.debug("Usando persistencia manual");
	
			i =	DefinicionTramitesDAO.getInstance().comprobarListasLlenas(desTramVO,params);
	
			m_Log.debug("flujo salida insertado correctamente");
			//We want	to be	informed when this method has	finalized
			m_Log.debug("comprobarListasLlenas");
	
		}	catch	(Exception ce) {
			m_Log.error("JDBC Technical	problem " +	ce.getMessage());
			throw new	AnotacionRegistroException("Problema técnico de	JDBC " + ce.getMessage());
	
		}
		return i;
	}
	
	public Vector getListaEnlaces(DefinicionTramitesValueObject defTramVO, String[] params) throws AnotacionRegistroException{
		
		Vector res = new Vector();
		m_Log.debug("getListaEnlaces");
		try	{
		
			m_Log.debug("Usando persistencia manual");
			res	= DefinicionTramitesDAO.getInstance().getListaEnlaces(defTramVO, params);
			m_Log.debug("Tipos de	tramites obtenidos");
			//We want	to be	informed when this method has	finalized
			m_Log.debug("getListaEnlaces");
		} catch (Exception ce) {
			res = new Vector();
			m_Log.error("JDBC Technical problem "	+ ce.getMessage());
			throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
		}
		return res;
	}


    /**** ORIGINAL
    public long insertWS(int codMunicipio, String codProcedimiento, int codTramite,
    								int codSWAv,int codSWRet,int ord, String[] params) throws AnotacionRegistroException {

      long cod = -1;
	 
		  try {
			DefinicionSWTramitacionDAO.getInstance().insertWS(codMunicipio, 
					codProcedimiento, codTramite,codSWAv,codSWRet,ord,params);
		} catch (TechnicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
  	  return (cod+1);
    } **/

    /**
     *
     * @param codMunicipio: Código del municipio
     * @param codProcedimiento: Código del procedimiento
     * @param codTramite: Código del trámite
     * @param codSWAv: Código de la operación de avanzar
     * @param codSWRet: Código de la operación de retroceder
     * @param codSWIniciar: Código de la operación al iniciar
     * @param ord: Orden
     * @param tipoOrigenAvanzar: tipo de operación de avanzar (WS o MODULO)
     * @param tipoOrigenRetroceder: tipo de operación de retroceder (WS o MODULO)
     * @param tipoOrigenIniciar: tipo de operación al iniciar (WS o MODULO)     .
     * @param nombreModuloAvanzar: Nombre del módulo correspondiente a la operación de avanzar de un módulo de integración
     * @param nombreModuloRetroceder: Nombre del módulo correspondiente a la operación de retroceder de un módulo de integración
     * @param nombreModuloIniciar   : Nombre del módulo correspondiente a la operación de iniciar de un módulo de integración
     * @param params: Parámetros de conexión a la base de datos
     * @return long
     * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
     public long insertWS(int codMunicipio, String codProcedimiento, int codTramite,
    								int codSWAv,int codSWRet,int codSWInicio,int ord,String tipoOrigenAvanzar,String tipoOrigenRetroceder,String tipoOrigenIniciar,
                                    String tituloOperacionAvanzar,String tituloOperacionRetroceder,String tituloOperacionIniciar,String nombreModuloAvanzar,String nombreModuloRetroceder,String nombreModuloIniciar,int codTipoRetroceso,String[] params) throws AnotacionRegistroException {

      long cod = -1;

		try {
            /*
			DefinicionSWTramitacionDAO.getInstance().insertWS(codMunicipio,
					codProcedimiento, codTramite,codSWAv,codSWRet,ord,tipoOrigenAvanzar,tipoOrigenRetroceder,tituloOperacionAvanzar,tituloOperacionRetroceder,nombreModuloAvanzar,nombreModuloRetroceder,params);
             **/
            DefinicionSWTramitacionDAO.getInstance().insertWS(codMunicipio,
					codProcedimiento, codTramite,codSWAv,codSWRet,codSWInicio,ord,tipoOrigenAvanzar,tipoOrigenRetroceder,tipoOrigenIniciar,tituloOperacionAvanzar,tituloOperacionRetroceder,tituloOperacionIniciar,nombreModuloAvanzar,nombreModuloRetroceder,nombreModuloIniciar,codTipoRetroceso,params);

		} catch (TechnicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	  return (cod+1);
    }


    
    /**
     * Recupera el código visible de un trrámite
     * @param codProcedimiento: Código del procedimiento
     * @param codTramite: Código del trámite
     * @param params: Parámetros de conexión a la base de datos
     * @return String
     */
   public String getCodigoVisibleTramite(String codMunicipio,String codProcedimiento,String codTramite, String[] params) {

		String codigo ="";
        Connection con = null;
		m_Log.debug("getCodigoVisibleTramite init");
		try	{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
			m_Log.debug("Usando persistencia manual");
			codigo	= DefinicionTramitesDAO.getInstance().getCodigoVisibleTramite(codMunicipio, codProcedimiento, codTramite,con);
		} catch (Exception e) {			
			e.printStackTrace();
		}finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
		return codigo;
	}// getNombreTramite

   
   
    /**
     * Recupera la definición de flujo de salida de un trámite de un determinado procedimiento
     * @param codTramite: Código trámite
     * @param codProcedimiento: Código procedimiento
     * @param codMunicipio: Código del municipio
     * @param con: Conexión a la base de datos
     * @return ArrayList<FlujoSalidaTramiteVO>
     */
    public ArrayList<FlujoSalidaTramiteVO> getFlujoSalidaTramiteImportacion(int codTramite,String codProcedimiento,int codMunicipio,String[] params) {
        ArrayList<FlujoSalidaTramiteVO> salida = null;
        Connection con = null;
        try{
            AdaptadorSQLBD a = new AdaptadorSQLBD(params);
            con = a.getConnection();

            salida = DefinicionTramitesDAO.getInstance().getFlujoSalidaTramiteImportacion(codTramite, codProcedimiento, codMunicipio, con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return salida;
    }


    /**
     * Recupera la información de una pantalla externa de trámite si la tiene
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la BBDD
     * @return DefinicionTramitesValueObject o null si el trámite no tiene asociada un plugin de pantalla de tramitación externa
     */
    public DefinicionTramitesValueObject getInfoPantallaExternaTramite(String codOrganizacion,String codTramite,String codProcedimiento,String[] params){
        
        DefinicionTramitesValueObject dfvo = null;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            dfvo = DefinicionTramitesDAO.getInstance().getInfoPantallaExternaTramite(codOrganizacion, codTramite, codProcedimiento, con);

        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                m_Log.error("getInfoPantallaExternaTramite: Error al cerrar la conexión a la BBDD");
            }
        }
        return dfvo;

    }// getInfoPantallaExternaTramite
    
    
     public DefinicionTramitesValueObject getInfoNotificacionElectronicaTramite(int codTramite,String codProcedimiento,String codMunicipio,String[] params){
        DefinicionTramitesValueObject dfvo = null;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            dfvo = DefinicionTramitesDAO.getInstance().getInfoNotificacionElectronicaTramite(codTramite,codProcedimiento,codMunicipio,con);

        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                m_Log.error("getInfoPantallaExternaTramite: Error al cerrar la conexión a la BBDD");
            }
        }
        return dfvo;
         
     }



     public ArrayList<DepartamentoNotificacionSneVO> getDepartamentosNotificacionSNE(String[] params){
         ArrayList<DepartamentoNotificacionSneVO> departamentos = new ArrayList<DepartamentoNotificacionSneVO>();
         AdaptadorSQLBD adapt = null;
         Connection con = null;

         try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            departamentos = DefinicionTramitesDAO.getInstance().getDepartamentosNotificacionSNE(con);

         }catch(Exception e){
             e.printStackTrace();
         }finally{
             try{
                 adapt.devolverConexion(con);

             }catch(Exception e){
                 e.printStackTrace();
             }
         }

         return departamentos;
     }
        
}