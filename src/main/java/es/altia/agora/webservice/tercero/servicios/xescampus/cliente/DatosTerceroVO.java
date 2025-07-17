/**
 * DatosTerceroVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.xescampus.cliente;

public class DatosTerceroVO  implements java.io.Serializable {
    private java.lang.String apellido1;

    private java.lang.String apellido2;

    private java.lang.String doi;

    private es.altia.agora.webservice.tercero.servicios.xescampus.cliente.DatosDomicilioVO[] domicilios;

    private java.lang.String idTercero;

    private java.lang.String nombre;

    private java.lang.String telefono;

    private java.lang.String tipoDocumento;

    public DatosTerceroVO() {
    }

    public DatosTerceroVO(
           java.lang.String apellido1,
           java.lang.String apellido2,
           java.lang.String doi,
           es.altia.agora.webservice.tercero.servicios.xescampus.cliente.DatosDomicilioVO[] domicilios,
           java.lang.String idTercero,
           java.lang.String nombre,
           java.lang.String telefono,
           java.lang.String tipoDocumento) {
           this.apellido1 = apellido1;
           this.apellido2 = apellido2;
           this.doi = doi;
           this.domicilios = domicilios;
           this.idTercero = idTercero;
           this.nombre = nombre;
           this.telefono = telefono;
           this.tipoDocumento = tipoDocumento;
    }


    /**
     * Gets the apellido1 value for this DatosTerceroVO.
     * 
     * @return apellido1
     */
    public java.lang.String getApellido1() {
        return apellido1;
    }


    /**
     * Sets the apellido1 value for this DatosTerceroVO.
     * 
     * @param apellido1
     */
    public void setApellido1(java.lang.String apellido1) {
        this.apellido1 = apellido1;
    }


    /**
     * Gets the apellido2 value for this DatosTerceroVO.
     * 
     * @return apellido2
     */
    public java.lang.String getApellido2() {
        return apellido2;
    }


    /**
     * Sets the apellido2 value for this DatosTerceroVO.
     * 
     * @param apellido2
     */
    public void setApellido2(java.lang.String apellido2) {
        this.apellido2 = apellido2;
    }


    /**
     * Gets the doi value for this DatosTerceroVO.
     * 
     * @return doi
     */
    public java.lang.String getDoi() {
        return doi;
    }


    /**
     * Sets the doi value for this DatosTerceroVO.
     * 
     * @param doi
     */
    public void setDoi(java.lang.String doi) {
        this.doi = doi;
    }


    /**
     * Gets the domicilios value for this DatosTerceroVO.
     * 
     * @return domicilios
     */
    public es.altia.agora.webservice.tercero.servicios.xescampus.cliente.DatosDomicilioVO[] getDomicilios() {
        return domicilios;
    }


    /**
     * Sets the domicilios value for this DatosTerceroVO.
     * 
     * @param domicilios
     */
    public void setDomicilios(es.altia.agora.webservice.tercero.servicios.xescampus.cliente.DatosDomicilioVO[] domicilios) {
        this.domicilios = domicilios;
    }


    /**
     * Gets the idTercero value for this DatosTerceroVO.
     * 
     * @return idTercero
     */
    public java.lang.String getIdTercero() {
        return idTercero;
    }


    /**
     * Sets the idTercero value for this DatosTerceroVO.
     * 
     * @param idTercero
     */
    public void setIdTercero(java.lang.String idTercero) {
        this.idTercero = idTercero;
    }


    /**
     * Gets the nombre value for this DatosTerceroVO.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this DatosTerceroVO.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the telefono value for this DatosTerceroVO.
     * 
     * @return telefono
     */
    public java.lang.String getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this DatosTerceroVO.
     * 
     * @param telefono
     */
    public void setTelefono(java.lang.String telefono) {
        this.telefono = telefono;
    }


    /**
     * Gets the tipoDocumento value for this DatosTerceroVO.
     * 
     * @return tipoDocumento
     */
    public java.lang.String getTipoDocumento() {
        return tipoDocumento;
    }


    /**
     * Sets the tipoDocumento value for this DatosTerceroVO.
     * 
     * @param tipoDocumento
     */
    public void setTipoDocumento(java.lang.String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosTerceroVO)) return false;
        DatosTerceroVO other = (DatosTerceroVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.apellido1==null && other.getApellido1()==null) || 
             (this.apellido1!=null &&
              this.apellido1.equals(other.getApellido1()))) &&
            ((this.apellido2==null && other.getApellido2()==null) || 
             (this.apellido2!=null &&
              this.apellido2.equals(other.getApellido2()))) &&
            ((this.doi==null && other.getDoi()==null) || 
             (this.doi!=null &&
              this.doi.equals(other.getDoi()))) &&
            ((this.domicilios==null && other.getDomicilios()==null) || 
             (this.domicilios!=null &&
              java.util.Arrays.equals(this.domicilios, other.getDomicilios()))) &&
            ((this.idTercero==null && other.getIdTercero()==null) || 
             (this.idTercero!=null &&
              this.idTercero.equals(other.getIdTercero()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.telefono==null && other.getTelefono()==null) || 
             (this.telefono!=null &&
              this.telefono.equals(other.getTelefono()))) &&
            ((this.tipoDocumento==null && other.getTipoDocumento()==null) || 
             (this.tipoDocumento!=null &&
              this.tipoDocumento.equals(other.getTipoDocumento())));
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
        if (getApellido1() != null) {
            _hashCode += getApellido1().hashCode();
        }
        if (getApellido2() != null) {
            _hashCode += getApellido2().hashCode();
        }
        if (getDoi() != null) {
            _hashCode += getDoi().hashCode();
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
        if (getIdTercero() != null) {
            _hashCode += getIdTercero().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getTelefono() != null) {
            _hashCode += getTelefono().hashCode();
        }
        if (getTipoDocumento() != null) {
            _hashCode += getTipoDocumento().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosTerceroVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tercero.ws.altia.es", "DatosTerceroVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellido1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apellido1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellido2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "apellido2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("doi");
        elemField.setXmlName(new javax.xml.namespace.QName("", "doi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domicilios");
        elemField.setXmlName(new javax.xml.namespace.QName("", "domicilios"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tercero.ws.altia.es", "DatosDomicilioVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idTercero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idTercero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("", "telefono"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoDocumento"));
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
