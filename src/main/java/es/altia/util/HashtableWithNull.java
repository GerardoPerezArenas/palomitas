/**
 * HashtableWithNull.java
 *
 * @author JJGD
 */

package es.altia.util;
import java.util.Hashtable;

public final class HashtableWithNull extends Hashtable
{
	public HashtableWithNull() {
		super();
	}
	
	/**
	 * Method put
	 *		Este método <code>put</code> permite que el value sea <code>null</code>
	 *		Si se hace <code>put(clave,null)</code> para una clave ya existente el efecto es
	 *		que en la siguiente llamada al método get con esa clave se devolverá <code>null</code>
	 *		Por lo demas se comporta como el método <code>put</code> de Hashtable
	 * @param    key                 an Object que no debe ser null
	 * @param    value               an Object que puede ser null
	 *
	 * @return   an Object El value anterior contenido en el <code>key</code> pasado
	 *
	 */
	public synchronized Object put(Object key, Object value) {
		Object valorAnterior=null;
		if (value==null) {
			if (this.get(key)!=null) valorAnterior=this.remove(key);
		} else valorAnterior=super.put(key,value);
		return valorAnterior;
	}
}

