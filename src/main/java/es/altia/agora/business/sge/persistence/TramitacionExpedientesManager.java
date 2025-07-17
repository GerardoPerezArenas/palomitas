package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UsuariosGruposDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.common.exception.*;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.CampoSuplementarioFicheroVO;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.SiguienteTramiteTO;
import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.agora.business.sge.persistence.manual.DefinicionTramitesDAO;
import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.business.SWFirmaDocManager;
import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.business.SWFirmaDocRelacionManager;

import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionOperacionModuloIntegracionException;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;


public class TramitacionExpedientesManager{
  /**
    * Código que sigue el patrón de diseño <code>Singleton</code>
    * Los métodos de negocio gestionan que la persistencia sea manual o automática
    * Es protected, por lo que la única manera de instanciar esta clase es usando el factory method <code>getInstance</code>
    */

  protected TramitacionExpedientesManager() {
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver"); // Fichero de configuracion technical.
        m_ConfigError = ConfigServiceHelper.getConfig("error"); // Mensajes de error localizados.
        m_ConfigCommon = ConfigServiceHelper.getConfig("common"); // Propiedades comunes de configuracion.
  }


  public TramitacionExpedientesValueObject cargarDatos(TramitacionExpedientesValueObject tEVO, String[] params)
  throws AnotacionRegistroException {

    m_Log.debug("cargarDatos");
    try {
      m_Log.debug("Usando persistencia manual");
      tEVO = TramitacionExpedientesDAO.getInstance().cargarDatos(tEVO,params);
      m_Log.debug("datos cargados");
    } catch (AnotacionRegistroException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new AnotacionRegistroException("Problema técnico de JDBC " + te.getMessage());
    } catch(Exception e) {
      e.printStackTrace();
    }
    m_Log.debug("Fin cargarDatos()");
    return tEVO;
  }

  public TramitacionExpedientesValueObject getListaDocumentosTramite(TramitacionExpedientesValueObject tEVO, String[] params)
  throws AnotacionRegistroException {

    m_Log.debug("getListaDocumentosTramite");
    try {
      m_Log.debug("Usando persistencia manual");
      tEVO = TramitacionExpedientesDAO.getInstance().getListaDocumentosTramite(tEVO,params);
      m_Log.debug("getListaDocumentosTramite");
    } catch (AnotacionRegistroException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new AnotacionRegistroException("Problema técnico de JDBC " + te.getMessage());
    } catch(Exception e) {
      e.printStackTrace();
    } finally {
      m_Log.debug("getListaDocumentosTramite");
      return tEVO;
    }
  }

  public Vector<RegistroValueObject> cargarDocumentosTramite(TramitacionExpedientesValueObject tVO, String[] params)
      throws AnotacionRegistroException{

    Vector<RegistroValueObject> docs = new Vector<RegistroValueObject>();

    try {
        docs = TramitacionExpedientesDAO.getInstance().cargarDocumentosTramite(tVO,params);
    } catch (AnotacionRegistroException te) {
        throw new AnotacionRegistroException("Problema técnico de JDBC " + te.getMessage());
    } catch(Exception e) {
        e.printStackTrace();
    } finally {
        return docs;
    }
  }

