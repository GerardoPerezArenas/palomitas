package es.altia.flexia.business.documentospresentados.persistence;

import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author oscar.rodriguez
 */
public class ExpedienteDocPresentadoManager {
    private static ExpedienteDocPresentadoManager instance;
    private ExpedienteDocPresentadoManager(){}

    public static ExpedienteDocPresentadoManager getInstance(){
        if(instance==null)
            instance = new ExpedienteDocPresentadoManager();

        return instance;
    }

   
    /**
     * Guarda un binario corresondiente a un documento de expediente presentado en base de datos
     * @param doc: Datos del documento
     * @return True si todo bien y false en caso contrario
     */
    public boolean setDocumentoPresentado(Documento doc){        
        Connection con = null;
        boolean exito = false;
        String[] params =  doc.getParams();

        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = ExpedienteDocPresentadoDAO.getInstance().setDocumentoPresentado(doc,con);
           
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }


    /**
     * Recupera un documento que ya ha sido presentado, incluido su contenido
     * @param doc: Datos necesario para recuperar el documento
     * @param con: Conexión a la base de datos
     * @return Documento
     */
     public Documento getDocumentoPresentado(Documento doc){
        Connection con = null;
        String[] params =  doc.getParams();

        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            doc = ExpedienteDocPresentadoDAO.getInstance().getDocumentoPresentado(doc,con);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return doc;
    }


      public Documento getContenidoDocumentoPresentado(Documento doc){
        Connection con = null;
        String[] params =  doc.getParams();

        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            doc = ExpedienteDocPresentadoDAO.getInstance().getContenidoDocumentoPresentado(doc,con);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return doc;
    }


     /**
      * Elimina un documento ya presentado de la base de datos
      * @param doc: Documento con los datos del documento de expediente a eliminar
      * @param con: Conexión a la base de datos
      * @return True si todo bien y false en caso contrario
      */
     public boolean eliminarDocumentoPresentado(Documento doc){
        boolean exito = false;
        Connection con = null;
        String[] params =  doc.getParams();

        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = ExpedienteDocPresentadoDAO.getInstance().eliminarDocumentoPresentado(doc, con);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }


     /**
      * Comprueba si un documento de expediente está marcado como presentado para un determinado expediente.
      * Si no lo está se marca el expediente como presentado.
      * @param doc: Objeto que contiene la información necesaria del documento
      * @return True si todo bien y false en caso contrario.
      */
   public boolean comprobarDocumentoPresentado(Documento doc){
        boolean exito = false;
        Connection con = null;
        String[] params =  doc.getParams();

        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = ExpedienteDocPresentadoDAO.getInstance().comprobarDocumentoPresentado(doc, con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
   }


    /**
     * Modifica el contenido de un adjunto asociado a un documento de expediente en base de datos
     * @param doc: Datos del documento
     * @return True si todo bien y false en caso contrario
     */
    public boolean modificarDocumentoPresentado(Documento doc){
        Connection con = null;
        boolean exito = false;
        String[] params =  doc.getParams();

        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = ExpedienteDocPresentadoDAO.getInstance().modificarDocumentoPresentado(doc,con);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }
    
    
    /**
     * Recupera el nombre y extensión de un documento de inicio de expediente
     * @param doc: Objeto que implementa la interfaz Documento
     * @param expedienteHistorico: True si el expediente está en el histórico y false en caso contrario
     * @return Objeto que implementa la interfaz Documento
     */
    public Documento getNombreDocumentoPresentado(Documento doc){
        Connection con = null;
        String[] params =  doc.getParams();

        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            doc = ExpedienteDocPresentadoDAO.getInstance().getNombreDocumentoPresentado(doc,con);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return doc;
    }
    
}