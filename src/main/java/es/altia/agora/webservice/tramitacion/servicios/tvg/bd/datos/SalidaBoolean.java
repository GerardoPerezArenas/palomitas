/**
 * SalidaBoolean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos;

public class SalidaBoolean  implements java.io.Serializable {
    private java.lang.String[] incidencias;

    private java.lang.Boolean resultado;

    public SalidaBoolean() {
    }

    public SalidaBoolean(
           java.lang.String[] incidencias,
           java.lang.Boolean resultado) {
           this.incidencias = incidencias;
           this.resultado = resultado;
    }


    /**
     * Gets the incidencias value for this SalidaBoolean.
     * 
     * @return incidencias
     */
    public java.lang.String[] getIncidencias() {
        return incidencias;
    }


    /**
     * Sets the incidencias value for this SalidaBoolean.
     * 
     * @param incidencias
     */
    public void setIncidencias(java.lang.String[] incidencias) {
        this.incidencias = incidencias;
    }


    /**
     * Gets the resultado value for this SalidaBoolean.
     * 
     * @return resultado
     */
    public java.lang.Boolean getResultado() {
        return resultado;
    }


    /**
     * Sets the resultado value for this SalidaBoolean.
     * 
     * @param resultado
     */
    public void setResultado(java.lang.Boolean resultado) {
        this.resultado = resultado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SalidaBoolean)) return false;
        SalidaBoolean other = (SalidaBoolean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.incidencias==null && other.getIncidencias()==null) || 
             (this.incidencias!=null &&
              java.util.Arrays.equals(this.incidencias, other.getIncidencias()))) &&
            ((this.resultado==null && other.getResultado()==null) || 
             (this.resultado!=null &&
              this.resultado.equals(other.getResultado())));
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
        if (getIncidencias() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIncidencias());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIncidencias(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getResultado() != null) {
            _hashCode += getResultado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SalidaBoolean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tvg.servicios.tramitacion.webservice.agora.altia.es", "SalidaBoolean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incidencias");
        elemField.setXmlName(new javax.xml.namespace.QName("", "incidencias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
