package es.altia.agora.business.gestionInformes.persistence;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.agora.business.gestionInformes.persistence.manual.FichaInformeDAO;
import es.altia.agora.business.gestionInformes.FichaInformeValueObject;
import es.altia.agora.business.gestionInformes.InformeValueObject;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.CriteriosValueObject;
import es.altia.agora.interfaces.user.web.gestionInformes.FachadaDatosInformes;
import es.altia.agora.interfaces.user.web.gestionInformes.FactoriaDatosInformes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Vector;

public class FichaInformeManager {

  // Mi propia instancia usada en el metodo getInstance.
  private static FichaInformeManager instance = null;

  protected static Config m_ConfigTechnical;
  protected static Config m_ConfigCommon;
  protected static Config m_ConfigError;
  protected static Log m_Log =
          LogFactory.getLog(FichaInformeManager.class.getName());


  protected FichaInformeManager() {
    m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    m_ConfigError = ConfigServiceHelper.getConfig("error");
    m_ConfigCommon = ConfigServiceHelper.getConfig("common");
  }


  /**
  * Factory method para el <code>Singelton</code>.
  * @return La unica instancia de FichaExpedienteManager
  */
  public static FichaInformeManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
    if (instance == null) {
        synchronized(FichaInformeManager.class) {
        if (instance == null)
            instance = new FichaInformeManager();
      }
    }
    return instance;
  }

    public Vector getListaCriterios(String plantilla, String[] params) {
      Vector lista = new Vector();
      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getListaCriterios");

      try{
        lista = FichaInformeDAO.getInstance().getListaCriterios(plantilla, params);
      }catch (Exception e) {
        e.printStackTrace();
      }
      return lista;
    }

    public Vector getDatosExpedientes(SolicitudInformeValueObject siVO, String codOrganizacion, String origen, String[] params) throws Exception {
        if(m_Log.isDebugEnabled()) m_Log.debug("getDatosExpedientes() BEGIN");
        Vector lista = new Vector();
        try{
            FachadaDatosInformes fachadaDatos = FactoriaDatosInformes.getImpl(origen);
            lista = fachadaDatos.getDatosExpedientes(siVO, codOrganizacion, params);
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        if(m_Log.isDebugEnabled()) m_Log.debug("getDatosExpedientes() END");
        return lista;
    }//getDatosExpedientes

    public int altaInforme(FichaInformeValueObject fiVO, String[] params) {
      int resultado = 0;
        //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("altaInforme");

      try{
        resultado = FichaInformeDAO.getInstance().altaInforme(fiVO, params);
      }catch (Exception e) {
        resultado = -1;
        e.printStackTrace();
      }
      return resultado;
    }

    public int eliminarInforme(String codigoPlantilla, String[] params) {
        int resultado = 0;
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("Dentro de eliminarInforme(FichaInformeManager)");

        try{
          resultado = FichaInformeDAO.getInstance().eliminarInforme(codigoPlantilla, params);
        }catch (Exception e) {
          resultado = -1;
          e.printStackTrace();
        }
        m_Log.debug("Salimos de eliminarInforme(FichaInformeManager)");
        return resultado;
      }
    
    
    
    public FichaInformeValueObject cargarInforme(String plantilla, String codMunicipio, String[] params) {
      FichaInformeValueObject fiVO = new FichaInformeValueObject();
        //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargarInforme");

      try{
        fiVO = FichaInformeDAO.getInstance().cargarInforme(plantilla, codMunicipio, params);
      }catch (Exception e) {
        fiVO = null;
        e.printStackTrace();
      }
      return fiVO;
    }

    public int grabarInforme(FichaInformeValueObject fiVO, String[] params) {
      int resultado = 0;
      m_Log.debug("grabarInforme");
      try{
        resultado = FichaInformeDAO.getInstance().grabarInforme(fiVO, params);
      }catch (Exception e) {
        resultado = -1;
        e.printStackTrace();
      }
      return resultado;
    }

    public int insertarInforme(InformeValueObject informeVO, String[] params) throws Exception{
        m_Log.debug("insertarInforme");
        return FichaInformeDAO.getInstance().insertarInforme(informeVO, params);       
    }

    public boolean yaExiste(String nombrePlantilla, String[] params) {
      boolean resultado = false;
      m_Log.debug("yaExiste");
      try{
        resultado = FichaInformeDAO.getInstance().yaExiste(nombrePlantilla, params);
      }catch (Exception e) {
        e.printStackTrace();
        return resultado;
      }
      return resultado;
    }

    public boolean hayDatosEnCuerpoInforme(String codInforme, String[] params) {
      boolean resultado = false;
      m_Log.debug("hayDatosEnCuerpoInforme");
      try{
        resultado = FichaInformeDAO.getInstance().hayDatosEnCuerpoInforme(codInforme, params);
      }catch (Exception e) {
        e.printStackTrace();
        return resultado;
      }
      return resultado;
    }

    public boolean camposDisponibles(String codPlantilla, String codProcedimiento, String descAmbito, UsuarioValueObject usuario, 
            String[] paramsEntornoPruebas, String[] params) {
      boolean resultado = false;
      m_Log.debug("camposDisponibles");
      try{
          FichaInformeValueObject fiVO = new FichaInformeValueObject();
          fiVO.setCodProcedimiento(codProcedimiento);
          fiVO.setCodMunicipio(String.valueOf(usuario.getOrgCod()));
          fiVO.setDescAmbito(descAmbito);
          FachadaDatosInformes fachadaDatos = FactoriaDatosInformes.getImpl(fiVO.getDescAmbito());
          Vector listaCamposDisponibles = fachadaDatos.getListaCampos(fiVO, params);
          Vector listaCamposSeleccionados = FichaInformeDAO.getInstance().getListaCamposSeleccionadosInforme(codPlantilla, paramsEntornoPruebas);
          boolean continuar = true;
          boolean encontrado;
          int i = 0;
          int j = 0;
          while (continuar && i<listaCamposSeleccionados.size()) {
              CampoValueObject campoSeleccionadoVO = (CampoValueObject) listaCamposSeleccionados.get(i);
              m_Log.debug("CAMPO BUSCADO " + campoSeleccionadoVO);
              j = 0;
              encontrado = false;
              while (!encontrado && j<listaCamposDisponibles.size()) {
                  CampoValueObject campoDisponibleVO = (CampoValueObject) listaCamposDisponibles.get(j);
                  encontrado = ((campoSeleccionadoVO.getOrigen().equalsIgnoreCase(campoDisponibleVO.getOrigen())) &&
                                (campoSeleccionadoVO.getTitulo().equalsIgnoreCase(campoDisponibleVO.getTitulo())) &&
                                (campoSeleccionadoVO.getTabla().equalsIgnoreCase(campoDisponibleVO.getTabla())));
                  j++;
              }
              m_Log.debug(campoSeleccionadoVO.getOrigen() + " encontrado " + encontrado  + "\n\n");
              continuar = encontrado;
              i++;
          }
          resultado = continuar;
          m_Log.debug("CAMPOS DISPONIBLES DEVUELVE "+resultado);
      }catch (Exception e) {
        e.printStackTrace();
        return resultado;
      }
      return resultado;
    }


public boolean criteriosDisponibles (String codPlantilla, String codProcedimiento, String descAmbito, UsuarioValueObject usuario,
            String[] paramsEntornoPruebas, String[] params) {
      boolean resultado = false;
      m_Log.debug("criteriosDipsonibles");
      try{
          FichaInformeValueObject fiVO = new FichaInformeValueObject();
          fiVO.setCodProcedimiento(codProcedimiento);
          fiVO.setCodMunicipio(String.valueOf(usuario.getOrgCod()));
          fiVO.setDescAmbito(descAmbito);
          FachadaDatosInformes fachadaDatos = FactoriaDatosInformes.getImpl(fiVO.getDescAmbito());
          Vector listaCamposDisponibles = fachadaDatos.getListaCriteriosDisponibles(fiVO, params);
          Vector listaCamposSeleccionados = FichaInformeDAO.getInstance().getListaCriterios(codPlantilla, paramsEntornoPruebas);
          boolean continuar = true;
          boolean encontrado;
          int i = 0;
          int j = 0;
          while (continuar && i<listaCamposSeleccionados.size()) {
              CriteriosValueObject campoSeleccionadoVO = (CriteriosValueObject) listaCamposSeleccionados.get(i);
              m_Log.debug("CRITERIO BUSCADO " + campoSeleccionadoVO);
              j = 0;
              encontrado = false;
              while (!encontrado && j<listaCamposDisponibles.size()) {
                  CampoValueObject campoDisponibleVO = (CampoValueObject) listaCamposDisponibles.get(j);
                  encontrado = ((campoSeleccionadoVO.getOrigen().equalsIgnoreCase(campoDisponibleVO.getOrigen())) &&
                                (campoSeleccionadoVO.getTitulo().equalsIgnoreCase(campoDisponibleVO.getTitulo())) &&
                                (campoSeleccionadoVO.getTabla().equalsIgnoreCase(campoDisponibleVO.getTabla())));
                  j++;
              }
              m_Log.debug(campoSeleccionadoVO.getOrigen() + " encontrado " + encontrado  + "\n\n");
              continuar = encontrado;
              i++;
          }
          resultado = continuar;
          m_Log.debug("CRITERIOS DISPONIBLES DEVUELVE "+resultado);
      }catch (Exception e) {
        e.printStackTrace();
        return resultado;
      }
      return resultado;
    }


public boolean permisosDisponibles(String codPlantilla, String codProcedimiento, String descAmbito, UsuarioValueObject usuario,
            String[] paramsEntornoPruebas, String[] params) {
      boolean resultado = false;
      m_Log.debug("camposDisponibles");
      try{

          if (descAmbito.equalsIgnoreCase("REXISTRO")||descAmbito.equalsIgnoreCase("REGISTRO")) descAmbito ="GENERAL";
          FichaInformeValueObject fiVO = new FichaInformeValueObject();
          fiVO.setCodProcedimiento(codProcedimiento);
          fiVO.setCodMunicipio(String.valueOf(usuario.getOrgCod()));
          fiVO.setDescAmbito(descAmbito);
          FachadaDatosInformes fachadaDatos = FactoriaDatosInformes.getImpl(fiVO.getDescAmbito());
          Vector listaPermisosDisponibles = fachadaDatos.getListaPermisos(params);
          Vector listaPermisosSeleccionados = FichaInformeDAO.getInstance().
                  getListaPermisosSeleccionadosInforme(codPlantilla, paramsEntornoPruebas);
          boolean continuar = true;
          boolean encontrado;
          int i = 0;
          int j = 0;
          while (continuar && i<listaPermisosSeleccionados.size()) {
              String permisoSeleccionado = (String) listaPermisosSeleccionados.get(i);
              m_Log.debug("CAMPO BUSCADO " + permisoSeleccionado);
              j = 0;
              encontrado = false;
              while (!encontrado && j<listaPermisosDisponibles.size()) {
                  String permisoDisponible = (String) listaPermisosDisponibles.get(j);
                  m_Log.debug ("permisos disponible mirado="+permisoDisponible);
                  encontrado = ((permisoSeleccionado.equalsIgnoreCase(permisoDisponible)));
                  j++;
              }
              continuar = encontrado;
              i++;
          }
          resultado = continuar;
          m_Log.debug("PERMISOS DISPONIBLES DEVUELVE "+resultado);
      }catch (Exception e) {
        e.printStackTrace();
        return resultado;
      }
      return resultado;
    }

    public int publicarInforme(String codInforme, String publicar, String[] params) {
      int resultado = 0;
      m_Log.debug("publicarInforme");
      try{
        resultado = FichaInformeDAO.getInstance().publicarInforme(codInforme, publicar, params);
      }catch (Exception e) {
        resultado = -1;
        e.printStackTrace();
      }
      return resultado;
    }

    public Vector getCamposOrdenadosInforme(String plantilla, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("getCamposOrdenadosInforme() : BEGIN");
        Vector lista = new Vector();
        try{
            lista = FichaInformeDAO.getInstance().getCamposOrdenadosInforme(plantilla, params);
        }catch (Exception e) {
            e.printStackTrace();
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("getCamposOrdenadosInforme() : END");
        return lista;
    }//getCamposOrdenadosInforme

    public InformeValueObject getEstructuraInforme( String[] params, String codPlantilla, String codSolicitud) {
        if(m_Log.isDebugEnabled())  m_Log.debug("getEstructuraInforme() : BEGIN");
        InformeValueObject informeVO = new InformeValueObject();
        try{
            informeVO = FichaInformeDAO.getInstance().getEstructuraInforme(params, codPlantilla, codSolicitud);
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(m_Log.isDebugEnabled())  m_Log.debug("getEstructuraInforme() : END");
        return informeVO;
    }//getEstructuraInforme

    public InformeValueObject getInforme( String[] params, String codPlantilla) throws Exception{
        InformeValueObject informeVO;
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("getInforme");
        informeVO = FichaInformeDAO.getInstance().getInforme(params, codPlantilla);
        return informeVO;
    }

}
