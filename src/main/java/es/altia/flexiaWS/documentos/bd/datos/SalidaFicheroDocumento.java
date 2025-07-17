/**
 * SalidaFicheroDocumento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos.bd.datos;

public class SalidaFicheroDocumento  implements java.io.Serializable {
    private java.lang.Integer codigoError;

    private java.lang.String incidencias;

    private es.altia.flexiaWS.documentos.bd.datos.FicheroDocumentoVO resultado;

    public SalidaFicheroDocumento() {
    }

    public SalidaFicheroDocumento(
           java.lang.Integer codigoError,
           java.lang.String incidencias,
           es.altia.flexiaWS.documentos.bd.datos.FicheroDocumentoVO resultado) {
           this.codigoError = codigoError;
           this.incidencias = incidencias;
           this.resultado = resultado;
    }


    /**
     * Gets the codigoError value for this SalidaFicheroDocumento.
     * 
     * @return codigoError
     */
    public java.lang.Integer getCodigoError() {
        return codigoError;
    }


    /**
     * Sets the codigoError value for this SalidaFicheroDocumento.
     * 
     * @param codigoError
     */
    public void setCodigoError(java.lang.Integer codigoError) {
        this.codigoError = codigoError;
    }


    /**
     * Gets the incidencias value for this SalidaFicheroDocumento.
     * 
     * @return incidencias
     */
    public java.lang.String getIncidencias() {
        return incidencias;
    }


    /**
     * Sets the incidencias value for this SalidaFicheroDocumento.
     * 
     * @param incidencias
     */
    public void setIncidencias(java.lang.String incidencias) {
        this.incidencias = incidencias;
    }


    /**
     * Gets the resultado value for this SalidaFicheroDocumento.
     * 
     * @return resultado
     */
    public es.altia.flexiaWS.documentos.bd.datos.FicheroDocumentoVO getResultado() {
        return resultado;
    }


    /**
     * Sets the resultado value for this SalidaFicheroDocumento.
     * 
     * @param resultado
     */
    public void setResultado(es.altia.flexiaWS.documentos.bd.datos.FicheroDocumentoVO resultado) {
        this.resultado = resultado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SalidaFicheroDocumento)) return false;
        SalidaFicheroDocumento other = (SalidaFicheroDocumento) obj;
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
            ((this.incidencias==null && other.getIncidencias()==null) || 
             (this.incidencias!=null &&
              this.incidencias.equals(other.getIncidencias()))) &&
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
        if (getCodigoError() != null) {
            _hashCode += getCodigoError().hashCode();
        }
        if (getIncidencias() != null) {
            _hashCode += getIncidencias().hashCode();
        }
        if (getResultado() != null) {
            _hashCode += getResultado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SalidaFicheroDocumento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "SalidaFicheroDocumento"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incidencias");
        elemField.setXmlName(new javax.xml.namespace.QName("", "incidencias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "FicheroDocumentoVO"));
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
