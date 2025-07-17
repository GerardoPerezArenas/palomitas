
package es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario;

import es.altia.agora.business.administracion.ParametrosBDVO;
import es.altia.agora.business.administracion.mantenimiento.persistence.OrganizacionesManager;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.util.Utilities;
import es.altia.flexia.business.documentospresentados.persistence.ExpedienteDocPresentadoManager;
import es.altia.flexia.historico.expedientes.dao.ExpedienteDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual.DireccionIntegracionModuloExternoDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual.ExpedienteIntegracionModuloExternoDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual.ModuloIntegracionExternoDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual.TerceroIntegracionModuloExternoDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.manual.TramiteModuloIntegracionExternoDAO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DocumentoExternoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DocumentoTramitacionModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DomicilioInteresadoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TerceroModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TramiteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.util.UtilitiesModuloIntegracion;
import es.altia.technical.PortableContext;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.SortedMap;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.pentaho.reporting.engine.classic.core.layout.output.PreparedCrosstabLayout;

/**
  * Clase que pueden utilizar los módulos de integración externos para obtener los valores de los campos suplementarios tanto a nivel
  * de expediente como de trámite.  
  */
public class ModuloIntegracionExternoCamposFlexia extends IModuloIntegracionExternoCamposFlexia{

    
    private final String PROPIEDAD_CON_JNDI     = "CON.jndi";
    private final String PROPIEDAD_CON_GESTOR   = "CON.gestor";
    private final String FICHERO_CONFIGURACION  = "techserver";    
    private Logger log = Logger.getLogger(ModuloIntegracionExternoCamposFlexia.class);

   /**
     * Recupera el campo suplementario de un expediente
     * @param codOrganizacion: Código del organización o municipio
     * @param ejercicio: ejercicio
     * @param numExpediente: Número del expediente
     * @param codCampo: Código del campo suplementario
     * @param TIPO_CAMPO: Tipo de campo suplementario. Se corresponde con alguno de los atributos
     * @return
     */
    public SalidaIntegracionVO getCampoSuplementarioExpediente(String codOrganizacion,String ejercicio,String numExpediente,String codProcedimiento,String codCampo,int TIPO_CAMPO){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try{
            // Se recupera el JNDI correspondiente a la organización
            params = this.getParams(Integer.parseInt(codOrganizacion));
            adapt  = new AdaptadorSQLBD(params);
            log.debug("getCampoSuplementarioExpediente params " + params);
            con    = adapt.getConnection();
            salida = ModuloIntegracionExternoDAO.getInstance().getCampoSuplementarioExpediente(codOrganizacion, ejercicio, numExpediente, codProcedimiento, codCampo, TIPO_CAMPO, con, params);
        }catch(BDException e){
            e.printStackTrace();                        
            salida.setStatus(-2);
            salida.setDescStatus("NO SE HA PODIDO ESTABLECER CONEXION CON LA BASE DE DATOS");
            salida.setCampoSuplementario(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);

        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }

        return salida;
    }
	

    public SalidaIntegracionVO getCampoSuplementarioTramite(String codOrganizacion,String ejercicio,String numExpediente, String codProcedimiento, int codTramite, int ocurrenciaTramite, String codCampo, int TIPO_CAMPO){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            // Se recupera el JNDI correspondiente a la organización
            params = this.getParams(Integer.parseInt(codOrganizacion));
            adapt  = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            salida = ModuloIntegracionExternoDAO.getInstance().getCampoSuplementarioTramite(codOrganizacion, ejercicio, numExpediente, codProcedimiento, codCampo, codTramite, ocurrenciaTramite,TIPO_CAMPO, con, params);
        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE HA PODIDO ESTABLECER CONEXION CON LA BASE DE DATOS");
            salida.setCampoSuplementario(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);

        }finally{

            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;
    }
    
    /**
     * Recupera el campo suplementario de un tercero
     * @param codOrganizacion
     * @param codTercero
     * @param codCampo
     * @param TIPO_CAMPO
     * @return 
     */
    public SalidaIntegracionVO getCampoSuplementarioTercero(String codOrganizacion, Integer codTercero, String codCampo, int TIPO_CAMPO){
        if(log.isDebugEnabled()) log.debug(("getCampoSuplementarioTercero() : BEGIN"));
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            // Se recupera el JNDI correspondiente a la organización
            params = this.getParams(Integer.parseInt(codOrganizacion));
            adapt  = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            salida = ModuloIntegracionExternoDAO.getInstance().getCampoSuplementarioTercero(codOrganizacion, codTercero, codCampo, TIPO_CAMPO, con);
        }catch(BDException e){
            log.error("Se ha producido un error recuperando el campo suplementario del tercero " + e.getMessage());
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE HA PODIDO ESTABLECER CONEXION CON LA BASE DE DATOS");
            salida.setCampoSuplementario(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setCampoSuplementarioTercero(null);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la base de datos " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug(("getCampoSuplementarioTercero() : END"));
        return salida;
    }//getCampoSuplementarioTercero


    /**
     * Devuelve los parámetros de conexión a base de datos para una determinada organización
     * @param codOrganizacion: Código de la organización
     * @return String[]
     */
    private String[] getParams(int codOrganizacion){

        String[] salida = null;
        String jndi = null;
        ResourceBundle config = ResourceBundle.getBundle(FICHERO_CONFIGURACION);
        String gestor = config.getString(PROPIEDAD_CON_GESTOR);
        
        
        synchronized (this) {
            SortedMap<ArrayList<String>, ParametrosBDVO> listaParametrosBD = (SortedMap<ArrayList<String>, ParametrosBDVO>) CacheDatosFactoria.getImplParametrosBD().getDatos();

            for (Map.Entry<ArrayList<String>, ParametrosBDVO> entry : listaParametrosBD.entrySet()) {
                ParametrosBDVO parametrosBD = entry.getValue();
                if (parametrosBD.getCodOrganizacion() == codOrganizacion
                        && parametrosBD.getCodAplicacion() == ConstantesDatos.APP_GESTION_EXPEDIENTES) {
                    jndi = parametrosBD.getJndi();
                    break;
                }
            }
            if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
                salida = new String[7];
                salida[0] = gestor;
                salida[1] = "";
                salida[2] = "";
                salida[3] = "";
                salida[4] = "";
                salida[5] = "";
                salida[6] = jndi;
            }
        }//synchronized


        log.debug("parametros.length: " + salida.length);
        log.debug(" getJndi <========= ");

        return salida;
    } 
    
    
    

    /**
     * Comprueba si no se ha pasado el valor el alguno de los atributos del objeto de la clase CampoSuplementarioModuloIntegracionVO y
     * que puedan ser necesarios para poder grabar el valor para un campo suplementario
     * @param campo: Objeto de la clase CampoSuplementarioModuloIntegracionVO
     * @return String con el mensaje de error a devolver al módulo externo
     */
    private String validarParametrosGrabacionCampoSuplementario(CampoSuplementarioModuloIntegracionVO campo){
        String mensaje = null;

        ResourceBundle bundle = ResourceBundle.getBundle(ConstantesDatos.FICHERO_CONFIGURACION_MODULOS_INTEGRACION);
        StringBuffer msgError = new StringBuffer();
        String obligatorio = null;
        String sufijo = "";
        int contador=0;

        if(campo.isTramite()) // Campo suplementario de trámite
            sufijo = ConstantesDatos.SUFIJO_TRAMITE;
        else // campo suplementario de expediente
            sufijo = ConstantesDatos.SUFIJO_EXPEDIENTE;

        boolean tipoCampoCorrecto = true;
        switch(campo.getTipoCampo()){
            
            case ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO:{
                obligatorio= bundle.getString(ConstantesDatos.OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_NUMERICO + sufijo);
                break;
            }

            case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO:{
               obligatorio= bundle.getString(ConstantesDatos.OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_TEXTO + sufijo);
               break;
            }

            case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO:{
               obligatorio= bundle.getString(ConstantesDatos.OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_TEXTO_LARGO + sufijo);
               break;
            }

            case ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA:{
               obligatorio= bundle.getString(ConstantesDatos.OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_FECHA + sufijo);
               break;
            }

            case ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO:{
               obligatorio= bundle.getString(ConstantesDatos.OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_FICHERO + sufijo);
               break;
            }

            case ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE:{
              obligatorio= bundle.getString(ConstantesDatos.OPERACIONES_OBLIGATORIAS_GRABACION_CAMPO_DESPLEGABLE + sufijo);
               break;
            }

            default : tipoCampoCorrecto = false;
                break;

        }// switch

        if(tipoCampoCorrecto){

            log.debug("obligatorio: " + obligatorio);

            // Se comprueba si todas las operaciones obligatorias devuelven un valor, si no lo devuelve es que no faltan datos entre los parámetros de entrada
            if(obligatorio!=null){
               String[] datos = obligatorio.split(ConstantesDatos.COMMA);
               for(int i=0;datos!=null && i<datos.length;i++){
                   Class[] tipoParametros = null;
                   Object[] valores = null;

                   log.debug("Probando la ejecución del método " + datos[i] + " de la clase " + campo.getClass().getName());
                   Object valor = ModuloIntegracionExternoFactoria.getInstance().ejecutarMetodo(campo, datos[i].trim(), tipoParametros, valores);
                   if(valor!=null && !"".equals(valor)){
                        contador++;
                   }else{
                        msgError.append(datos[i]);
                        msgError.append(ConstantesDatos.COMMA_SIMPLE);
                   }
               }// for

               if(contador==datos.length)
                   mensaje = null;
               else
                   mensaje = msgError.toString();
            } // if
        }else{
            mensaje = "getTipoCampo";
        }

        return mensaje;
    }


