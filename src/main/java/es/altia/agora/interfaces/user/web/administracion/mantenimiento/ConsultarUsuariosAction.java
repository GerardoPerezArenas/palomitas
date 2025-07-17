package es.altia.agora.interfaces.user.web.administracion.mantenimiento;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.administracion.mantenimiento.persistence.AplicacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.conexion.BDException;
import es.altia.util.io.IOOperations;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporter;	
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

public class ConsultarUsuariosAction extends DispatchAction{
    private Logger log = Logger.getLogger(ConsultarUsuariosAction.class);
    private TraductorAplicacionBean traductor = new TraductorAplicacionBean();
    
    public ActionForward cargarPantallaConsulta(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.debug("================= ConsultarUsuariosAction : cargarPantallaConsulta() ======================>");
        
        HttpSession session = request.getSession();
        ArrayList<GeneralValueObject> lista = null;
        String forward = null;
        
        if(session.getAttribute("usuario")!=null){
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            
            lista = new ArrayList<GeneralValueObject>(OrganizacionesManager.getInstance().getListaOrganizaciones(params));
            request.setAttribute("ListaOrganizaciones", lista);
            forward = "cargarPantallaConsulta";
        } else forward = "caducaSession";
        
        return mapping.findForward(forward);
    }
    
    public ActionForward cargarInfoAccesoUsuarios(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.debug("================= ConsultarUsuariosAction : cargarInfoAccesoUsuarios() ======================>");
        
        HttpSession session = request.getSession();
        String forward = null;
        
        if(session.getAttribute("usuario")!=null){
            ArrayList<GeneralValueObject> listaOrganizaciones = null;
            ArrayList<GeneralValueObject> listaAplicaciones = null;
            ArrayList<GeneralValueObject> listaAplicacionesCompleta = new ArrayList<GeneralValueObject>();

            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            
            listaOrganizaciones = new ArrayList<GeneralValueObject>(OrganizacionesManager.getInstance().getListaOrganizaciones(params));
            listaAplicaciones = new ArrayList<GeneralValueObject>(AplicacionesManager.getInstance().getListaAplicaciones(params));

            // Anadimos el login a la lista de aplicaciones para poder realizar la busqueda por el mismo
            GeneralValueObject login = new GeneralValueObject();
            login.setAtributo("codigo", ConstantesDatos.CODIGO_ACCESO_FLEXIA);
            login.setAtributo("descripcion", ConstantesDatos.DESCRIPCION_ACCESO_FLEXIA);
            listaAplicacionesCompleta.add(login);
            listaAplicacionesCompleta.addAll(listaAplicaciones);
            
            String listaOrganizacionJSON = new GsonBuilder().serializeNulls().create().toJson(listaOrganizaciones);
            String listaAplicacionesJSON = new GsonBuilder().serializeNulls().create().toJson(listaAplicacionesCompleta);

            request.setAttribute("ListaOrganizaciones", listaOrganizacionJSON);
            request.setAttribute("ListaAplicaciones", listaAplicacionesJSON);
            forward = "cargarInfoAccesoUsuarios";
        } else forward = "caducaSession";
        
        return mapping.findForward(forward);
    }
            
    public ActionForward recuperarUORsPorOrg(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.debug("================= ConsultarUsuariosAction : recuperarUORsPorOrg() ======================>");
        
        HttpSession session = request.getSession();
        ArrayList<UORDTO> listaAux = null;
        ArrayList<UORDTO> lista = new ArrayList<UORDTO>();;
        
        if(session.getAttribute("usuario")!=null){
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            
            String codOrgSel = request.getParameter("codOrg");
            String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                        "1", codOrgSel, String.valueOf(usuario.getAppCod()), params);
             
            String[] paramsNuevos = new String[7];
            paramsNuevos[0] = params[0];
            paramsNuevos[6] = jndi;            
            for(int i=0;i<paramsNuevos.length;i++)
                log.debug("paramsNuevos["+i+"] = "+paramsNuevos[i]);
            
            
            listaAux = new ArrayList<UORDTO>(UORsDAO.getInstance().getListaUORs(false,paramsNuevos));
            UORDTO todasUOR = new UORDTO();
            todasUOR.setUor_cod("TODAS");
            todasUOR.setUor_cod_vis("TODAS");
            todasUOR.setUor_nom("TODAS");
            lista.add(todasUOR);
            lista.addAll(listaAux);
            // devolvemos los datos como String en formato json
            retornarJSON(new Gson().toJson(lista), response);
        }
        
