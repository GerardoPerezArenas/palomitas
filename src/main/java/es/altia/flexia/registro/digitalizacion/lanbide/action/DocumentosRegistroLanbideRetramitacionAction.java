/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.registro.digitalizacion.lanbide.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.DigitalizacionDocumentosLanbideManager;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.GeneralComboServiceManager;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.DocumentoCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.ValidacionRetramitacionCambioProVO;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author INGDGC
 */

    public class DocumentosRegistroLanbideRetramitacionAction extends DispatchAction{
    
    private static Logger LOG = Logger.getLogger(DocumentosRegistroLanbideRetramitacionAction.class);
    private final DigitalizacionDocumentosLanbideManager digitalizacionDocumentosLanbideManager = DigitalizacionDocumentosLanbideManager.getInstance();
    private final GeneralComboServiceManager generalComboServiceManager = new GeneralComboServiceManager();
    private final TraductorAplicacionBean traductorAplicacionBean = new TraductorAplicacionBean();
    private UsuarioValueObject usuario=null;

    public ActionForward retramitarDocumentosCambioProcedimiento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.info("Iniciamos action de Retramitacion Documentos.");
        List<String> respuestaServicio = new ArrayList<String>();
        try {
            // Validamos entrada Rellenar parametros generales
            validarRequestSessionSetUsuarioTraductor(mapping,request);
            //Recojo los parametros
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            String codigoOrgaEsquema = (String) request.getParameter("codigoOrgaEsquema");
            String codDepPKAnotacion = (String) request.getParameter("codDepPKAnotacion");
            String codUnidOrgPKAnotacion = (String) request.getParameter("codUnidOrgPKAnotacion");
            String tipoAnotacion = (String) request.getParameter("tipoAnotacion");
            String ejercicioAnotacion = (String) request.getParameter("ejercicioAnotacion");
            String numeroAnotacion = (String) request.getParameter("numeroAnotacion");
            String codProcedimiento = (String) request.getParameter("codProcedimiento");
            String ownerSeleccionado = (String) request.getParameter("ownerSeleccionado");
            LOG.info("tipoOperacion : " + tipoOperacion);
            LOG.info("codDepPKAnotacion/codUnidOrgPKAnotacion/tipoAnotacion : " + codDepPKAnotacion+"/"+codUnidOrgPKAnotacion+"/"+tipoAnotacion);
            LOG.info("ejercicioAnotacion/numeroAnotacion : " + ejercicioAnotacion+"/"+numeroAnotacion);
            LOG.info("codProcedimiento: " + codProcedimiento);
            LOG.info("ownerSeleccionado: " + ownerSeleccionado);
            // Leer Documentos de la entrada y por cada uno llamar a retramitar
            String[] params = usuario.getParamsCon();
            RegistroValueObject registro = new RegistroValueObject();
            registro.setIdentDepart(Integer.valueOf(codDepPKAnotacion));
            registro.setUnidadOrgan(Integer.valueOf(codUnidOrgPKAnotacion));
            registro.setTipoReg(tipoAnotacion);
            registro.setAnoReg(Integer.valueOf(ejercicioAnotacion));
            registro.setNumReg(Integer.valueOf(numeroAnotacion));
            if (ownerSeleccionado == null || ownerSeleccionado.isEmpty()) {
                LOG.info("No se han recuperado datos del procedimiento Owner de los Documentos. No se ejecuta la retramitacion.");
                respuestaServicio.add("selectOwner");
            }else{
                List<DocumentoCatalogacionVO> listaDoc = digitalizacionDocumentosLanbideManager.getDocumentosRegistroCompleto(registro.getIdentDepart(), registro.getUnidadOrgan(), registro.getAnoReg(), registro.getNumReg(), registro.getTipoReg(), params);
                if (listaDoc != null && listaDoc.size() > 0) {
                    LOG.info("Nro. Documentos a Tratar: " + listaDoc.size());
                    for (DocumentoCatalogacionVO documento : listaDoc) {
                        LOG.info("Doc:  " + documento.getNomDocumento() + " " + documento.getIdDocGestor());
                        // Retramitamos
                        if (documento.getIdDocGestor() != null && !documento.getIdDocGestor().isEmpty()) {
                            try {
                                digitalizacionDocumentosLanbideManager.retramitarDocumentoCambioProcedimiento(Integer.valueOf(codigoOrgaEsquema), registro.getAnoReg(), registro.getNumReg(), documento.getIdDocGestor(), codProcedimiento, params, ownerSeleccionado);
                                // Si es correcto, borramos los datos de catalogacion
                                digitalizacionDocumentosLanbideManager.borrarDatosCatalogacionRetramitarCambioProc(documento, params);
                                respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocOK"));
                            } catch (Lan6Excepcion e) {
                                String textMess = (e.getCodes() != null ? Arrays.toString(e.getCodes().toArray()) : "")
                                        + " - " + (e.getMessages() != null ? Arrays.toString(e.getMessages().toArray()) : "")
                                        + " - " + (e.getMessage())
                                        + " - " + (e.getMensajeExcepcion())
                                        ;
                                LOG.error("Error al retramitar Documento: " + documento.getNomDocumento() + " - " + documento.getIdDocGestor()
                                        + " " + textMess,
                                         e);
                                respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocNOOK") + textMess);
                            }
                        } else {
                            LOG.info("No se invoca a retramitar para el Documento " + documento.getNomDocumento() + " - OID no recuperado.");
                            respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocNOOK") +" " + traductorAplicacionBean.getDescripcion("msgRetramiDocNoOID"));
                        }
                    }
                } else {
                    LOG.info("No se han recuperado documentos de asociados a la entrada.");
                    respuestaServicio.add(traductorAplicacionBean.getDescripcion("msgRetramiDocNoDocs"));
                }
            }
        } catch (Exception e) {
            LOG.error("Error tratando la operacion Retramitar : " + e.getMessage(), e);
            respuestaServicio.add(traductorAplicacionBean.getDescripcion("msgRetramiDocERROROpe") + " : " + e.getMessage());
        }
        
        LOG.info("respuestaServicio : " + Arrays.toString(respuestaServicio.toArray()));
        prepararResponseJSON(respuestaServicio, response);
        return null;
    }
    
    public ActionForward retramitarDocumentosCargarPantallaSelectOwner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.info("retramitarDocumentosCargarPantallaSelectOwner -  Begin ");
        List<GeneralComboVO> listaProcedimientos = new ArrayList<GeneralComboVO>();
        try {
            // Validamos entrada Rellenar parametros generales
            validarRequestSessionSetUsuarioTraductor(mapping,request);
            //Recojo los parametros
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            String codigoOrgaEsquema = (String) request.getParameter("codigoOrgaEsquema");
            LOG.info("CodOrganizacion/tipoOperacion : " + tipoOperacion+"/"+codigoOrgaEsquema);
            // Leer Documentos de la entrada y por cada uno llamar a retramitar
            String[] params = usuario.getParamsCon();
            listaProcedimientos = generalComboServiceManager.getComboProcedimiento(Integer.valueOf(codigoOrgaEsquema), params);
        } catch (Exception e) {
            LOG.error("Error recuperado las opciones del combo Procedimiento : " + e.getMessage(), e);
        }
        LOG.info("Elementos combo leidos : " + listaProcedimientos!= null ? listaProcedimientos.size() : 0);
        prepararResponseJSON(listaProcedimientos, response);
        return null;
    }
    
    private static void prepararResponseJSON(Object elemento,HttpServletResponse response){
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(elemento);
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (Exception e) {
            LOG.error("Error preparando respuesta " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    public ActionForward registrarDocumentosCompulsadosDokusi(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.info("Iniciamos action de Registro  Documentos.");
        List<String> respuestaServicio = new ArrayList<String>();
        try {
            // Validamos entrada Rellenar parametros generales
            validarRequestSessionSetUsuarioTraductor(mapping,request);
            //Recojo los parametros
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            String codigoOrgaEsquema = (String) request.getParameter("codigoOrgaEsquema");
            String codDepPKAnotacion = (String) request.getParameter("codDepPKAnotacion");
            String codUnidOrgPKAnotacion = (String) request.getParameter("codUnidOrgPKAnotacion");
            String tipoAnotacion = (String) request.getParameter("tipoAnotacion");
            String ejercicioAnotacion = (String) request.getParameter("ejercicioAnotacion");
            String numeroAnotacion = (String) request.getParameter("numeroAnotacion");
            String codProcedimiento = (String) request.getParameter("codProcedimiento");
            String registrarTodos = (String) request.getParameter("registrarTodos");
            String registrarDokusiOidDocumento = (String) request.getParameter("registrarDokusiOidDocumento");
            String registrarDokusiDniUsuario = (String) request.getParameter("registrarDokusiDniUsuario");
            String registrarDokusiNombreApellidosUsuario = (String) request.getParameter("registrarDokusiNombreApellidosUsuario");
            String registrarDokusiLoginUsuario = (String) request.getParameter("registrarDokusiLoginUsuario");
            LOG.info("tipoOperacion : " + tipoOperacion);
            LOG.info("codDepPKAnotacion/codUnidOrgPKAnotacion/tipoAnotacion : " + codDepPKAnotacion + "/" + codUnidOrgPKAnotacion + "/" + tipoAnotacion);
            LOG.info("ejercicioAnotacion/numeroAnotacion : " + ejercicioAnotacion + "/" + numeroAnotacion);
            LOG.info("codProcedimiento: " + codProcedimiento);
            LOG.info("registrarTodos: " + registrarTodos);
            LOG.info("registrarDokusiOidDocumento: " + registrarDokusiOidDocumento 
                        + " registrarDokusiDniUsuario: " + registrarDokusiDniUsuario 
                        + " registrarDokusiNombreApellidosUsuario: " + registrarDokusiNombreApellidosUsuario 
                        + " registrarDokusiLoginUsuario: " + registrarDokusiLoginUsuario 
                    );
            // Leer Documentos de la entrada y por cada uno llamar a retramitar
            String[] params = usuario.getParamsCon();
            RegistroValueObject registro = new RegistroValueObject();
            registro.setIdentDepart(Integer.valueOf(codDepPKAnotacion));
            registro.setUnidadOrgan(Integer.valueOf(codUnidOrgPKAnotacion));
            registro.setTipoReg(tipoAnotacion);
            registro.setAnoReg(Integer.valueOf(ejercicioAnotacion));
            registro.setNumReg(Integer.valueOf(numeroAnotacion));
            if (registrarTodos != null && !registrarTodos.isEmpty() && registrarTodos.equalsIgnoreCase("1")) {
                List<DocumentoCatalogacionVO> listaDoc = digitalizacionDocumentosLanbideManager.getDocumentosRegistroCompleto(registro.getIdentDepart(), registro.getUnidadOrgan(), registro.getAnoReg(), registro.getNumReg(), registro.getTipoReg(), params);
                if (listaDoc != null && listaDoc.size() > 0) {
                    LOG.info("Nro. Documentos a Tratar: " + listaDoc.size());
                    for (DocumentoCatalogacionVO documento : listaDoc) {
                        LOG.info("Doc:  " + documento.getNomDocumento() + " " + documento.getIdDocGestor());
                        // Registramos
                        if (documento.getIdDocGestor() != null && !documento.getIdDocGestor().isEmpty()) {
                            if(documento.getEsDocCompulsado()==1){
                                try {
                                    digitalizacionDocumentosLanbideManager.registrarDocumentoDokusiCompulsado(Integer.valueOf(codigoOrgaEsquema), registro.getAnoReg(), registro.getNumReg(), documento.getIdDocGestor(),registrarDokusiDniUsuario,registrarDokusiNombreApellidosUsuario,registrarDokusiLoginUsuario,params);
                                    respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocRegOK"));
                                } catch (Lan6Excepcion e) {
                                    String textMess = (e.getCodes() != null ? Arrays.toString(e.getCodes().toArray()) : "")
                                            + " - " + (e.getMessages() != null ? Arrays.toString(e.getMessages().toArray()) : "")
                                            + " - " + (e.getMessage())
                                            + " - " + (e.getMensajeExcepcion());
                                    LOG.error("Error al registrar Documento: " + documento.getNomDocumento() + " - " + documento.getIdDocGestor()
                                            + " " + textMess,
                                            e);
                                    respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocRegNOOK") + textMess);
                                }
                            }else{
                                LOG.info("No se invoca a registrar Documento Dokusi para el Documento " + documento.getNomDocumento() + " " + documento.getIdDocGestor() +" - No es un documento Compulsado.");
                                respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocRegNOOK") + " " + traductorAplicacionBean.getDescripcion("msgRetramiDocNoComp"));
                            }
                        } else {
                            LOG.info("No se invoca a registrar Documento Dokusi para el Documento " + documento.getNomDocumento() + " - OID no recuperado.");
                            respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocRegNOOK") + " " + traductorAplicacionBean.getDescripcion("msgRetramiDocNoOID"));
                        }
                    }
                } else {
                    LOG.info("No se han recuperado documentos de asociados a la entrada.");
                    respuestaServicio.add(traductorAplicacionBean.getDescripcion("msgRetramiDocNoDocs"));
                }
            } else {
                // Leemos datos del documento a registrar
                DocumentoCatalogacionVO documento = digitalizacionDocumentosLanbideManager.getDocumentoRegistroByOID(registrarDokusiOidDocumento, params);
                if(documento!=null){
                    if (documento.getEsDocCompulsado() == 1) {
                        try {
                            digitalizacionDocumentosLanbideManager.registrarDocumentoDokusiCompulsado(Integer.valueOf(codigoOrgaEsquema), registro.getAnoReg(), registro.getNumReg(), documento.getIdDocGestor(), registrarDokusiDniUsuario, registrarDokusiNombreApellidosUsuario, registrarDokusiLoginUsuario, params);
                            respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocRegOK"));
                        } catch (Lan6Excepcion e) {
                            String textMess = (e.getCodes() != null ? e.getCodes().get(0) : "")
                                    + " - " + e.getMessage();
                            LOG.error("Error al registrar Documento: " + documento.getNomDocumento() + " - " + documento.getIdDocGestor()
                                    + " " + textMess,
                                    e);
                            respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocRegNOOK")+ " " + textMess);
                        }
                    } else {
                        LOG.info("No se invoca a registrar Documento Dokusi para el Documento " + documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - No es un documento Compulsado.");
                        respuestaServicio.add(documento.getNomDocumento() + " " + documento.getIdDocGestor() + " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocRegNOOK") + " " + traductorAplicacionBean.getDescripcion("msgRetramiDocNoComp"));
                    }
                }else{
                    LOG.info("No se invoca a registrar Documento Dokusi para el Documento Solicitado " + registrarDokusiOidDocumento + " - Datos del Documento no recuperados de BD.");
                    respuestaServicio.add(registrarDokusiOidDocumento+ " - " + traductorAplicacionBean.getDescripcion("msgRetramiDocRegNOOK") + " " + traductorAplicacionBean.getDescripcion("msgRetramiDocNODataBD"));
                }
            }
        } catch (Exception e) {
            LOG.error("Error tratando la operacion Registrar : " + e.getMessage(), e);
            respuestaServicio.add(traductorAplicacionBean.getDescripcion("msgRetramiDocERROROpe")+ " : " + e.getMessage());
        }
        LOG.info("respuestaServicio : " + Arrays.toString(respuestaServicio.toArray()));
        prepararResponseJSON(respuestaServicio, response);
        return null;
    }
    
    public ActionForward getDatosEntradaRegistroValidaRetramitarCambioProc(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            LOG.info("Iniciamos action de Validacion Retranmitar al Guardar.");
            ValidacionRetramitacionCambioProVO respuestaServicio = new ValidacionRetramitacionCambioProVO();
            try {
                // Validamos entrada Rellenar parametros generales
                validarRequestSessionSetUsuarioTraductor(mapping,request);
                //Recojo los parametros
                String tipoOperacion = (String) request.getParameter("tipoOperacion");
                String codigoOrgaEsquema = (String) request.getParameter("codigoOrgaEsquema");
                String codDepPKAnotacion = (String) request.getParameter("codDepPKAnotacion");
                String codUnidOrgPKAnotacion = (String) request.getParameter("codUnidOrgPKAnotacion");
                String tipoAnotacion = (String) request.getParameter("tipoAnotacion");
                String ejercicioAnotacion = (String) request.getParameter("ejercicioAnotacion");
                String numeroAnotacion = (String) request.getParameter("numeroAnotacion");
                String codProcedimiento = (String) request.getParameter("codProcedimiento");
                LOG.info("codigoOrgaEsquema : " + codigoOrgaEsquema);
                LOG.info("tipoOperacion : " + tipoOperacion);
                LOG.info("codDepPKAnotacion/codUnidOrgPKAnotacion/tipoAnotacion : " + codDepPKAnotacion + "/" + codUnidOrgPKAnotacion + "/" + tipoAnotacion);
                LOG.info("ejercicioAnotacion/numeroAnotacion : " + ejercicioAnotacion + "/" + numeroAnotacion);
                LOG.info("codProcedimiento: " + codProcedimiento);
                
                // Leer Documentos de la entrada y por cada uno llamar a retramitar
                String[] params = usuario.getParamsCon();
                respuestaServicio.setCodDepartamento(Integer.valueOf(codDepPKAnotacion));
                respuestaServicio.setCodUnidadOrganica(Integer.valueOf(codUnidOrgPKAnotacion));
                respuestaServicio.setCodTipoRegistro(tipoAnotacion);
                respuestaServicio.setEjercicioRegistro(Integer.valueOf(ejercicioAnotacion));
                respuestaServicio.setNumeroRegistro(Integer.valueOf(numeroAnotacion));
                respuestaServicio.setCodProcedimiento(codProcedimiento);
                respuestaServicio = digitalizacionDocumentosLanbideManager.getDatosEntradaRegistroValidaRetramitarCambioProc(respuestaServicio, params);
            } catch (Exception e) {
                LOG.error("Error tratando la operacion Validar retramitar por cambio de procedimiento : " + e.getMessage(), e);
                respuestaServicio.setErrorEnlaPeticion(Boolean.TRUE);
                respuestaServicio.setDetalleErrorEnlaPeticion(traductorAplicacionBean.getDescripcion("msgRetramiDocERROROpe") +  " : " + e.getMessage());
            }
            LOG.info("respuestaServicio : " + respuestaServicio.toString());
            prepararResponseJSON(respuestaServicio, response);
            return null;
        }

    private void validarRequestSessionSetUsuarioTraductor(ActionMapping mapping,HttpServletRequest request){
        if(request!=null && request.getSession()!=null
                && request.getSession().getAttribute("usuario")!=null
        ){
            // Cumplimentamos parametros generales
            usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            // Traductor Segun idioma por defecto 1=Castellano, 1=Apilcacion Registro
            traductorAplicacionBean.setApl_cod(ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA);
            traductorAplicacionBean.setIdi_cod(1);
            // Traductor Segun idioma de Usuario
            if(usuario!=null){
                traductorAplicacionBean.setIdi_cod(usuario.getIdioma());
            }else{
                log.info("usuario No recuperado en la session, no hacemos set del Idioma");
            }
        }
    }
}
