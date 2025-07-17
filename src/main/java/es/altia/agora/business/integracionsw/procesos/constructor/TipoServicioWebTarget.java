package es.altia.agora.business.integracionsw.procesos.constructor;

import org.apache.axis.encoding.Target;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import es.altia.agora.business.integracionsw.*;
import es.altia.agora.business.integracionsw.exception.TipoNoValidoException;

import java.util.Vector;

public class TipoServicioWebTarget implements Target {

    private TipoServicioWebVO target;
    private String nombreCampo;
    private String nombreTipo;

    protected static Log m_Log = LogFactory.getLog(CustomDeserializer.class.getName());

    public TipoServicioWebTarget(TipoServicioWebVO target, String nombreCampo) {
        this.target = target;
        this.nombreCampo = nombreCampo;
    }

    public TipoServicioWebTarget(TipoServicioWebVO target, String nombreCampo, String nombreTipo) {
        this.target = target;
        this.nombreCampo = nombreCampo;
        this.nombreTipo = nombreTipo;
    }

    public void set(Object object) throws SAXException {

        TipoServicioWebVO tipo;
        if (object instanceof TipoServicioWebVO) {
            tipo = (TipoServicioWebVO)object;

        } else {
            // Metemos un tipo basico
            try {
                TipoBasicoVO tipoBasico = new TipoBasicoVO(nombreTipo);
                tipoBasico.setValor(object, nombreTipo);
                tipo = tipoBasico;
            } catch (TipoNoValidoException e) {
                e.printStackTrace();
                throw new SAXException(e);
            }
        }

        if (target.getClass() == TipoCompuestoVO.class) {
            TipoCompuestoVO tipoCompuesto = (TipoCompuestoVO) target;
            tipoCompuesto.getFields().add(new CampoTipoCompuestoVO(nombreCampo, tipo));
        } else if (target.getClass() == TipoArrayVO.class) {
            TipoArrayVO tipoArray = (TipoArrayVO) target;
            if (tipoArray.getTipoContenido() == null) tipoArray.setTipoContenido(tipo);
            if (tipoArray.getArray() == null) tipoArray.setArray(new Vector<TipoServicioWebVO>());
            tipoArray.getArray().add(tipo);
        }
    }

    public String toString() {
        return nombreCampo + " --> " + target;
    }
}
