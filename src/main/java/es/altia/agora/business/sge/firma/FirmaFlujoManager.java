package es.altia.agora.business.sge.firma;

import es.altia.agora.business.sge.firma.dao.FirmaFlujoDAO;
import es.altia.agora.business.sge.firma.exception.FlujoFirmaException;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaDocumentoTramiteClave;
import es.altia.agora.business.sge.firma.vo.FirmaFirmanteVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoUsuariosVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.business.sge.firma.vo.FirmaTipoVO;
import es.altia.agora.business.sge.firma.vo.FirmaUsuarioVO;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.pentaho.reporting.libraries.base.util.StringUtils;

public class FirmaFlujoManager {

    private static FirmaFlujoManager instance;

    protected static final Log log = LogFactory.getLog(FirmaFlujoManager.class.getName());

    private static FirmaFlujoDAO firmasFlujoDAO;
    
    private static Config configPortafirmas;

    // Mensajes de error
    private static final String ERROR_OBTENER_LISTADO_FLUJOS_FIRMA_BBDD
            = "Ha ocurrido un error de bbdd al intentar obtener el listado de flujos de firma";
    private static final String ERROR_OBTENER_LISTADO_TIPO_FIRMAS_BBDD
            = "Ha ocurrido un error de bbdd al intentar obtener el listado de tipos de firma";
    private static final String ERROR_INSERTAR_FLUJO_FIRMA_BBDD
            = "Ha ocurrido un error de bbdd al intentar insertar el flujo de firma";
    private static final String ERROR_ELIMINAR_FLUJO_FIRMA_BBDD
            = "Ha ocurrido un error de bbdd al intentar eliminar el flujo de firma";
    private static final String ERROR_ACTIVAR_DESACTIVAR_FLUJO_FIRMA_BBDD
            = "Ha ocurrido un error de bbdd al intentar activar/desactivar el flujo de firma";
    private static final String ERROR_ACTUALIZAR_CIRCUITO_BBDD
            = "Ha ocurrido un error de bbdd al intentar actualizar los datos del circuito de firma";
    private static final String ERROR_ACTUALIZAR_CIRCUITO_PERSONALIZADO_TRAMITE_BBDD
            = "Ha ocurrido un error de bbdd al intentar actualizar los datos del circuito de firma del documento de tramite";
    private static final String ERROR_ACTUALIZAR_USUARIO_PERSONALIZADO_TRAMITE_BBDD
            = "Ha ocurrido un error de bbdd al intentar actualizar los datos del usuario firmante del documento de tramite";
    private static final String ERROR_OBTENER_LISTADO_CIRCUITO_BY_FLUJO_ID_BBDD
            = "Ha ocurrido un error de bbdd al intentar obtener el listado de circuito del flujo de firma";
    private static final String ERROR_OBTENER_LISTADO_USUARIOS_FIRMAS_BBDD
            = "Ha ocurrido un error de bbdd al intentar obtener el listado de usuarios firmantes";
    private static final String ERROR_OBTENER_LISTADO_FLUJO_USUARIOS_FIRMAS_BBDD
        = "Ha ocurrido un error de bbdd al intentar obtener el listado de flujos con usuarios firmantes";
    private static final String ERROR_OBTENER_FLUJO_USUARIOS_FIRMAS_BBDD
        = "Ha ocurrido un error de bbdd al intentar obtener el flujo con usuarios firmantes";
    private static final String ERROR_OBTENER_FLUJO_BBDD
        = "Ha ocurrido un error de bbdd al intentar obtener el flujo de firmas";
    private static final String ERROR_OBTENER_ID_FLUJO_BBDD
        = "Ha ocurrido un error de bbdd al intentar obtener el id del flujo de firmas";
    private static final String ERROR_OBTENER_USUARIO_FIRMA_POR_DEFECTO_BBDD
        = "Ha ocurrido un error de bbdd al intentar obtener el usuario firmante por defecto del documento de tramite";
    private static final String ERROR_OBTENER_TIPO_FIRMA_BBDD
        = "Ha ocurrido un error de bbdd al intentar obtener el tipo de firma";
    private static final String ERROR_OBTENER_ESTADOS_CIRCUITO_FIRMA_BBDD
        = "Ha ocurrido un error de bbdd al intentar obtener los estados de la firma del circuito";
    private static final String ERROR_OBTENER_COMPROBAR_TIPO_FIRMA_DOC_TRAMITACION_BBDD
        = "Ha ocurrido un error de bbdd al intentar comprobar el tipo de firma del documento de tramitacion";
    
