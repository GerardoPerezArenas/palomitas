/**
 * BaseBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.registro.pisa.cliente.datos;

public class BaseBean  implements java.io.Serializable {
    private boolean busquedaExactaAYU;

    private java.lang.String ciBusquedaExactaAYU;

    private int error;

    private int generar;

    private java.lang.String textoError;

    public BaseBean() {
    }

    public BaseBean(
           boolean busquedaExactaAYU,
           java.lang.String ciBusquedaExactaAYU,
           int error,
           int generar,
           java.lang.String textoError) {
           this.busquedaExactaAYU = busquedaExactaAYU;
           this.ciBusquedaExactaAYU = ciBusquedaExactaAYU;
           this.error = error;
           this.generar = generar;
           this.textoError = textoError;
    }


    /**
     * Gets the busquedaExactaAYU value for this BaseBean.
     * 
     * @return busquedaExactaAYU
     */
    public boolean isBusquedaExactaAYU() {
        return busquedaExactaAYU;
    }


    /**
     * Sets the busquedaExactaAYU value for this BaseBean.
     * 
     * @param busquedaExactaAYU
     */
    public void setBusquedaExactaAYU(boolean busquedaExactaAYU) {
        this.busquedaExactaAYU = busquedaExactaAYU;
    }


    /**
     * Gets the ciBusquedaExactaAYU value for this BaseBean.
     * 
     * @return ciBusquedaExactaAYU
     */
    public java.lang.String getCiBusquedaExactaAYU() {
        return ciBusquedaExactaAYU;
    }


    /**
     * Sets the ciBusquedaExactaAYU value for this BaseBean.
     * 
     * @param ciBusquedaExactaAYU
     */
    public void setCiBusquedaExactaAYU(java.lang.String ciBusquedaExactaAYU) {
        this.ciBusquedaExactaAYU = ciBusquedaExactaAYU;
    }


    /**
     * Gets the error value for this BaseBean.
     * 
     * @return error
     */
    public int getError() {
        return error;
    }


    /**
     * Sets the error value for this BaseBean.
     * 
     * @param error
     */
    public void setError(int error) {
        this.error = error;
    }


    /**
     * Gets the generar value for this BaseBean.
     * 
     * @return generar
     */
    public int getGenerar() {
        return generar;
    }


    /**
     * Sets the generar value for this BaseBean.
     * 
     * @param generar
     */
    public void setGenerar(int generar) {
        this.generar = generar;
    }


    /**
     * Gets the textoError value for this BaseBean.
     * 
     * @return textoError
     */
    public java.lang.String getTextoError() {
        return textoError;
    }


    /**
     * Sets the textoError value for this BaseBean.
     * 
     * @param textoError
     */
    public void setTextoError(java.lang.String textoError) {
        this.textoError = textoError;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BaseBean)) return false;
        BaseBean other = (BaseBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.busquedaExactaAYU == other.isBusquedaExactaAYU() &&
            ((this.ciBusquedaExactaAYU==null && other.getCiBusquedaExactaAYU()==null) || 
             (this.ciBusquedaExactaAYU!=null &&
              this.ciBusquedaExactaAYU.equals(other.getCiBusquedaExactaAYU()))) &&
            this.error == other.getError() &&
            this.generar == other.getGenerar() &&
            ((this.textoError==null && other.getTextoError()==null) || 
             (this.textoError!=null &&
              this.textoError.equals(other.getTextoError())));
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
        _hashCode += (isBusquedaExactaAYU() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getCiBusquedaExactaAYU() != null) {
            _hashCode += getCiBusquedaExactaAYU().hashCode();
        }
        _hashCode += getError();
        _hashCode += getGenerar();
        if (getTextoError() != null) {
            _hashCode += getTextoError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BaseBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://negocio.util.aytos", "BaseBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("busquedaExactaAYU");
        elemField.setXmlName(new javax.xml.namespace.QName("", "busquedaExactaAYU"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ciBusquedaExactaAYU");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ciBusquedaExactaAYU"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("error");
        elemField.setXmlName(new javax.xml.namespace.QName("", "error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("generar");
        elemField.setXmlName(new javax.xml.namespace.QName("", "generar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textoError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "textoError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
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
