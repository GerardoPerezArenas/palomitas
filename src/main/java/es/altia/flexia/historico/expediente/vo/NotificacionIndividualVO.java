package es.altia.flexia.historico.expediente.vo;

import java.util.Calendar;

public class NotificacionIndividualVO {
	
    private long idProceso; //Se corresponde con el campo ID_PROCESO.
    private int codigoNotificacionIndividual;			//Corresponde con Código de la notificación individual
	private int codigoNotificacion;						//Corresponde con Código de la notificación origen - TABLA: NOTIFICACION
	private AutorizadoNotificacionVO autorizado = null;		//Corresponde con Código del interesado - TABLA: T_TER
	private String estadoNotificacionIndividual;			//Corresponde con Estado comunicación: E- Enviada , P - Pendiente Envío
	private Calendar fechaAcuse;							//Corresponde con Fecha acuse recibo notificación
    private String resultado;								//Corresponde con Resultado del acuse de la notificación: R - Rechazada, A - Aceptada
	private String numeroRegistroTelematico;				//Corresponde con el número de registro en la anotación de salida en el Registro Telemático

    
    public NotificacionIndividualVO()
    {
        
    }

	public long getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(long idProceso) {
		this.idProceso = idProceso;
	}

	public int getCodigoNotificacionIndividual() {
		return codigoNotificacionIndividual;
	}

	public void setCodigoNotificacionIndividual(int codigoNotificacionIndividual) {
		this.codigoNotificacionIndividual = codigoNotificacionIndividual;
	}

	public int getCodigoNotificacion() {
		return codigoNotificacion;
	}

	public void setCodigoNotificacion(int codigoNotificacion) {
		this.codigoNotificacion = codigoNotificacion;
	}

	public AutorizadoNotificacionVO getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(AutorizadoNotificacionVO autorizado) {
		this.autorizado = autorizado;
	}

	public String getEstadoNotificacionIndividual() {
		return estadoNotificacionIndividual;
	}

	public void setEstadoNotificacionIndividual(String estadoNotificacionIndividual) {
		this.estadoNotificacionIndividual = estadoNotificacionIndividual;
	}

	public Calendar getFechaAcuse() {
		return fechaAcuse;
	}

	public void setFechaAcuse(Calendar fechaAcuse) {
		this.fechaAcuse = fechaAcuse;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getNumeroRegistroTelematico() {
		return numeroRegistroTelematico;
	}

	public void setNumeroRegistroTelematico(String numeroRegistroTelematico) {
		this.numeroRegistroTelematico = numeroRegistroTelematico;
	}

    
}
