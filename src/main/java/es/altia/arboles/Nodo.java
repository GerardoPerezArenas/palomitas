package es.altia.arboles;

import java.util.ArrayList;


public interface Nodo {

	// GETTERS & SETTERS
	public abstract ArrayList getHijos();

	public abstract void setHijos(ArrayList hijos);

	public abstract Nodo getPadre();

	public abstract void setPadre(Nodo padre);

	public abstract Object getInformacion();

	public abstract void setInformacion(Object informacion);
	
    public Object getClave();

	public abstract void setClave(Object clave);


	public abstract boolean esRaiz();

	public abstract void addHijo(Nodo hijo);

        public abstract void addHijoOrdenado(Nodo hijo);
	/**
     * Borra el hijo con el campo información equals() al argumento
     * @return Nodo borrado o null si no existe
     */
	public abstract Nodo borrarHijo(Object dto);

	/**
	 * Devuelve la posición del primer hijo cuyo campo información del nodo es equals() al argumento.
	 * @param dto Object a encontrar en los nodos
	 * @return Posición del hijo o -1 si no existe
	 */
	public abstract int tieneHijo(Object dto);

}