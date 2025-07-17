// NOMBRE DEL PAQUETE
package es.altia.agora.business.sge.persistence;

// PAQUETES IMPORTADOS
import java.util.Vector;

import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.manual.ConsultaExpedientesDAO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>Título: Proyecto
 *
 * @gora</p> <p>Descripción: Clase DiligenciasManager</p> <p>Copyright:
 * Copyright (c) 2002</p> <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 *
 * @author Daniel Toril Cabrera
 * @version 1.0
 */
public class ConsultaExpedientesManager {

    private static ConsultaExpedientesManager instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(ConsultaExpedientesManager.class.getName());

    protected ConsultaExpedientesManager() {
        // Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

    public static ConsultaExpedientesManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronización aquí para serializar (no multithread)
            // las invocaciones a este metodo
            synchronized (ConsultaExpedientesManager.class) {
                if (instance == null) {
                    instance = new ConsultaExpedientesManager();
                }
            }
        }
        return instance;
    }

    public Vector consultar(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, boolean desdeGestion,
                            boolean buscarInicioProc,String columna,String tipoOrden,boolean adjuntarExpediente)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("consultar");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().consultar(uVO, consExpVO, params, desdeGestion, buscarInicioProc,columna,tipoOrden,adjuntarExpediente);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    public Vector consultarImprimir(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, boolean desdeGestion,
            boolean buscarInicioProc, String columna, String tipoOrden, boolean paraCSV)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("consultar");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().consultarImprimir(uVO, consExpVO, params, desdeGestion, buscarInicioProc, columna, tipoOrden, paraCSV);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    public Vector consultarImprimirOptimo(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, boolean desdeGestion,
            boolean buscarInicioProc, String columna, String tipoOrden, boolean paraCSV)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("consultar");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().consultarImprimirOptimo(uVO, consExpVO, params, desdeGestion, buscarInicioProc, columna, tipoOrden, paraCSV);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    public int contarExpedientes(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, boolean desdeGestion,
            boolean buscarInicioProc)
            throws AnotacionRegistroException {

        int numRelacionExpedientes = 0;

        m_Log.debug("contarExpedientes");

        try {

            m_Log.debug("Usando persistencia manual");
            numRelacionExpedientes = ConsultaExpedientesDAO.getInstance().contarExpedientes(uVO, consExpVO, params, desdeGestion, buscarInicioProc);
            m_Log.debug("contado");
            //We want to be informed when this method has finalized
            m_Log.debug("contarExpedientes");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return numRelacionExpedientes;
    }

    public int contarPorCamposSuplementarios(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, String columna, String tipoOrden)
            throws AnotacionRegistroException {

        int numRelacionExpedientes = 0;

        m_Log.debug("contar");

        try {

            m_Log.debug("Usando persistencia manual");
            numRelacionExpedientes = ConsultaExpedientesDAO.getInstance().contarPorCampoSuplementario(uVO, consExpVO, params, columna, tipoOrden);
            m_Log.debug("contado");
            //We want to be informed when this method has finalized
            m_Log.debug("contar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return numRelacionExpedientes;
    }

    public Vector consultaPorCamposSuplementarios(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, String columna, String tipoOrden)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("consultar");

        try {

            m_Log.debug("Usando persistencia manual");
            HashMap hash = consExpVO.getCamposSuplementarios();
            consulta = ConsultaExpedientesDAO.getInstance().consultarPorCampoSuplementario(uVO, consExpVO, params, columna, tipoOrden);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    public Vector consultaPorCamposSuplementariosImprimir(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, String columna, String tipoOrden, boolean paraCSV)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("consultar");

        try {

            m_Log.debug("Usando persistencia manual");
            HashMap hash = consExpVO.getCamposSuplementarios();
            consulta = ConsultaExpedientesDAO.getInstance().consultarPorCampoSuplementarioImprimir(uVO, consExpVO, params, columna, tipoOrden, paraCSV);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    public Vector getExpedientesRelacionados(ConsultaExpedientesValueObject defProcVO, String[] params)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("ConsultaExpedientesManager.consultar");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().getExpedientesRelacionados(defProcVO, params);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    public int insertExpedientesRelacionados(ConsultaExpedientesValueObject consExpVO, String[] params)
            throws AnotacionRegistroException {
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("insertExpedientesRelacionados");
        int i;

        try {
            m_Log.debug("Usando persistencia manual");

            i = ConsultaExpedientesDAO.getInstance().insertExpedientesRelacionados(consExpVO, params);

            m_Log.debug("procedimiento insertado correctamente");
            //We want to be informed when this method has finalized
            m_Log.debug("insertExpedientesRelacionados");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

        }
        return i;
    }

    public int eliminarExpedientesRelacionados(ConsultaExpedientesValueObject consExpVO, String[] params)
            throws AnotacionRegistroException {
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("eliminarExpedientesRelacionados");
        int i;

        try {
            m_Log.debug("Usando persistencia manual");

            i = ConsultaExpedientesDAO.getInstance().eliminarExpedientesRelacionados(consExpVO, params);

            m_Log.debug("procedimiento insertado correctamente");
            //We want to be informed when this method has finalized
            m_Log.debug("eliminarExpedientesRelacionados");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

        }
        return i;
    }

    public boolean esPendienteParaUsuario(UsuarioValueObject usuario, ConsultaExpedientesValueObject consExpVO, String[] params)
            throws AnotacionRegistroException {
        boolean esPendiente = false;
        try {
            esPendiente = ConsultaExpedientesDAO.getInstance().esPendienteParaUsuario(usuario, consExpVO, params);
        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

        }
        return esPendiente;
    }

    /**
     * public boolean permiteModificarObservacionesUsuario (UsuarioValueObject
     * usuario, ConsultaExpedientesValueObject consExpVO,String[] params) throws
     * AnotacionRegistroException { boolean permiteModificarObservacionesUsiario
     * = false; try { permiteModificarObservacionesUsiario =
     * ConsultaExpedientesDAO.getInstance().permiteModificarObservacionesUsuario
     * (usuario, consExpVO, params); } catch (Exception ce) { m_Log.error("JDBC
     * Technical problem " + ce.getMessage()); throw new
     * AnotacionRegistroException("Problema técnico de JDBC " +
     * ce.getMessage());
     *
     * }
     * return permiteModificarObservacionesUsiario; }
     */
    public Integer estadoExpModifObservacionesUsuario(UsuarioValueObject usuario, ConsultaExpedientesValueObject consExpVO, String[] params)
            throws AnotacionRegistroException {
        Integer estadoExp = null;
        try {
            estadoExp = ConsultaExpedientesDAO.getInstance().estadoExpModifObservacionesUsuario(usuario, consExpVO, params);
        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }
        return estadoExp;
    }

    /**
     * Cuenta el número de expedientes que tienen la misma localización que una
     * determinada pero excluyendo un determinado expediente.
     *
     * @param localizacion: Localización
     * @param numExpediente: Número de expediente
     * @param params: Parámetros de conexión a la base de datos
     * @return int
     * @throws es.altia.util.conexion.BDException
     */
    public int getNumExpedientesConLocalizacion(String localizacion, String numExpediente, String[] params)
            throws BDException {
        m_Log.debug("================>> ConsultaExpedientesManager getNumExpedientesConLocalizacion init ");
        return ConsultaExpedientesDAO.getInstance().getNumExpedientesConLocalizacion(localizacion, numExpediente, params);
    }

    /**
     * Recupera los expedientes que cumplen un determinado criterio exceptuando
     * un determinado expediente indicado en el parámetro numExpediente
     *
     * @param uVO
     * @param consExpVO
     * @param params
     * @param desdeGestion
     * @param buscarInicioProc
     * @param columna
     * @param tipoOrden
     * @param adjuntarExpediente
     * @param numExpediente
     * @return
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
    public Vector getExpedientesConCriterioExcepto(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, boolean desdeGestion,
            boolean buscarInicioProc, String columna, String tipoOrden, boolean adjuntarExpediente, String numExpediente)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("getExpedientesConCriterioExcepto");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().getExpedientesConCriterioExcepto(uVO, consExpVO, params, desdeGestion, buscarInicioProc, columna, tipoOrden, adjuntarExpediente, numExpediente);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    /**
     * Recupera los expedientes relacionados a uno dado
     *
     * @param numExpediente: Número del expediente
     * @param codMunicipio: Código del municipio
     * @param params: Parámetros de conexión
     * @return
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     */
    public ArrayList<ConsultaExpedientesValueObject> getExpRelacionados(String numExpediente, int codMunicipio, String localizacion, String[] params)
            throws AnotacionRegistroException {

        ArrayList<ConsultaExpedientesValueObject> consulta;

        m_Log.debug("========> getExpRelacionados");
        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().getExpRelacionados(numExpediente, codMunicipio, localizacion, params);
            m_Log.debug("getExpRelacionados <================");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    /**
     * Recupera los expedientes relacionados a uno dado
     *
     * @param numExpediente: Número del expediente
     * @param codMunicipio: Código del municipio
     * @param con: Conexión a la BBDD
     * @return ArrayList<ConsultaExpedientesValueObject>
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     * @throws es.altia.common.exception.TechnicalException
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     */
    public ArrayList<ConsultaExpedientesValueObject> getExpRelacionados(String numExpediente, int codMunicipio, String localizacion, Connection con, String[] params)
            throws AnotacionRegistroException {

        ArrayList<ConsultaExpedientesValueObject> consulta;

        m_Log.debug("========> getExpRelacionados");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().getExpRelacionados(numExpediente, codMunicipio, localizacion, con, params);
            m_Log.debug("getExpRelacionados <================");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    /**
     * Cuenta el número de expedientes que verificar un determinado filtro y que
     * han sido tramitador por un determinado usuario
     *
     * @param uVO: Datos del usuario
     * @param consExpVO: Criterios del filtrado
     * @param params: Parámetros de conexión a la BBDD
     * @param desdeGestion
     * @param buscarInicioProc
     * @return Un int
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
    public int contarExpedientesUsuarioTramitador(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, boolean desdeGestion,
            boolean buscarInicioProc)
            throws AnotacionRegistroException {

        int numRelacionExpedientes = 0;

        m_Log.debug("contarExpedientesUsuarioTramitador");

        try {

            m_Log.debug("Usando persistencia manual");
            numRelacionExpedientes = ConsultaExpedientesDAO.getInstance().contarExpedientesUsuarioTramitador(uVO, consExpVO, params, desdeGestion, buscarInicioProc);
            m_Log.debug("contado");
            //We want to be informed when this method has finalized
            m_Log.debug("contarExpedientes");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return numRelacionExpedientes;
    }

    /**
     * Recupera los expedientes que cumplen un determinado criterio de filtrado
     * y que han sido tramitador por un determinado usuario
     *
     * @param uVO: Objeto con los datos del usuario
     * @param consExpVO: Objeto con los criterios de filtrado
     * @param params: Parámetros de conexión a la BD
     * @param desdeGestion
     * @param buscarInicioProc
     * @param columna
     * @param tipoOrden
     * @param adjuntarExpediente
     * @return Vector
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
    public Vector consultarUsuarioTramitador(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, boolean desdeGestion,
            boolean buscarInicioProc, String columna, String tipoOrden, boolean adjuntarExpediente)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("consultarUsuarioTramitador");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().consultarUsuarioTramitador(uVO, consExpVO, params, desdeGestion, buscarInicioProc, columna, tipoOrden, adjuntarExpediente);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    public Vector consultarUsuarioTramitadorImprimir(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, boolean desdeGestion,
            boolean buscarInicioProc, String columna, String tipoOrden, boolean paraCSV)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("consultarUsuarioTramitador");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().consultarUsuarioTramitadorImprimir(uVO, consExpVO, params, desdeGestion, buscarInicioProc, columna, tipoOrden, paraCSV);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    /**
     * Cuenta los expedientes a recuperar tras realizar un filtrado por los
     * campos suplementarios. Sólo se recuperan los expedientes que ha tramitado
     * un determinado usuario o bien, los expedientes que este usuario ha
     * iniciado
     *
     * @param uVO
     * @param consExpVO
     * @param params
     * @param columna
     * @param tipoOrden
     * @return
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
    public int contarPorCamposSuplementariosUsuarioTramitador(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, String columna, String tipoOrden, int tam)
            throws AnotacionRegistroException {

        int numRelacionExpedientes = 0;

        m_Log.debug("contarPorCamposSuplementariosUsuarioTramitador");

        try {

            m_Log.debug("Usando persistencia manual");
            numRelacionExpedientes = ConsultaExpedientesDAO.getInstance().contarPorCampoSuplementarioUsuarioTramitador(uVO, consExpVO, params, columna, tipoOrden, tam);
            m_Log.debug("contado");
            //We want to be informed when this method has finalized
            m_Log.debug("contar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return numRelacionExpedientes;
    }

    /**
     * Recupera los expedientes tramitados o iniciados por un determinado
     * usuario. También se realiza un filtrado por los campos suplementarios
     * según los criterios indicados por el usuario.
     *
     * @param uVO: Objeto con los datos del usuario
     * @param consExpVO: Objeto con los criterios de filtrado por campos
     * suplementarios
     * @param params: Parámetros de conexión a la base de datos
     * @param columna
     * @param tipoOrden
     * @return Vector
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException si
     * ocurre algún error
     */
    public Vector consultaPorCamposSuplementariosUsuarioTramitador(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, String columna, String tipoOrden, int tam)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("consultar");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().consultarPorCampoSuplementarioUsuarioTramitador(uVO, consExpVO, params, columna, tipoOrden, tam);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }

    public Vector consultaPorCamposSuplementariosUsuarioTramitadorImprimir(UsuarioValueObject uVO, ConsultaExpedientesValueObject consExpVO, String[] params, String columna, String tipoOrden, int tam, boolean paraCSV)
            throws AnotacionRegistroException {

        Vector consulta;

        m_Log.debug("consultar");

        try {

            m_Log.debug("Usando persistencia manual");
            consulta = ConsultaExpedientesDAO.getInstance().consultarPorCampoSuplementarioUsuarioTramitadorImprimir(uVO, consExpVO, params, columna, tipoOrden, tam, paraCSV);
            m_Log.debug("consultado");
            //We want to be informed when this method has finalized
            m_Log.debug("consultar");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());
        }

        return consulta;
    }
}