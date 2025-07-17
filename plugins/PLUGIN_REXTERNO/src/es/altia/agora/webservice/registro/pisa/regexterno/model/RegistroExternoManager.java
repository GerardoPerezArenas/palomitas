package es.altia.agora.webservice.registro.pisa.regexterno.model.persistence;


import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.sge.DefinicionProcedimientosValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.TramitacionDAO;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.TipoDocumentosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.interfaces.user.web.util.combo.ComboCVO;
import es.altia.agora.interfaces.user.web.util.combo.ElementoComboCVO;
import es.altia.agora.webservice.registro.exceptions.RegistroException;
import es.altia.agora.webservice.registro.pisa.cliente.FachadaClientePisa;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_AnotacionesBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_InteresadosBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_ParametrosBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_RetornoBean;
import es.altia.agora.webservice.registro.pisa.cliente.datos.SWPisa_TercerosBean;
import es.altia.agora.webservice.registro.pisa.regexterno.model.persistence.manual.RegistroExternoDAO;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author ivan.perez
 */
public class RegistroExternoManager {
    private static RegistroExternoManager instance = null;

    protected static Log m_Log = LogFactory.getLog(RegistroExternoManager.class.getName());
    protected static Config m_ConfigTechnical; //Para el fichero de configuracion tecnico
    protected static Config m_ConfigError; //Para los mensajes de error localizados
    private static ResourceBundle m_TipoDocs =
            ResourceBundle.getBundle("es.altia.agora.webservice.registro.pisa.cliente.constantes.TipoDocumento");
    private static ResourceBundle m_Conf =
            ResourceBundle.getBundle("es.altia.agora.webservice.registro.pisa.cliente.configuracion.configuracion");


    
    protected RegistroExternoManager() {     
        
    }

    public static RegistroExternoManager getInstance() {
        if (instance == null) {
            synchronized (RegistroExternoManager.class) {
                if (instance == null) instance = new RegistroExternoManager();
            }
        }
        return instance;
    }
    
    public RegistroValueObject altaAnotacionRegistroExterno(RegistroValueObject registroVO, String[] params)
            throws AnotacionRegistroException {
        //queremos estar informados de cuando este metodo es ejecutado
        m_Log.debug("altaAnotacionRegistroExterno");        

        try {
            m_Log.info("Usando persistencia manual");            
            int codOrganizacion = registroVO.getIdOrganizacion();      
            String nombreServicio = m_Conf.getString("Pisa." + codOrganizacion + ".nombre");
            String prefijoPropiedad = "Registro/" + codOrganizacion + "/" + nombreServicio + "/" ;
            FachadaClientePisa fachadaPisa = FachadaClientePisa.getInstance(prefijoPropiedad);
            SWPisa_ParametrosBean dato = createSWParametrosBean(registroVO, params);
            SWPisa_RetornoBean retorno = fachadaPisa.gestionarEntradas(dato);
            
            if ("".equals(retorno.getNumero())) {
                throw new AnotacionRegistroException("ERROR ANOTANDO EN REGISTRO EXTERNO");
            }else {
                registroVO.setFecEntrada(retorno.getFecha());
                registroVO.setNumReg(Long.parseLong(retorno.getNumero()));
                registroVO.setAnoReg(Integer.parseInt(retorno.getEjercicio()));
                String tipoAnot = dato.getAnotacion().getTipo();
                registroVO.setTipoReg(tipoAnot);
                registroVO.setIdServicioOrigen(nombreServicio);
                AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
                Connection con = oad.getConnection();
                RegistroExternoDAO.getInstance().guardarReferenciaExpediente(con, registroVO);
                oad.devolverConexion(con);
            }

            m_Log.debug("Registro insertado correctamente");
            //We want to be informed when this method has finalized
            m_Log.debug("altaAnotacionRegistroExterno");

        } catch (Exception ce) {
            m_Log.error("JDBC Technical problem " + ce.getMessage());
            throw new AnotacionRegistroException("Problema técnico de JDBC " + ce.getMessage());

        }
        return registroVO;
    }
    


