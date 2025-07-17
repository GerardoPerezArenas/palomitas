/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.business.sge.persistence.manual;
 
import es.altia.agora.business.administracion.mantenimiento.ValoresCriterioBusquedaExpPendientesVO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.TramitacionValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import static es.altia.agora.business.sge.persistence.manual.TramitacionDAO.m_ConfigTechnical;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.TransformacionAtributoSelect;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesRelacionadosHistoricoVO;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.NumericOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  
import java.util.ArrayList;
import org.apache.log4j.Logger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author oscar 
 */
public class TramitacionDAOAuxiliar {
    
    private Logger m_Log = Logger.getLogger(TramitacionDAOAuxiliar.class);
    private static TramitacionDAOAuxiliar instance = null;
    
      protected static String idiomaDefecto;
    protected static String sql_res_codDpto;
    protected static String sql_res_codUnid;
    protected static String sql_res_tipoReg;
    protected static String sql_res_ejer;
    protected static String sql_res_num;
    protected static String sql_res_obs;
    protected static String sql_res_fecAnot;
    protected static String sql_res_asunt;
    protected static String sql_res_estado;
    protected static String sql_res_codTerc;
    protected static String sql_res_domTerc;
    protected static String sql_res_modTerc;
    protected static String sql_res_tipoDestino;
    protected static String sql_res_ord_dpto; // Ordinaria
    protected static String sql_res_ord_unid;

    protected static String sql_hte_ter; // Identificador tercero
    protected static String sql_hte_nvr; // Version tercero
    protected static String sql_hte_nan;
    protected static String sql_hte_a1a;
    protected static String sql_hte_a2a;
    protected static String sql_hte_noc;
    protected static String sql_hte_p1a;
    protected static String sql_hte_p2a;

    protected static String ext_mun;
    protected static String ext_eje;
    protected static String ext_num;
    protected static String ext_pro;
    protected static String ext_ter;
    protected static String ext_nvr;
    protected static String ext_rol;
    protected static String ext_dot;

    protected static String sql_dnn_dom; // Domicilio tercero, no normalizado
    protected static String sql_dpo_dom; // Domicilio tercero, normalizado

    protected static String sql_dnn_pai;
    protected static String sql_dnn_prv;
    protected static String sql_dnn_mun;
    protected static String sql_mun_pai;
    protected static String sql_mun_prv;
    protected static String sql_mun_cod;

    protected static String sql_dpo_dirSuelo;
    protected static String sql_dsu_codDirSuelo;
    protected static String sql_dsu_pais;
    protected static String sql_dsu_prov;
    protected static String sql_dsu_mun;

    protected static String sql_uor_nombre;
    protected static String sql_uor_codigo;
    protected static String sql_uor_codigo_visible;

    protected static String sql_uou_org;
    protected static String sql_uou_ent;
    protected static String sql_uou_uor;
    protected static String sql_uou_usu;

    protected static String sql_exp_codMun;
    protected static String sql_exp_codProc;
    protected static String sql_exp_ejer;
    protected static String sql_exp_num;
    protected static String sql_exp_fechIni;
    protected static String sql_exp_uor;
    protected static String sql_exp_estado;
    protected static String sql_exp_asunto;
  protected static String sql_exp_loc;

    protected static String sql_pro_codMun;
    protected static String sql_pro_cod;
    protected static String sql_pro_utr;
    protected static String sql_pro_tipo;
    protected static String sql_pro_fechHast;
    protected static String sql_pro_fechDesd;
    protected static String sql_pro_tipInic;
    protected static String sql_pro_estado;
    protected static String sql_pro_tramInternet;

    protected static String sql_pml_codMun;
    protected static String sql_pml_codProc;
    protected static String sql_pml_campo;
    protected static String sql_pml_lengua;
    protected static String sql_pml_valorCampo;

    protected static String sql_pui_codMun;
    protected static String sql_pui_codProc;
    protected static String sql_pui_uor;

    protected static String sql_org_codigo;
    protected static String sql_org_num;
    protected static String sql_nex_codMun;
    protected static String sql_nex_codProc;
    protected static String sql_nex_uor;
    protected static String sql_nex_num;
    protected static String sql_nex_ejer;

    protected static String sql_exr_mun;
    protected static String sql_exr_pro;
    protected static String sql_exr_eje;
    protected static String sql_exr_num;
    protected static String sql_exr_dep;
    protected static String sql_exr_uor;
    protected static String sql_exr_ejr;
    protected static String sql_exr_nre;
    protected static String sql_exr_tip;
    protected static String sql_exr_ori;
    protected static String sql_exr_top;

    protected static String sql_cro_mun;
    protected static String sql_cro_pro;
    protected static String sql_cro_eje;
    protected static String sql_cro_num;
    protected static String sql_cro_tra;
    protected static String sql_cro_ocu;
    protected static String sql_cro_fef;
    protected static String sql_cro_fli;
    protected static String sql_cro_uor;
    protected static String sql_cro_ffp;
     protected static String sql_cro_fip;

    protected static String sql_tra_mun;
    protected static String sql_tra_pro;
    protected static String sql_tra_cod;
    protected static String sql_tra_utr;

    protected static String rol_mun;
    protected static String rol_pro;
    protected static String rol_cod;
    protected static String rol_des;
    protected static String rol_pde;

    protected static String sql_rel_codMun;
    protected static String sql_rel_codProc;
    protected static String sql_rel_ejer;
    protected static String sql_rel_num;
    protected static String sql_rel_estado;
    protected static String sql_rel_exp_codMun;
    protected static String sql_rel_exp_codProc;
    protected static String sql_rel_exp_ejer;
    protected static String sql_rel_exp_num;
    protected static String sql_rel_exp_codMunR;
    protected static String sql_rel_exp_codProcR;
    protected static String sql_rel_exp_ejerR;
    protected static String sql_rel_exp_numR;
    
