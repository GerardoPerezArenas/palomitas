package es.altia.agora.interfaces.user.web.util;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.editor.mantenimiento.persistence.DocumentosAplicacionManager;
import es.altia.common.service.config.*;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.editor.mantenimiento.DocumentosAplicacionForm;
import java.util.Vector;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class GrabarDocServlet extends HttpServlet
{
  protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

  protected static Log log =
          LogFactory.getLog(GrabarDocServlet.class.getName());

  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
  }
  public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception
  {
      log.debug ("Entrando en el servlet de grabación de documentos.");
      HttpSession session=req.getSession();
      
    UsuarioValueObject uVO = (UsuarioValueObject) session.getAttribute("usuario");
    DocumentosAplicacionForm daf = (DocumentosAplicacionForm) session.getAttribute("EditorDocumentosAplicacionForm");
    String opcion="";
    if("OOFFICE".equals(daf.getEditorTexto()))
       opcion=(String)session.getAttribute("opcion");
    else if("WORD".equals(daf.getEditorTexto()))
       opcion=req.getParameter("opcion");
    else
        if(m_ConfigTechnical.getString("editorPlantillas").equals("OOFFICE"))
            opcion=(String)session.getAttribute("opcion");
        else 
            opcion=req.getParameter("opcion");
    log.debug("Opcion en servlet="+opcion);
    
    if (opcion.equals("grabarDocumentoOffice"))
    {
        GeneralValueObject gVO = new GeneralValueObject();
        String[] params = new String[7];
        params[0] = uVO.getParamsCon()[0];
        params[6] = uVO.getParamsCon()[6];

        
        String codMunicipio = String.valueOf(uVO.getOrgCod());
        String codAplicacion = daf.getCodAplicacion();
        String codProcedimiento = daf.getCodProcedimiento();
        String codTramite = "";
        if(uVO.getAppCod()==4) codTramite= daf.getCodTramite();
        else if (uVO.getAppCod()==6) codTramite= daf.getCodTramiteSeleccionado();
        String codDocumento = daf.getCodDocumento();
        String interesado = daf.getInteresado();
        String relacion = daf.getRelacion();
        String docActivo = daf.getDocActivo();
        
        
        
        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("codigoAplicacion",codAplicacion);
        gVO.setAtributo("codProcedimiento",codProcedimiento);
        gVO.setAtributo("codTramite",codTramite);
        gVO.setAtributo("codDocumento",codDocumento);        
        gVO.setAtributo("interesado",interesado);
        gVO.setAtributo("relacion",relacion);
        gVO.setAtributo("docActivo",docActivo);
        gVO.setAtributo("editorTexto",daf.getEditorTexto());

        String contentType = req.getContentType();
        log.debug ("contentType: "+contentType);
        
    
        
        ObjectInputStream os = new ObjectInputStream(req.getInputStream());
        Vector d = (Vector) os.readObject();
        String titulo = new String ((byte[])d.get(0));
        byte[] dataBytes = (byte[]) d.get(1);
        
            gVO.setAtributo("nombreDocumento",titulo);       
            gVO.setAtributo("ficheroWord",dataBytes);
                     
       

        int resultado = DocumentosAplicacionManager.getInstance().grabarDocumento(gVO,params);
    }
    
    
    else if (opcion.equals("grabarDocumento"))
    {
        GeneralValueObject gVO = new GeneralValueObject();
        String[] params = new String[7];
        params[0] = req.getParameter("parametro0");
        params[6] = req.getParameter("parametro6");


        String codMunicipio = req.getParameter("codMunicipio");
        String codAplicacion = req.getParameter("codAplicacion");
        String codProcedimiento = req.getParameter("codProcedimiento");
        String codTramite = "";
        if(uVO.getAppCod()==4) codTramite= req.getParameter("codTramite");
        else if (uVO.getAppCod()==6) codTramite= daf.getCodTramiteSeleccionado();                
        String codDocumento = req.getParameter("codDocumento");
        String nombreDocumento = req.getParameter("nombreDocumento");
        String interesado = req.getParameter("interesado");
        String relacion = req.getParameter("relacion");
        String docActivo = req.getParameter("docActivo");

        gVO.setAtributo("codMunicipio",codMunicipio);
        gVO.setAtributo("codigoAplicacion",codAplicacion);
        gVO.setAtributo("codProcedimiento",codProcedimiento);
        gVO.setAtributo("codTramite",codTramite);
        gVO.setAtributo("codDocumento",codDocumento);
        gVO.setAtributo("nombreDocumento",nombreDocumento);
        gVO.setAtributo("interesado",interesado);
        gVO.setAtributo("relacion",relacion);
        gVO.setAtributo("docActivo",docActivo);
        gVO.setAtributo("editorTexto",daf.getEditorTexto());

        DataInputStream in = null;
        String contentType = req.getContentType();
        log.debug ("contentType: "+contentType);
        
        if(contentType != null && contentType.indexOf("multipart/form-data") != -1)
        {
            //open input stream
            in = new DataInputStream(req.getInputStream());
            int formDataLength = req.getContentLength();
            byte dataBytes[] = new byte[formDataLength];
            int bytesLeidos = 0;
            int totalBytesLeidos = 0;
            while (totalBytesLeidos < formDataLength)
            {
                bytesLeidos = in.read(dataBytes,totalBytesLeidos,formDataLength);
                totalBytesLeidos += bytesLeidos;
            }
            String file = new String(dataBytes);
            // Paso 1
            int lastIndex = contentType.lastIndexOf("=");
            String boundary = contentType.substring(lastIndex+1,contentType.length());
            String saveFile = file.substring(file.indexOf("filename=\"")+10);
            saveFile = saveFile.substring(0,saveFile.indexOf("\n"));
            saveFile = saveFile.substring(saveFile.lastIndexOf("\\")+1,saveFile.indexOf("\""));
            String saveFileName = saveFile;

            // Paso 2
            String restant = "";
            int pos;
            pos = file.indexOf("filename=\"");
            pos = file.indexOf("\n",pos)+1;
            restant = file.substring(pos,file.indexOf("\n",pos)-1);
            restant = restant.substring(restant.indexOf(":")+2,restant.length());
            String mimeType = restant;

            //find position of eind content-type line
            pos = file.indexOf("\n",pos)+1;

            //find position of blank line
            pos = file.indexOf("\n",pos)+1;
            int start = pos;

            // Paso 3
            int boundaryLocation = file.indexOf(boundary,pos)-4;
            byte dataBytes2[] = new byte[boundaryLocation-start];

            for (int i=0;i<(boundaryLocation-start);i++)
            {
                dataBytes2[i]=dataBytes[start+i];
            }

            gVO.setAtributo("ficheroWord",dataBytes2);
        }

        int resultado = DocumentosAplicacionManager.getInstance().grabarDocumento(gVO,params);
    } // fin de la opcion grabarDocumento
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
      try     {
      defaultAction(req, res);
    } catch (Exception e) {
          if (log.isErrorEnabled()) log.error("ERROR doGet:" + e);
          e.printStackTrace();
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    try {
        defaultAction(req, res);
    } catch (Exception e) {
            if (log.isErrorEnabled()) log.error("ERROR doPost:" + e);
            e.printStackTrace();
    }
  }
}