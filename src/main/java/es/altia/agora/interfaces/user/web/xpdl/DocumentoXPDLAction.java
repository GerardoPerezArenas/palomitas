// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.xpdl;

// PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.persistence.AreasManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.ProcedimientosManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.ErrorImportacionXPDL;
import es.altia.agora.business.sge.ExistenciaUorImportacionTramiteVO;
import es.altia.agora.business.sge.ExistenciaUorImportacionVO;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.business.sge.persistence.ImportacionProcedimientoManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.StringUtils;
import es.altia.util.struts.StrutsFileValidation;
import es.altia.util.xpdl.XPDLToSgeConversor;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;
import javax.xml.validation.*;
import org.xml.sax.SAXException;

public class DocumentoXPDLAction extends ActionSession  {

    private final String MAPPING_SELECCION_UOR = "seleccionUors";
    protected static Config m_CommonProperties = ConfigServiceHelper.getConfig("common");

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    try
    {  
    m_Log.debug("perform"); 
  
    m_Log.debug("================= DocumentoXPDLAction ======================>");    
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    String opcion = request.getParameter("opcion");
    
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);
    
    if (form == null) {
      m_Log.debug("Rellenamos el form de fichero");
      form = new DocumentoXPDLForm();
      if ("request".equals(mapping.getScope())){          
        request.setAttribute(mapping.getAttribute(), form);
      }
      else{          
          session.setAttribute(mapping.getAttribute(), form);
      }
    }
    
    DocumentoXPDLForm docXPDLForm = (DocumentoXPDLForm)form;            
    FormFile fichero = null;
    if(docXPDLForm!=null && docXPDLForm.getFichero()!=null)
        fichero = docXPDLForm.getFichero();       
    
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    m_Log.debug("DocumentoXPDLAction - DocumentoXPDLForm es: " + docXPDLForm);

