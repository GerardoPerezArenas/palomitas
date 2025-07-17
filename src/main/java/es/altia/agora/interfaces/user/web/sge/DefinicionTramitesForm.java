package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.integracionsw.InfoConfTramSWVO;
import es.altia.agora.business.sge.DepartamentoNotificacionSneVO;
import es.altia.agora.business.sge.DocumentoExpedienteVO;
import es.altia.common.service.config.*;
import es.altia.flexia.tramitacion.externa.plugin.TramitacionExternaBase;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import es.altia.util.notificaciones.TipoNotificacionValueObject;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.ArrayList;


/** Clase utilizada para capturar o	mostrar el estado	de un	RegistroEntrada */
public class DefinicionTramitesForm	extends ActionForm {
  //Queremos usar el fichero	de configuración technical
  protected static Config m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
  //Necesitamos	el servicio	de log
  protected static Log m_Log =
          LogFactory.getLog(DefinicionTramitesForm.class.getName());

  //Reutilizamos
  DefinicionTramitesValueObject defTramVO = new DefinicionTramitesValueObject();

  // Lista de unidades que pueden tramitar (de la jsp)
  String txtUnidadesTramitadoras = null;

  public DefinicionTramitesValueObject getDefinicionTramites() {
    return defTramVO;
  }

  public void setDefinicionTramites(DefinicionTramitesValueObject dTramVO) {
    this.defTramVO = dTramVO;
  }

  /* Seccion donde metemos los metods get y set de los campos del formulario */

