// NOMBRE DEL PAQUETE 
package es.altia.agora.interfaces.user.web.terceros;

// PAQUETES IMPORTADOS
import es.altia.util.sqlxmlpdf.*;
import es.altia.util.struts.StrutsUtilOperations;
import es.altia.agora.business.escritorio.*;
import es.altia.agora.business.escritorio.persistence.*;
import es.altia.agora.business.terceros.*;
import es.altia.agora.business.terceros.persistence.*;
import es.altia.agora.business.terceros.mantenimiento.persistence.*;
import es.altia.agora.business.util.*;
import es.altia.agora.interfaces.user.web.helper.*;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import es.altia.common.service.config.*;

import java.io.*;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase FusionDivisionAction</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class FusionDivisionAction extends ActionSession {
    protected static Log m_Log =
            LogFactory.getLog(FusionDivisionAction.class.getName());
    protected static Config common = ConfigServiceHelper.getConfig("common");
    String baseDir = "";
    String pdfDir = "";
    String usuDir = "";
    String filePath = "";
    String fusionFile = "";
    String codPais = "";
    String codProvincia = "";
    String codMunicipio = "";

    public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        m_Log.debug("perform");
        ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
        HttpSession session = request.getSession();

        // Validaremos los parametros del request especificados
        ActionErrors errors = new ActionErrors();
        String opcion = request.getParameter("opcion");
        if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);

        // Rellenamos el form FusionDivisionForm
        if (form == null) {
            m_Log.debug("Rellenamos el form FusionDivisionForm");
            form = new FusionDivisionForm();
            if ("request".equals(mapping.getScope())){
                request.setAttribute(mapping.getAttribute(), form);
            }else{
                session.setAttribute(mapping.getAttribute(), form);
            }
        }
        UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        String[] params = usuario.getParamsCon();
        ParametrosTerceroValueObject paramsTercero = (ParametrosTerceroValueObject)session.getAttribute("parametrosTercero");
        //GeneralValueObject paramsPadron = (GeneralValueObject)session.getAttribute("parametrosPadron");
//        GeneralValueObject paramsPadron = ParametrosManager.getInstance().cargarParametrosPadron(params);
        baseDir = common.getString("INE.base_dir");
        pdfDir = common.getString("PDF.base_dir");
        usuDir = usuario.getDtr();
        codPais = paramsTercero.getPais();
        codProvincia = paramsTercero.getProvincia();
        codMunicipio = paramsTercero.getMunicipio();
//        String familiasPadron =  (String)paramsPadron.getAtributo("familias");

        int mostrarFamilia = 0;
