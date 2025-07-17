package es.altia.agora.webservice.registro.pist;

import es.altia.agora.webservice.registro.AccesoRegistro;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.agora.webservice.registro.pist.cliente.FachadaClientePist;
import es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationInfoVO;
import es.altia.agora.webservice.registro.pist.cliente.datos.SearchAnnotationInfoVO;
import es.altia.agora.webservice.registro.pist.cliente.datos.AnnotationPersonInfoVO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.persistence.UsuarioManager;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.sge.persistence.manual.ExpedientesDAO;
import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.PaisesDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.ProvinciasDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.MunicipiosDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.TiposViasDAO;
import es.altia.agora.business.terceros.mantenimiento.MunicipioVO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import es.altia.util.commons.DateOperations;
import java.util.*;
import java.sql.*;
import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AccesoRegistroPIST implements AccesoRegistro {

    protected static Log m_Log = LogFactory.getLog(AccesoRegistroPIST.class.getName());
    private static final int MAX_LENGHT_VIA_NOC = 25;
    protected static Config conf = ConfigServiceHelper.getConfig("techserver");

    String codSinDocumento = "0";
    String codNIF = "1";
    String codPasaporte = "2";
    String codTarjResidencia = "3";
    String codCIF = "4";

    private String nombreServicio;
    private String prefijoPropiedad;

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

    public Vector getAsientosEntradaRegistro(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                             String fechaDesde, String fechaHasta,String documento,String nombre,String apellido1,String apellido2,String codAsunto,String unidadRegistroAsunto,String tipoRegistroAsunto,String codUorDestino,String ejercicio,String numAnotacion, String codUorAnotacion) throws RegistroException {

        FachadaClientePist fachadaPist = FachadaClientePist.getInstance();
        fachadaPist.setPrefijoPropiedad(prefijoPropiedad);

        String tipoAnotacion = "ENTRADA";
        String estadoAnotacion = "PENDIENTE";
        Calendar calFechaDesde = toCalendar(fechaDesde);
        Calendar calFechaHasta = toCalendar(fechaHasta);

        UsuarioManager manager = UsuarioManager.getInstance();
        int appUsuario = uVO.getAppCod();
        uVO.setAppCod(ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA);
        Vector udsOrganicas = manager.buscaUnidadOrg(uVO);
        uVO.setAppCod(appUsuario);

        HashMap udsOrganicasMap = udsOrganicasToHashMap(udsOrganicas);
        String[] arrayUdsOrganicas = clavesToStringArray(udsOrganicasMap);

        // Se recuperarn los ids de aquellas anotaciones relacionadas en el SGE.
        Collection anotsRelacionadas = getNumerosAnotsEntradaPorServicio(nombreServicio, params);
        HashMap<String, String> mapaAnnots = new HashMap<String, String>();
        for (Object objAnnot: anotsRelacionadas) {
            GeneralValueObject annotGVO = (GeneralValueObject)objAnnot;
            String ejerAnnot =(String)annotGVO.getAtributo("annotationYear");
            String numAnnot = (String)annotGVO.getAtributo("annotationNumber");
            String uorAnnot = (String)annotGVO.getAtributo("annotationGrouping");
            String stateAnnot = (String)annotGVO.getAtributo("annotationState");
            mapaAnnots.put(ejerAnnot + "/" + numAnnot + "/" + uorAnnot, stateAnnot);
        }

        Vector<TramitacionValueObject> returnedAnots = new Vector<TramitacionValueObject>();
        AnnotationInfoVO[] anotaciones = fachadaPist.getAnnotations(calFechaDesde, calFechaHasta, estadoAnotacion, arrayUdsOrganicas, tipoAnotacion);
        for (AnnotationInfoVO anotacion : anotaciones) {

            // Comprobamos si la anotacion recuperada ya ha sido asociada en el SGE (Si esta asociada no esta pendiente).
            String strAnotNumber = Integer.toString(anotacion.getAnnotationNumber());
            String ejerAnnot = strAnotNumber.substring(0, 4);
            String numAnnot = Integer.toString(Integer.parseInt(strAnotNumber.substring(4, 10)));
            String uorAnnot = getCodUndOrganica(anotacion.getGroupingName(), udsOrganicasMap);
            String claveMapa = ejerAnnot + "/" + numAnnot + "/" + uorAnnot;
            if (mapaAnnots.get(claveMapa) != null) continue;

            TramitacionValueObject tramitacionVO = new TramitacionValueObject();
            tramitacionVO.setNumTerceros("1");
            tramitacionVO.setCodDepartamento("2");
            tramitacionVO.setCodUnidadRegistro(uorAnnot);
            m_Log.debug(tramitacionVO.getCodUnidadRegistro());
            tramitacionVO.setTipoRegistro("E");
            tramitacionVO.setEjerNum(ejerAnnot + "/" + numAnnot);
            tramitacionVO.setFechaAnotacion(calendarToString(anotacion.getAnnotationTime()));
            tramitacionVO.setAsunto(eliminarCaracteresNoValidos(anotacion.getAnnotationAbstract()));
            tramitacionVO.setRemitente(eliminarCaracteresNoValidos(anotacion.getFullName().replace('*', ' ')));
            tramitacionVO.setEstado("0");
            tramitacionVO.setOrigen(nombreServicio);
            returnedAnots.add(tramitacionVO);
        }

        return returnedAnots;
    }

    private String getCodUndOrganica(String groupingName, HashMap udsOrganicasMap) {
        for (Iterator itUds = udsOrganicasMap.keySet().iterator(); itUds.hasNext(); ) {
            String strUndOrganica = (String)itUds.next();
            if (groupingName.contains(strUndOrganica)) {
                return (String)udsOrganicasMap.get(strUndOrganica);
            }
        }
        return null;
    }

    private String[] clavesToStringArray(HashMap udsOrganicasMap) {
        String[] claves = new String[udsOrganicasMap.size()];
        int i = 0;
        for (Iterator itUds = udsOrganicasMap.keySet().iterator(); itUds.hasNext(); ) {
            claves[i++] = (String)itUds.next();
        }
        return claves;
    }

    private HashMap udsOrganicasToHashMap(Vector udsOrganicas) {
        HashMap<String, String> udsHashMap = new HashMap<String, String>();
        m_Log.debug("udsOrganicasToHashMap numero: " + udsOrganicas.size());

        //for (int i = 0; i < udsOrganicas.size(); i = i + 2) {
        for (int i = 0; i < udsOrganicas.size(); i = i + 3) {
            String codUndOrganica = (String) udsOrganicas.get(i);
            String nameUndOrganica = (String) udsOrganicas.get(i + 1);
            udsHashMap.put(nameUndOrganica, codUndOrganica);
        }

        return udsHashMap;
    }

    public Vector getAsientosExpedientesHistorico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion,String codUorAnotacion)
            throws RegistroException {

        FachadaClientePist fachadaPist = FachadaClientePist.getInstance();
        fachadaPist.setPrefijoPropiedad(prefijoPropiedad);

        String tipoAnotacion = "ENTRADA";
        String estadoAnotacion = "PENDIENTE";
        Calendar calFechaDesde = toCalendar(fechaDesde);
        Calendar calFechaHasta = toCalendar(fechaHasta);

        UsuarioManager manager = UsuarioManager.getInstance();
        int appUsuario = uVO.getAppCod();
        uVO.setAppCod(ConstantesDatos.APP_REGISTRO_ENTRADA_SALIDA);
        Vector udsOrganicas = manager.buscaUnidadOrg(uVO);
        uVO.setAppCod(appUsuario);

        HashMap udsOrganicasMap = udsOrganicasToHashMap(udsOrganicas);
        String[] arrayUdsOrganicas = clavesToStringArray(udsOrganicasMap);

        // Se recuperarn los ids de aquellas anotaciones relacionadas en el SGE.
        Collection anotsRelacionadas = getNumerosAnotsEntradaPorServicio(nombreServicio, params);
        HashMap<String, String> mapaAnnots = new HashMap<String, String>();
        for (Object objAnnot: anotsRelacionadas) {
            GeneralValueObject annotGVO = (GeneralValueObject)objAnnot;
            String ejerAnnot =(String)annotGVO.getAtributo("annotationYear");
            String numAnnot = (String)annotGVO.getAtributo("annotationNumber");
            String uorAnnot = (String)annotGVO.getAtributo("annotationGrouping");
            String stateAnnot = (String)annotGVO.getAtributo("annotationState");
            mapaAnnots.put(ejerAnnot + "/" + numAnnot + "/" + uorAnnot, stateAnnot);
        }

        Vector<TramitacionValueObject> returnedAnots = new Vector<TramitacionValueObject>();
        AnnotationInfoVO[] anotaciones = fachadaPist.getAnnotations(calFechaDesde, calFechaHasta, estadoAnotacion, arrayUdsOrganicas, tipoAnotacion);
        for (AnnotationInfoVO anotacion : anotaciones) {

            // Comprobamos si la anotacion recuperada ya ha sido asociada en el SGE (Si esta asociada no esta pendiente).
            String strAnotNumber = Integer.toString(anotacion.getAnnotationNumber());
            String ejerAnnot = strAnotNumber.substring(0, 4);
            String numAnnot = Integer.toString(Integer.parseInt(strAnotNumber.substring(4, 10)));
            String uorAnnot = getCodUndOrganica(anotacion.getGroupingName(), udsOrganicasMap);
            String claveMapa = ejerAnnot + "/" + numAnnot + "/" + uorAnnot;
            String stateAnnot = mapaAnnots.get(claveMapa);
            if (stateAnnot == null) continue;

            if (stateAnnot.equals("RECHAZADA")) stateAnnot = "2";
            else stateAnnot = "1";

            TramitacionValueObject tramitacionVO = new TramitacionValueObject();
            tramitacionVO.setNumTerceros("1");
            tramitacionVO.setCodDepartamento("2");
            tramitacionVO.setCodUnidadRegistro(uorAnnot);
            tramitacionVO.setTipoRegistro("E");
            tramitacionVO.setEjerNum(ejerAnnot + "/" + numAnnot);
            tramitacionVO.setFechaAnotacion(calendarToString(anotacion.getAnnotationTime()));
            tramitacionVO.setAsunto(eliminarCaracteresNoValidos(anotacion.getAnnotationAbstract()));
            tramitacionVO.setRemitente(eliminarCaracteresNoValidos(anotacion.getFullName().replace('*', ' ')));
            tramitacionVO.setEstado(stateAnnot);
            tramitacionVO.setOrigen(nombreServicio);
            returnedAnots.add(tramitacionVO);
        }

        return returnedAnots;
    }

    public RegistroValueObject getInfoAsientoConsulta(RegistroValueObject registroVO, String[] params)
            throws RegistroException {

        m_Log.debug("AccesoRegistroPIST --> getInfoAsientoConsulta");
        FachadaClientePist fachadaPist = FachadaClientePist.getInstance();
        fachadaPist.setPrefijoPropiedad(prefijoPropiedad);

        String numAnotacion = Integer.toString(registroVO.getAnoReg());
        String strNumAnotacion = Long.toString(registroVO.getNumReg());
        while (strNumAnotacion.length() < 6) strNumAnotacion = "0" + strNumAnotacion;

        if (strNumAnotacion.length() != 10)
            numAnotacion += strNumAnotacion;
        else
            //numAnotacion += strNumAnotacion;
            numAnotacion  = strNumAnotacion;
        String sgeTipoReg = registroVO.getTipoReg();
        String pistTipoReg;
        if (sgeTipoReg.equals("E")) pistTipoReg = "ENTRADA";
        else pistTipoReg = "SALIDA";

        AnnotationInfoVO annotInfo = fachadaPist.getAnnotationInfo(numAnotacion, pistTipoReg);
        if (annotInfo == null)
            throw new RegistroException(new Exception(),
                    "NO SE HA PODIDO ENCONTRAR LA ANOTACION EN LA LLAMADA AL SERVICIO WEB");


        // Lo primero, recuperar la informacion del tercero asociado a ese asiento.
        SearchAnnotationInfoVO searchInfo = new SearchAnnotationInfoVO(numAnotacion, pistTipoReg);
        AnnotationPersonInfoVO personInfo = fachadaPist.getAnnotationPersonInfo(searchInfo);

        registroVO.setFecEntrada(calendarToString(annotInfo.getAnnotationTime()));
        registroVO.setFecHoraDoc(calendarToString(annotInfo.getDocumentTime()));
        registroVO.setAsunto(eliminarCaracteresNoValidos(annotInfo.getAnnotationAbstract()));
        String numDocInteresado = annotInfo.getIdNumber();
        String ctrlDigit = annotInfo.getControlDigit();
        String personType = annotInfo.getPersonType();
        String vatAcronym = annotInfo.getVatAcronym();
        String tipoDocumento = comprobarTipoDocumento(numDocInteresado, personType, vatAcronym, ctrlDigit);
        if (tipoDocumento.equals(codSinDocumento)) numDocInteresado = "";
        if (tipoDocumento.equals(codNIF))
        {
            numDocInteresado = numDocInteresado.substring(numDocInteresado.length()-8, numDocInteresado.length());

        }
        String nombre = personInfo.getName();
        String apellido1 = personInfo.getFamilyName();
        if (personInfo.getFamNamePart() != null && !"".equals(personInfo.getFamNamePart()))
            apellido1 = personInfo.getFamNamePart() + " " + apellido1;
        String apellido2 = personInfo.getSecondName();
        if (personInfo.getSecNamePart() != null && !"".equals(personInfo.getSecNamePart()))
            apellido2 = personInfo.getSecNamePart() + " " + apellido2;
        if (("".equals(nombre))||(nombre == null)) nombre = eliminarCaracteresNoValidos(annotInfo.getFullName().replace('*', ' '));

        registroVO.setNombreInteresadoExterno(nombre);
        registroVO.setApellido1InteresadoExterno(apellido1);
        registroVO.setApellido2InteresadoExterno(apellido2);
        
           
        registroVO.setTipoDocInteresado(tipoDocumento);
        registroVO.setDocumentoInteresado(numDocInteresado+ctrlDigit);
        registroVO.setNomCompletoInteresado(eliminarCaracteresNoValidos(annotInfo.getFullName().replace('*', ' ')));


        // Si el numero de telefono esta vacio, cogemos el numero de Fax.
        String numTelfFax = generaNumTelFax(annotInfo);
        // El numero de contacto puede venir con espacios en blanco, que para almacenarlo en el SGE vamos a eliminarlos.
        numTelfFax = numTelfFax.replaceAll(" ", "");
        registroVO.setTlfInteresado(numTelfFax);
        registroVO.setEmailInteresado(annotInfo.getEmail());
        registroVO.setDomCompletoInteresado(eliminarCaracteresNoValidos(annotInfo.getAddress()));
        registroVO.setPoblInteresado(eliminarCaracteresNoValidos(annotInfo.getMunName()));
        registroVO.setProvInteresado(annotInfo.getCodProvince());
        registroVO.setMunInteresado(eliminarCaracteresNoValidos(annotInfo.getCodMunicipe()));

        // Ahora habra que comprobar si tiene asociado algún expediente.
        GeneralValueObject datosBusqExp = new GeneralValueObject();
        datosBusqExp.setAtributo("codDepartamento", registroVO.getIdentDepart());
        datosBusqExp.setAtributo("codUnidadOrganica", registroVO.getUnidadOrgan());
        datosBusqExp.setAtributo("tipoAnotacion", registroVO.getTipoReg());
        datosBusqExp.setAtributo("ejercicioAnotacion", registroVO.getAnoReg());
        datosBusqExp.setAtributo("numeroAnotacion", registroVO.getNumReg());
        datosBusqExp.setAtributo("codMunicipio", registroVO.getIdOrganizacion());
        datosBusqExp.setAtributo("origenServicio", nombreServicio);
        GeneralValueObject datosExpRelacionado = getInfoExpRelacionado(datosBusqExp, params);
        if (datosExpRelacionado != null) {
            registroVO.setNumExpediente((String)datosExpRelacionado.getAtributo("numExpediente"));
            registroVO.setCodProcedimiento((String)datosExpRelacionado.getAtributo("codProcedimiento"));
            registroVO.setDescProcedimiento((String)datosExpRelacionado.getAtributo("descProcedimiento"));
        }

        registroVO.setIdServicioOrigen(nombreServicio);

        registroVO.setListaTemasAsignados(new Vector());
        registroVO.setListaDocsAsignados(new Vector());

        return registroVO;
    }

    private GeneralValueObject getInfoExpRelacionado(GeneralValueObject datosGVO, String[] params) throws RegistroException {

        String sqlQuery = "SELECT EXREXT_NUM, EXREXT_PRO, PML_VALOR " +
                "FROM E_EXREXT JOIN E_PML ON (EXREXT_PRO = PML_COD AND EXREXT_MUN = PML_MUN AND PML_LENG = "+
                conf.getString("idiomaDefecto")+ " AND PML_CMP = 'NOM') " +
                "WHERE EXREXT_SER = ? AND EXREXT_UOR = ? AND EXREXT_TIP = ? AND EXREXT_EJR = ? AND EXREXT_NRE ="+datosGVO.getAtributo("numeroAnotacion")+"  AND EXREXT_MUN = ? " +
                "AND EXREXT_NUM <> 'RECHAZADA' AND EXREXT_NUM <> 'ACEPTADA'";

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
 
        try {
            con = dbAdapter.getConnection();

            ps = con.prepareStatement(sqlQuery);
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA OBTENER LOS DATOS DEL EXPEDIENTE RELACIONADO CON LA ANOTACION");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlQuery);
            if (m_Log.isDebugEnabled()) m_Log.debug("-->Parametro 1 - Id Servicio Busqueda: " + datosGVO.getAtributo("origenServicio"));
            if (m_Log.isDebugEnabled()) m_Log.debug("-->Parametro 2 - Unidad Organica: " + datosGVO.getAtributo("codUnidadOrganica"));
            if (m_Log.isDebugEnabled()) m_Log.debug("-->Parametro 3 - Tipo Anotacion: " + datosGVO.getAtributo("tipoAnotacion"));
            if (m_Log.isDebugEnabled()) m_Log.debug("-->Parametro 4 - Ejercicio: " + datosGVO.getAtributo("ejercicioAnotacion"));
            if (m_Log.isDebugEnabled()) m_Log.debug("-->Parametro 5 - Numero Anotacion: " + datosGVO.getAtributo("numeroAnotacion"));
            if (m_Log.isDebugEnabled()) m_Log.debug("-->Parametro 6 - Codigo Municipio: " + datosGVO.getAtributo("codMunicipio"));

            ps.setString(1, (String)datosGVO.getAtributo("origenServicio"));
            ps.setInt(2, (Integer)datosGVO.getAtributo("codUnidadOrganica"));
            ps.setString(3, (String)datosGVO.getAtributo("tipoAnotacion"));
            ps.setInt(4, (Integer)datosGVO.getAtributo("ejercicioAnotacion"));
           
            ps.setInt(5, (Integer)datosGVO.getAtributo("codMunicipio"));

            rs = ps.executeQuery();

            if (rs.next()) {
                GeneralValueObject datosExp = new GeneralValueObject();
                datosExp.setAtributo("numExpediente", rs.getString(1));
                datosExp.setAtributo("codProcedimiento", rs.getString(2));
                datosExp.setAtributo("descProcedimiento", rs.getString(3));
                return datosExp;
            } else return null;

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RegistroException(sqle, "FALLO AL BUSCAR EL EXPEDIENTE RELACIONADO A LA ENTRADA");
        } catch (BDException bde) {
            bde.printStackTrace();
            throw new RegistroException(bde, "FALLO AL BUSCAR EL EXPEDIENTE RELACIONADO A LA ENTRADA");
        } finally {
            cerrarStatement(ps);
            cerrarResultSet(rs);
            devolverConexion(dbAdapter, con);
        }
    }

    public void recuperarAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {

        AdaptadorSQLBD adapterBD = new AdaptadorSQLBD(params);
        Connection con = null;


        try {
            con = adapterBD.getConnection();
            adapterBD.inicioTransaccion(con);

            String sgeTipoReg = tramitacionVO.getTipoRegistro();
            String numAnotacion = tramitacionVO.getEjercicioRegistro();
            String strNumAnotacion = tramitacionVO.getNumero();

            deleteExpAnnotRelation(sgeTipoReg, numAnotacion, strNumAnotacion, con);

            adapterBD.finTransaccion(con);

        } catch (RegistroException swre) {
            rollBackTransaction(adapterBD, con);
            throw swre;
        } catch (BDException bde) {
            rollBackTransaction(adapterBD, con);
            throw new RegistroException(bde, "NO SE HA PODIDO RECUPERAR LA ANOTACION POR PROBLEMAS TECNICOS");
        } finally {
            devolverConexion(adapterBD, con);
        }
    }

    public void cambiaEstadoAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, int estado, String[] params) throws RegistroException {

        m_Log.debug("AccesoRegistroPIST --> cambiaEstadoAsiento");

        int uorAnnot = Integer.parseInt(tramitacionVO.getCodUnidadRegistro());
        String tipoAnnot = tramitacionVO.getTipoRegistro();
        int ejerAnnot = Integer.parseInt(tramitacionVO.getEjercicioRegistro());
        int numAnnot = Integer.parseInt(tramitacionVO.getNumero());
        int codMunicipio = usuarioVO.getOrgCod();

        // Abrimos una conexion y su transaccion correspondiente.
        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;
        try {
            con = dbAdapter.getConnection();
            dbAdapter.inicioTransaccion(con);
            insertarAceptarRechazarExterno(con, uorAnnot, tipoAnnot, ejerAnnot, numAnnot, codMunicipio, estado, 1, "1");

            dbAdapter.finTransaccion(con);

        } catch (BDException bde) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION DE LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            rollBackTransaction(dbAdapter, con);
            throw new RegistroException(bde, "NO SE PUDO CAMBIAR EL ESTADO DE LA ANOTACION POR PROBLEMAS TECNICOS");
        } catch (SQLException te) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR CAMBIAR EL ESTADO DE LA ANOTACION: " + te.getMessage());
            te.printStackTrace();
            rollBackTransaction(dbAdapter, con);
            throw new RegistroException(te, "NO SE PUDO CAMBIAR EL ESTADO DE LA ANOTACION POR PROBLEMAS TECNICOS");
        } finally {
            devolverConexion(dbAdapter, con);
        }
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
            dbAdapter.inicioTransaccion(con);

            // Comprobamos que el expediente existe en la Base de Datos.
            if (tramitacionDAO.localizaExpediente(con, tramitacionVO) <= 0) {
                if (m_Log.isDebugEnabled()) m_Log.debug("ERROR: EL EXPEDIENTE A RELACIONAR NO EXISTE.");
                rollBackTransaction(dbAdapter, con);
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

            dbAdapter.finTransaccion(con);

        } catch (BDException bde) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION DE LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            rollBackTransaction(dbAdapter, con);
            throw new RegistroException(bde, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (TramitacionException te) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR ADJUNTAR EL ASIENTO AL EXPEDIENTE: " + te.getMessage());
            te.printStackTrace();
            rollBackTransaction(dbAdapter, con);
            throw new RegistroException(te, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (TechnicalException te) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR ADJUNTAR EL ASIENTO AL EXPEDIENTE: " + te.getMessage());
            te.printStackTrace();
            rollBackTransaction(dbAdapter, con);
            throw new RegistroException(te, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (RegistroException swre) {
            rollBackTransaction(dbAdapter, con);
            throw swre;
        } finally {
            devolverConexion(dbAdapter, con);
        }
    }

    public ArrayList<AsientoFichaExpedienteVO> cargaListaAsientosExpediente(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {

        String numeroExpediente = (String)generalVO.getAtributo("numero");
        String municipioExpediente = (String)generalVO.getAtributo("codMunicipio");
        Collection annotNumbers = getNumerosAnotaciones(numeroExpediente, municipioExpediente, params);

        if (annotNumbers.size() == 0) return new ArrayList<AsientoFichaExpedienteVO>();

        SearchAnnotationInfoVO[] searchInfoVOs = new SearchAnnotationInfoVO[annotNumbers.size()];
        int i = 0;
        for (Iterator itSearch = annotNumbers.iterator(); itSearch.hasNext(); ) {
            GeneralValueObject annotInfo = (GeneralValueObject)itSearch.next();
            String annotNumber = (String)annotInfo.getAtributo("annotationNumber");
            String annotYear = (String)annotInfo.getAtributo("annotationYear");
            String annotType = (String)annotInfo.getAtributo("annotationType");


            while (annotNumber.length() < 6) annotNumber = "0" + annotNumber;
            annotNumber = annotYear + annotNumber;

            if (annotType.equals("E")) annotType = "ENTRADA";
            else annotType = "SALIDA";

            SearchAnnotationInfoVO searchInfo = new SearchAnnotationInfoVO(annotNumber, annotType);
            searchInfoVOs[i] = searchInfo;
            i++;
        }

        FachadaClientePist fachadaPist = FachadaClientePist.getInstance();
        fachadaPist.setPrefijoPropiedad(prefijoPropiedad);

        ArrayList<AsientoFichaExpedienteVO> returnedAnots = new ArrayList<AsientoFichaExpedienteVO>();
        AnnotationInfoVO[] anotaciones = fachadaPist.getAnnotationsByNumbers(searchInfoVOs);
        for (int anotsIndex = 0; anotsIndex < anotaciones.length; anotsIndex++) {

            AsientoFichaExpedienteVO asiento = new AsientoFichaExpedienteVO();
            asiento.setCodigoDepartamento(2);
            String strAnotNumber = Integer.toString(anotaciones[anotsIndex].getAnnotationNumber());
            String ejercicioAnot = strAnotNumber.substring(0, 4);
            String numeroAnot = strAnotNumber.substring(4, 10);
            asiento.setCodigoUOR(Integer.parseInt(findUORAnnotation(ejercicioAnot, numeroAnot, "E", annotNumbers)));
            asiento.setTipoAsiento("E");
            asiento.setEjercicioAsiento(Integer.parseInt(ejercicioAnot));
            asiento.setNumeroAsiento(Long.parseLong(strAnotNumber));
            asiento.setFechaAsiento(calendarToString(anotaciones[anotsIndex].getAnnotationTime()));
            asiento.setAsuntoAsiento(eliminarCaracteresNoValidos(anotaciones[anotsIndex].getAnnotationAbstract()));
            asiento.setObservaciones(false);
            String strNomCompleto = anotaciones[anotsIndex].getFullName();
            int indexSepAps = strNomCompleto.indexOf("*");
            int indexSepNom = strNomCompleto.indexOf(",");
            if (indexSepAps != -1 && indexSepNom != -1) {
                asiento.setApellido1Interesado(eliminarCaracteresNoValidos(strNomCompleto.substring(0, indexSepAps)));
                asiento.setApellido2Interesado(eliminarCaracteresNoValidos(strNomCompleto.substring(indexSepAps + 1, indexSepNom)));
                asiento.setNombreInteresado(eliminarCaracteresNoValidos(strNomCompleto.substring(indexSepNom + 1, strNomCompleto.length())));
            } else {
                asiento.setNombreInteresado(eliminarCaracteresNoValidos(strNomCompleto));
                asiento.setApellido1Interesado("");
                asiento.setApellido2Interesado("");

            }
            asiento.setMasInteresados(false);
            asiento.setOrigenAsiento("PIST");

            returnedAnots.add(asiento);

        }

        return returnedAnots;
    }

    private String findUORAnnotation(String ejercicio, String numero, String tipo, Collection anotaciones) {
        for (Iterator itAnots = anotaciones.iterator(); itAnots.hasNext();) {
            GeneralValueObject annotInfo = (GeneralValueObject)itAnots.next();
            String annotNumber = (String)annotInfo.getAtributo("annotationNumber");
            while (annotNumber.length() < 6) annotNumber = "0" + annotNumber;
            String annotYear = (String)annotInfo.getAtributo("annotationYear");
            String annotType = (String)annotInfo.getAtributo("annotationType");
            String annotGrouping = (String)annotInfo.getAtributo("annotationGrouping");
            if (annotNumber.equals(numero) && annotYear.equals(ejercicio) && annotType.equals(tipo)) return annotGrouping;
        }
        return "0";
    }

    public void iniciarExpedienteAsiento(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params) throws RegistroException {

        FachadaClientePist fachadaPist = FachadaClientePist.getInstance();
        fachadaPist.setPrefijoPropiedad(prefijoPropiedad);

        String tipoAnotacionSGE = (String)generalVO.getAtributo("tipoAsiento");
        String tipoAnotacionTAO;
        if (tipoAnotacionSGE.equals("E")) tipoAnotacionTAO = "ENTRADA";
        else tipoAnotacionTAO = "SALIDA";

        String ejercicioAnotacion = (String)generalVO.getAtributo("ejercicioAsiento");
        String numeroAnotacion = (String)generalVO.getAtributo("numeroAsiento");
        while (numeroAnotacion.length() < 6) numeroAnotacion = "0" + numeroAnotacion;
        String numeroAnotacionTAO = ejercicioAnotacion + numeroAnotacion;

        // Lo primero, recuperar la informacion del tercero asociado a ese asiento.
        SearchAnnotationInfoVO searchInfo = new SearchAnnotationInfoVO(numeroAnotacionTAO, tipoAnotacionTAO);
        AnnotationPersonInfoVO personInfo = fachadaPist.getAnnotationPersonInfo(searchInfo);

        AdaptadorSQLBD dbAdapter = new AdaptadorSQLBD(params);
        Connection con = null;

        try {
            con = dbAdapter.getConnection();
            dbAdapter.inicioTransaccion(con);

            // En primer lugar recuperamos un numero de expediente.
            TramitacionValueObject tVO = new TramitacionValueObject();
            tVO.setCodProcedimiento((String)generalVO.getAtributo("codProcedimiento"));
            // #303601: comprobamos si el procedimiento acepta que sus expedientes se numeren al ser iniciados a partir del anho del asiento.
            if(((String) generalVO.getAtributo("numeracionExpediente")).equals("anoAsiento")){
                tVO.setEjercicioRegistro(ejercicioAnotacion);
            }
            TramitacionDAO.getInstance().getNuevoExpediente(usuarioVO, tVO, con);
            String numeroExpediente = tVO.getNumero();
            StringTokenizer tokenizer = new StringTokenizer(numeroExpediente, "/");
            generalVO.setAtributo("ejercicio", tokenizer.nextToken());
            generalVO.setAtributo("numero", numeroExpediente);

            ExpedientesDAO expedientesDAO = ExpedientesDAO.getInstance();
            TramitesExpedienteDAO tramitesExpedienteDAO = TramitesExpedienteDAO.getInstance();

            GeneralValueObject personGVO = annotPersonToGVO(personInfo, params);
            personGVO.setAtributo("codUsuario", generalVO.getAtributo("usuario"));
            personGVO.setAtributo("codAplicacion", generalVO.getAtributo("codAplicacion"));
            GeneralValueObject codsTercero = buscarTerceroDomicilio(personGVO, con);
            GeneralValueObject codsTercExp = insertarTerceroDomicilio(codsTercero, personGVO, dbAdapter, con);

            // ACTUALIZAMOS EL GVO PRINCIPAL CON LOS CODIGOS DE TERCERO, VERSION Y DOMICILIO.
            generalVO.setAtributo("codTercero", codsTercExp.getAtributo("codTercero"));
            generalVO.setAtributo("codDomicilio", codsTercExp.getAtributo("codDomicilio"));
            generalVO.setAtributo("version", codsTercExp.getAtributo("numVersion"));

            // Actualizamos en el GVO principal el codigo de ROL.
            String codigoRol = getRolDefectoProc(con, generalVO);
            generalVO.setAtributo("codRol", codigoRol);

            // Creamos el expediente en la aplicación.
            int resInsercion = expedientesDAO.insertarExpediente(dbAdapter, con, generalVO);
            if (resInsercion != 1) throw new SQLException("ERROR EN LAS CONSULTAS PARA INSERTAR EXPEDIENTE");

            // Creamos el tramite de Inicio.
            int resInicioTramite = tramitesExpedienteDAO.iniciarTramiteInicio(dbAdapter, con, generalVO);
            if (resInicioTramite != 1) throw new SQLException("NO SE HA PODIDO INICIAR UN PRIMER TRAMITE PARA EL EXPEDIENTE. NO SE CREA EL EXPEDIENTE");

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
            tramitacionVO.setFechaAnotacion((String)generalVO.getAtributo("fechaAnotacion"));
            
            // Relacionamos el asiento con el expediente.
            try {
                insertarRelExpedienteExterno(con, tramitacionVO, 1, "1");
            } catch (SQLException sqle) {
                if (m_Log.isDebugEnabled()) m_Log.debug("ERROR: NO SE HA PODIDO ASOCIAR EL EXPEDIENTE AL ASIENTO");
                rollBackTransaction(dbAdapter, con);
                throw new RegistroException(
                        new Exception(), "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
            }

            dbAdapter.finTransaccion(con);
            if(resInicioTramite > 0)
                TramitesExpedienteDAO.getInstance().ejecutarOperacionesAlIniciarTramiteInicio(generalVO, params);
        } catch (SQLException sqle) {
            rollBackTransaction(dbAdapter, con);
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (BDException bde) {
            rollBackTransaction(dbAdapter, con);
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (TechnicalException te) {
            rollBackTransaction(dbAdapter, con);
            te.printStackTrace();
            throw new RegistroException(te, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (TramitacionException te) {
            rollBackTransaction(dbAdapter, con);
            te.printStackTrace();
            throw new RegistroException(te, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        }  catch (Exception te) {
            rollBackTransaction(dbAdapter, con);
            te.printStackTrace();
            throw new RegistroException(te, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } finally {
            devolverConexion(dbAdapter, con);
        }
    }

    private String getRolDefectoProc(Connection con, GeneralValueObject gVO) throws SQLException {

        String sqlQuery = "SELECT ROL_COD FROM E_ROL WHERE ROL_MUN = ? AND ROL_PRO = ? AND ROL_PDE = 1";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sqlQuery);
            int i = 1;
            ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
            ps.setString(i, (String) gVO.getAtributo("codProcedimiento"));

            rs = ps.executeQuery();

            if (rs.next()) return rs.getString(1);
            else return null;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        }
    }

    private Collection getNumerosAnotaciones(String numeroExpediente, String municipioExpediente, String[] params)
            throws RegistroException {

        String sqlQuery = "SELECT EXREXT_TIP, EXREXT_EJR, EXREXT_NRE, EXREXT_UOR FROM E_EXREXT WHERE EXREXT_NUM = ?";

        AdaptadorSQLBD adapterBD = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null; 

        try {
            con = adapterBD.getConnection();
            ps = con.prepareStatement(sqlQuery);

            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR LAS ANOTACIONES ASOCIADAS A UN EXPEDIENTE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlQuery);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1 - Numero de Expediente: " + numeroExpediente);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2 - Codigo de Municipio: " + municipioExpediente);

            int i = 1;
            ps.setString(i++, numeroExpediente);            

            rs = ps.executeQuery();

            Collection annotDatas = new ArrayList();
            while (rs.next()) {
                i = 1;
                GeneralValueObject annotData = new GeneralValueObject();
                annotData.setAtributo("annotationType", rs.getString(i++));
                annotData.setAtributo("annotationYear", rs.getString(i++));
                annotData.setAtributo("annotationNumber", rs.getString(i++));
                annotData.setAtributo("annotationGrouping", rs.getString(i));
                annotDatas.add(annotData);
            }

            return annotDatas;

        } catch (BDException bde) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION DE LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO RECUPERAR LAS ANOTACIONES ASOCIADAS AL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR REALIZAR LA CONSULTA CONTRA LA BASE DE DATOS: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO RECUPERAR LAS ANOTACIONES ASOCIADAS AL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } finally {
            cerrarStatement(ps);
            cerrarResultSet(rs);
            devolverConexion(adapterBD, con);
        }

    }

    private void deleteExpAnnotRelation(String annotationType, String annotationYear, String annotationNumber, Connection con)
            throws RegistroException {

        String sqlQuery = "DELETE E_EXREXT WHERE EXREXT_TIP = ? AND EXREXT_EJR = ? AND EXREXT_NRE = ?";

        PreparedStatement ps = null;

        try {

            ps = con.prepareStatement(sqlQuery);

            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA ELIMINAR UNA ANOTACION ASOCIADA A UN EXPEDIENTE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlQuery);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1 - Tipo del Expediente: " + annotationType);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 2 - Año de la Anotación: " + annotationYear);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 3 - Numero de la Anotación: " + annotationNumber);

            int i = 1;
            ps.setString(i++, annotationType);
            ps.setInt(i++, Integer.parseInt(annotationYear));
            ps.setInt(i, Integer.parseInt(annotationNumber));

            int deletedRows = ps.executeUpdate();
            if (deletedRows > 1) throw new RegistroException(new SQLException(), "NO SE PUDO RECUPERAR LAS " +
                    "ANOTACIONES ASOCIADAS AL EXPEDIENTE POR PROBLEMAS TECNICOS");

        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR REALIZAR LA CONSULTA CONTRA LA BASE DE DATOS: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO RECUPERAR LAS ANOTACIONES ASOCIADAS AL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } finally {
            cerrarStatement(ps);
        }
    }

    private Calendar toCalendar(String strFecha) {
        String[] parts = strFecha.split("/");
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(Date.valueOf(parts[2] + "-" + parts[1] + "-" + parts[0]));
        return fecha;
    }

    private String calendarToString(Calendar fecha) {
        String day = Integer.toString(fecha.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(fecha.get(Calendar.MONTH) + 1);
        String year = Integer.toString(fecha.get(Calendar.YEAR));
        String hour = Integer.toString(fecha.get(Calendar.HOUR_OF_DAY));
        String minute = Integer.toString(fecha.get(Calendar.MINUTE));
        while (day.length() < 2) day = "0" + day;
        while (month.length() < 2) month = "0" + month;
        while (hour.length() < 2) hour = "0" + hour;
        while (minute.length() < 2) minute = "0" + minute;
        return day + "/" + month + "/" + year + " " + hour + ":" + minute;
    }

    private String comprobarTipoDocumento(String numDocumento, String tipoPersona, String vatAcronym, String ctrlDigit) {

        if (numDocumento == null || "".equals(numDocumento)) return codSinDocumento;
        if (tipoPersona.equals("J")) return codCIF;
        if (tipoPersona.equals("F")) {
            boolean esDNIValido;
            try {
                esDNIValido = letraNIF(numDocumento, ctrlDigit);
            } catch (Exception e) {
                esDNIValido = false;
            }

            if (esDNIValido && "ES".equals(vatAcronym)) return codNIF;
            else if (numDocumento.startsWith("X")) return codTarjResidencia;
            else return codPasaporte;
        }
        return codSinDocumento;
    }

    private boolean letraNIF(String nif, String letraNif) throws Exception {
        String cadena = "TRWAGMYFPDXBNJZSQVHLCKET";
        int pos = Integer.parseInt(nif) % 23;
        return (String.valueOf(cadena.charAt(pos)).equals(letraNif));
    }

    private GeneralValueObject annotPersonToGVO(AnnotationPersonInfoVO personInfo, String[] params) {

        GeneralValueObject personGVO = new GeneralValueObject();

        String numDocumento = personInfo.getIdNumber();
        String tipoPersona = personInfo.getPersonType();
        String vatAcronym = personInfo.getVatAcronym();
        String controlDigit = personInfo.getCtrlDigit();

        String tipoDocumentoSGE = comprobarTipoDocumento(numDocumento, tipoPersona, vatAcronym, controlDigit);
        String numDocumentoSGE;
        if (tipoDocumentoSGE.equals(codSinDocumento)) numDocumentoSGE = "";
        else numDocumentoSGE = numDocumento;
        if (tipoDocumentoSGE.equals(codNIF))
            numDocumentoSGE = numDocumentoSGE.substring(numDocumentoSGE.length()-8, numDocumentoSGE.length());
        numDocumentoSGE += controlDigit;

        String nombre, apellido1, apellido2;
        if (tipoDocumentoSGE.equals(codCIF)) {
            nombre = personInfo.getCiaName();
            apellido1 = "";
            apellido2 = "";
        } else {
            nombre = personInfo.getName();
            apellido1 = personInfo.getFamilyName();
            if (personInfo.getFamNamePart() != null && !"".equals(personInfo.getFamNamePart()))
                apellido1 = personInfo.getFamNamePart() + " " + apellido1;
            apellido2 = personInfo.getSecondName();
            if (personInfo.getSecNamePart() != null && !"".equals(personInfo.getSecNamePart()))
                apellido2 = personInfo.getSecNamePart() + " " + apellido2;
            if (nombre == null) nombre = personInfo.getCiaName();
        }

        String telefono = generaNumTelFax(personInfo);

        String email = personInfo.getEmail();
        String tipoViaTAO = personInfo.getAddAcronym();
        String tipoViaSGE = "0";
        try {
            GeneralValueObject datosTipoVia = TiposViasDAO.getInstance().getTipoViaByAbreviatura(tipoViaTAO, params);
            tipoViaSGE = (String)datosTipoVia.getAtributo("codTipoVia");
        } catch (Exception e) {
            if (m_Log.isErrorEnabled()) m_Log.error("NO SE HA PODIDO RECUPERAR EL CODIGO DE TIPO DE VIA. SE DEVUELVE EL CODIGO POR DEFECTO");
            e.printStackTrace();
        }

        String direccion = personInfo.getStName();
        String numero = personInfo.getNum1();
        String bloque = personInfo.getBlock();
        String escalera = personInfo.getStair();
        String planta = personInfo.getFloor();
        String puerta = personInfo.getDoor();
        String codigoPostal = personInfo.getZipCode();

        String nombreMunicipio = personInfo.getMunName();
        String nombreProvincia = personInfo.getProvName();
        String nombrePais = personInfo.getCntryName();

        try {
            GeneralValueObject infoPais = PaisesDAO.getInstance().getPaisByDescription(nombrePais, params);
            String codPais = (String) infoPais.getAtributo("codigoPais");
            personGVO.setAtributo("codPais", codPais);


            if (!codPais.equals("108")) {

                m_Log.debug("SE TRATA DE UN DOMICILIO EXTRANJERO. PONEMOS LA CONFIGURACION PARA DOMICILIOS EXTRANJEROS");
                personGVO.setAtributo("codPais", "108");
                personGVO.setAtributo("codProvincia", "66");
                personGVO.setAtributo("codMunicipio", codPais);

            } else {

                m_Log.debug("SE TRATA DE UN DOMICILIO EN ESPAÑA. BUSCAMOS LA INFORMACION DE PROVINCIA Y MUNICIPIO");
                GeneralValueObject infoProvincia =
                        ProvinciasDAO.getInstance().getProvinciaByPaisAndDesc(
                                Integer.parseInt(codPais), nombreProvincia, params);

                String codProvincia = (String) infoProvincia.getAtributo("codigoProvincia");
                personGVO.setAtributo("codProvincia", codProvincia);

                MunicipioVO infoMunicipio = MunicipiosDAO.getInstance().getMunicipioByPaisAndProvAndDesc(
                        Integer.parseInt(codPais), Integer.parseInt(codProvincia), nombreMunicipio, params);

                personGVO.setAtributo("codMunicipio", Integer.toString(infoMunicipio.getCodigoMunicipio()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("NO SE HA PODIDO RECUPERAR ALGUN CODIGO DE PAIS, PROVINCIA O MUNICIPIO. PONEMOS LOS VALORES POR DEFECTO");
            personGVO.setAtributo("codPais", "108");
            personGVO.setAtributo("codProvincia", "99");
            personGVO.setAtributo("codMunicipio", "999");
        }

        personGVO.setAtributo("tipoDocumento", tipoDocumentoSGE);
        personGVO.setAtributo("documento", numDocumentoSGE);
        personGVO.setAtributo("nombre", eliminarCaracteresNoValidos(nombre));
        personGVO.setAtributo("apellido1", eliminarCaracteresNoValidos(apellido1));
        personGVO.setAtributo("apellido2", eliminarCaracteresNoValidos(apellido2));
        personGVO.setAtributo("telefono", telefono);
        personGVO.setAtributo("email", email);
        personGVO.setAtributo("tipoVia", tipoViaSGE);
        personGVO.setAtributo("direccion", eliminarCaracteresNoValidos(direccion));
        personGVO.setAtributo("numero", numero);
        personGVO.setAtributo("bloque", bloque);
        personGVO.setAtributo("escalera", escalera);
        personGVO.setAtributo("planta", planta);
        personGVO.setAtributo("puerta", puerta);
        personGVO.setAtributo("codigoPostal", codigoPostal);

        return personGVO;
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

    private void devolverConexion(AdaptadorSQLBD adapter, Connection con) throws RegistroException {
        try {
            if (con != null) adapter.devolverConexion(con);
        } catch (BDException bde) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR DEVOLVER LA CONEXION A LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO DEVOLVER LA CONEXION A LA BASE DE DATOS");
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

    private void cerrarResultSet(ResultSet rs) throws RegistroException {
        try {
            if (rs != null) rs.close();
        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR CERRAR EL RESULT SET DE LA CONSULTA: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO REALIZAR LA CONSULTA POR PROBLEMAS TECNICOS");
        }
    }

    private GeneralValueObject buscarTerceroDomicilio(GeneralValueObject gVO, Connection con)
            throws SQLException, BDException {

        String tipoDocumento = (String) gVO.getAtributo("tipoDocumento");
        String documento = (String) gVO.getAtributo("documento");
        String nombre = (String) gVO.getAtributo("nombre");
        String apellido1 = (String) gVO.getAtributo("apellido1");
        String apellido2 = (String) gVO.getAtributo("apellido2");
        String tipoVia = (String) gVO.getAtributo("tipoVia");
        String domicilio = (String) gVO.getAtributo("direccion");
        String numero = (String) gVO.getAtributo("numero");
        String bloque = (String) gVO.getAtributo("bloque");
        String escalera = (String) gVO.getAtributo("escalera");
        String planta = (String) gVO.getAtributo("planta");
        String puerta = (String) gVO.getAtributo("puerta");
        String codPostal = (String) gVO.getAtributo("codPostal");
        String codMunicipio = (String) gVO.getAtributo("codMunicipio");
        String codProvincia = (String) gVO.getAtributo("codProvincia");
        String codPais = (String) gVO.getAtributo("codPais");


        String sqlBuscarTercero = "SELECT TER_COD, TER_NVE FROM T_TER WHERE TER_SIT = 'A' AND TER_TID = ? " +
                "AND TER_DOC = ? AND TER_NOM = ? ";
        if (apellido1 != null && !"".equals(apellido1)) sqlBuscarTercero += "AND TER_AP1 = '" + apellido1 + "' ";
        else sqlBuscarTercero += "AND (TER_AP1 IS NULL OR TER_AP1 = '') ";
        if (apellido2 != null && !"".equals(apellido2)) sqlBuscarTercero += "AND TER_AP2 = '" + apellido2 + "' ";
        else sqlBuscarTercero += "AND (TER_AP2 IS NULL OR TER_AP2 = '') ";

        String sqlBuscarVia = "SELECT VIA_COD FROM T_VIA WHERE VIA_PAI = ? AND VIA_PRV = ? AND VIA_MUN = ? " +
                "AND VIA_TVI = ? AND (VIA_NOM = ? OR VIA_NOC = ?) AND VIA_SIT = 'A'";

        String sqlBuscarDomicilio = "SELECT DNN_DOM FROM T_DNN WHERE DNN_SIT = 'A' AND DNN_VIA = ? AND DNN_VPA = ? " +
                "AND DNN_VPR = ? AND DNN_VMU = ? AND DNN_PAI = ? AND DNN_PRV = ? AND DNN_MUN = ? ";
        if (codPostal != null && !"".equals(codPostal)) sqlBuscarDomicilio += "AND DNN_CPO = '" + codPostal + "' ";
        else sqlBuscarDomicilio += "AND (DNN_CPO IS NULL OR DNN_CPO = '') ";
        if (numero != null && !"".equals(numero)) sqlBuscarDomicilio += "AND DNN_NUD = '" + numero + "' ";
        else sqlBuscarDomicilio += "AND (DNN_NUD IS NULL OR DNN_NUD = '') ";
        if (bloque != null && !"".equals(bloque)) sqlBuscarDomicilio += "AND DNN_BLQ = '" + bloque + "' ";
        else sqlBuscarDomicilio += "AND (DNN_BLQ IS NULL OR DNN_BLQ = '') ";
        if (escalera != null && !"".equals(escalera)) sqlBuscarDomicilio += "AND DNN_ESC = '" + escalera + "' ";
        else sqlBuscarDomicilio += "AND (DNN_ESC IS NULL OR DNN_ESC = '') ";
        if (planta != null && !"".equals(planta)) sqlBuscarDomicilio += "AND DNN_PLT = '" + planta + "' ";
        else sqlBuscarDomicilio += "AND (DNN_PLT IS NULL OR DNN_PLT = '') ";
        if (puerta != null && !"".equals(puerta)) sqlBuscarDomicilio += "AND DNN_PTA = '" + puerta + "' ";
        else sqlBuscarDomicilio += "AND (DNN_PTA IS NULL OR DNN_PTA = '') ";

        String sqlExisteRelacion = "SELECT * FROM T_DOT WHERE DOT_DOM = ? AND DOT_TER = ? AND DOT_SIT = 'A'";

        PreparedStatement ps = null;
        ResultSet rs = null;

        GeneralValueObject codigosGVO = new GeneralValueObject();
        codigosGVO.setAtributo("codTercero", "-1");
        codigosGVO.setAtributo("numVersion", "-1");
        codigosGVO.setAtributo("codVia", "-1");
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

            // Buscamos la via.
            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR UNA VIA");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscarVia);

            ps = con.prepareStatement(sqlBuscarVia);
            ps.setInt(1, Integer.parseInt(codPais));
            ps.setInt(2, Integer.parseInt(codProvincia));
            ps.setInt(3, Integer.parseInt(codMunicipio));
            ps.setInt(4, Integer.parseInt(tipoVia));
            ps.setString(5, domicilio);
            ps.setString(6, domicilio);
            rs = ps.executeQuery();

            String codVia = null;
            if (rs.next()) {
                codVia = rs.getString(1);
                codigosGVO.setAtributo("codVia", codVia);
            }

            rs.close();
            ps.close();

            // Buscamos el domicilio;
            String codDomicilio = null;
            if (codVia != null) {
                if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA BUSCAR UN DOMICILIO");
                if (m_Log.isDebugEnabled()) m_Log.debug(sqlBuscarDomicilio);

                ps = con.prepareStatement(sqlBuscarDomicilio);
                ps.setInt(1, Integer.parseInt(codVia));
                ps.setInt(2, Integer.parseInt(codPais));
                ps.setInt(3, Integer.parseInt(codProvincia));
                ps.setInt(4, Integer.parseInt(codMunicipio));
                ps.setInt(5, Integer.parseInt(codPais));
                ps.setInt(6, Integer.parseInt(codProvincia));
                ps.setInt(7, Integer.parseInt(codMunicipio));

                rs = ps.executeQuery();

                if (rs.next()) {
                    codDomicilio = Integer.toString(rs.getInt(1));
                    codigosGVO.setAtributo("codDomicilio", codDomicilio);
                }

                rs.close();
                ps.close();
            }

            if (codTercero != null && codDomicilio != null) {
                if (m_Log.isDebugEnabled())
                    m_Log.debug("CONSULTA PARA COMPROBAR SI EXISTE RELACION ENTRE UN TERCERO Y" +
                            " UN DOMICILIO");
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

    private GeneralValueObject insertarTerceroDomicilio(GeneralValueObject codigosGVO, GeneralValueObject gVO, AdaptadorSQLBD abd, Connection con)
            throws SQLException, BDException {

        int codTercero = Integer.parseInt((String) codigosGVO.getAtributo("codTercero"));
        int numVersion = Integer.parseInt((String) codigosGVO.getAtributo("numVersion"));
        int codDomicilio = Integer.parseInt((String) codigosGVO.getAtributo("codDomicilio"));
        int existeRelacion = Integer.parseInt((String) codigosGVO.getAtributo("existeRelacion"));
        int codVia = Integer.parseInt((String) codigosGVO.getAtributo("codVia"));

        String sqlInsertTercero = "INSERT INTO T_TER (TER_COD, TER_TID, TER_DOC, TER_NOM, TER_AP1, TER_PA1, " +
                "TER_AP2, TER_PA2, TER_NOC, TER_NML, TER_TLF, TER_DCE, TER_SIT, TER_NVE, TER_FAL, TER_UAL, " +
                "TER_APL, TER_FBJ, TER_UBJ) " +
                "VALUES (?, ?, ?, ?, ?, NULL, ?, NULL, ?, 0, ?, ?, 'A', 1," + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, ?, NULL, NULL)";

        String sqlInsertVia = "INSERT INTO T_VIA (VIA_PAI, VIA_PRV, VIA_MUN, VIA_COD, VIA_TVI, VIA_CIN, " +
                "VIA_NOM, VIA_NOC, VIA_SIT, VIA_FAL, VIA_UAL, VIA_FBJ, VIA_UBJ, VIA_FIV) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'A', " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, NULL, NULL, " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ")";

        String sqlInsertDomicilio = "INSERT INTO T_DNN (DNN_DOM, DNN_TVI, DNN_PAI, DNN_PRV, DNN_MUN, DNN_VPA, " +
                "DNN_VPR, DNN_VMU, DNN_VIA, DNN_SPA, DNN_SPR, DNN_SMU, DNN_ESI, DNN_NUD, DNN_LED, DNN_NUH, " +
                "DNN_LEH, DNN_BLQ, DNN_POR, DNN_ESC, DNN_PLT, DNN_PTA, DNN_DMC, DNN_CPO, DNN_SIT, DNN_FAL, " +
                "DNN_UAL, DNN_FBJ, DNN_UBJ, DNN_RCA) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, NULL, NULL, NULL, ?, NULL, NULL, NULL, " +
                "?, NULL, ?, ?, ?, NULL, ?, 'A', " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, NULL, NULL, NULL)";

        String sqlInsertHistorico = "INSERT INTO T_HTE (HTE_TER, HTE_NVR, HTE_DOT, HTE_TID, HTE_DOC, HTE_NOM, " +
                "HTE_AP1, HTE_PA1, HTE_AP2, HTE_PA2, HTE_NOC, HTE_NML, HTE_TLF, HTE_DCE, HTE_FOP, HTE_USU, HTE_APL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?, NULL, ?, 0, ?, ?, " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, ?)";

        String sqlInsertTerDom = "INSERT INTO T_DOT (DOT_DOM, DOT_TER, DOT_TOC, DOT_SIT, DOT_FEC, DOT_USU, DOT_DPA) " +
                "VALUES (?, ?, NULL, 'A', " + abd.funcionFecha(AdaptadorSQLBD.FUNCIONFECHA_SYSDATE, null) + ", ?, 0)";

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
                if (nombre == null) nombre = "";
                if (apellido1 == null) apellido1 = "";
                if (apellido2 == null) apellido2 = "";

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

                if (codVia == -1) {
                    // Insertamos la via.
                    if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR LA VIA");
                    if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertVia);

                    codVia = obtenerNuevoCodigoVia(abd, con);
                    ps = con.prepareStatement(sqlInsertVia);

                    int i = 1;
                    ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codPais")));
                    ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codProvincia")));
                    ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
                    ps.setInt(i++, codVia);
                    ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("tipoVia")));
                    ps.setInt(i++, codVia);
                    ps.setString(i++, (String) gVO.getAtributo("direccion"));
                    String nomCortoVia = (String) gVO.getAtributo("direccion");
                    if (nomCortoVia.length() > MAX_LENGHT_VIA_NOC) nomCortoVia = nomCortoVia.substring(0, MAX_LENGHT_VIA_NOC);
                    ps.setString(i++, nomCortoVia);
                    ps.setInt(i, Integer.parseInt((String) gVO.getAtributo("codUsuario")));

                    int insertedRows = ps.executeUpdate();
                    if (insertedRows != 1) throw new SQLException("ERROR EN LAS CONSULTAS DE SQL");

                    ps.close();
                }

                // Insertamos el domicilio
                if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA INSERTAR UN DOMICILIO");
                if (m_Log.isDebugEnabled()) m_Log.debug(sqlInsertDomicilio);

                codDomicilio = obtenerNuevoCodigoDomicilio(abd, con);
                ps = con.prepareStatement(sqlInsertDomicilio);
                int i = 1;
                ps.setInt(i++, codDomicilio);
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("tipoVia")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codPais")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codProvincia")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codPais")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codProvincia")));
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codMunicipio")));
                ps.setInt(i++, codVia);
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("numero")));
                ps.setString(i++, (String) gVO.getAtributo("bloque"));
                ps.setString(i++, (String) gVO.getAtributo("escalera"));
                ps.setString(i++, (String) gVO.getAtributo("planta"));
                ps.setString(i++, (String) gVO.getAtributo("puerta"));
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

    private int obtenerNuevoCodigoVia(AdaptadorSQLBD abd, Connection con) throws SQLException {

        String sqlQuery = "SELECT " + abd.funcionMatematica(
                AdaptadorSQLBD.FUNCIONMATEMATICA_MAX, new String[]{"VIA_COD"}) + " AS MAXIMO FROM T_VIA";
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

    private String eliminarCaracteresNoValidos(String cadena) {
        if (cadena != null) {
            return cadena.replaceAll("\"", "'");
        } else return null;
    }

    private void insertarRelExpedienteExterno(Connection con, TramitacionValueObject tvo, int ori, String tipoOp)
            throws SQLException {


        String sqlInsertAnotacion = "INSERT INTO E_EXREXT (EXREXT_UOR, EXREXT_TIP, EXREXT_EJR, EXREXT_NRE, EXREXT_MUN, " +
                "EXREXT_NUM, EXREXT_ORI, EXREXT_TOP, EXREXT_SER, EXREXT_PRO,EXREXT_FECALTA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
        ps.setString(i++, tvo.getCodProcedimiento());
        if(!"".equals(tvo.getFechaAnotacion())){
            ps.setTimestamp(i++,DateOperations.toTimestamp(tvo.getFechaAnotacion(),"dd/MM/yyyy HH:mm"));
        }
        else
            ps.setNull(i++,java.sql.Types.TIMESTAMP);
        
        int insertedRows = ps.executeUpdate();
        if (insertedRows != 1) throw new SQLException("ERROR EN EL VALOR DEVUELTO POR LA CONSULTA A BBDD " +
                "(Valor = " + insertedRows);

    }

    private void insertarAceptarRechazarExterno(Connection con, int codigoUor, String tipoAnnot, int ejerAnnot,
                                                int numAnnot, int codMunicipio, int estado, int ori, String tipoOp)
            throws SQLException {

        String sqlInsertAnotacion = "INSERT INTO E_EXREXT (EXREXT_UOR, EXREXT_TIP, EXREXT_EJR, EXREXT_NRE, EXREXT_MUN, " +
                "EXREXT_NUM, EXREXT_ORI, EXREXT_TOP, EXREXT_SER, EXREXT_PRO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps;

        String strEstado;
        if (estado == 1) strEstado = "ACEPTADA";
        else strEstado = "RECHAZADA";

        ps = con.prepareStatement(sqlInsertAnotacion);
        int i = 1;
        ps.setInt(i++, codigoUor);
        ps.setString(i++, tipoAnnot);
        ps.setInt(i++, ejerAnnot);
        ps.setInt(i++, numAnnot);
        ps.setInt(i++, codMunicipio);
        ps.setString(i++, strEstado);
        ps.setInt(i++, ori);
        ps.setString(i++, tipoOp);
        ps.setString(i++, nombreServicio);
        ps.setString(i, "-");

        int insertedRows = ps.executeUpdate();

        if (insertedRows != 1) throw new SQLException("ERROR EN EL VALOR DEVUELTO POR LA CONSULTA A BBDD " +
                "(Valor = " + insertedRows);

    }

    private Collection getNumerosAnotsEntradaPorServicio(String idServicio, String[] params)
            throws RegistroException {

        String sqlQuery = "SELECT EXREXT_EJR, EXREXT_NRE, EXREXT_UOR, EXREXT_NUM FROM E_EXREXT WHERE EXREXT_SER = ? AND EXREXT_TIP = 'E'";

        AdaptadorSQLBD adapterBD = new AdaptadorSQLBD(params);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = adapterBD.getConnection();
            ps = con.prepareStatement(sqlQuery);

            if (m_Log.isDebugEnabled()) m_Log.debug("CONSULTA PARA RECUPERAR LAS ANOTACIONES ASOCIADAS A UN EXPEDIENTE");
            if (m_Log.isDebugEnabled()) m_Log.debug(sqlQuery);
            if (m_Log.isDebugEnabled()) m_Log.debug("PARAMETRO 1 - Identificador del Servicio: " + idServicio);

            int i = 1;
            ps.setString(i, idServicio);

            rs = ps.executeQuery();

            Collection<GeneralValueObject> annotDatas = new ArrayList<GeneralValueObject>();
            while (rs.next()) {
                i = 1;
                GeneralValueObject annotData = new GeneralValueObject();
                annotData.setAtributo("annotationYear", rs.getString(i++));
                annotData.setAtributo("annotationNumber", rs.getString(i++));
                annotData.setAtributo("annotationGrouping", rs.getString(i++));
                annotData.setAtributo("annotationState", rs.getString(i));
                annotDatas.add(annotData);
            }

            return annotDatas;

        } catch (BDException bde) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR OBTENER LA CONEXION DE LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO RECUPERAR LAS ANOTACIONES CON RELACION EN EL SGE POR PROBLEMAS TECNICOS");
        } catch (SQLException sqle) {
            if (m_Log.isErrorEnabled()) m_Log.error("ERROR AL INTENTAR REALIZAR LA CONSULTA CONTRA LA BASE DE DATOS: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO RECUPERAR LAS ANOTACIONES CON RELACION EN EL SGE POR PROBLEMAS TECNICOS");
        } finally {
            cerrarStatement(ps);
            cerrarResultSet(rs);
            devolverConexion(adapterBD, con);
        }

    }

    public DocumentoValueObject viewDocument(RegistroValueObject registroVO,int codDocumento,String[] params) throws RegistroException{
         return null;
    }

    private String generaNumTelFax(AnnotationInfoVO annotInfo) {
        String numTelfFax = "";
        boolean conTelefono = !(annotInfo.getTelephone() == null || "".equals(annotInfo.getTelephone()));
        boolean conFax = !(annotInfo.getFax() == null || "".equals(annotInfo.getFax()));
        if (conTelefono) {
            if (conFax) {
                numTelfFax = annotInfo.getTelephone()+"/"+annotInfo.getFax();
            }else {
                numTelfFax = annotInfo.getTelephone();
            }
        }else {
            if (conFax){
                numTelfFax = annotInfo.getFax();
            } else {
                numTelfFax = "";
            }
        }
        return numTelfFax;
    }
    private String generaNumTelFax(AnnotationPersonInfoVO annotPersonInfo) {
        String numTelfFax = "";
        boolean conTelefono = !(annotPersonInfo.getTelephone() == null || "".equals(annotPersonInfo.getTelephone()));
        boolean conFax = !(annotPersonInfo.getFax() == null || "".equals(annotPersonInfo.getFax()));
        if (conTelefono) {
            if (conFax) {
                numTelfFax = annotPersonInfo.getTelephone()+"/"+annotPersonInfo.getFax();
            }else {
                numTelfFax = annotPersonInfo.getTelephone();
            }
        }else {
            if (conFax){
                numTelfFax = annotPersonInfo.getFax();
            } else {
                numTelfFax = "";
            }
        }
        return numTelfFax;
    }

    @Override
    public Vector obtenerInteresados(RegistroValueObject gVO, String[] params) {
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