  //%que queda para la finalizacion de un tramite
   public int getPlazoFin() {
    return defTramVO.getPlazoFin();
  }
  public void	setPlazoFin(int plazoFin) {
    defTramVO.setPlazoFin(plazoFin);
  }
  public String	getCodEnlace() {
    return defTramVO.getCodEnlace();
  }
  public void	setCodEnlace(String codEnlace) {
    defTramVO.setCodEnlace(codEnlace);
  }
  public String getTxtCodigo() {
    return defTramVO.getTxtCodigo();
  }
  public void	setTxtCodigo(String txtCodigo) {
    defTramVO.setTxtCodigo(txtCodigo);
  }
  public String getDescEnlace()	{
    return defTramVO.getDescEnlace();
  }
  public void	setDescEnlace(String descEnlace) {
    defTramVO.setDescEnlace(descEnlace);
  }
  public String getDisponible()	{
    return defTramVO.getDisponible();
  }
  public void	setDisponible(String disponible) {
    defTramVO.setDisponible(disponible);
  }
  public String getTramiteInicio() {
    return defTramVO.getTramiteInicio();
  }
  public void	setTramiteInicio(String	tramiteInicio) {
    defTramVO.setTramiteInicio(tramiteInicio);
  }
  public String getTramitePregunta() {
    return defTramVO.getTramitePregunta();
  }
  public void	setTramitePregunta(String tramitePregunta) {
    defTramVO.setTramitePregunta(tramitePregunta);
  }
  public String getEjecucion() {
    return defTramVO.getEjecucion();
  }
  public void	setEjecucion(String ejecucion) {
    defTramVO.setEjecucion(ejecucion);
  }
  public String getCodClasifTramite()	{
    return defTramVO.getCodClasifTramite();
  }
  public void setCodClasifTramite(String codClasifTramite) {
    defTramVO.setCodClasifTramite(codClasifTramite);
  }
    public String getCodExpRel()	{
      return defTramVO.getCodExpRel();
    }
    public void setCodExpRel(String codExpRel) {
      defTramVO.setCodExpRel(codExpRel);
    }
  public String	getDescClasifTramite() {
    return defTramVO.getDescClasifTramite();
  }
  public void setDescClasifTramite(String descClasifTramite) {
    defTramVO.setDescClasifTramite(descClasifTramite);
  }
    public String getDescExpRel() {
      return defTramVO.getDescExpRel();
    }
    public void setDescExpRel(String descExpRel) {
      defTramVO.setDescExpRel(descExpRel);
    }
  public String	getCodMunicipio()	{
    return defTramVO.getCodMunicipio();
  }
  public void setCodMunicipio(String codMunicipio) {
    defTramVO.setCodMunicipio(codMunicipio);
  }
  public String	getCodAplicacion() {
    return defTramVO.getCodAplicacion();
  }
  public void setCodAplicacion(String codAplicacion) {
    defTramVO.setCodAplicacion(codAplicacion);
  }
  public String	getCodTipoTramite() {
    return defTramVO.getCodTipoTramite();
  }
  public void setCodTipoTramite(String codTipoTramite) {
    defTramVO.setCodTipoTramite(codTipoTramite);
  }
  public String	getCodUnidadInicio() {
    return defTramVO.getCodUnidadInicio();
  }
  public void setCodUnidadInicio(String	codUnidadInicio) {
    defTramVO.setCodUnidadInicio(codUnidadInicio);
  }
  public String getCodVisibleUnidadInicio() {
    return defTramVO.getCodVisibleUnidadInicio();
  }
  public void setCodVisibleUnidadInicio(String codVisibleUnidadInicio) {
    defTramVO.setCodVisibleUnidadInicio(codVisibleUnidadInicio);
  }
  public String	getCodUnidadTramite() {
    return defTramVO.getCodUnidadTramite();
  }
  public void setCodUnidadTramite(String codUnidadTramite) {
    defTramVO.setCodUnidadTramite(codUnidadTramite);
  }
    public String	getCodCargo() {
      return defTramVO.getCodCargo();
    }
    public void setCodCargo(String codCargo) {
      defTramVO.setCodCargo(codCargo);
    }
    public String	getCodVisibleCargo() {
      return defTramVO.getCodVisibleCargo();
    }
    public void setCodVisibleCargo(String codVisibleCargo) {
      defTramVO.setCodVisibleCargo(codVisibleCargo);
    }
  public String	getDescTipoTramite() {
    return defTramVO.getDescTipoTramite();
  }
  public void setDescTipoTramite(String	descTipoTramite) {
    defTramVO.setDescTipoTramite(descTipoTramite);
  }
  public String	getDescUnidadInicio() {
    return defTramVO.getDescUnidadInicio();
  }
  public void setDescUnidadInicio(String descUnidadInicio) {
    defTramVO.setDescUnidadInicio(descUnidadInicio);
  }
  public String	getDescUnidadTramite() {
    return defTramVO.getDescUnidadTramite();
  }
  public void setDescUnidadTramite(String descUnidadTramite) {
    defTramVO.setDescUnidadTramite(descUnidadTramite);
  }
  public String	getCodAreaTra() {
    return defTramVO.getCodAreaTra();
  }
  public void	setCodAreaTra(String codArea)	{
    defTramVO.setCodAreaTra(codArea);
  }
  public String getDescAreaTra() {
    return defTramVO.getDescAreaTra();
  }
  public void	setDescAreaTra(String descArea) {
    defTramVO.setDescAreaTra(descArea);
  }
  public String	getOcurrencias() {
    return defTramVO.getOcurrencias();
  }
  public void setOcurrencias(String ocurrencias) {
    defTramVO.setOcurrencias(ocurrencias);
  }
  public String	getPlazo() {
    return defTramVO.getPlazo();
  }
  public void setPlazo(String plazo) {
    defTramVO.setPlazo(plazo);
  }
  public String getUnidadesPlazo() {
    return defTramVO.getUnidadesPlazo();
  }
  public void	setUnidadesPlazo(String	unidadesPlazo) {
    defTramVO.setUnidadesPlazo(unidadesPlazo);
  }

