/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.registro.sir.vo;

/**
 *
 * @author INGDGC
 */
public class SirUnidadDIR3Filtros {
    
    private String codigoUnidad;    // 	VARCHAR2 ( 100 BYTE) PRIMARY KEY
    private String nombreUnidad_ES;    // 	 VARCHAR2(1500 BYTE)
    private String nombreUnidad_EU;    // 	 VARCHAR2(1500 BYTE)
    private String codigoOficina;    // 		VARCHAR2 ( 100 BYTE)
    private String nombreOficina;    // 		
    private String codigoOrganismo;    // 	 VARCHAR2 ( 100 BYTE)
    private String codigoRaiz;    // 	 VARCHAR2( 100 BYTE)
    private String nombreRaiz;    // 	 
    private String codigoNivelAdministrativo;    // 	 VARCHAR2 ( 100 BYTE)
    private String codigoComunidadAutonoma;    // 	 VARCHAR2 ( 100 BYTE)
    private String codigoProvincia;    // 	 VARCHAR2 ( 100 BYTE)
    private String fechaActivacionDesde;    // 	Formato dd/MM/YYYY para pasa a ==> date
    private String fechaActivacionHasta;    // 	Formato dd/MM/YYYY para pasa a ==> date
    private int idioma = 1;
    private int numPaginaRecuperar=1;
    private int numPaginasTotal;
    private int numResultadosPorPagina;
    private int numResultadosTotal;
    private boolean clasificarYPaginar=true;
    private int totalUnidadesDIR3Sistema;
    private int offsetQuery;    // Numero linea para hacer el corte en la consulta: 0 Pagina 1, 10 Pagina 2, ... etc 
    

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public void setCodigoUnidad(String codigoUnidad) {
        this.codigoUnidad = codigoUnidad;
    }

    public String getNombreUnidad_ES() {
        return nombreUnidad_ES;
    }

    public void setNombreUnidad_ES(String nombreUnidad_ES) {
        this.nombreUnidad_ES = nombreUnidad_ES;
    }

    public String getNombreUnidad_EU() {
        return nombreUnidad_EU;
    }

    public void setNombreUnidad_EU(String nombreUnidad_EU) {
        this.nombreUnidad_EU = nombreUnidad_EU;
    }

    public String getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(String codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public String getNombreOficina() {
        return nombreOficina;
    }

    public void setNombreOficina(String nombreOficina) {
        this.nombreOficina = nombreOficina;
    }

    public String getCodigoOrganismo() {
        return codigoOrganismo;
    }

    public void setCodigoOrganismo(String codigoOrganismo) {
        this.codigoOrganismo = codigoOrganismo;
    }

    public String getCodigoRaiz() {
        return codigoRaiz;
    }

    public void setCodigoRaiz(String codigoRaiz) {
        this.codigoRaiz = codigoRaiz;
    }

    public String getNombreRaiz() {
        return nombreRaiz;
    }

    public void setNombreRaiz(String nombreRaiz) {
        this.nombreRaiz = nombreRaiz;
    }

    public String getCodigoNivelAdministrativo() {
        return codigoNivelAdministrativo;
    }

    public void setCodigoNivelAdministrativo(String codigoNivelAdministrativo) {
        this.codigoNivelAdministrativo = codigoNivelAdministrativo;
    }

    public String getCodigoComunidadAutonoma() {
        return codigoComunidadAutonoma;
    }

    public void setCodigoComunidadAutonoma(String codigoComunidadAutonoma) {
        this.codigoComunidadAutonoma = codigoComunidadAutonoma;
    }

    public String getCodigoProvincia() {
        return codigoProvincia;
    }

    public void setCodigoProvincia(String codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    public String getFechaActivacionDesde() {
        return fechaActivacionDesde;
    }

    public void setFechaActivacionDesde(String fechaActivacionDesde) {
        this.fechaActivacionDesde = fechaActivacionDesde;
    }

    public String getFechaActivacionHasta() {
        return fechaActivacionHasta;
    }

    public void setFechaActivacionHasta(String fechaActivacionHasta) {
        this.fechaActivacionHasta = fechaActivacionHasta;
    }

    
    public boolean isStringNullOrEmpty(String string) {
        return string==null || string.isEmpty() || string.equalsIgnoreCase("null") ;
    }

    public String getClausulaPrevia(String clausulaPrevia) {
        return (clausulaPrevia==null || clausulaPrevia.isEmpty() ? " WHERE " : " AND ");
    }

    public int getIdioma() {
        return idioma;
    }

    public void setIdioma(int idioma) {
        this.idioma = idioma;
    }

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

    public int getOffsetQuery() {
        offsetQuery=0;
        if(numPaginaRecuperar>1){
           offsetQuery=((numPaginaRecuperar-1)*numResultadosPorPagina);
        }
        return offsetQuery;
    }

    //public void setOffsetQuery(int offsetQuery) {
    //    this.offsetQuery = offsetQuery;
    //}

    @Override
    public String toString() {
        return "SirUnidadDIR3Filtros{" + "codigoUnidad=" + codigoUnidad + ", nombreUnidad_ES=" + nombreUnidad_ES + ", nombreUnidad_EU=" + nombreUnidad_EU + ", codigoOficina=" + codigoOficina + ", nombreOficina=" + nombreOficina + ", codigoOrganismo=" + codigoOrganismo + ", codigoRaiz=" + codigoRaiz + ", nombreRaiz=" + nombreRaiz + ", codigoNivelAdministrativo=" + codigoNivelAdministrativo + ", codigoComunidadAutonoma=" + codigoComunidadAutonoma + ", codigoProvincia=" + codigoProvincia + ", fechaActivacionDesde=" + fechaActivacionDesde + ", fechaActivacionHasta=" + fechaActivacionHasta + ", idioma=" + idioma + ", numPaginaRecuperar=" + numPaginaRecuperar + ", numPaginasTotal=" + numPaginasTotal + ", numResultadosPorPagina=" + numResultadosPorPagina + ", numResultadosTotal=" + numResultadosTotal + ", clasificarYPaginar=" + clasificarYPaginar + ", totalUnidadesDIR3Sistema=" + totalUnidadesDIR3Sistema + ", offsetQuery=" + getOffsetQuery() + '}';
    }
    
}
