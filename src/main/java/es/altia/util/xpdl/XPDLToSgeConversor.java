package es.altia.util.xpdl;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.sge.CampoSuplementarioTramiteVO;
import es.altia.agora.business.sge.CampoSuplementarioVO;
import es.altia.agora.business.sge.CondicionEntradaTramiteVO;
import es.altia.agora.business.sge.DefinicionAgrupacionCamposValueObject;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.FirmasDocumentoProcedimientoVO;
import es.altia.agora.business.sge.DocumentoExpedienteVO;
import es.altia.agora.business.sge.DocumentoTramiteVO;
import es.altia.agora.business.sge.EnlaceProcedimientoVO;
import es.altia.agora.business.sge.EnlaceTramiteVO;
import es.altia.agora.business.sge.RolProcedimientoVO;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.xpdl.exception.MapeoXPDLException;
import java.io.InputStream;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import es.altia.util.xpdl.exceptions.ParsingException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import xades4j.utils.StringUtils;


public class XPDLToSgeConversor {
    
    //private Namespace XMLNS=Namespace.getNamespace("","http://www.wfmc.org/2002/XPDL1.0");
    private static Namespace XMLNS=Namespace.getNamespace("","http://www.wfmc.org/2008/XPDL2.1");
    private HashMap hashMapTramites = new HashMap();
    private HashMap hashMapTransicionesFavorable = new HashMap();
    private HashMap hashMapTransicionesDesfavorable = new HashMap();
    private HashMap hashMapCondEntrada =new HashMap();
    private HashMap hashMapUORS = new HashMap();
    private HashMap hashMapListaNumerosSecuencia = new HashMap();
    private DefinicionProcedimientosValueObject  dpVO;
    private Logger log = Logger.getLogger(XPDLToSgeConversor.class);
    private static XPDLToSgeConversor instance = null;
    private Hashtable<String,String> participantes = new Hashtable<String, String>();


    private HashMap hashMapTramitesTransicionesFavorable = new HashMap();
    private HashMap hashMapTramitesTransicionesDesfavorable = new HashMap();

    private XPDLToSgeConversor() {
    }

    public static XPDLToSgeConversor getInstance() {
        if (instance == null) {
            instance = new XPDLToSgeConversor();
        }
        return instance;
    }

    public Hashtable<String, String> getParticipantes(){
        return this.participantes;
    }

    public void vaciarHashMapTramitesTransiciones(){
        this.hashMapTramitesTransicionesFavorable       = new HashMap();
        this.hashMapTramitesTransicionesDesfavorable  = new HashMap();
    }

    /**
     * Recibe un flujo de entrada, construye documento y extrae los datos de los
     * procedimientos
     *
     * @param contenido: Fichero XPDL
     * @param codMunicipio: Código de la organización
     * @return un vector de procedimientos
     */     
    //public static Vector procedimientoFromXPDL(InputStream in,int codMunicipio){
    public Vector procedimientoFromXPDL(File ftemporal,int codMunicipio,String[] params){
         Vector  result = new Vector();
        try{
            log.debug(" Longitud del fichero: " +  ftemporal.length());

            SAXBuilder builder = new SAXBuilder();
            //Document document = builder.build(in);
            Document document = builder.build(ftemporal);
            Element packageElement = document.getRootElement();
            Element workflowProcessesElement = packageElement.getChild("WorkflowProcesses",XMLNS);
            
            List workflowProcessElements = null;
         
            workflowProcessElements = workflowProcessesElement.getChildren("WorkflowProcess",XMLNS);
         
            Iterator<Element> it = workflowProcessElements.iterator();
            while (it.hasNext()){
                DefinicionProcedimientosValueObject defProcTramVO = procedimientoFromXPDL(it.next(),codMunicipio,params);
                result.add(defProcTramVO);
            }
           
            return result;
            
        }catch(Exception e){
            e.printStackTrace();
            throw new ParsingException("Error deserializing instance"
                    + " of " + DefinicionProcedimientosValueObject.class, e);
        }
       
    }
    /**
     * Parsea el elemento participants para detectar las unidades organicas
     * @param participantsElement
     */
    public void añadirParticipantes(Element participantsElement){
        
        List participantes = participantsElement.getChildren("Participant",XMLNS);
        for (int i=0;i<participantes.size();i++){
            Element participantElement = (Element)participantes.get(i);
            String Id = participantElement.getAttributeValue("Id");
            String Name = participantElement.getAttributeValue("Name");
            hashMapUORS.put(Id, Name);
        }
    }
    /**
     * Parsea un elemento workflowProcessElement para extraer la informacion de
     * un procedimiento
     *
     * @param workflowProccessElement
     * @param codMunicipio: Código del municipio
     * @return un objeto DefinicionProcedimientosValueObject
     */
    public DefinicionProcedimientosValueObject procedimientoFromXPDL(Element workflowProccessElement,int codMunicipio,String[] params){
        
        Vector<DefinicionTramitesValueObject> aux = new Vector<DefinicionTramitesValueObject>();
        Vector<DefinicionTramitesValueObject> tramites = new Vector<DefinicionTramitesValueObject>();
        DefinicionProcedimientosValueObject defProcVO = inicializarProcedimiento(workflowProccessElement,codMunicipio);
        dpVO = defProcVO; 
        Element activitiesElement = workflowProccessElement.getChild("Activities",XMLNS);
        if (activitiesElement!=null){
            List <Element> activities = activitiesElement.getChildren("Activity",XMLNS);
            Element transitionsElement = workflowProccessElement.getChild("Transitions",XMLNS);            
            List <Element> transitions = transitionsElement.getChildren("Transition",XMLNS);
             // Se vacían los hash map que contienen las transiciones favorables y desfavorables entre los trámites
            vaciarHashMapTramitesTransiciones();
            
            for (Element activity : activities) {
                if (activity.getChild("Route",XMLNS)==null) {
                    DefinicionTramitesValueObject tramite = tramitesFromXPDL(activity,workflowProccessElement,codMunicipio,params);
                    hashMapTramites.put(tramite.getCodigoTramite(), tramite);
                    tramites.add(tramite);
                } else {
                    comprobarTransiciones(activity,transitions);
                }
            }
            
            for (DefinicionTramitesValueObject tr:tramites){
                DefinicionTramitesValueObject tramite = anhadirCondicionesTramite(tr);
                aux.add(tramite);
            }
            if (aux.size() == tramites.size()) {
                defProcVO.setTramites(aux);
            }
        }

        // Recuperamos los datos de los flujos de firmas y los circuitos de estos
        List<FirmaFlujoVO> listaFlujos = flujosYCircuitosFirmasFromXPDL(workflowProccessElement);
        defProcVO.setListaFlujosFirma(listaFlujos);

        return defProcVO;
    }

    /**
     * Extrae el código del procedimiento del fichero XPDL
     *
     * @param in: InputStream desde el que se lee el fichero XPDL
     * @return String con el código del procedimiento
     */
    public String getCodigoProcedimiento(InputStream in) throws MapeoXPDLException{
        String codProcedimiento = null;

        try{
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(in);
            Element packageElement = document.getRootElement();
            Element workflowProcessesElement = packageElement.getChild("WorkflowProcesses",XMLNS);
            
            List workflowProcessElements = null;
            workflowProcessElements = workflowProcessesElement.getChildren("WorkflowProcess",XMLNS);

            Iterator<Element> it = workflowProcessElements.iterator();
            while (it.hasNext()){
                Element proceso = it.next();
                codProcedimiento = proceso.getAttributeValue("Id");
                // Viene un único proceso en el documento
                break;
            }
        }catch(JDOMException e){
            e.printStackTrace();
            throw new MapeoXPDLException("Error durante la interpretación del fichero XPDL");
        }catch(IOException e){
            e.printStackTrace();
            throw new MapeoXPDLException("Error durante la interpretación del fichero XPDL");
        }

        return codProcedimiento;
    }

