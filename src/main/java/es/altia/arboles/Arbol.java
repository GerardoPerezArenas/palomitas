package es.altia.arboles;


public interface Arbol {

	public abstract Nodo getRaiz();

	public abstract void setRaiz(Nodo raiz);

	/**
	 * Borra el nodo cuyo atributo informaci�n es equals() al argumento. Si el nodo tiene
	 * hijos, tambi�n desaparecen
	 * @param o Objeto a buscar en los nodos para borrar
	 * @return Nodo borrado o null si no ha sido encontrado
	 */
	public abstract Nodo borrarNodo(Object o);

	/**
	 * Busca un nodo a partir del argumento con la informaci�n
	 * descrita por el objeto
	 * @param nodo Nodo desde el que buscar el argumento
	 * @param o Informaci�n a buscar
	 * @return Nodo encontrado o null
	 * @throws IllegalArgumentException
	 */
	public abstract Nodo buscarEnProfNodo(Nodo nodo, Object o)
			throws IllegalArgumentException;

}