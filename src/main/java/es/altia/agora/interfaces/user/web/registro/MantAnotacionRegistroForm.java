package es.altia.agora.interfaces.user.web.registro;

import es.altia.agora.business.registro.RegistroValueObject; 
import es.altia.agora.business.registro.SimpleRegistroValueObject;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.mantenimiento.HojaArbolClasifAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import es.altia.arboles.impl.ArbolImpl;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collection;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Clase para dar de alta anotaciones en el registro, tanto de entrada como de salida */

public class MantAnotacionRegistroForm extends ActionForm {

    //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Log m_Log =
            LogFactory.getLog(AperturaCierreRegistroForm.class.getName());

    RegistroValueObject laAnotacionVO = new RegistroValueObject();
    MantAsuntosValueObject asuntoVO = new MantAsuntosValueObject();

    // =====================
    Vector listaNuevasUORs = new Vector();
    Vector listaInteresados=new Vector();
    Vector listaRoles=new Vector();
    Vector listaPlantillaJustificantes=new Vector();
    ArbolImpl arbol;
    Vector listaInteresadosSoloRegistro=new Vector();  //Lista de interesados que van a estar en registro y no en expediente (al adjuntar un registro)
    
    //Arbol de clasificación de asuntos
    private ArrayList<HojaArbolClasifAsuntosValueObject> arbolClasifAsuntos; 
    //Los hijos del arbol, como un arrayList de vectores
    private ArrayList<Vector<MantAsuntosValueObject>> hijosArbolClasifAsuntos;

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

    public RegistroValueObject getRegistro() {
        return laAnotacionVO;
    }

    public void setRegistro(RegistroValueObject registroVO) {
        if (registroVO == null) this.laAnotacionVO = new RegistroValueObject();
        else this.laAnotacionVO = registroVO;
    }
    
    //para crear justificantes registro
    
     public Vector getListaPlantillasJustificantes() {
        return listaPlantillaJustificantes;
    }

    public void setListaPlantillasJustificantes(Vector lista) {
        this.listaPlantillaJustificantes = lista;
    }
    
   public void setDatosXML(String datos) {this.datosXML = datos;}
   public String getDatosXML() { return(this.datosXML); }
   
   public void setRegNum(String datos) {this.regNum = datos;}
   public String getRegNum() { return(this.regNum); }
   
   public void setRegUor(String datos) {this.regOur = datos;}
   public String getRegUor() { return(this.regOur); }
   
   public void setRegTip(String datos) {this.regTip = datos;}
   public String getRegTip() { return(this.regTip); }
   
    public void setCodPlantilla(String datos) {this.codPlantilla = datos;}
   public String getCodPlantilla() { return(this.codPlantilla); }
   
   //para recuperar los campos que tienen que aparecer en el listado
    public Vector getCamposListados() {
         return laAnotacionVO.getCamposListados();
    }
   
    public void setCamposListados(Vector newLista) {
        laAnotacionVO.setCamposListados(newLista);
    }
   
    public Map getCampos() {
        return campos;
    }

    public void setCampos(Map campos) {
        this.campos = campos;
    }
    
    
   
   
//fin justificante
    // =====================
/*
    //Create es el action por defecto
    private String action = "create";

    public String getAction() {
    return action;
    }

    public void setAction(String action) {
    this.action = action;
    }
*/


    /* Seccion donde metemos los metods get y set de los campos del formulario */

    public boolean getProcesarBuzon(){
        return laAnotacionVO.getHayBuzon();
    }

    public void setProcesarBuzon(boolean buzon){
        laAnotacionVO.setHayBuzon(buzon);
    }

    public String getEjercicioAnotacion() {
        return new Integer(laAnotacionVO.getAnoReg()).toString();
    }
    public String getNumeroAnotacion() {
        
        return new Long(laAnotacionVO.getNumReg()).toString();
    }

    // Fecha documento.
    public String getDia_doc(){
        diaDoc = laAnotacionVO.getDiaDoc();
        return diaDoc;
    }
    public String getMes_doc(){
        mesDoc = laAnotacionVO.getMesDoc();
        return mesDoc;
    }
    public String getAno_doc(){
        anoDoc = laAnotacionVO.getAnoDoc();
        return anoDoc;
    }
    public String getTxtHoraDoc() {
        horaDoc = laAnotacionVO.getTxtHoraDoc();
        return horaDoc;
    }
    public String getTxtMinDoc() {
        minDoc = laAnotacionVO.getTxtMinDoc();
        return minDoc;
    }
    public void setDia_doc(String dia){
        diaDoc = dia;
    }
    public void setMes_doc(String mes){
        mesDoc = mes;
    }
    public void setAno_doc(String ano){
        anoDoc = ano;
    }
    public void setTxtHoraDoc(String param) {
        horaDoc=param;
    }
    public void setTxtMinDoc(String param) {
        minDoc=param;
    }

 // FECHA del documento 
  public void setFechaDoc(String fechaDoc) {  laAnotacionVO.setFecEntrada(fechaDoc);}
   public String getFechaDoc() { 
    String f =laAnotacionVO.getFechaDocu();
        if (f!=null && f.length() >= 10) return f.substring(0,10);
        else return "";
   }
   
   
   
   

    // FECHA COMPLETA
    public String getFechaAnotacion(){
        String f =laAnotacionVO.getFecEntrada();
        if (f!=null && f.length() >= 10) return f.substring(0,10);
        else return "";
    }

    public void setFechaAnotacion(String fecha){
        laAnotacionVO.setFecEntrada(fecha);
    }

    public String getFechaDocumento(){
        String f =laAnotacionVO.getFecHoraDoc();
        if (f!=null && f.length()>=10) return f.substring(0,10);
        else return "";
    }

    public void setFechaDocumento(String fecha){
        laAnotacionVO.setFecHoraDoc(fecha);
    }

    public void setHoraMinDocumento(String horaMin) {
        // No hace nada
    }

    public String getHoraMinDocumento() {
        String hm = laAnotacionVO.getHoraMinDocumento();
        if (hm != null)
            return hm;
        else return "";
    }

    // Fecha anotacion (= fecha en q. está abierto el registro) .

    public String getDiaAnotacion(){
        diaAnotacion = laAnotacionVO.getDiaAnotacion();
        if(m_Log.isDebugEnabled()) m_Log.debug("el dia de anotacion es : " + diaAnotacion);
        return diaAnotacion;
    }
    public String getMesAnotacion(){
        mesAnotacion = laAnotacionVO.getMesAnotacion();
        return mesAnotacion;
    }
    public String getAnoAnotacion(){
        anoAnotacion = laAnotacionVO.getAnoAnotacion();
        return anoAnotacion;
    }
    public void setDiaAnotacion(String dia){
        diaAnotacion = dia;
    }
    public void setMesAnotacion(String mes){
        mesAnotacion = mes;
    }
    public void setAnoAnotacion(String ano){
        anoAnotacion = ano;
    }

    public String getTxtHoraEnt() {
        return laAnotacionVO.getHoraEnt();
    }

    public void setTxtHoraEnt(String param) {
        txtHoraEnt=param;
    }

    public String getTxtMinEnt() {
        return laAnotacionVO.getMinEnt();
    }

    public void setTxtMinEnt(String param) {
        txtMinEnt=param;
    }
     
     public String getTxtSegEnt() {
        return laAnotacionVO.getSegEnt();
    } 

