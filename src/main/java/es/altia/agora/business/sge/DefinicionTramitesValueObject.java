package es.altia.agora.business.sge;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.technical.ValueObject;
import es.altia.technical.Messages;
import es.altia.technical.ValidationException;
import es.altia.agora.business.integracionsw.InfoConfTramSWVO;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;

import es.altia.flexia.tramitacion.externa.plugin.TramitacionExternaBase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DefinicionTramitesValueObject implements	Serializable, ValueObject {

  /** Construye	un nuevo registroEntradaSalida por defecto. */
  public DefinicionTramitesValueObject() {
    super();
  }

   public int getPlazoFin() {
    return plazoFin;
  }
  public void	setPlazoFin(int plazoFin) {
    this.plazoFin = plazoFin;
  }
  public String	getCodEnlace() {
    return codEnlace;
  }
  public void	setCodEnlace(String codEnlace) {
    this.codEnlace = codEnlace;
  }
  public String getTxtCodigo() {
    return txtCodigo;
  }
  public void	setTxtCodigo(String txtCodigo) {
    this.txtCodigo = txtCodigo;
  }
  public String getCodAreaTra()	{
    return codAreaTra;
  }
  public void	setCodAreaTra(String codAreaTra) {
    this.codAreaTra	= codAreaTra;
  }
  public String getDescAreaTra() {
    return descAreaTra;
  }
  public void	setDescAreaTra(String descAreaTra) {
    this.descAreaTra = descAreaTra;
  }
  public String getDescEnlace()	{
    return descEnlace;
  }
  public void	setDescEnlace(String descEnlace) {
    this.descEnlace	= descEnlace;
  }
  public String getDisponible()	{
    return disponible;
  }
  public void	setDisponible(String disponible) {
    this.disponible	= disponible;
  }
  public String getTramiteInicio() {
    return tramiteInicio;
  }
  public void	setTramiteInicio(String	tramiteInicio) {
    this.tramiteInicio = tramiteInicio;
  }
  public String getEjecucion() {
    return ejecucion;
  }
  public void	setEjecucion(String ejecucion) {
    this.ejecucion = ejecucion;
  }
  public String getFinalizacionNF() {
    return finalizacionNF;
  }
  public void	setFinalizacionNF(String finalizacionNF) {
    this.finalizacionNF =	finalizacionNF;
  }
  public String getFinalizacionSF() {
    return finalizacionSF;
  }
  public void	setFinalizacionSF(String finalizacionSF) {
    this.finalizacionSF =	finalizacionSF;
  }
  public String getTxtDescripcion() {
    return txtDescripcion;
  }
  public void	setTxtDescripcion(String txtDescripcion) {
    this.txtDescripcion =	txtDescripcion;
  }
  public String getNombreTramite() {
    return nombreTramite;
  }
  public void	setNombreTramite(String	nombreTramite) {
    this.nombreTramite = nombreTramite;
  }
  public String getTexto() {
    return texto;
  }
  public void	setTexto(String texto) {
    this.texto = texto;
  }
  public String getTipoFinalizacion()	{
    return tipoFinalizacion;
  }
  public void	setTipoFinalizacion(String tipoFinalizacion) {
    this.tipoFinalizacion	= tipoFinalizacion;
  }
  public String getTipoPregunta() {
    return tipoPregunta;
  }
  public void	setTipoPregunta(String tipoPregunta) {
    this.tipoPregunta = tipoPregunta;
  }
  public String getTipoTramite() {
    return tipoTramite;
  }
  public void	setTipoTramite(String tipoTramite) {
    this.tipoTramite = tipoTramite;
  }
  public String getTipoTramiteNF() {
    return tipoTramiteNF;
  }
  public void	setTipoTramiteNF(String	tipoTramiteNF) {
    this.tipoTramiteNF = tipoTramiteNF;
  }
  public String getTipoTramiteSF() {
    return tipoTramiteSF;
  }
  public void	setTipoTramiteSF(String	tipoTramiteSF) {
    this.tipoTramiteSF = tipoTramiteSF;
  }
  public String getNumeroTramite() {
    return numeroTramite;
  }
  public void	setNumeroTramite(String	numeroTramite) {
    this.numeroTramite = numeroTramite;
  }
  public String getCodigoTramite() {
    return codigoTramite;
  }
  public void	setCodigoTramite(String	codigoTramite) {
    this.codigoTramite = codigoTramite;
  }
  public String getCodClasifTramite()	{
    return codClasifTramite;
  }
  public void setCodClasifTramite(String codClasifTramite) {
    this.codClasifTramite =	codClasifTramite;
  }
  public String	getDescClasifTramite() {
    return descClasifTramite;
  }
  public void setDescClasifTramite(String descClasifTramite) {
    this.descClasifTramite = descClasifTramite;
  }
  public String	getCodMunicipio()	{
    return codMunicipio;
  }
  public void setCodMunicipio(String codMunicipio) {
    this.codMunicipio	= codMunicipio;
  }
  public String	getCodAplicacion() {
    return codAplicacion;
  }
  public void setCodAplicacion(String codAplicacion) {
    this.codAplicacion = codAplicacion;
  }
  public String	getCodTipoTramite() {
    return codTipoTramite;
  }
  public void setCodTipoTramite(String codTipoTramite) {
    this.codTipoTramite = codTipoTramite;
  }
  public String	getCodUnidadInicio() {
    return codUnidadInicio;
  }
  public void setCodUnidadInicio(String	codUnidadInicio) {
    this.codUnidadInicio = codUnidadInicio;
  }
  public String getCodVisibleUnidadInicio() {
    return codVisibleUnidadInicio;
  }
  public void setCodVisibleUnidadInicio(String codVisibleUnidadInicio) {
    this.codVisibleUnidadInicio = codVisibleUnidadInicio;
  }
  public String	getCodUnidadTramite() {
    return codUnidadTramite;
  }
  public void setCodUnidadTramite(String codUnidadTramite) {
    this.codUnidadTramite =	codUnidadTramite;
  }
  public String	getDescTipoTramite() {
    return descTipoTramite;
  }
  public void setDescTipoTramite(String	descTipoTramite) {
    this.descTipoTramite = descTipoTramite;
  }
  public String	getDescUnidadInicio() {
    return descUnidadInicio;
  }
  public void setDescUnidadInicio(String descUnidadInicio) {
    this.descUnidadInicio =	descUnidadInicio;
  }
  public String	getDescUnidadTramite() {
    return descUnidadTramite;
  }
  public void setDescUnidadTramite(String descUnidadTramite) {
    this.descUnidadTramite = descUnidadTramite;
  }
  public String	getOcurrencias() {
    return ocurrencias;
  }
  public void setOcurrencias(String ocurrencias) {
    this.ocurrencias = ocurrencias;
  }
  public String	getPlazo() {
    return plazo;
  }
  public void setPlazo(String plazo) {
    this.plazo = plazo;
  }
  public String getUnidadesPlazo() {
    return unidadesPlazo;
  }
  public void	setUnidadesPlazo(String	unidadesPlazo) {
    this.unidadesPlazo = unidadesPlazo;
  }

  public Vector getListaEstadosTabla() {
    return listaEstadosTabla;
  }
  public void	setListaEstadosTabla(Vector listaEstadosTabla) {
    this.listaEstadosTabla = listaEstadosTabla;
  }

    public Vector getListaExpresionesTabla() {
      return listaExpresionesTabla;
    }
    public void	setListaExpresionesTabla(Vector expresiones) {
      this.listaExpresionesTabla= expresiones;
    }

  public Vector getListaTramitesTabla() {
    return listaTramitesTabla;
  }
  public void	setListaTramitesTabla(Vector listaTramitesTabla) {
    this.listaTramitesTabla = listaTramitesTabla;
  }
  public Vector getListaCodTramitesTabla() {
    return listaCodTramitesTabla;
  }
  public void setListaCodTramitesTabla(Vector listaCodTramitesTabla) {
    this.listaCodTramitesTabla = listaCodTramitesTabla;
  }
  public Vector getListaTiposTabla() {
    return listaTiposTabla;
  }
  public void setListaTiposTabla(Vector listaTiposTabla) {
    this.listaTiposTabla = listaTiposTabla;
  }
  public Vector getListaCodCondicionTabla() {
    return listaCodCondicionTabla;
  }
  public void setListaCodCondicionTabla(Vector listaCodCondicionTabla) {
    this.listaCodCondicionTabla = listaCodCondicionTabla;
  }

    public Vector getListaCodigosDocTabla() {
        return listaCodigosDocTabla;
    }

    public void setListaCodigosDocTabla(Vector listaCodigosDocTabla) {
        this.listaCodigosDocTabla = listaCodigosDocTabla;
    }


  public Vector getListaTipoDocumentos() {
    return listaTipoDocumentos;
  }
  public void	setListaTipoDocumentos(Vector	listaTipoDocumentos) {
    this.listaTipoDocumentos = listaTipoDocumentos;
  }



    public String getTramitePregunta() {
    return tramitePregunta;
  }
  public void	setTramitePregunta(String tramitePregunta) {
    this.tramitePregunta = tramitePregunta;
  }

  public String getTipoCondicion() {
    return tipoCondicion;
  }
  public void	setTipoCondicion(String	tipoCondicion) {
    this.tipoCondicion = tipoCondicion;
  }
  public String getTipoFavorableNO() {
    return tipoFavorableNO;
  }
  public void	setTipoFavorableNO(String tipoFavorableNO) {
    this.tipoFavorableNO = tipoFavorableNO;
  }
  public String getTipoFavorableSI() {
    return tipoFavorableSI;
  }
  public void	setTipoFavorableSI(String tipoFavorableSI) {
    this.tipoFavorableSI = tipoFavorableSI;
  }

  public String getCodTramiteCondEntrada() {
    return codTramiteCondEntrada;
  }
  public void setCodTramiteCondEntrada(String codTramiteCondEntrada) {
    this.codTramiteCondEntrada = codTramiteCondEntrada;
  }
  public String getIdTramiteCondEntrada()	{
    return idTramiteCondEntrada;
  }
  public void setIdTramiteCondEntrada(String idTramiteCondEntrada) {
    this.idTramiteCondEntrada	= idTramiteCondEntrada;
  }
  public String getDescTramiteCondEntrada() {
    return descTramiteCondEntrada;
  }
  public void setDescTramiteCondEntrada(String descTramiteCondEntrada) {
    this.descTramiteCondEntrada = descTramiteCondEntrada;
  }
  public String getEstadoTramiteCondEntrada() {
    return estadoTramiteCondEntrada;
  }
  public void setEstadoTramiteCondEntrada(String estadoTramiteCondEntrada) {
    this.estadoTramiteCondEntrada =	estadoTramiteCondEntrada;
  }
  public String getTipoCondEntrada() {
    return tipoCondEntrada;
  }
  public void setTipoCondEntrada(String tipoCondEntrada) {
    this.tipoCondEntrada =	tipoCondEntrada;
  }
  public String getCodCondEntrada()	{
    return codCondEntrada;
  }
  public void setCodCondEntrada(String codCondEntrada) {
    this.codCondEntrada	= codCondEntrada;
  }
  public String getExpresionCondEntrada() {
    return expresionCondEntrada;
  }
  public void setExpresionCondEntrada(String expresionCondEntrada) {
    this.expresionCondEntrada =	expresionCondEntrada;
  }

    public String getCodDocumentoCondEntrada() {
        return codDocumentoCondEntrada;
    }

    public void setCodDocumentoCondEntrada(String codDocumentoCondEntrada) {
        this.codDocumentoCondEntrada = codDocumentoCondEntrada;
    }


    public String getCodExpRel() {
        return codExpRel;
    }
    public void setCodExpRel(String codExpRel) {
        this.codExpRel =	codExpRel;
    }
    public String getDescExpRel() {
        return descExpRel;
    }
    public void setDescExpRel(String descExpRel) {
        this.descExpRel =	descExpRel;
    }
  public Vector getListasCondEntrada() {
    return listasCondEntrada;
  }
  public void	setListasCondEntrada(Vector listasCondEntrada) {
    this.listasCondEntrada = listasCondEntrada;
  }



  public Vector getListaCodigosDoc() {
    return listaCodigosDoc;
  }
  public void setListaCodigosDoc(Vector listaCodigosDoc) {
    this.listaCodigosDoc = listaCodigosDoc;
  }
  public Vector getListaCodTipoDoc() {
    return listaCodTipoDoc;
  }
  public void setListaCodTipoDoc(Vector listaCodTipoDoc) {
    this.listaCodTipoDoc = listaCodTipoDoc;
  }
  public Vector getListaFirmaDoc() {
    return listaFirmaDoc;
  }
  public void setListaFirmaDoc(Vector listaFirmaDoc) {
    this.listaFirmaDoc = listaFirmaDoc;
  }
  public Vector getListaNombresDoc() {
    return listaNombresDoc;
  }
  public void setListaNombresDoc(Vector listaNombresDoc) {
    this.listaNombresDoc = listaNombresDoc;
  }
  public Vector getListaPlantillaDoc() {
    return listaPlantillaDoc;
  }
  public void setListaPlantillaDoc(Vector	listaPlantillaDoc) {
    this.listaPlantillaDoc = listaPlantillaDoc;
  }
  public Vector getListaVisibleDoc() {
    return listaVisibleDoc;
  }
  public void setListaVisibleDoc(Vector listaVisibleDoc) {
    this.listaVisibleDoc = listaVisibleDoc;
  }

  public Vector getListaCodigoEnlaces() { return listaCodigoEnlaces; }
  public void setListaCodigoEnlaces(Vector listaCodigoEnlaces) { this.listaCodigoEnlaces = listaCodigoEnlaces; }
  public Vector getListaDescripcionEnlaces() { return listaDescripcionEnlaces; }
  public void setListaDescripcionEnlaces(Vector listaDescripcionEnlaces) { this.listaDescripcionEnlaces = listaDescripcionEnlaces; }
  public Vector getListaUrlEnlaces() { return listaUrlEnlaces; }
  public void setListaUrlEnlaces(Vector listaUrlEnlaces) { this.listaUrlEnlaces = listaUrlEnlaces; }
  public Vector getListaEstadoEnlaces() { return listaEstadoEnlaces; }
  public void setListaEstadoEnlaces(Vector listaEstadoEnlaces) { this.listaEstadoEnlaces = listaEstadoEnlaces; }

  public Vector getTramites() {
    return tramites;
  }
  public void	setTramites(Vector v) {
    this.tramites =	v;
  }

  public Vector getListaUnidadesTramitadoras() {
    return listaUnidadesTramitadoras;
  }
  public void	setListaUnidadesTramitadoras(Vector	listaUnidadesTramitadoras) {
    this.listaUnidadesTramitadoras=listaUnidadesTramitadoras;
  }

  public Vector getListaWebServices() {
    return listaWebServices;
  }
  public void setListaWebServices(Vector listaWebServices) {
    this.listaWebServices=listaWebServices;
  }

  public Vector getListaClasifTramite() {
    return listaClasifTramite;
  }
  public void	setListaClasifTramite(Vector listaClasifTramite) {
    this.listaClasifTramite=listaClasifTramite;
  }
    public Vector getListaExpRel() {
        return listaExpRel;
    }
    public void	setListaExpRel(Vector listaExpRel) {
        this.listaExpRel=listaExpRel;
    }
  public Vector getListaTramites() {
    return listaTramites;
  }
  public void	setListaTramites(Vector	listaTramites) {
    this.listaTramites=listaTramites;
  }

  public String getPosicion() {
    return posicion;
  }
  public void	setPosicion(String posicion) {
    this.posicion=posicion;
  }

  public String getTramiteEliminado()	{
    return tramiteEliminado;
  }
  public void	setTramiteEliminado(String tramiteEliminado) {
    this.tramiteEliminado=tramiteEliminado;
  }
  public String getFirma() {
    return firma;
  }
  public void	setFirma(String firma) {
    this.firma = firma;
  }
  public String getNombreDoc() {
    return nombreDoc;
  }
  public void	setNombreDoc(String nombreDoc) {
    this.nombreDoc = nombreDoc;
  }
  public String getPlantilla() {
    return plantilla;
  }
  public void	setPlantilla(String plantilla) {
    this.plantilla = plantilla;
  }
public String getContidoPlantilla() {
    return contidoPlantilla;
}

public void setContidoPlantilla(String contidoPlantilla) {
    this.contidoPlantilla = contidoPlantilla;
}
public String getVisibleInternet() {
    return visibleInternet;
  }
  public void	setVisibleInternet(String visibleInternet) {
    this.visibleInternet = visibleInternet;
  }
  public String getCodigoDoc() {
    return codigoDoc;
  }
  public void	setCodigoDoc(String codigoDoc) {
    this.codigoDoc = codigoDoc;
  }
  public String getCodTipoDoc()	{
    return codTipoDoc;
  }
  public void	setCodTipoDoc(String codTipoDoc) {
    this.codTipoDoc	= codTipoDoc;
  }
  public String getDescTipoDoc() {
    return descTipoDoc;
  }
  public void	setDescTipoDoc(String descTipoDoc) {
    this.descTipoDoc = descTipoDoc;
  }
  public Vector getListaDocusErroneos() {
    return listaDocusErroneos;
  }
  public void	setListaDocusErroneos(Vector listaDocusErroneos) {
    this.listaDocusErroneos = listaDocusErroneos;
  }
  public String getImportar()	{
    return importar;
  }
  public void	setImportar(String importar) {
    this.importar =	importar;
  }
  public String getNumeroCondicionSalida() {
    return numeroCondicionSalida;
  }
  public void	setNumeroCondicionSalida(String numeroCondicionSalida) {
    this.numeroCondicionSalida = numeroCondicionSalida;
  }
  public String getCodTramiteFlujoSalida() {
    return codTramiteFlujoSalida;
  }
  public void	setCodTramiteFlujoSalida(String codTramiteFlujoSalida) {
    this.codTramiteFlujoSalida = codTramiteFlujoSalida;
  }
  public String getIdTramiteFlujoSalida() {
    return idTramiteFlujoSalida;
  }
  public void	setIdTramiteFlujoSalida(String idTramiteFlujoSalida) {
    this.idTramiteFlujoSalida =	idTramiteFlujoSalida;
  }
  public String getNombreTramiteFlujoSalida() {
    return nombreTramiteFlujoSalida;
  }
  public void	setNombreTramiteFlujoSalida(String nombreTramiteFlujoSalida) {
    this.nombreTramiteFlujoSalida = nombreTramiteFlujoSalida;
  }
  public String getNumeroSecuencia() {
    return numeroSecuencia;
  }
  public void	setNumeroSecuencia(String numeroSecuencia) {
    this.numeroSecuencia = numeroSecuencia;
  }
  public Vector getListaDocumentos() {
    return listaDocumentos;
  }
  public void	setListaDocumentos(Vector listaDocumentos) {
    this.listaDocumentos = listaDocumentos;
  }
  public Vector getListaArea() {
    return listaArea;
  }
  public void	setListaArea(Vector listaArea) {
    this.listaArea = listaArea;
  }
  public Vector getListaCondEntradaImportar() {
    return listaCondEntradaImportar;
  }
  public void setListaCondEntradaImportar(Vector listaCondEntradaImportar) {
    this.listaCondEntradaImportar =	listaCondEntradaImportar;
  }
  public Vector getListaCondSalidaImportar() {
    return listaCondSalidaImportar;
  }
  public void setListaCondSalidaImportar(Vector	listaCondSalidaImportar) {
    this.listaCondSalidaImportar = listaCondSalidaImportar;
  }
  public Vector getListaPreguntaCondSalida() {
    return listaPreguntaCondSalida;
  }
  public void setListaPreguntaCondSalida(Vector	listaPreguntaCondSalida) {
    this.listaPreguntaCondSalida = listaPreguntaCondSalida;
  }
  public Vector getListaDocumentosImportar() {
    return listaDocumentosImportar;
  }
  public void setListaDocumentosImportar(Vector	listaDocumentosImportar) {
    this.listaDocumentosImportar = listaDocumentosImportar;
  }
  public Vector getListaTramitesImportar() {
    return listaTramitesImportar;
  }
  public void setListaTramitesImportar(Vector listaTramitesImportar) {
    this.listaTramitesImportar = listaTramitesImportar;
  }
  public Vector getListaFlujoSalida() {
    return listaFlujoSalida;
  }
  public void	setListaFlujoSalida(Vector listaFlujoSalida) {
    this.listaFlujoSalida	= listaFlujoSalida;
  }
  public String getCodTramiteInicialFlujoSalida() {
    return codTramiteInicialFlujoSalida;
  }
  public void	setCodTramiteInicialFlujoSalida(String codTramiteInicialFlujoSalida) {
    this.codTramiteInicialFlujoSalida	= codTramiteInicialFlujoSalida;
  }

  public Vector getListaPlantillas() {
    return listaPlantillas;
  }
  public void	setListaPlantillas(Vector listaPlantillas) {
    this.listaPlantillas=	listaPlantillas;
  }

    public Vector getListaDocActivos() {
        return listaDocActivos;
    }

    public void setListaDocActivos(Vector listaDocActivos) {
        this.listaDocActivos = listaDocActivos;
    }

    public String getEditorTexto() {
        return editorTexto;
    }

    public void setEditorTexto(String editorTexto) {
        this.editorTexto = editorTexto;
    }

    public Vector getListaEditoresTexto() {
        return listaEditoresTexto;
    }

    public void setListaEditoresTexto(Vector listaEditoresTexto) {
        this.listaEditoresTexto = listaEditoresTexto;
    }

    public String getNoModificar() {
    return noModificar;
  }
  public void	setNoModificar(String noModificar) {
    this.noModificar = noModificar;
  }
  public String getDeCatalogo()	{
    return deCatalogo;
  }
  public void	setDeCatalogo(String deCatalogo) {
    this.deCatalogo	= deCatalogo;
  }

  public Vector getListaTramitesDesfavorable() {
    return listaTramitesDesfavorable;
  }
  public void	setListaTramitesDesfavorable(Vector	listaTramitesDesfavorable) {
    this.listaTramitesDesfavorable = listaTramitesDesfavorable;
  }
  public Vector getListaTramitesFavorable()	{
    return listaTramitesFavorable;
  }
  public void	setListaTramitesFavorable(Vector listaTramitesFavorable) {
    this.listaTramitesFavorable	= listaTramitesFavorable;
  }
  public String getDeCatalogoDeProcedimiento() {
    return deCatalogoDeProcedimiento;
  }
  public void	setDeCatalogoDeProcedimiento(String	deCatalogoDeProcedimiento) {
    this.deCatalogoDeProcedimiento = deCatalogoDeProcedimiento;
  }
  public String getObligatorio() {
    return obligatorio;
  }
  public void	setObligatorio(String obligatorio) {
    this.obligatorio = obligatorio;
  }
  public String getObligatorioDesf() {
    return obligatorioDesf;
  }
  public void	setObligatorioDesf(String obligatorioDesf) {
    this.obligatorioDesf = obligatorioDesf;
  }

  public Vector getListaUnidadesInicio() {
    return listaUnidadesInicio;
  }
  public void	setListaUnidadesInicio(Vector	listaUnidadesInicio) {
    this.listaUnidadesInicio = listaUnidadesInicio;
  }


  public String getTramiteActual(){  return	tramiteActual;}
  public void	setTramiteActual(String	num) {  this.tramiteActual = num;}

  public String getNumeroTramites(){	return numeroTramites;}
  public void	setNumeroTramites(String num)	{  this.numeroTramites = num;}

  public String getCodPlantilla() {
    return codPlantilla;
  }
  public void	setCodPlantilla(String codPlantilla) {
    this.codPlantilla = codPlantilla;
  }

    public String getInteresado() {
      return interesado;
    }
    public void	setInteresado(String interesado) {
      this.interesado = interesado;
    }

    public String getRelacion() {
      return relacion;
    }
    public void	setRelacion(String relacion) {
      this.relacion = relacion;
    }

    public String getDocActivo() {
        return docActivo;
    }

    public void setDocActivo(String docActivo) {
        this.docActivo = docActivo;
    }

    public Vector getListaCodPlantilla() {
    return listaCodPlantilla;
  }
  public void	setListaCodPlantilla(Vector listaCodPlantilla) {
    this.listaCodPlantilla = listaCodPlantilla;
  }

    public Vector getListaContidoPlantilla() {
        return listaContidoPlantilla;
    }

    public void setListaContidoPlantilla(Vector listaContidoPlantilla) {
        this.listaContidoPlantilla = listaContidoPlantilla;
    }

    public Vector getListaEnlaces() {
        return listaEnlaces;
    }
  
    public Vector getListaInteresadoPlantilla() {
        return listaInteresadoPlantilla;
    }

    public void setListaInteresadoPlantilla(Vector listaInteresadoPlantilla) {
        this.listaInteresadoPlantilla = listaInteresadoPlantilla;
    }

    public Vector getListaRelacionPlantilla() {
        return listaRelacionPlantilla;
    }

    public void setListaRelacionPlantilla(Vector listaRelacionPlantilla) {
        this.listaRelacionPlantilla = listaRelacionPlantilla;
    }

    public void	setListaEnlaces(Vector enlaces) {
    this.listaEnlaces= enlaces;
  }

  public Vector getListaCampos() {
    return listaCampos;
  }
  public void setListaCampos(Vector listaCampos) {
    this.listaCampos = listaCampos;
  }
  public Vector getListaCodCampos() {
    return listaCodCampos;
  }
  public void setListaCodCampos(Vector listaCodCampos) {
    this.listaCodCampos = listaCodCampos;
  }
  public Vector getListaCodPlantill() {
    return listaCodPlantill;
  }
  public void setListaCodPlantill(Vector listaCodPlantill) {
    this.listaCodPlantill = listaCodPlantill;
  }
  public Vector getListaCodTipoDato() {
    return listaCodTipoDato;
  }
  public void setListaCodTipoDato(Vector listaCodTipoDato) {
    this.listaCodTipoDato = listaCodTipoDato;
  }
  public Vector getListaDescCampos() {
    return listaDescCampos;
  }
  public void setListaDescCampos(Vector listaDescCampos) {
    this.listaDescCampos = listaDescCampos;
  }
  public Vector getListaMascara() {
    return listaMascara;
  }
  public void setListaMascara(Vector listaMascara) {
    this.listaMascara = listaMascara;
  }
  public Vector getListaObligatorio() {
    return listaObligatorio;
  }
  public void setListaObligatorio(Vector listaObligatorio) {
    this.listaObligatorio = listaObligatorio;
  }
  public Vector getListaOrden() {
    return listaOrden;
  }
  public void setListaOrden(Vector listaOrden) {
    this.listaOrden = listaOrden;
  }
  public Vector getListaRotulo() {
    return listaRotulo;
  }
  public void setListaRotulo(Vector listaRotulo) {
    this.listaRotulo = listaRotulo;
  }
  public Vector getListaTamano() {
    return listaTamano;
  }
  public void setListaTamano(Vector listaTamano) {
    this.listaTamano = listaTamano;
  }
  public Vector getListaVisible() {
    return listaVisible;
  }
  public void setListaVisible(Vector listaVisible) {
    this.listaVisible = listaVisible;
  }
  public Vector getListaActivo() {
    return listaActivo;
  }
  public void setListaActivo(Vector listaActivo) {
    this.listaActivo = listaActivo;
  }

    public Vector getListaOcultos() {
        return listaOcultos;
    }

    public void setListaOcultos(Vector listaOcultos) {
        this.listaOcultos = listaOcultos;
    }

    public Vector getListaBloqueados() {
        return listaBloqueados;
    }

    public void setListaBloqueados(Vector listaBloqueados) {
        this.listaBloqueados = listaBloqueados;
    }

    public Vector getListaPlazoFecha() {
        return listaPlazoFecha;
    }

    public void setListaPlazoFecha(Vector listaPlazoFecha) {
        this.listaPlazoFecha = listaPlazoFecha;
    }

    public Vector getListaCheckPlazoFecha() {
        return listaCheckPlazoFecha;
    }

    public void setListaCheckPlazoFecha(Vector listaCheckPlazoFecha) {
        this.listaCheckPlazoFecha = listaCheckPlazoFecha;
    }



  public String getInstrucciones() {
    return instrucciones;
  }
  public void setInstrucciones(String instrucciones) {
    this.instrucciones = instrucciones;
  }


    public String getNotInteresadosFin() {
        return notInteresadosFin;
    }

    public void setNotInteresadosFin(String notInteresadosFin) {
        this.notInteresadosFin = notInteresadosFin;
    }

    public String getNotInteresadosIni() {
        return notInteresadosIni;
    }

    public void setNotInteresadosIni(String notInteresadosIni) {
        this.notInteresadosIni = notInteresadosIni;
    }

    public String getNotUsuInicioTramiteIni() {
        return notUsuInicioTramiteIni;
    }

    public void setNotUsuInicioTramiteIni(String notUsuInicioTramiteIni) {
        this.notUsuInicioTramiteIni = notUsuInicioTramiteIni;
    }

    public String getNotUsuInicioTramiteFin() {
        return notUsuInicioTramiteFin;
    }

    public void setNotUsuInicioTramiteFin(String notUsuInicioTramiteFin) {
        this.notUsuInicioTramiteFin = notUsuInicioTramiteFin;
    }

    public String getNotUsuInicioExpedIni() {
        return notUsuInicioExpedIni;
    }

    public void setNotUsuInicioExpedIni(String notUsuInicioExpedIni) {
        this.notUsuInicioExpedIni = notUsuInicioExpedIni;
    }

    public String getNotUsuInicioExpedFin() {
        return notUsuInicioExpedFin;
    }

    public void setNotUsuInicioExpedFin(String notUsuInicioExpedFin) {
        this.notUsuInicioExpedFin = notUsuInicioExpedFin;
    }

    public String getNotUnidadTramitFin() {
        return notUnidadTramitFin;
    }

    public void setNotUnidadTramitFin(String notUnidadTramitFin) {
        this.notUnidadTramitFin = notUnidadTramitFin;
    }

    public String getNotUnidadTramitIni() {
        return notUnidadTramitIni;
    }

    public void setNotUnidadTramitIni(String notUnidadTramitIni) {
        this.notUnidadTramitIni = notUnidadTramitIni;
    }

    public String getNotUsuUnidadTramitFin() {
        return notUsuUnidadTramitFin;
    }

    public void setNotUsuUnidadTramitFin(String notUsuUnidadTramitFin) {
        this.notUsuUnidadTramitFin = notUsuUnidadTramitFin;
    }

    public String getNotUsuUnidadTramitIni() {
        return notUsuUnidadTramitIni;
    }

    public void setNotUsuUnidadTramitIni(String notUsuUnidadTramitIni) {
        this.notUsuUnidadTramitIni = notUsuUnidadTramitIni;
    }

    public Vector getListaCamposTramitesCondEntrada() {
        return listaCamposTramitesCondEntrada;
    }

    public void setListaCamposTramitesCondEntrada(Vector listaCamposTramitesCondEntrada) {
        this.listaCamposTramitesCondEntrada = listaCamposTramitesCondEntrada;
    }

    public Vector getListaCargos() {
        return listaCargos;
    }

    public void setListaCargos(Vector listaCargos) {
        this.listaCargos = listaCargos;
    }

    public String getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(String codCargo) {
        this.codCargo = codCargo;
    }

    public String getCodVisibleCargo() {
        return codVisibleCargo;
    }

    public void setCodVisibleCargo(String codVisibleCargo) {
        this.codVisibleCargo = codVisibleCargo;
    }

    public Vector<UORDTO> getUnidadesTramitadoras() {
        return unidadesTramitadoras;
    }

    public void setUnidadesTramitadoras(Vector<UORDTO> unidadesTramitadoras) {
        this.unidadesTramitadoras = unidadesTramitadoras;
    }






  //notificacion de tramite automatica


    public boolean getNotificarCercaFinPlazo() {
        return notificarCercaFinPlazo;
    }

    public void setNotificarCercaFinPlazo(boolean notificarCercaFinPlazo) {
        this.notificarCercaFinPlazo = notificarCercaFinPlazo;
    }

    public boolean getNotificarFueraDePlazo() {
        return notificarFueraDePlazo;
    }

    public void setNotificarFueraDePlazo(boolean notificarFueraDePlazo) {
        this.notificarFueraDePlazo = notificarFueraDePlazo;
    }

    public int getTipoNotCercaFinPlazo() {
        return tipoNotCercaFinPlazo;
    }

    public void setTipoNotCercaFinPlazo(int tipoNotCercaFinPlazo) {
        this.tipoNotCercaFinPlazo = tipoNotCercaFinPlazo;
    }

    public int getTipoNotFueraDePlazo() {
        return tipoNotFueraDePlazo;
    }

    public void setTipoNotFueraDePlazo(int tipoNotFueraDePlazo) {
        this.tipoNotFueraDePlazo = tipoNotFueraDePlazo;
    }

    public String getAdmiteNotificacionElectronica() {
        return admiteNotificacionElectronica;
    }

    public void setAdmiteNotificacionElectronica(String admiteNotificacionElectronica) {
        this.admiteNotificacionElectronica = admiteNotificacionElectronica;
    }

    public String getCodigoOtroUsuarioFirma() {
        return codigoOtroUsuarioFirma;
    }

    public void setCodigoOtroUsuarioFirma(String codigoOtroUsuarioFirma) {
        this.codigoOtroUsuarioFirma = codigoOtroUsuarioFirma;
    }

    public String getNombreOtroUsuarioFirma() {
        return nombreOtroUsuarioFirma;
    }

    public void setNombreOtroUsuarioFirma(String nombreOtroUsuarioFirma) {
        this.nombreOtroUsuarioFirma = nombreOtroUsuarioFirma;
    }

    public String getCodigoTipoNotificacionElectronica() {
        return codigoTipoNotificacionElectronica;
    }

    public void setCodigoTipoNotificacionElectronica(String codigoTipoNotificacionElectronica) {
        this.codigoTipoNotificacionElectronica = codigoTipoNotificacionElectronica;
    }

    public String getTipoUsuarioFirma() {
        return tipoUsuarioFirma;
    }

    public void setTipoUsuarioFirma(String tipoUsuarioFirma) {
        this.tipoUsuarioFirma = tipoUsuarioFirma;
    }

    public String getVisibleExt() {
        return visibleExt;
    }

    public void setVisibleExt(String visibleExt) {
        this.visibleExt = visibleExt;
    }

    public String getNotUsuTraFinPlazo() {
        return notUsuTraFinPlazo;
    }

    public void setNotUsuTraFinPlazo(String notUsuTraFinPlazo) {
        this.notUsuTraFinPlazo = notUsuTraFinPlazo;
    }

    public String getNotUsuExpFinPlazo() {
        return notUsuExpFinPlazo;
    }

    public void setNotUsuExpFinPlazo(String notUsuExpFinPlazo) {
        this.notUsuExpFinPlazo = notUsuExpFinPlazo;
    }

    public String getNotUORFinPlazo() {
        return notUORFinPlazo;
    }

    public void setNotUORFinPlazo(String notUORFinPlazo) {
        this.notUORFinPlazo = notUORFinPlazo;
    }
    
    
   public String getFirmadocumentoDniUsuario() {
        return firmadocumentoDniUsuario;
    }

    public void setFirmadocumentoDniUsuario(String firmadocumentoDniUsuario) {
        this.firmadocumentoDniUsuario = firmadocumentoDniUsuario;
    }

    public List<String> getListaFirmasDocumentoDnisUsuario() {
        return listaFirmasDocumentoDnisUsuario;
    }

    public void setListaFirmasDocumentoDnisUsuario(List<String> listaFirmasDocumentoDnisUsuario) {
        this.listaFirmasDocumentoDnisUsuario = listaFirmasDocumentoDnisUsuario;
    }
    



 //fin notificacion de tramite automatica


  public void copy(DefinicionTramitesValueObject other) {
    this.txtCodigo = other.txtCodigo;
    this.txtDescripcion = other.txtDescripcion;
    this.numeroTramite = other.numeroTramite;
    this.nombreTramite = other.nombreTramite;
    this.disponible =	other.disponible;
    this.tipoTramite = other.tipoTramite;
    this.numeroTramite = other.numeroTramite;
    this.codEnlace = other.codEnlace;
    this.descEnlace =	other.descEnlace;
    this.texto = other.texto;
    this.tipoFinalizacion =	other.tipoFinalizacion;
    this.ejecucion = other.ejecucion;
    this.tipoPregunta	= other.tipoPregunta;
    this.tipoTramiteSF = other.tipoTramiteSF;
    this.tipoTramiteNF = other.tipoTramiteNF;
    this.finalizacionSF = other.finalizacionSF;
    this.finalizacionNF = other.finalizacionNF;
    this.certificadoOrganismoFirmaNotificacion = other.certificadoOrganismoFirmaNotificacion;
    this.notificacionElectronicaObligatoria = other.notificacionElectronicaObligatoria;
  }


  /**
  * Valida el estado	de esta RegistroSaida
  * Puede ser invocado desde la capa cliente	o desde la capa de negocio
  * @exception	ValidationException si el estado no	es válido
  */
  public void validate(String idioma) throws ValidationException {
    String sufijo =	"";
    if ("euskera".equals(idioma)) sufijo="_eu";
    boolean	correcto = true;
    Messages errors	= new	Messages();

    if (!errors.empty())
    throw	new ValidationException(errors);
    isValid =	true;
  }

  /** Devuelve un booleano que representa si el estado de	este RegistroSaida es válido.	*/
  public boolean IsValid() { return isValid; }


  //notificacion de tramite automatica
  private boolean notificarCercaFinPlazo;
  private int tipoNotCercaFinPlazo;
  private boolean notificarFueraDePlazo;
  private int tipoNotFueraDePlazo;
  private String admiteNotificacionElectronica;
 //fin notificacion de tramite automatica
private String tramitePregunta;

  private String txtCodigo;
  private String txtDescripcion;
  private String numeroTramite;
  private String codigoTramite;
  private String nombreTramite;
  private String disponible;
  private String tramiteInicio;
  private String tipoTramite;
  private String codEnlace;
  private String descEnlace;
  private String plazo;
  private String unidadesPlazo;
  private String codAreaTra;
  private String descAreaTra;
  private String codUnidadInicio;
  private String codVisibleUnidadInicio;
  private String descUnidadInicio;
  private String codUnidadTramite;
  private String descUnidadTramite;
  private String codTipoTramite;
  private String descTipoTramite;
  private String ocurrencias;
  private String codClasifTramite;
  private String descClasifTramite;
  private String texto;
  private String tipoFinalizacion;
  private String ejecucion;
  private String tipoPregunta;
  private String tipoTramiteSF;
  private String tipoTramiteNF;
  private String finalizacionSF;
  private String finalizacionNF;
  private String tipoCondicion;
  private String tipoFavorableSI;
  private String tipoFavorableNO;
  private String instrucciones;
  private String notUnidadTramitIni;
  private String notUnidadTramitFin;
  private String notUsuUnidadTramitIni;
  private String notUsuUnidadTramitFin;
  private String notInteresadosIni;
  private String notUsuInicioTramiteIni;
  private String notUsuInicioTramiteFin;
  private String notUsuInicioExpedIni;
  private String notUsuInicioExpedFin;
  private String notUsuTraFinPlazo;	
  private String notUsuExpFinPlazo;	
  private String notUORFinPlazo;
  private String expresionCondEntrada;
  private String codExpRel;
  private String descExpRel;
  private String fechaBaja;

  private String notInteresadosFin;
  private String codMunicipio;
  private String codAplicacion;
  
  private boolean tramiteNotificado = false;

  private Vector tramites;

  private Vector listaUnidadesTramitadoras;  // Lista de todas las uors
  private Vector<UORDTO> unidadesTramitadoras // Lista de uors que pueden tramitar este tramite
          = new Vector<UORDTO>();
  private Vector listaClasifTramite;
    private Vector listaExpRel;
  private Vector listaTramites;
  private Vector listaTipoDocumentos;

  private Vector listaWebServices;

  private Vector listaTramitesTabla;
  private Vector listaEstadosTabla;
  private Vector listaCodTramitesTabla;
  private Vector listaCodCondicionTabla;
  private Vector listaTiposTabla;
  private Vector listaExpresionesTabla;
  private Vector listaCodigosDocTabla;

  private String codTramiteCondEntrada;
  private String idTramiteCondEntrada;
  private String descTramiteCondEntrada;
  private String estadoTramiteCondEntrada;
  private String tipoCondEntrada;
  private String codCondEntrada;
  private String codDocumentoCondEntrada;
  private Vector listasCondEntrada;
  private Vector listaCamposTramitesCondEntrada;

  private Vector listaCodigosDoc;
  private Vector listaNombresDoc;
  private Vector listaVisibleDoc;
  private Vector listaPlantillaDoc;
  private Vector listaContidoPlantilla;
  private Vector listaInteresadoPlantilla;
  private Vector listaRelacionPlantilla;
  private Vector listaCodPlantilla;
  private Vector listaCodTipoDoc;
  private Vector listaFirmaDoc;
  private Vector listaArea;
  private Vector listaPlantillas;
  private Vector listaDocActivos;
  private Vector listaEditoresTexto;

  private Vector listaCodigoEnlaces;
  private Vector listaDescripcionEnlaces;
  private Vector listaUrlEnlaces;
  private Vector listaEstadoEnlaces;

  private List<String> listaFirmasDocumentoIdsUsuario;
   private List<String> listaFirmasDocumentoDnisUsuario;
  private List<String> listaFirmasDocumentoLogsUsuario;
  private List<FirmaFlujoVO> listaFirmasFlujo;
  private List<FirmaCircuitoVO> listaFirmasCircuito;
  
  private String codigoDoc;
  private String nombreDoc;
  private String visibleInternet;
  private String plantilla;
  private String contidoPlantilla;
  private String descTipoDoc;
  private String codTipoDoc;
  private String firma;
  private String codPlantilla;
  private String interesado;
  private String relacion;
  private String editorTexto;
  private String docActivo;
  private Vector listaDocumentos;
  private Vector listaDocusErroneos;

  private String posicion;	//para saber si es el ultimo tramite o el	primero
  private String tramiteEliminado;  //para saber si	en tramite fue eliminado

  private String noModificar;
  private String deCatalogo;
  private String deCatalogoDeProcedimiento;

  // PARA IMPORTAR
  private String importar;
  private String obligatorio;
  private String obligatorioDesf;
  private Vector listaTramitesImportar;
  private Vector listaDocumentosImportar;
  private Vector listaCondEntradaImportar;
  private Vector listaCondSalidaImportar;
  private Vector listaPreguntaCondSalida;
  private Vector listaFlujoSalida;
  
  private String visibleExt; 

  private String codTramiteInicialFlujoSalida;
  private String numeroCondicionSalida;
  private String numeroSecuencia;
  private String idTramiteFlujoSalida;
  private String codTramiteFlujoSalida;
  private String nombreTramiteFlujoSalida;

  private Vector listaTramitesFavorable;
  private Vector listaTramitesDesfavorable;

  private Vector listaUnidadesInicio;

  private String tramiteActual;
  private String numeroTramites;

  private Vector listaEnlaces;

  private Vector listaCampos;
  private Vector listaCodCampos;
  private Vector listaDescCampos;
  private Vector listaCodPlantill;
  private Vector listaCodTipoDato;
  private Vector listaTamano;
  private Vector listaMascara;
  private Vector listaObligatorio;
  private Vector listaOrden;
  private Vector listaRotulo;
  private Vector listaVisible;
  private Vector listaActivo;
  private Vector listaOcultos;
  private Vector listaBloqueados;
  private Vector listaValidacion;
  private Vector listaOperacion;
  private Vector listaPlazoFecha;
  private Vector listaCheckPlazoFecha;

    private Vector listaCargos;
    private String codCargo;
    private String codVisibleCargo;

  private Vector listaConfSW;

  private String codigoTipoNotificacionElectronica;
  private String tipoUsuarioFirma;
  private String codigoOtroUsuarioFirma;
  private String nombreOtroUsuarioFirma;

  // PARA IMPORTAR INCLUYENDO LOS DATOS DE FLUJO DE FIRMAS
  private Integer firmaDocumentoIdUsuario;  
  private String firmaDocumentoLogUsuario;
  private String firmadocumentoDniUsuario;  
  private FirmaFlujoVO firmaFlujo;
  private FirmaCircuitoVO firmaCircuito;
  
  /** Variable booleana que indica si el estado de la instancia de RegistroSaida es válido o no */
  private boolean isValid;
 
//codigo de tramite generado por flexia
  private String codigoInternoTramite;
//INICIO #76248 
   private boolean notificacionElectronicaObligatoria;
   private boolean certificadoOrganismoFirmaNotificacion;
   private String codDepartamentoNotificacion;
   private String descripcionDepartamentoNotificacion;
   private Boolean existeDepartamento;

   //WS
   private String tramWSCod;
   private String tramWSOb;
   private String tramWSTCod;
   private String tramWSTOb;
   private String tramWSRCod;
   private String tramWSROb;
   private byte[] byteArrayPlantillaTramitacion;

    public boolean getCertificadoOrganismoFirmaNotificacion() {
        return certificadoOrganismoFirmaNotificacion;
    }

    public void setCertificadoOrganismoFirmaNotificacion(boolean certificadoOrganismoFirmaNotificacion) {
        this.certificadoOrganismoFirmaNotificacion = certificadoOrganismoFirmaNotificacion;
    }

    public boolean getNotificacionElectronicaObligatoria() {
        return notificacionElectronicaObligatoria;
    }

    public void setNotificacionElectronicaObligatoria(boolean notificacionElectronicaObligatoria) {
        this.notificacionElectronicaObligatoria = notificacionElectronicaObligatoria;
    }
  //FIN #76248
  
  
  private ArrayList<FlujoSalidaTramiteVO> flujoSalidaTramiteImportacion = null;

    // Campos añadidos para manejar la información de los Servicios Web.
    private InfoConfTramSWVO infoSWAvanzar;
    private InfoConfTramSWVO infoSWRetroceder;
    private int plazoFin;//variable que nos indica el % que falta para finalizar un tramite
    
    private boolean generarPlazos = false;
   

   private ArrayList<TramitacionExternaBase> pluginPantallasTramitacionExterna = null;   
   private String codPluginPantallaTramitacionExterna=null;
   private String codUrlPluginPantallaTramitacionExterna=null;
   private String urlPluginPantallaTramitacionExterna=null;
   private String implClassPluginPantallaTramitacionExterna = null;
   

    public InfoConfTramSWVO getInfoSWRetroceder() {
        return infoSWRetroceder;
    }

    public void setInfoSWRetroceder(InfoConfTramSWVO infoSWRetroceder) {
        this.infoSWRetroceder = infoSWRetroceder;
    }

    public InfoConfTramSWVO getInfoSWAvanzar() {
        return infoSWAvanzar;
    }

    public void setInfoSWAvanzar(InfoConfTramSWVO infoSWAvanzar) {
        this.infoSWAvanzar = infoSWAvanzar;
    }

    public Vector getListaConfSW() {
        return listaConfSW;
    }

    public void setListaConfSW(Vector listaConfSW) {
        this.listaConfSW = listaConfSW;
    }
    
    public boolean isGenerarPlazos() {
        return generarPlazos;
    }

    public void setGenerarPlazos(boolean generarPlazos) {        
        this.generarPlazos = generarPlazos;
    }

    /**
     * @return the flujoSalidaTramiteImportacion
     */
    public ArrayList<FlujoSalidaTramiteVO> getFlujoSalidaTramiteImportacion() {
        return flujoSalidaTramiteImportacion;
    }

    /**
     * @param flujoSalidaTramiteImportacion the flujoSalidaTramiteImportacion to set
     */
    public void setFlujoSalidaTramiteImportacion(ArrayList<FlujoSalidaTramiteVO> flujoSalidaTramiteImportacion) {
        this.flujoSalidaTramiteImportacion = flujoSalidaTramiteImportacion;
    }

    /**
     * @return the pluginPantallasTramitacionExterna
     */
    public ArrayList<TramitacionExternaBase> getPluginPantallasTramitacionExterna() {
        return pluginPantallasTramitacionExterna;
    }

    /**
     * @param pluginPantallasTramitacionExterna the pluginPantallasTramitacionExterna to set
     */
    public void setPluginPantallasTramitacionExterna(ArrayList<TramitacionExternaBase> pluginPantallasTramitacionExterna) {
        this.pluginPantallasTramitacionExterna = pluginPantallasTramitacionExterna;
    }

    
    /**
     * @return the codPluginPantallaTramitacionExterna
     */
    public String getCodPluginPantallaTramitacionExterna() {
        return codPluginPantallaTramitacionExterna;
    }

    /**
     * @param codPluginPantallaTramitacionExterna the codPluginPantallaTramitacionExterna to set
     */
    public void setCodPluginPantallaTramitacionExterna(String codPluginPantallaTramitacionExterna) {
        this.codPluginPantallaTramitacionExterna = codPluginPantallaTramitacionExterna;
    }

    /**
     * @return the urlPluginPantallaTramitacionExterna
     */
    public String getUrlPluginPantallaTramitacionExterna() {
        return urlPluginPantallaTramitacionExterna;
    }

    /**
     * @param urlPluginPantallaTramitacionExterna the urlPluginPantallaTramitacionExterna to set
     */
    public void setUrlPluginPantallaTramitacionExterna(String urlPluginPantallaTramitacionExterna) {
        this.urlPluginPantallaTramitacionExterna = urlPluginPantallaTramitacionExterna;
    }

    /**
     * @return the implClassPluginPantallaTramitacionExterna
     */
    public String getImplClassPluginPantallaTramitacionExterna() {
        return implClassPluginPantallaTramitacionExterna;
    }

    /**
     * @param implClassPluginPantallaTramitacionExterna the implClassPluginPantallaTramitacionExterna to set
     */
    public void setImplClassPluginPantallaTramitacionExterna(String implClassPluginPantallaTramitacionExterna) {
        this.implClassPluginPantallaTramitacionExterna = implClassPluginPantallaTramitacionExterna;
    }

    /**
     * @return the codUrlPluginPantallaTramitacionExterna
     */
    public String getCodUrlPluginPantallaTramitacionExterna() {
        return codUrlPluginPantallaTramitacionExterna;
    }

    /**
     * @param codUrlPluginPantallaTramitacionExterna the codUrlPluginPantallaTramitacionExterna to set
     */
    public void setCodUrlPluginPantallaTramitacionExterna(String codUrlPluginPantallaTramitacionExterna) {
        this.codUrlPluginPantallaTramitacionExterna = codUrlPluginPantallaTramitacionExterna;
    }

    /**
     * @return the codDepartamentoNotificacion
     */
    public String getCodDepartamentoNotificacion() {
        return codDepartamentoNotificacion;
    }

    /**
     * @param codDepartamentoNotificacion the codDepartamentoNotificacion to set
     */
    public void setCodDepartamentoNotificacion(String codDepartamentoNotificacion) {
        this.codDepartamentoNotificacion = codDepartamentoNotificacion;
    }

    /**
     * @return the listaValidacion
     */
    public Vector getListaValidacion() {
        return listaValidacion;
    }

    /**
     * @param listaValidacion the listaValidacion to set
     */
    public void setListaValidacion(Vector listaValidacion) {
        this.listaValidacion = listaValidacion;
    }

    /**
     * @return the listaOperacion
     */
    public Vector getListaOperacion() {
        return listaOperacion;
    }

    /**
     * @param listaOperacion the listaOperacion to set
     */
    public void setListaOperacion(Vector listaOperacion) {
        this.listaOperacion = listaOperacion;
    }
    
    private Vector listaCodAgrupacion;
    private Vector listaDescAgrupacion;
    private Vector listaOrdenAgrupacion;
    private Vector listaAgrupacionActiva;

    public Vector getListaAgrupacionActiva() {
        return listaAgrupacionActiva;
    }
    public void setListaAgrupacionActiva(Vector listaAgrupacionActiva) {
        this.listaAgrupacionActiva = listaAgrupacionActiva;
    }

    public Vector getListaCodAgrupacion() {
        return listaCodAgrupacion;
    }
    public void setListaCodAgrupacion(Vector listaCodAgrupacion) {
        this.listaCodAgrupacion = listaCodAgrupacion;
    }

    public Vector getListaDescAgrupacion() {
        return listaDescAgrupacion;
    }
    public void setListaDescAgrupacion(Vector listaDescAgrupacion) {
        this.listaDescAgrupacion = listaDescAgrupacion;
    }

    public Vector getListaOrdenAgrupacion() {
        return listaOrdenAgrupacion;
    }
    public void setListaOrdenAgrupacion(Vector listaOrdenAgrupacion) {
        this.listaOrdenAgrupacion = listaOrdenAgrupacion;
    }
    
    private Vector listaAgrupaciones;
    public Vector getListaAgrupaciones() {
        return listaAgrupaciones;
    }
    public void setListaAgrupaciones(Vector listaAgrupaciones) {
        this.listaAgrupaciones = listaAgrupaciones;
    }
    
    private Vector listaCodAgrupacionCampo;
    public Vector getListaCodAgrupacionCampo() {
        return listaCodAgrupacionCampo;
    }
    public void setListaCodAgrupacionCampo(Vector listaCodAgrupacionCampo) {
        this.listaCodAgrupacionCampo = listaCodAgrupacionCampo;
    }
    
    private Vector listaPosX;
    private Vector listaPosY;
    
    public Vector getListaPosX() {
        return listaPosX;
    }
    public void setListaPosX(Vector listaPosX) {
        this.listaPosX = listaPosX;
    }

    public Vector getListaPosY() {
        return listaPosY;
    }
    public void setListaPosY(Vector listaPosY) {
        this.listaPosY = listaPosY;
    }

    public List<String> getListaFirmasDocumentoIdsUsuario() {
        return listaFirmasDocumentoIdsUsuario;
    }

    public void setListaFirmasDocumentoIdsUsuario(List<String> listaFirmasDocumentoIdsUsuario) {
        this.listaFirmasDocumentoIdsUsuario = listaFirmasDocumentoIdsUsuario;
    }

    public List<String> getListaFirmasDocumentoLogsUsuario() {
        return listaFirmasDocumentoLogsUsuario;
    }

    public void setListaFirmasDocumentoLogsUsuario(List<String> listaFirmasDocumentoLogsUsuario) {
        this.listaFirmasDocumentoLogsUsuario = listaFirmasDocumentoLogsUsuario;
    }

    public List<FirmaFlujoVO> getListaFirmasFlujo() {
        return listaFirmasFlujo;
    }

    public void setListaFirmasFlujo(List<FirmaFlujoVO> listaFirmasFlujo) {
        this.listaFirmasFlujo = listaFirmasFlujo;
    }

    public List<FirmaCircuitoVO> getListaFirmasCircuito() {
        return listaFirmasCircuito;
    }

    public void setListaFirmasCircuito(List<FirmaCircuitoVO> listaFirmasCircuito) {
        this.listaFirmasCircuito = listaFirmasCircuito;
    }

    
    
    /**
     * @return the codigoInternoTramite
     */
    public String getCodigoInternoTramite() {
        return codigoInternoTramite;
    }

    /**
     * @param codigoInternoTramite the codigoInternoTramite to set
     */
    public void setCodigoInternoTramite(String codigoInternoTramite) {
        this.codigoInternoTramite = codigoInternoTramite;
    }

    /**
     * @return the fechaBaja
     */
    public String getFechaBaja() {
        return fechaBaja;
    }

    /**
     * @param fechaBaja the fechaBaja to set
     */
    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    /**
     * @return the tramWSCod
     */
    public String getTramWSCod() {
        return tramWSCod;
    }

    /**
     * @param tramWSCod the tramWSCod to set
     */
    public void setTramWSCod(String tramWSCod) {
        this.tramWSCod = tramWSCod;
    }

    /**
     * @return the tramWSOb
     */
    public String getTramWSOb() {
        return tramWSOb;
    }

    /**
     * @param tramWSOb the tramWSOb to set
     */
    public void setTramWSOb(String tramWSOb) {
        this.tramWSOb = tramWSOb;
    }

    /**
     * @return the tramWSTCod
     */
    public String getTramWSTCod() {
        return tramWSTCod;
    }

    /**
     * @param tramWSTCod the tramWSTCod to set
     */
    public void setTramWSTCod(String tramWSTCod) {
        this.tramWSTCod = tramWSTCod;
    }

    /**
     * @return the tramWSTOb
     */
    public String getTramWSTOb() {
        return tramWSTOb;
    }

    /**
     * @param tramWSTOb the tramWSTOb to set
     */
    public void setTramWSTOb(String tramWSTOb) {
        this.tramWSTOb = tramWSTOb;
    }

    /**
     * @return the tramWSRCod
     */
    public String getTramWSRCod() {
        return tramWSRCod;
    }

    /**
     * @param tramWSRCod the tramWSRCod to set
     */
    public void setTramWSRCod(String tramWSRCod) {
        this.tramWSRCod = tramWSRCod;
    }

    /**
     * @return the tramWSROb
     */
    public String getTramWSROb() {
        return tramWSROb;
    }

    /**
     * @param tramWSROb the tramWSROb to set
     */
    public void setTramWSROb(String tramWSROb) {
        this.tramWSROb = tramWSROb;
    }

    public byte[] getByteArrayPlantillaTramitacion() {
        return byteArrayPlantillaTramitacion;
    }

    public void setByteArrayPlantillaTramitacion(byte[] byteArrayPlantillaTramitacion) {
        this.byteArrayPlantillaTramitacion = byteArrayPlantillaTramitacion;
    }
    
    /**
     * @return the tramiteNotificado
     */
    public boolean isTramiteNotificado() {
        return tramiteNotificado;
    }

    /**
     * @param tramiteNotificado the tramiteNotificado to set
     */
    public void setTramiteNotificado(boolean tramiteNotificado) {
        this.tramiteNotificado = tramiteNotificado;
    }

    public Integer getFirmaDocumentoIdUsuario() {
        return firmaDocumentoIdUsuario;
    }

    public void setFirmaDocumentoIdUsuario(Integer firmaDocumentoIdUsuario) {
        this.firmaDocumentoIdUsuario = firmaDocumentoIdUsuario;
    }

    public String getFirmaDocumentoLogUsuario() {
        return firmaDocumentoLogUsuario;
    }

    public void setFirmaDocumentoUsuarioLog(String firmaDocumentoLogUsuario) {
        this.firmaDocumentoLogUsuario = firmaDocumentoLogUsuario;
    }

    public FirmaFlujoVO getFirmaFlujo() {
        return firmaFlujo;
    }

    public void setFirmaFlujo(FirmaFlujoVO firmaFlujo) {
        this.firmaFlujo = firmaFlujo;
    }

    public FirmaCircuitoVO getFirmaCircuito() {
        return firmaCircuito;
    }

    public void setFirmaCircuito(FirmaCircuitoVO firmaCircuito) {
        this.firmaCircuito = firmaCircuito;
    }
    
    
    public String getDescripcionDepartamentoNotificacion() {
        return descripcionDepartamentoNotificacion;
    }

    public void setDescripcionDepartamentoNotificacion(String descripcionDepartamentoNotificacion) {
        this.descripcionDepartamentoNotificacion = descripcionDepartamentoNotificacion;
    }

    public Boolean getExisteDepartamento() {
        return existeDepartamento;
    }

    public void setExisteDepartamento(Boolean existeDepartamento) {
        this.existeDepartamento = existeDepartamento;
    }
    
    
}