package es.altia.flexia.notificacion.persistence;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.persistence.manual.DefinicionTramitesDAO;
import es.altia.agora.business.sge.persistence.manual.DocumentosExpedienteDAO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.ConstantesDatos;
import java.sql.Connection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;

import java.sql.*;
import java.util.ArrayList;


import es.altia.flexia.notificacion.vo.*;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.util.commons.DateOperations;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.ResourceBundle;


public class AdjuntoNotificacionDAO {


    private static AdjuntoNotificacionDAO instance =	null;
    protected   static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log = LogFactory.getLog(AdjuntoNotificacionDAO.class.getName());
    private final String ESTADO_FIRMA_PENDIENTE = "O";
    private final String ESTADO_FIRMA_FIRMADO   = "F";
    private final String ESTADO_FIRMA_RECHAZADO = "R";
            
            
    protected AdjuntoNotificacionDAO() {
                m_CommonProperties = ConfigServiceHelper.getConfig("common");
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");


	}

    public static AdjuntoNotificacionDAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (AdjuntoNotificacionDAO.class) {
            if (instance == null) {
                instance = new AdjuntoNotificacionDAO();
            }

        }
        return instance;
    }

//Recupera los documentos adjuntos asociados a una determinada notificación de la tabla ADJUNTO_NOTIFICACION
  public ArrayList<AdjuntoNotificacionVO> getAdjuntos(int codigoNotificacion,String[] params) throws TechnicalException {

        AdaptadorSQLBD obd = null;
        Connection con = null;

        ResultSet rs = null;
        Statement st = null;
        ArrayList<AdjuntoNotificacionVO> arrayRetorno=new ArrayList<AdjuntoNotificacionVO>();

        try{

            obd = new AdaptadorSQLBD(params);
            con = obd.getConnection();

            String sql="SELECT CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FIL,CRD_DES FROM E_CRD,ADJUNTO_NOTIFICACION " +
                    " WHERE CODIGO_NOTIFICACION="+codigoNotificacion+ " AND"+
                    "COD_MUNICIPIO=CRD_MUN AND COD_PROCEDIMIENTO=CRD_PRO AND EJERCICIO=CRD_EJE AND NUM_EXPEDIENTE=CRD_NUM " +
                    "AND COD_TRAMITE=CRD_TRA AND OCU_TRAMITE=CRD_OCU AND NUM_UNIDAD_DOC=CRD_NUD";

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            int i=0;
            while(rs.next()){

            AdjuntoNotificacionVO adjuntoNotificacionVO = new AdjuntoNotificacionVO();
            adjuntoNotificacionVO.setCodigoMunicipio(rs.getInt("CRD_MUN"));
            adjuntoNotificacionVO.setEjercicio(rs.getInt("CRD_EJE"));
            adjuntoNotificacionVO.setNumeroExpediente(rs.getString("CRD_NUM"));
            adjuntoNotificacionVO.setCodigoProcedimiento(rs.getString("CRD_PRO"));
            adjuntoNotificacionVO.setCodigoTramite(rs.getInt("CRD_TRA"));
            adjuntoNotificacionVO.setOcurrenciaTramite(rs.getInt("CRD_OCU"));
            adjuntoNotificacionVO.setNumeroUnidad(rs.getInt("CRD_NUD"));
            adjuntoNotificacionVO.setContenido(rs.getBytes("CRD_FIL"));
            adjuntoNotificacionVO.setNombre(rs.getString("CRD_DES"));


            arrayRetorno.add(i, adjuntoNotificacionVO);
            i++;
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                obd.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return arrayRetorno;
    }

   //Elimina un determinado adjunto de una notificación de la tabla ADJUNTO_NOTIFICACION
  public boolean eliminarAdjunto(AdjuntoNotificacionVO adjuntoNotificacionVO,String[] params) throws TechnicalException {

      AdaptadorSQLBD obd = null;
      Connection con = null;
      Statement st = null;
      String sql = "";

      try{

      obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();
      obd.inicioTransaccion(con);
      st = con.createStatement();

      int codNotificacion = adjuntoNotificacionVO.getCodigoNotificacion();
      int codigoMunicipio=adjuntoNotificacionVO.getCodigoMunicipio();
      int ejercicio=adjuntoNotificacionVO.getEjercicio();
      String numeroExpediente =adjuntoNotificacionVO.getNumeroExpediente();
      String codProcedimiento=adjuntoNotificacionVO.getCodigoProcedimiento();
      int codTramite=adjuntoNotificacionVO.getCodigoTramite();
      int ocuTramite=adjuntoNotificacionVO.getOcurrenciaTramite();
      int numUnidadDoc=adjuntoNotificacionVO.getNumeroUnidad();



      sql =	"DELETE FROM ADJUNTO_NOTIFICACION " +
          " WHERE CODIGO_NOTIFICACION = " + codNotificacion + " AND COD_MUNICIPIO="+codigoMunicipio+" AND EJERCICIO = " + ejercicio + " AND NUM_EXPEDIENTE = '" + numeroExpediente + "' AND " +
          " COD_PROCEDIMIENTO = '" + codProcedimiento + "' AND COD_TRAMITE= " + codTramite+" AND OCU_TRAMITE="+ocuTramite+" AND NUM_UNIDAD_DOC="+numUnidadDoc;
      if(m_Log.isDebugEnabled()) m_Log.debug(sql);

      st.executeUpdate(sql);
      obd.finTransaccion(con);



      }catch (Exception e){
      try{
            obd.rollBack(con);
      }catch(Exception ex){
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());

      }
      e.printStackTrace();
      if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
      return false;

    }finally{
      try{
            if (st!=null) st.close();
            obd.devolverConexion(con);
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            return false;
        }
    }

    return true;
  }




   //Elimina un determinado adjunto de una notificación de la tabla ADJUNTO_NOTIFICACION
  public boolean insertarAdjunto(AdjuntoNotificacionVO adjuntoNotificacionVO,Connection con) throws TechnicalException {
      
      Statement st = null;      
      String sql = "";

      try{

        st = con.createStatement();

        int codigoMunicipio=adjuntoNotificacionVO.getCodigoMunicipio();
        int ejercicio=adjuntoNotificacionVO.getEjercicio();
        String numeroExpediente =adjuntoNotificacionVO.getNumeroExpediente();
        String codProcedimiento=adjuntoNotificacionVO.getCodigoProcedimiento();
        int codTramite=adjuntoNotificacionVO.getCodigoTramite();
        int ocuTramite=adjuntoNotificacionVO.getOcurrenciaTramite();
        int numUnidadDoc=adjuntoNotificacionVO.getNumeroUnidad();
        int codigo=adjuntoNotificacionVO.getCodigoNotificacion();

        sql ="INSERT INTO ADJUNTO_NOTIFICACION (CODIGO_NOTIFICACION,COD_MUNICIPIO,EJERCICIO,NUM_EXPEDIENTE," +
            "COD_PROCEDIMIENTO,COD_TRAMITE,OCU_TRAMITE,NUM_UNIDAD_DOC) VALUES(" +codigo+","+codigoMunicipio+","+ejercicio+
            ",'"+numeroExpediente+"','"+codProcedimiento+"',"+codTramite+","+ocuTramite+","+numUnidadDoc+
            ")";
        
        if(m_Log.isDebugEnabled()) m_Log.debug(sql);
        int rowsInserted = st.executeUpdate(sql);
        m_Log.debug("Nº de filas insertadas: " + rowsInserted);

      }catch (Exception e){
      
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        return false;

    }finally{
        try{
            if (st!=null) st.close();            
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            return false;
        }
    }

    return true;
  }

   //Comprueba si para un determinado trámite de un expediente en concreto, existen documentos de tramitación que estén firmados. Se consulta sobre la tabla E_CRD.
  public boolean existeDocumentosFirmados(String numExpediente,int codTramite, int ocurrenciaTramite,String[] params) throws TechnicalException {

       AdaptadorSQLBD obd = null;
       Connection con = null;
       boolean retorno=false;
       ResultSet rs = null;
       Statement st = null;

       try{

            obd = new AdaptadorSQLBD(params);
            con = obd.getConnection();

            String sql = "SELECT CRD_NUM,CRD_TRA,CRD_OCU FROM E_CRD WHERE CRD_NUM='"+numExpediente+"' AND CRD_TRA="+codTramite+" AND CRD_OCU="+ocurrenciaTramite+" AND CRD_FIR_EST='F'";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);


            if(rs.next()){

            retorno=true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                obd.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return retorno;
  }
  
  
  
  public boolean existenDocumentosPendientesFirma(String numExpediente,int codTramite, int ocurrenciaTramite,String[] params) throws TechnicalException {

       AdaptadorSQLBD obd = null;
       Connection con = null;
       boolean retorno=false;
       ResultSet rs = null;
       Statement st = null;

       try{

            obd = new AdaptadorSQLBD(params);
            con = obd.getConnection();

            String sql = "SELECT CRD_NUM,CRD_TRA,CRD_OCU FROM E_CRD WHERE CRD_NUM='"+numExpediente+"' AND CRD_TRA="+codTramite+" AND CRD_OCU="+ocurrenciaTramite+" AND (CRD_FIR_EST='T'  or   CRD_FIR_EST='O'  or   CRD_FIR_EST='E' or   CRD_FIR_EST='L' or   CRD_FIR_EST='U')";
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);


            if(rs.next()){

            retorno=true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                obd.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return retorno;
  }
  
  
  /**
   * Recupera el contenido de un documento de tramitación a través del plugin de almacenamiento/recuperación de documentos
   * @param adjunto: Objeto de la clase AdjuntoNotificacionVO con los datos del documento a recuperar
   * @param con: Conexión a la BBDD
   * @return byte[] o null sino se ha podido recuperar
   */
  public byte[] getContenidoDocumentoTramitacionPlugin(AdjuntoNotificacionVO adjunto,Connection con) throws TechnicalException{
      byte[] contenido = null;
      
      try{
            String codMunicipio = Integer.toString(adjunto.getCodigoMunicipio());
                        
            Hashtable<String,Object> datos = new Hashtable<String,Object>();
            datos.put("codMunicipio",codMunicipio);
            datos.put("numeroExpediente",adjunto.getNumeroExpediente());
            datos.put("codTramite",Integer.toString(adjunto.getCodigoTramite()));
            datos.put("ocurrenciaTramite",Integer.toString(adjunto.getOcurrenciaTramite()));
            datos.put("numeroDocumento",Integer.toString(adjunto.getNumeroUnidad()));
            datos.put("perteneceRelacion","false");
            
            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,adjunto.getCodigoProcedimiento());
            Documento doc = null;
            int tipoDocumento = -1;
            if(!almacen.isPluginGestor())
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
            else{
                   
                ResourceBundle config = ResourceBundle.getBundle("common");
                String editorPlantillas = config.getString("editorPlantillas");
                if(editorPlantillas!=null && "OOFFICE".equalsIgnoreCase(editorPlantillas)){                       
                    datos.put("extension",ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE);                        
                }else{                     
                    datos.put("extension",ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD);
                }
    
                String codProcedimiento = adjunto.getCodigoProcedimiento();
                String ejercicio = Integer.toString(adjunto.getEjercicio());
                String tipoMime = "";

                // Se obtiene el nombre del documento a mostrar porque se necesita para el caso de que se venga de firmar el documento
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codMunicipio",Integer.toString(adjunto.getCodigoMunicipio()));
                gVO.setAtributo("codProcedimiento",codProcedimiento);
                gVO.setAtributo("ejercicio",ejercicio);
                gVO.setAtributo("codTramite",Integer.toString(adjunto.getCodigoTramite()));
                gVO.setAtributo("ocurrenciaTramite",Integer.toString(adjunto.getOcurrenciaTramite()));
                gVO.setAtributo("numeroExpediente",adjunto.getNumeroExpediente());
                gVO.setAtributo("numeroDocumento",Integer.toString(adjunto.getNumeroUnidad()));                
                datos.put("nombreDocumento", DocumentosExpedienteDAO.getInstance().getNombreDocumentoGestor(gVO,con));

                String codigoVisibleTramite = DefinicionTramitesDAO.getInstance().getCodigoVisibleTramite(Integer.toString(adjunto.getCodigoMunicipio()),codProcedimiento, Integer.toString(adjunto.getOcurrenciaTramite()), con);
                String nombreProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento, con);
                String descripcionOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(adjunto.getCodigoMunicipio(), con);
                datos.put("codProcedimiento",codProcedimiento);                    
                datos.put("codigoVisibleTramite",codigoVisibleTramite);

                if (editorPlantillas.equalsIgnoreCase("OOFFICE"))
                      tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
                else
                    tipoMime = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;

                datos.put("tipoMime",tipoMime);
                datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);

                /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                ResourceBundle bundle = ResourceBundle.getBundle("documentos");          
                String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + adjunto.getCodigoMunicipio() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                
                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(adjunto.getCodigoMunicipio() + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                listaCarpetas.add(((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));

                datos.put("listaCarpetas",listaCarpetas);                    
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
              }

                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos,tipoDocumento);
                contenido = almacen.getDocumento(doc,con);

      }catch(Exception e){
          e.printStackTrace();
          m_Log.error("Error al recuperar el contenido de un documento de tramitación a través del plugin: " + e.getMessage());
          throw new TechnicalException("Error al recuperar el contenido de un documento de tramitación a través del plugin: " + e.getMessage());
      }
      
      return contenido;
  }
  
  
  

   //Recupera todos los documentos de tramitación asociados a un trámite. Para cada documento comprueba además si este documento ya está asociado a una notificación determinada
    public ArrayList<AdjuntoNotificacionVO> getDocumentosTramitacion(AdjuntoNotificacionVO adjuntoNotificacionVO,Connection con) throws TechnicalException {

        ResultSet rs = null;
        Statement st = null;
        ArrayList<AdjuntoNotificacionVO> arrayAux=new ArrayList<AdjuntoNotificacionVO>();
        ArrayList<AdjuntoNotificacionVO> arrayRetorno=new ArrayList<AdjuntoNotificacionVO>();

        try{

            int codigoMunicipio=adjuntoNotificacionVO.getCodigoMunicipio();
            int ejercicio=adjuntoNotificacionVO.getEjercicio();
            String numeroExpediente =adjuntoNotificacionVO.getNumeroExpediente();
            String codProcedimiento=adjuntoNotificacionVO.getCodigoProcedimiento();
            int codTramite=adjuntoNotificacionVO.getCodigoTramite();
            int ocuTramite=adjuntoNotificacionVO.getOcurrenciaTramite();
            int codigoNotificacion=adjuntoNotificacionVO.getCodigoNotificacion();
          
            String sql = "SELECT E_CRD.CRD_MUN,E_CRD.CRD_PRO,E_CRD.CRD_EJE,E_CRD.CRD_NUM,E_CRD.CRD_TRA," +
                    "E_CRD.CRD_OCU,E_CRD.CRD_NUD,E_CRD.CRD_DES,E_CRD.CRD_FIR_EST, E_CRD_FIR.FIR " +
                    " FROM E_CRD "+
                    "LEFT join E_CRD_FIR  on (E_CRD.CRD_MUN=E_CRD_FIR.CRD_MUN AND E_CRD.CRD_PRO=E_CRD_FIR.CRD_PRO " +
                    "AND E_CRD.CRD_EJE=E_CRD_FIR.CRD_EJE AND E_CRD.CRD_NUM=E_CRD_FIR.CRD_NUM "+
                    "AND E_CRD.CRD_TRA=E_CRD_FIR.CRD_TRA AND E_CRD.CRD_OCU=E_CRD_FIR.CRD_OCU " +
                    "AND E_CRD.CRD_NUD=E_CRD_FIR.CRD_NUD"+
                    ") ";
             
             sql=sql+" WHERE E_CRD.CRD_MUN="+codigoMunicipio+" AND E_CRD.CRD_EJE="+ejercicio+" AND E_CRD.CRD_NUM='"+numeroExpediente+"' AND "+
                     " E_CRD.CRD_PRO='"+codProcedimiento+"' AND E_CRD.CRD_TRA="+codTramite+" AND E_CRD.CRD_OCU="+ocuTramite;

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            int i=0;
            while(rs.next()){
                AdjuntoNotificacionVO adjuntoNotifVORetorno=new AdjuntoNotificacionVO();
                adjuntoNotifVORetorno.setCodigoMunicipio(rs.getInt("CRD_MUN"));
                adjuntoNotifVORetorno.setCodigoProcedimiento(rs.getString("CRD_PRO"));
                adjuntoNotifVORetorno.setEjercicio(rs.getInt("CRD_EJE"));
                adjuntoNotifVORetorno.setNumeroExpediente(rs.getString("CRD_NUM"));
                adjuntoNotifVORetorno.setCodigoTramite(rs.getInt("CRD_TRA"));
                adjuntoNotifVORetorno.setOcurrenciaTramite(rs.getInt("CRD_OCU"));
                adjuntoNotifVORetorno.setNumeroUnidad(rs.getInt("CRD_NUD"));            
                // Se recupera el contenido del documento de tramitación a través del plugin
            
                adjuntoNotifVORetorno.setNombre(rs.getString("CRD_DES"));
                adjuntoNotifVORetorno.setEstadoFirma(rs.getString("CRD_FIR_EST"));
                if("F".equals(rs.getString("CRD_FIR_EST"))){
                    
                    adjuntoNotifVORetorno.setFirmado("SI");
                    byte[] contenido = null;
                    // Se lee el contenido binario del documento
                    java.io.InputStream stream = rs.getBinaryStream("FIR");
                    java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                    int c;
                    if (stream != null) {
                         while ((c = stream.read()) != -1) {
                             ot.write(c);
                         }
                    }
                    ot.flush();
                    contenido = ot.toByteArray();
                    ot.close();

                    String value = new String(contenido);
                    adjuntoNotifVORetorno.setFirma(value);
                    if(m_Log.isDebugEnabled()) m_Log.debug("Firma "+value);

                }else{
                    adjuntoNotifVORetorno.setFirmado("NO");
                    adjuntoNotifVORetorno.setFirma(null);
                }
            
                //adjuntoNotifVORetorno.setContenido(this.getContenidoDocumentoTramitacionPlugin(adjuntoNotifVORetorno,con));
                arrayAux.add(i, adjuntoNotifVORetorno);
                i++;
             }// while
            
             rs.close();
             
             
             for(int j=0;j<arrayAux.size();j++)
            {
               AdjuntoNotificacionVO adjuntoNotifVORetorno=new AdjuntoNotificacionVO();
               adjuntoNotifVORetorno=arrayAux.get(j);
               if(estaDocumentoAsociadoNotificacion(adjuntoNotifVORetorno,codigoNotificacion,false,con))
               {
                   adjuntoNotifVORetorno.setSeleccionado("SI");
                   adjuntoNotifVORetorno.setCodigoNotificacion(codigoNotificacion);
               }
               else
               {
                   adjuntoNotifVORetorno.setSeleccionado("NO");
                   adjuntoNotifVORetorno.setCodigoNotificacion(-1);
               }

               arrayRetorno.add(adjuntoNotifVORetorno);
            }

        }catch(Exception e){
            e.printStackTrace();
            throw new TechnicalException("Error técnico al recuperar los documentos de tramitación: " + e.getMessage());
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return arrayRetorno;
    }
    
    
    
    /**
     * Recupera la lista de documentos de tramitación asociados a una determinada notificación
     * @param adjuntoNotificacionVO: Objeto de la clase AdjuntoNotificacionVO con los datos del expediente, trámite y 
     * 
     * @param expedienteHistorico
     * @param con
     * @return
     * @throws TechnicalException 
     */
    public ArrayList<AdjuntoNotificacionVO> getDocumentosTramitacion(AdjuntoNotificacionVO adjuntoNotificacionVO,boolean expedienteHistorico,Connection con) throws TechnicalException {

        ResultSet rs = null;
        Statement st = null;
        ArrayList<AdjuntoNotificacionVO> arrayAux=new ArrayList<AdjuntoNotificacionVO>();
        ArrayList<AdjuntoNotificacionVO> arrayRetorno=new ArrayList<AdjuntoNotificacionVO>();

        try{

            int codigoMunicipio=adjuntoNotificacionVO.getCodigoMunicipio();
            int ejercicio=adjuntoNotificacionVO.getEjercicio();
            String numeroExpediente =adjuntoNotificacionVO.getNumeroExpediente();
            String codProcedimiento=adjuntoNotificacionVO.getCodigoProcedimiento();
            int codTramite=adjuntoNotificacionVO.getCodigoTramite();
            int ocuTramite=adjuntoNotificacionVO.getOcurrenciaTramite();
            int codigoNotificacion=adjuntoNotificacionVO.getCodigoNotificacion();
          
            String sql ="";
            
            if(!expedienteHistorico) {
                sql = "SELECT E_CRD.CRD_MUN,E_CRD.CRD_PRO,E_CRD.CRD_EJE,E_CRD.CRD_NUM,E_CRD.CRD_TRA," +
                    "E_CRD.CRD_OCU,E_CRD.CRD_NUD,E_CRD.CRD_DES,E_CRD.CRD_FIR_EST, E_CRD_FIR.FIR " +
                    " FROM E_CRD "+
                    "LEFT join E_CRD_FIR  on (E_CRD.CRD_MUN=E_CRD_FIR.CRD_MUN AND E_CRD.CRD_PRO=E_CRD_FIR.CRD_PRO " +
                    "AND E_CRD.CRD_EJE=E_CRD_FIR.CRD_EJE AND E_CRD.CRD_NUM=E_CRD_FIR.CRD_NUM "+
                    "AND E_CRD.CRD_TRA=E_CRD_FIR.CRD_TRA AND E_CRD.CRD_OCU=E_CRD_FIR.CRD_OCU " +
                    "AND E_CRD.CRD_NUD=E_CRD_FIR.CRD_NUD"+
                    ") ";
            
                 sql=sql+" WHERE E_CRD.CRD_MUN="+codigoMunicipio+" AND E_CRD.CRD_EJE="+ejercicio+" AND E_CRD.CRD_NUM='"+numeroExpediente+"' AND "+
                     " E_CRD.CRD_PRO='"+codProcedimiento+"' AND E_CRD.CRD_TRA="+codTramite+" AND E_CRD.CRD_OCU="+ocuTramite;
            }else {
                
                sql = "SELECT HIST_E_CRD.CRD_MUN,HIST_E_CRD.CRD_PRO,HIST_E_CRD.CRD_EJE,HIST_E_CRD.CRD_NUM,HIST_E_CRD.CRD_TRA," +
                    "HIST_E_CRD.CRD_OCU,HIST_E_CRD.CRD_NUD,HIST_E_CRD.CRD_DES,HIST_E_CRD.CRD_FIR_EST, HIST_E_CRD_FIR.FIR " +
                    " FROM HIST_E_CRD "+
                    "LEFT join HIST_E_CRD_FIR on (HIST_E_CRD.CRD_MUN=HIST_E_CRD_FIR.CRD_MUN AND HIST_E_CRD.CRD_PRO=HIST_E_CRD_FIR.CRD_PRO " +
                    "AND HIST_E_CRD.CRD_EJE=HIST_E_CRD_FIR.CRD_EJE AND HIST_E_CRD.CRD_NUM=HIST_E_CRD_FIR.CRD_NUM "+
                    "AND HIST_E_CRD.CRD_TRA=HIST_E_CRD_FIR.CRD_TRA AND HIST_E_CRD.CRD_OCU=HIST_E_CRD_FIR.CRD_OCU " +
                    "AND HIST_E_CRD.CRD_NUD=HIST_E_CRD_FIR.CRD_NUD"+
                    ") ";
            
                 sql=sql+" WHERE HIST_E_CRD.CRD_MUN="+codigoMunicipio+" AND HIST_E_CRD.CRD_EJE="+ejercicio+" AND HIST_E_CRD.CRD_NUM='"+numeroExpediente+"' AND "+
                     " HIST_E_CRD.CRD_PRO='"+codProcedimiento+"' AND HIST_E_CRD.CRD_TRA="+codTramite+" AND HIST_E_CRD.CRD_OCU="+ocuTramite;
                
            }
                 

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);

            int i=0;
            while(rs.next()){
                AdjuntoNotificacionVO adjuntoNotifVORetorno=new AdjuntoNotificacionVO();
                adjuntoNotifVORetorno.setCodigoMunicipio(rs.getInt("CRD_MUN"));
                adjuntoNotifVORetorno.setCodigoProcedimiento(rs.getString("CRD_PRO"));
                adjuntoNotifVORetorno.setEjercicio(rs.getInt("CRD_EJE"));
                adjuntoNotifVORetorno.setNumeroExpediente(rs.getString("CRD_NUM"));
                adjuntoNotifVORetorno.setCodigoTramite(rs.getInt("CRD_TRA"));
                adjuntoNotifVORetorno.setOcurrenciaTramite(rs.getInt("CRD_OCU"));
                adjuntoNotifVORetorno.setNumeroUnidad(rs.getInt("CRD_NUD"));            
                // Se recupera el contenido del documento de tramitación a través del plugin
            
                adjuntoNotifVORetorno.setNombre(rs.getString("CRD_DES"));
                adjuntoNotifVORetorno.setEstadoFirma(rs.getString("CRD_FIR_EST"));
                if("F".equals(rs.getString("CRD_FIR_EST"))){
                    
                    adjuntoNotifVORetorno.setFirmado("SI");
                    byte[] contenido = null;
                    // Se lee el contenido binario del documento
                    java.io.InputStream stream = rs.getBinaryStream("FIR");
                    java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                    int c;
                    if (stream != null) {
                         while ((c = stream.read()) != -1) {
                             ot.write(c);
                         }
                    }
                    ot.flush();
                    contenido = ot.toByteArray();
                    ot.close();

                    String value = new String(contenido);
                    adjuntoNotifVORetorno.setFirma(value);
                    if(m_Log.isDebugEnabled()) m_Log.debug("Firma "+value);

                }else{
                    adjuntoNotifVORetorno.setFirmado("NO");
                    adjuntoNotifVORetorno.setFirma(null);
                }
                arrayAux.add(i, adjuntoNotifVORetorno);
                i++;
             }// while
            
             rs.close();             
             
             for(int j=0;j<arrayAux.size();j++){
                 
               AdjuntoNotificacionVO adjuntoNotifVORetorno=new AdjuntoNotificacionVO();
               adjuntoNotifVORetorno=arrayAux.get(j);
               if(estaDocumentoAsociadoNotificacion(adjuntoNotifVORetorno,codigoNotificacion,expedienteHistorico,con)){
                   adjuntoNotifVORetorno.setSeleccionado("SI");
                   adjuntoNotifVORetorno.setCodigoNotificacion(codigoNotificacion);
               }
               else{
                   adjuntoNotifVORetorno.setSeleccionado("NO");
                   adjuntoNotifVORetorno.setCodigoNotificacion(-1);
               }

               arrayRetorno.add(adjuntoNotifVORetorno);
            }

        }catch(Exception e){
            e.printStackTrace();
            throw new TechnicalException("Error técnico al recuperar los documentos de tramitación: " + e.getMessage());
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return arrayRetorno;
    }
    
    

   /**
     * Comprueba si un documento de tramitación de un determinado trámite de un expediente está asociado a una determinada notificación.
     * @param adjuntoNotificacionVO: Objeto de la clase AdjuntoNotificacionVO con los datos del expediente, trámite y notificación para la que se hace la comprobación
     * @param codNotificacion: Código de la notificación
     * @param expedienteHistorico: True si el expediente está en el histórico y false en caso contrario
     * @param con: Conexión a la BBDD
     * @return boolean: True o false
     */ 
    private boolean estaDocumentoAsociadoNotificacion(AdjuntoNotificacionVO adjuntoNotificacionVO,int codNotificacion,boolean expedienteHistorico,Connection con) throws TechnicalException {

       boolean retorno=false;
       ResultSet rs = null;
       PreparedStatement ps = null;

       try{
            int codigoMunicipio=adjuntoNotificacionVO.getCodigoMunicipio();
            int ejercicio=adjuntoNotificacionVO.getEjercicio();
            String numeroExpediente =adjuntoNotificacionVO.getNumeroExpediente();
            String codProcedimiento=adjuntoNotificacionVO.getCodigoProcedimiento();
            int codTramite=adjuntoNotificacionVO.getCodigoTramite();
            int ocuTramite=adjuntoNotificacionVO.getOcurrenciaTramite();
            int numUnidadDoc=adjuntoNotificacionVO.getNumeroUnidad();

            String sql = "";            
            if(!expedienteHistorico) {
                sql = "SELECT CODIGO_NOTIFICACION FROM ADJUNTO_NOTIFICACION "+
                    " WHERE CODIGO_NOTIFICACION=? AND COD_MUNICIPIO=? AND EJERCICIO=? AND NUM_EXPEDIENTE=? AND " +
                    " COD_PROCEDIMIENTO=? AND COD_TRAMITE=? AND OCU_TRAMITE=? AND NUM_UNIDAD_DOC=?";
            } else {
                sql = "SELECT CODIGO_NOTIFICACION FROM HIST_ADJUNTO_NOTIFICACION "+
                    " WHERE CODIGO_NOTIFICACION=? AND COD_MUNICIPIO=? AND EJERCICIO=? AND NUM_EXPEDIENTE=? AND " +
                    " COD_PROCEDIMIENTO=? AND COD_TRAMITE=? AND OCU_TRAMITE=? AND NUM_UNIDAD_DOC=?";
            }
            
            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            
            int i =1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codNotificacion);
            ps.setInt(i++,codigoMunicipio);
            ps.setInt(i++,ejercicio);
            ps.setString(i++,numeroExpediente);
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocuTramite);
            ps.setInt(i++,numUnidadDoc);
            
            rs = ps.executeQuery();

            if(rs.next()){
                retorno=true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return retorno;
  }



  /** 
   * Da de alta un adjunto externo asociada a una determinada notificación
   * @param adjuntoNotificacionVO: Objeto de tiop AdjuntoNotificacionVO con el contenido binario del fichero
   * @param tipoGestor: boolean
   * @param con: Conexión a la BBDD
   */ 
    public boolean insertarAdjuntoExterno(AdjuntoNotificacionVO adjuntoNotificacionVO,String TIPO_GESTOR,Connection con) throws TechnicalException {
      
      PreparedStatement ps = null;      
      String sql = "";
      boolean exito = false;
      try{
            
            int codigoMunicipio=adjuntoNotificacionVO.getCodigoMunicipio();            
            String numeroExpediente =adjuntoNotificacionVO.getNumeroExpediente();            
            int codTramite=adjuntoNotificacionVO.getCodigoTramite();
            int ocuTramite=adjuntoNotificacionVO.getOcurrenciaTramite();            
            int codigo=adjuntoNotificacionVO.getCodigoNotificacion();
            String nombre = adjuntoNotificacionVO.getNombre();
            String tipoMime = adjuntoNotificacionVO.getContentType();
                    
            sql ="INSERT INTO ADJUNTO_EXT_NOTIFICACION(";
            if(TIPO_GESTOR.equalsIgnoreCase("ORACLE"))
                sql = sql + "ID,";

            sql = sql + "COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,FECHA,CONTENIDO,ID_NOTIFICACION,NOMBRE,TIPO_MIME,ESTADO_FIRMA) ";

            if(TIPO_GESTOR.equalsIgnoreCase("ORACLE")) sql = sql +  " VALUES(SEQ_FILE_EXT_NOTIFICACION.NextVal,?,?,?,?,?,?,?,?,?,?)";
            else sql = sql +  " VALUES(?,?,?,?,?,?,?,?,?,?)";
            m_Log.debug(sql);
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codigoMunicipio);
            ps.setString(i++,numeroExpediente);
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocuTramite);                        
            ps.setTimestamp(i++,DateOperations.toTimestamp(Calendar.getInstance()));            
            InputStream st = new ByteArrayInputStream(adjuntoNotificacionVO.getContenido());
            ps.setBinaryStream(i++,st,adjuntoNotificacionVO.getContenido().length);
            ps.setInt(i++, codigo);
            ps.setString(i++,nombre);
            ps.setString(i++,tipoMime);
            ps.setString(i++,ESTADO_FIRMA_PENDIENTE);
            int rowsInserted =ps.executeUpdate();            
            m_Log.debug(" Filas insertadas:  " + rowsInserted);
            if(rowsInserted==1) exito = true;

      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        exito = false;
        
      }finally{
          
        try{
          if (ps!=null) ps.close();
                
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            return false;
        }
      }

    return exito;
  }
 
  
    

    
  /**
   * Recupera la lista de adjuntos externo
   * @param codNotificacion: Código de la notificación para la que se recuperan los adjuntos externos
   * @param con: Conexión a la BBDD
   * @return Booleano
   * @throws TechnicalException 
   */  
  public ArrayList<AdjuntoNotificacionVO> getListaAdjuntosExterno(int codNotificacion,Connection con) throws TechnicalException {
      
      ArrayList<AdjuntoNotificacionVO> lista = new ArrayList<AdjuntoNotificacionVO>();
      ResultSet rs = null;
      Statement st = null;      
      String sql = "";
        
      try{                    
          
            sql = "SELECT ID,COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,FECHA,NOMBRE,TIPO_MIME,CONTENIDO,FIRMA,COD_USUARIO_FIRMA,ESTADO_FIRMA,TIPO_CERTIFICADO_FIRMA " +
                  "FROM ADJUNTO_EXT_NOTIFICACION " + 
                  "WHERE ID_NOTIFICACION=" + codNotificacion + 
                  " ORDER BY FECHA DESC";
            m_Log.debug(sql);                        
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){     
                                
                AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();
                adjunto.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                adjunto.setCodigoNotificacion(codNotificacion);
                adjunto.setCodigoTramite(rs.getInt("COD_TRAMITE"));
                adjunto.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                adjunto.setFechaAlta(DateOperations.toCalendar(rs.getTimestamp("FECHA")));
                adjunto.setNombre(rs.getString("NOMBRE"));
                adjunto.setContentType(rs.getString("TIPO_MIME"));                
                adjunto.setFirma(rs.getString("FIRMA"));
                adjunto.setCodUsuarioFirmaOtro(rs.getInt("COD_USUARIO_FIRMA"));
                String estado = rs.getString("ESTADO_FIRMA");
                adjunto.setEstadoFirma("");                
                if(estado!=null && !"".equals(estado) && !"null".equals(estado))
                    adjunto.setEstadoFirma(estado);
                
                adjunto.setIdDocExterno(rs.getInt("ID"));
                adjunto.setTipoCertificadoFirma(rs.getInt("TIPO_CERTIFICADO_FIRMA"));
                adjunto.setSeleccionado("SI");
                lista.add(adjunto);
            }            

      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
      }finally{
          
        try{
          if (st!=null) st.close();
          if (rs!=null) rs.close();                
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }

      return lista;
  }
   
  
   /**
   * Recupera la lista de adjuntos externo
   * @param codNotificacion: Código de la notificación para la que se recuperan los adjuntos externos
   * @param expedienteHistorico: True si el expediente está en el histórico y false en caso contrario
   * @param con: Conexión a la BBDD
   * @return Booleano
   * @throws TechnicalException 
   */  
  public ArrayList<AdjuntoNotificacionVO> getListaAdjuntosExterno(int codNotificacion,boolean expedienteHistorico,Connection con) throws TechnicalException {
      
      ArrayList<AdjuntoNotificacionVO> lista = new ArrayList<AdjuntoNotificacionVO>();
      ResultSet rs = null;
      PreparedStatement ps = null;      
      String sql = "";
        
      try{                    
            if(!expedienteHistorico) {
                sql = "SELECT ID,COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,FECHA,NOMBRE,TIPO_MIME,CONTENIDO,FIRMA,COD_USUARIO_FIRMA,ESTADO_FIRMA,TIPO_CERTIFICADO_FIRMA " +
                      "FROM ADJUNTO_EXT_NOTIFICACION " + 
                      "WHERE ID_NOTIFICACION=? " + 
                      "ORDER BY FECHA DESC";
            } else {
                sql = "SELECT ID,COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,FECHA,NOMBRE,TIPO_MIME,CONTENIDO,FIRMA,COD_USUARIO_FIRMA,ESTADO_FIRMA,TIPO_CERTIFICADO_FIRMA " +
                      "FROM HIST_ADJUNTO_EXT_NOTIFICACION " + 
                      "WHERE ID_NOTIFICACION=? " + 
                      "ORDER BY FECHA DESC";
                
            }
            m_Log.debug(sql);                        
            ps = con.prepareStatement(sql);
            ps.setInt(1,codNotificacion);            
            rs = ps.executeQuery();
            
            while(rs.next()){                                     
                AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();
                adjunto.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                adjunto.setCodigoNotificacion(codNotificacion);
                adjunto.setCodigoTramite(rs.getInt("COD_TRAMITE"));
                adjunto.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                adjunto.setFechaAlta(DateOperations.toCalendar(rs.getTimestamp("FECHA")));
                adjunto.setNombre(rs.getString("NOMBRE"));
                adjunto.setContentType(rs.getString("TIPO_MIME"));                
                adjunto.setFirma(rs.getString("FIRMA"));
                adjunto.setCodUsuarioFirmaOtro(rs.getInt("COD_USUARIO_FIRMA"));
                String estado = rs.getString("ESTADO_FIRMA");
                adjunto.setEstadoFirma("");                
                if(estado!=null && !"".equals(estado) && !"null".equals(estado))
                    adjunto.setEstadoFirma(estado);
                
                adjunto.setIdDocExterno(rs.getInt("ID"));
                adjunto.setTipoCertificadoFirma(rs.getInt("TIPO_CERTIFICADO_FIRMA"));
                lista.add(adjunto);
            }            

      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
      }finally{
          
        try{
          if (ps!=null) ps.close();
          if (rs!=null) rs.close();                
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }

      return lista;
  }
  
   
  /**
   * Se actualizan los documentos de tramitación asociados a una notificación
   * @param codigo: Código de la notificación
   * @param numeroExpediente: Número del expediente
   * @param codMunicipio: Código del municipio
   * @param codTramite: Código del trámite
   * @param ocuTramite: Ocurrencia del trámite
   * @param adjuntos: ArrayListz<AdjuntoNotificacionVO>
   * @param con: Conexión a la BBDD
   * @return Un boolean
   * @throws TechnicalException 
   */  
  public boolean actualizarAdjunto(int codigo,String numeroExpediente,int codMunicipio,int codTramite,int ocuTramite,ArrayList<AdjuntoNotificacionVO> adjuntos,Connection con) throws TechnicalException {
      
      PreparedStatement ps = null;      
      String sql = "";
      boolean exito = false;
      
      try{
            sql = "DELETE FROM ADJUNTO_NOTIFICACION WHERE CODIGO_NOTIFICACION=? AND NUM_EXPEDIENTE=? AND COD_MUNICIPIO=? AND " + 
                " COD_TRAMITE=? AND OCU_TRAMITE=? ";        
            m_Log.debug(sql);

            int i= 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codigo);
            ps.setString(i++,numeroExpediente);
            ps.setInt(i++,codMunicipio);
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocuTramite);

            int rowsDeleted = ps.executeUpdate();
            m_Log.debug("Nº de filas eliminadas: " + rowsDeleted);
            ps.close();


            
            for(int j=0;adjuntos!=null && j<adjuntos.size();j++){
                
                AdjuntoNotificacionVO adjunto = adjuntos.get(j);                
                if(adjunto.getSeleccionado().equals("SI")){
                    

                    sql ="INSERT INTO ADJUNTO_NOTIFICACION (CODIGO_NOTIFICACION,COD_MUNICIPIO,EJERCICIO,NUM_EXPEDIENTE," +
                        "COD_PROCEDIMIENTO,COD_TRAMITE,OCU_TRAMITE,NUM_UNIDAD_DOC) VALUES(" +codigo+","+adjunto.getCodigoMunicipio()+","+ adjunto.getEjercicio() +
                        ",'"+ numeroExpediente+"','"+ adjunto.getCodigoProcedimiento() + "',"+ codTramite+","+ocuTramite+","+adjunto.getNumeroUnidad()+")";

                    if(m_Log.isDebugEnabled()) m_Log.debug(sql);
                    ps = con.prepareStatement(sql);        
                    int rowsInserted = ps.executeUpdate(sql);
                    m_Log.debug("Nº de filas insertadas: " + rowsInserted);
                 
                }
                        
            }
            
            exito = true;

    }catch (Exception e){      
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        exito = false;
    }finally{
        try{
            if (ps!=null) ps.close();            
            
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
    }
      
    return exito;
  }
  
  

  
  
   /** 
   * Da de alta un adjunto externo asociada a una determinada notificación
   * @param numExpediente: Nº del expediente
   * @param codigo: Código de la notificación
   * @param adjuntoNotificacionVO: Objeto de tiop AdjuntoNotificacionVO con el contenido binario del fichero
   * @param tipoGestor: boolean
   * @param con: Conexión a la BBDD
   */ 
    public boolean actualizarAdjuntoExterno(String numeroExpediente,int codigo,ArrayList<AdjuntoNotificacionVO> adjuntos,String TIPO_GESTOR,Connection con) throws TechnicalException {
      
      PreparedStatement ps = null;      
      String sql = "";
      boolean exito = false;
        
      try{                      
            sql = "DELETE FROM ADJUNTO_EXT_NOTIFICACION WHERE ID_NOTIFICACION=" + codigo;
            ps = con.prepareStatement(sql);
            int rowsUpdated = ps.executeUpdate();
            m_Log.debug("Se han eliminado " + rowsUpdated + " filas");
                        
            int contador = 0;
            for(int j=0;adjuntos!=null && j<adjuntos.size();j++){
                
                AdjuntoNotificacionVO adjuntoNotificacionVO = adjuntos.get(j);                
                int codigoMunicipio=adjuntoNotificacionVO.getCodigoMunicipio();                        
                int codTramite=adjuntoNotificacionVO.getCodigoTramite();
                int ocuTramite=adjuntoNotificacionVO.getOcurrenciaTramite();                        
                String nombre = adjuntoNotificacionVO.getNombre();
                String tipoMime = adjuntoNotificacionVO.getContentType();

                sql ="INSERT INTO ADJUNTO_EXT_NOTIFICACION(";
                if(TIPO_GESTOR.equalsIgnoreCase("ORACLE"))
                    sql = sql + "ID,";

                sql = sql + "COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,FECHA,CONTENIDO,ID_NOTIFICACION,NOMBRE,TIPO_MIME,ESTADO_FIRMA) ";

                if(TIPO_GESTOR.equalsIgnoreCase("ORACLE")) sql = sql +  " VALUES(SEQ_FILE_EXT_NOTIFICACION.NextVal,?,?,?,?,?,?,?,?,?,?)";
                else sql = sql +  " VALUES(?,?,?,?,?,?,?,?,?,?)";
                m_Log.debug(sql);

                int i=1;
                ps = con.prepareStatement(sql);
                ps.setInt(i++,codigoMunicipio);
                ps.setString(i++,numeroExpediente);
                ps.setInt(i++,codTramite);
                ps.setInt(i++,ocuTramite);                        
                ps.setTimestamp(i++,DateOperations.toTimestamp(Calendar.getInstance()));            
                InputStream st = new ByteArrayInputStream(adjuntoNotificacionVO.getContenido());
                ps.setBinaryStream(i++,st,adjuntoNotificacionVO.getContenido().length);
                ps.setInt(i++, codigo);
                ps.setString(i++,nombre);
                ps.setString(i++,tipoMime);
                ps.setString(i++,ESTADO_FIRMA_PENDIENTE);
                int rowsInserted =ps.executeUpdate();            
                m_Log.debug(" Filas insertadas:  " + rowsInserted);
                if(rowsInserted==1) contador++;                
            }
            
            if(adjuntos==null || (adjuntos!=null && contador==adjuntos.size())) exito = true;

      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        exito = false;
        
      }finally{
          
            try{
                if (ps!=null) ps.close();

            }catch(Exception bde) {
                bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
                return false;
            }
      }

     return exito;
  }
  

    
    
    
 /**
   * Recupera los adjuntos externos de notificaciones que son enviados al portafirmas
   * @param nifUsuarioDelegado: NIF del usuario delegado
   * @param nifUsuarioActual: NIF del usuario actual
   * @param estado: Estado de la firma
   * @param con: Conexión a la BBDD
   * @return Booleano
   * @throws TechnicalException 
   */  
  public ArrayList<AdjuntoNotificacionVO> getListaAdjuntosExternosNotificacionPortafirmas(int codUsuario,String estado,Connection con) throws TechnicalException {
      
      ArrayList<AdjuntoNotificacionVO> lista = new ArrayList<AdjuntoNotificacionVO>();
      ResultSet rs = null;
      Statement st = null;      
      String sql = "";
        
      try{      
          
        // Se comprueba si en el usuario actual se ha delegado alguna firma de documentos externos de notificación, porque en ese caso,
        // se recuperan también los documentos
        sql = "SELECT USU_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USU_FIR_DEL WHERE USU_DELEGADO=" + codUsuario;
        m_Log.debug(sql);
        
        st = con.createStatement();
        rs = st.executeQuery(sql);
        int usuQueDelega = -1;
        while(rs.next()){
            usuQueDelega = rs.getInt("USU_COD");
        }
        
        m_Log.debug("USUARIO QUE DELEGA EN EL USUARIO ACTUAL " + codUsuario + " ES : " + usuQueDelega);
        st.close();
        rs.close();
        
        
        sql = "SELECT * FROM E_PML,E_TRA,ADJUNTO_EXT_NOTIFICACION WHERE E_TRA.TRA_TIPO_USUARIO_FIRMA=1 AND TRA_PRO=PML_COD AND TRA_MUN = PML_MUN ";              
        if(usuQueDelega==-1)
            sql = sql + " AND TRA_OTRO_COD_USUARIO_FIRMA=" + codUsuario;
        else
            sql = sql + " AND (TRA_OTRO_COD_USUARIO_FIRMA=" + codUsuario + " OR TRA_OTRO_COD_USUARIO_FIRMA=" + usuQueDelega + ")";
        
        if(estado!=null && !"".equals(estado) && !"null".equals(estado))
            sql = sql + " AND ESTADO_FIRMA='" + estado + "'";
                         
        m_Log.debug(sql);                        
        st = con.createStatement();
        rs = st.executeQuery(sql);
            
        while(rs.next()){     
            AdjuntoNotificacionVO adjunto = new AdjuntoNotificacionVO();
            
            String nombreProcedimiento = rs.getString("PML_VALOR");            
            String numExpediente = rs.getString("NUM_EXPEDIENTE");
            adjunto.setIdDocExterno(rs.getInt("ID"));
            adjunto.setNombreProcedimiento(nombreProcedimiento);
            adjunto.setNumeroExpediente(numExpediente);
            adjunto.setEjercicio(Integer.parseInt(numExpediente.substring(0,4)));            
            adjunto.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
            adjunto.setCodigoNotificacion(rs.getInt("ID_NOTIFICACION"));
            adjunto.setCodigoTramite(rs.getInt("COD_TRAMITE"));
            adjunto.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
            adjunto.setFechaAlta(DateOperations.toCalendar(rs.getTimestamp("FECHA")));
            adjunto.setNombre(rs.getString("NOMBRE"));
            adjunto.setContentType(rs.getString("TIPO_MIME"));                
            adjunto.setFirma(rs.getString("FIRMA"));
            adjunto.setEstadoFirma(rs.getString("ESTADO_FIRMA"));
            int codUsuarioFirma = rs.getInt("COD_USUARIO_FIRMA");
            if(codUsuarioFirma!=-1) adjunto.setCodUsuarioFirmaOtro(codUsuario);            
            adjunto.setIdDocExterno(rs.getInt("ID"));
            java.sql.Timestamp fechaFirma = rs.getTimestamp("FECHA_FIRMA");            
            if(fechaFirma!=null) adjunto.setFechaFirma(DateOperations.toCalendar(rs.getTimestamp("FECHA_FIRMA")));
            
            

            lista.add(adjunto);
        } 

      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
      }finally{
          
        try{
          if (st!=null) st.close();
          if (rs!=null) rs.close();                
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }

      return lista;
  }
    
   
  
  /**
   * Recupera el contenido binario de un adjunto externo asociado a una notificación a través del plugin
   * de almacenamiento/recuperación de documentos
   * @param adjunto: Objeto de tipo AdjuntoNotificacionVO con los datos del adjunto
   * @param con: Conexión a la BBDD
   * @return byte[] si todo ha ido bien y false en caso contrario
   */
  
  private byte[] getContenidoAdjuntoExternoNotificacionPlugin(AdjuntoNotificacionVO adjunto,Connection con) throws TechnicalException{
      byte[] contenido = null;      
      try{
          
          String codigo            = Integer.toString(adjunto.getIdDocExterno());
          String codMunicipio      = Integer.toString(adjunto.getCodigoMunicipio());
          String codProcedimiento  = adjunto.getCodigoProcedimiento();
          String ejercicio         = Integer.toString(adjunto.getEjercicio());
          String numExpediente     = adjunto.getNumeroExpediente();
          String codTramite        = Integer.toString(adjunto.getCodigoTramite());
          String ocurrenciaTramite = Integer.toString(adjunto.getOcurrenciaTramite());
          
          Hashtable<String,Object> datos = new Hashtable<String,Object>();
          datos.put("codMunicipio",codMunicipio);
          datos.put("ejercicio",ejercicio);
          datos.put("numeroExpediente",numExpediente);              
          datos.put("codTramite",codTramite);
          datos.put("ocurrenciaTramite",ocurrenciaTramite);                  
          datos.put("codDocumento",codigo);
          
          AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);          
          Documento doc = null;
          int tipoDocumento = -1;
          if(!almacen.isPluginGestor())
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
          else{
            codProcedimiento = numExpediente.split("[/]")[1];
            String nombreProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento, con);                        
            //AdjuntoNotificacionVO adjunto = AdjuntoNotificacionDAO.getInstance().getInfoDocumentoExternoNotificacion(Integer.parseInt(codigo),con);
            
            String nombreOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codMunicipio), con);
            datos.put("nombreDocumento",FileOperations.getNombreArchivo(adjunto.getNombre()));
            datos.put("extension",FileOperations.getExtension(adjunto.getNombre()));
            datos.put("tipoMime", adjunto.getContentType());            
            
            ResourceBundle bundle = ResourceBundle.getBundle("documentos");     
            String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
            
            datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
            ArrayList<String> listaCarpetas = new ArrayList<String>();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + nombreOrganizacion);
            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
            listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));                    
            listaCarpetas.add(ConstantesDatos.CARPETA_DOCUMENTOS_EXTERNOS_NOTIFICACION);
            datos.put("listaCarpetas",listaCarpetas);

            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
         }

          
         doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
         doc = almacen.getDocumentoExternoNotificacion(doc,con);

         contenido = doc.getFichero();
                
        }catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();                  
            m_Log.error(e.getMessage());
            throw new TechnicalException("Error al recuperar el contenido del documento a través del plugin: " + e.getMessage());
            
        }catch(Exception e){
            e.printStackTrace();                  
            m_Log.error(e.getMessage());
            throw new TechnicalException("Error al recuperar el contenido del documento a través del plugin: " + e.getMessage());
        }
      
      return contenido;
  }
      
  
  
 /**
   * Recupera un determinado adjunto
   * @param codAdjunto: Código del adjunto   
   * @param con: Conexión a la BBDD
   * @return AdjuntoNotificacionVO
   * @throws TechnicalException 
   */  
  public AdjuntoNotificacionVO getAdjuntoExternoNotificacion(int codAdjunto,Connection con){
      
      AdjuntoNotificacionVO adjunto = null;
      ResultSet rs = null;
      Statement st = null;      
      String sql = "";
        
      try{      
          
        // Se comprueba si en el usuario actual se ha delegado alguna firma de documentos externos de notificación, porque en ese caso,
        // se recuperan también los documentos
        sql = "SELECT ID,COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,TIPO_MIME,NOMBRE,FIRMA FROM ADJUNTO_EXT_NOTIFICACION WHERE ID=" + codAdjunto;
        m_Log.debug(sql);
        
        st = con.createStatement();
        rs = st.executeQuery(sql);
        
        while(rs.next()){       
            adjunto = new AdjuntoNotificacionVO();           
            adjunto.setContentType(rs.getString("TIPO_MIME"));
            adjunto.setNombre(rs.getString("NOMBRE"));
            adjunto.setContentType(rs.getString("TIPO_MIME"));
            adjunto.setFirma(rs.getString("FIRMA"));
            adjunto.setIdDocExterno(rs.getInt("ID"));
            adjunto.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
            adjunto.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
            adjunto.setCodigoTramite(rs.getInt("COD_TRAMITE"));
            adjunto.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
            
            String numExpediente = adjunto.getNumeroExpediente();
            String[] datos = numExpediente.split("/");
            adjunto.setEjercicio(Integer.parseInt(datos[0]));
            adjunto.setCodigoProcedimiento(datos[1]);
            
            adjunto.setContenido(this.getContenidoAdjuntoExternoNotificacionPlugin(adjunto, con));            
        }
        

      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
      }finally{
          
        try{
          if (st!=null) st.close();
          if (rs!=null) rs.close();                
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }

      return adjunto;
  }
  
  
  
  
 /**
   * Almacena la firma de un documento externo asociado a una notificación electrónca
   * @param Adjunto: Objeto de tipo AdjuntoNotificacionVO
   * @param con: Conexión a la BBDD
   * @return boolean
   * @throws TechnicalException 
   */  
  public boolean setFirmaAdjuntoExternoNotificacion(AdjuntoNotificacionVO adjunto,Connection con){
      
      boolean exito = false;      
      PreparedStatement ps = null;      
      String sql = "";
        
      try{      
          
        // Se comprueba si en el usuario actual se ha delegado alguna firma de documentos externos de notificación, porque en ese caso,
        // se recuperan también los documentos
        sql = "UPDATE ADJUNTO_EXT_NOTIFICACION SET FIRMA=?,ESTADO_FIRMA=?, FECHA_FIRMA=?, PLATAFORMA_FIRMA=?,COD_USUARIO_FIRMA=?, TIPO_CERTIFICADO_FIRMA=? WHERE ID=?";
        m_Log.debug(sql);
        
        int i = 1;
        ps = con.prepareStatement(sql);
        
        ps.setString(i++,adjunto.getFirma());
        ps.setString(i++,ESTADO_FIRMA_FIRMADO);        
        ps.setTimestamp(i++, DateOperations.toTimestamp(adjunto.getFechaFirma()));
        ps.setString(i++,adjunto.getPlataformaFirma());
        ps.setInt(i++,adjunto.getCodUsuarioFirmaOtro());
        ps.setInt(i++, adjunto.getTipoCertificadoFirma());
        ps.setInt(i++,adjunto.getIdDocExterno());
        
        int rowsUpdated = ps.executeUpdate();
        m_Log.debug("rowsUpdated: " + rowsUpdated);
        if(rowsUpdated==1) exito= true;
        

      }catch (Exception e){
          exito = false;
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
      }finally{
          
        try{
          if (ps!=null) ps.close();          
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }

      return exito;
  }
  

  
  
  
          
          
 /**
   * Recupera una determinado documento externo asociado a una notificación electrónica
   * @param codDocumento: Código del documento
   * @param con: Conexión a la BBDD
   * @return AdjuntoNotificacionVO o null sino se ha podido recuperar   
   */  
  public AdjuntoNotificacionVO getDocumentoExternoNotificacion(int codDocumento,Connection con){
      
      AdjuntoNotificacionVO adjunto = null;
      Statement st = null;      
      ResultSet rs = null;
      String sql = "";
        
      try{      
          
        // Se comprueba si en el usuario actual se ha delegado alguna firma de documentos externos de notificación, porque en ese caso,
        // se recuperan también los documentos
        sql = "SELECT * FROM ADJUNTO_EXT_NOTIFICACION WHERE ID=" + codDocumento;
        m_Log.debug(sql);
        
        int i = 1;
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while(rs.next()){
            adjunto = new AdjuntoNotificacionVO();
            byte[] fichero = null;
            InputStream str = rs.getBinaryStream("CONTENIDO");
            ByteArrayOutputStream ot = new ByteArrayOutputStream();
            int c;
            while ((c = str.read())!= -1){
                ot.write(c);
            }

            ot.flush();                
            fichero = ot.toByteArray(); 
            str.close();
            adjunto.setContenido(fichero);    
            adjunto.setContentType(rs.getString("TIPO_MIME"));
            adjunto.setNombre(rs.getString("NOMBRE"));
            adjunto.setEstadoFirma(rs.getString("ESTADO_FIRMA"));
            java.sql.Timestamp fechaFirma = rs.getTimestamp("FECHA_FIRMA");
            if(fechaFirma!=null) adjunto.setFechaFirma(DateOperations.toCalendar(fechaFirma));
            adjunto.setCodUsuarioFirmaOtro(rs.getInt("COD_USUARIO_FIRMA"));
            adjunto.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
            adjunto.setCodigoNotificacion(codDocumento);
            adjunto.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
            adjunto.setCodigoTramite(rs.getInt("COD_TRAMITE"));
            adjunto.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
            
            java.sql.Timestamp fechaAlta = rs.getTimestamp("FECHA");
            if(fechaAlta!=null) adjunto.setFechaAlta(DateOperations.toCalendar(fechaAlta));                     
            
            adjunto.setPlataformaFirma(rs.getString("PLATAFORMA_FIRMA"));
            adjunto.setIdDocExterno(rs.getInt("ID"));
            adjunto.setFirma(rs.getString("FIRMA"));
            java.sql.Timestamp fechaRechazo = rs.getTimestamp("FECHA_RECHAZO");
            if(fechaRechazo!=null) adjunto.setFechaRechazo(DateOperations.toCalendar(fechaRechazo));
            adjunto.setCodUsuarioRechazo(rs.getInt("COD_USUARIO_RECHAZO"));
            adjunto.setObservacionesRechazo(rs.getString("OBSERVACIONES_RECHAZO"));
            
        }
                

      }catch (Exception e){          
         e.printStackTrace();
         if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
         
      }finally{
          
        try{
          if (st!=null) st.close();          
          if (rs!=null) rs.close();          
          
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }
      return adjunto;
  }        
 
  
  
  /**
   * Recupera una determinado documento externo asociado a una notificación electrónica
   * @param codDocumento: Código del documento
   * @param con: Conexión a la BBDD
   * @return AdjuntoNotificacionVO o null sino se ha podido recuperar   
   */  
  public AdjuntoNotificacionVO getDocumentoExternoNotificacion(int codDocumento,boolean expedienteHistorico,Connection con){
      
      AdjuntoNotificacionVO adjunto = null;
      PreparedStatement ps = null;      
      ResultSet rs = null;
      String sql = "";
        
      try{      
        
        if(!expedienteHistorico) {
            // Se comprueba si en el usuario actual se ha delegado alguna firma de documentos externos de notificación, porque en ese caso,
            // se recuperan también los documentos
            sql = "SELECT * FROM ADJUNTO_EXT_NOTIFICACION WHERE ID=?";
            
        } else {
            sql = "SELECT * FROM HIST_ADJUNTO_EXT_NOTIFICACION WHERE ID=?";            
        }
        m_Log.debug(sql);
        
        int i = 1;
        //st = con.createStatement();
        ps = con.prepareStatement(sql);
        ps.setInt(1,codDocumento);
        rs = ps.executeQuery();
        
        while(rs.next()){
            adjunto = new AdjuntoNotificacionVO();
            byte[] fichero = null;
            InputStream str = rs.getBinaryStream("CONTENIDO");
            ByteArrayOutputStream ot = new ByteArrayOutputStream();
            int c;
            while ((c = str.read())!= -1){
                ot.write(c);
            }

            ot.flush();                
            fichero = ot.toByteArray(); 
            str.close();
            adjunto.setContenido(fichero);    
            adjunto.setContentType(rs.getString("TIPO_MIME"));
            adjunto.setNombre(rs.getString("NOMBRE"));
            adjunto.setEstadoFirma(rs.getString("ESTADO_FIRMA"));
            java.sql.Timestamp fechaFirma = rs.getTimestamp("FECHA_FIRMA");
            if(fechaFirma!=null) adjunto.setFechaFirma(DateOperations.toCalendar(fechaFirma));
            adjunto.setCodUsuarioFirmaOtro(rs.getInt("COD_USUARIO_FIRMA"));
            adjunto.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
            adjunto.setCodigoNotificacion(codDocumento);
            adjunto.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
            adjunto.setCodigoTramite(rs.getInt("COD_TRAMITE"));
            adjunto.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
            
            java.sql.Timestamp fechaAlta = rs.getTimestamp("FECHA");
            if(fechaAlta!=null) adjunto.setFechaAlta(DateOperations.toCalendar(fechaAlta));                     
            
            adjunto.setPlataformaFirma(rs.getString("PLATAFORMA_FIRMA"));
            adjunto.setIdDocExterno(rs.getInt("ID"));
            adjunto.setFirma(rs.getString("FIRMA"));
            java.sql.Timestamp fechaRechazo = rs.getTimestamp("FECHA_RECHAZO");
            if(fechaRechazo!=null) adjunto.setFechaRechazo(DateOperations.toCalendar(fechaRechazo));
            adjunto.setCodUsuarioRechazo(rs.getInt("COD_USUARIO_RECHAZO"));
            adjunto.setObservacionesRechazo(rs.getString("OBSERVACIONES_RECHAZO"));
            adjunto.setTipoCertificadoFirma(rs.getInt("TIPO_CERTIFICADO_FIRMA"));
        }
                

      }catch (Exception e){          
         e.printStackTrace();
         if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
         
      }finally{
          
        try{
          if (ps!=null) ps.close();          
          if (rs!=null) rs.close();          
          
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }
      return adjunto;
  }        
  
  
  /**
   * Modifica el estado de la firma de un documento externo asociado a una notificación
   * @param codAdjunto: Código del adjunto
   * @param codUsuario: Código del usuario que realiza el cambio de estado para tenerlo en cuenta si el estado del documento es RECHAZADO. Obligatorio si se rechaza el documento
   * @param observaciones: Observaciones si las hay. Se tienen en cuanta a la hora de rechazar
   * @param con: Conexión a la BBDD
   * @return boolean   
   */  
  public boolean actualizarEstadoFirmaAdjuntoExternoNotificacion(int codAdjunto,int codUsuario,String estado,String observaciones,Connection con){
      
      boolean exito = false;      
      PreparedStatement ps = null;      
      String sql = "";
        
      try{      
          
        // Se comprueba si en el usuario actual se ha delegado alguna firma de documentos externos de notificación, porque en ese caso,
        // se recuperan también los documentos
        
        sql = "UPDATE ADJUNTO_EXT_NOTIFICACION SET ESTADO_FIRMA=?,FECHA_RECHAZO=?,COD_USUARIO_RECHAZO=?,OBSERVACIONES_RECHAZO=? WHERE ID=?";
        
        
        m_Log.debug(sql);
        
        int i = 1;
        ps = con.prepareStatement(sql);
                
        ps.setString(i++,estado);
        if(estado.equalsIgnoreCase(this.ESTADO_FIRMA_RECHAZADO))          
            ps.setTimestamp(i++,DateOperations.toTimestamp(Calendar.getInstance()));
        else
            ps.setNull(i++, java.sql.Types.TIMESTAMP);
        
        if(estado.equalsIgnoreCase(this.ESTADO_FIRMA_RECHAZADO))          
            ps.setInt(i++,codUsuario);
        else
            ps.setNull(i++, java.sql.Types.INTEGER);
        
        if(observaciones!=null && !"".equals(observaciones)) 
            ps.setString(i++,observaciones);
        else 
            ps.setNull(i++,java.sql.Types.VARCHAR);
        
        ps.setInt(i++,codAdjunto);
        int rowsUpdated = ps.executeUpdate();
        m_Log.debug("rowsUpdated: " + rowsUpdated);
        if(rowsUpdated==1) exito= true;
        

      }catch (Exception e){
          exito = false;
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
      }finally{
          
        try{
          if (ps!=null) ps.close();          
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }

      return exito;
  }
  
  
 
  
  /** 
   * Eliminar un determinado adjunto asociado a una notificación
   * @param codAdjunto: Código adjnton   
   * @param con: Conexión a la BBDD
   */ 
    public boolean eliminarAdjuntoExterno(int codAdjunto,Connection con) throws TechnicalException {
      
      Statement st = null;      
      String sql = "";
      boolean exito = false;
      
      
      try{
                    
            sql ="DELETE FROM ADJUNTO_EXT_NOTIFICACION WHERE ID=" + codAdjunto;
            st = con.createStatement();
            int rowsUpdated = st.executeUpdate(sql);
            if(rowsUpdated==1) exito = true;        

      }catch (Exception e){        
        exito = false;                
      }finally{
          
        try{
          if (st!=null) st.close();
                
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            exito = false;
        }
      }

    return exito;
  }
    
    
    
  /** 
   * Eliminar un determinado adjunto asociado a una notificación
   * @param codAdjunto: Código adjnton   
   * @param con: Conexión a la BBDD
   */ 
    public boolean guardarFirma(AdjuntoNotificacionVO adjunto,Connection con) throws TechnicalException {
      
      PreparedStatement ps = null;            
      boolean exito = false;
      
      try{                    
            String sql ="UPDATE ADJUNTO_EXT_NOTIFICACION SET FIRMA=?,ESTADO_FIRMA=?,FECHA_FIRMA=?,PLATAFORMA_FIRMA=?,COD_USUARIO_FIRMA=?,TIPO_CERTIFICADO_FIRMA=? " + 
                        "WHERE ID=? AND ID_NOTIFICACION=?";
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            int i=1;
            
            String firma = adjunto.getFirma();
            String estado = adjunto.getEstadoFirma();
            String plataforma = adjunto.getPlataformaFirma();
            int codUsuario = adjunto.getCodUsuarioFirmaOtro();
            int tipoCertificado = adjunto.getTipoCertificadoFirma();
            int id = adjunto.getIdDocExterno();
            int idNotif = adjunto.getCodigoNotificacion();
            
            ps.setString(i++,firma);
            ps.setString(i++,estado);
            ps.setTimestamp(i++,DateOperations.toTimestamp(Calendar.getInstance()));
            ps.setString(i++,plataforma);
            ps.setInt(i++,codUsuario);
            ps.setInt(i++,tipoCertificado);
            ps.setInt(i++,id);
            ps.setInt(i++,idNotif);
            int rowsUpdated = ps.executeUpdate();
            m_Log.debug("rowsUpdated: " + rowsUpdated);
            if(rowsUpdated==1) exito = true;        

      }catch (Exception e){   
          e.printStackTrace();
        exito = false;                
      }finally{
          
        try{
          if (ps!=null) ps.close();
                
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            exito = false;
        }
      }

    return exito;
  }  
    
    
   /** 
   * Da de alta un adjunto externo asociada a una determinada notificación pero el binario no se almacena en base de datos
   * @param adjuntoNotificacionVO: Objeto de tiop AdjuntoNotificacionVO 
   * @param tipoGestor: boolean
   * @param con: Conexión a la BBDD
   */ 
    public int insertarAdjuntoExternosSinBinario(AdjuntoNotificacionVO adjuntoNotificacionVO,String TIPO_GESTOR,Connection con) throws TechnicalException {
      int salida = -1;
      PreparedStatement ps = null;      
      ResultSet rs = null;
      String sql = "";

      try{
            
            int codigoMunicipio=adjuntoNotificacionVO.getCodigoMunicipio();            
            String numeroExpediente =adjuntoNotificacionVO.getNumeroExpediente();            
            int codTramite=adjuntoNotificacionVO.getCodigoTramite();
            int ocuTramite=adjuntoNotificacionVO.getOcurrenciaTramite();            
            int codigo=adjuntoNotificacionVO.getCodigoNotificacion();
            String nombre = adjuntoNotificacionVO.getNombre();
            String tipoMime = adjuntoNotificacionVO.getContentType();
                    
            sql ="INSERT INTO ADJUNTO_EXT_NOTIFICACION(";
            if(TIPO_GESTOR.equalsIgnoreCase("ORACLE"))
                sql = sql + "ID,";

            sql = sql + "COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,FECHA,ID_NOTIFICACION,NOMBRE,TIPO_MIME,ESTADO_FIRMA) ";

            if(TIPO_GESTOR.equalsIgnoreCase("ORACLE")) sql = sql +  " VALUES(SEQ_FILE_EXT_NOTIFICACION.NextVal,?,?,?,?,?,?,?,?,?)";
            else sql = sql +  " VALUES(?,?,?,?,?,?,?,?,?)";
            m_Log.debug(sql);
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++,codigoMunicipio);
            ps.setString(i++,numeroExpediente);
            ps.setInt(i++,codTramite);
            ps.setInt(i++,ocuTramite);                        
            ps.setTimestamp(i++,DateOperations.toTimestamp(Calendar.getInstance()));             
            ps.setInt(i++, codigo);
            ps.setString(i++,nombre);
            ps.setString(i++,tipoMime);
            ps.setString(i++,ESTADO_FIRMA_PENDIENTE);
            int rowsInserted =ps.executeUpdate();                        
            m_Log.debug(" Filas insertadas:  " + rowsInserted);
            ps.close();
            if(rowsInserted==1){
                sql = "SELECT MAX(ID) AS NUM FROM ADJUNTO_EXT_NOTIFICACION";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                
                while(rs.next()){                    
                    salida = rs.getInt("NUM");
                }                
            }

      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
                
      }finally{
          
        try{
          if (ps!=null) ps.close();
                
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }

      return salida;
  }  
    
    
   /**
    * Recupera el nombre y tipo mime de un determinado documento externo asociado a una notificación
    * @param codDocumento: Código del documento
    * @param con: Conexión a la BBDD
    * @return Objeto AdjuntoNotificacionVO con la info del documento recuperado
    */ 
   public AdjuntoNotificacionVO getInfoDocumentoExternoNotificacion(int codDocumento,Connection con){
      
      AdjuntoNotificacionVO adjunto = null;
      Statement st = null;      
      ResultSet rs = null;
      String sql = "";
        
      try{      
          
        // Se comprueba si en el usuario actual se ha delegado alguna firma de documentos externos de notificación, porque en ese caso,
        // se recuperan también los documentos
        sql = "SELECT TIPO_MIME,NOMBRE FROM ADJUNTO_EXT_NOTIFICACION WHERE ID=" + codDocumento;
        m_Log.debug(sql);
        
        int i = 1;
        st = con.createStatement();
        rs = st.executeQuery(sql);
        while(rs.next()){
            adjunto = new AdjuntoNotificacionVO();            
            adjunto.setContentType(rs.getString("TIPO_MIME"));
            adjunto.setNombre(rs.getString("NOMBRE"));            
        }
                

      }catch (Exception e){          
         e.printStackTrace();
         if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
      }finally{
          
        try{
          if (st!=null) st.close();          
          if (rs!=null) rs.close();          
          
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }
      return adjunto;
  }          
   

    /**
    * Recupera el nombre y tipo mime de un determinado documento externo asociado a una notificación
    * @param codDocumento: Código del documento
    * @param con: Conexión a la BBDD
    * @return Objeto AdjuntoNotificacionVO con la info del documento recuperado
    */ 
    public AdjuntoNotificacionVO getInfoDocumentoExternoNotificacion(int codDocumento,boolean expedienteHistorico,Connection con){
      
        AdjuntoNotificacionVO adjunto = null;
        PreparedStatement ps = null;      
        ResultSet rs = null;
        String sql = "";
        
        try{      
            // Se comprueba si en el usuario actual se ha delegado alguna firma de documentos externos de notificación, porque en ese caso,
            // se recuperan también los documentos
            if(!expedienteHistorico)
                sql = "SELECT TIPO_MIME,NOMBRE FROM ADJUNTO_EXT_NOTIFICACION WHERE ID=?";
            else
                sql = "SELECT TIPO_MIME,NOMBRE FROM HIST_ADJUNTO_EXT_NOTIFICACION WHERE ID=?";
            
            m_Log.debug(sql);
            ps = con.prepareStatement(sql);
            ps.setInt(1,codDocumento);
            
            rs = ps.executeQuery();            
            while(rs.next()){
                adjunto = new AdjuntoNotificacionVO();            
                adjunto.setContentType(rs.getString("TIPO_MIME"));
                adjunto.setNombre(rs.getString("NOMBRE"));            
            }
                
        }catch (Exception e){          
           e.printStackTrace();
           if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
        }finally{

          try{
            if (ps!=null) ps.close();          
            if (rs!=null) rs.close();          

          }catch(Exception bde) {
              bde.printStackTrace();
              if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
          }
        }
        return adjunto;
    }          
   
 /**
   * Esta operación indica en BBDD que un documento externo de una notificación ha sido firmado, pero 
   * la firma no se almacena en BBDD
   * @param Adjunto: Objeto de tipo AdjuntoNotificacionVO
   * @param con: Conexión a la BBDD
   * @return True si todo correcto y false en caso contrario
   * @throws TechnicalException si ocurre algún error
   */  
  public boolean setFirmaAdjuntoExternoNotificacionSinBinario(AdjuntoNotificacionVO adjunto,Connection con){
      
      boolean exito = false;      
      PreparedStatement ps = null;      
      String sql = "";
        
      try{      
          
        // Se comprueba si en el usuario actual se ha delegado alguna firma de documentos externos de notificación, porque en ese caso,
        // se recuperan también los documentos
        sql = "UPDATE ADJUNTO_EXT_NOTIFICACION SET ESTADO_FIRMA=?, FECHA_FIRMA=?, PLATAFORMA_FIRMA=?,COD_USUARIO_FIRMA=? WHERE ID=?";
        m_Log.debug(sql);
        
        int i = 1;
        ps = con.prepareStatement(sql);
        ps.setString(i++,ESTADO_FIRMA_FIRMADO);        
        ps.setTimestamp(i++, DateOperations.toTimestamp(adjunto.getFechaFirma()));
        ps.setString(i++,adjunto.getPlataformaFirma());
        ps.setInt(i++,adjunto.getCodUsuarioFirmaOtro());
        ps.setInt(i++,adjunto.getIdDocExterno());
        
        int rowsUpdated = ps.executeUpdate();
        m_Log.debug("rowsUpdated: " + rowsUpdated);
        if(rowsUpdated==1) exito= true;
        

      }catch (Exception e){
          exito = false;
          e.printStackTrace();
          if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
          
      }finally{
          
        try{
          if (ps!=null) ps.close();          
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }
      return exito;
   }   
  
  
  
  
  /**
   * Recupera la firma de un determinado adjunto externo asociado a una notificación electrónica
   * @param codAdjunto: Código del adjunto   
   * @param con: Conexión a la BBDD
   * @return String con la firma
   * @throws TechnicalException 
   */  
  public String getFirmaAdjuntoExternoNotificacion(int codAdjunto,Connection con){
      
      String salida = null;
      ResultSet rs = null;
      Statement st = null;      
      String sql = "";
        
      try{      
        
        sql = "SELECT FIRMA FROM ADJUNTO_EXT_NOTIFICACION WHERE ID=" + codAdjunto;
        m_Log.debug(sql);
        
        st = con.createStatement();
        rs = st.executeQuery(sql);
        
        while(rs.next()){            
            salida = rs.getString("FIRMA");            
        }
        

      }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());                
      }finally{
          
        try{
          if (st!=null) st.close();
          if (rs!=null) rs.close();                
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());            
        }
      }

      return salida;
  }
  
  
 /**
   * Recupera los datos de un adjunto, como el código de trámite, expediente, ocurrencia de trámite, etc...
   * @param codAdjunto: Código del adjunto   
   * @param expedienteHistorico: True si el expediente está en el histórico y false en caso contrario
   * @param con: Conexión a la BBDD
   * @return Objeto de la clase AdjuntoNotificacionVO  
   */  
  public AdjuntoNotificacionVO getAdjuntoById(int codAdjunto,boolean expedienteHistorico,Connection con){
      AdjuntoNotificacionVO adjunto = null;      
      ResultSet rs = null;
      Statement st = null;      
      String sql = null;        
      
      try{      
        
          
        if(!expedienteHistorico) {
            sql = "SELECT COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,NOMBRE,TIPO_MIME,ID_NOTIFICACION FROM ADJUNTO_EXT_NOTIFICACION WHERE ID=" + codAdjunto;
        } else {
            sql = "SELECT COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,NOMBRE,TIPO_MIME,ID_NOTIFICACION FROM HIST_ADJUNTO_EXT_NOTIFICACION WHERE ID=" + codAdjunto;
        }
        m_Log.debug(sql);
        
        st = con.createStatement();
        rs = st.executeQuery(sql);
        
        while(rs.next()){            
            adjunto = new AdjuntoNotificacionVO();
            adjunto.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
            adjunto.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
            adjunto.setCodigoTramite(rs.getInt("COD_TRAMITE"));
            adjunto.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
            adjunto.setContentType(rs.getString("TIPO_MIME"));
            adjunto.setCodigoNotificacion(rs.getInt("ID_NOTIFICACION"));
        }
        

      }catch (Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName() + ": " + e.getMessage());
        
      }finally{          
            try{
                  if (st!=null) st.close();
                  if (rs!=null) rs.close();                
                  
            }catch(Exception bde) {
                bde.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName() + ": " + bde.getMessage());
            }
      }
      return adjunto;
  }
  
}