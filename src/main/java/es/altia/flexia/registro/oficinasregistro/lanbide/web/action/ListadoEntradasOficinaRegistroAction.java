package es.altia.flexia.registro.oficinasregistro.lanbide.web.action;

import es.altia.util.cache.CacheDatosFactoria;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.registro.AnotacionRegistroVO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.AuditoriaManager;
import es.altia.agora.business.registro.persistence.InformesManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.NumeroALetra;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.auditoria.ConstantesAuditoria;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.registro.oficinasregistro.lanbide.persistence.AnotacionOficinaRegistroManager;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.OficinaRegistroLanbideVO;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.sqlxmlpdf.GeneralPDF;
import es.altia.util.struts.StrutsUtilOperations;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action que es llamado para generar un informe con el listado de las anotaciones dadas de alta por la oficina de registro a la que pertenece un usuario
 * @author oscar.rodriguez
 */
public class ListadoEntradasOficinaRegistroAction extends ActionSession{
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");
    protected static Config conf = ConfigServiceHelper.getConfig("techserver");
    protected static Config confReg = ConfigServiceHelper.getConfig("Registro");
    private String[] params;
    private UsuarioValueObject usuario;    
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
    private final String OPCION_ENTRADA = "entrada";
    private final String OPCION_SALIDA = "salida";
    private final String OPCION_INFORME = "informe";
    private Logger log = Logger.getLogger(ListadoEntradasOficinaRegistroAction.class);
    private String codUnidadRegistro=null;
    private String codigoOficinaUsuario = null;
    private String descripcionUnidadRegistro = null;
    
    private final String OPCION_SELECCION_UOR = "seleccionUor";
    private final String PANTALLA_SELECCION_UNIDAD_REGISTRO = "seleccionUnidadRegistro";
    private final String PANTALLA_IMPRESION_INFORME_ENTRADA = "informeEntrada";
    private final String PANTALLA_IMPRESION_INFORME_SALIDA = "informeSalida";
    

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

            // Directorio base para guardar el informe en formato PDF
            pdf_dir = m_Conf.getString("PDF.base_dir");
            HttpSession session = request.getSession();
            String opcion = request.getParameter("opcion");
            if (log.isDebugEnabled()) {
                log.debug("Opcion: " + opcion);
            }
            this.request = request;
            usuario = (UsuarioValueObject) session.getAttribute("usuario");
            //usuarioR = (RegistroUsuarioValueObject) session.getAttribute("registroUsuario");
            this.params = usuario.getParamsCon();
            
            session.removeAttribute("CODIGO_ERROR_LISTADO_LANBIDE");
            session.removeAttribute("cod_oficina_registro_usuario");
            session.removeAttribute("cod_unidad_registro_usuario");
            