    /**
     * Inicializa los datos de un procedimiento
     *
     * @param workflowProccessElement
     * @param codMunicipio: Código del municipio
     * @return un objeto DefinicionProcedimientosValueObject
     */
    public DefinicionProcedimientosValueObject inicializarProcedimiento (Element workflowProccessElement,int codMunicipio){
        
        String tramiteInicioProcedimiento = null;
        String fechaVigenciaDesde = null;
        String fechaVigenciaHasta = null;
        String areaProcedimiento = null;
        String descripcionAreaProcedimiento = null;
        String tipoProcedimiento = null;
        String plazoProcedimiento = null;
        String porcentajePlazo = null;
        String tipoPlazo = null;
        String estadoProcedimiento = null;
        String localizacionProc = null;
        String tipoUnidadInicioProc = null;
        String unidadesInicioProc = null;
        String visibleTramitacion = null;
        String dispWebCiudadano = null;
        String restringido = null;
        String libreriaFlujo = null;
        String numeroSegunAnotacion = null;
        String soloInicioWebService = null;
        String descripcionBreve      = null;
        String tipoSilencio              = null;        
        String txtCodigo                = null;
        String tipoInicioProc           = null;
        String codPluginFinNoConvencional = null;
        String implClassPluginFinNoConvencional = null;
        String interesOblig = null;
        
        Element extendedAttributesElement = workflowProccessElement.getChild("ExtendedAttributes",XMLNS);                
        String txtDescripcion = workflowProccessElement.getAttributeValue("Name");
        
        List content = extendedAttributesElement.getContent();        
        if(content!=null){

            for(int i=0;i<content.size();i++){
                if(content.get(i) instanceof org.jdom.Element){
                    org.jdom.Element elemento = (org.jdom.Element)content.get(i);
                    List<Attribute> atributosElemento = (List<Attribute>)elemento.getAttributes();
                    
                    if(atributosElemento!=null){
                        Attribute atributo = atributosElemento.get(0);
                        String nombre = atributo.getValue();
                        String valor     = atributosElemento.size()>=2?atributosElemento.get(1).getValue():"";
                        
                        log.debug(" ======================> " + XPDLToSgeConversor.class.toString() + "<>  nombre:  " + nombre + ", valor: " + valor);

                        if ("tramite_inicio_procedimiento".equals(nombre)) {
                            tramiteInicioProcedimiento = valor;
                        } else if ("fecha_vigencia_desde".equals(nombre)) {
                            fechaVigenciaDesde = valor;
                        } else if ("fecha_vigencia_hasta".equals(nombre)) {
                            fechaVigenciaHasta = valor;
                        } else if ("area_procedimiento".equals(nombre)) {
                            areaProcedimiento = valor;
                        } else if ("descripcion_area_procedimiento".equals(nombre)){
                            descripcionAreaProcedimiento = valor;
                        } else if ("tipo_procedimiento".equals(nombre)) {
                            tipoProcedimiento = valor;
                        } else if ("plazo_procedimiento".equals(nombre)) {
                            plazoProcedimiento = valor;
                        } else if ("porcentaje_plazo_procedimiento".equals(nombre)) {
                            porcentajePlazo = valor;
                        } else if ("tipo_plazo_procedimiento".equals(nombre)) {
                            tipoPlazo = valor;
                        } else if ("estado_procedimiento".equals(nombre)) {
                            estadoProcedimiento = valor;
                        } else if ("localizacion_procedimiento".equals(nombre)) {
                            localizacionProc = valor;
                        } else if ("tipo_unidad_inicio_procedimiento".equals(nombre)) {
                            tipoUnidadInicioProc = valor;
                        } else if ("unidades_inicio_procedimiento".equals(nombre)) {
                            unidadesInicioProc = valor;
                        } else if ("visible_tramitacion_internet".equals(nombre)) {
                            visibleTramitacion = valor;
                        } else if ("disponible_web_ciudadano".equals(nombre)) {
                            dispWebCiudadano = valor;
                        } else if ("restringido".equals(nombre))   {
                            restringido = valor;
                        } else if ("es_libreria_flujo".equals(nombre))   {
                             libreriaFlujo = valor;
                        } else if ("numero_segun_anotacion".equals(nombre))   {
                             numeroSegunAnotacion = valor;
                        } else if ("solo_inicio_webservice".equals(nombre))   {
                             soloInicioWebService = valor;
                        } else if ("tipo_silencio".equals(nombre) && (valor != null && !"".equals(valor))) {
                            tipoSilencio = valor;
                        } else if ("descripcion_breve".equals(nombre)) {
                            descripcionBreve = valor;
                        } else if ("tipo_inicio_procedimiento".equals(nombre)) {
                            tipoInicioProc = valor;
                        } else if ("cod_plugin_fin_no_convencional".equals(nombre)) {
                            codPluginFinNoConvencional = valor;
                        } else if ("impl_class_fin_no_convencional".equals(nombre)) {
                            implClassPluginFinNoConvencional = valor;
                        } else if ("InteresadoObligatorio".equals(nombre)) {
                            interesOblig = valor;
                        }

                    }
                }
            }
        }          
    
        txtCodigo = workflowProccessElement.getAttributeValue("Id");
        log.debug(" ===========> Codigo:"+txtCodigo+"\nDescripcion:"+txtDescripcion);
        DefinicionProcedimientosValueObject defProcVO = new DefinicionProcedimientosValueObject();
        defProcVO.setTxtCodigo(txtCodigo);
        defProcVO.setTxtDescripcion(txtDescripcion);
        defProcVO.setCodMunicipio(Integer.toString(codMunicipio));
        defProcVO.setTramiteInicio(tramiteInicioProcedimiento);
        defProcVO.setFechaLimiteDesde(fechaVigenciaDesde);
        defProcVO.setFechaLimiteHasta(fechaVigenciaHasta);
        defProcVO.setCodArea(areaProcedimiento);
        defProcVO.setDescArea(descripcionAreaProcedimiento);
        defProcVO.setCodTipoProcedimiento(tipoProcedimiento);
        defProcVO.setPlazo(plazoProcedimiento);
        defProcVO.setPorcentaje(porcentajePlazo);
        defProcVO.setTipoPlazo(tipoPlazo);
        defProcVO.setCodEstado(estadoProcedimiento);
        defProcVO.setLocalizacion(localizacionProc);
        defProcVO.setCodUnidadInicio(tipoUnidadInicioProc);
        defProcVO.setTipoSilencio(tipoSilencio);
        defProcVO.setDescripcionBreve(descripcionBreve);
        defProcVO.setCodTipoInicio(tipoInicioProc);
        defProcVO.setCodServicioFinalizacion(codPluginFinNoConvencional);
        defProcVO.setImplClassServicioFinalizacion(implClassPluginFinNoConvencional);

        log.debug(" ************** codigo servicio finalización: " + defProcVO.getCodServicioFinalizacion());
        log.debug(" ************** implClass servicio finalización: " + defProcVO.getImplClassServicioFinalizacion());

        /**
         * ** Tratando las unidades de inicio del expediente si las hay **
         */
        if (unidadesInicioProc != null) {
            Vector listaUnidadesInicio = new Vector();
            String[] unidades = unidadesInicioProc.split(ConstantesDatos.COMMA);
            for(int i=0;unidades!=null && i<unidades.length;i++){
                String unidad = unidades[i].trim();
                // codigo visible es alfanumérico, no solo un número.
               // if ((unidad).matches("[0-9]+")) {
                    listaUnidadesInicio.add(unidad);
               // }
            }

            if (listaUnidadesInicio != null && listaUnidadesInicio.size() > 0) {
                defProcVO.setListaCodUnidadInicio(listaUnidadesInicio);
            }
        }

        defProcVO.setTramitacionInternet(visibleTramitacion);
        defProcVO.setDisponible(dispWebCiudadano);
        defProcVO.setInteresadoOblig(interesOblig);
        ///
        // desarrollo lanbide
        //
        defProcVO.setRestringido(restringido);
        defProcVO.setBiblioteca(libreriaFlujo);
        defProcVO.setNumeracionExpedientesAnoAsiento(Integer.parseInt(numeroSegunAnotacion));
        defProcVO.setSoloWS(soloInicioWebService);
         ///
        // desarrollo lanbide
        //
        
        ArrayList<DocumentoExpedienteVO> documentos = new ArrayList<DocumentoExpedienteVO>();
         ArrayList<EnlaceProcedimientoVO> enlaces = new  ArrayList<EnlaceProcedimientoVO>();
        ArrayList<RolProcedimientoVO> roles = new  ArrayList<RolProcedimientoVO>();
        ArrayList<CampoSuplementarioVO> campos =  new ArrayList<CampoSuplementarioVO>();
        ArrayList<DefinicionAgrupacionCamposValueObject> agrupaciones = new ArrayList<DefinicionAgrupacionCamposValueObject>();
        ArrayList<FirmasDocumentoProcedimientoVO> firmasDocumentoProcedimiento = new ArrayList<FirmasDocumentoProcedimientoVO>();
        Hashtable<String,String> unidadesParticipantes = new Hashtable<String,String>();
        
        Element sdataFields = workflowProccessElement.getChild("DataFields",XMLNS);
        if(sdataFields!=null){
            List<Element> hijos = sdataFields.getChildren("DataField",XMLNS);

            for(Element hijo : hijos){
                String nombre = hijo.getAttributeValue("Name");
                Element extendedAttributes = hijo.getChild("ExtendedAttributes",XMLNS);
                List<Element> extendedAttributeList = extendedAttributes.getChildren("ExtendedAttribute",XMLNS);
                
                if (extendedAttributes != null) {
                    if  ("Documento_Procedimiento".equals(nombre)) {
                        documentos.add(documentosExpedienteFromXPDL(extendedAttributeList));
                    } else if("Enlace_Procedimiento".equals(nombre)){
                         enlaces.add(enlaceExpedienteFromXPDL(extendedAttributeList));
                    } else if("Roles_Procedimiento".equals(nombre)){
                         roles.add(rolProcedimientoFromXPDL(extendedAttributeList));
                    } else if("Campo_Suplementario_Procedimiento".equals(nombre)){
                        campos.add(campoSuplementarioExpedienteFromXPDL(extendedAttributeList));
                    } else if("Agrupacion_Campo_Suplementario_Procedimiento".equals(nombre)){
                        agrupaciones.add(agrupacionCamposSuplementariosFromXPDL(extendedAttributeList,null));
                    } else if("Firma_Documento_Procedimiento".equals(nombre)){
                        firmasDocumentoProcedimiento.add(getFirmasDocumentoProcedimientoFromXPDL(extendedAttributeList));
                    } else if("Unidad_Participante".equals(nombre)){
                        getUnidadesParticipantesFromXPDL(extendedAttributeList,unidadesParticipantes);
                    }
            }
        }
        }

        /**
         * *** Se recuperan los documentos del procedimiento del XPDL ********
         */
        if (documentos.size() > 0) {
            Vector listaCodigosDocumentos = new Vector();
            Vector listaCondicionesDocumentos = new Vector();
            Vector listaNombresDocumentos = new Vector();

            for(Iterator<DocumentoExpedienteVO> it = documentos.iterator();it.hasNext();){
                DocumentoExpedienteVO doc = it.next();
                listaCodigosDocumentos.add(doc.getCodigo());
                listaNombresDocumentos.add(doc.getNombre());
                listaCondicionesDocumentos.add(doc.getCondicion());
            }

            defProcVO.setListaNombresDoc(listaNombresDocumentos);
            defProcVO.setListaCodigosDoc(listaCodigosDocumentos);
            defProcVO.setListaCondicionDoc(listaCondicionesDocumentos);
        }// if

        /**
         * *** Se recuperan la enlaces del procedimiento del XPDL ********
         */
        if (enlaces.size() > 0) {
            Vector listaCodigosEnlaces = new Vector();
            Vector listaDescEnlaces = new Vector();
            Vector listaUrlEnlaces = new Vector();
            Vector listaEstadoEnlaces = new Vector();

            for(Iterator<EnlaceProcedimientoVO> it = enlaces.iterator();it.hasNext();){
                EnlaceProcedimientoVO doc = it.next();
                listaCodigosEnlaces.add(doc.getCodigo());
                listaDescEnlaces.add(doc.getDescripcion());
                listaUrlEnlaces.add(doc.getUrl());
                listaEstadoEnlaces.add(doc.getEstado());
            }

            defProcVO.setListaCodEnlaces(listaCodigosEnlaces);
            defProcVO.setListaDescEnlaces(listaDescEnlaces);
            defProcVO.setListaUrlEnlaces(listaUrlEnlaces);
            defProcVO.setListaEstadoEnlaces(listaEstadoEnlaces);
        }// if

        /**
         * *** Se recuperan los roles del procedimiento ********
         */
        if (roles.size() > 0) {
            Vector listaCodigosRoles = new Vector();
            Vector listaDescRoles = new Vector();
            Vector listaPorDefecto = new Vector();
            Vector listaConsultaWeb = new Vector();

            for(Iterator<RolProcedimientoVO> it = roles.iterator();it.hasNext();){
                RolProcedimientoVO rol = it.next();
                listaCodigosRoles.add(rol.getCodigo());
                listaDescRoles.add(rol.getDescripcion());
                listaPorDefecto.add(rol.getDefecto());
                listaConsultaWeb.add(rol.getEstado());
            }

            defProcVO.setListaCodRoles(listaCodigosRoles);
            defProcVO.setListaDescRoles(listaDescRoles);
            defProcVO.setListaPorDefecto(listaPorDefecto);
            defProcVO.setListaConsultaWebRol(listaConsultaWeb);
        }// if

        /**
         * *** Se recuperan los campos suplementarios del procedimiento ********
         */
        if (campos.size() > 0) {
            Vector listaCodigosCampos = new Vector();
            Vector listaDescCampos     = new Vector();
            Vector listaCodPlantillaCampos = new Vector();
            Vector listaCodTipoDatoCampos = new Vector();
            Vector listaTamano = new Vector();
            Vector listaMascara = new Vector();
            Vector listaObligatorio = new Vector();
            Vector listaRotulos = new Vector();
            Vector listaActivos = new Vector();
            Vector listaOcultos = new Vector();
            Vector listaBloqueados = new Vector();
            Vector listaPlazoFecha = new Vector();
            Vector listaCheckPlazoFecha = new Vector();
            Vector listaValidacion = new Vector();
            Vector listaOperacion = new Vector();
            Vector listaAgrupacionCampo = new Vector();
            Vector listaPosicionesX = new Vector();
            Vector listaPosicionesY = new Vector();

            for(Iterator<CampoSuplementarioVO> it = campos.iterator();it.hasNext();){
                CampoSuplementarioVO campo = it.next();
                
                listaCodigosCampos.add(campo.getCodigo());
                listaDescCampos.add(campo.getDescripcion());
                listaCodPlantillaCampos.add(campo.getCodigoPlantilla());
                listaCodTipoDatoCampos.add(campo.getCodigoTipoDato());
                listaTamano.add(campo.getTamano());
                listaMascara.add(campo.getMascara());
                listaObligatorio.add(campo.getObligatorio());
                listaRotulos.add(campo.getRotulo());
                listaActivos.add(campo.getActivo());
                listaOcultos.add(campo.getOculto());
                listaBloqueados.add(campo.getBloqueado());
                listaPlazoFecha.add(campo.getPlazoFecha());
                listaCheckPlazoFecha.add(campo.getCheckPlazoFecha());                
                listaValidacion.add(campo.getValidacion());                
                listaOperacion.add(campo.getOperacion());
                listaAgrupacionCampo.add(campo.getAgrupacionCampo());
                listaPosicionesX.add(campo.getPosicionesX());
                listaPosicionesY.add(campo.getPosicionesY());
            }

            defProcVO.setListaCodCampos(listaCodigosCampos);
            defProcVO.setListaDescCampos(listaDescCampos);
            defProcVO.setListaCodPlantilla(listaCodPlantillaCampos);
            defProcVO.setListaCodTipoDato(listaCodTipoDatoCampos);
            defProcVO.setListaTamano(listaTamano);
            defProcVO.setListaMascara(listaMascara);
            defProcVO.setListaRotulo(listaRotulos);
            defProcVO.setListaActivos(listaActivos);
            defProcVO.setListaObligatorio(listaObligatorio);
            defProcVO.setListaOcultos(listaOcultos);
            defProcVO.setListaBloqueados(listaBloqueados);
            defProcVO.setListaPlazoFecha(listaPlazoFecha);
            defProcVO.setListaCheckPlazoFecha(listaCheckPlazoFecha);
            defProcVO.setListaValidacion(listaValidacion);
            defProcVO.setListaOperacion(listaOperacion);
            defProcVO.setListaAgrupacionesCampo(listaAgrupacionCampo);
            defProcVO.setListaPosicionesX(listaPosicionesX);
            defProcVO.setListaPosicionesY(listaPosicionesY);            
        }// if

        /**
         * *** Se recuperan las agrupaciones de campos suplementarios del
         * procedimiento ********
         */
        if (agrupaciones.size() > 0) {
            Vector listaCodAgrupaciones = new Vector();
            Vector listaDescAgrupaciones     = new Vector();
            Vector listaOrdenAgrupaciones = new Vector();
            Vector listaAgrupacionesActivas = new Vector();

            for(Iterator<DefinicionAgrupacionCamposValueObject> it = agrupaciones.iterator();it.hasNext();){
                DefinicionAgrupacionCamposValueObject agrupacion = it.next();
                
                listaCodAgrupaciones.add(agrupacion.getCodAgrupacion());
                listaDescAgrupaciones.add(agrupacion.getDescAgrupacion());
                listaOrdenAgrupaciones.add(agrupacion.getOrdenAgrupacion());
                listaAgrupacionesActivas.add(agrupacion.getAgrupacionActiva());
            }

            defProcVO.setListaCodAgrupaciones(listaCodAgrupaciones);
            defProcVO.setListaDescAgrupaciones(listaDescAgrupaciones);
            defProcVO.setListaOrdenAgrupaciones(listaOrdenAgrupaciones);
            defProcVO.setListaAgrupacionesActivas(listaAgrupacionesActivas);
        }// if

        /**
         * SE RECUPERAN LAS DESCRIPCIONES DE LAS UNIDADES PARTICIPANTES *
         */
        defProcVO.setParticipantes(unidadesParticipantes);

        /**
         * Se recuperan los circuitos de firma de los documentos de
         * procedimeinto *
         */
        if (firmasDocumentoProcedimiento.size() > 0) {
            defProcVO.setFirmasDocumentosProcedimiento(firmasDocumentoProcedimiento);
        }

        return defProcVO;        
    }