//        if (familiasPadron!=null && !"".equals(familiasPadron)) {
//            mostrarFamilia = Integer.parseInt(familiasPadron);
//        }

        FusionDivisionForm fusionForm = (FusionDivisionForm)form;
        UsuarioManager usuarioManager = UsuarioManager.getInstance();
        FusionDivisionManager fusionManager = FusionDivisionManager.getInstance();
        DistritosManager distritosManager = DistritosManager.getInstance();
        SeccionesManager seccionesManager = SeccionesManager.getInstance();
        ViasManager viasManager = ViasManager.getInstance();
        TiposNumeracionManager tiposNumerManager = TiposNumeracionManager.getInstance();
        TramerosManager tramosManager = TramerosManager.getInstance();
        fusionForm.setOpcion(opcion);
        if(opcion.equals("inicializar")){
            fusionForm.setListaUsuarios(usuarioManager.getListaUsuarios(params));
        }else if(opcion.equals("buscarProcesos")){
            GeneralValueObject gVO = recogerParametrosBuscarProcesos(request);
            fusionForm.setListaProcesos(fusionManager.getListaProcesos(params,gVO));
            opcion = "oculto";
        }else if(opcion.equals("inicializarInsertarProceso")){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",codPais);
            gVO.setAtributo("codProvincia",codProvincia);
            gVO.setAtributo("codMunicipio",codMunicipio);
            fusionForm.setListaDistritos(distritosManager.getListaDistritos(gVO,params));
        }else if(opcion.equals("inicializarProcesar")){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",codPais);
            gVO.setAtributo("codProvincia",codProvincia);
            gVO.setAtributo("codMunicipio",codMunicipio);
            gVO.setAtributo("codECO","");
            gVO.setAtributo("codESI","");
            gVO.setAtributo("codNUC","");
            gVO.setAtributo("numDesde","");
            gVO.setAtributo("tipoNumeracion","");
            gVO.setAtributo("distrito",request.getParameter("codDistritoOrigen"));
            gVO.setAtributo("seccion",request.getParameter("codSeccionOrigen"));
            gVO.setAtributo("letra",request.getParameter("letraOrigen"));
            //	Cambio combo viales	  */
            fusionForm.setListaVias(viasManager.getListaViasESIs(params,gVO));
            // fusionForm.setListaVias(viasManager.getListaVias(params,gVO));
            /* Fin cambio combo viales */
            fusionForm.setListaTipoNumeraciones(tiposNumerManager.getListaTiposNumeraciones(params));
        }else if(opcion.equals("retroceder")){
            GeneralValueObject gVO = recogerParametrosEliminarProceso(request);
            fusionForm.setOperacion(fusionManager.retrocederProceso(params,gVO));
            opcion = "oculto";
        }else if(opcion.equals("insertarProceso")){
            GeneralValueObject gVO = recogerParametrosInsertarProceso(request);
            gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
            fusionForm.setOperacion(fusionManager.insertarProceso(params,gVO));
            opcion = "insertarProceso";
        }else if(opcion.equals("eliminarProceso")){
            GeneralValueObject gVO = recogerParametrosEliminarProceso(request);
            fusionForm.setOperacion(fusionManager.eliminarProceso(params,gVO));
            opcion = "oculto";
        }else if(opcion.equals("procesarProceso")){
            GeneralValueObject gVO = recogerParametrosProcesarProceso(request);
            gVO.setAtributo("usuario",String.valueOf(usuario.getIdUsuario()));
            String operacion = "";
            if((mostrarFamilia==0)||(mostrarFamilia==2)) operacion = "NO_EXISTE_HOJA";
            if(mostrarFamilia==1) operacion = "VARIAS_FAMILIAS";
            gVO.setAtributo("operacion",operacion);
//            gVO.setAtributo("familias",paramsPadron.getAtributo("familias"));
            fusionForm.setOperacion(fusionManager.procesarProceso(params,gVO));

            gVO.setAtributo("codECO","");
            gVO.setAtributo("codESI","");
            gVO.setAtributo("codNUC","");
            gVO.setAtributo("numDesde","");
            gVO.setAtributo("tipoNumeracion","");
            gVO.setAtributo("distrito",request.getParameter("codDistritoOrigen"));
            gVO.setAtributo("seccion",request.getParameter("codSeccionOrigen"));
            gVO.setAtributo("letra",request.getParameter("letraOrigen"));
            //	Cambio combo viales
            fusionForm.setListaVias(viasManager.getListaViasESIs(params,gVO));
            // fusionForm.setListaVias(viasManager.getListaVias(params,gVO));
            /* Fin cambio combo viales */
            String todos = (String)gVO.getAtributo("traspasarTodos");
            String pasarVial = (String)gVO.getAtributo("traspasarVial");

            if(todos.equals("SI")||pasarVial.equals("SI")){
                m_Log.debug("FusionDivisionAction:inicializar");
                fusionForm.setInicializar("SI");
            }else{
                m_Log.debug("FusionDivisionAction:no inicializar");
                fusionForm.setInicializar("NO");
            }
            opcion = "oculto";
        }else if(opcion.equals("finalizarProceso")){
            GeneralValueObject gVO = recogerParametrosEliminarProceso(request);
            fusionForm.setOperacion(fusionManager.finalizarProceso(params,gVO));
            opcion = "oculto";
        }else if(opcion.equals("buscarTramosProceso")){
            GeneralValueObject gVO = recogerParametrosBuscarTramosProceso(request);
            fusionForm.setDatosVO(tramosManager.buscarTramosProceso(params,gVO));
            opcion = "oculto";
        }else if(opcion.equals("inicializarVerDomicilios")){
            GeneralValueObject gVO = recogerParametrosVerDomicilios(request);
            fusionForm.setDatosVO(fusionManager.verDomicilios(params,gVO));
        }else if(opcion.equals("recargaVerDomicilios")){
            fusionForm.setLineasPagina(Integer.parseInt(request.getParameter("lineasPagina")));
            fusionForm.setPagina(Integer.parseInt(request.getParameter("pagina")));
        }else if(opcion.equals("inicializarVerDetalle")){
            GeneralValueObject gVO = recogerParametrosEliminarProceso(request);
            fusionForm.setDatosVO(fusionManager.verDetalle(params,gVO));
        }else if(opcion.equals("imprimirVerDetalle")){
            GeneralValueObject gVO = fusionForm.getDatosVO();
            gVO.setAtributo("pdfFile","fusionDivision");
            String protocolo = StrutsUtilOperations.getProtocol(request);                
            m_Log.debug("PROTOCOLO en uso :" + protocolo);
            gVO.setAtributo("sUrl", protocolo + "://" + request.getHeader("Host") + request.getContextPath());
            String nombre = imprimirDetalle(gVO,params);
            request.setAttribute("nombre",nombre);
            opcion = "redirige";
        }else if((opcion.equals("cargarSeccionesOrigen"))||
                (opcion.equals("cargarSeccionesDestino"))){
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codPais",codPais);
            gVO.setAtributo("codProvincia",codProvincia);
            gVO.setAtributo("codMunicipio",codMunicipio);
            gVO.setAtributo("codDistrito",request.getParameter("codDistrito"));
            fusionForm.setListaSecciones(seccionesManager.getListaSecciones(gVO,params));
            opcion = "oculto";
        }else if(opcion.equals("salir")){
            if ((session.getAttribute(mapping.getAttribute()) != null))
                session.removeAttribute(mapping.getAttribute());
        }
        /* Redirigimos al JSP de salida*/
        return (mapping.findForward(opcion));
    }

    private GeneralValueObject recogerParametrosInsertarProceso(HttpServletRequest request){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codPais",codPais);
        gVO.setAtributo("codProvincia",codProvincia);
        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("tipoOperacion",request.getParameter("tipoOperacion"));
        gVO.setAtributo("descripcion",request.getParameter("descripcion"));
        gVO.setAtributo("fecha",request.getParameter("fecha"));
        gVO.setAtributo("distritoOrigen",request.getParameter("codDistritoOrigen"));
        gVO.setAtributo("seccionOrigen",request.getParameter("codSeccionOrigen"));
        gVO.setAtributo("letraOrigen",request.getParameter("letraOrigen"));
        gVO.setAtributo("distritoDestino",request.getParameter("codDistritoDestino"));
        gVO.setAtributo("seccionDestino",request.getParameter("codSeccionDestino"));
        gVO.setAtributo("letraDestino",request.getParameter("letraDestino"));
        return gVO;
    }

    private GeneralValueObject recogerParametrosProcesarProceso(HttpServletRequest request){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codPais",codPais);
        gVO.setAtributo("codProvincia",codProvincia);
        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("tipoOperacion",request.getParameter("tipoOperacion"));
        gVO.setAtributo("codProceso",request.getParameter("codProceso"));
        gVO.setAtributo("codECO",request.getParameter("codECO"));
        gVO.setAtributo("codESI",request.getParameter("codESI"));
        gVO.setAtributo("codNUC",request.getParameter("codNUC"));
        gVO.setAtributo("idVia",request.getParameter("idVia"));
        gVO.setAtributo("codTipoNumeracion",request.getParameter("codTipoNumeracion"));
        gVO.setAtributo("distritoOrigen",request.getParameter("codDistritoOrigen"));
        gVO.setAtributo("seccionOrigen",request.getParameter("codSeccionOrigen"));
        gVO.setAtributo("letraOrigen",request.getParameter("letraOrigen"));
        gVO.setAtributo("distritoDestino",request.getParameter("codDistritoDestino"));
        gVO.setAtributo("seccionDestino",request.getParameter("codSeccionDestino"));
        gVO.setAtributo("letraDestino",request.getParameter("letraDestino"));
        gVO.setAtributo("tramosOrigen",request.getParameter("tramosOrigen"));
        gVO.setAtributo("tramosDestino",request.getParameter("tramosDestino"));
        gVO.setAtributo("tramosOrigen",transformarArray((String)gVO.getAtributo("tramosOrigen")));
        gVO.setAtributo("tramosDestino",transformarArray((String)gVO.getAtributo("tramosDestino")));
        gVO.setAtributo("fechaOperacion",request.getParameter("fechaOperacion"));
        gVO.setAtributo("generarOperaciones",request.getParameter("generarOperaciones"));
        gVO.setAtributo("traspasarTodos",request.getParameter("traspasarTodos"));
        gVO.setAtributo("traspasarVial",request.getParameter("traspasarVial"));
        return gVO;
    }

    private GeneralValueObject recogerParametrosBuscarTramosProceso(HttpServletRequest request){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codPais",codPais);
        gVO.setAtributo("codProvincia",codProvincia);
        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("tipoOperacion",request.getParameter("tipoOperacion"));
        gVO.setAtributo("codProceso",request.getParameter("codProceso"));
        gVO.setAtributo("codECO",request.getParameter("codECO"));
        gVO.setAtributo("codESI",request.getParameter("codESI"));
        gVO.setAtributo("codNUC",request.getParameter("codNUC"));
        gVO.setAtributo("idVia",request.getParameter("idVia"));
        gVO.setAtributo("codTipoNumeracion",request.getParameter("codTipoNumeracion"));
        gVO.setAtributo("distritoOrigen",request.getParameter("codDistritoOrigen"));
        gVO.setAtributo("seccionOrigen",request.getParameter("codSeccionOrigen"));
        gVO.setAtributo("letraOrigen",request.getParameter("letraOrigen"));
        gVO.setAtributo("distritoDestino",request.getParameter("codDistritoDestino"));
        gVO.setAtributo("seccionDestino",request.getParameter("codSeccionDestino"));
        gVO.setAtributo("letraDestino",request.getParameter("letraDestino"));
        return gVO;
    }

    private GeneralValueObject recogerParametrosVerDomicilios(HttpServletRequest request){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codPais",codPais);
        gVO.setAtributo("codProvincia",codProvincia);
        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("tipoOperacion",request.getParameter("tipoOperacion"));
        gVO.setAtributo("codProceso",request.getParameter("codProceso"));
        gVO.setAtributo("codECO",request.getParameter("codECO"));
        gVO.setAtributo("codESI",request.getParameter("codESI"));
        gVO.setAtributo("codNUC",request.getParameter("codNUC"));
        gVO.setAtributo("idVia",request.getParameter("idVia"));
        gVO.setAtributo("codTipoNumeracion",request.getParameter("codTipoNumeracion"));
        gVO.setAtributo("codTramo",request.getParameter("codTramo"));
        gVO.setAtributo("numDesde",request.getParameter("numDesde1"));
        gVO.setAtributo("numHasta",request.getParameter("numHasta1"));
        gVO.setAtributo("distritoOrigen",request.getParameter("codDistritoOrigen"));
        gVO.setAtributo("seccionOrigen",request.getParameter("codSeccionOrigen"));
        gVO.setAtributo("letraOrigen",request.getParameter("letraOrigen"));
        gVO.setAtributo("distritoDestino",request.getParameter("codDistritoDestino"));
        gVO.setAtributo("seccionDestino",request.getParameter("codSeccionDestino"));
        gVO.setAtributo("letraDestino",request.getParameter("letraDestino"));
        return gVO;
    }

    private GeneralValueObject recogerParametrosBuscarProcesos(HttpServletRequest request){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codPais",codPais);
        gVO.setAtributo("codProvincia",codProvincia);
        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("tipoOperacion",request.getParameter("tipoOperacion"));
        gVO.setAtributo("fechaDesde",request.getParameter("fechaDesde"));
        gVO.setAtributo("fechaHasta",request.getParameter("fechaHasta"));
        gVO.setAtributo("usuario",request.getParameter("codUsuario"));
        gVO.setAtributo("estado",request.getParameter("codEstado"));
        return gVO;
    }

    private GeneralValueObject recogerParametrosEliminarProceso(HttpServletRequest request){
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("codProceso",request.getParameter("codProceso"));
        return gVO;
    }

    private Vector transformarArray(String array){
        Vector resultado = new Vector();
        StringTokenizer tramos = new StringTokenizer(array,"§");
        while(tramos.hasMoreTokens()){
            String tramoString = tramos.nextToken();
            StringTokenizer tramo = new StringTokenizer(tramoString, ",");
            GeneralValueObject gVO = new GeneralValueObject();
            while (tramo.hasMoreTokens()) {
                String temp = tramo.nextToken();
                gVO.setAtributo("codTramo",temp.substring(0,temp.length()-1));
                temp = tramo.nextToken();
                gVO.setAtributo("distrito",temp.substring(0,temp.length()-1));
                temp = tramo.nextToken();
                gVO.setAtributo("seccion",temp.substring(0,temp.length()-1));
                temp = tramo.nextToken();
                gVO.setAtributo("letra",temp.substring(0,temp.length()-1));
                temp = tramo.nextToken();
                gVO.setAtributo("numDesde",temp.substring(0,temp.length()-1));
                temp = tramo.nextToken();
                gVO.setAtributo("letraDesde",temp.substring(0,temp.length()-1));
                temp = tramo.nextToken();
                gVO.setAtributo("numHasta",temp.substring(0,temp.length()-1));
                temp = tramo.nextToken();
                gVO.setAtributo("letraHasta",temp.substring(0,temp.length()-1));
                temp = tramo.nextToken();
                gVO.setAtributo("accion",temp.substring(0,temp.length()-1));
            }
            resultado.add(gVO);
        }
        return resultado;
    }

    private String imprimirDetalle(GeneralValueObject datosVO,String[] params) {
        GeneralValueObject gVO = new GeneralValueObject();
        gVO.setAtributo("baseDir", pdfDir);
        // PDFS NUEVA SITUACION
        gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
        // FIN PDFS NUEVA SITUACION
        gVO.setAtributo("usuDir", usuDir);
        // PDFS NUEVA SITUACION
        gVO.setAtributo("pdfFile", "terceros");
        // FIN PDFS NUEVA SITUACION

        //gVO.setAtributo("pdfFile", datosVO.getAtributo("pdfFile"));
        gVO.setAtributo("estilo", "css/fusionDivision.css");
        Vector ficheros = new Vector();
        String textoXML = obtenerTextoDetalleXML(datosVO);
        String plantilla = "cabeceraFusionDivision";
        GeneralPDF pdf = new GeneralPDF(params, gVO);
        File cabecera = pdf.transformaXML(textoXML,plantilla);
        gVO.setAtributo("cabecera",cabecera.getPath());
        pdf = new GeneralPDF(params, gVO);
        plantilla = "detalleFusionDivision";
        ficheros.add(pdf.transformaXML(textoXML,plantilla));
        return (pdf.getPdf(ficheros));
    }

    private String obtenerTextoDetalleXML(GeneralValueObject gVO){
        String xml = "";
        xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+
                "<resumenFusionDivision>"+
                "<descripcion>"+gVO.getAtributo("descripcion")+"</descripcion>"+
                "<fecha>"+gVO.getAtributo("fechaProceso")+"</fecha>";
        Vector vias = (Vector)gVO.getAtributo("vias");
        int lengthVias = vias.size();
        int i = 0;
        for(i=0;i<lengthVias;i++){
            GeneralValueObject via = (GeneralValueObject)vias.get(i);
            xml+= "<via>";
            xml+="<codVia>"+via.getAtributo("codVia")+"</codVia>";
            xml+="<nombreVia>"+via.getAtributo("descVia")+"</nombreVia>";
            Vector numeraciones = (Vector)via.getAtributo("numeraciones");
            int lengthNum = numeraciones.size();
            int j=0;
            xml+= "<numeraciones>";
            for(j=0;j<lengthNum;j++){
                xml+= "<numeracion>";
                GeneralValueObject numeracion = (GeneralValueObject)numeraciones.get(j);
                int tipoNumeracion = Integer.parseInt((String)numeracion.getAtributo("tipoNumeracion"));
                String tipoNumer = "";
                if(tipoNumeracion==0){
                    tipoNumer = "Sin Numeración";
                }else if(tipoNumeracion==1){
                    tipoNumer = "Numeración impar";
                }else if(tipoNumeracion==2){
                    tipoNumer = "Numeración par";
                }
                xml+="<tipoNumeracion>"+tipoNumer+"</tipoNumeracion>";
                Vector tramos = (Vector)numeracion.getAtributo("tramos");
                int lengthTramos = tramos.size();
                int k=0;
                for(k=0;k<lengthTramos;k++){
                    xml+= "<tramo>";
                    GeneralValueObject tramo = (GeneralValueObject)tramos.get(k);
                    xml+="<descripcion>"+"Tramo "+k+": "+tramo.getAtributo("numDesde")+"-"+tramo.getAtributo("letraDesde");
                    xml+=" a "+tramo.getAtributo("numHasta")+"-"+tramo.getAtributo("letraHasta");
                    xml+=" Habitantes: "+tramo.getAtributo("habitantes");
                    xml+="</descripcion>";
                    xml+="</tramo>";
                }
                xml+= "</numeracion>";
            }
            xml+= "</numeraciones>";
            xml+="</via>";

        }
        xml+="</resumenFusionDivision>";
        return xml;
    }

}
