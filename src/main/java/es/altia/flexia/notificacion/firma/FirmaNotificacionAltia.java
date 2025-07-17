/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.notificacion.firma;





import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.notificacion.persistence.AutorizadoNotificacionDAO;
import es.altia.flexia.notificacion.persistence.NotificacionDAO;
import es.altia.flexia.notificacion.registro.RegistroTelematico;
import es.altia.flexia.notificacion.registro.RegistroFlexia;
import java.util.ResourceBundle;
import java.net.MalformedURLException;
import java.net.URL;
import es.altia.flexia.notificacion.persistence.NotificacionIndividualDAO;
import es.altia.notificacion.webservice.servicioaltanotificacion.client.ServicioAltaNotificacion;
import es.altia.notificacion.webservice.servicioaltanotificacion.client.ServicioAltaNotificacionService;
import es.altia.notificacion.webservice.servicioaltanotificacion.client.ServicioAltaNotificacionServiceLocator;
import es.altia.flexia.notificacion.vo.*;

import es.altia.merlin.licitacion.exceptions.SleException;
import es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.ResultadoVO;
import es.altia.util.commons.MimeTypes;
import java.sql.Connection;
import java.util.ArrayList;
import org.apache.axis.client.Stub;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author marcos.rama
 */

public class FirmaNotificacionAltia {

    protected static Log m_Log =
            LogFactory.getLog(FirmaNotificacionAltia.class.getName());

