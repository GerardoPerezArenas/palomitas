package es.altia.flexia.registro.digitalizacion.lanbide.vo;

/**
 * Clase para almacenar los datos de MELANBIDE68_DOKUSI_METADATOS
 * @author MilaNP
 */
public class MetadatoCatalogacionVO {
    private int idTipoDoc; // TIPDOC_ID
    private String idMetadato; // TIPDOC_ID_METADATO
    private int obligatorio; // TIPDOC_OBLIGATORIO
    private String valorMetadato; // Valor indicado por el usuario. Se almacena en R_DOC_METADATOS.VALOR_METADATO

    /**
     * @return the idTipoDoc
     */
    public int getIdTipoDoc() {
        return idTipoDoc;
    }

    /**
     * @param idTipoDoc the idTipoDoc to set
     */
    public void setIdTipoDoc(int idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    /**
     * @return the obligatorio
     */
    public int getObligatorio() {
        return obligatorio;
    }

    /**
     * @param obligatorio the obligatorio to set
     */
    public void setObligatorio(int obligatorio) {
        this.obligatorio = obligatorio;
    }

    /**
     * @return the idMetadato
     */
    public String getIdMetadato() {
        return idMetadato;
    }

    /**
     * @param idMetadato the idMetadato to set
     */
    public void setIdMetadato(String idMetadato) {
        this.idMetadato = idMetadato;
    }
    
    public String toString(boolean conTit){
        StringBuilder str;
        if(conTit) str = new StringBuilder("Objeto MetadatoCatalogacionVO:\n");
        else str = new StringBuilder("\n");
        str.append("Tipo documental: " + this.idTipoDoc);
        str.append("-");
        str.append("Id de metadato: " + this.idMetadato);
        str.append("-");
        str.append("Obligatorio?: " + this.obligatorio);
        
        return str.toString();
    }

    /**
     * @return the valorMetadato
     */
    public String getValorMetadato() {
        return valorMetadato;
    }

    /**
     * @param valorMetadato the valorMetadato to set
     */
    public void setValorMetadato(String valorMetadato) {
        this.valorMetadato = valorMetadato;
    }
}
