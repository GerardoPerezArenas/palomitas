package es.altia.agora.business.registro.mantenimiento;

import es.altia.technical.ValueObject;
import es.altia.agora.business.util.ElementoListaValueObject;

import java.io.Serializable;
import java.util.Vector;

public class MantAsuntosValueObject implements Serializable, ValueObject {
    
    public static final long serialVersionUID=0;
    private boolean isValid;
    
    private String codigo = "";
    private String unidadRegistro = null; // -1 es el codigo para 'TODOS LOS REGISTROS'
    private String tipoRegistro = "E";
    
    private String descripcion = "";
    private String descUor = "";
    private String extracto = "";
    private String procedimiento = "";
    private String munProc = "0";
    private String unidadTram = "-1";
    private boolean enviarCorreo = false;
    private String txtListaUorsCorreo = "";
    //Añadimos para la clasificacion del asunto
    private Integer codigoClasificacion=null;
    private Vector<ElementoListaValueObject> listaDocs = new Vector<ElementoListaValueObject>();

    private String asuntoBaja = "N";
    
    private boolean tipoDocObligatorio = false;
    private boolean bloquearDestino=false;
    private boolean bloquearProcedimiento=false;
    private boolean bloqueoPAC=false;
    private String desProcedimiento ="";
    private String digitProcedimiento="";
    
    public MantAsuntosValueObject() {
        super();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
        
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadRegistro() {
        return unidadRegistro;
    }

    public void setUnidadRegistro(String unidadRegistro) {
        this.unidadRegistro = unidadRegistro;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getDescUor() {
        return descUor;
    }

    public void setDescUor(String descUor) {
        this.descUor = descUor;
    }
    
    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }
        
    public String getMunProc() {
        return munProc;
    }

    public void setMunProc(String munProc) {
        this.munProc = munProc;
    }

    public String getUnidadTram() {
        return unidadTram;
    }

    public void setUnidadTram(String unidadTram) {
        this.unidadTram = unidadTram;
    }
    
    public Vector<ElementoListaValueObject> getListaDocs() {
        return listaDocs;
    }

    public void setListaDocs(Vector<ElementoListaValueObject> listaDocs) {
        this.listaDocs = listaDocs;
    }

    public String getTxtListaUorsCorreo() {
        return txtListaUorsCorreo;
    }

    public void setTxtListaUorsCorreo(String txtListaUorsCorreo) {
        this.txtListaUorsCorreo = txtListaUorsCorreo;
    }
    
    public boolean isEnviarCorreo() {
        return enviarCorreo;
    }

    public void setEnviarCorreo(boolean enviarCorreo) {
        this.enviarCorreo = enviarCorreo;
    }
    
    public void validate(String idioma) {
        isValid = true;
    }
    
    public boolean IsValid() { 
        return isValid; 
    }

      public Integer getCodigoClasificacion() {
        return codigoClasificacion;
    }

    public void setCodigoClasificacion(Integer codigoClasificacion) {
        this.codigoClasificacion = codigoClasificacion;
    }

    public String getDesProcedimiento() {
        return desProcedimiento;
    }

    public void setDesProcedimiento(String desProcedimiento) {
        this.desProcedimiento = desProcedimiento;
    }
    
    

    public String toString() {
        String vo = "MantAsuntosValueObject :" + 
               " codigo=" + codigo + 
               " tipo=" + tipoRegistro +
               " unidadRegistro=" + unidadRegistro +
               " desc=" + descripcion +
               " extracto=" + extracto +
               " unidadTramitadora=" + unidadTram +
               " procedimiento=" + procedimiento +
               " munProc=" + munProc +
               " codigoClasificacion="+codigoClasificacion+ 
               "\n Docs: ";
        for (ElementoListaValueObject doc : listaDocs) {
            vo += doc.getDescripcion() + " ";
        }
        vo += "\n Lista de uors a notificar: " + txtListaUorsCorreo;
        if (enviarCorreo) vo+= "Se notifica a las uors.";
        else vo+= "No se notifica a las uors.";

        return vo;
    }

    /**
     * @return the asuntoBaja
     */
    public String getAsuntoBaja() {
        return asuntoBaja;
    }

    /**
     * @param asuntoBaja the asuntoBaja to set
     */
    public void setAsuntoBaja(String asuntoBaja) {
        this.asuntoBaja = asuntoBaja;
    }

    /**
     * @return the tipoDocObligatorio
     */
    public boolean isTipoDocObligatorio() {
        return tipoDocObligatorio;
    }

    /**
     * @param tipoDocObligatorio the tipoDocObligatorio to set
     */
    public void setTipoDocObligatorio(boolean tipoDocObligatorio) {
        this.tipoDocObligatorio = tipoDocObligatorio;
    }

    public boolean isBloquearDestino() {
        return bloquearDestino;
    }

    public void setBloquearDestino(boolean bloquearDestino) {
        this.bloquearDestino = bloquearDestino;
    }

    public boolean isBloquearProcedimiento() {
        return bloquearProcedimiento;
    }

    public void setBloquearProcedimiento(boolean bloquearProcedimiento) {
        this.bloquearProcedimiento = bloquearProcedimiento;
    }

    public boolean isBloqueoPAC() {
        return bloqueoPAC;
    }

    public void setBloqueoPAC(boolean bloqueoPAC) {
        this.bloqueoPAC = bloqueoPAC;
    }

    /**
     * @return the digitProcedimiento
     */
    public String getDigitProcedimiento() {
        return digitProcedimiento;
    }

    /**
     * @param digitProcedimiento the digitProcedimiento to set
     */
    public void setDigitProcedimiento(String digitProcedimiento) {
        this.digitProcedimiento = digitProcedimiento;
    }
    
    
    
    
}