    /**
     * Parsea un elemento activity para extraer los datos de un tramite
     *
     * @param activityElement: Elemento del xml que hace referencia a un trámite
     * @param workflowProccessElement: Elemento padre del xml del que cuelga la
     * definición de todo el procedimiento y de sus trámites
     * @param codMunicipio: Código del municipio
     * @param params: Parámetros de conexión a la base de datoss
     * @return un objeto DefinicionTramitesValueObject
     */
    public DefinicionTramitesValueObject tramitesFromXPDL(Element activityElement,Element workflowProccessElement,int codMunicipio,String[] params){
    
        DefinicionTramitesValueObject tramite = new DefinicionTramitesValueObject();
        String txtNombreTramite = activityElement.getAttributeValue("Name");
        String txtCodTramite      = activityElement.getAttributeValue("Id");

        tramite.setTxtCodigo(dpVO.getTxtCodigo());
        tramite.setTxtDescripcion(dpVO.getTxtDescripcion());
        tramite.setCodMunicipio(Integer.toString(codMunicipio));
        tramite.setNombreTramite(txtNombreTramite);
        tramite.setCodigoTramite(txtCodTramite);

        String tramiteInicio = null;
        String codigoVisible=null;
        String codigoClasificacionTramite = null;
        String plazoNotificacion = null;
        String plazoFin = null;
        String unidadesPlazo = null;        
        String codigoUnidadInicioManual = null;
        String descripcionUnidadInicioManual = null;
        String tipoUnidadTramite = null;
        String soloEsta = null;
        String codigoCargo = null;
        String codigoCargoVisible = null;
        String instrucciones = null;
        String generarPlazo = null;
        String notificarCercaFinPlazo = null;
        String notificarFueraPlazo = null;
        String tipoNoCercaFinPlazo = null;
        String tipoNoFueraPlazo = null;
        String unidadesTramitadoras = null;

        String casoObligatorioFavorable = null;
        String casoObligatorioDesfavorable = null;
        String tipoAccion = null;
        String tipoAccionAfirmativa = null;
        String tipoAccionNegativa = null;
        String pregunta = null;
        
        String notUnidadTramitIni = null;
        String notUnidadTramitFin = null;
        String notUsuUnidadTramitIni = null;
        String notUsuUnidadTramitFin = null;
        String notInteresadosIni = null;
        String notInteresadosFin = null;
        String codigoExpedienteRelacionado = null;
        String visibleInternet = null;
        
        String admiteNotificacionElectronica = null;
        String codNotificacionElectronica = null;
        String tipoFirmaElectronicaNotificacion = null;
        String codUsuarioFirmaNotificacion = null;
        
        // Trámites con notificaciones
        String tramiteNotificaco = null;
        String codigoDepartamento = null;
        String descripcionDepartamento = null;
        String notificacionObligatoria = null;
        String certificadoOrganismo = null;

        Element extendedAttributesElement = activityElement.getChild("ExtendedAttributes", XMLNS);
        List extendedAttributes = extendedAttributesElement == null ? null : extendedAttributesElement.getChildren("ExtendedAttribute", XMLNS);
        if (extendedAttributes != null) {
            for (int i = 0; i < extendedAttributes.size(); i++) {
                Element e = (Element) extendedAttributes.get(i);
                String nombreAtributo = e.getAttributeValue("Name");
                String valorAtributo = e.getAttributeValue("Value");

                if (nombreAtributo.equals("es_tramite_inicio") && valorAtributo != null && valorAtributo.length() > 0) {
                    tramiteInicio = valorAtributo;
                } else if (nombreAtributo.equals("codigo_visible") && valorAtributo != null && valorAtributo.length() > 0) {
                    codigoVisible = valorAtributo;
                } else if (nombreAtributo.equals("codigo_clasificacion_tramite") && valorAtributo != null && valorAtributo.length() > 0) {
                    codigoClasificacionTramite = valorAtributo;
                } else if (nombreAtributo.equals("plazo_notificacion") && valorAtributo != null && valorAtributo.length() > 0) {
                    plazoNotificacion = valorAtributo;
                } else if (nombreAtributo.equals("plazo_fin") && valorAtributo != null && valorAtributo.length() > 0) {
                    plazoFin = valorAtributo;
                } else if (nombreAtributo.equals("unidades_plazo") && valorAtributo != null && valorAtributo.length() > 0) {
                    unidadesPlazo = valorAtributo;
                } else if (nombreAtributo.equals("codigo_unidad_inicio_manual") && valorAtributo != null && valorAtributo.length() > 0) {
                    codigoUnidadInicioManual = valorAtributo;
                } else if (nombreAtributo.equals("descripcion_unidad_inicio_manual") && valorAtributo != null && valorAtributo.length() > 0) {
                    descripcionUnidadInicioManual = valorAtributo;
                } else if (nombreAtributo.equals("tipo_unidad_tramite") && valorAtributo != null && valorAtributo.length() > 0) {
                    tipoUnidadTramite = valorAtributo;
                } else if (nombreAtributo.equals("solo_esta") && valorAtributo != null && valorAtributo.length() > 0) {
                    soloEsta = valorAtributo;
                } else if (nombreAtributo.equals("codigo_cargo") && valorAtributo != null && valorAtributo.length() > 0) {
                    codigoCargo = valorAtributo;
                } else if (nombreAtributo.equals("codigo_cargo_visible") && valorAtributo != null && valorAtributo.length() > 0) {
                    codigoCargoVisible = valorAtributo;
                } else if (nombreAtributo.equals("instrucciones") && valorAtributo != null && valorAtributo.length() > 0) {
                    instrucciones = valorAtributo;
                } else if (nombreAtributo.equals("generar_plazo") && valorAtributo != null && valorAtributo.length() > 0) {
                    generarPlazo = valorAtributo;
                } else if (nombreAtributo.equals("notificar_cerca_fin_plazo") && valorAtributo != null && valorAtributo.length() > 0) {
                    notificarCercaFinPlazo = valorAtributo;
                } else if (nombreAtributo.equals("notificar_fuera_plazo") && valorAtributo != null && valorAtributo.length() > 0) {
                    notificarFueraPlazo = valorAtributo;
                } else if (nombreAtributo.equals("tipo_no_cerca_fin_plazo") && valorAtributo != null && valorAtributo.length() > 0) {
                    tipoNoCercaFinPlazo = valorAtributo;
                } else if (nombreAtributo.equals("tipo_no_fuera_plazo") && valorAtributo != null && valorAtributo.length() > 0) {
                    tipoNoFueraPlazo = valorAtributo;
                } else if (nombreAtributo.equals("unidades_tramitadoras") && valorAtributo != null && valorAtributo.length() > 0) {
                    unidadesTramitadoras = valorAtributo;
                } else if (nombreAtributo.equals("caso_obligatorio_favorable") && valorAtributo != null && valorAtributo.length() > 0) {
                    casoObligatorioFavorable = valorAtributo;
                } else if (nombreAtributo.equals("caso_obligatorio_desfavorable") && valorAtributo != null && valorAtributo.length() > 0) {
                    casoObligatorioDesfavorable = valorAtributo;
                } else if (nombreAtributo.equals("tipo_accion") && valorAtributo != null && valorAtributo.length() > 0) {
                    tipoAccion = valorAtributo;
                } else if (nombreAtributo.equals("tipo_accion_afirmativa") && valorAtributo != null && valorAtributo.length() > 0) {
                    tipoAccionAfirmativa = valorAtributo;
                } else if (nombreAtributo.equals("tipo_accion_negativa") && valorAtributo != null && valorAtributo.length() > 0) {
                    tipoAccionNegativa = valorAtributo;
                } else if (nombreAtributo.equals("pregunta") && valorAtributo != null && valorAtributo.length() > 0) {
                    pregunta = valorAtributo;
                } else if (nombreAtributo.equals("not_unidad_tramit_ini") && valorAtributo != null && valorAtributo.length() > 0) {
                    notUnidadTramitIni = valorAtributo;
                } else if (nombreAtributo.equals("not_unidad_tramit_fin") && valorAtributo != null && valorAtributo.length() > 0) {
                    notUnidadTramitFin = valorAtributo;
                } else if (nombreAtributo.equals("not_usu_unidad_tramit_ini") && valorAtributo != null && valorAtributo.length() > 0) {
                    notUsuUnidadTramitIni = valorAtributo;
                } else if (nombreAtributo.equals("not_usu_unidad_tramit_fin") && valorAtributo != null && valorAtributo.length() > 0) {
                    notUsuUnidadTramitFin = valorAtributo;
                } else if (nombreAtributo.equals("not_interesados_ini") && valorAtributo != null && valorAtributo.length() > 0) {
                    notInteresadosIni = valorAtributo;
                } else if (nombreAtributo.equals("not_interesados_fin") && valorAtributo != null && valorAtributo.length() > 0) {
                    notInteresadosFin = valorAtributo;
                } else if (nombreAtributo.equals("codigo_expediente_relacionado") && valorAtributo != null && valorAtributo.length() > 0) {
                    codigoExpedienteRelacionado = valorAtributo;
                } else if (nombreAtributo.equals("visible_internet") && valorAtributo != null && valorAtributo.length() > 0) {
                    visibleInternet = valorAtributo;
                    // Trámites con notificaciones
                } else if (nombreAtributo.equals("tramite_notificado") && !StringUtils.isNullOrEmptyString(valorAtributo)) {
                    tramiteNotificaco = valorAtributo;
                } else if (nombreAtributo.equals("admite_notificacion_electronica") && valorAtributo != null && valorAtributo.length() > 0) {
                    admiteNotificacionElectronica = valorAtributo;
                } else if (nombreAtributo.equals("codigo_tipo_notificacion_electronica") && !StringUtils.isNullOrEmptyString(valorAtributo)) {
                    codNotificacionElectronica = valorAtributo;

                } else if (nombreAtributo.equals("codigo_departamento") && !StringUtils.isNullOrEmptyString(valorAtributo)) {
                    codigoDepartamento = valorAtributo;
                } else if (nombreAtributo.equals("descripcion_departamento") && !StringUtils.isNullOrEmptyString(valorAtributo)) {
                    descripcionDepartamento = valorAtributo;
                } else if (nombreAtributo.equals("notificacion_obligatoria") && !StringUtils.isNullOrEmptyString(valorAtributo)) {
                    notificacionObligatoria = valorAtributo;
                } else if (nombreAtributo.equals("certificado_organismo") && !StringUtils.isNullOrEmptyString(valorAtributo)) {
                    certificadoOrganismo = valorAtributo;

                } else if (nombreAtributo.equals("tipo_firma_electronica") && valorAtributo != null && valorAtributo.length() > 0) {
                    tipoFirmaElectronicaNotificacion = valorAtributo;
                } else if (nombreAtributo.equals("cod_usuario_firma_notificacion") && valorAtributo != null && valorAtributo.length() > 0) {
                    codUsuarioFirmaNotificacion = valorAtributo;
                }
            }
        }

        tramite.setTramiteInicio(tramiteInicio);
        tramite.setNumeroTramite(codigoVisible);
        tramite.setInstrucciones(instrucciones);
        tramite.setCodClasifTramite(codigoClasificacionTramite);
        tramite.setPlazo(plazoNotificacion);
        if (plazoFin != null && plazoFin.length() > 0 && plazoFin.matches("[0-9]{1,3}")) {
            tramite.setPlazoFin(Integer.parseInt(plazoFin));
        }
        tramite.setUnidadesPlazo(unidadesPlazo);
        
        
        // Trámites con notificaciones
        if ("1".equals(tramiteNotificaco)) {
            tramite.setTramiteNotificado(Boolean.TRUE);

            if ("1".equals(admiteNotificacionElectronica)) {
                tramite.setAdmiteNotificacionElectronica("1");
                tramite.setCodigoTipoNotificacionElectronica(codNotificacionElectronica);
                tramite.setCodDepartamentoNotificacion(codigoDepartamento);
                tramite.setDescripcionDepartamentoNotificacion(descripcionDepartamento);
            }
            if (notificacionObligatoria != null) {
                tramite.setNotificacionElectronicaObligatoria("1".equals(notificacionObligatoria) ? Boolean.TRUE : Boolean.FALSE);
            }

            if (notificacionObligatoria != null) {
                tramite.setCertificadoOrganismoFirmaNotificacion("1".equals(certificadoOrganismo) ? Boolean.TRUE : Boolean.FALSE);
            }
            // si Tra notificado es false todos los demás datos no pueden aplicar.
        } else {
            tramite.setTramiteNotificado(Boolean.FALSE);
            tramite.setAdmiteNotificacionElectronica("0");
            tramite.setCodigoTipoNotificacionElectronica(null);
            tramite.setCodDepartamentoNotificacion(null);
            tramite.setDescripcionDepartamentoNotificacion(null);
            tramite.setNotificacionElectronicaObligatoria(Boolean.FALSE);
            tramite.setCertificadoOrganismoFirmaNotificacion(Boolean.FALSE);
        }

        tramite.setTipoUsuarioFirma(tipoFirmaElectronicaNotificacion);
        tramite.setCodigoOtroUsuarioFirma(codUsuarioFirmaNotificacion);

        if (codigoUnidadInicioManual == null || "".equals(codigoUnidadInicioManual)) {
            tramite.setCodUnidadInicio(null);
        } else {
            tramite.setCodUnidadInicio(codigoUnidadInicioManual);
        }

        tramite.setDescUnidadInicio(descripcionUnidadInicioManual);
        tramite.setCodUnidadTramite(tipoUnidadTramite);
        tramite.setTramitePregunta(soloEsta);
        tramite.setCodCargo(codigoCargo);
        tramite.setCodVisibleCargo(codigoCargoVisible);
        tramite.setInstrucciones(instrucciones);
        if (generarPlazo != null && "true".equals(generarPlazo)) {
            tramite.setGenerarPlazos(true);
        } else {
            if (generarPlazo != null && "false".equals(generarPlazo)) {
                tramite.setGenerarPlazos(false);
            }
        }

        if (notificarCercaFinPlazo != null && "true".equals(notificarCercaFinPlazo)) {
            tramite.setNotificarCercaFinPlazo(true);
        } else {
            tramite.setNotificarCercaFinPlazo(false);
        }

        if (notificarFueraPlazo != null && "true".equals(notificarFueraPlazo)) {
            tramite.setNotificarFueraDePlazo(true);
        } else {
            if (notificarFueraPlazo != null && "false".equals(notificarFueraPlazo)) {
                tramite.setNotificarFueraDePlazo(false);
            }
        }

        if (tipoNoCercaFinPlazo != null) {
            tramite.setTipoNotCercaFinPlazo(Integer.parseInt(tipoNoCercaFinPlazo));
        }

        if (tipoNoFueraPlazo != null) {
            tramite.setTipoNotFueraDePlazo(Integer.parseInt(tipoNoFueraPlazo));
        }

        if (visibleInternet != null) {
            tramite.setDisponible(visibleInternet);
        }

        Vector<UORDTO> unidades = new Vector<UORDTO>();
        if(unidadesTramitadoras!=null && !"".equals(unidadesTramitadoras)){
            String[] unid = unidadesTramitadoras.split(ConstantesDatos.COMMA);            
            for(int i=0;unid!=null && i<unid.length;i++){
                UORDTO uor = new UORDTO();
                uor.setUor_cod_vis(unid[i]);                
                unidades.add(uor);
            }            
        }

        tramite.setUnidadesTramitadoras(unidades);
        if (casoObligatorioFavorable != null) {
            tramite.setObligatorio(casoObligatorioFavorable);
        }
        if (casoObligatorioDesfavorable != null) {
            tramite.setObligatorioDesf(casoObligatorioDesfavorable);
        }

        if ("T".equals(tipoAccion)) {
            tramite.setTipoCondicion("Tramite");
        } else if ("F".equals(tipoAccion)) {
            tramite.setTipoCondicion("Finalizacion");
        } else if ("P".equals(tipoAccion)) {
            tramite.setTipoCondicion("Pregunta");
        } else if ("R".equals(tipoAccion)) {
            tramite.setTipoCondicion("Resolucion");
        } else {
            tramite.setTipoCondicion("sinCondicion");
        }

        if ("P".equals(tipoAccion) || "R".equals(tipoAccion)) {
            if ("T".equals(tipoAccionAfirmativa)) {
                tramite.setTipoFavorableSI("T");
            } else if ("F".equals(tipoAccionAfirmativa)) {
                tramite.setTipoFavorableSI("F");
            }

            if ("T".equals(tipoAccionNegativa)) {
                tramite.setTipoFavorableNO("T");
            } else if ("F".equals(tipoAccionNegativa)) {
                tramite.setTipoFavorableNO("F");
            }
        }
        if (pregunta == null) {
            tramite.setTexto("");
        } else {
            tramite.setTexto(pregunta);
        }

        if (notUnidadTramitIni != null) {
            tramite.setNotUnidadTramitIni(notUnidadTramitIni);
        }
        if (notUnidadTramitFin != null) {
            tramite.setNotUnidadTramitFin(notUnidadTramitFin);
        }
        if (notUsuUnidadTramitIni != null) {
            tramite.setNotUsuUnidadTramitIni(notUsuUnidadTramitIni);
        }
        if (notUsuUnidadTramitFin != null) {
            tramite.setNotUsuUnidadTramitFin(notUsuUnidadTramitFin);
        }
        if (notInteresadosIni != null) {
            tramite.setNotInteresadosIni(notInteresadosIni);
        }
        if (notInteresadosFin != null) {
            tramite.setNotInteresadosFin(notInteresadosFin);
        }
        if (codigoExpedienteRelacionado != null) {
            tramite.setCodExpRel(codigoExpedienteRelacionado);
        }

        ArrayList<EnlaceTramiteVO> enlaces = new ArrayList<EnlaceTramiteVO>();
        ArrayList<CampoSuplementarioTramiteVO> campos = new ArrayList<CampoSuplementarioTramiteVO>();
        ArrayList<DefinicionAgrupacionCamposValueObject> agrupaciones = new ArrayList<DefinicionAgrupacionCamposValueObject>();
        ArrayList<CondicionEntradaTramiteVO> condiciones = new ArrayList<CondicionEntradaTramiteVO>();
        ArrayList<DocumentoTramiteVO> documentos = new ArrayList<DocumentoTramiteVO>();
        
        Element sdataFields = workflowProccessElement.getChild("DataFields",XMLNS);
        if(sdataFields!=null){
            List hijos = sdataFields.getChildren("DataField",XMLNS);

            for (int i=0;hijos!=null && i<hijos.size();i++) {
                Element hijo = (Element)hijos.get(i);
                String nombre = hijo.getAttributeValue("Name");
                Element atributosExtendidos = hijo.getChild("ExtendedAttributes", XMLNS);
                List<Element> listaAtributosExtendidos = atributosExtendidos.getChildren("ExtendedAttribute", XMLNS);
                if (listaAtributosExtendidos != null) {
                    if ("Enlace_Tramite".equals(nombre)) {
                        EnlaceTramiteVO enlaceAux = enlaceTramiteFromXPDL(listaAtributosExtendidos, txtCodTramite);
                        if (enlaceAux != null) {
                            enlaces.add(enlaceAux);
                        }
                    } else if ("Campo_Suplementario_Tramite".equals(nombre)) {
                        CampoSuplementarioTramiteVO campoAux = campoSuplementarioTramiteFromXPDL(listaAtributosExtendidos, txtCodTramite);
                        if (campoAux != null) {
                            campos.add(campoAux);
                        }
                    } else if ("Agrupacion_Campo_Suplementario_Tramite".equals(nombre)) {
                        DefinicionAgrupacionCamposValueObject agrupacionAux = agrupacionCamposSuplementariosFromXPDL(listaAtributosExtendidos, txtCodTramite);
                        if (agrupacionAux != null) {
                            agrupaciones.add(agrupacionAux);
                        }
                    } else if ("Condicion_Entrada_Tramite".equals(nombre)) {
                        CondicionEntradaTramiteVO condicionAux = condicionesEntradaTramiteFromXPDL(listaAtributosExtendidos, txtCodTramite);
                        if (condicionAux != null) {
                            condiciones.add(condicionAux);
                        }
                    } else if ("Documento_Tramite".equals(nombre)) {
                        DocumentoTramiteVO docAux = documentosTramiteFromXPDL(listaAtributosExtendidos, txtCodTramite);
                        if (docAux != null) {
                            documentos.add(docAux);
                        }
                    }
                }
            }
        }

        /**
         * ********************* ENLACES DEL TRÁMITE *****************
         */
        Vector listaCodigosEnlaces = new Vector();
        Vector listaDescripcionEnlaces = new Vector();
        Vector listaUrlEnlaces = new Vector();
        Vector listaEstadoEnlaces = new Vector();
        
        for(int i=0;i<enlaces.size();i++){
            EnlaceTramiteVO enlace = enlaces.get(i);
            listaCodigosEnlaces.add(enlace.getCodigo());
            listaDescripcionEnlaces.add(enlace.getDescripcion());
            listaUrlEnlaces.add(enlace.getUrl());
            listaEstadoEnlaces.add(enlace.getEstado());
        }

        tramite.setListaCodigoEnlaces(listaCodigosEnlaces);
        tramite.setListaDescripcionEnlaces(listaDescripcionEnlaces);
        tramite.setListaUrlEnlaces(listaUrlEnlaces);
        tramite.setListaEstadoEnlaces(listaEstadoEnlaces);

        /**
         * ************ CAMPOS SUPLEMENTARIOS DEL TRÁMITE **************************
         */
        Vector listaCodCampos = new Vector();
        Vector listaDescripcionCampos = new Vector();
        Vector listaCodPlantillas = new Vector();
        Vector listaCodTipoDato = new Vector();
        Vector listaTamano = new Vector();
        Vector listaMascara = new Vector();
        Vector listaObligatorio = new Vector();
        Vector listaOrden = new Vector();
        Vector listaRotulo = new Vector();
        Vector listaVisible = new Vector();
        Vector listaActivo = new Vector();
        Vector listaOculto = new Vector();
        Vector listaBloqueado = new Vector();
        Vector listaPlazoFecha = new Vector();
        Vector listaCheckPlazoFecha = new Vector();
        Vector listaValidacion = new Vector();
        Vector listaOperacion = new Vector();
        Vector listaCodAgrupacionCampo = new Vector();
        Vector listaPosX = new Vector();
        Vector listaPosY = new Vector();

        for(int i=0;i<campos.size();i++){
            CampoSuplementarioTramiteVO campo = campos.get(i);
            listaCodCampos.add(campo.getCodigo());
            listaDescripcionCampos.add(campo.getDescripcion());
            listaCodPlantillas.add(campo.getCodigoPlantilla());
            listaCodTipoDato.add(campo.getCodigoTipoDato());
            listaTamano.add(campo.getTamano());
            listaMascara.add(campo.getMascara());
            listaObligatorio.add(campo.getObligatorio());
            listaOrden.add(campo.getOrden());
            listaRotulo.add(campo.getRotulo());
            listaVisible.add(campo.getVisible());
            listaActivo.add(campo.getActivo());
            listaOculto.add(campo.getOculto());
            listaBloqueado.add(campo.getBloqueado());
            listaPlazoFecha.add(campo.getPlazoFecha());
            listaCheckPlazoFecha.add(campo.getCheckPlazoFecha());
            listaValidacion.add(campo.getValidacion());
            listaOperacion.add(campo.getOperacion());
            listaCodAgrupacionCampo.add(campo.getAgrupacionCampo());
            listaPosX.add(campo.getListaPosicionX());
            listaPosY.add(campo.getListaPosicionY());
        }//for

        tramite.setListaCodCampos(listaCodCampos);
        tramite.setListaDescCampos(listaDescripcionCampos);
        tramite.setListaCodPlantill(listaCodPlantillas);
        tramite.setListaCodTipoDato(listaCodTipoDato);
        tramite.setListaTamano(listaTamano);
        tramite.setListaMascara(listaMascara);
        tramite.setListaObligatorio(listaObligatorio);
        tramite.setListaOrden(listaOrden);
        tramite.setListaRotulo(listaRotulo);
        tramite.setListaVisible(listaVisible);
        tramite.setListaActivo(listaActivo);      
        tramite.setListaOcultos(listaOculto);      
        tramite.setListaBloqueados(listaBloqueado);      
        tramite.setListaPlazoFecha(listaPlazoFecha);
        tramite.setListaCheckPlazoFecha(listaCheckPlazoFecha);
        tramite.setListaValidacion(listaValidacion);
        tramite.setListaOperacion(listaOperacion);
        tramite.setListaCodAgrupacionCampo(listaCodAgrupacionCampo);
        tramite.setListaPosX(listaPosX);
        tramite.setListaPosY(listaPosY);
        /**
         * ******************************************************************************
         */

        /**
         * ********* Agrupaciones de campos suplementarios del trámite ********
         */
        Vector listaCodAgrupaciones = new Vector();
        Vector listaDescAgrupaciones     = new Vector();
        Vector listaOrdenAgrupaciones = new Vector();
        Vector listaAgrupacionesActivas = new Vector();

        for(Iterator<DefinicionAgrupacionCamposValueObject> it = agrupaciones.iterator();it.hasNext();){
            DefinicionAgrupacionCamposValueObject agrupacion = it.next();

            listaCodAgrupaciones.add(agrupacion.getCodAgrupacion());
            listaDescAgrupaciones.add(agrupacion.getDescAgrupacion());
            listaOrdenAgrupaciones.add(agrupacion.getOrdenAgrupacion());
            listaAgrupacionesActivas.add(agrupacion.getAgrupacionActiva());
        }

        tramite.setListaCodAgrupacion(listaCodAgrupaciones);
        tramite.setListaDescAgrupacion(listaDescAgrupaciones);
        tramite.setListaOrdenAgrupacion(listaOrdenAgrupaciones);
        tramite.setListaAgrupacionActiva(listaAgrupacionesActivas);
        /**
         * ******************************************************************************
         */

        /**
         * ********************** CONDICIONES DE ENTRADA *******************************
         */
        Vector listaTramitesTablaEntrada = new Vector();
        Vector listaCodTramitesTablaEntrada = new Vector();
        Vector listaEstadoTablaEntrada = new Vector();
        Vector listaTipoTablaEntrada = new Vector();
        Vector listaExpresionesTablaEntrada = new Vector();
        Vector listaCodigoDocumentoEntrada = new Vector();

        for(int i=0;i<condiciones.size();i++){
            CondicionEntradaTramiteVO condicion = condiciones.get(i);            
            listaTipoTablaEntrada.add(condicion.getTipoCondicionEntrada());
            listaTramitesTablaEntrada.add(condicion.getCodigoTramiteOrigen()); 

            if (condicion.getIdTramiteCondTramite() != null) {
                listaCodTramitesTablaEntrada.add(condicion.getIdTramiteCondTramite());
            } else {
                listaCodTramitesTablaEntrada.add("");
            }

            if (condicion.getExpresionCondicion() != null) {
                listaExpresionesTablaEntrada.add(condicion.getExpresionCondicion());
            } else {
                listaExpresionesTablaEntrada.add("");
            }

            if (condicion.getEstadoCodTramiteCondTramite() != null) {
                listaEstadoTablaEntrada.add(condicion.getEstadoCodTramiteCondTramite());
            } else {
                listaEstadoTablaEntrada.add("");
            }
            if (condicion.getCodDocumento() != null) {
                listaCodigoDocumentoEntrada.add(condicion.getCodDocumento());
            } else {
                listaCodigoDocumentoEntrada.add("");
            }
        }// for

        tramite.setListaTramitesTabla(listaTramitesTablaEntrada);
        tramite.setListaCodTramitesTabla(listaCodTramitesTablaEntrada);
        tramite.setListaEstadosTabla(listaEstadoTablaEntrada);
        tramite.setListaTiposTabla(listaTipoTablaEntrada);
        tramite.setListaExpresionesTabla(listaExpresionesTablaEntrada);
        tramite.setListaCodigosDocTabla(listaCodigoDocumentoEntrada);
        /**
         * ******************************************************************************
         */

        /**
         * *********************** DOCUMENTOS DE TRAMITACION ****************************
         */
        // Los datos de todos los documento se anaden al objeto tramite como listas independientes (codigos por un lado, nombres por otro, etc)
        Vector listaCodigosDoc = new Vector();
        Vector listaNombresDoc = new Vector();
        Vector listaVisibleDoc = new Vector();
        Vector listaPlantillaDoc = new Vector();
        Vector listaCodPlantilla = new Vector();
        Vector listaContPlantilla = new Vector();
        Vector listaRelPlantilla = new Vector();
        Vector listaIntPlantilla = new Vector();
        Vector listaCodTipoDoc = new Vector();
        Vector listaFirmaDoc = new Vector();
        Vector listaDocActivos = new Vector();
        Vector listaEditoresTexto = new Vector();
        List<String> listaFirmaDocumentoIdsUsuario = new ArrayList<String>();
        List<String> listaFirmaDocumentoLogsUsuario = new ArrayList<String>();
        List<String> listaFirmadocumentoDnisUsuario = new ArrayList<String>();
        List<FirmaFlujoVO> listaFirmasFlujo = new ArrayList<FirmaFlujoVO>();
        List<FirmaCircuitoVO> listaFirmasCircuito = new ArrayList<FirmaCircuitoVO>();

        for (DocumentoTramiteVO documento : documentos) {
            listaCodigosDoc.add(documento.getCodigo());
            listaNombresDoc.add(documento.getNombre());
            listaVisibleDoc.add(documento.getVisibleInternet());
            listaPlantillaDoc.add(documento.getPlantilla());
            listaCodPlantilla.add(documento.getCodPlantilla());
            listaContPlantilla.add(documento.getContPlantilla());
            listaRelPlantilla.add(documento.getRelacion());
            listaIntPlantilla.add(documento.getInteresado());
            listaCodTipoDoc.add(documento.getCodTipoDocumento());
            listaFirmaDoc.add(documento.getFirma());
            listaDocActivos.add(documento.getDocActivo());
            listaEditoresTexto.add(documento.getEditorTexto());
            listaFirmaDocumentoIdsUsuario.add(documento.getFirmaDocumentoIdUsuario());
            listaFirmaDocumentoLogsUsuario.add(documento.getFirmaDocumentoLogUsuario());
            listaFirmadocumentoDnisUsuario.add(documento.getFirmadocumentoDniUsuario());
            listaFirmasFlujo.add(documento.getFirmaFlujo());
            listaFirmasCircuito.add(documento.getFirmaCircuito());
        }
        
        tramite.setListaFirmasDocumentoIdsUsuario(listaFirmaDocumentoIdsUsuario);
        tramite.setListaFirmasDocumentoLogsUsuario(listaFirmaDocumentoLogsUsuario);
        tramite.setListaFirmasDocumentoDnisUsuario(listaFirmadocumentoDnisUsuario);
        tramite.setListaFirmasFlujo(listaFirmasFlujo);
        tramite.setListaFirmasCircuito(listaFirmasCircuito);
        tramite.setListaCodigosDoc(listaCodigosDoc);
        tramite.setListaNombresDoc(listaNombresDoc);
        tramite.setListaVisibleDoc(listaVisibleDoc);
        tramite.setListaPlantillaDoc(listaPlantillaDoc);
        tramite.setListaCodPlantilla(listaCodPlantilla);
        tramite.setListaContidoPlantilla(listaContPlantilla);
        tramite.setListaInteresadoPlantilla(listaIntPlantilla);
        tramite.setListaRelacionPlantilla(listaRelPlantilla);
        tramite.setListaCodTipoDoc(listaCodTipoDoc);
        tramite.setListaFirmaDoc(listaFirmaDoc);
        tramite.setListaDocActivos(listaDocActivos);
        tramite.setListaEditoresTexto(listaEditoresTexto);

        return tramite;
    }