        return null;
    }
    
    public ActionForward recuperarProcsPorOrg(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.debug("================= ConsultarUsuariosAction : recuperarProcsPorOrg() ======================>");
        
        HttpSession session = request.getSession();
        ArrayList<DefinicionProcedimientosValueObject> listaAux = null;
        ArrayList<DefinicionProcedimientosValueObject> lista = new ArrayList<DefinicionProcedimientosValueObject>();;
        
        if(session.getAttribute("usuario")!=null){
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            
            String codOrgSel = request.getParameter("codOrg");
            String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                        "1", codOrgSel, String.valueOf(usuario.getAppCod()), params);
             
            String[] paramsNuevos = new String[7];
            paramsNuevos[0] = params[0];
            paramsNuevos[6] = jndi;            
            for(int i=0;i<paramsNuevos.length;i++)
                log.debug("paramsNuevos["+i+"] = "+paramsNuevos[i]);
            
            
            listaAux = new ArrayList<DefinicionProcedimientosValueObject>(TramitacionDAO.getInstance().getListaProcedimientosVigentes(paramsNuevos));
            DefinicionProcedimientosValueObject todosPr = new DefinicionProcedimientosValueObject();
            todosPr.setCodMunicipio(codOrgSel);
            todosPr.setTxtCodigo("TODOS");
            todosPr.setTxtDescripcion("TODOS");
            lista.add(todosPr);
            lista.addAll(listaAux);
            // devolvemos los datos como String en formato json
            retornarJSON(new Gson().toJson(lista), response);
        }
        
        return null;
    }
    
    /**
     * Método que responde a una petición ajax, se recuperan los usuarios por cada unidad orgánica o por cada procedimiento, depende del panel activo (se indica en los parámetros de la petición)
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return Un string en formato json con: la lista de usuarios si la petición busca mostrarlos por pantalla; o con el array de bytes que se corresponde
     * con el archivo que se desea imprimir si viene indicado en la petición; o con el código de error si se ha producido alguno. Los posibles valores de dicho código son:
     * 1: error al establecer la conexión a la BBDD. 2: Error al recuperar el listado de usuarios. 3: Error en la generación del informe.
     * @throws Exception 
     */
    public ActionForward recuperarUsuariosFiltrados(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.debug("================= ConsultarUsuariosAction : recuperarUsuariosFiltrados() ======================>");
        
        HttpSession session = request.getSession();
        ArrayList<GeneralValueObject> lista = null;
        String mensajeError = null;
        
        if(session.getAttribute("usuario")!=null){
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            traductor.setIdi_cod(usuario.getIdioma());
            traductor.setApl_cod(usuario.getAppCod());
            
            String codOrgSel = request.getParameter("codOrg");
            String codUORSel = request.getParameter("codUOR");
            String codProcSel = request.getParameter("codProc");
            String imprimir = request.getParameter("imprimir");
            String panel = request.getParameter("panel");
                        
            String jndi = UsuariosGruposManager.getInstance().obtenerJNDI(
                        "1", codOrgSel, String.valueOf(usuario.getAppCod()), params);
             
            String[] paramsNuevos = new String[7];
            paramsNuevos[0] = params[0];
            paramsNuevos[6] = jndi;            
            for(int i=0;i<paramsNuevos.length;i++)
                log.debug("paramsNuevos["+i+"] = "+paramsNuevos[i]);
            
            try{
                lista = UsuarioManager.getInstance().getListaUsuariosFiltrados(codOrgSel, codProcSel, codUORSel, paramsNuevos);
                if(lista!=null){
                    if(imprimir!=null && imprimir.equals("si")){
                        String elemento;
                        if(panel.equals("UsusUORs")) elemento = traductor.getDescripcion("gEtiq_unidOrg");
                        else elemento = traductor.getDescripcion("gb_etiqProcedimiento");

                        HashMap<String,Object> generalMap = getHashMapDatosReporte(lista);
                        generalMap.put("txtElemento", elemento);
                        generalMap.put("txtUsuarios", traductor.getDescripcion("tit_usuar"));
                        generalMap.put("tituloPag", traductor.getDescripcion("etiqListaUsusTitInf"));
                        byte[] result = this.generarJasperReport("report_listaUsuarios.jasper", generalMap);

                        byte[] resultGzip = compress(result);

                        log.debug("================> longitud archivo antes: " + result.length);
                        log.debug("================> longitud archivo comprimido: " + resultGzip.length);

                        GeneralValueObject respuesta = new GeneralValueObject();
                        String jsonResult = new Gson().toJson(resultGzip);
                        log.debug("================> longitud json de archivo: " + jsonResult.length());
                        ArrayList<String> jsonPartes = this.dividirString(jsonResult, 450000);
                        
                        respuesta.setAtributo("fichero", jsonPartes);
                        respuesta.setAtributo("panel", panel);
                        // devolvemos los datos como String en formato json
                        retornarJSON(new Gson().toJson(respuesta), response);
                    } else {
                        // devolvemos los datos como String en formato json
                        retornarJSON(new Gson().toJson(lista), response);
                    }
                }
            } catch (BDException bde){
                mensajeError = traductor.getDescripcion("msgErrConexionBD");
                throw new Exception(mensajeError);
            } catch (SQLException sqle){
                mensajeError = traductor.getDescripcion("msjErrPropBusq");
                throw new Exception(mensajeError);
            } catch (Exception ex){
                mensajeError = traductor.getDescripcion("msgErrGenerarInf");
                throw new Exception(mensajeError);
            } finally {
                if(mensajeError!=null){
                    GeneralValueObject objError = new GeneralValueObject();
                    objError.setAtributo("error", mensajeError);
                    // devolvemos el codigo del error producido como String en formato json
                    retornarJSON(new Gson().toJson(objError), response);
                }
            }
        }
        
        return null;
    }
    
    public ActionForward descargarFichero(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.debug("================= ConsultarUsuariosAction : descargarFichero() ======================>");
        
        HttpSession session = request.getSession();
        ArrayList<GeneralValueObject> lista = null;
        
        if(session.getAttribute("usuario")!=null){
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();
            
            String[] jsonPartes = request.getParameterValues("jsonFichero");
            String jsonFichero = this.reconstruirString(jsonPartes);
            byte[] resultGzip = new Gson().fromJson(jsonFichero, byte[].class);
            byte[] result = decompress(resultGzip);
            if(result!=null && result.length>0) {

                System.setProperty("java.awt.headless", "true");
                Calendar hora = Calendar.getInstance();
                SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyy_HHmmss");
                String nombreFichero = "USUARIOS_" + sf.format(hora.getTime()) + ".xls";
                response.setContentType("application/vnd.ms-excel");                    
                //response.setContentType("text/csv");                    
                response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);
                //response.setHeader("Content-Transfer-Encoding", "binary");            

                ServletOutputStream out = response.getOutputStream();
                response.setContentLength(result.length);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                bos.write(result, 0, result.length);
                bos.flush();
                bos.close();
            }
        }
        
        return null;
    }
    
    private HashMap<String,Object> getHashMapDatosReporte(ArrayList<GeneralValueObject> datos){
        HashMap<String,Object> generalMap = new HashMap<String, Object>();
        ArrayList<HashMap<String,Object>> listaDatosMap = new ArrayList<HashMap<String,Object>>();
        for(GeneralValueObject gVO : datos){
            HashMap<String,Object> datosMap = new HashMap<String,Object>();
            String nombre = (String) gVO.getAtributo("elemento");
            datosMap.put("descElemento", nombre);
            
            ArrayList<String> listaUsuarios = (ArrayList<String>) gVO.getAtributo("usuarios");
            ArrayList<HashMap<String,Object>> listaHashMapUsus = new ArrayList<HashMap<String,Object>>();
            for(String usuario : listaUsuarios){
                HashMap<String,Object> usuMap = new HashMap<String,Object>();
                usuMap.put("descUsuario", usuario);
                listaHashMapUsus.add(usuMap);
            }
            datosMap.put("listaUsuarios", new JRBeanCollectionDataSource(listaHashMapUsus));
            listaDatosMap.add(datosMap);
        }
        generalMap.put("datosUsuarios",  new JRBeanCollectionDataSource(listaDatosMap));
        
        return generalMap;
    }
    
    private byte[] generarJasperReport(String reportName,HashMap<String,Object> datos) {
        JasperPrint jasperPrint = null;
        byte[] result = null;
        try {
            String rutaAbsolutaReporte  = this.getServlet().getServletContext().getRealPath("")+"//pdf//jasper//";
            String rutaAbsolutaReportName  = this.getServlet().getServletContext().getRealPath("//pdf//jasper//"+reportName);
            datos.put("SUBREPORT_DIR",rutaAbsolutaReporte);
            datos.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(rutaAbsolutaReportName);
            jasperPrint = JasperFillManager.fillReport(jasperReport, datos,new JREmptyDataSource());
            if (jasperPrint!=null) {
                ByteArrayOutputStream outAux = new ByteArrayOutputStream();

                try{
                    JExcelApiExporter exporter = new JExcelApiExporter();	
                    exporter.setParameter(JExcelApiExporterParameter.JASPER_PRINT, jasperPrint);	
                    exporter.setParameter(JExcelApiExporterParameter.OUTPUT_STREAM, (outAux));
                     
                    exporter.exportReport();
                    result = (outAux.toByteArray());
                } catch(Exception e) {
                     e.printStackTrace();
                } finally {
                    IOOperations.closeOutputStreamSilently(outAux);
                }
            }
        } catch (JRException ex) {
            ex.printStackTrace();
        }
        return result;
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
