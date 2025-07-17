package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.sge.persistence.manual.AlarmasExpedienteDAO;
import es.altia.agora.business.sge.persistence.manual.InteresadosDAO;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAOAuxiliar;
import es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesRelacionadosHistoricoVO;
import es.altia.util.StringUtils;
import es.altia.util.commons.DateOperations;

import java.sql.Date;
import java.util.*;

import es.altia.util.conexion.*;

import es.altia.util.jdbc.GeneralOperations;
import java.sql.*;


public class TramitacionManager{
  /**
    * Código que sigue el patrón de diseño <code>Singleton</code>
    * Los métodos de negocio gestionan que la persistencia sea manual o automática
    * Es protected, por lo que la única manera de instanciar esta clase es usando el factory method <code>getInstance</code>
    */

  private final static String ID_SERVICIO_POR_DEFECTO = "SGE";

  protected TramitacionManager() {
    // Fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        m_ConfigCommon = ConfigServiceHelper.getConfig("common");
    // Mensajes de error localizados
    // m_ConfigError = ConfigServiceHelper.getConfig("error");
    // Servicio de log

  }



    public Vector getListaUnidadesTramitadorasUsuario(UsuarioValueObject uVO, String[] params)
            throws TramitacionException {

        Vector v;

        m_Log.debug("getUnidadesTramitadorasUsuario");

        try {
            m_Log.debug("Usando persistencia manual");
            v = TramitacionDAO.getInstance().getListaUnidadesTramitadorasUsuario(uVO, params);
            m_Log.debug("getUnidadesTramitadorasUsuario");

        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());

        }
        return v;
    }


    public Vector getExpedientesPendientes(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,String columna,String nombreColumna,String tipoOrden)
            throws TramitacionException { 

        Vector v;

        m_Log.debug("getExpedientesPendientes");

        try {
            m_Log.debug("Usando persistencia manual");
            v = TramitacionDAO.getInstance().getExpedientesPendientes(uVO, tVO, params,columna,nombreColumna,tipoOrden);

        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }
        
        //Se procesa el listado de expedientes para comprobar si se han añadido caracteres unicodes 
        procesarListadoExpedientesPendientes(v);
        
        m_Log.debug("getExpedientesPendientes");
        return v;
    }
    
    
    /**
     * Metodo que procesa la lista de expedientes y busca si existen 
     * caracteres unicodes en los campos de titular y descripcion. 
     * Si existen se sustituyen por caracteres normales
     * @param vector 
     */
    private void procesarListadoExpedientesPendientes (Vector vector) {
        
        //Se comprueba si el vector se encuentra vacio
        if (vector == null || vector.size()==0) {
            return;
        }
        
        
        //Se recorrec el vector para comprobar si existen valores unicodes y se cambian por string
        for (int i=0; i<vector.size(); i++) {
            TramitacionValueObject tVO = (TramitacionValueObject) vector.elementAt(i);
            tVO.setTitular(StringUtils.convertirUnicodeAString(tVO.getTitular()));
            tVO.setDescProcedimiento(StringUtils.convertirUnicodeAString(tVO.getDescProcedimiento()));
        }
    }
    
    public Vector getExpedientesPendientes(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params)
            throws TramitacionException {

        Vector v;

        m_Log.debug("getExpedientesPendientes SOLO EXP");

        try {
            m_Log.debug("Usando persistencia manual"); 
            v = TramitacionDAO.getInstance().getExpedientesPendientes(uVO, tVO, params);

        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }
        m_Log.debug("getExpedientesPendientes");
        return v;
    }


    public Vector getExpedientesPendientesFiltrados(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,String columna,String tipoOrden, int filtro)
            throws TramitacionException {

        Vector v;

        m_Log.debug("getExpedientesPendientesFiltrados");

        try {
            m_Log.debug("Usando persistencia manual");
            v = TramitacionDAO.getInstance().getExpedientesPendientesFiltrados(uVO, tVO, params,columna,tipoOrden,filtro);

        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }
        m_Log.debug("getExpedientesPendientesFiltrados");
        return v;
    }


    public Vector getListaProcedimientosUsuario(UsuarioValueObject uVO, int sinTipoProcs, String[] params)
            throws TramitacionException {

        Vector v;

        m_Log.debug("getListaProcedimientosUsuario");

        try {
            m_Log.debug("Usando persistencia manual");
            v = TramitacionDAO.getInstance().getListaProcedimientosUsuario(uVO, sinTipoProcs, params);
            m_Log.debug("getListaProcedimientosUsuario");
        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }

        m_Log.debug("getListaProcedimientosUsuario");
        return v;
    }
    
    
     public Vector getListaProcedimientosUsuario2(UsuarioValueObject uVO, int sinTipoProcs, String[] params)
            throws TramitacionException {

        Vector v;

        m_Log.debug("getListaProcedimientosUsuario");

        try {
             
            m_Log.debug("Usando persistencia manual");
            v = TramitacionDAO.getInstance().getListaProcedimientosUsuario2(uVO, sinTipoProcs, params);
            m_Log.debug("getListaProcedimientosUsuario");
        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }

        m_Log.debug("getListaProcedimientosUsuario");
        return v;
    }
     
     
    public Vector getListaUORs_usuarioPorProc(UsuarioValueObject uVO,  String codigoOrganizacion ,String codigoProcedimiento, String[] params)
            throws TramitacionException,TechnicalException {

        Vector v;
        
        
         m_Log.debug("getListaUORs_usuarioPorProc");
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        
        try {
            m_Log.debug("Usando persistencia manual");
            con = oad.getConnection();
            v = TramitacionDAO.getInstance().getListaUORs_usuarioPorProc(oad, con, codigoOrganizacion,codigoProcedimiento,uVO);
            
            m_Log.debug("getNumeroExpediente");

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        
        } catch (BDException bde) {
            m_Log.error("JDBC Technical problem " + bde.getMessage());
            throw new TechnicalException("Problema técnico de JDBC " + bde.getMessage());
        } finally {
            m_Log.debug("getNumeroExpediente");
            SigpGeneralOperations.devolverConexion(oad, con);
        }

        m_Log.debug("getListaProcedimientosUsuario");

        

        m_Log.debug("getListaProcedimientosUsuario");
        return v;
    }
    
   

    public Vector getListaProcedimientosSoloUsuario(UsuarioValueObject uVO, String[] params)
            throws TramitacionException {

        Vector v;

        m_Log.debug("getListaProcedimientosSoloUsuario");

        try {
            m_Log.debug("Usando persistencia manual");
            v = TramitacionDAO.getInstance().getListaProcedimientosSoloUsuario(uVO, params);
            m_Log.debug("getListaProcedimientosUsuario");
        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());

        }
        m_Log.debug("getListaProcedimientosSoloUsuario");
        return v;
    }
