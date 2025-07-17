// NOMBRE DEL PAQUETE
package es.altia.agora.business.registro;

//PAQUETES QUE SE IMPORTAN
import es.altia.technical.ValueObject;
import es.altia.technical.Messages; 
import es.altia.technical.Message;
import es.altia.technical.ValidationException;
import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.mantenimiento.HojaArbolClasifAsuntosValueObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class RegistroValueObject implements Serializable, ValueObject {

    protected static Log m_Log =
            LogFactory.getLog(RegistroValueObject.class.getName());

    private int idOrganizacion;    // Quién hace la entrada
    private int idEntidad;

    private int identDepart;       //Identificador del departamento  (DEP_COD)
    private int unidadOrgan;       //Identificador de la unidad  (UOR_COD)
    private String uorCodVisible;   // UOR_COD_VIS de la tabla A_UOR

    private String tipoReg;        //Tipo registro  (RES_TIP)
    private long numReg;            //Numero registro  (RES_NUM)
    private int anoReg;            //Año registro  (RES_EJE)
    private int ejercicioAnotacionDuplicadaOrigen;
    private String fecEntrada;       //Fecha entrada  (RES_FEC)
    private String fecHoraDoc;       //Fecha y hora documento  (RES_FED)
    private String fechaDocu;        //Fecha entrada  (RES_FEDOC)
    private String asunto;         //Asunto  (RES_ASU) = extracto
    private String codAsunto;   // Codigo del asunto codificado (ASUNTO) != extracto
    private String descripcionAsunto; //Descripción del asunto != extracto
    private int tipoAnot;          //Tipo anotación  (RES_MOD)
    private String orgEntDest;        //Organizacion/Entidad destino  (RES_ECD)
    // tiruritata pendiente de quitar
    private String idDepDestino;      //Identificador del departamento destino  (RES_DCD)
    private String idUndRegDest;      //Identificador de la unidad de registro de destino  (RES_UCD)
    private String orgDestino;        //Organizacion destino  (RES_OCD)
    private String idDepartamento;    //Identificador del departamento  (RES_DOD)
    private String idUndTramitad;     //Identificador de la unidad tramitadora  (RES_UOD)
    private int orgEntOrigen;      //Organizacion/Entidad origen  (RES_ECO)
    private int idDepOrigen;       //Identificador del departamento origen  (RES_DCO)
    private String idUndRegOrigen;    //Identificador de la unidad de registro origen  (RES_UCO)
    private String organizacionOrigen;  //Unidad origen de la anotación  (RES_OCO)
    private String tipoRegOrigen;  //Tipo registruo origen  (RES_TOR)
    private String ejeOrigen;         //Ejercicio de origen (RES_EOR)
    private String numOrigen;         //Número origen (RES_NOR)
    private int idTransporte;      //Identificador del transporte  (RES_TTR)
    private String numTransporte;  //Número transporte  (RES_NTR)
    private int idTipoDoc;         //Identificador Tipo Documento  (RES_TDO)
    private int idTipoPers;        //Identificador Tipo Persona  (RES_TPE)
    private int domicInter;        //Domicilio interesado  (RES_DOM)
    private int codInter;          //Codigo del interesado  (RES_TER)
    private int numModInfInt;      //Numero de Modificacion de la informacion del Interesado  (RES_TNV)
    private int idActuacion;       //Identificador de actuación  (RES_ACT)
    private int estAnotacion;      //Estado anotación  (RES_EST)
    private Integer estadoSIR;
    private Date fechaEstadoSIR;
    private String identificadorRegistroSIR;
    private String dilAnulacion;   //Diligencia de anulación  (RES_DIL)
    private String numeroAnotacionContestada;
    private String ejercicioAnotacionContestada;
    private String codProcedimiento;
    private String codProcedimientoRoles; // Valor de proc q se debe usar para recuperar los roles.
    private String descProcedimiento = "";
    private String munProcedimiento = "0";
    private String nombreTecnicoReferencia;

    private Vector listaTiposDocumentosAlta;
    private Vector listaTiposDocumentos;
    private Vector listaTiposRemitentes;
    private Vector listaTiposTransportes;
    private Vector listaActuaciones;
    private Vector listaDepartamentos;
    private Vector listaTiposIdInteresado;
    private Vector listaTemas;
    private Vector listaProcedimientos;
    private Vector listaOrganizacionesExternas;
    private Vector<MantAsuntosValueObject> listaAsuntos;
    // Arbol de clasificacion de asuntos.
    private ArrayList<HojaArbolClasifAsuntosValueObject> arbolClasifAsuntos;

    
    //recojo los datos para insertar los interesados.	
    private Vector listaCodTercero;
    private Vector listaVersionTercero;
    private Vector listaCodDomicilio;
    private Vector listaRol;
    
    // Datos de asientos relacionados
    private Vector<SimpleRegistroValueObject> relaciones; 

    //Lista de los Formularios asociados a una entrada
    private Collection listaFormulariosAsociados;

    private Collection listaFormulariosAnexos;

    private Vector listaTemasAsignados; // Lista de códigos asociados a una entrada.
    private Vector listaDocsAsignados; // Lista de códigos asociados a una entrada.
    private Vector listaDocsAnteriores; //Lista doc aportados anteriormente

    
    private long idDocumento;
    private String nombreDoc;
    private String tipoDoc;
    private byte[] doc;
	private String cotejado; // SI -> Es un documento cotejado / NO -> No es un documento cotejado
    private String compulsado; // SI -> Es un documento compulsado / NO -> No es un documento compulsado
    private String catalogado; // SI -> Es un documento catalogado con tipo documental y metadados / NO -> No es un documento catalogado
    private DocumentoMetadatosVO metadatosDoc; // Metadatos de los documentos compulsados.
    //private byte[] docEscaneado;  // En el  caso de recuperar documento del scanner
    //private String tipoDocEscaneado;  // En el  caso de recuperar documento del scanner
    //private String nombreDocEscaneado;  // En el  caso de recuperar documento del scanner
    private boolean docNormal; // Indica si el documento subido se ha subido por POST o se extrae del scanner
    private String entregado; // Indica si el documento subido se ha subido por POST o se extrae del scanner
    private String descripcionTipoDocumental;  //Descripcion tipo documental documentos lanbide

    private String observDoc;
    
    private String codigoTema;
    private String descTema;
    private String txtNomeTipoEntrada;


    private String codTipoDoc;			// Los que conoce el usario: Determina TDO_IDe.
    private String codTipoRemit;		// Determina TPE_IDe
    private String codTipoTransp;		// Determina TTR_IDE
    private String codAct;				// Determina ACT_IDE
    private String codigoDocumento;
    private String fechaDoc; //fecha del documento (R_RED.RED_FEC_DOC)

    private String nomOrganizacionDestino;
    private String nomEntidadDestino;
    private String nomDptoDestino;
    private String nomUniRegDestino;
    private String nomOrganizacionOrigen;
    private String nomEntidadOrigen;
    private String nomDptoOrigen;
    private String nomUniRegOrigen;

    private String respOpcion;         // Estos dos campos son usados para pasar informacion del DAO
    private String fechaComprobacion;  // a la Action y a la jsp y al reves.

    private String descTipoDoc;
    private String descTipoRem;
    private String descTipoTransporte;
    private String descActuacion;

    private String fallo;
    private int contador;
    private String abiertCerrado;
    private String textoDiligencia;
    private String hayTexto;

    private String nomDptoDestinoOrd;
    private String nomUniDestinoOrd;

    private boolean hayBuzon = false;

    /* Fecha: 28/07/2003
     * Enlace con expedientes */
    private String numExpediente;
    private int estadoExpediente;
    private String numExpedienteBD;
    private String codDptoDestBD;
    private String codUORDestBD;

    /* Fecha: 04/09/2003  */
    private String deHistorico;


    private Vector subCamposNombreInteresado;

    private Vector camposListados;

    /** Variable boolean que indica si el estado de la instancia de RegistroEntrada es válido o no */
    private boolean isValid;

    /* Datos de quien registra */
    String usuarioQRegistra;
    String dptoUsuarioQRegistra;
    String unidOrgUsuarioQRegistra;

    String txtInteresado;

    String paginaListado;
    String numLineasPaginaListado;
    String posicionAnotacion;

    private String observaciones;
    private String autoridad; //Indica la autoridad a la que se dirige la anotación (RES_AUT).

    // Info sobre el interesado añadida para integracion con servicios web de registro (23/05/2007 - Jorge Sáenz).
    private String tipoDocInteresado;
    private String documentoInteresado;
    private String nomCompletoInteresado;
    private String tlfInteresado;
    private String emailInteresado;
    private String provInteresado;
    private String munInteresado;
    private String poblInteresado;
    private String cpInteresado;
    private String domCompletoInteresado;
    private String codTerceroExterno;
    
      // #262531	
    private String nombreInteresado;	
    private String apellido1Interesado;	
    private String apellido2Interesado;
    

    private String idServicioOrigen;
    
    private int numTerceros=0;

    private String nombreInteresadoExterno;
    private String apellido1InteresadoExterno;
    private String apellido2InteresadoExterno;

    private boolean asuntoAnotacionBaja = false;
    private String fechaBajaAsuntoCodificadoRegistro = null;
    
    private Integer codOficinaRegistro;
    private String nombreOficinaRegistro;

    // #278739: Entrada relacionada (RES_ENR) se muestra en dos campos: A?o y N?mero, necesarias dos propiedades para estos valores
    private String ejeEntradaRel;
    private String numEntradaRel;
    
    /************ ATRIBUTOS USADOS PARA LOS DOCUMENTOS DE REGISTRO *********/
    private int longitudDocumento;
    private int estadoDocumentoRegistro;
    private String rutaDocumentoRegistroDisco;
    
    /*Atributo de directiva de usuario de registro de salidas*/
    private String directivaUsuPermisoUor;
    private UsuarioValueObject usuarioLogueado;
    
    private boolean registroTelematico = false;
    private boolean finDigitalizacion;
    
    // #234108
    private boolean tipoDocInteresadoOblig = false;
    
    private String fechaDocAnterior;	
    private String nombreDocAnterior;	
    private String tipoDocAnterior;	
    private String organoDocAnterior;	
    private int estadoEntregadoAnterior;	
    private String nombreDocAnteriorMod;
    
    private String codigoSga;	
    private String expedienteSga;
    private boolean masInteresados;
    
    private boolean bloquearDestino=false;
    private boolean bloquearProcedimiento=false;

    // atributos usados para el buscador de entradas rechazadas
    private String fechaDesde;
    private String fechaHasta;
    private boolean misRechazadas;
    private String usuarioRechazo;
    
    // #270950
    private boolean procedimientoDigitalizacion;   
    
    // Variables para Gestionar llamadas a retramitrar documentos de registro en caso de cambio de procedimiento
    private String retramitarDocumentosCambioProcedimiento; // 1/0 Indica si Se deben retramitar los documento al modificar una entrada
    private String retramitarDocumentosDetalleOPeracion;  // Si no se puede retramitrar o falla el proceso, cancelamos la modificacion, y mostramos detalles al usuario en ocultoErrorAlta.jsp
    
    // #586104
    private boolean requiereGestionSIR;  // True para registros  Tipo Salida Tipo =1 (Salida SIR) y Entradas Tipo = 1 (Porcedente de otros registros - Se envian al SIE)
    private String codEstadoRespGestionSIR;  // Codigo estado respuesta Invocacion a operacion en Sistema. Codigo de A_TEX para mostrar mensaje en pantalla
    private String descEstadoRespGestionSIR; // Texto descriptivo del estado o mensaje al usuario con el resultado de las operaciones en SIR
    private String codigoUnidadDestinoSIR;
    private String nombreUnidadDestinoSIR;
    
    
    public String getNombreOficinaRegistro() {
        return nombreOficinaRegistro;
    }

    public void setNombreOficinaRegistro(String nombreOficinaRegistro) {
        this.nombreOficinaRegistro = nombreOficinaRegistro;
    }
    
    /**
    public int getCodOficinaRegistro() {
        return codOficinaRegistro;        
    }//getCodOficinaRegistro
    
    public void setCodOficinaRegistro(int codOficinaRegistro) {
        this.codOficinaRegistro = codOficinaRegistro;
    }//setCodOficinaRegistro
    **/
    
    public Integer getCodOficinaRegistro() {
        return codOficinaRegistro;        
    }//getCodOficinaRegistro
    
    public void setCodOficinaRegistro(Integer codOficinaRegistro) {
        this.codOficinaRegistro = codOficinaRegistro;
    }//setCodOficinaRegistro

    public int getNumTerceros() {
        return numTerceros;
    }

    public void setNumTerceros(int numTerceros) {
        this.numTerceros = numTerceros;
    }
    
     public String getFechaDoc() {
        return fechaDoc;
    }

    public void setFechaDoc(String param) {
        this.fechaDoc = param;
        // setFecha(param);
    }
    
    public boolean isDocNormal()
    {
        return this.docNormal;
    }
    
    
    public void setDocNormal(boolean flag){
        this.docNormal = flag;
    }
//  recojo los datos para insertar los interesados.	
    
    /*
    public byte[] getDocEscaneado()
    {
        return this.docEscaneado;
    }
    
    public void setDocEscaneado(byte[] doc)
    {
        this.docEscaneado = doc;
    }
    
    public String getTipoDocEscaneado()
    {
        return this.tipoDocEscaneado;
    }
    
    public void setTipoDocEscaneado(String doc)
    {
        this.tipoDocEscaneado = doc;
    }
    
    public String getNombreDocEscaneado()
    {
        return this.nombreDocEscaneado;
    }
    
    public void setNombreDocEscaneado(String nombre)
    {
       this.nombreDocEscaneado = nombre;
    }
    */
	 public void setlistaCodTercero(Vector lista) {
		 listaCodTercero = lista;
	    }
	    public Vector getlistaCodTercero() {
	        return listaCodTercero;
	    }
    public void setlistaVersionTercero(Vector lista) {
    	listaVersionTercero = lista;
    }
    public Vector getlistaVersionTercero() {
        return listaVersionTercero;
    }
    public void setlistaCodDomicilio(Vector lista) {
    	listaCodDomicilio = lista;
    }
    public Vector getlistaCodDomicilio() {
        return listaCodDomicilio;
    }
    public void setlistaRol(Vector lista) {
    	listaRol = lista;
    }
    public Vector getlistaRol() {
        return listaRol;
    }
   
    
    public String getIdServicioOrigen() {
        return idServicioOrigen;
    }

    public void setIdServicioOrigen(String idServicioOrigen) {
        this.idServicioOrigen = idServicioOrigen;
    }

    public String getDocumentoInteresado() {
        return documentoInteresado;
    }

    public void setDocumentoInteresado(String documentoInteresado) {
        this.documentoInteresado = documentoInteresado;
    }

    public String getNomCompletoInteresado() {
        return nomCompletoInteresado;
    }

    public void setNomCompletoInteresado(String nomCompletoInteresado) {
        this.nomCompletoInteresado = nomCompletoInteresado;
    }

    public String getApellido1InteresadoExterno() {
        return apellido1InteresadoExterno;
    }

    public void setApellido1InteresadoExterno(String apellido1InteresadoExterno) {
        this.apellido1InteresadoExterno = apellido1InteresadoExterno;
    }

    public String getApellido2InteresadoExterno() {
        return apellido2InteresadoExterno;
    }

    public void setApellido2InteresadoExterno(String apellido2InteresadoExterno) {
        this.apellido2InteresadoExterno = apellido2InteresadoExterno;
    }

    public String getNombreInteresadoExterno() {
        return nombreInteresadoExterno;
    }

    public void setNombreInteresadoExterno(String nombreInteresadoExterno) {
        this.nombreInteresadoExterno = nombreInteresadoExterno;
    }

    

    public String getTlfInteresado() {
        return tlfInteresado;
    }

    public void setTlfInteresado(String tlfInteresado) {
        this.tlfInteresado = tlfInteresado;
    }

    public String getEmailInteresado() {
        return emailInteresado;
    }

    public void setEmailInteresado(String emailInteresado) {
        this.emailInteresado = emailInteresado;
    }

    public String getProvInteresado() {
        return provInteresado;
    }

    public void setProvInteresado(String provInteresado) {
        this.provInteresado = provInteresado;
    }

    public String getMunInteresado() {
        return munInteresado;
    }

    public void setMunInteresado(String munInteresado) {
        this.munInteresado = munInteresado;
    }

    public String getPoblInteresado() {
        return poblInteresado;
    }

    public void setPoblInteresado(String poblInteresado) {
        this.poblInteresado = poblInteresado;
    }

    public String getCpInteresado() {
        return cpInteresado;
    }

    public void setCpInteresado(String cpInteresado) {
        this.cpInteresado = cpInteresado;
    }

    public String getDomCompletoInteresado() {
        return domCompletoInteresado;
    }

    public void setDomCompletoInteresado(String domCompletoInteresado) {
        this.domCompletoInteresado = domCompletoInteresado;
    }

    public String getCodTerceroExterno() {
        return codTerceroExterno;
    }

    public void setCodTerceroExterno(String codTerceroExterno) {
        this.codTerceroExterno = codTerceroExterno;
    }

    public String getNombreInteresado() {
        return nombreInteresado;
    }

    public void setNombreInteresado(String nombreInteresado) {
        this.nombreInteresado = nombreInteresado;
    }

    public String getApellido1Interesado() {
        return apellido1Interesado;
    }

    public void setApellido1Interesado(String apellido1Interesado) {
        this.apellido1Interesado = apellido1Interesado;
    }

    public String getApellido2Interesado() {
        return apellido2Interesado;
    }

    public void setApellido2Interesado(String apellido2Interesado) {
        this.apellido2Interesado = apellido2Interesado;
    }

    


    public DocumentoValueObject[] getDocumentos() {
        return documentos;
    }

    public void setDocumentos(DocumentoValueObject[] documentos) {
        this.documentos = documentos;
    }

    private DocumentoValueObject[] documentos;

    /** Construye un nuevo Registro por defecto. */
    public RegistroValueObject() {
        super();
    }

    public String getEjeOrigen(){
        return ejeOrigen;
    }

    public void setEjeOrigen(String ejeOrigen){
        m_Log.debug("en el value object la entrada relacionada es ^^^^^^^^^^^^^^^^^^^^^^ : " + ejeOrigen);
        this.ejeOrigen = ejeOrigen;
    }

    public int getIdentDepart() {
        return identDepart;
    }

    public void setIdentDepart(int param) {
        this.identDepart = param;
    }

    public int getUnidadOrgan() {
        return unidadOrgan;
    }

    public void setUnidadOrgan(int param) {
        this.unidadOrgan=param;
    }

    public String getTipoReg() {
        return tipoReg;
    }

    public void setTipoReg(String param) {
        this.tipoReg=param;
    }

    public long getNumReg() {
        return numReg;
    }

    public void setNumReg(long param) {
        this.numReg=param;
    }

    public int getAnoReg() {
        return anoReg;
    }

    public void setAnoReg(int param) {
        this.anoReg=param;
    }

    public String getFecEntrada() {
        return fecEntrada;
    }

    public void setFecEntrada(String param) {
        this.fecEntrada = param;
        // setFecha(param);
    }
    //fecha documento
     public String getFechaDocu() {
        return fechaDocu;
    }

    public void setFechaDocu(String fechaDoc) {
        this.fechaDocu = fechaDoc;
        // setFecha(param);
    }

    public String fecha() {
        String dia=getDiaAnotacion();
        String mes=getMesAnotacion();
        String ano=getAnoAnotacion();
        String fecha=dia + "/" + mes + "/" + ano;
        return fecha;
    }

    /* public void setFecha(Date param) {
       Fecha f = new Fecha();
       String ff = f.obtenerString(param);
       ff.substring(0,2);
       ff.substring(3,5);
       ff.substring(6,10);
       setFec(ff);
     }

     public void setFec(String fecha) {
       Fecha f = new Fecha();
       this.fecEntrada = f.obtenerDate(fecha);
     }*/


    public String getFecHoraDoc() {
        return fecHoraDoc;
    }

    // Fecha documento objeto de anotacion partida.

    public String getDiaDoc() {
        if ((fecHoraDoc != null)&&(fecHoraDoc.length() > 1))
            return fecHoraDoc.substring(0,2);
        else return "";
    }

    public String getMesDoc() {
        if ((fecHoraDoc != null)&&(fecHoraDoc.length() > 4))
            return fecHoraDoc.substring(3,5);
        else return "";
    }

    public String getAnoDoc() {
        if ((fecHoraDoc != null)&&(fecHoraDoc.length() > 9))
            return fecHoraDoc.substring(6,10);
        else return "";
    }

    public String getTxtHoraDoc() {
        if ((fecHoraDoc != null)&&(fecHoraDoc.length() > 12))
            return fecHoraDoc.substring(11,13);
        else return "";
    }

    public String getTxtMinDoc() {
        if ((fecHoraDoc != null)&&(fecHoraDoc.length() > 15))
            return fecHoraDoc.substring(14,16);
        else return "";
    }


    public String getHoraMinDocumento(){
        if(fecEntrada!=null && fecEntrada.length() >15) {
            return fecEntrada.substring(11,16);
        } else return "";
    }


    // Fecha entrada del documento objeto de anotacion partida
    // (= fecha en que esta abierto registro).

    public String getDiaAnotacion() {
        if(fecEntrada!=null && fecEntrada.length() >1) {
            return fecEntrada.substring(0,2);
        } else return "";

    }

    public String getMesAnotacion() {
        if(fecEntrada!=null && fecEntrada.length() >4) {
            return fecEntrada.substring(3,5);
        } else return "";
    }

    public String getAnoAnotacion() {
        if ((fecEntrada != null)&&(fecEntrada.length() > 9)) {
            return fecEntrada.substring(6,10);
        } else return "";
    }

    public String getHoraEnt() {
        if(fecEntrada !=null && fecEntrada.length() >12) {
            return fecEntrada.substring(11,13);
        } else return "";
    }

    public String getMinEnt() {
        if(fecEntrada != null && fecEntrada.length() >15) {
            return fecEntrada.substring(14,16);
        } else return "";
    }

    public String getSegEnt() { 
        if(fecEntrada != null && fecEntrada.length() >18) {
            return fecEntrada.substring(17,19);
        } else return ""; 
    }
        
    public String getHoraMinAnotacion(){
        if(fecEntrada !=null && fecEntrada.length() >15) {
            return fecEntrada.substring(11,16);
        } else return "";
    }

    public void setFecHoraDoc(String param) {
        this.fecHoraDoc=param;
    }

    public String getCodAsunto() {
        return codAsunto;
    }

    public void setCodAsunto(String param) {
        this.codAsunto=param;
    }    

    public String getDescripcionAsunto() {
        return descripcionAsunto;
    }

    public void setDescripcionAsunto(String descripcionAsunto) {
        this.descripcionAsunto = descripcionAsunto;
    }
    
    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String param) {
        this.asunto=param;
    }

    public int getTipoAnot() {
        return tipoAnot;
    }

    public void setTipoAnot(int param) {
        this.tipoAnot=param;
    }

    public String getOrgEntDest() {
        return orgEntDest;
    }

    public void setOrgEntDest(String param) {
        this.orgEntDest=param;
    }

    public String getIdDepDestino() {
        return idDepDestino;
    }

    public void setIdDepDestino(String param) {
        this.idDepDestino=param;
    }

    public String getDesc_dptoDestino() {
        return nomDptoDestino;
    }
    public void setDesc_dptoDestino(String nombre) {
        nomDptoDestino = nombre;
    }

    public String getIdUndRegDest() {
        return idUndRegDest;
    }
    public void setIdUndRegDest(String param) {
        this.idUndRegDest=param;
    }
    public String getDesc_uniRegDestino() {
        return nomUniRegDestino;
    }
    public void setDesc_uniRegDestino(String nombre) {
        nomUniRegDestino = nombre;
    }

    public String getOrgDestino() {
        return orgDestino;
    }

    public void setOrgDestino(String param) {
        this.orgDestino=param;
    }

