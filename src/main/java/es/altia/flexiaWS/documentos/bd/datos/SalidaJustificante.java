/**
 * SalidaJustificante.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos.bd.datos;

public class SalidaJustificante  implements java.io.Serializable {
    private java.lang.Integer codigoError;

    private byte[] fichero;

    private java.lang.String incidencias;

    public SalidaJustificante() {
    }

    public SalidaJustificante(
           java.lang.Integer codigoError,
           byte[] fichero,
           java.lang.String incidencias) {
           this.codigoError = codigoError;
           this.fichero = fichero;
           this.incidencias = incidencias;
    }


    /**
     * Gets the codigoError value for this SalidaJustificante.
     * 
     * @return codigoError
     */
    public java.lang.Integer getCodigoError() {
        return codigoError;
    }


    /**
     * Sets the codigoError value for this SalidaJustificante.
     * 
     * @param codigoError
     */
    public void setCodigoError(java.lang.Integer codigoError) {
        this.codigoError = codigoError;
    }


    /**
     * Gets the fichero value for this SalidaJustificante.
     * 
     * @return fichero
     */
    public byte[] getFichero() {
        return fichero;
    }


    /**
     * Sets the fichero value for this SalidaJustificante.
     * 
     * @param fichero
     */
    public void setFichero(byte[] fichero) {
        this.fichero = fichero;
    }


    /**
     * Gets the incidencias value for this SalidaJustificante.
     * 
     * @return incidencias
     */
    public java.lang.String getIncidencias() {
        return incidencias;
    }


    /**
     * Sets the incidencias value for this SalidaJustificante.
     * 
     * @param incidencias
     */
    public void setIncidencias(java.lang.String incidencias) {
        this.incidencias = incidencias;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SalidaJustificante)) return false;
        SalidaJustificante other = (SalidaJustificante) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoError==null && other.getCodigoError()==null) || 
             (this.codigoError!=null &&
              this.codigoError.equals(other.getCodigoError()))) &&
            ((this.fichero==null && other.getFichero()==null) || 
             (this.fichero!=null &&
              java.util.Arrays.equals(this.fichero, other.getFichero()))) &&
            ((this.incidencias==null && other.getIncidencias()==null) || 
             (this.incidencias!=null &&
              this.incidencias.equals(other.getIncidencias())));
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
        if (getCodigoError() != null) {
            _hashCode += getCodigoError().hashCode();
        }
        if (getFichero() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFichero());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFichero(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIncidencias() != null) {
            _hashCode += getIncidencias().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SalidaJustificante.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "SalidaJustificante"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fichero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fichero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incidencias");
        elemField.setXmlName(new javax.xml.namespace.QName("", "incidencias"));
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
