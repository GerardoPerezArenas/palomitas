package es.altia.agora.webservice.registro.rec;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.persistence.manual.ExpedientesDAO;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.webservice.registro.AccesoRegistro;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.agora.webservice.registro.rec.dao.RecFacade;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Clase de acceso a las anotaciones externas que se realizan en el REC
 * @author oscar.rodriguez
 */
public class AccesoRegistroRec implements AccesoRegistro{
    private Logger log = Logger.getLogger(AccesoRegistroRec.class);
    
    private final String PREFFIX_REC        = "Rec";
    private final String SUFFIX_URL_JDBC    = "url_jdbc";
    private final String SUFFIX_BBDD_DRIVER = "bbdd_driver";
    private final String SUFFIX_USER_JDBC   = "user_jdbc";
    private final String SUFFIX_PASS_JDBC   = "pass_jdbc";
    private final String SUFFIX_NOMBRE      = ".nombre";
    
    private final String BARRA = "/";
    private final String DOT   = ".";
    private final String TIPO_ANOTACION_ENTRADA = "E";
    private final String ESTADO_ANOTACION       = "0";
    private final String COD_DEPARTAMENTO       = "2";
    
    private String nombreServicio;
    private String prefijoPropiedad;

    private ResourceBundle m_ct         = null;
    private ResourceBundle m_TipoDocs   = null;
    private ResourceBundle m_constantes = null;
    
    private String PREFFIX_UOR_REC           = "uor/REC/";
    private int LONGITUD_MAX_ANOTACION_REC   = 6;
    private int LIMITE_NOMBRE_CORTO = 75;
    private int LIMITE_NOMBRE_LARGO = 83;
        
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
        
    public AccesoRegistroRec(){
        m_ct         = ResourceBundle.getBundle("es.altia.agora.webservice.registro.rec.configuracion.configuracion");
        m_TipoDocs   = ResourceBundle.getBundle("es.altia.agora.webservice.registro.pisa.cliente.constantes.TipoDocumento");
        m_constantes = ResourceBundle.getBundle("es.altia.agora.webservice.registro.pisa.cliente.constantes.constantes");
    }
    
