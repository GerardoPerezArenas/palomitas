/**
 * WSExpedienteImplSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package es.altia.agora.webservice.tramitacion.servicios.tvg.servicios.expediente;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.interfaces.user.web.sge.FichaExpedienteAction;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.agora.technical.EstructuraNotificacion;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso.ExpedienteDAO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso.TerceroDAO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso.TramiteDAO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.acceso.UsuarioDAO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.ExpedienteVO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.InteresadoVO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.TramiteVO;
import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.SalidaBooleanExpediente;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.cache.CacheDatosFactoria;
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
import java.util.Vector;

	public class WSExpedienteImplSoapBindingImpl implements WSExpedienteImpl {

	    Log m_Log = LogFactory.getLog(WSExpedienteImplSoapBindingImpl.class);
	    	    
	public SalidaBooleanExpediente setExpediente(ExpedienteVO expVO, String org)
	            throws java.rmi.RemoteException {

	        String[] mns = null;
	        SalidaBooleanExpediente salida = new SalidaBooleanExpediente();
	        int seguir;
	        AdaptadorSQLBD bd = null;
	        Connection con = null;
	        Config m_ConfigTechnical = ConfigServiceHelper.getConfig("formulariosPdf");
        String gestorD = m_ConfigTechnical.getString("gestor");
        String driverD = m_ConfigTechnical.getString(org+"/driver");
	        String urlD = "";
        String usuarioD = m_ConfigTechnical.getString(org+"/usuarioFormularios");
        String passwordD = m_ConfigTechnical.getString(org+"/passFormularios");
        String fichLogD = m_ConfigTechnical.getString(org+"/fichlog");
        String jndi = m_ConfigTechnical.getString(org+"/dataSource");


        String params[] = {gestorD, driverD, urlD, usuarioD, passwordD, fichLogD, jndi};
       
	        try {
	            bd = new AdaptadorSQLBD(params);
	            con = bd.getConnection();

	            //Comprobamos el usuario que da de alta el expediente//llega
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("COMPROBANDO QUE EL USUARIO EXISTE");
            }

	            UsuarioDAO usuario = new UsuarioDAO();
	            UsuarioValueObject user = new UsuarioValueObject();
	            user.setParamsCon(params);
	            user.setIdUsuario(Integer.parseInt(expVO.getUsuario()));

	            if (usuario.existeUsuarioCodigo(user, con)) {

	                //Comprobamos el tercero que esta en el expediente
	                if (expVO.getTercero() != null) {
	                    InteresadoVO terVO = expVO.getTercero();
	                    
	                    //MODO ANTERIOR CON WSTERCERO VIEJO
	                      TerceroDAO tercero = new TerceroDAO();
	                      expVO.setTercero(tercero.getTercero(terVO,org));
	                      
	                } else {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("INSERTANDO EXPEDIENTE SIN TERCERO");
                    }
	                }

                    /**
                     * SOLUCIÓN TEMPORAL: Se comprueba si existe un nodo en el xml con los campos del formulario
                     * con las observaciones, si lo hay se recupera para pasarlo a la función de iniciar expediente y que
                     * las incorpore, lo mismo que hace con el asunto. ESTO SE HACE PORQUE DESDE EL PORTAL DE FORMULARIOS NO
                     * LLEGAN LAS OBSERVACIONES PARA EL FORMULARIO DE GESTIÓN DE LA DEMANDA DE CAIXANOVA
                     * Cuando lleguen las observaciones en el objeto ExpedienteVO ya no se extraerán del campo del formulario
                     */
                     String observaciones = extraerObservacionesCamposFormulario(expVO.getCamposFormularios());

                    /***********************/
	                //Hacemos el expediente
	                int res = 0;
	                GeneralValueObject gVO = new GeneralValueObject();
	                gVO.setAtributo("codMunicipio", expVO.getMunicipio());
	                gVO.setAtributo("codProcedimiento", expVO.getProcedimiento());
	                gVO.setAtributo("ejercicio", expVO.getEjercicio());
	                gVO.setAtributo("usuario", expVO.getUsuario());
	                gVO.setAtributo("codOrganizacion", expVO.getOrganizacionUsuario());
	                gVO.setAtributo("codEntidad", expVO.getEntidadUsuario());
	                //gVO.setAtributo("codUOR", expVO.getUor());
                        String unidadProcedimiento =determinarUnidadExpediente(
                                expVO.getMunicipio(), expVO.getProcedimiento(), params);
                        if (unidadProcedimiento.equals("") || unidadProcedimiento==null)
	                gVO.setAtributo("codUOR", expVO.getUor());
                        else gVO.setAtributo("codUOR", unidadProcedimiento);
                    // Se añade este campo debido a los cambios sufridos por
                    gVO.setAtributo("codUnidadTramitadoraUsu",expVO.getUor());
                gVO.setAtributo("unidadTramiteInicioSeleccionada", determinarUnidadTramite(
                        expVO.getMunicipio(), expVO.getProcedimiento(), params));
	                gVO.setAtributo("asunto", expVO.getAsunto());
                if (!"".equals(observaciones)) {
                        gVO.setAtributo("observaciones",observaciones);
                }
                    	                
                if (expVO.getTercero() != null) {
                        gVO.setAtributo("codTercero", expVO.getTercero().getCodigo());
	                    gVO.setAtributo("codDomicilio", expVO.getTercero().getCoddomicilio());
	                    gVO.setAtributo("version", expVO.getTercero().getVersion());
	                }

	                  TramitacionValueObject tvo = new TramitacionValueObject();
                if (expVO.getOrganizacionUsuario() != null) {
	                    user.setOrgCod(Integer.parseInt(expVO.getOrganizacionUsuario()));
                } else {
	                    user.setOrgCod(Integer.parseInt(expVO.getMunicipio()));
                }
	                  tvo.setCodProcedimiento(expVO.getProcedimiento());
	                  TramitacionManager.getInstance().getNuevoExpediente(user,tvo,params);

	                gVO.setAtributo("numero",tvo.getNumero());
                                  
                                  gVO.setAtributo(ConstantesDatos.ORIGEN_LLAMADA_NOMBRE_PARAMETRO, ConstantesDatos.ORIGEN_LLAMADA_WEB_SERVICE);
	                res = FichaExpedienteManager.getInstance().iniciarExpediente(gVO, params);
	                FichaExpedienteAction ficha = new FichaExpedienteAction();
	                if (res > 0) {
	                    TramiteVO traVO = new TramiteVO();

	                    traVO.setCamposFormularios(expVO.getCamposFormularios());
	                    traVO.setMunicipio(expVO.getMunicipio());
	                    traVO.setEjercicio(expVO.getEjercicio());
	                    traVO.setNumeroExpediente(tvo.getNumero());
	                    traVO.setCodTramite((String) gVO.getAtributo("codTramite"));
	                    traVO.setOcurrenciaTramite("1");
	                    traVO.setProcedimiento(expVO.getProcedimiento());

                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("COMPROBANDO CAMPOS DEL FORMULARIO");
                    }
	                    //grabar los datos suplementarios del expediente
                    if ((expVO.getCamposFormularios() != null) && (!expVO.getCamposFormularios().equals(""))) {
	                       Vector estructuraDSExpediente = FichaExpedienteDAO.getInstance().cargaEstructuraDatosSuplementarios(gVO, bd, con);
	                    }
	                    grabarCamposFormulario(expVO.getMunicipio(),expVO.getEjercicio(), tvo.getNumero(),
	                          expVO.getCamposFormularios(), expVO.getProcedimiento(),bd, con);

	                    //grabar los datos suplementarios del tramite de inicio
                    if ((traVO.getCamposFormularios() != null) && (!traVO.getCamposFormularios().equals(""))) {
	                           grabarCamposFormulario(traVO.getMunicipio(), traVO.getEjercicio(), traVO.getNumeroExpediente(),
	                                   traVO.getCodTramite(), traVO.getOcurrenciaTramite(),traVO.getProcedimiento(),
	                                   traVO.getCamposFormularios(), bd, con);

	                   }



                    /*Creación de datos y proceso de notificaciones por mail al inicio del expediente*/
                    EstructuraNotificacion eNotif = (EstructuraNotificacion) gVO.getAtributo("mailsUsuariosAlIniciar");
                    gVO.setAtributo("desProcedimiento", obtenerNombreProcedimiento(params, gVO));
                    Vector mailTercero = null;
                    if (expVO.getTercero() != null) {
                        InteresadoVO terVO = expVO.getTercero();
                        mailTercero = new Vector();
                        mailTercero.add(terVO.getEmail());
                    }
                    //eNotif.setListaEMailsInteresados(mailTercero);
                    gVO.setAtributo("mailsUsuariosAlIniciar", getMailsUsuariosAlIniciar(params,gVO));
                    if (notificarInteresadoInicioExpediente(params, expVO.getMunicipio(), expVO.getProcedimiento(),
                            (String) gVO.getAtributo("codTramite"))) {
                        gVO.setAtributo("notificarInteresados", "si");
                    } else {
                        gVO.setAtributo("notificarInteresados", "no");
                    }
	                    ficha.notificar(user, gVO);
	                }
	                commitTransaction(bd, con);

	                salida.setResultado(Boolean.TRUE);
	                salida.setNumExpediente(tvo.getNumero());
	                salida.setCodigoTramite((String) gVO.getAtributo("codTramite"));
	                salida.setOcurrenciaTramite("1");
	                return salida;
	            } else {
	                mns[0] = "No existe el usuario";
	                salida.setIncidencias(mns[0]);
	                salida.setResultado(Boolean.FALSE);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("NO EXISTE EL USUARIO");
                }
	                return salida;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            rollBackTransaction(bd, con, e);
	            mns[0] = e.getMessage();
	            salida.setIncidencias(mns[0]);
	            salida.setResultado(Boolean.FALSE);
	            return salida;
	        } finally{
	            try{
	                bd.devolverConexion(con);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
	        }

	    }

    /**
     * Extrae las observaciones del xml con los datos enviados del formulario si los hay
     * @param datos: xml con los datos del formulario
     * @return Un String
     */
    private String extraerObservacionesCamposFormulario(String datos){
        String observaciones = "";

        try{
            m_Log.debug("extraerObservacionesCamposFormulario datos: " + datos);
            ByteArrayInputStream b = new ByteArrayInputStream(datos.getBytes());

            DocumentBuilder doc = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document docXML = doc.parse(b);

            NodeList nl = docXML.getDocumentElement().getElementsByTagName("dato");
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if(node!=null){
                    String codigo = node.getAttributes().getNamedItem("codigo").getNodeValue();
                    String valor = node.getAttributes().getNamedItem("valor").getNodeValue();
                    // Si existe el nodo observaciones se devuelve su valor
                    if("observaciones".equals(codigo.toLowerCase())){
                        observaciones = valor;
                        break;
                    }
                }// if
            }// for
        
        } catch (Exception e) {
            m_Log.error("error: " + e.getMessage());
        }

        m_Log.debug("observaciones obtenidas de los campos del formulario: " + observaciones);
        return observaciones;

    }


    /*Determinamos si, existiendo un interesado en el servicio web, se le debe notificar del inicio
     del expediente.*/
    private boolean notificarInteresadoInicioExpediente(String[] params, String municipio, String procedimiento, String tramite) {

        PreparedStatement st = null;
        ResultSet rs = null;
        AdaptadorSQL oad=null;
        Connection con=null;


        try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();
            String sql = "SELECT TRA_INI FROM e_tra WHERE TRA_MUN=" + municipio +
                    " AND TRA_PRO=" + oad.addString(procedimiento) + " AND TRA_COD=" + tramite;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                String ini = rs.getString("TRA_INI");
                if (ini.equals("S")) {
                    return true;
                } else {
                    return false;
                }
            }
            rs.close();
            st.close();


        } catch (BDException ex) {
            Logger.getLogger(WSExpedienteImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(WSExpedienteImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }
        return false;
    }

	       private void rollBackTransaction(AdaptadorSQLBD bd,Connection con, Exception e) {
			try {
				bd.rollBack(con);
	            bd.devolverConexion(con);
	        } catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				e.printStackTrace();
				m_Log.error(e.getMessage());
			}
		}

		private void commitTransaction(AdaptadorSQLBD bd, Connection con) {
			try {
				bd.finTransaccion(con);
				bd.devolverConexion(con);
			} catch (Exception ex) {
				ex.printStackTrace();
	            m_Log.error("SQLException: " + ex.getMessage());
			}
		}

	      private void grabarCamposFormulario(String municipio, String ejercicio, String numeroExpediente, String campos,
	                                          String procedimiento, AdaptadorSQLBD bd, Connection con)
	           throws TechnicalException, SQLException, IOException, SAXException,Exception {

	         Statement stm = null;
	         String sql = null;
	         int res = 0;


	         ByteArrayInputStream b = new ByteArrayInputStream(campos.getBytes());

	         DocumentBuilder doc = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	         Document docXML = doc.parse(b);

	            NodeList nl = docXML.getDocumentElement().getElementsByTagName("dato");
	            ExpedienteDAO e = new ExpedienteDAO();
	           Vector lista = e.cargaEstructuraDatosSuplementarios1(municipio,procedimiento,bd, con);
	            for (int i=0;i<nl.getLength();i++){
	                Node node = nl.item(i);
	                String codigo = node.getAttributes().getNamedItem("codigo").getNodeValue();
	                String valor = node.getAttributes().getNamedItem("valor").getNodeValue();
	                EstructuraCampo campo = obtenerCampo(lista,codigo);
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
	                        m_Log.debug("campo.getMascara="+campo.getMascara());
                if ((campo.getMascara() != null) && (!"".equals(campo.getMascara()))) {
	                             mascara = campo.getMascara();
                }
	                        grabarCampo(municipio,ejercicio,numeroExpediente,campo.getCodCampo(),valor,tipo,mascara, procedimiento,con);
	                }
	            }
	     }

	    private int grabarCampo(String municipio, String ejercicio, String numeroExpediente,
	                            String codigo, String valor, String tipo,String mascara, String procedimiento, Connection con)
	      throws SQLException{
	           int res = 0;
	            ExpedienteDAO expediente = new ExpedienteDAO();

	           if ("0".equals(tipo)) {
	               res = grabarCampo(municipio, ejercicio,numeroExpediente, codigo, valor,
	                       expediente.getTipoCampo(municipio, procedimiento, codigo, con) ,mascara, procedimiento, con);
	           } else if ("1".equals(tipo)) {
	               res = expediente.setDatoNumerico(municipio,ejercicio, numeroExpediente, codigo, valor, con);
	           } else if("2".equals(tipo)) {
	               res = expediente.setDatoTexto(municipio,ejercicio, numeroExpediente, codigo, valor, con);
	           } else if("3".equals(tipo)) {
	               res = expediente.setDatoFecha(municipio,ejercicio, numeroExpediente, codigo, valor, mascara, con);
	           } else if("4".equals(tipo)) {
	               res = expediente.setDatoTextoLargo(municipio,ejercicio, numeroExpediente, codigo, valor, con);
	           }else if("6".equals(tipo)) {
	               res = expediente.setDatoDesplegable(municipio,ejercicio, numeroExpediente, codigo, valor, con);
	           }
        if (m_Log.isDebugEnabled()) {
            m_Log.debug("el resultado de grabar los valores de datos suplementarios es : " + res);
        }

	              return res;
	      }

	     /* Buscar en la lista el campo cuyo codigo=valor
	     *  y que este activo
	     */
	     private EstructuraCampo obtenerCampo (Vector lista, String valor){

	         for (int i=0;i<lista.size();i++){
	            EstructuraCampo campo = (EstructuraCampo) lista.get(i);
	             if (campo.getCodCampo().equalsIgnoreCase(valor)){
                if ("SI".equals(campo.getActivo())) {
	                    return campo;
	             }                
	         }
        }
	         return null;
	     }

	    //grabar los datos suplementarios del tramite de inicio
	    private void grabarCamposFormulario(String municipio, String ejercicio, String numeroExpediente,String tramite,
	                                        String ocurrencia, String procedimiento, String campos, AdaptadorSQLBD bd,
	                                        Connection con)
	              throws TechnicalException,SQLException, IOException {

	          Statement stm = null;
	          String sql = null;
	          int res = 0;

	          ByteArrayInputStream b = new ByteArrayInputStream(campos.getBytes());
	          try{
	              DocumentBuilder doc = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	              Document docXML = doc.parse(b);
	              NodeList nl = docXML.getDocumentElement().getElementsByTagName("dato");
	              ExpedienteDAO e = new ExpedienteDAO();
	              Vector lista = e.cargaEstructuraDatosSuplementarios2(municipio,procedimiento,numeroExpediente,tramite, bd, con);
	              for (int i=0;i<nl.getLength();i++){
	                 Node node = nl.item(i);

                if (node == null) {
                         m_Log.debug("grabarCamposFormulario node ==null");
                } else {
                        m_Log.debug("grabarCamposFormulario node !=null");
                }

	                 String codigo = node.getAttributes().getNamedItem("codigo").getNodeValue();
	                 String valor = node.getAttributes().getNamedItem("valor").getNodeValue();
                     m_Log.debug("********************************");
                     m_Log.debug("grabarCamposFormulario codigo: " + codigo);
                     m_Log.debug("grabarCamposFormulario valor: " + valor);

	                 EstructuraCampo campo = obtenerCampo(lista,codigo);
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Grabando campo tramite " + codigo);
                }
	                 if (campo != null) {
                    if (m_Log.isDebugEnabled()) {
                        m_Log.debug("... " + campo.getCodCampo());
                    }
	                     String tipo = "0";
                    if ((campo.getCodTipoDato() != null) && (!"".equals(campo.getCodTipoDato()))) {
	                         tipo = campo.getCodTipoDato();
                    }

	                     String mascara = null;
	                     m_Log.debug("campo.getMascara="+campo.getMascara());
                    if ((campo.getMascara() != null) && (!"".equals(campo.getMascara()))) {
	                          mascara = campo.getMascara();
                    }

	                 grabarCampo(municipio,ejercicio,numeroExpediente,ocurrencia, procedimiento,
	                             campo.getCodCampo(),valor,tipo,mascara,tramite,con);
	                 }
	             }
	          }catch(ParserConfigurationException e){
	              e.printStackTrace();
	              throw  new TechnicalException(e.getMessage());
	          }catch(SAXException sax){
	              sax.printStackTrace();
	              throw new TechnicalException(sax.getMessage());
	          }catch(Exception tec){
	              tec.printStackTrace();
	              throw new TechnicalException(tec.getMessage());
	          }

	      }

	      private int grabarCampo(String municipio, String ejercicio, String numeroExpediente, String ocurrencia,
	                              String procedimiento, String codigo, String valor, String tipo,String mascara,
	                              String numTramite, Connection con)
	              throws TechnicalException{
	           int res = 0;
	          TramiteDAO tramite = new TramiteDAO();

              m_Log.debug("WSExpedienteImplSoapBindingImpl tipo campo: " + tipo + ", valor: " + valor);

	          try{

	           if("1".equals(tipo)) {
	               res = tramite.setDatoNumerico(municipio,ejercicio, numeroExpediente,numTramite,ocurrencia, procedimiento,
	                       codigo, valor, con);
	           } else if("2".equals(tipo)) {
	               res = tramite.setDatoTexto(municipio,ejercicio, numeroExpediente,numTramite,ocurrencia,procedimiento,
	                       codigo, valor, con);
	           } else if("3".equals(tipo)) {
	               res = tramite.setDatoFecha(municipio,ejercicio, numeroExpediente,numTramite, ocurrencia, procedimiento,
	                       codigo, valor, mascara, con);
	           } else if("4".equals(tipo)) {
	               res = tramite.setDatoTextoLargo(municipio,ejercicio, numeroExpediente,numTramite, ocurrencia, procedimiento,
	                       codigo, valor, con);
	           }else if("6".equals(tipo)) {
                       res = tramite.setDatoDesplegable(municipio,ejercicio, numeroExpediente,numTramite, ocurrencia, procedimiento,
	                       codigo, valor, con);
	           }

            if (m_Log.isDebugEnabled()) {
                m_Log.debug("el resultado de grabar los valores de datos suplementarios es : " + res);
            }
	             }catch(Exception e){
	                 throw new TechnicalException(e.getMessage());
	             }
	              return res;
	      }

    /*Determinar unidad de inicio del trámite de inicio*/
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


    /*Determinar unidad de inicio del trámite de inicio*/
    private String determinarUnidadExpediente(String municipio, String procedimiento,
            String[] params) {

        PreparedStatement st_tra = null;
        ResultSet rs_tra = null, rs_utr = null;
        AdaptadorSQL oad = null;
        Connection con = null;
        String uors ="";
        String resultado = "";
        String uorInicia="";
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
                sql_tra="SELECT pui_cod FROM E_Pui WHERE Pui_pro = ? AND  Pui_MUN = ?";
            con = oad.getConnection();
            st_tra = con.prepareStatement(sql_tra);
            st_tra.setString(1, procedimiento);
            st_tra.setString(2, municipio);
            rs_tra = st_tra.executeQuery();
            if (rs_tra.next()) uorInicia = rs_tra.getString("PUI_COD");
            }
            return uorInicia;

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


        private String obtenerNombreProcedimiento(String[] params, GeneralValueObject gVO) {
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
            st.setString(1, (String) gVO.getAtributo("codProcedimiento"));
            st.setInt(2, Integer.valueOf((String) gVO.getAtributo("codMunicipio")));
            rs = st.executeQuery();
            m_Log.debug("Consulta nombre procedimiento: " + sql);
            if (rs.next()) {
                return ((String) rs.getString("PRO_DES"));
            }

        } catch (BDException ex) {
            Logger.getLogger(WSExpedienteImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(WSExpedienteImplSoapBindingImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oad.devolverConexion(con);
            } catch (BDException be) {
                be.printStackTrace();
            }
        }
        return "";
    }



        private EstructuraNotificacion getMailsUsuariosAlIniciar(String[] params, GeneralValueObject gVO) {

        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = null;
        int resultado = 0;
        EstructuraNotificacion eNotif = new EstructuraNotificacion();
        AdaptadorSQL oad = null;
        Connection con = null;

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
            int procedimientoRestringido=0;


            sql = "SELECT CRO_UTR FROM E_CRO WHERE CRO_TRA=? AND CRO_NUM=? AND CRO_EJE=? " +
                    " AND CRO_PRO=? AND CRO_MUN=? AND CRO_FEF IS NULL";
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
            sql = "SELECT TRA_UTI,TRA_USI,TRA_INI,pro_restringido" +
                    " FROM e_tra, e_pro WHERE TRA_MUN=" + codMunicipio + " AND TRA_PRO=" + oad.addString(codProcedimiento) +
                    " AND TRA_COD=" + codTramite +" AND tra_pro=pro_cod and tra_mun=pro_mun";
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

                sql = "SELECT TML_VALOR" +
                        " FROM e_tml WHERE TML_MUN=" + codMunicipio + " AND TML_PRO=" + oad.addString(codProcedimiento) +
                        " AND TML_TRA=" + codTramite;
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
                    if (uorDTO!=null)	
                        if (uorDTO.getUor_email()!=null && !uorDTO.getUor_email().equals("")) {	
                            if(m_Log.isDebugEnabled()) m_Log.debug("UNIDAD TRAMITADORA MAIL: "+uor_mail);
                            mailsUOR.addElement(uor_mail);
                        }
                    
                }
                if (usi.equals("S")) {
                    //coger usuarios de a_uou buscando por uor
                    if (procedimientoRestringido == 0) {//El procedimiento no es restringido   
                        sql = "SELECT UOU_USU FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou WHERE "
                                + "UOU_UOR=" + codUORTramiteIniciado + " AND " + "UOU_ORG=" + codOrganizacion + " AND UOU_USU<>" + codUsuario;

                    } else {

                        sql = "SELECT UOU_USU FROM " + GlobalNames.ESQUEMA_GENERICO + "a_uou a_uou ," + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE "
                                + "UOU_UOR=" + codUORTramiteIniciado + " AND " + "UOU_ORG=" + codOrganizacion + " AND UOU_USU<>" + codUsuario + " AND USUARIO_PROC_RESTRINGIDO.PRO_COD='" + codProcedimiento
                                + "' AND USUARIO_PROC_RESTRINGIDO.ORG_COD=" + codOrganizacion
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
                        sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "a_usu a_usu WHERE " +
                                "USU_COD=" + usuarios.elementAt(i);
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
        return eNotif;
    }

	    }