  public String getFinalizacionNF() {
    return defTramVO.getFinalizacionNF();
  }
  public void	setFinalizacionNF(String finalizacionNF) {
    defTramVO.setFinalizacionNF(finalizacionNF);
  }
  public String getFinalizacionSF() {
    return defTramVO.getFinalizacionSF();
  }
  public void	setFinalizacionSF(String finalizacionSF) {
    defTramVO.setFinalizacionSF(finalizacionSF);
  }
  public String getTxtDescripcion() {
    return defTramVO.getTxtDescripcion();
  }
  public void	setTxtDescripcion(String txtDescripcion) {
    defTramVO.setTxtDescripcion(txtDescripcion);
  }
  public String getNombreTramite() {
    return defTramVO.getNombreTramite();
  }
  public void	setNombreTramite(String	nombreTramite) {
    defTramVO.setNombreTramite(nombreTramite);
  }
  public String getTexto() {
    return defTramVO.getTexto();
  }
  public void	setTexto(String texto) {
    defTramVO.setTexto(texto);
  }
  public String getTipoFinalizacion()	{
    return defTramVO.getTipoFinalizacion();
  }
  public void	setTipoFinalizacion(String tipoFinalizacion) {
    defTramVO.setTipoFinalizacion(tipoFinalizacion);
  }
  public String getTipoPregunta() {
    return defTramVO.getTipoPregunta();
  }
  public void	setTipoPregunta(String tipoPregunta) {
    defTramVO.setTipoPregunta(tipoPregunta);
  }
  public String getTipoTramite() {
    return defTramVO.getTipoTramite();
  }
  public void	setTipoTramite(String tipoTramite) {
    defTramVO.setTipoTramite(tipoTramite);
  }
  public String getTipoTramiteNF() {
    return defTramVO.getTipoTramiteNF();
  }
  public void	setTipoTramiteNF(String	tipoTramiteNF) {
    defTramVO.setTipoTramiteNF(tipoTramiteNF);
  }
  public String getTipoTramiteSF() {
    return defTramVO.getTipoTramiteSF();
  }
  public void	setTipoTramiteSF(String	tipoTramiteSF) {
    defTramVO.setTipoTramiteSF(tipoTramiteSF);
  }
  public String getNumeroTramite() {
    return defTramVO.getNumeroTramite();
  }
  public void	setNumeroTramite(String	numeroTramite) {
    defTramVO.setNumeroTramite(numeroTramite);
  }
  public String getCodigoTramite() {
    return defTramVO.getCodigoTramite();
  }
  public void	setCodigoTramite(String	codigoTramite) {
    defTramVO.setCodigoTramite(codigoTramite);
  }
  public Vector getTramites() {
    return defTramVO.getTramites();
  }
  public void	setTramites(Vector v) {
    defTramVO.setTramites(v);
  }
  public String getTipoCondicion() {
    return defTramVO.getTipoCondicion();
  }

  public String getTipoFavorableNO() {
    return defTramVO.getTipoFavorableNO();
  }

  public String getTipoFavorableSI() {
    return defTramVO.getTipoFavorableSI();
  }

  public String getCodTramiteCondEntrada() {
    return defTramVO.getCodTramiteCondEntrada();
  }
  public void	setCodTramiteCondEntrada(String codTramiteCondEntrada) {
    defTramVO.setCodTramiteCondEntrada(codTramiteCondEntrada);
  }
  public String getIdTramiteCondEntrada() {
    return defTramVO.getIdTramiteCondEntrada();
  }
  public void	setIdTramiteCondEntrada(String idTramiteCondEntrada) {
    defTramVO.setIdTramiteCondEntrada(idTramiteCondEntrada);
  }
  public String getDescTramiteCondEntrada()	{
    return defTramVO.getDescTramiteCondEntrada();
  }
  public void	setDescTramiteCondEntrada(String descTramiteCondEntrada) {
    defTramVO.setDescTramiteCondEntrada(descTramiteCondEntrada);
  }
  public String getEstadoTramiteCondEntrada() {
    return defTramVO.getEstadoTramiteCondEntrada();
  }
  public void	setEstadoTramiteCondEntrada(String estadoTramiteCondEntrada) {
    defTramVO.setEstadoTramiteCondEntrada(estadoTramiteCondEntrada);
  }

  public String getTipoCondEntrada() {
    return defTramVO.getTipoCondEntrada();
  }
  public void	setTipoCondEntrada(String tipoCondEntrada) {
    defTramVO.setTipoCondEntrada(tipoCondEntrada);
  }

  public String getCodCondEntrada()	{
    return defTramVO.getCodCondEntrada();
  }
  public void setCodCondEntrada(String codCondEntrada) {
    defTramVO.setCodCondEntrada(codCondEntrada);
  }

