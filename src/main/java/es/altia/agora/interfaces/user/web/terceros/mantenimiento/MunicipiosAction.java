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
 * <p>Descripción: Clase MunicipiosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Fernando Rueda Rueda
 * @version 1.0
 */


public class MunicipiosAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(MunicipiosAction.class.getName());
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
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    ParametrosTerceroValueObject paramsTercero = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    codPais = paramsTercero.getPais();
    codProvincia = paramsTercero.getProvincia();
    codMunicipio = paramsTercero.getMunicipio();
    String[] params = usuario.getParamsCon();
    MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm)form;
    ProvinciasManager provinciasManager = ProvinciasManager.getInstance();
    MunicipiosManager municipiosManager = MunicipiosManager.getInstance();
    mantForm.setVentana(request.getParameter("ventana"));
    GeneralValueObject gVO = recogerParametros(request);
    if ("cargar".equalsIgnoreCase(opcion))
    {
      GeneralValueObject gVO1 = new GeneralValueObject();
      gVO1.setAtributo("codPais",codPais);
      gVO1.setAtributo("codProvincia",codProvincia);
      Vector lista = provinciasManager.getListaProvincias(gVO1,params);
      mantForm.setListaProvincias(lista);
    }
    else if(opcion.equals("cargarMunicipios"))
    {
      Vector municipios = new Vector();
      municipios = municipiosManager.getListaMunicipios(gVO,params);
      mantForm.setListaMunicipios(municipios);
    }
    else if("eliminar".equalsIgnoreCase(opcion))
    {
      Vector municipios = new Vector();
      municipios = municipiosManager.eliminarMunicipio(gVO,params);
      if (municipios!=null && municipios.size()!=0) {
          gVO = (GeneralValueObject) municipios.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if (m_Log.isDebugEnabled()) m_Log.debug("el puede eliminar es : " + puedeEliminar);
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	opcion = "noPuedeEliminar";
      } else {
        mantForm.setListaMunicipios(municipios);
        opcion = "cargarMunicipios";
      }
    }
    else if("modificar".equals(opcion))
    {
      Vector municipios = new Vector();
      municipios = municipiosManager.modificarMunicipio(gVO,params);
      mantForm.setListaMunicipios(municipios);
      opcion = "cargarMunicipios";
    }
    else if("alta".equals(opcion))
    {
      Vector municipios = new Vector();
      municipios = municipiosManager.altaMunicipio(gVO,params);
      mantForm.setListaMunicipios(municipios);
      opcion = "cargarMunicipios";
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
    gVO.setAtributo("codPais",request.getParameter("codPais"));
    gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
    gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
    if (request.getParameter("nombreOficial")==null || request.getParameter("nombreOficial").equals("")) {
        gVO.setAtributo("nombreOficial","");
    } else {
        gVO.setAtributo("nombreOficial",request.getParameter("nombreOficial"));
    }
    if (request.getParameter("nombreLargo")==null || request.getParameter("nombreLargo").equals("")) {
        gVO.setAtributo("nombreLargo","");
    } else {
        gVO.setAtributo("nombreLargo",request.getParameter("nombreLargo"));
    }
    if (request.getParameter("partidoJudicial")==null || request.getParameter("partidoJudicial").equals("")) {
        gVO.setAtributo("partidoJudicial","null");
    } else {
        gVO.setAtributo("partidoJudicial",request.getParameter("partidoJudicial"));
    }
    if (request.getParameter("comarca")==null || request.getParameter("comarca").equals("")) {
        gVO.setAtributo("comarca","null");
    } else {
        gVO.setAtributo("comarca",request.getParameter("comarca"));
    }
    if (request.getParameter("digitoControl")==null || request.getParameter("digitoControl").equals("")) {
        gVO.setAtributo("digitoControl","");
    } else {
        gVO.setAtributo("digitoControl",request.getParameter("digitoControl"));
    }
    if (request.getParameter("superficie")==null || request.getParameter("superficie").equals("")) {
        gVO.setAtributo("superficie","null");
    } else {
        gVO.setAtributo("superficie",request.getParameter("superficie"));
    }
    if (request.getParameter("altitud")==null || request.getParameter("altitud").equals("")) {
        gVO.setAtributo("altitud","null");
    } else {
        gVO.setAtributo("altitud",request.getParameter("altitud"));
    }
    if (request.getParameter("kmtsACapital")==null || request.getParameter("kmtsACapital").equals("")) {
        gVO.setAtributo("kmtsACapital","null");
    } else {
        gVO.setAtributo("kmtsACapital",request.getParameter("kmtsACapital"));
    }
    if (request.getParameter("latitudNorte")==null || request.getParameter("latitudNorte").equals("")) {
        gVO.setAtributo("latitudNorte","null");
    } else {
        gVO.setAtributo("latitudNorte",request.getParameter("latitudNorte"));
    }
    if (request.getParameter("latitudSur")==null || request.getParameter("latitudSur").equals("")) {
        gVO.setAtributo("latitudSur","null");
    } else {
        gVO.setAtributo("latitudSur",request.getParameter("latitudSur"));
    }
    if (request.getParameter("longitudEste")==null || request.getParameter("longitudEste").equals("")) {
        gVO.setAtributo("longitudEste","null");
    } else {
        gVO.setAtributo("longitudEste",request.getParameter("longitudEste"));
    }
    if (request.getParameter("longitudOeste")==null || request.getParameter("longitudOeste").equals("")) {
        gVO.setAtributo("longitudOeste","null");
    } else {
        gVO.setAtributo("longitudOeste",request.getParameter("longitudOeste"));
    }
    if (request.getParameter("situacion")==null || request.getParameter("situacion").equals("")) {
        gVO.setAtributo("situacion","null");
    } else {
        gVO.setAtributo("situacion",request.getParameter("situacion"));
    }
    return gVO;
  }
}