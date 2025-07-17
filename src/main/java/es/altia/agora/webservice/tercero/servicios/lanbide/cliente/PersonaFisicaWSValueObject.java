/**
 * PersonaFisicaWSValueObject.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.lanbide.cliente;

public class PersonaFisicaWSValueObject  extends es.altia.agora.webservice.tercero.servicios.lanbide.cliente.ValueObject  implements java.io.Serializable {
    private java.lang.String ape1Reconocido;

    private java.lang.String ape2Reconocido;

    private java.lang.String apellido1;

    private java.lang.String apellido2;

    private java.lang.String bloqAgr;

    private java.lang.String cert_poli;

    private java.lang.String cert_ssss;

    private java.lang.String cod_ambitoComarca;

    private java.lang.String cod_ambitoMunicipio;

    private java.lang.String cod_ambitoProv;

    private java.lang.String cod_ambitoccaa;

    private java.lang.String cod_ambitoisla;

    private java.lang.String cod_tipo_autoriz_admin;

    private java.lang.Object[] condicionesEspeciales;

    private java.lang.Long corr;

    private java.lang.String correlec;

    private java.lang.String cpApdoCorreos;

    private java.lang.String demandConVoluntad;

    private java.lang.String demandanteUE;

    private java.lang.String desc_notmuni_c;

    private java.lang.String desc_notmuni_e;

    private java.lang.String desc_notprov_c;

    private java.lang.String desc_notprov_e;

    private java.lang.String desc_remuni_c;

    private java.lang.String desc_remuni_e;

    private java.lang.String desc_reprov_c;

    private java.lang.String desc_reprov_e;

    private java.lang.String doc_incompl;

    private java.lang.String estado;

    private boolean existeLanbide;

    private boolean existeSISPE;

    private java.lang.String fax;

    private java.util.Calendar fecConf;

    private java.util.Calendar fecResolReconocido;

    private java.util.Calendar fec_fin_min;

    private java.util.Calendar fec_fin_vigencia;

    private java.util.Calendar fec_ini_min;

    private java.util.Calendar fec_sol_comunic_ren;

    private java.util.Calendar fecha_nac;

    private java.lang.String generoReconocido;

    private java.lang.Long grado_min;

    private java.lang.String histor;

    private java.lang.Integer indBloq;

    private java.lang.String indConf;

    private java.lang.Integer indSispe;

    private java.lang.String inter;

    private java.lang.String intermedia;

    private java.util.Vector lista_errores;

    private java.lang.String muniApdoCorreos;

    private java.lang.String nacion;

    private java.lang.String nivfor;

    private java.lang.String noapartcorreos;

    private java.lang.String nobisdup;

    private java.lang.String nocopos;

    private java.lang.String noescale;

    private java.lang.String noextcodpost;

    private java.lang.String noexttexto1;

    private java.lang.String noexttexto2;

    private java.lang.String nolepu;

    private java.lang.String nombre;

    private java.lang.String nombreReconocido;

    private java.lang.String nomuni;

    private java.lang.String nonovp;

    private java.lang.String nonuvp;

    private java.lang.String nopais;

    private java.lang.String nopiso;

    private java.lang.String noprovin;

    private java.lang.String notivipu;

    private java.lang.String num_doc;

    private java.lang.String num_exp;

    private java.lang.String num_exp_act;

    private java.lang.String num_ssss;

    private java.util.Calendar pais_fecha_fin;

    private java.lang.String pais_marca_eee;

    private java.lang.String pasaporte;

    private java.lang.String rebisdup;

    private java.lang.String reccaa;

    private java.lang.String recopos;

    private java.lang.String reescale;

    private java.lang.String reextcodpost;

    private java.lang.String reexttexto1;

    private java.lang.String reexttexto2;

    private java.lang.String relepu;

    private java.lang.String remuni;

    private java.lang.String renovp;

    private java.lang.String renut;

    private java.lang.String renuvp;

    private java.lang.String repais;

    private java.lang.String repiso;

    private java.lang.String reprovin;

    private java.lang.String restricciones_Actividad;

    private java.lang.String restricciones_Ocupacion;

    private java.lang.String retivipu;

    private java.lang.String rgi_activa;

    private java.lang.String rgi_importe;

    private java.lang.String sexo;

    private boolean tieneIndSispe;

    private java.lang.String tipo_benef;

    private java.lang.String tipo_doc;

    private java.lang.String tlfno1;

    private java.lang.String tlfno2;

    private java.lang.String tlfno3;

    private java.lang.String tlfno4;

    private java.lang.String tlfno_notif;

    private java.lang.Integer ultAgr;

    public PersonaFisicaWSValueObject() {
    }

    public PersonaFisicaWSValueObject(
           es.altia.agora.webservice.tercero.servicios.lanbide.cliente.AuditoriaValueObject auditoria,
           long objectId,
           java.lang.String ape1Reconocido,
           java.lang.String ape2Reconocido,
           java.lang.String apellido1,
           java.lang.String apellido2,
           java.lang.String bloqAgr,
           java.lang.String cert_poli,
           java.lang.String cert_ssss,
           java.lang.String cod_ambitoComarca,
           java.lang.String cod_ambitoMunicipio,
           java.lang.String cod_ambitoProv,
           java.lang.String cod_ambitoccaa,
           java.lang.String cod_ambitoisla,
           java.lang.String cod_tipo_autoriz_admin,
           java.lang.Object[] condicionesEspeciales,
           java.lang.Long corr,
           java.lang.String correlec,
           java.lang.String cpApdoCorreos,
           java.lang.String demandConVoluntad,
           java.lang.String demandanteUE,
           java.lang.String desc_notmuni_c,
           java.lang.String desc_notmuni_e,
           java.lang.String desc_notprov_c,
           java.lang.String desc_notprov_e,
           java.lang.String desc_remuni_c,
           java.lang.String desc_remuni_e,
           java.lang.String desc_reprov_c,
           java.lang.String desc_reprov_e,
           java.lang.String doc_incompl,
           java.lang.String estado,
           boolean existeLanbide,
           boolean existeSISPE,
           java.lang.String fax,
           java.util.Calendar fecConf,
           java.util.Calendar fecResolReconocido,
           java.util.Calendar fec_fin_min,
           java.util.Calendar fec_fin_vigencia,
           java.util.Calendar fec_ini_min,
           java.util.Calendar fec_sol_comunic_ren,
           java.util.Calendar fecha_nac,
           java.lang.String generoReconocido,
           java.lang.Long grado_min,
           java.lang.String histor,
           java.lang.Integer indBloq,
           java.lang.String indConf,
           java.lang.Integer indSispe,
           java.lang.String inter,
           java.lang.String intermedia,
           java.util.Vector lista_errores,
           java.lang.String muniApdoCorreos,
           java.lang.String nacion,
           java.lang.String nivfor,
           java.lang.String noapartcorreos,
           java.lang.String nobisdup,
           java.lang.String nocopos,
           java.lang.String noescale,
           java.lang.String noextcodpost,
           java.lang.String noexttexto1,
           java.lang.String noexttexto2,
           java.lang.String nolepu,
           java.lang.String nombre,
           java.lang.String nombreReconocido,
           java.lang.String nomuni,
           java.lang.String nonovp,
           java.lang.String nonuvp,
           java.lang.String nopais,
           java.lang.String nopiso,
           java.lang.String noprovin,
           java.lang.String notivipu,
           java.lang.String num_doc,
           java.lang.String num_exp,
           java.lang.String num_exp_act,
           java.lang.String num_ssss,
           java.util.Calendar pais_fecha_fin,
           java.lang.String pais_marca_eee,
           java.lang.String pasaporte,
           java.lang.String rebisdup,
           java.lang.String reccaa,
           java.lang.String recopos,
           java.lang.String reescale,
           java.lang.String reextcodpost,
           java.lang.String reexttexto1,
           java.lang.String reexttexto2,
           java.lang.String relepu,
           java.lang.String remuni,
           java.lang.String renovp,
           java.lang.String renut,
           java.lang.String renuvp,
           java.lang.String repais,
           java.lang.String repiso,
           java.lang.String reprovin,
           java.lang.String restricciones_Actividad,
           java.lang.String restricciones_Ocupacion,
           java.lang.String retivipu,
           java.lang.String rgi_activa,
           java.lang.String rgi_importe,
           java.lang.String sexo,
           boolean tieneIndSispe,
           java.lang.String tipo_benef,
           java.lang.String tipo_doc,
           java.lang.String tlfno1,
           java.lang.String tlfno2,
           java.lang.String tlfno3,
           java.lang.String tlfno4,
           java.lang.String tlfno_notif,
           java.lang.Integer ultAgr) {
        super(
            auditoria,
            objectId);
        this.ape1Reconocido = ape1Reconocido;
        this.ape2Reconocido = ape2Reconocido;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.bloqAgr = bloqAgr;
        this.cert_poli = cert_poli;
        this.cert_ssss = cert_ssss;
        this.cod_ambitoComarca = cod_ambitoComarca;
        this.cod_ambitoMunicipio = cod_ambitoMunicipio;
        this.cod_ambitoProv = cod_ambitoProv;
        this.cod_ambitoccaa = cod_ambitoccaa;
        this.cod_ambitoisla = cod_ambitoisla;
        this.cod_tipo_autoriz_admin = cod_tipo_autoriz_admin;
        this.condicionesEspeciales = condicionesEspeciales;
        this.corr = corr;
        this.correlec = correlec;
        this.cpApdoCorreos = cpApdoCorreos;
        this.demandConVoluntad = demandConVoluntad;
        this.demandanteUE = demandanteUE;
        this.desc_notmuni_c = desc_notmuni_c;
        this.desc_notmuni_e = desc_notmuni_e;
        this.desc_notprov_c = desc_notprov_c;
        this.desc_notprov_e = desc_notprov_e;
        this.desc_remuni_c = desc_remuni_c;
        this.desc_remuni_e = desc_remuni_e;
        this.desc_reprov_c = desc_reprov_c;
        this.desc_reprov_e = desc_reprov_e;
        this.doc_incompl = doc_incompl;
        this.estado = estado;
        this.existeLanbide = existeLanbide;
        this.existeSISPE = existeSISPE;
        this.fax = fax;
        this.fecConf = fecConf;
        this.fecResolReconocido = fecResolReconocido;
        this.fec_fin_min = fec_fin_min;
        this.fec_fin_vigencia = fec_fin_vigencia;
        this.fec_ini_min = fec_ini_min;
        this.fec_sol_comunic_ren = fec_sol_comunic_ren;
        this.fecha_nac = fecha_nac;
        this.generoReconocido = generoReconocido;
        this.grado_min = grado_min;
        this.histor = histor;
        this.indBloq = indBloq;
        this.indConf = indConf;
        this.indSispe = indSispe;
        this.inter = inter;
        this.intermedia = intermedia;
        this.lista_errores = lista_errores;
        this.muniApdoCorreos = muniApdoCorreos;
        this.nacion = nacion;
        this.nivfor = nivfor;
        this.noapartcorreos = noapartcorreos;
        this.nobisdup = nobisdup;
        this.nocopos = nocopos;
        this.noescale = noescale;
        this.noextcodpost = noextcodpost;
        this.noexttexto1 = noexttexto1;
        this.noexttexto2 = noexttexto2;
        this.nolepu = nolepu;
        this.nombre = nombre;
        this.nombreReconocido = nombreReconocido;
        this.nomuni = nomuni;
        this.nonovp = nonovp;
        this.nonuvp = nonuvp;
        this.nopais = nopais;
        this.nopiso = nopiso;
        this.noprovin = noprovin;
        this.notivipu = notivipu;
        this.num_doc = num_doc;
        this.num_exp = num_exp;
        this.num_exp_act = num_exp_act;
        this.num_ssss = num_ssss;
        this.pais_fecha_fin = pais_fecha_fin;
        this.pais_marca_eee = pais_marca_eee;
        this.pasaporte = pasaporte;
        this.rebisdup = rebisdup;
        this.reccaa = reccaa;
        this.recopos = recopos;
        this.reescale = reescale;
        this.reextcodpost = reextcodpost;
        this.reexttexto1 = reexttexto1;
        this.reexttexto2 = reexttexto2;
        this.relepu = relepu;
        this.remuni = remuni;
        this.renovp = renovp;
        this.renut = renut;
        this.renuvp = renuvp;
        this.repais = repais;
        this.repiso = repiso;
        this.reprovin = reprovin;
        this.restricciones_Actividad = restricciones_Actividad;
        this.restricciones_Ocupacion = restricciones_Ocupacion;
        this.retivipu = retivipu;
        this.rgi_activa = rgi_activa;
        this.rgi_importe = rgi_importe;
        this.sexo = sexo;
        this.tieneIndSispe = tieneIndSispe;
        this.tipo_benef = tipo_benef;
        this.tipo_doc = tipo_doc;
        this.tlfno1 = tlfno1;
        this.tlfno2 = tlfno2;
        this.tlfno3 = tlfno3;
        this.tlfno4 = tlfno4;
        this.tlfno_notif = tlfno_notif;
        this.ultAgr = ultAgr;
    }


    /**
     * Gets the ape1Reconocido value for this PersonaFisicaWSValueObject.
     * 
     * @return ape1Reconocido
     */
    public java.lang.String getApe1Reconocido() {
        return ape1Reconocido;
    }


    /**
     * Sets the ape1Reconocido value for this PersonaFisicaWSValueObject.
     * 
     * @param ape1Reconocido
     */
    public void setApe1Reconocido(java.lang.String ape1Reconocido) {
        this.ape1Reconocido = ape1Reconocido;
    }


    /**
     * Gets the ape2Reconocido value for this PersonaFisicaWSValueObject.
     * 
     * @return ape2Reconocido
     */
    public java.lang.String getApe2Reconocido() {
        return ape2Reconocido;
    }


    /**
     * Sets the ape2Reconocido value for this PersonaFisicaWSValueObject.
     * 
     * @param ape2Reconocido
     */
    public void setApe2Reconocido(java.lang.String ape2Reconocido) {
        this.ape2Reconocido = ape2Reconocido;
    }


    /**
     * Gets the apellido1 value for this PersonaFisicaWSValueObject.
     * 
     * @return apellido1
     */
    public java.lang.String getApellido1() {
        return apellido1;
    }


    /**
     * Sets the apellido1 value for this PersonaFisicaWSValueObject.
     * 
     * @param apellido1
     */
    public void setApellido1(java.lang.String apellido1) {
        this.apellido1 = apellido1;
    }


    /**
     * Gets the apellido2 value for this PersonaFisicaWSValueObject.
     * 
     * @return apellido2
     */
    public java.lang.String getApellido2() {
        return apellido2;
    }


    /**
     * Sets the apellido2 value for this PersonaFisicaWSValueObject.
     * 
     * @param apellido2
     */
    public void setApellido2(java.lang.String apellido2) {
        this.apellido2 = apellido2;
    }


    /**
     * Gets the bloqAgr value for this PersonaFisicaWSValueObject.
     * 
     * @return bloqAgr
     */
    public java.lang.String getBloqAgr() {
        return bloqAgr;
    }


    /**
     * Sets the bloqAgr value for this PersonaFisicaWSValueObject.
     * 
     * @param bloqAgr
     */
    public void setBloqAgr(java.lang.String bloqAgr) {
        this.bloqAgr = bloqAgr;
    }


    /**
     * Gets the cert_poli value for this PersonaFisicaWSValueObject.
     * 
     * @return cert_poli
     */
    public java.lang.String getCert_poli() {
        return cert_poli;
    }


    /**
     * Sets the cert_poli value for this PersonaFisicaWSValueObject.
     * 
     * @param cert_poli
     */
    public void setCert_poli(java.lang.String cert_poli) {
        this.cert_poli = cert_poli;
    }


    /**
     * Gets the cert_ssss value for this PersonaFisicaWSValueObject.
     * 
     * @return cert_ssss
     */
    public java.lang.String getCert_ssss() {
        return cert_ssss;
    }


    /**
     * Sets the cert_ssss value for this PersonaFisicaWSValueObject.
     * 
     * @param cert_ssss
     */
    public void setCert_ssss(java.lang.String cert_ssss) {
        this.cert_ssss = cert_ssss;
    }


    /**
     * Gets the cod_ambitoComarca value for this PersonaFisicaWSValueObject.
     * 
     * @return cod_ambitoComarca
     */
    public java.lang.String getCod_ambitoComarca() {
        return cod_ambitoComarca;
    }


    /**
     * Sets the cod_ambitoComarca value for this PersonaFisicaWSValueObject.
     * 
     * @param cod_ambitoComarca
     */
    public void setCod_ambitoComarca(java.lang.String cod_ambitoComarca) {
        this.cod_ambitoComarca = cod_ambitoComarca;
    }


    /**
     * Gets the cod_ambitoMunicipio value for this PersonaFisicaWSValueObject.
     * 
     * @return cod_ambitoMunicipio
     */
    public java.lang.String getCod_ambitoMunicipio() {
        return cod_ambitoMunicipio;
    }


    /**
     * Sets the cod_ambitoMunicipio value for this PersonaFisicaWSValueObject.
     * 
     * @param cod_ambitoMunicipio
     */
    public void setCod_ambitoMunicipio(java.lang.String cod_ambitoMunicipio) {
        this.cod_ambitoMunicipio = cod_ambitoMunicipio;
    }


    /**
     * Gets the cod_ambitoProv value for this PersonaFisicaWSValueObject.
     * 
     * @return cod_ambitoProv
     */
    public java.lang.String getCod_ambitoProv() {
        return cod_ambitoProv;
    }


    /**
     * Sets the cod_ambitoProv value for this PersonaFisicaWSValueObject.
     * 
     * @param cod_ambitoProv
     */
    public void setCod_ambitoProv(java.lang.String cod_ambitoProv) {
        this.cod_ambitoProv = cod_ambitoProv;
    }


    /**
     * Gets the cod_ambitoccaa value for this PersonaFisicaWSValueObject.
     * 
     * @return cod_ambitoccaa
     */
    public java.lang.String getCod_ambitoccaa() {
        return cod_ambitoccaa;
    }


    /**
     * Sets the cod_ambitoccaa value for this PersonaFisicaWSValueObject.
     * 
     * @param cod_ambitoccaa
     */
    public void setCod_ambitoccaa(java.lang.String cod_ambitoccaa) {
        this.cod_ambitoccaa = cod_ambitoccaa;
    }


    /**
     * Gets the cod_ambitoisla value for this PersonaFisicaWSValueObject.
     * 
     * @return cod_ambitoisla
     */
    public java.lang.String getCod_ambitoisla() {
        return cod_ambitoisla;
    }


    /**
     * Sets the cod_ambitoisla value for this PersonaFisicaWSValueObject.
     * 
     * @param cod_ambitoisla
     */
    public void setCod_ambitoisla(java.lang.String cod_ambitoisla) {
        this.cod_ambitoisla = cod_ambitoisla;
    }


    /**
     * Gets the cod_tipo_autoriz_admin value for this PersonaFisicaWSValueObject.
     * 
     * @return cod_tipo_autoriz_admin
     */
    public java.lang.String getCod_tipo_autoriz_admin() {
        return cod_tipo_autoriz_admin;
    }


    /**
     * Sets the cod_tipo_autoriz_admin value for this PersonaFisicaWSValueObject.
     * 
     * @param cod_tipo_autoriz_admin
     */
    public void setCod_tipo_autoriz_admin(java.lang.String cod_tipo_autoriz_admin) {
        this.cod_tipo_autoriz_admin = cod_tipo_autoriz_admin;
    }


    /**
     * Gets the condicionesEspeciales value for this PersonaFisicaWSValueObject.
     * 
     * @return condicionesEspeciales
     */
    public java.lang.Object[] getCondicionesEspeciales() {
        return condicionesEspeciales;
    }


    /**
     * Sets the condicionesEspeciales value for this PersonaFisicaWSValueObject.
     * 
     * @param condicionesEspeciales
     */
    public void setCondicionesEspeciales(java.lang.Object[] condicionesEspeciales) {
        this.condicionesEspeciales = condicionesEspeciales;
    }


    /**
     * Gets the corr value for this PersonaFisicaWSValueObject.
     * 
     * @return corr
     */
    public java.lang.Long getCorr() {
        return corr;
    }


    /**
     * Sets the corr value for this PersonaFisicaWSValueObject.
     * 
     * @param corr
     */
    public void setCorr(java.lang.Long corr) {
        this.corr = corr;
    }


    /**
     * Gets the correlec value for this PersonaFisicaWSValueObject.
     * 
     * @return correlec
     */
    public java.lang.String getCorrelec() {
        return correlec;
    }


    /**
     * Sets the correlec value for this PersonaFisicaWSValueObject.
     * 
     * @param correlec
     */
    public void setCorrelec(java.lang.String correlec) {
        this.correlec = correlec;
    }


    /**
     * Gets the cpApdoCorreos value for this PersonaFisicaWSValueObject.
     * 
     * @return cpApdoCorreos
     */
    public java.lang.String getCpApdoCorreos() {
        return cpApdoCorreos;
    }


    /**
     * Sets the cpApdoCorreos value for this PersonaFisicaWSValueObject.
     * 
     * @param cpApdoCorreos
     */
    public void setCpApdoCorreos(java.lang.String cpApdoCorreos) {
        this.cpApdoCorreos = cpApdoCorreos;
    }


    /**
     * Gets the demandConVoluntad value for this PersonaFisicaWSValueObject.
     * 
     * @return demandConVoluntad
     */
    public java.lang.String getDemandConVoluntad() {
        return demandConVoluntad;
    }


    /**
     * Sets the demandConVoluntad value for this PersonaFisicaWSValueObject.
     * 
     * @param demandConVoluntad
     */
    public void setDemandConVoluntad(java.lang.String demandConVoluntad) {
        this.demandConVoluntad = demandConVoluntad;
    }


    /**
     * Gets the demandanteUE value for this PersonaFisicaWSValueObject.
     * 
     * @return demandanteUE
     */
    public java.lang.String getDemandanteUE() {
        return demandanteUE;
    }


    /**
     * Sets the demandanteUE value for this PersonaFisicaWSValueObject.
     * 
     * @param demandanteUE
     */
    public void setDemandanteUE(java.lang.String demandanteUE) {
        this.demandanteUE = demandanteUE;
    }


    /**
     * Gets the desc_notmuni_c value for this PersonaFisicaWSValueObject.
     * 
     * @return desc_notmuni_c
     */
    public java.lang.String getDesc_notmuni_c() {
        return desc_notmuni_c;
    }


    /**
     * Sets the desc_notmuni_c value for this PersonaFisicaWSValueObject.
     * 
     * @param desc_notmuni_c
     */
    public void setDesc_notmuni_c(java.lang.String desc_notmuni_c) {
        this.desc_notmuni_c = desc_notmuni_c;
    }


    /**
     * Gets the desc_notmuni_e value for this PersonaFisicaWSValueObject.
     * 
     * @return desc_notmuni_e
     */
    public java.lang.String getDesc_notmuni_e() {
        return desc_notmuni_e;
    }


    /**
     * Sets the desc_notmuni_e value for this PersonaFisicaWSValueObject.
     * 
     * @param desc_notmuni_e
     */
    public void setDesc_notmuni_e(java.lang.String desc_notmuni_e) {
        this.desc_notmuni_e = desc_notmuni_e;
    }


    /**
     * Gets the desc_notprov_c value for this PersonaFisicaWSValueObject.
     * 
     * @return desc_notprov_c
     */
    public java.lang.String getDesc_notprov_c() {
        return desc_notprov_c;
    }


    /**
     * Sets the desc_notprov_c value for this PersonaFisicaWSValueObject.
     * 
     * @param desc_notprov_c
     */
    public void setDesc_notprov_c(java.lang.String desc_notprov_c) {
        this.desc_notprov_c = desc_notprov_c;
    }


    /**
     * Gets the desc_notprov_e value for this PersonaFisicaWSValueObject.
     * 
     * @return desc_notprov_e
     */
    public java.lang.String getDesc_notprov_e() {
        return desc_notprov_e;
    }


    /**
     * Sets the desc_notprov_e value for this PersonaFisicaWSValueObject.
     * 
     * @param desc_notprov_e
     */
    public void setDesc_notprov_e(java.lang.String desc_notprov_e) {
        this.desc_notprov_e = desc_notprov_e;
    }


    /**
     * Gets the desc_remuni_c value for this PersonaFisicaWSValueObject.
     * 
     * @return desc_remuni_c
     */
    public java.lang.String getDesc_remuni_c() {
        return desc_remuni_c;
    }


    /**
     * Sets the desc_remuni_c value for this PersonaFisicaWSValueObject.
     * 
     * @param desc_remuni_c
     */
    public void setDesc_remuni_c(java.lang.String desc_remuni_c) {
        this.desc_remuni_c = desc_remuni_c;
    }


    /**
     * Gets the desc_remuni_e value for this PersonaFisicaWSValueObject.
     * 
     * @return desc_remuni_e
     */
    public java.lang.String getDesc_remuni_e() {
        return desc_remuni_e;
    }


    /**
     * Sets the desc_remuni_e value for this PersonaFisicaWSValueObject.
     * 
     * @param desc_remuni_e
     */
    public void setDesc_remuni_e(java.lang.String desc_remuni_e) {
        this.desc_remuni_e = desc_remuni_e;
    }


    /**
     * Gets the desc_reprov_c value for this PersonaFisicaWSValueObject.
     * 
     * @return desc_reprov_c
     */
    public java.lang.String getDesc_reprov_c() {
        return desc_reprov_c;
    }


    /**
     * Sets the desc_reprov_c value for this PersonaFisicaWSValueObject.
     * 
     * @param desc_reprov_c
     */
    public void setDesc_reprov_c(java.lang.String desc_reprov_c) {
        this.desc_reprov_c = desc_reprov_c;
    }


    /**
     * Gets the desc_reprov_e value for this PersonaFisicaWSValueObject.
     * 
     * @return desc_reprov_e
     */
    public java.lang.String getDesc_reprov_e() {
        return desc_reprov_e;
    }


    /**
     * Sets the desc_reprov_e value for this PersonaFisicaWSValueObject.
     * 
     * @param desc_reprov_e
     */
    public void setDesc_reprov_e(java.lang.String desc_reprov_e) {
        this.desc_reprov_e = desc_reprov_e;
    }


    /**
     * Gets the doc_incompl value for this PersonaFisicaWSValueObject.
     * 
     * @return doc_incompl
     */
    public java.lang.String getDoc_incompl() {
        return doc_incompl;
    }


    /**
     * Sets the doc_incompl value for this PersonaFisicaWSValueObject.
     * 
     * @param doc_incompl
     */
    public void setDoc_incompl(java.lang.String doc_incompl) {
        this.doc_incompl = doc_incompl;
    }


    /**
     * Gets the estado value for this PersonaFisicaWSValueObject.
     * 
     * @return estado
     */
    public java.lang.String getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this PersonaFisicaWSValueObject.
     * 
     * @param estado
     */
    public void setEstado(java.lang.String estado) {
        this.estado = estado;
    }


    /**
     * Gets the existeLanbide value for this PersonaFisicaWSValueObject.
     * 
     * @return existeLanbide
     */
    public boolean isExisteLanbide() {
        return existeLanbide;
    }


    /**
     * Sets the existeLanbide value for this PersonaFisicaWSValueObject.
     * 
     * @param existeLanbide
     */
    public void setExisteLanbide(boolean existeLanbide) {
        this.existeLanbide = existeLanbide;
    }


    /**
     * Gets the existeSISPE value for this PersonaFisicaWSValueObject.
     * 
     * @return existeSISPE
     */
    public boolean isExisteSISPE() {
        return existeSISPE;
    }


    /**
     * Sets the existeSISPE value for this PersonaFisicaWSValueObject.
     * 
     * @param existeSISPE
     */
    public void setExisteSISPE(boolean existeSISPE) {
        this.existeSISPE = existeSISPE;
    }


    /**
     * Gets the fax value for this PersonaFisicaWSValueObject.
     * 
     * @return fax
     */
    public java.lang.String getFax() {
        return fax;
    }


    /**
     * Sets the fax value for this PersonaFisicaWSValueObject.
     * 
     * @param fax
     */
    public void setFax(java.lang.String fax) {
        this.fax = fax;
    }


    /**
     * Gets the fecConf value for this PersonaFisicaWSValueObject.
     * 
     * @return fecConf
     */
    public java.util.Calendar getFecConf() {
        return fecConf;
    }


    /**
     * Sets the fecConf value for this PersonaFisicaWSValueObject.
     * 
     * @param fecConf
     */
    public void setFecConf(java.util.Calendar fecConf) {
        this.fecConf = fecConf;
    }


    /**
     * Gets the fecResolReconocido value for this PersonaFisicaWSValueObject.
     * 
     * @return fecResolReconocido
     */
    public java.util.Calendar getFecResolReconocido() {
        return fecResolReconocido;
    }


    /**
     * Sets the fecResolReconocido value for this PersonaFisicaWSValueObject.
     * 
     * @param fecResolReconocido
     */
    public void setFecResolReconocido(java.util.Calendar fecResolReconocido) {
        this.fecResolReconocido = fecResolReconocido;
    }


    /**
     * Gets the fec_fin_min value for this PersonaFisicaWSValueObject.
     * 
     * @return fec_fin_min
     */
    public java.util.Calendar getFec_fin_min() {
        return fec_fin_min;
    }


    /**
     * Sets the fec_fin_min value for this PersonaFisicaWSValueObject.
     * 
     * @param fec_fin_min
     */
    public void setFec_fin_min(java.util.Calendar fec_fin_min) {
        this.fec_fin_min = fec_fin_min;
    }


    /**
     * Gets the fec_fin_vigencia value for this PersonaFisicaWSValueObject.
     * 
     * @return fec_fin_vigencia
     */
    public java.util.Calendar getFec_fin_vigencia() {
        return fec_fin_vigencia;
    }


    /**
     * Sets the fec_fin_vigencia value for this PersonaFisicaWSValueObject.
     * 
     * @param fec_fin_vigencia
     */
    public void setFec_fin_vigencia(java.util.Calendar fec_fin_vigencia) {
        this.fec_fin_vigencia = fec_fin_vigencia;
    }


    /**
     * Gets the fec_ini_min value for this PersonaFisicaWSValueObject.
     * 
     * @return fec_ini_min
     */
    public java.util.Calendar getFec_ini_min() {
        return fec_ini_min;
    }


    /**
     * Sets the fec_ini_min value for this PersonaFisicaWSValueObject.
     * 
     * @param fec_ini_min
     */
    public void setFec_ini_min(java.util.Calendar fec_ini_min) {
        this.fec_ini_min = fec_ini_min;
    }


    /**
     * Gets the fec_sol_comunic_ren value for this PersonaFisicaWSValueObject.
     * 
     * @return fec_sol_comunic_ren
     */
    public java.util.Calendar getFec_sol_comunic_ren() {
        return fec_sol_comunic_ren;
    }


    /**
     * Sets the fec_sol_comunic_ren value for this PersonaFisicaWSValueObject.
     * 
     * @param fec_sol_comunic_ren
     */
    public void setFec_sol_comunic_ren(java.util.Calendar fec_sol_comunic_ren) {
        this.fec_sol_comunic_ren = fec_sol_comunic_ren;
    }


    /**
     * Gets the fecha_nac value for this PersonaFisicaWSValueObject.
     * 
     * @return fecha_nac
     */
    public java.util.Calendar getFecha_nac() {
        return fecha_nac;
    }


    /**
     * Sets the fecha_nac value for this PersonaFisicaWSValueObject.
     * 
     * @param fecha_nac
     */
    public void setFecha_nac(java.util.Calendar fecha_nac) {
        this.fecha_nac = fecha_nac;
    }


    /**
     * Gets the generoReconocido value for this PersonaFisicaWSValueObject.
     * 
     * @return generoReconocido
     */
    public java.lang.String getGeneroReconocido() {
        return generoReconocido;
    }


    /**
     * Sets the generoReconocido value for this PersonaFisicaWSValueObject.
     * 
     * @param generoReconocido
     */
    public void setGeneroReconocido(java.lang.String generoReconocido) {
        this.generoReconocido = generoReconocido;
    }


    /**
     * Gets the grado_min value for this PersonaFisicaWSValueObject.
     * 
     * @return grado_min
     */
    public java.lang.Long getGrado_min() {
        return grado_min;
    }


    /**
     * Sets the grado_min value for this PersonaFisicaWSValueObject.
     * 
     * @param grado_min
     */
    public void setGrado_min(java.lang.Long grado_min) {
        this.grado_min = grado_min;
    }


    /**
     * Gets the histor value for this PersonaFisicaWSValueObject.
     * 
     * @return histor
     */
    public java.lang.String getHistor() {
        return histor;
    }


    /**
     * Sets the histor value for this PersonaFisicaWSValueObject.
     * 
     * @param histor
     */
    public void setHistor(java.lang.String histor) {
        this.histor = histor;
    }


    /**
     * Gets the indBloq value for this PersonaFisicaWSValueObject.
     * 
     * @return indBloq
     */
    public java.lang.Integer getIndBloq() {
        return indBloq;
    }


    /**
     * Sets the indBloq value for this PersonaFisicaWSValueObject.
     * 
     * @param indBloq
     */
    public void setIndBloq(java.lang.Integer indBloq) {
        this.indBloq = indBloq;
    }


    /**
     * Gets the indConf value for this PersonaFisicaWSValueObject.
     * 
     * @return indConf
     */
    public java.lang.String getIndConf() {
        return indConf;
    }


    /**
     * Sets the indConf value for this PersonaFisicaWSValueObject.
     * 
     * @param indConf
     */
    public void setIndConf(java.lang.String indConf) {
        this.indConf = indConf;
    }


    /**
     * Gets the indSispe value for this PersonaFisicaWSValueObject.
     * 
     * @return indSispe
     */
    public java.lang.Integer getIndSispe() {
        return indSispe;
    }


    /**
     * Sets the indSispe value for this PersonaFisicaWSValueObject.
     * 
     * @param indSispe
     */
    public void setIndSispe(java.lang.Integer indSispe) {
        this.indSispe = indSispe;
    }


    /**
     * Gets the inter value for this PersonaFisicaWSValueObject.
     * 
     * @return inter
     */
    public java.lang.String getInter() {
        return inter;
    }


    /**
     * Sets the inter value for this PersonaFisicaWSValueObject.
     * 
     * @param inter
     */
    public void setInter(java.lang.String inter) {
        this.inter = inter;
    }


    /**
     * Gets the intermedia value for this PersonaFisicaWSValueObject.
     * 
     * @return intermedia
     */
    public java.lang.String getIntermedia() {
        return intermedia;
    }


    /**
     * Sets the intermedia value for this PersonaFisicaWSValueObject.
     * 
     * @param intermedia
     */
    public void setIntermedia(java.lang.String intermedia) {
        this.intermedia = intermedia;
    }


    /**
     * Gets the lista_errores value for this PersonaFisicaWSValueObject.
     * 
     * @return lista_errores
     */
    public java.util.Vector getLista_errores() {
        return lista_errores;
    }


    /**
     * Sets the lista_errores value for this PersonaFisicaWSValueObject.
     * 
     * @param lista_errores
     */
    public void setLista_errores(java.util.Vector lista_errores) {
        this.lista_errores = lista_errores;
    }


    /**
     * Gets the muniApdoCorreos value for this PersonaFisicaWSValueObject.
     * 
     * @return muniApdoCorreos
     */
    public java.lang.String getMuniApdoCorreos() {
        return muniApdoCorreos;
    }


    /**
     * Sets the muniApdoCorreos value for this PersonaFisicaWSValueObject.
     * 
     * @param muniApdoCorreos
     */
    public void setMuniApdoCorreos(java.lang.String muniApdoCorreos) {
        this.muniApdoCorreos = muniApdoCorreos;
    }


    /**
     * Gets the nacion value for this PersonaFisicaWSValueObject.
     * 
     * @return nacion
     */
    public java.lang.String getNacion() {
        return nacion;
    }


    /**
     * Sets the nacion value for this PersonaFisicaWSValueObject.
     * 
     * @param nacion
     */
    public void setNacion(java.lang.String nacion) {
        this.nacion = nacion;
    }


    /**
     * Gets the nivfor value for this PersonaFisicaWSValueObject.
     * 
     * @return nivfor
     */
    public java.lang.String getNivfor() {
        return nivfor;
    }


    /**
     * Sets the nivfor value for this PersonaFisicaWSValueObject.
     * 
     * @param nivfor
     */
    public void setNivfor(java.lang.String nivfor) {
        this.nivfor = nivfor;
    }


    /**
     * Gets the noapartcorreos value for this PersonaFisicaWSValueObject.
     * 
     * @return noapartcorreos
     */
    public java.lang.String getNoapartcorreos() {
        return noapartcorreos;
    }


    /**
     * Sets the noapartcorreos value for this PersonaFisicaWSValueObject.
     * 
     * @param noapartcorreos
     */
    public void setNoapartcorreos(java.lang.String noapartcorreos) {
        this.noapartcorreos = noapartcorreos;
    }


    /**
     * Gets the nobisdup value for this PersonaFisicaWSValueObject.
     * 
     * @return nobisdup
     */
    public java.lang.String getNobisdup() {
        return nobisdup;
    }


    /**
     * Sets the nobisdup value for this PersonaFisicaWSValueObject.
     * 
     * @param nobisdup
     */
    public void setNobisdup(java.lang.String nobisdup) {
        this.nobisdup = nobisdup;
    }


    /**
     * Gets the nocopos value for this PersonaFisicaWSValueObject.
     * 
     * @return nocopos
     */
    public java.lang.String getNocopos() {
        return nocopos;
    }


    /**
     * Sets the nocopos value for this PersonaFisicaWSValueObject.
     * 
     * @param nocopos
     */
    public void setNocopos(java.lang.String nocopos) {
        this.nocopos = nocopos;
    }


    /**
     * Gets the noescale value for this PersonaFisicaWSValueObject.
     * 
     * @return noescale
     */
    public java.lang.String getNoescale() {
        return noescale;
    }


    /**
     * Sets the noescale value for this PersonaFisicaWSValueObject.
     * 
     * @param noescale
     */
    public void setNoescale(java.lang.String noescale) {
        this.noescale = noescale;
    }


    /**
     * Gets the noextcodpost value for this PersonaFisicaWSValueObject.
     * 
     * @return noextcodpost
     */
    public java.lang.String getNoextcodpost() {
        return noextcodpost;
    }


    /**
     * Sets the noextcodpost value for this PersonaFisicaWSValueObject.
     * 
     * @param noextcodpost
     */
    public void setNoextcodpost(java.lang.String noextcodpost) {
        this.noextcodpost = noextcodpost;
    }


    /**
     * Gets the noexttexto1 value for this PersonaFisicaWSValueObject.
     * 
     * @return noexttexto1
     */
    public java.lang.String getNoexttexto1() {
        return noexttexto1;
    }


    /**
     * Sets the noexttexto1 value for this PersonaFisicaWSValueObject.
     * 
     * @param noexttexto1
     */
    public void setNoexttexto1(java.lang.String noexttexto1) {
        this.noexttexto1 = noexttexto1;
    }


    /**
     * Gets the noexttexto2 value for this PersonaFisicaWSValueObject.
     * 
     * @return noexttexto2
     */
    public java.lang.String getNoexttexto2() {
        return noexttexto2;
    }


    /**
     * Sets the noexttexto2 value for this PersonaFisicaWSValueObject.
     * 
     * @param noexttexto2
     */
    public void setNoexttexto2(java.lang.String noexttexto2) {
        this.noexttexto2 = noexttexto2;
    }


    /**
     * Gets the nolepu value for this PersonaFisicaWSValueObject.
     * 
     * @return nolepu
     */
    public java.lang.String getNolepu() {
        return nolepu;
    }


    /**
     * Sets the nolepu value for this PersonaFisicaWSValueObject.
     * 
     * @param nolepu
     */
    public void setNolepu(java.lang.String nolepu) {
        this.nolepu = nolepu;
    }


    /**
     * Gets the nombre value for this PersonaFisicaWSValueObject.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this PersonaFisicaWSValueObject.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the nombreReconocido value for this PersonaFisicaWSValueObject.
     * 
     * @return nombreReconocido
     */
    public java.lang.String getNombreReconocido() {
        return nombreReconocido;
    }


    /**
     * Sets the nombreReconocido value for this PersonaFisicaWSValueObject.
     * 
     * @param nombreReconocido
     */
    public void setNombreReconocido(java.lang.String nombreReconocido) {
        this.nombreReconocido = nombreReconocido;
    }


    /**
     * Gets the nomuni value for this PersonaFisicaWSValueObject.
     * 
     * @return nomuni
     */
    public java.lang.String getNomuni() {
        return nomuni;
    }


    /**
     * Sets the nomuni value for this PersonaFisicaWSValueObject.
     * 
     * @param nomuni
     */
    public void setNomuni(java.lang.String nomuni) {
        this.nomuni = nomuni;
    }


    /**
     * Gets the nonovp value for this PersonaFisicaWSValueObject.
     * 
     * @return nonovp
     */
    public java.lang.String getNonovp() {
        return nonovp;
    }


    /**
     * Sets the nonovp value for this PersonaFisicaWSValueObject.
     * 
     * @param nonovp
     */
    public void setNonovp(java.lang.String nonovp) {
        this.nonovp = nonovp;
    }


    /**
     * Gets the nonuvp value for this PersonaFisicaWSValueObject.
     * 
     * @return nonuvp
     */
    public java.lang.String getNonuvp() {
        return nonuvp;
    }


    /**
     * Sets the nonuvp value for this PersonaFisicaWSValueObject.
     * 
     * @param nonuvp
     */
    public void setNonuvp(java.lang.String nonuvp) {
        this.nonuvp = nonuvp;
    }


    /**
     * Gets the nopais value for this PersonaFisicaWSValueObject.
     * 
     * @return nopais
     */
    public java.lang.String getNopais() {
        return nopais;
    }


    /**
     * Sets the nopais value for this PersonaFisicaWSValueObject.
     * 
     * @param nopais
     */
    public void setNopais(java.lang.String nopais) {
        this.nopais = nopais;
    }


    /**
     * Gets the nopiso value for this PersonaFisicaWSValueObject.
     * 
     * @return nopiso
     */
    public java.lang.String getNopiso() {
        return nopiso;
    }


    /**
     * Sets the nopiso value for this PersonaFisicaWSValueObject.
     * 
     * @param nopiso
     */
    public void setNopiso(java.lang.String nopiso) {
        this.nopiso = nopiso;
    }


    /**
     * Gets the noprovin value for this PersonaFisicaWSValueObject.
     * 
     * @return noprovin
     */
    public java.lang.String getNoprovin() {
        return noprovin;
    }


    /**
     * Sets the noprovin value for this PersonaFisicaWSValueObject.
     * 
     * @param noprovin
     */
    public void setNoprovin(java.lang.String noprovin) {
        this.noprovin = noprovin;
    }


    /**
     * Gets the notivipu value for this PersonaFisicaWSValueObject.
     * 
     * @return notivipu
     */
    public java.lang.String getNotivipu() {
        return notivipu;
    }


    /**
     * Sets the notivipu value for this PersonaFisicaWSValueObject.
     * 
     * @param notivipu
     */
    public void setNotivipu(java.lang.String notivipu) {
        this.notivipu = notivipu;
    }


    /**
     * Gets the num_doc value for this PersonaFisicaWSValueObject.
     * 
     * @return num_doc
     */
    public java.lang.String getNum_doc() {
        return num_doc;
    }


    /**
     * Sets the num_doc value for this PersonaFisicaWSValueObject.
     * 
     * @param num_doc
     */
    public void setNum_doc(java.lang.String num_doc) {
        this.num_doc = num_doc;
    }


    /**
     * Gets the num_exp value for this PersonaFisicaWSValueObject.
     * 
     * @return num_exp
     */
    public java.lang.String getNum_exp() {
        return num_exp;
    }


    /**
     * Sets the num_exp value for this PersonaFisicaWSValueObject.
     * 
     * @param num_exp
     */
    public void setNum_exp(java.lang.String num_exp) {
        this.num_exp = num_exp;
    }


    /**
     * Gets the num_exp_act value for this PersonaFisicaWSValueObject.
     * 
     * @return num_exp_act
     */
    public java.lang.String getNum_exp_act() {
        return num_exp_act;
    }


    /**
     * Sets the num_exp_act value for this PersonaFisicaWSValueObject.
     * 
     * @param num_exp_act
     */
    public void setNum_exp_act(java.lang.String num_exp_act) {
        this.num_exp_act = num_exp_act;
    }


    /**
     * Gets the num_ssss value for this PersonaFisicaWSValueObject.
     * 
     * @return num_ssss
     */
    public java.lang.String getNum_ssss() {
        return num_ssss;
    }


    /**
     * Sets the num_ssss value for this PersonaFisicaWSValueObject.
     * 
     * @param num_ssss
     */
    public void setNum_ssss(java.lang.String num_ssss) {
        this.num_ssss = num_ssss;
    }


    /**
     * Gets the pais_fecha_fin value for this PersonaFisicaWSValueObject.
     * 
     * @return pais_fecha_fin
     */
    public java.util.Calendar getPais_fecha_fin() {
        return pais_fecha_fin;
    }


    /**
     * Sets the pais_fecha_fin value for this PersonaFisicaWSValueObject.
     * 
     * @param pais_fecha_fin
     */
    public void setPais_fecha_fin(java.util.Calendar pais_fecha_fin) {
        this.pais_fecha_fin = pais_fecha_fin;
    }


    /**
     * Gets the pais_marca_eee value for this PersonaFisicaWSValueObject.
     * 
     * @return pais_marca_eee
     */
    public java.lang.String getPais_marca_eee() {
        return pais_marca_eee;
    }


    /**
     * Sets the pais_marca_eee value for this PersonaFisicaWSValueObject.
     * 
     * @param pais_marca_eee
     */
    public void setPais_marca_eee(java.lang.String pais_marca_eee) {
        this.pais_marca_eee = pais_marca_eee;
    }


    /**
     * Gets the pasaporte value for this PersonaFisicaWSValueObject.
     * 
     * @return pasaporte
     */
    public java.lang.String getPasaporte() {
        return pasaporte;
    }


    /**
     * Sets the pasaporte value for this PersonaFisicaWSValueObject.
     * 
     * @param pasaporte
     */
    public void setPasaporte(java.lang.String pasaporte) {
        this.pasaporte = pasaporte;
    }


    /**
     * Gets the rebisdup value for this PersonaFisicaWSValueObject.
     * 
     * @return rebisdup
     */
    public java.lang.String getRebisdup() {
        return rebisdup;
    }


    /**
     * Sets the rebisdup value for this PersonaFisicaWSValueObject.
     * 
     * @param rebisdup
     */
    public void setRebisdup(java.lang.String rebisdup) {
        this.rebisdup = rebisdup;
    }


    /**
     * Gets the reccaa value for this PersonaFisicaWSValueObject.
     * 
     * @return reccaa
     */
    public java.lang.String getReccaa() {
        return reccaa;
    }


    /**
     * Sets the reccaa value for this PersonaFisicaWSValueObject.
     * 
     * @param reccaa
     */
    public void setReccaa(java.lang.String reccaa) {
        this.reccaa = reccaa;
    }


    /**
     * Gets the recopos value for this PersonaFisicaWSValueObject.
     * 
     * @return recopos
     */
    public java.lang.String getRecopos() {
        return recopos;
    }


    /**
     * Sets the recopos value for this PersonaFisicaWSValueObject.
     * 
     * @param recopos
     */
    public void setRecopos(java.lang.String recopos) {
        this.recopos = recopos;
    }


    /**
     * Gets the reescale value for this PersonaFisicaWSValueObject.
     * 
     * @return reescale
     */
    public java.lang.String getReescale() {
        return reescale;
    }


    /**
     * Sets the reescale value for this PersonaFisicaWSValueObject.
     * 
     * @param reescale
     */
    public void setReescale(java.lang.String reescale) {
        this.reescale = reescale;
    }


    /**
     * Gets the reextcodpost value for this PersonaFisicaWSValueObject.
     * 
     * @return reextcodpost
     */
    public java.lang.String getReextcodpost() {
        return reextcodpost;
    }


    /**
     * Sets the reextcodpost value for this PersonaFisicaWSValueObject.
     * 
     * @param reextcodpost
     */
    public void setReextcodpost(java.lang.String reextcodpost) {
        this.reextcodpost = reextcodpost;
    }


    /**
     * Gets the reexttexto1 value for this PersonaFisicaWSValueObject.
     * 
     * @return reexttexto1
     */
    public java.lang.String getReexttexto1() {
        return reexttexto1;
    }


    /**
     * Sets the reexttexto1 value for this PersonaFisicaWSValueObject.
     * 
     * @param reexttexto1
     */
    public void setReexttexto1(java.lang.String reexttexto1) {
        this.reexttexto1 = reexttexto1;
    }


    /**
     * Gets the reexttexto2 value for this PersonaFisicaWSValueObject.
     * 
     * @return reexttexto2
     */
    public java.lang.String getReexttexto2() {
        return reexttexto2;
    }


    /**
     * Sets the reexttexto2 value for this PersonaFisicaWSValueObject.
     * 
     * @param reexttexto2
     */
    public void setReexttexto2(java.lang.String reexttexto2) {
        this.reexttexto2 = reexttexto2;
    }


    /**
     * Gets the relepu value for this PersonaFisicaWSValueObject.
     * 
     * @return relepu
     */
    public java.lang.String getRelepu() {
        return relepu;
    }


    /**
     * Sets the relepu value for this PersonaFisicaWSValueObject.
     * 
     * @param relepu
     */
    public void setRelepu(java.lang.String relepu) {
        this.relepu = relepu;
    }


    /**
     * Gets the remuni value for this PersonaFisicaWSValueObject.
     * 
     * @return remuni
     */
    public java.lang.String getRemuni() {
        return remuni;
    }


    /**
     * Sets the remuni value for this PersonaFisicaWSValueObject.
     * 
     * @param remuni
     */
    public void setRemuni(java.lang.String remuni) {
        this.remuni = remuni;
    }


    /**
     * Gets the renovp value for this PersonaFisicaWSValueObject.
     * 
     * @return renovp
     */
    public java.lang.String getRenovp() {
        return renovp;
    }


    /**
     * Sets the renovp value for this PersonaFisicaWSValueObject.
     * 
     * @param renovp
     */
    public void setRenovp(java.lang.String renovp) {
        this.renovp = renovp;
    }


    /**
     * Gets the renut value for this PersonaFisicaWSValueObject.
     * 
     * @return renut
     */
    public java.lang.String getRenut() {
        return renut;
    }


    /**
     * Sets the renut value for this PersonaFisicaWSValueObject.
     * 
     * @param renut
     */
    public void setRenut(java.lang.String renut) {
        this.renut = renut;
    }


    /**
     * Gets the renuvp value for this PersonaFisicaWSValueObject.
     * 
     * @return renuvp
     */
    public java.lang.String getRenuvp() {
        return renuvp;
    }


    /**
     * Sets the renuvp value for this PersonaFisicaWSValueObject.
     * 
     * @param renuvp
     */
    public void setRenuvp(java.lang.String renuvp) {
        this.renuvp = renuvp;
    }


    /**
     * Gets the repais value for this PersonaFisicaWSValueObject.
     * 
     * @return repais
     */
    public java.lang.String getRepais() {
        return repais;
    }


    /**
     * Sets the repais value for this PersonaFisicaWSValueObject.
     * 
     * @param repais
     */
    public void setRepais(java.lang.String repais) {
        this.repais = repais;
    }


    /**
     * Gets the repiso value for this PersonaFisicaWSValueObject.
     * 
     * @return repiso
     */
    public java.lang.String getRepiso() {
        return repiso;
    }


    /**
     * Sets the repiso value for this PersonaFisicaWSValueObject.
     * 
     * @param repiso
     */
    public void setRepiso(java.lang.String repiso) {
        this.repiso = repiso;
    }


    /**
     * Gets the reprovin value for this PersonaFisicaWSValueObject.
     * 
     * @return reprovin
     */
    public java.lang.String getReprovin() {
        return reprovin;
    }


    /**
     * Sets the reprovin value for this PersonaFisicaWSValueObject.
     * 
     * @param reprovin
     */
    public void setReprovin(java.lang.String reprovin) {
        this.reprovin = reprovin;
    }


    /**
     * Gets the restricciones_Actividad value for this PersonaFisicaWSValueObject.
     * 
     * @return restricciones_Actividad
     */
    public java.lang.String getRestricciones_Actividad() {
        return restricciones_Actividad;
    }


    /**
     * Sets the restricciones_Actividad value for this PersonaFisicaWSValueObject.
     * 
     * @param restricciones_Actividad
     */
    public void setRestricciones_Actividad(java.lang.String restricciones_Actividad) {
        this.restricciones_Actividad = restricciones_Actividad;
    }


    /**
     * Gets the restricciones_Ocupacion value for this PersonaFisicaWSValueObject.
     * 
     * @return restricciones_Ocupacion
     */
    public java.lang.String getRestricciones_Ocupacion() {
        return restricciones_Ocupacion;
    }


    /**
     * Sets the restricciones_Ocupacion value for this PersonaFisicaWSValueObject.
     * 
     * @param restricciones_Ocupacion
     */
    public void setRestricciones_Ocupacion(java.lang.String restricciones_Ocupacion) {
        this.restricciones_Ocupacion = restricciones_Ocupacion;
    }


    /**
     * Gets the retivipu value for this PersonaFisicaWSValueObject.
     * 
     * @return retivipu
     */
    public java.lang.String getRetivipu() {
        return retivipu;
    }


    /**
     * Sets the retivipu value for this PersonaFisicaWSValueObject.
     * 
     * @param retivipu
     */
    public void setRetivipu(java.lang.String retivipu) {
        this.retivipu = retivipu;
    }


    /**
     * Gets the rgi_activa value for this PersonaFisicaWSValueObject.
     * 
     * @return rgi_activa
     */
    public java.lang.String getRgi_activa() {
        return rgi_activa;
    }


    /**
     * Sets the rgi_activa value for this PersonaFisicaWSValueObject.
     * 
     * @param rgi_activa
     */
    public void setRgi_activa(java.lang.String rgi_activa) {
        this.rgi_activa = rgi_activa;
    }


    /**
     * Gets the rgi_importe value for this PersonaFisicaWSValueObject.
     * 
     * @return rgi_importe
     */
    public java.lang.String getRgi_importe() {
        return rgi_importe;
    }


    /**
     * Sets the rgi_importe value for this PersonaFisicaWSValueObject.
     * 
     * @param rgi_importe
     */
    public void setRgi_importe(java.lang.String rgi_importe) {
        this.rgi_importe = rgi_importe;
    }


    /**
     * Gets the sexo value for this PersonaFisicaWSValueObject.
     * 
     * @return sexo
     */
    public java.lang.String getSexo() {
        return sexo;
    }


    /**
     * Sets the sexo value for this PersonaFisicaWSValueObject.
     * 
     * @param sexo
     */
    public void setSexo(java.lang.String sexo) {
        this.sexo = sexo;
    }


    /**
     * Gets the tieneIndSispe value for this PersonaFisicaWSValueObject.
     * 
     * @return tieneIndSispe
     */
    public boolean isTieneIndSispe() {
        return tieneIndSispe;
    }


    /**
     * Sets the tieneIndSispe value for this PersonaFisicaWSValueObject.
     * 
     * @param tieneIndSispe
     */
    public void setTieneIndSispe(boolean tieneIndSispe) {
        this.tieneIndSispe = tieneIndSispe;
    }


    /**
     * Gets the tipo_benef value for this PersonaFisicaWSValueObject.
     * 
     * @return tipo_benef
     */
    public java.lang.String getTipo_benef() {
        return tipo_benef;
    }


    /**
     * Sets the tipo_benef value for this PersonaFisicaWSValueObject.
     * 
     * @param tipo_benef
     */
    public void setTipo_benef(java.lang.String tipo_benef) {
        this.tipo_benef = tipo_benef;
    }


    /**
     * Gets the tipo_doc value for this PersonaFisicaWSValueObject.
     * 
     * @return tipo_doc
     */
    public java.lang.String getTipo_doc() {
        return tipo_doc;
    }


    /**
     * Sets the tipo_doc value for this PersonaFisicaWSValueObject.
     * 
     * @param tipo_doc
     */
    public void setTipo_doc(java.lang.String tipo_doc) {
        this.tipo_doc = tipo_doc;
    }


    /**
     * Gets the tlfno1 value for this PersonaFisicaWSValueObject.
     * 
     * @return tlfno1
     */
    public java.lang.String getTlfno1() {
        return tlfno1;
    }


    /**
     * Sets the tlfno1 value for this PersonaFisicaWSValueObject.
     * 
     * @param tlfno1
     */
    public void setTlfno1(java.lang.String tlfno1) {
        this.tlfno1 = tlfno1;
    }


    /**
     * Gets the tlfno2 value for this PersonaFisicaWSValueObject.
     * 
     * @return tlfno2
     */
    public java.lang.String getTlfno2() {
        return tlfno2;
    }


    /**
     * Sets the tlfno2 value for this PersonaFisicaWSValueObject.
     * 
     * @param tlfno2
     */
    public void setTlfno2(java.lang.String tlfno2) {
        this.tlfno2 = tlfno2;
    }


    /**
     * Gets the tlfno3 value for this PersonaFisicaWSValueObject.
     * 
     * @return tlfno3
     */
    public java.lang.String getTlfno3() {
        return tlfno3;
    }


    /**
     * Sets the tlfno3 value for this PersonaFisicaWSValueObject.
     * 
     * @param tlfno3
     */
    public void setTlfno3(java.lang.String tlfno3) {
        this.tlfno3 = tlfno3;
    }


    /**
     * Gets the tlfno4 value for this PersonaFisicaWSValueObject.
     * 
     * @return tlfno4
     */
    public java.lang.String getTlfno4() {
        return tlfno4;
    }


    /**
     * Sets the tlfno4 value for this PersonaFisicaWSValueObject.
     * 
     * @param tlfno4
     */
    public void setTlfno4(java.lang.String tlfno4) {
        this.tlfno4 = tlfno4;
    }


    /**
     * Gets the tlfno_notif value for this PersonaFisicaWSValueObject.
     * 
     * @return tlfno_notif
     */
    public java.lang.String getTlfno_notif() {
        return tlfno_notif;
    }


    /**
     * Sets the tlfno_notif value for this PersonaFisicaWSValueObject.
     * 
     * @param tlfno_notif
     */
    public void setTlfno_notif(java.lang.String tlfno_notif) {
        this.tlfno_notif = tlfno_notif;
    }


    /**
     * Gets the ultAgr value for this PersonaFisicaWSValueObject.
     * 
     * @return ultAgr
     */
    public java.lang.Integer getUltAgr() {
        return ultAgr;
    }


    /**
     * Sets the ultAgr value for this PersonaFisicaWSValueObject.
     * 
     * @param ultAgr
     */
    public void setUltAgr(java.lang.Integer ultAgr) {
        this.ultAgr = ultAgr;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PersonaFisicaWSValueObject)) return false;
        PersonaFisicaWSValueObject other = (PersonaFisicaWSValueObject) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.ape1Reconocido==null && other.getApe1Reconocido()==null) || 
             (this.ape1Reconocido!=null &&
              this.ape1Reconocido.equals(other.getApe1Reconocido()))) &&
            ((this.ape2Reconocido==null && other.getApe2Reconocido()==null) || 
             (this.ape2Reconocido!=null &&
              this.ape2Reconocido.equals(other.getApe2Reconocido()))) &&
            ((this.apellido1==null && other.getApellido1()==null) || 
             (this.apellido1!=null &&
              this.apellido1.equals(other.getApellido1()))) &&
            ((this.apellido2==null && other.getApellido2()==null) || 
             (this.apellido2!=null &&
              this.apellido2.equals(other.getApellido2()))) &&
            ((this.bloqAgr==null && other.getBloqAgr()==null) || 
             (this.bloqAgr!=null &&
              this.bloqAgr.equals(other.getBloqAgr()))) &&
            ((this.cert_poli==null && other.getCert_poli()==null) || 
             (this.cert_poli!=null &&
              this.cert_poli.equals(other.getCert_poli()))) &&
            ((this.cert_ssss==null && other.getCert_ssss()==null) || 
             (this.cert_ssss!=null &&
              this.cert_ssss.equals(other.getCert_ssss()))) &&
            ((this.cod_ambitoComarca==null && other.getCod_ambitoComarca()==null) || 
             (this.cod_ambitoComarca!=null &&
              this.cod_ambitoComarca.equals(other.getCod_ambitoComarca()))) &&
            ((this.cod_ambitoMunicipio==null && other.getCod_ambitoMunicipio()==null) || 
             (this.cod_ambitoMunicipio!=null &&
              this.cod_ambitoMunicipio.equals(other.getCod_ambitoMunicipio()))) &&
            ((this.cod_ambitoProv==null && other.getCod_ambitoProv()==null) || 
             (this.cod_ambitoProv!=null &&
              this.cod_ambitoProv.equals(other.getCod_ambitoProv()))) &&
            ((this.cod_ambitoccaa==null && other.getCod_ambitoccaa()==null) || 
             (this.cod_ambitoccaa!=null &&
              this.cod_ambitoccaa.equals(other.getCod_ambitoccaa()))) &&
            ((this.cod_ambitoisla==null && other.getCod_ambitoisla()==null) || 
             (this.cod_ambitoisla!=null &&
              this.cod_ambitoisla.equals(other.getCod_ambitoisla()))) &&
            ((this.cod_tipo_autoriz_admin==null && other.getCod_tipo_autoriz_admin()==null) || 
             (this.cod_tipo_autoriz_admin!=null &&
              this.cod_tipo_autoriz_admin.equals(other.getCod_tipo_autoriz_admin()))) &&
            ((this.condicionesEspeciales==null && other.getCondicionesEspeciales()==null) || 
             (this.condicionesEspeciales!=null &&
              java.util.Arrays.equals(this.condicionesEspeciales, other.getCondicionesEspeciales()))) &&
            ((this.corr==null && other.getCorr()==null) || 
             (this.corr!=null &&
              this.corr.equals(other.getCorr()))) &&
            ((this.correlec==null && other.getCorrelec()==null) || 
             (this.correlec!=null &&
              this.correlec.equals(other.getCorrelec()))) &&
            ((this.cpApdoCorreos==null && other.getCpApdoCorreos()==null) || 
             (this.cpApdoCorreos!=null &&
              this.cpApdoCorreos.equals(other.getCpApdoCorreos()))) &&
            ((this.demandConVoluntad==null && other.getDemandConVoluntad()==null) || 
             (this.demandConVoluntad!=null &&
              this.demandConVoluntad.equals(other.getDemandConVoluntad()))) &&
            ((this.demandanteUE==null && other.getDemandanteUE()==null) || 
             (this.demandanteUE!=null &&
              this.demandanteUE.equals(other.getDemandanteUE()))) &&
            ((this.desc_notmuni_c==null && other.getDesc_notmuni_c()==null) || 
             (this.desc_notmuni_c!=null &&
              this.desc_notmuni_c.equals(other.getDesc_notmuni_c()))) &&
            ((this.desc_notmuni_e==null && other.getDesc_notmuni_e()==null) || 
             (this.desc_notmuni_e!=null &&
              this.desc_notmuni_e.equals(other.getDesc_notmuni_e()))) &&
            ((this.desc_notprov_c==null && other.getDesc_notprov_c()==null) || 
             (this.desc_notprov_c!=null &&
              this.desc_notprov_c.equals(other.getDesc_notprov_c()))) &&
            ((this.desc_notprov_e==null && other.getDesc_notprov_e()==null) || 
             (this.desc_notprov_e!=null &&
              this.desc_notprov_e.equals(other.getDesc_notprov_e()))) &&
            ((this.desc_remuni_c==null && other.getDesc_remuni_c()==null) || 
             (this.desc_remuni_c!=null &&
              this.desc_remuni_c.equals(other.getDesc_remuni_c()))) &&
            ((this.desc_remuni_e==null && other.getDesc_remuni_e()==null) || 
             (this.desc_remuni_e!=null &&
              this.desc_remuni_e.equals(other.getDesc_remuni_e()))) &&
            ((this.desc_reprov_c==null && other.getDesc_reprov_c()==null) || 
             (this.desc_reprov_c!=null &&
              this.desc_reprov_c.equals(other.getDesc_reprov_c()))) &&
            ((this.desc_reprov_e==null && other.getDesc_reprov_e()==null) || 
             (this.desc_reprov_e!=null &&
              this.desc_reprov_e.equals(other.getDesc_reprov_e()))) &&
            ((this.doc_incompl==null && other.getDoc_incompl()==null) || 
             (this.doc_incompl!=null &&
              this.doc_incompl.equals(other.getDoc_incompl()))) &&
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            this.existeLanbide == other.isExisteLanbide() &&
            this.existeSISPE == other.isExisteSISPE() &&
            ((this.fax==null && other.getFax()==null) || 
             (this.fax!=null &&
              this.fax.equals(other.getFax()))) &&
            ((this.fecConf==null && other.getFecConf()==null) || 
             (this.fecConf!=null &&
              this.fecConf.equals(other.getFecConf()))) &&
            ((this.fecResolReconocido==null && other.getFecResolReconocido()==null) || 
             (this.fecResolReconocido!=null &&
              this.fecResolReconocido.equals(other.getFecResolReconocido()))) &&
            ((this.fec_fin_min==null && other.getFec_fin_min()==null) || 
             (this.fec_fin_min!=null &&
              this.fec_fin_min.equals(other.getFec_fin_min()))) &&
            ((this.fec_fin_vigencia==null && other.getFec_fin_vigencia()==null) || 
             (this.fec_fin_vigencia!=null &&
              this.fec_fin_vigencia.equals(other.getFec_fin_vigencia()))) &&
            ((this.fec_ini_min==null && other.getFec_ini_min()==null) || 
             (this.fec_ini_min!=null &&
              this.fec_ini_min.equals(other.getFec_ini_min()))) &&
            ((this.fec_sol_comunic_ren==null && other.getFec_sol_comunic_ren()==null) || 
             (this.fec_sol_comunic_ren!=null &&
              this.fec_sol_comunic_ren.equals(other.getFec_sol_comunic_ren()))) &&
            ((this.fecha_nac==null && other.getFecha_nac()==null) || 
             (this.fecha_nac!=null &&
              this.fecha_nac.equals(other.getFecha_nac()))) &&
            ((this.generoReconocido==null && other.getGeneroReconocido()==null) || 
             (this.generoReconocido!=null &&
              this.generoReconocido.equals(other.getGeneroReconocido()))) &&
            ((this.grado_min==null && other.getGrado_min()==null) || 
             (this.grado_min!=null &&
              this.grado_min.equals(other.getGrado_min()))) &&
            ((this.histor==null && other.getHistor()==null) || 
             (this.histor!=null &&
              this.histor.equals(other.getHistor()))) &&
            ((this.indBloq==null && other.getIndBloq()==null) || 
             (this.indBloq!=null &&
              this.indBloq.equals(other.getIndBloq()))) &&
            ((this.indConf==null && other.getIndConf()==null) || 
             (this.indConf!=null &&
              this.indConf.equals(other.getIndConf()))) &&
            ((this.indSispe==null && other.getIndSispe()==null) || 
             (this.indSispe!=null &&
              this.indSispe.equals(other.getIndSispe()))) &&
            ((this.inter==null && other.getInter()==null) || 
             (this.inter!=null &&
              this.inter.equals(other.getInter()))) &&
            ((this.intermedia==null && other.getIntermedia()==null) || 
             (this.intermedia!=null &&
              this.intermedia.equals(other.getIntermedia()))) &&
            ((this.lista_errores==null && other.getLista_errores()==null) || 
             (this.lista_errores!=null &&
              this.lista_errores.equals(other.getLista_errores()))) &&
            ((this.muniApdoCorreos==null && other.getMuniApdoCorreos()==null) || 
             (this.muniApdoCorreos!=null &&
              this.muniApdoCorreos.equals(other.getMuniApdoCorreos()))) &&
            ((this.nacion==null && other.getNacion()==null) || 
             (this.nacion!=null &&
              this.nacion.equals(other.getNacion()))) &&
            ((this.nivfor==null && other.getNivfor()==null) || 
             (this.nivfor!=null &&
              this.nivfor.equals(other.getNivfor()))) &&
            ((this.noapartcorreos==null && other.getNoapartcorreos()==null) || 
             (this.noapartcorreos!=null &&
              this.noapartcorreos.equals(other.getNoapartcorreos()))) &&
            ((this.nobisdup==null && other.getNobisdup()==null) || 
             (this.nobisdup!=null &&
              this.nobisdup.equals(other.getNobisdup()))) &&
            ((this.nocopos==null && other.getNocopos()==null) || 
             (this.nocopos!=null &&
              this.nocopos.equals(other.getNocopos()))) &&
            ((this.noescale==null && other.getNoescale()==null) || 
             (this.noescale!=null &&
              this.noescale.equals(other.getNoescale()))) &&
            ((this.noextcodpost==null && other.getNoextcodpost()==null) || 
             (this.noextcodpost!=null &&
              this.noextcodpost.equals(other.getNoextcodpost()))) &&
            ((this.noexttexto1==null && other.getNoexttexto1()==null) || 
             (this.noexttexto1!=null &&
              this.noexttexto1.equals(other.getNoexttexto1()))) &&
            ((this.noexttexto2==null && other.getNoexttexto2()==null) || 
             (this.noexttexto2!=null &&
              this.noexttexto2.equals(other.getNoexttexto2()))) &&
            ((this.nolepu==null && other.getNolepu()==null) || 
             (this.nolepu!=null &&
              this.nolepu.equals(other.getNolepu()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.nombreReconocido==null && other.getNombreReconocido()==null) || 
             (this.nombreReconocido!=null &&
              this.nombreReconocido.equals(other.getNombreReconocido()))) &&
            ((this.nomuni==null && other.getNomuni()==null) || 
             (this.nomuni!=null &&
              this.nomuni.equals(other.getNomuni()))) &&
            ((this.nonovp==null && other.getNonovp()==null) || 
             (this.nonovp!=null &&
              this.nonovp.equals(other.getNonovp()))) &&
            ((this.nonuvp==null && other.getNonuvp()==null) || 
             (this.nonuvp!=null &&
              this.nonuvp.equals(other.getNonuvp()))) &&
            ((this.nopais==null && other.getNopais()==null) || 
             (this.nopais!=null &&
              this.nopais.equals(other.getNopais()))) &&
            ((this.nopiso==null && other.getNopiso()==null) || 
             (this.nopiso!=null &&
              this.nopiso.equals(other.getNopiso()))) &&
            ((this.noprovin==null && other.getNoprovin()==null) || 
             (this.noprovin!=null &&
              this.noprovin.equals(other.getNoprovin()))) &&
            ((this.notivipu==null && other.getNotivipu()==null) || 
             (this.notivipu!=null &&
              this.notivipu.equals(other.getNotivipu()))) &&
            ((this.num_doc==null && other.getNum_doc()==null) || 
             (this.num_doc!=null &&
              this.num_doc.equals(other.getNum_doc()))) &&
            ((this.num_exp==null && other.getNum_exp()==null) || 
             (this.num_exp!=null &&
              this.num_exp.equals(other.getNum_exp()))) &&
            ((this.num_exp_act==null && other.getNum_exp_act()==null) || 
             (this.num_exp_act!=null &&
              this.num_exp_act.equals(other.getNum_exp_act()))) &&
            ((this.num_ssss==null && other.getNum_ssss()==null) || 
             (this.num_ssss!=null &&
              this.num_ssss.equals(other.getNum_ssss()))) &&
            ((this.pais_fecha_fin==null && other.getPais_fecha_fin()==null) || 
             (this.pais_fecha_fin!=null &&
              this.pais_fecha_fin.equals(other.getPais_fecha_fin()))) &&
            ((this.pais_marca_eee==null && other.getPais_marca_eee()==null) || 
             (this.pais_marca_eee!=null &&
              this.pais_marca_eee.equals(other.getPais_marca_eee()))) &&
            ((this.pasaporte==null && other.getPasaporte()==null) || 
             (this.pasaporte!=null &&
              this.pasaporte.equals(other.getPasaporte()))) &&
            ((this.rebisdup==null && other.getRebisdup()==null) || 
             (this.rebisdup!=null &&
              this.rebisdup.equals(other.getRebisdup()))) &&
            ((this.reccaa==null && other.getReccaa()==null) || 
             (this.reccaa!=null &&
              this.reccaa.equals(other.getReccaa()))) &&
            ((this.recopos==null && other.getRecopos()==null) || 
             (this.recopos!=null &&
              this.recopos.equals(other.getRecopos()))) &&
            ((this.reescale==null && other.getReescale()==null) || 
             (this.reescale!=null &&
              this.reescale.equals(other.getReescale()))) &&
            ((this.reextcodpost==null && other.getReextcodpost()==null) || 
             (this.reextcodpost!=null &&
              this.reextcodpost.equals(other.getReextcodpost()))) &&
            ((this.reexttexto1==null && other.getReexttexto1()==null) || 
             (this.reexttexto1!=null &&
              this.reexttexto1.equals(other.getReexttexto1()))) &&
            ((this.reexttexto2==null && other.getReexttexto2()==null) || 
             (this.reexttexto2!=null &&
              this.reexttexto2.equals(other.getReexttexto2()))) &&
            ((this.relepu==null && other.getRelepu()==null) || 
             (this.relepu!=null &&
              this.relepu.equals(other.getRelepu()))) &&
            ((this.remuni==null && other.getRemuni()==null) || 
             (this.remuni!=null &&
              this.remuni.equals(other.getRemuni()))) &&
            ((this.renovp==null && other.getRenovp()==null) || 
             (this.renovp!=null &&
              this.renovp.equals(other.getRenovp()))) &&
            ((this.renut==null && other.getRenut()==null) || 
             (this.renut!=null &&
              this.renut.equals(other.getRenut()))) &&
            ((this.renuvp==null && other.getRenuvp()==null) || 
             (this.renuvp!=null &&
              this.renuvp.equals(other.getRenuvp()))) &&
            ((this.repais==null && other.getRepais()==null) || 
             (this.repais!=null &&
              this.repais.equals(other.getRepais()))) &&
            ((this.repiso==null && other.getRepiso()==null) || 
             (this.repiso!=null &&
              this.repiso.equals(other.getRepiso()))) &&
            ((this.reprovin==null && other.getReprovin()==null) || 
             (this.reprovin!=null &&
              this.reprovin.equals(other.getReprovin()))) &&
            ((this.restricciones_Actividad==null && other.getRestricciones_Actividad()==null) || 
             (this.restricciones_Actividad!=null &&
              this.restricciones_Actividad.equals(other.getRestricciones_Actividad()))) &&
            ((this.restricciones_Ocupacion==null && other.getRestricciones_Ocupacion()==null) || 
             (this.restricciones_Ocupacion!=null &&
              this.restricciones_Ocupacion.equals(other.getRestricciones_Ocupacion()))) &&
            ((this.retivipu==null && other.getRetivipu()==null) || 
             (this.retivipu!=null &&
              this.retivipu.equals(other.getRetivipu()))) &&
            ((this.rgi_activa==null && other.getRgi_activa()==null) || 
             (this.rgi_activa!=null &&
              this.rgi_activa.equals(other.getRgi_activa()))) &&
            ((this.rgi_importe==null && other.getRgi_importe()==null) || 
             (this.rgi_importe!=null &&
              this.rgi_importe.equals(other.getRgi_importe()))) &&
            ((this.sexo==null && other.getSexo()==null) || 
             (this.sexo!=null &&
              this.sexo.equals(other.getSexo()))) &&
            this.tieneIndSispe == other.isTieneIndSispe() &&
            ((this.tipo_benef==null && other.getTipo_benef()==null) || 
             (this.tipo_benef!=null &&
              this.tipo_benef.equals(other.getTipo_benef()))) &&
            ((this.tipo_doc==null && other.getTipo_doc()==null) || 
             (this.tipo_doc!=null &&
              this.tipo_doc.equals(other.getTipo_doc()))) &&
            ((this.tlfno1==null && other.getTlfno1()==null) || 
             (this.tlfno1!=null &&
              this.tlfno1.equals(other.getTlfno1()))) &&
            ((this.tlfno2==null && other.getTlfno2()==null) || 
             (this.tlfno2!=null &&
              this.tlfno2.equals(other.getTlfno2()))) &&
            ((this.tlfno3==null && other.getTlfno3()==null) || 
             (this.tlfno3!=null &&
              this.tlfno3.equals(other.getTlfno3()))) &&
            ((this.tlfno4==null && other.getTlfno4()==null) || 
             (this.tlfno4!=null &&
              this.tlfno4.equals(other.getTlfno4()))) &&
            ((this.tlfno_notif==null && other.getTlfno_notif()==null) || 
             (this.tlfno_notif!=null &&
              this.tlfno_notif.equals(other.getTlfno_notif()))) &&
            ((this.ultAgr==null && other.getUltAgr()==null) || 
             (this.ultAgr!=null &&
              this.ultAgr.equals(other.getUltAgr())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getApe1Reconocido() != null) {
            _hashCode += getApe1Reconocido().hashCode();
        }
        if (getApe2Reconocido() != null) {
            _hashCode += getApe2Reconocido().hashCode();
        }
        if (getApellido1() != null) {
            _hashCode += getApellido1().hashCode();
        }
        if (getApellido2() != null) {
            _hashCode += getApellido2().hashCode();
        }
        if (getBloqAgr() != null) {
            _hashCode += getBloqAgr().hashCode();
        }
        if (getCert_poli() != null) {
            _hashCode += getCert_poli().hashCode();
        }
        if (getCert_ssss() != null) {
            _hashCode += getCert_ssss().hashCode();
        }
        if (getCod_ambitoComarca() != null) {
            _hashCode += getCod_ambitoComarca().hashCode();
        }
        if (getCod_ambitoMunicipio() != null) {
            _hashCode += getCod_ambitoMunicipio().hashCode();
        }
        if (getCod_ambitoProv() != null) {
            _hashCode += getCod_ambitoProv().hashCode();
        }
        if (getCod_ambitoccaa() != null) {
            _hashCode += getCod_ambitoccaa().hashCode();
        }
        if (getCod_ambitoisla() != null) {
            _hashCode += getCod_ambitoisla().hashCode();
        }
        if (getCod_tipo_autoriz_admin() != null) {
            _hashCode += getCod_tipo_autoriz_admin().hashCode();
        }
        if (getCondicionesEspeciales() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCondicionesEspeciales());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCondicionesEspeciales(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCorr() != null) {
            _hashCode += getCorr().hashCode();
        }
        if (getCorrelec() != null) {
            _hashCode += getCorrelec().hashCode();
        }
        if (getCpApdoCorreos() != null) {
            _hashCode += getCpApdoCorreos().hashCode();
        }
        if (getDemandConVoluntad() != null) {
            _hashCode += getDemandConVoluntad().hashCode();
        }
        if (getDemandanteUE() != null) {
            _hashCode += getDemandanteUE().hashCode();
        }
        if (getDesc_notmuni_c() != null) {
            _hashCode += getDesc_notmuni_c().hashCode();
        }
        if (getDesc_notmuni_e() != null) {
            _hashCode += getDesc_notmuni_e().hashCode();
        }
        if (getDesc_notprov_c() != null) {
            _hashCode += getDesc_notprov_c().hashCode();
        }
        if (getDesc_notprov_e() != null) {
            _hashCode += getDesc_notprov_e().hashCode();
        }
        if (getDesc_remuni_c() != null) {
            _hashCode += getDesc_remuni_c().hashCode();
        }
        if (getDesc_remuni_e() != null) {
            _hashCode += getDesc_remuni_e().hashCode();
        }
        if (getDesc_reprov_c() != null) {
            _hashCode += getDesc_reprov_c().hashCode();
        }
        if (getDesc_reprov_e() != null) {
            _hashCode += getDesc_reprov_e().hashCode();
        }
        if (getDoc_incompl() != null) {
            _hashCode += getDoc_incompl().hashCode();
        }
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        _hashCode += (isExisteLanbide() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isExisteSISPE() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getFax() != null) {
            _hashCode += getFax().hashCode();
        }
        if (getFecConf() != null) {
            _hashCode += getFecConf().hashCode();
        }
        if (getFecResolReconocido() != null) {
            _hashCode += getFecResolReconocido().hashCode();
        }
        if (getFec_fin_min() != null) {
            _hashCode += getFec_fin_min().hashCode();
        }
        if (getFec_fin_vigencia() != null) {
            _hashCode += getFec_fin_vigencia().hashCode();
        }
        if (getFec_ini_min() != null) {
            _hashCode += getFec_ini_min().hashCode();
        }
        if (getFec_sol_comunic_ren() != null) {
            _hashCode += getFec_sol_comunic_ren().hashCode();
        }
        if (getFecha_nac() != null) {
            _hashCode += getFecha_nac().hashCode();
        }
        if (getGeneroReconocido() != null) {
            _hashCode += getGeneroReconocido().hashCode();
        }
        if (getGrado_min() != null) {
            _hashCode += getGrado_min().hashCode();
        }
        if (getHistor() != null) {
            _hashCode += getHistor().hashCode();
        }
        if (getIndBloq() != null) {
            _hashCode += getIndBloq().hashCode();
        }
        if (getIndConf() != null) {
            _hashCode += getIndConf().hashCode();
        }
        if (getIndSispe() != null) {
            _hashCode += getIndSispe().hashCode();
        }
        if (getInter() != null) {
            _hashCode += getInter().hashCode();
        }
        if (getIntermedia() != null) {
            _hashCode += getIntermedia().hashCode();
        }
        if (getLista_errores() != null) {
            _hashCode += getLista_errores().hashCode();
        }
        if (getMuniApdoCorreos() != null) {
            _hashCode += getMuniApdoCorreos().hashCode();
        }
        if (getNacion() != null) {
            _hashCode += getNacion().hashCode();
        }
        if (getNivfor() != null) {
            _hashCode += getNivfor().hashCode();
        }
        if (getNoapartcorreos() != null) {
            _hashCode += getNoapartcorreos().hashCode();
        }
        if (getNobisdup() != null) {
            _hashCode += getNobisdup().hashCode();
        }
        if (getNocopos() != null) {
            _hashCode += getNocopos().hashCode();
        }
        if (getNoescale() != null) {
            _hashCode += getNoescale().hashCode();
        }
        if (getNoextcodpost() != null) {
            _hashCode += getNoextcodpost().hashCode();
        }
        if (getNoexttexto1() != null) {
            _hashCode += getNoexttexto1().hashCode();
        }
        if (getNoexttexto2() != null) {
            _hashCode += getNoexttexto2().hashCode();
        }
        if (getNolepu() != null) {
            _hashCode += getNolepu().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getNombreReconocido() != null) {
            _hashCode += getNombreReconocido().hashCode();
        }
        if (getNomuni() != null) {
            _hashCode += getNomuni().hashCode();
        }
        if (getNonovp() != null) {
            _hashCode += getNonovp().hashCode();
        }
        if (getNonuvp() != null) {
            _hashCode += getNonuvp().hashCode();
        }
        if (getNopais() != null) {
            _hashCode += getNopais().hashCode();
        }
        if (getNopiso() != null) {
            _hashCode += getNopiso().hashCode();
        }
        if (getNoprovin() != null) {
            _hashCode += getNoprovin().hashCode();
        }
        if (getNotivipu() != null) {
            _hashCode += getNotivipu().hashCode();
        }
        if (getNum_doc() != null) {
            _hashCode += getNum_doc().hashCode();
        }
        if (getNum_exp() != null) {
            _hashCode += getNum_exp().hashCode();
        }
        if (getNum_exp_act() != null) {
            _hashCode += getNum_exp_act().hashCode();
        }
        if (getNum_ssss() != null) {
            _hashCode += getNum_ssss().hashCode();
        }
        if (getPais_fecha_fin() != null) {
            _hashCode += getPais_fecha_fin().hashCode();
        }
        if (getPais_marca_eee() != null) {
            _hashCode += getPais_marca_eee().hashCode();
        }
        if (getPasaporte() != null) {
            _hashCode += getPasaporte().hashCode();
        }
        if (getRebisdup() != null) {
            _hashCode += getRebisdup().hashCode();
        }
        if (getReccaa() != null) {
            _hashCode += getReccaa().hashCode();
        }
        if (getRecopos() != null) {
            _hashCode += getRecopos().hashCode();
        }
        if (getReescale() != null) {
            _hashCode += getReescale().hashCode();
        }
        if (getReextcodpost() != null) {
            _hashCode += getReextcodpost().hashCode();
        }
        if (getReexttexto1() != null) {
            _hashCode += getReexttexto1().hashCode();
        }
        if (getReexttexto2() != null) {
            _hashCode += getReexttexto2().hashCode();
        }
        if (getRelepu() != null) {
            _hashCode += getRelepu().hashCode();
        }
        if (getRemuni() != null) {
            _hashCode += getRemuni().hashCode();
        }
        if (getRenovp() != null) {
            _hashCode += getRenovp().hashCode();
        }
        if (getRenut() != null) {
            _hashCode += getRenut().hashCode();
        }
        if (getRenuvp() != null) {
            _hashCode += getRenuvp().hashCode();
        }
        if (getRepais() != null) {
            _hashCode += getRepais().hashCode();
        }
        if (getRepiso() != null) {
            _hashCode += getRepiso().hashCode();
        }
        if (getReprovin() != null) {
            _hashCode += getReprovin().hashCode();
        }
        if (getRestricciones_Actividad() != null) {
            _hashCode += getRestricciones_Actividad().hashCode();
        }
        if (getRestricciones_Ocupacion() != null) {
            _hashCode += getRestricciones_Ocupacion().hashCode();
        }
        if (getRetivipu() != null) {
            _hashCode += getRetivipu().hashCode();
        }
        if (getRgi_activa() != null) {
            _hashCode += getRgi_activa().hashCode();
        }
        if (getRgi_importe() != null) {
            _hashCode += getRgi_importe().hashCode();
        }
        if (getSexo() != null) {
            _hashCode += getSexo().hashCode();
        }
        _hashCode += (isTieneIndSispe() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getTipo_benef() != null) {
            _hashCode += getTipo_benef().hashCode();
        }
        if (getTipo_doc() != null) {
            _hashCode += getTipo_doc().hashCode();
        }
        if (getTlfno1() != null) {
            _hashCode += getTlfno1().hashCode();
        }
        if (getTlfno2() != null) {
            _hashCode += getTlfno2().hashCode();
        }
        if (getTlfno3() != null) {
            _hashCode += getTlfno3().hashCode();
        }
        if (getTlfno4() != null) {
            _hashCode += getTlfno4().hashCode();
        }
        if (getTlfno_notif() != null) {
            _hashCode += getTlfno_notif().hashCode();
        }
        if (getUltAgr() != null) {
            _hashCode += getUltAgr().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PersonaFisicaWSValueObject.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langai.altia.es/business/personafisica", "PersonaFisicaWSValueObject"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ape1Reconocido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ape1Reconocido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ape2Reconocido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ape2Reconocido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellido1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apellido1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellido2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apellido2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bloqAgr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bloqAgr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cert_poli");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cert_poli"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cert_ssss");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cert_ssss"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_ambitoComarca");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_ambitoComarca"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_ambitoMunicipio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_ambitoMunicipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_ambitoProv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_ambitoProv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_ambitoccaa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_ambitoccaa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_ambitoisla");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_ambitoisla"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_tipo_autoriz_admin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_tipo_autoriz_admin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("condicionesEspeciales");
        elemField.setXmlName(new javax.xml.namespace.QName("", "condicionesEspeciales"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("corr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "corr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correlec");
        elemField.setXmlName(new javax.xml.namespace.QName("", "correlec"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cpApdoCorreos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cpApdoCorreos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("demandConVoluntad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "demandConVoluntad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("demandanteUE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "demandanteUE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_notmuni_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_notmuni_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_notmuni_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_notmuni_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_notprov_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_notprov_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_notprov_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_notprov_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_remuni_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_remuni_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_remuni_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_remuni_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_reprov_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_reprov_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_reprov_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_reprov_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("doc_incompl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "doc_incompl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("existeLanbide");
        elemField.setXmlName(new javax.xml.namespace.QName("", "existeLanbide"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("existeSISPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "existeSISPE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fax");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecConf");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecConf"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecResolReconocido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecResolReconocido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fec_fin_min");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fec_fin_min"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fec_fin_vigencia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fec_fin_vigencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fec_ini_min");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fec_ini_min"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fec_sol_comunic_ren");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fec_sol_comunic_ren"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_nac");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_nac"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generoReconocido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "generoReconocido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grado_min");
        elemField.setXmlName(new javax.xml.namespace.QName("", "grado_min"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("histor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "histor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indBloq");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indBloq"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indConf");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indConf"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indSispe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indSispe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inter");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("intermedia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "intermedia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lista_errores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lista_errores"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("muniApdoCorreos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "muniApdoCorreos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nivfor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nivfor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noapartcorreos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noapartcorreos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nobisdup");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nobisdup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nocopos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nocopos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noescale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noescale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noextcodpost");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noextcodpost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noexttexto1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noexttexto1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noexttexto2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noexttexto2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nolepu");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nolepu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreReconocido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreReconocido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomuni");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomuni"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nonovp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nonovp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nonuvp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nonuvp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nopais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nopais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nopiso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nopiso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noprovin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noprovin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notivipu");
        elemField.setXmlName(new javax.xml.namespace.QName("", "notivipu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_doc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_doc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_exp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_exp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_exp_act");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_exp_act"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_ssss");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_ssss"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pais_fecha_fin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pais_fecha_fin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pais_marca_eee");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pais_marca_eee"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pasaporte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pasaporte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rebisdup");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rebisdup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reccaa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reccaa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recopos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "recopos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reescale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reescale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reextcodpost");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reextcodpost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reexttexto1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reexttexto1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reexttexto2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reexttexto2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relepu");
        elemField.setXmlName(new javax.xml.namespace.QName("", "relepu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("remuni");
        elemField.setXmlName(new javax.xml.namespace.QName("", "remuni"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("renovp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "renovp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("renut");
        elemField.setXmlName(new javax.xml.namespace.QName("", "renut"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("renuvp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "renuvp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("repais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "repais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("repiso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "repiso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reprovin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reprovin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("restricciones_Actividad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "restricciones_Actividad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("restricciones_Ocupacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "restricciones_Ocupacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retivipu");
        elemField.setXmlName(new javax.xml.namespace.QName("", "retivipu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rgi_activa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rgi_activa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rgi_importe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rgi_importe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sexo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sexo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tieneIndSispe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tieneIndSispe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo_benef");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo_benef"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo_doc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo_doc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tlfno1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tlfno1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tlfno2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tlfno2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tlfno3");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tlfno3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tlfno4");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tlfno4"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tlfno_notif");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tlfno_notif"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ultAgr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ultAgr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