  public String getExpresionCondEntrada()	{
    return defTramVO.getExpresionCondEntrada();
  }
  public void setExpresionCondEntrada(String expresionCondEntrada) {
    defTramVO.setExpresionCondEntrada(expresionCondEntrada);
  }
  public Vector getListasCondEntrada() {
    return defTramVO.getListasCondEntrada();
  }
  public String getFirma() {
    return defTramVO.getFirma();
  }
  public void	setFirma(String firma) {
    defTramVO.setFirma(firma);
  }
  public String getNombreDoc() {
    return defTramVO.getNombreDoc();
  }
  public void	setNombreDoc(String nombreDoc) {
    defTramVO.setNombreDoc(nombreDoc);
  }
  public String getPlantilla() {
    return defTramVO.getPlantilla();
  }
  public void	setPlantilla(String plantilla) {
    defTramVO.setPlantilla(plantilla);
  }
  public String getVisibleInternet() {
    return defTramVO.getVisibleInternet();
  }
  public void	setVisibleInternet(String visibleInternet) {
    defTramVO.setVisibleInternet(visibleInternet);
  }
  public String getCodigoDoc() {
    return defTramVO.getCodigoDoc();
  }
  public void	setCodigoDoc(String codigoDoc) {
    defTramVO.setCodigoDoc(codigoDoc);
  }
  public String getCodTipoDoc()	{
    return defTramVO.getCodTipoDoc();
  }
  public void	setCodTipoDoc(String codTipoDoc) {
    defTramVO.setCodTipoDoc(codTipoDoc);
  }
  public String getDescTipoDoc() {
    return defTramVO.getDescTipoDoc();
  }
  public String getImportar() {
    return defTramVO.getImportar();
  }
  public void	setImportar(String importar) {
    defTramVO.setImportar(importar);
  }
  public void	setDescTipoDoc(String descTipoDoc) {
    defTramVO.setDescTipoDoc(descTipoDoc);
  }
  public Vector getListaDocusErroneos() {
    return defTramVO.getListaDocusErroneos();
  }
  public Vector getListaDocumentos() {
    return defTramVO.getListaDocumentos();
  }
  public Vector getListaAreas()	{
    return defTramVO.getListaArea();
  }
  public String getInstrucciones() {
    return defTramVO.getInstrucciones();
  }
  public void	setInstrucciones(String instrucciones) {
    defTramVO.setInstrucciones(instrucciones);
  }


  public String getNumeroCondicionSalida() {
    return defTramVO.getNumeroCondicionSalida();
  }
  public void	setNumeroCondicionSalida(String numeroCondicionSalida) {
    defTramVO.setNumeroCondicionSalida(numeroCondicionSalida);
  }
  public String getIdTramiteFlujoSalida() {
    return defTramVO.getIdTramiteFlujoSalida();
  }
  public void	setIdTramiteFlujoSalida(String idTramiteFlujoSalida) {
    defTramVO.setIdTramiteFlujoSalida(idTramiteFlujoSalida);
  }
  public String getCodTramiteFlujoSalida() {
    return defTramVO.getCodTramiteFlujoSalida();
  }
  public void	setCodTramiteFlujoSalida(String codTramiteFlujoSalida) {
    defTramVO.setCodTramiteFlujoSalida(codTramiteFlujoSalida);
  }
  public String getNombreTramiteFlujoSalida() {
    return defTramVO.getNombreTramiteFlujoSalida();
  }
  public void	setNombreTramiteFlujoSalida(String nombreTramiteFlujoSalida) {
    defTramVO.setNombreTramiteFlujoSalida(nombreTramiteFlujoSalida);
  }
  public String getNumeroSecuencia() {
    return defTramVO.getNumeroSecuencia();
  }
  public void setNumeroSecuencia(String numeroSecuencia) {
    defTramVO.setNumeroSecuencia(numeroSecuencia);
  }

  public String getNoModificar() {
    return defTramVO.getNoModificar();
  }
  public void	setNoModificar(String noModificar) {
    defTramVO.setNoModificar(noModificar);
  }
  public String getDeCatalogo()	{
    return defTramVO.getDeCatalogo();
  }
  public void	setDeCatalogo(String deCatalogo) {
    defTramVO.setDeCatalogo(deCatalogo);
  }

  public Vector getListaTramitesDesfavorable() {
    return defTramVO.getListaTramitesDesfavorable();
  }
  public void	setListaTramitesDesfavorable(Vector	listaTramitesDesfavorable) {
    defTramVO.setListaTramitesDesfavorable(listaTramitesDesfavorable);
  }
  public Vector getListaTramitesFavorable()	{
    return defTramVO.getListaTramitesFavorable();
  }
  public void	setListaTramitesFavorable(Vector listaTramitesFavorable) {
    defTramVO.setListaTramitesFavorable(listaTramitesFavorable);
  }

  public String getDeCatalogoDeProcedimiento() {
    return defTramVO.getDeCatalogoDeProcedimiento();
  }
  public void	setDeCatalogoDeProcedimiento(String	deCatalogoDeProcedimiento) {
    defTramVO.setDeCatalogoDeProcedimiento(deCatalogoDeProcedimiento);
  }

  public Vector getListaEnlaces() {
    return defTramVO.getListaEnlaces();
  }

  // Lista de Unidades tramitadoras (todas)
  public Vector getListaUnidadesTramitadoras() {
    return defTramVO.getListaUnidadesTramitadoras();
  }

