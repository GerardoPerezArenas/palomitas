// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

// PAQUETES IMPORTADOS
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.arboles.impl.ArbolImpl;
import org.apache.struts.upload.FormFile;

public class MantenimientosAdminForm extends ActionForm  {

	private static final long serialVersionUID = 1L;
	
    Vector listaOrganizaciones = new Vector();
    Vector listaCss = new Vector();
    Vector listadosParametrizables=new Vector();
    Vector camposListados=new Vector();
    int codCampo;
    String nomCampo;
    String codlistado;
    Vector listaEntidades = new Vector();
    Vector listaDepartamentos = new Vector();
    Vector listaTablasAtributos = new Vector();
    Vector listaAplicaciones = new Vector();
    Vector listaEtiquetas = new Vector();
    Vector listaNucleos = new Vector();
    Vector listaAreas = new Vector();
    Vector listaTramites = new Vector();
    Vector listaProcedimientos = new Vector();
    Vector listaIdiomas = new Vector();
    Vector listaUsuarios = new Vector();
    Vector listaUsoViviendas = new Vector();
    Vector listaTipoVias = new Vector();
    Vector listaVias = new Vector();
    Vector listaCodPostales = new Vector();
    Vector listaTrameros = new Vector();
    Vector listaNumeraciones = new Vector();
    Vector listaDistritos = new Vector();
    Vector listaSecciones = new Vector();
    Vector listaSubSecciones = new Vector();
    Vector listaTextosFijos = new Vector();
    Vector listaProcesos = new Vector();
    Vector listaMenus = new Vector();
    Vector listaCamposDesplegables = new Vector();
    Vector listaValoresCamposDesplegables = new Vector();
    // TODO quitar el Vector viejo
    Vector listaUORs = new Vector();
    // =====================
    Vector listaNuevasUORs = new Vector();
    ArbolImpl arbol;
    String codigoIdioma = null;
    String nombreIdioma = null;
    
     private FormFile fichero;
     private String tituloFichero;

    public Vector getListaNuevasUORs() {
        return listaNuevasUORs;
    }

    public void setListaNuevasUORs(Vector listaNuevasUORs) {
        this.listaNuevasUORs = listaNuevasUORs;
    }

    public ArbolImpl getArbol() {
        return arbol;
    }

    public void setArbol(ArbolImpl arbol) {
        this.arbol = arbol;
    }

    // ====================
    Vector listaUTRs = new Vector();
    GeneralValueObject otrosDatos = new GeneralValueObject();
    String operacion = "";
    String ventana = "false";

    
    public FormFile getFichero() {
        return fichero;

    }

    public void setFichero(FormFile fichero) {
        this.fichero = fichero;
    }

     public String getTituloFichero() {
        return tituloFichero;
    }

