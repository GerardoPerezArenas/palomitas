/**
 * ReturnSWPistVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.registro.pist.cliente.datos;

public class ReturnSWPistVO  implements java.io.Serializable {
    private es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO annotationInfo;

    private java.lang.String descStatus;

    private es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO[] findedAnnotations;

    private es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationPersonInfoVO personInfo;

    private int status;

    public ReturnSWPistVO() {
    }

    public ReturnSWPistVO(
           es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO annotationInfo,
           java.lang.String descStatus,
           es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO[] findedAnnotations,
           es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationPersonInfoVO personInfo,
           int status) {
           this.annotationInfo = annotationInfo;
           this.descStatus = descStatus;
           this.findedAnnotations = findedAnnotations;
           this.personInfo = personInfo;
           this.status = status;
    }


    /**
     * Gets the annotationInfo value for this ReturnSWPistVO.
     * 
     * @return annotationInfo
     */
    public es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO getAnnotationInfo() {
        return annotationInfo;
    }


    /**
     * Sets the annotationInfo value for this ReturnSWPistVO.
     * 
     * @param annotationInfo
     */
    public void setAnnotationInfo(es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO annotationInfo) {
        this.annotationInfo = annotationInfo;
    }


    /**
     * Gets the descStatus value for this ReturnSWPistVO.
     * 
     * @return descStatus
     */
    public java.lang.String getDescStatus() {
        return descStatus;
    }


    /**
     * Sets the descStatus value for this ReturnSWPistVO.
     * 
     * @param descStatus
     */
    public void setDescStatus(java.lang.String descStatus) {
        this.descStatus = descStatus;
    }


    /**
     * Gets the findedAnnotations value for this ReturnSWPistVO.
     * 
     * @return findedAnnotations
     */
    public es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO[] getFindedAnnotations() {
        return findedAnnotations;
    }


    /**
     * Sets the findedAnnotations value for this ReturnSWPistVO.
     * 
     * @param findedAnnotations
     */
    public void setFindedAnnotations(es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO[] findedAnnotations) {
        this.findedAnnotations = findedAnnotations;
    }


    /**
     * Gets the personInfo value for this ReturnSWPistVO.
     * 
     * @return personInfo
     */
    public es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationPersonInfoVO getPersonInfo() {
        return personInfo;
    }


    /**
     * Sets the personInfo value for this ReturnSWPistVO.
     * 
     * @param personInfo
     */
    public void setPersonInfo(es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationPersonInfoVO personInfo) {
        this.personInfo = personInfo;
    }


    /**
     * Gets the status value for this ReturnSWPistVO.
     * 
     * @return status
     */
    public int getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ReturnSWPistVO.
     * 
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReturnSWPistVO)) return false;
        ReturnSWPistVO other = (ReturnSWPistVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.annotationInfo==null && other.getAnnotationInfo()==null) || 
             (this.annotationInfo!=null &&
              this.annotationInfo.equals(other.getAnnotationInfo()))) &&
            ((this.descStatus==null && other.getDescStatus()==null) || 
             (this.descStatus!=null &&
              this.descStatus.equals(other.getDescStatus()))) &&
            ((this.findedAnnotations==null && other.getFindedAnnotations()==null) || 
             (this.findedAnnotations!=null &&
              java.util.Arrays.equals(this.findedAnnotations, other.getFindedAnnotations()))) &&
            ((this.personInfo==null && other.getPersonInfo()==null) || 
             (this.personInfo!=null &&
              this.personInfo.equals(other.getPersonInfo()))) &&
            this.status == other.getStatus();
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
        if (getAnnotationInfo() != null) {
            _hashCode += getAnnotationInfo().hashCode();
        }
        if (getDescStatus() != null) {
            _hashCode += getDescStatus().hashCode();
        }
        if (getFindedAnnotations() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFindedAnnotations());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFindedAnnotations(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPersonInfo() != null) {
            _hashCode += getPersonInfo().hashCode();
        }
        _hashCode += getStatus();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReturnSWPistVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "ReturnSWPistVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("annotationInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "annotationInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "AnnotationInfoVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("findedAnnotations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "findedAnnotations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "AnnotationInfoVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "personInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://data.pist.ws.altia.es", "AnnotationPersonInfoVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
