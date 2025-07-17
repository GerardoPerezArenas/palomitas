package es.altia.agora.business.geninformes.utils;


public class ExceptionXade extends Exception implements java.io.Serializable {

	/****************  Propiedades  *********************************/

	private int Codigo;
	private java.lang.Exception excepcion;
	private Object Mensaje;

	/****************   Constructores   *********************************/

	public ExceptionXade(
		int codigo,
		java.lang.Exception excepcion,
		Object Mensaje) {
		//es.altia.util.Debug.printException(excepcion);
		excepcion.printStackTrace();
		this.Codigo = codigo;
		this.excepcion = excepcion;
		this.Mensaje = Mensaje;
	}

	public ExceptionXade(int codigo) {
		this.Codigo = codigo;
		this.excepcion = null;
		this.Mensaje = null;
	}

	/*******************    Métodos    ***************************/

	public int getCodigo() {
		return this.Codigo;
	}

	public String getMensaje() {
		return this.Mensaje != null ? this.Mensaje.toString() : "No hay";
	}

	public java.lang.Exception getExcepcion() {
		return this.excepcion;
	}

	public String printCodigo() {
		return "Cod:" + this.Codigo;
	}
}