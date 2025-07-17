/**
 * FlujoFinalizacionVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.tramitacion.bd.datos;

public class FlujoFinalizacionVO  implements java.io.Serializable {
    private es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO[] listaRespuesta;

    private int tipoApertura;

    public FlujoFinalizacionVO() {
    }

    public FlujoFinalizacionVO(
           es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO[] listaRespuesta,
           int tipoApertura) {
           this.listaRespuesta = listaRespuesta;
           this.tipoApertura = tipoApertura;
    }


    /**
     * Gets the listaRespuesta value for this FlujoFinalizacionVO.
     * 
     * @return listaRespuesta
     */
    public es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO[] getListaRespuesta() {
        return listaRespuesta;
    }


    /**
     * Sets the listaRespuesta value for this FlujoFinalizacionVO.
     * 
     * @param listaRespuesta
     */
    public void setListaRespuesta(es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO[] listaRespuesta) {
        this.listaRespuesta = listaRespuesta;
    }


    /**
     * Gets the tipoApertura value for this FlujoFinalizacionVO.
     * 
     * @return tipoApertura
     */
    public int getTipoApertura() {
        return tipoApertura;
    }


    /**
     * Sets the tipoApertura value for this FlujoFinalizacionVO.
     * 
     * @param tipoApertura
     */
    public void setTipoApertura(int tipoApertura) {
        this.tipoApertura = tipoApertura;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FlujoFinalizacionVO)) return false;
        FlujoFinalizacionVO other = (FlujoFinalizacionVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.listaRespuesta==null && other.getListaRespuesta()==null) || 
             (this.listaRespuesta!=null &&
              java.util.Arrays.equals(this.listaRespuesta, other.getListaRespuesta()))) &&
            this.tipoApertura == other.getTipoApertura();
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
        if (getListaRespuesta() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListaRespuesta());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListaRespuesta(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getTipoApertura();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FlujoFinalizacionVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "FlujoFinalizacionVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listaRespuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listaRespuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "TramiteVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoApertura");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoApertura"));
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
