/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.portafirmasexternocliente.vo;

/**
 *
 * @author jesus.cordoba-perez
 */
public class EstadoPortafirmasHistVO {
    
    private Long idEstadoPortafirmasHist;
    
    private EstadoPortafirmasVO estadoPortafirmas;

    public Long getIdEstadoPortafirmasHist() {
        return idEstadoPortafirmasHist;
    }

    public void setIdEstadoPortafirmasHist(Long idEstadoPortafirmasHist) {
        this.idEstadoPortafirmasHist = idEstadoPortafirmasHist;
    }

    public EstadoPortafirmasVO getEstadoPortafirmas() {
        return estadoPortafirmas;
    }

    public void setEstadoPortafirmas(EstadoPortafirmasVO estadoPortafirmas) {
        this.estadoPortafirmas = estadoPortafirmas;
    }
            
}
