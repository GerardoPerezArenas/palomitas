// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.sql.SQLException;

import es.altia.common.service.config.*;
import es.altia.common.exception.TechnicalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.util.*;
import es.altia.agora.business.terceros.*;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.CodPostalesDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.ViasDAO;
import es.altia.agora.business.terceros.persistence.manual.DatosSuplementariosTerceroDAO;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.webservice.tercero.AccesoBusquedaTercero;
import es.altia.flexia.terceros.integracion.externa.excepciones.CamposObligatoriosTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.EjecucionTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.ErrorTransaccionalTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.InstantiationTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.RestriccionTerceroExternoException;
import es.altia.flexia.terceros.integracion.externa.excepciones.SalidaAltaPersonaFisicaException;
import es.altia.flexia.terceros.integracion.externa.factoria.AltaTerceroExternoFactoria;
import es.altia.flexia.terceros.integracion.externa.servicio.AltaTerceroExterno;
import es.altia.flexia.terceros.integracion.externa.vo.ErrorSistemaTerceroExternoVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class TercerosManager {

    private final static String ID_SERVICIO_POR_DEFECTO = "SGE";

  private static TercerosManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log = LogFactory.getLog(TercerosManager.class.getName());

  protected TercerosManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static TercerosManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    synchronized(TercerosManager.class) {
        if (instance == null) {
          instance = new TercerosManager();
        }
      }
    return instance;
  }

  public Vector getListaDomicilios(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getListaDomiciliosValue");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      m_Log.debug("Usando persistencia manual");
      resultado = terDAO.getListaDomicilios(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("getListaDomiciliosValue");
      return resultado;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

// public Vector getListaDomiciliosExt(String[] params,GeneralValueObject gVO){
//    // queremos estar informados de cuando este metodo es ejecutado
//    m_Log.debug("getListaDomiciliosValue");
//    TercerosDAO terDAO = TercerosDAO.getInstance();
//    Vector resultado = new Vector();
//    try{
//      m_Log.debug("Usando persistencia manual");
//      resultado = terDAO.getListaDomicilioExt(gVO,params);
//      // queremos estar informados de cuando este metodo finaliza
//      m_Log.debug("getListaDomiciliosValue");
//      return resultado;
//    }catch(Exception ce){
//      m_Log.error("JDBC Technical problem " + ce.getMessage());
//      return resultado;
//    }
//  }

  public Vector getListaDomiciliosNoNormalizados(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getListaDomiciliosNoNormalizados");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      m_Log.debug("Usando persistencia manual");
      resultado = terDAO.getListaDomiciliosNoNormalizados(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("getListaDomiciliosNoNormalizados");
      return resultado;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector getDomiciliosById(String[] params,GeneralValueObject gVO){
   // queremos estar informados de cuando este metodo es ejecutado
   m_Log.debug("getDomiciliosById");
   TercerosDAO terDAO = TercerosDAO.getInstance();
   Vector resultado = new Vector();
   try{
     m_Log.debug("Usando persistencia manual");
     resultado = terDAO.getDomiciliosById(params,gVO);
     // queremos estar informados de cuando este metodo finaliza
     m_Log.debug("getListaDomiciliosValue");
     return resultado;
   }catch(Exception ce){
     m_Log.error("JDBC Technical problem " + ce.getMessage());
     return resultado;
   }
  }


  public Vector getListaVias(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getListaViasValue");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      m_Log.debug("Usando persistencia manual");
      resultado = terDAO.getListaVias(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("getListaViasValue");
      return resultado;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

    public Vector getByDocumento(TercerosValueObject terVO,String[] params){
      // queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getByDocumentoValue");
      TercerosDAO terDAO = TercerosDAO.getInstance();
      Vector resultado = new Vector();
      try{
        m_Log.debug("Usando persistencia manual");
        resultado = terDAO.getByDocumento(terVO,params);
        // queremos estar informados de cuando este metodo finaliza
        m_Log.debug("getByDocumentoValue");
        return resultado;
      }catch(Exception ce){
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        return resultado;
      }
    }

    public Vector getByIdTercero(TercerosValueObject terVO,String[] params){
      // queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getByIdTercero");
      TercerosDAO terDAO = TercerosDAO.getInstance();
      Vector resultado = new Vector();
      try{
        m_Log.debug("Usando persistencia manual");
        resultado = terDAO.getByIdTercero(terVO,params);
        // queremos estar informados de cuando este metodo finaliza
        m_Log.debug("getByIdTercero");
        return resultado;
      }catch(Exception ce){
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        return resultado;
      }
    }

    public TercerosValueObject getDatosTercero(String idTercero, String[] params){
        m_Log.debug("----->getDatosTercero");
        TercerosDAO terDAO = TercerosDAO.getInstance();
        TercerosValueObject resultado = new TercerosValueObject();
        try{
          m_Log.debug("Usando persistencia manual");
          resultado = terDAO.getDatosTercero(idTercero,params);
          // queremos estar informados de cuando este metodo finaliza
          m_Log.debug("<-----getDatosTercero");
          return resultado;
        }catch(Exception ce){
          m_Log.error("JDBC Technical problem " + ce.getMessage());
          return resultado;
        }
      }
    
    public HashMap getTercero(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) {

        m_Log.debug("TercerosManager --> getTercero()");
        Config m_ConfigTerceros = ConfigServiceHelper.getConfig("Terceros");

        /*
        * Buscamos los identificadores de los servicios de busqueda instalados en el fichero techserver.
        * El resultado de la llamada sera una cadena con todos los identificadores separados por ';'.
        * Si no se lee ningun identificador del fichero de configuración, se utiliza el servicio de busqeuda por
        * defecto (Identificador = SGE).
        */
        String prefijoOrg = "BusquedaTercero/" + condsBusqueda.getCodOrganizacion() + "/";
        String strIdsServicios = m_ConfigTerceros.getString(prefijoOrg + "serviciosDisp");
        if (strIdsServicios == null || "".equals(strIdsServicios.trim())) {
            strIdsServicios = ID_SERVICIO_POR_DEFECTO;
            m_Log.error("NO SE HA PODIDO RECUPERAR UNA CADENA DE IDENTIFICADORES PARA LOS SERVICIOS DE BUSQUEDA DE " +
                    "TERCEROS. INICIAMOS CON EL SERVICIO DE BUSQEUDA POR DEFECTO");
        }

        /*
         * Transformamos la cadena de identificadores en un vector y cremos una instancia de la clase encargada de
         * gestionar la busqueda.
         */
        Vector<String> idsServiciosBusq = new Vector<String>();
        StringTokenizer idsTokenizer = new StringTokenizer(strIdsServicios, ";");
        while (idsTokenizer.hasMoreTokens()) idsServiciosBusq.add(idsTokenizer.nextToken());
        if (idsServiciosBusq.size() == 0) {
            idsServiciosBusq.add(ID_SERVICIO_POR_DEFECTO);
            m_Log.error("HA OCURRIDO UN ERROR AL PROCESAR LA CADENA DE IDENTIFICADORES DE SERVICIOS DE BUSQUEDA DE " +
                    "TERCEROS. INICIAMOS CON EL SERVICIO DE BUSQEUDA POR DEFECTO");
    }

        AccesoBusquedaTercero busquedaTercero = new AccesoBusquedaTercero(idsServiciosBusq, prefijoOrg);
        return busquedaTercero.getTercero(condsBusqueda, params);
  }
     public Vector getTerceroExpMiUnidad(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) {

         m_Log.info("getTerceroExpMiUnidad");
         Vector resultado = new Vector();
         try {
             m_Log.debug("Usando persistencia manual");
              /*
         * Esta busqueda se realiza para los terceros de expedientes de la unidad del usuario. tiene como origen SOLO el SGE.
         */
             TercerosDAO tercerosDAO = TercerosDAO.getInstance();
             resultado= tercerosDAO.getTerceroExpMiUnidad(condsBusqueda, params);
             return resultado;
        }catch(Exception ce){
             m_Log.error("JDBC Technical problem " + ce.getMessage());
             return resultado;
      }


  }
     
      public Vector getTerceroExpFlexia(CondicionesBusquedaTerceroVO condsBusqueda, String[] params) {

         m_Log.info("getTerceroExpMiUnidad");
         Vector resultado = new Vector();
         try {
             m_Log.debug("Usando persistencia manual");
              /*
         * Esta busqueda se realiza para los terceros de expedientes de la unidad del usuario. tiene como origen SOLO el SGE.
         */
             TercerosDAO tercerosDAO = TercerosDAO.getInstance();
             resultado= tercerosDAO.getTercero(condsBusqueda, params);
             return resultado;
        }catch(Exception ce){
             m_Log.error("JDBC Technical problem " + ce.getMessage());
             return resultado;
      }


  }

    public Vector getTerceroHistorico(GeneralValueObject terVO,String[] params){
      // queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getTerceroHistorico");
      TercerosDAO terDAO = TercerosDAO.getInstance();
      Vector resultado = new Vector();
      try{
        m_Log.debug("Usando persistencia manual");
        resultado = terDAO.getTerceroHistorico(terVO,params);
        // queremos estar informados de cuando este metodo finaliza
        m_Log.debug("getTerceroHistorico");
        return resultado;
      }catch(Exception ce){
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        return resultado;
      }
    }

  public Vector getByHistorico(TercerosValueObject terVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("loadTercerosValue");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    Vector resultado = new Vector();
    try{
      m_Log.debug("Usando persistencia manual");
      resultado = terDAO.getByHistorico(terVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("loadTercerosValue");
      return resultado;
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector getListaInteresadosRegistro(GeneralValueObject gVO, String[] params){
	    // queremos estar informados de cuando este metodo es ejecutado
	    m_Log.debug("loadTercerosValue");
	    TercerosDAO terDAO = TercerosDAO.getInstance();
	    Vector resultado = new Vector();
	    try{
	      m_Log.debug("Usando persistencia manual");
	      resultado = terDAO.getListaInteresadosRegistro(gVO,params);
	      // queremos estar informados de cuando este metodo finaliza
	      m_Log.debug("loadTercerosValue");
	      return resultado;
	    }catch(Exception ce){
	      m_Log.error("JDBC Technical problem " + ce.getMessage());
	      return resultado;
	    }
	  }

    public int setTercero(TercerosValueObject terVO,String[] params){
      // queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("insertTercerosValue");
      TercerosDAO terDAO = TercerosDAO.getInstance();
      int res=0;
      try{
        m_Log.debug("Usando persistencia manual");
        res = terDAO.setTercero(terVO,params);
        // queremos estar informados de cuando este metodo finaliza
        m_Log.debug("insertTercerosValue");
      }catch(Exception ce){
        m_Log.error("JDBC Technical problem " + ce.getMessage());
      }
      return res;
    }

  public int setTerceroDuplicado(TercerosValueObject terVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("insertTercerosValue");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    int res=0;
    try{
      m_Log.debug("Usando persistencia manual");
      res = terDAO.setTerceroDuplicado(terVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("insertTercerosValue");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return res;
  }

    public boolean existeTercero(TercerosValueObject terVO,String[] params){
       // queremos estar informados de cuando este metodo es ejecutado
       m_Log.debug("existeTercero");
       TercerosDAO terDAO = TercerosDAO.getInstance();
       boolean res=false;
       try{
         m_Log.debug("Usando persistencia manual");
         res=terDAO.existeTercero(terVO,params);
         // queremos estar informados de cuando este metodo finaliza
         m_Log.debug("existeTercero");
       }catch(Exception ce){
         m_Log.error("JDBC Technical problem " + ce.getMessage());
       }
       return res;
     }

    public int updateTercero(TercerosValueObject terVO,String[] params){
      // queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("updateTercerosValue");
      TercerosDAO terDAO = TercerosDAO.getInstance();
      int res=0;
      try{
        m_Log.debug("Usando persistencia manual");
        res=terDAO.updateTercero(terVO,params);
        // queremos estar informados de cuando este metodo finaliza
        m_Log.debug("updateTercerosValue");
      }catch(Exception ce){
        m_Log.error("JDBC Technical problem " + ce.getMessage());
        ce.printStackTrace();
      }
      return res;
    }

  public Vector getIdTercero(TercerosValueObject terVO,String[] params,String mod){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getIdTercero");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    Vector res=null;
    try{
      m_Log.debug("Usando persistencia manual");
      res=terDAO.getIdTercero(terVO,params,mod);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("getIdTercero");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return res;
  }

  public void cambiaSituacionTercero(TercerosValueObject terVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("bajaTercerosValue");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      terDAO.cambiaSituacionTercero(terVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("bajaTercerosValue");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
  }

    public void eliminarTercerosSimilares(String codTerDepurado, String codDomPrincipal, String[] codTerAEliminar,
                                          int codUsuario, String[] params){
      // queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("eliminarTercerosSimilares");
      TercerosDAO terDAO = TercerosDAO.getInstance();
      try{
        m_Log.debug("Usando persistencia manual");
        terDAO.eliminarTercerosSimilares(codTerDepurado, codDomPrincipal, codTerAEliminar,codUsuario, params);
        // queremos estar informados de cuando este metodo finaliza
        m_Log.debug(" fin eliminarTercerosSimilares manager");
      }catch(Exception ce){
        m_Log.error("JDBC Technical problem " + ce.getMessage());
      }
    }

  public int setDomicilioTercero(TercerosValueObject terVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("insertDomicilioTerceroValue");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    int res = 0;
    try{
      m_Log.debug("Usando persistencia manual");
      res = terDAO.setDomicilioTercero(terVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("insertDomicilioTerceroValue");
    }catch(Exception ce){
        ce.printStackTrace();
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return res;
  }
  public int setDomicilioTerceroRapido(TercerosValueObject terVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("insertDomicilioTerceroValue");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    int res = 0;
    try{
      m_Log.debug("Usando persistencia manual");
      res = terDAO.setDomicilioTerceroRapido(terVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("insertDomicilioTerceroValue");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return res;
  }

    public int existeDomicilio(TercerosValueObject terVO,String[] params){
    m_Log.debug("Comprobamos si existe el domicilio");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    int res = 0;
    try{
      res = terDAO.existeDomicilio(terVO,params);
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return res;
  }
  public void updateDomicilioTercero(TercerosValueObject terVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("updateDomicilioTerceroValue");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      terDAO.updateDomicilioTercero(terVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("updateDomicilioTerceroValue");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
  }
  
  public void cambiarDomicilioTercero(TercerosValueObject terVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cambiarDomicilioTercero");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      terDAO.cambiarDomicilioTercero(terVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("cambiarDomicilioTercero");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
  }

  public void deleteDomicilioTercero(TercerosValueObject terVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("deleteDomicilioTerceroValue");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    try{
      m_Log.debug("Usando persistencia manual");
      terDAO.deleteDomicilioTercero(terVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("deleteDomicilioTerceroValue");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
  }

  public boolean eliminarDomicilioTerceroPadron(TercerosValueObject terVO,String[] params){
	// queremos estar informados de cuando este metodo es ejecutado
	boolean eliminado;
	m_Log.debug("eliminarDomicilioTerceroPadron");
	TercerosDAO terDAO = TercerosDAO.getInstance();
	try{	  
	  eliminado = terDAO.eliminarDomicilioTerceroPadron(terVO,params);	  
	  m_Log.debug("eliminarDomicilioTerceroPadron");
	}catch(Exception ce){
	  eliminado = false;
	  ce.printStackTrace();
	  m_Log.error("JDBC Technical problem " + ce.getMessage());
	}
	return eliminado;
  }

  public boolean eliminarTerceroPadron(TercerosValueObject terVO,String[] params){
	  // queremos estar informados de cuando este metodo es ejecutado
	  boolean eliminado;
	  m_Log.debug("eliminarTerceroPadron");
	  TercerosDAO terDAO = TercerosDAO.getInstance();
	  try{	  
		eliminado = terDAO.eliminarTerceroPadron(terVO,params);	  
		m_Log.debug("eliminarTerceroPadron");
	  }catch(Exception ce){
		eliminado = false;
		ce.printStackTrace();
		m_Log.error("JDBC Technical problem " + ce.getMessage());
	  }
	  return eliminado;
	}

  public int grabarDomiciliosNoNormalizados(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("grabarDomiciliosNoNormalizados");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    int res=0;
    try{
      m_Log.debug("Usando persistencia manual");
      res = terDAO.grabarDomiciliosNoNormalizados(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("grabarDomiciliosNoNormalizados");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return res;
  }

   public int grabarDomiciliosNoNormalizadosLocalizacion(GeneralValueObject gVO,String[] params){
    // queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("grabarDomiciliosNoNormalizadosLocalizacion");
    TercerosDAO terDAO = TercerosDAO.getInstance();
    int res=0;
    try{
      m_Log.debug("Usando persistencia manual");
      res = terDAO.grabarDomiciliosNoNormalizadosLocalizacion(gVO,params);
      // queremos estar informados de cuando este metodo finaliza
      m_Log.debug("grabarDomiciliosNoNormalizadosLocalizacion");
    }catch(Exception ce){
      m_Log.error("JDBC Technical problem " + ce.getMessage());
    }
    return res;
  }

  public boolean eliminarDomiciliosNoNormalizado(GeneralValueObject gVO,String[] params){
	  // queremos estar informados de cuando este metodo es ejecutado
	  m_Log.debug("eliminarDomiciliosNoNormalizados");
	  TercerosDAO terDAO = TercerosDAO.getInstance();
	  boolean res=false;
	  try{
		m_Log.debug("Usando persistencia manual");
		res = terDAO.eliminarDomicilioNoNormalizado(gVO,params);
		// queremos estar informados de cuando este metodo finaliza
		m_Log.debug("eliminarDomiciliosNoNormalizados");
	  }catch(Exception ce){
		m_Log.error("JDBC Technical problem " + ce.getMessage());
	  }
	  return res;
	}

    public String getNombreTercero(int codigo,String[] params){
      // queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getNombreTercero");
      TercerosDAO terDAO = TercerosDAO.getInstance();
        String nombre = "";
      try{
        m_Log.debug("Usando persistencia manual");
        nombre = terDAO.getNombreTercero(codigo,params);
      }catch(Exception ce){
        m_Log.error("JDBC Technical problem " + ce.getMessage());
      }
      return nombre;
    }

    public Vector getTercerosRepByFec(String[] params,GeneralValueObject gVO) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getTercerosRepByFec");
        Vector resultado = new Vector();
        TercerosDAO terDAO = TercerosDAO.getInstance();
        try{
            m_Log.debug("Usando persistencia manual");
            resultado = terDAO.getTercerosRepByFec(params, gVO);
        }catch(Exception ce){
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        return resultado;
    }

    public Vector getTercerosRepByNomAndDoc(String[] params,GeneralValueObject gVO) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getTercerosRepByNomAndDoc");
        Vector resultado = new Vector();
        TercerosDAO terDAO = TercerosDAO.getInstance();
        try{
            m_Log.debug("Usando persistencia manual");
            resultado = terDAO.getTercerosRepByNomAndDoc(params, gVO);
        }catch(Exception ce){
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        return resultado;
    }

    public Vector getTercerosRepByNomCompleto(String[] params,GeneralValueObject gVO) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getTercerosRepByNomCompleto");
        Vector resultado = new Vector();
        TercerosDAO terDAO = TercerosDAO.getInstance();
        try{
            m_Log.debug("Usando persistencia manual");
            resultado = terDAO.getTercerosRepByNomCompleto(params, gVO);
        }catch(Exception ce){
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        return resultado;
    }

    /* Devuelve el número de terceros que comparten el domicilio pasado como argumento */
    public int getNumeroTercerosByDomicilio(DomicilioSimpleValueObject domVO, String[] params) {
    	
    	m_Log.debug("TercerosManager->getTercerosRepByNomCompleto");
    	int resultado = 0;
    	TercerosDAO terDAO = TercerosDAO.getInstance();
        try{
            m_Log.debug("Usando persistencia manual");
            resultado = terDAO.getNumeroTercerosByDomicilio(domVO, params);
        }catch(Exception ce){
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }        
    	return resultado;
    }
    
    public Vector getTercerosSimilaresById(String[] params,GeneralValueObject gVO) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getTercerosSimilaresById");
        Vector<Vector> resultado = new Vector<Vector>();
        TercerosDAO terDAO = TercerosDAO.getInstance();
        try{
            m_Log.debug("Usando persistencia manual");
            Vector tercerosSimilares = terDAO.getIdsTercerosSimilares(params, gVO);
            for (Object objIdTercero : tercerosSimilares) {
                TercerosValueObject terVO = new TercerosValueObject();
                terVO.setIdentificador(objIdTercero.toString());
                resultado.add(terDAO.getByIdTercero(terVO, params));
            }
        }catch(Exception ce){
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        return resultado;
    }

    public void depurarDomicilios(String[] params, GeneralValueObject gVO, String[] codDomAEliminar, int codUsuario) {
        // queremos estar informados de cuando este metodo es ejecutado
        try{
            m_Log.debug("depurarDomicilios");
            TercerosDAO tercerosDAO = TercerosDAO.getInstance();
            m_Log.debug("Usando persistencia manual");
            tercerosDAO.depurarDomicilios(params, gVO, codDomAEliminar, codUsuario);
        }catch(Exception ce){
            m_Log.error("Exception " + ce.getMessage());
        }
    }

    public void copiarDomiciliosATercero(Vector codDoms, String codTercero, String codUsuario, String[] params)
    throws TechnicalException {

        try {
            m_Log.debug("TercerosManager --> Inicio copiarDomiciliosATercero");

            TercerosDAO tercerosDAO = TercerosDAO.getInstance();
            tercerosDAO.copiarDomiciliosATercero(codDoms, codTercero, codUsuario, params);

            m_Log.debug("TercerosManager --> Fin copiarDomiciliosATercero");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        } catch (BDException bde) {
            bde.printStackTrace();
            throw new TechnicalException(bde.getMensaje());
        }       
    }

    public Vector getTerceroInterno(CondicionesBusquedaTerceroVO condsVO, String[] params) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getIdTercero");
        TercerosDAO terDAO = TercerosDAO.getInstance();
        Vector res = new Vector();
        try {
            m_Log.debug("Usando persistencia manual");
            res = terDAO.getTercero(condsVO, params);
            // queremos estar informados de cuando este metodo finaliza
            m_Log.debug("getIdTercero");
        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        return res;
    }
    
    
    
    /**
     * Recupera los terceros que cumplen determinadas condiciones de búsqueda
     * @param condsVO: Objeto de tipo CondicionesBusquedaTerceroVO con las condiciones de búsqueda
     * @param adapt: Objeto de tipo AdaptadorSQLBD
     * @param con: Conexión a la BBDD
     * @return Colección de objetos de tipo TercerosValueObject con los datos de los terceros que cumplen
     * las condiciones de búsqueda
    
    public Vector getTerceroInterno(CondicionesBusquedaTerceroVO condsVO,AdaptadorSQLBD adapt,Connection con) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getTerceroInterno ========>");
        TercerosDAO terDAO = TercerosDAO.getInstance();
        Vector res = new Vector();
        try {
            
            res = terDAO.getTercero(condsVO, adapt,con);
            // queremos estar informados de cuando este metodo finaliza            
        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        m_Log.debug("getTerceroInterno <========");
        return res;
    }
     */
    
    
    
    

    public Vector getTerceroUOR(CondicionesBusquedaTerceroVO condsVO, String[] params) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getTerceroUOR");
        TercerosDAO terDAO = TercerosDAO.getInstance();
        Vector res = new Vector();
        try {
            m_Log.debug("Usando persistencia manual");
            res = terDAO.getTerceroUOR(condsVO, params);
            // queremos estar informados de cuando este metodo finaliza
            m_Log.debug("getTerceroUOR");
        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        return res;
    }

    /**
     * Recupera a partir del número de expediente el e-mail del interesado del mismo
     * @param codTercero: Identificador del tercero
     * @param params: Parámetros de conexión a la base de datos
     * @return Un String
     */
    public String getEmailTercero(String codExpediente,String[] params){
        String email = null;
        
        try{          
            TercerosDAO terDAO = TercerosDAO.getInstance();
            email = terDAO.getEmailTercero(codExpediente,params);            
        }
        catch(Exception e){
            e.printStackTrace();
            m_Log.error(e.getMessage());
        }        
        return email;
    }


    public int grabarDomiciliosNoNormalizadosLocalizacionExpedientesRelacionados(GeneralValueObject gVO,String[] expedientes,String[] params){
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("=====>grabarDomiciliosNoNormalizadosLocalizacionExpedientesRelacionados");
        TercerosDAO terDAO = TercerosDAO.getInstance();
        int res=0;
        try{
          m_Log.debug("Usando persistencia manual");
          res = terDAO.grabarDomiciliosNoNormalizadosLocalizacionExpedientesRelacionados(gVO,expedientes,params);
          // queremos estar informados de cuando este metodo finaliza
          m_Log.debug("grabarDomiciliosNoNormalizadosLocalizacionExpedientesRelacionados end <=========");
        }catch(Exception ce){
          m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        return res;
    }


	/**
	 * Permite dar de alta un tercero junto con su domicilio, via y código postal encapsulado en una única transacción.
	 * Además en el caso de que haya servicios de terceros externos definidos, se invocan para dar de alta el tercero en
	 * dichos sistemas
	 *
	 * @param terVO: Objeto que contiene la información del tercero
	 * @param txtNormalizado: Cadena de caracteres que indica si se trata de un domicilio normalizado
	 * @param via: Datos de la vía correspondiente al domicilio
	 * @param altaVia: Se indica al método si tiene que dar de alta la vía correspondiente al domicilio del interesado
	 * @param codigoPostal: Objeto de la clase GeneralValueObject que contiene el código postal del domicilio
	 * @param sIdDomicilio: Id del domicilio
	 * @param params: Parámetros de conexión a la BD
	 * @return int
	 */
	public SalidaTratamientoTerceroExterno setTerceroExterno(int codOrganizacion, TercerosValueObject terVO,
			String txtNormalizado, GeneralValueObject via, boolean altaVia, GeneralValueObject codigoPostal, 
			String sIdDomicilio, int codAplicacion, Vector estructuraCampos, Vector valoresCampos, String[] params) 
			throws TechnicalException, ErrorTransaccionalTerceroExternoException {
		return setTerceroExterno(codOrganizacion, terVO, txtNormalizado, via, altaVia, codigoPostal, sIdDomicilio, 
				codAplicacion, estructuraCampos, valoresCampos, params, null, false);
	}

    /**
     * Permite dar de alta un tercero junto con su domicilio, via y código postal encapsulado en una única transacción. Además
     * en el caso de que haya servicios de terceros externos definidos, se invocan para dar de alta el tercero en dichos sistemas
     * @param terVO: Objeto que contiene la información del tercero
     * @param txtNormalizado: Cadena de caracteres que indica si se trata de un domicilio normalizado
     * @param via: Datos de la vía correspondiente al domicilio
     * @param altaVia: Se indica al método si tiene que dar de alta la vía correspondiente al domicilio del interesado
     * @param codigoPostal: Objeto de la clase GeneralValueObject que contiene el código postal del domicilio
     * @param sIdDomicilio: Id del domicilio
     * @param params: Parámetros de conexión a la BD
     * @return int
     */
    public SalidaTratamientoTerceroExterno setTerceroExterno(int codOrganizacion,TercerosValueObject terVO,
			String txtNormalizado,GeneralValueObject via,boolean altaVia,GeneralValueObject codigoPostal,
			String sIdDomicilio,int codAplicacion,Vector estructuraCampos,Vector valoresCampos,String[] params,
			Connection con, boolean transaccionExterna) 
			throws TechnicalException, ErrorTransaccionalTerceroExternoException{
      // queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("setTerceroNuevo");
      AdaptadorSQLBD adapt = null;
      // Connection con = null;
      boolean altaTerceroFlexia = false;
      SalidaTratamientoTerceroExterno salida = new SalidaTratamientoTerceroExterno();

      TercerosDAO terDAO = TercerosDAO.getInstance();      
      try{

        adapt = new AdaptadorSQLBD(params);
        if (!transaccionExterna) {
			con = adapt.getConnection();
			adapt.inicioTransaccion(con);
		}
        int max=0;
                
        
        if (terVO.getIdentificador()==null || terVO.getIdentificador().equals("0") || terVO.getIdentificador().equals("")) {
                int idTercero;
                               
                TercerosValueObject terExtVO = null;
                try{
                    terExtVO =  terDAO.existeTerceroExterno(terVO.getCodTerceroOrigen(),terVO.getDocumento(), con);
                }catch(Exception e){
                    terExtVO = null;
                }
                
                if (terExtVO!=null) {
                    idTercero = Integer.parseInt(terExtVO.getIdentificador());
                    terVO.setVersion(terExtVO.getVersion());
                    terVO.setIdentificador(terExtVO.getIdentificador());
                    //updateTercero(terVO, params);                                        
                    terDAO.updateTercero(terVO,adapt,con);
                    terVO.setVersion((Integer.parseInt(terVO.getVersion())+1)+"");     
                }else {
                    CondicionesBusquedaTerceroVO conds = new CondicionesBusquedaTerceroVO();
                    conds.setTipoDocumento(Integer.parseInt(terVO.getTipoDocumento()));
					conds.setNombre(terVO.getNombre());
					conds.setApellido1(terVO.getApellido1());
					conds.setApellido2(terVO.getApellido2()); 
                    if (!terVO.getTipoDocumento().equals("0")) {                   
                        conds.setDocumento(terVO.getDocumento());
                    }

                    Vector tercerosReps = terDAO.getTercero(conds, adapt,con, params);                

                    if (tercerosReps.size() > 0) {
                        // Se toma el primero de los terceros internos que aparecen
                        idTercero = Integer.parseInt(((TercerosValueObject) tercerosReps.elementAt(0)).getIdentificador());
                        terVO.setVersion(((TercerosValueObject) tercerosReps.elementAt(0)).getVersion());
                    } else {
                        //idTercero = setTercero(terVO, params);                    
                        idTercero = terDAO.setTercero(terVO,adapt,con);                    
                        terVO.setVersion("1");
                    }
                }
                if (m_Log.isDebugEnabled()) m_Log.debug("El valor del Id del usuario es : " + idTercero);
                terVO.setIdentificador(String.valueOf(idTercero));              
                max= idTercero;
        }
        
        if(max>0){
            terVO.setIdentificador(Integer.toString(max));
            
            Vector domicilios = terVO.getDomicilios();
            if(domicilios!=null && domicilios.size()==1){
                // Como se trata del alta de un tercero, sólo puede venir un domicilio
                DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject)domicilios.get(0);
            int idVia = -1;

            if(altaVia){
                    m_Log.debug("Se da de alta la v?a para el domicilio del tercero");
                idVia = ViasDAO.getInstance().altaViaNoRepetido(via, adapt, con);
                m_Log.debug("La via tiene el codigo "+ idVia);
            }
                domicilio.setIdVia(String.valueOf(idVia));
                
                m_Log.debug(" ============== domicilioNormalizado: " + txtNormalizado);
                int idExistente = terDAO.existeDomicilio(terVO,adapt,con);

                int idDomicilio = 0;
                if (idExistente > 0) {
                    // El domicilio ya existe, su id es idExistente
                    if(m_Log.isDebugEnabled()) m_Log.debug("EXISTE EL DOMICILIO");
                    txtNormalizado = "1";
                    domicilio.setNormalizado(txtNormalizado);
                    terVO.setIdDomicilio(String.valueOf(idExistente));
                    idDomicilio = terDAO.setDomicilioTercero(terVO, adapt, con);
                    if(idDomicilio>=1) altaTerceroFlexia = true;
                }else {
                    // El domicilio no existe
                    if(m_Log.isDebugEnabled()) m_Log.debug("NO EXISTE EL DOMICILIO");

                    if(!txtNormalizado.equals("2")){
                        terVO.setIdDomicilio(sIdDomicilio);
                    }

                    idDomicilio = terDAO.setDomicilioTercero(terVO, adapt, con);
                    if(idDomicilio>=1) altaTerceroFlexia = true;

                    if(m_Log.isDebugEnabled()) m_Log.debug("SE INSERTA EL COD POSTAL EN CASO DE QUE NO EXISTA");
                    
                    String descPostal = (String)codigoPostal.getAtributo("descPostal");

                    CodPostalesDAO codPostalDAO = CodPostalesDAO.getInstance();
                    if (!"".equals(descPostal) && !codPostalDAO.existeCodPostal(descPostal,con)) {

                        // Esta lista de códigos postales se va para el formulario de terceros
                        boolean exito = codPostalDAO.altaCodPostal(codigoPostal, con);
                        if(!exito)
                            altaTerceroFlexia = false;
                        else
                            altaTerceroFlexia = true;
                    }// if
                }// else

                domicilio.setIdDomicilio(String.valueOf(idDomicilio));


            }// if

            try{

                DatosSuplementariosTerceroDAO.getInstance().grabarDatosSuplementarios(Integer.toString(codOrganizacion),terVO.getIdentificador(),estructuraCampos,valoresCampos, adapt, con);
                
            } catch(TechnicalException e){
                e.printStackTrace();
                m_Log.error("Error al grabar los campos suplementarios del tercero");
                altaTerceroFlexia = false;
            }

         }else{
            altaTerceroFlexia = false;
         }


         if(altaTerceroFlexia){
            // Se confirma la transacción en la que se ha dado de alta el tercero en la BBDD de Flexia
            
            /*** INICIO: tratar los servicios de alta de tercero externo ***/
            Hashtable<Integer,ArrayList<ErrorSistemaTerceroExternoVO>> errores = new Hashtable<Integer,ArrayList<ErrorSistemaTerceroExternoVO>>();
            ArrayList<String> lista= AltaTerceroExternoFactoria.getServiciosTercerosExternosDisponibles(String.valueOf(codOrganizacion),codAplicacion);

            boolean transaccional = AltaTerceroExternoFactoria.altaTerceroExternoRequiereTransaccionalidad(String.valueOf(codOrganizacion));

                /*****/
                int numServicios = lista.size();
                int numAltasExternasExito = 0;
                
                for(int i=0;lista!=null && i<lista.size();i++){
                    String strServicio = lista.get(i);
                    m_Log.debug("Tratando el servicio " + strServicio);
                    
                    try{                        
                        AltaTerceroExterno servicio = AltaTerceroExternoFactoria.getServicio(String.valueOf(codOrganizacion),strServicio);
                        boolean exito = servicio.setTercero(terVO,estructuraCampos,valoresCampos,via,con);
                        if(exito) numAltasExternasExito++;

                    }catch(InstantiationTerceroExternoException e){

                        m_Log.error("Error al instanciar el servicio "  + strServicio + " para el alta de un tercero externo");
                       
                        ArrayList<ErrorSistemaTerceroExternoVO> err = new ArrayList<ErrorSistemaTerceroExternoVO>();
                        ArrayList<String> etiquetasError = new ArrayList<String>();
                        etiquetasError.add("msgErrInstanciaClaseTerExt");

                        ErrorSistemaTerceroExternoVO error = new ErrorSistemaTerceroExternoVO();
                        error.setNombreServicio(strServicio);
                        error.setTipoError(AltaTerceroExternoFactoria.ERROR_INSTANCIA_CLASE_SISTEMA_EXTERNO_TERCEROS);
                        error.setListaErrores(getListaErroresTraducidos(etiquetasError,3,Integer.parseInt(terVO.getCodIdioma())));
                        err.add(error);
                        errores = this.grabarError(AltaTerceroExternoFactoria.ERROR_INSTANCIA_CLASE_SISTEMA_EXTERNO_TERCEROS,err, errores);

                    }catch(CamposObligatoriosTerceroExternoException e){
                        m_Log.error("Error al dar de alta el tercero externo en el servicio porque hay campos obligatorios: " + strServicio);
                       
                        ArrayList<ErrorSistemaTerceroExternoVO> err = new ArrayList<ErrorSistemaTerceroExternoVO>();

                        // Se compone el mensaje de error a mostrar
                        ArrayList<String> mensajes = new ArrayList<String>();
                        mensajes.add(this.getDescripcionError(3, Integer.parseInt(terVO.getCodIdioma()), "msgErrParaServicio") + strServicio + " " +
                                this.getDescripcionError(3, Integer.parseInt(terVO.getCodIdioma()), "msgErrCamposObligatorios") + " " +
                                e.getListaCamposObligatorios());

                        ErrorSistemaTerceroExternoVO error = new ErrorSistemaTerceroExternoVO();
                        error.setNombreServicio(strServicio);
                        error.setTipoError(AltaTerceroExternoFactoria.ERROR_CAMPOS_OBLIGATORIOS_SISTEMA_EXTERNO_TERCEROS);                        
                        error.setListaErrores(mensajes);

                        err.add(error);
                        errores = this.grabarError(AltaTerceroExternoFactoria.ERROR_CAMPOS_OBLIGATORIOS_SISTEMA_EXTERNO_TERCEROS,err, errores);


                    }catch(RestriccionTerceroExternoException e){
                        m_Log.error("Error en restricciones durante el alta del tercero externo en el servicio " + strServicio);                       
                        ArrayList<ErrorSistemaTerceroExternoVO> err = new ArrayList<ErrorSistemaTerceroExternoVO>();
                        ErrorSistemaTerceroExternoVO error = new ErrorSistemaTerceroExternoVO();
                        error.setNombreServicio(strServicio);
                        error.setTipoError(AltaTerceroExternoFactoria.ERROR_RESTRICCIONES_ALTA_SISTEMA_EXTERNO_TERCEROS);
                        error.setListaErrores(getListaErroresTraducidos(e.getEtiquetasError(),3,Integer.parseInt(terVO.getCodIdioma())));
                        err.add(error);
                        errores = this.grabarError(AltaTerceroExternoFactoria.ERROR_RESTRICCIONES_ALTA_SISTEMA_EXTERNO_TERCEROS,err, errores);

                    }catch(EjecucionTerceroExternoException e){
                        m_Log.error("Error en ejecución del servicio de alta de tercero externo servicio " + strServicio);                                              
                        ArrayList<ErrorSistemaTerceroExternoVO> err = new ArrayList<ErrorSistemaTerceroExternoVO>();
                        ArrayList<String> etiquetasError = new ArrayList<String>();
                        etiquetasError.add(this.getDescripcionError(3,Integer.parseInt(terVO.getCodIdioma()),"msgErrEjecSistemaTerExt"));
                        
                        ErrorSistemaTerceroExternoVO error = new ErrorSistemaTerceroExternoVO();
                        error.setTipoError(AltaTerceroExternoFactoria.ERROR_EJECUCION_SISTEMA_EXTERNO_TERCEROS);
                        error.setListaErrores(etiquetasError);
                        error.setNombreServicio(strServicio);                        
                        err.add(error);
                        errores = grabarError(AltaTerceroExternoFactoria.ERROR_EJECUCION_SISTEMA_EXTERNO_TERCEROS,err, errores);

                    }catch(SalidaAltaPersonaFisicaException e){

                        ArrayList<ErrorSistemaTerceroExternoVO> err = new ArrayList<ErrorSistemaTerceroExternoVO>();
                        ErrorSistemaTerceroExternoVO error = new ErrorSistemaTerceroExternoVO();
                        error.setNombreServicio(strServicio);
                        error.setTipoError(AltaTerceroExternoFactoria.ERROR_SALIDA_ALTA_TERCERO_SISTEMA_EXTERNO_TERCEROS);
                        error.setListaErrores(e.getListadoErrores());
                        err.add(error);

                        errores = grabarError(AltaTerceroExternoFactoria.ERROR_SALIDA_ALTA_TERCERO_SISTEMA_EXTERNO_TERCEROS,err, errores);
                    }
                }// for
                /******/

                if(transaccional){
                    
                    if(numServicios==numAltasExternasExito){
                        finalizarTransaccion(adapt, con, transaccionExterna);
                        salida.setIdTercero(max);
                        salida.setVerTercero(terVO.getVersion());
                    }else{                        
                        if(errores.size()==0)
                            // Si no hay errores porque no ha saltado ninguna excepción del tipo  InstantiationTerceroExternoException, CamposObligatoriosTerceroExternoException,
                            // RestriccionTerceroExternoException o EjecucionTerceroExternoException, es porque alguna llamada a algún sistema de tercero
                            // externo ha fallado. Eso implica hacer un rollback de la transacción.
                            throw new ErrorTransaccionalTerceroExternoException("setTerceroExterno: No se ha dado de alta el tercero en todos los sistemas de terceros externos. Se cancela el alta.");
                            
                        else{
                            // hay llamadas de alta de tercero a los sistemas externos de terceros que han fallado. Eso implica  deshacer la transacción y
                            // mostrar los errores producidos en dichos sistemas.
                            //adapt.rollBack(con);
                            
                            con.rollback();
                        }
                        
                    }
                }else{
					finalizarTransaccion(adapt, con, transaccionExterna);
                    salida.setIdTercero(max);
                    salida.setVerTercero(terVO.getVersion());
                }

                salida.setErroresEjecucionServicio(errores);
            
            /*** FIN: tratar los servicios de alta de tercero externo ***/

         }else{
            // Se cancela la transacción porque ha ocurrido algún error al tratar de dar de alta el tercero en la BBDD de Flexia
            throw new TechnicalException("setTerceroExterno: Error al intentar dar de alta un tercero en Flexia");
            
         }

      }catch(Exception ce){
          m_Log.error("JDBC Technical problem " + ce.getMessage());
          try{
              adapt.rollBack(con);
              m_Log.debug("ejecución correcta del rollback");
          }catch(BDException e){
              m_Log.error("Error al realizar un rollback: " + e.getMessage());
          }

          if(ce instanceof ErrorTransaccionalTerceroExternoException){
                throw new ErrorTransaccionalTerceroExternoException(ce.getMessage());
          }else
                throw new TechnicalException("setTerceroExterno: Error al intentar dar de alta un tercero en Flexia  " + ce.getMessage());
            
      }finally{
          
          try{
              devolverConexion(adapt, con, transaccionExterna);
          }catch(BDException e){
              m_Log.error("Error al cerrar la conexión a la BBDD en setTerceroNuevo");
          }
      }

      return salida;
    }

    
    /**
	 * Finaliza la transaccion si no esta marcada como transaccion externa.
	 * 
	 * @param adaptador Adaptador de la base de datos.
	 * @param conexion Conexion a la base de datos.
	 * @param transaccionExterna Define si la transaccion es externa.
	 * @throws BDException 
	 */
	private void finalizarTransaccion(AdaptadorSQLBD adaptador, Connection conexion, boolean transaccionExterna) throws BDException {
		if (!transaccionExterna) {
			adaptador.finTransaccion(conexion);
		}
	}
    
	/**
	 * Devolver la conexion la transaccion no esta marcada como transaccion externa.
	 * 
	 * @param adaptador Adaptador de la base de datos.
	 * @param conexion Conexion a la base de datos.
	 * @param transaccionExterna Define si la transaccion es externa.
	 * @throws BDException 
	 */
	private void devolverConexion(AdaptadorSQLBD adaptador, Connection conexion, boolean transaccionExterna) throws BDException {
		if (!transaccionExterna) {
			adaptador.devolverConexion(conexion);
		}
	}
    
    public TercerosValueObject setTercero(String codOrganizacion,TercerosValueObject terVO,String codAplicacion,Vector estructuraCampos,Vector valoresCampos,String[] params) throws TechnicalException, ErrorTransaccionalTerceroExternoException{
      // queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("setTerceroNuevo");
      AdaptadorSQLBD adapt = null;
      Connection con = null;
      boolean altaTerceroFlexia = false;
      TercerosValueObject salida = new TercerosValueObject();
      String txtNormalizado="";

      TercerosDAO terDAO = TercerosDAO.getInstance();      
      try{

        adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        adapt.inicioTransaccion(con);
        int max=0;
        terVO.setModuloAlta(codAplicacion);
                
        
        if (terVO.getIdentificador()==null || terVO.getIdentificador().equals("0") || terVO.getIdentificador().equals("")) {
                int idTercero;
                               
             
                
                    CondicionesBusquedaTerceroVO conds = new CondicionesBusquedaTerceroVO();
                    conds.setTipoDocumento(Integer.parseInt(terVO.getTipoDocumento()));
                    if (terVO.getTipoDocumento().equals("0")) {
                        conds.setNombre(terVO.getNombre());
                        conds.setApellido1(terVO.getApellido1());
                        conds.setApellido2(terVO.getApellido2());                    
                    } else {
                        conds.setNombre(terVO.getNombre());
                        conds.setApellido1(terVO.getApellido1());
                        conds.setApellido2(terVO.getApellido2());
                        conds.setDocumento(terVO.getDocumento());
                    }

                    Vector tercerosReps = terDAO.getTercero(conds, adapt,con,params);                

                    if (tercerosReps.size() > 0) {
                        // Se toma el primero de los terceros internos que aparecen
                        idTercero = Integer.parseInt(((TercerosValueObject) tercerosReps.elementAt(0)).getIdentificador());
                        terVO.setVersion(((TercerosValueObject) tercerosReps.elementAt(0)).getVersion());
                    } else {
                        //idTercero = setTercero(terVO, params); 
                        terVO.setNormalizado("0");
                        idTercero = terDAO.setTercero(terVO,adapt,con);                    
                        terVO.setVersion("1");
                    }
                
                if (m_Log.isDebugEnabled()) m_Log.debug("El valor del Id del usuario es : " + idTercero);
                terVO.setIdentificador(String.valueOf(idTercero));              
                max= idTercero;
        }
        
        if(max>0){
            terVO.setIdentificador(Integer.toString(max));
            
            Vector domicilios = terVO.getDomicilios();
            if(domicilios!=null && domicilios.size()==1){
                // Como se trata del alta de un tercero, sólo puede venir un domicilio
                DomicilioSimpleValueObject domicilio = (DomicilioSimpleValueObject)domicilios.get(0);
            
                
                m_Log.debug(" ============== domicilioNormalizado: " + txtNormalizado);
                int idExistente = terDAO.existeDomicilio(terVO,adapt,con);

                int idDomicilio = 0;
                if (idExistente > 0) {
                    // El domicilio ya existe, su id es idExistente
                    if(m_Log.isDebugEnabled()) m_Log.debug("EXISTE EL DOMICILIO");
                    txtNormalizado = "1";
                    domicilio.setNormalizado(txtNormalizado);
                    terVO.setIdDomicilio(String.valueOf(idExistente));
                    idDomicilio = terDAO.setDomicilioTercero(terVO, adapt, con);
                    if(idDomicilio>=1) altaTerceroFlexia = true;
                }else {
                    // El domicilio no existe
                    if(m_Log.isDebugEnabled()) m_Log.debug("NO EXISTE EL DOMICILIO");
                     txtNormalizado = "2";
                    idDomicilio = terDAO.setDomicilioTercero2(terVO, adapt, con);
                    if(idDomicilio>=1) altaTerceroFlexia = true;

                }// else

                domicilio.setIdDomicilio(String.valueOf(idDomicilio));
                terVO.setIdDomicilio(Integer.toString(idDomicilio));
                Vector domiciliosReturn = new Vector ();
                domiciliosReturn.add(domicilio);
                terVO.setDomicilios(domiciliosReturn);


            }// if

            try{

                DatosSuplementariosTerceroDAO.getInstance().grabarDatosSuplementarios(codOrganizacion,terVO.getIdentificador(),estructuraCampos,valoresCampos, adapt, con);
                
            } catch(TechnicalException e){
                e.printStackTrace();
                m_Log.error("Error al grabar los campos suplementarios del tercero");
                altaTerceroFlexia = false;
            }

         }else{
              throw new TechnicalException("setTercero: Error al intentar dar de alta un tercero en Flexia");
         }
        
        adapt.finTransaccion(con);



      }catch(Exception ce){
          m_Log.error("JDBC Technical problem " + ce.getMessage());
          try{
              adapt.rollBack(con);
              m_Log.debug("ejecución correcta del rollback");
          }catch(BDException e){
              m_Log.error("Error al realizar un rollback: " + e.getMessage());
          }

          if(ce instanceof ErrorTransaccionalTerceroExternoException){
                throw new ErrorTransaccionalTerceroExternoException(ce.getMessage());
          }else
                throw new TechnicalException("setTerceroExterno: Error al intentar dar de alta un tercero en Flexia  " + ce.getMessage());
            
      }finally{
          
          try{           
              adapt.devolverConexion(con);
          }catch(BDException e){
              m_Log.error("Error al cerrar la conexión a la BBDD en setTerceroNuevo");
          }
      }

      return terVO;
    }


    /**
     * Traduce etiquetas de error que están en la base de datos para mostrar directamente el texto ya traducido
     * @param etiquetas: listado con las etiquetas de mensajes de BBDD
     * @param codAplicacion: Código de la aplicación
     * @param codIdioma: Código de idiomia
     * @return ArrayList<String>
     */
    private ArrayList<String> getListaErroresTraducidos(ArrayList<String> etiquetas,int codAplicacion,int codIdioma){
        ArrayList<String> salida = new ArrayList<String>();
        for(int i=0;i<etiquetas.size();i++){

            salida.add(getDescripcionError(codAplicacion,codIdioma,etiquetas.get(i)));
        }

        return salida;
    }


    private String getDescripcionError(int codAplicacion,int codIdioma,String etiqueta){
        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
        traductor.setApl_cod(codAplicacion);
        traductor.setIdi_cod(codIdioma);
        return traductor.getDescripcion(etiqueta);
    }



    /**
     * Tratamiento de un error producido durante el alta del tercero externo
     * @param codigo: Código del error
     * @param salidaServicio: Salida del servicio
     * @param errores: Tabla hash que contiene la lista de errores
     */
     private Hashtable<Integer,ArrayList<ErrorSistemaTerceroExternoVO>> grabarError(Integer codigo,ArrayList<ErrorSistemaTerceroExternoVO> lista_error,Hashtable<Integer,ArrayList<ErrorSistemaTerceroExternoVO>> errores){

        try{
            ArrayList<ErrorSistemaTerceroExternoVO> listaErroresTipo = null;
            if(errores.containsKey(codigo))
                listaErroresTipo = errores.get(codigo);
            else
                listaErroresTipo = new ArrayList<ErrorSistemaTerceroExternoVO>();

            listaErroresTipo.addAll(lista_error);
            errores.put(codigo, listaErroresTipo);
        }catch(Exception e){
            e.printStackTrace();
        }
        return errores;
    }



    /**
     * Actualiza el código externo de un tercero y si se ha pasado el documento, también se actualiza el documento
     * @param codTercero: Código del tercero
     * @param codigoExterno: Código externo del tercero
     * @param documento: Documento
     * @param params: Parámetros de conexión a la BBDD
     * @return Un boolean
     */
    public boolean actualizarCodigoExternoTercero(String codTercero,String codigoExterno,String documento,String[] params){
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        boolean exito = false;

        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            exito = TercerosDAO.getInstance().actualizarCodigoExternoTercero(codTercero,codigoExterno,documento,con);
            if(exito)
                adapt.finTransaccion(con);
            else
                adapt.rollBack(con);

        }catch(BDException e){
            e.printStackTrace();
            try{
                adapt.rollBack(con);
            }catch(Exception f){
                m_Log.error("Error al cerrar la transacción: " + f.getMessage());
            }
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }

        return exito;
    }

    
    public TercerosValueObject existeTerceroExterno(String codigoExterno,String documento, String[] params) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("=====>existeTerceroExterno");
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        TercerosValueObject terExtVO = null;
        TercerosDAO terDAO = TercerosDAO.getInstance();
        int res[] = new int[2];
        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            m_Log.debug("Usando persistencia manual");

            terExtVO = terDAO.existeTerceroExterno(codigoExterno,documento, con);
            // queremos estar informados de cuando este metodo finaliza
            m_Log.debug("existeTerceroExterno end <=========");
        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return terExtVO;
    } 
    
    public ArrayList<TercerosValueObject> getInteresadosExpediente(String codMunicipio,String numExpediente,String codProcedimiento,String ejercicio, String[] params){
        if(m_Log.isDebugEnabled()) m_Log.debug("getInteresadosExpediente() : BEGIN");
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        TercerosValueObject terExtVO = null;
        TercerosDAO terDAO = TercerosDAO.getInstance();
        ArrayList<TercerosValueObject> terceros = new ArrayList<TercerosValueObject>();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            terceros = terDAO.getInteresadosExpediente(codMunicipio, numExpediente, codProcedimiento, ejercicio, con, params);
        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
        }
        finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("getInteresadosExpediente() : END");
        return terceros;
    }//getInteresadosExpediente
    
    
    
    /**
     * Comprueba si un determinado domicilio está asignado a más de un tercero
     * @param codDomicilio: Código del domicilio
     * @param params: parametros de conexión a la BBDD
     * @return Un boolean
     */
    public boolean estaAsignadoDomicilioVariosTerceros(String codDomicilio,String[] params){
        boolean exito = false;
        Connection con  = null;
        AdaptadorSQLBD adapt = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = TercerosDAO.getInstance().estaAsignadoDomicilioVariosTerceros(codDomicilio, con);
            
            
        }catch(BDException e){
            m_Log.error("Error al recuperar conexión a la BBDD: " + e.getMessage());
        }catch(SQLException e){
            m_Log.error("Error al verificar si el domicilio está asignado a más de un tercero: " + e.getMessage());
        }
        finally{
            try{
                if(con!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }        
        return exito;
    }
    
    /**
     * Comprueba si un determinado domicilio está asignado a un registro o expediente
     * @param codDomicilio: Código del domicilio
     * @param params: parametros de conexión a la BBDD
     * @return Un boolean
     */
    public boolean existeDomEnRegistroExpediente(String codDomicilio,String[] params){
        boolean exito = false;
        Connection con  = null;
        AdaptadorSQLBD adapt = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = TercerosDAO.getInstance().existeDomEnRegistroExpediente(codDomicilio, con);
            
            
        }catch(BDException e){
            m_Log.error("Error al recuperar conexión a la BBDD: " + e.getMessage());
        }catch(SQLException e){
            m_Log.error("Error al verificar si el domicilio está asignado a algún registro o expediente: " + e.getMessage());
        }
        finally{
            try{
                if(con!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }        
        return exito;
    }
    
    public int modificarDomicilioCreandoNuevo(TercerosValueObject terVO,String codDomicilioModificado,String[] params){
        int salida = -1;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            salida=TercerosDAO.getInstance().modificarDomicilioCreandoNuevo(terVO, codDomicilioModificado, con,adapt);
            
            
            adapt.finTransaccion(con);
            
        }catch(BDException e){
            m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                m_Log.error("Error al realizar rollback: " + f.getMessage());
            }
        }catch(TechnicalException e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                m_Log.error("Error al realizar rollback: " + f.getMessage());
            }
        }
        finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return salida;
    }
    
    public Boolean esTerceroFormatoNifValido(String codTercero, String[] params) {
        // queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("comprobar si el tercero tiene doc de tipo NIF debe de tener primer apellido");
        Boolean valido = Boolean.FALSE;
        try {
            m_Log.debug("Usando persistencia manual, esTerceroFormatoNifValido");
            valido = TercerosDAO.getInstance().esTerceroFormatoNifValido(codTercero, params);
            // queremos estar informados de cuando este metodo finaliza
            m_Log.debug("esTerceroFormatoNifValido");
            return valido;
        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            return valido;
        }
    }
}