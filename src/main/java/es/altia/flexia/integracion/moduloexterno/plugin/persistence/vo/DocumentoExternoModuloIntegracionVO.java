package es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DocumentoExternoModuloIntegracionVO {
    
    private String numExp; //Número del expediente
    private int  ejercicio; //ejercicio del expediente
    private Long codDocumento; //código do documento
    private String nombreDocumento; // nombre do documento
    private Calendar fechaAlta; // fechaAlta
    private byte[] contenido; //contenido do documento externo
    private String mimetipe; //mimetipe do documento
    private String extensionDocumento; //extensión del documento

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

  

    public int getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(int ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Long getCodDocumento() {
        return codDocumento;
    }

    public void setCodDocumento(Long codDocumento) {
        this.codDocumento = codDocumento;
    }

    public String getMimetipe() {
        return mimetipe;
    }

    public void setMimetipe(String mimetipe) {
        this.mimetipe = mimetipe;
    }

  
    

    /**
     * @return the fechaAlta
     */
    public Calendar getFechaAlta() {
        return fechaAlta;
    }

    /**
     * @param fechaAlta the fechaAlta to set
     */
    public void setFechaAlta(Calendar fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    
    public String getFechaAltaAsString(){
        String salida = "";
        if(fechaAlta!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            salida = sf.format(fechaAlta.getTime());
        }
        return salida;
    }

  
   

    /**
     * @return the contenido
     */
    public byte[] getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    /**
     * @return the nombreDocumento
     */
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    /**
     * @param nombreDocumento the nombreDocumento to set
     */
    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    /**
     * @return the extensionDocumento
     */
    public String getExtensionDocumento() {
        return extensionDocumento;
    }

    /**
     * @param extensionDocumento the extensionDocumento to set
     */
    public void setExtensionDocumento(String extensionDocumento) {
        this.extensionDocumento = extensionDocumento;
    }

}