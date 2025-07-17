/*     */ package es.altia.flexia.integracion.lanbide.impresion;
/*     */ 
/*     */ import es.altia.agora.business.util.GeneralValueObject;
/*     */ import es.altia.technical.ValidationException;
/*     */ import es.altia.technical.ValueObject;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class ImpresionExpedientesLanbideValueObject
/*     */   implements Serializable, ValueObject
/*     */ {
/*     */   private GeneralValueObject listaNombresFicheros;
/*  21 */   private boolean desdeFichaExpediente = true;
/*     */   private boolean isValid;
/*     */   private String claveRegistral;
/*     */   private String nombreInteresado;
/*     */   private String apellido1Interesado;
/*     */   private String apellido2Interesado;
/*     */   private String sexo;
/*     */   private String fechNacimiento;
/*     */   private String numExpediente;
/*     */   private String fechaExpedicion;
/*     */   private String realDecreto;
            private String decretoMod;
            private String fechaDecretoMod;
/*     */   private String traduccionRD;
/*     */   private String fechaRD;
/*     */   private String codigoCP;
/*     */   private String nombreCertificadoCastellano;
/*     */   private String nombreCertificadoEuskera;
/*     */   private String nivel;
/*     */   private String numRegistroInicio;
/*     */   private String fechaPresentacionRegistroInicio;
/*     */   private String interesados;
    private String codCentro;
    private String desCentro;
    private String fechaExpediente;
    private String dni;
    private String observ;
    private String provinciaInteresado;
/*     */ 
/*     */   public boolean isDesdeFichaExpediente()
/*     */   {
/*  24 */     return this.desdeFichaExpediente;
/*     */   }
/*     */ 
/*     */   public void setDesdeFichaExpediente(boolean desdeFichaExpediente) {
/*  28 */     this.desdeFichaExpediente = desdeFichaExpediente;
/*     */   }
/*     */ 
/*     */   public String getFechaExpedicion()
/*     */   {
/*  61 */     return this.fechaExpedicion;
/*     */   }
/*     */ 
/*     */   public void setFechaExpedicion(String fechaExpedicion) {
/*  65 */     this.fechaExpedicion = fechaExpedicion;
/*     */   }
/*     */ 
/*     */   public String getNumExpediente() {
/*  69 */     return this.numExpediente;
/*     */   }
/*     */ 
/*     */   public void setNumExpediente(String numExpediente) {
/*  73 */     this.numExpediente = numExpediente;
/*     */   }
/*     */ 
/*     */   public String getApellido1Interesado() {
/*  77 */     return this.apellido1Interesado;
/*     */   }
/*     */ 
/*     */   public void setApellido1Interesado(String apellido1Interesado) {
/*  81 */     this.apellido1Interesado = apellido1Interesado;
/*     */   }
/*     */ 
/*     */   public String getApellido2Interesado() {
/*  85 */     return this.apellido2Interesado;
/*     */   }
/*     */ 
/*     */   public void setApellido2Interesado(String apellido2Interesado) {
/*  89 */     this.apellido2Interesado = apellido2Interesado;
/*     */   }
/*     */ 
/*     */   public String getClaveRegistral() {
/*  93 */     return this.claveRegistral;
/*     */   }
/*     */ 
/*     */   public void setClaveRegistral(String claveRegistral) {
/*  97 */     this.claveRegistral = claveRegistral;
/*     */   }
/*     */ 
/*     */   public String getFechNacimiento() {
/* 101 */     return this.fechNacimiento;
/*     */   }
/*     */ 
/*     */   public void setFechNacimiento(String fechNacimiento) {
/* 105 */     this.fechNacimiento = fechNacimiento;
/*     */   }
/*     */ 
/*     */   public GeneralValueObject getListaNombresFicheros() {
/* 109 */     return this.listaNombresFicheros;
/*     */   }
/*     */ 
/*     */   public void setListaNombresFicheros(GeneralValueObject listaNombresFicheros) {
/* 113 */     this.listaNombresFicheros = listaNombresFicheros;
/*     */   }
/*     */ 
/*     */   public String getNombreInteresado() {
/* 117 */     return this.nombreInteresado;
/*     */   }
/*     */ 
/*     */   public void setNombreInteresado(String nombreInteresado) {
/* 121 */     this.nombreInteresado = nombreInteresado;
/*     */   }
/*     */ 
/*     */   public String getSexo() {
/* 125 */     return this.sexo;
/*     */   }
/*     */ 
/*     */   public void setSexo(String sexo) {
/* 129 */     this.sexo = sexo;
/*     */   }
/*     */ 
/*     */   public void validate(String idioma)
/*     */     throws ValidationException
/*     */   {
/* 135 */     throw new UnsupportedOperationException("Not supported yet.");
/*     */   }
/*     */ 
/*     */   public String getRealDecreto()
/*     */   {
/* 142 */     return this.realDecreto;
/*     */   }
/*     */ 
/*     */   public void setRealDecreto(String realDecreto)
/*     */   {
/* 149 */     this.realDecreto = realDecreto;
/*     */   }

/*     */   public String getDecretoMod()
/*     */   {
/* 142 */     return this.decretoMod;
/*     */   }
/*     */ 
/*     */   public void setDecretoMod(String decretoMod)
/*     */   {
/* 149 */     this.decretoMod = decretoMod;
/*     */   }

            public String getFechaDecretoMod()
/*     */   {
/* 142 */     return this.fechaDecretoMod;
/*     */   }
/*     */ 
/*     */   public void setFechaDecretoMod(String fechaDecretoMod)
/*     */   {
/* 149 */     this.fechaDecretoMod = fechaDecretoMod;
/*     */   }       

/*     */ 
/*     */   public String getTraduccionRD()
/*     */   {
/* 156 */     return this.traduccionRD;
/*     */   }
/*     */ 
/*     */   public void setTraduccionRD(String traduccionRD)
/*     */   {
/* 163 */     this.traduccionRD = traduccionRD;
/*     */   }
/*     */ 
/*     */   public String getFechaRD()
/*     */   {
/* 170 */     return this.fechaRD;
/*     */   }
/*     */ 
/*     */   public void setFechaRD(String fechaRD)
/*     */   {
/* 177 */     this.fechaRD = fechaRD;
/*     */   }
/*     */ 
/*     */   public String getCodigoCP()
/*     */   {
/* 184 */     return this.codigoCP;
/*     */   }
/*     */ 
/*     */   public void setCodigoCP(String codigoCP)
/*     */   {
/* 191 */     this.codigoCP = codigoCP;
/*     */   }
/*     */ 
/*     */   public String getNombreCertificadoCastellano()
/*     */   {
/* 198 */     return this.nombreCertificadoCastellano;
/*     */   }
/*     */ 
/*     */   public void setNombreCertificadoCastellano(String nombreCertificadoCastellano)
/*     */   {
/* 205 */     this.nombreCertificadoCastellano = nombreCertificadoCastellano;
/*     */   }
/*     */ 
/*     */   public String getNombreCertificadoEuskera()
/*     */   {
/* 212 */     return this.nombreCertificadoEuskera;
/*     */   }
/*     */ 
/*     */   public void setNombreCertificadoEuskera(String nombreCertificadoEuskera)
/*     */   {
/* 219 */     this.nombreCertificadoEuskera = nombreCertificadoEuskera;
/*     */   }
/*     */ 
/*     */   public String getNivel()
/*     */   {
/* 226 */     return this.nivel;
/*     */   }
/*     */ 
/*     */   public void setNivel(String nivel)
/*     */   {
/* 233 */     this.nivel = nivel;
/*     */   }
/*     */ 
/*     */   public String getFechaPresentacionRegistroInicio()
/*     */   {
/* 240 */     return this.fechaPresentacionRegistroInicio;
/*     */   }
/*     */ 
/*     */   public void setFechaPresentacionRegistroInicio(String fechaPresentacionRegistroInicio)
/*     */   {
/* 247 */     this.fechaPresentacionRegistroInicio = fechaPresentacionRegistroInicio;
/*     */   }
/*     */ 
/*     */   public String getInteresados()
/*     */   {
/* 254 */     return this.interesados;
/*     */   }
/*     */ 
/*     */   public void setInteresados(String interesados)
/*     */   {
/* 261 */     this.interesados = interesados;
/*     */   }
/*     */ 
/*     */   public String getNumRegistroInicio()
/*     */   {
/* 268 */     return this.numRegistroInicio;
/*     */   }
/*     */ 
/*     */   public void setNumRegistroInicio(String numRegistroInicio)
/*     */   {
/* 275 */     this.numRegistroInicio = numRegistroInicio;
/*     */   }
            public String getFechaExpediente() {
                return fechaExpediente;
            }

            public void setFechaExpediente(String fechaExpediente) {
                this.fechaExpediente = fechaExpediente;
            }
            
            public String getCodCentro() {
                return codCentro;
            }

            public void setCodCentro(String codCentro) {
                this.codCentro = codCentro;
            }

            public String getDesCentro() {
                return desCentro;
            }

            public void setDesCentro(String desCentro) {
                this.desCentro = desCentro;
            }

            public String getDni() {
                return dni;
            }

            public void setDni(String dni) {
                this.dni = dni;
            }
/*     */

    /**
     * @return the observ
     */
    public String getObserv() {
        return observ;
    }

    /**
     * @param observ the observ to set
     */
    public void setObserv(String observ) {
        this.observ = observ;
    }

    /**
     * @return the provinciaInteresado
     */
    public String getProvinciaInteresado() {
        return provinciaInteresado;
    }

    /**
     * @param provinciaInteresado the provinciaInteresado to set
     */
    public void setProvinciaInteresado(String provinciaInteresado) {
        this.provinciaInteresado = provinciaInteresado;
    }
 }

/* Location:           C:\Users\leires.VITORIA\Desktop\
 * Qualified Name:     es.altia.flexia.integracion.lanbide.impresion.ImpresionExpedientesLanbideValueObject
 * JD-Core Version:    0.6.0
 */