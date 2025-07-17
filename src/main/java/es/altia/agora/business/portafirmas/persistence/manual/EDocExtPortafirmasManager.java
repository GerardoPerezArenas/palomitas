package es.altia.agora.business.portafirmas.persistence.manual;

import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Clase fachada que permite trabajar contra la tabla E_DOC_EXT_PORTAFIRMAS
 * @author oscar.rodriguez
 */
public class EDocExtPortafirmasManager {

    private static EDocExtPortafirmasManager instance = null;


    private EDocExtPortafirmasManager(){}

    public static EDocExtPortafirmasManager getInstance(){
        if(instance==null)
            instance = new EDocExtPortafirmasManager();

        return instance;
    }

    /**
     * Recupera los documentos externos al sistema enviados al portafirmas
     *  @param usuarioDelegado: Tabla hash con el nif y c�digo del usuario en el que se ha delegado la firma de un documento por parte del usuario que tiene
     * como NIF el pasado en el par�metro nifUsuario. Si esta a null quiere decir que no se ha delegado la firma en ning�n usuario.
     * @param estado: Estado del documento. Si est� a null se recuperan todos los documentos independientemente del estado
     * en el que se encuentren.
     * @param nifUsuario: Nif del usuario para el que se recuperan los documentos     
     * @param params: Par�metros de conexi�n a la base de datos
     * @return ArrayList<DocumentoPortafirmasVO>
     */
    public ArrayList<DocumentoOtroFirmaVO> getDocumentoExternosPortafirmas(String nifUsuarioDelegado,String nifUsuarioQueDelega,String estado,String[] params){
        Connection con = null;
        ArrayList<DocumentoOtroFirmaVO> documentos = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            documentos = EDocExtPortafirmasDAO.getInstance().getDocumentoExternosPortafirmas(nifUsuarioDelegado,nifUsuarioQueDelega,estado, con);
            
        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return documentos;
    }



    /**
     * Recupera el usuario en el que haya podido delegar un determinado usuario la firma de los documentos del portafrimas
     * @param codigoUsuario: C�digo del usuario que concede la delegaci�n de firma
     * @param params: Par�metros de conexi�n a la base d edatos
     * @return Hashtable<String,String>
     */
    public Hashtable<String,String> getUsuarioDelegado(int codigoUsuario,String[] params){
        Hashtable<String,String> salida = null;
        Connection con = null;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            salida = EDocExtPortafirmasDAO.getInstance().getUsuarioDelegado(codigoUsuario, con);
            
        }catch(BDException e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


    /**
     * Recupera el contenido de un determinado documento
     * @param codigoDocumento: C�digo del documento a recuperar
     * @param params: Par�metros de conexi�n a la base de datos
     */
    public DocumentoOtroFirmaVO getDocumento(String codigoDocumento,String[] params){
        Connection con = null;
        DocumentoOtroFirmaVO doc = null;
        try{
                AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                doc = EDocExtPortafirmasDAO.getInstance().getDocumento(codigoDocumento, con);
        }catch(BDException e){
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
     * Almacena la firma de un documento externo en base de datos.
     * @param doc: DocumentoOtroFirmaVO con los datos del objeto para el que se guarda la firma.
     * Al mismo tiempo se actualiza su estado a firmado y guarda el usuario para
     * @param params: Par�metros de conexi�n a la base de datos
     * @return Boolean
     */
    public boolean guardarFirma(DocumentoOtroFirmaVO doc,String[] params){
        Connection con = null;
        boolean exito = false;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = EDocExtPortafirmasDAO.getInstance().guardarFirma(doc, con);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return exito;
    }


     /**
      * Permite marcar como rechazado un documento externo enviado al portafirmas
      * @param doc: Objeto con el contenido necesario para almacenar la firma de documento externo y para cambiar el estado del documento a firmado
      * @param con:  Conexi�n a la base de datos
      * @return true si todo bien y false en caso contrario
      */
      public boolean rechazarDocumento(DocumentoOtroFirmaVO doc,String[] params){
        Connection con = null;
        boolean exito = false;
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            exito = EDocExtPortafirmasDAO.getInstance().rechazarDocumento(doc, con);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null && !con.isClosed()) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return exito;
      }


      /**
      * Recupera informaci�n del documento como el nombre, extensi�n, tipo mime y el nif del usuario al que est� dirigido el documento
      * @param codigoDocumento: C�digo del documento
      * @param con:  Conexi�n a la base de datos
      * @return DocumentoFirmaVO con la info indicada del documento
      */
     public DocumentoOtroFirmaVO getInfoDocumento(String codigoDocumento,String[] params){
        Connection con = null;
        DocumentoOtroFirmaVO doc = new DocumentoOtroFirmaVO();
        try{
            AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            doc = EDocExtPortafirmasDAO.getInstance().getInfoDocumento(codigoDocumento, con);
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null && !con.isClosed()) con.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return doc;
     }

}