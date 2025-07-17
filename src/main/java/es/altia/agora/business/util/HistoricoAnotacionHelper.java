package es.altia.agora.business.util;

import es.altia.agora.business.registro.DescripcionRegistroValueObject;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.ReservaOrdenValueObject;
import es.altia.agora.business.registro.SimpleRegistroValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

/**
 * M�todos para gestionar la informaci�n de los movimientos anotados en el
 * hist�rico de movimientos para una anotaci�n.
 * @author juan.jato
 */
public class HistoricoAnotacionHelper {
    
    private static Logger log = Logger.getLogger(HistoricoAnotacionHelper.class);
    
    protected void HistoricoAnotacionHelper() {
    }

    /**
     * Devuelve la clave de la anotaci�n en el formato usado en el hist�rico.
     * @param regVO un RegistroValueObject con los datos de la anotaci�n.
     * @return
     */
    public static String crearClaveHistorico(RegistroValueObject regVO) {
        long reg = regVO.getNumReg();
        return regVO.getIdentDepart() + "/" +
                regVO.getUnidadOrgan() + "/" +
                regVO.getTipoReg() + "/" +
                regVO.getAnoReg() + "/" +
                reg;
    }

    /**
     * Devuelve la clave de la anotaci�n en el formato usado en el hist�rico.
     * @param tVO un TramitacionValueObject con los datos de la anotaci�n.
     * @return
     */
    public static String crearClaveHistorico(TramitacionValueObject tVO) {
        return tVO.getCodDepartamento() + "/" +
                tVO.getCodUnidadRegistro() + "/" +
                tVO.getTipoRegistro() + "/" +
                tVO.getEjercicioRegistro() + "/" +
                tVO.getNumero();
    }
    
    /**
     * Devuelve la clave de la anotaci�n en el formato usado en el hist�rico.
     * @param reserva un ReservaOrdenValueObject con los datos de la anotaci�n.
     * @return
     */
    public static String crearClaveHistorico(ReservaOrdenValueObject reserva) {
        return reserva.getCodDepto() + "/" +
                reserva.getCodUnidad() + "/" +
                reserva.getTipoReg() + "/" +
                reserva.getEjercicio() + "/" +
                reserva.getTxtNumRegistrado();
    }
    
