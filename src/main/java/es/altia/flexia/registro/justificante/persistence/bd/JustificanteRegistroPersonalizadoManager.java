package es.altia.flexia.registro.justificante.persistence.bd;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.registro.justificante.vo.JustificanteRegistroPersonalizadoVO;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;	
import java.util.Map;
import org.apache.log4j.Logger;

public class JustificanteRegistroPersonalizadoManager {
    private static JustificanteRegistroPersonalizadoManager instance = null;
    private Logger log = Logger.getLogger(JustificanteRegistroPersonalizadoManager.class);

    private JustificanteRegistroPersonalizadoManager(){}

    public static JustificanteRegistroPersonalizadoManager getInstance(){
        if(instance==null)
            instance = new JustificanteRegistroPersonalizadoManager();

        return instance;
    }

    public boolean altaJustificante(JustificanteRegistroPersonalizadoVO justificante, String[] params) throws BDException,TechnicalException{
        AdaptadorSQL adapt = null;
        Connection con = null;
        boolean exito = false;
        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();
            adapt.inicioTransaccion(con);
            exito = JustificanteRegistroPersonalizadoDAO.getInstance().altaJustificante(justificante, con);
            if(exito==true)
                adapt.finTransaccion(con);

        }catch(BDException e){
            try{
                adapt.rollBack(con);
            }catch(Exception f){
                log.error(f.getMessage());
            }
            log.error("Error en operacion altaJustificante al recuperar conexion a la BBDD: " + e.getMessage());
            throw e;
        }catch(SQLException e){
            try{
                adapt.rollBack(con);
            }catch(Exception f){
                log.error(f.getMessage());
            }
            log.error("Error en operacion altaJustificante al ejecutar operación contra la BBDD: " + e.getMessage());
            throw new TechnicalException("No se ha podido recuperar una conexión a la BBDD",e);
        }catch(Exception e){
            try{
                adapt.rollBack(con);
            }catch(Exception f){
                log.error(f.getMessage());
            }
            log.error("Error técnico en operacion altaJustificante: " + e.getMessage());
            throw new TechnicalException("Error técnico al ejecutar la operación altaJustificante",e);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return exito;
    }


    public ArrayList<JustificanteRegistroPersonalizadoVO> getJustificantes(String[] params, Map<String, Boolean> tipoJustificantes) throws BDException,TechnicalException{
        AdaptadorSQL adapt = null;
        Connection con = null;
        ArrayList<JustificanteRegistroPersonalizadoVO> justificantes = null;
        
        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();

            justificantes = JustificanteRegistroPersonalizadoDAO.getInstance().getJustificantes(con, tipoJustificantes); 

        }catch(BDException e){
            log.error("Error en operacion getJustificantes al recuperar conexion a la BBDD: " + e.getMessage());
            throw e;
        }catch(SQLException e){
            log.error("Error en operacion getJustificantes al ejecutar operación contra la BBDD: " + e.getMessage());
            throw new TechnicalException("Error en operacion altaJustificante al ejecutar operación contra la BBDD: " + e.getMessage(),e);
        }catch(Exception e){
            log.error("Error técnico en operacion getJustificantes: " + e.getMessage());
            throw new TechnicalException("Error técnico en operacion getJustificantes: " + e.getMessage(),e);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return justificantes;
    }


    public boolean existeJustificante(String nombreJustificante, String[] params)  throws BDException,TechnicalException{
        AdaptadorSQL adapt = null;
        Connection con = null;
        boolean exito = false;
        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();

            exito = JustificanteRegistroPersonalizadoDAO.getInstance().existeJustificante(nombreJustificante, con);

        }catch(BDException e){
            log.error("Error en operacion existeJustificante al recuperar conexion a la BBDD: " + e.getMessage());
            throw e;
        }catch(SQLException e){
            log.error("Error en operacion existeJustificante al ejecutar operación contra la BBDD: " + e.getMessage());
            throw new TechnicalException("Error en operacion existeJustificante al ejecutar operación contra la BBDD: " + e.getMessage(),e);
        }catch(Exception e){
            log.error("Error técnico en operacion existeJustificante: " + e.getMessage());
            throw new TechnicalException("Error técnico en operacion existeJustificante: " + e.getMessage(),e);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return exito;
     }



