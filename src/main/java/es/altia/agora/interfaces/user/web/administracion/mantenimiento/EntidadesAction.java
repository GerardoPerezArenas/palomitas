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
 * <p>Descripción: Clase EntidadesAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class EntidadesAction extends ActionSession  {

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
    OrganizacionesManager orgManager = OrganizacionesManager.getInstance();
    EntidadesManager mantManager = EntidadesManager.getInstance(); 
    GeneralValueObject gVO = recogerParametros(request);
    mantForm.setOperacion("");
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = orgManager.getListaOrganizaciones(params);
      mantForm.setListaOrganizaciones(lista);
    }else if ("cargarEntidades".equalsIgnoreCase(opcion)){
      Vector lista = mantManager.getListaEntidades(gVO,params);
      mantForm.setListaEntidades(lista);
      opcion = "vuelveCargar";
    }else if("eliminar".equalsIgnoreCase(opcion)){
      Vector lista = new Vector();
      lista = mantManager.eliminarEntidad(gVO,params);
      if (lista!=null && lista.size()!=0) {
          gVO = (GeneralValueObject) lista.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	mantForm.setOperacion("noPuedeEliminar");
      } else {
        mantForm.setListaEntidades(lista);
        mantForm.setOperacion("cargarLista");
      }
      opcion = "vuelveCargar";
    }else if("modificar".equals(opcion)){
      Vector lista = mantManager.modificarEntidad(gVO,params);
      mantForm.setListaEntidades(lista);
      opcion = "vuelveCargar";
    }else if("alta".equals(opcion)){
      Vector lista = mantManager.altaEntidad(gVO,params);
      mantForm.setListaEntidades(lista);
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

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codOrganizacion",request.getParameter("codOrganizacion"));
    gVO.setAtributo("codEntidad",request.getParameter("codEntidad"));
    gVO.setAtributo("descEntidad",request.getParameter("descEntidad"));
    gVO.setAtributo("tipoEntidad",request.getParameter("tipoEntidad"));
    gVO.setAtributo("directorio",request.getParameter("directorio"));
    gVO.setAtributo("codEntidadAntiguo",request.getParameter("codEntidadAntiguo"));
    return gVO;
  }
}