  // Lista de Unidades tramitadoras (las que pueden tramitar este tramite)
  public Vector getUnidadesTramitadoras() {
    return defTramVO.getUnidadesTramitadoras();
  }

  public String getTxtUnidadesTramitadoras() {
      return txtUnidadesTramitadoras;
  }

  public void setTxtUnidadesTramitadoras(String txtUnidadesTramitadoras) {
      this.txtUnidadesTramitadoras = txtUnidadesTramitadoras;
  }

  // Lista de	Clasificación tramites
  public Vector getListaClasifTramite() {
    return defTramVO.getListaClasifTramite();
  }

    // Lista de	Clasificación tramites
    public Vector getListaExpRel() {
      return defTramVO.getListaExpRel();
    }

  // Lista de	nombre de tramites
  public Vector getListaTramites() {
    return defTramVO.getListaTramites();
  }

  //Lista de tipos de documentos
  public Vector getListaTipoDocumentos() {
    return defTramVO.getListaTipoDocumentos();
  }

  //Listas de la pestaña de condicion de entrada
  public Vector getListaEstadosTabla() {
    return defTramVO.getListaEstadosTabla();
  }
  public Vector getListaTramitesTabla() {
    return defTramVO.getListaTramitesTabla();
  }
  public Vector getListaCodTramitesTabla() {
    return defTramVO.getListaCodTramitesTabla();
  }
  public Vector getListaTiposTabla() {
    return defTramVO.getListaCodTramitesTabla();
  }

  // Lista de	Clasificación tramites
  public Vector getListaPlantillas() {
    return defTramVO.getListaPlantillas();
  }

  public String getPosicion() {
    return defTramVO.getPosicion();
  }

  public String getTramiteEliminado()	{
    return defTramVO.getTramiteEliminado();
  }

  public Vector getListaCampos() {
    return defTramVO.getListaCampos();
  }
  
    public Vector getListaAgrupaciones(){
        return defTramVO.getListaAgrupaciones();
    }

// Notificaciones
  public String getNotUnidadTramitIni()	{
    return defTramVO.getNotUnidadTramitIni();
  }
  public void setNotUnidadTramitIni(String disponible) {
    defTramVO.setNotUnidadTramitIni(disponible);
  }
  public String getNotUnidadTramitFin()	{
    return defTramVO.getNotUnidadTramitFin();
  }
  public void setNotUnidadTramitFin(String disponible) {
    defTramVO.setNotUnidadTramitFin(disponible);
  }
  public String getNotUsuUnidadTramitIni()	{
    return defTramVO.getNotUsuUnidadTramitIni();
  }
  public void setNotUsuUnidadTramitIni(String disponible) {
    defTramVO.setNotUsuUnidadTramitIni(disponible);
  }
  public String getNotUsuUnidadTramitFin()	{
    return defTramVO.getNotUsuUnidadTramitFin();
  }
  public void setNotUsuUnidadTramitFin(String disponible) {
    defTramVO.setNotUsuUnidadTramitFin(disponible);
  }
  public String getNotInteresadosIni()	{
    return defTramVO.getNotInteresadosIni();
  }
  public void setNotInteresadosIni(String disponible) {
    defTramVO.setNotInteresadosIni(disponible);
  }
  public String getNotInteresadosFin()	{
    return defTramVO.getNotInteresadosFin();
  }
  public void setNotInteresadosFin(String disponible) {
    defTramVO.setNotInteresadosFin(disponible);
  }
  public String getNotUsuInicioTramiteIni(){
      return defTramVO.getNotUsuInicioTramiteIni();
  }
  public void setNotUsuInicioTramiteIni(String disponible){
      defTramVO.setNotUsuInicioTramiteIni(disponible);
  }
  public String getNotUsuInicioTramiteFin(){
      return defTramVO.getNotUsuInicioTramiteFin();
  }
  public void setNotUsuInicioTramiteFin(String disponible){
      defTramVO.setNotUsuInicioTramiteFin(disponible);
  }
  public String getNotUsuInicioExpedIni(){
      return defTramVO.getNotUsuInicioExpedIni();
  }
  public void setNotUsuInicioExpedIni(String disponible){
      defTramVO.setNotUsuInicioExpedIni(disponible);
  }
  public String getNotUsuInicioExpedFin(){
      return defTramVO.getNotUsuInicioExpedFin();
  }
  public void setNotUsuInicioExpedFin(String disponible){
      defTramVO.setNotUsuInicioExpedFin(disponible);
  }
  
