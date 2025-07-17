package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.business.portafirmas.documentofirma.vo.DocumentoOtroFirmaVO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import java.io.BufferedOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action que se encarga de mostrar el contenido de un documento externo enviado al portafirmas
 * @author oscar.rodriguez
 */
public class MostrarDocumentoExternoPortafirmasAction  extends ActionSession{

      private Logger log = Logger.getLogger(MostrarDocumentoExternoPortafirmasAction.class);

        public ActionForward performSession(	ActionMapping mapping,
                    ActionForm form,
                    HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {

      m_Log.debug("performSession");

      String codDocumento = request.getParameter("codDocumento");

      log.debug(" =============> MostrarDocumentoExternoPortafirmas codDocumento: " + codDocumento);
      BufferedOutputStream bos= null;
      DocumentoOtroFirmaVO doc = null;
      String nombreFichero = "";
      String tipoMime = "";
      byte[] file = null;
      if(!"".equals(codDocumento)){
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario!=null){
                String[] params = usuario.getParamsCon();
                /** óscar **/
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassDocumentoExternoPortafirmas(Integer.toString(usuario.getOrgCod()));
                doc = new DocumentoOtroFirmaVO();
                doc.setParams(params);
                doc.setCodigoDocumento(Integer.parseInt(codDocumento));
                doc.setCodOrganizacion(Integer.toString(usuario.getOrgCod()));
                try{
                    doc = almacen.getDocumentoExternoPortafirmas(doc);
                }catch(AlmacenDocumentoTramitacionException e){
                    e.printStackTrace();
                }
                
                if(doc!=null){
                    log.debug("documento recuperado: " + doc.getNombreDocumento() + "." + doc.getExtension());                    
                    nombreFichero = doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension();
                    file = doc.getContenido();
                    tipoMime = doc.getTipoMime();

                }
            }


          if(file != null){
            if (log.isDebugEnabled())  log.debug("DOCUMENTO OK ::: " + file.length);
            response.setHeader("Content-disposition","attachment;filename="+nombreFichero);
            response.setContentType(tipoMime);
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setContentLength(file.length);
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream out = response.getOutputStream();
            bos = new BufferedOutputStream(out);
            bos.write(file, 0, file.length);
          }
      }
     return null;

  }

}