//-----------------------------------------------------------------
// 16/12/2002
/*    public int getIdDepartemento() {
    return idDepartamento;
    }
    public void setIdDepartemento(int param) {
    this.idDepartamento=param;
    }
*/  public String getIdDepartemento() {
    return idDepartamento;
}
    public void setIdDepartemento(String param) {
        this.idDepartamento=param;
    }

    // uor cod
    public String getIdUndTramitad() {
        return idUndTramitad;
    }
    public void setIdUndTramitad(String param) {
        this.idUndTramitad=param;
    }

    public String getUorCodVisible() {
        return uorCodVisible;
    }

    public void setUorCodVisible(String uorCodVisible) {
        this.uorCodVisible = uorCodVisible;
    }



    public String getNomDptoDestinoOrd() {
        return nomDptoDestinoOrd;
    }

    public void setNomDptoDestinoOrd(String s) {
        this.nomDptoDestinoOrd=s;
    }
    public String getNomUniDestinoOrd() {
        return nomUniDestinoOrd;
    }

    public void setNomUniDestinoOrd(String s) {
        this.nomUniDestinoOrd=s;
    }

//-------------------------------------------------------------------

    public int getOrgEntOrigen() {
        return orgEntOrigen;
    }

    public void setOrgEntOrigen(int param) {
        this.orgEntOrigen=param;
    }

    public int getIdDepOrigen() {
        return idDepOrigen;
    }

    public void setIdDepOrigen(int param) {
        this.idDepOrigen=param;
    }

    public String getDesc_dptoOrigen() {
        return nomDptoOrigen;
    }
    public void setDesc_dptoOrigen(String nombre) {
        nomDptoOrigen = nombre;
    }

    public String getIdUndRegOrigen() {
        return idUndRegOrigen;
    }

    public void setIdUndRegOrigen(String param) {
        this.idUndRegOrigen=param;
    }

    public String getDesc_uniRegOrigen() {
        return nomUniRegOrigen;
    }
    public void setDesc_uniRegOrigen(String nombre) {
        nomUniRegOrigen = nombre;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String param) {
        this.codProcedimiento=param;
    }
    
    public String getCodProcedimientoRoles() {
        return codProcedimientoRoles;
    }

    public void setCodProcedimientoRoles(String param) {
        this.codProcedimientoRoles=param;
    }

    public String getDescProcedimiento() {
        return descProcedimiento;
    }

    public void setDescProcedimiento(String param) {
        this.descProcedimiento=param;
    }
    
    public String getMunProcedimiento() {
        return munProcedimiento;
    }

    public void setMunProcedimiento(String munProcedimiento) {
        this.munProcedimiento = munProcedimiento;
    }
    
    public String getOrganizacionOrigen() {
        return organizacionOrigen;
    }

    public void setOrganizacionOrigen(String param) {
        this.organizacionOrigen=param;
    }

    public String getTipoRegOrigen() {
        return tipoRegOrigen;
    }

    public void setTipoRegOrigen(String param) {
        this.tipoRegOrigen=param;
    }

    public String getNumOrigen() {
        return numOrigen;
    }

    public void setNumOrigen(String param) {
        this.numOrigen=param;
    }


    public int getIdTransporte() {
        return idTransporte;
    }

    public void setIdTransporte(int param) {
        this.idTransporte=param;
    }

    public String getDescTipoTransporte() {
        return descTipoTransporte;
    }

    public void setDescTipoTransporte(String param) {
        this.descTipoTransporte=param;
    }


    public String getNumTransporte() {
        return numTransporte;
    }

    public void setNumTransporte(String param) {
        this.numTransporte=param;
    }

    public int getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(int param) {
        this.idTipoDoc=param;
    }


    public String getDescTipoDoc() {
        // Dependerá del valor del código en la variable
        // Buscar en los vectores que debería estar cargados por orden del action.
        return descTipoDoc;
    }

    public void setDescTipoDoc(String param) {
        this.descTipoDoc=param;
    }

    public String getEntregado() {
        return entregado;
    }

    public void setEntregado(String entregado) {
        this.entregado = entregado;
    }



    public int getIdTipoPers() {
        return idTipoPers;
    }

    public void setIdTipoPers(int param) {
        this.idTipoPers=param;
    }

    // DUDA: mejor en javascript
    public String getDescTipoRem() {
        // Dependerá del valor del código en la variable
        // Buscar en los vectores que debería estar cargados por orden del action.
        return descTipoRem;
    }

    public void setDescTipoRem(String param) {
        this.descTipoRem=param;
    }

    public int getDomicInter() {
        return domicInter;
    }

    public void setDomicInter(int param) {
        this.domicInter=param;
    }

    public int getCodInter() {
        return codInter;
    }

    public void setCodInter(int param) {
        this.codInter=param;
    }

    public int getNumModInfInt() {
        return numModInfInt;
    }

    public void setNumModInfInt(int param) {
        this.numModInfInt=param;
    }

    public int getIdActuacion() {
        return idActuacion;
    }

    public void setIdActuacion(int param) {
        this.idActuacion=param;
    }

    // DUDA: mejor en javascript
    public String getDescActuacion() {
        // Dependerá del valor del código en la variable
        // Buscar en los vectores que debería estar cargados por orden del action.
        return descActuacion;
    }

    public void setDescActuacion(String param) {
        this.descActuacion=param;
    }


    public int getEstAnotacion() {
        return estAnotacion;
    }

    public void setEstAnotacion(int param) {
        this.estAnotacion=param;
    }

    public Integer getEstadoSIR() {
        return estadoSIR;
    }

    public void setEstadoSIR(Integer estadoSIR) {
        this.estadoSIR = estadoSIR;
    }


    public Date getFechaEstadoSIR() {
        return this.fechaEstadoSIR;
    }

    public void setFechaEstadoSIR(final Date fechaEstadoSIR) {
        this.fechaEstadoSIR = fechaEstadoSIR;
    }


    public String getIdentificadorRegistroSIR() {
        return identificadorRegistroSIR;
    }

    public void setIdentificadorRegistroSIR(String param) {
        this.identificadorRegistroSIR=param;
    }

    public String getDilAnulacion() {
        return dilAnulacion;
    }

    public void setDilAnulacion(String param) {
        this.dilAnulacion=param;
    }

    //PESTAÑA DESTINO ORIGEN, DESCRIPCIONES DE ORGANIZACION, ENTIDAD, DEPARTAMENTO , UNIDAD DE REGISTRO
    public String getNomDptoDestino() {
        return nomDptoDestino;
    }
    public void setNomDptoDestino(String nomDptoDestino) {
        this.nomDptoDestino = nomDptoDestino;
    }
    public String getNomDptoOrigen() {
        return nomDptoOrigen;
    }
    public void setNomDptoOrigen(String nomDptoOrigen) {
        this.nomDptoOrigen = nomDptoOrigen;
    }
    public String getNomEntidadDestino() {
        return nomEntidadDestino;
    }
    public void setNomEntidadDestino(String nomEntidadDestino) {
        this.nomEntidadDestino = nomEntidadDestino;
    }
    public String getNomEntidadOrigen() {
        return nomEntidadOrigen;
    }
    public void setNomEntidadOrigen(String nomEntidadOrigen) {
        this.nomEntidadOrigen = nomEntidadOrigen;
    }
    public String getNomOrganizacionDestino() {
        return nomOrganizacionDestino;
    }
    public void setNomOrganizacionDestino(String nomOrganizacionDestino) {
        this.nomOrganizacionDestino = nomOrganizacionDestino;
    }
    public String getNomOrganizacionOrigen() {
        return nomOrganizacionOrigen;
    }
    public void setNomOrganizacionOrigen(String nomOrganizacionOrigen) {
        this.nomOrganizacionOrigen = nomOrganizacionOrigen;
    }
    public String getNomUniRegDestino() {
        return nomUniRegDestino;
    }
    public void setNomUniRegDestino(String nomUniRegDestino) {
        this.nomUniRegDestino = nomUniRegDestino;
    }
    public String getNomUniRegOrigen() {
        return nomUniRegOrigen;
    }
    public void setNomUniRegOrigen(String nomUniRegOrigen) {
        this.nomUniRegOrigen = nomUniRegOrigen;
    }

    // LISTAS DE VALORES
    public Vector getListaTiposDocumentosAlta() {
        return listaTiposDocumentosAlta;
    }

    public void setListaTiposDocumentosAlta(Vector listaTiposDocumentosAlta) {
        this.listaTiposDocumentosAlta = listaTiposDocumentosAlta;
    }
    
    
    
    public void setListaTiposDocumentos(Vector lista) {
        listaTiposDocumentos =lista;
    }
    public Vector getListaTiposDocumentos() {
        return listaTiposDocumentos;
    }

    public void setListaTiposRemitentes(Vector lista) {
        listaTiposRemitentes =lista;
    }
    public Vector getListaTiposRemitentes() {
        return listaTiposRemitentes;
    }

    public void setListaTiposTransportes(Vector lista) {
        listaTiposTransportes =lista;
    }
    public Vector getListaTiposTransportes() {
        return listaTiposTransportes;
    }

    public void setListaActuaciones(Vector lista) {
        listaActuaciones=lista;
    }
    public Vector getListaActuaciones() {
        return listaActuaciones;
    }

    public void setListaDepartamentos(Vector lista) {
        listaDepartamentos=lista;
    }
    public Vector getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaTemas(Vector lista) {
        listaTemas=lista;
    }
    public Vector getListaTemas() {
        return listaTemas;
    }
    /*
     * Metodos get y set para poder mostrar los formularios
     * asociados a una entrada
     * 
     */
    public Collection getListaFormulariosAsociados(){
    	return listaFormulariosAsociados;
    }
    
    public void setListaFormulariosAsociados(Collection lista){
    	 listaFormulariosAsociados = lista;
    }

    public Collection getListaFormulariosAnexos() {
        return listaFormulariosAnexos;
    }

    public void setListaFormulariosAnexos(Collection listaFormulariosAnexos) {
        this.listaFormulariosAnexos = listaFormulariosAnexos;
    }
    
    

    public void setListaTiposIdInteresado(Vector lista) {
        listaTiposIdInteresado=lista;
    }
    public Vector getListaTiposIdInteresado() {
        return listaTiposIdInteresado;
    }

    public void setListaProcedimientos(Vector lista) {
        listaProcedimientos=lista;
    }
    public Vector getListaProcedimientos() {
        return listaProcedimientos;
    }


    public void setListaOrganizacionesExternas(Vector lista) {
        listaOrganizacionesExternas=lista;
    }
    public Vector getListaOrganizacionesExternas() {
        return listaOrganizacionesExternas;
    }

    // Temas asignados a una entrada
    public void setListaTemasAsignados(Vector lista) {
        listaTemasAsignados = lista;
    }
    public Vector getListaTemasAsignados() {
        return listaTemasAsignados;
    }

    // Docs asignados a una entrada
    public void setListaDocsAsignados(Vector lista) {
        listaDocsAsignados = lista;
    }
    public Vector getListaDocsAsignados() {
        return listaDocsAsignados;
    }      

    public Vector getListaDocsAnteriores() {
        return listaDocsAnteriores;
    }

    public void setListaDocsAnteriores(Vector listaDocsAnteriores) {
        this.listaDocsAnteriores = listaDocsAnteriores;
    }
    
    

    public Vector<MantAsuntosValueObject> getListaAsuntos() {
        return listaAsuntos;
    }
    public void setListaAsuntos(Vector<MantAsuntosValueObject> listaAsuntos) {
        this.listaAsuntos = listaAsuntos;
    }
    
    public void setCodigoTema(String param) {
        codigoTema = param;
    }
    public String getCodigoTema() {
        return codigoTema;
    }

    public void setDescTema(String param) {
        descTema = param;
    }
    public String getDescTema() {
        return descTema;
    }

    public void setNombreDoc(String param) {
        nombreDoc = param;
    }
    public String getNombreDoc() {
        return nombreDoc;
    }

    public void setTipoDoc(String param) {
        tipoDoc = param;
    }
    public String getTipoDoc() {
        return tipoDoc;
    }

	public void setCotejado(String param) {
        cotejado = param;
    }
	
    public String getCotejado() {
        return cotejado;
    }

    public String getCompulsado() {
        return compulsado;
    }

    public void setCompulsado(String compulsado) {
        this.compulsado = compulsado;
    }

    public DocumentoMetadatosVO getMetadatosDoc() {
        return metadatosDoc;
    }

    public void setMetadatosDoc(DocumentoMetadatosVO metadatosDoc) {
        this.metadatosDoc = metadatosDoc;
    }
    
    
    

    public void setDoc(byte[] param) {
        doc = param;
    }
    public byte[] getDoc() {
        return doc;
    }

    public void setTxtNomeTipoEntrada(String param) {
        txtNomeTipoEntrada = param;
    }
    public String getTxtNomeTipoEntrada() {
        return txtNomeTipoEntrada;
    }

    // Codigos
    public void setCodTipoDoc(String cod){
        codTipoDoc = cod;
    }
    public String getCodTipoDoc(){
        return codTipoDoc;
    }
    public void setCodTipoRemit(String cod){
        codTipoRemit = cod;
    }
    public String getCodTipoRemit(){
        return codTipoRemit;
    }

    public void setCodTipoTransp(String cod){
        codTipoTransp = cod;
    }
    public String getCodTipoTransp(){
        return codTipoTransp;
    }
    public void setCodAct(String cod){
        codAct= cod;
    }
    public String getCodAct(){
        return codAct;
    }

    // Respuesta del action.
    public void setRespOpcion(String param) {
        respOpcion = param;
    }
    public String getRespOpcion() {
        return respOpcion;
    }

    // Fecha que no se debe sobrepasar al comprobar fechas anterior y posterior.
    public void setFechaComprobacion(String param) {
        fechaComprobacion = param;
    }
    public String getFechaComprobacion() {
        return fechaComprobacion;
    }
    
    // Organización y entidad de quién hace la entrada.
    public void setIdOrganizacion(int id) {
        idOrganizacion = id;
    }
    public int getIdOrganizacion() {
        return idOrganizacion;
    }
    public void setIdEntidad(int id) {
        idEntidad= id;
    }
    public int getIdEntidad() {
        return idEntidad;
    }

    // Fallo

    public String getFallo() {
        return fallo;
    }

    public void setFallo(String param) {
        this.fallo=param;
    }

    // Contador

    public int getContador() {
        return contador;
    }

    public void setContador(int param) {
        contador=param;
    }

    // Para saber si la fecha de entrada es la misma que la fecha del registro

    public String getAbiertCerrado() {
        return abiertCerrado;
    }

    public void setAbiertCerrado(String param) {
        m_Log.debug("el valor de abiertoCerrado en el Value Object es : " + param);
        abiertCerrado=param;
    }

    // Texto de la Diligencia

    public String getTextoDiligencia() {
        return textoDiligencia;
    }

    public void setTextoDiligencia(String param) {
        textoDiligencia=param;
    }

    // Para saber si hay texto en la diligencia
    public String getHayTexto() {
        return hayTexto;
    }

    public void setHayTexto(String param) {
        hayTexto=param;
    }

    //para el buzon
    public boolean getHayBuzon(){
        return hayBuzon;
    }

    public void setHayBuzon(boolean hayBuzon){
        this.hayBuzon = hayBuzon;
    }


    // Consulta por nombre del interesado.
    public Vector getSubcamposNombreInteresado() {
        return subCamposNombreInteresado;
    }

    public void setSubcamposNombreInteresado(Vector v) {
        subCamposNombreInteresado = v;
    }

    // Consulta por tipo de documento del intersado.
