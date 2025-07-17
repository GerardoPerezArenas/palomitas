package es.altia.agora.business.integracionsw;

import es.altia.agora.business.integracionsw.exception.TipoNoValidoException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TipoBasicoVO extends TipoServicioWebVO implements Serializable {

	protected static Log m_Log = LogFactory.getLog(TipoBasicoVO.class.getName());
	public static final int VALOR_INTEGER_DEFECTO = 0;
	public static final short VALOR_SHORT_DEFECTO = 0;
	public static final byte VALOR_BYTE_DEFECTO = 0;
	public static final long VALOR_LONG_DEFECTO = 0;
	public static final float VALOR_FLOAT_DEFECTO = 0;
	public static final double VALOR_DOUBLE_DEFECTO = 0;
	public static final boolean VALOR_BOOLEAN_DEFECTO = false;
    private Object valor;

    public TipoBasicoVO(String nombreTipo) {
        super(nombreTipo, TIPO_BASE);
    }

    public Object getValor() throws TipoNoValidoException {
        String tipo = super.nombreTipo;
        if (tipo.equals("int")) {
            return Integer.parseInt((String)valor);
        } else if (tipo.equals("string")) {
            return valor.toString();
        } else if (tipo.equals("dateTime")) {
            return (Calendar) valor;
        } else if (tipo.equals("byte")) {
            return (Byte) valor;
        } else if (tipo.equals("boolean")) {
            return (String)valor;
        } else if (tipo.equals("integer")) {
            return (BigInteger) valor;
        } else if (tipo.equals("float")) {
            return (Float) valor;
        } else if (tipo.equals("double")) {
            return (Double) valor;
        } else if (tipo.equals("long")) {
            return (Long) valor;
        } else if (tipo.equals("decimal")) {
            return (BigDecimal) valor;
        } else if (tipo.equals("QName")) {
            return (QName) valor;
        } else if (tipo.equals("short")) {
            return (Short) valor;
        } else if ((tipo.equals("base64Binary")) || (tipo.equals("hexBinary"))) {
            return (Vector) valor;
        } else if (tipo.equals("anyType")) {
            return valor;
        } else throw new TipoNoValidoException("EL IDENTIFICADOR " + tipo + " NO SE CORRESPONDE CON NINGUN TIPO " +
                "DEFINIDO PARA EL SERVICIO WEB");
    }

    public String getValorAsString() {
    	String tipo = super.nombreTipo; 
    	m_Log.debug("El valor al entrar es :   " + valor);
        if (tipo.equals("int")) {
            if (valor == null) {
            	valor = null;
            }        
        
        } else if (tipo.equals("byte")) {
            if ("".equals((String)valor)) {
            	valor = null;
            }        
        } else if (tipo.equals("boolean")) {
            if (valor == null) {
            	valor = VALOR_BOOLEAN_DEFECTO;
            }        

        } else if (tipo.equals("float")) {
            if ("".equals((String)valor)) {
            	valor = null;
            } 
        } else if (tipo.equals("double")) {
            if (valor == null || "".equals(valor)) {
                valor = VALOR_DOUBLE_DEFECTO;
            } 
        } else if (tipo.equals("long")) {
            if (valor == null) {
            	valor = null;
            } 
        } else if (tipo.equals("short")) {
            if ("".equals((String)valor)) {
            	valor = null;
            } 
        }
//        } else if ((tipo.equals("base64Binary")) || (tipo.equals("hexBinary"))) {
//            return (Vector) valor;
//        }
        m_Log.debug("El valor al salir es :   " + valor);

        if (valor == null) return "";
        return valor.toString();
    }


    public Object getValor(String tipo) throws TipoNoValidoException {
        if (tipo.equals("int")) {
            return (Integer) valor;
        } else if (tipo.equals("string")) {
            return (String) valor;
        } else if (tipo.equals("dateTime")) {
            return (Calendar) valor;
        } else if (tipo.equals("byte")) {
            return (Byte) valor;
        } else if (tipo.equals("boolean")) {
            return (Boolean) valor;
        } else if (tipo.equals("integer")) {
            return (BigInteger) valor;
        } else if (tipo.equals("float")) {
            return (Float) valor;
        } else if (tipo.equals("double")) {
            return (Double) valor;
        } else if (tipo.equals("long")) {
            return (Long) valor;
        } else if (tipo.equals("decimal")) {
            return (BigDecimal) valor;
        } else if (tipo.equals("QName")) {
            return (QName) valor;
        } else if (tipo.equals("short")) {
            return (Short) valor;
        } else if ((tipo.equals("base64Binary")) || (tipo.equals("hexBinary"))) {
            return (Vector) valor;
        } else if (tipo.equals("anyType")) {
            return valor;
        } else throw new TipoNoValidoException("EL IDENTIFICADOR " + tipo + " NO SE CORRESPONDE CON NINGUN TIPO " +
                "DEFINIDO PARA EL SERVICIO WEB");
    }


    public void setValor(String valor) throws TipoNoValidoException {
        this.valor = valor;
    }

    public void setValor(Object valor, String tipo) throws TipoNoValidoException {
        if (tipo.equals("int")) {
            this.valor = (Integer) valor;
        } else if (tipo.equals("string")) {
            this.valor = (String) valor;
        } else if (tipo.equals("dateTime")) {
            this.valor = (Calendar) valor;
        } else if (tipo.equals("byte")) {
            this.valor = (Byte) valor;
        } else if (tipo.equals("boolean")) {
            this.valor = (Boolean) valor;
        } else if (tipo.equals("integer")) {
            this.valor = (BigInteger) valor;
        } else if (tipo.equals("float")) {
            this.valor = (Float) valor;
        } else if (tipo.equals("double")) {
            this.valor = (Double) valor;
        } else if (tipo.equals("long")) {
            this.valor = (Long) valor;
        } else if (tipo.equals("decimal")) {
            this.valor = (BigDecimal) valor;
        } else if (tipo.equals("QName")) {
            this.valor = (QName) valor;
        } else if (tipo.equals("short")) {
            this.valor = (Short) valor;
        } else if ((tipo.equals("base64Binary")) || (tipo.equals("hexBinary"))) {
            this.valor = (Vector) valor;
        } else if (tipo.equals("anyType")) {
            this.valor = valor;
        } else throw new TipoNoValidoException("EL IDENTIFICADOR " + tipo + " NO SE CORRESPONDE CON NINGUN TIPO " +
                "DEFINIDO PARA EL SERVICIO WEB");
    }

    public String toString() {
        return "Tipo Basico --> Nombre Tipo: " + this.nombreTipo + " | Valor: " + this.valor;
    }

    public TipoBasicoVO copy() {
        TipoBasicoVO tipoBasico = new TipoBasicoVO(this.nombreTipo);
        tipoBasico.valor = this.valor;
        return tipoBasico;
    }

    public Class getClaseTipo() {
        if (super.nombreTipo.equals("int")) return Integer.class;
        else if (super.nombreTipo.equals("string")) return String.class;
        else if (super.nombreTipo.equals("dateTime")) return Calendar.class;
        else if (super.nombreTipo.equals("byte")) return Byte.class;
        else if (super.nombreTipo.equals("boolean")) return Boolean.class;
        else if (super.nombreTipo.equals("integer")) return BigInteger.class;
        else if (super.nombreTipo.equals("float")) return Float.class;
        else if (super.nombreTipo.equals("double")) return Double.class;
        else if (super.nombreTipo.equals("long")) return Long.class;
        else if (super.nombreTipo.equals("decimal")) return BigDecimal.class;
        else if (super.nombreTipo.equals("QName")) return QName.class;
        else if (super.nombreTipo.equals("short")) return Short.class;
        else if ((super.nombreTipo.equals("base64Binary")) || (super.nombreTipo.equals("hexBinary"))) return Vector.class;
        else if (super.nombreTipo.equals("anyType")) return Object.class;
        else return String.class;
    }
}