    public String getNotUsuTraFinPlazo(){	
      return defTramVO.getNotUsuTraFinPlazo();	
  }	
  public void setNotUsuTraFinPlazo(String disponible){	
      defTramVO.setNotUsuTraFinPlazo(disponible);	
  }	
  public String getNotUsuExpFinPlazo(){	
      return defTramVO.getNotUsuExpFinPlazo();	
  }	
  public void setNotUsuExpFinPlazo(String disponible){	
      defTramVO.setNotUsuExpFinPlazo(disponible);	
  }	
  public String getNotUORFinPlazo(){	
      return defTramVO.getNotUORFinPlazo();	
  }	
  public void setNotUORFinPlazo(String disponible){	
      defTramVO.setNotUORFinPlazo(disponible);	
  }

  public String getTramiteActual(){return defTramVO.getTramiteActual();}
  public void setTramiteActual(String num) {defTramVO.setTramiteActual(num);}

  public String getNumeroTramites(){return defTramVO.getNumeroTramites();}
  public void setNumeroTramites(String num) {defTramVO.setNumeroTramites(num);}

  // Lista de Web Services
  public Vector getListaWebServices() {
    return defTramVO.getListaWebServices();
  }
  public void setListaWebServices(Vector listaWebServices) {
   defTramVO.setListaWebServices(listaWebServices);
  }

    public InfoConfTramSWVO getInfoSWAvanzar() {
        return defTramVO.getInfoSWAvanzar();
    }

    public void setInfoSWAvanzar(InfoConfTramSWVO codOpSWAvz) {
        defTramVO.setInfoSWAvanzar(codOpSWAvz);
    }

    public InfoConfTramSWVO getInfoSWRetroceder() {
        return defTramVO.getInfoSWRetroceder();
    }

    public void setInfoSWRetroceder(InfoConfTramSWVO codOpSWRtr) {
        defTramVO.setInfoSWRetroceder(codOpSWRtr);
    }

    private InfoConfTramSWVO infoSWActual;
    private boolean avanzarActual;

    public InfoConfTramSWVO getInfoSWActual() {
        return infoSWActual;
    }

    public void setInfoSWActual(InfoConfTramSWVO infoSWActual) {
        this.infoSWActual = infoSWActual;
    }

    public boolean isAvanzarActual() {
        return avanzarActual;
    }

    public void setAvanzarActual(boolean avanzarActual) {
        this.avanzarActual = avanzarActual;
    }

    public void setListaCamposTramitesCondEntrada(Vector listaCamposTramitesCondEntrada) {
    defTramVO.setListaCamposTramitesCondEntrada(listaCamposTramitesCondEntrada);
  }

  public Vector getListaCamposTramitesCondEntrada() {
    return defTramVO.getListaCamposTramitesCondEntrada();  
  }

    public void setListaCargos(Vector listaCargos) {
      defTramVO.setListaCargos(listaCargos);
    }

