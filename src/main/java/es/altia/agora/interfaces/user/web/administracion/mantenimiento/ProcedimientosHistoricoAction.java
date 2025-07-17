/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.business.administracion.mantenimiento.persistence.*;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.flexia.historico.expediente.vo.ProcedimientoHistoricoVO;
import java.io.*; 
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 *
 * @author adrian.freixeiro
 */
public class ProcedimientosHistoricoAction extends ActionSession  {

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    m_Log.debug("perform");
    HttpSession session = request.getSession();
    
    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
    UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
    traductor.setApl_cod(usuarioVO.getAppCod());
    traductor.setIdi_cod(usuarioVO.getIdioma());
    
    // Validaremos los parametros del request especificados
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    if (form == null) {
      m_Log.debug("Rellenamos el form de ProcedimientosHistoricoForm");
      form = new ProcedimientosHistoricoForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    int codOrganizacion = usuario.getOrgCod();
    String[] params = usuario.getParamsCon();
    ProcedimientosHistoricoForm procHistForm = (ProcedimientosHistoricoForm) form;
    ProcedimientosHistoricoManager procHistManager = ProcedimientosHistoricoManager.getInstance();
    String resp = "";
    
    if ("cargar".equalsIgnoreCase(opcion)){
      ArrayList<ProcedimientoHistoricoVO> procedimientos = procHistManager.getListaProcedimientos(params,codOrganizacion);
      procHistForm.setProcedimientos(procedimientos);
    } else if("alta".equals(opcion)){
      procHistForm.setCodOrganizacionEditar(codOrganizacion);
      resp = procHistManager.grabarProcedimiento(params,procHistForm.getProcedimientoEditar());
      if ("".equals(resp)) {
          resp = "msgDatosGrabados";
          procHistForm.getProcedimientos().add(procHistForm.getProcedimientoEditar());
      }
    } else if("modificar".equals(opcion)){
      procHistForm.setCodOrganizacionEditar(codOrganizacion);
      resp = procHistManager.modificarProcedimiento(params,procHistForm.getProcedimientoEditar());
      if ("".equals(resp)) {
          resp = "msgDatosGrabados";
          for (ProcedimientoHistoricoVO procedimiento : procHistForm.getProcedimientos()) {
              if (procedimiento.getCodProcedimiento().equals(procHistForm.getCodProcedimientoEditar())){
                  procedimiento.setMeses(procHistForm.getMesesEditar());
              }
          }
      }
    } else if("eliminar".equals(opcion)){
        procHistForm.setCodOrganizacionEditar(codOrganizacion);
        resp = procHistManager.eliminarProcedimiento(params,procHistForm.getProcedimientoEditar());
        if ("".equals(resp)) {
            resp = "etiqAsunEliminado";
            for (Iterator<ProcedimientoHistoricoVO> iterador = procHistForm.getProcedimientos().iterator(); iterador.hasNext(); ) {
                ProcedimientoHistoricoVO procedimiento = iterador.next();
                if(procedimiento.getCodProcedimiento().equals(procHistForm.getCodProcedimientoEditar())){
                  iterador.remove();
                }
            }
        }
    }
    procHistForm.setResultado(traductor.getDescripcion(resp));
    
    procHistForm.setProcedimientoEditar(new ProcedimientoHistoricoVO());
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }
}