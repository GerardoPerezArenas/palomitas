/**
 * QueryEnhancedJobs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request;

public class QueryEnhancedJobs  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication;

    private java.lang.String queryJob;

    private java.lang.String querySeat;

    public QueryEnhancedJobs() {
    }

    public QueryEnhancedJobs(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication,
           java.lang.String queryJob,
           java.lang.String querySeat) {
           this.authentication = authentication;
           this.queryJob = queryJob;
           this.querySeat = querySeat;
    }


    /**
     * Gets the authentication value for this QueryEnhancedJobs.
     * 
     * @return authentication
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication getAuthentication() {
        return authentication;
    }


    /**
     * Sets the authentication value for this QueryEnhancedJobs.
     * 
     * @param authentication
     */
    public void setAuthentication(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication) {
        this.authentication = authentication;
    }


    /**
     * Gets the queryJob value for this QueryEnhancedJobs.
     * 
     * @return queryJob
     */
    public java.lang.String getQueryJob() {
        return queryJob;
    }


    /**
     * Sets the queryJob value for this QueryEnhancedJobs.
     * 
     * @param queryJob
     */
    public void setQueryJob(java.lang.String queryJob) {
        this.queryJob = queryJob;
    }


    /**
     * Gets the querySeat value for this QueryEnhancedJobs.
     * 
     * @return querySeat
     */
    public java.lang.String getQuerySeat() {
        return querySeat;
    }


    /**
     * Sets the querySeat value for this QueryEnhancedJobs.
     * 
     * @param querySeat
     */
    public void setQuerySeat(java.lang.String querySeat) {
        this.querySeat = querySeat;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryEnhancedJobs)) return false;
        QueryEnhancedJobs other = (QueryEnhancedJobs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.authentication==null && other.getAuthentication()==null) || 
             (this.authentication!=null &&
              this.authentication.equals(other.getAuthentication()))) &&
            ((this.queryJob==null && other.getQueryJob()==null) || 
             (this.queryJob!=null &&
              this.queryJob.equals(other.getQueryJob()))) &&
            ((this.querySeat==null && other.getQuerySeat()==null) || 
             (this.querySeat!=null &&
              this.querySeat.equals(other.getQuerySeat())));
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
        if (getAuthentication() != null) {
            _hashCode += getAuthentication().hashCode();
        }
        if (getQueryJob() != null) {
            _hashCode += getQueryJob().hashCode();
        }
        if (getQuerySeat() != null) {
            _hashCode += getQuerySeat().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryEnhancedJobs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedJobs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authentication");
        elemField.setXmlName(new javax.xml.namespace.QName("", "authentication"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "authentication"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryJob");
        elemField.setXmlName(new javax.xml.namespace.QName("", "queryJob"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("querySeat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "querySeat"));
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