    public Vector getAsientosEntradaRegistro(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                             String fechaDesde, String fechaHasta,String documento,String nombre,String apellido1,String apellido2,String codAsunto,String unidadRegistroAsunto,String tipoRegistroAsunto,String codUorDestino,String ejercicio,String numAnotacion, String codUorAnotacion) throws RegistroException{
        log.debug("getAsientosEntradaRegistro");        
        String origen = m_ct.getString(PREFFIX_REC + DOT + uVO.getOrgCod() + SUFFIX_NOMBRE);
        
        try{
            RecFacade facade = RecFacade.getInstance();
            return facade.getAnotacionesRec(fechaDesde, fechaHasta, origen);
        }
        catch(Exception e){
            throw new RegistroException(e,e.getMessage());
        }                
    }

    
    public Vector getAsientosExpedientesHistorico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion,String codUorAnotacion) throws RegistroException{
        
        log.debug("getAsientosExpedientesHistorico");
        return null;
    }

        
    
    public RegistroValueObject getInfoAsientoConsulta(RegistroValueObject registroVO, String[] params)
            throws RegistroException{
        
        log.debug("getInfoAsientoConsulta");
        String periodo = String.valueOf(registroVO.getAnoReg());
        String numero = String.valueOf(registroVO.getNumReg());
        String tipo = String.valueOf(registroVO.getTipoReg());
        String uor = String.valueOf(registroVO.getUnidadOrgan());
        int organizacion = registroVO.getIdOrganizacion();
        
        RecFacade facade = RecFacade.getInstance();        
        try{        
            facade.getInfoAsientoConsulta(registroVO);
            
        }
        catch(Exception e){
            e.printStackTrace();
            throw new RegistroException(e,e.getMessage());
        }        
        
        return registroVO;        
    }
     

    public void recuperarAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException{             
        log.debug("recuperarAsiento");
    }

    public void cambiaEstadoAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, int estado, String[] params)
            throws RegistroException{    
        try{
            
            // Hay que componer el número de anotación para el REC
            String ejerNum= tramitacionVO.getEjerNum();                              
            
            
            TramitacionDAO.getInstance().cambiaEstadoAsiento(tramitacionVO, estado, params);
        } catch (TramitacionException tramExc) {
            log.error(tramExc.getMessage());
            throw new RegistroException(tramExc, tramExc.getMessage());
        } catch (TechnicalException techExc) {
            log.error(techExc.getMessage());
            throw new RegistroException(techExc, techExc.getMessage());
        } catch (BDException bdExc) {
            log.error(bdExc.getMessage());
            throw new RegistroException(bdExc, bdExc.getMessage());
        }
    }

    public void adjuntarExpedientesDesdeUnidadTramitadora(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException{       
                
          // Comprobamos que llega el numero de expediente.
        if (tramitacionVO.getNumeroExpediente() == null || tramitacionVO.getNumeroExpediente().equals("")) {
            if (log.isDebugEnabled()) log.debug("ERROR: EL NUMERO DE EXPEDIENTE ES NULO.");
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
                if (log.isDebugEnabled()) log.debug("ERROR: EL EXPEDIENTE A RELACIONAR NO EXISTE.");
                throw new RegistroException(new Exception(), "NO EXISTE EL EXPEDIENTE A ADJUNTAR");
            }
            // Relacionamos el asiento con el expediente.
            try {        
                insertarRelExpedienteExterno(con, tramitacionVO, 1, "1");
            } catch (SQLException sqle) {
                if (log.isDebugEnabled()) log.debug("ERROR: NO SE HA PODIDO ASOCIAR EL EXPEDIENTE AL ASIENTO");
                rollBackTransaction(dbAdapter, con);
                throw new RegistroException(
                        new Exception(), "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
            }            

            
            cambiaEstadoAsiento(tramitacionVO, usuarioVO, 1, params);            
            
            /******* SE CAMBIA EL ESTADO DE LA ANOTACIÓN EN EL REC ********/                        
            //ResourceBundle bundle = ResourceBundle.getBundle("es.altia.agora.webservice.registro.rec.configuracion.configuracion");
            String unidadOrganicaREC =m_ct.getString("uor/REC/" + tramitacionVO.getCodUnidadRegistro());

            try{
                               
                String numAnotacion = tramitacionVO.getNumero();
                StringBuffer sb = new StringBuffer();

                for(int i=0;i<(LONGITUD_MAX_ANOTACION_REC-numAnotacion.length());i++){
                    sb.append("0");
                }
                
                sb.append(numAnotacion);                                            
                String numFinal = tramitacionVO.getEjercicio() + unidadOrganicaREC + sb.toString();                
                
                UORsDAO uorDAO = UORsDAO.getInstance();
                // Se recuperan los datos de la uor del SGE 
                UORDTO uorTO = uorDAO.getUORPorCodigoVisible(tramitacionVO.getCodUnidadRegistro(),params);                    
                // Se cambia el estado de la anotación del REC
                RecFacade facade = RecFacade.getInstance();
                facade.cambiarEstadoAsiento(numFinal,1,Integer.parseInt(tramitacionVO.getCodUnidadRegistro()),uorTO.getUor_nom(),tramitacionVO.getNumeroExpediente());
                
                String ejercicio = tramitacionVO.getEjercicio();                
                String numeroAnotacion = rellenar(tramitacionVO.getNumero(),"0",LONGITUD_MAX_ANOTACION_REC-tramitacionVO.getNumero().length());
                
                FichaExpedienteManager fmanager = FichaExpedienteManager.getInstance();

                fmanager.guardarObservaciones(tramitacionVO.getNumeroExpediente(),"RT:" + numeroAnotacion , params);
                
                dbAdapter.finTransaccion(con);
            }
            catch(Exception e){
                dbAdapter.rollBack(con);
                e.printStackTrace();
                log.error(e.getMessage());
            }            

        } catch (BDException bde) {
            log.error("ERROR AL INTENTAR OBTENER LA CONEXION DE LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (TramitacionException te) {
            log.error("ERROR AL INTENTAR ADJUNTAR EL ASIENTO AL EXPEDIENTE: " + te.getMessage());
            te.printStackTrace();
            throw new RegistroException(te, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } catch (TechnicalException te) {
            log.error("ERROR AL INTENTAR ADJUNTAR EL ASIENTO AL EXPEDIENTE: " + te.getMessage());
            te.printStackTrace();
            throw new RegistroException(te, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        } finally {
            devolverConexion(dbAdapter, con);
        }
        log.debug("AccesoRegistroRec. adjuntarExpedientesDesdeUnidadTramitadora end");        
    }

    public ArrayList<AsientoFichaExpedienteVO> cargaListaAsientosExpediente(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException{    
        ArrayList<AsientoFichaExpedienteVO> lista = new ArrayList<AsientoFichaExpedienteVO>();

        if(generalVO!=null){
            ArrayList regExternos = (ArrayList)generalVO.getAtributo("registrosExternos");
           
            for(int i=0;regExternos!=null && i<regExternos.size();i++){
                GeneralValueObject reg = (GeneralValueObject)regExternos.get(i);
                String codUnidadRegistro = (String)reg.getAtributo("codUnidadRegistro");
                String numeroAsiento     = (String)reg.getAtributo("numeroAsiento");
                String ejercicioAsiento  = (String)reg.getAtributo("ejercicioAsiento");

                StringBuffer sb = new StringBuffer();
                for(int j=0;j<(6-numeroAsiento.length());j++){
                    sb.append("0");
                }

                sb.append(numeroAsiento);
                    
                try{

                    log.debug("cargaListaAsientosExpediente init");
                     // Recuperamos los datos que utilizaremos en el metodo.
                    String numeroExpediente = (String)generalVO.getAtributo("numero");
                    log.debug("cargaListaAsientosExpediente numeroExpediente: " + numeroExpediente);

                    int organizacion = usuarioVO.getOrgCod();
                    String origen = "REC";
                    RecFacade facade = RecFacade.getInstance();

                    TramitacionValueObject tramitacion = facade.getAnotacionRec(codUnidadRegistro,sb.toString(),ejercicioAsiento);
                    AsientoFichaExpedienteVO asiento = new AsientoFichaExpedienteVO();
                    asiento.setTipoAsiento("E");
                    asiento.setCodigoDepartamento(2);
                    asiento.setEjercicioAsiento(Integer.parseInt(tramitacion.getEjercicio()));
                    asiento.setNumeroAsiento(Long.parseLong(tramitacion.getEjerNum()));
                    asiento.setFechaAsiento(tramitacion.getFechaAnotacion());
                    asiento.setAsuntoAsiento(convertirMAYUS(tramitacion.getAsunto()));
                    asiento.setCodigoUOR(1);
                    String interesado = tramitacion.getRemitente();
                    log.debug("AccesoRegistroRec - interesado: " + interesado);
                    log.debug("AccesoRegistroRec - getTitular: " + tramitacion.getTitular());


                    asiento.setOrigenAsiento("REC");


                    if("organismo".equals(tramitacion.getTitular())){
                        asiento.setNombreInteresado(interesado);
                        asiento.setApellido1Interesado("");
                        asiento.setApellido2Interesado("");
                        /*
                        entrada.setAtributo("nombre", interesado);
                        entrada.setAtributo("apellido1","");
                        entrada.setAtributo("apellido2",""); */
                    }else{
                        String nombre = tramitacion.getNombre();
                        String apel1 = tramitacion.getApellido1();
                        String apel2 = tramitacion.getApellido2();

                        if(nombre==null) nombre = "";
                        if(apel1==null) apel1 = "";
                        if(apel2==null) apel2 = "";

                        asiento.setNombreInteresado(nombre);
                        asiento.setApellido1Interesado(apel1);
                        asiento.setApellido2Interesado(apel2);
                        /*
                        entrada.setAtributo("nombre", nombre);
                        entrada.setAtributo("apellido1", apel1);
                        entrada.setAtributo("apellido2", apel2); */
                    }
                   
                    lista.add(asiento);
                }
                catch(Exception e){
                    throw new RegistroException(e,"Error en AccesoRegistroREc.cargaListaAsientosExpediente");
                }
                //
            }// for
            return lista;
        }        
        return new ArrayList<AsientoFichaExpedienteVO>();
        
    }   

    public void iniciarExpedienteAsiento(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException{       
        
        log.debug("AccesoRegistroRec iniciarExpedienteAsiento init");
        String tipoAsiento = (String) generalVO.getAtributo("tipoAsiento");
        String ejercicioAsiento = (String) generalVO.getAtributo("ejercicioAsiento");
        String numeroAsiento = (String) generalVO.getAtributo("numeroAsiento");
        String unidadOrganica = (String)generalVO.getAtributo("codUnidadRegistro");
        int organizacion = usuarioVO.getOrgCod();
    
        String unidadOrganicaREC = m_ct.getString("uor/REC/" + unidadOrganica);
                log.debug("iniciarExpedienteAsiento >>> unidaOrganicaREC " + unidadOrganicaREC);
        
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<(LONGITUD_MAX_ANOTACION_REC-numeroAsiento.length());i++){
            sb.append("0");
        }
        sb.append(numeroAsiento);
        String observaciones = "RT:" + ejercicioAsiento + unidadOrganicaREC + sb.toString();
        
        Connection con = null;
        AdaptadorSQLBD oad = null;
        int resInicioTramite = -1;

        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            oad.inicioTransaccion(con);      
            
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

            // No se dispone del codigo de tercero, por lo que vamos a buscar la entrada y
            // recuperarlo.            
            GeneralValueObject[] interesadosGVO = new GeneralValueObject[1];
            RecFacade facade = RecFacade.getInstance();
            RegistroValueObject registro = new RegistroValueObject();
            registro.setAnoReg(Integer.parseInt(ejercicioAsiento));
            registro.setUnidadOrgan(Integer.parseInt(unidadOrganica));
            registro.setNumReg(Long.parseLong(numeroAsiento));


            facade.getInfoAsientoConsulta(registro);
            
            log.debug("AccesoRegistroRec.iniciarExpedienteAsiento nombreInteresado: " +  registro.getNomCompletoInteresado());            
            /*********** DOMICILIO DEL INTERESADO ****************/

            GeneralValueObject oneInteresado = new GeneralValueObject();            
            oneInteresado.setAtributo("tipoDocumento", registro.getTipoDocInteresado());                        
            oneInteresado.setAtributo("documento", convertirMAYUS(registro.getDocumentoInteresado()));            
            log.debug("getTipoDoc() - tipoDoc: " + registro.getTipoDocInteresado());
            
            if("4".equals(registro.getTipoDocInteresado())){ // Estamos en un CIF                    
                oneInteresado.setAtributo("nombre", convertirMAYUS(registro.getNomCompletoInteresado()));
                oneInteresado.setAtributo("apellido1", convertirMAYUS(""));
                oneInteresado.setAtributo("apellido2", convertirMAYUS(""));
            }
            else 
            if("11".equals(registro.getTipoDocInteresado())){ // Si el tipo de documento es el CODIGO MAP
                oneInteresado.setAtributo("nombre", convertirMAYUS(registro.getNomCompletoInteresado()));                
            }    
            else{                    
                log.debug("AccesoRegistroRec.iniciarExpediente nombreCompletoInteresado: " + registro.getNomCompletoInteresado());
                String[] datosNombre = registro.getNomCompletoInteresado().split(" ");
                
                oneInteresado.setAtributo("nombre", convertirMAYUS(datosNombre[0]));
                //oneInteresado.setAtributo("apellido1", convertirMAYUS(interesados[k].getApellido1()));
                //oneInteresado.setAtributo("apellido2", convertirMAYUS(interesados[k].getApellido2()));
                if(datosNombre!=null && datosNombre.length>=2 && datosNombre[1]!=null)
                    oneInteresado.setAtributo("apellido1", convertirMAYUS(datosNombre[1]));
                else
                    oneInteresado.setAtributo("apellido1","");
                
                if(datosNombre!=null && datosNombre.length>=3 && datosNombre[2]!=null)
                    oneInteresado.setAtributo("apellido2", convertirMAYUS(datosNombre[2]));                        
                else
                    oneInteresado.setAtributo("apellido2","");                        
                
                log.debug("AccesoRegistroRec.iniciarExpediente nombreCompletoInteresado nombre-0: " + oneInteresado.getAtributo("nombre"));
                log.debug("AccesoRegistroRec.iniciarExpediente nombreCompletoInteresado apel1-1: " + oneInteresado.getAtributo("apellido1"));
                log.debug("AccesoRegistroRec.iniciarExpediente nombreCompletoInteresado apel2-: " + oneInteresado.getAtributo("apellido2"));
            }
            
            log.debug("Nombre: " + oneInteresado.getAtributo("nombre"));
            oneInteresado.setAtributo("codigoTipoRelacion","1");            
            oneInteresado.setAtributo("telefono",registro.getTlfInteresado());            
            oneInteresado.setAtributo("email", convertirMAYUS(registro.getEmailInteresado()));           
            oneInteresado.setAtributo("domicilio", convertirMAYUS(registro.getDomCompletoInteresado()));            
            oneInteresado.setAtributo("codPostal", registro.getCpInteresado());
            oneInteresado.setAtributo("codUsuario", generalVO.getAtributo("usuario"));
            oneInteresado.setAtributo("codAplicacion", generalVO.getAtributo("codAplicacion"));
            
            String descMunicipio = eliminarTildes(convertirMAYUS(registro.getMunInteresado()));
            String strMunicipio = "";
            String strProvincia = "";

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

            interesadosGVO[0] = oneInteresado;
            
            /************** FIN DOMICILIO *****************/            
            // Buscaremos los diferentes terceros en la BBDD del SGE.
            GeneralValueObject[] codsTercsExpediente = new GeneralValueObject[interesadosGVO.length];
            if (interesadosGVO != null && interesadosGVO.length > 0) {
                for (int k = 0; k < interesadosGVO.length; k++) {
                    GeneralValueObject codsTerceros = buscarTerceroDomicilio(interesadosGVO[k], con);
                    log.debug("AccesoRegistroRec antes llamar a insertarTerceroDomicilio");
                    log.debug("AccesoRegistroRec el nombre interesado es: " + (String)interesadosGVO[k].getAtributo("nombre"));
                    codsTercsExpediente[k] = insertarTerceroDomicilio(codsTerceros, interesadosGVO[k], oad, con);
                }
            }

            generalVO.setAtributo("observaciones",observaciones);
            insertarExpediente(oad, con, generalVO, codsTercsExpediente);
                         
            String unidadOrganicaAccede = codUor2CodAccede(unidadOrganica,params);             
            log.debug("La Unidad Organica en SGE es: ......................"+unidadOrganica);
            log.debug("La Unidad Organica en Accede es: ......................"+unidadOrganicaAccede);
            
            //dato.setAnotacion(datoAnotacion);           
            //fachadaPisa.asignarExpediente(dato);

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
            
            
            log.debug("AccesoRegistroRec.iniciarExpedienteAsiento tramitacionVO creado: " + (String)generalVO.getAtributo("codProcedimiento"));
            // Relacionamos el asiento con el expediente.
            try {
                insertarRelExpedienteExterno(con, tramitacionVO, 1, "1");
                log.debug("******** iniciarExpediente despues insertarRelExpedienteExteno");
                 log.debug("AccesoRegistroRec con.isClosed() : " + con.isClosed());
                    log.debug("AccesoRegistroRec con.getAutoCommit() : " + con.getAutoCommit());
                // Se cambia el estado del asiento en el REC
                try{
                    
                    StringBuffer sb2 = new StringBuffer();
                    for(int k=0;k<(6-numeroAsiento.length());k++){
                        sb2.append("0");
                    }
                    sb2.append(numeroAsiento);                                        
                    // Se recuperan los datos de la uor del SGE 
                    UORsDAO uorDAO = UORsDAO.getInstance();
                    UORDTO uorTO = uorDAO.getUORPorCodigoVisible(unidadOrganica,params);  
                    log.debug(">>>>>>>>>>>>>>> iniciarExpedienteAsiento uorTO.getUor_nom(): " + uorTO.getUor_nom());
                    // Se recupera el código de la unidad orgánica del REC correspondiente a la del SGE para generar
                    // el número de anotación del REc a buscar
                    String numFinal = ejercicioAsiento + m_ct.getString("uor/REC/" + unidadOrganica) + sb2.toString();
                    log.debug("cambia estado asiento de la anotación REC " + numFinal + " a estado 1");
                                                            
                    log.debug("AccesoRegistroRec, Nombre de la uor: " + uorTO.getUor_nom());
                   
                    // Se cambia el estado de la anotación del REC
                    facade.cambiarEstadoAsiento(numFinal,1,Integer.parseInt(unidadOrganica),uorTO.getUor_nom(),numeroExpediente);
                }
                catch(Exception e){
                    log.error("********* AccesoRegistroRec. No se puede cambiarEstado asiento en el REC");
                    con.rollback();
                    //rollBackTransaction(oad, con);
                    throw new RegistroException(
                        new Exception(), "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
                    }
                
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                log.debug("ERROR: NO SE HA PODIDO ASOCIAR EL EXPEDIENTE AL ASIENTO");
                con.rollback();
                //rollBackTransaction(oad, con);
                throw new RegistroException(
                        new Exception(), "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
            }            

            oad.finTransaccion(con);
            if(resInicioTramite > 0)
                TramitesExpedienteDAO.getInstance().ejecutarOperacionesAlIniciarTramiteInicio(generalVO, params);
        } catch (SQLException e) {
            log.error("iniciarExpedienteAsiento SQLException: " + e.getMessage());
            rollBackTransaction(oad, con);
            e.printStackTrace();
        } catch (BDException e) {
            log.error("iniciarExpedienteAsiento BDException: " + e.getMessage());
            rollBackTransaction(oad, con);
            e.printStackTrace();
        } catch (TechnicalException e) {
            log.error("iniciarExpedienteAsiento TechnicalException: " + e.getMessage());
            rollBackTransaction(oad, con);
            e.printStackTrace();
        } catch(Exception e){
            log.error("iniciarExpedienteAsiento Exception: " + e.getMessage());
            rollBackTransaction(oad, con);
            e.printStackTrace();            
        }        
        finally {
            devolverConexion(oad, con);
        }
        
        log.debug("AccesoRegistroRec iniciarExpedienteAsiento end");        
    } 
    
    
    private String convertirMAYUS(String valor) {
        if (("".equals(valor)) || (valor == null)) return valor;
        else return valor.toUpperCase();
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
    
    
    private GeneralValueObject getMunicipioByNombre(Connection con, int codProvincia, String descMunicipio) throws SQLException {

        String sqlQuery = "SELECT MUN_COD, MUN_PRV FROM " + GlobalNames.ESQUEMA_GENERICO + "T_MUN " +
                "WHERE MUN_PAI = 108 AND (MUN_NOM = '" + descMunicipio + "' OR MUN_NOL = '" + descMunicipio + "') ";
        int prvPorDefc = Integer.parseInt(m_constantes.getString("provincia.desconocida"));
        if (codProvincia != prvPorDefc) sqlQuery += "AND MUN_PRV = " + codProvincia;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (log.isDebugEnabled()) log.debug("CONSULTA PARA OBTENER CODIGOS DE MUNICIPIO Y PROVINCIA");
            if (log.isDebugEnabled()) log.debug(sqlQuery);

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

        log.debug("******* AccesoRegistroREC: insertarExpediente init");
        // Ponemos los datos de codigo de Tercero y rol a nulo para que no grabe ningun tercero.
        generalVO.setAtributo("codTercero", "");
        generalVO.setAtributo("codRol", "");
              
        generalVO.setAtributo("codTercero", (String)interesados[0].getAtributo("codTercero"));
        generalVO.setAtributo("codRol", (String)interesados[0].getAtributo("codRol"));
        generalVO.setAtributo("version", (String)interesados[0].getAtributo("numVersion"));
        generalVO.setAtributo("codDomicilio", (String)interesados[0].getAtributo("codDomicilio"));
                
        try {
            // Grabamos los datos generales del expediente.
            int resInsercion = ExpedientesDAO.getInstance().insertarExpediente(oad, con, generalVO);
            if (resInsercion != 1) throw new RegistroException(new SQLException(), "ERROR EN LAS CONSULTAS PARA INSERTAR EXPEDIENTE");

            // Recuperamos la coleccion de roles relacionados con el expediente.
            Vector roles = getRolesProcedimiento(con, generalVO);
            log.debug("insertarExpediente roles: " + roles);
            // Ahora asociamos los interesados de la anotacion como interesados del expediente.
            asociarInteresados(con, roles, interesados, generalVO);
            
        } catch (Exception sqle) {
            sqle.printStackTrace();
            throw new RegistroException(sqle, "ERROR EN LAS CONSULTAS PARA INSERTAR EXPEDIENTE");
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
         
        log.debug("AccesoRegistroRC.insertarTerceroDomicilio() codTercero: " + codTercero);
        log.debug("AccesoRegistroRC.insertarTerceroDomicilio() actualizaHistorico: " + actualizaHistorico);
        
        try {
            // Insertamos el tercero.
            if (codTercero == -1) {
                codTercero = obtenerNuevoCodigoTercero(abd, con);

                if (log.isDebugEnabled()) log.debug("CONSULTA PARA INSERTAR UN TERCERO");
                if (log.isDebugEnabled()) log.debug(sqlInsertTercero);

                ps = con.prepareStatement(sqlInsertTercero);
                int i = 1;
                ps.setInt(i++, codTercero);                
                log.debug("insertarTerceroDomicilio param1: " + codTercero);
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("tipoDocumento")));                
                log.debug("insertarTerceroDomicilio param2: " + (String) gVO.getAtributo("tipoDocumento"));
                ps.setString(i++, (String) gVO.getAtributo("documento"));                
                log.debug("insertarTerceroDomicilio param3: " + (String)gVO.getAtributo("documento"));                
                String nombre = (String) gVO.getAtributo("nombre");                
                log.debug("insertarTerceroDomicilio Tipo param4: " + (String)gVO.getAtributo("nombre"));                
                log.debug(" Tipo longitud nombre: " + ((String)gVO.getAtributo("nombre")).length());                
                
                log.debug("insertarTerceroDomicilio longitud nombre: " + nombre.length());
                
                String apellido1 = (String) gVO.getAtributo("apellido1");                
                String apellido2 = (String) gVO.getAtributo("apellido2");                
                log.debug("insertarTercerxoDomicilio nombre: " + (String)gVO.getAtributo("nombre"));                
                log.debug("insertarTerceroDomicilio apellido1: " + (String)gVO.getAtributo("apellido1"));                
                String nombreCompleto = "";
                if (apellido1 != null && !"".equals(apellido1)) nombreCompleto = apellido1;
                if (apellido2 != null && !"".equals(apellido2)) nombreCompleto += " " + apellido2;
                if (nombre != null && !"".equals(nombre)) {
                    if (nombreCompleto.length() > 0) nombreCompleto += ", " + nombre;
                    else nombreCompleto = nombre;
                }
                if(nombre.length()>LIMITE_NOMBRE_CORTO)
                    nombre = nombre.substring(0,LIMITE_NOMBRE_CORTO);                  
                
                ps.setString(i++, nombre);                 
                log.debug("insertarTercerxoDomicilio param5: " + nombre);                
                ps.setString(i++, apellido1);
                log.debug("insertarTercerxoDomicilio param6: " + apellido1);                
                ps.setString(i++, apellido2);
                log.debug("insertarTercerxoDomicilio param7: " + apellido2);                                                
                ps.setString(i++, nombreCompleto);
                log.debug("insertarTercerxoDomicilio param8: " + nombreCompleto);                
                
                ps.setString(i++, (String) gVO.getAtributo("telefono"));                
                log.debug("insertarTercerxoDomicilio param9: " + (String) gVO.getAtributo("telefono"));                
                ps.setString(i++, (String) gVO.getAtributo("email"));   
                log.debug("insertarTercerxoDomicilio param10: " + (String) gVO.getAtributo("email"));                
                ps.setInt(i++, Integer.parseInt((String) gVO.getAtributo("codUsuario")));                
                log.debug("insertarTercerxoDomicilio param11: " + (String) gVO.getAtributo("codUsuario"));                
                ps.setInt(i, Integer.parseInt((String) gVO.getAtributo("codAplicacion")));
                log.debug("insertarTercerxoDomicilio param12: " + (String) gVO.getAtributo("codAplicacion"));                

                int insertedRows = ps.executeUpdate();
                if (insertedRows != 1) throw new SQLException("ERROR EN LAS CONSULTAS DE SQL");

                ps.close();
                numVersion = 1;
            }

            if (codDomicilio == -1) {
                // Insertamos el domicilio
                if (log.isDebugEnabled()) log.debug("CONSULTA PARA INSERTAR UN DOMICILIO");
                if (log.isDebugEnabled()) log.debug(sqlInsertDomicilio);

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
                if (log.isDebugEnabled()) log.debug("CONSULTA PARA INSERTAR LA RELACION ENTRE TERCERO Y DOMICILIO");
                if (log.isDebugEnabled()) log.debug(sqlInsertTerDom);

                ps = con.prepareStatement(sqlInsertTerDom);
                int i = 1;
                ps.setInt(i++, codDomicilio);
                ps.setInt(i++, codTercero);
                ps.setInt(i, Integer.parseInt((String) gVO.getAtributo("codUsuario")));

                int insertedRows = ps.executeUpdate();
                if (insertedRows != 1) throw new SQLException("ERROR EN LAS CONSULTAS DE SQL");

                ps.close();
            }

            log.debug("sqlInsertHistorico antes de sqlInsertHistorico actualizaHistorico: " + actualizaHistorico);                
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
                log.debug("sqlInsertHistorico nombreCompleto: " + nombreCompleto);                
                if(nombre.length()>LIMITE_NOMBRE_CORTO)
                    nombre = nombre.substring(0,LIMITE_NOMBRE_CORTO);                                  
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
            
            if(codDomicilio>=0){                            
                 // Siempre se marca el domicilio como normalizado                
                Statement st = null;
                try{
                    String sqlInsertTDOM = "INSERT INTO T_DOM(DOM_COD,DOM_NML) VALUES('" + codDomicilio + "','2')";
                    log.debug("sqlInsertTDOM: " + sqlInsertTDOM);
                    st = con.createStatement();
                    int insertedRowsR = st.executeUpdate(sqlInsertTDOM);                                
                    // Se considera el domicilio como normalizado                
                    log.debug("insertedRowsR T_DOM: " + insertedRowsR);                
                    if (insertedRowsR!=1) 
                        throw new SQLException("ERROR EN LAS CONSULTAS DE SQL");
                }
                catch(SQLException e){
                    log.error("AccesoRegistroRec.insertarTerceroDomicilio(): " + e.getMessage());
                }
                finally{
                    st.close();
                }
            }
            
            codigosGVO.setAtributo("codTercero",Integer.toString(codTercero));
            codigosGVO.setAtributo("codDomicilio",Integer.toString(codDomicilio));
            codigosGVO.setAtributo("numVersion",Integer.toString(numVersion));
            codigosGVO.setAtributo("codigoTipoRelacion",gVO.getAtributo("codigoTipoRelacion"));

            return codigosGVO;

        } catch (SQLException sqle) {
            log.error("insertarTerceroDomicilio SQLException: " + sqle.getMessage());
            sqle.printStackTrace();
            throw sqle;
        } 
        catch (Exception sqle) {
            log.error("insertarTerceroDomicilio Exception: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new BDException(sqle.getMessage());
        } 
        
        finally {
            if (ps != null) ps.close();
        }
    }
      
      
     private Vector getRolesProcedimiento(Connection con, GeneralValueObject gVO) throws RegistroException {
        String sqlQuery = "SELECT ROL_COD, ROL_DES, ROL_PDE FROM E_ROL WHERE ROL_MUN = ? AND ROL_PRO = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        log.debug("******* AccesoRegistroREC: getRolesProcedimiento init");
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
        log.debug("============> buscarTerceroDomicilio init <===================");
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
            if (log.isDebugEnabled()) log.debug("CONSULTA PARA BUSCAR UN TERCERO");
            if (log.isDebugEnabled()) log.debug(sqlBuscarTercero);

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
            if (log.isDebugEnabled()) log.debug("CONSULTA PARA BUSCAR UN DOMICILIO");
            if (log.isDebugEnabled()) log.debug(sqlBuscarDomicilio);

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
                if (log.isDebugEnabled())
                    log.debug("CONSULTA PARA COMPROBAR SI EXISTE RELACION ENTRE UN TERCERO Y UN DOMICILIO");
                if (log.isDebugEnabled()) log.debug(sqlExisteRelacion);

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
    
    
    private void rollBackTransaction(AdaptadorSQLBD adapter, Connection con) throws RegistroException {
        try {
            if (con != null) adapter.rollBack(con);
        } catch (BDException bde) {            
            log.error("ERROR AL INTENTAR HACER ROLLBACK DE LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO ADJUNTAR EL EXPEDIENTE POR PROBLEMAS TECNICOS");
        }
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
                log.error(ex.getMessage());
            }

        }    
        return codAccede;
    }
     
     
     private void insertarRelExpedienteExterno(Connection con, TramitacionValueObject tvo, int ori, String tipoOp)
            throws SQLException {

        String sqlInsertAnotacion = "INSERT INTO E_EXREXT (EXREXT_UOR, EXREXT_TIP, EXREXT_EJR, EXREXT_NRE, EXREXT_MUN, " +
                "EXREXT_NUM, EXREXT_ORI, EXREXT_TOP, EXREXT_SER, EXREXT_PRO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        log.debug("AccesoRegistroRec.insertarRelExpedienteExterno sql: " + sqlInsertAnotacion);
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
        log.debug("AccesoRegistroRec.insertarRelExpedienteExterno insertedRows: " + insertedRows);
        if (insertedRows != 1) throw new SQLException("ERROR EN EL VALOR DEVUELTO POR LA CONSULTA A BBDD " +
                "(Valor = " + insertedRows);
    }    
     
    private void devolverConexion(AdaptadorSQLBD adapter, Connection con) throws RegistroException {
        try {
            if (con != null) adapter.devolverConexion(con);
        } catch (BDException bde) {
            log.error("ERROR AL INTENTAR DEVOLVER LA CONEXION A LA BASE DE DATOS: " + bde.getMensaje());
            bde.printStackTrace();
            throw new RegistroException(bde, "NO SE PUDO DEVOLVER LA CONEXION A LA BASE DE DATOS");
        }
    } 
    
     private void asociarInteresados(Connection con, Vector roles, GeneralValueObject[] interesados,
                                    GeneralValueObject infoExpediente) throws RegistroException {

        String sqlInsertInteresado = "INSERT INTO E_EXT (EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_DOT, " +
                "EXT_ROL, EXT_PRO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
                String codTipoRol = (String) interesados[i].getAtributo("codigoTipoRelacion");
                
                log.debug("AccesoRegistroRec.asociarInteresados. codTercero: " + codTercero);
                log.debug("AccesoRegistroRec.asociarInteresados. numVersion: " + numVersion);
                log.debug("AccesoRegistroRec.asociarInteresados. codDomicilio: " + codDomicilio);
                log.debug("AccesoRegistroRec.asociarInteresados. codTipoRol: " + codTipoRol);
                
                String datoABuscar = "rol." + codTipoRol;
                log.debug("AccesoRegistroRec.asociarInteresados. dato a buscar: " + datoABuscar);
                
                String codRolTraducido = m_constantes.getString("rol." + codTipoRol);
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
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new RegistroException(sqle, "ERROR EN LAS CONSULTAS PAR INSERTAR INTERESADOS");
        } finally {            
                cerrarStatement(ps);            
        }
    }


    /*
     * Recupera un determinado documento asociado a una anotación
     * @param registroVO: RegistroValueObject
     * @param codDocumento: Código del documento
     * @param params: Parámetros de acceso a la BBDD */
    public DocumentoValueObject viewDocument(RegistroValueObject registroVO,int codDocumento,String[] params) throws RegistroException
    {
        try{
            RecFacade facade = RecFacade.getInstance();            
            return facade.getDocumentoAnotacion(registroVO, codDocumento);
        }
        catch(Exception e){
            log.error("AccesoRegistroRec Exception: " + e.getMessage());
            throw new RegistroException(e,e.getMessage());
        }
    }
     
    private void cerrarStatement(Statement stmt) throws RegistroException {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException sqle) {
            log.error("ERROR AL INTENTAR CERRAR EL STATEMENT DE LA CONSULTA: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO REALIZAR LA CONSULTA POR PROBLEMAS TECNICOS");
        }
    }

    private void cerrarResultSet(ResultSet rs) throws RegistroException {
        try {
            if (rs != null) rs.close();
        } catch (SQLException sqle) {
            log.error("ERROR AL INTENTAR CERRAR EL RESULT SET DE LA CONSULTA: " + sqle.getMessage());
            sqle.printStackTrace();
            throw new RegistroException(sqle, "NO SE PUDO REALIZAR LA CONSULTA POR PROBLEMAS TECNICOS");
        }
    } 
 
    private String rellenar(String cadena,String relleno,int num)            
    {
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<num;i++){
            sb.append(relleno);
        }
        sb.append(cadena);
        return sb.toString();
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