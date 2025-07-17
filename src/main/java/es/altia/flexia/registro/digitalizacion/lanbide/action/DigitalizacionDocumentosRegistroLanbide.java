package es.altia.flexia.registro.digitalizacion.lanbide.action;

import com.google.gson.Gson;
import es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.sge.manager.visorregistro.VisorRegistroManager;
import es.altia.agora.interfaces.user.web.registro.MantAnotacionRegistroForm;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.DigitalizacionDocumentosLanbideManager;
import es.altia.flexia.registro.digitalizacion.lanbide.util.DocumentoCatalogacionConversor;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.DigitalizacionDocumentosLanbideVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.DocumentoCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.MetadatoCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.TipoDocumentalCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GrupoTipDocVO;
import es.altia.util.conexion.BDException;
import es.altia.util.persistance.GeneralValueObject;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import java.util.*;

public class DigitalizacionDocumentosRegistroLanbide extends DispatchAction {
    private Logger log = Logger.getLogger(DigitalizacionDocumentosRegistroLanbide.class);
    
    public ActionForward obtenerParametrosDigitalizacion(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.info("================= DigitalizacionDocumentosRegistroLanbide : obtenerParametrosDigitalizacion() ======================>");
        
        HttpSession session = request.getSession();
        String sessionID = (session!=null?session.getId():"");
        log.info("sessionID: "+sessionID);
        Config lanbideProp = ConfigServiceHelper.getConfig("Lanbide");
        DigitalizacionDocumentosLanbideVO respuesta = new DigitalizacionDocumentosLanbideVO();
        UsuarioValueObject usuario = null;
        int codigoUsuario = -1;
        String[] params = null;
        String digitalizacionUrlServicio = null;
        String digitalizacionRegistroTipo = null;
        String digitalizacionRegistroApp = null;
        
        try {
            if(session.getAttribute("usuario")!=null){
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuario.getParamsCon();

                try {
                    codigoUsuario = Integer.parseInt(request.getParameter("codUsu"));
                } catch (NumberFormatException nfe){
                    log.error("El codigo de usuario no es válido",nfe);
                    codigoUsuario = -1;
                }
                log.info(sessionID+" codigoUsuario:"+codigoUsuario);

                if(codigoUsuario != -1 && codigoUsuario == usuario.getIdUsuario()){
                    try{
                        digitalizacionRegistroTipo = lanbideProp.getString(usuario.getOrgCod() + "/DIGITALIZACION_REGISTRO_TIPO");
                        digitalizacionRegistroApp = lanbideProp.getString(usuario.getOrgCod() + "/DIGITALIZACION_REGISTRO_APP");
                        digitalizacionUrlServicio = lanbideProp.getString(usuario.getOrgCod() + "/DIGITALIZACION_URL_SERVICIO");
                    }catch(Exception e){
                        log.error("Se ha producido un error recuperando las propiedades referentes a la digitalización de documentos de Lanbide.properties");
                    }
                    respuesta.setTipo(digitalizacionRegistroTipo);
                    respuesta.setAplicacion(digitalizacionRegistroApp); 
                    respuesta.setUrlServicio(digitalizacionUrlServicio);

                    String auditUser = DigitalizacionDocumentosLanbideManager.getInstance().obtenerUsuario(codigoUsuario, params);
                    respuesta.setAuditUser(auditUser);
                    log.info(sessionID+"-"+codigoUsuario+"-"+" digitalizacionRegistroTipo,digitalizacionRegistroApp,auditUser : " + digitalizacionRegistroTipo+","+digitalizacionRegistroApp+","+auditUser);

                    String claveIdioma = IdiomasManager.getInstance().getClaveIdioma(params, usuario.getIdioma());
                    if (claveIdioma != null && !claveIdioma.isEmpty()) {
                        claveIdioma = claveIdioma.toLowerCase();
                    } else {
                        claveIdioma = ConstantesDatos.CLAVE_LOCALE_CASTELLANO;
                    }
                    respuesta.setIdioma(claveIdioma);
                            
                    // devolvemos los datos como String en formato json
                    retornarJSON(new Gson().toJson(respuesta), response);
                }
            }
        } catch (Exception ex){
            log.error("Ha ocurrido un error en obtenerParametrosDigitalizacion(): " + ex.getMessage(),ex);
        }
        
        return null;
    }
    
