/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.cache;

import es.altia.agora.business.administracion.ParametrosBDVO;
import es.altia.agora.business.administracion.persistence.manual.AutorizacionesExternasDAO;
import java.util.ArrayList;
import java.util.SortedMap;
import org.apache.log4j.Logger;

/**
 *
 * @author adrian
 */
public class CacheParametrosBD implements CacheDatosGenericos {
    
    private static CacheParametrosBD instance = null;    
    private static Logger log = Logger.getLogger(CacheParametrosBD.class);
    private static SortedMap listaParametrosBD = null;
    
    private CacheParametrosBD(){ 
        synchronized (this) {
            listaParametrosBD = AutorizacionesExternasDAO.getInstance().cargaCacheParametrosBD();
        }
    }
   
    public static CacheParametrosBD getInstance(){
        if (instance==null){
            synchronized (CacheParametrosBD.class) {
                instance = new CacheParametrosBD();
            }
        }
        return instance;
    }

    public SortedMap getDatos(){
        return listaParametrosBD;
    }
    
    public SortedMap getDatosBD(String jndi) {
        return listaParametrosBD;
    }
    
    public ParametrosBDVO getDatoClaveUnica(String ... clave) {
        ParametrosBDVO params = null;
        if (clave!=null && clave.length == 3){
            ArrayList<String> claveUnica = new ArrayList<String>();
            for (String elem : clave) {
                claveUnica.add(elem);
            }
            params = (ParametrosBDVO)listaParametrosBD.get(claveUnica);
        }
        return params;
    }
    
    public boolean eliminarDatoClaveUnica(String ... clave) {
        boolean resultado = false;
        if (clave!=null && clave.length == 3){
            ArrayList<String> claveUnica = new ArrayList<String>(3);
            for (String elem : clave) {
                claveUnica.add(elem);
            }
            listaParametrosBD.remove(claveUnica);
            resultado = true;
        }
        return resultado;
    }
    
    public boolean actualizarDatoClaveUnica(Object datoNuevo, String ... claveAnt) {
        boolean resultado = false;
         if (datoNuevo != null && datoNuevo instanceof ParametrosBDVO && 
                 claveAnt!=null &&claveAnt.length == 3) {
            ArrayList<String> claveUnica = new ArrayList<String>(3);
            for (String elem : claveAnt) {
                claveUnica.add(elem);
            }
            listaParametrosBD.remove(claveUnica);
            ParametrosBDVO parametrosBD = (ParametrosBDVO) datoNuevo;
            ArrayList <String> clave = new ArrayList<String>(3); 
            clave.add(String.valueOf(parametrosBD.getCodOrganizacion()));
            clave.add(String.valueOf(parametrosBD.getCodEntidad()));
            clave.add(String.valueOf(parametrosBD.getCodAplicacion()));
            listaParametrosBD.put(clave, parametrosBD);
            resultado = true;
         }
         return resultado;
    }
    
    public boolean insertarDato(Object dato) {
        boolean resultado = false;
        if (dato != null && dato instanceof ParametrosBDVO) {
             ParametrosBDVO parametrosBD = (ParametrosBDVO) dato;
             ArrayList <String> clave = new ArrayList<String>(3); 
             clave.add(String.valueOf(parametrosBD.getCodOrganizacion()));
             clave.add(String.valueOf(parametrosBD.getCodEntidad()));
             clave.add(String.valueOf(parametrosBD.getCodAplicacion()));
             listaParametrosBD.put(clave, parametrosBD);
             resultado = true;
        }
        return resultado;
    }
}
