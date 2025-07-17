/**
 * InteresadoVO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.flexiaWS.documentos.bd.datos;

public class InteresadoVO  implements java.io.Serializable {
    private java.lang.String ap1;

    private java.lang.String ap2;

    private java.lang.String coddomicilio;

    private java.lang.String codigo;

    private java.lang.String doc;

    private es.altia.flexiaWS.tramitacion.bd.datos.DomicilioVO domicilio;

    private java.lang.String email;

    private java.lang.String nombre;

    private java.lang.String telefono;

    private java.lang.String tipoDoc;

    private java.lang.String version;

    private int rol;

    public InteresadoVO() {
    }

    public InteresadoVO(
           java.lang.String ap1,
           java.lang.String ap2,
           java.lang.String coddomicilio,
           java.lang.String codigo,
           java.lang.String doc,
           es.altia.flexiaWS.tramitacion.bd.datos.DomicilioVO domicilio,
           java.lang.String email,
           java.lang.String nombre,
           java.lang.String telefono,
           java.lang.String tipoDoc,
           java.lang.String version,
           int rol) {
           this.ap1 = ap1;
           this.ap2 = ap2;
           this.coddomicilio = coddomicilio;
           this.codigo = codigo;
           this.doc = doc;
           this.domicilio = domicilio;
           this.email = email;
           this.nombre = nombre;
           this.telefono = telefono;
           this.tipoDoc = tipoDoc;
           this.version = version;
           this.rol = rol;
    }


    /**
     * Gets the ap1 value for this InteresadoVO.
     * 
     * @return ap1
     */
    public java.lang.String getAp1() {
        return ap1;
    }


    /**
     * Sets the ap1 value for this InteresadoVO.
     * 
     * @param ap1
     */
    public void setAp1(java.lang.String ap1) {
        this.ap1 = ap1;
    }


    /**
     * Gets the ap2 value for this InteresadoVO.
     * 
     * @return ap2
     */
    public java.lang.String getAp2() {
        return ap2;
    }


    /**
     * Sets the ap2 value for this InteresadoVO.
     * 
     * @param ap2
     */
    public void setAp2(java.lang.String ap2) {
        this.ap2 = ap2;
    }


    /**
     * Gets the coddomicilio value for this InteresadoVO.
     * 
     * @return coddomicilio
     */
    public java.lang.String getCoddomicilio() {
        return coddomicilio;
    }


    /**
     * Sets the coddomicilio value for this InteresadoVO.
     * 
     * @param coddomicilio
     */
    public void setCoddomicilio(java.lang.String coddomicilio) {
        this.coddomicilio = coddomicilio;
    }


    /**
     * Gets the codigo value for this InteresadoVO.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this InteresadoVO.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the doc value for this InteresadoVO.
     * 
     * @return doc
     */
    public java.lang.String getDoc() {
        return doc;
    }


    /**
     * Sets the doc value for this InteresadoVO.
     * 
     * @param doc
     */
    public void setDoc(java.lang.String doc) {
        this.doc = doc;
    }


    /**
     * Gets the domicilio value for this InteresadoVO.
     * 
     * @return domicilio
     */
    public es.altia.flexiaWS.tramitacion.bd.datos.DomicilioVO getDomicilio() {
        return domicilio;
    }


    /**
     * Sets the domicilio value for this InteresadoVO.
     * 
     * @param domicilio
     */
    public void setDomicilio(es.altia.flexiaWS.tramitacion.bd.datos.DomicilioVO domicilio) {
        this.domicilio = domicilio;
    }


    /**
     * Gets the email value for this InteresadoVO.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this InteresadoVO.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the nombre value for this InteresadoVO.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this InteresadoVO.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the telefono value for this InteresadoVO.
     * 
     * @return telefono
     */
    public java.lang.String getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this InteresadoVO.
     * 
     * @param telefono
     */
    public void setTelefono(java.lang.String telefono) {
        this.telefono = telefono;
    }


    /**
     * Gets the tipoDoc value for this InteresadoVO.
     * 
     * @return tipoDoc
     */
    public java.lang.String getTipoDoc() {
        return tipoDoc;
    }


    /**
     * Sets the tipoDoc value for this InteresadoVO.
     * 
     * @param tipoDoc
     */
    public void setTipoDoc(java.lang.String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }


    /**
     * Gets the version value for this InteresadoVO.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this InteresadoVO.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }


    /**
     * Gets the rol value for this InteresadoVO.
     * 
     * @return rol
     */
    public int getRol() {
        return rol;
    }


    /**
     * Sets the rol value for this InteresadoVO.
     * 
     * @param rol
     */
    public void setRol(int rol) {
        this.rol = rol;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InteresadoVO)) return false;
        InteresadoVO other = (InteresadoVO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ap1==null && other.getAp1()==null) || 
             (this.ap1!=null &&
              this.ap1.equals(other.getAp1()))) &&
            ((this.ap2==null && other.getAp2()==null) || 
             (this.ap2!=null &&
              this.ap2.equals(other.getAp2()))) &&
            ((this.coddomicilio==null && other.getCoddomicilio()==null) || 
             (this.coddomicilio!=null &&
              this.coddomicilio.equals(other.getCoddomicilio()))) &&
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.doc==null && other.getDoc()==null) || 
             (this.doc!=null &&
              this.doc.equals(other.getDoc()))) &&
            ((this.domicilio==null && other.getDomicilio()==null) || 
             (this.domicilio!=null &&
              this.domicilio.equals(other.getDomicilio()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.telefono==null && other.getTelefono()==null) || 
             (this.telefono!=null &&
              this.telefono.equals(other.getTelefono()))) &&
            ((this.tipoDoc==null && other.getTipoDoc()==null) || 
             (this.tipoDoc!=null &&
              this.tipoDoc.equals(other.getTipoDoc()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            this.rol == other.getRol();
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
        if (getAp1() != null) {
            _hashCode += getAp1().hashCode();
        }
        if (getAp2() != null) {
            _hashCode += getAp2().hashCode();
        }
        if (getCoddomicilio() != null) {
            _hashCode += getCoddomicilio().hashCode();
        }
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getDoc() != null) {
            _hashCode += getDoc().hashCode();
        }
        if (getDomicilio() != null) {
            _hashCode += getDomicilio().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getTelefono() != null) {
            _hashCode += getTelefono().hashCode();
        }
        if (getTipoDoc() != null) {
            _hashCode += getTipoDoc().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        _hashCode += getRol();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InteresadoVO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://datos.bd.documentos.flexiaWS.altia.es", "InteresadoVO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ap1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ap1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ap2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ap2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coddomicilio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "coddomicilio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("doc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "doc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("domicilio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "domicilio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://datos.bd.tramitacion.flexiaWS.altia.es", "DomicilioVO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("", "email"));
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
        elemField.setFieldName("tipoDoc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoDoc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rol");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rol"));
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
