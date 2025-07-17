/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.cache;

import es.altia.agora.business.administracion.EntidadVO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.EntidadesDAO;
import java.util.ArrayList;
import java.util.SortedMap;
import org.apache.log4j.Logger;

/**
 *
 * @author adrian
 */
public class CacheEntidades implements CacheDatosGenericos {
    
    private static CacheEntidades instance = null;    
    private static Logger log = Logger.getLogger(CacheEntidades.class);
    
    private static SortedMap listaEntidades = null;
    
    private CacheEntidades(){ 
        synchronized (this) {
            listaEntidades = EntidadesDAO.getInstance().cargaCacheEntidades();
        }
    }
   
    public static CacheEntidades getInstance(){
        if (instance==null){
            synchronized (CacheEntidades.class) {
                instance = new CacheEntidades();
            }
        }
        return instance;
    }

    public SortedMap getDatos(){
        return listaEntidades;
    }
        
    public SortedMap getDatosBD(String jndi) {
        return listaEntidades;
    }
    
    public EntidadVO getDatoClaveUnica(String ... clave) {
        EntidadVO ent = null;
        if (clave!=null && clave.length == 2){
            ArrayList<String> claveUnica = new ArrayList<String>(2);
            for (String elem : clave) {
                claveUnica.add(elem);
            }
            ent = (EntidadVO)listaEntidades.get(claveUnica);
        }

        return ent;
    }
    
    public boolean eliminarDatoClaveUnica(String ... clave) {
        boolean resultado = false;
        if (clave!=null && clave.length == 2){
            ArrayList<String> claveUnica = new ArrayList<String>(2);
            for (String elem : clave) {
                claveUnica.add(elem);
            }
            listaEntidades.remove(claveUnica);
            resultado = true;
        }
        return resultado;
    }
    
    public boolean actualizarDatoClaveUnica(Object datoNuevo, String ... claveAnt) {
        boolean resultado = false;
        if (datoNuevo!=null && datoNuevo instanceof EntidadVO && claveAnt!=null && claveAnt.length == 2) {
            ArrayList<String> claveUnica = new ArrayList<String>(2);
            for (String elem : claveAnt) {
                claveUnica.add(elem);
            }
            listaEntidades.remove(claveUnica);
            EntidadVO entidad = (EntidadVO) datoNuevo;
            ArrayList <String> clave = new ArrayList<String>(2); 
            clave.add(String.valueOf(entidad.getCodOrganizacion()));
            clave.add(String.valueOf(entidad.getCodEntidad()));
            listaEntidades.put(clave, entidad);
            resultado = true;
        }
        return resultado;
    }
    
    public boolean insertarDato(Object dato) {
        boolean resultado = false;
        if (dato != null && dato instanceof EntidadVO) {
             EntidadVO entidad = (EntidadVO) dato;
             ArrayList <String> clave = new ArrayList<String>(2); 
             clave.add(String.valueOf(entidad.getCodOrganizacion()));
             clave.add(String.valueOf(entidad.getCodEntidad()));
             listaEntidades.put(clave, entidad);
            resultado = true;
        }
        return resultado;
    }
}