    protected static String r_ext_coduor;
    protected static String r_ext_tipo;
    protected static String r_ext_numero;
    protected static String r_ext_ano;
    protected static String r_ext_dep;
    protected static String r_ext_ter;
    protected static String r_ext_nvr;
    protected static String r_ext_dot;
    protected static String r_ext_rol;

    
    private TramitacionDAOAuxiliar(){                
        super();
        // Fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Servicio de log

        idiomaDefecto = m_ConfigTechnical.getString("idiomaDefecto");
        // Tabla R_RES
        sql_res_codDpto= m_ConfigTechnical.getString("SQL.R_RES.codDpto");
        sql_res_codUnid= m_ConfigTechnical.getString("SQL.R_RES.codUnidad");
        sql_res_tipoReg= m_ConfigTechnical.getString("SQL.R_RES.tipoReg");
        sql_res_ejer= m_ConfigTechnical.getString("SQL.R_RES.ejercicio");
        sql_res_num= m_ConfigTechnical.getString("SQL.R_RES.numeroAnotacion");
        sql_res_fecAnot= m_ConfigTechnical.getString("SQL.R_RES.fechaAnotacion");
        sql_res_asunt = m_ConfigTechnical.getString("SQL.R_RES.asunto");
        sql_res_obs = m_ConfigTechnical.getString("SQL.R_RES.observaciones");
        sql_res_estado = m_ConfigTechnical.getString("SQL.R_RES.estAnot");
        sql_res_codTerc= m_ConfigTechnical.getString("SQL.R_RES.codTercero");
        sql_res_domTerc= m_ConfigTechnical.getString("SQL.R_RES.domicTercero");
        sql_res_modTerc= m_ConfigTechnical.getString("SQL.R_RES.modifInteresado");
        sql_res_tipoDestino = m_ConfigTechnical.getString("SQL.R_RES.tipoAnotacionDestino");
        sql_res_ord_dpto = m_ConfigTechnical.getString("SQL.R_RES.orgOrigAnot");
        sql_res_ord_unid= m_ConfigTechnical.getString("SQL.R_RES.unidOrigDest");

        sql_hte_ter = m_ConfigTechnical.getString("SQL.T_HTE.identificador");
        sql_hte_nvr = m_ConfigTechnical.getString("SQL.T_HTE.version");
        sql_hte_nan = m_ConfigTechnical.getString("SQL.T_HTE.nombre");
        sql_hte_a1a = m_ConfigTechnical.getString("SQL.T_HTE.apellido1");
        sql_hte_a2a = m_ConfigTechnical.getString("SQL.T_HTE.apellido2");
        sql_hte_p1a = m_ConfigTechnical.getString("SQL.T_HTE.partApellido1");
        sql_hte_p2a = m_ConfigTechnical.getString("SQL.T_HTE.partApellido2");

        ext_mun = m_ConfigTechnical.getString("SQL.E_EXT.codMunicipio");
        ext_eje = m_ConfigTechnical.getString("SQL.E_EXT.ano");
        ext_num = m_ConfigTechnical.getString("SQL.E_EXT.numero");
        ext_pro = m_ConfigTechnical.getString("SQL.E_EXT.codProcedimiento");
        ext_ter = m_ConfigTechnical.getString("SQL.E_EXT.codTercero");
        ext_nvr = m_ConfigTechnical.getString("SQL.E_EXT.verTercero");
        ext_rol = m_ConfigTechnical.getString("SQL.E_EXT.rolTercero");
        ext_dot = m_ConfigTechnical.getString("SQL.E_EXT.codDomicilio");

        sql_dnn_pai = m_ConfigTechnical.getString("SQL.T_DNN.idPaisD");
        sql_mun_pai = m_ConfigTechnical.getString("SQL.T_MUN.idPais");
        sql_dnn_prv = m_ConfigTechnical.getString("SQL.T_DNN.idProvinciaD");
        sql_mun_prv = m_ConfigTechnical.getString("SQL.T_MUN.idProvincia");
        sql_dnn_mun = m_ConfigTechnical.getString("SQL.T_DNN.idMunicipioD");
        sql_mun_cod = m_ConfigTechnical.getString("SQL.T_MUN.idMunicipio");

        sql_dnn_dom = m_ConfigTechnical.getString("SQL.T_DNN.idDomicilio");
        sql_dpo_dom = m_ConfigTechnical.getString("SQL.T_DPO.domicilio"); // Domicilio tercero, normalizado
        sql_dpo_dirSuelo = m_ConfigTechnical.getString("SQL.T_DPO.suelo");
        sql_dsu_codDirSuelo = m_ConfigTechnical.getString("SQL.T_DSU.identificador");
        sql_dsu_pais= m_ConfigTechnical.getString("SQL.T_DSU.pais");
        sql_dsu_prov= m_ConfigTechnical.getString("SQL.T_DSU.provincia");
        sql_dsu_mun = m_ConfigTechnical.getString("SQL.T_DSU.municipio");

        sql_uou_uor= m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
        sql_uou_usu= m_ConfigTechnical.getString("SQL.A_UOU.usuario");
        sql_uou_org= m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
        sql_uou_ent= m_ConfigTechnical.getString("SQL.A_UOU.entidad");

        sql_uor_nombre = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
        sql_uor_codigo = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
        sql_uor_codigo_visible = m_ConfigTechnical.getString("SQL.A_UOR.codigoVisible");

        sql_exp_codMun= m_ConfigTechnical.getString("SQL.E_EXP.codMunicipio");
        sql_exp_codProc= m_ConfigTechnical.getString("SQL.E_EXP.codProcedimiento");
        sql_exp_ejer= m_ConfigTechnical.getString("SQL.E_EXP.ano");
        sql_exp_num= m_ConfigTechnical.getString("SQL.E_EXP.numero");
        sql_exp_fechIni= m_ConfigTechnical.getString("SQL.E_EXP.fechaInicio");
        sql_exp_uor=  m_ConfigTechnical.getString("SQL.E_EXP.uor");
        sql_exp_estado= m_ConfigTechnical.getString("SQL.E_EXP.estado");
        sql_exp_asunto= m_ConfigTechnical.getString("SQL.E_EXP.asunto");
    sql_exp_loc= m_ConfigTechnical.getString("SQL.E_EXP.localizacion");

        sql_pro_codMun= m_ConfigTechnical.getString("SQL.E_PRO.municipio");
        sql_pro_cod= m_ConfigTechnical.getString("SQL.E_PRO.codigoProc");
        sql_pro_fechHast= m_ConfigTechnical.getString("SQL.E_PRO.fechaLimiteHasta");
        sql_pro_fechDesd= m_ConfigTechnical.getString("SQL.E_PRO.fechaLimiteDesde");
        sql_pro_utr=m_ConfigTechnical.getString("SQL.E_PRO.unidad");
        sql_pro_tipo= m_ConfigTechnical.getString("SQL.E_PRO.tipoProcedimiento");
        sql_pro_tipInic= m_ConfigTechnical.getString("SQL.E_PRO.tipoInicio");
        sql_pro_estado= m_ConfigTechnical.getString("SQL.E_PRO.estadoProcedimiento");
        sql_pro_tramInternet= m_ConfigTechnical.getString("SQL.E_PRO.tramitacionInternet");

        sql_pml_codMun= m_ConfigTechnical.getString("SQL.E_PML.codMunicipio");
        sql_pml_codProc= m_ConfigTechnical.getString("SQL.E_PML.codProcedimiento");
        sql_pml_campo= m_ConfigTechnical.getString("SQL.E_PML.codCampoML");
        sql_pml_lengua= m_ConfigTechnical.getString("SQL.E_PML.idioma");
        sql_pml_valorCampo= m_ConfigTechnical.getString("SQL.E_PML.valor");

        sql_pui_codMun = m_ConfigTechnical.getString("SQL.E_PUI.codMunicipio");
        sql_pui_codProc = m_ConfigTechnical.getString("SQL.E_PUI.codProcedimiento");
        sql_pui_uor = m_ConfigTechnical.getString("SQL.E_PUI.codUnidadInicio");

        sql_org_codigo= m_ConfigTechnical.getString("SQL.A_ORG.codigo");
        sql_org_num= m_ConfigTechnical.getString("SQL.A_ORG.numExp");

        sql_nex_codMun= m_ConfigTechnical.getString("SQL.E_NEX.codMunicipio");
        sql_nex_codProc= m_ConfigTechnical.getString("SQL.E_NEX.codProcedimiento");
        sql_nex_uor= m_ConfigTechnical.getString("SQL.E_NEX.uor");
        sql_nex_num= m_ConfigTechnical.getString("SQL.E_NEX.numero");
        sql_nex_ejer= m_ConfigTechnical.getString("SQL.E_NEX.ejercicio");

        sql_exr_mun= m_ConfigTechnical.getString("SQL.E_EXR.codMunicipio");
        sql_exr_pro= m_ConfigTechnical.getString("SQL.E_EXR.codProcedimiento");
        sql_exr_eje= m_ConfigTechnical.getString("SQL.E_EXR.ejercicio");
        sql_exr_num= m_ConfigTechnical.getString("SQL.E_EXR.numero");
        sql_exr_dep= m_ConfigTechnical.getString("SQL.E_EXR.departamentoAnotacion");
        sql_exr_uor= m_ConfigTechnical.getString("SQL.E_EXR.unidadAnotacion");
        sql_exr_ejr= m_ConfigTechnical.getString("SQL.E_EXR.ejercicioAnotacion");
        sql_exr_nre= m_ConfigTechnical.getString("SQL.E_EXR.numeroRegistro");
        sql_exr_tip= m_ConfigTechnical.getString("SQL.E_EXR.tipoRegistro");
        sql_exr_ori= m_ConfigTechnical.getString("SQL.E_EXR.origen");
        sql_exr_top= m_ConfigTechnical.getString("SQL.E_EXR.tipoOperacion");

        sql_cro_mun = m_ConfigTechnical.getString("SQL.E_CRO.codMunicipio");
        sql_cro_pro = m_ConfigTechnical.getString("SQL.E_CRO.codProcedimiento");
        sql_cro_eje = m_ConfigTechnical.getString("SQL.E_CRO.ano");
        sql_cro_num = m_ConfigTechnical.getString("SQL.E_CRO.numero");
        sql_cro_tra = m_ConfigTechnical.getString("SQL.E_CRO.codTramite");
        sql_cro_ocu = m_ConfigTechnical.getString("SQL.E_CRO.ocurrencia");
        sql_cro_fef = m_ConfigTechnical.getString("SQL.E_CRO.fechaFin");
        sql_cro_fli = m_ConfigTechnical.getString("SQL.E_CRO.fechaLimite");
        sql_cro_uor = m_ConfigTechnical.getString("SQL.E_CRO.codUnidadTramitadora");
        sql_cro_ffp = m_ConfigTechnical.getString("SQL.E_CRO.fechaFinPlazo");
        sql_cro_fip = m_ConfigTechnical.getString("SQL.E_CRO.fechaInicioPlazo");
        sql_tra_mun = m_ConfigTechnical.getString("SQL.E_TRA.codMunicipio");
        sql_tra_pro = m_ConfigTechnical.getString("SQL.E_TRA.codProcedimiento");
        sql_tra_cod = m_ConfigTechnical.getString("SQL.E_TRA.codTramite");
        sql_tra_utr = m_ConfigTechnical.getString("SQL.E_TRA.unidadTramite");

        rol_mun = m_ConfigTechnical.getString("SQL.E_ROL.codMunicipio");
        rol_pro = m_ConfigTechnical.getString("SQL.E_ROL.codProcedimiento");
        rol_cod = m_ConfigTechnical.getString("SQL.E_ROL.codRol");
        rol_des = m_ConfigTechnical.getString("SQL.E_ROL.descripcion");
        rol_pde = m_ConfigTechnical.getString("SQL.E_ROL.porDefecto");

        sql_rel_codMun= m_ConfigTechnical.getString("SQL.G_REL.codMunicipio");
        sql_rel_codProc= m_ConfigTechnical.getString("SQL.G_REL.codProcedimiento");
        sql_rel_ejer= m_ConfigTechnical.getString("SQL.G_REL.ano");
        sql_rel_num= m_ConfigTechnical.getString("SQL.G_REL.numero");
        sql_rel_estado= m_ConfigTechnical.getString("SQL.G_REL.estado");
        sql_rel_exp_codMun= m_ConfigTechnical.getString("SQL.G_EXP.codMunicipio");
        sql_rel_exp_codProc= m_ConfigTechnical.getString("SQL.G_EXP.codProcedimiento");
        sql_rel_exp_ejer= m_ConfigTechnical.getString("SQL.G_EXP.ejercicio");
        sql_rel_exp_num= m_ConfigTechnical.getString("SQL.G_EXP.numero");
        sql_rel_exp_codMunR= m_ConfigTechnical.getString("SQL.G_EXP.codMunicipioR");
        sql_rel_exp_codProcR= m_ConfigTechnical.getString("SQL.G_EXP.codProcedimientoR");
        sql_rel_exp_ejerR= m_ConfigTechnical.getString("SQL.G_EXP.ejercicioR");
        sql_rel_exp_numR= m_ConfigTechnical.getString("SQL.G_EXP.numeroR");


        // Interesados registro
        r_ext_coduor = m_ConfigTechnical.getString ("SQL.R_EXT.codUor");
        r_ext_tipo   = m_ConfigTechnical.getString ("SQL.R_EXT.tipo");
        r_ext_ano    = m_ConfigTechnical.getString ("SQL.R_EXT.ano");
        r_ext_numero = m_ConfigTechnical.getString ("SQL.R_EXT.numero");
        r_ext_dep    = m_ConfigTechnical.getString ("SQL.R_EXT.coddepartamento");
        r_ext_ter    = m_ConfigTechnical.getString ("SQL.R_EXT.codTercero");
        r_ext_nvr    = m_ConfigTechnical.getString ("SQL.R_EXT.verTercero");
        r_ext_dot    = m_ConfigTechnical.getString ("SQL.R_EXT.codDomicilio");
        r_ext_rol    = m_ConfigTechnical.getString ("SQL.R_EXT.rolTercero");
        
    }
    
    
    public static TramitacionDAOAuxiliar getInstance(){
        if(instance==null)
            instance = new TramitacionDAOAuxiliar();
        
        return instance;
    }
        
    
    