    /**
     * Crea el XML correspondiente al movimiento alta de anotaci�n.
     * @param vo DescripcionRegistroValueObject con los datos del alta.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLAlta(DescripcionRegistroValueObject vo) {
        
        String xml = "<DescripcionMovimiento><AltaAnotacion>" +
            crearElemento("tipo", vo.getTipoAsiento()) +
            crearElemento("fechaPres", vo.getFechaEntrada()) +
            // Hora de presentacion no existe en registro de salida.
            crearNoNulo("horaPres", vo.getHoraEntrada()) +
            crearElemento("fechaGravacion", vo.getFechaPres()) +
            crearElemento("horaGravacion", vo.getHoraPres()) +
            crearNoNulo("fechaDocu", vo.getFechaDocu()) +
            crearNoNulo("asunto", vo.getAsunto()) +
            crearElemento("extracto", vo.getExtracto()) +
            crearNoNulo("observaciones", vo.getObservaciones()) +
            crearNoNulo("unidad", vo.getUnidad()) +
            crearElemento("tipoEntrada", vo.getTipoEntrada()) +
            crearNoNulo("tipoDocumento", vo.getTipoDoc()) +
            crearNoNulo("tipoTransporte", vo.getTipoTrans()) +
            crearNoNulo("numTransporte", vo.getNumTrans()) +
            crearNoNulo("tipoRemitente", vo.getTipoRem()) +
            crearNoNulo("procedimiento", vo.getProcedimiento()) +
            crearNoNulo("expediente", vo.getExpediente()) +
            crearNoNulo("actuacion", vo.getActuacion()) +
            crearNoNulo("autoridad", vo.getAutoridad()) +
            crearNoNulo("orgDestino", vo.getOrgDestino()) +
            crearNoNulo("unidadDestino", vo.getUniDestino()) +
            crearNoNulo("orgProcedencia", vo.getOrgProc()) +
            crearNoNulo("unidadProcedencia", vo.getUniProc()) +
            crearNoNulo("entradaRelacionada", vo.getEntradaRel());
        
        // Interesados
        for (String descTercero : vo.getDescTerceros()) {
            xml += crearElemento("interesado", descTercero);
        }
        
        // Temas
        if(vo.getTemas()!=null){
			for (String descTema : vo.getTemas()) {
				xml += crearElemento("tema", descTema);
			}
        }
        
         if(vo.getRelaciones()!=null){
			// Relaciones
			for (SimpleRegistroValueObject relacion : vo.getRelaciones()) {
				xml += "<relacion>" +
					crearElemento("tipo", relacion.getTipo()) +
					crearElemento("ejeNum", relacion.getEjercicio() + 
								  "/" + relacion.getNumero()) +
					"</relacion>";
			}
        }
        
         if(vo.getNombreDocs()!=null){
			// Documentos
			Vector<String> nombreDocs = vo.getNombreDocs();
			Vector<String> tipoDocs = vo.getTipoDocs();
			Vector<String> fechaDocs = vo.getFechaDocs();
            Vector<String> cotejoDocs = vo.getCotejoDocs();
			for (int i=0; i<nombreDocs.size(); i++) {
				xml += "<documento>" +
					crearElemento("nombre", nombreDocs.elementAt(i)) +
					crearNoNulo("tipo", tipoDocs.elementAt(i)) +
					crearNoNulo("fecha", fechaDocs.elementAt(i)) +
						crearNoNulo("cotejo", cotejoDocs.elementAt(i)) +
					"</documento>";
			}
         }
        
        //Oficina de registro y descripci�n  
        if((vo.getDescOficinaRegistro()!=null)&&(!"".equals(vo.getDescOficinaRegistro()))){
			xml +=
				crearNoNulo("codOficinaRegistro", String.valueOf(vo.getCodOficinaRegistro())) +
				crearNoNulo("descOficinaRegistro", vo.getDescOficinaRegistro());
        }
        xml += "</AltaAnotacion></DescripcionMovimiento>";
        return xml;
    }
    
    /**
     * Crea el XML correspondiente al movimiento modificaci�n de anotaci�n.
     * @param antiguo DescripcionRegistroValueObject con los datos anteriores.
     * @param nuevo DescripcionRegistroValueObject con los datos nuevos.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLModificacion(DescripcionRegistroValueObject antiguo,
            DescripcionRegistroValueObject nuevo,boolean asuntoAntiguoDadoBaja) {
        
        String xml = "<DescripcionMovimiento><ModificarAnotacion>";
        String cambios = 
            crearCambio("cambioFechaPres", antiguo.getFechaEntrada(), nuevo.getFechaEntrada()) + 
            crearCambio("cambioHoraPres", antiguo.getHoraEntrada(), nuevo.getHoraEntrada()) + 
            crearCambio("cambiofechaDocu", antiguo.getFechaDocu(), nuevo.getFechaDocu()) + 
            //crearCambio("cambioAsunto", antiguo.getAsunto(), nuevo.getAsunto()) +
            crearAsunto("cambioAsunto", antiguo.getAsunto(), nuevo.getAsunto(),asuntoAntiguoDadoBaja) + 
            crearCambio("cambioExtracto", antiguo.getExtracto(), nuevo.getExtracto()) + 
            crearCambio("cambioObservaciones", antiguo.getObservaciones(), nuevo.getObservaciones()) + 
            crearCambio("cambioUnidad", antiguo.getUnidad(), nuevo.getUnidad()) + 
            crearCambio("cambioTipoEntrada", antiguo.getTipoEntrada(), nuevo.getTipoEntrada()) + 
            crearCambio("cambioTipoDocumento", antiguo.getTipoDoc(), nuevo.getTipoDoc()) + 
            crearCambio("cambioTipoTransporte", antiguo.getTipoTrans(), nuevo.getTipoTrans()) + 
            crearCambio("cambioNumTransporte", antiguo.getNumTrans(), nuevo.getNumTrans()) + 
            crearCambio("cambioTipoRemitente", antiguo.getTipoRem(), nuevo.getTipoRem()) + 
            crearCambio("cambioProcedimiento", antiguo.getProcedimiento(), nuevo.getProcedimiento()) + 
            crearCambio("cambioExpediente", antiguo.getExpediente(), nuevo.getExpediente()) + 
            crearCambio("cambioActuacion", antiguo.getActuacion(), nuevo.getActuacion()) + 
            crearCambio("cambioAutoridad", antiguo.getAutoridad(), nuevo.getAutoridad()) + 
            crearCambio("cambioOrgDestino", antiguo.getOrgDestino(), nuevo.getOrgDestino()) + 
            crearCambio("cambioUnidadDestino", antiguo.getUniDestino(), nuevo.getUniDestino()) + 
            crearCambio("cambioOrgProcedencia", antiguo.getOrgProc(), nuevo.getOrgProc()) + 
            crearCambio("cambioUnidadProcedencia", antiguo.getUniProc(), nuevo.getUniProc()) + 
            crearCambio("cambioEntradaRelacionada", antiguo.getEntradaRel(), nuevo.getEntradaRel()) +
            crearCambio("cambioRegistroTelematico", antiguo.getRegistroTelematico(), nuevo.getRegistroTelematico());

        // INTERESADOS
        Vector<String> codTerAntiguos = antiguo.getCodTerceros();
        Vector<String> codTerNuevos = nuevo.getCodTerceros();
        Vector<String> descTerAntiguos = antiguo.getDescTerceros();
        Vector<String> descTerNuevos = nuevo.getDescTerceros();

        // Interesados nuevos
        for (String cod : codTerNuevos) {
            if (!codTerAntiguos.contains(cod)) {
                cambios += crearElemento("nuevoInteresado", 
                        descTerNuevos.elementAt(codTerNuevos.indexOf(cod)));
            }
        }
        
        // Interesados dados de baja
        for (String cod : codTerAntiguos) {
            if (!codTerNuevos.contains(cod)) {
                cambios += crearElemento("borrarInteresado", 
                        descTerAntiguos.elementAt(codTerAntiguos.indexOf(cod)));
            }
        }
  
        // Interesados modificados
        int indiceAnterior = 0;
        int indiceActual = 0;
        for (String cod : codTerAntiguos) {
            if (codTerNuevos.contains(cod)) {
                indiceAnterior = codTerAntiguos.indexOf(cod);
                indiceActual = codTerNuevos.indexOf(cod);
                if (tieneCambiosTercero(antiguo,nuevo,indiceAnterior,indiceActual)) {
                    cambios += crearCambio("cambioInteresado",
                            descTerAntiguos.elementAt(indiceAnterior),
                            descTerNuevos.elementAt(indiceActual));
                }
            }
        }
        
        // DOCUMENTOS
        Vector<String> docsAntiguos = antiguo.getNombreDocs();
        Vector<String> docsNuevos = nuevo.getNombreDocs();
        
        // Documentos nuevos
        for (String doc : docsNuevos) {
            if (!docsAntiguos.contains(doc)) {
                int i = docsNuevos.indexOf(doc);
                cambios += "<nuevoDocumento>" +
                     crearElemento("nombre", doc) +
                     crearNoNulo("tipo", nuevo.getTipoDocs().elementAt(i)) +
                     crearNoNulo("fecha", nuevo.getFechaDocs().elementAt(i)) +
                     crearNoNulo("cotejo", nuevo.getCotejoDocs().elementAt(i)) +
                     "</nuevoDocumento>";
            }
        }
        
        // Documentos dados de baja
        for (String doc : docsAntiguos) {
            if (!docsNuevos.contains(doc)) {
                int i = docsAntiguos.indexOf(doc);
                cambios += "<borrarDocumento>" +
                     crearElemento("nombre", doc) +
                     crearNoNulo("tipo", antiguo.getTipoDocs().elementAt(i)) +
                     crearNoNulo("fecha", antiguo.getFechaDocs().elementAt(i)) +
                     crearNoNulo("cotejo", antiguo.getCotejoDocs().elementAt(i)) +
                     "</borrarDocumento>";
            }
        }        
        
  
        // Documentos modificados
        for (String doc : docsAntiguos) {
            if (docsNuevos.contains(doc)) {
                indiceAnterior = docsAntiguos.indexOf(doc);
                indiceActual = docsNuevos.indexOf(doc);
                if (tieneCambiosDoc(antiguo,nuevo,indiceAnterior,indiceActual)) {
                  cambios += "<cambioDocumento><docAnterior>" +
                    crearElemento("nombre", doc) +
                    crearNoNulo("tipo", antiguo.getTipoDocs().elementAt(indiceAnterior)) +
                    crearNoNulo("fecha", antiguo.getFechaDocs().elementAt(indiceAnterior)) +
                    crearNoNulo("cotejo", antiguo.getCotejoDocs().elementAt(indiceAnterior)) +
                    "</docAnterior><docActual>" +
                    crearElemento("nombre", doc) +
                    crearNoNulo("tipo", nuevo.getTipoDocs().elementAt(indiceActual)) +
                    crearNoNulo("fecha", nuevo.getFechaDocs().elementAt(indiceActual)) +
                    crearNoNulo("cotejo", nuevo.getCotejoDocs().elementAt(indiceActual)) +
                    "</docActual></cambioDocumento>";
                }
            }
        }
        
        // RELACIONES
        Vector<SimpleRegistroValueObject> relacionesAntiguas = antiguo.getRelaciones();
        Vector<SimpleRegistroValueObject> relacionesNuevas = nuevo.getRelaciones();

        // Relaciones nuevas
        for (SimpleRegistroValueObject relacion : relacionesNuevas) {
            if (!contieneRelacion(relacionesAntiguas,relacion)) {
                cambios += "<nuevaRelacion>" +
                           crearElemento("tipo", relacion.getTipo()) +
                           crearElemento("ejeNum", relacion.getEjercicio() + 
                                         "/" + relacion.getNumero()) +
                           "</nuevaRelacion>";
            }
        }

        // Relaciones borradas
        for (SimpleRegistroValueObject relacion : relacionesAntiguas) {
            if (!contieneRelacion(relacionesNuevas,relacion)) {
                cambios += "<borrarRelacion>" +
                           crearElemento("tipo", relacion.getTipo()) +
                           crearElemento("ejeNum", relacion.getEjercicio() + 
                                         "/" + relacion.getNumero()) +
                           "</borrarRelacion>";
            }
        }
        
        // TEMAS
        Vector<String> temasAntiguos = antiguo.getTemas();
        Vector<String> temasNuevos = nuevo.getTemas();
        
        // Temas nuevos
        for (String tema : temasNuevos) {
            if (!temasAntiguos.contains(tema)) {
                cambios += crearElemento("nuevoTema", tema);
            }
        }
        
        // Temas borrados
        for (String tema : temasAntiguos) {
            if (!temasNuevos.contains(tema)) {
                cambios += crearElemento("borrarTema", tema);
            }
        }
                
        // Si hay cambios a�adimos el tipo del asiento
        if (cambios.length() > 0) {
            cambios = crearElemento("tipo", nuevo.getTipoAsiento()) + cambios;
        }
        xml += cambios + "</ModificarAnotacion></DescripcionMovimiento>";
        return xml;
    }
    
    /**
     * Comprueba si el vector contiene la relacion, comparando los campos tipo,
     * numero y ejercicio, pues departamento y uor se suponen iguales al solo
     * poderse relacionar una anotaci�n con otra del mismo departamento y uor.
     * @param relaciones vector de relaciones
     * @param relacion la relacion que se busca
     * @return true si el vector contiene la relacion, false en otro caso
     */
    private static boolean contieneRelacion(Vector<SimpleRegistroValueObject> relaciones,
            SimpleRegistroValueObject relacion) {
        
        for (SimpleRegistroValueObject rel : relaciones) {
            if (rel.getTipo().equals(relacion.getTipo()) &&
                rel.getEjercicio().equals(relacion.getEjercicio()) &&
                rel.getNumero().equals(relacion.getNumero()))
            return true;
        }
        
        return false;
    }