    /**
    public boolean crearNotificacion(NotificacionVO notificacion,String strUrlEndPoint,String[] params) throws TechnicalException {

        boolean retorno=false;

        es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO
			notificacionAltia = new es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO();

        notificacionAltia.setAplicacion(notificacion.getAplicacion()); 														//Aplicacion que genera la TODO: Dejarlo Fijo ¿?
        notificacionAltia.setProcedimiento(null);


        notificacionAltia.setAsunto(notificacion.getActoNotificado());
        
        String TEXTO_NOTIFICACION = notificacion.getTextoNotificacion();
        TEXTO_NOTIFICACION = StringEscapeUtils.unescapeJavaScript(notificacion.getTextoNotificacion());
        TEXTO_NOTIFICACION = TEXTO_NOTIFICACION.replaceAll("\r\n"," ");
        //notificacionAltia.setTextoCastellano(notificacion.getTextoNotificacion());
        notificacionAltia.setTextoCastellano(TEXTO_NOTIFICACION);
        notificacionAltia.setFirma(notificacion.getFirma());

        notificacionAltia.setDepartamentoInicia(notificacion.getDepartamento());			//Departamento que inicia el tramite
        notificacionAltia.setDepartamentoTramita(notificacion.getDepartamento());			//Departamento que lleva el tramite
        notificacionAltia.setNombreExpediente(notificacion.getNombreExpediente());					//Nombre expdiente
        notificacionAltia.setNumeroExpediente(notificacion.getNumExpediente());					//Numero expediente
        notificacionAltia.setCifEmpresa(null);								//Empresa a la que se envia la notificacion
        notificacionAltia.setPuestoAdministrativo(null);			//Puesto de la persona que envia la notificacion
        //notificacionAltia.setNombreApoderado(datosNotificacion.getNombreApoderado());
        //notificacionAltia.setNombreApoderado(null);
        notificacionAltia.setNombreEmpresa(null);
        



        notificacionAltia.setTipoNotificacion(Integer.parseInt(notificacion.getCodigoTipoNotificacion()));

        notificacionAltia.setFechaCaducidad(null);

        //Datos que metemos a mano
        notificacionAltia.setClaseProcedimiento(notificacion.getClaseProcedimiento());





        //Cargamos las personas autorizadas a ver la notificacion
         
        if (notificacion.getAutorizados()!= null && notificacion.getAutorizados().size() > 0)
        {

                int j=0;
                for (int i=0;i<notificacion.getAutorizados().size();i++)
                {
                        AutorizadoNotificacionVO persona = (AutorizadoNotificacionVO) notificacion.getAutorizados().get(i);
                        if(persona.getSeleccionado().equals("SI"))
                        {
                            j++;
                        }
                }
                  es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO []
                        listaAutorizados = new
                        es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO
                                [j];

                j=0;
                for (int i=0;i<notificacion.getAutorizados().size();i++)
                {
                        AutorizadoNotificacionVO persona = (AutorizadoNotificacionVO) notificacion.getAutorizados().get(i);

                        if(persona.getSeleccionado().equals("SI"))
                        {
                            es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO
                                autorizado = new  es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO();
                            autorizado.setMail(persona.getEmail());
                            autorizado.setNif(persona.getNif());			//NIF o documento identificativo
                            autorizado.setNombre(persona.getNombre());
                            if(j==0)
                            {
                                notificacionAltia.setNifApoderado(persona.getNif());
                                notificacionAltia.setNombreApoderado(persona.getNombre());
                            }
                            listaAutorizados[j] = autorizado;
                            j++;
                        }
                        
                }


                notificacionAltia.setAutorizados(listaAutorizados);
        }
        else
        {
                //ERROR: NO SE HAN INDICADO AUTORIZADOS

        }

        
       //Cargar Documentos Adjuntos
       ResourceBundle bundle = ResourceBundle.getBundle("common");

       String tipoMime,extension = null;

       if (bundle.getString("editorPlantillas").equals("OOFFICE")){
            tipoMime  = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
            extension =  ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE;
       }
       else{
           tipoMime  = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;
           extension =  ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD;
       }


       
       int contadorAdjuntos = 0;
       int indice = 0;
       for (int i = 0; notificacion.getAdjuntos()!=null && i<notificacion.getAdjuntos().size();i++){
            AdjuntoNotificacionVO doc = (AdjuntoNotificacionVO) notificacion.getAdjuntos().get(i);
            if(doc.getSeleccionado().equals("SI")){
                contadorAdjuntos++;
            }
       }

       ArrayList<AdjuntoNotificacionVO> adjuntosExternos = notificacion.getAdjuntosExternos();
       if(adjuntosExternos!=null && adjuntosExternos.size()>0){
            contadorAdjuntos = contadorAdjuntos + adjuntosExternos.size();
       }

       es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO []
                        listaAdjuntos = new es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO[contadorAdjuntos];


        if (notificacion.getAdjuntos() != null && notificacion.getAdjuntos().size() > 0)
        {

                for (int i = 0; i<notificacion.getAdjuntos().size();i++)
                {
                     AdjuntoNotificacionVO doc = (AdjuntoNotificacionVO) notificacion.getAdjuntos().get(i);

                        if(doc.getSeleccionado().equals("SI"))
                        {
                            contadorAdjuntos++;
                        }
                }


           

                
                for (int i = 0; i<notificacion.getAdjuntos().size();i++)
                {
                        AdjuntoNotificacionVO doc = (AdjuntoNotificacionVO) notificacion.getAdjuntos().get(i);
                        if(doc.getSeleccionado().equals("SI"))
                        {
                            es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO
                                adjunto = new es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO();
                            adjunto.setDocumento(doc.getContenido());
                            if(doc.getNombre().length()>50)adjunto.setNombre(doc.getNombre().substring(0, 49));
                            else adjunto.setNombre(doc.getNombre());
                            adjunto.setFirma(doc.getFirma());
                         adjunto.setTipoDocumento(tipoMime);
                         

                        listaAdjuntos[indice] = adjunto;
                        indice++;
                        }
                }
                
        }


        m_Log.debug(" listaAdjuntos.length: " + listaAdjuntos.length + "<> indice: " + indice);
        // DOCUMENTOS ANEXOS EXTERNOS 
        for(int i=0;adjuntosExternos!=null && i<adjuntosExternos.size();i++){
            AdjuntoNotificacionVO doc = (AdjuntoNotificacionVO) adjuntosExternos.get(i);
            
            
                es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO
                    adjunto = new es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO();
                adjunto.setDocumento(doc.getContenido());
                if(doc.getNombre().length()>50)adjunto.setNombre(doc.getNombre().substring(0, 49));
                else adjunto.setNombre(doc.getNombre());
                adjunto.setFirma(doc.getFirma());
                adjunto.setTipoDocumento(doc.getContentType());

                listaAdjuntos[indice] = adjunto;
                indice++;
            
        }

       if(listaAdjuntos!=null && listaAdjuntos.length>0)
            notificacionAltia.setAnexos(listaAdjuntos);


        
        //Validamos la firma del Notificacion y lo firmamos
         
        String xml = NotificacionManager.getInstance().getXMLFirmaNotificacion(notificacion.getCodigoNotificacion(), params);
        
        //notificacionAltia.setXmlNotif(generarResumen(notificacion));
        notificacionAltia.setXmlNotif(xml);
        
        //TODO: Codigo de Validacion de firma ¿Duda que ocurre con las notificaciones no automaticas?
        notificacionAltia.setOscp(null);
        notificacionAltia.setTimeStamp(null);
        notificacionAltia.setFirma(notificacion.getFirma());

        GeneralValueObject gVO  = NotificacionManager.getInstance().getUnidadTramitadoraTramite(Integer.toString(notificacion.getCodigoTramite()),Integer.toString(notificacion.getOcurrenciaTramite()),notificacion.getNumExpediente(),params);
        String nombreUor = (String)gVO.getAtributo("UOR_NOM");
        String codUor = (String)gVO.getAtributo("UOR_COD");

        m_Log.debug("nombreUor: " + nombreUor + ", codUor: " + codUor);

        ApunteRegistroVO apunte = new ApunteRegistroVO();
        boolean continuar = true;

        ResourceBundle configNotificacion = ResourceBundle.getBundle("notificaciones");
        String _codProcedimiento = configNotificacion.getString("COD_PROCEDIMIENTO");
        try{

            apunte.setCodUnidadOrigen(codUor);
            apunte.setCodOrganoOrigen(codUor);
            apunte.setEntidad(nombreUor);


            ArrayList<AutorizadoNotificacionVO>  autorizados = AutorizadoNotificacionManager.getInstance().getInteresadosExpediente(notificacion.getNumExpediente(),notificacion.getCodigoNotificacion(),params);
            ArrayList<TerceroRegistroVO> auxiliar = new ArrayList<TerceroRegistroVO>();
            for(int i=0;autorizados!=null && i<autorizados.size();i++){
                AutorizadoNotificacionVO autorizado = autorizados.get(i);

                TerceroRegistroVO ter = new TerceroRegistroVO();
                ter.setApellido1(autorizado.getApellido1());
                ter.setApellido2(autorizado.getApellido2());
                ter.setNombre(autorizado.getNombre());

                ter.setNifTercero(autorizado.getNif());
                ter.setTipoDocumento(autorizado.getTipoDocumento());
                ter.setEmail(autorizado.getEmail());

                DireccionRegistroVO direccion = new DireccionRegistroVO();
                direccion.setCodigoPais(autorizado.getCodPais());
                direccion.setCodigoPostal(autorizado.getCodPostal());
                direccion.setCodigoProvincia(autorizado.getCodProvincia());
                direccion.setDescripcionMunicipio(autorizado.getDescProvincia());
                direccion.setDireccion(autorizado.getDireccion());
                direccion.setNombreVia(autorizado.getDescVia());
                direccion.setTelfRemitente(autorizado.getTelefono());
                ter.setDireccion(direccion);
                auxiliar.add(ter);
            }            
            apunte.setTerceros(auxiliar);

            apunte.setCodProcedimiento(_codProcedimiento);
            apunte.setNumExpediente(notificacion.getNumExpediente());
            apunte.setAsunto(notificacion.getActoNotificado());
            apunte.setTexto(notificacion.getTextoNotificacion());

            apunte = RegistroTelematico.getInstance().crearSalida(apunte, extension, notificacion.getFirma());
            if(apunte!=null && apunte.getNumeroRegistroRT()!=null && apunte.getFechaRT()!=null){
                // Se ha dado de alta al entrada en el RT => Se procede a dar de alta la notificación
                notificacionAltia.setNumeroRegistro(apunte.getNumeroRegistroRT());                
                String[] fechaRT = apunte.getFechaRT().split(" ");
                notificacionAltia.setFechaRegistro(fechaRT[0]);
                                
                String hora = fechaRT[1];
                int ultimoIndice = hora.lastIndexOf(":");                
                notificacionAltia.setHoraRegistro(hora.substring(0,ultimoIndice));
                notificacion.setNumeroRegistroTelematico(apunte.getNumeroRegistroRT());
                
                //Llamamos al servicio Web para crear la comunicacion
                ServicioAltaNotificacionService servicio;
                ServicioAltaNotificacion port=null;
                try
                {
                        URL urlEndPoint = new URL(strUrlEndPoint);
                        servicio = new ServicioAltaNotificacionServiceLocator();
                        port = servicio.getServicioAltaNotificacion(urlEndPoint);
                        m_Log.debug(" Se procede a insertar la notificación tras dar de alta la anotación de salida en el Registro Telemático");
                        ((Stub)port).setTimeout(180000); // 3 minutos de timeout
                        
                        ResultadoVO resultado = port.insertarNuevaNotificacion(notificacionAltia);


                        if (resultado.isCorrecta())
                        {
                                retorno=true;
                        }
                        else
                        {
                               retorno=false;
                               m_Log.error("===Erro Notificacion--> "+resultado.getCausaError());
                        }
                }
                catch (MalformedURLException error) {
                        // ERROR AL GENERAR LA SALIDA TELEMATICA

                            error.printStackTrace();
                            retorno=false;

                }

                catch (Exception error)
                {
                        // ERROR AL GENERAR LA SALIDA TELEMATICA
                        error.printStackTrace();
                         retorno=false;

                }
              
            }

        }catch(SleException e){
            m_Log.error("Error al generar entrada en el RT para la notificación: " + e.getMessage());
        }

        return retorno;
    }
*/
    
    
    
    
    
