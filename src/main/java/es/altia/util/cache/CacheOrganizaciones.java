/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.cache;

import es.altia.agora.business.administracion.OrganizacionVO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import java.util.SortedMap;
import org.apache.log4j.Logger;

/**
 *
 * @author adrian
 */
public class CacheOrganizaciones implements CacheDatosGenericos {
    
    private static CacheOrganizaciones instance = null;    
    private static Logger log = Logger.getLogger(CacheOrganizaciones.class);
    SortedMap listaOrganizaciones = null;
    
    private CacheOrganizaciones(){ 
        synchronized (this) {
            listaOrganizaciones = OrganizacionesDAO.getInstance().cargaCacheOrganizaciones();
        }
    }
   
    public static CacheOrganizaciones getInstance(){
        if (instance==null){
            synchronized (CacheOrganizaciones.class) {
                instance = new CacheOrganizaciones();
            }
        }
        return instance;
    }

    public SortedMap getDatos() {
        return listaOrganizaciones;
    }
    
    public SortedMap getDatosBD(String jndi) {
        return listaOrganizaciones;
    }
    
    public OrganizacionVO getDatoClaveUnica(String ... clave) {
        OrganizacionVO org = null;
        if (clave!=null && clave.length == 1) {
            org = (OrganizacionVO)listaOrganizaciones.get(clave[0]);
        }
        return org;
    }
    
    public boolean eliminarDatoClaveUnica(String ... clave) {
        boolean resultado = false;
        if (clave!=null && clave.length == 1) {
            listaOrganizaciones.remove(clave[0]);
            resultado = true;
        }
        return resultado;
    }
    
    public boolean actualizarDatoClaveUnica(Object datoNuevo, String ... claveAnt) {
        boolean resultado = false;
        if (datoNuevo != null && datoNuevo instanceof OrganizacionVO && claveAnt!=null && claveAnt.length == 1) {
            listaOrganizaciones.remove(claveAnt[0]);
            OrganizacionVO organizacion = (OrganizacionVO) datoNuevo;
            listaOrganizaciones.put(String.valueOf(organizacion.getCodOrganizacion()), organizacion);
            resultado = true;
        }
        return resultado;
    }
    
    public boolean insertarDato(Object dato) {
        boolean resultado = false;
        if (dato != null && dato instanceof OrganizacionVO) {
            OrganizacionVO organizacion = (OrganizacionVO) dato;
            listaOrganizaciones.put(String.valueOf(organizacion.getCodOrganizacion()), organizacion);
            resultado = true;
        }
        return resultado;
    }
}
