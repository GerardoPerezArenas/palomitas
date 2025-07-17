package es.altia.agora.business.integracionsw;

import java.io.Serializable;

public class ComprobacionEstructuraVO implements Serializable {

	private boolean correcta;
	private boolean avanzar;
    private boolean iniciar;
	private String titulo;
	private int orden;
	
	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	public boolean isAvanzar() {
		return avanzar;
	}
	public void setAvanzar(boolean avanzar) {
		this.avanzar = avanzar;
	}
	public boolean isCorrecta() {
		return correcta;
	}
	public void setCorrecta(boolean correcta) {
		this.correcta = correcta;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
    public boolean isIniciar(){
        return this.iniciar;
    }
    public void setIniciar(boolean iniciar){
        this.iniciar = iniciar;
    }
	public String toString() {
		return ("\nCorrecta: " + correcta + "\nTitulo: " + titulo +
				"\nOrden: " + orden + "\nAvanzar: " + avanzar);
	}
}