    public boolean crearNotificacion(NotificacionVO notificacion,String strUrlEndPoint,Connection con,String[] params) throws TechnicalException {

        boolean retorno=false;

        es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO
			notificacionAltia = new es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO();

        notificacionAltia.setAplicacion(notificacion.getAplicacion()); 														//Aplicacion que genera la TODO: Dejarlo Fijo ¿?
        notificacionAltia.setProcedimiento(null);


        notificacionAltia.setAsunto(notificacion.getActoNotificado());
        
        String TEXTO_NOTIFICACION = notificacion.getTextoNotificacion();
        TEXTO_NOTIFICACION = StringEscapeUtils.unescapeJavaScript(notificacion.getTextoNotificacion());
        TEXTO_NOTIFICACION = TEXTO_NOTIFICACION.replaceAll("\r\n"," ");
        //notificacionAltia.setTextoCastellano(notificacion.getTextoNotificacion());
        notificacionAltia.setTextoCastellano(TEXTO_NOTIFICACION);
        notificacionAltia.setFirma(notificacion.getFirma());

        notificacionAltia.setDepartamentoInicia(notificacion.getDepartamento());			//Departamento que inicia el tramite
        notificacionAltia.setDepartamentoTramita(notificacion.getDepartamento());			//Departamento que lleva el tramite
        notificacionAltia.setNombreExpediente(notificacion.getNombreExpediente());					//Nombre expdiente
        notificacionAltia.setNumeroExpediente(notificacion.getNumExpediente());					//Numero expediente
        notificacionAltia.setCifEmpresa(null);								//Empresa a la que se envia la notificacion
        notificacionAltia.setPuestoAdministrativo(null);			//Puesto de la persona que envia la notificacion
        //notificacionAltia.setNombreApoderado(datosNotificacion.getNombreApoderado());
        //notificacionAltia.setNombreApoderado(null);
        notificacionAltia.setNombreEmpresa(null);
        



        notificacionAltia.setTipoNotificacion(Integer.parseInt(notificacion.getCodigoTipoNotificacion()));

        notificacionAltia.setFechaCaducidad(null);

        //Datos que metemos a mano
        notificacionAltia.setClaseProcedimiento(notificacion.getClaseProcedimiento());



		/*
		Modificamos para crear una notificación para cada Interesado
		No realizamos la carga en este punto, solo comprobamos que tiene autorizados.
		*/
		
		if (!(notificacion.getAutorizados()!= null && notificacion.getAutorizados().size() > 0)){
			//ERROR: NO SE HAN INDICADO AUTORIZADOS
		}
		/*
         * Cargamos las personas autorizadas a ver la notificacion
         */
        if (notificacion.getAutorizados()!= null && notificacion.getAutorizados().size() > 0)
        {

                int j=0;
                for (int i=0;i<notificacion.getAutorizados().size();i++)
                {
                        AutorizadoNotificacionVO persona = (AutorizadoNotificacionVO) notificacion.getAutorizados().get(i);
                        if(persona.getSeleccionado().equals("SI"))
                        {
                            j++;
                        }
                }
                  es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO []
                        listaAutorizados = new
                        es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO
                                [j];

                j=0;
                for (int i=0;i<notificacion.getAutorizados().size();i++)
                {
                        AutorizadoNotificacionVO persona = (AutorizadoNotificacionVO) notificacion.getAutorizados().get(i);

                        if(persona.getSeleccionado().equals("SI"))
                        {
                            es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO
                                autorizado = new  es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO();
                            autorizado.setMail(persona.getEmail());
                            autorizado.setNif(persona.getNif());			//NIF o documento identificativo
                            autorizado.setNombre(persona.getNombre());
                            if(j==0)
                            {
                                notificacionAltia.setNifApoderado(persona.getNif());
                                notificacionAltia.setNombreApoderado(persona.getNombre());
                            }
                            listaAutorizados[j] = autorizado;
                            j++;
                        }
                        
                }


                notificacionAltia.setAutorizados(listaAutorizados);
        }
        else
        {
                //ERROR: NO SE HAN INDICADO AUTORIZADOS

        }

        /*
         * Cargar Documentos Adjuntos
         */

       ResourceBundle bundle = ResourceBundle.getBundle("common");

       String tipoMime,extension = null;

       if (bundle.getString("editorPlantillas").equals("OOFFICE")){
            tipoMime  = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
            extension =  ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE;
       }
       else{
           tipoMime  = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;
           extension =  ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD;
       }


       /************* NUMERO TOTAL DE ADJUNTOS *******************/
       int contadorAdjuntos = 0;
       int indice = 0;
       for (int i = 0; notificacion.getAdjuntos()!=null && i<notificacion.getAdjuntos().size();i++){
            AdjuntoNotificacionVO doc = (AdjuntoNotificacionVO) notificacion.getAdjuntos().get(i);
            if(doc.getSeleccionado().equals("SI")){
                contadorAdjuntos++;
            }
       }

       ArrayList<AdjuntoNotificacionVO> adjuntosExternos = notificacion.getAdjuntosExternos();
       if(adjuntosExternos!=null && adjuntosExternos.size()>0){
            contadorAdjuntos = contadorAdjuntos + adjuntosExternos.size();
       }

       es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO []
                        listaAdjuntos = new es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO[contadorAdjuntos];


        if (notificacion.getAdjuntos() != null && notificacion.getAdjuntos().size() > 0)
        {

                for (int i = 0; i<notificacion.getAdjuntos().size();i++)
                {
                     AdjuntoNotificacionVO doc = (AdjuntoNotificacionVO) notificacion.getAdjuntos().get(i);

                        if(doc.getSeleccionado().equals("SI"))
                        {
                            contadorAdjuntos++;
                        }
                }

                for (int i = 0; i<notificacion.getAdjuntos().size();i++)
                {
                        AdjuntoNotificacionVO doc = (AdjuntoNotificacionVO) notificacion.getAdjuntos().get(i);
                        if(doc.getSeleccionado().equals("SI"))
                        {
                            es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO
                                adjunto = new es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO();
                            adjunto.setDocumento(doc.getContenido());
                            if(doc.getNombre().length()>50)adjunto.setNombre(doc.getNombre().substring(0, 49));
                            else adjunto.setNombre(doc.getNombre());
                            adjunto.setFirma(doc.getFirma());
                            adjunto.setTipoDocumento(MimeTypes.guessMimeType(tipoMime, doc.getNombre()));
                            listaAdjuntos[indice] = adjunto;
                            indice++;
                        }
                }
        }

        m_Log.debug(" listaAdjuntos.length: " + listaAdjuntos.length + "<> indice: " + indice);
        /********* DOCUMENTOS ANEXOS EXTERNOS ******/
        for(int i=0;adjuntosExternos!=null && i<adjuntosExternos.size();i++){
            AdjuntoNotificacionVO doc = (AdjuntoNotificacionVO) adjuntosExternos.get(i);
                es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO
                    adjunto = new es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.DocumentoVO();
                adjunto.setDocumento(doc.getContenido());
                if(doc.getNombre().length()>50)adjunto.setNombre(doc.getNombre().substring(0, 49));
                else adjunto.setNombre(doc.getNombre());
                adjunto.setFirma(doc.getFirma());
                adjunto.setTipoDocumento(doc.getContentType());

                listaAdjuntos[indice] = adjunto;
                indice++;
            
        }

       if(listaAdjuntos!=null && listaAdjuntos.length>0)
            notificacionAltia.setAnexos(listaAdjuntos);


	   
	   /*
		Modificamos para crear una notificación para cada Interesado
		
	   Recorremos la lista de autorizados y creamos un envío por cada uno (que adjuntaremos como autorizado apoderado)
	   
	   Si no se envían correctamente todas las notificaciones individuales envioNotificaciones = -1
		*/
	   
	   es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO []
                        listApoderado = new
                        es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO
                                [1];
	   
	   boolean envioNotificaciones = true;
	   
	   for (int j=0;j<notificacion.getAutorizados().size();j++)
	   {	
			AutorizadoNotificacionVO persona = (AutorizadoNotificacionVO) notificacion.getAutorizados().get(j);

			if(persona.getSeleccionado().equals("SI"))
			{
				es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO
					apoderado = new  es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.AutorizadoVO();
				apoderado.setMail(persona.getEmail());
				apoderado.setNif(persona.getNif());			//NIF o documento identificativo
				apoderado.setNombre(persona.getNombre());
				notificacionAltia.setNifApoderado(persona.getNif());
				notificacionAltia.setNombreApoderado(persona.getNombre());

				listApoderado[0] = apoderado;

			}

			notificacionAltia.setAutorizados(listApoderado);
		
			
	   
        /*
         * Validamos la firma del Notificacion y lo firmamos
         */


        //String xml = NotificacionManager.getInstance().getXMLFirmaNotificacion(notificacion.getCodigoNotificacion(), params);
       String xml = NotificacionDAO.getInstance().getXMLFirmaNotificacion(notificacion.getCodigoNotificacion(),con);
        
        //notificacionAltia.setXmlNotif(generarResumen(notificacion));
        notificacionAltia.setXmlNotif(xml);
        
        //TODO: Codigo de Validacion de firma ¿Duda que ocurre con las notificaciones no automaticas?
        notificacionAltia.setOscp(null);
        notificacionAltia.setTimeStamp(null);
        notificacionAltia.setFirma(notificacion.getFirma());

        //GeneralValueObject gVO  = NotificacionManager.getInstance().getUnidadTramitadoraTramite(Integer.toString(notificacion.getCodigoTramite()),Integer.toString(notificacion.getOcurrenciaTramite()),notificacion.getNumExpediente(),params);
        GeneralValueObject gVO  = NotificacionDAO.getInstance().getUnidadTramitadoraTramite(Integer.toString(notificacion.getCodigoTramite()),Integer.toString(notificacion.getOcurrenciaTramite()),notificacion.getNumExpediente(),con);
        String nombreUor = (String)gVO.getAtributo("UOR_NOM");
        String codUor = (String)gVO.getAtributo("UOR_COD");

        m_Log.debug("nombreUor: " + nombreUor + ", codUor: " + codUor);

        ApunteRegistroVO apunte = new ApunteRegistroVO();
        boolean continuar = true;

        ResourceBundle configNotificacion = ResourceBundle.getBundle("notificaciones");
        String _codProcedimiento = configNotificacion.getString("COD_PROCEDIMIENTO");
        try{

            apunte.setCodUnidadOrigen(codUor);
            apunte.setCodOrganoOrigen(codUor);
            apunte.setEntidad(nombreUor);


            //ArrayList<AutorizadoNotificacionVO>  autorizados = AutorizadoNotificacionManager.getInstance().getInteresadosExpediente(notificacion.getNumExpediente(),notificacion.getCodigoNotificacion(),params);
            
            ResourceBundle configTechserver = ResourceBundle.getBundle("techserver");
            ArrayList<AutorizadoNotificacionVO>  autorizados = AutorizadoNotificacionDAO.getInstance().getInteresadosExpediente(notificacion.getNumExpediente(),notificacion.getCodigoNotificacion(),configTechserver.getString("CON.gestor"),con);
            ArrayList<TerceroRegistroVO> auxiliar = new ArrayList<TerceroRegistroVO>();
            for(int i=0;autorizados!=null && i<autorizados.size();i++){
                AutorizadoNotificacionVO autorizado = autorizados.get(i);

                TerceroRegistroVO ter = new TerceroRegistroVO();
                ter.setApellido1(autorizado.getApellido1());
                ter.setApellido2(autorizado.getApellido2());
                ter.setNombre(autorizado.getNombre());

                ter.setNifTercero(autorizado.getNif());
                ter.setTipoDocumento(autorizado.getTipoDocumento());
                ter.setEmail(autorizado.getEmail());

                DireccionRegistroVO direccion = new DireccionRegistroVO();
                direccion.setCodigoPais(autorizado.getCodPais());
                direccion.setCodigoPostal(autorizado.getCodPostal());
                direccion.setCodigoProvincia(autorizado.getCodProvincia());
                direccion.setDescripcionMunicipio(autorizado.getDescProvincia());
                direccion.setDireccion(autorizado.getDireccion());
                direccion.setNombreVia(autorizado.getDescVia());
                direccion.setTelfRemitente(autorizado.getTelefono());
                ter.setDireccion(direccion);
                auxiliar.add(ter);
            }            
            apunte.setTerceros(auxiliar);

            apunte.setCodProcedimiento(_codProcedimiento);
            apunte.setNumExpediente(notificacion.getNumExpediente());
            apunte.setAsunto(notificacion.getActoNotificado());
            apunte.setTexto(notificacion.getTextoNotificacion());

            String registro=configNotificacion.getString("REGISTRO_NOTIFICACIONES");
            if ("RT".equals(registro)) apunte = RegistroTelematico.getInstance().crearSalida(apunte, extension, notificacion.getFirma());
            else  if ("flexia".equals(registro)) apunte = RegistroFlexia.getInstance().crearSalida(apunte,  notificacion,con,params);
            else apunte = RegistroFlexia.getInstance().crearSalida(apunte,  notificacion,con,params);
            
           
            if(apunte!=null && apunte.getNumeroRegistroRT()!=null && apunte.getFechaRT()!=null){
                // Se ha dado de alta al entrada en el RT => Se procede a dar de alta la notificación
                notificacionAltia.setNumeroRegistro(apunte.getNumeroRegistroRT());                
                String[] fechaRT = apunte.getFechaRT().split(" ");
                notificacionAltia.setFechaRegistro(fechaRT[0]);
                                
                String hora = fechaRT[1];
                int ultimoIndice = hora.lastIndexOf(":");                
                notificacionAltia.setHoraRegistro(hora.substring(0,ultimoIndice));
                notificacion.setNumeroRegistroTelematico(apunte.getNumeroRegistroRT());

                /*********************/
                /*
                 * Llamamos al servicio Web para crear la comunicacion
                 */

                ServicioAltaNotificacionService servicio;
                ServicioAltaNotificacion port=null;
                try
                {
                        URL urlEndPoint = new URL(strUrlEndPoint);
                        servicio = new ServicioAltaNotificacionServiceLocator();
                        port = servicio.getServicioAltaNotificacion(urlEndPoint);
                        m_Log.debug(" Se procede a insertar la notificación tras dar de alta la anotación de salida en el Registro Telemático");
                        ((Stub)port).setTimeout(180000); // 3 minutos de timeout
                        
                        ResultadoVO resultado = port.insertarNuevaNotificacion(notificacionAltia);

						//Insertamos la notificacion individual en BBDD
						NotificacionIndividualVO notificacionIndividual = new NotificacionIndividualVO();

						notificacionIndividual.setCodigoNotificacion(notificacion.getCodigoNotificacion());
						notificacionIndividual.setAutorizado(persona);
						notificacionIndividual.setNumeroRegistroTelematico(notificacion.getNumeroRegistroTelematico());
				

                        if (resultado.isCorrecta())
                        {
                                retorno=true;
								
								notificacionIndividual.setEstadoNotificacion("E");
                        }
                        else
                        {
                               retorno=false;
							   
							   notificacionIndividual.setEstadoNotificacion("P");
                        } 
						
						
						int codigoNotificacion=NotificacionIndividualDAO.getInstance().insertarNotificacionIndividual(notificacionIndividual,con);
				
						if ( envioNotificaciones == true && retorno == false){
							envioNotificaciones = false;
						}
                }
                catch (MalformedURLException error) {
                        // ERROR AL GENERAR LA SALIDA TELEMATICA

                            error.printStackTrace();
                            retorno=false;

                }

                catch (Exception error)
                {
                        // ERROR AL GENERAR LA SALIDA TELEMATICA
                        error.printStackTrace();
                         retorno=false;

                }

                /**********************/

                /***** ORIGINAL
                notificacionAltia.setNumeroRegistro(Integer.toString(notificacion.getCodigoNotificacion()));   //Le paso el codigo de notificacion porque requiere que el numero de registro sea distinto en cada notificacion
                notificacionAltia.setFechaRegistro("0");
                notificacionAltia.setHoraRegistro("0");
                 */
            }

        }catch(SleException e){
            m_Log.error("Error al generar entrada en el RT para la notificación: " + e.getMessage());
        }

		}
	   
	   // devolvemos envioNotificaciones. será necesario revisar si manejamos ese estado.
        //return retorno;
		
		return envioNotificaciones;
    }
    
    
    


   



