package es.altia.flexia.registro.digitalizacion.servlet;



import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.HistoricoMovimientoValueObject;
import es.altia.agora.business.registro.persistence.HistoricoMovimientoManager;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.util.HistoricoAnotacionHelper;
import es.altia.agora.interfaces.user.web.administracion.SessionInfo;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.registro.digitalizacion.exception.DigitalizarDocumentosException;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.DigitalizacionDocumentosLanbideManager;
import es.altia.flexia.registro.digitalizacion.servlet.util.Configuracion;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.AdaptadorSQLBD;
import javax.servlet.GenericServlet;

public class RecuperarDocumentosDigitalizadosServlet extends HttpServlet {
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("common");
    protected static Log log = LogFactory.getLog(RecuperarDocumentosDigitalizadosServlet.class.getName());
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    
    public void defaultAction(HttpServletRequest req,HttpServletResponse res) throws Exception {

        log.info("EJECUTANDO SERVLET: RecuperarDocumentosDigitalizadosServlet");
        log.info("Cookies : " + req.getHeader("cookie"));
        
        /*
            Datos que necesito para la inserción en R_RED:
            RED_DEP - 1
            RED_UOR - 0
            RED_EJE - Lo obtengo de la request, del parámetro numRegistro
            RED_NUM - Lo obtengo de la request, del parámetro numRegistro
            RED_TIP - E
            RED_NOM_DOC - Lo obtengo de la request, del parámetro documentoNombre
            RED_TIP_DOC - application/pdf
            RED_DOC - Nulo, documento en Dokusi
            RED_FEC_DOC - Fecha actual
            RED_ENT - NO
            RED_TAMANHO - 0, documento en Dokusi
            RED_IDDOC_GESTOR  - Oid del documento. Lo obtengo de la request, del parámetro documentoOid
        */
        UsuarioValueObject usuarioVO = null;
        String separador = ";;";
        String organizacion = "0"; 
        String[] params = null;
        int codDep = 1;
        int codUor = 0;
        String tipoAnotacion = "E";   //TODO
        String tipoDoc ="application/pdf";
        Date fechaDoc = new Date();
        DateFormat formato = new SimpleDateFormat(DateOperations.LATIN_DATETIME_FORMAT);		
        String docEntregadoAlta = "SI";
        int tamanoDoc = 0;
        int ejercicio;
        int numAnotacion;
        ArrayList<String> oids = new ArrayList<String>();
        ArrayList<String> titulos = new ArrayList<String>();
        ArrayList<String> nombres = new ArrayList<String>();
        DocumentoGestor documento = null;
		int idUsuario=0;
				
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                log.error("SESION NULA EN RecuperarDocumentosDigitalizadosServlet.defaultAction");
                return;
            }
            String usuarioActualLog = SessionInfo.getSessionUserLogin(session);
            usuarioActualLog = (usuarioActualLog != null && usuarioActualLog != "") ? usuarioActualLog.replace("Usuario logado: ", "") : "";
            // Hay sesion, cogemos la organizacion del usuario de la session
            if(session.getAttribute("usuario") != null){
                usuarioVO = (UsuarioValueObject)session.getAttribute("usuario");
                organizacion = String.valueOf(usuarioVO.getOrgCod());
				idUsuario= usuarioVO.getIdUsuario();
            } else log.error("No hay usuario en el servlet"); 
            log.debug("La organizacion sobre la que obtendremos la conexión es: "+ organizacion);
            params = Configuracion.getParamsBD(organizacion);
            if (params == null) {
                log.error("PARAMETROS DE CONEXIÓN INCORRECTOS en RecuperarDocumentosDigitalizadosServlet.defaultAction");
                return;
            }
            
            String numRegistro = req.getParameter("numRegistro");
            String documentoTitulo = req.getParameter("documentoTitulo");
            String documentoNombre = req.getParameter("documentoNombre");
            String cadenaOids = req.getParameter("documentoOid");
			
