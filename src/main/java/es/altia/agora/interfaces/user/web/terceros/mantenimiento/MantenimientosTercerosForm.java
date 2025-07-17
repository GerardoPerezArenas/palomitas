// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.terceros.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.agora.business.terceros.*;

import java.util.Vector;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class MantenimientosTercerosForm extends ActionForm  {
  Vector listaTipoDocs = new Vector();
  Vector listaPaises = new Vector();
  Vector listaAutonomias = new Vector();
  Vector listaProvincias = new Vector();
  Vector listaMunicipios = new Vector();
  Vector listaUsoViviendas = new Vector();
  Vector listaTipoVias = new Vector();
  Vector listaVias = new Vector();
  Vector listaCodPostales = new Vector();
  Vector listaTrameros = new Vector();
  Vector listaNumeraciones = new Vector();
  Vector listaDistritos = new Vector();
  Vector listaSecciones = new Vector();
  Vector listaSubSecciones = new Vector();
  Vector listaManzanas = new Vector();
  Vector listaEcos = new Vector();
  Vector listaEsisCompleta = new Vector();
  Vector listaEsis = new Vector();
  Vector listaNucleos = new Vector();
  Vector listaOrganismosExternos = new Vector();
  Vector listaEscaleras = new Vector();
  Vector listaPlantas = new Vector();
  Vector listaDomicilios = new Vector();
  Vector listaPartJudiciales = new Vector();
  Vector listaViasBuscadas = new Vector();
    private int paginaListadoE;
    private int numLineasPaginaListadoE;
  ParametrosTerceroValueObject parametrosTerceros = new ParametrosTerceroValueObject();
  String opcion = "";
  String operacion = "";
  String ventana = "false";
  private String viaDarAlta = "";


    public int getPaginaListadoE() {
        return paginaListadoE;
    }

    public void setPaginaListadoE(int paginaListadoE) {
        this.paginaListadoE = paginaListadoE;
    }

    public int getNumLineasPaginaListadoE() {
        return numLineasPaginaListadoE;
    }

    public void setNumLineasPaginaListadoE(int numLineasPaginaListadoE) {
        this.numLineasPaginaListadoE = numLineasPaginaListadoE;
    }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    return super.validate(mapping, request);
  }

  public Vector getListaCodPostales() {
    return listaCodPostales;
  }

  public void setListaCodPostales(Vector newListaCodPostales) {
    listaCodPostales = newListaCodPostales;
  }

  public Vector getListaPaises() {
    return listaPaises;
  }

  public void setListaPaises(Vector newListaPaises) {
    listaPaises = newListaPaises;
  }

  public Vector getListaProvincias() {
    return listaProvincias;
  }

  public void setListaProvincias(Vector newListaProvincias) {
    listaProvincias = newListaProvincias;
  }

  public Vector getListaTipoDocs() {
    return listaTipoDocs;
  }

  public void setListaTipoDocs(Vector newListaTipoDocs) {
    listaTipoDocs = newListaTipoDocs;
  }

  public Vector getListaTipoVias() {
    return listaTipoVias;
  }

  public void setListaTipoVias(Vector newListaTipoVias) {
    listaTipoVias = newListaTipoVias;
  }

  public Vector getListaUsoViviendas() {
    return listaUsoViviendas;
  }

  public void setListaUsoViviendas(Vector newListaUsoViviendas) {
    listaUsoViviendas = newListaUsoViviendas;
  }

  public Vector getListaVias() {
    return listaVias;
  }

  public void setListaVias(Vector newListaVias) {
    listaVias = newListaVias;
  }

  public Vector getListaMunicipios() {
    return listaMunicipios;
  }

  public void setListaMunicipios(Vector newListaMunicipios) {
    listaMunicipios = newListaMunicipios;
  }

  public Vector getListaAutonomias() {
    return listaAutonomias;
  }

  public void setListaAutonomias(Vector newListaAutonomias) {
    listaAutonomias = newListaAutonomias;
  }

  public Vector getListaTrameros() {
    return listaTrameros;
  }

  public void setListaTrameros(Vector newListaTrameros) {
    listaTrameros = newListaTrameros;
  }

  public Vector getListaNumeraciones() {
    return listaNumeraciones;
  }

  public void setListaNumeraciones(Vector newListaNumeraciones) {
    listaNumeraciones = newListaNumeraciones;
  }

  public Vector getListaDistritos() {
    return listaDistritos;
  }

  public void setListaDistritos(Vector newListaDistritos) {
    listaDistritos = newListaDistritos;
  }

  public Vector getListaSecciones() {
    return listaSecciones;
  }

  public void setListaSecciones(Vector newListaSecciones) {
    listaSecciones = newListaSecciones;
  }

  public Vector getListaSubSecciones() {
    return listaSubSecciones;
  }

  public void setListaSubSecciones(Vector newListaSubSecciones) {
    listaSubSecciones = newListaSubSecciones;
  }

  public Vector getListaManzanas() {
    return listaManzanas;
  }

  public void setListaManzanas(Vector newListaManzanas) {
    listaManzanas = newListaManzanas;
  }

  public String getOperacion() {
    return operacion;
  }

  public void setOperacion(String newOperacion) {
    if(newOperacion!=null)
      operacion = newOperacion;
    else
      operacion = "";
  }

  public String getOpcion() {
    return opcion;
  }

  public void setOpcion(String newOpcion) {
    if(newOpcion!=null)
      opcion = newOpcion;
    else
      opcion = "";
  }

  public String getVentana() {
    return ventana;
  }

  public void setVentana(String newVentana) {
    if(newVentana!=null)
      ventana = newVentana;
    else
      ventana = "false";
  }

  public Vector getListaEcos() {
    return listaEcos;
  }

  public void setListaEcos(Vector newListaEcos) {
    listaEcos = newListaEcos;
  }
  
  public Vector getListaEsisCompleta() {
    return listaEsisCompleta;
  }

  public void setListaEsisCompleta(Vector newListaEsisCompleta) {
    listaEsisCompleta = newListaEsisCompleta;
  }

  public Vector getListaEsis() {
    return listaEsis;
  }

  public void setListaEsis(Vector newListaEsis) {
    listaEsis = newListaEsis;
  }

  public Vector getListaNucleos() {
    return listaNucleos;
  }

  public void setListaNucleos(Vector newListaNucleos) {
    listaNucleos = newListaNucleos;
  }

  public Vector getListaOrganismosExternos() {
    return listaOrganismosExternos;
  }

  public void setListaOrganismosExternos(Vector newListaOrganismosExternos) {
    listaOrganismosExternos = newListaOrganismosExternos;
  }

  public Vector getListaEscaleras() {
    return listaEscaleras;
  }

  public void setListaEscaleras(Vector newListaEscaleras) {
    listaEscaleras = newListaEscaleras;
  }

  public Vector getListaPlantas() {
    return listaPlantas;
  }

  public void setListaPlantas(Vector newListaPlantas) {
    listaPlantas = newListaPlantas;
  }

  public ParametrosTerceroValueObject getParametrosTerceros(){
    return parametrosTerceros;
  }

  public void setParametrosTerceros(ParametrosTerceroValueObject ptVO){
    parametrosTerceros = ptVO;
  }

  public Vector getListaDomicilios() {
    return listaDomicilios;
  }

  public void setListaDomicilios(Vector newListaDomicilios) {
    listaDomicilios = newListaDomicilios;
  }
  
  public Vector getListaPartJudiciales() {
    return listaPartJudiciales;
  }

  public void setListaPartJudiciales(Vector newListaPartJudiciales) {
    listaPartJudiciales = newListaPartJudiciales;
  }

  	/* Viales 06/06/05 */
  	public Vector getListaViasBuscadas() {
	  return listaViasBuscadas;
	}
	
	public void setListaViasBuscadas(Vector newListaVias) {
	  listaViasBuscadas = newListaVias;
	}

    /**
     * @return the viaDarAlta
     */
    public String getViaDarAlta() {
        return viaDarAlta;
    }

    /**
     * @param viaDarAlta the viaDarAlta to set
     */
    public void setViaDarAlta(String viaDarAlta) {
        this.viaDarAlta = viaDarAlta;
    }
	/* Fin viales 06/06/05 */
	
}