    public void setHoraMinAnotacion(String horaMin) {
        // No hace nada
    }

    public String getHoraMinAnotacion() {

        String hm = laAnotacionVO.getHoraMinAnotacion();
        if (hm != null)
            return hm;
        else return "";


    }

    // Asunto (extracto)
    public String getAsunto(){
        return laAnotacionVO.getAsunto();

    }
    public void setAsunto(String asunto){
            laAnotacionVO.setAsunto(asunto);
    }
    

    // Tipo documento
    public String getTxtCodigoDocumento(){
        return laAnotacionVO.getCodTipoDoc();
    }
    public String getTxtNomeDocumento(){
        return laAnotacionVO.getDescTipoDoc();
    }
    public void setTxtCodigoDocumento(String codigo){
        laAnotacionVO.setCodTipoDoc(codigo);
    }
    public void setTxtNomeDocumento(String nome){
        if (!"".equals(nome.trim())) laAnotacionVO.setDescTipoDoc(nome);
    }

    // Tipo entrada
    public int getCbTipoEntrada() {
        return laAnotacionVO.getTipoAnot();
    }
    public void setCbTipoEntrada(int id) {
        laAnotacionVO.setTipoAnot(id);
    }

    public String getTxtNomeTipoEntrada() {
        return laAnotacionVO.getTxtNomeTipoEntrada();
    }

    public void setTxtNomeTipoEntrada(String param) {
        laAnotacionVO.setTxtNomeTipoEntrada(param);
    }

    // Tipo transporte
    public String getCod_tipoTransporte(){
        return laAnotacionVO.getCodTipoTransp();
    }
    public String getDesc_tipoTransporte(){
        //return laAnotacionVO.getDescTransporte();
        return laAnotacionVO.getDescTipoTransporte();
    }
    public void setCod_tipoTransporte(String codigo){
        laAnotacionVO.setCodTipoTransp(codigo);
    }
    public void setDesc_tipoTransporte(String nombre){
        //if (!"".equals(nombre))
        laAnotacionVO.setDescTipoTransporte(nombre);
    }

    // Tipo remitente
    public String getCod_tipoRemitente(){
        return laAnotacionVO.getCodTipoRemit();
    }
    public String getTxtNomeTipoRemitente(){
        return laAnotacionVO.getDescTipoRem();
    }
    public void setCod_tipoRemitente(String codigo){
        laAnotacionVO.setCodTipoRemit(codigo);

    }
    public void setTxtNomeTipoRemitente(String nome){
        if (!"".equals(nome.trim())) laAnotacionVO.setDescTipoRem(nome);
    }

    // Tipo actuacion
    public String getCod_actuacion(){
        if (laAnotacionVO.getCodAct()!=null)
            return laAnotacionVO.getCodAct();
        else return "";
    }
    public String getTxtNomeActuacion(){
        if (laAnotacionVO.getDescActuacion()!= null)
            return laAnotacionVO.getDescActuacion();
        else return "";
    }
    public void setCod_actuacion(String codigo){
        if (!"".equals(codigo.trim())) laAnotacionVO.setCodAct(codigo);
        else laAnotacionVO.setCodAct(null);
        
    }
    public void setTxtNomeActuacion(String nome){
        if (!"".equals(nome.trim()))
            laAnotacionVO.setDescActuacion(nome);
    }

    // Numero transporte
    public String getTxtNumTransp(){
        return laAnotacionVO.getNumTransporte();
    }
    public void setTxtNumTransp(String num){
        if (!"".equals(num))
            laAnotacionVO.setNumTransporte(num);
    }

    // Organizacion Destino
    public String getCod_orgDestino(){
        if(laAnotacionVO.getOrgDestino()==null) return "";
        else return laAnotacionVO.getOrgDestino();
    }
    public void setCod_orgDestino(String codigo){
        if (!"".equals(codigo))
            laAnotacionVO.setOrgDestino(codigo);
    }

    public String getDesc_orgDestino () {
        return laAnotacionVO.getNomOrganizacionDestino();
    }

    public void setDesc_orgDestino (String param) {
        laAnotacionVO.setNomOrganizacionDestino(param);
    }

    // Entidad Destino
    public String getCod_entDestino(){
        if(laAnotacionVO.getOrgEntDest()==null) return "";
        else return laAnotacionVO.getOrgEntDest();
    }
    public void setCod_entDestino(String codigo){
        if (!"".equals(codigo))
            laAnotacionVO.setOrgEntDest(codigo);
    }

    public String getDesc_entDestino() {
        return laAnotacionVO.getNomEntidadDestino();
    }

    public void setDesc_entDestino(String param) {
        laAnotacionVO.setNomEntidadDestino(param);
    }

    // Dpto. Destino
    public String getCod_dptoDestino(){
        if(laAnotacionVO.getIdDepDestino()==null) return "";
        else return laAnotacionVO.getIdDepDestino();
    }
    public String getDesc_dptoDestino(){
        return laAnotacionVO.getDesc_dptoDestino();
    }
    public void setCod_dptoDestino(String codigo){
        if (!"".equals(codigo))
            laAnotacionVO.setIdDepDestino(codigo);
    }
    public void setDesc_dptoDestino(String nombre){
        if (!"".equals(nombre))
            laAnotacionVO.setDesc_dptoDestino(nombre);
    }

    // Unidad Registro Destino
    public String getCod_uniRegDestino(){
        if(laAnotacionVO.getIdUndRegDest()==null) return "";
        else return laAnotacionVO.getIdUndRegDest();
    }
    public String getDesc_uniRegDestino(){
        return laAnotacionVO.getDesc_uniRegDestino();
    }
    public void setCod_uniRegDestino(String codigo){
        if (!"".equals(codigo))
            laAnotacionVO.setIdUndRegDest(codigo);
    }
    public void setDesc_uniRegDestino(String nombre){
        if (!"".equals(nombre))
            laAnotacionVO.setDesc_uniRegDestino(nombre);
    }


    // Unidad tramitadora (pestaña destino)
    // Unidad tramitadora (pestaña destino)
    public String getCod_unidadTramitadora(){
/*    if (laAnotacionVO.getIdUndTramitad()!=0)
      return new Integer(laAnotacionVO.getIdUndTramitad()).toString();
    else return "";
*/
        if(laAnotacionVO.getIdUndTramitad()==null) return "";
        else return laAnotacionVO.getIdUndTramitad();
    }
    public String getDesc_unidadTramitadora(){
        //return laAnotacionVO.getDesc_?();
        return "";
    }
    public void setCod_unidadTramitadora(String codigo){
        if (!"".equals(codigo))
            //laAnotacionVO.setIdUndTramitad(Integer.parseInt(codigo));
            laAnotacionVO.setIdUndTramitad(codigo);
    }
    public void setDesc_unidadTramitadora(String nombre){
        //if (!"".equals(nombre))
        //	laAnotacionVO.setDesc_?(nombre);
    }


    // Expediente relacionado
    public String getTxtExp1(){
        return laAnotacionVO.getNumExpediente();
    }

    public void setTxtExp1(String num){
        laAnotacionVO.setNumExpediente(num);
    }


    //Organizacion origen
    public String getCod_orgOrigen() {
        return laAnotacionVO.getOrganizacionOrigen();
    }
    public void setCod_orgOrigen(String param) {
        laAnotacionVO.setOrganizacionOrigen(param);
    }

    public String getDesc_orgOrigen() {
        return laAnotacionVO.getNomOrganizacionOrigen();
    }

