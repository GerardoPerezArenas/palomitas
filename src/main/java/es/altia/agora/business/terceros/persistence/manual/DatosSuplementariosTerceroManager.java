package es.altia.agora.business.terceros.persistence.manual;

import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;

public class DatosSuplementariosTerceroManager {

    private static DatosSuplementariosTerceroManager instance = null;
    private Logger log = Logger.getLogger(DatosSuplementariosTerceroManager.class);

    private DatosSuplementariosTerceroManager(){}

    public static DatosSuplementariosTerceroManager getInstance(){
        if(instance==null)
            instance = new DatosSuplementariosTerceroManager();
        
        return instance;
    }


    /**
     * Recupera la estructura de los campos suplementarios para una organización y los valores que toman para un determinado tercero
     * @param codMunicipio: Código del municipio/organización
     * @param codTercero: Código del tercero
     * @param params: Parámetros de conexión a la BD
     * @return Hashtable<String,Vector>
     * @throws es.altia.util.conexion.BDException
     * @throws es.altia.common.exception.TechnicalException
     */
    public Hashtable<String,Vector> cargarEstructuraValoresDatosSuplementarios(String codMunicipio,String codTercero,String[] params) throws BDException,TechnicalException{

        Connection con = null;
        Hashtable<String,Vector> salida = new Hashtable<String, Vector>();

        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            Vector<EstructuraCampo> estructura = DatosSuplementariosTerceroDAO.getInstance().cargaEstructuraDatosSuplementariosTercero(codMunicipio, adapt, con);
            Vector valores = DatosSuplementariosTerceroDAO.getInstance().cargaValoresDatosSuplementarios(codMunicipio,codTercero,estructura, con, params);

            salida.put("ESTRUCTURA_CAMPOS_TERCERO",estructura);
            salida.put("VALORES_CAMPOS_TERCERO",valores);

            log.debug("Se ha recuperado una estructura de " + estructura.size() + " elementos");
            log.debug("Se ha recupera un número de valores de " + valores.size() + " elementos");

        }catch(BDException e){
            log.debug("Error al recuperar una conexión a la BBDD");
            throw e;
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                log.error("Error al cerrar la conexión a la BBDD");
            }
        }
        return salida;
    }


    public Vector<EstructuraCampo> cargaEstructuraDatosSuplementariosTercero(String codMunicipio,String[] params) throws BDException,TechnicalException {
         Vector<EstructuraCampo> estructura = null;
         Connection con = null;

         try{
             AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();

             log.debug("DatosSuplementariosTerceroManager.cargaEstructuraDatosSuplementarios =============>");
             estructura = DatosSuplementariosTerceroDAO.getInstance().cargaEstructuraDatosSuplementariosTercero(codMunicipio, adapt, con);
             log.debug("DatosSuplementariosTerceroManager.cargaEstructuraDatosSuplementarios <=============");


         }catch(BDException e){
             e.printStackTrace();
             log.error("Error al recuperar conexión a la base de datos en " + this.getClass().getName());            
         }finally{
             try{
                 if(con!=null) con.close();

             }catch(SQLException e){
                 log.error("Error al cerrar la conexión a la base de datos en " + this.getClass().getName());
             }
         }
         return estructura;
     }



    public Vector cargaValoresDatosSuplementarios(String codOrganizacion,String codTercero,Vector eCs, String[] params) throws TechnicalException {
         Vector valores = null;
         Connection con = null;

         try{
             AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();

             valores = DatosSuplementariosTerceroDAO.getInstance().cargaValoresDatosSuplementarios(codOrganizacion,codTercero,eCs,con,params);

         }catch(BDException e){
             e.printStackTrace();
             log.error("Error al recuperar conexión a la base de datos en " + this.getClass().getName());
         }finally{
             try{
                 if(con!=null) con.close();
             }catch(SQLException e){
                 log.error("Error al cerrar la conexión a la base de datos en " + this.getClass().getName());
             }
         }
         return valores;
     }
    
    
    public Vector cargaValoresDatosSuplementariosConCodigo(String codOrganizacion,String codTercero,Vector eCs, String[] params) throws TechnicalException {
         Vector valores = null;
         Connection con = null;

         try{
             AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
             con = adapt.getConnection();

             valores = DatosSuplementariosTerceroDAO.getInstance().cargaValoresDatosSuplementariosConCodigo(codOrganizacion,codTercero,eCs,con,params);

         }catch(BDException e){
             e.printStackTrace();
             log.error("Error al recuperar conexión a la base de datos en " + this.getClass().getName());
         }finally{
             try{
                 if(con!=null) con.close();
             }catch(SQLException e){
                 log.error("Error al cerrar la conexión a la base de datos en " + this.getClass().getName());
             }
         }
         return valores;
     }



     public Hashtable<String,Object> getContenidoCampoFichero(String codMunicipio,String codCampo,String codTercero,String[] params){
         Connection con = null;
         AdaptadorSQLBD bd = null;
         Hashtable<String,Object> datos = null;
         try{
             bd  = new AdaptadorSQLBD(params);
             con = bd.getConnection();

             datos = DatosSuplementariosTerceroDAO.getInstance().getContenidoCampoFichero(codMunicipio,codCampo,codTercero,con);

         }catch(Exception e){
            e.printStackTrace();
         }finally{
             try{
                 if(con!=null) con.close();
             }catch(Exception e){
                e.printStackTrace();
             }
         }

         return datos;
     }



     public void grabarDatosSuplementarios(String codOrganizacion,String codTercero,Vector estructuraDatosSuplementarios,Vector valoresDatosSuplementarios,String[] params) throws TechnicalException{
         Connection con = null;
         AdaptadorSQLBD bd = null;
         Hashtable<String,Object> datos = null;
         try{
             bd  = new AdaptadorSQLBD(params);
             con = bd.getConnection();

             DatosSuplementariosTerceroDAO.getInstance().grabarDatosSuplementarios(codOrganizacion,codTercero,estructuraDatosSuplementarios,valoresDatosSuplementarios,bd,con);

         }catch(BDException e){
             e.printStackTrace();
             log.error(e.getMessage());
             throw new TechnicalException("No se ha podido recuperar una conexión a la BD",e);
         }
         catch(TechnicalException e){
             e.printStackTrace();
             log.error(e.getMessage());
             throw e;
         }finally{
             try{
                 if(con!=null) con.close();
             }catch(Exception e){
                e.printStackTrace();
             }
         }

     }
     
     public String getValorDesplegable (String codMunicipio, String codCampo, String codValor, String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorDesplegable( codMunicipio = " + codMunicipio + " codCampo = " + codCampo 
                + " codValor = " + codValor + " ) : BEGIN"); 
        Connection con = null;
        AdaptadorSQLBD bd = null;
        String valor = "";
        try{
            bd  = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            DatosSuplementariosTerceroDAO datosSuplementariosTercerosDao = DatosSuplementariosTerceroDAO.getInstance();
            valor = datosSuplementariosTercerosDao.getValorDesplegable(codMunicipio, codCampo, codValor, con);
        }catch(Exception e){
            log.error("Se ha producido un error recuperando el valor de un campo desplegable " + e.getMessage());
            throw e;
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorDesplegable() : END");
        return valor;
     }//getValorDesplegable
     
    public String getValorDesplegableTabla (String codCampo, String codTercero, String codMunicipio,String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorDesplegableTabla( codMunicipio = " + codMunicipio + " codCampo = " + codCampo 
            + " codTercero = " + codTercero + " ) : BEGIN"); 
        Connection con = null;
        AdaptadorSQLBD bd = null;
        String valor = "";
        try{
            bd  = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            DatosSuplementariosTerceroDAO datosSuplementariosTercerosDao = DatosSuplementariosTerceroDAO.getInstance();
            valor = datosSuplementariosTercerosDao.getValorDesplegableTabla(codCampo, codTercero, codMunicipio, con);
        }catch(Exception e){
            log.error("Se ha producido un error recuperando el valor de un campo desplegable " + e.getMessage());
            throw e;
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorDesplegableTabla() : END");
        return valor;
    }//getValorDesplegableTabla
    
    public String getValorTexto (String codCampo, String codTercero, String codMunicipio, String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorTexto( codMunicipio = " + codMunicipio + " codCampo = " + codCampo 
            + " codTercero = " + codTercero + " ) : BEGIN"); 
        Connection con = null;
        AdaptadorSQLBD bd = null;
        String valor = "";
        try{
            bd  = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            DatosSuplementariosTerceroDAO datosSuplementariosTercerosDao = DatosSuplementariosTerceroDAO.getInstance();
            valor = datosSuplementariosTercerosDao.getValorTexto(codCampo, codTercero, codMunicipio, con);
        }catch(Exception e){
            log.error("Se ha producido un error recuperando el valor de un campo de texto " + e.getMessage());
            throw e;
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorTexto() : END");
        return valor;
    }//getValorTexto
    
    public String getValorTextoLargo (String codCampo, String codTercero, String codMunicipio, String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorTextoLargo( codMunicipio = " + codMunicipio + " codCampo = " + codCampo 
            + " codTercero = " + codTercero + " ) : BEGIN"); 
        Connection con = null;
        AdaptadorSQLBD bd = null;
        String valor = "";
        try{
            bd  = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            DatosSuplementariosTerceroDAO datosSuplementariosTercerosDao = DatosSuplementariosTerceroDAO.getInstance();
            valor = datosSuplementariosTercerosDao.getValorTextoLargo(codCampo, codTercero, codMunicipio, con);
        }catch(Exception e){
            log.error("Se ha producido un error recuperando el valor de un campo de texto largo " + e.getMessage());
            throw e;
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorTextoLargo() : END");
        return valor;
    }//getValorTextoLargo
    
    public String getValorFecha (String codCampo, String codTercero, String codMunicipio, String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorFecha( codMunicipio = " + codMunicipio + " codCampo = " + codCampo 
            + " codTercero = " + codTercero + " ) : BEGIN"); 
        Connection con = null;
        AdaptadorSQLBD bd = null;
        String valor = "";
        try{
            bd  = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            DatosSuplementariosTerceroDAO datosSuplementariosTercerosDao = DatosSuplementariosTerceroDAO.getInstance();
            valor = datosSuplementariosTercerosDao.getValorFecha(codCampo, codTercero, codMunicipio, con);
        }catch(Exception e){
            log.error("Se ha producido un error recuperando el valor de un campo de texto largo " + e.getMessage());
            throw e;
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorFecha() : END");
        return valor;
    }//getValorFecha
    
    public String getValorNumerico (String codCampo, String codTercero, String codMunicipio, String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorNumerico( codMunicipio = " + codMunicipio + " codCampo = " + codCampo 
            + " codTercero = " + codTercero + " ) : BEGIN"); 
        Connection con = null;
        AdaptadorSQLBD bd = null;
        String valor = "";
        try{
            bd  = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            DatosSuplementariosTerceroDAO datosSuplementariosTercerosDao = DatosSuplementariosTerceroDAO.getInstance();
            valor = datosSuplementariosTercerosDao.getValorNumerico(codCampo, codTercero, codMunicipio, con);
        }catch(Exception e){
            log.error("Se ha producido un error recuperando el valor de un campo de texto largo " + e.getMessage());
            throw e;
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorNumerico() : END");
        return valor;
    }//getValorNumerico
             
    public Vector getValoresSuplementariosTerceroFromVector (Vector listaCampos, String codMunicipio, String[] params) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValoresSuplementariosTerceroFromVector() :  BEGIN");
        Connection con = null;
        AdaptadorSQLBD bd = null;
        Vector valoresSuplementariosTercero = new Vector();
        try{
            bd  = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            DatosSuplementariosTerceroDAO datosSuplementariosTercerosDao = DatosSuplementariosTerceroDAO.getInstance();
            valoresSuplementariosTercero = datosSuplementariosTercerosDao.getValoresSuplementariosTerceroFromVector(listaCampos, codMunicipio, con, bd);
        }catch(Exception e){
            log.error("Se ha producido un error recuperando el valor de un campo de texto largo " + e.getMessage());
            throw e;
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValoresSuplementariosTerceroFromVector() :  END");
        return valoresSuplementariosTercero;
    }//getValoresSuplementariosTerceroFromVector
    
    public String getValorCampoSuplementario(String codCampoSuplementario, String codMunicipio, String codTercero ,String[] params) 
            throws Exception{
        if(log.isDebugEnabled()) log.debug("getValorCampoSuplementario() :  BEGIN");
        Connection con = null;
        AdaptadorSQLBD bd = null;
        String valor = "";
        try{
            bd  = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            DatosSuplementariosTerceroDAO datosSuplementariosTercerosDao = DatosSuplementariosTerceroDAO.getInstance();
            valor = datosSuplementariosTercerosDao.getValorCampoSuplementario(codCampoSuplementario, codMunicipio, codTercero, con, bd);
        }catch(Exception e){
            log.error("Se ha producido un error recuperando el valor de un campo suplementario " + e.getMessage());
            throw e;
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorCampoSuplementario() :  END");
        return valor;
    }//getValorCampoSuplementario
}