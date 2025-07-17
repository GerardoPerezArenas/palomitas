package es.altia.util.xpdl;

import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.sge.*;
import es.altia.agora.business.sge.firma.vo.FirmaCircuitoVO;
import es.altia.agora.business.sge.firma.vo.FirmaFlujoVO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.StringUtils;
import es.altia.util.commons.StringOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * Clase encargada de realizar la conversion de la información de un procedimiento
 * y sus tramites a formato xpdl .
 * Para realizar la conversion se ha utilizado las librerias jdom para el manejo
 * del xml. 
 */
public class ConversorXPDL {
    
    private static Namespace XSI=Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
    private static Namespace XPDL=Namespace.getNamespace("xpdl", "http://www.wfmc.org/2008/XPDL2.1");
    private static Namespace XMLNS=Namespace.getNamespace("","http://www.wfmc.org/2008/XPDL2.1");
    
    private static final HashMap hashMapTramites = new HashMap();
    private static int offsetUOR=10;
    public static final String CAMPO_SUPLEMENTARIO_PROCEDIMIENTO  = "Campo_Suplementario_Procedimiento";
    public static final String CAMPO_SUPLEMENTARIO_TRAMITE             = "Campo_Suplementario_Tramite";
    public static final String ENLACE_PROCEDIMIENTO                           = "Enlace_Procedimiento";
    public static final String ENLACE_TRAMITE                                       = "Enlace_Tramite";
    public static final String AGRUPACION_CAMPO_SUPLEMENTARIO_PROCEDIMIENTO  = "Agrupacion_Campo_Suplementario_Procedimiento";
    public static final String AGRUPACION_CAMPO_SUPLEMENTARIO_TRAMITE        = "Agrupacion_Campo_Suplementario_Tramite";
    public static final String FLUJO_FIRMA							= "Flujo_Firma";
    public static final String CIRCUITO_FIRMA						= "Circuito_Flujo_Firma";
    
    //campos usados para referirse a los distantas opciones de la unidad que realiza un tramite
    private static String UOR_EXP= "UOR_EXP";
  
    private static Logger log = Logger.getLogger(ConversorXPDL.class);

    /**
     * Pasa la informacion de un procedimiento a XPDL y lo imprime
     * @param dpVO , el procedimientoVO que se va a transformar
     * @param out, flujo de salida por donde se imprimira la información XPDL
     * @return un elemento con toda la información del procedimiento en formato
     * xpdl 
     */
    public static Element procedimientoToXPDL(DefinicionProcedimientosValueObject dpVO,
        Vector<DefinicionTramitesValueObject> tramites , List<FirmaFlujoVO> flujos, OutputStream out,String[] params){
        
        /* Write OutputStream. */
        Element procedimientoElement = procedimientoToXPDL(dpVO,tramites,flujos,params);
        
        try {
        Document document = new Document(procedimientoElement);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());                
        
        outputter.output(document, out);
    } catch (Exception e) {
        e.printStackTrace();
    }
	