    public void setDesc_orgOrigen(String param) {
        laAnotacionVO.setNomOrganizacionOrigen(param);
    }

    //Entidad origen
    public String getCod_entidadOrigen() {
        if(laAnotacionVO.getOrgEntOrigen()==0) {
            return "";
        } else {
            return new Integer(laAnotacionVO.getOrgEntOrigen()).toString();
        }
    }
    public void setCod_entidadOrigen(String param) {
        if (!"".equals(param))
            laAnotacionVO.setOrgEntOrigen(Integer.parseInt(param));
    }

    public String getDesc_entidadOrigen() {
        return laAnotacionVO.getNomEntidadOrigen();
    }

    public void setDesc_entidadOrigen(String param) {
        laAnotacionVO.setNomEntidadOrigen(param);
    }


    // Dpto. Orixe
    public String getCod_departamentoOrixe(){
        if(laAnotacionVO.getIdDepOrigen()==0) {
            return "";
        } else {
            return new Integer(laAnotacionVO.getIdDepOrigen()).toString();
        }
    }
    public String getDesc_departamentoOrixe(){
        return laAnotacionVO.getDesc_dptoOrigen();
    }
    public void setCod_departamentoOrixe(String codigo){
        if (!"".equals(codigo))
            laAnotacionVO.setIdDepOrigen(Integer.parseInt(codigo));
    }
    public void setDesc_departamentoOrixe(String nombre){
        if (!"".equals(nombre))
            laAnotacionVO.setDesc_dptoOrigen(nombre);
    }

    // Unidad Registro Destino
    public String getCod_unidadeRexistroOrixe(){
        return laAnotacionVO.getIdUndRegOrigen();
    }
    public String getDesc_unidadeRexistroOrixe(){
        return laAnotacionVO.getDesc_uniRegOrigen();
    }
    public void setCod_unidadeRexistroOrixe(String codigo){
        laAnotacionVO.setIdUndRegOrigen(codigo);
    }
    public void setDesc_unidadeRexistroOrixe(String nombre){
        if (!"".equals(nombre))
            laAnotacionVO.setDesc_uniRegOrigen(nombre);
    }

    //tipo registro origen
    public String getTipoRegistroOrigen(){
        return laAnotacionVO.getTipoRegOrigen();
    }

    public void setTipoRegistroOrigen(String tro){
        laAnotacionVO.setTipoRegOrigen(tro);
    }

    // Expediente relacionado
    public String getTxtExp2Orixe(){
        return laAnotacionVO.getNumEntradaRel();
    }
    public String getTxtExp1Orixe(){
        return laAnotacionVO.getEjeEntradaRel();
    }
    public void setTxtExp2Orixe(String no){
        if(m_Log.isDebugEnabled()) m_Log.debug("en el form el numero de la entrada relacionada es : ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ " + no);
        laAnotacionVO.setNumEntradaRel(no);
    }
    public void setTxtExp1Orixe(String e){
        if(m_Log.isDebugEnabled()) m_Log.debug("en el form el anho de la entrada relacionada es : ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ " + e);
        laAnotacionVO.setEjeEntradaRel(e);
    }

    // Terceros
    public String getCodTerc() {
        return new Integer( laAnotacionVO.getCodInter()).toString();
    }
    public void setCodTerc(String param) {
        if(m_Log.isDebugEnabled()) m_Log.debug(param);
        if (!"".equals(param.trim()))
            laAnotacionVO.setCodInter(Integer.parseInt(param));
    }
    public String getCodDomTerc() {
        return  new Integer( laAnotacionVO.getDomicInter()).toString();

    }
    public void setCodDomTerc(String param) {
        if(m_Log.isDebugEnabled()) m_Log.debug(param);
        if (!"".equals(param.trim()))
            laAnotacionVO.setDomicInter(Integer.parseInt(param));

    }
    public String getNumModifTerc() {
        return  new Integer( laAnotacionVO.getNumModInfInt()).toString();
    }
    public void setNumModifTerc(String param) {
        if (!"".equals(param))
            laAnotacionVO.setNumModInfInt(Integer.parseInt(param));
    }

    // Estado anotacion.
    public String getEstadoAnotacion() {
        if (laAnotacionVO.getEstAnotacion()==0)
            return "";
        else return new Integer( laAnotacionVO.getEstAnotacion()).toString();
    }
    public void setEstadoAnotacion(String param) {
        if (!"".equals(param.trim()))
            laAnotacionVO.setEstAnotacion(Integer.parseInt(param));

    }

    // Diligencias de anulación.
    public String getTxtDiligenciasAnulacion() {
        if (laAnotacionVO.getDilAnulacion() != null)
            return laAnotacionVO.getDilAnulacion();
        else return "";
    }
    public void setTxtDiligenciasAnulacion(String param) {
        if (!"".equals(param))
            laAnotacionVO.setDilAnulacion(param);
    }

    // Anotacion que contesta.
    public String getNumeroAnotacionContestada() {
        if (laAnotacionVO.getNumeroAnotacionContestada() != null)
            return laAnotacionVO.getNumeroAnotacionContestada();
        else return "";
    }
    public void setNumeroAnotacionContestada(String param) {
        if (!"".equals(param))
            laAnotacionVO.setNumeroAnotacionContestada(param);
    }
    public String getEjercicioAnotacionContestada() {
        if (laAnotacionVO.getNumeroAnotacionContestada() != null)
            return laAnotacionVO.getEjercicioAnotacionContestada();
        else return "";
    }
    public void setEjercicioAnotacionContestada(String param) {
        if (!"".equals(param))
            laAnotacionVO.setEjercicioAnotacionContestada(param);
    }
    
    
    //LISTA INTERESADOS PARA JUSTIFICANTE
     	
  public Vector getListaInteresados() { return listaInteresados; }
  public void setListaInteresados(Vector listaInteresados) { 
	  this.listaInteresados = listaInteresados; 
	  }
  
    public Vector getListaRoles() { return listaRoles; }
  public void setListaRoles(Vector listaRoles) { 
	  this.listaRoles = listaRoles; 
	  }

    public Vector getListaInteresadosSoloRegistro() {
        return listaInteresadosSoloRegistro;
    }

    public void setListaInteresadosSoloRegistro(Vector listaInteresadosSoloRegistro) {
        this.listaInteresadosSoloRegistro = listaInteresadosSoloRegistro;
    }
  

    // Listas de valores
     // Tipos documentos Alta 	
   public Vector getListaTiposDocumentosAlta() {	
        return (Vector) laAnotacionVO.getListaTiposDocumentosAlta();	
    }
    // 1. Tipos documentos.
    public Vector getListaTiposDocumentos() {
        return (Vector) laAnotacionVO.getListaTiposDocumentos();
    }
    // 2. Tipos remitentes
    public Vector getListaTiposRemitentes() {
        return (Vector) laAnotacionVO.getListaTiposRemitentes();
    }
    // 3. Tipos transportes
    public Vector getListaTiposTransportes() {
        return (Vector) laAnotacionVO.getListaTiposTransportes();
    }
    // 4. Actuaciones
    public Vector getListaActuaciones() {
        return (Vector) laAnotacionVO.getListaActuaciones();
    }
    // 5. Departamentos
    public Vector getListaDepartamentos() {
        return (Vector) laAnotacionVO.getListaDepartamentos();
    }
    // 6. Temas
    public Vector getListaTemas() {
        return (Vector) laAnotacionVO.getListaTemas();
    }
    // 7. Temas
    public Vector getListaTiposIdInteresado() {
        return (Vector) laAnotacionVO.getListaTiposIdInteresado();
    }