/* ********************************************************************************************************
     * Recoge los campos de listado de expedientes que existen en la bbdd
* ****************************************************************************************************** */
  public Vector getCamposListado(int codListado, String[] params) throws TramitacionException {

         try {
            m_Log.debug("Usando persistencia manual");
            return TramitacionDAO.getInstance().getCamposListado(codListado, params);

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + ce.getMessage());
        }
    }
    public Vector getListaProcedimientos(UsuarioValueObject uVO, String[] params)
            throws TramitacionException {

        Vector v;

        m_Log.debug("getListaProcedimientos");

        try {
            m_Log.debug("Usando persistencia manual");
            v = TramitacionDAO.getInstance().getListaProcedimientos(uVO, params);
            m_Log.debug("getListaProcedimientos");
        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());

        }
        m_Log.debug("getListaProcedimientos");
        return v;
    }

    public Vector getListaProcedimientosUOR(String UOR, String[] params)
            throws TramitacionException {

        Vector v;

        m_Log.debug("getListaProcedimientosUOR");

        try {
            m_Log.debug("Usando persistencia manual");
            v = TramitacionDAO.getInstance().getListaProcedimientosUOR(UOR, params);
            m_Log.debug("getListaProcedimientosUOR");
        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());

        }
        m_Log.debug("getListaProcedimientosUOR");
        return v;
    }

    public Vector getListaProcedimientosVigentes(String[] params)
            throws TramitacionException {

        Vector v;
        m_Log.debug("getListaProcedimientosVigentes");

        try {
            m_Log.info("Usando persistencia manual");
            v = TramitacionDAO.getInstance().getListaProcedimientosVigentes(params);
            m_Log.debug("getListaProcedimientosVigentes");
        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());
        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }
        m_Log.debug("getListaProcedimientosVigentes");
        return v;
    }


  public void getNuevoExpediente(UsuarioValueObject uvo, TramitacionValueObject tvo,  String[] params)
  throws TramitacionException {

    m_Log.debug("getNuevoExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      TramitacionDAO.getInstance().getNuevoExpediente(uvo, tvo,params);
      m_Log.debug("getNuevoExpediente");
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    }
    finally {
      m_Log.debug("getNuevoExpediente");
    }
  }

    public void getNumeroExpediente(TramitacionValueObject tvo, String[] params) throws TramitacionException, TechnicalException {

        m_Log.debug("getNumeroExpediente");
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        
        try {
            m_Log.debug("Usando persistencia manual");
            con = oad.getConnection();
            TramitacionDAO.getInstance().getNumeroExpediente(tvo, con);
            m_Log.debug("getNumeroExpediente");

        } catch (BDException bde) {
            m_Log.error("JDBC Technical problem " + bde.getMessage());
            throw new TechnicalException("Problema técnico de JDBC " + bde.getMessage());
        } finally {
            m_Log.debug("getNumeroExpediente");
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }
  
  public void getNumeroExpediente(TramitacionValueObject tvo,  Connection con)
  throws TramitacionException {

    m_Log.debug("getNumeroExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      TramitacionDAO.getInstance().getNumeroExpediente(tvo, con);
      m_Log.debug("getNumeroExpediente");
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } 
    finally {
      m_Log.debug("getNumeroExpediente");
    }
  }

  public int localizaExpediente(TramitacionValueObject tvo,  String[] params)
  throws TramitacionException {

    int i = 0;
    m_Log.debug("localizaExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      i = TramitacionDAO.getInstance().localizaExpediente(tvo,params);
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch (BDException e) {
      m_Log.error("JDBC Technical problem " + e.getMessage());
      e.printStackTrace();
    }
    finally {
      m_Log.debug("localizaExpediente");
    }
    return i;
  }
  public Vector localizaExpedienteByNum(TramitacionValueObject tvo, String codUsuario,String codOrganizacion, String[] params)
  throws TramitacionException {

    Vector res=new Vector();
    m_Log.debug("localizaExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      res = TramitacionDAO.getInstance().localizaExpedienteByNum(tvo,codUsuario,codOrganizacion, params);
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch (BDException e) {
      m_Log.error("JDBC Technical problem " + e.getMessage());
      e.printStackTrace();
    }
    finally {
      m_Log.debug("localizaExpediente");
    }
    return res;
  }

  public int insertarExpediente(Connection con,TramitacionValueObject tvo,int ori,String top)
  throws TramitacionException {

    int i = 0;
    m_Log.debug("insertarExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      i = TramitacionDAO.getInstance().insertarExpediente(con,tvo,ori,top);
      m_Log.debug("insertarExpediente");
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch (BDException e) {
      m_Log.error("JDBC Technical problem " + e.getMessage());
      e.printStackTrace();
    }
    finally {
      m_Log.debug("insertarExpediente");
    }
    return i;
  }

  public int insertarExpediente(TramitacionValueObject tvo,  String[] params,int ori,String top)
  throws TramitacionException {

    int i = 0;
    m_Log.debug("insertarExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      i = TramitacionDAO.getInstance().insertarExpediente(tvo,params,ori,top);
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch (BDException e) {
      m_Log.error("JDBC Technical problem " + e.getMessage());
      e.printStackTrace();
    }
    finally {
      m_Log.debug("insertarExpediente");
    }
    return i;
  }

  public int setInteresadosReg2Exp(TramitacionValueObject tramVO,RegistroValueObject elRegistroESVO,GeneralValueObject gVO,Vector tercerosSoloEnRegistro,  String[] params)
  throws TramitacionException {

   int i = 0;
    m_Log.debug("insertarExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      i = TramitacionDAO.getInstance().setInteresadosReg2Exp(tramVO,elRegistroESVO,gVO,tercerosSoloEnRegistro,params);
     
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch (BDException e) {
      m_Log.error("JDBC Technical problem " + e.getMessage());
      e.printStackTrace();
    }
    finally {
      m_Log.debug("insertarExpediente");
    }
    return i;
  }

   public int setInteresadoExternoReg2Exp(TramitacionValueObject tramVO,MantAnotacionRegistroForm registroForm,GeneralValueObject gVO,UsuarioValueObject usuarioVO,  String[] params)
  throws TramitacionException {

   int i = 0;
    m_Log.debug("insertarExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      i = TramitacionDAO.getInstance().setInteresadoExternoReg2Exp(tramVO,registroForm,gVO,usuarioVO,params);

    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch (BDException e) {
      m_Log.error("JDBC Technical problem " + e.getMessage());
      e.printStackTrace();
    }
    finally {
      m_Log.debug("insertarExpediente");
    }
    return i;
  }


  public int localizaExpediente(Connection con,TramitacionValueObject tvo)
  throws TramitacionException {

    int i = 0;
    m_Log.debug("localizaExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      i = TramitacionDAO.getInstance().localizaExpediente(con,tvo);
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch (BDException e) {
      m_Log.error("JDBC Technical problem " + e.getMessage());
      e.printStackTrace();
    }
    finally {
      m_Log.debug("localizaExpediente");
    }
    return i;
  }
  
  public int localizaExpedienteDesdeRegistro(Connection con,TramitacionValueObject tvo)
  throws TramitacionException {

    int i = 0;
    m_Log.debug("localizaExpedienteDesdeRegistro");
    try {
      m_Log.debug("Usando persistencia manual");
      i = TramitacionDAO.getInstance().localizaExpedienteDesdeRegistro(con,tvo);
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch (BDException e) {
      m_Log.error("JDBC Technical problem " + e.getMessage());
      e.printStackTrace();
    }
    finally {
      m_Log.debug("localizaExpedienteDesdeRegistro");
    }
    return i;
  }
  
  public String comprobarExpediente(TramitacionValueObject tvo,String[] params)
  throws TramitacionException {

    String resultado = "";
    m_Log.debug("comprobarExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      resultado = TramitacionDAO.getInstance().comprobarExpediente(tvo,params);
    } catch (TechnicalException tecE) {
      m_Log.error("JDBC Technical problem " + tecE.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

    } catch (TramitacionException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch (BDException e) {
      m_Log.error("JDBC Technical problem " + e.getMessage());
      e.printStackTrace();
    }
    finally {
      m_Log.debug("comprobarExpediente");
    }
    return resultado;
  }
  

  /**
    * Factory method para el <code>Singelton</code>.
     *
    * @return La unica instancia de SelectManager
  */
  public static TramitacionManager getInstance() {
    if (instance == null) {
    // Sincronización para serializar (no multithread) las invocaciones a este metodo
            synchronized (TramitacionManager.class) {
      if (instance == null) {
        instance = new TramitacionManager();
      }
    }
    }
    return instance;
  }

  private static TramitacionManager instance = null; // Mi propia instancia

  /* Declaracion de servicios */
  protected static Config m_ConfigCommon;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion technical
  protected static Config m_ConfigError; // Para el fichero de mensajes de error localizados
  protected static Log m_Log =
          LogFactory.getLog(TramitacionManager.class.getName());

    private RegistroValueObject getInfoAsientoFromSGE(RegistroValueObject registroVO, String[] params)
            throws AnotacionRegistroException {

        registroVO = AnotacionRegistroManager.getInstance().getByPrimaryKey(registroVO,params);

        int domicInter = registroVO.getDomicInter();
        int codInter = registroVO.getCodInter();
        int numModInfInt = registroVO.getNumModInfInt();

        TercerosValueObject terceroVO = new TercerosValueObject();
        terceroVO.setIdDomicilio(String.valueOf(domicInter));
        terceroVO.setIdentificador(String.valueOf(codInter));
        terceroVO.setVersion(String.valueOf(numModInfInt));

        Vector tercerosEncontrados = TercerosManager.getInstance().getByHistorico(terceroVO, params);

        if(tercerosEncontrados.size() > 0 ) {
            terceroVO = (TercerosValueObject) tercerosEncontrados.firstElement();
            // Consigo los datos del tercero
            registroVO.setTipoDocInteresado(terceroVO.getTipoDocumento());
            registroVO.setDocumentoInteresado(terceroVO.getDocumento());
            String nombre = "";
            if (terceroVO.getNombre() != null ) {
                if ( !"".equals(terceroVO.getNombre().trim()) ) {
                    if (terceroVO.getApellido1() != null) {
                        if (!"".equals(terceroVO.getApellido1())) {
                            nombre = terceroVO.getPartApellido1() + " " + terceroVO.getApellido1() + " " +
                                    terceroVO.getPartApellido2() + " " + terceroVO.getApellido2() + "," +
                                    terceroVO.getNombre();
                        } else {
                            nombre = terceroVO.getNombre();
                        }
                    } else {
                        nombre = terceroVO.getNombre();
                    }
                }
            }
            registroVO.setNomCompletoInteresado(nombre);
            registroVO.setTlfInteresado(terceroVO.getTelefono());
            registroVO.setEmailInteresado(terceroVO.getEmail());
            Vector domicilios = terceroVO.getDomicilios();
            if (domicilios != null) {
                if (domicilios.size() > 0 ) {
                    DomicilioSimpleValueObject domic = (DomicilioSimpleValueObject) domicilios.firstElement();
                    registroVO.setProvInteresado(domic.getProvincia());
                    registroVO.setMunInteresado(domic.getMunicipio());
                    String descTVia = domic.getTipoVia();
                    String numDesde = domic.getNumDesde();
                    String letraDesde = domic.getLetraDesde();
                    String numHasta = domic.getNumHasta();
                    String letraHasta = domic.getLetraHasta();
                    String bloque = domic.getBloque();
                    String portal = domic.getPortal();
                    String escal = domic.getEscalera();
                    String planta = domic.getPlanta();
                    String puerta = domic.getPuerta();
                    String dom = domic.getDomicilio();
                    String domicilio = "";
                    domicilio = (!descTVia.equals("")) ? domicilio + descTVia + " " : domicilio;
                    domicilio = (!dom.equals("")) ? domicilio + " " + dom : domicilio;
                    domicilio = (!numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                    domicilio = (!letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                    domicilio = (!numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                    domicilio = (!letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                    domicilio = (!bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                    domicilio = (!portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                    domicilio = (!escal.equals("")) ? domicilio + " Esc. " + escal : domicilio;
                    domicilio = (!planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                    domicilio = (!puerta.equals("")) ? domicilio + puerta : domicilio;
                    registroVO.setDomCompletoInteresado(domicilio);
                    registroVO.setPoblInteresado(domic.getBarriada());
                    registroVO.setCpInteresado(domic.getCodigoPostal());
                }
            }

        }
        return registroVO;
    }

    
    /**
     * Comprueba si un usuario tiene permiso sobre la unidad organizativa de inicio de un expediente
     * @param idOrganizacion: Id de la organización
     * @param idUsuario: Id del usuario
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la BBDD
     * @return Un boolean
     */
    public boolean tienePermisoSobreUnidadInicio(int idOrganizacion,int idUsuario,String codProcedimiento,String[] params) throws TechnicalException
    {
        boolean exito = false;
        TramitacionDAO tDAO = TramitacionDAO.getInstance();
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection connection = null;
        try{
            m_Log.debug("tienePermisoSobreUnidadInicio ------------ idOrganizacion: " + idOrganizacion);
            m_Log.debug("tienePermisoSobreUnidadInicio ------------ idUsuario: " + idUsuario);
            m_Log.debug("tienePermisoSobreUnidadInicio ------------ codProcedimiento: " + codProcedimiento);
            m_Log.debug("tienePermisoSobreUnidadInicio ------------ params " + params);
            connection = oad.getConnection();
            exito = tDAO.tienePermisoSobreUnidadInicio(idOrganizacion, idUsuario,codProcedimiento, connection);            
        }
        catch(BDException e){
            e.printStackTrace();
            m_Log.error("TramitacionManager - BDException.tienePermisoSobreUnidadInicio: " + e.getMessage());
        }
        catch(TramitacionException e){
            e.printStackTrace();
            m_Log.error("TramitacionManager - TramitacionException.tienePermisoSobreUnidadInicio: " + e.getMessage());
        }
        finally{
            try{
                GeneralOperations.closeConnection(connection);
            }
            catch(Exception e){
                m_Log.error("TramitacionManager - Exception.tienePermisoSobreUnidadInicio: " + e.getMessage());
            }
        }
        
        return exito;
    }
    
    
    /**
     * Comprueba si un usuario tiene permiso sobre la unidad organizativa de inicio de un expediente
     * @param codExpediente: Código del expediente
     * @param idUsuario: Id del usuario
     * @param idOrganizacion: Id de la organizaciónCódigo del procedimiento
     * @param params: Parámetros de conexión a la BBDD
     * @return Un boolean
     */
    public boolean tienePermisoUnidadesTramitadoras(String codExpediente, int idUsuario,int idOrganizacion,String[] params)
    {
        boolean exito = false;
        TramitacionDAO tDAO = TramitacionDAO.getInstance();
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection connection = null;
        try{
            m_Log.debug("tienePermisoSobreUnidadInicio ------------ idOrganizacion: " + idOrganizacion);
            m_Log.debug("tienePermisoSobreUnidadInicio ------------ idUsuario: " + idUsuario);
            m_Log.debug("tienePermisoSobreUnidadInicio ------------ codProcedimiento: " + codExpediente);
            m_Log.debug("tienePermisoSobreUnidadInicio ------------ params " + params);
            connection = oad.getConnection();
            exito = tDAO.tienePermisoUnidadesTramitadoras(codExpediente,idUsuario,idOrganizacion,connection);
        }
        catch(BDException e){
            e.printStackTrace();
            m_Log.error("TramitacionManager - BDException.usuarioTienePermisoUnidadesTramitadoras: " + e.getMessage());
        }
        catch(TramitacionException e){
            e.printStackTrace();
            m_Log.error("TramitacionManager - TramitacionException.usuarioTienePermisoUnidadesTramitadoras: " + e.getMessage());
        }
        catch(TechnicalException e){
            e.printStackTrace();
            m_Log.error("TramitacionManager - TechnicalException.usuarioTienePermisoUnidadesTramitadoras: " + e.getMessage());
        }
        finally{
            try{
               GeneralOperations.closeConnection(connection);
            }
            catch(Exception e){
                m_Log.error("TramitacionManager - Exception.tienePermisoSobreUnidadInicio: " + e.getMessage());
            }
        }
        return exito;
    }
    

    /** 
     * Recupera el municipio asociado a un determinado expediente
     * @param codExpediente: Código del expediente de la forma 200?/XXXX/********
     * @param params
     * @return String
     */
     public String getCodMunicipio(String codExpediente,String[] params)
     {
         String mun = null;
         Connection connection = null;
         try{
            TramitacionDAO tDAO = TramitacionDAO.getInstance();
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            connection = oad.getConnection();
            mun = tDAO.getCodMunicipio(codExpediente,connection);
         }
         catch(Exception e){
             e.printStackTrace();
             m_Log.error("TramitacionManager - getCodMunicipio " + e.getMessage());
        }
         finally{
             try{
                GeneralOperations.closeConnection(connection);
            }
            catch(Exception e){
                m_Log.error("TramitacionManager - Exception.tienePermisoSobreUnidadInicio: " + e.getMessage());
            }
         }
         
        return mun;
     }


     /**
     * REcupera las unidades tramitadoras del trámite de inicio de un expediente
     * @param codigoProc: Código del procedimiento
     * @param connection: Conexión a la base de datos
     * @return Colección de objetos UORDTO
     */
     public int getTipoUnidadTramitadoraTramiteInicio(String codPro,String[] params)
     {
        m_Log.debug("TramitacionManager --> getTipoUnidadTramitadoraTramiteInicio Inicio");     
        Connection connection = null;
        int tipo = -1;
        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            connection = oad.getConnection();
            tipo  = TramitacionDAO.getInstance().getTipoUnidadTramitadoraTramiteInicio(codPro,connection);
        } catch (Exception exc) {
            m_Log.error("ERROR - " + exc.getMessage());
        }
        finally{
             try{
                GeneralOperations.closeConnection(connection);
            }
            catch(Exception e){
                m_Log.error("Exception TramitacionManager.getTipoUnidadTramitadoraTramiteInicio: " + e.getMessage());
            }
        }

        m_Log.debug("TramitacionManager --> Fin getTipoUnidadTramitadoraTramiteInicio");
        return tipo;
     }


    /**
     * REcupera las unidades tramitadoras del trámite de inicio de un expediente
     * @param codigoProc: Código del procedimiento
     * @param connection: Conexión a la base de datos
     * @return Colección de objetos UORDTO
     */
     public Vector<UORDTO> getUnidadesTramitadorasTramiteInicio(String codPro,String[] params)
     {
        m_Log.debug("DefinicionProcedimientosManager --> Inicio getUnidadesTramitadorasTramiteInicio");
        Vector<UORDTO> unidadesTramitadoras = new Vector<UORDTO>();
        Connection connection = null;

        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            connection = oad.getConnection();
            unidadesTramitadoras = TramitacionDAO.getInstance().getUnidadesTramitadorasTramiteInicio(codPro, connection, oad);
        } catch (Exception exc) {
            m_Log.error("ERROR - " + exc.getMessage());
        }
        finally{
             try{
                GeneralOperations.closeConnection(connection);
            }
            catch(Exception e){
                m_Log.error("Exception TramitacionManager.getUnidadesTramitadorasTramiteInicio: " + e.getMessage());
            }
        }

        m_Log.debug("DefinicionProcedimientosManager --> Fin obtenerCamposDesplegables");
        return unidadesTramitadoras;
     }


    /**
     * Recupera las unidades tramitadoras del trámite de inicio de un expediente
     * @param connection: Conexión a la base de datos
     * @return Colección de objetos UORDTO
     */
     public Vector<UORDTO> getTodasUnidadesOrganizativas(String[] params)
     {
        m_Log.debug("TramitacionManager --> Inicio getTodasUnidadesOrganizativas");
        Vector<UORDTO> unidades = new Vector<UORDTO>();
        Connection connection = null;

        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            connection = oad.getConnection();
            unidades = TramitacionDAO.getInstance().getTodasUnidadesOrganizativas(connection,params[6]);
        } catch (Exception exc) {
            m_Log.error("ERROR - " + exc.getMessage());
        }
        finally{
             try{
                GeneralOperations.closeConnection(connection);
            }
            catch(Exception e){
                m_Log.error("Exception TramitacionManager.getTodasUnidadesOrganizativas: " + e.getMessage());
            }
        }

        m_Log.debug("TramitacionManager.getTodasUnidadesOrganizativas");
        return unidades;
     }



    /**
     * Recupera el tipo de unidad de inicio de un trámite
     * @param codigoProc: Código del procedimiento
     * @param codTramite: Código del trámite
     * @param connection: Conexión a la base de datos
     * @return int
     */
     public int getTipoUnidadTramitadoraTramite(String codPro,String codTramite,int codOrganizacion,String[] params)
     {
        m_Log.debug("TramitacionManager --> getTipoUnidadTramitadoraTramite init");
        Connection connection = null;
        int tipo = -1;
        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            connection = oad.getConnection();
            tipo  = TramitacionDAO.getInstance().getTipoUnidadTramitadoraTramite(codPro,codTramite,codOrganizacion,connection);
        } catch (Exception exc) {
            m_Log.error("ERROR - " + exc.getMessage());
        }
        finally{
             try{
                GeneralOperations.closeConnection(connection);
            }
            catch(Exception e){
                m_Log.error("Exception TramitacionManager.getTipoUnidadTramitadoraTramiteInicio: " + e.getMessage());
            }
        }

        m_Log.debug("TramitacionManager --> Fin getTipoUnidadTramitadoraTramiteInicio");
        return tipo;
     }



    /**
     *
     * Obtiene la fecha de fin de un expediente calculando a partir de la fecha de inicio del mismo, nº de unidades y tipo de plazo
     * @param fechaInicioExpediente: Fecha inicio
     * @param unidades: Número
     * @param tipoPlazo:En función del valor que tome se obtiene la fecha de fin del expediente. Los valores a tomar son:
     *                              1: Días naturales, 2: Días hábiles, 3: Meses
     * @param festivos: Colección de Calendar que representan a los días festivos correspondientes al mismo ejercicio al que pertenece el expediente
     *
     * @return Calendar
     */
    public Calendar calcularFechaFinExpediente(Calendar fechaInicioExpediente,int unidades,int tipoPlazo,ArrayList<Calendar> festivos){
        return TramitacionDAO.getInstance().calcularFechaFinExpediente(fechaInicioExpediente, unidades, tipoPlazo, festivos);
    }

/**
 * Comprueba si un expediente se encuentra actualmente cerca de su fin de plazo o bien fuera de plazo
 * @param porcentaje: Porcentaje de la fecha de fin a partir del cual se comienza a avisar
 * @param fechaInicio: Fecha de inicio del expediente
 * @param fechaFin: Fecha de fin de expediente
 * @return String que toma los siguientes valores:
 *                  "si": Expediente cercano a fin de plazo
 *                  "no": Fuera de plazo
 *                  null: No verifica ninguna de las condiciones anteriores
 */
     public String calculoExpedienteCercaFinPlazo(int porcentaje, Calendar fechaInicio, Calendar fechaFin) {
        return TramitacionDAO.getInstance().calculoExpedienteCercaFinPlazo(porcentaje, fechaInicio, fechaFin);
    }


    public ArrayList<Calendar> getFestivos(int ejercicio,Connection con) throws SQLException {
        return TramitacionDAO.getInstance().getFestivos(ejercicio, con);
    }
    
     /**
      * Recupera los expedientes pertenecientes a una relación
      * indicado en el parámetro filtro
      * @param codMunicipio: Código del municipio
      * @param codProcedimiento: Código del procedimiento
      * @param numRelacion: Nº de la relación
      * @param con: Conexión a la BD
      * @return ArrayList de String con los número de expedientes pertenecientes a la relación
      * @throws es.altia.agora.business.sge.exception.TramitacionException
      * @throws es.altia.common.exception.TechnicalException
      */
      public ArrayList<String> getExpedientesRelacion(String codMunicipio,String codProcedimiento,String numRelacion,String ejercicio,String[] params)
            throws TramitacionException, TechnicalException {
          ArrayList<String> exps = new ArrayList<String>();
          Connection con = null;
          try{
              AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
              con = adapt.getConnection();
              exps = TramitacionDAO.getInstance().getExpedientesRelacion(codMunicipio, codProcedimiento, numRelacion, ejercicio, con);

          }catch(Exception e){
                e.printStackTrace();
                m_Log.error(e.getMessage());
          }finally{
              try{
                   if(con!=null) con.close();
              }catch(SQLException e){
                  e.printStackTrace();
              }
          }

          return exps;
      }


     /**
      * Marca o desmarca como destacado un expediente
      * @param ejercicio: Ejercicio del expediente
      * @param numeroExp: Numero del expediente que se quiere marcar/desmarcar
      * @param params: Parametros de conexion a la base de datos
      * @param opcion: Opreacion que se va realizar (1:marcar expediente como destacado, 2:marcar expediente como no destacado)
      * @return int con el numero de filas actualizadas
      */
      public int establecerExpedienteDestacado(String ejercicio, String numeroExp, String[] params, int opcion){

        m_Log.debug("TramitacionManager --> establecerExpedienteDestacado opcion = " + opcion);
        Connection connection = null;
        int resultado = -1;
        try {
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            connection = oad.getConnection();
            resultado  = TramitacionDAO.getInstance().establecerExpedienteDestacado(ejercicio, numeroExp, connection, opcion);

        } catch (Exception exc) {
            m_Log.error("ERROR - " + exc.getMessage());
            exc.printStackTrace();
        }
        finally{
             try{
                GeneralOperations.closeConnection(connection);
            }
            catch(Exception e){
                m_Log.error("Exception TramitacionManager.establecerExpedienteDestacado: " + e.getMessage());
                e.printStackTrace();
            }
        }
        m_Log.debug("TramitacionManager --> Fin establecerExpedienteDestacado opcion=" + opcion);
        return resultado;
      }




     /**
      * Recupera el nº de expediente relacionado externo que tiene como un determinado origen
      * @param tvo: TramitacionValueObject
      * @param origen: Origen del expediente relacionado
      * @param con: Conexión a la base de datos
      * @throws es.altia.agora.business.sge.exception.TramitacionException si ocurre algún error
      */
    public void getNumeroExpedienteRelacionadoExterno(TramitacionValueObject tvo,  String origen, Connection con)  throws TramitacionException {

        try{

            TramitacionDAO.getInstance().getNumeroExpedienteRelacionadoExterno(tvo,origen,con);
    
        }catch (TechnicalException tecE) {
                tecE.printStackTrace();
                throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
                te.printStackTrace();
                throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }
        
    }
    
    public  ArrayList<ExpedientesRelacionadosHistoricoVO> getExpedientesAsociados(String uni_registro, String ejercicio, String tipo_reg , String[] params)
            throws TramitacionException {
        if(m_Log.isDebugEnabled()) m_Log.debug("getExpedientesAsociados() : BEGIN");
        Connection con = null;        
        ArrayList<ExpedientesRelacionadosHistoricoVO> retorno = new ArrayList<ExpedientesRelacionadosHistoricoVO>();
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            retorno = TramitacionDAOAuxiliar.getInstance().recuperarExpAsociados(con, uni_registro,ejercicio,tipo_reg);
        }catch(Exception e){
            m_Log.error("Se ha producido un error recuperando los expedientes asociados " + e.getMessage());
        }finally{
            try{
                if(con!=null) 
                    con.close();
            }catch(SQLException e){
                m_Log.error("Se ha producido un error cerrando la conexion " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("getExpedientesAsociados() : END");
        return retorno;
    }//getExpedientesAsociados
    
    public  ArrayList<ExpedientesRelacionadosHistoricoVO> getExpedientesAsociados(String departamento, String ejercicio, String tipo_reg ,String uni_registro , String[] params)
            throws TramitacionException {
        if(m_Log.isDebugEnabled()) m_Log.debug("getExpedientesAsociados() : BEGIN");
        Connection con = null;        
        ArrayList<ExpedientesRelacionadosHistoricoVO> retorno = new ArrayList<ExpedientesRelacionadosHistoricoVO>();
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            retorno = TramitacionDAOAuxiliar.getInstance().recuperarExpAsociados(con, departamento,ejercicio,tipo_reg,uni_registro);
        }catch(Exception e){
            m_Log.error("Se ha producido un error recuperando los expedientes asociados " + e.getMessage());
        }finally{ 
            try{
                if(con!=null) 
                    con.close();
            }catch(SQLException e){
                m_Log.error("Se ha producido un error cerrando la conexion " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("getExpedientesAsociados() : END");
        return retorno;
    }//getExpedientesAsociados
    
    public  String getFechaPresentacion(String departamento,String codUni, String ejercicio, String tipo_reg , String[] params)
            throws TramitacionException {
        
        Connection con = null;        
        String retorno = "";
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();            
            retorno = TramitacionDAOAuxiliar.getInstance().recuperaFechaPresentacion(con, departamento,codUni,ejercicio,tipo_reg);         
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) 
                    con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

         return retorno;
    }
    public  String getClasePluginExpedRelacHistorico(String procedimiento, String[] params)
            throws TramitacionException {
        
        Connection con = null;        
        String retorno = "";
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();            
            retorno = TramitacionDAO.getInstance().recuperaClasePluginExpedRelacHistorico(con, procedimiento);         
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) 
                    con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

         return retorno;
    }
    
    /********************* PRUEBA *********************************/
    public int contarNumeroExpedientesPendientes(UsuarioValueObject uVO, TramitacionValueObject tVO,String[] params) throws TramitacionException{
        int numero = 0;
        
        m_Log.debug("contarNumeroExpedientesPendientes SOLO EXP");

        try {            
            numero = TramitacionDAOAuxiliar.getInstance().contarNumeroExpedientesPendientes(uVO, tVO, params);
            m_Log.debug("Usando persistencia manual");
            
        }  catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }
        catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }
        m_Log.debug("contarNumeroExpedientesPendientes");
        return numero;
    }
    
  
    
   public int contarExpedientesPendientesFiltrados(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params, int filtro)
            throws TramitacionException {

       int numero = 0;

        m_Log.debug("contarExpedientesPendientesFiltrados =====>");

        try {
            m_Log.debug("Usando persistencia manual");
            numero = TramitacionDAO.getInstance().contarExpedientesPendientesFiltrados(uVO, tVO, params, filtro);

        } catch (TechnicalException tecE) {
            m_Log.error("JDBC Technical problem " + tecE.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + tecE.getMessage());

        } catch (TramitacionException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        }
        
        m_Log.debug("contarExpedientesPendientesFiltrados");
        return numero;
    }
    
    
    public GeneralValueObject recuperarDatosExpediente(int codOrganizacion, int ejercicio, String numExpediente, String codProcedimiento,  AdaptadorSQLBD adapt, Connection con) throws SQLException{
       GeneralValueObject datos = new GeneralValueObject();
       ArrayList<String> tramites = null;
       ArrayList<String> interesados = null;
       ArrayList<String> camposAlarmas = null; 
       
       m_Log.debug("TramitacionManager::recuperarDatosExpediente()");
      try {
        m_Log.debug("Va a recuperar los trámites abiertos");
        tramites = TramitacionDAO.getInstance().recuperarTramitesAbiertos(codOrganizacion, ejercicio, numExpediente, con);
        m_Log.debug("Se han recuperado los trámites abiertos");
        
        m_Log.debug("Va a recuperar los interesados");
        interesados = TramitacionDAO.getInstance().recuperarInteresados(codOrganizacion, ejercicio, numExpediente, codProcedimiento, con);
        m_Log.debug("Se han recuperado los interesados");
        
         m_Log.debug("Va a recuperar las alarmas vencidas");
        camposAlarmas = AlarmasExpedienteDAO.getInstance().getNombresAlarmasVencidasExpediente(codOrganizacion, 	
                ejercicio, numExpediente, codProcedimiento, adapt, con);	
        m_Log.debug("Se han recuperado las alarmas vencidas");
        
        datos.setAtributo("tramites", tramites);
        datos.setAtributo("interesados", interesados);
        datos.setAtributo("camposAlarmas", camposAlarmas);
      } catch (SQLException ex) {
          m_Log.error("Error al obtener los trámites abiertos y/o interesados del expediente.");
          ex.printStackTrace();
          throw ex;
      }
      
      return datos;
   }
    /********************* PRUEBA ***********************************/
    
   public ArrayList<GeneralValueObject> recuperarExpedientesInteresado(ArrayList<GeneralValueObject> interesados, String filtroProc, String[] params){
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<GeneralValueObject> listado = new ArrayList<GeneralValueObject>();
        ArrayList<String> numeros = new ArrayList<String>();
        TercerosValueObject tercero = null;
        
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            for(GeneralValueObject gVO : interesados){
                ArrayList<GeneralValueObject> listaAux = new ArrayList<GeneralValueObject>();
                tercero = new TercerosValueObject();
                tercero.setIdentificador((String) gVO.getAtributo("codigoTercero"));
                tercero.setVersion((String) gVO.getAtributo("versionTercero"));
                
                //Se comprueba si el filtro del procedimiento se encuentra nulo. Si lo esta significa que lo invoca la opcion de adjuntarExpediente
                // en caso contrario de la hayExpedientesIntYProc y se invocará al DAO con parametros de entrada distintos
                if (filtroProc == null || "".equals(filtroProc.trim())) {
                    tercero.setDocumento((String) gVO.getAtributo("doc"));
                    listaAux = InteresadosDAO.getInstance().getListaExpedientesInteresado(tercero, filtroProc, true, false, con);
                } else {
                    tercero.setDocumento((String) gVO.getAtributo("documentoTercero"));
                    listaAux = InteresadosDAO.getInstance().getListaExpedientesInteresado(tercero, filtroProc, true, false, con);
                }
               
	
                if(listado.size()>0){	
                    for(GeneralValueObject gvoFinal : listado)	
                        numeros.add((String) gvoFinal.getAtributo("numExpediente"));	
                    	
                    for(GeneralValueObject gvo : listaAux){	
                        String numExp = (String) gvo.getAtributo("numExpediente");	
                        if(!numeros.contains(numExp))	
                            listado.add(gvo);
                    }
                } else listado.addAll(listaAux);
            }
             // Ordenamos por fecha de inicio	
            Collections.sort(listado, new Comparator() {	
                @Override	
                public int compare(Object o1, Object o2) {	
                    int resultado;	
                    GeneralValueObject gvo1 = (GeneralValueObject) o1;	
                    GeneralValueObject gvo2 = (GeneralValueObject) o2;	
                    	
                    Calendar fecha1 = DateOperations.toCalendar((Date) gvo1.getAtributo("fechaInicio"));	
                    Calendar fecha2 = DateOperations.toCalendar((Date) gvo2.getAtributo("fechaInicio"));	
                    if(fecha2.before(fecha1)) resultado = -1;	
                    else if(fecha2.after(fecha1)) resultado = 1;	
                    else resultado = 0;	
                    return new Integer(resultado);	
                }	
            });
        } catch (BDException bde) {
            m_Log.error("Error al obtener una conexión a la BBDD");
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex){
                m_Log.error("Error al liberar la conexión a la BBDD");
            }
        }
        return listado;
    }

    public ArrayList<GeneralValueObject> recuperarExpedientesRelacionadosInteresado(ArrayList<GeneralValueObject> interesados, List<String> filtroProc,
                                                                                    String[] params){
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<GeneralValueObject> listado = new ArrayList<GeneralValueObject>();
        ArrayList<String> numeros = new ArrayList<String>();
        TercerosValueObject tercero = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            for(GeneralValueObject gVO : interesados){
                ArrayList<GeneralValueObject> listaAux = new ArrayList<GeneralValueObject>();
                tercero = new TercerosValueObject();
                tercero.setIdentificador((String) gVO.getAtributo("codigoTercero"));
                tercero.setVersion((String) gVO.getAtributo("versionTercero"));

                //Se comprueba si el filtro del procedimiento se encuentra nulo. Si lo esta significa que lo invoca la opcion de adjuntarExpediente
                // en caso contrario de la hayExpedientesIntYProc y se invocará al DAO con parametros de entrada distintos
                if (filtroProc == null || "".equals(filtroProc)) {
                    tercero.setDocumento((String) gVO.getAtributo("doc"));
                    listaAux = InteresadosDAO.getInstance().getListaExpedientesRelacionadosInteresado(tercero, new ArrayList<String>(), true, false, con);
                } else {
                    tercero.setDocumento((String) gVO.getAtributo("documentoTercero"));
                    listaAux = InteresadosDAO.getInstance().getListaExpedientesRelacionadosInteresado(tercero, filtroProc, true, false, con);
                }


                if(listado.size()>0){
                    for(GeneralValueObject gvoFinal : listado)
                        numeros.add((String) gvoFinal.getAtributo("numExpediente"));

                    for(GeneralValueObject gvo : listaAux){
                        String numExp = (String) gvo.getAtributo("numExpediente");
                        if(!numeros.contains(numExp))
                            listado.add(gvo);
                    }
                } else listado.addAll(listaAux);
            }
            // Ordenamos por fecha de inicio
            Collections.sort(listado, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    int resultado;
                    GeneralValueObject gvo1 = (GeneralValueObject) o1;
                    GeneralValueObject gvo2 = (GeneralValueObject) o2;

                    Calendar fecha1 = DateOperations.toCalendar((Date) gvo1.getAtributo("fechaInicio"));
                    Calendar fecha2 = DateOperations.toCalendar((Date) gvo2.getAtributo("fechaInicio"));
                    if(fecha2.before(fecha1)) resultado = -1;
                    else if(fecha2.after(fecha1)) resultado = 1;
                    else resultado = 0;
                    return new Integer(resultado);
                }
            });
        } catch (BDException bde) {
            m_Log.error("Error al obtener una conexión a la BBDD");
        } finally {
            try {
                if(con!=null) con.close();
            } catch (SQLException ex){
                m_Log.error("Error al liberar la conexión a la BBDD");
            }
        }
        return listado;
    }

 }