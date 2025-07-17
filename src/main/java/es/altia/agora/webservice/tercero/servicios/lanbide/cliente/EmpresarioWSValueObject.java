/**
 * EmpresarioWSValueObject.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.lanbide.cliente;

public class EmpresarioWSValueObject  extends es.altia.agora.webservice.tercero.servicios.lanbide.cliente.ValueObject  implements java.io.Serializable {
    private java.lang.String apellido1;

    private java.lang.String apellido1_contacto;

    private java.lang.String apellido1_contacto_rs;

    private java.lang.String apellido2;

    private java.lang.String apellido2_contacto;

    private java.lang.String apellido2_contacto_rs;

    private java.lang.String bis_duplicado;

    private java.lang.String bis_duplicado_ett;

    private java.lang.String bis_duplicado_rs;

    private java.lang.String cod_municipio;

    private java.lang.String cod_municipio_ett;

    private java.lang.String cod_municipio_rs;

    private java.lang.String cod_provincia;

    private java.lang.String cod_provincia_ett;

    private java.lang.String cod_provincia_rs;

    private java.lang.String cod_tipo_empresa;

    private java.lang.String cod_tipovia;

    private java.lang.String cod_tipovia_ett;

    private java.lang.String cod_tipovia_rs;

    private java.lang.String contraste_datos;

    private java.lang.String cp;

    private java.lang.String cp_ett;

    private java.lang.String cp_ext_loc;

    private java.lang.String cp_ext_rs;

    private java.lang.String cp_rs;

    private java.lang.String cuentaCot;

    private java.lang.String desc_muni_c;

    private java.lang.String desc_muni_e;

    private java.lang.String desc_muni_ett_c;

    private java.lang.String desc_muni_ett_e;

    private java.lang.String desc_muni_rs_c;

    private java.lang.String desc_muni_rs_e;

    private java.lang.String desc_prov_c;

    private java.lang.String desc_prov_e;

    private java.lang.String desc_prov_ett_c;

    private java.lang.String desc_prov_ett_e;

    private java.lang.String desc_prov_rs_c;

    private java.lang.String desc_prov_rs_e;

    private java.lang.String direc_ext_loc;

    private java.lang.String direc_ext_rs;

    private java.lang.String e_mail;

    private java.lang.String e_mail_rs;

    private java.lang.Long emp_corr;

    private java.lang.String escalera;

    private java.lang.String escalera_ett;

    private java.lang.String escalera_rs;

    private java.lang.String existe;

    private boolean existeSISPE;

    private java.lang.String existe_ett;

    private java.lang.String fax;

    private java.lang.String fax_rs;

    private java.util.Calendar fecCalif;

    private java.util.Calendar fecDesac;

    private java.lang.String fecha_fin_autorizacion;

    private java.lang.String fecha_inicio_autorizacion;

    private java.util.Calendar fecha_ultima_modificacion;

    private java.util.Calendar gen_emp_fec_regprov;

    private java.lang.String gen_emp_ind_impsepe;

    private java.lang.String gen_emp_ind_prov;

    private java.lang.Integer indBloq;

    private java.lang.Integer indSispe;

    private java.lang.Object[] lista_errores;

    private java.util.Vector lista_ett;

    private java.lang.String n_via;

    private java.lang.String n_via_ett;

    private java.lang.String n_via_rs;

    private java.lang.String nombre;

    private java.lang.String nombre_comercial;

    private java.lang.String nombre_contacto;

    private java.lang.String nombre_contacto_rs;

    private java.lang.String nombre_via;

    private java.lang.String nombre_via_ett;

    private java.lang.String nombre_via_rs;

    private java.lang.String num_autorizacion;

    private java.lang.String num_doc;

    private java.lang.String nut_ext_loc;

    private java.lang.String nut_ext_rs;

    private java.lang.String pais;

    private java.lang.String pais_ext_loc;

    private java.lang.String pais_ext_rs;

    private java.lang.String piso;

    private java.lang.String piso_ett;

    private java.lang.String piso_rs;

    private java.lang.String pobl_ext_loc;

    private java.lang.String pobl_ext_rs;

    private java.lang.String puerta;

    private java.lang.String puerta_ett;

    private java.lang.String puerta_rs;

    private java.lang.String razon_social;

    private java.lang.String telefono;

    private java.lang.String telefono_rs;

    private java.lang.String tipo_empresa;

    private java.lang.String titularidad;

    public EmpresarioWSValueObject() {
    }

    public EmpresarioWSValueObject(
           es.altia.agora.webservice.tercero.servicios.lanbide.cliente.AuditoriaValueObject auditoria,
           long objectId,
           java.lang.String apellido1,
           java.lang.String apellido1_contacto,
           java.lang.String apellido1_contacto_rs,
           java.lang.String apellido2,
           java.lang.String apellido2_contacto,
           java.lang.String apellido2_contacto_rs,
           java.lang.String bis_duplicado,
           java.lang.String bis_duplicado_ett,
           java.lang.String bis_duplicado_rs,
           java.lang.String cod_municipio,
           java.lang.String cod_municipio_ett,
           java.lang.String cod_municipio_rs,
           java.lang.String cod_provincia,
           java.lang.String cod_provincia_ett,
           java.lang.String cod_provincia_rs,
           java.lang.String cod_tipo_empresa,
           java.lang.String cod_tipovia,
           java.lang.String cod_tipovia_ett,
           java.lang.String cod_tipovia_rs,
           java.lang.String contraste_datos,
           java.lang.String cp,
           java.lang.String cp_ett,
           java.lang.String cp_ext_loc,
           java.lang.String cp_ext_rs,
           java.lang.String cp_rs,
           java.lang.String cuentaCot,
           java.lang.String desc_muni_c,
           java.lang.String desc_muni_e,
           java.lang.String desc_muni_ett_c,
           java.lang.String desc_muni_ett_e,
           java.lang.String desc_muni_rs_c,
           java.lang.String desc_muni_rs_e,
           java.lang.String desc_prov_c,
           java.lang.String desc_prov_e,
           java.lang.String desc_prov_ett_c,
           java.lang.String desc_prov_ett_e,
           java.lang.String desc_prov_rs_c,
           java.lang.String desc_prov_rs_e,
           java.lang.String direc_ext_loc,
           java.lang.String direc_ext_rs,
           java.lang.String e_mail,
           java.lang.String e_mail_rs,
           java.lang.Long emp_corr,
           java.lang.String escalera,
           java.lang.String escalera_ett,
           java.lang.String escalera_rs,
           java.lang.String existe,
           boolean existeSISPE,
           java.lang.String existe_ett,
           java.lang.String fax,
           java.lang.String fax_rs,
           java.util.Calendar fecCalif,
           java.util.Calendar fecDesac,
           java.lang.String fecha_fin_autorizacion,
           java.lang.String fecha_inicio_autorizacion,
           java.util.Calendar fecha_ultima_modificacion,
           java.util.Calendar gen_emp_fec_regprov,
           java.lang.String gen_emp_ind_impsepe,
           java.lang.String gen_emp_ind_prov,
           java.lang.Integer indBloq,
           java.lang.Integer indSispe,
           java.lang.Object[] lista_errores,
           java.util.Vector lista_ett,
           java.lang.String n_via,
           java.lang.String n_via_ett,
           java.lang.String n_via_rs,
           java.lang.String nombre,
           java.lang.String nombre_comercial,
           java.lang.String nombre_contacto,
           java.lang.String nombre_contacto_rs,
           java.lang.String nombre_via,
           java.lang.String nombre_via_ett,
           java.lang.String nombre_via_rs,
           java.lang.String num_autorizacion,
           java.lang.String num_doc,
           java.lang.String nut_ext_loc,
           java.lang.String nut_ext_rs,
           java.lang.String pais,
           java.lang.String pais_ext_loc,
           java.lang.String pais_ext_rs,
           java.lang.String piso,
           java.lang.String piso_ett,
           java.lang.String piso_rs,
           java.lang.String pobl_ext_loc,
           java.lang.String pobl_ext_rs,
           java.lang.String puerta,
           java.lang.String puerta_ett,
           java.lang.String puerta_rs,
           java.lang.String razon_social,
           java.lang.String telefono,
           java.lang.String telefono_rs,
           java.lang.String tipo_empresa,
           java.lang.String titularidad) {
        super(
            auditoria,
            objectId);
        this.apellido1 = apellido1;
        this.apellido1_contacto = apellido1_contacto;
        this.apellido1_contacto_rs = apellido1_contacto_rs;
        this.apellido2 = apellido2;
        this.apellido2_contacto = apellido2_contacto;
        this.apellido2_contacto_rs = apellido2_contacto_rs;
        this.bis_duplicado = bis_duplicado;
        this.bis_duplicado_ett = bis_duplicado_ett;
        this.bis_duplicado_rs = bis_duplicado_rs;
        this.cod_municipio = cod_municipio;
        this.cod_municipio_ett = cod_municipio_ett;
        this.cod_municipio_rs = cod_municipio_rs;
        this.cod_provincia = cod_provincia;
        this.cod_provincia_ett = cod_provincia_ett;
        this.cod_provincia_rs = cod_provincia_rs;
        this.cod_tipo_empresa = cod_tipo_empresa;
        this.cod_tipovia = cod_tipovia;
        this.cod_tipovia_ett = cod_tipovia_ett;
        this.cod_tipovia_rs = cod_tipovia_rs;
        this.contraste_datos = contraste_datos;
        this.cp = cp;
        this.cp_ett = cp_ett;
        this.cp_ext_loc = cp_ext_loc;
        this.cp_ext_rs = cp_ext_rs;
        this.cp_rs = cp_rs;
        this.cuentaCot = cuentaCot;
        this.desc_muni_c = desc_muni_c;
        this.desc_muni_e = desc_muni_e;
        this.desc_muni_ett_c = desc_muni_ett_c;
        this.desc_muni_ett_e = desc_muni_ett_e;
        this.desc_muni_rs_c = desc_muni_rs_c;
        this.desc_muni_rs_e = desc_muni_rs_e;
        this.desc_prov_c = desc_prov_c;
        this.desc_prov_e = desc_prov_e;
        this.desc_prov_ett_c = desc_prov_ett_c;
        this.desc_prov_ett_e = desc_prov_ett_e;
        this.desc_prov_rs_c = desc_prov_rs_c;
        this.desc_prov_rs_e = desc_prov_rs_e;
        this.direc_ext_loc = direc_ext_loc;
        this.direc_ext_rs = direc_ext_rs;
        this.e_mail = e_mail;
        this.e_mail_rs = e_mail_rs;
        this.emp_corr = emp_corr;
        this.escalera = escalera;
        this.escalera_ett = escalera_ett;
        this.escalera_rs = escalera_rs;
        this.existe = existe;
        this.existeSISPE = existeSISPE;
        this.existe_ett = existe_ett;
        this.fax = fax;
        this.fax_rs = fax_rs;
        this.fecCalif = fecCalif;
        this.fecDesac = fecDesac;
        this.fecha_fin_autorizacion = fecha_fin_autorizacion;
        this.fecha_inicio_autorizacion = fecha_inicio_autorizacion;
        this.fecha_ultima_modificacion = fecha_ultima_modificacion;
        this.gen_emp_fec_regprov = gen_emp_fec_regprov;
        this.gen_emp_ind_impsepe = gen_emp_ind_impsepe;
        this.gen_emp_ind_prov = gen_emp_ind_prov;
        this.indBloq = indBloq;
        this.indSispe = indSispe;
        this.lista_errores = lista_errores;
        this.lista_ett = lista_ett;
        this.n_via = n_via;
        this.n_via_ett = n_via_ett;
        this.n_via_rs = n_via_rs;
        this.nombre = nombre;
        this.nombre_comercial = nombre_comercial;
        this.nombre_contacto = nombre_contacto;
        this.nombre_contacto_rs = nombre_contacto_rs;
        this.nombre_via = nombre_via;
        this.nombre_via_ett = nombre_via_ett;
        this.nombre_via_rs = nombre_via_rs;
        this.num_autorizacion = num_autorizacion;
        this.num_doc = num_doc;
        this.nut_ext_loc = nut_ext_loc;
        this.nut_ext_rs = nut_ext_rs;
        this.pais = pais;
        this.pais_ext_loc = pais_ext_loc;
        this.pais_ext_rs = pais_ext_rs;
        this.piso = piso;
        this.piso_ett = piso_ett;
        this.piso_rs = piso_rs;
        this.pobl_ext_loc = pobl_ext_loc;
        this.pobl_ext_rs = pobl_ext_rs;
        this.puerta = puerta;
        this.puerta_ett = puerta_ett;
        this.puerta_rs = puerta_rs;
        this.razon_social = razon_social;
        this.telefono = telefono;
        this.telefono_rs = telefono_rs;
        this.tipo_empresa = tipo_empresa;
        this.titularidad = titularidad;
    }


    /**
     * Gets the apellido1 value for this EmpresarioWSValueObject.
     * 
     * @return apellido1
     */
    public java.lang.String getApellido1() {
        return apellido1;
    }


    /**
     * Sets the apellido1 value for this EmpresarioWSValueObject.
     * 
     * @param apellido1
     */
    public void setApellido1(java.lang.String apellido1) {
        this.apellido1 = apellido1;
    }


    /**
     * Gets the apellido1_contacto value for this EmpresarioWSValueObject.
     * 
     * @return apellido1_contacto
     */
    public java.lang.String getApellido1_contacto() {
        return apellido1_contacto;
    }


    /**
     * Sets the apellido1_contacto value for this EmpresarioWSValueObject.
     * 
     * @param apellido1_contacto
     */
    public void setApellido1_contacto(java.lang.String apellido1_contacto) {
        this.apellido1_contacto = apellido1_contacto;
    }


    /**
     * Gets the apellido1_contacto_rs value for this EmpresarioWSValueObject.
     * 
     * @return apellido1_contacto_rs
     */
    public java.lang.String getApellido1_contacto_rs() {
        return apellido1_contacto_rs;
    }


    /**
     * Sets the apellido1_contacto_rs value for this EmpresarioWSValueObject.
     * 
     * @param apellido1_contacto_rs
     */
    public void setApellido1_contacto_rs(java.lang.String apellido1_contacto_rs) {
        this.apellido1_contacto_rs = apellido1_contacto_rs;
    }


    /**
     * Gets the apellido2 value for this EmpresarioWSValueObject.
     * 
     * @return apellido2
     */
    public java.lang.String getApellido2() {
        return apellido2;
    }


    /**
     * Sets the apellido2 value for this EmpresarioWSValueObject.
     * 
     * @param apellido2
     */
    public void setApellido2(java.lang.String apellido2) {
        this.apellido2 = apellido2;
    }


    /**
     * Gets the apellido2_contacto value for this EmpresarioWSValueObject.
     * 
     * @return apellido2_contacto
     */
    public java.lang.String getApellido2_contacto() {
        return apellido2_contacto;
    }


    /**
     * Sets the apellido2_contacto value for this EmpresarioWSValueObject.
     * 
     * @param apellido2_contacto
     */
    public void setApellido2_contacto(java.lang.String apellido2_contacto) {
        this.apellido2_contacto = apellido2_contacto;
    }


    /**
     * Gets the apellido2_contacto_rs value for this EmpresarioWSValueObject.
     * 
     * @return apellido2_contacto_rs
     */
    public java.lang.String getApellido2_contacto_rs() {
        return apellido2_contacto_rs;
    }


    /**
     * Sets the apellido2_contacto_rs value for this EmpresarioWSValueObject.
     * 
     * @param apellido2_contacto_rs
     */
    public void setApellido2_contacto_rs(java.lang.String apellido2_contacto_rs) {
        this.apellido2_contacto_rs = apellido2_contacto_rs;
    }


    /**
     * Gets the bis_duplicado value for this EmpresarioWSValueObject.
     * 
     * @return bis_duplicado
     */
    public java.lang.String getBis_duplicado() {
        return bis_duplicado;
    }


    /**
     * Sets the bis_duplicado value for this EmpresarioWSValueObject.
     * 
     * @param bis_duplicado
     */
    public void setBis_duplicado(java.lang.String bis_duplicado) {
        this.bis_duplicado = bis_duplicado;
    }


    /**
     * Gets the bis_duplicado_ett value for this EmpresarioWSValueObject.
     * 
     * @return bis_duplicado_ett
     */
    public java.lang.String getBis_duplicado_ett() {
        return bis_duplicado_ett;
    }


    /**
     * Sets the bis_duplicado_ett value for this EmpresarioWSValueObject.
     * 
     * @param bis_duplicado_ett
     */
    public void setBis_duplicado_ett(java.lang.String bis_duplicado_ett) {
        this.bis_duplicado_ett = bis_duplicado_ett;
    }


    /**
     * Gets the bis_duplicado_rs value for this EmpresarioWSValueObject.
     * 
     * @return bis_duplicado_rs
     */
    public java.lang.String getBis_duplicado_rs() {
        return bis_duplicado_rs;
    }


    /**
     * Sets the bis_duplicado_rs value for this EmpresarioWSValueObject.
     * 
     * @param bis_duplicado_rs
     */
    public void setBis_duplicado_rs(java.lang.String bis_duplicado_rs) {
        this.bis_duplicado_rs = bis_duplicado_rs;
    }


    /**
     * Gets the cod_municipio value for this EmpresarioWSValueObject.
     * 
     * @return cod_municipio
     */
    public java.lang.String getCod_municipio() {
        return cod_municipio;
    }


    /**
     * Sets the cod_municipio value for this EmpresarioWSValueObject.
     * 
     * @param cod_municipio
     */
    public void setCod_municipio(java.lang.String cod_municipio) {
        this.cod_municipio = cod_municipio;
    }


    /**
     * Gets the cod_municipio_ett value for this EmpresarioWSValueObject.
     * 
     * @return cod_municipio_ett
     */
    public java.lang.String getCod_municipio_ett() {
        return cod_municipio_ett;
    }


    /**
     * Sets the cod_municipio_ett value for this EmpresarioWSValueObject.
     * 
     * @param cod_municipio_ett
     */
    public void setCod_municipio_ett(java.lang.String cod_municipio_ett) {
        this.cod_municipio_ett = cod_municipio_ett;
    }


    /**
     * Gets the cod_municipio_rs value for this EmpresarioWSValueObject.
     * 
     * @return cod_municipio_rs
     */
    public java.lang.String getCod_municipio_rs() {
        return cod_municipio_rs;
    }


    /**
     * Sets the cod_municipio_rs value for this EmpresarioWSValueObject.
     * 
     * @param cod_municipio_rs
     */
    public void setCod_municipio_rs(java.lang.String cod_municipio_rs) {
        this.cod_municipio_rs = cod_municipio_rs;
    }


    /**
     * Gets the cod_provincia value for this EmpresarioWSValueObject.
     * 
     * @return cod_provincia
     */
    public java.lang.String getCod_provincia() {
        return cod_provincia;
    }


    /**
     * Sets the cod_provincia value for this EmpresarioWSValueObject.
     * 
     * @param cod_provincia
     */
    public void setCod_provincia(java.lang.String cod_provincia) {
        this.cod_provincia = cod_provincia;
    }


    /**
     * Gets the cod_provincia_ett value for this EmpresarioWSValueObject.
     * 
     * @return cod_provincia_ett
     */
    public java.lang.String getCod_provincia_ett() {
        return cod_provincia_ett;
    }


    /**
     * Sets the cod_provincia_ett value for this EmpresarioWSValueObject.
     * 
     * @param cod_provincia_ett
     */
    public void setCod_provincia_ett(java.lang.String cod_provincia_ett) {
        this.cod_provincia_ett = cod_provincia_ett;
    }


    /**
     * Gets the cod_provincia_rs value for this EmpresarioWSValueObject.
     * 
     * @return cod_provincia_rs
     */
    public java.lang.String getCod_provincia_rs() {
        return cod_provincia_rs;
    }


    /**
     * Sets the cod_provincia_rs value for this EmpresarioWSValueObject.
     * 
     * @param cod_provincia_rs
     */
    public void setCod_provincia_rs(java.lang.String cod_provincia_rs) {
        this.cod_provincia_rs = cod_provincia_rs;
    }


    /**
     * Gets the cod_tipo_empresa value for this EmpresarioWSValueObject.
     * 
     * @return cod_tipo_empresa
     */
    public java.lang.String getCod_tipo_empresa() {
        return cod_tipo_empresa;
    }


    /**
     * Sets the cod_tipo_empresa value for this EmpresarioWSValueObject.
     * 
     * @param cod_tipo_empresa
     */
    public void setCod_tipo_empresa(java.lang.String cod_tipo_empresa) {
        this.cod_tipo_empresa = cod_tipo_empresa;
    }


    /**
     * Gets the cod_tipovia value for this EmpresarioWSValueObject.
     * 
     * @return cod_tipovia
     */
    public java.lang.String getCod_tipovia() {
        return cod_tipovia;
    }


    /**
     * Sets the cod_tipovia value for this EmpresarioWSValueObject.
     * 
     * @param cod_tipovia
     */
    public void setCod_tipovia(java.lang.String cod_tipovia) {
        this.cod_tipovia = cod_tipovia;
    }


    /**
     * Gets the cod_tipovia_ett value for this EmpresarioWSValueObject.
     * 
     * @return cod_tipovia_ett
     */
    public java.lang.String getCod_tipovia_ett() {
        return cod_tipovia_ett;
    }


    /**
     * Sets the cod_tipovia_ett value for this EmpresarioWSValueObject.
     * 
     * @param cod_tipovia_ett
     */
    public void setCod_tipovia_ett(java.lang.String cod_tipovia_ett) {
        this.cod_tipovia_ett = cod_tipovia_ett;
    }


    /**
     * Gets the cod_tipovia_rs value for this EmpresarioWSValueObject.
     * 
     * @return cod_tipovia_rs
     */
    public java.lang.String getCod_tipovia_rs() {
        return cod_tipovia_rs;
    }


    /**
     * Sets the cod_tipovia_rs value for this EmpresarioWSValueObject.
     * 
     * @param cod_tipovia_rs
     */
    public void setCod_tipovia_rs(java.lang.String cod_tipovia_rs) {
        this.cod_tipovia_rs = cod_tipovia_rs;
    }


    /**
     * Gets the contraste_datos value for this EmpresarioWSValueObject.
     * 
     * @return contraste_datos
     */
    public java.lang.String getContraste_datos() {
        return contraste_datos;
    }


    /**
     * Sets the contraste_datos value for this EmpresarioWSValueObject.
     * 
     * @param contraste_datos
     */
    public void setContraste_datos(java.lang.String contraste_datos) {
        this.contraste_datos = contraste_datos;
    }


    /**
     * Gets the cp value for this EmpresarioWSValueObject.
     * 
     * @return cp
     */
    public java.lang.String getCp() {
        return cp;
    }


    /**
     * Sets the cp value for this EmpresarioWSValueObject.
     * 
     * @param cp
     */
    public void setCp(java.lang.String cp) {
        this.cp = cp;
    }


    /**
     * Gets the cp_ett value for this EmpresarioWSValueObject.
     * 
     * @return cp_ett
     */
    public java.lang.String getCp_ett() {
        return cp_ett;
    }


    /**
     * Sets the cp_ett value for this EmpresarioWSValueObject.
     * 
     * @param cp_ett
     */
    public void setCp_ett(java.lang.String cp_ett) {
        this.cp_ett = cp_ett;
    }


    /**
     * Gets the cp_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @return cp_ext_loc
     */
    public java.lang.String getCp_ext_loc() {
        return cp_ext_loc;
    }


    /**
     * Sets the cp_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @param cp_ext_loc
     */
    public void setCp_ext_loc(java.lang.String cp_ext_loc) {
        this.cp_ext_loc = cp_ext_loc;
    }


    /**
     * Gets the cp_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @return cp_ext_rs
     */
    public java.lang.String getCp_ext_rs() {
        return cp_ext_rs;
    }


    /**
     * Sets the cp_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @param cp_ext_rs
     */
    public void setCp_ext_rs(java.lang.String cp_ext_rs) {
        this.cp_ext_rs = cp_ext_rs;
    }


    /**
     * Gets the cp_rs value for this EmpresarioWSValueObject.
     * 
     * @return cp_rs
     */
    public java.lang.String getCp_rs() {
        return cp_rs;
    }


    /**
     * Sets the cp_rs value for this EmpresarioWSValueObject.
     * 
     * @param cp_rs
     */
    public void setCp_rs(java.lang.String cp_rs) {
        this.cp_rs = cp_rs;
    }


    /**
     * Gets the cuentaCot value for this EmpresarioWSValueObject.
     * 
     * @return cuentaCot
     */
    public java.lang.String getCuentaCot() {
        return cuentaCot;
    }


    /**
     * Sets the cuentaCot value for this EmpresarioWSValueObject.
     * 
     * @param cuentaCot
     */
    public void setCuentaCot(java.lang.String cuentaCot) {
        this.cuentaCot = cuentaCot;
    }


    /**
     * Gets the desc_muni_c value for this EmpresarioWSValueObject.
     * 
     * @return desc_muni_c
     */
    public java.lang.String getDesc_muni_c() {
        return desc_muni_c;
    }


    /**
     * Sets the desc_muni_c value for this EmpresarioWSValueObject.
     * 
     * @param desc_muni_c
     */
    public void setDesc_muni_c(java.lang.String desc_muni_c) {
        this.desc_muni_c = desc_muni_c;
    }


    /**
     * Gets the desc_muni_e value for this EmpresarioWSValueObject.
     * 
     * @return desc_muni_e
     */
    public java.lang.String getDesc_muni_e() {
        return desc_muni_e;
    }


    /**
     * Sets the desc_muni_e value for this EmpresarioWSValueObject.
     * 
     * @param desc_muni_e
     */
    public void setDesc_muni_e(java.lang.String desc_muni_e) {
        this.desc_muni_e = desc_muni_e;
    }


    /**
     * Gets the desc_muni_ett_c value for this EmpresarioWSValueObject.
     * 
     * @return desc_muni_ett_c
     */
    public java.lang.String getDesc_muni_ett_c() {
        return desc_muni_ett_c;
    }


    /**
     * Sets the desc_muni_ett_c value for this EmpresarioWSValueObject.
     * 
     * @param desc_muni_ett_c
     */
    public void setDesc_muni_ett_c(java.lang.String desc_muni_ett_c) {
        this.desc_muni_ett_c = desc_muni_ett_c;
    }


    /**
     * Gets the desc_muni_ett_e value for this EmpresarioWSValueObject.
     * 
     * @return desc_muni_ett_e
     */
    public java.lang.String getDesc_muni_ett_e() {
        return desc_muni_ett_e;
    }


    /**
     * Sets the desc_muni_ett_e value for this EmpresarioWSValueObject.
     * 
     * @param desc_muni_ett_e
     */
    public void setDesc_muni_ett_e(java.lang.String desc_muni_ett_e) {
        this.desc_muni_ett_e = desc_muni_ett_e;
    }


    /**
     * Gets the desc_muni_rs_c value for this EmpresarioWSValueObject.
     * 
     * @return desc_muni_rs_c
     */
    public java.lang.String getDesc_muni_rs_c() {
        return desc_muni_rs_c;
    }


    /**
     * Sets the desc_muni_rs_c value for this EmpresarioWSValueObject.
     * 
     * @param desc_muni_rs_c
     */
    public void setDesc_muni_rs_c(java.lang.String desc_muni_rs_c) {
        this.desc_muni_rs_c = desc_muni_rs_c;
    }


    /**
     * Gets the desc_muni_rs_e value for this EmpresarioWSValueObject.
     * 
     * @return desc_muni_rs_e
     */
    public java.lang.String getDesc_muni_rs_e() {
        return desc_muni_rs_e;
    }


    /**
     * Sets the desc_muni_rs_e value for this EmpresarioWSValueObject.
     * 
     * @param desc_muni_rs_e
     */
    public void setDesc_muni_rs_e(java.lang.String desc_muni_rs_e) {
        this.desc_muni_rs_e = desc_muni_rs_e;
    }


    /**
     * Gets the desc_prov_c value for this EmpresarioWSValueObject.
     * 
     * @return desc_prov_c
     */
    public java.lang.String getDesc_prov_c() {
        return desc_prov_c;
    }


    /**
     * Sets the desc_prov_c value for this EmpresarioWSValueObject.
     * 
     * @param desc_prov_c
     */
    public void setDesc_prov_c(java.lang.String desc_prov_c) {
        this.desc_prov_c = desc_prov_c;
    }


    /**
     * Gets the desc_prov_e value for this EmpresarioWSValueObject.
     * 
     * @return desc_prov_e
     */
    public java.lang.String getDesc_prov_e() {
        return desc_prov_e;
    }


    /**
     * Sets the desc_prov_e value for this EmpresarioWSValueObject.
     * 
     * @param desc_prov_e
     */
    public void setDesc_prov_e(java.lang.String desc_prov_e) {
        this.desc_prov_e = desc_prov_e;
    }


    /**
     * Gets the desc_prov_ett_c value for this EmpresarioWSValueObject.
     * 
     * @return desc_prov_ett_c
     */
    public java.lang.String getDesc_prov_ett_c() {
        return desc_prov_ett_c;
    }


    /**
     * Sets the desc_prov_ett_c value for this EmpresarioWSValueObject.
     * 
     * @param desc_prov_ett_c
     */
    public void setDesc_prov_ett_c(java.lang.String desc_prov_ett_c) {
        this.desc_prov_ett_c = desc_prov_ett_c;
    }


    /**
     * Gets the desc_prov_ett_e value for this EmpresarioWSValueObject.
     * 
     * @return desc_prov_ett_e
     */
    public java.lang.String getDesc_prov_ett_e() {
        return desc_prov_ett_e;
    }


    /**
     * Sets the desc_prov_ett_e value for this EmpresarioWSValueObject.
     * 
     * @param desc_prov_ett_e
     */
    public void setDesc_prov_ett_e(java.lang.String desc_prov_ett_e) {
        this.desc_prov_ett_e = desc_prov_ett_e;
    }


    /**
     * Gets the desc_prov_rs_c value for this EmpresarioWSValueObject.
     * 
     * @return desc_prov_rs_c
     */
    public java.lang.String getDesc_prov_rs_c() {
        return desc_prov_rs_c;
    }


    /**
     * Sets the desc_prov_rs_c value for this EmpresarioWSValueObject.
     * 
     * @param desc_prov_rs_c
     */
    public void setDesc_prov_rs_c(java.lang.String desc_prov_rs_c) {
        this.desc_prov_rs_c = desc_prov_rs_c;
    }


    /**
     * Gets the desc_prov_rs_e value for this EmpresarioWSValueObject.
     * 
     * @return desc_prov_rs_e
     */
    public java.lang.String getDesc_prov_rs_e() {
        return desc_prov_rs_e;
    }


    /**
     * Sets the desc_prov_rs_e value for this EmpresarioWSValueObject.
     * 
     * @param desc_prov_rs_e
     */
    public void setDesc_prov_rs_e(java.lang.String desc_prov_rs_e) {
        this.desc_prov_rs_e = desc_prov_rs_e;
    }


    /**
     * Gets the direc_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @return direc_ext_loc
     */
    public java.lang.String getDirec_ext_loc() {
        return direc_ext_loc;
    }


    /**
     * Sets the direc_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @param direc_ext_loc
     */
    public void setDirec_ext_loc(java.lang.String direc_ext_loc) {
        this.direc_ext_loc = direc_ext_loc;
    }


    /**
     * Gets the direc_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @return direc_ext_rs
     */
    public java.lang.String getDirec_ext_rs() {
        return direc_ext_rs;
    }


    /**
     * Sets the direc_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @param direc_ext_rs
     */
    public void setDirec_ext_rs(java.lang.String direc_ext_rs) {
        this.direc_ext_rs = direc_ext_rs;
    }


    /**
     * Gets the e_mail value for this EmpresarioWSValueObject.
     * 
     * @return e_mail
     */
    public java.lang.String getE_mail() {
        return e_mail;
    }


    /**
     * Sets the e_mail value for this EmpresarioWSValueObject.
     * 
     * @param e_mail
     */
    public void setE_mail(java.lang.String e_mail) {
        this.e_mail = e_mail;
    }


    /**
     * Gets the e_mail_rs value for this EmpresarioWSValueObject.
     * 
     * @return e_mail_rs
     */
    public java.lang.String getE_mail_rs() {
        return e_mail_rs;
    }


    /**
     * Sets the e_mail_rs value for this EmpresarioWSValueObject.
     * 
     * @param e_mail_rs
     */
    public void setE_mail_rs(java.lang.String e_mail_rs) {
        this.e_mail_rs = e_mail_rs;
    }


    /**
     * Gets the emp_corr value for this EmpresarioWSValueObject.
     * 
     * @return emp_corr
     */
    public java.lang.Long getEmp_corr() {
        return emp_corr;
    }


    /**
     * Sets the emp_corr value for this EmpresarioWSValueObject.
     * 
     * @param emp_corr
     */
    public void setEmp_corr(java.lang.Long emp_corr) {
        this.emp_corr = emp_corr;
    }


    /**
     * Gets the escalera value for this EmpresarioWSValueObject.
     * 
     * @return escalera
     */
    public java.lang.String getEscalera() {
        return escalera;
    }


    /**
     * Sets the escalera value for this EmpresarioWSValueObject.
     * 
     * @param escalera
     */
    public void setEscalera(java.lang.String escalera) {
        this.escalera = escalera;
    }


    /**
     * Gets the escalera_ett value for this EmpresarioWSValueObject.
     * 
     * @return escalera_ett
     */
    public java.lang.String getEscalera_ett() {
        return escalera_ett;
    }


    /**
     * Sets the escalera_ett value for this EmpresarioWSValueObject.
     * 
     * @param escalera_ett
     */
    public void setEscalera_ett(java.lang.String escalera_ett) {
        this.escalera_ett = escalera_ett;
    }


    /**
     * Gets the escalera_rs value for this EmpresarioWSValueObject.
     * 
     * @return escalera_rs
     */
    public java.lang.String getEscalera_rs() {
        return escalera_rs;
    }


    /**
     * Sets the escalera_rs value for this EmpresarioWSValueObject.
     * 
     * @param escalera_rs
     */
    public void setEscalera_rs(java.lang.String escalera_rs) {
        this.escalera_rs = escalera_rs;
    }


    /**
     * Gets the existe value for this EmpresarioWSValueObject.
     * 
     * @return existe
     */
    public java.lang.String getExiste() {
        return existe;
    }


    /**
     * Sets the existe value for this EmpresarioWSValueObject.
     * 
     * @param existe
     */
    public void setExiste(java.lang.String existe) {
        this.existe = existe;
    }


    /**
     * Gets the existeSISPE value for this EmpresarioWSValueObject.
     * 
     * @return existeSISPE
     */
    public boolean isExisteSISPE() {
        return existeSISPE;
    }


    /**
     * Sets the existeSISPE value for this EmpresarioWSValueObject.
     * 
     * @param existeSISPE
     */
    public void setExisteSISPE(boolean existeSISPE) {
        this.existeSISPE = existeSISPE;
    }


    /**
     * Gets the existe_ett value for this EmpresarioWSValueObject.
     * 
     * @return existe_ett
     */
    public java.lang.String getExiste_ett() {
        return existe_ett;
    }


    /**
     * Sets the existe_ett value for this EmpresarioWSValueObject.
     * 
     * @param existe_ett
     */
    public void setExiste_ett(java.lang.String existe_ett) {
        this.existe_ett = existe_ett;
    }


    /**
     * Gets the fax value for this EmpresarioWSValueObject.
     * 
     * @return fax
     */
    public java.lang.String getFax() {
        return fax;
    }


    /**
     * Sets the fax value for this EmpresarioWSValueObject.
     * 
     * @param fax
     */
    public void setFax(java.lang.String fax) {
        this.fax = fax;
    }


    /**
     * Gets the fax_rs value for this EmpresarioWSValueObject.
     * 
     * @return fax_rs
     */
    public java.lang.String getFax_rs() {
        return fax_rs;
    }


    /**
     * Sets the fax_rs value for this EmpresarioWSValueObject.
     * 
     * @param fax_rs
     */
    public void setFax_rs(java.lang.String fax_rs) {
        this.fax_rs = fax_rs;
    }


    /**
     * Gets the fecCalif value for this EmpresarioWSValueObject.
     * 
     * @return fecCalif
     */
    public java.util.Calendar getFecCalif() {
        return fecCalif;
    }


    /**
     * Sets the fecCalif value for this EmpresarioWSValueObject.
     * 
     * @param fecCalif
     */
    public void setFecCalif(java.util.Calendar fecCalif) {
        this.fecCalif = fecCalif;
    }


    /**
     * Gets the fecDesac value for this EmpresarioWSValueObject.
     * 
     * @return fecDesac
     */
    public java.util.Calendar getFecDesac() {
        return fecDesac;
    }


    /**
     * Sets the fecDesac value for this EmpresarioWSValueObject.
     * 
     * @param fecDesac
     */
    public void setFecDesac(java.util.Calendar fecDesac) {
        this.fecDesac = fecDesac;
    }


    /**
     * Gets the fecha_fin_autorizacion value for this EmpresarioWSValueObject.
     * 
     * @return fecha_fin_autorizacion
     */
    public java.lang.String getFecha_fin_autorizacion() {
        return fecha_fin_autorizacion;
    }


    /**
     * Sets the fecha_fin_autorizacion value for this EmpresarioWSValueObject.
     * 
     * @param fecha_fin_autorizacion
     */
    public void setFecha_fin_autorizacion(java.lang.String fecha_fin_autorizacion) {
        this.fecha_fin_autorizacion = fecha_fin_autorizacion;
    }


    /**
     * Gets the fecha_inicio_autorizacion value for this EmpresarioWSValueObject.
     * 
     * @return fecha_inicio_autorizacion
     */
    public java.lang.String getFecha_inicio_autorizacion() {
        return fecha_inicio_autorizacion;
    }


    /**
     * Sets the fecha_inicio_autorizacion value for this EmpresarioWSValueObject.
     * 
     * @param fecha_inicio_autorizacion
     */
    public void setFecha_inicio_autorizacion(java.lang.String fecha_inicio_autorizacion) {
        this.fecha_inicio_autorizacion = fecha_inicio_autorizacion;
    }


    /**
     * Gets the fecha_ultima_modificacion value for this EmpresarioWSValueObject.
     * 
     * @return fecha_ultima_modificacion
     */
    public java.util.Calendar getFecha_ultima_modificacion() {
        return fecha_ultima_modificacion;
    }


    /**
     * Sets the fecha_ultima_modificacion value for this EmpresarioWSValueObject.
     * 
     * @param fecha_ultima_modificacion
     */
    public void setFecha_ultima_modificacion(java.util.Calendar fecha_ultima_modificacion) {
        this.fecha_ultima_modificacion = fecha_ultima_modificacion;
    }


    /**
     * Gets the gen_emp_fec_regprov value for this EmpresarioWSValueObject.
     * 
     * @return gen_emp_fec_regprov
     */
    public java.util.Calendar getGen_emp_fec_regprov() {
        return gen_emp_fec_regprov;
    }


    /**
     * Sets the gen_emp_fec_regprov value for this EmpresarioWSValueObject.
     * 
     * @param gen_emp_fec_regprov
     */
    public void setGen_emp_fec_regprov(java.util.Calendar gen_emp_fec_regprov) {
        this.gen_emp_fec_regprov = gen_emp_fec_regprov;
    }


    /**
     * Gets the gen_emp_ind_impsepe value for this EmpresarioWSValueObject.
     * 
     * @return gen_emp_ind_impsepe
     */
    public java.lang.String getGen_emp_ind_impsepe() {
        return gen_emp_ind_impsepe;
    }


    /**
     * Sets the gen_emp_ind_impsepe value for this EmpresarioWSValueObject.
     * 
     * @param gen_emp_ind_impsepe
     */
    public void setGen_emp_ind_impsepe(java.lang.String gen_emp_ind_impsepe) {
        this.gen_emp_ind_impsepe = gen_emp_ind_impsepe;
    }


    /**
     * Gets the gen_emp_ind_prov value for this EmpresarioWSValueObject.
     * 
     * @return gen_emp_ind_prov
     */
    public java.lang.String getGen_emp_ind_prov() {
        return gen_emp_ind_prov;
    }


    /**
     * Sets the gen_emp_ind_prov value for this EmpresarioWSValueObject.
     * 
     * @param gen_emp_ind_prov
     */
    public void setGen_emp_ind_prov(java.lang.String gen_emp_ind_prov) {
        this.gen_emp_ind_prov = gen_emp_ind_prov;
    }


    /**
     * Gets the indBloq value for this EmpresarioWSValueObject.
     * 
     * @return indBloq
     */
    public java.lang.Integer getIndBloq() {
        return indBloq;
    }


    /**
     * Sets the indBloq value for this EmpresarioWSValueObject.
     * 
     * @param indBloq
     */
    public void setIndBloq(java.lang.Integer indBloq) {
        this.indBloq = indBloq;
    }


    /**
     * Gets the indSispe value for this EmpresarioWSValueObject.
     * 
     * @return indSispe
     */
    public java.lang.Integer getIndSispe() {
        return indSispe;
    }


    /**
     * Sets the indSispe value for this EmpresarioWSValueObject.
     * 
     * @param indSispe
     */
    public void setIndSispe(java.lang.Integer indSispe) {
        this.indSispe = indSispe;
    }


    /**
     * Gets the lista_errores value for this EmpresarioWSValueObject.
     * 
     * @return lista_errores
     */
    public java.lang.Object[] getLista_errores() {
        return lista_errores;
    }


    /**
     * Sets the lista_errores value for this EmpresarioWSValueObject.
     * 
     * @param lista_errores
     */
    public void setLista_errores(java.lang.Object[] lista_errores) {
        this.lista_errores = lista_errores;
    }


    /**
     * Gets the lista_ett value for this EmpresarioWSValueObject.
     * 
     * @return lista_ett
     */
    public java.util.Vector getLista_ett() {
        return lista_ett;
    }


    /**
     * Sets the lista_ett value for this EmpresarioWSValueObject.
     * 
     * @param lista_ett
     */
    public void setLista_ett(java.util.Vector lista_ett) {
        this.lista_ett = lista_ett;
    }


    /**
     * Gets the n_via value for this EmpresarioWSValueObject.
     * 
     * @return n_via
     */
    public java.lang.String getN_via() {
        return n_via;
    }


    /**
     * Sets the n_via value for this EmpresarioWSValueObject.
     * 
     * @param n_via
     */
    public void setN_via(java.lang.String n_via) {
        this.n_via = n_via;
    }


    /**
     * Gets the n_via_ett value for this EmpresarioWSValueObject.
     * 
     * @return n_via_ett
     */
    public java.lang.String getN_via_ett() {
        return n_via_ett;
    }


    /**
     * Sets the n_via_ett value for this EmpresarioWSValueObject.
     * 
     * @param n_via_ett
     */
    public void setN_via_ett(java.lang.String n_via_ett) {
        this.n_via_ett = n_via_ett;
    }


    /**
     * Gets the n_via_rs value for this EmpresarioWSValueObject.
     * 
     * @return n_via_rs
     */
    public java.lang.String getN_via_rs() {
        return n_via_rs;
    }


    /**
     * Sets the n_via_rs value for this EmpresarioWSValueObject.
     * 
     * @param n_via_rs
     */
    public void setN_via_rs(java.lang.String n_via_rs) {
        this.n_via_rs = n_via_rs;
    }


    /**
     * Gets the nombre value for this EmpresarioWSValueObject.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this EmpresarioWSValueObject.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the nombre_comercial value for this EmpresarioWSValueObject.
     * 
     * @return nombre_comercial
     */
    public java.lang.String getNombre_comercial() {
        return nombre_comercial;
    }


    /**
     * Sets the nombre_comercial value for this EmpresarioWSValueObject.
     * 
     * @param nombre_comercial
     */
    public void setNombre_comercial(java.lang.String nombre_comercial) {
        this.nombre_comercial = nombre_comercial;
    }


    /**
     * Gets the nombre_contacto value for this EmpresarioWSValueObject.
     * 
     * @return nombre_contacto
     */
    public java.lang.String getNombre_contacto() {
        return nombre_contacto;
    }


    /**
     * Sets the nombre_contacto value for this EmpresarioWSValueObject.
     * 
     * @param nombre_contacto
     */
    public void setNombre_contacto(java.lang.String nombre_contacto) {
        this.nombre_contacto = nombre_contacto;
    }


    /**
     * Gets the nombre_contacto_rs value for this EmpresarioWSValueObject.
     * 
     * @return nombre_contacto_rs
     */
    public java.lang.String getNombre_contacto_rs() {
        return nombre_contacto_rs;
    }


    /**
     * Sets the nombre_contacto_rs value for this EmpresarioWSValueObject.
     * 
     * @param nombre_contacto_rs
     */
    public void setNombre_contacto_rs(java.lang.String nombre_contacto_rs) {
        this.nombre_contacto_rs = nombre_contacto_rs;
    }


    /**
     * Gets the nombre_via value for this EmpresarioWSValueObject.
     * 
     * @return nombre_via
     */
    public java.lang.String getNombre_via() {
        return nombre_via;
    }


    /**
     * Sets the nombre_via value for this EmpresarioWSValueObject.
     * 
     * @param nombre_via
     */
    public void setNombre_via(java.lang.String nombre_via) {
        this.nombre_via = nombre_via;
    }


    /**
     * Gets the nombre_via_ett value for this EmpresarioWSValueObject.
     * 
     * @return nombre_via_ett
     */
    public java.lang.String getNombre_via_ett() {
        return nombre_via_ett;
    }


    /**
     * Sets the nombre_via_ett value for this EmpresarioWSValueObject.
     * 
     * @param nombre_via_ett
     */
    public void setNombre_via_ett(java.lang.String nombre_via_ett) {
        this.nombre_via_ett = nombre_via_ett;
    }


    /**
     * Gets the nombre_via_rs value for this EmpresarioWSValueObject.
     * 
     * @return nombre_via_rs
     */
    public java.lang.String getNombre_via_rs() {
        return nombre_via_rs;
    }


    /**
     * Sets the nombre_via_rs value for this EmpresarioWSValueObject.
     * 
     * @param nombre_via_rs
     */
    public void setNombre_via_rs(java.lang.String nombre_via_rs) {
        this.nombre_via_rs = nombre_via_rs;
    }


    /**
     * Gets the num_autorizacion value for this EmpresarioWSValueObject.
     * 
     * @return num_autorizacion
     */
    public java.lang.String getNum_autorizacion() {
        return num_autorizacion;
    }


    /**
     * Sets the num_autorizacion value for this EmpresarioWSValueObject.
     * 
     * @param num_autorizacion
     */
    public void setNum_autorizacion(java.lang.String num_autorizacion) {
        this.num_autorizacion = num_autorizacion;
    }


    /**
     * Gets the num_doc value for this EmpresarioWSValueObject.
     * 
     * @return num_doc
     */
    public java.lang.String getNum_doc() {
        return num_doc;
    }


    /**
     * Sets the num_doc value for this EmpresarioWSValueObject.
     * 
     * @param num_doc
     */
    public void setNum_doc(java.lang.String num_doc) {
        this.num_doc = num_doc;
    }


    /**
     * Gets the nut_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @return nut_ext_loc
     */
    public java.lang.String getNut_ext_loc() {
        return nut_ext_loc;
    }


    /**
     * Sets the nut_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @param nut_ext_loc
     */
    public void setNut_ext_loc(java.lang.String nut_ext_loc) {
        this.nut_ext_loc = nut_ext_loc;
    }


    /**
     * Gets the nut_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @return nut_ext_rs
     */
    public java.lang.String getNut_ext_rs() {
        return nut_ext_rs;
    }


    /**
     * Sets the nut_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @param nut_ext_rs
     */
    public void setNut_ext_rs(java.lang.String nut_ext_rs) {
        this.nut_ext_rs = nut_ext_rs;
    }


    /**
     * Gets the pais value for this EmpresarioWSValueObject.
     * 
     * @return pais
     */
    public java.lang.String getPais() {
        return pais;
    }


    /**
     * Sets the pais value for this EmpresarioWSValueObject.
     * 
     * @param pais
     */
    public void setPais(java.lang.String pais) {
        this.pais = pais;
    }


    /**
     * Gets the pais_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @return pais_ext_loc
     */
    public java.lang.String getPais_ext_loc() {
        return pais_ext_loc;
    }


    /**
     * Sets the pais_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @param pais_ext_loc
     */
    public void setPais_ext_loc(java.lang.String pais_ext_loc) {
        this.pais_ext_loc = pais_ext_loc;
    }


    /**
     * Gets the pais_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @return pais_ext_rs
     */
    public java.lang.String getPais_ext_rs() {
        return pais_ext_rs;
    }


    /**
     * Sets the pais_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @param pais_ext_rs
     */
    public void setPais_ext_rs(java.lang.String pais_ext_rs) {
        this.pais_ext_rs = pais_ext_rs;
    }


    /**
     * Gets the piso value for this EmpresarioWSValueObject.
     * 
     * @return piso
     */
    public java.lang.String getPiso() {
        return piso;
    }


    /**
     * Sets the piso value for this EmpresarioWSValueObject.
     * 
     * @param piso
     */
    public void setPiso(java.lang.String piso) {
        this.piso = piso;
    }


    /**
     * Gets the piso_ett value for this EmpresarioWSValueObject.
     * 
     * @return piso_ett
     */
    public java.lang.String getPiso_ett() {
        return piso_ett;
    }


    /**
     * Sets the piso_ett value for this EmpresarioWSValueObject.
     * 
     * @param piso_ett
     */
    public void setPiso_ett(java.lang.String piso_ett) {
        this.piso_ett = piso_ett;
    }


    /**
     * Gets the piso_rs value for this EmpresarioWSValueObject.
     * 
     * @return piso_rs
     */
    public java.lang.String getPiso_rs() {
        return piso_rs;
    }


    /**
     * Sets the piso_rs value for this EmpresarioWSValueObject.
     * 
     * @param piso_rs
     */
    public void setPiso_rs(java.lang.String piso_rs) {
        this.piso_rs = piso_rs;
    }


    /**
     * Gets the pobl_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @return pobl_ext_loc
     */
    public java.lang.String getPobl_ext_loc() {
        return pobl_ext_loc;
    }


    /**
     * Sets the pobl_ext_loc value for this EmpresarioWSValueObject.
     * 
     * @param pobl_ext_loc
     */
    public void setPobl_ext_loc(java.lang.String pobl_ext_loc) {
        this.pobl_ext_loc = pobl_ext_loc;
    }


    /**
     * Gets the pobl_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @return pobl_ext_rs
     */
    public java.lang.String getPobl_ext_rs() {
        return pobl_ext_rs;
    }


    /**
     * Sets the pobl_ext_rs value for this EmpresarioWSValueObject.
     * 
     * @param pobl_ext_rs
     */
    public void setPobl_ext_rs(java.lang.String pobl_ext_rs) {
        this.pobl_ext_rs = pobl_ext_rs;
    }


    /**
     * Gets the puerta value for this EmpresarioWSValueObject.
     * 
     * @return puerta
     */
    public java.lang.String getPuerta() {
        return puerta;
    }


    /**
     * Sets the puerta value for this EmpresarioWSValueObject.
     * 
     * @param puerta
     */
    public void setPuerta(java.lang.String puerta) {
        this.puerta = puerta;
    }


    /**
     * Gets the puerta_ett value for this EmpresarioWSValueObject.
     * 
     * @return puerta_ett
     */
    public java.lang.String getPuerta_ett() {
        return puerta_ett;
    }


    /**
     * Sets the puerta_ett value for this EmpresarioWSValueObject.
     * 
     * @param puerta_ett
     */
    public void setPuerta_ett(java.lang.String puerta_ett) {
        this.puerta_ett = puerta_ett;
    }


    /**
     * Gets the puerta_rs value for this EmpresarioWSValueObject.
     * 
     * @return puerta_rs
     */
    public java.lang.String getPuerta_rs() {
        return puerta_rs;
    }


    /**
     * Sets the puerta_rs value for this EmpresarioWSValueObject.
     * 
     * @param puerta_rs
     */
    public void setPuerta_rs(java.lang.String puerta_rs) {
        this.puerta_rs = puerta_rs;
    }


    /**
     * Gets the razon_social value for this EmpresarioWSValueObject.
     * 
     * @return razon_social
     */
    public java.lang.String getRazon_social() {
        return razon_social;
    }


    /**
     * Sets the razon_social value for this EmpresarioWSValueObject.
     * 
     * @param razon_social
     */
    public void setRazon_social(java.lang.String razon_social) {
        this.razon_social = razon_social;
    }


    /**
     * Gets the telefono value for this EmpresarioWSValueObject.
     * 
     * @return telefono
     */
    public java.lang.String getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this EmpresarioWSValueObject.
     * 
     * @param telefono
     */
    public void setTelefono(java.lang.String telefono) {
        this.telefono = telefono;
    }


    /**
     * Gets the telefono_rs value for this EmpresarioWSValueObject.
     * 
     * @return telefono_rs
     */
    public java.lang.String getTelefono_rs() {
        return telefono_rs;
    }


    /**
     * Sets the telefono_rs value for this EmpresarioWSValueObject.
     * 
     * @param telefono_rs
     */
    public void setTelefono_rs(java.lang.String telefono_rs) {
        this.telefono_rs = telefono_rs;
    }


    /**
     * Gets the tipo_empresa value for this EmpresarioWSValueObject.
     * 
     * @return tipo_empresa
     */
    public java.lang.String getTipo_empresa() {
        return tipo_empresa;
    }


    /**
     * Sets the tipo_empresa value for this EmpresarioWSValueObject.
     * 
     * @param tipo_empresa
     */
    public void setTipo_empresa(java.lang.String tipo_empresa) {
        this.tipo_empresa = tipo_empresa;
    }


    /**
     * Gets the titularidad value for this EmpresarioWSValueObject.
     * 
     * @return titularidad
     */
    public java.lang.String getTitularidad() {
        return titularidad;
    }


    /**
     * Sets the titularidad value for this EmpresarioWSValueObject.
     * 
     * @param titularidad
     */
    public void setTitularidad(java.lang.String titularidad) {
        this.titularidad = titularidad;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EmpresarioWSValueObject)) return false;
        EmpresarioWSValueObject other = (EmpresarioWSValueObject) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.apellido1==null && other.getApellido1()==null) || 
             (this.apellido1!=null &&
              this.apellido1.equals(other.getApellido1()))) &&
            ((this.apellido1_contacto==null && other.getApellido1_contacto()==null) || 
             (this.apellido1_contacto!=null &&
              this.apellido1_contacto.equals(other.getApellido1_contacto()))) &&
            ((this.apellido1_contacto_rs==null && other.getApellido1_contacto_rs()==null) || 
             (this.apellido1_contacto_rs!=null &&
              this.apellido1_contacto_rs.equals(other.getApellido1_contacto_rs()))) &&
            ((this.apellido2==null && other.getApellido2()==null) || 
             (this.apellido2!=null &&
              this.apellido2.equals(other.getApellido2()))) &&
            ((this.apellido2_contacto==null && other.getApellido2_contacto()==null) || 
             (this.apellido2_contacto!=null &&
              this.apellido2_contacto.equals(other.getApellido2_contacto()))) &&
            ((this.apellido2_contacto_rs==null && other.getApellido2_contacto_rs()==null) || 
             (this.apellido2_contacto_rs!=null &&
              this.apellido2_contacto_rs.equals(other.getApellido2_contacto_rs()))) &&
            ((this.bis_duplicado==null && other.getBis_duplicado()==null) || 
             (this.bis_duplicado!=null &&
              this.bis_duplicado.equals(other.getBis_duplicado()))) &&
            ((this.bis_duplicado_ett==null && other.getBis_duplicado_ett()==null) || 
             (this.bis_duplicado_ett!=null &&
              this.bis_duplicado_ett.equals(other.getBis_duplicado_ett()))) &&
            ((this.bis_duplicado_rs==null && other.getBis_duplicado_rs()==null) || 
             (this.bis_duplicado_rs!=null &&
              this.bis_duplicado_rs.equals(other.getBis_duplicado_rs()))) &&
            ((this.cod_municipio==null && other.getCod_municipio()==null) || 
             (this.cod_municipio!=null &&
              this.cod_municipio.equals(other.getCod_municipio()))) &&
            ((this.cod_municipio_ett==null && other.getCod_municipio_ett()==null) || 
             (this.cod_municipio_ett!=null &&
              this.cod_municipio_ett.equals(other.getCod_municipio_ett()))) &&
            ((this.cod_municipio_rs==null && other.getCod_municipio_rs()==null) || 
             (this.cod_municipio_rs!=null &&
              this.cod_municipio_rs.equals(other.getCod_municipio_rs()))) &&
            ((this.cod_provincia==null && other.getCod_provincia()==null) || 
             (this.cod_provincia!=null &&
              this.cod_provincia.equals(other.getCod_provincia()))) &&
            ((this.cod_provincia_ett==null && other.getCod_provincia_ett()==null) || 
             (this.cod_provincia_ett!=null &&
              this.cod_provincia_ett.equals(other.getCod_provincia_ett()))) &&
            ((this.cod_provincia_rs==null && other.getCod_provincia_rs()==null) || 
             (this.cod_provincia_rs!=null &&
              this.cod_provincia_rs.equals(other.getCod_provincia_rs()))) &&
            ((this.cod_tipo_empresa==null && other.getCod_tipo_empresa()==null) || 
             (this.cod_tipo_empresa!=null &&
              this.cod_tipo_empresa.equals(other.getCod_tipo_empresa()))) &&
            ((this.cod_tipovia==null && other.getCod_tipovia()==null) || 
             (this.cod_tipovia!=null &&
              this.cod_tipovia.equals(other.getCod_tipovia()))) &&
            ((this.cod_tipovia_ett==null && other.getCod_tipovia_ett()==null) || 
             (this.cod_tipovia_ett!=null &&
              this.cod_tipovia_ett.equals(other.getCod_tipovia_ett()))) &&
            ((this.cod_tipovia_rs==null && other.getCod_tipovia_rs()==null) || 
             (this.cod_tipovia_rs!=null &&
              this.cod_tipovia_rs.equals(other.getCod_tipovia_rs()))) &&
            ((this.contraste_datos==null && other.getContraste_datos()==null) || 
             (this.contraste_datos!=null &&
              this.contraste_datos.equals(other.getContraste_datos()))) &&
            ((this.cp==null && other.getCp()==null) || 
             (this.cp!=null &&
              this.cp.equals(other.getCp()))) &&
            ((this.cp_ett==null && other.getCp_ett()==null) || 
             (this.cp_ett!=null &&
              this.cp_ett.equals(other.getCp_ett()))) &&
            ((this.cp_ext_loc==null && other.getCp_ext_loc()==null) || 
             (this.cp_ext_loc!=null &&
              this.cp_ext_loc.equals(other.getCp_ext_loc()))) &&
            ((this.cp_ext_rs==null && other.getCp_ext_rs()==null) || 
             (this.cp_ext_rs!=null &&
              this.cp_ext_rs.equals(other.getCp_ext_rs()))) &&
            ((this.cp_rs==null && other.getCp_rs()==null) || 
             (this.cp_rs!=null &&
              this.cp_rs.equals(other.getCp_rs()))) &&
            ((this.cuentaCot==null && other.getCuentaCot()==null) || 
             (this.cuentaCot!=null &&
              this.cuentaCot.equals(other.getCuentaCot()))) &&
            ((this.desc_muni_c==null && other.getDesc_muni_c()==null) || 
             (this.desc_muni_c!=null &&
              this.desc_muni_c.equals(other.getDesc_muni_c()))) &&
            ((this.desc_muni_e==null && other.getDesc_muni_e()==null) || 
             (this.desc_muni_e!=null &&
              this.desc_muni_e.equals(other.getDesc_muni_e()))) &&
            ((this.desc_muni_ett_c==null && other.getDesc_muni_ett_c()==null) || 
             (this.desc_muni_ett_c!=null &&
              this.desc_muni_ett_c.equals(other.getDesc_muni_ett_c()))) &&
            ((this.desc_muni_ett_e==null && other.getDesc_muni_ett_e()==null) || 
             (this.desc_muni_ett_e!=null &&
              this.desc_muni_ett_e.equals(other.getDesc_muni_ett_e()))) &&
            ((this.desc_muni_rs_c==null && other.getDesc_muni_rs_c()==null) || 
             (this.desc_muni_rs_c!=null &&
              this.desc_muni_rs_c.equals(other.getDesc_muni_rs_c()))) &&
            ((this.desc_muni_rs_e==null && other.getDesc_muni_rs_e()==null) || 
             (this.desc_muni_rs_e!=null &&
              this.desc_muni_rs_e.equals(other.getDesc_muni_rs_e()))) &&
            ((this.desc_prov_c==null && other.getDesc_prov_c()==null) || 
             (this.desc_prov_c!=null &&
              this.desc_prov_c.equals(other.getDesc_prov_c()))) &&
            ((this.desc_prov_e==null && other.getDesc_prov_e()==null) || 
             (this.desc_prov_e!=null &&
              this.desc_prov_e.equals(other.getDesc_prov_e()))) &&
            ((this.desc_prov_ett_c==null && other.getDesc_prov_ett_c()==null) || 
             (this.desc_prov_ett_c!=null &&
              this.desc_prov_ett_c.equals(other.getDesc_prov_ett_c()))) &&
            ((this.desc_prov_ett_e==null && other.getDesc_prov_ett_e()==null) || 
             (this.desc_prov_ett_e!=null &&
              this.desc_prov_ett_e.equals(other.getDesc_prov_ett_e()))) &&
            ((this.desc_prov_rs_c==null && other.getDesc_prov_rs_c()==null) || 
             (this.desc_prov_rs_c!=null &&
              this.desc_prov_rs_c.equals(other.getDesc_prov_rs_c()))) &&
            ((this.desc_prov_rs_e==null && other.getDesc_prov_rs_e()==null) || 
             (this.desc_prov_rs_e!=null &&
              this.desc_prov_rs_e.equals(other.getDesc_prov_rs_e()))) &&
            ((this.direc_ext_loc==null && other.getDirec_ext_loc()==null) || 
             (this.direc_ext_loc!=null &&
              this.direc_ext_loc.equals(other.getDirec_ext_loc()))) &&
            ((this.direc_ext_rs==null && other.getDirec_ext_rs()==null) || 
             (this.direc_ext_rs!=null &&
              this.direc_ext_rs.equals(other.getDirec_ext_rs()))) &&
            ((this.e_mail==null && other.getE_mail()==null) || 
             (this.e_mail!=null &&
              this.e_mail.equals(other.getE_mail()))) &&
            ((this.e_mail_rs==null && other.getE_mail_rs()==null) || 
             (this.e_mail_rs!=null &&
              this.e_mail_rs.equals(other.getE_mail_rs()))) &&
            ((this.emp_corr==null && other.getEmp_corr()==null) || 
             (this.emp_corr!=null &&
              this.emp_corr.equals(other.getEmp_corr()))) &&
            ((this.escalera==null && other.getEscalera()==null) || 
             (this.escalera!=null &&
              this.escalera.equals(other.getEscalera()))) &&
            ((this.escalera_ett==null && other.getEscalera_ett()==null) || 
             (this.escalera_ett!=null &&
              this.escalera_ett.equals(other.getEscalera_ett()))) &&
            ((this.escalera_rs==null && other.getEscalera_rs()==null) || 
             (this.escalera_rs!=null &&
              this.escalera_rs.equals(other.getEscalera_rs()))) &&
            ((this.existe==null && other.getExiste()==null) || 
             (this.existe!=null &&
              this.existe.equals(other.getExiste()))) &&
            this.existeSISPE == other.isExisteSISPE() &&
            ((this.existe_ett==null && other.getExiste_ett()==null) || 
             (this.existe_ett!=null &&
              this.existe_ett.equals(other.getExiste_ett()))) &&
            ((this.fax==null && other.getFax()==null) || 
             (this.fax!=null &&
              this.fax.equals(other.getFax()))) &&
            ((this.fax_rs==null && other.getFax_rs()==null) || 
             (this.fax_rs!=null &&
              this.fax_rs.equals(other.getFax_rs()))) &&
            ((this.fecCalif==null && other.getFecCalif()==null) || 
             (this.fecCalif!=null &&
              this.fecCalif.equals(other.getFecCalif()))) &&
            ((this.fecDesac==null && other.getFecDesac()==null) || 
             (this.fecDesac!=null &&
              this.fecDesac.equals(other.getFecDesac()))) &&
            ((this.fecha_fin_autorizacion==null && other.getFecha_fin_autorizacion()==null) || 
             (this.fecha_fin_autorizacion!=null &&
              this.fecha_fin_autorizacion.equals(other.getFecha_fin_autorizacion()))) &&
            ((this.fecha_inicio_autorizacion==null && other.getFecha_inicio_autorizacion()==null) || 
             (this.fecha_inicio_autorizacion!=null &&
              this.fecha_inicio_autorizacion.equals(other.getFecha_inicio_autorizacion()))) &&
            ((this.fecha_ultima_modificacion==null && other.getFecha_ultima_modificacion()==null) || 
             (this.fecha_ultima_modificacion!=null &&
              this.fecha_ultima_modificacion.equals(other.getFecha_ultima_modificacion()))) &&
            ((this.gen_emp_fec_regprov==null && other.getGen_emp_fec_regprov()==null) || 
             (this.gen_emp_fec_regprov!=null &&
              this.gen_emp_fec_regprov.equals(other.getGen_emp_fec_regprov()))) &&
            ((this.gen_emp_ind_impsepe==null && other.getGen_emp_ind_impsepe()==null) || 
             (this.gen_emp_ind_impsepe!=null &&
              this.gen_emp_ind_impsepe.equals(other.getGen_emp_ind_impsepe()))) &&
            ((this.gen_emp_ind_prov==null && other.getGen_emp_ind_prov()==null) || 
             (this.gen_emp_ind_prov!=null &&
              this.gen_emp_ind_prov.equals(other.getGen_emp_ind_prov()))) &&
            ((this.indBloq==null && other.getIndBloq()==null) || 
             (this.indBloq!=null &&
              this.indBloq.equals(other.getIndBloq()))) &&
            ((this.indSispe==null && other.getIndSispe()==null) || 
             (this.indSispe!=null &&
              this.indSispe.equals(other.getIndSispe()))) &&
            ((this.lista_errores==null && other.getLista_errores()==null) || 
             (this.lista_errores!=null &&
              java.util.Arrays.equals(this.lista_errores, other.getLista_errores()))) &&
            ((this.lista_ett==null && other.getLista_ett()==null) || 
             (this.lista_ett!=null &&
              this.lista_ett.equals(other.getLista_ett()))) &&
            ((this.n_via==null && other.getN_via()==null) || 
             (this.n_via!=null &&
              this.n_via.equals(other.getN_via()))) &&
            ((this.n_via_ett==null && other.getN_via_ett()==null) || 
             (this.n_via_ett!=null &&
              this.n_via_ett.equals(other.getN_via_ett()))) &&
            ((this.n_via_rs==null && other.getN_via_rs()==null) || 
             (this.n_via_rs!=null &&
              this.n_via_rs.equals(other.getN_via_rs()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.nombre_comercial==null && other.getNombre_comercial()==null) || 
             (this.nombre_comercial!=null &&
              this.nombre_comercial.equals(other.getNombre_comercial()))) &&
            ((this.nombre_contacto==null && other.getNombre_contacto()==null) || 
             (this.nombre_contacto!=null &&
              this.nombre_contacto.equals(other.getNombre_contacto()))) &&
            ((this.nombre_contacto_rs==null && other.getNombre_contacto_rs()==null) || 
             (this.nombre_contacto_rs!=null &&
              this.nombre_contacto_rs.equals(other.getNombre_contacto_rs()))) &&
            ((this.nombre_via==null && other.getNombre_via()==null) || 
             (this.nombre_via!=null &&
              this.nombre_via.equals(other.getNombre_via()))) &&
            ((this.nombre_via_ett==null && other.getNombre_via_ett()==null) || 
             (this.nombre_via_ett!=null &&
              this.nombre_via_ett.equals(other.getNombre_via_ett()))) &&
            ((this.nombre_via_rs==null && other.getNombre_via_rs()==null) || 
             (this.nombre_via_rs!=null &&
              this.nombre_via_rs.equals(other.getNombre_via_rs()))) &&
            ((this.num_autorizacion==null && other.getNum_autorizacion()==null) || 
             (this.num_autorizacion!=null &&
              this.num_autorizacion.equals(other.getNum_autorizacion()))) &&
            ((this.num_doc==null && other.getNum_doc()==null) || 
             (this.num_doc!=null &&
              this.num_doc.equals(other.getNum_doc()))) &&
            ((this.nut_ext_loc==null && other.getNut_ext_loc()==null) || 
             (this.nut_ext_loc!=null &&
              this.nut_ext_loc.equals(other.getNut_ext_loc()))) &&
            ((this.nut_ext_rs==null && other.getNut_ext_rs()==null) || 
             (this.nut_ext_rs!=null &&
              this.nut_ext_rs.equals(other.getNut_ext_rs()))) &&
            ((this.pais==null && other.getPais()==null) || 
             (this.pais!=null &&
              this.pais.equals(other.getPais()))) &&
            ((this.pais_ext_loc==null && other.getPais_ext_loc()==null) || 
             (this.pais_ext_loc!=null &&
              this.pais_ext_loc.equals(other.getPais_ext_loc()))) &&
            ((this.pais_ext_rs==null && other.getPais_ext_rs()==null) || 
             (this.pais_ext_rs!=null &&
              this.pais_ext_rs.equals(other.getPais_ext_rs()))) &&
            ((this.piso==null && other.getPiso()==null) || 
             (this.piso!=null &&
              this.piso.equals(other.getPiso()))) &&
            ((this.piso_ett==null && other.getPiso_ett()==null) || 
             (this.piso_ett!=null &&
              this.piso_ett.equals(other.getPiso_ett()))) &&
            ((this.piso_rs==null && other.getPiso_rs()==null) || 
             (this.piso_rs!=null &&
              this.piso_rs.equals(other.getPiso_rs()))) &&
            ((this.pobl_ext_loc==null && other.getPobl_ext_loc()==null) || 
             (this.pobl_ext_loc!=null &&
              this.pobl_ext_loc.equals(other.getPobl_ext_loc()))) &&
            ((this.pobl_ext_rs==null && other.getPobl_ext_rs()==null) || 
             (this.pobl_ext_rs!=null &&
              this.pobl_ext_rs.equals(other.getPobl_ext_rs()))) &&
            ((this.puerta==null && other.getPuerta()==null) || 
             (this.puerta!=null &&
              this.puerta.equals(other.getPuerta()))) &&
            ((this.puerta_ett==null && other.getPuerta_ett()==null) || 
             (this.puerta_ett!=null &&
              this.puerta_ett.equals(other.getPuerta_ett()))) &&
            ((this.puerta_rs==null && other.getPuerta_rs()==null) || 
             (this.puerta_rs!=null &&
              this.puerta_rs.equals(other.getPuerta_rs()))) &&
            ((this.razon_social==null && other.getRazon_social()==null) || 
             (this.razon_social!=null &&
              this.razon_social.equals(other.getRazon_social()))) &&
            ((this.telefono==null && other.getTelefono()==null) || 
             (this.telefono!=null &&
              this.telefono.equals(other.getTelefono()))) &&
            ((this.telefono_rs==null && other.getTelefono_rs()==null) || 
             (this.telefono_rs!=null &&
              this.telefono_rs.equals(other.getTelefono_rs()))) &&
            ((this.tipo_empresa==null && other.getTipo_empresa()==null) || 
             (this.tipo_empresa!=null &&
              this.tipo_empresa.equals(other.getTipo_empresa()))) &&
            ((this.titularidad==null && other.getTitularidad()==null) || 
             (this.titularidad!=null &&
              this.titularidad.equals(other.getTitularidad())));
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
        if (getApellido1() != null) {
            _hashCode += getApellido1().hashCode();
        }
        if (getApellido1_contacto() != null) {
            _hashCode += getApellido1_contacto().hashCode();
        }
        if (getApellido1_contacto_rs() != null) {
            _hashCode += getApellido1_contacto_rs().hashCode();
        }
        if (getApellido2() != null) {
            _hashCode += getApellido2().hashCode();
        }
        if (getApellido2_contacto() != null) {
            _hashCode += getApellido2_contacto().hashCode();
        }
        if (getApellido2_contacto_rs() != null) {
            _hashCode += getApellido2_contacto_rs().hashCode();
        }
        if (getBis_duplicado() != null) {
            _hashCode += getBis_duplicado().hashCode();
        }
        if (getBis_duplicado_ett() != null) {
            _hashCode += getBis_duplicado_ett().hashCode();
        }
        if (getBis_duplicado_rs() != null) {
            _hashCode += getBis_duplicado_rs().hashCode();
        }
        if (getCod_municipio() != null) {
            _hashCode += getCod_municipio().hashCode();
        }
        if (getCod_municipio_ett() != null) {
            _hashCode += getCod_municipio_ett().hashCode();
        }
        if (getCod_municipio_rs() != null) {
            _hashCode += getCod_municipio_rs().hashCode();
        }
        if (getCod_provincia() != null) {
            _hashCode += getCod_provincia().hashCode();
        }
        if (getCod_provincia_ett() != null) {
            _hashCode += getCod_provincia_ett().hashCode();
        }
        if (getCod_provincia_rs() != null) {
            _hashCode += getCod_provincia_rs().hashCode();
        }
        if (getCod_tipo_empresa() != null) {
            _hashCode += getCod_tipo_empresa().hashCode();
        }
        if (getCod_tipovia() != null) {
            _hashCode += getCod_tipovia().hashCode();
        }
        if (getCod_tipovia_ett() != null) {
            _hashCode += getCod_tipovia_ett().hashCode();
        }
        if (getCod_tipovia_rs() != null) {
            _hashCode += getCod_tipovia_rs().hashCode();
        }
        if (getContraste_datos() != null) {
            _hashCode += getContraste_datos().hashCode();
        }
        if (getCp() != null) {
            _hashCode += getCp().hashCode();
        }
        if (getCp_ett() != null) {
            _hashCode += getCp_ett().hashCode();
        }
        if (getCp_ext_loc() != null) {
            _hashCode += getCp_ext_loc().hashCode();
        }
        if (getCp_ext_rs() != null) {
            _hashCode += getCp_ext_rs().hashCode();
        }
        if (getCp_rs() != null) {
            _hashCode += getCp_rs().hashCode();
        }
        if (getCuentaCot() != null) {
            _hashCode += getCuentaCot().hashCode();
        }
        if (getDesc_muni_c() != null) {
            _hashCode += getDesc_muni_c().hashCode();
        }
        if (getDesc_muni_e() != null) {
            _hashCode += getDesc_muni_e().hashCode();
        }
        if (getDesc_muni_ett_c() != null) {
            _hashCode += getDesc_muni_ett_c().hashCode();
        }
        if (getDesc_muni_ett_e() != null) {
            _hashCode += getDesc_muni_ett_e().hashCode();
        }
        if (getDesc_muni_rs_c() != null) {
            _hashCode += getDesc_muni_rs_c().hashCode();
        }
        if (getDesc_muni_rs_e() != null) {
            _hashCode += getDesc_muni_rs_e().hashCode();
        }
        if (getDesc_prov_c() != null) {
            _hashCode += getDesc_prov_c().hashCode();
        }
        if (getDesc_prov_e() != null) {
            _hashCode += getDesc_prov_e().hashCode();
        }
        if (getDesc_prov_ett_c() != null) {
            _hashCode += getDesc_prov_ett_c().hashCode();
        }
        if (getDesc_prov_ett_e() != null) {
            _hashCode += getDesc_prov_ett_e().hashCode();
        }
        if (getDesc_prov_rs_c() != null) {
            _hashCode += getDesc_prov_rs_c().hashCode();
        }
        if (getDesc_prov_rs_e() != null) {
            _hashCode += getDesc_prov_rs_e().hashCode();
        }
        if (getDirec_ext_loc() != null) {
            _hashCode += getDirec_ext_loc().hashCode();
        }
        if (getDirec_ext_rs() != null) {
            _hashCode += getDirec_ext_rs().hashCode();
        }
        if (getE_mail() != null) {
            _hashCode += getE_mail().hashCode();
        }
        if (getE_mail_rs() != null) {
            _hashCode += getE_mail_rs().hashCode();
        }
        if (getEmp_corr() != null) {
            _hashCode += getEmp_corr().hashCode();
        }
        if (getEscalera() != null) {
            _hashCode += getEscalera().hashCode();
        }
        if (getEscalera_ett() != null) {
            _hashCode += getEscalera_ett().hashCode();
        }
        if (getEscalera_rs() != null) {
            _hashCode += getEscalera_rs().hashCode();
        }
        if (getExiste() != null) {
            _hashCode += getExiste().hashCode();
        }
        _hashCode += (isExisteSISPE() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getExiste_ett() != null) {
            _hashCode += getExiste_ett().hashCode();
        }
        if (getFax() != null) {
            _hashCode += getFax().hashCode();
        }
        if (getFax_rs() != null) {
            _hashCode += getFax_rs().hashCode();
        }
        if (getFecCalif() != null) {
            _hashCode += getFecCalif().hashCode();
        }
        if (getFecDesac() != null) {
            _hashCode += getFecDesac().hashCode();
        }
        if (getFecha_fin_autorizacion() != null) {
            _hashCode += getFecha_fin_autorizacion().hashCode();
        }
        if (getFecha_inicio_autorizacion() != null) {
            _hashCode += getFecha_inicio_autorizacion().hashCode();
        }
        if (getFecha_ultima_modificacion() != null) {
            _hashCode += getFecha_ultima_modificacion().hashCode();
        }
        if (getGen_emp_fec_regprov() != null) {
            _hashCode += getGen_emp_fec_regprov().hashCode();
        }
        if (getGen_emp_ind_impsepe() != null) {
            _hashCode += getGen_emp_ind_impsepe().hashCode();
        }
        if (getGen_emp_ind_prov() != null) {
            _hashCode += getGen_emp_ind_prov().hashCode();
        }
        if (getIndBloq() != null) {
            _hashCode += getIndBloq().hashCode();
        }
        if (getIndSispe() != null) {
            _hashCode += getIndSispe().hashCode();
        }
        if (getLista_errores() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLista_errores());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLista_errores(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLista_ett() != null) {
            _hashCode += getLista_ett().hashCode();
        }
        if (getN_via() != null) {
            _hashCode += getN_via().hashCode();
        }
        if (getN_via_ett() != null) {
            _hashCode += getN_via_ett().hashCode();
        }
        if (getN_via_rs() != null) {
            _hashCode += getN_via_rs().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getNombre_comercial() != null) {
            _hashCode += getNombre_comercial().hashCode();
        }
        if (getNombre_contacto() != null) {
            _hashCode += getNombre_contacto().hashCode();
        }
        if (getNombre_contacto_rs() != null) {
            _hashCode += getNombre_contacto_rs().hashCode();
        }
        if (getNombre_via() != null) {
            _hashCode += getNombre_via().hashCode();
        }
        if (getNombre_via_ett() != null) {
            _hashCode += getNombre_via_ett().hashCode();
        }
        if (getNombre_via_rs() != null) {
            _hashCode += getNombre_via_rs().hashCode();
        }
        if (getNum_autorizacion() != null) {
            _hashCode += getNum_autorizacion().hashCode();
        }
        if (getNum_doc() != null) {
            _hashCode += getNum_doc().hashCode();
        }
        if (getNut_ext_loc() != null) {
            _hashCode += getNut_ext_loc().hashCode();
        }
        if (getNut_ext_rs() != null) {
            _hashCode += getNut_ext_rs().hashCode();
        }
        if (getPais() != null) {
            _hashCode += getPais().hashCode();
        }
        if (getPais_ext_loc() != null) {
            _hashCode += getPais_ext_loc().hashCode();
        }
        if (getPais_ext_rs() != null) {
            _hashCode += getPais_ext_rs().hashCode();
        }
        if (getPiso() != null) {
            _hashCode += getPiso().hashCode();
        }
        if (getPiso_ett() != null) {
            _hashCode += getPiso_ett().hashCode();
        }
        if (getPiso_rs() != null) {
            _hashCode += getPiso_rs().hashCode();
        }
        if (getPobl_ext_loc() != null) {
            _hashCode += getPobl_ext_loc().hashCode();
        }
        if (getPobl_ext_rs() != null) {
            _hashCode += getPobl_ext_rs().hashCode();
        }
        if (getPuerta() != null) {
            _hashCode += getPuerta().hashCode();
        }
        if (getPuerta_ett() != null) {
            _hashCode += getPuerta_ett().hashCode();
        }
        if (getPuerta_rs() != null) {
            _hashCode += getPuerta_rs().hashCode();
        }
        if (getRazon_social() != null) {
            _hashCode += getRazon_social().hashCode();
        }
        if (getTelefono() != null) {
            _hashCode += getTelefono().hashCode();
        }
        if (getTelefono_rs() != null) {
            _hashCode += getTelefono_rs().hashCode();
        }
        if (getTipo_empresa() != null) {
            _hashCode += getTipo_empresa().hashCode();
        }
        if (getTitularidad() != null) {
            _hashCode += getTitularidad().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EmpresarioWSValueObject.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langai.altia.es/business/personafisica", "EmpresarioWSValueObject"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellido1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apellido1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellido1_contacto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apellido1_contacto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellido1_contacto_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apellido1_contacto_rs"));
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
        elemField.setFieldName("apellido2_contacto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apellido2_contacto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellido2_contacto_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apellido2_contacto_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bis_duplicado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bis_duplicado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bis_duplicado_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bis_duplicado_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bis_duplicado_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bis_duplicado_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_municipio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_municipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_municipio_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_municipio_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_municipio_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_municipio_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_provincia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_provincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_provincia_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_provincia_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_provincia_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_provincia_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_tipo_empresa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_tipo_empresa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_tipovia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_tipovia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_tipovia_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_tipovia_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_tipovia_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_tipovia_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contraste_datos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contraste_datos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cp_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cp_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cp_ext_loc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cp_ext_loc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cp_ext_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cp_ext_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cp_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cp_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cuentaCot");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cuentaCot"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_muni_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_muni_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_muni_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_muni_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_muni_ett_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_muni_ett_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_muni_ett_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_muni_ett_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_muni_rs_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_muni_rs_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_muni_rs_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_muni_rs_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_prov_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_prov_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_prov_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_prov_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_prov_ett_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_prov_ett_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_prov_ett_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_prov_ett_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_prov_rs_c");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_prov_rs_c"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("desc_prov_rs_e");
        elemField.setXmlName(new javax.xml.namespace.QName("", "desc_prov_rs_e"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("direc_ext_loc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "direc_ext_loc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("direc_ext_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "direc_ext_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_mail");
        elemField.setXmlName(new javax.xml.namespace.QName("", "e_mail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("e_mail_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "e_mail_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emp_corr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "emp_corr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("escalera");
        elemField.setXmlName(new javax.xml.namespace.QName("", "escalera"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("escalera_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "escalera_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("escalera_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "escalera_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("existe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "existe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("existeSISPE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "existeSISPE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("existe_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "existe_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fax");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fax_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fax_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecCalif");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecCalif"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecDesac");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecDesac"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_fin_autorizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_fin_autorizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_inicio_autorizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_inicio_autorizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_ultima_modificacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha_ultima_modificacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gen_emp_fec_regprov");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gen_emp_fec_regprov"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gen_emp_ind_impsepe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gen_emp_ind_impsepe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gen_emp_ind_prov");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gen_emp_ind_prov"));
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
        elemField.setFieldName("indSispe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indSispe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lista_errores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lista_errores"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lista_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lista_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("n_via");
        elemField.setXmlName(new javax.xml.namespace.QName("", "n_via"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("n_via_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "n_via_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("n_via_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "n_via_rs"));
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
        elemField.setFieldName("nombre_comercial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre_comercial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre_contacto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre_contacto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre_contacto_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre_contacto_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre_via");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre_via"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre_via_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre_via_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre_via_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre_via_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_autorizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "num_autorizacion"));
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
        elemField.setFieldName("nut_ext_loc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nut_ext_loc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nut_ext_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nut_ext_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pais");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pais"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pais_ext_loc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pais_ext_loc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pais_ext_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pais_ext_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piso_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piso_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piso_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piso_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pobl_ext_loc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pobl_ext_loc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pobl_ext_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pobl_ext_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("puerta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "puerta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("puerta_ett");
        elemField.setXmlName(new javax.xml.namespace.QName("", "puerta_ett"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("puerta_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "puerta_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("razon_social");
        elemField.setXmlName(new javax.xml.namespace.QName("", "razon_social"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("", "telefono"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono_rs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "telefono_rs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo_empresa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo_empresa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titularidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "titularidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