    /**
     * Graba un valor para un campo suplementario que bien puede ser de un trámite o de un expediente
     * @param campo: Objeto con la información relevante del campo
     * @return SalidaGrabacionCampoSuplementarioVO
     */
    public SalidaIntegracionVO grabarCampoSuplementario(CampoSuplementarioModuloIntegracionVO campo){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
       
        String error = validarParametrosGrabacionCampoSuplementario(campo);
        if(error!=null){
            // Errores en el parámetro/s de la operación
            salida.setStatus(-4);
            salida.setDescStatus("LOS SIGUIENTES MÉTODOS DEL OBJETO CampoSuplementarioModuloIntegracionVO NO DEVUELVEN VALORES: " + error);
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);

        }else{
            try{
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(campo.getCodOrganizacion()));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                adapt.inicioTransaccion(con);
                
                int tipoCampo     = campo.getTipoCampo();
                boolean isTramite = campo.isTramite();
                int codigoError   = -1;
                switch(tipoCampo){

                    case ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO:{
                        if(isTramite) // Grabación de campo suplementario de trámite
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoNumericoTramite(campo, con);
                        else // Grabación de campo suplementario de expediente
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoNumericoExpediente(campo, con);
                        break;
                    }

                    case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO:{
                        if(isTramite) // Grabación de campo suplementario de trámite
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoTextoTramite(campo, con);
                        else // Grabación de campo suplementario de expediente
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoTextoExpediente(campo, con);
                        break;
                    }

                    case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO:{
                        if(isTramite) // Grabación de campo suplementario de trámite
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoTextoLargoTramite(campo, con);
                        else // Grabación de campo suplementario de expediente
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoTextoLargoExpediente(campo, con);
                        break;
                    }

                    case ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA:{
                        if(isTramite) // Grabación de campo suplementario de trámite
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoFechaTramite(campo, con);
                        else // Grabación de campo suplementario de expediente
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoFechaExpediente(campo, con);
                        break;
                    }

                    case ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO:{
                        if(isTramite) // Grabación de campo suplementario de trámite
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoFicheroTramite(campo, con);
                        else // Grabación de campo suplementario de expediente
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoFicheroExpediente(campo, con);
                        
                        break;
                    }

                    case ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE:{
                        if(isTramite) // Grabación de campo suplementario de trámite
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoDesplegableTramite(campo, con);
                        else // Grabación de campo suplementario de expediente
                            codigoError = ModuloIntegracionExternoDAO.getInstance().setDatoDesplegableExpediente(campo, con);
                        break;
                    }
                }// switch

                salida.setStatus(codigoError);

                if(codigoError==0){
                    salida.setDescStatus("OK");
                }else
                if(codigoError==1){
                    salida.setDescStatus("NO EXISTE EL CAMPO");
                }else                
                if(codigoError==3){
                    salida.setDescStatus("EL VALOR NO SE CORRESPONDE CON UNO DE LOS VALORES VÁLIDOS DEL CAMPO DESPLEGABLE");
                }


                adapt.finTransaccion(con);
            }
            catch(BDException e){
                try{
                    salida = null;
                    salida = new SalidaIntegracionVO();
                    salida.setStatus(-2);
                    salida.setDescStatus("NO SE PUEDE OBTENER CONEXIÓN CON LA BASE DE DATOS");
                    adapt.rollBack(con);
                    e.printStackTrace();

                }catch(BDException f){
                    e.printStackTrace();
                }
            }catch(SQLException e){
                try{
                    salida = null;
                    salida = new SalidaIntegracionVO();
                    salida.setStatus(-1);
                    salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
                    adapt.rollBack(con);                    

                }catch(BDException f){
                    e.printStackTrace();
                }            
            }catch(Exception e){
                try{
                    salida = null;
                    salida = new SalidaIntegracionVO();
                    salida.setStatus(-1);
                    salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
                    adapt.rollBack(con);                    
                }catch(BDException f){
                    e.printStackTrace();
                }
            }
            finally{
                try{
                    adapt.devolverConexion(con);
                }catch(BDException e){
                    log.debug("Error al cerrar la conexión a la base de datos");
                    e.printStackTrace();
                }
            }
        }
        return salida;
    }
    
    /**
     * Elimina el valor de un campo suplementario
     * @param campo
     * @return 
     */
    @Override
    public SalidaIntegracionVO eliminarValorCampoSuplementario(CampoSuplementarioModuloIntegracionVO campo) {
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
       
        try{
            // Se recupera el JNDI correspondiente a la organización
            params = this.getParams(Integer.parseInt(campo.getCodOrganizacion()));
            adapt  = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            int tipoCampo     = campo.getTipoCampo();
            boolean isTramite = campo.isTramite();
            int codigoError   = -1;
            switch(tipoCampo){

                case ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO:{
                    if(isTramite) // Grabación de campo suplementario de trámite
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoNumericoTramite(campo, con);
                    else // Grabación de campo suplementario de expediente
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoNumericoExpediente(campo, con);
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO:{
                    if(isTramite) // Grabación de campo suplementario de trámite
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoTextoTramite(campo, con);
                    else // Grabación de campo suplementario de expediente
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoTextoExpediente(campo, con);
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO:{
                    if(isTramite) // Grabación de campo suplementario de trámite
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoTextoLargoTramite(campo, con);
                    else // Grabación de campo suplementario de expediente
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoTextoLargoExpediente(campo, con);
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA:{
                    if(isTramite) // Grabación de campo suplementario de trámite
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoFechaTramite(campo, con);
                    else // Grabación de campo suplementario de expediente
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoFechaExpediente(campo, con);
                    break;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_FICHERO:{
                    // No se ha implementado el borrado de campos de tipo fichero
                    codigoError = 4;
                }

                case ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE:{
                    if(isTramite) // Grabación de campo suplementario de trámite
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoDesplegableTramite(campo, con);
                    else // Grabación de campo suplementario de expediente
                        codigoError = ModuloIntegracionExternoDAO.getInstance().borrarDatoDesplegableExpediente(campo, con);
                    break;
                }
            }// switch

            salida.setStatus(codigoError);

            if(codigoError==0){
                salida.setDescStatus("OK");
            }else
            if(codigoError==1){
                salida.setDescStatus("NO EXISTE EL CAMPO");
            }else                
            if(codigoError==3){
                salida.setDescStatus("EL VALOR NO SE CORRESPONDE CON UNO DE LOS VALORES VÁLIDOS DEL CAMPO DESPLEGABLE");
            }else                
            if(codigoError==4){
                salida.setDescStatus("NO SE PUEDE BORRAR UN CAMPO SUPLEMENTARIO DE TIPO FICHERO");
            }


            adapt.finTransaccion(con);
        }
        catch(BDException e){
            try{
                salida = null;
                salida = new SalidaIntegracionVO();
                salida.setStatus(-2);
                salida.setDescStatus("NO SE PUEDE OBTENER CONEXIÓN CON LA BASE DE DATOS");
                adapt.rollBack(con);
                e.printStackTrace();

            }catch(BDException f){
                e.printStackTrace();
            }
        }catch(SQLException e){
            try{
                salida = null;
                salida = new SalidaIntegracionVO();
                salida.setStatus(-1);
                salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
                adapt.rollBack(con);                    

            }catch(BDException f){
                e.printStackTrace();
            }            
        }catch(Exception e){
            try{
                salida = null;
                salida = new SalidaIntegracionVO();
                salida.setStatus(-1);
                salida.setDescStatus("ERROR DURANTE EL ACCESO A LA BASE DE DATOS");
                adapt.rollBack(con);                    
            }catch(BDException f){
                e.printStackTrace();
            }
        }
        finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;
    }

    /**
     * Recupera un campo desplegable determinado con sus correspondientes valores
     * @param codOrganizacion: Código de la organización/municipio
     * @param codCampo: Código del campo     
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getCampoDesplegable(String codOrganizacion,String codCampo){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            // Se recupera el JNDI correspondiente a la organización
            params  = this.getParams(Integer.parseInt(codOrganizacion));
            adapt   = new AdaptadorSQLBD(params);
            con     = adapt.getConnection();
            
            salida = ModuloIntegracionExternoDAO.getInstance().getCampoDesplegable(codOrganizacion, codCampo, con);
            
        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }

        return salida;
    }


    /**
     * Recupera los campos desplegables de una organización.
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento en el caso de que sólo se desee recuperar aquellos campos suplementarios
     * a nivel de expediente que estén activos, si está a null, entonces recupera todos los campos desplegables
     * @param recuperarValores: Si está a true se indica que se recuperan también los valores de cada campo suplementarios, si los tiene.
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getCamposDesplegables(String codOrganizacion,String codProcedimiento,boolean recuperarValores){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{

            if(codOrganizacion==null || "".equals(codOrganizacion)){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
            }else{
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                salida = ModuloIntegracionExternoDAO.getInstance().getCamposDesplegables(codOrganizacion, codProcedimiento, recuperarValores, con);
            }

        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;
    }



   /**
     * Recupera los campos desplegables de una organización a nivel de trámite.
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @param codTramite: Código del trámite 
     * @param recuperarValores: Si está a true se indica que se recuperan también los valores de cada campo desplegable, si los tiene.
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getCamposDesplegables(String codOrganizacion,String codProcedimiento,int codTramite,boolean recuperarValores){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            
            if((codOrganizacion==null || "".equals(codOrganizacion)) || (codProcedimiento==null || codProcedimiento.length()==0) || codTramite<0){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
            }else{
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                salida = ModuloIntegracionExternoDAO.getInstance().getCamposDesplegables(codOrganizacion, codProcedimiento,codTramite,recuperarValores, con);
            }

        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;
    }

    /**
     * Recupera los expedientes relacionados de un determinado expediente. Se puede filtrar por cód. de procedimiento para recuperar sólo los
     * expedientes de un determinado procedimiento:
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento de los expedientes relacionados. Si está a null se recuperan todos los expedientes independientemente
     * del procedimiento al que pertenezcan
     * @param ejercicio: Ejercicio
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getExpedientesRelacionados(String codOrganizacion,String numExpediente,String codProcedimiento,String ejercicio){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{

            if((codOrganizacion==null || "".equals(codOrganizacion)) || (numExpediente==null || numExpediente.length()==0) || (ejercicio==null || ejercicio.length()==0)){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
            }else{
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();

                salida = ModuloIntegracionExternoDAO.getInstance().getExpedientesRelacionados(codOrganizacion,numExpediente,codProcedimiento,ejercicio,con);

            }

        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;
    }

    /**
     * Recupera la lista de documento de tramitación de una determinado ocurrencia de un trámite. Si no se indica la ocurrencia recupera los documentos de
     * tramitación de todas las ocurrencias del trámite. Los documentos se recuperan ordenados por fecha de modificación y de creación de forma descendente
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite. Si es <=0 entonces significa que se recupera los documentos de tramitación de todas las ocurrencias del trámite.
     * @param ejercicio: Ejercicio
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getListaDocumentosTramitacion(String codOrganizacion,String numExpediente,String codProcedimiento,int codTramite,int ocurrenciaTramite,String ejercicio){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{

            if((codOrganizacion==null || "".equals(codOrganizacion)) || (numExpediente==null || numExpediente.length()==0) || (ejercicio==null || ejercicio.length()==0) || codTramite<=0){
                salida.setStatus(-3);
                salida.setDescStatus("ERROR EN LOS PARÁMETROS DE LA OPERACIÓN");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
            }else{
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();

                salida = ModuloIntegracionExternoDAO.getInstance().getListaDocumentosTramitacion(codOrganizacion, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, ejercicio, con);
            }

        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;
    }


    /**
     * Recupera un determinado documento de tramitación. Tiene en cuenta que en Flexia el plugin de almacenamiento de documentos que esté activo en Flexia para
     * recuperar el contenido
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numeroDocumento: Número del documento
     * @param ejercicio: Ejercicio
     * @param nombreDocumento: Nombre del documento a visualizar
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getDocumentoTramitacion(String codOrganizacion,String numExpediente,String codProcedimiento,int codTramite,int ocurrenciaTramite,int numeroDocumento,String ejercicio,String nombreDocumento){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;              
        try{

            String extension = null;

            if((codOrganizacion==null || "".equals(codOrganizacion)) || (numExpediente==null || numExpediente.length()==0) || (codProcedimiento==null || "".equals(codProcedimiento))
                    || (ejercicio==null || "".equals(ejercicio)) || codTramite<=0 || ocurrenciaTramite<=0 || numeroDocumento<=0 || (nombreDocumento==null || "".equals(nombreDocumento))){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
                
            }else{            
                params = getParams(Integer.parseInt(codOrganizacion));
                log.debug("ENTRA POR AQUI");

                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",codOrganizacion);
                datos.put("numeroExpediente",numExpediente);
                datos.put("codTramite",Integer.toString(codTramite));
                datos.put("ocurrenciaTramite",Integer.toString(ocurrenciaTramite));
                datos.put("numeroDocumento",Integer.toString(numeroDocumento));
                // Se trata de documento de tramitación de expedientes que no pertenecen a una relación de expediente
                datos.put("perteneceRelacion","false");
                datos.put("params",params);      
                datos.put("expedienteHistorico","FALSE");
                
                boolean expedienteHistorico = false;
                Connection con = null;
                try{
                    AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
                    con = adapt.getConnection();
                    // 0 --> Expediente activo
                    // 1 --> Expediente historico
                    // 2 --> Expediente no existe
                    int sHistorico = ModuloIntegracionExternoDAO.getInstance().estaExpedienteHistorico(Integer.parseInt(ejercicio),numExpediente,con);
                    if(sHistorico==1) {
                        datos.put("expedienteHistorico","TRUE");                        
                    }                                            
                }catch(Exception e){
                    e.printStackTrace();                    
                }finally { 
                    try{
                        if(con!=null) con.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
                
                Documento doc = null;
                int tipoDocumento = -1;
                String tipoMime = null;

                if(getValorConfiguracion(ConstantesDatos.PROPIEDAD_EDITOR_PLANTILLAS,"common").equals(ConstantesDatos.OOFFICE)){
                    tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
                    datos.put("extension",ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE);
                    extension = ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE;
                }
                else{
                    tipoMime = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;
                    datos.put("extension",ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD);
                    extension = ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD;
                }

                if(!almacen.isPluginGestor())
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                else{
                    
                    // Se obtiene el nombre del documento a mostrar porque se necesita para el caso de que se venga de firmar el documento                   
                    datos.put("nombreDocumento",nombreDocumento);

                    String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(codOrganizacion,codProcedimiento, Integer.toString(codTramite), params);
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
                    String descripcionOrganizacion = OrganizacionesManager.getInstance().getDescripcionOrganizacion(codOrganizacion, params);
                    datos.put("codProcedimiento",codProcedimiento);
                    datos.put("codigoVisibleTramite",codigoVisibleTramite);                
                    datos.put("tipoMime",tipoMime);
                    datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);

                    /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/                    
                    String carpetaRaiz   = getValorConfiguracion(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ,"documentos");
                    
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrganizacion  + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);                    
                    listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));

                    datos.put("listaCarpetas",listaCarpetas);

                    /*** FIN  ***/
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;                    
                }// else

                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos,tipoDocumento);
                byte[] file = almacen.getDocumento(doc);
                log.debug("file: " + file);
                if(file!=null && file.length>0){
                    log.debug("Tamaño del documento recuperado: " + file.length);
                    DocumentoTramitacionModuloIntegracionVO dtmiVO = new DocumentoTramitacionModuloIntegracionVO();
                    dtmiVO.setCodMunicipio(Integer.parseInt(codOrganizacion));
                    dtmiVO.setCodProcedimiento(codProcedimiento);
                    dtmiVO.setCodTramite(codTramite);
                    dtmiVO.setOcuTramite(ocurrenciaTramite);
                    dtmiVO.setContenido(file);
                    dtmiVO.setExtensionDocumento(extension);
                    dtmiVO.setNombreDocumento(nombreDocumento);
                    dtmiVO.setExtensionDocumento(extension);
                    dtmiVO.setNumExpediente(numExpediente);
                    dtmiVO.setNumDocumento(numeroDocumento);

                    salida = new SalidaIntegracionVO();
                    salida.setStatus(0);
                    salida.setDescStatus("OK");
                    salida.setExpediente(null);
                    salida.setTramite(null);
                    salida.setCampoDesplegable(null);
                    salida.setCampoSuplementario(null);
                    salida.setCamposDesplegables(null);
                    salida.setExpedientesRelacionados(null);
                    salida.setListaDocumentosTramitacion(null);
                    salida.setDocumentoTramitacion(dtmiVO);
                }//if
                else{
                    salida = new SalidaIntegracionVO();
                    salida.setStatus(1);
                    salida.setDescStatus("NO EXISTE EL DOCUMENTO");
                    salida.setExpediente(null);
                    salida.setTramite(null);
                    salida.setCampoDesplegable(null);
                    salida.setCampoSuplementario(null);
                    salida.setCamposDesplegables(null);
                    salida.setExpedientesRelacionados(null);
                    salida.setListaDocumentosTramitacion(null);
                    salida.setDocumentoTramitacion(null);
                }
            }
        }
        catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            salida.setStatus(-1);
            salida.setDescStatus("NO SE PUEDE RECUPERAR EL DOCUMENTO");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
        }

        return salida;
    }


    public String getValorConfiguracion(String propiedad,String ficheroConfiguracion){
       String salida = null;
       try{
           ResourceBundle bundle = ResourceBundle.getBundle(ficheroConfiguracion);
           salida = bundle.getString(propiedad);

       }catch(Exception e){
           log.error("No se ha podido recuperar la propiedad " + propiedad + " del fichero de configuración " + ficheroConfiguracion);
       }

       return salida;
    }


    /**
     * Permite recuperar una determinada ocurrencia de un trámite
     * @param codOrganizacion: Código de la organización o municipio
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param ejercicio: Ejercicio
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getTramite(String codOrganizacion,String numExpediente,String codProcedimiento,int codTramite,int ocurrenciaTramite,String ejercicio){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{

            if((codOrganizacion==null || "".equals(codOrganizacion)) || (codProcedimiento==null || "".equals(codProcedimiento)) || (numExpediente==null || numExpediente.length()==0) || (ejercicio==null || ejercicio.length()==0) || codTramite<=0 || ocurrenciaTramite<=0){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
            }else{
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();

                salida = ModuloIntegracionExternoDAO.getInstance().getTramite(codOrganizacion, numExpediente, codProcedimiento, ejercicio, codTramite, ocurrenciaTramite, con);
            }

        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;
    }


   /**
     * Permite recuperar la información de un determinado expediente
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param codProcedimiento: Código del procedimiento     
     * @param ejercicio: Ejercicio
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getExpediente(String codOrganizacion,String numExpediente,String codProcedimiento,String ejercicio){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            if((codOrganizacion==null || "".equals(codOrganizacion)) || (codProcedimiento==null || "".equals(codProcedimiento)) || (numExpediente==null || numExpediente.length()==0) || (ejercicio==null || ejercicio.length()==0)){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
            }else{
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();

                salida = ModuloIntegracionExternoDAO.getInstance().getExpediente(codOrganizacion,numExpediente,codProcedimiento,ejercicio,con,params);
                
            }

        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
            
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;
    }
    
    public SalidaIntegracionVO getDocumentoPresentado(String codOrganizacion,String numExpediente,String codProcedimiento, String ejercicio, String codDocumento){
        if(log.isDebugEnabled()) log.debug("getDocumentoPresentado() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = getParams(Integer.valueOf(codOrganizacion));
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        byte[] fichero = null;
        String nombreFichero = null;
        String tipoContenido = null;
        Documento doc = null;

        // Obtener conexion
        try {
            adapt  = new AdaptadorSQLBD(params);
            con = adapt.getConnection(); 

            Hashtable<String,Object> datos = new Hashtable<String,Object>();
            datos.put("codDocumento",codDocumento);
            datos.put("codMunicipio",codOrganizacion);
            datos.put("ejercicio",ejercicio);
            datos.put("numeroExpediente",numExpediente);
            datos.put("params",params);
            datos.put("codProcedimiento",codProcedimiento);

            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion,codProcedimiento);
            int tipoDocumento = -1;
            if(!almacen.isPluginGestor()){
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
            }else{
                String nomOrg = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codOrganizacion),con);
                ResourceBundle bundle = ResourceBundle.getBundle("documentos");
                String carpetaRaiz = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);        
                datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);
                
                TraductorAplicacionBean traductor = new TraductorAplicacionBean();
                traductor.setApl_cod(4);
                traductor.setIdi_cod(Integer.valueOf("1"));
                /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + nomOrg);
                listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
                listaCarpetas.add(traductor.getDescripcion("carpetaDocumentosPresentados"));
                datos.put("listaCarpetas",listaCarpetas);
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
            }//if(!almacen.isPluginGestor())

            try{
                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
                doc.setExpHistorico((ExpedienteDAO.getInstance().getExpediente(Integer.parseInt(codOrganizacion),
                        Integer.parseInt(ejercicio),numExpediente,con) == null)?true:false);
                
                if(almacen.isPluginGestor()){
                    // Se recupera la información del documento de la base de datos para extraer la extensión del 
                    // fichero y componer el nombre del fichero para poder obtener su contenido del gestor
                    doc = ExpedienteDocPresentadoManager.getInstance().getDocumentoPresentado(doc);
                    log.debug(" ********* nombredocumento: " + doc.getNombreDocumento());
                    log.debug(" ********* extensiondocumento: " + doc.getExtension());
                    DocumentoGestor docGestor = (DocumentoGestor)doc;
                    docGestor.setNombreFicheroCompleto(codDocumento + ConstantesDatos.GUION + doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension());
                    doc = almacen.getDocumentoPresentado(docGestor);
                }else{
                    doc = almacen.getDocumentoPresentado(doc);
                }
                fichero             = doc.getFichero();
                nombreFichero = doc.getNombreDocumento() + "." + doc.getExtension();
                tipoContenido  = doc.getTipoMimeContenido();
                log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero longitud: " + fichero.length);
                log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero nombreFichero: " + nombreFichero);
                log.debug(" >>>>>>>>>>>>>>>>>>>>>>  fichero tipoContenido: " + tipoContenido);
            }catch(AlmacenDocumentoTramitacionException e){
                e.printStackTrace();    
                fichero             = null;
                nombreFichero = null;
                tipoContenido   = null;
            }//try-catch
        } catch (Exception e) {
            e.printStackTrace();    fichero             = null;
            nombreFichero = null;
            tipoContenido   = null;
        } finally {
            try {
                // Devolver la conexion
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Fallo al devolver la conexion");
                }
                e.printStackTrace();
            }
        }

        salida.setStatus(0);
        salida.setDescStatus("OK");
        salida.setExpediente(null);
        salida.setTramite(null);
        salida.setCampoDesplegable(null);
        salida.setCampoSuplementario(null);
        salida.setCamposDesplegables(null);
        salida.setExpedientesRelacionados(null);
        salida.setListaDocumentosTramitacion(null);
        salida.setDocumentoTramitacion(null);
        salida.setDocumentoInicioExpediente(doc);
        
        if(log.isDebugEnabled()) log.debug("getDocumentoPresentado() : END");
        return salida;
    }//getDocumentoPresentado

    
    
    
     /**
     * Permite recuperar el circuito de firmas definido para un determinado documento presentado de inicio de un determinado
     * procedimiento
     * @param codOrganizacion: Código de la organización     
     * @param codProcedimiento: Código del procedimiento     
     * @param codDocumento: Nombre del documento en Flexia
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getCircuitoFirmasDocumentoPresentado(String codOrganizacion,String codProcedimiento,String codDocumento){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{
            if((codOrganizacion==null || "".equals(codOrganizacion)) || (codProcedimiento==null || "".equals(codProcedimiento)) || (codDocumento==null || codDocumento.length()==0)){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
            }else{
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();

                ModuloIntegracionExternoDAO dao = ModuloIntegracionExternoDAO.getInstance();
                int codigo = dao.getCodigoDocumentoInicio(codOrganizacion, codProcedimiento, codDocumento, con);
                if(codigo==-1){
                    salida.setStatus(-3);
                    salida.setDescStatus("NO SE HA PODIDO RECUPERAR EL CÓDIGO DEL DOCUMENTO DE INICIO, PUEDE QUE NO EXISTA");
                    salida.setExpediente(null);
                    salida.setTramite(null);
                    salida.setCampoDesplegable(null);
                    salida.setCamposDesplegables(null);
                    salida.setDocumentoTramitacion(null);
                    salida.setExpedientesRelacionados(null);
                    salida.setListaDocumentosTramitacion(null);
                    salida.setCircuitoFirmasDocumento(null);
                    salida.setFirmasDocumentoPresentado(null);
                }else{
                    salida = dao.getCircuitoFirmasDocumento(Integer.parseInt(codOrganizacion),codProcedimiento,codigo, con);
                }                
            }

        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setCircuitoFirmasDocumento(null);
            salida.setFirmasDocumentoPresentado(null);
            
        }finally{
            try{
                if(adapt!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;
    }
    
    
    /**
     * Recupera las firmas de un documento de inicio 
     * @param codOrganizacion:: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @param numExpediente: Nº del expediente
     * @param ejercicio: Ejercicio
     * @param nombreDocumento: Nombre del documento
     * @return SalidaIntegracionVO
     */                               
    public SalidaIntegracionVO getFirmasDocumentoPresentado (String codOrganizacion,String codProcedimiento,String numExpediente,String ejercicio,String nombreDocumento){
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        try{

            if((codOrganizacion==null || "".equals(codOrganizacion)) || (codProcedimiento==null || "".equals(codProcedimiento)) || (numExpediente==null || numExpediente.length()==0) || (ejercicio==null || ejercicio.length()==0) || (nombreDocumento==null || nombreDocumento.length()==0)){
                
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
            }else{
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();

                ModuloIntegracionExternoDAO dao = ModuloIntegracionExternoDAO.getInstance();
                int codigo = dao.getCodigoDocumentoInicio(codOrganizacion, codProcedimiento, nombreDocumento, con);
                if(codigo==-1){
                    salida.setStatus(-3);
                    salida.setDescStatus("NO SE HA PODIDO RECUPERAR EL CÓDIGO DEL DOCUMENTO DE INICIO, PUEDE QUE NO EXISTA");
                    salida.setExpediente(null);
                    salida.setTramite(null);
                    salida.setCampoDesplegable(null);
                    salida.setCamposDesplegables(null);
                    salida.setDocumentoTramitacion(null);
                    salida.setExpedientesRelacionados(null);
                    salida.setListaDocumentosTramitacion(null);
                    salida.setCircuitoFirmasDocumento(null);
                    salida.setFirmasDocumentoPresentado(null);
                }else{
                    salida = dao.getFirmasDocumentoPresentado(codOrganizacion, codProcedimiento, ejercicio, numExpediente, codigo, con);
                }                
            }

        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setCircuitoFirmasDocumento(null);
            salida.setFirmasDocumentoPresentado(null);
            
        }finally{
            try{
                if(adapt!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return salida;        
        
    }
    
    /**
     * Metodo que recupera una unidad organica en funcion de su codigo visible
     * 
     * @param codOrganizacion
     * @param uorCodVis
     * @return 
     */
    public SalidaIntegracionVO getUOR(String codOrganizacion, String uorCodVis){
        if(log.isDebugEnabled()) log.debug("getUOR() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            if((codOrganizacion==null || "".equals(codOrganizacion)) || 
                    (uorCodVis == null || "".equalsIgnoreCase(uorCodVis))){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setUorModuloIntegracionVO(null);
            }else{
                if(log.isDebugEnabled()) log.debug("Recuperamos los datos para la conexion");
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                if(log.isDebugEnabled()) log.debug("Recuperamos una instancia de la clase ModuloIntegracionExternoDAO");
                ModuloIntegracionExternoDAO dao = ModuloIntegracionExternoDAO.getInstance();
                salida = dao.getUOR(codOrganizacion, uorCodVis, con);
            }/*if((codOrganizacion==null || "".equals(codOrganizacion)) || 
                    (uorCodVis == null || "".equalsIgnoreCase(uorCodVis)))*/
        }catch(BDException e){
            log.error("Se ha producido un error en la conexion a la BBDD " + e.getMessage());
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setUorModuloIntegracionVO(null);
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Cerramos la conexion a la BBDD");
                if(adapt!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUOR() : END");
        return salida;
    }//getUOR

    /**
     * Metodo que recupera un usuario por su identificador
     * 
     * @param codOrganizacion
     * @param idUsuario
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getUsuario(String codOrganizacion, String idUsuario){
        if(log.isDebugEnabled()) log.debug("getUsuario() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            if((codOrganizacion==null || "".equals(codOrganizacion)) || 
                    (idUsuario == null || "".equalsIgnoreCase(idUsuario))){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setUorModuloIntegracionVO(null);
            }else{
                if(log.isDebugEnabled()) log.debug("Recuperamos los datos para la conexion");
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                if(log.isDebugEnabled()) log.debug("Recuperamos una instancia de la clase ModuloIntegracionExternoDAO");
                ModuloIntegracionExternoDAO dao = ModuloIntegracionExternoDAO.getInstance();
                salida = dao.getUsuario(codOrganizacion, idUsuario, con);
            }/*if((codOrganizacion==null || "".equals(codOrganizacion)) || 
                    (idUsuario == null || "".equalsIgnoreCase(idUsuario)))*/
        }catch(BDException e){
            log.error("Se ha producido un error en la conexion a la BBDD " + e.getMessage());
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setUorModuloIntegracionVO(null);
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Cerramos la conexion a la BBDD");
                if(adapt!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUsuario() : END");
        return salida;
    }//getUsuario
    
    /**
     * Metodo que recupera una lista de usuarios con permisos sobre una UOR con el uorCodVis que enviamos como parametro
     * 
     * @param codOrganizacion
     * @param uorCodVis
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getUsuariosPermisoUOR(String codOrganizacion, String uorCodVis){
        if(log.isDebugEnabled()) log.debug("getUsuario() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            if((codOrganizacion==null || "".equals(codOrganizacion)) || 
                    (uorCodVis == null || "".equalsIgnoreCase(uorCodVis))){
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setUorModuloIntegracionVO(null);
            }else{
                if(log.isDebugEnabled()) log.debug("Recuperamos los datos para la conexion");
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                if(log.isDebugEnabled()) log.debug("Recuperamos una instancia de la clase ModuloIntegracionExternoDAO");
                ModuloIntegracionExternoDAO dao = ModuloIntegracionExternoDAO.getInstance();
                salida = dao.getUsuariosPermisoUOR(codOrganizacion, uorCodVis, con);
            }/*if((codOrganizacion==null || "".equals(codOrganizacion)) || 
                    (uorCodVis == null || "".equalsIgnoreCase(uorCodVis)))*/
        }catch(BDException e){
            log.error("Se ha producido un error en la conexion a la BBDD " + e.getMessage());
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setUorModuloIntegracionVO(null);
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Cerramos la conexion a la BBDD");
                if(adapt!=null) adapt.devolverConexion(con);
            }catch(BDException e){
                log.error("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }//try-catch
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUsuario() : END");
        return salida;
    }//getUsuario
    
    /**
     * Metodo que devuelve una lista de expedientes del procedimiento y para los interesados que se le pasan como parametro.
     * 
     * @param codOrganizacion
     * @param codPro
     * @param idDocumento
     * @param documento
     * @return SalidaIntegracionVO
     */
    public SalidaIntegracionVO getExpedientesByInteresadoAndProc (String codOrganizacion, String codPro, String idDocumento,
        String documento){
        if(log.isDebugEnabled()) log.debug("getExpedientesByInteresadoAndProc() : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            if((codOrganizacion==null || "".equals(codOrganizacion)) 
                    || (codPro==null || "".equals(codPro)) 
                    || (idDocumento==null || "".equals(idDocumento))
                    || (documento==null || "".equals(documento))){
                if(log.isDebugEnabled())  log.debug("Los parametros no son correctos");
                salida.setStatus(-3);
                salida.setDescStatus("Error en los parámetros de la operación");
                salida.setExpediente(null);
                salida.setTramite(null);
                salida.setCampoDesplegable(null);
                salida.setCamposDesplegables(null);
                salida.setDocumentoTramitacion(null);
                salida.setExpedientesRelacionados(null);
                salida.setListaDocumentosTramitacion(null);
                salida.setFirmasDocumentoPresentado(null);
                salida.setExpedientes(null);
            }else{
                if(log.isDebugEnabled())  log.debug("Procedemos a recuperar la informacion para el acceso a la BBDD");
                // Se recupera el JNDI correspondiente a la organización
                params = this.getParams(Integer.parseInt(codOrganizacion));
                adapt  = new AdaptadorSQLBD(params);
                con = adapt.getConnection();
                if(log.isDebugEnabled())  log.debug("Llamamos al metodo getExpedientesByInteresadoAndProc");
                salida = ModuloIntegracionExternoDAO.getInstance().
                        getExpedientesByInteresadoAndProc(codOrganizacion, codPro, idDocumento, documento, con, params);
            }/*if((codOrganizacion==null || "".equals(codOrganizacion)) 
                    || (codPro==null || "".equals(codPro)) 
                    || (idDocumento==null || "".equals(idDocumento))
                    || (documento==null || "".equals(documento)))*/
        }catch(BDException e){
            e.printStackTrace();
            log.error("Se ha producido un error recuperando los expedientes para un procedimiento e interesado");
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
            salida.setFirmasDocumentoPresentado(null);
            salida.setExpedientes(null);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getExpedientesByInteresadoAndProc() : END");
        return salida;
    }//getExpedientesByInteresadoAndProc

    /**
     * 
     * @param codOrganizacion
     * @param expediente
     * @param unidadInicioTramite puede venir a null
     * @return 
     */
    public  SalidaIntegracionVO iniciarExpediente(String codOrganizacion, ExpedienteModuloIntegracionVO expediente, String unidadInicioTramite){
       
       log.debug(" ****** IniciarExpediente:BEGIN");
       //Se llama al método privado getParams para obtener los parámetros de conexión
       //a la BD
       String[] paramsBD = null;
       paramsBD = this.getParams(Integer.parseInt(codOrganizacion));
       SalidaIntegracionVO resultado= new SalidaIntegracionVO();
       ExpedienteIntegracionModuloExternoDAO expedienteDAO = ExpedienteIntegracionModuloExternoDAO.getInstance();
       TramiteModuloIntegracionExternoDAO tramiteDAO = TramiteModuloIntegracionExternoDAO.getInstance();
       Connection con = null;
       AdaptadorSQLBD adapt = new AdaptadorSQLBD(paramsBD);;
       
       //Hay que comprobar el el atributo expediente trae cubiertos los atributos
       //obligatorios para poder iniciar el expediente
       
     try {  
         
         
         log.debug(" =====================> ModuloIntegracionExternoCamposFlexia.iniciarExpediente expediente: " + expediente);
         log.debug(" =====================> ModuloIntegracionExternoCamposFlexia.iniciarExpediente unidadInicioTramite: " + unidadInicioTramite);
       
         
         
         if(traeTodosLosCamposIniciarExpediente(expediente,unidadInicioTramite)){
             TramiteModuloIntegracionVO tramite= new TramiteModuloIntegracionVO();
             
             int numero=-1;
             String procedimiento=expediente.getCodProcedimiento();             
             con = adapt.getConnection(); 
             adapt.inicioTransaccion(con);
             
           
           //Es necesario comprobar que el usuario que inicia el 
           //expediente tiene permisos sobre la unidad que inicia el 
           //expediente.
           String unidadInicioVisible=expediente.getCodigoUorVisibleInicioExpediente();
           log.debug(" ============================= ModuloIntegracionExternoCamposFlexia.iniciarExpediente() unidadInicioVisibleExpediente:" + unidadInicioVisible);
           int unidadInicioExpediente=expedienteDAO.dameCodigoUorInterno(con,unidadInicioVisible);
           int usuario=expediente.getCodUsuarioIniciaExpediente();
           
           int codInternoUorInicioTramite = expedienteDAO.dameCodigoUorInterno(con,unidadInicioTramite);
           log.debug("La unidad tramitadora del trámite de inicio es: " + codInternoUorInicioTramite);
           
           if (expedienteDAO.tienePermiso(usuario,unidadInicioExpediente,Integer.parseInt(codOrganizacion),con)){
                int ejercicio= expediente.getEjercicio();

                 //Nos fumamos el Manager--> ojo con cerrar la conexión!
          
                //LLamamos al método esProcedimientoValido para comprobar si 
                //el procedimiento para el que se desea inicira el expediente es válido.

                     if(expedienteDAO.esProcedimientoValido(procedimiento, con)){

                         if(expedienteDAO.esUnidadInicioValida(procedimiento, unidadInicioExpediente, con)>0){                           
                             //Comprobamos que hai un trámite de inicio,
                             //y rellenamos el objeto tramite con el código del trámite
                             tramite = tramiteDAO.getTramitesInicio(expediente, con);
                             if (tramite.getCodTramite()>-1) {   
                                 //hai tramite de inicio
                                log.debug("-->idTramite  " +tramite.getCodTramite());  
                              
                              log.debug(" ============================= ModuloIntegracionExternoCamposFlexia.iniciarExpediente() unidadInicioTramite:" + unidadInicioTramite + ", unidadInicioExpediente: " + unidadInicioExpediente);
                              if (comprobarTramite(Integer.parseInt(codOrganizacion),
                                 tramite.getCodTramite(), procedimiento,unidadInicioExpediente,codInternoUorInicioTramite,tramiteDAO,con)){
  
                                    log.debug(" ****** IniciarExpediente.Despues de todas las comprobaciones:");
                                      //Se llama al método generarNumeroExpediente  
                                    numero=expedienteDAO.generarNumeroExpediente(procedimiento, codOrganizacion,ejercicio, con);
                                    String numExpediente=dameNumeroExpediente(numero,expediente.getEjercicio(),expediente.getCodProcedimiento());
                                    expediente.setNumExpediente(numExpediente);
                                    expedienteDAO.crearExpediente(expediente,Integer.parseInt(codOrganizacion),con,adapt);  
                                    tramiteDAO.crearTramite(expediente, tramite, unidadInicioExpediente,Integer.parseInt(codOrganizacion),  con, adapt);
                                    tramite.setOcurrenciaTramite(1);
                                    log.debug("-->Tramite creado  ");
                                    //Todo se ha ejecutado correctamente 
                                    adapt.finTransaccion(con);
                                    adapt.devolverConexion(con);
                                    log.debug("TODO OK.");
                                    resultado.setTramite(tramite);
                                    resultado.setExpediente(expediente);
                                    resultado.setStatus(0);
                                    resultado.setDescStatus("OK");
                                   log.debug(" ****** IniciarExpediente.Fin del proceso.");
                            
                              }else{ //comprobarTramite viene a False
                                    adapt.rollBack(con);
                                    log.debug("LA UNIDAD TRAMITADORA DEL TRAMITE DE INICIO NO ES CORRECTA");
                                    resultado.setExpediente(null);
                                    resultado.setTramite(null);
                                    resultado.setStatus(9);
                                    resultado.setDescStatus("LA UNIDAD TRAMITADORA DEL TRAMITE DE INICIO NO ES CORRECTA");
                                    return resultado;
                              }        
                                
                                
                                    
                             }else{ 
                                //El procedimiento no tiene un trámite de inicio
                                log.debug("EL PROCEDIMIENTO NO TIENE TRÁMITO DE INICIO. 5");
                                adapt.rollBack(con);
                                resultado.setStatus(5);
                                resultado.setDescStatus("EL PROCEDIMIENTO NO TIENE TRÁMITE DE INICIO");
                                resultado.setExpediente(null);
                                resultado.setTramite(null);
                                return resultado;
                             }        
                                    
                            

                         }else{
                           //La unidad de inicio no es válida.Se indica.
                             adapt.rollBack(con);
                             log.debug("LA UNIDAD DE INICIO NO ES VÁLIDA. 3");  
                             resultado.setStatus(3);
                             resultado.setDescStatus("LA UNIDAD DE INICIO NO ES VÁLIDA");
                             resultado.setExpediente(null);
                             resultado.setTramite(null);
                             return resultado;

                          }

                     }else{
                         //El procedimiento no es válido.
                          adapt.rollBack(con);
                          log.debug("EL CODIGO DE PROCEDIMIENTO NO ES VÁLIDO. 2");  
                          resultado.setStatus(2);
                          resultado.setDescStatus("EL CODIGO DE PROCEDIMIENTO NO ES VÁLIDO");
                          resultado.setExpediente(null);
                          resultado.setTramite(null);
                          return resultado;

                     }    



          
            }else{ //El usuario qeu quiere crear el expediente no tiene permiso sobre la unidad
                   //que inicia el expediente 
                    adapt.rollBack(con);
                    log.debug("EL USUARIO NO TIENE PERMISOS SOBRE LA UNIDAD QUE INICIA EL EXPEDIENTE. 8");  
                    resultado.setStatus(8);
                    resultado.setDescStatus("EL USUARIO NO TIENE PERMISOS SOBRE LA UNIDAD QUE INICIA EL EXPEDIENTE");
                    resultado.setExpediente(null);
                    resultado.setTramite(null);
                    return resultado;

             }  
        }else{ //No vienen cubiertos los atributos obligatorios en el parametro expediente         
            log.debug("NO SE HAN CUBIERTO TODOS LOS CAMPOS OBLIGATORIOS PARA PODER CREAR UN EXPEDIENTE");  
            resultado.setStatus(1);
            resultado.setDescStatus("NO SE HAN CUBIERTO LOS CAMPOS OBLIGATORIOS PARA PODER CREAR UN EXPEDIENTE");  
            resultado.setExpediente(null);
            resultado.setTramite(null);
            return resultado;

         }
            
    }catch(TechnicalException e){            
           log.debug("ERROR TÉCNICO AL CREAR EL EXPEDIENTE.STATUS 4: " + e.getMessage());  
           try{
                adapt.rollBack(con);
           }catch(BDException f){
               log.error("Error al realizar un rollback: " + f.getMessage());
           }
            
           //Salta una technical Exception si falla la creación del expediente
           resultado.setStatus(4);
           resultado.setDescStatus("ERROR TÉCNICO AL CREAR EL EXPEDIENTE");
           resultado.setExpediente(null);
           log.debug("Salto exception.Método iniciarExpediente ");
           e.printStackTrace();

       }catch(Exception e){
          try{
                adapt.rollBack(con);
           }catch(BDException f){
               log.error("Error al realizar un rollback: " + f.getMessage());
           }
          log.debug("SE HA PRODUCIDO ERROR TÉCNICO AL INICIAR EL EXPEDIENTE.STATUS 6: " + e.getMessage());   
          resultado.setStatus(6);
          resultado.setDescStatus("SE HA PRODUCIDO ERROR TÉCNICO AL INICIAR EL EXPEDIENTE");
          resultado.setExpediente(null);
          log.debug("Salto exception.Método iniciarExpediente ");  
          e.printStackTrace();

       }finally{
            try {              
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                java.util.logging.Logger.getLogger(ModuloIntegracionExternoCamposFlexia.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       
       log.debug(" ****** IniciarExpediente:END");
       return resultado;
    }

    
    private boolean isInteger(String dato){
        boolean exito = false;
        try{
            Integer.parseInt(dato);
            exito = true;
        }catch(NumberFormatException e){
            exito = false;
        }        
        return exito;
    }
    
    /**
     * Método auxiliar que se utiliza en crear expediente, para
     * verificar que el parámetro pasado trae todos los campos necesarios
     * @param ExpedienteModuloIntegracionVO
     * @param unidadInicioTramite
     * @return Boolean (true-> si sí  se traen todos los datos necesarios para iniciar
     * el expediente, false-> en caso contrario)
     */
    private boolean traeTodosLosCamposIniciarExpediente(ExpedienteModuloIntegracionVO expediente, String unidadInicioTramite){
        boolean resultado=true;
        try{
            
            log.debug("******* traeTodosLosCamposIniciarExpediente.unidadInicioTramite: "+ unidadInicioTramite);
            String procedimiento=expediente.getCodProcedimiento();
            log.debug("******* traeTodosLosCamposIniciarExpediente.Codigo procedimiento:"+ procedimiento);
            int usuarioInicio=expediente.getCodUsuarioIniciaExpediente();
            log.debug("******* traeTodosLosCamposIniciarExpediente.CodigoUsuarioIniciaExpediente: "+ usuarioInicio);
            int ejercicio= expediente.getEjercicio();
            log.debug("******* traeTodosLosCamposIniciarExpediente.Ejercicio: "+ ejercicio);
            int codMunicipio=expediente.getCodMunicipio();
            log.debug("******* traeTodosLosCamposIniciarExpediente.codMunicipio: "+ codMunicipio);

            String codUorVisibleExpediente = expediente.getCodigoUorVisibleInicioExpediente();
            log.debug("******* traeTodosLosCamposIniciarExpediente.codUorvisibleInicioExpedinte: "+ codUorVisibleExpediente);
            

            log.debug("1");
            if(usuarioInicio<0) return false;
            log.debug("2");
            if(procedimiento==null || "".equals(procedimiento)) return false; 
            log.debug("3");
            if ((usuarioInicio<0) || (codMunicipio<0) || (ejercicio<0)) return false;
            log.debug("4");
            if(unidadInicioTramite==null || "".equals(unidadInicioTramite)) return false;
            log.debug("5");
            if(codUorVisibleExpediente==null || "".equals(codUorVisibleExpediente)) return false;
            log.debug("6");

        }catch(Exception e){
            e.printStackTrace();
            log.debug("Error al ejecutar ModuloIntegracionExternoCamposFlexia.traeTodosLosCamposIniciarExpediente: " + e.getMessage());            
        }
        
        log.debug("************** traeTodosLosCamposIniciarExpediente.Resultado Devuelto: "+ resultado);
        return resultado;              
    }
    
    
    /**
     * Metodo auxiliar que devuelve el numero de Expediente,
     * en formato "2013/aacfe/000001", a partir del indice que le toca 
     * @return 
     */
    private String dameNumeroExpediente(int numero,int ejercicio, String procedimiento) {
        String exp=null;
        String cadNumero=String.valueOf(numero);
        cadNumero= "000000".substring(0,6-cadNumero.length()) +cadNumero;
        exp=ejercicio+"/"+procedimiento+"/"+cadNumero;
        return exp;
    }
  	
    
    /*
     * Método privado que se utiliza para comprobar lo siguiente:
     * Se mira el "tipo de unidad" de inicio del trámite, 
     * es decir( si es cq, la del expediente, otras, etc..)
     * y se comprueba si la unidad de inicio del trámite
     * que nos viene como parámetro está en una de ellas,
     * (si no fuese así, el trámite no sería válido)
     * 
     */
    private Boolean comprobarTramite(int codOrg, int codTramite, String codProc, int unidadInicioExpediente,
        int unidadInicioTramite,  TramiteModuloIntegracionExternoDAO tramiteDAO, Connection con) throws TechnicalException, SQLException{
    
       log.debug("comprobarTramite.BEGIN. codTramite:"+ codTramite); 
       log.debug("comprobarTramite.BEGIN. codProcedimiento:"+ codProc);
       ArrayList<Integer> unidadesInicioTramite= new ArrayList<Integer>(); 
   
       int tipoUnidad=tramiteDAO.dameTipoUnidadInicio(codOrg, codTramite, codProc, con);
    
       //El tipo de unidad de inicio es Cualquiera, ó la que lo inicia(para efectos = cq)
       if((tipoUnidad==1) || (tipoUnidad==3)){ 
           log.debug("El tipoUnidad es 1 o  3");
           return true; 
       }
       
       //La del expediente
       if ((tipoUnidad==4) && (unidadInicioExpediente==unidadInicioTramite)){
           log.debug("El tipo unidad es 4");
           return true;
       }

       if(tipoUnidad==0){
          log.debug("El tipo unidad es 0"); 
          unidadesInicioTramite=tramiteDAO.dameUnidadesInicio(codOrg, codTramite,codProc,con);
       
          if (unidadesInicioTramite.size() > 0) {
                for (int i = 0; i < unidadesInicioTramite.size(); i++) {
                    if (unidadesInicioTramite.get(i) == unidadInicioTramite) {
                        return true;
                    }
                }//for
          }  //if    
      }//tipoUnidad==0 
     return false;
    }

   /**
    * Permite recuperar un alista de terceros de Flexia de una determinada
    * organizacion. Damos la posibilidade de filtrar por documento, nombre,
    * primer y segundo apellido. Si no se filtra se recuperan todos los terceros.
    * Con los datos del tercero se recuperan además la posible lista de domicilios
    * que éste pueda tener.
    * @param codOrganizacion
    * @param documento documento del tercero
    * @param nombre nombre del tercero
    * @param apellido1 primer apellido del tercero
    * @param apellido2 segundo apellido del tercero
    * @return 
    */
   
    public SalidaIntegracionVO getTerceros(String codOrganizacion,int tipoDocumento, String documento, String nombre, String apelido1, String apelido2) {
     
       log.debug("getTerceros.BEGIN. codOrganizacion: "+ codOrganizacion);
       log.debug("getTerceros.BEGIN. documento: "+ documento);
       log.debug("getTerceros.BEGIN. nombre: "+ nombre);
       log.debug("getTerceros.BEGIN. apelido1: "+ apelido1);
       log.debug("getTerceros.BEGIN. apelido2: "+ apelido2);
       log.debug("getTerceros.BEGIN. tipoDocumento: "+ tipoDocumento);
       SalidaIntegracionVO  resultado= new SalidaIntegracionVO();
       String[] paramsBD = null;
       Connection con=null;
       paramsBD = this.getParams(Integer.parseInt(codOrganizacion)); 
       AdaptadorSQLBD adapt = null;
       ArrayList<TerceroModuloIntegracionVO> terceros=new ArrayList<TerceroModuloIntegracionVO>();
       try{
        adapt  = new AdaptadorSQLBD(paramsBD);
        con = adapt.getConnection(); 
       
      
       TerceroIntegracionModuloExternoDAO terceroModuloIntegracionExternoDAO= TerceroIntegracionModuloExternoDAO.getInstance(); 
       terceros= terceroModuloIntegracionExternoDAO.getTerceros(codOrganizacion, tipoDocumento, documento, nombre, apelido1, apelido2, con); 
       log.debug("Longitud del array de TERCEROS devuelto: "+ terceros.size());
       
       //Ahora  rellenar los domicilios
       
       
     for(TerceroModuloIntegracionVO tercero: terceros){ 
      
       TercerosValueObject tVO = new TercerosValueObject();
       tVO.setIdentificador(tercero.getCodTercero());
       ArrayList<TercerosValueObject> tercerosFlexia = TercerosDAO.getInstance().getByIdTercero(tVO,con,paramsBD);

        for(int i=0;tercerosFlexia!=null && i<tercerosFlexia.size();i++){
            TercerosValueObject ter = tercerosFlexia.get(i);
            String codTerceroActual = tercero.getCodTercero();            
            if(ter.getIdentificador().equals(codTerceroActual)){
                Vector<DomicilioSimpleValueObject> dom = (Vector<DomicilioSimpleValueObject>)ter.getDomicilios();
                tercero.setDomicilios(UtilitiesModuloIntegracion.listDomicilioSimpleValueObjectTolistDomicilioInteresadoModuloIntegracionVO(dom));
                break;
            }
        }// for

     } 
     resultado.setTerceros(terceros); 
     resultado.setStatus(0);
     resultado.setDescStatus("La operación se ha realizado correctamente");  
     
       }catch (Exception e){
         resultado.setTerceros(null);
         resultado.setStatus(27);
         resultado.setDescStatus("Se ha producido un erro técnico recuperando el tercero");
         e.printStackTrace();
       }
  
     return resultado;
    }

   /**
    * Esta operacion permite dar de alta un nuevo tercero en
    * Flexia
    * El nuevo tercero viene con un domicilio, si no se le conoce
    * el domicilio se le asigna el que está indicado por defecto
    * en Flexia.
    * 
    * @param codOrganizacion
    * @param tercero
    * @return 
    */
    public SalidaIntegracionVO altaTercero(String codOrganizacion,Integer usuarioAlta, TerceroModuloIntegracionVO tercero,Integer moduloAlta) {
     
     log.debug("AltaTercero.BEGIN.");
     String[] paramsBD = null;
     SalidaIntegracionVO resultado=new SalidaIntegracionVO();
     Connection con=null;
     AdaptadorSQLBD adapt = null;
     paramsBD = this.getParams(Integer.parseInt(codOrganizacion)); 
     TerceroIntegracionModuloExternoDAO terceroModuloIntegracionExternoDAO= TerceroIntegracionModuloExternoDAO.getInstance(); 
     DireccionIntegracionModuloExternoDAO direccionDAO = DireccionIntegracionModuloExternoDAO.getInstance();
     try {
         
        adapt  = new AdaptadorSQLBD(paramsBD);
        con = adapt.getConnection(); 
        
       
        //Insertamos o domicilio, en principio só ven un, ou ningún
        //(insertariamos o domicilio por defecto)
        ArrayList<DomicilioInteresadoModuloIntegracionVO> domicilios = tercero.getDomicilios();
      
        
        if((domicilios!=null) && (domicilios.size()>0)){
            //Viene un domicilio, que hay que dar de alta
           DomicilioInteresadoModuloIntegracionVO domicilio= domicilios.get(0);
           //Se comprueba que el objeto domicilio trae todos los campos necesarios para darlo de alta
           if (traeTodosLosDatosDomicilio(domicilio)){
                int identificadorDomicilio = direccionDAO.altaDireccion(domicilio,usuarioAlta,con);
                log.debug("AltaTercero.Identificador del Domicilio: "+ identificadorDomicilio);
                tercero.setDomPrincipal(String.valueOf(identificadorDomicilio));
           } else{
                 //Si faltan datos do domicilio, non creamos o terceiro e indicamolo
                 resultado.setStatus(11);
                 resultado.setDescStatus("Faltan datos del domicilio"); 
                 return resultado;
           }
        } else{ 
             //el tercero viene sin domicilio, hay que asignarle el domicilio por defecto
             log.debug("AltaTercero.El tercero viene sin domicilio, hay que asignar el domicilio por defecto ");
             DomicilioInteresadoModuloIntegracionVO domicilio=dameDomicilioDefecto(Integer.parseInt(codOrganizacion));
             int identificadorDomicilioDefecto=direccionDAO.altaDireccion(domicilio,usuarioAlta,con);
             domicilio.setIdDomicilio(Integer.toString(identificadorDomicilioDefecto));
             tercero.setDomPrincipal(String.valueOf(identificadorDomicilioDefecto));
             ArrayList<DomicilioInteresadoModuloIntegracionVO> doms = new ArrayList<DomicilioInteresadoModuloIntegracionVO>();             
             doms.add(domicilio);             
             tercero.setDomicilios(doms);
        }
        
       //Una vez que gestionamos el tema de las domicilios, damos de alta al tercero 
       int codTercero = terceroModuloIntegracionExternoDAO.altaTercero(tercero, con);
       tercero.setCodTercero(String.valueOf(codTercero));
        // Relacion tercero-domicilio
       int domicilio=Integer.parseInt(tercero.getDomPrincipal());
       //ahora tenemos que crear la relacion entre el tercero y el domicilio
       terceroModuloIntegracionExternoDAO.altaDomicilioTercero(codTercero,domicilio ,usuarioAlta, con);
       //Ahora el histórico
       int version= terceroModuloIntegracionExternoDAO.actualizarHistoricoTercero(tercero, codTercero, 
               Integer.parseInt(tercero.getDomPrincipal()), usuarioAlta,moduloAlta,con);
       
       log.debug("AltaTercero.Codigo del tercero: "+ codTercero);
       tercero.setVersionTercero(String.valueOf(version));
       
       ArrayList<TerceroModuloIntegracionVO> terceros= new ArrayList<TerceroModuloIntegracionVO>();
       terceros.add(tercero);
       resultado.setTerceros(terceros);
       resultado.setStatus(0);
       resultado.setDescStatus("TODO OK"); 
       adapt.finTransaccion(con);

     } catch (Exception e) {
            try {
                // Hacer rollback de la transaccion
                adapt.rollBack(con);
            } catch (Exception ex) {
                if (log.isDebugEnabled()) log.debug("Fallo al hacer rollback");
                ex.printStackTrace();
            }
            if (log.isDebugEnabled()) log.debug("Fallo durante la operacion");
            e.printStackTrace();
            resultado.setStatus(12);
            resultado.setDescStatus("No se ha podido dar de alta el tercero debido a un fallo técnico");

        } finally {
            try {
                // Devolver la conexion
               if (con != null && !con.isClosed()) con.close();
            } catch (SQLException e) {
                if (log.isDebugEnabled()) log.debug("Fallo al devolver la conexion");
                e.printStackTrace();
            }
        }

        if (log.isDebugEnabled()) log.debug("AltaTercero.END");
        return resultado;
    }

    /**
    * Esta operacion permite dar de alta un interesado a un
    * expediente. Previamente el tercero tiene que existir en 
    * flexia. Por tanto hay que comprobar previamente, que el
    * tercero existe en flexia.
    * @param codOrganizacion
    * @param expediente Datos del expediente en el que queremos dar de alta un interesado
    * @param tercero Datos del tercero que queremos poner como interesado
    * @param codDomicilio domicilio del tercero que queremos poner como edificio principal
    * @param codRolInteresado rol que tendrá el interesado en el expediente
    * @param mostrar, Este parámetro vale 1, si queremos que el interesado aparezca
    * en la bandeja de expedientes pendientes, columna interesado, 0 en caso contrario
    * @return 
    */
   public  SalidaIntegracionVO altaInteresadoExpediente(String codOrganizacion, ExpedienteModuloIntegracionVO expediente, 
           TerceroModuloIntegracionVO tercero, int codigoRolInteresado,int mostrar,Integer usuarioAlta, Integer moduloAlta){
     log.debug("AltaInteresadoExpediente.BEGIN.");
     String[] paramsBD = null;
     SalidaIntegracionVO resultado=new SalidaIntegracionVO();
     Connection con=null;
     AdaptadorSQLBD adapt = null;
     paramsBD = this.getParams(Integer.parseInt(codOrganizacion)); 
     TerceroIntegracionModuloExternoDAO terceroModuloIntegracionExternoDAO= TerceroIntegracionModuloExternoDAO.getInstance(); 
     ExpedienteIntegracionModuloExternoDAO expedienteIntegracionModuloExternoDAO=ExpedienteIntegracionModuloExternoDAO.getInstance();
     
     
     log.debug(" ============= ModuloIntegracionExternoCamposFlexia.altaInteresadoExpediente ==================>");
     //Verificamos que vienen todos los datos para poder realizar la operacion
     if (traeTodosLosDatosAltaInteresado(expediente, tercero,  codigoRolInteresado,mostrar)){
        String codProcedimiento=expediente.getCodProcedimiento();
        int ejercicio=expediente.getEjercicio();
        String numExp=expediente.getNumExpediente();
        String documento=tercero.getDocumentoTercero();
        int tipoDocumento=Integer.parseInt(tercero.getTipoDocumentoTercero());
        int numVersion=Integer.parseInt(tercero.getVersionTercero());
        int  codTercero=Integer.parseInt(tercero.getCodTercero());
        //int codDomicilio=Integer.parseInt(tercero.getDomPrincipal());
        
        int codDomicilio = -1;
        if(tercero.getDomicilios()!=null && tercero.getDomicilios().size()>=1)
            codDomicilio=Integer.parseInt(tercero.getDomicilios().get(0).getIdDomicilio());
                    
         //Primeiramente vamos a buscar se existe o terceiro en flexia
            try {
                adapt  = new AdaptadorSQLBD(paramsBD);
                con = adapt.getConnection(); 
        
                //HAY QUE MIRAR SI EXISTE EL EL TERCERO EN FLEXIA
                if(terceroModuloIntegracionExternoDAO.existeTercero(codTercero,tipoDocumento,documento,numVersion,con)){
                    
                 //Existe el tercero, así que lo damos de alta como interesado en el expediente
                    if(expedienteIntegracionModuloExternoDAO.existeExpediente(numExp, codProcedimiento, ejercicio, con)){  

                        terceroModuloIntegracionExternoDAO.altaInteresado(mostrar,
                              codOrganizacion,codProcedimiento,ejercicio,numExp, codigoRolInteresado,
                              codTercero,numVersion,codDomicilio,con);
                        resultado.setStatus(0);
                        resultado.setDescStatus("TODO OK");

                    }else{ //Algo falla con el expediente

                          resultado.setStatus(14);
                          resultado.setDescStatus("Hay algún problema con el expediente");
                          return resultado;
                         }
                    
                }else{ //no Existe el tercero . Devolvemos codigo de error
                    
                     resultado.setStatus(13);
                     resultado.setDescStatus("No existe el tercero en Flexia");
                     return resultado;
                  
                } 
              adapt.finTransaccion(con);   
              adapt.devolverConexion(con);

            } catch (Exception ex) {
                log.debug("altaInteresadoExpediente.Saltó excepción!");
                ex.printStackTrace();
                resultado.setStatus(16);
                resultado.setDescStatus("No se ha podido dar de alta el interesado en el expediente debido a un fallo técnico");
            }

     } else{
            resultado.setStatus(15);
            resultado.setDescStatus("Faltan datos para dar de alta el interesado en el expediente");
            return resultado;
      }
    log.debug("AltaInteresadoExpediente.END.");
    return resultado;
   }
   
   /**
    * Método para asociar un documento a un 
    * expediente
    * @param documento
    * @param codOrg
    * @return 
    *
   public SalidaIntegracionVO setDocumentoExterno(DocumentoExternoModuloIntegracionVO documento,
            String codOrg) {
   
   SalidaIntegracionVO resultado=new SalidaIntegracionVO();   
   String[] paramsBD = null;
   Connection con=null;
   AdaptadorSQLBD adapt = null;
   ExpedienteIntegracionModuloExternoDAO expedienteDAO= ExpedienteIntegracionModuloExternoDAO.getInstance();
    
   try {
       
     paramsBD = this.getParams(Integer.parseInt(codOrg));
     adapt  = new AdaptadorSQLBD(paramsBD);
     con = adapt.getConnection(); 
     adapt.inicioTransaccion(con);
     
     if (esValidoDocumento(documento)){
       expedienteDAO.setDocumentoExterno(documento,Integer.parseInt(codOrg), con);
     
     }
     resultado.setStatus(0);
     resultado.setDescStatus("TODO OK");
     adapt.finTransaccion(con);
     
   }catch (Exception ex) {
            log.debug("Saltó excepción en setDocumentoExterno");
            ex.printStackTrace();
            resultado.setStatus(17);
            resultado.setDescStatus("Error interno al asociar un documento al expediente");
     
         
    } finally {
            try {
                // Devolver la conexion
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Fallo al devolver la conexion");
                }
                e.printStackTrace();
            }
        }
   
    return resultado;
   } */
   
   
   
   private DomicilioInteresadoModuloIntegracionVO dameDomicilioDefecto(int codOrg) {
        
       DomicilioInteresadoModuloIntegracionVO domicilio=new DomicilioInteresadoModuloIntegracionVO();
       Config configCommon = ConfigServiceHelper.getConfig("common");       
       Config techserver=ConfigServiceHelper.getConfig("techserver");
       domicilio.setNormalizado("1");
       //Ponemos como paíspor defecto, España, (108), lo sacamos del techServer.properties
       domicilio.setIdPais(techserver.getString("SQL.CODIGOPAIS"));
       domicilio.setCodigoVia(configCommon.getString(codOrg+ ConstantesDatos.CODIGO_VIA_DESCONOCIDO));
       domicilio.setIdProvincia(configCommon.getString(codOrg + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO));
       domicilio.setIdMunicipio(configCommon.getString(codOrg + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO));
       domicilio.setTipoVia(Integer.parseInt(configCommon.getString(codOrg+ConstantesDatos.CODIGO_TIPO_VIA_DESCONOCIDA)));
       domicilio.setProvincia(configCommon.getString(codOrg + ConstantesDatos.DESCRIPCION_PROVINCIA_DESCONOCIDA));
       domicilio.setMunicipio(configCommon.getString(codOrg + ConstantesDatos.DESCRIPCION_MUNICIPIO_DESCONOCIDO));
       domicilio.setIdProvinciaVia(configCommon.getString(codOrg+ ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO));
       domicilio.setIdMunicipioVia(configCommon.getString(codOrg + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO));
       domicilio.setDescripcionVia(configCommon.getString(codOrg + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA));
       return domicilio;    
    }
   
   /**
    * Método auxiliar que verificar que el parámetro domicilio
    * trae todos los datos necesarios para dar de alta un domicilio
    * @param domicilio
    * @return Métod
    */
   
    private Boolean traeTodosLosDatosDomicilio(DomicilioInteresadoModuloIntegracionVO domicilio) {
      log.debug("traeTodosLosDatosDomicilio. BEGIN:");   
      Boolean resultado=false;
      String idPais=domicilio.getIdPais();
      String idProvincia=domicilio.getIdProvincia();
      String idMunicipio=domicilio.getIdMunicipio();
      int tipoVia=domicilio.getTipoVia();
      String descripcionVia=domicilio.getDescripcionVia();
      
      if((idPais!=null) &&(!"".equals(idPais)) && (idProvincia!=null) && (!"".equals(idProvincia))
             &&(idMunicipio!=null) && (!"".equals(idMunicipio)) && (descripcionVia!=null) && (!"".equals(descripcionVia)) && (tipoVia>-1) ){
          return true;
      }
     log.debug("traeTodosLosDatosDomicilio.Resultado Devuelto: "+ resultado);
     return resultado;
              
    }
   
    /**
     * Método auxiliar que comprueba que tenemos los datos
     * necesarios para dar de alta un interesado en un expediente
     */
    private Boolean traeTodosLosDatosAltaInteresado(ExpedienteModuloIntegracionVO expediente,
            TerceroModuloIntegracionVO tercero,   int codRolInteresado,int mostrar){
   
   log.debug("traeTodosLosDatosAltaInteresado.BEGIN..");     
   Boolean resultado=false;
   try{
    
    String codProcedimiento=expediente.getCodProcedimiento();
    String numExp=expediente.getNumExpediente();
    int ejercicio=expediente.getEjercicio();
    
    String documento=tercero.getDocumentoTercero();
    String tipoDocumento=tercero.getTipoDocumentoTercero();
  
    int numVersion=Integer.parseInt(tercero.getVersionTercero());
    int codTercero=Integer.parseInt(tercero.getCodTercero());
    //int codDomicilio=Integer.parseInt(tercero.getDomPrincipal());
    
    
    int codDomicilio=Integer.parseInt(tercero.getDomicilios().get(0).getIdDomicilio());
    
    
    
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() codProcedimiento: " + codProcedimiento);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() numExp: " + numExp);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() ejercicio: " + ejercicio);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() documento: " + documento);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() codDomicilio: " + codDomicilio);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() codRolInteresado: " + codRolInteresado);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() mostrar: " + mostrar);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() tipoDocumento: " + tipoDocumento);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() numVersion: " + numVersion);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() codTercero: " + codTercero);
    log.debug(" =================== ModuloIntegracionExternoCamposFlexia.traeTodosLosDatosAltaInteresado() codDomicilio: " + codDomicilio);
    
    if((codProcedimiento!=null) && !("".equals(codProcedimiento)) 
        && (numExp!=null) &&   !("".equals(numExp))
        && (ejercicio>1999)
        && (documento!=null) && (!"".equals(documento)) 
        && (codDomicilio>-2) 
        && (codRolInteresado>-2)
        && ((mostrar==1) || (mostrar==0))
        && (tipoDocumento!=null) && !("".equals(tipoDocumento))
        && (numVersion>-1) 
        && (codTercero>0) 
        && (codDomicilio>0)    
            ){
    
        return true;
    
    } 
    
    }catch(Exception e){
      log.debug("traeTodosLosDatosAltaInteresado.Salto excepcion");
      resultado=false;
      e.printStackTrace();
      
        
   }
   log.debug("traeTodosLosDatosAltaInteresado.END.El resultado devuelto es: "+resultado);     
    return resultado;
    }
    
    
    private Boolean esValidoDocumento(DocumentoExternoModuloIntegracionVO documento){
    
       if ((documento.getNombreDocumento()==null) || ("".equals(documento.getNombreDocumento()))){
         return false;
       
       }
       
      if((documento.getMimetipe()==null) || ("".equals(documento.getMimetipe()))){
         return false;
      
      }
      if((documento.getExtensionDocumento()==null) || ("".equals(documento.getExtensionDocumento())) ){
        return false;
    
      } 
     if((documento.getNumExp()==null )|| ("".equals(documento.getNumExp()))){
       return false;
     
     }
     if((documento.getContenido()==null) || (documento.getContenido().length<0)){
         return false;
     
     }
     if(documento.getEjercicio()<0){
         return false;
     
     }
    

     return true;
    }
    
     /**
      * Recupera el contenido de un documento a través del plugin de almacenamiento
      * @param documento
      * @param codOrg
      * @return SalidaIntegracionVO, si el documento se recupera correctamente
      * en su campo documentoExternoModuloIntegracion, en el campo contenido
      * tendremos el contenido recuperado del plugin de almacenamiento.
      */
    
     public SalidaIntegracionVO getDocumentoExterno(DocumentoExternoModuloIntegracionVO documento, String codOrg)  {

        log.debug("getDocumentoExterno. BEGIN:"); 
        SalidaIntegracionVO resultado = new SalidaIntegracionVO();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String[] paramsBD = null;
        
        // Obtener conexion
        try {
            paramsBD = this.getParams(Integer.parseInt(codOrg));
            
            adapt  = new AdaptadorSQLBD(paramsBD);
            con = adapt.getConnection(); 
            if(validarDatosGetDocumento(documento)){     
                String codProcedimiento = documento.getNumExp().split("[/]")[1];

                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",codOrg);
                datos.put("ejercicio",Integer.toString(documento.getEjercicio()));
                datos.put("numeroExpediente",documento.getNumExp());              
                datos.put("numeroDocumento",Long.toString(documento.getCodDocumento()));
                datos.put("params",paramsBD);
                datos.put("nombreDocumento",documento.getNombreDocumento());
                datos.put("extension",documento.getExtensionDocumento());              
                datos.put("tipoMime","application/octet-stream");

                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrg).getImplClassPluginProcedimiento(codOrg,codProcedimiento);

                Documento doc = null;
                int tipoDocumento = -1;

                if(!almacen.isPluginGestor())
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;              
                else{
                    String nomOrg = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(Integer.parseInt(codOrg),con);
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, paramsBD);

                    ResourceBundle bundle = ResourceBundle.getBundle("documentos");                    
                    String carpetaRaiz = bundle.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrg + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                    datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);

                    /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN LOS DOCUMENTOS EN EL GESTOR DOCUMENTAL **/
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrg + ConstantesDatos.GUION + nomOrg);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                    listaCarpetas.add(documento.getNumExp().replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));                    
                    datos.put("listaCarpetas",listaCarpetas);

                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                }

                doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);

                doc.setExpHistorico((ExpedienteDAO.getInstance().getExpediente(Integer.parseInt(codOrg),
                        documento.getEjercicio(),documento.getNumExp(),con) == null)?true:false);
                doc = almacen.getDocumentoExterno(doc);

                byte[] contenido = doc.getFichero();

                if (contenido != null) {
                    documento.setContenido(contenido);
                    resultado.setStatus(0);
                    resultado.setDescStatus("Operacion correcta");
                    resultado.setDocumentoExternoModuloIntegracion(documento);
                    log.debug("La operación se ha ejecutado correctamente");
                } else {
                    resultado.setStatus(19);
                    resultado.setDescStatus("No se encuentra el documento");
                    documento.setContenido(null);
                    resultado.setDocumentoExternoModuloIntegracion(documento);
                    return resultado;
                }
            }else{
                 log.debug("getDocumentoExterno.Faltan datos necesarios para recuperar el documento:");
                 resultado.setStatus(20);
                 resultado.setDescStatus("Faltan datos necesarios para recuperar el documento");
                 documento.setContenido(null);
                 resultado.setDocumentoExternoModuloIntegracion(documento);
                 return resultado;
            }     
        } catch (Exception ex) {
            log.debug("Se ha producido un error técnico al recuperar el contenido del documento ");
            resultado.setStatus(21);
            resultado.setDescStatus("Se ha producido un error téccnico al recuperar el contenido del documento");
            documento.setContenido(null);
            resultado.setDocumentoExternoModuloIntegracion(documento);
        } finally {

            try {
                // Devolver la conexion
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Fallo al devolver la conexion");
                }
                e.printStackTrace();
            }
        }
        log.debug("getDocumentoExterno.END");  
        return resultado; 
     } 
    
    /**
     * Método auxiliar que se utiliza en la función getDocumento, para
     * validar que vienen todos los atributos que son necesarios
     * para recuperar un documento.
     * @param documento
     * @return Boolean (true->si vienene todos los datos necesarios para recuperar
     * un documento, false-> en caso contrario)
     * 
     * 
     */
     private Boolean validarDatosGetDocumento(DocumentoExternoModuloIntegracionVO documento){
    
      log.debug("validarDatosGetDocumento. BEGIN");
       if (documento.getCodDocumento()<-1){
         return false;
       
       }
     
     if((documento.getNumExp()==null )|| ("".equals(documento.getNumExp()))){
       return false;
     
     }
     
     if((documento.getNombreDocumento()==null )|| ("".equals(documento.getNombreDocumento()))){
       return false;
     }

     if((documento.getExtensionDocumento()==null )|| ("".equals(documento.getExtensionDocumento()))){
       return false;
     }
     
     if(documento.getEjercicio()<0){
         return false;
     
     }
    
     log.debug("validarDatosGetDocumento. END");
     return true;
    }
     
     
    /**
    * Recupera el contenido de los modulos de extesion de un documento xml del buzon de entrada
    * @param codOrganizacion Codigo de organizacion
    * @param numExpediente Numero de expediente
    * @return String Contenido de la extensión
    */
    public String getModulosExtensionXmlBuzonEntrada(String codOrganizacion,String numExpediente){
        

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String extension = "";
        
        try{
            // Se recupera el JNDI correspondiente a la organización
            params = this.getParams(Integer.parseInt(codOrganizacion));
            adapt  = new AdaptadorSQLBD(params);
            con = adapt.getConnection();

            byte[] extensionBytes = ModuloIntegracionExternoDAO.getInstance().getModulosExtensionXmlBuzonEntrada(numExpediente, con);

            extension = Utilities.leerContenidoFicheroXML(extensionBytes,"<"+ConstantesDatos.TAG_XML_FLX_EXTENSION+">", "</"+ConstantesDatos.TAG_XML_FLX_EXTENSION+">");
            
       }catch(SQLException sqle){
            sqle.printStackTrace();
            extension = null; 
        }catch(BDException bde){
            bde.printStackTrace();
            extension = null;
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }
        return extension;
    }
    
    
    public SalidaIntegracionVO setDocumentoExterno(DocumentoExternoModuloIntegracionVO documento,String codOrg) {
   
        SalidaIntegracionVO resultado=new SalidaIntegracionVO();   
        String[] params = null;        
        
        try {

            params = this.getParams(Integer.parseInt(codOrg));          

            if (esValidoDocumento(documento)){            
                /****************************************************/               
                String codMunicipio  = codOrg;
                int ejercicio        = documento.getEjercicio();
                String numExpediente = documento.getNumExp();

                String[] datosExpediente = numExpediente.split("/");
                String codProcedimiento = datosExpediente[1];
                // Se obtiene la implementación del plugin correspondiente                        
                AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrg).getImplClassPluginProcedimiento(codOrg,codProcedimiento);

                Hashtable<String,Object> datos = new Hashtable<String,Object>();
                datos.put("codMunicipio",codMunicipio);
                datos.put("ejercicio",Integer.toString(ejercicio));
                datos.put("numeroExpediente",numExpediente);
                datos.put("nombreDocumento",documento.getNombreDocumento());
                datos.put("perteneceRelacion","false");
                datos.put("params",params);
                datos.put("fichero",documento.getContenido());
                datos.put("tipoMime",documento.getMimetipe());             
                String fileName = documento.getNombreDocumento();
                String[] datosFichero = fileName.split("[.]");
                datos.put("extension",documento.getExtensionDocumento()); 

                int tipoDocumento = -1;
                if(!almacen.isPluginGestor())
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
                else{
                    String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);                            
                    datos.put("codProcedimiento",codProcedimiento);                            
                    datos.put("codificacion",ConstantesDatos.CODIFICACION_UTF_8);

                    /** SE INDICA POR ORDEN CUALES SERÁN LAS CARPETAS EN LAS QUE SE ALOJARÁN EL DOCUMENTO EN EL GESTOR DOCUMENTAL **/
                    ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");                 
                    String carpetaRaiz     = bundleDocumentos.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrg + ConstantesDatos.BARRA + almacen.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                    String descripcionOrganizacion = OrganizacionesManager.getInstance().getDescripcionOrganizacion(codOrg, params);
                    ArrayList<String> listaCarpetas = new ArrayList<String>();
                    listaCarpetas.add(carpetaRaiz);
                    listaCarpetas.add(codOrg + ConstantesDatos.GUION + descripcionOrganizacion);
                    listaCarpetas.add(codProcedimiento + ConstantesDatos.GUION + nombreProcedimiento);
                    listaCarpetas.add(numExpediente.replaceAll(ConstantesDatos.BARRA,ConstantesDatos.GUION));
                    datos.put("listaCarpetas",listaCarpetas);                  
                    tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
                }

                Documento doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);            
                almacen.setDocumentoExterno(doc);
                resultado.setStatus(0);
                resultado.setDescStatus("OK");
             
         } else {
            resultado.setStatus(18);
            resultado.setDescStatus("Error: Algunos de los parámetros de entrada que son obligatorios no han sido proporcionados");                
         }
         
        }catch (Exception ex) {
           log.debug("Saltó excepción en setDocumentoExterno");
           ex.printStackTrace();
           resultado.setStatus(17);
           resultado.setDescStatus("Error interno al grabar el documento externo en el expediente " + documento.getNumExp() + ": " + ex.getMessage());
         } 
         return resultado;
   }
    
   @Override
    public SalidaIntegracionVO getCodDesplegableCampoSup(String codCampo, String codOrganizacion, String codProcedimiento) {
        SalidaIntegracionVO salida = new SalidaIntegracionVO();

        String[] params = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try{
            // Se recupera el JNDI correspondiente a la organización
            params  = this.getParams(Integer.parseInt(codOrganizacion));
            adapt   = new AdaptadorSQLBD(params);
            con     = adapt.getConnection();
            
            salida = ModuloIntegracionExternoDAO.getInstance().getCodDesplegableCampoSup(codCampo, codOrganizacion, codProcedimiento, con);
            
        }catch(BDException e){
            e.printStackTrace();
            salida = new SalidaIntegracionVO();
            salida.setStatus(-2);
            salida.setDescStatus("NO SE PUEDE OBTENER UNA CONEXIÓN A LA BASE DE DATOS");
            salida.setExpediente(null);
            salida.setTramite(null);
            salida.setCampoDesplegable(null);
            salida.setCamposDesplegables(null);
            salida.setDocumentoTramitacion(null);
            salida.setExpedientesRelacionados(null);
            salida.setListaDocumentosTramitacion(null);
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(BDException e){
                log.debug("Error al cerrar la conexión a la base de datos");
                e.printStackTrace();
            }
        }

        return salida;
        
    } 
}//class