    /**
     * Se recuperan los datos de los flujos de firma y los circuitos de estos
     * del XPDL
     *
     * @param workflowProccessElement
     * @return
     */
    public List<FirmaFlujoVO> flujosYCircuitosFirmasFromXPDL(Element workflowProccessElement) {
        List<FirmaFlujoVO> listaFlujos = null;

        Element sdataFields = workflowProccessElement.getChild("DataFields", XMLNS);
        if (sdataFields != null) {
            List<Element> listaDataFields = sdataFields.getChildren("DataField", XMLNS);

            List<Element> listaDataFieldsFlujo = new ArrayList<Element>();
            List<Element> listaDataFieldsCirc = new ArrayList<Element>();
            Map<Integer, List<Element>> mapaDFCircuitoPorFlujo = new HashMap<Integer, List<Element>>();
            Integer idFlujoAnterior = -1;
            // Hacemos una lista con los dataField Flujo_Firma 
            // y un map con la lista de dataFields Circuito_Firma agrupados por id de flujo
            for (Element dataField : listaDataFields) {
                String dFNombre = dataField.getAttributeValue("Name");
                if ("Flujo_Firma".equals(dFNombre)) {
                    listaDataFieldsFlujo.add(dataField);
                } else if ("Circuito_Flujo_Firma".equals(dFNombre)) {
                    String dFId = dataField.getAttributeValue("Id");
                    Integer idFlujo = obtenerIdFlujo(dFId);
                    if (!idFlujo.equals(idFlujoAnterior)) {
                        if (!listaDataFieldsCirc.isEmpty()) {
                            mapaDFCircuitoPorFlujo.put(idFlujoAnterior, listaDataFieldsCirc);
                        }
                        listaDataFieldsCirc = new ArrayList<Element>();
                        listaDataFieldsCirc.add(dataField);
                        idFlujoAnterior = idFlujo;
                    } else {
                        listaDataFieldsCirc.add(dataField);
                    }
                }
            }
            if (!listaDataFieldsCirc.isEmpty()) {
                mapaDFCircuitoPorFlujo.put(idFlujoAnterior, listaDataFieldsCirc);
            }

            // Recuperamos los objetos FirmaFlujoVO con la lista de objetos FirmaCircuitoVO correspondiente en cada caso
            listaFlujos = flujosFirmasFromXPDL(listaDataFieldsFlujo);
            circuitosFirmasFromXPDL(mapaDFCircuitoPorFlujo, listaFlujos);

        }

        return listaFlujos;
    }