/*	public int getTipoDocInteresado(){
    return tipoDocInteresado;
  }
  public void setTipoDocInteresado(int id) {
    tipoDocInteresado = id;
  }
*/
    public String getTipoDocInteresado(){
        return tipoDocInteresado;
    }
    public void setTipoDocInteresado(String id) {
        tipoDocInteresado = id;
    }
    /* Datos de quien registra */
    public String getUsuarioQRegistra() {
        return usuarioQRegistra;
    }
    public void setUsuarioQRegistra(String param) {
        usuarioQRegistra=param;
    }

    public String getDptoUsuarioQRegistra(){
        return dptoUsuarioQRegistra;
    }

    public void setDptoUsuarioQRegistra(String param){
        dptoUsuarioQRegistra=param;
    }

    public String getUnidOrgUsuarioQRegistra(){
        return unidOrgUsuarioQRegistra;
    }

    public void setUnidOrgUsuarioQRegistra(String param){
        unidOrgUsuarioQRegistra=param;
    }

    public void setTxtInteresado(String param) {
        txtInteresado = param;
    }
    public String getTxtInteresado() {
        return txtInteresado;
    }

    public String getNumeroAnotacionContestada(){
        return numeroAnotacionContestada;
    }
    public void setNumeroAnotacionContestada(String param){
        numeroAnotacionContestada=param;
    }
    public String getEjercicioAnotacionContestada(){
        return ejercicioAnotacionContestada;
    }
    public void setEjercicioAnotacionContestada(String param){
        ejercicioAnotacionContestada=param;
    }

    /* Fecha: 28/07/2033
* Enlace con expedientes */
    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String num) {
        numExpediente= num;
    }
    public int getEstadoExpediente() {
        return this.estadoExpediente;
    }

    public void setEstadoExpediente(final int estadoExpediente) {
        this.estadoExpediente = estadoExpediente;
    }


    public String getNumExpedienteBD() {
        return numExpedienteBD;
    }

    public void setNumExpedienteBD(String num) {
        numExpedienteBD= num;
    }

    public String getCodDptoDestBD() {
        return codDptoDestBD;
    }
    public void setCodDptoDestBD(String cod) {
        codDptoDestBD = cod;
    }
    public String getCodUORDestBD() {
        return codUORDestBD;
    }
    public void setCodUORDestBD(String cod) {
        this.codUORDestBD=cod;
    }


    public void setPaginaListado(String param) {
        paginaListado = param;
    }
    public String getPaginaListado() {
        return paginaListado;
    }

    public void setNumLineasPaginaListado(String param) {
        numLineasPaginaListado = param;
    }
    public String getNumLineasPaginaListado() {
        return numLineasPaginaListado;
    }

    public String getPosicionAnotacion() {
        return posicionAnotacion;
    }

    public void setPosicionAnotacion(String param) {
        posicionAnotacion = param;
    }

    public String getDeHistorico() {
        return deHistorico;
    }
    public void setDeHistorico(String deHistorico) {
        this.deHistorico = deHistorico;
    }

    public String getObservaciones()
    {
        return observaciones;
    }

    public void setObservaciones(String observaciones)
    {
        this.observaciones = observaciones;
    }
    public String getAutoridad()
    {
        return autoridad;
    }

    public void setAutoridad(String autoridad)
    {
        this.autoridad = autoridad;
    }
    
    public Vector<SimpleRegistroValueObject> getRelaciones() {
        return relaciones;
    }

    public void setRelaciones(Vector<SimpleRegistroValueObject> relaciones) {
        this.relaciones = relaciones;
    }
    
    //para recuperar los campos que aparecn en el listado
     public Vector getCamposListados() {
        return camposListados;
    }

    public void setCamposListados(Vector newLista) {
        camposListados = newLista;
    }

    public String getDirectivaUsuPermisoUor() {
        return directivaUsuPermisoUor;
    }

    public void setDirectivaUsuPermisoUor(String directivaUsuPermisoUor) {
        this.directivaUsuPermisoUor = directivaUsuPermisoUor;
    }

    public UsuarioValueObject getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(UsuarioValueObject usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
    
    
    
    
    
    public void copy(RegistroValueObject other) {
        this.codTipoDoc = other.codTipoDoc;
        this.codTipoTransp = other.codTipoTransp;
        this.codTipoRemit = other.codTipoRemit;
        this.identDepart=other.identDepart;
        this.unidadOrgan=other.unidadOrgan;
        this.tipoReg=other.tipoReg;
        this.numReg=other.numReg;
        this.anoReg=other.anoReg;
        this.fecEntrada=other.fecEntrada;
        this.fecHoraDoc=other.fecHoraDoc;
        this.asunto=other.asunto;
        this.tipoAnot=other.tipoAnot;
        this.idDepDestino=other.idDepDestino;
        this.idUndRegDest=other.idUndRegDest;
        this.idDepartamento=other.idDepartamento;
        this.idUndTramitad=other.idUndTramitad;
        this.idDepOrigen=other.idDepOrigen;
        this.idUndRegOrigen=other.idUndRegOrigen;
        this.tipoRegOrigen=other.tipoRegOrigen;
        this.ejeOrigen = other.ejeOrigen;
        this.numOrigen=other.numOrigen;
        this.idTransporte=other.idTransporte;
        this.numTransporte=other.numTransporte;
        this.idTipoDoc=other.idTipoDoc;
        this.idTipoPers=other.idTipoPers;
        this.idActuacion=other.idActuacion;
        this.estAnotacion=other.estAnotacion;
        this.dilAnulacion=other.dilAnulacion;
        this.listaTemas = other.listaTemas;
        this.usuarioQRegistra = other.usuarioQRegistra;
        this.dptoUsuarioQRegistra = other.dptoUsuarioQRegistra;
        this.unidOrgUsuarioQRegistra = other.unidOrgUsuarioQRegistra;
        this.observaciones = other.observaciones;
        this.autoridad = other.autoridad;
        this.txtInteresado = other.txtInteresado;
        this.tipoDocInteresado = other.tipoDocInteresado;
        this.documentoInteresado = other.documentoInteresado;
        this.codInter = other.codInter;
        this.numModInfInt = other.numModInfInt;
        this.domicInter = other.domicInter;
        this.munProcedimiento = other.munProcedimiento;
        this.codProcedimiento = other.codProcedimiento;
        this.codProcedimientoRoles = other.codProcedimientoRoles;
        this.orgDestino = other.orgDestino;
        this.organizacionOrigen = other.organizacionOrigen; 
        this.numExpediente=other.numExpediente;
        this.estadoExpediente=other.estadoExpediente;
        this.relaciones=other.relaciones;
        this.codAsunto=other.codAsunto;
        this.codAct = other.codAct;
        this.listaTemasAsignados = other.listaTemasAsignados;
        this.fechaDocu=other.fechaDocu;
        this.codOficinaRegistro = other.codOficinaRegistro;
        this.registroTelematico = other.registroTelematico;
        // # : se copian las propiedades de entrada relacionada
        this.ejeEntradaRel = other.ejeEntradaRel;
        this.numEntradaRel = other.numEntradaRel;
        this.finDigitalizacion = other.finDigitalizacion;
        // se copian las propiedades del filtro de entradas rechazadas
        this.misRechazadas = other.misRechazadas;
        this.fechaDesde = other.fechaDesde;
        this.fechaHasta = other.fechaHasta;
        this.estadoSIR = other.estadoSIR;
        this.identificadorRegistroSIR = other.identificadorRegistroSIR;
        this.codigoUnidadDestinoSIR = other.codigoUnidadDestinoSIR;

    }


    /**
     * Valida el estado de esta Registro
     * Puede ser invocado desde la capa cliente o desde la capa de negocio
     * @exception ValidationException si el estado no es válido
     */

    public void validate(String idioma) throws ValidationException {
        String sufijo = "";
        if ("euskera".equals(idioma))
            sufijo="_eu";
        boolean correcto = true;
        Messages errors = new Messages();

        if ((numReg == 0) || (numReg < 1))
            errors.add(
                    new Message("formulario", "registroentrada.error.numero.required"));

        if (!errors.empty())
            throw new ValidationException(errors);
        isValid = true;
    }

    public String toString() {
        String sc = "\n"; // salto de carro

        StringBuffer resultado = new StringBuffer("");

        resultado.append("AbiertCerrado: " + getAbiertCerrado() + sc);
        resultado.append("AnoAnotacion: " + getAnoAnotacion() + sc);
        resultado.append("AnoDoc: " + getAnoDoc() + sc);
        resultado.append("AnoReg: " + getAnoReg() + sc);
        resultado.append("Asunto: " + getAsunto() + sc);
        resultado.append("CodAct: " + getCodAct() + sc);
        resultado.append("CodDptoDestBD: " + getCodDptoDestBD() + sc);
        resultado.append("CodTema: " + getCodigoTema() + sc);
        resultado.append("CodInter: " + getCodInter() + sc);
        resultado.append("CodProcedimiento: " + getCodProcedimiento() + sc);
        resultado.append("CodProcedimientoRoles: " + getCodProcedimientoRoles() + sc);
        resultado.append("CodTipoDoc: " + getCodTipoDoc() + sc);
        resultado.append("CodTipoRemit: " + getCodTipoRemit() + sc);
        resultado.append("CodTipoTrans: " + getCodTipoTransp() + sc);
        resultado.append("CodUORDestBD: " + getCodUORDestBD() + sc);
        resultado.append("Contador: " + getContador() + sc);
        resultado.append("DeHistorico: " + getDeHistorico() + sc);
        resultado.append("Desc_dptoDestino: " + getDesc_dptoDestino() + sc);
        resultado.append("Desc_dptoOrigen: " + getDesc_dptoOrigen() + sc);
        resultado.append("Desc_uniRegDestino: " + getDesc_uniRegDestino() + sc);
        resultado.append("Desc_uniRegOrigen: " + getDesc_uniRegOrigen() + sc);
        resultado.append("DescActuacion: " + getDescActuacion() + sc);
        resultado.append("DescProcedimiento: " + getDescProcedimiento() + sc);
        resultado.append("DescTema: " + getDescTema() + sc);
        resultado.append("DescTipoDoc: " + getDescTipoDoc() + sc);
        resultado.append("DescTipoRem: " + getDescTipoRem() + sc);
        resultado.append("DescTipoTransporte: " + getDescTipoTransporte() + sc);
        resultado.append("DiaAnotacion: " + getDiaAnotacion() + sc);
        resultado.append("DiaDoc: " + getDiaDoc() + sc);
        resultado.append("DilAnulacion: " + getDilAnulacion() + sc);
        resultado.append("DomicInter: " + getDomicInter() + sc);
        resultado.append("DptoUsuarioQRegistra: " + getDptoUsuarioQRegistra() + sc);
        resultado.append("EjeOrigen: " + getEjeOrigen() + sc);
        resultado.append("EjercicioAnotacionContestada: " + getEjercicioAnotacionContestada() + sc);
        resultado.append("EstAnotacion: " + getEstAnotacion() + sc);
        resultado.append("Fallo: " + getFallo() + sc);
        resultado.append("FecEntrada: " + getFecEntrada() + sc);
        resultado.append("FecHoraDoc: " + getFecHoraDoc() + sc);
        resultado.append("HayBuzon: " + getHayBuzon() + sc);
        resultado.append("HayTexto: " + getHayTexto() + sc);
        resultado.append("HoraEnt: " + getHoraEnt() + sc);
        resultado.append("HoraMinAnotacion: " + getHoraMinAnotacion() + sc);
        resultado.append("HoraMinDocumento: " + getHoraMinDocumento() + sc);
        resultado.append("IdActuacion: " + getIdActuacion() + sc);
        resultado.append("IdDepartemento: " + getIdDepartemento() + sc);
        resultado.append("IdDepDestino: " + getIdDepDestino() + sc);
        resultado.append("IdDepOrigen: " + getIdDepOrigen() + sc);
        resultado.append("IdentDepart: " + getIdentDepart() + sc);
        resultado.append("IdEntidad: " + getIdEntidad() + sc);
        resultado.append("IdOrganizacion: " + getIdOrganizacion() + sc);
        resultado.append("IdTipoDoc: " + getIdTipoDoc() + sc);
        resultado.append("IdTipoPers: " + getIdTipoPers() + sc);
        resultado.append("IdTransporte: " + getIdTransporte() + sc);
        resultado.append("IdUndRegDest: " + getIdUndRegDest() + sc);
        resultado.append("IdUndRegOrigen: " + getIdUndRegOrigen() + sc);
        resultado.append("IdUndTramitad: " + getIdUndTramitad() + sc);
        resultado.append("UorCodVisible: " + getUorCodVisible() + sc);
        if(getListaActuaciones() != null) {
            resultado.append("ListaActuaciones (size()): " + getListaActuaciones().size() + sc);
        }
        // salto
        resultado.append("MesAnotacion: " + getMesAnotacion() + sc);
        resultado.append("MesDoc: " + getMesDoc() + sc);
        resultado.append("MinEnt: " + getMinEnt() + sc);
        resultado.append("NomDptoDestino: " + getNomDptoDestino() + sc);
        resultado.append("NomDptoDestinoOrd: " + getNomDptoDestinoOrd() + sc);
        resultado.append("NomDptoOrigen: " + getNomDptoOrigen() + sc);
        resultado.append("NomEntidadDestino: " + getNomEntidadDestino() + sc);
        resultado.append("NomEntidadOrigen: " + getNomEntidadOrigen() + sc);
        resultado.append("NomOrganizacionDestino: " + getNomOrganizacionDestino() + sc);
        resultado.append("NomOrganizacionOrigen: " + getNomOrganizacionOrigen() + sc);
        resultado.append("NomUniDestinoOrd: " + getNomUniDestinoOrd() + sc);
        resultado.append("NomUniRegDestino: " + getNomUniRegDestino() + sc);
        resultado.append("NomUniRegOrigen: " + getNomUniRegOrigen() + sc);
        resultado.append("NumeroAnotacionContestada: " + getNumeroAnotacionContestada() + sc);
        resultado.append("NumExpediente: " + getNumExpediente() + sc);
        resultado.append("EstadoExpediente: " + getEstadoExpediente() + sc);
        resultado.append("NumExpedienteBD: " + getNumExpedienteBD() + sc);
        resultado.append("NumLineasPaginaListado: " + getNumLineasPaginaListado() + sc);
        resultado.append("NumModInfInt: " + getNumModInfInt() + sc);
        resultado.append("NumOrigen: " + getNumOrigen() + sc);
        long numReg = getNumReg();
        resultado.append("NumReg: " + numReg + sc);
        resultado.append("NumTransporte: " + getNumTransporte() + sc);
        resultado.append("Observaciones: " + getObservaciones() + sc);
        resultado.append ("Autoridad: " + getAutoridad() + sc);
        resultado.append("OrganizacionOrigen(): " + getOrganizacionOrigen() + sc);
        resultado.append("OrgDestino: " + getOrgDestino() + sc);
        resultado.append("OrgEntDest: " + getOrgEntDest() + sc);
        resultado.append("OrgEntOrigen: " + getOrgEntOrigen() + sc);
        resultado.append("COD_OFICINA_REGISTRO"+getCodOficinaRegistro()+sc);
        resultado.append("PaginaListado: " + getPaginaListado() + sc);
        resultado.append("PosicionAnotacion: " + getPosicionAnotacion() + sc);
        resultado.append("RespOpcion: " + getRespOpcion() + sc);
        resultado.append("SubcamposNombreInteresado: " + getSubcamposNombreInteresado() + sc);
        resultado.append("TextoDiligencia: " + getTextoDiligencia() + sc);
        resultado.append("TipoAnot: " + getTipoAnot() + sc);
        resultado.append("TipoDocInteresado: " + getTipoDocInteresado() + sc);
        resultado.append("TipoReg: " + getTipoReg() + sc);
        resultado.append("TipoRegOrigen: " + getTipoRegOrigen() + sc);
        resultado.append("TxtHoraDoc: " + getTxtHoraDoc() + sc);
        resultado.append("TxtInteresado: " + getTxtInteresado() + sc);
        resultado.append("TxtMinDoc: " + getTxtMinDoc() + sc);
        resultado.append("TxtNomeTipoEntrada: " + getTxtNomeTipoEntrada() + sc);
        resultado.append("UnidadOrgan: " + getUnidadOrgan() + sc);
        resultado.append("UnidOrgUsuarioQRegistra: " + getUnidOrgUsuarioQRegistra() + sc);
        resultado.append("UsuarioQRegistra: " + getUsuarioQRegistra() + sc);
        resultado.append("CodProcedimiento: " + getCodProcedimiento() + sc);
        resultado.append("MunProcedimiento: " + getMunProcedimiento() + sc);
        resultado.append("Asunto codificado: " + getCodAsunto() + sc);
        //resultado.append(sc);
        resultado.append("Registro telematico: "+ isRegistroTelematico() + sc);
        resultado.append("Fin Digitalizacion: "+ isFinDigitalizacion() + sc);

        return resultado.toString();
    }

    /**
     * @return the asuntoAnotacionBaja
     */
    public boolean isAsuntoAnotacionBaja() {
        return asuntoAnotacionBaja;
    }

    /**
     * @param asuntoAnotacionBaja the asuntoAnotacionBaja to set
     */
    public void setAsuntoAnotacionBaja(boolean asuntoAnotacionBaja) {
        this.asuntoAnotacionBaja = asuntoAnotacionBaja;
    }


    public String getFechaBajaAsuntoCodificadoRegistro(){
        return this.fechaBajaAsuntoCodificadoRegistro;
    }
    
    
    public void setFechaBajaAsuntoCodificadoRegistro(String fecha){
        this.fechaBajaAsuntoCodificadoRegistro = fecha;
    }
    
     public ArrayList<HojaArbolClasifAsuntosValueObject> getArbolClasifAsuntos() {
        return arbolClasifAsuntos;
    }

    public void setArbolClasifAsuntos(ArrayList<HojaArbolClasifAsuntosValueObject> arbolClasifAsuntos) {
        this.arbolClasifAsuntos = arbolClasifAsuntos;
    }
            
    private ArrayList<String> numExpedientesRelacionados;
    private ArrayList<String> codProcedimientoExpedientesRelacionados;

    public ArrayList<String> getCodProcedimientoExpedientesRelacionados() {
        return codProcedimientoExpedientesRelacionados;
    }//getCodProcedimientoExpedientesRelacionados
    public void setCodProcedimientoExpedientesRelacionados(ArrayList<String> codProcedimientoExpedientesRelacionados) {
        this.codProcedimientoExpedientesRelacionados = codProcedimientoExpedientesRelacionados;
    }//setCodProcedimientoExpedientesRelacionados

    public ArrayList<String> getNumExpedientesRelacionados() {
        return numExpedientesRelacionados;
    }//getNumExpedientesRelacionados
    public void setNumExpedientesRelacionados(ArrayList<String> numExpedientesRelacionados) {
        this.numExpedientesRelacionados = numExpedientesRelacionados;
    }//setNumExpedientesRelacionados
    
    private Boolean opcionPermanencia = false;
    public Boolean getOpcionPermanencia() {
        return opcionPermanencia;
    }//getOpcionPermanencia
    public void setOpcionPermanencia(Boolean opcionPermanencia) {
        this.opcionPermanencia = opcionPermanencia;
    }//setOpcionPermanencia

    /**
     * @return the longitudDocumento
     */
    public int getLongitudDocumento() {
        return longitudDocumento;
    }

    /**
     * @param longitudDocumento the longitudDocumento to set
     */
    public void setLongitudDocumento(int longitudDocumento) {
        this.longitudDocumento = longitudDocumento;
    }

    /**
     * @return the estadoDocumentoRegistro
     */
    public int getEstadoDocumentoRegistro() {
        return estadoDocumentoRegistro;
    }

    /**
     * @param estadoDocumentoRegistro the estadoDocumentoRegistro to set
     */
    public void setEstadoDocumentoRegistro(int estadoDocumentoRegistro) {
        this.estadoDocumentoRegistro = estadoDocumentoRegistro;
    }

    /**
     * @return the rutaDocumentoRegistroDisco
     */
    public String getRutaDocumentoRegistroDisco() {
        return rutaDocumentoRegistroDisco;
    }

    /**
     * @param rutaDocumentoRegistroDisco the rutaDocumentoRegistroDisco to set
     */
    public void setRutaDocumentoRegistroDisco(String rutaDocumentoRegistroDisco) {
        this.rutaDocumentoRegistroDisco = rutaDocumentoRegistroDisco;
    }

    /**
     * @return the ejercicioAnotacionDuplicadaOrigen
     */
    public int getEjercicioAnotacionDuplicadaOrigen() {
        return ejercicioAnotacionDuplicadaOrigen;
    }

    /**
     * @param ejercicioAnotacionDuplicadaOrigen the ejercicioAnotacionDuplicadaOrigen to set
     */
    public void setEjercicioAnotacionDuplicadaOrigen(int ejercicioAnotacionDuplicadaOrigen) {
        this.ejercicioAnotacionDuplicadaOrigen = ejercicioAnotacionDuplicadaOrigen;
    }

    public boolean isRegistroTelematico() {
        return registroTelematico;
    }

    public void setRegistroTelematico(boolean registroTelematico) {
        this.registroTelematico = registroTelematico;
    }

    public boolean isFinDigitalizacion() {
        return finDigitalizacion;
    }

    public void setFinDigitalizacion(boolean finDigitalizacion) {
        this.finDigitalizacion = finDigitalizacion;
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

    public String getFechaDocAnterior() {
        return fechaDocAnterior;
    }

    public void setFechaDocAnterior(String fechaDocAnterior) {
        this.fechaDocAnterior = fechaDocAnterior;
    }

    public String getNombreDocAnterior() {
        return nombreDocAnterior;
    }

    public void setNombreDocAnterior(String nombreDocAnterior) {
        this.nombreDocAnterior = nombreDocAnterior;
    }

    public String getTipoDocAnterior() {
        return tipoDocAnterior;
    }

    public void setTipoDocAnterior(String tipoDocAnterior) {
        this.tipoDocAnterior = tipoDocAnterior;
    }

    public String getOrganoDocAnterior() {
        return organoDocAnterior;
    }

    public void setOrganoDocAnterior(String organoDocAnterior) {
        this.organoDocAnterior = organoDocAnterior;
    }

    public int getEstadoEntregadoAnterior() {
        return estadoEntregadoAnterior;
    }

    public void setEstadoEntregadoAnterior(int estadoEntregadoAnterior) {
        this.estadoEntregadoAnterior = estadoEntregadoAnterior;
    }

    public String getNombreDocAnteriorMod() {
        return nombreDocAnteriorMod;
    }

    public void setNombreDocAnteriorMod(String nombreDocAnteriorMod) {
        this.nombreDocAnteriorMod = nombreDocAnteriorMod;
    }

    public String getCodigoSga() {
        return codigoSga;
    }

    public void setCodigoSga(String codigoSga) {
        this.codigoSga = codigoSga;
    }

    public String getExpedienteSga() {
        return expedienteSga;
    }

    public void setExpedienteSga(String expedienteSga) {
        this.expedienteSga = expedienteSga;
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
     * 
     * @return 
     */
    public String getEjeEntradaRel() {
        return ejeEntradaRel;
    }

    /**
     * 
     * @param ejeEntradaRel 
     */
    public void setEjeEntradaRel(String ejeEntradaRel) {
        this.ejeEntradaRel = ejeEntradaRel;
    }

    /**
     * 
     * @return 
     */
    public String getNumEntradaRel() {
        return numEntradaRel;
    }

    /**
     * 
     * @param numEntradaRel 
     */
    public void setNumEntradaRel(String numEntradaRel) {
        this.numEntradaRel = numEntradaRel;
    }

    /**
     * @return the catalogado
     */
    public String getCatalogado() {
        return catalogado;
    }

    /**
     * @param catalogado the catalogado to set
     */
    public void setCatalogado(String catalogado) {
        this.catalogado = catalogado;
    }
    
    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public boolean isMisRechazadas() {
        return misRechazadas;
    }

    public void setMisRechazadas(boolean misRechazadas) {
        this.misRechazadas = misRechazadas;
    }

    /**
     * @return the usuarioRechazo
     */
    public String getUsuarioRechazo() {
        return usuarioRechazo;
    }

    /**
     * @param usuarioRechazo the usuarioRechazo to set
     */
    public void setUsuarioRechazo(String usuarioRechazo) {
        this.usuarioRechazo = usuarioRechazo;
    }
    
    public boolean isMasInteresados() {
        return masInteresados;
    }

    public void setMasInteresados(boolean masInteresados) {
        this.masInteresados = masInteresados;
    }

    public boolean isProcedimientoDigitalizacion() {
        return procedimientoDigitalizacion;
    }

    public void setProcedimientoDigitalizacion(boolean procedimientoDigitalizacion) {
        this.procedimientoDigitalizacion = procedimientoDigitalizacion;
    }

    public String getDescripcionTipoDocumental() {
        return descripcionTipoDocumental;
    }

    public void setDescripcionTipoDocumental(String descripcionTipoDocumental) {
        this.descripcionTipoDocumental = descripcionTipoDocumental;
    }
    
    /**
     * @return the idDocumento
     */
    public long getIdDocumento() {
        return idDocumento;
    }

    /**
     * @param idDocumento the idDocumento to set
     */
    public void setIdDocumento(long idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getObservDoc() {
        return observDoc;
    }

    public void setObservDoc(String observDoc) {
        this.observDoc = observDoc;
    }

    public String getNombreTecnicoReferencia() {
        return nombreTecnicoReferencia;
    }

    public void setNombreTecnicoReferencia(String nombreTecnicoReferencia) {
        this.nombreTecnicoReferencia = nombreTecnicoReferencia;
    }  

    public String getRetramitarDocumentosCambioProcedimiento() {
        return retramitarDocumentosCambioProcedimiento;
    }

    public void setRetramitarDocumentosCambioProcedimiento(String retramitarDocumentosCambioProcedimiento) {
        this.retramitarDocumentosCambioProcedimiento = retramitarDocumentosCambioProcedimiento;
    }

    public String getRetramitarDocumentosDetalleOPeracion() {
        return retramitarDocumentosDetalleOPeracion;
    }

    public void setRetramitarDocumentosDetalleOPeracion(String retramitarDocumentosDetalleOPeracion) {
        this.retramitarDocumentosDetalleOPeracion = retramitarDocumentosDetalleOPeracion;
    }

    public String getNombreUnidadDestinoSIR() {
        return nombreUnidadDestinoSIR;
    }

    public void setNombreUnidadDestinoSIR(String nombreUnidadDestinoSIR) {
        this.nombreUnidadDestinoSIR = nombreUnidadDestinoSIR;
    }

    public String getCodigoUnidadDestinoSIR() {
        return codigoUnidadDestinoSIR;
    }

    public void setCodigoUnidadDestinoSIR(String codigoUnidadDestinoSIR) {
        this.codigoUnidadDestinoSIR = codigoUnidadDestinoSIR;
    }

    public boolean isRequiereGestionSIR() {
        return requiereGestionSIR;
    }

    public void setRequiereGestionSIR(boolean requiereGestionSIR) {
        this.requiereGestionSIR = requiereGestionSIR;
    }

    public String getCodEstadoRespGestionSIR() {
        return codEstadoRespGestionSIR;
    }

    public void setCodEstadoRespGestionSIR(String codEstadoRespGestionSIR) {
        this.codEstadoRespGestionSIR = codEstadoRespGestionSIR;
    }

    public String getDescEstadoRespGestionSIR() {
        return descEstadoRespGestionSIR;
    }

    public void setDescEstadoRespGestionSIR(String descEstadoRespGestionSIR) {
        this.descEstadoRespGestionSIR = descEstadoRespGestionSIR;
    }
    
}//class
