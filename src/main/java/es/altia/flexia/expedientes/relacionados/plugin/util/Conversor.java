package es.altia.flexia.expedientes.relacionados.plugin.util;

import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.flexia.expedientes.relacionados.plugin.vo.ExpedienteRelacionadoVO;

/**
 *
 * @author oscar.rodriguez
 */
public class Conversor {

    /**
     * Convierte un objeto de la clase ExpedienteRelacionadoVO en un objeto TramitacionValueObject
     * @param expediente: ExpedienteRelacionadoVO
     * @return TramitacionValueObject
     */
    public static TramitacionValueObject expedienteRelacionadoVOToTramitacionValueObject(ExpedienteRelacionadoVO expediente){
        
        TramitacionValueObject tvo = new TramitacionValueObject();
        tvo.setCodDepartamento(expediente.getCodigoDepartamento());
        tvo.setCodUnidadRegistro(expediente.getCodigoUnidadRegistro());
        tvo.setTipoRegistro(expediente.getTipoRegistro());
        tvo.setEjercicioRegistro(expediente.getEjercicioRegistro());
        tvo.setNumero(expediente.getNumeroRegistro());
        tvo.setCodTercero(expediente.getCodigoTercero());
        tvo.setVersion(expediente.getVersionTercero());
        tvo.setCodDomicilio(expediente.getCodigoDomicilio());
        tvo.setCodProcedimiento(expediente.getCodProcedimiento());
        tvo.setNumeroExpediente(expediente.getNumExpedienteRelacionado());
        tvo.setNumeroExpedienteAntiguo(expediente.getNumExpedienteAntiguo());
        tvo.setDejarAnotacionBuzonEntrada(expediente.getDejarAnotacionBuzonEntrada());
        return tvo;
    }

}