    /**
     * Se recuperan los datos de flujos de firmas del XPDL
     */
    private List<FirmaFlujoVO> flujosFirmasFromXPDL(List<Element> listaDataFieldsFlujo) {
        Element atributosExtendidos = null;
        List<Element> listaAtributosExtendidos = null;
        List<FirmaFlujoVO> listaFlujos = new ArrayList<FirmaFlujoVO>();
        FirmaFlujoVO flujo = null;
        for (Element dataField : listaDataFieldsFlujo) {
            atributosExtendidos = dataField.getChild("ExtendedAttributes", XMLNS);
            listaAtributosExtendidos = atributosExtendidos.getChildren("ExtendedAttribute", XMLNS);
            if (listaAtributosExtendidos != null) {
                flujo = new FirmaFlujoVO();
                for (Element atrExt : listaAtributosExtendidos) {
                    String nombreElemento = atrExt.getAttributeValue("Name");
                    if ("firmaFlujoId".equals(nombreElemento)) {
                        flujo.setId(Integer.parseInt(atrExt.getAttributeValue("Value")));
                    } else if ("firmaFlujoNombre".equals(nombreElemento)) {
                        flujo.setNombre(atrExt.getAttributeValue("Value"));
                    } else if ("firmaFlujoIdTipo".equals(nombreElemento)) {
                        flujo.setIdTipoFirma(Integer.parseInt(atrExt.getAttributeValue("Value")));
                    } else if ("firmaFlujoActivo".equals(nombreElemento)) {
                        flujo.setActivo(atrExt.getAttributeValue("Value").equals("1"));
                    }
                }
                listaFlujos.add(flujo);
            }
        }

        return listaFlujos;
    }