    // 8. Temas Asignados
    public Vector getListaTemasAsignados() {
        return laAnotacionVO.getListaTemasAsignados();
    }

    public void setListaTemasAsignados(Vector param) {
        laAnotacionVO.setListaTemasAsignados(param);
    }

    // 8. Docs Asignados
    public Vector getListaDocsAsignados() {
        return laAnotacionVO.getListaDocsAsignados();
    }

    public void setListaDocsAsignados(Vector param) {
        laAnotacionVO.setListaDocsAsignados(param);
    }
    
    public Vector getListaDocsAnteriores(){	
        return laAnotacionVO.getListaDocsAnteriores();	
    }	
    public void setListaDocsAnteriores(Vector param){	
        laAnotacionVO.setListaDocsAnteriores(param);	
    }

    public void setListaProcedimientos(Vector lista) {
        laAnotacionVO.setListaProcedimientos(lista);
    }
    public Vector getListaProcedimientos() {
        return laAnotacionVO.getListaProcedimientos();
    }

    //	9. Departamentos
    public Vector getListaOrganizacionesExternas() {
        return (Vector) laAnotacionVO.getListaOrganizacionesExternas();
    }
    
    // 10. Asuntos
    public Vector<MantAsuntosValueObject> getListaAsuntos() {
        return laAnotacionVO.getListaAsuntos();
    }

    public String getRespOpcion() {
        respOpcion = (String) laAnotacionVO.getRespOpcion();
        return respOpcion;
    }
    
    public String getRespOpcionForm() {
        return respOpcion;
    }
    
    public void setRespOpcion(String param) {
        respOpcion = param;
    }
    
    // Fecha que no se debe sobrepasar al comprobar fechas anterior y posterior.
    public String getFechaComprobacion() {
        return laAnotacionVO.getFechaComprobacion();
    }
    public void setFechaComprobacion(String param) {
        laAnotacionVO.setFechaComprobacion(param);
    }    

    //	9. Formularios asociados
    public Collection getListaFormulariosAsociados() {
        return (Collection) laAnotacionVO.getListaFormulariosAsociados();
    }
    public void setListaFormulariosAsociados(Collection lista) {
         laAnotacionVO.setListaFormulariosAsociados(lista);
    }

    public Vector getListaFormulariosAnexos() {
        return (Vector) laAnotacionVO.getListaFormulariosAnexos();
    }
    public void setListaFormulariosAnexos(Vector lista) {
         laAnotacionVO.setListaFormulariosAnexos(lista);
    }

    public String getProcedoRelaciones() {
        return relaciones;
    }

    public void setProcedoRelaciones(String relaciones) {
        this.relaciones = relaciones;
    }
        
    //Fallo

    public String getFallo() {
        return laAnotacionVO.getFallo();
    }

    public void setFallo(String param) {
        laAnotacionVO.setFallo(param);
    }

    // Contador

    public int getContador(){
        return laAnotacionVO.getContador();
    }

    public void setContador(int param) {
        laAnotacionVO.setContador(param);
    }



    // Campos que faltan.
    public String getAno(){
        m_Log.debug("dentro del getAno en el Form" + laAnotacionVO.getAnoReg());
        if (laAnotacionVO.getAnoReg()==0)
            return "";
        else return new Integer(laAnotacionVO.getAnoReg()).toString();
    }
    public void setAno(String s) {
    }
    public String getNumero(){                
        if (laAnotacionVO.getNumReg()==0)
            return "";
        else return new Long(laAnotacionVO.getNumReg()).toString();
    }
    public void setNumero(String s) {
    }

    public String getCod_tema(){
        return "";
    }
    public void setCod_tema(String s){
    }

    public String getTxtNomeTema(){
        return "";
    }
    public void setTxtNomeTema(String s){
    }

    public String getCod_procedimiento(){
        return laAnotacionVO.getCodProcedimiento();
    }
    public void setCod_procedimiento(String param){
        laAnotacionVO.setCodProcedimiento(param);
    }
    
    // Valor de procedimiento que se debe usar para recuperar roles del asiento.
    public String getCodProcedimientoRoles(){
        return laAnotacionVO.getCodProcedimientoRoles();
    }
    public void setCodProcedimientoRoles(String param){
        laAnotacionVO.setCodProcedimientoRoles(param);
    }
    
    public String getDesc_procedimiento(){
        return laAnotacionVO.getDescProcedimiento();
    }
    public void setDesc_procedimiento(String param){
        laAnotacionVO.setDescProcedimiento(param);
    }
    
    public String getMun_procedimiento() {
        return laAnotacionVO.getMunProcedimiento();
    }
    
    public void setMun_procedimiento(String munProcedimiento) {
        laAnotacionVO.setMunProcedimiento(munProcedimiento);
    }

    // Capa interesado, página altaRE.jsp
/*    public int getCbTipoDoc(){
        // return _cbTipoDoc;
    return laAnotacionVO.getTipoDocInteresado();
    }
    public void setCbTipoDoc(int id) {
      //_cbTipoDoc = id;
    laAnotacionVO.setTipoDocInteresado(id);
    }
*/
    public String getCbTipoDoc(){
        // return _cbTipoDoc;
        return laAnotacionVO.getTipoDocInteresado();
    }
    public void setCbTipoDoc(String id) {
        //_cbTipoDoc = id;
        laAnotacionVO.setTipoDocInteresado(id);
    }

    public String getTxtDNI(){
        return _txtDNI;
    }
    public void setTxtDNI(String s){
        _txtDNI=s;
    }
    public void setTxtInteresado(String s){
        laAnotacionVO.setTxtInteresado(s);
        _txtInteresado=s;
    }
    public String getTxtInteresado(){
        return _txtInteresado;
    }
    public void setTxtPart(String s){
        _txtPart = s;
    }
    public String getTxtPart(){
        return _txtPart;
    }
    public void setTxtApell1(String s){
        _txtApell1 = s;
    }
    public String getTxtApell1(){
        return _txtApell1;
    }
    public void setTxtPart2(String s){
        _txtPart2 = s;
    }
    public String getTxtPart2(){
        return _txtPart;
    }
    public void setTxtApell2(String s){
        _txtApell2 = s;
    }
    public String getTxtApell2(){
        return _txtApell2;
    }
    public void setTxtCorreo(String s){
        _txtCorreo =s;
    }
    public String getTxtCorreo(){
        return _txtCorreo;
    }
    public void setTxtTelefono(String s){
        _txtTelefono=s;
    }
    public String getTxtTelefono(){
        return _txtTelefono;
    }
    public void setTxtPais(String s){
        _txtPais=s;
    }
    public String getTxtPais(){
        return _txtPais;
    }
    public void setTxtProv(String s){
        _txtProv=s;
    }
    public String getTxtProv(){
        return _txtProv;
    }

    public void setTxtMuni(String s){
        _txtMuni = s;
    }
    public String getTxtMuni(){
        return _txtMuni;
    }
    public void setTxtDomicilio(String s){
        _txtDomicilio=s;
    }
    public String getTxtDomicilio(){
        return _txtDomicilio;
    }
    public void setTxtPoblacion(String s){
        _txtPoblacion=s;
    }
    public String getTxtPoblacion(){
        return _txtPoblacion;
    }
    public void setTxtCP(String s){
        _txtCP=s;
    }
    public String getTxtCP(){
        return _txtCP;
    }

