package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.interfaces.user.web.formularios.tramitar.AnexoForm;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.catalogoformularios.model.solicitudes.vo.AnexoVO;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.catalogoformularios.util.exceptions.InternalErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.upload.FormFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Nuevo anexo y eliminar anexo
 * @author Diana Piñeiro
 * @version 1.0
 */

public class TratarAnexosAction extends ActionSession {

   protected static Log log = LogFactory.getLog(TratarAnexosAction.class.getName());

   public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
       throws IOException, ServletException {

       ActionErrors errors = new ActionErrors();
       AnexoForm anexoForm = (AnexoForm) form;
       try{
           FormularioFacade fac = new FormularioFacade();

           if ("add".equals(anexoForm.getAccion())){
               try{
                   FormFile file = anexoForm.getFile();
                   AnexoVO vo = new AnexoVO(
                           anexoForm.getFormulario(), 0, anexoForm.getDescripcion(),
                           anexoForm.getFile().getContentType(),
                        anexoForm.getFile().getFileData()
                   );
                   fac.addAnexo(vo);
               }catch(Exception e){
                   errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("error.addAnexo", e.getMessage()));
                   m_Log.error("***** Fallo en tratarAnexosAction-add:"+ e.getMessage());
                   e.printStackTrace();
               }

           } else if ("del".equals(anexoForm.getAccion())){
               try{
                   fac.eliminarAnexo(anexoForm.getFormulario(), anexoForm.getAnexo());
               }catch(Exception e){
                   errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("error.delAnexo", e.getMessage()));
                   m_Log.error("***** Fallo en tratarAnexosAction-del:"+ e.getMessage());
                   e.printStackTrace();
               }
           }
       }catch(InternalErrorException e){
           errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionError("error.tratarAnexos", e.getMessage()));
           m_Log.error("***** Fallo en tratarAnexosAction:" + e.getMessage());
       }

       if (!errors.isEmpty()){
           saveErrors(request, errors);
           //request.setAttribute("formPDF", anexoForm.getFormulario());
           //request.setAttribute("estado","0");
           return (mapping.findForward("Fail"));
       }else{
           request.setAttribute("formPDF", anexoForm.getFormulario());
           request.setAttribute("estado","0");
           return (mapping.findForward("Success"));
       }

   }


}
