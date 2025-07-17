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
 * <p>Descripción: Clase OrganismosExternosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */


public class OrganismosExternosAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(OrganismosExternosAction.class.getName());
  String codPais = "";
  String codProvincia = "";
  String codMunicipio = "";

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
      m_Log.debug("Rellenamos el form de MantenimientosTerceros");
      form = new MantenimientosTercerosForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    //Cogemos los parametros contenidos en la sesión
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    String[] params = usuario.getParamsCon();
    //Instanciamos el Form de Terceros que usaremos como contenedor de
    //datos
    MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm)form;
    //Recogemos los parámetros del request, principalmente vienen de la jsp
    GeneralValueObject gVO = recogerParametros(request);
    //Instaciamos las clases necesarias para cargar la lista de datos
    //que será la de los tipos de Documentos
    OrganismosExternosManager organismosExternosManager = OrganismosExternosManager.getInstance();
    //Añadimos el tipo de ventana en el Form
    mantForm.setVentana(request.getParameter("ventana"));
    if ("cargar".equalsIgnoreCase(opcion))
    {
      //Cargamos la lista de Tipos de Documentos
      Vector listaOrganismosExternos = new Vector();
      listaOrganismosExternos = organismosExternosManager.getListaOrganismosExternos(params);
      mantForm.setListaOrganismosExternos(listaOrganismosExternos);
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector listaOrganismosExternos = new Vector();
      String codigo = new String((String)gVO.getAtributo("codigo"));
      listaOrganismosExternos = organismosExternosManager.eliminarOrganismoExterno(codigo,params);
      mantForm.setListaOrganismosExternos(listaOrganismosExternos);
      opcion = "cargarOrgmsExternos";
    }
    else if("modificar".equals(opcion))
    {
      Vector listaOrganismosExternos = new Vector();
      listaOrganismosExternos = organismosExternosManager.modificarOrganismoExterno(gVO,params);
      mantForm.setListaOrganismosExternos(listaOrganismosExternos);
      opcion = "cargarOrgmsExternos";
    }
    else if("alta".equals(opcion))
    {
      Vector listaOrganismosExternos = new Vector();
      listaOrganismosExternos = organismosExternosManager.altaOrganismoExterno(gVO,params);
      mantForm.setListaOrganismosExternos(listaOrganismosExternos);
      opcion = "cargarOrgmsExternos";
    }
    else if("salir".equals(opcion))
    {
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }
    else
    {
      opcion = mapping.getInput();
    }
    return (mapping.findForward(opcion));
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    //Recogemos y asignamos al gVO los parametros con los nombres que les damos
    //en la jsp
    gVO.setAtributo("codigo",request.getParameter("codigo"));
    gVO.setAtributo("descripcion",request.getParameter("descripcion"));
    gVO.setAtributo("codigoAntiguo",request.getParameter("codigoAntiguo"));
    return gVO;
  }
}