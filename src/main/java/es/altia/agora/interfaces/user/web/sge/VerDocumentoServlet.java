package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.documentos.DocumentoManager;
import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.geninformes.utils.Utilidades;
import es.altia.agora.business.sge.persistence.DatosSuplementariosManager;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.agora.webservice.tramitacion.servicios.aytos.sw.firmadoc.business.SWFirmaDocManager;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.catalogoformularios.model.solicitudes.vo.AnexoVO;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.gestionmensaje.GestionMensajeErrorDokusi;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;


public class VerDocumentoServlet extends HttpServlet
{
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

    protected static Log log =
            LogFactory.getLog(VerDocumentoServlet.class.getName());

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception {
      
      if (log.isDebugEnabled()) {
      	log.debug("EJECUTANDO SERVLET: VerDocumentoServlet");
      	log.debug("Cookies : " + req.getHeader("cookie"));
        log.debug("method : " + req.getMethod());
      }
      AlmacenDocumentoTramitacionException exceptionDokusiGestionMensajeError = null;
      HttpSession session = req.getSession();
      if (session == null) {
          log.error("SESION NULA EN VerDocumentoServlet.defaultAction");
          return;
      }
      log.debug("ID de la sesion : " + session.getId());            
      String nombreFichero = req.getParameter("nombreFich");
      String desdeWS = req.getParameter("desdeWS");
      String tipoContenido =  "application/x-download";
      String codigo = req.getParameter("codigo");
      String opcion = req.getParameter("opcion");
      // Por defecto el valor es inline, por lo que solo usaremos el modo attachement si
      // se indica el parametro embedded explicitamente a false.
      boolean embeddedMode = !"false".equalsIgnoreCase(req.getParameter("embedded"));
      boolean expHistorico = ("true".equals(req.getParameter("expHistorico")));
      String organizacion = req.getParameter("organizacion");
      byte[] fichero=null;
      
      if (log.isDebugEnabled()) {
        log.debug(">>>>embeddedMode .... "+embeddedMode);
        log.debug(">>>>OPCION .... "+opcion);
        log.debug(">>>>NOMBRE FICHERO .... "+nombreFichero);
      	log.debug(">>> CODIGO a visualizar: " + codigo);      
      }
            
      // Ficheros cargados en el form al cargar el expediente
      if (opcion.equals("0")) {
          FichaExpedienteForm expForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
          if (expForm == null) {
              log.error("SESION INCORRECTA: EL FORM NO ESTA");
              if(!"SI".equals(desdeWS))return;
          }

          if ("si".equals(req.getParameter("otroDocExp"))) {

              String ejercicio        = req.getParameter("ejercicio");
              String numero         = req.getParameter("numero");
              String codMunicipio  = req.getParameter("codMunicipio");
              String extension      = req.getParameter("extension");
              String tipoMime       = req.getParameter("tipoMime");

              UsuarioValueObject usuario=new UsuarioValueObject();
               String[] params=null;
               
              if("SI".equals(desdeWS)){
                usuario.setOrgCod(Integer.parseInt(codMunicipio));
                usuario.setIdioma(1);

                Config confWS = ConfigServiceHelper.getConfig("WSFlexia");
                String gestor=confWS.getString(codMunicipio+"/BBDD/gestor");
                String jndi=confWS.getString(codMunicipio+"/BBDD/jndi");
                log.debug("jndi "+jndi);
                params = new String[7];
                params[0]=gestor;
                params[6]=jndi;
              
              }
              else{
                  usuario = (UsuarioValueObject) session.getAttribute("usuario");
                  params = usuario.getParamsCon();
              }
              

              Hashtable<String,Object> datos = new Hashtable<String,Object>();
              datos.put("codMunicipio",codMunicipio);
              datos.put("ejercicio",ejercicio);
              datos.put("numeroExpediente",numero);              
              datos.put("numeroDocumento",codigo);
              datos.put("params",params);
              datos.put("nombreDocumento",nombreFichero);
              datos.put("extension",extension);              
              datos.put("tipoMime","application/octet-stream");
              String codProcedimiento = numero.split("[/]")[1];
              
              AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
              Documento doc = null;
              int tipoDocumento = -1;
              
              if(!almacen.isPluginGestor())
                   tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
              else{
                    
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);

                    ResourceBundle bundle = ResourceBundle.getBundle("documentos");                    
                    String carpetaRaiz       = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                    datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
                    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                    traductor.setApl_cod(4);
                    traductor.setIdi_cod(usuario.getIdioma());

                    /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + usuario.getOrg());
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                    listaCarpetas.add(numero.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));                    
                    datos.put("listaCarpetas",listaCarpetas);

                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
              }

