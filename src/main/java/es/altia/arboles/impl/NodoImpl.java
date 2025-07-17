package es.altia.arboles.impl;

import java.util.*;
import java.io.Serializable;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.arboles.Nodo;

/**
 * Representa un nodo del árbol
 * User: Esteban Sánchez Soler
 */
public class NodoImpl implements Nodo, Serializable,Comparable {
    private ArrayList hijos;
    private Nodo padre;
    private Object informacion;
    /**
     * Además del campo información, el nodo puede tener un clave única 
     */
    private Object clave;
    protected static Log log =
            LogFactory.getLog(NodoImpl.class.getName());
   

    public NodoImpl(Object clave) {        
        hijos = new ArrayList();        
    }

    public NodoImpl(Object informacion, Object clave) {        
        this.informacion = informacion;
        this.clave = clave;
        hijos = new ArrayList();        
    }

    public NodoImpl(ArrayList hijos, Object informacion, Object clave) {
        this.hijos = hijos;        
        this.informacion = informacion;
        this.clave = clave;
    }

    // GETTERS & SETTERS
    /* (non-Javadoc)
	 * @see es.altia.arboles.impl.Nodo#getHijos()
	 */
    public ArrayList getHijos() {
        return hijos;
    }

    /* (non-Javadoc)
	 * @see es.altia.arboles.impl.Nodo#setHijos(java.util.ArrayList)
	 */
    public void setHijos(ArrayList hijos) {
        this.hijos = hijos;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
    }

    /* (non-Javadoc)
	 * @see es.altia.arboles.impl.Nodo#getInformacion()
	 */
    public Object getInformacion() {
        return informacion;
    }

    /* (non-Javadoc)
	 * @see es.altia.arboles.impl.Nodo#setInformacion(java.lang.Object)
	 */
    public void setInformacion(Object informacion) {
        this.informacion = informacion;
    }
    
    public Object getClave() {
		return clave;
	}

	public void setClave(Object clave) {
		this.clave = clave;
	}

	// MÉTODOS
    /* (non-Javadoc)
	 * @see es.altia.arboles.impl.Nodo#esRaiz()
	 */
    public boolean esRaiz() {
        return(getPadre() == null);
    }

    public void addHijo(Nodo hijo) {
    	hijo.setPadre(this);
        hijos.add(hijo);
    }

    public void addHijoOrdenado(Nodo hijo) {//para ordenar hijos por campo nombre, usa criterio implementado en la funcion compareTo
    	hijo.setPadre(this);
        hijos.add(hijo);
        ordenarHijos(hijos);
    }
    
    public void ordenarHijos(ArrayList hijos){
    Collections.sort(hijos);
    }
  
    /*
     *  (non-Javadoc)
     * @see es.altia.arboles.Nodo#borrarHijo(java.lang.Object)
     */
    public Nodo borrarHijo(Object o) {
        int posicion = tieneHijo(o);
        Nodo nodo = null;

        if(posicion != -1) {
            nodo = (NodoImpl)hijos.get(posicion);
            hijos.remove(posicion);
        }
        return nodo;
    }

    public int tieneHijo(Object dto) {
        for(int i=0; i<hijos.size(); i++) {
            if(((Nodo)hijos.get(i)).getInformacion().equals(dto) == true) {
                return i;
            }
        }

        return -1;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {		
		return informacion.toString();
	}

    public int compareTo(Object o) { //criterio de ordenacion para ordenar por nombre el arbol
        Nodo nodo= (Nodo)o;
        
        if ((this.getInformacion().toString().compareTo(nodo.getInformacion().toString()))>0)
            return 1;
        else if ((this.getInformacion().toString().compareTo(nodo.getInformacion().toString()))<0)
            return -1;
            else//((this.getInformacion().toString().compareTo(nodo.getInformacion().toString()))==0)
            return 0;
        
      }
    

}