    if("documentoNuevo".equals(opcion)){
        docXPDLForm.setTituloModificando("");
        docXPDLForm.setTituloFichero("");
        opcion="documentoNuevo";
    } 
    else 
    if("insertarProcedimiento".equals(opcion)){                
        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
        traductor.setApl_cod(usuario.getAppCod());
        traductor.setIdi_cod(usuario.getIdioma());
        byte[] contenido = fichero.getFileData();
        request.removeAttribute("error_importacion_xpdl");
        
        Calendar fecha = Calendar.getInstance();
        SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyyHHmmss");

        File ftemporal = File.createTempFile("xpdl_importacion" + sf.format(fecha.getTime()) , ".xpdl");
        m_Log.debug(" ==========> DocumentoXPDLAction - path del fichero a importar : " + ftemporal.getAbsolutePath());

        FileOutputStream fos = new FileOutputStream(ftemporal);
        fos.write(contenido);
        fos.flush();
        fos.close();            
        
//        try {
//            URL urlSchemaFile = new URL("http://www.xpdl.org/standards/xpdl-2.1/bpmnxpdl_31a.xsd");
//            BufferedReader lectura = new BufferedReader(new InputStreamReader(urlSchemaFile.openStream()));
//            String linha;
//            StringBuilder texto = new StringBuilder();
//            while ((linha = lectura.readLine()) != null){
//                if (linha.indexOf("deprecated:") == -1)
//                    texto.append(linha);
//            }
//            lectura.close();
//
//            Source src = new StreamSource(new java.io.StringReader(texto.toString()));
//            
//            Source xmlFile = new StreamSource(ftemporal);
//            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            Schema schema = schemaFactory.newSchema(src);
//            Validator validator = schema.newValidator();
//            try {
//                validator.validate(xmlFile);
//                m_Log.debug(" fichero válido ");
//            } catch (SAXException e) {
//                ArrayList<ErrorImportacionXPDL> errorFichero = new  ArrayList<ErrorImportacionXPDL>();
//                errorFichero.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errFormatoXPDL") + ": " + e.getMessage()));
//                session.setAttribute("erroresXPDL",errorFichero);
//                opcion = "actualizacionProcedimientoNoPosible";
//                return mapping.findForward(opcion);
//            }
//         }
//         catch(IOException ex) {
//            m_Log.debug(" Error validando XPDL: " + ex.getMessage());
//         }
        
        // Se extrae el procedimiento del documento XPDL en forma de los objetos del modelo utilizados por FLEXIA.
        Vector resultado = XPDLToSgeConversor.getInstance().procedimientoFromXPDL(ftemporal,usuario.getOrgCod(),params);    

        /****************** SE COMPRUEBA LA EXISTENCIAS DE LAS UNIDADES DE INICIO DEL PROCEDIMIENTO SI LAS HAY ********************************/
        // Se comprueba la existencias de las unidades de inicio del procedimiento si las hay.
        for(int i=0;i<resultado.size();i++){ // En el XPDL viene un solo procedimiento pero podría haber más de uno
            DefinicionProcedimientosValueObject dfVO = (DefinicionProcedimientosValueObject)resultado.get(i);
            Vector unidadesInicioProcedimiento = dfVO.getListaCodUnidadInicio();
            
            boolean existeProcedimiento = ImportacionProcedimientoManager.getInstance().existeProcedimiento(dfVO.getTxtCodigo(), params);
            if(existeProcedimiento){
                m_Log.debug(" existeProcedimiento: " + existeProcedimiento);
                
                // Se comprueba si alguno de los trámites ya existentes no están en la nueva definición. Para cada uno de esos trámites habrá que comprobar si
                // hay expedientes pendientes de tramitar para alguno de esos trámites, porque en ese caso no se puede realizar la actualización del procedimiento y, se procede a 
                // informar al usuario.
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                
               ArrayList<ErrorImportacionXPDL> errores = ImportacionProcedimientoManager.getInstance().tieneExpedientesPendientesTramitar(dfVO, usuarioVO.getIdioma(),usuarioVO.getAppCod(),params);
               if(errores.size()>0){
                    session.setAttribute("erroresXPDL",errores);
                    opcion = "actualizacionProcedimientoNoPosible";
                    return mapping.findForward(opcion);
               }
            }

            ArrayList<ErrorImportacionXPDL> erroresAux = comprobarErrosProc(dfVO,traductor,params);
            
            if (!erroresAux.isEmpty()){
                session.setAttribute("erroresXPDL",erroresAux);
                opcion = "actualizacionProcedimientoNoPosible";
                return mapping.findForward(opcion);
            }
                
            /********** SE COMPRUEBA SI EL USUARIO DEBE SELECCIONAR LAS UNIDADES DE INICIO DEL PROCEDIMIENTO *******************/

            ArrayList<ExistenciaUorImportacionVO> existencia = ImportacionProcedimientoManager.getInstance().getExistenUors(unidadesInicioProcedimiento,dfVO.getParticipantes(), params);
            ArrayList<ExistenciaUorImportacionVO> unidadesProcExisten = new ArrayList<ExistenciaUorImportacionVO>();
            ArrayList<ExistenciaUorImportacionVO> unidadesProcNoExisten = new ArrayList<ExistenciaUorImportacionVO>();

            for(Iterator<ExistenciaUorImportacionVO> it = existencia.iterator();it.hasNext();){
                ExistenciaUorImportacionVO exist = it.next();
                if(exist.isExiste())
                    unidadesProcExisten.add(exist);
                else
                    unidadesProcNoExisten.add(exist);
            }// for

            /** Si el tipo de operación es un alta o una modificación, se redirige el control a una jsp u a otra */
            if(existeProcedimiento){
                request.setAttribute("tipoOperacion","actualizacion");
            }
            else
                request.setAttribute("tipoOperacion","alta");

           // Si hay unidades de inicio del procedimiento que no existen en la organización en la que se realiza la importación
           if(unidadesProcNoExisten.size()>0){

              // Como el procedimiento existe, se recuperan las unidades inicio que pueda tener ya seleccionadas para mostrarselas al usuario y que decida si las mantiene o selecciona unas nuevas
              if(existeProcedimiento){
                  ArrayList<ExistenciaUorImportacionVO> unidades = ImportacionProcedimientoManager.getInstance().getUnidadesInicioProcedimiento(unidadesInicioProcedimiento, dfVO.getTxtCodigo(), params);
                  if(unidades!=null && unidades.size()>0){
                    for(int z=0;z<unidades.size();z++){                        
                        ExistenciaUorImportacionVO uor = unidades.get(z);
                        unidadesProcExisten.add(uor);
                    }
                  }//if
              }//if

              session.setAttribute("definicion_procedimiento_importacion",dfVO);
              session.setAttribute("unidades_proc_existen",unidadesProcExisten);
              session.setAttribute("unidades_proc_no_existen",unidadesProcNoExisten);
              request.setAttribute("redireccion","procedimiento");

              return mapping.findForward(MAPPING_SELECCION_UOR);
           }else{
                session.setAttribute("definicion_procedimiento_importacion",dfVO);
                request.setAttribute("redireccion","tramite");
                // Se comprueba si la existencia de las unidades de inicio de los trámites
                 return mapping.findForward(MAPPING_SELECCION_UOR);
           }         
      } //for
      
     
    } else if("modificar".equals(opcion)){
        String tituloDoc = request.getParameter("tituloDoc");
        docXPDLForm.setTituloModificando(tituloDoc);
        docXPDLForm.setTituloFichero(tituloDoc);
        opcion="documentoNuevo";
    } else if("documentoEliminar".equals(opcion)){
      String codigo = request.getParameter("codigoDocumento");
      docXPDLForm.setCodigo(codigo);
      opcion = "documentoEliminar";
    }else if("insertarDocumento".equals(opcion)){
        
        opcion="insertarDocumento";   
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else
    if("actualizarUnidadesInicioProcedimiento".equals(opcion)){
        
        /** SE ACTUALIZA LA DEFINICIÓN DEL PROCEDIMIENTO CON LA LISTA DE UNIDADES DE INICIO DEL MISMO CON SUS CODIGOS VISIBLES **/
        String sListaCodigosUorsImportacion = docXPDLForm.getListaCodigosVisibleUorsImportacion();
        Vector listaCodigosInternosUor = tratarLista(sListaCodigosUorsImportacion); 

        // Ya se tienen las unidades de inicio del procedimiento
        DefinicionProcedimientosValueObject dfVO = (DefinicionProcedimientosValueObject)session.getAttribute("definicion_procedimiento_importacion");        
        dfVO.setListaCodUnidadInicio(listaCodigosInternosUor);

        // Se actualiza la definición del procedimiento en sesión
        session.setAttribute("definicion_procedimiento_importacion",dfVO);
        // Se eliminan las unidades del procedimiento
        session.removeAttribute("unidades_proc_existen");
        session.removeAttribute("unidades_proc_no_existen");

        return mapping.findForward("actualizarUnidadesTramite");
      
    }else
      if("actualizarUnidadesInicioProcedimientoActualizacion".equals(opcion)){

        /** SE ACTUALIZA LA DEFINICIÓN DEL PROCEDIMIENTO CON LA LISTA DE UNIDADES DE INICIO DEL MISMO CON SUS CODIGOS VISIBLES **/
        String sListaCodigosUorsImportacion = docXPDLForm.getListaCodigosVisibleUorsImportacion();
        Vector listaCodigosInternosUor = tratarLista(sListaCodigosUorsImportacion);

        // Ya se tienen las unidades de inicio del procedimiento
        DefinicionProcedimientosValueObject dfVO = (DefinicionProcedimientosValueObject)session.getAttribute("definicion_procedimiento_importacion");
        dfVO.setListaCodUnidadInicio(listaCodigosInternosUor);

        // Se actualiza la definición del procedimiento en sesión
        session.setAttribute("definicion_procedimiento_importacion",dfVO);
        // Se eliminan las unidades del procedimiento
        session.removeAttribute("unidades_proc_existen");
        session.removeAttribute("unidades_proc_no_existen");

        return mapping.findForward("actualizarUnidadesTramiteActualizacion");

    }else
    if("actualizarUnidadesTramite".equals(opcion)){
        
        String listaCodigosUorsTramitadorasTramite = docXPDLForm.getListaCodigosUorsTramitadorasTramite();        
        String codigoUorInicioManualTramite            = docXPDLForm.getCodigoUorInicioManualTramite();
        String codigoTramite                                   = docXPDLForm.getCodigoTramite();

        Vector sListaCodigosUorsTramitadorasTramite = this.tratarLista(listaCodigosUorsTramitadorasTramite);        

        DefinicionProcedimientosValueObject dfVO = (DefinicionProcedimientosValueObject)session.getAttribute("definicion_procedimiento_importacion");
        Hashtable<String,String> participantes = dfVO.getParticipantes();

        m_Log.debug("ListaCodigosUorsTramitadorasTramite: " + listaCodigosUorsTramitadorasTramite);
        m_Log.debug("codigoUorInicioManualTramite: " + codigoUorInicioManualTramite);
        m_Log.debug("codigoTramite: " + codigoTramite);

        Hashtable<String,String> tramitesTratados = (Hashtable<String,String>)session.getAttribute("Tramites_tratados_importacion");
        if(tramitesTratados==null)
            tramitesTratados =  new Hashtable<String, String>();

        /** Si ya se han seleccionado la lista de unidades tramitadoras de un trámite, se actualiza el trámite correspondiente con dichas unidades tramitadoras */
        if((codigoTramite!=null && !"".equals(codigoTramite)) || sListaCodigosUorsTramitadorasTramite.size()>=1 && (tramitesTratados.get(codigoTramite)==null || "SI".equals(tramitesTratados.get(codigoTramite)))){
            // Se actualiza la lista de unidades tramitadoras del trámite
            if(XPDLToSgeConversor.getInstance().actualizarListaUnidadesTramitadoras(codigoTramite,codigoUorInicioManualTramite,sListaCodigosUorsTramitadorasTramite,dfVO))
                tramitesTratados.put(codigoTramite,"SI");
        }

        session.setAttribute("definicion_procedimiento_importacion",dfVO);
        session.setAttribute("Tramites_tratados_importacion",tramitesTratados);
        
        // Se comprueba si hay que verificar las unidades de inicio de los trámites
        Vector<DefinicionTramitesValueObject> tramites = (Vector<DefinicionTramitesValueObject>)dfVO.getTramites();
        int contador=0;
        boolean insertarProcedimiento = false;
        for(int i=0;i<tramites.size();i++){
           DefinicionTramitesValueObject tramite =  tramites.get(i);
           contador++;

           boolean tramiteTratado =false;
          try{
              String valor = tramitesTratados.get(tramite.getCodigoTramite());
              if(valor.equals("SI")) tramiteTratado = true;
          }catch(Exception e){              
          }
           
           if(!tramiteTratado && tramite.getCodUnidadTramite()!=null && "0".equals(tramite.getCodUnidadTramite())){
               ExistenciaUorImportacionTramiteVO existe = verificarExistenciaUnidadesTramite(tramite, participantes, params,false,dfVO.getTxtCodigo());
               
               if(existe!=null && !existe.isExisten()){
                   // Se redirige el control a la jsp que se encarga de la sección de las uors de trámite
                   opcion = "seleccionUorsTramite";
                   request.setAttribute("ExistenciaUorImportacionTramiteVO",existe);
                   return mapping.findForward(opcion);
               }else{
                    /***/
                        // Todas las uors del trámite existen, se actualiza el trámite dentro de la definición del procedimiento.
                    Vector<UORDTO> uors = tramite.getUnidadesTramitadoras();
                    Vector<UORDTO> aux = new Vector<UORDTO>();

                    String codUnidadInicioManual = tramite.getCodUnidadInicio();
                    UORDTO uorManual = UORsDAO.getInstance().getUORPorCodigoVisible(codUnidadInicioManual, params);
                    if(uorManual!=null)
                        tramite.setCodUnidadInicio(uorManual.getUor_cod());

                    for(int h=0;uors!=null && h<uors.size();h++){
                        String codVisible = uors.get(h).getUor_cod_vis();
                        m_Log.debug("Código de uor visible: " + codVisible);
                        
                        for(int g=0;uors!=null && g<uors.size();g++){

                            UORDTO uor = UORsDAO.getInstance().getUORPorCodigoVisible(codVisible, params);
                            aux.add(uor);
                        }// for

                        tramite.setListaUnidadesTramitadoras(aux);
                    }// for
                   
                      tramitesTratados.put(tramite.getCodigoTramite(),"SI");

                      tramites.remove(i);
                      tramites.add(i,tramite);
                      dfVO.setTramites(tramites);
                      session.setAttribute("Tramites_tratados_importacion",tramitesTratados);
                      session.setAttribute("definicion_procedimiento_importacion",dfVO);
                    /***/
               }
           }//if

           m_Log.debug("========> VALOR DE CONTADOR: " + contador);
           m_Log.debug("========> VALOR DE CONTADOR: " + tramites.size());

           if(contador==tramites.size()){
               // Se han tratado todos los trámites
               insertarProcedimiento = true;
               break;
           }
        }//for

        if(insertarProcedimiento){
            session.removeAttribute("Tramites_tratados_importacion");
            return mapping.findForward("insercionDefinitivaProcedimiento");
        }        
    }else
        /****/
     if("actualizarUnidadesTramiteActualizacion".equals(opcion)){

        String listaCodigosUorsTramitadorasTramite = docXPDLForm.getListaCodigosUorsTramitadorasTramite();
        String codigoUorInicioManualTramite            = docXPDLForm.getCodigoUorInicioManualTramite();
        String codigoTramite                                   = docXPDLForm.getCodigoTramite();

        Vector sListaCodigosUorsTramitadorasTramite = this.tratarLista(listaCodigosUorsTramitadorasTramite);

        DefinicionProcedimientosValueObject dfVO = (DefinicionProcedimientosValueObject)session.getAttribute("definicion_procedimiento_importacion");
        Hashtable<String,String> participantes = dfVO.getParticipantes();

        m_Log.debug("ListaCodigosUorsTramitadorasTramite: " + listaCodigosUorsTramitadorasTramite);
        m_Log.debug("codigoUorInicioManualTramite: " + codigoUorInicioManualTramite);
        m_Log.debug("codigoTramite: " + codigoTramite);

        Hashtable<String,String> tramitesTratados = (Hashtable<String,String>)session.getAttribute("Tramites_tratados_importacion");
        if(tramitesTratados==null)
            tramitesTratados =  new Hashtable<String, String>();

        /** Si ya se han seleccionado la lista de unidades tramitadoras de un trámite, se actualiza el trámite correspondiente con dichas unidades tramitadoras */
        if((codigoTramite!=null && !"".equals(codigoTramite)) || sListaCodigosUorsTramitadorasTramite.size()>=1 && (tramitesTratados.get(codigoTramite)==null || "SI".equals(tramitesTratados.get(codigoTramite)))){
            // Se actualiza la lista de unidades tramitadoras del trámite
            if(XPDLToSgeConversor.getInstance().actualizarListaUnidadesTramitadoras(codigoTramite,codigoUorInicioManualTramite,sListaCodigosUorsTramitadorasTramite,dfVO))
                tramitesTratados.put(codigoTramite,"SI");
        }

        session.setAttribute("definicion_procedimiento_importacion",dfVO);
        session.setAttribute("Tramites_tratados_importacion",tramitesTratados);

        // Se comprueba si hay que verificar las unidades de inicio de los trámites
        Vector<DefinicionTramitesValueObject> tramites = (Vector<DefinicionTramitesValueObject>)dfVO.getTramites();
        int contador=0;
        boolean insertarProcedimiento = false;
        for(int i=0;i<tramites.size();i++){
           DefinicionTramitesValueObject tramite =  tramites.get(i);
           contador++;

           boolean tramiteTratado =false;
          try{
              String valor = tramitesTratados.get(tramite.getCodigoTramite());
              if(valor.equals("SI")) tramiteTratado = true;
          }catch(Exception e){
          }

           if(!tramiteTratado && tramite.getCodUnidadTramite()!=null && "0".equals(tramite.getCodUnidadTramite())){
               ExistenciaUorImportacionTramiteVO existe = verificarExistenciaUnidadesTramite(tramite, participantes, params,true,dfVO.getTxtCodigo());
               if(existe!=null && !existe.isExisten()){
                   // Se redirige el control a la jsp que se encarga de la sección de las uors de trámite
                   opcion = "seleccionUorsTramiteActualizacion";
                   request.setAttribute("ExistenciaUorImportacionTramiteVO",existe);
                   return mapping.findForward(opcion);
               }/** óscar **/
               else{
                    /***/
                        // Todas las uors del trámite existen, se actualiza el trámite dentro de la definición del procedimiento.
                    Vector<UORDTO> uors = tramite.getUnidadesTramitadoras();
                    Vector<UORDTO> aux = new Vector<UORDTO>();

                    String codUnidadInicioManual = tramite.getCodUnidadInicio();
                        UORDTO uorManual = UORsDAO.getInstance().getUORPorCodigoVisible(codUnidadInicioManual, params);
                        if(uorManual!=null)
                            tramite.setCodUnidadInicio(uorManual.getUor_cod());
                        
                    for(int h=0;uors!=null && h<uors.size();h++){
                        String codVisible = uors.get(h).getUor_cod_vis();
                        m_Log.debug("Código de uor visible: " + codVisible);
                      
                        UORDTO uor = UORsDAO.getInstance().getUORPorCodigoVisible(codVisible, params);
                        aux.add(uor);
                        
                        tramite.setListaUnidadesTramitadoras(aux);
                    }// for

                      tramitesTratados.put(tramite.getCodigoTramite(),"SI");

                      tramites.remove(i);
                      tramites.add(i,tramite);
                      dfVO.setTramites(tramites);
                      session.setAttribute("Tramites_tratados_importacion",tramitesTratados);
                      session.setAttribute("definicion_procedimiento_importacion",dfVO);
                    /***/
               }

               /** óscar **/
           }//if

           m_Log.debug("========> VALOR DE CONTADOR: " + contador);
           m_Log.debug("========> VALOR DE CONTADOR: " + tramites.size());

           if(contador==tramites.size()){
               // Se han tratado todos los trámites
               insertarProcedimiento = true;
               break;
           }
        }//for

        if(insertarProcedimiento){
            session.removeAttribute("Tramites_tratados_importacion");
            return mapping.findForward("actualizacionDefinitivaProcedimiento");
        }
    }else
        /****/
    if("insercionDefinitivaProcedimiento".equals(opcion)){
        m_Log.debug(" ================> insercionDefinitivaProcedimiento");
        DefinicionProcedimientosValueObject dfVO = (DefinicionProcedimientosValueObject)session.getAttribute("definicion_procedimiento_importacion");
        session.removeAttribute("definicion_procedimiento_importacion");
        session.removeAttribute("Tramites_tratados_importacion");
        session.removeAttribute("unidades_proc_existen");
        session.removeAttribute("unidades_proc_no_existen");
        
        boolean existeProcedimiento = ImportacionProcedimientoManager.getInstance().existeProcedimiento(dfVO.getTxtCodigo(), params);
        if(!existeProcedimiento){
            boolean exito = ImportacionProcedimientoManager.getInstance().importarProcedimientoNuevo(dfVO, params);
            m_Log.debug("Procedimiento importado con éxito: " + exito);
            if(exito){
                // Se ha importado correctamente el procedimiento, se comprueba los posibles errores que hayan podido ocurrir pero que
                // no impidan dar de alta el procedimiento
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                ArrayList<ErrorImportacionXPDL> errores = new ArrayList<ErrorImportacionXPDL>();
                errores = ImportacionProcedimientoManager.getInstance().verificarErroresImportacionProcedimientoNuevo(dfVO,usuarioVO.getIdioma(),usuarioVO.getAppCod(),params);
                this.anhadeErroresDepartamentoNotificacion(errores,usuarioVO.getAppCod(), usuarioVO.getIdioma(), dfVO);
                this.anhadeErroresAreaProcedimiento(errores, usuarioVO.getAppCod(), usuarioVO.getIdioma(), dfVO);
                if(errores!=null && errores.size()>0){
                    request.setAttribute("erroresXPDL",errores);
                    opcion = "procedimientoImportadoConErrores";
                    return mapping.findForward(opcion);
                }
                else{
                    opcion = "procedimientoImportadoConExito";                
                    return mapping.findForward(opcion);
                }
            }//if
            else{
                opcion =  "errorActualizacionProcedimiento";
                return mapping.findForward(opcion);
            }
        }

    }else
    if("cancelarImportacion".equals(opcion)){
        // Se eliminan las variables de sesión utilizadas para la importación
        session.removeAttribute("definicion_procedimiento_importacion");
        session.removeAttribute("Tramites_tratados_importacion");
        session.removeAttribute("unidades_proc_existen");
        session.removeAttribute("unidades_proc_no_existen");
        opcion = "cancelarImportacion"  ;
        return mapping.findForward(opcion);
    }else
    if("actualizacionDefinitivaProcedimiento".equals(opcion)){
        m_Log.debug(" ================> actualizacionDefinitivaProcedimiento");
        DefinicionProcedimientosValueObject dfVO = (DefinicionProcedimientosValueObject)session.getAttribute("definicion_procedimiento_importacion");

        session.removeAttribute("definicion_procedimiento_importacion");
        session.removeAttribute("Tramites_tratados_importacion");
        session.removeAttribute("unidades_proc_existen");
        session.removeAttribute("unidades_proc_no_existen");
        
        // Existe el procedimiento, entonces hay que hacer los chequeos previos para verificar si hay trámites pendientes que no están en la nueva definición y también
        // comprobar la existencias de los roles de procedimiento        
        boolean procedimientoActualizado = ImportacionProcedimientoManager.getInstance().actualizarProcedimiento(dfVO, usuario.getOrgCod(),params);

        if(procedimientoActualizado){
            // El procedimiento se ha actualizado correctamente
                // Se ha importado correctamente el procedimiento, se comprueba los posibles errores que hayan podido ocurrir pero que
                // no impidan dar de alta el procedimiento
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");

                ArrayList<ErrorImportacionXPDL> errores = new ArrayList<ErrorImportacionXPDL>();
                errores = ImportacionProcedimientoManager.getInstance().verificarErroresImportacionProcedimientoExistente(dfVO,usuarioVO.getIdioma(),usuarioVO.getAppCod(),params);
                this.anhadeErroresDepartamentoNotificacion(errores,usuarioVO.getAppCod(), usuarioVO.getIdioma(), dfVO);
                this.anhadeErroresAreaProcedimiento(errores, usuarioVO.getAppCod(), usuarioVO.getIdioma(), dfVO);
                if(errores!=null && errores.size()>0){
                    request.setAttribute("erroresXPDL",errores);
                    opcion = "procedimientoImportadoConErrores";
                    return mapping.findForward(opcion);
                }
                else{
                    opcion = "procedimientoActualizadoConExito";
                    return mapping.findForward(opcion);
                }
        }else{
            opcion =  "errorActualizacionProcedimiento";
            return mapping.findForward(opcion);
        }
    }
    else{
      opcion = mapping.getInput();
    }   
        
    // Cada vez que se excede el tamaño máximo del fichero a subir por POST el parámetro opcion
    // llega vacío
    //if(opcion==null && fichaForm!=null && (fichaForm.getFichero()==null || (fichaForm.getFichero()!=null && fichaForm.getFichero().getFileSize()==0))){
    if(opcion==null || opcion.equals("")){
        // Se indica que hay error y se vuelve a la página que
        // permite dar de alta un nuevo documento        
        //int num = Math.round(StrutsFileValidation.TAM_MAX_FILE/ConstantesDatos.DIVISOR_BYTES);  
        int num = (Integer)StrutsFileValidation.getLimite(StrutsFileValidation.TAM_MAX_DOC_PREFFIX,StrutsFileValidation.SUFFIX_REGISTRO);        

        request.setAttribute(ConstantesDatos.TAM_MAX_FILE_BYTE,Math.round(num/ConstantesDatos.DIVISOR_BYTES));
        request.setAttribute("ERROR_FILESIZE_UPLOAD","si");
        opcion = "documentoNuevo";

    }
    m_Log.debug("<================= DocumentoXPDLAction ======================");        
    return (mapping.findForward(opcion));
  }
  catch(Exception e){
      // Si ocurre algún error
      e.printStackTrace();
      m_Log.error("Se ha producido el siguiente error: " + e.getMessage());
      return null;
  }
  }
