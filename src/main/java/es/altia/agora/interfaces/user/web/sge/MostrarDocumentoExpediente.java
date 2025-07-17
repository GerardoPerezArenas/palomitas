package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.DocumentosExpedienteManager;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MostrarDocumentoExpediente extends HttpServlet {
  public static Log m_log;
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");


  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String uri = request.getRequestURI();      
      m_log = LogFactory.getLog(this.getClass().getName());
      m_log.debug ("doGet mostrar documento="+uri);
      BufferedOutputStream bos= null;
      Documento doc = null;
      byte[] file;
      try{
          if(!"".equals(request.getContextPath())){
              uri = uri.substring(uri.indexOf(request.getContextPath()) + request.getContextPath().length(), uri.length());
          }
          m_log.debug (uri);
          StringTokenizer tokenizer = new StringTokenizer(uri,"/");
          String[] tokens = new String[tokenizer.countTokens()];
          int i=0;
            while (tokenizer.hasMoreTokens()) {
                tokens[i++] = tokenizer.nextToken();
            }

          if (tokens.length == 10 && tokens[9].contains("agora")) {
              String macroPlantillas = m_ConfigTechnical.getString("macroPlantillas");
              String uriAgoraDot = request.getProtocol() + "://" + macroPlantillas + ":" + request.getLocalPort() + request.getContextPath() + "jsp/editor/agora.dot";
              File fileAgoraDot = new File(uriAgoraDot);
              FileReader fr = new FileReader(fileAgoraDot);

              java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
              int c;
              while ((c = fr.read())!= -1){
                  ot.write(c);
              }
              ot.flush();
              file = ot.toByteArray();
              ot.close();
              fr.close();
          } else {

                String[] params = {tokens[2],"","","","","",tokens[3]};
                String codMunicipio = tokens[4];

                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",tokens[4]);
                datos.put("numeroExpediente",tokens[5].replace('+', '/'));
                datos.put("codTramite",tokens[6]);
                datos.put("ocurrenciaTramite",tokens[7]);
                datos.put("numeroDocumento",tokens[8]);
                datos.put("perteneceRelacion","false");
                datos.put("params",params);
                datos.put("expedienteHistorico","false");
                 //añadimos atributo a hashtable para indicar procedencia
                datos.put("desdeNotificacion","no");
                
                String codProcedimientoOrigen = tokens[5].replace('+', '/');
                String[] datosProc = codProcedimientoOrigen.split("/");
                String codProcedimiento = datosProc[1];
                String ejercicio = datosProc[0];
                
                boolean expedienteHistorico = false;
                try{
                    if(tokens[10]!=null && tokens[10].equalsIgnoreCase("historico"))
                        expedienteHistorico = true;
                    
                }catch(Exception e){
                    expedienteHistorico = false;
                }
                
                if(expedienteHistorico) datos.put("expedienteHistorico","true");
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(tokens[4]).getImplClassPluginProcedimiento(tokens[4],codProcedimiento);
               
                int tipoDocumento = -1;
                
                if(!almacen.isPluginGestor())
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                else{
                   
                    String editorPlantillas = m_ConfigTechnical.getString("editorPlantillas");
                    if(editorPlantillas!=null && "OOFFICE".equalsIgnoreCase(editorPlantillas)){                       
                        datos.put("extension",ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE);                        
                    }else{                     
                        datos.put("extension",ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD);
                    }
                    
                    String tipoMime = "";
                    // Se obtiene el nombre del documento a mostrar porque se necesita para el caso de que se venga de firmar el documento
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio",tokens[4]);
                    gVO.setAtributo("codProcedimiento",codProcedimiento);
                    gVO.setAtributo("ejercicio",ejercicio);
                    gVO.setAtributo("codTramite",tokens[6]);
                    gVO.setAtributo("ocurrenciaTramite",tokens[7]);
                    gVO.setAtributo("numeroExpediente",tokens[5].replace('+', '/'));
                    gVO.setAtributo("numeroDocumento",tokens[8]);                    
                    gVO.setAtributo("expHistorico","false");
                    
                    if(expedienteHistorico) gVO.setAtributo("expHistorico","true");
                                        
                    datos.put("nombreDocumento",DocumentosExpedienteManager.getInstance().getNombreDocumentoGestor(gVO, params));
                    String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(tokens[4],codProcedimiento, tokens[6], params);
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                    String descripcionOrganizacion = OrganizacionesManager.getInstance().getDescripcionOrganizacion(codMunicipio, params);
                    datos.put("codProcedimiento",codProcedimiento);                    
                    datos.put("codigoVisibleTramite",codigoVisibleTramite);

                    if (m_ConfigTechnical.getString("editorPlantillas").equals("OOFFICE"))
                          tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
                    else
                        tipoMime = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;

                    datos.put("tipoMime",tipoMime);
                    datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);


                    /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                    ResourceBundle bundle = ResourceBundle.getBundle("documentos");            
                    String carpetaRaiz  = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ); 
                    
                    
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                    listaCarpetas.add(((String)gVO.getAtributo("numeroExpediente")).replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));

                    datos.put("listaCarpetas",listaCarpetas);                    
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                }

                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos,tipoDocumento);
                file = almacen.getDocumento(doc);

          }

          String nombreFichero = null;
          Calendar c = Calendar.getInstance();
          SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyyHHmmss");

          /* ORIGINAL
        if (m_ConfigTechnical.getString("editorPlantillas").equals("OOFFICE")) {
              nombreFichero = "prueba" + sf.format(c.getTime()) + ".odt";
        } else {
              nombreFichero = "prueba" + sf.format(c.getTime()) + ".doc";
        }
        */
        if(doc!=null && doc.getExtension()!=null) {
            nombreFichero = "prueba" + sf.format(c.getTime()) + "." + doc.getExtension();
        } else if (m_ConfigTechnical.getString("editorPlantillas").equals("OOFFICE")) {
            nombreFichero = "prueba" + sf.format(c.getTime()) + ".odt";
        } else { 
            nombreFichero = "prueba" + sf.format(c.getTime()) + ".doc";
        }

          if(file != null){
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "inline; filename=" + nombreFichero);
            m_log.debug("DOCUMENTO OK ::: " + file.length);
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setContentLength(file.length);
            ServletOutputStream out = response.getOutputStream();
            bos = new BufferedOutputStream(out);
            bos.write(file, 0, file.length);


          } else {
              m_log.warn("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
              throw new ServletException("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
          }
      } catch (IOException ioe) {
          //ioe.printStackTrace();
          throw ioe;
      } catch (ServletException se) {
          se.printStackTrace();
          throw se;
      } catch (Exception e) {
          //e.printStackTrace();
           m_log.error("Error al mostrarDocumentoPlantillaGen:  "+e.getMessage());
          throw new ServletException("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO", e);
      } finally{
            if (bos != null) {
                bos.close();
            }
    }
  }

  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request,response);
  }
}