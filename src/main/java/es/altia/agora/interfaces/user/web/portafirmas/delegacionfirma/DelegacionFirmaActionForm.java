/*______________________________BOF_________________________________*/
package es.altia.agora.interfaces.user.web.portafirmas.delegacionfirma;

import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaPK;
import es.altia.agora.business.portafirmas.delegacionfirma.vo.DelegacionFirmaVO;
import es.altia.agora.interfaces.user.web.portafirmas.GlobalNames;
import es.altia.agora.interfaces.user.web.portafirmas.SessionManager;
import es.altia.util.commons.DateOperations;
import es.altia.util.struts.DefaultActionForm;
import es.altia.util.struts.PropertyValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * @version $\Date$ $\Revision$
 */
public class DelegacionFirmaActionForm extends DefaultActionForm {
    /*_______Constants______________________________________________*/
    private static final String FECHA_DESDE = "fechaDesde";
    private static final String FECHA_HASTA = "fechaHasta";
    private static final String USUARIO_DELEGADO = "usuarioDelegado";

    /*_______Atributes______________________________________________*/
    protected int pUsuario;
    protected int pUsuarioDelegado;
    protected String pNombreUsuarioDelegado;
    protected String pFechaDesde;
    protected String pFechaHasta;

    /*_______Operations_____________________________________________*/
    protected void reset() {
        pUsuario=-1;
        pUsuarioDelegado=-1;
        pNombreUsuarioDelegado=null;
        pFechaDesde=null;
        pFechaHasta=null;
    }//reset

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        final ActionErrors errors = super.validate(mapping, request);
        if (pUsuarioDelegado<=0) {
            errors.add(USUARIO_DELEGADO,new ActionError(PropertyValidator.MANDATORY_FIELD));
        }
        PropertyValidator.validateDate(errors,FECHA_DESDE,pFechaDesde,true,GlobalNames.DATE_FORMAT);
        PropertyValidator.validateDate(errors,FECHA_HASTA,pFechaHasta,true,GlobalNames.DATE_FORMAT);
        return errors;
    }//validate

    public void setDelegacionFirmaVO(DelegacionFirmaVO vo, HttpServletRequest request) {
        pUsuario=vo.getCodigoUsuario();
        pUsuarioDelegado=vo.getUsuarioDelegado();
        pFechaDesde=DateOperations.toString(vo.getFechaDesde(),GlobalNames.DATE_FORMAT);
        pFechaHasta=DateOperations.toString(vo.getFechaHasta(),GlobalNames.DATE_FORMAT);
        this.setPersistanceContext(vo.getPersistanceContext());
    }//setDelegacionFirmaVO


    public DelegacionFirmaPK getDelegacionFirmaPK(HttpServletRequest request) {
        final DelegacionFirmaPK result;
        if (pUsuario>0)
            result = new DelegacionFirmaPK(pUsuario);
        else if (SessionManager.isUserAuthenticated(request))
            result = new DelegacionFirmaPK(SessionManager.getAuthenticatedUser(request).getIdUsuario());
        else
            result = null;
        return result;
    }//getDelegacionFirmaPK

    public DelegacionFirmaVO getDelegacionFirmaVO(HttpServletRequest request) {
        final DelegacionFirmaVO result;
        Calendar fechaDesde = DateOperations.toCalendar(pFechaDesde,GlobalNames.DATE_FORMAT);
        Calendar fechaHasta = DateOperations.toCalendar(pFechaHasta,GlobalNames.DATE_FORMAT);
        result = new DelegacionFirmaVO(getDelegacionFirmaPK(request),pUsuarioDelegado,fechaDesde,fechaHasta);
        result.setPersistanceContext(this.getPersistanceContext());
        return result;
    }//getDelegacionFirmaVO








    public int getUsuario() {
        return pUsuario;
    }

    public void setUsuario(int usuario) {
        pUsuario = usuario;
    }

    public int getUsuarioDelegado() {
        return pUsuarioDelegado;
    }

    public void setUsuarioDelegado(int usuarioDelegado) {
        pUsuarioDelegado = usuarioDelegado;
    }

    public String getNombreUsuarioDelegado() {
        return pNombreUsuarioDelegado;
    }

    public void setNombreUsuarioDelegado(String nombreUsuarioDelegado) {
        pNombreUsuarioDelegado = nombreUsuarioDelegado;
    }

    public String getFechaDesde() {
        return pFechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        pFechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return pFechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        pFechaHasta = fechaHasta;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append(super.toString()).append(" ::: ");
        buf.append("DelegacionFirmaActionForm");
        buf.append("{pUsuario=").append(pUsuario);
        buf.append(",pUsuarioDelegado=").append(pUsuarioDelegado);
        buf.append(",pNombreUsuarioDelegado=").append(pNombreUsuarioDelegado);
        buf.append(",pFechaDesde=").append(pFechaDesde);
        buf.append(",pFechaHasta=").append(pFechaHasta);
        buf.append('}');
        return buf.toString();
    }

}//class
/*______________________________EOF_________________________________*/