    /**
     * Crear el XML correspondiente a un cambio con el nombre de elemento
     * indicado, si los strings que se pasan son diferentes. En otro caso
     * devuelve el string vac�o.
     * @param nombreElemento nombre del elemento a crear
     * @param antiguo valor anterior del elemento
     * @param nuevo valor nuevo del elemento
     * @return xml correspondiente a un cambio o string vac�o
     */
    private static String crearCambio(String nombreElemento, String antiguo, 
            String nuevo)
    {
        if (!antiguo.equals(nuevo)) {
            return "<" + nombreElemento + ">" +
                    crearElemento("anterior", antiguo) +
                    crearElemento("actual", nuevo) +
                    "</" + nombreElemento + ">";
        } else {
            return "";
        }
    }


    /**
     * Metodo que trata el cambio de asunto codificado
     * @param nombreElemento: Nombre del elemento en el xml
     * @param antiguo: C�digo de asunto antiguo
     * @param nuevo: Nuevo c�digo de asunto
     * @param asuntoAntiguoDadoBaja: Booleano que indica si el asunto antiguo est� dado de baja
     * @return String
     */
    private static String crearAsunto(String nombreElemento, String antiguo,
            String nuevo,boolean asuntoAntiguoDadoBaja){
        String salida = "";
        if(asuntoAntiguoDadoBaja){
            if(!"".equals(antiguo) && "".equals(nuevo)){
                // El asunto antiguo est� dado de baja y no se asigna uno nuevo, no se hace nada ya que el asunto antiguo (dado de baja
                // seguir� estando asociado a la anotaci�n
                salida =  "";
            }else
            if(!"".equals(antiguo) && !"".equals(nuevo)){
                salida =  "<" + nombreElemento + ">" +
                    crearElemento("anterior", "") +
                    crearElemento("actual", nuevo) +
                    "</" + nombreElemento + ">";
            }

        }else{
			if(!nuevo.equals(antiguo)){
				salida = "<" + nombreElemento + ">" +
						crearElemento("anterior", antiguo) +
						crearElemento("actual", nuevo) +
						"</" + nombreElemento + ">";
			}
        }
        
        return salida;
    }

