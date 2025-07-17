package es.altia.agora.webservice.registro.sge;

import es.altia.agora.webservice.registro.AccesoRegistro;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.registro.DocumentoValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.AsientoFichaExpedienteVO;
import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.interfaces.user.web.util.FormateadorTercero;
import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.flexia.registro.digitalizacion.lanbide.persistence.DigitalizacionDocumentosLanbideManager;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.log4j.Logger;


public class AccesoRegistroSGE implements AccesoRegistro {

    private String nombreServicio;
    private String prefijoPropiedad;
    private Logger log = Logger.getLogger(AccesoRegistroSGE.class);

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
                                             String fechaDesde, String fechaHasta,String documento,String nombre,String apellido1,String apellido2,String codAsunto,String unidadRegistroAsunto,String tipoRegistroAsunto,String codUorDestino,String ejercicio,String numAnotacion, String codUorAnotacion)
            throws RegistroException { 

        try {
            return TramitacionDAO.getInstance().getAsientosEntradaRegistro(uVO, tVO, params, fechaDesde, fechaHasta, nombreServicio,documento,nombre,apellido1,apellido2,codAsunto,unidadRegistroAsunto,tipoRegistroAsunto,codUorDestino,ejercicio,numAnotacion,codUorAnotacion);
        } catch (TramitacionException tramExc) {
            throw new RegistroException(tramExc, tramExc.getMessage());
        } catch (TechnicalException techExc) {
            throw new RegistroException(techExc, techExc.getMessage());
        }
    }

    public Vector getAsientosExpedientesHistorico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
                                                  String fechaDesde, String fechaHasta,String documento,String nombre,String primerApellido,String segundoApellido,String codAsuntoSeleccionado,String unidadRegistroAsuntoSeleccionado,String tipoRegistroAsuntoSeleccionado,String codUorInterno, String ejercicio, String numAnotacion,String codUorAnotacion) throws RegistroException {

        try {
            return TramitacionDAO.getInstance().getAsientosExpedientesHistorico(uVO, tVO, nombreServicio, params, fechaDesde, fechaHasta,documento,nombre,primerApellido,segundoApellido,codAsuntoSeleccionado,unidadRegistroAsuntoSeleccionado,tipoRegistroAsuntoSeleccionado,codUorInterno, ejercicio, numAnotacion,codUorAnotacion);
        } catch (TramitacionException tramExc) {
            throw new RegistroException(tramExc, tramExc.getMessage());
        } catch (TechnicalException techExc) {
            throw new RegistroException(techExc, techExc.getMessage());
        }
    }

    public RegistroValueObject getInfoAsientoConsulta(RegistroValueObject registroVO, String[] params)
            throws RegistroException {
        
        try {
            return getInfoAsientoFromSGE(registroVO, params);
        } catch (AnotacionRegistroException are) {
           throw new RegistroException(are, are.getMessage());
        }
    }

    public void recuperarAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {

        try {
            tramitacionVO.setIdUsuario(usuarioVO.getIdUsuario());
            TramitacionDAO.getInstance().recuperarAsiento(tramitacionVO, params);
        } catch (TramitacionException tramExc) {
            throw new RegistroException(tramExc, tramExc.getMessage());
        } catch (TechnicalException techExc) {
            throw new RegistroException(techExc, techExc.getMessage());
        } catch (BDException bdExc) {
            throw new RegistroException(bdExc, bdExc.getMessage());
        }
    }

    public void cambiaEstadoAsiento(TramitacionValueObject tramitacionVO, UsuarioValueObject usuarioVO, int estado,
                                    String[] params) throws RegistroException {
        try {
            tramitacionVO.setIdUsuario(usuarioVO.getIdUsuario());
            TramitacionDAO.getInstance().cambiaEstadoAsiento(tramitacionVO, estado, params);
        } catch (TramitacionException tramExc) {
            throw new RegistroException(tramExc, tramExc.getMessage());
        } catch (TechnicalException techExc) {
            throw new RegistroException(techExc, techExc.getMessage());
        } catch (BDException bdExc) {
            throw new RegistroException(bdExc, bdExc.getMessage());
        }
    }

    public void adjuntarExpedientesDesdeUnidadTramitadora(TramitacionValueObject tramitacionVO,
                                                          UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {

        try {
            tramitacionVO.setIdUsuario(usuarioVO.getIdUsuario());
            TramitacionDAO.getInstance().adjuntarExpedientesDesdeUnidadTramitadora(tramitacionVO, params);
        } catch (TramitacionException tramExc) {
            throw new RegistroException(tramExc, tramExc.getMessage());
        } catch (TechnicalException techExc) {
            throw new RegistroException(techExc, techExc.getMessage());
        } catch (BDException bdExc) {
            throw new RegistroException(bdExc, bdExc.getMessage());
        }

    }

    public ArrayList<AsientoFichaExpedienteVO> cargaListaAsientosExpediente(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, 
                                               String[] params) throws RegistroException {

        try {
            return FichaExpedienteDAO.getInstance().cargaListaAsientosExpediente(generalVO, nombreServicio, params);
        } catch (TechnicalException techExc) {
            throw new RegistroException(techExc, techExc.getMessage());
        }
    }

    public void iniciarExpedienteAsiento(GeneralValueObject generalVO, UsuarioValueObject usuarioVO, String[] params)
            throws RegistroException {
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = null;
        Integer resultado = new Integer(0);
        try {
            con = oad.getConnection();
            oad.inicioTransaccion(con);
            TramitacionValueObject tVO = new TramitacionValueObject();
            tVO.setCodProcedimiento((String)generalVO.getAtributo("codProcedimiento"));
            // #303601: comprobamos si el procedimiento acepta que sus expedientes se numeren al ser iniciados a partir del anho del asiento.
            if(((String) generalVO.getAtributo("numeracionExpediente")).equals("anoAsiento")){
                tVO.setEjercicioRegistro((String) generalVO.getAtributo("ejercicioAsiento"));
            } 
            TramitacionDAO.getInstance().getNuevoExpediente(usuarioVO, tVO, con);
            String numeroExpediente = tVO.getNumero();
            StringTokenizer tokenizer = new StringTokenizer(numeroExpediente, "/");
            generalVO.setAtributo("ejercicio", tokenizer.nextToken());
            generalVO.setAtributo("numero", numeroExpediente);
            resultado = FichaExpedienteDAO.getInstance().iniciarExpedienteAsiento(oad, generalVO, con, params);
            
            // Comprobamos si el servicio de digitalizacion esta activo
            if(((String) generalVO.getAtributo("servicioDigitalizacionActivo")).equalsIgnoreCase("si")){
                // Digitalizacion-Lanbide :: retramitar documentos al iniciar expediente 
                DigitalizacionDocumentosLanbideManager digitManager = DigitalizacionDocumentosLanbideManager.getInstance();
                digitManager.retramitarDocumentoInicioExpediente(generalVO, params); 
            }
            
            commitTransaction(oad, con);
            if(resultado > 0)
                TramitesExpedienteDAO.getInstance().ejecutarOperacionesAlIniciarTramiteInicio(generalVO, params);
        } catch (TechnicalException techExc) {
            rollBackTransaction(oad, con);
            throw new RegistroException(techExc, techExc.getMessage());
        } catch (TramitacionException techExc) {
            rollBackTransaction(oad, con);
            throw new RegistroException(techExc, techExc.getMessage());
        } catch (BDException techExc) {
            rollBackTransaction(oad, con);
            throw new RegistroException(techExc, techExc.getMessage());
        } catch(Lan6Excepcion le){
           rollBackTransaction(oad, con);
           throw new RegistroException(le, le.getMessage());
        } catch (Exception techExc) {
            rollBackTransaction(oad, con);
            throw new RegistroException(techExc, techExc.getMessage());
        } finally {
            devolverConexion(oad, con);
        }
    }
    
    private RegistroValueObject getInfoAsientoFromSGE(RegistroValueObject registroVO, String[] params)
            throws AnotacionRegistroException {

        registroVO = AnotacionRegistroManager.getInstance().getByPrimaryKey(registroVO,params);

        int domicInter = registroVO.getDomicInter();
        int codInter = registroVO.getCodInter();
        int numModInfInt = registroVO.getNumModInfInt();

        TercerosValueObject terceroVO = new TercerosValueObject();
        terceroVO.setIdDomicilio(String.valueOf(domicInter));
        terceroVO.setIdentificador(String.valueOf(codInter));
        terceroVO.setVersion(String.valueOf(numModInfInt));

        Vector tercerosEncontrados = TercerosManager.getInstance().getByHistorico(terceroVO, params);

        if(tercerosEncontrados.size() > 0 ) {
            terceroVO = (TercerosValueObject) tercerosEncontrados.firstElement();
            // Consigo los datos del tercero
            registroVO.setTipoDocInteresado(terceroVO.getTipoDocumento());
            registroVO.setDocumentoInteresado(terceroVO.getDocumento());
            registroVO.setTipoRegOrigen("SGE");
            registroVO.setCodTerceroExterno(null);
            //formato del interesado
            String nombre = FormateadorTercero.getDescTercero(terceroVO.getNombre(), terceroVO.getApellido1(), terceroVO.getApellido2(), false);
            registroVO.setNomCompletoInteresado(nombre);
            registroVO.setTlfInteresado(terceroVO.getTelefono());
            registroVO.setEmailInteresado(terceroVO.getEmail());
            Vector domicilios = terceroVO.getDomicilios();
            if (domicilios != null) {
                if (domicilios.size() > 0 ) {
                    DomicilioSimpleValueObject domic = (DomicilioSimpleValueObject) domicilios.firstElement();
                    registroVO.setProvInteresado(domic.getProvincia());
                    registroVO.setMunInteresado(domic.getMunicipio());
                    String descTVia = domic.getTipoVia();
                    String numDesde = domic.getNumDesde();
                    String letraDesde = domic.getLetraDesde();
                    String numHasta = domic.getNumHasta();
                    String letraHasta = domic.getLetraHasta();
                    String bloque = domic.getBloque();
                    String portal = domic.getPortal();
                    String escal = domic.getEscalera();
                    String planta = domic.getPlanta();
                    String puerta = domic.getPuerta();
                    String dom = domic.getDomicilio();
                    String domicilio = "";
                    domicilio = (!descTVia.equals("")) ? domicilio + descTVia + " " : domicilio;
                    domicilio = (!dom.equals("")) ? domicilio + " " + dom : domicilio;
                    domicilio = (!numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
                    domicilio = (!letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
                    domicilio = (!numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
                    domicilio = (!letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
                    domicilio = (!bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
                    domicilio = (!portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
                    domicilio = (!escal.equals("")) ? domicilio + " Esc. " + escal : domicilio;
                    domicilio = (!planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
                    domicilio = (!puerta.equals("")) ? domicilio + puerta : domicilio;
                    registroVO.setDomCompletoInteresado(domicilio);
                    registroVO.setPoblInteresado(domic.getBarriada());
                    registroVO.setCpInteresado(domic.getCodigoPostal());
                }
            }

        }
        return registroVO;
    }

    public DocumentoValueObject viewDocument(RegistroValueObject registroVO,int codDocumento, String[] params) throws RegistroException {        
        DocumentoValueObject doc = null;
        try{
            AnotacionRegistroManager manager = AnotacionRegistroManager.getInstance();
            doc = manager.getDocumento(registroVO, params);
        }
        catch(Exception e){
            log.error("AccesoRegistroSGE Exception: " + e.getMessage());
            throw new RegistroException(e,e.getMessage());
        }
        return doc;
    }
    
    private void commitTransaction(AdaptadorSQLBD abd, Connection con) throws RegistroException {
        
        try {
            if (con != null) abd.finTransaccion(con);
        } catch (BDException bde) {
            throw new RegistroException(bde, bde.getMensaje());
        }
    }
    
    private void rollBackTransaction(AdaptadorSQLBD abd, Connection con) throws RegistroException {
        try {
            if (con != null) abd.rollBack(con);
        } catch (BDException bde) {
            throw new RegistroException(bde, bde.getMensaje());
    }
}
    
    private void devolverConexion(AdaptadorSQLBD abd, Connection con) throws RegistroException {
        try {
            if (con != null) abd.devolverConexion(con);
        } catch (BDException bde) {
            throw new RegistroException(bde, bde.getMensaje());
        }
    }

    @Override
    public Vector obtenerInteresados(RegistroValueObject gVO, String[] params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector getAsientosEntradaRegistroPluginTecnico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
            String fechaDesde, String fechaHasta, String documento, String nombre, String apellido1,
            String apellido2, String codAsunto, String unidadRegistroAsunto, String tipoRegistroAsunto,
            String codUorDestino, String ejercicio, String numAnotacion, String codUorAnotacion,
            String codClasificacionAsunto, String unidadRegistroClasifAsunto, String docTecnicoRegistro)
            throws RegistroException {

        try {

            return TramitacionDAO.getInstance().getAsientosEntradaRegistroPluginTecnico(uVO, tVO, params, fechaDesde, fechaHasta, nombreServicio,
                    documento, nombre, apellido1, apellido2, codAsunto,
                    unidadRegistroAsunto, tipoRegistroAsunto, codUorDestino,
                    ejercicio, numAnotacion, codUorAnotacion, codClasificacionAsunto,
                    unidadRegistroClasifAsunto, docTecnicoRegistro);
        } catch (TramitacionException tramExc) {
            throw new RegistroException(tramExc, tramExc.getMessage());
        } catch (TechnicalException techExc) {
            throw new RegistroException(techExc, techExc.getMessage());
        }
    }

    @Override
    public Vector getAsientosExpedientesHistoricoPluginTecnico(UsuarioValueObject uVO, TramitacionValueObject tVO, String[] params,
            String fechaDesde, String fechaHasta, String documento, String nombre, String primerApellido,
            String segundoApellido, String codAsuntoSeleccionado, String unidadRegistroAsuntoSeleccionado,
            String tipoRegistroAsuntoSeleccionado, String codUorInterno, String ejercicio,
            String numAnotacion, String codUorAnotacion, String codClasificacionAsunto, String unidadRegistroClasifAsunto, String docTecnicoRegistro
    ) throws RegistroException {

        try {
            return TramitacionDAO.getInstance().getAsientosExpedientesHistoricoPluginTecnico(uVO, tVO, nombreServicio, params, fechaDesde,
                    fechaHasta, documento, nombre, primerApellido, segundoApellido,
                    codAsuntoSeleccionado, unidadRegistroAsuntoSeleccionado,
                    tipoRegistroAsuntoSeleccionado, codUorInterno, ejercicio,
                    numAnotacion, codUorAnotacion, codClasificacionAsunto,
                    unidadRegistroClasifAsunto, docTecnicoRegistro);
        } catch (TramitacionException tramExc) {
            throw new RegistroException(tramExc, tramExc.getMessage());
        } catch (TechnicalException techExc) {
            throw new RegistroException(techExc, techExc.getMessage());
        }
    } 
    
    

}