    public String generarResumen(NotificacionVO notif) throws TechnicalException {

       return generarXML(notif);
 
    }

private String generarXML(NotificacionVO notif) throws TechnicalException {

       StringBuffer xml = new StringBuffer();
        xml.append("<cuerponotificacion>");
        xml.append("<codaplicacion>");xml.append(notif.getAplicacion());xml.append("</codaplicacion>");
        xml.append("<cifempresa>");xml.append("</cifempresa>");
        xml.append("<empresa>");xml.append("");xml.append("</empresa>");
        xml.append("<interesado>");
                xml.append("<nif>");xml.append("</nif>");
                xml.append("<nombre>");xml.append("</nombre>");
        xml.append("</interesado>");

        xml.append("<autorizados>");

        ArrayList<AutorizadoNotificacionVO> arrayAutorizados= new ArrayList<AutorizadoNotificacionVO>();

        arrayAutorizados=notif.getAutorizados();

       //Inserto Autorizados

       for(int i=0;i<arrayAutorizados.size();i++)
       {
            AutorizadoNotificacionVO autorizadoVO=new AutorizadoNotificacionVO() ;
            autorizadoVO=arrayAutorizados.get(i);
            if(autorizadoVO.getSeleccionado().equals("SI"))
            {
               xml.append("<autorizado>");
                                xml.append("<nifautorizado>");xml.append(autorizadoVO.getNif());xml.append("</nifautorizado>");
                                xml.append("<emailautorizado>");xml.append(autorizadoVO.getEmail());xml.append("</emailautorizado>");
               xml.append("</autorizado>");
            }
        }


        xml.append("</autorizados>");

        xml.append("<puestoidadm>");xml.append("</puestoidadm>");
        /*
        xml.append("<departamentoinicia>");xml.append("</departamentoinicia>");
        xml.append("<departamentotramita>");xml.append("</departamentotramita>"); */
        xml.append("<departamentoinicia>");notif.getCodDepartamento();xml.append("</departamentoinicia>");
        xml.append("<departamentotramita>");notif.getCodDepartamento();xml.append("</departamentotramita>");
        xml.append("<numexp>");xml.append(notif.getNumExpediente());xml.append("</numexp>");
        xml.append("<denomexp>");xml.append(notif.getNombreExpediente());xml.append("</denomexp>");
        xml.append("<tipoprocedimiento>");xml.append("</tipoprocedimiento>");
        xml.append("<actonotificado>");xml.append(notif.getActoNotificado());xml.append("</actonotificado>");
        
        String TEXTO_NOTIFICACION = notif.getTextoNotificacion();
        TEXTO_NOTIFICACION = StringEscapeUtils.unescapeJavaScript(TEXTO_NOTIFICACION);
        TEXTO_NOTIFICACION = TEXTO_NOTIFICACION.replaceAll("\r\n"," ");
                        
        
        xml.append("<textonotificacionCAS><![CDATA[");xml.append(TEXTO_NOTIFICACION);xml.append("]]></textonotificacionCAS>");
        xml.append("<textonotificacionGAL><![CDATA[");xml.append(TEXTO_NOTIFICACION);xml.append("]]></textonotificacionGAL>");
        
        /** original        
        xml.append("<textonotificacionCAS><![CDATA[");xml.append(notif.getTextoNotificacion());xml.append("]]</textonotificacionCAS>");
        xml.append("<textonotificacionGAL><![CDATA[");xml.append(notif.getTextoNotificacion());xml.append("]]</textonotificacionGAL>");
        */ 


        ArrayList<AdjuntoNotificacionVO> arrayDocumentos= new ArrayList<AdjuntoNotificacionVO>();

        arrayDocumentos=notif.getAdjuntos();

        if (arrayDocumentos != null && arrayDocumentos.size() > 0)
        {
            //Inserto documentos
            xml.append("<adjuntos>");
            for(int i=0;i<arrayDocumentos.size();i++)
            {
                AdjuntoNotificacionVO adjuntoVO=new AdjuntoNotificacionVO() ;
                adjuntoVO=arrayDocumentos.get(i);
                if(adjuntoVO.getSeleccionado().equals("SI"))
                {
                    xml.append("<adjunto>");
                                xml.append("<nombreadj>");xml.append(adjuntoVO.getNombre());xml.append("</nombreadj>");
                                xml.append("<firmaadj>");xml.append(adjuntoVO.getFirma());xml.append("</firmaadj>");
                                xml.append("<respuestaAdjOCSP>");xml.append("</respuestaAdjOCSP>");
                                xml.append("<respuestaAdjTS>");xml.append("</respuestaAdjTS>");
                    xml.append("</adjunto>");
                }
            }
            /****** documentos externos firmados *******/
            ArrayList<AdjuntoNotificacionVO> adjuntos = notif.getAdjuntosExternos();
            for(int i=0;adjuntos!=null && i<adjuntos.size();i++){
                String firma = adjuntos.get(i).getFirma();
                String nombre = adjuntos.get(i).getNombre();
                if(firma!=null && !"".equals(firma)){
                    // Sólo se añade el documento que esté firmado
                    xml.append("<adjunto>");
                                xml.append("<nombreadj>");xml.append(nombre);xml.append("</nombreadj>");
                                xml.append("<firmaadj>");xml.append(firma);xml.append("</firmaadj>");
                                xml.append("<respuestaAdjOCSP>");xml.append("</respuestaAdjOCSP>");
                                xml.append("<respuestaAdjTS>");xml.append("</respuestaAdjTS>");
                    xml.append("</adjunto>");
                }
            }
            /****** documentos externos firmados *******/

            xml.append("</adjuntos>");
        }

        xml.append("</cuerponotificacion>");
        return xml.toString();
    }




}









