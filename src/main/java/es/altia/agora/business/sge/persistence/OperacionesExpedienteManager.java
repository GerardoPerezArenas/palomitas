/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge.persistence;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.eni.util.TiposRegistro;
import es.altia.merlin.licitacion.commons.utils.integraciones.gv.webservices.utils.Tipo;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author fran
 */
public class OperacionesExpedienteManager {
          
  private static OperacionesExpedienteManager instance = null;
  protected static Log m_Log =  LogFactory.getLog(OperacionesExpedienteManager.class.getName());  
  
  /**
  * Factory method para el <code>Singelton</code>.
  * @return La unica instancia de OperacionesExpedienteManager
  */
  public static OperacionesExpedienteManager getInstance() {
    //Si no hay una instancia de esta clase tenemos que crear una.
    synchronized(OperacionesExpedienteManager.class) {
        if (instance == null)
        instance = new OperacionesExpedienteManager();      
    }
    return instance;
  }
/**
 * Funcion generica utilizada para registrar cualquier operacion relacionada con la importacion/exportacion de expedientes
 * 
 * @param tipoOp
 * @param usuario
 * @param numExp
 * @param params
 * @throws TechnicalException 
 */
  public void registrarOperacionExpediente(TiposRegistro tipoOp, UsuarioValueObject usuario, String numExp, String[] params) throws TechnicalException{
		if (m_Log.isErrorEnabled()){ m_Log.debug("registrarExportacionExpediente");}
		AdaptadorSQLBD oad = null;
		Connection con = null;
		try {
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();
			oad.inicioTransaccion(con);
			OperacionExpedienteVO operacion = crearOperacionExpedienteVO(tipoOp, usuario, numExp);
			OperacionesExpedienteDAO.getInstance().registrarOperacionExpediente(operacion, con);
			oad.finTransaccion(con);
		} catch (TechnicalException te) {
			m_Log.error(te.getMessage());
			SigpGeneralOperations.rollBack(oad, con);
			throw new TechnicalException(te.getMessage(), te);
		} catch (Exception e) {
			m_Log.error(e.getMessage());
			SigpGeneralOperations.rollBack(oad, con);
			throw new TechnicalException(e.getMessage(), e);
		} finally {
			SigpGeneralOperations.devolverConexion(oad, con);
		}
	}
	