    private FirmaFlujoManager() {
        firmasFlujoDAO = FirmaFlujoDAO.getInstance();
        configPortafirmas = ConfigServiceHelper.getConfig("Portafirmas");
    }

    /**
     * Obtiene una instancia unica.
     *
     * @return
     */
    public static FirmaFlujoManager getInstance() {
        FirmaFlujoManager temp = instance;
        if (temp == null) {
            synchronized (FirmaFlujoManager.class) {
                temp = instance;
                if (temp == null) {
                    temp = new FirmaFlujoManager();
                    instance = temp;
                }
            }
        }

        return instance;
    }

    /**
     * Obtiene el listado de flujos de firma disponibles
     *
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public List<FirmaFlujoVO> getListaFlujosFirma(String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<FirmaFlujoVO> listaFlujos = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            listaFlujos = firmasFlujoDAO.getListaFlujosFirma(con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_LISTADO_FLUJOS_FIRMA_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listaFlujos;
    }

    /**
     * Obtiene el flujo a partir del id
     *
     * @param id
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public FirmaFlujoVO getFlujoFirma(Integer id, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        FirmaFlujoVO flujo = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            flujo = firmasFlujoDAO.getFlujoFirma(id, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_FLUJO_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return flujo;
    }
    
    /**
     * Obtiene el listado de firmantes del circuito de un flujo de firma
     *
     * @param idFlujoFirma
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public List<FirmaCircuitoVO> getListaCircuitoFirmasByIdFlujo(Integer idFlujoFirma, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<FirmaCircuitoVO> listaCircuitoFlujo = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            listaCircuitoFlujo = firmasFlujoDAO.getListaCircuitoFirmasByIdFlujo(idFlujoFirma, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_LISTADO_CIRCUITO_BY_FLUJO_ID_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listaCircuitoFlujo;
    }

    /**
     * Insertar un flujo de firma
     *
     * @param nombreFlujo
     * @param idTipoFlujoFirma
     * @param activo
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public Integer insertarFlujoFirma(String nombreFlujo, Integer idTipoFlujoFirma, Boolean activo, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Integer idFlujo = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            FirmaFlujoVO flujo = new FirmaFlujoVO();
            flujo.setNombre(nombreFlujo);
            flujo.setIdTipoFirma(idTipoFlujoFirma);
            flujo.setActivo(activo);

            idFlujo = firmasFlujoDAO.insertarFlujoFirma(flujo, adapt, con);

            SigpGeneralOperations.commit(adapt, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());

            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error(te.getMessage());
            }

            throw new FlujoFirmaException(ERROR_INSERTAR_FLUJO_FIRMA_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return idFlujo;
    }

    /**
     * Eliminar un flujo de firma
     *
     * @param idFlujoFirma
     * @param params
     * @throws FlujoFirmaException
     */
    public void eliminarFlujoFirma(Integer idFlujoFirma, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            firmasFlujoDAO.eliminarFlujoFirma(idFlujoFirma, con);

            SigpGeneralOperations.commit(adapt, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());

            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error(te.getMessage());
            }