    public Vector getListaCargos() {
      return defTramVO.getListaCargos();
    }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
    m_Log.debug("validate");
    ActionErrors errors = new ActionErrors();
    //RegistroSaidaValueObject hara el trabajo para nostros ...
    try	{
      defTramVO.validate(idioma);
    } catch (ValidationException ve) {
      //Hay errores...
      //Tenemos que	traducirlos	a formato struts
      errors=validationException(ve,errors);
    }
    return errors;
  }

  /* Función que procesa los errores de	validación a formato struts */
  private ActionErrors validationException(ValidationException ve,ActionErrors errors){
    Iterator iter = ve.getMessages().get();
    while	(iter.hasNext()) {
      Message message	= (Message)iter.next();
      errors.add(message.getProperty(),	new ActionError(message.getMessageKey()));
    }
    return errors;
  }

  public Vector getListaConfSW() {
	return defTramVO.getListaConfSW();
  }

  public void setListaConfSW(Vector listaConfSW) {
	defTramVO.setListaConfSW(listaConfSW);
  }
  
    public boolean isGenerarPlazos() {
        return defTramVO.isGenerarPlazos();
    }

    public void setGenerarPlazos(boolean generarPlazos) {
        defTramVO.setGenerarPlazos(generarPlazos);
    }
 
    public boolean getNotificarCercaFinPlazo() {
      return defTramVO.getNotificarCercaFinPlazo();
    }
    public void setNotificarCercaFinPlazo(boolean notificarCercaFinPlazo) {
        defTramVO.setNotificarCercaFinPlazo(notificarCercaFinPlazo);
    }

    public boolean getNotificarFueraDePlazo() {
        return defTramVO.getNotificarFueraDePlazo();
    }

    public void setNotificarFueraDePlazo(boolean notificarFueraDePlazo) {
        defTramVO.setNotificarFueraDePlazo(notificarFueraDePlazo);
    }

     public int getTipoNotCercaFinPlazo() {
        return defTramVO.getTipoNotCercaFinPlazo();
    }

    public void setTipoNotCercaFinPlazo(int tipoNotCercaFinPlazo) {
       defTramVO.setTipoNotCercaFinPlazo(tipoNotCercaFinPlazo);
    }

     public int getTipoNotFueraDePlazo() {
        return defTramVO.getTipoNotFueraDePlazo();
    }

    public void setTipoNotFueraDePlazo(int tipoNotFueraDePlazo) {
       defTramVO.setTipoNotFueraDePlazo(tipoNotFueraDePlazo);
    }
    
     public String getAdmiteNotificacionElectronica() {
        return defTramVO.getAdmiteNotificacionElectronica();
    }

    public void setAdmiteNotificacionElectronica(String tipoNotFueraDePlazo) {
       defTramVO.setAdmiteNotificacionElectronica(tipoNotFueraDePlazo);
    }

    public String getCodigoTipoNotificacionElectronica()
    {
        return	defTramVO.getCodigoTipoNotificacionElectronica();
    }
    public void setCodigoTipoNotificacionElectronica(String tipoNotificacion)
    {
        defTramVO.setCodigoTipoNotificacionElectronica(tipoNotificacion);
    }
    public String getCodigoOtroUsuarioFirma()
    {
        return	defTramVO.getCodigoOtroUsuarioFirma();
    }
    public void setCodigoOtroUsuarioFirma(String codOtroUsuarioFirma)
    {
        defTramVO.setCodigoOtroUsuarioFirma(codOtroUsuarioFirma);
    }
    public String getTipoUsuarioFirma()
    {
        return defTramVO.getTipoUsuarioFirma();
    }
    public void setTipoUsuarioFirma(String tipoUsuFirma)
    {
        defTramVO.setTipoUsuarioFirma(tipoUsuFirma);
    }

    public String getNombreOtroUsuarioFirma() {
        return defTramVO.getNombreOtroUsuarioFirma();
    }

    public void setNombreOtroUsuarioFirma(String nombreOtroUsuarioFirma) {
        defTramVO.setNombreOtroUsuarioFirma(nombreOtroUsuarioFirma);
    }

    public ArrayList<TipoNotificacionValueObject> getListaTiposNotificacionElectronica()
    {
        return listaTiposNotificacionElectronica;
    }
    public void setListaTiposNotificacionElectronica(ArrayList<TipoNotificacionValueObject> tipoNotif)
    {
        this.listaTiposNotificacionElectronica=tipoNotif;
    }

    public ArrayList<DocumentoExpedienteVO> getListaDocsPresentados() {
        return listaDocsPresentados;
    }

    public void setListaDocsPresentados(ArrayList<DocumentoExpedienteVO> listaDocsPresentados) {
        this.listaDocsPresentados = listaDocsPresentados;
    }

    public ArrayList<TramitacionExternaBase> getPluginPantallasTramitacionExterna() {
        return this.defTramVO.getPluginPantallasTramitacionExterna();
    }

    public void setPluginPantallasTramitacionExterna(ArrayList<TramitacionExternaBase> pluginPantallasTramitacionExterna) {
        this.defTramVO.setPluginPantallasTramitacionExterna(pluginPantallasTramitacionExterna);
    }


    public String getCodPluginPantallaTramitacionExterna() {
        return this.defTramVO.getCodPluginPantallaTramitacionExterna();
    }


    /**
     * @param codPluginPantallaTramitacionExterna the codPluginPantallaTramitacionExterna to set
     */
    public void setCodPluginPantallaTramitacionExterna(String codPluginPantallaTramitacionExterna) {
        this.defTramVO.setCodPluginPantallaTramitacionExterna(codPluginPantallaTramitacionExterna);
    }

    public String getUrlPluginPantallaTramitacionExterna(){
        return this.defTramVO.getUrlPluginPantallaTramitacionExterna();
    }

    public void setUrlPluginPantallaTramitacionExterna(String url){
        this.defTramVO.setUrlPluginPantallaTramitacionExterna(url);
    }


    public String getImplClassPluginPantallaTramitacionExterna(){
        return this.defTramVO.getImplClassPluginPantallaTramitacionExterna();
    }

    public void setImplClassPluginPantallaTramitacionExterna(String implClass){
        this.defTramVO.setImplClassPluginPantallaTramitacionExterna(implClass);
    }

    

    

    private String admiteNotificacionElectronica;
    private String codigoTipoNotificacionElectronica;
    private String tipoUsuarioFirma;
    private String codigoOtroUsuarioFirma;
    private String nombreOtroUsuarioFirma;
    private ArrayList<TipoNotificacionValueObject> listaTiposNotificacionElectronica ;
    private ArrayList<DocumentoExpedienteVO> listaDocsPresentados ;
    private String notificacionElectronicaObligatoria;
    private String notificacionElectronicaObligatoriaValue;
    private String certificadoOrganismoFirmaNotificacion;
    private String certificadoOrganismoFirmaNotificacionValue;

    private ArrayList<DepartamentoNotificacionSneVO> listaDepartamentosNotificacion;
    
    private String mostrarTramiteNotificado;
    
    public String getNotificacionElectronicaObligatoria() {
        return notificacionElectronicaObligatoria;
    }
    public String getNotificacionElectronicaObligatoriaValue() {
        return notificacionElectronicaObligatoriaValue;
    }

    public void setNotificacionElectronicaObligatoria(String notifElectronicaObligatoria) {
        notificacionElectronicaObligatoria =notifElectronicaObligatoria;
    }
    
    public void setNotificacionElectronicaObligatoriaValue(String notifElectronicaObligatoria) {
        notificacionElectronicaObligatoriaValue =notifElectronicaObligatoria;
    }
     
    public String getCertificadoOrganismoFirmaNotificacion() {
        return certificadoOrganismoFirmaNotificacion;
    }
    
     public String getCertificadoOrganismoFirmaNotificacionValue() {
        return certificadoOrganismoFirmaNotificacionValue;
    }

    public void setCertificadoOrganismoFirmaNotificacion(String certOrganismoFirmaNotificacion) {
       certificadoOrganismoFirmaNotificacion =certOrganismoFirmaNotificacion;
    }
    
    public void setCertificadoOrganismoFirmaNotificacionValue(String certOrganismoFirmaNotificacion) {
       certificadoOrganismoFirmaNotificacionValue =certOrganismoFirmaNotificacion;
    }
    

 //fin notificacion de tramite automatica
    public void reset(ActionMapping mapping, HttpServletRequest request) {
       setNotificarFueraDePlazo(false);
        setNotificarCercaFinPlazo(false);
         setGenerarPlazos(false);
         setTramiteNotificado(false);
    }

    /**
     * @return the listaDepartamentosNotificacion
     */
    public ArrayList<DepartamentoNotificacionSneVO> getListaDepartamentosNotificacion() {
        return listaDepartamentosNotificacion;
    }

    /**
     * @param listaDepartamentosNotificacion the listaDepartamentosNotificacion to set
     */
    public void setListaDepartamentosNotificacion(ArrayList<DepartamentoNotificacionSneVO> listaDepartamentosNotificacion) {
        this.listaDepartamentosNotificacion = listaDepartamentosNotificacion;
    }



    public String getCodDepartamentoNotificacion(){
        return this.defTramVO.getCodDepartamentoNotificacion();
    }

    public void setCodDepartamentoNotificacion(String codigo){
        this.defTramVO.setCodDepartamentoNotificacion(codigo);
    }
    
    public String getCodigoInternoTramite() {
        return this.defTramVO.getCodigoInternoTramite();
    }
    public void setCodigoInternoTramite(String codigoInternoTramite) {
        this.defTramVO.setCodigoInternoTramite(codigoInternoTramite);
    }
    
    public boolean isTramiteNotificado() {
        return defTramVO.isTramiteNotificado();
    }

    public void setTramiteNotificado(boolean tramiteNotificado) {
        this.defTramVO.setTramiteNotificado(tramiteNotificado);
    }

    /**
     * @return the mostrarTramiteNotificado
     */
    public String getMostrarTramiteNotificado() {
        return mostrarTramiteNotificado;
    }

    /**
     * @param mostrarTramiteNotificado the mostrarTramiteNotificado to set
     */
    public void setMostrarTramiteNotificado(String mostrarTramiteNotificado) {
        this.mostrarTramiteNotificado = mostrarTramiteNotificado;
    }
    
}