            if(OPCION_SELECCION_UOR.equalsIgnoreCase(opcion)){                
                String codOrganizacion = request.getParameter("codOrganizacion");
                String codUnidadRegistro = request.getParameter("codUor");
                String tipoListado       = request.getParameter("tipoListado");
                
                m_Log.debug(" SELECCIÓN DE UOR codOrganizacion: " + codOrganizacion + ", codUnidadRegistro: " + codUnidadRegistro);
                if(codOrganizacion!=null && codUnidadRegistro!=null && !"".equals(codOrganizacion) && !"".equals(codUnidadRegistro)){
                                         
                     int numOficinasPermisoUsuario = UsuarioManager.getInstance().getNumOficinasRegistroPermiso(usuario.getOrgCod(),usuario.getIdUsuario(), Integer.parseInt(codUnidadRegistro),params);
                     m_Log.debug("numOficinasPermisoUsuario: " + numOficinasPermisoUsuario);
                                          
                     if(numOficinasPermisoUsuario==1){
                         // El usuario tiene permiso sobre una única oficina de registro hija de la unidad de registro seleccionada
                         Integer codOficinaRegistro = UsuarioManager.getInstance().getCodOficinaRegistro(usuario.getIdUsuario(), Integer.parseInt(codUnidadRegistro), params);
                         m_Log.debug("Codigo de la oficina de registro del usuario: " + codOficinaRegistro);
                         session.setAttribute("cod_oficina_registro_usuario",codOficinaRegistro);
                         session.setAttribute("cod_unidad_registro_usuario",codUnidadRegistro);
                     }else
                     if(numOficinasPermisoUsuario==0){
                         // ERROR => El usuario no tiene permiso sobre ninguna oficina de registro
                        session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","NO_PERMISO_NINGUNA_OFICINA_REGISTRO");
                     }else
                     if(numOficinasPermisoUsuario>1){
                         // ERROR => El usuario tiene permiso sobre más de una oficina de registro
                        session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","PERMISO_MAS_UNA_OFICINA_REGISTRO");
                     }                                                                       
                }else{
                    m_Log.warn("Selección de unidad de registro incorrecta");
                    session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","ERROR_TECNICO_VERIFICACION_OFICINA_REGISTRO");
                }  
                
                if(tipoListado.equalsIgnoreCase("ENTRADA"))
                    return mapping.findForward(PANTALLA_IMPRESION_INFORME_ENTRADA);
                else
                if(tipoListado.equalsIgnoreCase("SALIDA"))
                    return mapping.findForward(PANTALLA_IMPRESION_INFORME_SALIDA);                        
                    
            }else            
            if((OPCION_ENTRADA.equalsIgnoreCase(opcion)) || (OPCION_SALIDA.equalsIgnoreCase(opcion)))
            {   
                
                // SE COMPRUEBA SOBRE QUE UORS DE TIPO REGISTRO TIENE PERMISO EL USUARIO
                ArrayList<UORDTO> unidades = UsuarioManager.getInstance().loadUnidadTipoRegistro(usuario,usuario.getParamsCon());
                m_Log.debug("unidades: " + unidades.size());
                
                if(unidades.size()>1){
                    // El usuario tiene permiso sobre más de una unidad de tipo registro, entonces, se pasa el control al 
                    // listado correspondiente para que seleccione la unidad de tipo registro sobre la que trabajar
                    m_Log.debug("Nº de unidades recuperadas: " + unidades.size());
                    session.setAttribute("unidades_tipo_registro",unidades);
                    String TIPO_LISTADO = "salida";
                    if(OPCION_ENTRADA.equalsIgnoreCase(opcion)){
                        TIPO_LISTADO = "entrada";
                    }                        
                    session.setAttribute("TIPO_LISTADO",TIPO_LISTADO);                    
                    return mapping.findForward(PANTALLA_SELECCION_UNIDAD_REGISTRO);
                    
                }else
                if(unidades.size()==1){
                    // Si el usuario sólo tiene permiso sobre una unidad de tipo registro, se comprueba si ésta tiene hijas.
                     int numOficinasPermisoUsuario = UsuarioManager.getInstance().getNumOficinasRegistroPermiso(usuario.getOrgCod(),usuario.getIdUsuario(), Integer.parseInt(unidades.get(0).getUor_cod()),params);
                     m_Log.debug("numOficinasPermisoUsuario: " + numOficinasPermisoUsuario);
                     
                     if(numOficinasPermisoUsuario==1){
                         // El usuario tiene permiso sobre una única oficina de registro hija de la unidad de registro seleccionada
                         Integer codOficinaRegistro = UsuarioManager.getInstance().getCodOficinaRegistro(usuario.getIdUsuario(), Integer.parseInt(unidades.get(0).getUor_cod()), params);
                         m_Log.debug("Codigo de la oficina de registro del usuario: " + codOficinaRegistro);
                         session.setAttribute("cod_oficina_registro_usuario",codOficinaRegistro);
                         session.setAttribute("cod_unidad_registro_usuario",unidades.get(0).getUor_cod());
                     }else
                     if(numOficinasPermisoUsuario==0){
                         // ERROR => El usuario no tiene permiso sobre ninguna oficina de registro
                        session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","NO_PERMISO_NINGUNA_OFICINA_REGISTRO");
                     }else
                     if(numOficinasPermisoUsuario>1){
                         // ERROR => El usuario tiene permiso sobre más de una oficina de registro
                        session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","PERMISO_MAS_UNA_OFICINA_REGISTRO");
                     }                              
                     
                     return mapping.findForward(opcion);
                }else
                if(unidades.size()==0){
                    // El usuario no tiene permiso sobre ninguna oficina de registro => ERROR
                    session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","NO_PERMISO_UNIDAD_TIPO_REGISTRO");
                    return mapping.findForward(opcion);
                }
                
                
                /* ORIGINAL
                AnotacionOficinaRegistroManager manager = AnotacionOficinaRegistroManager.getInstance();
                // Se comprueba si el usuario tiene permiso sobre una única unidad organizativa con el rol de registro.
                int salida = manager.tieneUsuarioUnaSolaOficinaRegistro(usuario,usuario.getParamsCon());

                
                *     0 --> Si el usuario tiene permiso sobre una única unidad de tipo registro
                 *   -1 --> Si el usuario no tiene permiso ninguna unidad de tipo registro
                 *   -2 --> Si el usuario tiene permiso sobre más de una unidad de tipo registro
                 *   -3 --> Si el usuario no tiene permiso sobre ninguna oficina de registro
                 *   -4 --> Si el usuario tiene permiso sobre más de una oficina de registro
                 

                /** Se comprueba si hay que mostrar algún tipo de mensaje de error **
                 if(salida==-1)
                     session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","NO_PERMISO_UNIDAD_TIPO_REGISTRO");
                 else
                 if(salida==-2)
                     session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","PERMISO_MAS_UNA_UNIDAD_TIPO_REGISTRO");
                 else
                 if(salida==-3)
                     session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","NO_PERMISO_NINGUNA_OFICINA_REGISTRO");
                 else
                 if(salida==-4)
                     session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","PERMISO_MAS_UNA_OFICINA_REGISTRO");
                 else
                 if(salida==0){
                     session.setAttribute("CODIGO_ERROR_LISTADO_LANBIDE","0");                     
                 }
                 */
               
            }else
            if(OPCION_INFORME.equals(opcion)){

                String codOficinaRegistroUsuario = request.getParameter("codOficinaRegistroUsuario");                
                String codUnidadRegistro = request.getParameter("codUnidadRegistroUsuario");
                m_Log.debug("IMPRIMIR INFORME codUnidadRegistro: " + codUnidadRegistro + ", codOficinaRegistroUsuario: " + codOficinaRegistroUsuario);
                this.codigoOficinaUsuario = codOficinaRegistroUsuario;
                
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
                if ((opcion.equals("informeLibroRegistro"))||(opcion.equals("relacionUnidadesOrganicas")) || (opcion.equals("relacionUnidadesOrganicasExternas")) ){
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
                log.debug("el claveIdioma es ***************************** " + claveIdioma);
                log.debug("el codUnidad es ***************************** " + codUnidad);


                nombre = informeLibroRegistro(codUnidad,claveIdioma,codUnidadRegistro,params);
                request.setAttribute("nombre", nombre);
                request.setAttribute("tipoVentana", tipoVentana);
                request.setAttribute("fallo", fallo);
                request.setAttribute("numAsientos", numAsientos);
                request.setAttribute("maxAsientos", maxAsientos);
                /* Redirigimos al JSP de salida*/
                opcion = "redirige";
            }

           return mapping.findForward(opcion);
       }



