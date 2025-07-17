package es.altia.agora.business.gestionInformes.particular.impl;

import java.util.Vector;

public class DatoEstadisticaRegistro{
	
	private Vector<Vector<String>> filas;
	private Vector<String> uors;
	private String titulo;
	private String usuario;
	private String fecha;
	
	public DatoEstadisticaRegistro(){}
	
	public DatoEstadisticaRegistro(Vector<String> uors, Vector<Vector<String>> filas, String titulo,
			String fecha, String usuario){
		this.uors = uors;
		this.filas = filas;
		this.titulo = titulo;
		this.fecha = fecha;
		this.usuario = usuario;
	}
	
	public void setFecha (String fecha){
		this.fecha = fecha;
	}
	
	public String getFecha (){
		return this.fecha;
	}
	
	public void setUsuario (String usuario){
		this.usuario = usuario;
	}
	
	public String getUsuario(){
		return this.usuario;
	}
	
	public void setTitulo (String titulo){
		this.titulo = titulo;
	}
	
	public String getTitulo(){
		return this.titulo;
	}
	
	public Vector<Vector<String>> getFilas(){
		return this.filas;
	}
	
	public Vector<String> getUors(){
		return this.uors;
	}
	
	public void setFilas(Vector<Vector<String>> filas){
		this.filas=filas;
	}
	
	public void setUors(Vector<String> uors){
		this.uors=uors;
	}
	
	public String toString(){
		int numFilas = filas.size();
		String dato = "UORS = ";
		Vector<String> fila = new Vector<String>();
		
		for(int i=0;i<uors.size();i++){
			dato+= uors.get(i) + ",";
		}
		
		for(int j=0;j<numFilas;j++){
			dato+= " --> FILA" + String.valueOf(j) + "=";
			fila = filas.get(j);
			for(int t=0; t<fila.size();t++){
				dato+= fila.get(t) + ",";
			}
		}
		return dato;
	}
	
	
}