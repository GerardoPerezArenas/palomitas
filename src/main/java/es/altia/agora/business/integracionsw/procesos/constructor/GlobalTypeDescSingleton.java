package es.altia.agora.business.integracionsw.procesos.constructor;

import org.apache.axis.description.TypeDesc;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;

import es.altia.agora.business.integracionsw.*;

import javax.xml.namespace.QName;

public class GlobalTypeDescSingleton {

    private Map<QName, TypeDesc> collectedTypeDescs = new HashMap<QName, TypeDesc>();
    private static GlobalTypeDescSingleton instance;
    protected static Log m_Log = LogFactory.getLog(GlobalTypeDescSingleton.class.getName());

    protected GlobalTypeDescSingleton() {
        collectedTypeDescs = new HashMap<QName, TypeDesc>();
    }

    public static GlobalTypeDescSingleton getInstance() {
        if (instance == null)
            synchronized(GlobalTypeDescSingleton.class){
				if (instance == null)
					instance = new GlobalTypeDescSingleton();
			}
        return instance;
    }

    public void registerComplexTypeDesc(TipoCompuestoVO tipoCompuesto) {
        QName qName = new QName(tipoCompuesto.getNamespace(), tipoCompuesto.getNombreTipo());
        if (collectedTypeDescs.get(qName) != null) return;

        TypeDesc typeDesc = new TypeDesc(TipoCompuestoVO.class, true);
        typeDesc.setXmlType(qName);
        for (CampoTipoCompuestoVO campo: tipoCompuesto.getFields()) {
            String key = campo.getIdCampo();
            TipoServicioWebVO tipoContenido = campo.getTipoCampo();
            ElementDesc elemField = new ElementDesc();
            elemField.setFieldName(key);
            elemField.setXmlName(new QName(tipoCompuesto.getNamespace(), key));
            if (tipoContenido.esTipoBase()) {
                TipoBasicoVO tipoBasico = (TipoBasicoVO)tipoContenido;
                elemField.setXmlType(new QName(Constants.URI_2001_SCHEMA_XSD, tipoBasico.getNombreTipo()));
                elemField.setJavaType(TipoBasicoVO.class);
            } else if (tipoContenido.esTipoArray()) {
                TipoArrayVO tipoArray = (TipoArrayVO) tipoContenido;
                TipoServicioWebVO tipoEnArray = tipoArray.getTipoContenido();
                if (tipoEnArray.esTipoBase()) {
                    TipoBasicoVO tipoBasicoEnArray = (TipoBasicoVO)tipoEnArray;
                    elemField.setXmlType(new QName(Constants.URI_2001_SCHEMA_XSD, tipoBasicoEnArray.getNombreTipo()));
                    elemField.setItemQName(new QName(Constants.URI_2001_SCHEMA_XSD, tipoBasicoEnArray.getNombreTipo()));
                    
                } else if (tipoEnArray.esTipoComplejo()) {
                    TipoCompuestoVO tipoComplejoEnArray = (TipoCompuestoVO) tipoEnArray;
                    elemField.setXmlType(new QName(tipoComplejoEnArray.getNamespace(), tipoComplejoEnArray.getNombreTipo()));
                    elemField.setItemQName(new QName(tipoComplejoEnArray.getNamespace(), tipoComplejoEnArray.getNombreTipo()));
                }
            }
            elemField.setMinOccurs(0);
            elemField.setNillable(false);
            typeDesc.addFieldDesc(elemField);

        }

        m_Log.debug("DESCRIPCION DEL TIPO:" + typeDesc);
        collectedTypeDescs.put(qName, typeDesc);
    }

    public TypeDesc getTypeDesc(QName qName) {
        return collectedTypeDescs.get(qName);
    }

}