    /**
     * Comprueba si existe alg�n cambio en un tercero respecto a su c�digo de 
     * versi�n, c�digo de domicilio o descripci�n de su rol.
     * @param antiguo DescripcionRegistroValueObject anterior a modificacion
     * @param nuevo DescripcionRegistroValueObject posterior a modificacion
     * @param indiceAnterior indice del tercero en el VO antiguo
     * @param indiceActual indice del tercero en el VO nuevo
     * @return true si ha cambiado alg�n dato, false en otro caso
     */
    private static boolean tieneCambiosTercero(
            DescripcionRegistroValueObject antiguo, 
            DescripcionRegistroValueObject nuevo, 
            int indiceAnterior, int indiceActual) {
        return !antiguo.getVersionTerceros().elementAt(indiceAnterior).equals(
                    nuevo.getVersionTerceros().elementAt(indiceActual)) ||
               !antiguo.getCodDomicilios().elementAt(indiceAnterior).equals(
                    nuevo.getCodDomicilios().elementAt(indiceActual)) ||
               !antiguo.getRolesTerceros().elementAt(indiceAnterior).equals(
                    nuevo.getRolesTerceros().elementAt(indiceActual));
    }
    
    /**
     * Comprueba si existe alg�n cambio en un documento respecto a su tipo o 
     * fecha de aportaci�n.
     * @param antiguo DescripcionRegistroValueObject anterior a modificacion
     * @param nuevo DescripcionRegistroValueObject posterior a modificacion
     * @param indiceAnterior indice del documento en el VO antiguo
     * @param indiceActual indice del documento en el VO nuevo
     * @return true si ha cambiado alg�n dato, false en otro caso
     */
    private static boolean tieneCambiosDoc(
            DescripcionRegistroValueObject antiguo, 
            DescripcionRegistroValueObject nuevo, 
            int indiceAnterior, int indiceActual) {            
            log.debug("indiceAnterior: " + indiceAnterior + ", indiceActual: " + indiceActual);
            log.debug("indiceAnterior: " + indiceAnterior + ", indiceActual: " + indiceActual);
            
            log.debug("antiguo.getTipoDocs: " + antiguo.getTipoDocs().elementAt(indiceAnterior));
            log.debug("nuevo.getTipoDocs: " + nuevo.getTipoDocs().elementAt(indiceActual));
            
            log.debug("antiguo.getFechaDocs().elementAt(indiceAnterior): " + antiguo.getFechaDocs().elementAt(indiceAnterior));
            log.debug("nuevo.getFechaDocs().elementAt(indiceAnterior): " + nuevo.getFechaDocs().elementAt(indiceActual));
            
            log.debug("antiguo.getCotejoDocs: " + antiguo.getCotejoDocs().elementAt(indiceAnterior));
            log.debug("nuevo.getCotejoDocs: " + nuevo.getCotejoDocs().elementAt(indiceActual));
            
            if(antiguo.getTipoDocs().elementAt(indiceAnterior)!=null && nuevo.getTipoDocs().elementAt(indiceActual)!=null
                    && antiguo.getFechaDocs().elementAt(indiceAnterior)!=null && nuevo.getFechaDocs().elementAt(indiceActual)!=null
                    && antiguo.getCotejoDocs().elementAt(indiceAnterior)!=null && nuevo.getCotejoDocs().elementAt(indiceActual)!=null){
                
                return !antiguo.getTipoDocs().elementAt(indiceAnterior).equals(nuevo.getTipoDocs().elementAt(indiceActual))
                        || !antiguo.getFechaDocs().elementAt(indiceAnterior).equals(nuevo.getFechaDocs().elementAt(indiceActual))
                        || !antiguo.getCotejoDocs().elementAt(indiceAnterior).equals(nuevo.getCotejoDocs().elementAt(indiceActual));                
            }else
                return false;              
            //return !antiguo.getTipoDocs().elementAt(indiceAnterior).equals(nuevo.getTipoDocs().elementAt(indiceActual)) || !antiguo.getFechaDocs().elementAt(indiceAnterior).equals(nuevo.getFechaDocs().elementAt(indiceActual));
    }
    
