package es.altia.agora.webservice.registro.pisa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.ExpedientesDAO;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.webservice.registro.AccesoRegistro;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.agora.webservice.registro.pisa.cliente.FachadaClientePisa;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_AnotacionesBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_InteresadosBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ParametrosBean;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;

public class AccesoRegistroPisa implements AccesoRegistro {

    private String nombreServicio;
    private String prefijoPropiedad;
    protected static Config conf = ConfigServiceHelper.getConfig("techserver");

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getPrefijoPropiedad() {
        return prefijoPropiedad;
    }

    public void setPrefijoPropiedad(String prefijoPropiedad) {
        this.prefijoPropiedad = prefijoPropiedad;
    }

    protected static Log m_Log = LogFactory.getLog(AccesoRegistroPisa.class.getName());
    private static ResourceBundle m_ct =
            ResourceBundle.getBundle("es.altia.agora.webservice.registro.pisa.cliente.configuracion.configuracion");
    private static ResourceBundle m_TipoDocs =
            ResourceBundle.getBundle("es.altia.agora.webservice.registro.pisa.cliente.constantes.TipoDocumento");
    private static ResourceBundle m_constantes =
            ResourceBundle.getBundle("es.altia.agora.webservice.registro.pisa.cliente.constantes.constantes");

    public Vector getAsientosEntradaRegistro(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                             String fechaDesde, String fechaHasta,String documento,String nombre,String apellido1,String apellido2,String codAsunto,String unidadRegistroAsunto,String tipoRegistroAsunto,String codUorDestino) throws RegistroException {

        String tipoAnotacion = "E";
        String estadoAnotacionAccede = m_constantes.getString("estadoPendienteAccede");

        String origen = m_ct.getString("Pisa." + uVO.getOrgCod() + ".nombre");

        try {
            /*
            * La función obtener entradas del servicio de PISA tiene como parámetro la unidad orgánica, que además
            * tendrá su propio código (tenemos el mapping en el campo accede de la tabla A_UOR)
            * El usuario que realiza la consulta puede tener una o varias unidades orgánicas asociadas,
            */
            //Obtenemos hashMap de uors del usuario (key = codAccede, valor = codUOR)

            HashMap uorsUsuario = getListaUorsUsuario(params, uVO);
            Iterator iterator = uorsUsuario.entrySet().iterator();
            String uors = "";
            while (iterator.hasNext()) {
                String key = (String) ((Map.Entry) iterator.next()).getKey();
                uors = uors + "," + key;
            }

            if ("".equals(uors)) return new Vector();
            else uors = uors.substring(1);

            SWPisa_ParametrosBean dato = new SWPisa_ParametrosBean();
            dato.setOrganizacion(m_ct.getString("Pisa." + uVO.getOrgCod() + ".organizacion"));
            dato.setEntidad(m_ct.getString("Pisa." + uVO.getOrgCod() + ".entidad"));
            dato.setTipo(tipoAnotacion);
            dato.setFechaDesde(fechaDesde);
            dato.setFechaHasta(fechaHasta);
            dato.setEjercicio("");
            dato.setNumeros("");
            dato.setEstado(estadoAnotacionAccede);
            dato.setExpediente("");
            dato.setUnidad(uors);
            dato.setEfecto("1");

            return getAnotacionFromSWPisa(dato, uorsUsuario, origen);
        } catch (BDException bde) {
            if (m_Log.isErrorEnabled())
                m_Log.error("ERROR DE CONEXION AL INTENTAR RECUPERAR LAS UNIDADES ORGANICAS DEL USUARIO");
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE HAN PODIDO RECUPERAR LAS UNIDADES ORGANICAS DEL USUARIO");
        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL CONSULTAR LAS UNIDADES ORGANICAS DEL USUARIO");
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE HAN PODIDO RECUPERAR LAS UNIDADES ORGANICAS DEL USUARIO");
        }
    }

