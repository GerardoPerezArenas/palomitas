/**
 * InfoConexionVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.tramitacion.bd.datos;

public class InfoConexionVO  implements java.io.Serializable {
    private java.lang.String aplicacion;

    private java.lang.String organizacion;

    public InfoConexionVO() {
    }

    public InfoConexionVO(
           java.lang.String aplicacion,
           java.lang.String organizacion) {
           this.aplicacion = aplicacion;
           this.organizacion = organizacion;
    }


    /**
     * Gets the aplicacion value for this InfoConexionVO.
     * 
     * @return aplicacion
     */
    public java.lang.String getAplicacion() {
        return aplicacion;
    }


    /**
     * Sets the aplicacion value for this InfoConexionVO.
     * 
     * @param aplicacion
     */
    public void setAplicacion(java.lang.String aplicacion) {
        this.aplicacion = aplicacion;
    }


    /**
     * Gets the organizacion value for this InfoConexionVO.
     * 
     * @return organizacion
     */
    public java.lang.String getOrganizacion() {
        return organizacion;
    }


    /**
     * Sets the organizacion value for this InfoConexionVO.
     * 
     * @param organizacion
     */
    public void setOrganizacion(java.lang.String organizacion) {
        this.organizacion = organizacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InfoConexionVO)) return false;
        InfoConexionVO other = (InfoConexionVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.aplicacion==null && other.getAplicacion()==null) || 
             (this.aplicacion!=null &&
              this.aplicacion.equals(other.getAplicacion()))) &&
            ((this.organizacion==null && other.getOrganizacion()==null) || 
             (this.organizacion!=null &&
              this.organizacion.equals(other.getOrganizacion())));
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
        if (getAplicacion() != null) {
            _hashCode += getAplicacion().hashCode();
        }
        if (getOrganizacion() != null) {
            _hashCode += getOrganizacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InfoConexionVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "InfoConexionVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "aplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "organizacion"));
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