    /**
     * Crea el XML correspondiente al movimiento reservar anotaci�n.
     * @param reserva vo con los datos de la reserva.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLReserva(ReservaOrdenValueObject reserva) {
        String xml = "<DescripcionMovimiento><ReservarAnotacion>" +
                     crearElemento("ejercicio", reserva.getEjercicio()) +
                     crearElemento("numero", 
                         Integer.toString(reserva.getTxtNumRegistrado()));
        
                     SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
                     SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
                     String fechaTxt = fecha.format(reserva.getFecha());
                     String horaTxt = hora.format(reserva.getFecha());
                     
             xml +=  crearElemento("fechaEntrada", fechaTxt) +
                     crearElemento("horaEntrada", horaTxt) +
                     "</ReservarAnotacion></DescripcionMovimiento>";
        return xml;
    }
    
    /**
     * Crea el XML correspondiente al movimiento anular anotaci�n.
     * @param diligencia string con la diligencia de anulaci�n.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLAnular(String diligencia) {
        String xml = "<DescripcionMovimiento><AnularAnotacion>" +
                     crearElemento("diligenciaAnulacion", diligencia) +
                     "</AnularAnotacion></DescripcionMovimiento>";
        return xml;
    }
    
    /**
     * Crea el XML correspondiente al movimiento recuperar anulada.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLDesanular() {
        String xml = "<DescripcionMovimiento><DesanularAnotacion/></DescripcionMovimiento>";
        return xml;
    }
    
    /**
     * Crea el XML correspondiente al movimiento aceptar anotaci�n.
     * @param textoAceptacion string con las observaciones de aceptar.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLAceptar(String textoAceptacion) {
        String xml = "<DescripcionMovimiento><AceptarAnotacion>" +
                     crearElemento("textoAceptacion", textoAceptacion) +
                     "</AceptarAnotacion></DescripcionMovimiento>";
        return xml;
    }
    
    public static String crearXmlAceptarEnDestino(String textoAceptacion) {
        String xml = "<DescripcionMovimiento><AceptarAnotacionEnDestino>" +
                     crearElemento("textoAceptacion", textoAceptacion) +
                     "</AceptarAnotacionEnDestino></DescripcionMovimiento>";
        return xml;
    }//crearXmlAceptarEnDestino
    
    /**
     * Crea el XML correspondiente al movimiento rechazar anotaci�n.
     * @param textoRechazo string con las observaciones de rechazar.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLRechazar(String textoRechazo) {
        String xml = "<DescripcionMovimiento><RechazarAnotacion>" +
                     crearElemento("textoRechazo", textoRechazo) +
                     "</RechazarAnotacion></DescripcionMovimiento>";
        return xml;
    }
    
    /**
     * Crea el XML correspondiente al movimiento recuperar de hist�rico.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLRecuperarHistorico() {
        String xml = "<DescripcionMovimiento><RecuperarHistorico/></DescripcionMovimiento>";
        return xml;
    }
    
    /**
     * Crea el XML correspondiente al movimiento iniciar expediente.
     * @param numExp string con el numero del expediente.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLIniciar(String numExp) {
        String xml = "<DescripcionMovimiento><InicioExpediente>" +
                     crearElemento("expedienteIniciado", numExp) +
                     "</InicioExpediente></DescripcionMovimiento>";
        return xml;
    }
    
    /**
     * Crea el XML correspondiente al movimiento adjuntar expediente.
     * @param numExp string con el numero del expediente
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLAdjuntar(String numExp) {
        String xml = "<DescripcionMovimiento><AdjuntarExpediente>" +
                     crearElemento("expedienteAdjunto", numExp) +
                     "</AdjuntarExpediente></DescripcionMovimiento>";
        return xml;
    }
    
    /**
     * Crea el XML correspondiente al movimiento anular reserva.
     * @param diligencia string con la diligencia de anulaci�n.
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLAnularReserva(String diligencia) {
        String xml = "<DescripcionMovimiento><AnularReserva>" +
                     crearElemento("diligenciaAnulacion", diligencia) +
                     "</AnularReserva></DescripcionMovimiento>";
        return xml;
    }
        
    
    /**
     * Crea el XML correspondiente al movimiento para eliminar el rechazo de una anotaci�n.     
     * @return String en formato XML que describe el movimiento.
     */
    public static String crearXMLEliminarRechazo() {
        String xml = "<DescripcionMovimiento><EliminarRechazoAnotacion>" +        
                     "</EliminarRechazoAnotacion></DescripcionMovimiento>";
        return xml;
    }

