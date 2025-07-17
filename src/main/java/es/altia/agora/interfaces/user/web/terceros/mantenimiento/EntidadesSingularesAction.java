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
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */


public class EntidadesSingularesAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(EntidadesSingularesAction.class.getName());
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
    ProvinciasManager provinciasManager = ProvinciasManager.getInstance();
    EntidadesSingularesManager entidadesSingularesManager = EntidadesSingularesManager.getInstance();
    EcosManager entidadesColectivasManager = EcosManager.getInstance();
    MunicipiosManager municipiosManager = MunicipiosManager.getInstance();
    //Añadimos el tipo de ventana en el Form
    mantForm.setVentana(request.getParameter("ventana"));
    mantForm.setOpcion(opcion);
    if ("cargar".equalsIgnoreCase(opcion)||"cargarEsiTerritorio".equalsIgnoreCase(opcion))
    {
      //Cargamos las privincias
      //Cargamos la lista de Municipios dependientes de la provincia por defecto
      GeneralValueObject gVOMunicipio = new GeneralValueObject();
      gVOMunicipio.setAtributo("codPais",codPais);
      gVOMunicipio.setAtributo("codProvincia",codProvincia);
      gVOMunicipio.setAtributo("codMunicipio",codMunicipio);
      Vector listaProvincias = provinciasManager.getListaProvincias(gVOMunicipio,params);
      mantForm.setListaProvincias(listaProvincias);
      Vector listaMunicipios = new Vector();
      listaMunicipios = municipiosManager.getListaMunicipios(gVOMunicipio,params);
      mantForm.setListaMunicipios(listaMunicipios);
      //Cargamos la lista de Entidades Colectivas dependientes de la provincia
      //y Municipio por defecto
      Vector listaEntColectivas = new Vector();
      listaEntColectivas = entidadesColectivasManager.getListaEcos(gVOMunicipio,params);
      mantForm.setListaEcos(listaEntColectivas);
    }
    else if(opcion.equals("cargarMunicipios"))
    {
      //Cargamos la lista de Municipios dependientes de una provincia
      Vector listaMunicipios = new Vector();
      listaMunicipios = municipiosManager.getListaMunicipios(gVO,params);
      mantForm.setListaMunicipios(listaMunicipios);
    }
    else if(opcion.equals("cargarEcos"))
    {
      //Cargamos las Entidades Colectivas dependientes de una provincia y Municipio
      Vector listaEntidadesColectivas = new Vector();
      listaEntidadesColectivas = entidadesColectivasManager.getListaEcos(gVO,params);
      mantForm.setListaEcos(listaEntidadesColectivas);
    }
    else if(opcion.equals("cargarEsis") || opcion.equals("cargarEsisTodas"))
    {
      //Cargamos las Entidades Singulares dependientes de una provincia,municipio
      //y entidad colectiva (opcional)
      Vector listaEntidadesSingulares = new Vector();
      listaEntidadesSingulares = entidadesSingularesManager.getListaEntidadesSingulares(gVO,params);
	  if (opcion.equals("cargarEsisTodas")){
		listaEntidadesSingulares = entidadesSingularesManager.getListaEntidadesSingularesTodas(gVO,params);
	  } else {
		listaEntidadesSingulares = entidadesSingularesManager.getListaEntidadesSingulares(gVO,params);
      }
	  mantForm.setListaEsis(listaEntidadesSingulares); 
      GeneralValueObject g = new GeneralValueObject();      
      g.setAtributo("codPais",codPais);
      g.setAtributo("codProvincia",codProvincia);
      g.setAtributo("codMunicipio",codMunicipio);
      Vector listaEntidadesSingularesCompleta = new Vector();
      if (opcion.equals("cargarEsisTodas")){      
      	listaEntidadesSingularesCompleta = entidadesSingularesManager.getListaEntidadesSingularesTodas(g,params);
      	opcion = "cargarEsis";
      } else listaEntidadesSingularesCompleta = entidadesSingularesManager.getListaEntidadesSingulares(g,params);
      
      mantForm.setListaEsisCompleta(listaEntidadesSingularesCompleta);
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector listaEntidadesSingulares = new Vector();
      listaEntidadesSingulares = entidadesSingularesManager.eliminarEntidadSingular(gVO,params);
      if(listaEntidadesSingulares.size() != 0 ) {
        gVO = (GeneralValueObject) listaEntidadesSingulares.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	opcion = "noPuedeEliminar";
      } else {
        mantForm.setListaEsis(listaEntidadesSingulares);
        opcion = "cargarEsis";
      }
    }
    else if("modificar".equals(opcion))
    {
      Vector listaEntidadesSingulares = new Vector();
      listaEntidadesSingulares = entidadesSingularesManager.modificarEntidadSingular(gVO,params);
      mantForm.setListaEsis(listaEntidadesSingulares);
      opcion = "cargarEsis";
    }else if("modificarEsiTerritorio".equals(opcion)){
//      GeneralValueObject paramsPadron = ParametrosManager.getInstance().cargarParametrosPadron(params);
//      int mostrarFamilia = Integer.parseInt((String)paramsPadron.getAtributo("familias"));
    	
      gVO = recogerParametros(request);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      String operacion = "";
//      if((mostrarFamilia==0)||(mostrarFamilia==2)) operacion = "NO_EXISTE_HOJA";
//      if(mostrarFamilia==1) operacion = "VARIAS_FAMILIAS";
      gVO.setAtributo("operacion",operacion);
//      gVO.setAtributo("familias",paramsPadron.getAtributo("familias"));
      mantForm.setOperacion(entidadesSingularesManager.modificarEsiTerritorio(params,gVO));
      Vector esis = new Vector();
      esis = entidadesSingularesManager.getListaEntidadesSingularesTodas(gVO,params);
      mantForm.setListaEsis(esis);
      opcion = "cargarEsis";
    }else if("alta".equals(opcion))
    {
      Vector listaEntidadesSingulares = new Vector();
      listaEntidadesSingulares = entidadesSingularesManager.altaEntidadSingular(gVO,params);
      mantForm.setListaEsis(listaEntidadesSingulares);
      opcion = "cargarEsis";
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
    gVO.setAtributo("codEntidadSingular",request.getParameter("codEntidadSingular"));
    gVO.setAtributo("codEntidadColectiva",request.getParameter("codEntidadColectiva"));
    gVO.setAtributo("digitoControl",request.getParameter("digitoControl"));
    gVO.setAtributo("ine",request.getParameter("ine"));
    gVO.setAtributo("nombreOficial",request.getParameter("nombreOficial"));
    gVO.setAtributo("nombreLargo",request.getParameter("nombreLargo"));
    gVO.setAtributo("kmtsACapital",request.getParameter("kmtsACapital"));
    gVO.setAtributo("altitud",request.getParameter("altitud"));
    gVO.setAtributo("imagen",request.getParameter("imagen"));
    gVO.setAtributo("situacion",request.getParameter("situacion"));
    gVO.setAtributo("codECO",request.getParameter("codEco"));
    gVO.setAtributo("codESI",request.getParameter("codEntidadSingular"));
    gVO.setAtributo("descESI",request.getParameter("nombreOficial"));
    gVO.setAtributo("codINE",request.getParameter("ine"));
    gVO.setAtributo("fechaOperacion",request.getParameter("fechaOperacion"));
    gVO.setAtributo("generarOperaciones",request.getParameter("generarOperaciones"));


    return gVO;
  }
}