    // DESTINO ANOTACION ORDINARIA

    // Dpto. Destino
    public String getCod_dptoDestinoORD(){
        if (laAnotacionVO.getIdDepartemento()==null) return "";
        else return laAnotacionVO.getIdDepartemento();
    }

    public String getDesc_dptoDestinoORD(){
        if (laAnotacionVO.getNomDptoDestinoOrd() != null)
            return laAnotacionVO.getNomDptoDestinoOrd();
        else return "";
    }

    public void setCod_dptoDestinoORD(String codigo){
        if (!"".equals(codigo))
            laAnotacionVO.setIdDepartemento(codigo);
        else laAnotacionVO.setIdDepartemento(null);
    }

    public void setDesc_dptoDestinoORD(String nombre){
        if (!"".equals(nombre))
            laAnotacionVO.setNomDptoDestinoOrd(nombre);
    }

    // Unidad Registro Destino
    // codigo de la UOR
    public String getCod_uniRegDestinoORD(){
        if(laAnotacionVO.getIdUndTramitad()==null) return "";
        else return laAnotacionVO.getIdUndTramitad();
    }

    public void setCod_uniRegDestinoORD(String codigo){
        //if (!"".equals(codigo)) Dedalo tarea 718
            laAnotacionVO.setIdUndTramitad(codigo);
    }

    // codigo visible
    public String getCod_uor() {
        if(laAnotacionVO.getUorCodVisible() == null) return "";
        else return laAnotacionVO.getUorCodVisible();
    }

    public void setCod_uor(String s) {
        laAnotacionVO.setUorCodVisible(s);
    }

    // nombre UOR
    public String getDesc_uniRegDestinoORD(){
        if (laAnotacionVO.getNomUniDestinoOrd() != null)
            return laAnotacionVO.getNomUniDestinoOrd();
        else return "";
    }


    public void setDesc_uniRegDestinoORD(String nombre){
        if (!"".equals(nombre))
            laAnotacionVO.setNomUniDestinoOrd(nombre);
    }

    // Para saber si la fecha de entrada es la misma que la fecha del registro

    public String getAbiertCerrado() {
        return laAnotacionVO.getAbiertCerrado();
    }

    public void setAbiertCerrado(String param) {
        laAnotacionVO.setAbiertCerrado(param);
    }

    // Para el texto de la Diligencia

    public String getTextoDiligencia() {
        if (laAnotacionVO.getTextoDiligencia() != null)
            return laAnotacionVO.getTextoDiligencia();
        else return "";
    }

    public void setTextoDiligencia(String param) {
        if (!"".equals(param))
            laAnotacionVO.setTextoDiligencia(param);
    }

    // Para saber si hay texto en la Diligencia

    public String getHayTexto() {
        return laAnotacionVO.getHayTexto();
    }

    public void setHayTexto(String param) {
        laAnotacionVO.setHayTexto(param);
    }

    public String getPaginaListado() {
        if (laAnotacionVO.getPaginaListado()!=null)
            return laAnotacionVO.getPaginaListado();
        else return "";
    }

    public void setPaginaListado(String param) {
        laAnotacionVO.setPaginaListado(param);
    }

    public String getNumLineasPaginaListado() {
        if (laAnotacionVO.getNumLineasPaginaListado()!=null)
            return laAnotacionVO.getNumLineasPaginaListado();
        else return "";
    }

    public void setNumLineasPaginaListado(String param) {
        laAnotacionVO.setNumLineasPaginaListado(param);
    }

    public String getPosicionAnotacion() {
        if (laAnotacionVO.getPosicionAnotacion()!=null)
            return laAnotacionVO.getPosicionAnotacion();
        else return "";
    }

    public void setPosicionAnotacion(String param) {
        laAnotacionVO.setPosicionAnotacion(param);
    }

    public String getIdentDepart() {
        return Integer.toString(laAnotacionVO.getIdentDepart());
    }

    public String getUnidadOrgan() {
        return Integer.toString(laAnotacionVO.getUnidadOrgan());
    }

    public String getTipoReg() {
        return laAnotacionVO.getTipoReg();
    }
    
