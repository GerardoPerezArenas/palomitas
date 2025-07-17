// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.terceros.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.terceros.*;
import es.altia.agora.business.terceros.mantenimiento.persistence.*;
import es.altia.agora.business.terceros.mantenimiento.CondicionesBusquedaViaVO;
import es.altia.agora.business.terceros.mantenimiento.ViaEncontradaVO;
import es.altia.agora.business.util.*;
import es.altia.agora.interfaces.user.web.util.*;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.*;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

  
/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase VialesAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */


public class VialesAction extends ActionSession {
  protected static Log m_Log =
          LogFactory.getLog(VialesAction.class.getName());
  protected static Config m_ConfigTerceros = 
          ConfigServiceHelper.getConfig("Vias");
    
  String codPais = "108"; //Pais por defecto: Espana
    String codProvincia = "99"; //Provincia por defecto: desconocida
    String codMunicipio = "999"; //Municipio por defecto: desconocido

  public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    m_Log.debug("====================== VialesAction =======================>");
    HttpSession session = request.getSession();

    // Validaremos los parametros del request especificados
    String opcion = request.getParameter("opcion");
    if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

    // Rellenamos el form de Padron
    if (form == null) {
      m_Log.debug("Rellenamos el form de Padron");
      form = new MantenimientosTercerosForm();
      if ("request".equals(mapping.getScope())){
        request.setAttribute(mapping.getAttribute(), form);
      }else{
        session.setAttribute(mapping.getAttribute(), form);
      }
    }
    UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
    ParametrosTerceroValueObject paramsTercero = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
    if(paramsTercero != null){
        codPais = paramsTercero.getPais();
        codProvincia = paramsTercero.getProvincia();
        codMunicipio = paramsTercero.getMunicipio();
    }
    String[] params = usuario.getParamsCon();
    MantenimientosTercerosForm mantForm = (MantenimientosTercerosForm) form;
    ProvinciasManager provinciasManager = ProvinciasManager.getInstance();
    MunicipiosManager municipiosManager = MunicipiosManager.getInstance();
    TiposViasManager tipoViasManager = TiposViasManager.getInstance();
    EcosManager ecosManager = EcosManager.getInstance();
    EntidadesSingularesManager esisManager = EntidadesSingularesManager.getInstance();
    NucleosManager nucleosManager = NucleosManager.getInstance();
    ViasManager viasManager = ViasManager.getInstance();
    mantForm.setOpcion(opcion);
    //mantForm.setOperacion(request.getParameter("operacion"));
    if(opcion.equals("inicializar")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",codPais);
      gVO.setAtributo("codProvincia",codProvincia);
      gVO.setAtributo("codMunicipio",codMunicipio);
      Vector lista = provinciasManager.getListaProvincias(gVO,params);
      Vector listaM = municipiosManager.getListaMunicipios(gVO,params);
      Vector listaTVias = tipoViasManager.getListaTiposVias(params);
      mantForm.setListaTipoVias(listaTVias);
      mantForm.setListaProvincias(lista);
      mantForm.setListaMunicipios(listaM);
      mantForm.setOperacion(viasManager.existeAplicacion(params,"2")); // MIRO SI EXISTE PADRON
    }else if (opcion.equals("cargarMunicipios")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",request.getParameter("codPais"));
      gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
      Vector muns = new Vector();
      muns = municipiosManager.getListaMunicipios(gVO,params);
      mantForm.setListaMunicipios(muns);
    }if(opcion.equals("inicializarModViales")){
      Vector listaTVias = new Vector();
      Vector listaECOs = new Vector();
      Vector listaESIs = new Vector();
      Vector listaNUCs = new Vector();
      Vector listaVias = new Vector();
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
      listaTVias = tipoViasManager.getListaTiposVias(params);
      listaECOs = ecosManager.getListaEcos(gVO,params);
      listaESIs = esisManager.getListaEntidadesSingulares(gVO,params);
      listaNUCs = nucleosManager.getListaNucleos(gVO,params);
	  // Cambiando combo viales
	  // listaVias = viasManager.getListaVias(params,gVO);
      listaVias = viasManager.getListaViasSolas(params,gVO);
	  // Fin cambiando combo viales      
      mantForm.setListaTipoVias(listaTVias);
      mantForm.setListaEcos(listaECOs);
      mantForm.setListaEsis(listaESIs);
      mantForm.setListaNucleos(listaNUCs);
      mantForm.setListaVias(listaVias);
      mantForm.setOperacion(request.getParameter("operacion"));
    }else if (opcion.equals("cargarNumeraciones")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO = recogerParametros(request);
      Vector numer = new Vector();      
      numer = viasManager.getListaNumeraciones(params,gVO);      
      mantForm.setListaNumeraciones(numer);      
      opcion="ocultoModViales";
    }else if(opcion.equals("cargarListas")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",codPais);
      gVO.setAtributo("codProvincia",codProvincia);
      gVO.setAtributo("codMunicipio",codMunicipio);
      gVO.setAtributo("codECO",request.getParameter("codECO"));
      gVO.setAtributo("codEntidadColectiva",request.getParameter("codECO"));
      gVO.setAtributo("codESI",request.getParameter("codESI"));
      gVO.setAtributo("codNUC",request.getParameter("codNUC"));
      gVO.setAtributo("numDesde","");
      gVO.setAtributo("tipoNumeracion","");
      Vector esis = new Vector();
      Vector nucleos = new Vector();
      Vector vias = new Vector();
      esis = esisManager.getListaEntidadesSingulares(gVO,params);
      nucleos = nucleosManager.getListaNucleos(gVO,params);
	  // Cambiando combo viales
	  // vias = viasManager.getListaVias(params,gVO);
	  vias = viasManager.getListaViasSolas(params,gVO);
	  // Fin cambiando combo viales      
      mantForm.setListaEsis(esis);
      mantForm.setListaNucleos(nucleos);
      mantForm.setListaVias(vias);
      opcion="ocultoModViales";
    }else if(opcion.equals("cargarVias")){
      GeneralValueObject gVO = new GeneralValueObject();
      gVO.setAtributo("codPais",codPais);
      gVO.setAtributo("codProvincia",codProvincia);
      gVO.setAtributo("codMunicipio",codMunicipio);
      gVO.setAtributo("codECO",request.getParameter("codECO"));
      gVO.setAtributo("codESI",request.getParameter("codESI"));
      gVO.setAtributo("codNUC",request.getParameter("codNUC"));
      gVO.setAtributo("numDesde","");
      gVO.setAtributo("tipoNumeracion","");
      Vector vias = new Vector();
	  // Cambiando combo viales
	  // vias = viasManager.getListaVias(params,gVO);
	  vias = viasManager.getListaViasSolas(params,gVO);
	  // Fin cambiando combo viales      
      mantForm.setListaVias(vias);
      opcion="ocultoModViales";
    }else if(opcion.equals("cargarVias1")){
      GeneralValueObject gVO = new GeneralValueObject();

      String codPaisAux = request.getParameter("codPais");
      String codProvinciaAux = request.getParameter("codProvincia");
      String codMunicipioAux = request.getParameter("codMunicipio");
      if ((codPaisAux == null) || (codProvinciaAux == null) || (codMunicipioAux == null) ) {
      gVO.setAtributo("codPais",codPais);
      gVO.setAtributo("codProvincia",codProvincia);
      gVO.setAtributo("codMunicipio",codMunicipio);
      } else {
          gVO.setAtributo("codPais",codPaisAux);
          gVO.setAtributo("codProvincia",codProvinciaAux);
          gVO.setAtributo("codMunicipio",codMunicipioAux);
      }

      Vector vias = new Vector();
      vias = viasManager.getListaVias1(params,gVO);
      mantForm.setListaVias(vias);
      opcion="oculto";
    }else if (opcion.equals("modificarViaTerritorio")){
    	//Comentado para corrección de mantenimiento de viales. 08-05-2007
    	/*
    	 int mostrarFamilia = Integer.parseInt((String)paramsTercero.getFamiliasPadron());
    	 */
      GeneralValueObject gVO = new GeneralValueObject();
      gVO = recogerParametros(request);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      String operacion = "";
      //Comentado para corrección de mantenimiento de viales. 08-05-2007
      /*
        if((mostrarFamilia==0)||(mostrarFamilia==2)) operacion = "NO_EXISTE_HOJA";
        if(mostrarFamilia==1) operacion = "VARIAS_FAMILIAS";
        gVO.setAtributo("familias",paramsTercero.getFamiliasPadron());
       */
      gVO.setAtributo("operacion",operacion);
      mantForm.setOperacion(viasManager.modificarViaTerritorio(params,gVO));
      opcion="ocultoModViales";
    }else if("eliminar".equalsIgnoreCase(opcion)){
      GeneralValueObject gVO = recogerParametrosVias(request);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      Vector vias = new Vector();
      vias = viasManager.eliminarVia(gVO,params);
      mantForm.setListaVias(vias);
      opcion = "oculto";
    }else if("modificar".equals(opcion)){
      GeneralValueObject gVO = recogerParametrosVias(request);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      Vector vias = new Vector();
      vias = viasManager.modificarVia(gVO,params);
      mantForm.setListaVias(vias);
      opcion = "oculto";
    }else if("alta".equals(opcion)){
      GeneralValueObject gVO = recogerParametrosVias(request);
      gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
      Vector vias = new Vector();
      vias = viasManager.altaVia(gVO,params);
      mantForm.setListaVias(vias);
      opcion="oculto";
	
	/* Viales 06/06/05 */
    } else if("buscarVias".equals(opcion)){

      CondicionesBusquedaViaVO condiciones = new CondicionesBusquedaViaVO();
      condiciones.setCodPais(Integer.parseInt(codPais));
      String reqCodProvincia = request.getParameter("codProvincia");
      String reqCodMunicipio = request.getParameter("codMunicipio");

      String descripcionVia = request.getParameter("descVia");
      m_Log.debug("VialesAction buscarvias: " + descripcionVia);
      
      // Si no hay provincia o municipio, usamos valores por defecto
      if (reqCodProvincia.equals("") || reqCodMunicipio.equals("")) {
          reqCodProvincia = codProvincia;
          reqCodMunicipio = codMunicipio;
      }
      
      condiciones.setCodProvincia(Integer.parseInt(reqCodProvincia));
      condiciones.setCodMunicipio(Integer.parseInt(reqCodMunicipio));
      
      // Si se ha especificado busqueda abierta se trata la cadena de busqueda.
      String busquedaAbierta = m_ConfigTerceros.getString("BusquedaAbierta");
      if (busquedaAbierta == null) busquedaAbierta = "no";
      if (busquedaAbierta.equals("si")) {
          condiciones.setNombreVia(tratarBusquedaAbierta(request.getParameter("descVia")));
      } else {
          condiciones.setNombreVia(request.getParameter("descVia"));
      }
      condiciones.setCodOrganizacion(usuario.getOrgCod());

      // Llamar al manager.
      HashMap<String, Collection> mapaResultados = viasManager.buscarVias(condiciones, params);
      /* Asignamos el ValueObject al formulario*/
      mantForm.setListaViasBuscadas(new Vector<ViaEncontradaVO>(mapaResultados.get("resultados")));
      request.setAttribute("errores", mapaResultados.get("errores"));

      // Se guarda la descripción para darla de alta
      request.setAttribute("descripcionViaAlta",descripcionVia);
	//04 10 2005			
    }  else if(opcion.equals("iniciarAltaDirecta")){ // Despues de haber buscado y no encontrado.
        m_Log.debug("****** VialesAction iniciarAltaDirecta");        
        m_Log.debug("VialesAction viaDarDeAlta: " + mantForm.getViaDarAlta());
        // Se guarda el valor de la descripción de la via para rellenar las
        // cajas de texto correspondiente
        request.setAttribute("descripcionViaAlta",mantForm.getViaDarAlta());

		String codProvinciaVia=  request.getParameter("codProvincia");
		String codMunicipioVia=  request.getParameter("codMunicipio");
		GeneralValueObject gVO = new GeneralValueObject();
		gVO.setAtributo("codPais",codPais);
		gVO.setAtributo("codProvincia",codProvinciaVia);
		gVO.setAtributo("codMunicipio",codMunicipioVia);        
		Vector listaTVias = tipoViasManager.getListaTiposVias(params);
		mantForm.setListaTipoVias(listaTVias); 	

	}else if("altaDirecta".equals(opcion)){
		String codProvinciaVia=  request.getParameter("codProvincia");
		String codMunicipioVia=  request.getParameter("codMunicipio");
		String nombreVia = request.getParameter("descVia");    				
		GeneralValueObject gVO = new GeneralValueObject();
		gVO.setAtributo("codPais",codPais);
		gVO.setAtributo("codProvincia",codProvinciaVia);
		gVO.setAtributo("codMunicipio",codMunicipioVia);  		
		gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
		gVO.setAtributo("descVia",request.getParameter("descVia"));
		gVO.setAtributo("nombreCorto",request.getParameter("nombreCorto"));
		gVO.setAtributo("codTipoVia",request.getParameter("codTipoVia"));
		Vector listaViasBuscadas = new Vector();		
		if (viasManager.insertVia(gVO,params)){		
			mantForm.setOperacion("altaRealizada");
			gVO.setAtributo("nombreVia",nombreVia);
			listaViasBuscadas = viasManager.getListaViasBusquedaGeneral(params,gVO);									
		} else mantForm.setOperacion("altaNoRealizada");
		mantForm.setListaViasBuscadas(listaViasBuscadas);
	/* Fin viales 06/06/05 */     
      /**
       * Alta de una via obtenida de una base que no es agora (Padron)
       */

    }else if("altaExt".equals(opcion)){

        GeneralValueObject gVO = new GeneralValueObject();
		gVO.setAtributo("codPais",codPais);
		gVO.setAtributo("codProvincia",request.getParameter("codProvincia").toUpperCase());
		gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio").toUpperCase());
		gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()).toUpperCase());
		gVO.setAtributo("descVia",request.getParameter("descVia").toUpperCase());
		gVO.setAtributo("nombreCorto",request.getParameter("nombreCorto").toUpperCase());
		gVO.setAtributo("codTipoVia",request.getParameter("codTipoVia").toUpperCase());

		viasManager.altaViaNoRepetido(gVO,params);
      
    }else if("salir".equals(opcion)){
      if ((session.getAttribute(mapping.getAttribute()) != null))
        session.removeAttribute(mapping.getAttribute());
    }
    /* Redirigimos al JSP de salida*/
    m_Log.debug("<====================== VialesAction =======================");
    return (mapping.findForward(opcion));
  }

  public GeneralValueObject recogerParametros(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codPais",codPais);
    gVO.setAtributo("codProvincia",codProvincia);
    gVO.setAtributo("codMunicipio",codMunicipio);
    gVO.setAtributo("codECO",request.getParameter("codECO"));
    gVO.setAtributo("codESI",request.getParameter("codESI"));
    gVO.setAtributo("codNUC",request.getParameter("codNUC"));
    gVO.setAtributo("idVia",request.getParameter("idVia"));
    gVO.setAtributo("codVia",request.getParameter("codViaNuevo"));
    gVO.setAtributo("descVia",request.getParameter("descViaNuevo"));
    gVO.setAtributo("codTipoVia",request.getParameter("codTVia"));
    gVO.setAtributo("generarOperaciones",request.getParameter("generarOperaciones"));
    gVO.setAtributo("haCambiadoVial",request.getParameter("haCambiadoVial"));
    gVO.setAtributo("haCambiadoNumeracion",request.getParameter("haCambiadoNumeracion"));
    gVO.setAtributo("nuevaNumeracion",transformaArray(request.getParameter("nuevaNumeracion")));
    gVO.setAtributo("fechaOperacion",request.getParameter("fechaOperacion"));
    return gVO;
  }

  public GeneralValueObject recogerParametrosVias(HttpServletRequest request){
    GeneralValueObject gVO = new GeneralValueObject();
    gVO.setAtributo("codPais",request.getParameter("codPais"));
    gVO.setAtributo("codProvincia",request.getParameter("codProvincia"));
    gVO.setAtributo("codMunicipio",request.getParameter("codMunicipio"));
    gVO.setAtributo("idVia",request.getParameter("idVia"));
    gVO.setAtributo("codVia",request.getParameter("codVia"));
    gVO.setAtributo("descVia",request.getParameter("descVia"));
    gVO.setAtributo("nombreCorto",request.getParameter("nombreCorto"));
    gVO.setAtributo("codTipoVia",request.getParameter("codTipoVia"));
    return gVO;
  }

  private Vector transformaArray(String arrayNum){
  Vector resultado = new Vector();
  StringTokenizer numeraciones = new StringTokenizer(arrayNum,"§");
  while(numeraciones.hasMoreTokens()){
    String numToken = numeraciones.nextToken();
    StringTokenizer numeracion = new StringTokenizer(numToken,",");
    GeneralValueObject gVO = new GeneralValueObject();
    while(numeracion.hasMoreTokens()){
      String codDSU = numeracion.nextToken();
      codDSU = codDSU.substring(0,codDSU.length()-1);
      String numDesde = numeracion.nextToken();
      numDesde = numDesde.substring(0,numDesde.length()-1);
      String letraDesde = numeracion.nextToken();
      letraDesde = letraDesde.substring(0,letraDesde.length()-1);
      String numHasta = numeracion.nextToken();
      numHasta = numHasta.substring(0,numHasta.length()-1);
      String letraHasta = numeracion.nextToken();
      letraHasta = letraHasta.substring(0,letraHasta.length()-1);
      String haCambiadoNumeracion = numeracion.nextToken();
      haCambiadoNumeracion = haCambiadoNumeracion.substring(0,haCambiadoNumeracion.length()-1);
      String tipoNumeracionTRM = numeracion.nextToken();
      tipoNumeracionTRM = tipoNumeracionTRM.substring(0,tipoNumeracionTRM.length()-1);
      gVO.setAtributo("codPais",codPais);
      gVO.setAtributo("codProvincia",codProvincia);
      gVO.setAtributo("codMunicipio",codMunicipio);
      gVO.setAtributo("codDSU",codDSU);
      gVO.setAtributo("numDesde",numDesde);
      gVO.setAtributo("letraDesde",letraDesde);
      gVO.setAtributo("numHasta",numHasta);
      gVO.setAtributo("letraHasta",letraHasta);
      gVO.setAtributo("haCambiadoNumeracion",haCambiadoNumeracion);
      gVO.setAtributo("codTipoNumeracion",tipoNumeracionTRM);
    }
    resultado.add(gVO);
  }
  return resultado;
}
  
    /**
     * Para realizar busqueda abierta, rodea el string con "*" si no contiene
     * ninguno de los comodines "*", "&" o "|".
     * @param str String a tratar
     * @return String rodeado de "*", si es el caso
     */
    private String tratarBusquedaAbierta(String str){
        if (str != null && !str.equals("")) {
            if (!str.contains("*") && !str.contains("&") && !str.contains("|")) {
                str = "*" + str + "*";   
            }
        }            
        return str;    
    }

}