// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.terceros;

// PAQUETES IMPORTADOS
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.EstructuraCampo;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import es.altia.common.service.config.*;
import es.altia.flexia.terceros.integracion.externa.vo.ErrorSistemaTerceroExternoVO;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.ValidationException;
import es.altia.technical.Message;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class BusquedaTercerosForm extends org.apache.struts.action.ActionForm {

  //Queremos usar el fichero de configuración technical
  protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
  //Necesitamos el servicio de log
  protected static Log m_Log =
          LogFactory.getLog(BusquedaTercerosForm.class.getName());
  //Reutilizamos
  Vector listaTerceros=new Vector();
  Vector listaTercerosModificados = new Vector();
  Vector listaTipoDocs=new Vector();
  Vector listaPaises=new Vector();
  Vector listaProvincias=new Vector();
  Vector listaMunicipios=new Vector();
  Vector listaUsoViviendas=new Vector();
  Vector listaTipoVias=new Vector();
  Vector listaVias=new Vector();
  Vector listaCodPostales = new Vector();
  Vector listaInteresados = new Vector();
  Vector listaDomicilios = new Vector();
  String ventana="false";
  int lineasPagina = 10;
  int pagina = 1;
  
  /* anadir ECO/ESI */
  Vector listaECOs = new Vector();	
  Vector listaESIs = new Vector();
  String resOp="";
  /* fin anadir ECO/ESI */

  private String permitirAltaTerceroSinDomicilio;

  private Hashtable<Integer,ArrayList<ErrorSistemaTerceroExternoVO>> erroresSistemaExterno = null;
  private String pantallaDatosSuplementariosTerceroActivada = null;

  private Vector<EstructuraCampo> estructuraCamposSuplementariosTercero = null;
  private Vector valoresCamposSuplementariosTercero = null;

  private GeneralValueObject listaFicheros = new GeneralValueObject();
  private GeneralValueObject listaTiposFicheros = new GeneralValueObject();
  private GeneralValueObject listaNombreFicheros = new GeneralValueObject();
    
  public Vector getListaInteresados() { return listaInteresados; }
  public void setListaInteresados(Vector listaInteresados) { 
	  this.listaInteresados = listaInteresados; 
	  }
  public void setListaTerceros(Vector lista){
    listaTerceros = lista;
  }

  public Vector getListaTerceros(){
    return listaTerceros;
  }

  public void setListaTipoDocs(Vector lista){
    listaTipoDocs = lista;
  }

  public Vector getListaPaises(){
    return listaPaises;
  }

  public void setListaPaises(Vector lista){
    listaPaises = lista;
  }

  public Vector getListaProvincias(){
    return listaProvincias;
  }

  public void setListaProvincias(Vector lista){
    listaProvincias = lista;
  }

   public Vector getListaMunicipios() {
    return listaMunicipios;
  }

  public void setListaMunicipios(Vector newListaMunicipios) {
    listaMunicipios = newListaMunicipios;
  }

  public Vector getListaTipoDocs(){
    return listaTipoDocs;
  }

  public Vector getListaUsoViviendas(){
    return listaUsoViviendas;
  }

  public void setListaUsoViviendas(Vector lista){
    listaUsoViviendas = lista;
  }

  public Vector getListaTipoVias(){
      return listaTipoVias;
    }

  public void setListaTipoVias(Vector lista){
      listaTipoVias = lista;
  }

  public Vector getListaVias(){
        return listaVias;
      }

    public void setListaVias(Vector lista){
        listaVias = lista;
  }

  public void setListaCodPostales(Vector lista){
    listaCodPostales = lista;
  }

  public Vector getListaCodPostales(){
    return listaCodPostales;
  }

  public void setListaDomicilios(Vector lista){
    listaDomicilios = lista;
  }

  public Vector getListaDomicilios(){
    return listaDomicilios;
  }

  public void setVentana(String v){
    ventana = v;
  }

  public String getVentana(){
    return ventana;
  }

  public void setLineasPagina(int v){
    lineasPagina = v;
  }

  public int getLineasPagina(){
    return lineasPagina;
  }

  public void setPagina(int v){
    pagina = v;
  }

  public int getPagina(){
    return pagina;
  }

  public Vector getListaTercerosModificados() {
    return listaTercerosModificados;
  }

  public void setListaTercerosModificados(Vector listaTercerosModificados) {
        this.listaTercerosModificados = listaTercerosModificados;
  }
  
  /* anadir ECO/ESI */
  public Vector getListaECOs(){ return listaECOs; }
  public void setListaECOs(Vector eco){ listaECOs=eco; }
  
  public Vector getListaESIs(){ return listaESIs; }
  public void setListaESIs(Vector esi){ listaESIs=esi; }

  public String getResOp(){ return resOp; }
  public void setResOp(String resultado){ resOp=resultado; }
  
  /* fin anadir ECO/ESI */


  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
    m_Log.debug("validate");
    ActionErrors errors = new ActionErrors();
    //RegistroEntradaValueObject hara el trabajo para nostros ...
    /*try {
      laDiligVO.validate(idioma);
    } catch (ValidationException ve) {
      //Hay errores...
      //Tenemos que traducirlos a formato struts
      errors=validationException(ve,errors);
    }*/
    return errors;
  }

    /* Función que procesa los errores de validación a formato struts */
  private ActionErrors validationException(ValidationException ve,ActionErrors errors){
    Iterator iter = ve.getMessages().get();
    while (iter.hasNext()) {
      Message message = (Message)iter.next();
      errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
    }
    return errors;
  }

    /**
     * @return the permitirAltaTerceroSinDomicilio
     */
    public String getPermitirAltaTerceroSinDomicilio() {
        return permitirAltaTerceroSinDomicilio;
    }

    /**
     * @param permitirAltaTerceroSinDomicilio the permitirAltaTerceroSinDomicilio to set
     */
    public void setPermitirAltaTerceroSinDomicilio(String permitirAltaTerceroSinDomicilio) {
        this.permitirAltaTerceroSinDomicilio = permitirAltaTerceroSinDomicilio;
    }

    /**
     * @return the erroresSistemaExterno
     */
    public Hashtable<Integer, ArrayList<ErrorSistemaTerceroExternoVO>> getErroresSistemaExterno() {
        return erroresSistemaExterno;
    }

    /**
     * @param erroresSistemaExterno the erroresSistemaExterno to set
     */
    public void setErroresSistemaExterno(Hashtable<Integer, ArrayList<ErrorSistemaTerceroExternoVO>> erroresSistemaExterno) {
        this.erroresSistemaExterno = erroresSistemaExterno;
    }

    /**
     * @return the pantallaDatosSuplementariosTerceroActivada
     */
    public String getPantallaDatosSuplementariosTerceroActivada() {
        return pantallaDatosSuplementariosTerceroActivada;
    }

    /**
     * @param pantallaDatosSuplementariosTerceroActivada the pantallaDatosSuplementariosTerceroActivada to set
     */
    public void setPantallaDatosSuplementariosTerceroActivada(String pantallaDatosSuplementariosTerceroActivada) {
        this.pantallaDatosSuplementariosTerceroActivada = pantallaDatosSuplementariosTerceroActivada;
    }

    /**
     * @return the valoresCamposSuplementariosTercero
     */
    public Vector getValoresCamposSuplementariosTercero() {
        return valoresCamposSuplementariosTercero;
    }

    /**
     * @param valoresCamposSuplementariosTercero the valoresCamposSuplementariosTercero to set
     */
    public void setValoresCamposSuplementariosTercero(Vector valoresCamposSuplementariosTercero) {
        this.valoresCamposSuplementariosTercero = valoresCamposSuplementariosTercero;
    }

    /**
     * @return the estructuraCamposSuplementariosTercero
     */
    public Vector<EstructuraCampo> getEstructuraCamposSuplementariosTercero() {
        return estructuraCamposSuplementariosTercero;
    }

    /**
     * @param estructuraCamposSuplementariosTercero the estructuraCamposSuplementariosTercero to set
     */
    public void setEstructuraCamposSuplementariosTercero(Vector<EstructuraCampo> estructuraCamposSuplementariosTercero) {
        this.estructuraCamposSuplementariosTercero = estructuraCamposSuplementariosTercero;
    }


    public GeneralValueObject getListaFicheros() {
        return listaFicheros;
    }

    public void setListaFicheros(GeneralValueObject listaFicheros) {
        this.listaFicheros = listaFicheros;
    }

    public GeneralValueObject getListaTiposFicheros() {
        return listaTiposFicheros;
    }

    public void setListaTiposFicheros(GeneralValueObject listaTiposFicheros) {
        this.listaTiposFicheros = listaTiposFicheros;
    }

    /**
     * @return the listaNombreFicheros
     */
    public GeneralValueObject getListaNombreFicheros() {
        return listaNombreFicheros;
    }

    /**
     * @param listaNombreFicheros the listaNombreFicheros to set
     */
    public void setListaNombreFicheros(GeneralValueObject listaNombreFicheros) {
        this.listaNombreFicheros = listaNombreFicheros;
    }
   
}