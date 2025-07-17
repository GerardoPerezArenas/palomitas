package es.altia.agora.business.registro.persistence.manual;


import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.AnotacionRegistroManager;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.technical.Fecha;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

public class RegistroDesdeFicheroDAO {

    /* * Declaracion de servicios */

    protected static Config m_ConfigTechnical; //Para el fichero de configuracion tecnico
    protected static Config m_ConfigError; //Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(RegistroDesdeFicheroDAO.class.getName());

    protected static String codAplicacion="1";
    String[] nombreCampos= { "expediente","tipoDocumento","documento","nombre","apellidos","tipoVia","via",
            "numDesde","bis","bloque","portal","escalera","piso","puerta","poblacion","cp"};
    int longitudLineaFichero = 260;

    /* AUTOMATICOS!!!!!!!! */
    String asunto = ". EXPEDIENTE: ";
    String codTipoDocumento="0";
    int tipoSalida=0;
    String unidadTramitadora="1";

    /* Podria ser conveniente consultarlos de la tabla T_TID de la BD */
    String codSinDocumento ="0";
    String codNIF ="1";
    String codPasaporte ="2";
    String codTarjResidencia ="3";
    String codCIF ="4";
    String codCIFEntPublica ="5";
    String codProvDesconocida ="99";
    String codMunDesconocido ="999";
    String codPaisDefecto="108";

    String rer_ejercicio = "";
    String rer_numero = "";
    String rer_fecha = "";
    String rer_tipo = "";
    String rer_dep_cod = "";
    String rer_uor_cod = "";

    String munIdPais="";
    String munIdPrv="";
    String munIdMun="";
    String munNomMun=""; // Nombre corto
    String munNomLMun=""; // Nombre largo

    String dnnIdDom="";
    String dnnIdPais="";
    String dnnIdProv="";
    String dnnIdMun="";
    String dnnDomicilio="";
    String dnnCP="";
    String dnnBarriada="";
    String dnnSit="";
    String dotIdDom="";
    String dotIdTerc="";
    String dotTOC="";
    String dotSit="";
    String dotPadron="";


    /**
     * Construye un nuevo SelectListaDAO. Es protected, por lo que la unica manera de instanciar esta clase
     * es usando el factory method <code>getInstance</code>
     */
    protected RegistroDesdeFicheroDAO() {
        super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

        // Atributos tabla
        rer_ejercicio = m_ConfigTechnical.getString("SQL.R_RER.ejercicio");
        rer_numero = m_ConfigTechnical.getString("SQL.R_RER.numero");
        rer_fecha = m_ConfigTechnical.getString("SQL.R_RER.fecha");
        rer_tipo = m_ConfigTechnical.getString("SQL.R_RER.tipo");
        rer_dep_cod = m_ConfigTechnical.getString("SQL.R_RER.dep_cod");
        rer_uor_cod = m_ConfigTechnical.getString("SQL.R_RER.uor_cod");

        munIdPais= m_ConfigTechnical.getString("SQL.T_MUN.idPais");
        munIdPrv= m_ConfigTechnical.getString("SQL.T_MUN.idProvincia");
        munIdMun= m_ConfigTechnical.getString("SQL.T_MUN.idMunicipio");
        munNomMun= m_ConfigTechnical.getString("SQL.T_MUN.nombre");
        munNomLMun= m_ConfigTechnical.getString("SQL.T_MUN.nombreLargo");

        dnnIdDom=m_ConfigTechnical.getString("SQL.T_DNN.idDomicilio");
        dnnIdPais=m_ConfigTechnical.getString("SQL.T_DNN.idPaisD");
        dnnIdProv=m_ConfigTechnical.getString("SQL.T_DNN.idProvinciaD");
        dnnIdMun=m_ConfigTechnical.getString("SQL.T_DNN.idMunicipioD");
        dnnDomicilio=m_ConfigTechnical.getString("SQL.T_DNN.domicilio");
        dnnCP=m_ConfigTechnical.getString("SQL.T_DNN.codigoPostal");
        dnnBarriada=m_ConfigTechnical.getString("SQL.T_DNN.barriada");
        dnnSit=m_ConfigTechnical.getString("SQL.T_DNN.situacion");
        dotIdDom=m_ConfigTechnical.getString("SQL.T_DOT.idDomicilio");
        dotIdTerc=m_ConfigTechnical.getString("SQL.T_DOT.idTercero");
        dotTOC=m_ConfigTechnical.getString("SQL.T_DOT.tipoUso");
        dotSit=m_ConfigTechnical.getString("SQL.T_DOT.situacion");
        dotPadron=m_ConfigTechnical.getString("SQL.T_DOT.padron");


    }