  public void getListaDocumentosCronologia(TramitacionExpedientesValueObject tEVO, String[] params)
  throws AnotacionRegistroException {

      m_Log.debug("getListaDocumentosCronologia");
      try {
          if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
              m_Log.debug("Llamando al Servicio Web.");
              SWFirmaDocManager.getInstance(params).getListaDocumentosCronologia(tEVO);
              m_Log.debug("SWFirmaDocManager.getListaDocumentosCronologia");
          } else {
              m_Log.debug("Usando persistencia manual");
              TramitacionExpedientesDAO.getInstance().getListaDocumentosCronologia(tEVO,params);
              m_Log.debug("getListaDocumentosCronologia");
          }
      } catch (AnotacionRegistroException te) {
          m_Log.error("JDBC Technical problem " + te.getMessage());
          throw new AnotacionRegistroException("Problema técnico de JDBC " + te.getMessage());
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          m_Log.debug("getListaDocumentosCronologia");
      }

  }

    public void getListaDocumentosRelacionCronologia(TramitacionExpedientesValueObject tEVO, String[] params)
    throws AnotacionRegistroException {

        m_Log.debug("getListaDocumentosCronologia");
        try {
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                m_Log.debug("Llamando al Servicio Web.");
                SWFirmaDocRelacionManager.getInstance(params).getListaDocumentosRelacionCronologia(tEVO);
                m_Log.debug("SWFirmaDocManager.getListaDocumentosCronologia");
            } else {
                m_Log.debug("Usando persistencia manual");
                TramitacionExpedientesDAO.getInstance().getListaDocumentosRelacionCronologia(tEVO,params);
                m_Log.debug("getListaDocumentosCronologia");
            }
        } catch (AnotacionRegistroException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + te.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            m_Log.debug("getListaDocumentosCronologia");
        }

    }


    public int bloquearTramite(TramitacionExpedientesValueObject tEVO,Integer usuario, String[] params)
    {
      int res=0;
      m_Log.debug("bloquearTramite");
      try {
        m_Log.debug("Usando persistencia manual");
        res = TramitacionExpedientesDAO.getInstance().bloquearTramite(tEVO, usuario, params);
        m_Log.debug("bloquearTramite");
      } catch (TramitacionException te) {
        res = -1;
        te.printStackTrace();
        m_Log.error("JDBC Technical problem " + te.getMessage());
        //throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
      } catch(Exception e) {
        res = -1;
        e.printStackTrace();
        //throw new TramitacionException("Problema técnico de JDBC " + e.getMessage());
      } finally {
        m_Log.debug("bloquearTramite");
        return res;
      }
    }

    public int desbloquearTramite(TramitacionExpedientesValueObject tEVO, Integer usuario, String[] params)
    {
      int res=0;
      m_Log.debug("desbloquearTramite");
      try {
        m_Log.debug("Usando persistencia manual");
        res = TramitacionExpedientesDAO.getInstance().desbloquearTramite(tEVO, usuario, params);
        m_Log.debug("desbloquearTramite");
      } catch (TramitacionException te) {
        res = -1;
        te.printStackTrace();
        m_Log.error("JDBC Technical problem " + te.getMessage());
        //throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
      } catch(Exception e) {
        res = -1;
        e.printStackTrace();
        //throw new TramitacionException("Problema técnico de JDBC " + e.getMessage());
      } finally {
        m_Log.debug("desbloquearTramite");
        return res;
      }
    }

  public int grabarTramite(TramitacionExpedientesValueObject tEVO, String[] params)
  //throws TramitacionException
  {
    int res=0;
    m_Log.debug("grabarTramite");
    try {
      m_Log.debug("Usando persistencia manual");
      res = TramitacionExpedientesDAO.getInstance().grabarTramite(tEVO,params);
      m_Log.debug("grabarTramite");
    } catch (TramitacionException te) {
      res = -1;
      te.printStackTrace();
      m_Log.error("JDBC Technical problem " + te.getMessage());
      //throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch(Exception e) {
      res = -1;
      e.printStackTrace();
      //throw new TramitacionException("Problema técnico de JDBC " + e.getMessage());
    } finally {
      m_Log.debug("grabarTramite");
      return res;
    }
  }

  public int grabarLocalizacion(GeneralValueObject gVO, String[] params)
  //throws TramitacionException
  {
    int res=0;
    m_Log.debug("grabarLocalizacion");
    try {
      m_Log.debug("Usando persistencia manual");
      res = TramitacionExpedientesDAO.getInstance().grabarLocalizacion(gVO,params);
      m_Log.debug("grabarLocalizacion");
    } catch (TramitacionException te) {
      res = -1;
      te.printStackTrace();
      m_Log.error("JDBC Technical problem " + te.getMessage());
      //throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch(Exception e) {
      res = -1;
      e.printStackTrace();
      //throw new TramitacionException("Problema técnico de JDBC " + e.getMessage());
    } finally {
      m_Log.debug("grabarLocalizacion");
      return res;
    }
  }

  public int eliminarLocalizacion(GeneralValueObject gVO, String[] params)
  //throws TramitacionException
  {
    int res=0;
    m_Log.debug("eliminarLocalizacion");
    try {
      m_Log.debug("Usando persistencia manual");
      res = TramitacionExpedientesDAO.getInstance().eliminarLocalizacion(gVO,params);
      m_Log.debug("eliminarLocalizacion");
    } catch (TramitacionException te) {
      res = -1;
      te.printStackTrace();
      m_Log.error("JDBC Technical problem " + te.getMessage());
      //throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch(Exception e) {
      res = -1;
      e.printStackTrace();
      //throw new TramitacionException("Problema técnico de JDBC " + e.getMessage());
    } finally {
      m_Log.debug("eliminarLocalizacion");
      return res;
    }
  }

  public int eliminarDocumentoCRD(TramitacionExpedientesValueObject tEVO, String[] params)
  /*throws TramitacionException*/ {

      int res=0;
      m_Log.debug("TramitacionExpedienteManager.eliminarDocumentoCRD");

      try {
          if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
              m_Log.debug("Llamando al Servicio Web de Firmadoc");
              res = SWFirmaDocManager.getInstance(params).eliminarDocumentoCRD(tEVO);
              m_Log.debug("SWFirmaDocManager.eliminarDocumentoCRD");
          } else {
              m_Log.debug("Usando persistencia manual");
              res = TramitacionExpedientesDAO.getInstance().eliminarDocumentoCRD(tEVO,params);
              m_Log.debug("eliminarDocumentoCRD");
          }
      } catch (TramitacionException te) {
          res = -1;
          te.printStackTrace();
          m_Log.error("JDBC Technical problem " + te.getMessage());
          //throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
      } catch(Exception e) {
          res = -1;
          e.printStackTrace();
          //throw new TramitacionException("Problema técnico de JDBC " + e.getMessage());
      }
      m_Log.debug("eliminarDocumentoCRD");
      return res;
  }

    public int eliminarDocumentoRelacionCRD(TramitacionExpedientesValueObject tEVO, String[] params)
    /*throws TramitacionException*/ {

        int res;
        m_Log.debug("TramitacionExpedienteManager.eliminarDocumentoCRD");

        try {
            if (m_ConfigCommon.getString("aytos.firmadoc").equalsIgnoreCase("si")) {
                m_Log.debug("Llamando al Servicio Web de Firmadoc");
                res = SWFirmaDocRelacionManager.getInstance(params).eliminarDocumentoRelacionCRD(tEVO);
                m_Log.debug("SWFirmaDocManager.eliminarDocumentoCRD");
            } else {
                m_Log.debug("Usando persistencia manual");
                res = TramitacionExpedientesDAO.getInstance().eliminarDocumentoRelacionCRD(tEVO,params);
                m_Log.debug("eliminarDocumentoCRD");
            }
        } catch (TramitacionException te) {
            res = -1;
            te.printStackTrace();
            m_Log.error("JDBC Technical problem " + te.getMessage());
            //throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
        } catch(Exception e) {
            res = -1;
            e.printStackTrace();
            //throw new TramitacionException("Problema técnico de JDBC " + e.getMessage());
        }
        m_Log.debug("eliminarDocumentoCRD");
        return res;
    }


    public int cambiarEstadoFirmaDocumentoCRD(TramitacionExpedientesValueObject tEVO, int idUsuario, String portafirmas, String[] params) {
      int res=0;
      m_Log.debug("cambiarEstadoFirmaDocumentoCRD");
      try {
        m_Log.debug("Usando persistencia manual");
        m_Log.debug("tEVO: " + tEVO.toString());
        m_Log.debug("portafirmas: " + portafirmas);
        m_Log.debug("params: " + params.toString());
        m_Log.debug("TramitacionExpedientesDAO.getInstance(): " + TramitacionExpedientesDAO.getInstance());
        
        res = TramitacionExpedientesDAO.getInstance().cambiarEstadoFirmaDocumentoCRD(tEVO, idUsuario, portafirmas, params);
        m_Log.debug("cambiarEstadoFirmaDocumentoCRD");
      } catch (TramitacionException te) {
        res = -1;
        te.printStackTrace();
        m_Log.error("JDBC Technical problem " + te.getMessage());
      } catch(Exception e) {
        m_Log.error("Exception " + e + e.getMessage());
        res = -1;
        e.printStackTrace();
      } finally {
        m_Log.debug("cambiarEstadoFirmaDocumentoCRD");
        }
        return res;
      }

    public int cambiarEstadoFirmaDocumentoRelacion(TramitacionExpedientesValueObject tEVO, String[] params) {
      int res=0;
      m_Log.debug("cambiarEstadoFirmaDocumentoCRD");
      try {
        m_Log.debug("Usando persistencia manual");
        res = TramitacionExpedientesDAO.getInstance().cambiarEstadoFirmaDocumentoRelacion(tEVO,params);
        m_Log.debug("cambiarEstadoFirmaDocumentoCRD");
      } catch (TramitacionException te) {
        res = -1;
        te.printStackTrace();
        m_Log.error("JDBC Technical problem " + te.getMessage());
      } catch(Exception e) {
        res = -1;
        e.printStackTrace();
      } finally {
        m_Log.debug("cambiarEstadoFirmaDocumentoCRD");
        return res;
      }
    }

  public void finalizarTramite(TramitacionExpedientesValueObject tEVO, String[] params)
	throws WSException, TramitacionException, EjecucionSWException
  {
    // Sin condicion de salida.
    
    m_Log.debug("finalizarTramite");
    try {
      m_Log.debug("Usando persistencia manual");
      TramitacionExpedientesDAO.getInstance().finalizarTramite(tEVO, params);
      m_Log.debug("finalizarTramite");
	} catch (WSException wse) {
		throw wse;
    } catch (TramitacionException te) {
    
      te.printStackTrace();
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw te;
    } catch (TechnicalException te) {
    
        te.printStackTrace();
    }catch(EjecucionOperacionModuloIntegracionException e){
        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
        traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
        traductor.setIdi_cod(tEVO.getCodIdiomaUsuario());

        StringBuffer mensaje = new StringBuffer();
        if(e.getNombreModulo()!=null && !"".equals(e.getCodigoErrorOperacion()) && e.getCodigoErrorOperacion()!=null && !"".equals(e.getCodigoErrorOperacion())){
            try{
                ResourceBundle  resource = ResourceBundle.getBundle(e.getNombreModulo());
                if(resource!=null){                    
                    // Se obtiene el nombre del mensaje de error correspondiente a la operación del módulo que ha fallado
                    String nombreMensaje = resource.getString(tEVO.getCodMunicipio() + ConstantesDatos.MODULO_INTEGRACION + e.getOperacion() + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + e.getCodigoErrorOperacion());
                    mensaje.append(resource.getString(nombreMensaje + ConstantesDatos.GUION_BAJO + tEVO.getCodIdiomaUsuario()));
                }
            }catch(Exception ex){
                m_Log.error("Error al leer las propiedades de gestión de errores personalizadas del módulo: " + e.getNombreModulo() + " para la operación "
                        + e.getOperacion() + ". Se muestra un mensaje de error genérico.");

                // SE MUESTRA ENTONCES UN MENSAJE GENÉRICO
                mensaje.delete(0,mensaje.length());
                mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion1"));
                mensaje.append(" ");
                mensaje.append(e.getOperacion());
                mensaje.append(" ");
                mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion2"));
                mensaje.append(e.getNombreModulo());
                mensaje.append(" ");
                mensaje.append(traductor.getDescripcion("msgFalloOpModuloIntegracion3"));

            }// catch
        }// if

        throw new TramitacionException(mensaje.toString());

    }
    
    }



  public int reabrirExpediente(GeneralValueObject gVO, String[] params)
  {
    // Con condicion de salida: finalizacion.
    int resultado = 0;
    m_Log.debug("reabrirExpediente");
    try {
      m_Log.debug("Usando persistencia manual");
      resultado = TramitacionExpedientesDAO.getInstance().reabrirExpediente(gVO,params);
      m_Log.debug("reabrirExpediente");
    } catch (TramitacionException te) {
      resultado = -1;
      te.printStackTrace();
      m_Log.error("JDBC Technical problem " + te.getMessage());
    } catch(Exception e) {
      resultado = -1;
      e.printStackTrace();
    } finally {
      m_Log.debug("reabrirExpediente");
      return resultado;
    }
  }


    public int finalizarExpedienteNoConvencional(TramitacionExpedientesValueObject tEVO, Vector estructura, String[] params)
            throws TramitacionException {

        m_Log.debug("finalizarExpediente:Datos-->");
        m_Log.debug("Tevo: "+tEVO.toString());
        m_Log.debug("Parametro 1 --> Fecha Fin Expediente: " + tEVO.getNumero());
        m_Log.debug("Parametro 2 --> Estado Expediente: " +  tEVO.getNumeroExpediente());
        m_Log.debug("estructura: "+estructura);
        m_Log.debug("Params: ");
        for(int i=0; params.length<i;i++){
             m_Log.debug("Params: "+i+" = "+params[i]);
        }
        try {
            m_Log.debug("Usando persistencia manual");
            int resultado = TramitacionExpedientesDAO.getInstance().finalizarExpedienteNoConvencional(tEVO, estructura, params);
            m_Log.debug("finalizarExpediente");
            return resultado;
        } catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("ERROR TECNICO AL FINALIZAR EXPEDIENTE (" + te.getMessage() + ")", te);
        }
    }

    public GeneralValueObject conFinalizarExpedienteNoConvencional(TramitacionExpedientesValueObject tEVO, String[] params)
            throws TramitacionException {

        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        Connection con = null;
        
        m_Log.debug("finalizarExpediente");
        try {           
            m_Log.debug("Usando persistencia manual");
            con = adapt.getConnection();
            
            GeneralValueObject resultado = TramitacionExpedientesDAO.getInstance().conFinalizarExpedienteNoConvencional(tEVO,con);
            m_Log.debug("finalizarExpediente");
            return resultado;
        } catch (BDException e) {
            m_Log.error("JDBC Technical problem " + e.getMessage());
            throw new TramitacionException("ERROR TECNICO AL RECUPERAR CONEXION A LA BBDD " + e.getMessage(), e);
        } 
        catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("ERROR TECNICO AL FINALIZAR EXPEDIENTE (" + te.getMessage() + ")", te);
        } finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
            }
        }
    }

   public int finalizarExpediente(TramitacionExpedientesValueObject tEVO,  String[] params)
            throws TramitacionException {

        m_Log.debug("finalizarExpediente");
        try {
            m_Log.debug("Usando persistencia manual");
            int resultado = TramitacionExpedientesDAO.getInstance().finalizarExpediente(tEVO,  params);
            m_Log.debug("finalizarExpediente");
            return resultado;
        } catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw new TramitacionException("ERROR TECNICO AL FINALIZAR EXPEDIENTE (" + te.getMessage() + ")", te);
        }
    }



    public Vector finalizarConTramites(TramitacionExpedientesValueObject tEVO,  String[] params)
            throws WSException, TramitacionException, EjecucionSWException {
        // Con condicion de salida: lista de trámites a iniciar.
        Vector devolver = new Vector();
        m_Log.debug("finalizarConTramites");
        try {
            m_Log.debug("Usando persistencia manual");
            devolver = TramitacionExpedientesDAO.getInstance().finalizarConTramites(tEVO, params);
            m_Log.debug("finalizarConTramites");
        } catch (WSException wse) {
            throw wse;
        } catch (TramitacionException te) {
            devolver = null;
            te.printStackTrace();
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw te;
        } catch (TechnicalException e) {
            throw new TramitacionException(e.getMessage());
        }
        return devolver;
    }

    public int actualizarTramiteRelacion(TramitacionExpedientesValueObject tEVO, String[] params, boolean finalizar)
        throws WSException
    //throws TramitacionException
    {
      // Con condicion de salida: lista de trámites a iniciar.
      int devolver=0;
      m_Log.debug("finalizarConTramites");
      try {
        m_Log.debug("Usando persistencia manual");
        devolver = TramitesExpedienteDAO.getInstance().actualizarTramiteRelacion(tEVO, params, finalizar);
        m_Log.debug("finalizarConTramites");
      } catch(Exception e) {
        devolver = 0;
        e.printStackTrace();

      }
      return devolver;
    }

    public int insertarTramiteRelacion(TramitacionExpedientesValueObject tEVO, String[] params, boolean finalizar)
        throws WSException
    //throws TramitacionException
    {
      // Con condicion de salida: lista de trámites a iniciar.
      int devolver=0;
      m_Log.debug("finalizarConTramites");
      try {
        m_Log.debug("Usando persistencia manual");
        devolver = TramitesExpedienteDAO.getInstance().insertarTramiteRelacion(tEVO, params, finalizar);
        m_Log.debug("finalizarConTramites");
      } catch(Exception e) {
        devolver = 0;
        e.printStackTrace();

    }
      return devolver;
    }

 

    public Vector finalizarConPreguntaConTramites(TramitacionExpedientesValueObject tEVO, String[] params, boolean desFavorable)
            throws TramitacionException, WSException, EjecucionSWException {
        // Con condicion de salida: pregunta que inicia lista de trámites.
        Vector devolver = new Vector();
        m_Log.debug("finalizarConPreguntaConTramites");
        try {
            m_Log.debug("Usando persistencia manual");
            devolver = TramitacionExpedientesDAO.getInstance().finalizarConPreguntaConTramites(tEVO, params, desFavorable);
            m_Log.debug("finalizarConPreguntaConTramites");
        } catch (WSException wse) {
            throw wse;
        } catch (TramitacionException te) {
            devolver = null;
            te.printStackTrace();
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw te;
        } catch (EjecucionSWException eswe) {
            throw eswe;
        } catch (TechnicalException e) {
            devolver = null;
            e.printStackTrace();
            throw new TramitacionException(e.getMessage());
        }
        return devolver;
    }

    public Vector finalizarConResolucionConTramites(TramitacionExpedientesValueObject tEVO, String[] params, boolean desFavorable)
            throws WSException, TramitacionException, EjecucionSWException {
        // Con condicion de salida: resolucion que inicia lista de trámites.
        Vector devolver = new Vector();
        m_Log.debug("finalizarConResolucionConTramites");
        try {
            m_Log.debug("Usando persistencia manual");
            devolver = TramitacionExpedientesDAO.getInstance().finalizarConResolucionConTramites(tEVO, params, desFavorable);
            m_Log.debug("finalizarConResolucionConTramites");
        } catch (WSException wse) {
            throw wse;
        } catch (TramitacionException te) {
            devolver = null;
            te.printStackTrace();
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw te;
        } catch (EjecucionSWException e) {
            throw e;
        } catch (TechnicalException e) {
            throw new TramitacionException(e.getMessage());
        }
        return devolver;
    }

  public Vector iniciarTramitesManual(TramitacionExpedientesValueObject tEVO, String[] params)
  //throws TramitacionException
  {
    // Sin condicion de salida.
    Vector devolver = new Vector();
    m_Log.debug("iniciarTramiteManual");
    try {
      m_Log.debug("Usando persistencia manual");
      devolver = TramitacionExpedientesDAO.getInstance().iniciarTramitesManual(tEVO,params);
      m_Log.debug("iniciarTramiteManual");
    } catch (TramitacionException te) {
      devolver = null;
      te.printStackTrace();
      m_Log.error("JDBC Technical problem " + te.getMessage());
      //throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch(Exception e) {
      devolver = null;
      e.printStackTrace();
      //throw new TramitacionException("Problema técnico de JDBC " + e.getMessage());
    } finally {
      m_Log.debug("iniciarTramiteManual");
      return devolver;
    }
  }
  
      public Vector cargaEstructuraAgrupaciones (TramitacionExpedientesValueObject tEVO, String[] params) {
        if(m_Log.isDebugEnabled()) m_Log.debug("cargaEstructuraAgrupaciones() : BEGIN");
        Vector lista = new Vector();
        try{
            lista = TramitacionExpedientesDAO.getInstance().cargaEstructuraAgrupaciones(tEVO, params);
        }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
            m_Log.error("Se ha producido un error recuperando las agrupaciones " + e.getMessage());
        }finally{
            if(m_Log.isDebugEnabled()) m_Log.debug("cargaEstructuraAgrupaciones() : END");
            return lista;
        }//try-catch
    }//cargaEstructuraAgrupaciones
    
    public boolean cargarVista(TramitacionExpedientesValueObject tVO, String[] params){
        if(m_Log.isDebugEnabled()) m_Log.debug("cargarVista() : BEGIN");
        Boolean cargarVista = false;
        try{
           cargarVista = TramitacionExpedientesDAO.getInstance().cargarVista(tVO, params);
        }catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
            m_Log.error("Se ha producido un error recuperando las agrupaciones " + e.getMessage());
        }finally{
            if(m_Log.isDebugEnabled()) m_Log.debug("cargarVista() : END");
            return cargarVista;
        }//try-catchFinally
    }//cargarVista

  public Vector cargaEstructuraDatosSuplementarios(TramitacionExpedientesValueObject tEVO, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaEstructuraDatosSuplementarios");
    Vector lista = new Vector();

    try{
      lista = TramitacionExpedientesDAO.getInstance().cargaEstructuraDatosSuplementarios(tEVO, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return lista;
    }
  }

    public Vector cargaConsultaCamposSuplemetarios(GeneralValueObject gVO, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaEstructuraDatosSuplementarios");
      Vector lista = new Vector();

      try{
        lista = TramitacionExpedientesDAO.getInstance().cargaConsultaCamposSuplementarios(gVO, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return lista;
      }
    }

  public Vector cargaValoresDatosSuplementarios(TramitacionExpedientesValueObject tEVO,Vector eCs, String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargaValoresDatosSuplementarios");
    Vector lista = new Vector();

    try{
      lista = TramitacionExpedientesDAO.getInstance().cargaValoresDatosSuplementarios(tEVO,eCs, params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return lista;
    }
  }
  
    public Vector cargarDatosSuplementariosExpediente(TramitacionExpedientesValueObject tEVO,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("cargarDatosSuplementariosExpediente");
    Vector lista = new Vector();
    AdaptadorSQLBD abd = null;
    Connection con = null;
    abd = new AdaptadorSQLBD(params);

    try{
        con = abd.getConnection();                               

        lista = TramitacionExpedientesDAO.getInstance().cargarDatosSuplementariosExpediente(tEVO,abd,con);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      try{
            abd.devolverConexion(con);
       }catch(BDException e){
                m_Log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
      }
      return lista;
    }
  }

    public Vector cargaValoresDatosSuplEtiquetas(TramitacionExpedientesValueObject tEVO, Vector eCs, String[] params) {

        //Queremos estar informados de cuando este metodo es ejecutado
        if (m_Log.isDebugEnabled())
            m_Log.debug("COMIENZO DE TramitacionExpedientesManager.cargaValoresDatosSuplementarios()");
        Vector lista = new Vector();

        try {
            lista = TramitacionExpedientesDAO.getInstance().cargaValoresDatosSuplEtiquetas(tEVO, eCs, params);
        } catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public GeneralValueObject cargaValoresFicheros(TramitacionExpedientesValueObject gVO,Vector eCs, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaValoresDatosSuplementarios");
      GeneralValueObject lista = new GeneralValueObject();

      try{
        lista = TramitacionExpedientesDAO.getInstance().cargaValoresFicheros(gVO,eCs, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return lista;
      }
    }

    public GeneralValueObject cargaNombresFicheros(TramitacionExpedientesValueObject gVO, Vector eCs, String[] params) {

        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("cargaValoresDatosSuplementarios");
        GeneralValueObject lista = new GeneralValueObject();

        try {
            lista = TramitacionExpedientesDAO.getInstance().cargaNombresFicheros(gVO, eCs, params);
        } catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return lista;
        }
    }

    public GeneralValueObject cargaTiposFicheros(TramitacionExpedientesValueObject gVO,Vector eCs, String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("cargaValoresDatosSuplementarios");
      GeneralValueObject lista = new GeneralValueObject();

      try{
        lista = TramitacionExpedientesDAO.getInstance().cargaTiposFicheros(gVO,eCs, params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return lista;
      }
    }


  public Vector getListaInteresados(GeneralValueObject g,String[] params) {

    //queremos estar informados de cuando este metodo es ejecutado
    m_Log.debug("getListaInteresados");
    Vector lista = new Vector();

    try{
      lista = TramitacionExpedientesDAO.getInstance().getListaInteresados(g,params);
    }catch (TechnicalException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
    }catch (Exception e) {
      e.printStackTrace();
    }finally{
      return lista;
    }
  }

  public Vector getListaInteresadosRelacion(GeneralValueObject g,String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getListaInteresados");
      Vector lista = new Vector();

      try{
        lista = TramitacionExpedientesDAO.getInstance().getListaInteresadosRelacion(g,params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return lista;
      }
    }




    public Vector getListaExpedientesNoInteresadosRelacion(GeneralValueObject g,String[] params) {

      //queremos estar informados de cuando este metodo es ejecutado
      m_Log.debug("getListaInteresados");
      Vector lista = new Vector();

      try{
        lista = TramitacionExpedientesDAO.getInstance().getListaExpedientesNoInteresadosRelacion(g,params);
      }catch (TechnicalException te) {
        m_Log.error("JDBC Technical problem " + te.getMessage());
      }catch (Exception e) {
        e.printStackTrace();
      }finally{
        return lista;
      }
    }

    public int getUsuarioFirma (TramitacionExpedientesValueObject tramExpVO, String[] params) throws Exception {
        return TramitacionExpedientesDAO.getInstance().getUsuarioFirma(tramExpVO, params);
    }

    public int getUsuarioFirmaRelacion (TramitacionExpedientesValueObject tramExpVO, String[] params) throws Exception {
        return TramitacionExpedientesDAO.getInstance().getUsuarioFirmaRelacion(tramExpVO, params);
    }

    public String getFirmaDocumento (TramitacionExpedientesValueObject tramExpVO, String[] params) throws Exception {
        return TramitacionExpedientesDAO.getInstance().getFirmaDocumento(tramExpVO, params);
    }

    public String getFirmaDocumentoRelacion (TramitacionExpedientesValueObject tramExpVO, String[] params) throws Exception {
        return TramitacionExpedientesDAO.getInstance().getFirmaDocumentoRelacion(tramExpVO, params);
    }

  /**
    * Factory method para el <code>Singelton</code>.
    * @return La unica instancia de SelectManager
  */
  public static TramitacionExpedientesManager getInstance() {
    synchronized(TramitacionExpedientesManager.class)
    {
      if (instance == null) {
        instance = new TramitacionExpedientesManager();
      }
    }
    return instance;
  }

  private static TramitacionExpedientesManager instance = null; // Mi propia instancia

  /* Declaracion de servicios */

  protected static Config m_ConfigTechnical; // Para el fichero de configuracion technical
  protected static Config m_ConfigError; // Para el fichero de mensajes de error localizados
  protected static Config m_ConfigCommon;
  protected static Log m_Log =
          LogFactory.getLog(TramitacionExpedientesManager.class.getName());

  public Vector<ElementoListaValueObject> getUnidadesTramitadorasUsuario(String[] params, int codMunicipio, String codProcedimiento, int codTramite, int codUsuario) throws TechnicalException {

      AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
      Connection con = null;

      try {
          con = abd.getConnection();
          return TramitacionExpedientesDAO.getInstance().getUnidadesTramitadorasUsuario(con, codMunicipio, codProcedimiento, codTramite, codUsuario);
      } catch (BDException bde) {
          throw new TechnicalException(bde.getMensaje(), bde);
      } finally {
          SigpGeneralOperations.devolverConexion(abd, con);
      }


  }

  public String getOcurrenciaTramiteAbierto(String[] params, TramitacionExpedientesValueObject tramExpVO) throws TechnicalException {

      AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
      Connection con = null;

      try {
          con = abd.getConnection();
          return TramitesExpedienteDAO.getInstance().getOcurrenciaTramiteAbierto(con, tramExpVO);
      } catch (BDException bde) {
          throw new TechnicalException(bde.getMensaje(), bde);
      } finally {
          SigpGeneralOperations.devolverConexion(abd, con);
      }


  }
    public int getCodRolPorDefecto (String codProcedimiento, String[] params) throws TechnicalException {


      AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
      Connection con = null;

      try {
          con = abd.getConnection();
          return TramitacionExpedientesDAO.getInstance().getCodRolPorDefecto(codProcedimiento,con);
      } catch (BDException bde) {
          throw new TechnicalException(bde.getMensaje(), bde);
      } finally {
          SigpGeneralOperations.devolverConexion(abd, con);
      }

    }

    public Calendar getDocumentoFechaModificacion(Integer codDocumento, Integer ejercicio,
            Integer municipio, String numeroExpediente, Integer codTramite,
            Integer ocurrenciaTramite, String procedimiento, String[] params)
            throws TechnicalException {

        m_Log.debug("getDocumentoFechaModificacion");

        Calendar fechaModificacion = null;
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        Connection con = null;

        try {
            con = abd.getConnection();

            fechaModificacion = TramitacionExpedientesDAO.getInstance().getDocumentoFechaModificacion(
                    codDocumento, ejercicio,
                    municipio, numeroExpediente, codTramite,
                    ocurrenciaTramite, procedimiento, con);
        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje(), bde);
        } finally {
            SigpGeneralOperations.devolverConexion(abd, con);
        }
        
        return fechaModificacion;
    }

     public boolean tieneDocumentosFirmados(GeneralValueObject gVO,String[] params) throws TechnicalException{
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
       Connection con = null;

      try {
          con = abd.getConnection();
          return TramitacionExpedientesDAO.getInstance().tieneDocumentosFirmados(gVO,con);
      } catch (BDException bde) {
          throw new TechnicalException(bde.getMensaje(), bde);
      }
       finally {
          SigpGeneralOperations.devolverConexion(abd, con);
      }
     }
   
    public boolean documentosPendientesDeFirma(TramitacionExpedientesValueObject tramExpVO, String[] params) throws TechnicalException {
        boolean tienePendientes = true;
        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);

        try {           
            con = adapt.getConnection();            
            tienePendientes = TramitacionExpedientesDAO.getInstance().documentosPendientesDeFirma(tramExpVO, con);
        } catch (BDException ex) {            
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(TramitacionExpedientesManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("Error de Base de datos en documentosPendientesDeFirma", ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);            
        }        
        
        return tienePendientes;
    }
        
    public GeneralValueObject expedientePermiteSubsanacion(String expediente, String[] params) throws TechnicalException {
        GeneralValueObject tramite = null;
        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        try {
            m_Log.debug(TramitacionExpedientesManager.class.getName() + " --> expedientePermiteSubsanacion");
            con = adapt.getConnection();
            tramite = TramitacionExpedientesDAO.getInstance().expedientePermiteSubsanacion(expediente, con);
            m_Log.debug(TramitacionExpedientesManager.class.getName() + " <-- expedientePermiteSubsanacion: " + tramite);
        } catch (BDException ex) {            
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(TramitacionExpedientesManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("Error de Base de datos en expedientePermiteSubsanacion", ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);            
            return tramite;
        }
    }

    public Vector<TramitacionExpedientesValueObject> obtenerTramitesSiguientes (String codOrg, String codProc, String codTram, 
            int numCod, String[] params) throws TechnicalException {
        Vector<TramitacionExpedientesValueObject> listaTramites = new Vector<TramitacionExpedientesValueObject>();
        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        try {
            m_Log.debug(TramitacionExpedientesManager.class.getName() + " --> obtenerTramitesSiguientes");
            con = adapt.getConnection();
            Vector<SiguienteTramiteTO> lista = TramitacionExpedientesDAO.getInstance().getFlujoSalida(con, codOrg, codProc, codTram, numCod);
            Iterator i = lista.iterator();
            while (i.hasNext()) {
                SiguienteTramiteTO tramite = (SiguienteTramiteTO) i.next();
                TramitacionExpedientesValueObject traExp = new TramitacionExpedientesValueObject();
                traExp.setCodTramite(tramite.getCodigoTramiteFlujoSalida());
                traExp.setOcurrenciaTramite(tramite.getNumeroSecuencia());
                listaTramites.add(traExp);
            }
            m_Log.debug(TramitacionExpedientesManager.class.getName() + " <-- obtenerTramitesSiguientes");
        } catch (BDException ex) {            
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(TramitacionExpedientesManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new TechnicalException("Error de Base de datos en obtenerTramitesSiguientes", ex);
        } finally {
            SigpGeneralOperations.devolverConexion(adapt, con);            
            return listaTramites;
        }
    }
    
    public Vector finalizarConSubsanacion(TramitacionExpedientesValueObject tEVO, String[] params, boolean desFavorable)
            throws TramitacionException, WSException, EjecucionSWException {
        Vector devolver = new Vector();
        m_Log.debug(TramitacionExpedientesManager.class.getName() + "--> finalizarConSubsanacion");
        try {
            m_Log.debug("Usando persistencia manual");
            devolver = TramitacionExpedientesDAO.getInstance().finalizarConSubsanacion(tEVO, params, desFavorable);
        } catch (WSException wse) {
            throw wse;
        } catch (TramitacionException te) {
            devolver = null;
            te.printStackTrace();
            m_Log.error("JDBC Technical problem " + te.getMessage());
            throw te;
        } catch (EjecucionSWException eswe) {
            throw eswe;
        } catch (TechnicalException e) {
            devolver = null;
            e.printStackTrace();
            throw new TramitacionException(e.getMessage());
        }
        m_Log.debug(TramitacionExpedientesManager.class.getName() + "<-- finalizarConSubsanacion");
        return devolver;
    }
    
    
    
    
    
    public String tratarCamposExpresion(TramitacionExpedientesValueObject tramExpVO,GeneralValueObject gVO,HttpServletRequest request,String[] params){
        AdaptadorSQLBD abd = null;
        Connection con = null;
        String salida = null;
        try{
            abd = new AdaptadorSQLBD(params);
            con = abd.getConnection();
            
            Vector estructuraDSExpediente = FichaExpedienteDAO.getInstance().cargaEstructuraDatosSuplementarios(gVO, abd, con);
            Vector valoresDSExpediente = new Vector();
            
            //*************************************************
            //*************************************************                                
            
            int contador = 0;
            for(int i=0;i<estructuraDSExpediente.size();i++) {
                HashMap campos = new HashMap();                 
                EstructuraCampo CC = (EstructuraCampo) estructuraDSExpediente.elementAt(i);                                                                       
                String valor = request.getParameter(CC.getCodCampo());
                String codigo = request.getParameter(CC.getCodCampo()+"_CODSEL");
                if(valor!=null && !"".equals(valor)) contador++;
                campos.put(CC.getCodCampo(),valor);  
                campos.put(CC.getCodCampo()+"_CODSEL",codigo);  
                CamposFormulario CampF = new CamposFormulario(campos);                              
                valoresDSExpediente.addElement(CampF);
            }// for

            tramExpVO.setEstructuraDatosSuplExpediente(estructuraDSExpediente);
            tramExpVO.setValoresDatosSuplExpediente(valoresDSExpediente);
            tramExpVO.setEstructuraDatosSuplTramites(new Vector());
            tramExpVO.setValoresDatosSuplTramites(new Vector());
            
            if(contador==0){
                // Sino se ha recuperado ningún valor, es que la pestaña con los datos suplementarios no está visible
                salida = "0";
            }else
            salida = TramitacionExpedientesDAO.getInstance().tratarCamposExpresion(abd, con, tramExpVO);                  
            
            
        }catch(BDException e){
            m_Log.error("Error al tratar los campos de con expresión de cálculo de fecha o número: " + e.getMessage());
        }catch(TechnicalException e){
            m_Log.error("Error al cargar la estructura de campos suplementarios: " + e.getMessage());
        }catch(TramitacionException e){
            m_Log.error("Error al tratar los campos de expresión: " + e.getMessage());
        }catch(Exception e){
            m_Log.error("Error al tratar los campos de expresión: " + e.getMessage());
        }
        finally{
            try{
                abd.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        
        return salida;
    }
    
    
    
      public String tratarCamposExpresionTramite(TramitacionExpedientesValueObject tramExpVO,TramitacionExpedientesForm tramExpForm,HttpServletRequest request,String[] params){
        AdaptadorSQLBD abd = null;
        Connection con = null;
        String salida = null;
        
        try{
            abd = new AdaptadorSQLBD(params);
            con = abd.getConnection();
            
            Vector DS = TramitacionExpedientesDAO.getInstance().cargarDatosSuplementariosExpediente(tramExpVO,abd,con);
            Vector estructuraDSExpediente          = (Vector) DS.elementAt(0);
            Vector valoresDSExpediente             = (Vector) DS.elementAt(1);
            Vector estructuraDSTramitesRecuperados = (Vector) DS.elementAt(2);
            Vector valoresDSTramiteRecuperados     = (Vector) DS.elementAt(3);
            
            Vector estructuraDSTramitesFinal = new Vector();
            Vector valoresDSTramiteFinal = new Vector();                    

            //Recuperamos datos del tramite activo.
            Vector estructuraForm =  tramExpForm.getEstructuraDatosSuplementarios();                    
            String prefijoCampo = "T_" + tramExpVO.getCodTramite() + "_" + tramExpVO.getOcurrenciaTramite() + "_";

            ArrayList <String> datosRequest = new ArrayList();
            String aux = "";
           
            for(int i=0;i<estructuraForm.size();i++) {
                HashMap Camp_Form = new HashMap();
                EstructuraCampo CC = (EstructuraCampo) estructuraForm.elementAt(i);
                estructuraDSTramitesFinal.addElement(CC);
                
                if (CC.getCodTipoDato().equals("6")) Camp_Form.put(CC.getCodCampo(),request.getParameter("cod"+prefijoCampo + CC.getCodCampo()));
                else Camp_Form.put(CC.getCodCampo(),request.getParameter(prefijoCampo + CC.getCodCampo()));
                
                CamposFormulario Camp_F = new CamposFormulario(Camp_Form);
                valoresDSTramiteFinal.addElement(Camp_F); 
                datosRequest.add("T" + tramExpVO.getCodTramite() + CC.getCodCampo());       
            }     
            
            tramExpVO.setValoresDatosSuplementarios(new Vector(valoresDSTramiteFinal));
            
            for(int i=0;i<estructuraDSTramitesRecuperados.size();i++){
                  EstructuraCampo CO = (EstructuraCampo) estructuraDSTramitesRecuperados.elementAt(i);
                  aux = "T" + CO.getCodTramite() + CO.getCodCampo();
                  if (!datosRequest.contains(aux)) {                              
                        HashMap Camp_Tram = new HashMap();
                        estructuraDSTramitesFinal.addElement(CO);
                        EstructuraCampo eC = (EstructuraCampo) estructuraDSTramitesRecuperados.elementAt(i);
                        CamposFormulario cF = (CamposFormulario) valoresDSTramiteRecuperados.get(i);
                        Camp_Tram.put(CO.getCodCampo() ,cF.getString(eC.getCodCampo()));                             
                        CamposFormulario Camp_T = new CamposFormulario(Camp_Tram);
                        valoresDSTramiteFinal.addElement(Camp_T); 
                  }                                                        
            }                                    
            tramExpVO.setEstructuraDatosSuplExpediente(estructuraDSExpediente);
            tramExpVO.setValoresDatosSuplExpediente(valoresDSExpediente);
            tramExpVO.setEstructuraDatosSuplTramites(estructuraDSTramitesFinal);
            tramExpVO.setValoresDatosSuplTramites(valoresDSTramiteFinal);

            salida = TramitacionExpedientesDAO.getInstance().tratarCamposExpresion(abd, con, tramExpVO);                                             
            
        }catch(BDException e){
            m_Log.error("Error al tratar los campos de con expresión de cálculo de fecha o número: " + e.getMessage());
        }catch(TechnicalException e){
            m_Log.error("Error al cargar la estructura de campos suplementarios: " + e.getMessage());
        }catch(TramitacionException e){
            m_Log.error("Error al tratar los campos de expresión: " + e.getMessage());
        }catch(Exception e){
            m_Log.error("Error al tratar los campos de expresión: " + e.getMessage());
        }
        finally{
            try{
                abd.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        
        return salida;
    }
    
    
    public ArrayList<GeneralValueObject> recuperarExternos(String[] params,GeneralValueObject gVO){
    /*
     * @Descripcion --> Recuperacion de datos externos para campos suplementarios de tipo desplegable externo.
     * @Autor       --> David.vidal
     * @Fecha       --> 24/04/2013     
     */
         String retorno ="";
         Connection con = null;
          ArrayList<GeneralValueObject> resultados = new ArrayList<GeneralValueObject>();
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            String CampoExterno = TramitacionExpedientesDAO.getInstance().recuperarCodigoExterno(con, gVO); 
            String[] parametros = TramitacionExpedientesDAO.getInstance().recuperarParametrosConexionExternos(con, CampoExterno); 
            resultados = TramitacionExpedientesDAO.getInstance().recuperarDatosExternos(parametros);
           
        }catch(Exception e){ 
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

         return resultados;
     }
    
    
    
    
    /**
     * Cuenta el número de interesados que tienen asignado un expediente
     * @param codOrganizacion: Código de la organización/municipio
     * @param numExpediente: Número del expediente
     * @param params: Parámetros de conexión a la BBDD
     * @return int
     */
    public int getNumInteresadosExpediente(int codOrganizacion,String numExpediente,String[] params){
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        int numero =0;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            numero = TramitacionExpedientesDAO.getInstance().getNumInteresadosExpediente(codOrganizacion, numExpediente, con);
            
        }catch(BDException e){
            m_Log.error("Error al recuperar número de interesados del expediente: " + e.getMessage());
            e.printStackTrace();
            
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                m_Log.error("Error al recuperar número de interesados del expediente: " + e.getMessage());
                e.printStackTrace();
            }
            return numero;
        }
        
    }

    
    
    /**
     * Método que es llamada para recuperar la información de los campos suplementarios de tipo fecha
     * @param tEVO
     * @param eCs
     * @param params
     * @return
     * @throws TechnicalException M
     */
     public Hashtable<String,GeneralValueObject> cargaNombresEstadosFicherosTramite(TramitacionExpedientesValueObject tEVO, Vector eCs, String[] params) throws TechnicalException {

        Hashtable<String,GeneralValueObject> salida = new Hashtable<String, GeneralValueObject>();
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        GeneralValueObject nombres = new GeneralValueObject();
        GeneralValueObject estados = new GeneralValueObject();
        GeneralValueObject longitudes = new GeneralValueObject();
        
        try{
            
            con = oad.getConnection();
            String codMunicipio = tEVO.getCodMunicipio();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();

            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String codCampo = eC.getCodCampo();
                String ocurrencia = eC.getOcurrencia();

                String tipo;

                if("5".equals(codTipoDato)) {                    
                    //tipo=DatosSuplementariosDAO.getInstance().getNombreFicheroTramiteExpediente(codCampo,codMunicipio,ejercicio,numero,ocurrencia,params,codTramite);
                    
                    
                    CampoSuplementarioFicheroVO campo = DatosSuplementariosDAO.getInstance().getNombreLongitudFicheroTramite(codCampo,codMunicipio,ejercicio,numero,ocurrencia, codTramite,params);
                    
                    
                    codCampo=codCampo+"_"+ocurrencia;                    
                    //nombres.setAtributo(codCampo,tipo);
                    nombres.setAtributo(codCampo,campo.getTipoMime());

                    // Como se recupera de base de datos, el fichero ya ha sido grabado en un repositorio 
                    // de documentación
                    estados.setAtributo(codCampo,ConstantesDatos.ESTADO_DOCUMENTO_GRABADO);                    
                    
                    // Longitud del fichero
                    longitudes.setAtributo(codCampo,campo.getLongitudFichero());
                }
            }
            

        } catch (Exception e){
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        salida.put("estadosFicheros", estados);
        salida.put("nombresFicheros", nombres);
        salida.put("longitudesFicheros", nombres);
        
        return salida;
    }
    
        
    public boolean tieneTramiteCamposObligatoriosSinValor(TramitacionExpedientesValueObject tEVO,String[] params) throws BDException,TechnicalException{
        boolean exito = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            TramitacionExpedientesDAO dao = TramitacionExpedientesDAO.getInstance();
            Vector<EstructuraCampo> estructura = dao.cargaEstructuraDatosSuplementarios(tEVO,adapt,con);
            Vector<CamposFormulario> valores = dao.cargaValoresDatosSuplementarios(tEVO, estructura, adapt, con);
            
            if(estructura!=null && valores!=null){
            
                int contadorNumObligatorios = 0;
                int contadorNumValoresObligatorios = 0;
                
                for(int i=0;i<estructura.size();i++){
                    EstructuraCampo eC  = estructura.get(i);
                    CamposFormulario cF = valores.get(i);                
                    String obligatorio = eC.getObligatorio();
                    String valor = cF.getString(eC.getCodCampo());
                    String activo=eC.getActivo();
                    
                    if (!"NO".equals(activo)){
                        if(obligatorio!=null && "1".equals(obligatorio)){
                            // Se cuenta el número de campos obligatorios
                            contadorNumObligatorios++;
                        }

                        if(obligatorio!=null && "1".equals(obligatorio) && valor!=null && !"".equals(valor)){
                            // Se cuenta el número de campos obligatorios con valor !=null
                            contadorNumValoresObligatorios++;
                        }
                    }
                }
                
                if(contadorNumObligatorios!=contadorNumValoresObligatorios) 
                    exito = true; 
                
           }
            
        }catch(TechnicalException e){
            m_Log.error("Error al recuperar la estructra o valores de los campos suplementarios del trámite " + tEVO.getCodTramite() + " del expediente: " + tEVO.getNumeroExpediente() + ": " + e.getMessage());
            throw e;
        }catch(BDException e){
            m_Log.error("Error al obtener la conexión a la BBDD: " + e.getMessage());
            throw e;
        }        
        finally{
            try{                
                adapt.devolverConexion(con);
                
            }catch(BDException e){
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        
        return exito;
    }

    
    
    
    /**
     * Recupera la estructura 
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @param params: Parámetros de conexión a la BBDD
     * @return 
     */
    public Vector<EstructuraCampo> recuperarEstructuraCamposSuplementariosTramiteInicio(int codOrganizacion,String codTramite,String codProcedimiento,String[] params) throws TechnicalException{
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Vector<EstructuraCampo> salida = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            
            TramitacionExpedientesDAO dao = TramitacionExpedientesDAO.getInstance();                 
            TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
            tEVO.setCodMunicipio(Integer.toString(codOrganizacion));
            tEVO.setCodProcedimiento(codProcedimiento);
            tEVO.setCodTramite(codTramite);
            // Se recupera la estructura de datos suplementarios del trámite de inicio
            salida = dao.cargaEstructuraDatosSuplementarios(tEVO, adapt, con);
            
        }catch(BDException e){
            m_Log.error("Error al obtener conexión a la BBDD: " + e.getMessage());
        }catch(Exception e){
            m_Log.error("Error al obtener conexión a la BBDD: " + e.getMessage());
        }        
        finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                
            }
        }
        
        return salida;        
    }

    
   /**
    * Carga los datos de un trámite de un expediente que está en las tablas del histórico
    * @param tEVO: Objeto de la clase TramitacionExpedientesValueObject
    * @param params: Parámetros de conexión a la BD
    * @return TramitacionExpedientesValueObject con la información del trámite
    * @throws AnotacionRegistroException 
    */ 
   public TramitacionExpedientesValueObject cargarDatosHistorico(TramitacionExpedientesValueObject tEVO, String[] params)
  throws AnotacionRegistroException {

    m_Log.debug("cargarDatosHistorico");
    try {
      m_Log.debug("Usando persistencia manual");
      tEVO = TramitacionExpedientesDAO.getInstance().cargarDatosHistorico(tEVO,params);
      m_Log.debug("datos cargados");
    } catch (AnotacionRegistroException te) {
      m_Log.error("JDBC Technical problem " + te.getMessage());
      throw new AnotacionRegistroException("Problema técnico de JDBC " + te.getMessage());
    } catch(Exception e) {
      e.printStackTrace();
    }
    m_Log.debug("Fin cargarDatosHistorico()");
    return tEVO;
  }

   /********************************/

    
    
    /*********************************************************/
    public Hashtable<String,GeneralValueObject> cargaNombresEstadosFicherosTramite(TramitacionExpedientesValueObject tEVO, Vector eCs,boolean expedienteHistorico,String[] params) throws TechnicalException {

        Hashtable<String,GeneralValueObject> salida = new Hashtable<String, GeneralValueObject>();
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        GeneralValueObject nombres = new GeneralValueObject();
        GeneralValueObject estados = new GeneralValueObject();
        GeneralValueObject longitudes = new GeneralValueObject();
        
        try{
            
            con = oad.getConnection();
            String codMunicipio = tEVO.getCodMunicipio();
            String ejercicio = tEVO.getEjercicio();
            String numero = tEVO.getNumeroExpediente();
            String codTramite = tEVO.getCodTramite();

            for(int i=0;i<eCs.size();i++) {
                EstructuraCampo eC = new EstructuraCampo();
                eC = (EstructuraCampo) eCs.elementAt(i);
                String codTipoDato = eC.getCodTipoDato();
                String codCampo = eC.getCodCampo();
                String ocurrencia = eC.getOcurrencia();

                String tipo;

                if("5".equals(codTipoDato)) {                    
                    CampoSuplementarioFicheroVO campo = DatosSuplementariosDAO.getInstance().getNombreLongitudFicheroTramite(codCampo,codMunicipio,ejercicio,numero,ocurrencia,codTramite,expedienteHistorico,params);
                    codCampo=codCampo+"_"+ocurrencia;                    
                    nombres.setAtributo(codCampo,campo.getTipoMime());
                    // Como se recupera de base de datos, el fichero ya ha sido grabado en un repositorio 
                    // de documentación
                    estados.setAtributo(codCampo,ConstantesDatos.ESTADO_DOCUMENTO_GRABADO);                                        
                    // Longitud del fichero
                    longitudes.setAtributo(codCampo,campo.getLongitudFichero());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
        salida.put("estadosFicheros", estados);
        salida.put("nombresFicheros", nombres);
        salida.put("longitudesFicheros", longitudes);
        
        return salida;
    }

    
    
    public GeneralValueObject cargaTiposFicheros(TramitacionExpedientesValueObject gVO,Vector eCs,boolean expedienteHistorico,String[] params) {
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("cargaTiposFicheros");
        GeneralValueObject lista = new GeneralValueObject();

        try{
          lista = TramitacionExpedientesDAO.getInstance().cargaTiposFicheros(gVO,eCs,expedienteHistorico,params);
          
        }catch (TechnicalException te) {
          m_Log.error("JDBC Technical problem " + te.getMessage());
        }catch (Exception e) {
          e.printStackTrace();
        }finally{
          return lista;
        }
    }
    
    public GeneralValueObject getDatosTramiteIniciado(String codTramite, GeneralValueObject gVO, String[] params){
        m_Log.debug("---------------- TramitacionExpedientesManager::getDatosTramiteIniciado() -----------");
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        ArrayList<GeneralValueObject> todosTr = null;
        ArrayList<GeneralValueObject> tramitesAux = new  ArrayList<GeneralValueObject>();
        Vector<GeneralValueObject> tramites = new  Vector<GeneralValueObject>();
        ArrayList<String> permisosTram = null;
        String fechaIni = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            todosTr = new ArrayList<GeneralValueObject>(FichaExpedienteDAO.getInstance().cargaTramites(gVO,adapt,con,true));
            if(todosTr.size()>0){
                for(int j=0;j<todosTr.size();j++){
                    GeneralValueObject g = todosTr.get(j);
                    if(g.getAtributo("codTramite").equals(codTramite)){
                        if(fechaIni==null) fechaIni = (String) g.getAtributo("fehcaInicio");
                        else if(DateOperations.compararFechasString((String) g.getAtributo("fehcaInicio"), fechaIni))
                             fechaIni = (String) g.getAtributo("fehcaInicio");
                        tramitesAux.add(g);
                    } 
                }
                for(int j=0;j<tramitesAux.size();j++){
                    GeneralValueObject g = todosTr.get(j);
                    if(g.getAtributo("fehcaInicio").equals(fechaIni)){
                        tramites.add(g);
                        gVO.setAtributo("codTramite", g.getAtributo("codTramite"));
                        gVO.setAtributo("ocuTramite", g.getAtributo("ocurrenciaTramite"));
                        gVO.setAtributo("descTramite", g.getAtributo("tramite"));
                        gVO.setAtributo("fechaIniTramite", g.getAtributo("fehcaInicio"));
                        gVO.setAtributo("fechaFinTramite", g.getAtributo("fechaFin"));
                        gVO.setAtributo("codUsuTramite", g.getAtributo("codUsuario"));
                        gVO.setAtributo("usuarioTramite", g.getAtributo("usuario"));
                        gVO.setAtributo("codUsuFinTramite", g.getAtributo("codUsuarioFinalizacion"));
                        gVO.setAtributo("clasifTramite", g.getAtributo("clasificacion"));
                        gVO.setAtributo("codUniTramTramite", g.getAtributo("codUniTramTramite"));
                        gVO.setAtributo("consultarTramite", g.getAtributo("consultar"));
                    } 
                }
                permisosTram = new ArrayList<String>(FichaExpedienteDAO.getInstance().cargaPermisosTramites(tramites,gVO, con));
                for(int j=0;j<permisosTram.size();j++){
                    gVO.setAtributo("permisoTramite", permisosTram.get(j));
                }
            }
            
            m_Log.debug("******************** DATOS OBTENIDOS: "+gVO.toString());
        } catch (BDException ex) {
            ex.printStackTrace();
            m_Log.error("Error al obtener una conexión a la bbdd");
        } catch (TechnicalException ex) {
            ex.printStackTrace();
            m_Log.error("Error al obtener la lista de trámites");
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error: "+e.getMessage());
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                ex.printStackTrace();
                m_Log.error("Error al cerrar la conexión a la bbdd.");
            }
        }
        return gVO;
    }
    
    public ArrayList<TramitacionExpedientesValueObject> getDatosTramitesNotificadosNoE(int codOrg, int ejerc, String codProc, String numExp, boolean enHistorico, String[] params){
        m_Log.debug("---------------- TramitacionExpedientesManager::getDatosTramiteIniciado() -----------");
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Vector<EstructuraCampo> estructuraDatosSuplementarios = null;
        Vector<CamposFormulario> valoresDatosSuplementarios = null;
        ArrayList<TramitacionExpedientesValueObject> tramites = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            adapt.inicioTransaccion(con);
            
            tramites = TramitacionExpedientesDAO.getInstance().getTramitesNotificadosNoE(codOrg, ejerc, codProc, numExp, enHistorico, con);
            if(!tramites.isEmpty()){
                for(TramitacionExpedientesValueObject tram : tramites){
                    estructuraDatosSuplementarios = new Vector<EstructuraCampo>();
                    Vector<EstructuraCampo> estructuraAux = cargaEstructuraDatosSuplementarios(tram, params);
                    if(!estructuraAux.isEmpty()){
                        for(EstructuraCampo eCampo : estructuraAux){
                            // Comprobamos que el campo suplementario sea de tipo numérico, fecha o desplegable
                            String tipo = eCampo.getCodTipoDato();
                            if(tipo.equals("1") || tipo.equals("3") || tipo.equals("6"))
                                estructuraDatosSuplementarios.add(eCampo);
                        }
                        tram.setEstructuraDatosSuplementarios(estructuraDatosSuplementarios);

                        valoresDatosSuplementarios = TramitacionExpedientesManager.getInstance().cargaValoresDatosSuplementarios(tram, estructuraDatosSuplementarios, params);
                        tram.setValoresDatosSuplementarios(valoresDatosSuplementarios);
                    }
                } 
            }
            adapt.finTransaccion(con);
            
        } catch (BDException ex) {
            ex.printStackTrace();
            m_Log.error("Error al obtener una conexión a la bbdd");
        } catch (TechnicalException ex) {
            ex.printStackTrace();
            m_Log.error(ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error: "+e.getMessage());
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                ex.printStackTrace();
                m_Log.error("Error al cerrar la conexión a la bbdd.");
            }
        }
        return tramites;
    }
    
    /**
     * Obtiene datos del trámite (cargo y uin) y comprueba si el usuario tiene
     * ese cargo en esa/s UORS
     * @param tramite
     * @param usuario
     * @param params
     * @return 
     */
    public String comprobarPermisoUsuarioIniciarTramite(TramitacionExpedientesValueObject tramite, UsuarioValueObject usuario, String[] params) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String tienePermiso = "no";
        ArrayList<String> listaUnidadesUsuario = null;
        ArrayList<Integer> listaUnidadesTramite = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            //Recuperamos datos de la definición del trámite
            DefinicionTramitesValueObject defTramite = new DefinicionTramitesValueObject();
            defTramite.setTxtCodigo(tramite.getCodProcedimiento());
            defTramite.setCodigoTramite(tramite.getCodTramite());
            defTramite = DefinicionTramitesDAO.getInstance().getTramite(defTramite, tramite.getCodOrganizacion(), con, params);
            
            //Comprobamos se el trámite tiene unidad de inicio manual
            //Si no existe el trámite no se puede iniciar de forma manual en cuyo caso no hacemos nada
            if(defTramite.getCodUnidadInicio() != null){  
                //Recuperamos las unidades sobre las que tiene permiso el usuario
                listaUnidadesUsuario = UsuariosGruposDAO.getInstance().getListaCodigosUnidadesOrganicasUsuario(usuario.getIdUsuario(), usuario.getOrgCod(), usuario.getEntCod(), con);
                //Recuperar lista de unidades de inicio manual según la definición del trámite
                listaUnidadesTramite = (ArrayList<Integer>) TramitacionExpedientesDAO.getInstance().recuperarListaUnidadesInicioManual(tramite, defTramite.getCodUnidadInicio(), listaUnidadesUsuario, con);
                
                if(defTramite.getCodCargo() == null){ //El trámite no tiene definido un cargo que limite el inicio
                    //Comprobamos el permiso del usuario sobre las unidades del trámite: revisamos si cada unidad de la lista de unidades del trámite existe en la lista de unidades del usuario
                    for(Integer codigoUIN : listaUnidadesTramite){
                        if(listaUnidadesUsuario.contains(String.valueOf(codigoUIN))){
                            tienePermiso = "si";
                            break;
                        }
                    }
                } else {
                    //Comprobamos si el usuario tiene para la unidad de inicio del trámite el cargo del mismo
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("usuario", String.valueOf(usuario.getIdUsuario()));
                    gVO.setAtributo("codOrganizacion", String.valueOf(usuario.getOrgCod()));
                    gVO.setAtributo("codEntidad", String.valueOf(usuario.getEntCod()));
                    
                    String permiso = "no";
                    for(Integer codigo : listaUnidadesTramite){
                        permiso = TramitesExpedienteDAO.getInstance().permisoTramitacionUsuario(gVO, defTramite.getCodCargo(), String.valueOf(codigo), con);
                        if("si".equals(permiso)){
                            break;
                        }
                    }
                    tienePermiso = permiso;
                }
            }
            
        } catch (BDException bdex) {
            m_Log.error("Se ha producido un error al obtener la conexión a la base de datos.", bdex);
        } catch (AnotacionRegistroException arex) {
            m_Log.error("Se ha producido un error al recuperar datos de trámite.", arex);
        } catch (TechnicalException tex) {
            m_Log.error("Se ha producido un error al recuperar datos de trámite.", tex);
        } catch (SQLException sqlex) {
            m_Log.error("Se ha producido un error al recuperar la lista de códigos de UOR que pueden iniciar el trámite.", sqlex);
        } catch (Exception ex) {
            m_Log.error("Se ha producido un error al recuperar la lista de códigos de UOR permitidas para el usuario.", ex);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqlex){
                m_Log.error("Se ha producido un error al devolver la conexión de base de datos.", sqlex);
            }
        }
        
        return tienePermiso;
    }
}