    /**
     * Crea el XML correspondiente al movimiento de envio de correos.
     * 
     * Formato:
     * <DescripcionMovimiento>
     *     <EnvioCorreo>
     *         <razon>ENVIO DE DOCUMENTO COTEJADO</razon>
     *         <para>correo1@email.com</para>
     *         <para>correo2@email.com</para>
     *         <cc>correo1@email.com</cc>
     *         <cc>correo2@email.com</cc>
     *         <cco>correo1@email.com</cco>
     *         <cco>correo2@email.com</cco>
     *         <asunto>Asunto del correo</asunto>
     *         <contenido>Contenido del correo</contenido>
     *         <ficherosAdjuntos>fichero1.pdf</ficherosAdjuntos>
     *         <ficherosAdjuntos>fichero2.txt</ficherosAdjuntos>
     *     </EnvioCorreo>
     * </DescripcionMovimiento>
     * 
     * @param vo HistoricoMailVO con los datos del correo enviado.
     * @return String en formato XML que describe el movimiento.
     * 
     */
    public static String crearXMLEnvioCorreo(HistoricoMailVO vo) {
        StringBuilder xml = new StringBuilder();
        
        xml.append("<DescripcionMovimiento><EnvioCorreo>");
        
        // Razon
        xml.append(crearElemento("razon", String.valueOf(vo.getRazon())));

        // Para
        if (vo.getPara() != null) {
            for (String elemento : vo.getPara()) {
               xml.append(crearNoNulo("para", elemento)); 
            }
        }

        // CC
        if (vo.getCc() != null) {
            for (String elemento : vo.getCc()) {
               xml.append(crearNoNulo("cc", elemento)); 
            }
        }
        
        // CCO
        if (vo.getCco() != null) {
            for (String elemento : vo.getCco()) {
               xml.append(crearNoNulo("cco", elemento)); 
            }
        }
        
        // Asunto y contenido
        xml.append(crearElemento("asunto", vo.getAsunto()));
        xml.append(crearElemento("contenido", vo.getContenido()));
        
        // Ficheros adjuntos
        if (vo.getFicherosAdjuntos() != null) {
            for (String elemento : vo.getFicherosAdjuntos()) {
               xml.append(crearNoNulo("ficherosAdjuntos", elemento)); 
            }
        }
        
        xml.append("</EnvioCorreo></DescripcionMovimiento>");
        
        return xml.toString();
    }