    public ActionForward recuperarDocumentos(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.info("================= DigitalizacionDocumentosRegistroLanbide : recuperarDocumentos() ======================>");
        
        HttpSession session = request.getSession();
        String sessionID = (session!=null?session.getId():"");
        log.info("sessionID: "+sessionID);
        Config lanbideProp = ConfigServiceHelper.getConfig("Lanbide");
        UsuarioValueObject usuario = null;
        int codigoUsuario = -1;
        String[] params = null;
        Vector documentos=new Vector();
        
         
        try {
            
               if(session.getAttribute("usuario")!=null){ 
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuario.getParamsCon();
                RegistroValueObject regBusqueda = new RegistroValueObject();
                 
                String ejercicio = request.getParameter("ejercicio");
                String numero = request.getParameter("numero");
                long numReg= Long.parseLong(numero);
                log.info(sessionID+" ejercicio: " + ejercicio);
                log.info(sessionID+" numReg: " + numReg);
                
                regBusqueda.setIdentDepart(usuario.getDepCod());
                regBusqueda.setUnidadOrgan(0);
                regBusqueda.setTipoReg("E");
                regBusqueda.setAnoReg(Integer.parseInt(ejercicio));
                regBusqueda.setNumReg(numReg);
        
                documentos=AnotacionRegistroManager.getInstance().getListaDocumentos(regBusqueda, params);
                log.info(sessionID+" documentos.size()" + documentos.size());
                                  
                MantAnotacionRegistroForm mantARForm = (MantAnotacionRegistroForm)session.getAttribute("MantAnotacionRegistroForm");
                mantARForm.setListaDocsAsignados(documentos);
                session.setAttribute("MantAnotacionRegistroForm", mantARForm);
                
                
                for (int i=0;i<documentos.size();i++){
                    RegistroValueObject registro = new RegistroValueObject();
                    registro=(RegistroValueObject)documentos.get(i);
                    log.info(sessionID+ " documentos: " + registro.getNombreDoc());
                    
                }
         
                retornarJSON(new Gson().toJson(documentos), response); 
                
               }
            
        } catch (Exception ex){
            log.error("Ha ocurrido un error en recuperarDocumentos(): " + ex.getMessage(),ex);
        }
        
        return null;
    }    
    
    public ActionForward recuperarDocumentosConsulta(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.info("================= DigitalizacionDocumentosRegistroLanbide : recuperarDocumentosConsulta() ======================>");
        
        HttpSession session = request.getSession();
        String sessionID = (session!=null?session.getId():"");
        log.info("sessionID: "+sessionID);
        UsuarioValueObject usuario = null;
        String[] params = null;
        Vector documentosAux = null;
        DocumentoValueObject[] documentos = null;
         
        try {
               if(session.getAttribute("usuario")!=null){ 
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    params = usuario.getParamsCon();
                    RegistroValueObject regBusqueda = new RegistroValueObject();

                    String ejercicio = request.getParameter("ejercicio");
                    String numero = request.getParameter("numero");
                    String uor = request.getParameter("uor");
                    long numReg= Long.parseLong(numero);
                    log.info(sessionID+" ejercicio: " + ejercicio);
                    log.info(sessionID+" numReg: " + numReg);

                    regBusqueda.setIdentDepart(usuario.getDepCod());
                    regBusqueda.setUnidadOrgan(Integer.parseInt(uor));
                    regBusqueda.setTipoReg("E");
                    regBusqueda.setAnoReg(Integer.parseInt(ejercicio));
                    regBusqueda.setNumReg(numReg);
        
                    documentosAux = AnotacionRegistroManager.getInstance().getListaDocumentos(regBusqueda, params);
                    documentos = DocumentoCatalogacionConversor.toDocumentoVOArray(documentosAux);
                    
                    log.info(sessionID+" Documentos: ");
                    for (int i=0;i<documentos.length;i++){
                        DocumentoValueObject doc = documentos[i];
                        log.info(sessionID+ " - " + doc.getNombre());
                    }
                    
                     retornarJSON(new Gson().toJson(documentos), response); 
               }
        } catch (Exception ex){
            log.error("Ha ocurrido un error en recuperarDocumentosConsulta(): " + ex.getMessage(),ex);
        }
        
        return null;
    }
    
