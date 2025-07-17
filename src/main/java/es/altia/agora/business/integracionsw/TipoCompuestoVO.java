package es.altia.agora.business.integracionsw;

import org.apache.axis.encoding.Deserializer;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.Vector;

import es.altia.agora.business.integracionsw.procesos.constructor.CustomDeserializer;

public class TipoCompuestoVO extends TipoServicioWebVO implements Serializable{

    private Vector<CampoTipoCompuestoVO> fields;
    private String namespace;

    public TipoCompuestoVO() {
        super(TIPO_COMPLEJO);
        fields = new Vector<CampoTipoCompuestoVO>();
    }

    public TipoCompuestoVO(String nombreTipo, Vector<CampoTipoCompuestoVO> fields, String namespace) {

        super(nombreTipo,TIPO_COMPLEJO);
        this.fields = fields;
        this.namespace = namespace;
    }

    public Vector<CampoTipoCompuestoVO> getFields() {
        return fields;
    }

    public void setFields(Vector<CampoTipoCompuestoVO> fields) {
        this.fields = fields;
    }

    public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public CampoTipoCompuestoVO getCampo(String nombreCampo) {
        for (CampoTipoCompuestoVO campo: fields) {
            if (campo.getIdCampo().equals(nombreCampo)) return campo;
        }
        return null;
	}

    public String toString() {
        return "Tipo Compuesto --> nombreTipo: " + this.nombreTipo + " | Namespace: " + this.namespace +
                " | Campos del Bean: " + this.fields;
    }

    public TipoCompuestoVO copy() {
        Vector<CampoTipoCompuestoVO> copyFields = new Vector<CampoTipoCompuestoVO>();
        for (CampoTipoCompuestoVO campo: this.fields) {
            copyFields.add(new CampoTipoCompuestoVO(campo.getIdCampo(), campo.getTipoCampo().copy()));
        }
        return new TipoCompuestoVO(nombreTipo, copyFields, namespace);
    }

    public static Deserializer getDeserializer(String mechType, Class javaType, QName xmlType) {
        return new CustomDeserializer(javaType, xmlType);
    }
}
