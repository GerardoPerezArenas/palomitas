package es.altia.agora.business.administracion.mantenimiento.persistence;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UorPermisoVO;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import java.util.Vector;

import es.altia.common.service.config.*;
import es.altia.arboles.Nodo;
import es.altia.arboles.impl.ArbolImpl;
import es.altia.arboles.impl.NodoImpl;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;


/**
 * Gestor de DAOs para A_UOR
 */
public class UORsManager  {
    private static UORsManager instance = null;
    protected static Config m_ConfigTechnical; // Para el fichero de configuracion t�cnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log log =
            LogFactory.getLog(UORsManager.class.getName());

    private static final int GRADO_ANIDAMIENTO = 50;


    /**
     *  Constructor singleton
     */
    private UORsManager() {
        // Queremos usar el fichero de configuraci�n technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

    /**
     * Singleton
     * @return Instancia unica
     */
    public static UORsManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized(UORsManager.class) {
                if (instance == null) {
                    instance = new UORsManager();
                }
            }
        return instance;
    }


    /**
     * Crea un �rbol con la jerarqu�a de Unidades Org�nicas
     * @param recuperarTodo Indica si se recpueran todas las unidades org�nicas
     * @param soloVisiblesRegistro <i>true</i> si el �rbol se mostrar� en un po-up, en cuyo caso no se muestran
     * las unidades marcadas con "no visible en registro"     *
     * @param params Par�metros de conexi�n
     * @return Instancia del �rbol
     */
    public ArbolImpl getArbolUORs(boolean recuperarTodo,boolean soloVisiblesRegistro,
            boolean ordenarNombre, String[] params) {
        Vector listaUors = null;

        // si es un pop-up, no sacar las que son "no visible en registro"
        if (!recuperarTodo && soloVisiblesRegistro == true && ordenarNombre==true) 
            listaUors = getListaUORsPorNombreNoVisRegistro('0', params);
        else  if (!recuperarTodo && soloVisiblesRegistro == true && ordenarNombre==false) 
            listaUors = getListaUORsPorNoVisRegistro('0', params);
        else if (ordenarNombre==true) 
            listaUors = getListaUORsPorNombre(recuperarTodo,params);
        else
            listaUors = getListaUORs(recuperarTodo,params);
        


        // crear �rbol y ra�z
        NodoImpl raiz = new NodoImpl(null);
        
       

        // a�adir primero nodos q dependen directamente de la ra�z
        for(int i=0; i<listaUors.size(); i++) {
            UORDTO dto = (UORDTO)listaUors.get(i);
            if(dto.getUor_pad() == null) {
                raiz.addHijo(new NodoImpl(dto, dto.getUor_cod()));//clave es el codigo
               
                //arbol.getRaiz().setClave(dto.getUor_pad());
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
        ArbolImpl arbol = new ArbolImpl(raiz);
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
                        if (ordenarNombre==true)
                        encontrado.addHijoOrdenado(new NodoImpl(dto, dto.getUor_cod()));
                        else encontrado.addHijo(new NodoImpl(dto, dto.getUor_cod()));
                        //log.info("informacion: "+encontrado.getInformacion().toString());
                        
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
    
    
     public ArbolImpl getArbolUORsPermisoUsuario(boolean soloVisiblesRegistro,boolean ordenarNombre, UsuarioValueObject usuario,String[] params) {
        Vector listaUors = null;

        // si es un pop-up, no sacar las que son "no visible en registro"
        if((soloVisiblesRegistro == true)&&(ordenarNombre==true)) {
            listaUors = getListaUORsPorNombreNoVisRegistroPermisoUsuario('0', usuario, params);
        } else 
        if((soloVisiblesRegistro == true)&&(ordenarNombre==false)) {
            listaUors = getListaUORsPorNoVisRegistroPermisoUsuario('0',usuario, params);
        }
        else if (ordenarNombre==true){
            listaUors = getListaUORsPorNombrePermisoUsuario(usuario,params);
        }
        else
            listaUors = getListaUORsPermisoUsuarioNuevo(usuario,params);
        


        // crear �rbol y ra�z
        NodoImpl raiz = new NodoImpl(null);
        
       

        // a�adir primero nodos q dependen directamente de la ra�z
        for(int i=0; i<listaUors.size(); i++) {
            UORDTO dto = (UORDTO)listaUors.get(i);
            if(dto.getUor_pad() == null) {
                raiz.addHijo(new NodoImpl(dto, dto.getUor_cod()));//clave es el codigo
               
                //arbol.getRaiz().setClave(dto.getUor_pad());
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
        ArbolImpl arbol = new ArbolImpl(raiz);
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
                        if (ordenarNombre==true)
                        encontrado.addHijoOrdenado(new NodoImpl(dto, dto.getUor_cod()));
                        else encontrado.addHijo(new NodoImpl(dto, dto.getUor_cod()));
                        //log.info("informacion: "+encontrado.getInformacion().toString());
                        
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

     
    
     public Vector getListaUORsPorNombre(boolean recuperarOcultas,String[] params) {
        log.debug("getListaUORs en UORsManager");
        // obtener singleton del DAO
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            resultado = uorDAO.getListaUORsPorNombre(recuperarOcultas,params);
            return resultado;
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }
     
     public Vector getListaUORsPorNombrePermisoUsuario(UsuarioValueObject usuario,String[] params) {
        log.debug("getListaUORs en UORsManager");
        // obtener singleton del DAO
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            resultado = uorDAO.getListaUORsPorNombrePermisoUsuario(usuario,params);
            return resultado;
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }
     

    
    
    /**
     * Obtiene lista de todos los DTOs de las Unidades Organizativas
     * @param recuperarOcultas Indica si se recpueran las unidades ocultas
     * @param params Par�metros de conexi�n
     * @return Lista de registros
     */
    public Vector getListaUORs(boolean recuperarOcultas, String[] params) {
        log.debug("getListaUORs en UORsManager");
        // obtener singleton del DAO 
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            resultado = uorDAO.getListaUORs(recuperarOcultas, params);
            return resultado;
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }


     public Vector getListaUORsDeAlta(String[] params) {
        log.debug("getListaUORsDeAlta en UORsManager");
        // obtener singleton del DAO
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            resultado = uorDAO.getListaUORsDeAlta(params);
            return resultado;
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    public Vector getListaUOROrdenPorDesc(String[] params) {
        log.debug("getListaUORs en UORsManager");
        // obtener singleton del DAO
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            resultado = uorDAO.getListaUOROrdenPorDesc(params);
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
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            //resultado = uorDAO.getListaUORs(gVO,params);
            resultado = uorDAO.getListaUORsPorCodigo(codigo, params);
            return resultado;
        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }
    
    
     public Vector getListaUORsPorCodigoPermisoUsuario(int codigo, UsuarioValueObject usuario,String[] params){
        log.debug("getListaUORValue");
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try{
            log.debug("Usando persistencia manual");
            //resultado = uorDAO.getListaUORs(gVO,params);
            resultado = uorDAO.getListaUORsPorCodigoPermisoUsuario(codigo,usuario, params);
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
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            //resultado = uorDAO.getListaUORs(gVO,params);
            resultado = uorDAO.getListaUORsPorNoVisRegistro(noVisible, params);
            return resultado;
        } catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }
    
    public Vector getListaUORsPorNoVisRegistroPermisoUsuario(char noVisible,UsuarioValueObject usuario,String[] params){
        log.debug("getListaUORValue");
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            //resultado = uorDAO.getListaUORs(gVO,params);
            resultado = uorDAO.getListaUORsPorNoVisRegistroPermisoUsuario(noVisible, usuario,params);
            return resultado;
        } catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }
    
    
    public Vector getListaUORsPorNombreNoVisRegistro(char noVisible,String[] params){
        log.debug("getListaUORValue");
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            //resultado = uorDAO.getListaUORs(gVO,params);
            resultado = uorDAO.getListaUORsPorNombreNoVisRegistro(noVisible, params);
            return resultado;
        } catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }
    
    public Vector getListaUORsPorNombreNoVisRegistroPermisoUsuario(char noVisible, UsuarioValueObject usuario,String[] params){
        log.debug("getListaUORValue");
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            //resultado = uorDAO.getListaUORs(gVO,params);
            resultado = uorDAO.getListaUORsPorNombreNoVisRegistroPermisoUsuario(noVisible,usuario, params);
            return resultado;
        } catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }
    
    
    
   /**
     * Obtiene lista de todos los DTOs de las Unidades Organizativas que sean de 
    *  registro (RES_TIP = 1)
     * @param params Par�metros de conexi�n
     * @return Lista de registros
     */
    public Vector getListaUORsDeRegistro(String[] params){
        log.debug("getListaUORsDeRegistro");
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = uorDAO.getListaUORsDeRegistro(params);
            return resultado;
        } catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    /**
     * Obtiene lista de todos los DTOs de las Unidades Organizativas que sean de
     * registro (RES_TIP = 1), pero solo devuelve codigo y descripcion.
     * @param params Par�metros de conexi�n
     * @param usuario Usuario
     * @return Lista de registros
     */
    public Vector getListaSimpleUORsDeRegistroUsuario(String[] params, UsuarioValueObject usuario){
        log.debug("getListaUORsDeRegistro");
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try {
            log.debug("Usando persistencia manual");
            resultado = uorDAO.getListaSimpleUORsDeRegistroUsuario(params, usuario);
            return resultado;
        } catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
        }
    }

    public int eliminarUOR(int codigo, String[] params, String organizacion) {
        log.debug("eliminarUOR");
        UORsDAO uorDAO = UORsDAO.getInstance();
        int resultado = 0;
        try {
            resultado = uorDAO.eliminarUORPorCodigo(codigo, params, organizacion);
        } catch (Exception e){
            log.error("Excepci�n capturada eliminando UOR: " + e.toString());
        }
        return resultado;
    }

    public int modificarUOR(UORDTO dto, String[] params, String organizacion){
        log.debug("modificarUOR");
        UORsDAO uorDAO = UORsDAO.getInstance();
        int resultado = 0;
        try{
            resultado = uorDAO.modificarUOR(dto, params, organizacion);
        }catch(Exception e){
            log.error("Excepci�n capturada modificando UOR: " + e.toString());
        }
        return resultado;
    }

    public int modificarUORCodAccede(UORDTO dto, String[] params){
        log.debug("modificarUORCodAccede");
        UORsDAO uorDAO = UORsDAO.getInstance();
        int resultado = 0;
        try{
            resultado = uorDAO.modificarUORCodAccede(dto, params);
        }catch(Exception e){
            log.error("Excepci�n capturada modificando UOR: " + e.toString());
        }
        return resultado;
    }

    public int altaUOR(UORDTO dto, String[] params) {
        log.debug("altaUOR");
        UORsDAO uorDAO  = UORsDAO.getInstance();
        int resultado = 0;
        try{
            resultado = uorDAO.altaUOR(dto, params);
        }catch (Exception e) {
            log.error("Excepci�n capturada en alta de UOR: " + e.toString());
        }
        return resultado;
    }

    /**
     * Devuelve el c�digo de la �ltima unidad organizativa creada una vez que haya sido 
     * dada de alta
     * @return Un int
     */
    public int getLastUnidadOrganizativaCreada()
    {
        return UORsDAO.getInstance().getLastUnidadOrganizativaCreada();
    }


   /**
     * Recupera el c�digo visible de uor a partir del c�digo interno de una uor
     * @param codUor: C�digo de uor visible
     * @param params: Par�metros de conexi�n a la base de datos
     * @return C�digo visible de uor o null si no se ha podido recuperar
     */
    public String getCodigoVisibleUorByCodUor(String codUor,String[] params){
        Connection con = null;
        String salida = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            salida = UORsDAO.getInstance().getCodigoVisibleUorByCodUor(codUor, params[6]);

        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                // Se cierra la conexi�n a la base de datos
                if(con!=null) con.close();                
            }catch(SQLException e){
                e.printStackTrace();
            }
            
        }// finally
        return salida;
    }


       /**
     * Comprueba la existencia de una uor buscando por el c�digo visible de la misma
     * @param codUor: C�digo de uor visible
     * @param params: Par�metros de conexi�n a la base de datos
     * @return boolean
     */
    public boolean existeUorByCodigoVisible(String codUor,String[] params){
        Connection con = null;
        boolean salida = false;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            salida = UORsDAO.getInstance().existeUorByCodigoVisible(codUor, con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                // Se cierra la conexi�n a la base de datos
                if(con!=null) con.close();

            }catch(SQLException e){
                e.printStackTrace();
            }

        }// finally
        return salida;
    }

