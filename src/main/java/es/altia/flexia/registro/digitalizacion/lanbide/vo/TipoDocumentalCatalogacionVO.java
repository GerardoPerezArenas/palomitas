package es.altia.flexia.registro.digitalizacion.lanbide.vo;

/**
 * Objetos para almacenar los datos de la tabla MELANBIDE68_TIPDOC_LANBIDE
 * @author MilaNP
 */
public class TipoDocumentalCatalogacionVO {
    private int identificador; // TIPDOC_ID
    private String descripcion; // TIPDOC_LANBIDE_ES
    private String otraDesc; // TIPDOC_LANBIDE_EU
    //se añade el código a mostrar CODTIPDOC
    private int codigoNuevo;
    //se añaden descripciones largas para el buscador de tipos documentales
    private String descripcionLargaCAS; // DESCTIPDOC_LANBIDE_ES
    private String descripcionLargaEUS; // DESCTIPDOC_LANBIDE_EU
    private String codGrupo; // COD_GRUPO_TIPDOC

    /**
     * @return the identificador
     */
    public int getIdentificador() {
        return identificador;
    }

    /**
     * @param identificador the identificador to set
     */
    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    /**
     * @return the otraDesc
     */
    public String getOtraDesc() {
        return otraDesc;
    }

    /**
     * @param otraDesc the otraDesc to set
     */
    public void setOtraDesc(String otraDesc) {
        this.otraDesc = otraDesc;
    }
    
    /**
     * 
     * @return the codigoNuevo
     */
    public int getCodigoNuevo() {
        return codigoNuevo;
    }
   
    /**
     * 
     * @param codigoNuevo the codigoNuevo to set
     */
    public void setCodigoNuevo(int codigoNuevo) {
        this.codigoNuevo = codigoNuevo;
    }

    /**
     * 
     * @return the descripcionLargaCAS
     */
    public String getDescripcionLargaCAS() {
        return descripcionLargaCAS;
    }

    /**
     * 
     * @param descripcionLargaCAS the descripcionLargaCAS to set
     */
    public void setDescripcionLargaCAS(String descripcionLargaCAS) {
        this.descripcionLargaCAS = descripcionLargaCAS;
    }

    /**
     * 
     * @return the descripcionLargaEUS
     */
    public String getDescripcionLargaEUS() {
        return descripcionLargaEUS;
    }

    /**
     * 
     * @param descripcionLargaEUS the descripcionLargaEUS to set
     */
    public void setDescripcionLargaEUS(String descripcionLargaEUS) {
        this.descripcionLargaEUS = descripcionLargaEUS;
    }
    
    /**
     * 
     * @return the codGrupo
     */
    public String getCodGrupo() {
        return codGrupo;
    }
    
    /**
     * 
     * @param codGrupo the codGrupo to set
     */
    public void setCodGrupo(String codGrupo) {
        this.codGrupo = codGrupo;
    }
    
    
    public String toString(boolean conTit){
        StringBuilder str;
        if(conTit) str = new StringBuilder("Objeto TipoDocumentalCatalogacionVO:\n");
        else str = new StringBuilder("\n");
        str.append("ID Tipo documental: " + this.identificador);
        str.append("-");
        str.append("Descripción principal: " + this.descripcion);
        str.append("-");
        str.append("Descripción secundaria: " + this.otraDesc);
        str.append("-");
        str.append("Código Tipo documental: " + this.codigoNuevo);
        str.append("-");
        str.append("Descripción Larga CAS: " + this.descripcionLargaCAS);
        str.append("-");
        str.append("Descripción Larga EUS: " + this.descripcionLargaEUS);
        str.append("-");
        str.append("Grupo: " + this.codGrupo);
        
        return str.toString();
    }
}