    private SWPisa_ParametrosBean createSWParametrosBean(RegistroValueObject registroVO, String[] params) throws RegistroException, BDException {  
        String periodo = String.valueOf(registroVO.getAnoReg());
        int uor = registroVO.getUnidadOrgan();
        int organizacion = registroVO.getIdOrganizacion();    
        
        AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
        Connection con = oad.getConnection();
        SWPisa_AnotacionesBean anotacion = new SWPisa_AnotacionesBean();
        SWPisa_InteresadosBean interesado = new SWPisa_InteresadosBean();
        SWPisa_InteresadosBean[] interesados = new SWPisa_InteresadosBean[1];
        m_Log.debug("ANTES DE LLAMAR A TRADUCIR UNIDAD. UOR(SGE): " + uor);
        String uorAccede = RegistroExternoDAO.getInstance().traducirUnidad(con, uor);
        m_Log.debug("DESPUES DE LLAMAR A TRADUCIR UNIDAD. UOR(ACCEDE): " + uorAccede);
        String[] organizacionEntidad = RegistroExternoDAO.getInstance().obtenerOrganizacionEntidad(organizacion);
        String tipoDocumento = RegistroExternoDAO.getInstance().traducirTipoDocumento(registroVO.getTipoDocInteresado());
        interesado.setCodigo_tipo_documento(tipoDocumento);
        interesado.setDocumento(registroVO.getDocumentoInteresado());
        interesado.setNombre(registroVO.getNomCompletoInteresado());
        interesado.setCodigo_tipo_relacion("1");
        
        interesado.setDomicilio(registroVO.getDomCompletoInteresado());
        interesados[0] = interesado;
        anotacion.setInteresados(interesados);
        anotacion.setTipo("S");
        anotacion.setEfecto("1");
        anotacion.setAsunto(registroVO.getAsunto());
        anotacion.setUnidad_organica(uorAccede);
        anotacion.setEjercicio(periodo);
        SWPisa_ParametrosBean dato = new SWPisa_ParametrosBean();
        dato.setOrganizacion(organizacionEntidad[0]);
        dato.setEntidad(organizacionEntidad[1]);
        dato.setEjercicio(periodo);        
        dato.setExpediente(registroVO.getNumExpediente());
        dato.setUnidad(uorAccede);            
        dato.setAnotacion(anotacion);
        dato.setEfecto("1");
        
        oad.devolverConexion(con);
        return dato;    
    }
    
    public ComboCVO getComboUnidadesAccede(String[] params) {                
        ComboCVO combo = new ComboCVO();
        combo.setNombreCombo("UnidadesAccedeCombo");
        try {            
            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            Connection con = oad.getConnection();
            combo = RegistroExternoDAO.getInstance().getComboUnidadesAccede(con);
            oad.devolverConexion(con);            
        } catch (BDException ex) {
            m_Log.debug("ERROR CONSTRUYENDO EL COMBO DE UORS ACCEDE");
        }
        return combo;
    }
    
    public ComboCVO getComboTiposDocumentos(String[] params) {
        ComboCVO combo = new ComboCVO();
        combo.setNombreCombo("tiposDocumentoCombo");
        int i = 0;
        Vector listaTiposDocumentos = TipoDocumentosDAO.getInstance().getListaTipoDocumentos(params);
        Vector<ElementoComboCVO> listaElementosCombo = new Vector<ElementoComboCVO>();
        for (Iterator it = listaTiposDocumentos.iterator();it.hasNext();) {
            GeneralValueObject gVO = (GeneralValueObject)it.next();
            ElementoComboCVO elementoCombo = new ElementoComboCVO();
            String codTipoDocSGE = (String)gVO.getAtributo("codTipoDoc");
            String descTipoDoc = (String)gVO.getAtributo("descTipoDoc");
//            m_Log.debug("Codigo Tipo Documento Sge: "+ codTipoDocSGE);
//            m_Log.debug("Descripcion tipo ducumento: "+ descTipoDoc);
            String codTipoDocServicio = "-1";
            
            try {
                codTipoDocServicio = m_TipoDocs.getString(codTipoDocSGE);
                m_Log.debug("Codigo Tipo Documento Accede: " + codTipoDocServicio);
            } catch (Exception exception) {
                continue;
            }

            elementoCombo.setCodigoElemento(codTipoDocServicio);
 
            elementoCombo.setCodigoElemento(codTipoDocSGE);
            elementoCombo.setDescripcionElemento(descTipoDoc);
            elementoCombo.setCodigoInternoElemento(codTipoDocServicio);
            listaElementosCombo.addElement(elementoCombo);
            i++;

        }
        combo.setElementosCombo(listaElementosCombo);
        return combo;
    }    
    
    public ComboCVO getListaProcedimientosByUOR(String codigoUOR, String[] params) throws TramitacionException, TechnicalException {
        ComboCVO combo = new ComboCVO();
        combo.setNombreCombo("procedimientosByUORCombo");
        Vector listaProcedimientos = TramitacionDAO.getInstance().getListaProcedimientosUOR(codigoUOR, params);
        Vector<ElementoComboCVO> listaElementosCombo = new Vector<ElementoComboCVO>();
        int i = 0;
        for (Iterator it = listaProcedimientos.iterator();it.hasNext();) {
            DefinicionProcedimientosValueObject dpVO = (DefinicionProcedimientosValueObject) it.next();
            ElementoComboCVO elementoCombo = new ElementoComboCVO();
            String codigoElemento = dpVO.getTxtCodigo();
            String descripcionElemento = dpVO.getTxtDescripcion();
            elementoCombo.setCodigoElemento(codigoElemento);
            elementoCombo.setDescripcionElemento(descripcionElemento);
            elementoCombo.setCodigoInternoElemento(i+"");
            listaElementosCombo.addElement(elementoCombo);            
            i++;
        }
        combo.setElementosCombo(listaElementosCombo);
        return combo;
    }
}