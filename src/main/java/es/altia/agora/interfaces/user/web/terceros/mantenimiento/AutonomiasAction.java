// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.terceros.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.business.terceros.ParametrosTerceroValueObject;
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
 * <p>Descripción: Clase EntidadesSingularesAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: ALTIA CONSULTORES</p>
 * @author Jorge Hombre Tuñas
 * @version 1.0
 */


public class AutonomiasAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(AutonomiasAction.class.getName());
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
    ParametrosTerceroValueObject paramsTercero = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    codPais = paramsTercero.getPais();
    codProvincia = paramsTercero.getProvincia();
    codMunicipio = paramsTercero.getMunicipio();
    String[] params = usuario.getParamsCon();
    //Instanciamos el Form de Terceros que usaremos como contenedor de
    //datos
    MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm)form;
    //Recogemos los parámetros del request, principalmente vienen de la jsp
    GeneralValueObject gVO = recogerParametros(request);
    PaisesManager paisesManager = PaisesManager.getInstance();
    AutonomiasManager autonomiasManager = AutonomiasManager.getInstance();
    //Añadimos el tipo de ventana en el Form
    mantForm.setVentana(request.getParameter("ventana"));
    if ("cargar".equalsIgnoreCase(opcion))
    {
      Vector listaPaises = paisesManager.getListaPaises(params);
      mantForm.setListaPaises(listaPaises);

    }
    else if(opcion.equals("cargarAutonomias"))
    {
      Vector listaAutonomias = new Vector();
      String codPais = (String) gVO.getAtributo("codPais");
      listaAutonomias = autonomiasManager.getListaAutonomias(codPais,params);
      mantForm.setListaAutonomias(listaAutonomias);
      mantForm.setOperacion("cargarAutonomias");
      opcion = "oculto";
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector listaAutonomias = new Vector();
      String codPais = (String) gVO.getAtributo("codPais");
      String codAutonomia = (String) gVO.getAtributo("codigo");
      listaAutonomias = autonomiasManager.eliminarAutonomia(codPais,codAutonomia,params);
      if (listaAutonomias!=null && listaAutonomias.size()!=0) {
        gVO = (GeneralValueObject) listaAutonomias.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	mantForm.setOperacion("noPuedeEliminar");
      } else {
        mantForm.setListaAutonomias(listaAutonomias);
        mantForm.setOperacion("cargarAutonomias");
      }
      opcion = "oculto";
    }
    else if("modificar".equals(opcion))
    {
      Vector listaAutonomias = new Vector();
      listaAutonomias = autonomiasManager.modificarAutonomia(gVO,params);
      mantForm.setListaAutonomias(listaAutonomias);
      mantForm.setOperacion("cargarAutonomias");
      opcion = "oculto";
    }
    else if("alta".equals(opcion))
    {
      Vector listaAutonomias = new Vector();
      listaAutonomias = autonomiasManager.altaAutonomia(gVO,params);
      mantForm.setListaAutonomias(listaAutonomias);
      mantForm.setOperacion("cargarAutonomias");
      opcion = "oculto";
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
    gVO.setAtributo("codPais",request.getParameter("codPais"));
    gVO.setAtributo("codigo",request.getParameter("codAutonomia"));
    gVO.setAtributo("descripcion",request.getParameter("nombre"));
    gVO.setAtributo("nombreLargo",request.getParameter("nombreLargo"));
    return gVO;
  }
}