    /**
     * Se recuperan los datos de flujos de firmas del XPDL
     */
    private void circuitosFirmasFromXPDL(Map<Integer, List<Element>> mapaDFCircuitoPorFlujo, List<FirmaFlujoVO> listaFlujos) {
        List<FirmaCircuitoVO> listaCircuitos = null;
        FirmaCircuitoVO circuito = null;
        List<Element> listaDataFieldsCirc = null;
        Element atributosExtendidos = null;
        List<Element> listaAtributosExtendidos = null;

        for (FirmaFlujoVO flujo : listaFlujos) {
            listaCircuitos = new ArrayList<FirmaCircuitoVO>();
            Integer flujoId = flujo.getId();
            listaDataFieldsCirc = mapaDFCircuitoPorFlujo.get(flujoId);
            if (listaDataFieldsCirc != null){
                for (Element dataField : listaDataFieldsCirc) {
                    circuito = new FirmaCircuitoVO();
                    atributosExtendidos = dataField.getChild("ExtendedAttributes", XMLNS);
                    listaAtributosExtendidos = atributosExtendidos.getChildren("ExtendedAttribute", XMLNS);
                    if (listaAtributosExtendidos != null) {
                        for (Element atrExt : listaAtributosExtendidos) {
                            String nombreElemento = atrExt.getAttributeValue("Name");
                            if ("firmaFlujoId".equals(nombreElemento)) {
                                circuito.setIdFlujoFirma(Integer.parseInt(atrExt.getAttributeValue("Value")));
                            } else if ("firmaCircuitoIdUsuario".equals(nombreElemento)) {
                                circuito.setIdUsuario(Integer.parseInt(atrExt.getAttributeValue("Value")));
                            } else if ("firmaCircuitoLogUsuario".equals(nombreElemento)) {
                                circuito.setLogUsuario(atrExt.getAttributeValue("Value"));
                            } else if ("firmaCircuitoOrden".equals(nombreElemento)) {
                                circuito.setOrden(Integer.parseInt(atrExt.getAttributeValue("Value")));
                            } else if ("firmaCircuitoDniUsuario".equals(nombreElemento)) {
                                circuito.setDniUsuario(atrExt.getAttributeValue("Value"));
                            }
                        }
                    }
                    listaCircuitos.add(circuito);
                }
            }
            flujo.setListaFirmasCircuito(listaCircuitos);
        }
    }

    /**
     * Parsea una etiqueta transition y modifica los hashmap de destinos
     *
     * @param transitionElement
     */
    private void comprobarTransiciones(Element chaveLoxica, List<Element> transitions) {

        String chaveId = chaveLoxica.getAttributeValue("Id");
		// Código del trámite en el que nos encontramos
        String from = null;
		// Elements que representan los trámites de condición de salida del trámite actual
        ArrayList <Element> toList = new ArrayList<Element>();
                
        for (Element transitionElement : transitions){
			// Recogemos el código del trámite actual
            if (chaveId.equals(transitionElement.getAttributeValue("To"))) {
                 from = transitionElement.getAttributeValue("From");
			// Se añaden los trámites de condición de salida
            } else if (chaveId.equals(transitionElement.getAttributeValue("From"))) {
                toList.add(transitionElement);
            }
        }
        
        for (Element to : toList) {
            Element extendedAttributesElement = to.getChild("ExtendedAttributes",XMLNS);

            String numeroSecuencia       = "";
            String codigoCondicionSalida = "";
            String listaFavorables  = "";
            String listaDesfavorables = "";

            // Esta iteración sólo debería ocurrir otra vez, ya que cada transición pertenece
            // a la lista de favorables o desfavorables
            List atributos = extendedAttributesElement.getChildren("ExtendedAttribute", XMLNS);
            for (int i = 0; atributos != null && i < atributos.size(); i++) {
                Element atributo = (Element) atributos.get(i);
                if ("lista_favorables".equals(atributo.getAttributeValue("Name"))) {
                    listaFavorables = atributo.getAttributeValue("Value");
                } else if ("lista_desfavorables".equals(atributo.getAttributeValue("Name"))) {
                    listaDesfavorables = atributo.getAttributeValue("Value");
                }
            }// for

            log.debug("Tramite origen:  " + from);
            log.debug("Tramite destino:  " + to);
            log.debug("esDeListaFavorables: " + listaFavorables);
            log.debug("esDeListaDesfavorables: " + listaDesfavorables);
            log.debug("numeroSecuencia: " + numeroSecuencia);
            log.debug("codigoCondicionSalida: " + codigoCondicionSalida);

			// Se añade el flujo a la entrada de la lista de favorables o desfavorables global
            if ("SI".equalsIgnoreCase(listaFavorables)) {
                log.debug("añadiendo a favorables " + from);
                Vector<String> destinosFavorable = new Vector();
                if (hashMapTramitesTransicionesFavorable.containsKey(from)) {
                    destinosFavorable = (Vector<String>)hashMapTramitesTransicionesFavorable.get(from);
                }
                destinosFavorable.add(to.getAttributeValue("To"));
                hashMapTramitesTransicionesFavorable.put(from, destinosFavorable);
            } else if ("SI".equalsIgnoreCase(listaDesfavorables)) {
                Vector<String> destinosDesfavorable = new Vector();
                log.debug("añadiendo a desfavorables " + from );
                if (hashMapTramitesTransicionesDesfavorable.containsKey(from)) {
                    destinosDesfavorable = (Vector<String>)hashMapTramitesTransicionesDesfavorable.get(from);
                }
                destinosDesfavorable.add(to.getAttributeValue("To"));
                hashMapTramitesTransicionesDesfavorable.put(from, destinosDesfavorable);
            }   
        }
    }

    /**
     * anhade las condiciones de salida a un tramite
     *
     * @param tramite
     * @return el tramite con las condiciones de salida
     */
    private DefinicionTramitesValueObject anhadirCondicionesTramite(DefinicionTramitesValueObject tramite){
        
        //Condiciones de salida(tramites)
        String txtCodigo = tramite.getCodigoTramite();
        Vector<String> destinosFavorable=new Vector<String>();
        Vector<String> destinosDesfavorable=new Vector<String>();
        Vector<DefinicionTramitesValueObject> tramitesDestinoFavorables =  new Vector<DefinicionTramitesValueObject>();
        Vector<DefinicionTramitesValueObject> tramitesDestinoDesfavorables =  new Vector<DefinicionTramitesValueObject>();

        if (hashMapTramitesTransicionesFavorable.containsKey(txtCodigo)){            
            destinosFavorable = ( Vector<String>)hashMapTramitesTransicionesFavorable.get(txtCodigo);
            
            for (String s:destinosFavorable){
                DefinicionTramitesValueObject trVO = (DefinicionTramitesValueObject)hashMapTramites.get(s);
                trVO.setCodTramiteFlujoSalida(s);
                trVO.setNombreTramiteFlujoSalida(trVO.getNombreTramite());
                tramitesDestinoFavorables.add(trVO);
            }
            tramite.setListaTramitesFavorable(tramitesDestinoFavorables);
        }

        if (hashMapTramitesTransicionesDesfavorable.containsKey(txtCodigo)){
            destinosDesfavorable = ( Vector<String>)hashMapTramitesTransicionesDesfavorable.get(txtCodigo);
            for (String s:destinosDesfavorable){
                DefinicionTramitesValueObject trVO = (DefinicionTramitesValueObject)hashMapTramites.get(s);
                trVO.setCodTramiteFlujoSalida(s);
                trVO.setNombreTramiteFlujoSalida(trVO.getNombreTramite());
                tramitesDestinoDesfavorables.add(trVO);
            }            
            tramite.setListaTramitesDesfavorable(tramitesDestinoDesfavorables);
        }       
        return tramite;
    }    
    
    /**
     * Recupera la definición de los documentos asociados a un procedimiento
     *
     * @param workflowProccessElement: Elemento del XPDL a partir del cual se
     * extraen la definición de los documentos de expediente
     * @return DocumentoExpedienteVO
     */
    private DocumentoExpedienteVO documentosExpedienteFromXPDL(List <Element> atributosExtendidos){
        
        DocumentoExpedienteVO doc = new DocumentoExpedienteVO();
        for(int j=0;atributosExtendidos!=null && j<atributosExtendidos.size();j++){
            Element aux = (Element)atributosExtendidos.get(j);
            if("codigo".equals(aux.getAttributeValue("Name"))){
                doc.setCodigo(aux.getAttributeValue("Value"));
            }else if("nombre".equals(aux.getAttributeValue("Name"))){
                doc.setNombre(aux.getAttributeValue("Value"));
            }else if("condicion".equals(aux.getAttributeValue("Name"))){
                doc.setCondicion(aux.getAttributeValue("Value"));
            }
        }// for

        return doc;
    }

     /**
     * Recupera la definición de los documentos asociados a un procedimiento
     *
     * @param workflowProccessElement: Elemento del XPDL a partir del cual se
     * extraen la definición de los documentos de expediente
     * @return DocumentoExpedienteVO
     */
    private FirmasDocumentoProcedimientoVO getFirmasDocumentoProcedimientoFromXPDL(List <Element> atributosExtendidos){
      
        FirmasDocumentoProcedimientoVO firma = new FirmasDocumentoProcedimientoVO();

        for(int j=0;atributosExtendidos!=null && j<atributosExtendidos.size();j++){
            Element aux = (Element)atributosExtendidos.get(j);
            if("firmaUsuario".equals(aux.getAttributeValue("Name"))){
                firma.setUsuario(aux.getAttributeValue("Value"));
            }else if("firmaOrden".equals(aux.getAttributeValue("Name"))){
                firma.setOrden(aux.getAttributeValue("Value"));
            }else if("codigoDocumento".equals(aux.getAttributeValue("Name"))){
                firma.setCodDocumento(aux.getAttributeValue("Value"));
            } else if("uor".equals(aux.getAttributeValue("Name"))){
                firma.setUor(aux.getAttributeValue("Value"));
            } else if("cargo".equals(aux.getAttributeValue("Name"))){
                firma.setCargo(aux.getAttributeValue("Value"));
            } else if("finalizarRechazar".equals(aux.getAttributeValue("Name"))){
                firma.setFinalizaRechazo(aux.getAttributeValue("Value"));
            } else if("tramitarSubsanacion".equals(aux.getAttributeValue("Name"))){
                firma.setTramitar(aux.getAttributeValue("Value"));
            }
        }// for

        return firma;
    }