    /**
     * Elimina un determinado justificante de BBDD
     * @param justificante: Objeto con los datos del justificante
     * @param rutaFichero: Ruta al justificante de registro en disco
     * @param params: Parámetros de conexion a la BBDD
     * @return int: Puede tomar los valores:
     *          0 -> OK
     *          1 --> No se ha podido eliminar el justificante de registro de la BBDD
     *          2 --> No se ha podido eliminar el justificante de registro del servidor
     *          3 --> No se ha podido obtener una conexión a la BBDD     
     *          4 --> Error técnico durante la ejecución de la operación
     * @throws BDException
     * @throws TechnicalException
     */
     public int  eliminarJustificante(JustificanteRegistroPersonalizadoVO justificante,String directorio, String[] params){
        AdaptadorSQL adapt = null;
        Connection con = null;
        int codigo = 0;
        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();
            adapt.inicioTransaccion(con);

            String rutaFichero = directorio + File.separator + justificante.getNombreJustificante() + ConstantesDatos.EXTENSION_JUSTIFICANTE_REGISTRO_JASPER;

            //JustificanteRegistroPersonalizadoVO just = JustificanteRegistroPersonalizadoDAO.getInstance().getJustificante(justificante.getNombreJustificante(), con);


            boolean eliminadoBD = JustificanteRegistroPersonalizadoDAO.getInstance().eliminarJustificante(justificante, con);
            if(eliminadoBD){
               // Si se ha eliminado la transacción de la BBDD => Se procede a eliminarlo el justificante de disco.
               // NOTA: Las imágenes que se hayan podido subir al servidor para un justificante no se eliminan

                boolean eliminadoDisco = false;
                try{
                    File f = new File(rutaFichero);
                    eliminadoDisco = f.delete();
                    
                }catch(Exception e){
                    log.error(this.getClass().getName() + "eliminarJustificante: Error al eliminar justificante de registro de disco: " + e.getMessage());
                }

                if(eliminadoDisco){
                    adapt.finTransaccion(con);
                    codigo = 0;
                }else{
                    adapt.rollBack(con);
                    codigo = 2;
                }

            }else{
                codigo = 1;
                adapt.rollBack(con);
            }

            
        }catch(BDException e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();
            }

