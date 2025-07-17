/**
 * SWPisa_RetornoBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.pisa.cliente;

public class SWPisa_RetornoBean  extends es.altia.agora.webservice.tercero.servicios.pisa.cliente.BaseBean  implements java.io.Serializable {
    private es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_AnotacionesBean[] anotaciones;

    private es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_DomiciliosBean[] domicilios;

    private java.lang.String ejercicio;

    private java.lang.String fecha;

    private java.lang.String numero;

    private boolean resultado;

    private es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_TercerosBean[] terceros;

    private es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_ViasBean[] vias;

    public SWPisa_RetornoBean() {
    }

    public SWPisa_RetornoBean(
           int error,
           int generar,
           java.lang.String textoError,
           es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_AnotacionesBean[] anotaciones,
           es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_DomiciliosBean[] domicilios,
           java.lang.String ejercicio,
           java.lang.String fecha,
           java.lang.String numero,
           boolean resultado,
           es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_TercerosBean[] terceros,
           es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_ViasBean[] vias) {
        super(
            error,
            generar,
            textoError);
        this.anotaciones = anotaciones;
        this.domicilios = domicilios;
        this.ejercicio = ejercicio;
        this.fecha = fecha;
        this.numero = numero;
        this.resultado = resultado;
        this.terceros = terceros;
        this.vias = vias;
    }


    /**
     * Gets the anotaciones value for this SWPisa_RetornoBean.
     * 
     * @return anotaciones
     */
    public es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_AnotacionesBean[] getAnotaciones() {
        return anotaciones;
    }


    /**
     * Sets the anotaciones value for this SWPisa_RetornoBean.
     * 
     * @param anotaciones
     */
    public void setAnotaciones(es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_AnotacionesBean[] anotaciones) {
        this.anotaciones = anotaciones;
    }


    /**
     * Gets the domicilios value for this SWPisa_RetornoBean.
     * 
     * @return domicilios
     */
    public es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_DomiciliosBean[] getDomicilios() {
        return domicilios;
    }


    /**
     * Sets the domicilios value for this SWPisa_RetornoBean.
     * 
     * @param domicilios
     */
    public void setDomicilios(es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_DomiciliosBean[] domicilios) {
        this.domicilios = domicilios;
    }


    /**
     * Gets the ejercicio value for this SWPisa_RetornoBean.
     * 
     * @return ejercicio
     */
    public java.lang.String getEjercicio() {
        return ejercicio;
    }


    /**
     * Sets the ejercicio value for this SWPisa_RetornoBean.
     * 
     * @param ejercicio
     */
    public void setEjercicio(java.lang.String ejercicio) {
        this.ejercicio = ejercicio;
    }


    /**
     * Gets the fecha value for this SWPisa_RetornoBean.
     * 
     * @return fecha
     */
    public java.lang.String getFecha() {
        return fecha;
    }


    /**
     * Sets the fecha value for this SWPisa_RetornoBean.
     * 
     * @param fecha
     */
    public void setFecha(java.lang.String fecha) {
        this.fecha = fecha;
    }


    /**
     * Gets the numero value for this SWPisa_RetornoBean.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this SWPisa_RetornoBean.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the resultado value for this SWPisa_RetornoBean.
     * 
     * @return resultado
     */
    public boolean isResultado() {
        return resultado;
    }


    /**
     * Sets the resultado value for this SWPisa_RetornoBean.
     * 
     * @param resultado
     */
    public void setResultado(boolean resultado) {
        this.resultado = resultado;
    }


    /**
     * Gets the terceros value for this SWPisa_RetornoBean.
     * 
     * @return terceros
     */
    public es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_TercerosBean[] getTerceros() {
        return terceros;
    }


    /**
     * Sets the terceros value for this SWPisa_RetornoBean.
     * 
     * @param terceros
     */
    public void setTerceros(es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_TercerosBean[] terceros) {
        this.terceros = terceros;
    }


    /**
     * Gets the vias value for this SWPisa_RetornoBean.
     * 
     * @return vias
     */
    public es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_ViasBean[] getVias() {
        return vias;
    }


    /**
     * Sets the vias value for this SWPisa_RetornoBean.
     * 
     * @param vias
     */
    public void setVias(es.altia.agora.webservice.tercero.servicios.pisa.cliente.SWPisa_ViasBean[] vias) {
        this.vias = vias;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SWPisa_RetornoBean)) return false;
        SWPisa_RetornoBean other = (SWPisa_RetornoBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.anotaciones==null && other.getAnotaciones()==null) || 
             (this.anotaciones!=null &&
              java.util.Arrays.equals(this.anotaciones, other.getAnotaciones()))) &&
            ((this.domicilios==null && other.getDomicilios()==null) || 
             (this.domicilios!=null &&
              java.util.Arrays.equals(this.domicilios, other.getDomicilios()))) &&
            ((this.ejercicio==null && other.getEjercicio()==null) || 
             (this.ejercicio!=null &&
              this.ejercicio.equals(other.getEjercicio()))) &&
            ((this.fecha==null && other.getFecha()==null) || 
             (this.fecha!=null &&
              this.fecha.equals(other.getFecha()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            this.resultado == other.isResultado() &&
            ((this.terceros==null && other.getTerceros()==null) || 
             (this.terceros!=null &&
              java.util.Arrays.equals(this.terceros, other.getTerceros()))) &&
            ((this.vias==null && other.getVias()==null) || 
             (this.vias!=null &&
              java.util.Arrays.equals(this.vias, other.getVias())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getAnotaciones() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnotaciones());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnotaciones(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDomicilios() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDomicilios());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDomicilios(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEjercicio() != null) {
            _hashCode += getEjercicio().hashCode();
        }
        if (getFecha() != null) {
            _hashCode += getFecha().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        _hashCode += (isResultado() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getTerceros() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTerceros());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTerceros(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getVias() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getVias());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getVias(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SWPisa_RetornoBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_RetornoBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anotaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anotaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_AnotacionesBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domicilios");
        elemField.setXmlName(new javax.xml.namespace.QName("", "domicilios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_DomiciliosBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ejercicio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ejercicio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("terceros");
        elemField.setXmlName(new javax.xml.namespace.QName("", "terceros"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_TercerosBean"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vias");
        elemField.setXmlName(new javax.xml.namespace.QName("", "vias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://registro.sw.aytos", "SWPisa_ViasBean"));
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
