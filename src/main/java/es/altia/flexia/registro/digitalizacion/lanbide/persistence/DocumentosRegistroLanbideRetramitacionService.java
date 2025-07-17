/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.registro.digitalizacion.lanbide.persistence;

import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.DocumentoCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.RespRetramitarDocumentosRegistroCambioProcVO;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author INGDGC
 */
public class DocumentosRegistroLanbideRetramitacionService {
    
    private static Logger LOG = Logger.getLogger(DocumentosRegistroLanbideRetramitacionService.class);
    private final DigitalizacionDocumentosLanbideManager digitalizacionDocumentosLanbideManager = DigitalizacionDocumentosLanbideManager.getInstance();
    
    /**
     * Retramita todos los documentos Compulsados de una Entrada de Registro. Se debe recibir el nuevo procedimiento a Asignar y el Owner del Documento Procedimineto autorizado para retramitar Generalmente LANDIS
     * @param codOrganizacion
     * @param datosEntradaRegistro
     * @param codProcedimientoNuevo
     * @param params
     * @param procOwnerOrAutorizaCambiar
     * @return Lista String Detalles, cada linea con el Nombre Documento - OID Documento - Resultado Operacion - Detalles De error en su caso
     * @throws Lan6Excepcion
     */
    public RespRetramitarDocumentosRegistroCambioProcVO retramitarTodosDocumentosERCambioProcedimiento(int codOrganizacion, RegistroValueObject datosEntradaRegistro, String codProcedimientoNuevo, String[] params, String procOwnerOrAutorizaCambiar) throws Lan6Excepcion {
        LOG.info("DigitalizacionDocumentosLanbideManager.retramitarDocumentoCambioProcedimiento(): INICIO");
        RespRetramitarDocumentosRegistroCambioProcVO respuestaServicio = new RespRetramitarDocumentosRegistroCambioProcVO();
        List<String> respuestaDocumentos = new ArrayList<String>();
        String textMess="";
        try {
            respuestaServicio.setDatosEntradaregistro(datosEntradaRegistro);
            List<DocumentoCatalogacionVO> listaDoc = digitalizacionDocumentosLanbideManager.getDocumentosRegistroCompleto(datosEntradaRegistro.getIdentDepart(), datosEntradaRegistro.getUnidadOrgan(), datosEntradaRegistro.getAnoReg(), datosEntradaRegistro.getNumReg(), datosEntradaRegistro.getTipoReg(), params);
            if (listaDoc != null && listaDoc.size() > 0) {
                LOG.info("Nro. Documentos a Tratar: " + listaDoc.size());
                for (DocumentoCatalogacionVO documento : listaDoc) {
                    LOG.info("Doc:  " + documento.getNomDocumento() + " " + documento.getIdDocGestor());
                    // Retramitamos
                    if (documento.getIdDocGestor() != null && !documento.getIdDocGestor().isEmpty()) {
                        try {
                            digitalizacionDocumentosLanbideManager.retramitarDocumentoCambioProcedimiento(codOrganizacion, datosEntradaRegistro.getAnoReg(), datosEntradaRegistro.getNumReg(), documento.getIdDocGestor(), codProcedimientoNuevo, params, procOwnerOrAutorizaCambiar);
                            // Si es correcto, borramos los datos de catalogacion
                            digitalizacionDocumentosLanbideManager.borrarDatosCatalogacionRetramitarCambioProc(documento, params);
                            respuestaDocumentos.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - Retramitado Correctamente.");
                        } catch (Lan6Excepcion e) {
                            LOG.error("Error al retramitar Documento: " + documento.getNomDocumento() + " - " + documento.getIdDocGestor()
                                    + " " + textMess,
                                    e);
                            textMess = (e.getCodes() != null ? Arrays.toString(e.getCodes().toArray()) : "")
                                    + " - " + (e.getMessages() != null ? Arrays.toString(e.getMessages().toArray()) : "")
                                    + " - " + (e.getMessage())
                                    + " - " + (e.getMensajeExcepcion());
                            respuestaDocumentos.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - No Retramitado. " + textMess);
                            respuestaServicio.setErrorEnlaPeticion(Boolean.TRUE);
                            respuestaServicio.setDetalleErrorEnlaPeticion("Se ha  presentado un error al procesar la retramitacion de los Documentos de la entrada por cambio de procedimiento. Modificacion no realizada");
                            break;
                        }
                    } else {
                        LOG.info("No se invoca a retramitar para el Documento " + documento.getNomDocumento() + " - OID no recuperado.");
                        respuestaDocumentos.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - No Retramitado. OID no recuperado.");
                    }
                }
            } else {
                LOG.info("No se han recuperado documentos de asociados a la entrada.");
                respuestaServicio.setDetalleErrorEnlaPeticion("No se han recuperado documentos de asociados a la entrada.");
            }
        } catch (TechnicalException e) {
            LOG.error("TechnicalException al retramitar Documento: "  + textMess,e);
            textMess = e.getMessage();
            respuestaDocumentos.add(datosEntradaRegistro.getAnoReg() + " " + datosEntradaRegistro.getNumReg() + " - No Procesado. " + textMess);
            respuestaServicio.setErrorEnlaPeticion(Boolean.TRUE);
            respuestaServicio.setDetalleErrorEnlaPeticion(datosEntradaRegistro.getAnoReg() + " " + datosEntradaRegistro.getNumReg() + " - No Procesado. " + textMess);
        } catch (BDException e) {
            LOG.error("BDException al retramitar Documento: " + textMess,e);
            textMess = e.getErrorCode() 
                    + " - " +e.getDescripcion()
                    + " - " +e.getMensaje()
                    + " - " +e.getMessage()
                    ;
            respuestaDocumentos.add(datosEntradaRegistro.getAnoReg() + " " + datosEntradaRegistro.getNumReg() + " - No Procesado. " + textMess);
            respuestaServicio.setErrorEnlaPeticion(Boolean.TRUE);
            respuestaServicio.setDetalleErrorEnlaPeticion(datosEntradaRegistro.getAnoReg() + " " + datosEntradaRegistro.getNumReg() + " - No Procesado. " + textMess);
        } catch (SQLException e) {
            LOG.error("SQLException al retramitar Documento: " + textMess,e);
            textMess = e.getErrorCode()
                    + " - " + e.getSQLState()
                    + " - " + e.getMessage()
                    ;
            respuestaDocumentos.add(datosEntradaRegistro.getAnoReg() + " " + datosEntradaRegistro.getNumReg() + " - No Procesado. " + textMess);
            respuestaServicio.setErrorEnlaPeticion(Boolean.TRUE);
            respuestaServicio.setDetalleErrorEnlaPeticion(datosEntradaRegistro.getAnoReg() + " " + datosEntradaRegistro.getNumReg() + " - No Procesado. " + textMess);
        } catch (Exception e) {
            LOG.error("SQLException al retramitar Documento: " + textMess, e);
            textMess = e.getMessage();
            respuestaDocumentos.add(datosEntradaRegistro.getAnoReg() + " " + datosEntradaRegistro.getNumReg() + " - No Procesado. " + textMess);
            respuestaServicio.setErrorEnlaPeticion(Boolean.TRUE);
            respuestaServicio.setDetalleErrorEnlaPeticion(datosEntradaRegistro.getAnoReg() + " " + datosEntradaRegistro.getNumReg() + " - No Procesado. " + textMess);
        }
        respuestaServicio.setDatosDocumentosRetramitados(respuestaDocumentos);
        return respuestaServicio;
    }
    
}