public ArrayList<ErrorImportacionXPDL> comprobarErrosProc(DefinicionProcedimientosValueObject dfVO,
        TraductorAplicacionBean traductor, String[] params) {
        
    ArrayList<ErrorImportacionXPDL> erroresAux = new ArrayList<ErrorImportacionXPDL>(); 
    
    for (FirmaFlujoVO firmaFlujo : dfVO.getListaFlujosFirma()) {
        if (firmaFlujo.getListaFirmasCircuito().isEmpty() || firmaFlujo.getListaFirmasCircuito() == null) {
            String mensaje = traductor.getDescripcion("sinFirmaCircuito");
            mensaje = mensaje.replace("[1]", firmaFlujo.getNombre());
            erroresAux.add(new ErrorImportacionXPDL(1, mensaje));
        }
    }
    
    if (dfVO.getTxtCodigo()==null || "".equals(dfVO.getTxtCodigo()) || dfVO.getTxtCodigo().length()>5)
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCodigo")));
    if (dfVO.getTxtDescripcion()==null || "".equals(dfVO.getTxtDescripcion()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcDesc")));
    if (dfVO.getDescripcionBreve()==null || "".equals(dfVO.getDescripcionBreve()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcDescBreve")));
    if (isErrorTipoProc(dfVO.getCodTipoProcedimiento(), params))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcTipoProc")));
    if (dfVO.getCodArea()==null || "".equals(dfVO.getCodArea()) || 
            !dfVO.getCodArea().matches("[0-9]+"))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcAreaProc")));
    if (!StringUtils.isNotNullOrEmptyOrNullString(dfVO.getDescArea())){
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcAreaProc")));
    }
    
    if ((dfVO.getCodArea() != null || !"".equals(dfVO.getCodArea())
            || dfVO.getCodArea().matches("[0-9]+")) && (StringUtils.isNotNullOrEmptyOrNullString(dfVO.getCodArea()))) {
        Map<Integer, Integer> error = this.errorAreaProcedimiento(dfVO.getCodArea(), dfVO.getDescArea(), params);
        Map.Entry<Integer, Integer> entry = error.entrySet().iterator().next();

        dfVO.setErrorArea(entry.getKey());
        if (entry.getKey().equals(3)) {
            dfVO.setCodArea(entry.getValue().toString());
        }
    }
    
    
    if (!"0".equals(dfVO.getCodUnidadInicio()) && !"1".equals(dfVO.getCodUnidadInicio()))  
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCodUnidIni")));
    else if ("0".equals(dfVO.getCodUnidadInicio()) && dfVO.getListaCodUnidadInicio().size() == 0) 
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcUnidsIni")));
    
    if (dfVO.getFechaLimiteDesde()==null || "".equals(dfVO.getFechaLimiteDesde()) || 
            !dataCorrecta(dfVO.getFechaLimiteDesde(),"dd/MM/yyyy"))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFecVigDesde")));
    if (dfVO.getFechaLimiteHasta()!=null && !"".equals(dfVO.getFechaLimiteHasta()) && 
            !dataCorrecta(dfVO.getFechaLimiteDesde(),"dd/MM/yyyy"))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFecVigHasta")));
    if (!"0".equals(dfVO.getCodEstado()) && !"1".equals(dfVO.getCodEstado())) 
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcEstadoProc")));
    if (!"0".equals(dfVO.getCodTipoInicio()) && !"1".equals(dfVO.getCodTipoInicio()) && !"2".equals(dfVO.getCodTipoInicio())) 
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcTipoIni")));
    if (!"0".equals(dfVO.getLocalizacion()) && !"1".equals(dfVO.getLocalizacion()))  
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcLocaliz")));
    if (dfVO.getTipoSilencio() != null && !"".equals(dfVO.getTipoSilencio()) && !dfVO.getTipoSilencio().matches("[0-2]"))  
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcTipoSil")));
    
    if (dfVO.getPlazo() != null && !"".equals(dfVO.getPlazo())){
        if (!dfVO.getPlazo().matches("[0-9]{1,2}"))  
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcPlazo")));
        if (!dfVO.getTipoPlazo().matches("[1-3]"))  
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcTipoPlazo")));
    }
    
    if (dfVO.getPorcentaje() != null && !"".equals(dfVO.getPorcentaje()) && !dfVO.getLocalizacion().matches("[0-9]{1,3}"))  
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcPorcPlazo")));
    if (!"0".equals(dfVO.getTramitacionInternet()) && !"1".equals(dfVO.getTramitacionInternet()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcTramInt")));
    if (!"0".equals(dfVO.getDisponible()) && !"1".equals(dfVO.getDisponible()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcDispon")));

    if (dfVO.getListaCodRoles().isEmpty())
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcSinRoles")));
    else {
        Vector <String> codRoles = dfVO.getListaCodRoles();
        Vector <String> descRoles = dfVO.getListaDescRoles();
        Vector <String> porDefectoL = dfVO.getListaPorDefecto();
        Vector <String> consultaWebRolL = dfVO.getListaConsultaWebRol();
        for(int i = 0; i < codRoles.size();i++){
            String codRol = codRoles.get(i);
            String descRol = descRoles.get(i);
            String porDef = porDefectoL.get(i);
            String consultaWebRol = consultaWebRolL.get(i);
            
            if (codRol == null || !codRol.matches("[0-9]{1,2}"))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcRolCod")));
            if (descRol == null || "".equals(descRol))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcRolDesc")));
            if (!"0".equals(porDef) && !"1".equals(porDef))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcRolPorDef")));
            if (!"0".equals(consultaWebRol) && !"1".equals(consultaWebRol))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcRolConsWeb")));
        }
    }
    
    Vector <String> codDocs = dfVO.getListaCodigosDoc();
    Vector <String> descDocs = dfVO.getListaNombresDoc();
    
    for(int i = 0; i < codDocs.size();i++){
        String codDoc = codDocs.get(i);
        String descDoc = descDocs.get(i);
        
        if (codDoc == null || !codDoc.matches("[0-9]{1,6}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcDocCod")));
        if (descDoc == null || "".equals(descDoc))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcDocDesc")));
    }
    
    ArrayList<FirmasDocumentoProcedimientoVO> firmasDocumentoProcedimiento = dfVO.getFirmasDocumentosProcedimiento();
    
    for (FirmasDocumentoProcedimientoVO firmaDoc : firmasDocumentoProcedimiento) {
        if (firmaDoc.getCodDocumento() == null || !firmaDoc.getCodDocumento().matches("[0-9]{1,6}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFirmCodDoc")));
        else {
            boolean refOk = false;
            for(int i = 0; i < codDocs.size();i++){
                if (firmaDoc.getCodDocumento().equals(codDocs.get(i))) {
                    refOk = true;
                }
            }
            if (!refOk) { 
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFirmDocNoEx")));
            } 
        }
        
        if (firmaDoc.getOrden() == null || !firmaDoc.getOrden().matches("[0-9]{1,2}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFirmOrd")));
    
        if (firmaDoc.getUor() != null && !"".equals(firmaDoc.getUor()) && !"null".equals(firmaDoc.getUor()) && 
                !"-888".equals(firmaDoc.getUor()) && !"-999".equals(firmaDoc.getUor()) && !firmaDoc.getUor().matches("[0-9]{1,5}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFirmUor")));
        if (firmaDoc.getCargo() != null && !"".equals(firmaDoc.getCargo()) && !"null".equals(firmaDoc.getCargo()) && 
                !firmaDoc.getCargo().matches("[0-9]{1,3}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFirmCarg")));
        if (firmaDoc.getUsuario() != null && !"".equals(firmaDoc.getUsuario()) && !"null".equals(firmaDoc.getUsuario()) && 
                !firmaDoc.getUsuario().matches("[0-9]{1,4}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFirmUsu")));
        if (firmaDoc.getFinalizaRechazo() != null && !"".equals(firmaDoc.getFinalizaRechazo()) && 
                !"0".equals(firmaDoc.getFinalizaRechazo()) && !"1".equals(firmaDoc.getFinalizaRechazo()))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFirmFinRech")));
        if (firmaDoc.getTramitar() != null && !"".equals(firmaDoc.getTramitar()) && 
                !"0".equals(firmaDoc.getTramitar()) && !"1".equals(firmaDoc.getTramitar()))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcFirmTram")));
    }
    
    Vector listaCodigosEnlaces = dfVO.getListaCodEnlaces();
    Vector listaDescEnlaces = dfVO.getListaDescEnlaces();
    Vector listaUrlEnlaces = dfVO.getListaUrlEnlaces();
    Vector listaEstadoEnlaces = dfVO.getListaEstadoEnlaces();
    
    for (int i = 0; i < listaCodigosEnlaces.size();i++) {
        String codEnlace = (String) listaCodigosEnlaces.get(i);
        String descEnlace = (String) listaDescEnlaces.get(i);
        String urlEnlace = (String) listaUrlEnlaces.get(i);
        String estadoEnlace = (String) listaEstadoEnlaces.get(i);
        
        if (codEnlace == null || !codEnlace.matches("[0-9]{1,3}")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcEnlCodEnl")));
        if (descEnlace == null || "".equals(descEnlace)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcEnlDescEnl")));
        if (urlEnlace == null || "".equals(urlEnlace)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcEnlUrlEnl")));
        if (!"S".equals(estadoEnlace) && !"N".equals(estadoEnlace)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcEnlEstEnl")));
    }
    
    Vector listaCodAgrupaciones = dfVO.getListaCodAgrupaciones();
    Vector listaDescAgrupaciones = dfVO.getListaDescAgrupaciones();
    Vector listaOrdenAgrupaciones = dfVO.getListaOrdenAgrupaciones();
    Vector listaAgrupacionesActivas = dfVO.getListaAgrupacionesActivas();
    
    for (int i = 0; i < listaCodAgrupaciones.size();i++) {
        String codAgrupacion = (String) listaCodAgrupaciones.get(i);
        String descAgrupacion = (String) listaDescAgrupaciones.get(i);
        Integer ordenAgrupacion = (Integer)listaOrdenAgrupaciones.get(i);
        String agrupacionActiva = (String) listaAgrupacionesActivas.get(i);
        
        if (codAgrupacion == null || "".equals(codAgrupacion) || codAgrupacion.length() > 5) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcAgrCod")));
        if (descAgrupacion == null || "".equals(descAgrupacion)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcAgrDesc")));
        if (ordenAgrupacion == null) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcAgrOrd")));
        if (!"NO".equalsIgnoreCase(agrupacionActiva) && !"SI".equalsIgnoreCase(agrupacionActiva))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcAgrAct")));
    }
    
    Vector listaCodCampos = dfVO.getListaCodCampos();
    Vector listaDescCampos = dfVO.getListaDescCampos();
    Vector listaCodPlantilla = dfVO.getListaCodPlantilla();
    Vector listaCodTipoDato = dfVO.getListaCodTipoDato();
    Vector listaTamano = dfVO.getListaTamano();
    Vector listaRotulo = dfVO.getListaRotulo();
    Vector listaActivos = dfVO.getListaActivos();
    Vector listaObligatorios = dfVO.getListaObligatorio();
    Vector listaOcultos = dfVO.getListaOcultos();
    Vector listaBloqueados = dfVO.getListaBloqueados();
    Vector listaPlazoAviso = dfVO.getListaPlazoFecha();
    Vector listaPeriodoAviso = dfVO.getListaCheckPlazoFecha();
    Vector listaAgrupacionesCampo = dfVO.getListaAgrupacionesCampo();
    Vector listaPosicionesX = dfVO.getListaPosicionesX();
    Vector listaPosicionesY = dfVO.getListaPosicionesY();
    
    for (int i = 0; i < listaCodCampos.size();i++) {
        String codCampo = (String) listaCodCampos.get(i);
        String descCampo = (String) listaDescCampos.get(i);
        String codPlantilla = (String) listaCodPlantilla.get(i);
        String codTipoDato = (String) listaCodTipoDato.get(i);
        String tamanho = (String) listaTamano.get(i);
        String rotulo = (String) listaRotulo.get(i);
        String activo = (String) listaActivos.get(i);
        String obligatorio = (String) listaObligatorios.get(i);
        String oculto = (String) listaOcultos.get(i);
        String bloqueado = (String) listaBloqueados.get(i);
        String plazoAviso = (String) listaPlazoAviso.get(i);
        String periodoAviso = (String) listaPeriodoAviso.get(i);
        String agrupacionCampo = (String) listaAgrupacionesCampo.get(i);
        String posicionX = (String) listaPosicionesX.get(i);
        String posicionY = (String) listaPosicionesY.get(i);
        
        if (codCampo == null || "".equals(codCampo)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamCodCam")));
        if (descCampo == null || "".equals(descCampo)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamDescCam")));
        
        if (codTipoDato == null || !codTipoDato.matches("[0-9]{1,2}")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamCTD")));
        else if (codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable")) ||
                codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))) {
            if (codPlantilla == null ||  "".equals(codPlantilla) || codPlantilla.length()>4)
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamDesp")));
        } else if (codPlantilla == null ||  !codPlantilla.matches("[0-9]{1,3}")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamCodPl")));
            
        if (tamanho == null || !tamanho.matches("[0-9]{1,4}")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamTam")));
        if (rotulo == null || "".equals(rotulo)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamRot")));
        if (!"NO".equalsIgnoreCase(activo) && !"SI".equalsIgnoreCase(activo))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamAct")));
        if (!"0".equals(obligatorio) && !"1".equals(obligatorio))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamOblig")));
        if (!"NO".equalsIgnoreCase(oculto) && !"SI".equalsIgnoreCase(oculto))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamOcu")));
        if (!"NO".equalsIgnoreCase(bloqueado) && !"SI".equalsIgnoreCase(bloqueado))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamBlo")));
         
        if (plazoAviso != null && !"".equals(plazoAviso)) {
            if (!plazoAviso.matches("[0-9]{1,4}"))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamPlz")));
            if (!"A".equals(periodoAviso) && !"M".equals(periodoAviso) && !"D".equals(periodoAviso))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamPer")));
        }
        
        if (agrupacionCampo != null && !"".equals(agrupacionCampo) && !"DEF".equalsIgnoreCase(agrupacionCampo)){
            boolean agrupacOk = false;
            for(int j = 0; j < listaCodAgrupaciones.size();j++){
                if (agrupacionCampo.equals(listaCodAgrupaciones.get(j))) 
                    agrupacOk = true;
            }
            if (!agrupacOk)  
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamAgrNoEx")));
        }
        
        if (posicionX != null && !"".equals(posicionX) && !posicionX.matches("[0-9]+")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamPosX")));
        if (posicionY != null && !"".equals(posicionY) && !posicionY.matches("[0-9]+")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamPosY")));
    }
    
    Vector <DefinicionTramitesValueObject> tramites = dfVO.getTramites();
    
    for(int i = 0; i < tramites.size();i++){
        DefinicionTramitesValueObject tramite = tramites.get(i);
        
        comprobarErrosTramite(tramite, traductor, erroresAux, tramites, codDocs, params);
    }
    
    return erroresAux;
}

public void comprobarErrosTramite(DefinicionTramitesValueObject tramite, TraductorAplicacionBean traductor, 
        ArrayList <ErrorImportacionXPDL> erroresAux, Vector <DefinicionTramitesValueObject> tramites, Vector <String> codDocsProc,String[] params){
     
    if (tramite.getCodUnidadInicio() != null && !"-99999".equals(tramite.getCodUnidadInicio()) && !"-99998".equals(tramite.getCodUnidadInicio()) &&  null == ImportacionProcedimientoManager.getInstance().getExisteUor(tramite.getCodUnidadInicio(), params)){
        erroresAux.add(new ErrorImportacionXPDL(1,"La unidad de inicio con unidad visible: "+ tramite.getCodUnidadInicio() +" para el trámite: "+tramite.getCodigoTramite()+ " no existe en el sistema"));
    } 
    if (tramite.getCodigoTramite()==null || !tramite.getCodigoTramite().matches("[0-9]{1,4}"))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCodigo")));
    if (tramite.getNombreTramite()==null || "".equals(tramite.getNombreTramite()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramNombre")));
    if (tramite.getNumeroTramite()==null || !tramite.getNumeroTramite().matches("[0-9]{1,4}"))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramNumero")));
    if (!"0".equals(tramite.getTramiteInicio()) && !"1".equals(tramite.getTramiteInicio()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramIndIni")));
    if (tramite.getCodClasifTramite()==null || !tramite.getCodClasifTramite().matches("[0-9]{1,3}"))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCodCls")));
    if (!"0".equals(tramite.getDisponible()) && !"1".equals(tramite.getDisponible()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramDispInt")));
    if (tramite.getPlazo() != null && !"".equals(tramite.getPlazo())) {
        if (!tramite.getPlazo().matches("[0-9]{1,3}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramPlazo")));
        if (!"H".equals(tramite.getUnidadesPlazo()) && !"N".equals(tramite.getUnidadesPlazo()) && 
                !"M".equals(tramite.getUnidadesPlazo()))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramUnidPl")));
    }
    if (!"0".equals(tramite.getTramitePregunta()) && !"1".equals(tramite.getTramitePregunta()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramTipoPreg")));
    if (tramite.getCodCargo() != null && !"".equals(tramite.getCodCargo()) && 
            !tramite.getCodCargo().matches("[0-9]{1,3}")) 
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCodCargo")));
    if (!"S".equals(tramite.getNotUnidadTramitIni()) && !"N".equals(tramite.getNotUnidadTramitIni()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramNotUnTrI")));
    if (!"S".equals(tramite.getNotUnidadTramitFin()) && !"N".equals(tramite.getNotUnidadTramitFin()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramNotUnTrF")));
    if (!"S".equals(tramite.getNotUsuUnidadTramitIni()) && !"N".equals(tramite.getNotUsuUnidadTramitIni()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramNotUsUnTrI")));
    if (!"S".equals(tramite.getNotUsuUnidadTramitFin()) && !"N".equals(tramite.getNotUsuUnidadTramitFin()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramNotUsUnTrF")));
    if (!"S".equals(tramite.getNotInteresadosIni()) && !"N".equals(tramite.getNotInteresadosIni()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramNotIntI")));
    if (!"S".equals(tramite.getNotInteresadosFin()) && !"N".equals(tramite.getNotInteresadosFin()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramNotIntF")));
    if (tramite.getCodExpRel()!=null && !"".equals(tramite.getCodExpRel()) && tramite.getCodExpRel().length() > 5)
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCodExpRel")));
    if (tramite.getObligatorio()!=null && !"".equals(tramite.getObligatorio()) && 
            !"0".equals(tramite.getObligatorio()) && !"1".equals(tramite.getObligatorio()) && !"2".equals(tramite.getObligatorio()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramObligFav")));    
    if (tramite.getObligatorio()!=null && !"".equals(tramite.getObligatorio()) && 
            !"0".equals(tramite.getObligatorio()) && !"1".equals(tramite.getObligatorio()) && !"2".equals(tramite.getObligatorio()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramObligDes")));
    if ("Pregunta".equals(tramite.getTipoCondicion()) && (tramite.getTexto() == null || "".equals(tramite.getTexto())))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramPregunta")));
    if("1".equals(tramite.getAdmiteNotificacionElectronica())){
        if (tramite.getCodigoTipoNotificacionElectronica()==null || !tramite.getCodigoTipoNotificacionElectronica().matches("[0-9]{1,4}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCodNotElec")));
    } else if (tramite.getAdmiteNotificacionElectronica()!=null && !"".equals(tramite.getAdmiteNotificacionElectronica()) && 
            !"0".equals(tramite.getAdmiteNotificacionElectronica()))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramNotElec")));
    if (tramite.getTipoUsuarioFirma()!=null && tramite.getTipoUsuarioFirma().length() > 0 && tramite.getTipoUsuarioFirma().length() > 1)
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramTipUsFir")));
    if (tramite.getCodigoOtroUsuarioFirma()!=null && tramite.getCodigoOtroUsuarioFirma().length() > 0 && !tramite.getCodigoOtroUsuarioFirma().matches("[0-9]{1,4}"))
        erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCodUsFir")));
    
    Vector listaCodigosDoc = tramite.getListaCodigosDoc();
    Vector listaNombresDoc = tramite.getListaNombresDoc();
    Vector listaVisibleDoc = tramite.getListaVisibleDoc();
    Vector listaPlantillaDoc = tramite.getListaPlantillaDoc();
    Vector listaContidoPlantilla = tramite.getListaContidoPlantilla();
    Vector listaInteresadoPlantilla = tramite.getListaInteresadoPlantilla();
    Vector listaRelacionPlantilla = tramite.getListaRelacionPlantilla();
    Vector listaFirmaDoc = tramite.getListaFirmaDoc();
    Vector listaDocActivos = tramite.getListaDocActivos();
    Vector listaEditoresTexto = tramite.getListaEditoresTexto();
    
    for (int i = 0; i < listaCodigosDoc.size();i++) {
        String codDoc = (String) listaCodigosDoc.get(i);
        String nomeDoc = (String) listaNombresDoc.get(i);
        String visibleDoc = (String) listaVisibleDoc.get(i);
        String docActivo = (String) listaDocActivos.get(i);
        String firmaDoc = (String) listaFirmaDoc.get(i);
        String plantillaDoc = (String) listaPlantillaDoc.get(i);
        String contidoPlantilla = (String) listaContidoPlantilla.get(i);
        String interesadoPlantilla = (String) listaInteresadoPlantilla.get(i);
        String relacionPlantilla = (String) listaRelacionPlantilla.get(i);
        String editorTexto = (String) listaEditoresTexto.get(i);
        
        if (codDoc == null || !codDoc.matches("[0-9]{1,3}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramDocCod")));
        if (nomeDoc == null || "".equals(nomeDoc))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramDocNom")));
        if (!"S".equals(visibleDoc) && !"N".equals(visibleDoc)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramDocVis")));
        if (!"SI".equals(docActivo) && !"NO".equals(docActivo)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramDocAct")));
        if (StringUtils.isNotNullOrEmpty(firmaDoc) // Si existe la firma y es un valor incorrecto se produce un error
				&& !ConstantesDatos.XPDL_EXPORTACION_N.equals(firmaDoc) 
                && !ConstantesDatos.XPDL_EXPORTACION_S.equals(firmaDoc)
                && !ConstantesDatos.XPDL_FIRMA_FLUJO.equals(firmaDoc)
                && !ConstantesDatos.XPDL_FIRMA_OTRO_USUARIO.equals(firmaDoc)
                && !ConstantesDatos.XPDL_FIRMA_TRAMITADOR.equals(firmaDoc)
                && !ConstantesDatos.XPDL_FIRMA_UN_USUARIO.equals(firmaDoc)) { 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramDocFir")));
        }        
        if (plantillaDoc != null && !"".equals(plantillaDoc)) {
            if (contidoPlantilla == null || contidoPlantilla.isEmpty()) 
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramPlantCont")));
            if (!"S".equals(interesadoPlantilla) && !"N".equals(interesadoPlantilla)) 
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramPlantInt")));
            if (!"S".equals(relacionPlantilla) && !"N".equals(relacionPlantilla)) 
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramPlantRel")));
        }
        
        if (!"WORD".equals(editorTexto) && !"OOFFICE".equals(editorTexto) && !"ODT".equals(editorTexto)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramEdText")));
    }
    
    Vector listaCodigosEnlaces = tramite.getListaCodigoEnlaces();
    Vector listaDescEnlaces = tramite.getListaDescripcionEnlaces();
    Vector listaUrlEnlaces = tramite.getListaUrlEnlaces();
    Vector listaEstadoEnlaces = tramite.getListaEstadoEnlaces();
    
    for (int i = 0; i < listaCodigosEnlaces.size();i++) {
        String codEnlace = (String) listaCodigosEnlaces.get(i);
        String descEnlace = (String) listaDescEnlaces.get(i);
        String urlEnlace = (String) listaUrlEnlaces.get(i);
        String estadoEnlace = (String) listaEstadoEnlaces.get(i);
        
        if (codEnlace == null || !codEnlace.matches("[0-9]{1,3}")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramEnlCod")));
        if (descEnlace == null || "".equals(descEnlace)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramEnlDesc")));
        if (urlEnlace == null || "".equals(urlEnlace)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramEnlUrl")));
        if (!"1".equals(estadoEnlace) && !"0".equals(estadoEnlace)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramEnlEst")));
    }
    
    Vector listaCodAgrupaciones = tramite.getListaCodAgrupacion();
    Vector listaDescAgrupaciones = tramite.getListaDescAgrupacion();
    Vector listaOrdenAgrupaciones = tramite.getListaOrdenAgrupacion();
    Vector listaAgrupacionesActivas = tramite.getListaAgrupacionActiva();
    
    for (int i = 0; i < listaCodAgrupaciones.size();i++) {
        String codAgrupacion = (String) listaCodAgrupaciones.get(i);
        String descAgrupacion = (String) listaDescAgrupaciones.get(i);
        Integer ordenAgrupacion = (Integer)listaOrdenAgrupaciones.get(i);
        String agrupacionActiva = (String) listaAgrupacionesActivas.get(i);
        
        if (codAgrupacion == null || "".equals(codAgrupacion) || codAgrupacion.length() > 5) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramAgrCod")));
        if (descAgrupacion == null || "".equals(descAgrupacion)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramAgrDesc")));
        if (ordenAgrupacion == null) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramAgrOrd")));
        if (!"NO".equalsIgnoreCase(agrupacionActiva) && !"SI".equalsIgnoreCase(agrupacionActiva))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramAgrAct")));
    }
    
    Vector listaCodCampos = tramite.getListaCodCampos();
    Vector listaDescCampos = tramite.getListaDescCampos();
    Vector listaCodPlantilla = tramite.getListaCodPlantill();
    Vector listaCodTipoDato = tramite.getListaCodTipoDato();
    Vector listaTamano = tramite.getListaTamano();
    Vector listaRotulo = tramite.getListaRotulo();
    Vector listaActivos = tramite.getListaActivo();
    Vector listaObligatorios = tramite.getListaObligatorio();
    Vector listaOcultos = tramite.getListaOcultos();
    Vector listaBloqueados = tramite.getListaBloqueados();
    Vector listaPlazoAviso = tramite.getListaPlazoFecha();
    Vector listaPeriodoAviso = tramite.getListaCheckPlazoFecha();
    Vector listaOrden = tramite.getListaOrden();
    Vector listaVisible = tramite.getListaVisible();
    Vector listaAgrupacionesCampo = tramite.getListaCodAgrupacionCampo();
    Vector listaPosicionesX = tramite.getListaPosX();
    Vector listaPosicionesY = tramite.getListaPosY();
    
    for (int i = 0; i < listaCodCampos.size();i++) {
        String codCampo = (String) listaCodCampos.get(i);
        String descCampo = (String) listaDescCampos.get(i);
        String codPlantilla = (String) listaCodPlantilla.get(i);
        String codTipoDato = (String) listaCodTipoDato.get(i);
        String tamanho = (String) listaTamano.get(i);
        String rotulo = (String) listaRotulo.get(i);
        String activo = (String) listaActivos.get(i);
        String obligatorio = (String) listaObligatorios.get(i);
        String oculto = (String) listaOcultos.get(i);
        String bloqueado = (String) listaBloqueados.get(i);
        String plazoAviso = (String) listaPlazoAviso.get(i);
        String periodoAviso = (String) listaPeriodoAviso.get(i);
        String orden = (String) listaOrden.get(i);
        String visible = (String) listaVisible.get(i);
        String agrupacionCampo = (String) listaAgrupacionesCampo.get(i);
        String posicionX = (String) listaPosicionesX.get(i);
        String posicionY = (String) listaPosicionesY.get(i);
            
        if (codCampo == null || "".equals(codCampo)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamCodCam")));
        if (descCampo == null || "".equals(descCampo)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamDescCam")));
        
        if (codTipoDato == null || !codTipoDato.matches("[0-9]{1,2}")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamCTD")));
        else if (codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegable")) ||
                codTipoDato.equals(m_CommonProperties.getString("E_PLT.CodigoCampoDesplegableExt"))) {
            if (codPlantilla == null ||  "".equals(codPlantilla) || codPlantilla.length()>4)
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamDesp")));
        } else if (codPlantilla == null ||  !codPlantilla.matches("[0-9]{1,3}")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamCodPl")));
            
        if (tamanho == null || !tamanho.matches("[0-9]{1,4}")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamTam")));
        if (rotulo == null || "".equals(rotulo)) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamRot")));
        if (!"NO".equalsIgnoreCase(activo) && !"SI".equalsIgnoreCase(activo))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamAct")));
        if (!"0".equals(obligatorio) && !"1".equals(obligatorio))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamOblig")));
        if (!"NO".equalsIgnoreCase(oculto) && !"SI".equalsIgnoreCase(oculto))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamOcu")));
        if (!"NO".equalsIgnoreCase(bloqueado) && !"SI".equalsIgnoreCase(bloqueado))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamBlo")));
        
        if (plazoAviso != null && !"".equals(plazoAviso)) {
            if (!plazoAviso.matches("[0-9]{1,4}"))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamPlz")));
            if (!"A".equals(periodoAviso) && !"M".equals(periodoAviso) && !"D".equals(periodoAviso))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamPer")));
        }
        
        if (orden == null || !orden.matches("[0-9]{1,2}"))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamOrd")));
        if (!"N".equalsIgnoreCase(visible) && !"S".equalsIgnoreCase(visible))
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamVis")));
        if (agrupacionCampo != null && !"".equals(agrupacionCampo) && !"DEF".equalsIgnoreCase(agrupacionCampo)){
            boolean agrupacOk = false;
            for(int j = 0; j < listaCodAgrupaciones.size();j++){
                if (agrupacionCampo.equals(listaCodAgrupaciones.get(j))) 
                    agrupacOk = true;
            }
            if (!agrupacOk)  
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCamAgrNoEx")));
        }
        if (posicionX != null && !"".equals(posicionX) && !posicionX.matches("[0-9]+")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamPosX")));
        if (posicionY != null && !"".equals(posicionY) && !posicionY.matches("[0-9]+")) 
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errProcCamPosY")));
    }
    
    Vector listaEstadosTabla = tramite.getListaEstadosTabla();
    Vector listaCodTramitesTabla = tramite.getListaCodTramitesTabla();
    Vector listaTiposTabla = tramite.getListaTiposTabla();
    Vector listaExpresionesTabla = tramite.getListaExpresionesTabla();
    Vector listaCodigosDocTabla = tramite.getListaCodigosDocTabla();
    
    for (int i = 0; i < listaTiposTabla.size();i++) {
        String estadoTabla = (String) listaEstadosTabla.get(i);
        String codTramite = (String) listaCodTramitesTabla.get(i);
        String tipoCond = (String) listaTiposTabla.get(i);
        String expresion = (String) listaExpresionesTabla.get(i);
        String codigoDoc = (String) listaCodigosDocTabla.get(i);
        
        if ("EXPRESION".equals(tipoCond)) {
            if (expresion == null || "".equals(expresion))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCondExp")));
        } else if ("DOCUMENTO".equals(tipoCond)) {
            if (codigoDoc == null || "".equals(codigoDoc))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCondDoc")));
            else {
                boolean codDocOk = false;
                for (int j=0;j<codDocsProc.size();j++) {
                    if (codigoDoc.equals((String) codDocsProc.get(j))){
                        codDocOk = true;
                        break;
                    }
                }
                if (!codDocOk)
                    erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCondDoc")));
            }
            if (!"FIRMADO".equals(estadoTabla) && !"PENDIENTE".equals(estadoTabla) && 
                    !"RECHAZADO".equals(estadoTabla) && !"CIRCUITO FINALIZADO".equals(estadoTabla))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCondEstDoc")));
        } else if ("TRÁMITE".equals(tipoCond)) {
            if (codTramite == null || "".equals(codTramite))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCondTra")));
            else {
                boolean codTracOk = false;
                for (int j=0;j<tramites.size();j++) {
                    DefinicionTramitesValueObject tramiteAux = (DefinicionTramitesValueObject) tramites.get(j);
                    if (codTramite.equals(tramiteAux.getCodigoTramite())){
                        codTracOk = true;
                        break;
                    }
                }
                if (!codTracOk)
                    erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCondTra")));
            }
            
            if (!"FINALIZADO".equals(estadoTabla) && !"INICIADO".equals(estadoTabla) && 
                    !"NO INICIADO".equals(estadoTabla) && !"FAVORABLE".equals(estadoTabla) && 
                    !"DESFAVORABLE".equals(estadoTabla) && !"NO INICIADO O FINALIZADO".equals(estadoTabla))
                erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCondEstTra")));
        } else
            erroresAux.add(new ErrorImportacionXPDL(1,traductor.getDescripcion("errTramCondEntTipo")));
    }
    
    
}

  public boolean dataCorrecta(String maybeDate, String format) {
    Date data = null;
    String reFormat = Pattern.compile("d+|M+").matcher(Matcher.quoteReplacement(format)).replaceAll("\\\\d{1,2}");
    reFormat = Pattern.compile("y+").matcher(reFormat).replaceAll("\\\\d{4}");

    if ( Pattern.compile(reFormat).matcher(maybeDate).matches() ) {
        SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getDateInstance();
        sdf.applyPattern(format);
        sdf.setLenient(false);
        try { 
            data = sdf.parse(maybeDate); 
        } catch (ParseException e) { }
    } 
    return (data!=null);
  }
  
  public void mostrar(Hashtable<String,String> lista){
      Enumeration<String> llaves = lista.keys();
      while(llaves.hasMoreElements()){
          String llave = llaves.nextElement();
          m_Log.debug(" ========> Llave es : " + llave);
          m_Log.debug(" ========> valor es: " + lista.get(llave));
      }
  }
  
    private void anhadeErroresDepartamentoNotificacion(List<ErrorImportacionXPDL> errores, int codAplicacion, int codIdioma, DefinicionProcedimientosValueObject procedimiento) {

        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
        traductor.setApl_cod(codAplicacion);
        traductor.setIdi_cod(codIdioma);

        for (int i = 0; i < procedimiento.getTramites().size(); i++) {
            DefinicionTramitesValueObject tramite = (DefinicionTramitesValueObject) procedimiento.getTramites().get(i);
            if (tramite.getExisteDepartamento() != null && !tramite.getExisteDepartamento()) {
                ErrorImportacionXPDL error = new ErrorImportacionXPDL();
                String textoError = traductor.getDescripcion("codDepartamentoNoExiste");
                textoError = textoError.replace("[1]", tramite.getNumeroTramite());
                textoError = textoError.replace("[2]", tramite.getCodDepartamentoNotificacion());
                textoError = textoError.replace("[3]", tramite.getDescripcionDepartamentoNotificacion());

                error.setCodError(1);
                error.setDescripcionError(textoError);
                errores.add(error);
            }
        }
    }
    
    
        private void anhadeErroresAreaProcedimiento(List<ErrorImportacionXPDL> errores, int codAplicacion, int codIdioma, DefinicionProcedimientosValueObject procedimiento) {

        TraductorAplicacionBean traductor = new TraductorAplicacionBean();
        traductor.setApl_cod(codAplicacion);
        traductor.setIdi_cod(codIdioma);
        ErrorImportacionXPDL error = new ErrorImportacionXPDL();
        
        
        switch (procedimiento.getErrorArea()) {
            case 2:
                {
                    String textoError = traductor.getDescripcion("descripcionAreaNoCoincide");
                    error.setCodError(1);
                    error.setDescripcionError(textoError);
                    errores.add(error);
                    break;
                }
            case 3:
                {
                    String textoError = traductor.getDescripcion("codigoAreaNoCoincide");
                    error.setCodError(1);
                    error.setDescripcionError(textoError);
                    errores.add(error);
                    break;
                }
            case 4:
                {
                    String textoError = traductor.getDescripcion("areaNoExiste");
                    textoError = textoError.replace("[1]", procedimiento.getCodArea());
                    textoError = textoError.replace("[2]", procedimiento.getDescArea());
                    error.setCodError(1);
                    error.setDescripcionError(textoError);
                    errores.add(error);
                    break;
                }
            default:
                break;
        }
    }

  /**
   * Trata las lista que proceden de las jsp y que contienen los items separados por un §¥
   * @param lista
   * @return Vector de String
   */
  private Vector tratarLista(String lista){
      Vector salida = new Vector();
      if(lista!=null){
          String[] datos = lista.split("§¥");

          for(int i=0;datos!=null && i<datos.length;i++){
              if(datos[i]!=null && !"".equals(datos[i])) salida.add(datos[i]);
          }// for
      }
      return salida;
  }
  
  /**
   * Verifica la existencia de las unidades organizativas de un determinado trámite   
   * @param dfVO: Datos del trámite
   * @param participantes: Hashtable<String,STring> con los participantes que forman parte del xpdl
   * @param params: Parámetros de conexión a la base de datos
   * @param actualizacion: Indica si la comprobación se realiza para un procedimiento que se da de alta o si por el contrario, se está actualizando
   * @return ExistenciaUorImportacionTramiteVO si hay unidades tramitadoras del trámite que no existen o null si existen 
   */
  private ExistenciaUorImportacionTramiteVO verificarExistenciaUnidadesTramite(DefinicionTramitesValueObject dtVO,Hashtable<String,String> participantes,String[] params,boolean actualizacion,String codProcedimiento){
                 
      ExistenciaUorImportacionTramiteVO existencia = new ExistenciaUorImportacionTramiteVO();
      ArrayList<ExistenciaUorImportacionVO> existen = new ArrayList<ExistenciaUorImportacionVO>();
      ArrayList<ExistenciaUorImportacionVO> noExisten = new ArrayList<ExistenciaUorImportacionVO>();

      existencia.setCodigoTramite(dtVO.getCodigoTramite());
      existencia.setNombreTramite(dtVO.getNombreTramite());
      // Se comprueba la existencia de la unidad de inicio manual del trámite
      if(dtVO.getCodUnidadInicio()!=null && !"-99999".equals(dtVO.getCodUnidadInicio()) && !"-99998".equals(dtVO.getCodUnidadInicio()) && !"".equals(dtVO.getCodUnidadInicio())){
          boolean existeUorInicioManual = UORsManager.getInstance().existeUorByCodigoVisible(dtVO.getCodUnidadInicio(), params);
          if(existeUorInicioManual){
              existencia.setCodigoUorInicioManual(null);
          }else{
              existencia.setCodigoUorInicioManual(dtVO.getCodUnidadInicio());
              existencia.setDescripcionUorInicioManual(dtVO.getDescUnidadInicio());
              existencia.setObligatorioUorInicioManual(true);
          }

          if(actualizacion){
              ArrayList<ExistenciaUorImportacionVO> manual =  ImportacionProcedimientoManager.getInstance().getUnidadInicioManualTramite(dtVO, codProcedimiento, params);
              existencia.setUorsManualesExisten(manual);
          }
          m_Log.debug(" ============>  Para el trámite " + dtVO.getCodigoTramite() + " existe UorInicioManual: " +  existeUorInicioManual);
      }
      
      Vector<UORDTO> uors = dtVO.getUnidadesTramitadoras();
      m_Log.debug(" ============>  Para el trámite " + dtVO.getCodigoTramite() + ",nombreTramite: " + dtVO.getNombreTramite() + " las unidades tramitadoras son: " +  uors);
      if(dtVO.getCodUnidadTramite()!=null && "0".equals(dtVO.getCodUnidadTramite()) && uors!=null && uors.size()>=1){
            for(Iterator<UORDTO> it = uors.iterator();it.hasNext();){
                UORDTO uor = it.next();
                String codigoUorVisible = uor.getUor_cod_vis();                
                UORDTO oUORTO = UORsDAO.getInstance().getUORPorCodigoVisible(codigoUorVisible,params);
                if(oUORTO==null){
                    ExistenciaUorImportacionVO oUor = new ExistenciaUorImportacionVO();
                    oUor.setCodigoUorVisible(codigoUorVisible);
                    oUor.setExiste(false);
                    oUor.setNombre(participantes.get(codigoUorVisible));                    
                    noExisten.add(oUor);
                }else{
                    ExistenciaUorImportacionVO oUor = new ExistenciaUorImportacionVO();
                    oUor.setCodigoUorVisible(codigoUorVisible);
                    oUor.setCodigoUor(oUORTO.getUor_cod());
                    oUor.setExiste(true);
                    oUor.setNombre(participantes.get(codigoUorVisible));
                    existen.add(oUor);
                }
            }
      }//if

      existencia.setUorsExisten(existen);
      // Si hay unidades tramitadoras que no existe
      if(noExisten.size()>0 || existencia.getCodigoUorInicioManual()!=null) {
          if(actualizacion){
              // Como se está realizando una actualización de un procedimiento ya existente, se recuperan las unidades tramitadoras del trámite que actualmente se
              // está evaluando y se recuperan aquellas que no se estén entre las existentes en la nueva definición
              ArrayList<ExistenciaUorImportacionVO> unidades = ImportacionProcedimientoManager.getInstance().getUnidadesTramitadorasTramite(dtVO, uors, codProcedimiento, params);
              if(unidades!=null && unidades.size()>0){
                  for(int i=0;i<unidades.size();i++){
                      ExistenciaUorImportacionVO unidad = unidades.get(i);
                      existen.add(unidad);
                  }//for
              }//if
          }//if
          existencia.setUorsNoExisten(noExisten);
          existencia.setExisten(false);
      }
      else
          existencia.setExisten(true);

      return existencia;
  }

    /**
     * Comprueba si codTipoProcedimiento está en la base de datos y no es nulo o vacío.
     * 
     * @param codTipoProcedimiento
     * @param params
     * @return 
     */
    private boolean isErrorTipoProc(String codTipoProcedimiento, String[] params) {
        if (StringUtils.isNullOrEmptyOrNullString(codTipoProcedimiento)) {
            return true;
        }
        Integer codigo = Integer.parseInt(codTipoProcedimiento);
        String codigoActual = null;
        Vector<GeneralValueObject> tiposProcedimiento = ProcedimientosManager.getInstance().getListaProcedimientos(params);
        
        // Comprobar si está en la BD
        for (GeneralValueObject tipoProcedimiento : tiposProcedimiento) {
            codigoActual = (String) tipoProcedimiento.getAtributo("codigo");
            if (!StringUtils.isNullOrEmptyOrNullString(codigoActual) && Integer.parseInt(codigoActual) == codigo) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 
     * @param codigoArea
     * @param descripcionArea
     * @param params
     * @return <1,null> En caso de existir el código del área y coincidir con la descripción no se mostrará ningún error.
     * @return <2,null> En caso de existir el código del área pero no coincidir con la descripción se mostrará un mensaje de error conforme el área del procedimiento no tiene la misma descripción que el área que se exporto.
     * @return <3,nuevoCódigo> En caso de existir el código del áreapero si la descripción. se cambiará el código del área por el registrado en el sistema.
     * @return <4,null> En caso de no existir el código del área ni la descripción, se creara una área nueva y se informará al usaurio.
     */
    private Map<Integer, Integer> errorAreaProcedimiento(String codigoArea, String descripcionArea, String[] params) {
        Map<Integer, Integer> error = new HashMap<Integer, Integer>();
        error = AreasManager.getInstance().checkErrorAreaProcedimiento(codigoArea, descripcionArea, params);
        return error;

    }
  
}