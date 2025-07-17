package es.altia.agora.interfaces.user.web.informes; 

// PAQUETES IMPORTADOS
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.informes.InformesValueObject;
import es.altia.agora.business.informes.persistence.InformesManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.helper.ActionHelper;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.sqlxmlpdf.GeneralPDF;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * <p>Título: @gora</p>
 * <p>Descripción: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author sin atribuir
 * @version 1.0
 */

public class InformesAction extends ActionSession{

   protected static Log log =
           LogFactory.getLog(InformesAction.class.getName());

   protected static Config m_Conf = ConfigServiceHelper.getConfig("common");


   public ActionForward performSession(ActionMapping mapping,ActionForm form,
	   HttpServletRequest request,HttpServletResponse response)
	   throws IOException, ServletException{
	
       log.debug("===================== InformesAction ================>");

	ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));
	ActionErrors errors = new ActionErrors();
	// Validaremos los parametros del request especificados
	HttpSession session = request.getSession();
	String opcion = request.getParameter("opcion");
	UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
	String[] params = usuario.getParamsCon();


        if ("cargar".equalsIgnoreCase(opcion) || "cargarHTM".equalsIgnoreCase(opcion) ||
            "volverCargar".equalsIgnoreCase(opcion)){
           if (form == null){
                form = new InformesForm();
                if ("request".equals(mapping.getScope())){
                   request.setAttribute(mapping.getAttribute(), form);
                   m_Log.debug("el form está en el request");
                }
                else{
                   session.setAttribute(mapping.getAttribute(), form);
                   m_Log.debug("el form está en el session");
                }
	   }

           UsuarioValueObject usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
           int idioma =  usuarioVO.getIdioma();
           int entCod = usuarioVO.getEntCod();
           int codOrg = usuarioVO.getOrgCod();
           String lenguaje = Integer.toString(idioma);
           String municipio = Integer.toString(codOrg);
           String filtroReq = "";
           String grupoReq = "";
           String tipoSalida = "";
           String tiempo = "";
           String verPend = "";
           String verFin = "";
           String verVol = "";
           String todos = "";
           if (m_Log.isInfoEnabled()) m_Log.info("la opcion en el action es " + opcion);
           String fechaInicio = null;
           String fechaFin = null;
           
           if("cargarHTM".equalsIgnoreCase(opcion)) {
	           filtroReq = request.getParameter("filtros");
	           grupoReq = request.getParameter("grupos");
	           tipoSalida = request.getParameter("tipoSalida");
	           if (tipoSalida==null) tipoSalida="HTM";
	           tiempo = request.getParameter("tiempo");
	           if (tiempo == null) tiempo = "ALL";
	           verPend = request.getParameter("verPend");
	           if (verPend == null) verPend = "true";
	           verFin = request.getParameter("verFin");
	           if (verFin == null) verFin = "true";
	           verVol = request.getParameter("verVol");
	           if (verVol == null) verVol = "true";
	           todos = crearParametroTodos(request);
	           opcion = "cargar";
	         } else if("volverCargar".equalsIgnoreCase(opcion)) {
	         	 todos = (String) session.getAttribute("todos");
	         	 GeneralValueObject g = new GeneralValueObject();
	         	 g = recuperaParametrosDesdeTodos(todos); 
	         	 filtroReq = (String) g.getAtributo("filtroReq");
	         	 grupoReq = (String) g.getAtributo("grupoReq");
	         	 tipoSalida = (String) g.getAtributo("tipoSalida");
	         	 tiempo = (String) g.getAtributo("tiempo");
	         	 verPend = (String) g.getAtributo("verPend");
	         	 verFin = (String) g.getAtributo("verFin");
	         	 verVol = (String) g.getAtributo("verVol");
	         	 opcion = "cargar";
           } else {
           	 filtroReq = request.getParameter("Filtro");
	           grupoReq = request.getParameter("Grupo");
	           tipoSalida = request.getParameter("TipoSalida");
	           if (tipoSalida==null) tipoSalida="HTM";
	           tiempo = request.getParameter("Tiempo");
	           if (tiempo == null) tiempo = "ALL";
	           verPend = request.getParameter("VerPend");
	           if (verPend == null) verPend = "true";
	           verFin = request.getParameter("VerFin");
	           if (verFin == null) verFin = "true";
	           verVol = request.getParameter("VerVol");
	           if (verVol == null) verVol = "true";
           }

           
           /**** prueba ****/
           if(tiempo!=null && !"".equals(tiempo) && !"ALL".equalsIgnoreCase(tiempo) && !"TRI".equalsIgnoreCase(tiempo) && !"MES".equalsIgnoreCase(tiempo) && !"SEM".equalsIgnoreCase(tiempo)){
               int posicion = tiempo.indexOf("||");
               fechaInicio = tiempo.substring(0,10);
               fechaFin    = tiempo.substring(12,tiempo.length());
               
               request.setAttribute("fechaInicio",fechaInicio);
               request.setAttribute("fechaFin",fechaFin);
           }
           /**** prueba ****/
           
           GeneralValueObject paramsPagina = new GeneralValueObject();
           paramsPagina.setAtributo("lenguaje",lenguaje);
           paramsPagina.setAtributo("municipio",municipio);
           paramsPagina.setAtributo("TipoSalida",tipoSalida);
           paramsPagina.setAtributo("Tiempo",tiempo);
           paramsPagina.setAtributo("VerPend",verPend);
           paramsPagina.setAtributo("VerFin",verFin);
           paramsPagina.setAtributo("VerVol",verVol);
           paramsPagina.setAtributo("todos",todos);
           session.setAttribute("todos", todos);

           Vector grupos = new Vector();
           if ((grupoReq!=null)&&(!grupoReq.equals(""))){
             String[] grupo = split (grupoReq, ",");
             for (int i=0; i<grupo.length; i++){
               grupos.add(grupo[i]);
             }
           }

           Vector filtros = new Vector();
           if ((filtroReq!=null)&&(!filtroReq.equals(""))&&(!filtroReq.equals(",0"))){
             String[] f = split(filtroReq, ",");
             for (int i=0; i < (f.length-1) ; i+=2){
               String nombre = f[i];
               String valor = f[i+1];
               if(!"-1".equals(valor)) {
                 GeneralValueObject filtro = new GeneralValueObject();
                 filtro.setAtributo("nombre",nombre);
                 filtro.setAtributo("valor",valor.toUpperCase());
                 filtros.add(filtro);
               }
             }
           }

           //Al parecer esto esta para el caso en que no hay expedientes, pero se hacen pruebas y funciona correctamente con esto comentado #10661
//           if("true".equals(verVol)) {
//           	 String respuesta = InformesManager.getInstance().verExisteExpedientes(params, filtros, grupos, paramsPagina);
//           	 if("noHayExpedientes".equals(respuesta)) {
//           	 	 verVol = "false";
//           	 	 paramsPagina.setAtributo("VerVol",verVol);
//           	 }
//           }

           Vector estadisticas = InformesManager.getInstance().generarInforme(params, filtros, grupos, paramsPagina);
           
           ((InformesForm)form).setEstadisticas(estadisticas);
	         ((InformesForm)form).setTipoSalida(tipoSalida);
	         
	         if ((tipoSalida!=null)&&(tipoSalida.equals("PDF"))){
	           opcion = "redirige";
	           InformesForm iForm = (InformesForm)form;
                String sUrl = request.getSession().getServletContext().getRealPath("/");
               request.setAttribute("nombre",imprimirPDF(iForm.getInformesVO(),usuario, estadisticas, sUrl));
	         }

           if(estadisticas.size() < 2 && (tipoSalida!=null)&&(tipoSalida.equals("HTM"))) {
	           opcion = "consultaVacia"; 
	         }

        }
        log.debug("===================== InformesAction ================>");
	return (mapping.findForward(opcion));
   }
   
   String crearParametroTodos(HttpServletRequest request){

    String filtroReq = "";
    String grupoReq = "";
    String tipoSalida = "";
    String tiempo = "";
    String verPend = "";
    String verFin = "";
    String verVol = "";
        filtroReq = request.getParameter("filtros");
	  grupoReq = request.getParameter("grupos");
	  tipoSalida = request.getParameter("tipoSalida");
	  if (tipoSalida==null) tipoSalida="HTM";
	  tiempo = request.getParameter("tiempo");
	  if (tiempo == null) tiempo = "ALL";
	  verPend = request.getParameter("verPend");
	  if (verPend == null) verPend = "true";
	  verFin = request.getParameter("verFin");
	  if (verFin == null) verFin = "true";
	  verVol = request.getParameter("verVol");
	  if (verVol == null) verVol = "true";
    
	  String todos = "";
	  if(filtroReq != null && !filtroReq.equals("")) {
	  	todos = filtroReq + "§¥";
	  } else {
	  	todos = "%" + "§¥";
	  }
	  if(grupoReq != null && !grupoReq.equals("")) {
	  	todos += grupoReq + "§¥";
	  } else {
	  	todos += "%" + "§¥";
	  }
	  if(tipoSalida != null && !tipoSalida.equals("")) {
	  	todos += tipoSalida + "§¥";
	  } else {
	  	todos += "%" + "§¥";
	  }
	  if(tiempo != null && !tiempo.equals("")) {
	  	todos += tiempo + "§¥";
	  } else {
	  	todos += "%" + "§¥";
	  }
	  if(verPend != null && !verPend.equals("")) {
	  	todos += verPend + "§¥";
	  } else {
	  	todos += "%" + "§¥";
	  }
	  if(verFin != null && !verFin.equals("")) {
	  	todos += verFin + "§¥";
	  } else {
	  	todos += "%" + "§¥";
	  }
	  if(verVol != null && !verVol.equals("")) {
	  	todos += verVol + "§¥";
	  } else {
	  	todos += "%" + "§¥";
	  }
	  
    return todos;
  }
  
  GeneralValueObject recuperaParametrosDesdeTodos(String todos){
    GeneralValueObject gVO = new GeneralValueObject();
    Vector lista = new Vector();
    Vector listaNombres = new Vector();
    listaNombres.addElement("filtroReq");
    listaNombres.addElement("grupoReq");
    listaNombres.addElement("tipoSalida");
    listaNombres.addElement("tiempo");
    listaNombres.addElement("verPend");
    listaNombres.addElement("verFin");
    listaNombres.addElement("verVol");
    
    StringTokenizer valores = null;
	  if (todos != null) {
	    valores = new StringTokenizer(todos,"§¥",false);
	    while (valores.hasMoreTokens()) {
	      String valor = valores.nextToken();
	      if(valor == null || valor.equals("%")) {
	      	valor = "";
	      }
	      lista.addElement(valor);
	    }
	  }
	  if(lista.size() > 0) {
	  	for(int i=0;i<lista.size();i++) {
		    gVO.setAtributo((String)listaNombres.elementAt(i),(String)lista.elementAt(i));
		  }	
	  }
	  return gVO;
  }


    private String imprimirPDF(InformesValueObject iVO, UsuarioValueObject usuVO, Vector estadisticas, String sUrl) {
     GeneralValueObject gVO = new GeneralValueObject();

     gVO.setAtributo("baseDir", m_Conf.getString("PDF.base_dir"));
     // PDFS NUEVA SITUACION
     gVO.setAtributo("aplPathReal", this.getServlet().getServletContext().getRealPath(""));
     // FIN PDFS NUEVA SITUACION


     gVO.setAtributo("usuDir", usuVO.getDtr());
     // PDFS NUEVA SITUACION
     gVO.setAtributo("pdfFile", "SGE");
     // FIN PDFS NUEVA SITUACION
     
     //gVO.setAtributo("pdfFile", "informes_pdf");
        int index = sUrl.indexOf("\\");
        if (index > 0) sUrl = sUrl.substring(index);
        gVO.setAtributo("estilo", "css/informesPDF.css");

     GeneralPDF pdf = new GeneralPDF(usuVO.getParamsCon(), gVO);

     //Se leen titulos y criterios para mostrar contenidos, informacion necesaria tanto para
     //la cabecera como para el cuerpo
     GeneralValueObject titulosVO = (GeneralValueObject)estadisticas.remove(0);
     Vector titulos = (Vector)titulosVO.getAtributo("titulos");
     String verPend = (String)titulosVO.getAtributo("VerPend");
     if (verPend==null) verPend = "false";
     String verFin = (String)titulosVO.getAtributo("VerFin");
     String sTiempo = null;
     if ((verFin!=null)&&(verFin.equals("true"))){
            int tiempo = (Integer) titulosVO.getAtributo("tiempo");
       if (tiempo==1) sTiempo = "sem.";
       if (tiempo==2) sTiempo = "mes";
       if (tiempo==3) sTiempo = "trim.";
     }

     if (verFin ==null) verFin = "false";
     String verVol = (String)titulosVO.getAtributo("VerVol");
     if (verVol == null) verVol = "false";

        gVO.setAtributo("pie", pdf.transformaXML(generarXmlPiePdf(), "informesPie").getAbsolutePath());
     gVO.setAtributo("tamPie", "20");
     gVO.setAtributo("pagina", "1");

     pdf = new GeneralPDF(usuVO.getParamsCon(), gVO);
        File pdfFile = pdf.transformaXML(iVO.toXml(estadisticas, titulos, verPend, verFin, verVol, sTiempo), "informes");
        Vector<File> ficheros = new Vector<File>();
        ficheros.add(pdfFile);

     return (pdf.getPdf(ficheros));

   }


   private String generarXmlPiePdf () {

     StringBuffer textoXml = new StringBuffer("");

     textoXml.append ("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
     textoXml.append ("<pie>");
     textoXml.append ("</pie>");

     return textoXml.toString();
   }


   public static String [] split(String line, String delim)
   {
     List list = new ArrayList();
     StringTokenizer t = new StringTokenizer(line, delim);
     while (t.hasMoreTokens())
     {
       list.add(t.nextToken());
     }
     return (String []) list.toArray(new String[list.size()]);
   }

}