// NOMBRE DEL PAQUETE
package es.altia.agora.business.terceros.mantenimiento.persistence;

// PAQUETES IMPORTADOS
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.*;
import es.altia.agora.business.terceros.mantenimiento.CondicionesBusquedaViaVO;
import es.altia.agora.business.util.*;
import es.altia.agora.webservice.via.AccesoBusquedaVia;
import es.altia.common.service.config.*;
import es.altia.util.conexion.BDException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Vector;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: ViasManager</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @version 1.0
 */

public class ViasManager  {

    private final static String ID_SERVICIO_POR_DEFECTO = "SGE";

  private static ViasManager instance = null;
  protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
  protected static Config m_ConfigError; // Para los mensajes de error localizados
  protected static Log log =
          LogFactory.getLog(ViasManager.class.getName());

  protected ViasManager() {
    // Queremos usar el fichero de configuración technical
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    // Queremos tener acceso a los mensajes de error localizados
    m_ConfigError = ConfigServiceHelper.getConfig("error");
  }

  public static ViasManager getInstance() {
    // Si no hay una instancia de esta clase tenemos que crear una
    if (instance == null) {
      // Necesitamos sincronización aquí para serializar (no multithread)
      // las invocaciones a este metodo
      synchronized(ViasManager.class) {
        if (instance == null) {
          instance = new ViasManager();
        }
      }
    }
    return instance;
  }
  
