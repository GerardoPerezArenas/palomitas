/**
 * SWPisa_ViasBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.pisa.cliente;

public class SWPisa_ViasBean  implements java.io.Serializable {
    private java.lang.String codigo_colectiva_ine;

    private java.lang.String codigo_municipio;

    private java.lang.String codigo_nucleo_ine;

    private java.lang.String codigo_provincia;

    private java.lang.String codigo_singular_ine;

    private java.lang.String codigo_tipo_via_ine;

    private java.lang.String codigo_via_ine;

    private java.lang.String nombre;

    public SWPisa_ViasBean() {
    }

    public SWPisa_ViasBean(
           java.lang.String codigo_colectiva_ine,
           java.lang.String codigo_municipio,
           java.lang.String codigo_nucleo_ine,
           java.lang.String codigo_provincia,
           java.lang.String codigo_singular_ine,
           java.lang.String codigo_tipo_via_ine,
           java.lang.String codigo_via_ine,
           java.lang.String nombre) {
           this.codigo_colectiva_ine = codigo_colectiva_ine;
           this.codigo_municipio = codigo_municipio;
           this.codigo_nucleo_ine = codigo_nucleo_ine;
           this.codigo_provincia = codigo_provincia;
           this.codigo_singular_ine = codigo_singular_ine;
           this.codigo_tipo_via_ine = codigo_tipo_via_ine;
           this.codigo_via_ine = codigo_via_ine;
           this.nombre = nombre;
    }


    /**
     * Gets the codigo_colectiva_ine value for this SWPisa_ViasBean.
     * 
     * @return codigo_colectiva_ine
     */
    public java.lang.String getCodigo_colectiva_ine() {
        return codigo_colectiva_ine;
    }


    /**
     * Sets the codigo_colectiva_ine value for this SWPisa_ViasBean.
     * 
     * @param codigo_colectiva_ine
     */
    public void setCodigo_colectiva_ine(java.lang.String codigo_colectiva_ine) {
        this.codigo_colectiva_ine = codigo_colectiva_ine;
    }


    /**
     * Gets the codigo_municipio value for this SWPisa_ViasBean.
     * 
     * @return codigo_municipio
     */
    public java.lang.String getCodigo_municipio() {
        return codigo_municipio;
    }


    /**
     * Sets the codigo_municipio value for this SWPisa_ViasBean.
     * 
     * @param codigo_municipio
     */
    public void setCodigo_municipio(java.lang.String codigo_municipio) {
        this.codigo_municipio = codigo_municipio;
    }


    /**
     * Gets the codigo_nucleo_ine value for this SWPisa_ViasBean.
     * 
     * @return codigo_nucleo_ine
     */
    public java.lang.String getCodigo_nucleo_ine() {
        return codigo_nucleo_ine;
    }


    /**
     * Sets the codigo_nucleo_ine value for this SWPisa_ViasBean.
     * 
     * @param codigo_nucleo_ine
     */
    public void setCodigo_nucleo_ine(java.lang.String codigo_nucleo_ine) {
        this.codigo_nucleo_ine = codigo_nucleo_ine;
    }


    /**
     * Gets the codigo_provincia value for this SWPisa_ViasBean.
     * 
     * @return codigo_provincia
     */
    public java.lang.String getCodigo_provincia() {
        return codigo_provincia;
    }


    /**
     * Sets the codigo_provincia value for this SWPisa_ViasBean.
     * 
     * @param codigo_provincia
     */
    public void setCodigo_provincia(java.lang.String codigo_provincia) {
        this.codigo_provincia = codigo_provincia;
    }


    /**
     * Gets the codigo_singular_ine value for this SWPisa_ViasBean.
     * 
     * @return codigo_singular_ine
     */
    public java.lang.String getCodigo_singular_ine() {
        return codigo_singular_ine;
    }


    /**
     * Sets the codigo_singular_ine value for this SWPisa_ViasBean.
     * 
     * @param codigo_singular_ine
     */
    public void setCodigo_singular_ine(java.lang.String codigo_singular_ine) {
        this.codigo_singular_ine = codigo_singular_ine;
    }


    /**
     * Gets the codigo_tipo_via_ine value for this SWPisa_ViasBean.
     * 
     * @return codigo_tipo_via_ine
     */
    public java.lang.String getCodigo_tipo_via_ine() {
        return codigo_tipo_via_ine;
    }


    /**
     * Sets the codigo_tipo_via_ine value for this SWPisa_ViasBean.
     * 
     * @param codigo_tipo_via_ine
     */
    public void setCodigo_tipo_via_ine(java.lang.String codigo_tipo_via_ine) {
        this.codigo_tipo_via_ine = codigo_tipo_via_ine;
    }


    /**
     * Gets the codigo_via_ine value for this SWPisa_ViasBean.
     * 
     * @return codigo_via_ine
     */
    public java.lang.String getCodigo_via_ine() {
        return codigo_via_ine;
    }


    /**
     * Sets the codigo_via_ine value for this SWPisa_ViasBean.
     * 
     * @param codigo_via_ine
     */
    public void setCodigo_via_ine(java.lang.String codigo_via_ine) {
        this.codigo_via_ine = codigo_via_ine;
    }


    /**
     * Gets the nombre value for this SWPisa_ViasBean.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this SWPisa_ViasBean.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SWPisa_ViasBean)) return false;
        SWPisa_ViasBean other = (SWPisa_ViasBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo_colectiva_ine==null && other.getCodigo_colectiva_ine()==null) || 
             (this.codigo_colectiva_ine!=null &&
              this.codigo_colectiva_ine.equals(other.getCodigo_colectiva_ine()))) &&
            ((this.codigo_municipio==null && other.getCodigo_municipio()==null) || 
             (this.codigo_municipio!=null &&
              this.codigo_municipio.equals(other.getCodigo_municipio()))) &&
            ((this.codigo_nucleo_ine==null && other.getCodigo_nucleo_ine()==null) || 
             (this.codigo_nucleo_ine!=null &&
              this.codigo_nucleo_ine.equals(other.getCodigo_nucleo_ine()))) &&
            ((this.codigo_provincia==null && other.getCodigo_provincia()==null) || 
             (this.codigo_provincia!=null &&
              this.codigo_provincia.equals(other.getCodigo_provincia()))) &&
            ((this.codigo_singular_ine==null && other.getCodigo_singular_ine()==null) || 
             (this.codigo_singular_ine!=null &&
              this.codigo_singular_ine.equals(other.getCodigo_singular_ine()))) &&
            ((this.codigo_tipo_via_ine==null && other.getCodigo_tipo_via_ine()==null) || 
             (this.codigo_tipo_via_ine!=null &&
              this.codigo_tipo_via_ine.equals(other.getCodigo_tipo_via_ine()))) &&
            ((this.codigo_via_ine==null && other.getCodigo_via_ine()==null) || 
             (this.codigo_via_ine!=null &&
              this.codigo_via_ine.equals(other.getCodigo_via_ine()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCodigo_colectiva_ine() != null) {
            _hashCode += getCodigo_colectiva_ine().hashCode();
        }
        if (getCodigo_municipio() != null) {
            _hashCode += getCodigo_municipio().hashCode();
        }
        if (getCodigo_nucleo_ine() != null) {
            _hashCode += getCodigo_nucleo_ine().hashCode();
        }
        if (getCodigo_provincia() != null) {
            _hashCode += getCodigo_provincia().hashCode();
        }
        if (getCodigo_singular_ine() != null) {
            _hashCode += getCodigo_singular_ine().hashCode();
        }
        if (getCodigo_tipo_via_ine() != null) {
            _hashCode += getCodigo_tipo_via_ine().hashCode();
        }
        if (getCodigo_via_ine() != null) {
            _hashCode += getCodigo_via_ine().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SWPisa_ViasBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_ViasBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_colectiva_ine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_colectiva_ine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_municipio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_municipio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_nucleo_ine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_nucleo_ine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_provincia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_provincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_singular_ine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_singular_ine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_tipo_via_ine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_tipo_via_ine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_via_ine");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_via_ine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre"));
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
