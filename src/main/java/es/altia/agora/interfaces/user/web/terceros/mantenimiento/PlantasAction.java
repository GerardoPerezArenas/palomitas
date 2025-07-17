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
 * <p>Descripción: Clase PlantasAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */


public class PlantasAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(PlantasAction.class.getName());
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
    PlantasManager plantasManager = PlantasManager.getInstance();
    //Añadimos el tipo de ventana en el Form
    mantForm.setVentana(request.getParameter("ventana"));
    if ("cargar".equalsIgnoreCase(opcion))
    {
      //Cargamos la lista de Tipos de Documentos
      Vector listaPlantas = new Vector();
      listaPlantas = plantasManager.getListaPlantas(params);
      mantForm.setListaPlantas(listaPlantas);
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector listaPlantas = new Vector();
      String codigo = (String)gVO.getAtributo("codigo");
      listaPlantas = plantasManager.eliminarPlanta(codigo,params);
      if (listaPlantas!=null && listaPlantas.size()!=0) {
        gVO = (GeneralValueObject) listaPlantas.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	opcion = "noPuedeEliminar";
      } else {
        mantForm.setListaPlantas(listaPlantas);
        opcion = "cargarPlantas";
      }
    }
    else if("modificar".equals(opcion))
    {
      Vector listaPlantas = new Vector();
      listaPlantas = plantasManager.modificarPlanta(gVO,params);
      mantForm.setListaPlantas(listaPlantas);
      opcion = "cargarPlantas";
    }
    else if("alta".equals(opcion))
    {
      Vector listaPlantas = new Vector();
      listaPlantas = plantasManager.altaPlanta(gVO,params);
      mantForm.setListaPlantas(listaPlantas);
      opcion = "cargarPlantas";
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