package es.altia.agora.interfaces.user.web.registro;

//PAQUETES IMPORTADOS
import es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.registro.AnotacionRegistroVO;
import es.altia.agora.business.registro.persistence.InformesManager;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.AuditoriaManager;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.util.*;
import es.altia.agora.interfaces.user.web.util.*;
import es.altia.common.service.config.*;
import es.altia.util.sqlxmlpdf.GeneralPDF;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.NumeroALetra; 
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.auditoria.ConstantesAuditoria;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.exceptions.InternalErrorException;
import es.altia.util.struts.StrutsUtilOperations;
import org.apache.commons.lang.StringEscapeUtils;
import java.io.*;

import java.util.*;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 *
 * @author Daniel Toril Cabrera
 * @version 1.0
 */
public class InformesRegistroAction extends ActionSession {

    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");
    protected static Config conf = ConfigServiceHelper.getConfig("techserver");
    protected static Config confReg = ConfigServiceHelper.getConfig("Registro");
    private String[] params;
    private UsuarioValueObject usuario;
    private RegistroUsuarioValueObject usuarioR;
    private GeneralValueObject consultaVO = new GeneralValueObject();
    private HttpServletRequest request;
    private InformesManager infMan;
    private String pdf_dir;
    private String pdfFile;
    private String idioma;
    private int numAsientos;
    private int maxAsientos;
    private String fallo = "";
    private String opcionint;
    private static TraductorAplicacionBean descriptor = new TraductorAplicacionBean();
    
    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        m_Log.debug("=================== InformesRegistroAction ================>");
        
        pdf_dir = m_Conf.getString("PDF.base_dir");
        HttpSession session = request.getSession();
        String opcion = request.getParameter("opcion");
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("Opcion: " + opcion);
        }
        this.request = request;

        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        usuarioR = (RegistroUsuarioValueObject) session.getAttribute("registroUsuario");
        String codUnidad = "";
        if (request.getParameter("txtCodigoUnidad") != null) {
            codUnidad = request.getParameter("txtCodigoUnidad");
        }
        m_Log.debug("La unidad tramitadora elegida es: " + codUnidad);
        params = usuario.getParamsCon();
        infMan = InformesManager.getInstance();
        pdfFile = request.getParameter("pdfFile");
        String tipoVentana = request.getParameter("tipoVentana");
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("Tipo de Ventana=" + tipoVentana);
        }

        // Si el formulario dispone del campo idioma, utilizamos ese, sino
        // utilizamos el idioma del usuario.
        idioma = request.getParameter("idioma");
        if ((idioma == null) || (idioma.equals(""))) {
            idioma = new Integer(usuario.getIdioma()).toString();
        }
        
        //opciones de impresión para interesado
        if ((opcion.equals("informeLibroRegistro"))||(opcion.equals("relacionUnidadesOrganicas")) || (opcion.equals("relacionUnidadesOrganicasExternas"))
                || opcion.equals("informeBuscarLibroRegistro")){
            opcionint= request.getParameter("interopcion");
            if (opcionint.equals("op1")) opcionint="CORTO";
            else if(opcionint.equals("op2")) opcionint="MEDIO";
            else if (opcionint.equals("op3")) opcionint="LARGO";
        }
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("el idioma es ***************************** " + idioma);
        }
        String nombre = "";
        String claveIdioma="";
        claveIdioma=infMan.getClaveIdioma(params,idioma);
