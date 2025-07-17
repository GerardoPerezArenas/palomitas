package es.altia.agora.interfaces.user.web.sge;

import com.google.gson.Gson;
import es.altia.agora.business.administracion.mantenimiento.persistence.UORsManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.UsuariosGruposManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.administracion.persistence.GestionManager;
import es.altia.agora.business.documentos.DocumentoManager;
import es.altia.agora.business.documentos.helper.CodigoSeguroVerificacionHelper;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.ConsultaExpedientesValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.*;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.terceros.ParametrosTerceroValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.*;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.integracionsw.exception.FaltaDatoObligatorioException;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.DefinicionTramitesValueObject;
import es.altia.agora.business.sge.InteresadoExpedienteVO;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.util.ElementoListaValueObject;
import static es.altia.agora.interfaces.user.web.sge.FichaExpedienteAction.registroConf;
import es.altia.agora.interfaces.user.web.terceros.BusquedaTercerosForm;
import es.altia.agora.interfaces.user.web.util.ActionSession;
import es.altia.agora.interfaces.user.web.util.ResultadoAjax;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.technical.EstructuraNotificacion;
import es.altia.agora.technical.Fecha;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.arboles.impl.ArbolImpl;
import es.altia.catalogoformularios.model.solicitudes.vo.DefinicionTramiteKey;
import es.altia.catalogoformularios.model.solicitudes.vo.FormularioTramitadoVO;
import es.altia.catalogoformularios.model.solicitudes.vo.TramiteTramitadoKey;
import es.altia.catalogoformularios.model.solicitudesfacade.FormularioFacade;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;

import es.altia.flexia.expedientes.anulacion.exception.VerificacionFinNoConvencionalExpedienteException;
import es.altia.flexia.expedientes.anulacion.exception.VerificacionFinNoConvencionalInstanceException;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionModuloException;
import es.altia.flexia.integracion.moduloexterno.plugin.exception.EjecucionOperacionModuloIntegracionException;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.ModuloIntegracionExternoManager;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TareasPendientesInicioTramiteVO;
import es.altia.util.conexion.AdaptadorSQLBD;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import es.altia.flexia.notificacion.plugin.FactoriaPluginNotificacion;
import es.altia.flexia.notificacion.plugin.PluginNotificacion;
import es.altia.flexia.notificacion.vo.*;
import es.altia.flexia.tramitacion.externa.plugin.TramitacionExternaBase;
import es.altia.flexia.tramitacion.externa.plugin.TramitacionExternaCargador;
import java.lang.String;
import es.altia.flexia.expedientes.anulacion.plugin.VerificacionFinNoConvencionalExpediente;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.BDException;
import es.altia.util.documentos.DocumentOperations;
import es.altia.util.evalua_cadena;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;

public final class TramitacionExpedientesAction extends ActionSession {

    String codPais = "";
    String codProvincia = "";
    String codMunicipio = "";
    private String ALMOHADILLA = "#";

    public ActionForward performSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        m_Log.info("==================== TramitacionExpedientesAction ====================>");
//        ActionHelper myActionHelper = new ActionHelper(getLocale(request), getResources(request));

        // Validaremos los parametros del request especificados
        HttpSession session = request.getSession();
        String opcion = "";

        if ((session.getAttribute("usuario") != null)) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String[] params = usuario.getParamsCon();

            ParametrosTerceroValueObject paramsTercero = (ParametrosTerceroValueObject) session.getAttribute("parametrosTercero");
            if(paramsTercero != null){
                codPais = paramsTercero.getPais();
                codProvincia = paramsTercero.getProvincia();
                codMunicipio = paramsTercero.getMunicipio();
            } else {
                codPais = "108"; //Código España
                codProvincia = "99"; //codigo_provincia_desconocido
                codMunicipio = "999"; //codigo_municipio_desconocido
            }

            int idUsuario = usuario.getIdUsuario();
            String nombreUsuario = usuario.getNombreUsu();

            m_Log.debug("El idUsuario es------------------------->" + idUsuario);
            m_Log.debug("El nombre del usuario es:------------------------->" + nombreUsuario);
            // Si usuario en sesion es nulo --> error.


            TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
            tramExpVO.setOrigenLlamada(ConstantesDatos.ORIGEN_LLAMADA_INTERFAZ_WEB);
            TramitacionExpedientesForm tramExpForm = null;

            if (form == null) {
                m_Log.debug("Rellenamos el form de Tramitacion de Expedientes");
                form = new TramitacionExpedientesForm();
                if ("request".equals(mapping.getScope())) {
                    request.setAttribute(mapping.getAttribute(), form);
                } else {
                    session.setAttribute(mapping.getAttribute(), form);
                }
            }

            tramExpForm = (TramitacionExpedientesForm) form;

            opcion = request.getParameter("opcion");
            if (m_Log.isInfoEnabled()) {
                m_Log.info("la opcion en el action es " + opcion);
            }

            FichaExpedienteForm fichaExpForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("VECTOR DE INTERESADOS EN EL ACTION ----------------> "
                        + tramExpForm.getVectorCodInteresados());
            }

            if (opcion.equals("inicio") || opcion.equals("inicioAccesoExterno")) {
                String numeroExpediente = request.getParameter("numExpediente");
                String procedimiento = request.getParameter("procedimiento");
                String codMunicipio = request.getParameter("codMunicipio");
                String codProcedimiento = request.getParameter("codProcedimiento");
                String ejercicio = request.getParameter("ejercicio");
                String numero = request.getParameter("numero");
                String codTramite = request.getParameter("codTramite");
                String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
                String titular = request.getParameter("titular");
                String codUnidadOrganicaExp = request.getParameter("codUnidadOrganicaExp");
                String codUnidadTramitadoraTram = request.getParameter("codUnidadTramitadoraTram");
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Estamos en tramitacionExpedientesAction. opcion inicio. Codigo unidad Tramitadora: " + codUnidadTramitadoraTram);
                }
                String desdeInformesGestion = request.getParameter("desdeInformesGestion");
                String todos = request.getParameter("todos");
                tramExpVO.setNumeroExpediente(numeroExpediente);
                tramExpVO.setProcedimiento(procedimiento);
                tramExpVO.setCodMunicipio(codMunicipio);
                tramExpVO.setCodProcedimiento(codProcedimiento);
                tramExpVO.setEjercicio(ejercicio);
                tramExpVO.setNumero(numero);
                tramExpVO.setCodTramite(codTramite);
                tramExpVO.setOcurrenciaTramite(ocurrenciaTramite);
                tramExpVO.setTitular(titular);
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setCodOrganizacion(Integer.toString(usuario.getOrgCod()));
                tramExpVO.setCodEntidad(Integer.toString(usuario.getEntCod()));
                tramExpVO.setCodUnidadOrganicaExp(codUnidadOrganicaExp);
                tramExpVO.setCodUnidadTramitadoraTram(codUnidadTramitadoraTram);
                tramExpVO.setDesdeInformesGestion(desdeInformesGestion);
                tramExpVO.setTodos(todos);

                session.removeAttribute("RelacionDomicilios");
                String modoConsulta = request.getParameter("modoConsulta");
                if (modoConsulta == null) {
                    modoConsulta = "no";
                }
                tramExpVO.setModoConsulta(modoConsulta);
                String expRelacionado = request.getParameter("expRelacionado");
                if (expRelacionado == null) {
                    expRelacionado = "no";
                } else {
                    String codMunExpIni = request.getParameter("codMunExpIni");
                    String ejercicioExpIni = request.getParameter("ejercicioExpIni");
                    String numeroExpIni = request.getParameter("numeroExpIni");
                    tramExpVO.setCodMunicipioIni(codMunExpIni);
                    tramExpVO.setEjercicioIni(ejercicioExpIni);
                    tramExpVO.setNumeroIni(numeroExpIni);
                }
                tramExpVO.setExpRelacionado(expRelacionado);
                tramExpVO.setDesdeJsp("si");


                boolean expedienteEnHistorico = fichaExpForm.isExpHistorico();
                if (!fichaExpForm.isExpHistorico()) {
                    tramExpVO = TramitacionExpedientesManager.getInstance().cargarDatos(tramExpVO, params);
                    tramExpVO.setExpHistorico(false);
                } else {
                    tramExpVO = TramitacionExpedientesManager.getInstance().cargarDatosHistorico(tramExpVO, params);
                    tramExpVO.setExpHistorico(true);
                }

                m_Log.debug("TramitacionExpedientesAction codUsuario finaliza tramite: " + tramExpVO.getNombreUsuario());


                GeneralValueObject g = new GeneralValueObject();
                g.setAtributo("codMunicipio", codMunicipio);
                g.setAtributo("codProcedimiento", codProcedimiento);
                g.setAtributo("numeroExpediente", numeroExpediente);
                g.setAtributo("numero", numeroExpediente);
                g.setAtributo("ejercicio", ejercicio);
                g.setAtributo("codRol", "");
                Vector listaInteresados = TramitacionExpedientesManager.getInstance().getListaInteresados(g, params);
                tramExpVO.setListaInteresados(listaInteresados);
                String hayInterAdmitenNotif = "0";
                for (int i = 0; i < listaInteresados.size(); i++) {
                    GeneralValueObject interesado = (GeneralValueObject) listaInteresados.get(i);
                    if ("1".equals(interesado.getAtributo("admiteNotif"))) {
                        tramExpForm.setHayInteresadosNotifAutorizada(true);
                        hayInterAdmitenNotif = "1";
                        break;
                    }
                }
                request.setAttribute("hayInteresadosAdmitenNotif", hayInterAdmitenNotif);
                /**
                 * *************** MODULOS DE INTEGRACION EXTERNOS *********************
                 */
                ArrayList<ModuloIntegracionExterno> modulosExternos = ModuloIntegracionExternoFactoria.getInstance().getImplClassModuloTramiteConPantallaTramitacion(usuario.getOrgCod(), codProcedimiento, Integer.parseInt(codTramite), false, usuario.getIdUsuario(), params);
                tramExpVO.setModulosExternos(modulosExternos);
                /**
                 * *************** MODULOS DE INTEGRACION EXTERNOS *********************
                 */
                /**
                 * *************** MODULOS DE INTEGRACION EXTERNOS *********************
                 */
                // SE COMPRUEBA SI HAY QUE LLAMAR A ALGUNA OPERACION JAVASCRIPT DE LAS PESTAÑAS DE TRÁMITE DE LOS MÓDULOS EXTERNOS QUE PUEDAN ESTAR
                // CONFIGURADAS PARA EL TRÁMITE ACTUAL. ESTO ES NECESARIO PORQUE EL CONTENIDO DE ALGUNA DE ESTAS PESTAÑAS PUEDE DEPENDER DE ALGUNO
                // DE LOS CAMBIOS QUE SE HAYAN HECHO EN EL TRÁMITE COMO EN EL VALOR DE ALGÚN CAMPO SUPLEMENTARIO
                ArrayList<String> opsJavascriptEntrada = ModuloIntegracionExternoFactoria.getInstance().getFuncionesJavascriptAccederPantallaTramitacion(usuario.getOrgCod(), Integer.parseInt(tramExpVO.getCodTramite()), tramExpVO.getCodProcedimiento());
                m_Log.debug("TRAMITACIONEXPEDIENTESACTION->OPSJAVASCRIPTENTRADA:" + opsJavascriptEntrada.toString());
                tramExpVO.setFuncionesJSModulosExternosAccederPantallaTramite(opsJavascriptEntrada);


                /**
                 * *************** MODULOS DE INTEGRACION EXTERNOS *********************
                 */
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Cargamos la estructura de datos suplementarios");
                }
                Vector estructuraDatosSuplementarios =
                        TramitacionExpedientesManager.getInstance().cargaEstructuraDatosSuplementarios(tramExpVO, params);
                tramExpVO.setEstructuraDatosSuplementarios(estructuraDatosSuplementarios);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Cargamos la estructura de agrupaciones de datos suplementarios");
                }
                Vector estructuraAgrupacionesDatosSuplementarios =
                        TramitacionExpedientesManager.getInstance().cargaEstructuraAgrupaciones(tramExpVO, params);
                tramExpVO.setListaAgrupaciones(estructuraAgrupacionesDatosSuplementarios);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Cargamos los valores de datos suplementarios");
                }
                Vector valoresDatosSuplementarios = new Vector();
                valoresDatosSuplementarios = TramitacionExpedientesManager.getInstance().cargaValoresDatosSuplementarios(tramExpVO, estructuraDatosSuplementarios, params);
                tramExpVO.setValoresDatosSuplementarios(valoresDatosSuplementarios);
//                m_Log.info("ESTRUCTURA VALORES ... "+valoresDatosSuplementarios);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Comprobamos si hay que cargar la vista de datos suplementarios");
                }
                Boolean cargarVista = false;
                cargarVista = TramitacionExpedientesManager.getInstance().cargarVista(tramExpVO, params);
                tramExpForm.setCargarVista(cargarVista);

                Hashtable<String, GeneralValueObject> datos = TramitacionExpedientesManager.getInstance().cargaNombresEstadosFicherosTramite(tramExpVO, estructuraDatosSuplementarios, expedienteEnHistorico, params);
                GeneralValueObject estadosFicheros = (GeneralValueObject) datos.get("estadosFicheros");
                GeneralValueObject nombresFicheros = (GeneralValueObject) datos.get("nombresFicheros");
                GeneralValueObject longitudesFicheros = (GeneralValueObject) datos.get("longitudesFicheros");

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Cargamos los nombres de los ficheros");
                }
                tramExpVO.setListaNombresFicheros(nombresFicheros);
                tramExpVO.setListaEstadoFicheros(estadosFicheros);
                tramExpVO.setListaLongitudFicheros(longitudesFicheros);

                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Cargamos los tipos de los ficheros");
                }
                GeneralValueObject tiposFicheros = new GeneralValueObject();
                tiposFicheros = TramitacionExpedientesManager.getInstance().cargaTiposFicheros(tramExpVO, estructuraDatosSuplementarios, expedienteEnHistorico, params);
                tramExpVO.setListaTiposFicheros(tiposFicheros);
                cargarFormsPDF(tramExpVO);

                // inicio COMPROBAR SI EXISTE BLOQUEO EN EL TRÁMITE DEL EXPEDIENTE
                String usu_bloqueo = tramExpVO.getBloqueo();
                if (!usu_bloqueo.equals("")) {
                    if (String.valueOf(usuario.getIdUsuario()).equals(usu_bloqueo)) {
                        tramExpVO.setBloqueo("1"); // Bloqueo por el usuario
                    } else {
                        tramExpVO.setBloqueo("2"); // Bloqueo por otro usuario
                        //tramExpVO.setModoConsulta("si");
                    }
                } else {
                    tramExpVO.setBloqueo("0"); // Sin bloqueo
                }

                // fin COMPROBAR SI EXISTE BLOQUEO EN EL TRÁMITE DEL EXPEDIENTE

                // Comprobacion de si el usuario tiene permisos para dar de
                // alta salidas desde tramitacion. Si tiene permiso, cargamos
                // la lista de uors de registro.
                boolean permisoSalidas = UsuariosGruposManager.getInstance().tienePermisoDirectiva(
                        ConstantesDatos.DIRECTIVA_SALIDAS_DESDE_TRAMITAR, usuario.getIdUsuario(), params);

                if (permisoSalidas) {
                    tramExpForm.setTienePermisoSalidas("SI");
                    tramExpForm.setUorsDeRegistro(UORsManager.getInstance().getListaSimpleUORsDeRegistroUsuario(params, usuario));
                } else {
                    tramExpForm.setTienePermisoSalidas(null);
                }


                /**
                 * ********** SE COMPRUEBA SI LA OCURRENCIA DEL TRÁMITE TIENE
                 * TAREAS PENDIENTES DE INICIO PENDIENTES DE EJECUCIÓN *********************
                 */
                tramExpForm.setTieneTareasPendientesInicio(false);
                ArrayList<TareasPendientesInicioTramiteVO> tareas = ModuloIntegracionExternoManager.getInstance().getTareasPendientesInicio(Integer.parseInt(codMunicipio), Integer.parseInt(codTramite), Integer.parseInt(ocurrenciaTramite), numeroExpediente,
                        usuario.getIdioma(), params);
                m_Log.debug("Se han recuperado  " + tareas.size() + " pendientes de inicio");
                if (tareas.size() > 0) {
                    tramExpForm.setTieneTareasPendientesInicio(true);
                }
                tramExpForm.setTareasPendientesInicioTramite(tareas);

                /**
                 * ********* FIN: COMPROBACIÓN DE SI LA OCURRENCIA DEL TRÁMITE
                 * TIENE TAREAS PENDIENTES DE INICIO PENDIENTES DE EJECUCIÓN ***************
                 */
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                
                
            } else if ("calculo_expresion".equals(opcion)) {
                //davidvim00
                GeneralValueObject gVO = new GeneralValueObject();
                String codMunicipio_exp = request.getParameter("codMunicipio");
                String codProcedimiento_exp = request.getParameter("codProcedimiento");
                String tramite_exp = request.getParameter("codTramite");
                String retorno = "";
                String retorno1 = "";
                String retorno2 = "";
                String retorno3 = "";

                gVO.setAtributo("desdeJsp", "si");
                gVO.setAtributo("codMunicipio", codMunicipio_exp);
                gVO.setAtributo("codProcedimiento", codProcedimiento_exp);


                Vector resultado = calcular_campos_expresion(params, fichaExpForm, session, request, gVO, tramite_exp);
                for (int i = 0; i < resultado.size(); i++) {
                    GeneralValueObject g = (GeneralValueObject) resultado.elementAt(i);
                    retorno1 = (String) g.getAtributo("cod_campo");
                    retorno2 = (String) g.getAtributo("valor");
                    retorno3 = (String) g.getAtributo("origen");
                    retorno = retorno + retorno1 + "|" + retorno2 + "|" + retorno3 + "#";
                }
                response.setContentType("text/xml");
                PrintWriter out = response.getWriter();
                out.println(retorno);
                out.flush();
                out.close();

            } else if ("recuperarExpresion".equals(opcion)) {
                //davidvim00
                GeneralValueObject gVO = new GeneralValueObject();

                gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
                gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
                gVO.setAtributo("campo", request.getParameter("campo"));
                gVO.setAtributo("tramite", request.getParameter("tramite"));


                String resultado = TramitacionExpedientesDAO.getInstance().recuperarExpresion(params, gVO);
                response.setContentType("text/xml");
                PrintWriter out = response.getWriter();
                out.println(resultado);
                out.flush();
                out.close();
            } else if ("verFechaVencimiento".equals(opcion)) {
                String fechaVencimiento = "";
                String fecha = request.getParameter("fecha");
                String plazo = request.getParameter("plazo");
                String periodoPlazo = request.getParameter("periodoPlazo");
                String rdoPeriodoPlazo = "";
                m_Log.debug("fecha:" + fecha);
                m_Log.debug("plazo:" + plazo);
                m_Log.debug("periodoPlazo:" + periodoPlazo);
                //Vamos a calcular a fecha de vencimiento

                String[] dataTemp = fecha.split("/");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(dataTemp[2]), Integer.parseInt(dataTemp[1]) - 1, Integer.parseInt(dataTemp[0]));
                if (periodoPlazo.equals("D")) {
                    c.add(Calendar.DAY_OF_YEAR, Integer.parseInt(plazo));
                    rdoPeriodoPlazo = plazo + " días";
                } else {
                    if (periodoPlazo.equals("M")) {
                        c.add(Calendar.MONTH, Integer.parseInt(plazo));
                        rdoPeriodoPlazo = plazo + " meses";
                    } else {
                        c.add(Calendar.YEAR, Integer.parseInt(plazo));
                        rdoPeriodoPlazo = plazo + " años";
                    }
                }
                fechaVencimiento = sdf.format(c.getTime());
                m_Log.debug("La fecha de vencimiento es:" + fechaVencimiento);
                request.setAttribute("fecha", fecha);
                request.setAttribute("fechaVencimiento", fechaVencimiento);
                request.setAttribute("plazo", plazo);
                request.setAttribute("periodoPlazo", periodoPlazo);
                request.setAttribute("rdoPeriodoPlazo", rdoPeriodoPlazo);
                opcion = "verFechaVencimiento";
            } else if ("recuperarExterno".equals(opcion)) {
                GeneralValueObject gVO = new GeneralValueObject();

                gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
                gVO.setAtributo("codProcedimiento", request.getParameter("codProcedimiento"));
                gVO.setAtributo("codTram", request.getParameter("tramite"));
                gVO.setAtributo("codOcu", request.getParameter("ocurrencia"));
                gVO.setAtributo("campo", request.getParameter("campo"));

                ArrayList<GeneralValueObject> resultados = new ArrayList<GeneralValueObject>();

                resultados = TramitacionExpedientesManager.getInstance().recuperarExternos(params, gVO);

                String codigos_externos = "";
                String datos_externos = "";
                String valores_externos = "";

                if (resultados != null) {
                    for (int i = 0; i < resultados.size(); i++) {
                        GeneralValueObject gVO1 = new GeneralValueObject();
                        gVO1 = resultados.get(i);
                        if ("".equals(codigos_externos)) {
                            codigos_externos = (String) gVO1.getAtributo("codigo");
                        } else {
                            codigos_externos = codigos_externos + "||" + (String) gVO1.getAtributo("codigo");
                        }

                    }



                    for (int i = 0; i < resultados.size(); i++) {
                        GeneralValueObject gVO1 = new GeneralValueObject();
                        gVO1 = resultados.get(i);

                        String valor = (String) gVO1.getAtributo("valor");
                        valor = valor.replaceAll("\r", "").replace("\n", "").replace("\r\n", "");

                        if ("".equals(datos_externos)) {
                            datos_externos = valor;
                        } else {
                            datos_externos = datos_externos + "||" + valor;
                        }
                    }
                    valores_externos = codigos_externos + "$$" + datos_externos;
                }


                request.setAttribute("datos_externos", valores_externos);

            } else if (opcion.equals("iniciarProcedimientoAsociado")) {
                // Se va a iniciar un expediente relacionado debido a la finalización
                // de un trámite.
                String procedimientoAsociado = request.getParameter("procedimientoAsociadoAIniciar");

                // Se cargan las opciones de inicio del procedimiento: unidades del expediente,
                // unidades de tramitacion del tramite de inicio.
                DefinicionProcedimientosValueObject dpVO =
                        DefinicionProcedimientosManager.getInstance().getOpcionesInicioProcedimiento(procedimientoAsociado, params, usuario);

                // Comprobamos si el usuario debe de seleccionar alguna opcion o
                // si ya vienen determinadas por la definicion del procedimiento.
                if (dpVO.getTablaUnidadInicio().size() == 1 && dpVO.getUorsTramiteInicio().size() <= 1) {
                    // El usuario no selecciona nada. Se inicia el procedimiento
                    // automaticamente.
                    ElementoListaValueObject unidadExpediente =
                            (ElementoListaValueObject) dpVO.getTablaUnidadInicio().get(0);
                    fichaExpForm.setCodUnidadOrganicaExp(unidadExpediente.getCodigo());

                    String unidadTramInicio = null;
                    if (dpVO.getUorsTramiteInicio().size() == 1) {
                        unidadTramInicio = dpVO.getUorsTramiteInicio().get(0).getUor_cod();
                    }
                    String expAsociado = this.iniciarExpedienteAsociado(procedimientoAsociado, usuario, fichaExpForm, params, unidadTramInicio);
                    tramExpVO.setExpAsociadoIniciado(expAsociado);
                    tramExpVO.setListaDocumentos(new Vector());
                    tramExpForm.setTramitacionExpedientes(tramExpVO);
                    tramExpForm.setRespOpcion("iniciadoProcAsociado");

                    // Ahora se comprueba el estado del expediente de origen
                    // para conocer la acción precisa de la redirección.
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", fichaExpForm.getCodMunicipio());
                    String ejercicioExpOrigen = fichaExpForm.getEjercicio();
                    String procedimientoExpOrigen = fichaExpForm.getCodProcedimiento();
                    String numeroExpOrigen = fichaExpForm.getNumero();
                    gVO.setAtributo("ejercicio", ejercicioExpOrigen);
                    gVO.setAtributo("codProcedimiento", procedimientoExpOrigen);
                    gVO.setAtributo("numero", numeroExpOrigen);

                    gVO = FichaExpedienteManager.getInstance().cargaExpediente(gVO, params);
                    if (gVO.getAtributo("fechaFinExpediente") == null || "".equals(gVO.getAtributo("fechaFinExpediente"))) {
                        tramExpForm.setResultadoFinalizar("expedientePendiente");
                    } else {
                        tramExpForm.setResultadoFinalizar("expedienteFinalizado");
                    }
                } else {
                    // Es necesario que el usuario seleccione algún tipo de informacion.
                    request.setAttribute("unidadesExpediente", dpVO.getTablaUnidadInicio());
                    request.setAttribute("unidadesTramite", dpVO.getUorsTramiteInicio());
                    tramExpForm.setRespOpcion("seleccionarInfoInicio");
                }
                return mapping.findForward("grabarTramite");
            } else if (opcion.equals("enviarInfoExpIniciado")) {
                // Recuperamos la información de inicio del expediente
                String uorInicioExp = request.getParameter("uorInicioExp");
                String uorInicioTram = request.getParameter("uorInicioTram");
                String procedimientoAsociado = request.getParameter("procedimientoAsociadoAIniciar");

                // Creamos el expediente asociado.
                fichaExpForm.setCodUnidadOrganicaExp(uorInicioExp);
                String expAsociado = this.iniciarExpedienteAsociado(procedimientoAsociado, usuario, fichaExpForm, params, uorInicioTram);

                tramExpVO.setExpAsociadoIniciado(expAsociado);
                tramExpVO.setListaDocumentos(new Vector());
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                tramExpForm.setRespOpcion("iniciadoProcAsociado");

                // Ahora se comprueba el estado del expediente de origen
                // para conocer la acción precisa de la redirección.
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codMunicipio", fichaExpForm.getCodMunicipio());
                String ejercicioExpOrigen = fichaExpForm.getEjercicio();
                String procedimientoExpOrigen = fichaExpForm.getCodProcedimiento();
                String numeroExpOrigen = fichaExpForm.getNumero();
                gVO.setAtributo("ejercicio", ejercicioExpOrigen);
                gVO.setAtributo("codProcedimiento", procedimientoExpOrigen);
                gVO.setAtributo("numero", numeroExpOrigen);

                gVO = FichaExpedienteManager.getInstance().cargaExpediente(gVO, params);
                if (gVO.getAtributo("fechaFinExpediente") == null || "".equals(gVO.getAtributo("fechaFinExpediente"))) {
                    tramExpForm.setResultadoFinalizar("expedientePendiente");
                } else {
                    tramExpForm.setResultadoFinalizar("expedienteFinalizado");
                }
                return mapping.findForward("grabarTramite");

            } else if (opcion.equals("inicioOculto")) {
                //sirve para cargar todas las variable del tramite y vuelve a la ficha expediente
                m_Log.debug("_________ entro en inicioOculto");
                String numeroExpediente = request.getParameter("numExpediente");
                String procedimiento = request.getParameter("procedimiento");
                String codMunicipio = request.getParameter("codMunicipio");
                String codProcedimiento = request.getParameter("codProcedimiento");
                String ejercicio = request.getParameter("ejercicio");
                String numero = request.getParameter("numero");
                String codTramite = request.getParameter("codTramite");
                String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
                String titular = request.getParameter("titular");
                String codUnidadOrganicaExp = request.getParameter("codUnidadOrganicaExp");
                String codUnidadTramitadoraTram = request.getParameter("codUnidadTramitadoraTram");
                String desdeInformesGestion = request.getParameter("desdeInformesGestion");
                String todos = request.getParameter("todos");
                tramExpVO.setNumeroExpediente(numeroExpediente);
                tramExpVO.setProcedimiento(procedimiento);
                tramExpVO.setCodMunicipio(codMunicipio);
                tramExpVO.setCodProcedimiento(codProcedimiento);
                tramExpVO.setEjercicio(ejercicio);
                tramExpVO.setNumero(numero);
                tramExpVO.setCodTramite(codTramite);
                tramExpVO.setOcurrenciaTramite(ocurrenciaTramite);
                tramExpVO.setTitular(titular);
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setCodOrganizacion(Integer.toString(usuario.getOrgCod()));
                tramExpVO.setCodEntidad(Integer.toString(usuario.getEntCod()));
                tramExpVO.setCodUnidadOrganicaExp(codUnidadOrganicaExp);
                tramExpVO.setCodUnidadTramitadoraTram(codUnidadTramitadoraTram);
                tramExpVO.setDesdeInformesGestion(desdeInformesGestion);
                tramExpVO.setTodos(todos);
                session.removeAttribute("RelacionDomicilios");
                String modoConsulta = request.getParameter("modoConsulta");
                if (modoConsulta == null) {
                    modoConsulta = "no";
                }
                tramExpVO.setModoConsulta(modoConsulta);
                String expRelacionado = request.getParameter("expRelacionado");
                if (expRelacionado == null) {
                    expRelacionado = "no";
                } else {
                    String codMunExpIni = request.getParameter("codMunExpIni");
                    String ejercicioExpIni = request.getParameter("ejercicioExpIni");
                    String numeroExpIni = request.getParameter("numeroExpIni");
                    tramExpVO.setCodMunicipioIni(codMunExpIni);
                    tramExpVO.setEjercicioIni(ejercicioExpIni);
                    tramExpVO.setNumeroIni(numeroExpIni);
                }
                tramExpVO.setExpRelacionado(expRelacionado);

                tramExpVO.setDesdeJsp("si");

                tramExpVO = TramitacionExpedientesManager.getInstance().cargarDatos(tramExpVO, params);
                session.setAttribute("obs", tramExpVO.getObservaciones());
                Vector DS = TramitacionExpedientesManager.getInstance().cargarDatosSuplementariosExpediente(tramExpVO, params);

                tramExpVO.setEstructuraDatosSuplExpediente((Vector) DS.elementAt(0));
                tramExpVO.setValoresDatosSuplExpediente((Vector) DS.elementAt(1));
                tramExpVO.setEstructuraDatosSuplTramites((Vector) DS.elementAt(2));
                tramExpVO.setValoresDatosSuplTramites((Vector) DS.elementAt(3));
                tramExpVO.setEstructuraDatosSuplementarios((Vector) DS.elementAt(4));
                tramExpVO.setValoresDatosSuplementarios((Vector) DS.elementAt(5));

                Vector estructuraAgrupacionesDatosSuplementarios =
                        TramitacionExpedientesManager.getInstance().cargaEstructuraAgrupaciones(tramExpVO, params);
                tramExpVO.setListaAgrupaciones(estructuraAgrupacionesDatosSuplementarios);



                GeneralValueObject valoresFicheros = new GeneralValueObject();
                valoresFicheros = TramitacionExpedientesManager.getInstance().cargaValoresFicheros(tramExpVO, tramExpVO.getEstructuraDatosSuplementarios(), params);
                tramExpVO.setListaFicheros(valoresFicheros);
//                 m_Log.info("VALORES FICHEROS ..... "+tramExpForm.getListaFicheros());

                GeneralValueObject nombresFicheros = TramitacionExpedientesManager.getInstance().cargaNombresFicheros(tramExpVO, tramExpVO.getEstructuraDatosSuplementarios(), params);
                tramExpVO.setListaNombresFicheros(nombresFicheros);


                GeneralValueObject tiposFicheros = new GeneralValueObject();
                tiposFicheros = TramitacionExpedientesManager.getInstance().cargaTiposFicheros(tramExpVO, tramExpVO.getEstructuraDatosSuplementarios(), params);
                tramExpVO.setListaTiposFicheros(tiposFicheros);
//                 m_Log.info("VALORES TIPOS FICHEROS ..... "+tramExpForm.getListaTiposFicheros());

                cargarFormsPDF(tramExpVO);


                // inicio COMPROBAR SI EXISTE BLOQUEO EN EL TRÁMITE DEL EXPEDIENTE
                String usu_bloqueo = tramExpVO.getBloqueo();
                if (usu_bloqueo != null) {
                    if (!usu_bloqueo.equals("")) {
                        if (String.valueOf(usuario.getIdUsuario()).equals(usu_bloqueo)) {
                            tramExpVO.setBloqueo("1"); // Bloqueo por el usuario
                        } else {
                            tramExpVO.setBloqueo("2"); // Bloqueo por otro usuario
                        }
                    } else {
                        tramExpVO.setBloqueo("0"); // Sin bloqueo
                    }
                } else {
                    tramExpVO.setBloqueo("0"); // Sin bloqueo
                }


                Vector listaTramitesNoFinalizados = TramitesExpedientesManager.getInstance().getTramitesExpedienteSinFinalizar(tramExpVO, params);
                if (listaTramitesNoFinalizados.size() == 0) {
                    tramExpVO.setRespOpcion("");
                } else {
                    tramExpVO.setListaTramitesPendientes(listaTramitesNoFinalizados);
                    tramExpVO.setRespOpcion("tramitesSinFinalizar");
                    m_Log.debug("  *********************   no puede finalizar ... ");

                }
                m_Log.debug("  *********************   no puede finalizar ... " + tramExpVO.getRespOpcion());
                // fin COMPROBAR SI EXISTE BLOQUEO EN EL TRÁMITE DEL EXPEDIENTE
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "inicioOculto";

                //davidvim
                AdaptadorSQLBD abd = null;
                Connection con = null;

                try {

                    abd = new AdaptadorSQLBD(params);
                    con = abd.getConnection();

                    String ChequearCamposExpresion = TramitacionExpedientesDAO.getInstance().tratarCamposExpresion(abd, con, tramExpVO);

                    //if ("".equals(ChequearCamposExpresion))
                    if (!"0".equals(ChequearCamposExpresion) && !"1".equals(ChequearCamposExpresion)) {
                        opcion = "grabarTramite";
                        tramExpVO.setRespOpcion("ErrorExpresion#" + ChequearCamposExpresion);
                    } else {
                        tramExpVO.setRespOpcion("");
                        m_Log.debug("Value of ChequearCamposExpresion -- >" + ChequearCamposExpresion);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        abd.devolverConexion(con);
                    } catch (BDException e) {
                        e.printStackTrace();
                    }
                }


            } else if (opcion.equals("bloquear") || opcion.equals("bloquearAccesoExterno")) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                if (TramitacionExpedientesManager.getInstance().bloquearTramite(tramExpVO, usuario.getIdUsuario(), params) == -1) {
                    tramExpVO.setRespOpcion("noGrabado");
                } else {
                    tramExpVO.setRespOpcion("grabado");
                    
                    // Registramos el movimiento de la operacion
                    GeneralValueObject paramMovimiento = new GeneralValueObject();
                    paramMovimiento.setAtributo("codMunicipio", tramExpVO.getCodMunicipio());
                    paramMovimiento.setAtributo("codProcedimiento", tramExpVO.getCodProcedimiento());
                    paramMovimiento.setAtributo("ejercicio", tramExpVO.getEjercicio());
                    paramMovimiento.setAtributo("numero", tramExpVO.getNumeroExpediente());
                    paramMovimiento.setAtributo("codTramite", tramExpVO.getCodTramite());
                    paramMovimiento.setAtributo("nomTramite", tramExpVO.getTramite());
                    paramMovimiento.setAtributo("ocurrTramite", tramExpVO.getOcurrenciaTramite());
                    paramMovimiento.setAtributo("fechaInicioTramite", tramExpVO.getFechaInicio());
                    paramMovimiento.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
                    paramMovimiento.setAtributo("nombreUsuario", usuario.getNombreUsu());
                    
                    OperacionesExpedienteManager.getInstance().registrarBloquearTramite(paramMovimiento, params);
                }
                tramExpVO.setBloqueo("1"); // Bloqueo por el usuario

                tramExpVO.setDesdeJsp("si");
                tramExpVO = TramitacionExpedientesManager.getInstance().cargarDatos(tramExpVO, params);

                Vector estructuraDatosSuplementarios =
                        TramitacionExpedientesManager.getInstance().cargaEstructuraDatosSuplementarios(tramExpVO, params);
                tramExpVO.setEstructuraDatosSuplementarios(estructuraDatosSuplementarios);

                Vector estructuraAgrupacionesDatosSuplementarios =
                        TramitacionExpedientesManager.getInstance().cargaEstructuraAgrupaciones(tramExpVO, params);
                tramExpVO.setListaAgrupaciones(estructuraAgrupacionesDatosSuplementarios);

                Vector valoresDatosSuplementarios = TramitacionExpedientesManager.getInstance().cargaValoresDatosSuplementarios(tramExpVO, estructuraDatosSuplementarios, params);
                tramExpVO.setValoresDatosSuplementarios(valoresDatosSuplementarios);
