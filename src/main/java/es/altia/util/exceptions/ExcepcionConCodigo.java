package es.altia.util.exceptions;

public abstract class ExcepcionConCodigo extends Exception {
	
	String codigo;
	Object[] parametros;

	public ExcepcionConCodigo(String codigo, String mensaje, Throwable excepcion, Object... parametros) {
		super(String.format("%s: %s", codigo, mensaje), excepcion);
		this.codigo = codigo;
	}

	public ExcepcionConCodigo(String codigo, String mensaje, Object... parametros) {
		this(codigo, mensaje, null, parametros);
	}

	public String getCodigo() {
		return codigo;
	}

	public Object[] getParametros() {
		return parametros;
	}

}
