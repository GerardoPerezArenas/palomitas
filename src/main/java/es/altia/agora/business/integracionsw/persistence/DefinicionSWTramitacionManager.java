package es.altia.agora.business.integracionsw.persistence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.integracionsw.AvanzarRetrocederSWVO;
import es.altia.agora.business.integracionsw.InfoConfTramSWVO;
import es.altia.agora.business.integracionsw.persistence.manual.DefinicionSWTramitacionDAO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.gestionInformes.persistence.CampoValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.util.exceptions.InternalErrorException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class DefinicionSWTramitacionManager {

    private static DefinicionSWTramitacionManager instance = null;

    protected static Log m_Log = LogFactory.getLog(DefinicionSWTramitacionManager.class.getName());

    protected DefinicionSWTramitacionManager() {}

    public static DefinicionSWTramitacionManager getInstance() {
        if (instance == null) {
            synchronized(DefinicionSWTramitacionManager.class) {
                if (instance == null) instance = new DefinicionSWTramitacionManager();
            }
        }
        return instance;
    }

    public boolean existeOperacionAsociada(int codigoOp, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Comienzo existeServicioWebPorTitulo");
        try {
            return DefinicionSWTramitacionDAO.getInstance().existeOperacionAsociada(codigoOp, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Fin existeServicioWebPorTitulo");
        }
    }

    public InfoConfTramSWVO[] getInfoSWTramite(AvanzarRetrocederSWVO avRet, String[] params)
            throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Comienzo getCodigosOpTramite");
        try {
            return DefinicionSWTramitacionDAO.getInstance().getInfoSWTramite(avRet, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Fin getCodigosOpTramite");
        }
    }

//    public void creaActualizaOpsTramite(DefinicionTramitesValueObject defTramVO, String[] params)
//            throws InternalErrorException {
//
//        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Comienzo creaActualizaOpsTramite");
//        try {
//            DefinicionSWTramitacionDAO.getInstance().creaActualizaOpsTramite(defTramVO, params);
//        } catch (TechnicalException te) {
//            m_Log.error(te.getMessage(), te);
//            throw new InternalErrorException(te);
//        } finally {
//            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Fin creaActualizaOpsTramite");
//        }
//    }

//    public void crearListaParamsTramite(int codMunicipio, String codProcedimiento, int codTramite,
//                                                 boolean avanzar, int newCodigoOp, String[] params)
//            throws InternalErrorException {
//
//        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Comienzo crearListaParamsTramite");
//        try {
//            // Recuperamos (si existe) el codigo de la operacion asociado a la lista creada.
//            int codigoOp = DefinicionSWTramitacionDAO.getInstance().getCodigoListaParams(codMunicipio, codProcedimiento,
//                    codTramite, avanzar, params);
//            // Si existe, pero no es igual al que hemos pasado, eliminamos la lista creada.
//            if (codigoOp != -1 && codigoOp != newCodigoOp) {
//                DefinicionSWTramitacionDAO.getInstance().eliminarListaParams(codMunicipio, codProcedimiento, codTramite,
//                        avanzar, params);
//                codigoOp = -1;
//            }
//            // Si no existe, la creamos.
//            if (codigoOp == -1) {
//                Collection paramsIn = DefinicionOperacionesSWManager.getInstance().getParamsDefEntrada(newCodigoOp, params);
//                Collection paramsOut = DefinicionOperacionesSWManager.getInstance().getParamsDefSalida(newCodigoOp, params);
//                DefinicionSWTramitacionDAO.getInstance().crearListaParams(codMunicipio, codProcedimiento,
//                    codTramite, avanzar, paramsIn, paramsOut, params);
//            }
//
//        } catch (TechnicalException te) {
//            m_Log.error(te.getMessage(), te);
//            throw new InternalErrorException(te);
//        } finally {
//            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Fin crearListaParamsTramite");
//        }
//    }

    public Collection getParamsEntradaTramite(int codMunicipio, String codProcedimiento, int codTramite,
                                                  boolean avanzar, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Comienzo getParamsEntradaTramite");
        try {
            return DefinicionSWTramitacionDAO.getInstance().getParamsEntradaTramite(codMunicipio, codProcedimiento,
                    codTramite, avanzar, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Fin getParamsEntradaTramite");
        }
    }

    public Collection getParamsSalidaTramite(int codMunicipio, String codProcedimiento, int codTramite,
                                                  boolean avanzar, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Comienzo getParamsSalidaTramite");
        try {
            return DefinicionSWTramitacionDAO.getInstance().getParamsSalidaTramite(codMunicipio, codProcedimiento, 
                    codTramite, avanzar, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Fin getParamsSalidaTramite");
        }
    }
    
    public boolean esOperacionObligatoria(int codMunicipio, String codProcedimiento, int codTramite, boolean avanzar,
                                         int codigoOp, String[] params) throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Comienzo esOperacionObligatoria");
        try {
            return DefinicionSWTramitacionDAO.getInstance().esOperacionObligatoria(codMunicipio, codProcedimiento,
                    codTramite, avanzar, codigoOp, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Fin esOperacionObligatoria");
        }
    }

    public int getMaxOrd (int codMunicipio, String codProcedimiento, int codTramite, String[] params)
    													throws InternalErrorException {
      													
    
      if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Comienzo getMaxOrd");			
      
      try {
          return DefinicionSWTramitacionDAO.getInstance().getMaxOrd(codMunicipio, codProcedimiento,
                  codTramite, params);
      } catch (TechnicalException te) {
          m_Log.error(te.getMessage(), te);
          throw new InternalErrorException(te);
      } finally {
          if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Fin getMaxOrd");
      }
      
    }
    

    
    public void removeWSyOrdena (int codMunicipio, String codProcedimiento, int codTramite, int orden, 
			String[] params) throws AnotacionRegistroException{
    	try {
    		DefinicionSWTramitacionDAO.getInstance().removeWSyOrdena(codMunicipio, codProcedimiento, codTramite, 
				orden,params);
    	} catch (TechnicalException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (InternalErrorException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }


    /**
     * Recupera la lista de tareas pendientes que están asociados a una ocurrencia de un trámite de un determinado procedimiento
     * @param codMunicipio: Código del municipio
     * @param codProcedimiento: Cod. del procedimiento
     * @param codTramite: Cod. del trámite
     * @param params: Params de conexión a la BD
     * @return
     * @throws es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
    public Vector getListaConfSWTareasPendientes (int codMunicipio, String codProcedimiento, int codTramite,int ocurrencia,String numExpediente,String[] params) throws AnotacionRegistroException{
    	Vector listaAux = new Vector();
    	Vector lista = new Vector();
        Hashtable<Integer,Hashtable<String,GeneralValueObject>> datos = null;
		try {
            /*
			listaAux =(Vector)(DefinicionSWTramitacionDAO.getInstance().getListaConfSW(codMunicipio,
												codProcedimiento,codTramite,params)).elementAt(0);
			listaAuxCfos =(Vector)(DefinicionSWTramitacionDAO.getInstance().getListaConfSW(codMunicipio,
					codProcedimiento,codTramite,params)).elementAt(1);
             *             
             */
            datos = DefinicionSWTramitacionDAO.getInstance().getListaConfSWPendientes(codMunicipio,codProcedimiento,codTramite,ocurrencia,numExpediente,params);
            Set<Integer> keySet = datos.keySet();
            ArrayList<Integer> claves = new ArrayList<Integer>();
            claves.addAll(keySet);

            for(int i=(claves.size()-1);i>=0;i--){
                int clave = claves.get(i);
                m_Log.debug(" -- Tratando operaciones del orden " + clave);
                // Se recuperan los elementos de una fila de operaciones
                Hashtable<String,GeneralValueObject> orden = datos.get(clave);
                AvanzarRetrocederSWVO avRetSWVO = new AvanzarRetrocederSWVO();
                if(!orden.containsKey("INICIAR")){
                    avRetSWVO.setCodIniciar(-1);
                    avRetSWVO.setCfoIniciar(-1);
                    avRetSWVO.setIdTareaPendiente(-1);
                    avRetSWVO.setTipoOperacion(null);
                }else{
                    GeneralValueObject gVO = orden.get("INICIAR");
                    int codOp = (Integer)gVO.getAtributo("COD_OP");
                    long cfo = (Long)gVO.getAtributo("CFO");
                    int idTarea = (Integer)gVO.getAtributo("ID_TAREA_PENDIENTE");
                    String tipoOperacion = (String)gVO.getAtributo("TIPO_OPERACION");

                    avRetSWVO.setCodIniciar(codOp);
                    avRetSWVO.setCfoIniciar(cfo);
                    avRetSWVO.setIdTareaPendiente(idTarea);
                    avRetSWVO.setTipoOperacion(tipoOperacion);
                }

                if(!orden.containsKey("AVANZAR")){
                    avRetSWVO.setCodAvanzar(-1);
                    avRetSWVO.setCfoAvanzar(-1);
                }else{
                    GeneralValueObject gVO = orden.get("AVANZAR");
                    int codOp = (Integer)gVO.getAtributo("COD_OP");
                    long cfo = (Long)gVO.getAtributo("CFO");
                    avRetSWVO.setCodAvanzar(codOp);
                    avRetSWVO.setCfoAvanzar(cfo);                    
                }

                 if(!orden.containsKey("RETROCEDER")){
                    avRetSWVO.setCodRetroceder(-1);
                    avRetSWVO.setCfoRetroceder(-1);                    
                }else{
                    GeneralValueObject gVO = orden.get("RETROCEDER");
                    int codOp = (Integer)gVO.getAtributo("COD_OP");
                    long cfo = (Long)gVO.getAtributo("CFO");
                    avRetSWVO.setCodRetroceder(codOp);
                    avRetSWVO.setCfoRetroceder(cfo);                    
                }

                lista.addElement(avRetSWVO);
            }// while


		} catch (TechnicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	m_Log.debug("Long: " + listaAux.size());

    	for(Iterator it=lista.iterator();it.hasNext();){
    		m_Log.debug((AvanzarRetrocederSWVO)it.next());
    	}
    	return lista;
    }


   public Vector getListaConfSW (int codMunicipio, String codProcedimiento, int codTramite,String[] params) throws AnotacionRegistroException{
    	Vector listaAux = new Vector();
    	Vector lista = new Vector();    	
        Hashtable<Integer,Hashtable<String,GeneralValueObject>> datos = null;
		try {
           
            datos = DefinicionSWTramitacionDAO.getInstance().getListaConfSW(codMunicipio,codProcedimiento,codTramite,params);            
            Set<Integer> keySet = datos.keySet();
            ArrayList<Integer> claves = new ArrayList<Integer>();
            claves.addAll(keySet);
            
            for(int i=(claves.size()-1);i>=0;i--){            
                int clave = claves.get(i);
                m_Log.debug(" -- Tratando operaciones del orden " + clave);
                // Se recuperan los elementos de una fila de operaciones
                Hashtable<String,GeneralValueObject> orden = datos.get(clave);
                AvanzarRetrocederSWVO avRetSWVO = new AvanzarRetrocederSWVO();
                if(!orden.containsKey("INICIAR")){                                        
                    avRetSWVO.setCodIniciar(-1);
                    avRetSWVO.setCfoIniciar(-1);                    
                }else{
                    GeneralValueObject gVO = orden.get("INICIAR");
                    int codOp = (Integer)gVO.getAtributo("COD_OP");
                    long cfo = (Long)gVO.getAtributo("CFO");
                    String nombreOperacion = (String)gVO.getAtributo("NOMBRE_OPERACION");
                    String tipoOrigen = (String)gVO.getAtributo("TIPO_ORIGEN");
                    String nombreModulo = (String)gVO.getAtributo("NOMBRE_MODULO");

                    avRetSWVO.setCodIniciar(codOp);
                    avRetSWVO.setCfoIniciar(cfo);
                    //avRetSWVO.setNombreOperacion(nombreOperacion.trim());
                    //avRetSWVO.setTipoOrigenOperacion(tipoOrigen);
                    avRetSWVO.setNombreOperacionIniciar(nombreOperacion);
                    avRetSWVO.setTipoOperacionIniciar(tipoOrigen);
                    if(nombreModulo!=null || !"".equals(nombreModulo) || !"null".equals(nombreModulo))
                        avRetSWVO.setNombreModuloIniciar(nombreModulo);
                    else
                        avRetSWVO.setNombreModuloIniciar("");

                    avRetSWVO.setTipoRetroceso(-1);
                    avRetSWVO.setNumeroOrden((Integer) gVO.getAtributo("NUMERO_ORDEN"));
                    avRetSWVO.setObligatorio((Integer) gVO.getAtributo("OBLIGATORIO"));
                            
                }

                if(!orden.containsKey("AVANZAR")){
                    avRetSWVO.setCodAvanzar(-1);
                    avRetSWVO.setCfoAvanzar(-1);
                }else{
                    GeneralValueObject gVO = orden.get("AVANZAR");
                    int codOp = (Integer)gVO.getAtributo("COD_OP");
                    long cfo = (Long)gVO.getAtributo("CFO");
                    String nombreOperacion = (String)gVO.getAtributo("NOMBRE_OPERACION");
                    String tipoOrigen = (String)gVO.getAtributo("TIPO_ORIGEN");
                    avRetSWVO.setCodAvanzar(codOp);
                    avRetSWVO.setCfoAvanzar(cfo);
                    String nombreModulo = (String)gVO.getAtributo("NOMBRE_MODULO");

                    avRetSWVO.setNombreOperacionAvanzar(nombreOperacion);
                    avRetSWVO.setTipoOperacionAvanzar(tipoOrigen);

                    if(nombreModulo!=null || !"".equals(nombreModulo) || !"null".equals(nombreModulo))
                        avRetSWVO.setNombreModuloAvanzar(nombreModulo);
                    else
                        avRetSWVO.setNombreModuloAvanzar("");
                    
                    avRetSWVO.setTipoRetroceso(-1);
                    avRetSWVO.setNumeroOrden((Integer) gVO.getAtributo("NUMERO_ORDEN"));
                    avRetSWVO.setObligatorio((Integer) gVO.getAtributo("OBLIGATORIO"));
                }

                 if(!orden.containsKey("RETROCEDER")){
                    avRetSWVO.setCodRetroceder(-1);
                    avRetSWVO.setCfoRetroceder(-1);
                }else{
                    GeneralValueObject gVO = orden.get("RETROCEDER");
                    int codOp = (Integer)gVO.getAtributo("COD_OP");
                    long cfo = (Long)gVO.getAtributo("CFO");
                    String nombreOperacion = (String)gVO.getAtributo("NOMBRE_OPERACION");
                    String tipoOrigen = (String)gVO.getAtributo("TIPO_ORIGEN");
                    String nombreModulo = (String)gVO.getAtributo("NOMBRE_MODULO");                    
                    String tipoOperacionRetroceso = (String)gVO.getAtributo("TIPO_OPERACION_RETROCESO");
                    
                    avRetSWVO.setCodRetroceder(codOp);
                    avRetSWVO.setCfoRetroceder(cfo);
                    
                    avRetSWVO.setNombreOperacionRetroceder(nombreOperacion);
                    avRetSWVO.setTipoOperacionRetroceder(tipoOrigen);

                    if(nombreModulo!=null || !"".equals(nombreModulo) || !"null".equals(nombreModulo))
                        avRetSWVO.setNombreModuloRetroceder(nombreModulo);
                    else
                        avRetSWVO.setNombreModuloRetroceder("");
                    
                    if(tipoOperacionRetroceso!=null && tipoOperacionRetroceso.length()>0) {
                        avRetSWVO.setTipoRetroceso(Integer.parseInt(tipoOperacionRetroceso));
                    }
                    avRetSWVO.setNumeroOrden((Integer) gVO.getAtributo("NUMERO_ORDEN"));
                    avRetSWVO.setObligatorio((Integer) gVO.getAtributo("OBLIGATORIO"));
                    
                }// else
                 
                lista.addElement(avRetSWVO);
            }// while

            
		} catch (TechnicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	m_Log.debug("Long: " + listaAux.size());

    	for(Iterator it=lista.iterator();it.hasNext();){
    		m_Log.debug((AvanzarRetrocederSWVO)it.next());
    	}
    	return lista;
    }


    //public Vector getListaConfSW (int codMunicipio, String codProcedimiento, int codTramite,int tipoAvz,String[] params) throws AnotacionRegistroException{
   public Vector getListaConfSW (int codMunicipio, String codProcedimiento, int codTramite,int tipoAvz,int ocurrenciaTramite,String numExpediente,int ejercicio,boolean cerrado,String[] params) throws AnotacionRegistroException{
    	Vector listaAux = new Vector();
    	Vector lista = new Vector();
        Hashtable<Integer,Hashtable<String,GeneralValueObject>> datos = null;
	try {
            
                    
            datos = DefinicionSWTramitacionDAO.getInstance().getListaConfSW(codMunicipio,codProcedimiento,codTramite,tipoAvz,ocurrenciaTramite,numExpediente,ejercicio,cerrado,params);
            Set<Integer> keySet = datos.keySet();
            ArrayList<Integer> claves = new ArrayList<Integer>();
            claves.addAll(keySet);

            for(int i=(claves.size()-1);i>=0;i--){
                int clave = claves.get(i);
                m_Log.debug(" -- Tratando operaciones del orden " + clave);
                // Se recuperan los elementos de una fila de operaciones
                Hashtable<String,GeneralValueObject> orden = datos.get(clave);
                AvanzarRetrocederSWVO avRetSWVO = new AvanzarRetrocederSWVO();
                if(!orden.containsKey("INICIAR")){
                    avRetSWVO.setCodIniciar(-1);
                    avRetSWVO.setCfoIniciar(-1);
                }else{
                    GeneralValueObject gVO = orden.get("INICIAR");
                    int codOp = (Integer)gVO.getAtributo("COD_OP");
                    long cfo = (Long)gVO.getAtributo("CFO");
                    avRetSWVO.setCodIniciar(codOp);
                    avRetSWVO.setCfoIniciar(cfo);
                }

                if(!orden.containsKey("AVANZAR")){
                    avRetSWVO.setCodAvanzar(-1);
                    avRetSWVO.setCfoAvanzar(-1);
                }else{
                    GeneralValueObject gVO = orden.get("AVANZAR");
                    int codOp = (Integer)gVO.getAtributo("COD_OP");
                    long cfo = (Long)gVO.getAtributo("CFO");
                    avRetSWVO.setCodAvanzar(codOp);
                    avRetSWVO.setCfoAvanzar(cfo);
                }

                 if(!orden.containsKey("RETROCEDER")){
                    avRetSWVO.setCodRetroceder(-1);
                    avRetSWVO.setCfoRetroceder(-1);
                }else{
                    GeneralValueObject gVO = orden.get("RETROCEDER");
                    int codOp = (Integer)gVO.getAtributo("COD_OP");
                    long cfo = (Long)gVO.getAtributo("CFO");
                    avRetSWVO.setCodRetroceder(codOp);
                    avRetSWVO.setCfoRetroceder(cfo);
                }

                lista.addElement(avRetSWVO);
            }// while


		} catch (TechnicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	m_Log.debug("Long: " + listaAux.size());

    	for(Iterator it=lista.iterator();it.hasNext();){
    		m_Log.debug((AvanzarRetrocederSWVO)it.next());
    	}
    	return lista;
    }



   public void updateWS(long cfoAv, long cfoRet,long cfoIni, int codMunicipio, String codProcedimiento, int codTramite, int orden,int codAvanzarSW,int codRetrocederSW,
			int codIniciarSW,String tipoOperacionAvanzar,String tipoOperacionRetroceder,String tipoOperacionIniciar,String tituloOperacionAvanzar,String tituloOperacionRetroceder,String tituloOperacionIniciar,String nombreModuloAvanzar,String nombreModuloRetroceder, String nombreModuloIniciar,int codTipoRetrocesoSW,String[] params) throws AnotacionRegistroException{

    	try {

			DefinicionSWTramitacionDAO.getInstance().updateWS(cfoAv,cfoRet,cfoIni,codMunicipio, codProcedimiento,
					codTramite, orden, codAvanzarSW,codRetrocederSW , codIniciarSW,tipoOperacionAvanzar,tipoOperacionRetroceder,tipoOperacionIniciar, tituloOperacionAvanzar,tituloOperacionRetroceder,tituloOperacionIniciar,nombreModuloAvanzar,nombreModuloRetroceder,nombreModuloIniciar,codTipoRetrocesoSW,params);

		} catch (TechnicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String getTitulo(int codOp,String [] params) {
    	String tit = "";
    	try {
			tit = DefinicionSWTramitacionDAO.getInstance().getTitulo(codOp, params);
		} catch (TechnicalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tit;
    }

    public Vector<CampoValueObject> getListaCampos(int codMunicipio, String codProcedimiento, boolean mostrarTodosTramites, int codTramite, String[] params)
    throws InternalErrorException {

        if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Comienzo getListaCampos");
        try {
            return DefinicionSWTramitacionDAO.getInstance().getListaCampos(codMunicipio,codProcedimiento, mostrarTodosTramites, codTramite, params);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage(), te);
            throw new InternalErrorException(te);
        } finally {
            if (m_Log.isDebugEnabled()) m_Log.debug("DefinicionSWTramitacionManager: Fin getListaCampos");
        }
    }

}
