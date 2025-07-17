/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.cache;

import java.util.SortedMap;

/**
 *
 * @author adrian
 */
public interface CacheDatosGenericos {
    
    public SortedMap getDatos();
    public SortedMap getDatosBD(String jndi);
    public Object getDatoClaveUnica(String ... clave);
    public boolean eliminarDatoClaveUnica(String ...  clave);
    public boolean actualizarDatoClaveUnica(Object datoNuevo, String ... claveAnt);
    public boolean insertarDato(Object dato);
}
