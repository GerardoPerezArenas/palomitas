package es.altia.agora.interfaces.user.web.util;

import es.altia.agora.business.util.HistoricoAnotacionHelper;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.technical.ConstantesDatos;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Traduce los detalles en XML de un movimiento del histórico de una anotación
 * a un String en formato HTML.
 * 
 * @author juan.jato
 */
public class HistoricoAnotacionTraductorHTML {
    
    //Para informacion de logs.
    private static Log m_Log =
          LogFactory.getLog(HistoricoAnotacionHelper.class.getName());
    
    // Mensaje a devolver en caso de error al parsear un xml
    private static String MENSAJE_ERROR = "OCURRIO UN PROBLEMA TECNICO " +
            "AL RECUPERAR LOS DETALLES DE ESTE MOVIMIENTO";
    
    // Encabezado que se añadira a los xml antes de parsearlos
    private static String ENCABEZADO_XML = 
       "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
       //"<!DOCTYPE DescripcionMovimiento SYSTEM \"DescripcionMovimiento.dtd\">";
    
    // Propiedad que indica si se escriben los rotulos en mayúsculas.
    private static boolean rotulosMayusculas = true;
    // Propiedad que indica si se valida el XML.
    private static boolean validar = false;

    protected void HistoricoAnotacionTraductorHTML() {
    }
    
        /**
     * Obtiene la descripción de un tipo de movimiento de una anotación en el 
     * idioma del usuario.
     * 
     * @param traductor configurado con el idioma del usuario.
     * @param tipoMovimiento Tipo del movimiento.
     * @return un String con la descripción del tipo de movimiento
     */
    public static String getDescripcionMovimiento(TraductorAplicacionBean traductor,
            int tipoMovimiento) {
        
        m_Log.debug("***************************  tipoMovimiento " + tipoMovimiento);
        String descripcion = "DESCONOCIDO";

        switch (tipoMovimiento) {
            case ConstantesDatos.HIST_ANOT_ALTA:
                descripcion = traductor.getDescripcion("etiqAltaAnot");
                break;
            case ConstantesDatos.HIST_ANOT_MODIFICAR:
                descripcion = traductor.getDescripcion("etiqModifAnot");
                break;
            case ConstantesDatos.HIST_ANOT_RESERVA:
                descripcion = traductor.getDescripcion("etiqReservaAnot");
                break;
            case ConstantesDatos.HIST_ANOT_ANULAR:
                descripcion = traductor.getDescripcion("etiqAnularAnot");
                break;
            case ConstantesDatos.HIST_ANOT_DESANULAR:
                descripcion = traductor.getDescripcion("etiqDesAnularAnot");
                break;
            case ConstantesDatos.HIST_ANOT_ACEPTAR:
                descripcion = traductor.getDescripcion("etiqAceptarAnot");
                break;
            case ConstantesDatos.HIST_ANOT_ACEPTAR_EN_DESTINO:
                descripcion = traductor.getDescripcion("etiqEstAceptadaDestino");
                break;
            case ConstantesDatos.HIST_ANOT_RECHAZAR:
                descripcion = traductor.getDescripcion("etiqRechazarAnot");
                break;
            case ConstantesDatos.HIST_ANOT_RECUPERAR:
                descripcion = traductor.getDescripcion("etiqRecuperarAnot");
                break;
            case ConstantesDatos.HIST_ANOT_INICIAR:
                descripcion = traductor.getDescripcion("etiqInicioExpAnot");
                break;
            case ConstantesDatos.HIST_ANOT_ADJUNTAR:
                descripcion = traductor.getDescripcion("etiqAdjuntarExpAnot");
                break;
            case ConstantesDatos.HIST_ANOT_ANULAR_RES:
                descripcion = traductor.getDescripcion("etiqAnularRes");
                break;
            case ConstantesDatos.HIST_ANOT_IMPORTAR:
                descripcion = traductor.getDescripcion("etiqImportarAnot");
                break;
            case ConstantesDatos.HIST_ANOT_ELIMINAR_RECHAZO:
                descripcion = traductor.getDescripcion("etiqEliminarRechazo");
                break;
            case ConstantesDatos.HIST_ANOT_ENVIO_CORREO:
                descripcion = traductor.getDescripcion("etiqEnvioCorreo");
                break;
			case ConstantesDatos.HIST_DOC_DIGIT:
				descripcion=traductor.getDescripcion("etiqDigitalizacionDocumentos");
				break;
			case ConstantesDatos.HIST_ANOT_FINALIZAR:
				descripcion = traductor.getDescripcion("etiqFinalizacionDocumentos");
				break;
			case ConstantesDatos.HIST_ANOT_FINALIZAR_MODIFICACION:
				descripcion = traductor.getDescripcion("etiqFinalizacionModificacion");
				break;
                
            /*** prueba ***/
            case ConstantesDatos.HISTORICO_ANOTACION_CANCELAR_INICIO_EXPEDIENTE:
                descripcion = traductor.getDescripcion("etiqCancelarInicioExp");
                break;    
            case ConstantesDatos.HISTORICO_ANOTACION_CANCELAR_ADJUNTAR_EXPEDIENTE:
                descripcion = traductor.getDescripcion("etiqCancelarAdjuntarExp");
                break;
            /*** prueba ***/
        }

        return descripcion;
    }
    
