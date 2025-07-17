package es.altia.agora.business.integracionsw;

import java.io.Serializable;
import java.util.Vector;

public class OperacionServicioWebVO implements Serializable {

    private String nombreOperacion;
    private ParametroSWVO salida;
    private Vector<ParametroSWVO> paramsEntrada;
    private String namespace;
    private String soapActionUri;
    private String opStyle;

    public OperacionServicioWebVO(String nombreOperacion, ParametroSWVO salida,  Vector<ParametroSWVO> paramsEntrada,
                                  String namespace, String soapActionUri, String opStyle) {
		super();
		this.nombreOperacion = nombreOperacion;
		this.salida = salida;
		this.paramsEntrada = paramsEntrada;
		this.namespace = namespace;
        this.soapActionUri = soapActionUri;
        this.opStyle = opStyle;
    }

    public String getSoapActionUri() {
        return soapActionUri;
    }

    public void setSoapActionUri(String soapActionUri) {
        this.soapActionUri = soapActionUri;
    }

    public String getNombreOperacion() {
		return nombreOperacion;
	}

	public void setNombreOperacion(String nombreOperacion) {
		this.nombreOperacion = nombreOperacion;
	}

	public Vector<ParametroSWVO> getParamsEntrada() {
		return paramsEntrada;
	}

	public void setParamsEntrada(Vector<ParametroSWVO> paramsEntrada) {
		this.paramsEntrada = paramsEntrada;
	}

	public ParametroSWVO getSalida() {
		return salida;
	}

	public void setSalida(ParametroSWVO salida) {
		this.salida = salida;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

    public String getOpStyle() {
        return opStyle;
    }

    public void setOpStyle(String opStyle) {
        this.opStyle = opStyle;
    }

    public String toString() {
        return "OPERACION --> nombreOperacion: " + this.nombreOperacion + " | namespace: " + this.namespace + "\n " +
                "Uri Soap Action: " + this.soapActionUri + "Operation Style: " + opStyle + "ParametroSalida: " + this.salida +
                "\n Parametros de Entrada: " + this.paramsEntrada;
    }

}
