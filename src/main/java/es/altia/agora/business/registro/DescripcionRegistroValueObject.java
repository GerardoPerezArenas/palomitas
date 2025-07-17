package es.altia.agora.business.registro;

import java.util.Vector;

/**
 * RegistroValueObject con descripciones de todos los atributos, incluidos
 * interesados y temas.
 * @author juan.jato
 */
public class DescripcionRegistroValueObject {

    // Los campos asunto, unidad, tipoEntrada, tipoDoc, tipoTrans, tipoRem,
    // actuacion, orgDestino, uniDestino, orgProc y uniProc contienen código 
    // visual y descripción, en formato 'cod - desc'.
    private String tipoAsiento = "";
    private String fechaEntrada = "";
    private String horaEntrada = "";
    private String fechaPres = "";
    private String horaPres = "";
    private String asunto = "";
    private String extracto = "";
    private String observaciones = "";
    private String unidad = "";
    private String tipoEntrada = "";
    private String tipoDoc = "";
    private String tipoTrans = "";
    private String numTrans = "";
    private String tipoRem = "";
    private String procedimiento = "";
    private String expediente = "";
    private String actuacion = "";
    private String autoridad = "";
    private String orgDestino = "";
    private String uniDestino = "";
    private String orgProc = "";
    private String uniProc = "";
    private String entradaRel = "";
    private String fechaDocu ="";

    // TERCEROS
    private Vector<String> codTerceros = null;
    private Vector<String> versionTerceros = null;
    private Vector<String> codDomicilios = null;
    // Descripcion del rol, usamos la descripción para comparar los terceros
    // porque al cambiar de procedimiento pueden cambiar los códigos de los
    // roles pero seguir teniendo la misma descripción.
    private Vector<String> rolesTerceros = null; 
     // Descripcion completa del tercero
    private Vector<String> descTerceros = null;

    // RELACIONES
    private Vector<SimpleRegistroValueObject> relaciones;

    // TEMAS
    // Contiene la lista de codigos y descripciones de temas en formato 'cod - desc'
    private Vector<String> temas = null;

    // DOCUMENTOS
    private Vector<String> nombreDocs = null;
    private Vector<String> tipoDocs = null;
    private Vector<String> fechaDocs = null;
    private Vector<String> cotejoDocs = null;
    
    private Integer codOficinaRegistro;
    private String descOficinaRegistro;
    
    //REGISTRO TELEMATICO
    private String registroTelematico;

    public String getActuacion() {
        return actuacion;
    }