//                m_Log.info("ESTRUCTURA VALORES ... "+valoresDatosSuplementarios);

                GeneralValueObject valoresFicheros = TramitacionExpedientesManager.getInstance().cargaValoresFicheros(tramExpVO, estructuraDatosSuplementarios, params);
                tramExpVO.setListaFicheros(valoresFicheros);
//                m_Log.info("VALORES FICHEROS ..... "+tramExpForm.getListaFicheros());

                GeneralValueObject nombresFicheros = TramitacionExpedientesManager.getInstance().cargaNombresFicheros(tramExpVO, estructuraDatosSuplementarios, params);
                tramExpVO.setListaNombresFicheros(nombresFicheros);

                GeneralValueObject tiposFicheros = TramitacionExpedientesManager.getInstance().cargaTiposFicheros(tramExpVO, estructuraDatosSuplementarios, params);
                tramExpVO.setListaTiposFicheros(tiposFicheros);
//                m_Log.info("VALORES TIPOS FICHEROS ..... "+tramExpForm.getListaTiposFicheros());
                // inicio COMPROBAR SI EXISTE BLOQUEO EN EL TRÁMITE DEL EXPEDIENTE
                String usu_bloqueo = tramExpVO.getBloqueo();
                if (!usu_bloqueo.equals("")) {
                    if (String.valueOf(usuario.getIdUsuario()).equals(usu_bloqueo)) {
                        tramExpVO.setBloqueo("1"); // Bloqueo por el usuario
                    } else {
                        tramExpVO.setBloqueo("2"); // Bloqueo por otro usuario
                        //tramExpVO.setModoConsulta("si");
                    }
                } else {
                    tramExpVO.setBloqueo("0"); // Sin bloqueo
                }

                if(opcion.equals("bloquear")) {
                    opcion = "inicio";
                } else if(opcion.equals("bloquearAccesoExterno")) {
                    opcion = "inicioAccesoExterno";
                }
                m_Log.debug("FIN BLOQUEAR");
            } else if (opcion.equals("desbloquear") || opcion.equals("desbloquearAccesoExterno")) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                if (TramitacionExpedientesManager.getInstance().desbloquearTramite(tramExpVO, usuario.getIdUsuario(), params) == -1) {
                    tramExpVO.setRespOpcion("noGrabado");
                } else {
                    tramExpVO.setRespOpcion("grabado");
                    
                    // Registramos el movimiento de la operacion
                    GeneralValueObject paramMovimiento = new GeneralValueObject();
                    paramMovimiento.setAtributo("codMunicipio", tramExpVO.getCodMunicipio());
                    paramMovimiento.setAtributo("codProcedimiento", tramExpVO.getCodProcedimiento());
                    paramMovimiento.setAtributo("ejercicio", tramExpVO.getEjercicio());
                    paramMovimiento.setAtributo("numero", tramExpVO.getNumeroExpediente());
                    paramMovimiento.setAtributo("codTramite", tramExpVO.getCodTramite());
                    paramMovimiento.setAtributo("nomTramite", tramExpVO.getTramite());
                    paramMovimiento.setAtributo("ocurrTramite", tramExpVO.getOcurrenciaTramite());
                    paramMovimiento.setAtributo("fechaInicioTramite", tramExpVO.getFechaInicio());
                    paramMovimiento.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
                    paramMovimiento.setAtributo("nombreUsuario", usuario.getNombreUsu());

                    OperacionesExpedienteManager.getInstance().registrarDesbloquearTramite(paramMovimiento, params);
                }

                tramExpVO.setBloqueo("0"); // Bloqueo por el usuario

                tramExpVO.setDesdeJsp("si");

                tramExpVO = TramitacionExpedientesManager.getInstance().cargarDatos(tramExpVO, params);

                Vector estructuraDatosSuplementarios =
                        TramitacionExpedientesManager.getInstance().cargaEstructuraDatosSuplementarios(tramExpVO, params);
                tramExpVO.setEstructuraDatosSuplementarios(estructuraDatosSuplementarios);

                Vector estructuraAgrupacionesDatosSuplementarios =
                        TramitacionExpedientesManager.getInstance().cargaEstructuraAgrupaciones(tramExpVO, params);
                tramExpVO.setListaAgrupaciones(estructuraAgrupacionesDatosSuplementarios);

                Vector valoresDatosSuplementarios =
                        TramitacionExpedientesManager.getInstance().cargaValoresDatosSuplementarios(tramExpVO, estructuraDatosSuplementarios, params);
                tramExpVO.setValoresDatosSuplementarios(valoresDatosSuplementarios);
//                m_Log.info("ESTRUCTURA VALORES ... "+valoresDatosSuplementarios);

                GeneralValueObject valoresFicheros =
                        TramitacionExpedientesManager.getInstance().cargaValoresFicheros(tramExpVO, estructuraDatosSuplementarios, params);
                tramExpVO.setListaFicheros(valoresFicheros);
//                m_Log.info("VALORES FICHEROS ..... "+tramExpForm.getListaFicheros());

                GeneralValueObject nombresFicheros = TramitacionExpedientesManager.getInstance().cargaNombresFicheros(tramExpVO, estructuraDatosSuplementarios, params);
                tramExpVO.setListaNombresFicheros(nombresFicheros);

                GeneralValueObject tiposFicheros =
                        TramitacionExpedientesManager.getInstance().cargaTiposFicheros(tramExpVO, estructuraDatosSuplementarios, params);
                tramExpVO.setListaTiposFicheros(tiposFicheros);
