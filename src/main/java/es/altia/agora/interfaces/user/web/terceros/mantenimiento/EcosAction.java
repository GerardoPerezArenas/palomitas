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
 * <p>Descripción: Clase EcosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @version 1.0
 */


public class EcosAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(EcosAction.class.getName());
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
    EcosManager ecosManager = EcosManager.getInstance();
    mantForm.setVentana(request.getParameter("ventana"));
    mantForm.setOpcion(opcion);
    GeneralValueObject gVO = recogerParametros(request);
    if ("cargar".equalsIgnoreCase(opcion)||"cargarEcoTerritorio".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO1 = new GeneralValueObject();
      gVO1.setAtributo("codPais",codPais);
      gVO1.setAtributo("codProvincia",codProvincia);
      Vector lista = provinciasManager.getListaProvincias(gVO1,params);
      Vector listaM = municipiosManager.getListaMunicipios(gVO1,params);
      mantForm.setListaProvincias(lista);
      mantForm.setListaMunicipios(listaM);
    }else if (opcion.equals("cargarMunicipios")){
      GeneralValueObject gVO1 = new GeneralValueObject();
      gVO1.setAtributo("codPais",request.getParameter("codPais"));
      gVO1.setAtributo("codProvincia",request.getParameter("codProvincia"));
      Vector muns = new Vector();
      muns = municipiosManager.getListaMunicipios(gVO1,params);
      mantForm.setListaMunicipios(muns);
    }else if(opcion.equals("cargarEcos")||opcion.equals("cargarEcosTodas")){
      Vector ecos = new Vector();
      if (opcion.equals("cargarEcosTodas")){      
      	ecos = ecosManager.getListaEcosTodas(gVO,params);
      	opcion = "cargarEcos"; 
      }else ecos = ecosManager.getListaEcos(gVO,params);
      mantForm.setListaEcos(ecos);
    }else if("eliminar".equalsIgnoreCase(opcion)){
      Vector ecos = new Vector();
      ecos = ecosManager.eliminarEco(gVO,params);
      if(ecos.size() != 0) {
        gVO = (GeneralValueObject) ecos.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	opcion = "noPuedeEliminar";
      } else {
        mantForm.setListaEcos(ecos);
        opcion = "cargarEcos";
      }
    }else if("modificar".equals(opcion)){
      Vector ecos = new Vector();
      ecos = ecosManager.modificarEco(gVO,params);
      mantForm.setListaEcos(ecos);
      opcion = "cargarEcos";
    }else if("alta".equals(opcion)){
      Vector ecos = new Vector();
      ecos = ecosManager.altaEco(gVO,params);
      mantForm.setListaEcos(ecos);
      opcion = "cargarEcos";
    }else if("modificarEcoTerritorio".equals(opcion)){
//      GeneralValueObject paramsPadron = ParametrosManager.getInstance().cargarParametrosPadron(params);
//      int mostrarFamilia = Integer.parseInt((String)paramsPadron.getAtributo("familias"));
      gVO = recogerParametros(request);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      String operacion = "";
//      if((mostrarFamilia==0)||(mostrarFamilia==2)) operacion = "NO_EXISTE_HOJA";
//      if(mostrarFamilia==1) operacion = "VARIAS_FAMILIAS";
      gVO.setAtributo("operacion",operacion);
//      gVO.setAtributo("familias",paramsPadron.getAtributo("familias"));
      mantForm.setOperacion(ecosManager.modificarEcoTerritorio(params,gVO));
      Vector ecos = new Vector();
      ecos = ecosManager.getListaEcosTodas(gVO,params);
      mantForm.setListaEcos(ecos);
      opcion = "cargarEcos";
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }
    return (mapping.findForward(opcion));
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codPais",request.getParameter("codPais"));
    gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
    gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
    gVO.setAtributo("codECO",request.getParameter("codECO"));
    gVO.setAtributo("codECOAntiguo",request.getParameter("codECOAntiguo"));
    gVO.setAtributo("descECO",request.getParameter("descECO"));
    gVO.setAtributo("nombreLargo",request.getParameter("nombreLargo"));
    gVO.setAtributo("codINE",request.getParameter("codINE"));
    gVO.setAtributo("idObjetoGrafico",request.getParameter("idObjetoGrafico"));
    gVO.setAtributo("situacion",request.getParameter("situacion"));
    gVO.setAtributo("fechaOperacion",request.getParameter("fechaOperacion"));
    gVO.setAtributo("generarOperaciones",request.getParameter("generarOperaciones"));
    return gVO;
  }
}