    public Vector getAsientosExpedientesHistorico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion,String codUorAnotacion)
            throws RegistroException {

        String tipoAnotacion = "E";
        String estadoAnotacion = m_constantes.getString("estadoAceptadaAccede") + "," + m_constantes.getString("estadoRechazadaAccede");

        String origen = m_ct.getString("Pisa." + uVO.getOrgCod() + ".nombre");

        try {
            /*
            * La función obtener entradas del servicio de PISA tiene como parámetro la unidad orgánica, que además
            * tendrá su propio código (tenemos el mapping en el campo accede de la tabla A_UOR)
            * El usuario que realiza la consulta puede tener una o varias unidades orgánicas asociadas,
            */
            //Obtenemos hashMap de uors del usuario (key = codAccede, valor = codUOR)

            HashMap uorsUsuario = getListaUorsUsuario(params, uVO);
            Iterator iterator = uorsUsuario.entrySet().iterator();
            String uors = "";
            while (iterator.hasNext()) {
                String key = (String) ((Map.Entry) iterator.next()).getKey();
                uors = uors + "," + key;
            }

            if ("".equals(uors)) return new Vector();
            else uors = uors.substring(1);

            SWPisa_ParametrosBean dato = new SWPisa_ParametrosBean();
            dato.setOrganizacion(m_ct.getString("Pisa." + uVO.getOrgCod() + ".organizacion"));
            dato.setEntidad(m_ct.getString("Pisa." + uVO.getOrgCod() + ".entidad"));
            dato.setTipo(tipoAnotacion);
            dato.setFechaDesde(fechaDesde);
            dato.setFechaHasta(fechaHasta);
            dato.setEjercicio("");
            dato.setNumeros("");
            dato.setEstado(estadoAnotacion);
            dato.setExpediente("");
            dato.setUnidad(uors);
            dato.setEfecto("1");

            return getAnotacionFromSWPisa(dato, uorsUsuario, origen);
        } catch (BDException bde) {
            if (m_Log.isErrorEnabled())
                m_Log.error("ERROR DE CONEXION AL INTENTAR RECUPERAR LAS UNIDADES ORGANICAS DEL USUARIO");
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE HAN PODIDO RECUPERAR LAS UNIDADES ORGANICAS DEL USUARIO");
        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL CONSULTAR LAS UNIDADES ORGANICAS DEL USUARIO");
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE HAN PODIDO RECUPERAR LAS UNIDADES ORGANICAS DEL USUARIO");
        }
    }

    public RegistroValueObject getInfoAsientoConsulta(RegistroValueObject registroVO, String[] params) throws RegistroException {

        String periodo = String.valueOf(registroVO.getAnoReg());
        String numero = String.valueOf(registroVO.getNumReg());
        String tipo = String.valueOf(registroVO.getTipoReg());
        String uor = String.valueOf(registroVO.getUnidadOrgan());
        int estado = registroVO.getEstAnotacion();
        int organizacion = registroVO.getIdOrganizacion();
                
        HashMap uors = this.getUORsEnAccede(params, uor);
        Iterator iterator = uors.entrySet().iterator();

        String key;
        if (iterator.hasNext()) key = (String) ((Map.Entry) iterator.next()).getKey();
        else throw new RegistroException(new Exception(), "NO EXISTE EL VALOR DE ACCEDE ASOCIADO A LA UOR " + uor);

        SWPisa_ParametrosBean dato = new SWPisa_ParametrosBean();
        dato.setOrganizacion(m_ct.getString("Pisa." + organizacion + ".organizacion"));
        dato.setEntidad(m_ct.getString("Pisa." + organizacion + ".entidad"));
        dato.setFechaDesde("");
        dato.setFechaHasta("");
        dato.setEjercicio(periodo);
        dato.setTipo(tipo);
        dato.setEstado(Integer.toString(estadoSgeToAccede(estado)));
        dato.setExpediente("");
        dato.setUnidad(key);
        dato.setNumeros(numero);
        dato.setEfecto("1");

        FachadaClientePisa fachadaPisa = FachadaClientePisa.getInstance(prefijoPropiedad);
        SWPisa_AnotacionesBean[] anotaciones = fachadaPisa.buscarEntradas(dato);

        if (anotaciones == null || anotaciones.length == 0) throw new RegistroException(new Exception(),
                    "NO SE HA PODIDO ENCONTRAR LA ANOTACION EN LA LLAMADA AL SERVICIO WEB");

        // Aunque haya mas de una solo vamos a recuperar la primera.
        // TODO: Solo puede devolverse una, controlar esa excepción.
        SWPisa_AnotacionesBean anotacion = anotaciones[0];

        if (anotacion.getNumero().equals(numero)) {
            registroVO.setFecEntrada(anotacion.getFecha_1() + " " + anotacion.getHora_1());
            registroVO.setFecHoraDoc(anotacion.getFecha_2() + " " + anotacion.getHora_2());
            registroVO.setAsunto(convertirMAYUS(anotacion.getExtracto()));
            registroVO.setIdUndTramitad((String) uors.get(anotacion.getUnidad_organica()));
            

            ArrayList documentos = new ArrayList();
            if ((anotacion.getDocumentos() != null) && (anotacion.getDocumentos().length > 0)) {
                for (int j = 0; j < anotacion.getDocumentos().length; j++) {
                    if (anotacion.getDocumentos()[j] != null) {
                        DocumentoValueObject documento = new DocumentoValueObject();
                        documento.setCodigo(anotacion.getDocumentos()[j].getCodigo());
                        documento.setNombre(anotacion.getDocumentos()[j].getNombre());
                        documento.setExtension(anotacion.getDocumentos()[j].getExtension());
                        documentos.add(documento);
                    }
                }
            }
            DocumentoValueObject[] documentosArray = new DocumentoValueObject[documentos.size()];
            Iterator documentosIterator = documentos.iterator();
            int indiceDocumento = 0;
            while (documentosIterator.hasNext()) {
                documentosArray[indiceDocumento++] = (DocumentoValueObject) documentosIterator.next();
            }
            registroVO.setDocumentos(documentosArray);

            SWPisa_InteresadosBean[] interesados = anotacion.getInteresados();
            if (interesados != null && interesados.length > 0) {
                for (int k = 0; k < interesados.length; k++) {
                    String codigoTipoRelacion = interesados[k].getCodigo_tipo_relacion();
                    if (codigoTipoRelacion.equals("1")) {
                        registroVO.setTipoDocInteresado(m_TipoDocs.getString(interesados[k].getCodigo_tipo_documento()));
                        registroVO.setDocumentoInteresado(convertirMAYUS(interesados[k].getDocumento()));
                        String nombre = convertirMAYUS(interesados[k].getNombre());
                        String apellido1 = convertirMAYUS(interesados[k].getApellido1());
                        String apellido2 = convertirMAYUS(interesados[k].getApellido2());
                        String nombreCompleto = getNombreCompleto(nombre, apellido1, apellido2);
                        registroVO.setNomCompletoInteresado(nombreCompleto);
                        registroVO.setApellido1InteresadoExterno(apellido1);
                        registroVO.setApellido2InteresadoExterno(apellido2);
                        registroVO.setNombreInteresadoExterno(nombre);
                        registroVO.setTlfInteresado(convertirMAYUS(interesados[k].getTelefono()));
                        registroVO.setEmailInteresado(convertirMAYUS(interesados[k].getEmail()));
                        registroVO.setDomCompletoInteresado(convertirMAYUS(interesados[k].getDomicilio()));
                        registroVO.setMunInteresado(convertirMAYUS(interesados[k].getMunicipio()));
                        registroVO.setProvInteresado(interesados[k].getCodigo_provincia());
                        registroVO.setCpInteresado(interesados[k].getCodigo_postal());
                    }
                }
                registroVO.setNumTerceros(1);
            }

            // Recuperamos los datos referentes al expediente y procedimiento asociado.
            String numExpediente;
            if ((numExpediente = anotacion.getExpediente()) != null) {
                registroVO.setNumExpediente(numExpediente);
                StringTokenizer tokenizer = new StringTokenizer(numExpediente, "/");
                if (tokenizer.countTokens() == 3) {     // Realizamos esta comprobacion para ver si el numero de expediente tiene la misma estructura que en SGE.
                    tokenizer.nextToken();
                    registroVO.setCodProcedimiento(tokenizer.nextToken());
                    registroVO.setDescProcedimiento(getDescProcedimiento(registroVO.getCodProcedimiento(), params));
                }
            }

        } else throw new RegistroException(new Exception(), "EL CODIGO DE LA ANOTACION DEVUELTA POR EL " +
                "SERVICIO WEB NO COINCIDE CON EL CODIGO PEDIDO");

        registroVO.setListaTemasAsignados(new Vector());
        registroVO.setListaDocsAsignados(new Vector());
        registroVO.setRelaciones(new Vector());
        registroVO.setTipoAnot(2);
        String[] codDescUOR = new String[2];
        codDescUOR = getCodDescUOR(uor, params);
        registroVO.setUorCodVisible(codDescUOR[0]);
        registroVO.setNomUniDestinoOrd(codDescUOR[1]);

        return registroVO;
    }

    private String getDescProcedimiento(String codProcedimiento, String[] params) {

        m_Log.debug("getNumeroExpediente");

        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            String sql = "SELECT PML_VALOR FROM E_PML WHERE PML_COD = '" + codProcedimiento + "'" +
                    " AND PML_CMP = 'NOM' AND PML_LENG = '"+ conf.getString("idiomaDefecto")+"'";

            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) return resultSet.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    resultSet.close();
                    preparedStatement.close();
                    oad.devolverConexion(con);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            }
        }

        m_Log.debug("getNumeroExpediente");
        return null;
    }

    private String getNombreCompleto(String nombre, String apellido1, String apellido2) {
        String nombreCompleto = nombre;
        if (apellido1 != null || !"".equals(apellido1)) nombreCompleto = apellido1 + ", " + nombreCompleto;
        if (apellido2 != null || !"".equals(apellido2)) nombreCompleto = apellido2 + " " + nombreCompleto;

        return nombreCompleto;
    }

    public void recuperarAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {

        int organizacion = usuarioVO.getOrgCod();
        String sgeTipoReg = tramitacionVO.getTipoRegistro();
        String ejercicio = tramitacionVO.getEjercicioRegistro();
        String numAnotacion = tramitacionVO.getNumero();
        String estadoAccede = m_constantes.getString("estadoPendienteAccede");
        String unidadOrganica = codUor2CodAccede(tramitacionVO.getCodUnidadRegistro(), params);
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement ps = null;
        String sql="";

        SWPisa_ParametrosBean dato = new SWPisa_ParametrosBean();
        dato.setOrganizacion(m_ct.getString("Pisa." + organizacion + ".organizacion"));
        dato.setEntidad(m_ct.getString("Pisa." + organizacion + ".entidad"));
        dato.setFechaDesde("");
        dato.setFechaHasta("");
        SWPisa_AnotacionesBean datoAnotacion = new SWPisa_AnotacionesBean();
        datoAnotacion.setEjercicio(ejercicio);
        datoAnotacion.setTipo(sgeTipoReg);
        datoAnotacion.setEstado(estadoAccede);
        datoAnotacion.setExpediente("");
        datoAnotacion.setNumero(numAnotacion);
        datoAnotacion.setEfecto("1");
        datoAnotacion.setUnidad_organica(unidadOrganica);
        dato.setAnotacion(datoAnotacion);
        dato.setEfecto("1");
        FachadaClientePisa fachadaPisa = FachadaClientePisa.getInstance(prefijoPropiedad);
        fachadaPisa.asignarExpediente(dato);
        oad = new AdaptadorSQLBD(params);
        try {
            con = oad.getConnection();

            sql = "DELETE E_EXREXT WHERE EXREXT_UOR= ? AND EXREXT_EJE= ? AND EXREXT_NRE= ? AND EXREXT_TIP = ?";
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setString(i++, tramitacionVO.getCodUnidadRegistro());
            ps.setString(i++, ejercicio);
            ps.setString(i++, numAnotacion);
            ps.setString(i++, sgeTipoReg);

            ps.executeUpdate();
        } catch (BDException ex) {
            Logger.getLogger(AccesoRegistroPisa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AccesoRegistroPisa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cambiaEstadoAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, int estado, String[] params)
            throws RegistroException {

        int organizacion = usuarioVO.getOrgCod();
        String sgeTipoReg = tramitacionVO.getTipoRegistro();
        String ejercicio = tramitacionVO.getEjercicioRegistro();
        String numAnotacion = tramitacionVO.getNumero();
        String uor = codUor2CodAccede(tramitacionVO.getCodUnidadRegistro(),params);

        SWPisa_ParametrosBean dato = new SWPisa_ParametrosBean();
        SWPisa_AnotacionesBean datoAnotacion = new SWPisa_AnotacionesBean();
        dato.setOrganizacion(m_ct.getString("Pisa." + organizacion + ".organizacion"));
        dato.setEntidad(m_ct.getString("Pisa." + organizacion + ".entidad"));
        dato.setEfecto("1");
        dato.setFechaDesde("");
        dato.setFechaHasta("");
        datoAnotacion.setEjercicio(ejercicio);
        datoAnotacion.setTipo(sgeTipoReg);
        datoAnotacion.setEstado(Integer.toString(estado));
        if (tramitacionVO.getNumeroExpediente()==null) datoAnotacion.setExpediente("");
        else datoAnotacion.setExpediente(tramitacionVO.getNumeroExpediente());
        datoAnotacion.setNumero(numAnotacion);
        datoAnotacion.setUnidad_organica(uor);
        datoAnotacion.setEfecto("1");
        dato.setAnotacion(datoAnotacion);

        FachadaClientePisa fachadaPisa = FachadaClientePisa.getInstance(prefijoPropiedad);
        fachadaPisa.asignarExpediente(dato);
    }

    public void adjuntarExpedientesDesdeUnidadTramitadora(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params) throws RegistroException {

        // Comprobamos que llega el numero de expediente.
        if (tramitacionVO.getNumeroExpediente() == null || tramitacionVO.getNumeroExpediente().equals("")) {
            if (m_Log.isDebugEnabled()) m_Log.debug("ERROR: EL NUMERO DE EXPEDIENTE ES NULO.");
            throw new RegistroException(new Exception(), "NO SE HA PUESTO EL NUMERO DE EXPEDIENTE A ADJUNTAR");
        }

        // Abrimos una conexion y su transaccion correspondiente.
        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        TramitacionDAO tramitacionDAO = TramitacionDAO.getInstance();
        Connection con = null;
        try {
            con = dbAdapter.getConnection();

            // Comprobamos que el expediente existe en la Base de Datos.
            if (tramitacionDAO.localizaExpediente(con, tramitacionVO) <= 0) {
                if (m_Log.isDebugEnabled()) m_Log.debug("ERROR: EL EXPEDIENTE A RELACIONAR NO EXISTE.");
                throw new RegistroException(new Exception(), "NO EXISTE EL EXPEDIENTE A ADJUNTAR");
            }
            // Relacionamos el asiento con el expediente.
            try {
                insertarRelExpedienteExterno(con, tramitacionVO, 1, "1");
            } catch (SQLException sqle) {
                if (m_Log.isDebugEnabled()) m_Log.debug("ERROR: NO SE HA PODIDO ASOCIAR EL EXPEDIENTE AL ASIENTO");
                rollBackTransaction(dbAdapter, con);
                throw new RegistroException(
                        new Exception(), "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
            }            

            cambiaEstadoAsiento(tramitacionVO, usuarioVO, 1, params);

            dbAdapter.finTransaccion(con);

        } catch (BDException bde) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION DE LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (TramitacionException te) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR ADJUNTAR EL ASIENTO AL EXPEDIENTE: " + te.getMessage());
            te.printStackTrace();
            throw new RegistroException(te, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (TechnicalException te) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR ADJUNTAR EL ASIENTO AL EXPEDIENTE: " + te.getMessage());
            te.printStackTrace();
            throw new RegistroException(te, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } finally {
            devolverConexion(dbAdapter, con);
        }
    }

    private void devolverConexion(AdaptadorSQLBD adapter, Connection con) throws RegistroException {
        try {
            if (con != null) adapter.devolverConexion(con);
        } catch (BDException bde) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR DEVOLVER LA CONEXION A LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO DEVOLVER LA CONEXION A LA BASE DE DATOS");
        }
    }

    public ArrayList<AsientoFichaExpedienteVO> cargaListaAsientosExpediente(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params) throws RegistroException {

        // Recuperamos los datos que utilizaremos en el metodo.
        String numeroExpediente = (String)generalVO.getAtributo("numero");
        int organizacion = usuarioVO.getOrgCod();
        HashMap uors = this.getUORsEnAccede(params, "");

        // Construimos el bean de los parametros de llamada al Servicio Web.
        SWPisa_ParametrosBean dato = new SWPisa_ParametrosBean();
        dato.setOrganizacion(m_ct.getString("Pisa." + organizacion + ".organizacion"));
        dato.setEntidad(m_ct.getString("Pisa." + organizacion + ".entidad"));
        dato.setFechaDesde("");
        dato.setFechaHasta("");
        dato.setEjercicio("");
        dato.setTipo("");
        dato.setEstado("");
        dato.setExpediente(numeroExpediente);
        dato.setNumeros("");
        dato.setEfecto("1");

        // Realizamos la llamada al Servicio Web.
        FachadaClientePisa fachadaPisa = FachadaClientePisa.getInstance(prefijoPropiedad);
        SWPisa_AnotacionesBean[] anotaciones = fachadaPisa.buscarEntradas(dato);

        ArrayList<AsientoFichaExpedienteVO> lista = new ArrayList();
        for (int i = 0; i < anotaciones.length; i++) {

            SWPisa_AnotacionesBean anotacion = anotaciones[i];
            if (!esAnotacionValida(anotacion, numeroExpediente,uors, params)) continue;
            AsientoFichaExpedienteVO asiento = new AsientoFichaExpedienteVO();
            asiento.setCodigoDepartamento(2);
            int cU = -1;            
            if (uors.get(anotacion.getUnidad_organica())!=null && !"".equals(uors.get(anotacion.getUnidad_organica()))) {
                cU = Integer.parseInt((String)uors.get(anotacion.getUnidad_organica()));
            }
            
            asiento.setCodigoUOR(cU);
            asiento.setTipoAsiento(anotacion.getTipo());
            int ej = 0;
            Long num = 0L;
            if (anotacion.getEjercicio()!=null && !"".equals(anotacion.getEjercicio())) {
                ej = Integer.parseInt(anotacion.getEjercicio());
            }
            if (anotacion.getNumero()!=null && !"".equals(anotacion.getNumero())) {
                num = Long.parseLong(anotacion.getNumero());
            }
            asiento.setEjercicioAsiento(ej);
            asiento.setNumeroAsiento(num);
            asiento.setFechaAsiento(anotacion.getFecha_1());
            asiento.setAsuntoAsiento(convertirMAYUS(anotacion.getExtracto()));
            SWPisa_InteresadosBean[] interesados = anotacion.getInteresados();
            String nombre = null;
            String apellido1 = null;
            String apellido2 = null;
            if (interesados != null && interesados.length > 0) {
                for (int k = 0; k < interesados.length; k++) {
                    String codigoTipoRelacion = interesados[k].getCodigo_tipo_relacion();
                    if (codigoTipoRelacion.equals("1")) {
                        nombre = interesados[k].getNombre();
                        apellido1 = interesados[k].getApellido1();
                        apellido2 = interesados[k].getApellido2();
                    }
                }
            }
            asiento.setNombreInteresado(convertirMAYUS(nombre));
            if (apellido1 != null) asiento.setApellido1Interesado(convertirMAYUS(apellido1));
            if (apellido2 != null) asiento.setApellido2Interesado(convertirMAYUS(apellido2));
            asiento.setOrigenAsiento("Pisa");
            
            lista.add(asiento);
            
        }
        return lista;
    }
    private boolean esAnotacionValida(SWPisa_AnotacionesBean anotacion,String numeroExpediente,HashMap uors, String[] params) throws RegistroException {
        boolean esValida = false;
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        PreparedStatement ps = null;
        ResultSet rs = null;
        int uor = -1;
        if (uors.get(anotacion.getUnidad_organica())!=null && !"".equals(uors.get(anotacion.getUnidad_organica()))) {
                uor = Integer.parseInt((String)uors.get(anotacion.getUnidad_organica()));
        }
        
        String sql = "SELECT EXREXT_NRE FROM E_EXREXT WHERE EXREXT_UOR = ? AND EXREXT_TIP = ? " +
                "AND EXREXT_NRE = ? AND EXREXT_SER = ? AND EXREXT_NUM = ?";
        m_Log.debug("sql esAnotacionValida" + sql);
        m_Log.debug("UOR: " + uor);
        m_Log.debug("Tipo: " + anotacion.getTipo());
        m_Log.debug("Numero: " + anotacion.getNumero());
        m_Log.debug("Servicio: Pisa");
        m_Log.debug("Expediente: " + numeroExpediente);
        Connection con = null;
        int i = 1;
        try {
            con = oad.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(i++, uor);
            ps.setString(i++, anotacion.getTipo());
            ps.setString(i++, anotacion.getNumero());
            ps.setString(i++, "Pisa");
            ps.setString(i++, numeroExpediente);
            rs = ps.executeQuery();
            esValida = rs.next();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RegistroException(ex, "Error sql viendo si una anotacion es valida");
        } catch (BDException ex) {
            ex.printStackTrace();
            throw new RegistroException(ex, "Error viendo si una anotacion es valida");
        }finally {
            cerrarPreparedStatement(ps);
            cerrarResultSet(rs);
            try {
                con.close();
            } catch (SQLException ex) {
                throw new RegistroException(ex, "Error cerrando conexion");
            }
        }


        return esValida;
    }
    private GeneralValueObject buscarTerceroDomicilio(GeneralValueObject gVO, Connection con)
            throws SQLException, BDException {

        String tipoDocumento = (String) gVO.getAtributo("tipoDocumento");
        String documento = (String) gVO.getAtributo("documento");
        String nombre = (String) gVO.getAtributo("nombre");
        String apellido1 = (String) gVO.getAtributo("apellido1");
        String apellido2 = (String) gVO.getAtributo("apellido2");
        String domicilio = (String) gVO.getAtributo("domicilio");
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String codProvincia = (String) gVO.getAtributo("codProvincia");
        String codPostal = (String) gVO.getAtributo("codPostal");

        String sqlBuscarTercero = "SELECT TER_COD, TER_NVE FROM T_TER WHERE TER_SIT = 'A' AND TER_TID = ? " +
                "AND TER_DOC = ? AND TER_NOM = ? ";
        if (apellido1 != null && !"".equals(apellido1)) sqlBuscarTercero += "AND TER_AP1 = '" + apellido1 + "' ";
        else sqlBuscarTercero += "AND (TER_AP1 IS NULL OR TER_AP1 = '') ";
        if (apellido2 != null && !"".equals(apellido2)) sqlBuscarTercero += "AND TER_AP2 = '" + apellido2 + "' ";
        else sqlBuscarTercero += "AND (TER_AP2 IS NULL OR TER_AP2 = '') ";

        String sqlBuscarDomicilio = "SELECT DNN_DOM FROM T_DNN WHERE DNN_SIT = 'A' AND DNN_DMC = ? AND DNN_PRV = ? " +
                "AND DNN_MUN = ? ";
        if (codPostal != null && !"".equals(codPostal)) sqlBuscarDomicilio += "AND DNN_CPO = '" + codPostal + "' ";
        else sqlBuscarDomicilio += "AND (DNN_CPO IS NULL OR DNN_CPO = '') ";

        String sqlExisteRelacion = "SELECT * FROM T_DOT WHERE DOT_DOM = ? AND DOT_TER = ? AND DOT_SIT = 'A'";

        PreparedStatement ps = null;
        ResultSet rs = null;

        GeneralValueObject codigosGVO = new GeneralValueObject();
        codigosGVO.setAtributo("codTercero", "-1");
        codigosGVO.setAtributo("numVersion", "-1");
        codigosGVO.setAtributo("codDomicilio", "-1");
        codigosGVO.setAtributo("existeRelacion", "-1");

        try {
            // Buscamos el tercero.
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR UN TERCERO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscarTercero);

            ps = con.prepareStatement(sqlBuscarTercero);
            ps.setInt(1, Integer.parseInt(tipoDocumento));
            ps.setString(2, documento);
            ps.setString(3, nombre);
            rs = ps.executeQuery();

            String codTercero = null;
            if (rs.next()) {
                int i = 1;
                codTercero = Integer.toString(rs.getInt(i++));
                String numVersion = Integer.toString(rs.getInt(i));
                codigosGVO.setAtributo("codTercero", codTercero);
                codigosGVO.setAtributo("numVersion", numVersion);
            }

            rs.close();
            ps.close();

            // Buscamos el domicilio;
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR UN DOMICILIO");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscarDomicilio);

            ps = con.prepareStatement(sqlBuscarDomicilio);
            ps.setString(1, domicilio);
            ps.setInt(2, Integer.parseInt(codProvincia));
            ps.setInt(3, Integer.parseInt(codMunicipio));
            rs = ps.executeQuery();

            String codDomicilio = null;
            if (rs.next()) {
                codDomicilio = Integer.toString(rs.getInt(1));
                codigosGVO.setAtributo("codDomicilio", codDomicilio);
            }

            rs.close();
            ps.close();

            if (codTercero != null && codDomicilio != null) {
                if (m_Log.isDebugEnabled())
                    m_Log.debug("CONSULTA PARA COMPROBAR SI EXISTE RELACION ENTRE UN TERCERO Y UN DOMICILIO");
                if (m_Log.isDebugEnabled()) m_Log.debug(sqlExisteRelacion);

                ps = con.prepareStatement(sqlExisteRelacion);
                int i = 1;
                ps.setInt(i++, Integer.parseInt(codDomicilio));
                ps.setInt(i, Integer.parseInt(codTercero));

                rs = ps.executeQuery();
                if (rs.next()) codigosGVO.setAtributo("existeRelacion", "1");
            }

            return codigosGVO;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw sqle;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }

    private int obtenerNuevoCodigoDomicilio(AdaptadorSQLBD abd, Connection con) throws SQLException {

        String sqlQuery = "SELECT " + abd.funcionMatematica(
                AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[]{"DOM_COD"}) + " AS MAXIMO FROM T_DOM";
        String sqlInsert = "INSERT INTO T_DOM (DOM_COD, DOM_NML) VALUES (?, 2)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sqlQuery);
            rs = ps.executeQuery();

            int nuevoCodigo = 0;
            if (rs.next()) {
                nuevoCodigo = rs.getInt(1);
            }
            nuevoCodigo++;

            rs.close();
            ps.close();

            ps = con.prepareStatement(sqlInsert);
            ps.setInt(1, nuevoCodigo);

            int insertedRows = ps.executeUpdate();
            if (insertedRows != 1) throw new SQLException("ERROR EN LA GENERACION DEL NUEVO CODIGO DE DOMICILIO");

            return nuevoCodigo;

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }


    private GeneralValueObject insertarTerceroDomicilio(GeneralValueObject codigosGVO, GeneralValueObject gVO, AdaptadorSQLBD abd, Connection con)
            throws SQLException, BDException {

        int codTercero = Integer.parseInt((String) codigosGVO.getAtributo("codTercero"));
        int numVersion = Integer.parseInt((String) codigosGVO.getAtributo("numVersion"));
        int codDomicilio = Integer.parseInt((String) codigosGVO.getAtributo("codDomicilio"));
        int existeRelacion = Integer.parseInt((String) codigosGVO.getAtributo("existeRelacion"));

        String sqlInsertTercero = "INSERT INTO T_TER (TER_COD, TER_TID, TER_DOC, TER_NOM, TER_AP1, TER_PA1, " +
                "TER_AP2, TER_PA2, TER_NOC, TER_NML, TER_TLF, TER_DCE, TER_SIT, TER_NVE, TER_FAL, TER_UAL, " +
                "TER_APL, TER_FBJ, TER_UBJ) " +
                "VALUES (?, ?, ?, ?, ?, NULL, ?, NULL, ?, 0, ?, ?, 'A', 1, " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, ?, NULL, NULL)";

        String sqlInsertDomicilio = "INSERT INTO T_DNN (DNN_DOM, DNN_TVI, DNN_PAI, DNN_PRV, DNN_MUN, DNN_VPA, " +
                "DNN_VPR, DNN_VMU, DNN_VIA, DNN_SPA, DNN_SPR, DNN_SMU, DNN_ESI, DNN_NUD, DNN_LED, DNN_NUH, " +
                "DNN_LEH, DNN_BLQ, DNN_POR, DNN_ESC, DNN_PLT, DNN_PTA, DNN_DMC, DNN_CPO, DNN_SIT, DNN_FAL, " +
                "DNN_UAL, DNN_FBJ, DNN_UBJ, DNN_RCA) " +
                "VALUES (?, 0, 108, ?, ?, 108, ?, ?, 0, 108, ?, ?, NULL, NULL, NULL, NULL, NULL, " +
                "NULL, NULL, NULL, NULL, NULL, ?, ?, 'A', " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, NULL, NULL, NULL)";

        String sqlInsertHistorico = "INSERT INTO T_HTE (HTE_TER, HTE_NVR, HTE_DOT, HTE_TID, HTE_DOC, HTE_NOM, " +
                "HTE_AP1, HTE_PA1, HTE_AP2, HTE_PA2, HTE_NOC, HTE_NML, HTE_TLF, HTE_DCE, HTE_FOP, HTE_USU, HTE_APL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, NULL, ?, 0, ?, ?," + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, ?)";

        String sqlInsertTerDom = "INSERT INTO T_DOT (DOT_DOM, DOT_TER, DOT_TOC, DOT_SIT, DOT_FEC, DOT_USU, DOT_DPA) " +
                "VALUES (?, ?, NULL, 'A'," + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, 0)";

        String sqlUpdateTercero = "UPDATE T_TER SET TER_NVE = ? WHERE TER_COD = ?";

        PreparedStatement ps = null;

        int actualizaHistorico;
        if (codTercero == -1) actualizaHistorico = 2;
        else if (codTercero != -1 && codDomicilio != -1 && existeRelacion == 1) actualizaHistorico = 0;
        else actualizaHistorico = 1;

        try {
            // Insertamos el tercero.
            if (codTercero == -1) {
                codTercero = obtenerNuevoCodigoTercero(abd, con);

                if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UN TERCERO");
                if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertTercero);

                ps = con.prepareStatement(sqlInsertTercero);
                int i = 1;
                ps.setInt(i++, codTercero);
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("tipoDocumento")));
                ps.setString(i++, (String) gVO.getAtributo("documento"));
                String nombre = (String) gVO.getAtributo("nombre");
                String apellido1 = (String) gVO.getAtributo("apellido1");
                String apellido2 = (String) gVO.getAtributo("apellido2");
                String nombreCompleto = "";
                if (apellido1 != null && !"".equals(apellido1)) nombreCompleto = apellido1;
                if (apellido2 != null && !"".equals(apellido2)) nombreCompleto += " " + apellido2;
                if (nombre != null && !"".equals(nombre)) {
                    if (nombreCompleto.length() > 0) nombreCompleto += ", " + nombre;
                    else nombreCompleto = nombre;
                }
                ps.setString(i++, nombre);
                ps.setString(i++, apellido1);
                ps.setString(i++, apellido2);
                ps.setString(i++, nombreCompleto);
                ps.setString(i++, (String) gVO.getAtributo("telefono"));
                ps.setString(i++, (String) gVO.getAtributo("email"));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codUsuario")));
                ps.setInt(i, Integer.parseInt((String) gVO.getAtributo("codAplicacion")));

                int insertedRows = ps.executeUpdate();
                if (insertedRows != 1) throw new SQLException("ERROR EN LAS CONSULTAS DE SQL");

                ps.close();
                numVersion = 1;
            }

            if (codDomicilio == -1) {
                // Insertamos el domicilio
                if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UN DOMICILIO");
                if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertDomicilio);

                codDomicilio = obtenerNuevoCodigoDomicilio(abd, con);
                ps = con.prepareStatement(sqlInsertDomicilio);
                int i = 1;
                ps.setInt(i++, codDomicilio);
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codProvincia")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codProvincia")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codProvincia")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
                ps.setString(i++, (String) gVO.getAtributo("domicilio"));
                ps.setString(i++, (String) gVO.getAtributo("codPostal"));
                ps.setInt(i, Integer.parseInt((String) gVO.getAtributo("codUsuario")));

                int insertedRows = ps.executeUpdate();
                if (insertedRows != 1) throw new SQLException("ERROR EN LAS CONSULTAS DE SQL");

                ps.close();

            }

            if (existeRelacion == -1) {
                if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR LA RELACION ENTRE TERCERO Y DOMICILIO");
                if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertTerDom);

                ps = con.prepareStatement(sqlInsertTerDom);
                int i = 1;
                ps.setInt(i++, codDomicilio);
                ps.setInt(i++, codTercero);
                ps.setInt(i, Integer.parseInt((String) gVO.getAtributo("codUsuario")));

                int insertedRows = ps.executeUpdate();
                if (insertedRows != 1) throw new SQLException("ERROR EN LAS CONSULTAS DE SQL");

                ps.close();
            }

            if (actualizaHistorico != 0) {
                if (actualizaHistorico == 1) numVersion++;
                ps = con.prepareStatement(sqlInsertHistorico);
                int i = 1;
                ps.setInt(i++, codTercero);
                ps.setInt(i++, numVersion);
                ps.setInt(i++, codDomicilio);
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("tipoDocumento")));
                ps.setString(i++, (String) gVO.getAtributo("documento"));
                String nombre = (String) gVO.getAtributo("nombre");
                String apellido1 = (String) gVO.getAtributo("apellido1");
                String apellido2 = (String) gVO.getAtributo("apellido2");
                String nombreCompleto = "";
                if (apellido1 != null && !"".equals(apellido1)) nombreCompleto = apellido1;
                if (apellido2 != null && !"".equals(apellido2)) nombreCompleto += " " + apellido2;
                if (nombre != null && !"".equals(nombre)) {
                    if (nombreCompleto.length() > 0) nombreCompleto += ", " + nombre;
                    else nombreCompleto = nombre;
                }
                ps.setString(i++, nombre);
                ps.setString(i++, apellido1);
                ps.setString(i++, apellido2);
                ps.setString(i++, nombreCompleto);
                ps.setString(i++, (String) gVO.getAtributo("telefono"));
                ps.setString(i++, (String) gVO.getAtributo("email"));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codUsuario")));
                ps.setInt(i, Integer.parseInt((String) gVO.getAtributo("codAplicacion")));

                int insertedRows = ps.executeUpdate();
                if (insertedRows != 1) throw new SQLException("ERROR EN LAS CONSULTAS DE SQL");

                ps.close();
            }

            if (actualizaHistorico == 1) {
                ps = con.prepareStatement(sqlUpdateTercero);
                int i = 1;
                ps.setInt(i++, numVersion);
                ps.setInt(i, codTercero);

                int insertedRows = ps.executeUpdate();
                if (insertedRows != 1) throw new SQLException("ERROR EN LAS CONSULTAS DE SQL");

                ps.close();
            }

            codigosGVO.setAtributo("codTercero", Integer.toString(codTercero));
            codigosGVO.setAtributo("codDomicilio", Integer.toString(codDomicilio));
            codigosGVO.setAtributo("numVersion", Integer.toString(numVersion));
            codigosGVO.setAtributo("codigoTipoRelacion", gVO.getAtributo("codigoTipoRelacion"));

            return codigosGVO;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw sqle;
        } finally {
            if (ps != null) ps.close();
        }
    }

    private int obtenerNuevoCodigoTercero(AdaptadorSQLBD abd, Connection con) throws SQLException {

        String sqlQuery = "SELECT " + abd.funcionMatematica(
                AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[]{"TER_COD"}) + " AS MAXIMO FROM T_TER";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(sqlQuery);
            rs = ps.executeQuery();

            int nuevoCodigo = 0;
            if (rs.next()) {
                nuevoCodigo = rs.getInt(1);
            }
            return ++nuevoCodigo;

        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }

    public void iniciarExpedienteAsiento(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params) throws RegistroException {

        Connection con = null;
        AdaptadorSQLBD oad = null;
        int resInicioTramite = -1;
        String estadoAccede = Integer.toString(estadoSgeToAccede(Integer.parseInt((String)generalVO.getAtributo("estAnotacion"))));

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);

            String tipoAsiento = (String) generalVO.getAtributo("tipoAsiento");
            String ejercicioAsiento = (String) generalVO.getAtributo("ejercicioAsiento");
            String numeroAsiento = (String) generalVO.getAtributo("numeroAsiento");
            String unidadOrganicaAccede = codUor2CodAccede((String)generalVO.getAtributo("codUnidadRegistro"), params);
            int organizacion = usuarioVO.getOrgCod();

            // En primer lugar recuperamos un numero de expediente.
            TramitacionValueObject tVO = new TramitacionValueObject();
            tVO.setCodProcedimiento((String)generalVO.getAtributo("codProcedimiento"));
            // #303601: comprobamos si el procedimiento acepta que sus expedientes se numeren al ser iniciados a partir del anho del asiento.
            if(((String) generalVO.getAtributo("numeracionExpediente")).equals("anoAsiento")){
                tVO.setEjercicioRegistro(ejercicioAsiento);
            } 
            TramitacionDAO.getInstance().getNuevoExpediente(usuarioVO, tVO, con);
            String numeroExpediente = tVO.getNumero();            
            StringTokenizer tokenizer = new StringTokenizer(numeroExpediente, "/");
            generalVO.setAtributo("ejercicio", tokenizer.nextToken());
            generalVO.setAtributo("numero", numeroExpediente);    

            // Construimos el bean de los parametros de llamada al Servicio Web.
            SWPisa_ParametrosBean dato = new SWPisa_ParametrosBean();
            dato.setOrganizacion(m_ct.getString("Pisa." + organizacion + ".organizacion"));
            dato.setEntidad(m_ct.getString("Pisa." + organizacion + ".entidad"));
            dato.setFechaDesde("");
            dato.setFechaHasta("");
            dato.setEjercicio(ejercicioAsiento);
            dato.setTipo(tipoAsiento);
            dato.setEstado(estadoAccede);
            dato.setExpediente("");
            dato.setNumeros(numeroAsiento);
            dato.setUnidad(unidadOrganicaAccede);
            dato.setEfecto("1");

            // No se dispone del codigo de tercero, por lo que vamos a buscar la entrada y
            // recuperarlo.
            FachadaClientePisa fachadaPisa = FachadaClientePisa.getInstance(prefijoPropiedad);

            SWPisa_AnotacionesBean[] anotaciones = fachadaPisa.buscarEntradas(dato);
            SWPisa_AnotacionesBean anotacion = anotaciones[0];

            SWPisa_InteresadosBean[] interesados = anotacion.getInteresados();
            GeneralValueObject[] interesadosGVO = new GeneralValueObject[interesados.length];

            if (interesados != null && interesados.length > 0) {
                for (int k = 0; k < interesados.length; k++) {
                    GeneralValueObject oneInteresado = new GeneralValueObject();
                    oneInteresado.setAtributo("codigoTipoRelacion", interesados[k].getCodigo_tipo_relacion());
                    oneInteresado.setAtributo("tipoDocumento", m_TipoDocs.getString(interesados[k].getCodigo_tipo_documento()));
                    oneInteresado.setAtributo("documento", convertirMAYUS(interesados[k].getDocumento()));
                    oneInteresado.setAtributo("nombre", convertirMAYUS(interesados[k].getNombre()));
                    oneInteresado.setAtributo("apellido1", convertirMAYUS(interesados[k].getApellido1()));
                    oneInteresado.setAtributo("apellido2", convertirMAYUS(interesados[k].getApellido2()));
                    oneInteresado.setAtributo("telefono", interesados[k].getTelefono());
                    oneInteresado.setAtributo("email", convertirMAYUS(interesados[k].getEmail()));
                    oneInteresado.setAtributo("domicilio", convertirMAYUS(interesados[k].getDomicilio()));
                    oneInteresado.setAtributo("codPostal", interesados[k].getCodigo_postal());
                    oneInteresado.setAtributo("codUsuario", generalVO.getAtributo("usuario"));
                    oneInteresado.setAtributo("codAplicacion", generalVO.getAtributo("codAplicacion"));

                    String descMunicipio = eliminarTildes(convertirMAYUS(interesados[k].getMunicipio()));
                    String strMunicipio = interesados[k].getCodigo_municipio();
                    String strProvincia = interesados[k].getCodigo_provincia();

                    // Definimos los codigos de municipio y provincia en caso de que no existan.
                    int prvPorDefc = Integer.parseInt(m_constantes.getString("provincia.desconocida"));
                    int munPorDefc = Integer.parseInt(m_constantes.getString("municipio.desconocido"));
                    int codProvincia = prvPorDefc;
                    int codMunicipio = munPorDefc;
                    if (strProvincia != null && !"".equals(strProvincia)) codProvincia = Integer.parseInt(strProvincia);
                    if (strMunicipio != null && !"".equals(strMunicipio)) codMunicipio = Integer.parseInt(strMunicipio);

                    if (codMunicipio == munPorDefc) {
                        GeneralValueObject cods = getMunicipioByNombre(con, codProvincia, descMunicipio);
                        if (!(cods.getAtributo("codMunicipio")).equals("-1"))
                            codMunicipio = Integer.parseInt((String) cods.getAtributo("codMunicipio"));
                        if (!(cods.getAtributo("codProvincia")).equals("-1"))
                            codProvincia = Integer.parseInt((String) cods.getAtributo("codProvincia"));
                    }

                    oneInteresado.setAtributo("municipio", descMunicipio);
                    oneInteresado.setAtributo("codMunicipio", Integer.toString(codMunicipio));
                    oneInteresado.setAtributo("codProvincia", Integer.toString(codProvincia));

                    interesadosGVO[k] = oneInteresado;
                }
            }

            // Buscaremos los diferentes terceros en la BBDD del SGE.
            GeneralValueObject[] codsTercsExpediente = new GeneralValueObject[interesadosGVO.length];
            if (interesadosGVO != null && interesadosGVO.length > 0) {
                for (int k = 0; k < interesadosGVO.length; k++) {
                    GeneralValueObject codsTerceros = buscarTerceroDomicilio(interesadosGVO[k], con);
                    codsTercsExpediente[k] = insertarTerceroDomicilio(codsTerceros, interesadosGVO[k], oad, con);
                }
            }

            insertarExpediente(oad, con, generalVO, codsTercsExpediente);

            dato = new SWPisa_ParametrosBean();
            dato.setOrganizacion(m_ct.getString("Pisa." + organizacion + ".organizacion"));
            dato.setEntidad(m_ct.getString("Pisa." + organizacion + ".entidad"));
            dato.setFechaDesde("");
            dato.setFechaHasta("");
            dato.setEfecto("1");
            SWPisa_AnotacionesBean datoAnotacion = new SWPisa_AnotacionesBean();
            datoAnotacion.setEjercicio(ejercicioAsiento);
            datoAnotacion.setTipo(tipoAsiento);
            datoAnotacion.setEstado("1");
            datoAnotacion.setExpediente(numeroExpediente);
            datoAnotacion.setNumero(numeroAsiento);
            datoAnotacion.setEfecto("1");
           
            datoAnotacion.setUnidad_organica(unidadOrganicaAccede);
            
            dato.setAnotacion(datoAnotacion);           

            fachadaPisa.asignarExpediente(dato);

            resInicioTramite = TramitesExpedienteDAO.getInstance().iniciarTramiteInicio(oad, con, generalVO);

            if (resInicioTramite <= 0) {
                rollBackTransaction(oad, con);
                throw new RegistroException(new SQLException(), "ERROR AL INICIAR EL TRAMITE DE INICIO");
            }
            // Creamos la estructura para poder relacionae anotacion y expediente.
            TramitacionValueObject tramitacionVO = new TramitacionValueObject();
            tramitacionVO.setCodMunicipio((String)generalVO.getAtributo("codMunicipio"));
            tramitacionVO.setCodDepartamento((String)generalVO.getAtributo("codDepartamento"));
            tramitacionVO.setCodUnidadRegistro((String)generalVO.getAtributo("codUnidadRegistro"));
            tramitacionVO.setTipoRegistro((String)generalVO.getAtributo("tipoAsiento"));
            tramitacionVO.setEjercicioRegistro((String)generalVO.getAtributo("ejercicioAsiento"));
            tramitacionVO.setNumero((String)generalVO.getAtributo("numeroAsiento"));
            tramitacionVO.setNumeroExpediente((String)generalVO.getAtributo("numero"));
            tramitacionVO.setCodProcedimiento((String)generalVO.getAtributo("codProcedimiento"));            
            // Relacionamos el asiento con el expediente.
            try {
                insertarRelExpedienteExterno(con, tramitacionVO, 1, "1");
            } catch (SQLException sqle) {
                if (m_Log.isDebugEnabled()) m_Log.debug("ERROR: NO SE HA PODIDO ASOCIAR EL EXPEDIENTE AL ASIENTO");
                rollBackTransaction(oad, con);
                throw new RegistroException(
                        new Exception(), "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
            }            

            oad.finTransaccion(con);
            if(resInicioTramite > 0)
                TramitesExpedienteDAO.getInstance().ejecutarOperacionesAlIniciarTramiteInicio(generalVO, params);
        } catch (SQLException e) {
            rollBackTransaction(oad, con);
            e.printStackTrace();
        } catch (BDException e) {
            rollBackTransaction(oad, con);
            e.printStackTrace();
        } catch (TechnicalException e) {
            rollBackTransaction(oad, con);
            e.printStackTrace();
        } catch (TramitacionException e) {
            rollBackTransaction(oad, con);
            e.printStackTrace();
        } finally {
            devolverConexion(oad, con);
        }
    }
    
    private void insertarRelExpedienteExterno(Connection con, TramitacionValueObject tvo, int ori, String tipoOp)
            throws SQLException {

        String sqlInsertAnotacion = "INSERT INTO E_EXREXT (EXREXT_UOR, EXREXT_TIP, EXREXT_EJR, EXREXT_NRE, EXREXT_MUN, " +
                "EXREXT_NUM, EXREXT_ORI, EXREXT_TOP, EXREXT_SER, EXREXT_PRO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps;

        ps = con.prepareStatement(sqlInsertAnotacion);
        int i = 1;
        ps.setInt(i++, Integer.parseInt(tvo.getCodUnidadRegistro()));
        ps.setString(i++, tvo.getTipoRegistro());
        ps.setInt(i++, Integer.parseInt(tvo.getEjercicioRegistro()));
        ps.setInt(i++, Integer.parseInt(tvo.getNumero()));
        ps.setInt(i++, Integer.parseInt(tvo.getCodMunicipio()));
        ps.setString(i++, tvo.getNumeroExpediente());
        ps.setInt(i++, ori);
        ps.setString(i++, tipoOp);
        ps.setString(i++, nombreServicio);
        ps.setString(i, tvo.getCodProcedimiento());

        int insertedRows = ps.executeUpdate();

        if (insertedRows != 1) throw new SQLException("ERROR EN EL VALOR DEVUELTO POR LA CONSULTA A BBDD " +
                "(Valor = " + insertedRows);

    }    

    private void rollBackTransaction(AdaptadorSQLBD adapter, Connection con) throws RegistroException {
        try {
            if (con != null) adapter.rollBack(con);
        } catch (BDException bde) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR HACER ROLLBACK DE LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        }
    }

    /**
     * Inserta un expediente en la BBDD de Agora.
     *
     * @param oad         --> Adaptador SQL de la BBDD.
     * @param con         --> Conexion con la BBDD.
     * @param generalVO   --> General Value Object con los datos del expediente.
     * @param interesados --> Array de General Value Objects con los datos de los interesados.
     * @throws es.altia.agora.webservice.registro.exceptions.RegistroException En caso de que se produzca algún error.
     */
    private void insertarExpediente(AdaptadorSQLBD oad, Connection con, GeneralValueObject generalVO,
                                    GeneralValueObject[] interesados) throws RegistroException {

        // Ponemos los datos de codigo de Tercero y rol a nulo para que no grabe ningun tercero.
        generalVO.setAtributo("codTercero", "");
        generalVO.setAtributo("codRol", "");

        try {
// Grabamos los datos generales del expediente.
            int resInsercion = ExpedientesDAO.getInstance().insertarExpediente(oad, con, generalVO);
            if (resInsercion != 1) throw new RegistroException(new SQLException(), "ERROR EN LAS CONSULTAS PARA INSERTAR EXPEDIENTE");

            // Recuperamos la coleccion de roles relacionados con el expediente.
            Vector roles = getRolesProcedimiento(con, generalVO);
            // Ahora asociamos los interesados de la anotacion como interesados del expediente.
            asociarInteresados(con, roles, interesados, generalVO);
        } catch (Exception sqle) {
            sqle.printStackTrace();
            throw new RegistroException(sqle, "ERROR EN LAS CONSULTAS PARA INSERTAR EXPEDIENTE");
        }
    }

    private void asociarInteresados(Connection con, Vector roles, GeneralValueObject[] interesados,
                                    GeneralValueObject infoExpediente) throws RegistroException {

        String sqlInsertInteresado = "INSERT INTO E_EXT (EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_DOT, " +
                "EXT_ROL, EXT_PRO, MOSTRAR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";

        String codMunicipio = (String) infoExpediente.getAtributo("codMunicipio");
        String ejercicio = (String) infoExpediente.getAtributo("ejercicio");
        String numExpediente = (String) infoExpediente.getAtributo("numero");
        String codProcedimiento = (String) infoExpediente.getAtributo("codProcedimiento");

        PreparedStatement ps = null;

        try {
            for (int i = 0; i < interesados.length; i++) {
                String codTercero = (String) interesados[i].getAtributo("codTercero");
                String numVersion = (String) interesados[i].getAtributo("numVersion");
                String codDomicilio = (String) interesados[i].getAtributo("codDomicilio");
                String codRolTraducido = Integer.toString(getCodRolPorDefecto(codProcedimiento, con));
                for (int j = 0; j < roles.size(); j++) {
                    GeneralValueObject rol = (GeneralValueObject) roles.get(j);
                    String codigoRol = (String) rol.getAtributo("codigoRol");
                    if (codigoRol.equals(codRolTraducido)) {
                        ps = con.prepareStatement(sqlInsertInteresado);
                        int index = 1;
                        ps.setInt(index++, Integer.parseInt(codMunicipio));
                        ps.setInt(index++, Integer.parseInt(ejercicio));
                        ps.setString(index++, numExpediente);
                        ps.setInt(index++, Integer.parseInt(codTercero));
                        ps.setInt(index++, Integer.parseInt(numVersion));
                        ps.setInt(index++, Integer.parseInt(codDomicilio));
                        ps.setInt(index++, Integer.parseInt(codigoRol));
                        ps.setString(index, codProcedimiento);

                        int insertedRows = ps.executeUpdate();
                        if (insertedRows != 1) throw new RegistroException(
                                new SQLException(), "ERROR EN LAS CONSULTAS PAR INSERTAR INTERESADOS");
                        ps.close();
                    }
                }
            }
        } catch (TechnicalException ex) {
            throw new RegistroException(ex, "ERROR EN LAS CONSULTAS PAR INSERTAR INTERESADOS");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RegistroException(sqle, "ERROR EN LAS CONSULTAS PAR INSERTAR INTERESADOS");
        } finally {
            cerrarStatement(ps);
        }
    }


    private Vector getRolesProcedimiento(Connection con, GeneralValueObject gVO) throws RegistroException {

        String sqlQuery = "SELECT ROL_COD, ROL_DES, ROL_PDE FROM E_ROL WHERE ROL_MUN = ? AND ROL_PRO = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlQuery);
            int i = 1;
            ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
            ps.setString(i, (String) gVO.getAtributo("codProcedimiento"));

            rs = ps.executeQuery();

            Vector roles = new Vector();
            while (rs.next()) {
                i = 1;
                GeneralValueObject rolGVO = new GeneralValueObject();
                rolGVO.setAtributo("codigoRol", rs.getString(i++));
                rolGVO.setAtributo("descripcionRol", rs.getString(i++));
                rolGVO.setAtributo("porDefecto", rs.getString(i));
                roles.add(rolGVO);
            }

            return roles;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RegistroException(sqle, "ERROR AL REALIZAR LAS CONSULTAS PARA RECUPERAR LOS ROLES DE LA BASE DE DATOS");
        } finally {
            cerrarResultSet(rs);
            cerrarStatement(ps);
        }
    }

    private void cerrarStatement(Statement stmt) throws RegistroException {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR CERRAR EL STATEMENT DE LA CONSULTA: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO REALIZAR LA CONSULTA POR PROBLEMAS TECNICOS");
        }
    }

     private void cerrarPreparedStatement(PreparedStatement ps) throws RegistroException {
        try {
            if (ps != null) ps.close();
        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR CERRAR EL STATEMENT DE LA CONSULTA: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO REALIZAR LA CONSULTA POR PROBLEMAS TECNICOS");
        }
    }

    private void cerrarResultSet(ResultSet rs) throws RegistroException {
        try {
            if (rs != null) rs.close();
        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR CERRAR EL RESULT SET DE LA CONSULTA: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO REALIZAR LA CONSULTA POR PROBLEMAS TECNICOS");
        }
    }

    private GeneralValueObject getMunicipioByNombre(Connection con, int codProvincia, String descMunicipio) throws SQLException {

        String sqlQuery = "SELECT MUN_COD, MUN_PRV FROM " + GlobalNames.ESQUEMA_GENERICO + "T_MUN " +
                "WHERE MUN_PAI = 108 AND (MUN_NOM = '" + descMunicipio + "' OR MUN_NOL = '" + descMunicipio + "') ";
        int prvPorDefc = Integer.parseInt(m_constantes.getString("provincia.desconocida"));
        if (codProvincia != prvPorDefc) sqlQuery += "AND MUN_PRV = " + codProvincia;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER CODIGOS DE MUNICIPIO Y PROVINCIA");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlQuery);

            ps = con.prepareStatement(sqlQuery);
            rs = ps.executeQuery();

            GeneralValueObject codMunAndPrv = new GeneralValueObject();
            codMunAndPrv.setAtributo("codMunicipio", "-1");
            codMunAndPrv.setAtributo("codProvincia", "-1");
            if (rs.next()) {
                codMunAndPrv.setAtributo("codMunicipio", rs.getString(1));
                codMunAndPrv.setAtributo("codProvincia", rs.getString(2));
            }

            return codMunAndPrv;
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }

    private String eliminarTildes(String valor) {
        if (("".equals(valor)) || (valor == null)) {
            return valor;
        } else {
            valor = valor.replace('Á', 'A');
            valor = valor.replace('É', 'E');
            valor = valor.replace('Í', 'I');
            valor = valor.replace('Ó', 'O');
            valor = valor.replace('Ú', 'U');
            return valor;
        }
    }

    private HashMap getListaUorsUsuario(String[] params, UsuarioValueObject uVO) throws BDException, SQLException {

        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        HashMap lista = new HashMap();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = oad.getConnection();

            String sql = "SELECT UOR_COD_ACCEDE, UOR_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "A_UOU, A_UOR " +
                    " WHERE UOR_COD = UOU_UOR AND UOU_USU = ? AND UOU_ORG = ? AND UOU_ENT = ?" +
                    " AND UOR_COD_ACCEDE IS NOT NULL";

            if (m_Log.isDebugEnabled()) m_Log.debug("ServicioSWPisa : Get Lista Uors --> " + sql);
            ps = con.prepareStatement(sql);

            int i = 1;
            ps.setInt(i++, uVO.getIdUsuario());
            ps.setInt(i++, uVO.getOrgCod());
            ps.setInt(i, uVO.getEntCod());
            rs = ps.executeQuery();

            while (rs.next()) {
                String codAccede = rs.getString(1);
                String codUOR = rs.getString(2);
                if (!"".equals(codAccede) && codAccede != null) {
                    lista.put(codAccede, codUOR);
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            oad.devolverConexion(con);
        }

        if (m_Log.isDebugEnabled()) m_Log.debug("ServicioSWPisa : Get Lista Uors  --> numero unidades " + lista.size());
        return lista;
    }

    private String convertirMAYUS(String valor) {
        if (("".equals(valor)) || (valor == null)) return valor;
        else{
            valor = valor.replace('\"', '\'');
            return valor.toUpperCase();
        }
    }

    private Vector getAnotacionFromSWPisa(SWPisa_ParametrosBean dato, HashMap uorsUsuario, String origen)
            throws RegistroException {

        FachadaClientePisa fachadaPisa = FachadaClientePisa.getInstance(prefijoPropiedad);

        SWPisa_AnotacionesBean[] anotaciones = fachadaPisa.buscarEntradas(dato);
        Vector listaAnotaciones = new Vector();

        for (int i = 0; i < anotaciones.length; i++) {
            SWPisa_AnotacionesBean anotacion = anotaciones[i];
            TramitacionValueObject tvo1 = new TramitacionValueObject();
            tvo1.setCodDepartamento("2"); //este parametro va a desaparecer de la base de datos
            tvo1.setCodUnidadRegistro((String) uorsUsuario.get(anotacion.getUnidad_organica()));
            tvo1.setTipoRegistro(anotacion.getTipo());
            tvo1.setEjerNum(anotacion.getEjercicio() + "/" + anotacion.getNumero());
            tvo1.setFechaAnotacion(anotacion.getFecha_1());
            tvo1.setAsunto(convertirMAYUS(anotacion.getExtracto()));
            tvo1.setNumTerceros("1");//por el momento no soporta multiinteresado
            String descRemitente = "";
            SWPisa_InteresadosBean[] interesados = anotacion.getInteresados();
            if (interesados != null && interesados.length > 0) {
                for (int k = 0; k < interesados.length; k++) {
                    String codigoTipoRelacion = interesados[k].getCodigo_tipo_relacion();
                    if (codigoTipoRelacion.equals("1")) {
                        String apellido1 = interesados[k].getApellido1();
                        String apellido2 = interesados[k].getApellido2();
                        if (apellido1 != null && !("").equals(apellido1)) descRemitente += apellido1 + " ";
                        if (apellido2 != null && !("").equals(apellido2)) descRemitente += apellido2 + ", ";
                        else if (descRemitente.length() > 0)
                            descRemitente = descRemitente.substring(0, descRemitente.length() - 1) + ", ";
                        descRemitente += interesados[k].getNombre();
                        descRemitente = convertirMAYUS(descRemitente);
                    }
                }
            }
            descRemitente = convertirMAYUS(descRemitente);

            tvo1.setRemitente(descRemitente);
            tvo1.setEstado(Integer.toString(estadoAccedeToSge(Integer.parseInt(anotacion.getEstado()))));
            m_Log.debug("El estado de la anotacion " + anotacion.getNumero() + " es " + anotacion.getEstado());
            tvo1.setOrigen(origen);
            listaAnotaciones.addElement(tvo1);
        }

        return listaAnotaciones;

    }

    private String codUor2CodAccede(String codUOR, String[] params){
        AdaptadorSQLBD oad = null;
        Statement stmt = null;
        String sql;
        ResultSet rs = null;
        Connection con = null;
        String codAccede = "";

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            if (!"".equals(codUOR) && (codUOR != null)) {
                sql = "SELECT UOR_COD_ACCEDE, UOR_COD FROM A_UOR WHERE UOR_COD = " + codUOR + " " +
                        "AND UOR_COD_ACCEDE IS NOT NULL";
            } else {
                return "";
            }    
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            
            if (rs.next()) codAccede = rs.getString(1);
             
             
                        
        } catch (Exception e) {
            e.printStackTrace();
            codAccede = null;
        } finally {
            try {
                if (con != null) {
                    rs.close();
                    stmt.close();
                    oad.devolverConexion(con);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            }

        }    
        return codAccede;
    }
    
    private String codAccede2CodUor(String codAccede, String[] params){
        AdaptadorSQLBD oad = null;
        Statement stmt = null;
        String sql;
        ResultSet rs = null;
        Connection con = null;
        String codUor = "";

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            if (!"".equals(codAccede) && (codAccede != null)) {
                sql = "SELECT UOR_COD FROM A_UOR WHERE UOR_COD_ACCEDE = " + codAccede + " " +
                        "AND UOR_COD IS NOT NULL";
            } else {
                return "";
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);


            if (rs.next()) codUor = rs.getString(1);



        } catch (Exception e) {
            e.printStackTrace();
            codUor = null;
        } finally {
            try {
                if (con != null) {
                    rs.close();
                    stmt.close();
                    oad.devolverConexion(con);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            }

        }
        return codUor;
    }


    /**
     * @param params: datos de la conexión
     * @param codUOR: código de la uor en SGE. En blanco -> Todas las uors
     * @return HahsMap con las uors (key = codAccede, value = codSGE)
     */        
    private HashMap getUORsEnAccede(String[] params, String codUOR) {

        AdaptadorSQLBD oad = null;
        Statement stmt = null;
        String sql;
        ResultSet rs = null;
        HashMap lista = new HashMap();
        Connection con = null;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            if (!"".equals(codUOR) && (codUOR != null)) {
                sql = "SELECT UOR_COD_ACCEDE, UOR_COD FROM A_UOR WHERE UOR_COD = " + codUOR + " " +
                        "AND UOR_COD_ACCEDE IS NOT NULL";
            } else {
                sql = "SELECT UOR_COD_ACCEDE, UOR_COD FROM A_UOR WHERE UOR_COD_ACCEDE IS NOT NULL";
            }

            if (m_Log.isDebugEnabled()) m_Log.debug("ServicioSWPisa : Get Lista Uors --> " + sql);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String codAccede = rs.getString(1);
                String codUor = rs.getString(2);
                lista.put(codAccede, codUor);
            }

            if (m_Log.isDebugEnabled()) m_Log.debug("ServicioSWPisa : Get Uor ");

        } catch (Exception e) {
            e.printStackTrace();
            lista = null;
        } finally {
            try {
                if (con != null) {
                    rs.close();
                    stmt.close();
                    oad.devolverConexion(con);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            }

        }
        return lista;
    }


    private int estadoAccedeToSge(int estAccede) {
        if (estAccede == Integer.parseInt(m_constantes.getString("estadoPendienteAccede"))) return 0;
        else return estAccede;
    }

 public DocumentoValueObject viewDocument(RegistroValueObject registroVO,int codDocumento,String[] params) throws RegistroException
     {
         return null;
     }

    private int estadoSgeToAccede(int estSge) {
        if (estSge == 0) return Integer.parseInt(m_constantes.getString("estadoPendienteAccede"));
        else if (estSge == 1) return Integer.parseInt(m_constantes.getString("estadoAceptadaAccede"));
        else if (estSge == 2) return Integer.parseInt(m_constantes.getString("estadoRechazadaAccede"));
        else return -999;
    }

    private int getCodRolPorDefecto (String codProcedimiento, Connection con) throws TechnicalException, RegistroException {
        int codRol = -1;

        PreparedStatement ps = null;
        String sql;
        ResultSet rs = null;
        try {
            sql = "SELECT ROL_COD FROM E_ROL WHERE ROL_PRO = ? AND ROL_PDE = 1";
            ps = con.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            rs = ps.executeQuery();
            if (rs.next()) codRol = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TechnicalException("ERROR AL RECUPERAR EL ROL POR DEFECTO");
        }finally {
            cerrarPreparedStatement(ps);
            cerrarResultSet(rs);
        }
        return codRol;
    }

    private String[] getCodDescUOR (String uor, String[] params) {
        String[] codDescUOR = new String[2];
        AdaptadorSQLBD oad = null;
        Statement stmt = null;
        String sql;
        ResultSet rs = null;
        Connection con = null;


        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            if (!"".equals(uor) && (uor != null)) {
                sql = "SELECT UOR_COD_VIS, UOR_NOM FROM A_UOR WHERE UOR_COD = " + uor;
            } else {
                return null;
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);


            if (rs.next()) {
                codDescUOR[0] = rs.getString(1);
                codDescUOR[1] = rs.getString(2);
            }



        } catch (Exception e) {
            e.printStackTrace();
            codDescUOR = null;
        } finally {
            try {
                if (con != null) {
                    rs.close();
                    stmt.close();
                    oad.devolverConexion(con);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (m_Log.isErrorEnabled()) m_Log.error(ex.getMessage());
            }

        }
        return codDescUOR;
    }

    @Override
    public Vector obtenerInteresados(RegistroValueObject gVO, String[] params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector getAsientosEntradaRegistro(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params, String fechaDesde, String fechaHasta, String documento, String nombre, String apellido1, String apellido2, String codAsunto, String unidadRegistroAsunto, String tipoRegistroAsunto, String codUorDestino, String ejercicio, String numAnotacion,String codUorAnotacion) throws RegistroException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector getAsientosEntradaRegistroPluginTecnico(UsuarioValueObject uvo, TramitacionValueObject tvo, String[] strings, String string, String string1, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, String string12, String string13, String string14, String string15) throws RegistroException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector getAsientosExpedientesHistoricoPluginTecnico(UsuarioValueObject uvo, TramitacionValueObject tvo, String[] strings, String string, String string1, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, String string12, String string13, String string14, String string15) throws RegistroException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
