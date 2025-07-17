/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.sge.plantillafirma;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.sge.firma.FirmaFlujoManager;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.business.sge.plantillafirma.vo.PlantillaFirmaVO;
import es.altia.agora.business.sge.plantillafirmafacade.PlantillaFirmaFacadeDelegate;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.flexia.portafirmas.plugin.util.ConstantesPortafirmas;
import es.altia.util.commons.BasicTypesOperations;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.exceptions.ModelException;
import es.altia.util.facades.BusinessFacadeDelegateFactory;
import es.altia.util.struts.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version $\Date$ $\Revision$
 */
public class ProcessUpdatePlantillaFirmaAction extends DefaultAction {
    /*_______Constants______________________________________________*/
    public static final String CLSNAME="ProcessUpdatePlantillaFirmaAction";
    public static final String MAPPING_SUCCESS="PlantillaFirmaForm";
    private static final String MESSAGE_UPDATE_SUCCESS="PlantillaFirmaForm.Update.Success";

    /*_______Atributes______________________________________________*/

    /*_______Operations_____________________________________________*/

    protected ActionForward doPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InternalErrorException, ModelException {
        /*Cast form*/
        PlantillaFirmaActionForm concreteForm = (PlantillaFirmaActionForm) form;

        /* Pre-fill static form fields */
        int idUsuario = SessionManager.getAuthenticatedUser(request).getIdUsuario();
        concreteForm.setUsuario(idUsuario);

        /* Recuperar parámetros de conexión */
        UsuarioValueObject usuario = SessionManager.getAuthenticatedUser(request);
        String[] params = usuario.getParamsCon();
        if (_log.isDebugEnabled()) _log.debug(CLSNAME+".doPerform() PARAMS = {"+BasicTypesOperations.toString(params,",")+"}");

        /* Retrieve data from form */
        PlantillaFirmaVO vo = concreteForm.getPlantillaFirmaVO(request);

        /* Do the job */
        PlantillaFirmaFacadeDelegate facade = (PlantillaFirmaFacadeDelegate) BusinessFacadeDelegateFactory.getFacadeDelegate(PlantillaFirmaFacadeDelegate.class);
        facade.setDsKey(params[params.length-1]);
        facade.updatePlantillaFirma(vo);
        concreteForm.setPlantillaFirmaVO(vo,request);

        /* Recuperar nombres */
        if (ConstantesPortafirmas.FIRMA_FLUJO.equals(vo.getRequiereFirma())) { /* flujo */
            int idFlujo = vo.getIdFlujo();
            FirmaFlujoVO flujo = FirmaFlujoManager.getInstance().getFlujoFirma(idFlujo, params);
            
            if (flujo != null) {
                concreteForm.setNombreFlujo(flujo.getNombre());
            } else {
                concreteForm.setNombreFlujo("FLUJO NO VALIDO");
            }
        } else { /* usuario delegado */

			final int[] idsUsuariosDelegados = vo.getIdsUsuariosFirmantes();
			final int nroUsuariosDelegados = idsUsuariosDelegados.length;
			if (nroUsuariosDelegados > 0) {
				final String[] nombresUsuariosDelegados = new String[nroUsuariosDelegados];
				final UsuarioManager mgr = UsuarioManager.getInstance();
				for (int i = 0; i < nroUsuariosDelegados; i++) {
					UsuarioEscritorioValueObject usuarioEscritorioVO = mgr.buscaUsuario(idsUsuariosDelegados[i]);
						if (usuarioEscritorioVO != null) {
						nombresUsuariosDelegados[i]=usuarioEscritorioVO.getNombreUsu();
						} else {
						nombresUsuariosDelegados[i]="USUARIO NO VALIDO";
						}
				}//str
				concreteForm.setNombresUsuariosFirmantes(nombresUsuariosDelegados);
			}//if
        }

        /* Return ActionForward */
        saveSingleMessage( request, new ActionMessage(MESSAGE_UPDATE_SUCCESS) );

        // Indicarle a la pantalla que debe cerrarse porque todo fue satisfactorio
        request.setAttribute("EXIT_SUCCESS", true);
        
        /* Return ActionForward. */
        return mapping.findForward(MAPPING_SUCCESS);
    }//doPerform

    protected String getMainPageMapping() {
        return MAPPING_SUCCESS;
    }

    protected String getInternalErrorMapping() {
        return GlobalNames.INTERNALERROR_GLOBAL_FORWARD;
    }

    protected String getModelErrorMapping() {
        return GlobalNames.MODELERROR_GLOBAL_FORWARD;
    }

}//class
/*______________________________EOF_________________________________*/
