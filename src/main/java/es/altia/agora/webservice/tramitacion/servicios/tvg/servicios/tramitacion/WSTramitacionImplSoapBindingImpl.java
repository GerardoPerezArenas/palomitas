/**
 * WSTramitacionImplSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.tramitacion;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.sge.SiguienteTramiteTO;
import es.altia.agora.business.sge.persistence.TramitesExpedientesManager;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesAction;
import es.altia.agora.technical.EstructuraNotificacion;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso.TramiteDAO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso.UsuarioDAO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso.ExpedienteDAO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.CROTramiteVO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.SalidaBoolean;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.TramiteVO;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class WSTramitacionImplSoapBindingImpl {

    Log m_Log = LogFactory.getLog(WSTramitacionImplSoapBindingImpl.class);

    /**
     * Devuelve los tramites iniciados en el expediente.
     * @param expediente
     * @param jndi Acceso a la BD
     * @return
     * @throws java.rmi.RemoteException
     */
    public CROTramiteVO[] getTramiteExpediente(String expediente, String org)
            throws java.rmi.RemoteException {
        AdaptadorSQLBD bd = null;
        Connection con = null;
        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
        String gestorD = m_ConfigTechnical.getString("gestor");
        String driverD = m_ConfigTechnical.getString(org + "/driver");
        String urlD = "";
        String usuarioD = m_ConfigTechnical.getString(org + "/usuarioFormularios");
        String passwordD = m_ConfigTechnical.getString(org + "/passFormularios");
        String fichLogD = m_ConfigTechnical.getString(org + "/fichlog");
        String jndi = m_ConfigTechnical.getString(org + "/dataSource");

        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndi};


        try {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("BUSCANDO TRAMITES DE UN EXPEDIENTE");
            }
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            TramiteDAO tramiteDAO = new TramiteDAO();
            List salida = tramiteDAO.getTramitesExpediente(expediente, con, bd);
            CROTramiteVO[] lista = new CROTramiteVO[salida.size()];
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("CREANDO LISTA TRAMITES DE UN EXPEDIENTE");
            }
            for (int i = 0; i < salida.size(); i++) {
                lista[i] = (CROTramiteVO) salida.get(i);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("FIN TRAMITES DE UN EXPEDIENTE");
            }
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bd.devolverConexion(con);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public java.lang.String setTramite(TramiteVO traVO, String org)
            throws java.rmi.RemoteException {

        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
        String gestorD = m_ConfigTechnical.getString("gestor");
        String driverD = m_ConfigTechnical.getString(org + "/driver");
        String urlD = "";
        String usuarioD = m_ConfigTechnical.getString(org + "/usuarioFormularios");
        String passwordD = m_ConfigTechnical.getString(org + "/passFormularios");
        String fichLogD = m_ConfigTechnical.getString(org + "/fichlog");
        String jndi = m_ConfigTechnical.getString(org + "/dataSource");


        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndi};

        TramiteDAO tramitedao = new TramiteDAO();
        try {
            String existe = tramitedao.getExisteTermiteAbierto(traVO.getNumeroExpediente(), traVO.getProcedimiento(),
                    traVO.getMunicipio(), traVO.getEjercicio(), traVO.getCodTramite(), params);

            if (existe == null) {
                TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();

                tramExpVO.setNumeroExpediente(traVO.getNumeroExpediente());
                tramExpVO.setProcedimiento(traVO.getProcedimiento());
                tramExpVO.setCodMunicipio(traVO.getMunicipio());
                tramExpVO.setCodProcedimiento(traVO.getProcedimiento());
                tramExpVO.setEjercicio(traVO.getEjercicio());
                tramExpVO.setCodTramite(traVO.getCodTramite());
                String listaCodTramites = traVO.getCodTramite() + "§¥";
                tramExpVO.setCodUsuario(traVO.getUsuario());
                tramExpVO.setCodOrganizacion(traVO.getOrganizacion());
                tramExpVO.setCodUnidadTramitadoraManual(traVO.getUor());
                tramExpVO.setCodEntidad(traVO.getEntidad());
                tramExpVO.setCodUnidadTramitadoraTram(traVO.getUor());
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setObservaciones(traVO.getObservaciones());

                Vector listaTramitesIniciar = listaTramitesSeleccionados(listaCodTramites);
                tramExpVO.setListaTramitesIniciar(listaTramitesIniciar);
                Vector DS = TramitacionExpedientesManager.getInstance().cargarDatosSuplementariosExpediente(tramExpVO,params);
              	
                tramExpVO.setEstructuraDatosSuplExpediente((Vector) DS.elementAt(0));	
                tramExpVO.setValoresDatosSuplExpediente((Vector) DS.elementAt(1));	
                tramExpVO.setEstructuraDatosSuplTramites((Vector) DS.elementAt(2));	
                tramExpVO.setValoresDatosSuplTramites((Vector) DS.elementAt(3));	
                tramExpVO.setEstructuraDatosSuplementarios((Vector) DS.elementAt(4));
                tramExpVO.setValoresDatosSuplementarios((Vector) DS.elementAt(5));

                Vector listaTramitesNoIniciados = new Vector();

                listaTramitesNoIniciados = TramitacionExpedientesManager.getInstance().iniciarTramitesManual(tramExpVO, params);

                tramExpVO.setListaTramitesPendientes(listaTramitesNoIniciados);


                if (listaTramitesNoIniciados.size() == 0) {

                    tramitedao.cambioEstado(traVO.getEstado(), traVO.getNumeroExpediente(), traVO.getProcedimiento(),
                            traVO.getMunicipio(), traVO.getEjercicio(), traVO.getCodTramite(), traVO.getObservaciones(),
                            tramExpVO.getOcurrenciaTramite(), params);
                    if ((traVO.getCamposFormularios() != null) && (!traVO.getCamposFormularios().equals(""))) {
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("Grabando campo del tramite ");
                        }
                        grabarCamposFormulario(traVO.getMunicipio(), traVO.getEjercicio(), traVO.getNumeroExpediente(),
                                traVO.getCodTramite(), tramExpVO.getOcurrenciaTramite(), traVO.getProcedimiento(),
                                traVO.getCamposFormularios(), params);
                    }
                    TramitacionExpedientesAction ficha = new TramitacionExpedientesAction();

                    UsuarioValueObject user = new UsuarioValueObject();
                    user.setParamsCon(params);
                    UsuarioDAO usuariodao = new UsuarioDAO();
                    usuariodao.existeUsuarioCodigo(user, params);
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", traVO.getMunicipio());
                    gVO.setAtributo("expediente", traVO.getNumeroExpediente());
                    gVO.setAtributo("ejercicio", traVO.getEjercicio());
                    boolean notificar = ficha.notificar(user, tramExpVO, (obtenerAsuntoExpediente(params, gVO)));

                    return tramExpVO.getOcurrenciaTramite();
                } else {
                    return "-1";
                }

            } else {
                return "-2" + existe;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "-3" + e.getMessage();
        }

    }

    private Vector listaTramitesSeleccionados(String listSelecc) {
        Vector lista = new Vector();
        StringTokenizer codigos = null;

        if (listSelecc != null) {
            codigos = new StringTokenizer(listSelecc, "§¥", false);

            while (codigos.hasMoreTokens()) {
                String cod = codigos.nextToken();
                TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                tEVO.setCodigoTramiteFlujoSalida(cod);
                tEVO.setCodTramite(cod);
                lista.addElement(tEVO);
                m_Log.debug("-->" + cod);
            }

        }
        return lista;

    }

    //TODO igualar con finalizarTramiteResolucion agora cuando solucionado retroceder tramite con formularios
    public SalidaBoolean finalizarConTramites(String usuario, String municipio, String procedimiento, String ejercicio,
            String expediente, String tramite, String ocurrencia, String org)
            throws java.rmi.RemoteException {

        if (m_Log.isDebugEnabled()) {
            m_Log.debug("Finalizar con Tramites en servicio web");
        }

        Vector mns = new Vector();
        SalidaBoolean salida = new SalidaBoolean();
        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
        String gestorD = m_ConfigTechnical.getString("gestor");
        String driverD = m_ConfigTechnical.getString(org + "/driver");
        String urlD = "";
        String usuarioD = m_ConfigTechnical.getString(org + "/usuarioFormularios");
        String passwordD = m_ConfigTechnical.getString(org + "/passFormularios");
        String fichLogD = m_ConfigTechnical.getString(org + "/fichlog");
        String jndi = m_ConfigTechnical.getString(org + "/dataSource");
        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndi};

        TramitacionExpedientesValueObject teVO = new TramitacionExpedientesValueObject();
        teVO.setCodMunicipio(municipio);
        teVO.setCodProcedimiento(procedimiento);
        teVO.setEjercicio(ejercicio);
        teVO.setNumeroExpediente(expediente);
        teVO.setNumero(expediente);
        teVO.setCodTramite(tramite);
        teVO.setOcurrenciaTramite(ocurrencia);
        teVO.setUsuario(usuario);
        teVO.setCodUsuario(usuario);
        teVO.setCodOrganizacion(org);
        teVO.setCodEntidad("1");

        if (m_Log.isDebugEnabled()) {
            m_Log.debug("Carga de datos");
        }

        teVO = TramitacionExpedientesManager.getInstance().cargarDatos(teVO, params);
        
          //Cargar DATOS SUPLEMENTARIOS
        Vector DS = TramitacionExpedientesManager.getInstance().cargarDatosSuplementariosExpediente(teVO,params);	
        teVO.setEstructuraDatosSuplExpediente((Vector) DS.elementAt(0));
        teVO.setValoresDatosSuplExpediente((Vector) DS.elementAt(1));	
        teVO.setEstructuraDatosSuplTramites((Vector) DS.elementAt(2));	
        teVO.setValoresDatosSuplTramites((Vector) DS.elementAt(3));
        
        /****** INICIO: COMPROBACIÓN DE FINALIZACIÓN DEL EXPEDIENTE *********/
         if ("F".equals(teVO.getAccion())) {
             // Si el trámite es el de finalización => Se comprueba primero si el expediente tiene más trámites pendientes
             // de finalizar
                
            //finalizar expediente
            Vector listaTramitesNoFinalizados = new Vector();
            listaTramitesNoFinalizados = TramitesExpedientesManager.getInstance().getTramitesExpedienteSinFinalizar(teVO, params);
            if(listaTramitesNoFinalizados.size() == 0) {
               int res = TramitacionExpedientesManager.getInstance().finalizarExpediente(teVO,params);
                if(res >0) {
                    salida.setResultado(Boolean.TRUE);
                    String[] vacio = new String[0];
                    salida.setIncidencias(vacio);
                    mns.add("Expediente Finalizado");
                } else {
                    teVO.setRespOpcion("noGrabado");
                    mns.add("Tramite no grabado");
                    salida.setResultado(Boolean.FALSE);
                }
            } else {
                teVO.setRespOpcion("1");
                mns.add("No se puede finalizar el expediente, porque hay ocurrencias de trámites pendientes");
                salida.setResultado(Boolean.FALSE);
            }
            
            
            return salida;
        }else /****** FIN: COMPROBACIÓN DE FINALIZACIÓN DEL EXPEDIENTE ***********/    
        {
        
            Vector<SiguienteTramiteTO> tramitesParaIniciar = teVO.getListaTramitesFavorables(); /*Vector de SiguienteTramiteVO*/
            Vector<TramitacionExpedientesValueObject> auxiliar = new Vector<TramitacionExpedientesValueObject>();
            for (int f = 0; f < tramitesParaIniciar.size(); f++) {
                auxiliar.add(convertToTramitacionExpedientesValueObject(tramitesParaIniciar.elementAt(f), teVO.getCodMunicipio(),
                        teVO.getCodProcedimiento(), params));
            }
            teVO.setListaTramitesFavorables(auxiliar);

           

            teVO.setListaEMailsAlIniciar(new Vector());
            teVO.setListaEMailsAlFinalizar(new Vector());

            Vector listaTramitesIniciar = teVO.getListaTramitesFavorables();//Tramites que se deberian inciar por condiciones de salida

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("el tamaño de la lista de trámites para iniciar es : " + listaTramitesIniciar.size());
            }

            /*************************** INICIO: COMPROBACIÓN DE SI PARA LA LISTA DE TRÁMITES, SE VERIFICAN SUS CONDICIONES DE ENTRADA *****************************/

            for(int i=0;listaTramitesIniciar!=null && i<listaTramitesIniciar.size();i++){
                ((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i)).setCodMunicipio(municipio);
                ((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i)).setCodOrganizacion(municipio);
                ((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i)).setCodProcedimiento(procedimiento);
                ((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i)).setEjercicio(ejercicio);
                ((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i)).setNumeroExpediente(expediente);
                ((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i)).setCodigoTramiteFlujoSalida(((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i)).getCodTramite());
                ((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i)).setCodEntidad("1");
                ((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i)).setCodUsuario(usuario);
            }// for         

            Vector resultadoTramitesIniciar = new Vector();        
            for(int i=0;listaTramitesIniciar!=null && i<listaTramitesIniciar.size();i++){
                Vector error = this.comprobarCondicionesEntrada((TramitacionExpedientesValueObject)listaTramitesIniciar.get(i),params, teVO.getCodTramite());
                if(error==null || error.size()==0){
                    // el trámite siguiente verifica las condiciones de entrada, entonces continuar en la lista de trámites
                    // a iniciar tras finalizar el actual
                    resultadoTramitesIniciar.add(listaTramitesIniciar.get(i));                
                }            
            }        
            // En listaTramitesIniciar sólo se quedan los trámites siguientes que verifican sus condiciones de entrada
            listaTramitesIniciar = resultadoTramitesIniciar;
            /*************************** FIN: COMPROBACIÓN DE SI PARA LA LISTA DE TRÁMITES, SE VERIFICAN SUS CONDICIONES DE ENTRADA *******************************/


            teVO.setListaTramitesIniciar(listaTramitesIniciar);
            Vector listaTramitesNoIniciados = new Vector();
           
            try {
                listaTramitesNoIniciados = TramitacionExpedientesManager.getInstance().finalizarConTramites(teVO,params);
            } catch (EjecucionSWException e) {
                throw new java.rmi.RemoteException(e.getMensaje(), e);
            }

            Vector codTramitesNoIniciados = new Vector();
            if (listaTramitesNoIniciados == null) {
                teVO.setRespOpcion("noGrabado");
                mns.add("Tramite no grabado");
                salida.setResultado(Boolean.FALSE);
            } else {
                salida.setResultado(Boolean.TRUE);
                teVO.setListaTramitesPendientes(listaTramitesNoIniciados);
                if (listaTramitesNoIniciados.size() != 0) {
                    String tram = "";
                    for (int i = 0; i < listaTramitesNoIniciados.size(); i++) {
                        GeneralValueObject gvo = (GeneralValueObject) listaTramitesNoIniciados.get(i);
                        codTramitesNoIniciados.add((String) gvo.getAtributo("codTramite"));
                        tram += gvo.getAtributo("codTramite") + " ; ";
                    }
                    mns.add("Tramites pendientes de finalizar:: " + tram);
                }
            }

            //Creo un GVO para almacenar datos necesarios posteriomente en notificaciones FINALIZAR
            GeneralValueObject objectNotificar = new GeneralValueObject();
            objectNotificar.setAtributo("codOrganizacion", org);
            objectNotificar.setAtributo("codMunicipio", municipio);
            objectNotificar.setAtributo("codProcedimiento", procedimiento);
            objectNotificar.setAtributo("codTramite", tramite);
            objectNotificar.setAtributo("usuario", usuario);
            objectNotificar.setAtributo("ocurrencia", ocurrencia);
            objectNotificar.setAtributo("expediente", expediente);
            objectNotificar.setAtributo("ejercicio", ejercicio);
            notificarFinalTramite(params, objectNotificar);
            notificarInicioTramite(params, objectNotificar, listaTramitesNoIniciados, listaTramitesIniciar);

            String[] incidencia = new String[mns.size()];

            for (int i = 0; i < mns.size(); i++) {
                incidencia[i] = (String) mns.get(i);
            }

            salida.setIncidencias(incidencia);
            return salida;
        }// else
    }

    /**
     * Convierte un objeto SiguienteTramiteTO en un objeto TramitacionExpedientesValueObject
     * @param sig: SiguienteTramiteTO
     * @return: TramitacionExpedientesValueObject
     */
    private TramitacionExpedientesValueObject convertToTramitacionExpedientesValueObject(SiguienteTramiteTO sig,
            String municipio, String procedimiento, String[] params) {
        TramitacionExpedientesValueObject tvo = new TramitacionExpedientesValueObject();

        tvo.setCodTramite(sig.getCodigoTramiteFlujoSalida());
        tvo.setCodigoTramiteFlujoSalida(sig.getCodigoTramiteFlujoSalida());
        tvo.setNumeroSecuencia(sig.getNumeroSecuencia());
        tvo.setCodigoVisibleTramiteFlujoSalida(sig.getCodigoVisibleTramiteFlujoSalida());
        tvo.setDescripcionTramiteFlujoSalida(sig.getDescripcionTramiteFlujoSalida());
        tvo.setModoSeleccionUnidad(Integer.toString(sig.getModoSeleccionUnidad()));
        tvo.setCodUnidadTramitadoraTram(determinarUnidadTramite(municipio, procedimiento, tvo.getCodTramite(), params));

        return tvo;
    }

    private Vector buscaTramitesIniciados(Vector listaTramitesNoIniciados, Vector listaTramitesIniciar) {
        String[] codigosNoIniciar = new String[listaTramitesNoIniciados.size()];
        for (int y = 0; y < listaTramitesNoIniciados.size(); y++) {
            codigosNoIniciar[y] = (String) ((GeneralValueObject) listaTramitesNoIniciados.get(y)).getAtributo("codTramite");
        }

        Vector inicioNotifica = new Vector();
        String dato = "";
        for (int t = 0; t < listaTramitesIniciar.size(); t++) {
            dato = ((TramitacionExpedientesValueObject) (listaTramitesIniciar.get(t))).getCodTramite();
            if (buscaLineal(codigosNoIniciar, dato)) {
            } else {
                inicioNotifica.add(dato);
            }
        }
        return inicioNotifica;
    }

    private boolean buscaLineal(String[] vector, String tramite) {

        boolean res = false;
        for (int i = 0; i < vector.length; i++) {
            if (tramite.equals(vector[i])) {
                return true;
            }
        }
        return res;

    }

    //TODO igualar con finalizarTramiteResolucion agora cuando solucionado retroceder tramite con formularios
    public SalidaBoolean finalizarTramiteResolucion(String usuario, Boolean aceptado, String municipio, String procedimiento,
            String periodo, String expediente, String tramite, String ocurrencia, String org)
            throws java.rmi.RemoteException {


        Vector mns = new Vector();
        SalidaBoolean salida = new SalidaBoolean();
        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
        String gestorD = m_ConfigTechnical.getString("gestor");
        String driverD = m_ConfigTechnical.getString(org + "/driver");
        String urlD = "";
        String usuarioD = m_ConfigTechnical.getString(org + "/usuarioFormularios");
        String passwordD = m_ConfigTechnical.getString(org + "/passFormularios");
        String fichLogD = m_ConfigTechnical.getString(org + "/fichlog");
        String jndi = m_ConfigTechnical.getString(org + "/dataSource");
        int res = -1;

        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndi};

        TramitacionExpedientesValueObject teVO = new TramitacionExpedientesValueObject();
        teVO.setCodMunicipio(municipio);
        teVO.setCodProcedimiento(procedimiento);
        teVO.setEjercicio(periodo);
        teVO.setNumeroExpediente(expediente);
        teVO.setNumero(expediente);
        teVO.setCodTramite(tramite);
        teVO.setOcurrenciaTramite(ocurrencia);
        teVO.setUsuario(usuario);
        teVO.setCodUsuario(usuario);
        teVO.setCodOrganizacion(org);
        teVO.setCodEntidad("1");

        teVO = TramitacionExpedientesManager.getInstance().cargarDatos(teVO, params);
        
        Vector DS = TramitacionExpedientesManager.getInstance().cargarDatosSuplementariosExpediente(teVO,params);	
        teVO.setEstructuraDatosSuplExpediente((Vector) DS.elementAt(0)); 
        teVO.setValoresDatosSuplExpediente((Vector) DS.elementAt(1));	
        teVO.setEstructuraDatosSuplTramites((Vector) DS.elementAt(2));	
        teVO.setValoresDatosSuplTramites((Vector) DS.elementAt(3)); 

        Vector<SiguienteTramiteTO> tramitesParaIniciar = teVO.getListaTramitesFavorables(); /*Vector de SiguienteTramiteVO*/
        Vector<TramitacionExpedientesValueObject> auxiliar = new Vector<TramitacionExpedientesValueObject>();
        for (int f = 0; f < tramitesParaIniciar.size(); f++) {
            auxiliar.add(convertToTramitacionExpedientesValueObject(tramitesParaIniciar.elementAt(f), teVO.getCodMunicipio(),
                    teVO.getCodProcedimiento(), params));
        }
        teVO.setListaTramitesFavorables(auxiliar);


        Vector<SiguienteTramiteTO> tramitesParaNoIniciar = teVO.getListaTramitesNoFavorables(); /*Vector de SiguienteTramiteVO*/
        auxiliar = new Vector<TramitacionExpedientesValueObject>();
        for (int f = 0; f < tramitesParaNoIniciar.size(); f++) {
            auxiliar.add(convertToTramitacionExpedientesValueObject(tramitesParaNoIniciar.elementAt(f), teVO.getCodMunicipio(),
                    teVO.getCodProcedimiento(), params));
        }
        teVO.setListaTramitesNoFavorables(auxiliar);

        teVO.setListaEMailsAlIniciar(new Vector());
        teVO.setListaEMailsAlFinalizar(new Vector());

        Vector listaTramitesIniciar = new Vector();
        if (aceptado.booleanValue()) {
            listaTramitesIniciar = teVO.getListaTramitesFavorables();
        } else {
            listaTramitesIniciar = teVO.getListaTramitesNoFavorables();
        }
        Vector listaTramitesNoIniciados = new Vector();

        if (("F".equals(teVO.getAccionAfirmativa()) && aceptado.booleanValue())
                || ("F".equals(teVO.getAccionNegativa()) && !aceptado.booleanValue())) {
            //finalizar expediente
            Vector listaTramitesNoFinalizados = new Vector();
            listaTramitesNoFinalizados = TramitesExpedientesManager.getInstance().getTramitesExpedienteSinFinalizar(teVO, params);
            if(listaTramitesNoFinalizados.size() == 0) {
                  res = TramitacionExpedientesManager.getInstance().finalizarExpediente(teVO, params);
                if(res >0) {
                    salida.setResultado(Boolean.TRUE);
                    String[] vacio = new String[0];
                    salida.setIncidencias(vacio);
                    mns.add("Expediente Finalizado");
                } else {
                    teVO.setRespOpcion("noGrabado");
                    mns.add("Tramite no grabado");
                    salida.setResultado(Boolean.FALSE);
                }
            } else {
                teVO.setRespOpcion("noGrabado");
                mns.add("Tramite no grabado");
                salida.setResultado(Boolean.FALSE);
            }
        } else {
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("el tamaño de la lista para finalizar es : " + listaTramitesIniciar.size());
            }
            teVO.setListaTramitesIniciar(listaTramitesIniciar);
            boolean resPreg = aceptado.booleanValue();
            try {
                listaTramitesNoIniciados = TramitacionExpedientesManager.getInstance().finalizarConResolucionConTramites(teVO, params, resPreg);
            } catch (EjecucionSWException e) {
                throw new java.rmi.RemoteException(e.getMensaje(), e);
            }
            if (listaTramitesNoIniciados == null) {
                teVO.setRespOpcion("noGrabado");
                mns.add("Tramite no grabado");
                salida.setResultado(Boolean.FALSE);
            } else {
                salida.setResultado(Boolean.TRUE);
                teVO.setListaTramitesPendientes(listaTramitesNoIniciados);
                if (listaTramitesNoIniciados.size() != 0) {
                    String tram = "";
                    for (int i = 0; i < listaTramitesNoIniciados.size(); i++) {
                        GeneralValueObject gvo = (GeneralValueObject) listaTramitesNoIniciados.get(i);
                        tram += gvo.getAtributo("codTramite") + " ; ";
                    }
                    mns.add("Tramites pendientes de finalizar:: " + tram);
                }
            }
        }

            //Creo un GVO para almacenar datos necesarios posteriomente en notificaciones FINALIZAR
            GeneralValueObject objectNotificar = new GeneralValueObject();
        objectNotificar.setAtributo("codOrganizacion", org);
            objectNotificar.setAtributo("codMunicipio", municipio);
            objectNotificar.setAtributo("codProcedimiento", procedimiento);
            objectNotificar.setAtributo("codTramite", tramite);
            objectNotificar.setAtributo("usuario", usuario);
            objectNotificar.setAtributo("ocurrencia", ocurrencia);
            objectNotificar.setAtributo("expediente", expediente);
            objectNotificar.setAtributo("ejercicio", periodo);
            notificarFinalTramite(params, objectNotificar);
            notificarInicioTramite(params, objectNotificar, listaTramitesNoIniciados, listaTramitesIniciar);

            String[] incidencia = new String[mns.size()];

            for (int i = 0; i < mns.size(); i++) {
                incidencia[i] = (String) mns.get(i);
            }

            salida.setIncidencias(incidencia);
        
        return salida;
    }

    private TramitacionExpedientesValueObject getMailsUsuariosAlFinalizar(String[] params, GeneralValueObject gVO)
            throws SQLException {

        AdaptadorSQL oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = null;
        int resultado = 0;
        TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();

        EstructuraNotificacion eNotif = new EstructuraNotificacion();
        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("GET MAILS USUARIOS AL FINALIZAR!!!!!!!!!!!!");
            }

            String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String codTramite = (String) gVO.getAtributo("codTramite");
            String ocurrencia = (String) gVO.getAtributo("ocurrencia");
            String expediente = (String) gVO.getAtributo("expediente");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String codUsuario = (String) gVO.getAtributo("usuario");
            String utf = "";
            String usf = "";
            String inf = "";
            String uor_mail = "";
            String uor_usu = "";
            String usu_mail = "";
            String int_mail = "";
            Vector mailsUOR = new Vector();
            Vector mailsUsusUOR = new Vector();
            Vector mailsInteresados = new Vector();
            Vector usuarios = new Vector();
            String uor = "";
            int procedimientoRestringido=0;

            sql = "SELECT CRO_UTR FROM E_CRO WHERE CRO_TRA=? AND CRO_OCU=? AND CRO_NUM=? AND CRO_EJE=? "
                    + " AND CRO_PRO=? AND CRO_MUN=?";
            st = con.prepareStatement(sql);
            st.setString(1, codTramite);
            st.setString(2, ocurrencia);
            st.setString(3, expediente);
            st.setString(4, ejercicio);
            st.setString(5, codProcedimiento);
            st.setString(6, codMunicipio);
            rs = st.executeQuery();
            m_Log.debug("Consulta la unidad tramitadora del trámite que se finaliza: " + sql);
            if (rs.next()) {
                uor = rs.getString("CRO_UTR");
            }
            m_Log.debug("La unidad trmaitadora es: " + uor);

            sql = "SELECT EXT_TER, EXT_NVR FROM E_EXT WHERE EXT_NUM=? AND EXT_MUN=? AND EXT_EJE=?";
            st = con.prepareStatement(sql);
            st.setString(1, expediente);
            st.setString(2, codMunicipio);
            st.setString(3, ejercicio);
            m_Log.debug("Consulta de terceros del expediente: " + sql);
            rs = st.executeQuery();
            Vector datosInteresados = new Vector();


            while (rs.next()) {
                GeneralValueObject res = new GeneralValueObject();
                res.setAtributo("codigo", rs.getString("EXT_TER"));
                res.setAtributo("version", rs.getString("EXT_NVR"));
                datosInteresados.add(res);
            }
            m_Log.debug("Interesados = " + datosInteresados);


            if (m_Log.isDebugEnabled()) {
                m_Log.debug("UOR DEL TRAMITE QUE FINALIZA : " + uor);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("COD USUARIO : " + codUsuario);
            }
            
            sql = "SELECT TRA_UTF,TRA_USF,TRA_INF,pro_restringido FROM e_tra,e_pro WHERE TRA_MUN=" + codMunicipio
                    + " AND TRA_PRO=" + oad.addString(codProcedimiento)
                    + " AND TRA_COD=" + codTramite +" AND tra_pro=pro_cod and tra_mun=pro_mun";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                utf = rs.getString("TRA_UTF");
                usf = rs.getString("TRA_USF");
                inf = rs.getString("TRA_INF");
                procedimientoRestringido = rs.getInt("pro_restringido");
                resultado = 1;
            }
            rs.close();
            st.close();
            if (resultado > 0) {

                sql = "SELECT TML_VALOR FROM e_tml WHERE TML_MUN=" + codMunicipio
                        + " AND TML_PRO=" + oad.addString(codProcedimiento)
                        + " AND TML_TRA=" + codTramite;
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while (rs.next()) {
                    eNotif.setNombreTramite(rs.getString("TML_VALOR"));
                }
                rs.close();
                st.close();

                if (utf.equals("S")) {
                    //coger mail de a_uor buscando por uor
                    UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],uor);	
                    if (uorDTO!=null && uorDTO.getUor_email()!=null && !uorDTO.getUor_email().equals("")) {	
                        if(m_Log.isDebugEnabled()) m_Log.debug("UNIDAD TRAMITADORA MAIL: "+uor_mail);	
                        mailsUOR.addElement(uor_mail);
                    }
                    
                }
                if (usf.equals("S")) {
                    //coger usuarios de a_uou buscando por uor                                                           
                    if (procedimientoRestringido == 0) {//El procedimiento no es restringido   
                        sql = "SELECT UOU_USU FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou WHERE "
                                + "UOU_UOR=" + uor + " AND UOU_ORG=" + codOrganizacion
                                + " AND UOU_USU<>" + codUsuario;
                    } else {
                        sql = "SELECT UOU_USU FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou, " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE "
                                + "UOU_UOR=" + uor + " AND UOU_ORG=" + codOrganizacion
                                + " AND UOU_USU<>" + codUsuario + " AND USUARIO_PROC_RESTRINGIDO.PRO_COD='" + codProcedimiento + "' AND USUARIO_PROC_RESTRINGIDO.ORG_COD=" + codOrganizacion
                                + " AND USUARIO_PROC_RESTRINGIDO.USU_COD=A_UOU.UOU_USU";

                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(sql);
                    }
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        uor_usu = rs.getString("UOU_USU");
                        if (!(uor_usu == null) && !(uor_usu.equals(""))) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("CODIGO USUARIO: " + uor_usu);
                            }
                            usuarios.addElement(uor_usu);
                        }
                    }
                    rs.close();
                    st.close();
                    //coger mail de a_usu buscando por cod_usu
                    for (int i = 0; i < usuarios.size(); i++) {
                        sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu WHERE "
                                + "USU_COD=" + usuarios.elementAt(i);
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(sql);
                        }
                        st = con.prepareStatement(sql);
                        rs = st.executeQuery();
                        while (rs.next()) {
                            usu_mail = rs.getString("USU_EMAIL");
                            if (!(usu_mail == null) && !(usu_mail.equals(""))) {
                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug("USUARIOS MAIL: " + usu_mail);
                                }
                                mailsUsusUOR.addElement(usu_mail);
                            }
                        }
                        rs.close();
                        st.close();
                    }
                }
                if (inf.equals("S")) {
                    //coger mail de t_ter buscando por datosInteresados
                    if (datosInteresados != null) {
                        for (int i = 0; i < datosInteresados.size(); i++) {
                            GeneralValueObject resVO = (GeneralValueObject) datosInteresados.elementAt(i);
                            sql = "SELECT HTE_DCE FROM T_HTE WHERE HTE_TER=? AND HTE_NVR=?";
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug(sql);
                            }
                            st = con.prepareStatement(sql);
                            st.setString(1, (String) resVO.getAtributo("codigo"));
                            st.setString(2, (String) resVO.getAtributo("version"));
                            rs = st.executeQuery();
                            while (rs.next()) {
                                int_mail = rs.getString("HTE_DCE");
                                if (!(int_mail == null) && !(int_mail.equals(""))) {
                                    if (m_Log.isDebugEnabled()) {
                                        m_Log.debug("INTERESADO MAIL: " + int_mail);
                                    }
                                    mailsInteresados.addElement(int_mail);
                                }
                            }
                            rs.close();
                            st.close();
                        }
                    }
                }
                eNotif.setListaEMailsUOR(mailsUOR);
                eNotif.setListaEMailsUsusUOR(mailsUsusUOR);
                eNotif.setListaEMailsInteresados(mailsInteresados);

                Vector datos = new Vector();
                datos.add(eNotif);
                tEVO.setListaEMailsAlFinalizar(datos);
                tEVO.setTramite(codTramite);
                tEVO.setUnidadTramitadora(uor);
                tEVO.setNumeroExpediente(expediente);
                tEVO.setListaEMailsAlIniciar(new Vector());
                tEVO.setCodMunicipio(codMunicipio);
                tEVO.setCodProcedimiento(codProcedimiento);
                tEVO.setProcedimiento(obtenerNombreProcedimiento (params, tEVO));
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sale de getMailsUsuariosAlFinalizar");
            }
        } catch (Exception e) {
            if (m_Log.isErrorEnabled()) {
                e.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }

        return tEVO;
    }

    private String obtenerAsuntoExpediente(String[] params, GeneralValueObject gVO) {
        AdaptadorSQL oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = null;

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String expediente = (String) gVO.getAtributo("expediente");
            String ejercicio = (String) gVO.getAtributo("ejercicio");

            sql = "SELECT EXP_ASU FROM E_EXP WHERE EXP_NUM=? AND EXP_MUN=? AND EXP_EJE=?";
            st = con.prepareStatement(sql);
            st.setString(1, expediente);
            st.setString(2, codMunicipio);
            st.setString(3, ejercicio);
            rs = st.executeQuery();
            m_Log.debug("Consulta la unidad tramitadora del trámite que se finaliza: " + sql);
            if (rs.next()) {
                return ((String) rs.getString("EXP_ASU"));
            }

        } catch (BDException ex) {
            Logger.getLogger(WSTramitacionImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(WSTramitacionImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }
        return "";
    }

    private String obtenerNombreProcedimiento(String[] params, TramitacionExpedientesValueObject tramExpVO) {
        AdaptadorSQL oad = null;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = null;

        try {

            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT PRO_DES FROM E_PRO WHERE PRO_COD=? AND PRO_MUN=?";
            st = con.prepareStatement(sql);
            st.setString(1, tramExpVO.getCodProcedimiento());
            st.setInt(2, Integer.parseInt(tramExpVO.getCodMunicipio()));
            rs = st.executeQuery();
            m_Log.debug("Consulta nombre procedimiento: " + sql);
            if (rs.next()) {
                return ((String) rs.getString("PRO_DES"));
            }

        } catch (BDException ex) {
            Logger.getLogger(WSTramitacionImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(WSTramitacionImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }
        return "";
    }

    private boolean notificarFinalTramite(String[] params, GeneralValueObject objectNotificar) {
        try {
            return notificar(getMailsUsuariosAlFinalizar(params, objectNotificar), obtenerAsuntoExpediente(params, objectNotificar));
        } catch (SQLException ex) {
            Logger.getLogger(WSTramitacionImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void notificarInicioTramite(String[] params, GeneralValueObject gVO,
            Vector tramitesNoIniciados, Vector TramitesIniciados) {

        Vector tramitesIniciados = new Vector();

        tramitesIniciados = buscaTramitesIniciados(tramitesNoIniciados, TramitesIniciados);

        for (int i = 0; i < tramitesIniciados.size(); i++) {
            gVO.setAtributo("codTramite", (String) tramitesIniciados.get(i));
            notificar(getMailsUsuariosAlIniciar(params, gVO), obtenerAsuntoExpediente(params, gVO));
        }

    }

    private TramitacionExpedientesValueObject getMailsUsuariosAlIniciar(String[] params, GeneralValueObject gVO) {

        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = null;
        int resultado = 0;
        EstructuraNotificacion eNotif = new EstructuraNotificacion();
        AdaptadorSQL oad = null;
        Connection con = null;
        TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("entra en getMailsUsuariosAlIniciar");
            }

            String codOrganizacion = (String) gVO.getAtributo("codOrganizacion");
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String codProcedimiento = (String) gVO.getAtributo("codProcedimiento");
            String codTramite = (String) gVO.getAtributo("codTramite");
            String codUORTramiteIniciado = (String) gVO.getAtributo("codUORTramiteIniciado");
            Vector codInteresados = new Vector();
            String codUsuario = (String) gVO.getAtributo("usuario");
            String expediente = (String) gVO.getAtributo("expediente");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String uti = "";
            String usi = "";
            String ini = "";
            String uor_mail = "";
            String uor_usu = "";
            String usu_mail = "";
            String int_mail = "";
            Vector mailsUOR = new Vector();
            Vector mailsUsusUOR = new Vector();
            Vector mailsInteresados = new Vector();
            Vector usuarios = new Vector();
            int procedimientoRestringido = 0;


            sql = "SELECT CRO_UTR FROM E_CRO WHERE CRO_TRA=? AND CRO_NUM=? AND CRO_EJE=? "
                    + " AND CRO_PRO=? AND CRO_MUN=? AND CRO_FEF IS NULL";
            st = con.prepareStatement(sql);
            st.setString(1, codTramite);
            st.setString(2, expediente);
            st.setString(3, ejercicio);
            st.setString(4, codProcedimiento);
            st.setString(5, codMunicipio);
            rs = st.executeQuery();
            m_Log.debug("Consulta la unidad tramitadora del trámite que se finaliza: " + sql);
            if (rs.next()) {
                codUORTramiteIniciado = rs.getString("CRO_UTR");
            }
            m_Log.debug("La unidad trmaitadora es: " + codUORTramiteIniciado);



            sql = "SELECT EXT_TER, EXT_NVR FROM E_EXT WHERE EXT_NUM=? AND EXT_MUN=? AND EXT_EJE=?";
            st = con.prepareStatement(sql);
            st.setString(1, expediente);
            st.setString(2, codMunicipio);
            st.setString(3, ejercicio);
            m_Log.debug("Consulta de terceros del expediente: " + sql);
            rs = st.executeQuery();


            while (rs.next()) {
                GeneralValueObject res = new GeneralValueObject();
                res.setAtributo("codigo", rs.getString("EXT_TER"));
                res.setAtributo("version", rs.getString("EXT_NVR"));
                codInteresados.add(res);
            }
            m_Log.debug("Interesados = " + codInteresados);


            //Determinar cuales son las notificaciones que hay que enviar
            sql = "SELECT TRA_UTI,TRA_USI,TRA_INI,pro_restringido "
                    + " FROM e_tra,e_pro WHERE TRA_MUN=" + codMunicipio + " AND TRA_PRO=" + oad.addString(codProcedimiento)
                    + " AND TRA_COD=" + codTramite +" AND tra_pro=pro_cod and tra_mun=pro_mun";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                uti = rs.getString("TRA_UTI");
                usi = rs.getString("TRA_USI");
                ini = rs.getString("TRA_INI");
                procedimientoRestringido = rs.getInt("pro_restringido");
                resultado = 1;
            }
            rs.close();
            st.close();
            if (resultado > 0) {

                sql = "SELECT TML_VALOR"
                        + " FROM e_tml WHERE TML_MUN=" + codMunicipio + " AND TML_PRO=" + oad.addString(codProcedimiento)
                        + " AND TML_TRA=" + codTramite;
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug(sql);
                }
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while (rs.next()) {
                    eNotif.setNombreTramite(rs.getString("TML_VALOR"));
                }
                rs.close();
                st.close();
                if (uti.equals("S")) {  
                    //coger mail de a_uor buscando por uor
                     UORDTO uorDTO = (UORDTO)CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(oad.getParametros()[6],codUORTramiteIniciado);
                    if (uorDTO!=null && uorDTO.getUor_email()!=null && !uorDTO.getUor_email().equals("")) {
                         uor_mail=uorDTO.getUor_email();
                        if(m_Log.isDebugEnabled()) m_Log.debug("UNIDAD TRAMITADORA MAIL: "+uor_mail);
                        mailsUOR.addElement(uor_mail);
                    }
                    
                }
                if (usi.equals("S")) {
                    //coger usuarios de a_uou buscando por uor
                    if (procedimientoRestringido == 0) {//El procedimiento no es restringido   
                        sql = "SELECT UOU_USU FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou WHERE "
                                + "UOU_UOR=" + codUORTramiteIniciado + " AND " + "UOU_ORG=" + codOrganizacion + " AND UOU_USU<>" + codUsuario;

                    } else //El procedimiento  es restringido           
                    {
                        sql = "SELECT UOU_USU FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou, "
                                + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE "
                                + "UOU_UOR=" + codUORTramiteIniciado + " AND " + "UOU_ORG=" + codOrganizacion + " AND UOU_USU<>" + codUsuario
                                + " AND USUARIO_PROC_RESTRINGIDO.PRO_COD='" + codProcedimiento + "' AND USUARIO_PROC_RESTRINGIDO.ORG_COD=" + codOrganizacion
                                + " AND USUARIO_PROC_RESTRINGIDO.USU_COD=A_UOU.UOU_USU";
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug(sql);
                    }
                    st = con.prepareStatement(sql);
                    rs = st.executeQuery();
                    while (rs.next()) {
                        uor_usu = rs.getString("UOU_USU");
                        if (!(uor_usu == null) && !(uor_usu.equals(""))) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("CODIGO USUARIO: " + uor_usu);
                            }
                            usuarios.addElement(uor_usu);
                        }
                    }
                    rs.close();
                    st.close();
                    //coger mail de a_usu buscando por cod_usu
                    for (int i = 0; i < usuarios.size(); i++) {
                        sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu WHERE "
                                + "USU_COD=" + usuarios.elementAt(i);
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug(sql);
                        }
                        st = con.prepareStatement(sql);
                        rs = st.executeQuery();
                        while (rs.next()) {
                            usu_mail = rs.getString("USU_EMAIL");
                            if (!(usu_mail == null) && !(usu_mail.equals(""))) {
                                if (m_Log.isDebugEnabled()) {
                                    m_Log.debug("USUARIO MAIL: " + usu_mail);
                                }
                                mailsUsusUOR.addElement(usu_mail);
                            }
                        }
                        rs.close();
                        st.close();
                    }
                }
                if (ini.equals("S")) {
                    //coger mail de t_ter buscando por datosInteresados
                    if (codInteresados != null) {
                        for (int i = 0; i < codInteresados.size(); i++) {
                            GeneralValueObject resVO = (GeneralValueObject) codInteresados.elementAt(i);
                            sql = "SELECT HTE_DCE FROM T_HTE WHERE HTE_TER=? AND HTE_NVR=?";
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug(sql);
                            }
                            st = con.prepareStatement(sql);
                            st.setString(1, (String) resVO.getAtributo("codigo"));
                            st.setString(2, (String) resVO.getAtributo("version"));
                            rs = st.executeQuery();
                            while (rs.next()) {
                                int_mail = rs.getString("HTE_DCE");
                                if (!(int_mail == null) && !(int_mail.equals(""))) {
                                    if (m_Log.isDebugEnabled()) {
                                        m_Log.debug("INTERESADO MAIL: " + int_mail);
                                    }
                                    mailsInteresados.addElement(int_mail);
                                }
                            }
                            rs.close();
                            st.close();
                        }
                    }
                }
                eNotif.setListaEMailsUOR(mailsUOR);
                eNotif.setListaEMailsUsusUOR(mailsUsusUOR);
                eNotif.setListaEMailsInteresados(mailsInteresados);


                Vector datos = new Vector();
                datos.add(eNotif);
                tEVO.setListaEMailsAlIniciar(datos);
                tEVO.setTramite(codTramite);
                tEVO.setUnidadTramitadoraTramiteIniciado(codUORTramiteIniciado);
                tEVO.setNumeroExpediente(expediente);
                tEVO.setListaEMailsAlFinalizar(new Vector());
                tEVO.setCodMunicipio(codMunicipio);
                tEVO.setCodProcedimiento(codProcedimiento);
                tEVO.setProcedimiento(obtenerNombreProcedimiento (params, tEVO));

            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sale de getMailsUsuariosAlIniciar y devuelve: " + eNotif);
            }
        } catch (Exception e) {
            if (m_Log.isErrorEnabled()) {
                m_Log.error("Exception: " + e.getMessage());
            }
            e.printStackTrace();
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }
        return tEVO;
    }

    public boolean notificar(TramitacionExpedientesValueObject tramExpVO, String asuntoExp) {
        boolean resultado = true;

        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
        String usuarioNotificaciones = m_ConfigTechnical.getString("usuarioNotificaciones");

        try {

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("ENTRA EN NOTIFICAR");
            }
            Vector listaEMailsAlIniciar = tramExpVO.getListaEMailsAlIniciar();
            Vector listaEMailsAlFinalizar = tramExpVO.getListaEMailsAlFinalizar();
            Config m_ConfigApplication = ConfigServiceHelper.getConfig("techserver");
            String asunto = m_ConfigApplication.getString("mail.subject");
            String contenido;
            Vector emailsUOR;
            Vector emailsUsusUOR;
            Vector emailsInteresados;
            MailHelper mailHelper = new MailHelper();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("UNIDAD TRAMITADORA DEL USUARIO: " + tramExpVO.getUnidadTramitadora());
            }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("TRAMITE Q FINALIZA: " + tramExpVO.getTramite());
            }

            // Reemplazos de campos en el asunto y el contenido del mensaje
            asunto = asunto.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
            for (int i = 0; i < listaEMailsAlFinalizar.size(); i++) {
                emailsUOR = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEMailsUOR();
                emailsUsusUOR = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEMailsUsusUOR();
                emailsInteresados = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEMailsInteresados();
                for (int j = 0; j < emailsUOR.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteUOR");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@usuario@", usuarioNotificaciones);
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    mailHelper.sendMail((String) emailsUOR.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO ENVIADO A " + emailsUOR.elementAt(j));
                    }
                }
                for (int j = 0; j < emailsUsusUOR.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteUsusUOR");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadora());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    mailHelper.sendMail((String) emailsUsusUOR.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO ENVIADO A " + emailsUsusUOR.elementAt(j));
                    }
                }
                for (int j = 0; j < emailsInteresados.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteInteresados");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    mailHelper.sendMail((String) emailsInteresados.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO ENVIADO A " + emailsInteresados.elementAt(j));
                    }
                }
            }

            for (int i = 0; i < listaEMailsAlIniciar.size(); i++) {
                emailsUOR = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getListaEMailsUOR();
                emailsUsusUOR = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getListaEMailsUsusUOR();
                emailsInteresados = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getListaEMailsInteresados();
                for (int j = 0; j < emailsUOR.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentInicioTramiteUOR");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@usuario@", usuarioNotificaciones);
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("PLAZO TRÁMITE : " + tramExpVO.getPlazo());
                    }

                    mailHelper.sendMail((String) emailsUOR.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO . CONTENIDO : " + contenido);
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO ENVIADO A " + emailsUOR.elementAt(j));
                    }
                }
                for (int j = 0; j < emailsUsusUOR.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentInicioTramiteUsusUOR");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadoraTramiteIniciado());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);

                    mailHelper.sendMail((String) emailsUsusUOR.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO . CONTENIDO : " + contenido);
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO ENVIADO A " + emailsUsusUOR.elementAt(j));
                    }
                }
                for (int j = 0; j < emailsInteresados.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentInicioTramiteInteresados");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    mailHelper.sendMail((String) emailsInteresados.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO ENVIADO A " + emailsInteresados.elementAt(j));
                    }
                }
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("SALE DE NOTIFICAR");
            }
        } catch (MailServiceNotActivedException e) {
            m_Log.error("Servicio de mail no activado");
            //Servicio de mail no activado, funcionamiento normal
            resultado = false;
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
            resultado = false;
        }
        return resultado;
    }

    public SalidaBoolean grabarCamposTramite(String municipio, String ejercicio, String numeroExpediente, String tramite,
            String ocurrencia, String procedimiento, String campos, String org)
            throws java.rmi.RemoteException {

        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
        String gestorD = m_ConfigTechnical.getString("gestor");
        String driverD = m_ConfigTechnical.getString(org + "/driver");
        String urlD = "";
        String usuarioD = m_ConfigTechnical.getString(org + "/usuarioFormularios");
        String passwordD = m_ConfigTechnical.getString(org + "/passFormularios");
        String fichLogD = m_ConfigTechnical.getString(org + "/fichlog");
        String jndi = m_ConfigTechnical.getString(org + "/dataSource");

        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndi};

        SalidaBoolean salida = new SalidaBoolean(new String[0], Boolean.TRUE);
        try {

            grabarCamposFormulario(municipio, ejercicio, numeroExpediente, tramite,
                    ocurrencia, procedimiento, campos, params);

        } catch (Exception e) {
            salida.setResultado(Boolean.FALSE);
            salida.setIncidencias(new String[]{e.getMessage()});
        }
        return salida;

    }

    /* Buscar en la lista el campo cuyo codigo=valor
     *  y que este activo
     */
    private EstructuraCampo obtenerCampo(Vector lista, String valor) {

        for (int i = 0; i < lista.size(); i++) {
            EstructuraCampo campo = (EstructuraCampo) lista.get(i);
            if (campo.getCodCampo().equalsIgnoreCase(valor)) {
                if ("SI".equals(campo.getActivo())) {
                    return campo;
                }
            }
        }
        return null;
    }

    private void grabarCamposFormulario(String municipio, String ejercicio, String numeroExpediente, String tramite,
            String ocurrencia, String procedimiento, String campos, String[] params)
            throws TechnicalException, SQLException, IOException {

        Statement stm = null;
        String sql = null;
        int res = 0;
        AdaptadorSQLBD bd=null;
        Connection con=null;

        ByteArrayInputStream b = new ByteArrayInputStream(campos.getBytes());
        try {
            DocumentBuilder doc = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document docXML = doc.parse(b);
            NodeList nl = docXML.getDocumentElement().getElementsByTagName("dato");
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            ExpedienteDAO e = new ExpedienteDAO();

            //EXPEDIENTE
            Vector listaCSExpediente = e.cargaEstructuraDatosSuplementarios1(municipio, procedimiento, bd, con);
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                String codigo = node.getAttributes().getNamedItem("codigo").getNodeValue();
                String valor = node.getAttributes().getNamedItem("valor").getNodeValue();
                EstructuraCampo campo = obtenerCampo(listaCSExpediente, codigo);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Grabando campo expediente" + codigo);
                }
                if (campo != null) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("..." + campo.getCodCampo());
                    }
                    String tipo = "0";
                    if ((campo.getCodTipoDato() != null) && (!"".equals(campo.getCodTipoDato()))) {
                        tipo = campo.getCodTipoDato();
                    }
                    String mascara = null;
                    m_Log.debug("campo.getMascara=" + campo.getMascara());
                    if ((campo.getMascara() != null) && (!"".equals(campo.getMascara()))) {
                        mascara = campo.getMascara();
                    }
                    grabarCampoExpediente(municipio, ejercicio, numeroExpediente, campo.getCodCampo(), valor, tipo, mascara, procedimiento, con);
                }
            }

            //TRAMITE
            //obtener los campos suplementarios asociados al tramite
            Vector lista = e.cargaEstructuraDatosSuplementarios2(municipio, procedimiento, numeroExpediente, tramite, bd, con);
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                String codigo = node.getAttributes().getNamedItem("codigo").getNodeValue();
                String valor = node.getAttributes().getNamedItem("valor").getNodeValue();
                /* el formulario puede enviar datos que no estan definidos como campos suplementarios
                del tramite. Solo si coinciden se graba.
                 */
                EstructuraCampo campo = obtenerCampo(lista, codigo);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Grabando campo tramite " + codigo);
                }
                if (campo != null) {
                    /* el tipo y mascara del campo se toma de la definicion de campo suplementario,
                    no vienen del formulario */
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("... " + campo.getCodCampo());
                    }
                    String tipo = "0";
                    if ((campo.getCodTipoDato() != null) && (!"".equals(campo.getCodTipoDato()))) {
                        tipo = campo.getCodTipoDato();
                    }

                    String mascara = null;
                    m_Log.debug("campo.getMascara=" + campo.getMascara());
                    if ((campo.getMascara() != null) && (!"".equals(campo.getMascara()))) {
                        mascara = campo.getMascara();
                    }

                    grabarCampo(municipio, ejercicio, numeroExpediente, ocurrencia, procedimiento,
                            campo.getCodCampo(), valor, tipo, mascara, tramite, con);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new TechnicalException(e.getMessage());
        } catch (SAXException sax) {
            sax.printStackTrace();
            throw new TechnicalException(sax.getMessage());
        } catch (Exception tec) {
            tec.printStackTrace();
            throw new TechnicalException(tec.getMessage());
        } finally{
            try {
                bd.devolverConexion(con);
            } catch (BDException ex) {
                ex.printStackTrace();
            }
        }

    }

    private int grabarCampo(String municipio, String ejercicio, String numeroExpediente, String ocurrencia,
            String procedimiento, String codigo, String valor, String tipo, String mascara,
            String numTramite, Connection con)
            throws TechnicalException {
        int res = 0;


        TramiteDAO tramite = new TramiteDAO();

        try {
            if ("1".equals(tipo)) {
                res = tramite.setDatoNumerico(municipio, ejercicio, numeroExpediente, numTramite, ocurrencia, procedimiento,
                        codigo, valor, con);
            } else if ("2".equals(tipo)) {
                res = tramite.setDatoTexto(municipio, ejercicio, numeroExpediente, numTramite, ocurrencia, procedimiento,
                        codigo, valor, con);
            } else if ("3".equals(tipo)) {
                res = tramite.setDatoFecha(municipio, ejercicio, numeroExpediente, numTramite, ocurrencia, procedimiento,
                        codigo, valor, mascara, con);
            } else if ("4".equals(tipo)) {
                res = tramite.setDatoTextoLargo(municipio, ejercicio, numeroExpediente, numTramite, ocurrencia, procedimiento,
                        codigo, valor, con);
            } else if ("6".equals(tipo)){
                res = tramite.setDatoDesplegable(municipio, ejercicio, numeroExpediente, numTramite, ocurrencia, procedimiento, codigo, valor, con);
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("el resultado de grabar los valores de datos suplementarios es : " + res);
            }
        } catch (Exception e) {
            throw new TechnicalException(e.getMessage());
        }
        return res;
    }

    private int grabarCampoExpediente(String municipio, String ejercicio, String numeroExpediente,
            String codigo, String valor, String tipo, String mascara, String procedimiento, Connection con)
            throws SQLException {
        int res = 0;
        ExpedienteDAO expediente = new ExpedienteDAO();

        if ("0".equals(tipo)) {
            res = grabarCampoExpediente(municipio, ejercicio, numeroExpediente, codigo, valor,
                    expediente.getTipoCampo(municipio, procedimiento, codigo, con), mascara, procedimiento, con);
        } else if ("1".equals(tipo)) {
            res = expediente.setDatoNumerico(municipio, ejercicio, numeroExpediente, codigo, valor, con);
        } else if ("2".equals(tipo)) {
            res = expediente.setDatoTexto(municipio, ejercicio, numeroExpediente, codigo, valor, con);
        } else if ("3".equals(tipo)) {
            res = expediente.setDatoFecha(municipio, ejercicio, numeroExpediente, codigo, valor, mascara, con);
        } else if ("4".equals(tipo)) {
            res = expediente.setDatoTextoLargo(municipio, ejercicio, numeroExpediente, codigo, valor, con);
        } else if ("6".equals(tipo)){
            res = expediente.setDatoDesplegable(municipio, ejercicio, numeroExpediente, codigo, valor, con);
        }

        if (m_Log.isDebugEnabled()) {
            m_Log.debug("el resultado de grabar los valores de datos suplementarios es : " + res);
        }

        return res;
    }

    private String determinarUnidadTramite(String municipio, String procedimiento,
            String tramite, String[] params) {

        PreparedStatement st_tra = null, st_utr = null;
        ResultSet rs_tra = null, rs_utr = null;
        AdaptadorSQL oad = null;
        Connection con = null;
        String tipoInicio = "";
        String resultado = "";

        String sql_tra = "SELECT TRA_UTR FROM E_TRA WHERE TRA_PRO=? AND TRA_MUN=? AND TRA_COD=?";
        String sql_utr = "SELECT COUNT(*) AS UORS_INICIO FROM E_TRA_UTR WHERE TRA_PRO=? AND TRA_MUN=? AND TRA_COD=?";

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st_tra = con.prepareStatement(sql_tra);
            st_utr = con.prepareStatement(sql_utr);
            st_tra.setString(1, procedimiento);
            st_utr.setString(1, procedimiento);
            st_tra.setString(2, municipio);
            st_utr.setString(2, municipio);

            m_Log.debug(sql_tra);
            m_Log.debug(sql_utr);

            tipoInicio = null;
            int unidades = 0;

            st_tra.setString(3, tramite);
            st_utr.setString(3, tramite);
            rs_tra = st_tra.executeQuery();
            if (rs_tra.next()) {
                tipoInicio = rs_tra.getString("TRA_UTR");
            }

            if (tipoInicio.equals("0")) {
                m_Log.debug("Trámite con unidad de inicio de tipo 0 (OTRAS)");
                rs_utr = st_utr.executeQuery();
                if (rs_utr.next()) {
                    unidades = Integer.parseInt(rs_utr.getString("UORS_INICIO"));
                }
                if (unidades == 1) {
                    sql_utr = "SELECT TRA_UTR_COD FROM E_TRA_UTR WHERE TRA_PRO=? AND TRA_MUN=? AND TRA_COD=?";
                    m_Log.debug(sql_utr);
                    st_utr = con.prepareStatement(sql_utr);
                    st_utr.setString(1, procedimiento);
                    st_utr.setString(2, municipio);
                    st_utr.setString(3, tramite);
                    rs_utr = st_utr.executeQuery();
                    if (rs_utr.next()) {
                        resultado = rs_utr.getString("TRA_UTR_COD");
                    }
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (BDException ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs_tra.close();
                if (rs_utr != null) {
                    rs_utr.close();
                }
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return resultado;

    }

public SalidaBoolean grabarCampoGenerico(String municipio, String ejercicio, String numeroExpediente, 
        String tramite, String ocurrencia, String procedimiento, String codigo, String campo, String org)
            throws java.rmi.RemoteException {

        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
        String gestorD = m_ConfigTechnical.getString("gestor");
        String driverD = m_ConfigTechnical.getString(org + "/driver");
        String urlD = "";
        String usuarioD = m_ConfigTechnical.getString(org + "/usuarioFormularios");
        String passwordD = m_ConfigTechnical.getString(org + "/passFormularios");
        String fichLogD = m_ConfigTechnical.getString(org + "/fichlog");
        String jndi = m_ConfigTechnical.getString(org + "/dataSource");

        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndi};

        SalidaBoolean salida = new SalidaBoolean(new String[0], Boolean.TRUE);
        try {

            grabarCampoGenerico(municipio, ejercicio, numeroExpediente, tramite,
                    ocurrencia, procedimiento, codigo, campo, params);

        } catch (Exception e) {
            salida.setResultado(Boolean.FALSE);
            salida.setIncidencias(new String[]{e.getMessage()});
        }
        return salida;

    }

    private void grabarCampoGenerico(String municipio, String ejercicio, String numeroExpediente, String tramite,
            String ocurrencia, String procedimiento, String codigo, String valor, String[] params)
            throws TechnicalException, SQLException, IOException {

        Statement stm = null;
        String sql = null;
        int res = 0;
        AdaptadorSQLBD bd=null;
        Connection con=null;

        try {
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            ExpedienteDAO e = new ExpedienteDAO();

            //EXPEDIENTE
            Vector listaCSExpediente = e.cargaEstructuraDatosSuplementarios1(municipio, procedimiento, bd, con);
            //if (!valor.equals("") && valor!=null) {
                EstructuraCampo campo = obtenerCampo(listaCSExpediente, codigo);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Grabando campo expediente" + codigo);
                }
                if (campo != null) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("..." + campo.getCodCampo());
                    }
                    String tipo = "0";
                    if ((campo.getCodTipoDato() != null) && (!"".equals(campo.getCodTipoDato()))) {
                        tipo = campo.getCodTipoDato();
                    }
                    String mascara = null;
                    m_Log.debug("campo.getMascara=" + campo.getMascara());
                    if ((campo.getMascara() != null) && (!"".equals(campo.getMascara()))) {
                        mascara = campo.getMascara();
                    }
                    grabarCampoExpediente(municipio, ejercicio, numeroExpediente, campo.getCodCampo(), valor, tipo, mascara, procedimiento, con);
                }
            //}

            //TRAMITE
            //obtener los campos suplementarios asociados al tramite
            Vector lista = e.cargaEstructuraDatosSuplementarios2(municipio, procedimiento, numeroExpediente, tramite, bd, con);
            //if (!valor.equals("") && valor!=null) {
               
                EstructuraCampo campoT = obtenerCampo(lista, codigo);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Grabando campo tramite " + codigo);
                }
                if (campoT != null) {
                    /* el tipo y mascara del campo se toma de la definicion de campo suplementario,
                    no vienen del formulario */
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("... " + campoT.getCodCampo());
                    }
                    String tipo = "0";
                    if ((campoT.getCodTipoDato() != null) && (!"".equals(campoT.getCodTipoDato()))) {
                        tipo = campoT.getCodTipoDato();
                    }

                    String mascara = null;
                    m_Log.debug("campo.getMascara=" + campoT.getMascara());
                    if ((campoT.getMascara() != null) && (!"".equals(campoT.getMascara()))) {
                        mascara = campoT.getMascara();
                    }

                    grabarCampo(municipio, ejercicio, numeroExpediente, ocurrencia, procedimiento,
                            campoT.getCodCampo(), valor, tipo, mascara, tramite, con);
                }
            //}
        } catch (Exception tec) {
            tec.printStackTrace();
            throw new TechnicalException(tec.getMessage());
        } finally {
            try {
                bd.devolverConexion(con);
            } catch (BDException ex) {
                Logger.getLogger(WSTramitacionImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    
    private Vector comprobarCondicionesEntrada(TramitacionExpedientesValueObject teVO, String[] params, String codTramAFinalizar)
    {
        Vector listaFinal = new Vector();
        Connection connection = null;
        try{
            AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
            connection = abd.getConnection();
            // AHORA SE COMPRUEBAN LAS CONDICIONES DE ENTRADA DE TIPO TRÁMITE
            Vector listaCondicionesEntradaTramite = TramitacionExpedientesDAO.getInstance().getListaCondicionesEntrada(abd,connection,teVO);
            m_Log.debug("Numero de condiciones de entrada de tipo estado de tramite: " +
                            listaCondicionesEntradaTramite.size());
            Vector listaCondicionesEntradaNoCumplidas = new Vector();

            if (listaCondicionesEntradaTramite.size() > 0) {
                    listaCondicionesEntradaNoCumplidas = TramitacionExpedientesDAO.getInstance().comprobarCondicionesEntrada(connection, teVO, listaCondicionesEntradaTramite, codTramAFinalizar);
            }

            m_Log.debug("Numero de condiciones de entrada de tipo estado de tramite no cumplidas: " +
                            listaCondicionesEntradaNoCumplidas.size());

            // AHORA SE COMPRUEBAN LAS CONDICIONES DE ENTRADA DE TIPO EXPRESIÓN
            Vector listaCondicionesExpresionTramites = TramitacionExpedientesDAO.getInstance().getListaCondicionesEntradaExpresion(abd,connection,teVO);
            m_Log.debug("Numero de condiciones de entrada de tipo expresión del trámite: " + listaCondicionesExpresionTramites.size());

           

            Vector tramitesnoOK = new Vector();
             // Si hay alguna condición de entrada de tipo expresión.
            if (listaCondicionesExpresionTramites.size() > 0) {
                try {
                    m_Log.debug("$$$$$$$$ El trámite " + teVO.getCodTramite() + " tiene " + listaCondicionesExpresionTramites.size() + " condiciones de entrada");
                    tramitesnoOK = TramitacionExpedientesDAO.getInstance().comprobarCondicionesEntradaExpresion(teVO.getEstructuraDatosSuplExpediente(), 	
                            teVO.getValoresDatosSuplExpediente(), teVO.getEstructuraDatosSuplTramites(), teVO.getValoresDatosSuplTramites(), 
                            listaCondicionesExpresionTramites);

                    m_Log.debug("$$$$$$$$ El trámite " + teVO.getCodTramite() + " tiene tramites con condiciones de entrada no validos "  + tramitesnoOK.size());
                } catch (Exception e) {
                    throw e;
                }
            }
            
            // AHORA SE COMPRUEBAN LAS CONDICIONES DE ENTRADA DE TIPO FIRMA DE DOCUMENTO
            Vector listaCondicionesEntradaFirmaDoc = TramitacionExpedientesDAO.getInstance().getListaCondicionesEntradaDocumento(teVO,connection);
            m_Log.debug("Numero de condiciones de entrada de tipo firma de documento: " +
                            listaCondicionesEntradaFirmaDoc.size());
            Vector listaCondicionesEntradaFirmaDocNoCumplidas = new Vector();

            if (listaCondicionesEntradaFirmaDoc.size() > 0) {
                    listaCondicionesEntradaFirmaDocNoCumplidas = TramitacionExpedientesDAO.getInstance().comprobarCondicionesEntradaFirmaDoc(connection, teVO, listaCondicionesEntradaFirmaDoc);
            }

            m_Log.debug("Numero de condiciones de entrada de tipo estado de tramite no cumplidas: " +
                            listaCondicionesEntradaNoCumplidas.size());
            // Se guardan las condiciones de entrada de trámite y las de expresión en una misma lista
            for(int i=0;i<listaCondicionesEntradaNoCumplidas.size();i++){
                m_Log.debug("$$$$$$$$ TramitacionExpedientesAction guardando en errores la lista de CondicioensEntradaNoCumplidas $$$$$$$$$$");
                listaFinal.add((GeneralValueObject)listaCondicionesEntradaNoCumplidas.get(i));
            }

            for(int i=0;i<tramitesnoOK.size();i++){
                listaFinal.add((GeneralValueObject)tramitesnoOK.get(i));
            }

            
            for(int i=0;i<listaCondicionesEntradaFirmaDocNoCumplidas.size();i++){
                listaFinal.add((GeneralValueObject)listaCondicionesEntradaFirmaDocNoCumplidas.get(i));
            }
            
            m_Log.debug("********************* comprobarCondicionesEntrada listafinal: " + listaFinal.size());     
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(connection!=null)
                    connection.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return listaFinal;
    }
    
    
}
