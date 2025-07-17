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
 * <p>Descripción: Clase TramerosAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class TramerosAction extends ActionSession  {
  protected static Log m_Log =
          LogFactory.getLog(TramerosAction.class.getName());

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
    NucleosManager nucleosManager = NucleosManager.getInstance();
    TiposNumeracionManager tipNumManager = TiposNumeracionManager.getInstance();
    DistritosManager distritosManager = DistritosManager.getInstance();
    SeccionesManager secManager = SeccionesManager.getInstance();
    SubSeccionesManager subSeccionesManager = SubSeccionesManager.getInstance();
    ManzanasManager manzanasManager = ManzanasManager.getInstance();
    ViasManager viasManager = ViasManager.getInstance();
    CodPostalesManager codPostalesManager = CodPostalesManager.getInstance();
    TramerosManager tramerosManager = TramerosManager.getInstance();
    mantForm.setVentana(request.getParameter("ventana"));
    if ("cargar".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",codPais);
      gVO.setAtributo("codProvincia",codProvincia);
      gVO.setAtributo("codMunicipio",codMunicipio);
      gVO.setAtributo("codECO","");
      gVO.setAtributo("codEntidadColectiva","");
      gVO.setAtributo("codESI","");
      gVO.setAtributo("codNUC","");
      gVO.setAtributo("numDesde","");
      gVO.setAtributo("tipoNumeracion","");
      Vector lista = provinciasManager.getListaProvincias(gVO,params);
      Vector listaM = municipiosManager.getListaMunicipios(gVO,params);
      Vector listaEsis = esisManager.getListaEntidadesSingulares(gVO,params);
      Vector lista1 = tipNumManager.getListaTiposNumeraciones(params);
      Vector listaCP = codPostalesManager.getListaCodPostales(params,gVO);
      Vector listaDistritos = distritosManager.getListaDistritos(gVO,params);
      Vector listaVias = new Vector();
      Vector nucleos = new Vector();
      nucleos = nucleosManager.getListaNucleos(gVO,params);
      mantForm.setListaNucleos(nucleos);
	  // Cambio combo viales
	  listaVias = viasManager.getListaViasSolas(params,gVO);
	  // Fin cambio combo viales
      mantForm.setListaProvincias(lista);
      mantForm.setListaMunicipios(listaM);
      mantForm.setListaEsis(listaEsis);
      mantForm.setListaNumeraciones(lista1);
      mantForm.setListaVias(listaVias);
      mantForm.setListaCodPostales(listaCP);
      mantForm.setListaDistritos(listaDistritos);
    }else if ("cargarListasBusqueda".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codECO","");
      gVO.setAtributo("codEntidadColectiva","");
      gVO.setAtributo("codESI","");
      gVO.setAtributo("codNUC","");
      gVO.setAtributo("numDesde","");
      gVO.setAtributo("tipoNumeracion","");
      Vector listaEsis = esisManager.getListaEntidadesSingulares(gVO,params);
      Vector listaCP = codPostalesManager.getListaCodPostales(params,gVO);
      Vector listaVias = new Vector();
      listaVias = viasManager.getListaVias(params,gVO);
      mantForm.setListaEsis(listaEsis);
      mantForm.setListaVias(listaVias);
      mantForm.setListaCodPostales(listaCP);
    }else if ("cargarListasRejilla".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codDistrito",request.getParameter("codDistrito"));
      gVO.setAtributo("codSeccion",request.getParameter("codSeccion"));
      gVO.setAtributo("letraSeccion",request.getParameter("letraSeccion"));
      Vector subSecciones = new Vector();
      Vector manzanas = new Vector();
      subSecciones = subSeccionesManager.getListaSubSecciones(gVO,params);
      manzanas = manzanasManager.getListaManzanas(gVO,params);
      mantForm.setListaManzanas(manzanas);
      mantForm.setListaSubSecciones(subSecciones);
    } else if (opcion.equals("cargarSubSecciones")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codDistrito",request.getParameter("codDistrito"));
      gVO.setAtributo("codSeccion",request.getParameter("codSeccion"));
      gVO.setAtributo("letraSeccion",request.getParameter("letraSeccion"));
      Vector subSecciones = new Vector();
      subSecciones = subSeccionesManager.getListaSubSecciones(gVO,params);
      mantForm.setListaSubSecciones(subSecciones);
    } else if (opcion.equals("cargarManzanas")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codDistrito",request.getParameter("codDistrito"));
      gVO.setAtributo("codSeccion",request.getParameter("codSeccion"));
      gVO.setAtributo("letraSeccion",request.getParameter("letraSeccion"));
      Vector manzanas = new Vector();
      manzanas = manzanasManager.getListaManzanas(gVO,params);
      mantForm.setListaManzanas(manzanas);
    } else if (opcion.equals("cargarMunicipios")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      Vector muns = new Vector();
      muns = municipiosManager.getListaMunicipios(gVO,params);
      mantForm.setListaMunicipios(muns);
    }else if(opcion.equals("cargarTramos")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codESI",request.getParameter("codESI"));
      gVO.setAtributo("codNUC",request.getParameter("codNUC"));
      gVO.setAtributo("idVia",request.getParameter("idVia"));
      gVO.setAtributo("codTipoNumeracion",request.getParameter("codTipoNumeracion"));
      gVO.setAtributo("idESI",request.getParameter("idESI"));
      gVO.setAtributo("idNucleo",request.getParameter("idNucleo"));
      Vector tramos = new Vector();
      tramos = tramerosManager.getListaTrameros(gVO,params);
      mantForm.setListaTrameros(tramos);
    }else if(opcion.equals("cargarNucleos")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codECO","");
      gVO.setAtributo("codESI",request.getParameter("codESITramo"));
      Vector nucleos = new Vector();
      nucleos = nucleosManager.getListaNucleos(gVO,params);
      mantForm.setListaNucleos(nucleos);
    }else if(opcion.equals("cargarVias")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codECO",request.getParameter("codECO"));
      gVO.setAtributo("codESI",request.getParameter("codESI"));
      gVO.setAtributo("codNUC",request.getParameter("codNUC"));
      gVO.setAtributo("numDesde","");
      gVO.setAtributo("tipoNumeracion","");
      Vector vias = new Vector();
      vias = viasManager.getListaViasSolas(params,gVO);
      mantForm.setListaVias(vias);
    }else if(opcion.equals("cargarDistritos")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      Vector distritos = new Vector();
      distritos = distritosManager.getListaDistritos(gVO,params);
      mantForm.setListaDistritos(distritos);
    }else if(opcion.equals("cargarSecciones")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
      gVO.setAtributo("codDistrito",request.getParameter("codDistrito"));
      Vector secciones = new Vector();
      secciones = secManager.getListaSecciones(gVO,params);
      mantForm.setListaSecciones(secciones);
    }else if("eliminar".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codTipoNumeracion",request.getParameter("codTipoNumeracion"));
      gVO.setAtributo("tipoNumeracion",request.getParameter("tipoNumeracion"));
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
	    gVO.setAtributo("codECO",request.getParameter("codECO"));
	    gVO.setAtributo("codESI",request.getParameter("codESI"));
	    gVO.setAtributo("codNUC",request.getParameter("codNUC"));      
      gVO.setAtributo("idVia",request.getParameter("idVia"));
	    gVO.setAtributo("codVia",request.getParameter("idVia"));
      gVO.setAtributo("codTramo",request.getParameter("codTramo"));
      gVO.setAtributo("idESI",request.getParameter("idESI"));
      gVO.setAtributo("idNucleo",request.getParameter("idNucleo"));
      Vector tramos = new Vector();
      tramos = tramerosManager.eliminarTramero(gVO,params);
      if(tramos.size() > 0) {
        gVO = (GeneralValueObject) tramos.firstElement();
      }
      String puedeEliminar = (String) gVO.getAtributo("puedeEliminar");
      if(puedeEliminar != null && !puedeEliminar.equals("")) {
      	opcion = "noPuedeEliminar";
      } else {     
        mantForm.setListaTrameros(tramos);
        opcion = "cargarTramos";
      }
    }else if("modificar".equals(opcion)){
      GeneralValueObject gVO = recogerParametros(request);
      gVO.setAtributo("idVia",request.getParameter("idVia"));
      gVO.setAtributo("idESI",request.getParameter("idESI"));
      gVO.setAtributo("idNucleo",request.getParameter("idNucleo"));
      gVO.setAtributo("tipoNumeracion",request.getParameter("tipoNumeracion"));
      Vector tramos = new Vector();
      tramos = tramerosManager.modificarTramero(gVO,params);
      mantForm.setListaTrameros(tramos);
      opcion = "cargarTramos";
    }else if("alta".equals(opcion)){
      GeneralValueObject gVO = recogerParametros(request);
      gVO.setAtributo("idVia",request.getParameter("idVia"));
      gVO.setAtributo("idESI",request.getParameter("idESI"));
      gVO.setAtributo("idNucleo",request.getParameter("idNucleo"));
      gVO.setAtributo("tipoNumeracion",request.getParameter("tipoNumeracion"));
      Vector tramos = new Vector();
      tramos = tramerosManager.altaTramero(gVO,params);
      mantForm.setListaTrameros(tramos);
      opcion = "cargarTramos";
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }else{
      opcion = mapping.getInput();
    }
    return (mapping.findForward(opcion));
  }

  private GeneralValueObject recogerParametros(HttpServletRequest request){
    String pais = request.getParameter("codPais");
    String provincia = request.getParameter("codProvincia");
    String municipio = request.getParameter("codMunicipio");
    //String manzana = request.getParameter("codManzana");
    String manzana = null;
    String subSeccion = request.getParameter("codSubSeccion");

    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codTipoNumeracion",request.getParameter("codTipoNumeracion"));
    gVO.setAtributo("codPais",pais);
    gVO.setAtributo("codProvincia",provincia);
    //String codESI = request.getParameter("codESI");
	//gVO.setAtributo("codESI",codESI);
	//gVO.setAtributo("codNUC",request.getParameter("codNUC"));
 	//if ( (codESI == null) || "".equals(codESI) ) {
 		String codESITramo = request.getParameter("codESITramo");
 		String codNUCTramo = request.getParameter("codNUCTramo");
		gVO.setAtributo("codESITramo",codESITramo);
		gVO.setAtributo("codNUCTramo",codNUCTramo);
 	//}
	  	
    gVO.setAtributo("codMunicipio",municipio);
    gVO.setAtributo("codVia",request.getParameter("idVia"));    
    gVO.setAtributo("codTramo",request.getParameter("codTramo"));
    if(manzana!=null){
      gVO.setAtributo("codPaisManzana",pais);
      gVO.setAtributo("codProvinciaManzana",provincia);
      gVO.setAtributo("codMunicipioManzana",municipio);
      gVO.setAtributo("codDistritoManzana",request.getParameter("codDistrito"));
      gVO.setAtributo("codSeccionManzana",request.getParameter("codSeccion"));
      gVO.setAtributo("codLetraManzana",request.getParameter("letraSeccion"));
    }else{
      gVO.setAtributo("codPaisManzana","");
      gVO.setAtributo("codProvinciaManzana","");
      gVO.setAtributo("codMunicipioManzana","");
      gVO.setAtributo("codDistritoManzana","");
      gVO.setAtributo("codSeccionManzana","");
      gVO.setAtributo("codLetraManzana","");
    }
    gVO.setAtributo("codManzana",manzana);
    gVO.setAtributo("codPaisSeccion",pais);
    gVO.setAtributo("codProvinciaSeccion",provincia);
    gVO.setAtributo("codMunicipioSeccion",municipio);
    gVO.setAtributo("codDistritoSeccion",request.getParameter("codDistrito"));
    gVO.setAtributo("codSeccion",request.getParameter("codSeccion"));
    String letraSeccion = request.getParameter("letraSeccion");
    if (letraSeccion != null){    
    	if ("".equals(letraSeccion)) gVO.setAtributo("letraSeccion"," ");
    	else gVO.setAtributo("letraSeccion",letraSeccion);
    }
    if(subSeccion!=null){
      gVO.setAtributo("codPaisSubSeccion",pais);
      gVO.setAtributo("codProvinciaSubSeccion",provincia);
      gVO.setAtributo("codMunicipioSubSeccion",municipio);
      gVO.setAtributo("codDistritoSubSeccion",request.getParameter("codDistrito"));
      gVO.setAtributo("codSeccionSubSeccion",request.getParameter("codSeccion"));
      gVO.setAtributo("codLetraSubSeccion",request.getParameter("letraSeccion"));
    }else{
      gVO.setAtributo("codPaisSubSeccion","");
      gVO.setAtributo("codProvinciaSubSeccion","");
      gVO.setAtributo("codMunicipioSubSeccion","");
      gVO.setAtributo("codDistritoSubSeccion","");
      gVO.setAtributo("codSeccionSubSeccion","");
      gVO.setAtributo("codLetraSubSeccion","");
    }
    gVO.setAtributo("codSubSeccion",request.getParameter("codSubSeccion"));
    gVO.setAtributo("codPostal",request.getParameter("descPostal"));
    gVO.setAtributo("numDesde",request.getParameter("txtNumDesde"));
    gVO.setAtributo("letraDesde",request.getParameter("txtLetraDesde"));
    gVO.setAtributo("numHasta",request.getParameter("txtNumHasta"));
    gVO.setAtributo("letraHasta",request.getParameter("txtLetraHasta"));
    return gVO;
  }
}