    /**
     * Crea un elemento XML simple (contiene un nodo texto), tanto si el 
     * contenido es vac�o como si no es vac�o. Escapa el contenido para XML.
     * OJO no usar para elementos que contienen a otros pues escapar�a los tags.
     * @param nombre nombre del elemento
     * @param contenido contenido del elemento
     * @return XML correspondiente
     */
    private static String crearElemento(String nombre, String contenido) {
        if (contenido != null) {
            return "<" + nombre + ">" + escapar(contenido) + "</" + nombre + ">";
        } else {
            return "<" + nombre + "></" + nombre + ">";
        }
    }
    
    /**
     * Crea un elemento XML simple (contiene un nodo texto), pero s�lo si 
     * el contenido no es vac�o. Escapa el contenido para XML.
     * OJO no usar para elementos que contienen a otros pues escapar�a los tags.
     * @param nombre nombre del elemento
     * @param contenido contenido del elemento
     * @return XML correspondiente, string vac�o si el contenido es vac�o
     */
    private static String crearNoNulo(String nombre, String contenido) {
        if (contenido != null && !contenido.equals("")) {
            return "<" + nombre + ">" + escapar(contenido) + "</" + nombre + ">";
        } else {
            return "";
        }
    }
	
	/**
	 * Crea el tag XML correspondiente a cada documento digitalizado.
	 * @param Documento
	 * @return 
	 */
	public static String crearXMLDocumento(List<DocumentoGestor> Documento) {
		String xml = "<DescripcionMovimiento><AltaDocDigitalizado>";
			for(int i=0;i<Documento.size();i++){
				xml+= "<Documento>";
				xml+=crearElemento("nombreDocumento",Documento.get(i).getNombreDocumento());
				xml+= crearElemento("idDocGestorDocumental",Documento.get(i).getNumeroDocumento());
				xml+= crearElemento("codigoDepartamento", Integer.toString(Documento.get(i).getCodigoDepartamento()));
				xml+= crearElemento("codigoUnidadOrg�nica", Integer.toString(Documento.get(i).getCodigoUnidadOrganica()));
				xml+= crearElemento("tipoRegistro", Documento.get(i).getTipoRegistro());
				xml+= crearElemento("ejercicioAnotacion", Integer.toString(Documento.get(i).getEjercicioAnotacion()));
				xml+= crearElemento("numeroRegistro", Long.toString(Documento.get(i).getNumeroRegistro()));
				xml+= "</Documento>";
						}
				xml+= "</AltaDocDigitalizado></DescripcionMovimiento>";
		return xml;
	}
		