    public int contarNumeroExpedientesPendientes(UsuarioValueObject uVO, TramitacionValueObject tVO,String[] params)
            throws TramitacionException, TechnicalException {
        
        AdaptadorSQLBD oad = null;
        Connection con = null;
        PreparedStatement stmt;
        ResultSet rs = null;
        String sql;
        int numero = 0;
        
        ArrayList bindVar = new ArrayList();
        ArrayList bindVarSup = new ArrayList();
        
       
        /************************/
        ValoresCriterioBusquedaExpPendientesVO criterio = tVO.getCriterioBusquedaExpPendientes();        
        TransformacionAtributoSelect transformador = null;
        /************************/
        try{
            oad = new AdaptadorSQLBD(uVO.getParamsCon());
            con = oad.getConnection();
            transformador = new TransformacionAtributoSelect(oad);
            
            m_Log.debug("********** TramitacionDAO.getExpedientesPendientes SOLO EXP");
            String parteSelect2 = "SELECT COUNT(*) AS NUM FROM (SELECT E_EXP.EXP_NUM ";
            String parteFrom2 = " " +
                    "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU, A_UOR,E_EXP " +
                    "LEFT JOIN E_CRO ON (E_EXP." + sql_exp_codMun + " = " + sql_cro_mun + " AND " +
                    "E_EXP." + sql_exp_ejer + " = " + sql_cro_eje + " AND " +
                    "E_EXP." + sql_exp_num + " = " + sql_cro_num + " AND " +
                    sql_cro_fef + " IS NULL) " +                                        
                    "LEFT JOIN " + GlobalNames.ESQUEMA_GENERICO + "A_UOU ON (" + sql_uou_uor + " = " + sql_cro_uor + " AND " +
                    sql_uou_usu + " = ? AND " +
                    sql_uou_org + " = ? AND " +
                    sql_uou_ent + " = ? ) " + 
                    /*
                    "LEFT JOIN E_EXR ON (E_EXP."+sql_exp_codMun + " = E_EXR." + sql_exr_mun + " AND " + 
                    "E_EXP." + sql_exp_ejer + " = E_EXR." + sql_exr_eje + " AND " +
                    "E_EXP." + sql_exp_num + " = E_EXR." + sql_exr_num +")" + */
                    " INNER JOIN E_PRO ON (EXP_PRO=E_PRO.PRO_COD AND EXP_MUN=PRO_MUN) " + 
                    "AND (PRO_RESTRINGIDO=0 OR (PRO_RESTRINGIDO=1 AND EXP_PRO IN (SELECT PRO_COD FROM " + GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO WHERE "+GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO.ORG_COD=? AND "+GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO.PRO_COD=EXP_PRO AND "+GlobalNames.ESQUEMA_GENERICO + "USUARIO_PROC_RESTRINGIDO.USU_COD=?) ))";
            
            bindVar.add(uVO.getIdUsuario()); // pos 0
            bindVar.add(uVO.getOrgCod()); 
            bindVar.add(uVO.getEntCod()); 
            bindVar.add(uVO.getOrgCod()); 
            bindVar.add(uVO.getIdUsuario()); // pos 4 
            
            
            
            if(criterio!=null){

                String auxiliar="";
                // Si se filtra por identificación o por interesado => Se añade el join para la parte FROM de la consulta
                if("2".equals(criterio.getCodigoCriterioBusqueda()) || "3".equals(criterio.getCodigoCriterioBusqueda())){
                    auxiliar  = " LEFT JOIN E_EXT ON (E_EXP.EXP_EJE=E_EXT.EXT_EJE AND E_EXP.EXP_MUN=E_EXT.EXT_MUN AND E_EXP.EXP_NUM=E_EXT.EXT_NUM )"
                              + " LEFT JOIN T_HTE ON (E_EXT.EXT_TER=T_HTE.HTE_TER AND E_EXT.EXT_NVR=T_HTE.HTE_NVR) ";

                    parteFrom2 = parteFrom2 + auxiliar;
                }//if
            }// if
            
            String parteFromBusquedaCampoSuplementario    = "";            
            String parteWhereBusquedaCampoSuplementario   = "";
            boolean busquedaCampoSuplementario            = false;

            /********************** PRUEBA **********************/
               /** SE COMPRUEBA SI SE HA ESTABLECIDO UN CRITERIO DE BÚSQUEDA POR CAMPO SUPLEMENTARIO, YA QUE EN ESTE CASO HABRÁ QUE
                     AÑADIR LA PARTE FROM Y LA PARTE WHERE  **/
                if(criterio!=null && criterio.isCampoSuplementarioCriterioBusqueda()){
                    /** SE DETERMINA LA COLUMNA POR LA QUE SE ORDENA A QUE TIPO DE CAMPO SUPLEMENTARIO PERTENECE RECUPERANDO EL NOMBRE DE LA TABLA EN LA QUE SE ALOJA EL VALOR **/
                    ArrayList<String> valores = criterio.getValoresCriterioBusqueda();
                    if(criterio.getTipoCampoSuplementarioCriterioBusqueda()!=null && NumericOperations.isInteger(criterio.getTipoCampoSuplementarioCriterioBusqueda()))
                    {
                        int tipoDato = Integer.parseInt(criterio.getTipoCampoSuplementarioCriterioBusqueda());
                        m_Log.debug("El tipo del dato del campo suplementario por el que se hace la búsqueda es " + tipoDato);
                        switch (tipoDato){
                            case 1: //  Campo de tipo numérico                                
                                parteFromBusquedaCampoSuplementario    = "LEFT JOIN E_TNU ON (E_TNU.TNU_MUN=E_EXP.EXP_MUN AND E_TNU.TNU_EJE = E_EXP.EXP_EJE AND E_TNU.TNU_NUM=E_EXP.EXP_NUM AND E_TNU.TNU_COD=?) ";                                
                                bindVar.add(criterio.getCodigoCriterioBusqueda());

                                if(criterio.getOperadorCriterioBusqueda()==0){ // Operador =
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNU_VALOR FROM E_TNU WHERE TNU_VALOR=? AND E_TNU.TNU_MUN=E_EXP.EXP_MUN AND E_TNU.TNU_EJE = E_EXP.EXP_EJE AND E_TNU.TNU_NUM=E_EXP.EXP_NUM AND E_TNU.TNU_COD=?) ";
                                    
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    
                                    
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==1){ // Operador >
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNU_VALOR FROM E_TNU WHERE TNU_VALOR>? AND E_TNU.TNU_MUN=E_EXP.EXP_MUN AND E_TNU.TNU_EJE = E_EXP.EXP_EJE AND E_TNU.TNU_NUM=E_EXP.EXP_NUM AND E_TNU.TNU_COD=?) ";
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==2){ // Operador >=

                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNU_VALOR FROM E_TNU WHERE TNU_VALOR>=? AND E_TNU.TNU_MUN=E_EXP.EXP_MUN AND E_TNU.TNU_EJE = E_EXP.EXP_EJE AND E_TNU.TNU_NUM=E_EXP.EXP_NUM AND E_TNU.TNU_COD=?) ";
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==3){ // Operador <                                   
                                         parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNU_VALOR FROM E_TNU WHERE TNU_VALOR<? AND E_TNU.TNU_MUN=E_EXP.EXP_MUN AND E_TNU.TNU_EJE = E_EXP.EXP_EJE AND E_TNU.TNU_NUM=E_EXP.EXP_NUM AND E_TNU.TNU_COD=?) ";
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==4){ // Operador <=
                               
                                         parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNU_VALOR FROM E_TNU WHERE TNU_VALOR<=? AND E_TNU.TNU_MUN=E_EXP.EXP_MUN AND E_TNU.TNU_EJE = E_EXP.EXP_EJE AND E_TNU.TNU_NUM=E_EXP.EXP_NUM AND E_TNU.TNU_COD=?) ";
                                         
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==5){ // Operador ENTRE                                    
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNU_VALOR FROM E_TNU WHERE TNU_VALOR>=? AND TNU_VALOR<=? AND E_TNU.TNU_MUN=E_EXP.EXP_MUN AND E_TNU.TNU_EJE = E_EXP.EXP_EJE AND E_TNU.TNU_NUM=E_EXP.EXP_NUM AND E_TNU.TNU_COD=?) ";
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(valores.get(1));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==5){ // Operador <>                                    
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNU_VALOR FROM E_TNU WHERE TNU_VALOR!=? AND E_TNU.TNU_MUN=E_EXP.EXP_MUN AND E_TNU.TNU_EJE = E_EXP.EXP_EJE AND E_TNU.TNU_NUM=E_EXP.EXP_NUM AND E_TNU.TNU_COD=?) ";
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }


                                parteFrom2 = parteFrom2 + parteFromBusquedaCampoSuplementario;
                                //parteOrderByColumnaCampoSuplementario = "E_TNU.TNU_VALOR ";
                                busquedaCampoSuplementario             = true;
                                break;
                            case 2: //  Campo de tipo texto corto
                                parteFromBusquedaCampoSuplementario    = "LEFT JOIN E_TXT ON (E_TXT.TXT_MUN=E_EXP.EXP_MUN AND E_TXT.TXT_EJE = E_EXP.EXP_EJE AND E_TXT.TXT_NUM=E_EXP.EXP_NUM AND E_TXT.TXT_COD=?) ";
                                bindVar.add(criterio.getCodigoCriterioBusqueda());
                                if(criterio.getOperadorCriterioBusqueda()==0){ // Operador =
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                    " AND EXISTS(SELECT TXT_VALOR FROM E_TXT WHERE TXT_VALOR=? AND E_TXT.TXT_MUN=E_EXP.EXP_MUN AND E_TXT.TXT_EJE = E_EXP.EXP_EJE AND E_TXT.TXT_NUM=E_EXP.EXP_NUM AND E_TXT.TXT_COD=?) ";
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());

                                }else
                                if(criterio.getOperadorCriterioBusqueda()==6){ // Operador <>                                    
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                    " AND EXISTS(SELECT TXT_VALOR FROM E_TXT WHERE TXT_VALOR!=? AND E_TXT.TXT_MUN=E_EXP.EXP_MUN AND E_TXT.TXT_EJE = E_EXP.EXP_EJE AND E_TXT.TXT_NUM=E_EXP.EXP_NUM AND E_TXT.TXT_COD=?) ";
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                  
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==7){ // Operador LIKE                                    
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                    " AND EXISTS(SELECT TXT_VALOR FROM E_TXT WHERE  ";
                                    
                                    Vector condicionYFiltros=new Vector();                                   
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("TXT_VALOR",valores.get(0), false);
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+((String)condicionYFiltros.get(0));
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                        bindVarSup.add(filtroOperadores.get(j));
                                    }
                                 
                                    
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+ " AND E_TXT.TXT_MUN=E_EXP.EXP_MUN AND E_TXT.TXT_EJE = E_EXP.EXP_EJE AND E_TXT.TXT_NUM=E_EXP.EXP_NUM AND E_TXT.TXT_COD=?) ";                                  
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }

                                parteFrom2 = parteFrom2 + parteFromBusquedaCampoSuplementario;
                                busquedaCampoSuplementario              = true;
                                break;
                            case 3: //  Campo de tipo fecha
                                Vector condicionYFiltros=new Vector();
                                 parteFromBusquedaCampoSuplementario    = "LEFT JOIN E_TFE ON (E_TFE.TFE_MUN=E_EXP.EXP_MUN AND E_TFE.TFE_EJE = E_EXP.EXP_EJE AND E_TFE.TFE_NUM=E_EXP.EXP_NUM AND E_TFE.TFE_COD=?) ";   
                                 bindVar.add(criterio.getCodigoCriterioBusqueda());
                                 
                               
                               if(criterio.getOperadorCriterioBusqueda()==0){ // Operador =
                                    /*
                                    parteWhereBusquedaCampoSuplementario = " AND " + transformador.construirCondicionWhereConOperadoresCampoFecha("E_TFE.TFE_VALOR", valores.get(0));
                                    */
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario
                                            + " AND EXISTS(SELECT TFE_VALOR FROM E_TFE WHERE E_TFE.TFE_MUN=E_EXP.EXP_MUN AND E_TFE.TFE_EJE = E_EXP.EXP_EJE AND E_TFE.TFE_NUM=E_EXP.EXP_NUM AND E_TFE.TFE_COD=?"
                                            + " AND ";
                                    
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFE.TFE_VALOR","="+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==1){ // Operador >
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFE_VALOR FROM E_TFE WHERE E_TFE.TFE_MUN=E_EXP.EXP_MUN AND E_TFE.TFE_EJE = E_EXP.EXP_EJE AND E_TFE.TFE_NUM=E_EXP.EXP_NUM AND E_TFE.TFE_COD=?"
                                           + " AND ";
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFE.TFE_VALOR",">"+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==2){ // Operador >=

                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFE_VALOR FROM E_TFE WHERE E_TFE.TFE_MUN=E_EXP.EXP_MUN AND E_TFE.TFE_EJE = E_EXP.EXP_EJE AND E_TFE.TFE_NUM=E_EXP.EXP_NUM AND E_TFE.TFE_COD=?"
                                           + " AND " ;
                                    
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFE.TFE_VALOR",">="+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }

                                }else
                                if(criterio.getOperadorCriterioBusqueda()==3){ // Operador <

                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFE_VALOR FROM E_TFE WHERE E_TFE.TFE_MUN=E_EXP.EXP_MUN AND E_TFE.TFE_EJE = E_EXP.EXP_EJE AND E_TFE.TFE_NUM=E_EXP.EXP_NUM AND E_TFE.TFE_COD=?"
                                            + " AND " ;
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFE.TFE_VALOR","<="+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==4){ // Operador <=

                                         parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFE_VALOR FROM E_TFE WHERE E_TFE.TFE_MUN=E_EXP.EXP_MUN AND E_TFE.TFE_EJE = E_EXP.EXP_EJE AND E_TFE.TFE_NUM=E_EXP.EXP_NUM AND E_TFE.TFE_COD=?"
                                           + " AND " ;
                                      
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFE.TFE_VALOR","<="+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==5){ // Operador ENTRE
                                    
                                         parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFE_VALOR FROM E_TFE WHERE E_TFE.TFE_MUN=E_EXP.EXP_MUN AND E_TFE.TFE_EJE = E_EXP.EXP_EJE AND E_TFE.TFE_NUM=E_EXP.EXP_NUM AND E_TFE.TFE_COD=?"
                                            + " AND " ;
                                         
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFE.TFE_VALOR",valores.get(0)+":"+valores.get(1));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }

                                parteFrom2 = parteFrom2 + parteFromBusquedaCampoSuplementario;
                                busquedaCampoSuplementario             = true;
                                break;
                            case 6: //  Campo de tipo desplegable                                
                                parteFromBusquedaCampoSuplementario    = "LEFT JOIN E_TDE ON (E_TDE.TDE_MUN=E_EXP.EXP_MUN AND E_TDE.TDE_EJE = E_EXP.EXP_EJE AND E_TDE.TDE_NUM=E_EXP.EXP_NUM AND E_TDE.TDE_COD=?) ";                                
                               
                                bindVar.add(criterio.getCodigoCriterioBusqueda());
                                
                                if(criterio.getOperadorCriterioBusqueda()==0){ // Operador =                                    
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario + 
                                    " AND EXISTS(SELECT TDE_VALOR FROM E_TDE WHERE E_TDE.TDE_MUN=E_EXP.EXP_MUN AND E_TDE.TDE_EJE = E_EXP.EXP_EJE AND E_TDE.TDE_NUM=E_EXP.EXP_NUM AND E_TDE.TDE_COD=?"
                                            +" AND E_TDE.TDE_VALOR=?) ";
                                    
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==6){ // Operador !=
                                    
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                    " AND EXISTS(SELECT TDE_VALOR FROM E_TDE WHERE E_TDE.TDE_MUN=E_EXP.EXP_MUN AND E_TDE.TDE_EJE = E_EXP.EXP_EJE AND E_TDE.TDE_NUM=E_EXP.EXP_NUM AND E_TDE.TDE_COD=?"
                                            +" AND E_TDE.TDE_VALOR!=?) ";
                                    
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }

                                parteFrom2 = parteFrom2 + parteFromBusquedaCampoSuplementario;
                                busquedaCampoSuplementario            = true;
                                break;
                           case 8: //  Campo de tipo numérico calculado
                                parteFromBusquedaCampoSuplementario    = "LEFT JOIN E_TNUC ON (E_TNUC.TNUC_MUN=E_EXP.EXP_MUN AND E_TNUC.TNUC_EJE = E_EXP.EXP_EJE AND E_TNUC.TNUC_NUM=E_EXP.EXP_NUM AND E_TNUC.TNUC_COD=?) ";                               
                                
                                 bindVar.add(criterio.getCodigoCriterioBusqueda());

                                if(criterio.getOperadorCriterioBusqueda()==0){ // Operador =
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNUC_VALOR FROM E_TNUC WHERE TNUC_VALOR=? AND E_TNUC.TNUC_MUN=E_EXP.EXP_MUN AND E_TNUC.TNUC_EJE = E_EXP.EXP_EJE AND E_TNUC.TNUC_NUM=E_EXP.EXP_NUM AND E_TNUC.TNUC_COD=?) ";
                                    
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==1){ // Operador >
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNUC_VALOR FROM E_TNUC WHERE TNUC_VALOR>? AND E_TNUC.TNUC_MUN=E_EXP.EXP_MUN AND E_TNUC.TNUC_EJE = E_EXP.EXP_EJE AND E_TNUC.TNUC_NUM=E_EXP.EXP_NUM AND E_TNUC.TNUC_COD=?) ";
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==2){ // Operador >=

                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNUC_VALOR FROM E_TNUC WHERE TNUC_VALOR>=? AND E_TNUC.TNUC_MUN=E_EXP.EXP_MUN AND E_TNUC.TNUC_EJE = E_EXP.EXP_EJE AND E_TNUC.TNUC_NUM=E_EXP.EXP_NUM AND E_TNUC.TNUC_COD=?) ";
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==3){ // Operador <                                   
                                         parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNUC_VALOR FROM E_TNUC WHERE TNUC_VALOR<? AND E_TNUC.TNUC_MUN=E_EXP.EXP_MUN AND E_TNUC.TNUC_EJE = E_EXP.EXP_EJE AND E_TNUC.TNUC_NUM=E_EXP.EXP_NUM AND E_TNUC.TNUC_COD=?) ";
                                         
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==4){ // Operador <=
                               
                                         parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNUC_VALOR FROM E_TNUC WHERE TNUC_VALOR<=? AND E_TNUC.TNUC_MUN=E_EXP.EXP_MUN AND E_TNUC.TNUC_EJE = E_EXP.EXP_EJE AND E_TNUC.TNUC_NUM=E_EXP.EXP_NUM AND E_TNUC.TNUC_COD=?) ";
                                         
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==5){ // Operador ENTRE                                    
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNUC_VALOR FROM E_TNUC WHERE TNUC_VALOR>=? AND TNUC_VALOR<=? AND E_TNUC.TNUC_MUN=E_EXP.EXP_MUN AND E_TNUC.TNUC_EJE = E_EXP.EXP_EJE AND E_TNUC.TNUC_NUM=E_EXP.EXP_NUM AND E_TNUC.TNUC_COD=?) ";
                                    
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(valores.get(1));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==5){ // Operador <>                                    
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario +
                                            " AND EXISTS(SELECT TNUC_VALOR FROM E_TNUC WHERE TNUC_VALOR!=? AND E_TNUC.TNUC_MUN=E_EXP.EXP_MUN AND E_TNUC.TNUC_EJE = E_EXP.EXP_EJE AND E_TNUC.TNUC_NUM=E_EXP.EXP_NUM AND E_TNUC.TNUC_COD=?) ";
                                    
                                    bindVarSup.add(valores.get(0));
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                }


                                parteFrom2 = parteFrom2 + parteFromBusquedaCampoSuplementario;
                                busquedaCampoSuplementario             = true;
                                break;                            
                            case 9: //  Campo de tipo fecha calculada
                                parteFromBusquedaCampoSuplementario    = "LEFT JOIN E_TFEC ON (E_TFEC.TFEC_MUN=E_EXP.EXP_MUN AND E_TFEC.TFEC_EJE = E_EXP.EXP_EJE AND E_TFEC.TFEC_NUM=E_EXP.EXP_NUM AND E_TFEC.TFEC_COD=?) ";                                                                
                                
                                bindVar.add(criterio.getCodigoCriterioBusqueda());

                               if(criterio.getOperadorCriterioBusqueda()==0){ // Operador =
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario
                                            + " AND EXISTS(SELECT TFEC_VALOR FROM E_TFEC WHERE E_TFEC.TFEC_MUN=E_EXP.EXP_MUN AND E_TFEC.TFEC_EJE = E_EXP.EXP_EJE AND E_TFEC.TFEC_NUM=E_EXP.EXP_NUM AND E_TFEC.TFEC_COD=?"
                                            + " AND " ;
                                    
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFEC.TFEC_VALOR","="+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==1){ // Operador >
                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFEC_VALOR FROM E_TFEC WHERE E_TFEC.TFEC_MUN=E_EXP.EXP_MUN AND E_TFEC.TFEC_EJE = E_EXP.EXP_EJE AND E_TFEC.TFEC_NUM=E_EXP.EXP_NUM AND E_TFEC.TFEC_COD=?"
                                            + " AND " ;
                                    
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFEC.TFEC_VALOR",">"+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==2){ // Operador >=

                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFEC_VALOR FROM E_TFEC WHERE E_TFEC.TFEC_MUN=E_EXP.EXP_MUN AND E_TFEC.TFEC_EJE = E_EXP.EXP_EJE AND E_TFEC.TFEC_NUM=E_EXP.EXP_NUM AND E_TFEC.TFEC_COD=?"
                                            + " AND ";
                                    
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFEC.TFEC_VALOR",">="+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }

                                }else
                                if(criterio.getOperadorCriterioBusqueda()==3){ // Operador <

                                    parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFEC_VALOR FROM E_TFEC WHERE E_TFEC.TFEC_MUN=E_EXP.EXP_MUN AND E_TFEC.TFEC_EJE = E_EXP.EXP_EJE AND E_TFEC.TFEC_NUM=E_EXP.EXP_NUM AND E_TFEC.TFEC_COD=?"
                                            + " AND " ;
                                    
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFEC.TFEC_VALOR","<"+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==4){ // Operador <=

                                         parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFEC_VALOR FROM E_TFEC WHERE E_TFEC.TFEC_MUN=E_EXP.EXP_MUN AND E_TFEC.TFEC_EJE = E_EXP.EXP_EJE AND E_TFEC.TFEC_NUM=E_EXP.EXP_NUM AND E_TFEC.TFEC_COD=?"
                                            + " AND " ;
                                         
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFEC.TFEC_VALOR","<="+valores.get(0));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }else
                                if(criterio.getOperadorCriterioBusqueda()==5){ // Operador ENTRE
                                    
                                         parteWhereBusquedaCampoSuplementario = parteWhereBusquedaCampoSuplementario  +
                                       " AND EXISTS(SELECT TFEC_VALOR FROM E_TFEC WHERE E_TFEC.TFEC_MUN=E_EXP.EXP_MUN AND E_TFEC.TFEC_EJE = E_EXP.EXP_EJE AND E_TFEC.TFEC_NUM=E_EXP.EXP_NUM AND E_TFEC.TFEC_COD=?"
                                            + " AND " ;
                                         
                                    bindVarSup.add(criterio.getCodigoCriterioBusqueda());
                                    condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_TFEC.TFEC_VALOR",valores.get(0)+":"+valores.get(1));
                                    parteWhereBusquedaCampoSuplementario=parteWhereBusquedaCampoSuplementario+" "+(String)condicionYFiltros.get(0) + " ) ";                                    
                                    Vector filtroOperadores=new Vector();
                                    filtroOperadores=(Vector)condicionYFiltros.get(1);
                                    for (int j=0;j<filtroOperadores.size();j++)
                                    {
                                       bindVarSup.add(filtroOperadores.get(j));
                                    }
                                }

                                parteFrom2 = parteFrom2 + parteFromBusquedaCampoSuplementario;
                                busquedaCampoSuplementario             = true;
                                break;
                        }// switch
                    }// if
                }// if
            
            String pWhere2 = " WHERE USU_COD=EXP_USU AND UOR_COD=EXP_UOR  " 
                    + " AND " + sql_exp_estado + " = 0 "
                    + " AND ((exists ( "
                    + " SELECT DISTINCT " +sql_uou_uor
                        + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU "
                        + " WHERE "+ sql_uou_usu + "=?" 
                        + " AND "+ sql_uou_org +"=?"
                        + " AND "+ sql_uou_ent +"=?"
                        + " AND "+ sql_uou_uor +"="+ sql_exp_uor
                    + "))OR(exists (SELECT DISTINCT "+ sql_uou_uor
                        + " FROM "+GlobalNames.ESQUEMA_GENERICO+"A_UOU "
                        + " WHERE "+ sql_uou_usu +"=?"
                        + " AND "+ sql_uou_org +"=?"
                        + " AND "+ sql_uou_ent +"=?"
                        + " AND " + sql_uou_uor + "=" + sql_cro_uor +")) "
                        + " AND " + sql_cro_fef + " IS NULL) ";
            
            bindVar.add(uVO.getIdUsuario()); 
            bindVar.add(uVO.getOrgCod()); 
            bindVar.add(uVO.getEntCod()); 
            bindVar.add(uVO.getIdUsuario()); 
            bindVar.add(uVO.getOrgCod()); 
            bindVar.add(uVO.getEntCod());

            /**
             * Inicio filtro por procedimiento *
             */
            if (tVO.getCodProcedimiento() != null && !"".equalsIgnoreCase(tVO.getCodProcedimiento()) && !"TODOS".equalsIgnoreCase(tVO.getCodProcedimiento())) {
                pWhere2 = pWhere2 + " AND E_EXP.EXP_PRO=?";
                bindVar.add(tVO.getCodProcedimiento());
            }
            /**
             * Fin filtro por procedimiento *
             */
            if ("1".equals(tVO.getCodRangoTemporal()) || "2".equals(tVO.getCodRangoTemporal())) {

                pWhere2 = pWhere2 + " AND EXP_FEI >= ? ";


                if ("1".equals(tVO.getCodRangoTemporal())) {
                    Calendar calendario = Calendar.getInstance();
                    calendario.add(Calendar.MONTH, -6);
                    Timestamp time = new Timestamp(calendario.getTimeInMillis());
                    bindVar.add(time);

                } else if ("2".equals(tVO.getCodRangoTemporal())) {
                    Calendar calendario = Calendar.getInstance();
                    calendario.add(Calendar.MONTH, -12);
                    Timestamp time = new Timestamp(calendario.getTimeInMillis());
                    bindVar.add(time);

                }

            }

            /*****************************************************************************************/
            // Se comprueba si el criterio no es nulo
            if(criterio!=null){
                // Si se ha introducido algún criterio de búsqueda               
               ArrayList<String> valores = criterio.getValoresCriterioBusqueda();
               String auxiliar = "";
               if("1".equals(criterio.getCodigoCriterioBusqueda())){
                    // Número de expediente
                    
                        auxiliar = " AND";
                        String crit="";
                       
                        if(criterio.getOperadorCriterioBusqueda()==0) crit=valores.get(0);
                        else if(criterio.getOperadorCriterioBusqueda()==6) crit="!"+valores.get(0);
                        else if(criterio.getOperadorCriterioBusqueda()==7) crit=valores.get(0);

                        boolean intervalo=false;
                        if(criterio.getOperadorCriterioBusqueda()==6) intervalo=true;
                        
                        Vector condicionYFiltros = new Vector();
                        condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("E_EXP.EXP_NUM", crit, intervalo);
                        auxiliar = auxiliar + " " + ((String) condicionYFiltros.get(0));
                        Vector filtroOperadores = new Vector();
                        filtroOperadores = (Vector) condicionYFiltros.get(1);
                        for (int j = 0; j < filtroOperadores.size(); j++) {
                            bindVar.add(filtroOperadores.get(j));
                        }
            
               }
               else
               if("4".equals(criterio.getCodigoCriterioBusqueda())){
                    // Búsqueda por fecha de inicio de expediente
                   
                     auxiliar = " AND " ;
                     if(criterio.getOperadorCriterioBusqueda()==5) {
                        Vector condicionYFiltros=new Vector();
                       
                        condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_EXP.EXP_FEI",valores.get(0)+":"+valores.get(1));
                        auxiliar=auxiliar+" "+(String)condicionYFiltros.get(0);                                  
                        Vector filtroOperadores=new Vector();
                        filtroOperadores=(Vector)condicionYFiltros.get(1);
                        for (int j=0;j<filtroOperadores.size();j++)
                        {
                           bindVar.add(filtroOperadores.get(j));
                        }
                     }else{
                       

                        String crit="";
                         
                        if(criterio.getOperadorCriterioBusqueda()==0) crit="="+valores.get(0);
                        if(criterio.getOperadorCriterioBusqueda()==1) crit=">"+valores.get(0);
                        if(criterio.getOperadorCriterioBusqueda()==2) crit=">="+valores.get(0);
                        if(criterio.getOperadorCriterioBusqueda()==3) crit="<"+valores.get(0);
                        if(criterio.getOperadorCriterioBusqueda()==4) crit="<="+valores.get(0);
                        
                            
                        Vector condicionYFiltros=new Vector();
                        
                        condicionYFiltros = transformador.construirCondicionWhereConOperadoresCampoFechaBindVariables("E_EXP.EXP_FEI",crit);
                        auxiliar=auxiliar+" "+(String)condicionYFiltros.get(0);                                  
                        Vector filtroOperadores=new Vector();
                        filtroOperadores=(Vector)condicionYFiltros.get(1);
                        for (int j=0;j<filtroOperadores.size();j++)
                        {
                           bindVar.add(filtroOperadores.get(j));
                        }
                    }
                   
               }else
               if("5".equals(criterio.getCodigoCriterioBusqueda())){
                    // Asunto
                   
                        auxiliar = " AND " ;
                        String crit="";
                         
                        if(criterio.getOperadorCriterioBusqueda()==0) crit=valores.get(0);
                        if(criterio.getOperadorCriterioBusqueda()==6) crit="!"+valores.get(0);
                        if(criterio.getOperadorCriterioBusqueda()==7) crit=valores.get(0);
                       
                        
                            
                        Vector condicionYFiltros=new Vector();
                       
                        condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("E_EXP.EXP_ASU",crit);
                        auxiliar=auxiliar+" "+(String)condicionYFiltros.get(0);                                  
                        Vector filtroOperadores=new Vector();
                        filtroOperadores=(Vector)condicionYFiltros.get(1);
                        for (int j=0;j<filtroOperadores.size();j++)
                        {
                           bindVar.add(filtroOperadores.get(j));
                        }
  
                    
                }else
                if("6".equals(criterio.getCodigoCriterioBusqueda())){
                    // Observaciones
                    
                     auxiliar = " AND " ;
                     String crit="";
                         
                        if(criterio.getOperadorCriterioBusqueda()==0) crit=valores.get(0);
                        if(criterio.getOperadorCriterioBusqueda()==6) crit="!"+valores.get(0);
                        if(criterio.getOperadorCriterioBusqueda()==7) crit=valores.get(0);
                    
                        Vector condicionYFiltros=new Vector();
                       
                        condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("E_EXP.EXP_OBS",crit);
                        auxiliar=auxiliar+" "+(String)condicionYFiltros.get(0);                                  
                        Vector filtroOperadores=new Vector();
                        filtroOperadores=(Vector)condicionYFiltros.get(1);
                        for (int j=0;j<filtroOperadores.size();j++)
                        {
                           bindVar.add(filtroOperadores.get(j));
                        }
                        
                        
                  
               }else
               if("2".equals(criterio.getCodigoCriterioBusqueda())){
                    // Identificación
                   if(valores!=null && valores.get(0)!=null && criterio.getOperadorCriterioBusqueda()>0){
                       auxiliar  = " AND T_HTE.HTE_TID=?"
                                 + " AND T_HTE.HTE_DOC=? ";
                       
                       bindVar.add(criterio.getOperadorCriterioBusqueda());
                       bindVar.add(valores.get(0));
                               
                   }else{
                       auxiliar  = " AND T_HTE.HTE_TID=? ";
                       bindVar.add(criterio.getOperadorCriterioBusqueda());
                   }
               }else
               if("3".equals(criterio.getCodigoCriterioBusqueda())){
                    // Interesado
                    String nombre = null;
                    String apellido1 = null;
                    String apellido2 = null;

                    if (valores != null && valores.size() == 1) {
                        nombre = valores.get(0);
                    } else if (valores != null && valores.size() == 2) {
                        nombre = valores.get(0);
                        apellido1 = valores.get(1);
                    } else if (valores != null && valores.size() == 3) {
                        nombre = valores.get(0);
                        apellido1 = valores.get(1);
                        apellido2 = valores.get(2);
                    }

                    if (nombre != null) {


                        Vector condicionYFiltros = new Vector();
                       
                        condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("T_HTE.HTE_NOM",  nombre );
                        auxiliar = auxiliar + " AND " + (String) condicionYFiltros.get(0);
                        Vector filtroOperadores = new Vector();
                        filtroOperadores = (Vector) condicionYFiltros.get(1);
                        for (int j = 0; j < filtroOperadores.size(); j++) {
                            bindVar.add(filtroOperadores.get(j));
                        }
                    }

                    if (apellido1 != null) {

                        Vector condicionYFiltros = new Vector();
                       
                        condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("T_HTE.HTE_AP1",  apellido1);
                        auxiliar = auxiliar + " AND " + (String) condicionYFiltros.get(0);
                        Vector filtroOperadores = new Vector();
                        filtroOperadores = (Vector) condicionYFiltros.get(1);
                        for (int j = 0; j < filtroOperadores.size(); j++) {
                            bindVar.add(filtroOperadores.get(j));
                        }
                    }
                    if (apellido2 != null) {
 

                        Vector condicionYFiltros = new Vector();
                       
                        condicionYFiltros = transformador.construirCondicionWhereConOperadoresBindVariables("T_HTE.HTE_AP2", apellido2);
                        auxiliar = auxiliar + " AND " + (String) condicionYFiltros.get(0);
                        Vector filtroOperadores = new Vector();
                        filtroOperadores = (Vector) condicionYFiltros.get(1);
                        for (int j = 0; j < filtroOperadores.size(); j++) {
                            bindVar.add(filtroOperadores.get(j));
                        }
                    }
                }//if

               pWhere2 = pWhere2 + auxiliar;
            }//if

            if (tVO.getUsuarioTramitador()!=null && tVO.getUsuarioTramitador()!="" && es.altia.agora.business.geninformes.utils.Utilidades.isInteger(tVO.getUsuarioTramitador())) {
               pWhere2 += " AND (E_EXP.EXP_USU = ? OR E_CRO.CRO_USU =  ?"+
                        " OR E_CRO.CRO_USF = ?) ";
               
               bindVar.add(tVO.getUsuarioTramitador());
               bindVar.add(tVO.getUsuarioTramitador());
               bindVar.add(tVO.getUsuarioTramitador());
               
                       
               
               }//if

            /*********************** prueba *********************/
           
            /*********************** prueba *********************/
            
            /*****************************************************************************************/            
            String groupSelect = " GROUP BY E_EXP.EXP_NUM) consulta";
                    //+ "E_EXR.EXR_TOP)";

           /****** PARA ACTIVAR LA BÚSQUEDA INSENTITIVA MAYUSCULAS/MINÚSCULAS EN ORACLE ******/
            //Nota Febrero 2017: Para la consulta de expedientes pendientes vamos a desactivar estos alter. Ya que se ejecutan siempre y dan problemas de stuck-thread