    public ActionForward cargarPantallaCatalogar(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.info("================= DigitalizacionDocumentosRegistroLanbide : cargarPantallaCatalogar() ======================>");
        
        HttpSession session = request.getSession();
        String sessionID = (session!=null?session.getId():"");
        log.info("sessionID: "+sessionID);
        UsuarioValueObject usuario = null;
        String[] params = null;
        List<TipoDocumentalCatalogacionVO> listaTipos = null;
        List<GrupoTipDocVO> listaGrupos = null;
        DigitalizacionDocumentosLanbideManager digitalizacionManager = DigitalizacionDocumentosLanbideManager.getInstance();
        GeneralValueObject respuesta = null;
        String docCatalogado = "NO";
        String resultado = "-1";
        
        //String codProcedimiento = null;
        
         String numReg;
         String ejercicio;
         String uorRegistro; 
         String codDepartamento;
         String tipoRegOrigen;
         String cod_uor;
         Vector documentosAux;
         DocumentoValueObject[] documentos = null;
         RegistroValueObject regBusqueda = new RegistroValueObject();
        try {
            if(session.getAttribute("usuario") != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuario.getParamsCon();
            
            numReg = request.getParameter("numero");
            ejercicio = request.getParameter("ejercicio");
            uorRegistro = request.getParameter("uorRegistro");  //uor registro
            codDepartamento = request.getParameter("codDepartamento");
            tipoRegOrigen = request.getParameter("tipoRegOrigen");  // Tipo registro de origen: REC,SGE,...
            
            //si se viene desde la ficha del expediente hay que recuperar el tipo registro origen 
            if(tipoRegOrigen==null || tipoRegOrigen.equals("")){
                 String numeroExpediente = request.getParameter("numeroExpediente");
                 String expedienteHistorico = request.getParameter("expedienteHistorico");
                  es.altia.agora.business.util.GeneralValueObject gVO = new es.altia.agora.business.util.GeneralValueObject();
                  gVO.setAtributo("numero",numeroExpediente);
                  gVO.setAtributo("expHistorico",expedienteHistorico);
                  
                  HashMap resultadoConsulta = VisorRegistroManager.getInstance().cargaListaAsientosExpediente(gVO, usuario, params);                
                  ArrayList<AsientoFichaExpedienteVO> asientos = (ArrayList<AsientoFichaExpedienteVO>) resultadoConsulta.get("resultados");
                  
                  for(AsientoFichaExpedienteVO asiento: asientos){
                      if(Long.parseLong(numReg)==asiento.getNumeroAsiento() && Integer.parseInt(ejercicio)==asiento.getEjercicioAsiento()){
                      
                         tipoRegOrigen = asiento.getOrigenAsiento();
                         break;
                      }
                   }
                  
            }
                  
            cod_uor = request.getParameter("cod_uor");   // unidad tramitadora destino 
            
            log.info(sessionID+" numero "+numReg);
            log.info(sessionID+" ejercicio "+ejercicio);
            log.info(sessionID+" uorRegistro "+uorRegistro);
            log.info(sessionID+" codDepartamento "+codDepartamento);
            log.info(sessionID+" tipoRegOrigen "+tipoRegOrigen);
            log.info(sessionID+" cod_uor "+cod_uor);
            
            

             regBusqueda.setIdentDepart(Integer.parseInt(codDepartamento));
             regBusqueda.setUnidadOrgan(Integer.parseInt(uorRegistro));
             regBusqueda.setTipoReg("E");
             regBusqueda.setAnoReg(Integer.parseInt(ejercicio));
             regBusqueda.setNumReg(Long.parseLong(numReg));
             regBusqueda.setTipoRegOrigen(tipoRegOrigen);
             regBusqueda.setCodUORDestBD(cod_uor); 
       
             documentosAux = AnotacionRegistroManager.getInstance().getListaDocumentos(regBusqueda, params);
             log.info(sessionID+" documentosAux.size() " + documentosAux.size());
             // Convertimos el vector de RegistroValueObject en DocumentoValueObject[]
              documentos = DocumentoCatalogacionConversor.toDocumentoVOArray(documentosAux);
             log.info(sessionID+" Convsersion a array documentos.size() " + documentos.length);
              
            //se añade: si hay algún documento tipificado se muestran todos los tipos documentales
            //boolean hayDocumentosTipificados = false;
              
              //#317119 : Los documentos sin contenido no se envian a la interfaz de catalogacion
              log.debug("Nº Documentos iniciales " +  documentos.length);
              List<DocumentoValueObject> documentosNuevo = new ArrayList<DocumentoValueObject>();
              // Reseteamos la variable cont para hacer referencia al insert
              for(int i=0;i<documentos.length;i++){
                  if (documentos[i].getExtension()!=null && !"".equals(documentos[i].getExtension())){
                      log.debug("Existe extension para doc " + documentos[i].getNombre() +": "+ documentos[i].getExtension());
                      documentosNuevo.add(documentos[i]);
                      
                  }else
                      log.info(sessionID+" No viene extension para doc " + documentos[i].getNombre() +": "+ documentos[i].getExtension());


                //se añade: si hay algún documento tipificado se muestran todos los tipos documentales
                /*if (documentos[i].getCatalogado().equals("SI")){
                    hayDocumentosTipificados = true;
                }*/
              }
              log.info(sessionID+" Despues de descartar los docs sin extension, se cargan :" + documentosNuevo.size());
              
              request.setAttribute("listaDocumentos", documentosNuevo.toArray(new DocumentoValueObject[documentosNuevo.size()]));
              request.setAttribute("datosAnotacion", regBusqueda);
           
                //codProcedimiento = request.getParameter("codProcedimiento");
                
                //se añade: si hay algún documento tipificado se muestran todos los tipos documentales (se cambia, siempre los del procedimiento)
                /*if(codProcedimiento!=null && !codProcedimiento.equals("") && !hayDocumentosTipificados){
                    listaTipos = digitalizacionManager.getTipDocCatalogacionProcedimiento(codProcedimiento, params);
                } else{
                    listaTipos = digitalizacionManager.getTipDocCatalogacion(params);
                }*/
                
                listaTipos = digitalizacionManager.getTipDocCatalogacionProcedimiento(ejercicio, numReg, params);
                
                request.setAttribute("listaTiposDocumentales", listaTipos);
                
                //grupos de tipos documentales
                listaGrupos = digitalizacionManager.getGruposTipDoc(params);
                request.setAttribute("listaGruposTiposDocumentales", listaGrupos);
                
                // primer elemento seleccionado 
                DocumentoCatalogacionVO docCatalogar = new DocumentoCatalogacionVO();
                docCatalogar.setDepartamento(regBusqueda.getIdentDepart());
                docCatalogar.setUnidadOrg(regBusqueda.getUnidadOrgan());
                docCatalogar.setTipoAnot(regBusqueda.getTipoReg());
                docCatalogar.setEjercicio(regBusqueda.getAnoReg());
                docCatalogar.setNumeroAnot(regBusqueda.getNumReg());
                
                for(int i=0; i<documentos.length; i++){
                    if(documentos[i].getCatalogado().equals("NO")){
                        docCatalogar.setNomDocumento(documentos[i].getNombre());     
                        break;
                    }
                }
                
                if(docCatalogar.getNomDocumento()==null || docCatalogar.getNomDocumento().equals("")){
                    docCatalogar.setNomDocumento(documentos[0].getNombre());
                    respuesta = recuperarDatosCatalogacionDoc(usuario.getDepCod(), docCatalogar, digitalizacionManager, params);
                    if(respuesta.getAtributo("datos") != null) {
                        request.setAttribute("datosCatalogacionDoc", respuesta.getAtributo("datos"));
                    }
                }
                log.info(sessionID+" documentoSeleccionado : "+docCatalogar.getNomDocumento());
                request.setAttribute("documentoSeleccionado",docCatalogar.getNomDocumento());
                
            }
       } catch (BDException ex){
            log.error("Ha ocurrido un error en cargarPantallaCatalogar(): " + ex.getMessage(),ex);
            resultado = "4";
        } catch (SQLException ex){
            log.error("Ha ocurrido un error en cargarPantallaCatalogar(): " + ex.getMessage(),ex);
            resultado = "7";
            
        } catch (Exception ex){
            log.error("Ha ocurrido un error en cargarPantallaCatalogar(): " + ex.getMessage(),ex);
        } finally {
            if(resultado.equals("-1") && docCatalogado.equals("NO")){
                resultado = "0";
            } else if(docCatalogado.equals("SI")){
                String resOp = (String) respuesta.getAtributo("resultadoOp");
                resultado = resOp;
            } 
            request.setAttribute("resultadoOp", resultado);
            log.info(sessionID+" resultadoOp"+resultado);
        } 
        
        return mapping.findForward(request.getParameter("opcion"));
    }
    
