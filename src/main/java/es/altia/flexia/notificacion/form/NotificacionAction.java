package es.altia.flexia.notificacion.form;

import es.altia.agora.business.escritorio.UsuarioValueObject;

import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import es.altia.flexia.notificacion.persistence.NotificacionManager;
import es.altia.flexia.notificacion.plugin.*;
import es.altia.flexia.notificacion.vo.*;
import es.altia.util.notificaciones.*;

import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionManager;
import es.altia.flexia.notificacion.persistence.NotificacionDAO;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmasAfirma;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmasAsf;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.util.struts.StrutsFileValidation;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import javax.servlet.ServletOutputStream;
import com.itextpdf.text.pdf.codec.Base64;
import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionDAO;
import es.altia.util.StringUtils;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.sql.Connection;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.upload.FormFile;


public final class NotificacionAction extends ActionSession {

private static final String MESSAGE_SUCCESS = "Sge.FirmaDocumentoTramitacionForm.FirmaGuardadaCorrectamente";

      public ActionForward performSession(	ActionMapping mapping,
                  ActionForm form,
                  HttpServletRequest request,
                  HttpServletResponse response) throws IOException, ServletException {


    m_Log.info("================= NotificacionAction ======================>");
    
    // Validaremos los parametros del request especificados
    HttpSession session = request.getSession();
    String opcion ="";
    ActionErrors errors;

    opcion = request.getParameter("opcion");
    m_Log.info("Opcion : " + opcion);


    NotificacionForm notificacionForm = null;

    if (form == null) {
        m_Log.debug("Rellenamos el form de Tramitacion");
        form = new NotificacionForm();
        if ("request".equals(mapping.getScope()))
            request.setAttribute(mapping.getAttribute(), form);
        else
            session.setAttribute(mapping.getAttribute(), form);
    }

    notificacionForm = (NotificacionForm)form;
    // Si usuario en sesion es nulo --> error.
    if ((session.getAttribute("usuario") != null)) {
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
      String[] params = usuario.getParamsCon();

      Config m_Conf = ConfigServiceHelper.getConfig("common");
      String JSP_Tramitacion;

      if (opcion.equals("mostrarPantallaNotificacion")) {
          String url="";
          ArrayList<AutorizadoNotificacionVO> arrayAutorizados=new ArrayList<AutorizadoNotificacionVO>();
          ArrayList<AdjuntoNotificacionVO> arrayAdjuntos=new ArrayList<AdjuntoNotificacionVO>();

          try{

                String codMun            = request.getParameter("codMunicipio");
                String codProcedimiento  = request.getParameter("codProcedimiento");
                String ejercicio         = request.getParameter("ejercicio");
                String numero            = request.getParameter("numero");
                String codTramite        = request.getParameter("codTramite");
                String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
                String procedimiento     = request.getParameter("procedimiento");
                String recargar          = request.getParameter("recargar");
                
                PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(usuario.getOrgCod()));
                NotificacionVO notificacionVO=new NotificacionVO();
                notificacionVO.setCodigoMunicipio(Integer.parseInt(codMun));
                notificacionVO.setCodigoProcedimiento(codProcedimiento);
                notificacionVO.setEjercicio(Integer.parseInt(ejercicio));
                notificacionVO.setNumExpediente(numero);
                notificacionVO.setCodigoTramite(Integer.parseInt(codTramite));
                notificacionVO.setOcurrenciaTramite(Integer.parseInt(ocurrenciaTramite));
                notificacionVO.setNombreExpediente(procedimiento);

                String codigoTipoNotificacion=NotificacionManager.getInstance().getTipoNotificacion(notificacionVO, params);
                String tipoNotificacion="";

                ArrayList<TipoNotificacionValueObject> arrayTipoNotificaciones=new ArrayList<TipoNotificacionValueObject>();
                arrayTipoNotificaciones=NotificacionesUtil.getTiposNotificacion();

                if((!"".equals(codigoTipoNotificacion))&&(codigoTipoNotificacion!=null)&&(arrayTipoNotificaciones.size()>0))
                {
                    for(int i=0;i<arrayTipoNotificaciones.size();i++)
                    {
                        TipoNotificacionValueObject tipoNotifVO=new TipoNotificacionValueObject();
                        tipoNotifVO=arrayTipoNotificaciones.get(i);
                     
                        if((tipoNotifVO.getCodigo()).equals(codigoTipoNotificacion))
                        {
                            tipoNotificacion=tipoNotifVO.getDescripcion();
                           
                        }
                    }
                }


                m_Log.debug("===notificacionVO");
                m_Log.debug("===GetNotificacion");
                
                // Se crea la notificación por defecto si es que no existe
                pluginNotificacion.crearNotificacionDefecto(notificacionVO,params);
                
                try{
                ResourceBundle m_ConfNotificaciones = ResourceBundle.getBundle("notificaciones");
               
                int codOrganizacion = usuario.getOrgCod();
                String caducidadDefecto = m_ConfNotificaciones.getString(codOrganizacion + "/DIAS_CADUCIDAD_NOTIFICACIONES_DEF" );                
                notificacionVO.setCaducidadNotificacion(Integer.parseInt(caducidadDefecto));
                
                }catch (Exception e)
                {
                     notificacionVO.setCaducidadNotificacion(0);
                }
                
                notificacionVO = pluginNotificacion.getNotificacion(notificacionVO, params);                              
                arrayAutorizados = notificacionVO.getAutorizados();
                arrayAdjuntos    = notificacionVO.getAdjuntos();
                
                ArrayList<AdjuntoNotificacionVO> adjuntosExternos = notificacionVO.getAdjuntosExternos();                                   
                
                url=pluginNotificacion.getUrlPantallaDatosNotificacion();

                notificacionForm.setCodigoMunicipio(Integer.parseInt(codMun));
                notificacionForm.setCodigoProcedimiento(codProcedimiento);
                notificacionForm.setEjercicio(Integer.parseInt(ejercicio));
                notificacionForm.setCodigoTramite(Integer.parseInt(codTramite));
                notificacionForm.setOcurrenciaTramite(Integer.parseInt(ocurrenciaTramite));



                //Nos hace falta en la jsp
                notificacionForm.setNombreExpediente(procedimiento);
                notificacionForm.setNumExpediente(numero);
                notificacionForm.setActoNotificado(notificacionVO.getActoNotificado());
                notificacionForm.setCaducidadNotificacion(notificacionVO.getCaducidadNotificacion());
                notificacionForm.setTextoNotificacion(notificacionVO.getTextoNotificacion());
                notificacionForm.setTipoNotificacion(tipoNotificacion);
                notificacionForm.setAdjuntos(notificacionVO.getAdjuntos());
                notificacionForm.setAutorizados(notificacionVO.getAutorizados());
                notificacionForm.setEstadoNotificacion(notificacionVO.getEstadoNotificacion());
                notificacionForm.setAdjuntosExternos(adjuntosExternos);
                notificacionForm.setDescripcionProcedimiento(procedimiento);
                notificacionForm.setCodNotificacion(notificacionVO.getCodigoNotificacion());

                DefinicionTramitesValueObject defVO = DefinicionTramitesManager.getInstance().getInfoNotificacionElectronicaTramite(Integer.parseInt(codTramite),codProcedimiento,codMun,params);
                notificacionForm.setAdmiteFirmaCertificadoOrganismo(false);
                if(defVO.getCertificadoOrganismoFirmaNotificacion()){
                    ResourceBundle config = ResourceBundle.getBundle("Portafirmas");
                    String pluginPortafirmas = config.getString(codMun + "/PluginPortafirmas");
                    if(pluginPortafirmas!=null && !"FLEXIA".equalsIgnoreCase(pluginPortafirmas)){                    
                        // Sólo se puede utilizar la firma con certificado de organismo si hay un plugin distinto
                        // al de Flexia en el portafirmas, actualmente sólo es válido con ASF o con AFIRMA
                        notificacionForm.setAdmiteFirmaCertificadoOrganismo(true);
                    }   
                }
                
                session.setAttribute("notificacionForm", notificacionForm);
                session.setAttribute("notificacionVO", notificacionVO);
                                              
                //response.sendRedirect(request.getContextPath() + url);
                return new ActionForward(url);

                }catch (Exception cnfe) {
                    m_Log.error("Al crear la factoria");
                    cnfe.printStackTrace();
                }

         
      }
      else if (opcion.equals("guardar")) {
            m_Log.debug("===NotificacionAction--> Guardar");

          if (session != null) {

                notificacionForm = (NotificacionForm) session.getAttribute("notificacionForm");
          }

           session.removeAttribute("exitoGrabarNotificacion");
           String listaAutorizadosSeleccionados =	request.getParameter("lAutorizadosSeleccionados");           
           String listaAdjuntosSeleccionados = request.getParameter("lAdjuntosSeleccionados");

           String actoNotificado = request.getParameter("actoNotificado");
           String textoNotificacion = request.getParameter("textoNotificacion");
           String caducidadNotificacion = request.getParameter("caducidadNotificacion");           
           String codNotificacion = request.getParameter("codNotificacion");
                      
           m_Log.debug("NotificacionAction.guardar() codNotificacion: " + codNotificacion + " =======> ");
           
           try{

                PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(usuario.getOrgCod()));
                NotificacionVO notificacionVO=new NotificacionVO();

                StringTokenizer autorizadosSel = null;
                StringTokenizer adjuntosSel = null;

                autorizadosSel = new StringTokenizer(listaAutorizadosSeleccionados,"§¥",false);
                adjuntosSel = new StringTokenizer(listaAdjuntosSeleccionados,"§¥",false);

                ArrayList<AutorizadoNotificacionVO> arrayAutorizados=new ArrayList<AutorizadoNotificacionVO>();
                arrayAutorizados=notificacionForm.getAutorizados();

                ArrayList<AdjuntoNotificacionVO> arrayAdjuntos=new ArrayList<AdjuntoNotificacionVO>();
                arrayAdjuntos=notificacionForm.getAdjuntos();
            
                  int i=0;
                  String [] autorizadosSeleccionados= new String[arrayAutorizados.size()];
                  while (autorizadosSel.hasMoreTokens()) {
                        String seleccionado = autorizadosSel.nextToken();
                        autorizadosSeleccionados[i]=seleccionado;
                        i++;
                  }

                   for(int j=0;j<arrayAutorizados.size();j++)
                   {
                       AutorizadoNotificacionVO autorizadoVO=arrayAutorizados.get(j);
                       autorizadoVO.setSeleccionado(autorizadosSeleccionados[j]);
                       arrayAutorizados.set(j, autorizadoVO);
                       //m_Log.debug("===AUTORIZADO despues de volver de la JSP nombre "+autorizadoVO.getNombre());
                       //m_Log.debug("===NUM EXPEDIENTE despues de volver de la JSP nombre "+autorizadoVO.getNumeroExpediente());
                   }


                  i=0;
                  String [] adjuntosSeleccionados= new String[arrayAdjuntos.size()];
                  while (adjuntosSel.hasMoreTokens()) {
                        String seleccionado = adjuntosSel.nextToken();
                        adjuntosSeleccionados[i]=seleccionado;
                        i++;
                  }

                   for(int j=0;j<arrayAdjuntos.size();j++)
                   {
                       AdjuntoNotificacionVO adjuntoVO=arrayAdjuntos.get(j);
                       adjuntoVO.setSeleccionado(adjuntosSeleccionados[j]);
                       arrayAdjuntos.set(j, adjuntoVO);
                      // m_Log.debug("===AUTORIZADO despues de volver de la JSP nombre "+adjuntoVO.getNombre());
                      // m_Log.debug("===NUM EXPEDIENTE despues de volver de la JSP nombre "+adjuntoVO.getNumeroExpediente());
                   }

                  notificacionVO.setActoNotificado(actoNotificado);
                  notificacionVO.setAdjuntos(arrayAdjuntos);
                  notificacionVO.setAutorizados(arrayAutorizados);
                  if(caducidadNotificacion!=null && !"".equals(caducidadNotificacion))
                    notificacionVO.setCaducidadNotificacion(Integer.parseInt(caducidadNotificacion));
                  
                  notificacionVO.setTextoNotificacion(textoNotificacion);
                  notificacionVO.setNumExpediente(notificacionForm.getNumExpediente());
                  notificacionVO.setCodigoProcedimiento(notificacionForm.getCodigoProcedimiento());
                  notificacionVO.setEjercicio(notificacionForm.getEjercicio());
                  notificacionVO.setCodigoMunicipio(notificacionForm.getCodigoMunicipio());
                  notificacionVO.setCodigoTramite(notificacionForm.getCodigoTramite());
                  notificacionVO.setOcurrenciaTramite(notificacionForm.getOcurrenciaTramite());
                  notificacionVO.setAdjuntosExternos(notificacionForm.getAdjuntosExternos());
                  //notificacionVO.setFirma("");

                  //boolean retornoGrabar=pluginNotificacion.grabarNotificacion(notificacionVO, params);
                  
                  
                  boolean retornoGrabar = false;
                  if(codNotificacion==null || "".equals(codNotificacion) || "-1".equals(codNotificacion))
                      retornoGrabar=pluginNotificacion.grabarNotificacion(notificacionVO, params);
                  else{
                      notificacionVO.setCodigoNotificacion(Integer.parseInt(codNotificacion));
                      retornoGrabar=pluginNotificacion.actualizarNotificacion(notificacionVO, params);
                  }
                  
                  
                  if(retornoGrabar==true) session.setAttribute("exitoGrabarNotificacion","true");
                  else session.setAttribute("exitoGrabarNotificacion","false");
                  session.setAttribute("opcion",opcion);
                  session.setAttribute("notificacionForm", notificacionForm);
                 

                    m_Log.debug("===setCaducidadNotificacion "+notificacionForm.getCaducidadNotificacion());
                        m_Log.debug("===setActoNotificado "+notificacionForm.getActoNotificado());
                        m_Log.debug("===setTextoNotificacion "+notificacionForm.getTextoNotificacion());
             }catch (Exception cnfe) {
                    m_Log.error("EXCEPCION");
                    cnfe.printStackTrace();
             }

    
      }
      else if (opcion.equals("firmar")) {

              m_Log.debug("===NotificacionAction--> Firmar");
          if (session != null) {

                notificacionForm = (NotificacionForm) session.getAttribute("notificacionForm");
          }


           session.removeAttribute("exitoFirmarNotificacion");

           String listaAutorizadosSeleccionados =	request.getParameter("lAutorizadosSeleccionados");
           String listaAdjuntosSeleccionados = request.getParameter("lAdjuntosSeleccionados");

           String actoNotificado = request.getParameter("actoNotificado");
           String textoNotificacion = request.getParameter("textoNotificacion");
           m_Log.debug(" =========> ANTES textoNotificacion: " + textoNotificacion);
           String caducidadNotificacion = request.getParameter("caducidadNotificacion");
           String procedimiento = request.getParameter("procedimiento");
                      
           m_Log.debug(" =========> DESPUES textoNotificacion: " + textoNotificacion);
           try{

                PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(usuario.getOrgCod()));

                NotificacionVO notificacionVO=new NotificacionVO();

                StringTokenizer autorizadosSel = null;
                StringTokenizer adjuntosSel = null;

                autorizadosSel = new StringTokenizer(listaAutorizadosSeleccionados,"§¥",false);
                adjuntosSel = new StringTokenizer(listaAdjuntosSeleccionados,"§¥",false);

                ArrayList<AutorizadoNotificacionVO> arrayAutorizados=new ArrayList<AutorizadoNotificacionVO>();
                arrayAutorizados=notificacionForm.getAutorizados();

                ArrayList<AdjuntoNotificacionVO> arrayAdjuntos=new ArrayList<AdjuntoNotificacionVO>();
                arrayAdjuntos=notificacionForm.getAdjuntos();
              /* for(int i=0;i<arrayAdjuntos.size();i++)
                     {
                        AdjuntoNotificacionVO aVO=arrayAdjuntos.get(i);
                        m_Log.debug("===AUTORIZADO despues de volver de la JSP nombre "+aVO.getNombre());
                         m_Log.debug("===NUM EXPEDIENTE despues de volver de la JSP nombre "+aVO.getNumeroExpediente());
                     }
    */


                  int i=0;
                  String [] autorizadosSeleccionados= new String[arrayAutorizados.size()];
                  while (autorizadosSel.hasMoreTokens()) {
                        String seleccionado = autorizadosSel.nextToken();
                        autorizadosSeleccionados[i]=seleccionado;
                        i++;
                  }

                   for(int j=0;j<arrayAutorizados.size();j++)
                   {
                       AutorizadoNotificacionVO autorizadoVO=arrayAutorizados.get(j);
                       autorizadoVO.setSeleccionado(autorizadosSeleccionados[j]);
                       arrayAutorizados.set(j, autorizadoVO);
                       //m_Log.debug("===AUTORIZADO despues de volver de la JSP nombre "+autorizadoVO.getNombre());
                       //m_Log.debug("===NUM EXPEDIENTE despues de volver de la JSP nombre "+autorizadoVO.getNumeroExpediente());
                   }


                  i=0;
                  String [] adjuntosSeleccionados= new String[arrayAdjuntos.size()];
                  while (adjuntosSel.hasMoreTokens()) {
                        String seleccionado = adjuntosSel.nextToken();
                        adjuntosSeleccionados[i]=seleccionado;
                        i++;
                  }

                   for(int j=0;j<arrayAdjuntos.size();j++)
                   {
                       AdjuntoNotificacionVO adjuntoVO=arrayAdjuntos.get(j);
                       adjuntoVO.setSeleccionado(adjuntosSeleccionados[j]);
                       arrayAdjuntos.set(j, adjuntoVO);
                      // m_Log.debug("===AUTORIZADO despues de volver de la JSP nombre "+adjuntoVO.getNombre());
                      // m_Log.debug("===NUM EXPEDIENTE despues de volver de la JSP nombre "+adjuntoVO.getNumeroExpediente());
                   }

                  notificacionForm.setAutorizados(arrayAutorizados);
                  notificacionForm.setAdjuntos(arrayAdjuntos);
                  notificacionForm.setCaducidadNotificacion(Integer.parseInt(caducidadNotificacion));
                  notificacionForm.setActoNotificado(actoNotificado);
                  notificacionForm.setTextoNotificacion(textoNotificacion);
                  notificacionForm.setNombreExpediente(procedimiento);

                  notificacionVO.setActoNotificado(actoNotificado);
                  notificacionVO.setAdjuntos(arrayAdjuntos);
                  notificacionVO.setAutorizados(arrayAutorizados);
                  notificacionVO.setCaducidadNotificacion(Integer.parseInt(caducidadNotificacion));
                  notificacionVO.setTextoNotificacion(textoNotificacion);
                  notificacionVO.setNumExpediente(notificacionForm.getNumExpediente());
                  notificacionVO.setCodigoProcedimiento(notificacionForm.getCodigoProcedimiento());
                  notificacionVO.setEjercicio(notificacionForm.getEjercicio());
                  notificacionVO.setCodigoMunicipio(notificacionForm.getCodigoMunicipio());
                  notificacionVO.setCodigoTramite(notificacionForm.getCodigoTramite());
                  notificacionVO.setOcurrenciaTramite(notificacionForm.getOcurrenciaTramite());
                  notificacionVO.setNombreExpediente(procedimiento);
                  //notificacionVO.setFirma("");

                  //boolean retornoGrabar=pluginNotificacion.grabarNotificacion(notificacionVO, params);

                  int codigoUsuarioFirmante=NotificacionManager.getInstance().getUsuarioFirmanteNotificacion(notificacionVO, params);
                  int codigoUsuarioDelegado = UsuariosGruposManager.getInstance().getUsuarioDelegado(codigoUsuarioFirmante, params);

                  /*
                  if(codigoUsuarioFirmante!=-1)
                  {
                      if((codigoUsuarioFirmante==0) || (codigoUsuarioFirmante==usuario.getIdUsuario()) || (codigoUsuarioDelegado==usuario.getIdUsuario()))
                      { */
                          notificacionVO=pluginNotificacion.getNotificacion(notificacionVO, params);
                          boolean retornoGrabar=true;
                          if(notificacionVO==null)
                          {
                               retornoGrabar=pluginNotificacion.grabarNotificacion(notificacionVO, params);
                                 m_Log.debug("===NotificacionAction--> Guardar dentro de Firmar");
                          }
                          if(retornoGrabar==true)
                          {
                                m_Log.debug("===NotificacionAction--> Firmar, retornoGrabar:true");
                              session.setAttribute("exitoFirmarNotificacion","true");
                              //notificacionVO=pluginNotificacion.getNotificacion(notificacionVO, params);
                              String firma=pluginNotificacion.getNotificacionFirma(notificacionVO, params);                              
                              request.setAttribute("codNotificacion", notificacionVO.getCodigoNotificacion());
                              request.setAttribute("codOrganizacion", Integer.toString(usuario.getIdUsuario()));
                              //session.setAttribute("textoFirmar",firma);

                          }
                    /*  }
                      else
                      {
                           UsuarioManager mgr = UsuarioManager.getInstance();
                           UsuarioEscritorioValueObject usuarioEscritorioVO = mgr.buscaUsuario(codigoUsuarioFirmante);
                           session.setAttribute("nombreUsuarioFirmanteNotificacion",usuarioEscritorioVO.getNombreUsu());
                           session.setAttribute("exitoFirmarNotificacion","noUsu");
                      }
                  }
                  else  session.setAttribute("exitoFirmarNotificacion","false"); */

                  
                  session.setAttribute("notificacionForm", notificacionForm);
                  session.setAttribute("opcion",opcion);
                   
             }catch (Exception cnfe) {
                    m_Log.error("EXCEPCION");
                    cnfe.printStackTrace();
             }


      }

       else if (opcion.equals("guardaFirma")) {

              m_Log.debug("===NotificacionAction--> GuardarFirma");
          if (session != null) {

                notificacionForm = (NotificacionForm) session.getAttribute("notificacionForm");
          }

           session.removeAttribute("exitoFirmarNotificacion");
           String codigoNotificacion = request.getParameter("codigoNotificacion");
           String firma=request.getParameter("firma");
           boolean retornoGrabar=false;
           boolean firmaValida=false;

           NotificacionVO notificacionVO=new NotificacionVO();

           notificacionVO.setAplicacion(notificacionForm.getAplicacion());
           notificacionVO.setAdjuntos(notificacionForm.getAdjuntos());
           notificacionVO.setAutorizados(notificacionForm.getAutorizados());
           notificacionVO.setNombreExpediente(notificacionForm.getNombreExpediente());
           notificacionVO.setNumExpediente(notificacionForm.getNumExpediente());
           notificacionVO.setActoNotificado(notificacionForm.getActoNotificado());
           notificacionVO.setTextoNotificacion(notificacionForm.getTextoNotificacion());

           try{

               PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(usuario.getOrgCod()));

              firmaValida=pluginNotificacion.verificarFirma(notificacionVO, firma, params);
              

            }catch(Exception e){
            e.printStackTrace();
            session.setAttribute("exitoFirmarNotificacion","firmaFail");
            }

        if(!firmaValida){
              m_Log.debug("===NotificacionAction--> Firma no validada");
            session.setAttribute("exitoFirmarNotificacion","firmaFail");

        }else{
            m_Log.debug("===NotificacionAction--> Firma validada");
            boolean continuar = false;
            try{

                PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(usuario.getOrgCod()));
                retornoGrabar=pluginNotificacion.guardarFirma(Integer.parseInt(codigoNotificacion), firma, params);

                  if(retornoGrabar==true)
                  {
                        m_Log.debug("===NotificacionAction--> Firma guardada con exito");
                     session.setAttribute("exitoFirmarNotificacion","firmaSave");
                  }
                  else
                  {
                      m_Log.debug("===NotificacionAction--> Firma no guardada ERROR");
                    session.setAttribute("exitoFirmarNotificacion","firmaSaveFail");
                  }
                  
            }catch(Exception e){
                e.printStackTrace();
                session.setAttribute("exitoFirmarNotificacion","firmaSaveFail");
            }

         }
            session.setAttribute("notificacionForm", notificacionForm);
            session.setAttribute("opcion",opcion);
           

      }

       else if (opcion.equals("enviar")) {

             m_Log.debug("===NotificacionAction--> Enviar notificacion");
           if (session != null) {

                notificacionForm = (NotificacionForm) session.getAttribute("notificacionForm");
          }

           boolean res=false;
           try{

                PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(usuario.getOrgCod()));
                NotificacionVO notificacionVO=new NotificacionVO();

                notificacionVO.setNumExpediente(notificacionForm.getNumExpediente());
                notificacionVO.setCodigoProcedimiento(notificacionForm.getCodigoProcedimiento());
                notificacionVO.setEjercicio(notificacionForm.getEjercicio());
                notificacionVO.setCodigoMunicipio(notificacionForm.getCodigoMunicipio());
                notificacionVO.setCodigoTramite(notificacionForm.getCodigoTramite());
                notificacionVO.setOcurrenciaTramite(notificacionForm.getOcurrenciaTramite());
                String nombreExpediente = request.getParameter("nombreExpediente");
                notificacionVO.setCodDepartamento(Integer.toString(usuario.getDepCod()));

                notificacionVO=pluginNotificacion.getNotificacion(notificacionVO, params);

                String codigoTipoNotificacion=NotificacionManager.getInstance().getTipoNotificacion(notificacionVO, params);
                notificacionVO.setCodigoTipoNotificacion(codigoTipoNotificacion);
                notificacionVO.setNombreExpediente(nombreExpediente);                
                session.removeAttribute("exitoEnvio");
                
                boolean necesariaFirma = false;
                try{
                    ResourceBundle config = ResourceBundle.getBundle("notificaciones");
                    String firma = config.getString(usuario.getOrgCod() + "/FIRMAR_NOTIFICACION");
                    if(firma!=null && "SI".equalsIgnoreCase(firma))
                        necesariaFirma = true;
                    
                }catch(Exception e){
                    e.printStackTrace();;
                    
                }
                
                String[] emisor = UsuarioDAO.getInstance().getNombreNifUsuario(usuario.getIdUsuario(), params);
                notificacionVO.setNombreEmisor(emisor[0]);
                notificacionVO.setIdEmisor(emisor[1]);
                
                if(necesariaFirma && !"F".equals(notificacionVO.getEstadoNotificacion())){
                    // Si por configuración se indica que es necesaria la firma, y la 
                    // notificación no está firmada => Se muestra un error al usuario
                    session.setAttribute("exitoEnvio","noFirmado");
                }else{
                    
                     m_Log.debug("===NotificacionAction--> esta firmado, se puede enviar");
                     res=pluginNotificacion.enviarNotificacion(notificacionVO, params);
                     if(res==true)
                     {
                         session.setAttribute("exitoEnvio","envioSucess");
                         pluginNotificacion.guardarEstadoNotificacionEnviada(notificacionVO, params);

                     }
                     else session.setAttribute("exitoEnvio","envioFail");
                }
                                

             }catch (Exception cnfe) {
                    m_Log.error("EXCEPCION");
                    cnfe.printStackTrace();
                    res=false;
             }

          
           session.setAttribute("notificacionForm", notificacionForm);
           session.setAttribute("opcion",opcion);


      }else
      if(opcion.equalsIgnoreCase("adjuntarFicheroExterno")){
          
          FormFile fichero = notificacionForm.getFicheroExterno();
                    
          /*******************/          
          String fileName    = fichero.getFileName();
          String contentType = fichero.getContentType();
          byte[] contenido   = fichero.getFileData();
          
          String codMunicipio = request.getParameter("codMunicipio");
          String codProcedimiento = request.getParameter("codProcedimiento");
          String ejercicio = request.getParameter("ejercicio");          
          String numExpediente = request.getParameter("numero");
          String codTramite = request.getParameter("codTramite");
          String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
          String proc = request.getParameter("procedimiento");
          String codNotificacion = request.getParameter("codNotificacion");
          m_Log.debug("*****************Notificación para la que se adjunta un documento externo: " + codNotificacion);          
          if(contenido!=null) m_Log.debug("leng: " + contenido.length);
          m_Log.debug(" **** nombreFichero: " + fileName + ",contentType: " + contentType);

          boolean isExtensionValida = false;
          boolean isSizeValido      = false;        
          boolean isTipoMimeValido  = false;
          boolean existeFichero     = false;
                      
          request.setAttribute("ADJUNTO_ARCHIVO_OPCION","ADJUNTAR");
          
          if(fileName!=null && contentType!=null && contenido!=null){
                
            // Se ha subido un fichero adjunto para almacenar junto a una comunicación                                
            String extension  = fileName.substring(fileName.lastIndexOf(".") +1,fileName.length());
            isExtensionValida = this.extensionFicheroValido(extension);
            isTipoMimeValido  = this.tipoMimeFicheroValido(contentType);
            isSizeValido      = this.isTamanhoValido(fichero.getFileSize());                            
          }        
          
          
            if(!isExtensionValida){       
                m_Log.debug("La extensión del fichero no es válida");
                request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT,"si");                                  
                request.setAttribute(ConstantesDatos.EXTENSION_PERMITED,StrutsFileValidation.getLimite(StrutsFileValidation.EXTENSION_DOC_PREFFIX,StrutsFileValidation.APP_EXPEDIENTES));                    
                request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES,ConstantesDatos.DESCRIPCION_BYTES);

            }else {
                m_Log.debug("La extensión del fichero es válida");
                request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT,"no");                                  
            }
            

            if(!isSizeValido){       
                m_Log.debug("El tamaño del fichero excede el límite máximo");                
                request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE,this.limiteTamanhoFichero());
                request.setAttribute("ERROR_FILESIZE_UPLOAD","si");                        
                request.setAttribute(ConstantesDatos.DESCRIPCION_BYTES,ConstantesDatos.DESCRIPCION_BYTES);                                

            }else {
                m_Log.debug("El tamaño del fichero no excede el límite máximo");
                request.setAttribute(ConstantesDatos.TAM_MAX_FILE_EXCEED,"no");              
            }       
            
            
            if(!isTipoMimeValido){       
                m_Log.debug("La extensión del fichero no es válida");
                request.setAttribute(ConstantesDatos.TIPO_MIME_INCORRECTO,"si");                                                  

            }else {
                m_Log.debug("El tamaño del fichero no excede el límite máximo");
                request.setAttribute(ConstantesDatos.EXTENSION_FILE_INCORRECT,"no");        
            }     
            
            
            if(isTipoMimeValido && isSizeValido && isExtensionValida && !existeFichero){
                
                request.setAttribute("CORRECTO","si");                
                // String nombreDocumento = FileOperations.getNombreArchivo(fileName);
                String nombreDocumento = fileName;
                String extension = FileOperations.getExtension(fileName);
                
                
                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",codMunicipio);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numExpediente);              
                datos.put("codTramite",codTramite);
                datos.put("ocurrenciaTramite",ocurrenciaTramite);
                datos.put("params",params);
                datos.put("nombreDocumento",nombreDocumento);
                datos.put("extension",extension);              
                datos.put("tipoMime",contentType);
                datos.put("codDocumento",codNotificacion);
                datos.put("fichero",contenido);

                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
                Documento doc = null;
                int tipoDocumento = -1;
                if(!almacen.isPluginGestor())
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                else{
                    codProcedimiento = numExpediente.split("[/]")[1];
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);

                    ResourceBundle bundle = ResourceBundle.getBundle("documentos");           
                    String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                    
                    datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
                    /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                    listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));                    
                    listaCarpetas.add(ConstantesDatos.CARPETA_DOCUMENTOS_EXTERNOS_NOTIFICACION);
                    datos.put("listaCarpetas",listaCarpetas);

                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
               }

              try{
                    doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                    boolean insertado = almacen.setDocumentoExternoNotificacion(doc);
                    
                    if(!insertado){
                        // Error al adjuntar el archivo
                        request.setAttribute("ERROR_ALTA_ARCHIVO_BBDD","SI");
                    }else{
                        // Se recupera la lista de adjuntos para actualizar la vista                    
                        ArrayList<AdjuntoNotificacionVO> adjuntosExternos = AdjuntoNotificacionManager.getInstance().getListaAdjuntosExterno(Integer.parseInt(codNotificacion),params);
                        request.setAttribute("LISTA_ADJUNTOS_EXTERNOS",adjuntosExternos);
                    }                
                    
              }catch(AlmacenDocumentoTramitacionException e){
                  e.printStackTrace();                  
                  m_Log.error(e.getMessage());
              }
              
             
                
            }// if
            
            return mapping.findForward("adjuntarFicheroExterno");
            
          
          /********************/
      }else
      if(opcion.equals("eliminarArchivoExternoNotificacion")){
      
          String codigo = request.getParameter("codigo");
          String codNotificacion = request.getParameter("codNotificacion");
          m_Log.debug("NotificacionAction eliminar fichero de lista de adjuntos: " + codigo + " de la notificacion: " + codNotificacion);
                            
          request.setAttribute("ADJUNTO_ARCHIVO_OPCION","ELIMINAR");
          
          /**********************************************/
          String codMunicipio = request.getParameter("codMunicipio");
          String codProcedimiento = request.getParameter("codProcedimiento");
          String ejercicio = request.getParameter("ejercicio");
          String numExpediente = request.getParameter("numero");
          String codTramite = request.getParameter("codTramite");
          String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");        
          String nombreArchivo = request.getParameter("nombreDocumento");
                    
          String nombreDocumento = FileOperations.getNombreArchivo(nombreArchivo);
          String extension =  FileOperations.getExtension(nombreArchivo);
                   
          Hashtable<String,Object> datos = new Hashtable<String,Object>();
          datos.put("codMunicipio",codMunicipio);
          datos.put("ejercicio",ejercicio);
          datos.put("numeroExpediente",numExpediente);              
          datos.put("codTramite",codTramite);
          datos.put("ocurrenciaTramite",ocurrenciaTramite);
          datos.put("params",params);
          datos.put("codDocumento",codigo);
          datos.put("nombreDocumento",nombreDocumento);
          datos.put("extension",extension);
          
          AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
          Documento doc = null;
          int tipoDocumento = -1;
          if(!almacen.isPluginGestor())
             tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
          else{
                codProcedimiento = numExpediente.split("[/]")[1];
                String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);

                ResourceBundle bundle = ResourceBundle.getBundle("documentos");            
                String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                
                
                datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
                /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));                    
                listaCarpetas.add(ConstantesDatos.CARPETA_DOCUMENTOS_EXTERNOS_NOTIFICACION);
                datos.put("listaCarpetas",listaCarpetas);

                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
           }

          try{
                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                boolean insertado = almacen.eliminarDocumentoExternoNotificacion(doc);

                if(!insertado){
                    // Error al adjuntar el archivo
                    request.setAttribute("EXITO_ELIMINAR_ADJUNTO","NO");
                }else{
                    // Se recupera la lista de adjuntos para actualizar la vista                    
                    ArrayList<AdjuntoNotificacionVO> adjuntosExternos = AdjuntoNotificacionManager.getInstance().getListaAdjuntosExterno(Integer.parseInt(codNotificacion),params);
                    request.setAttribute("LISTA_ADJUNTOS_EXTERNOS",adjuntosExternos);
                    request.setAttribute("EXITO_ELIMINAR_ADJUNTO","SI");
                }                

          }catch(AlmacenDocumentoTramitacionException e){
              e.printStackTrace();                  
              m_Log.error(e.getMessage());
          }
         
          return mapping.findForward("eliminarFicheroExterno");
          
      }// else
      if(opcion.equals("descargarArchivoExternoNotificacion")){
      
          String codigo           = request.getParameter("codigo");
          String codMunicipio     = request.getParameter("codMunicipio");
          String codProcedimiento = request.getParameter("codProcedimiento");
          String ejercicio        = request.getParameter("ejercicio");
          String numExpediente    = request.getParameter("numero");
          String codTramite       = request.getParameter("codTramite");
          String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
          String descargarFichaExpediente = request.getParameter("descargarFichaExpediente");          
          FichaExpedienteForm fichaForm   = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
          boolean expedienteHistorico     = fichaForm.isExpHistorico();
          
          if(descargarFichaExpediente!=null && "1".equalsIgnoreCase(descargarFichaExpediente)){
              // Se intenta descargar el fichero externo desde la ficha del expediente. En este caso, sólo
              // se envían los parámetros codigo y descargarFichaExpediente, por tanto, hay que recuperar los
              // valores de los restantes.
              AdjuntoNotificacionVO  adjunto = AdjuntoNotificacionManager.getInstance().getAdjuntoById(Integer.parseInt(codigo),expedienteHistorico,params);
              if(adjunto!=null){
                codMunicipio = Integer.toString(adjunto.getCodigoMunicipio());
                numExpediente = adjunto.getNumeroExpediente();
                codTramite    = Integer.toString(adjunto.getCodigoTramite());
                ocurrenciaTramite    = Integer.toString(adjunto.getOcurrenciaTramite());
                
                String[] datosExp = numExpediente.split("/");
                ejercicio = datosExp[0];
                codProcedimiento = datosExp[1];
              }
          }
          
          byte[] fichero     = null;
          String fileName    = null;
          String contentType = null;
          AdaptadorSQLBD adapt = null;
          Connection con = null;
          
          try{              
                adapt = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
              
                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",codMunicipio);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numExpediente);              
                datos.put("codTramite",codTramite);
                datos.put("ocurrenciaTramite",ocurrenciaTramite);
                datos.put("params",params);                
                datos.put("codDocumento",codigo);
                
                datos.put("expedienteHistorico","false");
                if(expedienteHistorico) datos.put("expedienteHistorico","true");
                
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
                Documento doc = null;
                int tipoDocumento = -1;
                if(!almacen.isPluginGestor())
                  tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                else{
                  codProcedimiento = numExpediente.split("[/]")[1];
                  String nombreProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento, con);
                  AdjuntoNotificacionVO adjunto = AdjuntoNotificacionDAO.getInstance().getInfoDocumentoExternoNotificacion(Integer.parseInt(codigo),expedienteHistorico,con);
                  datos.put("nombreDocumento",FileOperations.getNombreArchivo(adjunto.getNombre()));
                  datos.put("extension",FileOperations.getExtension(adjunto.getNombre()));
                  datos.put("tipoMime", adjunto.getContentType());            
                  datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);            
                  
                  
                  ResourceBundle bundle = ResourceBundle.getBundle("documentos");                 
                  String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);                  
                  /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                  ArrayList<String> listaCarpetas = new ArrayList<String>();
                  listaCarpetas.add(carpetaRaiz);
                  listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
                  listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                  listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));                    
                  listaCarpetas.add(ConstantesDatos.CARPETA_DOCUMENTOS_EXTERNOS_NOTIFICACION);
                  datos.put("listaCarpetas",listaCarpetas);

                  tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
               }

               doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
               doc = almacen.getDocumentoExternoNotificacion(doc,con);

               fichero  = doc.getFichero();
               fileName = doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension();
               contentType = doc.getTipoMimeContenido();

                
               if(fichero!=null && fileName!=null && contentType!=null){
                    BufferedOutputStream bos = null;
                    try{                        

                        response.setContentType(contentType);
                        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                        response.setHeader("Content-Transfer-Encoding", "binary");            

                        ServletOutputStream out = response.getOutputStream();
                        response.setContentLength(fichero.length);
                        bos = new BufferedOutputStream(out);
                        bos.write(fichero, 0, fichero.length);
                        bos.flush();
                        bos.close();

                    }catch(Exception e){
                        e.printStackTrace();                
                    }finally {
                        if(bos != null) bos.close();
                    } 
               }

          }catch(BDException e){
              e.printStackTrace();                  
              m_Log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
          }
          catch(AlmacenDocumentoTramitacionException e){
              e.printStackTrace();                  
              m_Log.error("Error al recuperar el contenido del documento externo adjunto a una notificación: " + e.getMessage());
          }finally{     
              
              try{                  
                adapt.devolverConexion(con);
                
              }catch(Exception e){
                  e.printStackTrace();
              }
          }
            
              
      }// else
      else
      if("firmaCertificadoOrganismo".equals(opcion)){
          // Marca como pendiente de firma un adjunto externo
          m_Log.debug("Firma con certificado de organismo");
          String codAdjunto = request.getParameter("codAdjunto");
          String codNotificacion = request.getParameter("codNotificacion");
          String codOrganizacion = request.getParameter("codOrganizacion");
                              
          m_Log.debug("codAdjunto: " + codAdjunto);
          m_Log.debug("codNotificacion: " + codNotificacion);
          m_Log.debug("codOrganizacion: " + codOrganizacion);
          
          request.setAttribute("ADJUNTO_ARCHIVO_OPCION","FIRMAR_CERTIFICADO_ORGANISMO");
          
          if(codAdjunto!=null && !"".equalsIgnoreCase(codAdjunto) && codNotificacion!=null && !"".equalsIgnoreCase(codNotificacion)
                  && codOrganizacion!=null && !"".equalsIgnoreCase(codOrganizacion)){
              
              AdjuntoNotificacionVO adjunto = AdjuntoNotificacionManager.getInstance().getAdjuntoExternoNotificacion(Integer.parseInt(codAdjunto), params);
              if(adjunto!=null && adjunto.getContenido()!=null && adjunto.getContenido().length>0){
              
                  String firma = null;
                  request.setAttribute("FIRMA_CERTIFICADO_ORGANISMO_BBDD_EXITO","NO");                       
                  try{
                      
                        PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
                        firma = plugin.firmaServidor(adjunto.getContenido());
                        
                        if(firma!=null && !"".equals(firma)){
                            // Se ha recuperado la firma, se procede a actualizar la firma del adjunto en BBDD
                            
                            
                            adjunto.setFirma(firma);
                            adjunto.setCodUsuarioFirmaOtro(usuario.getIdUsuario());
                            if(plugin instanceof PluginPortafirmasAsf)
                                adjunto.setPlataformaFirma("ASF");
                            else
                            if(plugin instanceof PluginPortafirmasAfirma)
                                adjunto.setPlataformaFirma("AFIRMA");
                            
                            adjunto.setEstadoFirma(ConstantesDatos.ESTADO_FIRMA_FIRMADO);
                            adjunto.setTipoCertificadoFirma(ConstantesDatos.TIPO_CERTIFICADO_ORGANISMO);
                            adjunto.setIdDocExterno(Integer.parseInt(codAdjunto));
                            adjunto.setCodigoNotificacion(Integer.parseInt(codNotificacion));
                            boolean firmaAlmacenada = AdjuntoNotificacionManager.getInstance().guardarFirma(adjunto,params);
                            
                            if(firmaAlmacenada){
                                // Se recupera la lista de adjuntos para proceder a su actualización
                                ArrayList<AdjuntoNotificacionVO> adjuntosExternos = AdjuntoNotificacionManager.getInstance().getListaAdjuntosExterno(Integer.parseInt(codNotificacion),params);
                                request.setAttribute("LISTA_ADJUNTOS_EXTERNOS",adjuntosExternos);
                                request.setAttribute("FIRMA_CERTIFICADO_ORGANISMO_BBDD_EXITO","SI");                                                   
                            }
                        }
                        
                  }catch(Exception e){
                      m_Log.error("Error al obtener al firmar el documento con certificado de organismo: " + e.getMessage());
                      e.printStackTrace();
                   }
                
                
              }else{
                  // Error 
                  request.setAttribute("ERROR_RECUPERAR_ADJUNTO_FIRMA","SI");
                  
                  
                  
              }
              
          }
          
          return mapping.findForward("firmaCertificadoOrganismo");
      }else
      if("firmaNotificacionCertificadoOrganismo".equals(opcion)){
          // Marca como pendiente de firma un adjunto externo
          m_Log.debug("Firma notificacion con certificado de organismo");

          String codNotificacion = request.getParameter("codNotificacion");
          String codOrganizacion = request.getParameter("codOrganizacion");

          m_Log.debug("codNotificacion: " + codNotificacion);
          m_Log.debug("codOrganizacion: " + codOrganizacion);

          request.setAttribute("ADJUNTO_ARCHIVO_OPCION","FIRMAR_NOTIFICACION_CERTIFICADO_ORGANISMO");

          if(codNotificacion!=null && !"".equalsIgnoreCase(codNotificacion)
                  && codOrganizacion!=null && !"".equalsIgnoreCase(codOrganizacion)){

              String xml = NotificacionManager.getInstance().getXMLFirmaNotificacion(Integer.parseInt(codNotificacion), params);
              xml = StringUtils.escapeCP1252(xml, "UTF-8");
              xml = xml.replaceAll("\n",".");
              xml = xml.replaceAll("\r",".");
              xml = xml.replaceAll("\u201c",".");
              xml = xml.replaceAll("\u201d",".");
              xml = xml.replaceAll("\u20ac","E");

              if(xml!=null && !"".equals(xml)){

                  String firma = null;
                  request.setAttribute("ERROR_RECUPERAR_NOTIFICACION","NO");
                  try{

                        PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
                        firma = plugin.firmaServidor(xml.getBytes());

                        if(firma!=null && !"".equals(firma)){
                            // Se ha recuperado la firma, se procede a actualizar la firma del adjunto en BBDD

                            boolean firmaAlmacenada = NotificacionDAO.getInstance().guardarFirma(Integer.parseInt(codNotificacion), firma, params);

                            if(firmaAlmacenada){                                                                
                                request.setAttribute("ERROR_GUARDAR_FIRMA_NOTIFICACION","SI");
                            }else
                                request.setAttribute("ERROR_GUARDAR_FIRMA_NOTIFICACION","NO");
                        }

                  }catch(Exception e){
                      m_Log.error("Error al obtener al firmar el documento con certificado de organismo: " + e.getMessage());
                      e.printStackTrace();
                   }

              }else{
                  // Error
                  request.setAttribute("ERROR_RECUPERAR_NOTIFICACION","SI");
              }
          }
          return mapping.findForward("firmaNotificacionCertificadoOrganismo");
          
      }else
      if("actualizarListaAdjuntos".equals(opcion)){
          String codNotificacion = request.getParameter("codNotificacion");
          request.setAttribute("ADJUNTO_ARCHIVO_OPCION","ACTUALIZAR_LISTA_ADJUNTOS");
          if(codNotificacion!=null && !"".equals(codNotificacion)){
             ArrayList<AdjuntoNotificacionVO> adjuntosExternos = AdjuntoNotificacionManager.getInstance().getListaAdjuntosExterno(Integer.parseInt(codNotificacion),params);
             request.setAttribute("LISTA_ADJUNTOS_EXTERNOS",adjuntosExternos);
          }

          return mapping.findForward("actualizarListaAdjuntos");
      }else
      if("descargarNotificacionXML".equals(opcion)){
          String codNotificacion = request.getParameter("codNotificacion");
          if(codNotificacion!=null && !"".equals(codNotificacion)){
             
              String xml = NotificacionManager.getInstance().getXMLFirmaNotificacion(Integer.parseInt(codNotificacion), params);
              m_Log.debug("************ NotificacionAction xml a firmar *******");
              m_Log.debug("************ NotificacionAction xml: "  + xml);

             //String resumen = StringUtils.escapeCP1252(xml, "ISO-8859-1");
              String resumen = StringUtils.escapeCP1252(xml, "UTF-8");
             resumen = resumen.replaceAll("\n",".");
             resumen = resumen.replaceAll("\r",".");
             resumen = resumen.replaceAll("\u201c",".");
             resumen = resumen.replaceAll("\u201d",".");
             resumen = resumen.replaceAll("\u20ac","E");

             m_Log.debug("************ NotificacionAction xlm modificado: "  + resumen);

             if(resumen!=null && !"".equals(resumen)){
                  SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyHHmmss");
                  String fecha = sf.format(Calendar.getInstance().getTime());
                  byte[] fichero     = resumen.getBytes();
                  String fileName    = codNotificacion + "_" + fecha + ".xml";

                  BufferedOutputStream bos = null;
                  try{

                    response.setContentType("text/xml");
                    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                    response.setHeader("Content-Transfer-Encoding", "binary");

                    ServletOutputStream out = response.getOutputStream();
                    response.setContentLength(fichero.length);
                    bos = new BufferedOutputStream(out);
                    bos.write(fichero, 0, fichero.length);
                    bos.flush();
                    bos.close();

                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    if(bos != null) bos.close();
                }
             }

          }
      } else
      if("verFirmaAdjunto".equals(opcion)){
          
          String codAdjunto       = request.getParameter("codAdjunto");
          String codOrganizacion  = request.getParameter("codOrganizacion");                    
          String codProcedimiento = request.getParameter("codProcedimiento");
          String ejercicio        = request.getParameter("ejercicio");
          String numExpediente    = request.getParameter("numExpediente");
          String codTramite       = request.getParameter("codigoTramite");
          String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
          
          AdaptadorSQLBD adapt = null;
          Connection con = null;
          
          byte[] fichero     = null;                   
          Hashtable<String,Object> datos = new Hashtable<String,Object>();
          datos.put("codMunicipio",codOrganizacion);
          datos.put("ejercicio",ejercicio);
          datos.put("numeroExpediente",numExpediente);              
          datos.put("codTramite",codTramite);
          datos.put("ocurrenciaTramite",ocurrenciaTramite);                     
          datos.put("codDocumento",codAdjunto);
          
          
          try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
            Documento doc = null;
            int tipoDocumento = -1;
            if(!almacen.isPluginGestor())
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
            else{
              codProcedimiento = numExpediente.split("[/]")[1];
              String nombreProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);

              AdjuntoNotificacionVO adjunto = AdjuntoNotificacionDAO.getInstance().getInfoDocumentoExternoNotificacion(Integer.parseInt(codAdjunto),con);
              
              datos.put("nombreDocumento",FileOperations.getNombreArchivo(adjunto.getNombre()));
              datos.put("extension",FileOperations.getExtension(adjunto.getNombre()));
              datos.put("tipoMime", adjunto.getContentType());            

              ResourceBundle bundle = ResourceBundle.getBundle("documentos");       
              String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
              
              datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
              /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
              ArrayList<String> listaCarpetas = new ArrayList<String>();
              listaCarpetas.add(carpetaRaiz);
              listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + usuario.getOrg());
              listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
              listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));                    
              listaCarpetas.add(ConstantesDatos.CARPETA_DOCUMENTOS_EXTERNOS_NOTIFICACION);
              datos.put("listaCarpetas",listaCarpetas);

              tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
           }
              
            // Se recupera el contenido binario del documento
            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
            doc = almacen.getDocumentoExternoNotificacion(doc,con);


            if(doc!=null && doc.getFichero()!=null){

                byte[] documento =doc.getFichero();
                String documento64 = Base64.encodeBytes(doc.getFichero());

                File f = File.createTempFile("prueba", "temp");
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(documento);
                fos.flush();
                fos.close();


                // Se recupera la firma del documento para proceder la verificación de la firma
                doc.setExtension(ConstantesDatos.EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION);
                String firma = almacen.getFirmaDocumentoExternoNotificacion(doc,con);

                String tipoMime = doc.getTipoMimeContenido();
                
                if(firma!=null){
                    DocumentoFirmadoVO docFirmado = new DocumentoFirmadoVO();
                    docFirmado.setFicheroFirma(f);
                    docFirmado.setFirma(firma);
                    docFirmado.setFicheroHash64(documento64);
                    docFirmado.setTipoMime(tipoMime);

                    PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
                    ArrayListFirmasVO datosFirma = plugin.verificarFirmaInfo(docFirmado);

                    if(!datosFirma.isEmpty() && datosFirma.get(0) != null){
                        FirmaVO infoFirma = (FirmaVO) datosFirma.get(0);
                        m_Log.debug("asunto: " + infoFirma.getAsuntoCertificado());
                        m_Log.debug("getEmisorCertificado: " + infoFirma.getEmisorCertificado());
                        m_Log.debug("getNombrePersona: " + infoFirma.getNombrePersona());
                        m_Log.debug("getNif: " + infoFirma.getNif());
                        m_Log.debug("getValidez: " + infoFirma.getValidez());

                        notificacionForm.setDatosCertificado(infoFirma.getAsuntoCertificado());
                        notificacionForm.setEmisorCertificado(infoFirma.getEmisorCertificado());
                        notificacionForm.setNombreFirmante(infoFirma.getNombrePersona());
                        notificacionForm.setNifFirmante(infoFirma.getNif());
                        notificacionForm.setValidezCertificado(infoFirma.getValidez());
                        notificacionForm.setFirmaValida(infoFirma.getValido());                        
                        notificacionForm.setExtraccionDatosCertificado(true);

                        try{
                            f.delete();
                        }catch(Exception e){
                            m_Log.error(" Error al borrar el fichero temporal: " + e.getMessage());
                        }
                    }else{
                        notificacionForm.setExtraccionDatosCertificado(false);
                        try{
                            f.delete();
                        }catch(Exception e){
                            m_Log.error(" Error al borrar el fichero temporal: " + e.getMessage());
                        }
                    }
                 }
            }

          }catch(BDException e){
              e.printStackTrace();                  
              m_Log.error("Error al recuperar conexión a la BBDD: " + e.getMessage());
          }
          catch(AlmacenDocumentoTramitacionException e){
              e.printStackTrace();                  
              m_Log.error("Error al verificar la firma del documento externo asociado a una notificación: " + e.getMessage());
          }finally{
              try{
                  adapt.devolverConexion(con);
              }catch(Exception e){
                  e.printStackTrace();
              }
          }
          
          
          
          /**
          String codAdjunto = request.getParameter("codAdjunto");
          String codOrganizacion = request.getParameter("codOrganizacion");
          m_Log.debug("verFirmaAdjunto codAdjunto: " + codAdjunto);

          if(codAdjunto!=null && !"".equals(codAdjunto) && codOrganizacion!=null && !"".equals(codOrganizacion)){

              AdjuntoNotificacionVO adjunto = AdjuntoNotificacionManager.getInstance().getAdjuntoExternoNotificacion(Integer.parseInt(codAdjunto), params);

              if(adjunto!=null && !"".equals(adjunto)){

                  byte[] documento =adjunto.getContenido();

                  String documento64 = Base64.encodeBytes(adjunto.getContenido());

                    File f = File.createTempFile("prueba", "temp");
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(documento);
                    fos.flush();
                    fos.close();

                    DocumentoFirmadoVO docFirmado = new DocumentoFirmadoVO();
                    docFirmado.setFicheroFirma(f);
                    docFirmado.setFirma(adjunto.getFirma());
                    docFirmado.setFicheroHash64(documento64);

                    if (m_Log.isDebugEnabled()){m_Log.debug(this.getClass().getName() + ".verficarFirmaDocumentoComunicacion() Validando la firma");}
                    PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
                    ArrayListFirmasVO datosFirma = plugin.verificarFirmaInfo(docFirmado);

                    if(!datosFirma.isEmpty() && datosFirma.get(0) != null){
                        FirmaVO infoFirma = (FirmaVO) datosFirma.get(0);
                        m_Log.debug("asunto: " + infoFirma.getAsuntoCertificado());
                        m_Log.debug("getEmisorCertificado: " + infoFirma.getEmisorCertificado());
                        m_Log.debug("getNombrePersona: " + infoFirma.getNombrePersona());
                        m_Log.debug("getNif: " + infoFirma.getNif());
                        m_Log.debug("getValidez: " + infoFirma.getValidez());
                        
                        notificacionForm.setDatosCertificado(infoFirma.getAsuntoCertificado());
                        notificacionForm.setEmisorCertificado(infoFirma.getEmisorCertificado());
                        notificacionForm.setNombreFirmante(infoFirma.getNombrePersona());
                        notificacionForm.setNifFirmante(infoFirma.getNif());
                        notificacionForm.setValidezCertificado(infoFirma.getValidez());
                        notificacionForm.setFirmaValida(infoFirma.getValido());                        
                        notificacionForm.setExtraccionDatosCertificado(true);

                        try{
                            f.delete();
                        }catch(Exception e){
                            m_Log.error(" Error al borrar el fichero temporal: " + e.getMessage());
                        }
                    }else{
                        notificacionForm.setExtraccionDatosCertificado(false);
                        try{
                            f.delete();
                        }catch(Exception e){
                            m_Log.error(" Error al borrar el fichero temporal: " + e.getMessage());
                        }
                    }
             }
          } **/
          
          return mapping.findForward("verificarFirmaAdjuntoNotificacion");
      }else
      if("notificacionGrabadaCorrectamente".equals(opcion)){
          String codNotificacion = request.getParameter("codNotificacion");
          String numExpediente = request.getParameter("numExpediente");
          /*** SE COMPRUEBA QUE LA NOTIFICACIÓN HAYA SIDO GRABADA CORRECTAMENTE ANTES DE PROCEDER A REALIZAR EL PROCESO DE FIRMA **/

          m_Log.debug("codNotificacion: " + codNotificacion + ",numExpediente: " + numExpediente);
          boolean exito = false;
          if(codNotificacion!=null && !"".equals(codNotificacion) && numExpediente!=null && !"".equals(numExpediente)){

             exito = NotificacionManager.getInstance().estaNotificacionPreparadaParaFirma(codNotificacion, numExpediente, params);
             m_Log.debug("exito: " + exito);
          }

          try{

            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.print("NOTIFICACION_CORRECTA=" + exito);
            out.flush();
            out.close();

          }catch(Exception e){
                e.printStackTrace();
          }

          return null;
      }else
      if("detalleNotificacion".equals(opcion)){
          m_Log.debug("opcion = detalleNotificacion");

          String codNotificacion = request.getParameter("codNotificacion");          
          m_Log.debug("codNotificacion:  " + codNotificacion);
          
          FichaExpedienteForm fichaForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
          
          if(codNotificacion!=null && !"".equals(codNotificacion)){

              try{
                    //NotificacionVO notif = NotificacionManager.getInstance().getDetalleNotificacion(codNotificacion,fichaForm.isExpHistorico(),params);
					
				    String codRegTel = request.getParameter("numRegistro");
					NotificacionVO notif = NotificacionManager.getInstance().getDetalleNotificacion(codNotificacion,codRegTel,fichaForm.isExpHistorico(),params);

                    session.setAttribute("DetalleNotificacion",notif);

                                        
              }catch(Exception e){
                  e.printStackTrace();
                  // GESTIONAR EL ERROR
                  request.setAttribute("ERROR_RECUPERAR_NOTIFICACION","SI");
              }

          }
          return mapping.findForward("detalleNotificacion");
      }else
       if(opcion.equals("descargarDocumentoTramitacion")){

          String unidad = request.getParameter("unidad");
          String codTramite = request.getParameter("codTramite");
          String ocuTramite = request.getParameter("ocuTramite");
          String numExpediente = request.getParameter("numExpediente");
          String nombre        = request.getParameter("nombre");
          m_Log.debug("******************NotificacionAction descargar archivo tramitacion numero: " + unidad + ",codTramite:"  + codTramite + ",ocuTramite: " + ocuTramite + ",numExpediente: " + numExpediente);
          
          FichaExpedienteForm fichaForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
          if(unidad!=null && !"".equals(unidad) && codTramite!=null && !"".equals(codTramite) && ocuTramite!=null && !"".equals(ocuTramite) && numExpediente!=null && !"".equals(numExpediente) && nombre!=null && !"".equals(nombre)){
                String codOrganizacion = Integer.toString(usuario.getOrgCod());              
                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",codOrganizacion);
                datos.put("numeroExpediente",numExpediente);
                datos.put("codTramite",codTramite);
                datos.put("ocurrenciaTramite",ocuTramite);
                datos.put("numeroDocumento",unidad);
                datos.put("perteneceRelacion","false");
                datos.put("params",params);                
                datos.put("expedienteHistorico","false");
                //añadimos atributo a hashtable para indicar procedencia
                datos.put("desdeNotificacion","si");
                if(fichaForm.isExpHistorico()) datos.put("expedienteHistorico","true");
                
                String ejercicio = numExpediente.split("/")[0];                
                String codProcedimiento = numExpediente.split("/")[1];                
                
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
                Documento doc = null;
                ResourceBundle configCommon = ResourceBundle.getBundle("common");
                int tipoDocumento = -1;
                byte[] fichero = null;

                if(!almacen.isPluginGestor())
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                else{                    
                    String editor = configCommon.getString("editorPlantillas");
                    String extension = "doc";
                    if(editor!=null && "OOFFICE".equalsIgnoreCase(editor)){
                        extension = "odt";
                    }

                    datos.put("nombreDocumento",nombre);
                    datos.put("extension",extension);
                    String tipoMime = "";

                    // Se obtiene el nombre del documento a mostrar porque se necesita para el caso de que se venga de firmar el documento
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio",codOrganizacion);
                    gVO.setAtributo("codProcedimiento",codProcedimiento);
                    gVO.setAtributo("ejercicio",ejercicio);
                    gVO.setAtributo("codTramite",codTramite);
                    gVO.setAtributo("ocurrenciaTramite",ocuTramite);
                    gVO.setAtributo("numeroExpediente",numExpediente);
                    gVO.setAtributo("numeroDocumento",unidad);
                    gVO.setAtributo("expHistorico","false");                    
                    if(fichaForm.isExpHistorico()) gVO.setAtributo("expHistorico","true");
                    
                    datos.put("nombreDocumento",DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, params));                    
                    String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(codOrganizacion,codProcedimiento, codTramite, params);
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                    String descripcionOrganizacion = OrganizacionesManager.getInstance().getDescripcionOrganizacion(codOrganizacion, params);
                    datos.put("codProcedimiento",codProcedimiento);                    
                    datos.put("codigoVisibleTramite",codigoVisibleTramite);

                    if (configCommon.getString("editorPlantillas").equals("OOFFICE"))
                          tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
                    else
                        tipoMime = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;

                    datos.put("tipoMime",tipoMime);
                    datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);

                    /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                    ResourceBundle bundle = ResourceBundle.getBundle("documentos");          
                    String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                    
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                    listaCarpetas.add(((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));

                    datos.put("listaCarpetas",listaCarpetas);
                    /*** FIN  ***/

                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                }

                try{
                    doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos,tipoDocumento);
                    fichero = almacen.getDocumento(doc);
                }catch(AlmacenDocumentoTramitacionException e){
                    e.printStackTrace();
                }


                if(fichero!=null && fichero.length>0){
                    String nombreFichero = "";
                    String tipoMime = "";
                    SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyyHHmmss");
                    Calendar c = Calendar.getInstance();
					
                    // Obtener la extension  y tipoMime del documento a partir del nombre del mismo
					if(doc.getExtension() == null){
						String nombreFicheroExtension = FilenameUtils.getExtension(nombre);
						if(!org.apache.commons.lang.StringUtils.isEmpty(nombreFicheroExtension))
							doc.setExtension(nombreFicheroExtension);
					}
					if(doc.getTipoMimeContenido() == null && doc.getExtension() != null)
						doc.setTipoMimeContenido(MimeTypes.guessMimeTypeFromExtension(doc.getExtension()));
					
                    if(doc!=null && doc.getExtension()!=null) {
                        nombreFichero = "prueba" + sf.format(c.getTime()) + "." + doc.getExtension();
                    } else if (configCommon.getString("editorPlantillas").equals("OOFFICE")) {
                        nombreFichero = "prueba" + sf.format(c.getTime()) + ".odt";
                    } else {
                        nombreFichero = "prueba" + sf.format(c.getTime()) + ".doc";
                    }
                    
                    if(doc!=null && doc.getTipoMimeContenido()!=null) {
                        tipoMime = doc.getTipoMimeContenido();
                    } else if (configCommon.getString("editorPlantillas").equals("OOFFICE")) {
                       tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
                    } else { 
                        tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_WORD;
                    }
              

                    BufferedOutputStream bos = null;
                    try{
                        response.setContentType(tipoMime);
                        response.setHeader("Content-Disposition", "inline; filename=" + nombreFichero);
                        response.setHeader("Content-Transfer-Encoding", "binary");
                        response.setContentLength(fichero.length);
                        ServletOutputStream out = response.getOutputStream();
                        bos = new BufferedOutputStream(out);
                        bos.write(fichero, 0, fichero.length);
                        bos.flush();
                        bos.close();
                         
                    }catch(Exception e){
                        e.printStackTrace();
                    }finally {
                        if(bos != null) bos.close();
                    }
                 }

            }// else
       }
      } else { // No hay usuario.
      m_Log.debug("NotificacionAction --> no hay usuario");
      opcion = "no_usuario";
    }

    /* Redirigimos al JSP de salida*/
    m_Log.debug("<================= NotificacionAction ======================");
    return (mapping.findForward(opcion));

  }

      
      
    /**
     * Comprueba si existe un fichero
     * @param fileName: Nombre del fichero
     * @param contentType: Content-type del fichero
     * @param notificaciones: Colección con las notificaciones
     * @return Un boolean
     */  
    private boolean existeFichero(String fileName,String contentType,ArrayList<AdjuntoNotificacionVO> notificaciones){
        boolean exito = false;
        if(fileName!=null && contentType!=null && notificaciones!=null){
            for(int i=0;i<notificaciones.size();i++){
                if(notificaciones.get(i).getNombre().equals(fileName) && notificaciones.get(i).getContentType().equals(contentType)){
                   exito = true;
                   break;
                }
                
            }// for            
        }
        
        return exito;
    }
      
      
      
      
      /**     
     * Comprueba si el tipo mime de un fichero está entre las válidos
     * @param extension: Extensión a verificar
     * @return booleano
     */
    private boolean tipoMimeFicheroValido(String mime){
        boolean exito = false;
        
        try{
            ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
            String MIME_TYPES = bundle.getString("mimetype.upload.correct");
            String[] tiposMime = MIME_TYPES.split(",");

            for(int i=0;tiposMime!=null && i<tiposMime.length;i++){
                if(tiposMime[i].equalsIgnoreCase(mime)){
                    exito = true;
                    break;
                }
            }            
        }catch(Exception e){
            e.printStackTrace();            
        }
        
        return exito;
    }
    
    
    /**     
     * Comprueba si la extensión de un fichero está entre las válidas
     * @param extension: Extensión a verificar
     * @return booleano
     */
     private boolean extensionFicheroValido(String extension){
        boolean exito = false;
        
        try{
            ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
            String extensiones = bundle.getString("extension.upload.correct");
            String[] listaExtensiones = extensiones.split(",");

            for(int i=0;listaExtensiones!=null && i<listaExtensiones.length;i++){
                if(listaExtensiones[i].equalsIgnoreCase(extension)){
                    exito = true;
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            exito = false;
        }
        return exito;
    }
     
     
   /**     
     * Comprueba si el tamaño del fichero es válido
     * @param size: Tamaño del fichero a verificar
     * @return booleano
     */
     private boolean isTamanhoValido(int size){
        boolean exito = false;
        
        try{
            ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
            String sTamano  = bundle.getString("filesize.upload.correct");
            int tamanoMaximo = Integer.parseInt(sTamano);            
            if(size<=tamanoMaximo){
                exito = true;
            }
            
        }catch(Exception e){
            e.printStackTrace();
            exito = false;
        }
        return exito;
    }
          
    
     private String extensionesValidas(){
         String extensiones = "";
         try{
             ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
             String lista  = bundle.getString("extension.upload.correct");
             if(lista!=null && !"".equals(lista))
                 extensiones = lista;                 
             
         }catch(Exception e){
             e.printStackTrace();
         }
         
         return extensiones;
     }
        
     
     
     private String limiteTamanhoFichero(){
         String limite = "";
         try{
             ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
             String lista  = bundle.getString("filesize.upload.correct");
             if(lista!=null && !"".equals(lista))
                 limite = lista;                 
             
         }catch(Exception e){
             e.printStackTrace();
         }         
         return limite;
     }

    

}
