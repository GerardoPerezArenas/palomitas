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
 * <p>Descripción: Clase NucleosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class NucleosAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(NucleosAction.class.getName());
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
    EntidadesSingularesManager esisManager = EntidadesSingularesManager.getInstance();
    EcosManager ecosManager = EcosManager.getInstance();
    NucleosManager nucleosManager = NucleosManager.getInstance();
    mantForm.setVentana(request.getParameter("ventana"));
    mantForm.setOpcion(opcion);
    GeneralValueObject gVO = recogerParametros(request,usuario);
    if ("cargar".equalsIgnoreCase(opcion)||"cargarNucTerritorio".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO1 = new GeneralValueObject();
      gVO1.setAtributo("codPais",codPais);
      gVO1.setAtributo("codProvincia",codProvincia);
      gVO1.setAtributo("codMunicipio",codMunicipio);
      gVO1.setAtributo("codEntidadColectiva","");
      Vector lista = provinciasManager.getListaProvincias(gVO1,params);
      Vector listaM = municipiosManager.getListaMunicipios(gVO1,params);
      Vector listaEsis = esisManager.getListaEntidadesSingulares(gVO1,params);
      Vector listaEcos = ecosManager.getListaEcos(gVO1,params);
      mantForm.setListaProvincias(lista);
      mantForm.setListaMunicipios(listaM);
      mantForm.setListaEsis(listaEsis);
      mantForm.setListaEcos(listaEcos);
    }else if (opcion.equals("cargarMunicipios")){
      GeneralValueObject gVO1 = new GeneralValueObject();
      gVO1.setAtributo("codPais",request.getParameter("codPais"));
      gVO1.setAtributo("codProvincia",request.getParameter("codProvincia"));
      Vector muns = new Vector();
      muns = municipiosManager.getListaMunicipios(gVO1,params);
      mantForm.setListaMunicipios(muns);
    }else if (opcion.equals("cargarEsis")){
      GeneralValueObject gVO1 = new GeneralValueObject();
      gVO1.setAtributo("codPais",request.getParameter("codPais"));
      gVO1.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO1.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO1.setAtributo("codECO",request.getParameter("codECO"));
      Vector esis = new Vector();
      esis = esisManager.getListaEntidadesSingulares(gVO1,params);
      mantForm.setListaEsis(esis);
    }else if(opcion.equals("cargarNucleos")||opcion.equals("cargarNucleosTodos")){
      Vector nucleos = new Vector();
      if (opcion.equals("cargarNucleosTodos")){
	  	nucleos = nucleosManager.getListaNucleosTodos(gVO,params);
	  	opcion = "cargarNucleos";
      } else{ 
      	nucleos = nucleosManager.getListaNucleos(gVO,params);
      }      
      mantForm.setListaNucleos(nucleos);
    }else if("eliminar".equalsIgnoreCase(opcion)){
      Vector nucleos = new Vector();
      nucleos = nucleosManager.eliminarNucleo(gVO,params);
      if(nucleos.size() != 0) {
        gVO = (GeneralValueObject) nucleos.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	opcion = "noPuedeEliminar";
      } else {
	      mantForm.setListaNucleos(nucleos);
	      opcion = "cargarNucleos";
      }
    }else if("modificar".equals(opcion)){
      Vector nucleos = new Vector();
      nucleos = nucleosManager.modificarNucleo(gVO,params);
      mantForm.setListaNucleos(nucleos);
      opcion = "cargarNucleos";
    }else if("alta".equals(opcion)){
      Vector nucleos = new Vector();
      nucleos = nucleosManager.altaNucleo(gVO,params);
      mantForm.setListaNucleos(nucleos);
      opcion = "cargarNucleos";
    }else if("modificarNucTerritorio".equals(opcion)){
//      GeneralValueObject paramsPadron = ParametrosManager.getInstance().cargarParametrosPadron(params);
//      int mostrarFamilia = Integer.parseInt((String)paramsPadron.getAtributo("familias"));
    	
      gVO = recogerParametros(request,usuario);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      String operacion = "";
//      if((mostrarFamilia==0)||(mostrarFamilia==2)) operacion = "NO_EXISTE_HOJA";
//      if(mostrarFamilia==1) operacion = "VARIAS_FAMILIAS";
      gVO.setAtributo("operacion",operacion);
//      gVO.setAtributo("familias",paramsPadron.getAtributo("familias"));
      mantForm.setOperacion(nucleosManager.modificarNucTerritorio(params,gVO));
      Vector nucleos = new Vector();
      nucleos = nucleosManager.getListaNucleosTodos(gVO,params);
      mantForm.setListaNucleos(nucleos);
      opcion = "cargarNucleos";
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }
    return (mapping.findForward(opcion));
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request,UsuarioValueObject usuario){
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("concatenar","");// PARA OBTENER LOS NOMBRES DE LOS NÚCLEOS SIN CONCATENAR

    gVO.setAtributo("codPais",request.getParameter("codPais"));
    gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
    gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
    gVO.setAtributo("codESI",request.getParameter("codESI"));
    gVO.setAtributo("codNUC",request.getParameter("codNUC"));
    gVO.setAtributo("codNUCAntiguo",request.getParameter("codNUCAntiguo"));
    gVO.setAtributo("descNUC",request.getParameter("descNUC"));
    gVO.setAtributo("nombreLargo",request.getParameter("nombreLargo"));
    gVO.setAtributo("codINE",request.getParameter("codINE"));
    gVO.setAtributo("idObjetoGrafico",request.getParameter("idObjetoGrafico"));
    gVO.setAtributo("fechaOperacion",request.getParameter("fechaOperacion"));
    gVO.setAtributo("generarOperaciones",request.getParameter("generarOperaciones"));
    String situacion = request.getParameter("situacion");
    gVO.setAtributo("situacion",situacion);
    String fechaBaja = "null";
    String usuarioBaja = "null";
    if("B".equals(situacion)){
      fechaBaja = "SYSDATE";
      usuarioBaja = String.valueOf(usuario.getIdUsuario());
    }
    gVO.setAtributo("fechaBaja",fechaBaja);
    gVO.setAtributo("usuarioBaja",usuarioBaja);
    return gVO;
  }
}