m_Log.debug("el claveIdioma es ***************************** " + claveIdioma);
m_Log.debug("el codUnidad es ***************************** " + codUnidad);
        try {
            if ("informeEstadisticas".endsWith(opcion)) {
                InformesRegistroForm iForm = null;
                if (form == null) {
                    form = new InformesRegistroForm();
                    if ("request".equals(mapping.getScope())) {
                        request.setAttribute(mapping.getAttribute(), form);
                    } else {
                        session.setAttribute(mapping.getAttribute(), form);
                    }
                }
                iForm = (InformesRegistroForm) form;
                iForm.setEstadisticas(InformesManager.getInstance().getEstadisticas(params, usuario));
            } else {
                if ("informeLibroRegistro".equals(opcion)) {
                    nombre = informeLibroRegistro(codUnidad,claveIdioma,params);
                } else if("informeBuscarLibroRegistro".equals(opcion)){
                    
                    InformesRegistroForm iForm = null;
                    if (form == null) {
                        form = new InformesRegistroForm();
                        if ("request".equals(mapping.getScope())) {
                            request.setAttribute(mapping.getAttribute(), form);
                        } else {
                            session.setAttribute(mapping.getAttribute(), form);
                        }
                    }
                    iForm = (InformesRegistroForm) form;
                    Vector listadoAnotaciones=informeBuscarLibroRegistro(codUnidad, claveIdioma, params);
                    iForm.setAnotacionesLibro(listadoAnotaciones);
                } else if ("relacionUnidadesOrganicas".equals(opcion)) {
                    nombre = relacionUnidadesOrganicas(claveIdioma);
                } else if ("relacionUnidadesOrganicasExternas".equals(opcion)) {    // Informe para unidades de organizaciones externas
                    nombre = relacionUnidadesOrganicasExternas(claveIdioma);
                } else if ("informeDiligencias".equals(opcion)) {
                    nombre = informeDiligencias(params,claveIdioma);
                } else if ("informeDocumento".equals(opcion)) {
                    nombre = informeDocumento(params);
                 } else if ("imprimirListadoAsientos".equals(opcion) || "exportarCSV".equals(opcion)) {
 
                    String tipoOrden = request.getParameter("tipoOrden");
                    String columna = request.getParameter("columna");
                    RegistroUsuarioValueObject regUsuarioVO = (RegistroUsuarioValueObject) session.getAttribute("registroUsuario");	
                    String lista = request.getParameter("listaAnotaciones");
                    nombre = listadoAsientos(opcion,regUsuarioVO.getUnidadOrgCod(),regUsuarioVO.getDepCod(),lista,tipoOrden,Integer.parseInt(columna),params);
                } else if("exportarBuzonEntradaCSV".equals(opcion)){
                    UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");	
                    String lista = request.getParameter("listaAnotacionesBuzon");
                    nombre= confReg.getString("INTEGRACION_TECNICO_REFERENCIA").equals("SI") ? listadoAsientosBuzonTecnicoReferencia(opcion,usuarioVO.getOrgCod(),usuarioVO.getDepCod(),lista, params) : listadoAsientosBuzon(opcion,usuarioVO.getOrgCod(),usuarioVO.getDepCod(),lista, params);
                }
                request.setAttribute("nombre", nombre);
                request.setAttribute("tipoVentana", tipoVentana);               
                request.setAttribute("fallo", fallo);
                request.setAttribute("numAsientos", numAsientos);
                request.setAttribute("maxAsientos", maxAsientos);

            }
        } catch (InternalErrorException iee) {
            m_Log.error(iee.getMessage());
        }
        m_Log.debug("<================== InformesRegistroAction =================");

        return (mapping.findForward(opcion));
    }
    
    /**
     * Función encargada de recuperar el listado de anotaciones del libro de registro
     * que se mostrar en tabla
     * 
     */
    private Vector informeBuscarLibroRegistro(String unidad, String claveIdioma, String[] params){
        m_Log.info("Buscar Impresión del Libro de Registro - BEGIN");

        String desde = request.getParameter("regDesde");
        String hasta = request.getParameter("regHasta");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String ejercicio = fechaInicio.substring(6);
        boolean orden = ((request.getParameter("orden") != null) && (request.getParameter("orden").equals("1")));
        consultaVO.setAtributo("tipo", request.getParameter("tipo"));
        consultaVO.setAtributo("fechaInicio", request.getParameter("fechaInicio"));
        consultaVO.setAtributo("fechaFin", request.getParameter("fechaFin"));
        consultaVO.setAtributo("regInicio", request.getParameter("regDesde"));
        consultaVO.setAtributo("regFinal", request.getParameter("regHasta"));
        consultaVO.setAtributo("unidadOrg",
                String.valueOf(usuarioR.getUnidadOrgCod()));
        consultaVO.setAtributo("depto", String.valueOf(usuarioR.getDepCod()));
        consultaVO.setAtributo("numDesde", request.getParameter("numDesde"));
        consultaVO.setAtributo("opcionint", request.getParameter("interopcion"));
        Vector<RegistroValueObject> listado = new Vector<RegistroValueObject>();
         String sql="";
         if ((desde != null) && (!desde.equals(""))) {
            if (desde.compareTo(hasta) <= 0) {
                m_Log.debug("ENTRO aqui--> compareTo" + unidad);
                sql=consulta(0, unidad, desde, hasta, orden, ejercicio, false, null);   
           }
            
        }else{
             sql=consulta_fecha(0, unidad, fechaInicio, fechaFin, orden, ejercicio, false, null);
         }
         try{
             listado=InformesManager.getInstance().getListadoInformeRegistro(sql, params);
         }catch(Exception e ){
             e.printStackTrace();
         }
        return listado;   
     }
    
    /**
     * Función encargada de realizar el imforme del libro de registro
     *
     * @return Un String con la ruta del pdf generado
     */
    private String informeLibroRegistro(String unidad,String claveIdioma,String[] params) {
        m_Log.info("Impresión del Libro de Registro - BEGIN");

        List<AnotacionRegistroVO> listaNumAnotacionImpresas = new ArrayList<AnotacionRegistroVO>();
        String desde = request.getParameter("regDesde");
        String hasta = request.getParameter("regHasta");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String ejercicio = fechaInicio.substring(6);
        String elementosImprimir=request.getParameter("elementosImprimir");
        boolean orden = ((request.getParameter("orden") != null) && (request.getParameter("orden").equals("1")));
        consultaVO.setAtributo("tipo", request.getParameter("tipo"));
        consultaVO.setAtributo("fechaInicio", request.getParameter("fechaInicio"));
        consultaVO.setAtributo("fechaFin", request.getParameter("fechaFin"));
        consultaVO.setAtributo("regInicio", request.getParameter("regDesde"));
        consultaVO.setAtributo("regFinal", request.getParameter("regHasta"));
        consultaVO.setAtributo("unidadOrg",
                String.valueOf(usuarioR.getUnidadOrgCod()));
        consultaVO.setAtributo("depto", String.valueOf(usuarioR.getDepCod()));
        consultaVO.setAtributo("numDesde", request.getParameter("numDesde"));
        consultaVO.setAtributo("opcionint", request.getParameter("interopcion"));
        
        Vector ficheros = new Vector();
        GeneralPDF pdf;
        try {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("opcionint",request.getParameter("interopcion")) ;
        gVO.setAtributo("baseDir", pdf_dir);
        // PDFS NUEVA SITUACION
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("usuDir", usuario.getDtr());
        // PDFS NUEVA SITUACION
        gVO.setAtributo("pdfFile", "registro");
        // FIN PDFS NUEVA SITUACION

        gVO.setAtributo("tamCabecera", "20mm");
        gVO.setAtributo("tamPie", "32mm");
        gVO.setAtributo("estilo", "css/informesRegistro.css");
        gVO.setAtributo("pagina", request.getParameter("pagDesde"));
        gVO.setAtributo("cabecera", encabezado(1, claveIdioma));
        pdf = new GeneralPDF(params, gVO);
        
        String xsl = "";
        if (request.getParameter("tipo").equalsIgnoreCase("E")) {
            xsl="libroE_"+claveIdioma;
        } else if (request.getParameter("tipo").equalsIgnoreCase("S")) {
             xsl="libroS_"+claveIdioma;
        }

        File file;

        if ((desde != null) && (!desde.equals(""))) {
            if (desde.compareTo(hasta) <= 0) {
                m_Log.debug("ENTRO aqui--> compareTo" + unidad);
                if(elementosImprimir!=null && elementosImprimir!="" && elementosImprimir.length()>0){
                    file = pdf.construyeTabla(consulta2(consulta(0, unidad, desde, hasta, orden, ejercicio, false, null),elementosImprimir), xsl,listaNumAnotacionImpresas);
                }else{
                    file = pdf.construyeTabla(consulta(0, unidad, desde, hasta, orden, ejercicio, false, null), xsl,listaNumAnotacionImpresas);
                }
                if (file != null) {
                    ficheros.add(file);
                }//Añado el dia del libro
                if(elementosImprimir!=null && elementosImprimir !="" && elementosImprimir.length()>0){
                    ficheros.add(pdf.construyeTabla(consulta2(consulta(1, unidad, desde, hasta, orden, ejercicio, false, null),elementosImprimir), "diligencias_"+claveIdioma));//añado la diligencia
                }else{
                    ficheros.add(pdf.construyeTabla(consulta(1, unidad, desde, hasta, orden, ejercicio, false, null), "diligencias_"+claveIdioma));//añado la diligencia
                }
                  if (m_Conf.getString("PDF.pie_registro").equals("si")) {
                        Hashtable tabla = new Hashtable();
                        ficheros.add(pdf.construyeTablaVacia2("pieHoja3_"+claveIdioma, tabla));

                    }
            }
        } else {
            m_Log.debug("ENTRO AQUÍ--> compareTo" + gVO.getSize());
           // file = pdf.construyeTabla(consulta_fecha(0, unidad, fechaInicio, fechaFin, orden, ejercicio, false, null), xsl,listaNumAnotacionImpresas);
            if(elementosImprimir!=null && elementosImprimir!="" && elementosImprimir.length()>0 ){
                    file = pdf.construyeTabla(consulta_fecha2(consulta_fecha(0, unidad, desde, hasta, orden, ejercicio, false, null),elementosImprimir), xsl,listaNumAnotacionImpresas);
                }else{
                    file = pdf.construyeTabla(consulta_fecha(0, unidad, fechaInicio, fechaFin, orden, ejercicio, false, null), xsl,listaNumAnotacionImpresas);
                }
            if (file != null) {
                ficheros.add(file);
            }
            
             ficheros.add(pdf.construyeTabla(consulta_fecha(1, unidad, fechaInicio, fechaFin, orden, ejercicio, false, null), "diligencias_"+claveIdioma));//añado la diligencia
                if (m_Conf.getString("PDF.pie_registro").equals("si")) {
                    Hashtable tabla = new Hashtable();
                    ficheros.add(pdf.construyeTablaVacia2("pieHoja3_"+claveIdioma, tabla));

                }
            
        }
        } catch (TechnicalException te) {
            te.printStackTrace();
            fallo = "errorTecnico";
            return "";
        }

        if (ficheros.isEmpty()) {
            //si la ejecución finaliza antes por no haber registros
            m_Log.info("Impresión del Libro de Registro - END");
            return "NO EXISTE";
        }

        // Se comprueba el numero de asientos recuperados y se estima si es suficiente la
        // memoria disponible en la máquina virtual.
        numAsientos = pdf.getNumFilas();
        m_Log.debug("numAsientos : " + numAsientos);
        maxAsientos = estimacionMaximoAsientos();
        fallo = "";
        if (numAsientos > maxAsientos) {
            fallo = "maxAsientos";
            return "";
        }

        // De paso, comprobamos si no se ha recuperado ninguna fila.
        if (numAsientos == 0) {
            fallo = "noAsientos";
            //si la ejecución finaliza antes por no haber registros
            m_Log.info("Impresión del Libro de Registro - END");
            return "";
        }

        String pathResultado = pdf.getPdf(ficheros);
        String portada = request.getParameter("portada");
        if (portada != null) {
            int numPaginas = pdf.getNumeroPaginas(pathResultado);
            if (numPaginas > 0) {
                NumeroALetra transformadorNumero = new NumeroALetra();
                String descNumero = numPaginas +"";
                m_Log.debug(descNumero);
                String pdfPortada = imprimirPortada(request.getParameter("tipo"), descNumero, claveIdioma);
                pathResultado = pdf.anhadePortada(pdfPortada, pathResultado);
            }
        }
        
        // Auditoria de acceso al registro
        try {
            AuditoriaManager.getInstance().auditarAccesoRegistro(
                    ConstantesAuditoria.REGISTRO_LIBRO_INFORME,
                    usuario,
                    (String) consultaVO.getAtributo("depto"),
                    (String) consultaVO.getAtributo("unidadOrg"),
                    (String) consultaVO.getAtributo("tipo"),
                    ejercicio,
                    listaNumAnotacionImpresas);
        } catch (AnotacionRegistroException are) {
            m_Log.error("No se pudo registrar el evento de auditoria", are);
            fallo = "errorTecnico";
            return "";
        }
        
        //si la ejecución finaliza antes por no haber registros
        m_Log.info("Impresión del Libro de Registro - END");
        
        return pathResultado;
    }

    /**
     * Función encargada de realizar el informe de unidades orgánicas
     *
     * @return Un String con la ruta del pdf generado
     */
    private String relacionUnidadesOrganicas(String claveIdioma) {
        List<AnotacionRegistroVO> listaNumAnotacionImpresas = new ArrayList<AnotacionRegistroVO>();
        String tipoConsulta  =  "";
        if(request.getParameter("tipo").equals("E")) tipoConsulta = "de Entrada" ;
        else if(request.getParameter("tipo").equals("S")) tipoConsulta = "de Salida" ;
        m_Log.info("Impresión del Registro "+tipoConsulta+" por Unidad Orgánica - BEGIN");
        
        numAsientos = 0;
        Vector unidades;
        Vector ficheros = new Vector();
        GeneralPDF pdf = null;
        String codUnidad = "";
        int numDias = 0;
        String desde = request.getParameter("regDesde");
        String hasta = request.getParameter("regHasta");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String ejercicio = fechaInicio.substring(6);
        boolean orden = ((request.getParameter("orden") != null) && (request.getParameter("orden").equals("1")));

        if (request.getParameter("txtCodigoUnidad") != null) {
            codUnidad = request.getParameter("txtCodigoUnidad");
        }

        consultaVO.setAtributo("tipo", request.getParameter("tipo"));
        consultaVO.setAtributo("fechaInicio", request.getParameter("fechaInicio"));
        consultaVO.setAtributo("regInicio", request.getParameter("regDesde"));
        consultaVO.setAtributo("regFinal", request.getParameter("regHasta"));
        consultaVO.setAtributo("fechaFin", request.getParameter("fechaFin"));
        consultaVO.setAtributo("unidadOrg", String.valueOf(usuarioR.getUnidadOrgCod()));
        consultaVO.setAtributo("depto", String.valueOf(usuarioR.getDepCod()));
        consultaVO.setAtributo("departamento", request.getParameter("txtCodigoDpto"));
        
        if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuario.getIdUsuario(), params)) {                
             consultaVO.setAtributo("directivaUsuPermisoUor", "SI");    
             consultaVO.setAtributo("codigoUsuario", usuario.getIdUsuario());
             consultaVO.setAtributo("organizacionUsuario", usuario.getOrgCod());
             consultaVO.setAtributo("entidadUsuario", usuario.getEntCod());
        }else 
        {
             consultaVO.setAtributo("directivaUsuPermisoUor", "NO");    
             consultaVO.setAtributo("codigoUsuario",999);
             consultaVO.setAtributo("organizacionUsuario", 999);
             consultaVO.setAtributo("entidadUsuario", 999);
        }   
        
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        // PDFS NUEVA SITUACION
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("usuDir", usuario.getDtr());
        // PDFS NUEVA SITUACION
        gVO.setAtributo("pdfFile", "registro");
        // FIN PDFS NUEVA SITUACION

        //gVO.setAtributo("pdfFile",pdfFile);
        gVO.setAtributo("tamCabecera", "20mm");
        gVO.setAtributo("tamPie", "32mm");
        gVO.setAtributo("estilo", "css/informesRegistro.css");
        gVO.setAtributo("pagina", "1");
        gVO.setAtributo("opcionint",request.getParameter("interopcion")) ;
        
        unidades = new Vector();
        if (codUnidad.equals("0")) {
            unidades = infMan.getTodasUnidades(params, consultaVO);
        } else {
            unidades.add(infMan.getUORPorCodigoVisual(params, String.valueOf(codUnidad)));
        }

        if (unidades.isEmpty()) {
            return "NO EXISTE";
        } else {
            for (int numUnidades = 0; numUnidades < unidades.size(); numUnidades++) {
                consultaVO.setAtributo("unidad", unidades.get(numUnidades));
                pdf = new GeneralPDF(params, gVO);
                String pieFile;

                    pieFile = pdf.construyeTablaVacia("pieNumPagina_"+claveIdioma).getPath();
      
                pdf.setPie(pieFile);

                m_Log.debug(".......................Se procesa la unidad " + unidades.get(numUnidades));
                
                String xsl = "";
                m_Log.debug("tipo de documento: " + request.getParameter("tipo"));

                if (request.getParameter("tipo").equalsIgnoreCase("E")) {
                   // if ("1".equals(idioma)) {
                        xsl = "librocE_"+claveIdioma;
                    /*} else {
                        xsl = "librocG";
                    }*/
                } else if (request.getParameter("tipo").equalsIgnoreCase("S")) {
                    //if ("1".equals(idioma)) {
                        xsl = "librocS_"+claveIdioma;
                   /* } else {
                        xsl = "librocSG";
                    }*/
                }

                int antes = ficheros.size();
                File file;
                try {
                if ((desde != null) && (!desde.equals(""))) {
                    file = pdf.construyeTabla(consulta(2, (String) unidades.get(numUnidades), desde, hasta, orden, ejercicio, false, null), xsl, listaNumAnotacionImpresas);
                    if (file != null) {
                        ficheros.add(encabezadoUOR1(1, (String) unidades.get(numUnidades),claveIdioma));
                        ficheros.add(file);//añado el dia del libro
                        int despues = ficheros.size();
                        numDias = despues - antes;
                        if ((m_Conf.getString("PDF.pie_uors").equals("si")) &&
                                (numDias > 0)) {
                            Hashtable tabla = new Hashtable();

                            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");//indico el formato que quiero
                            String sFecha = s.format(new Date());

                            tabla.put("FECHA", sFecha);

                            ficheros.add(pdf.construyeTablaVacia2("pieHoja2", tabla));
                        }
                        if (numUnidades < (unidades.size() - 1)) {
                            ficheros.add(pdf.construyeTablaVacia("saltoPagina"));
                        }
                    }
                } else {
                    file = pdf.construyeTabla(consulta_fecha(2, (String) unidades.get(numUnidades), fechaInicio, fechaFin, orden, ejercicio, false, null), xsl, listaNumAnotacionImpresas);

                    if (file != null) {
                        if (!ficheros.isEmpty()) {
                            ficheros.add(pdf.construyeTablaVacia("saltoPagina"));
                        }
                        ficheros.add(encabezadoUOR1(1, (String) unidades.get(numUnidades),claveIdioma));
                        ficheros.add(file);//añado el dia del libro
                        int despues = ficheros.size();
                        numDias = despues - antes;
                        if ((m_Conf.getString("PDF.pie_uors").equals("si")) &&
                                (numDias > 0)) {
                            Hashtable tabla = new Hashtable();

                            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");//indico el formato que quiero
                            String sFecha = s.format(new Date());

                            tabla.put("FECHA", sFecha);

                            ficheros.add(pdf.construyeTablaVacia2("pieHoja2", tabla));
                        }
                    }
                }
                } catch (TechnicalException te) {
                    te.printStackTrace();
                    fallo = "errorTecnico";
                    return "";
                }
                numAsientos += pdf.getNumFilas();
            }
                if (m_Conf.getString("PDF.pie_registro").equals("si")) {
                    Hashtable tabla = new Hashtable();
                    ficheros.add(pdf.construyeTablaVacia2("pieHoja3_"+claveIdioma, tabla));

                }

            if (ficheros.isEmpty()) {
                //si la ejecución finaliza antes por no haber registros
                m_Log.info("Impresión del Registro "+tipoConsulta+" por Unidad Orgánica - END"); 
                return "NO EXISTE";
            }

            // Se comprueba el numero de asientos recuperados y se estima si es
            // suficiente la
            // memoria disponible en la máquina virtual.
            m_Log.debug("numAsientos : " + numAsientos);
            maxAsientos = estimacionMaximoAsientos();
            fallo = "";
            if (numAsientos > maxAsientos) {
                fallo = "maxAsientos";
                return "";
            }

            // De paso, comprobamos si no se ha recuperado ninguna fila.
            if (numAsientos == 0) {
                fallo = "noAsientos";
                //si la ejecución finaliza antes por no haber registros
                m_Log.info("Impresión del Registro "+tipoConsulta+" por Unidad Orgánica - END"); 
                return "";
            }
            
            // Auditoria de acceso al registro
            try {
                AuditoriaManager.getInstance().auditarAccesoRegistro(
                        ConstantesAuditoria.REGISTRO_INFORME_POR_UNIDAD_TRAMITADORA,
                        usuario,
                        (String) consultaVO.getAtributo("depto"),
                        (String) consultaVO.getAtributo("unidadOrg"),
                        (String) consultaVO.getAtributo("tipo"),
                        ejercicio,
                        listaNumAnotacionImpresas);
            } catch (AnotacionRegistroException are) {
                m_Log.error("No se pudo registrar el evento de auditoria", are);
                fallo = "errorTecnico";
                return "";
            }
            
            //si la ejecución finaliza antes por no haber registros
            m_Log.info("Impresión del Registro "+tipoConsulta+" por Unidad Orgánica - END"); 

            return pdf.getPdf(ficheros);
        }
    }

    /**
     * Función encargada de realizar el informe de unidades orgánicas externas
     *
     * @return Un String con la ruta del pdf generado
     */
    private String relacionUnidadesOrganicasExternas(String claveIdioma) {
        numAsientos = 0;
 
        List<AnotacionRegistroVO> listaNumAnotacionImpresas = new ArrayList<AnotacionRegistroVO>();
        Vector ficheros = new Vector();
        GeneralPDF pdf = null;
        String codUnidad = "";          // Codigo de unidad organica externa seleccionada
        String codOrganizacion = "";    // Codigo de la organizacion externa seleccionada
        int numDias = 0;
        String desde = request.getParameter("regDesde");
        String hasta = request.getParameter("regHasta");
        String fechaInicio = request.getParameter("fechaInicio");
        String fechaFin = request.getParameter("fechaFin");
        String ejercicio = fechaInicio.substring(6);
        boolean orden = ((request.getParameter("orden") != null) && (request.getParameter("orden").equals("1")));

        // Obtenemos la organizacion y la unidad externas seleccionadas
        if (request.getParameter("txtCodigoUnidad") != null) {
            codUnidad = request.getParameter("txtCodigoUnidad");
        }
        if (request.getParameter("txtCodigoOrganizacion") != null) {
            codOrganizacion = request.getParameter("txtCodigoOrganizacion");
        }

        consultaVO.setAtributo("tipo", request.getParameter("tipo"));
        consultaVO.setAtributo("fechaInicio", request.getParameter("fechaInicio"));
        consultaVO.setAtributo("regInicio", request.getParameter("regDesde"));
        consultaVO.setAtributo("regFinal", request.getParameter("regHasta"));
        consultaVO.setAtributo("fechaFin", request.getParameter("fechaFin"));
        consultaVO.setAtributo("unidadOrg", String.valueOf(usuarioR.getUnidadOrgCod()));
        consultaVO.setAtributo("depto", String.valueOf(usuarioR.getDepCod()));
        consultaVO.setAtributo("departamento", request.getParameter("txtCodigoDpto"));

        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        // PDFS NUEVA SITUACION
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("usuDir", usuario.getDtr());
        // PDFS NUEVA SITUACION
        gVO.setAtributo("pdfFile", "registro");
        // FIN PDFS NUEVA SITUACION

        gVO.setAtributo("tamCabecera", "20mm");
        gVO.setAtributo("tamPie", "32mm");
        gVO.setAtributo("estilo", "css/informesRegistro.css");
        gVO.setAtributo("pagina", "1");
        gVO.setAtributo("opcionint", request.getParameter("interopcion"));
       
        Vector listaOrganizacionUors  = new Vector();   // lista con las organizaciones y sus correspondientes unidades organicas externas
        boolean hayUnidadesExternas = false;
       
        Vector organizaciones = new Vector();
        organizaciones = infMan.getOrganizacionesExternas( consultaVO, codOrganizacion, params);   // codOrganizacion= 0 -> todas las organizaciones externas
        
        // Para cada organizacion obtenemos las unidades organicas externas presentes en algun registro
        for (Iterator it = organizaciones.iterator(); it.hasNext(); ){
             String codigoOrganizacion = (String)it.next();
            
           Vector unidadesOrg = infMan.getUnidadesExternas( consultaVO, codigoOrganizacion, codUnidad, params);   // codUnidad= 0 -> todas las unidades de una org
            if (!unidadesOrg.isEmpty()){
                hayUnidadesExternas = true;
            }
            GeneralValueObject organizacionUors = new GeneralValueObject();
            organizacionUors.setAtributo("organizacion", codigoOrganizacion);
            organizacionUors.setAtributo("unidadesOrg", unidadesOrg);
            listaOrganizacionUors.addElement(organizacionUors);
        }

  
        if (!hayUnidadesExternas) {  // no hay unidades en ninguan organizacion
            return "NO EXISTE";
        } else {
            for (int j = 0; j < listaOrganizacionUors.size(); j++) {
                GeneralValueObject organizacionUors = (GeneralValueObject) listaOrganizacionUors.elementAt(j);
                String codOrganizacionExterna = (String) organizacionUors.getAtributo("organizacion");
                Vector unidades = (Vector) organizacionUors.getAtributo("unidadesOrg");

                /*    if (unidades.isEmpty()) {
                return "NO EXISTE";
                } else {
                 *
                 */
                for (int numUnidades = 0; numUnidades < unidades.size(); numUnidades++) {
                    consultaVO.setAtributo("unidad", unidades.get(numUnidades));
                    pdf = new GeneralPDF(params, gVO);

                    String pieFile;
                    pieFile = pdf.construyeTablaVacia("pieNumPagina_Externa_" + claveIdioma).getPath();
                    pdf.setPie(pieFile);

                    String xsl = "";
                    m_Log.debug(".......................Se procesa la unidad " + unidades.get(numUnidades));
                    m_Log.debug("TIPO de documento: " + request.getParameter("tipo"));

                    if (request.getParameter("tipo").equalsIgnoreCase("E")) {
                        xsl = "librocE_Externa_" + claveIdioma;

                    } else if (request.getParameter("tipo").equalsIgnoreCase("S")) {
                        xsl = "librocS_" + claveIdioma;
                    }

                    int antes = ficheros.size();
                    File file;
                    try {
                        if ((desde != null) && (!desde.equals(""))) {
                            file = pdf.construyeTabla(consulta(2, (String) unidades.get(numUnidades), desde, hasta, orden, ejercicio, true, codOrganizacionExterna), xsl, listaNumAnotacionImpresas);
                            if (file != null) {
                                ficheros.add(encabezadoUORExterna(1, (String) unidades.get(numUnidades), codOrganizacionExterna, claveIdioma));
                                ficheros.add(file);//añado el dia del libro
                                int despues = ficheros.size();
                                numDias = despues - antes;
                                if ((m_Conf.getString("PDF.pie_uors").equals("si"))
                                        && (numDias > 0)) {
                                    Hashtable tabla = new Hashtable();

                                    SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");//indico el formato que quiero
                                    String sFecha = s.format(new Date());

                                    tabla.put("FECHA", sFecha);

                                    ficheros.add(pdf.construyeTablaVacia2("pieHoja2", tabla));
                                }
                                if (numUnidades < (unidades.size() - 1)) {
                                    ficheros.add(pdf.construyeTablaVacia("saltoPagina"));
                                }
                            }
                        } else {
                            file = pdf.construyeTabla(consulta_fecha(2, (String) unidades.get(numUnidades), fechaInicio, fechaFin, orden, ejercicio, true, codOrganizacionExterna), xsl, listaNumAnotacionImpresas);

                            if (file != null) {
                                if (!ficheros.isEmpty()) {
                                    ficheros.add(pdf.construyeTablaVacia("saltoPagina"));
                                }
                                ficheros.add(encabezadoUORExterna(1, (String) unidades.get(numUnidades), codOrganizacionExterna, claveIdioma));
                                ficheros.add(file);//añado el dia del libro
                                int despues = ficheros.size();
                                numDias = despues - antes;
                                if ((m_Conf.getString("PDF.pie_uors").equals("si"))
                                        && (numDias > 0)) {
                                    Hashtable tabla = new Hashtable();

                                    SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");//indico el formato que quiero
                                    String sFecha = s.format(new Date());

                                    tabla.put("FECHA", sFecha);

                                    ficheros.add(pdf.construyeTablaVacia2("pieHoja2", tabla));
                                }
                            }
                        }
                    } catch (TechnicalException te) {
                        te.printStackTrace();
                        fallo = "errorTecnico";
                        return "";
                    }
                    numAsientos += pdf.getNumFilas();
                }
            
            }   // END FOR organizaciones

            if (m_Conf.getString("PDF.pie_registro").equals("si")) {
                Hashtable tabla = new Hashtable();
                ficheros.add(pdf.construyeTablaVacia2("pieHoja3_" + claveIdioma, tabla));

            }

            if (ficheros.isEmpty()) {
                return "NO EXISTE";
            }

            // Se comprueba el numero de asientos recuperados y se estima si es
            // suficiente la
            // memoria disponible en la máquina virtual.
            m_Log.debug("numAsientos : " + numAsientos);
            maxAsientos = estimacionMaximoAsientos();
            fallo = "";
            if (numAsientos > maxAsientos) {
                fallo = "maxAsientos";
                return "";
            }

            // De paso, comprobamos si no se ha recuperado ninguna fila.
            if (numAsientos == 0) {
                fallo = "noAsientos";
                return "";
            }

            // Auditoria de acceso al registro
            try {
                AuditoriaManager.getInstance().auditarAccesoRegistro(
                        ConstantesAuditoria.REGISTRO_INFORME_POR_UNIDAD_TRAMITADORA_EXTERNA,
                        usuario,
                        (String) consultaVO.getAtributo("depto"),
                        (String) consultaVO.getAtributo("unidadOrg"),
                        (String) consultaVO.getAtributo("tipo"),
                        ejercicio,
                        listaNumAnotacionImpresas);
            } catch (AnotacionRegistroException are) {
                m_Log.error("No se pudo registrar el evento de auditoria", are);
                fallo = "errorTecnico";
                return "";
            }
            
            return pdf.getPdf(ficheros);
        }
    }


    /**
     * Esta función se hizo en su momento dado que Flexia iba muy lento y la
     * exportación de un gran número de entradas copaba muchos recursos de la
     * máquina lo que propiciaba que funcionara peor, en base a el mejor
     * rendimiento de Flexia, en Lanbide piden eliminar esta restricción y
     * subiarla a una cantidad fija de 10.000. Esto se pide en la tarea 401822,
     * nota 54.
     *
     * @return máximo asientos exportables en base a la carga del servidor.
     */
    private int estimacionMaximoAsientos() {
//        int memoriaJVMKbytes = (int) Runtime.getRuntime().maxMemory() / 1024;
//        m_Log.debug("Memoria JVM en KBytes : " + memoriaJVMKbytes);
//        // Se estima que se necesitan 30Mb mas 90Kb por asiento para el parsing y se
//        // ha comprobado que con 64MB se pueden manejar 600 asientos, se pone como minimo. 
//        //Además se reduce el número a la mitad, ya que no se puede usar toda la JVM para un proceso que puede ser largo
//        int max = ((memoriaJVMKbytes - 40 * 1024) / 90)/2;
//        if (max < 600) {
//            max = 500;
//        }
//        m_Log.debug("Numero maximo de asientos estimado : " + max);
//        return max;
        return 10000;
    }

    private String informeDocumento(String[] params) {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        gVO.setAtributo("usuDir", usuario.getDtr());
        gVO.setAtributo("aplPathReal", getServlet().getServletContext().getRealPath(""));
        gVO.setAtributo("pdfFile", pdfFile);
        String protocolo = StrutsUtilOperations.getProtocol(request);
        m_Log.debug("PROTOCOLO en uso :" + protocolo);
        gVO.setAtributo("estilo", "css/documento.css");
        gVO.setAtributo("cuerpo", request.getParameter("cuerpo"));
        gVO.setAtributo("style", request.getParameter("style"));
        gVO.setAtributo("opcionint",request.getParameter("interopcion"));
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("Cuerpo de PDF:" + request.getParameter("cuerpo"));
        }
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("Estilo para el body :" + request.getParameter("style"));
        }
        GeneralPDF pdf = new GeneralPDF(gVO);
        return pdf.getPdf();
    }

    /**
     * Función encargada de realizar el informe de diligencias
     *
     * @return La ruta del informe generado
     */
    private String informeDiligencias(String[] params,String claveIdioma) {

        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);

        GeneralPDF pdf;
        Vector ficheros = new Vector();
        
        try {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        // PDFS NUEVA SITUACION
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("usuDir", usuario.getDtr());
        // PDFS NUEVA SITUACION
        gVO.setAtributo("pdfFile", "registro");
        // FIN PDFS NUEVA SITUACION        
        gVO.setAtributo("estilo", "css/informes.css");
        gVO.setAtributo("pagina", "1");
        gVO.setAtributo("cabecera", encabezado(0, claveIdioma));
        gVO.setAtributo("opcionint",request.getParameter("interopcion"));
        pdf = new GeneralPDF(params, gVO);
        String sql = "SELECT " + abd.convertir("dil_fec", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA,dil_txt AS" +
                " ANOTACION FROM R_DIL WHERE dil_dep=" +
                String.valueOf(usuarioR.getDepCod()) + " AND dil_uor=" +
                String.valueOf(usuarioR.getUnidadOrgCod()) + " AND dil_tir='" + request.getParameter("tipo") + "' AND (" + abd.convertir("dil_fec", AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + "  BETWEEN " +
                abd.convertir("'" + request.getParameter("fechaInicio") + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + " AND " +
                abd.convertir("'" + request.getParameter("fechaFin") + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + ")";

        File file;
        file = pdf.construyeTabla(sql, "dils_" + claveIdioma);
            if (file != null) {
                ficheros.add(file);
            }

        } catch (TechnicalException te) {
            te.printStackTrace();
            fallo = "errorTecnico";
            return "";
        }

        if (ficheros.isEmpty()) {
            return "NO EXISTE";
        } else {
            return pdf.getPdf(ficheros);
        }
    }
    /**
     * 
     */
    private String listadoAsientosBuzon(String opcion, int unidad, int departamento,String lista, String[] params) throws InternalErrorException{
        m_Log.debug("Impresión de anotación Buzón de Entrada - BEGIN");
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        
        HttpSession session = request.getSession();
        //Grabo los parametros
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        
        gVO.setAtributo("usuDir", usuario.getDtr());
        
        gVO.setAtributo("pdfFile","buzonEntrada");
        
        gVO.setAtributo("estilo", "css/informes.css");
        gVO.setAtributo("pagina", "1");
 
        GeneralPDF pdf = new GeneralPDF(params, gVO);
        // Recuperar el vector de anotaciones de la sesion.

        AnotacionRegistroManager anotRegManager = null;
        ArrayList<Vector> relacionAnotaciones = new ArrayList<Vector>();
       
        int numAnots=0;
        try {
            
            anotRegManager = AnotacionRegistroManager.getInstance();
            
           if(lista!=null && !lista.equals("")){
                
                String separador      = "§¥";
                String[] anotaciones = lista.split(separador);
                for(String anotacion : anotaciones){
                    RegistroValueObject regEntrada = new RegistroValueObject();
                    String[] datos = anotacion.split(";");
                    regEntrada.setIdentDepart(departamento);
                    regEntrada.setUnidadOrgan(Integer.parseInt(datos[3]));
                    regEntrada.setAnoReg(Integer.parseInt(datos[0]));
                    regEntrada.setNumReg(Long.parseLong(datos[1]));
                    regEntrada.setTipoReg(datos[2]);
                    
                    Vector<String> datosAnotacion = anotRegManager.getDatosAnotacion(regEntrada, params);
                    m_Log.debug("getDatosAnotacion");
                    relacionAnotaciones.add(datosAnotacion);
                }
                numAnots = anotaciones.length;
            } else {
                Vector resultadosConsultaBuzon = (Vector) session.getAttribute("consultaBuzonEntrada");
                
                numAnots = resultadosConsultaBuzon.size();
                
                for(Object resultadoConsulta: resultadosConsultaBuzon){
                    TramitacionValueObject traVO =(TramitacionValueObject) resultadoConsulta;
                    String[] anotacion=traVO.getEjerNum().split("/");
                   
                    Vector<String> datosAnotacion= new Vector();
                    datosAnotacion.add(String.valueOf(traVO.getCodDepartamento()));//0-codDepartamento
                    datosAnotacion.add(String.valueOf(traVO.getCodUnidadRegistro()));//codUOR
                    datosAnotacion.add(String.valueOf(traVO.getTipoRegistro()));//tipoAnotacion
                    datosAnotacion.add(anotacion[0]);//ejercicio
                    datosAnotacion.add(anotacion[1]);//numero
                    datosAnotacion.add(traVO.getFechaAnotacion());//5-fechaEntrada
                    datosAnotacion.add(null);//fechaDocumento
                    datosAnotacion.add(traVO.getAsunto());//extracto
                    datosAnotacion.add(traVO.getRemitente());//nombreInteresado
                    datosAnotacion.add(traVO.getApellido1());//apell1Interesado
                    datosAnotacion.add(traVO.getApellido2());//10-apell2Interesado
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(traVO.getUnidadTramitadora());//destino
                    datosAnotacion.add(String.valueOf(traVO.getEstado()));//estado
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(null);//15-relleno
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(null);//expediente relacionado
                    datosAnotacion.add(traVO.getDocTercero());
                    datosAnotacion.add(traVO.getCodigoAsuntoCodificado()); //20-cod Asunto
                    datosAnotacion.add(traVO.getDescripcionAsuntoCodificado()); // descripcion asunto
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(traVO.getObservaciones()); // observaciones
                    
                    m_Log.debug("getDatosAnotacion");
                    relacionAnotaciones.add(datosAnotacion);
                    
                }

                m_Log.debug(" =============> NUMERO DE ANOTACIONES: " + numAnots);

                
                maxAsientos = estimacionMaximoAsientos();
                fallo = "";
                if (numAnots > maxAsientos) {
                    fallo = "maxAsientos";
                    return "";
                }

            }
            
            // Auditoria acceso a anotaciones buzón entrada
               String pantalla = null;
               pantalla = ConstantesAuditoria.EXPEDIENTE_BUZON_ENTRADA_LISTADO_EXPORT_CSV;

               AuditoriaManager.getInstance().auditarAccesoRegistroListado(
                   pantalla, usuario, relacionAnotaciones);
           
            m_Log.debug(" =============> relacionAnotaciones: " + relacionAnotaciones.size());
        } catch (AnotacionRegistroException are) {
            throw new InternalErrorException(are);
        }

        StringBuffer textoXml = new StringBuffer("");
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(relacionAnotaciones);
        }
        if (relacionAnotaciones != null) {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Tam:" + relacionAnotaciones.size());
            }
            textoXml.append("<listado>");
           
            for (int i = 0; i < relacionAnotaciones.size(); i++) {
                Vector anotacion = (Vector) relacionAnotaciones.get(i);
                String remitente = "";
                if(anotacion.elementAt(9)==null){
                    remitente = (String) anotacion.elementAt(8);
                }else if (((String) anotacion.elementAt(9)).trim().equals("")) {
                    if (((String) anotacion.elementAt(10)).trim().equals("")) {
                        remitente += (String) anotacion.elementAt(8);
                    } else {
                        remitente += (String) anotacion.elementAt(10);
                        remitente += ", " + (String) anotacion.elementAt(8);
                    }
                } else {
                    remitente = (String) anotacion.elementAt(9);
                    if (((String) anotacion.elementAt(10)).trim().equals("")) {
                        remitente += ", " + (String) anotacion.elementAt(8);
                    } else {
                        remitente += " " + (String) anotacion.elementAt(10);
                        remitente += ", " + (String) anotacion.elementAt(8);
                    }
                }
                String ejercicio_numero = (String) anotacion.elementAt(3);
                ejercicio_numero += "/";
                ejercicio_numero += (String) anotacion.elementAt(4);
                String fecha_hora = (String) anotacion.elementAt(5);
                int espacio = fecha_hora.indexOf(" ");
                String fecha = fecha_hora.substring(0, espacio);
                String hora = fecha_hora.substring(espacio + 1, fecha_hora.length());                            
                String documentoRemitente =(String) anotacion.elementAt(19);
                String codAsunto = (String) anotacion.elementAt(20);
                String desAsunto = (String) anotacion.elementAt(21);
                                
                
                
                textoXml.append("<fila>");
                
                textoXml.append("<numeroAnotacion>" + ejercicio_numero + "</numeroAnotacion>");
                textoXml.append("<fecha>" + fecha + "</fecha>");
                textoXml.append("<hora>" + hora + "</hora>");
                textoXml.append("<documentoRemitente>"+ documentoRemitente +"</documentoRemitente>");
                textoXml.append("<nombreRemitente>" + remitente + "</nombreRemitente>");
                String extracto = TransformacionAtributoSelect.escapeValorParaXML(AdaptadorSQLBD.js_unescape((String) anotacion.elementAt(7))).replaceAll("\r\n|\r|\n"," ");
                extracto = "\""+extracto+"\"";
                textoXml.append("<extracto>" + extracto + "</extracto>");	
                String observaciones = (anotacion.elementAt(24) != null)?
                        TransformacionAtributoSelect.escapeValorParaXML(AdaptadorSQLBD.js_unescape((String) anotacion.elementAt(24))).replaceAll("\r\n|\r|\n"," ") : "";
                observaciones = "\""+observaciones+"\"";
                textoXml.append("<observaciones>" + observaciones + "</observaciones>");	
                textoXml.append("<destino>" + (String) anotacion.elementAt(12) + "</destino>");
                textoXml.append("<codAsunto>" + codAsunto + "</codAsunto>");
                textoXml.append("<asunto>" + desAsunto + "</asunto>");
                
                textoXml.append("</fila>");
            }
            
            textoXml.append("</listado>");
        }
         String plantilla="listadoAsientosBuzonCSV"+"_";	
        	
      
 
        plantilla +=IdiomasManager.getInstance().getClaveIdioma(params, Integer.parseInt(idioma));       
        File f = pdf.transformaXML(textoXml.toString(), plantilla);
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("F:" + f);
        }
        String nombreFichero = null;
        Vector ficheros = new Vector();
        if (f != null) {
            ficheros.add(f);
           	
                nombreFichero=pdf.getCSV(ficheros);	
            
        }
        
        m_Log.info("Impresión de anotaciones Buzón Entrada - END");
        request.setAttribute("opcion", opcion);
        return nombreFichero;
        
    }
    
    
    
        private String listadoAsientosBuzonTecnicoReferencia(String opcion, int unidad, int departamento,String lista, String[] params) throws InternalErrorException{
        m_Log.debug("Impresión de anotación Buzón de Entrada - BEGIN");
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        
        HttpSession session = request.getSession();
        //Grabo los parametros
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        
        gVO.setAtributo("usuDir", usuario.getDtr());
        
        gVO.setAtributo("pdfFile","buzonEntrada");
        
        gVO.setAtributo("estilo", "css/informes.css");
        gVO.setAtributo("pagina", "1");
 
        GeneralPDF pdf = new GeneralPDF(params, gVO);
        // Recuperar el vector de anotaciones de la sesion.

        AnotacionRegistroManager anotRegManager = null;
        ArrayList<Vector> relacionAnotaciones = new ArrayList<Vector>();
       
        int numAnots=0;
        try {
            
            anotRegManager = AnotacionRegistroManager.getInstance();
            
           if(lista!=null && !lista.equals("")){
                
                String separador      = "§¥";
                String[] anotaciones = lista.split(separador);
                for(String anotacion : anotaciones){
                    RegistroValueObject regEntrada = new RegistroValueObject();
                    String[] datos = anotacion.split(";");
                    regEntrada.setIdentDepart(departamento);
                    regEntrada.setUnidadOrgan(Integer.parseInt(datos[3]));
                    regEntrada.setAnoReg(Integer.parseInt(datos[0]));
                    regEntrada.setNumReg(Long.parseLong(datos[1]));
                    regEntrada.setTipoReg(datos[2]);
                    regEntrada.setNombreTecnicoReferencia(datos.length == 5 ? datos[4] : "");
                    
                    Vector<String> datosAnotacion = anotRegManager.getDatosAnotacionTecnicoReferencia(regEntrada, params);
                    m_Log.debug("getDatosAnotacion");
                    relacionAnotaciones.add(datosAnotacion);
                }
                numAnots = anotaciones.length;
            } else {
                Vector resultadosConsultaBuzon = (Vector) session.getAttribute("consultaBuzonEntrada");
                
                numAnots = resultadosConsultaBuzon.size();
                
                for(Object resultadoConsulta: resultadosConsultaBuzon){
                    TramitacionValueObject traVO =(TramitacionValueObject) resultadoConsulta;
                    String[] anotacion=traVO.getEjerNum().split("/");
                   
                    Vector<String> datosAnotacion= new Vector();
                    datosAnotacion.add(String.valueOf(traVO.getCodDepartamento()));//0-codDepartamento
                    datosAnotacion.add(String.valueOf(traVO.getCodUnidadRegistro()));//codUOR
                    datosAnotacion.add(String.valueOf(traVO.getTipoRegistro()));//tipoAnotacion
                    datosAnotacion.add(anotacion[0]);//ejercicio
                    datosAnotacion.add(anotacion[1]);//numero
                    datosAnotacion.add(traVO.getFechaAnotacion());//5-fechaEntrada
                    datosAnotacion.add(null);//fechaDocumento
                    datosAnotacion.add(traVO.getAsunto());//extracto
                    datosAnotacion.add(traVO.getRemitente());//nombreInteresado
                    datosAnotacion.add(traVO.getApellido1());//apell1Interesado
                    datosAnotacion.add(traVO.getApellido2());//10-apell2Interesado
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(traVO.getUnidadTramitadora());//destino
                    datosAnotacion.add(String.valueOf(traVO.getEstado()));//estado
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(null);//15-relleno
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(null);//expediente relacionado
                    datosAnotacion.add(traVO.getDocTercero());
                    datosAnotacion.add(traVO.getCodigoAsuntoCodificado()); //20-cod Asunto
                    datosAnotacion.add(traVO.getDescripcionAsuntoCodificado()); // descripcion asunto
                    datosAnotacion.add(traVO.getTecnicoReferencia());
                    datosAnotacion.add(null);//relleno
                    datosAnotacion.add(traVO.getObservaciones());
                    
                    m_Log.debug("getDatosAnotacion");
                    relacionAnotaciones.add(datosAnotacion);
                    
                }

                m_Log.debug(" =============> NUMERO DE ANOTACIONES: " + numAnots);

                
                maxAsientos = estimacionMaximoAsientos();
                fallo = "";
                if (numAnots > maxAsientos) {
                    fallo = "maxAsientos";
                    return "";
                }

            }
            
            // Auditoria acceso a anotaciones buzón entrada
               String pantalla = null;
               pantalla = ConstantesAuditoria.EXPEDIENTE_BUZON_ENTRADA_LISTADO_EXPORT_CSV;

               AuditoriaManager.getInstance().auditarAccesoRegistroListado(
                   pantalla, usuario, relacionAnotaciones);
           
            m_Log.debug(" =============> relacionAnotaciones: " + relacionAnotaciones.size());
        } catch (AnotacionRegistroException are) {
            throw new InternalErrorException(are);
        }

        StringBuffer textoXml = new StringBuffer("");
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(relacionAnotaciones);
        }
        if (relacionAnotaciones != null) {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Tam:" + relacionAnotaciones.size());
            }
            textoXml.append("<listado>");
           
            for (int i = 0; i < relacionAnotaciones.size(); i++) {
                Vector anotacion = (Vector) relacionAnotaciones.get(i);
                String remitente = "";
                if(anotacion.elementAt(9)==null){
                    remitente = (String) anotacion.elementAt(8);
                }else if (((String) anotacion.elementAt(9)).trim().equals("")) {
                    if (((String) anotacion.elementAt(10)).trim().equals("")) {
                        remitente += (String) anotacion.elementAt(8);
                    } else {
                        remitente += (String) anotacion.elementAt(10);
                        remitente += ", " + (String) anotacion.elementAt(8);
                    }
                } else {
                    remitente = (String) anotacion.elementAt(9);
                    if (((String) anotacion.elementAt(10)).trim().equals("")) {
                        remitente += ", " + (String) anotacion.elementAt(8);
                    } else {
                        remitente += " " + (String) anotacion.elementAt(10);
                        remitente += ", " + (String) anotacion.elementAt(8);
                    }
                }
                String ejercicio_numero = (String) anotacion.elementAt(3);
                ejercicio_numero += "/";
                ejercicio_numero += (String) anotacion.elementAt(4);
                String fecha_hora = (String) anotacion.elementAt(5);
                int espacio = fecha_hora.indexOf(" ");
                String fecha = fecha_hora.substring(0, espacio);
                String hora = fecha_hora.substring(espacio + 1, fecha_hora.length());                            
                String documentoRemitente =(String) anotacion.elementAt(19);
                String codAsunto = (String) anotacion.elementAt(20);
                String desAsunto = (String) anotacion.elementAt(21);
                String nombreTecnicoReferencia = (String) anotacion.elementAt(22);
                m_Log.debug("tecnico referencia : "+nombreTecnicoReferencia);
                
                
                textoXml.append("<fila>");
                
                textoXml.append("<numeroAnotacion>" + ejercicio_numero + "</numeroAnotacion>");
                textoXml.append("<fecha>" + fecha + "</fecha>");
                textoXml.append("<hora>" + hora + "</hora>");
                textoXml.append("<documentoRemitente>"+ documentoRemitente +"</documentoRemitente>");
                textoXml.append("<nombreRemitente>" + remitente + "</nombreRemitente>");
                String extracto = TransformacionAtributoSelect.escapeValorParaXML(AdaptadorSQLBD.js_unescape((String) anotacion.elementAt(7))).replaceAll("\r\n|\r|\n"," ");
                extracto = "\""+extracto+"\"";
                textoXml.append("<extracto>" + extracto + "</extracto>");
                String observaciones = (anotacion.elementAt(24) != null)?
                        TransformacionAtributoSelect.escapeValorParaXML(AdaptadorSQLBD.js_unescape((String) anotacion.elementAt(24))).replaceAll("\r\n|\r|\n"," ") : "";
                observaciones = "\""+observaciones+"\"";
                textoXml.append("<observaciones>" + observaciones + "</observaciones>");
                textoXml.append("<destino>" + (String) anotacion.elementAt(12) + "</destino>");
                textoXml.append("<codAsunto>" + codAsunto + "</codAsunto>");
                textoXml.append("<asunto>" + desAsunto + "</asunto>");
                textoXml.append("<tecnicoReferencia>" + nombreTecnicoReferencia + "</tecnicoReferencia>");
                
                textoXml.append("</fila>");
            }
            
            textoXml.append("</listado>");
        }
        String plantilla="listadoAsientosBuzonTecnicoReferenciaCSV"+"_";	
         m_Log.debug("texto plantilla generado : "+textoXml.toString());
      
 
        plantilla +=IdiomasManager.getInstance().getClaveIdioma(params, Integer.parseInt(idioma));       
        File f = pdf.transformaXML(textoXml.toString(), plantilla);
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("F:" + f);
        }
        String nombreFichero = null;
        Vector ficheros = new Vector();
        if (f != null) {
            ficheros.add(f);
           	
                nombreFichero=pdf.getCSV(ficheros);	
            
        }
        
        m_Log.info("Impresión de anotaciones Buzón Entrada - END");
        request.setAttribute("opcion", opcion);
        return nombreFichero;
        
    }
    
    
    /**
     * Función encargada de realizar el listado de los asientos
     * jsp: listadoRelacionAnotaciones.jsp
     * @param params String[] con los parametros de conexion a BBDD.
     * @return La ruta del informe generado
     */
     private String listadoAsientos(String opcion,int unidad, int departamento, String lista, String tipoOrden, int columna,String[] params) throws InternalErrorException {
        m_Log.info("Impresión de anotaciones de registro - BEGIN");
        
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("listadoAsientos");
        }
        // Recupero la sesion
        HttpSession sesion = request.getSession();
        // Grabo los parametros
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        // PDFS NUEVA SITUACION
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("usuDir", usuario.getDtr());
        // PDFS NUEVA SITUACION
        gVO.setAtributo("pdfFile", "registro");
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("opcionint",request.getParameter("interopcion"));
        
        gVO.setAtributo("estilo", "css/informes.css");
        gVO.setAtributo("pagina", "1");

        if(opcion.equals("imprimirListadoAsientos")){	
            try {	
                gVO.setAtributo("cabecera", encabezadoListadoAsientos());	
            } catch (TechnicalException te) {	
                throw new InternalErrorException(te);	
            }
            
        gVO.setAtributo("tamCabecera", "20mm");
        gVO.setAtributo("tamPie", "32mm");
        }

        GeneralPDF pdf = new GeneralPDF(params, gVO);
        // Recuperar el vector de anotaciones de la sesion.

        AnotacionRegistroManager anotRegManager = null;
        ArrayList<Vector> relacionAnotaciones = new ArrayList<Vector>();
        //Vector relacionAnotaciones = new Vector();
        int numAnots=0;
        try {
            // OJO: se debe tener en cuenta que la consulta de bbdd que se va a ejecutar no es la misma si la exportacion se solicita para todas las anotaciones (no se ha marcado ningún check)
            //o para algunas (se marca el check de las elegidas para exportar)
            anotRegManager = AnotacionRegistroManager.getInstance();
            
           if(lista!=null && !lista.equals("")){
                
                String separador      = "§¥";
                String[] anotaciones = lista.split(separador);
                for(String anotacion : anotaciones){
                    RegistroValueObject regEntrada = new RegistroValueObject();
                    String[] datos = anotacion.split(";");
                    regEntrada.setIdentDepart(departamento);
                    regEntrada.setUnidadOrgan(unidad);
                    regEntrada.setAnoReg(Integer.parseInt(datos[0]));
                    regEntrada.setNumReg(Long.parseLong(datos[1]));
                    regEntrada.setTipoReg(datos[2]);
                    
                    Vector<String> datosAnotacion = anotRegManager.getDatosAnotacion(regEntrada, params);
                    relacionAnotaciones.add(datosAnotacion);
                }
                numAnots = anotaciones.length;
            } else {


                RegistroValueObject storedConsulta = (RegistroValueObject) sesion.getAttribute("registroValueObjectConsulta");

                storedConsulta.setUsuarioLogueado(usuario);

                 if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuario.getIdUsuario(), params)) {                
                     storedConsulta.setDirectivaUsuPermisoUor("SI");    
                 }


                numAnots = AnotacionRegistroManager.getInstance().getNumeroTotalAnotaciones(storedConsulta, params);
                m_Log.debug(" =============> NUMERO DE ANOTACIONES: " + numAnots);

                m_Log.debug("numAsientos en listado : " + numAnots);
                maxAsientos = estimacionMaximoAsientos();
                fallo = "";
                if (numAnots > maxAsientos) {
                    fallo = "maxAsientos";
                    return "";
                }
                
                 if ("false".equals(tipoOrden)){
                        tipoOrden="DESC";
                    }else if("true".equals(tipoOrden)) {
                        tipoOrden="ASC";
                    }

                relacionAnotaciones = AnotacionRegistroManager.getInstance().relacionRegistroValueObjectImprimirOptimo(storedConsulta, params, 1, numAnots, columna,tipoOrden);
            }
            
            // Auditoria acceso a registros
           String pantalla = null;
            if (opcion.equals("imprimirListadoAsientos")) {
                pantalla = ConstantesAuditoria.REGISTRO_LISTADO_INFORME;
            } else if (opcion.equals("exportarCSV")) {
                pantalla = ConstantesAuditoria.REGISTRO_LISTADO_EXPORT_CSV;
            }
           
            // Auditoria de acceso a registro
            AuditoriaManager.getInstance().auditarAccesoRegistroListado(
                    pantalla, usuario, relacionAnotaciones);
           
            m_Log.debug(" =============> relacionAnotaciones: " + relacionAnotaciones.size());
        } catch (AnotacionRegistroException are) {
            throw new InternalErrorException(are);
        }

        StringBuffer textoXml = new StringBuffer("");
        if (m_Log.isDebugEnabled()) {
            m_Log.debug(relacionAnotaciones);
        }
        if (relacionAnotaciones != null) {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("Tam:" + relacionAnotaciones.size());
            }
            textoXml.append("<listado>");
           
            for (int i = 0; i < relacionAnotaciones.size(); i++) {
                Vector anotacion = (Vector) relacionAnotaciones.get(i);
                String remitente = "";
                if (((String) anotacion.elementAt(9)).trim().equals("")) {
                    if (((String) anotacion.elementAt(10)).trim().equals("")) {
                        remitente += (String) anotacion.elementAt(8);
                    } else {
                        remitente += (String) anotacion.elementAt(10);
                        remitente += ", " + (String) anotacion.elementAt(8);
                    }
                } else {
                    remitente = (String) anotacion.elementAt(9);
                    if (((String) anotacion.elementAt(10)).trim().equals("")) {
                        remitente += ", " + (String) anotacion.elementAt(8);
                    } else {
                        remitente += " " + (String) anotacion.elementAt(10);
                        remitente += ", " + (String) anotacion.elementAt(8);
                    }
                }
                String ejercicio_numero = (String) anotacion.elementAt(3);
                ejercicio_numero += "/";
                ejercicio_numero += (String) anotacion.elementAt(4);
                String fecha_hora = (String) anotacion.elementAt(5);
                int espacio = fecha_hora.indexOf(" ");
                String fecha = fecha_hora.substring(0, espacio);
                String hora = fecha_hora.substring(espacio + 1, fecha_hora.length());
                String estado = (String) anotacion.elementAt(13);                              
                if (estado != null) {
                    if (estado.equals("9")) {
                        estado = "Anulada";
                    } else {
                        estado = "";
                    }
                } else {
                    estado = "";
                }

                String estadoAnotacion = (String)anotacion.elementAt(13);
                String exp_asociado = (String)anotacion.elementAt(18);
                String textoEstadoAnotacion = "";
                String salida_expedientes = ""; 
                
                //el largo de la linea de expediente es de 26, en caso de existir expediente es necesario formatear cada linea de expediente a 26
                if(estadoAnotacion.equals("3")){                                                              
                        textoEstadoAnotacion = "Asociada a Expediente: ";
                        exp_asociado = exp_asociado.replace("\\n","#");                    
                    } else if (estadoAnotacion.equals("0")) {
                        textoEstadoAnotacion = "Pendiente";
                        exp_asociado = "";
                    } else if (estadoAnotacion.equals("1")) {
                        textoEstadoAnotacion = "Aceptada";
                        exp_asociado = "";
                        }else if (estadoAnotacion.equals("9")){	
                        textoEstadoAnotacion="Anulada";	
                        exp_asociado="";
                    } else if (estadoAnotacion.equals("2")) {
                        textoEstadoAnotacion = "Rechazada";
                        exp_asociado = "";
                    }else
                        exp_asociado = "";
                
                
                // original
                //String[] expedientes = exp_asociado.split("#");  
                
                String[] expedientes = null;
                
                if(exp_asociado!=null && !"".equals(exp_asociado))
                    expedientes = exp_asociado.split("#");  
                
                textoXml.append("<fila>");
                textoXml.append("<estado>" + estado + "</estado>");
                textoXml.append("<ejercicio>" + ejercicio_numero + "</ejercicio>");
                textoXml.append("<fecha>" + fecha + "</fecha>");
                textoXml.append("<hora>" + hora + "</hora>");
                textoXml.append("<remitente>" + remitente + "</remitente>");
                String extracto = TransformacionAtributoSelect.escapeValorParaXML(AdaptadorSQLBD.js_unescape((String) anotacion.elementAt(7))).replaceAll("\r\n|\r|\n"," ");
                extracto = "\""+extracto+"\"";
                 textoXml.append("<asunto>" + extracto + "</asunto>");
                textoXml.append("<tipo>" + (String) anotacion.elementAt(2) + "</tipo>");
                textoXml.append("<estadoAnotacion>" + textoEstadoAnotacion  + "</estadoAnotacion>");
                textoXml.append("<documento>" + anotacion.elementAt(22)  + "</documento>");
                textoXml.append("<usuarioAlta>" + anotacion.elementAt(23)  + "</usuarioAlta>");
                textoXml.append("<expedientes>"); 
                for (int j = 0;expedientes!=null && j<expedientes.length;j++){                        
                    textoXml.append(expedientes[j].trim());
                    if(opcion.equals("imprimirListadoAsientos")){	
                    textoXml.append("\r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n \r\n");	
                    }else{	
                        if(j<expedientes.length-1)	
                        textoXml.append(", ");	
                    }                }
                textoXml.append("</expedientes>");
                textoXml.append("<destino>" + (String) anotacion.elementAt(12) + "</destino>");
                textoXml.append("</fila>");
            }
            
            textoXml.append("</listado>");
        }
         String plantilla="";	
        if(opcion.equals("imprimirListadoAsientos")){	
            plantilla = "listadoAsientos" + "_";	
        }else if(opcion.equals("exportarCSV")){	
            plantilla="listadoAsientosCSV"+"_";	
        }
        plantilla +=IdiomasManager.getInstance().getClaveIdioma(params, Integer.parseInt(idioma));       
        File f = pdf.transformaXML(textoXml.toString(), plantilla);
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("F:" + f);
        }
        String nombreFichero = null;
        Vector ficheros = new Vector();
        if (f != null) {
            ficheros.add(f);
           if(opcion.equals("imprimirListadoAsientos")){	
                nombreFichero = pdf.getPdf(ficheros);	
            }else if(opcion.equals("exportarCSV")){	
                nombreFichero=pdf.getCSV(ficheros);	
            }
        }
        
        m_Log.info("Impresión de anotaciones de registro - END");
        request.setAttribute("opcion", opcion);
        return nombreFichero;
    }

    /**
     * Función auxiliar encargada de realizar el encabezado para el
     * listado de Asientos
     *
     * @return La ruta del fichero generado
     */
    private String encabezadoListadoAsientos() throws TechnicalException {
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("encabezadoListadoAsientos");
        }
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        // PDFS NUEVA SITUACION
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("usuDir", usuario.getDtr());
        // PDFS NUEVA SITUACION
        gVO.setAtributo("pdfFile", "registro");
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("pdfFile", pdfFile);
        gVO.setAtributo("opcionint",request.getParameter("interopcion"));
        GeneralPDF pdf = new GeneralPDF(params, gVO);
        String tipo = "";
        String desOri = "";
        if (request.getParameter("tipo").equalsIgnoreCase("E")) {
            if ("1".equals(idioma)) {
                tipo = "REGISTRO DE ENTRADA";
            } else {
                tipo = "REXISTRO DE ENTRADA";
            }
            desOri = "DESTINO";
        } else if (request.getParameter("tipo").equalsIgnoreCase("S")) {
            if ("1".equals(idioma)) {
                tipo = "REGISTRO DE SALIDA";
                desOri = "ORIGEN";
            } else {
                tipo = "REXISTRO DE SAIDA";
                desOri = "ORIXE";
            }
        }
        String nombre = null;
        String escudo = null;
        String sql = "SELECT DISTINCT '" + tipo + "' AS TIP,'" + desOri + "' AS DOR,'";
        if (escudo != null) {
            sql += usuario.getOrgIco().substring(1);
        }
        sql += "' AS IMG,'";
        if (nombre != null) {
            sql += usuario.getEnt();
        }
        sql += "' AS ENT,'" + usuarioR.getUnidadOrg() + "' AS UOR,2 AS OPC ";
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        sql += abd.consultaSinTabla();

        File f = null;
        if (request.getParameter("tipo").equalsIgnoreCase("E")) {
            if ("1".equals(idioma)) {
                m_Log.debug("********************* encabezadoRegistro ********************************");
                f = pdf.construyeTabla(sql, "encabezadoRegistro");
            } else {
                m_Log.debug("********************* encabezadoRegistroG ********************************");
                f = pdf.construyeTabla(sql, "encabezadoRegistroG");
            }
        } else {
            if ("1".equals(idioma)) {
                m_Log.debug("********************* encabezadoRegistroS ********************************");
                f = pdf.construyeTabla(sql, "encabezadoRegistroS");
            } else {
                m_Log.debug("********************* encabezadoRegistroSG ********************************");
                f = pdf.construyeTabla(sql, "encabezadoRegistroSG");
            }
        }
        return f.getPath();
    }

    /**
     * Función auxiliar encargada de realizar los encabezados del registro
     *
     * @return La ruta del fichero generado
     */
    private String encabezado(int num, String claveIdioma) throws TechnicalException {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        // PDFS NUEVA SITUACION
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("usuDir", usuario.getDtr());
        // PDFS NUEVA SITUACION
        gVO.setAtributo("pdfFile", "registro");
        // FIN PDFS NUEVA SITUACION

        gVO.setAtributo("pdfFile", pdfFile);
        gVO.setAtributo("opcionint",request.getParameter("interopcion")) ;
        GeneralPDF pdf = new GeneralPDF(params, gVO);
        String tipo = "";
        String desOri = "";
         int idi=Integer.parseInt(idioma);

        descriptor.setIdi_cod(idi);
        if (request.getParameter("tipo").equalsIgnoreCase("E")) {
            tipo = descriptor.getDescripcion(1, "gEtiq_REGENTRADA")+" "+request.getParameter("fechaDesde").substring(0, 10)+" "+descriptor.getDescripcion(1, "gEtiq_HASTA")+" "+request.getParameter("fechaHasta").substring(0, 10);
            desOri =descriptor.getDescripcion(1, "gEtiq_DESTINO");
        } else if (request.getParameter("tipo").equalsIgnoreCase("S")) {
            tipo = descriptor.getDescripcion(1, "gEtiq_REGSALIDA")+" "+request.getParameter("fechaDesde").substring(0, 10)+" "+descriptor.getDescripcion(1, "gEtiq_HASTA")+" "+request.getParameter("fechaHasta").substring(0, 10);
            desOri = descriptor.getDescripcion(1, "gEtiq_ORIGEN");
        }
        //EN CATALAN HAY APOSTROFES TENGO QUE ESCAPARLOS PARA EL SQL
        tipo=StringEscapeUtils.escapeSql(tipo);
        String nombre = request.getParameter("nombre");
        String escudo = request.getParameter("escudo");
        String sql = "SELECT DISTINCT '" + tipo + "' AS TIP,'" + desOri + "' AS DOR,'";
        if (escudo != null) {
            sql += usuario.getOrgIco().substring(1);
        }
        sql += "' AS IMG,'";
        if (nombre != null) {
            sql += usuario.getEnt();
        }
        sql += "' AS ENT,'" + usuarioR.getUnidadOrg() + "' AS UOR," + num + "AS" +
                " OPC ";
        AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
        sql += abd.consultaSinTabla();
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("Sql: " + sql);
        }
        File cabeza = null;
        if (request.getParameter("tipo").equalsIgnoreCase("E")) {
            cabeza = pdf.construyeTabla(sql, "encabezadoRegistroE_"+claveIdioma);
        } else {
             cabeza = pdf.construyeTabla(sql, "encabezadoRegistroS_"+claveIdioma);
        }

        return cabeza.getPath();
    }

    private File encabezadoUOR1(int num, String codigoUnidad,String claveIdioma) throws TechnicalException {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        gVO.setAtributo("usuDir", usuario.getDtr());
        gVO.setAtributo("pdfFile", "registro");

        gVO.setAtributo("pdfFile", pdfFile);
        gVO.setAtributo("opcionint",request.getParameter("interopcion"));
        GeneralPDF pdf = new GeneralPDF(params, gVO);
        String tipo = "";
        String desOri = "";
        
       int idi=Integer.parseInt(idioma);
       descriptor.setIdi_cod(idi);
        if (request.getParameter("tipo").equalsIgnoreCase("E")) {
          
            tipo = descriptor.getDescripcion(1, "gEtiq_INFENTRADA")+" "+request.getParameter("fechaDesde").substring(0, 10)+" "+descriptor.getDescripcion(1, "gEtiq_HASTA")+" "+request.getParameter("fechaHasta").substring(0, 10);
            desOri =descriptor.getDescripcion(1, "gEtiq_DESTINO");
        } else if (request.getParameter("tipo").equalsIgnoreCase("S")) {
         
            tipo = descriptor.getDescripcion(1, "gEtiq_REGSALIDA")+" "+request.getParameter("fechaDesde").substring(0, 10)+" "+descriptor.getDescripcion(1, "gEtiq_HASTA")+" "+request.getParameter("fechaHasta").substring(0, 10);
            desOri = descriptor.getDescripcion(1, "gEtiq_ORIGEN");
        }
        String nombre = request.getParameter("nombre");
        String escudo = request.getParameter("escudo");
        tipo=StringEscapeUtils.escapeSql(tipo);
        String sql = "SELECT DISTINCT '" + tipo + "' AS TIP,'" + desOri + "' AS DOR,'";
        if (escudo != null) {
            sql += usuario.getOrgIco().substring(1);
        }
        sql += "' AS IMG,'";
        if (nombre != null) {
            sql += usuario.getEnt();
        }
       
        sql += "' AS ENT," + conf.getString("SQL.A_UOR.nombre") + " AS UOR," + num + " AS"
                + " OPC FROM A_UOR WHERE " + conf.getString("SQL.A_UOR.codigo") + "=" // request.getParameter("txtCodigoUnidad");
                + codigoUnidad;

        
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("encabezadoRegistro: " + sql);
        }
        File xmlEncabezado;
        if (request.getParameter("tipo").equalsIgnoreCase("E")) {
             xmlEncabezado = pdf.construyeTabla(sql, "encabezadoRegistroUorE_"+claveIdioma);
        } else {
            xmlEncabezado = pdf.construyeTabla(sql, "encabezadoRegistroUorS_"+claveIdioma);
        }
        return xmlEncabezado;
    }

 /*
  * Funcion que genera el encabezado para el informde de registro cuando las unidades son externas
  * @param num
  */
    private File encabezadoUORExterna(int num, String codigoUnidad, String codOrganizacion, String claveIdioma) throws TechnicalException {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        gVO.setAtributo("usuDir", usuario.getDtr());
        gVO.setAtributo("pdfFile", "registro");

        gVO.setAtributo("pdfFile", pdfFile);
        gVO.setAtributo("opcionint", request.getParameter("interopcion"));
        GeneralPDF pdf = new GeneralPDF(params, gVO);
        String tipo = "";
        String desOri = "";

        int idi = Integer.parseInt(idioma);
        descriptor.setIdi_cod(idi);
        if (request.getParameter("tipo").equalsIgnoreCase("E")) {

            tipo = descriptor.getDescripcion(1, "gEtiq_REGEntradaExterna") + " " + request.getParameter("fechaDesde").substring(0, 10) + " " + descriptor.getDescripcion(1, "gEtiq_HASTA") + " " + request.getParameter("fechaHasta").substring(0, 10);
            desOri = descriptor.getDescripcion(1, "gEtiq_DESTINO");
        } else if (request.getParameter("tipo").equalsIgnoreCase("S")) {

            tipo = descriptor.getDescripcion(1, "gEtiq_REGSalidaExterna") + " " + request.getParameter("fechaDesde").substring(0, 10) + " " + descriptor.getDescripcion(1, "gEtiq_HASTA") + " " + request.getParameter("fechaHasta").substring(0, 10);
            desOri = descriptor.getDescripcion(1, "gEtiq_ORIGEN");
        }
        String nombre = request.getParameter("nombre");
        String escudo = request.getParameter("escudo");
        tipo = StringEscapeUtils.escapeSql(tipo);
        String sql = "SELECT DISTINCT '" + tipo + "' AS TIP,'" + desOri + "' AS DOR,'";
        if (escudo != null) {
            sql += usuario.getOrgIco().substring(1);
        }
        sql += "' AS IMG,'";
        if (nombre != null) {
            sql += usuario.getEnt();
        }

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        sql += "' AS ENT," + oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, new String[]{" ORGEX_DES ", "' : '", " UOREX_NOM " }) + " AS UOR," + num + " AS"
               + " OPC FROM " +
               GlobalNames.ESQUEMA_GENERICO + "A_UOREX A_UOREX LEFT JOIN " +
               GlobalNames.ESQUEMA_GENERICO + "A_ORGEX A_ORGEX ON UOREX_ORG = ORGEX_COD WHERE UOREX_ORG =" + codOrganizacion + " AND UOREX_COD=" + codigoUnidad;

       
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("encabezadoRegistro UOR EXTERNA: " + sql);
        }

        File xmlEncabezado;
        if (request.getParameter("tipo").equalsIgnoreCase("E")) {
            xmlEncabezado = pdf.construyeTabla(sql, "encabezadoRegistroUorE_Externa_" + claveIdioma);
        } else {
            xmlEncabezado = pdf.construyeTabla(sql, "encabezadoRegistroUorS_" + claveIdioma);
        }
        return xmlEncabezado;
    }


    /**
     * Función auxiliar encargada de seleccionar la conculta especificada por el
     * parámetro t
     *
     * @param fecha Parámetro de la consulta
     * @param t     Selector de la consulta
     * @return La sql requerida
     */
    private String consulta(String fecha, int t, String codigoUnidad) {
        String sql = "";
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        switch (t) {
            case 0:
            case 2:
                String concat[] = {conf.getString("SQL.T_TVI.abreviatura"), "' '"};
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_VIA.nombreVia");
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                String[] nulo = {oad.convertir(conf.getString("SQL.T_DSU.numeroDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null), "''"};
                concat[1] = oad.convertir(nulo[0], AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DSU.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DSU.numeroHasta");
                concat[1] = oad.convertir(nulo[0], AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DSU.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                String parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                        concat);
                String parteWhere = conf.getString("SQL.T_DPO.domicilio") + " = " +
                        conf.getString("SQL.R_RES.domicTercero");
                String[] joins = new String[14];
                joins[0] = "T_DPO";
                joins[1] = "INNER";
                joins[2] = "T_DSU";
                joins[3] = conf.getString("SQL.T_DPO.suelo") + " = " +
                        conf.getString("SQL.T_DSU.identificador");
                joins[4] = "INNER";
                joins[5] = "T_VIA";
                joins[6] = conf.getString("SQL.T_DSU.pais") + " = " +
                        conf.getString("SQL.T_VIA.pais") + " AND " + conf.getString("S" +
                        "QL.T_DSU.provincia") + " = " + conf.getString("SQL.T_VIA.prov" +
                        "incia") + " AND " + conf.getString("SQL.T_DSU.municipio") +
                        " = " + conf.getString("SQL.T_VIA.municipio") + " AND " +
                        conf.getString("SQL.T_DSU.vial") + " = " + conf.getString("SQL" +
                        ".T_VIA.identificador");
                joins[7] = "INNER";
                joins[8] = "T_TVI";
                joins[9] = conf.getString("SQL.T_VIA.tipo") + " = " +
                        conf.getString("SQL.T_TVI.codigo");
                joins[10] = "INNER";
                joins[11] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
                joins[12] = conf.getString("SQL.T_DSU.pais") + " = " +
                        conf.getString("SQL.T_MUN.idPais") + " AND " +
                        conf.getString("SQL.T_DSU.provincia") + " = " +
                        conf.getString("SQL.T_MUN.idProvincia") + " AND " +
                        conf.getString("SQL.T_DSU.municipio") + " = " +
                        conf.getString("SQL.T_MUN.idMunicipio");
                joins[13] = "false";
                String calle1 = "''";
                try {
                    calle1 = oad.join(parteFrom, parteWhere, joins);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en calle1: " + bde.toString());
                    }
                }
                concat[0] = conf.getString("SQL.T_TVI.abreviatura");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_VIA.nombreVia");
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.convertir(nulo[0], AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.convertir(nulo[0], AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                parteWhere = conf.getString("SQL.T_DNN.idDomicilio") + " = " +
                        conf.getString("SQL.R_RES.domicTercero");
                joins = new String[11];
                joins[0] = "T_DNN";
                joins[1] = "INNER";
                joins[2] = "T_VIA";
                joins[3] = conf.getString("SQL.T_DNN.idPaisDVia") + " = " +
                        conf.getString("SQL.T_VIA.pais") + " AND " + conf.getString("S" +
                        "QL.T_DNN.idProvinciaDVia") + " = " + conf.getString("SQL.T_VI" +
                        "A.provincia") + " AND " + conf.getString("SQL.T_DNN.idMunicip" +
                        "ioDVia") + " = " + conf.getString("SQL.T_VIA.municipio") + " " +
                        "AND " + conf.getString("SQL.T_DNN.codigoVia") + " = " +
                        conf.getString("SQL.T_VIA.identificador");
                joins[4] = "INNER";
                joins[5] = "T_TVI";
                joins[6] = conf.getString("SQL.T_VIA.tipo") + " = " +
                        conf.getString("SQL.T_TVI.codigo");
                joins[7] = "INNER";
                joins[8] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
                joins[9] = conf.getString("SQL.T_DNN.idPaisD") + " = " +
                        conf.getString("SQL.T_MUN.idPais") + " AND " +
                        conf.getString("SQL.T_DNN.idProvinciaD") + " = " +
                        conf.getString("SQL.T_MUN.idProvincia") + " AND " +
                        conf.getString("SQL.T_DNN.idMunicipioD") + " = " +
                        conf.getString("SQL.T_MUN.idMunicipio");
                joins[10] = "false";
                String calle2 = "''";
                try {
                    calle2 = oad.join(parteFrom, parteWhere, joins);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en calle2: " + bde.toString());
                    }
                }
                concat[0] = conf.getString("SQL.T_TVI.abreviatura");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = oad.convertir(conf.getString("SQL.T_DNN.domicilio"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.numDesde");
                concat[1] = oad.convertir(nulo[0], AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.numHasta");
                concat[1] = oad.convertir(nulo[0], AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                parteWhere = conf.getString("SQL.T_DNN.idDomicilio") + " = " +
                        conf.getString("SQL.R_RES.domicTercero");
                joins = new String[8];
                joins[0] = "T_DNN";
                joins[1] = "INNER";
                joins[2] = "T_TVI";
                joins[3] = conf.getString("SQL.T_DNN.idTipoVia") + " = " +
                        conf.getString("SQL.T_TVI.codigo");
                joins[4] = "INNER";
                joins[5] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
                joins[6] = conf.getString("SQL.T_DNN.idPaisD") + " = " +
                        conf.getString("SQL.T_MUN.idPais") + " AND " +
                        conf.getString("SQL.T_DNN.idProvinciaD") + " = " +
                        conf.getString("SQL.T_MUN.idProvincia") + " AND " +
                        conf.getString("SQL.T_DNN.idMunicipioD") + " = " +
                        conf.getString("SQL.T_MUN.idMunicipio");
                joins[7] = "false";
                String calle3 = "''";
                try {
                    calle3 = oad.join(parteFrom, parteWhere, joins);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en calle3: " + bde.toString());
                    }
                }
                concat[0] = oad.convertir(conf.getString("SQL.T_DNN.domicilio"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.numDesde");
                concat[1] = oad.convertir(nulo[0], AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
                concat[1] = oad.convertir(nulo[0], AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.numHasta");
                concat[1] = oad.convertir(nulo[0], AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                String calle4 = "SELECT " + oad.funcionCadena(
                        AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat) + " AS CALLE4 FROM T_DNN," + GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN WHERE " +
                        conf.getString("SQL.T_DNN.idDomicilio") + " = " +
                        conf.getString("SQL.R_RES.domicTercero") + " AND " +
                        conf.getString("SQL.T_DNN.idPaisD") + " = " +
                        conf.getString("SQL.T_MUN.idPais") + " AND " +
                        conf.getString("SQL.T_DNN.idProvinciaD") + " = " +
                        conf.getString("SQL.T_MUN.idProvincia") + " AND " +
                        conf.getString("SQL.T_DNN.idMunicipioD") + " = " +
                        conf.getString("SQL.T_MUN.idMunicipio");

                parteFrom = oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO,";
                concat[0] = conf.getString("SQL.T_HTE.apellido1");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_HTE.apellido2");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                parteFrom += oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat) +
                        " AS APELLIDOS,";
                concat[0] = conf.getString("SQL.T_HTE.nombre");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_HTE.documento");
                parteFrom += oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat) +
                        " AS REMITENTE," + conf.getString("SQL.A_UOR.nombre") + " AS D" +
                        "ESTINO, " + conf.getString("SQL.R_RES.estAnot") + " AS ESTADO" +
                        "," + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA" +
                        ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,(" +
                        calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4";

                parteWhere = conf.getString("SQL.R_RES.tipoReg") + " = '" +
                        request.getParameter("tipo") + "' AND " + oad.convertir(
                        conf.getString("SQL.R_RES.fechaAnotacion"),
                        AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " = '" + fecha +
                        "'" +/* " AND " + conf.getString("SQL.R_RES.codDpto") + " = " +
                        usuarioR.getDepCod() +*/ " AND " + conf.getString("SQL.R_RES.cod" +
                        "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND (" +
                        conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=0 OR " +
                        conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=2)";
                if (t == 0) {
                    parteWhere += " AND " + conf.getString("SQL.R_RES.numeroAnotac" +
                            "ion") + " >= " + request.getParameter("numDesde");
                } else if (t == 2) {
                    if (!codigoUnidad.equals("")) {
                        parteWhere += " AND " + conf.getString("SQL.R_RES.unidOrigD" +
                                "est") + " = " + codigoUnidad;
                    } //request.getParameter("txtCodigoUnidad");
                }
                joins = new String[8];
                joins[0] = "R_RES";
                joins[1] = "INNER";
                joins[2] = "T_HTE";
                joins[3] = conf.getString("SQL.R_RES.codTercero") + " = " +
                        conf.getString("SQL.T_HTE.identificador") + " AND " +
                        conf.getString("SQL.R_RES.modifInteresado") + " = " +
                        conf.getString("SQL.T_HTE.version");
                joins[4] = "LEFT";
                joins[5] = "A_UOR";
                joins[6] = conf.getString("SQL.R_RES.unidOrigDest") + " = " +
                        conf.getString("SQL.A_UOR.codigo");
                joins[7] = "false";


                String parteFrom1 = oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO,";
                concat[0] = conf.getString("SQL.T_HTE.apellido1");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_HTE.apellido2");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                parteFrom1 += oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat) +
                        " AS APELLIDOS,";
                concat[0] = conf.getString("SQL.T_HTE.nombre");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_HTE.documento");
                parteFrom1 += oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat) +
                        " AS REMITENTE," + conf.getString("SQL.A_ORGEX.descripcion") + " AS D" +
                        "ESTINO, " + conf.getString("SQL.R_RES.estAnot") + " AS ESTADO" +
                        "," + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA" +
                        ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,(" +
                        calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4";
                String parteWhere1 = conf.getString("SQL.R_RES.tipoReg") + " = '" +
                        request.getParameter("tipo") + "' AND " + oad.convertir(
                        conf.getString("SQL.R_RES.fechaAnotacion"),
                        AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " = '" + fecha +
                        "' AND " + conf.getString("SQL.R_RES.cod" +
                        "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND " +
                        conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=1";
                if (t == 0) {
                    parteWhere1 += " AND " + conf.getString("SQL.R_RES.numeroAnotac" +
                            "ion") + " >= " + request.getParameter("numDesde");
                } else if (t == 2) {
                    if (!codigoUnidad.equals("")) {
                        parteWhere1 += " AND " + conf.getString("SQL.R_RES.unidOrigD" +
                                "est") + " = " + codigoUnidad;
                    }
                }
                String[] joins1 = new String[8];
                joins1[0] = "R_RES";
                joins1[1] = "INNER";
                joins1[2] = "T_HTE";
                joins1[3] = conf.getString("SQL.R_RES.codTercero") + " = " +
                        conf.getString("SQL.T_HTE.identificador") + " AND " +
                        conf.getString("SQL.R_RES.modifInteresado") + " = " +
                        conf.getString("SQL.T_HTE.version");
                joins1[4] = "LEFT";
                joins1[5] = GlobalNames.ESQUEMA_GENERICO + "A_ORGEX A_ORGEX";
                joins1[6] = conf.getString("SQL.R_RES.orgDestAnot") + " = " +
                        conf.getString("SQL.A_ORGEX.codigo");
                joins1[7] = "false";


                sql = "";
                try {
                    sql = oad.join(parteFrom, parteWhere, joins);
                    sql += " UNION ";
                    sql += oad.join(parteFrom1, parteWhere1, joins1);
                    String[] order = {"NUM", "4"};
                    sql += oad.orderUnion(order);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en el join: " + bde.toString());
                    }
                }
                break;
            case 1:
                sql = "SELECT " + oad.convertir(conf.getString("SQL.R_DIL.fecha"),
                        AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," +
                        conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION FROM R_" +
                        "DIL WHERE " + conf.getString("SQL.R_DIL.cod" +
                        "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND " +
                        conf.getString("SQL.R_DIL.tipo") + " = '" + request.getParameter(
                        "tipo") + "' AND " + oad.convertir(conf.getString("SQL.R_DIL.fecha"),
                        AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " = '" +
                        fecha + "'";
                break;

        }
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("sql :" + sql);
        }
        return sql;
    }
    /**
     * Funcion auxiliar se emplea para modificar la consulta cuando seleccionamos alguna anotacion
     */
     private String consulta2(String consulta, String anotaciones){
         String sql="";
         String where="";
         if(anotaciones!=null && anotaciones!=""){
             String[] num=anotaciones.split(";");
             sql="SELECT * FROM ("+consulta+") WHERE ";
             
             for(int i=0; i<num.length; i++){
                 if(i!=num.length-1){
                    where+=("NUM='"+num[i]+"' OR ");
                 }else{
                     where+=("NUM='"+num[i]+"'");
                 }
             }
         }
         return sql+where;
     }
    /**
     * Función auxiliar encargada de seleccionar la conculta especificada por el
     * parámetro t
     *
     * @param fecha Parámetro de la consulta
     * @param t     Selector de la consulta
     * @param unidadesExternas boolean que indicara si la consulta se debe adaptar para obtener la informacion de las unidades externas
     * @return La sql requerida
     */
    private String consulta(int t, String codigoUnidad, String desde, String hasta, boolean orden, String ejercicio, boolean unidadesExternas, String codOrganizacion) {
        String sql = "";
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        String nombreLargo = " ";
        nombreLargo = opcionint;
        
        String directivaUsuPermisoUor= "NO";
        
        
         if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuario.getIdUsuario(), params)) {                
             directivaUsuPermisoUor= "SI";    
             
         }

        switch (t) {
            case 0:
            case 2:
                String concat[] = {conf.getString("SQL.T_TVI.abreviatura"), "' '"};
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_VIA.nombreVia");
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                String[] nulo = {oad.convertir(conf.getString("SQL.T_DSU.numeroDesde"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null), "''"};
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DSU.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DSU.numeroHasta"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DSU.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                String parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                        concat);
                String parteWhere = conf.getString("SQL.T_DPO.domicilio") + " = "
                        + conf.getString("SQL.R_RES.domicTercero");
                String[] joins = new String[14];
                joins[0] = "T_DPO";
                joins[1] = "INNER";
                joins[2] = "T_DSU";
                joins[3] = conf.getString("SQL.T_DPO.suelo") + " = "
                        + conf.getString("SQL.T_DSU.identificador");
                joins[4] = "INNER";
                joins[5] = "T_VIA";
                joins[6] = conf.getString("SQL.T_DSU.pais") + " = "
                        + conf.getString("SQL.T_VIA.pais") + " AND " + conf.getString("S"
                        + "QL.T_DSU.provincia") + " = " + conf.getString("SQL.T_VIA.prov"
                        + "incia") + " AND " + conf.getString("SQL.T_DSU.municipio")
                        + " = " + conf.getString("SQL.T_VIA.municipio") + " AND "
                        + conf.getString("SQL.T_DSU.vial") + " = " + conf.getString("SQL"
                        + ".T_VIA.identificador");
                joins[7] = "INNER";
                joins[8] = "T_TVI";
                joins[9] = conf.getString("SQL.T_VIA.tipo") + " = "
                        + conf.getString("SQL.T_TVI.codigo");
                joins[10] = "INNER";
                joins[11] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
                joins[12] = conf.getString("SQL.T_DSU.pais") + " = "
                        + conf.getString("SQL.T_MUN.idPais") + " AND "
                        + conf.getString("SQL.T_DSU.provincia") + " = "
                        + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                        + conf.getString("SQL.T_DSU.municipio") + " = "
                        + conf.getString("SQL.T_MUN.idMunicipio");
                joins[13] = "false";
                String calle1 = "''";
                try {
                    calle1 = oad.join(parteFrom, parteWhere, joins);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en calle1: " + bde.toString());
                    }
                }
                concat[0] = conf.getString("SQL.T_TVI.abreviatura");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_VIA.nombreVia");
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                parteWhere = conf.getString("SQL.T_DNN.idDomicilio") + " = "
                        + conf.getString("SQL.R_RES.domicTercero");
                joins = new String[11];
                joins[0] = "T_DNN";
                joins[1] = "INNER";
                joins[2] = "T_VIA";
                joins[3] = conf.getString("SQL.T_DNN.idPaisDVia") + " = "
                        + conf.getString("SQL.T_VIA.pais") + " AND " + conf.getString("S"
                        + "QL.T_DNN.idProvinciaDVia") + " = " + conf.getString("SQL.T_VI"
                        + "A.provincia") + " AND " + conf.getString("SQL.T_DNN.idMunicip"
                        + "ioDVia") + " = " + conf.getString("SQL.T_VIA.municipio") + " "
                        + "AND " + conf.getString("SQL.T_DNN.codigoVia") + " = "
                        + conf.getString("SQL.T_VIA.identificador");
                joins[4] = "INNER";
                joins[5] = "T_TVI";
                joins[6] = conf.getString("SQL.T_VIA.tipo") + " = "
                        + conf.getString("SQL.T_TVI.codigo");
                joins[7] = "INNER";
                joins[8] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
                joins[9] = conf.getString("SQL.T_DNN.idPaisD") + " = "
                        + conf.getString("SQL.T_MUN.idPais") + " AND "
                        + conf.getString("SQL.T_DNN.idProvinciaD") + " = "
                        + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                        + conf.getString("SQL.T_DNN.idMunicipioD") + " = "
                        + conf.getString("SQL.T_MUN.idMunicipio");
                joins[10] = "false";
                String calle2 = "''";
                try {
                    calle2 = oad.join(parteFrom, parteWhere, joins);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en calle2: " + bde.toString());
                    }
                }
                concat[0] = conf.getString("SQL.T_TVI.abreviatura");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_DNN.domicilio");
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                parteWhere = conf.getString("SQL.T_DNN.idDomicilio") + " = "
                        + conf.getString("SQL.R_RES.domicTercero");
                joins = new String[8];
                joins[0] = "T_DNN";
                joins[1] = "INNER";
                joins[2] = "T_TVI";
                joins[3] = conf.getString("SQL.T_DNN.idTipoVia") + " = "
                        + conf.getString("SQL.T_TVI.codigo");
                joins[4] = "INNER";
                joins[5] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
                joins[6] = conf.getString("SQL.T_DNN.idPaisD") + " = "
                        + conf.getString("SQL.T_MUN.idPais") + " AND "
                        + conf.getString("SQL.T_DNN.idProvinciaD") + " = "
                        + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                        + conf.getString("SQL.T_DNN.idMunicipioD") + " = "
                        + conf.getString("SQL.T_MUN.idMunicipio");
                joins[7] = "false";
                String calle3 = "''";
                try {
                    calle3 = oad.join(parteFrom, parteWhere, joins);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en calle3: " + bde.toString());
                    }
                }
                concat[0] = conf.getString("SQL.T_DNN.domicilio");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                String calle4 = "SELECT " + oad.funcionCadena(
                        AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat) + " AS CALLE4 FROM T_DNN," + GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN WHER"
                        + "E " + conf.getString("SQL.T_DNN.idDomicilio") + " = "
                        + conf.getString("SQL.R_RES.domicTercero") + " AND "
                        + conf.getString("SQL.T_DNN.idPaisD") + " = "
                        + conf.getString("SQL.T_MUN.idPais") + " AND "
                        + conf.getString("SQL.T_DNN.idProvinciaD") + " = "
                        + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                        + conf.getString("SQL.T_DNN.idMunicipioD") + " = "
                        + conf.getString("SQL.T_MUN.idMunicipio");

                parteFrom =
                        oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAANOTACION," + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION, " + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS FECHA_ORDE," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO,";
                parteFrom += "HTE_NOM AS NOMBRE, HTE_AP1 AS APELLIDO1, HTE_AP2 AS APELLIDO2, HTE_DOC AS DOCUMENTO, ";

                if (unidadesExternas) {
                    parteFrom += " UOREX_NOM AS DESTINO, ";
                } else {
                    parteFrom += conf.getString("SQL.A_UOR.nombre") + " AS DESTINO, ";
                }
                
                parteFrom += conf.getString("SQL.R_RES.estAnot") + " AS ESTADO"
                        + "," + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA"
                        + ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,("
                        + calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4,"
                        + "'" + nombreLargo + "' AS TIPONOMBRE ";

                parteWhere = conf.getString("SQL.R_RES.tipoReg") + " = '"
                        + request.getParameter("tipo") + "' " + " AND " + conf.getString("SQL.R_RES.cod"
                        + "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND (" ;

                if (unidadesExternas){ // solo nos interesan los registros de tipo 1: destino otra administracion
                    parteWhere +=  conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=1)"
                        + " AND " + conf.getString("SQL.R_RES.ejercicio") + "=" + ejercicio;
                }else{
                    parteWhere += conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=0 OR "
                        + conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=2)"
                        + " AND " + conf.getString("SQL.R_RES.ejercicio") + "=" + ejercicio;
                }
                       

                if ((desde != null) && (!desde.equals(""))) {
                    parteWhere += " AND RES_NUM >= " + desde;
                }
                if ((hasta != null) && (!hasta.equals(""))) {
                    parteWhere += " AND RES_NUM <= " + hasta;
                }


                if (t == 0) {
                    parteWhere += " AND " + conf.getString("SQL.R_RES.numeroAnotac"
                            + "ion") + " >= " + request.getParameter("numDesde");
                } else if (t == 2) {
                     if (!codigoUnidad.equals("")) { // tenemos que tener en cuenta las unidades externas
                        if (unidadesExternas) {
                            parteWhere += " AND RES_UCD = " + codigoUnidad + " AND RES_OCD = " + codOrganizacion; // unidad destino externa
                        } else {
                            parteWhere += " AND " + conf.getString("SQL.R_RES.unidOrigD"
                                    + "est") + " = " + codigoUnidad;
                             
                        }
                    }
                }
                
                if (("SI".equals(directivaUsuPermisoUor)) && ("S".equals((String) request.getParameter("tipo")))) {
                    parteWhere += " AND RES_UOD in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE"
                            + " UOU_USU=" + usuario.getIdUsuario() + " AND UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod() + ")";
                }

                joins = new String[11];
                joins[0] = "R_RES";
                joins[1] = "INNER";
                joins[2] = "T_HTE";
                joins[3] = conf.getString("SQL.R_RES.codTercero") + " = "
                        + conf.getString("SQL.T_HTE.identificador") + " AND "
                        + conf.getString("SQL.R_RES.modifInteresado") + " = "
                        + conf.getString("SQL.T_HTE.version");
                joins[4] = "LEFT";
                
                if (unidadesExternas) { // si se va imprimir un informe de las unidades externas necesitamos consultar la tabla A_UOREX
                    joins[5] = GlobalNames.ESQUEMA_GENERICO + "A_UOREX A_UOREX";
                    joins[6] = " RES_UCD = UOREX_COD AND RES_OCD = UOREX_ORG ";

                } else {
                    joins[5] = "A_UOR";
                    joins[6] = conf.getString("SQL.R_RES.unidOrigDest") + " = "
                            + conf.getString("SQL.A_UOR.codigo");
                }

                joins[7] = "LEFT";
                joins[8] = "R_DIL";
                if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
                    joins[9] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                            + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                            + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                            + oad.convertir(oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null);
                } else {
                    joins[9] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                            + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                            + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                            + conf.getString("SQL.R_DIL.fecha");
                }
                joins[10] = "false";


                String parteFrom1 =
                        oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAANOTACION," + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION, " + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS FECHA_ORDE," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO";
                parteFrom1 += "HTE_NOM AS NOMBRE, HTE_AP1 AS APELLIDO1, HTE_AP2 AS APELLIDO2, HTE_DOC AS DOCUMENTO, "
                        + conf.getString("SQL.R_RES.estAnot") + " AS ESTADO,"
                        + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA"
                        + ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,("
                        + calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4,"
                        + "'" + nombreLargo + "' AS TIPONOMBRE ";
                String parteWhere1 = conf.getString("SQL.R_RES.tipoReg") + " = '"
                        + request.getParameter("tipo") + "' " + " AND " + conf.getString("SQL.R_RES.cod"
                        + "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND "
                        + conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=1"
                        + " AND " + conf.getString("SQL.R_RES.ejercicio") + "=" + ejercicio;
                if ((desde != null) && (!desde.equals(""))) {
                    parteWhere1 += " AND RES_NUM >= " + desde;
                }
                if ((hasta != null) && (!hasta.equals(""))) {
                    parteWhere1 += " AND RES_NUM <= " + hasta;
                }

                if (t == 0) {
                    parteWhere1 += " AND " + conf.getString("SQL.R_RES.numeroAnotac"
                            + "ion") + " >= " + request.getParameter("numDesde");
                } else if (t == 2) {
                     if (!codigoUnidad.equals("")) { // tenemos que tener en cuenta las unidades externas
                        if (unidadesExternas ) {
                            parteWhere1 += " AND RES_UCD = " + codigoUnidad + " AND RES_OCD = " + codOrganizacion; // unidad y org destino externa
                        } else {
                            parteWhere1 += " AND " + conf.getString("SQL.R_RES.unidOrigD"
                                    + "est") + " = " + codigoUnidad;
                            
                        }
                    }                                        
                }
                
                 if (("SI".equals(directivaUsuPermisoUor)) && ("S".equals((String) request.getParameter("tipo")))) {
                    parteWhere1 += " AND RES_UOD in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE"
                            + " UOU_USU=" + usuario.getIdUsuario() + " AND UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod() + ")";
                }
                
                String[] joins1 = new String[11];
                if (unidadesExternas) {
                    joins1 = new String[14];
                }
                joins1[0] = "R_RES";
                joins1[1] = "INNER";
                joins1[2] = "T_HTE";
                joins1[3] = conf.getString("SQL.R_RES.codTercero") + " = "
                        + conf.getString("SQL.T_HTE.identificador") + " AND "
                        + conf.getString("SQL.R_RES.modifInteresado") + " = "
                        + conf.getString("SQL.T_HTE.version");
                joins1[4] = "LEFT";     
                joins1[5] = GlobalNames.ESQUEMA_GENERICO + "A_ORGEX A_ORGEX";
                joins1[6] = conf.getString("SQL.R_RES.orgDestAnot") + " = "
                        + conf.getString("SQL.A_ORGEX.codigo");
                
                                
                if (unidadesExternas) { // si se va imprimir un informe de las unidades externas necesitamos consultar la tabla A_UOREX
                    joins1[7] = "LEFT";
                    joins1[8] = GlobalNames.ESQUEMA_GENERICO + "A_UOREX A_UOREX";
                    joins1[9] = " RES_UCD = UOREX_COD AND RES_OCD = UOREX_ORG ";

                    joins1[10] = "LEFT";
                    joins1[11] = "R_DIL";
                    if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
                        joins1[12] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                                + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                                + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                                + oad.convertir(oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null);
                    } else {
                        joins1[12] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                                + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                                + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                                + conf.getString("SQL.R_DIL.fecha");
                    }
                    joins1[13] = "false";
                    
                }else{
                    joins1[7] = "LEFT";
                    joins1[8] = "R_DIL";
                    if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
                        joins1[9] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                                + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                                + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                                + oad.convertir(oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null);
                    } else {
                        joins1[9] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                                + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                                + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                                + conf.getString("SQL.R_DIL.fecha");
                    }
                    joins1[10] = "false";
                }

                sql = "";
                try {
                    sql = oad.join(parteFrom, parteWhere, joins);
                    if (!orden) {
                        String[] order = {"NUM", "4"};
                        sql += oad.orderUnion(order);
                    } else {
                        String[] order = {"FECHA_ORDE", "2","HORA","3", "NUM", "4"};
                        sql += oad.orderUnion(order);
                    }
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en el join: " + bde.toString());
                    }
                }
                break;
            case 1:
                sql = "SELECT " + oad.convertir(conf.getString("SQL.R_DIL.fecha"),
                        AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA,"
                        + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION FROM R_DIL WHERE " //+ conf.getString("SQL.R_DIL.codDepto") + " = " + usuarioR.getDepCod()  + " AND "
                        + conf.getString("SQL.R_DIL.codUnidad") + " = " + usuarioR.getUnidadOrgCod()
                        + " AND " + conf.getString("SQL.R_DIL.tipo") + " = '" + request.getParameter("tipo") + "'";


                break;

        }
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("sql consulta en InformesRegsitroAction (1617) :" + sql);
        }
        return sql;
    }
    private String consulta_fecha2(String consulta, String anotaciones){
        String sql="";
         String where="";
         if(anotaciones!=null && anotaciones!=""){
             String[] num=anotaciones.split(";");
             sql="SELECT * FROM ("+consulta+") WHERE ";
             
             for(int i=0; i<num.length; i++){
                 if(i!=num.length-1){
                    where+=("NUM='"+num[i]+"' OR ");
                 }else{
                     where+=("NUM='"+num[i]+"'");
                 }
             }
         }
         return sql+where;
    }

    /**
     * Función auxiliar encargada de seleccionar la conculta especificada por el
     * parámetro t
     *
     * @param fecha Parámetro de la consulta
     * @param t     Selector de la consulta
     * @param unidadesExternas boolean que indicara si la consulta se debe adaptar para obtener la informacion de las unidades externas
     * @return La sql requerida
     */
    private String consulta_fecha(int t, String codigoUnidad, String fecha_desde, String fecha_hasta, boolean orden,
            String ejercicio, boolean unidadesExternas, String codOrganizacion) {
        m_Log.debug("ENTRO AKI--> consulta_fecha");
        String sql = "";
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        String nombreLargo = " ";
        nombreLargo = opcionint;//confReg.getString("Registro/LibroRegistro/Encabezado");
        
        String directivaUsuPermisoUor= "NO";
       
        
         if (UsuariosGruposManager.getInstance().tienePermisoDirectiva(ConstantesDatos.REGISTRO_S_SOLO_UORS_USUARIO, usuario.getIdUsuario(), params)) {                
             directivaUsuPermisoUor= "SI";    
             
         }
        
        
        
        switch (t) {
            case 0:
            case 2:
                String concat[] = {conf.getString("SQL.T_TVI.abreviatura"), "' '"};
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_VIA.nombreVia");
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                String[] nulo = {oad.convertir(conf.getString("SQL.T_DSU.numeroDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null), "''"};
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DSU.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DSU.numeroHasta"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DSU.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                String parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT,
                        concat);
                String parteWhere = conf.getString("SQL.T_DPO.domicilio") + " = "
                        + conf.getString("SQL.R_RES.domicTercero");
                String[] joins = new String[14];
                joins[0] = "T_DPO";
                joins[1] = "INNER";
                joins[2] = "T_DSU";
                joins[3] = conf.getString("SQL.T_DPO.suelo") + " = "
                        + conf.getString("SQL.T_DSU.identificador");
                joins[4] = "INNER";
                joins[5] = "T_VIA";
                joins[6] = conf.getString("SQL.T_DSU.pais") + " = "
                        + conf.getString("SQL.T_VIA.pais") + " AND " + conf.getString("S"
                        + "QL.T_DSU.provincia") + " = " + conf.getString("SQL.T_VIA.prov"
                        + "incia") + " AND " + conf.getString("SQL.T_DSU.municipio")
                        + " = " + conf.getString("SQL.T_VIA.municipio") + " AND "
                        + conf.getString("SQL.T_DSU.vial") + " = " + conf.getString("SQL"
                        + ".T_VIA.identificador");
                joins[7] = "INNER";
                joins[8] = "T_TVI";
                joins[9] = conf.getString("SQL.T_VIA.tipo") + " = "
                        + conf.getString("SQL.T_TVI.codigo");
                joins[10] = "INNER";
                joins[11] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
                joins[12] = conf.getString("SQL.T_DSU.pais") + " = "
                        + conf.getString("SQL.T_MUN.idPais") + " AND "
                        + conf.getString("SQL.T_DSU.provincia") + " = "
                        + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                        + conf.getString("SQL.T_DSU.municipio") + " = "
                        + conf.getString("SQL.T_MUN.idMunicipio");
                joins[13] = "false";
                String calle1 = "''";
                try {
                    calle1 = oad.join(parteFrom, parteWhere, joins);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en calle1: " + bde.toString());
                    }
                }
                concat[0] = conf.getString("SQL.T_TVI.abreviatura");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_VIA.nombreVia");
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                parteWhere = conf.getString("SQL.T_DNN.idDomicilio") + " = "
                        + conf.getString("SQL.R_RES.domicTercero");
                joins = new String[11];
                joins[0] = "T_DNN";
                joins[1] = "INNER";
                joins[2] = "T_VIA";
                joins[3] = conf.getString("SQL.T_DNN.idPaisDVia") + " = "
                        + conf.getString("SQL.T_VIA.pais") + " AND " + conf.getString("S"
                        + "QL.T_DNN.idProvinciaDVia") + " = " + conf.getString("SQL.T_VI"
                        + "A.provincia") + " AND " + conf.getString("SQL.T_DNN.idMunicip"
                        + "ioDVia") + " = " + conf.getString("SQL.T_VIA.municipio") + " "
                        + "AND " + conf.getString("SQL.T_DNN.codigoVia") + " = "
                        + conf.getString("SQL.T_VIA.identificador");
                joins[4] = "INNER";
                joins[5] = "T_TVI";
                joins[6] = conf.getString("SQL.T_VIA.tipo") + " = "
                        + conf.getString("SQL.T_TVI.codigo");
                joins[7] = "INNER";
                joins[8] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
                joins[9] = conf.getString("SQL.T_DNN.idPaisD") + " = "
                        + conf.getString("SQL.T_MUN.idPais") + " AND "
                        + conf.getString("SQL.T_DNN.idProvinciaD") + " = "
                        + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                        + conf.getString("SQL.T_DNN.idMunicipioD") + " = "
                        + conf.getString("SQL.T_MUN.idMunicipio");
                joins[10] = "false";
                String calle2 = "''";
                try {
                    calle2 = oad.join(parteFrom, parteWhere, joins);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en calle2: " + bde.toString());
                    }
                }
                concat[0] = conf.getString("SQL.T_TVI.abreviatura");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_DNN.domicilio");
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                parteFrom = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                parteWhere = conf.getString("SQL.T_DNN.idDomicilio") + " = "
                        + conf.getString("SQL.R_RES.domicTercero");
                joins = new String[8];
                joins[0] = "T_DNN";
                joins[1] = "INNER";
                joins[2] = "T_TVI";
                joins[3] = conf.getString("SQL.T_DNN.idTipoVia") + " = "
                        + conf.getString("SQL.T_TVI.codigo");
                joins[4] = "INNER";
                joins[5] = GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN";
                joins[6] = conf.getString("SQL.T_DNN.idPaisD") + " = "
                        + conf.getString("SQL.T_MUN.idPais") + " AND "
                        + conf.getString("SQL.T_DNN.idProvinciaD") + " = "
                        + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                        + conf.getString("SQL.T_DNN.idMunicipioD") + " = "
                        + conf.getString("SQL.T_MUN.idMunicipio");
                joins[7] = "false";
                String calle3 = "''";
                try {
                    calle3 = oad.join(parteFrom, parteWhere, joins);
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en calle3: " + bde.toString());
                    }
                }
                concat[0] = conf.getString("SQL.T_DNN.domicilio");
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numDesde"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraDesde");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = oad.convertir(conf.getString("SQL.T_DNN.numHasta"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, null);
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                nulo[0] = conf.getString("SQL.T_DNN.letraHasta");
                concat[1] = oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, nulo);

                // CONCATENAR LA POBLACION
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = "' '";
                concat[0] = oad.funcionCadena(AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat);
                concat[1] = conf.getString("SQL.T_MUN.nombre");

                String calle4 = "SELECT " + oad.funcionCadena(
                        AdaptadorSQLBD.FUNCIONCADENA_CONCAT, concat) + " AS CALLE4 FROM T_DNN," + GlobalNames.ESQUEMA_GENERICO + "T_MUN T_MUN WHER"
                        + "E " + conf.getString("SQL.T_DNN.idDomicilio") + " = "
                        + conf.getString("SQL.R_RES.domicTercero") + " AND "
                        + conf.getString("SQL.T_DNN.idPaisD") + " = "
                        + conf.getString("SQL.T_MUN.idPais") + " AND "
                        + conf.getString("SQL.T_DNN.idProvinciaD") + " = "
                        + conf.getString("SQL.T_MUN.idProvincia") + " AND "
                        + conf.getString("SQL.T_DNN.idMunicipioD") + " = "
                        + conf.getString("SQL.T_MUN.idMunicipio");

                parteFrom =
                        oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAANOTACION," + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION, " + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS FECHA_ORDE," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO,";
                parteFrom += "HTE_NOM AS NOMBRE, HTE_AP1 AS APELLIDO1, HTE_AP2 AS APELLIDO2, HTE_DOC AS DOCUMENTO, ";

                if (unidadesExternas) {
                    parteFrom += " UOREX_NOM AS DESTINO, ";
                } else {
                    parteFrom += conf.getString("SQL.A_UOR.nombre") + " AS DESTINO, ";
                }
                parteFrom += conf.getString("SQL.R_RES.estAnot") + " AS ESTADO"
                        + "," + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA"
                        + ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,("
                        + calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4," + oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{conf.getString("SQL.R_TTR.descripcion"), "'-'"}) + "as TRANSPORTE, "
                        + "'" + nombreLargo + "' AS TIPONOMBRE";

                parteWhere = conf.getString("SQL.R_RES.tipoReg") + " = '"
                        + request.getParameter("tipo") + "' " + " AND " + conf.getString("SQL.R_RES.cod"
                        + "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND ("
                        + conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=0 OR "
                        + conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=2)"
                        + " AND " + conf.getString("SQL.R_RES.ejercicio") + "=" + ejercicio;

                if ((fecha_desde != null) && (!fecha_desde.equals(""))) {
                    parteWhere += " AND "
                            + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + " >= "
                            + oad.convertir("'" + fecha_desde + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null);
                }

                if ((fecha_hasta != null) && (!fecha_hasta.equals(""))) {
                    parteWhere += " AND "
                            + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + " <= "
                            + oad.convertir("'" + fecha_hasta + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null);
                }

                if (t == 0) {
                    parteWhere += " AND " + conf.getString("SQL.R_RES.numeroAnotac"
                            + "ion") + " >= " + request.getParameter("numDesde");
                } else if (t == 2) {
                    if (!codigoUnidad.equals("")) { // tenemos que tener en cuenta las unidades externas
                        if (unidadesExternas) {
                            parteWhere += " AND RES_UCD = " + codigoUnidad + " AND RES_OCD = " + codOrganizacion; // unidad y org destino externa
                        } else {
                            parteWhere += " AND " + conf.getString("SQL.R_RES.unidOrigD"
                                    + "est") + " = " + codigoUnidad;
                            
                        }
                    }
                }
                
                if (("SI".equals(directivaUsuPermisoUor)) && ("S".equals((String) request.getParameter("tipo")))) {
                    parteWhere += " AND RES_UOD in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE"
                            + " UOU_USU=" + usuario.getIdUsuario() + " AND UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod() + ")";
                }
                
                joins = new String[14];
                joins[0] = "R_RES";
                joins[1] = "LEFT";
                joins[2] = "R_TTR";
                joins[3] = "res_ttr=ttr_ide";
                joins[4] = "INNER";
                joins[5] = "T_HTE";
                joins[6] = conf.getString("SQL.R_RES.codTercero") + " = "
                        + conf.getString("SQL.T_HTE.identificador") + " AND "
                        + conf.getString("SQL.R_RES.modifInteresado") + " = "
                        + conf.getString("SQL.T_HTE.version");
                joins[7] = "LEFT";

                if (unidadesExternas) { // si se va imprimir un informe de las unidades externas necesitamos consultar la tabla A_UOREX
                    joins[8] = GlobalNames.ESQUEMA_GENERICO + "A_UOREX A_UOREX";
                    joins[9] = " RES_UCD = UOREX_COD AND RES_OCD = UOREX_ORG ";

                } else {
                    joins[8] = "A_UOR";
                    joins[9] = conf.getString("SQL.R_RES.unidOrigDest") + " = "
                            + conf.getString("SQL.A_UOR.codigo");
                }

                joins[10] = "LEFT";
                joins[11] = "R_DIL";
                if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
                    joins[12] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                            + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                            + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                            + oad.convertir(oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null);
                } else {
                    joins[12] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                            + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                            + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                            + conf.getString("SQL.R_DIL.fecha");
                }
                joins[13] = "false";

                String parteFrom1 =
                        oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAANOTACION," + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION, " + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "YYYY/MM/DD") + " AS FECHA_ORDE," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO,";
                parteFrom1 += "HTE_NOM AS NOMBRE, HTE_AP1 AS APELLIDO1, HTE_AP2 AS APELLIDO2, HTE_DOC AS DOCUMENTO, "
                        + conf.getString("SQL.A_ORGEX.descripcion") + " AS "
                        + "DESTINO, " + conf.getString("SQL.R_RES.estAnot") + " AS ESTADO"
                        + "," + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA"
                        + ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,("
                        + calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4," + oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{conf.getString("SQL.R_TTR.descripcion"), "'-'"}) + "as TRANSPORTE, "
                        + "'" + nombreLargo + "' AS TIPONOMBRE";


                String parteWhere1 = conf.getString("SQL.R_RES.tipoReg") + " = '"
                        + request.getParameter("tipo") + "' " + " AND " + conf.getString("SQL.R_RES.cod"
                        + "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND "
                        + conf.getString("SQL.R_RES.tipoAnotacionDestino") + "=1"
                        + " AND " + conf.getString("SQL.R_RES.ejercicio") + "=" + ejercicio;

                if ((fecha_desde != null) && (!fecha_desde.equals(""))) {
                    parteWhere1 += " AND "
                            + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + " >= "
                            + oad.convertir("'" + fecha_desde + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null);
                }

                if ((fecha_hasta != null) && (!fecha_hasta.equals(""))) {
                    parteWhere1 += " AND "
                            + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + " <= "
                            + oad.convertir("'" + fecha_hasta + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null);
                }

                if (t == 0) {
                    parteWhere1 += " AND " + conf.getString("SQL.R_RES.numeroAnotac"
                            + "ion") + " >= " + request.getParameter("numDesde");
                } else if (t == 2) {
                    if (!codigoUnidad.equals("")) {
                        if (unidadesExternas) {
                            parteWhere1 += " AND RES_UCD = " + codigoUnidad + " AND RES_OCD = " + codOrganizacion; // unidad y org destino externa
                        }  else {
                            parteWhere1 += " AND " + conf.getString("SQL.R_RES.unidOrigD"
                                    + "est") + " = " + codigoUnidad;
                            
                             
                        }
                    }
                }
                
                if (("SI".equals(directivaUsuPermisoUor)) && ("S".equals((String) request.getParameter("tipo")))) {
                    parteWhere1 += " AND RES_UOD in (SELECT UOU_UOR FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU WHERE"
                           + " UOU_USU=" + usuario.getIdUsuario() + " AND UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod() + ")";
                }

                String[] joins1 = new String[14];
                if (unidadesExternas) {
                    joins1 = new String[17];
                }
                joins1[0] = "R_RES";
                joins1[1] = "LEFT";
                joins1[2] = "R_TTR";
                joins1[3] = "res_ttr=ttr_ide";
                joins1[4] = "INNER";
                joins1[5] = "T_HTE";
                joins1[6] = conf.getString("SQL.R_RES.codTercero") + " = "
                        + conf.getString("SQL.T_HTE.identificador") + " AND "
                        + conf.getString("SQL.R_RES.modifInteresado") + " = "
                        + conf.getString("SQL.T_HTE.version");
                joins1[7] = "LEFT";
                joins1[8] = GlobalNames.ESQUEMA_GENERICO + "A_ORGEX A_ORGEX";
                joins1[9] = conf.getString("SQL.R_RES.orgDestAnot") + " = "
                        + conf.getString("SQL.A_ORGEX.codigo");


                // tambien metemos la uor externa
                if (unidadesExternas) { // si se va imprimir un informe de las unidades externas necesitamos consultar la tabla A_UOREX
                    joins1[10] = "LEFT";
                    joins1[11] = GlobalNames.ESQUEMA_GENERICO + "A_UOREX A_UOREX";
                    joins1[12] = " RES_UCD = UOREX_COD  AND RES_OCD = UOREX_ORG ";

                    joins1[13] = "LEFT";
                    joins1[14] = "R_DIL";
                    if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
                        joins1[15] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                                + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                                + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                                + oad.convertir(oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null);
                    } else {
                        joins1[15] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                                + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                                + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                                + conf.getString("SQL.R_DIL.fecha");
                    }
                    joins1[16] = "false";


                } else {
                    joins1[10] = "LEFT";
                    joins1[11] = "R_DIL";
                    if (params[0].equals("sqlserver") || params[0].equals("SQLSERVER")) {
                        joins1[12] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                                + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                                + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                                + oad.convertir(oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null);
                    } else {
                        joins1[12] = conf.getString("SQL.R_RES.codUnidad") + " = " + conf.getString("SQL.R_DIL.codUnidad")
                                + " AND " + conf.getString("SQL.R_RES.tipoReg") + " = " + conf.getString("SQL.R_DIL.tipo")
                                + " AND " + oad.convertir(oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQL.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQL.CONVERTIR_COLUMNA_FECHA, null) + " = "
                                + conf.getString("SQL.R_DIL.fecha");
                    }
                    joins1[13] = "false";
                }

                sql = "";
                try {
                    sql = oad.join(parteFrom, parteWhere, joins);
                    sql += " UNION ";
                    sql += oad.join(parteFrom1, parteWhere1, joins1);
                    if (!orden) {
                        String[] order = {"NUM", "4"};
                        sql += oad.orderUnion(order);
                    } else {
                        String[] order = {"FECHA_ORDE", "2","HORA","3", "NUM", "4"};
                        sql += oad.orderUnion(order);
                    }
                } catch (Exception bde) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.error("Error en el join: " + bde.toString());
                    }
                }
                break;
            case 1:
                sql = "SELECT " + oad.convertir(conf.getString("SQL.R_DIL.fecha"),
                        AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA,"
                        + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION FROM R_DIL WHERE " + conf.getString("SQL.R_DIL.codUnidad") + " = " + usuarioR.getUnidadOrgCod()
                        + " AND " + conf.getString("SQL.R_DIL.tipo") + " = '" + request.getParameter("tipo") + "'";
                if ((fecha_desde != null) && (!fecha_desde.equals(""))) {
                    sql += " AND "
                            + oad.convertir(oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + " >= " + oad.convertir("'" + fecha_desde + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null);
                }

                if ((fecha_hasta != null) && (!fecha_hasta.equals(""))) {
                    sql += " AND "
                            + oad.convertir(oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY"), AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null) + " <= " + oad.convertir("'" + fecha_hasta + "'", AdaptadorSQLBD.CONVERTIR_COLUMNA_FECHA, null);
                }
                break;

        }
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("sql consulta_fecha:" + sql);
        }
        return sql;
    }

    /* Función: ImprimirPortada*/
    private String imprimirPortada(String tipoReg, String numPaginas, String claveIdioma) {
        GeneralValueObject gVO = new GeneralValueObject();

        String plantilla = "";
        String alcalde = "";
        String secretario = "";
        alcalde = m_Conf.getString("portadaRexitro.alcalde");
        secretario = m_Conf.getString("portadaRexitro.secretario");
        plantilla = "portadaRegistro_" + claveIdioma;

        gVO.setAtributo("baseDir", m_Conf.getString("PDF.base_dir"));
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        gVO.setAtributo("usuDir", usuario.getDtr());
        gVO.setAtributo("pdfFile", "registro");
        String protocolo = StrutsUtilOperations.getProtocol(request);
        m_Log.debug("PROTOCOLO en uso :" + protocolo);
        String sUrl = protocolo + "://" + request.getHeader("Host") + request.getContextPath();
        String estilo = "css/portadaRegistro.css";
        gVO.setAtributo("estilo", estilo);

        gVO.setAtributo("opcionint",request.getParameter("interopcion"));
        GeneralPDF pdf = new GeneralPDF(usuario.getParamsCon(), gVO);
        String textoXML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><PORTADA>" +
                "<TIPOREGISTRO>" + tipoReg + "</TIPOREGISTRO>" +
                "<NUMPAGINAS>" + numPaginas + "</NUMPAGINAS>" +
                "<ALCALDE>" + alcalde + "</ALCALDE>" +
                "<SECRETARIO>" + secretario + "</SECRETARIO>" +
                "</PORTADA>";

        if (m_Log.isDebugEnabled()) {
            m_Log.debug("InformesRegistroAction. imprimirPortada. " + textoXML);
        }

        File f = pdf.transformaXML(textoXML, plantilla);
        Vector vector = new Vector();
        vector.add(f);

        return pdf.getPdf(vector);
    }
}
