package es.altia.common.service.auditoria;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuditoriaServiceFileImpl implements AuditoriaService {

    private static AuditoriaServiceFileImpl instance;
    private static final Log log = LogFactory.getLog(AuditoriaServiceFileImpl.class.getName());

    private AuditoriaServiceFileImpl() {
    }

    /**
     * Obtiene una instancia de esta clase
     * 
     * @return 
     */
    public static AuditoriaServiceFileImpl getInstance() {
        if (instance == null) {
            synchronized (AuditoriaServiceFileImpl.class) {
                if (instance == null) {
                    instance = new AuditoriaServiceFileImpl();
                }
            }
        }

        return instance;
    }
    
    /**
     * Registra un evento de auditoria
     *
     * @param tipoEvento tipo evento
     * @param idUsuario id usuario
     * @param usuario usuario
     * @param mensaje mensaje
     * @throws AuditoriaException
     */
    @Override
    public void registrarEvento(TipoEventoAuditoria tipoEvento, Integer idUsuario, String usuario, String mensaje)
            throws AuditoriaException {
        EventoAuditoria evento = new EventoAuditoria();
        evento.setTipoEvento(tipoEvento);
        evento.setMensaje(mensaje);
        evento.setIdUsuario(idUsuario);
        evento.setUsuario(usuario);

        registrarEvento(evento);
    }
    
    /**
     * Registra un evento de auditoria
     * 
     * @param evento evento de auditoria
     * @throws AuditoriaException 
     */
    @Override
    public void registrarEvento(EventoAuditoria evento) throws AuditoriaException {     
        comprobarParametros(evento);
        procesarEvento(evento);
    }

    /**
     * Comprueba que existan y sean correctos los parametros del evento
     * 
     * @param evento evento de auditoria
     * @throws AuditoriaException 
     */
    private void comprobarParametros(EventoAuditoria evento) throws AuditoriaException {        
        int codError = ConstantesAuditoria.COD_ERROR_OK;

        if (evento == null) {
            codError = ConstantesAuditoria.COD_ERROR_NO_PARAM_EVENTO;
        } else if (evento.getTipoEvento() == null) {
            codError = ConstantesAuditoria.COD_ERROR_NO_TIPO_EVENTO;
        } else if (evento.getUsuario() == null || evento.getIdUsuario() == null) {
            codError = ConstantesAuditoria.COD_ERROR_NO_USUARIO;
        }

        comprobarExcepcionAuditoria(codError);
    }

    /**
     * Procesa y vuelca el evento en el fichero de auditoria
     * 
     * @param evento evento de auditoria
     * @throws AuditoriaException 
     */
    private void procesarEvento(EventoAuditoria evento) throws AuditoriaException {
        int codError = ConstantesAuditoria.COD_ERROR_OK;
        String tipoEvento = evento.getTipoEvento().name();
        Integer idUsuario = evento.getIdUsuario();
        String nombreUsuario = evento.getUsuario();
        String mensajeEvento = evento.getMensaje();

        StringBuilder msgAuditoria = new StringBuilder();
        msgAuditoria.append(tipoEvento)
                .append(ConstantesAuditoria.SEPARADOR_COLUMNAS)
                .append(idUsuario)
                .append(ConstantesAuditoria.SEPARADOR_COLUMNAS)
                .append(nombreUsuario)
                .append(ConstantesAuditoria.SEPARADOR_COLUMNAS)
                .append(mensajeEvento);

        comprobarExcepcionAuditoria(codError);

        log.info(msgAuditoria);
    }

    /**
     * Comprueba si el codigo corresponde a un error y en caso afirmativo
     * lanza una excepcion con el texto correspondiente al error.
     * 
     * @param codError codigo de error
     * @throws AuditoriaException 
     */
    private void comprobarExcepcionAuditoria(int codError) throws AuditoriaException {
        String msjError = null;

        if (codError != ConstantesAuditoria.COD_ERROR_OK) {
            switch (codError) {
                case ConstantesAuditoria.COD_ERROR_GENERICO:
                    msjError = ConstantesAuditoria.DESC_ERROR_GENERICO;
                    break;
                case ConstantesAuditoria.COD_ERROR_NO_PARAM_EVENTO:
                    msjError = ConstantesAuditoria.DESC_ERROR_NO_PARAM_EVENTO;
                    break;
                case ConstantesAuditoria.COD_ERROR_NO_TIPO_EVENTO:
                    msjError = ConstantesAuditoria.DESC_ERROR_NO_TIPO_EVENTO;
                    break;
                default:
                    msjError = ConstantesAuditoria.DESC_ERROR_GENERICO;
            }

            throw new AuditoriaException(msjError);
        }
    }
}
