package es.altia.agora.business.registro.persistence;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.gestionInformes.SolicitudInformeValueObject;
import es.altia.agora.business.registro.AnotacionRegistroVO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.service.auditoria.AuditoriaException;
import es.altia.common.service.auditoria.AuditoriaServiceFileImpl;
import es.altia.common.service.auditoria.TipoEventoAuditoria;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.AnotacionOficinaRegistroVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuditoriaManager {

    private static AuditoriaManager instance = null;
    private static final Log log = LogFactory.getLog(AuditoriaManager.class.getName());

    // Servicios
    private static AuditoriaServiceFileImpl auditoriaService;

    private AuditoriaManager() {
        auditoriaService = AuditoriaServiceFileImpl.getInstance();
    }

    /**
     * Obtiene una instancia de la clase
     *
     * @return
     */
    public static AuditoriaManager getInstance() {
        //Si no hay una instancia de esta clase tenemos que crear una.
        if (instance == null) {
            synchronized (AuditoriaManager.class) {
                if (instance == null) {
                    instance = new AuditoriaManager();
                }
            }
        }
        return instance;
    }

    /************
     * REGISTRO *
     ************/
    
    /**
     * Registra un evento de acceso a una entrada o salida de registro
     *
     * @param pantalla pantalla
     * @param usuario usuario
     * @param registro registro
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
    public void auditarAccesoRegistro(String pantalla, UsuarioValueObject usuario, RegistroValueObject registro)
            throws AnotacionRegistroException {

        log.debug("auditarAccesoRegistro");

        if (registro != null) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(String.format("%s: ", pantalla));
            }

            mensaje.append(String.format("%d-%d-%s-%d-%d",
                    registro.getIdentDepart(),
                    registro.getUnidadOrgan(),
                    registro.getTipoReg(),
                    registro.getAnoReg(),
                    registro.getNumReg()));

            auditarAccesoRegistro(usuario, mensaje.toString());
        }
    }

    /**
     * Registra un evento de acceso al listado de una entrada o salida de
     * registro
     *
     * @param pantalla
     * @param usuario
     * @param anotaciones
     * @throws AnotacionRegistroException
     */
    public void auditarAccesoRegistro(String pantalla,
            UsuarioValueObject usuario, ArrayList<AnotacionOficinaRegistroVO> anotaciones)
            throws AnotacionRegistroException {

        log.debug("auditarAccesoRegistro");

        if (anotaciones != null && !anotaciones.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(String.format("%s: ", pantalla));
            }

            for (AnotacionOficinaRegistroVO anotacion : anotaciones) {
                mensaje.append(String.format("%s-%s-%s-%s-%s#",
                        anotacion.getCodDepartamento(),
                        anotacion.getUor(),
                        anotacion.getTipoEntrada(),
                        anotacion.getEjercicio(),
                        anotacion.getNumero()));
            }

            auditarAccesoRegistro(usuario, mensaje.toString());
        }
    }

    /**
     * Registra un evento de acceso a una entrada o salida de registro
     *
     * @param pantalla pantalla
     * @param usuario usuario
     * @param listaAnotaciones
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
    public void auditarAccesoRegistro(String pantalla, UsuarioValueObject usuario, List<AnotacionRegistroVO> listaAnotaciones)
            throws AnotacionRegistroException {

        log.debug("auditarAccesoRegistro");

        if (listaAnotaciones != null && !listaAnotaciones.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(pantalla).append(": ");
            }

            for (AnotacionRegistroVO anotacion : listaAnotaciones) {
                mensaje.append(String.format("%d-%d-%s-%d-%d#",
                        anotacion.getCodDepartamento(),
                        anotacion.getUor(),
                        anotacion.getTipo(),
                        anotacion.getEjercicio(),
                        anotacion.getNumero()));
            }

            auditarAccesoRegistro(usuario, mensaje.toString());
        }
    }

    /**
     * Registra un evento de acceso de una entrada o salida de registro
     *
     * @param pantalla
     * @param usuario
     * @param codDepartamento
     * @param codUor
     * @param tipoRegistro
     * @param ejercicio
     * @param listaNumAnotacion
     * @throws AnotacionRegistroException
     */
    public void auditarAccesoRegistro(
            String pantalla, UsuarioValueObject usuario,
            String codDepartamento, String codUor,
            String tipoRegistro, String ejercicio,
            List<AnotacionRegistroVO> listaNumAnotacion)
            throws AnotacionRegistroException {

        log.debug("auditarAccesoRegistro");

        if (listaNumAnotacion != null && !listaNumAnotacion.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(pantalla).append(": ");
            }

            for (AnotacionRegistroVO numAnotacion : listaNumAnotacion) {
                mensaje.append(String.format("%s-%s-%s-%s-%d#",
                        codDepartamento,
                        codUor,
                        tipoRegistro,
                        ejercicio,
                        numAnotacion.getNumero()));
            }

            auditarAccesoRegistro(usuario, mensaje.toString());
        }
    }

    /**
     * Registra un evento de acceso de una entrada o salida de registro
     *
     * @param pantalla
     * @param usuario
     * @param codDepartamento
     * @param codUor
     * @param listaNumAnotacion
     * @throws AnotacionRegistroException
     */
    public void auditarAccesoRegistro(
            String pantalla, UsuarioValueObject usuario,
            String codDepartamento, String codUor,
            List<AnotacionRegistroVO> listaNumAnotacion)
            throws AnotacionRegistroException {

        log.debug("auditarAccesoRegistro");

        if (listaNumAnotacion != null && !listaNumAnotacion.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(pantalla).append(": ");
            }

            for (AnotacionRegistroVO numAnotacion : listaNumAnotacion) {
                mensaje.append(String.format("%s-%s-%s-%d-%d#",
                        codDepartamento,
                        codUor,
                        numAnotacion.getTipo(),
                        numAnotacion.getEjercicio(),
                        numAnotacion.getNumero()));
            }

            auditarAccesoRegistro(usuario, mensaje.toString());
        }
    }

    /**
     * Registra un evento de acceso al listado de una entrada o salida de
     * registro
     *
     * @param pantalla pantalla en la que se ejecuta el evento
     * @param usuario usuario
     * @param listaAnotaciones listado de anotaciones
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
    public void auditarAccesoRegistroListado(String pantalla, UsuarioValueObject usuario, List<Vector> listaAnotaciones)
            throws AnotacionRegistroException {

        log.debug("auditarAccesoRegistroListado");

        StringBuilder mensaje = new StringBuilder();
        String departamento = null;
        String uor = null;
        String tipoRegistro = null;
        String ejercicio = null;
        String numeroRegistro = null;

        if (listaAnotaciones != null && !listaAnotaciones.isEmpty()) {
            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(String.format("%s: ", pantalla));
            }

            for (Vector<String> anotacion : listaAnotaciones) {
                departamento = anotacion.get(0);
                uor = anotacion.get(1);
                tipoRegistro = anotacion.get(2);
                ejercicio = anotacion.get(3);
                numeroRegistro = anotacion.get(4);

                mensaje.append(String.format("%s-%s-%s-%s-%s#",
                        departamento, uor, tipoRegistro, ejercicio, numeroRegistro));
            }

            auditarAccesoRegistro(usuario, mensaje.toString());
        }
    }

    /**
     * Registra un evento de acceso a una entrada o salida de registro
     *
     * @param usuario usuario
     * @param mensaje mensaje
     * @throws
     * es.altia.agora.business.registro.exception.AnotacionRegistroException
     */
    private void auditarAccesoRegistro(UsuarioValueObject usuario, String mensaje)
            throws AnotacionRegistroException {

        try {
            Integer idUsuario = null;
            String nombreUsuario = null;
            if (usuario != null) {
                idUsuario = usuario.getIdUsuario();
                nombreUsuario = usuario.getNombreUsu();
            }
            
            auditoriaService.registrarEvento(TipoEventoAuditoria.ACCESO_REGISTRO, idUsuario, nombreUsuario, mensaje);
        } catch (AuditoriaException ae) {
            log.error("Error al intentar registrar el acceso", ae);
            throw new AnotacionRegistroException("Error al intentar registrar el acceso");
        }
    }
    
    /***************
     * EXPEDIENTES *
     ***************/
    
    /**
     * Registra un evento de acceso a un expediente
     *
     * @param pantalla
     * @param usuario
     * @param expediente
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     */
    public void auditarAccesoExpediente(
            String pantalla, UsuarioValueObject usuario,
            GeneralValueObject expediente)
            throws TramitacionException {

        log.debug("auditarAccesoExpediente");

        if (expediente != null) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(pantalla).append(": ");
            }

            mensaje.append(String.format("%s-%s-%s",
                    expediente.getAtributoONulo("codMunicipio"),
                    expediente.getAtributoONulo("ejercicio"),
                    expediente.getAtributoONulo("numero")));

            auditarAccesoExpediente(usuario, mensaje.toString());
        }
    }

    /**
     * Registra un evento de acceso a varios expedientes
     *
     * @param pantalla
     * @param usuario
     * @param listaExpedientes
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     */
    public void auditarAccesoExpediente(
            String pantalla, UsuarioValueObject usuario,
            List<TramitacionValueObject> listaExpedientes)
            throws TramitacionException {

        log.debug("auditarAccesoExpediente");

        if (listaExpedientes != null && !listaExpedientes.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(pantalla).append(": ");
            }

            for (TramitacionValueObject expediente : listaExpedientes) {
                mensaje.append(String.format("%s-%s-%s#",
                        expediente.getCodMunicipio(),
                        expediente.getEjercicio(),
                        expediente.getNumero()));
            }

            auditarAccesoExpediente(usuario, mensaje.toString());
        }
    }

    /**
     * Registra un evento de acceso a varios expedientes relacionados
     *
     * @param pantalla
     * @param usuario
     * @param listaExpedientes
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     */
    public void auditarAccesoExpedienteRelacionado(
            String pantalla, UsuarioValueObject usuario,
            List<ConsultaExpedientesValueObject> listaExpedientes)
            throws TramitacionException {

        log.debug("auditarAccesoExpedienteRelacionado");

        if (listaExpedientes != null && !listaExpedientes.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(pantalla).append(": ");
            }

            for (ConsultaExpedientesValueObject expediente : listaExpedientes) {
                mensaje.append(String.format("%s-%s-%s#",
                        expediente.getCodMunicipioRel(),
                        expediente.getEjercicioRel(),
                        expediente.getNumeroExpedienteRel()));
            }

            auditarAccesoExpediente(usuario, mensaje.toString());
        }
    }

    /**
     * Auditoria de acceso al listado de anotaciones
     *
     * @param pantalla
     * @param usuario
     * @param listaAnotaciones
     * @throws TramitacionException
     */
    public void auditarAccesoAnotacion(String pantalla, UsuarioValueObject usuario, List<TramitacionValueObject> listaAnotaciones) throws TramitacionException {

        log.debug("auditarAccesoAnotacion");

        if (listaAnotaciones != null && !listaAnotaciones.isEmpty()) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(pantalla).append(": ");
            }

            for (TramitacionValueObject fila : (List<TramitacionValueObject>) listaAnotaciones) {
                String[] numeroAnotacion = StringUtils.split(fila.getEjerNum(), "/");

                mensaje.append(String.format("%s-%s-%s-%s-%s#",
                        fila.getCodDepartamento(),
                        fila.getCodUnidadRegistro(),
                        fila.getTipoRegistro(),
                        numeroAnotacion[0],
                        numeroAnotacion[1]));
            }

            auditarAccesoExpediente(usuario, mensaje.toString());
        }
    }

    /**
     * Auditoria de acceso a una anotacion
     *
     * @param pantalla
     * @param usuario
     * @param registro
     * @throws TramitacionException
     */
    public void auditarAccesoAnotacion(String pantalla, UsuarioValueObject usuario, RegistroValueObject registro) throws TramitacionException {

        log.debug("auditarAccesoAnotacion");

        if (registro != null) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(pantalla).append(": ");
            }

            mensaje.append(String.format("%d-%d-%s-%d-%d",
                    registro.getIdentDepart(),
                    registro.getUnidadOrgan(),
                    registro.getTipoReg(),
                    registro.getAnoReg(),
                    registro.getNumReg()));

            auditarAccesoExpediente(usuario, mensaje.toString());
        }
    }

    /**
     * Registra un evento de acceso a un expediente
     *
     * @param usuario usuario
     * @param mensaje mensaje
     * @throws es.altia.agora.business.sge.exception.TramitacionException
     */
    private void auditarAccesoExpediente(UsuarioValueObject usuario, String mensaje)
            throws TramitacionException {

        try {
            Integer idUsuario = null;
            String nombreUsuario = null;
            if (usuario != null) {
                idUsuario = usuario.getIdUsuario();
                nombreUsuario = usuario.getNombreUsu();
            }
            
            auditoriaService.registrarEvento(TipoEventoAuditoria.ACCESO_EXPEDIENTE, idUsuario, nombreUsuario, mensaje);
        } catch (AuditoriaException ae) {
            log.error("Error al intentar registrar el acceso", ae);
            throw new TramitacionException("Error al intentar registrar el acceso");
        }
    }
    
    /****************************************
     * Generacion de informes de expediente *
     ****************************************/

    /**
     * Auditoria de acceso a una solicitud de informe de expediente
     *
     * @param pantalla
     * @param usuario
     * @param solicitudInforme
     * @throws TramitacionException
     */
    public void auditarAccesoSolicitudInforme(String pantalla, UsuarioValueObject usuario, SolicitudInformeValueObject solicitudInforme) throws TramitacionException {

        log.debug("auditarAccesoSolicitudInforme");

        if (solicitudInforme != null) {
            StringBuilder mensaje = new StringBuilder();

            if (StringUtils.isNotEmpty(pantalla)) {
                mensaje.append(pantalla).append(": ");
            }

            mensaje.append("codPlantilla=[").append(solicitudInforme.getCodPlantilla()).append("],")
                   .append("codSolicitud=[").append(solicitudInforme.getCodSolicitud()).append("],")
                   .append("descripcion=[").append(solicitudInforme.getDescripcion()).append("],")
                   .append("procedimiento=[").append(solicitudInforme.getProcedimiento()).append("],")
                   .append("origen=[").append(solicitudInforme.getOrigen()).append("],")
                   .append("fechaSolicitud=[").append(solicitudInforme.getFechaSolicitud()).append("],")
                   .append("criterios=[").append(solicitudInforme.getCadenaCriterios()).append("]");

            auditarAccesoExpediente(usuario, mensaje.toString());
        }
    }
    
    /**
     * Auditoria de acceso a una solicitud de informe de expediente
     *
     * @param pantalla
     * @param usuario
     * @param mensajeEvento
     * @throws TramitacionException
     */
    public void auditarAccesoSolicitudInforme(String pantalla, UsuarioValueObject usuario, String mensajeEvento) throws TramitacionException {

        log.debug("auditarAccesoSolicitudInforme");

        StringBuilder mensaje = new StringBuilder();
        
        if (StringUtils.isNotEmpty(pantalla)) {
            mensaje.append(pantalla).append(": ");
        }

        mensaje.append(mensajeEvento);
        auditarAccesoExpediente(usuario, mensaje.toString());

    }
}
