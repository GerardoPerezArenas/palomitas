// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.terceros.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.terceros.mantenimiento.persistence.*;
import es.altia.agora.business.util.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;


/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase MantenimientosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class PaisesAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(PaisesAction.class.getName());

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
      m_Log.debug("Rellenamos el form de BusquedaTerceros");
      form = new MantenimientosTercerosForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm)form;
    PaisesManager mantManager = PaisesManager.getInstance();
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = mantManager.getListaPaises(params);
      mantForm.setListaPaises(lista);
    }else if("eliminar".equalsIgnoreCase(opcion)){
      String identificador = request.getParameter("identificador");
      Vector lista = new Vector();
      lista = mantManager.eliminarPais(identificador,params);
      GeneralValueObject gVO  =new GeneralValueObject();
      if (lista!=null && lista.size()!=0) {
        gVO = (GeneralValueObject) lista.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
          opcion = "noPuedeEliminar";
      } else {
        mantForm.setListaPaises(lista);
        opcion = "vuelveCargar";
      }
    }else if("modificar".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codigo",request.getParameter("txtCodigo"));
      gVO.setAtributo("descripcion",request.getParameter("txtDescripcion"));
      gVO.setAtributo("nombreLargo",request.getParameter("txtNombreLargo"));
      gVO.setAtributo("digitoControl",request.getParameter("txtDigitoControl"));
      Vector lista = mantManager.modificarPais(gVO,params);
      mantForm.setListaPaises(lista);
      opcion = "vuelveCargar";
    }else if("alta".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codigo",request.getParameter("txtCodigo"));
      gVO.setAtributo("descripcion",request.getParameter("txtDescripcion"));
      gVO.setAtributo("nombreLargo",request.getParameter("txtNombreLargo"));
      gVO.setAtributo("digitoControl",request.getParameter("txtDigitoControl"));
      Vector lista = mantManager.altaPais(gVO,params);
      mantForm.setListaPaises(lista);
      opcion = "vuelveCargar";
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }
    return (mapping.findForward(opcion));
  }
}