    public ActionForward cargarPantallaTerminarDigitalizar(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("================= DigitalizacionDocumentosRegistroLanbide : cargarPantallaTerminarDigitalizar() ======================>");

        return mapping.findForward(request.getParameter("opcion"));
    }
    
    public ActionForward recuperarMetadosPorTipoDoc(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.info("================= DigitalizacionDocumentosRegistroLanbide : recuperarMetadosPorTipoDoc() ======================>");
        
        HttpSession session = request.getSession();
        String sessionID = (session!=null?session.getId():"");
        log.info("sessionID: "+sessionID);
        UsuarioValueObject usuario = null;
        String[] params = null;
        List<MetadatoCatalogacionVO> listaMetadatos = null;
        DigitalizacionDocumentosLanbideManager digitalizacionManager = DigitalizacionDocumentosLanbideManager.getInstance();
        String tipoDocumental;
        String resultado = "-1";
        GeneralValueObject resp = new GeneralValueObject();
         
        try {
            if(session.getAttribute("usuario")!=null){ 
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuario.getParamsCon();
                tipoDocumental = request.getParameter("codTipoDoc");

                listaMetadatos = digitalizacionManager.getMetadatosCatalogByTipDoc(Integer.parseInt(tipoDocumental), params);
                resultado = "0";
            }
        } catch (BDException bde) {
            log.error("Ha ocurrido un error en recuperarMetadosPorTipoDoc(): " + bde.getMessage(),bde);
            resultado = "4";
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error en recuperarMetadosPorTipoDoc(): " + sqle.getMessage(),sqle);
            resultado = "9";
        } catch (Exception ex){
            log.error("Ha ocurrido un error en recuperarMetadosPorTipoDoc(): " + ex.getMessage(),ex);
        } finally {
            // devolvemos los datos como String en formato json
            resp.setAtributo("datos", listaMetadatos);
            resp.setAtributo("codError", resultado);
            log.info(sessionID+" resultado: " + resultado);
            retornarJSON(new Gson().toJson(resp), response);
        }    
        
        return null;
    }
    
