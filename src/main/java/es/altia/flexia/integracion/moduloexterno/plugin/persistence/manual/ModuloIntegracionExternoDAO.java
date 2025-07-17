package es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.sge.persistence.DatosSuplementariosManager;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.historico.expedientes.dao.ExpedienteDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoDesplegableModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.*;
import es.altia.flexia.integracion.moduloexterno.plugin.util.UtilitiesModuloIntegracion;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.MimeTypes;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.log4j.Logger;


public class ModuloIntegracionExternoDAO {
    private static ModuloIntegracionExternoDAO instance = null;
    private Logger log = Logger.getLogger(ModuloIntegracionExternoDAO.class);

    private ModuloIntegracionExternoDAO(){
    }
    
    public static ModuloIntegracionExternoDAO getInstance(){
        if(instance==null)
            instance = new ModuloIntegracionExternoDAO();

        return instance;
    }


   /**
     * Recupera un campo suplementario a nivel de expediente
     * @param codMunicipio: Código del municipio
     * @param ejercicio: Ejercicio
     * @param numExpediente: Número del expediente
     * @param codCampo: Código del campo
     * @param tipoCampo: Tipo de campo. Los valores que toma son los de los atributos públicos y estáticos de la clase ModuloIntegracionExternoCampoSuplementarioFlexia
     * @param con: Conexión a la base de datos
     * @return CampoSuplementarioVO
     */
    public SalidaIntegracionVO getCampoSuplementarioExpediente(String codMunicipio,String ejercicio,String numExpediente,String codProcedimiento,String codCampo,int tipoCampo,Connection con,String[] params){
        CampoSuplementarioModuloIntegracionVO campo = null;
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        Statement st = null;
        ResultSet rs = null;
                
        try{
            String sql = "";
            
            boolean expHistorico = (ExpedienteDAO.getInstance().getExpediente(Integer.parseInt(codMunicipio),
                Integer.parseInt(ejercicio),numExpediente,con) == null)?true:false;
            
            switch(tipoCampo){

                case ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO:{
                    // Campo suplementario numérico
                    if (expHistorico)
                        sql = "SELECT TNU_VALOR,PCA_ROT,PCA_DES FROM E_PCA,HIST_E_TNU,E_TDA ";
                    else
                        sql = "SELECT TNU_VALOR,PCA_ROT,PCA_DES FROM E_PCA,E_TNU,E_TDA ";
                    
                    sql += "WHERE TNU_MUN=PCA_MUN AND TNU_COD=PCA_COD AND PCA_TDA = E_TDA.TDA_COD AND PCA_TDA=1 AND PCA_PRO='" + codProcedimiento + "' " + 
                          "AND TNU_MUN=" + codMunicipio + " AND TNU_EJE=" + ejercicio + " AND TNU_NUM='" + numExpediente + "' " +
                          "AND TNU_COD='" + codCampo + "' AND PCA_ACTIVO='SI'";                    
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO:{
                    // Campo suplementario texto corto
                    if (expHistorico)
                        sql = "SELECT TXT_VALOR,PCA_ROT,PCA_DES FROM E_PCA,HIST_E_TXT,E_TDA ";
                    else
                        sql = "SELECT TXT_VALOR,PCA_ROT,PCA_DES FROM E_PCA,E_TXT,E_TDA ";

                    sql += "WHERE TXT_MUN=PCA_MUN AND TXT_COD=PCA_COD AND PCA_TDA = E_TDA.TDA_COD AND PCA_TDA=2 AND PCA_PRO='" + codProcedimiento + "' " + 
                           "AND TXT_MUN=" + codMunicipio + " AND TXT_EJE=" + ejercicio + " AND TXT_NUM='" + numExpediente + "' " +
                           "AND TXT_COD='" + codCampo + "' AND PCA_ACTIVO='SI'";
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO:{
                    // Campo suplementario texto largo
                    if (expHistorico)
                        sql = "SELECT TTL_VALOR,PCA_ROT,PCA_DES FROM E_PCA,HIST_E_TTL,E_TDA ";
                    else
                        sql = "SELECT TTL_VALOR,PCA_ROT,PCA_DES FROM E_PCA,E_TTL,E_TDA ";

                    sql += "WHERE TTL_MUN=PCA_MUN AND TTL_COD=PCA_COD AND PCA_TDA = E_TDA.TDA_COD AND PCA_TDA=4 AND PCA_PRO='" + codProcedimiento + "' " +
                           "AND TTL_MUN=" + codMunicipio + " AND TTL_EJE=" + ejercicio + " AND TTL_NUM='" + numExpediente + "' " +
                           "AND TTL_COD='" + codCampo + "' AND PCA_ACTIVO='SI'";
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA:{
                    // Campo suplementario fecha
                    if (expHistorico)
                        sql = "SELECT TFE_VALOR,PCA_ROT,PCA_DES FROM E_PCA,HIST_E_TFE,E_TDA ";
                    else
                        sql = "SELECT TFE_VALOR,PCA_ROT,PCA_DES FROM E_PCA,E_TFE,E_TDA ";
                   
                    sql += "WHERE TFE_MUN=PCA_MUN AND TFE_COD=PCA_COD AND PCA_TDA = E_TDA.TDA_COD AND PCA_TDA=3 AND PCA_PRO='" + codProcedimiento + "' " +
                           "AND TFE_MUN=" + codMunicipio + " AND TFE_EJE=" + ejercicio + " AND TFE_NUM='" + numExpediente + "' " +
                           "AND TFE_COD='" + codCampo + "' AND PCA_ACTIVO='SI'";
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO:{
                    // Campo suplementario fichero                    
                    if (expHistorico)
                        sql = "SELECT TFI_MIME,TFI_NOMFICH,PCA_ROT,PCA_DES FROM E_PCA,HIST_E_TFI ";
                    else
                        sql = "SELECT TFI_MIME,TFI_NOMFICH,PCA_ROT,PCA_DES FROM E_PCA,E_TFI ";
                        
                    sql += "WHERE TFI_MUN=PCA_MUN AND TFI_COD=PCA_COD AND PCA_TDA=5 AND PCA_PRO='" + codProcedimiento + "' " +
                          "AND TFI_MUN=" + codMunicipio + " AND TFI_EJE=" + ejercicio + " AND TFI_NUM='" + numExpediente + "' " +
                          "AND TFI_COD='" + codCampo + "' AND PCA_ACTIVO='SI'";
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE:{
                    // Campo suplementario desplegable
                    if (expHistorico)
                        sql = "SELECT TDE_VALOR,E_DES_VAL.DES_NOM,PCA_ROT,PCA_DES,PCA_DESPLEGABLE FROM E_PCA,HIST_E_TDE,E_TDA,E_DES_VAL ";
                    else
                        sql = "SELECT TDE_VALOR,E_DES_VAL.DES_NOM,PCA_ROT,PCA_DES,PCA_DESPLEGABLE FROM E_PCA,E_TDE,E_TDA,E_DES_VAL ";
                    
                    sql += "WHERE TDE_MUN=PCA_MUN AND TDE_COD=PCA_COD AND PCA_TDA = E_TDA.TDA_COD AND PCA_TDA=6 AND PCA_PRO='" + codProcedimiento + "' " + 
                          "AND TDE_MUN=" + codMunicipio + " AND TDE_EJE=" + ejercicio + " AND TDE_NUM='" + numExpediente + "' " +
                          "AND TDE_COD='" + codCampo + "' AND PCA_ACTIVO='SI' AND PCA_DESPLEGABLE=E_DES_VAL.DES_COD AND E_DES_VAL.DES_VAL_COD=TDE_VALOR";
                    break;
                }
            }// switch
            
            log.debug("sql: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                campo = new CampoSuplementarioModuloIntegracionVO();
                // Fijo
                String rotulo      = rs.getString("PCA_ROT");
                String descripcion = rs.getString("PCA_DES");
                // Variable
                if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO){
                    campo.setValorNumero(rs.getString("TNU_VALOR"));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                }
                else
                if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO){
                    campo.setValorTexto(rs.getString("TXT_VALOR"));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                }
                else
                if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA){
                    campo.setValorFecha(DateOperations.toCalendar(rs.getTimestamp("TFE_VALOR")));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                }
                else
                if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO){
                    try{
                        Reader reader = rs.getCharacterStream("TTL_VALOR");
                        char[] cbuf = new char[65536];
                        StringBuffer stringbuf = new StringBuffer();
                        while (reader.read(cbuf,0,65536)!=-1) {
                            stringbuf.append(cbuf);
                        }//end while

                        campo.setValorTexto(stringbuf.toString().trim());                        
                        campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }else
                if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE){
                    //campo.setValorTexto(rs.getString("DES_NOM"));
                    campo.setValorDesplegable(rs.getString("TDE_VALOR"));
                    campo.setCodigoDesplegable(rs.getString("PCA_DESPLEGABLE"));
                    campo.setDescripcionValorDesplegable(rs.getString("DES_NOM"));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
                }else
                if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO){
                    byte[] contenido = null;

                    String nombreFichero = "";
                    String tipoMime = "";
                    
                    try{
                        nombreFichero = rs.getString("TFI_NOMFICH");
                        tipoMime = rs.getString("TFI_MIME");                        
    
                        Hashtable<String,Object> datos = new Hashtable<String,Object>();
                        datos.put("numeroDocumento",codCampo);              
                        datos.put("codTipoDato",codCampo);
                        datos.put("codMunicipio",codMunicipio);
                        datos.put("ejercicio",ejercicio);
                        datos.put("numeroExpediente",numExpediente);              
                        datos.put("params",params);
                        
                        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
                        Documento doc = null;
                        int tipoDocumento = -1;
                        if(!almacen.isPluginGestor())
                             tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                        else{
                            //PENDIENTE IMPLEMENTACION - GESTOR ALFRESCO
                            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                            
                            //  Si se trata de un plugin de un gestor documental, se pasa la información extra necesaria
                            ResourceBundle config = ResourceBundle.getBundle("documentos");                            
                            String carpetaRaiz       = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                            GeneralValueObject gVO = DatosSuplementariosManager.getInstance().getInfoCampoSuplementarioFicheroExpediente(Integer.parseInt(codMunicipio),codCampo,numExpediente,expHistorico,params);              
                            String descripcionOrganizacion = (String)gVO.getAtributo("descOrganizacion");
                            String descProcedimiento = (String)gVO.getAtributo("descProcedimiento");
                            nombreFichero = (String)gVO.getAtributo("nombreFichero");
                            
                            datos.put("tipoMime",tipoMime);                    
                            datos.put("nombreDocumento", codCampo + "_" + nombreFichero);
                            datos.put("nombreFicheroCompleto", codCampo + "_" + nombreFichero);

                            ArrayList<String> listaCarpetas = new ArrayList<String>();
                            listaCarpetas.add(carpetaRaiz);
                            listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                            listaCarpetas.add(numExpediente.replaceAll("/","-"));
                            datos.put("listaCarpetas",listaCarpetas);
                        }

                        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                        doc.setExpHistorico(expHistorico);
                        doc = almacen.getDocumentoDatosSuplementarios(doc);

                        contenido = doc.getFichero();

                        if (log.isDebugEnabled()) {
                            log.debug("Logitud del fichero: " + contenido.length);
                            log.debug("Nombre del fichero: " + nombreFichero);
                            log.debug("Tipo contenido fichero: " + tipoMime);
                        }

                    }  catch(AlmacenDocumentoTramitacionException e){
                        e.printStackTrace();
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    campo.setValorFichero(contenido);
                    campo.setNombreFichero(nombreFichero);
                    campo.setTipoMimeFichero(tipoMime);
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO);
                }

                campo.setCodOrganizacion(codMunicipio);
                campo.setCodigoCampo(codCampo);
                campo.setCodProcedimiento(codProcedimiento);
                campo.setNumExpediente(numExpediente);
                campo.setDescripcionCampo(descripcion);
                campo.setRotuloCampo(rotulo);                
                campo.setTramite(false);                
                salida.setCampoSuplementario(campo);
                salida.setStatus(0);
                salida.setDescStatus("OK");                
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);

                log.debug("El campo suplementario es: " + salida.getCampoSuplementario());
            }
        }catch(SQLException e){
            e.printStackTrace();       
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(1);
            salida.setDescStatus("ERROR AL RECUPERAR EL CAMPO SUPLEMENTARIO DE BASE DE DATOS");
            salida.setCampoDesplegable(null);
        }catch(Exception e){
            e.printStackTrace();       
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(1);
            salida.setDescStatus("ERROR AL RECUPERAR EL CAMPO SUPLEMENTARIO DE BASE DE DATOS");
            salida.setCampoDesplegable(null);
        }finally{
            try{                
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        if(campo==null){            
            salida = new SalidaIntegracionVO();
            salida.setStatus(2);
            salida.setDescStatus("EL CAMPO SUPLEMENTARIO NO TIENE VALOR");
            salida.setCampoSuplementario(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
        }
        
        return salida;
    }

    public SalidaIntegracionVO getCampoSuplementarioTercero(String codMunicipio, Integer codTercero, String codCampo, int tipoCampo, Connection con){
        if(log.isDebugEnabled()) log.debug("getCampoSuplementarioTercero() : BEGIN");
        CampoSuplementarioTerceroModuloIntegracionVO campo = null;
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "";
                      
            switch(tipoCampo){
                case ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO:{
                    sql = "Select T_CAMPOS_NUMERICO.valor, T_CAMPOS_EXTRA.TIPO_DATO, T_CAMPOS_EXTRA.DESCRIPCION, T_CAMPOS_EXTRA.ROTULO " +
                            "From T_CAMPOS_EXTRA, T_CAMPOS_NUMERICO " +
                            "Where T_CAMPOS_EXTRA.COD_CAMPO = T_CAMPOS_NUMERICO.COD_CAMPO " +
                            "And T_CAMPOS_EXTRA.ACTIVO = 'SI' " +
                            "And T_CAMPOS_NUMERICO.COD_CAMPO = '" + codCampo + "' " +
                            "And T_CAMPOS_NUMERICO.COD_MUNICIPIO = " + codMunicipio + " " +
                            "And T_CAMPOS_NUMERICO.COD_TERCERO = " + codTercero + " ";
                    break;
                }//case ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO
                case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO:{
                    sql = "Select T_CAMPOS_TEXTO.valor, T_CAMPOS_EXTRA.TIPO_DATO, T_CAMPOS_EXTRA.DESCRIPCION, T_CAMPOS_EXTRA.ROTULO " +
                            "From T_CAMPOS_EXTRA, T_CAMPOS_TEXTO " +
                            "Where T_CAMPOS_EXTRA.COD_CAMPO = T_CAMPOS_TEXTO.COD_CAMPO " +
                            "And T_CAMPOS_EXTRA.ACTIVO = 'SI' " +
                            "And T_CAMPOS_TEXTO.COD_CAMPO = '" + codCampo + "' " +
                            "And T_CAMPOS_TEXTO.COD_MUNICIPIO = " + codMunicipio + " " +
                            "And T_CAMPOS_TEXTO.COD_TERCERO = " + codTercero + " ";
                    break;
                }//case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO
                case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO:{
                    sql = "Select T_CAMPOS_TEXTO_LARGO.valor, T_CAMPOS_EXTRA.TIPO_DATO, T_CAMPOS_EXTRA.DESCRIPCION, T_CAMPOS_EXTRA.ROTULO " +
                            "From T_CAMPOS_EXTRA, T_CAMPOS_TEXTO_LARGO " +
                            "Where T_CAMPOS_EXTRA.COD_CAMPO = T_CAMPOS_TEXTO_LARGO.COD_CAMPO " +
                            "And T_CAMPOS_EXTRA.ACTIVO = 'SI' " +
                            "And T_CAMPOS_TEXTO_LARGO.COD_CAMPO = '" + codCampo + "' " +
                            "And T_CAMPOS_TEXTO_LARGO.COD_MUNICIPIO = " + codMunicipio + " " +
                            "And T_CAMPOS_TEXTO_LARGO.COD_TERCERO = " + codTercero + " ";
                    break;
                }//case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO
                case ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA:{
                    sql = "Select T_CAMPOS_FECHA.valor, T_CAMPOS_EXTRA.TIPO_DATO, T_CAMPOS_EXTRA.DESCRIPCION, T_CAMPOS_EXTRA.ROTULO " +
                            "From T_CAMPOS_EXTRA, T_CAMPOS_FECHA " +
                            "Where T_CAMPOS_EXTRA.COD_CAMPO = T_CAMPOS_FECHA.COD_CAMPO " +
                            "And T_CAMPOS_EXTRA.ACTIVO = 'SI' " +
                            "And T_CAMPOS_FECHA.COD_CAMPO = '" + codCampo + "' " +
                            "And T_CAMPOS_FECHA.COD_MUNICIPIO = " + codMunicipio + " " +
                            "And T_CAMPOS_FECHA.COD_TERCERO = " + codTercero + " ";
                    break;
                }//case ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA
                case ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO:{
                    sql = "Select T_CAMPOS_FICHERO.valor, T_CAMPOS_FICHERO.TIPO_MIME, T_CAMPOS_FICHERO.NOMBRE_FICHERO, T_CAMPOS_EXTRA.TIPO_DATO" +
                            ", T_CAMPOS_EXTRA.DESCRIPCION, T_CAMPOS_EXTRA.ROTULO " +
                            "From T_CAMPOS_EXTRA, T_CAMPOS_FICHERO " +
                            "Where T_CAMPOS_EXTRA.COD_CAMPO = T_CAMPOS_FICHERO.COD_CAMPO " +
                            "And T_CAMPOS_EXTRA.ACTIVO = 'SI' " +
                            "And T_CAMPOS_FICHERO.COD_CAMPO = '" + codCampo + "' " +
                            "And T_CAMPOS_FICHERO.COD_MUNICIPIO = " + codMunicipio + " " +
                            "And T_CAMPOS_FICHERO.COD_TERCERO = " + codTercero + " ";
                    break;
                }//case ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO
                case ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE:{
                    sql = "Select T_CAMPOS_DESPLEGABLE.valor, T_CAMPOS_EXTRA.TIPO_DATO, T_CAMPOS_EXTRA.DESCRIPCION, T_CAMPOS_EXTRA.ROTULO " +
                            "From T_CAMPOS_EXTRA, T_CAMPOS_DESPLEGABLE " +
                            "Where T_CAMPOS_EXTRA.COD_CAMPO = T_CAMPOS_DESPLEGABLE.COD_CAMPO " +
                            "And T_CAMPOS_EXTRA.ACTIVO = 'SI' " +
                            "And T_CAMPOS_DESPLEGABLE.COD_CAMPO = '" + codCampo + "' " +
                            "And T_CAMPOS_DESPLEGABLE.COD_MUNICIPIO = " + codMunicipio + " " +
                            "And T_CAMPOS_DESPLEGABLE.COD_TERCERO = " + codTercero + " ";
                    break;
                }//case ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE
            }//switch(tipoCampo)
            
            log.debug("sql: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                campo = new CampoSuplementarioTerceroModuloIntegracionVO();
                
                String descripcion = rs.getString("DESCRIPCION");
                String rotulo = rs.getString("ROTULO");
                
                if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO){
                    campo.setValorNumero(rs.getString("valor"));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                }else if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO){
                    campo.setValorTexto(rs.getString("valor"));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                }else if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO){
                    try{
                        Reader reader = rs.getCharacterStream("valor");
                        char[] cbuf = new char[65536];
                        StringBuffer stringbuf = new StringBuffer();
                        while (reader.read(cbuf,0,65536)!=-1) {
                            stringbuf.append(cbuf);
                        }//end while

                        campo.setValorTexto(stringbuf.toString());                        
                        campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO);
                    }catch(IOException e){
                        log.error("Se ha producido un error leyendo el tipo de campo texto largo");
                    }//try-catch
                }else if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA){
                    campo.setValorFecha(DateOperations.toCalendar(rs.getTimestamp("valor")));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                }else if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO){
                    byte[] contenido = null;

                    String nombreFichero = "";
                    String tipoMime = "";
                    try{
                        nombreFichero = rs.getString("NOMBRE_FICHERO");
                        tipoMime      = rs.getString("TIPO_MIME");                        
                        contenido = this.getValorFicheroSuplementarioTercero(codMunicipio, codCampo, codTercero, con);
                    }catch(Exception e){
                        log.error("Se ha producido un error recuperando el campo suplementario de tipo fichero del tercero " + e.getMessage());
                    }//try-catch

                    campo.setValorFichero(contenido);
                    campo.setNombreFichero(nombreFichero);
                    campo.setTipoMimeFichero(tipoMime);
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO);
                }else if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE){
                    campo.setValorDesplegable(rs.getString("valor"));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
                }//if(tipoCampo)
                
                campo.setCodOrganizacion(codMunicipio);
                campo.setCodigoCampo(codCampo);
                campo.setDescripcionCampo(descripcion);
                campo.setRotuloCampo(rotulo);                
                salida.setStatus(0);
                salida.setDescStatus("OK");                
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setCampoSuplementarioTercero(campo);

                log.debug("El campo suplementario es: " + salida.getCampoSuplementario());
            }//while(rs.next())
        }catch(SQLException e){
            log.error("Se ha producido un error recuperando el campo suplementario del tercero " + e.getMessage());
            e.printStackTrace();       
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(1);
            salida.setDescStatus("ERROR AL RECUPERAR EL CAMPO SUPLEMENTARIO DE BASE DE DATOS");
            salida.setCampoDesplegable(null);
        }finally{
            try{                
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                log.error("Se ha producido un error cerrando el statement o el resultset " + e.getMessage());
                e.printStackTrace();
            }//try-catch
        }//try-catch-finally

        if(campo==null){            
            if(log.isDebugEnabled()) log.debug("El campo suplementario del tercero no tiene valor");
            salida = new SalidaIntegracionVO();
            salida.setStatus(2);
            salida.setDescStatus("EL CAMPO SUPLEMENTARIO NO TIENE VALOR");
            salida.setCampoSuplementario(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
        }//if(campo==null)
        if(log.isDebugEnabled()) log.debug("getCampoSuplementarioTercero() : END");
        return salida;
    }//getCampoSuplementarioTercero

    private byte[] getValorFicheroSuplementarioTercero(String codMunicipio, String codCampo, Integer codTercero, Connection con){
        if(log.isDebugEnabled()) log.debug("getValorFicheroSuplementarioTercero() : BEGIN");
        byte[] salida = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = null;

            sql = "Select T_CAMPOS_FICHERO.valor " + "T_CAMPOS_EXTRA.ROTULO " + "T_CAMPOS_FICHERO " +
                            "Where T_CAMPOS_FICHERO.COD_CAMPO = '" + codCampo + "' " +
                            "And T_CAMPOS_FICHERO.COD_MUNICIPIO = " + codMunicipio + " " +
                            "And T_CAMPOS_FICHERO.COD_TERCERO = " + codTercero + " ";

            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                try{
                    java.io.InputStream ist = rs.getBinaryStream("valor");
                    java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                    int c;
                    while ((c = ist.read())!= -1){
                        ot.write(c);
                    }//while ((c = ist.read())!= -1)
                    ot.flush();
                    salida = ot.toByteArray();
                    ot.close();
                    ist.close();
                }catch(Exception e){
                    log.error("Se ha producido un error recuperando el fichero del campo suplementario de tercero " + e.getMessage());
                }
            }// whil
        }catch(SQLException e){
            log.error("Se ha producido un error recuperando el fichero del campo suplementario de tercero " + e.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getValorFicheroSuplementarioTercero() : END");        
        return salida;
    }//getValorFicheroSuplementarioTercero


    public SalidaIntegracionVO getCampoSuplementarioTramite(String codMunicipio,String ejercicio,String numExpediente,String codProcedimiento,String codCampo,int codTramite,int ocurrenciaTramite,int tipoCampo,Connection con,String[] params){
        CampoSuplementarioModuloIntegracionVO campo = null;
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        Statement st = null;
        ResultSet rs = null;

        try{
            String sql = "";
            String orderBy="";
            boolean expHistorico = (ExpedienteDAO.getInstance().getExpediente(Integer.parseInt(codMunicipio),
                Integer.parseInt(ejercicio),numExpediente,con) == null)?true:false;

            switch(tipoCampo){

                case ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO:{
                    // Campo suplementario numérico
                    if (expHistorico)
                        sql = "SELECT TNUT_VALOR,TCA_ROT,TCA_DES FROM E_TCA,HIST_E_TNUT ";
                    else
                        sql = "SELECT TNUT_VALOR,TCA_ROT,TCA_DES FROM E_TCA,E_TNUT ";
                    
                    sql += "WHERE TNUT_MUN=TCA_MUN AND TNUT_PRO=TCA_PRO AND TNUT_TRA=TCA_TRA AND TNUT_COD=TCA_COD AND TCA_TDA=1 " +
                          "AND TNUT_MUN=" + codMunicipio + " AND TNUT_EJE=" + ejercicio + " AND TNUT_NUM='" + numExpediente + "' " +
                          "AND TNUT_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_PRO='" + codProcedimiento + "' " +
                          "AND TNUT_TRA=" + codTramite ;
                    if(ocurrenciaTramite>0){
                        sql=sql+ " AND TNUT_OCU=" + ocurrenciaTramite;
                        
                    } else orderBy=" ORDER BY TNUT_OCU DESC";
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO:{
                    // Campo suplementario texto corto
                    if (expHistorico)
                        sql = "SELECT TXTT_VALOR,TCA_ROT,TCA_DES FROM E_TCA,HIST_E_TXTT ";
                    else
                        sql = "SELECT TXTT_VALOR,TCA_ROT,TCA_DES FROM E_TCA,E_TXTT ";
                    
                    sql += "WHERE TXTT_MUN=TCA_MUN AND TXTT_PRO=TCA_PRO AND TCA_TRA=TXTT_TRA AND TXTT_COD=TCA_COD AND TCA_TDA=2 " +
                           "AND TXTT_MUN=" + codMunicipio + " AND TXTT_PRO='"  + codProcedimiento +  "' AND TXTT_EJE=" + ejercicio + " AND TXTT_NUM='" + numExpediente + "' " +
                           "AND TXTT_TRA=" + codTramite ;
                    if(ocurrenciaTramite>0){
                       sql=sql+ " AND TXTT_OCU=" + ocurrenciaTramite;
                      
                    }else  orderBy=" ORDER BY TXTT_OCU DESC";
                    
                    sql=sql+ " AND TXTT_COD='" + codCampo + "' AND TCA_ACTIVO='SI'";
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO:{
                    // Campo suplementario texto largo
                    if (expHistorico)
                        sql = "SELECT TTLT_VALOR,TCA_ROT,TCA_DES FROM E_TCA,HIST_E_TTLT ";
                    else
                        sql = "SELECT TTLT_VALOR,TCA_ROT,TCA_DES FROM E_TCA,E_TTLT ";
                    
                    sql += "WHERE TTLT_MUN=TCA_MUN AND TTLT_PRO=TCA_PRO AND TTLT_TRA=TCA_TRA AND TTLT_COD=TCA_COD AND TCA_TDA=4 " +
                           "AND TTLT_MUN=" + codMunicipio + " AND TTLT_PRO='" + codProcedimiento + "' AND TTLT_EJE=" + ejercicio + " AND TTLT_NUM='" + numExpediente + "' " +
                           "AND TTLT_TRA=" + codTramite;
                    
                    if(ocurrenciaTramite>0){
                        sql=sql+ " AND TTLT_OCU=" + ocurrenciaTramite;
                        
                    }else orderBy=" ORDER BY TTLT_OCU DESC";
                    sql=sql+ " AND TTLT_COD='" + codCampo + "' AND TCA_ACTIVO='SI'";
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA:{
                    // Campo suplementario fecha
                    if (expHistorico)
                        sql = "SELECT TFET_VALOR,TCA_ROT,TCA_DES FROM E_TCA,HIST_E_TFET ";
                    else
                        sql = "SELECT TFET_VALOR,TCA_ROT,TCA_DES FROM E_TCA,E_TFET ";
                    
                    sql += "WHERE TFET_MUN=TCA_MUN AND TFET_PRO=TCA_PRO AND TFET_TRA=TCA_TRA AND TFET_COD=TCA_COD AND TCA_TDA=3 " +
                           "AND TFET_MUN=" + codMunicipio + " AND TFET_PRO='" + codProcedimiento +  "' AND TFET_EJE=" + ejercicio + " AND TFET_NUM='" + numExpediente + "' " +
                           "AND TFET_TRA=" + codTramite;
                    if(ocurrenciaTramite>0){
                    sql=sql+ " AND TFET_OCU=" + ocurrenciaTramite;
                    } else orderBy=" ORDER BY TFET_OCU DESC";
                    sql=sql+ " AND TFET_COD='" + codCampo + "' AND TCA_ACTIVO='SI'";
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO:{
                    // Campo suplementario fichero
                    if (expHistorico)
                        sql = "SELECT TFIT_NOMFICH,TFIT_MIME,TCA_ROT,TCA_DES FROM E_TCA,HIST_E_TFIT ";
                    else
                        sql = "SELECT TFIT_NOMFICH,TFIT_MIME,TCA_ROT,TCA_DES FROM E_TCA,E_TFIT ";

                    sql += "WHERE TFIT_MUN=TCA_MUN AND TFIT_PRO=TCA_PRO AND TFIT_TRA=TCA_TRA AND TFIT_COD=TCA_COD AND TCA_TDA=5 " +
                          "AND TFIT_MUN=" + codMunicipio + " AND TFIT_PRO='" + codProcedimiento +  "' AND TFIT_EJE=" + ejercicio + " AND TFIT_NUM='" + numExpediente + "' " +
                          "AND TFIT_TRA=" + codTramite;
                     if(ocurrenciaTramite>0){
                        sql=sql+ " AND TFIT_OCU=" + ocurrenciaTramite;
                     }else orderBy=" ORDER BY TFIT_OCU DESC";
                    sql=sql+  " AND TFIT_COD='" + codCampo + "' AND TCA_ACTIVO='SI'";
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE:{
                    // Campo suplementario desplegable
                    if (expHistorico)
                        sql = "SELECT TDET_VALOR,E_DES_VAL.DES_NOM,TCA_ROT,TCA_DES,TCA_DESPLEGABLE FROM E_TCA,HIST_E_TDET,E_DES_VAL ";
                    else
                        sql = "SELECT TDET_VALOR,E_DES_VAL.DES_NOM,TCA_ROT,TCA_DES,TCA_DESPLEGABLE FROM E_TCA,E_TDET,E_DES_VAL ";
                    
                    sql += "WHERE TDET_MUN=TCA_MUN AND TDET_PRO=TCA_PRO AND TDET_TRA=TCA_TRA AND TDET_COD=TCA_COD  AND TCA_TDA=6 " +
                          "AND TDET_MUN=" + codMunicipio + " AND TDET_PRO='" + codProcedimiento +  "' AND TDET_EJE=" + ejercicio + " AND TDET_NUM='" + numExpediente + "' " +
                          "AND TDET_TRA=" + codTramite;
                    if(ocurrenciaTramite>0){
                    sql=sql+ " AND TDET_OCU=" + ocurrenciaTramite;
                    } else orderBy=" ORDER BY TDET_OCU DESC";
                    sql=sql+ " AND TDET_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_DESPLEGABLE=E_DES_VAL.DES_COD AND E_DES_VAL.DES_VAL_COD=TDET_VALOR";
                    break;
                }
            }
            sql=sql+orderBy;
            log.debug("sql: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                campo = new CampoSuplementarioModuloIntegracionVO();
                // Fijo
                String rotulo      = rs.getString("TCA_ROT");
                String descripcion = rs.getString("TCA_DES");
                // Variable
                if(tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO){
                    campo.setValorNumero(rs.getString("TNUT_VALOR"));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                } else if (tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO){
                    campo.setValorTexto(rs.getString("TXTT_VALOR"));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                } else if (tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA){
                    campo.setValorFecha(DateOperations.toCalendar(rs.getTimestamp("TFET_VALOR")));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                } else if (tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO){
                    try{
                        Reader reader = rs.getCharacterStream("TTLT_VALOR");
                        char[] cbuf = new char[65536];
                        StringBuffer stringbuf = new StringBuffer();
                        while (reader.read(cbuf,0,65536)!=-1) {
                            stringbuf.append(cbuf);
                        }//end while

                        campo.setValorTexto(stringbuf.toString());
                        campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO);

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                } else if (tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE){
                    campo.setValorDesplegable(rs.getString("TDET_VALOR"));
                    campo.setDescripcionValorDesplegable(rs.getString("DES_NOM"));
                    campo.setCodigoDesplegable(rs.getString("TCA_DESPLEGABLE"));
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
                } else if (tipoCampo==ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO){
                    byte[] contenido = null;
                    String nombreFichero = rs.getString("TFIT_NOMFICH");
                    String mimeType      = rs.getString("TFIT_MIME");
                    try {
                        Hashtable<String,Object> datos = new Hashtable<String,Object>();
                        datos.put("numeroDocumento",codCampo);              
                        datos.put("codTipoDato",codCampo);
                        datos.put("codMunicipio",codMunicipio);
                        datos.put("ejercicio",ejercicio);
                        datos.put("numeroExpediente",numExpediente);              
                        datos.put("params",params);
                        datos.put("codTramite",Integer.toString(codTramite));
                        datos.put("ocurrenciaTramite",Integer.toString(ocurrenciaTramite));
                        
                        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codMunicipio).getImplClassPluginProcedimiento(codMunicipio,codProcedimiento);
                        Documento doc = null;
                        int tipoDocumento = -1;
                        if(!almacen.isPluginGestor())
                             tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                        else{
                            //PENDIENTE IMPLEMENTACION - GESTOR ALFRESCO
                            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                            
                            //  Si se trata de un plugin de un gestor documental, se pasa la información extra necesaria
                            ResourceBundle config = ResourceBundle.getBundle("documentos");                            
                            String carpetaRaiz       = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codMunicipio + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                            GeneralValueObject gVO = DatosSuplementariosManager.getInstance().getInfoCampoSuplementarioFicheroTramite(Integer.parseInt(codMunicipio),
                                    codTramite,ocurrenciaTramite,codCampo,numExpediente,expHistorico,params);              
                            String descripcionOrganizacion = (String)gVO.getAtributo("descOrganizacion");
                            String descProcedimiento = (String)gVO.getAtributo("descProcedimiento");
                            nombreFichero = (String)gVO.getAtributo("nombreFichero");
                            
                            datos.put("tipoMime",mimeType);                    
                            datos.put("nombreDocumento", codCampo + "_" + nombreFichero);
                            datos.put("nombreFicheroCompleto", codCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);
                            datos.put("nombreDocumento", codCampo + "_" + codTramite + "_" + ocurrenciaTramite + "_" +  nombreFichero);

                            ArrayList<String> listaCarpetas = new ArrayList<String>();
                            listaCarpetas.add(carpetaRaiz);
                            listaCarpetas.add(codMunicipio + ConstantesDatos.GUION + descripcionOrganizacion);
                            listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                            listaCarpetas.add(numExpediente.replaceAll("/","-"));
                            datos.put("listaCarpetas",listaCarpetas);
                        }

                        doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                        doc.setExpHistorico(expHistorico);
                        doc = almacen.getDocumentoDatosSuplementariosTramite(doc);

                        contenido = doc.getFichero();

                        if (log.isDebugEnabled()) {
                            log.debug("Logitud del fichero: " + contenido.length);
                            log.debug("Nombre del fichero: " + nombreFichero);
                            log.debug("Tipo contenido fichero: " + mimeType);
                        }
                    }  catch(AlmacenDocumentoTramitacionException e){
                        e.printStackTrace();
                    } catch(Exception e){
                        e.printStackTrace();
                    }

                    campo.setValorFichero(contenido);
                    campo.setNombreFichero(nombreFichero);
                    campo.setTipoMimeFichero(mimeType);
                    campo.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO);
                }

                campo.setCodProcedimiento(codProcedimiento);
                campo.setCodOrganizacion(codMunicipio);
                campo.setCodigoCampo(codCampo);
                campo.setNumExpediente(numExpediente);
                campo.setDescripcionCampo(descripcion);
                campo.setRotuloCampo(rotulo);
                campo.setTramite(true);

                salida.setStatus(0);
                salida.setDescStatus("OK");
                salida.setCampoSuplementario(campo);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
            }
        }catch(SQLException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(1);
            salida.setDescStatus("ERROR AL RECUPERAR EL CAMPO SUPLEMENTARIO DE BASE DE DATOS");
            salida.setCampoSuplementario(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
        }catch(Exception e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(1);
            salida.setDescStatus("ERROR AL RECUPERAR EL CAMPO SUPLEMENTARIO DE BASE DE DATOS");
            salida.setCampoSuplementario(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        if(campo==null){
            salida.setStatus(2);
            salida.setDescStatus("EL CAMPO SUPLEMENTARIO NO TIENE VALOR");
            salida.setCampoSuplementario(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
        }
        
        return salida;
    }


    /**
     * Graba el valor de un campo suplementarios de tipo numérico a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO     
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoNumericoExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int salida = -1;
        
        try{            
            String codOrganizacion = campo.getCodOrganizacion();
            String codCampo     = campo.getCodigoCampo();
            String codProcedimiento = campo.getCodProcedimiento();
            String numExpediente = campo.getNumExpediente();            
            String valor            = campo.getValorNumero();
            String ejercicio        = campo.getEjercicio();

            String sql = "SELECT COUNT(*) AS NUM FROM E_PCA WHERE PCA_MUN=" + codOrganizacion + " AND PCA_PRO='" + codProcedimiento + "' " +
                         "AND PCA_COD='" + codCampo + "' AND PCA_ACTIVO='SI' AND PCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{
               
              sql = "DELETE FROM E_TNU WHERE TNU_MUN=" + codOrganizacion + " AND TNU_EJE=" + ejercicio +
                    " AND TNU_NUM='" + numExpediente + "' AND TNU_COD='" + codCampo + "'";

              log.debug(sql);
              st = con.createStatement();
              int rowsDeleted = st.executeUpdate(sql);
              log.debug("Filas eliminadas: " + rowsDeleted);
              st.close();
              
              sql = "INSERT INTO E_TNU (TNU_MUN,TNU_EJE,TNU_NUM,TNU_COD,TNU_VALOR) VALUES(?,?,?,?,TO_NUMBER(" + valor + ",'99999999D99'))";
              log.debug(sql);
              ps = con.prepareStatement(sql);
              int i = 1;
              ps.setInt(i++,Integer.parseInt(codOrganizacion));
              ps.setInt(i++,Integer.parseInt(ejercicio));
              ps.setString(i++, numExpediente);
              ps.setString(i++, codCampo);              
              int rowsInserted = ps.executeUpdate();
              log.debug("Filas insertadas: " + rowsInserted);
              if(rowsInserted==1) salida = 0;
            }
           
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();                
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        
        return salida;
    }


   /**
     * Graba el valor de un campo suplementarios de tipo numérico a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoNumericoTramite(CampoSuplementarioModuloIntegracionVO campo, Connection con) throws SQLException {
        int salida              = -1;
        Statement st            = null;
        ResultSet rs            = null;        
        String codOrganizacion  = campo.getCodOrganizacion();
        String codProcedimiento = campo.getCodProcedimiento();        
        String numeroExpediente = campo.getNumExpediente();
        String codTramite          = campo.getCodTramite();
        String ocurrencia          = campo.getOcurrenciaTramite();
        String ejercicio           = campo.getEjercicio();
        String codCampo         = campo.getCodigoCampo();
        String valor               = campo.getValorNumero();

        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_TCA WHERE TCA_MUN=" + codOrganizacion + " AND TCA_PRO='" + codProcedimiento + "' " +
                         "AND TCA_TRA=" + codTramite + " AND TCA_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{

                sql = "DELETE FROM E_TNUT WHERE TNUT_MUN = " + codOrganizacion + " AND TNUT_PRO ='" + codProcedimiento + "' " +
                             "AND TNUT_EJE = " + ejercicio + " AND TNUT_NUM = '" + numeroExpediente + "' " +
                             "AND TNUT_TRA = " + codTramite + " AND TNUT_OCU = " + ocurrencia + " " +
                             "AND TNUT_COD = '" + codCampo + "'";
                log.debug(sql);

                st  = con.createStatement();
                int rowsDeleted = st.executeUpdate(sql);
                st.close();
                
                sql = "INSERT INTO E_TNUT(TNUT_MUN,TNUT_PRO,TNUT_EJE,TNUT_NUM,TNUT_TRA,TNUT_OCU,TNUT_COD,TNUT_VALOR) " +
                      "VALUES (" + codOrganizacion + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," +
                      codTramite + "," + ocurrencia + ",'" + codCampo + "',T0_NUMBER(" + valor + ",'99999999D99'))";

                log.debug(sql);
                st = con.createStatement();
                int rowsInserted = st.executeUpdate(sql);
                log.debug("rowsInserted: " + rowsInserted);
                if(rowsInserted==1) salida = 0;
                
            }// else
           
        }catch(SQLException e){
            e.printStackTrace();
             throw e;
        }finally{
            try{
                if(st!=null) st.close();                
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }



   /**
     * Graba el valor de un campo suplementarios de tipo texto a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoTextoExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int salida = -1;

        try{            
            String codOrganizacion     = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String codProcedimiento = campo.getCodProcedimiento();
            String numExpediente    = campo.getNumExpediente();
            String[] datos          = numExpediente.split("/");
            String valor            = campo.getValorTexto();
            String ejercicio           = campo.getEjercicio();

            String sql = "SELECT COUNT(*) AS NUM FROM E_PCA WHERE PCA_MUN=" + codOrganizacion + " AND PCA_PRO='" + codProcedimiento + "' " +
                         "AND PCA_COD='" + codCampo + "' AND PCA_ACTIVO='SI' AND PCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{

              sql = "DELETE FROM E_TXT WHERE TXT_MUN=" + codOrganizacion + " AND TXT_EJE=" + ejercicio +
                    " AND TXT_NUM='" + numExpediente + "' AND TXT_COD='" + codCampo + "'";

              log.debug(sql);
              st = con.createStatement();
              int rowsDeleted = st.executeUpdate(sql);
              log.debug("Filas eliminadas: " + rowsDeleted);
              st.close();
            
              sql = "INSERT INTO E_TXT (TXT_MUN,TXT_EJE,TXT_NUM,TXT_COD,TXT_VALOR) VALUES(?,?,?,?,?)";
              log.debug(sql);
              ps = con.prepareStatement(sql);
              int i = 1;
              ps.setInt(i++,Integer.parseInt(codOrganizacion));
              ps.setInt(i++, Integer.parseInt(ejercicio));
              ps.setString(i++, numExpediente);
              ps.setString(i++, codCampo);
              ps.setString(i++,valor);
              int rowsInserted = ps.executeUpdate();
              log.debug("Filas insertadas: " + rowsInserted);
              if(rowsInserted==1) salida = 0;
            }
           

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


    /**
     * Graba el valor de un campo suplementarios de tipo texto corto a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoTextoTramite(CampoSuplementarioModuloIntegracionVO campo, Connection con) throws SQLException {
        int salida              = -1;
        Statement st            = null;
        ResultSet rs            = null;        
        String codOrganizacion     = campo.getCodOrganizacion();
        String codProcedimiento = campo.getCodProcedimiento();
        String numeroExpediente = campo.getNumExpediente();
        String codTramite       = campo.getCodTramite();
        String ocurrencia       = campo.getOcurrenciaTramite();
        String ejercicio        = campo.getEjercicio();
        String codCampo         = campo.getCodigoCampo();
        String valor            = campo.getValorTexto();

        try{

            String sql = "SELECT COUNT(*) AS NUM FROM E_TCA WHERE TCA_MUN=" + codOrganizacion + " AND TCA_PRO='" + codProcedimiento + "' " +
                         "AND TCA_TRA=" + codTramite + " AND TCA_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{

                sql = "DELETE FROM E_TXTT WHERE TXTT_MUN = " + codOrganizacion + " AND TXTT_PRO ='" + codProcedimiento + "' " +
                             "AND TXTT_EJE = " + ejercicio + " AND TXTT_NUM = '" + numeroExpediente + "' " +
                             "AND TXTT_TRA = " + codTramite + " AND TXTT_OCU = " + ocurrencia + " " +
                             "AND TXTT_COD = '" + codCampo + "'";
                log.debug(sql);

                st  = con.createStatement();
                int rowsDeleted = st.executeUpdate(sql);
                log.debug("rowsDeleted: " + rowsDeleted);
                st.close();
                
                sql = "INSERT INTO E_TXTT (TXTT_MUN, TXTT_PRO, TXTT_EJE, TXTT_NUM, TXTT_TRA, TXTT_OCU, TXTT_COD, TXTT_VALOR) " +
                      "VALUES (" + codOrganizacion + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," +
                      codTramite + "," + ocurrencia + ",'" + codCampo + "','" + valor + "')";
                log.debug(sql);
                st= con.createStatement();
                int rowsInserted = st.executeUpdate(sql);
                log.debug("rowsInserted: " + rowsInserted);
                if(rowsInserted==1) salida = 0;
                
            }

        }catch(SQLException e){
            e.printStackTrace();
            salida = -1;
        }finally{
            try{
                if(st!=null) st.close();                
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


   /**
     * Graba el valor de un campo suplementarios de tipo texto largo a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoTextoLargoExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int salida = -1;

        try{            
            String codOrganizacion  = campo.getCodOrganizacion();
            String codProcedimiento = campo.getCodProcedimiento();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();            
            String valor            = campo.getValorTexto();
            String ejercicio        = campo.getEjercicio();
            
            String sql = "SELECT COUNT(*) AS NUM FROM E_PCA WHERE PCA_MUN=" + codOrganizacion + " AND PCA_PRO='" + codProcedimiento + "' " +
                         "AND PCA_COD='" + codCampo + "' AND PCA_ACTIVO='SI' AND PCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{

              sql = "DELETE FROM E_TTL WHERE TTL_MUN=" + codOrganizacion + " AND TTL_EJE=" + ejercicio +
                    " AND TTL_NUM='" + numExpediente + "' AND TTL_COD='" + codCampo + "'";
              log.debug(sql);
              st = con.createStatement();
              int rowsDeleted = st.executeUpdate(sql);
              log.debug("Filas eliminadas: " + rowsDeleted);
              st.close();
              
              sql = "INSERT INTO E_TTL (TTL_MUN,TTL_EJE,TTL_NUM,TTL_COD,TTL_VALOR) VALUES(?,?,?,?,?)";
              log.debug(sql);
              ps = con.prepareStatement(sql);
              int i = 1;
              ps.setInt(i++,Integer.parseInt(codOrganizacion));
              ps.setInt(i++, Integer.parseInt(ejercicio));
              ps.setString(i++, numExpediente);
              ps.setString(i++, codCampo);
              java.io.StringReader cr = new java.io.StringReader(valor);
              ps.setCharacterStream(i++,cr,valor.length());
              int rowsInserted = ps.executeUpdate();
              log.debug("Filas insertadas: " + rowsInserted);
              if(rowsInserted==1) salida = 0;
                  
           }//else
         

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


   /**
     * Graba el valor de un campo suplementarios de tipo texto largo a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoTextoLargoTramite(CampoSuplementarioModuloIntegracionVO campo, Connection con) throws SQLException {
        int salida              = -1;
        Statement st            = null;
        ResultSet rs            = null;        
        String codOrganizacion  = campo.getCodOrganizacion();
        String codProcedimiento = campo.getCodProcedimiento();
        String numeroExpediente = campo.getNumExpediente();
        String codTramite       = campo.getCodTramite();
        String ocurrencia       = campo.getOcurrenciaTramite();
        String ejercicio        = campo.getEjercicio();
        String codCampo         = campo.getCodigoCampo();
        String valor            = campo.getValorTexto();

        try{
            String sql = "SELECT COUNT(*) AS NUM FROM E_TCA WHERE TCA_MUN=" + codOrganizacion + " AND TCA_PRO='" + codProcedimiento + "' " +
                         "AND TCA_TRA=" + codTramite + " AND TCA_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{

                sql = "DELETE FROM E_TTLT WHERE TTLT_MUN = " + codOrganizacion + " AND TTLT_PRO ='" + codProcedimiento + "' " +
                             "AND TTLT_EJE = " + ejercicio + " AND TTLT_NUM = '" + numeroExpediente + "' " +
                             "AND TTLT_TRA = " + codTramite + " AND TTLT_OCU = " + ocurrencia + " " +
                             "AND TTLT_COD = '" + codCampo + "'";
                log.debug(sql);

                st  = con.createStatement();
                int rowsDeleted = st.executeUpdate(sql);
                st.close();               

                sql = "INSERT INTO E_TTLT (TTLT_MUN, TTLT_PRO, TTLT_EJE, TTLT_NUM, TTLT_TRA, TTLT_OCU, TTLT_COD, TTLT_VALOR) " +
                      "VALUES ("+ codOrganizacion + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numeroExpediente + "'," +
                      codTramite + "," + ocurrencia + ",'" + codCampo + "','" + valor + "')";
                      
                log.debug(sql);
                st = con.createStatement();
                int rowsInserted = st.executeUpdate(sql);
                log.debug("rowsInserted: " + rowsInserted);
                if(rowsInserted==1) salida = 0;
            }

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();                
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }



   /**
     * Graba el valor de un campo suplementarios de tipo texto largo a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoFechaExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int salida = -1;

        try{            
            String codOrganizacion  = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();
            String codProcedimiento = campo.getCodProcedimiento();            
            Calendar valor          = campo.getValorFecha();
            String ejercicio        = campo.getEjercicio();
            
            String sql = "SELECT COUNT(*) AS NUM FROM E_PCA WHERE PCA_MUN=" + codOrganizacion + " AND PCA_PRO='" + codProcedimiento + "' " +
                         "AND PCA_COD='" + codCampo + "' AND PCA_ACTIVO='SI' AND PCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{
              sql = "DELETE FROM E_TFE WHERE TFE_MUN=" + codOrganizacion + " AND TFE_EJE=" + ejercicio +
                    " AND TFE_NUM='" + numExpediente + "' AND TFE_COD='" + codCampo + "'";

              log.debug(sql);
              st = con.createStatement();
              int rowsDeleted = st.executeUpdate(sql);
              log.debug("Filas eliminadas: " + rowsDeleted);
              st.close();
              
              sql = "INSERT INTO E_TFE (TFE_MUN,TFE_EJE,TFE_NUM,TFE_COD,TFE_VALOR) VALUES(?,?,?,?,?)";
              log.debug(sql);
              ps = con.prepareStatement(sql);
              int i = 1;
              ps.setInt(i++,Integer.parseInt(codOrganizacion));
              ps.setInt(i++,Integer.parseInt(ejercicio));
              ps.setString(i++, numExpediente);
              ps.setString(i++, codCampo);
              ps.setTimestamp(i++, DateOperations.toTimestamp(valor));

              int rowsInserted = ps.executeUpdate();
              log.debug("Filas insertadas: " + rowsInserted);
              if(rowsInserted==1) salida = 0;
            }

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


     /**
     * Graba el valor de un campo suplementarios de tipo texto largo a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoFechaTramite(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int salida = -1;

        try{            
            String codOrganizacion     = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();            
            Calendar valor          = campo.getValorFecha();
            String codProcedimiento = campo.getCodProcedimiento();
            String codTramite          = campo.getCodTramite();
            String ocurrencia          = campo.getOcurrenciaTramite();
            String ejercicio           = campo.getEjercicio();
            

            String sql = "SELECT COUNT(*) AS NUM FROM E_TCA WHERE TCA_MUN=" + codOrganizacion + " AND TCA_PRO='" + codProcedimiento + "' " +
                         "AND TCA_TRA=" + codTramite + " AND TCA_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{

                  sql = "DELETE FROM E_TFET WHERE TFET_MUN=" + codOrganizacion + " AND TFET_EJE=" + ejercicio +
                        " AND TFET_NUM='" + numExpediente + "' AND TFET_COD='" + codCampo + "' AND TFET_PRO='" + codProcedimiento + "'" +
                        " AND TFET_TRA=" + codTramite + "AND TFET_OCU=" + ocurrencia;

                  log.debug(sql);
                  st = con.createStatement();
                  int rowsDeleted = st.executeUpdate(sql);
                  log.debug("Filas eliminadas: " + rowsDeleted);
                  st.close();
                
                  sql = "INSERT INTO E_TFET (TFET_MUN,TFET_PRO,TFET_EJE,TFET_NUM,TFET_TRA,TFET_OCU,TFET_COD,TFET_VALOR) " +
                        "VALUES(" + codOrganizacion + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numExpediente + "'," +
                        codTramite + "," + ocurrencia + ",'" + codCampo + "',?)";

                  log.debug(sql);
                  ps = con.prepareStatement(sql);                                    
                  ps.setTimestamp(1, DateOperations.toTimestamp(valor));

                  int rowsInserted = ps.executeUpdate();
                  log.debug("Filas insertadas: " + rowsInserted);
                  if(rowsInserted==1) salida = 0;
                
            }// else
           
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
    }


  /**
     * Graba el valor de un campo suplementarios de tipo desplegable a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoDesplegableExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int salida = -1;

        try{            
            String codOrganizacion  = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();
            String codProcedimiento = campo.getCodProcedimiento();            
            String valor            = campo.getValorDesplegable();
            String ejercicio        = campo.getEjercicio();

            String sql = "SELECT PCA_DESPLEGABLE FROM E_PCA WHERE PCA_MUN=" + codOrganizacion + " AND PCA_PRO='" + codProcedimiento + "' " +
                         "AND PCA_COD='" + codCampo + "' AND PCA_ACTIVO='SI' AND PCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            String codDesplegable = null;
            while(rs.next()){
                codDesplegable = rs.getString("PCA_DESPLEGABLE");
            }
            st.close();
            rs.close();

            if(codDesplegable==null || "".equals(codDesplegable) || "null".equals(codDesplegable)){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{
                
               boolean valorCorrecto = this.esValorDesplegableValido(codDesplegable,valor,con);
               if(!valorCorrecto){
                    salida  = 3;
               }else{

                  sql = "DELETE FROM E_TDE WHERE TDE_MUN=" + codOrganizacion + " AND TDE_EJE=" + ejercicio +
                        " AND TDE_NUM='" + numExpediente + "' AND TDE_COD='" + codCampo + "'";
                  log.debug(sql);

                  st = con.createStatement();
                  int rowsDeleted = st.executeUpdate(sql);
                  log.debug("Filas eliminadas: " + rowsDeleted);
                  st.close();

                  sql = "INSERT INTO E_TDE (TDE_MUN,TDE_EJE,TDE_NUM,TDE_COD,TDE_VALOR) VALUES(?,?,?,?,?)";
                  log.debug(sql);
                  ps = con.prepareStatement(sql);
                  int i = 1;
                  ps.setInt(i++,Integer.parseInt(codOrganizacion));
                  ps.setInt(i++,Integer.parseInt(ejercicio));
                  ps.setString(i++, numExpediente);
                  ps.setString(i++, codCampo);
                  ps.setString(i++,valor);

                  int rowsInserted = ps.executeUpdate();
                  log.debug("Filas insertadas: " + rowsInserted);
                  if(rowsInserted==1) salida = 0;
               }
           }          

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


   /**
     * Graba el valor de un campo suplementarios de tipo desplegable a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo     
     *              3 -> El valor no se corresponde con uno de los valores del campo desplegable
     */
    public int setDatoDesplegableTramite(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;        
        int salida = -1;

        try{            
            String codOrganizacion  = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();
            String valor            = campo.getValorDesplegable();
            String codTramite       = campo.getCodTramite();
            String ocurrencia       = campo.getOcurrenciaTramite();
            String codProcedimiento = campo.getCodProcedimiento();
            String ejercicio        = campo.getEjercicio();
            
            String sql = "SELECT TCA_DESPLEGABLE FROM E_TCA WHERE TCA_MUN=" + codOrganizacion + " AND TCA_PRO='" + codProcedimiento + "' " +
                         "AND TCA_TRA=" + codTramite + " AND TCA_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE;;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            String codDesplegable = null;
            while(rs.next()){                
                codDesplegable = rs.getString("TCA_DESPLEGABLE");
            }
            
            st.close();
            rs.close();

            if(codDesplegable==null || "".equals(codDesplegable) || "null".equals(codDesplegable)){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{

              boolean valorCorrecto = this.esValorDesplegableValido(codDesplegable,valor,con);
              if(!valorCorrecto){
                salida  = 3;
              }else{

                  sql = "DELETE FROM E_TDET WHERE TDET_MUN=" + codOrganizacion + " AND TDET_EJE=" + ejercicio +
                        " AND TDET_NUM='" + numExpediente + "' AND TDET_COD='" + codCampo + "' AND TDET_PRO='" + codProcedimiento + "' " +
                        " AND TDET_TRA=" + codTramite + " AND TDET_OCU=" + ocurrencia;
                  log.debug(sql);

                  st = con.createStatement();
                  int rowsDeleted = st.executeUpdate(sql);
                  log.debug("Filas eliminadas: " + rowsDeleted);
                  st.close();

                  sql = "INSERT INTO E_TDET(TDET_MUN,TDET_PRO,TDET_EJE,TDET_NUM,TDET_TRA,TDET_OCU,TDET_COD,TDET_VALOR) " +
                        "VALUES(" + codOrganizacion + ",'" + codProcedimiento + "'," + ejercicio + ",'" + numExpediente + "'," +
                        codTramite + "," + ocurrencia + ",'" + codCampo + "','" + valor + "')";
                  log.debug(sql);
                  st = con.createStatement();
                  int rowsInserted = st.executeUpdate(sql);
                  log.debug("Filas insertadas: " + rowsInserted);
                  if(rowsInserted==1) salida = 0;
              }
            }

        }catch(SQLException e){
            e.printStackTrace();            
            throw e;
        }finally{
            try{
                if(st!=null) st.close();                
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
  }



   /**
     * Comprueba si un determinado valor se corresponde con el valor de un determinado campo desplegable
     * @param codCampo: Código del campo
     * @param valor: Valor
     * @param con: Conexión a la base de datos
     * @return boolean
     */
    private boolean esValorDesplegableValido(String codCampo,String valor,Connection con){
        Statement st = null;
        ResultSet rs = null;                
        boolean exito = false;

        try{
          
            String sql = "SELECT COUNT(*) AS NUM FROM E_DES_VAL WHERE DES_COD='" + codCampo + "' AND DES_VAL_COD='" + valor + "'";
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }

            if(num==1) exito = true;
            
        }catch(SQLException e){
            e.printStackTrace();            
        }finally{
            try{
                if(st!=null) st.close();                
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }



   /**
     * Graba el valor de un campo suplementarios de tipo fichero a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoFicheroExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException, Exception {
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int salida = -1;

        try{            
            String codOrganizacion  = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();
            String codProcedimiento = campo.getCodProcedimiento();
            String ejercicio        = campo.getEjercicio();
            byte[] valor            = campo.getValorFichero();
            String tipoMime         = campo.getTipoMimeFichero();
            String nombreFichero    = campo.getNombreFichero();

            String sql = "SELECT COUNT(*) AS NUM FROM E_PCA WHERE PCA_MUN=" + codOrganizacion + " AND PCA_PRO='" + codProcedimiento + "' " +
                         "AND PCA_COD='" + codCampo + "' AND PCA_ACTIVO='SI' AND PCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO;
            log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{
/*                boolean expHistorico = (ExpedienteDAO.getInstance().getExpediente(Integer.parseInt(codOrganizacion),
                    Integer.parseInt(ejercicio),numExpediente,con) == null)?true:false;
*/                
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
                int tipoDocumento = -1;

                Hashtable<String,Object> datos = new Hashtable<String,Object>();                
                datos.put("fichero",valor);
                datos.put("codMunicipio",codOrganizacion);
                datos.put("codOrganizacion",codOrganizacion);
                datos.put("nombreDocumento",nombreFichero);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numExpediente);
                datos.put("tipoMime",tipoMime);
                datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));
                datos.put("longitudDocumento",valor.length);
                datos.put("codTipoDato",codCampo);

                if(!almacen.isPluginGestor()){ //Plugin BBDDD
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                }else{ 
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;            

                    //  Si se trata de un plugin de un gestor documental, se pasa la información
                    // extra necesaria                                    
                    ResourceBundle config = ResourceBundle.getBundle("documentos");
                    String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);                    

                    String descProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);
                    String descripcionOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codOrganizacion), con);

                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                    listaCarpetas.add(numExpediente.replaceAll("/","-"));
                    datos.put("listaCarpetas",listaCarpetas);
                }

                Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                                
                
                if(doc!=null) {
                    //doc.setExpHistorico(expHistorico);
                    if(almacen.setDocumentoDatoSuplementarioExpediente(doc,con)) 
                        salida = 0;
                }
            }
        }catch(TechnicalException e){
            e.printStackTrace();
            throw new SQLException(e);
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


   /**
     * Graba el valor de un campo suplementarios de tipo fichero a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int setDatoFicheroTramite(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException, Exception {
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int salida = -1;

        try{            
            String codOrganizacion  = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();
            String ejercicio        = campo.getEjercicio();
            byte[] valor            = campo.getValorFichero();
            String tipoMime         = campo.getTipoMimeFichero();
            String nombreFichero    = campo.getNombreFichero();
            String codTramite       = campo.getCodTramite();
            String ocurrencia       = campo.getOcurrenciaTramite();
            String codProcedimiento = campo.getCodProcedimiento();

            String sql = "SELECT COUNT(*) AS NUM FROM E_TCA WHERE TCA_MUN=" + codOrganizacion + " AND TCA_PRO='" + codProcedimiento + "' " +
                         "AND TCA_TRA=" + codTramite + " AND TCA_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
                int tipoDocumento = -1;
                
                Hashtable<String,Object> datos = new Hashtable<String,Object>();                
                datos.put("fichero",valor);
                datos.put("codMunicipio",codOrganizacion);
                datos.put("codOrganizacion",codOrganizacion);
                datos.put("nombreDocumento",nombreFichero);
                datos.put("ejercicio",ejercicio);
                datos.put("numeroExpediente",numExpediente);
                datos.put("tipoMime",tipoMime);
                datos.put("extension", MimeTypes.guessExtensionFromMimeType(tipoMime));
                datos.put("longitudDocumento",valor.length);
                datos.put("codTipoDato",codCampo);
                datos.put("codTramite",codTramite);
                datos.put("ocurrenciaTramite",ocurrencia);
                datos.put("codProcedimiento",codProcedimiento);
                datos.put("nombreFicheroCompleto",codCampo + "_" + codTramite + "_" + ocurrencia + "_" + 
                        nombreFichero);

                if(!almacen.isPluginGestor()){ //Plugin BBDDD
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                }else{ 
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;            
                
                    //  Si se trata de un plugin de un gestor documental, se pasa la información
                    // extra necesaria                                    
                    ResourceBundle config = ResourceBundle.getBundle("documentos");
                    String carpetaRaiz    = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);                    

                    String descProcedimiento = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento,con);
                    String descripcionOrganizacion = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codOrganizacion), con);
                    
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + descProcedimiento);
                    listaCarpetas.add(numExpediente.replaceAll("/","-"));
                    datos.put("listaCarpetas",listaCarpetas);
                }

                Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);                                
                
                if(doc!=null) {
                   if(almacen.setDocumentoDatoSuplementarioTramite(doc,con)) 
                       salida = 0;
                }
            }// if

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }catch(TechnicalException e){
            e.printStackTrace();
            throw e;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


    /**
     * Recupera los valores de un determinado campo desplegable
     * @param codOrganizacion: Código de la organización
     * @param codCombo: Código del combo
     * @param con: Conexión a la base de datos
     * @return ArrayList<CodigoValorVO>
     */
    public SalidaIntegracionVO getCampoDesplegable(String codOrganizacion,String codCombo,Connection con){
        SalidaIntegracionVO salida  = new SalidaIntegracionVO();
        CampoDesplegableModuloIntegracionVO campo    = null;
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> valores = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;

        try {

            String sql = "SELECT DES_NOM FROM E_DES WHERE DES_COD='" + codCombo + "'";
            log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            String descripcionCampo = null;
            while(rs.next()){
                descripcionCampo = rs.getString("DES_NOM");
            }

            st.close();
            rs.close();

            if(descripcionCampo==null){
                // No existe el campo desplegable
                salida.setStatus(1);
                salida.setDescStatus("NO EXISTE EL CAMPO DESPLEGABLE");
                salida.setCampoDesplegable(campo);
                salida.setCamposDesplegables(null);

            }else{
                // Existe el campo desplegable
                // Crear la consulta.
                sql = "SELECT E_DES_VAL.DES_VAL_COD,E_DES_VAL.DES_NOM FROM E_DES_VAL,E_DES WHERE E_DES.DES_COD=E_DES_VAL.DES_COD AND E_DES.DES_COD='" + codCombo + "'";
                log.debug(sql);

                // Ejecutar la consulta.
                st = con.createStatement();
                rs = st.executeQuery(sql);

                campo = new CampoDesplegableModuloIntegracionVO();
                campo.setCodigo(codCombo);
                campo.setDescripcion(descripcionCampo);
                
                while (rs.next()) {
                    
                    String codigoValor      = rs.getString("DES_VAL_COD");
                    String valorCampo       = rs.getString("DES_NOM");

                    ValorCampoDesplegableModuloIntegracionVO valor = new ValorCampoDesplegableModuloIntegracionVO();
                    valor.setCodigo(codigoValor);
                    valor.setDescripcion(valorCampo);
                    valores.add(valor);
                }

                if(valores.size()>0)
                    campo.setValores(valores);
                else
                    campo.setValores(null);
            }// else

            if(campo!=null){
                salida.setStatus(0);
                salida.setDescStatus("OK");
                salida.setCampoDesplegable(campo);
                salida.setCamposDesplegables(null);
            }            
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            
        } finally {
            try{                
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return salida;
    }

    public SalidaIntegracionVO getCodDesplegableCampoSup(String codCampo, String codOrganizacion,String codProcedimiento,Connection con){
        SalidaIntegracionVO salida  = new SalidaIntegracionVO();
        CampoDesplegableModuloIntegracionVO campo    = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String codCombo = null;

        try {

            query = "SELECT PCA_DESPLEGABLE FROM E_PCA WHERE PCA_TDA=6 AND PCA_MUN=? AND PCA_PRO=? AND PCA_COD=?";

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetors pasados a la query: " + codOrganizacion + "-" + codProcedimiento + "-" + codCampo);
            }

            int contbd = 1;
            ps = con.prepareStatement(query);
            ps.setString(contbd++, codOrganizacion);
            ps.setString(contbd++, codProcedimiento);
            ps.setString(contbd++, codCampo);

            rs = ps.executeQuery();
            while(rs.next()){
                codCombo = rs.getString("PCA_DESPLEGABLE");
            }

            if(codCombo==null){
                // No existe el campo desplegable
                salida.setStatus(1);
                salida.setDescStatus("NO EXISTE EL CAMPO DESPLEGABLE");
                salida.setCampoDesplegable(campo);
                salida.setCamposDesplegables(null);

            }else{
                // Existe el campo desplegable
                campo = new CampoDesplegableModuloIntegracionVO();
                campo.setCodigo(codCombo);
            }// else

            if(campo!=null){
                salida.setStatus(0);
                salida.setDescStatus("OK");
                salida.setCampoDesplegable(campo);
                salida.setCamposDesplegables(null);
            }            
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            
        } finally {
            try{                
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return salida;
    }
    
    /**
     * Recupera los campos desplegables de una organización. Es posible recuperar sólo los de un determinado procedimiento pero si no
     * se indica se recuperan todos. También es posible recuperar los valores de estos campos si se desea.
     * @param codOrganizacion:  Código del municipio/organización
     * @param codProcedimiento: Código del procedimiento si se desea recuperar los campos desplegables qctivos que está asociados a un determinado procedimiento
     * @param recuperarValores: Flag que indica si se recuperan también los valores de los campos desplegables en el caso de que éstos los tengan
     * @return SalidaIntegracionVO con los campos recuperables con el método getCamposDesplegables
     */
     public SalidaIntegracionVO getCamposDesplegables(String codOrganizacion,String codProcedimiento,boolean recuperarValores,Connection con){
        SalidaIntegracionVO salida  = new SalidaIntegracionVO();
        
        ArrayList<CampoDesplegableModuloIntegracionVO> campos = new ArrayList<CampoDesplegableModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;

        try {
            String sql = null;
             if(codProcedimiento==null || codProcedimiento.length()==0)
            	sql= "SELECT DES_COD,DES_NOM FROM E_DES";
            else
            	sql= "SELECT DES_COD,DES_NOM FROM E_DES, E_PCA WHERE E_DES.DES_COD=E_PCA.PCA_DESPLEGABLE AND PCA_ACTIVO='SI' AND PCA_PRO='" + codProcedimiento + "' AND PCA_MUN=" + codOrganizacion;

            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                String codigoCampo       = rs.getString("DES_COD");
                String descripcionCampo  = rs.getString("DES_NOM");
                CampoDesplegableModuloIntegracionVO campo = new CampoDesplegableModuloIntegracionVO();
                campo.setCodigo(codigoCampo);
                campo.setDescripcion(descripcionCampo);
                if(recuperarValores){
                    ArrayList<ValorCampoDesplegableModuloIntegracionVO> valores =  this.getValoresCampoDesplegable(codigoCampo, con);
                    campo.setValores(valores);
                }else campo.setValores(null);                
                campos.add(campo);
            }            
            salida.setStatus(0);
            salida.setDescStatus("OK");
            salida.setCamposDesplegables(campos);

        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);

        } finally {
            try{
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
     }



   /**
     * Recupera los campos desplegables de una organización. Es posible recuperar sólo los de un determinado procedimiento pero si no
     * se indica se recuperan todos. También es posible recuperar los valores de estos campos si se desea.
     * @param codOrganizacion:  Código del municipio/organización
     * @param codProcedimiento: Código del procedimiento
     * @param codTramite: Código del trámite
     * @param recuperarValores: Flag que indica si se recuperan también los valores de los campos desplegables en el caso de que éstos los tengan
     * @return SalidaIntegracionVO con los campos recuperables con el método getCamposDesplegables
     */
     public SalidaIntegracionVO getCamposDesplegables(String codOrganizacion,String codProcedimiento,int codTramite,boolean recuperarValores,Connection con){
        SalidaIntegracionVO salida  = new SalidaIntegracionVO();

        ArrayList<CampoDesplegableModuloIntegracionVO> campos = new ArrayList<CampoDesplegableModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;

        try {
                       
            String sql= "SELECT DES_COD,DES_NOM FROM E_DES, E_TCA WHERE E_DES.DES_COD=E_TCA.TCA_DESPLEGABLE AND TCA_ACTIVO='SI' AND TCA_PRO='" + codProcedimiento + "' " +
                        "AND TCA_TRA=" + codTramite + " AND TCA_MUN=" + codOrganizacion;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                String codigoCampo       = rs.getString("DES_COD");
                String descripcionCampo  = rs.getString("DES_NOM");
                CampoDesplegableModuloIntegracionVO campo = new CampoDesplegableModuloIntegracionVO();
                campo.setCodigo(codigoCampo);
                campo.setDescripcion(descripcionCampo);
                if(recuperarValores){
                    ArrayList<ValorCampoDesplegableModuloIntegracionVO> valores =  this.getValoresCampoDesplegable(codigoCampo, con);
                    campo.setValores(valores);
                }else campo.setValores(null);
                campos.add(campo);
            }
            salida.setStatus(0);
            salida.setDescStatus("OK");
            salida.setCamposDesplegables(campos);

        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);

        } finally {
            try{
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
     }



   /**
     * Recupera los valores de un determinado campo desplegable
     * @param codCombo: Código del combo
     * @param con: Conexión a la base de datos
     * @return ArrayList<ValorCampoDesplegableVO>
     */
    private ArrayList<ValorCampoDesplegableModuloIntegracionVO> getValoresCampoDesplegable(String codCampo,Connection con) throws SQLException {

        ArrayList<ValorCampoDesplegableModuloIntegracionVO> valores = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;

        try {            
            String sql = "SELECT DES_VAL_COD,DES_NOM FROM E_DES_VAL WHERE DES_COD='" + codCampo + "'";
            log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                String codigo           = rs.getString("DES_VAL_COD");
                String descripcionCampo = rs.getString("DES_NOM");
                ValorCampoDesplegableModuloIntegracionVO valor = new ValorCampoDesplegableModuloIntegracionVO();
                valor.setCodigo(codigo);
                valor.setDescripcion(descripcionCampo);
                valores.add(valor);
            }// while
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try{
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return valores;
    }


    /**
     * Recupera el id de mayor valor de alguno de los registro de la tabla de tareas pendientes
     * @param con: conexión a la bb
     * @return int
     */
    private int getMaxId(Connection con)  {
        int num = 0;
        Statement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT MAX(ID) AS NUM FROM TAREAS_PENDIENTES_INICIO";
            log.debug(sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                num = rs.getInt("NUM");
            }
            
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();            
        } finally {
            try{
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return num;
    }


    public boolean insertarTareaPendienteInicio(int codMunicipio,int codTramite,int ocurrenciaTramite,String numExpediente,long codigoOperacion,Connection con)  {
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        try {
            int id = getMaxId(con) + 1;
            String sql = "INSERT INTO TAREAS_PENDIENTES_INICIO(ID,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,COD_MUNICIPIO,COD_OPERACION) " +
                         "VALUES(" + id + ",'" + numExpediente + "'," + codTramite + "," + ocurrenciaTramite + "," + codMunicipio + ","+ codigoOperacion + ")";

            log.debug(sql);
            st = con.createStatement();
            int rowsInserted = st.executeUpdate(sql);
            log.debug("rowsInserted: " + rowsInserted);
            if(rowsInserted==1) exito = true;            
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            exito = false;
        } finally {
            try{
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    } 

    
    public boolean insertarTareaPendienteInicio(int codMunicipio,int codTramite,int ocurrenciaTramite,String numExpediente,long codigoOperacion,int errorPersonalizado,String mensaje,Connection con)  {
    
        boolean exito = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            int id = getMaxId(con) + 1;

            
            String sql = "INSERT INTO TAREAS_PENDIENTES_INICIO(ID,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,COD_MUNICIPIO,COD_OPERACION,ERROR_PERSONALIZADO,ETIQUETAS_MENSAJE) " +
                         "VALUES(" + id + ",'" + numExpediente + "'," + codTramite + "," + ocurrenciaTramite + "," + codMunicipio + ","+ codigoOperacion + "," + errorPersonalizado + ",?)";
            
            log.debug(sql);
            ps = con.prepareStatement(sql);

            if(mensaje!=null && !"".equals(mensaje))
                ps.setString(1,mensaje);
            else
                ps.setNull(1,java.sql.Types.VARCHAR);

            int rowsInserted = ps.executeUpdate();
            log.debug("rowsInserted: " + rowsInserted);
            if(rowsInserted==1) exito = true;
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            exito = false;
        } finally {
            try{                
                if (ps != null) ps.close();
                if (rs != null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }
    



    public boolean tieneTareasPendienteInicio(int codMunicipio,int codTramite,int ocurrenciaTramite,String numExpediente,Connection con) {
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) AS NUM FROM TAREAS_PENDIENTES_INICIO WHERE NUM_EXPEDIENTE='" + numExpediente
                       + "' AND COD_TRAMITE=" + codTramite + " AND OCU_TRAMITE=" + ocurrenciaTramite + " AND COD_MUNICIPIO=" + codMunicipio;
                         
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;
            while(rs.next()){
                num = rs.getInt("NUM");
            }
            
            if(num>=1) exito = true;
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();            
        } finally {
            try{
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }
    
    
     public Vector tieneTareasPendienteInicioExpediente(int codMunicipio,String numExpediente,Connection con) {
       
        Vector resultado = new Vector();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            /*
            String sql = "SELECT COUNT(*) AS NUM, COD_TRAMITE,OCU_TRAMITE FROM TAREAS_PENDIENTES_INICIO WHERE NUM_EXPEDIENTE='" + numExpediente
                       + "' AND COD_MUNICIPIO=" + codMunicipio+" GROUP BY COD_TRAMITE,OCU_TRAMITE";
              */           
            
            String sql = "SELECT COUNT(*) AS NUM, COD_TRAMITE,OCU_TRAMITE FROM TAREAS_PENDIENTES_INICIO " + 
                         "WHERE NUM_EXPEDIENTE=? AND " + 
                         "COD_MUNICIPIO=? GROUP BY COD_TRAMITE,OCU_TRAMITE";
            
            log.debug(sql);
            int i = 1;
            ps = con.prepareStatement(sql);            
            ps.setString(i++,numExpediente);
            ps.setInt(i++,codMunicipio);
            
            rs = ps.executeQuery();
            int num = 0;
            boolean flag=false;
            while(rs.next()){
                GeneralValueObject exito = new GeneralValueObject();
                num = rs.getInt("NUM");
                if(num>=1) flag = true;
                else flag=false;
                exito.setAtributo("tramite", rs.getString("COD_TRAMITE"));
                exito.setAtributo("ocurrencia", rs.getString("OCU_TRAMITE"));
                exito.setAtributo("exito", flag);
                resultado.add(exito);
                
            }
            
            
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();            
        } finally {
            try{
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return resultado;
    }



    /**
     * Elimina una tarea pendiente de una determinada ocurrencia de un trámite
     * @param idTarea: Id de la tarea a eliminar
     * @param con: Conexión a la BD
     * @return boolean
     */
    public boolean eliminarTareasPendienteInicio(int idTarea,Connection con) {
        boolean exito = false;
        Statement st = null;
        
        try {
            String sql = "DELETE FROM TAREAS_PENDIENTES_INICIO WHERE ID=" + idTarea;
            log.debug(sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            log.debug("Se han eliminado: " + rowsDeleted + " tareas pendientes del trámite");
            exito = true;
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try{
                if (st != null) st.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }


    
     public boolean eliminarTareasPendienteInicio(int codMunicipio,int codTramite,int ocurrenciaTramite,String numExpediente,Connection con) {
        boolean exito = false;
        Statement st = null;
        
        try {
            String sql = "DELETE FROM TAREAS_PENDIENTES_INICIO WHERE NUM_EXPEDIENTE='" + numExpediente + "'"
                       + " AND COD_TRAMITE=" + codTramite + " AND OCU_TRAMITE=" + ocurrenciaTramite + " AND COD_MUNICIPIO=" + codMunicipio;

            log.debug(sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            log.debug("Se han eliminado: " + rowsDeleted + " tareas pendientes del trámite");
            exito = true;
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try{
                if (st != null) st.close();        
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }


   /**
     * Recupera los expedientes relacionados con un determinado
     * @param codMunicipio: Código del municipio
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @return CampoSuplementarioVO
     */
    public SalidaIntegracionVO getExpedientesRelacionados(String codMunicipio,String numExpediente,String codProcedimiento,String ejercicio,Connection con){
        ArrayList<ExpedienteModuloIntegracionVO> expedientes = new ArrayList<ExpedienteModuloIntegracionVO>();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        Statement st = null;
        ResultSet rs = null;

        try{

            if(!this.existeExpediente(Integer.parseInt(codMunicipio), Integer.parseInt(ejercicio), numExpediente, con)){
                salida.setStatus(1);
                salida.setDescStatus("NO EXISTE EL EXPEDIENTE PARA EL QUE SE BUSCAN LOS EXPEDIENTES RELACIONADOS");
                salida.setExpedientesRelacionados(null);
                salida.setCampoDesplegable(null);
                salida.setCampoSuplementario(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setListaDocumentosTramitacion(null);
            }else{

                String sql = "SELECT REX_MUNR, REX_EJER, REX_NUMR FROM E_REX " +
                             "WHERE REX_MUN =" + codMunicipio + " AND REX_EJE =" + ejercicio + " AND REX_NUM = '" + numExpediente + "'";

                if(codProcedimiento!=null && !"".equals(codProcedimiento)){
                    sql = sql + " AND REX_NUMR LIKE ('%" + codProcedimiento + "%')";
                }

                log.debug("sql: " + sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    ExpedienteModuloIntegracionVO exp = new ExpedienteModuloIntegracionVO();
                    exp.setCodMunicipio(rs.getInt("REX_MUNR"));
                    exp.setEjercicio(rs.getInt("REX_EJER"));
                    String rNumExpediente = rs.getString("REX_NUMR");
                    exp.setNumExpediente(rNumExpediente);
                    if(rNumExpediente!=null && rNumExpediente.length()>0){
                        String[] datos = rNumExpediente.split(ConstantesDatos.BARRA);
                        if(datos!=null && datos.length==3)
                            exp.setCodProcedimiento(datos[1]);
                    }
                    expedientes.add(exp);
                }
                
                salida.setStatus(0);
                salida.setDescStatus("OK");
                salida.setExpedientesRelacionados(expedientes);
                salida.setCampoDesplegable(null);
                salida.setCampoSuplementario(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setListaDocumentosTramitacion(null);
            }

        }catch(SQLException e){
            e.printStackTrace();
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR AL RECUPERAR EL CAMPO SUPLEMENTARIO DE BASE DE DATOS");
            salida.setExpedientesRelacionados(null);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setCampoDesplegable(null);

        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        if(expedientes.size()==0){
            salida = new SalidaIntegracionVO();
            salida.setStatus(1);
            salida.setDescStatus("EL EXPEDIENTE NO TIENE EXPEDIENTES RELACIONADOS");
            salida.setCampoSuplementario(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
        }

        return salida;
    }

    private boolean existeExpediente(int codMunicipio,int ejercicio,String numExpediente,Connection con) throws SQLException{
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
            boolean expHistorico = (ExpedienteDAO.getInstance().getExpediente(codMunicipio,
                    ejercicio,numExpediente,con) == null)?true:false;
            
            if (expHistorico)
                sql = "SELECT COUNT(*) AS NUM FROM HIST_E_EXP WHERE EXP_MUN=" + codMunicipio + " AND EXP_EJE=" + ejercicio + " AND EXP_NUM='" + numExpediente + "'";
            else
                sql = "SELECT COUNT(*) AS NUM FROM E_EXP WHERE EXP_MUN=" + codMunicipio + " AND EXP_EJE=" + ejercicio + " AND EXP_NUM='" + numExpediente + "'";
            
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                if(rs.getInt("NUM")>=1)
                    exito = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }catch(Exception e){
            e.printStackTrace();
            throw new SQLException(e);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }

    private boolean existeOcurrenciaTramite(int codMunicipio,String codProcedimiento,String numExpediente,
            int ejercicio,int codTramite,int ocurrenciaTramite,boolean expHistorico,Connection con) throws SQLException{
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
            if (expHistorico)
                sql = "SELECT COUNT(*) AS NUM FROM HIST_E_CRO WHERE CRO_MUN=" + codMunicipio;
            else
                sql = "SELECT COUNT(*) AS NUM FROM E_CRO WHERE CRO_MUN=" + codMunicipio;
                
            sql += " AND CRO_PRO='" + codProcedimiento + "' AND CRO_EJE=" + ejercicio +
                " AND CRO_NUM='" + numExpediente + "' AND CRO_TRA=" + codTramite;                
            
            if(ocurrenciaTramite>=0)
                sql = sql + " AND CRO_OCU=" + ocurrenciaTramite;
            
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                if(rs.getInt("NUM")>=1)
                    exito = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
            exito = false;
            throw e;
        }catch(Exception e){
            e.printStackTrace();
            exito = false;
            throw new SQLException(e);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }

    public SalidaIntegracionVO getListaDocumentosTramitacion(String codOrganizacion,String numExpediente,String codProcedimiento,int codTramite,int ocurrenciaTramite,String ejercicio,Connection con){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        ArrayList<DocumentoTramitacionModuloIntegracionVO> lista = new ArrayList<DocumentoTramitacionModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
            boolean expHistorico = (ExpedienteDAO.getInstance().getExpediente(Integer.parseInt(codOrganizacion),
                Integer.parseInt(ejercicio),numExpediente,con) == null)?true:false;
            
            if(existeOcurrenciaTramite(Integer.parseInt(codOrganizacion), codProcedimiento, numExpediente, 
                    Integer.parseInt(ejercicio),codTramite, ocurrenciaTramite, expHistorico, con)) {

                if (expHistorico)
                    sql = "SELECT CRD_FAL,CRD_FMO,CRD_FINF,CRD_USC,CRD_USM,CRD_DES,CRD_NUD,CRD_OCU,CRD_TRA FROM HIST_E_CRD";
                else
                    sql = "SELECT CRD_FAL,CRD_FMO,CRD_FINF,CRD_USC,CRD_USM,CRD_DES,CRD_NUD,CRD_OCU,CRD_TRA FROM E_CRD";
                
                sql += " WHERE CRD_MUN=" + codOrganizacion + " AND CRD_PRO='" + codProcedimiento + "' AND CRD_EJE=" + ejercicio
                           + " AND CRD_NUM='" + numExpediente+ "' AND CRD_TRA=" + codTramite;

                if(ocurrenciaTramite!=-1){
                    sql = sql + " AND CRD_OCU=" + ocurrenciaTramite;
                }

                sql = sql + " ORDER BY CRD_FMO DESC,CRD_FAL DESC";

                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    DocumentoTramitacionModuloIntegracionVO doc = new DocumentoTramitacionModuloIntegracionVO();
                    doc.setFechaAlta(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("CRD_FAL")));
                    doc.setFechaModificacion(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("CRD_FMO")));
                    doc.setFechaInforme(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("CRD_FINF")));
                    doc.setCodUsuarioAlta(rs.getInt("CRD_USC"));
                    doc.setCodUsuarioModificacion(rs.getInt("CRD_USM"));
                    doc.setNombreDocumento(rs.getString("CRD_DES"));
                    doc.setCodMunicipio(Integer.parseInt(codOrganizacion));
                    doc.setCodProcedimiento(codProcedimiento);
                    doc.setCodTramite(rs.getInt("CRD_TRA"));
                    doc.setOcuTramite(rs.getInt("CRD_OCU"));
                    doc.setNumExpediente(numExpediente);
                    doc.setNumDocumento(rs.getInt("CRD_NUD"));
                    lista.add(doc);

                }// while

                if(lista.size()>0){
                    salida.setStatus(0);
                    salida.setDescStatus("OK");
                    salida.setListaDocumentosTramitacion(lista);
                    salida.setCampoDesplegable(null);
                    salida.setCampoSuplementario(null);
                    salida.setCamposDesplegables(null);
                    salida.setDocumentoTramitacion(null);
                    salida.setExpedientesRelacionados(null);
                }
            }else{
                salida.setStatus(1);
                salida.setDescStatus("NO EXISTE EL TRÁMITE");
                salida.setListaDocumentosTramitacion(null);
                salida.setCampoDesplegable(null);
                salida.setCampoSuplementario(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
            }
        }catch(SQLException e){
            e.printStackTrace();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setListaDocumentosTramitacion(null);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
        }catch(Exception e){
            e.printStackTrace();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setListaDocumentosTramitacion(null);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }    
        }
        return salida;
    }


   /**
     * Recupera la información sobre una determinada ocurrencia de un trámite
     * @param codMunicipio: Código del municipio
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param ejercicio: Ejercicio
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param con: Conexión a la base de datos
     * @return CampoSuplementarioVO
     */
    public SalidaIntegracionVO getTramite(String codMunicipio,String numExpediente,String codProcedimiento,String ejercicio,int codTramite,int ocurrenciaTramite,Connection con){
        TramiteModuloIntegracionVO tramite = null;
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
            boolean expHistorico = (ExpedienteDAO.getInstance().getExpediente(Integer.parseInt(codMunicipio),
                Integer.parseInt(ejercicio),numExpediente,con) == null)?true:false;

            sql = "SELECT CRO_PRO,CRO_EJE,CRO_NUM,CRO_TRA,CRO_OCU,CRO_FEI,CRO_FEF,CRO_USU,CRO_MUN,CRO_FIP,CRO_FLI,CRO_FFP,CRO_OBS,CRO_USF,TML_VALOR,CRO_UTR ";
            
            if (expHistorico)
                sql += "FROM HIST_E_CRO,E_TML WHERE CRO_MUN=" + codMunicipio + " AND CRO_PRO='" + codProcedimiento + "' AND CRO_EJE=" + ejercicio;
            else
                sql += "FROM E_CRO,E_TML WHERE CRO_MUN=" + codMunicipio + " AND CRO_PRO='" + codProcedimiento + "' AND CRO_EJE=" + ejercicio;

            sql += " AND CRO_NUM='" + numExpediente + "' AND CRO_TRA=" + codTramite + " AND CRO_OCU=" + ocurrenciaTramite + " AND TML_MUN=CRO_MUN "
                       + " AND TML_PRO=CRO_PRO AND TML_TRA=CRO_TRA";

            log.debug("sql: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                tramite = new TramiteModuloIntegracionVO();
                tramite.setCodProcedimiento(rs.getString("CRO_PRO"));
                tramite.setEjercicio(rs.getInt("CRO_EJE"));
                tramite.setNumExpediente(rs.getString("CRO_NUM"));
                tramite.setCodTramite(rs.getInt("CRO_TRA"));
                tramite.setOcurrenciaTramite(rs.getInt("CRO_OCU"));
                tramite.setFechaInicio(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("CRO_FEI")));
                tramite.setFechaFin(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("CRO_FEF")));
                tramite.setCodUsuarioInicio(rs.getInt("CRO_USU"));
                tramite.setCodOrganizacion(rs.getInt("CRO_MUN"));
                tramite.setFechaInicioPlazo(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("CRO_FIP")));
                tramite.setFechaLimite(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("CRO_FLI")));
                tramite.setFechaFinPlazo(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("CRO_FFP")));
                tramite.setObservaciones(rs.getString("CRO_OBS"));
                tramite.setCodUsuarioFin(rs.getInt("CRO_USF"));
                tramite.setDescripcion(rs.getString("TML_VALOR"));
                tramite.setCodUnidadTramitadora(rs.getInt("CRO_UTR"));
            }

            if(tramite!=null){
                salida.setStatus(0);
                salida.setDescStatus("OK");
                salida.setTramite(tramite);
                salida.setCampoDesplegable(null);
                salida.setCampoSuplementario(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setListaDocumentosTramitacion(null);
                salida.setExpedientesRelacionados(null);
            }else{
                salida.setStatus(1);
                salida.setDescStatus("NO EXISTE LA OCURRENCIA DEL TRÁMITE");
                salida.setTramite(tramite);
                salida.setCampoDesplegable(null);
                salida.setCampoSuplementario(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setListaDocumentosTramitacion(null);
                salida.setExpedientesRelacionados(null);
            }
            
        }catch(SQLException e){
            e.printStackTrace();
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setTramite(tramite);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setExpedientesRelacionados(null);
        }catch(Exception e){
            e.printStackTrace();
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setTramite(tramite);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setExpedientesRelacionados(null);

        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }
    
      

    /**
     * Recupera la información de un determinado expediente
     * @param codMunicipio: Código del municipio
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la base de datos
     * @param params: Parámetros de conexión a la BBDD
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getExpediente(String codMunicipio,String numExpediente,String codProcedimiento,String ejercicio,Connection con,String[] params){
        ExpedienteModuloIntegracionVO expediente = null;
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
          boolean expHistorico = (ExpedienteDAO.getInstance().getExpediente(Integer.parseInt(codMunicipio),
                Integer.parseInt(ejercicio),numExpediente,con) == null)?true:false;

          sql = "SELECT EXP_PRO,EXP_EJE,EXP_NUM,EXP_FEI,EXP_FEF,EXP_USU,EXP_UOR,EXP_OBS,EXP_ASU,EXP_IMP,EXP_LOC, " +
                        "EXP_TRA,EXP_TOCU,UOR_COD_VIS,UOR_NOM,PRO_TIP,EXP_TRA, " + GlobalNames.ESQUEMA_GENERICO + "A_TPML.TPML_VALOR AS DESC_TIPO_PROCEDIMIENTO ";

          if (expHistorico)
              sql += "FROM HIST_E_EXP,A_UOR,E_PRO," + GlobalNames.ESQUEMA_GENERICO + "A_TPML ";
          else
            sql += "FROM E_EXP,A_UOR,E_PRO," + GlobalNames.ESQUEMA_GENERICO + "A_TPML ";
          
          sql += "WHERE EXP_MUN=" + codMunicipio + " AND EXP_PRO='" + codProcedimiento + "' AND EXP_EJE=" + ejercicio + " AND EXP_NUM='" + numExpediente + "' " +                         
                        "AND EXP_UOR = A_UOR.UOR_COD " +
                        "AND EXP_PRO=PRO_COD AND EXP_MUN=PRO_MUN AND PRO_TIP=" + GlobalNames.ESQUEMA_GENERICO + "A_TPML.TPML_COD";

            log.debug("sql: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            int codUltimoTramiteCerrado = -1;

            while(rs.next()){
                expediente = new ExpedienteModuloIntegracionVO();
                expediente.setCodProcedimiento(rs.getString("EXP_PRO"));
                expediente.setEjercicio(rs.getInt("EXP_EJE"));
                expediente.setNumExpediente(rs.getString("EXP_NUM"));
                expediente.setFechaInicio(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("EXP_FEI")));
                expediente.setFechaFin(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("EXP_FEF")));
                expediente.setCodUsuarioIniciaExpediente(rs.getInt("EXP_USU"));
                expediente.setCodigoUorVisibleInicioExpediente(rs.getString("UOR_COD_VIS"));
                expediente.setDescripcionUnidadInicioExpediente(rs.getString("UOR_NOM"));

                codUltimoTramiteCerrado = rs.getInt("EXP_TRA");
                log.debug("*************** codUltimoTramiteCerrado: " + codUltimoTramiteCerrado);
                
                String importante = rs.getString("EXP_IMP");
                if(importante!=null && !"".equals(importante) && "S".equalsIgnoreCase(importante)){
                    expediente.setImportante(true);
                }else
                    expediente.setImportante(false);
                
                expediente.setObservaciones(rs.getString("EXP_OBS"));
                expediente.setAsunto(rs.getString("EXP_ASU"));
                expediente.setCodigoUorVisibleInicioExpediente(rs.getString("UOR_COD_VIS"));
                expediente.setDescripcionUnidadInicioExpediente(rs.getString("UOR_NOM"));
                expediente.setLocalizacion(rs.getString("EXP_LOC"));
                expediente.setTipoProcedimiento(rs.getInt("PRO_TIP"));
                expediente.setDescripcionTipoProcedimiento(rs.getString("DESC_TIPO_PROCEDIMIENTO"));

                // Se recuperan los interesados del expediente
                expediente.setInteresados(this.getInteresadosExpediente(codMunicipio, numExpediente, codProcedimiento, ejercicio, expHistorico,con,params));
               
            }// while

            st.close();
            rs.close();

            if(codUltimoTramiteCerrado>0){
                // Si el expediente tiene algún trámite cerrado, se recupera la información del último
                
                if (expHistorico)
                    sql = "SELECT EXP_TRA,TRA_COU,EXP_TOCU,TML_VALOR FROM E_TRA,E_TML,HIST_E_EXP ";
                else
                    sql = "SELECT EXP_TRA,TRA_COU,EXP_TOCU,TML_VALOR FROM E_TRA,E_TML,E_EXP ";
                
                sql += "WHERE TRA_COD=" + codUltimoTramiteCerrado + " AND TRA_COD = TML_TRA AND TRA_MUN = TML_MUN AND TRA_PRO=TML_PRO " +
                      "AND TRA_PRO='" + codProcedimiento + "' AND EXP_PRO=TRA_PRO AND EXP_MUN=TRA_MUN " +
                      "AND EXP_TRA = TRA_COD AND EXP_NUM='" + numExpediente + "' AND EXP_MUN=" + codMunicipio + " AND EXP_EJE=" + ejercicio;
                log.debug(sql);

                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()){
                    expediente.setCodigoTramiteCerrado(rs.getInt("EXP_TRA"));
                    expediente.setCodigoTramiteVisibleCerrado(rs.getInt("TRA_COU"));
                    expediente.setOcurrenciaTramiteCerrado(rs.getInt("EXP_TOCU"));
                    expediente.setDescripcionTramiteCerrado(rs.getString("TML_VALOR"));
                }     
            }else{
                expediente.setCodigoTramiteCerrado(-1);
                expediente.setCodigoTramiteVisibleCerrado(-1);
                expediente.setOcurrenciaTramiteCerrado(-1);
                expediente.setDescripcionTramiteCerrado(null);
            }



            if(expediente!=null){
                salida.setStatus(0);
                salida.setDescStatus("OK");
                salida.setExpediente(expediente);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCampoSuplementario(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setListaDocumentosTramitacion(null);
                salida.setExpedientesRelacionados(null);
            }else{
                salida.setStatus(1);
                salida.setDescStatus("NO EXISTE EL EXPEDIENTE");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCampoSuplementario(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setListaDocumentosTramitacion(null);
                salida.setExpedientesRelacionados(null);
            }
        }catch(SQLException e){
            e.printStackTrace();
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setExpedientesRelacionados(null);
        }catch(Exception e){
            e.printStackTrace();
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setExpedientesRelacionados(null);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }

    
    /**
     * Recupera los interesados de un determinado expediente
     * @param codMunicipio: Código del municipio
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param ejercicio: Ejercicio
     * @param con: Conexión a la BBDD
     * @return ArrayList<InteresadoExpedienteModuloIntegracionVO>
     */
    private ArrayList<InteresadoExpedienteModuloIntegracionVO> getInteresadosExpediente(String codMunicipio,
            String numExpediente,String codProcedimiento,String ejercicio,boolean expHistorico,Connection con,String[] params){
        ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados = new ArrayList<InteresadoExpedienteModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;

        try{
            
            String sql = "SELECT EXT_ROL,HTE_TER,HTE_NVR,HTE_TID,HTE_DOC,HTE_NOM,HTE_AP1,HTE_AP2,HTE_PA1,HTE_PA2,HTE_NOC,HTE_TLF, " + 
                         "HTE_DCE,TID_DES,ROL_DES,ROL_PDE,EXT_NOTIFICACION_ELECTRONICA,EXT_DOT ";
            
            if (expHistorico)
                sql += "FROM HIST_E_EXT,T_HTE,T_TID,E_ROL ";
            else
                sql += "FROM E_EXT,T_HTE,T_TID,E_ROL ";
            
            sql += "WHERE EXT_MUN=" + codMunicipio + " AND EXT_NUM='" + numExpediente + "' AND EXT_EJE=" + ejercicio +
                         " AND EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR " +
                         " AND HTE_TID=TID_COD" +
                         " AND EXT_ROL=ROL_COD AND ROL_MUN=" + codMunicipio + " AND ROL_PRO='" + codProcedimiento + "'";
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                InteresadoExpedienteModuloIntegracionVO interesado = new InteresadoExpedienteModuloIntegracionVO();
                interesado.setApellido1(rs.getString("HTE_AP1"));
                interesado.setApellido2(rs.getString("HTE_AP2"));
                interesado.setCodigoRol(rs.getInt("EXT_ROL"));
                interesado.setCodigoTercero(rs.getInt("HTE_TER"));
                interesado.setDescripcionDocumento(rs.getString("TID_DES"));
                interesado.setDescripcionRol(rs.getString("ROL_DES"));
                interesado.setDocumento(rs.getString("HTE_DOC"));
                interesado.setDescripcionDocumento(rs.getString("TID_DES"));
                interesado.setEmail(rs.getString("HTE_DCE"));
                interesado.setNombre(rs.getString("HTE_NOM"));
                interesado.setNombreCompleto(rs.getString("HTE_NOC"));
                interesado.setNumeroTelefonoFax(rs.getString("HTE_TLF"));
                interesado.setNumeroVersion(rs.getInt("HTE_NVR"));
                interesado.setParticula1(rs.getString("HTE_PA1"));
                interesado.setParticula2(rs.getString("HTE_PA2"));
                int rolDefecto = rs.getInt("ROL_PDE");
                interesado.setCodDomicilioExpediente(rs.getInt("EXT_DOT"));
                if(rolDefecto==1)
                    interesado.setRolDefecto(true);
                else
                    interesado.setRolDefecto(false);

                interesado.setTipoDocumento(rs.getInt("HTE_TID"));
                int notificacionElectronica = rs.getInt("EXT_NOTIFICACION_ELECTRONICA");
                if(notificacionElectronica==1){
                    // El interesado admite notificación electrónica
                    interesado.setAdmiteNotificacionElectronica(true);
                }else // El interesado no admite notificación electrónica
                    interesado.setAdmiteNotificacionElectronica(false);

               
                TercerosValueObject tVO = new TercerosValueObject();
                tVO.setIdentificador(Integer.toString(interesado.getCodigoTercero()));
              
                ArrayList<TercerosValueObject> terceros = TercerosDAO.getInstance().getByIdTercero(tVO,con,params);

                for(int i=0;terceros!=null && i<terceros.size();i++){
                    TercerosValueObject ter = terceros.get(i);
                    String codTerceroActual = Integer.toString(interesado.getCodigoTercero());
                    
                    /**** PRUEBA ****/
                    String codDomicilioPrincipal = ter.getDomPrincipal();
                    /**** PRUEBA ****/
                    
                    if(ter.getIdentificador().equals(codTerceroActual)){
                        Vector<DomicilioSimpleValueObject> dom = (Vector<DomicilioSimpleValueObject>)ter.getDomicilios();
                        
                        interesado.setDomicilios(UtilitiesModuloIntegracion.listDomicilioSimpleValueObjectTolistDomicilioInteresadoModuloIntegracionVO(dom,codDomicilioPrincipal));
                        break;
                    }
                }// for

             interesados.add(interesado);


              
            }// while

        }catch(SQLException e){
            e.printStackTrace();
        }catch(TechnicalException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return interesados;
    }



    public ArrayList<TareasPendientesInicioTramiteVO> getTareasPendientesInicio(int codMunicipio,int codTramite,int ocurrenciaTramite,String numExpediente,int codIdiomaUsuario,Connection con) {
        ArrayList<TareasPendientesInicioTramiteVO> tareas = new ArrayList<TareasPendientesInicioTramiteVO>();
        Statement st = null;
        ResultSet rs = null;

        try{
            
            String sql = "SELECT ID,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,COD_MUNICIPIO,COD_OPERACION,ERROR_PERSONALIZADO,ETIQUETAS_MENSAJE, " +
                         "DEF_TRA_TIPO_ORIGEN_OPERACION,DEF_TRA_NOMBRE_MODULO,DEF_TRA_NOMBRE_OPERACION " +
                         "FROM TAREAS_PENDIENTES_INICIO,DEF_TRA_SW " +
                         "WHERE NUM_EXPEDIENTE='" + numExpediente + "' AND COD_TRAMITE=" + codTramite + " AND OCU_TRAMITE=" + ocurrenciaTramite +
                         " AND COD_MUNICIPIO=" + codMunicipio + " AND TAREAS_PENDIENTES_INICIO.COD_OPERACION=DEF_TRA_SW.DEF_TRA_CFO " +
                         "ORDER BY TAREAS_PENDIENTES_INICIO.ID ASC";
            
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            ResourceBundle config = null;

            TraductorAplicacionBean traductor = new TraductorAplicacionBean();
            traductor.setApl_cod(ConstantesDatos.APP_GESTION_EXPEDIENTES);
            traductor.setIdi_cod(codIdiomaUsuario);

            while(rs.next()){
                TareasPendientesInicioTramiteVO tarea = new TareasPendientesInicioTramiteVO();
                tarea.setIdTarea(rs.getInt("ID"));
                tarea.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
                tarea.setCodTramite(rs.getInt("COD_TRAMITE"));
                tarea.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                tarea.setCodMunicipio(rs.getInt("COD_MUNICIPIO"));
                tarea.setCodOperacion(rs.getLong("COD_OPERACION"));
                tarea.setErrorPersonalizado(rs.getBoolean("ERROR_PERSONALIZADO"));
                String etiquetasMensaje = rs.getString("ETIQUETAS_MENSAJE");
                log.debug("etiquetasMensaje: " + etiquetasMensaje);

                String tipoOrigen = rs.getString("DEF_TRA_TIPO_ORIGEN_OPERACION");
                if(tipoOrigen!=null && ConstantesDatos.TIPO_ORIGEN_OPERACION_WS.equals(tipoOrigen))
                    tarea.setOperacionModulo(false);
                else
                if(tipoOrigen!=null && ConstantesDatos.TIPO_ORIGEN_OPERACION_MODULO.equals(tipoOrigen))
                    tarea.setOperacionModulo(true);

                tarea.setNombreOperacion(rs.getString("DEF_TRA_NOMBRE_OPERACION"));
                tarea.setNombreModulo(rs.getString("DEF_TRA_NOMBRE_MODULO"));

                if(tarea.isOperacionModulo() && tarea.isErrorPersonalizado()){
                    // SI LA OPERACIÓN ES PARA UN MÓDULO Y EL MENSAJE DE ERROR DE LA OPERACIÓN ESTÁ PERSONALIZADO, SE RECUPERA DEL FICHERO DE CONFIGURACIÓN DEL MODULO
                   String mensaje = "";
                   try{
                        config = ResourceBundle.getBundle(tarea.getNombreModulo());
                        /*
                        String etiqueta = config.getString(tarea.getCodMunicipio() + ConstantesDatos.MODULO_INTEGRACION + tarea.getNombreOperacion()
                                       + ConstantesDatos.MENSAJE_ERROR_MODULO_EXTERNO + codIdiomaUsuario);
                        */
                        mensaje = config.getString(etiquetasMensaje + ConstantesDatos.GUION_BAJO + codIdiomaUsuario);

                        if(mensaje!=null && !"".equals(mensaje))
                            tarea.setMensajeError(mensaje);

                    }catch(Exception e){
                        // Si no se ha podido recuperar el error personalizado, se muestra un error genérico
                        tarea.setMensajeError(UtilitiesModuloIntegracion.getMensajeGenericoErrorOperacionModulo(codIdiomaUsuario, tarea.getNombreOperacion(),tarea.getNombreModulo()));
                    }
                }else
                if(tarea.isOperacionModulo() && !tarea.isErrorPersonalizado()){
                    // Si no se trata de un error personalizado para una operación de un módulo                  
                    if(etiquetasMensaje!=null && !"".equals(etiquetasMensaje))
                        tarea.setMensajeError(UtilitiesModuloIntegracion.getMensajeFalloOperacionInicioTramite(etiquetasMensaje,tarea.getNombreOperacion(),tarea.getNombreModulo(),traductor)); 
                    else
                        tarea.setMensajeError(UtilitiesModuloIntegracion.getMensajeGenericoErrorOperacionModulo(codIdiomaUsuario, tarea.getNombreOperacion(), tarea.getNombreModulo()));

                }else
                //if(!tarea.isOperacionModulo() && !tarea.isErrorPersonalizado()) {
                if(!tarea.isOperacionModulo()) {
                    // Si la operación es de un servicio web => Vamos a suponer que es un error genérico de un servicio web.
                    String nombreServicioWeb = this.getNombreServicioWeb(tarea.getCodOperacion(), tarea.getNombreOperacion(), con);
                    log.debug("nombreServicio web: " + nombreServicioWeb);
                    tarea.setMensajeError(UtilitiesModuloIntegracion.getMensajeFalloOperacionInicioTramite(etiquetasMensaje,tarea.getNombreOperacion(),nombreServicioWeb,traductor));
                }

                tareas.add(tarea);
            }// while

        }catch(SQLException e){
            log.error("Error al ejecutar getTareasPendientesInicio: " + e.getMessage());
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return tareas;
    }

    /**
     * Recupera el nombre de un servicio web a partir del código de una de sus operaciones
     * @param codOperacion: Código de la operación
     * @param operacion Nombre de la operación
     * @return String con el nombre o null sino se ha podido recuperar
     */
    private String getNombreServicioWeb(long codOperacion,String operacion,Connection con){
        String nombre = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            
            String sql = "SELECT SW_COD,SW_TIT " +
                         "FROM DEF_TRA_SW,SW_OPS_DEF,SW_INFO " +
                         "WHERE DEF_TRA_CFO=" + codOperacion + " AND DEF_TRA_NOMBRE_OPERACION='" + operacion + "' AND DEF_TRA_OP=OPS_DEF_COD " +
                         "AND OPS_DEF_TIT='" + operacion + "' AND OPS_SW_COD=SW_INFO.SW_COD";

            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                nombre = rs.getString("SW_TIT");
            }

        }catch(SQLException e){
            log.error("Error al ejecutar getNombreServicioWeb: " + e.getMessage());
            nombre = null;
        }
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return nombre;
    }


    

    /**
     * Recupera la info de la tabla DEF_TRA_SW de una determinada operación, que bien puede ser de un módulo o de un servicio web
     * @param codMunicipio: Código del municipio u organización
     * @param codTramite: Código del trámite
     * @param codOperacion: Código de la operación
     * @param con: Conexión a la BBDD
     * @return TareasPendientesInicioTramiteVO o null
     */
    public TareasPendientesInicioTramiteVO getInfoOperacion(int codMunicipio,int codTramite,long codOperacion,Connection con){
        TareasPendientesInicioTramiteVO tarea = null;
        Statement st = null;
        ResultSet rs = null;
        try{
             String sql = "SELECT DEF_TRA_TIPO_ORIGEN_OPERACION,DEF_TRA_NOMBRE_MODULO,DEF_TRA_NOMBRE_OPERACION " +
                          "FROM DEF_TRA_SW " +
                          "WHERE DEF_TRA_CFO=" + codOperacion + " AND DEF_TRA_MUN=" + codMunicipio + " AND DEF_COD_TRA=" + codTramite;

             log.debug(sql);
             st = con.createStatement();
             rs = st.executeQuery(sql);
             while(rs.next()){
                tarea = new TareasPendientesInicioTramiteVO();                  
                tarea.setCodTramite(codTramite);
                tarea.setCodMunicipio(codMunicipio);
                tarea.setCodOperacion(codOperacion);
                
                String tipoOrigen = rs.getString("DEF_TRA_TIPO_ORIGEN_OPERACION");
                if(tipoOrigen!=null && ConstantesDatos.TIPO_ORIGEN_OPERACION_WS.equals(tipoOrigen))
                    tarea.setOperacionModulo(false);
                else
                if(tipoOrigen!=null && ConstantesDatos.TIPO_ORIGEN_OPERACION_MODULO.equals(tipoOrigen))
                    tarea.setOperacionModulo(true);
                    
                tarea.setNombreModulo(rs.getString("DEF_TRA_NOMBRE_MODULO"));
                tarea.setNombreOperacion(rs.getString("DEF_TRA_NOMBRE_OPERACION"));

            }// while

        }catch(SQLException e){
            tarea = null;
            log.error("Error al recuperar una tarea pendiente de inicio: " + e.getMessage());
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return tarea;
    } 
    
    
    /**
     * Comprueba si una determinada operación de un módulo externo o de un WS si tiene
     * @param codMunicipio: Código del municipio
     * @param codTramite: Código del trámite
     * @param orden: Orden
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return TareasPendientesInicioTramiteVO o null
     */
    public boolean tieneOperacionTareasPendientesAsociadas(int codMunicipio,int codTramite,int orden,String codProcedimiento,Connection con){
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        try{
             String sql = "SELECT COUNT(*) AS NUM " +
                          "FROM DEF_TRA_SW,TAREAS_PENDIENTES_INICIO " +
                          "WHERE DEF_TRA_MUN=" + codMunicipio + " AND DEF_TRA_PRO='" + codProcedimiento + "' AND DEF_COD_TRA=" + codTramite + " " +
                          "AND DEF_TRA_ORD=" + orden + " AND DEF_TRA_CFO=TAREAS_PENDIENTES_INICIO.COD_OPERACION AND TAREAS_PENDIENTES_INICIO.COD_TRAMITE=" + codTramite + " " +
                          "AND TAREAS_PENDIENTES_INICIO.COD_MUNICIPIO=" + codMunicipio + " AND TAREAS_PENDIENTES_INICIO.NUM_EXPEDIENTE LIKE('%" + codProcedimiento + "%')";

             log.debug(sql);
             st = con.createStatement();
             rs = st.executeQuery(sql);
        
             int num = 0;
             while(rs.next()){
                num = rs.getInt("NUM");

             }// while

             if(num>0) exito = true;

        }catch(SQLException e){
            exito = false;
            log.error("Error en el método tieneOperacionTareasPendientesAsociadas: " + e.getMessage());
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return exito;
    }


   /**
     * Elimna las tareas pendientes de inicio de un determinado trámite si una determinada operación de un módulo externo o de un WS si tiene
     * @param codMunicipio: Código del municipio
     * @param codTramite: Código del trámite
     * @param orden: Orden
     * @param codProcedimiento: Código del procedimiento
     * @param con: Conexión a la BBDD
     * @return TareasPendientesInicioTramiteVO o null
     */
    public boolean borrarTareasPendientesAsociadasTramite(int codMunicipio,int codTramite,int orden,String codProcedimiento,Connection con) throws SQLException{
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;

        try{
             String sql = "SELECT TAREAS_PENDIENTES_INICIO.ID AS ID " +
                          "FROM DEF_TRA_SW,TAREAS_PENDIENTES_INICIO " +
                          "WHERE DEF_TRA_MUN=" + codMunicipio + " AND DEF_TRA_PRO='" + codProcedimiento + "' AND DEF_COD_TRA=" + codTramite + " " +
                          "AND DEF_TRA_ORD=" + orden + " AND DEF_TRA_CFO=TAREAS_PENDIENTES_INICIO.COD_OPERACION AND TAREAS_PENDIENTES_INICIO.COD_TRAMITE=" + codTramite + " " +
                          "AND TAREAS_PENDIENTES_INICIO.COD_MUNICIPIO=" + codMunicipio + " AND TAREAS_PENDIENTES_INICIO.NUM_EXPEDIENTE LIKE('%" + codProcedimiento + "%')";

             log.debug(sql);

             st = con.createStatement();
             rs = st.executeQuery(sql);

             ArrayList<Integer> tareas = new ArrayList<Integer>();
             while(rs.next()){
                tareas.add(rs.getInt("ID"));
             }

             st.close();
             rs.close();

             int contador=0;
             for(int i=0;tareas!=null && i<tareas.size();i++){
                 int id = tareas.get(i);

                 sql = "DELETE FROM TAREAS_PENDIENTES_INICIO WHERE ID=" + id;
                 st = con.createStatement();
                 int rowsUpdated = st.executeUpdate(sql);
                 if(rowsUpdated!=1){
                     exito = false;
                     break;
                 }else contador++;
             }

             log.debug("borrarTareasPendientesAsociadasTramite número de registros a borrar: " + tareas.size());
             log.debug("borrarTareasPendientesAsociadasTramite número de registro borrados: " + contador);

             if(tareas!=null && tareas.size()==contador)
                 exito = true;

        }catch(SQLException e){
            exito = false;
            log.error("Error en el método tieneOperacionTareasPendientesAsociadas: " + e.getMessage());
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return exito;
    }
    
    
    
    public SalidaIntegracionVO getCircuitoFirmasDocumento(int codOrganizacion,String codProcedimiento,int codDocumento,Connection con){
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        ArrayList<CircuitoFirmaModuloIntegracionVO> circuito = new ArrayList<CircuitoFirmaModuloIntegracionVO>();
        
        try{
            
            String sql = "SELECT ID_FIRMA,FIRMA_USUARIO,FIRMA_ORDEN,FIRMA_COD_DOC,FIRMA_MUN,FIRMA_PROC,FIRMA_UOR,FIRMA_CARGO, FIRMA_FIN_REC, FIRMA_TRA_SUB " + 
                         "FROM E_DEF_FIRMA WHERE FIRMA_MUN=? AND FIRMA_PROC=? AND FIRMA_COD_DOC=? " + 
                         "ORDER BY FIRMA_ORDEN ASC";
            log.debug(sql);
            ps = con.prepareStatement(sql);            
            int i=1;
            ps.setInt(i++,codOrganizacion);
            ps.setString(i++,codProcedimiento);
            ps.setInt(i++,codDocumento);
            
            rs = ps.executeQuery();
            while(rs.next()){
                CircuitoFirmaModuloIntegracionVO c = new CircuitoFirmaModuloIntegracionVO();
                c.setIdFirma(rs.getInt("ID_FIRMA"));
                c.setCodUsuarioFirma(rs.getInt("FIRMA_USUARIO"));
                c.setFirmaOrden(rs.getInt("FIRMA_ORDEN"));
                c.setCodDocumentoPresentado(rs.getInt("FIRMA_COD_DOC"));
                c.setCodMunicipio(rs.getInt("FIRMA_MUN"));
                c.setCodUor(rs.getInt("FIRMA_UOR"));
                c.setCodCargoFirma(rs.getInt("FIRMA_CARGO"));
                c.setCodProcedimiento(rs.getString("FIRMA_PROC"));
				c.setTramitar(rs.getString("FIRMA_TRA_SUB"));
                c.setFinalizaRechazo("FIRMA_FIN_REC");
                circuito.add(c);
            }
            
            
            salida.setStatus(0);
            salida.setDescStatus("OK");
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoInicioExpediente(null);
            salida.setExpediente(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setCircuitoFirmasDocumento(circuito);
            salida.setFirmasDocumentoPresentado(null);
            
            
        }catch(Exception e){
            e.printStackTrace();
                          
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS AL RECUPERAR EL CIRCUITO DE FIRMAS DEL DOCUMENTO: " + e.getMessage());
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setCircuitoFirmasDocumento(null);
            salida.setFirmasDocumentoPresentado(null);
            
        }finally{            
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }           
        
        return salida;
    }
    
    
    
    /**
     * Recupera las firmas realizadas sobre un determinado documento de inicio de un expediente
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @param ejercicio: ejercicio
     * @param numExpediente: Número del expediente
     * @param codDocumento: Código del documento
     * @param con: Conexión a la BBDD
     * @return ArrayList<FirmaDocumentoPresentadoModuloIntegracionVO>
     */
    public SalidaIntegracionVO getFirmasDocumentoPresentado (String codOrganizacion,String codProcedimiento,String ejercicio,String numExpediente,int codDocumento,Connection con){
        ArrayList<FirmaDocumentoPresentadoModuloIntegracionVO> firmas = new ArrayList<FirmaDocumentoPresentadoModuloIntegracionVO>();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "";
        
        try{
            /*            
            String sql = "SELECT ID_DOC_FIRMA,DOC_FIRMA_ESTADO,DOC_FIRMA_ORDEN,DOC_FIRMA_UOR,DOC_FIRMA_CARGO,DOC_FIRMA_USUARIO,DOC_FIRMA_FECHA, " + 
                         "ID_DOC_PRESENTADO,DOC_FECHA_ENVIO,FIRMA,DOC_FIRMA_OBSERVACIONES,USU_COD,USU_LOG,USU_NOM,USU_EMAIL,USU_NIF " + 
                         "FROM E_DOCS_FIRMAS," + GlobalNames.ESQUEMA_GENERICO + "A_USU,E_DOCS_PRESENTADOS " +                          
                         "WHERE E_DOCS_FIRMAS.ID_DOC_PRESENTADO=E_DOCS_PRESENTADOS.PRESENTADO_COD " + 
                         "AND DOC_FIRMA_USUARIO=A_USU.USU_COD " + 
                         "AND E_DOCS_PRESENTADOS.PRESENTADO_COD_DOC=? AND E_DOCS_PRESENTADOS.PRESENTADO_MUN=? "+ 
                         "AND E_DOCS_PRESENTADOS.PRESENTADO_EJE=? AND E_DOCS_PRESENTADOS.PRESENTADO_NUM=? AND E_DOCS_PRESENTADOS.PRESENTADO_PRO=? " + 
                         "ORDER BY DOC_FIRMA_ORDEN ASC";
            */
            boolean expHistorico = (ExpedienteDAO.getInstance().getExpediente(Integer.parseInt(codOrganizacion),
                Integer.parseInt(ejercicio),numExpediente,con) == null)?true:false;

            sql = "SELECT ID_DOC_FIRMA,DOC_FIRMA_ESTADO,DOC_FIRMA_ORDEN,DOC_FIRMA_UOR,DOC_FIRMA_CARGO,DOC_FIRMA_USUARIO,DOC_FIRMA_FECHA, " + 
                    "ID_DOC_PRESENTADO,DOC_FECHA_ENVIO,DOC_FIRMA_OBSERVACIONES,USU_COD,USU_LOG,USU_NOM,USU_EMAIL,USU_NIF ";
            
            if (expHistorico)
                sql += "FROM HIST_E_DOCS_FIRMAS," + GlobalNames.ESQUEMA_GENERICO + "A_USU,HIST_E_DOCS_PRESENTADOS ";
            else
                sql += "FROM E_DOCS_FIRMAS," + GlobalNames.ESQUEMA_GENERICO + "A_USU,E_DOCS_PRESENTADOS ";

            sql += "WHERE E_DOCS_FIRMAS.ID_DOC_PRESENTADO=E_DOCS_PRESENTADOS.PRESENTADO_COD " + 
                         "AND DOC_FIRMA_USUARIO=A_USU.USU_COD " + 
                         "AND E_DOCS_PRESENTADOS.PRESENTADO_COD_DOC=? AND E_DOCS_PRESENTADOS.PRESENTADO_MUN=? "+ 
                         "AND E_DOCS_PRESENTADOS.PRESENTADO_EJE=? AND E_DOCS_PRESENTADOS.PRESENTADO_NUM=? AND E_DOCS_PRESENTADOS.PRESENTADO_PRO=? " + 
                         "ORDER BY DOC_FIRMA_ORDEN ASC";
            
            log.debug(sql);
            ps = con.prepareStatement(sql);            
            int i=1;            
            ps.setInt(i++,codDocumento);
            ps.setInt(i++,Integer.parseInt(codOrganizacion));
            ps.setInt(i++,Integer.parseInt(ejercicio));
            ps.setString(i++,numExpediente);
            ps.setString(i++,codProcedimiento);
            
            if(log.isDebugEnabled()){
                log.debug("codDocumento = " + codDocumento);
                log.debug("codOrganizacion = " + codOrganizacion);
                log.debug("ejercicio = " + ejercicio);
                log.debug("numExpediente = " + numExpediente);
                log.debug("codProcedimiento = " + codProcedimiento);
            }//if(log.isDebugEnabled)
            
            rs = ps.executeQuery();
            if(log.isDebugEnabled()) log.debug("Recorremos el array de resultados");
            while(rs.next()){
                FirmaDocumentoPresentadoModuloIntegracionVO f = new FirmaDocumentoPresentadoModuloIntegracionVO();
                f.setIdFirma(rs.getInt("ID_DOC_FIRMA"));
                f.setEstadoFirma(rs.getString("DOC_FIRMA_ESTADO"));
                f.setOrden(rs.getInt("DOC_FIRMA_ORDEN"));
                f.setCodUor(rs.getInt("DOC_FIRMA_UOR"));
                f.setCodCargoFirma(rs.getInt("DOC_FIRMA_CARGO"));
                f.setCodUsuario(rs.getInt("DOC_FIRMA_USUARIO"));
                java.sql.Timestamp fechaFirma = rs.getTimestamp("DOC_FIRMA_FECHA");
                java.sql.Timestamp fechaEnvio = rs.getTimestamp("DOC_FECHA_ENVIO");
                f.setFechaFirma(null);
                f.setFechaEnvio(null);
                if(fechaFirma!=null) f.setFechaFirma(DateOperations.toCalendar(fechaFirma));                
                if(fechaEnvio!=null) f.setFechaEnvio(DateOperations.toCalendar(fechaEnvio));
                f.setCodDocumentoFirma(rs.getInt("ID_DOC_PRESENTADO"));
                f.setObservaciones(rs.getString("DOC_FIRMA_OBSERVACIONES"));
                f.setNombreUsuario(rs.getString("USU_NOM"));
                f.setLoginUsuario(rs.getString("USU_LOG"));
                f.setEmailUsuario(rs.getString("USU_EMAIL"));
                f.setDocumento(rs.getString("USU_NIF"));
                
                /*
                Reader reader = rs.getCharacterStream("FIRMA");
                char[] cbuf = new char[65536];
                StringBuffer stringbuf = new StringBuffer();
                String firma = null;
                while (reader.read(cbuf,0,65536)!=-1) {
                    stringbuf.append(cbuf);
                }//end while
                firma = stringbuf.toString();                
                f.setFirma(firma);
                */
                firmas.add(f);
            }          
            
            if(log.isDebugEnabled()) log.debug("Numero de firmas = " + firmas.size());
            
            salida.setStatus(0);
            salida.setDescStatus("OK");
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoInicioExpediente(null);
            salida.setExpediente(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setCircuitoFirmasDocumento(null);
            salida.setFirmasDocumentoPresentado(firmas);
            
        }catch(Exception e){
            log.error("Se ha producido un error recuperando las firmas del documento " + e.getMessage());
            e.printStackTrace();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR AL RECUPERAR LAS FIRMAS DEL DOCUMENTO PRESENTADO DE BASE DE DATOS: " + e.getMessage());
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoInicioExpediente(null);
            salida.setExpediente(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setCircuitoFirmasDocumento(null);
            salida.setFirmasDocumentoPresentado(null);
            
        }finally{            
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }    
        
        return salida;
    }
    
    
    /**
     * Recupera el código interno de un documento de inicio de un procedimiento
     * @param codOrganizacion: Código de la organización/municipio
     * @param codPro: Código del procedimiento
     * @param nomDoc: Nombre del documento de inicio del procedimiento en Flexia
     * @param con: Conexión a la BBDD
     * @return int: -1 si no se ha podido recuperar
     *              0 en caso contrario
     */
    public int getCodigoDocumentoInicio(String codOrganizacion, String codPro, String nomDoc, Connection con){
        
        int codDocumento = -1;
        Statement stmt = null;
        ResultSet rs = null;
        String sql="";
        try{
            sql = "SELECT DOP_COD,  dpml1.DPML_VALOR AS nombreDocumento, dpml2.DPML_VALOR AS condicion" +
                    " FROM   e_dpml dpml1,  e_dpml dpml2,  E_DOP  WHERE DOP_MUN = " + Integer.valueOf(codOrganizacion) + 
                    " AND DOP_PRO= '" + codPro + "' AND e_dop.DOP_MUN  = dpml1.DPML_MUN" +
                    " AND e_dop.DOP_PRO = dpml1.DPML_PRO AND e_dop.DOP_COD = dpml1.DPML_DOP" +
                    " AND dpml1.DPML_LENG = '1'" +
                    " AND e_dop.DOP_MUN = dpml2.DPML_MUN AND e_dop.DOP_PRO = dpml2.DPML_PRO" +
                    " AND e_dop.DOP_COD = dpml2.DPML_DOP " +
                    " AND dpml2.DPML_LENG = '1' and dpml1.DPML_VALOR = '" + nomDoc + "'" + 
                    " GROUP BY DOP_COD, dpml1.DPML_VALOR, dpml2.DPML_VALOR";
        
            log.debug(sql);            
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                codDocumento = rs.getInt("DOP_COD");
            }
            
        }catch(Exception ex){
            log.error("Se ha producido un error buscando el código del documento: " + ex.getMessage(), ex);
            
        }finally{                        
            try{
                if(stmt!=null) stmt.close();
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                log.error("Error al cerrar los recursos asociados a la conexión a BBDD: " + e.getMessage());
            }            
        }        
        
        return codDocumento;
        
    }//getCodigoDocumentoInicio
    
    /**
     * Devuelve un objeto del tipo SalidaIntegracionVO con la UOR correspondiente al uorCodVis pasado como parametro
     * 
     * @param codOrganizacion
     * @param uorCodVis
     * @param con
     * @return 
     */
    public SalidaIntegracionVO getUOR (String codOrganizacion, String uorCodVis, Connection con){
        if(log.isDebugEnabled()) log.debug("getUOR() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        UORModuloIntegracionVO uorModuloIntegracionVO = new UORModuloIntegracionVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql="";
        try{
            sql += "Select UOR_COD, UOR_PAD, UOR_NOM, UOR_TIP, UOR_FECHA_ALTA, UOR_FECHA_BAJA, UOR_ESTADO, UOR_COD_VIS, ";
            sql += "UOR_EMAIL, UOR_NO_VIS, UOR_COD_ACCEDE, UOR_REX_GENERAL, OFICINA_REGISTRO ";
            sql += "From A_UOR ";
            sql += "where UOR_COD_VIS = ?";
            if(log.isDebugEnabled()) log.debug(("sql = " + sql));
            ps = con.prepareStatement(sql);
            ps.setString(1, uorCodVis);
            
            if(log.isDebugEnabled()) log.debug("Ejecutamos la sentencia");
            rs = ps.executeQuery();
            if(log.isDebugEnabled()) log.debug("Recorremos el array de resultados");
            while(rs.next()){
                uorModuloIntegracionVO.setUorCod(rs.getInt("UOR_COD"));
                uorModuloIntegracionVO.setUorPad(rs.getInt("UOR_PAD"));
                uorModuloIntegracionVO.setUorNom(rs.getString("UOR_NOM"));
                uorModuloIntegracionVO.setUorTip(rs.getString("UOR_TIP"));
                if(rs.getObject("UOR_FECHA_ALTA") != null){
                    java.sql.Date fechaAltaSql = rs.getDate("UOR_FECHA_ALTA");
                    uorModuloIntegracionVO.setUorFechaAlta(fechaAltaSql);
                }//if(rs.getObject("UOR_FECHA_ALTA") != null)
                if(rs.getObject("UOR_FECHA_BAJA") != null){
                    java.sql.Date fechaBajaSql = rs.getDate("UOR_FECHA_BAJA");
                    uorModuloIntegracionVO.setUorFechaBaja(fechaBajaSql);
                }//if(rs.getObject("UOR_FECHA_BAJA") != null)
                uorModuloIntegracionVO.setUorEstado(rs.getString("UOR_ESTADO"));
                uorModuloIntegracionVO.setUorCodVis(rs.getString("UOR_COD_VIS"));
                uorModuloIntegracionVO.setUorEmail(rs.getString("UOR_EMAIL"));
                uorModuloIntegracionVO.setUorNoVis(rs.getString("UOR_NO_VIS"));
                uorModuloIntegracionVO.setUorCodAccede(rs.getString("UOR_COD_ACCEDE"));
                uorModuloIntegracionVO.setUorRexGeneral(rs.getString("UOR_REX_GENERAL"));
                uorModuloIntegracionVO.setUorOficinaRegistro(rs.getInt("OFICINA_REGISTRO"));
            }//while(rs.next())
            if(log.isDebugEnabled())log.debug("Creamos el objeto a devolver por el metodo");
            salida.setStatus(0);
            salida.setDescStatus("OK");
            salida.setUorModuloIntegracionVO(uorModuloIntegracionVO);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la UOR de la BBDD " + ex.getMessage());
            salida.setStatus(-1);
            salida.setDescStatus("Se ha producido un error recuperando la UOR de la BBDD " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUOR() : BEGIN");
        return salida;
    }//getUOR

    /**
     * Devuelve un objeto del tipo SalidaIntegracionVO con el usuario correspondiente al idUsuario pasado como parametro
     * 
     * @param codOrganizacion
     * @param idUsuario
     * @param con
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getUsuario(String codOrganizacion, String idUsuario, Connection con){
        if(log.isDebugEnabled()) log.debug("getUsuario() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        UsuarioModuloIntegracionVO usuarioModuloIntegracionVO = new UsuarioModuloIntegracionVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql="";
        try{
            sql += "Select USU_COD, USU_LOG, USU_NOM, USU_FIRMANTE, USU_EMAIL ";
            sql += "From " + GlobalNames.ESQUEMA_GENERICO + "A_USU ";
            sql += "Where USU_COD = ?";
            sql += "And USU_FBA is null";
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            ps = con.prepareStatement(sql);
            ps.setString(1, idUsuario);
            
            if(log.isDebugEnabled()) log.debug("Ejecutamos la sentencia");
            rs = ps.executeQuery();
            if(log.isDebugEnabled()) log.debug("Recorremos el array de resultados");
            while(rs.next()){
                usuarioModuloIntegracionVO.setUsuCod(rs.getInt("USU_COD"));
                usuarioModuloIntegracionVO.setUsuLog(rs.getString("USU_LOG"));
                usuarioModuloIntegracionVO.setUsuNom(rs.getString("USU_NOM"));
                usuarioModuloIntegracionVO.setUsuFirmante(rs.getInt("USU_FIRMANTE"));
                usuarioModuloIntegracionVO.setUsuEmail(rs.getString("USU_EMAIL"));
            }//while(rs.next())
            if(log.isDebugEnabled())log.debug("Creamos el objeto a devolver por el metodo");
            salida.setStatus(0);
            salida.setDescStatus("OK");
            salida.setUsuarioModuloIntegracionVO(usuarioModuloIntegracionVO);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el usuario de la BBDD " + ex.getMessage());
            salida.setStatus(-1);
            salida.setDescStatus("Se ha producido un error recuperando el usuario de la BBDD " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUsuario() : END");
        return salida;
    }//getUsuario
    
    /**
     * Devuelve un objeto del tipo SalidaIntegracionVO con la lista de usuarios con permisos sobre la uor cuyo uorCodVis 
     * pasamos como parametro
     * 
     * @param codOrganizacion
     * @param idUsuario
     * @param con
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getUsuariosPermisoUOR (String codOrganizacion, String uorCodVis, Connection con){
        if(log.isDebugEnabled()) log.debug("getUsuariosPermisoUOR() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        ArrayList<UsuarioModuloIntegracionVO> listaUsuariosModuloIntegracionVO = new ArrayList<UsuarioModuloIntegracionVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql="";
        try{
            sql += "Select USU_COD, USU_LOG, USU_NOM, USU_FIRMANTE, USU_EMAIL ";
            sql += "From " + GlobalNames.ESQUEMA_GENERICO +"A_USU usu, " + GlobalNames.ESQUEMA_GENERICO + "A_UOU uou, A_UOR uor ";
            sql += "Where usu.usu_cod = uou.uou_usu ";
            sql += "And uou.uou_uor = uor.uor_cod ";
            sql += "And uou.uou_org = ? ";
            sql += "And uor.uor_cod_vis = ? ";
            sql += "And usu.usu_fba is null ";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.valueOf(codOrganizacion));
            ps.setString(2, uorCodVis);
            
            if(log.isDebugEnabled()) log.debug("Ejecutamos la sentencia");
            rs = ps.executeQuery();
            if(log.isDebugEnabled()) log.debug("Recorremos el array de resultados");
            while(rs.next()){
                UsuarioModuloIntegracionVO usuarioModuloIntegracionVO = new UsuarioModuloIntegracionVO();
                usuarioModuloIntegracionVO.setUsuCod(rs.getInt("USU_COD"));
                usuarioModuloIntegracionVO.setUsuLog(rs.getString("USU_LOG"));
                usuarioModuloIntegracionVO.setUsuNom(rs.getString("USU_NOM"));
                usuarioModuloIntegracionVO.setUsuFirmante(rs.getInt("USU_FIRMANTE"));
                usuarioModuloIntegracionVO.setUsuEmail(rs.getString("USU_EMAIL"));
                listaUsuariosModuloIntegracionVO.add(usuarioModuloIntegracionVO);
            }//while(rs.next())
            
            if(log.isDebugEnabled())log.debug("Creamos el objeto a devolver por el metodo");
            salida.setStatus(0);
            salida.setDescStatus("OK");
            salida.setListaUsuariosModuloIntegracionVO(listaUsuariosModuloIntegracionVO);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el usuario de la BBDD " + ex.getMessage());
            salida.setStatus(-1);
            salida.setDescStatus("Se ha producido un error recuperando el usuario de la BBDD " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUsuariosPermisoUOR() : END");
        return salida;
    }//getUsuariosPermisoUOR
    
    /**
     * Metodo que devuelve los expedientes del procedimiento para el cual es interesado segun los datos que se le pasan como
     * parametros.
     * 
     * @param codOrganizacion
     * @param codPro
     * @param idDocumento
     * @param documento
     * @param con
     * @param params
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getExpedientesByInteresadoAndProc (String codOrganizacion, String codPro, String idDocumento,
        String documento, Connection con, String[] params){
        if(log.isDebugEnabled()) log.debug("getExpedientesByInteresadoAndProc() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        ArrayList<ExpedienteModuloIntegracionVO> expedientes = new ArrayList<ExpedienteModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT EXT_ROL,HTE_TER,HTE_NVR,HTE_TID,HTE_DOC,HTE_NOM,HTE_AP1,HTE_AP2,HTE_PA1,HTE_PA2,HTE_NOC,HTE_TLF, " + 
                            "HTE_DCE,TID_DES,ROL_DES,ROL_PDE,EXT_NOTIFICACION_ELECTRONICA, EXT_NUM, EXT_EJE, EXT_PRO,'0' AS EXP_HISTORICO " +
                            "FROM E_EXT,T_HTE,T_TID,E_ROL " +
                            "WHERE EXT_MUN= " + codOrganizacion + " " +
                            "AND EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR " +
                            "AND HTE_TID=TID_COD " + 
                            "AND EXT_ROL=ROL_COD AND ROL_MUN=" + codOrganizacion + " AND ROL_PRO = '" + codPro + "' " +
                            "AND EXT_PRO = '" + codPro + "' " +
                            "AND HTE_TID = " + idDocumento + " " +
                            "AND HTE_DOC = '" + documento + "' " +
                            "UNION ALL " + 
                            "SELECT EXT_ROL,HTE_TER,HTE_NVR,HTE_TID,HTE_DOC,HTE_NOM,HTE_AP1,HTE_AP2,HTE_PA1,HTE_PA2,HTE_NOC,HTE_TLF, " + 
                            "HTE_DCE,TID_DES,ROL_DES,ROL_PDE,EXT_NOTIFICACION_ELECTRONICA, EXT_NUM, EXT_EJE, EXT_PRO,'1' AS EXP_HISTORICO " +
                            "FROM HIST_E_EXT,T_HTE,T_TID,E_ROL " +
                            "WHERE EXT_MUN= " + codOrganizacion + " " +
                            "AND EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR " +
                            "AND HTE_TID=TID_COD " + 
                            "AND EXT_ROL=ROL_COD AND ROL_MUN=" + codOrganizacion + " AND ROL_PRO = '" + codPro + "' " +
                            "AND EXT_PRO = '" + codPro + "' " +
                            "AND HTE_TID = " + idDocumento + " " +
                            "AND HTE_DOC = '" + documento + "' ";
            
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                //Recuperamos los datos del interesado.
                ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados = new ArrayList<InteresadoExpedienteModuloIntegracionVO>();
                InteresadoExpedienteModuloIntegracionVO interesado = new InteresadoExpedienteModuloIntegracionVO();
                ExpedienteModuloIntegracionVO expediente = new ExpedienteModuloIntegracionVO();
                interesado.setApellido1(rs.getString("HTE_AP1"));
                interesado.setApellido2(rs.getString("HTE_AP2"));
                interesado.setCodigoRol(rs.getInt("EXT_ROL"));
                interesado.setCodigoTercero(rs.getInt("HTE_TER"));
                interesado.setDescripcionDocumento(rs.getString("TID_DES"));
                interesado.setDescripcionRol(rs.getString("ROL_DES"));
                interesado.setDocumento(rs.getString("HTE_DOC"));
                interesado.setDescripcionDocumento(rs.getString("TID_DES"));
                interesado.setEmail(rs.getString("HTE_DCE"));
                interesado.setNombre(rs.getString("HTE_NOM"));
                interesado.setNombreCompleto(rs.getString("HTE_NOC"));
                interesado.setNumeroTelefonoFax(rs.getString("HTE_TLF"));
                interesado.setNumeroVersion(rs.getInt("HTE_NVR"));
                interesado.setParticula1(rs.getString("HTE_PA1"));
                interesado.setParticula2(rs.getString("HTE_PA2"));
                int rolDefecto = rs.getInt("ROL_PDE");
                if(rolDefecto==1){
                    interesado.setRolDefecto(true);
                }else{
                    interesado.setRolDefecto(false);
                }//if(rolDefecto==1)
                interesado.setTipoDocumento(rs.getInt("HTE_TID"));
                int notificacionElectronica = rs.getInt("EXT_NOTIFICACION_ELECTRONICA");
                if(notificacionElectronica==1){
                    // El interesado admite notificación electrónica
                    interesado.setAdmiteNotificacionElectronica(true);
                }else{ 
                    // El interesado no admite notificación electrónica
                    interesado.setAdmiteNotificacionElectronica(false);
                }//if(notificacionElectronica==1)
                TercerosValueObject tVO = new TercerosValueObject();
                tVO.setIdentificador(Integer.toString(interesado.getCodigoTercero()));
                ArrayList<TercerosValueObject> terceros = TercerosDAO.getInstance().getByIdTercero(tVO,con,params);
                for(int i=0;terceros!=null && i<terceros.size();i++){
                    TercerosValueObject ter = terceros.get(i);
                    String codTerceroActual = Integer.toString(interesado.getCodigoTercero());
                    if(ter.getIdentificador().equals(codTerceroActual)){
                        Vector<DomicilioSimpleValueObject> dom = (Vector<DomicilioSimpleValueObject>)ter.getDomicilios();
                        interesado.setDomicilios(UtilitiesModuloIntegracion.listDomicilioSimpleValueObjectTolistDomicilioInteresadoModuloIntegracionVO(dom));
                        break;
                    }//if(ter.getIdentificador().equals(codTerceroActual))
                }//for(int i=0;terceros!=null && i<terceros.size();i++)
                
                //Anhadimos el interesado al array que pasaremos al expediente.
                interesados.add(interesado);
                
                //Datos del expediente del cual es interesado segun los parametros recibidos
                String numExpediente = rs.getString("EXT_NUM");
                String ejercicio = rs.getString("EXT_EJE");
                String codProcedimiento = rs.getString("EXT_PRO");
                boolean expHistorico = "1".equals(rs.getString("EXP_HISTORICO"))?true:false;
                //Una vez tenemos los datos del interesado recuperamos los del expediente.
                expediente = getDatosExpediente(codOrganizacion, codPro, ejercicio, numExpediente, expHistorico,con);
                expediente.setInteresados(interesados);
                
                //Anhadimos el expediente al array que devolveremos
                expedientes.add(expediente);
            }//while(rs.next())
            
            if(expedientes.size() > 0){
                salida.setStatus(0);
                salida.setDescStatus("OK");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCampoSuplementario(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setListaDocumentosTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setExpedientes(expedientes);
            }else{
                salida.setStatus(1);
                salida.setDescStatus("NO EXISTE EL EXPEDIENTE");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCampoSuplementario(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setListaDocumentosTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setExpedientes(null);
            }//if(expedientes.size() > 0)
        }catch(SQLException e){
            e.printStackTrace();
            log.error("Se ha producido un error recuperando los expedientes para un interesado y procedimiento" + e.getMessage());
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setExpedientesRelacionados(null);
        }catch(TechnicalException e){
            e.printStackTrace();
            log.error("Se ha producido un error recuperando los expedientes para un interesado y procedimiento" + e.getMessage());
            salida = null;
            salida = new SalidaIntegracionVO();
            salida.setStatus(-1);
            salida.setDescStatus("ERROR DURANTE LA OPERACIÓN");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCampoSuplementario(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setExpedientesRelacionados(null);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
                log.error("Se ha producido un error cerrando el statement y el resultset " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getExpedientesByInteresadoAndProc() : END");
        return salida;
    }//getExpedientesByInteresadoAndProc
    
    /**
     * Método que devuelve los datos de un expediente cuyos datos se le envian como parametros.
     * 
     * @param codOrganizacion
     * @param codPro
     * @param ejercicio
     * @param numExpediente
     * @param con
     * @return ExpedienteModuloIntegracionVO
     */
    private ExpedienteModuloIntegracionVO getDatosExpediente (String codOrganizacion, String codPro, String ejercicio,
            String numExpediente, boolean expHistorico, Connection con){
        if(log.isDebugEnabled()) log.debug("getDatosExpediente() : BEGIN");
        ExpedienteModuloIntegracionVO expediente = new ExpedienteModuloIntegracionVO();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT EXP_PRO,EXP_EJE,EXP_NUM,EXP_FEI,EXP_FEF,EXP_USU,EXP_UOR,EXP_OBS,EXP_ASU,EXP_IMP,EXP_LOC, " +
                        "EXP_TRA,EXP_TOCU,UOR_COD_VIS,UOR_NOM,PRO_TIP,EXP_TRA, " + GlobalNames.ESQUEMA_GENERICO + "A_TPML.TPML_VALOR AS DESC_TIPO_PROCEDIMIENTO ";
            
            if (expHistorico)
                sql += "FROM HIST_E_EXP,A_UOR,E_PRO," + GlobalNames.ESQUEMA_GENERICO + "A_TPML ";
            else
                sql += "FROM E_EXP,A_UOR,E_PRO," + GlobalNames.ESQUEMA_GENERICO + "A_TPML ";
            
            sql += "WHERE EXP_MUN=" + codOrganizacion + " AND EXP_PRO='" + codPro + "' AND EXP_EJE=" + ejercicio + " AND EXP_NUM='" + numExpediente + "' " +                         
                        "AND EXP_UOR = A_UOR.UOR_COD " +
                        "AND EXP_PRO=PRO_COD AND EXP_MUN=PRO_MUN AND PRO_TIP=" + GlobalNames.ESQUEMA_GENERICO + "A_TPML.TPML_COD";
            
            log.debug("sql: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int codUltimoTramiteCerrado = -1;
            while(rs.next()){
                expediente.setCodProcedimiento(rs.getString("EXP_PRO"));
                expediente.setEjercicio(rs.getInt("EXP_EJE"));
                expediente.setNumExpediente(rs.getString("EXP_NUM"));
                expediente.setFechaInicio(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("EXP_FEI")));
                expediente.setFechaFin(UtilitiesModuloIntegracion.timestampToCalendar(rs.getTimestamp("EXP_FEF")));
                expediente.setCodUsuarioIniciaExpediente(rs.getInt("EXP_USU"));
                expediente.setCodigoUorVisibleInicioExpediente(rs.getString("UOR_COD_VIS"));
                expediente.setDescripcionUnidadInicioExpediente(rs.getString("UOR_NOM"));

                codUltimoTramiteCerrado = rs.getInt("EXP_TRA");
                log.debug("*************** codUltimoTramiteCerrado: " + codUltimoTramiteCerrado);
                
                String importante = rs.getString("EXP_IMP");
                if(importante!=null && !"".equals(importante) && "S".equalsIgnoreCase(importante)){
                    expediente.setImportante(true);
                }else
                    expediente.setImportante(false);
                
                expediente.setObservaciones(rs.getString("EXP_OBS"));
                expediente.setAsunto(rs.getString("EXP_ASU"));
                expediente.setCodigoUorVisibleInicioExpediente(rs.getString("UOR_COD_VIS"));
                expediente.setDescripcionUnidadInicioExpediente(rs.getString("UOR_NOM"));
                expediente.setLocalizacion(rs.getString("EXP_LOC"));
                expediente.setTipoProcedimiento(rs.getInt("PRO_TIP"));
                expediente.setDescripcionTipoProcedimiento(rs.getString("DESC_TIPO_PROCEDIMIENTO"));                
            }//while(rs.next())
            
            st.close();
            rs.close();
            
            if(codUltimoTramiteCerrado>0){
                // Si el expediente tiene algún trámite cerrado, se recupera la información del último
                if (expHistorico)
                    sql = "SELECT EXP_TRA,TRA_COU,EXP_TOCU,TML_VALOR FROM E_TRA,E_TML,HIST_E_EXP ";
                else
                    sql = "SELECT EXP_TRA,TRA_COU,EXP_TOCU,TML_VALOR FROM E_TRA,E_TML,E_EXP ";
                
                sql += "WHERE TRA_COD=" + codUltimoTramiteCerrado + " AND TRA_COD = TML_TRA AND TRA_MUN = TML_MUN AND TRA_PRO=TML_PRO " +
                      "AND TRA_PRO='" + codPro + "' AND EXP_PRO=TRA_PRO AND EXP_MUN=TRA_MUN " +
                      "AND EXP_TRA = TRA_COD AND EXP_NUM='" + numExpediente + "' AND EXP_MUN=" + codOrganizacion + " AND EXP_EJE=" + ejercicio;
                log.debug(sql);

                st = con.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next()){
                    expediente.setCodigoTramiteCerrado(rs.getInt("EXP_TRA"));
                    expediente.setCodigoTramiteVisibleCerrado(rs.getInt("TRA_COU"));
                    expediente.setOcurrenciaTramiteCerrado(rs.getInt("EXP_TOCU"));
                    expediente.setDescripcionTramiteCerrado(rs.getString("TML_VALOR"));
                }     
            }else{
                expediente.setCodigoTramiteCerrado(-1);
                expediente.setCodigoTramiteVisibleCerrado(-1);
                expediente.setOcurrenciaTramiteCerrado(-1);
                expediente.setDescripcionTramiteCerrado(null);
            }//if(codUltimoTramiteCerrado>0)
        }catch(SQLException e){
            e.printStackTrace();
            log.error("Se ha producido un error recuperando los datos de un expediente para un interesado y procedimiento" + e.getMessage());
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
                log.error("Se ha producido un error cerrando el statement y el resultset " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDatosExpediente() : END");
        return expediente;
    }//getDatosExpediente
    
    
    
     /**
     * 
     * @param con Conexión de la BD
     * @param etiqueta Estructura que trae los valores de la etiqueta necesarios para construir la clausula Sql
     * @param numExpediente Valor de uno de los tres  Campos que puede ir en la clausula Where
     * @param codTramite Valor de uno de los tres Campos que pueden ir en la clausula Where
     * @param ocurTramite Valor de uno de los tres Campos que pueden ir en la clausula Where
     * @return String. El valor de la etiqueta
     * @throws SQLException 
     */
      public String getValorDeEtiqueta(Connection con, EstructuraEtiquetaModuloIntegracionVO etiqueta,
              String numExpediente, String codTramite, String ocuTramite)throws SQLException {
        String valor="";
        log.debug("getValoreDeEtiqueta.BEGIN:");  
        log.debug("getValoreDeEtiqueta.Columna:"+ etiqueta.getNombreColumna());
        log.debug("getValoreDeEtiqueta.Num expediente:"+numExpediente);
        log.debug("getValoreDeEtiqueta.Cod Trámite:"+codTramite);
        log.debug("getValoreDeEtiqueta.Ocu Trámite:"+ocuTramite);

       Statement st = null;
       ResultSet rs = null;
       Date valorDate=null;
       
       
       String sql=etiqueta.getSqlS();
       log.debug("Valor de sql en el DAO:"+sql);
       String clausulaWhere=etiqueta.getWhereS();
       log.debug("Valor de clausulaWhere en el DAO:"+clausulaWhere);
       String campoWhere=etiqueta.getCampoWhere();
       log.debug("Valor de campoWhere en el DAO:"+ campoWhere);
       String campoAnd1=etiqueta.getCampoAnd1();
       log.debug("Valor de campoAnd1 en el DAO:"+campoAnd1);
       String campoAnd2=etiqueta.getCampoAnd2();
       log.debug("Valor de campoAnd2 en el DAO:"+campoAnd2);
       String columna=etiqueta.getNombreColumna();
       log.debug("Valor de columna en el DAO:"+columna);
       Integer tipo=etiqueta.getTipoEtiqueta();
       log.debug("Valor de tipo en el DAO:"+tipo);
       
       
       String clausulaAnd1=etiqueta.getAnd1();
       log.debug("Valor de clausulaAnd1:"+clausulaAnd1);
       String clausulaAnd2=etiqueta.getAnd2();
       log.debug("Valor de clausulaAnd2:"+clausulaAnd2);
       String clausulaAnd3=etiqueta.getAnd3();
       log.debug("Valor de clausulaAnd3:"+clausulaAnd3);
     
       String valorCampoAnd1="";
       String valorCampoAnd2="";
       //Para cuando tenemos un lista de resultados
       String valorAux=""; 
       //Hay que inicializar a nulo, por si el campo where viene a nulo
       String valorCampoWhere=null;
       //Determinamos que parámetro va en la clusula Where, si el numeroExpediente, codigoTramite, u ocurrenciaTramite
       if ("numeroExpediente".equals(campoWhere)){
           valorCampoWhere=numExpediente;
       }else if("codigoTramite".equals(campoWhere)){
           valorCampoWhere=codTramite;
       }else if("ocurrenciaTramite".equals(campoWhere)){
           valorCampoWhere=ocuTramite;
       }
       
       if ("numeroExpediente".equals(campoAnd1)){
           valorCampoAnd1=numExpediente;
       }else if("codigoTramite".equals(campoAnd1)){
           valorCampoAnd1=codTramite;
       }else if("ocurrenciaTramite".equals(campoAnd1)){
           valorCampoAnd1=ocuTramite;
       }
       if ("numeroExpediente".equals(campoAnd2)){
           valorCampoAnd2=numExpediente;
       }else if("codigoTramite".equals(campoAnd2)){
           valorCampoAnd2=codTramite;
       }else if("ocurrenciaTramite".equals(campoAnd2)){
           valorCampoAnd2=ocuTramite;
       }
       
       String sqlAEjecutar=sql;
       
       //Ahora vamos a construir la consulta 
       //Tendremos que ir mirando que clausulas existen y que campos...
       if((clausulaWhere!=null) && !("".equals(clausulaWhere))){
           
           sqlAEjecutar= sql+ " "+ clausulaWhere;
           
           if(valorCampoWhere!=null){
              sqlAEjecutar= sqlAEjecutar+   " '"+valorCampoWhere+"' " ;
           }
          
           if((clausulaAnd1!=null) && !("".equals(clausulaAnd1))){

              sqlAEjecutar=sqlAEjecutar+" "+clausulaAnd1; 
              
              if(valorCampoAnd1!=null){
                 sqlAEjecutar=sqlAEjecutar+" '"+valorCampoAnd1+"' "; 
              }
          
          }
          if((clausulaAnd2!=null) && !("".equals(clausulaAnd2))){

             sqlAEjecutar=sqlAEjecutar+ " "+clausulaAnd2; 
             
             if(valorCampoAnd2!=null){
                 sqlAEjecutar=sqlAEjecutar+" '"+valorCampoAnd2+"' "; 
              }

           }
           if((clausulaAnd3!=null) && !("".equals(clausulaAnd3))){

             sqlAEjecutar=sqlAEjecutar+" "+clausulaAnd3; 

           }
       }   
        try {            
           
            log.debug("getValoreDeEtiqueta.CONSULTA: "+sqlAEjecutar);

            st = con.createStatement();
            rs = st.executeQuery(sqlAEjecutar);
            
            //Si la etiqueta es de tipo fecha  necesitamos recuperarla de la bd como Date, si, se recuperase
            // como Strint, da muchos pbs al formatear
           if(3==tipo.intValue()){
               
               log.debug("El tipo de la etiqueta es tipo Fecha");
               if(rs.next()){
                 valorDate=rs.getDate(columna);
                 log.debug("La fecha(Date) recuperada de la BD es:"+valorDate);
               }
               
           } else{ //NO es de tipo fecha, así que recuperamos como String
          
              while(rs.next()){
                
                    valor= rs.getString(columna);
                    log.debug("El valor recuperado  de la BD es:"+valor);
                    if((valor==null) || ("null".equals(valor.trim()))){
                        valor="";
                         log.debug("El valor recuperado  de la BD era null, asi que ahora es..:"+valor);
                    }
                    // Por si tenemos  varios resultados, vamos a devolverlos en un String, y separados por ;
                    valorAux=valorAux+valor+";";
                }  
            }// while
            
            
            //Necesitamos formatear el valor devuelto
            // Si es de tipo numerico
             if(1==tipo.intValue()){ 
                String formato=etiqueta.getFormatoNumero(); 
                log.debug("Formato para el numero: "+formato);
                String salida = "";
                //El cero lo formatea mal
                //formato nunca debería ser null si el tipo es numérico
                //peeero, por si acaso  está mal el fichero de properties del módulo
                //hacemos la comprobación.
                if(formato!=null){  
                      if("0".equals(valor)){
                          salida="0";
                       }
                      else{
                          
                          if(valor!=null && !valor.equals("")){
                            Double num = new Double(valor);
                            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();            
                            DecimalFormat format = new DecimalFormat(formato,simbolo);            
                            salida = format.format(num);
                          }
                      }
                } 
                log.debug("O numero despois de ser formateado resulta ser:"+salida);    
                return salida;
            
            }
            //Si es tipo fecha, formateamos al formato indicado
            //Si no se recupera fecha de la B.D, valorDate es nulo 
            if(3==tipo.intValue() ){
                   
                  if(valorDate!=null){
                    String formato=etiqueta.getFormatoFecha();
                    log.debug("Formato para la fecha: "+formato);
                    String data="";
                    //Ejemplos de formato
                    //dd/MM/yyyy
                    
                    //Si recuperamos algun valor de formato
                    if(formato!=null && !"".equals(formato)){
                        
                        SimpleDateFormat sdf = new SimpleDateFormat(formato);
                        data=sdf.format(valorDate);
                      
                        log.debug("A data despois de formateada resulta ser:"+data);    
                        
                      }
                 
                   return data;
                  } 
               return "";   
            }
        
            
         
         
            
            
        //Si la etiqueta es una lista, eliminamos el ultimo ;
        if(4==tipo.intValue()){
           int tamanho=valorAux.length();
           if(tamanho>1) {tamanho=tamanho-1;}
           String valorDevolver=valorAux.substring(0,tamanho);
          
           log.debug("getValoresDeEtiquetas.END. Lista de valores devuelta:"+ valorDevolver); 
           return valorDevolver;         
        } 
          else{ 
            
            log.debug("getValoresDeEtiquetas.END. Valor devuelto:"+ valor); 
            return valor;
       }
         
        
        
        
            
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try{
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    
       
    

}
      
      
       /**
     * Recupera los valores de un determinado campo consulta desplegable
     * @param nombreTabla: nombre de la tabla de donde se recuperan los valroes
     * @param campoCodigo: nombre del campo que contiene los códigos del desplegable
     * @param campoDescripcion: nombre del campo que contiene las descripciones del desplegable
     * @param con: Conexión a la base de datos
     * @return ArrayList<ValorCampoDesplegableModuloIntegracionVO>
     */
    public ArrayList<ValorCampoDesplegableModuloIntegracionVO> getValoresCampoConsultaDesplegable
            (String nombreTabla, String campoCodigo, String campoDescripcion,Connection con) throws SQLException {
        
            
        
        log.debug("getValoresCampoConsultaDesplegable.BEGIN");
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> valores = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        Statement st = null;
        ResultSet rs = null;

        try {            
            String sql = "SELECT "+campoCodigo+ ","+ campoDescripcion + 
                         " FROM " +nombreTabla ;
            log.debug("getValoresCampoConsultaDesplegable.CONSULTA: "+sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                String codigo           = rs.getString(campoCodigo);
                String descripcion = rs.getString(campoDescripcion);
                ValorCampoDesplegableModuloIntegracionVO valor = new ValorCampoDesplegableModuloIntegracionVO();
                valor.setCodigo(codigo);
                log.debug("getValoresCampoConsultaDesplegable. Codigo: "+codigo);
                log.debug("getValoresCampoConsultaDesplegable. Descipcion: "+descripcion);
                valor.setDescripcion(descripcion);
                valores.add(valor);
            }// while
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try{
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        log.debug("getValoresCampoConsultaDesplegable.END");
        return valores;
    }

    
    /**
    * Recupera el contenido de la "extesion" de un documento xml
    * @param numExpediente Numero de expediente
    * @param con Conexion
    * @return String Contenido de la extensión
    */
    public byte[] getModulosExtensionXmlBuzonEntrada(String numExpediente,Connection con) throws SQLException {
            
        if(log.isDebugEnabled()){ log.debug("getExtensionDocumento.BEGIN");}

        byte[] extensionBytes=null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        String[] datos = numExpediente.split("/");
        
        try {
            boolean expHistorico = false;
            int salida = this.estaExpedienteHistorico(Integer.parseInt(datos[0]), numExpediente, con);
            
             // 0 --> Expediente activo
             // 1 --> Expediente historico
             // 2 --> Expediente no existe
            switch(salida) { 
                
                case 0: expHistorico = false;
                       break;                
                case 1: expHistorico = true;
                       break;                    
                default: expHistorico = false;
            }
            
            if (expHistorico)
                sql = "SELECT R.RED_DOC AS documento FROM R_RED R, HIST_E_EXR E ";
            else
                sql = "SELECT R.RED_DOC AS documento FROM R_RED R, E_EXR E ";
            
            sql += "WHERE R.RED_DEP = E.EXR_DEP AND R.RED_UOR = E.EXR_UOR AND R.RED_EJE = E.EXR_EJR"
                        + " AND R.RED_NUM = E.EXR_NRE AND R.RED_TIP = E.EXR_TIP "
                        + " AND E.EXR_NUM = '" + numExpediente + "' AND E.EXR_TOP = " + ConstantesDatos.REGISTRO_GENERA_EXPEDIENTE;
            
            log.debug("getExtensionDocumento.CONSULTA: "+sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                extensionBytes  = rs.getBytes("documento");
            }// while
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw sqle;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException(e);
        } finally {
            try{
                if (st != null) st.close();
                if (rs != null) rs.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        if(log.isDebugEnabled()){log.debug("getExtensionDocumento.END");}
        
        return extensionBytes;
    }
    
    
    /**
     * Comprueba si un expediente está activo, o está en el histórico
     * @param ejercicio: Ejercicio del expediente
     * @param numExpediente: Número del expedinete
     * @param con: Conexión a la BBDD
     * @return int que puede tomar los siguientes valores:
     *          0 --> Expediente activo
     *          1 --> Expediente historico
     *          2 --> Expediente no existe
     */
    public int estaExpedienteHistorico(int ejercicio,String numExpediente,Connection con) {
         int salida =2;
         PreparedStatement ps = null;
         ResultSet rs = null;
         String origen = null;
         int numero = 0;
         
         try{
             String sql = "SELECT COUNT(*) AS NUM,'ACTIVO' ORIGEN FROM E_EXP WHERE EXP_EJE=? AND EXP_NUM=? " + 
                          "UNION " + 
                          "SELECT COUNT(*) AS NUM,'HISTORICO' ORIGEN FROM HIST_E_EXP WHERE EXP_EJE=? AND EXP_NUM=?";
             log.debug(sql);
             ps = con.prepareStatement(sql);
             int i=1;
             ps.setInt(i++,ejercicio);
             ps.setString(i++,numExpediente);
             ps.setInt(i++,ejercicio);
             ps.setString(i++,numExpediente);
             
             rs = ps.executeQuery();             
             while(rs.next()) { 
                 numero = rs.getInt("NUM");
                 origen = rs.getString("ORIGEN");                 
                 
             }// while
             
             log.debug("numero: " + numero + ", origen: " + origen);
             if(origen==null) { 
                 salida = 2;
             }else
             if(origen!=null && origen.equals(ConstantesDatos.ACTIVO)){
                 salida = 0;                 
             } else
             if(origen!=null && origen.equals(ConstantesDatos.HISTORICO)){
                 salida = 1;
             }
             
         }catch(Exception e){             
             e.printStackTrace();
             
         } finally{
             try{
                 if(ps!=null) ps.close();
                 if(rs!=null) rs.close();
                 
             }catch(SQLException e){
                 e.printStackTrace();
             }
         }
         
        return salida;
    }
    
    /**
     * Pone a 0 el valor de un campo suplementarios de tipo numérico a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int borrarDatoNumericoTramite(CampoSuplementarioModuloIntegracionVO campo, Connection con) throws SQLException {
        int salida              = -1;
        String valor               = "0";

        try{
            campo.setValorNumero(valor);
            salida = setDatoNumericoTramite(campo, con);
        }catch(SQLException e){
            e.printStackTrace();
             throw e;
        }
        return salida;
    }
    
    /**
     * Pone a 0 el valor de un campo suplementarios de tipo numérico a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO     
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int borrarDatoNumericoExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        int salida = -1;
        String valor            = "0";
        
        try{            
            campo.setValorNumero(valor);
            salida = setDatoNumericoExpediente(campo, con);
           
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        return salida;
    }
    
    /**
     * Vacía el valor de un campo suplementarios de tipo texto a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int borrarDatoTextoExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        int salida = -1;
        String valor            = "";

        try{            
            campo.setValorTexto(valor);
            salida = setDatoTextoExpediente(campo, con);
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        return salida;
    }


    /**
     * Vacía el valor de un campo suplementarios de tipo texto corto a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int borrarDatoTextoTramite(CampoSuplementarioModuloIntegracionVO campo, Connection con) throws SQLException {
        int salida              = -1;
        String valor            = "";

        try{
            campo.setValorTexto(valor);
            salida = setDatoTextoTramite(campo, con);
        }catch(SQLException e){
            e.printStackTrace();
            salida = -1;
        }
        return salida;
    }
    
    /**
     * Vacía el valor de un campo suplementarios de tipo texto largo a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int borrarDatoTextoLargoExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        int salida = -1;
        String valor            = "";
        
        try{            
            campo.setValorTexto(valor);
            salida = setDatoTextoLargoExpediente(campo, con);
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        return salida;
    }


   /**
     * Vacía el valor de un campo suplementarios de tipo texto largo a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int borrarDatoTextoLargoTramite(CampoSuplementarioModuloIntegracionVO campo, Connection con) throws SQLException {
        int salida              = -1;
        String valor            = "";

        try{
            campo.setValorTexto(valor);
            salida = setDatoTextoLargoTramite(campo, con);
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        return salida;
    }
    
    /**
     * Borra el valor de un campo suplementarios de tipo texto largo a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int borrarDatoFechaExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int salida = -1;
        int numVal = 0;

        try{            
            String codOrganizacion  = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();
            String codProcedimiento = campo.getCodProcedimiento(); 
            String ejercicio        = campo.getEjercicio();
            
            String sql = "SELECT COUNT(*) AS NUM FROM E_PCA WHERE PCA_MUN=" + codOrganizacion + " AND PCA_PRO='" + codProcedimiento + "' " +
                         "AND PCA_COD='" + codCampo + "' AND PCA_ACTIVO='SI' AND PCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{
              sql = "SELECT COUNT(*) AS NUM FROM E_TFE WHERE TFE_MUN=" + codOrganizacion + " AND TFE_EJE=" + ejercicio +
                    " AND TFE_NUM='" + numExpediente + "' AND TFE_COD='" + codCampo + "'";
                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    numVal = rs.getInt("NUM");
                }
                st.close();
                rs.close();
                
              sql = "DELETE FROM E_TFE WHERE TFE_MUN=" + codOrganizacion + " AND TFE_EJE=" + ejercicio +
                    " AND TFE_NUM='" + numExpediente + "' AND TFE_COD='" + codCampo + "'";

              log.debug(sql);
              st = con.createStatement();
              int rowsDeleted = st.executeUpdate(sql);
              log.debug("Filas eliminadas: " + rowsDeleted);
              if(rowsDeleted==1 || numVal<=0) salida = 0;
            }

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


     /**
     * Borra el valor de un campo suplementarios de tipo texto largo a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int borrarDatoFechaTramite(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int salida = -1;
        int numVal = 0;

        try{            
            String codOrganizacion     = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();            
            String codProcedimiento = campo.getCodProcedimiento();
            String codTramite          = campo.getCodTramite();
            String ocurrencia          = campo.getOcurrenciaTramite();
            String ejercicio           = campo.getEjercicio();
            

            String sql = "SELECT COUNT(*) AS NUM FROM E_TCA WHERE TCA_MUN=" + codOrganizacion + " AND TCA_PRO='" + codProcedimiento + "' " +
                         "AND TCA_TRA=" + codTramite + " AND TCA_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;

            while(rs.next()){
                num = rs.getInt("NUM");
            }
            st.close();
            rs.close();

            if(num<=0){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{
                  sql = "SELECT COUNT(*) AS NUM FROM E_TFET WHERE TFET_MUN=" + codOrganizacion + " AND TFET_EJE=" + ejercicio +
                        " AND TFET_NUM='" + numExpediente + "' AND TFET_COD='" + codCampo + "' AND TFET_PRO='" + codProcedimiento + "'" +
                        " AND TFET_TRA=" + codTramite + "AND TFET_OCU=" + ocurrencia;
                  log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    numVal = rs.getInt("NUM");
                }
                st.close();
                rs.close();
                    
                  sql = "DELETE FROM E_TFET WHERE TFET_MUN=" + codOrganizacion + " AND TFET_EJE=" + ejercicio +
                        " AND TFET_NUM='" + numExpediente + "' AND TFET_COD='" + codCampo + "' AND TFET_PRO='" + codProcedimiento + "'" +
                        " AND TFET_TRA=" + codTramite + "AND TFET_OCU=" + ocurrencia;

                  log.debug(sql);
                  st = con.createStatement();
                  int rowsDeleted = st.executeUpdate(sql);
                  log.debug("Filas eliminadas: " + rowsDeleted);
                  if(rowsDeleted==1 || numVal<=0) salida = 0;
                
            }// else
           
        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return salida;
    }
    
    /**
     * Borra el valor de un campo suplementarios de tipo desplegable a nivel de expediente
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo
     *             -1 -> Error durante el acceso a la base de datos
     */
    public int borrarDatoDesplegableExpediente(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int salida = -1;
        int num = 0;

        try{            
            String codOrganizacion  = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();
            String codProcedimiento = campo.getCodProcedimiento();            
            String ejercicio        = campo.getEjercicio();

            String sql = "SELECT PCA_DESPLEGABLE FROM E_PCA WHERE PCA_MUN=" + codOrganizacion + " AND PCA_PRO='" + codProcedimiento + "' " +
                         "AND PCA_COD='" + codCampo + "' AND PCA_ACTIVO='SI' AND PCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            String codDesplegable = null;
            while(rs.next()){
                codDesplegable = rs.getString("PCA_DESPLEGABLE");
            }
            st.close();
            rs.close();

            if(codDesplegable==null || "".equals(codDesplegable) || "null".equals(codDesplegable)){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{
                sql = "SELECT COUNT(*) AS NUM FROM E_TDE WHERE TDE_MUN=" + codOrganizacion + " AND TDE_EJE=" + ejercicio +
                      " AND TDE_NUM='" + numExpediente + "' AND TDE_COD='" + codCampo + "'";
                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                
                while(rs.next()){
                    num = rs.getInt("NUM");
                }
                st.close();
                rs.close();
                
                sql = "DELETE FROM E_TDE WHERE TDE_MUN=" + codOrganizacion + " AND TDE_EJE=" + ejercicio +
                      " AND TDE_NUM='" + numExpediente + "' AND TDE_COD='" + codCampo + "'";
                log.debug(sql);

                st = con.createStatement();
                int rowsDeleted = st.executeUpdate(sql);
                log.debug("Filas eliminadas: " + rowsDeleted);
                if(rowsDeleted==1 || num<=0) salida = 0;
           }          

        }catch(SQLException e){
            e.printStackTrace();
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
    }


   /**
     * Borra el valor de un campo suplementarios de tipo desplegable a nivel de trámite
     * @param campo: CampoSuplementarioModuloIntegracionVO
     * @param con: Conexión a la base de datos
     * @return int: 0 -> Operación OK
     *              1 -> No existe el campo     
     *              3 -> El valor no se corresponde con uno de los valores del campo desplegable
     */
    public int borrarDatoDesplegableTramite(CampoSuplementarioModuloIntegracionVO campo,Connection con) throws SQLException{
        Statement st = null;
        ResultSet rs = null;        
        int salida = -1;
        int num = 0;

        try{            
            String codOrganizacion  = campo.getCodOrganizacion();
            String codCampo         = campo.getCodigoCampo();
            String numExpediente    = campo.getNumExpediente();
            String codTramite       = campo.getCodTramite();
            String ocurrencia       = campo.getOcurrenciaTramite();
            String codProcedimiento = campo.getCodProcedimiento();
            String ejercicio        = campo.getEjercicio();
            
            String sql = "SELECT TCA_DESPLEGABLE FROM E_TCA WHERE TCA_MUN=" + codOrganizacion + " AND TCA_PRO='" + codProcedimiento + "' " +
                         "AND TCA_TRA=" + codTramite + " AND TCA_COD='" + codCampo + "' AND TCA_ACTIVO='SI' AND TCA_TDA=" + IModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE;;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            String codDesplegable = null;
            while(rs.next()){                
                codDesplegable = rs.getString("TCA_DESPLEGABLE");
            }
            
            st.close();
            rs.close();

            if(codDesplegable==null || "".equals(codDesplegable) || "null".equals(codDesplegable)){
                salida = 1; // No existe el campo suplementario asociado al trámite
            }else{
                sql = "SELECT COUNT(*) AS NUM FROM E_TDET WHERE TDET_MUN=" + codOrganizacion + " AND TDET_EJE=" + ejercicio +
                      " AND TDET_NUM='" + numExpediente + "' AND TDET_COD='" + codCampo + "' AND TDET_PRO='" + codProcedimiento + "' " +
                      " AND TDET_TRA=" + codTramite + " AND TDET_OCU=" + ocurrencia;
                log.debug(sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                
                while(rs.next()){
                    num = rs.getInt("NUM");
                }
                st.close();
                rs.close();
                
                sql = "DELETE FROM E_TDET WHERE TDET_MUN=" + codOrganizacion + " AND TDET_EJE=" + ejercicio +
                      " AND TDET_NUM='" + numExpediente + "' AND TDET_COD='" + codCampo + "' AND TDET_PRO='" + codProcedimiento + "' " +
                      " AND TDET_TRA=" + codTramite + " AND TDET_OCU=" + ocurrencia;
                log.debug(sql);

                st = con.createStatement();
                int rowsDeleted = st.executeUpdate(sql);
                log.debug("Filas eliminadas: " + rowsDeleted);
                if(rowsDeleted==1 || num<=0) salida = 0;
            }

        }catch(SQLException e){
            e.printStackTrace();            
            throw e;
        }finally{
            try{
                if(st!=null) st.close();                
                if(rs!=null) rs.close();

            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        return salida;
  }
      
}//class