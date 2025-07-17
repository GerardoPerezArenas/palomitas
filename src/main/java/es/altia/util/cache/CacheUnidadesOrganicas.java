/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.util.cache;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.common.service.config.Config;
import es.altia.common.util.commons.Pair;
import java.util.ArrayList;
import java.util.SortedMap;
import org.apache.log4j.Logger;

/**
 *
 * @author adrian
 */
public class CacheUnidadesOrganicas implements CacheDatosGenericos {
    
    private static CacheUnidadesOrganicas instance = null;    
    private static Logger log = Logger.getLogger(CacheUnidadesOrganicas.class);
    protected static Config m_ConfigTechnical;
    protected static String gestorD;
    private static SortedMap unidadesOrganicasPorJndiCodVis = null;
    private static SortedMap unidadesOrganicasPorJndiCodUor = null;
    
    private CacheUnidadesOrganicas(){ 
        synchronized (this) {
            Pair<SortedMap,SortedMap> mapas = UORsDAO.getInstance().cargaCacheUnidadesOrganicas();
            unidadesOrganicasPorJndiCodVis = mapas.getFirst();
            unidadesOrganicasPorJndiCodUor = mapas.getSecond();
        }
    }
   
    public static CacheUnidadesOrganicas getInstance(){
        if (instance==null){
            synchronized (CacheUnidadesOrganicas.class) {
                instance = new CacheUnidadesOrganicas();
            }
        }
        
        return instance;
    }

    public SortedMap getDatos(){
        return unidadesOrganicasPorJndiCodUor;
    }
    
    public SortedMap getDatosBD(String jndi) {
        return (SortedMap)unidadesOrganicasPorJndiCodVis.get(jndi);
    }

    public UORDTO getDatoClaveUnica(String ... clave) {
        UORDTO uorDTO = null;
        if (clave!=null && clave.length == 2){
            String jndi = clave[0];
            String codUor = clave[1];
            uorDTO = (UORDTO)((SortedMap)unidadesOrganicasPorJndiCodUor.get(jndi)).get(codUor);
        }
        return uorDTO;
    }
    
    public boolean eliminarDatoClaveUnica(String ... clave) {
        boolean resultado = false;
        if (clave!=null && clave.length == 2){
            synchronized (this) {
                String jndi = clave[0];
                String codUor = clave[1];
                UORDTO uorDTO = (UORDTO)((SortedMap)unidadesOrganicasPorJndiCodUor.get(jndi)).get(codUor);
                ArrayList <String> claveVis = new ArrayList<String>(3); 
                claveVis.add(uorDTO.getUor_cod_vis());
                claveVis.add(uorDTO.getUor_nom());
                claveVis.add(uorDTO.getUor_estado());
                ((SortedMap)unidadesOrganicasPorJndiCodVis.get(jndi)).remove(claveVis);
                ((SortedMap)unidadesOrganicasPorJndiCodUor.get(jndi)).remove(codUor);
            }
            resultado = true;
        }
        return resultado;
    }
    
    public boolean actualizarDatoClaveUnica(Object datoNuevo, String ... claveAnt) {
        boolean resultado = false;
        if (datoNuevo != null && datoNuevo instanceof UORDTO && 
                claveAnt!=null &&claveAnt.length == 2) {
            synchronized (this) {
                String jndi = claveAnt[0];
                String codUor = claveAnt[1];
                UORDTO uorDTOAnt = (UORDTO)((SortedMap)unidadesOrganicasPorJndiCodUor.get(jndi)).get(codUor);
                ArrayList <String> claveVisAnt = new ArrayList<String>(3); 
                claveVisAnt.add(uorDTOAnt.getUor_cod_vis());
                claveVisAnt.add(uorDTOAnt.getUor_nom());
                claveVisAnt.add(uorDTOAnt.getUor_estado());
               ((SortedMap)unidadesOrganicasPorJndiCodVis.get(jndi)).remove(claveVisAnt);
               ((SortedMap)unidadesOrganicasPorJndiCodUor.get(jndi)).remove(codUor);

               UORDTO uorDTO = (UORDTO) datoNuevo;
               ArrayList <String> clave = new ArrayList<String>(3); 
               clave.add(String.valueOf(uorDTO.getUor_cod_vis()));
               clave.add(String.valueOf(uorDTO.getUor_nom()));
               clave.add(String.valueOf(uorDTO.getUor_estado()));
               ((SortedMap)unidadesOrganicasPorJndiCodVis.get(jndi)).put(clave,uorDTO);
               ((SortedMap)unidadesOrganicasPorJndiCodUor.get(jndi)).put(uorDTO.getUor_cod(),uorDTO);
            }
           resultado = true;
        }
        return resultado;
    }
    
    public boolean insertarDato(Object dato) {
        boolean resultado = false;
        if (dato != null && dato instanceof UORDTO) {
            UORDTO uorDTO = (UORDTO) dato;
            if (uorDTO.getJndi()!=null && !"".equals(uorDTO.getJndi())) {
                synchronized (this) {
                    ArrayList <String> clave = new ArrayList<String>(3);
                    clave.add(uorDTO.getUor_cod_vis());
                    clave.add(uorDTO.getUor_nom());
                    clave.add(uorDTO.getUor_estado());
                    ((SortedMap)unidadesOrganicasPorJndiCodVis.get(uorDTO.getJndi())).put(clave,uorDTO);
                    ((SortedMap)unidadesOrganicasPorJndiCodUor.get(uorDTO.getJndi())).put(uorDTO.getUor_cod(),uorDTO);
                } 
                resultado = true;
            }
        }
        return resultado;
    }
}
