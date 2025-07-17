/**
 * SWFirmadoc_RetornoBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc;

public class SWFirmadoc_RetornoBean  extends es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.BaseBean  implements java.io.Serializable {
    private java.lang.String base64;
    private java.lang.String codigo_documento;
    private java.lang.String codigo_expediente;
    private java.lang.String estado_firma;
    private boolean resultado;

    public SWFirmadoc_RetornoBean() {
    }

    public SWFirmadoc_RetornoBean(
           java.lang.String base64,
           java.lang.String codigo_documento,
           java.lang.String codigo_expediente,
           java.lang.String estado_firma,
           boolean resultado) {
           this.base64 = base64;
           this.codigo_documento = codigo_documento;
           this.codigo_expediente = codigo_expediente;
           this.estado_firma = estado_firma;
           this.resultado = resultado;
    }


    /**
     * Gets the base64 value for this SWFirmadoc_RetornoBean.
     * 
     * @return base64
     */
    public java.lang.String getBase64() {
        return base64;
    }


    /**
     * Sets the base64 value for this SWFirmadoc_RetornoBean.
     * 
     * @param base64
     */
    public void setBase64(java.lang.String base64) {
        this.base64 = base64;
    }


    /**
     * Gets the codigo_documento value for this SWFirmadoc_RetornoBean.
     * 
     * @return codigo_documento
     */
    public java.lang.String getCodigo_documento() {
        return codigo_documento;
    }


    /**
     * Sets the codigo_documento value for this SWFirmadoc_RetornoBean.
     * 
     * @param codigo_documento
     */
    public void setCodigo_documento(java.lang.String codigo_documento) {
        this.codigo_documento = codigo_documento;
    }


    /**
     * Gets the codigo_expediente value for this SWFirmadoc_RetornoBean.
     * 
     * @return codigo_expediente
     */
    public java.lang.String getCodigo_expediente() {
        return codigo_expediente;
    }


    /**
     * Sets the codigo_expediente value for this SWFirmadoc_RetornoBean.
     * 
     * @param codigo_expediente
     */
    public void setCodigo_expediente(java.lang.String codigo_expediente) {
        this.codigo_expediente = codigo_expediente;
    }


    /**
     * Gets the estado_firma value for this SWFirmadoc_RetornoBean.
     * 
     * @return estado_firma
     */
    public java.lang.String getEstado_firma() {
        return estado_firma;
    }


    /**
     * Sets the estado_firma value for this SWFirmadoc_RetornoBean.
     * 
     * @param estado_firma
     */
    public void setEstado_firma(java.lang.String estado_firma) {
        this.estado_firma = estado_firma;
    }


    /**
     * Gets the resultado value for this SWFirmadoc_RetornoBean.
     * 
     * @return resultado
     */
    public boolean isResultado() {
        return resultado;
    }


    /**
     * Sets the resultado value for this SWFirmadoc_RetornoBean.
     * 
     * @param resultado
     */
    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SWFirmadoc_RetornoBean)) return false;
        SWFirmadoc_RetornoBean other = (SWFirmadoc_RetornoBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.base64==null && other.getBase64()==null) || 
             (this.base64!=null &&
              this.base64.equals(other.getBase64()))) &&
            ((this.codigo_documento==null && other.getCodigo_documento()==null) || 
             (this.codigo_documento!=null &&
              this.codigo_documento.equals(other.getCodigo_documento()))) &&
            ((this.codigo_expediente==null && other.getCodigo_expediente()==null) || 
             (this.codigo_expediente!=null &&
              this.codigo_expediente.equals(other.getCodigo_expediente()))) &&
            ((this.estado_firma==null && other.getEstado_firma()==null) || 
             (this.estado_firma!=null &&
              this.estado_firma.equals(other.getEstado_firma()))) &&
            this.resultado == other.isResultado();
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
        if (getBase64() != null) {
            _hashCode += getBase64().hashCode();
        }
        if (getCodigo_documento() != null) {
            _hashCode += getCodigo_documento().hashCode();
        }
        if (getCodigo_expediente() != null) {
            _hashCode += getCodigo_expediente().hashCode();
        }
        if (getEstado_firma() != null) {
            _hashCode += getEstado_firma().hashCode();
        }
        _hashCode += (isResultado() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SWFirmadoc_RetornoBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://firmadoc.sw.aytos", "SWFirmadoc_RetornoBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("base64");
        elemField.setXmlName(new javax.xml.namespace.QName("", "base64"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_documento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_documento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo_expediente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo_expediente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado_firma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estado_firma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
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
