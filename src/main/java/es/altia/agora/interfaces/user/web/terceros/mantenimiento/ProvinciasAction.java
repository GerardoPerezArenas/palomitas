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
 * <p>Descripción: Clase ProvinciasAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class ProvinciasAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(ProvinciasAction.class.getName());

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

    // Rellenamos el form de Mantenimientos
    if (form == null) {
      m_Log.debug("Rellenamos el form de Mantenimientos");
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
    PaisesManager paisesManager = PaisesManager.getInstance();
    AutonomiasManager autonomiasManager = AutonomiasManager.getInstance();
    ProvinciasManager provinciasManager = ProvinciasManager.getInstance();
    if ("cargar".equalsIgnoreCase(opcion)){
      Vector lista = paisesManager.getListaPaises(params);
      mantForm.setListaPaises(lista);
    }else if("cargarProvincias".equals(opcion)){
      String codPais = request.getParameter("codPais");
      Vector lista = new Vector();
      Vector listaAut = new Vector();
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",codPais);
      listaAut = autonomiasManager.getListaAutonomias(codPais,params);
      lista = provinciasManager.getListaProvincias(gVO,params);
      mantForm.setListaAutonomias(listaAut);
      mantForm.setListaProvincias(lista);
      opcion = "vuelveCargar";
    }else if("eliminar".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codigo",request.getParameter("txtCodigo"));
      Vector lista = new Vector();
      lista = provinciasManager.eliminarProvincia(gVO,params);
      if (lista!=null && lista.size()!=0) {
          gVO = (GeneralValueObject) lista.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	opcion = "noPuedeEliminar";
      } else {
        mantForm.setListaProvincias(lista);
        opcion = "vuelveCargar";
      }
    }else if("modificar".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codigo",request.getParameter("txtCodigo"));
      gVO.setAtributo("codAutonomia",request.getParameter("codAutonomia"));
      gVO.setAtributo("descripcion",request.getParameter("txtDescripcion"));
      gVO.setAtributo("nombreLargo",request.getParameter("txtNombreLargo"));
      Vector lista = provinciasManager.modificarProvincia(gVO,params);
      mantForm.setListaProvincias(lista);
      opcion = "vuelveCargar";
    }else if("alta".equals(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codigo",request.getParameter("txtCodigo"));
      gVO.setAtributo("codAutonomia",request.getParameter("codAutonomia"));
      gVO.setAtributo("descripcion",request.getParameter("txtDescripcion"));
      gVO.setAtributo("nombreLargo",request.getParameter("txtNombreLargo"));
      Vector lista = provinciasManager.altaProvincia(gVO,params);
      mantForm.setListaProvincias(lista);
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