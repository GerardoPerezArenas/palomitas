package es.altia.agora.interfaces.user.web.registro.mantenimiento;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ListLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.registro.mantenimiento.MantAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.persistence.MantAsuntosManager;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.ElementoListaValueObject;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.mantenimiento.MantClasifAsuntosValueObject;
import es.altia.agora.business.registro.mantenimiento.persistence.MantClasifAsuntosManager;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.util.ArrayList;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.io.IOOperations;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.commons.io.IOUtils;





import java.util.Vector;

public class MantAsuntosAction  extends ActionSession {

    protected static Log m_Log =
            LogFactory.getLog(MantRolesAction.class.getName());
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");

    public ActionForward performSession(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response) throws IOException {

        m_Log.debug("================= MantAsuntosAction ======================>");

        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        RegistroUsuarioValueObject regUsuarioVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
        MantAsuntosForm asuntosForm = (MantAsuntosForm) form;
        MantAsuntosValueObject asuntoVO = null;
        String formName = mapping.getAttribute();

        // Parametros propios de esta accion        
        String opcion = asuntosForm.getOpcion();
        String apl    = asuntosForm.getApl();
        if (apl==null || (!apl.equals("admin") && !apl.equals("registro"))) {
            if (usuario.getAppCod() == 1) apl = "registro";
            else apl = "admin";
        }

        // Cogemos el codigo de unidad de registro del usuario para recuperar
        // los asuntos de esta unidad. En caso de que estemos en la aplicacion 
        // de admin, usamos -1 para recuperar todos.
        int codUnidadOrg;
        if (apl.equals("admin")) {
            codUnidadOrg = -1;
        } else {
            codUnidadOrg = regUsuarioVO.getUnidadOrgCod();
        }

        m_Log.debug("Opcion=" + opcion + ", apl=" + apl);
        if (apl.equals("registro")) {
            m_Log.debug("UOR del usuario: codigo=" + codUnidadOrg + " descripcion=" + regUsuarioVO.getUnidadOrg());
        }
        m_Log.debug("Usamos el form : " + formName);

        // Recuperamos los parametros de conexión                
        String[] params = usuario.getParamsCon();

        // ---- OPCION CARGAR ----        
        if (opcion.equals("cargar")){
            // Carga la página de asuntos (página con el listado de asuntos).

            Vector<MantAsuntosValueObject> asuntos =
                    MantAsuntosManager.getInstance().cargarAsuntos(codUnidadOrg,params);
            asuntosForm.setAsuntos(asuntos);

            //---- OPCION ALTA ----
        } else if(opcion.equals("alta")) {
            // Carga la página de definición de asuntos con un asunto en blanco.

            // Cargamos toda la informacion del asunto
            asuntoVO = new MantAsuntosValueObject();
            asuntosForm.setAsuntoVO(asuntoVO);

            // Recuperar listas de UORs
            Vector<UORDTO> uors = UORsManager.getInstance().getListaUORsPorNoVisRegistro('0', params);
            asuntosForm.setUors(uors);

            Vector<UORDTO> uorsDeRegistro = UORsManager.getInstance().getListaUORsDeRegistro(params);
            asuntosForm.setUorsDeRegistro(uorsDeRegistro);

            ArrayList <MantClasifAsuntosValueObject> clasificaciones =MantClasifAsuntosManager.getInstance().cargarClasifAsuntos(codUnidadOrg,params);
            asuntosForm.setClasificaciones(clasificaciones);

            asuntosForm.setCodigoClasificacion(-1);

            // Indicar unidad de registro actual en el form
            if (apl.equals("registro")) {
                asuntosForm.setUnidadRegistroActual(Integer.toString(regUsuarioVO.getUnidadOrgCod()));
            }

            //---- OPCION GRABAR_ALTA ----
        } else if(opcion.equals("grabar_alta")) {
            // Guarda el alta de un nuevo asunto.

            asuntoVO = asuntosForm.getAsuntoVO();
            int codigoClasificacion=asuntosForm.getCodigoClasificacion();

            m_Log.debug("Grabando: " + asuntoVO);
            m_Log.debug("Grabando con codigo de clasificacion: " + codigoClasificacion);


            asuntoVO.setCodigoClasificacion(codigoClasificacion);

            // #234108: guardamos en asuntoVO el valor del check de si permite un tipo de documento 0 para interesado
            String sinDocPermitido = request.getParameter("tipoDocObligatorio");
            if(sinDocPermitido!= null && sinDocPermitido.equals("1"))
                asuntoVO.setTipoDocObligatorio(true);
            else asuntoVO.setTipoDocObligatorio(false);

            String bloquearDestino=request.getParameter("bloquearDestino");
            if(bloquearDestino !=null && bloquearDestino.equals("true")){
                asuntoVO.setBloquearDestino(true);
            }else{
                asuntoVO.setBloquearDestino(false);
            }
            asuntoVO.setBloquearDestino(asuntosForm.isBloquearDestino());

            String bloquearProcedimiento=request.getParameter("bloquearProcedimiento");
            if(bloquearProcedimiento !=null && bloquearProcedimiento.equals("true")){
                asuntoVO.setBloquearProcedimiento(true);
            }else{
                asuntoVO.setBloquearProcedimiento(false);
            }
            asuntoVO.setBloquearProcedimiento(asuntosForm.isBloquearProcedimiento());

            String bloqueoPAC=request.getParameter("bloqueoPAC");
            if(bloqueoPAC !=null && bloqueoPAC.equals("true")){
                asuntoVO.setBloqueoPAC(true);
            }else{
                asuntoVO.setBloqueoPAC(false);
            }
            MantAsuntosManager.getInstance().grabarAlta(asuntoVO,params);

            // Recargamos la pagina de asuntos
            Vector<MantAsuntosValueObject> asuntos =
                    MantAsuntosManager.getInstance().cargarAsuntos(codUnidadOrg,params);
            asuntosForm.setAsuntos(asuntos);

            opcion = "cargar";

            //---- OPCION MODIFICAR ----
        } else if(opcion.equals("modificar") || opcion.equals("consultar")) {
            // Carga la página de definición de asuntos con el asunto seleccionado.

            // Cargamos toda la informacion del asunto
            asuntoVO = asuntosForm.getAsuntoVO();
            asuntoVO = MantAsuntosManager.getInstance().cargarAsunto(asuntoVO,params);
            m_Log.debug("Recuperado: " + asuntoVO);
            asuntosForm.setAsuntoVO(asuntoVO);

            // Recuperar listas de UORs
            Vector<UORDTO> uors = UORsManager.getInstance().getListaUORsPorNoVisRegistro('0', params);
            asuntosForm.setUors(uors);

            Vector<UORDTO> uorsDeRegistro = UORsManager.getInstance().getListaUORsDeRegistro(params);
            asuntosForm.setUorsDeRegistro(uorsDeRegistro);

            ArrayList <MantClasifAsuntosValueObject> clasificaciones =MantClasifAsuntosManager.getInstance().cargarClasifAsuntos(codUnidadOrg,params);
            asuntosForm.setClasificaciones(clasificaciones);

            // Indicar unidad de registro actual en el form
            if (apl.equals("registro")) {
                asuntosForm.setUnidadRegistroActual(Integer.toString(regUsuarioVO.getUnidadOrgCod()));
            }

            // Guardar valores de la clave primaria (se pueden modificar todos)
            asuntosForm.setUnidadRegistroAnterior(asuntoVO.getUnidadRegistro());
            asuntosForm.setTipoRegistroAnterior(asuntoVO.getTipoRegistro());
            asuntosForm.setCodigoAnterior(asuntoVO.getCodigo());

            // Recuperar documentos del procedimiento en caso de que haya procedimiento
            if (asuntosForm.getCodProcedimiento()!=null && !asuntosForm.getCodProcedimiento().equals("")) {
                GeneralValueObject procGVO = new GeneralValueObject();
                procGVO.setAtributo("codMunicipio", asuntosForm.getMunProc());
                procGVO.setAtributo("codProcedimiento", asuntosForm.getCodProcedimiento());

                Vector<ElementoListaValueObject> listaDocs =
                        DefinicionProcedimientosManager.getInstance().getListaDocumentos(procGVO,params);

                asuntosForm.setListaDocs(listaDocs);

            }

            asuntosForm.setCodigoClasificacion(asuntoVO.getCodigoClasificacion());
            m_Log.info("Estamos en la opcion Modificar. El codigo de Clasificacion el asunto actual es : " + asuntoVO.getCodigoClasificacion());

            // #234108
            boolean esObligTipoDoc = asuntoVO.isTipoDocObligatorio();
            if(esObligTipoDoc) asuntosForm.setTipoDocObligatorio("1");
            else asuntosForm.setTipoDocObligatorio("0");

            //---- OPCION GRABAR_MODIFICAR ----
        } else if(opcion.equals("grabar_modificar")) {
            // Guarda las modificaciones realizadas en un asunto existente.

            asuntoVO = asuntosForm.getAsuntoVO();
            int codigoClasificacion=asuntosForm.getCodigoClasificacion();

            m_Log.debug("Modificando: " + asuntoVO);
            m_Log.debug("Modificando con codigo de clasificacion: " + codigoClasificacion);
            asuntoVO.setCodigoClasificacion(codigoClasificacion);

            m_Log.debug("Grabando: " + asuntoVO);
            // Tomamos los datos guardados de la clave primaria para saber que fila
            // de la BD vamos a modificar (pueden haber sido modificados).
            MantAsuntosValueObject anteriorVO = new MantAsuntosValueObject();
            anteriorVO.setUnidadRegistro(asuntosForm.getUnidadRegistroAnterior());
            anteriorVO.setTipoRegistro(asuntosForm.getTipoRegistroAnterior());
            anteriorVO.setCodigo(asuntosForm.getCodigoAnterior());

            // #234108: guardamos en asuntoVO el valor del check de si permite un tipo de documento 0 para interesado
            String sinDocPermitido = request.getParameter("tipoDocObligatorio");
            if(sinDocPermitido!= null && sinDocPermitido.equals("1"))
                asuntoVO.setTipoDocObligatorio(true);
            else asuntoVO.setTipoDocObligatorio(false);

            String bloquearDestino=request.getParameter("bloquearDestino");
            if(bloquearDestino !=null && bloquearDestino.equals("true")){
                asuntoVO.setBloquearDestino(true);
            }else{
                asuntoVO.setBloquearDestino(false);
            }
            asuntoVO.setBloquearDestino(asuntosForm.isBloquearDestino());

            String bloquearProcedimiento=request.getParameter("bloquearProcedimiento");
            if(bloquearProcedimiento !=null && bloquearProcedimiento.equals("true")){
                asuntoVO.setBloquearProcedimiento(true);
            }else{
                asuntoVO.setBloquearProcedimiento(false);
            }
            asuntoVO.setBloquearProcedimiento(asuntosForm.isBloquearProcedimiento());

            String bloqueoPAC=request.getParameter("bloqueoPAC");
            if(bloqueoPAC !=null && bloqueoPAC.equals("true")){
                asuntoVO.setBloqueoPAC(true);
            }else{
                asuntoVO.setBloqueoPAC(false);
            }

            MantAsuntosManager.getInstance().grabarModificacion(asuntoVO, anteriorVO, params);

            // Recargamos la pagina de asuntos
            Vector<MantAsuntosValueObject> asuntos =
                    MantAsuntosManager.getInstance().cargarAsuntos(codUnidadOrg,params);
            asuntosForm.setAsuntos(asuntos);

            opcion = "cargar";

            //---- OPCION ELIMINAR ----
        } else if(opcion.equals("eliminar")) {
            // Elimina el asunto seleccionado.

            asuntoVO = ((MantAsuntosForm) form).getAsuntoVO();
            m_Log.debug("Eliminando: " + asuntoVO);
            MantAsuntosManager.getInstance().eliminarAsunto(asuntoVO,usuario.getIdUsuario(), params);

            // Recargamos la pagina de asuntos
            Vector<MantAsuntosValueObject> asuntos =
                    MantAsuntosManager.getInstance().cargarAsuntos(codUnidadOrg,params);
            asuntosForm.setAsuntos(asuntos);

            } else if(opcion.equals("altaLogica")) {
            // Elimina el asunto seleccionado.

            asuntoVO = ((MantAsuntosForm) form).getAsuntoVO();
            m_Log.debug("Eliminando: " + asuntoVO);
            MantAsuntosManager.getInstance().altaLogica(asuntoVO,usuario.getIdUsuario(), params);

            // Recargamos la pagina de asuntos
            Vector<MantAsuntosValueObject> asuntos =
                    MantAsuntosManager.getInstance().cargarAsuntos(codUnidadOrg,params);
            asuntosForm.setAsuntos(asuntos);

            //---- OPCION SALIR ----
        } else if(opcion.equals("salir")) {
            // Abandona la página de asuntos.

            if ((session.getAttribute(formName) != null))
                session.removeAttribute(formName);

            //---- OPCION LISTAPROCEDIMIENTOS ----
        } else if(opcion.equals("listaProcedimientos")) {
            // Carga la lista de procedimientos para la Unidad Tramitadora indicada.

            String uor = asuntosForm.getUnidadTram();
            try {
                Vector<DefinicionProcedimientosValueObject> listaProcedimientos;
                if ("-1".equals(uor))
                    listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientosVigentes(params);
                else
                    listaProcedimientos = TramitacionManager.getInstance().getListaProcedimientosUOR(uor, params);

                asuntosForm.setProcedimientos(listaProcedimientos);
            } catch (Exception e){
                e.printStackTrace();
            }

            //---- OPCION CARGARPROCEDIMIENTO ----
        } else if(opcion.equals("cargarProcedimiento")) {
            // Carga las listas de roles y de documentos de un procedimiento.
            GeneralValueObject procGVO = new GeneralValueObject();
            procGVO.setAtributo("codMunicipio", asuntosForm.getMunProc());
            procGVO.setAtributo("codProcedimiento", asuntosForm.getCodProcedimiento());

            Vector<ElementoListaValueObject> listaDocs =
                    DefinicionProcedimientosManager.getInstance().getListaDocumentos(procGVO,params);

            asuntosForm.setListaDocs(listaDocs);
        } else if(opcion.equals("comprobarTipoDocObligatorio")){ // opción añadida en #234108
            asuntoVO = new MantAsuntosValueObject();
            asuntoVO.setCodigo(request.getParameter("codAsunto"));
            asuntoVO.setUnidadRegistro(request.getParameter("unidadReg"));

            asuntoVO = MantAsuntosManager.getInstance().cargarAsunto(asuntoVO,params);
            m_Log.debug("Recuperado: " + asuntoVO);
            // -----------OPCION EXPORTAR EXCEL ----------------
        } else if(opcion.equals("exportarExcel")) {
            m_Log.debug("MantAsuntosAction --> ExportarExcel");

            Vector<MantAsuntosValueObject> listado = asuntosForm.getAsuntos();

            ArrayList<String> columnasInforme= new ArrayList();
            columnasInforme.add("Código");
            columnasInforme.add("Descripción");
            columnasInforme.add("Unidad Registro");
            columnasInforme.add("Tipo Registro");
            columnasInforme.add("Procedimiento");
            columnasInforme.add("Descripción Procedimiento");
            columnasInforme.add("Eliminado");

            ArrayList<Integer> anchoColumnas = new ArrayList();
            anchoColumnas.add(80);
            anchoColumnas.add(130);
            anchoColumnas.add(100);
            anchoColumnas.add(50);
            anchoColumnas.add(75);
            anchoColumnas.add(130);
            anchoColumnas.add(70);

            try {
                byte[] result=generarInformeExcel(listado, columnasInforme, anchoColumnas);

                byte[] resultGzip = compress(result);

                m_Log.debug("================> longitud archivo antes: " + result.length);
                m_Log.debug("================> longitud archivo comprimido: " + resultGzip.length);

                GeneralValueObject respuesta = new GeneralValueObject();
                String jsonResult = new Gson().toJson(resultGzip);
                m_Log.debug("================> longitud json de archivo: " + jsonResult.length());
                ArrayList<String> jsonPartes = this.dividirString(jsonResult, 450000);

                respuesta.setAtributo("fichero", jsonPartes);

                // devolvemos los datos como String en formato json
                retornarJSON(new Gson().toJson(respuesta), response);

            } catch (Exception ex) {
                Logger.getLogger(MantAsuntosAction.class.getName()).log(Level.SEVERE, null, ex);
            }

            m_Log.debug("fin --> ExportarExcel");

            // -----------OPCION DESCARGAR FICHERO EXCEL ---------------- 
        }else if(opcion.equals("descargarFichero")) {

            m_Log.debug("MantAsuntosAction --> descargarFichero");

            if(session.getAttribute("usuario")!=null){


                String[] jsonPartes = request.getParameterValues("jsonFichero");
                String jsonFichero = this.reconstruirString(jsonPartes);
                byte[] resultGzip = new Gson().fromJson(jsonFichero, byte[].class);
                byte[] result = decompress(resultGzip);
                if(result!=null && result.length>0) {

                    System.setProperty("java.awt.headless", "true");
                    Calendar hora = Calendar.getInstance();
                    SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyy_HHmmss");
                    String nombreFichero = "ListadoAsuntos_" + sf.format(hora.getTime()) + ".xls";
                    m_Log.debug("nombreFichero"+nombreFichero);
                    response.setContentType("application/vnd.ms-excel");

                    response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);

                    ServletOutputStream out = response.getOutputStream();
                    response.setContentLength(result.length);
                    BufferedOutputStream bos = new BufferedOutputStream(out);
                    bos.write(result, 0, result.length);
                    bos.flush();
                    bos.close();
                }
            }
        }

