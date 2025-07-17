package es.altia.flexia.registro.oficinasregistro.lanbide.web.action;

import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.RegistroUsuarioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.AnotacionRegistroVO;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.AuditoriaManager;
import es.altia.agora.business.registro.persistence.InformesManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.auditoria.ConstantesAuditoria;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.registro.oficinasregistro.lanbide.persistence.AnotacionOficinaRegistroManager;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.OficinaRegistroLanbideVO;
import es.altia.flexia.registro.oficinasregistro.lanbide.vo.AnotacionOficinaRegistroVO;
import es.altia.flexia.registro.oficinasregistro.lanbide.web.form.AnotacionesOficinasRegistroForm;
import es.altia.util.sqlxmlpdf.GeneralPDF;
import java.io.File;
import es.altia.util.cache.CacheDatosFactoria;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action encargado de las peticiones enviadas desde el  módulo de control de envío de anotaciones entre oficinas de registro
 */
public class EnvioAnotacionOficinasRegistroAction extends ActionSession{
    private Logger log = Logger.getLogger(EnvioAnotacionOficinasRegistroAction.class);
    private final String OPCION_ENTRADA    = "entrada";
    private final String OPCION_BUSQUEDA = "busqueda";
    private final String OPCION_ACEPTAR    = "aceptar";
    private final String OPCION_RECHAZAR = "rechazar";
    private final String OPCION_ENVIAR      = "enviar";
    private final String OPCION_INFORME    = "imprimir";
    private String pdf_dir = null;
    private String idioma = null;
    protected static Config conf = ConfigServiceHelper.getConfig("techserver");
    protected static Config m_Conf = ConfigServiceHelper.getConfig("common");
    private static TraductorAplicacionBean descriptor = new TraductorAplicacionBean();
    private String nombre = null;
    private String escudo = null;
    private String pdfFile = null;
    private String fallo = "";
    private int numAsientos;
    private int maxAsientos;

    public ActionForward performSession(ActionMapping mapping,ActionForm form,HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("EnvioAnotacionOficinasRegistroAction =================>");
        
        HttpSession session = request.getSession();
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        // Parámetros de conexión a base de datos
        String[] params = usuario.getParamsCon();

        log.debug("Recuperando el usuario : " + usuario.getIdUsuario() +  " de la sesión");
        String opcion = request.getParameter("opcion");
        log.debug("opcion= " + opcion);

        AnotacionesOficinasRegistroForm formulario = (AnotacionesOficinasRegistroForm)form;       
        if(OPCION_ENTRADA.equalsIgnoreCase(opcion)){
            /*********/
            RegistroUsuarioValueObject regVO = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
            int codOficinaRegistro = regVO.getCodOficinaRegistro();
            int codUnidadRegistro = regVO.getUnidadOrgCod();
            
            log.debug("EnvioAnotacionOficinasRegistroAction codOficinaRegistro: " + codOficinaRegistro + ",codUnidadRegistro: " + codUnidadRegistro);
            
            // Se comprueba si la oficina de registro es hija de la unidad de registro => Sino lo es ERROR
            /**
                0 --> El usuario tiene permiso sobre la unidad de registro codUorRegistro y sobre una única oficina de registro hija que es codUorOficinaRegistro
                1 --> El usuario tiene permiso sobre la unidad de registro codUorRegistro y esta unidad no tiene hijas, por tanto la oficina de registro es la 
                    unidad de registro
                -1 --> El usuario tiene permiso sobre más de una oficina de registro hija de la unidad de registro codUorRegistro.
                -2 --> La unidad  de registro codUorRegistro no tiene hijas, y además codUorOficinaRegistro != codUorRegistro, por tanto, se ha producido un 
                    error técnico ya que la unidad de registro y la oficina de registro deberían ser la misma
                -3 --> Error al obtener la conexión a la BBDD
                -4 --> Error técnico
            */ 
            int salida = UORsManager.getInstance().comprobarPermisosUsuarioOficinaRegistro(codOficinaRegistro, codUnidadRegistro, usuario.getIdUsuario(), usuario.getOrgCod(), usuario.getEntCod(), params);
            log.debug("EnvioAnotacionOficinasRegistroAction salida: " + salida);
            /*********/
                        
            formulario.setCodigoUnidadRegistroUsuario(null);
            formulario.setCodigoOficinaUsuario(null);
            formulario.setDescripcionOficinaUsuario(null);
            
            if(salida==-1)
                // El usuario tiene permiso sobre más de una oficina de registro hija de la unidad de registro codUorRegistro.                
                 //formulario.setCodigoError("NO_PERMISO_UNIDAD_TIPO_REGISTRO");
                formulario.setCodigoError("PERMISO_MAS_UNA_OFICINA_REGISTRO");
             else
             if(salida==-2 || salida==1)
                 // El usuario no tiene permiso sobre una oficina de registro hija de la unidad de registro padre
                 formulario.setCodigoError("NO_PERMISO_NINGUNA_OFICINA_REGISTRO");
             else
             if(salida==-3 || salida==-4)
                 formulario.setCodigoError("ERROR_TECNICO_VERIFICACION_PERMISOS");
             else
             if(salida==0){
                 formulario.setCodigoError("OK");
                 formulario.setCodigoUnidadRegistroUsuario(Integer.toString(codUnidadRegistro));
                 formulario.setCodigoOficinaUsuario(Integer.toString(codOficinaRegistro));
                 UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],Integer.toString(codOficinaRegistro));
                 if (uorDTO!=null)
                    formulario.setDescripcionOficinaUsuario(uorDTO.getUor_nom());               
                 // Se recupera la única oficina de registro que tiene el usuario
                 //OficinaRegistroLanbideVO ofiUsuario = manager.getOficinaRegistroUsuario(usuario, params);                 
                 //formulario.setCodigoOficinaUsuario(ofiUsuario.getCodOficina());
             }             
            
