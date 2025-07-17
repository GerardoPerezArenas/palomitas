package es.altia.flexia.registro.digitalizacion.servlet;

import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.DigitalizacionDocumentosLanbideManager;
import es.altia.flexia.registro.digitalizacion.lanbide.util.DocumentoCatalogacionConversor;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.DocumentoCatalogacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GrupoTipDocVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.TipoDocumentalCatalogacionVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;

/**
 *
 * @author altia
 */
public class CatalogacionExternaServlet extends HttpServlet {

    private Logger log = Logger.getLogger(CatalogacionExternaServlet.class);
    private static Config m_Config = ConfigServiceHelper.getConfig("common");
    private static Config m_ConfigReg = ConfigServiceHelper.getConfig("Registro");
    private static Config m_AuthConfig = ConfigServiceHelper.getConfig("authentication");

    private static final String JSP_EXITO = "/jsp/digitalizacion/pantallaCatalogacionExterna.jsp";
    private static final String JSP_ERROR = "/jsp/digitalizacion/errorCatalogacionExterna.jsp";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void defaultAction(HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.debug("EJECUTANDO SERVLET: CatalogacionExternaServlet");

        HttpSession session = request.getSession();
        if (session == null) {
            log.error("SESION NULA EN CatalogacionExternaServlet.defaultAction");
            return;
        }

        log.debug("ID de la sesión: " + session.getId());
        
        boolean existeLogin = false;
        boolean haySesionCAS = false;

        RequestDispatcher rd = null;

        try {
            // Se obtiene de la request el ticket devuelto por CAS
            String ticket = request.getParameter("ticket");
            log.info(String.format("El ticket recibido es: %s", ticket));
            // Se obtiene de la session la Assertion devuelta por CAS
            Assertion assertion = (AssertionImpl) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
            if(ticket != null || assertion != null){
                haySesionCAS = true;
            }
            
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            UsuarioEscritorioValueObject usuarioEscritorioVO = new UsuarioEscritorioValueObject();
            String usuario = m_ConfigReg.getString("catalogacionExterna/usuario");
            log.debug("usuario: " + usuario);
            usuarioEscritorioVO.setLogin(usuario);
            existeLogin = UsuarioManager.getInstance().existeLogin(usuarioEscritorioVO.getLogin());
            
            if (existeLogin && haySesionCAS) {
                RegistroValueObject regBusqueda = new RegistroValueObject();
                Vector documentosAux;
                DocumentoValueObject[] documentos = null;
                String[] params = null;
                List<TipoDocumentalCatalogacionVO> listaTipos = null;
                List<GrupoTipDocVO> listaGrupos = null;
                DigitalizacionDocumentosLanbideManager digitalizacionManager = DigitalizacionDocumentosLanbideManager.getInstance();

                String codOrganizacion = request.getParameter("organizacion");
                String ejercicio = request.getParameter("ejercicio");
                String numAnotacion = request.getParameter("numero");
                int codDepartamento = ConstantesDatos.REG_COD_DEP_DEFECTO;
                int unidadRegistro = 0;
                String tipoRegistroOrigen = "SGE";

                //log.debug("organizacion: "+ codOrganizacion);
                log.debug("numero: " + numAnotacion);
                log.debug("ejercicio: " + ejercicio);
                log.debug("uorRegistro " + unidadRegistro);
                log.debug("codDepartamento: " + codDepartamento);
                log.debug("tipoRegOrigen: " + tipoRegistroOrigen);

                if (numAnotacion != null && !numAnotacion.equals("") && ejercicio != null && !ejercicio.equals("")) {
                    String entrada = String.format("%s/%s", ejercicio, numAnotacion);
                    log.debug("---> Abre catalogacion externa de la entrada " + entrada);
                    
                    usuarioEscritorioVO = UsuarioManager.getInstance().buscaUsuario(usuarioEscritorioVO);
                    usuarioVO.setIdUsuario(usuarioEscritorioVO.getIdUsuario());
                    usuarioVO.setNombreUsu(usuarioEscritorioVO.getNombreUsu());
                    usuarioVO.setIdioma(usuarioEscritorioVO.getIdiomaEsc());
                    usuarioVO.setOrgCod(Integer.parseInt(codOrganizacion));
                    usuarioVO.setAppCod(Integer.parseInt(m_ConfigReg.getString("catalogacionExterna/codApl")));
                    usuarioVO.setDepCod(codDepartamento);
                    usuarioVO.setDep("DEPARTAMENTO POR DEFECTO (1)");

                    
                    regBusqueda.setIdentDepart(codDepartamento);
                    regBusqueda.setUnidadOrgan(unidadRegistro);
                    regBusqueda.setTipoReg("E");
                    regBusqueda.setAnoReg(Integer.parseInt(ejercicio));
                    regBusqueda.setNumReg(Long.parseLong(numAnotacion));
                    regBusqueda.setTipoRegOrigen(tipoRegistroOrigen);

                    params = recuperarParamsBD(codOrganizacion);
                    if (params != null) {
                        usuarioVO.setParamsCon(params);
                        session.setAttribute("usuario", usuarioVO);
                        
                        //Recuperamos la unidad de destino
                        regBusqueda = AnotacionRegistroManager.getInstance().getCampoAnotacionByPrimaryKey(regBusqueda, "RES_UOD", params);
                        if (regBusqueda.getCodUORDestBD() != null){
                            
                            documentosAux = AnotacionRegistroManager.getInstance().getListaDocumentos(regBusqueda, params);
                            log.debug("Tamaño de vector de documento: " + documentosAux.size());
                            
                            if(!documentosAux.isEmpty()){
                                        
                                documentos = DocumentoCatalogacionConversor.toDocumentoVOArray(documentosAux);
                                log.debug("Convsersion de vector de documentos a array. Tamaño de array: " + documentos.length);

                                //Los documentos sin contenido no se envian a la interfaz de catalogacion
                                List<DocumentoValueObject> documentosNuevo = new ArrayList<DocumentoValueObject>();
                                // Reseteamos la variable cont para hacer referencia al insert
                                for (DocumentoValueObject documento : documentos) {
                                    if (documento.getExtension() != null && !"".equals(documento.getExtension())) {
                                        log.debug("Existe extension para doc " + documento.getNombre() + ": " + documento.getExtension());
                                        documentosNuevo.add(documento);
                                    }
                                }
                                log.debug("Despues de descartar los docs sin extension, se cargan: " + documentosNuevo.size());

                                request.setAttribute("listaDocumentos", documentosNuevo.toArray(new DocumentoValueObject[documentosNuevo.size()]));
                                request.setAttribute("datosAnotacion", regBusqueda);

                                listaTipos = digitalizacionManager.getTipDocCatalogacionProcedimiento(ejercicio, numAnotacion, params);

                                request.setAttribute("listaTiposDocumentales", listaTipos);

                                //grupos de tipos documentales
                                listaGrupos = digitalizacionManager.getGruposTipDoc(params);
                                request.setAttribute("listaGruposTiposDocumentales", listaGrupos);

                                // primer elemento seleccionado 
                                DocumentoCatalogacionVO docCatalogar = new DocumentoCatalogacionVO();
                                docCatalogar.setDepartamento(regBusqueda.getIdentDepart());
                                docCatalogar.setUnidadOrg(regBusqueda.getUnidadOrgan());
                                docCatalogar.setTipoAnot(regBusqueda.getTipoReg());
                                docCatalogar.setEjercicio(regBusqueda.getAnoReg());
                                docCatalogar.setNumeroAnot(regBusqueda.getNumReg());

                                for (DocumentoValueObject documento : documentos) {
                                    if (documento.getCatalogado().equals("NO")) {
                                        docCatalogar.setNomDocumento(documento.getNombre());
                                        break;
                                    }
                                }

                                if (docCatalogar.getNomDocumento() == null || docCatalogar.getNomDocumento().equals("")) {
                                    docCatalogar.setNomDocumento(documentos[0].getNombre());
                                    List<DocumentoCatalogacionVO> listado = digitalizacionManager.recuperarDatosCatalogacionDoc(docCatalogar, params);
                                    if (listado != null) {
                                        request.setAttribute("datosCatalogacionDoc", listado);
                                    }
                                }
                                log.debug("documentoPreseleccionado al cargar la pantalla de catalogación: " + docCatalogar.getNomDocumento());
                                request.setAttribute("documentoSeleccionado", docCatalogar.getNomDocumento());

                                rd = getServletContext().getRequestDispatcher(JSP_EXITO);
                            } else {
                                
                                log.debug("La entrada "  + entrada + " no tiene documentos");
                                request.setAttribute("errorCatalogacionExterna", "La anotación de entrada "  + entrada + " no tiene documentos que catalogar");
                                rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                            }
                            
                        } else {
                            log.debug("Se ha producido un error al obtener la unidad de destino de la anotación");
                            request.setAttribute("errorCatalogacionExterna", "La anotación de entrada " + entrada + " cuyos documentos se quieren catalogar no es válida");
                            rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                        }

                    } else {
                        log.debug("No hay paramétros de acceso a BD");
                        request.setAttribute("errorCatalogacionExterna", "Se ha producido un error al obtener una conexión a la base de datos");
                        rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                    }
                } else {
                    log.debug("Los parámetros recibidos en la request no son correctos.");
                    request.setAttribute("errorCatalogacionExterna", "Los datos de entrada no son correctos");
                    rd = getServletContext().getRequestDispatcher(JSP_ERROR);
                }
            } else {
                if(!haySesionCAS) {
                   String urlLogin = m_AuthConfig.getString(String.format("Auth/%s/urlLogin", m_AuthConfig.getString("Auth/accessMode")));
                    log.info("No hay una sesión activa en CAS. Se va a redirigir a: " + urlLogin);
                    
                    rd = getServletContext().getRequestDispatcher(String.format("/jsp/redirigeCaduca.jsp?opcion=irALoginCAS&url=%s", urlLogin)); 
                } else {
                    request.setAttribute("errorCatalogacionExterna", "El usuario indicado no es válido");                    
                    rd = getServletContext().getRequestDispatcher(JSP_ERROR);                    
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            rd.forward(request, response);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {

            log.info("Entrando en el servlet CatalogacionExternaServlet.doGet()");
            defaultAction(req, res);
        } catch (Exception e) {
            log.error("ERROR doGet:" + e);
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            log.info("Entrando en el servlet CatalogacionExternaServlet.doPost()");
            defaultAction(req, res);
        } catch (Exception e) {
            log.error("ERROR doGet:" + e);
            e.printStackTrace();
        }
    }

    private String[] recuperarParamsBD(String organizacion) {
        String[] params = new String[7];
        String gestor = m_ConfigReg.getString("catalogacionExterna/BBDD/gestor");
        String jndi = m_ConfigReg.getString("catalogacionExterna/BBDD/jndi/" + organizacion);

        params[0] = gestor;
        params[6] = jndi;

        return params;
    }
}