    public String getDeHistorico() {
        return laAnotacionVO.getDeHistorico();
    }
    public void setDeHistorico(String deHistorico) {
        laAnotacionVO.setDeHistorico(deHistorico);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
        m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //RegistroSaidaValueObject hara el trabajo para nostros ...
        try {
            laAnotacionVO.validate(idioma);
        } catch (ValidationException ve) {
            //Hay errores...
            //Tenemos que traducirlos a formato struts
            errors=validationException(ve,errors);
        }
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
    public void setDescTipoDoc(String descTipoDoc) {
        this.descTipoDoc = descTipoDoc;
    }
    public String getDescTipoDoc() {
        return descTipoDoc;
    }


    public void setFechaHoraServidor(String horaMin) {
        // No hace nada
    }

    public String getFechaHoraServidor() {
        String fhs = laAnotacionVO.getFecEntrada(); // Provisional
        if (fhs != null) return fhs;
        else return "";
    }

    public void setFechaHoraHoy(String hora) {}
    public String getFechaHoraHoy(){
       Calendar calendar = Calendar.getInstance();
       SimpleDateFormat parser =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
       try{
            return parser.format(calendar.getTime());
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }

    }

    public String getObservaciones()
    {
        String o = laAnotacionVO.getObservaciones();
        if (o == null)
            return "";
        else
            return o;
    }

    public void setObservaciones(String dato)
    {
        laAnotacionVO.setObservaciones(dato);
    }
    
    public String getAutoridad(){
        return laAnotacionVO.getAutoridad();

    }
    public void setAutoridad(String dato){
        laAnotacionVO.setAutoridad(dato);
    }

    // Codigo del asunto (asunto codificado)
    public String getCodAsunto(){
        return laAnotacionVO.getCodAsunto();
    }
    
    public void setCodAsunto(String codAsunto) {
        laAnotacionVO.setCodAsunto(codAsunto);   
    }

    public String getDescAsunto() {
        return descAsunto;
    }

    public void setDescAsunto(String descAsunto) {
        this.descAsunto = descAsunto;
    }
      
    public String getUniRegAsunto() {
        return uniRegAsunto;
    }

    public void setUniRegAsunto(String uniRegAsunto) {
        this.uniRegAsunto = uniRegAsunto;
    }
    
    public void setAsuntoVO(MantAsuntosValueObject asuntoVO) {
        this.asuntoVO = asuntoVO;
    }
    
    public MantAsuntosValueObject getAsuntoVO() {
        return this.asuntoVO;
    }
    
    public String getAnoReg() {
        return anoReg;
    }

    public void setAnoReg(String anoReg) {
        this.anoReg = anoReg;
    }

    public String getNumeroReg() {
        return numeroReg;
    }

    public void setNumeroReg(String numeroReg) {
        this.numeroReg = numeroReg;
    }

    public String getCodUor() {
        return codUor;
    }

    public void setCodUor(String codUor) {
        this.codUor = codUor;
    }

    public String getCodDep() {
        return codDep;
    }

    public void setCodDep(String codDep) {
        this.codDep = codDep;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }
    
    // Métodos para relaciones entre asientos
    public Vector<SimpleRegistroValueObject> getRelaciones() {
        return laAnotacionVO.getRelaciones();
    }
    
    public void setRelaciones(Vector<SimpleRegistroValueObject> relaciones) {
        laAnotacionVO.setRelaciones(relaciones);
    }
    
    public String getTxtTiposRelaciones() {
        return null;
    }
    
    // La lista de relaciones en forma de SimpleRegistroValueObject se construye
    // una vez que tenemos las listas de tipos, ejercicios y numeros.
    public void setTxtTiposRelaciones(String txtTiposRelaciones) {
        this.txtTiposRelaciones = txtTiposRelaciones;
        if (this.txtTiposRelaciones != null &&
            this.txtEjerciciosRelaciones != null &&
            this.txtNumerosRelaciones != null) construirListaRelaciones();
    }
    
    public String getTxtEjerciciosRelaciones() {
        return null;
    }
    
    public void setTxtEjerciciosRelaciones(String txtEjerciciosRelaciones) {
        this.txtEjerciciosRelaciones = txtEjerciciosRelaciones;
        if (this.txtTiposRelaciones != null &&
            this.txtEjerciciosRelaciones != null &&
            this.txtNumerosRelaciones != null) construirListaRelaciones();
    }
    
    public String getTxtNumerosRelaciones() {
        return null;
    }
    
    public void setTxtNumerosRelaciones(String txtNumerosRelaciones) {
        this.txtNumerosRelaciones = txtNumerosRelaciones;
        if (this.txtTiposRelaciones != null &&
            this.txtEjerciciosRelaciones != null &&
            this.txtNumerosRelaciones != null) construirListaRelaciones();
    }

    // Construye la lista de asientos relacionados y la asigna al VO
    private void construirListaRelaciones() {
        Vector<SimpleRegistroValueObject> listaRelaciones = new Vector<SimpleRegistroValueObject>();
        if (!txtTiposRelaciones.equals("")) {
            String[] listaTipos = txtTiposRelaciones.split("§¥");
            String[] listaEjercicios = txtEjerciciosRelaciones.split("§¥");
            String[] listaNumeros = txtNumerosRelaciones.split("§¥");        
            for (int i=0; i<listaTipos.length; i++) {
                SimpleRegistroValueObject reg = new SimpleRegistroValueObject();
                reg.setTipo(listaTipos[i]);
                reg.setEjercicio(listaEjercicios[i]);
                reg.setNumero(listaNumeros[i]);
                // Uor y departamento son los mismos que los del asiento actual
                reg.setUor(getUnidadOrgan());
                reg.setDep(getIdentDepart());
                listaRelaciones.add(reg);    
            }
        }
        txtTiposRelaciones = null;
        txtEjerciciosRelaciones = null;
        txtNumerosRelaciones = null;
        this.setRelaciones(listaRelaciones); 
    }
    
    // Métodos para el rol por defecto
    public String getCodRolDefecto() {
        return codRolDefecto;
    }

    public void setCodRolDefecto(String codRolDefecto) {
        this.codRolDefecto = codRolDefecto;
    }

    public String getDescRolDefecto() {
        return descRolDefecto;
    }

    public void setDescRolDefecto(String descRolDefecto) {
        this.descRolDefecto = descRolDefecto;
    }

    public Vector<String> getCodRolesAnteriores() {
        return codRolesAnteriores;
    }

    public void setCodRolesAnteriores(Vector<String> codRolesAnteriores) {
        this.codRolesAnteriores = codRolesAnteriores;
    }

    public Vector<String> getCodRolesNuevos() {
        return codRolesNuevos;
    }

    public void setCodRolesNuevos(Vector<String> codRolesNuevos) {
        this.codRolesNuevos = codRolesNuevos;
    }

    public Vector<String> getDescRolesNuevos() {
        return descRolesNuevos;
    }

    public void setDescRolesNuevos(Vector<String> descRolesNuevos) {
        this.descRolesNuevos = descRolesNuevos;
    }
    
    // Para envio de correo a unidades organicas
    public String getTxtListaUorsCorreo() {
        return asuntoVO.getTxtListaUorsCorreo();
    }

    public void setTxtListaUorsCorreo(String txtListaUorsCorreo) {
        asuntoVO.setTxtListaUorsCorreo(txtListaUorsCorreo);
    }
    
    public boolean isEnviarCorreo() {
        return asuntoVO.isEnviarCorreo();
    }

    public void setEnviarCorreo(boolean enviarCorreo) {
        asuntoVO.setEnviarCorreo(enviarCorreo);
    }
    
    // Para cargar la lista en la jsp, se tranforma el String separado por comas en Vector
    public Vector<String> getListaUorsCorreo() { 
        Vector<String> listaUorsCorreo = new Vector<String>();
        String[] listaTxt = asuntoVO.getTxtListaUorsCorreo().split(",");
        
        // Si la lista estaba vacía, tendremos una array con un elemento: ""
        // en este caso devolvemos el vector vacío.
        if (listaTxt.length == 1) {
            if (listaTxt[0].equals("")) return listaUorsCorreo;
        }
        
        // Se construye el vector
        for (int i=0; i<listaTxt.length; i++) {
            listaUorsCorreo.add(listaTxt[i]);
        }        
        return listaUorsCorreo;
    }
    
    //Usado en tramitación para la jsp 'consultaAsiento'
    public int getNumTerceros() {
        return laAnotacionVO.getNumTerceros();
    }        
    
    public Vector<HistoricoMovimientoValueObject> getMovimientosHistorico() {
        return movimientosHistorico;
    }

    public void setMovimientosHistorico(Vector<HistoricoMovimientoValueObject> movimientosHistorico) {
        this.movimientosHistorico = movimientosHistorico;
    }

    public String getDetallesMovimientoHTML() {
        return detallesMovimientoHTML;
    }

    public void setDetallesMovimientoHTML(String detallesMovimientoHTML) {
        this.detallesMovimientoHTML = detallesMovimientoHTML;
    }
    
    //datos para la ordenacion
      public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getColumna() {
        return columna;
    }
    
   public void setTipoOrden(String tipoOrden) {
        this.tipoOrden = tipoOrden;
    }

    public String getTipoOrden() {
        return tipoOrden;
    }
    
    // Necesario para que Struts reconozca cuando el checkbox no esta seleccionado.
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setEnviarCorreo(false);
        setCbTipoEntrada(0); // Valor por defecto para tipoEntrada

    }
    private int columna;
    private String tipoOrden;
    private String diaDoc;
    private String mesDoc;
    private String anoDoc;
    private String diaAnotacion;
    private String mesAnotacion;
    private String anoAnotacion;
    private String horaDoc;
    private String minDoc;
    private String txtHoraEnt;
    private String txtMinEnt;

    private String respOpcion;

    // Interesado
    private int _cbTipoDoc=1;
    private String _txtDNI="";
    private String _txtInteresado="";
    private String _txtPart="";
    private String _txtApell1="";
    private String _txtPart2="";
    private String _txtApell2="";
    private String _txtCorreo="";
    private String _txtTelefono="";
    private String _txtPais="";
    private String _txtMuni="";
    private String _txtDomicilio="";
    private String _txtPoblacion="";
    private String _txtCP="";
    private String _txtProv="";
    private String descTipoDoc="N.I.F.";
    private String datosXML="";
    private String regNum="";
    private String regOur="";
    private String regTip="";
    private String codPlantilla="";
    private Map campos=new HashMap();