            log.error("Error en operacion eliminarJustificante al recuperar conexion a la BBDD: " + e.getMessage());
            codigo = 3;
            
        }catch(SQLException e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();
            }
            log.error("Error en operacion eliminarJustificante al ejecutar operación contra la BBDD: " + e.getMessage());
            codigo = 4;
        }catch(Exception e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();
            }
            log.error("Error técnico en operacion eliminarJustificante: " + e.getMessage());
            codigo = 4;
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return codigo;
     }



      /**
      * Operación encargada de marca un justificante de registro personalizado como activo o por defecto
      * @param nombreJustificante: Nombre del justificante
      * @param params: Parámetros de conexión a la BBDD
      * @return int que puede tomar los siguientes valores:
      *     0 --> OK
      *     1 --> No se ha podido marcar el justificante como activado
      *     2 --> Error al obtener una conexión con la BBDD
      *     3 --> Error técnico durante la ejecución de la operación
      *     4 --> Ya existe un justificante marcado como activo o por defecto
      */
     public int activarJustificante(JustificanteRegistroPersonalizadoVO justificante, String[] params){
        AdaptadorSQL adapt = null;
        Connection con = null;
        int codigo = 0;
        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();
            adapt.inicioTransaccion(con);


            String nombre = JustificanteRegistroPersonalizadoDAO.getInstance().existeJustificanteActivo(justificante.getTipoJustificante(),con);
            if(nombre==null){

                boolean activado = JustificanteRegistroPersonalizadoDAO.getInstance().activarJustificante(justificante,1,con);
                if(activado){
                    adapt.finTransaccion(con);
                    codigo = 0;
                }else{
                    adapt.rollBack(con);
                    codigo = 1;
                }
            }else{
                //EXISTE YA UN JUSTIFICANTE ACTIVO, SE INFORMA DE QUE ES NECESARIO DESACTIVARLO PREVIAMENTE
                adapt.finTransaccion(con);
                codigo = 4;
            }

        }catch(BDException e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();
            }

            log.error("Error en operacion activarJustificante al recuperar conexion a la BBDD: " + e.getMessage());
            codigo = 2;

        }catch(SQLException e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();
            }
            log.error("Error en operacion activarJustificante al ejecutar operación contra la BBDD: " + e.getMessage());
            codigo = 3;
        }catch(Exception e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();
            }
            log.error("Error técnico en operacion activarJustificante: " + e.getMessage());
            codigo = 3;
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return codigo;
    }



    public JustificanteRegistroPersonalizadoVO getJustificanteActivo(String tipoInforme, String[] params) throws BDException,TechnicalException{
        AdaptadorSQL adapt = null;
        Connection con = null;
        JustificanteRegistroPersonalizadoVO justificante = null;

        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();

            justificante = JustificanteRegistroPersonalizadoDAO.getInstance().getJustificanteActivo(tipoInforme,con);

        }catch(BDException e){
            log.error("Error en operacion getJustificanteActivo al recuperar conexion a la BBDD: " + e.getMessage());
            throw e;
        }catch(SQLException e){
            log.error("Error en operacion getJustificantes al ejecutar operación contra la BBDD: " + e.getMessage());
            throw new TechnicalException("Error en operacion getJustificanteActivo al ejecutar operación contra la BBDD: " + e.getMessage(),e);
        }catch(Exception e){
            log.error("Error técnico en operacion getJustificantes: " + e.getMessage());
            throw new TechnicalException("Error técnico en operacion getJustificanteActivo: " + e.getMessage(),e);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return justificante;
    }



    /**
      * Operación encargada de marcar un justificante de registro personalizado como desactivado
      * @param nombreJustificante: Nombre del justificante
      * @param params: Parámetros de conexión a la BBDD
      * @return int que puede tomar los siguientes valores:
      *     0 --> OK
      *     1 --> No se ha podido marcar el justificante como desactivado
      *     2 --> Error al obtener una conexión con la BBDD
      *     3 --> Error técnico durante la ejecución de la operación
      */
     public int desactivarJustificante(JustificanteRegistroPersonalizadoVO justif, String[] params){
        AdaptadorSQL adapt = null;
        Connection con = null;
        int codigo = 0;
        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();
            adapt.inicioTransaccion(con);

            boolean activado = JustificanteRegistroPersonalizadoDAO.getInstance().activarJustificante(justif,0,con);
            if(activado){
                adapt.finTransaccion(con);
                codigo = 0;
            }else{
                adapt.rollBack(con);
                codigo = 1;
            }

        }catch(BDException e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();
            }

            log.error("Error en operacion desactivarJustificante al recuperar conexion a la BBDD: " + e.getMessage());
            codigo = 2;

        }catch(SQLException e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();
            }
            log.error("Error en operacion desactivarJustificante al ejecutar operación contra la BBDD: " + e.getMessage());
            codigo = 3;
        }catch(Exception e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();
            }
            log.error("Error técnico en operacion desactivarJustificante: " + e.getMessage());
            codigo = 3;
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return codigo;
    }

    public JustificanteRegistroPersonalizadoVO getJustificanteByName(String nombreJustificante, String[] params) throws BDException,TechnicalException{
        AdaptadorSQL adapt = null;
        Connection con = null;
        JustificanteRegistroPersonalizadoVO justificante = null;

        try{
            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();

            justificante = JustificanteRegistroPersonalizadoDAO.getInstance().getJustificante(nombreJustificante,con);

        }catch(BDException e){
            log.error("Error en operacion getJustificanteByName al recuperar conexion a la BBDD: " + e.getMessage());
            throw e;
        }catch(SQLException e){
            log.error("Error en operacion getJustificanteByName al ejecutar operación contra la BBDD: " + e.getMessage());
            throw new TechnicalException("Error en operacion getJustificanteByName al ejecutar operación contra la BBDD: " + e.getMessage(),e);
        }catch(Exception e){
            log.error("Error técnico en operacion getJustificanteByName: " + e.getMessage());
            throw new TechnicalException("Error técnico en operacion getJustificanteByName: " + e.getMessage(),e);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return justificante;
    }


}