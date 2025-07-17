/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.portafirmasexternocliente.plugin.service.plugin;

import com.ejie.x43S.v2.ttn.xml.TSignature;
import com.ejie.x43S.v2.ttn.xml.TSignatureList;
import com.ejie.x43S.v2.ttn.xml.TSigner;
import com.ejie.x43S.v2.ttn.xml.TSignerList;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.util.MeLanbideDokusiUtil;
import es.altia.flexia.portafirmasexternocliente.vo.DocumentoTramitacionVO;
import es.lanbide.lan6.adaptadoresPlatea.portafirmas.beans.Lan6PeticionFirma;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import es.altia.flexia.portafirmasexternocliente.plugin.service.PluginPortafirmasExternoService;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.portafirmas.servicios.Lan6PortaFirmasServicios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author jesus.cordoba-perez
 */
public class PluginPortafirmasExternoServiceLan6PortaFirmas implements PluginPortafirmasExternoService{
    
    //Logger
    private static final Logger LOG = Logger.getLogger(PluginPortafirmasExternoServiceLan6PortaFirmas.class);
    
    //Fichero de propiedades de texto
    private static final ResourceBundle APPLICATIONPROPERTIESBOUNDLE = ResourceBundle.getBundle("Portafirmas");
    
    private static final String LAN6PORTAFIRMAS = "PluginPortafirmasExternoCliente/LANBIDE/Lan6PortaFirmas/";