     public ActionForward cargarTodosTipos(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
         log.info("======================= DigitalizacionDocumentosRegistroLanbide : cargarTodosTipos() ==========================>");
         
         HttpSession session = request.getSession();
         String sessionID = (session!=null?session.getId():"");
         log.info("sessionID: "+sessionID);
         UsuarioValueObject usuario = null;
         String[] params = null;
         String resultado = "-1";
         List<TipoDocumentalCatalogacionVO> listaTipos = null;
         List<GrupoTipDocVO> listaGrupos = null;
         GeneralValueObject resp = new GeneralValueObject();
         DigitalizacionDocumentosLanbideManager digitalizacionManager = DigitalizacionDocumentosLanbideManager.getInstance();
         
         try {
             
            if(session.getAttribute("usuario") != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuario.getParamsCon();
                listaTipos = digitalizacionManager.getTipDocCatalogacion(params);
                request.setAttribute("listaTiposDocumentales", listaTipos);
                
                //grupos de tipos documentales
                listaGrupos = digitalizacionManager.getGruposTipDoc(params);
                request.setAttribute("listaGruposTiposDocumentales", listaGrupos);
                
                resultado = "0";
            }    
          } catch (BDException ex){
            log.error("Ha ocurrido un error en cargarPantallaCatalogar(): " + ex.getMessage(),ex);
            resultado = "4";
        } catch (SQLException ex){
            log.error("Ha ocurrido un error en cargarPantallaCatalogar(): " + ex.getMessage(),ex);
            resultado = "7";
        } catch (Exception ex){
            log.error("Ha ocurrido un error en cargarPantallaCatalogar(): " + ex.getMessage(),ex);
        } finally {
            
             // devolvemos los datos como String en formato json
            resp.setAtributo("datos", listaTipos);
            resp.setAtributo("codError", resultado);
            log.info(sessionID+" resultado: "+resultado);
            retornarJSON(new Gson().toJson(resp), response);
            
        } 
        
        return null;
     }
     
