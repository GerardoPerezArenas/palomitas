/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.plugin;

import es.altia.agora.technical.ConstantesDatos;
import java.sql.Connection;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.util.ArrayList;

import es.altia.flexia.notificacion.persistence.*;
import es.altia.flexia.notificacion.vo.*;
import es.altia.flexia.notificacion.firma.FirmaNotificacionAltia;


import es.altia.notificacion.webservice.servicioaltanotificacion.client.ServicioAltaNotificacion;
import es.altia.notificacion.webservice.servicioaltanotificacion.client.ServicioAltaNotificacionService;
import es.altia.notificacion.webservice.servicioaltanotificacion.client.ServicioAltaNotificacionServiceLocator;
import es.altia.util.conexion.BDException;
import es.altia.x509.certificados.validacion.ValidacionCertificado;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class PluginNotificacionAltia implements PluginNotificacion{

    protected static Config conf =
            ConfigServiceHelper.getConfig("notificaciones");
    
    private Logger log = Logger.getLogger(PluginNotificacionAltia.class);

    private String codOrganizacion;
   

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

   



    public boolean existenInteresadosAdmitenNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException {

        boolean res=false;

	try{

            AutorizadoNotificacionManager autorizadoNotificacionManager=AutorizadoNotificacionManager.getInstance();
            res=autorizadoNotificacionManager.existenInteresadosNotificacionElectronica(notificacionVO, params);
            
	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }

    public NotificacionVO getNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException {

        NotificacionVO notificacionVORetorno=new NotificacionVO();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
	try{

            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();
            
            //Otengo la notificacion
            notificacionVORetorno=notificacionVO;
            //NotificacionManager notificacionManager=NotificacionManager.getInstance();
            NotificacionDAO notificacionDAO = NotificacionDAO.getInstance();
            notificacionVORetorno=notificacionDAO.getNotificacion(notificacionVO,con);

           //Obtengo los documentos de la notificacion
            AdjuntoNotificacionVO adjuntoNotificacionVO=new AdjuntoNotificacionVO();
            adjuntoNotificacionVO.setCodigoMunicipio(notificacionVO.getCodigoMunicipio());
            adjuntoNotificacionVO.setEjercicio(notificacionVO.getEjercicio());
            adjuntoNotificacionVO.setNumeroExpediente(notificacionVO.getNumExpediente());
            adjuntoNotificacionVO.setCodigoProcedimiento(notificacionVO.getCodigoProcedimiento());
            adjuntoNotificacionVO.setCodigoTramite(notificacionVO.getCodigoTramite());
            adjuntoNotificacionVO.setOcurrenciaTramite(notificacionVO.getOcurrenciaTramite());
            adjuntoNotificacionVO.setCodigoNotificacion(notificacionVO.getCodigoNotificacion());

            
            /** DOCUMENTOS DE TRAMITACIÓN ANEXOS A LA NOTIFICACIÓN **/
            AdjuntoNotificacionDAO adjuntoNotificacionDAO = AdjuntoNotificacionDAO.getInstance();
            ArrayList<AdjuntoNotificacionVO> arrayDocumentos= new ArrayList<AdjuntoNotificacionVO>();
            arrayDocumentos=adjuntoNotificacionDAO.getDocumentosTramitacion(adjuntoNotificacionVO,false,con);
            notificacionVORetorno.setAdjuntos(arrayDocumentos);
            
            /** DOCUMENTOS EXTERNOS ANEXOS A LA NOTIFICACIÓN **/
            ArrayList<AdjuntoNotificacionVO> adjuntosExternos = adjuntoNotificacionDAO.getListaAdjuntosExterno(notificacionVO.getCodigoNotificacion(), con);
            notificacionVORetorno.setAdjuntosExternos(adjuntosExternos);
            

            /** SE RECUPERAN LOS INTERESADOS DEL EXPEDIENTE **/
            ArrayList<AutorizadoNotificacionVO> arrayAutorizadoNotif=new ArrayList<AutorizadoNotificacionVO>();
            AutorizadoNotificacionDAO autorizadoNotificacionDAO = AutorizadoNotificacionDAO.getInstance();
            arrayAutorizadoNotif=autorizadoNotificacionDAO.getInteresadosExpediente((notificacionVO.getNumExpediente()), notificacionVO.getCodigoNotificacion(),params[0], con);

            notificacionVORetorno.setAutorizados(arrayAutorizadoNotif);
	
	}catch (Exception e) {            
            log.error("PluginNotificacionAltia.getNotificacion(): Error al recuperar la notificación: " + e.getMessage());
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(),e);
	}finally{
            try{
                if(con!=null) con.close();
                
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD");
            }            
            return notificacionVORetorno;
	}
    }

    public String getUrlPantallaDatosNotificacion() throws TechnicalException {

        String url="";


        try{
            
            String plugin=conf.getString(codOrganizacion +"/plugin");

            url=conf.getString(codOrganizacion +"/Notificacion/"+plugin+"/urlPaginaDatosNotificacion");

           

        }catch (Exception e) {
            e.printStackTrace();
	}finally{
            return url;
	}
    }

    public boolean grabarNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException {

        boolean res=false;
        int codigoNotificacion=-1;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        
	try{

            // SE OBTIENE CONEXIÓN A LA BBDD
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            
            //Otengo la notificacion
            NotificacionManager notificacionManager=NotificacionManager.getInstance();

            codigoNotificacion=NotificacionDAO.getInstance().insertarNotificacion(notificacionVO,con);
            notificacionVO.setCodigoNotificacion(codigoNotificacion);
            ArrayList<AdjuntoNotificacionVO> arrayDocumentos= new ArrayList<AdjuntoNotificacionVO>();

            arrayDocumentos=notificacionVO.getAdjuntos();

            if(codigoNotificacion!=-1)
            {
                res=true;
            //Inserto documentos
                for(int i=0;i<arrayDocumentos.size();i++)
                {
                    AdjuntoNotificacionVO adjuntoVO=new AdjuntoNotificacionVO() ;
                    adjuntoVO=arrayDocumentos.get(i);
                    if(adjuntoVO.getSeleccionado().equals("SI"))
                    {
                        adjuntoVO.setCodigoNotificacion(codigoNotificacion);                        
                        AdjuntoNotificacionDAO adjuntoNotifDAO = AdjuntoNotificacionDAO.getInstance();
                        res=res&(adjuntoNotifDAO.insertarAdjunto(adjuntoVO, con));
                    }
                }

               ArrayList<AutorizadoNotificacionVO> arrayAutorizados= new ArrayList<AutorizadoNotificacionVO>();
               arrayAutorizados=notificacionVO.getAutorizados();

               //Inserto Autorizados
               for(int i=0;i<arrayAutorizados.size();i++)
                {
                    AutorizadoNotificacionVO autorizadoVO=new AutorizadoNotificacionVO() ;
                    autorizadoVO=arrayAutorizados.get(i);
                    if(autorizadoVO.getSeleccionado().equals("SI"))
                    {
                        autorizadoVO.setCodigoNotificacion(codigoNotificacion);
                        //AutorizadoNotificacionManager autorizadoNotificacionManager=AutorizadoNotificacionManager.getInstance();
                        AutorizadoNotificacionDAO autorizadoDAO = AutorizadoNotificacionDAO.getInstance();
                        res=res&(autorizadoDAO.insertarAutorizado(autorizadoVO, con));
                    }
                }
               
               
               /*
               ArrayList<AdjuntoNotificacionVO> adjuntosExternos = notificacionVO.getAdjuntosExternos();
               for(int i=0;adjuntosExternos!=null && i<adjuntosExternos.size();i++)
               {
                    AdjuntoNotificacionVO adjuntoVO=new AdjuntoNotificacionVO() ;
                    adjuntoVO=adjuntosExternos.get(i);
                    
                    adjuntoVO.setCodigoNotificacion(codigoNotificacion);                        
                    AdjuntoNotificacionDAO adjuntoNotifDAO = AdjuntoNotificacionDAO.getInstance();
                    
                    res=res&(adjuntoNotifDAO.insertarAdjuntoExterno(adjuntoVO,params[0],con));
                    
                }
               */
               
               
               
               
             }


	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            
            try{
                // Se cierra la conexión a la BBDD
                if(con!=null) con.close();
                
            }catch(Exception e){
                
            }
            
            return res;
	}

    }

     public boolean guardarFirma(int codigoNotificacion,String firma,String[] params) throws TechnicalException {

        boolean res=false;


	try{

            //Otengo la notificacion
            NotificacionManager notificacionManager=NotificacionManager.getInstance();

            res=notificacionManager.guardarFirma(codigoNotificacion, firma, params);




	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}

    }

    
     //LLAMADA AL SERVICIO WEB
    public boolean enviarNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException {
        
        boolean dev=false;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            AdjuntoNotificacionDAO adjuntoNotificacionDAO = AdjuntoNotificacionDAO.getInstance();
            
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            String plugin=conf.getString(codOrganizacion +"/plugin");
            String url=conf.getString(codOrganizacion +"/Notificacion/"+plugin+"/servicioWebNotificacion");
            String codAplicacion=conf.getString(codOrganizacion +"/"+plugin+"/codigo_aplicacion_ws_notificacion");
            String claseProcedimiento = "";
            
            try{
               claseProcedimiento = conf.getString(codOrganizacion+"/" +plugin+"/PROCEDIMIENTO/"+notificacionVO.getCodigoProcedimiento());

            }catch(Exception e){
                claseProcedimiento = conf.getString(codOrganizacion+"/" +plugin+"/PROCEDIMIENTO_SNE");
            }
            
            
            String departamento = NotificacionDAO.getInstance().getCodDepartamentoNotifTramite(notificacionVO.getCodigoTramite(),notificacionVO.getCodigoProcedimiento(),notificacionVO.getCodigoMunicipio(),con);
            
            notificacionVO.setAplicacion(codAplicacion);
            notificacionVO.setClaseProcedimiento(Integer.parseInt(claseProcedimiento));
            notificacionVO.setDepartamento(departamento);

            // Documentos anexados
            ArrayList<AdjuntoNotificacionVO> adjuntos = notificacionVO.getAdjuntosExternos();
            ArrayList<AdjuntoNotificacionVO> auxiliar = new ArrayList<AdjuntoNotificacionVO>();
            for(int i=0;adjuntos!=null && i<adjuntos.size();i++){
                AdjuntoNotificacionVO adjunto = adjuntos.get(i);

                if (ConstantesDatos.SI.equalsIgnoreCase(adjunto.getSeleccionado())) {
                    // Se recupera el contenido del adjunto                
                    AdjuntoNotificacionVO aux = adjuntoNotificacionDAO.getAdjuntoExternoNotificacion(adjunto.getIdDocExterno(), con);
                    adjunto.setContenido(aux.getContenido());
                    auxiliar.add(adjunto);
                }
            }
            notificacionVO.setAdjuntosExternos(auxiliar);
            
            // Documentos de tramitacion
            ArrayList<AdjuntoNotificacionVO> adjuntosDocsTramitacion = notificacionVO.getAdjuntos();
            auxiliar = new ArrayList<AdjuntoNotificacionVO>();
            for(int i=0;adjuntosDocsTramitacion!=null && i<adjuntosDocsTramitacion.size();i++){
                AdjuntoNotificacionVO adjunto = adjuntosDocsTramitacion.get(i);
                if (ConstantesDatos.SI.equalsIgnoreCase(adjunto.getSeleccionado())) {
                    adjunto.setContenido(adjuntoNotificacionDAO.getContenidoDocumentoTramitacionPlugin(adjunto, con));
                    auxiliar.add(adjunto);
                }
            }
            notificacionVO.setAdjuntos(auxiliar);
            
           FirmaNotificacionAltia firmaNotificacion=new FirmaNotificacionAltia();
           //dev=firmaNotificacion.crearNotificacion(notificacionVO,url,params);
           dev=firmaNotificacion.crearNotificacion(notificacionVO,url,con,params);
           adapt.finTransaccion(con);
        }catch(Exception e){
            try{
                adapt.rollBack(con);
            }catch(BDException f){
                f.printStackTrace();                        
            }
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }

        return dev;
    }
     

    public boolean existenDocumentosFirmados(NotificacionVO notificacionVO, String[] params) throws TechnicalException {

         boolean res=false;

	try{

            String numExpediente=notificacionVO.getNumExpediente();
            int codTramite=notificacionVO.getCodigoTramite();
            int ocuTramite=notificacionVO.getOcurrenciaTramite();

            AdjuntoNotificacionManager adjuntoNotificacionManager=AdjuntoNotificacionManager.getInstance();
            res=adjuntoNotificacionManager.existeDocumentosFirmados(numExpediente,codTramite,ocuTramite, params);
            

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }
    
    
     public boolean existenDocumentosPendientesFirma(NotificacionVO notificacionVO, String[] params) throws TechnicalException {

         boolean res=false;

	try{

            String numExpediente=notificacionVO.getNumExpediente();
            int codTramite=notificacionVO.getCodigoTramite();
            int ocuTramite=notificacionVO.getOcurrenciaTramite();

            AdjuntoNotificacionManager adjuntoNotificacionManager=AdjuntoNotificacionManager.getInstance();
            res=adjuntoNotificacionManager.existenDocumentosPendientesFirma(numExpediente,codTramite,ocuTramite, params);
            

	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }

    //LLAMADA AL SERVICIO WEB
    public String getNotificacionFirma(NotificacionVO notificacionVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String res="";

        try{

            String plugin=conf.getString(codOrganizacion +"/plugin");
            String codAplicacion=conf.getString(codOrganizacion +"/"+plugin+"/codigo_aplicacion_ws_notificacion");
            notificacionVO.setAplicacion(codAplicacion);            
            FirmaNotificacionAltia firmaNotificacion=new FirmaNotificacionAltia();
            res=firmaNotificacion.generarResumen(notificacionVO);

            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            boolean exito = NotificacionDAO.getInstance().guardarXMLFirmaNotificacion(notificacionVO.getCodigoNotificacion(),res, con);
            if(exito) adapt.finTransaccion(con);
            else adapt.rollBack(con);

        }catch (Exception e) {
             e.printStackTrace();
             try{
                 adapt.rollBack(con);
             }catch(Exception f){
                log.error("Error al realizar un rollback contra la BBDD: " + e.getMessage());
             }
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }

        }

        return res;

    }

    public boolean verificarFirma(NotificacionVO notificacionVO, String firma,String[] params) throws TechnicalException {

      boolean res=false;
      String xml="";

	try{

            String plugin=conf.getString(codOrganizacion +"/plugin");

            String codAplicacion=conf.getString(codOrganizacion +"/"+plugin+"/codigo_aplicacion_ws_notificacion");

            notificacionVO.setAplicacion(codAplicacion);

            FirmaNotificacionAltia firmaNotificacion=new FirmaNotificacionAltia();
            xml=firmaNotificacion.generarResumen(notificacionVO);

            //FirmaNotificacionAltia firmaNotificacion=new FirmaNotificacionAltia();
            //res=firmaNotificacion.verificarFirma(notificacionVO,firma);

            byte[] documento = null;

            documento = xml.getBytes();

            File f = File.createTempFile("prueba",".temp");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(documento);
            fos.flush();
            fos.close();

            res = verificarFirma(f,firma);

            f.delete();


	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}

    }

    public boolean verificarFirma(File documento,String firma){
       ValidacionCertificado validar = new ValidacionCertificado();
        return validar.verificarFirma(documento, firma);
    }


     public boolean guardarEstadoNotificacionEnviada (NotificacionVO notificacionVO,String[] params)  throws TechnicalException
     {
          boolean res=false;


	try{

            //Otengo la notificacion
            NotificacionManager notificacionManager=NotificacionManager.getInstance();

            res=notificacionManager.guardarEstadoNotificacionEnviada(notificacionVO,params);


	/*}catch (TechnicalException te) {
            m_Log.error("JDBC Technical problem " + te.getMessage());*/
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}

     }


     
    public boolean actualizarNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException{
        boolean res=false;        
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        
	try{
            // SE OBTIENE CONEXIÓN A LA BBDD
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            boolean actualizacionAutorizados = false;            
            boolean actualizacionAdjuntosTramitacion = false;
            boolean actualizado = false;
            
            int codigoNotificacion = notificacionVO.getCodigoNotificacion();
            log.debug("******************* scodigoNotificacion : " + codigoNotificacion);
            
            actualizado = NotificacionDAO.getInstance().updateNotificacion(notificacionVO,con);
            if(!actualizado){
                log.error("PluginNotificacionAltia error al actualizar la notificación");
                res = false;                
            }else{
                
                ArrayList<AdjuntoNotificacionVO> arrayDocumentos= new ArrayList<AdjuntoNotificacionVO>();

                arrayDocumentos=notificacionVO.getAdjuntos();                                        

                
                /*** DOCUMENTOS DE TRAMITACIÓN  ***/                        
                AdjuntoNotificacionDAO adjuntoNotifDAO = AdjuntoNotificacionDAO.getInstance();                 
                actualizacionAdjuntosTramitacion = adjuntoNotifDAO.actualizarAdjunto(notificacionVO.getCodigoNotificacion(),notificacionVO.getNumExpediente(),notificacionVO.getCodigoMunicipio(),notificacionVO.getCodigoTramite(),notificacionVO.getOcurrenciaTramite(),notificacionVO.getAdjuntos(),con);                                            
                                                
                
                /****** AUTORIZADOS *******/
                ArrayList<AutorizadoNotificacionVO> arrayAutorizados = new ArrayList<AutorizadoNotificacionVO>();
                arrayAutorizados=notificacionVO.getAutorizados();
                                                                        
                AutorizadoNotificacionDAO autorizadoDAO = AutorizadoNotificacionDAO.getInstance();                        
                actualizacionAutorizados = autorizadoDAO.actualizarAutorizado(notificacionVO.getCodigoNotificacion(),notificacionVO.getCodigoMunicipio(),notificacionVO.getNumExpediente(),arrayAutorizados,con);
                
               
            }// else
            
            
            if(actualizado && actualizacionAutorizados && actualizacionAdjuntosTramitacion){
                adapt.finTransaccion(con);
                res = true;
            }
            else{
                adapt.rollBack(con);
                res = false;
            }

	
	}catch (Exception e) {
            res = false;
            e.printStackTrace();
            try{
                adapt.rollBack(con);
                
            }catch(Exception f){
                e.printStackTrace();
            }
            
	}finally{
            
            try{
                // Se cierra la conexión a la BBDD
                if(con!=null) con.close();                
            }catch(Exception e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }            
            return res;
	}
        
    }
    
    
    /**
     * Crea una notificación por defecto para una determinada ocurrencia de un trámite de un determinado expediente
     * @param notificacion: Objeto de tipo NotificacionVO
     * @param params: Parámetros de conexión a la BBDD
     */
    public void crearNotificacionDefecto(NotificacionVO notificacion,String[] params){
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            NotificacionDAO.getInstance().crearNotificacionPorDefecto(notificacion, con);
            
            adapt.finTransaccion(con);
            
        }catch(Exception e){
            e.printStackTrace();
            try{
                adapt.rollBack(con);
            }catch(Exception f){
                log.error("Error al realizar un rollback: " + e.getMessage());
            }
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }        
    }



    public es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO consultarNotificacionSNE(String codOrganizacion,String numeroRegistroRT){

        ServicioAltaNotificacionService servicio;
        ServicioAltaNotificacion port=null;
        es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO notif = null;
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
            String PLUGIN = bundle.getString(codOrganizacion + "/plugin");
            String URL_WS = bundle.getString(codOrganizacion + "/Notificacion/" + PLUGIN + "/servicioWebNotificacion");

            URL urlEndPoint = new URL(URL_WS);
            servicio = new ServicioAltaNotificacionServiceLocator();
            port = servicio.getServicioAltaNotificacion(urlEndPoint);

            notif = port.consultarNotificacion(numeroRegistroRT);

        }
        catch (MalformedURLException error) {
            // ERROR AL GENERAR LA SALIDA TELEMATICA
            error.printStackTrace();
        }
        catch (Exception error){
            // ERROR AL GENERAR LA SALIDA TELEMATICA
            error.printStackTrace();
        }

        return notif;

    }






}