    //Para relaciones entre asientos
    private String txtTiposRelaciones=null;
    private String txtEjerciciosRelaciones=null;
    private String txtNumerosRelaciones=null;
    
    // Para asuntos codificados
    private String codAsunto="";
    private String descAsunto="";
    private String uniRegAsunto="";
    
    private String anoReg;
    private String numeroReg;
    private String codUor;
    private String codDep;
    private String tipoRegistro;
    private String codRolDefecto="";
    private String descRolDefecto="";
    private String fechaDoc;
    
    // Mapeo de roles, usado al cambiar de procedimiento
    private Vector<String> codRolesAnteriores;
    private Vector<String> codRolesNuevos;
    private Vector<String> descRolesNuevos;
    
    // Listado de movimientos historicos de una anotacion
    private Vector<HistoricoMovimientoValueObject> movimientosHistorico;
    private String detallesMovimientoHTML;
    
    private String anoCarga;
    private String numeroCarga;
    private String relaciones;
    private String bloquearFechaHoraPresentacion;
    private String cargadoPluginExpRelacionadosFlexia;

    private int modoAlta;

    //Listado combo EstadoSIR
    private String codEstadoSIR;
    private String descEstadoSIR;

    private boolean registroTelematico = false;
    private boolean registroTelematicoModicable = false;
    
    // #239565: indica si mostrar o no el enlace para generar el modelo_peticion_respuesta según el valor de una propiedad en Registro.properties
    private String mostrarGenerarModelo;
    // #288821: indica si mostrar o no el enlace para generar el justificante de registrado desde el listado de consulta
    private String generarJustificanteConsulta;

    // #234108
    private boolean tipoDocInteresadoOblig = false;
    private boolean bloquearDestino=false;
	
    private boolean bloquearProcedimiento=false;
        
    // Variables para Gestionar llamadas a retramitrar documentos de registro en caso de cambio de procedimiento
    //private String retramitarDocumentosCambioProcedimiento; // 1/0 Indica si Se deben retramitar los documento al modificar una entrada
    //private String retramitarDocumentosDetalleOPeracion;  // Si no se puede retramitrar o falla el proceso, cancelamos la modificacion, y mostramos detalles al usuario en ocultoErrorAlta.jsp

    // #270950
    public boolean isProcedimientoDigitalizacion(){
        return laAnotacionVO.isProcedimientoDigitalizacion();
    }
    public void setProcedimientoDigitalizacion(boolean procedimientoDigitalizacion){
       laAnotacionVO.setProcedimientoDigitalizacion(procedimientoDigitalizacion);
    }

    public boolean isFinDigitalizacion(){
        return laAnotacionVO.isFinDigitalizacion();
    }
            
    public void setFinDigitalizacion(boolean finDigitalizacion){
       laAnotacionVO.setFinDigitalizacion(finDigitalizacion);
    }
    
    public boolean isRegistroTelematico() {
        return registroTelematico;
    }

    public void setRegistroTelematico(boolean registroTelematico) {
        this.registroTelematico = registroTelematico;
    }
    
    public String getAnoCarga() {
        return anoCarga;
    }

    public void setAnoCarga(String anoCarga) {
        this.anoCarga = anoCarga;
    }

    public String getNumeroCarga() {
        return numeroCarga;
    }

    public void setNumeroCarga(String numeroCarga) {
        this.numeroCarga = numeroCarga;
    }
    
    private JSONArray datosGenerales;
    private JSONArray datosTemas;
    private JSONArray datosRelaciones;
    private JSONArray datosDocs;
    private JSONArray datosInteresados;
     private JSONArray datosAnts;

    public void setDatosGenerales(JSONArray datosGenerales) {
        this.datosGenerales = datosGenerales;
    }

    public void setDatosTemas(JSONArray datosTemas) {
        this.datosTemas = datosTemas;
    }

    public void setDatosRelaciones(JSONArray datosRelaciones) {
        this.datosRelaciones = datosRelaciones;
    }

    public void setDatosDocs(JSONArray datosDocs) {
        this.datosDocs = datosDocs;
    }
    
    public void setDatosAnts(JSONArray datosAnt){	
        this.datosAnts=datosAnt;	
    }

    public void setDatosInteresados(JSONArray datosInteresados) {
        this.datosInteresados = datosInteresados;
    }
          
    public String toJSONStringDescripcion() throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("datosGenerales", this.datosGenerales);
        jsonObj.put("datosTemas", this.datosTemas);
        jsonObj.put("datosRelaciones", this.datosRelaciones);
        jsonObj.put("datosDocs", this.datosDocs);
        jsonObj.put("datosInteresados", this.datosInteresados);
        return jsonObj.toString();
    }

    /**
     * @return the bloquearFechaHoraPresentacion
     */
    public String getBloquearFechaHoraPresentacion() {
        return bloquearFechaHoraPresentacion;
    }

    /**
     * @param bloquearFechaHoraPresentacion the bloquearFechaHoraPresentacion to set
     */
    public void setBloquearFechaHoraPresentacion(String bloquearFechaHoraPresentacion) {
        this.bloquearFechaHoraPresentacion = bloquearFechaHoraPresentacion;
    } 

    public int getModoAlta() {
        return modoAlta;
    }

    public void setModoAlta(int modoAlta) {
        this.modoAlta = modoAlta;
    }


    public boolean getAsuntoAnotacionBaja(){
        return this.laAnotacionVO.isAsuntoAnotacionBaja();
    }


    public String getFechaBajaAsuntoCodificadoRegistro(){
        return this.laAnotacionVO.getFechaBajaAsuntoCodificadoRegistro();
    }
    
    private Boolean obligatorioAsuntoCodificado = false;
    
    public Boolean getObligatorioAsuntoCodificado(){
        return this.obligatorioAsuntoCodificado;
    }//isObligatorioAsuntoCodificado
    public void setObligatorioAsuntoCodificado (Boolean obligatorioAsuntoCodificado){
        this.obligatorioAsuntoCodificado = obligatorioAsuntoCodificado;
    }//setObligatorioAsuntoCodificado
    
    private Boolean busquedaAjaxTercero = true;
    public Boolean getBusquedaAjaxTercero() {
        return busquedaAjaxTercero;
    }//getBusquedaAjaxTercero
    public void setBusquedaAjaxTercero(Boolean busquedaAjaxTercero) {
        this.busquedaAjaxTercero = busquedaAjaxTercero;
    }//setBusquedaAjaxTercero
    
    public ArrayList<String> getListaNumExpedientesRelacionados() {
        return laAnotacionVO.getNumExpedientesRelacionados();
    }//getListaNumExpedientesRelacionados
    
    public ArrayList<String> getListaCodProcExpedientesRelacionados(){
        return laAnotacionVO.getCodProcedimientoExpedientesRelacionados();
    }//getListaCodProcExpedientesRelacionados
    
    private Boolean opcionPermanencia = false;
    public Boolean getOpcionPermanencia() {
        return opcionPermanencia;
    }//getOpcionPermanencia
    public void setOpcionPermanencia(Boolean opcionPermanencia) {
        this.opcionPermanencia = opcionPermanencia;
    }//setOpcionPermanencia
    
     /**
     * @return the arbolClasifAsuntos
     */
    public ArrayList<HojaArbolClasifAsuntosValueObject> getArbolClasifAsuntos() {
        return arbolClasifAsuntos;
    }

    /**
     * @param arbolClasifAsuntos the arbolClasifAsuntos to set
     */
    public void setArbolClasifAsuntos(ArrayList<HojaArbolClasifAsuntosValueObject> arbolClasifAsuntos) {
        this.arbolClasifAsuntos = arbolClasifAsuntos;
    }

    
    //Pruebas

    public ArrayList<Vector<MantAsuntosValueObject>> getHijosArbolClasifAsuntos() {
        return hijosArbolClasifAsuntos;
    }

    //Mantenemos esta estructura tab, para pasar a la .jsp, el vector de hijos,de forma más sencilla
    public void setHijosArbolClasifAsuntos(ArrayList<Vector<MantAsuntosValueObject>> hijosArbolClasifAsuntos) {
        this.hijosArbolClasifAsuntos = hijosArbolClasifAsuntos;
    }

 /**
     * @return the mostrarGenerarModelo
     */
    public String getMostrarGenerarModelo() {
        return mostrarGenerarModelo;
    }

    /**
     * @param mostrarGenerarModelo the mostrarGenerarModelo to set
     */
    public void setMostrarGenerarModelo(String mostrarGenerarModelo) {
        this.mostrarGenerarModelo = mostrarGenerarModelo;
    }
    

    /**
     * @return the tipoDocInteresadoOblig
     */
    public boolean isTipoDocInteresadoOblig() {
        return tipoDocInteresadoOblig;
    }

    /**
     * @param tipoDocInteresadoOblig the tipoDocInteresadoOblig to set
     */
    public void setTipoDocInteresadoOblig(boolean tipoDocInteresadoOblig) {
        this.tipoDocInteresadoOblig = tipoDocInteresadoOblig;
    }
    
    
      //para los datos del bloque SGA	
    public String getCodigoSga(){	
        return laAnotacionVO.getCodigoSga();	
    }	
    public void setCodigoSga(String dato){	
        laAnotacionVO.setCodigoSga(dato);	
    }	
    public String getExpedienteSga(){	
        return laAnotacionVO.getExpedienteSga();	
    }	
    public void setExpedienteSga(String dato){	
        laAnotacionVO.setExpedienteSga(dato);
	
    }	
    
    public boolean isBloquearDestino() {	
        return bloquearDestino;	
    }		
    public void setBloquearDestino(boolean bloquearDestino) {	
        this.bloquearDestino = bloquearDestino;	
    }
	
	public boolean isBloquearProcedimiento() {
		return bloquearProcedimiento;
	}

	public void setBloquearProcedimiento(boolean bloquearProcedimiento) {
		this.bloquearProcedimiento = bloquearProcedimiento;
	}
    
