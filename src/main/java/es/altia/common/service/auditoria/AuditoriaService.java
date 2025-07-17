package es.altia.common.service.auditoria;

public interface AuditoriaService {

    /**
     * Registra un evento de auditoria
     *
     * @param tipoEvento tipo de evento de auditoria
     * @param idUsuario id del usuario
     * @param usuario nombre del usuario
     * @param mensaje mensaje del evento
     * @throws AuditoriaException
     */
    void registrarEvento(TipoEventoAuditoria tipoEvento, Integer idUsuario, String usuario, String mensaje)
            throws AuditoriaException;
    
    /**
     * Registra un evento de auditoria
     *
     * @param evento evento de auditoria
     * @throws AuditoriaException
     */
    void registrarEvento(EventoAuditoria evento)
            throws AuditoriaException;
}
