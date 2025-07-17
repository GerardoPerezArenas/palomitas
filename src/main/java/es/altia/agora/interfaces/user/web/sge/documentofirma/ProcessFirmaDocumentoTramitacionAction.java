/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.sge.documentofirma;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.agora.business.portafirmas.documentofirma.vo.*;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoFirmaFacadeDelegate;
import es.altia.agora.business.portafirmas.documentofirmafacade.DocumentoRelacionFirmaFacadeDelegate;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import es.altia.agora.business.sge.persistence.DocumentosRelacionExpedientesManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.struts.DefaultAction;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.escritorio.exception.UsuarioNoEncontradoException;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.util.commons.MimeTypes;
import es.altia.util.documentos.DocumentOperations;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.x509.certificados.validacion.ValidacionCertificado;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.servlet.http.HttpSession;



/**
 * @version $\Date$ $\Revision$
 */
public class ProcessFirmaDocumentoTramitacionAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "ProcessFirmaDocumentoTramitacionAction";
    private static final String MAPPING_SUCCESS = "FirmaDocumentoTramitacionForm";
    private static final String MESSAGE_SUCCESS = "Sge.FirmaDocumentoTramitacionForm.FirmaGuardadaCorrectamente";
    private static final String MESSAGE_CERTIFICATE_NOT_VALID = "Sge.FirmaDocumentoTramitacionForm.CertificadoNoValido";
    private static final String MESSAGE_USER_NOT_VALID = "Sge.FirmaDocumentoTramitacionForm.UsuarioFirmaNoValido";
    
    private static final Log _log =
            LogFactory.getLog(ProcessFirmaDocumentoTramitacionAction.class.getName());

    private String pDataSourceKey = null;
    private boolean usuarioValido=false;
    int idUsuarioLogueado=0;
    
    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");

        /* Cast form */             
        DocumentoFirmaActionForm concreteForm = (DocumentoFirmaActionForm) form;
        this.pDataSourceKey = SessionManager.getDataSourceKey(request);
        UsuarioValueObject usuario = new UsuarioValueObject();
        
        if (SessionManager.isUserAuthenticated(request)) {
        
        
            
            idUsuarioLogueado = SessionManager.getAuthenticatedUser(request).getIdUsuario();

            usuario.setIdUsuario(idUsuarioLogueado);
        
            int codOrganizacion             = SessionManager.getAuthenticatedUser(request).getOrgCod();
            String nombreOrganizacion  = SessionManager.getAuthenticatedUser(request).getOrg();
            String[] params                  =SessionManager.getAuthenticatedUser(request).getParamsCon();
            usuario.setParamsCon(params);
            usuario.setOrgCod(codOrganizacion);
            usuario.setOrg(nombreOrganizacion);
        }
        boolean firmaValida = false;
        try{
            firmaValida = verificarFirma(concreteForm,usuario);
            
        }catch(Exception e){
            e.printStackTrace();
        }

        if(!firmaValida){
            if (!usuarioValido) saveSingleMessage(request,new ActionMessage(MESSAGE_USER_NOT_VALID));
            else saveSingleMessage(request,new ActionMessage(MESSAGE_CERTIFICATE_NOT_VALID));
        }else{
            boolean continuar = false;
            try{
                continuar = guardarFirma(concreteForm, usuario);
            }catch(Exception e){
                e.printStackTrace();
            }

            if(continuar){
                /* Retrieve DataSource key */
                this.pDataSourceKey = SessionManager.getDataSourceKey(request);

                /* Save Signature */
                int idUsuarioAutenticado = SessionManager.getAuthenticatedUser(request).getIdUsuario();
                int idUsuarioResponsable = concreteForm.getUsuarioFirmante();
                
                int tipoDocumento = concreteForm.getTipoDocumento();
                if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_EXPEDIENTE) {
                final DocumentoFirmaVO vo = new DocumentoFirmaVO(
                        new DocumentoFirmaPK(concreteForm.getIdMunicipio(),
                                concreteForm.getIdProcedimiento(), concreteForm.getIdEjercicio(),
                                concreteForm.getIdNumeroExpediente(), concreteForm.getIdTramite(),
                                concreteForm.getIdOcurrenciaTramite(), concreteForm.getIdNumeroDocumento(),
                                idUsuarioResponsable),
                            DocumentoFirmaVO.ESTADO_FIRMA_FIRMADO,concreteForm.getFirma().getBytes(), Calendar.getInstance(),
                            null, idUsuarioAutenticado);
                DocumentoFirmaFacadeDelegate facade = (DocumentoFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoFirmaFacadeDelegate.class);
                facade.setDsKey(this.pDataSourceKey);
                facade.firmarDocumentoTramitacion(vo);
                } else if (tipoDocumento == DocumentoCustomVO.DOCUMENTO_RELACION) {
                    final DocumentoRelacionFirmaVO vo = new DocumentoRelacionFirmaVO(
                            new DocumentoRelacionFirmaPK(concreteForm.getIdMunicipio(),
                                    concreteForm.getIdProcedimiento(), concreteForm.getIdEjercicio(),
                                    concreteForm.getIdNumeroExpediente(), concreteForm.getIdTramite(),
                                    concreteForm.getIdOcurrenciaTramite(), concreteForm.getIdNumeroDocumento(),
                                    idUsuarioResponsable),
                            DocumentoRelacionFirmaVO.ESTADO_FIRMA_FIRMADO,concreteForm.getFirma().getBytes(), Calendar.getInstance(),
                            null, idUsuarioAutenticado);
                    DocumentoRelacionFirmaFacadeDelegate facade = (DocumentoRelacionFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(DocumentoRelacionFirmaFacadeDelegate.class);
                    facade.setDsKey(this.pDataSourceKey);
                    facade.firmarDocumentoTramitacion(vo);
                }
            }

            /* Save messages */
            saveSingleMessage(request,new ActionMessage(MESSAGE_SUCCESS));
         }

        /* Return ActionForward */
        //ActionForward result  = mapping.findForward(MAPPING_SUCCESS);
        ActionForward result  = mapping.findForward("ResultadoFirmaDocumentoTramitacionForm");
        
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() END");
        return result;
       
    }//doPerform

    protected String getMainPageMapping() {
        return GlobalNames.MAINPAGE_GLOBAL_FORWARD;
    }

    protected String getInternalErrorMapping() {
        return GlobalNames.INTERNALERROR_GLOBAL_FORWARD;
    }

    protected String getModelErrorMapping() {
        return GlobalNames.MODELERROR_GLOBAL_FORWARD;
    }



  
    private boolean guardarFirma(DocumentoFirmaActionForm concreteForm,UsuarioValueObject usuario) throws WSException,TechnicalException,AlmacenDocumentoTramitacionException{
        boolean continuar = false;
        /************** SE GUARDA LA FIRMA EN EL GESTOR DOCUMENTAL SI PROCEDE ******************************/        
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),concreteForm.getIdProcedimiento());
        
        if(almacen.isPluginGestor()){
            Hashtable<String,Object> datos = new Hashtable<String,Object>();
            datos.put("codMunicipio",Integer.toString(usuario.getOrgCod()));
            datos.put("codProcedimiento",concreteForm.getIdProcedimiento());
            datos.put("ejercicio",Integer.toString(concreteForm.getIdEjercicio()));
            datos.put("numeroExpediente",concreteForm.getIdNumeroExpediente());
            datos.put("codTramite",Integer.toString(concreteForm.getIdTramite()));
            datos.put("ocurrenciaTramite",Integer.toString(concreteForm.getIdOcurrenciaTramite()));
            datos.put("codDocumento",Integer.toString(concreteForm.getIdNumeroDocumento()));
            datos.put("codUsuario",Integer.toString(usuario.getIdUsuario()));            
            datos.put("numeroDocumento",Integer.toString(concreteForm.getIdNumeroDocumento()));            
            datos.put("params",usuario.getParamsCon());
            datos.put("fichero",concreteForm.getFirma().getBytes());

            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio",Integer.toString(usuario.getOrgCod()));
            gVO.setAtributo("codProcedimiento",concreteForm.getIdProcedimiento());
            gVO.setAtributo("ejercicio",Integer.toString(concreteForm.getIdEjercicio()));
            gVO.setAtributo("codTramite",Integer.toString(concreteForm.getIdTramite()));
            gVO.setAtributo("ocurrenciaTramite",Integer.toString(concreteForm.getIdOcurrenciaTramite()));
            gVO.setAtributo("numeroExpediente",concreteForm.getIdNumeroExpediente());
            gVO.setAtributo("numeroDocumento",Integer.toString(concreteForm.getIdNumeroDocumento()));


            Documento doc = null;
            String codigoVisibleTramite =  DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(usuario.getOrgCod()), concreteForm.getIdProcedimiento(), Integer.toString(concreteForm.getIdTramite()), usuario.getParamsCon());
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(concreteForm.getIdProcedimiento(), usuario.getParamsCon());
            //datos.put("nombreOrganizacion",usuario.getOrg());
            //datos.put("nombreProcedimiento",nombreProcedimiento);
            datos.put("codigoVisibleTramite",codigoVisibleTramite);


            /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
            ResourceBundle bundle = ResourceBundle.getBundle("documentos");     
            String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
                        

            // Nombre de la carpeta
            String nombreDocumento = null;
            String nombreCarpetaExpediente = null;
            if(concreteForm.getTipoDocumento()==0){
                nombreDocumento = DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, usuario.getParamsCon());
                datos.put("perteneceRelacion","false");
                nombreCarpetaExpediente = ((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION);
            } else {
                nombreDocumento = DocumentosRelacionExpedientesManager.getInstance().getNombreDocumentoRelacionGestor(gVO, usuario.getParamsCon());
                gVO.setAtributo("numeroRelacion",concreteForm.getIdNumeroExpediente());
                datos.put("numeroRelacion",concreteForm.getIdNumeroExpediente());
                datos.put("perteneceRelacion","true");
                
                //listaCarpetas.add(ConstantesDatos.PREFIJO_DOCUMENTO_RELACION + numeroRelacion.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
                // El nombre de la carpeta de una relación entre expedientes es diferente al nombre otorgado a una carpeta de expediente
                nombreCarpetaExpediente = ConstantesDatos.PREFIJO_DOCUMENTO_RELACION + ((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION);
            }
            
            datos.put("nombreDocumento",nombreDocumento);

            ArrayList<String> listaCarpetas = new ArrayList<String>();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add(usuario.getOrgCod() + ConstantesDatos.GUION + usuario.getOrg());
            listaCarpetas.add(concreteForm.getIdProcedimiento() + ConstantesDatos.GUION + nombreProcedimiento);
            //listaCarpetas.add(((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
            listaCarpetas.add(nombreCarpetaExpediente);
            
            datos.put("listaCarpetas",listaCarpetas);

            String editorTexto = concreteForm.getEditorTexto();
            String tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                    editorTexto, nombreDocumento);
            datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime) + ConstantesDatos.DOT + ConstantesDatos.EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION);
            datos.put("tipoMime", tipoMime);
            
            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR);
            try{
                almacen.setFirmaDocumento(doc);
                continuar = true;
            }catch(AlmacenDocumentoTramitacionException e){
                continuar  = false;
                e.printStackTrace();
                _log.error("Error al guardar la firma en el gestor documental: "  + e.getMessage());
                throw e;
            }
        }else continuar = true;

        /****************************************************************************************************/
        return continuar;
    }

    public boolean verificarFirma(DocumentoFirmaActionForm formulario,UsuarioValueObject usuario){
        boolean exito = false;
        
        byte[] documento = null;
        int codOrganizacion             = usuario.getOrgCod();
        String nombreOrganizacion  = usuario.getOrg();
        String[] params                  = usuario.getParamsCon();
        String firma                        = formulario.getFirma();
        _log.debug("**************** verificarFirma codOrg: " + codOrganizacion + " <> nombreOrg: " + nombreOrganizacion);
        _log.debug("**************** La firma del fichero es:  " + firma);

        try{

            documento = getContenidoDocumento(formulario,Integer.toString(codOrganizacion), nombreOrganizacion, params);
            _log.debug("El contenido del documento tiene una longitud de " + documento.length);
            
            File f = File.createTempFile("prueba",".temp");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(documento);
            fos.flush();
            fos.close();
                        
            DocumentoFirmadoVO docFirma = new DocumentoFirmadoVO();
            docFirma.setFirma(firma);
            docFirma.setFicheroFirma(f);
            docFirma.setFicheroHash64(formulario.getHashB64());
            docFirma.setTipoMime(formulario.getTipoMime());
            
            PluginPortafirmas plugin = PluginPortafirmasFactoria.getImplClass(String.valueOf(codOrganizacion));
            ArrayListFirmasVO datosFirma = plugin.verificarFirmaInfo(docFirma);
            
            FirmaVO infoFirma = new FirmaVO();
            
            if (!datosFirma.isEmpty()){
                if(datosFirma.get(0) != null){
                infoFirma = (FirmaVO) datosFirma.get(0);         
                exito = true;
                exito=verificaUsuarioFirmante(infoFirma);
                }else{
                    infoFirma.setValido(false);
                    exito = false;
                }     
            }else{
                infoFirma.setValido(false);
                exito = false;
            }
                       
            f.delete();                        
            /*
            _log.debug("Validando antes firmar");
            exito = verificarFirma(f,firma);
            _log.debug("Validando firmar después " + exito);
            f.delete();
            */
        }catch(Exception e){
            e.printStackTrace();
        }
        
       return exito;        
    }



    public boolean verificarFirma(File documento,String firma){
       ValidacionCertificado validar = new ValidacionCertificado();
        return validar.verificarFirma(documento, firma);
    }

    public boolean verificaUsuarioFirmante(FirmaVO infoFirma)
    {
        boolean exito=false;
        try{
            
            String nifUsuarioFirmaDocumento=infoFirma.getNif();
            UsuarioEscritorioValueObject usuario=new UsuarioEscritorioValueObject();
            int codUsuarioFirmante;
            
            if(UsuarioManager.getInstance().buscaUsuario(nifUsuarioFirmaDocumento)!=null)
            {
                usuario=UsuarioManager.getInstance().buscaUsuario(nifUsuarioFirmaDocumento);
                codUsuarioFirmante=usuario.getIdUsuario();
                if(codUsuarioFirmante==idUsuarioLogueado)
                {
                    exito=true;
                    usuarioValido=true; 
                }
                else
                {
                    exito=false;
                    usuarioValido=false; 
                }
            }
            else
            {
                exito=false;
                usuarioValido=false;        
            }
                                             
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return exito;
    }
    
    
    /**
     * Se recupera el contenido del documento para poder verificar la firma
    */
    private byte[] getContenidoDocumento(DocumentoFirmaActionForm formulario,String codOrganizacion,String nombreOrganizacion,String[] params) throws TechnicalException,WSException,AlmacenDocumentoTramitacionException{
        byte[] resultado = null;

       Hashtable<String,Object> datos = new Hashtable<String,Object>();
       
       datos.put("codMunicipio",Integer.toString(formulario.getIdMunicipio()));
       datos.put("numeroExpediente",formulario.getIdNumeroExpediente());
       datos.put("codTramite",Integer.toString(formulario.getIdTramite()));
       datos.put("ocurrenciaTramite",Integer.toString(formulario.getIdOcurrenciaTramite()));
       datos.put("numeroDocumento",Integer.toString(formulario.getIdNumeroDocumento()));
       datos.put("params",params);

        // Se obtiene el nombre del documento a mostrar porque se necesita para el caso de que se venga de firmar el documento
        String codProcedimiento = formulario.getIdProcedimiento();
        String ejercicio = Integer.toString(formulario.getIdEjercicio());
            
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codMunicipio", Integer.toString(formulario.getIdMunicipio()));
        gVO.setAtributo("codProcedimiento", codProcedimiento);
        gVO.setAtributo("ejercicio", ejercicio);
        gVO.setAtributo("codTramite", Integer.toString(formulario.getIdTramite()));
        gVO.setAtributo("ocurrenciaTramite", Integer.toString(formulario.getIdOcurrenciaTramite()));
        gVO.setAtributo("numeroExpediente", formulario.getIdNumeroExpediente());
        gVO.setAtributo("numeroDocumento", Integer.toString(formulario.getIdNumeroDocumento()));

        String nombreDocumento = null;
        if (formulario.getTipoDocumento() == 0) {
            nombreDocumento = DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, params);
            datos.put("perteneceRelacion", "false");
        } else {
            nombreDocumento = DocumentosRelacionExpedientesManager.getInstance().getNombreDocumentoRelacionGestor(gVO, params);
            datos.put("perteneceRelacion", "true");
            datos.put("numeroRelacion", formulario.getIdNumeroExpediente());
        }
        
        datos.put("nombreDocumento",nombreDocumento);

        String editorTexto = formulario.getEditorTexto();
        String tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                editorTexto, nombreDocumento);
        datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));
        datos.put("tipoMime", tipoMime);

        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,formulario.getIdProcedimiento());
        Documento doc = null;
        int tipoDocumento = -1;
        if(!almacen.isPluginGestor())
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        else{
            String carpetaExpediente = null;            
            // Se recupera el nombre del documento para poder componer la ruta y leerlo en el gestor documental
            if(formulario.getTipoDocumento()==0){
                carpetaExpediente = ((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION);
            }
            else{
                carpetaExpediente = ConstantesDatos.PREFIJO_DOCUMENTO_RELACION + formulario.getIdNumeroExpediente().replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION);
                gVO.setAtributo("numeroRelacion",formulario.getIdNumeroExpediente());
            }

            String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(formulario.getIdMunicipio()),codProcedimiento,Integer.toString(formulario.getIdTramite()),params);
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);            
            datos.put("codProcedimiento",codProcedimiento);            
            datos.put("codigoVisibleTramite",codigoVisibleTramite);
            datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);

           /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
            ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");         
            String carpetaRaiz  = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + formulario.getIdMunicipio() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
            
            ArrayList<String> listaCarpetas = new ArrayList<String>();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add( formulario.getIdMunicipio() + ConstantesDatos.GUION + nombreOrganizacion);
            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
            // Original
            //listaCarpetas.add(((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
            listaCarpetas.add(carpetaExpediente);

            datos.put("listaCarpetas",listaCarpetas);
            
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        }

        // Se recupera el contenido del documento
        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos,tipoDocumento);
        resultado = almacen.getDocumento(doc);

        return resultado;
    }


}//class
