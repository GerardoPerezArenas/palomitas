/*______________________________BOF_________________________________*/
package es.altia.util.persistance;

import java.io.Serializable;
import java.util.Hashtable;



public class GeneralValueObject implements Serializable{
  private Hashtable tabla;

  public GeneralValueObject() {
    super();
    tabla=new Hashtable();
  }

  public Object getAtributo(Object nombre){
    return tabla.get(nombre);
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