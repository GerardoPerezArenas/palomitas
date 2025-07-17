package es.altia.flexia.registro.digitalizacion.lanbide.vo;

/**
 * Clase para almacenar documentos catalogados. Tabla R_DOC_METADATOS
 * @author MilaNP
 */
public class DocumentoCatalogacionVO implements Cloneable{
    private long identificador = -1; // ID_DOC_METADATOS
    private long idDocumento; // RED_DOC_ID
    private int departamento = -1; // R_RED.RED_DEP
    private long unidadOrg = -1; // R_RED.RED_UOR
    private int ejercicio; // R_RED.RED_EJE
    private long numeroAnot = -1; // R_RED.RED_NUM
    private String tipoAnot; // R_RED.RED_TIP
    private String nomDocumento; // R_RED.RED_NOM_DOC
    private String idDocGestor; // R_RED.RED_IDDOC_GESTOR
    private TipoDocumentalCatalogacionVO tipoDocumental; // En R_RED.RED_TIPODOC_ID almacenamos tipoDocumental.getIdentificador()
    private MetadatoCatalogacionVO metadato; // DOC_ID_METADATO es metadato.getIdMetadato() ; DOC_VALOR_METADATO es metadato.getValorMetadato()
    private String observDoc;
    private int esDocMigrado; // RED_MIGRA Indica si es documento migrado desde RGI.
    private int esDocCompulsado; // RED_DOCDIGIT Indica si es documento escaneado y adjuntado desde el componente desde digitalizacion, documento compulsado
    
    /**
     * @return the identificador
     */
    public long getIdentificador() {
        return identificador;
    }

    /**
     * @param identificador the identificador to set
     */
    public void setIdentificador(long identificador) {
        this.identificador = identificador;
    }

    /**
     * @return the departamento
     */
    public int getDepartamento() {
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    /**
     * @return the unidadOrg
     */
    public long getUnidadOrg() {
        return unidadOrg;
    }

    /**
     * @param unidadOrg the unidadOrg to set
     */
    public void setUnidadOrg(long unidadOrg) {
        this.unidadOrg = unidadOrg;
    }

    /**
     * @return the ejercicio
     */
    public int getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the numeroAnot
     */
    public long getNumeroAnot() {
        return numeroAnot;
    }

    /**
     * @param numeroAnot the numeroAnot to set
     */
    public void setNumeroAnot(long numeroAnot) {
        this.numeroAnot = numeroAnot;
    }

    /**
     * @return the tipoAnot
     */
    public String getTipoAnot() {
        return tipoAnot;
    }

    /**
     * @param tipoAnot the tipoAnot to set
     */
    public void setTipoAnot(String tipoAnot) {
        this.tipoAnot = tipoAnot;
    }

    /**
     * @return the nomDocumento
     */
    public String getNomDocumento() {
        return nomDocumento;
    }

    /**
     * @param nomDocumento the nomDocumento to set
     */
    public void setNomDocumento(String nomDocumento) {
        this.nomDocumento = nomDocumento;
    }

    /**
     * @return the tipoDocumental
     */
    public TipoDocumentalCatalogacionVO getTipoDocumental() {
        return tipoDocumental;
    }

    /**
     * @param tipoDocumental the tipoDocumental to set
     */
    public void setTipoDocumental(TipoDocumentalCatalogacionVO tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }
    
    public String toString(boolean conTit){
        StringBuilder str;
        if(conTit) str = new StringBuilder("Objeto MetadatoCatalogacionVO:\n");
        else str = new StringBuilder("\n");
        if(this.identificador != -1) {
            str.append("Ident: " + this.identificador);
            str.append("-");
        }
        if(this.idDocumento!=0) {
            str.append("Id del documento: " + this.idDocumento);
            str.append("-");
        }
        if(this.idDocGestor!=null){
            str.append("Id doc gestor: " + this.idDocGestor);
            str.append("-");
        }
        str.append("Depart: " + this.departamento);
        str.append("-");
        str.append("UOR: " + this.unidadOrg);
        str.append("\n");
        str.append("Tipo: " + this.tipoAnot);
        str.append("-");
        str.append("Eje: " + this.ejercicio);
        str.append("-");
        str.append("Número: " + this.numeroAnot);
        str.append("-");
        str.append("Doc: " + this.nomDocumento);
        if(tipoDocumental != null){
            str.append(tipoDocumental.toString(false));
            if(metadato!=null)
            	str.append(getMetadato().toString(false));
        }
        
        return str.toString();
    }
    
    @Override
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch(CloneNotSupportedException e) {
            // No deberia suceder porque la clase implementa la interfaz Cloneable
        }
        return clone;
    }

    /**
     * @return the metadato
     */
    public MetadatoCatalogacionVO getMetadato() {
        return metadato;
    }

    /**
     * @param metadato the metadato to set
     */
    public void setMetadato(MetadatoCatalogacionVO metadato) {
        this.metadato = metadato;
    }
    
    // Getters de las propiedades del objeto TipoDocumentalCatalogacionVO
    public int getTipDocId() {
        return tipoDocumental.getIdentificador();
    }


    public String getTipDocDesc() {
        return tipoDocumental.getDescripcion();
    }


    public String getTipDocDesc2() {
        return tipoDocumental.getOtraDesc();
    }


    // Getters de las propiedades del objeto MetadatoCatalogacionVO
    public int getMetadatoOblig() {
        return metadato.getObligatorio();
    }


    public String getMetadatoId() {
        return metadato.getIdMetadato();
    }

    
    public String getMetadatoValor() {
        return metadato.getValorMetadato();
    }

    /**
     * @return the idDocGestor
     */
    public String getIdDocGestor() {
        return idDocGestor;
    }

    /**
     * @param idDocGestor the idDocGestor to set
     */
    public void setIdDocGestor(String idDocGestor) {
        this.idDocGestor = idDocGestor;
    }

    /**
     * @return the idDocumento
     */
    public long getIdDocumento() {
        return idDocumento;
    }

    /**
     * @param idDocumento the idDocumento to set
     */
    public void setIdDocumento(long idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getObservDoc() {
        return observDoc;
    }

    public void setObservDoc(String observDoc) {
        this.observDoc = observDoc;
    }

    public int getEsDocMigrado() {
        return esDocMigrado;
    }

    public void setEsDocMigrado(int esDocMigrado) {
        this.esDocMigrado = esDocMigrado;
    }

    public int getEsDocCompulsado() {
        return esDocCompulsado;
    }

    public void setEsDocCompulsado(int esDocCompulsado) {
        this.esDocCompulsado = esDocCompulsado;
    }
}