     public ActionForward cargarTiposProcedimiento(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
         log.info("======================= DigitalizacionDocumentosRegistroLanbide : cargarTiposProcedimiento() ==========================>");
         
         HttpSession session = request.getSession();
         String sessionID = (session!=null?session.getId():"");
         log.info("sessionID: "+sessionID);
         UsuarioValueObject usuario = null;
         String[] params = null;
         String resultado = "-1";
         List<TipoDocumentalCatalogacionVO> listaTipos = null;
         List<GrupoTipDocVO> listaGrupos = null;
         GeneralValueObject resp = new GeneralValueObject();
         DigitalizacionDocumentosLanbideManager digitalizacionManager = DigitalizacionDocumentosLanbideManager.getInstance();
         
         String ejercicio = null;
         String nRegistro = null;
         
         try {
             
             ejercicio = request.getParameter("ejercicio");
             nRegistro = request.getParameter("nRegistro");
             
            if(session.getAttribute("usuario") != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuario.getParamsCon();
                listaTipos = digitalizacionManager.getTipDocCatalogacionProcedimiento(ejercicio, nRegistro, params);
                request.setAttribute("listaTiposDocumentales", listaTipos);
                
                //grupos de tipos documentales
                listaGrupos = digitalizacionManager.getGruposTipDoc(params);
                request.setAttribute("listaGruposTiposDocumentales", listaGrupos);
                
                resultado = "0";
            }    
          } catch (BDException ex){
            log.error("Ha ocurrido un error en cargarPantallaCatalogar(): " + ex.getMessage(),ex);
            resultado = "4";
        } catch (SQLException ex){
            log.error("Ha ocurrido un error en cargarPantallaCatalogar(): " + ex.getMessage(),ex);
            resultado = "7";
        } catch (Exception ex){
            log.error("Ha ocurrido un error en cargarPantallaCatalogar(): " + ex.getMessage(),ex);
        } finally {
            
             // devolvemos los datos como String en formato json
            resp.setAtributo("datos", listaTipos);
            resp.setAtributo("codError", resultado);
            log.info(sessionID+" resultado: "+resultado);
            retornarJSON(new Gson().toJson(resp), response);
            
        } 
        
        return null;
     }
    
     public ActionForward recuperarMetadatosDocumento(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.info("================= DigitalizacionDocumentosRegistroLanbide : recuperarMetadatosDocumento() ======================>"); 
        
        HttpSession session = request.getSession();
        String sessionID = (session!=null?session.getId():"");
        log.info("sessionID: "+sessionID);
        UsuarioValueObject usuario = null;
        String[] params = null;
        DigitalizacionDocumentosLanbideManager digitalizacionManager = DigitalizacionDocumentosLanbideManager.getInstance();
        GeneralValueObject respuesta = null;
        DocumentoCatalogacionVO docCatalogar = new DocumentoCatalogacionVO();
        String resultado = "-1";
        
        try {
            if(session.getAttribute("usuario")!=null){ 
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuario.getParamsCon();
                
                // Damos valores comunes a las propiedades del objeto DocumentoCatalogacionVO
                docCatalogar.setDepartamento(usuario.getDepCod());
                docCatalogar.setUnidadOrg(Integer.parseInt(request.getParameter("uorDoc")));
                docCatalogar.setTipoAnot(request.getParameter("tipo"));
                docCatalogar.setEjercicio(Integer.parseInt(request.getParameter("ejercicio")));
                docCatalogar.setNumeroAnot(Long.parseLong(request.getParameter("numero")));
                docCatalogar.setNomDocumento(request.getParameter("nombreDoc"));
                
                //Se recuperan los metadatos del documento
                respuesta = recuperarDatosCatalogacionDoc(usuario.getDepCod(), docCatalogar, digitalizacionManager, params);
                        if(respuesta.getAtributo("datos") != null) {
                            respuesta.setAtributo("datosCatalogacionDoc", respuesta.getAtributo("datos"));
                            resultado="0";
                        }
            }
        }catch(Exception e){
                 log.error("Ha ocurrido un error en recuperarMetadatosDocumento(): La operación ha fallado.",e);
        } finally {
            // devolvemos los datos como String en formato json
            respuesta.setAtributo("resultadoOp", resultado);
            log.info(sessionID+" resultado: "+resultado);
            retornarJSON(new Gson().toJson(respuesta), response);
        }
        
        return null;
     }
     