    /**
     * Recupera la definición de los documentos de tramitación asociados a un
     * determinado trámite
     *
     * @param workflowProccessElement: Elemento del XPDL a partir del cual se
     * extraen la definición de los documentos
     * @param codTramite: Código del trámite
     * @return DocumentoTramiteVO
     */
    public DocumentoTramiteVO documentosTramiteFromXPDL(List <Element> atributosExtendidos,String codTramite){
        
        DocumentoTramiteVO doc = new DocumentoTramiteVO();
        FirmaFlujoVO firmaFlujo = new FirmaFlujoVO();
        for(int j=0;atributosExtendidos!=null && j<atributosExtendidos.size();j++){
            Element aux = (Element)atributosExtendidos.get(j);
            String nombreElemento = aux.getAttributeValue("Name");
            if("codigo".equals(nombreElemento)){
                doc.setCodigo(aux.getAttributeValue("Value"));
            }else if("codigoTramite".equals(nombreElemento)){
                doc.setCodTramite(aux.getAttributeValue("Value"));
            }else if("nombre".equals(nombreElemento)){
                doc.setNombre(aux.getAttributeValue("Value"));
            }else if("codTipoDocumento".equals(nombreElemento)){
                doc.setCodTipoDocumento(aux.getAttributeValue("Value"));
            }else if("visibleInternet".equals(nombreElemento)){
                doc.setVisibleInternet(aux.getAttributeValue("Value"));
            }else if("firma".equals(nombreElemento)){
                String tipoFirma = aux.getAttributeValue("Value");
                doc.setFirma(tipoFirma);
                firmaFlujo.setTipoFirma(tipoFirma);
            }else if("plantilla".equals(nombreElemento)){
                doc.setPlantilla(aux.getAttributeValue("Value"));
            }else if("codPlantilla".equals(nombreElemento)){
                doc.setCodPlantilla(aux.getAttributeValue("Value"));
            }else if("contPlantilla".equals(nombreElemento)){
                doc.setContPlantilla(aux.getText());
            }else if("interesado".equals(nombreElemento)){
                doc.setInteresado(aux.getAttributeValue("Value"));
            }else if("docActivo".equals(nombreElemento)){
                doc.setDocActivo(aux.getAttributeValue("Value"));
            }else if("relacion".equals(nombreElemento)){
                doc.setRelacion(aux.getAttributeValue("Value"));
            } else if ("editorTexto".equals(nombreElemento)) {
                doc.setEditorTexto(aux.getAttributeValue("Value"));
            } // Recuperamos los datos de la firma de usuario
            else if ("firmaDocumentoIdUsuario".equals(nombreElemento)) {
                doc.setFirmaDocumentoIdUsuario(aux.getAttributeValue("Value"));
            } else if ("firmaDocumentoUsuarioLog".equals(nombreElemento)) {
                doc.setFirmaDocumentoLogUsuario(aux.getAttributeValue("Value"));
            } // Recuperamos los datos del flujo de firmas 
            else if ("firmaDocumentoDniUsuario".equals(nombreElemento)) {
                doc.setFirmadocumentoDniUsuario(aux.getAttributeValue("Value"));
            } else if ("firmaFlujoId".equals(nombreElemento)) {
                firmaFlujo.setId(Integer.parseInt(aux.getAttributeValue("Value")));
                doc.setFirmaFlujo(firmaFlujo);
             /*   String atributo = aux.getAttributeValue("Value");
                Integer id = (atributo != null && !atributo.isEmpty()) ? Integer.parseInt(atributo) : null;
                firmaFlujo.setId(id);
                firmaCircuito.setIdFlujoFirma(id);
            } else if ("firmaFlujoNombre".equals(nombreElemento)) {
                firmaFlujo.setNombre(aux.getAttributeValue("Value"));
                firmaCircuito.setNombreFlujoFirma(aux.getAttributeValue("Value"));
            } else if ("firmaFlujoActivo".equals(nombreElemento)) {
                String atributo = aux.getAttributeValue("Value");
                firmaFlujo.setActivo(atributo.equals("1"));
            } else if ("firmaFlujoIdTipo".equals(nombreElemento)) {
                String atributo = aux.getAttributeValue("Value");
                firmaFlujo.setIdTipoFirma((atributo != null && !atributo.isEmpty()) ? Integer.parseInt(atributo): null);
            } else if ("firmaCircuitoIdUsuario".equals(nombreElemento)) {
                String atributo = aux.getAttributeValue("Value");
                firmaCircuito.setIdUsuario((atributo != null && !atributo.isEmpty()) ? Integer.parseInt(atributo) : null);
            } else if ("firmaCircuitoOrden".equals(nombreElemento)) {
                String atributo = aux.getAttributeValue("Value");
                firmaCircuito.setOrden((atributo != null && !atributo.isEmpty()) ? Integer.parseInt(atributo) : null);
            } else if ("firmaCircuitoLogUsuario".equals(nombreElemento)) {
                firmaCircuito.setNombreUsuario(aux.getAttributeValue("Value"));  */
            }
        }// for

        if (doc.getFirmaFlujo() != null && doc.getFirmaDocumentoIdUsuario() == null) {
            doc.setFirmaDocumentoIdUsuario("-1");
        }

        if (codTramite != null && codTramite.equals(doc.getCodTramite())) {
            return doc;
        } else {
            return null;
        }
    }
    
    /**
     * Recupera la definición de los enlaces de un procedimiento
     *
     * @param elemento: Elemento del XPDL a partir del cual se extraen la
     * definición de los documentos de expediente
     * @return DocumentoExpedienteVO
     */
    private EnlaceProcedimientoVO enlaceExpedienteFromXPDL(List <Element> atributosExtendidos){

        EnlaceProcedimientoVO enlace = new EnlaceProcedimientoVO();
        
        for(int j=0;atributosExtendidos!=null && j<atributosExtendidos.size();j++){
            Element aux = (Element)atributosExtendidos.get(j);
            if("codigo".equals(aux.getAttributeValue("Name"))){
                enlace.setCodigo(aux.getAttributeValue("Value"));
            }else if("descripcion".equals(aux.getAttributeValue("Name"))){
                enlace.setDescripcion(aux.getAttributeValue("Value"));
            }else if("url".equals(aux.getAttributeValue("Name"))){
                enlace.setUrl(aux.getAttributeValue("Value"));
            }else if("estado".equals(aux.getAttributeValue("Name"))){
                enlace.setEstado(aux.getAttributeValue("Value"));
            }
        }
        
        return enlace;
    }

   /**
     * Recupera la definición de los enlaces de un trámite
     *
     * @param workflowProccessElement: Elemento del XPDL a partir del cual se
     * extraen la definición de los enlaces del trámite
     * @param codTramite: Código del trámite
     * @return EnlaceTramiteVO
     */
    private EnlaceTramiteVO enlaceTramiteFromXPDL(List <Element> atributosExtendidos,String codTramite){

        EnlaceTramiteVO enlace = new EnlaceTramiteVO();
        for(Element aux : atributosExtendidos){
            if("codigo".equals(aux.getAttributeValue("Name"))){
                enlace.setCodigo(aux.getAttributeValue("Value"));
            }else if("descripcion".equals(aux.getAttributeValue("Name"))){
                enlace.setDescripcion(aux.getAttributeValue("Value"));
            }else if("url".equals(aux.getAttributeValue("Name"))){
                enlace.setUrl(aux.getAttributeValue("Value"));
            }else if("estado".equals(aux.getAttributeValue("Name"))){
                enlace.setEstado(aux.getAttributeValue("Value"));
            }else if("codigoTramite".equals(aux.getAttributeValue("Name"))){
                enlace.setCodTramite(aux.getAttributeValue("Value"));
            }                            
        }// for

        if(codTramite!=null && codTramite.equals(enlace.getCodTramite()))  {
            return enlace;
        } else {
            return null;
        }
    }

  /**
     * Recupera la definición los campos suplementarios de un procedimiento
     *
     * @param workflowProccessElement: Elemento del XPDL a partir del cual se
     * extraen la definición de los documentos de expediente
     * @return DocumentoExpedienteVO
     */
    private CampoSuplementarioVO campoSuplementarioExpedienteFromXPDL(List <Element> atributosExtendidos){

        CampoSuplementarioVO campo = new CampoSuplementarioVO();

        for(int j=0;atributosExtendidos!=null && j<atributosExtendidos.size();j++){
            Element aux = (Element)atributosExtendidos.get(j);
            if("codigo".equals(aux.getAttributeValue("Name"))){
                campo.setCodigo(aux.getAttributeValue("Value"));
            }else if("descripcion".equals(aux.getAttributeValue("Name"))){
                campo.setDescripcion(aux.getAttributeValue("Value"));
            }else if("codigoTipoDato".equals(aux.getAttributeValue("Name"))){
                campo.setCodigoTipoDato(aux.getAttributeValue("Value"));
            }else if("codigoPlantilla".equals(aux.getAttributeValue("Name"))){
                campo.setCodigoPlantilla(aux.getAttributeValue("Value"));
            }else if("tamano".equals(aux.getAttributeValue("Name"))){
                campo.setTamano(aux.getAttributeValue("Value"));
            }else if("mascara".equals(aux.getAttributeValue("Name"))){
                campo.setMascara(aux.getAttributeValue("Value"));
            }else if("obligatorio".equals(aux.getAttributeValue("Name"))){
                campo.setObligatorio(aux.getAttributeValue("Value"));
            }else if("rotulo".equals(aux.getAttributeValue("Name"))){
                campo.setRotulo(aux.getAttributeValue("Value"));
            }else if("activo".equals(aux.getAttributeValue("Name"))){
                campo.setActivo(aux.getAttributeValue("Value"));
            } else if("oculto".equals(aux.getAttributeValue("Name"))){
                campo.setOculto(aux.getAttributeValue("Value"));
            } else if("bloqueado".equals(aux.getAttributeValue("Name"))){
                campo.setBloqueado(aux.getAttributeValue("Value"));
            } else if("plazoAviso".equals(aux.getAttributeValue("Name"))){
                campo.setPlazoFecha(aux.getAttributeValue("Value"));
            } else if("periodoAviso".equals(aux.getAttributeValue("Name"))){
                campo.setCheckPlazoFecha(aux.getAttributeValue("Value"));
            } else if("validacion".equals(aux.getAttributeValue("Name"))){
                campo.setValidacion(aux.getAttributeValue("Value"));
            } else if("operacion".equals(aux.getAttributeValue("Name"))){
                campo.setOperacion(aux.getAttributeValue("Value"));
            } else if("agrupacionCampo".equals(aux.getAttributeValue("Name"))){
                campo.setAgrupacionCampo(aux.getAttributeValue("Value"));
            } else if("posicionX".equals(aux.getAttributeValue("Name"))){
                campo.setPosicionesX(parsearPosicion(aux.getAttributeValue("Value")));
            } else if("posicionY".equals(aux.getAttributeValue("Name"))){
                campo.setPosicionesY(parsearPosicion(aux.getAttributeValue("Value")));
            }
        }// for
                        
        return campo;
    }