            Calendar fechaActual = Calendar.getInstance();
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            // Se establece la fecha actual del formulario para la fecha hasta
            String fechaHasta = sf.format(fechaActual.getTime()).trim();
            formulario.setFechaHasta(fechaHasta.trim());

            // Se establece para la fecha desde la fecha de una semana anterior a la actual
            fechaActual.add(Calendar.DAY_OF_MONTH,-7);
            formulario.setFechaDesde(sf.format(fechaActual.getTime()).trim());

            /** Se recuperan todas las oficinas de registro **/
            ArrayList<OficinaRegistroLanbideVO> oficinas = AnotacionOficinaRegistroManager.getInstance().getOficinasRegistro(params);
            formulario.setOficinasOrigen(oficinas);
            formulario.setOficinasDestino(oficinas);
            request.setAttribute("AnotacionesOficinasRegistroForm",formulario);
            opcion = OPCION_ENTRADA;
        }else
        if(OPCION_BUSQUEDA.equalsIgnoreCase(opcion)){
            /** Se realiza una búsqueda de anotaciones **/
            log.debug(" ================> buscando anotaciones");
            String codOfiOrigen  = formulario.getCodigoOficinaRegistroOrigen();
            String codOfiDestino = formulario.getCodigoOficinaRegistroDestino();
            String nombreOfiDestino = formulario.getNombreOficinaRegistroDestino();
            String fechaDesde    = formulario.getFechaDesde();
            String fechaHasta     = formulario.getFechaHasta();
            String pagina           = formulario.getPagina();
            String estado           = formulario.getEstado();
            
            RegistroUsuarioValueObject registroUsuario = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");
            int codUnidadRegistro = registroUsuario.getUnidadOrgCod();
            
            log.debug("***** Código de la unidad de registro: " + codUnidadRegistro);
            
            if(pagina==null || "".equals(pagina)) pagina ="1";
            
            if(codOfiOrigen!=null && "undefined".equals(codOfiOrigen)) codOfiOrigen = null;
            if(codOfiDestino!=null && "undefined".equals(codOfiDestino)) {

                OficinaRegistroLanbideVO oficinaUsuario =
                        AnotacionOficinaRegistroManager.getInstance().getOficinaRegistroUsuario(registroUsuario.getUnidadOrgCod(),usuario, params);                
                codOfiDestino = oficinaUsuario.getCodOficina();
                //nombreOfiDestino = oficinaUsuario.getNombreOficina();
            }

            log.debug("codOfiOrigen: " + codOfiOrigen + ", codOfiDestino: " + codOfiDestino);
            log.debug("fechaDesde: " + fechaDesde + ", fechaHasta: " + fechaHasta);
            log.debug("pagina actual: " + pagina + ", estado: " + estado);
            
            int numPagina = Integer.parseInt(pagina);
            /** Número de registros a mostrar por página */
            log.debug("Pagina a recuperar: " + numPagina + ", num registros a recuperar: " + formulario.getLineasPaginas());

            /** Se recupera el número total de anotaciones */
            int totalAnotaciones = AnotacionOficinaRegistroManager.getInstance().getTotalAnotaciones(Integer.toString(codUnidadRegistro),codOfiOrigen, codOfiDestino, fechaDesde, fechaHasta, estado,usuario, params);
            /** Se recuperan las anotaciones correspondientes a la página actual */            
            ArrayList<AnotacionOficinaRegistroVO> anotaciones = AnotacionOficinaRegistroManager.getInstance().
                    getAnotaciones(Integer.toString(codUnidadRegistro),codOfiOrigen, codOfiDestino,nombreOfiDestino, fechaDesde, fechaHasta,estado, 
                    usuario, numPagina,Integer.parseInt(formulario.getLineasPaginas()),params);
            formulario.setAnotaciones(anotaciones);
            formulario.setPagina(pagina);
            formulario.setNumRelacionAnotaciones(Integer.toString(totalAnotaciones));
            request.setAttribute("AnotacionesOficinasRegistroForm",formulario);
            
            // Auditoria de acceso al registro
            try {
                AuditoriaManager.getInstance().auditarAccesoRegistro(
                        ConstantesAuditoria.REGISTRO_LISTADO_ENVIO_ENTRADAS, usuario, anotaciones);
            } catch (AnotacionRegistroException are) {
                m_Log.error("No se pudo registrar el evento de auditoria", are);
                fallo = "errorTecnico";
            }
            
            opcion = OPCION_BUSQUEDA;            
        }else
         if(OPCION_ACEPTAR.equalsIgnoreCase(opcion)){
            /** Se marcan una o varias anotaciones como aceptadas **/
            log.debug(" ================> buscando anotaciones");            
            String pagina           = formulario.getPagina();
            String listaAnotacionesAceptar = formulario.getListaAnotaciones();
            log.debug("listaAnotacionesAceptar: " + listaAnotacionesAceptar);
            log.debug("pagina: " + pagina);
                        
            ArrayList<AnotacionOficinaRegistroVO> anotaciones = this.tratarListaAnotaciones(listaAnotacionesAceptar);            
            log.debug(" ===========> anotaciones.size() " + anotaciones.size());

            boolean exito = AnotacionOficinaRegistroManager.getInstance().aceptarAnotaciones(anotaciones, usuario, params);
            if(exito){                                
                request.setAttribute("pagina_recargar",pagina);                
            }else{
                request.setAttribute("error_operacion","A");

            }

            opcion = OPCION_ACEPTAR;
        }else
         if(OPCION_RECHAZAR.equalsIgnoreCase(opcion)){
            /** Se marcan una o varias anotaciones como rechazadas **/
            log.debug(" ================> buscando anotaciones");
            String pagina           = formulario.getPagina();
            String listaAnotacionesAceptar = formulario.getListaAnotaciones();
            log.debug("listaAnotacionesAceptar: " + listaAnotacionesAceptar);
            log.debug("pagina: " + pagina);

            ArrayList<AnotacionOficinaRegistroVO> anotaciones = this.tratarListaAnotaciones(listaAnotacionesAceptar);            
            log.debug(" ===========> anotaciones.size() " + anotaciones.size());

            boolean exito = AnotacionOficinaRegistroManager.getInstance().rechazarAnotaciones(anotaciones, usuario, params);
            if(exito){
                request.setAttribute("pagina_recargar",pagina);
            }else{
                request.setAttribute("error_operacion","R");
            }

            opcion = OPCION_RECHAZAR;
        }else
         if(OPCION_ENVIAR.equalsIgnoreCase(opcion)){
            /** Se marcan una o varias anotaciones como enviadas **/            
            String pagina           = formulario.getPagina();
            String listaAnotaciones = formulario.getListaAnotaciones();
            log.debug("listaAnotacionesAceptar: " + listaAnotaciones);
            log.debug("pagina: " + pagina);

            ArrayList<AnotacionOficinaRegistroVO> anotaciones = this.tratarListaAnotaciones(listaAnotaciones);          
            log.debug(" ===========> anotaciones.size() " + anotaciones.size());

            boolean exito = AnotacionOficinaRegistroManager.getInstance().enviarAnotaciones(anotaciones, usuario, params);
            if(exito){
                request.setAttribute("pagina_recargar",pagina);
            }else{
                request.setAttribute("error_operacion","R");
            }

            opcion = OPCION_ENVIAR;
        }else
        if(OPCION_INFORME.equalsIgnoreCase(opcion))
        {
            String codOfiOrigen  = formulario.getCodigoOficinaRegistroOrigen();
            String codOfiDestino = formulario.getCodigoOficinaRegistroDestino();
            String fechaDesde    = formulario.getFechaDesde();
            String fechaHasta     = formulario.getFechaHasta();
            String estado           = formulario.getEstado();
            String listaAnotaciones = formulario.getListaAnotaciones();
            ArrayList<GeneralValueObject> anotaciones=new ArrayList();
            String separador      = "§¥";

            if(("".equals(listaAnotaciones))||(listaAnotaciones==null))
            {
               anotaciones=null;
            }
            else
            {
                anotaciones = this.tratarListaAnotacionesImprimir(listaAnotaciones);
            }


            if(codOfiOrigen!=null && "undefined".equals(codOfiOrigen)) codOfiOrigen = null;
            if(codOfiDestino!=null && "undefined".equals(codOfiDestino)) codOfiDestino = null;

            log.debug("=============> IMPRESION INFORME EN PDF <=====================");
            log.debug("codOfiOrigen " + codOfiOrigen + ", codOfiDestino: " + codOfiDestino + ",fechaDesde: " + fechaDesde + ",fechaHasta: " + fechaHasta +  ",estado: " + estado);
            idioma  = Integer.toString(usuario.getIdioma());
            nombre = request.getParameter("nombre");
            escudo  = request.getParameter("escudo");
            pdfFile   = request.getParameter("pdfFile");

            log.debug("Parametros idioma: " + idioma + ", escudo: " + escudo + ", nombre: " + nombre + ", pdfFile: " + pdfFile);
            RegistroUsuarioValueObject registroUsuario = (RegistroUsuarioValueObject)session.getAttribute("registroUsuario");                        
            String uor = Integer.toString(registroUsuario.getUnidadOrgCod());
            String codDepartamento = Integer.toString(registroUsuario.getDepCod());
            log.debug("codDepartamento: " + codDepartamento);

            String claveIdioma=InformesManager.getInstance().getClaveIdioma(params,idioma);
            log.debug("********** claveIdioma: " + claveIdioma);
            //private String relacionUnidadesOrganicas(String claveIdioma,String fechaInicio,String fechaFin,String tipo,String codUorRegistro,String codDepartamento,UsuarioValueObject usuario) {

            String name = this.relacionUnidadesOrganicas(claveIdioma, fechaDesde, fechaHasta, "E", uor, codDepartamento,codOfiOrigen,codOfiDestino,estado,usuario,anotaciones);
            log.debug("Nombre fichero informe: " + name);            
            request.setAttribute("nombre", name);
            request.setAttribute("fallo", fallo);
            request.setAttribute("numAsientos", numAsientos);
            request.setAttribute("maxAsientos", maxAsientos);      
            opcion = OPCION_INFORME;

        }//if

        log.debug("EnvioAnotacionOficinasRegistroAction <=================");
        return mapping.findForward(opcion);
    }


    private ArrayList<AnotacionOficinaRegistroVO> tratarListaAnotaciones(String lista){
        ArrayList<AnotacionOficinaRegistroVO> anotaciones = new ArrayList<AnotacionOficinaRegistroVO>();
        String separador      = "§¥";
        String DOT_COMMA = ";";

        log.debug("==========> tratarListaAnotaciones parametro: " + lista);
        String[] anotacion = lista.split(separador);
        for(int i=0;i<anotacion.length;i++){
            String anot = anotacion[i];
            log.debug(" ====> anotacion : " + anot);

            String[] infoAnotacion = anot.split(DOT_COMMA);
            if(infoAnotacion!=null && infoAnotacion.length>=5){
                AnotacionOficinaRegistroVO a = new AnotacionOficinaRegistroVO();
                a.setNumero(infoAnotacion[0]);
                a.setEjercicio(infoAnotacion[1]);
                a.setTipoEntrada(infoAnotacion[2]);
                a.setCodDepartamento(infoAnotacion[3]);
                a.setUor(infoAnotacion[4]);
                anotaciones.add(a);
            }//if
        }//for

        return anotaciones;
    }
    private ArrayList<GeneralValueObject> tratarListaAnotacionesImprimir(String lista){
        ArrayList<GeneralValueObject> anotaciones = new ArrayList<GeneralValueObject>();
        String separador      = "§¥";
        String DOT_COMMA = ";";

        log.debug("==========> tratarListaAnotaciones parametro: " + lista);
        String[] anotacion = lista.split(separador);
        for(int i=0;i<anotacion.length;i++){
            String anot = anotacion[i];
            log.debug(" ====> anotacion : " + anot);

            String[] infoAnotacion = anot.split(DOT_COMMA);
            if(infoAnotacion!=null && infoAnotacion.length==7){
                GeneralValueObject a = new GeneralValueObject();
                a.setAtributo("numero", infoAnotacion[0]);
                a.setAtributo("ejercicio", infoAnotacion[1]);
                a.setAtributo("tipo", infoAnotacion[2]);
                a.setAtributo("ofiOrigen", infoAnotacion[5]);
                a.setAtributo("ofiDestino", infoAnotacion[6]);
                anotaciones.add(a);
            }//if
        }//for

        return anotaciones;
    }



    private File encabezadoUOR1(int num, String codigoUnidad,String claveIdioma,String tipoEntrada,String fechaDesde,String fechaHasta,String codigoUnidadOrigen,UsuarioValueObject usuario) throws TechnicalException {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdf_dir);
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        gVO.setAtributo("usuDir", usuario.getDtr());
        gVO.setAtributo("pdfFile", "registro");

        Config m_Conf = ConfigServiceHelper.getConfig("common");
        pdf_dir = m_Conf.getString("PDF.base_dir");


        pdfFile = "informeLanbide";
        gVO.setAtributo("pdfFile", pdfFile);

        // original
        //gVO.setAtributo("opcionint",request.getParameter("interopcion"));
        gVO.setAtributo("opcionint","op1");
        // original
        //GeneralPDF pdf = new GeneralPDF(params, gVO);
        GeneralPDF pdf = new GeneralPDF(usuario.getParamsCon(), gVO);

        String tipo = "";
        String desOri = "";

       int idi=Integer.parseInt(idioma);
       descriptor.setIdi_cod(idi);
        if (tipoEntrada.equalsIgnoreCase("E")) {
            tipo = descriptor.getDescripcion(1, "gEtiq_INFENTRADA")+" "+ fechaDesde.substring(0, 10)+" "+descriptor.getDescripcion(1, "gEtiq_HASTA")+" "+ fechaHasta.substring(0, 10);
            desOri =descriptor.getDescripcion(1, "gEtiq_DESTINO");
        }
       
        
        tipo=StringEscapeUtils.escapeSql(tipo);
        String sql = "SELECT DISTINCT '" + tipo + "' AS TIP,'" + desOri + "' AS DOR,'";
        if (escudo != null) {
            sql += usuario.getOrgIco().substring(1);
        }
        sql += "' AS IMG,'";
        if (nombre != null) {
            sql += usuario.getEnt();
        }

        sql += "' AS ENT,uor_dest.UOR_NOM AS UOR,uor_orig.UOR_NOM AS ORIGEN, " + num + " AS"
                + " OPC FROM A_UOR uor_orig, A_UOR uor_dest WHERE uor_dest.UOR_COD=" // request.getParameter("txtCodigoUnidad");
                + codigoUnidad +" AND uor_orig.UOR_COD="+ codigoUnidadOrigen;


        if (m_Log.isDebugEnabled()) {
            m_Log.debug("encabezadoRegistro: " + sql);
        }
        File xmlEncabezado;
        if (tipoEntrada.equalsIgnoreCase("E")) {
             xmlEncabezado = pdf.construyeTabla(sql, "encabezadoRegistroOficinaUorE_"+claveIdioma);
        } else {
            xmlEncabezado = pdf.construyeTabla(sql, "encabezadoRegistroUorS_"+claveIdioma);
        }
        return xmlEncabezado;
    }


    private ArrayList<String> getOficinasFromAnotaciones(ArrayList<GeneralValueObject> anotaciones, String tipo)
    {
        ArrayList<String> unidades;
        unidades= new ArrayList<String>();
        
        if ("origen".equals(tipo))
        {
             for(int i=0;i<anotaciones.size();i++){
                 GeneralValueObject anot = anotaciones.get(i);
                 String ofiOrigen=(String)anot.getAtributo("ofiOrigen");
                 if (!unidades.contains(ofiOrigen))
                 {
                     unidades.add(ofiOrigen);
                 }
                 
             }
            
        }else if ("destino".equals(tipo))
        {
            for(int i=0;i<anotaciones.size();i++){
                 GeneralValueObject anot = anotaciones.get(i);
                 String ofiDestino=(String)anot.getAtributo("ofiDestino");
                 if (!unidades.contains(ofiDestino))
                 {
                     unidades.add(ofiDestino);
                 }
                 
             }
            
        }else return null;
        
        return unidades;
    }
    
  

     /**
     * Función encargada de realizar el informe de unidades orgánicas
     *
     * @return Un String con la ruta del pdf generado
     */
    private String relacionUnidadesOrganicas(String claveIdioma,String fechaInicio,String fechaFin,String tipo,
            String codUorRegistro,String codDepartamento,String codOficinaOrigen,String codOficinaDestino,
            String estado,UsuarioValueObject usuario,ArrayList<GeneralValueObject> anotaciones) {
        int numAsientos = 0;
        ArrayList<String> unidades;
        ArrayList<String> unidadesOrigen;
        Vector ficheros = new Vector();
        GeneralPDF pdf = null;
        String codUnidad = "";
        String[] params = usuario.getParamsCon();
        int numDias = 0;        
        String ejercicio = fechaInicio.substring(6);
        
        Config m_Conf = ConfigServiceHelper.getConfig("common");
        pdf_dir = m_Conf.getString("PDF.base_dir");

        List<AnotacionRegistroVO> listaAnotacionesImpresas = new ArrayList<AnotacionRegistroVO>();
        GeneralValueObject consultaVO = new GeneralValueObject();
        consultaVO.setAtributo("tipo", tipo);
        consultaVO.setAtributo("fechaInicio", fechaInicio);
        consultaVO.setAtributo("regInicio", null);
        consultaVO.setAtributo("regFinal", null);
        consultaVO.setAtributo("fechaFin", fechaFin);
        consultaVO.setAtributo("unidadOrg", codUorRegistro);
        consultaVO.setAtributo("depto", codDepartamento);

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
        gVO.setAtributo("opcionint","op1") ;

        unidades = new ArrayList<String>();
        unidadesOrigen = new ArrayList<String>();
        boolean ofiDestinoLaDelUsuario = false;
        boolean ofiOrigenLaDelUsuario = false;
        String nombreOficinaOrigen="";
        try{

        
            // Se recupera la oficina de registro del usuario actual
            OficinaRegistroLanbideVO oficina = AnotacionOficinaRegistroManager.getInstance().getOficinaRegistroUsuario(Integer.parseInt(codUorRegistro),usuario, params);

            nombreOficinaOrigen=oficina.getNombreOficina();
            
            if(!"".equals(codOficinaOrigen) && codOficinaDestino!=null && "-1".equals(codOficinaDestino)){
                
                //Se agrupa por todas las unidades de destino
                //unidades = AnotacionOficinaRegistroManager.getInstance().getListaCodigosUorsDestinoTodasOficinas(usuario.getParamsCon());
                //a partir de 14-5-2013: Se obtienes las unidades de destino de los registros seleccionados (o todos), en vez de obtener todas las oficinas, ya que asi mejoramos la velocidad
                
                if (anotaciones==null) unidades = AnotacionOficinaRegistroManager.getInstance().getListaCodigosUorsDestinoTodasOficinas(usuario.getParamsCon());
                else unidades= getOficinasFromAnotaciones(anotaciones,"destino");
                
                if("-1".equals(codOficinaOrigen))
                { 
                    //También se agrupa por las unidades de origen
                    if (anotaciones==null)unidadesOrigen=unidades;
                    else unidadesOrigen= getOficinasFromAnotaciones(anotaciones,"origen");
                    
                }else
                {
                    unidadesOrigen.add(codOficinaOrigen);
                    if(codOficinaOrigen.equals(oficina.getCodOficina())){
                    // Si la oficina de origen es la del usuario actual
                    ofiOrigenLaDelUsuario = true;
                    }
                }
            }
            else if(!"".equals(codOficinaOrigen) && codOficinaDestino!=null && !"-1".equals(codOficinaDestino)){
               // Se agrupa sólo por una oficina de registro determinada
                unidades.add(codOficinaDestino);
                if("-1".equals(codOficinaOrigen))
                {
                    //Se agrupa por las unidades de origen
                    if (anotaciones==null) unidadesOrigen = AnotacionOficinaRegistroManager.getInstance().getListaCodigosUorsDestinoTodasOficinas(usuario.getParamsCon());                
                    else unidadesOrigen= getOficinasFromAnotaciones(anotaciones,"origen");
                    
                }else
                {
                    unidadesOrigen.add(codOficinaOrigen);
                    if(codOficinaOrigen.equals(oficina.getCodOficina())){
                    // Si la oficina de origen es la del usuario actual
                    ofiOrigenLaDelUsuario = true;
                    } 
                }
                
           
            }else
            if(!"".equals(codOficinaOrigen) && (codOficinaDestino==null || "".equals(codOficinaDestino))){
                // La oficina de destino es la del usuario actual
                /** ORIGINAL
                ArrayList<OficinaRegistroLanbideVO> oficinasRegistro = AnotacionOficinaRegistroManager.getInstance().getUnidadesUsuarioTipoOficinasRegistro(usuario, params);                
                if(oficinasRegistro!=null && oficinasRegistro.size()==1){
                    unidades.add(oficinasRegistro.get(0).getCodOficina());
                }*/
                                
                unidades.add(oficina.getCodOficina());
                ofiDestinoLaDelUsuario = true; // La oficina de destino es la del usuario actual
                
                if("-1".equals(codOficinaOrigen))
                {
                    //Se agrupa por las unidades de origen
                   if (anotaciones==null) unidadesOrigen = AnotacionOficinaRegistroManager.getInstance().getListaCodigosUorsDestinoTodasOficinas(usuario.getParamsCon());                
                    else unidadesOrigen= getOficinasFromAnotaciones(anotaciones,"origen");
                    
                }else
                {
                    unidadesOrigen.add(codOficinaOrigen);
                    if(codOficinaOrigen.equals(oficina.getCodOficina())){
                    // Si la oficina de origen es la del usuario actual
                    ofiOrigenLaDelUsuario = true; 
                    } 
                }
            }
            
            

        }catch(Exception e){
            e.printStackTrace();
        }

        if (unidades.isEmpty()) {
            return "NO EXISTE";
        } else {
            for (int numUnidadesOrigen = 0; numUnidadesOrigen < unidadesOrigen.size(); numUnidadesOrigen++) {
                m_Log.debug(".......................Se procesa la unidad de origen " + unidadesOrigen.get(numUnidadesOrigen));

                for (int numUnidades = 0; numUnidades < unidades.size(); numUnidades++) {
                    consultaVO.setAtributo("unidad", unidades.get(numUnidades));
                    pdf = new GeneralPDF(params, gVO);
                    String pieFile;
                    pieFile = pdf.construyeTablaVacia("pieNumPagina_"+claveIdioma).getPath();
                    pdf.setPie(pieFile);

                    m_Log.debug(".......................Se procesa la unidad de destino " + unidades.get(numUnidades));

                    String xsl = "";


                    if (tipo.equalsIgnoreCase("E")) {                   
                        xsl = "librocOfiE_"+claveIdioma;
                    } else if (tipo.equalsIgnoreCase("S")) {
                        xsl = "librocS_"+claveIdioma;                   
                    }

                    int antes = ficheros.size();
                    File file;
                    try {

                        String consulta = AnotacionOficinaRegistroManager.getInstance().consulta_fecha
                                ((String)unidades.get(numUnidades), fechaInicio, fechaFin,false,ejercicio,
                                null,"E",codUorRegistro,estado,ofiDestinoLaDelUsuario,
                                ofiOrigenLaDelUsuario,unidadesOrigen.get(numUnidadesOrigen),usuario);
                        log.debug("************** EnvioAnotacionOficinasRegistroAction consulta: " + consulta);
                        
                        

                        if (anotaciones==null)
                        {
                            file = pdf.construyeTabla(consulta, xsl, listaAnotacionesImpresas);
                        }
                        else file = pdf.construyeTablaListadoEntradasOficina(consulta, xsl,anotaciones, listaAnotacionesImpresas);

                       if (file != null) {
                            if (!ficheros.isEmpty()) {
                                ficheros.add(pdf.construyeTablaVacia("saltoPagina"));
                            }

                            if (file != null) {
                            // original
                            //ficheros.add(encabezadoUOR1(1, (String) unidades.get(numUnidades),claveIdioma));
                            ficheros.add(encabezadoUOR1(1, (String) unidades.get(numUnidades),claveIdioma,tipo,fechaInicio,fechaFin,(String)unidadesOrigen.get(numUnidadesOrigen),usuario));
                            ficheros.add(file);//añado el dia del libro
                            }
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

                    } catch (TechnicalException te) {
                        te.printStackTrace();
                        fallo = "errorTecnico";
                        return "";
                    }
                    numAsientos += pdf.getNumFilas();
                }
            }
                if (m_Conf.getString("PDF.pie_registro").equals("si")) {
                    Hashtable tabla = new Hashtable();
                    ficheros.add(pdf.construyeTablaVacia2("pieHoja3_"+claveIdioma, tabla));

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
                        ConstantesAuditoria.REGISTRO_INFORME_ENVIO_ENTRADAS,
                        usuario,
                        (String) consultaVO.getAtributo("depto"),
                        (String) consultaVO.getAtributo("unidadOrg"),
                        listaAnotacionesImpresas);
            } catch (AnotacionRegistroException are) {
                m_Log.error("No se pudo registrar el evento de auditoria", are);
                fallo = "errorTecnico";
                return "";
            }
            
            return pdf.getPdf(ficheros);
        }
    }


    private int estimacionMaximoAsientos() {
        int memoriaJVMKbytes = (int) Runtime.getRuntime().maxMemory() / 1024;
        log.debug("Memoria JVM en KBytes : " + memoriaJVMKbytes);
        // Se estima que se necesitan 30Mb mas 90Kb por asiento para el parsing y se
        // ha comprobado que con 64MB se pueden manejar 600 asientos, se pone como minimo.
        int max = (memoriaJVMKbytes - 40 * 1024) / 90;
        if (max < 600) {
            max = 500;
        }
        m_Log.debug("Numero maximo de asientos estimado : " + max);
        return max;
    }

}
