package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.persistence.DocumentosRelacionExpedientesManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.commons.StringOperations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MostrarDocumentoRelacion extends HttpServlet {
  public static Log m_log;
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");

  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String uri = request.getRequestURI();
      m_log = LogFactory.getLog(this.getClass().getName());
      BufferedOutputStream bos= null;
      try{
          if(!"".equals(request.getContextPath())){
              uri = uri.substring(uri.indexOf(request.getContextPath()) + request.getContextPath().length(), uri.length());
          }
          StringTokenizer tokenizer = new StringTokenizer(uri,"/");
          String[] tokens = new String[tokenizer.countTokens()];
          int i=0;
            while (tokenizer.hasMoreTokens()) {
                tokens[i++] = tokenizer.nextToken();
            }

          String[] params = {tokens[2],"","","","","",tokens[3]};
          byte[] file = null;

         
          if (tokens.length == 10 && tokens[9].contains("agora")) {
              String macroPlantillas = m_ConfigTechnical.getString("macroPlantillas");
              String uriAgoraDot = request.getProtocol() + "://" + macroPlantillas + ":" + request.getLocalPort() + request.getContextPath() + "/jsp/editor/agora.dot";              
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
          }else{

              String numeroRelacion = tokens[5].replace('+', '/').replace('+', '/');
               Hashtable<String,Object> datos = new Hashtable<String,Object>();
               datos.put("codMunicipio",tokens[4]);
               datos.put("numeroRelacion",numeroRelacion);
               datos.put("codTramite",tokens[6]);
               datos.put("ocurrenciaTramite",tokens[7]);
               datos.put("numeroDocumento",tokens[8]);
               datos.put("perteneceRelacion","true");
               datos.put("params",params);
               String codProcedimientoOrigen = tokens[5].replace('+', '/');
               String[] datosProc = codProcedimientoOrigen.split("/");
               String codProcedimiento = datosProc[1];
               
               String nombreFichero = tokens[9];
               if (nombreFichero.indexOf(".")!=-1){
                   String[] datosFichero = nombreFichero.split("[.]");
                   nombreFichero = datosFichero[0];
               }
               m_log.debug("  ***** Nombre fichero:: " + nombreFichero);
               datos.put("nombreDocumento",StringOperations.unescape(nombreFichero));
               //datos.put("nombreDocumento",nombreDoc);
               datos.put("extension","odt");

               if(nombreFichero.equals("**buscarNombreDocRelacion**") || nombreFichero.equals("documento")){
                   GeneralValueObject gVO = new GeneralValueObject();
                   gVO.setAtributo("codMunicipio", tokens[4]);
                   gVO.setAtributo("codProcedimiento",codProcedimiento);
                   gVO.setAtributo("codTramite",tokens[6]);
                   gVO.setAtributo("ocurrenciaTramite",tokens[7]);
                   gVO.setAtributo("numeroDocumento",tokens[8]);
                   gVO.setAtributo("numeroRelacion",numeroRelacion);
                   String[] dNumRelacion = numeroRelacion.split("/");
                   if(dNumRelacion!=null && dNumRelacion.length>=1)
                        gVO.setAtributo("ejercicio",dNumRelacion[0]);

                    // Hay recuperar de la base de datos el nombre del documento
                    String nombreOriginalDocumento = DocumentosRelacionExpedientesManager.getInstance().getNombreDocumentoRelacionGestor(gVO, params);
                    datos.put("nombreDocumento",nombreOriginalDocumento);
              }

            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(tokens[4]).getImplClassPluginProcedimiento(tokens[4],codProcedimiento);            
            Documento doc = null;
            int tipoDocumento = -1;
            if(!almacen.isPluginGestor())
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
            else{

                String codMunicipio = tokens[4];
                String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(codMunicipio,codProcedimiento, tokens[6], params);
                String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                String descripcionOrganizacion = OrganizacionesManager.getInstance().getDescripcionOrganizacion(codMunicipio, params);
                //datos.put("nombreOrganizacion",descripcionOrganizacion);
                //datos.put("nombreProcedimiento",nombreProcedimiento);
                datos.put("codProcedimiento",codProcedimiento);                
                datos.put("codigoVisibleTramite",codigoVisibleTramite);
                datos.put("tipoMime",ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE);
                datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);

                /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");           
                String carpetaRaiz  = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ); 
                
                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                listaCarpetas.add(ConstantesDatos.PREFIJO_DOCUMENTO_RELACION + numeroRelacion.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
                datos.put("listaCarpetas",listaCarpetas);
                /** FIN **/
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
            }

            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos,tipoDocumento);
            file = almacen.getDocumento(doc);
          }

         
          if(file != null){
                if (m_log.isDebugEnabled()) {
                    m_log.debug("DOCUMENTO OK ::: " + file.length);
                }

                if (m_ConfigTechnical.getString("editorPlantillas").equals("OOFFICE")) {
                    response.setContentType("application/vnd.oasis.opendocument.text");
                } else {
            response.setContentType("application/vnd.ms-word");
                }

            response.setContentLength(file.length);
            ServletOutputStream out = response.getOutputStream();
            bos = new BufferedOutputStream(out);
            bos.write(file, 0, file.length);
          } else {
              m_log.warn("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
              throw new ServletException("NO SE HA PODIDO RECUPERAR EL CONTENIDO DEL DOCUMENTO");
          }
      } catch (IOException ioe) {
          ioe.printStackTrace();
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