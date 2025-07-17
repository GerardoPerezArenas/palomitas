// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.administracion.mantenimiento.persistence.*;
import es.altia.agora.business.util.*;
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


public class ProcedimientosAction extends ActionSession  {

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
      m_Log.debug("Rellenamos el form de MantenimientosAdminForm");
      form = new MantenimientosAdminForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    MantenimientosAdminForm mantForm = (MantenimientosAdminForm)form;
    ProcedimientosManager mantManager = ProcedimientosManager.getInstance();
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = mantManager.getListaProcedimientos(params);
      mantForm.setListaProcedimientos(lista);
      Vector listaIdiomas = IdiomasManager.getInstance().getListaIdiomas(params);
      mantForm.setListaIdiomas(listaIdiomas);
    }else if("eliminar".equalsIgnoreCase(opcion)){
      String identificador = request.getParameter("identificador");
      String codCampoAntiguo = request.getParameter("codCampoAntiguo");
      String codIdiomaAntiguo = request.getParameter("codIdiomaAntiguo");
      Vector lista = new Vector();
      lista = mantManager.eliminarProcedimiento(identificador,codCampoAntiguo,codIdiomaAntiguo,params);
      mantForm.setListaProcedimientos(lista);
      opcion = "vuelveCargar";
    }else if("modificar".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codigoAntiguo",request.getParameter("identificador"));
      gVO.setAtributo("codCampoAntiguo",request.getParameter("codCampoAntiguo"));
      gVO.setAtributo("codIdiomaAntiguo",request.getParameter("codIdiomaAntiguo"));
      gVO.setAtributo("codigo",request.getParameter("Codigo"));
      gVO.setAtributo("descripcion",request.getParameter("Descripcion"));
      gVO.setAtributo("codCampo",request.getParameter("codCampo"));
      gVO.setAtributo("idioma",request.getParameter("codIdioma"));
      Vector lista = mantManager.modificarProcedimiento(gVO,params);
      mantForm.setListaProcedimientos(lista);
      opcion = "vuelveCargar";
    }else if("alta".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codigo",request.getParameter("Codigo"));
      gVO.setAtributo("descripcion",request.getParameter("Descripcion"));
      gVO.setAtributo("codCampo",request.getParameter("codCampo"));
      gVO.setAtributo("idioma",request.getParameter("codIdioma"));
      Vector lista = mantManager.altaProcedimiento(gVO,params);
      mantForm.setListaProcedimientos(lista);
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