    public Vector insertRegistrosDesdeFichero (GeneralValueObject gVO,String[] params)
            throws AnotacionRegistroException {

        Vector lista= new Vector();

        try {

            String fichero = (String)gVO.getAtributo("fichero");
            File file = new File(fichero);
            FileInputStream fIS = new FileInputStream(fichero);
            BufferedReader bufferR = new BufferedReader(new InputStreamReader(fIS));

            String ejercicioD = (String) gVO.getAtributo("ejercicioDesde");
            String numeroD = (String) gVO.getAtributo("numeroDesde");
            Vector campos= null;

            int iLinea=0;
            String linea = new String();
            linea = bufferR.readLine();	// Primera linea: se ignora.
            linea = bufferR.readLine(); // Segunda linea: define long. campos.
            campos = definirCampos(linea, nombreCampos);

            while((linea = bufferR.readLine())!=null){

                m_Log.debug("!!!!!!!!!! linea a tratar "+linea);
                GeneralValueObject dVO = extraerCampos(linea, campos);
                dVO.setAtributo("ejercicio", ejercicioD);
                dVO.setAtributo("numero", Integer.toString(Integer.parseInt(numeroD)+iLinea));
                if (linea.length() <= longitudLineaFichero ){
                    if (m_Log.isDebugEnabled()) m_Log.debug("insertRegistrosDesdeFichero." + dVO.getAtributo("nombre") +" " + dVO.getAtributo("apellidos"));
                    // Tratar tercero
                    TercerosValueObject terVO = tratarTercero(dVO,gVO,params);
                    if (terVO != null) {

                        if (m_Log.isDebugEnabled())  m_Log.debug("!!!!!!!!!!!!!! TERCERO TRATADO " + dVO.getAtributo("codTercero") + " " +dVO.getAtributo("nombre")
                                +  " " +dVO.getAtributo("apellidos") +  " Version " + dVO.getAtributo("versionTercero")
                                +  " Domicilio " + dVO.getAtributo("codDomicilio"));

                        // Tratar registro
                        if (!tratarRegistro(dVO, gVO,params)){
                            lista.addElement(dVO);
                        }
                    } else {
                        lista.addElement(dVO);
                    }
                } else {
                    lista.addElement(dVO);
                }
                iLinea++;
            }
            fIS.close();
        } catch(Exception e){
            e.printStackTrace();
            if (m_Log.isDebugEnabled())  m_Log.error("Registro/RegistroDesdeFicheroDAO: insertRegistrosDesdeFichero. Mensaje exception:" + e.toString());
            lista = new Vector();
            new AnotacionRegistroException(m_ConfigError.getString("Error.Registro/RegistroDesdeFicheroDAO: insertRegistrosDesdeFichero .sql"), e);
        } finally {
            return lista;
        }

    }

