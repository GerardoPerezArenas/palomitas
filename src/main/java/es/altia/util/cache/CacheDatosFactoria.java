/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.cache;

import org.apache.log4j.Logger;

/**
 *
 * @author adrian
 */
public abstract class CacheDatosFactoria {
    
    private static Logger log = Logger.getLogger(CacheDatosFactoria.class);
        
    private CacheDatosFactoria(){ }
           
    public static CacheDatosGenericos getImplOrganizaciones(){
        return CacheOrganizaciones.getInstance();
    }
    
    public static CacheDatosGenericos getImplEntidades(){
        return CacheEntidades.getInstance();
    }
    
    public static CacheDatosGenericos getImplParametrosBD(){
        return CacheParametrosBD.getInstance();
    }

    public static CacheDatosGenericos getImplUnidadesOrganicas(){
        return CacheUnidadesOrganicas.getInstance();
    }
}
