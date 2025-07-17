/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.sge.documentofirma;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.documentoportafirmas.DocumentoExternoNotificacionForm;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionDAO;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.portafirmas.plugin.PluginPortafirmas;
import es.altia.flexia.portafirmas.plugin.factoria.PluginPortafirmasFactoria;
import es.altia.flexia.portafirmas.plugin.vo.DocumentoFirmadoVO;
import es.altia.flexia.registro.justificante.util.FileOperations;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.ArrayListFirmasVO;
import es.altia.merlin.licitacion.commons.utils.integraciones.vo.FirmaVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.struts.DefaultAction;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author Administrador
 */
public class ProcessFirmaDocExternoNotificacionAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    private static final String CLSNAME = "ProcessFirmaDocumentoTramitacionAction";
    private static final String MAPPING_SUCCESS = "success";
    private static final String MESSAGE_SUCCESS = "Sge.FirmaDocumentoTramitacionForm.FirmaGuardadaCorrectamente";
    private static final String MESSAGE_CERTIFICATE_NOT_VALID = "Sge.FirmaDocumentoTramitacionForm.CertificadoNoValido";

    private static final Log _log =
            LogFactory.getLog(ProcessFirmaDocumentoTramitacionAction.class.getName());

    private String pDataSourceKey = null;
    /*_______Operations_____________________________________________*/
    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        if (_log.isInfoEnabled()) _log.info(CLSNAME+".doPerform() BEGIN");

        /* Cast form */     
        DocumentoExternoNotificacionForm concreteForm = (DocumentoExternoNotificacionForm) form;
        UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        boolean firmaValida = false;
        try{
            firmaValida = verificarFirma(concreteForm,usuario);
            
        }catch(Exception e){
            e.printStackTrace();
        }

        if(!firmaValida){
            saveSingleMessage(request,new ActionMessage(MESSAGE_CERTIFICATE_NOT_VALID));
            request.setAttribute("FIRMA_VALIDA","NO");
        }else{

            request.setAttribute("FIRMA_VALIDA","SI");
            boolean continuar = false;
            try{
                continuar = guardarFirma(concreteForm, usuario);
            }catch(Exception e){
                e.printStackTrace();
            }

            request.setAttribute("FIRMA_ALMACENADA","NO");
            if(continuar) request.setAttribute("FIRMA_ALMACENADA","SI");

            /* Save messages */
            saveSingleMessage(request,new ActionMessage(MESSAGE_SUCCESS));
            
        }// else

        /* Return ActionForward */
        ActionForward result  = mapping.findForward("success");
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
    
    
    private boolean guardarFirma(DocumentoExternoNotificacionForm concreteForm,UsuarioValueObject usuario){
        boolean continuar = false;
        
        int codNotificacion = concreteForm.getCodNotificacion();
        int codAdjunto = concreteForm.getCodAdjunto();
        String firma = concreteForm.getFirma();
        
        int ejercicio    = concreteForm.getIdEjercicio();
        int codTramite   = concreteForm.getIdTramite();
        int ocurrenciaTramite   = concreteForm.getIdOcurrenciaTramite();
        String codProcedimiento = concreteForm.getIdProcedimiento();
        String numExpediente    = concreteForm.getIdNumeroExpediente();        
        String[] params = usuario.getParamsCon();
        
        Hashtable<String,Object> datos = new Hashtable<String,Object>();
        String codMunicipio = Integer.toString(concreteForm.getIdMunicipio());
        datos.put("codMunicipio",codMunicipio);
        datos.put("ejercicio",Integer.toString(ejercicio));
        datos.put("numeroExpediente",numExpediente);              
        datos.put("codTramite",Integer.toString(codTramite));
        datos.put("ocurrenciaTramite",Integer.toString(ocurrenciaTramite));
        datos.put("params",params);        
        datos.put("numeroDocumento",Integer.toString(codAdjunto));
        datos.put("codDocumento",Integer.toString(codNotificacion));
        datos.put("fichero",firma.getBytes());
        datos.put("codUsuario",Integer.toString(usuario.getIdUsuario()));

        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
        Documento doc = null;
        int tipoDocumento = -1;
        if(!almacen.isPluginGestor())
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
        else{
            codProcedimiento = numExpediente.split("[/]")[1];
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);

            ResourceBundle bundle = ResourceBundle.getBundle("documentos");    
            String carpetaRaiz   = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);
            
            AdjuntoNotificacionVO adjunto = AdjuntoNotificacionManager.getInstance().getInfoDocumentoExternoNotificacion(codAdjunto,params);
            datos.put("nombreDocumento",FileOperations.getNombreArchivo(adjunto.getNombre()));
            datos.put("extension",ConstantesDatos.EXTENSION_FICHERO_FIRMA_DOCUMENTO_TRAMITACION);
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
            continuar = almacen.setFirmaDocumentoExternoNotificacion(doc);

       }catch(AlmacenDocumentoTramitacionException e){
           e.printStackTrace();                  
           _log.error(e.getMessage());
       }
        
        return continuar;
    }

    
    
    
    
    public boolean verificarFirma(DocumentoExternoNotificacionForm formulario,UsuarioValueObject usuario){
        boolean exito = false;
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        byte[] documento = null;
        int codOrganizacion             = usuario.getOrgCod();        
        String[] params                  = usuario.getParamsCon();
        String firma                        = formulario.getFirma();
        int codAdjunto      = formulario.getCodAdjunto();
        _log.debug("**************** verificarFirma codOrg: " + codOrganizacion + " <> codAdjunto: " + codAdjunto);
        
        try{     
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            String numExpediente = formulario.getIdNumeroExpediente();
            int codTramite = formulario.getIdTramite();
            int ocurrenciaTramite = formulario.getIdOcurrenciaTramite();
            int ejercicio = formulario.getIdEjercicio();
            //int codMunicipio = formulario.getIdMunicipio();
            String codMunicipio = Integer.toString(formulario.getIdMunicipio());
            String codProcedimiento = formulario.getIdProcedimiento();

            Hashtable<String,Object> datos = new Hashtable<String,Object>();
            datos.put("codMunicipio",codMunicipio);
            datos.put("ejercicio",Integer.toString(ejercicio));
            datos.put("numeroExpediente",numExpediente);              
            datos.put("codTramite",Integer.toString(codTramite));
            datos.put("ocurrenciaTramite",Integer.toString(ocurrenciaTramite));                            
            datos.put("codDocumento",Integer.toString(codAdjunto));

            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
            Documento doc = null;
            int tipoDocumento = -1;
            if(!almacen.isPluginGestor())
              tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
            else{
              codProcedimiento = numExpediente.split("[/]")[1];
              String nombreProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);
              AdjuntoNotificacionVO adjunto = AdjuntoNotificacionDAO.getInstance().getInfoDocumentoExternoNotificacion(codAdjunto,con);
                
              datos.put("nombreDocumento",FileOperations.getNombreArchivo(adjunto.getNombre()));
              datos.put("extension",FileOperations.getExtension(adjunto.getNombre()));
              datos.put("tipoMime", adjunto.getContentType());            

              ResourceBundle bundle = ResourceBundle.getBundle("documentos");              
              String carpetaRaiz   = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

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

          
            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
            doc = almacen.getDocumentoExternoNotificacion(doc,con);

            File f = File.createTempFile("prueba",".temp");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(doc.getFichero());
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
                }else{
                    infoFirma.setValido(false);
                    exito = false;
                }
            }else{
                infoFirma.setValido(false);
                exito = false;
            }

            f.delete();

          }catch(BDException e){
              e.printStackTrace();                  
              _log.error("Error al recuperar conexión a la BBDD: " + e.getMessage());
          }
          catch(AlmacenDocumentoTramitacionException e){
              e.printStackTrace();                  
              _log.error("Error al procesar la firma de un doucmento externo de una notificación: " + e.getMessage());
          }catch(Exception e){
              e.printStackTrace();                  
              _log.error("Error al procesar la firma de un doucmento externo de una notificación: " + e.getMessage());
              
          }
        
       return exito;        
    }
    
}
