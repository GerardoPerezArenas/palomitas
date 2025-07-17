/*     */ package es.altia.flexia.integracion.lanbide.impresion;
/*     */
/*     */ import es.altia.agora.business.administracion.mantenimiento.persistence.IdiomasManager;
/*     */ import es.altia.agora.business.escritorio.UsuarioValueObject;
/*     */ import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
/*     */ import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
/*     */ import es.altia.agora.business.sge.SiguienteTramiteTO;
/*     */ import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
/*     */ import es.altia.agora.business.sge.TramitacionValueObject;
/*     */ import es.altia.agora.business.sge.exception.TramitacionException;
/*     */ import es.altia.agora.business.util.GeneralValueObject;
/*     */ import es.altia.agora.interfaces.user.web.sge.TramitacionForm;
/*     */ import es.altia.agora.interfaces.user.web.util.ActionSession;
/*     */ import es.altia.agora.webservice.tramitacion.servicios.WSException;
/*     */ import es.altia.common.exception.TechnicalException;
/*     */ import es.altia.common.service.config.Config;
/*     */ import es.altia.common.service.config.ConfigServiceHelper;
/*     */ import es.altia.util.conexion.AdaptadorSQLBD;
/*     */ import es.altia.util.conexion.BDException;
/*     */ import es.altia.util.sqlxmlpdf.GeneralPDF;
import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.rmi.RemoteException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.struts.action.ActionForm;
/*     */ import org.apache.struts.action.ActionForward;
/*     */ import org.apache.struts.action.ActionMapping;
/*     */ import org.apache.struts.action.ActionServlet;
/*     */ import org.apache.struts.util.MessageResources;
/*     */
/*     */ public final class ImpresionExpedientesLanbideAction extends ActionSession /*     */ {

    /*  44 */ private static ImpresionExpedientesLanbideAction instance = null;

    public static ImpresionExpedientesLanbideAction getInstance() {
        if (instance == null) {
            instance = new ImpresionExpedientesLanbideAction();
        }
        return instance;
    }
    /*     */ public ActionForward performSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            /*     */ throws IOException, ServletException /*     */ {
        /*  60 */ m_Log.debug("================= ImpresionExpedientesLanbideAction ======================>");
        /*     */
        /*  64 */ HttpSession session = request.getSession();
        /*  65 */ String opcion = "";
        /*     */
        /*  67 */ opcion = request.getParameter("opcion");
        /*  68 */ m_Log.info("Opcion : " + opcion);
        /*     */
        /*  70 */ TramitacionValueObject tramVO = new TramitacionValueObject();
        /*  71 */ TramitacionForm tramForm = null;
        /*     */
        /*  73 */ ConsultaExpedientesValueObject consExpVO = new ConsultaExpedientesValueObject();
        /*     */
        /*  76 */ if (form == null) {
            /*  77 */ form = new TramitacionForm();
            /*  78 */ if ("request".equals(mapping.getScope())) /*  79 */ {
                request.setAttribute(mapping.getAttribute(), form);
            } /*     */ else {
                /*  81 */ session.setAttribute(mapping.getAttribute(), form);
                /*     */            }
            /*     */        }
        /*     */
        /*  85 */ tramForm = (TramitacionForm) form;
        /*     */
        /*  87 */ if (session.getAttribute("usuario") != null) {
            /*  88 */ UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            /*  89 */ String[] params = usuario.getParamsCon();
            /*     */
            /*  91 */ Config m_Conf = ConfigServiceHelper.getConfig("common");
            /*     */
            /*  94 */ if (opcion.equals("impresionCEPAP")) /*     */ {
                /*  96 */ Vector consulta = new Vector();
                /*  97 */ Vector consultaE = new Vector();
                /*  98 */ int codOrg = usuario.getOrgCod();
                /*     */ try {
                    /* 100 */ ResourceBundle configImpresionLanbide = ResourceBundle.getBundle("impresionLanbide");
                    /* 101 */ String codProcedImpresion = usuario.getOrgCod() + "/PROCEDIMIENTO_IMPRESION";
                    /* 102 */ String codTramiteImpresion = usuario.getOrgCod() + "/CODIGO_TRAMITE_PENDIENTE";
                    /* 103 */ String codProcedimiento = configImpresionLanbide.getString(codProcedImpresion);
                    /* 104 */ String codTramite = configImpresionLanbide.getString(codTramiteImpresion);
                    /* 105 */ int codOrganizacion = usuario.getOrgCod();
                    /*     */
                    /* 107 */ consultaE = ExpedienteImpresionManager.getInstance().getExpedientesImpresion(codOrganizacion, codProcedimiento, Integer.parseInt(codTramite), params);
                    /* 108 */ consulta = ExpedienteImpresionManager.getInstance().getFicherosImpresionGenerados(params);
                    /*     */                } /*     */ catch (Exception te) {
                    /* 111 */ te.getMessage();
                    /* 112 */ consulta = new Vector();
                    /* 113 */ consultaE = new Vector();
                    /*     */                }
                /* 115 */ session.setAttribute("RelacionExpedientesCEPAP", consultaE);
                /* 116 */ session.setAttribute("RelacionFicherosImpresionGenerados", consulta);
                /*     */            } /* 121 */ else if (opcion.equals("exportarCEPAPCSV")) /*     */ {
                /* 123 */ int codOrg = usuario.getOrgCod();
                /*     */
                /* 125 */ ResourceBundle configImpresionLanbide = ResourceBundle.getBundle("impresionLanbide");
                /* 126 */ String codProcedImpresion = usuario.getOrgCod() + "/PROCEDIMIENTO_IMPRESION";
                /* 127 */ String codTramiteImpresion = usuario.getOrgCod() + "/CODIGO_TRAMITE_PENDIENTE";
                /* 128 */ String codImpresionCEPAP = usuario.getOrgCod() + "/VALOR_IMPRESION_CEPAP";
                /* 129 */ String codProcedimiento = configImpresionLanbide.getString(codProcedImpresion);
                /* 130 */ String codTramite = configImpresionLanbide.getString(codTramiteImpresion);
                /* 131 */ String impresionCEPAP = configImpresionLanbide.getString(codImpresionCEPAP);
                /*     */
                /* 133 */ String idioma = IdiomasManager.getInstance().getClaveIdioma(params, usuario.getIdioma());
                /* 134 */ String listaExpedientesSeleccionados = request.getParameter("listaExpedientesSeleccionados");
                /* 135 */ String codExpediente = "";

                /*     */
                /* 137 */ m_Log.info("ListaExpedientesSeleccionados: " + listaExpedientesSeleccionados);
                /* 138 */ String[] lListaExpedientesSeleccionados = listaExpedientesSeleccionados.split("-");
                /* 139 */ Vector relacionExpedientes = new Vector();
                /* 140 */         //for (int i = 0; i < lListaExpedientesSeleccionados.length; i++) {
/* 141 */           //codExpediente = codExpediente + "'" + lListaExpedientesSeleccionados[i] + "',";
/*     */         //}
/* 143 */         //String newCodExpediente = codExpediente.substring(0, codExpediente.length() - 1);
                //m_Log.debug("EXPEDIENTES:::::" +newCodExpediente);

                //if (resInsertTable == 1)
/*     */           //{
/* 206 */
                /* 209 */
                /*     */           //}
                try {
                    //Comprobar duplicidad
                    m_Log.info("COMPROBANDO DUPLICIDAD: BEGIN------>");
                    //String[] listaExpedientesProcesar = null;
                    Vector listaAProcesar = new Vector();
                    String certificadoDu = "";
                    String duplicado = "";
                    String codExpeSinDuplicados = "";
                    
                    //Ordenar la lista de expedientes para que a la hora de anular duplicados lo haga para los mas modernos dejando el mas antiguo en la lista de procesados
                    for(int i=0;i<(lListaExpedientesSeleccionados.length-1);i++){
					for(int j=i+1;j<lListaExpedientesSeleccionados.length;j++){
						if(lListaExpedientesSeleccionados[i].compareToIgnoreCase(lListaExpedientesSeleccionados[j])>0){
							//Intercambiamos valores
							String expAuxiliar=lListaExpedientesSeleccionados[i];
							lListaExpedientesSeleccionados[i]=lListaExpedientesSeleccionados[j];
							lListaExpedientesSeleccionados[j]=expAuxiliar;

						}
					}
				}

                    for (int i = 0; i < lListaExpedientesSeleccionados.length; i++) {
                        //Obtener CERTIFICADO para expediente i
                        certificadoDu = ExpedienteImpresionManager.getInstance().getCertificadoDuplicidad("'" + lListaExpedientesSeleccionados[i] + "'", params);
                        m_Log.info("Certificado: " + certificadoDu + " del Expediente: " + "'" + lListaExpedientesSeleccionados[i] + "'");
                        if (certificadoDu != null && !"".equals(certificadoDu)) {
                            //Comprobar duplicidad 
                            duplicado = ExpedienteImpresionManager.getInstance().compruebaExpedientes("'" + lListaExpedientesSeleccionados[i] + "'", certificadoDu, params);
                            if (duplicado.equals("0")) {
                                m_Log.info("El expediente '" + lListaExpedientesSeleccionados[i] + "' NO está duplicado");
                                codExpeSinDuplicados = codExpeSinDuplicados + "'" + lListaExpedientesSeleccionados[i] + "',";
                                listaAProcesar.add(lListaExpedientesSeleccionados[i]);
                                //listaExpedientesProcesar[i] = lListaExpedientesSeleccionados[i];
                            } else {
                                /*1. OBTENER LISTA DE SUPLICADOS-- ListaDuplicados
                                 2. rECORRER LISTA DUPLICADOS -- MIRAR SI exp dupli en lista de procesados
                               
                                 */

                                List<String> listaDup = ExpedienteImpresionManager.getInstance().getExpMismoCertificadoPorExp("'" + lListaExpedientesSeleccionados[i] + "'", certificadoDu, params);
                                boolean enListaProc = false;

                                for (String expDupli : listaDup) {
                                    if (listaAProcesar.size() > 0 && listaAProcesar.contains(expDupli)) {

                                        //existe duplicado en lista de procesados --> el exp tratado hay q anularlo
                                        m_Log.info("Eliminamos " + lListaExpedientesSeleccionados[i] + " de la lista xq ya existe " + expDupli + ".");
                                        m_Log.info("El expediente '" + lListaExpedientesSeleccionados[i] + "' SI está duplicado por lo que se van a cerrar sus tramites abiertos, cambiar su estado a Anulado y añadir en observacones el motivo: Anulado por duplicidad");
                                        try {
                                            //Cerrar los trámites abiertos del expediente i:
                                            boolean modOK = ExpedienteImpresionManager.getInstance().cerrarTramitesExpediente("'" + lListaExpedientesSeleccionados[i] + "'", params);
                                            if (modOK) {
                                                m_Log.info("Trámites del expediente '" + lListaExpedientesSeleccionados[i] + "' cerrados");
                                            }

                                            //En observaciones del expediente i añadirá el texto Anulado por duplicidad
                                            boolean observOK = ExpedienteImpresionManager.getInstance().cambioObservExpediente("'" + lListaExpedientesSeleccionados[i] + "'", params);
                                            if (observOK) {
                                                m_Log.info("Cambio observaciones del expediente '" + lListaExpedientesSeleccionados[i] + "' por Anulado por duplicidad");
                                            }

                                            //Pondrá el expediente i en estado ?Anulado?
                                            boolean estadoOK = ExpedienteImpresionManager.getInstance().cambioEstadoExpediente("'" + lListaExpedientesSeleccionados[i] + "'", params);
                                            if (estadoOK) {
                                                m_Log.info("Cambio de estado del expediente '" + lListaExpedientesSeleccionados[i] + "' correcto");
                                            }

                                        } catch (Exception ex) {
                                            m_Log.info("Error cerrando los tramites del expediente : " + ex.getMessage());
                                        }
                                        enListaProc = true;
                                        continue; //ya he anulado expediente a tratar--> no tengo q seguir comprobando duplicados relacionados
                                        /*enListaProc=true;
                                         continue;//salir del bucle*/
                                    }
                                }
                                if (!enListaProc && !listaAProcesar.contains(lListaExpedientesSeleccionados[i])) {
                                    //metemos en lista a procesar
                                    listaAProcesar.add(lListaExpedientesSeleccionados[i]);
                                    m_Log.info("Incluyo " + lListaExpedientesSeleccionados[i] + " en lista aprocesar.");
                                    codExpeSinDuplicados = codExpeSinDuplicados + "'" + lListaExpedientesSeleccionados[i] + "',";
                                }

                                /*m_Log.info("El expediente '"+lListaExpedientesSeleccionados[i]+"' SI está duplicado por lo que se van a cerrar sus tramites abiertos, cambiar su estado a Anulado y añadir en observacones el motivo: Anulado por duplicidad");
                                 try {
                                 //Cerrar los trámites abiertos del expediente i:
                                 boolean modOK = ExpedienteImpresionManager.getInstance().cerrarTramitesExpediente("'"+lListaExpedientesSeleccionados[i]+"'",params);
                                 if(modOK)m_Log.info("Trámites del expediente '"+lListaExpedientesSeleccionados[i]+"' cerrados");
                                    
                                 //En observaciones del expediente i añadirá el texto Anulado por duplicidad
                                 boolean observOK = ExpedienteImpresionManager.getInstance().cambioObservExpediente("'"+lListaExpedientesSeleccionados[i]+"'",params);
                                 if(observOK)m_Log.info("Cambio observaciones del expediente '"+lListaExpedientesSeleccionados[i]+"' por Anulado por duplicidad");
                                        
                                 //Pondrá el expediente i en estado ?Anulado?
                                 boolean estadoOK = ExpedienteImpresionManager.getInstance().cambioEstadoExpediente("'"+lListaExpedientesSeleccionados[i]+"'",params);
                                 if(estadoOK)m_Log.info("Cambio de estado del expediente '"+lListaExpedientesSeleccionados[i]+"' correcto");
                                    
                                 } catch (Exception ex) {
                                 m_Log.info("Error cerrando los tramites del expediente : " + ex.getMessage());
                                 }*/
                            }
                        }
                    }
                    try {
                        if (!"".equals(codExpeSinDuplicados)) {

                            String newCodExpedienteLimpio = codExpeSinDuplicados.substring(0, codExpeSinDuplicados.length() - 1);
                            m_Log.info("Cod Expedientes limpios (no duplicados) a procesar: " + newCodExpedienteLimpio);
                            m_Log.info("Lista Expedientes limpios (no duplicados) a procesar:\n");
                            for (int i = 0; i < listaAProcesar.size(); i++) {
                                m_Log.info("" + listaAProcesar.get(i) + "\n");
                            }

                            EjecutaImpresion ejecuta = new EjecutaImpresion();
                            String tSexo = usuario.getOrgCod() + "/CODIGO_CAMPO_TSEXOTERCERO";
                            String codtSexo = configImpresionLanbide.getString(tSexo);
                            String tFechNacimiento = usuario.getOrgCod() + "/CODIGO_CAMPO_TFECHANACIMIENTO";
                            String codtFechNacimiento = configImpresionLanbide.getString(tFechNacimiento);
                            String sUrl = request.getSession().getServletContext().getRealPath("/");
                            String path = getServlet().getServletContext().getRealPath("");
                            //
                            String[] listaExpedientesProcesar = new String[listaAProcesar.size()];
                            for (int i = 0; i < listaAProcesar.size(); i++) {
                                listaExpedientesProcesar[i] = (String) listaAProcesar.get(i);
                            }
                            ejecuta.start(listaExpedientesProcesar, codOrg, codProcedimiento, codTramite, params, usuario, newCodExpedienteLimpio, sUrl,
                                    m_Conf, idioma, tSexo, codtSexo, tFechNacimiento, codtFechNacimiento, impresionCEPAP, path);

                            request.setAttribute("resInsertTable", 1);


                            /*m_Log.debug(" ****************** Antes de llamar a finalizarAvanzarTramite **********>");
                             int finTramite = finalizarAvanzarTramite(codOrg, codProcedimiento, codTramite, params, usuario, lListaExpedientesSeleccionados);
                             m_Log.debug(" ****************** DespuÃ©s de llamar a finalizarAvanzarTramite finTramite: " + finTramite);
                             relacionExpedientes = ExpedienteImpresionManager.getInstance().consultar(newCodExpediente, codtSexo, codtFechNacimiento, usuario, params);
                             GeneralValueObject gVO = new GeneralValueObject();
                             gVO.setAtributo("baseDir", m_Conf.getString("PDF.base_dir"));
                             gVO.setAtributo("aplPathReal", getServlet().getServletContext().getRealPath(""));
                             gVO.setAtributo("usuDir", usuario.getDtr());
                             gVO.setAtributo("pdfFile", "SGE");


                             int index = sUrl.indexOf("\\");
                             if (index > 0) {
                             sUrl = sUrl.substring(index);
                             }
                             gVO.setAtributo("estilo", "css/listadoExpedientes.css");

                             GeneralPDF pdf = null;
                             Vector ficheros = new Vector();
                             String plantilla = "";
                             plantilla = "listadoExpedientesCEPAP";
                             plantilla = plantilla + "_" + idioma;
                             String textoXML = crearXML(relacionExpedientes, null);
                             pdf = new GeneralPDF(params, gVO);
                             ficheros.add(pdf.transformaXML(textoXML, plantilla));

                             Calendar c = new GregorianCalendar();
                             String dia = Integer.toString(c.get(5));
                             String mes = Integer.toString(c.get(2));
                             String annio = Integer.toString(c.get(1));
                             String hora = Integer.toString(c.get(10));
                             if (hora.length() < 2) hora = "0" + hora;
                             String minuto = Integer.toString(c.get(12));
                             if (minuto.length() < 2) minuto = "0" + minuto;
                             String segundo = Integer.toString(c.get(13));
                             if (segundo.length() < 2) segundo = "0" + segundo;
                             Calendar FECHA_ACTUAL = Calendar.getInstance();
                             SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyy_HHmmss");

                             String nombreFichero = impresionCEPAP + sf.format(FECHA_ACTUAL.getTime());
                             request.setAttribute("nombre", pdf.getCSVCEPAP(ficheros, nombreFichero));
                             request.setAttribute("dir", usuario.getDtr());

                             int resInsertTable = guardarDatosExcelTabla(nombreFichero, lListaExpedientesSeleccionados, c, params, gVO);
                             if (finTramite > 0) {
                             request.setAttribute("resInsertTable", Integer.valueOf(resInsertTable));
                             } else {
                             resInsertTable = 0;
                             m_Log.debug(" ****************** ANTES DE DESHACER LOS CAMBIOS REALIZADOS");
                             int desCamR = deshacerCambiosRealizados(nombreFichero, params, gVO);
                             m_Log.debug(" ****************** DESPUÃ‰S DE DESHACER LOS CAMBIOS REALIZADOS");
                             if (desCamR > 0) {
                             resInsertTable = 0;
                             request.setAttribute("resInsertTable", Integer.valueOf(resInsertTable));
                             } else {
                             resInsertTable = 2;
                             request.setAttribute("resInsertTable", Integer.valueOf(resInsertTable));
                             }
                             }*/
                        } else {
                            m_Log.info("Todos los expedientes seleccionados estaban duplicados");
                        }
                    } catch (Exception ex) {
                        m_Log.info(" ============= ERROR en ImpresionExpedienteLanbideAction.exportarCSVCEPAP.DUPLICIDAD: " + ex.getMessage());
                    }
                    Vector consulta = new Vector();
                    Vector consultaE = new Vector();

                    session.setAttribute("RelacionExpedientesCEPAP", consultaE);
                    session.setAttribute("RelacionFicherosImpresionGenerados", consulta);
                    m_Log.info(" ****************** Fin de la recaga de la pagina ****************** ");
                    request.setAttribute("opcion", opcion);

                    /*try {
                     int codOrganizacion = usuario.getOrgCod();

                     m_Log.error(" ****************** Inicio de la recaga de la pagina ****************** " );
                     consultaE = ExpedienteImpresionManager.getInstance().getExpedientesImpresion(codOrganizacion, codProcedimiento, Integer.parseInt(codTramite), params);
                     consulta = ExpedienteImpresionManager.getInstance().getFicherosImpresionGenerados(params);
                     }
                     catch (Exception te) {
                     te.getMessage();
                     consulta = new Vector();
                     consultaE = new Vector();
                     }*/
                    /* Vector consulta = new Vector();
                     Vector consultaE = new Vector();
                     m_Log.debug("COMPROBANDO DUPLICIDAD: END------>");
                     session.setAttribute("RelacionExpedientesCEPAP", consultaE);
                     session.setAttribute("RelacionFicherosImpresionGenerados", consulta);
                     m_Log.error(" ****************** Fin de la recaga de la pagina ****************** " );
                     request.setAttribute("opcion", opcion);*/
                    m_Log.error(" FIN COMPROBAR DUPLICIDAD ");
                    /*     */                } catch (Exception ex) {
                    /* 228 */ m_Log.info(" ============= ERROR en ImpresionExpedienteLanbideAction.exportarCSVCEPAP: " + ex.getMessage());
                    /*     */                }
                /*     */            } /* 232 */ else if ("listadoExpedientes".equals(opcion)) /*     */ {
                /* 234 */ String nombreFichero = request.getParameter("nombreFichero");
                /* 235 */ m_Log.debug("Fichero a visualizar: " + nombreFichero);
                /* 236 */ int codOrganizacion = usuario.getOrgCod();
                /* 237 */ ArrayList salida = ExpedienteImpresionManager.getInstance().getListaExpedientesDocumento(codOrganizacion, nombreFichero, params);
                /* 238 */ request.setAttribute("lista_expedientes_documento", salida);
                /* 239 */ request.setAttribute("nombreFichero", nombreFichero);
                /* 240 */ opcion = "expedientesRelacionadosExcel";
                /*     */            } else if ("imprimirEtiquetas".equals(opcion)) {
                String fichero = request.getParameter("nombreFichero");
                /* 235 */ m_Log.error("Fichero a visualizar: " + fichero);
                try {
                    Map parameters = new HashMap();
                    ArrayList<String> expedientes = ExpedienteImpresionManager.getInstance().getExpediente(fichero, params);
                    List elementos = new ArrayList();
                    List listaElementos = new ArrayList();
                    List listaElementos2 = new ArrayList();
                    for (String exp : expedientes) {
                        //lleno el arraylist elementos con todos los participantes
                        ArrayList<Participantes> parti = ExpedienteImpresionManager.getInstance().getParticipantes(exp, params);
                        for (Participantes par : parti) {
                            if (par.getApe1() == null) {
                                par.setApe1("");
                            }
                            if (par.getApe2() == null) {
                                par.setApe2("");
                            }
                            if (par.getCalle() == null) {
                                par.setCalle("");
                            }
                            if (par.getNum() == null) {
                                par.setNum("");
                            }
                            if (par.getMuni() == null) {
                                par.setMuni("");
                            }
                            if (par.getProv() == null) {
                                par.setProv("");
                            }
                            String nombreC = par.getNombre() + " " + par.getApe1() + " " + par.getApe2();
                            String direccion = "C/" + par.getCalle() + ", " + par.getNum();
                            String direccion2 = par.getMuni();
                            if (!par.getProv().equals("")) {
                                direccion2 += " (" + par.getProv() + ")";
                            }

                            HashMap elemento = new HashMap();
                            elemento.put("nombreC", nombreC != null ? nombreC : "");
                            elemento.put("direccion", direccion);
                            elemento.put("direccion2", direccion2);
                            elementos.add(elemento);
                        }
//                            for(int i = 0; i< parti.size(); i = i+2)
//                            //for(Participantes par: parti)
//                            {
//                                Participantes par = parti.get(i);
//                                String nombreC = par.getNombre() + " " + par.getApe1() + " " + par.getApe2();
//                                String direccion = par.getCalle() + ", " + par.getNum();
//                                String direccion2 =  par.getMuni() + " ("+ par.getProv()+")";
//
//                                HashMap elemento = new HashMap();
//                                elemento.put("nombreC", nombreC != null ? nombreC : "");
//                                elemento.put("direccion", direccion);
//                                elemento.put("direccion2", direccion2);
//                                listaElementos.add(elemento);                                
//                            }
//                            for(int i = 1; i< parti.size(); i = i+2)
//                            {
//                                Participantes par = parti.get(i);
//                                String nombreC = par.getNombre() + " " + par.getApe1() + " " + par.getApe2();
//                                String direccion = par.getCalle() + ", " + par.getNum();
//                                String direccion2 =  par.getMuni() + " ("+ par.getProv()+")";
//
//                                HashMap elemento = new HashMap();
//                                elemento.put("nombreC", nombreC != null ? nombreC : "");
//                                elemento.put("direccion", direccion);
//                                elemento.put("direccion2", direccion2);
//                                listaElementos2.add(elemento);                                
//                            }
                    }
                    //recorro elementos y los separo entre pares e impares para que quede correctamente
                    for (int i = 0; i < elementos.size(); i = i + 2) {
                        listaElementos2.add(elementos.get(i));
                    }
                    for (int i = 1; i < elementos.size(); i = i + 2) {
                        listaElementos.add(elementos.get(i));
                    }
                    parameters.put("listaElemento1", listaElementos);
                    parameters.put("listaElemento2", listaElementos2);
                    List<Subreport> subreportList = new ArrayList<Subreport>();
                    Subreport subreport = null;
                    String xmlReport = ExpedienteImpresionManager.getInstance().generarXML("");
                    subreport = ExpedienteImpresionManager.getInstance().inicializarSubreport("etiquetas", xmlReport, "", parameters);
                    subreportList.add(subreport);
                    byte[] pdf = null;
                    pdf = ExpedienteImpresionManager.getInstance().runMasterReportWithSubreports("report_main", parameters, subreportList, "PDF");

                    if (pdf != null && pdf.length > 0) {
                        response.setContentType("application/pdf");
                        response.setHeader("Content-Disposition", "attachment;filename=etiqueta.pdf");
                        response.setContentLength(pdf.length);
                        response.getOutputStream().write(pdf, 0, pdf.length);
                        response.getOutputStream().flush();
                        response.getOutputStream().close();

                    }
                } catch (Exception ex) {
                    m_Log.error("Error imprimiendo etiquetas: " + ex.toString());
                }
            } else if (opcion.equals("verFicheros")) {
                String nombreFichero = request.getParameter("nombreFichero");
                byte[] fichero = ExpedienteImpresionManager.getInstance().getContenidoDocumento(nombreFichero, params);

                //ver documento
                BufferedOutputStream bos = null;
                try {

                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "inline; filename=" + nombreFichero + ".csv");
                    response.setHeader("Content-Transfer-Encoding", "binary");

                    ServletOutputStream out = response.getOutputStream();
                    response.setContentLength(fichero.length);
                    bos = new BufferedOutputStream(out);
                    bos.write(fichero, 0, fichero.length);
                    bos.flush();
                    bos.close();

                } catch (Exception e) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("Excepcion en catch de VerDocumentoServlet.defaultAction()");
                    }
                    //throw e;
                } finally {
                    if (bos != null) {
                        bos.close();
                    }
                }
            }
            /*     */
            /*     */        }
        /*     */
        /* 246 */ m_Log.debug(
                "<================= ImpresionExpedientesLanbideAction ======================");
        /* 247 */ m_Log.debug(
                "opcion->" + opcion);
        /* 248 */ return mapping.findForward(opcion);
        /*     */    }
    /*     */
    /*     */ public int deshacerCambiosRealizados(String nombreFichExcel, String[] params, GeneralValueObject gVO) throws SQLException /*     */ {
        /* 255 */ PreparedStatement ps = null;
        /* 256 */ Connection con = null;
        /* 257 */ AdaptadorSQLBD adapt = null;
        /* 258 */ adapt = new AdaptadorSQLBD(params);
        /* 259 */ String sql = "";
        /* 260 */ String sql1 = "";
        /* 261 */ int resultado = 0;
        /* 262 */ String nombreFichExcelR = (String) gVO.getAtributo("baseDir") + "/" + (String) gVO.getAtributo("usuDir") + "/csv" + "/" + nombreFichExcel + ".csv";
        /*     */ try /*     */ {
            /* 267 */ con = adapt.getConnection();
            /* 268 */ adapt.inicioTransaccion(con);
            /*     */
            /* 270 */ sql = "DELETE FROM MELANBIDE03_EXP_IMPR_CEPAP WHERE  NOMBRE_FICHERO = '" + nombreFichExcel + "'";
            /* 271 */ sql1 = "DELETE FROM MELANBIDE03_IMPRESION_CEPAP WHERE  NOMBRE_FICHERO = '" + nombreFichExcel + "'";
            /*     */
            /* 273 */ if (m_Log.isDebugEnabled()) {
                /* 274 */ m_Log.debug(sql);
                /*     */            }
            /* 276 */ if (m_Log.isDebugEnabled()) {
                /* 277 */ m_Log.debug(sql1);
                /*     */            }
            /* 279 */ ps = con.prepareStatement(sql);
            /* 280 */ resultado = ps.executeUpdate();
            /* 281 */ ps = con.prepareStatement(sql1);
            /* 282 */ resultado = ps.executeUpdate();
            /*     */
            /* 284 */ adapt.finTransaccion(con);
            /*     */        } /*     */ catch (Exception ex) {
            /*     */ try {
                /* 288 */ adapt.rollBack(con);
                /* 289 */ resultado = 0;
                /* 290 */         //sqle.printStackTrace();
/* 291 */ if (m_Log.isErrorEnabled()) /* 292 */ {
                    m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
                }
                /*     */            } /*     */ catch (BDException e) {
                /* 295 */ m_Log.error("ERROR al deshacer transacciÃ³n contra la BBDD: " + e.getMessage());
                /*     */            }
            /*     */        } finally {
            /*     */ try {
                /* 299 */ adapt.devolverConexion(con);
                if (ps != null) {
                    ps.close();
                }
                /*     */            } catch (BDException e) {
                /* 301 */ m_Log.error("ERROR al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
                /*     */            }
            /*     */
            /*     */        }
        /*     */
        /* 307 */ File file = new File(nombreFichExcelR);
        /*     */
        /* 309 */ if (file.isFile()) /* 310 */ {
            m_Log.debug("El fichero " + nombreFichExcelR + " existe");
        } /*     */ else {
            /* 312 */ m_Log.debug("El fichero " + nombreFichExcelR + " no existe");
            /*     */        }
        /*     */
        /* 315 */ if (file.delete()) {
            /* 316 */ m_Log.info("El fichero ha sido borrado satisfactoriamente");
            /* 317 */ resultado = 1;
            /*     */        } else {
            /* 319 */ m_Log.info("El fichero no puede ser borrado");
            /* 320 */ resultado = 0;
            /*     */        }
        /* 322 */ return resultado;
        /*     */    }
    /*     */
    /*     */ private Vector<TramitacionExpedientesValueObject> transformarListaTramitesIniciar(Vector<TramitacionExpedientesValueObject> listaTramitesIniciar, String codUnidadTramitadora) {
        /* 326 */ for (int i = 0; (listaTramitesIniciar != null) && (i < listaTramitesIniciar.size()); i++) {
            /* 327 */ ((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).setCodUnidadTramitadoraTram(codUnidadTramitadora);
            /*     */        }
        /*     */
        /* 330 */ return listaTramitesIniciar;
        /*     */    }
    /*     */
    /*     */ public int finalizarAvanzarTramite(int codOrg, String codProc, String codTram, String[] params, UsuarioValueObject usuario, String[] lListaExpedientesSeleccionados) throws BDException, SQLException, TechnicalException, WSException, TramitacionException, RemoteException /*     */ {
        /* 335 */ Connection con = null;
        /* 336 */ AdaptadorSQLBD adapt = null;
        /* 337 */ adapt = new AdaptadorSQLBD(params);
        /* 338 */ con = adapt.getConnection();
        /* 339 */ String codOrgCad = String.valueOf(codOrg);
        /* 340 */ Vector devolver = new Vector();
        /* 341 */ int resultado = 0;
        /*     */
        /* 343 */ Vector listaTramites = new Vector();
        /* 344 */ Vector lista = null;
        /*     */ try /*     */ {
            m_Log.error(" ****************** Finalizando los trámites ****************** ");
            /* 347 */ lista = ExpedienteImpresionManager.getInstance().getFlujoSalida(con, codOrgCad, codProc, codTram, 0);

            //m_Log.error("rlista.isEmpty(): " + lista.isEmpty());
/* 350 */ if (!lista.isEmpty()) {
                /* 351 */ Iterator i = lista.iterator();
                /* 352 */ while (i.hasNext()) {
                    /* 353 */ SiguienteTramiteTO tramite = (SiguienteTramiteTO) i.next();
                    /* 354 */ TramitacionExpedientesValueObject traExp = new TramitacionExpedientesValueObject();
                    /* 355 */ traExp.setCodTramite(tramite.getCodigoTramiteFlujoSalida());
                    /* 356 */ traExp.setOcurrenciaTramite(tramite.getNumeroSecuencia());
                    /* 357 */ traExp.setCodigoTramiteFlujoSalida(tramite.getCodigoTramiteFlujoSalida());
                    /* 358 */ traExp.setInsertarCodUnidadTramitadoraTram("si");
                    /*     */
                    /* 361 */ listaTramites.add(traExp);
                    /*     */                }
                /*     */
                /* 366 */ for (int j = 0; j < lListaExpedientesSeleccionados.length; j++) /*     */ {
                    /* 368 */ String ejercicio = lListaExpedientesSeleccionados[j].substring(0, 4);
                    /*     */
                    /* 370 */ TramitacionExpedientesValueObject tAUX = ExpedienteImpresionDAO.getInstance().getInfoTramite(codProc, lListaExpedientesSeleccionados[j], codTram, con);
                    /*     */
                    /* 372 */ String transformarListaTramitesIniciar = tAUX.getCodUnidadTramitadoraTram();
                    /*     */
                    /* 374 */ TramitacionExpedientesValueObject traExpVO = new TramitacionExpedientesValueObject();
                    /* 375 */ traExpVO.setListaEMailsAlIniciar(new Vector());
                    /* 376 */ traExpVO.setListaEMailsAlFinalizar(new Vector());
                    /* 377 */ traExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                    /* 378 */ traExpVO.setCodIdiomaUsuario(usuario.getIdioma());
                    /* 379 */ traExpVO.setListaTramitesIniciar(transformarListaTramitesIniciar(listaTramites, transformarListaTramitesIniciar));
                    /* 380 */ traExpVO.setCodMunicipio(codOrgCad);
                    /* 381 */ traExpVO.setCodOrganizacion(codOrgCad);
                    /* 382 */ traExpVO.setCodProcedimiento(codProc);
                    /* 383 */ traExpVO.setEjercicio(ejercicio);
                    /* 384 */ traExpVO.setNumeroExpediente(lListaExpedientesSeleccionados[j]);
                    /* 385 */ traExpVO.setNumero(lListaExpedientesSeleccionados[j]);
                    /* 386 */ traExpVO.setCodTramite(codTram);
                    /* 387 */ traExpVO.setOcurrenciaTramite(tAUX.getOcurrenciaTramite());
                    /* 388 */ traExpVO.setCodUnidadTramitadoraTram(tAUX.getCodUnidadTramitadoraTram());
                    /* 389 */ traExpVO.setCodEntidad(String.valueOf(usuario.getEntCod()));
                    /*     */ try {
                        /* 391 */ devolver = ExpedienteImpresionManager.getInstance().finalizarTramitesImpresion(traExpVO, params);
                        /*     */                    } /*     */ catch (EjecucionSWException e) {
                        /* 394 */ throw new RemoteException(e.getMensaje(), e);
                        /*     */                    }
                    /* 396 */ if (devolver == null) {
                        /* 397 */ m_Log.info("Tramite no grabado");
                        /* 398 */ resultado = 0;
                        /* 399 */             //e = resultado;
/*     */ return resultado;
                        /*     */                    }
                    /* 402 */ m_Log.info("Tramite grabado");
                    /* 403 */ resultado = 1;
                    /*     */                }
                /*     */            } /*     */ else {
                /* 407 */ resultado = 0;
                m_Log.error("resultado = 0");
                /*     */

            }
            /*     */        } /*     */ catch (SQLException ex) {
            /* 411 */ Logger.getLogger(ImpresionExpedientesLanbideAction.class
                    .getName()).log(Level.SEVERE, null, ex);
            /*     */        } finally {
            /*     */ try {
                /* 414 */ adapt.devolverConexion(con);
                /*     */            } catch (BDException e) {
                /* 416 */ m_Log.error("ERROR AL DEVOLVER LA CONEXIÃ“N A LA BBDD: " + e.getMessage());
                /*     */            }
            /*     */        }
        /*     */
        /* 420 */ return resultado;
        /*     */    }
    /*     */
    /*     */ public int guardarDatosExcelTabla(String nombreFichExcel, String[] lListaExpedientesSeleccionados, Calendar c, String[] params, GeneralValueObject gVO)
            /*     */ throws Exception /*     */ {
        /* 426 */ int resInsertImprExp = 0;
        /* 427 */ int resInsertImpr = 0;

        /* 428 */ String nombreFichExcelR = (String) gVO.getAtributo("baseDir") + "/" + (String) gVO.getAtributo("usuDir") + "/csv" + "/" + nombreFichExcel + ".csv";
        /*     */ m_Log.error("guardarDatosExcelTabla. nombreFichExcelR: " + nombreFichExcelR);
        /* 431 */ File file = new File(nombreFichExcelR);
        m_Log.error(" ****************** Inciado el guardado de datos ****************** ");
        /*     */ try /*     */ {
            /* 434 */ FileInputStream fin = new FileInputStream(file);
            /* 435 */ byte[] fileContent = new byte[(int) file.length()];
            /* 436 */ fin.read(fileContent);
            /*     */
            /* 439 */ java.util.Date fecha = c.getTime();
            /* 440 */ SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
            /* 441 */ String cadFecha = formatoDeFecha.format(fecha);
            /* 442 */ fecha = formatoDeFecha.parse(cadFecha);
            /*     */
            /* 444 */ resInsertImpr = inserta_MELANBIDE03_IMPRESION_CEPAP(nombreFichExcel, c, fileContent, params);
            m_Log.error(" ****************** inserta_MELANBIDE03_IMPRESION_CEPAP ****************** ");
            /* 445 */ if (resInsertImpr == 1) {
                /* 446 */ resInsertImprExp = inserta_MELANBIDE03_EXP_IMPR_CEPAP(nombreFichExcel, lListaExpedientesSeleccionados, params);
                m_Log.error(" ****************** inserta_MELANBIDE03_EXP_IMPR_CEPAP ****************** ");
                /*     */            }
            /*     */
            /* 449 */ fin.close();
            /*     */        } catch (FileNotFoundException e) {
            /* 451 */ m_Log.error("File not found" + e);
            /*     */        } catch (IOException ioe) {
            /* 453 */ m_Log.error("Exception while reading the file " + ioe);
            /*     */        } catch (Exception e) {
            m_Log.error("Error en guardarDatosExcelTabla: " + e);
        }
        /* 455 */ if ((resInsertImpr == 1) && (resInsertImprExp == 1)) {
            /* 456 */ return 1;
            /*     */        }
        /* 458 */ return 0;
        /*     */    }
    /*     */
    /*     */ private int inserta_MELANBIDE03_EXP_IMPR_CEPAP(String nombreFichero, String[] lListaExpedientesSeleccionados, String[] params) throws BDException, SQLException /*     */ {
        /* 463 */ PreparedStatement ps = null;
        /* 464 */ Connection con = null;
        /* 465 */ AdaptadorSQLBD adapt = null;
        /* 466 */ adapt = new AdaptadorSQLBD(params);
        /* 467 */ int resultado = 0;
        /* 468 */ int resultado2 = 0;
        /* 469 */ String sql = "";
        /* 470 */ String sql1 = "";
        /*     */ try /*     */ {
            /* 473 */ con = adapt.getConnection();
            /* 474 */ adapt.inicioTransaccion(con);
            /*     */
            /* 476 */ for (int i = 0; i < lListaExpedientesSeleccionados.length; i++) {
                /* 477 */ sql = "INSERT INTO MELANBIDE03_EXP_IMPR_CEPAP (NOMBRE_FICHERO, NUM_EXPEDIENTE) VALUES ('" + nombreFichero + "','" + lListaExpedientesSeleccionados[i] + "')";
                /*     */
                /* 479 */ m_Log.debug("inserta_MELANBIDE03_EXP_IMPR_CEPAP:" + sql);
                /* 480 */ ps = con.prepareStatement(sql);
                /* 481 */ resultado = ps.executeUpdate();
                /*     */            }
            /* 483 */ if (resultado != 1) {
                /* 484 */ sql = "DELETE FROM MELANBIDE03_EXP_IMPR_CEPAP WHERE  NOMBRE_FICHERO = '" + nombreFichero + "'";
                /* 485 */ sql1 = "DELETE FROM MELANBIDE03_IMPRESION_CEPAP WHERE  NOMBRE_FICHERO = '" + nombreFichero + "'";
                /*     */
                /* 487 */ if (m_Log.isDebugEnabled()) {
                    /* 488 */ m_Log.debug(sql);
                    /*     */                }
                /* 490 */ if (m_Log.isDebugEnabled()) {
                    /* 491 */ m_Log.debug(sql1);
                    /*     */                }
                /* 493 */ ps = con.prepareStatement(sql);
                /* 494 */ resultado2 = ps.executeUpdate();
                /* 495 */ ps = con.prepareStatement(sql1);
                /* 496 */ resultado2 = ps.executeUpdate();
                /*     */            }
            /*     */
            /* 499 */ adapt.finTransaccion(con);
            /*     */        } /*     */ catch (Exception e) {
            /* 502 */ adapt.rollBack(con);
            /* 503 */ resultado = 0;
            /* 504 */       //sqle.printStackTrace();
/* 505 */ if (m_Log.isErrorEnabled()) /* 506 */ {
                m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
            }
            /*     */        } /*     */ finally {
            /*     */ try {
                /* 510 */ adapt.devolverConexion(con);
                if (ps != null) {
                    ps.close();
                }
                /*     */            } catch (BDException e) {
                /* 512 */ e.printStackTrace();
                /*     */            }
            /*     */        }
        /*     */
        /* 516 */ return resultado;
        /*     */    }
    /*     */
    /*     */ private int inserta_MELANBIDE03_IMPRESION_CEPAP(String nombreFichero, Calendar c, byte[] fileContent, String[] params)
            /*     */ throws Exception /*     */ {
        /* 522 */ Connection con = null;
        /* 523 */ AdaptadorSQLBD adapt = null;
        /* 524 */ adapt = new AdaptadorSQLBD(params);
        /* 525 */ int resultado = 0;
        /* 526 */ String sql = "";
        /* 527 */ java.util.Date fecha = c.getTime();
        /*     */ try /*     */ {
            /* 530 */ con = adapt.getConnection();
            /* 531 */ adapt.inicioTransaccion(con);
            /* 532 */ sql = "INSERT INTO MELANBIDE03_IMPRESION_CEPAP (NOMBRE_FICHERO, FECHA_GENERACION, CONTENIDO)VALUES (?,?,?)";
            /*     */
            /* 534 */ PreparedStatement stmt = con.prepareStatement(sql);
            /* 535 */ stmt.setString(1, nombreFichero);
            /* 536 */ java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());
            /* 537 */ stmt.setDate(2, sqlDate);
            /* 538 */ InputStream st = new ByteArrayInputStream(fileContent);
            /* 539 */ stmt.setBinaryStream(3, st, fileContent.length);
            /*     */
            /* 541 */ if (m_Log.isDebugEnabled()) {
                /* 542 */ m_Log.debug(sql);
                /*     */            }
            /* 544 */ resultado = stmt.executeUpdate();
            /* 545 */ stmt.close();
            /* 546 */ adapt.finTransaccion(con);
            /*     */        } /*     */ catch (Exception e) {
            /* 549 */ adapt.rollBack(con);
            /* 550 */ resultado = 0;
            /* 551 */       //sqle.printStackTrace();
/* 552 */ if (m_Log.isErrorEnabled()) /* 553 */ {
                m_Log.error("SQLException haciendo rollBackTransaction en: " + getClass().getName());
            }
            /*     */        } /*     */ finally {
            /*     */ try {
                /* 557 */ adapt.devolverConexion(con);
                /*     */            } catch (BDException e) {
                /* 559 */ e.printStackTrace();
                /*     */            }
            /*     */        }
        /* 562 */ return resultado;
        /*     */    }
    /*     */
    /*     */ public String crearXML(Vector relacionExp, ConsultaExpedientesValueObject consExpVO) /*     */ {

        /*     */ m_Log.error(" ****************** En crear XML ****************** ");
        /* 593 */ StringBuffer textoXml = new StringBuffer("");
        /* 594 */ textoXml.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        /* 595 */ textoXml.append("<LISTADO_EXPEDIENTES>");
        /* 596 */ textoXml.append("<CRITERIOS>");
        /* 597 */ textoXml.append("<PROCEDIMIENTO/>");
        /* 598 */ textoXml.append("<ESTADO/>");
        /* 599 */ textoXml.append("<TITULAR/>");
        /* 600 */ textoXml.append("<LOCALIZACION/>");
        /* 601 */ textoXml.append("<CLASIFICACION_TRAMITE/>");
        /* 602 */ textoXml.append("<TIPO_LISTADO>1</TIPO_LISTADO>");
        /* 603 */ textoXml.append("</CRITERIOS>");
        /*     */
        /* 605 */ m_Log.debug("NUMERO DE RELACIONES RECUPERADAS:" + consExpVO);
        m_Log.debug("Nuevo XML!!!!!!!!!!!!!!!!!!:");
        /*     */
        /* 607 */ for (Iterator i$ = relacionExp.iterator(); i$.hasNext();) {
            Object aRelacionExp = i$.next();
            /* 608 */ ImpresionExpedientesLanbideValueObject filaInforme = (ImpresionExpedientesLanbideValueObject) aRelacionExp;
            /* 609 */ textoXml.append("<EXPEDIENTE>");
            textoXml.append("<CODEXPEDIENTE>").append(filaInforme.getNumExpediente()).append("</CODEXPEDIENTE>");
            /* 610 */ textoXml.append("<CLAVEREGISTRAL>").append(filaInforme.getClaveRegistral()).append("</CLAVEREGISTRAL>");
            textoXml.append("<NIF>").append(filaInforme.getDni()).append("</NIF>");
            textoXml.append("<APELLIDO1INTERESADO>").append(filaInforme.getApellido1Interesado().toUpperCase()).append("</APELLIDO1INTERESADO>");
            textoXml.append("<APELLIDO2INTERESADO>").append(filaInforme.getApellido2Interesado().toUpperCase()).append("</APELLIDO2INTERESADO>");
            textoXml.append("<NOMBREINTERESADO>").append(filaInforme.getNombreInteresado().toUpperCase()).append("</NOMBREINTERESADO>");
            textoXml.append("<SEXO>").append(filaInforme.getSexo()).append("</SEXO>");
            textoXml.append("<FECHNACIMIENTO>").append(filaInforme.getFechNacimiento()).append("</FECHNACIMIENTO>");
            textoXml.append("<FECHEXPEDICION>").append(filaInforme.getFechaExpedicion()).append("</FECHEXPEDICION>");
            textoXml.append("<REALDECRETO>").append(filaInforme.getRealDecreto()).append("</REALDECRETO>");
            textoXml.append("<FECHARD>").append(filaInforme.getFechaRD()).append("</FECHARD>");
            textoXml.append("<DECRETOMOD>").append(filaInforme.getDecretoMod()).append("</DECRETOMOD>");
            textoXml.append("<FECHADECRETOMOD>").append(filaInforme.getFechaDecretoMod()).append("</FECHADECRETOMOD>");
            textoXml.append("<CODIGOCP>").append(filaInforme.getCodigoCP()).append("</CODIGOCP>");
            textoXml.append("<NOMBRE_CERT_ES>").append(filaInforme.getNombreCertificadoCastellano().toUpperCase()).append("</NOMBRE_CERT_ES>");
            textoXml.append("<NOMBRE_CERT_ESKERA>").append(filaInforme.getNombreCertificadoEuskera().toUpperCase()).append("</NOMBRE_CERT_ESKERA>");
            textoXml.append("<NIVEL>").append(filaInforme.getNivel()).append("</NIVEL>");
            textoXml.append("<CODCENTRO>").append(filaInforme.getCodCentro()).append("</CODCENTRO>");
            textoXml.append("<DESCENTRO>").append(filaInforme.getDesCentro()).append("</DESCENTRO>");
            textoXml.append("<OBSERVACIONES>").append(filaInforme.getObserv()).append("</OBSERVACIONES>");
            textoXml.append("<PROVINCIAINTERESADO>").append(filaInforme.getProvinciaInteresado()).append("</PROVINCIAINTERESADO>");

            //textoXml.append("<FECHAEXPEDIENTE>").append(filaInforme.getFechaExpediente()).append("</FECHAEXPEDIENTE>");
/* 618 */       //textoXml.append("<TRADUCCIONRD>").append(filaInforme.getTraduccionRD()).append("</TRADUCCIONRD>");
/* 624 */ textoXml.append("</EXPEDIENTE>");
            /*     */        }
        /* 626 */ textoXml.append("</LISTADO_EXPEDIENTES>");
        /* 627 */     //m_Log.debug("Creacion del xml:" + textoXml.toString());

        /*     */ m_Log.error(" ****************** XML creado ****************** ");
        /* 628 */ return textoXml.toString();
        /*     */    }
    /*     */
    /*     */ public MessageResources getResources() /*     */ {
        /* 633 */ return null;
        /*     */    }
    /*     */ }

/* Location:           C:\Users\leires.VITORIA\Desktop\
 * Qualified Name:     es.altia.flexia.integracion.lanbide.impresion.ImpresionExpedientesLanbideAction
 * JD-Core Version:    0.6.0
 */