	/**
	 * Crea un objeto OperacionExpedienteVO a partir de los datos de la sesion y del usuario
	 * @param tipoOp
	 * @param usuario
	 * @param numExp Numero del expediente. No puede ser null.
	 * @return 
	 */
	public OperacionExpedienteVO crearOperacionExpedienteVO (TiposRegistro tipoOp, UsuarioValueObject usuario, String numExp){
		OperacionExpedienteVO operacion = new OperacionExpedienteVO();
		
		operacion.setCodMunicipio(usuario.getOrgCod());
		operacion.setEjercicio(Integer.parseInt(numExp.substring(0, 4)));
		operacion.setNumExpediente(numExp);
		
		//se utiliza un if en vez de un switch por tratarse de pocas operaciones
		if (tipoOp == TiposRegistro.EXPORTACION){
			operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_EXPORTAR_EXPEDIENTE);
		}else if (tipoOp == TiposRegistro.IMPORTACION){
			operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_IMPORTAR_EXPEDIENTE);
		}
		operacion.setFechaOperacion(new GregorianCalendar());
		operacion.setCodUsuario(usuario.getIdUsuario());
		return operacion;
	}
  /*
   * Da de alta un movimiento de inicio de expediente en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarAltaExpediente(GeneralValueObject infoExpYTramiteInicio, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarAltaExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarAltaExpediente(infoExpYTramiteInicio,con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }
  
  /*
   * Da de alta un movimiento de grabar expediente en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarGrabarExpediente(GeneralValueObject infoExp, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarGrabarExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarGrabarExpediente(infoExp, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }
  
  /*
   * Da de alta un movimiento de reabrir expediente en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarReabrirExpediente(GeneralValueObject infoReabrirExp, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarReabrirExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarReabrirExpediente(infoReabrirExp, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }
  
  /*
   * Da de alta un movimiento de finalizar expediente en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarFinalizarExpediente(TramitacionExpedientesValueObject infoFinExp, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarFinalizarExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarFinalizarExpediente(infoFinExp, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }
  
  /*
   * Da de alta un movimiento de anular expediente en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarAnularExpediente(TramitacionExpedientesValueObject infoAnularExp, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarAnularExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarAnularExpediente(infoAnularExp, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }
  
  /*
   * Da de alta un movimiento de grabar trámite en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarGrabarTramite(TramitacionExpedientesValueObject infoTramite, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarGrabarTramite");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarGrabarTramite(infoTramite, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }
  
  /**
   * Recupera todos los datos necesarios antes de registrar la operación de inicio de trámite
   * @param tramExpVO
   * @param listaTramitesIniciados
   * @param listaTramitesNoIniciados
   * @param listadoTramitesExpediente
   * @param paramsRegistroOperacion
   * @param usuario
   * @param params
   * @param manual
   * @throws TechnicalException 
   */
  public void previoRegistrarIniciarTramitePrepararDatos(TramitacionExpedientesValueObject tramExpVO,
            Vector<TramitacionExpedientesValueObject> listaTramitesIniciados, Vector<TramitacionExpedientesValueObject> listaTramitesNoIniciados,
            Vector<GeneralValueObject> listadoTramitesExpediente, GeneralValueObject paramsRegistroOperacion,
            String[] params, boolean manual)
            throws TechnicalException {
        
        List<TramitacionExpedientesValueObject> listaTramitesCorrectos = 
                extraerTramitesIniciadosCorrectamente(listaTramitesIniciados, listaTramitesNoIniciados);
        
        TramitacionExpedientesValueObject tramiteIniciado = null;
        String codTramiteIniciado = null;
        GeneralValueObject tramite = null;
        for (int i = 0; i < listaTramitesCorrectos.size(); i++) {
            tramiteIniciado = listaTramitesIniciados.get(i);
            codTramiteIniciado = tramiteIniciado.getCodTramite();

            tramite = new GeneralValueObject();
            // Pueden existir varios tramites con el mismo codTramite, por lo que se busca
            // al reves, ya que la lista esta ordenada del tramite mas antiguo al mas reciente
            for (int j = listadoTramitesExpediente.size() - 1; j >= 0; j--) {
                tramite = listadoTramitesExpediente.get(j);
                if (codTramiteIniciado.equals(tramite.getAtributo("codTramite"))) {
                    break;
                }
            }
            
            paramsRegistroOperacion.setAtributo("codTramite", tramite.getAtributoONulo("codTramite"));
            paramsRegistroOperacion.setAtributo("nomTramite", tramite.getAtributoONulo("tramite"));
            paramsRegistroOperacion.setAtributo("ocurrTramite", tramite.getAtributoONulo("ocurrenciaTramite"));
            paramsRegistroOperacion.setAtributo("fechaInicioTramite", tramite.getAtributoONulo("fehcaInicio"));

            OperacionesExpedienteManager.getInstance().registrarIniciarTramite(paramsRegistroOperacion, manual, params);
        }
    }
  
 
  /*
   * Da de alta un movimiento de grabar inicio de trámite manual en la tabla de operaciones de expedientes
   * @param infoTramite Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @param manual Si se inicia de forma manual o automatica
   * @return void
   * @exception TechnicalException
   */
  public void registrarIniciarTramite(GeneralValueObject infoTramite, boolean manual, String[] params) throws TechnicalException{  

    m_Log.debug("registrarIniciarTramite");

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarIniciarTramite(infoTramite, manual, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }
  
  /*
   * Da de alta un movimiento de grabar finalización de trámite en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarFinalizarTramite(TramitacionExpedientesValueObject infoTramite, Boolean desfavorable, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarFinalizarTramite");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarFinalizarTramite(infoTramite, desfavorable, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }
  
   /*
   * Da de alta un movimiento de retroceder trámite en la tabla de operaciones de expedientes
   * @param infoTramite Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarRetrocederTramite(GeneralValueObject infoTramite, String[] params) 
          throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarRetrocederTramite");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarRetrocederTramite(infoTramite, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }
  
  
  public void registrarReabrirTramite(GeneralValueObject infoTramite, String[] params) 
          throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarReabrirTramite");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarReabrirTramite(infoTramite, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }

    /*
     * Da de alta un movimiento de retroceso de trámite de origen en la tabla de operaciones de expedientes
     * @param infoTramite Objeto con los datos necesarios para actualizar el registro de historico
     * @param params Parametros de la conexión
     * @return void
     * @exception TechnicalException
     */
    public void registrarRetrocederTramiteOrigen(GeneralValueObject infoTramite, String[] params)
            throws TechnicalException {

        if (m_Log.isErrorEnabled()) {
            m_Log.debug("registrarReaperturaTramiteOrigen");
        }

        AdaptadorSQLBD oad = null;
        Connection con = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            OperacionesExpedienteDAO.getInstance().registrarRetrocederTramiteOrigen(infoTramite, con);

            oad.finTransaccion(con);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage());
            SigpGeneralOperations.rollBack(oad, con);
            throw new TechnicalException(te.getMessage(), te);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            SigpGeneralOperations.rollBack(oad, con);
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }
  
    /*
     * Da de alta un movimiento de bloquear trámite en la tabla de operaciones de expedientes
     * @param historico Objeto con los datos necesarios para actualizar el registro de historico
     * @param params Parametros de la conexión
     * @return void
     * @exception TechnicalException
     */
    public void registrarBloquearTramite(GeneralValueObject infoTramite, String[] params) throws TechnicalException {

        if (m_Log.isErrorEnabled()) {
            m_Log.debug("registrarBloquearTramite");
        }

        AdaptadorSQLBD oad = null;
        Connection con = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            OperacionesExpedienteDAO.getInstance().registrarBloquearTramite(infoTramite, con);

            oad.finTransaccion(con);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage());
            SigpGeneralOperations.rollBack(oad, con);
            throw new TechnicalException(te.getMessage(), te);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            SigpGeneralOperations.rollBack(oad, con);
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }

    /*
     * Da de alta un movimiento de desbloquear trámite en la tabla de operaciones de expedientes
     * @param historico Objeto con los datos necesarios para actualizar el registro de historico
     * @param params Parametros de la conexión
     * @return void
     * @exception TechnicalException
     */
    public void registrarDesbloquearTramite(GeneralValueObject infoTramite, String[] params) throws TechnicalException {

        if (m_Log.isErrorEnabled()) {
            m_Log.debug("registrarDesbloquearTramite");
        }

        AdaptadorSQLBD oad = null;
        Connection con = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            OperacionesExpedienteDAO.getInstance().registrarDesbloquearTramite(infoTramite, con);

            oad.finTransaccion(con);
        } catch (TechnicalException te) {
            m_Log.error(te.getMessage());
            SigpGeneralOperations.rollBack(oad, con);
            throw new TechnicalException(te.getMessage(), te);
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            SigpGeneralOperations.rollBack(oad, con);
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
        }
    }
    
   /*
   * Da de alta un movimiento de alta de interesado en la tabla de operaciones de expedientes
   * @param codOrg Código de organización
   * @param numExpediente Número de expediente
   * @param codUsu Código de usuario
   * @param nomUsu Nombre de usuario
   * @param tercero Tercero
   * @param con Conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarAltaInteresado(int codOrg, String numExpediente, int codUsu, String nomUsu, 
            TercerosValueObject tercero, Connection con) throws TechnicalException {

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarAltaInteresado");}

    try{
      OperacionesExpedienteDAO.getInstance().registrarAltaInteresado(codOrg, numExpediente, codUsu, nomUsu, 
            tercero, con);
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        throw new TechnicalException(e.getMessage(), e);
    }
  }

   /*
   * Da de alta un movimiento de modificación de interesado en la tabla de operaciones de expedientes
   * @param gVO GeneralValueObject con información de la modificación
   * @param cambioVersion Indicador de cambio de versión
   * @param cambioRol Indicador de cambio de rol
   * @param cambioDomicilio Indicador de cambio de domicilio
   * @param cambioNotifElec Indicador de cambio de notificación
   * @param cambioDatos Indicador de cambio de datos
   * @param con Conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarModificacionInteresado(GeneralValueObject gVO, boolean cambioVersion, boolean cambioRol, 
                boolean cambioDomicilio, boolean cambioNotifElec, boolean cambioDatos, Connection con) 
          throws TechnicalException {

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarModificacionInteresado");}

    try{
      OperacionesExpedienteDAO.getInstance().registrarModificacionInteresado(gVO, cambioVersion, cambioRol, 
                cambioDomicilio, cambioNotifElec, cambioDatos, con);
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        throw new TechnicalException(e.getMessage(), e);
    }
  }

   /*
   * Da de alta un movimiento de baja de interesado en la tabla de operaciones de expedientes
   * @param codOrg Código de organización
   * @param numExpediente Número de expediente
   * @param codUsu Código de usuario
   * @param nomUsu Nombre de usuario
   * @param tercero Tercero
   * @param con Conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarEliminacionInteresado(int codOrg, String numExpediente, int codUsu, String nomUsu, 
            TercerosValueObject tercero, Connection con) throws TechnicalException {

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarEliminacionInteresado");}
    
    try{
      OperacionesExpedienteDAO.getInstance().registrarEliminacionInteresado(codOrg, numExpediente, codUsu, nomUsu, 
            tercero, con);
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        throw new TechnicalException(e.getMessage(), e);
    }
  }

    /*
   * Da de alta un movimiento de añadir expediente relacionado en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarAnhadirRelacion(ConsultaExpedientesValueObject infoRelExp, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarAnhadirRelacion");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarAnhadirRelacion(infoRelExp, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }

    /*
   * Da de alta un movimiento de eliminar expediente relacionado en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarEliminarRelacion(ConsultaExpedientesValueObject infoRelExp, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarEliminarRelacion");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarEliminarRelacion(infoRelExp, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }

    /*
   * Da de alta un movimiento de añadir documento de expediente en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarAltaDocumentoExpediente(Documento doc, String nomUsuario, boolean externo, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarAltaDocumentoExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarAltaDocumentoExpediente(doc, nomUsuario, externo, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }

    /*
   * Da de alta un movimiento de eliminar documento de expediente en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarEliminacionDocumentoExpediente(Documento doc, String nomUsuario, boolean externo, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarEliminacionDocumentoExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarEliminacionDocumentoExpediente(doc, nomUsuario, externo, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }

    /*
   * Da de alta un movimiento de añadir documento de trámite en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarAltaDocumentoTramite(Documento doc, String nomUsuario, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarAltaDocumentoExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarAltaDocumentoTramite(doc, nomUsuario, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }

    /*
   * Da de alta un movimiento de eliminar documento de trámite en la tabla de operaciones de expedientes
   * @param historico Objeto con los datos necesarios para actualizar el registro de historico
   * @param params Parametros de la conexión
   * @return void
   * @exception TechnicalException
   */
  public void registrarEliminacionDocumentoTramite(Documento doc, String nomUsuario, String[] params) throws TechnicalException{  

    if (m_Log.isErrorEnabled()) {m_Log.debug("registrarEliminacionDocumentoExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
      oad.inicioTransaccion(con);        
        
      OperacionesExpedienteDAO.getInstance().registrarEliminacionDocumentoTramite(doc, nomUsuario, con);
      
      oad.finTransaccion(con);       
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());
        SigpGeneralOperations.rollBack(oad, con);
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                
        SigpGeneralOperations.rollBack(oad, con);        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
  }

  /*
   * Recupera una lista de registro de la tabla de movimientos de expedientes
   * @param numExpediente: Numero de expediente
   * @param traductor: Traductor
   * @return Lista de objetos OperacionExpedienteVO
   * @exception TechnicalException
   */
  public ArrayList <OperacionExpedienteVO> recuperarOperacionesExpediente(int codOrganizacion, String numExpediente, 
          boolean isExpHistorico, TraductorAplicacionBean traductor, String[] params) throws TechnicalException {      

    if (m_Log.isErrorEnabled()) {m_Log.debug("recuperarOperacionesExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    ArrayList <OperacionExpedienteVO> resultado = new ArrayList <OperacionExpedienteVO>();

    try{
        oad = new AdaptadorSQLBD(params);      
        con = oad.getConnection();
        oad.inicioTransaccion(con);

        // Se recuperan los movimientos de los interesados del expediente de la tabla OPERACIONES_EXPEDIENTE
        resultado = OperacionesExpedienteDAO.getInstance().recuperarOperacionesExpediente(codOrganizacion, numExpediente, 
                isExpHistorico, traductor, con);

        oad.finTransaccion(con);
    }catch (TechnicalException te) {
        try{
            oad.rollBack(con);
        }catch(Exception e){
            e.printStackTrace();
        }
        m_Log.error(te.getMessage());        
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        try{
            oad.rollBack(con);
        }catch(Exception f){
            f.printStackTrace();
        }
        m_Log.error(e.getMessage());                        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }

    return resultado;
}
    
  /*
   * Recupera un registro de la tabla de movimientos de expedientes
   * @param id Identificador de la tabla de historico
   * @param params Parametros de la conexión
   * @return Objetos OperacionExpedienteVO
   * @exception TechnicalException
   */
  public OperacionExpedienteVO recuperarOperacion (int idOperacion, boolean isExpHistorico, String[] params) throws TechnicalException{      

    if (m_Log.isErrorEnabled()) {m_Log.debug("recuperarOperacionesExpediente");}

    AdaptadorSQLBD oad = null;
    Connection con = null;
    OperacionExpedienteVO resultado = null;
    
    try{
      oad = new AdaptadorSQLBD(params);
      con = oad.getConnection();
              
      resultado = OperacionesExpedienteDAO.getInstance().recuperarOperacion(idOperacion, isExpHistorico, con);
    }catch (TechnicalException te) {
        m_Log.error(te.getMessage());      
        throw new TechnicalException(te.getMessage(), te);
    }catch (Exception e) {
        m_Log.error(e.getMessage());                        
        throw new TechnicalException(e.getMessage(), e);
    }finally{
       SigpGeneralOperations.devolverConexion(oad, con);
    }
    
    return resultado;
  }
  
  /**
     * Devuelve una lista de tramites que han sido iniciados correctamente
     * 
     * @param listaTramitesIniciados
     * @param listaTramitesNoIniciados
     * @return 
     */
    private List<TramitacionExpedientesValueObject> extraerTramitesIniciadosCorrectamente(
            Vector<TramitacionExpedientesValueObject> listaTramitesIniciados, Vector<TramitacionExpedientesValueObject> listaTramitesNoIniciados) {
        
        List<TramitacionExpedientesValueObject> lista = new ArrayList<TramitacionExpedientesValueObject>();
        boolean anadir = true;
        
        for (TramitacionExpedientesValueObject tramiteIniciado: listaTramitesIniciados) {
            String codTramiteIniciado = tramiteIniciado.getCodTramite();
            anadir = true;
            
            for (TramitacionExpedientesValueObject tramiteNoIniciado: listaTramitesNoIniciados) {
                String codTramiteNoIniciado = tramiteNoIniciado.getCodTramite();
                if (codTramiteIniciado.equals(codTramiteNoIniciado)) {
                    anadir = false;
                    break;
                }
            }
            
            if (anadir) {
                lista.add(tramiteIniciado);
            }
        }
        
        return lista;
    }
    
}