            throw new FlujoFirmaException(ERROR_ELIMINAR_FLUJO_FIRMA_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Activa/Desactiva un flujo de firma
     *
     * @param idFlujoFirma
     * @param activar
     * @param params
     * @throws FlujoFirmaException
     */
    public void activarDesactivarFlujoFirma(Integer idFlujoFirma, Boolean activar, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            firmasFlujoDAO.activarDesactivarFlujoFirma(idFlujoFirma, activar, con);

            SigpGeneralOperations.commit(adapt, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());

            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error(te.getMessage());
            }

            throw new FlujoFirmaException(ERROR_ACTIVAR_DESACTIVAR_FLUJO_FIRMA_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene el listado de tipos de firma disponibles
     *
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public List<FirmaTipoVO> getListaTipoFirmas(String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<FirmaTipoVO> listaTipoFirmas = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            listaTipoFirmas = firmasFlujoDAO.getListaTipoFirmas(con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_LISTADO_TIPO_FIRMAS_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listaTipoFirmas;
    }

    /**
     * Obtiene el listado de los usuarios de firma disponibles
     *
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public List<FirmaUsuarioVO> getListaUsuariosFirmaDisponibles(String codOrganizacion, String portafirmas, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<FirmaUsuarioVO> listaUsuarios = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            boolean obligatorioNIF = false;
            boolean obligBuzonFirma = false;

            Boolean existePortafirmasExterno
                    = PluginPortafirmasExternoClienteFactoria.getExistePortafirmasExterno(codOrganizacion);
            
            if (existePortafirmasExterno) {
                String propObligatorioNif = configPortafirmas.getString(
                        String.format("%s/PortafirmasExterno/obligatorioFirmanteDocumentoIdentificativo",
                                codOrganizacion));

                if (StringUtils.equalsIgnoreCase(ConstantesDatos.SI, propObligatorioNif)) {
                    obligatorioNIF = true;
                }
            }
            
            if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
                obligatorioNIF = false;
                obligBuzonFirma = true;
            }

            listaUsuarios = firmasFlujoDAO.getListaUsuariosFirmaDisponibles(obligatorioNIF, obligBuzonFirma, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_LISTADO_USUARIOS_FIRMAS_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listaUsuarios;
    }

    /**
     * Obtiene un listado de flujos con los usuarios asociados al mismo. Si se
     * especifica el flag activo a true, solo se seleccionaran los que estan
     * activos.
     *
     * @param activo
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public List<FirmaFlujoUsuariosVO> getListaFlujosFirmaConUsuarios(Boolean activo, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<FirmaFlujoUsuariosVO> listaFlujosUsuario = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            listaFlujosUsuario = firmasFlujoDAO.getListaFlujosFirmaConUsuarios(null, activo, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_LISTADO_FLUJO_USUARIOS_FIRMAS_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listaFlujosUsuario;
    }

    /**
     * Obtiene un listado del estado de las firmas del circuito (E_CRD_FIR_FIRMANTES)
     * 
     * @param clave
     * @param params
     * @return
     * @throws FlujoFirmaException 
     */
    public List<FirmaFirmanteVO> getListaEstadosCircuitoFirmas(
            FirmaDocumentoTramiteClave clave, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        List<FirmaFirmanteVO> listaEstadosFirma = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            listaEstadosFirma = firmasFlujoDAO.getListaEstadosCircuitoFirmas(
                    clave, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_ESTADOS_CIRCUITO_FIRMA_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listaEstadosFirma;
    }
    
    /**
     * Obtiene el id de flujo de firma a partir de la pk del documento de tramite (E_CRD)
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param codOcurrencia
     * @param codDocumento
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public Integer getIdFlujoByDocumentoTramite(
            Integer codMunicipio, String codProcedimiento, Integer ejercicio, String numExpediente,
            Integer codTramite, Integer codOcurrencia, Integer codDocumento,
            String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Integer idFlujo = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            idFlujo = firmasFlujoDAO.getIdFlujoByDocumentoTramite(
                    codMunicipio, codProcedimiento, ejercicio, numExpediente,
                    codTramite, codOcurrencia, codDocumento,
                    con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_ID_FLUJO_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return idFlujo;
    }
    
    /**
     * Obtiene un flujo con los usuarios asociados al mismo. Si se
     * especifica el flag activo a true, solo se buscara un flujo activo.
     * 
     * @param idFlujo
     * @param activo
     * @param params
     * @return 
     * @throws FlujoFirmaException 
     */
    public FirmaFlujoUsuariosVO getFlujoFirmaConUsuarios(Integer idFlujo, Boolean activo, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        FirmaFlujoUsuariosVO firmaFlujoUsuarios = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            List<FirmaFlujoUsuariosVO> listaFlujosUsuario =
                    firmasFlujoDAO.getListaFlujosFirmaConUsuarios(idFlujo, activo, con);
            
            if (listaFlujosUsuario != null && !listaFlujosUsuario.isEmpty()) {
                firmaFlujoUsuarios = listaFlujosUsuario.get(0);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_FLUJO_USUARIOS_FIRMAS_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return firmaFlujoUsuarios;
    }

    /**
     * Obtiene el usuario firmante definido por defecto del documento de tramite.
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param codOcurrencia
     * @param codDocumento
     * @param params
     * @return
     * @throws FlujoFirmaException 
     */
    public FirmaUsuarioVO getUsuarioPorDefectoByDocumentoTramite(
            Integer codMunicipio, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTramite, Integer codOcurrencia,
            Integer codDocumento, String portafirmas, String[] params)
            throws FlujoFirmaException {
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        FirmaUsuarioVO firmaUsuario = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            firmaUsuario = firmasFlujoDAO.getUsuarioPorDefectoByDocumentoTramite(
                    codMunicipio, codProcedimiento, ejercicio, numExpediente,
                    codTramite, codOcurrencia, codDocumento, portafirmas,
                    con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_USUARIO_FIRMA_POR_DEFECTO_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return firmaUsuario;
    }
    
    /**
     * Obtiene el usuario firmante seleccionado del documento de tramite.(E_CRD_FIR_FIRMANTE)
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param codOcurrencia
     * @param codDocumento
     * @param params
     * @return
     * @throws FlujoFirmaException 
     */
    public FirmaUsuarioVO getUsuarioFirmanteByDocumentoTramite(
            Integer codMunicipio, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTramite, Integer codOcurrencia,
            Integer codDocumento, String portafirmas, String[] params)
            throws FlujoFirmaException {
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        FirmaUsuarioVO firmaUsuario = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            firmaUsuario = firmasFlujoDAO.getUsuarioFirmanteByDocumentoTramite(
                    codMunicipio, codProcedimiento, ejercicio, numExpediente,
                    codTramite, codOcurrencia, codDocumento, portafirmas,
                    con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_USUARIO_FIRMA_POR_DEFECTO_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return firmaUsuario;
    }
    
    
    
    /**
     * Obtiene el tipo de firma a partir del id
     *
     * @param id
     * @param params
     * @return
     * @throws FlujoFirmaException
     */
    public FirmaTipoVO getTipoFirma(Integer id, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        FirmaTipoVO tipo = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            tipo = firmasFlujoDAO.getTipoFirma(id, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_TIPO_FIRMA_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tipo;
    }
    
    /**
     * Actualiza los datos de los firmantes del circuito de firmas
     *
     * @param idFlujoFirma
     * @param idsUsuario
     * @param params
     * @throws FlujoFirmaException
     */
    public void actualizarDatosCircuitoFlujo(Integer idFlujoFirma, List<Integer> idsUsuario, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            firmasFlujoDAO.actualizarDatosCircuitoFlujo(idFlujoFirma, idsUsuario, con);

            SigpGeneralOperations.commit(adapt, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());

            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error(te.getMessage());
            }

            throw new FlujoFirmaException(ERROR_ACTUALIZAR_CIRCUITO_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Actualiza los datos de los firmantes del circuito de firmas personalizado
     * de un documento de tramite.
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param codOcurrencia
     * @param codDocumento
     * @param idTipoFirma
     * @param idsUsuario
     * @param params
     * @throws FlujoFirmaException 
     */
    public void actualizarFlujoCircuitoTramitePersonalizado(
            Integer codMunicipio, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTramite, Integer codOcurrencia,
            Integer codDocumento, Integer idTipoFirma, List<Integer> idsUsuario,
            String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            FirmaDocumentoTramiteClave clave = new FirmaDocumentoTramiteClave();
            clave.setCodMunicipio(codMunicipio);
            clave.setCodProcedimiento(codProcedimiento);
            clave.setEjercicio(ejercicio);
            clave.setNumExpediente(numExpediente);
            clave.setCodTramite(codTramite);
            clave.setCodOcurrencia(codOcurrencia);
            clave.setCodDocumento(codDocumento);
            
            // Borramos los firmantes
            firmasFlujoDAO.eliminarFirmantesTramitePersonalizado(clave, con);
            
            // Borrar los datos del flujo
            firmasFlujoDAO.eliminarFlujoTramitePersonalizado(clave, con);
            
            // Insertamos el flujo
            firmasFlujoDAO.insertarFlujoTramitePersonalizado(clave, idTipoFirma, con);

            // Insertamos los firmantes
            firmasFlujoDAO.insertarFirmantesTramitePersonalizado(clave, idsUsuario, con);
            
            SigpGeneralOperations.commit(adapt, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());

            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error(te.getMessage());
            }

            throw new FlujoFirmaException(ERROR_ACTUALIZAR_CIRCUITO_PERSONALIZADO_TRAMITE_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Actualiza los datos del usuario firmante personalizado
     * de un documento de tramite.
     * 
     * @param codMunicipio
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codTramite
     * @param codOcurrencia
     * @param codDocumento
     * @param idUsuario
     * @param params
     * @throws FlujoFirmaException 
     */
    public void actualizarUsuarioTramitePersonalizado(
            Integer codMunicipio, String codProcedimiento, Integer ejercicio,
            String numExpediente, Integer codTramite, Integer codOcurrencia,
            Integer codDocumento, Integer idUsuario, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            FirmaDocumentoTramiteClave clave = new FirmaDocumentoTramiteClave();
            clave.setCodMunicipio(codMunicipio);
            clave.setCodProcedimiento(codProcedimiento);
            clave.setEjercicio(ejercicio);
            clave.setNumExpediente(numExpediente);
            clave.setCodTramite(codTramite);
            clave.setCodOcurrencia(codOcurrencia);
            clave.setCodDocumento(codDocumento);
            
            List<Integer> listaIds = new ArrayList<Integer>();
            listaIds.add(idUsuario);
            
            // Borramos los firmantes
            firmasFlujoDAO.eliminarFirmantesTramitePersonalizado(clave, con);
            
            // Insertamos los firmantes
            firmasFlujoDAO.insertarFirmantesTramitePersonalizado(clave, listaIds, con);
            
            //se actualiza el usuario de modificacion del documento del tramite
            firmasFlujoDAO.actualizarUsuarioModificacionDocumentoTramite(clave, listaIds, con);
            
            SigpGeneralOperations.commit(adapt, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());

            try {
                SigpGeneralOperations.rollBack(adapt, con);
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error(te.getMessage());
            }

            throw new FlujoFirmaException(ERROR_ACTUALIZAR_USUARIO_PERSONALIZADO_TRAMITE_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Comprueba si el tipo de firma para un documento de tramitacion que ya ha
     * sido enviado al portafirmas es de tipo nuevo L o U (Flujo o Un Usuario))
     * 
     * @param clave
     * @param params
     * @return
     * @throws FlujoFirmaException 
     */
    public Boolean comprobarSiTipoFirmaFlujoUsuarioDocumentoTramitacion(
            FirmaDocumentoTramiteClave clave, String[] params)
            throws FlujoFirmaException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Boolean resultado = null;

        try {
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            resultado = firmasFlujoDAO.comprobarSiTipoFirmaFlujoUsuarioDocumentoTramitacion(clave, con);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FlujoFirmaException(ERROR_OBTENER_COMPROBAR_TIPO_FIRMA_DOC_TRAMITACION_BBDD, ex);
        } finally {
            try {
                SigpGeneralOperations.devolverConexion(adapt, con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultado;
    }
}
