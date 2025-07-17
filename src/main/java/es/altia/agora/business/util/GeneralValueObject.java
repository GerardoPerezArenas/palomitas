// NOMBRE DEL PAQUETE
package es.altia.agora.business.util;

// PAQUETES IMPORTADOS
import java.io.Serializable;
import java.util.Hashtable;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class GeneralValueObject implements Serializable{
  private Hashtable tabla;

  public GeneralValueObject() {
    super();
    tabla=new Hashtable();
  }

  public Object getAtributo(Object nombre){
    return tabla.get(nombre);
  }

  public Object getAtributoONulo(Object nombre){
      Object aux = tabla.get(nombre);
      if (aux == null || "".equals(aux.toString()))
          return null;
      return aux;
  }

  public void setAtributo(String nombre,Object valor){
    if (valor==null)
      valor="";
    /*if(tabla.containsKey(nombre)){
      tabla.remove(nombre);
    }*/
    tabla.put(nombre,valor);
  }

  public int getSize(){
    return tabla.size();
  }
  
  // Devuelve la tabla hash
  public Hashtable getTabla() {
      return tabla;
  }
  
  public String toString(){
  	java.util.Enumeration claves = tabla.keys();
  	String txt="";
  	while(claves.hasMoreElements()){
  		Object clave = claves.nextElement();
  		Object valor = tabla.get(clave);
  		txt += "[ "+clave.toString()+" = "+valor.toString()+"]\n";
  	}
  	return txt;
  }
}