    public void setActuacion(String actuacion) {
        this.actuacion = actuacion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getAutoridad() {
        return autoridad;
    }

    public void setAutoridad(String autoridad) {
        this.autoridad = autoridad;
    }

    public Vector<String> getCodDomicilios() {
        return codDomicilios;
    }

    public void setCodDomicilios(Vector<String> codDomicilios) {
        this.codDomicilios = codDomicilios;
    }

    public Vector<String> getRolesTerceros() {
        return rolesTerceros;
    }

    public void setRolesTerceros(Vector<String> rolesTerceros) {
        this.rolesTerceros = rolesTerceros;
    }

    public Vector<String> getCodTerceros() {
        return codTerceros;
    }

    public void setCodTerceros(Vector<String> codTerceros) {
        this.codTerceros = codTerceros;
    }

    public Vector<String> getDescTerceros() {
        return descTerceros;
    }

    public void setDescTerceros(Vector<String> descTerceros) {
        this.descTerceros = descTerceros;
    }

    public String getEntradaRel() {
        return entradaRel;
    }

    public void setEntradaRel(String entradaRel) {
        this.entradaRel = entradaRel;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }

    public Vector<String> getFechaDocs() {
        return fechaDocs;
    }

    public void setFechaDocs(Vector<String> fechaDocs) {
        this.fechaDocs = fechaDocs;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getFechaPres() {
        return fechaPres;
    }

    public void setFechaPres(String fechaPres) {
        this.fechaPres = fechaPres;
    }

    public String getFechaDocu() {
        return fechaDocu;
    }

    public void setFechaDocu(String fechaDocu) {
        this.fechaDocu = fechaDocu;
    }
    
    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraPres() {
        return horaPres;
    }

    public void setHoraPres(String horaPres) {
        this.horaPres = horaPres;
    }

    public Vector<String> getNombreDocs() {
        return nombreDocs;
    }

    public void setNombreDocs(Vector<String> nombreDocs) {
        this.nombreDocs = nombreDocs;
    }

    public String getNumTrans() {
        return numTrans;
    }

    public void setNumTrans(String numTrans) {
        this.numTrans = numTrans;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getOrgDestino() {
        return orgDestino;
    }

    public void setOrgDestino(String orgDestino) {
        this.orgDestino = orgDestino;
    }

    public String getOrgProc() {
        return orgProc;
    }

    public void setOrgProc(String orgProc) {
        this.orgProc = orgProc;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public Vector<SimpleRegistroValueObject> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(Vector<SimpleRegistroValueObject> relaciones) {
        this.relaciones = relaciones;
    }

    public Vector<String> getTemas() {
        return temas;
    }

    public void setTemas(Vector<String> temas) {
        this.temas = temas;
    }

    public String getTipoAsiento() {
        return tipoAsiento;
    }

    public void setTipoAsiento(String tipoAsiento) {
        this.tipoAsiento = tipoAsiento;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public Vector<String> getTipoDocs() {
        return tipoDocs;
    }

    public void setTipoDocs(Vector<String> tipoDocs) {
        this.tipoDocs = tipoDocs;
    }

    public String getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(String tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public String getTipoRem() {
        return tipoRem;
    }

    public void setTipoRem(String tipoRem) {
        this.tipoRem = tipoRem;
    }

    public String getTipoTrans() {
        return tipoTrans;
    }

    public void setTipoTrans(String tipoTrans) {
        this.tipoTrans = tipoTrans;
    }

    public String getUniDestino() {
        return uniDestino;
    }

    public void setUniDestino(String uniDestino) {
        this.uniDestino = uniDestino;
    }

    public String getUniProc() {
        return uniProc;
    }

    public void setUniProc(String uniProc) {
        this.uniProc = uniProc;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Vector<String> getVersionTerceros() {
        return versionTerceros;
    }

    public void setVersionTerceros(Vector<String> versionTerceros) {
        this.versionTerceros = versionTerceros;
    }
    
    public int getCodOficinaRegistro() {
        return codOficinaRegistro;
    }//getCodOficinaRegistro
    public void setCodOficinaRegistro(int codOficinaRegistro) {
        this.codOficinaRegistro = codOficinaRegistro;
    }//setCodOficinaRegistro
    
    public String getDescOficinaRegistro() {
        return descOficinaRegistro;
    }//getDescOficinaRegistro
    public void setDescOficinaRegistro(String descOficinaRegistro) {
        this.descOficinaRegistro = descOficinaRegistro;
    }//setDescOficinaRegistro
    
     public Vector<String> getCotejoDocs() {
        return cotejoDocs;
    }

    public void setCotejoDocs(Vector<String> cotejoDocs) {
        this.cotejoDocs = cotejoDocs;
    }
    
    public String toString() {
       String descripcion = 
               "\ntipoAsiento: '" + tipoAsiento + "'\n" +
               "fechaEntrada: '" + fechaEntrada + "'\n" +
               "horaEntrada: '" + horaEntrada + "'\n" +
               "fechaPres: '" + fechaPres + "'\n" +
               "horaPres: '" + horaPres + "'\n" +
               "asunto: '" + asunto + "'\n" +
               "extracto: '" + extracto + "'\n" +
               "observaciones: '" + observaciones + "'\n" +
               "unidad: '" + unidad + "'\n" +
               "tipoEntrada: '" + tipoEntrada + "'\n" +
               "tipoDoc: '" + tipoDoc + "'\n" +
               "tipoTrans: '" + tipoTrans + "'\n" +
               "numTrans: '" + numTrans + "'\n" +
               "tipoRem: '" + tipoRem + "'\n" +
               "procedimiento: '" + procedimiento + "'\n" +
               "expediente: '" + expediente + "'\n" +
               "actuacion: '" + actuacion + "'\n" +
               "autoridad: '" + autoridad + "'\n" +
               "orgDestino: '" + orgDestino + "'\n" +
               "uniDestino: '" + uniDestino + "'\n" +
               "orgProc: '" + orgProc + "'\n" +
               "uniProc: '" + uniProc + "'\n" +
               "entradaRel: '" + entradaRel + "'\n" +
               "codOficinaRegistro : '" + codOficinaRegistro + "'\n" +
               "descOficinaRegistro : '" + descOficinaRegistro + "'\n";

       if (temas != null && temas.size() > 0) {
           descripcion += "temas:\n";
           for (String tema : temas) {
               descripcion += "  " + tema + "\n";
           }
       }
        
       if (relaciones != null && relaciones.size() > 0) {
           descripcion += "relaciones:\n";
           for (SimpleRegistroValueObject vo : relaciones) {
               descripcion += "  " + vo.getEjercicio() + "/" + vo.getNumero() + "-" +vo.getTipo() + "\n";
           }
       }
       
       if (nombreDocs != null && nombreDocs.size() > 0) {
           descripcion += "documentos:\n";
           for (int i = 0; i<nombreDocs.size(); i++) {
               descripcion += "  " + nombreDocs.elementAt(i) + "/" + tipoDocs.elementAt(i) + "/" + fechaDocs.elementAt(i) + "/" + cotejoDocs.elementAt(i) + "\n";
           }
       }
       
       if (codTerceros != null && codTerceros.size() > 0) {
          descripcion += "terceros:\n";
          for (int i = 0; i< codTerceros.size(); i++) {
              descripcion += "  " + codTerceros.elementAt(i) + ", " + 
                 versionTerceros.elementAt(i) + ", " +
                 rolesTerceros.elementAt(i) + ", " +
                 codDomicilios.elementAt(i) + ", " +
                 descTerceros.elementAt(i) + "\n";
          }
       }
       
       return descripcion;   
    }

    /**
     * @return the registroTelematico
     */
    public String getRegistroTelematico() {
        return registroTelematico;
    }

    /**
     * @param registroTelematico the registroTelematico to set
     */
    public void setRegistroTelematico(String registroTelematico) {
        this.registroTelematico = registroTelematico;
    }
}