/**
 * SWPisa_DomiciliosBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.pisa.cliente;

public class SWPisa_DomiciliosBean  implements java.io.Serializable {
    private java.lang.String codigo_domicilio;

    private java.lang.String codigo_municipio;

    private java.lang.String codigo_postal;

    private java.lang.String codigo_provincia;

    private java.lang.String codigo_tercero;

    private java.lang.String domicilio;

    private java.lang.String municipio;

    public SWPisa_DomiciliosBean() {
    }

    public SWPisa_DomiciliosBean(
           java.lang.String codigo_domicilio,
           java.lang.String codigo_municipio,
           java.lang.String codigo_postal,
           java.lang.String codigo_provincia,
           java.lang.String codigo_tercero,
           java.lang.String domicilio,
           java.lang.String municipio) {
           this.codigo_domicilio = codigo_domicilio;
           this.codigo_municipio = codigo_municipio;
           this.codigo_postal = codigo_postal;
           this.codigo_provincia = codigo_provincia;
           this.codigo_tercero = codigo_tercero;
           this.domicilio = domicilio;
           this.municipio = municipio;
    }


    /**
     * Gets the codigo_domicilio value for this SWPisa_DomiciliosBean.
     * 
     * @return codigo_domicilio
     */
    public java.lang.String getCodigo_domicilio() {
        return codigo_domicilio;
    }


    /**
     * Sets the codigo_domicilio value for this SWPisa_DomiciliosBean.
     * 
     * @param codigo_domicilio
     */
    public void setCodigo_domicilio(java.lang.String codigo_domicilio) {
        this.codigo_domicilio = codigo_domicilio;
    }


    /**
     * Gets the codigo_municipio value for this SWPisa_DomiciliosBean.
     * 
     * @return codigo_municipio
     */
    public java.lang.String getCodigo_municipio() {
        return codigo_municipio;
    }


    /**
     * Sets the codigo_municipio value for this SWPisa_DomiciliosBean.
     * 
     * @param codigo_municipio
     */
    public void setCodigo_municipio(java.lang.String codigo_municipio) {
        this.codigo_municipio = codigo_municipio;
    }


    /**
     * Gets the codigo_postal value for this SWPisa_DomiciliosBean.
     * 
     * @return codigo_postal
     */
    public java.lang.String getCodigo_postal() {
        return codigo_postal;
    }


    /**
     * Sets the codigo_postal value for this SWPisa_DomiciliosBean.
     * 
     * @param codigo_postal
     */
    public void setCodigo_postal(java.lang.String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }


    /**
     * Gets the codigo_provincia value for this SWPisa_DomiciliosBean.
     * 
     * @return codigo_provincia
     */
    public java.lang.String getCodigo_provincia() {
        return codigo_provincia;
    }


    /**
     * Sets the codigo_provincia value for this SWPisa_DomiciliosBean.
     * 
     * @param codigo_provincia
     */
    public void setCodigo_provincia(java.lang.String codigo_provincia) {
        this.codigo_provincia = codigo_provincia;
    }


    /**
     * Gets the codigo_tercero value for this SWPisa_DomiciliosBean.
     * 
     * @return codigo_tercero
     */
    public java.lang.String getCodigo_tercero() {
        return codigo_tercero;
    }


    /**
     * Sets the codigo_tercero value for this SWPisa_DomiciliosBean.
     * 
     * @param codigo_tercero
     */
    public void setCodigo_tercero(java.lang.String codigo_tercero) {
        this.codigo_tercero = codigo_tercero;
    }


    /**
     * Gets the domicilio value for this SWPisa_DomiciliosBean.
     * 
     * @return domicilio
     */
    public java.lang.String getDomicilio() {
        return domicilio;
    }


    /**
     * Sets the domicilio value for this SWPisa_DomiciliosBean.
     * 
     * @param domicilio
     */
    public void setDomicilio(java.lang.String domicilio) {
        this.domicilio = domicilio;
    }


    /**
     * Gets the municipio value for this SWPisa_DomiciliosBean.
     * 
     * @return municipio
     */
    public java.lang.String getMunicipio() {
        return municipio;
    }


    /**
     * Sets the municipio value for this SWPisa_DomiciliosBean.
     * 
     * @param municipio
     */
    public void setMunicipio(java.lang.String municipio) {
        this.municipio = municipio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SWPisa_DomiciliosBean)) return false;
        SWPisa_DomiciliosBean other = (SWPisa_DomiciliosBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo_domicilio==null && other.getCodigo_domicilio()==null) || 
             (this.codigo_domicilio!=null &&
              this.codigo_domicilio.equals(other.getCodigo_domicilio()))) &&
            ((this.codigo_municipio==null && other.getCodigo_municipio()==null) || 
             (this.codigo_municipio!=null &&
              this.codigo_municipio.equals(other.getCodigo_municipio()))) &&
            ((this.codigo_postal==null && other.getCodigo_postal()==null) || 
             (this.codigo_postal!=null &&
              this.codigo_postal.equals(other.getCodigo_postal()))) &&
            ((this.codigo_provincia==null && other.getCodigo_provincia()==null) || 
             (this.codigo_provincia!=null &&
              this.codigo_provincia.equals(other.getCodigo_provincia()))) &&
            ((this.codigo_tercero==null && other.getCodigo_tercero()==null) || 
             (this.codigo_tercero!=null &&
              this.codigo_tercero.equals(other.getCodigo_tercero()))) &&
            ((this.domicilio==null && other.getDomicilio()==null) || 
             (this.domicilio!=null &&
              this.domicilio.equals(other.getDomicilio()))) &&
            ((this.municipio==null && other.getMunicipio()==null) || 
             (this.municipio!=null &&
              this.municipio.equals(other.getMunicipio())));
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
        if (getCodigo_domicilio() != null) {
            _hashCode += getCodigo_domicilio().hashCode();
        }
        if (getCodigo_municipio() != null) {
            _hashCode += getCodigo_municipio().hashCode();
        }
        if (getCodigo_postal() != null) {
            _hashCode += getCodigo_postal().hashCode();
        }
        if (getCodigo_provincia() != null) {
            _hashCode += getCodigo_provincia().hashCode();
        }
        if (getCodigo_tercero() != null) {
            _hashCode += getCodigo_tercero().hashCode();
        }
        if (getDomicilio() != null) {
            _hashCode += getDomicilio().hashCode();
        }
        if (getMunicipio() != null) {
            _hashCode += getMunicipio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SWPisa_DomiciliosBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_DomiciliosBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_domicilio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_domicilio"));
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
        elemField.setFieldName("codigo_postal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_postal"));
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
        elemField.setFieldName("codigo_tercero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_tercero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domicilio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "domicilio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("municipio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "municipio"));
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
