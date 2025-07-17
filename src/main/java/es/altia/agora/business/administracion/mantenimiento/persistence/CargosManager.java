package es.altia.agora.business.administracion.mantenimiento.persistence;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.CargosDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;

import java.util.ArrayList;
import java.util.Vector;

import es.altia.common.service.config.*;
import es.altia.arboles.Nodo;
import es.altia.arboles.impl.ArbolImpl;
import es.altia.arboles.impl.NodoImpl;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 * Gestor de DAOs para A_UOR
 */
public class CargosManager  {
    private static CargosManager instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion t�cnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log log =
            LogFactory.getLog(CargosManager.class.getName());

    private static final int GRADO_ANIDAMIENTO = 50;


    /**
     *  Constructor singleton
     */
    private CargosManager() {
        // Queremos usar el fichero de configuraci�n technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

    /**
     * Singleton
     * @return Instancia unica
     */
    public static CargosManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizaci�n aqu� para serializar (no multithread)
            // las invocaciones a este metodo
            synchronized(CargosManager.class) {
                if (instance == null) {
                    instance = new CargosManager();
                }
            }
        }
        return instance;
    }


    /**
     * Crea un �rbol con la jerarqu�a de Unidades Org�nicas
     * @param soloVisiblesRegistro <i>true</i> si el �rbol se mostrar� en un po-up, en cuyo caso no se muestran
     * las unidades marcadas con "no visible en registro"     *
     * @param params Par�metros de conexi�n
     * @return Instancia del �rbol
     */
    public ArbolImpl getArbolUORs(boolean soloVisiblesRegistro, String[] params) {
        Vector listaUors = null;

        // si es un pop-up, no sacar las que son "no visible en registro"
        if(soloVisiblesRegistro == true) {
            listaUors = getListaUORsPorNoVisRegistro('0', params);
        }
        else {
            listaUors = getListaUORs(params);
        }


        // crear �rbol y ra�z
        NodoImpl raiz = new NodoImpl(null);
        ArbolImpl arbol = new ArbolImpl(raiz);

        // a�adir primero nodos q dependen directamente de la ra�z
        for(int i=0; i<listaUors.size(); i++) {
            UORDTO dto = (UORDTO)listaUors.get(i);
            if(dto.getUor_pad() == null) {
                raiz.addHijo(new NodoImpl(dto, dto.getUor_cod()));
                listaUors.set(i, null);
            }
        }

        // crear array con s�lo hijos (no forzosamente hojas)
        Vector soloHijos = new Vector();
        for(int i=0; i<listaUors.size(); i++) {
            UORDTO dto = (UORDTO)listaUors.get(i);
            // si el objeto no es null (arriba hemos marcado padres con null), a�adir
            if(dto != null) {
                soloHijos.add(dto);
            }
        }

        /* recorrer los elementos restantes hasta un n�mero de iteraciones igual
         al grado m�ximo de anidamiento, y a�adir al �rbol. Los que se van colocando
          incrementan la variable y se reemplazan con null en la lista */
        int iteraciones = 0; // del while
        int colocados = 0; // n�mero de nodos hijo ya colocados en el �rbol
        while((iteraciones < GRADO_ANIDAMIENTO)&&(colocados<soloHijos.size())) {
            for(int i=0; i<soloHijos.size(); i++) {
                UORDTO dto = (UORDTO)soloHijos.get(i);
                if(dto != null) {
                    //log.info("Colocamos: " + dto.toString());
                    Nodo encontrado = arbol.buscarEnProfNodoClave(arbol.getRaiz(), dto.getUor_pad());
                    if(encontrado != null) {
                        //log.info("Colocado como hijo de " + encontrado.getClave());
                        //log.info("encontrado " + dto.getUor_pad());
                        encontrado.addHijo(new NodoImpl(dto, dto.getUor_cod()));
                        colocados++;
                        soloHijos.set(i, null);
                    }
                    else {
                        //log.info("No colocado");
                    }
                }
            }
            iteraciones++;
        }
                
        return(arbol);
    }

    /**
     * Obtiene lista de todos los DTOs de las Unidades Organizativas
     * @param params Par�metros de conexi�n
     * @return Lista de registros
     */
    public Vector getListaUORs(String[] params) {
        log.debug("getListaUORs en UORsManager");
        // obtener singleton del DAO
        CargosDAO UORDTO  = CargosDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            resultado = UORDTO.getListaUORs(params);
            return resultado;
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    /**
     * Obtiene lista de todos los DTOs de las Unidades Organizativas
     * @param params Par�metros de conexi�n
     * @return Lista de registros
     */
    public Vector getListaUOROrdenPorDesc(String[] params) {
        log.debug("getListaUORs en UORsManager");
        // obtener singleton del DAO
        CargosDAO UORDTO  = CargosDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            resultado = UORDTO.getListaUOROrdenPorDesc(params);
            return resultado;
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    /**
     * Obtiene lista de todos los DTOs de las Unidades Organizativas con el c�digo dado
     * @param params Par�metros de conexi�n
     * @return Lista de registros
     */
    public Vector getListaUORsPorCodigo(int codigo, String[] params){
        log.debug("getListaUORValue");
        CargosDAO UORDTO  = CargosDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            //resultado = UORDTO.getListaUORs(gVO,params);
            resultado = UORDTO.getListaUORsPorCodigo(codigo, params);
            return resultado;
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    /**
     * Obtiene lista de todos los DTOs de las Unidades Organizativas con el valor de
     * "no visible en registro" dado
     * @param params Par�metros de conexi�n
     * @return Lista de registros
     */
    public Vector getListaUORsPorNoVisRegistro(char noVisible,String[] params){
        log.debug("getListaUORValue");
        CargosDAO UORDTO  = CargosDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            //resultado = UORDTO.getListaUORs(gVO,params);
            resultado = UORDTO.getListaUORsPorNoVisRegistro(noVisible, params);
            return resultado;
        } catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    public int eliminarUOR(int codigo, String[] params) {
        log.debug("eliminarUOR");
        CargosDAO UORDTO = CargosDAO.getInstance();
        int resultado = 0;
        try {
            resultado = UORDTO.eliminarUORPorCodigo(codigo, params);
        } catch (Exception e){
            log.error("Excepci�n capturada eliminando UOR: " + e.toString());
            resultado = -1;
        }
        return resultado;
    }

    public int usuariosCatalogoAfectados(int codigo, String[] params, String estado,String codVisible) {
        log.debug("eliminarUOR");
        CargosDAO cargos = CargosDAO.getInstance();
        int resultado = 0;
        try {
            resultado = cargos.usuariosCatalogoAfectados(params, codigo, estado, codVisible);
        } catch (Exception e){
            log.error("Excepci�n capturada en comprobacion de cargos del cat�logo: " + e.toString());
            resultado = -1;
        }
        return resultado;
    }

    public int modificarUOR(UORDTO dto, String[] params){
        log.debug("modificarUOR");
        CargosDAO UORDTO = CargosDAO.getInstance();
        int resultado = 0;
        try{
            resultado = UORDTO.modificarUOR(dto, params);
        }catch(Exception e){
            log.error("Excepci�n capturada modificando UOR: " + e.toString());
        }
        return resultado;
    }

    public int altaUOR(UORDTO dto, String[] params) {
        log.debug("altaUOR");
        CargosDAO UORDTO  = CargosDAO.getInstance();
        int resultado = 0;
        try{
            resultado = UORDTO.altaUOR(dto, params);
        }catch (Exception e) {
            log.error("Excepci�n capturada en alta de UOR: " + e.toString());
        }
        return resultado;
    }

}