	/**
	 * Crea el XML correspondiente a finalizar anotaciones directamente en el alta.
	 *
	 * @param vo DescripcionRegistroValueObject con los datos de la anotaci�n.
	 * @return String en formato XML que describe el movimiento.
	 */
	public static String crearXMLFinalizacionAlta(DescripcionRegistroValueObject vo) {
		String xml = "<DescripcionMovimiento><FinalizarAnotacion>"
				+ crearElemento("tipo", vo.getTipoAsiento())
				+ crearElemento("fechaPres", vo.getFechaEntrada())
				+ crearNoNulo("horaPres", vo.getHoraEntrada())
				+ crearNoNulo("asunto", vo.getAsunto())
				+ crearElemento("extracto", vo.getExtracto())
				+ crearNoNulo("unidad", vo.getUnidad())
				+ crearElemento("tipoEntrada", vo.getTipoEntrada())
				+ crearNoNulo("tipoDocumento", vo.getTipoDoc())
                                + crearNoNulo("tipoRemitente",vo.getTipoRem())
                                + crearNoNulo("procedimiento",vo.getProcedimiento());

		for (String descTercero : vo.getDescTerceros()) {
			xml += crearElemento("interesado", descTercero);
		}
		Vector<String> nombreDocs = vo.getNombreDocs();
		Vector<String> tipoDocs = vo.getTipoDocs();
		Vector<String> fechaDocs = vo.getFechaDocs();
		for (int i = 0; i < nombreDocs.size(); i++) {
			xml += "<documento>"
					+ crearElemento("nombre", nombreDocs.elementAt(i))
					+ crearNoNulo("tipo", tipoDocs.elementAt(i))
					+ crearNoNulo("fecha", fechaDocs.elementAt(i))
					+ "</documento>";
		}

		//Oficina de registro y descripci�n  
		if ((vo.getDescOficinaRegistro() != null) && (!"".equals(vo.getDescOficinaRegistro()))) {
			xml
					+= crearNoNulo("codOficinaRegistro", String.valueOf(vo.getCodOficinaRegistro()))
					+ crearNoNulo("descOficinaRegistro", vo.getDescOficinaRegistro());
		}
		xml += "</FinalizarAnotacion></DescripcionMovimiento>";
		return xml;
	}
	  /**
     * Crea el XML correspondiente a finalizar anotaciones al modificar.
     * @param vo DescripcionRegistroValueObject con los datos de la anotaci�n.
     * @return String en formato XML que describe el movimiento.
     */	
	public static String crearXMLFinalizacionModificacion() {
		String xml = "<DescripcionMovimiento><FinalizarAnotacion>";

		xml += "</FinalizarAnotacion></DescripcionMovimiento>";
		return xml;
	}

    
    /**
     * Escapa un string para XML.
     * @param texto string a escapar
     * @return string escapado
     */
    private static String escapar(String texto) {
        return StringEscapeUtils.escapeXml(texto);
    }
        
}