        m_Log.debug("<================= MantAsuntosAction ======================");
        return (mapping.findForward(opcion));
    }



    public byte[] generarInformeExcel(Vector datosInforme, ArrayList columnasInforme, ArrayList anchoColumnas) throws Exception{
        JasperPrint jasperPrint;
        JasperReport jasperReport;
        byte[] result = null;
        m_Log.debug("MantAsuntosAction --> generarInformeExcel()");
        try {

            FastReportBuilder fastReportBuilder = new FastReportBuilder();

            Style headerStyle = new Style();
            headerStyle.setHorizontalAlign(HorizontalAlign.LEFT);
            headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
            headerStyle.setFont(Font.ARIAL_SMALL_BOLD);
            headerStyle.setBorderBottom(Border.PEN_1_POINT());

            Style detailStyle = new Style();
            detailStyle.setHorizontalAlign(HorizontalAlign.LEFT);
            detailStyle.setVerticalAlign(VerticalAlign.MIDDLE);
            detailStyle.setFont(Font.ARIAL_SMALL);

            for(int i=0; i<columnasInforme.size(); i++){
                AbstractColumn column = ColumnBuilder.getInstance()
                        .setColumnProperty("Columna"+i, String.class.getName())
                        .setTitle(columnasInforme.get(i).toString())
                        .setWidth((Integer)anchoColumnas.get(i))
                        .setFixedWidth(Boolean.TRUE)
                        .setStyle(detailStyle).setHeaderStyle(headerStyle).build();

                fastReportBuilder.addColumn(column)
                        .setPrintColumnNames(true)
                        .setIgnorePagination(true)
                        .setDetailHeight(35)
                        .setPrintBackgroundOnOddRows(true);

            }
            fastReportBuilder.setUseFullPageWidth(true);

            //Se pasa la plantilla JasperReport (con la estructura básica) a DynamicJasper para su generación

            // Rutas de la plantilla
            StringBuilder plantillaJasper = new StringBuilder();

            plantillaJasper.append(m_Conf.getString("SIGP.webapp_base_dir"))
                    .append(File.separator)
                    .append(ConstantesDatos.RUTA_PLANTILLAS_JASPER)
                    .append(File.separator)
                    .append(ConstantesDatos.FICHERO_PLANTILLA_EXCEL_INFORMES);

            fastReportBuilder.setTemplateFile(plantillaJasper.toString());

            DynamicReport dinamicReport = fastReportBuilder.build();
            JRDataSource ds = setDatosInforme(datosInforme);

            jasperReport = DynamicJasperHelper.generateJasperReport(dinamicReport, new ListLayoutManager(),null);
            if (ds != null) {
                jasperPrint = JasperFillManager.fillReport(jasperReport, null, ds);
            } else {
                jasperPrint = JasperFillManager.fillReport(jasperReport, null);
            }

            if (jasperPrint!=null) {
                ByteArrayOutputStream outAux = new ByteArrayOutputStream();

                try{
                    JRAbstractExporter exporter = null;
                    exporter = new JRXlsExporter();

                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outAux);

                    //Parametros para la exportación a excel
                    exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
                    exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
                    exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);

                    exporter.exportReport();
                    result = (outAux.toByteArray());
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    IOOperations.closeOutputStreamSilently(outAux);
                }
            }

        } catch (JRException e) {
            m_Log.error("No se ha podido generar el archivo xls");
            e.printStackTrace();
            throw new InternalErrorException(e);
        }
        return result;
    }

    protected JRDataSource setDatosInforme(Vector<MantAsuntosValueObject> datosInforme) {
        List listaDatos = new ArrayList();

        for(int i=0; i<datosInforme.size(); i++){
            int j=0;
            Map columnas = new HashMap();
            columnas.put("Columna"+j++, datosInforme.elementAt(i).getCodigo());
            columnas.put("Columna"+j++,datosInforme.elementAt(i).getDescripcion());
            columnas.put("Columna"+j++,datosInforme.elementAt(i).getDescUor());
            columnas.put("Columna"+j++,datosInforme.elementAt(i).getTipoRegistro());
            columnas.put("Columna"+j++,datosInforme.elementAt(i).getProcedimiento());
            columnas.put("Columna"+j++,datosInforme.elementAt(i).getDesProcedimiento());
            columnas.put("Columna"+j++,datosInforme.elementAt(i).getAsuntoBaja());

            listaDatos.add(columnas);
        }
        JRDataSource ds = new JRMapCollectionDataSource(listaDatos);
        return ds;

    }


    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json, HttpServletResponse response) {

        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Divide un String en substrings, cada uno de longitud tamano, excepto el último que será de menor tamaño
     * @param cadena String a dividir
     * @param tamano Int que indica la longitud máxima de cada substring
     * @return List que contiene todos los substrings
     */
    private ArrayList<String> dividirString(String cadena, int tamano){
        ArrayList<String> partes = new ArrayList<String>();
        while(cadena.length()>tamano){
            String parte = cadena.substring(0,tamano);
            partes.add(parte);
            cadena = cadena.substring(tamano);
        }
        partes.add(cadena);
        return partes;
    }

    /**
     * Reconstruye un String uniendo los Strings que son elementos del array pasado
     * @param cadenas Array con los String a unir
     * @return String resultante
     */
    private String reconstruirString(String[] cadenas){
        String todo = "";
        for(int i=0; i<cadenas.length; i++){
            todo += cadenas[i];
        }

        return todo;
    }

    private byte[] compress(byte[] content){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(content);
            gzipOutputStream.close();
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decompress(byte[] contentBytes){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
            IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out);
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

}