    public ActionForward grabarCatalogacionDocumento(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.info("================= DigitalizacionDocumentosRegistroLanbide : grabarCatalogacionDocumento() ======================>");
        
        HttpSession session = request.getSession();
        String sessionID = (session!=null?session.getId():"");
        log.info("sessionID: "+sessionID);
        UsuarioValueObject usuario = null;
        String[] params = null;
        DigitalizacionDocumentosLanbideManager digitalizacionManager = DigitalizacionDocumentosLanbideManager.getInstance();
        DocumentoCatalogacionVO docCatalogar = new DocumentoCatalogacionVO();
        RegistroValueObject regVO = new RegistroValueObject();
        GeneralValueObject respuesta = new GeneralValueObject();
        String catalogacion;
        String tipoDocumental = null;
        String[] listaMetadatosAux = null;
        ArrayList<String> listaMetadatos = new ArrayList<String>();
        String resultado = "-1";
         Vector documentosAux;
         DocumentoValueObject[] documentos = null;
         
        try {
            if(session.getAttribute("usuario")!=null){ 
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                params = usuario.getParamsCon();
                
                // Damos valores comunes a las propiedades del objeto DocumentoCatalogacionVO
                docCatalogar.setDepartamento(usuario.getDepCod());
                docCatalogar.setUnidadOrg(Integer.parseInt(request.getParameter("uorDoc")));
                docCatalogar.setTipoAnot(request.getParameter("tipo"));
                docCatalogar.setEjercicio(Integer.parseInt(request.getParameter("ejercicio")));
                docCatalogar.setNumeroAnot(Long.parseLong(request.getParameter("numero")));
                docCatalogar.setNomDocumento(request.getParameter("nombreDoc"));
                docCatalogar.setIdDocumento(Long.parseLong(request.getParameter("idDoc")));
                docCatalogar.setObservDoc(request.getParameter("observDoc"));
                
                // Obtenemos los distintos metadatos con los que catalogar el documento
                catalogacion = request.getParameter("catalogacion");
              
                // Obtenemos el tipo documental
                tipoDocumental = request.getParameter("tipoDocumental");
                log.info(sessionID+" tipoDocumental: "+tipoDocumental);
                if(catalogacion != null) { 
                    catalogacion = URLDecoder.decode(catalogacion, "ISO_8859-1");
                    listaMetadatosAux = catalogacion.split(ConstantesDatos.SEPARADORAJAX);
                    for(int index=0; index<listaMetadatosAux.length; index++){
                        String metadato = listaMetadatosAux[index].trim();
                        if(!metadato.equals("")){
                            listaMetadatos.add(metadato);
                        }
                    }
                }
                
                boolean exito = digitalizacionManager.grabarCatalogacionDocumento(docCatalogar, listaMetadatos,tipoDocumental, params);
                log.info(sessionID+" grabarCatalogacionDocumento exito: "+exito);
                if(exito){
                    resultado = "0";
                    regVO.setAnoReg(Integer.parseInt(request.getParameter("ejercicio")));
                    regVO.setNumReg(Long.parseLong(request.getParameter("numero")));
                    regVO.setIdentDepart(usuario.getDepCod());
                    regVO.setUnidadOrgan(Integer.parseInt(request.getParameter("uorDoc")));
                    regVO.setTipoReg(request.getParameter("tipo"));
                    regVO.setTipoRegOrigen(request.getParameter("tipoRegOrigen"));
                    regVO.setCodUORDestBD(request.getParameter("codUORDestino")); 
                    regVO.setObservDoc(request.getParameter("observDoc"));
                    
                    documentosAux = AnotacionRegistroManager.getInstance().getListaDocumentos(regVO, params);
                    // Convertimos el vector de RegistroValueObject en DocumentoValueObject[]
                    documentos = DocumentoCatalogacionConversor.toDocumentoVOArray(documentosAux);
                    log.info(sessionID+" lista documentosAux.size(): "+documentosAux.size());
                    log.info(sessionID+" lista documentos.size() convertido array : "+documentosAux.size());
                    //#317119 : Los documentos sin contenido no se envian a la interfaz de catalogacion
                    log.debug("Nº Documentos iniciales " +  documentos.length);
                    List<DocumentoValueObject> documentosNuevo = new ArrayList<DocumentoValueObject>();
                    // Reseteamos la variable cont para hacer referencia al insert
                    for(int i=0;i<documentos.length;i++){
                        if (documentos[i].getExtension()!=null && !"".equals(documentos[i].getExtension())){
                            log.debug("Existe extension para doc " + documentos[i].getNombre() +": "+ documentos[i].getExtension());
                            documentosNuevo.add(documentos[i]);
                        }
                    }
                    
                    docCatalogar.setNomDocumento("");
                    //se busca el siguiente documento sin catalogación
                     for(int i=0; i<documentosNuevo.size(); i++){
                         if(documentosNuevo.get(i).getCatalogado().equals("NO")){
                            docCatalogar.setNomDocumento(documentosNuevo.get(i).getNombre());     
                            break;
                        }
                    }
                     
                    //si no hay documentos sin catalogar se queeda en el que esta 
                    if(docCatalogar.getNomDocumento()==null || docCatalogar.getNomDocumento().equals("")){
                        docCatalogar.setNomDocumento(request.getParameter("nombreDoc"));
                        respuesta = recuperarDatosCatalogacionDoc(usuario.getDepCod(), docCatalogar, digitalizacionManager, params);
                        if(respuesta.getAtributo("datos") != null) {
                            respuesta.setAtributo("datosCatalogacionDoc", respuesta.getAtributo("datos"));
                            
                        }
                    }
                    log.info(sessionID+" Documento que aparece seleccionado al abrir la ventana de catalogacion: "+docCatalogar.getNomDocumento());
                    respuesta.setAtributo("documentoSeleccionado",docCatalogar.getNomDocumento());
                    respuesta.setAtributo("listaDocumentos", documentosNuevo.toArray(new DocumentoValueObject[documentosNuevo.size()]));
                    respuesta.setAtributo("datosAnotacion", regVO);
                    
                    
                }
            }
            
        } catch (NumberFormatException ex){
            log.error("Ha ocurrido un error en grabarCatalogacionDocumento(): El formato de los datos de entrada es incorrecto.",ex);
            resultado = "-2";
        } catch (TechnicalException ex){
            log.error("Ha ocurrido un error en grabarCatalogacionDocumento(): La operación ha fallado.",ex);
        } catch (BDException ex){
            log.error("Ha ocurrido un error en grabarCatalogacionDocumento(): " + ex.getMessage(),ex);
            resultado = "4";
        } catch (SQLException ex){
            log.error("Ha ocurrido un error en grabarCatalogacionDocumento(): " + ex.getMessage(),ex);
            resultado = "5";
        } catch(Lan6Excepcion le){
            log.error("Ha ocurrido un error en grabarCatalogacionDocumento():"+le.getMessage(),le);
            resultado = "11";
        } catch (Exception ex){
            log.error("Ha ocurrido un error en grabarCatalogacionDocumento(): " + ex.getMessage(),ex);
        } finally {
            // devolvemos los datos como String en formato json
            respuesta.setAtributo("resultadoOp", resultado);
            log.info(sessionID+" resultadoOp: "+resultado);
            retornarJSON(new Gson().toJson(respuesta), response);
        }
        
        return null;
    }
    