    @Override
    public String enviarDocumentoTramitacionAFirma(DocumentoTramitacionVO doc, int idUsuario, String[] params) throws Exception {
        
        if(LOG.isDebugEnabled()) LOG.debug("enviarDocumentoTramitacionAFirma() : BEGIN");
        
        String cadenaDatos = "";
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        //El procedimiento puede estar formado por un buzon_procedimiento o solo un buzon
        String procedimiento = MeLanbideDokusiUtil.getCodigoProcedimientoPlateaFromNumeroExpediente(doc.getNumExpediente(),false,adapt);//APPLICATIONPROPERTIESBOUNDLEDOKUSI.getString(IDPROC+doc.getCodProcedimiento());
        String buzonPropertie = APPLICATIONPROPERTIESBOUNDLE.getString(LAN6PORTAFIRMAS+"buzon");
        
        String nombreCastTipoDocumental = APPLICATIONPROPERTIESBOUNDLE.getString(LAN6PORTAFIRMAS+"nombreCastTipoDocumental");
        String nombreEuskeraTipoDocumental = APPLICATIONPROPERTIESBOUNDLE.getString(LAN6PORTAFIRMAS+"nombreEuskeraTipoDocumental");
        
         
        if (procedimiento == null || "".equals(procedimiento)) {
            LOG.error("enviarDocumentoTramitacionAFirma el procedimiento es vacio y por tanto no se puede invocar al servicio : END");
            return cadenaDatos;
        }
        
        String OIDDocumento = doc.getIdDocumentoEnGestor();
        String nombreUsuLogado = "";

        Connection con = adapt.getConnection();
        
        String sql = "SELECT USU_NOM FROM "+ GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_COD = ?";
        if(LOG.isDebugEnabled()) LOG.debug("SQL = " + sql);
        
        if(LOG.isDebugEnabled()) LOG.debug("USU_COD = " + idUsuario);
        
        ResultSet rs = null;
        PreparedStatement ps = con.prepareStatement(sql);
        
        ps.setInt(1, idUsuario);
        
        rs = ps.executeQuery();
        
        while (rs.next()){
            nombreUsuLogado = rs.getString("USU_NOM");
        }
        
        if (nombreUsuLogado == null || "".equals(nombreUsuLogado)) {
            LOG.error("enviarDocumentoTramitacionAFirma dni o nombre se encuentra vacio y por tanto no se puede invocar al servicio : END");
            return cadenaDatos;
        }
        
        LOG.debug("nombreUsuLogado vale: " + nombreUsuLogado);
        String buzon = buzonPropertie + nombreUsuLogado;
        
        LOG.debug("procedimiento vale: " + procedimiento);
        LOG.debug("buzon vale: " + buzon);
        LOG.debug("nombreCastTipoDocumental vale: " + nombreCastTipoDocumental);
        LOG.debug("nombreEuskeraTipoDocumental vale: " + nombreEuskeraTipoDocumental);
        
        
        
        LOG.debug("OID Documento vale: " + OIDDocumento);
        
        if (OIDDocumento == null || "".equals(OIDDocumento)) {
            LOG.error("enviarDocumentoTramitacionAFirma OIDDocumento se encuentra vacio y por tanto no se puede invocar al servicio : END");
            return cadenaDatos;
        }
        
        try {
            //Se inicializa el servicio
            LOG.debug("Se genera el servicio con el procedimiento " + procedimiento);
            Lan6PortaFirmasServicios servicio = new Lan6PortaFirmasServicios(procedimiento);
            
            //Se crea el parametro de entrada que se usa para el servicio y se setean los datos
            Lan6PeticionFirma lan6PeticionFirma = new Lan6PeticionFirma();
            
            LOG.debug("numExpediente vale: " + doc.getNumExpediente());
            LOG.debug("ejercicio vale: " + doc.getEjercicio());

            LOG.debug("OidDocumento vale: " + OIDDocumento);
            LOG.debug("NIF Firmante vale: " + doc.getNifUsuarioFirmante());
            LOG.debug("Nombre Firmante vale: " + doc.getNombreUsuarioFirmante());
            LOG.debug("Buzon Firma del Firmante vale: " + doc.getBuzonFirma());
            
            
            lan6PeticionFirma.setNumExpediente(doc.getNumExpediente());
            lan6PeticionFirma.setEjercicio(doc.getEjercicio());
            
            
            lan6PeticionFirma.setOidDocumento(OIDDocumento);
            lan6PeticionFirma.setNombreDocumento(doc.getDescripcion());
            LOG.debug("Extension del documento vale: " + ConstantesDatos.EXTENSION_FICHERO_PDF);
            lan6PeticionFirma.setExtensionDocumento(ConstantesDatos.EXTENSION_FICHERO_PDF);
            
            LOG.debug("Tipo de documento vale: " + Lan6Constantes.TIPO_DOCUMENTAL_RESOLUCION);
            lan6PeticionFirma.setTipoDocumental(Lan6Constantes.TIPO_DOCUMENTAL_RESOLUCION);
            
            lan6PeticionFirma.setNombreCastTipoDocumental(nombreCastTipoDocumental);
            lan6PeticionFirma.setNombreEuskeraTipoDocumental(nombreEuskeraTipoDocumental);
            
            //SENDER no es obligatorio pero se recomienda insertarlo
            lan6PeticionFirma.setSenderMailBox(buzon);
            
            //SIGNER
            LOG.debug("Se genera la clase TSignatureList");
            TSignatureList tSignatureList = TSignatureList.Factory.newInstance();
            TSignature tSignature = tSignatureList.addNewSignature();
            
            TSignerList tSignerList = TSignerList.Factory.newInstance();
            TSigner tSigner = tSignerList.addNewSigner();
            
            LOG.debug("Se ha generado la clase TSigner y se setean los datos del informante");
            tSigner.setID(doc.getNifUsuarioFirmante());
            tSigner.setName(doc.getNombreUsuarioFirmante());
            tSigner.setMailboxID(doc.getBuzonFirma());
            
            tSignature.setSignerList(tSignerList);
            
            lan6PeticionFirma.settSignatureList(tSignatureList);

            LOG.debug("Se invoca al servicio servicio.documentSignRequestLan6");
            String resultado = servicio.documentSignRequestLan6(lan6PeticionFirma);
            
            LOG.debug("resultado vale: " + resultado);
            
            if (resultado != null && "true".equals(resultado.trim())) {
                LOG.debug("El resultado ha sido OK y se setea la informacion necesaria para insertar el estado del portafirmas en el mapa");
                LOG.debug("1 OID Documento, 2 Extension Documento, 3 Buzon, 4 Buzon firma");
                cadenaDatos = OIDDocumento + "$" + ConstantesDatos.EXTENSION_FICHERO_PDF + "$" + buzon + "$" + doc.getBuzonFirma() ;
                
            } else {
                LOG.debug("El resultado ha sido KO y por tanto el mapa se enviara nulo");
            }
            
            if(LOG.isDebugEnabled()) LOG.debug("enviarDocumentoTramitacionAFirma() : END");
            
        } catch (Lan6Excepcion ex) {
            LOG.error("Se ha producido un error llamando al Servicio de Lan6PortaFirmasServicios " + ex.getMessage());
            ex.printStackTrace();
            throw new Exception (ex.getMessage());
        } catch (Exception e) {
            LOG.error("Se ha producido una Excepcion generica en PluginPortafirmasExternoServiceLan6PortaFirmas: " + e.getMessage());
            e.printStackTrace();
            throw new Exception (e.getMessage());
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(ps);
            SigpGeneralOperations.devolverConexion(adapt, con);
        }
        
        LOG.debug("cadenaDatos vale: " + cadenaDatos);
        if(LOG.isDebugEnabled()) LOG.debug("enviarDocumentoTramitacionAFirma() : END");
        
        return cadenaDatos;
    }
    
}
