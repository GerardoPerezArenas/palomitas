/**
 * SalidaCodigoCSV.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos.bd.datos;

public class SalidaCodigoCSV  implements java.io.Serializable {
    private es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO estado;

    private java.lang.String csv;

    private java.lang.String token;

    public SalidaCodigoCSV() {
    }

    public SalidaCodigoCSV(
           es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO estado,
           java.lang.String csv,
           java.lang.String token) {
           this.estado = estado;
           this.csv = csv;
           this.token = token;
    }


    /**
     * Gets the estado value for this SalidaCodigoCSV.
     * 
     * @return estado
     */
    public es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this SalidaCodigoCSV.
     * 
     * @param estado
     */
    public void setEstado(es.altia.flexiaWS.documentos.bd.datos.EstadoOperacionVO estado) {
        this.estado = estado;
    }


    /**
     * Gets the csv value for this SalidaCodigoCSV.
     * 
     * @return csv
     */
    public java.lang.String getCsv() {
        return csv;
    }


    /**
     * Sets the csv value for this SalidaCodigoCSV.
     * 
     * @param csv
     */
    public void setCsv(java.lang.String csv) {
        this.csv = csv;
    }


    /**
     * Gets the token value for this SalidaCodigoCSV.
     * 
     * @return token
     */
    public java.lang.String getToken() {
        return token;
    }


    /**
     * Sets the token value for this SalidaCodigoCSV.
     * 
     * @param token
     */
    public void setToken(java.lang.String token) {
        this.token = token;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SalidaCodigoCSV)) return false;
        SalidaCodigoCSV other = (SalidaCodigoCSV) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            ((this.csv==null && other.getCsv()==null) || 
             (this.csv!=null &&
              this.csv.equals(other.getCsv()))) &&
            ((this.token==null && other.getToken()==null) || 
             (this.token!=null &&
              this.token.equals(other.getToken())));
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
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        if (getCsv() != null) {
            _hashCode += getCsv().hashCode();
        }
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SalidaCodigoCSV.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "SalidaCodigoCSV"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "EstadoOperacionVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("csv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "csv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("", "token"));
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