    /**
     * Traduce los detalles del movimiento en formato XML a un String en 
     * formato HTML. 
     * Delega en los métodos de la clase HistoricoAnotacionTraductorHTML.
     * 
     * @param traductor traductor configurado con el idioma del usuario
     * @param mov HistoricoMovimientoValueObject del movimiento
     * @return String en formato html con los detalles del movimiento
     */
    public static String traducirDetallesHTML(TraductorAplicacionBean traductor,
            HistoricoMovimientoValueObject mov) {

        String detallesXML = mov.getDetallesMovimiento();
        String traduccion = null;

        m_Log.debug("***************************  mov.getTipoMovimiento() " + mov.getTipoMovimiento());
        if (detallesXML == null) {
            traduccion = "NO HAY DETALLES PARA ESTE MOVIMIENTO";
        } else {
            switch (mov.getTipoMovimiento()) {
                case ConstantesDatos.HIST_ANOT_ALTA:
                    traduccion = traducirAlta(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_MODIFICAR:
                    traduccion = traducirModificar(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_RESERVA:
                    traduccion = traducirReservar(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_ANULAR:
                    traduccion = traducirAnular(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_DESANULAR:
                    traduccion = traducirDesanular(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_ACEPTAR:
                    traduccion = traducirAceptar(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_ACEPTAR_EN_DESTINO:
                    traduccion = traducirAceptarEnDestino(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_RECHAZAR:
                    traduccion = traducirRechazar(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_RECUPERAR:
                    traduccion = traducirRecuperar(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_INICIAR:
                    traduccion = traducirIniciar(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_ADJUNTAR:
                    traduccion = traducirAdjuntar(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_ANULAR_RES:
                    traduccion = traducirAnularReserva(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_IMPORTAR:
                    traduccion = traducirAlta(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_ELIMINAR_RECHAZO:
                    traduccion = traducirEliminarRechazar(traductor, mov);
                    break;
                case ConstantesDatos.HIST_ANOT_ENVIO_CORREO:
                    traduccion = traducirEnvioCorreo(traductor, mov);
                    break;
				case ConstantesDatos.HIST_DOC_DIGIT:
					traduccion=traducirMovDocDigit(traductor,mov);
					break;
				case ConstantesDatos.HIST_ANOT_FINALIZAR:
					traduccion = traducirAlta(traductor, mov);
					break;
				case ConstantesDatos.HIST_ANOT_FINALIZAR_MODIFICACION:
					traduccion = traducirFinalizarModificaciones(traductor, mov);
					break;                 
                case ConstantesDatos.HISTORICO_ANOTACION_CANCELAR_INICIO_EXPEDIENTE:
                    traduccion = traducirCancelarInicioExpediente(traductor,mov);
                    break;
                case ConstantesDatos.HISTORICO_ANOTACION_CANCELAR_ADJUNTAR_EXPEDIENTE:
                    traduccion = traducirCancelarAdjuntarExpediente(traductor,mov);
                    break;

            }
        }

        return traduccion;
    }


   /**
     * Traduce el XML que describe iniciar expediente.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirCancelarInicioExpediente(TraductorAplicacionBean mensajes,
       HistoricoMovimientoValueObject mov) {

        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());

            // Obtener el numero de expediente
            String exp = doc.getRootElement().getChild("CancelarInicioExpediente").
                    getChild("expedienteIniciado").getTextTrim();

            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "etiqCancelarInicioExp") + "</b>: " + exp;
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }


     /**
     * Traduce el XML que describe iniciar expediente.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirCancelarAdjuntarExpediente(TraductorAplicacionBean mensajes,
       HistoricoMovimientoValueObject mov) {

        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());

            // Obtener el numero de expediente
            String exp = doc.getRootElement().getChild("CancelarAdjuntarExpediente").
                    getChild("expedienteAdjunto").getTextTrim();

            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "etiqCancelarAdjuntarExp") + "</b>: " + exp;
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }

    
    /**
     * Traduce el XML que describe el alta de una anotación y también el que 
     * describe una anotaciín importada.
     * @param m utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirAlta(TraductorAplicacionBean m, 
       HistoricoMovimientoValueObject mov) {
       
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());            
            // Obtener elemento alta o importar
            Element alta = null;
			switch (mov.getTipoMovimiento()) {
				case ConstantesDatos.HIST_ANOT_ALTA:
					// Alta de anotacion
					alta = doc.getRootElement().getChild("AltaAnotacion");
					detalles = "<b>" + getDesc(m, "histAlta") + "</b>:</br></br>";
					break;
				case ConstantesDatos.HIST_ANOT_FINALIZAR:
					alta = doc.getRootElement().getChild("FinalizarAnotacion");
					detalles = "<b>" + getDesc(m, "etiqFinalizacionModificacion") + "</b>:</br></br>";
					break;
				default:
					// Importacion de anotacion
					alta = doc.getRootElement().getChild("ImportarAnotacion");
					detalles = "<b>" + getDesc(m, "histImportar") + "</b>:</br></br>";
					break;
			}
            
            // Construir HTML
            // Fechas
            String tipoAsiento = getDato(alta,"tipo");
            if ("E".equals(tipoAsiento)) {
                detalles += traducirDato(m, "res_fecE", alta, "fechaEntrada") +
                            traducirDato(m, "res_HoraEnt", alta, "horaEntrada");
            } else {
                detalles += traducirDato(m, "res_fecS", alta, "fechaEntrada") +
                            traducirDato(m, "res_HoraSal", alta, "horaEntrada");
            }
            detalles += traducirDato(m, "res_fecPres", alta, "fechaPres") +
                        traducirDato(m, "res_HoraPres", alta, "horaPres");
            
            // Interesados
            detalles += "<br><b>" + getDesc(m, "histInteresados") + "</b>:<ul>";
            List<Element> interesados = alta.getChildren("interesado");
            for (Element interesado : interesados) {
                detalles += "<li>" + interesado.getTextTrim() + "</li>";
            }
            detalles += "</ul>";
            
            // Asunto
            detalles += traducirDato(m, "res_asunto", alta, "asunto");
            
            // Extracto
            detalles += "<b>" + getDesc(m, "etiq_Extracto") +  "</b>:</br><textarea>" + 
                        getDato(alta, "extracto") + "</textarea></br>";
            
            // Observaciones
            String obs = getDato(alta, "observaciones");
            if (obs != null) {
                detalles += "<b>" + getDesc(m, "etiq_observaciones") +  "</b>:</br><textarea>" + 
                            obs + "</textarea></br>";
            }
            
            //Oficina
            if ("E".equals(tipoAsiento)) {
                detalles += traducirDato(m, "res_ofiReg", alta, "descOficinaRegistro");
            } else {
                detalles += traducirDato(m, "res_ofiReg", alta, "descOficinaRegistro");
            }
            
            // Unidad
            if ("E".equals(tipoAsiento)) {
                detalles += traducirDato(m, "gEtiqUnidDestino", alta, "unidad");
            } else {
                detalles += traducirDato(m, "gEtiqUnidOrigen", alta, "unidad");
            }
            
            // Tipo de entrada
            if ("E".equals(tipoAsiento)) {
                detalles += "<b>" + getDesc(m, "gEtiqTipoEntrada") + "</b>: ";
            } else {
                detalles += "<b>" + getDesc(m, "gEtiqTipoSalida") + "</b>: ";
            }
            int tipoEntrada = Integer.parseInt(getDato(alta,"tipoEntrada"));
            detalles += tipoEntrada + " - ";
            switch (tipoEntrada) {
                case ConstantesDatos.REG_ANOT_TIPO_ORDINARIA:
                    detalles += getDesc(m, "entradaOrd");
                    break;
                case ConstantesDatos.REG_ANOT_TIPO_DESTINO_OTRO_REG:
                    detalles += getDesc(m, "DestOtroReg");
                    break;
                case ConstantesDatos.REG_ANOT_TIPO_ORIGEN_OTRO_REG:
                    detalles += getDesc(m, "procOtroReg");
                    break;
            }
            detalles += "</br>";

            // Tipo de documento, tipo transporte, numero transporte, tipo
            // remitente, procedimiento, expediente
            detalles += traducirDato(m, "res_tipoDoc", alta, "tipoDocumento") +
                    traducirDato(m, "res_tipoTrans", alta, "tipoTransporte") +
                    traducirDato(m, "res_numTransp", alta, "numTransporte") +
                    traducirDato(m, "res_tipoRemit", alta, "tipoRemitente") +
                    traducirDato(m, "gEtiqProc", alta, "procedimiento") +
                    traducirDato(m, "res_ExpRel", alta, "expediente");
            
            // Documentos
            List<Element> documentos = alta.getChildren("documento");
            if (!documentos.isEmpty()) {
                detalles += "<br><b>" + getDesc(m, "histDocumentos") + "</b>:<ul>";
                for (Element documento : documentos) {
                    detalles += "<li>" + traducirDoc(m, documento) + "</li>";
                }
                detalles += "</ul>";
            }
            
            // Autoridad
            if ("E".equals(tipoAsiento)) {
                detalles += traducirDato(m, "etiq_autoridad_a", alta, "autoridad");
            } else {
                detalles += traducirDato(m, "etiq_autoridad_desde", alta, "autoridad");
            }
            
            // Organizacion origen/destino, unidad origen/destino, entrada relacionada.
            detalles += traducirDato(m, "histOrgDes", alta, "orgDestino") +
                    traducirDato(m, "histUniDes", alta, "unidadDestino") +
                    traducirDato(m, "histOrgProc", alta, "orgProcedencia") +
                    traducirDato(m, "histUniProc", alta, "unidadProcedencia") +
                    traducirDato(m, "res_EntRel", alta, "entradaRelacionada");
            
            // Relaciones
            List<Element> relaciones = alta.getChildren("relacion");
            if (!relaciones.isEmpty()) {
                detalles += "<br><b>" + getDesc(m, "histRelaciones") + "</b>:<ul>";
                for (Element relacion : relaciones) {
                    detalles += "<li>" + traducirRelacion(m, relacion) + "</li>";
                }
                detalles += "</ul>";
            }
            
            // Actuación y temas
            detalles += traducirDato(m, "res_actuacion", alta, "actuacion");
            List<Element> temas = alta.getChildren("tema");
            if (!temas.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histTemas") + "</b>:<ul>";
                for (Element tema : temas) {
                    detalles += "<li>" + tema.getTextTrim() + "</li>";
                }
                detalles += "</ul>";
            }

        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    
    /**
     * Traduce el XML que describe la modificación de una anotación.
     * @param m utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirModificar(TraductorAplicacionBean m, 
       HistoricoMovimientoValueObject mov) {
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());            
            // Obtener elemento modificacion
            Element modif = doc.getRootElement().getChild("ModificarAnotacion");
            m_Log.debug("***************************  modif " +modif);
            String tipoAsiento = getDato(modif,"tipo");
            m_Log.debug("***************************  modif " +modif);
            // Construir HTML
            detalles = "<b>" + getDesc(m, "histModif") + "</b>:</br></br>";
            // Comprobar si no hay ningún cambio
            if (modif.getContentSize() == 0) {
                detalles += getDesc(m, "histNoModif");
                return detalles;
            }
            
            m_Log.debug("***************************  modif.getContentSize() " +modif.getContentSize());
            // Fechas
            if ("E".equals(tipoAsiento)) {
                detalles += traducirCambio(m, "res_fecE", modif, "cambioFechaEntrada") +
                            traducirCambio(m, "res_HoraEnt", modif, "cambioHoraEntrada");
            } else {
                detalles += traducirCambio(m, "res_fecS", modif, "cambioFechaEntrada") +
                            traducirCambio(m, "res_HoraSal", modif, "cambioHoraEntrada");
            }
            detalles += traducirCambio(m, "res_fecPres", modif, "cambioFechaPres") +
                        traducirCambio(m, "res_HoraPres", modif, "cambioHoraPres");
            detalles += traducirCambio(m, "res_fecDocu", modif, "cambiofechaDocu");

            // Interesados de alta
            List<Element> interesadosAlta = modif.getChildren("nuevoInteresado");
            if (!interesadosAlta.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histIntAlta") + "</b>:<ul>";
                for (Element interesado : interesadosAlta) {
                    detalles += "<li>" + interesado.getTextTrim() + "</li>";
                }
                detalles += "</ul>";
            }
            
            // Interesados borrados
            List<Element> interesadosBaja = modif.getChildren("borrarInteresado");
            if (!interesadosBaja.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histIntBaja") + "</b>:<ul>";
                for (Element interesado : interesadosBaja) {
                    detalles += "<li>" + interesado.getTextTrim() + "</li>";
                }
                detalles += "</ul>";
            }
            
            // Interesados modificados
            List<Element> interesadosModif = modif.getChildren("cambioInteresado");
            if (!interesadosModif.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histIntModif") + "</b>:<ul>";
                for (Element interesado : interesadosModif) {
                    detalles += "<li>" + getDato(interesado,"anterior") + 
                                "</br> &#187;</br>" + getDato(interesado, "actual") + 
                                "</li>";
                }
                detalles += "</ul>";
            }
            
            // Asunto
            detalles += traducirCambio(m, "res_asunto", modif, "cambioAsunto");
            
            // Extracto
            Element extracto = modif.getChild("cambioExtracto");
            if (extracto != null) {
                detalles += "<b>" + getDesc(m, "etiq_Extracto") +  "</b>:</br><textarea>" + 
                            getDato(extracto, "anterior") + "\n" +
                            "&#187;\n" + 
                            getDato(extracto, "actual") + "</textarea></br>";
            }
            
            // Observaciones
            Element obs = modif.getChild("cambioObservaciones");
            if (obs != null) {
                detalles += "<b>" + getDesc(m, "etiq_observaciones") +  "</b>:</br><textarea>" + 
                            getDato(obs, "anterior") + "\n" +
                            "&#187;\n" + 
                            getDato(obs, "actual") + "</textarea></br>";
            }
            
            // Unidad
            if ("E".equals(tipoAsiento)) {
                detalles += traducirCambio(m, "gEtiqUnidDestino", modif, "cambioUnidad");
            } else {
                detalles += traducirCambio(m, "gEtiqUnidOrigen", modif, "cambioUnidad");
            }
            
            // Tipo de entrada
            Element tipEntr = modif.getChild("cambioTipoEntrada");
            if (tipEntr != null) {
                if ("E".equals(tipoAsiento)) {
                    detalles += "<b>" + getDesc(m, "gEtiqTipoEntrada") + "</b>: ";
                } else {
                    detalles += "<b>" + getDesc(m, "gEtiqTipoSalida") + "</b>: ";
                }
                int tipoEntradaAnterior = Integer.parseInt(getDato(tipEntr,"anterior"));
                int tipoEntradaActual = Integer.parseInt(getDato(tipEntr,"actual"));
                detalles += tipoEntradaAnterior + " - ";
                switch (tipoEntradaAnterior) {
                    case ConstantesDatos.REG_ANOT_TIPO_ORDINARIA:
                        detalles += getDesc(m, "entradaOrd");
                        break;
                    case ConstantesDatos.REG_ANOT_TIPO_DESTINO_OTRO_REG:
                        detalles += getDesc(m, "DestOtroReg");
                        break;
                    case ConstantesDatos.REG_ANOT_TIPO_ORIGEN_OTRO_REG:
                        detalles += getDesc(m, "procOtroReg");
                        break;
                }
                detalles += " &#187; " + tipoEntradaActual + " - ";
                switch (tipoEntradaActual) {
                    case ConstantesDatos.REG_ANOT_TIPO_ORDINARIA:
                        detalles += getDesc(m, "entradaOrd");
                        break;
                    case ConstantesDatos.REG_ANOT_TIPO_DESTINO_OTRO_REG:
                        detalles += getDesc(m, "DestOtroReg");
                        break;
                    case ConstantesDatos.REG_ANOT_TIPO_ORIGEN_OTRO_REG:
                        detalles += getDesc(m, "procOtroReg");
                        break;
                }
                detalles += "</br>";
            }

            // Tipo de documento, tipo transporte, numero transporte, tipo
            // remitente, procedimiento, expediente, registro telematico
            detalles += traducirCambio(m, "res_tipoDoc", modif, "cambioTipoDocumento") +
                    traducirCambio(m, "res_tipoTrans", modif, "cambioTipoTransporte") +
                    traducirCambio(m, "res_numTransp", modif, "cambioNumTransporte") +
                    traducirCambio(m, "res_tipoRemit", modif, "cambioTipoRemitente") +
                    traducirCambio(m, "gEtiqProc", modif, "cambioProcedimiento") +
                    traducirCambio(m, "res_ExpRel", modif, "cambioExpediente") +
                    traducirCambio(m, "histAnotTel", modif, "cambioRegistroTelematico");
            
            // Documentos nuevos
            List<Element> documentosAlta = modif.getChildren("nuevoDocumento");
            if (!documentosAlta.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histDocAlta") + "</b>:<ul>";
                for (Element documento : documentosAlta) {
                    detalles += "<li>" + traducirDoc(m, documento) + "</li>";
                }
                detalles += "</ul>";
            }
            
            // Documentos eliminados
            List<Element> documentosBaja = modif.getChildren("borrarDocumento");
            if (!documentosBaja.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histDocBaja") + "</b>:<ul>";
                for (Element documento : documentosBaja) {
                    detalles += "<li>" + traducirDoc(m, documento) + "</li>";
                }
                detalles += "</ul>";
            }
            
            // Documentos modificados
            List<Element> documentosModif = modif.getChildren("cambioDocumento");
            if (!documentosModif.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histDocModif") + "</b>:<ul>";
                for (Element documento : documentosModif) {
                    detalles += "<li>" + traducirDoc(m, documento.getChild("docAnterior")) +
                                "</br> &#187;</br>" + traducirDoc(m, documento.getChild("docActual")) + 
                                "</li>";
                }
                detalles += "</ul>";
            }
            
            // Autoridad
            if ("E".equals(tipoAsiento)) {
                detalles += traducirCambio(m, "etiq_autoridad_a", modif, "cambioAutoridad");
            } else {
                detalles += traducirCambio(m, "etiq_autoridad_desde", modif, "cambioAutoridad");
            }
            
            // Organizacion origen/destino, unidad origen/destino, entrada relacionada.
            detalles += traducirCambio(m, "histOrgDes", modif, "cambioOrgDestino") +
                    traducirCambio(m, "histUniDes", modif, "cambioUnidadDestino") +
                    traducirCambio(m, "histOrgProc", modif, "cambioOrgProcedencia") +
                    traducirCambio(m, "histUniProc", modif, "cambioUnidadProcedencia") +
                    traducirCambio(m, "res_EntRel", modif, "cambioEntradaRelacionada");
            
            // Relaciones nuevas
            List<Element> relacionesAlta = modif.getChildren("nuevaRelacion");
            if (!relacionesAlta.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histRelAlta") + "</b>:<ul>";
                for (Element relacion : relacionesAlta) {
                    detalles += "<li>" + traducirRelacion(m, relacion) + "</li>";
                }
                detalles += "</ul>";
            }
            
            // Relaciones eliminadas
            List<Element> relacionesBaja = modif.getChildren("borrarRelacion");
            if (!relacionesBaja.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histRelBaja") + "</b>:<ul>";
                for (Element relacion : relacionesBaja) {
                    detalles += "<li>" + traducirRelacion(m, relacion) + "</li>";
                }
                detalles += "</ul>";
            }
            
            // Actuación y temas
            detalles += traducirCambio(m, "res_actuacion", modif, "cambioActuacion");
            // Temas nuevos
            List<Element> temasAlta = modif.getChildren("nuevoTema");
            if (!temasAlta.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histTemaAlta") + "</b>:<ul>";
                for (Element tema : temasAlta) {
                    detalles += "<li>" + tema.getTextTrim() + "</li>";
                }
                detalles += "</ul>";
            }
            // Temas borrados
            List<Element> temasBaja = modif.getChildren("borrarTema");
            if (!temasBaja.isEmpty()) {
                detalles += "<b>" + getDesc(m, "histTemaBaja") + "</b>:<ul>";
                for (Element tema : temasBaja) {
                    detalles += "<li>" + tema.getTextTrim() + "</li>";
                }
                detalles += "</ul>";
            }

        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    
    /**
     * Traduce un documento.
     * @param m Utilidad de mensajes
     * @param doc Element que contiene los datos del documento
     * @return html correspondiente al documento traducido
     */
    private static String traducirDoc(TraductorAplicacionBean m, Element doc) {
        String traduccion = getDato(doc, "nombre") + ", ";
        String tipo = getDato(doc, "tipo");
        if (tipo != null) {
            traduccion += getDesc(m, "histTipo") + ": " + tipo + ", ";
            traduccion += getDesc(m ,"histFechaAportado") + ": " + getDato(doc, "fecha");
            
            String cotejo = getDato(doc, "cotejo");
            if (cotejo != null) {
                traduccion += ", " + getDesc(m ,"etiqCompulsado") + ": " + cotejo;   
            }
        } else {
            traduccion += getDesc(m, "histNoAportado");
        }
        return traduccion;
    }
    
    /**
     * Traduce una relación.
     * @param m Utilidad de mensajes
     * @param doc Element que contiene los datos de la relación
     * @return html correspondiente a la relación traducida
     */
    private static String traducirRelacion(TraductorAplicacionBean m, Element relacion) {
        String tipo = getDato(relacion, "tipo");
        String traduccion = "";
        
        if (tipo.equals("E")) {
            traduccion += getDesc(m, "etiqTipoAnotEntrada");
        } else {
            traduccion += getDesc(m, "etiqTipoAnotSalida");
        }
        
        traduccion += " " + getDato(relacion, "ejeNum");
        return traduccion;
    }
    
    /**
     * En caso de existir el hijo 'hijo' en el elemento 'e', devuelve un String
     * correspondiente a una linea con el rótulo indicado por 'codigoMensaje' y
     * el cambio que contenga el hijo. En caso de no existir devuelve un String
     * vacío.
     * @param m utilidad de mensajes
     * @param codMensaje codigo del rótulo a mostrar
     * @param e elemento que contiene al hijo
     * @param hijo nombre del elemento hijo
     * @return una linea traducida si existe el dato, string vacio si no
     */
    private static String traducirCambio(TraductorAplicacionBean m, 
            String codMensaje, Element e, String hijo) {
        
        Element cambio = e.getChild(hijo);
        if (cambio != null) {
            String anterior = getDato(cambio, "anterior");
            if (anterior.equals("")) anterior = "(" + getDesc(m, "histSinValor") + ")";
            String actual = getDato(cambio, "actual");
            if (actual.equals("")) actual = "(" + getDesc(m, "histSinValor") + ")";
            return "<b>" + getDesc(m, codMensaje) + "</b>" + ": " + anterior + " &#187; " + actual + "</br>";
        } else {
            return "";
        }
    }

    /**
     * Traduce el XML que describe reservar una anotación.
     * @param m utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirReservar(TraductorAplicacionBean m, 
       HistoricoMovimientoValueObject mov) {
        
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());
            
            // Obtener elemento reserva
            Element reserva = doc.getRootElement().getChild("ReservarAnotacion");

            // Construir HTML
            detalles = "<b>" + getDesc(m, "histReservada") + "</b>:</br></br>" +
               traducirDato(m, "histNumero", reserva, "numero") +
               traducirDato(m, "histEjercicio", reserva, "ejercicio") +
               traducirDato(m, "histFechaReserva", reserva, "fechaEntrada") +
               traducirDato(m, "histHoraReserva", reserva, "horaEntrada");  
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    
    /**
     * Traduce el XML que describe anular una anotación.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirAnular(TraductorAplicacionBean mensajes, 
       HistoricoMovimientoValueObject mov) {
        
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());
            
            // Obtener texto
            String texto = doc.getRootElement().getChild("AnularAnotacion").
                    getChild("diligenciaAnulacion").getTextTrim();
            
            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "histAnulada") + 
                    "</b>:</br><textarea>" + texto + "</textarea>";  
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    
    /**
     * Traduce el XML que describe una recuperación de anulada.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirDesanular(TraductorAplicacionBean mensajes, 
       HistoricoMovimientoValueObject mov) {
        // Este tipo de movimiento no contiene detalles.
        return "<b>" + getDesc(mensajes, "histSinDetalles") + "</b>";
    }
    
    /**
     * Traduce el XML que describe aceptar anotación.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirAceptar(TraductorAplicacionBean mensajes, 
       HistoricoMovimientoValueObject mov) {
        
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());
            
            // Obtener texto
            String texto = doc.getRootElement().getChild("AceptarAnotacion").
                    getChild("textoAceptacion").getTextTrim();
            
            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "histAceptada") + 
                    "</b>:</br><textarea>" + texto + "</textarea>";  
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    
        /**
     * Traduce el XML que describe aceptar en destino la anotación.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirAceptarEnDestino(TraductorAplicacionBean mensajes, 
       HistoricoMovimientoValueObject mov) {
        
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());
            
            // Obtener texto
            String texto = doc.getRootElement().getChild("AceptarAnotacionEnDestino").
                    getChild("textoAceptacion").getTextTrim();
            
            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "histAceptadaDestino") + 
                    "</b>:</br><textarea>" + texto + "</textarea>";  
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    
    /**
     * Traduce el XML que describe rechazar anotación.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirRechazar(TraductorAplicacionBean mensajes, 
       HistoricoMovimientoValueObject mov) {
        
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());
            
            // Obtener texto
            String texto = doc.getRootElement().getChild("RechazarAnotacion").
                    getChild("textoRechazo").getTextTrim();
            
            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "histRechazada") + 
                    "</b>:</br><textarea>" + texto + "</textarea>"; 
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    
    /**
     * Traduce el XML que describe una recuperación del histórico.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirRecuperar(TraductorAplicacionBean mensajes, 
       HistoricoMovimientoValueObject mov) {
        // Este tipo de movimiento no contiene detalles.
        return "<b>" + getDesc(mensajes, "histSinDetalles") + "</b>";
    }
    
    
    /**
     * Traduce el XML que describe iniciar expediente.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirIniciar(TraductorAplicacionBean mensajes, 
       HistoricoMovimientoValueObject mov) {
        
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());
            
            // Obtener el numero de expediente
            String exp = doc.getRootElement().getChild("InicioExpediente").
                    getChild("expedienteIniciado").getTextTrim();
            
            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "histExpIni") + "</b>: " + exp; 
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    
    /**
     * Traduce el XML que describe adjuntar a expediente.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirAdjuntar(TraductorAplicacionBean mensajes, 
       HistoricoMovimientoValueObject mov) {
        
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());
            
            // Obtener el numero de expediente
            String exp = doc.getRootElement().getChild("AdjuntarExpediente").
                    getChild("expedienteAdjunto").getTextTrim();
            
            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "histExpAdjunto") + "</b>: " + exp; 
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    
    /**
     * Traduce el XML que describe anular una reserva.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirAnularReserva(TraductorAplicacionBean mensajes, 
       HistoricoMovimientoValueObject mov) {
        
        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());
            
            // Obtener texto
            String texto = doc.getRootElement().getChild("AnularReserva").
                    getChild("diligenciaAnulacion").getTextTrim();
            
            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "histResAnulada") + 
                    "</b>:</br><textarea>" + texto + "</textarea>";  
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }



    /**************************************** TODOOOOOOOOOOOOOO ***************************************/


  /**
     * Traduce el XML que describe la operación de eliminación de rechazo de una anotación.
     * @param mensajes utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirEliminarRechazar(TraductorAplicacionBean mensajes,
       HistoricoMovimientoValueObject mov) {

        String detalles = "";
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());

            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "histEliminarRechazo");
                    

            // acabar con esta operación que es la que falta
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }

    /**
     * Traduce el XML que describe el envio de correos.
     *
     * @param m utilidad de mensajes configurada con el idioma del usuario.
     * @param mov ValueObject que contiene el XML con la descripción.
     * @return Código HTML que describe el movimiento.
     */
    public static String traducirEnvioCorreo(TraductorAplicacionBean m,
            HistoricoMovimientoValueObject mov) {

        StringBuilder detalles = new StringBuilder("");
        try {
            // Parsear string
            Document doc = parsearString(mov.getDetallesMovimiento());
            // Obtener elemento de envio de correo
            Element envioCorreo = null;
            envioCorreo = doc.getRootElement().getChild("EnvioCorreo");
            detalles.append("<b>").append(getDesc(m, "histEnvioCorreo")).append("</b>:</br/></br/>");

            // Construir HTML
            // Razon
            String datoRazon = getDato(envioCorreo, "razon");
            if (datoRazon != null) {
                String razon = "";
                
                Integer tipoRazon = Integer.parseInt(datoRazon);
                switch (tipoRazon) {
                    case ConstantesDatos.HIST_ENVIO_CORREO_DOC_COTEJADO:
                        razon = getDesc(m, "histRazonDocCotejado");
                        break;
                }
                
                detalles.append("<b>").append(getDesc(m, "histRazon")).append("</b>: ")
                        .append(razon).append("<br/><br/>");
            }

            // Para
            List<Element> elementosPara = envioCorreo.getChildren("para");
            if (elementosPara != null) {
                detalles.append("<b>").append(getDesc(m, "histPara")).append("</b>: ");
                
                List<String> listaPara = new ArrayList<String>(elementosPara.size());
                for (Element para : elementosPara) {
                    listaPara.add(para.getTextTrim());
                }
                detalles.append(StringUtils.join(listaPara, ", ")).append("<br>");
            }

            // CC
            List<Element> elementosCc = envioCorreo.getChildren("cc");
            if (elementosCc != null) {
                detalles.append("<b>").append(getDesc(m, "histCC")).append("</b>: ");
                
                List<String> listaCc = new ArrayList<String>(elementosCc.size());
                for (Element cc : elementosCc) {
                    listaCc.add(cc.getTextTrim());
                }
                detalles.append(StringUtils.join(listaCc, ", ")).append("<br>");
            }
            
            // Cco
            List<Element> elementosCco = envioCorreo.getChildren("cco");
            if (elementosCco != null) {
                detalles.append("<b>").append(getDesc(m, "histCCO")).append("</b>: ");
                
                List<String> listaCco = new ArrayList<String>(elementosCco.size());
                for (Element cco : elementosCco) {
                    listaCco.add(cco.getTextTrim());
                }
                detalles.append(StringUtils.join(listaCco, ", ")).append("<br>");
            }
            
            // Asunto
            String asunto = getDato(envioCorreo, "asunto");
            if (asunto != null) {
                detalles.append("<b>").append(getDesc(m, "histAsunto")).append("</b>: ")
                        .append(asunto).append("<br/>");
            }
            
            // Contenido
            String contenido = getDato(envioCorreo, "contenido");
            if (contenido != null) {
                detalles.append("<b>").append(getDesc(m, "histContenido")).append("</b>:<br>")
                        .append("<textarea rows=\"5\">").append(contenido).append("</textarea><br/>");
            }

            // Ficheros adjuntos
            List<Element> ficherosAdjuntos = envioCorreo.getChildren("ficherosAdjuntos");
            if (ficherosAdjuntos != null) {
                detalles.append("<b>").append(getDesc(m, "histFicherosAdjuntos")).append("</b>:").append("<ul>");
                
                for (Element fichero : ficherosAdjuntos) {
                    detalles.append("<li>").append(fichero.getTextTrim()).append("</li>");
                }
                
                detalles.append("</ul>");
            }
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = new StringBuilder(MENSAJE_ERROR);
        }
        
        return detalles.toString();
    }
	
	public static String traducirFinalizarModificaciones(TraductorAplicacionBean mensajes,
       HistoricoMovimientoValueObject mov) {

        String detalles = "";
        try {
            // Construir HTML
            detalles = "<b>" + getDesc(mensajes, "etiqFinDigModif")+ "</b> ";
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
	
	/**
	 * Traduce el XML que describe el movimiento insertado en histórico al digitalizar documentos
	 * @param mensajes
	 * @param mov
	 * @return 
	 */
	public static String traducirMovDocDigit(TraductorAplicacionBean m,
       HistoricoMovimientoValueObject mov) {
		Element digi=null;
		Element digi2=null;
        String detalles = "";
        try {		
			// Parsear string
			
            Document doc = parsearString(mov.getDetallesMovimiento());
			digi = doc.getRootElement().getChild("AltaDocDigitalizado").getChild("Documento");
			digi2= doc.getRootElement().getChild("AltaDocDigitalizado");
            // Construir HTML
			List<Element> k=digi2.getChildren("Documento");
			
			detalles = "<b>" +  getDesc(m, "etiqDigitalizacionDocumentos") + "</b><br><br>";
			detalles += traducirDato(m, "gEtiqDepto", digi, "codigoDepartamento") ;
			detalles += traducirDato(m, "etiqCodUndOrganica", digi, "codigoUnidadOrgánica");
			detalles += traducirDato(m, "etiqTipoRegistro", digi, "tipoRegistro") ;
			detalles += traducirDato(m, "etiqEjercicioAnotacion", digi, "ejercicioAnotacion") ;
			detalles += traducirDato(m, "etiqNumeroRegistro", digi, "numeroRegistro")+"</br>";
			for(int i=0;i<k.size();i++){
				detalles += traducirDato(m, "etiqNombreDocumento", k.get(i), "nombreDocumento");
				detalles += traducirDato(m, "etiqIdDocGestDoc", k.get(i), "idDocGestorDocumental")+"</br>";
			}
        } catch (Exception e) {
            m_Log.error("EXCEPCION: " + e.getMessage());
            e.printStackTrace();
            detalles = MENSAJE_ERROR;
        }
        return detalles;
    }
    /**************************************** TODOOOOOOOOOOOOOO ***************************************/



    
    /**
     * Parsea un string con xml y devuelve un Document de JDOM.
     * @param xml String que contiene XML
     * @return org.jdom.Document XML parseado
     * @throws org.jdom.JDOMException
     * @throws java.io.IOException
     */
    private static Document parsearString(String xml)
            throws JDOMException, IOException {
        StringReader reader = new StringReader(ENCABEZADO_XML + xml);
        // Cremos parser con 'true' para validación.
        SAXBuilder builder = new SAXBuilder(validar);
        return builder.build(reader);
    }
    
    /**
     * En caso de existir el hijo 'hijo' en el elemento 'e', devuelve un String
     * correspondiente a una linea con el rótulo indicado por 'codigoMensaje' y
     * el contenido del hijo. En caso de no existir devuelve un String vacío.
     * @param m utilidad de mensajes
     * @param codigoMensaje codigo del rótulo a mostrar
     * @param e elemento que contiene al hijo
     * @param hijo nombre del elemento hijo
     * @return una linea traducida si existe el dato, string vacio si no
     */
    private static String traducirDato(TraductorAplicacionBean m, 
            String codMensaje, Element e, String hijo) {
        
        String dato = getDato(e, hijo);
        if (dato == null) {
            return "";
        } else {
            return "<b>" + getDesc(m, codMensaje) + "</b>: " + dato + "</br>";
        }
    }
    
    /**
     * Devuelve el valor contenido en el hijo especificado del elemento que se
     * indica. Si no existe el hijo, devuelve null.
     * 
     * @param e elemento con hijos.
     * @param hijo nombre del hijo del que queremos su contenido.
     * @return contenido del hijo.
     */
    private static String getDato(Element e, String hijo) {
        Element elementoHijo = e.getChild(hijo);
        if (elementoHijo != null) {
            return elementoHijo.getTextTrim();
        } else {
            return null;
        }
    }
    
    /**
     * Devuelve el mensaje de la utilidad de mensajes correspondiente al código 
     * indicado. Si la propiedad 'rotulosMayusculas' es 'true' pasa a 
     * mayúsuculas el mensaje.
     * @param m Utilidad de mensajes.
     * @param codigo Código del mensaje.
     * @return mensaje
     */
    private static String getDesc(TraductorAplicacionBean m, String codigo) {
        if (rotulosMayusculas) {
            if (m.getDescripcion(codigo) != null) {
                return m.getDescripcion(codigo).toUpperCase();
            } else {
                return null;
            }
        } else {
            return m.getDescripcion(codigo);
        }
    }


}
