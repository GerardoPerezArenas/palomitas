/**
 * QueryEnhancedUsers.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.query.request;

public class QueryEnhancedUsers  implements java.io.Serializable {
    private es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication;

    private java.lang.String queryUser;

    private java.lang.String querySeat;

    public QueryEnhancedUsers() {
    }

    public QueryEnhancedUsers(
           es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication,
           java.lang.String queryUser,
           java.lang.String querySeat) {
           this.authentication = authentication;
           this.queryUser = queryUser;
           this.querySeat = querySeat;
    }


    /**
     * Gets the authentication value for this QueryEnhancedUsers.
     * 
     * @return authentication
     */
    public es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication getAuthentication() {
        return authentication;
    }


    /**
     * Sets the authentication value for this QueryEnhancedUsers.
     * 
     * @param authentication
     */
    public void setAuthentication(es.altia.flexia.portafirmasexternocliente.plugin.sanse.queryservice.client.juntadeandalucia.cice.pfirma.type.Authentication authentication) {
        this.authentication = authentication;
    }


    /**
     * Gets the queryUser value for this QueryEnhancedUsers.
     * 
     * @return queryUser
     */
    public java.lang.String getQueryUser() {
        return queryUser;
    }


    /**
     * Sets the queryUser value for this QueryEnhancedUsers.
     * 
     * @param queryUser
     */
    public void setQueryUser(java.lang.String queryUser) {
        this.queryUser = queryUser;
    }


    /**
     * Gets the querySeat value for this QueryEnhancedUsers.
     * 
     * @return querySeat
     */
    public java.lang.String getQuerySeat() {
        return querySeat;
    }


    /**
     * Sets the querySeat value for this QueryEnhancedUsers.
     * 
     * @param querySeat
     */
    public void setQuerySeat(java.lang.String querySeat) {
        this.querySeat = querySeat;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryEnhancedUsers)) return false;
        QueryEnhancedUsers other = (QueryEnhancedUsers) obj;
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
            ((this.queryUser==null && other.getQueryUser()==null) || 
             (this.queryUser!=null &&
              this.queryUser.equals(other.getQueryUser()))) &&
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
        if (getQueryUser() != null) {
            _hashCode += getQueryUser().hashCode();
        }
        if (getQuerySeat() != null) {
            _hashCode += getQuerySeat().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryEnhancedUsers.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:query:request:v2.0", ">queryEnhancedUsers"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authentication");
        elemField.setXmlName(new javax.xml.namespace.QName("", "authentication"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:juntadeandalucia:cice:pfirma:type:v2.0", "authentication"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryUser");
        elemField.setXmlName(new javax.xml.namespace.QName("", "queryUser"));
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