/**
     * @return the generarJustificanteConsulta
     */
    public String getGenerarJustificanteConsulta() {
        return generarJustificanteConsulta;
    }

    /**
     * @param generarJustificanteConsulta the generarJustificanteConsulta to set
     */
    public void setGenerarJustificanteConsulta(String generarJustificanteConsulta) {
        this.generarJustificanteConsulta = generarJustificanteConsulta;
    }
    
    /**
     * @return the registroTelematicoModicable
     */
    public boolean isRegistroTelematicoModicable() {
        return registroTelematicoModicable;
    }

    /**
     * @param registroTelematicoModicable the registroTelematicoModicable to set
     */
    public void setRegistroTelematicoModicable(boolean registroTelematicoModicable) {
        this.registroTelematicoModicable = registroTelematicoModicable;
    }

    public String getRetramitarDocumentosCambioProcedimiento() {
        //return retramitarDocumentosCambioProcedimiento;
        if (laAnotacionVO.getRetramitarDocumentosCambioProcedimiento() == null) {
            return "";
        } else {
            return laAnotacionVO.getRetramitarDocumentosCambioProcedimiento();
        }
    }

    public void setRetramitarDocumentosCambioProcedimiento(String retramitarDocumentosCambioProcedimiento) {
        //this.retramitarDocumentosCambioProcedimiento = retramitarDocumentosCambioProcedimiento;
        laAnotacionVO.setRetramitarDocumentosCambioProcedimiento(retramitarDocumentosCambioProcedimiento);
    }

    public String getRetramitarDocumentosDetalleOPeracion() {
        //return retramitarDocumentosDetalleOPeracion;
        if (laAnotacionVO.getRetramitarDocumentosDetalleOPeracion() == null) {
            return "";
        } else {
            return laAnotacionVO.getRetramitarDocumentosDetalleOPeracion();
        }
    }

    public void setRetramitarDocumentosDetalleOPeracion(String retramitarDocumentosDetalleOPeracion) {
        //this.retramitarDocumentosDetalleOPeracion = retramitarDocumentosDetalleOPeracion;
        laAnotacionVO.setRetramitarDocumentosDetalleOPeracion(retramitarDocumentosDetalleOPeracion);
    }
    
    // Codigo Unidad Destino SIR
    public String getCodigoUnidadDestinoSIR() {
        if (laAnotacionVO.getCodigoUnidadDestinoSIR() == null) {
            return "";
        } else {
            return laAnotacionVO.getCodigoUnidadDestinoSIR();
        }
    }

    public void setCodigoUnidadDestinoSIR(String codigo) {
        laAnotacionVO.setCodigoUnidadDestinoSIR(codigo);
    }
    public String getNombreUnidadDestinoSIR() {
        if (laAnotacionVO.getNombreUnidadDestinoSIR() == null) {
            return "";
        } else {
            return laAnotacionVO.getNombreUnidadDestinoSIR();
        }
    }

    public void setNombreUnidadDestinoSIR(String codigo) {
        laAnotacionVO.setNombreUnidadDestinoSIR(codigo);
    }
    
    public boolean getRequiereGestionSIR() {
        return laAnotacionVO.isRequiereGestionSIR();
    }

    public void setRequiereGestionSIR(boolean codigo) {
        laAnotacionVO.setRequiereGestionSIR(codigo);
    }
    
    public String getCodEstadoRespGestionSIR() {
        if (laAnotacionVO.getCodEstadoRespGestionSIR() == null) {
            return "";
        } else {
            return laAnotacionVO.getCodEstadoRespGestionSIR();
        }
    }

    public void setCodEstadoRespGestionSIR(String codigo) {
        laAnotacionVO.setCodEstadoRespGestionSIR(codigo);
    }
    
    public String getDescEstadoRespGestionSIR() {
        if (laAnotacionVO.getDescEstadoRespGestionSIR() == null) {
            return "";
        } else {
            return laAnotacionVO.getDescEstadoRespGestionSIR();
        }
    }

    public void setDescEstadoRespGestionSIR(String codigo) {
        laAnotacionVO.setDescEstadoRespGestionSIR(codigo);
    }


    public String getCodEstadoSIR() {
        return codEstadoSIR;
    }

    public void setCodEstadoSIR(String codEstadoSIR) {
        this.codEstadoSIR = codEstadoSIR;
    }

    public String getDescEstadoSIR() {
        return descEstadoSIR;
    }

    public void setDescEstadoSIR(String descEstadoSIR) {
        this.descEstadoSIR = descEstadoSIR;
    }

    private String identificadorRegistroSIR;

    public String getIdentificadorRegistroSIR() {
        return identificadorRegistroSIR;
    }
    public void setIdentificadorRegistroSIR(String identificadorRegistroSIR) {
        this.identificadorRegistroSIR = identificadorRegistroSIR;
    }

}//class