              try{
                    doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                    doc.setExpHistorico(expHistorico);
                    doc = almacen.getDocumentoExterno(doc);
                    
                    fichero             = doc.getFichero();
                    nombreFichero = doc.getNombreDocumento() + "." + doc.getExtension();
                    tipoContenido  = doc.getTipoMimeContenido();
                                     
                    log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero longitud: " + fichero.length);
                    log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero nombreFichero: " + nombreFichero);
                    log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero tipoContenido: " + tipoContenido);
                    
              }catch(AlmacenDocumentoTramitacionException e){
				  e.printStackTrace();
                  log.error("Error al recuperar documento : ", e);
                  log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                  exceptionDokusiGestionMensajeError = e;
                  fichero             = null;
                  nombreFichero = null;
                  tipoContenido   = null;
              }
              
          } else {
                 // Se procede a extraer el código y ocurrencia del trámite, del código del campo suplementario por parámetro
                
              
                // Se comprueba si lo que se desea visualizar es un fichero de un campo de un trámite (desde la ficha del expediente)
                // o de un expediente
                if(!verificarVisualizarCampoFicheroTramiteDesdeExpediente(codigo)){
                
                      Integer estadoFich =0;
                     if(!"SI".equals(desdeWS)){
                    //Recupera el estado de localizacion del fichero de expediente
                    GeneralValueObject estadosFichero = expForm.getListaEstadoFicheros();
                     estadoFich = (Integer)estadosFichero.getAtributo(codigo);
                    } else estadoFich=ConstantesDatos.ESTADO_DOCUMENTO_GRABADO;

                    //Recupera el fichero de un repositorio, bien a traves del plugin de BBDD o de un Gestor
                    if (estadoFich.equals(ConstantesDatos.ESTADO_DOCUMENTO_GRABADO)){


                        String codMunicipio  = "";              
                        String ejercicio = "";
                        String numero  = "";

                        UsuarioValueObject usuario = new UsuarioValueObject();
                        String[] params = null;

                        if ("SI".equals(desdeWS)) {
                            
                            codMunicipio = req.getParameter("codMunicipio");
                            ejercicio = req.getParameter("ejercicio");
                            numero = req.getParameter("numExpediente");
                            
                            usuario.setOrgCod(Integer.parseInt(codMunicipio));
                            usuario.setIdioma(1);

                            Config confWS = ConfigServiceHelper.getConfig("WSFlexia");
                            String gestor = confWS.getString(codMunicipio + "/BBDD/gestor");
                            String jndi = confWS.getString(codMunicipio + "/BBDD/jndi");
                            log.debug("jndi " + jndi);
                            params = new String[7];
                            params[0] = gestor;
                            params[6] = jndi;
                            
                            

                        } else {
                            codMunicipio  = expForm.getCodMunicipio();              
                            ejercicio = expForm.getEjercicio();
                            numero  = expForm.getNumero();

                            usuario = (UsuarioValueObject) session.getAttribute("usuario");
                            params = usuario.getParamsCon();
                        }

                        Hashtable<String,Object> datos = new Hashtable<String,Object>();
                        datos.put("numeroDocumento",codigo);              
                        datos.put("codTipoDato",codigo);
                        datos.put("codMunicipio",codMunicipio);
                        datos.put("ejercicio",ejercicio);
                        datos.put("numeroExpediente",numero);              
                        datos.put("params",params);
                        String codProcedimiento = numero.split("/")[1];

                        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
                        Documento doc = null;
                        int tipoDocumento = -1;
                        if(!almacen.isPluginGestor())
                             tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                        else{
                            //PENDIENTE IMPLEMENTACION - GESTOR ALFRESCO
                            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                            int codOrganizacion = usuario.getOrgCod();

                            //  Si se trata de un plugin de un gestor documental, se pasa la información
                            // extra necesaria                                    
                            ResourceBundle config = ResourceBundle.getBundle("documentos");                            
                            String carpetaRaiz       = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                            GeneralValueObject gVO = DatosSuplementariosManager.getInstance().getInfoCampoSuplementarioFicheroExpediente(codOrganizacion,codigo,numero,expHistorico,params);              
                            String descripcionOrganizacion = (String)gVO.getAtributo("descOrganizacion");
                            String descProcedimiento = (String)gVO.getAtributo("descProcedimiento");
                            nombreFichero = (String)gVO.getAtributo("nombreFichero");
                            tipoContenido = (String)gVO.getAtributo("tipoMime");

                            datos.put("tipoMime",tipoContenido);                    
                            datos.put("nombreDocumento", codigo + "_" + nombreFichero);
                            datos.put("nombreFicheroCompleto", codigo + "_" + nombreFichero);

                            ArrayList<String> listaCarpetas = new ArrayList<String>();
                            listaCarpetas.add(carpetaRaiz);
                            listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                            listaCarpetas.add(numero.replaceAll("/","-"));
                            datos.put("listaCarpetas",listaCarpetas);
                        }

                        try{
                              doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                              doc.setExpHistorico(expHistorico);
                              doc = almacen.getDocumentoDatosSuplementarios(doc);

                              fichero = doc.getFichero();
                              nombreFichero = doc.getNombreDocumento() ;
                              tipoContenido  = doc.getTipoMimeContenido();

                              if (log.isDebugEnabled()) {
                                  log.debug("Logitud del fichero: " + fichero.length);
                                  log.debug("Nombre del fichero: " + nombreFichero);
                                  log.debug("Tipo contenido fichero: " + tipoContenido);

                              }

                        }catch(AlmacenDocumentoTramitacionException e){
							e.printStackTrace();
                            log.error("Error al recuperar documento : ", e);
                            log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                            exceptionDokusiGestionMensajeError = e;
                            fichero             = null;
                            nombreFichero = null;
                            tipoContenido   = null;
                        }

                    }else{

                        if (estadoFich.equals(ConstantesDatos.ESTADO_DOCUMENTO_NUEVO)){

                            GeneralValueObject rutasFicherosNuevosVO = expForm.getListaRutaFicherosDisco();
                            String path = (String)rutasFicherosNuevosVO.getAtributo(codigo);

                            GeneralValueObject tipoMimeFicheros = expForm.getListaTiposFicheros();
                            tipoContenido = (String)tipoMimeFicheros.getAtributo(codigo);

                            GeneralValueObject nombreFicheros = expForm.getListaNombreFicheros();
                            nombreFichero = (String)nombreFicheros.getAtributo(codigo);

                            if(path!=null && tipoContenido!=null && nombreFichero!=null){

                                try{                            
                                    File f = new File(path);
                                    fichero = FileOperations.readFile(f);

                                }catch(Exception e){
                                    if (log.isErrorEnabled()) {log.error("Error al recuperar el documento " + nombreFichero + " de disco: " + e.getMessage());}
                                    e.printStackTrace();
                                    fichero  = null;
                                    nombreFichero = null;
                                    tipoContenido = null;
                                }
                           }// if
                        }
                    }
                }else{
                    
                    /**
                     * *************************
                     */
                    String codMunicipio = "";
                    String ejercicio = "";
                    String numero = "";

                    Hashtable<String,String> datosTramite= obtenerTramiteOcurrenciaCampo(codigo);                    
                    String codTramite        = datosTramite.get("codTramite");
                    String ocurrenciaTramite = datosTramite.get("ocurrenciaTramite");
                    String codigoCampo       = datosTramite.get("codigoCampo");
                    
                    UsuarioValueObject usuario = new UsuarioValueObject();
                    String[] params = null;

                    if ("SI".equals(desdeWS)) {
                         
                        codMunicipio = req.getParameter("codMunicipio");
                        ejercicio = req.getParameter("ejercicio");
                        numero = req.getParameter("numExpediente");
                        
                        usuario.setOrgCod(Integer.parseInt(codMunicipio));
                        usuario.setIdioma(1);

                        Config confWS = ConfigServiceHelper.getConfig("WSFlexia");
                        String gestor = confWS.getString(codMunicipio + "/BBDD/gestor");
                        String jndi = confWS.getString(codMunicipio + "/BBDD/jndi");
                        log.debug("jndi " + jndi);
                        params = new String[7];
                        params[0] = gestor;
                        params[6] = jndi;

                        

                    } else {
                        codMunicipio = expForm.getCodMunicipio();
                        ejercicio = expForm.getEjercicio();
                        numero = expForm.getNumero();

                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        params = usuario.getParamsCon();
                    }

                    Hashtable<String,Object> datos = new Hashtable<String,Object>();                                
                    codigo = codigo.substring(0,codigo.indexOf(("_")));                
                    datos.put("codTipoDato",codigoCampo);             
                    datos.put("codMunicipio",codMunicipio);
                    datos.put("ejercicio",ejercicio);
                    datos.put("numeroExpediente",numero);              
                    datos.put("params",params);
                    datos.put("codTramite",codTramite);
                    datos.put("ocurrenciaTramite",ocurrenciaTramite);
                    String codProcedimiento = (numero.split("/"))[1];
                    
                    AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);

                    Documento doc = null;
                    int tipoDocumento = -1;
                    if(!almacen.isPluginGestor())
                         tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                    else{
                         //PENDIENTE IMPLEMENTACION - GESTOR ALFRESCO
                         tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;

                        

                         //  Si se trata de un plugin de un gestor documental, se pasa la información
                         // extra necesaria                                    
                         ResourceBundle config = ResourceBundle.getBundle("documentos");                         
                         String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                         GeneralValueObject gVO = DatosSuplementariosManager.getInstance().getInfoCampoSuplementarioFicheroTramite(Integer.parseInt(codMunicipio),
                                 Integer.parseInt(codTramite),Integer.parseInt(ocurrenciaTramite),codigoCampo,numero,expHistorico,params);
                         String descripcionOrganizacion = (String)gVO.getAtributo("descOrganizacion");
                         String descProcedimiento = (String)gVO.getAtributo("descProcedimiento");
                         nombreFichero = (String)gVO.getAtributo("nombreFichero");
                         tipoContenido = (String)gVO.getAtributo("tipoMime");

                         datos.put("tipoMime",tipoContenido);                    
                         datos.put("nombreDocumento", codigoCampo + "_" + nombreFichero);
                         datos.put("nombreFicheroCompleto", codigoCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);
                         datos.put("nombreDocumento", codigoCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);


                         ArrayList<String> listaCarpetas = new ArrayList<String>();
                         listaCarpetas.add(carpetaRaiz);
                         listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                         listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                         listaCarpetas.add(numero.replaceAll("/","-"));
                         datos.put("listaCarpetas",listaCarpetas);

                    }

                    try{
                          doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                          doc.setExpHistorico(expHistorico);
                          doc = almacen.getDocumentoDatosSuplementariosTramite(doc);

                          fichero = doc.getFichero();
                          nombreFichero = doc.getNombreDocumento() ;
                          tipoContenido  = doc.getTipoMimeContenido();

                          if (log.isDebugEnabled()) {
                              log.debug("Logitud del fichero: " + fichero.length);
                              log.debug("Nombre del fichero: " + nombreFichero);
                              log.debug("Tipo contenido fichero: " + tipoContenido);
                          }

                    }catch(AlmacenDocumentoTramitacionException e){
						e.printStackTrace();
                        log.error("Error al recuperar documento : ", e);
                        log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                        exceptionDokusiGestionMensajeError = e;
                        fichero  = null;
                        nombreFichero = null;
                        tipoContenido = null;
                    }

                    /*****************************/
                }
          }
          
      } else if (opcion.equals("1")){
          
            
            String numero  = null;
          String codMunicipio = null;
          String ejercicio = null;

          String codTramite = null;
          String ocurrenciaTramite = null;

          boolean expedienteHistorico = false;

          Integer estadoDocumento = null;

          //Recupera el estado de localizacion del fichero de Tramite
           TramitacionExpedientesForm expForm = null;
              GeneralValueObject estadosFichero = null;

              
          if (!"SI".equals(desdeWS)) {

               expForm = (TramitacionExpedientesForm) session.getAttribute("TramitacionExpedientesForm");
               estadosFichero = expForm.getTramitacionExpedientes().getListaEstadoFicheros();

              numero = null;
              codMunicipio = expForm.getCodMunicipio();
              ejercicio = expForm.getEjercicio();
            numero  = expForm.getNumeroExpediente();
              codTramite = expForm.getCodTramite();
              ocurrenciaTramite = expForm.getOcurrenciaTramite();

              expedienteHistorico = expForm.isExpHistorico();

              estadoDocumento = (Integer) estadosFichero.getAtributo(codigo);
          } else {
              estadoDocumento = ConstantesDatos.ESTADO_DOCUMENTO_GRABADO;
              numero = null;

              codMunicipio = req.getParameter("codMunicipio");
              ejercicio = req.getParameter("ejercicio");
              numero = req.getParameter("numExpediente");
              codTramite = req.getParameter("codTramite");
              ocurrenciaTramite = req.getParameter("ocurrenciaTramite");

          }
            
            
            
            //Recupera el fichero de un repositorio, bien a traves del plugin de BBDD o de un Gestor
            if (estadoDocumento.equals(ConstantesDatos.ESTADO_DOCUMENTO_GRABADO)){
                
                    UsuarioValueObject usuario = new UsuarioValueObject();
                String[] params = null;
                   if ("SI".equals(desdeWS)) {
                            
                            codMunicipio = req.getParameter("codMunicipio");
                            ejercicio = req.getParameter("ejercicio");
                            numero = req.getParameter("numExpediente");
                            
                            usuario.setOrgCod(Integer.parseInt(codMunicipio));
                            usuario.setIdioma(1);

                            Config confWS = ConfigServiceHelper.getConfig("WSFlexia");
                            String gestor = confWS.getString(codMunicipio + "/BBDD/gestor");
                            String jndi = confWS.getString(codMunicipio + "/BBDD/jndi");
                            log.debug("jndi " + jndi);
                            params = new String[7];
                            params[0] = gestor;
                            params[6] = jndi;
                   }else{
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuario.getParamsCon();
                   }
                
               

                Hashtable<String,Object> datos = new Hashtable<String,Object>();                                
                codigo = codigo.substring(0,codigo.indexOf(("_")));                
                datos.put("codTipoDato",codigo);             
                datos.put("codMunicipio",codMunicipio);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numero);              
                datos.put("params",params);
                datos.put("codTramite",codTramite);
                datos.put("ocurrenciaTramite",ocurrenciaTramite);                
                datos.put("expedienteHistorico","false");
                if(expedienteHistorico) datos.put("expedienteHistorico","true");

                String codProcedimiento = (numero.split("/"))[1];                
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento); 

                FichaExpedienteForm fichaForm = (FichaExpedienteForm)session.getAttribute("FichaExpedienteForm");
                                
                Documento doc = null;
                int tipoDocumento = -1;
                if(!almacen.isPluginGestor())
                     tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                else{
                     //PENDIENTE IMPLEMENTACION - GESTOR ALFRESCO
                     tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;                     
                     //String codProcedimiento = (numero.split("/"))[1];
                     
                     //  Si se trata de un plugin de un gestor documental, se pasa la información
                     // extra necesaria                                    
                     ResourceBundle config = ResourceBundle.getBundle("documentos");                     
                     String carpetaRaiz       = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                     
                     GeneralValueObject gVO = DatosSuplementariosManager.getInstance().getInfoCampoSuplementarioFicheroTramite(Integer.parseInt(codMunicipio),
                             Integer.parseInt(codTramite),Integer.parseInt(ocurrenciaTramite),codigo,numero,expedienteHistorico,params);
                     String descripcionOrganizacion = (String)gVO.getAtributo("descOrganizacion");
                     String descProcedimiento = (String)gVO.getAtributo("descProcedimiento");
                     nombreFichero = (String)gVO.getAtributo("nombreFichero");
                     tipoContenido = (String)gVO.getAtributo("tipoMime");
                    
                     datos.put("tipoMime",tipoContenido);                    
                     datos.put("nombreDocumento", codigo + "_" + nombreFichero);
                     datos.put("nombreFicheroCompleto", codigo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);
                     datos.put("nombreDocumento", codigo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);
                     
                    
                     ArrayList<String> listaCarpetas = new ArrayList<String>();
                     listaCarpetas.add(carpetaRaiz);
                     listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                     listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                     listaCarpetas.add(numero.replaceAll("/","-"));
                     datos.put("listaCarpetas",listaCarpetas);
                     
                }

                try{
                      doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                      if (!"SI".equals(desdeWS)) {
                      doc.setExpHistorico(fichaForm.isExpHistorico());                      
                      }                      
                      doc = almacen.getDocumentoDatosSuplementariosTramite(doc);

                      fichero = doc.getFichero();
                      nombreFichero = doc.getNombreDocumento() ;
                      tipoContenido  = doc.getTipoMimeContenido();

                      if (log.isDebugEnabled()) {
                          log.debug("Logitud del fichero: " + fichero.length);
                          log.debug("Nombre del fichero: " + nombreFichero);
                          log.debug("Tipo contenido fichero: " + tipoContenido);
                      }

                }catch(AlmacenDocumentoTramitacionException e){
					e.printStackTrace();
                    log.error("Error al recuperar documento : ", e);
                    log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                    exceptionDokusiGestionMensajeError = e;
                    fichero  = null;
                    nombreFichero = null;
                    tipoContenido = null;
                }
                
            }else{
                if (estadoDocumento.equals(ConstantesDatos.ESTADO_DOCUMENTO_NUEVO)){

                    GeneralValueObject rutaFicherosDisco = expForm.getListaRutaFicherosDisco();
                    GeneralValueObject listaTiposMimeDocumento = expForm.getListaTiposFicheros();
                    GeneralValueObject listaNombreDocumento = expForm.getListaNombresFicheros();
                    
                    String path = null;
                    try{
                                                
                        path = (String)rutaFicherosDisco.getAtributo(codigo);
                        tipoContenido = (String)listaTiposMimeDocumento.getAtributo(codigo);
                        nombreFichero = (String)listaNombreDocumento.getAtributo(codigo);
                        
                        if(path!=null && !"".equals(path)){                        
                            File f = new File(path);
                            fichero = FileOperations.readFile(f);
                        }
                           
                    }catch(Exception e){
                        if (log.isErrorEnabled()) {log.error("Error al recuperar el documento " + nombreFichero + " de disco: " + e.getMessage());}
                         e.printStackTrace();
                        fichero  = null;
                        nombreFichero = null;
                        tipoContenido = null;
                    }
                }
            }

          
      } else if (opcion.equals("6")){//Ficheros de inicio de expediente
          FichaExpedienteForm expForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
          UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
          String[] params = usuario.getParamsCon();
          Hashtable<String, Object> datos = new Hashtable<String, Object>();
          datos.put("codMunicipio", expForm.getCodMunicipio());
          datos.put("ejercicio", expForm.getEjercicio());
          datos.put("numeroExpediente", expForm.getNumExpediente());
          datos.put("codProcedimiento", expForm.getCodProcedimiento());
          datos.put("codDocumento", codigo);
          datos.put("params", params);
          //Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, 1,almacen.getNombreServicio());
          //doc = ExpedienteDocPresentadoManager.getInstance().getContenidoDocumentoPresentado(doc);
          //fichero = doc.getFichero();
          //tipoContenido = doc.getTipoMimeContenido();
          //nombreFichero = doc.getNombreDocumento();
      } else if (opcion.equals("2")){
          FichaRelacionExpedientesForm expForm = (FichaRelacionExpedientesForm)session.getAttribute("FichaRelacionExpedientesForm");
          fichero = (byte[])expForm.getListaFicheros().getAtributo(codigo);
      } else if (opcion.equals("3")){
          log.debug("RECUPERAMOS EL DOCUMENTO DEL SERVICIO WEB DE FIRMADOC...");
          UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
          String[] params = usuario.getParamsCon();
          fichero = obtenerFicheroFirmaDoc(codigo, params);
      }else if (opcion.equals("4")){
           //anexo del formulario pdf
          log.debug("Recuperamos el fichero anexo al formulario pdf...");
          String formulario = req.getParameter("formPDF");
          int codAnexo = Integer.parseInt(req.getParameter("anexo"));
          log.debug("form="+formulario+ " anexo=" + codAnexo);
          FormularioFacade facade = new FormularioFacade();
          AnexoVO anexo = facade.getAnexoConFichero(formulario, codAnexo);
          fichero = anexo.getFichero();
          nombreFichero = anexo.getDescripcion();
          tipoContenido = anexo.getTipo();
      // Ficheros de registro o de tramites para los cuales existe un mapeo en el form
      } else if (opcion.equals("5")){
          
          String codMunicipio = null;
          String numero = null;
          String codTramite = null;
          String ocurrenciaTramite = null;
          String codigoCampo = null;
          String ejercicio = null;
          String[] datosExp = codigo.split("-");
          log.debug(" ============> VerDocumentoServlet visualizar contenido dato suplementario tramite");
          if(datosExp!=null && datosExp.length==6){
              
            codMunicipio = datosExp[1];
            numero = datosExp[2];
            codTramite = datosExp[3];
            ocurrenciaTramite = datosExp[4];
            codigoCampo = datosExp[5];
            
            String[] datosExp2 = numero.split("/");
            ejercicio = datosExp2[0];
                        
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();

            Hashtable<String,Object> datos = new Hashtable<String,Object>();                                
            //codigo = codigo.substring(0,codigo.indexOf(("_")));                
            datos.put("codTipoDato",codigoCampo);             
            datos.put("codMunicipio",codMunicipio);
            datos.put("ejercicio",ejercicio);
            datos.put("numeroExpediente",numero);              
            datos.put("params",params);
            datos.put("codTramite",codTramite);
            datos.put("ocurrenciaTramite",ocurrenciaTramite);
            String codProcedimiento = (numero.split("/"))[1];
            
            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);

            Documento doc = null;
            int tipoDocumento = -1;
            if(!almacen.isPluginGestor())
                 tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
            else{                 
                 tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                 
                 //  Si se trata de un plugin de un gestor documental, se pasa la información
                 // extra necesaria                                    
                 ResourceBundle config = ResourceBundle.getBundle("documentos");                 
                 String carpetaRaiz       = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                 GeneralValueObject gVO = DatosSuplementariosManager.getInstance().getInfoCampoSuplementarioFicheroTramite(Integer.parseInt(codMunicipio),
                         Integer.parseInt(codTramite),Integer.parseInt(ocurrenciaTramite),codigoCampo,numero,expHistorico,params);
                 
                 String descripcionOrganizacion = (String)gVO.getAtributo("descOrganizacion");
                 String descProcedimiento = (String)gVO.getAtributo("descProcedimiento");
                 nombreFichero = (String)gVO.getAtributo("nombreFichero");
                 tipoContenido = (String)gVO.getAtributo("tipoMime");

                 datos.put("tipoMime",tipoContenido);                    
                 datos.put("nombreDocumento", codigoCampo + "_" + nombreFichero);
                 datos.put("nombreFicheroCompleto", codigoCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);
                 datos.put("nombreDocumento", codigoCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);

                 ArrayList<String> listaCarpetas = new ArrayList<String>();
                 listaCarpetas.add(carpetaRaiz);
                 listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                 listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                 listaCarpetas.add(numero.replaceAll("/","-"));
                 datos.put("listaCarpetas",listaCarpetas);
            }
            
            
            try{
                
                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                
                doc.setExpHistorico(expHistorico);                
                doc = almacen.getDocumentoDatosSuplementariosTramite(doc);                
                
                
                fichero = doc.getFichero();
                nombreFichero = doc.getNombreDocumento() ;
                tipoContenido  = doc.getTipoMimeContenido();

                if (log.isDebugEnabled()) {
                    log.debug("Logitud del fichero: " + fichero.length);
                    log.debug("Nombre del fichero: " + nombreFichero);
                    log.debug("Tipo contenido fichero: " + tipoContenido);
                }

            }catch(AlmacenDocumentoTramitacionException e){
				e.printStackTrace();
                log.error("Error al recuperar documento : ", e);
                log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                exceptionDokusiGestionMensajeError = e;
                fichero  = null;
                nombreFichero = null;
                tipoContenido = null;
            } 
              
          }
      }else
      if(opcion.equals("7")){
          
        String codMunicipio = null;
        String numero = null;
        String codTramite = null;
        String ocurrenciaTramite = null;
        String numeroDocumento = null;
        String ejercicio = null;
        String codProcedimiento = null;
        String[] datosExp = codigo.split("-");
          
        if(datosExp!=null && datosExp.length==6){
              
            codMunicipio = datosExp[1];
            numero = datosExp[2];
            codTramite = datosExp[3];
            ocurrenciaTramite = datosExp[4];
            numeroDocumento = datosExp[5];
            
            String[] datosExp2 = numero.split("/");
            ejercicio = datosExp2[0];
            codProcedimiento = datosExp2[1];
            
            log.debug("codMunicipio : " + datosExp[1]);
            log.debug("numeroExpediente : " + datosExp[2]);
            log.debug("codTramite : " + datosExp[3]);
            log.debug("ocurrenciaTramite : " + datosExp[4]);
            log.debug("numeroDocumento : " + datosExp[5]);
            log.debug("ejercicio : " + datosExp2[0]);
            log.debug("codProcedimiento : " + datosExp2[1]);
            log.debug("desdeWS : " + desdeWS);
            
               UsuarioValueObject usuario=new UsuarioValueObject();
               String[] params=null;
            if("SI".equals(desdeWS)){
                usuario.setOrgCod(Integer.parseInt(codMunicipio));
                usuario.setIdioma(1);

                Config confWS = ConfigServiceHelper.getConfig("WSFlexia");
                String gestor=confWS.getString(codMunicipio+"/BBDD/gestor");
                String jndi=confWS.getString(codMunicipio+"/BBDD/jndi");
                log.debug("jndi "+jndi);
                params = new String[7];
                params[0]=gestor;
                params[6]=jndi;
              
              }
              else{
                  usuario = (UsuarioValueObject) session.getAttribute("usuario");
                  if (usuario == null) {
                      log.debug("USUARIO ES NULO");
                  }
                  params = usuario.getParamsCon();
              }
            
            Hashtable<String,Object> datos = new Hashtable<String,Object>();
            datos.put("codMunicipio",codMunicipio);
            datos.put("numeroExpediente",numero);
            datos.put("codTramite",codTramite);
            datos.put("ocurrenciaTramite",ocurrenciaTramite);
            datos.put("numeroDocumento",numeroDocumento);
            datos.put("perteneceRelacion","false");
            datos.put("params",params);
            log.debug("params : " + params);
            
            String editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(Integer.parseInt(codMunicipio),
                    numero,Integer.parseInt(codTramite),Integer.parseInt(ocurrenciaTramite),
                    Integer.parseInt(numeroDocumento),false,params);

            // Para las viejas plantillas comprobamos si tiene CSV, en cuyo caso se
            // trata como un documento que tiene extension en el nombre de fichero.
            if (ConstantesDatos.OOFFICE.equals(editorTexto) || ConstantesDatos.WORD.equals(editorTexto)) {
                // Se verifica si tiene CSV, si tiene CSV es que no es un documento de tipo WORD o OOFFICE
                // Por lo que el fichero tiene extension
                if (DocumentoManager.getInstance().existeMetadatoCSV(
                        params, ConstantesDatos.SUBCONSULTA_E_CRD_ID_METADATO_PK,
                        codMunicipio,
                        codProcedimiento,
                        ejercicio,
                        numero,
                        codTramite,
                        ocurrenciaTramite,
                        numeroDocumento)) {
                    
                    editorTexto = "";
                }
            }
            
            String nombreDocumento = null;
            String extension = null;
            String tipoMime = "";

            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", codMunicipio);
            gVO.setAtributo("codProcedimiento", codProcedimiento);
            gVO.setAtributo("ejercicio", ejercicio);
            gVO.setAtributo("codTramite", codTramite);
            gVO.setAtributo("ocurrenciaTramite", ocurrenciaTramite);
            gVO.setAtributo("numeroExpediente", numero);
            gVO.setAtributo("numeroDocumento", numeroDocumento);
            gVO.setAtributo("expHistorico", expHistorico ? "true" : "false");
            nombreDocumento = DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, params);

            if (ConstantesDatos.OOFFICE.equals(editorTexto)) { 
                extension = "odt";
                tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
            } else if (ConstantesDatos.WORD.equals(editorTexto)) { 
                extension = "doc";                 
                tipoMime = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;
            } else { // Ficheros sin plantilla
                extension = FilenameUtils.getExtension(nombreDocumento);
                tipoMime = MimeTypes.guessMimeTypeFromExtension(extension);
            }

            datos.put("nombreDocumento", nombreDocumento);
            datos.put("tipoMime",tipoMime);
            datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);            
            datos.put("extension",extension);

            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
            Documento doc = null;
            
            int tipoDocumento = -1;
            if(!almacen.isPluginGestor()){
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
            } else{
                String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(codMunicipio,codProcedimiento,codTramite, params);
                String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                String descripcionOrganizacion = OrganizacionesManager.getInstance().getDescripcionOrganizacion(codMunicipio, params);
                datos.put("codProcedimiento",codProcedimiento);                
                datos.put("codigoVisibleTramite",codigoVisibleTramite);

                /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                ResourceBundle bundle = ResourceBundle.getBundle("documentos");                
                String carpetaRaiz   = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                
                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                listaCarpetas.add(((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));

                datos.put("listaCarpetas",listaCarpetas);
                /*** FIN  ***/

                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
            }

            
            
            try{
                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                doc.setExpHistorico(expHistorico);
                fichero = almacen.getDocumento(doc);

                String nombreFicheroExtension = FilenameUtils.getExtension(doc.getNombreDocumento());
                if (StringUtils.isEmpty(nombreFicheroExtension) || !nombreFicheroExtension.equalsIgnoreCase(doc.getExtension())) {
                nombreFichero = doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension();
                } else {
                    nombreFichero = doc.getNombreDocumento();
                }
                
                tipoContenido  = doc.getTipoMimeContenido();

                if (log.isDebugEnabled()) {
                    log.debug("Logitud del fichero: " + fichero.length);
                    log.debug("Nombre del fichero: " + nombreFichero);
                    log.debug("Tipo contenido fichero: " + tipoContenido);
                }

            }catch(AlmacenDocumentoTramitacionException e){
				e.printStackTrace();
                log.error("Error al recuperar documento : ", e);
                log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                exceptionDokusiGestionMensajeError = e;
                fichero  = null;
                nombreFichero = null;
                tipoContenido = null;
            }           
          
        }
      }else
      if(opcion.equals("8")){
          // Ver un documento de registro
          
          String[] parametros = codigo.split("§¥");
          
          log.debug(" ===============> VerDocumentoServlet número parametros: " + parametros.length);
          String ejercicio = null;
          String numeroAnotacion = null;
          String tipoEntrada = null;
          String nombreDocumento = null;
          String codUor = null;
          String codDepartamento = null;
          String tipoMime = null;
          
          if(parametros!=null && parametros.length==7){              
              ejercicio = parametros[0];
              numeroAnotacion = parametros[1];
              tipoEntrada = parametros[2];
              nombreDocumento = parametros[3];
              codUor = parametros[4];
              codDepartamento = parametros[5];
              tipoMime = parametros[6];
              
              tipoContenido = tipoMime;
              
              /*******/
              UsuarioValueObject usuario=new UsuarioValueObject();
               String[] params=null;
              if("SI".equals(desdeWS)){
                usuario.setOrgCod(Integer.parseInt(organizacion));
                usuario.setIdioma(1);

                Config confWS = ConfigServiceHelper.getConfig("WSFlexia");
                String gestor=confWS.getString(organizacion+"/BBDD/gestor");
                String jndi=confWS.getString(organizacion+"/BBDD/jndi");
                log.debug("jndi "+jndi);
                params = new String[7];
                params[0]=gestor;
                params[6]=jndi;
              
              }
              else{
                  usuario = (UsuarioValueObject) session.getAttribute("usuario");
                  params = usuario.getParamsCon();
              }
              
              
    
              
              AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassRegistro(Integer.toString(usuario.getOrgCod()));
              Hashtable<String,Object> datos = new Hashtable<String,Object>();

              int codOrganizacion = usuario.getOrgCod();
              datos.put("identDepart",new Integer(codDepartamento));
              datos.put("unidadOrgan",new Integer(codUor));
              datos.put("anoReg",new Integer(ejercicio));
              datos.put("numReg",new Long(numeroAnotacion));
              datos.put("tipoReg",tipoEntrada);
              datos.put("nombreDocumento",nombreDocumento);            
              datos.put("documentoRegistro",new Boolean(true));
              datos.put("params",params);
              datos.put("tipoMime",tipoMime);
              datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));

              int tipoDocumento = -1;
              if(!almacen.isPluginGestor())
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
              else{
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                
                datos.put("codMunicipio",Integer.toString(codOrganizacion));

                if(almacen.isPluginGestor()){
                    //  Si se trata de un plugin de un gestor documental, se pasa la información
                    // extra necesaria                                    
                    ResourceBundle config = ResourceBundle.getBundle("documentos");                    
                    String carpetaRaiz       = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                    
                    Connection con = null;
                    String descripcionOrganizacion = null;
                    String descripcionUnidadRegistro = null;
                    AdaptadorSQLBD adapt = null;
                    try{
                        adapt = new AdaptadorSQLBD(params);
                        con = adapt.getConnection();

                        descripcionOrganizacion   = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(codOrganizacion, con);                            
                        UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],codUor);
                        if (uorDTO!=null)
                            descripcionUnidadRegistro = uorDTO.getUor_nom();
                    }catch(BDException e){
                        log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());
                    }finally{
                        try{
                            adapt.devolverConexion(con);
                        }catch(BDException e){
                            log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
                        }
                    }

                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codUor + ConstantesDatos.GUION + descripcionUnidadRegistro);

                    if(tipoEntrada.equalsIgnoreCase("E"))
                        listaCarpetas.add(ConstantesDatos.DESCRIPCION_ENTRADAS_REGISTRO);
                    else
                    if(tipoEntrada.equalsIgnoreCase("S"))
                        listaCarpetas.add(ConstantesDatos.DESCRIPCION_SALIDAS_REGISTRO);    

                    listaCarpetas.add(ejercicio + ConstantesDatos.GUION + numeroAnotacion);
                    datos.put("listaCarpetas",listaCarpetas);
                 }                       
               }

                try{
                    Documento documento = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                    
                    documento = almacen.getDocumentoRegistro(documento);                    
                    
                    if(documento!=null && documento.getFichero()!=null && documento.getFichero().length>0){
                        fichero = documento.getFichero();
                        nombreFichero = String.format("%s.%s", documento.getNombreDocumento(), documento.getExtension());
                    }

                }catch(AlmacenDocumentoTramitacionException e){
					e.printStackTrace();
                    log.error("Error al recuperar documento : ", e);
                    log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
                    exceptionDokusiGestionMensajeError = e;
                }
              
              /*******/
          }
          
      }
     
            
      if(fichero != null){
        BufferedOutputStream bos = null;
        try{                        
            String contentDisposition = String.format("%s; filename=\"%s\"",
                    (embeddedMode) ? "inline" : "attachment",
                    nombreFichero);
            res.setContentType(tipoContenido);
            res.setHeader("Content-Disposition", contentDisposition);
            res.setHeader("Content-Transfer-Encoding", "binary");            
            
            ServletOutputStream out = res.getOutputStream();
            res.setContentLength(fichero.length);
            bos = new BufferedOutputStream(out);
            bos.write(fichero, 0, fichero.length);
            bos.flush();
            bos.close();
            
            
        }catch(Exception e){
            if(log.isDebugEnabled()) log.debug("Excepcion en catch de VerDocumentoServlet.defaultAction()");
            throw e;
        }finally {
            if(bos != null) bos.close();
        }
      } else {
          log.warn("FICHERO NULO EN VerDocumentoServlet.defaultAction");
          try {
              log.error(this.getClass() + " - Preparamos el js para mostrar mensaje error Dokusi en Servelet");
              //ResourceBundle configDocumentosDokusi = ResourceBundle.getBundle("MELANBIDE_DOKUSI");
              UsuarioValueObject usu = (UsuarioValueObject) session.getAttribute("usuario");
              Integer idioma = 1;
              if (usu != null) {
                  idioma = usu.getIdioma();
              } else {
                   log.debug(this.getClass() + " usuario vuelve a estar nulo");
              }
              GestionMensajeErrorDokusi gestionMensajeErrorDokusi = new GestionMensajeErrorDokusi();
              String textoMensaje = gestionMensajeErrorDokusi.preparaTextoHtmlContenidoMensaje(exceptionDokusiGestionMensajeError, idioma);
              PrintWriter out = res.getWriter();
              out.println(textoMensaje);
          } catch (Exception e) {
              log.error(this.getClass() + " - Error al mostrar mensaje error Dokusi en Servelet - " + e.getMessage());
          }
      }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
      try     {
          if (log.isDebugEnabled()) log.debug("Entrando en el servlet VerDocumentosServlet");
          defaultAction(req, res);
    } catch (Exception e) {
        if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
        e.printStackTrace();
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    try {
        defaultAction(req, res);
    } catch (Exception e) {
        if (log.isErrorEnabled()) log.error("ERROR doPost:" + e);
    }
  }

  
  /**
   * Comprueba si un código de un campo suplementario de tipo fichero, es de un trámite o de un expediente
   * @param codigo: Código del campo
   * @return True si todo ha ido bien y false en caso contrario
   */
  private boolean verificarVisualizarCampoFicheroTramiteDesdeExpediente(String codigo){
    boolean exito = false;
    Hashtable<String,String> datosCampo = null;
    try{
        datosCampo = obtenerTramiteOcurrenciaCampo(codigo);	
        String codTramite        = datosCampo.get("codTramite");	
        String ocurrenciaTramite = datosCampo.get("ocurrenciaTramite");	
        String codigoCampo       = datosCampo.get("codigoCampo");	
        if(codigoCampo!=null && codTramite!=null && ocurrenciaTramite!=null && Utilidades.isInteger(ocurrenciaTramite) && Utilidades.isInteger(codTramite)){
            exito = true;  
        }
      }catch(Exception e){
        e.printStackTrace();
      }
      
      return exito;
  }
  
  
  private Hashtable<String,String> obtenerTramiteOcurrenciaCampo(String codigo){
      Hashtable<String,String> salida = new  Hashtable<String,String>();
      try{
      
        String[] datosCodigo = codigo.split("_");        
        if(datosCodigo!=null && datosCodigo.length==2){
            String ocurrenciaTramite = datosCodigo[1];
            
            String cabecera = datosCodigo[0].substring(0,2);                        
            String codTramite = cabecera.substring(1,2);
            String codigoCampo = datosCodigo[0].substring(2,datosCodigo[0].length());
            
            salida.put("codigoCampo",codigoCampo);
            salida.put("codTramite",codTramite);
            salida.put("ocurrenciaTramite",ocurrenciaTramite);
       } else if(datosCodigo!=null && datosCodigo.length==3){	
            String ocurrenciaTramite = datosCodigo[2];	
            String codigoCampo = datosCodigo[1];	
            String codTramite = datosCodigo[0].substring(1);	
            	
            log.debug("codigoCampo: " + codigoCampo);	
            log.debug("ocurrenciaTramite: " + ocurrenciaTramite);	
            log.debug("codTramite: " + codTramite);	
            salida.put("codigoCampo",codigoCampo);	
            salida.put("codTramite",codTramite);	
            salida.put("ocurrenciaTramite",ocurrenciaTramite);	
        } 
          
      }catch(Exception e){
        e.printStackTrace();
      }
      
      return salida;
      
  }
  

    /* Metodo encargado de extraer el contenido del documento del servicio web y visualizarlo. */
    private byte[] obtenerFicheroFirmaDoc(String codigoDocumento, String[] params) {

        byte[] documentoExtraido;

        try {
            log.debug("LLAMAMOS AL SERVICIO WEB DE FIRMADOC");
            SWFirmaDocManager manager = SWFirmaDocManager.getInstance(params);
            documentoExtraido = manager.obtenerDocumento(codigoDocumento);
            return documentoExtraido;
        } catch (WSException e) {
            e.printStackTrace();
            return null;
        }

    }
    
}