    /**
     * Recupera la definición los campos suplementarios de un determinado
     * trámite
     *
     * @param atributosExtendidos: Elemento del XPDL a partir del cual se
     * extraen la definición de los campos suplementarios del trámites
     * @param codTramite: Código del trámite
     * @return CampoSuplementarioTramiteVO
     */
    private CampoSuplementarioTramiteVO campoSuplementarioTramiteFromXPDL(List <Element> atributosExtendidos,String codTramite){
        CampoSuplementarioTramiteVO campo = new CampoSuplementarioTramiteVO();

        for(Element aux : atributosExtendidos){
            if("codigo".equals(aux.getAttributeValue("Name"))){
                campo.setCodigo(aux.getAttributeValue("Value"));
            }else if("descripcion".equals(aux.getAttributeValue("Name"))){
                campo.setDescripcion(aux.getAttributeValue("Value"));
            }else if("codigoTipoDato".equals(aux.getAttributeValue("Name"))){
                campo.setCodigoTipoDato(aux.getAttributeValue("Value"));
            }else if("codigoPlantilla".equals(aux.getAttributeValue("Name"))){
                campo.setCodigoPlantilla(aux.getAttributeValue("Value"));
            }else if("tamano".equals(aux.getAttributeValue("Name"))){
                campo.setTamano(aux.getAttributeValue("Value"));
            }else if("mascara".equals(aux.getAttributeValue("Name"))){
                campo.setMascara(aux.getAttributeValue("Value"));
            }else if("obligatorio".equals(aux.getAttributeValue("Name"))){
                campo.setObligatorio(aux.getAttributeValue("Value"));
            }else if("orden".equals(aux.getAttributeValue("Name"))){
                campo.setOrden(aux.getAttributeValue("Value"));
            }else if("rotulo".equals(aux.getAttributeValue("Name"))){
                campo.setRotulo(aux.getAttributeValue("Value"));
            }else if("activo".equals(aux.getAttributeValue("Name"))){
                campo.setActivo(aux.getAttributeValue("Value"));
            }else if("oculto".equals(aux.getAttributeValue("Name"))){
                campo.setOculto(aux.getAttributeValue("Value"));
            }else if("bloqueado".equals(aux.getAttributeValue("Name"))){
                campo.setBloqueado(aux.getAttributeValue("Value"));
            }else if("codigoTramite".equals(aux.getAttributeValue("Name"))){                                
                campo.setCodTramite(aux.getAttributeValue("Value"));
            }else if("visible".equals(aux.getAttributeValue("Name"))){
                campo.setVisible(aux.getAttributeValue("Value"));
            } else if("plazoAviso".equals(aux.getAttributeValue("Name"))){
                campo.setPlazoFecha(aux.getAttributeValue("Value"));
            } else if("periodoAviso".equals(aux.getAttributeValue("Name"))){
                campo.setCheckPlazoFecha(aux.getAttributeValue("Value"));
            } else if("validacion".equals(aux.getAttributeValue("Name"))){
                campo.setValidacion(aux.getAttributeValue("Value"));
            } else if("operacion".equals(aux.getAttributeValue("Name"))){
                campo.setOperacion(aux.getAttributeValue("Value"));
            } else if("agrupacionCampo".equals(aux.getAttributeValue("Name"))){
                campo.setAgrupacionCampo(aux.getAttributeValue("Value"));
            } else if("posicionX".equals(aux.getAttributeValue("Name"))){
                campo.setListaPosicionX(aux.getAttributeValue("Value"));
            } else if("posicionY".equals(aux.getAttributeValue("Name"))){
                campo.setListaPosicionY(aux.getAttributeValue("Value"));
            }
        }// for

        if (codTramite != null && codTramite.equals(campo.getCodTramite())) {
            return campo;
        } else {
            return null;
        }
    }

    /**
     * Recupera las definiciones de las agrupaciones de campos suplementarios de
     * un procedimiento o trámite
     *
     * @param workflowProccessElement: Elemento del XPDL a partir del cual se
     * extraen las definiciones de las agrupaciones de campos suplementarios del
     * trámites
     * @param codTramite: Código del trámite. Nulo en caso de que se trate del
     * procedimiento
     * @return DefinicionAgrupacionCamposValueObject
     */
    private DefinicionAgrupacionCamposValueObject agrupacionCamposSuplementariosFromXPDL(List <Element> atributosExtendidos,String codTramite){
        
        boolean tramiteCorrecto = false;

        DefinicionAgrupacionCamposValueObject agrupacion = new DefinicionAgrupacionCamposValueObject();

        for(int j=0;atributosExtendidos!=null && j<atributosExtendidos.size();j++){
            Element aux = (Element)atributosExtendidos.get(j);
            if("codigo".equals(aux.getAttributeValue("Name"))){
                agrupacion.setCodAgrupacion(aux.getAttributeValue("Value"));
            }else if("descripcion".equals(aux.getAttributeValue("Name"))){
                agrupacion.setDescAgrupacion(aux.getAttributeValue("Value"));
            }else if("orden".equals(aux.getAttributeValue("Name"))){
                try{
                    agrupacion.setOrdenAgrupacion(Integer.parseInt(aux.getAttributeValue("Value")));
                } catch (NumberFormatException e) {
                    agrupacion.setOrdenAgrupacion(null);
                }
            }else if("activo".equals(aux.getAttributeValue("Name"))){
                agrupacion.setAgrupacionActiva(aux.getAttributeValue("Value"));
            } else if ("codigoTramite".equals(aux.getAttributeValue("Name")) && codTramite != null
                    && codTramite.equals(aux.getAttributeValue("Value"))) {
                tramiteCorrecto = true;
            }
        }// for

        if (codTramite==null || tramiteCorrecto) {
            return agrupacion;
        } else {
            return null;
        }
    }

    /**
     * Recupera la definición de los roles los campos suplementarios de un
     * procedimiento
     *
     * @param workflowProccessElement: Elemento del XPDL a partir del cual se
     * extraen la definición de los documentos de expediente
     * @return RolProcedimientoVO
     */
    private RolProcedimientoVO rolProcedimientoFromXPDL(List <Element> atributosExtendidos){

        RolProcedimientoVO rol = new RolProcedimientoVO();

        for(int j=0;atributosExtendidos!=null && j<atributosExtendidos.size();j++){
            Element aux = (Element)atributosExtendidos.get(j);
            if("codigo".equals(aux.getAttributeValue("Name"))){
                rol.setCodigo(aux.getAttributeValue("Value"));
            }else if("descripcion".equals(aux.getAttributeValue("Name"))){
                rol.setDescripcion(aux.getAttributeValue("Value"));
            }else if("defecto".equals(aux.getAttributeValue("Name"))){
                rol.setDefecto(aux.getAttributeValue("Value"));
            }else if("estado".equals(aux.getAttributeValue("Name"))){
                rol.setEstado(aux.getAttributeValue("Value"));
            }
        }// for
                        
        return rol;
    }

    /**
     * Recupera las unidades participantes del procedimienot y de sus trámites
     * definidos en el fichero XPDL
     *
     * @param workflowProccessElement: Elemento del XPDL a partir del cual se
     * extraen la definición de los documentos de expediente
     * @return ArrayList<UnidadesParticipantesVO>
     */
    private void getUnidadesParticipantesFromXPDL(List <Element> atributosExtendidos, Hashtable<String,String> unidades){        

        String codigo = null;
        String nombre = null;
       
        for(int i=0;atributosExtendidos!=null && i<atributosExtendidos.size();i++){
            Element aux = (Element)atributosExtendidos.get(i);
            if("codigo".equals(aux.getAttributeValue("Name"))){
                codigo = aux.getAttributeValue("Value");
            }else if("nombre".equals(aux.getAttributeValue("Name"))){
                nombre = aux.getAttributeValue("Value");
            }
        }//for
        unidades.put(codigo,nombre);
    }

  /**
     * Recupera las condiciones de entrada de un determinado trámite
     *
     * @param atributosExtendidos: Elemento del XPDL a partir del cual se
     * extraen las condiciones de entrada del trámite
     * @param codTramite: Código del trámite
     * @return CondicionEntradaTramiteVO
     */
    private CondicionEntradaTramiteVO condicionesEntradaTramiteFromXPDL(List <Element> atributosExtendidos,String codTramite){
        
        CondicionEntradaTramiteVO condicion = new CondicionEntradaTramiteVO();
        for(Element aux : atributosExtendidos){
            if("tipo_condicion_entrada".equals(aux.getAttributeValue("Name"))){
                String dato = aux.getAttributeValue("Value");
                if ("E".equals(dato)) {
                    condicion.setTipoCondicionEntrada("EXPRESION");
                } else if ("D".equals(dato)) {
                    condicion.setTipoCondicionEntrada("DOCUMENTO");
                } else if ("T".equals(dato)) {
                    condicion.setTipoCondicionEntrada("TRÁMITE");
                }
            } else if ("codigo_tramite_origen".equals(aux.getAttributeValue("Name"))) {
                condicion.setCodigoTramiteOrigen(aux.getAttributeValue("Value"));
            }else if("id_tramite_cond_tramite".equals(aux.getAttributeValue("Name"))){
                condicion.setIdTramiteCondTramite(aux.getAttributeValue("Value"));
            }else if("estado".equals(aux.getAttributeValue("Name"))){
                condicion.setEstadoCodTramiteCondTramite(aux.getAttributeValue("Value"));
            }else if("expresion_condicion".equals(aux.getAttributeValue("Name"))){
                condicion.setExpresionCondicion(aux.getAttributeValue("Value"));
            }else if("codigo_documento".equals(aux.getAttributeValue("Name"))){
                condicion.setCodDocumento(aux.getAttributeValue("Value"));
            }
        }// for

        // Si el código de trámite de origen de la condición de entrada es la del trámite, se agrega a la lista de condiciones de entrada
        if(codTramite!=null && codTramite.equals(condicion.getCodigoTramiteOrigen())) {
            return condicion;
        } else {
            return null;
        }
    }

    /**
     * Actualiza la lista de unidades tramitadoras de un determinado trámite
     * @param codTramite: Código del trámite
     * @param codUorInicioManual: Código de inicio de la uor de inicial manual,
     * null si está vacía.
     * @param listaCodigosUnidadesTramitadoras: Lista de unidades tramitadoras
     * @param dfVO: Objeto con la definición del procedimiento y de los trámites
     * @return boolean
     */
    public boolean actualizarListaUnidadesTramitadoras(String codTramite,String codUorInicioManual,Vector listaCodigosUnidadesTramitadoras,DefinicionProcedimientosValueObject dfVO){
        boolean exito = false;

        Vector<DefinicionTramitesValueObject> tramites = (Vector<DefinicionTramitesValueObject>)dfVO.getTramites();
        DefinicionTramitesValueObject tramite = null;

        int posicionTramiteEnLista  =0;
        for(int i=0;i<tramites.size();i++){
            DefinicionTramitesValueObject dtVO = tramites.get(i);
            if(dtVO.getCodigoTramite().equals(codTramite)){
                tramite = dtVO;
                posicionTramiteEnLista = i;
                break;
            }
        }//for

        // Código de unidad de inicio manual
        if(codUorInicioManual!=null && !"".equals(codUorInicioManual)){
            tramite.setCodUnidadInicio(codUorInicioManual);
        }

        Vector<UORDTO> listaUnidades = new Vector<UORDTO>();
        for(int j=0;listaCodigosUnidadesTramitadoras!=null && j<listaCodigosUnidadesTramitadoras.size();j++){
            String codigo = (String)listaCodigosUnidadesTramitadoras.get(j);

            if(codigo!=null && !"".equals(codigo) && codigo.length()>0){
                UORDTO uor = new UORDTO();
                uor.setUor_cod((String)listaCodigosUnidadesTramitadoras.get(j));
                listaUnidades.add(uor);
            } // if
        }// for

        // Se actualiza la lista de unidades tramitadoras
        tramite.setListaUnidadesTramitadoras(listaUnidades);
        tramites.remove(posicionTramiteEnLista);
        tramites.add(posicionTramiteEnLista,tramite);
        dfVO.setTramites(tramites);
        
        // Se actualiza el trámite
        exito = true;
        return exito;
    }// actualizarListaUnidadesTramitadoras

    /**
     * Se recupera el integer correspondiente al id de flujo dentro de la cadena
     * correspondiente al id de dataField de Circuito_Firma Se obtiene
     * calculando el substring entre: - el indice del primer caracter guion + 1
     * posición - la longitud del id de flujo, esto es: la posición del ultimo
     * guion - la posicion del primer guion
     *
     * @return el valor Integer correspondiente a el substring obtenido.
     */
    private Integer obtenerIdFlujo(String idDataField) {
        Integer flujoId = null;

        try {
            int posPrimGuion = idDataField.indexOf("-");
            int posUltGuion = idDataField.lastIndexOf("-");
            flujoId = Integer.parseInt(idDataField.substring((posPrimGuion + 1), (posUltGuion)));
        } catch (NumberFormatException e) {
            log.error("No se puede formatear el valor del String que se intenta convertir a Integer.");
        }
        return flujoId;
    }
    
    private String parsearPosicion(String attributeValue) {
        Integer valor = (StringUtils.isNullOrEmptyString(attributeValue)) ? -1 : Integer.parseInt(attributeValue);
        return (valor < 0) ? "" : attributeValue;
    }
    
}