    /**
     * Factory method para el<code>Singelton</code>.
     * @return La unica instancia de SelectListaDAO.The only CustomerDAO instance.
     */
    public static RegistroDesdeFicheroDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized(RegistroDesdeFicheroDAO.class) {
                if (instance == null) {
                    instance = new RegistroDesdeFicheroDAO();
                }
            }
        }
        return instance;
    }

    /*
    * Mi propia instancia. Usada en el metodo getInstance
    */
    private static RegistroDesdeFicheroDAO instance = null;


    private Vector leerFicheroINE(GeneralValueObject gVO){
        String fichero = (String)gVO.getAtributo("fichero");
        Vector resultado = new Vector();
        try{
            File file = new File(fichero);
            FileInputStream fIS = new FileInputStream(fichero);
            BufferedReader bufferR = new BufferedReader(new InputStreamReader(fIS));
            String linea = new String();
            while((linea = bufferR.readLine())!=null){
                resultado.add(linea);
            }
            fIS.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    private Vector definirCampos(String linea, String[] nombresCampos) {

        Vector resultado = new Vector();
        int ini = 0;
        int fin = linea.indexOf(" "); //busco el separador
        int i=0;

        while (ini<linea.length() && ' '==linea.charAt(ini)) ini++;
        fin = linea.indexOf(" ",ini);

        while ( ini < linea.length()) { //mientras no encuentre el último campo
            Vector campo= new Vector();
            campo.addElement(nombresCampos[i]);
            campo.addElement(Integer.toString(ini));
            campo.addElement(Integer.toString(fin));
            if (m_Log.isDebugEnabled())  m_Log.debug("--------> " + nombresCampos[i] + " ("+ini+","+fin+")");
            resultado.addElement(campo);
            i++;
            ini = fin + 1; //voy a buscar desde la última posición encontrada

            while (ini<linea.length() && ' '==linea.charAt(ini)) ini++;
            if (linea.indexOf(" ",ini)==-1)
                fin= linea.length();
            else fin = linea.indexOf(" ",ini);

        }
        return resultado;
    }



    private GeneralValueObject extraerCampos(String linea, Vector campos) {
        GeneralValueObject dVO= new GeneralValueObject();
        for (int i=0; i<campos.size(); i++){
            Vector pos= (Vector) campos.elementAt(i);
            if (i==campos.size()-1) {
                m_Log.debug(pos.elementAt(0)+" "+linea.substring(Integer.parseInt((String)pos.elementAt(1))).trim());
                dVO.setAtributo((String)pos.elementAt(0), linea.substring(Integer.parseInt((String)pos.elementAt(1))).trim());
            } else {
                m_Log.debug(pos.elementAt(0)+" "+linea.substring(Integer.parseInt((String)pos.elementAt(1)),Integer.parseInt((String)pos.elementAt(2))).trim());
                dVO.setAtributo((String)pos.elementAt(0), linea.substring(Integer.parseInt((String)pos.elementAt(1)),Integer.parseInt((String)pos.elementAt(2))).trim());
            }
        }
        return dVO;
    }


    private GeneralValueObject datos(String linea) {
        GeneralValueObject dVO= new GeneralValueObject();
        dVO.setAtributo("expediente", linea.substring(0,15));
        dVO.setAtributo("DNI", linea.substring(15,29));
        dVO.setAtributo("nombre", linea.substring(29,49));
        dVO.setAtributo("apellidos", linea.substring(49,84));
        dVO.setAtributo("tipoVia", linea.substring(84,95));
        dVO.setAtributo("via", linea.substring(95,125));
        dVO.setAtributo("numeroDesde", linea.substring(125,134));
        dVO.setAtributo("bis", linea.substring(134,138));
        dVO.setAtributo("bloque", linea.substring(138,145));
        dVO.setAtributo("portal", linea.substring(145,152));
        dVO.setAtributo("escalera", linea.substring(152,161));
        dVO.setAtributo("piso", linea.substring(161,166));
        dVO.setAtributo("puerta", linea.substring(166,173));
        dVO.setAtributo("poblacion", linea.substring(173,208));
        dVO.setAtributo("cp", linea.substring(208,220));

        return dVO;
    }

    private String letraNIF(String nif) throws Exception {
        String cadena = "TRWAGMYFPDXBNJZSQVHLCKET";
        int pos =Integer.parseInt(nif) % 23;
        return(String.valueOf(cadena.charAt( pos)));
    }

    private TercerosValueObject crearTercero(GeneralValueObject linea, String codUsuario, String tid, String doc) {
        TercerosValueObject tercVO = new TercerosValueObject();
        tercVO.setTipoDocumento(tid);
        tercVO.setDocumento(doc);
        String nom = null;
        String ap1 = null;
        String ap2 = null;
        if ((codNIF.equals(tid))||(codSinDocumento.equals(tid))) {
            if (linea.getAtributo("apellidos") != null){
                nom = (String) linea.getAtributo("nombre");
                int posEspBlanco = ((String) linea.getAtributo("apellidos")).indexOf(" ");
                if (posEspBlanco != -1) {
                    ap1 = ((String) linea.getAtributo("apellidos")).substring(0,posEspBlanco);
                    ap2 = ((String) linea.getAtributo("apellidos")).substring(posEspBlanco+1);
                } else { // En el primer apellido
                    ap1 = (String) linea.getAtributo("apellidos");
                }
            }
        } else if (codPasaporte.equals(tid)||codTarjResidencia.equals(tid)) {
            nom = (String) linea.getAtributo("nombre");
            if (linea.getAtributo("apellidos") != null){
                ap1 = (String) linea.getAtributo("apellidos");
                ap2 = null;
            }
        } else if (codCIF.equals(tid) || codCIFEntPublica.equals(tid)){
            nom = (String) linea.getAtributo("apellidos");
            ap1=null;
            ap2=null;
        } else {
            nom = (String) linea.getAtributo("nombre");
            if (nom == null || nom.equals("")) {
                nom = (String) linea.getAtributo("apellidos");
            }
        }
        m_Log.debug("!!!!!!!!!!!!!!! tid: "+tid);
        m_Log.debug("!!!!!!!!!!!!!!! nom: "+nom);
        m_Log.debug("!!!!!!!!!!!!!!! ap1: "+ap1);
        m_Log.debug("!!!!!!!!!!!!!!! ap2: "+ap2);
        if ( nom!=null && nom.length()>80)
            tercVO.setNombre(nom.substring(0,80));
        else tercVO.setNombre(nom);
        tercVO.setPartApellido1(null);
        if (ap1 != null) {
            if ( ap1.length()>25){
                tercVO.setApellido1(ap1.substring(0,25));
            } else {
                tercVO.setApellido1(ap1);
            }
        } else tercVO.setApellido1(null);
        tercVO.setPartApellido2(null);
        if (ap2 != null) {
            if ( ap2.length()>25){
                tercVO.setApellido2(ap2.substring(0,25));
            } else {
                tercVO.setApellido2(ap2);
            }
        } else tercVO.setApellido2(ap2);
        tercVO.setTelefono(null);
        tercVO.setEmail(null);
        tercVO.setSituacion('A');
        tercVO.setNormalizado("0");
        tercVO.setUsuarioAlta(codUsuario);
        tercVO.setModuloAlta(codAplicacion);
        tercVO.setVersion("1");
        String nombre = "";
        tercVO.setNombreCompleto(tercVO.getNombre() + (tercVO.getApellido1()!=null? " " + tercVO.getApellido1():""));
        return tercVO;
    }

    private DomicilioSimpleValueObject crearDomicilio(GeneralValueObject linea, String[] params) {
        DomicilioSimpleValueObject domicilio= new DomicilioSimpleValueObject();
        domicilio.setNormalizado("2");
        domicilio.setCodTipoUso("");
        domicilio.setEnPadron("0");
        //domicilio.getIdTipoVia();
        String idProvincia=codProvDesconocida;
        String idMunicipio=codMunDesconocido;
        if (linea.getAtributo("cp")!=null && !linea.getAtributo("cp").equals("")) {
            idProvincia = ( (String) linea.getAtributo("cp")).substring(0,2);
            if (linea.getAtributo("poblacion")!=null){
                String codMunicipio = buscarMunicipio(idProvincia, (String) linea.getAtributo("poblacion"), params);
                if (codMunicipio != null){
                    idMunicipio = codMunicipio;
                }  else {
                    idProvincia=codProvDesconocida;
                    idMunicipio=codMunDesconocido;
                }
            } else {
                idProvincia=codProvDesconocida;
                idMunicipio=codMunDesconocido;
            }
        }

        domicilio.setIdProvincia(idProvincia);
        domicilio.setIdMunicipio(idMunicipio);

        /* NO COINCIDEN TIPOS
        domicilio.getNumDesde((String) linea.getAtributo("numDesde"));
        domicilio.setBloque((String) linea.getAtributo("bloque"));
        domicilio.setPortal((String) linea.getAtributo("portal"));
        domicilio.setEscalera((String) linea.getAtributo("escalera"));
        domicilio.setPlanta((String) linea.getAtributo("piso"));
        domicilio.setPuerta((String) linea.getAtributo("puerta"));
        */
        /* NO LOS TIENEN
              domicilio.getIdPaisVia();
              domicilio.getIdProvinciaVia();
              domicilio.getIdMunicipioVia();
              domicilio.getIdVia();
              domicilio.getLetraDesde();
              domicilio.getNumHasta();
              domicilio.getLetraHasta();
              */
        domicilio.setDomicilio((String) linea.getAtributo("tipoVia") +" "+ (String) linea.getAtributo("via")
                +" " + (String) linea.getAtributo("numDesde") + " " + (String) linea.getAtributo("bis")
                +" " + (String) linea.getAtributo("bloque") + " " + (String) linea.getAtributo("portal")
                +" " + (String) linea.getAtributo("escalera") + " " + (String) linea.getAtributo("piso")
                +" " + (String) linea.getAtributo("puerta") );
        domicilio.setBarriada((String) linea.getAtributo("poblacion"));
        domicilio.setCodigoPostal((String) linea.getAtributo("cp"));
        return domicilio;
    }

    private TercerosValueObject tratarTercero(GeneralValueObject dVO, GeneralValueObject gVO, String[] params){

        boolean tratamientoTercero=true;
        TercerosValueObject terVO = new TercerosValueObject();

        try {
            m_Log.debug("\n!!!!!!!!!!!!!!! EN TRATARTERCERO");
        String usuarioQRegistra =(String)gVO.getAtributo("usuarioQRegistra");
        String idProvincia = (String)gVO.getAtributo("codProvincia");
        String idMunicipio= (String)gVO.getAtributo("codMunicipio");

        //	Buscar tercero
        int domicilio=0;

        String tipoDocumento = (String) dVO.getAtributo("tipoDocumento");
        String documento = (String) dVO.getAtributo("documento");
            m_Log.debug("!!!!!!!!!!!!!!! TIPODOCUMENTO "+tipoDocumento);
            m_Log.debug("!!!!!!!!!!!!!!! DOCUMENTO "+documento);
            m_Log.debug("!!!!!!!!!!!!!!! DOCUMENTO.longitud "+documento.length());

        if (tipoDocumento==null || tipoDocumento.equals("")) {
            try {
            tipoDocumento = codNIF;
        if (!documento.equals("")) {
        if ("".equals( ((String) dVO.getAtributo("nombre")).trim())){
            tipoDocumento=codCIF;
        } else {
            if (documento.toUpperCase().charAt(0)=='X'){
                tipoDocumento=codTarjResidencia;
            } else if (documento.toUpperCase().charAt(0)<'0' || documento.toUpperCase().charAt(0)>'9' ){
                tipoDocumento=codPasaporte;
                        } else if ((documento.length()==9) && (documento.toUpperCase().charAt(8)<'0' || documento.toUpperCase().charAt(8)>'9' )) {
                if (!letraNIF(documento.substring(0,8)).equalsIgnoreCase(documento.substring(8))) {
                    tipoDocumento = codPasaporte;
                }
            } else if (documento.length()>8) {
                tipoDocumento = codPasaporte;
            } else { // NIF: autocompletar.
                            String documentoCompletado = "00000000".substring(0,8-documento.length()) + documento;
                            try {
                                documentoCompletado = documentoCompletado + letraNIF(documentoCompletado);
                                documento = documentoCompletado;
                            } catch (Exception e) {
                                throw new Exception(e);
                            }
            }
        }
        } else {
            tipoDocumento=codSinDocumento;
        }
            } catch (Exception e) {
                m_Log.debug("\n");
                m_Log.debug("!!!!!!!!!!!!!!! NO RECONOCE EL DOCUMENTO ASI QUE SUPONEMOS QUE ES PASAPORTE");
                m_Log.debug("\n");
                tipoDocumento = codPasaporte;
            }
        }
        if (tipoDocumento.equals(codNIF) && documento.length()<=8) {
            documento = "00000000".substring(0,8-documento.length()) + documento;
            documento = documento + letraNIF(documento);
        }
        terVO.setTipoDocumento(tipoDocumento);
        terVO.setDocumento(documento);
            m_Log.debug("!!!!!!!!!!!!!!! TIPO DE DOCUMENTO DESPUES DE PARSEARLO "+tipoDocumento);
            m_Log.debug("!!!!!!!!!!!!!!! DOCUMENTO DESPUES DE PARSEARLO "+documento);

            terVO = crearTercero(dVO,usuarioQRegistra,tipoDocumento,documento);
            TercerosManager terMan = TercerosManager.getInstance();
            if (!terMan.existeTercero(terVO,params)) {
                if (m_Log.isDebugEnabled())  m_Log.debug("TratarTercero. NO existe el tercero");
            /*  if (terVO.getTipoDocumento().equals(codSinDocumento)) {
                terVO.setApellido2(terVO.getApellido2() +  "(" + nuevoDocumento + ")");
                }*///Se utilizaria en caso de documentos extraños para fijarlos como sin documento
                int codTerc = TercerosManager.getInstance().setTercero(terVO,params);
                if(codTerc >0) {//INSERTO EL DOMICILIO DEL HABITANTE
                    terVO.setIdentificador(Integer.toString(codTerc));
                    DomicilioSimpleValueObject domicilioVO = crearDomicilio(dVO,params);
                    domicilioVO.setEsDomPrincipal("true");
                    Vector dom = new Vector();
                    dom.add(domicilioVO);
                    terVO.setDomicilios(dom);
                    terVO.setDomPrincipal(domicilioVO.getIdDomicilio());
                    domicilio = TercerosManager.getInstance().setDomicilioTercero(terVO, params);
                    if (domicilio == 0) tratamientoTercero = false;
                } else tratamientoTercero=false; //ERROR
            } else {
                if (!terVO.getTipoDocumento().equals("0")) {
                Vector listaCodTerc = TercerosManager.getInstance().getByDocumento(terVO,params);
                if (m_Log.isDebugEnabled())  m_Log.debug("TratarTercero. Existe el tercero: " + listaCodTerc.size());
                for(int m=0;m<listaCodTerc.size();m++) {
                    TercerosValueObject g =(TercerosValueObject) listaCodTerc.elementAt(m);
                    String cTerc = g.getIdentificador();
                    String versionTerc = g.getVersion();
                    int codTerc = Integer.parseInt(cTerc);
                    if(codTerc >0) {
                        terVO.setIdentificador(Integer.toString(codTerc));
                        terVO.setVersion(versionTerc);
                        terVO.setUsuarioAlta(usuarioQRegistra);
                        DomicilioSimpleValueObject domicilioVO = crearDomicilio(dVO,params);
                        // Comprobar si existe el domicilio
                        String codDomicilioBuscado = existeDomicilio(domicilioVO, params, codTerc);
                        if ( codDomicilioBuscado == null){
                            domicilioVO.setEsDomPrincipal("true");
                            Vector dom = new Vector();
                            dom.add(domicilioVO);
                            terVO.setDomicilios(dom);
                            terVO.setDomPrincipal(domicilioVO.getIdDomicilio());
                            domicilio = TercerosManager.getInstance().setDomicilioTercero(terVO,params);
                        } else {
                            domicilio = Integer.parseInt(codDomicilioBuscado);
                        }
                        if (domicilio == 0) tratamientoTercero = false;
                    } else tratamientoTercero = false;
                }
            } else {
                    Vector listaCodTerc = TercerosManager.getInstance().getIdTercero(terVO,params,"noModificar");
                    if (m_Log.isDebugEnabled())  m_Log.debug("TratarTercero. Existe el tercero: " + listaCodTerc.size());
                    for(int m=0;m<listaCodTerc.size();m++) {
                        GeneralValueObject g =(GeneralValueObject) listaCodTerc.elementAt(m);
                        String cTerc = (String) g.getAtributo("codTercero");
                        String versionTerc = (String) g.getAtributo("versionTercero");
                        int codTerc = Integer.parseInt(cTerc);
                        if(codTerc >0) {
                            terVO.setIdentificador(Integer.toString(codTerc));
                            terVO.setVersion(versionTerc);
                            terVO.setUsuarioAlta(usuarioQRegistra);
                            DomicilioSimpleValueObject domicilioVO = crearDomicilio(dVO,params);
                            // Comprobar si existe el domicilio
                            String codDomicilioBuscado = existeDomicilio(domicilioVO, params, codTerc);
                            if ( codDomicilioBuscado == null){
                                domicilioVO.setEsDomPrincipal("true");
                                Vector dom = new Vector();
                                dom.add(domicilioVO);
                                terVO.setDomicilios(dom);
                                terVO.setDomPrincipal(domicilioVO.getIdDomicilio());
                                domicilio = TercerosManager.getInstance().setDomicilioTercero(terVO,params);
                            } else {
                                domicilio = Integer.parseInt(codDomicilioBuscado);
                            }
                            if (domicilio == 0) tratamientoTercero = false;
                        } else tratamientoTercero = false;
                    }

            }
        }
            dVO.setAtributo("codTercero", terVO.getIdentificador());
            dVO.setAtributo("versionTercero", terVO.getVersion());
            dVO.setAtributo("codDomicilio", Integer.toString(domicilio));
        } catch(Exception e){
            e.printStackTrace();
            if (m_Log.isDebugEnabled())  m_Log.error("tratarTercero. Mensaje exception:" + e.toString());
            terVO= null;
        } finally {
            if (!tratamientoTercero) terVO = null;
        }
        return terVO;
    }

    private boolean tratarRegistro(GeneralValueObject dVO, GeneralValueObject gVO, String[] params) throws TechnicalException{

        RegistroValueObject elRegistroESVO = new RegistroValueObject();

        elRegistroESVO.setIdOrganizacion(Integer.parseInt((String) gVO.getAtributo("orgCod")));
        elRegistroESVO.setIdEntidad(Integer.parseInt((String) gVO.getAtributo("entCod")));
        elRegistroESVO.setIdentDepart(Integer.parseInt((String) gVO.getAtributo("depCod")));
        elRegistroESVO.setUnidadOrgan(Integer.parseInt((String) gVO.getAtributo("unidadOrgCod")));
        // Datos usuario que registra.
        elRegistroESVO.setUsuarioQRegistra((String) gVO.getAtributo("usuarioQRegistra"));
        elRegistroESVO.setDptoUsuarioQRegistra((String) gVO.getAtributo("dptoUsuarioQRegistra"));
        elRegistroESVO.setUnidOrgUsuarioQRegistra((String) gVO.getAtributo("unidOrgUsuarioQRegistra"));
        elRegistroESVO.setTipoReg((String) gVO.getAtributo("tipoRegistro"));
        elRegistroESVO.setAnoReg(Integer.parseInt((String) dVO.getAtributo("ejercicio")));
        elRegistroESVO.setNumReg(Long.parseLong((String) dVO.getAtributo("numero")));
        //	Fechas del servidor.
        Fecha f=new Fecha();
        Date fSistema = new Date();
        String hora=f.construirHora(fSistema);
        elRegistroESVO.setFecEntrada(Fecha.obtenerString(fSistema) + " " + hora);
        elRegistroESVO.setFecHoraDoc(Fecha.obtenerString(fSistema) + " " + hora);
        elRegistroESVO.setHayBuzon(false);
        //elRegistroESVO.setAnoReg( Integer.parseInt(Fecha.obtenerString(fSistema).substring(6,10)));
        elRegistroESVO.setRespOpcion("actualizacionConfirmada"); // No se si es necesario
        elRegistroESVO.setCodInter(Integer.parseInt((String) dVO.getAtributo("codTercero")));
        elRegistroESVO.setDomicInter(Integer.parseInt((String) dVO.getAtributo("codDomicilio")));
        elRegistroESVO.setNumModInfInt(Integer.parseInt((String) dVO.getAtributo("versionTercero")));
        Vector listaCodTercero=new Vector();
        listaCodTercero.add(elRegistroESVO.getCodInter()+"");
        elRegistroESVO.setlistaCodTercero(listaCodTercero);

        Vector listaVersionTercero=new Vector();
        listaVersionTercero.add(elRegistroESVO.getNumModInfInt()+"");
        elRegistroESVO.setlistaVersionTercero(listaVersionTercero);

        Vector listaCodDomicilio=new Vector();
        listaCodDomicilio.add(elRegistroESVO.getDomicInter()+"");
        elRegistroESVO.setlistaCodDomicilio(listaCodDomicilio);

        Vector listaRol=new Vector();
        listaRol.add((String) gVO.getAtributo("rolPorDefecto"));
        elRegistroESVO.setlistaRol(listaRol);
        elRegistroESVO.setEstAnotacion(0);
        elRegistroESVO.setAsunto(gVO.getAtributo("asunto") + asunto + (dVO.getAtributo("expediente")));
        elRegistroESVO.setCodTipoDoc(codTipoDocumento);
        elRegistroESVO.setTipoAnot(tipoSalida);
        elRegistroESVO.setIdUndTramitad(unidadTramitadora); 
        
        try {
            elRegistroESVO= AnotacionRegistroManager.getInstance().getByPrimaryKey(elRegistroESVO,params);
            // Para inicializar la variable cont1 al valor que le corresponde como reserva que es... ¡Lo que hay!
            if (elRegistroESVO.getContador()!= 0) {
                String codUOR = (String) gVO.getAtributo("codUOR");
                if (codUOR!=null && !codUOR.equals("")) {
                    elRegistroESVO.setIdUndTramitad(codUOR);
                }
                AnotacionRegistroManager.getInstance().modify(elRegistroESVO,false,params);
            } else elRegistroESVO.setRespOpcion("anotacionNoEsReserva");

        } catch (AnotacionRegistroException rException) {
            elRegistroESVO.setRespOpcion("anotacionRegistroException");
        }
        String resultado = elRegistroESVO.getRespOpcion();
        if (m_Log.isDebugEnabled())  m_Log.debug("TratarRegistro. Resultado: " + resultado);
        if ("modify_no_realizado".equals(resultado) || "registrar_actualizacion_sin_confirmar".equals(resultado)
                || "no_existe_expediente".equals(resultado) || "proc_mal_relacionado".equals(resultado)
                || "anotacionRegistroException".equals(resultado) || "anotacionNoEsReserva".equals(resultado)){
            return false;
        } else return true;

    }

    public boolean comprobarNumeroReservados(GeneralValueObject gVO){
        boolean resultado=false;
        String numeroD = (String) gVO.getAtributo("numeroDesde");
        String numeroH = (String) gVO.getAtributo("numeroHasta");
        int numeroReservados = Integer.parseInt((String) gVO.getAtributo("numeroHasta")) - Integer.parseInt((String) gVO.getAtributo("numeroDesde")) + 1;
        if (numeroReservados == (contarLineasFichero(gVO)-2)){ // Las dos primeras lineas del fichero se ignoran
            resultado = true;
        }
        return resultado;
    }

    public Vector comprobarReservados(GeneralValueObject gVO, String[] params)
            throws AnotacionRegistroException {
        Vector resultado = new Vector();
        AdaptadorSQLBD aod = null;
        Connection conexion = null;

        try {

            aod = new AdaptadorSQLBD(params);
            conexion = aod.getConnection();
            String ejercicioD = (String) gVO.getAtributo("ejercicioDesde");
            String numeroD = (String) gVO.getAtributo("numeroDesde");
            String numeroH = (String) gVO.getAtributo("numeroHasta");
            String tipoRegistro = (String) gVO.getAtributo("tipoRegistro");
            int organizacion = Integer.parseInt((String) gVO.getAtributo("depCod"));
            int uor = Integer.parseInt((String) gVO.getAtributo("unidadOrgCod"));

            Statement stmt = conexion.createStatement();
            String sql = "select " + rer_ejercicio + "," + rer_numero + " from r_rer where " +
                    rer_ejercicio +"=" + ejercicioD + " AND " +  rer_tipo + "='"+ tipoRegistro +"'" +
                    " AND " + rer_dep_cod + "=" + organizacion + " AND " + rer_uor_cod + "=" + uor+
                    " AND " + rer_numero + "  between " + numeroD + " AND " + numeroH +
                    " order by " + rer_numero + " asc ";

            if (m_Log.isDebugEnabled())  m_Log.debug("comprobarReservas. " + sql);
            ResultSet rs = stmt.executeQuery(sql);
            String numero = numeroD;
            if (rs.next()){
                String numeroRER =	rs.getString(rer_numero);
                while (!numero.equals(numeroRER) && (Integer.parseInt(numero)<=Integer.parseInt(numeroH))){
                    GeneralValueObject noReservaVO = new GeneralValueObject();
                    noReservaVO.setAtributo("ejercicio",ejercicioD);
                    noReservaVO.setAtributo("numero",numero);
                    resultado.addElement(noReservaVO);
                    numero = Integer.toString(Integer.parseInt(numero) + 1);
                }
                numero = Integer.toString(Integer.parseInt(numero) + 1);

                while(rs.next()){
                    numeroRER =	rs.getString(rer_numero);
                    while (!numero.equals(numeroRER) && (Integer.parseInt(numero)<=Integer.parseInt(numeroH))){
                        GeneralValueObject noReservaVO = new GeneralValueObject();
                        noReservaVO.setAtributo("ejercicio",ejercicioD);
                        noReservaVO.setAtributo("numero",numero);
                        resultado.addElement(noReservaVO);
                        numero = Integer.toString(Integer.parseInt(numero) + 1);
                    }
                    numero = Integer.toString(Integer.parseInt(numero) + 1);
                }

                while ( (Integer.parseInt(numero)<=Integer.parseInt(numeroH))){
                    GeneralValueObject noReservaVO = new GeneralValueObject();
                    noReservaVO.setAtributo("ejercicio",ejercicioD);
                    noReservaVO.setAtributo("numero",numero);
                    resultado.addElement(noReservaVO);
                    numero = Integer.toString(Integer.parseInt(numero) + 1);
                }

            } else {
                while ( (Integer.parseInt(numero)<=Integer.parseInt(numeroH))){
                    GeneralValueObject noReservaVO = new GeneralValueObject();
                    noReservaVO.setAtributo("ejercicio",ejercicioD);
                    noReservaVO.setAtributo("numero",numero);
                    resultado.addElement(noReservaVO);
                    numero = Integer.toString(Integer.parseInt(numero) + 1);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception sqle) {
            sqle.printStackTrace();
            if (m_Log.isDebugEnabled())  m_Log.error("ComprobarReservas. " + sqle.getMessage());
            new AnotacionRegistroException(m_ConfigError.getString("Error.Registro.RegistroDesdeFicheroDAO.comprobarReservas.sql"), sqle);
            resultado = null;
        } finally {
            try {
                aod.devolverConexion(conexion);
            } catch(Exception sqle) {
                sqle.printStackTrace();
                if (m_Log.isDebugEnabled())  m_Log.error("ComprobarReservas. Error al cerrar la conexion con la BD. " + sqle.getMessage());
                new AnotacionRegistroException(m_ConfigError.getString("Error.Registro.RegistroDesdeFicheroDAO.conexion.sql"), sqle);
                resultado = null;
            }
        }
        return resultado;
    }

    private int contarLineasFichero(GeneralValueObject gVO)
    {
        int cuenta = 0;
        try {
            String fichero = (String)gVO.getAtributo("fichero");
            File file = new File(fichero);
            FileInputStream fIS = new FileInputStream(fichero);
            BufferedReader bufferR = new BufferedReader(new InputStreamReader(fIS));
            while( bufferR.readLine()!=null){
                cuenta++;
            }
            fIS.close();
        } catch(Exception e){
            cuenta=0;
            e.printStackTrace();
            if (m_Log.isDebugEnabled()) m_Log.error("contarLineasFichero. Exception. " + e.getMessage());
        } finally {
            return cuenta;
        }
    }


    private String buscarMunicipio(String idProvincia, String nomMun, String[] params){

        String idMunBuscado = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT " + munIdMun +" FROM "+ GlobalNames.ESQUEMA_GENERICO+"T_MUN T_MUN WHERE " + munIdPais +" =? AND " + munIdPrv +" =? AND ("
                + munNomMun + " = UPPER(?)  OR " + munNomLMun +"=UPPER(?) )";

        if (m_Log.isDebugEnabled()) m_Log.debug("buscarMunicipio. idProvincia: " + idProvincia +" nomMun: " +nomMun);
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            stmt = conexion.prepareStatement(sql);
            stmt.setInt(1,Integer.parseInt(codPaisDefecto));
            stmt.setInt(2,Integer.parseInt(idProvincia));
            if (nomMun.length() > 30)
                stmt.setString(3,nomMun.substring(0,30));
            else stmt.setString(3,nomMun);
            if (nomMun.length() > 50)
                stmt.setString(4,nomMun.substring(0,50));
            else stmt.setString(4,nomMun);

            if (m_Log.isDebugEnabled()) m_Log.debug(sql + ". " + codPaisDefecto +" " + idProvincia + " " + nomMun);
            rs = stmt.executeQuery();
            if (rs.next()){
                idMunBuscado = rs.getString(munIdMun);
            }
            rs.close();
            stmt.close();
        }catch (Exception e){
            idMunBuscado = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        } finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }

        }
        if (m_Log.isDebugEnabled()) m_Log.debug("buscarMunicipio: " + (idMunBuscado!=null?idMunBuscado:"no encontrado"));
        return idMunBuscado;
    }

    private String existeDomicilio( DomicilioSimpleValueObject domVO, String[] params, int idTercero){

        String codDomicilioExistente = null;
        AdaptadorSQLBD abd = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT " + dnnIdDom +" FROM T_DNN, T_DOT WHERE "
                + dnnIdDom +"=" + dotIdDom + " AND "
                + dnnSit +"='A' AND " + dotSit +"='A' AND "
                + dnnIdPais +" =? AND " + dnnIdProv +" =? AND "
                + dnnIdMun + " =?  AND " + dnnBarriada +" LIKE ? AND "
                + dnnDomicilio+" LIKE ? AND " + dnnCP+" =?  AND "
                + dotIdTerc + " =? ";

        if (m_Log.isDebugEnabled()) m_Log.debug("existeDomicilio.");
        try{
            abd = new AdaptadorSQLBD(params);
            conexion = abd.getConnection();
            stmt = conexion.prepareStatement(sql);
            stmt.setInt(1,Integer.parseInt(codPaisDefecto));
            stmt.setInt(2,Integer.parseInt(domVO.getIdProvincia()));
            stmt.setInt(3,Integer.parseInt(domVO.getIdMunicipio()));
            stmt.setString(4,domVO.getBarriada());
            stmt.setString(5,domVO.getDomicilio());
            stmt.setString(6,domVO.getCodigoPostal());
            stmt.setInt(7,idTercero);

            if (m_Log.isDebugEnabled())
                m_Log.debug(sql + ". " + codPaisDefecto +" " + domVO.getIdProvincia()
                        + " " + domVO.getIdMunicipio() + " " + domVO.getBarriada()
                        + " " + domVO.getDomicilio() + " " + domVO.getCodigoPostal());
            rs = stmt.executeQuery();
            if (rs.next()){
                codDomicilioExistente = rs.getString(dnnIdDom);
            }
            rs.close();
            stmt.close();
        }catch (Exception e){
            codDomicilioExistente = null;
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
        }finally {
            try{
                abd.devolverConexion(conexion);
            }catch (Exception e){
                e.printStackTrace();
                if(m_Log.isErrorEnabled()) m_Log.error(e.getMessage());
            }
        }
        if (m_Log.isDebugEnabled()) m_Log.debug("existeDomicilio: " + (codDomicilioExistente!=null?codDomicilioExistente:"no encontrado"));
        return codDomicilioExistente;

    }
}