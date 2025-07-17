package es.altia.agora.business.escritorio;

import java.util.Date;

public class EDUNotaPersoal implements EDUBean{

	public EDUNotaPersoal() {}
	
	private String idNota=null;
	private String nomeNota=null;
	private Date instanteGrabacion=null;
	private String textoNota=null;
	private String idUsuario=null;
	
	public void setIdNota(String s) {
		if (s!=null) idNota=s.trim();
	}
	public void setNomeNota(String s) {
		if (s!=null) nomeNota=s.trim();
	}
	public void setInstanteGrabacion(Date d) {
		if (d!=null) instanteGrabacion=d;
	}
	public void setTextoNota(String s) {
		if (s!=null) textoNota=s.trim();
	}
	public void setIdUsuario (String s) {
		if (s!=null) idUsuario=s;
	}
	
	public String getIdNota() {
		return idNota;
	}
	public String getNomeNota () {
		return nomeNota;
	}
	public String getTextoNota() {
		return textoNota;
	}
	public String getIdUsuario() {
		return idUsuario;
	}
	public Date getInstanteGrabacion() {
		
		return instanteGrabacion;
	}
	
}