  /**
     * Función encargada de realizar el imforme del libro de registro
     *
     * @return Un String con la ruta del pdf generado
     */
    private String informeLibroRegistro(String unidad,String claveIdioma,String codUnidadRegistro,String[] params) {

        List<AnotacionRegistroVO> listaNumAnotacionImpresas = new ArrayList<AnotacionRegistroVO>();
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

        UORDTO unidadRegistroTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],codUnidadRegistro);
        if(unidadRegistroTO!=null){
            this.codUnidadRegistro = codUnidadRegistro;
            descripcionUnidadRegistro = unidadRegistroTO.getUor_nom();
            consultaVO.setAtributo("unidadOrg",codUnidadRegistro);
        }
        /** ORIGINAL
        OficinaRegistroLanbideVO registro = AnotacionOficinaRegistroManager.getInstance().getUnidadRegistroUsuario(usuario, usuario.getParamsCon());        
        if(registro!=null){
            codUnidadRegistro = String.valueOf(registro.getCodOficina());
            descripcionUnidadRegistro = registro.getNombreOficina();
            consultaVO.setAtributo("unidadOrg",String.valueOf(registro.getCodOficina()));
        }
        **/
                
        consultaVO.setAtributo("depto", String.valueOf(usuario.getDepCod()));
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

            //int iDesde = Integer.parseInt(desde);
            //int iHasta  = Integer.parseInt(hasta);
            
            if (desde.compareTo(hasta) <= 0) {
            //if(iDesde<=iHasta){
                m_Log.debug("ENTRO aqui--> compareTo" + unidad);
                file = pdf.construyeTabla(consulta(0, unidad, desde, hasta, orden, ejercicio, false, null), xsl, listaNumAnotacionImpresas);
                if (file != null) {
                    ficheros.add(file);
                }//Añado el dia del libro

                 ficheros.add(pdf.construyeTabla(consulta(1, unidad, desde, hasta, orden, ejercicio, false, null), "diligencias_"+claveIdioma));//añado la diligencia
                  if (m_Conf.getString("PDF.pie_registro").equals("si")) {
                        Hashtable tabla = new Hashtable();
                        ficheros.add(pdf.construyeTablaVacia2("pieHoja3_"+claveIdioma, tabla));

                    }
            }
        } else {
            m_Log.debug("ENTRO AQUÍ--> compareTo" + gVO.getSize());
            file = pdf.construyeTabla(consulta_fecha(0, unidad, fechaInicio, fechaFin, orden, ejercicio, false, null), xsl, listaNumAnotacionImpresas);
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
            return "";
        }

        String pathResultado = pdf.getPdf(ficheros);
        String portada = request.getParameter("portada");
        if (portada != null) {
            int numPaginas = pdf.getNumeroPaginas(pathResultado);
            if (numPaginas > 0) {
                NumeroALetra transformadorNumero = new NumeroALetra();
                String descNumero = transformadorNumero.convertirLetras(numPaginas);
                m_Log.debug(descNumero);
                String pdfPortada = imprimirPortada(request.getParameter("tipo"), descNumero, claveIdioma);
                pathResultado = pdf.anhadePortada(pdfPortada, pathResultado);
            }
        }
        
        // Auditoria de acceso al registro
        try {
            AuditoriaManager.getInstance().auditarAccesoRegistro(
                    ConstantesAuditoria.REGISTRO_INFORME_POR_OFICINA,
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
        
        return pathResultado;
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
        /* original 
        sql += "' AS ENT,'" + usuarioR.getUnidadOrg() + "' AS UOR," + num + "AS" +
                " OPC "; */
        
        sql += "' AS ENT,'" + this.descripcionUnidadRegistro + "' AS UOR," + num + "AS" +
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

        if (m_Log.isInfoEnabled()) {
            m_Log.debug("InformesRegistroAction. imprimirPortada. " + textoXML);
        }

        File f = pdf.transformaXML(textoXML, plantilla);
        Vector vector = new Vector();
        vector.add(f);

        return pdf.getPdf(vector);
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


                    log.debug("parteWhere antes: " + parteWhere);
                    parteWhere = parteWhere + " AND (EXISTS(SELECT UOR_COD " +
                                                              "FROM A_UOR," + GlobalNames.ESQUEMA_GENERICO + "A_ORG," + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                                                              "WHERE UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod() + " AND UOU_USU=RES_USU " +
                                                              "AND UOR_COD_VIS LIKE 'OF%' AND UOR_COD=" + this.codigoOficinaUsuario + " " +
                                                              "AND (UOR_TIP IS NULL OR UOR_TIP=0) AND UOU_UOR	= UOR_COD	AND UOU_ORG	= ORG_COD)) ";
                    
                    log.debug("parteWhere despues: " + parteWhere);

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
                        oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAANOTACION," + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION, " + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA_ORDE," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO,";
                parteFrom += "HTE_NOM AS NOMBRE, HTE_AP1 AS APELLIDO1, HTE_AP2 AS APELLIDO, HTE_DOC AS DOCUMENTO, ";

                if (unidadesExternas) {
                    parteFrom += " UOREX_NOM AS DESTINO, ";
                } else {
                    parteFrom += conf.getString("SQL.A_UOR.nombre") + " AS DESTINO, ";
                }

                parteFrom += conf.getString("SQL.R_RES.estAnot") + " AS ESTADO"
                        + "," + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA"
                        + ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,("
                        + calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4,";

                parteWhere = conf.getString("SQL.R_RES.tipoReg") + " = '"
                        + request.getParameter("tipo") + "' " + " AND " + conf.getString("SQL.R_RES.cod"
                        //+ "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND (" ;
                        + "Unidad") + " = " + this.codUnidadRegistro + " AND (" ;

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
                        oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAANOTACION," + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION, " + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA_ORDE," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO";
                parteFrom += "HTE_NOM AS NOMBRE, HTE_AP1 AS APELLIDO1, HTE_AP2 AS APELLIDO, HTE_DOC AS DOCUMENTO, "
                        + conf.getString("SQL.R_RES.estAnot") + " AS ESTADO,"
                        + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA"
                        + ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,("
                        + calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4";
                String parteWhere1 = conf.getString("SQL.R_RES.tipoReg") + " = '"
                        + request.getParameter("tipo") + "' " + " AND " + conf.getString("SQL.R_RES.cod"
                        //+ "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND "
                        + "Unidad") + " = " + this.codUnidadRegistro + " AND "
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

                    /** Se añade en el where la comprobación de que el usuario que ha dado de alta la anotación sea de la misma oficina de registro que la del usuario que accede
                     * al módulo  */

                    /*
                    parteWhere = parteWhere + " AND EXISTS(SELECT UOR_COD " +
                                                               "FROM A_UOR, " + GlobalNames.ESQUEMA_GENERICO + "A_ORG," + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                                                               "WHERE UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod() + " AND UOU_USU=RES_USU " +
                                                               "AND UOR_COD_VIS LIKE '" + ConstantesDatos.PREFIJO_OFICINA_REGISTRO_LANBIDE +"%' AND UOR_COD=" + codigoOficinaUsuario + " " +
                                                               "AND (UOR_TIP IS NULL OR UOR_TIP=0) AND UOU_UOR	= UOR_COD	AND UOU_ORG	= ORG_COD) ";
                                                               */
                    sql = oad.join(parteFrom, parteWhere, joins);
                    if (!orden) {
                        String[] order = {"NUM", "3"};
                        sql += oad.orderUnion(order);
                    } else {
                        String[] order = {"FECHA_ORDE", "2", "NUM", "3"};
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
                        //+ conf.getString("SQL.R_DIL.codUnidad") + " = " + usuarioR.getUnidadOrgCod()
                        + conf.getString("SQL.R_DIL.codUnidad") + " = " + this.codUnidadRegistro
                        + " AND " + conf.getString("SQL.R_DIL.tipo") + " = '" + request.getParameter("tipo") + "'";


                break;

        }
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("sql consulta en InformesRegsitroAction (1617) :" + sql);
        }
        return sql;
    }



    private int estimacionMaximoAsientos() {
        int memoriaJVMKbytes = (int) Runtime.getRuntime().maxMemory() / 1024;
        m_Log.debug("Memoria JVM en KBytes : " + memoriaJVMKbytes);
        // Se estima que se necesitan 30Mb mas 90Kb por asiento para el parsing y se
        // ha comprobado que con 64MB se pueden manejar 600 asientos, se pone como minimo.
        int max = (memoriaJVMKbytes - 40 * 1024) / 90;
        if (max < 600) {
            max = 500;
        }
        m_Log.debug("Numero maximo de asientos estimado : " + max);
        return max;
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
                        oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAANOTACION," + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION, " + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA_ORDE," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO,";
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
                        //+ "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND ("
                        + "Unidad") + " = " + codUnidadRegistro + " AND ("
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
                        oad.convertir(conf.getString("SQL.R_DIL.fecha"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHAANOTACION," + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION, " + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA_ORDE," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHA," + oad.convertir(conf.getString("SQL.R_RES.fechaAnotacion"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORA," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "DD/MM/YYYY") + " AS FECHADOCUMENTO," + oad.convertir(conf.getString("SQL.R_RES.fechaDocumento"), AdaptadorSQLBD.CONVERTIR_COLUMNA_TEXTO, "HH24:MI") + " AS HORADOCUMENTO," + conf.getString("SQL.R_RES.numeroAnotacion") + " AS NUM," + conf.getString("SQL.R_RES.asunto") + " AS ASUNTO," + conf.getString("SQL.R_RES.tipoReg") + " AS TIPO,";
                parteFrom1 += "HTE_NOM AS NOMBRE, HTE_AP1 AS APELLIDO1, HTE_AP2 AS APELLIDO2, HTE_DOC AS DOCUMENTO, "
                        + conf.getString("SQL.A_ORGEX.descripcion") + " AS "
                        + "DESTINO, " + conf.getString("SQL.R_RES.estAnot") + " AS ESTADO"
                        + "," + conf.getString("SQL.R_RES.diligencia") + " AS DILIGENCIA"
                        + ",(" + calle1 + ") AS CALLE1,(" + calle2 + ") AS CALLE2,("
                        + calle3 + ") AS CALLE3,(" + calle4 + ") AS CALLE4," + oad.funcionSistema(AdaptadorSQLBD.FUNCIONSISTEMA_NVL, new String[]{conf.getString("SQL.R_TTR.descripcion"), "'-'"}) + "as TRANSPORTE, "
                        + "'" + nombreLargo + "' AS TIPONOMBRE";


                String parteWhere1 = conf.getString("SQL.R_RES.tipoReg") + " = '"
                        + request.getParameter("tipo") + "' " + " AND " + conf.getString("SQL.R_RES.cod"
                        //+ "Unidad") + " = " + usuarioR.getUnidadOrgCod() + " AND "
                        + "Unidad") + " = " + codUnidadRegistro + " AND "
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
                    /** AÑADIR AQUÍ QUE LA COMPROBACIÓN PARA LA PRIMERA PARTE DE LA UNION EN LA QUE SE COMPRUEBA QUE EL USUARIO QUE HA DADO
                     * DE ALTA LA ANOTACIÓN TIENE PERMISO SOBRE LA OFICINA DE REGISTRO DEL USUARIO */
                    //codigoOficinaUsuario

                    
                    /*** ORIGINAL
                    // Se añade la comprobación de que el usuario que ha creado la anotación tenga permiso sobre la oficina de registro del usuario actual
                    parteWhere = parteWhere + " AND EXISTS(SELECT UOR_COD " +
                                        "FROM A_UOR, " + GlobalNames.ESQUEMA_GENERICO + "A_ORG," + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                                        "WHERE UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod() + " AND UOU_USU=RES_USU " +
                                        "AND UOR_COD_VIS LIKE '" + ConstantesDatos.PREFIJO_OFICINA_REGISTRO_LANBIDE +"%' AND UOR_COD=" + codigoOficinaUsuario + " " +
                                        "AND (UOR_TIP IS NULL OR UOR_TIP=0) AND UOU_UOR	= UOR_COD	AND UOU_ORG	= ORG_COD) ";
                    ****/
                    
                    parteWhere = parteWhere + " AND RES_OFI=" + codigoOficinaUsuario + " ";
                    
                    sql = oad.join(parteFrom, parteWhere, joins);
                    sql += " UNION ";
                    
                    /**** ORIGINAL
                    // Se añade la comprobación de que el usuario que ha creado la anotación tenga permiso sobre la oficina de registro del usuario actual
                    parteWhere1 = parteWhere1 + " AND EXISTS(SELECT UOR_COD " +
                                        "FROM A_UOR, " + GlobalNames.ESQUEMA_GENERICO + "A_ORG," + GlobalNames.ESQUEMA_GENERICO + "A_UOU " +
                                        "WHERE UOU_ORG=" + usuario.getOrgCod() + " AND UOU_ENT=" + usuario.getEntCod() + " AND UOU_USU=RES_USU " +
                                        "AND UOR_COD_VIS LIKE '" + ConstantesDatos.PREFIJO_OFICINA_REGISTRO_LANBIDE +"%' AND UOR_COD=" + codigoOficinaUsuario + " " +
                                        "AND (UOR_TIP IS NULL OR UOR_TIP=0) AND UOU_UOR	= UOR_COD	AND UOU_ORG	= ORG_COD) ";
                    **/
                    
                    parteWhere1 = parteWhere1 + " AND RES_OFI=" + codigoOficinaUsuario + " ";
                    sql += oad.join(parteFrom1, parteWhere1, joins1);
                    if (!orden) {
                        String[] order = {"NUM", "3"};
                        sql += oad.orderUnion(order);
                    } else {
                        String[] order = {"FECHA_ORDE", "2", "NUM", "3"};
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
                        //+ conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION FROM R_DIL WHERE " + conf.getString("SQL.R_DIL.codUnidad") + " = " + usuarioR.getUnidadOrgCod()
                        + conf.getString("SQL.R_DIL.anotacion") + " AS ANOTACION FROM R_DIL WHERE " + conf.getString("SQL.R_DIL.codUnidad") + " = " + this.codUnidadRegistro
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
}