//                m_Log.info("VALORES TIPOS FICHEROS ..... "+tramExpForm.getListaTiposFicheros());
                // inicio COMPROBAR SI EXISTE BLOQUEO EN EL TRÁMITE DEL EXPEDIENTE
                String usu_bloqueo = tramExpVO.getBloqueo();
                if (!usu_bloqueo.equals("")) {
                    if (String.valueOf(usuario.getIdUsuario()).equals(usu_bloqueo)) {
                        tramExpVO.setBloqueo("1"); // Bloqueo por el usuario
                    } else {
                        tramExpVO.setBloqueo("2"); // Bloqueo por otro usuario
                        //tramExpVO.setModoConsulta("si");
                    }
                } else {
                    tramExpVO.setBloqueo("0"); // Sin bloqueo
                }

                if(opcion.equals("desbloquear")) {
                    opcion = "inicio";
                } else if(opcion.equals("desbloquearAccesoExterno")) {
                    opcion = "inicioAccesoExterno";
                }
                m_Log.debug("FIN DESBLOQUEAR");
            } else if (opcion.equals("listaDocumentosTramite")) {
                tramExpForm = (TramitacionExpedientesForm) form;
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setRelacion("N");
                tramExpVO = TramitacionExpedientesManager.getInstance().getListaDocumentosTramite(tramExpVO, params);
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                session.removeAttribute("RelacionDomicilios");

                opcion = "listaDocumentosTramite";
            } else if ("chequearExpresionesValidacionCamposNumericos".equals(opcion)) {

                /**
                 * var parametros =
                 * "opcion=chequearExpresionesValidacionCamposNumericos&codTramite="
                 * + codTramite + "&ocurrenciaTramite=" + ocurrenciaTramite +
                 * "&codProcedimiento=" + escape(codProcedimiento) +
                 * "&codOrganizacion=" + escape(codMunicipio) + "&numero=" +
                 * escape(numero) + "&ejercicio=" + escape(ejercicio);
                 *
                 */
                /**
                 * *
                 * TramitacionExpedientesValueObject tramVO = new
                 * TramitacionExpedientesValueObject();
                 * tramVO.setCodTramite(request.getParameter("codTramite"));
                 * tramVO.setOcurrenciaTramite(request.getParameter("ocurrenciaTramite"));
                 * tramVO.setCodOrganizacion(request.getParameter("codOrganizacion"));
                 * tramVO.setCodMunicipio(request.getParameter("codOrganizacion"));
                 * tramVO.setNumeroExpediente(request.getParameter("numero"));
                 * tramVO.setNumero(request.getParameter("numero"));
                 * tramVO.setEjercicio(request.getParameter("ejercicio"));
                 *
                 */
                // Se comprueba si el trámite tiene campos numéricos con alguna expresión de validación que no se cumple
                // La llamada se realizar por AJAX
                String ChequearCamposExpresion = "";
                try {
                    tramExpVO = tramExpForm.getTramitacionExpedientes();
                    ChequearCamposExpresion = TramitacionExpedientesManager.getInstance().tratarCamposExpresionTramite(tramExpVO, tramExpForm, request, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.setContentType("text/xml");
                PrintWriter out = response.getWriter();
                out.print(ChequearCamposExpresion);
                out.flush();
                out.close();

            } else if ("grabarTramite".equals(opcion) || opcion.equals("grabarAccesoExterno")) {

                String finalizar = request.getParameter("finalizar");
                String bloqueo = request.getParameter("bloqueo");
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setNombreUsuario(usuario.getNombreUsu());
                tramExpVO.setBloqueo("no"); //Aqui se utiliza para no eliminar el bloqueo, solo grabar                
                String ChequearCamposExpresion = null;

                try {
                    ChequearCamposExpresion = TramitacionExpedientesManager.getInstance().tratarCamposExpresionTramite(tramExpVO, tramExpForm, request, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //**********************
                //**********************
                if ("0".equals(ChequearCamposExpresion) || "1".equals(ChequearCamposExpresion)) {
                    /**
                     * ***** SE PROCEDE A GRABAR EL TRÁMITE **************
                     */
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("el codigo del tramite en grabarTramite es : " + tramExpVO.getCodTramite());
                    }
                    if (TramitacionExpedientesManager.getInstance().grabarTramite(tramExpVO, params) == -1) {
                        tramExpVO.setRespOpcion("noGrabado");
                    } else {
                        int resultado = grabarDatosSuplementarios(tramExpForm, tramExpVO, request, params);

                        Vector eDS = tramExpForm.getEstructuraDatosSuplementarios();

                        if (resultado == 1) {
                            resultado = grabarDatosSuplementariosFichero(tramExpForm, tramExpVO, request, params);
                            if (resultado != 1) {
                                request.setAttribute("errorGrabarDatosSuplFichero", "1");
                            }
                        }


                        /**
                         * ********************************************************************************************
                         */
                        /**
                         * ** SE ACTUALIZAN EL ESTADO Y RUTA DE LOS DOCUMENTOS
                         * NUEVOS, PORQUE HAN SIDO DADOS      ****
                         */
                        /**
                         * ** DE ALTA A TRAVÉS DEL PLUGIN DE ALMACENAMIENTO
                         * CORRESPONDIENTE                        ****
                         */
                        /**
                         * ********************************************************************************************
                         */
                        if (resultado == 1) {
                            // Registrar el movimiento
                            if (StringUtils.isNotEmpty(bloqueo) && !bloqueo.equals("0")) {
                                tramExpVO.setBloqueo("si");
                            }
                            OperacionesExpedienteManager.getInstance().registrarGrabarTramite(tramExpVO, params);

                            // Si se han grabado los campos suplementarios, entonces, se procede
                            // a cambiar el estado de los documentos con estado NUEVO a GRABADO
                            GeneralValueObject estados = tramExpForm.getListaEstadoFicheros();
                            GeneralValueObject rutas = tramExpForm.getListaRutaFicherosDisco();
                            GeneralValueObject longitudesFicherosTramite = tramExpForm.getListaLongitudFicheros();
                            String sufijoFichero = "_" + tramExpVO.getOcurrenciaTramite();

                            GeneralValueObject longitudesFicherosExpediente = fichaExpForm.getListaLongitudFicherosDisco();

                            for (int i = 0; eDS != null && i < eDS.size(); i++) {
                                EstructuraCampo eC = (EstructuraCampo) eDS.get(i);
                                String codCampo = eC.getCodCampo();

                                if (eC.getCodTipoDato().equals("5")) {
                                    // 5 = FICHERO
                                    Integer estado = (Integer) estados.getAtributo(codCampo + sufijoFichero);
                                    if (estado != null && estado.intValue() == ConstantesDatos.ESTADO_DOCUMENTO_NUEVO) {
                                        estados.setAtributo(codCampo + sufijoFichero, ConstantesDatos.ESTADO_DOCUMENTO_GRABADO);
                                    } else if (estado != null && estado.intValue() == ConstantesDatos.ESTADO_DOCUMENTO_ELIMINADO) {
                                        // Se pone el estado del fichero a vacío
                                        estados.setAtributo(codCampo + sufijoFichero, new Integer(ConstantesDatos.ESTADO_DOCUMENTO_VACIO));
                                        // Al ser eliminado el fichero, se pone su longitud a cero
                                        longitudesFicherosTramite.setAtributo(codCampo + sufijoFichero, new Integer(0));
                                        longitudesFicherosExpediente.setAtributo(codCampo + sufijoFichero, new Integer(0));
                                    }

                                    rutas.setAtributo(codCampo + sufijoFichero, null);
                                }
                            }// for

                            tramExpForm.setListaEstadoFicheros(estados);
                            tramExpForm.setListaRutaFicherosDisco(rutas);
                            tramExpForm.setListaLongitudFicheros(longitudesFicherosTramite);
                        }
                        /**
                         * ********************************************************************************************
                         */
                        if ("true".equals(finalizar)) {
                            tramExpVO.setRespOpcion("grabadoFinalizar");
                        } else {
                            tramExpVO.setRespOpcion("grabado");
                        }
                    }
                    session.removeAttribute("RelacionDomicilios");

                    // SE COMPRUEBA SI HAY QUE LLAMAR A ALGUNA OPERACION JAVASCRIPT DE LAS PESTAÑAS DE TRÁMITE DE LOS MÓDULOS EXTERNOS QUE PUEDAN ESTAR
                    // CONFIGURADAS PARA EL TRÁMITE ACTUAL. ESTO ES NECESARIO PORQUE EL CONTENIDO DE ALGUNA DE ESTAS PESTAÑAS PUEDE DEPENDER DE ALGUNO
                    // DE LOS CAMBIOS QUE SE HAYAN HECHO EN EL TRÁMITE COMO EN EL VALOR DE ALGÚN CAMPO SUPLEMENTARIO
                    ArrayList<String> opsJavascript = ModuloIntegracionExternoFactoria.getInstance().getFuncionesJavascriptActualizarPantallaTramitacion(usuario.getOrgCod(), Integer.parseInt(tramExpVO.getCodTramite()), tramExpVO.getCodProcedimiento(), false);
                    tramExpVO.setFuncionesJavascriptModulosExternos(opsJavascript);

                    /**
                     * ***** SE PROCEDE A GRABAR EL TRÁMITE  FIN**************
                     */
                } else {
                    tramExpVO.setRespOpcion("ErrorExpresion#" + ChequearCamposExpresion);
                }





            } 
            /* ------------------------- FINALIZACION DE TRAMITES ----------------------------------*/ 
            else if ("finalizarSinCondicion".equals(opcion) || "finalizarSinCondAccesoExterno".equals(opcion)) { 

                // Sin condicion de salida.
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));


                tramExpVO.setDesdeFichaExpediente(tramExpForm.isDesdeFichaExpediente());
                String ubicacionMensajeSW = request.getParameter("ubicacionMensajeSW");
                m_Log.debug("ubicacionMensajeSW: " + ubicacionMensajeSW);

                boolean resultado = true;


                AdaptadorSQLBD abd = null;
                Connection con = null;

                try {

                    abd = new AdaptadorSQLBD(params);
                    con = abd.getConnection();

                    String ChequearCamposExpresion = TramitacionExpedientesDAO.getInstance().tratarCamposExpresion(abd, con, tramExpVO);

                    if (!"0".equals(ChequearCamposExpresion) && !"1".equals(ChequearCamposExpresion)) {
                        if(opcion.endsWith("AccesoExterno")) {
                            opcion = "grabarAccesoExterno";
                        } else {
                            opcion = "grabarTramite";
                        }
                        resultado = false;
                        tramExpVO.setRespOpcion("ErrorExpresion" + "#" + ChequearCamposExpresion);
                    } else {
                        tramExpVO.setRespOpcion("");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        abd.devolverConexion(con);
                    } catch (BDException e) {
                    }
                }

                boolean mensajeNoFinalizado = false; //flag para mostrar msg

                try {
                    if (resultado == true) {

                        m_Log.debug("BLOQUEO : " + request.getParameter("bloqueo"));
                        tramExpVO.setBloqueo(request.getParameter("bloqueo"));
                        tramExpVO.setCodIdiomaUsuario(usuario.getIdioma());
						tramExpVO.setNombreUsuario(usuario.getNombreUsu());
                        TramitacionExpedientesManager.getInstance().finalizarTramite(tramExpVO, params);

						OperacionesExpedienteManager.getInstance().registrarFinalizarTramite(tramExpVO, null, params);
                    }
                } catch (WSException wse) {
                    ponerMensajeFalloSW(tramExpVO, wse);
                    if (wse.isMandatoryExecution()) {
                        m_Log.debug("______________no finalizado: ");
                        mensajeNoFinalizado = true;
                    }
                } catch (EjecucionSWException eswe) {
                    ponerMensajeFalloSW(tramExpVO, eswe);
                    if (eswe.isStopEjecucion()) {
                        mensajeNoFinalizado = true;
                    }
                } catch (TramitacionException te) {
                    m_Log.debug("______________ dentro de cach1: ");
                    tramExpVO.setMensajeSW(te.getMessage());
                    mensajeNoFinalizado = true;
                }

                if (resultado == true) {


                    if (mensajeNoFinalizado) {
                        tramExpVO.setRespOpcion("noFinalizado");
                        if (tramExpVO.getMensajeSW() != null && !"".equals(tramExpVO.getMensajeSW())) {
                            if ("FICHAEXPEDIENTE".equals(ubicacionMensajeSW)) {
                                // De este modo en oculto sólo se muestra el mensaje lanzada por la ejecución del servicio
                                request.setAttribute("noPulsarVolver", "SI");
                            }
                        }
                    } else {
                        tramExpVO.setRespOpcion("finalizado");
                    }

                    tramExpForm.setTramitacionExpedientes(tramExpVO);

                    if (notificar(usuario, tramExpVO, fichaExpForm.getAsunto())) {
                        tramExpVO.setNotificacionRealizada("si");
                    } else {
                        tramExpVO.setNotificacionRealizada("no");
                    }
                    session.removeAttribute("RelacionDomicilios");
                    if(opcion.endsWith("AccesoExterno")) {
                        opcion = "grabarAccesoExterno";
                    } else {
                        opcion = "grabarTramite";
                    }
                }
            } else if ("finalizarExpediente".equals(opcion) || "finalizarExpAccesoExterno".equals(opcion)) {
                // Condicion de salida: Finalizacion
                //mirar los tramites que hay finalizados si hay mas de uno no puedo finalizar
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setCodIdiomaUsuario(usuario.getIdioma());
                Vector listaTramitesNoFinalizados = TramitesExpedientesManager.getInstance().getTramitesExpedienteSinFinalizar(tramExpVO, params);
                int res = -1;

                if (listaTramitesNoFinalizados.size() == 0) {
                    boolean mensajeNoFinalizado = false; //flag para mostrar msg
                    try {
                        m_Log.debug("BLOQUEO : " + request.getParameter("bloqueo"));
                        tramExpVO.setBloqueo(request.getParameter("bloqueo"));
                        if (tramExpVO.isDesdeFichaExpediente()) {
                            // Si se viene desde ficha de expediente, se pone el campo observaciones a nulo para que no se reemplace
                            // las observaciones del trámite a finalizar por las que el usuario pueda haber introducido para el expediente
                            String obsSession = (String) session.getAttribute("obs");
                            tramExpVO.setObservaciones(obsSession);
                        }
                        res = TramitacionExpedientesManager.getInstance().finalizarExpediente(tramExpVO, params);

                    } catch (TramitacionException te) {
                        Throwable t = te.getOriginalException();
                        // Se comprueba si la excepción ha sido lanzada por la ejecución de una  operación de un WS o de un módulo de integracion
                        if ((t instanceof EjecucionOperacionModuloIntegracionException) || (t instanceof EjecucionSWException) || (t instanceof FaltaDatoObligatorioException)) {
                            res = -2;
                        }
                        tramExpVO.setMensajeSW(te.getMessage());
                        mensajeNoFinalizado = true;
                    }

                    if (mensajeNoFinalizado) {
                        tramExpVO.setRespOpcion("expedienteNoFinalizado");
                    }
                    if (res > 0) {
                        tramExpVO.setNombreUsuario(usuario.getNombreUsu());
                        OperacionesExpedienteManager.getInstance().registrarFinalizarExpediente(tramExpVO, params);
                        tramExpVO.setRespOpcion("expedienteFinalizado");
                    } else if (res == -1) {
                        tramExpVO.setRespOpcion("noFinalizadoFirmasExpediente");
                    } else {
                        tramExpVO.setRespOpcion("expedienteNoFinalizado");
                    }
                } else {
                    m_Log.debug("**********************************entro");
                    tramExpVO.setRespOpcion("tramitesSinFinalizar");
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                tramExpVO.setNotificacionRealizada("no");
                if (res > 0) {
                    if (notificar(usuario, tramExpVO, fichaExpForm.getAsunto())) {
                        tramExpVO.setNotificacionRealizada("si");
                    } else {
                        tramExpVO.setNotificacionRealizada("no");
                    }
                }
                session.removeAttribute("RelacionDomicilios");
                if("finalizarExpediente".equals(opcion)) {
                    opcion = "grabarTramite";
                } else {
                    opcion = "grabarAccesoExterno";
                }
            } else if ("finalizarExpedienteNoConvencional".equals(opcion)) {

                String ChequearCamposExpresion = "";
                m_Log.debug("** entro en finalizarExpedienteNoConvencional");
                String justificacion = request.getParameter("justificacion");
                String autoriza = request.getParameter("autoriza");
                m_Log.debug("** justificacion " + justificacion);
                m_Log.debug("** autoriza " + autoriza);

                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setJustificacion(justificacion);
                tramExpVO.setPersonaAutoriza(autoriza);
                m_Log.debug("** justificacion " + tramExpVO.getJustificacion());
                m_Log.debug("** autoriza " + tramExpVO.getPersonaAutoriza());
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setCodOrganizacion(Integer.toString(usuario.getOrgCod()));
                tramExpVO.setCodEntidad(Integer.toString(usuario.getEntCod()));
                m_Log.debug("** usuario " + Integer.toString(usuario.getIdUsuario()));
                boolean mensajeNoFinalizado = false; //flag para mostrar msg
                int res = -1;

                // Las observaciones del expediente se ponen a null para evitar que se actualizen las observaciones del trámite/s a cerrar
                tramExpVO.setObservaciones(null);
                tramExpVO.setNumeroExpediente(tramExpVO.getNumero());

                try {

                    int codOrganizacion = usuario.getOrgCod();
                    String codProcedimiento = tramExpVO.getCodProcedimiento();
                    String numExpediente = tramExpVO.getNumero();
                    int codUsuario = usuario.getIdUsuario();
                    String loginUsuario = UsuarioManager.getInstance().getLoginUsuario(codUsuario, params);

                    VerificacionFinNoConvencionalExpediente plugin = DefinicionProcedimientosManager.getInstance().getPluginFinalizacionNoConvencional(usuario.getOrgCod(), codProcedimiento, params);

                    m_Log.debug("codOrganizacion: " + codOrganizacion + ",codProcedimiento: " + codProcedimiento + ",numExpediente: " + numExpediente
                            + "codUsuario: " + codUsuario + ",loginUsuario: " + loginUsuario);


                    if (!plugin.verificarFinalizacionNoConvencional(codOrganizacion, codProcedimiento, numExpediente, codUsuario, loginUsuario)) {
                        // no se permite finalizar el expediente/ anular
                        // Se almacena un error para identificar la causa
                        request.setAttribute("ERROR_VERIFICACION_ANULACION", "NO_PERMITIR_FINALIZACION");
                    } else {
                        res = TramitacionExpedientesManager.getInstance().finalizarExpedienteNoConvencional(tramExpVO,
                                tramExpForm.getEstructuraDatosSuplementarios(), params);

                        // ini INSERTAR EXPEDIENTE DEL PROCEDIMIENTO RELACIONADO (si lo tiene)
                        m_Log.debug("** usuario " + Integer.toString(usuario.getIdUsuario()));
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("______________PROCEDIMIENTO ASOCIADO: " + tramExpVO.getProcedimientoAsociado());
                        }
                    }

                } catch (TramitacionException te) {
                    tramExpVO.setMensajeSW(te.getMessage());
                    mensajeNoFinalizado = true;
                } catch (VerificacionFinNoConvencionalExpedienteException ve) {
                    m_Log.error("ERROR AL VERIFICAR SI SE PUEDE O NO FINALIZAR EL EXPEDIENTE DE FORMA NO CONVENCIONAL: " + ve.getMessage());
                    request.setAttribute("ERROR_VERIFICACION_ANULACION", "ERROR_AL_VERIFICAR");

                } catch (VerificacionFinNoConvencionalInstanceException vie) {
                    m_Log.error("ERROR AL RECUPERAR LA INSTANCIA DEL PLUGIN DE VERIFICACIÓN DE FINALIZACIÓN DE EXPEDIENTE: " + vie.getMessage());
                    request.setAttribute("ERROR_VERIFICACION_ANULACION", "ERROR_AL_VERIFICAR");
                }

                if (mensajeNoFinalizado) {
                    tramExpVO.setRespOpcion("expedienteNoFinalizado");
                }
                if (res > 0) {
                    tramExpVO.setNombreUsuario(usuario.getNombreUsu());
                    OperacionesExpedienteManager.getInstance().registrarAnularExpediente(tramExpVO, params);
                    tramExpVO.setRespOpcion("expedienteFinalizado");
                } else if (res == -1) {
                    tramExpVO.setRespOpcion("noFinalizadoFirmasExpediente");
                } else {
                    tramExpVO.setRespOpcion("expedienteNoFinalizado");
                }

                tramExpForm.setTramitacionExpedientes(tramExpVO);
                if (notificar(usuario, tramExpVO, fichaExpForm.getAsunto())) {
                    tramExpVO.setNotificacionRealizada("si");
                } else {
                    tramExpVO.setNotificacionRealizada("no");
                }
                session.removeAttribute("RelacionDomicilios");
                if (!mensajeNoFinalizado) {
                    tramExpVO.setRespOpcion("finalizadoNoConvencional");
                }
                opcion = "finalizadoExp";

            } else if ("consultaFinalizarExpedienteNoConvencional".equals(opcion)) {

                String ejercicio = request.getParameter("codEje");
                String numeroExpediente = request.getParameter("codNum");
                String codMun = request.getParameter("codMun");
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setEjercicio(ejercicio);
                tramExpVO.setCodMunicipio(codMun);
                tramExpVO.setNumeroExpediente(numeroExpediente);

                GeneralValueObject g = TramitacionExpedientesManager.getInstance().conFinalizarExpedienteNoConvencional(tramExpVO, params);

                String justificacion = (String) g.getAtributo("justificacion");
                String perAutoriza = (String) g.getAtributo("perAutoriza");
                m_Log.debug("** justificacion " + justificacion);

                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setJustificacion(justificacion);
                tramExpVO.setPersonaAutoriza(perAutoriza);
                m_Log.debug("** setPersonaAutoriza " + tramExpVO.getJustificacion());
                opcion = "conFinalizarExpedienteNoConvencional";
            } else if ("finalizarExpedienteDefinitivo".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                boolean mensajeNoFinalizado = false; //flag para mostrar msg
                int res = -1;
                try {
                    m_Log.debug("BLOQUEO : " + request.getParameter("bloqueo"));
                    tramExpVO.setBloqueo(request.getParameter("bloqueo"));
                    res = TramitacionExpedientesManager.getInstance().finalizarExpediente(tramExpVO, params);

                } catch (TramitacionException te) {
                    tramExpVO.setMensajeSW(te.getMessage());
                    mensajeNoFinalizado = true;
                }


                if (mensajeNoFinalizado) {
                    tramExpVO.setRespOpcion("expedienteNoFinalizado");
                }
                if (res > 0) {
                    tramExpVO.setNombreUsuario(usuario.getNombreUsu());
                    OperacionesExpedienteManager.getInstance().registrarFinalizarExpediente(tramExpVO, params);
                    tramExpVO.setRespOpcion("expedienteFinalizado");
                } else {
                    tramExpVO.setRespOpcion("expedienteNoFinalizado");
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                if (notificar(usuario, tramExpVO, fichaExpForm.getAsunto())) {
                    tramExpVO.setNotificacionRealizada("si");
                } else {
                    tramExpVO.setNotificacionRealizada("no");
                }
                session.removeAttribute("RelacionDomicilios");
                opcion = "grabarTramite";
            } else if ("finalizarConTramites".equals(opcion) || "finalizarConTramAccesoExterno".equals(opcion)) {
                // Condicion de salida: tramites.

                boolean resultado = true;

                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setInsertarCodUnidadTramitadoraTram("si");


                AdaptadorSQLBD abd = null;
                Connection con = null;
                try {
                    abd = new AdaptadorSQLBD(params);
                    con = abd.getConnection();

                    String ChequearCamposExpresion = TramitacionExpedientesDAO.getInstance().tratarCamposExpresion(abd, con, tramExpVO);

                    if (!"0".equals(ChequearCamposExpresion) && !"1".equals(ChequearCamposExpresion)) {
                        if("finalizarConTramites".equals(opcion)){
                            opcion = "grabarTramite";
                        } else {
                            opcion = "grabarAccesoExterno";
                        }
                        resultado = false;
                        tramExpVO.setRespOpcion("ErrorExpresion#" + ChequearCamposExpresion);
                    } else {
                        tramExpVO.setRespOpcion("");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        abd.devolverConexion(con);
                    } catch (BDException e) {
                        m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                    }
                }

                if (resultado == true) {
                    // Con esta variable se sabe si se finaliza el trámite desde ficha de expediente
                    String ubicacionMensajeSW = request.getParameter("ubicacionMensajeSW");
                    // Lista de tramites a iniciar
                    String listaCodTramites = request.getParameter("listaCodTramites");
                    String listaModoTramites = request.getParameter("listaModoTramites");
                    String listaUtrTramites = request.getParameter("listaUtrTramites");
                    String listaTramitesCondEntradaNoCumplidas = request.getParameter("listaTramSigNoCumplenCondEntrada");
                    m_Log.debug("listaCodTramites : " + listaCodTramites);
                    m_Log.debug("listaModoTramites : " + listaModoTramites);
                    m_Log.debug("listaUtrTramites : " + listaUtrTramites);
                    m_Log.debug("listaTramitesCondEntradaNoCumplidas : " + listaTramitesCondEntradaNoCumplidas);
                    Vector listaTramitesIniciar = listaTramitesIniciar(listaCodTramites, listaModoTramites, listaUtrTramites, listaTramitesCondEntradaNoCumplidas, true,params);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("el tamao de la lista para finalizar es : "
                                + listaTramitesIniciar.size());
                    }
                    tramExpVO.setListaTramitesIniciar(listaTramitesIniciar);
                    Vector listaTramitesNoIniciados = new Vector();

                    boolean mensajeNoFinalizado = false; //flag para mostrar msg
                    try {
                        if (tramExpVO.isDesdeFichaExpediente()) {
                            tramExpVO.setObservaciones((String) session.getAttribute("obs"));
                            session.removeAttribute("obs");
                        }

                        m_Log.debug("BLOQUEO : " + request.getParameter("bloqueo"));
                        tramExpVO.setBloqueo(request.getParameter("bloqueo"));
                        tramExpVO.setInsertarCodUnidadTramitadoraTram("si");
                        m_Log.debug("$$$$$$ insertar codigo de la unidad tramitodora: " + tramExpVO.getInsertarCodUnidadTramitadoraTram());
                        tramExpVO.setCodIdiomaUsuario(usuario.getIdioma());
                        listaTramitesNoIniciados = TramitacionExpedientesManager.getInstance().finalizarConTramites(tramExpVO, params);
                        tramExpVO.setListaTramitesPendientes(listaTramitesNoIniciados);

                    } catch (WSException wse) {
                        ponerMensajeFalloSW(tramExpVO, wse);
                        if (wse.isMandatoryExecution()) {
                            mensajeNoFinalizado = true;
                        }
                    } catch (TramitacionException te) {
                        tramExpVO.setMensajeSW(te.getMessage());
                        mensajeNoFinalizado = true;
                    } catch (EjecucionSWException eswe) {
                        ponerMensajeFalloSW(tramExpVO, eswe);
                        if (eswe.isStopEjecucion()) {
                            mensajeNoFinalizado = true;
                        }
                    }


                    boolean tramiteYaFinalizado = false;
                    if (tramExpVO.getRespOpcion() != null) {
                        if (tramExpVO.getRespOpcion().equals("yaFinalizado")) {
                            tramiteYaFinalizado = true;
                        }
                    }
                    m_Log.debug("Trámite Ya Finalizado : " + tramiteYaFinalizado);
                    if (!tramiteYaFinalizado) {
                        if (listaTramitesNoIniciados == null) {
                            tramExpVO.setRespOpcion("noGrabado");
                        } else {
                            tramExpVO.setNombreUsuario(usuario.getNombreUsu());

                            OperacionesExpedienteManager.getInstance().registrarFinalizarTramite(tramExpVO, null, params);

                            tramExpVO.setListaTramitesPendientes(listaTramitesNoIniciados);

                            if (mensajeNoFinalizado) {
                                tramExpVO.setRespOpcion("noFinalizado");
                                if ("FICHAEXPEDIENTE".equals(ubicacionMensajeSW)) {
                                    // De este modo en oculto sólo se muestra el mensaje lanzada por la ejecución del servicio
                                    request.setAttribute("noPulsarVolver", "SI");
                                }


                            } else if (listaTramitesNoIniciados.size() == 0 && listaTramitesIniciar.size() > 0) {
                                tramExpVO.setRespOpcion("finalizado");
                            } else {
                                tramExpVO.setRespOpcion("tramitesPendientes");
                                // Comprobamos si se ha iniciado o no algun tramite.
                                // Para ello comparamos el tamaño de la lista de tramites a iniciar,
                                // con el tamaño de la lista de tramites no iniciados.
                                if (listaTramitesNoIniciados.size() == listaTramitesIniciar.size()) {
                                    // Entonces no se ha iniciado ningun tramite.
                                    // Ahora quedaría comprobar si existe algun trámite abierto por el que el expediente pueda
                                    // continuar.
                                    // Creamos el GeneralValueObject que contiene los datos del tramite a retroceder.
                                    GeneralValueObject gVO = new GeneralValueObject();
                                    gVO.setAtributo("codMunicipio", tramExpVO.getCodMunicipio());
                                    gVO.setAtributo("codProcedimiento", tramExpVO.getCodProcedimiento());
                                    gVO.setAtributo("ejercicio", tramExpVO.getEjercicio());
                                    gVO.setAtributo("usuario", tramExpVO.getCodUsuario());
                                    gVO.setAtributo("codOrganizacion", tramExpVO.getCodOrganizacion());
                                    gVO.setAtributo("codEntidad", tramExpVO.getCodEntidad());
                                    gVO.setAtributo("numero", tramExpVO.getNumeroExpediente());
                                    gVO.setAtributo("tramiteRetroceder", tramExpVO.getTramite());
                                    gVO.setAtributo("codTramiteRetroceder", tramExpVO.getCodTramite());
                                    gVO.setAtributo("ocurrenciaTramiteRetroceder", tramExpVO.getOcurrenciaTramite());
                                    gVO.setAtributo("codigoIdiomaUsuario", Integer.toString(usuario.getIdioma()));
                                    gVO.setAtributo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO, ConstantesDatos.ORIGEN_LLAMADA_INTERFAZ_WEB);


                                    //Esta condición sirve para determinar si hay tramites abiertos que son condicion de entrada de otro y tienen que estar cerrados
                                    if (!FichaExpedienteManager.getInstance().tieneTramitesAbiertos(gVO, params)) {

                                        // No queda ningun tramite abierto en el flujo.
                                        // Retrocedemos el expediente.
                                        try {
                                            FichaExpedienteManager.getInstance().retrocederExpedienteMetodoAtrasNuevo(gVO, params);
                                        } catch (EjecucionSWException e) {
                                            ponerMensajeFalloSW(tramExpVO, e);
                                        } catch (EjecucionModuloException e) {
                                            tramExpVO.setMensajeSW(e.getMensaje());
                                        }
                                        tramExpVO.setResultadoFinalizar("AutoRetrocedido");
		     // Registrar movimiento
                                        if (StringUtils.isEmpty(tramExpVO.getUsuario())) {
                                            gVO.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
                                        }
                                        gVO.setAtributo("nomUsuario", usuario.getNombreUsu());
                                        gVO.setAtributo("fechaInicioTramiteRetroceder", tramExpVO.getFechaInicio());
                                        
                                        OperacionesExpedienteManager.getInstance().registrarRetrocederTramite(gVO, params);
                                    } else {
                                        tramExpVO.setResultadoFinalizar("FinalizadoNormal");
                                    }
                                } else {
                                    tramExpVO.setResultadoFinalizar("FinalizadoNormal");
                                }
                            }

                            //int resultadoGrabarDatosSuplementarios = grabarDatosSuplementarios(tramExpForm,tramExpVO,request,params);
                        }
                    }
                    tramExpForm.setTramitacionExpedientes(tramExpVO);
                    boolean notificar = notificar(usuario, tramExpVO, fichaExpForm.getAsunto());
                    if (notificar) {
                        tramExpVO.setNotificacionRealizada("si");
                    } else {
                        tramExpVO.setNotificacionRealizada("no");
                    }

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("-->Valor de RespOpcion =" + tramExpForm.getRespOpcion());
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("-->Valor de NotificacionRealizada  =" + tramExpForm.getNotificacionRealizada());
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("-->Valor de MensajeSW  =" + tramExpForm.getMensajeSW());
                    }
                    //if (m_Log.isDebugEnabled()) m_Log.debug("-->Valor de   =" + tramExpForm.getMensajeSW());

                    session.removeAttribute("RelacionDomicilios");
                }
                if("finalizarConTramites".equals(opcion)) {
                    opcion = "grabarTramite";
                } else {
                    opcion = "grabarAccesoExterno";
                }
            } else if (("finalizarConResolucionFavorable".equals(opcion)) || ("finalizarConResolucionDesfavorable".equals(opcion)) 
                    || ("finalizarResFavAccesoExterno".equals(opcion)) || ("finalizarResDesfavAccesoExterno".equals(opcion))) {
                // Condicion de salida: Resolucion con finalizacion
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                Vector listaTramitesNoFinalizados = new Vector();
                listaTramitesNoFinalizados = TramitesExpedientesManager.getInstance().getTramitesExpedienteSinFinalizar(tramExpVO, params);

                String ubicacionMensajeSW = request.getParameter("ubicacionMensajeSW");
                m_Log.debug("ubicacionMensajeSW: " + ubicacionMensajeSW);

                if (listaTramitesNoFinalizados.size() == 0) {
                    boolean mensajeNoFinalizado = false; //flag para mostrar msg
                    int res = -1;
                    try {
                        if (tramExpVO.isDesdeFichaExpediente()) {
                            tramExpVO.setObservaciones((String) session.getAttribute("obs"));
                            session.removeAttribute("obs");
                        }

                        m_Log.debug("BLOQUEO : " + request.getParameter("bloqueo"));
                        tramExpVO.setBloqueo(request.getParameter("bloqueo"));
                        res = TramitacionExpedientesManager.getInstance().finalizarExpediente(tramExpVO, params);

                    } catch (TramitacionException te) {
                        tramExpVO.setMensajeSW(te.getMessage());
                        mensajeNoFinalizado = true;
                    }

                    boolean tramiteYaFinalizado = false;
                    if (tramExpVO.getRespOpcion() != null) {
                        if (tramExpVO.getRespOpcion().equals("yaFinalizado")) {
                            tramiteYaFinalizado = true;
                        }
                    }
                    m_Log.debug("Trámite Ya Finalizado : " + tramiteYaFinalizado);
                    if (!tramiteYaFinalizado) {
                    }
                    //   int resultadoGrabarDatosSuplementarios = 0;
                    if (mensajeNoFinalizado) {
                        tramExpVO.setRespOpcion("expedienteNoFinalizado");
                    } if(res >0) {
                        tramExpVO.setNombreUsuario(usuario.getNombreUsu());
                        OperacionesExpedienteManager.getInstance().registrarFinalizarExpediente(tramExpVO, params);
                        tramExpVO.setRespOpcion("expedienteFinalizado");
                    } else {
                        tramExpVO.setRespOpcion("expedienteNoFinalizado");
                    }
                } else {
                    tramExpVO.setListaTramitesPendientes(listaTramitesNoFinalizados);
                    tramExpVO.setRespOpcion("tramitesSinFinalizar");
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                if (notificar(usuario, tramExpVO, fichaExpForm.getAsunto())) {
                    tramExpVO.setNotificacionRealizada("si");
                } else {
                    tramExpVO.setNotificacionRealizada("no");
                }
                session.removeAttribute("RelacionDomicilios");
                if(opcion.endsWith("AccesoExterno")) {
                    opcion = "grabarAccesoExterno";
                } else {
                    opcion = "grabarTramite";
                }
            } else if (("finalizarConResolucionFavorableConTramites".equals(opcion)) || ("finalizarConResolucionDesfavorableConTramites".equals(opcion)) 
                    || ("finalizarResFavConTramAccesoExterno".equals(opcion)) || ("finalizarResDesfavConTramAccesoExterno".equals(opcion))) {

                // Condicion de salida: Resolucion con tramites
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setCodIdiomaUsuario(usuario.getIdioma());
                // Lista de tramites a iniciar
                String listaCodTramites = request.getParameter("listaCodTramites");
                String listaModoTramites = request.getParameter("listaModoTramites");
                String listaUtrTramites = request.getParameter("listaUtrTramites");
                String listaTramSigNoCumplenCondEntrada = request.getParameter("listaTramSigNoCumplenCondEntrada");
                m_Log.debug("listaCodTramites : " + listaCodTramites);
                m_Log.debug("listaModoTramites : " + listaModoTramites);
                m_Log.debug("listaUtrTramites : " + listaUtrTramites);
                m_Log.debug("listaTramSigNoCumplenCondEntrada : " + listaTramSigNoCumplenCondEntrada);
                Vector listaTramitesIniciar = listaTramitesIniciar(listaCodTramites, listaModoTramites, listaUtrTramites, listaTramSigNoCumplenCondEntrada, false,params);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el tamao de la lista para finalizar es : "
                            + listaTramitesIniciar.size());
                }
                tramExpVO.setListaTramitesIniciar(listaTramitesIniciar);

                if (tramExpVO.isDesdeFichaExpediente()) {
                    tramExpVO.setObservaciones((String) session.getAttribute("obs"));
                    session.removeAttribute("obs");
                }
                boolean resPreg = !("finalizarConResolucionFavorableConTramites".equals(opcion) && "finalizarResFavConTramAccesoExterno".equals(opcion));
                Vector listaTramitesNoIniciados = new Vector();

                boolean mensajeNoFinalizado = false; //flag para mostrar msg

                String ubicacionMensajeSW = request.getParameter("ubicacionMensajeSW");
                m_Log.debug("ubicacionMensajeSW: " + ubicacionMensajeSW);

                try {
                    m_Log.debug("BLOQUEO : " + request.getParameter("bloqueo"));
                    tramExpVO.setBloqueo(request.getParameter("bloqueo"));
                    listaTramitesNoIniciados = TramitacionExpedientesManager.getInstance().finalizarConResolucionConTramites(tramExpVO, params, resPreg);

                    m_Log.debug("EL METODO DE FINALIZACION HA INTENTADO FINALIZAR: " + listaTramitesIniciar.size() + " TRAMITES");

                } catch (EjecucionSWException e) {
                    ponerMensajeFalloSW(tramExpVO, e);
                    if (e.isStopEjecucion()) {
                        mensajeNoFinalizado = true;
                    }
                } catch (WSException wse) {
                    ponerMensajeFalloSW(tramExpVO, wse);
                    if (wse.isMandatoryExecution()) {
                        mensajeNoFinalizado = true;
                    }
                } catch (TramitacionException te) {
                    tramExpVO.setMensajeSW(te.getMessage());
                    mensajeNoFinalizado = true;
                }

                if (mensajeNoFinalizado) {
                    tramExpVO.setRespOpcion("noFinalizado");
                    if ("FICHAEXPEDIENTE".equals(ubicacionMensajeSW)) {
                        // De este modo en oculto sólo se muestra el mensaje lanzada por la ejecución del servicio
                        request.setAttribute("noPulsarVolver", "SI");
                    }
                } else if (listaTramitesNoIniciados == null) {
                    tramExpVO.setRespOpcion("noGrabado");
                    tramExpForm.setTramitacionExpedientes(tramExpVO);
                } else {
                    tramExpVO.setNombreUsuario(usuario.getNombreUsu());

                    OperacionesExpedienteManager.getInstance().registrarFinalizarTramite(tramExpVO, resPreg, params);
                    tramExpVO.setListaTramitesPendientes(listaTramitesNoIniciados);

                    // Registrar la operacion
                    GeneralValueObject paramsRegistroOperacion = new GeneralValueObject();
                    paramsRegistroOperacion.setAtributo("codMunicipio", tramExpVO.getCodMunicipio());
                    paramsRegistroOperacion.setAtributo("codProcedimiento", tramExpVO.getCodProcedimiento());
                    paramsRegistroOperacion.setAtributo("ejercicio", tramExpVO.getEjercicio());
                    paramsRegistroOperacion.setAtributo("numero", tramExpVO.getNumeroExpediente());
                    paramsRegistroOperacion.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
                    paramsRegistroOperacion.setAtributo("codOrganizacion", tramExpVO.getCodOrganizacion());
                    paramsRegistroOperacion.setAtributo("codEntidad", tramExpVO.getCodEntidad());
                    paramsRegistroOperacion.setAtributo("nombreUsuario", usuario.getNombreUsu());
                    Vector tramites = FichaExpedienteManager.getInstance().cargaTramites(paramsRegistroOperacion, params);
                    OperacionesExpedienteManager.getInstance().previoRegistrarIniciarTramitePrepararDatos(tramExpVO, listaTramitesIniciar, listaTramitesNoIniciados,
                            tramites, paramsRegistroOperacion, params, false);
                    
                    if (listaTramitesNoIniciados.size() == 0 && listaTramitesIniciar.size() > 0) {
                        tramExpVO.setRespOpcion("finalizado");
                    } else {
                        tramExpVO.setRespOpcion("tramitesPendientes");
                        // Comprobamos si se ha iniciado o no algun tramite.
                        // Para ello comparamos el tamaño de la lista de tramites a iniciar,
                        // con el tamaño de la lista de tramites no iniciados.
                        if (listaTramitesNoIniciados.size() == listaTramitesIniciar.size()) {
                            // Entonces no se ha iniciado ningun tramite.
                            // Ahora quedaría comprobar si existe algun trámite abierto por el que el expediente pueda
                            // continuar.
                            // Creamos el GeneralValueObject que contiene los datos del tramite a retroceder.
                            GeneralValueObject gVO = new GeneralValueObject();
                            gVO.setAtributo("codMunicipio", tramExpVO.getCodMunicipio());
                            gVO.setAtributo("codProcedimiento", tramExpVO.getCodProcedimiento());
                            gVO.setAtributo("ejercicio", tramExpVO.getEjercicio());
                            gVO.setAtributo("usuario", tramExpVO.getUsuario());
                            gVO.setAtributo("codOrganizacion", tramExpVO.getCodOrganizacion());
                            gVO.setAtributo("codEntidad", tramExpVO.getCodEntidad());
                            gVO.setAtributo("numero", tramExpVO.getNumeroExpediente());
                            gVO.setAtributo("tramiteRetroceder", tramExpVO.getTramite());
                            gVO.setAtributo("codTramiteRetroceder", tramExpVO.getCodTramite());
                            gVO.setAtributo("ocurrenciaTramiteRetroceder", tramExpVO.getOcurrenciaTramite());
                            gVO.setAtributo("codigoIdiomaUsuario", Integer.toString(usuario.getIdioma()));
                            gVO.setAtributo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO, ConstantesDatos.ORIGEN_LLAMADA_INTERFAZ_WEB);


                            if (!FichaExpedienteManager.getInstance().tieneTramitesAbiertos(gVO, params)) {
                                // No queda ningun tramite abierto en el flujo.

                                // Retrocedemos el expediente.
                                try {
                                    FichaExpedienteManager.getInstance().retrocederExpedienteMetodoAtrasNuevo(gVO, params);
                                } catch (EjecucionSWException e) {
                                    ponerMensajeFalloSW(tramExpVO, e);
                                } catch (EjecucionModuloException e) {
                                    tramExpVO.setMensajeSW(e.getMensaje());
                                }
                                tramExpVO.setResultadoFinalizar("AutoRetrocedido");

                                // Registrar movimiento
                                if (StringUtils.isEmpty(tramExpVO.getUsuario())) {
                                    gVO.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
                                }
                                gVO.setAtributo("nomUsuario", usuario.getNombreUsu());
                                gVO.setAtributo("fechaInicioTramiteRetroceder", tramExpVO.getFechaInicio());
                                                                OperacionesExpedienteManager.getInstance().registrarRetrocederTramite(gVO, params);
                            } else {
                                tramExpVO.setResultadoFinalizar("FinalizadoNormal");
                            }

                        } else {
                            tramExpVO.setResultadoFinalizar("FinalizadoNormal");
                        }
                    }
                }
                if (notificar(usuario, tramExpVO, fichaExpForm.getAsunto())) {
                    tramExpVO.setNotificacionRealizada("si");
                } else {
                    tramExpVO.setNotificacionRealizada("no");
                }
                session.removeAttribute("RelacionDomicilios");
                if(opcion.endsWith("AccesoExterno")) {
                    opcion = "grabarAccesoExterno";
                } else {
                    opcion = "grabarTramite";
                }
            } else if (("finalizarConPreguntaFavorable".equals(opcion)) || ("finalizarConPreguntaDesfavorable".equals(opcion))
                    || ("finalizarPregFavAccesoExterno".equals(opcion)) || ("finalizarPregDesfavAccesoExterno".equals(opcion))) {
                // Condicion de salida: Pregunta con finalizacion
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                Vector listaTramitesNoFinalizados = new Vector();
                listaTramitesNoFinalizados = TramitesExpedientesManager.getInstance().getTramitesExpedienteSinFinalizar(tramExpVO, params);
                if (listaTramitesNoFinalizados.size() == 0) {
                    boolean mensajeNoFinalizado = false; //flag para mostrar msg
                    int res = -1;
                    try {
                        if (tramExpVO.isDesdeFichaExpediente()) {
                            tramExpVO.setObservaciones((String) session.getAttribute("obs"));
                            session.removeAttribute("obs");
                        }
                        m_Log.debug("BLOQUEO : " + request.getParameter("bloqueo"));
                        tramExpVO.setBloqueo(request.getParameter("bloqueo"));
                        res = TramitacionExpedientesManager.getInstance().finalizarExpediente(tramExpVO, params);

                    } catch (TramitacionException te) {
                        tramExpVO.setMensajeSW(te.getMessage());
                        mensajeNoFinalizado = true;

                    }
                    if (mensajeNoFinalizado) {
                        tramExpVO.setRespOpcion("expedienteNoFinalizado");
                    }
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("__________RES ()()()() : " + res);
                    }
                    if (res > 0) {
                        tramExpVO.setNombreUsuario(usuario.getNombreUsu());
                        OperacionesExpedienteManager.getInstance().registrarFinalizarExpediente(tramExpVO, params);
                        tramExpVO.setRespOpcion("expedienteFinalizado");
                    } else {
                        if (res == -999) {
                            tramExpVO.setRespOpcion("yaFinalizado");
                        } else {
                            tramExpVO.setRespOpcion("expedienteNoFinalizado");
                        }
                    }
                } else {
                    tramExpVO.setListaTramitesPendientes(listaTramitesNoFinalizados);
                    tramExpVO.setRespOpcion("tramitesSinFinalizar");
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                if (notificar(usuario, tramExpVO, fichaExpForm.getAsunto())) {
                    tramExpVO.setNotificacionRealizada("si");
                } else {
                    tramExpVO.setNotificacionRealizada("no");
                }
                session.removeAttribute("RelacionDomicilios");
                if(opcion.endsWith("AccesoExterno")) {
                    opcion = "grabarAccesoExterno";
                } else {
                    opcion = "grabarTramite";
                }

            } else if (("finalizarConPreguntaFavorableConTramites".equals(opcion)) || ("finalizarConPreguntaDesfavorableConTramites".equals(opcion))
                    || ("finalizarPregFavConTramAccesoExterno".equals(opcion)) || ("finalizarPregDesfavConTramAccesoExterno".equals(opcion))) {
                // Condicion de salida: Resolucion con tramites
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setCodIdiomaUsuario(usuario.getIdioma());
                // Lista de tramites a iniciar
                String listaCodTramites = request.getParameter("listaCodTramites");
                String listaModoTramites = request.getParameter("listaModoTramites");
                String listaUtrTramites = request.getParameter("listaUtrTramites");
                String listaTramSigNoCumplenCondEntrada = request.getParameter("listaTramSigNoCumplenCondEntrada");
                String ubicacionMensajeSW = request.getParameter("ubicacionMensajeSW");
                m_Log.debug("listaCodTramites : " + listaCodTramites);
                m_Log.debug("listaModoTramites : " + listaModoTramites);
                m_Log.debug("listaUtrTramites : " + listaUtrTramites);
                m_Log.debug("listaTramSigNoCumplenCondEntrada : " + listaTramSigNoCumplenCondEntrada);
                m_Log.debug("ubicacionMensajeSW : " + ubicacionMensajeSW);

                Vector listaTramitesIniciar = listaTramitesIniciar(listaCodTramites, listaModoTramites, listaUtrTramites, listaTramSigNoCumplenCondEntrada, true,params);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el tamao de la lista para inicar es : "
                            + listaTramitesIniciar.size());
                }
                tramExpVO.setListaTramitesIniciar(listaTramitesIniciar);

                if (tramExpVO.isDesdeFichaExpediente()) {
                    tramExpVO.setObservaciones((String) session.getAttribute("obs"));
                    session.removeAttribute("obs");
                }
                boolean resPreg = !("finalizarConPreguntaFavorableConTramites".equals(opcion) || "finalizarPregFavConTramAccesoExterno".equals(opcion));

                // fin DATOS SUPLEMENTARIOS DE LOS TRÁMITES ABIERTOS para luego comprobar las expresiones de entrada
                Vector listaTramitesNoIniciados = new Vector();

                boolean mensajeNoFinalizado = false; //flag para mostrar msg
                try {
                    m_Log.debug("BLOQUEO : " + request.getParameter("bloqueo"));
                    tramExpVO.setBloqueo(request.getParameter("bloqueo"));
                    listaTramitesNoIniciados = TramitacionExpedientesManager.getInstance().finalizarConPreguntaConTramites(tramExpVO, params, resPreg);

                } catch (WSException wse) {
                    ponerMensajeFalloSW(tramExpVO, wse);
                    if (wse.isMandatoryExecution()) {
                        mensajeNoFinalizado = true;
                    }
                } catch (TramitacionException te) {
                    tramExpVO.setMensajeSW(te.getMessage());
                    mensajeNoFinalizado = true;
                } catch (EjecucionSWException eswe) {
                    ponerMensajeFalloSW(tramExpVO, eswe);
                    if (eswe.isStopEjecucion()) {
                        mensajeNoFinalizado = true;
                    }
                }

                boolean tramiteYaFinalizado = false;
                if (tramExpVO.getRespOpcion() != null) {
                    if (tramExpVO.getRespOpcion().equals("yaFinalizado")) {
                        tramiteYaFinalizado = true;
                    }
                }
                if (!tramiteYaFinalizado) {
                    if (listaTramitesNoIniciados == null) {
                        tramExpVO.setRespOpcion("noGrabado");
                        tramExpForm.setTramitacionExpedientes(tramExpVO);
                    } else {
                        tramExpVO.setNombreUsuario(usuario.getNombreUsu());

                        OperacionesExpedienteManager.getInstance().registrarFinalizarTramite(tramExpVO, resPreg, params);
                        tramExpVO.setListaTramitesPendientes(listaTramitesNoIniciados);
                         // Registrar la operacion
                        GeneralValueObject paramsRegistroOperacion = new GeneralValueObject();
                        paramsRegistroOperacion.setAtributo("codMunicipio", tramExpVO.getCodMunicipio());
                        paramsRegistroOperacion.setAtributo("codProcedimiento", tramExpVO.getCodProcedimiento());
                        paramsRegistroOperacion.setAtributo("ejercicio", tramExpVO.getEjercicio());
                        paramsRegistroOperacion.setAtributo("numero", tramExpVO.getNumeroExpediente());
                        paramsRegistroOperacion.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
                        paramsRegistroOperacion.setAtributo("codOrganizacion", tramExpVO.getCodOrganizacion());
                        paramsRegistroOperacion.setAtributo("codEntidad", tramExpVO.getCodEntidad());
                        paramsRegistroOperacion.setAtributo("nombreUsuario", usuario.getNombreUsu());
                        Vector tramites = FichaExpedienteManager.getInstance().cargaTramites(paramsRegistroOperacion, params);
                        OperacionesExpedienteManager.getInstance().previoRegistrarIniciarTramitePrepararDatos(tramExpVO, listaTramitesIniciar, listaTramitesNoIniciados,
                                tramites, paramsRegistroOperacion, params, false);
                        
                        if (mensajeNoFinalizado) {
                            tramExpVO.setRespOpcion("noFinalizado");
                            if ("FICHAEXPEDIENTE".equals(ubicacionMensajeSW)) {
                                // De este modo en oculto sólo se muestra el mensaje lanzada por la ejecución del servicio
                                request.setAttribute("noPulsarVolver", "SI");
                            }
                        } else if (listaTramitesNoIniciados.size() == 0 && listaTramitesIniciar.size() > 0) {
                            tramExpVO.setRespOpcion("finalizado");
                        } else {
                            tramExpVO.setRespOpcion("tramitesPendientes");
                            // Comprobamos si se ha iniciado o no algun tramite.
                            // Para ello comparamos el tamaño de la lista de tramites a iniciar,
                            // con el tamaño de la lista de tramites no iniciados.
                            if (listaTramitesNoIniciados.size() == listaTramitesIniciar.size()) {
                                // Entonces no se ha iniciado ningun tramite.
                                // Ahora quedaría comprobar si existe algun trámite abierto por el que el expediente pueda
                                // continuar.
                                // Creamos el GeneralValueObject que contiene los datos del tramite a retroceder.
                                GeneralValueObject gVO = new GeneralValueObject();
                                gVO.setAtributo("codMunicipio", tramExpVO.getCodMunicipio());
                                gVO.setAtributo("codProcedimiento", tramExpVO.getCodProcedimiento());
                                gVO.setAtributo("ejercicio", tramExpVO.getEjercicio());
                                gVO.setAtributo("usuario", tramExpVO.getUsuario());
                                gVO.setAtributo("codOrganizacion", tramExpVO.getCodOrganizacion());
                                gVO.setAtributo("codEntidad", tramExpVO.getCodEntidad());
                                gVO.setAtributo("numero", tramExpVO.getNumeroExpediente());
                                gVO.setAtributo("tramiteRetroceder", tramExpVO.getTramite());
                                gVO.setAtributo("codTramiteRetroceder", tramExpVO.getCodTramite());
                                gVO.setAtributo("ocurrenciaTramiteRetroceder", tramExpVO.getOcurrenciaTramite());
                                gVO.setAtributo("codigoIdiomaUsuario", Integer.toString(usuario.getIdioma()));
                                gVO.setAtributo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO, ConstantesDatos.ORIGEN_LLAMADA_INTERFAZ_WEB);

                                if (!FichaExpedienteManager.getInstance().tieneTramitesAbiertos(gVO, params)) {
                                    // No queda ningun tramite abierto en el flujo.
                                    // Retrocedemos el expediente.

                                    try {
                                        FichaExpedienteManager.getInstance().retrocederExpedienteMetodoAtrasNuevo(gVO, params);
                                    } catch (EjecucionSWException e) {
                                        ponerMensajeFalloSW(tramExpVO, e);
                                    } catch (EjecucionModuloException e) {
                                        tramExpVO.setMensajeSW(e.getMensaje());
                                    }
                                    tramExpVO.setResultadoFinalizar("AutoRetrocedido");
                                     // Registrar movimiento
                                    if (StringUtils.isEmpty(tramExpVO.getUsuario())) {
                                        gVO.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
                                    }
                                    gVO.setAtributo("nomUsuario", usuario.getNombreUsu());
                                    gVO.setAtributo("fechaInicioTramiteRetroceder", tramExpVO.getFechaInicio());
                                    
                                    OperacionesExpedienteManager.getInstance().registrarRetrocederTramite(gVO, params);
                                } else {
                                    tramExpVO.setResultadoFinalizar("FinalizadoNormal");
                                }
                            } else {
                                tramExpVO.setResultadoFinalizar("FinalizadoNormal");
                            }
                        }
                    }
                }
                if (notificar(usuario, tramExpVO, fichaExpForm.getAsunto())) {
                    tramExpVO.setNotificacionRealizada("si");
                } else {
                    tramExpVO.setNotificacionRealizada("no");
                }
                session.removeAttribute("RelacionDomicilios");
                if(opcion.endsWith("AccesoExterno")) {
                    opcion = "grabarAccesoExterno";
                } else {
                    opcion = "grabarTramite";
                }
            } else if ("abrirDomicilios".equals(opcion)) {

                // Rellenamos un hashmap con los datos de pais, provincia y municipio por defecto.
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codPais", codPais);
                gVO.setAtributo("codProvincia", codProvincia);
                gVO.setAtributo("codMunicipio", codMunicipio);
                Vector listaCP = CodPostalesManager.getInstance().getListaCodPostales(params, gVO);

                // Anhadimos los listados de Entidades Colectivas y Singulares
                Vector listaECOs = EcosManager.getInstance().getListaEcos(gVO, params);
                Vector listaESIs = EntidadesSingularesManager.getInstance().getListaEntidadesSingulares(gVO, params);

                // Anhadimos las listas de provincias y municipios disponibles
                Vector listaProvincias = ProvinciasManager.getInstance().getListaProvincias(gVO, params);
                Vector listaMunicipios = MunicipiosManager.getInstance().getListaMunicipios(gVO, params);

                String ejercicio = request.getParameter("eje");
                String numeroExpediente = request.getParameter("num");
                String codLocalizacion = request.getParameter("codProc");
                String codmun = request.getParameter("codMun");
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setEjercicio(ejercicio);
                tramExpVO.setCodMunicipioIni(codmun);
                tramExpVO.setNumeroExpediente(numeroExpediente);
                tramExpVO.setCodLocalizacion(codLocalizacion);

                BusquedaTercerosForm bTercerosForm = new BusquedaTercerosForm();
                bTercerosForm.setListaCodPostales(listaCP);
                bTercerosForm.setListaECOs(listaECOs);
                bTercerosForm.setListaESIs(listaESIs);
                bTercerosForm.setListaProvincias(listaProvincias);
                bTercerosForm.setListaMunicipios(listaMunicipios);

                session.removeAttribute("RelacionDomicilios");
                session.setAttribute("BusquedaTercerosForm", bTercerosForm);
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("TramitacionExpedientesAction: localización |" + codLocalizacion + "|");
                }
                //m_Log.debug("TramitacionExpedientesAction: localización |" + codLocalizacion + "|");

                if (codLocalizacion != null && !"".equals(codLocalizacion) && !"null".equals(codLocalizacion)) {
                    TercerosManager terMan = TercerosManager.getInstance();
                    Vector domicilios = new Vector();
                    GeneralValueObject domicilioVO = new GeneralValueObject();

                    domicilioVO.setAtributo("codDomicilio", codLocalizacion);
                    domicilios = terMan.getDomiciliosById(params, domicilioVO);
                    session.setAttribute("RelacionDomicilios", domicilios);
                    bTercerosForm.setListaDomicilios(domicilios);
                    tramExpVO.setIdDomicilio("0");
                    session.setAttribute("modoInicio", "recargarDomicilioInicio");
                    tramExpForm.setTramitacionExpedientes(tramExpVO);
                    opcion = "abrirDomicilios";
                } else {
                    opcion = "abrirDomicilios";
                }

                request.setAttribute("desdeAltaLocalizacion", "si");

            } else if ("abrirDomiciliosConsulta".equals(opcion)) {
                /* IGUAL A abrirDomicilios pero con menos trabajo... */
                Vector listaCP = new Vector();
                BusquedaTercerosForm bTercerosForm = new BusquedaTercerosForm();
                GeneralValueObject gVO = new GeneralValueObject();
                TercerosValueObject terVO = new TercerosValueObject();
                /* anadir ECO/ESI */
                Vector listaECOs = new Vector();
                Vector listaESIs = new Vector();
                //Vector listaVias = new Vector();
                /* Fin anadir ECO/ESI */
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                gVO.setAtributo("codPais", codPais);
                gVO.setAtributo("codProvincia", codProvincia);
                gVO.setAtributo("codMunicipio", codMunicipio);
                tramExpVO.setCodMunicipio(codMunicipio);
                listaCP = CodPostalesManager.getInstance().getListaCodPostales(params, gVO);
                bTercerosForm.setListaCodPostales(listaCP);
                /* anadir ECO/ESI */
                listaECOs = EcosManager.getInstance().getListaEcos(gVO, params);
                listaESIs = EntidadesSingularesManager.getInstance().getListaEntidadesSingulares(gVO, params);
                // Anhadimos las listas de provincias y municipios disponibles
                Vector listaProvincias = ProvinciasManager.getInstance().getListaProvincias(gVO, params);
                Vector listaMunicipios = MunicipiosManager.getInstance().getListaMunicipios(gVO, params);
                bTercerosForm.setListaECOs(listaECOs);
                bTercerosForm.setListaESIs(listaESIs);
                bTercerosForm.setListaProvincias(listaProvincias);
                bTercerosForm.setListaMunicipios(listaMunicipios);
                /* fin anadir ECO/ESI */
                session.setAttribute("BusquedaTercerosForm", bTercerosForm);
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                session.setAttribute("modoInicio", "consultaLocalizacion");
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "abrirDomicilios";

            } else if ("abrirDomic".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                Vector tiposVias = new Vector();
                Vector listaCP = new Vector();
                BusquedaTercerosForm bTercerosForm = new BusquedaTercerosForm();
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codPais", codPais);
                gVO.setAtributo("codProvincia", codProvincia);
                gVO.setAtributo("codMunicipio", codMunicipio);
                TercerosValueObject terVO = new TercerosValueObject();
                CodPostalesManager codPostalesManager = CodPostalesManager.getInstance();
                //TiposViasManager tiposViasManager = TiposViasManager.getInstance();
                listaCP = codPostalesManager.getListaCodPostales(params, gVO);
                //tiposVias = tiposViasManager.getListaTiposVias(params);
                //bTercerosForm.setListaTipoVias(tiposVias);
                bTercerosForm.setListaCodPostales(listaCP);
                /* anadir ECO/ESI */
                Vector listaECOs = new Vector();
                Vector listaESIs = new Vector();
                Vector listaVias = new Vector();
                listaECOs = EcosManager.getInstance().getListaEcos(gVO, params);
                listaESIs = EntidadesSingularesManager.getInstance().getListaEntidadesSingulares(gVO, params);
                listaVias = ViasManager.getInstance().getListaViasSolas(params, gVO);
                bTercerosForm.setListaECOs(listaECOs);
                bTercerosForm.setListaESIs(listaESIs);
                bTercerosForm.setListaVias(listaVias);
                /* fin anadir ECO/ESI */
                session.removeAttribute("RelacionDomicilios");
                if (tramExpVO.getCodLocalizacion() != null) {
                    if (!"".equals(tramExpVO.getCodLocalizacion())) {
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("TramitacionExpedientesAction: tenía localización |" + tramExpVO.getCodLocalizacion() + "|");
                        }
                        TercerosManager terMan = TercerosManager.getInstance();
                        GeneralValueObject domicilioVO = new GeneralValueObject();
                        domicilioVO.setAtributo("codDomicilio", tramExpVO.getCodLocalizacion());
                        Vector domicilios = new Vector();
                        domicilios = terMan.getDomiciliosById(params, domicilioVO);
                        session.setAttribute("RelacionDomicilios", domicilios);
                        bTercerosForm.setListaDomicilios(domicilios);
                        tramExpVO.setIdDomicilio("0");
                        session.setAttribute("modoInicio", "recargarDomicilioInicio");
                    }
                }
                session.setAttribute("BusquedaTercerosForm", bTercerosForm);
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "abrirDomicilios";
            } else if ("buscarDomicilio".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                GeneralValueObject domicilioVO = new GeneralValueObject();
                TercerosManager terMan = TercerosManager.getInstance();
                BusquedaTercerosForm bTercerosForm = new BusquedaTercerosForm();
                domicilioVO.setAtributo("codPais", request.getParameter("codPais"));
                domicilioVO.setAtributo("codProvincia", request.getParameter("codProvincia"));
                domicilioVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
                /* anadir ECO/ESI */
                Vector listaECOs = new Vector();
                Vector listaESIs = new Vector();
                Vector listaVias = new Vector();
                listaECOs = EcosManager.getInstance().getListaEcos(domicilioVO, params);
                listaESIs = EntidadesSingularesManager.getInstance().getListaEntidadesSingulares(domicilioVO, params);
                listaVias = ViasManager.getInstance().getListaViasSolas(params, domicilioVO);
                bTercerosForm.setListaECOs(listaECOs);
                bTercerosForm.setListaESIs(listaESIs);
                bTercerosForm.setListaVias(listaVias);
                /* fin anadir ECO/ESI */
                domicilioVO.setAtributo("codVia", request.getParameter("txtCodVia"));
                String descVia = request.getParameter("txtNombreVia");
                String codTipoVia = request.getParameter("codTVia");
                String descTipoVia = request.getParameter("descTVia");
                String numDesde = request.getParameter("txtNumDesde");
                String letraDesde = request.getParameter("txtLetraDesde");
                String numHasta = request.getParameter("txtNumHasta");
                String letraHasta = request.getParameter("txtLetraHasta");
                String bloque = request.getParameter("txtBloque");
                String portal = request.getParameter("txtPortal");
                String escalera = request.getParameter("txtEsc");
                String planta = request.getParameter("txtPlta");
                String puerta = request.getParameter("txtPta");
                String km = request.getParameter("txtKm");
                String hm = request.getParameter("txtHm");
                String domicilio = request.getParameter("txtDomicilio");
                String poblacion = request.getParameter("txtPoblacion");
                String codPostal = request.getParameter("descPostal");
                String codECO = request.getParameter("codECO");
                String codESI = request.getParameter("codESI");
                domicilioVO.setAtributo("descVia", descVia);
                domicilioVO.setAtributo("codTipoVia", codTipoVia);
                domicilioVO.setAtributo("descTipoVia", descTipoVia);
                domicilioVO.setAtributo("numDesde", numDesde);
                domicilioVO.setAtributo("letraDesde", letraDesde);
                domicilioVO.setAtributo("numHasta", numHasta);
                domicilioVO.setAtributo("letraHasta", letraHasta);
                domicilioVO.setAtributo("bloque", bloque);
                domicilioVO.setAtributo("portal", portal);
                domicilioVO.setAtributo("escalera", escalera);
                domicilioVO.setAtributo("planta", planta);
                domicilioVO.setAtributo("puerta", puerta);
                domicilioVO.setAtributo("km", km);
                domicilioVO.setAtributo("hm", hm);
                domicilioVO.setAtributo("domicilio", domicilio);
                domicilioVO.setAtributo("poblacion", poblacion);
                domicilioVO.setAtributo("codECO", codECO);
                domicilioVO.setAtributo("codESI", codESI);
                domicilioVO.setAtributo("codPostal", codPostal);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("TramitacionExpedientesAction. BuscarDomicilio: poblacion " + poblacion);
                }
                Vector domicilios = new Vector();
                // Campo busqueda obligatorio para normalizados: Cod vía y nombre.
                boolean buscarNormalizado = false;
                if ((domicilioVO.getAtributo("codVia") != null) && (domicilioVO.getAtributo("descVia") != null)) {
                    if ((!"".equals(domicilioVO.getAtributo("codVia"))) && (!"".equals(domicilioVO.getAtributo("descVia")))) {
                        buscarNormalizado = true;
                    }
                }
                if ((domicilioVO.getAtributo("codECO") != null)) {
                    if (!"".equals(domicilioVO.getAtributo("codECO"))) {
                        buscarNormalizado = true;
                    }
                }
                if ((domicilioVO.getAtributo("codESI") != null)) {
                    if (!"".equals(domicilioVO.getAtributo("codESI"))) {
                        buscarNormalizado = true;
                    }
                }

                if (buscarNormalizado) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("TramitacionExpedientesAction. BuscarDomicilio: buscar normalizados ");
                    }
                    domicilios = terMan.getListaDomicilios(params, domicilioVO);
                }

                // Campo busqueda obligatorio para no normalizados.
                Vector domiciliosNoNormalizados = new Vector();
                domicilioVO.setAtributo("codVia", request.getParameter("txtCodViaOculto"));
                m_Log.debug("TramitacionExpedientesAction: txtCodViaOculto " + domicilioVO.getAtributo("codVia"));
                domiciliosNoNormalizados = terMan.getListaDomiciliosNoNormalizados(params, domicilioVO);
                for (int i = 0; i < domiciliosNoNormalizados.size(); i++) {
                    domicilios.addElement(domiciliosNoNormalizados.elementAt(i));
                }
                session.setAttribute("RelacionDomicilios", domicilios);
                bTercerosForm.setListaDomicilios(domicilios);
                session.setAttribute("BusquedaTercerosForm", bTercerosForm);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el codigo del tramite en buscarDomicilios es : " + tramExpVO.getCodTramite());
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "consultarListado";
            } else if ("cargarVias".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                GeneralValueObject gVO = new GeneralValueObject();
                TercerosManager terMan = TercerosManager.getInstance();
                BusquedaTercerosForm bTercerosForm = new BusquedaTercerosForm();
                String codPais = request.getParameter("nCS");
                String codMunicipio = request.getParameter("codMun");
                String codProvincia = request.getParameter("codProc");
                gVO.setAtributo("codPais", codPais);
                gVO.setAtributo("codProvincia", codProvincia);
                gVO.setAtributo("codMunicipio", codMunicipio);
                Vector vias = new Vector();
                vias = terMan.getListaVias(params, gVO);
                bTercerosForm.setListaVias(vias);
                session.removeAttribute("RelacionDomicilios");
                session.setAttribute("BusquedaTercerosForm", bTercerosForm);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el codigo del tramite en cargarVias es : " + tramExpVO.getCodTramite());
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);
            } else if ("cargar_pagina".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el codigo del tramite en cargar_pagina es : " + tramExpVO.getCodTramite());
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "cargar_pagina";
            } else if ("recargaDomicilio".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                String idDomicilio = request.getParameter("idDomicilio");
                tramExpVO.setIdDomicilio(idDomicilio);
                session.setAttribute("modoInicio", "recargarDomicilio");
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "abrirDomicilios";
            } else if ("grabarDomicilioNoNormalizado".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                GeneralValueObject gVO = new GeneralValueObject();
                TercerosManager terMan = TercerosManager.getInstance();
                gVO.setAtributo("codPais", request.getParameter("codPais"));
                gVO.setAtributo("codProvincia", request.getParameter("codProvincia"));
                gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
                gVO.setAtributo("codUsuario", Integer.toString(usuario.getIdUsuario()));
                String codVia = request.getParameter("txtCodViaOculto");
                String descVia = request.getParameter("txtNombreVia");
                String codTipoVia = request.getParameter("codTVia");
                String descTipoVia = request.getParameter("descTVia");
                String numDesde = request.getParameter("txtNumDesde");
                String letraDesde = request.getParameter("txtLetraDesde");
                String numHasta = request.getParameter("txtNumHasta");
                String letraHasta = request.getParameter("txtLetraHasta");
                String bloque = request.getParameter("txtBloque");
                String portal = request.getParameter("txtPortal");
                String escalera = request.getParameter("txtEsc");
                String planta = request.getParameter("txtPlta");
                String puerta = request.getParameter("txtPta");
                String km = request.getParameter("txtKm");
                String hm = request.getParameter("txtHm");
                String domicilio = request.getParameter("txtDomicilio");
                String poblacion = request.getParameter("txtPoblacion");
                String codPostal = request.getParameter("descPostal");
                gVO.setAtributo("codVia", codVia);
                gVO.setAtributo("descVia", descVia);
                gVO.setAtributo("codTipoVia", codTipoVia);
                gVO.setAtributo("descTipoVia", descTipoVia);
                gVO.setAtributo("numDesde", numDesde);
                gVO.setAtributo("letraDesde", letraDesde);
                gVO.setAtributo("numHasta", numHasta);
                gVO.setAtributo("letraHasta", letraHasta);
                gVO.setAtributo("bloque", bloque);
                gVO.setAtributo("portal", portal);
                gVO.setAtributo("escalera", escalera);
                gVO.setAtributo("planta", planta);
                gVO.setAtributo("puerta", puerta);
                gVO.setAtributo("km", km);
                gVO.setAtributo("hm", hm);
                gVO.setAtributo("domicilio", domicilio);
                gVO.setAtributo("poblacion", poblacion);
                gVO.setAtributo("codPostal", codPostal);
                /* anadir ECO/ESI */
                String codECO = request.getParameter("codECO");
                String codESI = request.getParameter("codESI");
                gVO.setAtributo("codECO", codECO);
                gVO.setAtributo("codESI", codESI);
                /* fin anadir ECO/ESI */
                int codLocalizacion = 0;
                codLocalizacion = terMan.grabarDomiciliosNoNormalizados(gVO, params);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el codigo de la localizacion es : " + codLocalizacion);
                }
                String codLoc = "";
                codLoc = Integer.toString(codLocalizacion);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el codigo de la localizacion es : " + codLoc);
                }
                tramExpVO.setCodLocalizacion(codLoc);
                tramExpVO.setListaDocumentos(new Vector());
                if (codLocalizacion == 0) {
                    tramExpVO.setRespOpcion("domicilioNoGrabado");
                } else if (codLocalizacion > 0) {
                    tramExpVO.setRespOpcion("domicilioGrabado");
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";

            } else if (opcion.equals("iniciarTramitesManual")) {

                String numeroExpediente = request.getParameter("numExpediente");
                String procedimiento = request.getParameter("procedimiento");
                String codMunicipio = request.getParameter("codMunicipio");
                String codProcedimiento = request.getParameter("codProcedimiento");
                String ejercicio = request.getParameter("ejercicio");
                String numero = request.getParameter("numero");
                String codTramite = request.getParameter("codTramite");
                String listaCodTramites = request.getParameter("listaCodTramites");
                Vector listaTramitesIniciar = listaTramitesSeleccionados(listaCodTramites);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("el tamaño de la lista para finalizar es : "
                            + listaTramitesIniciar.size());
                }
                String codUnidadTramitadoraUsu = request.getParameter("codUnidadTramitadoraUsu");
                tramExpVO.setVectorCodInteresados(tramExpForm.getVectorCodInteresados());
                tramExpVO.setListaEMailsAlIniciar(new Vector());
                tramExpVO.setListaEMailsAlFinalizar(new Vector());
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setNumeroExpediente(numeroExpediente);
                tramExpVO.setProcedimiento(procedimiento);
                tramExpVO.setCodMunicipio(codMunicipio);
                tramExpVO.setCodProcedimiento(codProcedimiento);
                tramExpVO.setEjercicio(ejercicio);
                tramExpVO.setNumeroExpediente(numero);
                tramExpVO.setCodTramite(codTramite);
                tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                tramExpVO.setCodOrganizacion(Integer.toString(usuario.getOrgCod()));
                tramExpVO.setCodEntidad(Integer.toString(usuario.getEntCod()));
                //tramExpVO.setCodUOR(Integer.toString(usuario.getUnidadOrgCod()));
                tramExpVO.setListaTramitesIniciar(listaTramitesIniciar);
                tramExpVO.setCodUnidadTramitadoraUsu(codUnidadTramitadoraUsu);
                String codUnidadTramitadoraManual = request.getParameter("codUnidadTramitadoraManual");
                tramExpVO.setCodUnidadTramitadoraManual(codUnidadTramitadoraManual);
                Vector listaTramitesNoIniciados = new Vector();
                m_Log.debug("TRAMITE MANUAL1 : " + tramExpVO.getCodUnidadTramitadoraManual());
                Vector DS = TramitacionExpedientesManager.getInstance().cargarDatosSuplementariosExpediente(tramExpVO, params);
                tramExpVO.setEstructuraDatosSuplExpediente((Vector) DS.elementAt(0));
                tramExpVO.setValoresDatosSuplExpediente((Vector) DS.elementAt(1));
                tramExpVO.setEstructuraDatosSuplTramites((Vector) DS.elementAt(2));
                tramExpVO.setValoresDatosSuplTramites((Vector) DS.elementAt(3));
                tramExpVO.setEstructuraDatosSuplementarios((Vector) DS.elementAt(4));
                tramExpVO.setValoresDatosSuplementarios((Vector) DS.elementAt(5));

                
                listaTramitesNoIniciados = TramitacionExpedientesManager.getInstance().iniciarTramitesManual(tramExpVO, params);
                tramExpVO.setListaTramitesPendientes(listaTramitesNoIniciados);
                if (listaTramitesNoIniciados.size() == 0) {
                    tramExpVO.setRespOpcion("iniciado");
                    Vector tramites = new Vector();
                    GeneralValueObject gVO = new GeneralValueObject();
                    gVO.setAtributo("codMunicipio", codMunicipio);
                    gVO.setAtributo("codProcedimiento", codProcedimiento);
                    gVO.setAtributo("ejercicio", ejercicio);
                    gVO.setAtributo("numero", numero);
                    gVO.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
                    gVO.setAtributo("codOrganizacion", Integer.toString(usuario.getOrgCod()));
                    gVO.setAtributo("codEntidad", Integer.toString(usuario.getEntCod()));
                    gVO.setAtributo("nombreUsuario", usuario.getNombreUsu());
                    tramites = FichaExpedienteManager.getInstance().cargaTramites(gVO, params);
                    tramExpVO.setListaTramitesExpediente(tramites);
                    
                    // Registrar la operacion
                    OperacionesExpedienteManager.getInstance().previoRegistrarIniciarTramitePrepararDatos(tramExpVO, listaTramitesIniciar, listaTramitesNoIniciados,
                            tramites, gVO, params, true);
                } else {
                    tramExpVO.setRespOpcion("tramitesPendientes");
                }
                boolean notificar = notificar(usuario, tramExpVO, fichaExpForm.getAsunto());
                if (notificar) {
                    tramExpVO.setNotificacionRealizada("si");
                } else {
                    tramExpVO.setNotificacionRealizada("no");
                }
                tramExpVO.setListaDocumentos(new Vector());
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";

            } else if ("comprobarPermisoUsuario".equals(opcion)) {
                TraductorAplicacionBean traductor = new TraductorAplicacionBean();          
                GeneralValueObject permisoUsuarioTramite = new GeneralValueObject();
                ResultadoAjax resultado = new ResultadoAjax();
                                                
                String codTramite = request.getParameter("codTramite");
                traductor.setApl_cod(usuario.getAppCod());
                traductor.setIdi_cod(usuario.getIdioma());
                
                tramExpVO.setCodTramite(codTramite);
                tramExpVO.setCodOrganizacion(request.getParameter("codOrganizacion"));
                tramExpVO.setCodProcedimiento(request.getParameter("codProcedimiento"));
                tramExpVO.setEjercicio(request.getParameter("ejercicio"));
                tramExpVO.setNumeroExpediente(request.getParameter("numExpediente"));
                String permiso = TramitacionExpedientesManager.getInstance().comprobarPermisoUsuarioIniciarTramite(tramExpVO, usuario, params);
                if("si".equals(permiso)){
                    permisoUsuarioTramite.setAtributo("posicion", request.getParameter("posicion"));
                    permisoUsuarioTramite.setAtributo("codTramite", codTramite);
                    permisoUsuarioTramite.setAtributo("permiso", permiso);
                    resultado.setStatus(0);
                    resultado.setDescStatus("El usuario tiene permiso para iniciar el trámite");
                    resultado.setResultado(permisoUsuarioTramite);
                } else {
                    resultado.setStatus(-1);
                    resultado.setDescStatus(traductor.getDescripcion("msgSinPermisoInicioTramite"));
                }
                
                // devolvemos los datos como String en formato json
                RespuestaAjaxUtils.retornarJSON(new Gson().toJson(resultado), response);
                return null;
            } else if ("grabarLocalizacion".equals(opcion)) {

                tramExpVO = tramExpForm.getTramitacionExpedientes();
                GeneralValueObject gVO = new GeneralValueObject();

                String expedientesSeleccionados = request.getParameter("expSeleccionadosMismaLocalizacion");
                m_Log.debug("Expedientes seleccionados : " + expedientesSeleccionados);
                String[] expedientes = expedientesSeleccionados.split(ALMOHADILLA);

                String ejercicio = request.getParameter("ejercicio");
                String numeroExpediente = request.getParameter("numeroExpediente");
                String codLocalizacionVieja = request.getParameter("codLocalizacion");
                //String localizacion = request.getParameter("txtDomicilio");
                String localizacion = request.getParameter("descLocalizacionNueva");
                String referencia = request.getParameter("txtRefCatastral");
                gVO.setAtributo("codMunicipio", codMunicipio);
                gVO.setAtributo("ejercicio", ejercicio);
                gVO.setAtributo("numeroExpediente", numeroExpediente);
                gVO.setAtributo("codLocalizacion", codLocalizacionVieja);
                gVO.setAtributo("localizacion", localizacion);
                gVO.setAtributo("referencia", referencia);
                gVO.setAtributo("codPais", request.getParameter("codPais"));
                gVO.setAtributo("codProvincia", request.getParameter("codProvincia"));
                gVO.setAtributo("codMunicipio", request.getParameter("codMunicipio"));
                gVO.setAtributo("codUsuario", Integer.toString(usuario.getIdUsuario()));
                gVO.setAtributo("cancelarExpRelacionadosLocalizacion", request.getParameter("cancelarExpRelacionadosLocalizacion"));

                int resultado = 0;
                TercerosManager terMan = TercerosManager.getInstance();
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("TramitacionExpedientesAction. Grabar localizacion: codLocalizacion /" + codLocalizacionVieja + "/");
                }

                if (!"".equals(codLocalizacionVieja)) {
                    m_Log.debug("TramitacionExpedientesAction. Borrar localizacion");
                    //Borrar
                    resultado = TramitacionExpedientesManager.getInstance().eliminarLocalizacion(gVO, params);
                    if (resultado == 0) {
                        tramExpVO.setRespOpcion("localizacionNoEliminado");
                    } else if (resultado > 0) {

                        // Borrar domicilio
                        boolean eliminado = false;
                        eliminado = terMan.eliminarDomiciliosNoNormalizado(gVO, params);
                    }
                } else {
                    m_Log.debug("TramitacionExpedientesAction. No Borrar localizacion");
                }


                // Grabar localizacion como domicilio no normalizado
                String codVia = request.getParameter("txtCodViaOculto");
                String descVia = request.getParameter("txtNombreVia");
                String codTipoVia = request.getParameter("codTVia");
                String descTipoVia = request.getParameter("descTVia");
                String numDesde = request.getParameter("txtNumDesde");
                String letraDesde = request.getParameter("txtLetraDesde");
                String numHasta = request.getParameter("txtNumHasta");
                String letraHasta = request.getParameter("txtLetraHasta");
                String bloque = request.getParameter("txtBloque");
                String portal = request.getParameter("txtPortal");
                String escalera = request.getParameter("txtEsc");
                String planta = request.getParameter("txtPlta");
                String puerta = request.getParameter("txtPta");
                String km = request.getParameter("txtKm");
                String hm = request.getParameter("txtHm");
                String domicilio = request.getParameter("txtDomicilio");
                String refCatastral = request.getParameter("txtRefCatastral");
                String codPostal = request.getParameter("descPostal");
                gVO.setAtributo("codVia", codVia);
                gVO.setAtributo("descVia", descVia);
                gVO.setAtributo("codTipoVia", codTipoVia);
                gVO.setAtributo("descTipoVia", descTipoVia);
                gVO.setAtributo("numDesde", numDesde);
                gVO.setAtributo("letraDesde", letraDesde);
                gVO.setAtributo("numHasta", numHasta);
                gVO.setAtributo("letraHasta", letraHasta);
                gVO.setAtributo("bloque", bloque);
                gVO.setAtributo("portal", portal);
                gVO.setAtributo("escalera", escalera);
                gVO.setAtributo("planta", planta);
                gVO.setAtributo("puerta", puerta);
                gVO.setAtributo("km", km);
                gVO.setAtributo("hm", hm);
                gVO.setAtributo("domicilio", domicilio);
                gVO.setAtributo("refCatastral", refCatastral);
                gVO.setAtributo("codPostal", codPostal);
                gVO.setAtributo("descPostal", codPostal);

                /* anadir ECO/ESI */
                String codECO = request.getParameter("codECO");
                String codESI = request.getParameter("codESI");
                gVO.setAtributo("codECO", codECO);
                gVO.setAtributo("codESI", codESI);
                String codMunExp = tramExpVO.getCodMunicipioIni();
                gVO.setAtributo("codMunExp", codMunExp);
                CodPostalesManager codPostalesManager = CodPostalesManager.getInstance();

                /* fin anadir ECO/ESI */
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("TramitacionExpedientesAction. codECO: " + codECO + " , codESI: " + codESI + " , codVia: " + codVia);
                }
                boolean grabarLoc = true;
                if ((descVia == null && domicilio == null) || ("".equals(descVia) && "".equals(domicilio))) {
                    grabarLoc = false;
                    m_Log.warn("No se esta grabando la localizacion. Es necesario especificar via o domicilio");
                }
                if (grabarLoc) {
                    //codLocalizacion = terMan.grabarDomiciliosNoNormalizados(gVO,params);
                    //resultado = terMan.grabarDomiciliosNoNormalizadosLocalizacion(gVO,params);

                    // Se graba la localización y los expedientes seleccionados por el usuario se guardan también como
                    // relacionados al expediente actual
                    resultado = terMan.grabarDomiciliosNoNormalizadosLocalizacionExpedientesRelacionados(gVO, expedientes, params);
                    gVO.setAtributo("defecto", 0);
                    if (!codPostalesManager.existeCodPostal(gVO, params)) {
                        codPostalesManager.altaCodPostal(gVO, params);
                    }
                    tramExpVO.setCodLocalizacion(Integer.toString(resultado));

                    /*
                     if (m_Log.isDebugEnabled()) m_Log.debug("El codigo de la localizacion es : " + codLocalizacion);
                     String codLoc = "";
                     codLoc = Integer.toString(codLocalizacion);
                     if (m_Log.isDebugEnabled()) m_Log.debug("El codigo de la localizacion es : " + codLoc);
                     tramExpVO.setCodLocalizacion(codLoc);

                     // Grabar la nueva localizacion.
                     gVO.setAtributo("codLocalizacion",codLoc);
                     //EL MUINICIPIO DEL EXPEDIENTE NO TIENE QUE VER CON EL MUNICIPIO DE LA LOCALIZACION
                     String codMunExp=tramExpVO.getCodMunicipioIni();
                     gVO.setAtributo("codMunExp", codMunExp);
                     resultado = 0;
                     resultado = TramitacionExpedientesManager.getInstance().grabarLocalizacion(gVO,params);
                     */

                    if (resultado == -1) {
                        tramExpVO.setRespOpcion("localizacionNoGrabado");
                    } else if (resultado > 0) {
                        tramExpVO.setRespOpcion("localizacionGrabado");
                    }
                } else {
                    m_Log.warn("No se esta grabando la localizacion.");
                    tramExpVO.setCodLocalizacion("");
                    tramExpVO.setRespOpcion("localizacionNoGrabado");
                }

                tramExpVO.setListaDocumentos(new Vector());
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";

            } else if ("eliminarLocalizacion".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                GeneralValueObject gVO = new GeneralValueObject();
                String codMunicipio = request.getParameter("codMunicipio");
                String ejercicio = request.getParameter("ejercicio");
                String numeroExpediente = request.getParameter("numeroExpediente");
                String codLocalizacion = request.getParameter("codLocalizacion");
                String localizacion = request.getParameter("txtDomicilio");
                if (!"".equals(codLocalizacion)) { // Tenia localizacion

                    gVO.setAtributo("codMunicipio", codMunicipio);
                    gVO.setAtributo("ejercicio", ejercicio);
                    gVO.setAtributo("numeroExpediente", numeroExpediente);
                    gVO.setAtributo("codLocalizacion", codLocalizacion);
                    gVO.setAtributo("localizacion", localizacion);
                    String codMunExp = tramExpVO.getCodMunicipioIni();
                    gVO.setAtributo("codMunExp", codMunExp);
                    int resultado = 0;
                    resultado = TramitacionExpedientesManager.getInstance().eliminarLocalizacion(gVO, params);
                    if (resultado == 0) {
                        tramExpVO.setRespOpcion("localizacionNoEliminado");
                    } else if (resultado > 0) {
                        // Borrar domicilio
                        boolean eliminado = false;
                        eliminado = TercerosManager.getInstance().eliminarDomiciliosNoNormalizado(gVO, params);
                        tramExpVO.setRespOpcion("localizacionEliminado");
                    }
                }
                tramExpVO.setListaDocumentos(new Vector());
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";
            } else if ("diasHabiles".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                String fechaInicio = tramExpVO.getFechaInicioPlazo();
                String ano = fechaInicio.substring(6, 10);
                Vector diasFestivos = new Vector();
                diasFestivos = GestionManager.getInstance().obtenerFestivosPorAno(ano, params);
                String plazo = tramExpVO.getPlazo();
                String fechaLimite = obtenerFechaLimite(fechaInicio, diasFestivos, plazo);
                tramExpVO.setFechaLimite(fechaLimite);
                tramExpVO.setRespOpcion("diasHabiles");
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";
            } else if ("irAlAction".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setRespOpcion("volverAlJSP");
                tramExpVO.setListaDocumentos(new Vector());
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";
            } else if ("irAlAction2".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                tramExpVO.setRespOpcion("volverAlJSP2");
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";
            } else if ("eliminarDocumentoCRD".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();

                Hashtable<String, Object> datos = new Hashtable<String, Object>();

                String codMunicipio = Integer.toString(usuario.getOrgCod());
                datos.put("codMunicipio", codMunicipio);
                datos.put("codProcedimiento", tramExpVO.getCodProcedimiento());
                datos.put("ejercicio", tramExpVO.getEjercicio());
                datos.put("numeroExpediente", tramExpVO.getNumeroExpediente());
                datos.put("codTramite", tramExpVO.getCodTramite());
                datos.put("ocurrenciaTramite", tramExpVO.getOcurrenciaTramite());
                datos.put("codDocumento", tramExpVO.getCodDocumento());
                datos.put("perteneceRelacion", "false");
                datos.put("params", params);
                datos.put("nombreDocumento", request.getParameter("nombreDocumento"));

                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio, tramExpVO.getCodProcedimiento());
                Documento doc = null;
                int tipoDocumento = -1;
                if (!almacen.isPluginGestor()) {
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                } else {
                    String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString(usuario.getOrgCod()), tramExpVO.getCodProcedimiento(), tramExpVO.getCodTramite(), params);
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(tramExpVO.getCodProcedimiento(), params);
                    datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);

                    String tipoMime = DocumentOperations.determinarTipoMimePlantilla(
                            tramExpVO.getEditorTexto(), request.getParameter("nombreDocumento"));
                    datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));
                    datos.put("tipoMime", tipoMime);
                    
                    datos.put("nombreOrganizacion", usuario.getOrg());
                    datos.put("nombreProcedimiento", nombreProcedimiento);
                    datos.put("codigoVisibleTramite", codigoVisibleTramite);
                    datos.put("numeroDocumento", tramExpVO.getCodDocumento());

                    /**
                     * SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE
                     * SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL *
                     */
                    ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");
                    String carpetaRaiz = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + usuario.getOrgCod() + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);


                    String descripcionOrganizacion = usuario.getOrg();
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(usuario.getOrgCod() + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(tramExpVO.getCodProcedimiento() + ConstantesDatos.GUION + nombreProcedimiento);
                    listaCarpetas.add(tramExpVO.getNumeroExpediente().replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
                    datos.put("listaCarpetas", listaCarpetas);
                    /**
                     * FIN *
                     */
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                }

                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);

                boolean eliminado = false;
                try {
                    eliminado = almacen.eliminarDocumento(doc);
                    OperacionesExpedienteManager.getInstance().registrarEliminacionDocumentoTramite(doc,usuario.getNombreUsu(),params);
                } catch (AlmacenDocumentoTramitacionException e) {
                    e.printStackTrace();
                    m_Log.error(this.getClass().getName() + ": " + e.getMessage());
                }

                if (eliminado) {
                    tramExpVO.setRespOpcion("eliminadoCRD");
                } else {
                    tramExpVO.setRespOpcion("noEliminadoCRD");
                }

                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";
            } else if ("cambiarEstadoFirmaDocumentoCRD".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                int resultado = 0;
                 //Se obtienen los tipos de procedimientos del fichero properties Portafirmas.properties
                ResourceBundle expPortafirmas = ResourceBundle.getBundle("Portafirmas");
                 String propiedad = usuario.getOrgCod()+"/Portafirmas";
                String portafirmas = expPortafirmas.getString(propiedad);
                //Si se trata de un Portafirmas lanbide, como se han modificado la funcionalidad, es necesario obtener los codigos del documento original y duplicado
                if (portafirmas != null && "LAN".equals(portafirmas)) {
                    String[] codigosDocumentos = tramExpVO.getCodDocumento().split("___");
                    tramExpVO.setCodDocumento(codigosDocumentos[0]);
                    if (codigosDocumentos[1] != null && !"".equals(codigosDocumentos[1].trim())) {
                        tramExpVO.setCodigoDocumentoAnterior(codigosDocumentos[1]);
                    }
                    m_Log.debug("tramExpVO.getCodDocumento() vale " + tramExpVO.getCodDocumento());
                    m_Log.debug("tramExpVO.getCodigoDocumentoAnterior() vale " + tramExpVO.getCodigoDocumentoAnterior());
                }
                
                 m_Log.debug("usuario.getIdUsuario() vale " + usuario.getIdUsuario());
                 m_Log.debug("usuario.getNombreUsu() vale " + usuario.getNombreUsu());
                
                resultado = TramitacionExpedientesManager.getInstance().cambiarEstadoFirmaDocumentoCRD(tramExpVO, usuario.getIdUsuario(), portafirmas, params);
                if (resultado > 0) {
                    tramExpVO.setRespOpcion("cambiadoEstadoFirmaDocumentoCRD");
                    /**
                     * fernando.bermudez Envio de mail al usuario no tramitador
                     */
                    Config configPortafirmas = ConfigServiceHelper.getConfig("Portafirmas");
                    String enviarNotificacion = configPortafirmas.getString(
                            String.format("%d/PortafirmasExterno/envioNotificacionFirma", usuario.getOrgCod()));
                    if (ConstantesDatos.SI.equalsIgnoreCase(enviarNotificacion)) {
                        m_Log.debug("Se enviara correo de notificacion a un usuario");
                    boolean enviado = mailToUsuarioNoTramitador(tramExpVO, params);
						if (!enviado) {
							tramExpVO.setRespOpcion("cambiadoEstadoFirmaDocumentoCRDNoEnviadoMail");
							tramExpForm.setRespOpcion("cambiadoEstadoFirmaDocumentoCRDNoEnviadoMail");
						}
                    }
                } else {
                    tramExpVO.setRespOpcion("noCambiadoEstadoFirmaDocumentoCRD");
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";
            } else if ("actualizarTablaDocumentos".equals(opcion)) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                TramitacionExpedientesManager.getInstance().getListaDocumentosCronologia(tramExpVO, params);
                tramExpVO.setRespOpcion("actualizarTablaDocumentos");
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";
            } else if ("verInteresados".equals(opcion)) {
                String codMunicipio = request.getParameter("codMun");
                String codProcedimiento = request.getParameter("codProc");
                String numeroExpediente = request.getParameter("nCS");
                String ejercicio = request.getParameter("eje");
                GeneralValueObject g = new GeneralValueObject();
                g.setAtributo("codMunicipio", codMunicipio);
                g.setAtributo("codProcedimiento", codProcedimiento);
                g.setAtributo("numeroExpediente", numeroExpediente);
                g.setAtributo("numero", numeroExpediente);
                g.setAtributo("ejercicio", ejercicio);
                g.setAtributo("codRol", "");
                Vector listaInteresados = TramitacionExpedientesManager.getInstance().getListaInteresados(g, params);
                // Si no se realiza esto se está perdiendo información, hay que obtener datos de la tramitacion y volverlos a poner
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                // Fin de la linea añadida
                tramExpVO.setListaInteresados(listaInteresados);
                Vector listaRoles = new Vector();
                listaRoles = InteresadosManager.getInstance().getListaRoles(g, params);
                tramExpVO.setListaRoles(listaRoles);
                tramExpForm.setTramitacionExpedientes(tramExpVO);
            } else if ("actualizarTablaInteresados".equals(opcion)) {
                String codMunicipio = request.getParameter("codMunicipio");
                String codProcedimiento = request.getParameter("codProcedimiento");
                String numeroExpediente = request.getParameter("numeroExpediente");
                String ejercicio = request.getParameter("ejercicio");
                String codRol = request.getParameter("codRol");
                GeneralValueObject g = new GeneralValueObject();
                g.setAtributo("codMunicipio", codMunicipio);
                g.setAtributo("codProcedimiento", codProcedimiento);
                g.setAtributo("numeroExpediente", numeroExpediente);
                g.setAtributo("numero", numeroExpediente);
                g.setAtributo("ejercicio", ejercicio);
                g.setAtributo("codRol", codRol);
                Vector listaInteresados = TramitacionExpedientesManager.getInstance().getListaInteresados(g, params);
                tramExpVO.setListaInteresados(listaInteresados);
                tramExpVO.setListaDocumentos(new Vector());
                tramExpVO.setRespOpcion("actualizarTablaInteresados");
                tramExpForm.setTramitacionExpedientes(tramExpVO);
                opcion = "grabarTramite";
            } else if (opcion.equals("listaAjuntosPDFTramite")) {
                //buscar los formularios que se pueden agregar como adjuntos
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                cargarListaAjuntosPDFTramite(tramExpVO);
                tramExpForm.setTramitacionExpedientes(tramExpVO);
            } else if (opcion.equals("eliminarFormPDF")) {
                tramExpVO = tramExpForm.getTramitacionExpedientes();
                //eliminar el formulario
                if (eliminarFormPDF(tramExpForm.getCodFormPDF())) {
                    //rehacer la lista de formularios
                    cargarFormsPDF(tramExpVO);
                    tramExpVO.setRespOpcion("eliminadoFormPDF");
                } else {
                    tramExpVO.setRespOpcion("noEliminadoFormPDF");
                }
                tramExpForm.setTramitacionExpedientes(tramExpVO);

            } else if (opcion.equals("listaUnidadesPosibles")) {
                String modoSeleccion = request.getParameter("modoSeleccion");
                Vector<UORDTO> listaUtrs = null;

                if (modoSeleccion.equals(ConstantesDatos.TRA_UTR_CUALQUIERA)) {
                    // Se recuperan las uors en forma de árbol
                    ArbolImpl arbolUors = UORsManager.getInstance().getArbolUORs(false,false, false, params);
                    m_Log.debug("Arbol Recuperadas " + (arbolUors.contarNodos() - 1) + " UORs en el árbol");
                    listaUtrs = UORsManager.getInstance().getListaUORs(false,params);

                    session.setAttribute("listaUors", listaUtrs);
                    session.setAttribute("arbolUorsTramitadoras", arbolUors);
                    opcion = "listaUnidadesCualquieraBuscador";
                } else if (modoSeleccion.equals(ConstantesDatos.TRA_UTR_INICIA)) {
                    listaUtrs = UORsManager.getInstance().getListaUORs(false,params);
                } else {
                    int codTramite = Integer.parseInt(request.getParameter("codigoTramite"));
                    int codOrg = Integer.parseInt(tramExpForm.getCodMunicipio());
                    String codProc = tramExpForm.getCodProcedimiento();
                    listaUtrs = DefinicionTramitesManager.getInstance().getUTRByTramite(codOrg, codProc, codTramite, params);
                }

                tramExpForm.setUorsTramitacion(listaUtrs);

                /*
                 if (modoSeleccion.equals(ConstantesDatos.TRA_UTR_CUALQUIERA) || modoSeleccion.equals(ConstantesDatos.TRA_UTR_INICIA)) {
                 listaUtrs = UORsManager.getInstance().getListaUORs(false,params);
                 } else {
                 int codTramite = Integer.parseInt(request.getParameter("codigoTramite"));
                 int codOrg = Integer.parseInt(tramExpForm.getCodMunicipio());
                 String codProc = tramExpForm.getCodProcedimiento();
                 listaUtrs = DefinicionTramitesManager.getInstance().getUTRByTramite(codOrg, codProc, codTramite, params);
                 }
                 tramExpForm.setUorsTramitacion(listaUtrs);
                 */
            } else if (opcion.equals("cargarTramiteIniciado")) {
                // #212448 opcion añadida para recuperar los datos del tramite iniciado para cargarlo directamente
                String numExpediente = (String) request.getParameter("numExpediente");
                String codTramite = (String) request.getParameter("codTramite");
                String[] partes = numExpediente.split("/");
                String codProc = partes[1];
                String ejercicio = partes[0];
                m_Log.debug("************* DATOS -> numExpediente: " + numExpediente + " - codTramite: " + codTramite);

                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("codMunicipio", String.valueOf(usuario.getOrgCod()));
                gVO.setAtributo("codOrganizacion", String.valueOf(usuario.getOrgCod()));
                gVO.setAtributo("codProcedimiento", codProc);
                gVO.setAtributo("ejercicio", ejercicio);
                gVO.setAtributo("numero", numExpediente);
                gVO.setAtributo("usuario", String.valueOf(usuario.getIdUsuario()));
                gVO.setAtributo("codEntidad", String.valueOf(usuario.getEntCod()));
                gVO.setAtributo("expHistorico", "false");

                gVO = TramitacionExpedientesManager.getInstance().getDatosTramiteIniciado(codTramite, gVO, params);

                // devolvemos los datos como String en formato json
                RespuestaAjaxUtils.retornarJSON(new Gson().toJson(gVO), response);
                return null;

            } else if ("ordenarArbolUORCodigo".equalsIgnoreCase(request.getParameter("opcion"))) {
                // Se ordena el árbol de uors por código
                Vector uors = UORsManager.getInstance().getListaUORsPorNombre(false,params);
                m_Log.debug("Recuperadas " + uors.size() + " UORs");
                //registroForm.setListaNuevasUORs(nuevasUOR);
                //arbol de jerarquía de uors
                ArbolImpl arboluors = UORsManager.getInstance().getArbolUORs(false,false, false, params);
                m_Log.debug("Recuperadas " + (arboluors.contarNodos() - 1) + " UORs en el árbol");

                // Se guardan en sesión la lista de uors y el árbol
                session.setAttribute("listaUors", uors);
                session.setAttribute("arbolUorsTramitadoras", arboluors);
                request.setAttribute("ordenUor", "codigo");
                //===============================
                opcion = "listaUnidadesCualquieraBuscador";
            } else if ("ordenarArbolUORNombre".equalsIgnoreCase(request.getParameter("opcion"))) {
                // Se ordena el árbol de uors por nombre
                Vector uors = UORsManager.getInstance().getListaUORsPorNombre(false,params);
                m_Log.debug("Recuperadas " + uors.size() + " UORs");
                //registroForm.setListaNuevasUORs(nuevasUOR);
                //arbol de jerarquía de uors
                ArbolImpl arboluors = UORsManager.getInstance().getArbolUORs(false,false, true, params);
                m_Log.debug("Recuperadas " + (arboluors.contarNodos() - 1) + " UORs en el árbol");

                // Se guardan en sesión la lista de uors y el árbol
                session.setAttribute("listaUors", uors);
                session.setAttribute("arbolUorsTramitadoras", arboluors);
                request.setAttribute("ordenUor", "nombre");
                //===============================
                opcion = "listaUnidadesCualquieraBuscador";
            } else if (opcion.equals("recuperaUnicaUnidadTramite")) {
                String modoSeleccion = request.getParameter("modoSeleccion");
                m_Log.debug("TramitacionExpedientesAction opcion = " + opcion + " modoSeleccion: " + modoSeleccion);
                Vector<UORDTO> listaUtrs = null;
                if (modoSeleccion.equals(ConstantesDatos.TRA_UTR_CUALQUIERA)) {
                    listaUtrs = UORsManager.getInstance().getListaUORs(false,params);
                } else {
                    int codTramite = Integer.parseInt(request.getParameter("codigoTramite"));
                    int codOrg = Integer.parseInt(tramExpForm.getCodMunicipio());
                    String codProc = tramExpForm.getCodProcedimiento();
                    listaUtrs = DefinicionTramitesManager.getInstance().getUTRByTramite(codOrg, codProc, codTramite, params);
                    m_Log.debug("TramitacionExpedientesAction listaUtrs size: " + listaUtrs.size());
                    String uorCod = "";
                    if (listaUtrs != null && listaUtrs.size() == 1) {
                        UORDTO uorTO = (UORDTO) listaUtrs.get(0);
                        uorCod = uorTO.getUor_cod();
                    }

                    String salida = listaUtrs.size() + "&" + uorCod;
                    m_Log.debug("TramitacionExpedientesAction uorCod: " + uorCod);
                    PrintWriter out = response.getWriter();
                    response.setContentType("text/html");
                    out.println(salida);
                }
            } else if (opcion.equals("recuperaUnidadesOrganicasTramite")) {
                // Se recuperan del parámetro códigos todos los códigos de trámite para recuperar sus
                // posibles unidades tramitadoras con la que asignarselas al trámite.
                String codigos = request.getParameter("codigos");
                int codOrg = Integer.parseInt(tramExpForm.getCodMunicipio());
                String codProc = tramExpForm.getCodProcedimiento();
                String ALMOHADILLA = "#";
                String SEPARADOR = "-";

                m_Log.debug("TramitacionExpedientesAction opcion = " + opcion + " codigos: " + codigos);
                m_Log.debug("TramitacionExpedientesAction opcion = " + opcion + " codProc: " + codProc);
                m_Log.debug("TramitacionExpedientesAction opcion = " + opcion + " codOrg: " + codOrg);

                String[] lCodigos = codigos.split("-");
                String salida = "";
                m_Log.debug("TramitacionExpedientesAction opcion = " + opcion + " total de  " + lCodigos.length + " a tratar");
                for (int i = 0; i < lCodigos.length; i++) {
                    int codTramite = Integer.parseInt(lCodigos[i]);
                    m_Log.debug("***** TramitacionExpedientesAction tratando codTramite = " + codTramite);
                    //Vector<UORDTO> listaUtrs = DefinicionTramitesManager.getInstance().getUTRByTramite(codOrg, codProc, codTramite, params);

                    int tipo = TramitacionManager.getInstance().getTipoUnidadTramitadoraTramite(codProc, lCodigos[i], usuario.getOrgCod(), params);
                    m_Log.debug("***** TramitacionExpedientesAction tratando codTramite = " + codTramite + " y tipo = " + tipo);
                    Vector<UORDTO> listaUtrs = new Vector<UORDTO>();
                    if (tipo == 0) // Unidad tramite de tipo otras;
                    {
                        listaUtrs = DefinicionTramitesManager.getInstance().getUTRByTramite(codOrg, codProc, codTramite, params);
                    }
                    if (tipo == 1 || tipo == 3) //listaUtrs = TramitacionManager.getInstance().getTodasUnidadesOrganizativas(params);
                    {
                        listaUtrs = UORsManager.getInstance().getListaUORs(false,params);
                    }

                    m_Log.debug("***** TramitacionExpedientesAction tratando codTramite = " + codTramite + " y con posibles unidades : " + listaUtrs.size());

                    if (listaUtrs.size() == 1) {
                        m_Log.debug("Hay código");
                        salida += listaUtrs.get(0).getUor_cod();
                    }

                    if (listaUtrs.size() > 1) {
                        salida += "VARIOS";
                    }

                    if (listaUtrs.size() == 0) {
                        salida += "NO";
                    }

                    salida += SEPARADOR;
                }//for

                m_Log.debug("TramitacionExpedientesAction salida a devolver para recuperar por ajax: " + salida);
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                out.println(salida);
            } else if (opcion.equals("recuperarNombreTramiteInicio")) {
                // Se recupera el nombre del trámite de inicio de un determinado procedimiento
                String codProc = request.getParameter("codProc");
                m_Log.debug("TramitacionExpedientesAction opcion = " + opcion + " codigos: " + codProc);

                String salida = TramitesExpedientesManager.getInstance().getDescripcionTramInicio(codProc, params);

                m_Log.debug("TramitacionExpedientesAction salida a devolver por ajax: " + salida);
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                out.println(salida);
            } else if (opcion.equals("comprobarCondicionesEntrada")) {
                // Comprueba si se verifican las condiciones de entrada de un trámite
                String codTramite = request.getParameter("codTramiteComp");
                String codProc = tramExpForm.getCodProcedimiento();
                String codTramiteAFinalizar = tramExpForm.getCodTramite();

                TramitacionExpedientesValueObject teVO = new TramitacionExpedientesValueObject();
                teVO.setCodMunicipio(tramExpForm.getCodMunicipio());
                teVO.setCodOrganizacion(tramExpForm.getCodMunicipio());
                teVO.setCodProcedimiento(codProc);
                teVO.setCodTramite(codTramite);
                teVO.setEjercicio(tramExpForm.getEjercicio());
                teVO.setNumeroExpediente(tramExpForm.getNumeroExpediente());
                teVO.setCodigoTramiteFlujoSalida(codTramite);
                teVO.setCodUsuario(tramExpForm.getCodUsuario());
                teVO.setCodEntidad(Integer.toString(usuario.getEntCod()));
                teVO.setEstructuraDatosSuplExpediente(tramExpForm.getEstructuraDatosSuplExpediente());
                teVO.setValoresDatosSuplExpediente(tramExpForm.getValoresDatosSuplExpediente());
                teVO.setEstructuraDatosSuplTramites(tramExpForm.getEstructuraDatosSuplTramites());
                teVO.setValoresDatosSuplTramites(tramExpForm.getValoresDatosSuplTramites());
                Vector listaErrores = this.comprobarCondicionesEntrada(teVO, params, codTramiteAFinalizar);
                // Se guarda la lista de tramites siguientes que no

                m_Log.debug("\\\\\\\\\\ Guardando " + listaErrores.size() + " condiciones de entrada que no se cumplen \\\\\\\\\\ ");
                tramExpForm.addTramiteCondEntradaNoCumplida(codTramite, null);
                for (int i = 0; i < listaErrores.size(); i++) {
                    GeneralValueObject error = (GeneralValueObject) listaErrores.get(i);
                    m_Log.debug("\\\\\\\\\\  Se guarda en  lista de errores el error del trámite: " + (String) error.getAtributo("codTramite") + " \\\\\\\\\\");
                    m_Log.debug("\\\\\\\\\\  Se muestran condiciones que fallan del trámite \\\\\\\\\\ ");
                    tramExpForm.addTramiteCondEntradaNoCumplida((String) error.getAtributo("codTramite"), error);
                }

                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                if (listaErrores != null && listaErrores.size() >= 1) {
                    out.println("no");
                } else {
                    out.println("si");
                }
            } else if (opcion.equals("vaciarListadoErroresCondicionesEntrada")) {
                // Vacia el listado de errores del formulario de aquellos trámites que no cumplen las
                // condiciones de entrada
                tramExpForm.setListaTramitesCondEntradaNoCumplidas(new Hashtable<String, GeneralValueObject>());

                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                out.println("si");
            } /**
             * ***** ORIGINAL else
             * if(opcion.equals("chequeoDocumentosTramitacionFirmados")) {
             * m_Log.debug("Entro en chequeoDocumentosTramitacionFirmados");
             *
             *
             * String numeroExpediente = request.getParameter("numExpediente");
             * String procedimiento = request.getParameter("procedimiento");
             * String codMun = request.getParameter("codMunicipio"); String
             * codProcedimiento = request.getParameter("codProcedimiento");
             * String ejercicio = request.getParameter("ejercicio"); String
             * numero = request.getParameter("numero"); String codTramite =
             * request.getParameter("codTramite"); String ocurrenciaTramite =
             * request.getParameter("ocurrenciaTramite"); String
             * codUnidadTramitadoraTram =
             * request.getParameter("codUnidadTramitadoraTram");
             *
             * NotificacionVO notificacionVO=new NotificacionVO();
             * notificacionVO.setNumExpediente(numero);
             * notificacionVO.setCodigoMunicipio(Integer.parseInt(codMun));
             * notificacionVO.setCodigoProcedimiento(codProcedimiento);
             * notificacionVO.setCodigoTramite(Integer.parseInt(codTramite));
             * notificacionVO.setOcurrenciaTramite(Integer.parseInt(ocurrenciaTramite));
             * notificacionVO.setEjercicio(Integer.parseInt(ejercicio));
             *
             * try{ PluginNotificacion pluginNotificacion =
             * FactoriaPluginNotificacion.getImpl(Integer.toString(usuario.getOrgCod()));
             *
             *
             *
             * boolean existenDocumentos=false;
             * existenDocumentos=pluginNotificacion.existenDocumentosFirmados(notificacionVO,
             * params);
             *
             * boolean existenInteresados=false;
             * existenInteresados=pluginNotificacion.existenInteresadosAdmitenNotificacion(notificacionVO,
             * params);
             *
             * if
             * (existenDocumentos==true)request.setAttribute("existenDocumentos","si");
             * else request.setAttribute("existenDocumentos","no"); if
             * (existenInteresados==true)request.setAttribute("existenInteresados","si");
             * else request.setAttribute("existenInteresados","no");
             *
             * notificacionVO=pluginNotificacion.getNotificacion(notificacionVO,
             * params); String
             * estadoNotif=notificacionVO.getEstadoNotificacion();
             *
             * m_Log.debug("\n\n Estado notificacion
             * "+notificacionVO.getEstadoNotificacion()); m_Log.debug("\n\n
             * Estado notificacion "+estadoNotif); if("E".equals(estadoNotif)) {
             *
             * request.setAttribute("estadoNotif","enviada"); } else { String
             * url=pluginNotificacion.getUrlPantallaDatosNotificacion();
             *
             * request.setAttribute("codTramite",codTramite);
             * request.setAttribute("ocurrenciaTramite",ocurrenciaTramite);
             * request.setAttribute("codUnidadTramitadoraTram",codUnidadTramitadoraTram);
             * request.setAttribute("urlPantallaDatosNotificacion",url);
             *
             * }
             * opcion="chequeoDocumentosTramitacionFirmados";
             *
             * }catch (Exception cnfe) { m_Log.error("Al crear la factoria");
             * cnfe.printStackTrace(); } }
             */
            else if (opcion.equals("chequeoDocumentosTramitacionFirmados")) {
                m_Log.debug("Entro en chequeoDocumentosTramitacionFirmados");


                String numeroExpediente = request.getParameter("numExpediente");
                String procedimiento = request.getParameter("procedimiento");
                String codMun = request.getParameter("codMunicipio");
                String codProcedimiento = request.getParameter("codProcedimiento");
                String ejercicio = request.getParameter("ejercicio");
                String numero = request.getParameter("numero");
                String codTramite = request.getParameter("codTramite");
                String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
                String codUnidadTramitadoraTram = request.getParameter("codUnidadTramitadoraTram");

                NotificacionVO notificacionVO = new NotificacionVO();
                notificacionVO.setNumExpediente(numero);
                notificacionVO.setCodigoMunicipio(Integer.parseInt(codMun));
                notificacionVO.setCodigoProcedimiento(codProcedimiento);
                notificacionVO.setCodigoTramite(Integer.parseInt(codTramite));
                notificacionVO.setOcurrenciaTramite(Integer.parseInt(ocurrenciaTramite));
                notificacionVO.setEjercicio(Integer.parseInt(ejercicio));

                try {
                    PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(usuario.getOrgCod()));

                    /**
                     * boolean existenDocumentos=false;
                     * existenDocumentos=pluginNotificacion.existenDocumentosFirmados(notificacionVO,
                     * params);
                     */
                    /**
                     * boolean existenInteresados=false;
                     * existenInteresados=pluginNotificacion.existenInteresadosAdmitenNotificacion(notificacionVO,
                     * params);
                     */
                    /*
                     if (existenDocumentos==true)request.setAttribute("existenDocumentos","si");
                     else request.setAttribute("existenDocumentos","no");
                     * 
                     */
                    /*
                     if (existenInteresados==true)request.setAttribute("existenInteresados","si");
                     else request.setAttribute("existenInteresados","no");
                     **/
                    
                     boolean existenDocumentosPendientesFirma=false;
                     try{
                     existenDocumentosPendientesFirma=pluginNotificacion.existenDocumentosPendientesFirma(notificacionVO, params);
                     }catch (Exception e)
                     {
                         existenDocumentosPendientesFirma=false;
                     }
                    
                      if (existenDocumentosPendientesFirma==true)request.setAttribute("existenDocumentosPendientesFirma","si");
                     else request.setAttribute("existenDocumentosPendientesFirma","no");
                    
                    notificacionVO = pluginNotificacion.getNotificacion(notificacionVO, params);
                    String estadoNotif = notificacionVO.getEstadoNotificacion();

                    m_Log.debug("\n\n Estado notificacion " + notificacionVO.getEstadoNotificacion());
                    m_Log.debug("\n\n Estado notificacion " + estadoNotif);
                    /*
                     if("E".equals(estadoNotif))
                     {
                        
                     request.setAttribute("estadoNotif","enviada");
                     }
                     else
                     { */
                    String url = pluginNotificacion.getUrlPantallaDatosNotificacion();

                    request.setAttribute("codTramite", codTramite);
                    request.setAttribute("ocurrenciaTramite", ocurrenciaTramite);
                    request.setAttribute("codUnidadTramitadoraTram", codUnidadTramitadoraTram);
                    request.setAttribute("urlPantallaDatosNotificacion", url);
                    //}

                } catch (Exception cnfe) {
                    m_Log.error("Al crear la factoria");
                    cnfe.printStackTrace();
                }
            } else if ("ejecutarTareasInicioPendientes".equals(opcion)) {
                /**
                 * ** SE EJECUTAN LAS TAREAS PENDIENTES DE INICIO DE UNA
                 * OCURRENCIA DE UN TRÁMITE DE UN DETERMINADO EXPEDIENTE **
                 */
                m_Log.debug(" =========> Ejecución de las tareas de inicio pendientes ");
                int codTramite = Integer.parseInt(request.getParameter("codTramite"));
                int ocurrencia = Integer.parseInt(request.getParameter("ocurrencia"));
                int codMunicipio = Integer.parseInt(request.getParameter("codMunicipio"));
                int ejercicio = Integer.parseInt(request.getParameter("ejercicio"));
                String numero = request.getParameter("numero");
                String codProcedimiento = request.getParameter("codProcedimiento");

                m_Log.debug(" codTramite: " + codTramite + ",ocurrencia: " + ocurrencia + ",codMunicipio: " + codMunicipio + ",numero: " + numero + ",codProcedimiento: " + codProcedimiento);
                 String mensaje = ModuloIntegracionExternoManager.getInstance().ejecutarTareasPendientesInicioTramite(codMunicipio, codTramite, ocurrencia, ejercicio, codProcedimiento, numero, tramExpVO.getOrigenLlamada(), params);
                m_Log.debug("mensaje de la ejecución de las tareas: " + mensaje);

                String msgError = "";

                if (mensaje != null && !"".equals(mensaje)) {
                    TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                    traductor.setApl_cod(4);
                    traductor.setIdi_cod(usuario.getIdioma());

                    String cab = traductor.getDescripcion("msgEjecuTareasPendientesFallo1");
                    String pie = traductor.getDescripcion("msgEjecuTareasPendientesFallo2");
                    msgError = cab + " " + mensaje + "" + pie;
                }

                request.setAttribute("mensaje_error", msgError);
                // Se recuperan las tareas pendientes de inicio que pueda tener el trámite, si las hay
                ArrayList<TareasPendientesInicioTramiteVO> tareas = ModuloIntegracionExternoManager.getInstance().getTareasPendientesInicio(codMunicipio, codTramite, ocurrencia, numero, usuario.getIdioma(), params);
                tramExpForm.setTareasPendientesInicioTramite(tareas);
                opcion = "ocultoTareasPendientesInicio";
            } else if ("numeroInteresadosExpediente".equals(opcion)) {
                // Se recibe petición de comprobación de cuantos interesados tiene el expediente
                String numExpediente = request.getParameter("numExpediente");

                int numero = TramitacionExpedientesManager.getInstance().getNumInteresadosExpediente(usuario.getOrgCod(), numExpediente, params);
                try {
                    // Se envía la salida                    
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.print("numero=" + numero);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if ("comprobarPluginTramitacionExterna".equals(opcion)) {
                /**
                 * SE COMPRUEBA SI EL TRÁMITE TIENE UN PLUGIN DE TRAMITACIÓN
                 * EXTERNO *
                 */
                String codTramite = request.getParameter("codTramite");
                String codProcedimiento = request.getParameter("codProcedimiento");
                String codOrganizacion = request.getParameter("codOrganizacion");
                String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
                String fechaInicio = request.getParameter("fechaInicioTramite");
                String fechaFin = request.getParameter("fechaFinTramite");
                String codUorTramitadora = request.getParameter("codUorTramitadora");
                String numExpediente = request.getParameter("numExpediente");
                String ejercicio = request.getParameter("ejercicio");

                m_Log.debug("   codTramite " + codTramite + ", codProcedimiento: " + codProcedimiento + ", codOrganizacion: " + codOrganizacion + ",ocurrenciaTramite: " + ocurrenciaTramite);
                m_Log.debug("   fechaInicio: " + fechaInicio + ", fechaFin: " + fechaFin + ",codUorTramitadora: " + codUorTramitadora + ",numExpediente: " + numExpediente + ", ejercicio: " + ejercicio);

                String url = "";
                try {
                    if (codTramite != null && !"".equals(codTramite) && codProcedimiento != null && !"".equals(codProcedimiento) && codProcedimiento != null && !"".equals(codProcedimiento)
                            && ocurrenciaTramite != null && !"".equals(ocurrenciaTramite) && fechaInicio != null && !"".equals(fechaInicio) && codUorTramitadora != null
                            && !"".equals(codUorTramitadora) && numExpediente != null && !"".equals(numExpediente)) {

                        DefinicionTramitesValueObject dfVO = DefinicionTramitesManager.getInstance().getInfoPantallaExternaTramite(codOrganizacion, codTramite, codProcedimiento, params);
                        if (dfVO != null) {

                            TramitacionExternaCargador cargador = TramitacionExternaCargador.getInstance();
                            //public TramitacionExternaBase getPlugin(int codOrganizacion,String nombrePlugin,String codUrl,String url,String implClass)
                            TramitacionExternaBase ext = cargador.getPlugin(Integer.parseInt(codOrganizacion), dfVO.getCodPluginPantallaTramitacionExterna(), dfVO.getCodUrlPluginPantallaTramitacionExterna(),
                                    dfVO.getUrlPluginPantallaTramitacionExterna(), dfVO.getImplClassPluginPantallaTramitacionExterna());

                            if (ext != null) {
                                ext.setCodOrganizacion(codOrganizacion);
                                ext.setCodTramite(codTramite);
                                ext.setCodProcedimiento(codProcedimiento);
                                ext.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                                ext.setOcurrenciaTramite(ocurrenciaTramite);
                                ext.setCodUorTramitadora(codUorTramitadora);
                                ext.setNumExpediente(numExpediente);
                                ext.setEjercicio(ejercicio);

                                if (fechaInicio != null && !"".equals(fechaInicio)) {
                                    if (fechaFin != null && !"".equals(fechaFin)) {
                                        ext.setTramiteAbierto(false);
                                    }// if
                                    else {
                                        ext.setTramiteAbierto(true);
                                    }
                                }// if

                                String urlExterna = ext.getUrlLlamadaCompleta();

                                m_Log.debug("***************** url: " + urlExterna);
                                if (urlExterna != null && !"".equals(urlExterna)) {
                                    url = urlExterna;
                                }

                            }
                        }

                    }// if
                    else {
                        m_Log.debug("No se han pasado los parámetros correctos para verificar si el trámite tiene una ficha de tramitación externa asociada");
                    }

                } catch (Exception e) {
                    m_Log.debug("TramitacionExpedientesAction error al recuperar la url de una ficha de tramitación externa: " + e.getMessage());
                }


                try {
                    // Se envía la salida                    
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.print(url);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("parametrosPluginTramitacionExterna".equals(opcion)) {
                /**
                 * SE COMPRUEBA SI EL TRÁMITE TIENE UN PLUGIN DE TRAMITACIÓN
                 * EXTERNO *
                 */
                String codTramite = request.getParameter("codTramite");
                String codProcedimiento = request.getParameter("codProcedimiento");
                String codOrganizacion = request.getParameter("codOrganizacion");
                String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
                String fechaInicio = request.getParameter("fechaInicioTramite");
                String fechaFin = request.getParameter("fechaFinTramite");
                String codUorTramitadora = request.getParameter("codUorTramitadora");
                String numExpediente = request.getParameter("numExpediente");
                String ejercicio = request.getParameter("ejercicio");

                m_Log.debug("   codTramite " + codTramite + ", codProcedimiento: " + codProcedimiento + ", codOrganizacion: " + codOrganizacion + ",ocurrenciaTramite: " + ocurrenciaTramite);
                m_Log.debug("   fechaInicio: " + fechaInicio + ", fechaFin: " + fechaFin + ",codUorTramitadora: " + codUorTramitadora + ",numExpediente: " + numExpediente + ", ejercicio: " + ejercicio);


                String paramConfigVentana = "";
                try {
                    if (codTramite != null && !"".equals(codTramite) && codProcedimiento != null && !"".equals(codProcedimiento) && codProcedimiento != null && !"".equals(codProcedimiento)
                            && ocurrenciaTramite != null && !"".equals(ocurrenciaTramite) && fechaInicio != null && !"".equals(fechaInicio) && codUorTramitadora != null
                            && !"".equals(codUorTramitadora) && numExpediente != null && !"".equals(numExpediente)) {

                        DefinicionTramitesValueObject dfVO = DefinicionTramitesManager.getInstance().getInfoPantallaExternaTramite(codOrganizacion, codTramite, codProcedimiento, params);
                        if (dfVO != null) {

                            TramitacionExternaCargador cargador = TramitacionExternaCargador.getInstance();
                            //public TramitacionExternaBase getPlugin(int codOrganizacion,String nombrePlugin,String codUrl,String url,String implClass)
                            TramitacionExternaBase ext = cargador.getPlugin(Integer.parseInt(codOrganizacion), dfVO.getCodPluginPantallaTramitacionExterna(), dfVO.getCodUrlPluginPantallaTramitacionExterna(),
                                    dfVO.getUrlPluginPantallaTramitacionExterna(), dfVO.getImplClassPluginPantallaTramitacionExterna());

                            if (ext != null) {
                                ext.setCodOrganizacion(codOrganizacion);
                                ext.setCodTramite(codTramite);
                                ext.setCodProcedimiento(codProcedimiento);
                                ext.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                                ext.setOcurrenciaTramite(ocurrenciaTramite);
                                ext.setCodUorTramitadora(codUorTramitadora);
                                ext.setNumExpediente(numExpediente);
                                ext.setEjercicio(ejercicio);

                                if (fechaInicio != null && !"".equals(fechaInicio)) {
                                    if (fechaFin != null && !"".equals(fechaFin)) {
                                        ext.setTramiteAbierto(false);
                                    }// if
                                    else {
                                        ext.setTramiteAbierto(true);
                                    }
                                }// if

                                String configVentana = ext.getParametrosVentana();
                                m_Log.debug("***************** configVentana: " + configVentana);
                                if (configVentana != null && !"".equals(configVentana)) {
                                    paramConfigVentana = configVentana;
                                }

                            }
                        }

                    }// if
                    else {
                        m_Log.debug("No se han pasado los parámetros correctos para verificar si el trámite tiene una ficha de tramitación externa asociada");
                    }

                } catch (Exception e) {
                    m_Log.debug("TramitacionExpedientesAction error al recuperar la url de una ficha de tramitación externa: " + e.getMessage());
                }


                try {
                    // Se envía la salida
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    out.print(paramConfigVentana);
                    out.flush();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        } else { // No hay usuario.
            m_Log.warn("MantAnotacionRegistroAction --> no hay usuario");
            opcion = "no_usuario";
        }

        /* Redirigimos al JSP de salida*/
        m_Log.info("<==================== TramitacionExpedientesAction ====================");
        return (mapping.findForward(opcion));

    }

    /**
     * Pone en el VO el mensaje adecuado con el error que devuelve un servicio
     * web en una WSException
     *
     * @param tramExpVO
     * @param wse
     */
    private void ponerMensajeFalloSW(
            TramitacionExpedientesValueObject tramExpVO, WSException wse) {
        String msg = "Fallo en ejecución de servicio web ";
        if (wse.isMandatoryExecution()) {
            msg += "obligatorio. ";
        } else {
            msg += "no obligatorio. ";
        }
        tramExpVO.setMensajeSW(msg + wse.getMessage());
    }

    private void ponerMensajeFalloSW(TramitacionExpedientesValueObject tramExpVO, EjecucionSWException eswe) {
        String msg = "Fallo en ejecución de servicio web obligatorio. ";
        tramExpVO.setMensajeSW(msg + eswe.getMensaje());
    }

    private String obtenerFechaLimite(String fechaInicio, Vector diasFestivos, String plazo) {
        Vector dF = new Vector();
        for (int j = 0; j < diasFestivos.size(); j++) {
            Fecha f_fecha = new Fecha();
            String f_string = new String();
            f_string = (String) diasFestivos.elementAt(j);
            java.util.Date d_date = f_fecha.obtenerDate(f_string);
            dF.addElement(d_date);
        }
        Calendar fecha = Calendar.getInstance();
        Fecha f = new Fecha();
        java.util.Date d = f.obtenerDate(fechaInicio);
        fecha.setTime(d);
        int i = 0;
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("el plazo es : " + plazo);
        }
        while (i < Integer.parseInt(plazo)) {
            String incremento = "si";
            fecha.add(Calendar.DATE, 1);
            d = fecha.getTime();
            if (d.getDay() == 0) {
                incremento = "no";
            }
            for (int j = 0; j < dF.size(); j++) {
                java.util.Date diasFest = (java.util.Date) dF.elementAt(j);
                if (d.equals(diasFest)) {
                    incremento = "no";
                }
            }
            if ("si".equals(incremento)) {
                i++;
            }
        }
        String fechaLimite = f.construirFecha(d);
        m_Log.debug(fechaLimite + " limite como String");
        return fechaLimite;
    }

    private GeneralValueObject grabarDatosSuplementarios2(TramitacionExpedientesForm tEF, 
            TramitacionExpedientesValueObject tramExpForm, HttpServletRequest request) {

        String prefijoCampo = "T_" + tEF.getCodTramite() + "_" + tEF.getOcurrenciaTramite() + "_";
        String sufijoFichero = "_" + tEF.getOcurrenciaTramite();

        m_Log.debug("PREFIJO RECUPERACIÓN DE CAMPOS: " + prefijoCampo);
        m_Log.debug("SUFIJO RECUPRACION DE CAMPOS: " + sufijoFichero);

        GeneralValueObject gVO = new GeneralValueObject();
        Vector estructuraDatosSuplementarios = tEF.getEstructuraDatosSuplementarios();
        GeneralValueObject listaFicheros;
        listaFicheros = tEF.getListaFicheros();
        GeneralValueObject listaTiposFicheros;
        listaTiposFicheros = tEF.getListaTiposFicheros();
        Config m_Conf = ConfigServiceHelper.getConfig("common");
        String campo = "E_PLT.CodigoPlantillaFichero";
        String tipoDatoFichero = m_Conf.getString(campo);
        m_Log.debug("Dato common.properties (fichero)... " + tipoDatoFichero);
        campo = "E_PLT.CodigoCampoDesplegable";
        String tipoDatoDesplegable = m_Conf.getString(campo);
        for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
            EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);
            gVO.setAtributo("codOrganizacion", tEF.getCodMunicipio());
            if (eC.getCodTipoDato().equals(tipoDatoFichero)) { // 5 = FICHERO
                m_Log.debug("Procesado FICHERO ... ");
                if (listaFicheros.getAtributo(eC.getCodCampo() + sufijoFichero) != "") {
                    m_Log.debug("FICHERO ........................ NO VACIO ");
                    byte[] fichero = (byte[]) listaFicheros.getAtributo(eC.getCodCampo() + sufijoFichero);
                    String tipoFichero = (String) listaTiposFicheros.getAtributo(eC.getCodCampo() + sufijoFichero);
                    gVO.setAtributo(eC.getCodCampo(), fichero);
                    if (fichero != null) {
                        String aux;
                        if (tEF.isDesdeFichaExpediente()) {
                            aux = (String) tEF.getTramitacionExpedientes().getListaNombresFicheros().getAtributo(eC.getCodCampo() + sufijoFichero);
                        } else {
                            aux = request.getParameter(eC.getCodCampo() + sufijoFichero);
                        }
                        String[] matriz = aux.split("/");
                        aux = matriz[matriz.length - 1];
                        gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", aux);
                        gVO.setAtributo(eC.getCodCampo() + "_TIPO", tipoFichero);
                    }
                } else {
                    m_Log.debug("FICHERO ........................ VACIO ");
                    gVO.setAtributo(eC.getCodCampo(), null);
                    gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", "");
                    gVO.setAtributo(eC.getCodCampo() + "_TIPO", "");
                }
                m_Log.debug("lista FICHEROs ... " + listaFicheros);
            } else if (eC.getCodTipoDato().equals(tipoDatoDesplegable)) {
                m_Log.debug("Procesado DESPLEGABLE ... ");
                gVO.setAtributo(eC.getCodCampo(), request.getParameter("cod" + prefijoCampo + eC.getCodCampo()));
                m_Log.debug("DESPLEGABLE ........................ " + request.getParameter("cod" + eC.getCodCampo()));
           } else if (eC.getCodTipoDato().equals("10")) { // DESPL. EXT.
                // #208822 ALMACENAMOS EL CODIGO DEL VALOR SELECCIONADO EN EL DESPLEGABLE
                gVO.setAtributo(eC.getCodCampo(),request.getParameter(prefijoCampo + eC.getCodCampo()));
                gVO.setAtributo(eC.getCodCampo()+"_CODSEL",request.getParameter(prefijoCampo + eC.getCodCampo() + "_CODSEL"));
            } else {
                gVO.setAtributo(eC.getCodCampo(), request.getParameter(prefijoCampo + eC.getCodCampo()));
            }
        }
        gVO.setAtributo("estDatosSuplementarios", estructuraDatosSuplementarios);
        tramExpForm.setCamposSuplementarios(gVO);

        return gVO;
    }

    private int grabarDatosSuplementarios(TramitacionExpedientesForm tEF, TramitacionExpedientesValueObject t, HttpServletRequest request, String[] params) {
        m_Log.debug("***** TramitacionExpedientesAction.grabarDatosSuplementarios entrando");
        String prefijoCampo = "T_" + t.getCodTramite() + "_" + t.getOcurrenciaTramite() + "_";
        String sufijoFichero = "_" + t.getOcurrenciaTramite();

        int res = 0;
        GeneralValueObject gVO = new GeneralValueObject();
        Vector estructuraDatosSuplementarios = new Vector();
        estructuraDatosSuplementarios = tEF.getEstructuraDatosSuplementarios();
        
        GeneralValueObject listaTiposFicheros;
        listaTiposFicheros = tEF.getListaTiposFicheros();
        GeneralValueObject listaNombresFicheros = tEF.getListaNombresFicheros();
        Config m_Conf = ConfigServiceHelper.getConfig("common");
        String campo = "E_PLT.CodigoPlantillaFichero";
        String tipoDatoFichero = m_Conf.getString(campo);
        m_Log.debug("Dato common.properties (fichero)... " + tipoDatoFichero);
        campo = "E_PLT.CodigoCampoDesplegable";
        String tipoDatoDesplegable = m_Conf.getString(campo);

        GeneralValueObject listaEstadosFicheros = tEF.getListaEstadoFicheros();
        GeneralValueObject listaRutaFicherosNuevos = tEF.getListaRutaFicherosDisco();
        GeneralValueObject listaMetadatosNuevos = tEF.getListaMetadatosFicheros();
        

        for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
            EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);

            m_Log.debug("CODIGO ... " + eC.getCodCampo());
            m_Log.debug("NOMBRE ... " + eC.getDescCampo());
            m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
            eC.setCampoActivo("activar" + prefijoCampo + eC.getCodCampo());
            m_Log.debug("ACTIVAR/DESACTIVAR ... " + eC.getCampoActivo());

            gVO.setAtributo("codOrganizacion", tEF.getCodMunicipio());

            if (eC.getCodTipoDato().equals(tipoDatoFichero)) { // 5 = FICHERO
                m_Log.debug("Procesado FICHERO ... ");

                if (listaNombresFicheros.getAtributo(eC.getCodCampo()) != "") {
                    m_Log.debug("FICHERO ........................ NO VACIO ");

                    String tipoFichero = (String) listaTiposFicheros.getAtributo(eC.getCodCampo() + sufijoFichero);
                    String aux = request.getParameter(eC.getCodCampo() + sufijoFichero);
                    String[] matriz = aux.split("/");
                    aux = matriz[matriz.length - 1];
                    gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", aux);
                    gVO.setAtributo(eC.getCodCampo() + "_TIPO", tipoFichero);

                    Integer estadoFichero = (Integer) listaEstadosFicheros.getAtributo(eC.getCodCampo() + sufijoFichero);
                    // Sólo si estado del fichero es 
                    String rutaFicheroDisco = (String) listaRutaFicherosNuevos.getAtributo(eC.getCodCampo() + sufijoFichero);

                    gVO.setAtributo(eC.getCodCampo() + "_ESTADO", estadoFichero);
                    gVO.setAtributo(eC.getCodCampo() + "_RUTA", rutaFicheroDisco);
                    // Metadatos
                    MetadatosDocumentoVO metadatos = (MetadatosDocumentoVO) listaMetadatosNuevos.getAtributoONulo(eC.getCodCampo() + sufijoFichero);
                    gVO.setAtributo(eC.getCodCampo() + "_METADATOS", metadatos);
                    //}
                } else {
                    m_Log.debug("FICHERO ........................ VACIO ");
                    gVO.setAtributo(eC.getCodCampo(), null);
                    gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", "");
                    gVO.setAtributo(eC.getCodCampo() + "_TIPO", "");
                    gVO.setAtributo(eC.getCodCampo() + "_METADATOS", null);
                }
            } else if (eC.getCodTipoDato().equals(tipoDatoDesplegable)) {
                m_Log.debug("Procesado DESPLEGABLE ... ");
                gVO.setAtributo(eC.getCodCampo(), request.getParameter("cod" + prefijoCampo + eC.getCodCampo()));
                m_Log.debug("DESPLEGABLE ........................ " + request.getParameter("cod" + eC.getCodCampo()));
            } else if (eC.getCodTipoDato().equals("3")) {
                m_Log.debug("Busqueda del tipo FECHA:" + eC.getCodTipoDato());
                m_Log.debug("Valor activar/desactivar:" + request.getParameter("activar" + prefijoCampo + eC.getCodCampo()));
				gVO.setAtributo(eC.getCampoActivo(), request.getParameter("activar" + prefijoCampo + eC.getCodCampo()));
                m_Log.debug("Valor fecha::" + request.getParameter(prefijoCampo + eC.getCodCampo()));
                gVO.setAtributo(eC.getCodCampo(), request.getParameter(prefijoCampo + eC.getCodCampo()));
            } else if(eC.getCodTipoDato().equals("10")){ // desplegable externo
                // #208822 ALMACENAMOS EL CODIGO DEL VALOR SELECCIONADO EN EL DESPLEGABLE
                gVO.setAtributo(eC.getCodCampo(),request.getParameter(prefijoCampo + eC.getCodCampo()));
                gVO.setAtributo(eC.getCodCampo()+"_CODSEL",request.getParameter(prefijoCampo + eC.getCodCampo() + "_CODSEL"));

            } else {
                gVO.setAtributo(eC.getCodCampo(), request.getParameter(prefijoCampo + eC.getCodCampo()));
            }
        }
        
        gVO.setAtributo("estDatosSuplementarios", estructuraDatosSuplementarios);
        t.setCamposSuplementarios(gVO);
        
        res = DatosSuplementariosManager.getInstance().grabarDatosSuplementariosTramite(estructuraDatosSuplementarios,gVO,params,t);
        return res;
    }

    private int grabarDatosSuplementariosFichero(TramitacionExpedientesForm tEF, TramitacionExpedientesValueObject t, HttpServletRequest request, String[] params) {
        m_Log.debug("***** TramitacionExpedientesAction.grabarDatosSuplementarios entrando");
        String prefijoCampo = "T_" + t.getCodTramite() + "_" + t.getOcurrenciaTramite() + "_";
        String sufijoFichero = "_" + t.getOcurrenciaTramite();

        int res = 0;
        GeneralValueObject gVO = new GeneralValueObject();
        Vector estructuraDatosSuplementarios = new Vector();
        estructuraDatosSuplementarios = tEF.getEstructuraDatosSuplementarios();
        Vector valoresDatosSuplementarios = new Vector();
        //GeneralValueObject listaFicheros;
        //listaFicheros = tEF.getListaFicheros();
        GeneralValueObject listaTiposFicheros;
        listaTiposFicheros = tEF.getListaTiposFicheros();
        GeneralValueObject listaNombresFicheros = tEF.getListaNombresFicheros();
        Config m_Conf = ConfigServiceHelper.getConfig("common");
        String campo = "E_PLT.CodigoPlantillaFichero";
        String tipoDatoFichero = m_Conf.getString(campo);
        m_Log.debug("Dato common.properties (fichero)... " + tipoDatoFichero);
        campo = "E_PLT.CodigoCampoDesplegable";
        String tipoDatoDesplegable = m_Conf.getString(campo);

        GeneralValueObject listaEstadosFicheros = tEF.getListaEstadoFicheros();
        GeneralValueObject listaRutaFicherosNuevos = tEF.getListaRutaFicherosDisco();
        GeneralValueObject listaMetadatosNuevos = tEF.getListaMetadatosFicheros();

        for (int i = 0; i < estructuraDatosSuplementarios.size(); i++) {
            EstructuraCampo eC = (EstructuraCampo) estructuraDatosSuplementarios.elementAt(i);

            m_Log.debug("CODIGO ... " + eC.getCodCampo());
            m_Log.debug("NOMBRE ... " + eC.getDescCampo());
            m_Log.debug("TIPO DATO ... " + eC.getCodTipoDato());
            eC.setCampoActivo("activar" + prefijoCampo + eC.getCodCampo());
            m_Log.debug("ACTIVAR/DESACTIVAR ... " + eC.getCampoActivo());

            gVO.setAtributo("codOrganizacion", tEF.getCodMunicipio());

            if (eC.getCodTipoDato().equals(tipoDatoFichero)) { // 5 = FICHERO
                m_Log.debug("Procesado FICHERO ... ");

                /*
                 if (listaFicheros.getAtributo(eC.getCodCampo() + sufijoFichero) != ""
                 && listaFicheros.getAtributo(eC.getCodCampo() + sufijoFichero) != null) { */

                if (listaNombresFicheros.getAtributo(eC.getCodCampo()) != "") {
                    m_Log.debug("FICHERO ........................ NO VACIO ");
                    //byte[] fichero = (byte[]) listaFicheros.getAtributo(eC.getCodCampo() + sufijoFichero);
                    String tipoFichero = (String) listaTiposFicheros.getAtributo(eC.getCodCampo() + sufijoFichero);
                    //gVO.setAtributo(eC.getCodCampo(),fichero);
                    //if (fichero != null) {
                    String aux = request.getParameter(eC.getCodCampo() + sufijoFichero);
                    String[] matriz = aux.split("/");
                    aux = matriz[matriz.length - 1];
                    gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", aux);
                    gVO.setAtributo(eC.getCodCampo() + "_TIPO", tipoFichero);

                    Integer estadoFichero = (Integer) listaEstadosFicheros.getAtributo(eC.getCodCampo() + sufijoFichero);
                    // Sólo si estado del fichero es 
                    String rutaFicheroDisco = (String) listaRutaFicherosNuevos.getAtributo(eC.getCodCampo() + sufijoFichero);

                    gVO.setAtributo(eC.getCodCampo() + "_ESTADO", estadoFichero);
                    gVO.setAtributo(eC.getCodCampo() + "_RUTA", rutaFicheroDisco);
                    
                    // Metadatos
                    MetadatosDocumentoVO metadatos = (MetadatosDocumentoVO) listaMetadatosNuevos.getAtributoONulo(eC.getCodCampo() + sufijoFichero);
                    boolean generarCSV = true;
                    if (t.getCodMunicipio() != null) {
                        generarCSV = CodigoSeguroVerificacionHelper.incrustarCSVenJustificante(Integer.parseInt(t.getCodMunicipio().trim()), registroConf);
                    }
                     if (generarCSV) {
                         gVO.setAtributo(eC.getCodCampo() + "_METADATOS", metadatos);
                     } else {
                         gVO.setAtributo(eC.getCodCampo() + "_METADATOS", null);
                     }
                    //}
                } else {
                    m_Log.debug("FICHERO ........................ VACIO ");
                    gVO.setAtributo(eC.getCodCampo(), null);
                    gVO.setAtributo(eC.getCodCampo() + "_NOMBRE", "");
					gVO.setAtributo(eC.getCodCampo() + "_TIPO", "");
                    gVO.setAtributo(eC.getCodCampo() + "_METADATOS", null);
                }
                //m_Log.info("lista FICHEROs ... "+listaFicheros);
            }
            valoresDatosSuplementarios.addElement(gVO);
        }
        res = DatosSuplementariosManager.getInstance().grabarDatosSuplementariosFicheroTramite(estructuraDatosSuplementarios, valoresDatosSuplementarios, params, t);
        return res;
    }

    private Vector listaTramitesSeleccionados(String listSelecc) {
        Vector lista = new Vector();
        StringTokenizer codigos = null;

        if (listSelecc != null) {
            codigos = new StringTokenizer(listSelecc, "§¥", false);

            while (codigos.hasMoreTokens()) {
                String cod = codigos.nextToken();
                TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                tEVO.setCodTramite(cod);
                tEVO.setCodigoTramiteFlujoSalida(cod);
                lista.addElement(tEVO);
                m_Log.debug("-->" + cod);
            }

        }
        return lista;
    }

    /**
     * A partir de las listas de trámites a iniciar genera el correspondiente
     * colección de objetos TramitacionExpedientesValueObject para pasar a la
     * capa modelo
     *
     * @param listaCodTramites: Códigos de los trámites a iniciar
     * @param listaModoTramites: Módo de cada trámite a iniciar
     * @param listaUtrTramites: Unidad tramitadora con la que se iniciará cada
     * trámite
     * @param listaCumpleCondicion: Contiene un si o un no para cada trámite
     * indicando si cumple o no sus condiciones de entrada
     * @param insertarUtr
     * @return Vector<TramitacionExpedientesValueObject>
     */
    private Vector<TramitacionExpedientesValueObject> listaTramitesIniciar(
            String listaCodTramites, String listaModoTramites, String listaUtrTramites, String listaCumpleCondicion, boolean insertarUtr,String[] params) {

        Vector<TramitacionExpedientesValueObject> lista =
                new Vector<TramitacionExpedientesValueObject>();
        StringTokenizer codigos = null;
        StringTokenizer modos = null;
        StringTokenizer utrs = null;
        StringTokenizer condiciones = null;


        if (listaCodTramites != null) {

            m_Log.debug("TramitacionExpedienteAction.listaTramitesIniciar listaCodTramites: " + listaCodTramites);
            m_Log.debug("TramitacionExpedienteAction.listaTramitesIniciar listaModoTramites: " + listaModoTramites);
            m_Log.debug("TramitacionExpedienteAction.listaTramitesIniciar listaUtrTramites: " + listaUtrTramites);
            m_Log.debug("TramitacionExpedienteAction.listaTramitesIniciar listaCumpleCondicion: " + listaCumpleCondicion);
            codigos = new StringTokenizer(listaCodTramites, "§¥", false);
            modos = new StringTokenizer(listaModoTramites, "§¥", false);
            utrs = new StringTokenizer(listaUtrTramites, "§¥", false);


            condiciones = new StringTokenizer(listaCumpleCondicion, "§¥", false);

            while (codigos.hasMoreTokens()) {
                String cod = codigos.nextToken();
                String modo = modos.nextToken();
                String utr = "";
                if (utrs.hasMoreTokens()) {
                    utr = utrs.nextToken();
                }
                m_Log.debug("utr: " + utr);
                m_Log.debug("insertarUtr: " + insertarUtr);
                //String utr = utrs.nextToken();
                TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                tEVO.setCodTramite(cod);
                tEVO.setModoSeleccionUnidad(modo);
                tEVO.setCodUnidadTramitadoraTram(utr);
                if (utr != "_" && insertarUtr) {
                    tEVO.setInsertarCodUnidadTramitadoraTram("si");
                }

                String condicion = condiciones.nextToken();
                if ("si".equals(condicion) || "SI".equals(condicion)) // si cumple sus condiciones de entrada no se guarda en la lista de tramites a iniciar
                {
                    if(params!=null){
                        if(utr!=null && !utr.equals("") && !utr.equals("_")){
                            ArrayList listaDatosUtr = new ArrayList(UORsDAO.getInstance().getListaUORsPorCodigo(Integer.parseInt(utr), params));	
                            //UORDTO datosUtr = (UORDTO) listaDatosUtr.get(0);	
                            if(!((UORDTO) listaDatosUtr.get(0)).getUor_estado().equals("B")){	
                                m_Log.debug("-->Tramite a iniciar " + cod + ": unidad de tramite " + utr + " DE ALTA");	
                                lista.addElement(tEVO);	
                            } else {	
                                m_Log.debug("-->Tramite a iniciar " + cod + ": unidad de tramite " + utr + " DE BAJA");	
                            }	
                        } else {	
                            lista.addElement(tEVO);	
                        }	
                    } else {	
                        lista.addElement(tEVO);	
                    }
                    m_Log.debug("-->Tramite a iniciar: " + cod + ", modo " + modo + ", unidad de tramite " + utr + " cumple condiciones entrada: " + condicion);
                } else {
                    m_Log.debug("-->Tramite a iniciar: " + cod + ", modo " + modo + ", unidad de tramite " + utr + " no cumple condiciones entrada: " + condicion + " => NO SE INICIA");
                }

            }
        }
        return lista;
    }

    /**
     * Funcion que envia un mail a un usuario no tramitador que debe firmar un
     * documento pendiente de firma, a su usuario delegado en caso de que
     * exista, al usuario delegado de este y asi sucesivamente
     */
    private boolean mailToUsuarioNoTramitador(TramitacionExpedientesValueObject tramExpVO, String[] params) {
        try {
            String requiereFirma = TramitacionExpedientesManager.getInstance().getFirmaDocumento(tramExpVO, params);
            if (requiereFirma.equals("O")) {//Si el documento requiere firma de otro usuario
                int codigoUsuario = TramitacionExpedientesManager.getInstance().getUsuarioFirma(tramExpVO, params);

                Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
                String asunto = m_ConfigTechnical.getString("mail.subject");
                String contenido = m_ConfigTechnical.getString("mail.content");
                String email = "";
                MailHelper mailHelper = new MailHelper();

                /* Reemplazos de campos en el asunto y el contenido del mensaje*/
                asunto = asunto.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                String documento = null;
                if (asunto.indexOf("@documento@") != -1) {
                    Vector documentos = tramExpVO.getListaDocumentos();
                    for (int i = 0; i < documentos.size(); i++) {
                        TramitacionExpedientesValueObject tramitacionExpedientesVO = (TramitacionExpedientesValueObject) documentos.get(i);
                        if (tramitacionExpedientesVO.getCodDocumento().equals(tramExpVO.getCodDocumento())) {
                            documento = tramitacionExpedientesVO.getDescDocumento();
                            break;
                        }
                    }
                    asunto = asunto.replaceAll("@documento@", documento);
                }
                contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                if (contenido.indexOf("@documento@") != -1) {
                    if (documento == null) {
                        Vector documentos = tramExpVO.getListaDocumentos();
                        for (int i = 0; i < documentos.size(); i++) {
                            TramitacionExpedientesValueObject tramitacionExpedientesVO = (TramitacionExpedientesValueObject) documentos.get(i);
                            if (tramitacionExpedientesVO.getCodDocumento().equals(tramExpVO.getCodDocumento())) {
                                documento = tramitacionExpedientesVO.getDescDocumento();
                                break;
                            }
                        }
                    }
                    contenido = contenido.replaceAll("@documento@", documento);
                }

                /* Realizamos el proceso para el usuario en cuestion y para los delegados */
                while (codigoUsuario != -1) {//codigoUsuario == -1 -> no existe usuario delegado
                    //enviamos mail
                    email = UsuariosGruposManager.getInstance().getMailByUsuario(codigoUsuario, params);
                    if ((email != null) && (!email.equalsIgnoreCase(""))) {
                        mailHelper.sendMail(email, asunto, contenido);
                    }

                    //recuperamos usuariodelegado
                    codigoUsuario = UsuariosGruposManager.getInstance().getUsuarioDelegado(codigoUsuario, params);
                }
            }
        } catch (MailServiceNotActivedException e) {
            m_Log.error("Servicio de mail no activado");
            //Servicio de mail no activado, funcionamiento normal
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (m_Log.isErrorEnabled()) {
                m_Log.error(e.getMessage());
            }
            return false;
        }
        return true;
    }

    public boolean notificar(UsuarioValueObject usuario, TramitacionExpedientesValueObject tramExpVO, String asuntoExp) {
        boolean resultado = true;
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
            String emailUsuarioIniciaTramite;
            String emailUsuarioIniciaExped;
            asuntoExp = AdaptadorSQLBD.js_unescape(asuntoExp);
            MailHelper mailHelper = new MailHelper();
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("UNIDAD TRAMITADORA DEL USUARIO: " + tramExpVO.getUnidadTramitadora());
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("UNIDAD TRAMITADORA DEL TRAMITE Q SE INICIA: "
                        + tramExpVO.getUnidadTramitadoraTramiteIniciado());
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("TRAMITE Q FINALIZA: " + tramExpVO.getTramite());
            }

            //Comprobacion
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("-->Tamanho listaEMailsAlIniciar" + listaEMailsAlIniciar.size());
            }
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("-->Tamanho listaEMailsAlFinalizar" + listaEMailsAlFinalizar.size());
            }

            String procedimiento = "";
            if (tramExpVO.getProcedimiento() != null && !"".equals(tramExpVO.getProcedimiento())) {
                procedimiento = tramExpVO.getProcedimiento();
            } else {
                procedimiento = tramExpVO.getCodProcedimiento();
            }

            // Reemplazos de campos en el asunto y el contenido del mensaje            
            asunto = asunto.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());

            for (int i = 0; i < listaEMailsAlFinalizar.size(); i++) {
                emailsUOR = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEMailsUOR();
                emailsUsusUOR = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEMailsUsusUOR();
                emailsInteresados = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEMailsInteresados();
                emailUsuarioIniciaTramite = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEmailsUsuInicioTramite();
                emailUsuarioIniciaExped = ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getListaEmailsUsuInicioExped();
                
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("-->emailsUOR" + emailsUOR);
                }
                for (int j = 0; j < emailsUOR.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteUOR");
                    contenido = contenido.replaceAll("@usuario@", usuario.getNombreUsu());
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadora());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                    mailHelper.sendMail((String) emailsUOR.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO ENVIADO A " + emailsUOR.elementAt(j));
                    }
                }

                for (int j = 0; j < emailsUsusUOR.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteUsusUOR");
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadora());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                    mailHelper.sendMail((String) emailsUsusUOR.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO ENVIADO A " + emailsUsusUOR.elementAt(j));
                    }
                }

                for (int j = 0; j < emailsInteresados.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteInteresados");

                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadora());

                    mailHelper.sendMail((String) emailsInteresados.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO ENVIADO A " + emailsInteresados.elementAt(j));
                    }
                }
                
                if (emailUsuarioIniciaTramite != null && !emailUsuarioIniciaTramite.equals("")) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteUsuarioInicio");
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@tramite@", ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadora());

                    mailHelper.sendMail((String) emailUsuarioIniciaTramite, asunto, contenido);

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO ENVIADO A " + emailUsuarioIniciaTramite);
                    }
                }

                if (emailUsuarioIniciaExped != null && !emailUsuarioIniciaExped.equals("")) {
                    contenido = m_ConfigApplication.getString("mail.contentFinalizacionTramiteUsuarioInicioExped");
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@tramite@", ((EstructuraNotificacion) listaEMailsAlFinalizar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadora());

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

                    contenido = contenido.replaceAll("@usuario@", usuario.getNombreUsu());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadoraTramiteIniciado());
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);


                    String plazo = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getPlazoTramite();
                    String unidadPlazo = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getUnidadPlazo();

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("PLAZO TRMITE : " + plazo);
                    }
                    if (plazo == null || !plazo.equals("")) {
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("TIPO PLAZO TRMITE : " + unidadPlazo);
                        }
                        if (unidadPlazo != null) {
                            if ((unidadPlazo.equals("DAS HÁBILES")) || (unidadPlazo.equals("H"))) {
                                contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoH");
                            } else if ((unidadPlazo.equals("DAS NATURALES")) || (unidadPlazo.equals("N"))) {
                                contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoN");
                            } else if ((unidadPlazo.equals("MESES")) || (unidadPlazo.equals("M"))) {
                                contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoM");
                            }
                            contenido = contenido.replaceAll("@plazo@", plazo);
                            contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                        }
                    }

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO . CONTENIDO : " + contenido);
                    }
                    mailHelper.sendMail((String) emailsUOR.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO ENVIADO A " + emailsUOR.elementAt(j));
                    }
                }
                for (int j = 0; j < emailsUsusUOR.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentInicioTramiteUsusUOR");

                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadoraTramiteIniciado());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);

                    String plazo = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getPlazoTramite();
                    String unidadPlazo = ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getUnidadPlazo();


                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("PLAZO TRMITE : " + plazo);
                    }
                    if (plazo == null || !plazo.equals("")) {
                        if (m_Log.isDebugEnabled()) {
                            m_Log.debug("TIPO PLAZO TRMITE : " + tramExpVO.getTipoPlazo());
                        }
                        if (unidadPlazo != null) {
                            if ((unidadPlazo.equals("DAS HBILES")) || (unidadPlazo.equals("H"))) {
                                contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoH");
                            } else if ((unidadPlazo.equals("DAS NATURALES")) || (unidadPlazo.equals("N"))) {
                                contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoN");
                            } else if ((unidadPlazo.equals("MESES")) || (unidadPlazo.equals("M"))) {
                                contenido += m_ConfigApplication.getString("mail.contentInicioTramitePlazoM");
                            }
                            contenido = contenido.replaceAll("@plazo@", plazo);
                            contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                        }
                    }

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE FINALIZADO . CONTENIDO : " + contenido);
                    }
                    mailHelper.sendMail((String) emailsUsusUOR.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO ENVIADO A " + emailsUsusUOR.elementAt(j));
                    }
                }
                for (int j = 0; j < emailsInteresados.size(); j++) {
                    contenido = m_ConfigApplication.getString("mail.contentInicioTramiteInteresados");

                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadoraTramiteIniciado());
                    contenido = contenido.replaceAll("@tramite@",
                            ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);

                    mailHelper.sendMail((String) emailsInteresados.elementAt(j), asunto, contenido);
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO ENVIADO A " + emailsInteresados.elementAt(j));
                    }
                }
            
                if (emailUsuarioIniciaTramite != null && !emailUsuarioIniciaTramite.equals("")) {
                    contenido = m_ConfigApplication.getString("mail.contentInicioTramiteUsuarioInicio");
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@tramite@", ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadora());

                    mailHelper.sendMail((String) emailUsuarioIniciaTramite, asunto, contenido);

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("MAIL DE TRAMITE INICIADO ENVIADO A " + emailUsuarioIniciaTramite);
                    }
                }

                if (emailUsuarioIniciaExped != null && !emailUsuarioIniciaExped.equals("")) {
                    contenido = m_ConfigApplication.getString("mail.contentInicioTramiteUsuarioInicioExped");
                    contenido = contenido.replaceAll("@expediente@", tramExpVO.getNumeroExpediente());
                    contenido = contenido.replaceAll("@asunto@", asuntoExp);
                    contenido = contenido.replaceAll("@tramite@", ((EstructuraNotificacion) listaEMailsAlIniciar.elementAt(i)).getNombreTramite());
                    contenido = contenido.replaceAll("@procedimiento@", procedimiento);
                    contenido = contenido.replaceAll("@unidadTramitadora@", tramExpVO.getUnidadTramitadora());

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
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("-->RESULTADO : " + resultado);
        }
        return resultado;
    }

    private void cargarListaAjuntosPDFTramite(TramitacionExpedientesValueObject tramExpVO) {
        Collection pdfs = new ArrayList();
        String tramiteKey = "";
        /* no compuebo si en configuracion esta JSP.Formularios=si porque si han
         llegado hasta esta opcion es que lo esta
         */
        try {
            FormularioFacade facade = new FormularioFacade();
            DefinicionTramiteKey tramite = new DefinicionTramiteKey(
                    Integer.parseInt(tramExpVO.getCodTramite()),
                    Integer.parseInt(tramExpVO.getCodMunicipio()),
                    tramExpVO.getCodProcedimiento());
            tramiteKey = tramite.toString();
            pdfs = facade.getDefAdjuntosTramite(tramite);
        } catch (Exception e) {
            m_Log.error("Error al intentar cargar los def_adjuntos del tramite  '" + tramiteKey + "': " + e.getMessage());
        } finally {
            tramExpVO.setListaAjuntosPDFTramite(pdfs);
        }
        m_Log.debug("'" + tramExpVO.getListaAjuntosPDFTramite().size() + "' adjuntos PDF de " + tramiteKey + " ..... " + tramExpVO.getListaAjuntosPDFTramite());
    }

    private void cargarFormsPDF(TramitacionExpedientesValueObject tramExpVO) {
        Collection pdfs = new ArrayList();
        Collection temp = new ArrayList();
        String tramiteKey = "";
        boolean mostrar = false;
        try {
            Config m_Config = ConfigServiceHelper.getConfig("common");
            m_Log.debug("JSP.Formularios..... " + m_Config.getString("JSP.Formularios"));
            if ("si".equals(m_Config.getString("JSP.Formularios"))) {
                FormularioFacade facade = new FormularioFacade();
                TramiteTramitadoKey tramite = new TramiteTramitadoKey(
                        Integer.parseInt(tramExpVO.getCodMunicipio()),
                        tramExpVO.getCodProcedimiento(),
                        Integer.parseInt(tramExpVO.getEjercicio()),
                        tramExpVO.getNumeroExpediente(),
                        Integer.parseInt(tramExpVO.getCodTramite()),
                        Integer.parseInt(tramExpVO.getOcurrenciaTramite()));
                //int municipio, String procedimiento, int ejercicio, String numero, int tramite, int ocu) {
                tramiteKey = tramite.toString();
                pdfs = facade.getFormsOfTramite(tramite);

                /* se mostraran los pdf si:
                 - JSP.Formularios = si
                 - el tramite tiene formularios asociados F_TRAFORM_TRA
                 - o podria tenerelos F_DEFFORM_TRA                */
                if (pdfs.isEmpty()) {
                    Collection defs = facade.getDefFormsOfTramite(tramite);
                    mostrar = !defs.isEmpty();
                } else {
                    mostrar = true;
                }
            }
        } catch (Exception e) {
            m_Log.error("Error al intentar cargar los pdfs del tramite  '" + tramiteKey + "': " + e.getMessage());
            e.printStackTrace();
        } finally {

            m_Log.debug(pdfs);
            Iterator it = pdfs.iterator();

            while (it.hasNext()) {

                FormularioTramitadoVO formVO = (FormularioTramitadoVO) it.next();
                String nombre = UsuarioManager.getInstance().getNombre(formVO.getUsuario());
                if (nombre.equals("") || nombre == null) {
                    nombre = formVO.getUsuario();
                }
                FormularioTramitadoVO novoVO = new FormularioTramitadoVO(formVO.getCodigo(),
                        formVO.getDefinicionFormulario(), formVO.getDemandante(), formVO.getSecuencia(),
                        formVO.getDescripcion(), formVO.getAsunto(), formVO.getFormRaiz(),
                        formVO.getTipo(), nombre,
                        formVO.getEntrada(), formVO.getInstancia(), formVO.getFecAlta(),
                        formVO.getFecBaja(), formVO.getFecMod(), formVO.getEstado(), formVO.getDatos());

                temp.add(novoVO);
            }

            pdfs = temp;

            tramExpVO.setListaFormsPDF(pdfs);
            tramExpVO.setMostrarFormsPDF(mostrar);
        }
        m_Log.debug("'" + tramExpVO.getListaFormsPDF().size() + "' FORMULARIOS PDF de " + tramiteKey + " ..... " + tramExpVO.getListaFormsPDF());
    }

    private boolean eliminarFormPDF(String codigo) {
        try {
            FormularioFacade facade = new FormularioFacade();
            facade.borrarFormulario(codigo);
            return true;
        } catch (Exception e) {
            m_Log.error("Error al intentar borrar el formulario '" + codigo + "': " + e.getMessage());
            return false;
        }
    }

    private String iniciarExpedienteAsociado(String procedimiento, UsuarioValueObject usuario, FichaExpedienteForm fichaExpForm, String[] params, String unidadTramInicio) {
        String numero = "";
        try {
            m_Log.debug("ini INICIO DE EXPEDIENTE .......................................... ");
            // INI Cargar los datos para iniciar el expediente
            GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", fichaExpForm.getCodMunicipio());
            gVO.setAtributo("usuario", Integer.toString(usuario.getIdUsuario()));
            gVO.setAtributo("codOrganizacion", Integer.toString(usuario.getOrgCod()));
            gVO.setAtributo("codEntidad", Integer.toString(usuario.getEntCod()));
            gVO.setAtributo("codAplicacion", Integer.toString(usuario.getAppCod()));
            gVO.setAtributo("codUOR", fichaExpForm.getCodUnidadOrganicaExp());
            String ejercicioExpOrigen = fichaExpForm.getEjercicio();
            String procedimientoExpOrigen = fichaExpForm.getCodProcedimiento();
            String numeroExpOrigen = fichaExpForm.getNumero();

            gVO.setAtributo("ejercicio", ejercicioExpOrigen); //Solo utilizado AKI para buscar los interesados
            gVO.setAtributo("codProcedimiento", procedimientoExpOrigen); //Solo utilizado AKI para buscar los interesados
            gVO.setAtributo("numero", numeroExpOrigen); //Solo utilizado AKI para buscar los interesados
            gVO.setAtributo("numExpedienteOriginal",numeroExpOrigen); //Solo utilizado AKI para buscar los interesados
            Vector lTerceros = InteresadosManager.getInstance().getListaInteresados(gVO, params);
            // FIN Cargar los datos para iniciar el expediente

            // Cargar los datos de la unidad del tramite de inicio seleccionada
            gVO.setAtributo("unidadTramiteInicioSeleccionada", unidadTramInicio);

            // INI Calcular el nuevo numero de expediente
            TramitacionValueObject tramVO = new TramitacionValueObject();
            tramVO.setCodProcedimiento(procedimiento);
            TramitacionManager.getInstance().getNuevoExpediente(usuario, tramVO, params);
            gVO.setAtributo("ejercicio", tramVO.getEjercicio());
            gVO.setAtributo("codProcedimiento", procedimiento);
            numero = tramVO.getNumero();
            gVO.setAtributo("numero", numero);
            gVO.setAtributo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO, ConstantesDatos.ORIGEN_LLAMADA_INTERFAZ_WEB);
            m_Log.debug("NUMERO PARA NUEVO EXPEDIENTE RELACIONADO: " + numero);
            // FIN Calcular el nuevo numero de expediente

            gVO.setAtributo("nomUsuario",usuario.getNombreUsu());

            int res = FichaExpedienteManager.getInstance().iniciarExpediente(gVO, params);

            if (res > 0) {
                // INI Cargar los otros datos para grabar en el expediente
                gVO.setAtributo("asunto", AdaptadorSQLBD.js_unescape(fichaExpForm.getAsunto()));
                gVO.setAtributo("observaciones", fichaExpForm.getObservaciones());
                gVO.setAtributo("localizacion", fichaExpForm.getLocalizacion());
                gVO.setAtributo("codLocalizacion", fichaExpForm.getCodLocalizacion());
                if (lTerceros == null) {
                    lTerceros = new Vector();
                }
                Vector listaCodTercero = new Vector();
                Vector listaVersionTercero = new Vector();
                Vector listaCodDomicilio = new Vector();
                Vector listaRol = new Vector();
                Vector listaMostrar = new Vector();
                int rolPorDefecto = TramitacionExpedientesManager.getInstance().getCodRolPorDefecto(procedimiento, params);
                for (int i = 0; i < lTerceros.size(); i++) {
                    InteresadoExpedienteVO t = (InteresadoExpedienteVO) lTerceros.elementAt(i);
                    listaCodTercero.addElement(t.getCodTercero() + "");
                    listaVersionTercero.addElement(t.getNumVersion() + "");
                    listaCodDomicilio.addElement(t.getCodDomicilio() + "");
                    listaRol.addElement(rolPorDefecto + "");
                    listaMostrar.addElement(t.isMostrar() + "");
                }
                gVO.setAtributo("listaCodTercero", listaCodTercero);
                gVO.setAtributo("listaVersionTercero", listaVersionTercero);
                gVO.setAtributo("listaCodDomicilio", listaCodDomicilio);
                gVO.setAtributo("listaRol", listaRol);
                gVO.setAtributo(("listaMostrar"), listaMostrar);

                gVO.setAtributo("tipoAlta", ConstantesDatos.TIPO_ALTA_EXP_ASOCIADO);
                OperacionesExpedienteManager.getInstance().registrarAltaExpediente(gVO, params);

                int res2 = FichaExpedienteManager.getInstance().grabarExpediente(gVO, params);
                // FIN Cargar los otros datos para grabar en el expediente
                if (res2 > 0) {
                    ConsultaExpedientesValueObject consExpVO = new ConsultaExpedientesValueObject();
                    consExpVO.setCodMunicipioIni((String) gVO.getAtributo("codMunicipio"));
                    consExpVO.setEjercicioIni(ejercicioExpOrigen);
                    consExpVO.setNumeroExpedienteIni(numeroExpOrigen);
                    consExpVO.setCodMunicipio((String) gVO.getAtributo("codMunicipio"));
                    consExpVO.setEjercicio((String) gVO.getAtributo("ejercicio"));
                    consExpVO.setNumeroExpediente((String) gVO.getAtributo("numero"));
                    int resultado = ConsultaExpedientesManager.getInstance().insertExpedientesRelacionados(consExpVO, params);
                    
                    if (resultado > 0){
                        consExpVO.setUsuario(Integer.toString(usuario.getIdUsuario()));
                        consExpVO.setNombreUsuario(usuario.getNombreUsu());
                        OperacionesExpedienteManager.getInstance().registrarAnhadirRelacion(consExpVO, params);
                    }
                }
            }
            m_Log.debug("fin INICIO DE EXPEDIENTE .......................................... ");
        } catch (TramitacionException te) {
            if (m_Log.isErrorEnabled()) {
                m_Log.error(te.getMessage());
            }
            return "";
        } catch (AnotacionRegistroException re) {
            if (m_Log.isErrorEnabled()) {
                m_Log.error(re.getMessage());
            }
            return "";
        } catch (TechnicalException teche) {
            if (m_Log.isErrorEnabled()) {
                m_Log.error(teche.getMessage());
            }
            return "";
        }


        return numero;
    }

    /**
     * Comprueba para un determinado trámite de un determinado procedimiento si
     * cumple sus condiciones de entrada, sean de trámite o de expresión. Se
     * hace para e
     */
    //private Vector comprobarCondicionesEntrada(String codTramite,String codMunicipio,String codProcedimiento,String numeroExpediente,String ejercicio,String codEntidad,String[] params)
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
                    tramitesnoOK = TramitacionExpedientesDAO.getInstance().comprobarCondicionesEntradaExpresion(teVO.getEstructuraDatosSuplExpediente(), teVO.getValoresDatosSuplExpediente(),
                            teVO.getEstructuraDatosSuplTramites(), teVO.getValoresDatosSuplTramites(), listaCondicionesExpresionTramites);

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
            /*
             if(listaCondicionesEntradaNoCumplidas!=null && listaCondicionesEntradaNoCumplidas.size()>0)
             listaFinal.addAll(listaCondicionesEntradaNoCumplidas);

             if(tramitesnoOK!=null && tramitesnoOK.size()>0)
             listaFinal.addAll(tramitesnoOK); */
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

    private Vector calcular_campos_expresion(String[] params, FichaExpedienteForm expForm, HttpSession session, HttpServletRequest request, GeneralValueObject gVO, String tramite) {
        //davidvim00
        Vector expresiones_calculadas = new Vector();
        AdaptadorSQLBD abd = null;
        Connection con = null;

        try {


            abd = new AdaptadorSQLBD(params);
            con = abd.getConnection();
            TramitacionExpedientesValueObject tramExpVO = new TramitacionExpedientesValueObject();
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            String valor_exp = new String();

            tramExpVO.setCodMunicipio(expForm.getCodMunicipio());
            tramExpVO.setCodProcedimiento(expForm.getCodProcedimiento());
            tramExpVO.setEjercicio(expForm.getEjercicio());
            tramExpVO.setCodTramite(expForm.getCodTramite());
            tramExpVO.setCodEntidad(Integer.toString(usuario.getEntCod()));
            tramExpVO.setCodOrganizacion(Integer.toString(usuario.getOrgCod()));
            tramExpVO.setCodTramite(tramite);
            tramExpVO.setNumeroExpediente(expForm.getNumExpediente());
            tramExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
            tramExpVO.setBloqueo("no");
            tramExpVO.setExpHistorico(expForm.isExpHistorico());

            Vector DS = TramitacionExpedientesDAO.getInstance().cargarDatosSuplementariosExpediente(tramExpVO, abd, con);
            Vector estructuraDSExpediente = (Vector) DS.elementAt(0);
            Vector valoresDSExpediente = (Vector) DS.elementAt(1);
            Vector estructuraDSTramites = (Vector) DS.elementAt(2);
            Vector valoresDSTramites = new Vector();

            //*************************************************
            //*************************************************                                
            HashMap campos = new HashMap();
            CamposFormulario CampF = null;

            for (int i = 0; i < estructuraDSTramites.size(); i++) {
                EstructuraCampo CC = (EstructuraCampo) estructuraDSTramites.elementAt(i);
                campos.put(CC.getCodCampo(), request.getParameter(CC.getCodCampo()));
                CampF = new CamposFormulario(campos);
                valoresDSTramites.addElement(CampF);
            }
            //**************************************************
            //**************************************************                
            Vector ExpresionesCalculadas = TramitacionExpedientesDAO.getInstance().getListaCamposExpresionCalculada(abd, con, tramExpVO);
            GeneralValueObject g;
            for (int i = 0; i < ExpresionesCalculadas.size(); i++) {
                g = (GeneralValueObject) ExpresionesCalculadas.elementAt(i);
                GeneralValueObject h = new GeneralValueObject();

                String expresion = (String) g.getAtributo("expresion");
                String codigoCampo = (String) g.getAtributo("cod_campo");
                String origen = (String) g.getAtributo("origen");

                //Para reconocer si se trata de una expresion de fecha o de un campo numerico buscaremos en la expresion las etiquetas que utilizamos para fechas                    
                if (expresion != null || !"".equals(expresion)) {
                    if (expresion.indexOf(";MESES;") > 0) //valor_exp = recupera_valor_expresion_fec(expresion,valoresDSExpediente,estructuraDSExpediente);
                    {
                        valor_exp = recupera_valor_expresion_fec(expresion, estructuraDSExpediente, valoresDSExpediente, estructuraDSTramites, valoresDSTramites);
                    } else //valor_exp = recupera_valor_expresion_num(expresion,valoresDSExpediente,estructuraDSExpediente);                    
                    {
                        valor_exp = recupera_valor_expresion_num(expresion, estructuraDSExpediente, valoresDSExpediente, estructuraDSTramites, valoresDSTramites);
                    }
                }

                h.setAtributo("cod_campo", codigoCampo);
                h.setAtributo("origen", origen);
                h.setAtributo("valor", valor_exp);
                expresiones_calculadas.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                abd.devolverConexion(con);
            } catch (BDException e) {
                m_Log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return expresiones_calculadas;
    }

    private String recupera_valor_expresion_num(String expresion, Vector estructura_e, Vector valores_e, Vector estructura_t, Vector valores_t) {
        String campo = "";
        String valor_campo = "";
        String tramite = "";

        boolean valido = true;


        expresion = expresion.replace(" x ", " * ");
        expresion = expresion.replace(" X ", " * ");

        for (int i = 0; i < estructura_e.size(); i++) {
            EstructuraCampo eC_e = (EstructuraCampo) estructura_e.elementAt(i);
            CamposFormulario cF_e = (CamposFormulario) valores_e.get(i);
            campo = eC_e.getCodCampo();
            valor_campo = cF_e.getString(eC_e.getCodCampo());
            if (Campo_sin_valor(expresion, campo) && ("".equals(valor_campo) || valor_campo == null)) {
                valido = false;
            }

            expresion = expresion.replace(" " + campo + " ", " " + valor_campo + " ");
            expresion = expresion.replace("(" + campo + " ", "(" + valor_campo + " ");
            expresion = expresion.replace(" " + campo + ")", " " + valor_campo + ")");
            expresion = expresion.replace("(" + campo + ")", "(" + valor_campo + ")");
        }
        for (int i = 0; i < estructura_t.size(); i++) {
            EstructuraCampo eC_t = (EstructuraCampo) estructura_t.elementAt(i);
            CamposFormulario cF_t = (CamposFormulario) valores_t.get(i);

            campo = eC_t.getCodCampo();
            valor_campo = cF_t.getString(eC_t.getCodCampo());
            tramite = eC_t.getCodTramite();

            if (Campo_sin_valor(expresion, campo) && ("".equals(valor_campo) || valor_campo == null)) {
                valido = false;
            }

            expresion = expresion.replace(" " + campo + "_T" + tramite + " ", " " + valor_campo + " ");
            expresion = expresion.replace("(" + campo + "_T" + tramite + " ", "(" + valor_campo + " ");
            expresion = expresion.replace(" " + campo + "_T" + tramite + ")", " " + valor_campo + ")");
            expresion = expresion.replace("(" + campo + "_T" + tramite + ")", "(" + valor_campo + ")");
        }
        if (valido == true) {
            return evalua_cadena.evalua_calculos(expresion);
        } else {
            return "";
        }
    }

    private String recupera_valor_expresion_fec(String expresion, Vector estructura_e, Vector valores_e, Vector estructura_t, Vector valores_t) {
        String aux = "";
        String campo = "";
        String valor_campo = "";
        String tramite = "";
        String operacion = "";
        String cadena = "";
        String dia = "0";
        String mes = "0";
        String ano = "0";
        String resultado = "";
        int posicion = 0;
        int inicio = 0;
        boolean valido = true;
        String retorno = "";

        try {
            Date fecha = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //Calendar cal = Calendar.getInstance(); 
            Calendar cal = new GregorianCalendar();

            inicio = expresion.indexOf("(");
            if (expresion.indexOf("+") > 0) {
                operacion = "SUMA";
            } else {
                operacion = "RESTA";
            }


            for (int i = 0; i < estructura_e.size(); i++) {
                EstructuraCampo eC = (EstructuraCampo) estructura_e.elementAt(i);
                CamposFormulario cF = (CamposFormulario) valores_e.get(i);

                campo = eC.getCodCampo();
                valor_campo = cF.getString(eC.getCodCampo());
                tramite = eC.getCodTramite();
                if (Campo_sin_valor(expresion, campo) && ("".equals(valor_campo) || valor_campo == null)) {
                    valido = false;
                }

                if (tramite == null) {
                    aux = campo + " ";
                } else {
                    aux = campo + "_T" + tramite + " ";
                }


                if (expresion.indexOf(aux) >= 0 && valido == true) {
                    fecha = sdf.parse(valor_campo);
                    cadena = expresion.substring(inicio, expresion.length());
                    cadena = cadena.replace("(", "");
                    cadena = cadena.replace(")", "");

                    posicion = cadena.indexOf(";DIAS;");
                    dia = cadena.substring(0, posicion);
                    cadena = cadena.substring(posicion + 6, cadena.length());

                    posicion = cadena.indexOf(";MESES;");
                    mes = cadena.substring(0, posicion);
                    cadena = cadena.substring(posicion + 7, cadena.length());

                    posicion = cadena.indexOf(";ANOS;");
                    ano = cadena.substring(0, posicion);
                }
            }
            for (int i = 0; i < estructura_t.size(); i++) {
                EstructuraCampo eC = (EstructuraCampo) estructura_t.elementAt(i);
                CamposFormulario cF = (CamposFormulario) valores_t.get(i);

                campo = eC.getCodCampo();
                valor_campo = cF.getString(eC.getCodCampo());
                tramite = eC.getCodTramite();
                if (Campo_sin_valor(expresion, campo) && ("".equals(valor_campo) || valor_campo == null)) {
                    valido = false;
                }

                if (tramite == null) {
                    aux = campo + " ";
                } else {
                    aux = campo + "_T" + tramite + " ";
                }


                if (expresion.indexOf(aux) >= 0 && valido == true) {
                    fecha = sdf.parse(valor_campo);
                    cadena = expresion.substring(inicio, expresion.length());
                    cadena = cadena.replace("(", "");
                    cadena = cadena.replace(")", "");

                    posicion = cadena.indexOf(";DIAS;");
                    dia = cadena.substring(0, posicion);
                    cadena = cadena.substring(posicion + 6, cadena.length());

                    posicion = cadena.indexOf(";MESES;");
                    mes = cadena.substring(0, posicion);
                    cadena = cadena.substring(posicion + 7, cadena.length());

                    posicion = cadena.indexOf(";ANOS;");
                    ano = cadena.substring(0, posicion);
                }
            }

            if (operacion.equals("RESTA")) {
                dia = "-" + dia;
                mes = "-" + mes;
                ano = "-" + ano;
            }

            cal.setTime(fecha);
            resultado = sdf.format(cal.getTime());
            cal.add(Calendar.DATE, Integer.parseInt(dia));
            resultado = sdf.format(cal.getTime());
            cal.add(Calendar.MONTH, Integer.parseInt(mes));
            resultado = sdf.format(cal.getTime());
            cal.add(Calendar.YEAR, Integer.parseInt(ano));
            resultado = sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (valido == true) {
            return resultado;
        } else {
            return "";
        }
    }
    
    private boolean Campo_sin_valor(String expresion, String campo) {
        boolean vacio = false;
        if (expresion.indexOf(" " + campo + " ") >= 0) {
            vacio = true;
        } else if (expresion.indexOf("(" + campo + ")") >= 0) {
            vacio = true;
        } else if (expresion.indexOf("(" + campo + " ") >= 0) {
            vacio = true;
        } else if (expresion.indexOf(" " + campo + ")") >= 0) {
            vacio = true;
        } else if (expresion.indexOf(" " + campo + "_T") >= 0) {
            vacio = true;
        } else if (expresion.indexOf("(" + campo + "_T") >= 0) {
            vacio = true;
        }
        return vacio;
    }
}// TramitacionExpedientesAction
