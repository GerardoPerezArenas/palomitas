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


public class ManzanasAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(ManzanasAction.class.getName());
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
    //Instaciamos las clases necesarias para cargar las listas de datos
    //como provincias, entidades colectivas ,entidades singulares y municipios
    SeccionesManager seccionesManager = SeccionesManager.getInstance();
    DistritosManager distritosManager = DistritosManager.getInstance();
    ManzanasManager manzanasManager = ManzanasManager.getInstance();
    //Añadimos el tipo de ventana en el Form
    mantForm.setVentana(request.getParameter("ventana"));
    if ("cargar".equalsIgnoreCase(opcion))
    {
    	GeneralValueObject g = new GeneralValueObject();
      g.setAtributo("codPais",codPais);
      g.setAtributo("codProvincia",codProvincia);
      g.setAtributo("codMunicipio",codMunicipio); 
      Vector listaDistritos = distritosManager.getListaDistritos(g,params);
      mantForm.setListaDistritos(listaDistritos);
      Vector listaSecciones= new Vector();
      mantForm.setListaSecciones(listaSecciones);

    }
    else if(opcion.equals("cargarSecciones"))
    {
      Vector listaSecciones = new Vector();
      listaSecciones = seccionesManager.getListaSecciones(gVO,params);
      mantForm.setListaSecciones(listaSecciones);
      mantForm.setOperacion("cargarSecciones");
      opcion = "oculto";
    }
    else if(opcion.equals("cargarManzanas"))
    {
      Vector listaManzanas = new Vector();
      listaManzanas = manzanasManager.getListaManzanas(gVO,params);
      mantForm.setListaManzanas(listaManzanas);
      mantForm.setOperacion("cargarManzanas");
      opcion = "oculto";
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector listaManzanas = new Vector();
      listaManzanas = manzanasManager.eliminarManzana(gVO,params);
      if (listaManzanas!=null && listaManzanas.size()!=0) {
          gVO = (GeneralValueObject) listaManzanas.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	mantForm.setOperacion("noPuedeEliminar");
      } else {
        mantForm.setListaManzanas(listaManzanas);
        mantForm.setOperacion("cargarManzanas");
      }
      opcion = "oculto";
    }
    else if("modificar".equals(opcion))
    {
      Vector listaManzanas = new Vector();
      listaManzanas = manzanasManager.modificarManzana(gVO,params);
      mantForm.setListaManzanas(listaManzanas);
      mantForm.setOperacion("cargarManzanas");
      opcion = "oculto";
    }
    else if("alta".equals(opcion))
    {
      Vector listaManzanas = new Vector();
      listaManzanas = manzanasManager.altaManzana(gVO,params);
      mantForm.setListaManzanas(listaManzanas);
      mantForm.setOperacion("cargarManzanas");
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
    gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
    gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
    gVO.setAtributo("codDistrito",request.getParameter("codDistrito"));
    gVO.setAtributo("codSeccion",request.getParameter("cSeccion"));
    gVO.setAtributo("codManzana",request.getParameter("codManzana"));
    String letraSeccion = (String) request.getParameter("letraManzana");
    if(letraSeccion != null && !letraSeccion.equals("null") && !letraSeccion.equals("")) {
    	gVO.setAtributo("letraSeccion",letraSeccion);
    } else {
      gVO.setAtributo("letraSeccion"," ");
    }  
    gVO.setAtributo("descManzana",request.getParameter("descManzana"));
    return gVO;
  }
}