    return procedimientoElement;
        
      
    }
    public static Element procedimientoToXPDL_XML(DefinicionProcedimientosValueObject dpVO,
            Vector<DefinicionTramitesValueObject> tramites , List<FirmaFlujoVO> flujos, String[] params){

        Element procedimientoElement = procedimientoToXPDL(dpVO,tramites,flujos,params);                
        return procedimientoElement;              
    }
    /**
     * Pasa la informacion de un procedimiento a XPDL 
     * @param dpVO, el DefinicionProcedimientoVO que se va a transformar
     * @param tramites , vector con el conjunto de tramites correspondientes a ese
     * procedimiento
     * @return un elemento xml  con toda la información del procedimiento en formato
     * xpdl 
     */
    private  static Element procedimientoToXPDL(DefinicionProcedimientosValueObject dpVO,
        Vector<DefinicionTramitesValueObject> tramites, List<FirmaFlujoVO> flujos, String[] params){

        Element packageElement = new Element("Package",XMLNS); 
        packageElement.setAttribute("Id", "ProcedimientosXPDL");
        Element packageHeaderElement = new Element("PackageHeader",XMLNS);
        Element xpdlVersionElement = new Element("XPDLVersion",XMLNS);
        xpdlVersionElement.setText("2.1");
        Element vendorElement=new Element ("Vendor",XMLNS);
        vendorElement.setText("Altia");
        Element descriptionElement = new Element("Description",XMLNS);
        descriptionElement.setText("Informacion en xpdl de procedimientos del sge ");
        packageHeaderElement.addContent(xpdlVersionElement);
        packageHeaderElement.addContent(vendorElement);
        packageHeaderElement.addContent(XPDLUtil.fechaToXML());
        packageHeaderElement.addContent(descriptionElement);
        packageElement.addContent(packageHeaderElement);
        Element workFlowProcessesElement = new Element("WorkflowProcesses",XMLNS); 
        Element workFlowProcessElement = new Element("WorkflowProcess",XMLNS);
        Element ProcessHeaderElement = new Element ("ProcessHeader",XMLNS);
        workFlowProcessElement.addContent(ProcessHeaderElement);
        workFlowProcessElement.setAttribute("Id",dpVO.getTxtCodigo());
        workFlowProcessElement.setAttribute("Name",dpVO.getTxtDescripcion());

        // Se añaden los DataFields con los enlaces del procedimiento
        Vector<DefinicionProcedimientosValueObject> enlacesProc  = (Vector<DefinicionProcedimientosValueObject>)dpVO.getListaEnlaces();
        Vector<DefinicionProcedimientosValueObject> docsProc      = (Vector<DefinicionProcedimientosValueObject>)dpVO.getListasDoc();
        Vector<DefinicionCampoValueObject> camposSupProc       = (Vector<DefinicionCampoValueObject>)dpVO.getListaCampos();
        Vector<DefinicionAgrupacionCamposValueObject> agrupCamposProc = (Vector<DefinicionAgrupacionCamposValueObject>) dpVO.getListaAgrupaciones();
        Vector<DefinicionProcedimientosValueObject> roles           = (Vector<DefinicionProcedimientosValueObject>)dpVO.getListaRoles();
        ArrayList<FirmasDocumentoProcedimientoVO> firmasDocumentoProcedimiento = new ArrayList<FirmasDocumentoProcedimientoVO>();
        firmasDocumentoProcedimiento=dpVO.getFirmasDocumentosProcedimiento();

        log.debug(" ====> procedimientoToXPDL enlaces procedimiento recuperados: " + enlacesProc.size());
        log.debug(" ====> procedimientoToXPDL dos procedimiento recuperados: " + docsProc.size());
        log.debug(" ====> procedimientoToXPDL camposSupProc procedimiento recuperados: " + camposSupProc.size());
        log.debug(" ====> procedimientoToXPDL firmas documentos procedimiento recuperados: " + firmasDocumentoProcedimiento.size());

        Element dataFieldsProcedimiento = new Element("DataFields",XMLNS);
        dataFieldsProcedimiento.addContent(anhadirRolesProcedimiento(roles));

        dataFieldsProcedimiento.addContent(anhadirEnlaces(enlacesProc,ENLACE_PROCEDIMIENTO,null));
        dataFieldsProcedimiento.addContent(anhadirDocumentosProcedimiento(docsProc));
        dataFieldsProcedimiento.addContent(anhadirCamposSuplementarios(camposSupProc,CAMPO_SUPLEMENTARIO_PROCEDIMIENTO,null));

        dataFieldsProcedimiento.addContent(anhadirAgrupacionCamposSuplementarios(agrupCamposProc,AGRUPACION_CAMPO_SUPLEMENTARIO_PROCEDIMIENTO,null));
        dataFieldsProcedimiento.addContent(anhadirFirmasDocumentoProcedimiento(firmasDocumentoProcedimiento)); 
        workFlowProcessElement.addContent(dataFieldsProcedimiento);

        /** SE INCORPORAN A LOS DATAFIELDS LA DEFINICIÓN DE CAMPOS SUPLEMENTARIOS, ENLACES Y DOCUMENTOS DE CADA TRÁMITE **/
        for(int i=0;tramites!=null && i<tramites.size();i++){
            DefinicionTramitesValueObject dtVO = (DefinicionTramitesValueObject)tramites.get(i);

              /** SE AÑADEN LOS ENLACES, DOCUMENTOS Y CAMPOS SUPLEMENTARIOS DEL TRÁMITE */
            Vector<DefinicionProcedimientosValueObject> enlaces  = (Vector<DefinicionProcedimientosValueObject>)dtVO.getListaEnlaces();
            Vector<DefinicionCampoValueObject> campos              = (Vector<DefinicionCampoValueObject>)dtVO.getListaCampos();
            Vector<DefinicionAgrupacionCamposValueObject> agrupaciones = (Vector<DefinicionAgrupacionCamposValueObject>) dtVO.getListaAgrupaciones();                    
            Vector<DefinicionTramitesValueObject> documentos     = (Vector<DefinicionTramitesValueObject>)dtVO.getListaDocumentos();
            Vector<DefinicionTramitesValueObject> condiciones      = (Vector<DefinicionTramitesValueObject>)dtVO.getListasCondEntrada();

            dataFieldsProcedimiento.addContent(anhadirEnlaces(enlaces,ENLACE_TRAMITE,dtVO.getCodigoTramite()));
            dataFieldsProcedimiento.addContent(anhadirCamposSuplementarios(campos,CAMPO_SUPLEMENTARIO_TRAMITE,dtVO.getCodigoTramite()));
            dataFieldsProcedimiento.addContent(anhadirAgrupacionCamposSuplementarios(agrupaciones,AGRUPACION_CAMPO_SUPLEMENTARIO_TRAMITE,dtVO.getCodigoTramite())); 
            dataFieldsProcedimiento.addContent(anhadirDocumentosTramite(documentos,dtVO.getCodigoTramite()));
            dataFieldsProcedimiento.addContent(anhadirCondicionesEntradaTramite(condiciones,dtVO.getCodigoTramite(),dtVO.getTxtCodigo(),dtVO.getCodMunicipio())); 
        }
		
		// SE INCORPORAN A LOS DATAFIELDS LOS DATOS DE LOS FLUJOS DE FIRMA Y CIRCUITOS DE FIRMA DE ESTOS
        for(FirmaFlujoVO flujo : flujos){
            List<FirmaCircuitoVO> circuitos      = flujo.getListaFirmasCircuito();

            dataFieldsProcedimiento.addContent(anhadirDatosFlujo(flujo,FLUJO_FIRMA));
            dataFieldsProcedimiento.addContent(anhadirDatosCircuitos(circuitos,CIRCUITO_FIRMA,flujo.getId()));
        }
        
		// SE INCORPORAN OTROS DATOS DE TRAMITE COMO ACTIVITY O TRANSACTION
        if (tramites.size() > 0) {
            Element activitiesElement = new Element ("Activities",XMLNS);
            for (DefinicionTramitesValueObject defTramVO:tramites){
                activitiesElement.addContent(tramiteToXPDL(defTramVO,params));
            }            
            
            Element transitionsElement = new Element ("Transitions",XMLNS);
            for (DefinicionTramitesValueObject defTramVO:tramites){
                Collection <Element> enlaces = transicionesToXPDL(defTramVO);
                
                for (Element elemento : enlaces) {
                    if (elemento.getName().equals("Transition")) 
                        transitionsElement.addContent(elemento);
                    else 
                        activitiesElement.addContent(elemento);
                }
            }
                         
            dataFieldsProcedimiento.addContent(anhadirParticipantes(tramites,dpVO.getTablaUnidadInicio()));
            
            workFlowProcessElement.addContent(activitiesElement);
            workFlowProcessElement.addContent(transitionsElement);
            workFlowProcessElement.addContent(getRestanteInformacionProcedimiento(dpVO));
            workFlowProcessesElement.addContent(workFlowProcessElement);
            packageElement.addContent(workFlowProcessesElement);
        }   
        
        packageElement.addNamespaceDeclaration(XPDL);
        packageElement.addNamespaceDeclaration(XSI);
        packageElement.setAttribute("schemaLocation",XPDL.getURI()+
                                        " http://wfmc.org/standards/docs/TC-1025_schema_10_xpdl.xsd",XSI);
        packageElement.setNamespace(XMLNS);
        return packageElement;
    }

    private static Element getRestanteInformacionProcedimiento(DefinicionProcedimientosValueObject dfpVO){
        Element extendedAtributesElement = new Element ("ExtendedAttributes",XMLNS);

        String sDescripcionBreve = "";
        if(dfpVO.getDescripcionBreve()!=null && dfpVO.getDescripcionBreve().length()>0) sDescripcionBreve = dfpVO.getDescripcionBreve();
        Element descripcionBreve = new Element ("ExtendedAttribute",XMLNS);
        descripcionBreve.setAttribute("Name","descripcion_breve");
        descripcionBreve.setAttribute("Value",sDescripcionBreve);
        extendedAtributesElement.addContent(descripcionBreve);

        String sTramiteInicio = "";
        if(dfpVO.getTramiteInicio()!=null) sTramiteInicio = dfpVO.getTramiteInicio();
        Element tramiteInicio = new Element ("ExtendedAttribute",XMLNS);
        tramiteInicio.setAttribute("Name","tramite_inicio_procedimiento");
        tramiteInicio.setAttribute("Value",sTramiteInicio);
        extendedAtributesElement.addContent(tramiteInicio);

        String sFechaVigenciaDesde = "";
        if(dfpVO.getFechaLimiteDesde()!=null) sFechaVigenciaDesde = dfpVO.getFechaLimiteDesde();
        Element fechaVigenciaDesde = new Element ("ExtendedAttribute",XMLNS);
        fechaVigenciaDesde.setAttribute("Name","fecha_vigencia_desde");
        fechaVigenciaDesde.setAttribute("Value",sFechaVigenciaDesde);
        extendedAtributesElement.addContent(fechaVigenciaDesde);

        String sFechaVigenciaHasta = "";
        if(dfpVO.getFechaLimiteHasta()!=null) sFechaVigenciaHasta = dfpVO.getFechaLimiteHasta();
        Element fechaVigenciaHasta = new Element ("ExtendedAttribute",XMLNS);
        fechaVigenciaHasta.setAttribute("Name","fecha_vigencia_hasta");
        fechaVigenciaHasta.setAttribute("Value",sFechaVigenciaHasta);
        extendedAtributesElement.addContent(fechaVigenciaHasta);

        String sArea = "";
        if(dfpVO.getCodArea()!=null) sArea = dfpVO.getCodArea();
        Element area = new Element ("ExtendedAttribute",XMLNS);
        area.setAttribute("Name","area_procedimiento");
        area.setAttribute("Value",sArea);
        extendedAtributesElement.addContent(area);
        
        String descripcionArea = "";
        if(StringUtils.isNotNullOrEmptyOrNullString(dfpVO.getDescArea())) descripcionArea = dfpVO.getDescArea();
        Element descArea = new Element ("ExtendedAttribute",XMLNS);
        descArea.setAttribute("Name","descripcion_area_procedimiento");
        descArea.setAttribute("Value",descripcionArea);
        extendedAtributesElement.addContent(descArea);        

        String sCodTipoProc ="";
        if(dfpVO.getCodTipoProcedimiento()!=null) sCodTipoProc = dfpVO.getCodTipoProcedimiento();
        Element tipo = new Element ("ExtendedAttribute",XMLNS);
        tipo.setAttribute("Name","tipo_procedimiento");
        tipo.setAttribute("Value",sCodTipoProc);
        extendedAtributesElement.addContent(tipo);

        String sPlazo = "";
        if(dfpVO.getPlazo()!=null) sPlazo = dfpVO.getPlazo();
        Element plazo = new Element ("ExtendedAttribute",XMLNS);
        plazo.setAttribute("Name","plazo_procedimiento");
        plazo.setAttribute("Value",sPlazo);
        extendedAtributesElement.addContent(plazo);

        String sPorcentaje = "";
        if(dfpVO.getPorcentaje()!=null) sPorcentaje = dfpVO.getPorcentaje();
        Element porcentaje = new Element ("ExtendedAttribute",XMLNS);
        porcentaje.setAttribute("Name","porcentaje_plazo_procedimiento");
        porcentaje.setAttribute("Value",sPorcentaje);
        extendedAtributesElement.addContent(porcentaje);

        String sTipoPlazo = "";
        if(dfpVO.getTipoPlazo()!=null) sTipoPlazo = dfpVO.getTipoPlazo();
        Element tipoPlazo = new Element ("ExtendedAttribute",XMLNS);
        tipoPlazo.setAttribute("Name","tipo_plazo_procedimiento");
        tipoPlazo.setAttribute("Value",sTipoPlazo);
        extendedAtributesElement.addContent(tipoPlazo);

        String sCodEstado ="";
        if(dfpVO.getCodEstado()!=null) sCodEstado = dfpVO.getCodEstado();
        Element estado = new Element ("ExtendedAttribute",XMLNS);
        estado.setAttribute("Name","estado_procedimiento");
        estado.setAttribute("Value",sCodEstado);
        extendedAtributesElement.addContent(estado);

        String sLocalizacion = "";
        if(dfpVO.getLocalizacion()!=null) sLocalizacion = dfpVO.getLocalizacion();
        Element localizacion = new Element ("ExtendedAttribute",XMLNS);
        localizacion.setAttribute("Name","localizacion_procedimiento");
        localizacion.setAttribute("Value",sLocalizacion);
        extendedAtributesElement.addContent(localizacion);

        String sUnidad = "";
        if(dfpVO.getCqUnidadInicio()!=null) sUnidad = dfpVO.getCqUnidadInicio();
        Element unidad = new Element ("ExtendedAttribute",XMLNS);
        unidad.setAttribute("Name","tipo_unidad_inicio_procedimiento");
        unidad.setAttribute("Value",sUnidad);
        extendedAtributesElement.addContent(unidad);


        if("0".equals(dfpVO.getCqUnidadInicio())){
            StringBuffer sUnidades = new StringBuffer();
            Vector<DefinicionProcedimientosValueObject> lista = dfpVO.getTablaUnidadInicio();
            for(int i=0;i<lista.size();i++){
                sUnidades.append(lista.get(i).getCodVisibleUnidadInicio());
                if(lista.size()-i>1){
                    sUnidades.append(ConstantesDatos.COMMA);
                }
            }//for

            Element unidades = new Element ("ExtendedAttribute",XMLNS);
            unidades.setAttribute("Name","unidades_inicio_procedimiento");
            unidades.setAttribute("Value",sUnidades.toString());
            extendedAtributesElement.addContent(unidades);
        }

        String sTramitacion = "";
        if(dfpVO.getTramitacionInternet()!=null) sTramitacion = dfpVO.getTramitacionInternet();
        Element tramitacionInternet = new Element ("ExtendedAttribute",XMLNS);
        tramitacionInternet.setAttribute("Name","visible_tramitacion_internet");
        tramitacionInternet.setAttribute("Value",sTramitacion);
        extendedAtributesElement.addContent(tramitacionInternet);

        String sInteresOblig = "";
        if(dfpVO.getInteresadoOblig()!=null) sInteresOblig = dfpVO.getInteresadoOblig(); 
        Element InteresadoOblig = new Element ("ExtendedAttribute",XMLNS);
        InteresadoOblig.setAttribute("Name","InteresadoObligatorio");
        InteresadoOblig.setAttribute("Value",sInteresOblig); 
        extendedAtributesElement.addContent(InteresadoOblig);
        
        String sDisponible = ""; 
        if(dfpVO.getDisponible()!=null) sDisponible = dfpVO.getDisponible();
        Element disponibleWebCiudadano = new Element ("ExtendedAttribute",XMLNS);
        disponibleWebCiudadano.setAttribute("Name","disponible_web_ciudadano");
        disponibleWebCiudadano.setAttribute("Value",sDisponible);
        extendedAtributesElement.addContent(disponibleWebCiudadano);
        //
        //  Desarrrollo para Lanbide
        //
        String sRestringido= dfpVO.getRestringido() != null ? dfpVO.getRestringido() : "";
        Element restringido = new Element ("ExtendedAttribute",XMLNS);
        restringido.setAttribute("Name","restringido");
        restringido.setAttribute("Value",sRestringido);
        extendedAtributesElement.addContent(restringido);
        
        String sLibreriaFlujo= dfpVO.getBiblioteca() != null ? dfpVO.getBiblioteca() : "";
        Element libreriaFlujo = new Element ("ExtendedAttribute",XMLNS);
        libreriaFlujo.setAttribute("Name","es_libreria_flujo");
        libreriaFlujo.setAttribute("Value",sLibreriaFlujo);
        extendedAtributesElement.addContent(libreriaFlujo);
        
        String sNumeroSegunAnotacion = String.valueOf(dfpVO.getNumeracionExpedientesAnoAsiento());
        Element numeroSegunAnotacion = new Element ("ExtendedAttribute",XMLNS);
        numeroSegunAnotacion.setAttribute("Name","numero_segun_anotacion");
        numeroSegunAnotacion.setAttribute("Value",sNumeroSegunAnotacion);
        extendedAtributesElement.addContent(numeroSegunAnotacion);
        
        String sSoloInicioWebService =  dfpVO.getSoloWS() != null ? dfpVO.getSoloWS() : "";
        Element soloInicioWebService = new Element ("ExtendedAttribute",XMLNS);
        soloInicioWebService.setAttribute("Name","solo_inicio_webservice");
        soloInicioWebService.setAttribute("Value",sSoloInicioWebService);
        extendedAtributesElement.addContent(soloInicioWebService);
         //
        //  Desarrrollo para Lanbide
        //
        
        String sTipoSilencio = "";
        if(dfpVO.getTipoSilencio()!=null && dfpVO.getTipoSilencio().length()>0) sTipoSilencio = dfpVO.getTipoSilencio();

        Element tipoSilencio = new Element ("ExtendedAttribute",XMLNS);
        tipoSilencio.setAttribute("Name","tipo_silencio");
        tipoSilencio.setAttribute("Value",sTipoSilencio);
        extendedAtributesElement.addContent(tipoSilencio);
        
        String sTipoInicioProcedimiento = "";
        if(dfpVO.getCodTipoInicio()!=null && dfpVO.getCodTipoInicio().length()>0) sTipoInicioProcedimiento = dfpVO.getCodTipoInicio();
        Element tipoInicioProc = new Element ("ExtendedAttribute",XMLNS);
        tipoInicioProc.setAttribute("Name","tipo_inicio_procedimiento");
        tipoInicioProc.setAttribute("Value",sTipoInicioProcedimiento);
        extendedAtributesElement.addContent(tipoInicioProc);
        
          
        if(StringOperations.stringNoNuloNoVacio(dfpVO.getCodServicioFinalizacion()) && StringOperations.stringNoNuloNoVacio(dfpVO.getImplClassServicioFinalizacion())){            
            //  Nombre del plugin
            Element codServicioFinalizacion = new Element ("ExtendedAttribute",XMLNS);
            codServicioFinalizacion.setAttribute("Name","cod_plugin_fin_no_convencional");
            codServicioFinalizacion.setAttribute("Value",dfpVO.getCodServicioFinalizacion());
            extendedAtributesElement.addContent(codServicioFinalizacion);
                        
            // Si el procedimiento tiene asignado un plugin de finalización de expediente de forma no convencional
            Element implClassFinNoConvencional = new Element ("ExtendedAttribute",XMLNS);
            implClassFinNoConvencional.setAttribute("Name","impl_class_fin_no_convencional");
            implClassFinNoConvencional.setAttribute("Value",dfpVO.getImplClassServicioFinalizacion());
            extendedAtributesElement.addContent(implClassFinNoConvencional);            
        }// if
        
        return extendedAtributesElement;
    }

        /**
     * Metodo que genera en el documento xpdl los posibles participantes (las Unidades
     * Organizativas ) que pueden participar en la tramitacion de un determinado
     * procedimiento.Mediante un HashMap se comprueban que no haya participantes repetidos.
     * @param tramites , Vector con los distintos tramites de un procedimiento cualquiera
     * @param procedimientos: Vector con la descripción de las unidades de inicio de los procedimientos
     * @return una Collection de elementos xml DataField con codigos y nombres de las UORs participantes
     */
    private static Collection anhadirParticipantes(Vector<DefinicionTramitesValueObject> tramites,Vector<DefinicionProcedimientosValueObject> unidadesProc){

        HashMap hashMapUORS = new HashMap();
        Collection resultado = new Vector();
        int posicion=0;

        for (DefinicionTramitesValueObject t:tramites){
            String codUnidadTramite = t.getCodUnidadTramite();

            // Se trata la unidad/es del trámite si se ha marcado en la definición del trámite "Otras"
            if (codUnidadTramite!=null && codUnidadTramite.equals(ConstantesDatos.TRA_UTR_OTRAS)){
               Vector<UORDTO> unidadesTramitadoras = t.getUnidadesTramitadoras();
               for(int i=0;unidadesTramitadoras!=null && i<unidadesTramitadoras.size();i++){
                    UORDTO uor = (UORDTO)unidadesTramitadoras.get(i);
                    Element participantElement = new Element("DataField",XMLNS);
                    participantElement.setAttribute("Id",uor.getUor_cod());
                    participantElement.setAttribute("Name","Unidad_Participante");

                    Element dataType = new Element("DataType",XMLNS);
                    Element basicType = new Element("BasicType",XMLNS);
                    basicType.setAttribute("Type","STRING");
                    dataType.addContent(basicType);
                    participantElement.addContent(dataType);
                    
                    Element extendAttributes = new Element("ExtendedAttributes",XMLNS);
                    
                    Element codigo = new Element ("ExtendedAttribute",XMLNS);
                    codigo.setAttribute("Name","codigo");
                    codigo.setAttribute("Value",uor.getUor_cod());
                    extendAttributes.addContent(codigo);

                    Element nombre = new Element ("ExtendedAttribute",XMLNS);
                    nombre.setAttribute("Name","nombre");
                    nombre.setAttribute("Value",uor.getUor_nom());
                    extendAttributes.addContent(nombre);
                    
                    participantElement.addContent(extendAttributes);
                    
                    if (!hashMapUORS.containsKey(uor.getUor_cod_vis())){
                        hashMapUORS.put(uor.getUor_cod_vis(), posicion);
                        posicion++;
                        resultado.add(participantElement);
                    }
               }// for
            }//if
        }//for

        for (DefinicionProcedimientosValueObject p:unidadesProc){
            p.getCodVisibleUnidadInicio();
            Element participantElement = new Element("DataField",XMLNS);
            participantElement.setAttribute("Id",p.getCodVisibleUnidadInicio());
            participantElement.setAttribute("Name","Unidad_Participante");
            
            Element dataType = new Element("DataType",XMLNS);
            Element basicType = new Element("BasicType",XMLNS);
            basicType.setAttribute("Type","STRING");
            dataType.addContent(basicType);
            participantElement.addContent(dataType);
            
            Element extendAttributes = new Element("ExtendedAttributes",XMLNS);

            Element codigo = new Element ("ExtendedAttribute",XMLNS);
            codigo.setAttribute("Name","codigo");
            codigo.setAttribute("Value",p.getCodVisibleUnidadInicio());
            extendAttributes.addContent(codigo);

            Element nombre = new Element ("ExtendedAttribute",XMLNS);
            nombre.setAttribute("Name","nombre");
            nombre.setAttribute("Value",p.getDescUnidadInicio());
            extendAttributes.addContent(nombre);

            participantElement.addContent(extendAttributes);
                    
            if (!hashMapUORS.containsKey(p.getCodVisibleUnidadInicio())){
                hashMapUORS.put(p.getCodVisibleUnidadInicio(), posicion);
                posicion++;
                resultado.add(participantElement);
            }//if
        }//for
        
        return resultado;
    }

    /**Pasa la información de un tramite a xpdl . En este caso se correspondera
     * con el tag Activity de xpdl . 
     * 
     * @param dtVO, tramite sobre el que se va a generar el campo Activity con la informacion
     * del tramite en xpdl
     * @return Un elemento xml con los datos del trámite
     */
    private static Element tramiteToXPDL(DefinicionTramitesValueObject dtVO,String[] params){
        
        Element activityElement = new Element ("Activity",XMLNS);        
        activityElement.setAttribute("Id",dtVO.getCodigoTramite());        
        hashMapTramites.put(dtVO.getCodigoTramite(), dtVO.getCodigoTramite());        
        activityElement.setAttribute("Name",dtVO.getNombreTramite());

        Element extendedAttributtes = new Element ("ExtendedAttributes",XMLNS);        
        extendedAttributtes.addContent(obtenerRestanteInformacionTramite(dtVO,params));
        extendedAttributtes.addContent(XPDLUtil.rellenarTramite(dtVO));
        activityElement.addContent(extendedAttributtes);
                        
        return activityElement;        
    }

    private static Collection obtenerRestanteInformacionTramite(DefinicionTramitesValueObject dtVO,String[] params){
         Collection resultado = new ArrayList();

         String sTramiteInicio  ="";
         if(dtVO.getTramiteInicio()!=null) sTramiteInicio= dtVO.getTramiteInicio();
         Element esTramiteInicio  = new Element ("ExtendedAttribute",XMLNS);
         esTramiteInicio.setAttribute("Name","es_tramite_inicio");
         esTramiteInicio.setAttribute("Value",sTramiteInicio);
         resultado.add(esTramiteInicio);

         String sCodVisible  ="";
         if(dtVO.getNumeroTramite()!=null) sCodVisible= dtVO.getNumeroTramite();
         Element codigoVisible = new Element ("ExtendedAttribute",XMLNS);
         codigoVisible.setAttribute("Name","codigo_visible");
         codigoVisible.setAttribute("Value",sCodVisible);
         resultado.add(codigoVisible);

         String sOcurrencias  ="";
         if(dtVO.getOcurrencias()!=null) sOcurrencias= dtVO.getOcurrencias();
         Element ocurrencias = new Element ("ExtendedAttribute",XMLNS);
         ocurrencias.setAttribute("Name","ocurrencias");
         ocurrencias.setAttribute("Value",sOcurrencias);
         resultado.add(ocurrencias);
         
         String sDisponible ="";
         if(dtVO.getDisponible()!=null) sDisponible = dtVO.getDisponible();
         
         Element visibleInternet = new Element ("ExtendedAttribute",XMLNS);
         visibleInternet.setAttribute("Name","visible_internet");
         visibleInternet.setAttribute("Value",sDisponible);
         resultado.add(visibleInternet);

         String sClasifTramite = "";
         if(dtVO.getCodClasifTramite()!=null) sClasifTramite = dtVO.getCodClasifTramite();
         Element clasifTramite = new Element ("ExtendedAttribute",XMLNS);
         clasifTramite.setAttribute("Name","codigo_clasificacion_tramite");
         clasifTramite.setAttribute("Value",sClasifTramite);
         resultado.add(clasifTramite);

         String sPlazo = "";
         if(dtVO.getPlazo()!=null) sPlazo = dtVO.getPlazo();
         Element plazo = new Element ("ExtendedAttribute",XMLNS);
         plazo.setAttribute("Name","plazo_notificacion");
         plazo.setAttribute("Value",sPlazo);
         resultado.add(plazo);
         
         String sPlazoFin = "";
         if(dtVO.getPlazoFin()>=0) sPlazoFin = Integer.toString(dtVO.getPlazoFin());
         Element plazoFin = new Element ("ExtendedAttribute",XMLNS);
         plazoFin.setAttribute("Name","plazo_fin");
         plazoFin.setAttribute("Value",sPlazoFin);
         resultado.add(plazoFin);

         String sUnidadesPlazo = "";
         if(dtVO.getUnidadesPlazo()!=null) sUnidadesPlazo=dtVO.getUnidadesPlazo();
         Element unidadesPlazo = new Element ("ExtendedAttribute",XMLNS);
         unidadesPlazo.setAttribute("Name","unidades_plazo");
         unidadesPlazo.setAttribute("Value",sUnidadesPlazo);
         resultado.add(unidadesPlazo);
         
        if(!"-99998".equals(dtVO.getCodUnidadInicio()) && !"-99999".equals(dtVO.getCodUnidadInicio()) && dtVO.getCodUnidadInicio()!=null && !"null".equals(dtVO.getCodUnidadInicio())){
           // HAY QUE RECUPERAR EL CÓDIGO DE UOR VISIBLE CORRESPONDIENTE AL CÓDIGO DE LA UNIDAD DE INICIO DEL TRÁMITE
           String sCod = "";
           if(dtVO.getCodUnidadInicio()!=null && !"".equals(dtVO.getCodUnidadInicio())) sCod = UORsManager.getInstance().getCodigoVisibleUorByCodUor(dtVO.getCodUnidadInicio(), params);
           Element codUnidadInicioManual = new Element ("ExtendedAttribute",XMLNS);
           codUnidadInicioManual.setAttribute("Name","codigo_unidad_inicio_manual");
           codUnidadInicioManual.setAttribute("Value",sCod);
           resultado.add(codUnidadInicioManual);

           String sDescripcion = "";
           if(dtVO.getDescUnidadInicio()!=null) sDescripcion = dtVO.getDescUnidadInicio();
           Element descripcionUnidadInicioManual = new Element ("ExtendedAttribute",XMLNS);
           descripcionUnidadInicioManual.setAttribute("Name","descripcion_unidad_inicio_manual");
           descripcionUnidadInicioManual.setAttribute("Value",sDescripcion);
           resultado.add(descripcionUnidadInicioManual);
        }//if
        else{
           String sCod = "";
           if(dtVO.getCodUnidadInicio()!=null && !"".equals(dtVO.getCodUnidadInicio())) sCod = dtVO.getCodUnidadInicio();
           Element codUnidadInicioManual = new Element ("ExtendedAttribute",XMLNS);
           codUnidadInicioManual.setAttribute("Name","codigo_unidad_inicio_manual");
           codUnidadInicioManual.setAttribute("Value",sCod);
           resultado.add(codUnidadInicioManual);
        }

         String sTipoUnidadTramite ="";
         if(dtVO.getCodUnidadTramite()!=null) sTipoUnidadTramite = dtVO.getCodUnidadTramite();
         Element tipoUnidadTramite  = new Element ("ExtendedAttribute",XMLNS);
         tipoUnidadTramite.setAttribute("Name","tipo_unidad_tramite");
         tipoUnidadTramite.setAttribute("Value",sTipoUnidadTramite);
         resultado.add(tipoUnidadTramite);

         String sTramitePregunta = "";
         if(dtVO.getTramitePregunta()!=null) sTramitePregunta = dtVO.getTramitePregunta();
         Element soloEsta  = new Element ("ExtendedAttribute",XMLNS);
         soloEsta.setAttribute("Name","solo_esta");
         soloEsta.setAttribute("Value",sTramitePregunta);
         resultado.add(soloEsta);

         String sCodCargo ="";
         if(dtVO.getCodCargo()!=null) sCodCargo = dtVO.getCodCargo();
         Element cargo = new Element ("ExtendedAttribute",XMLNS);
         cargo.setAttribute("Name","codigo_cargo");
         cargo.setAttribute("Value",sCodCargo);
         resultado.add(cargo);
         
         String sCodVisibleCargo ="";
         if(dtVO.getCodVisibleCargo()!=null) sCodVisibleCargo=dtVO.getCodVisibleCargo();
         Element cargoVisible = new Element ("ExtendedAttribute",XMLNS);
         cargoVisible.setAttribute("Name","codigo_cargo_visible");
         cargoVisible.setAttribute("Value",sCodVisibleCargo);
         resultado.add(cargoVisible);
         
         String sIntruc = "";
         if(dtVO.getInstrucciones()!=null) sIntruc = AdaptadorSQLBD.js_unescape(dtVO.getInstrucciones());
         Element instrucciones = new Element ("ExtendedAttribute",XMLNS);
         instrucciones.setAttribute("Name","instrucciones");
         instrucciones.setAttribute("Value",sIntruc);
         resultado.add(instrucciones);
         
         Element generarPlazo = new Element ("ExtendedAttribute",XMLNS);
         generarPlazo.setAttribute("Name","generar_plazo");
         generarPlazo.setAttribute("Value",Boolean.toString(dtVO.isGenerarPlazos()));
         resultado.add(generarPlazo);

         Element notificarCercaFinPlazo = new Element ("ExtendedAttribute",XMLNS);
         notificarCercaFinPlazo.setAttribute("Name","notificar_cerca_fin_plazo");
         notificarCercaFinPlazo.setAttribute("Value",Boolean.toString(dtVO.getNotificarCercaFinPlazo()));
         resultado.add(notificarCercaFinPlazo);

         Element notificarFueraPlazo = new Element ("ExtendedAttribute",XMLNS);
         notificarFueraPlazo.setAttribute("Name","notificar_fuera_plazo");
         notificarFueraPlazo.setAttribute("Value",Boolean.toString(dtVO.getNotificarFueraDePlazo()));
         resultado.add(notificarFueraPlazo);

         Element tipoNotCercaFinPlazo = new Element ("ExtendedAttribute",XMLNS);
         tipoNotCercaFinPlazo.setAttribute("Name","tipo_no_cerca_fin_plazo");
         tipoNotCercaFinPlazo.setAttribute("Value",Integer.toString(dtVO.getTipoNotCercaFinPlazo()));
         resultado.add(tipoNotCercaFinPlazo);

         Element tipoNotFueraPlazo = new Element ("ExtendedAttribute",XMLNS);
         tipoNotFueraPlazo.setAttribute("Name","tipo_no_fuera_plazo");
         tipoNotFueraPlazo.setAttribute("Value",Integer.toString(dtVO.getTipoNotFueraDePlazo()));
         resultado.add(tipoNotFueraPlazo);

        String codUnidadTramite = dtVO.getCodUnidadTramite();
        // Se trata la unidad/es del trámite si se ha marcado en la definición del trámite "Otras"
        if (codUnidadTramite!=null && codUnidadTramite.equals(ConstantesDatos.TRA_UTR_OTRAS)){
            StringBuffer dato = new StringBuffer();
            Vector<UORDTO> unidadesTramitadoras = dtVO.getUnidadesTramitadoras();
            
            for(int i=0;unidadesTramitadoras!=null && i<unidadesTramitadoras.size();i++){            
                UORDTO uor = (UORDTO)unidadesTramitadoras.get(i);
                dato.append(uor.getUor_cod_vis());
                if(unidadesTramitadoras.size()-i>1)
                    dato.append(ConstantesDatos.COMMA);
            }// if
            
           Element unidades = new Element ("ExtendedAttribute",XMLNS);
           unidades.setAttribute("Name","unidades_tramitadoras");
           unidades.setAttribute("Value",dato.toString());
           resultado.add(unidades);           
        }//if

       String sNotUnidadTramitIni = "";
        if(dtVO.getNotUnidadTramitIni()!=null && "1".equals(dtVO.getNotUnidadTramitIni()))
            sNotUnidadTramitIni = "S";
        else
            sNotUnidadTramitIni = "N";

       Element notUnidadTramitIni = new Element ("ExtendedAttribute",XMLNS);
       notUnidadTramitIni.setAttribute("Name","not_unidad_tramit_ini");
       notUnidadTramitIni.setAttribute("Value",sNotUnidadTramitIni);
       resultado.add(notUnidadTramitIni);
       
       String sNotUnidadTramitFin = "";
        if(dtVO.getNotUnidadTramitFin()!=null && "1".equals(dtVO.getNotUnidadTramitFin()))
            sNotUnidadTramitFin = "S";
        else
            sNotUnidadTramitFin = "N";

       Element notUnidadTramitFin = new Element ("ExtendedAttribute",XMLNS);
       notUnidadTramitFin.setAttribute("Name","not_unidad_tramit_fin");
       notUnidadTramitFin.setAttribute("Value",sNotUnidadTramitFin);
       resultado.add(notUnidadTramitFin);

       String sNotUsuUnidadTramitIni = "";
       if(dtVO.getNotUsuUnidadTramitIni()!=null && "1".equals(dtVO.getNotUsuUnidadTramitIni()))
            sNotUsuUnidadTramitIni = "S";
       else
            sNotUsuUnidadTramitIni = "N";

       Element ab = new Element ("ExtendedAttribute",XMLNS);
       ab.setAttribute("Name","not_usu_unidad_tramit_ini");
       ab.setAttribute("Value",sNotUsuUnidadTramitIni);
       resultado.add(ab);

       String sNotUsuUnidadTramitFin = "";
       if(dtVO.getNotUsuUnidadTramitFin()!=null && "1".equals(dtVO.getNotUsuUnidadTramitFin()))
            sNotUsuUnidadTramitFin = "S";
       else
            sNotUsuUnidadTramitFin = "N";

       Element abc = new Element ("ExtendedAttribute",XMLNS);
       abc.setAttribute("Name","not_usu_unidad_tramit_fin");
       abc.setAttribute("Value",sNotUsuUnidadTramitFin);
       resultado.add(abc);


       String sNotInteresadosIni = "";
       if(dtVO.getNotInteresadosIni()!=null && "1".equals(dtVO.getNotInteresadosIni()))
            sNotInteresadosIni = "S";
       else
            sNotInteresadosIni = "N";

       Element abcd = new Element ("ExtendedAttribute",XMLNS);
       abcd.setAttribute("Name","not_interesados_ini");
       abcd.setAttribute("Value",sNotInteresadosIni);
       resultado.add(abcd);

       String sNotInteresadosFin = "";
       if(dtVO.getNotInteresadosFin()!=null && "1".equals(dtVO.getNotInteresadosFin()))
            sNotInteresadosFin = "S";
       else
            sNotInteresadosFin = "N";

       Element abcde = new Element ("ExtendedAttribute",XMLNS);
       abcde.setAttribute("Name","not_interesados_fin");
       abcde.setAttribute("Value",sNotInteresadosFin);
       resultado.add(abcde);

       String sCodigoExpedienteRelacionado = "";
       if(dtVO.getCodExpRel()!=null) sCodigoExpedienteRelacionado = dtVO.getCodExpRel();
       Element abcdef = new Element ("ExtendedAttribute",XMLNS);
       abcdef.setAttribute("Name","codigo_expediente_relacionado");
       abcdef.setAttribute("Value",sCodigoExpedienteRelacionado);
       resultado.add(abcdef);
       
        return resultado;
    }

    /**
     * Genera las transiciones que se producen entre los distintos tramites, lo cual 
     * indica un determinado orden entre los trámites. Mediante estas secuencias se posibilita
     * la definición de un flujograma entre los trámites de un procedimiento
     * @param tramite, trámite para el que se calculan sus posibles transiciones. 
     * @return un elemento xml Transition con las transciones desde ese trámite hacia otros. 
     */
    private static Collection transicionesToXPDL(DefinicionTramitesValueObject tramite ){
        
        Collection resultado = new Vector();
        Vector<DefinicionTramitesValueObject> tramites = tramite.getListaTramitesFavorable();
        Vector tramitesDesfavorable = tramite.getListaTramitesDesfavorable();
        String origen = (String)hashMapTramites.get(tramite.getCodigoTramite());                
            
        if ((tramites!=null && tramites.size()>0) || (tramitesDesfavorable!=null && tramitesDesfavorable.size()>0)) {
            Element  transicionCond = new Element("Transition",XMLNS);
            Element  chave = new Element("Activity",XMLNS);
            transicionCond.setAttribute("Id",origen+"salida");
            transicionCond.setAttribute("From",origen);
            transicionCond.setAttribute("To",origen+"condSalida");
            chave.setAttribute("Id",origen+"condSalida");
            Element rota = new Element ("Route",XMLNS);
            rota.setAttribute("GatewayType","Exclusive");
            chave.addContent(rota);
            resultado.add(chave);
            resultado.add(transicionCond);
        }
        
        if (tramites!=null)           
            for (DefinicionTramitesValueObject trDestino:tramites){
                // Almacena para un trámite los códigos de las diferentes condiciones de entrada de tipo trámite por el orden por el que se van tratando
                StringBuffer codigosCondicionesEntradaTipoTramite    = new StringBuffer();
                // Almacena el código de la condición de entrada de tipo expresión que puede tener en el trámite entre sus condiciones de entrada
                StringBuffer codigoCondicionEntradaTipoExpresion = new StringBuffer();

                Element  transicionSaida = new Element("Transition",XMLNS);
                String codTramite = trDestino.getIdTramiteFlujoSalida();
                codTramite = parsearEntero(codTramite);
                String destino = (String)hashMapTramites.get(codTramite);
                if(destino==null) destino = codTramite;

                transicionSaida.setAttribute("Id",origen+"condSalida-"+destino);
                transicionSaida.setAttribute("From",origen+"condSalida");
                transicionSaida.setAttribute("To",destino);
                Element extendedAttributesElement = new Element ("ExtendedAttributes",XMLNS);
                
                if(codigosCondicionesEntradaTipoTramite.length()>=1 && codigoCondicionEntradaTipoExpresion.length()>=1){
                    // Se añaden los códigos de cada condición de entrada como atributo extendido y separador por punto y coma
                    Element extendedAttributeCodCondicion =  new Element ("ExtendedAttribute",XMLNS);
                    extendedAttributeCodCondicion.setAttribute("Name","codigos_condiciones_entrada");
                    extendedAttributeCodCondicion.setAttribute("Value",codigosCondicionesEntradaTipoTramite.toString() +
                            ConstantesDatos.COMMA + codigoCondicionEntradaTipoExpresion.toString());
                    extendedAttributesElement.addContent(extendedAttributeCodCondicion);
                }
                
                Element eListaFavorables = new Element("ExtendedAttribute",XMLNS);
                eListaFavorables.setAttribute("Name","lista_favorables");
                eListaFavorables.setAttribute("Value","SI");
                extendedAttributesElement.addContent(eListaFavorables);

                transicionSaida.addContent(extendedAttributesElement);
                resultado.add(transicionSaida);
            }//for
        
        if (tramitesDesfavorable!=null)
            for (Object tr:tramitesDesfavorable){
                DefinicionTramitesValueObject trDestino = (DefinicionTramitesValueObject)tr;                            
                Element  transicionSaida = new Element("Transition",XMLNS);
                //String codTramite = trDestino.getCodTramiteFlujoSalida();
                String codTramite = trDestino.getIdTramiteFlujoSalida();

                log.debug(" ===================> TRATANDO TRANSICIONES PARA TRAMITES DESFAVORABLES PARA TRÁMITE DE ORIGEN:  " + origen);
                log.debug(" ===================> TRATANDO TRANSICIONES PARA TRAMITES DESFAVORABLES PARA codTramiteFlujoSalida:  " + trDestino.getCodTramiteFlujoSalida());
                log.debug(" ===================> TRATANDO TRANSICIONES PARA TRAMITES DESFAVORABLES PARA idTramiteFlujoSalida:  " + trDestino.getIdTramiteFlujoSalida());

                codTramite = parsearEntero(codTramite);
                String destino = (String)hashMapTramites.get(codTramite);
                log.debug("=================> DESTINO EXTRAIDO DE HASHMAPTRAMITES " + destino);
                if(destino==null) destino = codTramite;
                log.debug("=================> DESTINO DESPUÉS DE TRATAMIENTO: " + destino);

                transicionSaida.setAttribute("Id",origen+"condSalidaDesf-"+destino);
                transicionSaida.setAttribute("From",origen+"condSalida");
                transicionSaida.setAttribute("To",destino);
                
                Element extendedAttributesElement = new Element ("ExtendedAttributes",XMLNS);
                Element eListaFavorables = new Element("ExtendedAttribute",XMLNS);
                eListaFavorables.setAttribute("Name","lista_desfavorables");
                eListaFavorables.setAttribute("Value","SI");
                extendedAttributesElement.addContent(eListaFavorables);
                transicionSaida.addContent(extendedAttributesElement);
                
                resultado.add(transicionSaida);
            }
        
        return resultado;
            
    }

  /**
     * Añade al documento xpdl la definición de los enlaces del procedimiento
     * @param ennlaces: Vector<DefinicionProcedimientosValueObject>
     * @return una Collection de elementos xml DataField con las variables de las condiciones de entrada tipo
     * expresión, siempre que no hayan sido añadidas
     */
    public static Collection anhadirEnlaces (Vector<DefinicionProcedimientosValueObject> enlaces,String tipo,String codTramite){
        Collection resultado = new Vector();
        
        for(int i=0;i<enlaces.size();i++){
            DefinicionProcedimientosValueObject dfVO = enlaces.get(i);
            String identificador = "";
            if(tipo.equals(ENLACE_PROCEDIMIENTO))
                identificador = tipo + ConstantesDatos.GUION + dfVO.getCodEnlace();
            else
                identificador = tipo + ConstantesDatos.GUION + codTramite + ConstantesDatos.GUION + dfVO.getCodEnlace();
            
            Element dataFieldElement = new Element("DataField",XMLNS);
            dataFieldElement.setAttribute("Id",identificador);
            dataFieldElement.setAttribute("Name",tipo);
            
            Element dataType = new Element("DataType",XMLNS);
            Element basicType = new Element("BasicType",XMLNS);
            basicType.setAttribute("Type","STRING");
            dataType.addContent(basicType);
            dataFieldElement.addContent(dataType);
            
            Element extendAttributes = new Element("ExtendedAttributes",XMLNS);
            Element codigo = new Element("ExtendedAttribute",XMLNS);
            codigo.setAttribute("Name","codigo");
            codigo.setAttribute("Value",dfVO.getCodEnlace());
            // Se añade el código del enlace
            extendAttributes.addContent(codigo);

            if(tipo.equals(ENLACE_TRAMITE) && codTramite!=null){
                Element codigoTramite = new Element("ExtendedAttribute",XMLNS);
                codigoTramite.setAttribute("Name","codigoTramite");
                codigoTramite.setAttribute("Value",codTramite);
                // Si el enlace pertenece a un trámite, se indica el código del trámite
                extendAttributes.addContent(codigoTramite);
            }

            Element descripcion = new Element("ExtendedAttribute",XMLNS);
            descripcion.setAttribute("Name","descripcion");
            descripcion.setAttribute("Value",dfVO.getDescEnlace());
            // Se añade la descripción del enlace
            extendAttributes.addContent(descripcion);

            Element url = new Element("ExtendedAttribute",XMLNS);
            url.setAttribute("Name","url");
            url.setAttribute("Value",dfVO.getUrlEnlace());
            // Se añade la url del enlace
            extendAttributes.addContent(url);

            Element estado  = new Element("ExtendedAttribute",XMLNS);
            estado.setAttribute("Name","estado");
            estado.setAttribute("Value",dfVO.getEstadoEnlace());
            // Se añade el estado del enlace
            extendAttributes.addContent(estado);
            dataFieldElement.addContent(extendAttributes);
            resultado.add(dataFieldElement);            
        }
        return resultado;
    }

  /**
     * Añade los roles de un procedimento al documento XPDL
     * @param ennlaces: Vector<DefinicionProcedimientosValueObject>
     * @return una Collection de elementos xml DataField con las variables de las condiciones de entrada tipo
     * expresión, siempre que no hayan sido añadidas
     */
    public static Collection anhadirRolesProcedimiento(Vector<DefinicionProcedimientosValueObject> roles){
        Collection resultado = new Vector();

        for(int i=0;i<roles.size();i++){
            DefinicionProcedimientosValueObject dfVO = roles.get(i);
            Element dataFieldElement = new Element("DataField",XMLNS);
            dataFieldElement.setAttribute("Id","Rol_Procedimiento-"+ dfVO.getCodRol());
            dataFieldElement.setAttribute("Name","Roles_Procedimiento");
            
            Element dataType = new Element("DataType",XMLNS);
            Element basicType = new Element("BasicType",XMLNS);
            basicType.setAttribute("Type","STRING");
            dataType.addContent(basicType);
            dataFieldElement.addContent(dataType);
            
            Element extendAttributes = new Element("ExtendedAttributes",XMLNS);
            Element codigo = new Element("ExtendedAttribute",XMLNS);
            codigo.setAttribute("Name","codigo");
            codigo.setAttribute("Value",dfVO.getCodRol());
            // Se añade el código del enlace
            extendAttributes.addContent(codigo);

            Element descripcion = new Element("ExtendedAttribute",XMLNS);
            descripcion.setAttribute("Name","descripcion");
            descripcion.setAttribute("Value",dfVO.getDescRol());
            // Se añade la descripción del enlace
            extendAttributes.addContent(descripcion);

            Element defecto = new Element("ExtendedAttribute",XMLNS);
            defecto.setAttribute("Name","defecto");
            defecto.setAttribute("Value",dfVO.getRolPorDefecto());
            // Se añade la url del enlace
            extendAttributes.addContent(defecto);

            Element consultaWeb  = new Element("ExtendedAttribute",XMLNS);
            consultaWeb.setAttribute("Name","estado");
            consultaWeb.setAttribute("Value",dfVO.getConsultaWebRol());
            // Se añade la url del enlace
            extendAttributes.addContent(consultaWeb);
            dataFieldElement.addContent(extendAttributes);
            resultado.add(dataFieldElement);
        }
        return resultado;
    }



  /**
     * Añade los documentos definidos para un procedimento al XPDL
     * @param documentos: Vector<DefinicionProcedimientosValueObject> con la definición de los documentos del procedimiento
     * @return una Collection de elementos xml DataField con la definición de los documentos del procedimiento
     */
    public static Collection anhadirDocumentosProcedimiento (Vector<DefinicionProcedimientosValueObject> documentos){
        Collection resultado = new Vector();

        for(int i=0;i<documentos.size();i++){
            DefinicionProcedimientosValueObject dfVO = documentos.get(i);
            Element dataFieldElement = new Element("DataField",XMLNS);
            dataFieldElement.setAttribute("Id","Documento" + ConstantesDatos.GUION + dfVO.getCodigoDocumento());
            dataFieldElement.setAttribute("Name","Documento_Procedimiento");
            
            Element dataType = new Element("DataType",XMLNS);
            Element basicType = new Element("BasicType",XMLNS);
            basicType.setAttribute("Type","STRING");
            dataType.addContent(basicType);
            dataFieldElement.addContent(dataType);

            Element extendAttributes = new Element("ExtendedAttributes",XMLNS);
            
            Element codigo = new Element("ExtendedAttribute",XMLNS);
            codigo.setAttribute("Name","codigo");
            codigo.setAttribute("Value",dfVO.getCodigoDocumento());
            // Se añade el nombre del documento del procedimiento
            extendAttributes.addContent(codigo);
            
            Element nombre = new Element("ExtendedAttribute",XMLNS);
            nombre.setAttribute("Name","nombre");
            nombre.setAttribute("Value",dfVO.getNombreDocumento());

            // Se añade el nombre del documento del procedimiento
            extendAttributes.addContent(nombre);
            
            Element condicion = new Element("ExtendedAttribute",XMLNS);
            condicion.setAttribute("Name","condicion");
            condicion.setAttribute("Value",dfVO.getCondicion());
            // Se añade la condición del documento
            extendAttributes.addContent(condicion);
            
            dataFieldElement.addContent(extendAttributes);
            resultado.add(dataFieldElement);            
        }
        return resultado;
    }

    /**
     * Añade los documentos definidos para un procedimento al XPDL
     * @param documentos: Vector<DefinicionProcedimientosValueObject> con la definición de los documentos del procedimiento
     * @return una Collection de elementos xml DataField con la definición de los documentos del procedimiento
     */
    public static Collection anhadirFirmasDocumentoProcedimiento (ArrayList<FirmasDocumentoProcedimientoVO> firmasDocumentoProcedimiento){
        Collection resultado = new Vector();

        for(int i=0;i<firmasDocumentoProcedimiento.size();i++){
            FirmasDocumentoProcedimientoVO fDPVO = firmasDocumentoProcedimiento.get(i);
            Element dataFieldElement = new Element("DataField",XMLNS);
            dataFieldElement.setAttribute("Id","FirmaDocumento" + ConstantesDatos.GUION + i);
            dataFieldElement.setAttribute("Name","Firma_Documento_Procedimiento");
            
            Element dataType = new Element("DataType",XMLNS);
            Element basicType = new Element("BasicType",XMLNS);
            basicType.setAttribute("Type","STRING");
            dataType.addContent(basicType);
            dataFieldElement.addContent(dataType);

            Element extendAttributes = new Element("ExtendedAttributes",XMLNS);

            if (fDPVO.getUsuario()!=null) {
                Element firma = new Element("ExtendedAttribute",XMLNS);
                firma.setAttribute("Name","firmaUsuario");
                firma.setAttribute("Value",fDPVO.getUsuario());
                // Se añade el nombre del documento del procedimiento
                extendAttributes.addContent(firma);
            }
                
            if (fDPVO.getOrden()!=null) {
                Element orden = new Element("ExtendedAttribute",XMLNS);
                orden.setAttribute("Name","firmaOrden");
                orden.setAttribute("Value",fDPVO.getOrden());
                // Se añade el nombre del documento del procedimiento
                extendAttributes.addContent(orden);
            }
            
            Element codigoDocumento = new Element("ExtendedAttribute",XMLNS);
            codigoDocumento.setAttribute("Name","codigoDocumento");
            codigoDocumento.setAttribute("Value",fDPVO.getCodDocumento());
            // Se añade la condición del documento
            extendAttributes.addContent(codigoDocumento);
            
            if(fDPVO.getUor()!=null){
                Element uor = new Element("ExtendedAttribute",XMLNS);
                uor.setAttribute("Name","uor");            
                uor.setAttribute("Value",fDPVO.getUor());
                extendAttributes.addContent(uor);
            }
            
            if( fDPVO.getCargo()!=null){
                Element cargo = new Element("ExtendedAttribute",XMLNS);
                cargo.setAttribute("Name","cargo");            
                cargo.setAttribute("Value",fDPVO.getCargo());            
                cargo.setAttribute("Value","");
                extendAttributes.addContent(cargo);
            }

            Element finalizarRechazar = new Element("ExtendedAttribute",XMLNS);
            finalizarRechazar.setAttribute("Name","finalizarRechazar");            
            finalizarRechazar.setAttribute("Value",fDPVO.getFinalizaRechazo());
            extendAttributes.addContent(finalizarRechazar);

            Element tramitarSubsanacion = new Element("ExtendedAttribute",XMLNS);
            tramitarSubsanacion.setAttribute("Name","tramitarSubsanacion");            
            tramitarSubsanacion.setAttribute("Value",fDPVO.getTramitar());
            extendAttributes.addContent(tramitarSubsanacion);

            dataFieldElement.addContent(extendAttributes);
            resultado.add(dataFieldElement);
        }
        return resultado;
    }

    /**
     * Añade los documentos de tramitación al XPDL
     * @param documentos: Vector<DefinicionTramitesValueObject> con la definición de los documentos del procedimiento
     * @param codTramite: String que contiene el código del trámite al que pertenecen las plantillas, a partir de las cuales, se
     * crearán los documentos de tramitación
     * @return una Collection de elementos xml DataField con la definición de los documentos del procedimiento
     */
    public static Collection anhadirDocumentosTramite (Vector<DefinicionTramitesValueObject> documentos,String codTramite){
        Collection resultado = new Vector();

        for(int i=0;i<documentos.size();i++){
            DefinicionTramitesValueObject dfVO = documentos.get(i);
            Element dataFieldElement = new Element("DataField",XMLNS);
            String id = "DOCUMENTO" + ConstantesDatos.GUION + codTramite + ConstantesDatos.GUION + dfVO.getCodPlantilla() + ConstantesDatos.GUION +dfVO.getCodigoDoc();
            log.debug(" ******** anhadirDocumentosTramite() id: " + id);
            dataFieldElement.setAttribute("Id",id);
            dataFieldElement.setAttribute("Name","Documento_Tramite");
            
            Element dataType = new Element("DataType",XMLNS);
            Element basicType = new Element("BasicType",XMLNS);
            basicType.setAttribute("Type","STRING");
            dataType.addContent(basicType);
            dataFieldElement.addContent(dataType);

            Element extendAttributes = new Element("ExtendedAttributes",XMLNS);
            Element codigo = new Element("ExtendedAttribute",XMLNS);
            codigo.setAttribute("Name","codigo");
            codigo.setAttribute("Value", parsearString(dfVO.getCodigoDoc()));
            // Se añade el nombre del documento del procedimiento
            extendAttributes.addContent(codigo);
            

            Element codigoTramite = new Element("ExtendedAttribute",XMLNS);
            codigoTramite.setAttribute("Name","codigoTramite");
            codigoTramite.setAttribute("Value",parsearString(codTramite));
            // Se añade el código del trámite
            extendAttributes.addContent(codigoTramite);
            
            String nombreDoc="";
            Element nombre = new Element("ExtendedAttribute",XMLNS);
            nombre.setAttribute("Name","nombre");
            if(dfVO.getNombreDoc()!=null)
                nombreDoc = dfVO.getNombreDoc();
            nombre.setAttribute("Value",nombreDoc);
            // Se añade el nombre del documento del procedimiento
            extendAttributes.addContent(nombre);

            String codigoTipoDocumento = "";
            if(dfVO.getCodTipoDoc()!=null && !"".equals(dfVO.getCodTipoDoc()))
                codigoTipoDocumento = dfVO.getCodTipoDoc();

            Element codTipoDocumento = new Element("ExtendedAttribute",XMLNS);
            codTipoDocumento.setAttribute("Name","codTipoDocumento");
            codTipoDocumento.setAttribute("Value",codigoTipoDocumento);
            // Se añade el código de tipo de documento
            extendAttributes.addContent(codTipoDocumento);

            String visibleInternet = "";
            if(dfVO.getVisibleInternet()!=null )
                visibleInternet = dfVO.getVisibleInternet();
            
            Element visible = new Element("ExtendedAttribute",XMLNS);
            visible.setAttribute("Name","visibleInternet");
            visible.setAttribute("Value",visibleInternet);
            // Se indica si el documento es o no visible
            extendAttributes.addContent(visible);

            String firmaDoc = "";
            if(dfVO.getFirma()!=null){
                firmaDoc = dfVO.getFirma();
            }else{
                firmaDoc = ConstantesDatos.XPDL_EXPORTACION_N;
            }
            Element firma = new Element("ExtendedAttribute",XMLNS);
            firma.setAttribute("Name","firma");
            firma.setAttribute("Value",firmaDoc);
            // Se añade la firma (el tipo de la firma: L,U,O,...)
            extendAttributes.addContent(firma);
            
            // =================
            // DATOS FLUJO FIRMA 
            // =================
			
            // Se añade más información sobre la firma: en caso de tratarse de firma de usuario: datos del usuario firmante;
			// en caso de existir un flujo de firmas: datos sobre el flujo en sí (id) o texto flujo no válido si el flujo con el id indicado en bd no existe
			Integer firmaDocumentoIdUsuario = dfVO.getFirmaDocumentoIdUsuario();
			if(firmaDocumentoIdUsuario != null && firmaDocumentoIdUsuario != -1){
				Element elFirmaDocIdUsuario = new Element("ExtendedAttribute", XMLNS);
				elFirmaDocIdUsuario.setAttribute("Name", "firmaDocumentoIdUsuario");
				elFirmaDocIdUsuario.setAttribute("Value", parsearInteger(dfVO.getFirmaDocumentoIdUsuario()));
				extendAttributes.addContent(elFirmaDocIdUsuario);

				Element elFirmaDocLogUsuario = new Element("ExtendedAttribute", XMLNS);
				elFirmaDocLogUsuario.setAttribute("Name", "firmaDocumentoUsuarioLog");
				elFirmaDocLogUsuario.setAttribute("Value", parsearString(dfVO.getFirmaDocumentoLogUsuario()));
				extendAttributes.addContent(elFirmaDocLogUsuario);
                                
                                Element elFirmaDocNifUsuario = new Element("ExtendedAttribute", XMLNS);
				elFirmaDocNifUsuario.setAttribute("Name", "firmaDocumentoDniUsuario");
				elFirmaDocNifUsuario.setAttribute("Value", parsearString(dfVO.getFirmadocumentoDniUsuario()));
				extendAttributes.addContent(elFirmaDocNifUsuario);
	           } else {
                FirmaFlujoVO firmaFlujo = dfVO.getFirmaFlujo();
                if (firmaFlujo != null) {
                    anhadirDatosFirmasDocumento(extendAttributes, firmaFlujo);
                }
            }

                // Elementos de FirmaCircuito
                // --------------------------
                // Añadido en Flexia18 ?¿?¿
//                FirmaCircuitoVO firmaCircuito = dfVO.getFirmaCircuito();
//                if (firmaCircuito != null) {
//                    // idUsuario (FIRMA_CIRCUITO : ID_USUARIO)
//                    Element idUsuario = new Element("ExtendedAttribute", XMLNS);
//                    idUsuario.setAttribute("Name", "firmaCircuitoIdUsuario");
//                    idUsuario.setAttribute("Value", parsearInteger(firmaCircuito.getIdUsuario()));
//                    extendAttributes.addContent(idUsuario);
//
//                    // orden (FIRMA_CIRCUITO : ORDEN)
//                    Element firmaCircuitoOrden = new Element("ExtendedAttribute", XMLNS);
//                    firmaCircuitoOrden.setAttribute("Name", "firmaCircuitoOrden");
//                    firmaCircuitoOrden.setAttribute("Value", parsearInteger(firmaCircuito.getOrden()));
//                    extendAttributes.addContent(firmaCircuitoOrden);
//
//                    // log (FIRMA_CIRCUITO : ID_USUARIO)
//                    Element firmaLogUsuario = new Element("ExtendedAttribute", XMLNS);
//                    firmaLogUsuario.setAttribute("Name", "firmaCircuitoLogUsuario");
//                    firmaLogUsuario.setAttribute("Value", parsearString(firmaCircuito.getNombreUsuario()));
//                    extendAttributes.addContent(firmaLogUsuario);
//                }
//            }
            
            
            
            // ===============
            // DATOS PLANTILLA
            // ===============
            
            String plantillaDoc = "";
            if(dfVO.getPlantilla()!=null)
                plantillaDoc = dfVO.getPlantilla();
            
            Element plantilla = new Element("ExtendedAttribute",XMLNS);
            plantilla.setAttribute("Name","plantilla");
            plantilla.setAttribute("Value",plantillaDoc);
            // Se añade la plantilla
            extendAttributes.addContent(plantilla);
            
            String contentPlantilla = "";
            if(dfVO.getContidoPlantilla()!=null)
                contentPlantilla = dfVO.getContidoPlantilla();
            
            Element contPlantilla = new Element("ExtendedAttribute",XMLNS);
            contPlantilla.setAttribute("Name","contPlantilla");
            contPlantilla.setText(contentPlantilla);
            // Se añade el código de la plantilla
            extendAttributes.addContent(contPlantilla);

            Element interesado = new Element("ExtendedAttribute",XMLNS);
            interesado.setAttribute("Name","interesado");
            if(dfVO.getInteresado()!=null && !"".equals(dfVO.getInteresado()))
                interesado.setAttribute("Value",dfVO.getInteresado());
            else
                interesado.setAttribute("Value","N");
            // Se añade el interesado
            extendAttributes.addContent(interesado);
                        
            Element docActivo= new Element("ExtendedAttribute",XMLNS);
            docActivo.setAttribute("Name","docActivo");
            if(dfVO.getDocActivo()!=null && !"".equals(dfVO.getDocActivo()))
                docActivo.setAttribute("Value", dfVO.getDocActivo());
            else
                interesado.setAttribute("Value","NO");
            // Se indica si el documento está activo
            extendAttributes.addContent(docActivo);
            
            Element relacion= new Element("ExtendedAttribute",XMLNS);
            relacion.setAttribute("Name","relacion");
            
            if(dfVO.getRelacion()!=null && !"".equals(dfVO.getRelacion()))
                relacion.setAttribute("Value", dfVO.getRelacion());
            else
                relacion.setAttribute("Value","N");
            // Se indica relacion
            extendAttributes.addContent(relacion);
            
            
            String editorTextoDoc = "";
            if (dfVO.getEditorTexto() != null) {
                editorTextoDoc = dfVO.getEditorTexto();
            }

            Element editorTexto = new Element("ExtendedAttribute", XMLNS);
            editorTexto.setAttribute("Name", "editorTexto");
            editorTexto.setAttribute("Value", editorTextoDoc);
            // Se indica editorTexto
            extendAttributes.addContent(editorTexto);

            dataFieldElement.addContent(extendAttributes);
            resultado.add(dataFieldElement);
        }
        
        return resultado;
    }

    private static String parsearInteger(Integer n) {
        return (n != null) ? String.valueOf(n) : "";
    }

    private static String parsearString(String s) {
        return (s != null) ? s : "";
    }

    /**
     * Añade las condiciones de entrada de un trámite al documento XPDL
     * @param condiciones: Vector<DefinicionTramitesValueObject> con las condiciones de entrada de un trámite
     * @param codTramite: código del trámite
     * @param codProcedimiento: Código del procedimiento
     * @param codMunicipio: Código del municipio u organización
     * @return una Collection de elementos xml DataField con las condiciones de entrada de un determinado trámite
     */
    public static Collection anhadirCondicionesEntradaTramite (Vector<DefinicionTramitesValueObject> condiciones,String codTramite,String codProcedimiento,String codMunicipio){
        Collection resultado = new Vector();

        for(int i=0;i<condiciones.size();i++){
            DefinicionTramitesValueObject condicion = condiciones.get(i);
            Element dataFieldElement = new Element("DataField",XMLNS);
            /** Identificador de una condición, está formado por [ENT_MUN]-[ENT_PRO]-[ENT_TRA]-[ENT_COD] */
            String id = "CONDICION_ENTRADA" + ConstantesDatos.GUION + codMunicipio + ConstantesDatos.GUION + codProcedimiento +  ConstantesDatos.GUION + codTramite + ConstantesDatos.GUION + condicion.getCodCondEntrada();
            dataFieldElement.setAttribute("Id",id);            
            dataFieldElement.setAttribute("Name","Condicion_Entrada_Tramite");
            
            Element dataType = new Element("DataType",XMLNS);
            Element basicType = new Element("BasicType",XMLNS);
            basicType.setAttribute("Type","STRING");
            dataType.addContent(basicType);
            dataFieldElement.addContent(dataType);
            
            /**** CONDICIONES DE ENTRADA DEL TRÁMITE ****/
            Element extendAttributes = new Element("ExtendedAttributes",XMLNS);
            
            if(condicion!=null){
                String aux = "";
                if(condicion.getTipoCondEntrada()!=null && "TRÁMITE".equalsIgnoreCase(condicion.getTipoCondEntrada()))
                    aux = "T";
                else  if(condicion.getTipoCondEntrada()!=null && "EXPRESION".equalsIgnoreCase(condicion.getTipoCondEntrada()))
                    aux = "E";
                else  if(condicion.getTipoCondEntrada()!=null && "DOCUMENTO".equalsIgnoreCase(condicion.getTipoCondEntrada()))
                    aux = "D";
                
                Element tipo = new Element ("ExtendedAttribute",XMLNS);
                tipo.setAttribute("Name","tipo_condicion_entrada");
                tipo.setAttribute("Value",aux);
                extendAttributes.addContent(tipo);
                 
                Element codTramiteOrigen = new Element ("ExtendedAttribute",XMLNS);
                codTramiteOrigen.setAttribute("Name","codigo_tramite_origen");
                codTramiteOrigen.setAttribute("Value",codTramite);
                extendAttributes.addContent(codTramiteOrigen);
                
                if("T".equals(aux)){
                    String sIdTramiteCondEntrada ="";
                    if(condicion.getIdTramiteCondEntrada()!=null) sIdTramiteCondEntrada = condicion.getIdTramiteCondEntrada();
                    Element tramiteCondEntrada = new Element ("ExtendedAttribute",XMLNS);
                    tramiteCondEntrada.setAttribute("Name","id_tramite_cond_tramite");
                    tramiteCondEntrada.setAttribute("Value",sIdTramiteCondEntrada);
                    extendAttributes.addContent(tramiteCondEntrada);

                    String sEstadoTramite ="";
                    if(condicion.getEstadoTramiteCondEntrada()!=null) sEstadoTramite = condicion.getEstadoTramiteCondEntrada();
                    Element estadoCodTramiteCondEntrada = new Element ("ExtendedAttribute",XMLNS);
                    estadoCodTramiteCondEntrada.setAttribute("Name","estado");
                    estadoCodTramiteCondEntrada.setAttribute("Value",sEstadoTramite);
                    extendAttributes.addContent(estadoCodTramiteCondEntrada);
                } else if("D".equals(aux)){ //la condicion es de tipo documento

                    String codDocumento ="";
                    if(condicion.getCodigoDoc()!=null) codDocumento = condicion.getCodigoDoc();
                   
                    Element CodDocumentoTramiteCondEntrada = new Element ("ExtendedAttribute",XMLNS);
                    CodDocumentoTramiteCondEntrada.setAttribute("Name","codigo_documento");
                    CodDocumentoTramiteCondEntrada.setAttribute("Value",codDocumento);
                    extendAttributes.addContent(CodDocumentoTramiteCondEntrada);

                    String sEstadoTramite ="";
                    if(condicion.getEstadoTramiteCondEntrada()!=null) sEstadoTramite = condicion.getEstadoTramiteCondEntrada();

                    Element estadoCodTramiteCondEntrada = new Element ("ExtendedAttribute",XMLNS);
                    estadoCodTramiteCondEntrada.setAttribute("Name","estado");
                    estadoCodTramiteCondEntrada.setAttribute("Value",sEstadoTramite);
                    extendAttributes.addContent(estadoCodTramiteCondEntrada);
                } else{
                    // La condición es de tipo expresión
                    String sExpresion ="";
                    if(condicion.getExpresionCondEntrada()!=null) sExpresion = condicion.getExpresionCondEntrada();
                    Element expresion = new Element ("ExtendedAttribute",XMLNS);
                    expresion.setAttribute("Name","expresion_condicion");
                    expresion.setAttribute("Value",sExpresion);
                    extendAttributes.addContent(expresion);
                }
            }//if
            dataFieldElement.addContent(extendAttributes);
            resultado.add(dataFieldElement);
        }// for
        return resultado;
    }



  /**
     * Añade los campos suplementarios al documento XPDL
     * @param campos: Vector<DefinicionCampoValueObject> con los datos de los campos suplementarios del procedimiento o de un trámite
     * @param tipo: Puede tomar el valor CAMPO_SUPLEMENTARIO_TRAMITE o CAMPO_SUPLEMENTARIO_PROCEDIMIENTO e indica si los
     * campos sup. son de un trámite o de un procedimiento
     * @param codTramite: Código del trámite si los campos sup. son de un trámite o null si son de un procedimiento
     * @return una Collection de elementos xml DataField con las variables de las condiciones de entrada tipo
     * expresión, siempre que no hayan sido añadidas
     */
    public static Collection anhadirCamposSuplementarios (Vector<DefinicionCampoValueObject> campos,String tipo,String codTramite){
        Collection resultado = new Vector();

        for(int i=0;i<campos.size();i++){
            DefinicionCampoValueObject dfcVO = campos.get(i);
            String identificadorCampo = "";
            if(tipo.equals(CAMPO_SUPLEMENTARIO_TRAMITE)){
                // Campo suplementario de trámite
                identificadorCampo = "Campo" + ConstantesDatos.GUION + codTramite + ConstantesDatos.GUION + dfcVO.getCodCampo();
            }else // Campo suplementario de procedimiento
                identificadorCampo = "Campo" + ConstantesDatos.GUION + dfcVO.getCodCampo();

            Element dataFieldElement = new Element("DataField",XMLNS);
            dataFieldElement.setAttribute("Id",identificadorCampo);
            dataFieldElement.setAttribute("Name",tipo);
            
            Element dataType = new Element("DataType",XMLNS);
            Element basicType = new Element("BasicType",XMLNS);
            basicType.setAttribute("Type","STRING");
            dataType.addContent(basicType);
            dataFieldElement.addContent(dataType);
            
            Element extendAttributes = new Element("ExtendedAttributes",XMLNS);

            Element codigo = new Element("ExtendedAttribute",XMLNS);
            codigo.setAttribute("Name","codigo");
            codigo.setAttribute("Value",dfcVO.getCodCampo());
            // Se añade el código del campo
            extendAttributes.addContent(codigo);

            if(tipo.equals(CAMPO_SUPLEMENTARIO_TRAMITE) && codTramite!=null){
                // Si el campo suplementario es de un trámite, hay que indicar el código del mismo
                Element codigoTramite = new Element("ExtendedAttribute",XMLNS);
                codigoTramite.setAttribute("Name","codigoTramite");
                codigoTramite.setAttribute("Value",codTramite);
                // Se añade el código del campo
                extendAttributes.addContent(codigoTramite);
            }
            
            Element descripcion = new Element("ExtendedAttribute",XMLNS);
            descripcion.setAttribute("Name","descripcion");
            descripcion.setAttribute("Value",dfcVO.getDescCampo());
            // Se añade la descripción del campo suplementario
            extendAttributes.addContent(descripcion);

            Element codTipoDato = new Element("ExtendedAttribute",XMLNS);
            codTipoDato.setAttribute("Name","codigoTipoDato");
            codTipoDato.setAttribute("Value",dfcVO.getCodTipoDato());
            // Se añade el código del tipo de dato
            extendAttributes.addContent(codTipoDato);

            Element codigoPlantilla  = new Element("ExtendedAttribute",XMLNS);
            codigoPlantilla.setAttribute("Name","codigoPlantilla");
            if(dfcVO.getCodPlantilla()!=null){
                codigoPlantilla.setAttribute("Value",dfcVO.getCodPlantilla());
            }else{
                codigoPlantilla.setAttribute("Value","");
            }    
            // Se añade el  código de la plantilla
           extendAttributes.addContent(codigoPlantilla);

            Element tamano  = new Element("ExtendedAttribute",XMLNS);
            tamano.setAttribute("Name","tamano");
            if(dfcVO.getTamano()!=null && !"".equals(dfcVO.getTamano())){
                tamano.setAttribute("Value",dfcVO.getTamano());
            }else{
                tamano.setAttribute("Value","");
            }
            // Se añade el tamaño del campo suplementario
            extendAttributes.addContent(tamano);

            String mascara ="";
            if(dfcVO.getDescMascara()!=null && !"".equals(dfcVO.getDescMascara())){
                mascara = dfcVO.getDescMascara();
            }
            Element descMascara = new Element("ExtendedAttribute",XMLNS);
            descMascara.setAttribute("Name","mascara");
            descMascara.setAttribute("Value",mascara);
            // Se añade la descripción de la máscara
            extendAttributes.addContent(descMascara);

            Element obligatorio = new Element("ExtendedAttribute",XMLNS);
            obligatorio.setAttribute("Name","obligatorio");
            obligatorio.setAttribute("Value",dfcVO.getObligat());
            // Se indica si el campo es o no obligatorio
            extendAttributes.addContent(obligatorio);
            
            Element descPlantilla = new Element("ExtendedAttribute",XMLNS);
            descPlantilla.setAttribute("Name","descripcionPlantilla");
            descPlantilla.setAttribute("Value",dfcVO.getDescPlantilla());
            // Se indica la descripción de la plantilla
            extendAttributes.addContent(descPlantilla);

            Element descTipoDato = new Element("ExtendedAttribute",XMLNS);
            descTipoDato.setAttribute("Name","descripcionTipoDato");
            descTipoDato.setAttribute("Value",dfcVO.getDescTipoDato());
            // Se indica la descripción del tipo de dato
            extendAttributes.addContent(descTipoDato);

            Element rotulo = new Element("ExtendedAttribute",XMLNS);
            rotulo.setAttribute("Name","rotulo");
            rotulo.setAttribute("Value",dfcVO.getRotulo());
            // Se indica cual es el rótulo del campo
            extendAttributes.addContent(rotulo);
            
            Element activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","activo");
            activo.setAttribute("Value",dfcVO.getActivo());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);
            
            activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","oculto");
            activo.setAttribute("Value",dfcVO.getOculto());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);
            
            activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","bloqueado");
            activo.setAttribute("Value",dfcVO.getBloqueado());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);                              
            
            activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","plazoAviso");
            if (dfcVO.getPlazoFecha() == null)
                activo.setAttribute("Value","");
            else
                activo.setAttribute("Value",dfcVO.getPlazoFecha());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);           
            
            activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","periodoAviso");
            if (dfcVO.getCheckPlazoFecha() == null)
                activo.setAttribute("Value","");
            else
                activo.setAttribute("Value",dfcVO.getCheckPlazoFecha());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);           
            
            activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","validacion");
            if (dfcVO.getValidacion() == null)                
                activo.setAttribute("Value","");
            else
                activo.setAttribute("Value",dfcVO.getValidacion());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);           
            
            activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","operacion");
            if (dfcVO.getOperacion() == null)
                activo.setAttribute("Value","");
            else
                activo.setAttribute("Value",dfcVO.getOperacion());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);           
            
            activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","agrupacionCampo");
            if (dfcVO.getCodAgrupacion() == null)
                activo.setAttribute("Value","");
            else    
                activo.setAttribute("Value",dfcVO.getCodAgrupacion());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);         
            
            activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","posicionX");
            if (dfcVO.getPosX() == null || "null".equals(dfcVO.getPosX()))
                activo.setAttribute("Value","");
            else
                activo.setAttribute("Value",dfcVO.getPosX());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);           
            
            activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","posicionY");
            if (dfcVO.getPosY() == null || "null".equals(dfcVO.getPosY()))
                activo.setAttribute("Value","");
            else
                activo.setAttribute("Value",dfcVO.getPosY());
            // Se indica si el campo está activo
            extendAttributes.addContent(activo);           
                       
            if(tipo.equals(CAMPO_SUPLEMENTARIO_TRAMITE)){
                Element orden = new Element("ExtendedAttribute",XMLNS);
                orden.setAttribute("Name","orden");
                orden.setAttribute("Value",dfcVO.getOrden());
                // Se indica el orden
                extendAttributes.addContent(orden);

                Element visible = new Element("ExtendedAttribute",XMLNS);
                visible.setAttribute("Name","visible");
                visible.setAttribute("Value",dfcVO.getVisible());
                // Se indica si el campo está activo
                extendAttributes.addContent(visible);
            }
            
            dataFieldElement.addContent(extendAttributes);
            resultado.add(dataFieldElement);              
        }
        return resultado;
    }
    
      /**
     * Añade las agrupaciones de campos suplementarios al documento XPDL
     * @param agrupacionesCampos: Vector<DefinicionAgrupacionCamposValueObject> con los datos de las agrupaciones de los campos suplementarios del procedimiento o de un trámite
     * @param tipo: Puede tomar el valor CAMPO_SUPLEMENTARIO_TRAMITE o CAMPO_SUPLEMENTARIO_PROCEDIMIENTO e indica si las agrupaciones de campos sup. son de un 
     * trámite o de un procedimiento
     * @param codTramite: Código del trámite si las agrupaciones de los campos sup. son de un trámite o null si son de un procedimiento
     * @return una Collection de elementos xml DataField con las variables de las condiciones de entrada tipo
     * expresión, siempre que no hayan sido añadidas
     */
    public static Collection anhadirAgrupacionCamposSuplementarios (Vector<DefinicionAgrupacionCamposValueObject> agrupacionesCampos,String tipo,String codTramite){
        Collection resultado = new Vector();

        for(int i=0;i<agrupacionesCampos.size();i++){
            DefinicionAgrupacionCamposValueObject dfacVO = agrupacionesCampos.get(i);
            String identificadorCampo = "";
            if(tipo.equals(AGRUPACION_CAMPO_SUPLEMENTARIO_TRAMITE)){
                // Campo suplementario de trámite
                identificadorCampo = "Agrupacion" + ConstantesDatos.GUION + codTramite + ConstantesDatos.GUION + dfacVO.getCodAgrupacion();
            }else // Campo suplementario de procedimiento
                identificadorCampo = "Agrupacion" + ConstantesDatos.GUION + dfacVO.getCodAgrupacion();

            Element dataFieldElement = new Element("DataField",XMLNS);
            dataFieldElement.setAttribute("Id",identificadorCampo);
            dataFieldElement.setAttribute("Name",tipo);
            
            Element dataType = new Element("DataType",XMLNS);
            Element basicType = new Element("BasicType",XMLNS);
            basicType.setAttribute("Type","STRING");
            dataType.addContent(basicType);
            dataFieldElement.addContent(dataType);
            
            Element extendAttributes = new Element("ExtendedAttributes",XMLNS);

            Element codigo = new Element("ExtendedAttribute",XMLNS);
            codigo.setAttribute("Name","codigo");
            codigo.setAttribute("Value",dfacVO.getCodAgrupacion());
            // Se añade el código del campo
            extendAttributes.addContent(codigo);

            if(tipo.equals(AGRUPACION_CAMPO_SUPLEMENTARIO_TRAMITE) && codTramite!=null){
                // Si el campo suplementario es de un trámite, hay que indicar el código del mismo
                Element codigoTramite = new Element("ExtendedAttribute",XMLNS);
                codigoTramite.setAttribute("Name","codigoTramite");
                codigoTramite.setAttribute("Value",codTramite);
                // Se añade el código del campo
                extendAttributes.addContent(codigoTramite);
            }

            Element descripcion = new Element("ExtendedAttribute",XMLNS);
            descripcion.setAttribute("Name","descripcion");
            descripcion.setAttribute("Value",dfacVO.getDescAgrupacion());
            // Se añade la descripción del campo suplementario
            extendAttributes.addContent(descripcion);

            Element orden = new Element("ExtendedAttribute",XMLNS);
            orden.setAttribute("Name","orden");
            orden.setAttribute("Value",String.valueOf(dfacVO.getOrdenAgrupacion()));
            // Se añade la descripción del campo suplementario
            extendAttributes.addContent(orden);
            
            Element activo = new Element("ExtendedAttribute",XMLNS);
            activo.setAttribute("Name","activo");
            activo.setAttribute("Value",dfacVO.getAgrupacionActiva());
            // Se añade el código del tipo de dato
            extendAttributes.addContent(activo);
            
            dataFieldElement.addContent(extendAttributes);
            resultado.add(dataFieldElement);              
        }
        return resultado;
    }
    
	/**
	 * Anhade la informacion de la firma (de usuario o de flujo) a los datos del documento de tramite
	 * @param numero
	 * @return 
	 */	
	private static void anhadirDatosFirmasDocumento(Element extendAttributes, FirmaFlujoVO firmaFlujo){
		String firmaFlujoNombreValueObjeto = firmaFlujo.getNombre();
		if(!firmaFlujoNombreValueObjeto.equals("-1")){
			// id (FIRMA_FLUJO : ID)
			Element firmaFlujoId = new Element("ExtendedAttribute", XMLNS);
			firmaFlujoId.setAttribute("Name", "firmaFlujoId");
			firmaFlujoId.setAttribute("Value", parsearInteger(firmaFlujo.getId()));
			extendAttributes.addContent(firmaFlujoId);
		} else {
			// nombre (FIRMA_FLUJO : NOMBRE)
			Element firmaFlujoNombre = new Element("ExtendedAttribute", XMLNS);
			firmaFlujoNombre.setAttribute("Name", "firmaFlujoNombre");
			firmaFlujoNombre.setAttribute("Value", "FLUJO NO VÁLIDO");
			extendAttributes.addContent(firmaFlujoNombre);
		}
	}
    
	/**
	 * Anhadir al documento XPDL los datos de un flujo de firmas como DataField
	 * @param numero
	 * @return 
	 */	
	private static Collection anhadirDatosFlujo(FirmaFlujoVO firmaFlujo, String nombreDataField){
		Collection resultado = new Vector();
		
		Element dataFieldElement = new Element("DataField",XMLNS);
		String id = "Flujo" + ConstantesDatos.GUION + firmaFlujo.getId();
		log.debug(" ConversorXPDL.anhadirDatosFlujo() id: " + id);
		dataFieldElement.setAttribute("Id",id);
		dataFieldElement.setAttribute("Name",nombreDataField);

		Element dataType = new Element("DataType",XMLNS);
		Element basicType = new Element("BasicType",XMLNS);
		basicType.setAttribute("Type","STRING");
		dataType.addContent(basicType);
		dataFieldElement.addContent(dataType);
		
		Element extendAttributes = new Element("ExtendedAttributes",XMLNS);
			
		// id (FIRMA_FLUJO : ID)
		Element firmaFlujoId = new Element("ExtendedAttribute", XMLNS);
		firmaFlujoId.setAttribute("Name", "firmaFlujoId");
		firmaFlujoId.setAttribute("Value", parsearInteger(firmaFlujo.getId()));
		extendAttributes.addContent(firmaFlujoId);

		// nombre (FIRMA_FLUJO : NOMBRE)
		String firmaFlujoNombreValueMostrar;
		String firmaFlujoNombreValueObjeto = firmaFlujo.getNombre();
		Element firmaFlujoNombre = new Element("ExtendedAttribute", XMLNS);
		firmaFlujoNombre.setAttribute("Name", "firmaFlujoNombre");
		if(firmaFlujoNombreValueObjeto.equals("-1")){
			firmaFlujoNombreValueMostrar = "FLUJO NO VÁLIDO";
		} else {
			firmaFlujoNombreValueMostrar = parsearString(firmaFlujoNombreValueObjeto);
		}
		firmaFlujoNombre.setAttribute("Value", firmaFlujoNombreValueMostrar);
		extendAttributes.addContent(firmaFlujoNombre);

		if(!firmaFlujoNombreValueObjeto.equals("-1")){
			// idTipo (FIRMA_FLUJO : ID_TIPO)
			Element firmaFlujoIdTipo = new Element("ExtendedAttribute", XMLNS);
			firmaFlujoIdTipo.setAttribute("Name", "firmaFlujoIdTipo");
			firmaFlujoIdTipo.setAttribute("Value", parsearInteger(firmaFlujo.getIdTipoFirma()));
			extendAttributes.addContent(firmaFlujoIdTipo);

			// activo (FIRMA_FLUJO : ACTIVO)
			Element firmaFlujoActivo = new Element("ExtendedAttribute", XMLNS);
			firmaFlujoActivo.setAttribute("Name", "firmaFlujoActivo");
			firmaFlujoActivo.setAttribute("Value", (firmaFlujo.isActivo()) ? "1" : "0");
			extendAttributes.addContent(firmaFlujoActivo);
		}
		
		dataFieldElement.addContent(extendAttributes);
		resultado.add(dataFieldElement);       
		
		return resultado;
	}
    
	/**
	 * Anhadir al documento XPDL los datos de los circuitos de firmas de los flujos de firmas como DataField
	 * @param numero
	 * @return 
	 */	
	private static Collection anhadirDatosCircuitos(List<FirmaCircuitoVO> circuitos, String nombreDataField, Integer flujoId){
		Collection resultado = new Vector();
		
		for(FirmaCircuitoVO circuito : circuitos){
			if(flujoId.equals(circuito.getIdFlujoFirma())){
				Element dataFieldElement = new Element("DataField",XMLNS);
				String id = "Circuito" + ConstantesDatos.GUION + flujoId + ConstantesDatos.GUION + circuito.getOrden();
				log.debug(" ConversorXPDL.anhadirDatosFlujo() id: " + id);
				dataFieldElement.setAttribute("Id",id);
				dataFieldElement.setAttribute("Name",nombreDataField);

				Element dataType = new Element("DataType",XMLNS);
				Element basicType = new Element("BasicType",XMLNS);
				basicType.setAttribute("Type","STRING");
				dataType.addContent(basicType);
				dataFieldElement.addContent(dataType);

				Element extendAttributes = new Element("ExtendedAttributes",XMLNS);

				// id (FIRMA_FLUJO : ID)
				Element firmaFlujoId = new Element("ExtendedAttribute", XMLNS);
				firmaFlujoId.setAttribute("Name", "firmaFlujoId");
				firmaFlujoId.setAttribute("Value", parsearInteger(flujoId));
				extendAttributes.addContent(firmaFlujoId);
				
				// idUsuario (FIRMA_CIRCUITO : ID_USUARIO)
				Element idUsuario = new Element("ExtendedAttribute", XMLNS);
				idUsuario.setAttribute("Name", "firmaCircuitoIdUsuario");
				idUsuario.setAttribute("Value", parsearInteger(circuito.getIdUsuario()));
				extendAttributes.addContent(idUsuario);

				// log (FIRMA_CIRCUITO : ID_USUARIO)
				Element firmaLogUsuario = new Element("ExtendedAttribute", XMLNS);
				firmaLogUsuario.setAttribute("Name", "firmaCircuitoLogUsuario");
				firmaLogUsuario.setAttribute("Value", parsearString(circuito.getLogUsuario()));
				extendAttributes.addContent(firmaLogUsuario);
                                
                                // log (FIRMA_CIRCUITO : NIF_USUARIO)
				Element firmaNifUsuario = new Element("ExtendedAttribute", XMLNS);
				firmaNifUsuario.setAttribute("Name", "firmaCircuitoDniUsuario");
				firmaNifUsuario.setAttribute("Value", parsearString(circuito.getDocumentoUsuario()));
				extendAttributes.addContent(firmaNifUsuario);

				// orden (FIRMA_CIRCUITO : ORDEN)
				Element firmaCircuitoOrden = new Element("ExtendedAttribute", XMLNS);
				firmaCircuitoOrden.setAttribute("Name", "firmaCircuitoOrden");
				firmaCircuitoOrden.setAttribute("Value", parsearInteger(circuito.getOrden()));
				extendAttributes.addContent(firmaCircuitoOrden);

				dataFieldElement.addContent(extendAttributes);
				resultado.add(dataFieldElement);          
			}
		}
        
		return resultado;
	}
			
    private static String parsearEntero(String numero) {
        if (numero!=null && !"".equals(numero)) {
            return Integer.parseInt(numero)+"";
        } else return numero;
    }
}