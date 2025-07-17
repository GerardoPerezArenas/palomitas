package es.altia.agora.interfaces.user.web.editor;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import es.altia.agora.business.escritorio.*;
import es.altia.common.util.*;
import es.altia.agora.interfaces.user.web.registro.*;
import es.altia.agora.interfaces.user.web.sge.*;
import es.altia.agora.business.registro.*;
import es.altia.agora.business.sge.*;
import es.altia.agora.business.editor.persistence.*;
import es.altia.agora.business.util.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.struts.StrutsUtilOperations;


public class EditorServlet extends HttpServlet
{

  protected static Log log =
          LogFactory.getLog(EditorServlet.class.getName());


  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp)
  throws ServletException, IOException
  {
    doPost(req, resp);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException
  {
        HttpSession misesion;
        RequestDispatcher rd =null;
        Config mTech = ConfigServiceHelper.getConfig("common"); //Para el fichero de configuracion tecnico

        try
        {
            misesion=request.getSession();
        String opcion = null;

      Registro parametros=null;
      MultipartRequest mr=null;

      if(request.getContentType() !=null ) {
        if(request.getContentType().equals("application/x-www-form-urlencoded")) {
          parametros = obtenerParametros(request);
        } else {
          mr = new MultipartRequest(request);
          parametros = obtenerParametros(mr,request);
        }
      }

      if(parametros != null) {
        request.setAttribute("parametros",parametros);
        opcion = parametros.getString("opcion");
      } else {
        opcion = request.getParameter("opcion");
      }
      if (log.isDebugEnabled()) log.debug("la opcion en el Editor servlet es : " + opcion);

      if (opcion == null)
          opcion = "CARGA_INICIAL";

      String[] params = obtenerParametrosConexion(request);

      EditorManagerWord editorMan = EditorManagerWord.getInstance();

      if (opcion.equals("CARGA_INICIAL")) { // Para cargar el editor la primera vez
        Vector etiquetas = editorMan.obtenerDatosEtiquetas(params,parametros);
        request.setAttribute("Etiquetas", etiquetas);
        request.setAttribute("parametros", parametros);
        request.setAttribute("opcion","CARGA_INICIAL");
        rd = getServletContext().getRequestDispatcher("/jsp/editor/editorWord.jsp");
      }
      else if (opcion.equals("GRABAR_PLANTILLA")) {
          MultipartRequest.FileInfo [] files = mr.getFileInfoValues("fichero");
          byte [] fichero = files[0].getContent();
          parametros.put("plt_doc",fichero);
          editorMan.grabarPlantilla(params, parametros);
          rd = getServletContext().getRequestDispatcher("/jsp/editor/ocultoEditor.jsp");
      }
      else if (opcion.equals("grabarCRD")) {
          String nombreDocumento = mr.getParameter("nombreDocumento");
          parametros.setString("nombreDocumento", nombreDocumento);
          parametros = obtenerParametrosDeTramitacionExpedientes(parametros,request);
          MultipartRequest.FileInfo [] files = mr.getFileInfoValues("fichero");
          byte [] fichero = files[0].getContent();
          parametros.put("crd_fil",fichero);
          editorMan.grabarCRD(params, parametros);
          rd = getServletContext().getRequestDispatcher("/jsp/editor/ocultoEditorCronologia.jsp");
      }
      else if (opcion.equals("OBTENER_DATOS_PLANTILLA")) {
        Registro datosPlantilla = editorMan.obtenerDatosPlantilla(params,parametros);

        request.setAttribute("datosPlantilla", datosPlantilla);
        request.setAttribute("opcion","OBTENER_DATOS_PLANTILLA");

        Date fecha = new Date();
        long selloAux = fecha.getTime();
        String sello = ""+selloAux;
        request.setAttribute("sello",sello);

        DataOutputStream fichero = new DataOutputStream(new FileOutputStream(mTech.getString("PATH.publicHtml")+"/documentos/temp/"+sello+".doc"));

        Registro plantillaSinDatos = (Registro)datosPlantilla.get("plantillaSinDatos");
        plantillaSinDatos.setString("host",(String)parametros.get("host"));

        byte [] ficheroWord = (byte [])plantillaSinDatos.get("plt_doc");

        fichero.write(ficheroWord,0,ficheroWord.length);
        fichero.close();

        rd = getServletContext().getRequestDispatcher("/jsp/editor/editorWord.jsp");
      }

      else if (opcion.equals("OBTENER_PLANTILLA") || opcion.equals("VER_PLANTILLA")) { // Para modificarla O VISUALIZARLA DESDE TRAMITES
        Vector etiquetas = editorMan.obtenerDatosEtiquetas(params,parametros);

        request.setAttribute("Etiquetas", etiquetas);
        Registro datosPlantilla = editorMan.obtenerPlantilla(params, parametros);
        datosPlantilla.setString("host",(String)parametros.get("host"));
        datosPlantilla.setString("codClasif",parametros.getString("codClasif"));
        datosPlantilla.setString("descClasif",parametros.getString("descClasif"));
        request.setAttribute("datosPlantilla", datosPlantilla);
        request.setAttribute("opcion","OBTENER_PLANTILLA");

        Date fecha = new Date();
        long selloAux = fecha.getTime();
        String sello = ""+selloAux;
        request.setAttribute("sello",sello);
        DataOutputStream fichero = new DataOutputStream(new FileOutputStream(mTech.getString("PATH.publicHtml")+"/documentos/temp/"+sello+".doc"));

        byte [] ficheroWord = (byte [])datosPlantilla.get("plt_doc");
        fichero.write(ficheroWord,0,ficheroWord.length);
        fichero.close();
        if (opcion.equals("OBTENER_PLANTILLA"))
          rd = getServletContext().getRequestDispatcher("/jsp/editor/editorWord.jsp");
        else if (opcion.equals("VER_PLANTILLA"))
        {
          request.setAttribute("opcion","VER_PLANTILLA");
          rd = getServletContext().getRequestDispatcher("/jsp/editor/ocultoEditorWord.jsp");
        }
      }
      else if (opcion.equals("MODIFICAR_PLANTILLA")) {
        MultipartRequest.FileInfo [] files = mr.getFileInfoValues("fichero");
          byte [] fichero = files[0].getContent();
          parametros.put("plt_doc",fichero);
        editorMan.modificarPlantilla(params, parametros);
        rd = getServletContext().getRequestDispatcher("/jsp/editor/ocultoEditor.jsp");
      }
      else if(opcion.equals("modificarCRD")) {
        String nombreDocumento = mr.getParameter("nombreDocumento");
        parametros.setString("nombreDocumento", nombreDocumento);
        parametros = obtenerParametrosDeTramitacionExpedientes(parametros,request);
        if (log.isDebugEnabled()) log.debug("el fichero en el servlet es : " + mr.getFileInfoValues("fichero"));
        MultipartRequest.FileInfo [] files = mr.getFileInfoValues("fichero");
        byte [] fichero = files[0].getContent();
        parametros.put("crd_fil",fichero);
        editorMan.modificarPlantillaCRD(params, parametros);
        rd = getServletContext().getRequestDispatcher("/jsp/editor/ocultoEditorCronologia.jsp");
      }
      else if(opcion.equals("OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA")) {
        UsuarioValueObject usuarioVO = null;
        HttpSession session = request.getSession();
        parametros = obtenerParametrosDeTramitacionExpedientes(parametros,request);
        String codAplicacion = "";
        if (session.getAttribute("usuario") != null) {
          usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
          codAplicacion = Integer.toString(usuarioVO.getAppCod());
        }
        parametros.setString("plt_apl", codAplicacion);
        Registro datosPlantilla = editorMan.obtenerDatosPlantilla(params,parametros);

        Date fecha = new Date();
        long selloAux = fecha.getTime();
        String sello = ""+selloAux;
        request.setAttribute("sello",sello);

        DataOutputStream fichero = new DataOutputStream(new FileOutputStream(mTech.getString("PATH.publicHtml")+"/documentos/temp/"+sello+".doc"));
        Registro plantillaSinDatos = editorMan.obtenerPlantilla(params, parametros);
        plantillaSinDatos.setString("host",parametros.getString("host"));
        plantillaSinDatos.setString("nombreDocumento",parametros.getString("nombreDocumento"));
        datosPlantilla.put("plantillaSinDatos",plantillaSinDatos);

        request.setAttribute("datosPlantilla", datosPlantilla);
        request.setAttribute("opcion","OBTENER_DATOS_PLANTILLA_DESDE_CRONOLOGIA");
        byte [] ficheroWord = (byte [])plantillaSinDatos.get("plt_doc");

        fichero.write(ficheroWord,0,ficheroWord.length);
        fichero.close();

        rd = getServletContext().getRequestDispatcher("/jsp/editor/editorWord.jsp");
      }
      else if(opcion.equals("OBTENER_PLANTILLA_DESDE_CRONOLOGIA")) {
        UsuarioValueObject usuarioVO = null;
        HttpSession session = request.getSession();
        parametros = obtenerParametrosDeTramitacionExpedientes(parametros,request);
        String codAplicacion = "";
        if (session.getAttribute("usuario") != null) {
          usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
          codAplicacion = Integer.toString(usuarioVO.getAppCod());
        }
        parametros.setString("plt_apl", codAplicacion);
        Vector etiquetas = editorMan.obtenerDatosEtiquetas(params,parametros);
        request.setAttribute("Etiquetas", etiquetas);

        Registro datosPlantilla = editorMan.obtenerPlantillaCRD(params, parametros);
        datosPlantilla.setString("host",parametros.getString("host"));
        datosPlantilla.setString("nombreDocumento",parametros.getString("nombreDocumento"));
        request.setAttribute("datosPlantilla", datosPlantilla);
        request.setAttribute("opcion","OBTENER_PLANTILLA_DESDE_CRONOLOGIA");

        Date fecha = new Date();
        long selloAux = fecha.getTime();
        String sello = ""+selloAux;
        request.setAttribute("sello",sello);

        DataOutputStream fichero = new DataOutputStream(new FileOutputStream(mTech.getString("PATH.publicHtml")+"/documentos/temp/"+sello+".doc"));
        byte [] ficheroWord = (byte [])datosPlantilla.get("crd_fil");
        fichero.write(ficheroWord,0,ficheroWord.length);
        fichero.close();

        rd = getServletContext().getRequestDispatcher("/jsp/editor/editorWord.jsp");
      }

      /*Redirigimos al JSP de salida*/
            rd.forward(request,response);
      }
        catch(Exception e)
        {
          e.printStackTrace();
          rd = getServletContext().getRequestDispatcher("/PaxinaErroTS.jsp");
            rd.forward(request,response);
        }
  }

  private Registro obtenerParametros(MultipartRequest mr,HttpServletRequest request) throws Exception
  {
    try
    {
      Registro parametros = new Registro();

      parametros.setString("opcion", mr.getParameter("opcion"));
      parametros.setString("plt_des", mr.getParameter("plt_des"));
      parametros.setString("plt_cod", mr.getParameter("plt_cod"));
      parametros.setString("plt_apl", mr.getParameter("plt_apl"));
      parametros.setString("tipVentana", mr.getParameter("tipVentana"));
      parametros.setString("codClasif", mr.getParameter("codClasif"));
      parametros.setString("descClasif", mr.getParameter("descClasif"));

      return parametros = obtenerMasParametros(parametros,request);
    }
    catch (Exception e)
    {
      throw e;
    }
  }

  private Registro obtenerMasParametros(Registro parametros,HttpServletRequest request) throws Exception
  {
    try
    {
      HttpSession session = request.getSession();
      MantAnotacionRegistroForm marf = (MantAnotacionRegistroForm) session.getAttribute("MantAnotacionRegistroForm");
      if (marf != null) {
        RegistroValueObject rvo = marf.getRegistro();
        if (rvo != null) {
          parametros.setString("identDepart", rvo.getIdentDepart() + "");
          parametros.setString("unidadOrgan", rvo.getUnidadOrgan() + "");
          parametros.setString("tipoReg", rvo.getTipoReg());
          parametros.setString("anoReg", rvo.getAnoReg() + "");
          parametros.setString("numReg", rvo.getNumReg() + "");
        }
      }

      String host = obtenerHostLocal(request);
      parametros.setString("host",host);

      return parametros;
    }
    catch (Exception e)
    {
      throw e;
    }
  }

  private Registro obtenerParametrosDeTramitacionExpedientes(Registro parametros,HttpServletRequest request) throws Exception
  {
    try
    {
      if(parametros != null) {
        HttpSession session = request.getSession();
        TramitacionExpedientesForm tEF = (TramitacionExpedientesForm) session.getAttribute("TramitacionExpedientesForm");
        if (tEF != null) {
          TramitacionExpedientesValueObject tEVO = tEF.getTramitacionExpedientes();
          if (tEVO != null) {
            parametros.setString("codProcedimiento", tEVO.getCodProcedimiento() + "");
            parametros.setString("codMunicipio", tEVO.getCodMunicipio() + "");
            parametros.setString("ejercicio", tEVO.getEjercicio() + "");
            parametros.setString("numeroExpediente", tEVO.getNumeroExpediente() + "");
            parametros.setString("codTramite", tEVO.getCodTramite() + "");
            parametros.setString("ocurrenciaTramite", tEVO.getOcurrenciaTramite() + "");
            parametros.setString("codDocumento", tEVO.getCodDocumento() + "");
            parametros.setString("codUsuario",tEVO.getCodUsuario());
            parametros.setString("plt_cod", tEVO.getCodPlantilla() + "");
          }
        }
      } else {
        parametros = new Registro();
        parametros.setString("codProcedimiento", request.getParameter("codProc") + "");
        parametros.setString("codMunicipio", request.getParameter("codMun") + "");
        parametros.setString("ejercicio", request.getParameter("eje") + "");
        parametros.setString("numeroExpediente", request.getParameter("num") + "");
        parametros.setString("codTramite", request.getParameter("codTram") + "");
        parametros.setString("ocurrenciaTramite", request.getParameter("codClasifTram") + "");
        parametros.setString("codDocumento", request.getParameter("nombreCodTram") + "");
        parametros.setString("codUsuario",request.getParameter("nCS"));
        parametros.setString("plt_cod", request.getParameter("codPlantilla") + "");
        parametros.setString("nombreDocumento", request.getParameter("nombDoc") + "");
      }

      String host = obtenerHostLocal(request);
      parametros.setString("host",host);

      return parametros;
    }
    catch (Exception e)
    {
      throw e;
    }
  }


  private Registro obtenerParametros(HttpServletRequest request) throws Exception
  {
    try
    {
      Registro parametros = new Registro();

      parametros.setString("opcion", request.getParameter("opcion"));
      parametros.setString("plt_des", request.getParameter("plt_des"));
      parametros.setString("plt_cod", request.getParameter("plt_cod"));
      parametros.setString("plt_apl", request.getParameter("plt_apl"));
      parametros.setString("tipVentana", request.getParameter("tipVentana"));
      parametros.setString("codClasif", request.getParameter("codClasif"));
      parametros.setString("descClasif", request.getParameter("descClasif"));

      return parametros = obtenerMasParametros(parametros,request);
    }
    catch (Exception e)
    {
      throw e;
    }
  }



  private String[] obtenerParametrosConexion(HttpServletRequest request) throws Exception
  {
    try
    {
      HttpSession session = request.getSession();
      UsuarioValueObject usuarioVO = null;
      String[] params = null;

      if (session.getAttribute("usuario") != null) {
      usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
      params = usuarioVO.getParamsCon();
      }

      return params;
    }
    catch (Exception e)
    {
      throw e;
    }
  }


  private String obtenerHostLocal(HttpServletRequest req) throws Exception
  {
    try
    {
      String host="";
      if(req.getHeader("Host") != null)
      {
        host = req.getHeader("Host") + req.getContextPath();                    
        String protocolo = StrutsUtilOperations.getProtocol(req);                        
        if(host != null)
            host= protocolo + "://" + host + "/";
      }
      return host;
    }
    catch (Exception e)
    {
      throw e;
    }
  }

}