//           if(params[0]!=null && (params[0].equalsIgnoreCase("oracle"))){
//                String alter1 = "ALTER SESSION SET NLS_COMP=LINGUISTIC";
//                String alter2 = "ALTER SESSION SET NLS_SORT=GENERIC_BASELETTER";                
//                Statement st =con.createStatement();
//                st.executeQuery(alter1);
//                st=con.prepareStatement(alter2);
//                st.executeQuery(alter2);
//                st.close();
//            }
            /****** PARA ACTIVAR LA BÚSQUEDA INSENTITIVA MAYUSCULAS/MINÚSCULAS EN ORACLE ******/            

            if(busquedaCampoSuplementario){ 
                // Si se realiza una búsqueda por campo suplementario se añade la parte correspondiente en el WHERE de la consulta SQL
               sql = parteSelect2 + parteFrom2 + pWhere2 +  parteWhereBusquedaCampoSuplementario+ groupSelect;
            }else 
            {
                sql = parteSelect2 + parteFrom2 + pWhere2 + groupSelect;
            }

            if(m_Log.isDebugEnabled()) m_Log.debug(sql);
            stmt = con.prepareStatement(sql);

            
            int posicion_interrogacion=1;
            for (int i=0;i<bindVar.size();i++)
            {
                Object elemento=new Object();
                elemento=bindVar.get(i);
                if(elemento instanceof Integer)
                {
                    int e=(Integer)bindVar.get(i);
                    stmt.setInt(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof String)
                {
                    String e=(String)bindVar.get(i);
                    stmt.setString(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                if(elemento instanceof Long)
                {
                    Long e=(Long)bindVar.get(i);
                    stmt.setLong(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                
                if(elemento instanceof Long)
                {
                    Long e=(Long)bindVar.get(i);
                    stmt.setLong(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                
                if(elemento instanceof Timestamp)
                {
                    Timestamp e=(Timestamp)bindVar.get(i);
                    stmt.setTimestamp(posicion_interrogacion, e);
                    m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                }
                posicion_interrogacion=posicion_interrogacion+1;
                
            }
            
            if(busquedaCampoSuplementario){
                for (int i=0;i<bindVarSup.size();i++)
                {
                    Object elemento=new Object();
                    elemento=bindVarSup.get(i);
                    if(elemento instanceof Integer)
                    {
                        int e=(Integer)bindVar.get(i);
                        stmt.setInt(posicion_interrogacion, e);
                        m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                    }
                    if(elemento instanceof String)
                    {
                        String e=(String)bindVarSup.get(i);
                        stmt.setString(posicion_interrogacion, e);
                        m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                    }
                    if(elemento instanceof Long)
                    {
                        Long e=(Long)bindVarSup.get(i);
                        stmt.setLong(posicion_interrogacion, e);
                        m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                    }

                    if(elemento instanceof Long)
                    {
                        Long e=(Long)bindVarSup.get(i);
                        stmt.setLong(posicion_interrogacion, e);
                        m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                    }

                    if(elemento instanceof Timestamp)
                    {
                        Timestamp e=(Timestamp)bindVarSup.get(i);
                        stmt.setTimestamp(posicion_interrogacion, e);
                        m_Log.debug("Valor de paso "+(posicion_interrogacion)+": "+e);
                    }
                    posicion_interrogacion=posicion_interrogacion+1;

                }
            
            }


            rs = stmt.executeQuery();            
            while(rs.next()) {
                numero = rs.getInt("NUM");
            }
             rs.close();
             stmt.close();
            
            return numero;
        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage()); 
            throw new TramitacionException("Error. TramitacionDAO.getExpedientesPendientes", e);

        } finally {
            try{
                
                if(params[0]!=null && (params[0].equals("oracle") || params[0].equals("ORACLE")) ){
                        Statement st_alter;
                        String alter1 = "ALTER SESSION SET NLS_COMP=BINARY";
                        String alter2 = "ALTER SESSION SET NLS_SORT=SPANISH";
                        st_alter = con.createStatement();
                        st_alter.executeQuery(alter1);
                        st_alter.executeQuery(alter2);

                        st_alter.close();
                    }
                
                oad.devolverConexion(con);
            }catch(BDException bde) {
                bde.printStackTrace();
                m_Log.error(bde.getMessage());
                throw new TramitacionException("Error.TramitacionDAO.getExpedientesPendientes.Devolver conexion", bde);
            }catch (SQLException sqle){
                sqle.printStackTrace();
                m_Log.error(sqle.getMessage());
                throw new TramitacionException("Error.TramitacionDAO.getExpedientesPendientes.Devolver conexion", sqle);
            }
        }
    }
    
   public ArrayList<ExpedientesRelacionadosHistoricoVO> recuperarExpAsociados(Connection con,String uni_registro,String ejercicio, 
           String tipo_reg)  throws TramitacionException,TechnicalException {
        // Recupera los expedientes asociados que esten anulados.
        if(m_Log.isDebugEnabled()) m_Log.debug("recuperarExpAsociados() : BEGIN");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<ExpedientesRelacionadosHistoricoVO> resultado = new ArrayList<ExpedientesRelacionadosHistoricoVO>();        
        int posicion = 0;
        try {
            posicion = ejercicio.indexOf("/");
            String ejer = ejercicio.substring(0,posicion);
            String num = ejercicio.substring(posicion+1, ejercicio.length()) ; 
            
            String sqlGeneral = " SELECT EXR_NUM, EXR_PRO, EXP_EST FROM %s " +  
                         " INNER JOIN %s ON EXP_MUN = EXR_MUN AND EXP_EJE=EXR_EJE AND EXP_NUM = EXR_NUM "; 
            
            String parteWhere = " WHERE EXR_UOR="+  uni_registro+
                         " AND EXR_TIP = '" + tipo_reg + "'"+
                         " AND EXR_EJR = " + ejer + 
                         " AND EXR_NRE = " + num;
                    
            String sqlTablasNoHist = String.format(sqlGeneral, "E_EXR", "E_EXP");
            String sqlTablasHist = String.format(sqlGeneral, "HIST_E_EXR", "HIST_E_EXP");
            
            StringBuilder sql = new StringBuilder(sqlTablasNoHist).append(parteWhere)
                    .append(" UNION ")
                    .append(sqlTablasHist).append(parteWhere)
                    .append(" ORDER BY EXR_NUM DESC");
                    

            if (m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: recuperarExpAsociados--> " + sql); 

            stmt = con.prepareStatement(sql.toString());                          
            rs = stmt.executeQuery();
            
            while(rs.next()){
                ExpedientesRelacionadosHistoricoVO expediente = new ExpedientesRelacionadosHistoricoVO();
                    expediente.setExpediente(rs.getString("EXR_NUM"));
                    expediente.setEstado(rs.getString("EXP_EST"));
                resultado.add(expediente);
            }//while(rs.next())
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            throw new TramitacionException("Error. TramitacionDAO.recuperarExpAsociados", e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("recuperarExpAsociados() : END");
        return resultado;
   }//recuperarExpAsociados
   
    
   public ArrayList<ExpedientesRelacionadosHistoricoVO> recuperarExpAsociados(Connection con,String departamento,String ejercicio, 
           String tipo_reg,String uni_registro)  throws TramitacionException,TechnicalException {
        // Recupera los expedientes asociados que esten anulados.
        if(m_Log.isDebugEnabled()) m_Log.debug("recuperarExpAsociados() : BEGIN");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<ExpedientesRelacionadosHistoricoVO> resultado = new ArrayList<ExpedientesRelacionadosHistoricoVO>();        
        int posicion = 0;
        try {
            posicion = ejercicio.indexOf("/");
            String ejer = ejercicio.substring(0,posicion);
            String num = ejercicio.substring(posicion+1, ejercicio.length()) ; 
            
            String sql = " SELECT EXR_NUM, EXR_PRO, EXP_EST FROM E_EXR " +  
                         " INNER JOIN E_EXP ON EXP_MUN = EXR_MUN AND EXP_EJE=EXR_EJE AND EXP_NUM = EXR_NUM " +
                         " WHERE EXR_DEP = " + departamento +
                         " AND EXR_UOR="+  uni_registro+
                         " AND EXR_TIP = '" + tipo_reg + "'"+
                         " AND EXR_EJR = " + ejer + 
                         " AND EXR_NRE = " + num +
                         " ORDER BY EXR_NUM DESC"; 

            if (m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: recuperarExpAsociados--> " + sql); 

            stmt = con.prepareStatement(sql);    
            rs = stmt.executeQuery();
            
            while(rs.next()){
                ExpedientesRelacionadosHistoricoVO expediente = new ExpedientesRelacionadosHistoricoVO();
                    expediente.setExpediente(rs.getString("EXR_NUM"));
                    expediente.setEstado(rs.getString("EXP_EST"));
                resultado.add(expediente);
            }//while(rs.next())
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            throw new TramitacionException("Error. TramitacionDAO.recuperarExpAsociados", e);
        } finally {
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(stmt);
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("recuperarExpAsociados() : END");
        return resultado;
   }//recuperarExpAsociados
   
    public String recuperaClasePluginExpedRelacHistorico(Connection con,String procedimiento)  throws TramitacionException,TechnicalException {

        m_Log.debug("recuperaClasePluginExpedRelacHistorico");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String resultado = "";        
        
        try {
                                    
            String sql = " SELECT PLUGIN_RELAC_HISTORICO FROM E_PRO " +  
                         " WHERE PRO_COD = '" + procedimiento +"'";

            if (m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: recuperaClasePluginExpedRelacHistorico--> " + sql); 

            stmt = con.prepareStatement(sql);             
            rs = stmt.executeQuery();            
            if(rs.next()){         
                resultado = rs.getString("PLUGIN_RELAC_HISTORICO");   
            }

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new TramitacionException("Error. TramitacionDAO.recuperaClasePluginExpedRelacHistorico", e);
        } finally {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(stmt);
        }
        return resultado;
   }
    
    
   public String recuperaFechaPresentacion(Connection con,String departamento,String codUni, String ejercicio, String tipo_reg)  throws TramitacionException,TechnicalException {

        m_Log.debug("recuperaFechaPresentacion");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String resultado = "";
        String datos = "";
        int posicion = 0;
        
        try {            
            posicion = ejercicio.indexOf("/");
            String ejer = ejercicio.substring(0,posicion);
            String num = ejercicio.substring(posicion+1, ejercicio.length()) ; 
            
            String sql = " SELECT DISTINCT RES_FEC FROM R_RES " +  
                         " WHERE RES_DEP = " + departamento +
                         " AND RES_UOR = " + codUni +
                         " AND RES_TIP = '" + tipo_reg + "'"+
                         " AND RES_EJE = " + ejer + 
                         " AND RES_NUM = " + num ;

            if (m_Log.isDebugEnabled()) m_Log.debug("TramitacionDAO: recuperaFechaPresentacion--> " + sql); 

            stmt = con.prepareStatement(sql);             
            rs = stmt.executeQuery();
            
            if(rs.next()){         
                resultado = rs.getString("RES_FEC");   
            }

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(e.getMessage());
            throw new TramitacionException("Error. TramitacionDAO.recuperarExpAsociados", e);
        } finally {
                SigpGeneralOperations.closeResultSet(rs);
                SigpGeneralOperations.closeStatement(stmt);
        }
        return resultado;
   } 
    
    
}
