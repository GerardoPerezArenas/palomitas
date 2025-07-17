/**
 * EntidadesComunesWSValueObject.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.altia.agora.webservice.tercero.servicios.lanbide.cliente;

public class EntidadesComunesWSValueObject  extends es.altia.agora.webservice.tercero.servicios.lanbide.cliente.ValueObject  implements java.io.Serializable {
    private es.altia.agora.webservice.tercero.servicios.lanbide.cliente.EmpresarioWSValueObject empresario;

    private java.util.Vector lista_errores;

    private es.altia.agora.webservice.tercero.servicios.lanbide.cliente.PersonaFisicaWSValueObject persona;

    private java.lang.String tipo_consulta;

    public EntidadesComunesWSValueObject() {
    }

    public EntidadesComunesWSValueObject(
           es.altia.agora.webservice.tercero.servicios.lanbide.cliente.AuditoriaValueObject auditoria,
           long objectId,
           es.altia.agora.webservice.tercero.servicios.lanbide.cliente.EmpresarioWSValueObject empresario,
           java.util.Vector lista_errores,
           es.altia.agora.webservice.tercero.servicios.lanbide.cliente.PersonaFisicaWSValueObject persona,
           java.lang.String tipo_consulta) {
        super(
            auditoria,
            objectId);
        this.empresario = empresario;
        this.lista_errores = lista_errores;
        this.persona = persona;
        this.tipo_consulta = tipo_consulta;
    }


    /**
     * Gets the empresario value for this EntidadesComunesWSValueObject.
     * 
     * @return empresario
     */
    public es.altia.agora.webservice.tercero.servicios.lanbide.cliente.EmpresarioWSValueObject getEmpresario() {
        return empresario;
    }


    /**
     * Sets the empresario value for this EntidadesComunesWSValueObject.
     * 
     * @param empresario
     */
    public void setEmpresario(es.altia.agora.webservice.tercero.servicios.lanbide.cliente.EmpresarioWSValueObject empresario) {
        this.empresario = empresario;
    }


    /**
     * Gets the lista_errores value for this EntidadesComunesWSValueObject.
     * 
     * @return lista_errores
     */
    public java.util.Vector getLista_errores() {
        return lista_errores;
    }


    /**
     * Sets the lista_errores value for this EntidadesComunesWSValueObject.
     * 
     * @param lista_errores
     */
    public void setLista_errores(java.util.Vector lista_errores) {
        this.lista_errores = lista_errores;
    }


    /**
     * Gets the persona value for this EntidadesComunesWSValueObject.
     * 
     * @return persona
     */
    public es.altia.agora.webservice.tercero.servicios.lanbide.cliente.PersonaFisicaWSValueObject getPersona() {
        return persona;
    }


    /**
     * Sets the persona value for this EntidadesComunesWSValueObject.
     * 
     * @param persona
     */
    public void setPersona(es.altia.agora.webservice.tercero.servicios.lanbide.cliente.PersonaFisicaWSValueObject persona) {
        this.persona = persona;
    }


    /**
     * Gets the tipo_consulta value for this EntidadesComunesWSValueObject.
     * 
     * @return tipo_consulta
     */
    public java.lang.String getTipo_consulta() {
        return tipo_consulta;
    }


    /**
     * Sets the tipo_consulta value for this EntidadesComunesWSValueObject.
     * 
     * @param tipo_consulta
     */
    public void setTipo_consulta(java.lang.String tipo_consulta) {
        this.tipo_consulta = tipo_consulta;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EntidadesComunesWSValueObject)) return false;
        EntidadesComunesWSValueObject other = (EntidadesComunesWSValueObject) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.empresario==null && other.getEmpresario()==null) || 
             (this.empresario!=null &&
              this.empresario.equals(other.getEmpresario()))) &&
            ((this.lista_errores==null && other.getLista_errores()==null) || 
             (this.lista_errores!=null &&
              this.lista_errores.equals(other.getLista_errores()))) &&
            ((this.persona==null && other.getPersona()==null) || 
             (this.persona!=null &&
              this.persona.equals(other.getPersona()))) &&
            ((this.tipo_consulta==null && other.getTipo_consulta()==null) || 
             (this.tipo_consulta!=null &&
              this.tipo_consulta.equals(other.getTipo_consulta())));
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
        if (getEmpresario() != null) {
            _hashCode += getEmpresario().hashCode();
        }
        if (getLista_errores() != null) {
            _hashCode += getLista_errores().hashCode();
        }
        if (getPersona() != null) {
            _hashCode += getPersona().hashCode();
        }
        if (getTipo_consulta() != null) {
            _hashCode += getTipo_consulta().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EntidadesComunesWSValueObject.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://langai.altia.es/business/personafisica", "EntidadesComunesWSValueObject"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empresario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "empresario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langai.altia.es/business/personafisica", "EmpresarioWSValueObject"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lista_errores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lista_errores"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Vector"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("persona");
        elemField.setXmlName(new javax.xml.namespace.QName("", "persona"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://langai.altia.es/business/personafisica", "PersonaFisicaWSValueObject"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo_consulta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo_consulta"));
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