    public GeneralValueObject recuperarDatosCatalogacionDoc(int departamento, DocumentoCatalogacionVO docCatalogar, DigitalizacionDocumentosLanbideManager digitalizacionManager, String[] params) throws Exception{
        log.info("================= DigitalizacionDocumentosRegistroLanbide : recuperarDatosCatalogacionDoc() ======================>");
        
        List<DocumentoCatalogacionVO> listado = null;
        GeneralValueObject respuesta = new GeneralValueObject();
        String resultado = "-1";
         
        try {

            listado = digitalizacionManager.recuperarDatosCatalogacionDoc(docCatalogar, params);
            resultado = "0";
        } catch (NumberFormatException ex){
            log.error("Ha ocurrido un error en comprobarDocCatalogado(): El formato de los datos de entrada es incorrecto.",ex);
            resultado = "-2";
        } catch (TechnicalException ex){
            log.error("Ha ocurrido un error en comprobarDocCatalogado(): La operación ha fallado.",ex);
        } catch (BDException ex){
            log.error("Ha ocurrido un error en comprobarDocCatalogado(): " + ex.getMessage(),ex);
            resultado = "4";
        } catch (SQLException ex){
            log.error("Ha ocurrido un error en comprobarDocCatalogado(): " + ex.getMessage(),ex);
            resultado = "6";
        } catch (Exception ex){
            log.error("Ha ocurrido un error en comprobarDocCatalogado(): " + ex.getMessage(),ex);
        } finally {
            // devolvemos los datos como String en formato json
            respuesta.setAtributo("resultadoOp", resultado);
            log.info("resultadoOp: "+resultado);
            if(listado != null)
                respuesta.setAtributo("datos", listado);
        }
        
        return respuesta;
    }
    
    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json, HttpServletResponse response) {

        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            log.error("Error al preparar el JSON para retornar", e);
        }

    }
}
