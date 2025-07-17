// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.terceros;

// PAQUETES IMPORTADOS
import java.util.*;
import es.altia.agora.business.util.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class FusionDivisionForm extends org.apache.struts.action.ActionForm {
  private String opcion = "";
  private String operacion = "";
  private String inicializar = "";
  private Vector listaProcesos = new Vector();
  private Vector listaUsuarios = new Vector();
  private Vector listaDistritos = new Vector();
  private Vector listaSecciones = new Vector();
  private Vector listaVias = new Vector();
  private Vector listaTipoNumeraciones = new Vector();
  private GeneralValueObject datosVO = new GeneralValueObject();
  private int lineasPagina = 10;
  private int pagina = 1;

  public FusionDivisionForm() {
  }

  public String getOpcion() {
    return opcion;
  }

  public void setOpcion(String newOpcion) {
    opcion = newOpcion;
  }

  public String getOperacion() {
    return operacion;
  }

  public void setOperacion(String newOperacion) {
    operacion = newOperacion;
  }

	public String getInicializar() {
    return inicializar;
  }

  public void setInicializar(String newInicializar) {
    inicializar = newInicializar;
  }

  public Vector getListaProcesos() {
    return listaProcesos;
  }

  public void setListaProcesos(Vector newListaProcesos) {
    listaProcesos = newListaProcesos;
  }

  public Vector getListaUsuarios() {
    return listaUsuarios;
  }

  public void setListaUsuarios(Vector newListaUsuarios) {
    listaUsuarios = newListaUsuarios;
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

  public void setListaSecciones(Vector newListaSecciones)
  {
    listaSecciones = newListaSecciones;
  }

  public Vector getListaVias() {
    return listaVias;
  }

  public void setListaVias(Vector newListaVias) {
    listaVias = newListaVias;
  }

  public Vector getListaTipoNumeraciones() {
    return listaTipoNumeraciones;
  }

  public void setListaTipoNumeraciones(Vector newListaTipoNumeraciones) {
    listaTipoNumeraciones = newListaTipoNumeraciones;
  }

  public GeneralValueObject getDatosVO() {
    return datosVO;
  }

  public void setDatosVO(GeneralValueObject newDatosVO) {
    datosVO = newDatosVO;
  }

  public int getLineasPagina() {
    return lineasPagina;
  }

  public void setLineasPagina(int newLineasPagina) {
    lineasPagina = newLineasPagina;
  }

  public int getPagina() {
    return pagina;
  }

  public void setPagina(int newPagina) {
    pagina = newPagina;
  }


}