     /**
     * Recupera el nombre de una uor a partir del c�digo de uor visible
     * @param codUor: C�digo de uor visible
     * @param params: Par�metros de conexi�n a la base de datos
     * @return Nombre la uor o null si no existe
     */
    public String getNombreByCodUorVisible(String codUor,String[] params){
        Connection con = null;
        String salida = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            salida = UORsDAO.getInstance().getNombreByCodUorVisible(codUor, con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                // Se cierra la conexi�n a la base de datos
                if(con!=null) con.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }// finally
        return salida;
    }
    
    
     /**
     * Recupera el nombre de una uor a partir del c�digo de uor interno
     * @param codUor: C�digo de uor interno
     * @param params: Par�metros de conexi�n a la base de datos
     * @return Nombre la uor o null si no existe
     */
    public String getNombreByCodUor(String codUor,String[] params){
        Connection con = null;
        String salida = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            salida = UORsDAO.getInstance().getDescripcionUOR(Integer.parseInt(codUor), con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                // Se cierra la conexi�n a la base de datos
                if(con!=null) con.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }// finally
        return salida;
    }



   /**
     * Recupera la lista de uors sobre las que tiene permiso un determinado usuario
     * @param usuario: Objeto de tipo UsuarioValueObject que contiene la informaci�n del usuario
     * @param con: Conexi�n a la BBDD
     * @param abd: AdaptadorSQLBD
     * @return Vector
     * @throws es.altia.common.exception.TechnicalException
     */
    public Vector getListaUORsPermisoUsuario(UsuarioValueObject usuario, String[] params) {
        log.debug("getListaUORsPermisoUsuario en UORsManager");
        // obtener singleton del DAO
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            log.debug("Usando persistencia manual");
            resultado = uorDAO.getListaUORsPermisoUsuario(usuario,con, adapt);
            return resultado;

        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
            
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                log.error("Error al cerrar la conexi�n: " + e.getMessage());
            }
        }
    }
    
    
    
    public Vector getListaUORsPermisoUsuarioNuevo(UsuarioValueObject usuario, String[] params) {
        log.debug("getListaUORsPermisoUsuario en UORsManager");
        // obtener singleton del DAO
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        UORsDAO uorDAO  = UORsDAO.getInstance();
        Vector resultado = new Vector();
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            log.debug("Usando persistencia manual");
            resultado = uorDAO.getListaUORsPermisoUsuarioNuevo(usuario,con, adapt);
            return resultado;

        }catch(Exception ce){
            log.error("JDBC Technical problem " + ce.getMessage());
            return resultado;
            
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                log.error("Error al cerrar la conexi�n: " + e.getMessage());
            }
        }
    }
    
    
    
    
    /************************ OFICINAS DE REGISTRO ***********************************/
    
    
    /**
     * Comprueba para las unidades a asignar al usuario (par�metro unidades), cuantas hay que sean oficinas de registro de una misma unidad padre
     * @param oficinasRegistro: Colecci�n con las oficinas de registro hijas de una unidad de registro determinada que se asigna a un usuario
     * @param unidades: Unidades a asignar al usuario
     */
    private ArrayList<UORDTO> buscarOficinasRegistroHermanas(ArrayList<UORDTO> oficinasRegistro,Vector<String> unidades){
        
        ArrayList<UORDTO> salida = new ArrayList<UORDTO>();
        for(int i=0;oficinasRegistro!=null && i<oficinasRegistro.size();i++){
            if(unidades.contains(oficinasRegistro.get(i).getUor_cod())){
                // Se devuelve una colecci�n con las oficinas de registro que se est�n asignando al usuario y que son hijas todas
                // de una misma unidad de registro.
                salida.add(oficinasRegistro.get(i));
            }
        }
        
        return salida;        
    }
    
    
    /**
     * Recupera la uor del par�metro codUor y devuelve un objeto con la informaci�n del mismo
     * param uors: Colecci�n de todas las uors
     * @param codUor: C�digo del uor
     * @return Objeto UORDTO o null sino se encuentra
     */
    private UORDTO getInfoUor(Vector uors,String codUor){
        UORDTO dato = null;
        
        for(int i=0;uors!=null && i<uors.size();i++){
            UORDTO uor = (UORDTO)uors.get(i);
            if(uor.getUor_cod().equalsIgnoreCase(codUor)){
                dato = uor;
                break;
            }// if
        }// for
        
        return dato;
    }
   
    
    private String tratarListadoOficinas(ArrayList<UORDTO> listado){
       
       StringBuffer sb = new StringBuffer();       
       for(int i=0;listado!=null && i<listado.size();i++){           
            sb.append("<OFICINA>");
            /*String texto=listado.get(i).getUor_cod_vis();
            texto=texto+"#"+listado.get(i).getUor_nom();
            sb.append("<![CDATA["+texto+"]]>");
            */
            sb.append(listado.get(i).getUor_cod_vis());
            sb.append("#");                
            sb.append(listado.get(i).getUor_nom());              
            
            sb.append("</OFICINA>");
       }
       return sb.toString();       
    }
    
    
    /**
     * Comprueba si se est� asignando permiso al usuario sobre una determinada uor
     * @param codUorPadre: C�digo interno de la UOR
     * @param unidades: Unidades a asignar al usuario
     * @return true si se asigna permiso y false en otro caso
     */
    private boolean seAsignaPermisoSobreUorPadreRegistro(String codUorPadre,Vector<String> unidades){
        boolean exito = false;        
        
        if(unidades.contains(codUorPadre)) exito = true;        
        return exito;        
    }
    
    private Vector<String> transformarColeccionUORS(Vector uors){
        Vector<String> salida = new Vector<String>();
        for(int i=0;uors!=null && i<uors.size();i++){
            UORDTO uor = (UORDTO)uors.get(i);
            salida.add(uor.getUor_cod());
            
        }     
        return salida;
    }
    
    
    /***
    public String verificarAsignacionPermisosOficinasRegistro(Vector unidades,String[] params) throws TechnicalException{
        log.debug("verificarAsignacionPermisosOficinasRegistro =====>");
        
        String salida = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
                        
            // Se recupera todas las unidades organizativas
            Vector UORs = UORsDAO.getInstance().getListaUOROrdenPorDesc(con,params);
            
            // SE COMPRUEBA SI SE EST�N ASIGNANDO TODAS LAS UNIDADES AL USUARIO
            if(unidades!=null && unidades.size()==1 && unidades.get(0).equals("-1")){
                unidades = transformarColeccionUORS(UORs);
            }

            StringBuffer sb = new StringBuffer();
            int contadorErrores = 0;
            for(int i=0;unidades!=null && i<unidades.size();i++)
            {
                String codUnidad = (String)unidades.get(i);
                log.debug("Unidad a tratar: " + codUnidad);

                // SE OBTIENE INFORMACI�N DE LA UNIDAD ORGANIZATIVA
                UORDTO uor = getInfoUor(UORs,codUnidad);
                
                if(uor!=null){                                                    
                                        
                    if(uor.getUor_tipo()!=null && "1".equals(uor.getUor_tipo())){
                        // Si la uor es de registro. Se comprueba si tiene hijas
                        log.debug(" *** LA UNIDAD " + codUnidad + " es de tipo registro");
                        
                        ArrayList<UORDTO> hijas = UORsDAO.getInstance().getListaUORHijas(Integer.parseInt(uor.getUor_cod()),true,con);
                        
                        // Se comprueba si se est�n asignando permisos sobre las oficinas de registro hijas de la uor actual
                        ArrayList<UORDTO> oficinasRegistroHermanas = buscarOficinasRegistroHermanas(hijas,unidades);            
                        
                        if(hijas!=null && hijas.size()>=1){
                            // LA UNIDAD DE REGISTRO TIENE M�S DE UNA OFICINA DE REGISTRO COMO HIJA
                                                        
                            if(oficinasRegistroHermanas!=null && oficinasRegistroHermanas.size()>1){                                
                                // SE EST� ASIGNADA M�S DE UN PERMISO SOBRE UNA OFICINA DE REGISTRO DE UN MISMO PADRE => ERROR
                                
                                sb.append("<ERROR>");
                                sb.append("<STATUS>");
                                sb.append("1");
                                sb.append("</STATUS>");
                                sb.append("<PADRE_COD_VISIBLE>");
                                sb.append(uor.getUor_cod_vis());
                                sb.append("</PADRE_COD_VISIBLE>");
                                sb.append("<PADRE_DESCRIPCION>");
                                sb.append(uor.getUor_nom());
                                sb.append("</PADRE_DESCRIPCION>");
                                
                                String oficinas = tratarListadoOficinas(oficinasRegistroHermanas);                                
                                if(oficinas!=null && !"".equals(oficinas)){
                                    sb.append("<OFICINAS>");
                                    sb.append(oficinas);
                                    sb.append("</OFICINAS>");
                                }                                
                                sb.append("</ERROR>");                                
                                contadorErrores++;
        
                           }else
                           if(oficinasRegistroHermanas!=null && oficinasRegistroHermanas.size()==0){                                
                                // SE ASIGNA PERMISO SOBRE UNA OFICINA DE REGISTRO PADRE CON HIJAS, PERO NO SE DA PERMISO SOBRE UNA HIJA => ERROR                        
                                sb.append("<ERROR>");
                                sb.append("<STATUS>");
                                sb.append("2");
                                sb.append("</STATUS>");
                                sb.append("<PADRE_COD_VISIBLE>");
                                sb.append(uor.getUor_cod_vis());
                                sb.append("</PADRE_COD_VISIBLE>");
                                sb.append("<PADRE_DESCRIPCION>");
                                sb.append(uor.getUor_nom());
                                sb.append("</PADRE_DESCRIPCION>");                                
                                
                                String oficinas = tratarListadoOficinas(hijas);                                
                                if(oficinas!=null && !"".equals(oficinas)){
                                    sb.append("<OFICINAS>");
                                    sb.append(oficinas);
                                    sb.append("</OFICINAS>");
                                }                                
                                sb.append("</ERROR>");
                                contadorErrores++;
                           }
                        }
                        salida = sb.toString();                               
                    }//if     
                    else
                    if(uor.isOficinaRegistro()){
                        // Sino es de tipo registro, entonces se comprueba si la uor es oficina de registro y, adem�s, se comprueba
                        // si se da permiso sobre la uor padre e, incluso, sobre otras oficinas de registro hermanas de la misma
                        String codUorPadre = uor.getUor_pad();
                        
                        log.debug("C�digo de la uor padre: " + codUorPadre);
                        if(!seAsignaPermisoSobreUorPadreRegistro(codUorPadre,unidades)){
                            // Se asigna permiso sobre una oficina de registro pero no sobre su padre => Error
                            UORDTO uorPadre = getInfoUor(UORs,codUorPadre);
                            sb.append("<ERROR>");
                            sb.append("<STATUS>");
                            sb.append("3");
                            sb.append("</STATUS>");
                            sb.append("<PADRE_COD_VISIBLE>");
                            sb.append(uor.getUor_cod_vis());
                            sb.append("</PADRE_COD_VISIBLE>");
                            sb.append("<PADRE_DESCRIPCION>");
                            sb.append(uor.getUor_nom());
                            sb.append("</PADRE_DESCRIPCION>");                                
                                                        
                            sb.append("<OFICINAS>");                               
                                sb.append("<OFICINA>");            
                                sb.append(uorPadre.getUor_cod_vis());
                                sb.append("#");            
                                sb.append(uorPadre.getUor_nom());                
                                sb.append("</OFICINA>");
                            sb.append("</OFICINAS>");
                            
                           sb.append("</ERROR>");
                            contadorErrores++;
                            
                        }
                        
                        
                    }
                }
            
            }// for
            
            
            salida = "<SALIDA_CUENTA_USUARIO>";
            if(contadorErrores>=1){
                salida = salida + "<CODIGO_ERROR>NO_OK</CODIGO_ERROR>" ;
                salida = salida + "<ERRORES>";
                salida = salida + sb.toString();
                salida = salida + "</ERRORES>";
            }else
                salida = salida + "<CODIGO_ERROR>0K</CODIGO_ERROR>" ;
            
            salida = salida + "</SALIDA_CUENTA_USUARIO>";
            
        }
        catch(TechnicalException e){
            log.error("Error al obtener una conexi�n a la BBDD: " + e.getMessage());
            throw new TechnicalException("002-Error al verificar si los permisos sobre unidades y oficinas de registro son correctos",e);
        }catch(Exception e){
            log.error("Error al obtener una conexi�n a la BBDD: " + e.getMessage());
            throw new TechnicalException("002-Error al verificar si los permisos sobre unidades y oficinas de registro son correctos",e);
        }
        finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }            
        }
        
        log.debug("Se devuelve " + salida);
        log.debug("verificarAsignacionPermisosOficinasRegistro <=====");
        return salida;
        
    }
    */
     
    
    
    private ArrayList<UORDTO> getOficinasRegistroCuelganDeUnidadRegistro(String codUorRegistro,ArrayList<UORDTO> oficinas,Vector uors) {
        ArrayList<UORDTO> salida = new ArrayList<UORDTO>();
        
        for(int i=0;i<oficinas.size();i++){
            UORDTO oficina = oficinas.get(i);
            
            if(oficina.isOficinaRegistro() && oficina.getUor_pad()!=null && oficina.getUor_pad().equals(codUorRegistro))
                // La oficina tiene como padre a codUor, entonces es su hija o descendiente
                salida.add(oficina);
            else{
                
                boolean salir = false;
                String codigo = oficina.getUor_pad();
                while(!salir){
                    // Se consiguen los datos del padre
                    UORDTO abuelo = getInfoUor(uors,codigo);
                    if(abuelo!=null){
                        if(abuelo.getUor_pad()!=null && !"".equalsIgnoreCase(abuelo.getUor_pad()) 
                                && abuelo.getUor_pad().equalsIgnoreCase(codUorRegistro)){

                            salida.add(oficina); // El padre del padre de la oficina es la unidad de registro
                            salir = true;
                        }else // Se analiza el padre de la oficina
                            codigo = abuelo.getUor_pad() ;
                    }else salir = true;                        
                }                
                
            }// else
        }// for
                
        return salida;
    }
     
    
    /**
     * Comprueba para una uor, si se le est� asignando permiso sobre la 
     * @param codUor: C�digo interno de la uor
     * @param unidadesPermiso: Vector con las unidades que se dan permiso al usurio
     * @param todasLasUors: Vector con todas la uors de la organizaci�n
     * @return String
     */
    private UorPermisoVO tieneUnidadAncestroTipoRegistroAsignado(String codUor,Vector unidadesPermiso,Vector todasLasUors){        
        UorPermisoVO permiso = new UorPermisoVO();
        
        
        //Se consigue la unidad de registro padre de la uor, si es que la tiene
        UORDTO uor = this.getInfoUor(todasLasUors,codUor);
        boolean salir = false;
        String codigo = uor.getUor_pad();
        String codUorTipoRegistro = null;
        String descUorTipoRegistro = null;
        while(!salir){
            // Se consiguen los datos del padre
            UORDTO abuelo = getInfoUor(todasLasUors,codigo);
            if(abuelo!=null){
                if(abuelo.getUor_tipo()!=null && abuelo.getUor_tipo().equals("1")){                
                    salir = true;
                    codUorTipoRegistro = abuelo.getUor_cod();
                    descUorTipoRegistro = abuelo.getUor_nom();
                    permiso.setCodigoUor(codUorTipoRegistro);
                    permiso.setDescripcionUor(descUorTipoRegistro);
                    permiso.setCodigoUorVisible(abuelo.getUor_cod_vis());
                    
                }else
                    codigo = abuelo.getUor_pad() ;
                
            }else{
                // La oficina de registro no tiene una unidad ancestro de tipo registro 
                permiso.setCodigoUor(null);
                permiso.setDescripcionUor(null);
                permiso.setCodigoUorVisible(null);
                permiso.setStatus(-2);                         
                salir = true;                
            }                        
        }                
        
        
        
        if(salir){
            if(codUorTipoRegistro!=null){
                // Se ha encontrado una unidad de registro ancestro de codUor => Se comprueba is est� entre la unidades 
                // sobre las que se da permiso al usuario            
                if(unidadesPermiso.contains(codUorTipoRegistro)) 
                    permiso.setStatus(0); // La unidad de tipo registro  existe              
                else // La oficina de registro tiene una unidad de tipo registro ancestro pero no se le ha asignado permiso al usuario sobre la misma 
                    permiso.setStatus(-1);
            }
        }
        
        return permiso;            
        
        
    }
     
    public String verificarAsignacionPermisosOficinasRegistro(Vector unidades,String[] params) throws TechnicalException{
        log.debug("verificarAsignacionPermisosOficinasRegistro =====>");
        
        String salida = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
                        
            // Se recupera todas las unidades organizativas
            Vector UORs = UORsDAO.getInstance().getListaUOROrdenPorDesc(con,params);
            
            // SE COMPRUEBA SI SE EST�N ASIGNANDO TODAS LAS UNIDADES AL USUARIO
            if(unidades!=null && unidades.size()==1 && unidades.get(0).equals("-1")){
                unidades = transformarColeccionUORS(UORs);
            }

            StringBuffer sb = new StringBuffer();
            int contadorErrores = 0;
            
            
            /******/
            ArrayList<UORDTO> lista = UORsDAO.getInstance().getListaOficinasRegistro(params[6]);                        
            /******/
            
            
            for(int i=0;unidades!=null && i<unidades.size();i++){
                String codUnidad = (String)unidades.get(i);
                
                // SE OBTIENE INFORMACI�N DE LA UNIDAD ORGANIZATIVA
                UORDTO uor = getInfoUor(UORs,codUnidad);
                
                if(uor!=null){                                                    
                                        
                    if(uor.getUor_tipo()!=null && "1".equals(uor.getUor_tipo())){
                        // Si la uor es de registro. Se comprueba si tiene hijas
                        
                        /*******/
                        ArrayList<UORDTO> hijas= this.getOficinasRegistroCuelganDeUnidadRegistro(uor.getUor_cod(),lista,UORs);
                        
                        /*******/
                        
                        
                        //ArrayList<UORDTO> hijas = UORsDAO.getInstance().getListaUORHijas(Integer.parseInt(uor.getUor_cod()),true,con);
                        
                        // Se comprueba si se est�n asignando permisos sobre las oficinas de registro hijas de la uor actual
                        ArrayList<UORDTO> oficinasRegistroHermanas = buscarOficinasRegistroHermanas(hijas,unidades);            
                        
                        if(hijas!=null && hijas.size()>=1){
                            // LA UNIDAD DE REGISTRO TIENE M�S DE UNA OFICINA DE REGISTRO COMO HIJA
                                                        
                            if(oficinasRegistroHermanas!=null && oficinasRegistroHermanas.size()>1){                                
                                // SE EST� ASIGNADA M�S DE UN PERMISO SOBRE UNA OFICINA DE REGISTRO DE UN MISMO PADRE => ERROR
                                
                                sb.append("<ERROR>");
                                sb.append("<STATUS>");
                                sb.append("1");
                                sb.append("</STATUS>");
                                sb.append("<PADRE_COD_VISIBLE>");
                                sb.append(uor.getUor_cod_vis());
                                sb.append("</PADRE_COD_VISIBLE>");
                                sb.append("<PADRE_DESCRIPCION>");
                                sb.append(uor.getUor_nom());
                                sb.append("</PADRE_DESCRIPCION>");
                                
                                String oficinas = tratarListadoOficinas(oficinasRegistroHermanas);                                
                                if(oficinas!=null && !"".equals(oficinas)){
                                    sb.append("<OFICINAS>");
                                    sb.append(oficinas);
                                    sb.append("</OFICINAS>");
                                }                                
                                sb.append("</ERROR>");                                
                                contadorErrores++;
        
                           }else
                           if(oficinasRegistroHermanas!=null && oficinasRegistroHermanas.size()==0){                                
                                // SE ASIGNA PERMISO SOBRE UNA OFICINA DE REGISTRO PADRE CON HIJAS, PERO NO SE DA PERMISO SOBRE UNA HIJA => ERROR                        
                                sb.append("<ERROR>");
                                sb.append("<STATUS>");
                                sb.append("2");
                                sb.append("</STATUS>");
                                sb.append("<PADRE_COD_VISIBLE>");
                                sb.append(uor.getUor_cod_vis());
                                sb.append("</PADRE_COD_VISIBLE>");
                                sb.append("<PADRE_DESCRIPCION>");
                                sb.append(uor.getUor_nom());
                                sb.append("</PADRE_DESCRIPCION>");                                
                                
                                String oficinas = tratarListadoOficinas(hijas);                                
                                if(oficinas!=null && !"".equals(oficinas)){
                                    sb.append("<OFICINAS>");
                                    sb.append(oficinas);
                                    sb.append("</OFICINAS>");
                                }                                
                                sb.append("</ERROR>");
                                contadorErrores++;
                           }
                        }
                        salida = sb.toString();                               
                    }//if     
                    else
                    if(uor.isOficinaRegistro()){
                        // Sino es de tipo registro, entonces se comprueba si la uor es oficina de registro y, adem�s, se comprueba
                        // si se da permiso sobre la uor padre e, incluso, sobre otras oficinas de registro hermanas de la misma
                        String codUorPadre = uor.getUor_pad();
                        
                        log.debug("C�digo de la uor padre: " + codUorPadre);
                        //if(!seAsignaPermisoSobreUorPadreRegistro(codUorPadre,unidades)){
                        
                        UorPermisoVO permiso = tieneUnidadAncestroTipoRegistroAsignado(uor.getUor_cod(),unidades,UORs);
                        if(permiso.getStatus()==-1){
                            // La oficina de registro pertenece a una unidad de tipo registro pero no se ha asignado permiso al usuario
                            // sobre la misma
                            
                            sb.append("<ERROR>");
                            sb.append("<STATUS>");
                            sb.append("3");
                            sb.append("</STATUS>");
                            sb.append("<PADRE_COD_VISIBLE>");
                            sb.append(uor.getUor_cod_vis());
                            sb.append("</PADRE_COD_VISIBLE>");
                            sb.append("<PADRE_DESCRIPCION>");
                            sb.append(uor.getUor_nom());
                            sb.append("</PADRE_DESCRIPCION>");                                
                                                        
                            sb.append("<OFICINAS>");                               
                                sb.append("<OFICINA>");            
                                sb.append(permiso.getCodigoUorVisible());
                                sb.append("#"); 
                                sb.append(permiso.getDescripcionUor());                
                                sb.append("</OFICINA>");
                            sb.append("</OFICINAS>");
                            
                           sb.append("</ERROR>");
                            contadorErrores++;
                        }else
                        if(permiso.getStatus()==-2){
                            sb.append("<ERROR>");
                            sb.append("<STATUS>");
                            sb.append("4");
                            sb.append("</STATUS>");
                            sb.append("<PADRE_COD_VISIBLE>");
                            sb.append(uor.getUor_cod_vis());
                            sb.append("</PADRE_COD_VISIBLE>");
                            sb.append("<PADRE_DESCRIPCION>");
                            sb.append(uor.getUor_nom());
                            sb.append("</PADRE_DESCRIPCION>");                                
                                    
                            /*
                            sb.append("<OFICINAS>");                               
                                sb.append("<OFICINA>");            
                                sb.append(permiso.getCodigoUorVisible());
                                sb.append("#");            
                                sb.append(permiso.getDescripcionUor());                
                                sb.append("</OFICINA>");
                            sb.append("</OFICINAS>");
                            * 
                            */
                            
                           sb.append("</ERROR>");
                           contadorErrores++;                            
                        }    
                                
                        
                    }
                }
            
            }// for
             
          
            if(contadorErrores>=1){               
               
                salida = sb.toString();
              
            }else
                salida = "" ;
            
       
            
        }/*catch(BDException e){
            log.error("Error al obtener una conexi�n a la BBDD: " + e.getMessage());
            throw new TechnicalException("001-Error al obtener conexi�n a la BBDD. No se puede verificar si los permisos sobre unidades y oficinas de registro son correctos",e);
        }*/
        catch(TechnicalException e){
            log.error("Error al obtener una conexi�n a la BBDD: " + e.getMessage());
            throw new TechnicalException("002-Error al verificar si los permisos sobre unidades y oficinas de registro son correctos",e);
        }catch(Exception e){
            log.error("Error al obtener una conexi�n a la BBDD: " + e.getMessage());
            throw new TechnicalException("002-Error al verificar si los permisos sobre unidades y oficinas de registro son correctos",e);
        }
        finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }            
        }
        
        log.debug("Se devuelve " + salida);
        log.debug("verificarAsignacionPermisosOficinasRegistro <=====");
        return salida;
        
    }
     
     
     
     
     
     
     /**
     * Comprueba si los permisos que tiene un determinado usuario sobre la unidad de registro y sobre la oficina de registro son los correctos
     * @param codUorOficinaRegistro: C�digo de la oficina de registro
     * @param codUorRegistro;: C�digo de la unidad de tipo registro
     * @param codUsuario: C�digo del usuario
     * @param codOrganizacion: C�digo de la organizaci�n
     * @param codEntidad: C�digo de la entidad
     * @param conexion: Conexi�n a la BBDD
     * @return int:
     *        0 --> El usuario tiene permiso sobre la unidad de registro codUorRegistro y sobre una �nica oficina de registro hija que es codUorOficinaRegistro
     *       -1 --> El usuario tiene permiso sobre m�s de una oficina de registro hija de la unidad de registro codUorRegistro.
     *       -2 --> El usuario no tiene permiso sobre ninguna oficina de registro hija de la unidad  de registro "codUorRegistro"
     *       -3 --> Error al obtener la conexi�n a la BBDD
     *       -4 --> Error t�cnico
     * 
     */
    public int comprobarPermisosUsuarioOficinaRegistro(int codUorOficinaRegistro,int codUorRegistro,int codUsuario,int codOrganizacion,int codEntidad,String[] params)
    {           
        int salida = -1;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();            
            salida = UORsDAO.getInstance().comprobarPermisosUsuarioOficinaRegistro(codUorOficinaRegistro, codUorRegistro, codUsuario, codOrganizacion, codEntidad, con,params[6]);
                        
        }catch(BDException e){            
            e.printStackTrace();            
            salida = -3;
        }catch(SQLException e){
            log.error("Error durante el lanzamiento de la consulta que comprueba los permisos que el usuario tiene sobre la oficina y unidad de registro " + e.getMessage());
            salida = -4;
        }
        finally{
            try{
                adapt.devolverConexion(con);
                
            }catch(BDException e){
                log.error("Error al cerrar la conexi�n a la BBDD: " + e.getMessage());
            }
        }
        
        return salida;
   }  
   
   /**
    * Comprueba si una unidad org�nica permite digitalizar documentos
    *  @param codUorRegistro: C�digo de la unidad de tipo registro
    *  @param params: parametros de conexi�n a base de datos
    */ 
   public boolean comprobarUorPermiteDigitalizacion(int codUnidadRegistro, String[] params){
        log.debug("comprobarUorPermiteDigitalizacion (  codUnidadRegistro = " + codUnidadRegistro + " ) : BEGIN");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        boolean permiteDigitalizacionUor = false;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            permiteDigitalizacionUor = UORsDAO.getInstance().comprobarUorPermiteDigitalizacion(codUnidadRegistro, con);
        }catch(BDException e){
            log.error("Se ha producido un error al recuperar si la oficina de registro permite digitalizaci�n ", e);
        }   catch (SQLException ex) {
                log.error("Se ha producido un error al recuperar si la oficina de registro permite digitalizaci�n ", ex);
            }finally{
            try{
                adapt.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("permite digitalizaci�n = " + permiteDigitalizacionUor);
        if(log.isDebugEnabled()) log.debug("comprobarUorPermiteDigitalizacion() : END");
        return permiteDigitalizacionUor;
    } 
     /************************ OFICINAS DE REGISTRO ***********************************/
}