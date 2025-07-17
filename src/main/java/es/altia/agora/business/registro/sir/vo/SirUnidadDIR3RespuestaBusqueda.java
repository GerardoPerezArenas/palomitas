/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsula la lista de resultados par la busqueda de unidades , los parametros de filtrados y configuracion de paginacion
 * @author INGDGC
 */
public class SirUnidadDIR3RespuestaBusqueda {
    
    private int numPaginaRecuperar;
    private int numPaginasTotal;
    private int numResultadosPorPagina;
    private int numResultadosTotal;
    private boolean clasificarYPaginar;
    private int totalUnidadesDIR3Sistema;
    private SirUnidadDIR3Filtros sirUnidadDIR3Filtros = new  SirUnidadDIR3Filtros();
    private List<SirUnidadDIR3Dto> listaUnidadesDIR3 =  new ArrayList<SirUnidadDIR3Dto>();

    public int getNumPaginaRecuperar() {
        return numPaginaRecuperar;
    }

    public void setNumPaginaRecuperar(int numPaginaRecuperar) {
        this.numPaginaRecuperar = numPaginaRecuperar;
    }

    public int getNumPaginasTotal() {
        return numPaginasTotal;
    }

    public void setNumPaginasTotal(int numPaginasTotal) {
        this.numPaginasTotal = numPaginasTotal;
    }

    public int getNumResultadosPorPagina() {
        return numResultadosPorPagina;
    }

    public void setNumResultadosPorPagina(int numResultadosPorPagina) {
        this.numResultadosPorPagina = numResultadosPorPagina;
    }

    public int getNumResultadosTotal() {
        return numResultadosTotal;
    }

    public void setNumResultadosTotal(int numResultadosTotal) {
        this.numResultadosTotal = numResultadosTotal;
    }

    public boolean isClasificarYPaginar() {
        return clasificarYPaginar;
    }

    public void setClasificarYPaginar(boolean clasificarYPaginar) {
        this.clasificarYPaginar = clasificarYPaginar;
    }

    public int getTotalUnidadesDIR3Sistema() {
        return totalUnidadesDIR3Sistema;
    }

    public void setTotalUnidadesDIR3Sistema(int totalUnidadesDIR3Sistema) {
        this.totalUnidadesDIR3Sistema = totalUnidadesDIR3Sistema;
    }

    public SirUnidadDIR3Filtros getSirUnidadDIR3Filtros() {
        return sirUnidadDIR3Filtros;
    }

    public void setSirUnidadDIR3Filtros(SirUnidadDIR3Filtros sirUnidadDIR3Filtros) {
        this.sirUnidadDIR3Filtros = sirUnidadDIR3Filtros;
    }

    public List<SirUnidadDIR3Dto> getListaUnidadesDIR3() {
        return listaUnidadesDIR3;
    }

    public void setListaUnidadesDIR3(List<SirUnidadDIR3Dto> listaUnidadesDIR3) {
        this.listaUnidadesDIR3 = listaUnidadesDIR3;
    }

    @Override
    public String toString() {
        return "SirUnidadDIR3RespuestaBusqueda{" + "numPaginaRecuperar=" + numPaginaRecuperar + ", numPaginasTotal=" + numPaginasTotal + ", numResultadosPorPagina=" + numResultadosPorPagina + ", numResultadosTotal=" + numResultadosTotal + ", clasificarYPaginar=" + clasificarYPaginar + ", totalUnidadesDIR3Sistema=" + totalUnidadesDIR3Sistema + ", sirUnidadDIR3Filtros=" + (sirUnidadDIR3Filtros!=null?sirUnidadDIR3Filtros.toString():"") + ", listaUnidadesDIR3(Size)=" + (listaUnidadesDIR3!=null?listaUnidadesDIR3.size():0) + '}';
    }
    
    
    
}