    public void setTituloFichero(String titulo) {
        this.tituloFichero = titulo;
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

    public Vector getListaEntidades() {
        return listaEntidades;
    }

    public void setListaEntidades(Vector newListaEntidades) {
        listaEntidades = newListaEntidades;
    }

    public Vector getListaTablasAtributos() {
        return listaTablasAtributos;
    }

    public void setListaTablasAtributos(Vector newListaTablasAtributos) {
        listaTablasAtributos = newListaTablasAtributos;
    }

    public Vector getListaOrganizaciones() {
        return listaOrganizaciones;
    }

    public void setListaOrganizaciones(Vector newListaOrganizaciones) {
        listaOrganizaciones = newListaOrganizaciones;
    }

        public Vector getListaCss() {
        return listaCss;
    }

    public void setListaCss(Vector newListaCss) {
        listaCss = newListaCss;
    }

    /* Listados parametrizados*/
     public Vector getListadosParametrizables() {
    return listadosParametrizables;
    }

    public void setListadosParametrizables(Vector newLista) {
        listadosParametrizables = newLista;
    }
    
     public Vector getCamposListados() {
        return camposListados;
    }

    public void setCamposListados(Vector newLista) {
        camposListados = newLista;
    }
    
   public int getCodCampo() {
        return this.codCampo;
    }

    public void setCodCampo(int codCampo) {
        this.codCampo = codCampo;
    }
     public String getNomCampo() {
        return this.nomCampo;
    }

    public void setNomCampo(String nomCampo) {
        this.nomCampo = nomCampo;
    }
    
    /*Fin de listados parametrizados*/

    public Vector getListaUORs() {
        return listaUORs;
    }

    public void setListaUORs(Vector newLista) {
        listaUORs = newLista;
    }

    public Vector getListaUTRs() {
        return listaUTRs;
    }

    public void setListaUTRs(Vector newLista) {
        listaUTRs = newLista;
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

    public Vector getListaIdiomas() {
        return listaIdiomas;
    }

    public void setListaIdiomas(Vector newListaIdiomas) {
        listaIdiomas = newListaIdiomas;
    }

    public Vector getListaUsuarios() {
        return listaUsuarios;
    }
    
    public void setListaUsuarios(Vector newListaUsuarios) {
    	listaUsuarios = newListaUsuarios;
    }

    public Vector getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(Vector newListaDepartamentos) {
        listaDepartamentos = newListaDepartamentos;
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

    public Vector getListaTextosFijos() {
        return listaTextosFijos;
    }

    public void setListaTextosFijos(Vector newListaTextosFijos) {
        listaTextosFijos = newListaTextosFijos;
    }

    public Vector getListaAreas() {
        return listaAreas;
    }

    public void setListaAreas(Vector newListaAreas) {
        listaAreas = newListaAreas;
    }

    public Vector getListaTramites() {
        return listaTramites;
    }

    public void setListaTramites(Vector newListaTramites) {
        listaTramites = newListaTramites;
    }

    public Vector getListaProcedimientos() {
        return listaProcedimientos;
    }

    public void setListaProcedimientos(Vector newListaProcedimientos) {
        listaProcedimientos = newListaProcedimientos;
    }

    public Vector getListaProcesos() {
        return listaProcesos;
    }

    public void setListaProcesos(Vector newListaProcesos) {
        listaProcesos = newListaProcesos;
    }

    public Vector getListaMenus() {
        return listaMenus;
    }

    public void setListaMenus(Vector newListaMenus) {
        listaMenus = newListaMenus;
    }

    public GeneralValueObject getOtrosDatos() {
        return otrosDatos;
    }

    public void setOtrosDatos(GeneralValueObject newOtrosDatos) {
        otrosDatos = newOtrosDatos;
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

    public String getVentana() {
        return ventana;
    }

    public void setVentana(String newVentana) {
        if(newVentana!=null)
            ventana = newVentana;
        else
            ventana = "false";
    }

    public Vector getListaAplicaciones() {
        return listaAplicaciones;
    }

    public void setListaAplicaciones(Vector newListaAplicaciones) {
        listaAplicaciones = newListaAplicaciones;
    }

    public Vector getListaEtiquetas() {
        return listaEtiquetas;
    }

    public void setListaEtiquetas(Vector newListaEtiquetas) {
        listaEtiquetas = newListaEtiquetas;
    }

    public Vector getListaNucleos() {
        return listaNucleos;
    }

    public void setListaNucleos(Vector newListaNucleos) {
        listaNucleos = newListaNucleos;
    }

    public Vector getListaCamposDesplegables() {
        return listaCamposDesplegables;
    }

    public void setListaCamposDesplegables(Vector listaCamposDesplegables) {
        this.listaCamposDesplegables = listaCamposDesplegables;
    }

    public Vector getListaValoresCamposDesplegables() {
        return listaValoresCamposDesplegables;
    }

    public void setListaValoresCamposDesplegables(Vector listaValoresCamposDesplegables) {
        this.listaValoresCamposDesplegables = listaValoresCamposDesplegables;
    }

	public String getCodigoIdioma() {
		return codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

	public String getNombreIdioma() {
		return nombreIdioma;
	}

	public void setNombreIdioma(String nombreIdioma) {
		this.nombreIdioma = nombreIdioma;
	}

    public String getCodlistado() {
        return codlistado;
    }

    public void setCodlistado(String codlistado) {
        this.codlistado = codlistado;
    }
}