            log.info(session.getId()+" "+usuarioActualLog  + " numRegistro: "+numRegistro);
            log.info(session.getId()+" "+usuarioActualLog  + " documentoTitulo: "+documentoTitulo);
            log.info(session.getId()+" "+usuarioActualLog  + " documentoNombre: "+documentoNombre);
            log.info(session.getId()+" "+usuarioActualLog  + " cadenaOids: "+cadenaOids);
            String[] partesNum = numRegistro.split("/");
            if(cadenaOids!=null && !cadenaOids.equals("") && documentoNombre!=null && !documentoNombre.equals("")){
                ejercicio = Integer.parseInt(partesNum[0]);
                numAnotacion = Integer.parseInt(partesNum[1]);
                
                if(cadenaOids.contains(separador)){
                    String[] oidsAux = cadenaOids.split(separador);
                    for(String oid : oidsAux) 
                        oids.add(oid);
                } else oids.add(cadenaOids);
                log.info(session.getId()+" "+usuarioActualLog  + " Lista OID preparada desde string con separador tiene " + oids.size() +" elementos.");
                for(int i=0; i<oids.size(); i++){
                    String secuencia = "_"+oids.get(i);
                    nombres.add(documentoNombre+secuencia);
                    titulos.add(documentoTitulo+secuencia);
                }
                log.info(session.getId()+" "+usuarioActualLog  + " Lista nombresDocumentos y titulosDocumentos preparada desde lista de OIDs tienen " + nombres.size() +" y " +titulos.size() + " elementos.");
                ArrayList<DocumentoGestor> listaDocumentos= new ArrayList();

                for(int index = 0; index < oids.size(); index++){
                    String oidActual = oids.get(index);
                    String nombreActual = nombres.get(index);
                    log.info(session.getId()+" "+usuarioActualLog  + " Tratamos el documento: "+nombreActual + " " + oidActual );
                    documento = new DocumentoGestor();
					
                    documento.setCodigoDepartamento(codDep);
                    documento.setCodigoUnidadOrganica(codUor);
                    documento.setEjercicioAnotacion(ejercicio);
                    documento.setNumeroRegistro(numAnotacion);
                    documento.setTipoRegistro(tipoAnotacion);
                    documento.setNombreDocumento(nombreActual);
                    documento.setNumeroDocumento(oidActual);
                    documento.setTipoDocumento(tipoDoc);                        
                    documento.setFechaDocumento(formato.format(fechaDoc));
                    documento.setEntregado(docEntregadoAlta);
                    documento.setLongitudDocumento(tamanoDoc);
                    documento.setIdDocGestor(oidActual);
                    log.info(session.getId()+" "+usuarioActualLog  + " Parametros que se pasan a DigitalizacionDocumentosLanbideManager.getInstance().insertarDocumentoRegistro en el objeto DocumentoGestor : "
                            + " codDep: "+codDep
                            + " codUor: "+codUor
                            + " ejercicio: "+ejercicio
                            + " numAnotacion: "+numAnotacion
                            + " tipoAnotacion: "+tipoAnotacion
                            + " tipoDoc: "+tipoDoc
                            + " fechaDoc: "+fechaDoc
                            + " docEntregadoAlta: "+docEntregadoAlta
                            + " tamanoDoc: "+tamanoDoc
                            + " documento.setNumeroDocumento: "+oidActual
                    );
					listaDocumentos.add(documento);
				}
				
                if(DigitalizacionDocumentosLanbideManager.getInstance().insertarTodosLosDocumentoRegistro(listaDocumentos, params))
					DigitalizacionDocumentosLanbideManager.getInstance().insertarHistoricoDocumentoRegistro(listaDocumentos, params,idUsuario);
						
				
            } else {
                log.error(session.getId()+" "+usuarioActualLog  + " Se va a lanzar una exception : No se han recibido documentos.");
                throw new Exception("No se han recibido documentos.");
            }
        } catch (NumberFormatException nfe){
            log.error("Error: los parámetros recibidos no tienen el formato correcto",nfe);
            throw new Exception("Error: " + nfe.getMessage());
        } catch (TechnicalException te){
            log.error("Error: " + te.getMessage(),te);
            throw new Exception("Error: " + te.getMessage());
        } catch (Exception e){
            log.error("Error: " + e.getMessage(),e);
            throw new Exception("Error: " + e.getMessage());
        }
    }
  
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
      try     {
          log.info("RecuperarDocumentosDigitalizadosServlet::doGet()");
          defaultAction(req, res);
    } catch (Exception e) {
        log.error("ERROR doGet:" + e.getMessage(),e);
    }
  }

    @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
    try {
        log.info("RecuperarDocumentosDigitalizadosServlet::doPost()");
        defaultAction(req, res);
    } catch (Exception e) {
        log.error("ERROR doPost:" + e.getMessage(),e);
    }
  }
}
