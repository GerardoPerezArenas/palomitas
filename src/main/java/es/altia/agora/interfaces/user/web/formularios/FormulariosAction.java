// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.formularios;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.interfaces.user.web.formularios.FormulariosForm;
import es.altia.agora.business.formularios.persistence.FormulariosManager;
import es.altia.agora.business.formularios.persistence.FichaFormularioManager;
import es.altia.agora.business.formularios.mantenimiento.persistence.TiposManager;
import es.altia.agora.business.util.*;
import es.altia.agora.business.sge.persistence.TramitacionManager;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ProcedimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES & AYTOS CPD</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */


public class FormulariosAction extends ActionSession  {

  public ActionForward performSession(ActionMapping mapping, ActionForm form,
                      HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

    m_Log.debug("perform");
    ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    ActionErrors errors = new ActionErrors();
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de BusquedaTerceros
    if (form == null) {
      m_Log.debug("Rellenamos el form de formularios");
      form = new FormulariosForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    FormulariosForm formForm = (FormulariosForm)form;
    FormulariosManager formManager = FormulariosManager.getInstance();

    FichaFormularioManager fichaManager = FichaFormularioManager.getInstance();
    if ("inicio".equalsIgnoreCase(opcion)){
      m_Log.debug("Iniciando la operacion...");
      Vector listaTipos = TiposManager.getInstance().getListaTipos(usuario.getParamsCon());
      Vector listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientos(usuario ,usuario.getParamsCon());
      Vector lista = FormulariosManager.getInstance().getListaFormulariosSinParametros(params);
      formForm.setListaTipos(listaTipos);
      formForm.setListaProcedimientos(listaProcedimientos);
      formForm.setListaFormularios(lista);
    } else if("buscarForms".equalsIgnoreCase(opcion)){
    	GeneralValueObject gVO = new GeneralValueObject();
        String codFormulario = request.getParameter("codFormulario");
        String descFormulario = request.getParameter("descFormulario");
        String codTipo = request.getParameter("codTipo");
        String codProcedimiento = request.getParameter("codProcedimiento");
        gVO.setAtributo("codFormulario",codFormulario);
        gVO.setAtributo("descFormulario",descFormulario);
        gVO.setAtributo("codTipo",codTipo);
        gVO.setAtributo("codProcedimiento",codProcedimiento);
    	Vector lista = formManager.getInstance().getListaFormularios(gVO,params);
    	formForm.setListaFormularios(lista);
    	opcion = "vuelveCargar";
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }
    m_Log.debug("perform");
    return (mapping.findForward(opcion));
  }
}