  public Vector getListaNumeraciones(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaNumeracionesValue");
    ViasDAO viasDAO = ViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = viasDAO.getListaNumeraciones(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaNumeracionesValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector getListaTramos(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaTramosValue");
    ViasDAO viasDAO = ViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = viasDAO.getListaTramos(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaTramosValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public boolean existeTramero(String[] params){
      // queremos estar informados de cuando este metodo es ejecutado
      log.debug("existeTramero");
      ViasDAO viasDAO = ViasDAO.getInstance();
      boolean resultado = false;
      try{
        log.debug("Usando persistencia manual");
        resultado = viasDAO.existeTramero(params);
        // queremos estar informados de cuando este metodo finaliza
        log.debug("existeTramero");
        return resultado;
      }catch(Exception ce){
        log.error("JDBC Technical problem " + ce.getMessage());
        return resultado;
      }
  }

  public Vector getListaVias(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaViasValue");
    ViasDAO viasDAO = ViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = viasDAO.getListaVias(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaViasValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  public Vector getListaViasBusqueda(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaViasValue");
    ViasDAO viasDAO = ViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = viasDAO.getListaViasBusqueda(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaViasValue");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }
  
  /* Viales 06/06/05 */
  public Vector getListaViasBusquedaGeneral(String[] params,GeneralValueObject gVO){
	  // queremos estar informados de cuando este metodo es ejecutado
	  log.debug("getListaViasBusquedaGeneral");
	  ViasDAO viasDAO = ViasDAO.getInstance();
	  Vector resultado = new Vector();
	  try{
		log.debug("Usando persistencia manual");
		resultado = viasDAO.getListaViasBusquedaGeneral(params,gVO);
		// queremos estar informados de cuando este metodo finaliza
		log.debug("getListaViasBusquedaGeneral");
		return resultado;
	  }catch(Exception ce){
		log.error("JDBC Technical problem " + ce.getMessage());
		return resultado;
	  }
	}
	/* Fin viales 06/06/05 */
	
    public HashMap<String, Collection> buscarVias(CondicionesBusquedaViaVO condsBusqueda, String[] params) {

        log.debug("ViasManager --> buscarVias()");
        Config m_ConfigVias = ConfigServiceHelper.getConfig("Vias");

        /*
        * Buscamos los identificadores de los servicios de busqueda instalados en el fichero techserver.
        * El resultado de la llamada sera una cadena con todos los identificadores separados por ';'.
        * Si no se lee ningun identificador del fichero de configuración, se utiliza el servicio de busqeuda por
        * defecto (Identificador = SGE).
        */
        String prefijoOrg = "BusquedaVia/" + condsBusqueda.getCodOrganizacion() + "/";
        String strIdsServicios = m_ConfigVias.getString(prefijoOrg + "serviciosDisp");
        if (strIdsServicios == null || "".equals(strIdsServicios.trim())) {
            strIdsServicios = ID_SERVICIO_POR_DEFECTO;
            log.error("NO SE HA PODIDO RECUPERAR UNA CADENA DE IDENTIFICADORES PARA LOS SERVICIOS DE BUSQUEDA DE " +
                    "VIAS. INICIAMOS CON EL SERVICIO DE BUSQEUDA POR DEFECTO");
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
            log.error("HA OCURRIDO UN ERROR AL PROCESAR LA CADENA DE IDENTIFICADORES DE SERVICIOS DE BUSQUEDA DE " +
                    "VIAS. INICIAMOS CON EL SERVICIO DE BUSQEUDA POR DEFECTO");
	  }

        AccesoBusquedaVia busquedaTercero = new AccesoBusquedaVia(idsServiciosBusq, prefijoOrg);
        return busquedaTercero.buscarVias(condsBusqueda, params);
	}


  public Vector getListaVias1(String[] params,GeneralValueObject gVO){
    // queremos estar informados de cuando este metodo es ejecutado
    log.debug("getListaVias1Value");
    ViasDAO viasDAO = ViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      log.debug("Usando persistencia manual");
      resultado = viasDAO.getListaVias1(params,gVO);
      // queremos estar informados de cuando este metodo finaliza
      log.debug("getListaVias1Value");
      return resultado;
    }catch(Exception ce){
      log.error("JDBC Technical problem " + ce.getMessage());
      return resultado;
    }
  }

  public Vector eliminarVia(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("eliminarVia");
    ViasDAO viasDAO = ViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = viasDAO.eliminarVia(gVO,params);
    }catch (Exception e){
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("eliminarVia");
    return resultado;
  }

  public Vector modificarVia(GeneralValueObject gVO, String[] params){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarVia");
    ViasDAO viasDAO = ViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = viasDAO.modificarVia(gVO, params);
    }catch(Exception e){
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarVia");
    return resultado;
  }

  public Vector altaVia(GeneralValueObject gVO, String[] params) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("altaVia");
    ViasDAO viasDAO = ViasDAO.getInstance();
    Vector resultado = new Vector();
    try{
      resultado = viasDAO.altaVia(gVO,params);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("altaVia");
    return resultado;
  }

  public String modificarViaTerritorio(String[] params,GeneralValueObject gVO){
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("modificarViaTerritorio");
    ViasDAO viasDAO = ViasDAO.getInstance();
    String resultado = "SI";
    try{
      resultado = viasDAO.modificarViaTerritorio(params,gVO);
    }catch(Exception e){
      resultado = "NO";
      log.error("Excepcion capturada: " + e.toString());
    }
    log.debug("modificarViaTerritorio");
    return resultado;
  }

  public String existeAplicacion(String[] params,String codigo) {
    //queremos estar informados de cuando este metodo es ejecutado
    log.debug("existeAplicacion");
    ViasDAO viasDAO = ViasDAO.getInstance();
    String resultado = "NO";
    try{
      resultado = viasDAO.existeAplicacion(params,codigo);
    }catch (Exception e) {
      log.error("Excepción capturada: " + e.toString());
    }
    log.debug("existeAplicacion");
    return resultado;
  }
  
  /* Cambio combo viales */    
  public Vector getListaViasESIs(String[] params,GeneralValueObject gVO){
	  // queremos estar informados de cuando este metodo es ejecutado
	  log.debug("getListaViasESIs");
	  ViasDAO viasDAO = ViasDAO.getInstance();
	  Vector resultado = new Vector();
	  try{
		resultado = viasDAO.getListaViasESIs(params,gVO);		
		log.debug("getListaViasESIs");
		return resultado;
	  }catch(Exception ce){
		log.error("JDBC Technical problem " + ce.getMessage());
		return resultado;
	  }
	}

	public Vector getListaViasSolas(String[] params,GeneralValueObject gVO){
		  // queremos estar informados de cuando este metodo es ejecutado
		  log.debug("getListaViasSolas");
		  ViasDAO viasDAO = ViasDAO.getInstance();
		  Vector resultado = new Vector();
		  try{
			resultado = viasDAO.getListaViasSolas(params,gVO);		
			log.debug("getListaViasSolas");
			return resultado;
		  }catch(Exception ce){
			log.error("JDBC Technical problem " + ce.getMessage());
			return resultado;
		  }
		}

	/* Fin cambio combo viales */

	public boolean insertVia(GeneralValueObject gVO, String[] params) {
		//queremos estar informados de cuando este metodo es ejecutado
		log.debug("insertVia");
		ViasDAO viasDAO = ViasDAO.getInstance();
		boolean resultado = false;
		try{
		  resultado = viasDAO.insertVia(gVO,params);
		}catch (Exception e) {
		  log.error("Excepción capturada: " + e.toString());
		}
		log.debug("insertVia");
		return resultado;
	  }

    /**
     * Comprueba que no existe la via en la base de datos y la inserta. Si existe nos da su id
     */
    public int altaViaNoRepetido(GeneralValueObject gVO, String[] params) {
        log.debug("comprobamos e insertamos via");
        ViasDAO viasDAO = ViasDAO.getInstance();
        int resultado = 0;
        try{
            resultado = viasDAO.altaViaNoRepetido(gVO,params);
        }catch(Exception e){
            log.error("Exceptión capturada:" + e.toString());
        }
        log.debug("comprobacion acabada");
		return resultado;
	  }

    public Vector getViasByDescAndProvinciaAndMunicipioGrouped(String[] params,GeneralValueObject gVO) {
        // queremos estar informados de cuando este metodo es ejecutado
        log.debug("getViasByDescAndProvinciaAndMunicipio");
        Vector resultado = new Vector();
        ViasDAO viasDAO = ViasDAO.getInstance();
        try{
            log.debug("Usando persistencia manual");
            resultado = viasDAO.getViasByDescAndProvinciaAndMunicipioGrouped(params, gVO);
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
        }
        return resultado;
    }

    public Vector getViasByDescAndProvinciaAndMunicipio(String[] params,GeneralValueObject gVO) {
        // queremos estar informados de cuando este metodo es ejecutado
        log.debug("getViasByDescAndProvinciaAndMunicipio");
        Vector resultado = new Vector();
        ViasDAO viasDAO = ViasDAO.getInstance();
        try{
            log.debug("Usando persistencia manual");
            resultado = viasDAO.getViasByDescAndProvinciaAndMunicipio(params, gVO);
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
        }
        return resultado;
    }

    public void depurarVias(String[] params, GeneralValueObject gVO, String[] codigosVias) throws BDException {
        // queremos estar informados de cuando este metodo es ejecutado
        log.debug("depurarVias");
        ViasDAO viasDAO = ViasDAO.getInstance();
        log.debug("Usando persistencia manual");
        viasDAO.depurarVias(params, gVO, codigosVias);
    }
}