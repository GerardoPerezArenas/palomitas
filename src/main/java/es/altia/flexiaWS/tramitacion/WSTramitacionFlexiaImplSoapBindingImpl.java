/**
 * WSTramitacionFlexiaImplSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL by the Apache Axis 1.4 Apr 22, 2006
 * (06:55:48 PDT) WSDL2Java emitter.
 */
package es.altia.flexiaWS.tramitacion;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.manual.UsuarioDAO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.TramitacionExpedientesManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.sge.SiguienteTramiteTO;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitesExpedientesManager;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.technical.EstructuraNotificacion;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.flexiaWS.tramitacion.bd.acceso.ExpedienteDAO;
import es.altia.flexiaWS.tramitacion.bd.datos.SalidaBoolean;
import es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO;
import es.altia.flexiaWS.tramitacion.bd.datos.TramiteVO;
import es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO;
import es.altia.flexiaWS.tramitacion.bd.datos.CondicionFinalizacionVO;
import es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO;
import es.altia.flexiaWS.tramitacion.bd.datos.InteresadoVO;
import es.altia.flexiaWS.tramitacion.bd.util.*;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.flexiaWS.tramitacion.bd.util.XMLTraductor;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import es.altia.util.cache.CacheDatosFactoria;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class WSTramitacionFlexiaImplSoapBindingImpl {
    
    Log m_Log = LogFactory.getLog(es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingImpl.class);

    /**
     * Devuelve los tramites iniciados en el expediente.
     *
     * @param expediente
     * @param jndi Acceso a la BD
     * @return
     * @throws java.rmi.RemoteException
     */
    //TODO igualar con finalizarTramiteResolucion agora cuando solucionado retroceder tramite con formularios
    public SalidaBoolean finalizarTramiteOperacion(ExpedienteVO idExpediente, TramiteVO idTramite, CondicionFinalizacionVO condFinalizacionVO, String origenLlamada, InfoConexionVO infoConexion)
            throws java.rmi.RemoteException {
        
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("Finalizar Tramite en servicio web interno de flexia");
        }
        
        
        AdaptadorSQLBD oad = null;
        Connection con = null;
        SalidaBoolean salida = new SalidaBoolean();
        ExpedienteDAO expedienteDAO = ExpedienteDAO.getInstance();
        String org = infoConexion.getOrganizacion();
        String apli = infoConexion.getAplicacion();
        
        Vector mns = new Vector();
        
        String aplicaciones = Configuracion.getAplicacion();        
        String entidad = Configuracion.getEntidad(org);
        
        idExpediente.setMunicipio(org);
        idExpediente.setOrganizacionUsuario(org);
        idExpediente.setEntidadUsuario(entidad);


        /*
         *  String usuario, String municipio, String procedimiento, String ejercicio,
         String expediente, String tramite, String ocurrencia, String org
         */

        //TODO: COMPROBAR LO MISMO QUE EN EL WS, YA QUE EN ESTE METODO SE DA POR SUPUESTO PERMISOS, EXISTENCIA DE EXPEDIENTE, ETC.
        boolean aplicacionTrue = false;
        
        try {
            
            
            aplicacionTrue = comprobarAplicacion(aplicaciones, apli);
            m_Log.debug("-->aplicacionTrue " + aplicacionTrue);
            if (!aplicacionTrue) {
                
                salida.setIncidencias("La aplicación no tiene permisos para acceder a la conexión");
                salida.setCodigoError(-1);
                mns.add("La aplicación no tiene permisos para acceder a la conexión");
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
            
            mns.add("No se pudo obtener una conexion a BD");
        }
        
        if (aplicacionTrue) {
            
            try {
                // Inicio de la operacion de alta de tercero y domicilio
                // Iniciamos transaccion
                String[] params = Configuracion.getParamsBD(org);
                oad = new AdaptadorSQLBD(params);
                con = oad.getConnection();

                // Se comprueba si el expediente es válido
                boolean expValido = expedienteDAO.esExpedienteValido(idExpediente, con);
                if (!expValido) {
                    
                    m_Log.info("Traza de control (No error)  expediente No valido");
                    mns.add("Expediente no Válido");
                    salida.setIncidencias("Expediente no valido o finalizado");
                    salida.setCodigoError(1);
                } else {
                    m_Log.info("Traza de control (No error)  expediente Válido");
                    
                    int codOrganizacion = Integer.parseInt(org);
                    int ocurrenciaTramiteCerrada = expedienteDAO.estaOcurrenciaTramiteCerrada(idExpediente, idTramite, codOrganizacion, con);
                    
                    if (ocurrenciaTramiteCerrada == -1) {
                        // Error, se intenta finalizar un trámite que no existe
                        //res.setStatus(12);
                        salida.setCodigoError(2);
                        mns.add("El trámite que se intenta finalizar no existe");
                        m_Log.info("Traza de control (No error) no existe trámite");
                        salida.setIncidencias("No existe el trámite");                        
                    }// if
                    else {
                        m_Log.info("Traza de control (No error) existe ocurrencia de trámite");
                        
                        if (ocurrenciaTramiteCerrada == 0) {
                            //res.setStatus(3);
                            salida.setCodigoError(3);
                            mns.add("No se puede finalizar el trámite porque ya está finalizado");
                            m_Log.info("Traza de control (No error)  el trámite ya está finalizado");
                            salida.setIncidencias("No se puede finalizar el trámite porque ya está finalizado");                            
                        } else {
                            m_Log.info("Traza de control (No error)el trámite está abierto");
                            boolean documentosPendientesFirma = expedienteDAO.tieneDocumentoSinFirma(idExpediente, idTramite, codOrganizacion, con);
                            if (documentosPendientesFirma) {
                                //res.setStatus(5);
                                salida.setCodigoError(4);
                                mns.add("No se puede finalizar el trámite porque tiene documentos pendientes de ser firmados.");
                                m_Log.info("Traza de control (No error) documento pendientes de firma");
                                salida.setIncidencias("No se puede finalizar el trámite porque tiene documentos pendientes de ser firmados.");
                            } else {
                                m_Log.info("Traza de control (No error) no hay documentos pendientes de firma");
                                boolean perteneceRelacion = expedienteDAO.perteneceRelacion(idExpediente, con);
                                if (perteneceRelacion) {
                                    //res.setStatus(6);
                                    salida.setCodigoError(5);
                                    mns.add("No se puede finalizar el trámite porque el expediente pertenece a una relación");
                                    m_Log.info("Traza de control (No error) el expediente pertence a una relación");
                                    salida.setIncidencias("Expediente Finalizado");
                                } else {
                                    TramitacionExpedientesValueObject teVO = new TramitacionExpedientesValueObject();
                                    teVO.setCodMunicipio(idExpediente.getMunicipio());
                                    teVO.setCodProcedimiento(idExpediente.getProcedimiento());
                                    teVO.setEjercicio(idExpediente.getEjercicio());
                                    teVO.setNumeroExpediente(idExpediente.getNumero());                                    
                                    teVO.setCodTramite(idTramite.getCodTramite());
                                    teVO.setOcurrenciaTramite(idTramite.getOcurrenciaTramite());                                    
                                    teVO.setCodUsuario(idTramite.getUsuario());
                                    teVO.setCodUnidadTramitadoraTram(idTramite.getUnidadTramitadoraTram());
                                    teVO.setCodOrganizacion(org);
                                    teVO.setCodMunicipio(org);
                                    teVO.setCodEntidad(entidad);                                    
                                    teVO.setNumero(idExpediente.getNumero());
                                     teVO.setOrigenLlamada(origenLlamada);
                                    
                                    
                                    
                                    
                                    
                                    
                                    if (m_Log.isDebugEnabled()) {
                                        m_Log.debug("Carga de datos");
                                    }
                                    
                                    teVO = TramitacionExpedientesManager.getInstance().cargarDatos(teVO, params);
                                    
                                    Vector DS = TramitacionExpedientesManager.getInstance().cargarDatosSuplementariosExpediente(teVO,params);	
                                    teVO.setEstructuraDatosSuplExpediente((Vector) DS.elementAt(0));
                                    teVO.setValoresDatosSuplExpediente((Vector) DS.elementAt(1));	
                                    teVO.setEstructuraDatosSuplTramites((Vector) DS.elementAt(2));	
                                    teVO.setValoresDatosSuplTramites((Vector) DS.elementAt(3));

                                    /**
                                     * **** INICIO: COMPROBACIÓN DE
                                     * FINALIZACIÓN DEL EXPEDIENTE ********
                                     */
                                    if ("F".equals(teVO.getAccion())) {
                                        // Si el trámite es el de finalización => Se comprueba primero si el expediente tiene más trámites pendientes
                                        // de finalizar

                                        //finalizar expediente
                                        Vector listaTramitesNoFinalizados = new Vector();
                                        listaTramitesNoFinalizados = TramitesExpedientesManager.getInstance().getTramitesExpedienteSinFinalizar(teVO, params);
                                        if (listaTramitesNoFinalizados.size() == 0) {
                                            int res = TramitacionExpedientesManager.getInstance().finalizarExpediente(teVO,  params);
                                            if (res > 0) {
                                                salida.setResultado(Boolean.TRUE);
                                                salida.setCodigoError(0);
                                                salida.setIncidencias("Expediente Finalizado");
                                                mns.add("Expediente Finalizado");
                                                m_Log.info("Traza de control (No error) Expediente Finalizado");
                                                salida.setIncidencias("Expediente Finalizado");
                                                
                                            } else {
                                                teVO.setRespOpcion("noGrabado");
                                                mns.add("Tramite no grabado");
                                                salida.setCodigoError(6);
                                                salida.setResultado(Boolean.FALSE);
                                                m_Log.info("Traza de control (No error) Tramite no finalizado");
                                                salida.setIncidencias("Tramite no finalizado");
                                            }
                                        } else {
                                            teVO.setRespOpcion("1");
                                            salida.setCodigoError(7);
                                            mns.add("No se puede finalizar el expediente, porque hay ocurrencias de trámites pendientes");
                                            salida.setResultado(Boolean.FALSE);
                                            salida.setIncidencias("No se puede finalizar el expediente, porque hay ocurrencias de trámites pendientes");
                                            m_Log.info("Traza de control (No error) No se puede finalizar el expediente, porque hay ocurrencias de trámites pendientes");
                                        }
                                        
                                        
                                        
                                    } else /**
                                     * **** FIN: COMPROBACIÓN DE FINALIZACIÓN
                                     * DEL EXPEDIENTE **********
                                     */
                                    {

                                        //Vector<SiguienteTramiteTO> tramitesParaIniciar = teVO.getListaTramitesFavorables(); /*Vector de SiguienteTramiteVO*/
                                        Vector<TramitacionExpedientesValueObject> auxiliar = new Vector<TramitacionExpedientesValueObject>();
                                        Vector<SiguienteTramiteTO> tramitesParaIniciar = tramitesParaAbrirCondSalida(condFinalizacionVO, teVO.getListaTramitesFavorables(), teVO.getListaTramitesNoFavorables());
                                        
                                        
                                        for (int f = 0; f < tramitesParaIniciar.size(); f++) {
                                            SiguienteTramiteTO siguienteTramite = tramitesParaIniciar.elementAt(f);
                                            siguienteTramite.setCodUnidadInicioTramite(idTramite.getUnidadTramitadoraTram());
                                            auxiliar.add(convertToTramitacionExpedientesValueObject(siguienteTramite, teVO.getCodMunicipio(),
                                                    teVO.getCodProcedimiento(), params));
                                        }
                                        teVO.setListaTramitesFavorables(auxiliar);

                                        
                                        
                                        teVO.setListaEMailsAlIniciar(new Vector());
                                        teVO.setListaEMailsAlFinalizar(new Vector());
                                        
                                        Vector listaTramitesIniciar = teVO.getListaTramitesFavorables();//Tramites que se deberian inciar por condiciones de salida
                                        Vector listaTramitesIniciarOriginal = null;

                                        if (m_Log.isDebugEnabled()) {
                                            m_Log.debug("el tamaño de la lista de trámites para iniciar es : " + listaTramitesIniciar.size());
                                        }

                                        /**
                                         * ************************* INICIO:
                                         * COMPROBACIÓN DE SI PARA LA LISTA DE
                                         * TRÁMITES, SE VERIFICAN SUS
                                         * CONDICIONES DE ENTRADA
                                         * ****************************
                                         */
                                        for (int i = 0; listaTramitesIniciar != null && i < listaTramitesIniciar.size(); i++) {
                                            ((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).setCodMunicipio(idExpediente.getMunicipio());
                                            ((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).setCodOrganizacion(idExpediente.getMunicipio());
                                            ((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).setCodProcedimiento(idExpediente.getProcedimiento());
                                            ((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).setEjercicio(idExpediente.getEjercicio());
                                            ((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).setNumeroExpediente(idExpediente.getNumero());
                                            ((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).setCodigoTramiteFlujoSalida(((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).getCodTramite());                                            
                                            ((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).setCodUsuario(idTramite.getUsuario());
                                            ((TramitacionExpedientesValueObject) listaTramitesIniciar.get(i)).setCodEntidad(entidad);
                                        }// for         

                                        Vector resultadoTramitesIniciar = new Vector();
                                        String msgError = "No se cumplen las condiciones de entrada para los trámites: ";
                                        for (int i = 0; listaTramitesIniciar != null && i < listaTramitesIniciar.size(); i++) {
                                            TramitacionExpedientesValueObject tramiteIniciar = (TramitacionExpedientesValueObject) listaTramitesIniciar.get(i);
                                            tramiteIniciar.setEstructuraDatosSuplExpediente((Vector) DS.elementAt(0));
                                            tramiteIniciar.setValoresDatosSuplExpediente((Vector) DS.elementAt(1));	
                                            tramiteIniciar.setEstructuraDatosSuplTramites((Vector) DS.elementAt(2));	
                                            tramiteIniciar.setValoresDatosSuplTramites((Vector) DS.elementAt(3));
                                            Vector error = this.comprobarCondicionesEntrada(tramiteIniciar, params, teVO.getCodTramite());
                                            if (error == null || error.size() == 0) {
                                                // el trámite siguiente verifica las condiciones de entrada, entonces continuar en la lista de trámites
                                                // a iniciar tras finalizar el actual
                                                resultadoTramitesIniciar.add(tramiteIniciar);
                                            } else {
                                                msgError += tramiteIniciar.getDescripcionTramiteFlujoSalida() + ",";
                                            }
                                        }
                                        
                                        if (resultadoTramitesIniciar.size() == 0 && 
                                                condFinalizacionVO.getTipoFinalizacion() != null && !condFinalizacionVO.getTipoFinalizacion().equals("S")) {
                                            mns.add("Tramite no finalizado. No se cumplen las condiciones de entrada");
                                            salida.setCodigoError(8);
                                            salida.setResultado(Boolean.FALSE);
                                            salida.setIncidencias("No se puede finalizar el tramite, porque no se abre ninguno al no cumplir las condiciones de salida o de entrada");
                                            m_Log.info("Traza de control (No error) Tramite no finalizado. No se cumplen las condiciones de entrada");
                                        } else {
                                            if(listaTramitesIniciar != null && resultadoTramitesIniciar != null && listaTramitesIniciar.size() > resultadoTramitesIniciar.size()) {
                                                mns.add(msgError);
                                            }
                                            // En listaTramitesIniciar sólo se quedan los trámites siguientes que verifican sus condiciones de entrada
                                            listaTramitesIniciar = resultadoTramitesIniciar;
                                            listaTramitesIniciarOriginal = teVO.getListaTramitesFavorables();
                                            /**
                                             * ************************* FIN:
                                             * COMPROBACIÓN DE SI PARA LA LISTA
                                             * DE TRÁMITES, SE VERIFICAN SUS
                                             * CONDICIONES DE ENTRADA
                                             * ******************************
                                             */
                                            teVO.setListaTramitesIniciar(listaTramitesIniciar);
                                            Vector listaTramitesNoIniciados = new Vector();
                                            
                                            try {
                                                listaTramitesNoIniciados = TramitacionExpedientesManager.getInstance().finalizarConTramites(teVO,params);
                                            } catch (EjecucionSWException e) {
                                                mns.add("Tramite no finalizado");
                                                salida.setCodigoError(8);
                                                throw new java.rmi.RemoteException(e.getMensaje(), e);
                                                
                                            }
                                            
                                            Vector codTramitesNoIniciados = new Vector();
                                            if (listaTramitesNoIniciados == null) {
                                                teVO.setRespOpcion("noGrabado");
                                                mns.add("Tramite no grabado");
                                                salida.setCodigoError(8);
                                                salida.setResultado(Boolean.FALSE);
                                                m_Log.info("Traza de control (No error) Tramite no finalizado");
                                                
                                            } else if (listaTramitesNoIniciados != null && listaTramitesNoIniciados.size() == 1 && listaTramitesNoIniciados.get(0).equals(-1)) {
                                                teVO.setRespOpcion("noGrabado");
                                                mns.add("Tramite no finalizado. El usuario no tiene permisos sobre el trámite.");
                                                salida.setCodigoError(8);
                                                salida.setResultado(Boolean.FALSE);
                                                m_Log.info("Traza de control (No error) Tramite no finalizado. El usuario no tiene permisos sobre el trámite.");
                                                
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
                                                    m_Log.info("Traza de control (No error) Tramites pendientes de finalizar:: " + tram);
                                                } else {
                                                    mns.add("Tramite finalizado correctamente");
                                                }
                                                salida.setCodigoError(0);
                                                
                                                
                                            }
                                            
                                            //Insertamos las operaciones en OPERACIONES_EXPEDIENTE
                                            if (teVO.getRespOpcion() != null && teVO.getRespOpcion().equals("tramiteFinalizado")) {
                                                teVO.setNombreUsuario(UsuarioDAO.getInstance().getNombreUsuario(params, Integer.parseInt(teVO.getCodUsuario())));
                                                OperacionesExpedienteManager.getInstance().registrarFinalizarTramite(teVO, null, params);
                                                if (listaTramitesIniciarOriginal != null) {
                                                    this.m_Log.debug(String.format("Tamano de la lista de tramites inicialmente: %d", listaTramitesIniciarOriginal.size())); 
                                                }
                                                if (listaTramitesNoIniciados != null) {
                                                    this.m_Log.debug(String.format("Tamano de la lista de tramites no iniciados: %d", listaTramitesNoIniciados.size())); 
                                                }
                                                if (!listaTramitesIniciar.isEmpty() && listaTramitesIniciarOriginal != null && listaTramitesNoIniciados != null && 
                                                        (listaTramitesNoIniciados.isEmpty() || listaTramitesNoIniciados.size() < listaTramitesIniciarOriginal.size())) {
                                                    GeneralValueObject paramsRegistroOperacion = new GeneralValueObject();
                                                    paramsRegistroOperacion.setAtributo("codMunicipio", teVO.getCodMunicipio());
                                                    paramsRegistroOperacion.setAtributo("codProcedimiento", teVO.getCodProcedimiento());
                                                    paramsRegistroOperacion.setAtributo("ejercicio", teVO.getEjercicio());
                                                    paramsRegistroOperacion.setAtributo("numero", teVO.getNumeroExpediente());
                                                    paramsRegistroOperacion.setAtributo("usuario", teVO.getCodUsuario());
                                                    paramsRegistroOperacion.setAtributo("codOrganizacion", teVO.getCodOrganizacion());
                                                    paramsRegistroOperacion.setAtributo("codEntidad", teVO.getCodEntidad());
                                                    paramsRegistroOperacion.setAtributo("nombreUsuario", teVO.getNombreUsuario());
                                                    Vector tramites = FichaExpedienteManager.getInstance().cargaTramites(paramsRegistroOperacion, params);
                                                    OperacionesExpedienteManager.getInstance().previoRegistrarIniciarTramitePrepararDatos(teVO, listaTramitesIniciar, listaTramitesNoIniciados,
                                                            tramites, paramsRegistroOperacion, params, false);
                                                }
                                            }

                                            //Creo un GVO para almacenar datos necesarios posteriomente en notificaciones FINALIZAR
                                            GeneralValueObject objectNotificar = new GeneralValueObject();
                                            objectNotificar.setAtributo("codOrganizacion", org);
                                            objectNotificar.setAtributo("codMunicipio", idExpediente.getMunicipio());
                                            objectNotificar.setAtributo("codProcedimiento", idExpediente.getProcedimiento());
                                            objectNotificar.setAtributo("codTramite", idTramite.getCodTramite());
                                            objectNotificar.setAtributo("usuario", "5");
                                            objectNotificar.setAtributo("ocurrencia", idTramite.getOcurrenciaTramite());
                                            objectNotificar.setAtributo("expediente", idTramite.getNumeroExpediente());
                                            objectNotificar.setAtributo("ejercicio", idTramite.getEjercicio());
                                            notificarFinalTramite(params, objectNotificar);
                                            notificarInicioTramite(params, objectNotificar, listaTramitesNoIniciados, listaTramitesIniciar);
                                            
                                            String incidencia = "";
                                            
                                            for (int i = 0; i < mns.size(); i++) {
                                                incidencia += (String) mns.get(i) + "; ";
                                            }
                                            
                                            salida.setIncidencias(incidencia);
                                            
                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
                }
                
                
                
            } catch (Exception e) {
                e.printStackTrace();
                m_Log.error(e.getMessage());
                
            } finally {
                try {
                    oad.devolverConexion(con);
                } catch (BDException be) {
                    be.printStackTrace();
                }
            }
        }
        
        m_Log.warn("INCIDENCIAS " + salida.getIncidencias());
        return salida;
        
        
    }
    
    public RespuestasTramitacionVO iniciarExpedienteOperacion(ExpedienteVO expVO, InfoConexionVO infoConexion)
            throws java.rmi.RemoteException {
        
        
        SalidaBoolean salida = new SalidaBoolean();
        RespuestasTramitacionVO respuesta = new RespuestasTramitacionVO();
        int seguir;
        AdaptadorSQLBD bd = null;
        Connection con = null;
        Vector mns = new Vector();
        String org = infoConexion.getOrganizacion();
        String apli = infoConexion.getAplicacion();
        ExpedienteDAO expedienteDAO = ExpedienteDAO.getInstance();
        TramiteVO traVO = new TramiteVO();
        HashMap<String, String> datos = new HashMap<String, String>();
        
        String aplicaciones = Configuracion.getAplicacion();
        String entidad = Configuracion.getEntidad(org);
        expVO.setMunicipio(org);
        expVO.setOrganizacionUsuario(org);
        expVO.setEntidadUsuario(entidad);
        
        respuesta.setExpediente(null);
        respuesta.setTramite(null);
        
        try {
            String[] params = Configuracion.getParamsBD(org);
            bd = new AdaptadorSQLBD(params);
            con = bd.getConnection();
            UsuarioValueObject user = new UsuarioValueObject();
            user.setParamsCon(params);
            user.setIdUsuario(Integer.parseInt(expVO.getUsuario()));
            user.setOrgCod(Integer.parseInt(org));
            
          
            
            boolean aplicacionTrue = comprobarAplicacion(aplicaciones, apli);
            m_Log.debug("-->aplicacionTrue " + aplicacionTrue);
            if (!aplicacionTrue) {
                
                salida.setIncidencias("La aplicación no tiene permisos para acceder a la conexión");
                salida.setCodigoError(10);
                mns.add("La aplicación no tiene permisos para acceder a la conexión");
                m_Log.debug("La aplicación no tiene permisos para acceder a la conexión");
                respuesta.setSalida(salida);
                return (respuesta);
            } else {
                
                String uorinicio = expVO.getUor();
                int expedienteValido = expedienteDAO.comprobarInicioExpediente(Integer.parseInt(org), expVO, con,params);
                
                
                
                if (expedienteValido == 1) {
                    salida.setCodigoError(3);
                    salida.setIncidencias("El procedimiento no es válido");
                    m_Log.debug("El procedimiento no es válido");
                    bd.rollBack(con);
                    respuesta.setSalida(salida);
                    return (respuesta);
                } else if (expedienteValido == 2 || expedienteValido == 3) {
                    m_Log.debug("La unidad organizativa no existe");
                    salida.setCodigoError(4);
                    salida.setIncidencias("La unidad organizativa no existe");
                    bd.rollBack(con);
                    respuesta.setSalida(salida);
                    return (respuesta);
                } else if (expedienteValido == 4) {
                    m_Log.debug("La unidad del trámite de inicio no existe o no es válida");
                    salida.setCodigoError(4);
                    salida.setIncidencias("La unidad del trámite de inicio no existe o no es válida");
                    bd.rollBack(con);
                    respuesta.setSalida(salida);
                    return (respuesta);
                } else {
                    
                    
                   GeneralValueObject gVO = new GeneralValueObject();
                    
                    //Añadir el interesado
                    
                   
                   InteresadoVO[] interesados = expVO.getTercero();
                   
                   
                   
                   if (interesados != null) {
                        for (int x = 0; x < interesados.length; x++) {
                          InteresadoVO interesadoExpedienteVO = interesados[x];
                          boolean continuar = true;
                          int rolInteresado=interesados[0].getRol();
                          m_Log.debug("-->Rol de interesdo: " + rolInteresado);
                            // Obtenemos el rol por defecto del procedimiento y el rol que trae el interesado en el xml
                            int codRolPorDefecto = expedienteDAO.getCodRolPorDefecto(expVO.getProcedimiento(), con);
                            m_Log.debug("-->codRolPorDefecto: " + codRolPorDefecto);
                            if (rolInteresado != 0) {
                                //Obtenemos todos los códigos de roles del procedimiento
                                ArrayList<Integer> rolesProc = expedienteDAO.getRolesProcedimiento(org, expVO.getProcedimiento(), con);
                                if (!rolesProc.contains(rolInteresado)) {
                                    if (m_Log.isDebugEnabled()) {
                                        m_Log.debug("Los datos de entrada son incorrectos");
                                    }

                                    salida.setCodigoError(5);
                                    salida.setIncidencias("El rol indicado no está entre los roles del procedimiento al que pertenece el expediente");
                                    bd.rollBack(con);
                                    respuesta.setSalida(salida);
                                    continuar=false;
                                    return (respuesta);

                                }
                            } else {
                                //Si en el xml no se espcifica el rol, al ser un int al impl le llega un 0, se toma como rol el rol por defecto
                                rolInteresado = codRolPorDefecto;
                            }                          
                          
                          
                          //Parte de alta del tercero y domicilio
                                                                                      
                            TercerosValueObject terVO=new TercerosValueObject();
                            
                            terVO.setApellido1(interesadoExpedienteVO.getAp1());
                            terVO.setApellido2(interesadoExpedienteVO.getAp2());
                            terVO.setNombre(interesadoExpedienteVO.getNombre());
                            terVO.setDocumento(interesadoExpedienteVO.getDoc());
                            terVO.setTipoDocumento(interesadoExpedienteVO.getTipoDoc());
                            terVO.setEmail(interesadoExpedienteVO.getEmail());
                            terVO.setTelefono(interesadoExpedienteVO.getTelefono());
                            terVO.setUsuarioAlta("5");
                            
                            DomicilioSimpleValueObject domVO=new DomicilioSimpleValueObject();
                            
                            domVO.setIdProvincia(Integer.toString(interesadoExpedienteVO.getDomicilio().getCodProvincia()));
                            domVO.setIdMunicipio(Integer.toString(interesadoExpedienteVO.getDomicilio().getCodMunicipio()));
                            domVO.setDescVia(interesadoExpedienteVO.getDomicilio().getNombreVia());        
                            domVO.setNumDesde(Integer.toString(interesadoExpedienteVO.getDomicilio().getPrimerNumero()));
                            domVO.setNumHasta(Integer.toString(interesadoExpedienteVO.getDomicilio().getUltimoNumero()));
                            domVO.setLetraDesde(interesadoExpedienteVO.getDomicilio().getPrimeraLetra());
                            domVO.setLetraHasta(interesadoExpedienteVO.getDomicilio().getUltimaLetra());
                            domVO.setBloque(interesadoExpedienteVO.getDomicilio().getBloque());        
                            domVO.setEscalera(interesadoExpedienteVO.getDomicilio().getEscalera());  
                            domVO.setPlanta(interesadoExpedienteVO.getDomicilio().getPlanta());  
                            domVO.setPortal(interesadoExpedienteVO.getDomicilio().getPortal());  
                            domVO.setPuerta(interesadoExpedienteVO.getDomicilio().getPuerta());  
                            domVO.setCodigoPostal(interesadoExpedienteVO.getDomicilio().getCodPostal());  
                            
                            Vector<DomicilioSimpleValueObject> dom = new Vector<DomicilioSimpleValueObject>();
                           
                            dom.add(domVO);                           
                            terVO.setDomicilios(dom);
                            
                            
                           
                            
                            int codTercero=0;
                            int verTercero=0;
                            int codDomicilio=0;
                            try{
                                
                                terVO=TercerosManager.getInstance().setTercero(org,terVO,"4", null, null, params);
                                 
                                codTercero = Integer.parseInt(terVO.getIdentificador());
                                verTercero = Integer.parseInt(terVO.getVersion());
                                codDomicilio = Integer.parseInt(terVO.getIdDomicilio());
                                
                                m_Log.debug("codTercero  "+codTercero);  
                                m_Log.debug("verTercero  "+verTercero);
                                m_Log.debug("codDomicilio  "+codDomicilio);
                            } catch (Exception e) { 
                                salida.setCodigoError(6);
                                salida.setIncidencias("No se ha podido dar de alta el interesado");
                                bd.rollBack(con);
                                respuesta.setSalida(salida);
                                continuar = false;
                                return (respuesta);
                            }
                            
                            //El alta de interesado ya se hace en el método de iniciar expediente
                            gVO.setAtributo("codTercero", terVO.getIdentificador());
                            gVO.setAtributo("version", terVO.getVersion());
                            gVO.setAtributo("codDomicilio", terVO.getIdDomicilio());
                            gVO.setAtributo("codRol", Integer.toString(rolInteresado));
                            
                           
                        }
                    }

                    //logica de creación de expediente y trámite de inicio. Campos y demas info (excepto interesados)
                
                    //Hacemos el expediente
                    int res = 0;
                   
                    gVO.setAtributo("codMunicipio", expVO.getMunicipio());
                    gVO.setAtributo("codProcedimiento", expVO.getProcedimiento());
                    gVO.setAtributo("ejercicio", expVO.getEjercicio());
                    gVO.setAtributo("usuario", expVO.getUsuario());
                    gVO.setAtributo("codOrganizacion", expVO.getOrganizacionUsuario());
                    gVO.setAtributo("codEntidad", expVO.getEntidadUsuario());
                    //gVO.setAtributo("codUOR", expVO.getUor());
                    String unidadProcedimiento = determinarUnidadExpediente(
                            expVO.getMunicipio(), expVO.getProcedimiento(), params);
                    if (unidadProcedimiento.equals("") || unidadProcedimiento == null) {
                        gVO.setAtributo("codUOR", expVO.getUor());
                    } else {
                        gVO.setAtributo("codUOR", unidadProcedimiento);
                    }
                    
                    
                    
                    String unidadTramiteInicio = determinarUnidadTramite(
                            expVO.getMunicipio(), expVO.getProcedimiento(), params);
                    if (unidadTramiteInicio.equals("") || unidadTramiteInicio == null) {
                        gVO.setAtributo("unidadTramiteInicioSeleccionada", expVO.getUorTramiteInicio());
                    } else {
                        gVO.setAtributo("unidadTramiteInicioSeleccionada", unidadTramiteInicio);
                    }
                    
                    gVO.setAtributo("codUnidadTramitadoraUsu", expVO.getUor());
                    
                    gVO.setAtributo("asunto", expVO.getAsunto());
                    if (!"".equals((expVO.getObservaciones()))) {
                        gVO.setAtributo("observaciones", expVO.getObservaciones());
                    }
//                    	                
                    TramitacionValueObject tvo = new TramitacionValueObject();
                    tvo.setCodProcedimiento(expVO.getProcedimiento());
                    TramitacionManager.getInstance().getNuevoExpediente(user, tvo, params);
                    
                    m_Log.debug("NUMERO DE EXPEDIENTE----- " + tvo.getNumero());
                    gVO.setAtributo("numero", tvo.getNumero());
                    
                    gVO.setAtributo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO, ConstantesDatos.ORIGEN_LLAMADA_WEB_SERVICE);
                    
                    res = FichaExpedienteManager.getInstance().iniciarExpediente(gVO, params);
                    
                    if (res > 0) {
                        
                        
                        traVO.setCamposFormularios(expVO.getCamposFormularios());
                        traVO.setMunicipio(expVO.getMunicipio());
                        traVO.setEjercicio(expVO.getEjercicio());
                        traVO.setNumeroExpediente(tvo.getNumero());
                        traVO.setCodTramite((String) gVO.getAtributo("codTramite"));
                        traVO.setOcurrenciaTramite("1");
                        traVO.setProcedimiento(expVO.getProcedimiento());                        
                        expVO.setNumero(tvo.getNumero());
                       
                        
                    }
                    
                    

                    String campos = expVO.getCampos();
                    if (campos != null) {
                        datos = XMLTraductor.traduccionXmlToHashMap(campos);
                        m_Log.debug("-->Campos hashmap  ");
                        java.util.Iterator it = datos.keySet().iterator();
                        while (it.hasNext()) {
                            String cod = (String) it.next();
                            String valor = (String) datos.get(cod);
                            String tit = null;
                            String tip = null;
                            
                            if (valor != null && !valor.equals("")) {
                                StringTokenizer tokens = new StringTokenizer(valor, "|");
                                int numTokes = tokens.countTokens();
                                if (numTokes == 1) {
                                    valor = valor;
                                } else {//numTokes=3
                                    String[] dato = new String[numTokes];
                                    int i = 0;
                                    while (tokens.hasMoreTokens()) {
                                        String str = tokens.nextToken();
                                        dato[i] = str;
                                        i++;
                                    }
                                    tit = dato[0];
                                    tip = dato[1];
                                    valor = dato[2];
                                }
                                m_Log.debug("\n-->cod  " + cod);
                                m_Log.debug("\n-->tit  " + tit);
                                m_Log.debug("\n-->tip  " + tip);
                                expedienteDAO.grabarDatoSuplementario(expVO, cod, valor, tit, tip, con);
                                m_Log.debug("\n Inserción de datos suplementarios Expediente correcto.");
                            }
                        }
                        
                    } else {
                        m_Log.debug("No hay campos suplementarios para el expediente");
                    }
                    
                    
                    
                  
                    salida.setCodigoError(0);
                    salida.setIncidencias("Expediente iniciado correctamente");
                    respuesta.setSalida(salida);
                    
                    
                    expVO.setNumero(tvo.getNumeroSimple()); //Para la vuelta necesita el número en formato 000001
                    respuesta.setTramite(traVO);
                    respuesta.setExpediente(expVO);
                    
                    
                }
//                        
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Hacer rollback de la transaccion
                bd.rollBack(con);
            } catch (Exception ex) {
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Fallo al hacer rollback");
                }
                ex.printStackTrace();
            }
            
            salida.setCodigoError(-1);
            salida.setResultado(Boolean.FALSE);
            respuesta.setSalida(salida);
            return respuesta;
        } finally {
            try {
                m_Log.debug("Finalizamos método");
                bd.devolverConexion(con);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        
        return respuesta;
        
    }

    /**
     * Convierte un objeto SiguienteTramiteTO en un objeto
     * TramitacionExpedientesValueObject
     *
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
        
        String uorTramite = sig.getCodUnidadInicioTramite();
        if (uorTramite == null || Integer.parseInt(uorTramite) < 0) {
            uorTramite = determinarUnidadTramite(municipio, procedimiento, tvo.getCodTramite(), params);
        }
        tvo.setCodUnidadTramitadoraTram(uorTramite);
        
        return tvo;
    }
    
    private Vector buscaTramitesIniciados(Vector listaTramitesNoIniciados, Vector listaTramitesIniciar) {
        String[] codigosNoIniciar = new String[listaTramitesNoIniciados.size()];
        for (int y = 0; y < listaTramitesNoIniciados.size(); y++) {
            Object tramiteNoIniciciado = listaTramitesNoIniciados.get(y);
            if(tramiteNoIniciciado instanceof GeneralValueObject) {
                codigosNoIniciar[y] = (String) ((GeneralValueObject) listaTramitesNoIniciados.get(y)).getAtributo("codTramite");
            }
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
    
    private Vector<SiguienteTramiteTO> tramitesParaAbrirCondSalida(CondicionFinalizacionVO condFinalizacionVO, Vector<SiguienteTramiteTO> listaFavorables, Vector<SiguienteTramiteTO> listaDesfavorables) {
        
        String tipo = condFinalizacionVO.getTipoFinalizacion() + ""; //Viene del Ws externo

        String respuesta = condFinalizacionVO.getRespuesta();//si o no. Esto viene del Ws externo
        
        Vector<SiguienteTramiteTO> listaDevolver = new Vector();        
        
        
        if (("T".equals(tipo)) || (("R".equals(tipo)) && ("si".equals(respuesta))) || (("P".equals(tipo)) && ("si".equals(respuesta)))) {
            int tamano = (condFinalizacionVO.getFlujoSI() != null && condFinalizacionVO.getFlujoSI().getListaRespuesta() != null)?
                    condFinalizacionVO.getFlujoSI().getListaRespuesta().length:0;
            TramiteVO[] idTra = new TramiteVO[tamano];
            for (int x = 0; x < tamano; x++) {
                idTra[x] = condFinalizacionVO.getFlujoSI().getListaRespuesta()[x];
                String codTramite = idTra[x].getCodTramite();
                for (int i = 0; i < listaFavorables.size(); i++) {
                    if (listaFavorables.get(i).getCodigoTramiteFlujoSalida().equals(codTramite)) {
                        listaDevolver.add(listaFavorables.get(i));                        
                    }
                }
            }
            return listaDevolver;
        } else if ((("R".equals(tipo)) && ("no".equals(respuesta))) || (("P".equals(tipo)) && ("no".equals(respuesta)))) {
            int tamano = (condFinalizacionVO.getFlujoNO() != null && condFinalizacionVO.getFlujoNO().getListaRespuesta() != null)?
                    condFinalizacionVO.getFlujoNO().getListaRespuesta().length:0;
            TramiteVO[] idTra = new TramiteVO[tamano];
            
            for (int x = 0; x < tamano; x++) {
                idTra[x] = condFinalizacionVO.getFlujoNO().getListaRespuesta()[x];
                String codTramite = idTra[x].getCodTramite();
                for (int i = 0; i < listaDesfavorables.size(); i++) {
                    if (listaDesfavorables.get(i).getCodigoTramiteFlujoSalida().equals(codTramite)) {
                        listaDevolver.add(listaDesfavorables.get(i));                        
                    }
                }
            }
            return listaDevolver;
            
        } else if ("S".equals(tipo)) {
            Vector<SiguienteTramiteTO> litsaDevolver = new Vector<SiguienteTramiteTO>(0);
            return litsaDevolver;
        } else {
            return null;
        }
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
            String uitf = "";
            String uief = "";
            String uor_mail = "";
            String uor_usu = "";
            String usu_mail = "";
            String int_mail = "";
            String usu_mail_tramite;
            String usu_mail_exped;
            Vector mailsUOR = new Vector();
            Vector mailsUsusUOR = new Vector();
            Vector mailsInteresados = new Vector();
            String mailUsuInicioTramite = "";
            String mailUsuInicioExpediente = "";
            Vector usuarios = new Vector();
            String uor = "";
            int procedimientoRestringido = 0;
            
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
            
            sql = "SELECT TRA_UTF,TRA_USF,TRA_INF, TRA_NOTIF_UITF, TRA_NOTIF_UIEF, pro_restringido FROM e_tra,e_pro WHERE TRA_MUN=" + codMunicipio
                    + " AND TRA_PRO=" + oad.addString(codProcedimiento)
                    + " AND TRA_COD=" + codTramite + " AND tra_pro=pro_cod and tra_mun=pro_mun";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                utf = rs.getString("TRA_UTF");
                usf = rs.getString("TRA_USF");
                inf = rs.getString("TRA_INF");
                uitf = rs.getString("TRA_NOTIF_UITF");
                uief = rs.getString("TRA_NOTIF_UIEF");
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
                
                if (uitf.equals("S")) {
                    //coger el mail usuario inicio tramite
                    sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + " A_USU.USU_COD= E_CRO.CRO_USU) "
                            + " WHERE E_CRO.CRO_NUM = ? "
                            + " AND E_CRO.CRO_TRA = ?";

                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));
                    st.setString(2, codTramite);

                    m_Log.debug("Consulta Mail Usuario Inicio Tramite" + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_tramite = rs.getString("USU_EMAIL");

                        if (!(usu_mail_tramite == null) && !(usu_mail_tramite.equals(""))) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("USUARIO MAIL: " + usu_mail_tramite);
                            }

                            mailUsuInicioTramite = usu_mail_tramite;
                        }
                    }

                    rs.close();
                    st.close();
                }
                if (uief.equals("S")) {
                    //coger el mail de usuario inicio expediente 
                    sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + " INNER JOIN E_EXP ON (A_USU.USU_COD= E_EXP.EXP_USU) "
                            + " WHERE E_EXP.EXP_NUM = ?";

                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));

                    m_Log.debug("Consulta Mail Usuario Inicio Expediente: " + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_exped = rs.getString("USU_EMAIL");

                        if (!(usu_mail_exped == null) && !(usu_mail_exped.equals(""))) {

                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("USUARIO MAIL: " + usu_mail_exped);
                            }

                            mailUsuInicioExpediente = usu_mail_exped;
                        }
                    }

                    rs.close();
                    st.close();
                }
                
                eNotif.setListaEMailsUOR(mailsUOR);
                eNotif.setListaEMailsUsusUOR(mailsUsusUOR);
                eNotif.setListaEMailsInteresados(mailsInteresados);
                eNotif.setListaEmailsUsuInicioTramite(mailUsuInicioTramite);
                eNotif.setListaEmailsUsuInicioExped(mailUsuInicioExpediente);
                
                Vector datos = new Vector();
                datos.add(eNotif);
                tEVO.setListaEMailsAlFinalizar(datos);
                tEVO.setTramite(codTramite);
                tEVO.setUnidadTramitadora(uor);
                tEVO.setNumeroExpediente(expediente);
                tEVO.setListaEMailsAlIniciar(new Vector());
                tEVO.setCodMunicipio(codMunicipio);
                tEVO.setCodProcedimiento(codProcedimiento);
                tEVO.setProcedimiento(obtenerNombreProcedimiento(params, tEVO));
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
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            } catch (TechnicalException te) {
                te.printStackTrace();
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
            Logger.getLogger(es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            } catch (TechnicalException te) {
                te.printStackTrace();
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
            Logger.getLogger(es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
             try {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            } catch (TechnicalException te) {
                te.printStackTrace();
            }
        }
        return "";
    }
    
    private boolean notificarFinalTramite(String[] params, GeneralValueObject objectNotificar) {
        try {
            return notificar(getMailsUsuariosAlFinalizar(params, objectNotificar), obtenerAsuntoExpediente(params, objectNotificar));
        } catch (SQLException ex) {
            Logger.getLogger(es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
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
            String uiti = "";
            String uiei = "";
            String uor_mail = "";
            String uor_usu = "";
            String usu_mail = "";
            String int_mail = "";
            String usu_mail_tramite;
            String usu_mail_exped;
            Vector mailsUOR = new Vector();
            Vector mailsUsusUOR = new Vector();
            Vector mailsInteresados = new Vector();
            String mailUsuInicioTramite ="";
            String mailUsuInicioExpediente = "";
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
            sql = "SELECT TRA_UTI,TRA_USI,TRA_INI, TRA_NOTIF_UITI, TRA_NOTIF_UIEI, pro_restringido "
                    + " FROM e_tra,e_pro WHERE TRA_MUN=" + codMunicipio + " AND TRA_PRO=" + oad.addString(codProcedimiento)
                    + " AND TRA_COD=" + codTramite + " AND tra_pro=pro_cod and tra_mun=pro_mun";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                uti = rs.getString("TRA_UTI");
                usi = rs.getString("TRA_USI");
                ini = rs.getString("TRA_INI");
                uiti = rs.getString("TRA_NOTIF_UITI");
                uiei = rs.getString("TRA_NOTIF_UIEI");
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
                
                if (uiti.equals("S")) {
                    //coger el mail E_CRO
                    sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + " A_USU.USU_COD= E_CRO.CRO_USU ) "
                            + " WHERE E_CRO.CRO_NUM = ? "
                            + " AND E_CRO.CRO_TRA = ? ";

                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));
                    st.setString(2, codTramite);

                    m_Log.debug("Consulta Mail Usuario Inicio Tramite" + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_tramite = rs.getString("USU_EMAIL");

                        if (!(usu_mail_tramite == null) && !(usu_mail_tramite.equals(""))) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("USUARIO MAIL: " + usu_mail_tramite);
                            }

                            mailUsuInicioTramite = usu_mail_tramite;
                        }
                    }

                    rs.close();
                    st.close();

                }
                if (uiei.equals("S")) {
                    //coger el mail de usuario inicio expediente 
                    sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu "
                            + " INNER JOIN E_EXP ON (A_USU.USU_COD= E_EXP.EXP_USU) "
                            + " WHERE E_EXP.EXP_NUM = ?";

                    st = con.prepareStatement(sql);
                    st.setString(1, (String) gVO.getAtributo("numero"));

                    m_Log.debug("Consulta Mail Usuario Inicio Expediente: " + sql);

                    rs = st.executeQuery();

                    while (rs.next()) {
                        usu_mail_exped = rs.getString("USU_EMAIL");

                        if (!(usu_mail_exped == null) && !(usu_mail_exped.equals(""))) {
                            if (m_Log.isDebugEnabled()) {
                                m_Log.debug("USUARIO MAIL: " + usu_mail_exped);
                            }

                            mailUsuInicioExpediente = usu_mail_exped;
                        }
                    }

                    rs.close();
                    st.close();

                }
                
                eNotif.setListaEMailsUOR(mailsUOR);
                eNotif.setListaEMailsUsusUOR(mailsUsusUOR);
                eNotif.setListaEMailsInteresados(mailsInteresados);
                eNotif.setListaEmailsUsuInicioTramite(mailUsuInicioTramite);
                eNotif.setListaEmailsUsuInicioExped(mailUsuInicioExpediente);
                
                Vector datos = new Vector();
                datos.add(eNotif);
                tEVO.setListaEMailsAlIniciar(datos);
                tEVO.setTramite(codTramite);
                tEVO.setUnidadTramitadoraTramiteIniciado(codUORTramiteIniciado);
                tEVO.setNumeroExpediente(expediente);
                tEVO.setListaEMailsAlFinalizar(new Vector());
                tEVO.setCodMunicipio(codMunicipio);
                tEVO.setCodProcedimiento(codProcedimiento);
                tEVO.setProcedimiento(obtenerNombreProcedimiento(params, tEVO));
                
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
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(st);
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            } catch (TechnicalException te) {
                te.printStackTrace();
            }
        }
        return tEVO;
    }
    
    public boolean notificar(TramitacionExpedientesValueObject tramExpVO, String asuntoExp) {
        boolean resultado = true;
        
        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("WSFlexia");
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
            String emailUsuarioIniciaTramite = null;
            String emailUsuarioIniciaExped;
            
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
                emailUsuarioIniciaTramite = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEmailsUsuInicioTramite();
                emailUsuarioIniciaExped = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEmailsUsuInicioExped();
                
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

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("emailUsuarioInicioTramite" + emailUsuarioIniciaTramite);
                }

                if (emailUsuarioIniciaTramite != null && !emailUsuarioIniciaTramite.equals("")) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteUsuarioInicio");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@", ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());

                    mailHelper.sendMail((String) emailUsuarioIniciaTramite, asunto, contenido);

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO ENVIADO A " + emailUsuarioIniciaTramite);
                    }

                }

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("emailUsuarioInicioExpediente" + emailUsuarioIniciaExped);
                }

                if (emailUsuarioIniciaTramite != null && !emailUsuarioIniciaExped.equals("")) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteUsuarioInicioExped");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@", ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());

                    mailHelper.sendMail((String) emailUsuarioIniciaExped, asunto, contenido);

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO ENVIADO A " + emailUsuarioIniciaExped);
                    }
                }
            }
            
            for (int i = 0; i < listaEMailsAlIniciar.size(); i++) {
                emailsUOR = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getListaEMailsUOR();
                emailsUsusUOR = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getListaEMailsUsusUOR();
                emailsInteresados = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getListaEMailsInteresados();
                emailUsuarioIniciaTramite = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getListaEmailsUsuInicioTramite();
                emailUsuarioIniciaExped = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getListaEmailsUsuInicioExped();
                
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

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("emailUsuarioInicioTramite" + emailUsuarioIniciaTramite);
                }

                if (emailUsuarioIniciaTramite != null && !emailUsuarioIniciaTramite.equals("")) {
                    contenido = m_ConfigApplication.getString("mail.contentInicioTramiteUsuarioInicio");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@", ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());

                    mailHelper.sendMail((String) emailUsuarioIniciaTramite, asunto, contenido);

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO ENVIADO A " + emailUsuarioIniciaTramite);
                    }

                }

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("emailUsuarioInicioExpediente" + emailUsuarioIniciaExped);
                }

                if (emailUsuarioIniciaTramite != null && !emailUsuarioIniciaExped.equals("")) {
                    contenido = m_ConfigApplication.getString("mail.contentInicioTramiteUsuarioInicioExped");
                    contenido = contenido.replaceAll("@procedimiento@", tramExpVO.getProcedimiento());
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@", ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getNombreTramite());

                    mailHelper.sendMail((String) emailUsuarioIniciaExped, asunto, contenido);

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO ENVIADO A " + emailUsuarioIniciaExped);
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
        } else if ("6".equals(tipo)) {
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
                if (rs_tra != null) {
                    rs_tra.close();
                }
                
                if (rs_utr != null) {
                    rs_utr.close();
                }
                if (st_tra != null) {
                    st_tra.close();
                }
                if (st_utr != null) {
                    st_utr.close();
                }
                
                if (con!=null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return resultado;
        
    }
    
    private String determinarUnidadTramite(String municipio, String procedimiento,
            String[] params) {
        
        PreparedStatement st_tra = null, st_utr = null;
        ResultSet rs_tra = null, rs_utr = null;
        AdaptadorSQL oad = null;
        Connection con = null;
        String tipoInicio = "";
        String codTramiteInicio = "";
        String resultado = "";
        String sql_tra = " SELECT PRO_TRI, TRA_UTR FROM E_PRO JOIN E_TRA ON (PRO_MUN = TRA_MUN AND PRO_COD = TRA_PRO AND PRO_TRI = TRA_COD) " + " WHERE PRO_COD = ? AND  PRO_MUN = ?";
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
            
            tipoInicio = "";
            int unidades = 0;
            
            rs_tra = st_tra.executeQuery();
            if (rs_tra.next()) {
                tipoInicio = rs_tra.getString("TRA_UTR");
                codTramiteInicio = rs_tra.getString("PRO_TRI");
            }
            
            if (tipoInicio.equals("0")) {
                m_Log.debug("Trámite con unidad de inicio de tipo 0 (OTRAS)");
                st_utr.setString(3, codTramiteInicio);
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
                    st_utr.setString(3, codTramiteInicio);
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
        } finally {try {
                if (rs_tra != null) {
                    rs_tra.close();
                }
                
                if (rs_utr != null) {
                    rs_utr.close();
                }
                if (st_tra != null) {
                    st_tra.close();
                }
                if (st_utr != null) {
                    st_utr.close();
                }
                
               if (con!=null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return resultado;
        
    }
    
    private String determinarUnidadExpediente(String municipio, String procedimiento,
            String[] params) {
        
        PreparedStatement st_tra = null;
        ResultSet rs_tra = null, rs_utr = null;
        AdaptadorSQL oad = null;
        Connection con = null;
        String uors = "";
        String resultado = "";
        String uorInicia = "";
        String sql_tra = " SELECT count(*) as uors FROM E_Pui WHERE Pui_pro = ? AND  Pui_MUN = ?";
        
        
        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            st_tra = con.prepareStatement(sql_tra);
            st_tra.setString(1, procedimiento);
            st_tra.setString(2, municipio);
            m_Log.debug(sql_tra);
            
            rs_tra = st_tra.executeQuery();
            if (rs_tra.next()) {
                uors = rs_tra.getString("uors");
            }
            
            if (uors.equals("1")) {
                sql_tra = "SELECT pui_cod FROM E_Pui WHERE Pui_pro = ? AND  Pui_MUN = ?";
                st_tra = con.prepareStatement(sql_tra);
                st_tra.setString(1, procedimiento);
                st_tra.setString(2, municipio);
                rs_tra = st_tra.executeQuery();
                if (rs_tra.next()) {
                    uorInicia = rs_tra.getString("PUI_COD");
                }
            }
            return uorInicia;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (BDException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs_tra != null) {
                    rs_tra.close();
                }
                
                if (rs_utr != null) {
                    rs_utr.close();
                }
                if (st_tra != null) {
                    st_tra.close();
                }
              
                if (con!=null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return resultado;
        
    }
    
    private Vector comprobarCondicionesEntrada(TramitacionExpedientesValueObject teVO, String[] params, String codTramAFinalizar) {
        Vector listaFinal = new Vector();
        Connection connection = null;
        try {
            AdaptadorSQLBD abd = new AdaptadorSQLBD(params);
            connection = abd.getConnection();
            // AHORA SE COMPRUEBAN LAS CONDICIONES DE ENTRADA DE TIPO TRÁMITE
            Vector listaCondicionesEntradaTramite = TramitacionExpedientesDAO.getInstance().getListaCondicionesEntrada(abd, connection, teVO);
            m_Log.debug("Numero de condiciones de entrada de tipo estado de tramite: "
                    + listaCondicionesEntradaTramite.size());
            Vector listaCondicionesEntradaNoCumplidas = new Vector();
            
            if (listaCondicionesEntradaTramite.size() > 0) {
                listaCondicionesEntradaNoCumplidas = TramitacionExpedientesDAO.getInstance().comprobarCondicionesEntrada(connection, teVO, listaCondicionesEntradaTramite, codTramAFinalizar);
            }
            
            m_Log.debug("Numero de condiciones de entrada de tipo estado de tramite no cumplidas: "
                    + listaCondicionesEntradaNoCumplidas.size());

            // AHORA SE COMPRUEBAN LAS CONDICIONES DE ENTRADA DE TIPO EXPRESIÓN
            Vector listaCondicionesExpresionTramites = TramitacionExpedientesDAO.getInstance().getListaCondicionesEntradaExpresion(abd, connection, teVO);
            m_Log.debug("Numero de condiciones de entrada de tipo expresión del trámite: " + listaCondicionesExpresionTramites.size());
            
            
            
            Vector tramitesnoOK = new Vector();
            // Si hay alguna condición de entrada de tipo expresión.
            if (listaCondicionesExpresionTramites.size() > 0) {
                try {
                    m_Log.debug("$$$$$$$$ El trámite " + teVO.getCodTramite() + " tiene " + listaCondicionesExpresionTramites.size() + " condiciones de entrada");
                     tramitesnoOK = TramitacionExpedientesDAO.getInstance().comprobarCondicionesEntradaExpresion(teVO.getEstructuraDatosSuplExpediente(), 	
                            teVO.getValoresDatosSuplExpediente(), teVO.getEstructuraDatosSuplTramites(), teVO.getValoresDatosSuplTramites(), 
                            listaCondicionesExpresionTramites);
                    
                    m_Log.debug("$$$$$$$$ El trámite " + teVO.getCodTramite() + " tiene tramites con condiciones de entrada no validos " + tramitesnoOK.size());
                } catch (Exception e) {
                    throw e;
                }
            }

            // AHORA SE COMPRUEBAN LAS CONDICIONES DE ENTRADA DE TIPO FIRMA DE DOCUMENTO
            Vector listaCondicionesEntradaFirmaDoc = TramitacionExpedientesDAO.getInstance().getListaCondicionesEntradaDocumento(teVO, connection);
            m_Log.debug("Numero de condiciones de entrada de tipo firma de documento: "
                    + listaCondicionesEntradaFirmaDoc.size());
            Vector listaCondicionesEntradaFirmaDocNoCumplidas = new Vector();
            
            if (listaCondicionesEntradaFirmaDoc.size() > 0) {
                listaCondicionesEntradaFirmaDocNoCumplidas = TramitacionExpedientesDAO.getInstance().comprobarCondicionesEntradaFirmaDoc(connection, teVO, listaCondicionesEntradaFirmaDoc);
            }
            
            m_Log.debug("Numero de condiciones de entrada de tipo estado de tramite no cumplidas: "
                    + listaCondicionesEntradaNoCumplidas.size());
            // Se guardan las condiciones de entrada de trámite y las de expresión en una misma lista
            for (int i = 0; i < listaCondicionesEntradaNoCumplidas.size(); i++) {
                m_Log.debug("$$$$$$$$ TramitacionExpedientesAction guardando en errores la lista de CondicioensEntradaNoCumplidas $$$$$$$$$$");
                listaFinal.add((GeneralValueObject) listaCondicionesEntradaNoCumplidas.get(i));
            }
            
            for (int i = 0; i < tramitesnoOK.size(); i++) {
                listaFinal.add((GeneralValueObject) tramitesnoOK.get(i));
            }
            
            
            for (int i = 0; i < listaCondicionesEntradaFirmaDocNoCumplidas.size(); i++) {
                listaFinal.add((GeneralValueObject) listaCondicionesEntradaFirmaDocNoCumplidas.get(i));
            }
            
            m_Log.debug("********************* comprobarCondicionesEntrada listafinal: " + listaFinal.size());            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaFinal;
    }

    /**
     * Comprueba si la applicacion pasada exite en el propeties Devuelve true si
     * existe
     */
    private boolean comprobarAplicacion(String aplicaciones, String apli) {
        
        Vector<String> palabras = new Vector<String>();
        StringTokenizer tokenizer = new StringTokenizer(aplicaciones, ";");
        while (tokenizer.hasMoreTokens()) {
            palabras.add(tokenizer.nextToken());
        }
        return palabras.contains(apli);
        
    }
    
   
}

