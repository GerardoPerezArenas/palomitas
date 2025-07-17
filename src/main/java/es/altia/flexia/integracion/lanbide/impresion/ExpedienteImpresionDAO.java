/*     */ package es.altia.flexia.integracion.lanbide.impresion;
/*     */ 
/*     */ import es.altia.agora.business.escritorio.UsuarioValueObject;
/*     */ import es.altia.agora.business.registro.exception.AnotacionRegistroException;
/*     */ import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
/*     */ import es.altia.agora.business.sge.SiguienteTramiteTO;
/*     */ import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
/*     */ import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
/*     */ import es.altia.agora.business.util.GeneralValueObject;
/*     */ import es.altia.agora.business.util.GlobalNames;
/*     */ import es.altia.common.exception.TechnicalException;
/*     */ import es.altia.flexia.integracion.lanbide.impresion.util.ConstantesExpedienteImpresion;
/*     */ import es.altia.util.commons.DateOperations;
/*     */ import es.altia.util.conexion.AdaptadorSQLBD;
/*     */ import es.altia.util.conexion.BDException;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
import java.util.List;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Vector;
import java.util.logging.Level;
/*     */ 
/*     */ public class ExpedienteImpresionDAO
/*     */ {
/*     */   protected static String idiomaDefecto;
/*  44 */   private static ExpedienteImpresionDAO instance = null;
/*  45 */   private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ExpedienteImpresionDAO.class);
/*     */ /*     */ /*     */ /*     */ 
/*     */   public static ExpedienteImpresionDAO getInstance()
            {
                if (instance == null)
                    instance = new ExpedienteImpresionDAO();
                return instance;
            }
/*     */ 
/*     */   public Vector getExpedientesImpresion(int codOrganizacion, String codProcedimiento, int codTramite, Connection con) {
/*  55 */     Vector lista = new Vector();
/*  56 */     Statement st = null;
/*  57 */     ResultSet rs = null;
/*     */ 
/*  59 */     this.log.debug(" ===================> ExpeienteImpresionDAO.getExpedientesImpresion codOrganizacion: " + codOrganizacion);
/*  60 */     this.log.debug(" ===================> ExpeienteImpresionDAO.getExpedientesImpresion codProcedimiento: " + codProcedimiento);
/*  61 */     this.log.debug(" ===================> ExpeienteImpresionDAO.getExpedientesImpresion codTramite: " + codTramite);
/*     */     try
/*     */     {
                String sql ="SELECT * FROM (SELECT DISTINCT EXP_NUM,  EXP_EJE,  EXP_MUN,  TFE_VALOR\n" +
                    "FROM E_EXP\n" +
                    "LEFT JOIN E_TFE ON TFE_NUM   = EXP_NUM AND TFE_EJE = EXP_EJE AND TFE_MUN = EXP_MUN AND TFE_COD  = 'FECHAPRESENTACION'\n" +
                    "INNER JOIN E_CRO ON EXP_NUM = CRO_NUM AND CRO_PRO=EXP_PRO  AND CRO_MUN  =EXP_MUN  AND \n" +
                    "  CRO_EJE  =EXP_EJE AND CRO_FEF IS NULL  AND CRO_TRA  ="+ codTramite +"\n" +
                    "WHERE EXP_PRO='" + codProcedimiento + "' AND EXP_FEF IS NULL AND EXP_MUN  =" + codOrganizacion + "\n" +
                    "ORDER BY TFE_VALOR ASC ) "
                        + "WHERE ROWNUM < "+ ConstantesExpedienteImpresion.NUM_MAX;
                /*String sql = "SELECT EXP_NUM,EXP_EJE,EXP_MUN, TFE_VALOR FROM E_EXP "
                        + "LEFT JOIN E_TFE ON TFE_NUM = EXP_NUM AND TFE_COD = 'FECHAPRESENTACION'WHERE EXP_PRO='" + codProcedimiento + "' "
                        + "AND EXP_FEF IS NULL AND EXP_MUN=" + codOrganizacion + " AND EXP_NUM IN (SELECT CRO_NUM FROM E_CRO " + 
                        "WHERE CRO_PRO=EXP_PRO AND CRO_MUN=EXP_MUN AND CRO_EJE=EXP_EJE AND CRO_FEF IS NULL AND CRO_TRA=" + codTramite + ")"
                        + " AND rownum < 100 ORDER BY TFE_VALOR ASC";*/
/*  65 */       /*String sql = "SELECT EXP_NUM,EXP_EJE,EXP_MUN, TFE_VALOR, SOLICI, res_fec, "
                    + " CASE WHEN EXR_EJR IS NOT NULL THEN exr_ejr||'/'||exr_nre ELSE '' END AS NUMREG "
                    + "FROM E_EXP "
                    + "LEFT JOIN E_TFE ON TFE_NUM = EXP_NUM AND TFE_COD = 'FECHAPRESENTACION' "
                    + " LEFT JOIN e_exr on EXP_NUM =  exr_num AND EXR_TOP = '0' " +
                    " LEFT JOIN r_res ON exr_ejr  = res_eje AND exr_tip  = res_tip AND exr_uor  = res_uor AND exr_nre  = res_num"
                    + " LEFT JOIN ( SELECT HTE_AP2 ||' '|| HTE_AP1||', '||HTE_NOM AS SOLICI, E_EXT.EXT_NUM AS NUM,"
                    + " E_EXT.EXT_EJE AS EJE, E_EXT.EXT_MUN AS MUN  FROM E_EXT "
                    + " INNER JOIN T_HTE ON E_EXT.EXT_TER=HTE_TER AND E_EXT.EXT_NVR=HTE_NVR "
                  + ") ON NUM = EXP_NUM AND EJE = EXP_EJE AND MUN = EXP_MUN "
                   + "WHERE EXP_PRO='" + codProcedimiento + "' AND EXP_FEF IS NULL AND EXP_MUN=" + codOrganizacion + 
                    " AND EXP_NUM IN (SELECT CRO_NUM FROM E_CRO " + 
                    "WHERE CRO_PRO=EXP_PRO AND CRO_MUN=EXP_MUN AND CRO_EJE=EXP_EJE AND CRO_FEF IS NULL AND CRO_TRA=" + codTramite + ")" + 
                    " AND rownum < 100 ORDER BY TFE_VALOR ASC nulls last";*/
                    
/*     */ 
/*  72 */       this.log.debug("ExpedienteImpresionDAO:getExpedientesImpresion: " + sql);
/*     */ 
/*  75 */       st = con.createStatement();
/*  76 */       rs = st.executeQuery(sql);
/*     */ 
/*  78 */       AnotacionRegistroDAO anotacionDAO = AnotacionRegistroDAO.getInstance();
/*     */ 
/*  80 */       while (rs.next()) {
/*  81 */         GeneralValueObject gVO = new GeneralValueObject();
/*  82 */         String numExpediente = rs.getString("EXP_NUM");
/*  83 */         String ejercicio = rs.getString("EXP_EJE");
/*  84 */         String codMunicipio = rs.getString("EXP_MUN");
                  //String interesado = rs.getString("SOLICI");
                  String fecPres = "";Date date = new Date();
                  fecPres = rs.getString("TFE_VALOR");
                  Date fec = null;
                  SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                  if(rs.getString("TFE_VALOR") != null && !rs.getString("TFE_VALOR").equals("")){
                      fec = rs.getDate("TFE_VALOR");
                      try
                      {                          
                            fecPres = formatoFecha.format(fec);
                      }catch(Exception e){this.log.debug("Error en getExpedientesImpresion: " + e);}
                      //fecPres = fec.toString();                      
                  }
                  
                  this.log.debug("despues de dar formato a la fecha");
/*  86 */         GeneralValueObject anotacion = anotacionDAO.getAnotacionMasAntigua(numExpediente, con);
/*  87 */         String fechaEntrada = (String)anotacion.getAtributo("FECHA_ENTRADA_REGISTRO_ANOTACION");
/*  88 */         String numRegistro = (String)anotacion.getAtributo("NUMERO_REGISTRO_ANOTACION");
                  //String fechaEntrada = rs.getString("res_fec");
                  //fec = rs.getDate("res_fec");
                  //if(fechaEntrada != null && !fechaEntrada.equals(""))
                      //fechaEntrada = formatoFecha.format(fec);
                  //String numRegistro = rs.getString("NUMREG");
/*     */ 
/*  90 */         gVO.setAtributo("numExpediente", numExpediente);
/*     */ 
/*  92 */         gVO.setAtributo("numRegistro", numRegistro);
/*  93 */         gVO.setAtributo("fechaEntrada", fechaEntrada);
/*  94 */         gVO.setAtributo("ejercicio", ejercicio);
/*  95 */         gVO.setAtributo("codMunicipio", codMunicipio);
/*  95 */         gVO.setAtributo("fecPres", fecPres);
                  //gVO.setAtributo("interesados", interesado);
/*  96 */         lista.add(gVO);
/*     */       }
/*     */ 
/* 100 */       for (int j = 0; j < lista.size(); j++) {
/* 101 */         GeneralValueObject exp = (GeneralValueObject)lista.get(j);
/* 102 */         String numExpediente = (String)exp.getAtributo("numExpediente");
/* 103 */         String ejercicio = (String)exp.getAtributo("ejercicio");
/* 104 */         String codMunicipio = (String)exp.getAtributo("codMunicipio");
/*     */ 
/* 106 */         ArrayList interesados = getListaInteresadosExpediente(Integer.parseInt(codMunicipio), numExpediente, Integer.parseInt(ejercicio), con);
/*     */ 
/* 108 */         String interesado = "";
                for (int z = 0; z < interesados.size(); z++) {
                  interesado = interesado + (String)interesados.get(z);
                  if (interesados.size() - z <= 1) continue; interesado = interesado + "<br>";
                }
               exp.setAtributo("interesados", interesado);
              }
/*     */ 
/* 117 */       this.log.debug("getExpedientesImpresion: lista resultante: " + lista.toString());
/*     */     } catch (SQLException e) {
/* 119 */       e.printStackTrace();
/*     */     }
             catch (TechnicalException e)
            {
              e.printStackTrace();
            }
            catch (AnotacionRegistroException e)
            {
              e.printStackTrace();
            }   
            finally {
/*     */       try {
/* 126 */         if (st != null) st.close();
/* 127 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (SQLException e) {
/* 129 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 133 */     return lista;
/*     */   }
/*     */ 
/*     */   public Vector getFicherosImpresionGenerados(Connection con)
/*     */   {
/* 138 */     Vector lista = new Vector();
/* 139 */     Statement st = null;
/* 140 */     ResultSet rs = null;
/*     */     try
/*     */     {
/* 144 */       ResourceBundle config = ResourceBundle.getBundle("impresionLanbide");
/* 145 */       String sql = "SELECT NOMBRE_FICHERO, FECHA_GENERACION  FROM MELANBIDE03_IMPRESION_CEPAP ORDER BY FECHA_GENERACION desc, NOMBRE_FICHERO desc";
/*     */ 
/* 148 */       this.log.debug("ExpedienteImpresionDAO:getFicherosImpresionGenerados " + sql);
/* 149 */       st = con.createStatement();
/* 150 */       rs = st.executeQuery(sql);
/*     */ 
/* 152 */       while (rs.next()) {
/* 153 */         GeneralValueObject gVO = new GeneralValueObject();
/* 154 */         gVO.setAtributo("numExpediente", rs.getString("NOMBRE_FICHERO"));
/* 155 */         SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
/* 156 */         String fechGeneracion = formato.format(rs.getDate("FECHA_GENERACION"));
/* 157 */         gVO.setAtributo("fechaGeneracion", fechGeneracion);
/* 158 */         lista.add(gVO);
/*     */       }
/* 160 */       this.log.debug("ExpedienteImpresionDAO:getFicherosImpresionGenerados: lista resultante: " + lista.toString());
/*     */     } catch (SQLException e) {
/* 162 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 165 */         if (st != null) st.close();
/* 166 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (SQLException e) {
/* 168 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 172 */     return lista;
/*     */   }
/*     */ 
/*     */   private ImpresionExpedientesLanbideValueObject rellenarDatosCertificado(int codOrganizacion, ImpresionExpedientesLanbideValueObject dato, Connection con)
/*     */   {
/* 185 */     Statement st = null;
/* 186 */     ResultSet rs = null;
/*     */     try
/*     */     {
/* 190 */       this.log.debug("ExpedientesImpresionDAO.rellenarDatosCertificado ======>");
/* 191 */       ResourceBundle config = ResourceBundle.getBundle("impresionLanbide");
/* 192 */       String TABLA_CERTIFICADOS = "S_CER_CERTIFICADOS"; //config.getString(codOrganizacion + "/TABLA_DATOS_CERTIFICADO");
/*     */ 
/* 195 */       String sql = "SELECT MELANBIDE03_CERTIFICADO.COD_CERTIFICADO,DECRETO,TO_CHAR(FECHA_RD,'dd/mm/yyyy') as FECHA_RD,DESCERTIFICADO_C," + 
        "DESCERTIFICADO_E,NIVEL, DECRETO_MOD, TO_CHAR(FECHA_MODIF_RD,'dd/mm/yyyy') as FECHA_MODIF_RD " +
            "FROM MELANBIDE03_CERTIFICADO," + GlobalNames.ESQUEMA_GENERICO + TABLA_CERTIFICADOS + " WHERE NUM_EXPEDIENTE='" + dato.getNumExpediente() + "' " + "AND MELANBIDE03_CERTIFICADO.COD_CERTIFICADO=" + GlobalNames.ESQUEMA_GENERICO + TABLA_CERTIFICADOS + ".CODCERTIFICADO";
/*     */ 
/* 199 */       this.log.debug(sql);
/* 200 */       st = con.createStatement();
/* 201 */       rs = st.executeQuery(sql);
/* 202 */       while (rs.next()) {
/* 203 */         String decreto = rs.getString("DECRETO");
/* 204 */         String desCertificadoC = rs.getString("DESCERTIFICADO_C");
/* 205 */         //Timestamp fechaRD = rs.getTimestamp("FECHA_RD");
/* 206 */         String desCertificadoE = rs.getString("DESCERTIFICADO_E");
                  String decretoMod = rs.getString("DECRETO_MOD");
                  String nivel = rs.getString("NIVEL");
/*     */ 
/* 208 */         dato.setCodigoCP(rs.getString("COD_CERTIFICADO"));
                  //dato.setDecretoMod(rs.getString("DECRETO_MOD"));
                  if ((decretoMod != null) && (!"".equals(decretoMod))) dato.setDecretoMod(decretoMod); else dato.setDecretoMod("");
                  SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                  String fechaRD = "";
                  if(rs.getString("FECHA_RD") != null)
                    fechaRD = rs.getString("FECHA_RD");
                  dato.setFechaRD(fechaRD);
                  
/* 156 */         String fecha = "";
                  if(rs.getString("FECHA_MODIF_RD") != null)
                    fecha = rs.getString("FECHA_MODIF_RD");
                  dato.setFechaDecretoMod(fecha);
/*     */ 
/* 211 */         dato.setRealDecreto("");
/* 212 */         if (decreto != null) dato.setRealDecreto(decreto);
/*     */ 
/* 214 */         dato.setNombreCertificadoCastellano("");
/* 215 */         if (desCertificadoC != null) dato.setNombreCertificadoCastellano(desCertificadoC);
/*     */ 
/* 217 */         if ((desCertificadoE != null) && (!"".equals(desCertificadoE))) dato.setNombreCertificadoEuskera(desCertificadoE);
/*     */           
/* 219 */         //dato.setNivel(rs.getString("NIVEL"));
                  if ((nivel != null) && (!"".equals(nivel))) dato.setNivel(nivel); else dato.setNivel("");
/* 220 */         dato.setTraduccionRD("");
/*     */ 
/* 222 */         /*dato.setFechaRD("");
         if (fechaRD != null) {
           Calendar cFechaRD = DateOperations.timestampToCalendar(fechaRD);
           if (cFechaRD != null) {
             SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
             dato.setFechaRD(sf.format(cFechaRD.getTime()));
           }
         }*/
/*     */       }
/*     */     } 
            catch (Exception e) {
                this.log.error("ExpedientesImpresionDAO.rellenarDatosCertificado: " + e.getMessage());
                e.printStackTrace();
              }
/*catch (SQLException e) {
       this.log.error("ExpedientesImpresionDAO.rellenarDatosCertificado: " + e.getMessage());
       e.printStackTrace();
     }*/ finally {
/*     */       try {
/* 236 */         if (st != null) st.close();
/* 237 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (SQLException e) {
/* 239 */         this.log.error("ExpedientesImpresionDAO.rellenarDatosCertificado: Error al liberar recursos asociados a la conexi√≥n de BD: " + e.getMessage());
/* 240 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 243 */     this.log.debug("ExpedientesImpresionDAO.rellenarDatosCertificado <======");
/* 244 */     return dato;
/*     */   }
/*     */ 
/*     */   public String getValorCampoDesplegableTercero(String codTercero, String codCampo, Connection con)
/*     */   {
/* 249 */     Statement st = null;
/* 250 */     ResultSet rs = null;
/* 251 */     String valor = null;
/*     */ 
/* 253 */     String sql = "SELECT VALOR FROM T_CAMPOS_DESPLEGABLE WHERE COD_CAMPO='" + codCampo + "' AND COD_TERCERO=" + codTercero;
/*     */     try {
/* 255 */       st = con.createStatement();
/* 256 */       rs = st.executeQuery(sql);
/* 257 */       while (rs.next())
/* 258 */         valor = rs.getString("VALOR");
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/* 262 */       e.printStackTrace();
/* 263 */       this.log.error("Error al recuperar valor del campo desplegable de tercero: " + e.getMessage());
/*     */     } finally {
/*     */       try {
/* 266 */         if (st != null) st.close();
/* 267 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (SQLException e) {
/* 269 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 272 */     return valor;
/*     */   }
/*     */ 
/*     */   public String getValorFechaTercero(String codTercero, String codCampo, Connection con)
/*     */   {
/* 278 */     Statement st = null;
/* 279 */     ResultSet rs = null;
/* 280 */     String valor = null;
/*     */ 
/* 282 */     String sql = "SELECT VALOR FROM T_CAMPOS_FECHA WHERE COD_CAMPO='" + codCampo + "' AND COD_TERCERO=" + codTercero;
/*     */     try {
/* 284 */       st = con.createStatement();
/* 285 */       rs = st.executeQuery(sql);
/* 286 */       while (rs.next()) {
/* 287 */         Timestamp time = rs.getTimestamp("VALOR");
/* 288 */         if (time != null) {
/* 289 */           Calendar c = DateOperations.timestampToCalendar(time);
/*     */ 
/* 291 */           SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
/* 292 */           valor = sf.format(c.getTime());
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SQLException e) {
/* 297 */       e.printStackTrace();
/* 298 */       this.log.error("Error al recuperar valor del campo desplegable de tercero: " + e.getMessage());
/*     */     } finally {
/*     */       try {
/* 301 */         if (st != null) st.close();
/* 302 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (SQLException e) {
/* 304 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 307 */     return valor;
/*     */   }
/*     */ 
/*     */   public String getValorTextoCampo(String codCampo, String numExpediente, Connection con)
/*     */   {
/* 314 */     Statement st = null;
/* 315 */     ResultSet rs = null;
/* 316 */     String valor = null;
/*     */ 
/* 318 */     String sql = "SELECT TXT_VALOR FROM E_TXT WHERE TXT_COD='" + codCampo + "' AND TXT_NUM='" + numExpediente + "'";
/* 319 */     this.log.debug(sql);
/*     */     try
/*     */     {
/* 322 */       st = con.createStatement();
/* 323 */       rs = st.executeQuery(sql);
/* 324 */       while (rs.next())
/* 325 */         valor = rs.getString("TXT_VALOR");
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/* 329 */       e.printStackTrace();
/* 330 */       this.log.error("Error al recuperar valor del campo de texto: " + e.getMessage());
/*     */     } finally {
/*     */       try {
/* 333 */         if (st != null) st.close();
/* 334 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (SQLException e) {
/* 336 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 339 */     return valor;
/*     */   }
/*     */ /*     */ /*     */ /*     */ 
/*     */   public Vector consultar(String codExpediente, String tSexo, String tFechNacimiento, UsuarioValueObject usuario, Connection con)
/*     */            {
/* 345 */     Vector lista = new Vector();
/* 346 */     Statement st = null;
/* 347 */     ResultSet rs = null;
/*     */ 
/* 349 */     String sexo = "";
/* 350 */     String fNac = "";
/* 351 */     String numExp = "";
/* 352 */     String nombre = "";
/* 353 */     String apellido1 = "";
/* 354 */     String apellido2 = "";
/* 355 */     String claveReg = "";
/* 356 */     String fechExpediente = "";
              String dni = "";
              String observaciones = "";
              String provincia = "";
/*     */       this.log.error(" ****************** Consulta datos para la generacion del excel ****************** " );
/* 359 */     ResourceBundle configImpresionLanbide = ResourceBundle.getBundle("impresionLanbide");
/* 360 */     String tSexoHombre = usuario.getOrgCod() + "/CODIGO_CAMPO_TSEXOTERCERO_HOMBRE";
/* 361 */     String rSexoHombre = configImpresionLanbide.getString(tSexoHombre);
/* 362 */     String tSexoMujer = usuario.getOrgCod() + "/CODIGO_CAMPO_TSEXOTERCERO_MUJER";
/* 363 */     String rSexoMujer = configImpresionLanbide.getString(tSexoMujer);
/* 364 */     String valorSexoHombre = usuario.getOrgCod() + "/VALOR_CAMPO_TSEXOTERCERO_HOMBRE";
/* 365 */     String rValorSexoHombre = configImpresionLanbide.getString(valorSexoHombre);
/* 366 */     String valorSexoMujer = usuario.getOrgCod() + "/VALOR_CAMPO_TSEXOTERCERO_MUJER";
/* 367 */     String rValorSexoMujer = configImpresionLanbide.getString(valorSexoMujer);
/*     */ 
/* 369 */     String COD_CAMPO_SEXO_TERCERO = configImpresionLanbide.getString(usuario.getOrgCod() + "/CODIGO_CAMPO_TSEXOTERCERO");
/* 370 */     String COD_CAMPO_FECHANAC_TERCERO = configImpresionLanbide.getString(usuario.getOrgCod() + "/CODIGO_CAMPO_TFECHANACIMIENTO");
/* 371 */     String COD_CAMPO_CLAVE_REGISTRAL = configImpresionLanbide.getString(usuario.getOrgCod() + "/CODIGO_CAMPO_CLAVEREGISTRAL");
/*     */     try
/*     */                    {
/* 374 */       //String sql = "SELECT EXT_NUM,HTE_TER, HTE_NOM AS nombre, NVL(HTE_PA1,'') ||  NVL('','') ||  NVL(HTE_AP1,'') AS ap1,  NVL(HTE_PA2,'') ||  NVL('','') ||  NVL(HTE_AP2,'') AS ap2 FROM E_EXT,T_HTE  WHERE EXT_NUM IN (" + codExpediente + ")" + " AND EXT_TER =HTE_TER(+)" + " AND EXT_NVR = HTE_NVR(+)";
    
                //Leire, cambio de sentencia
/*     */       /*String sql = "SELECT HTE_DOC, EXT_NUM,HTE_TER, HTE_NOM AS nombre, NVL(HTE_PA1,'') ||  NVL('','') ||  NVL(HTE_AP1,'') AS ap1,  " +
                        "NVL(HTE_PA2,'') ||  NVL('','') ||  NVL(HTE_AP2,'') AS ap2, TO_CHAR(EXP_FEI,'dd/mm/yyyy') as EXP_FEI, " +
                        "CASE WHEN TDE_VALOR = 'CEFO' THEN " + 
                        "(SELECT TDE_VALOR || '#' || DES_NOM FROM E_TDE " +
                        "INNER JOIN E_DES_VAL ON DES_vAL_COD = TDE_VALOR AND DES_COD = 'CENT' " +
                        "WHERE EXT_NUM = TDE_NUM AND TDE_COD = 'CENTRO') " +
                        //"WHEN TDE_VALOR = 'INTE' or TDE_VALOR = 'OFIC' THEN COD_OFICINA || '#' || DESCRIP " +
                        "ELSE  COD_OFICINA || '#' || DESCRIP END AS DESTINO,TXT_VALOR AS  CLAVE, TO_CHAR(TFE_VALOR,'dd/mm/yyyy') AS FECPRES " +
                        "FROM E_EXT " +
                        "INNER JOIN E_EXP ON EXP_NUM = EXT_NUM " +
                        "LEFT JOIN T_HTE ON EXT_TER =HTE_TER AND EXT_NVR = HTE_NVR " +
                        "LEFT JOIN T_DOT ON EXT_TER = DOT_TER AND EXT_DOT=DOT_DOM " +
                        "LEFT JOIN T_DNN ON  DOT_DOM = DNN_DOM " +
                        "LEFT JOIN CP_OFICINAS ON CP_OFICINAS.COD_POSTAL = DNN_CPO " +
                        "LEFT JOIN E_TDE ON EXT_NUM = TDE_NUM AND TDE_COD = 'VIASOLIC' " +
                        "LEFT JOIN E_TFE ON TFE_NUM = EXT_NUM AND TFE_COD = 'FECHAPRESENTACION' " +
                        "LEFT JOIN E_TXT ON TXT_COD = 'CLAVEREGISTRALCP' AND TXT_NUM = EXT_NUM" +
                        " WHERE EXT_NUM IN (" + codExpediente + ") ORDER BY TFE_VALOR " ;*/
                /*String sql = "SELECT EXT_NUM,HTE_TER, HTE_NOM AS nombre, NVL(HTE_PA1,'') ||  NVL('','') ||  NVL(HTE_AP1,'') AS ap1,  NVL(HTE_PA2,'') ||  NVL('','') ||  NVL(HTE_AP2,'') AS ap2, " +
                        "EXP_FEI, COD_OFICINA, DESCRIP " +
                        "FROM E_EXT " +
                        "INNER JOIN E_EXP ON EXP_NUM = EXT_NUM " +
                        "LEFT JOIN T_HTE ON EXT_TER =HTE_TER AND EXT_NVR = HTE_NVR " +
                        "LEFT JOIN T_DOT ON EXT_TER = DOT_TER   " +
                        "LEFT JOIN T_DNN ON  DOT_DOM = DNN_DOM " +
                        "LEFT JOIN CP_OFICINAS ON COD_POSTAL = DNN_CPO " +
                        " WHERE EXT_NUM IN (" + codExpediente + ")" ;*/
                //Sergio Lamas, como si fuese siempre 'INTE'
                String sql = "SELECT HTE_DOC, prv_nom AS PROVINCIA, EXT_NUM, REPLACE(SUBSTR(EXP_OBS,1,10), '<', ' ') as observ, HTE_TER, HTE_NOM AS nombre, NVL(HTE_PA1,'') ||  NVL('','') ||  NVL(HTE_AP1,'') AS ap1,  " +
                        "NVL(HTE_PA2,'') ||  NVL('','') ||  NVL(HTE_AP2,'') AS ap2, TO_CHAR(EXP_FEI,'dd/mm/yyyy') as EXP_FEI, " +
                        "COD_OFICINA || '#' || DESCRIP AS DESTINO, TXT_VALOR AS  CLAVE, TO_CHAR(TFE_VALOR,'dd/mm/yyyy') AS FECPRES " +
                        "FROM E_EXT " +
                        "INNER JOIN E_EXP ON EXP_NUM = EXT_NUM " +
                        "LEFT JOIN T_HTE ON EXT_TER =HTE_TER AND EXT_NVR = HTE_NVR " +
                        "LEFT JOIN T_DOT ON EXT_TER = DOT_TER AND EXT_DOT=DOT_DOM " +
                        "LEFT JOIN T_DNN ON  DOT_DOM = DNN_DOM " +
                        "LEFT JOIN t_prv on DNN_PRV=t_prv.prv_cod " +
                        "LEFT JOIN CP_OFICINAS ON CP_OFICINAS.COD_POSTAL = DNN_CPO " +
                        "LEFT JOIN E_TDE ON EXT_NUM = TDE_NUM AND TDE_COD = 'VIASOLIC' " +
                        "LEFT JOIN E_TFE ON TFE_NUM = EXT_NUM AND TFE_COD = 'FECHAPRESENTACION' " +
                        "LEFT JOIN E_TXT ON TXT_COD = 'CLAVEREGISTRALCP' AND TXT_NUM = EXT_NUM" +
                        " WHERE EXT_NUM IN (" + codExpediente + ") ORDER BY TFE_VALOR " ;
    
/* 380 */       this.log.debug("ExpedienteImpresionDAOgetExpedientesImpresion:consulta datos expedientes:" + sql);
/* 381 */       st = con.createStatement(1004, 1007);
/* 382 */       rs = st.executeQuery(sql);
/* 383 */       while (rs.next()) {
/* 384 */         ImpresionExpedientesLanbideValueObject gVO = new ImpresionExpedientesLanbideValueObject();
/* 385 */         String codTercero = rs.getString("HTE_TER");
/* 386 */         numExp = rs.getString("EXT_NUM");
/*     */ 
/* 388 */         nombre = rs.getString("nombre");
/* 389 */         apellido1 = rs.getString("ap1");
/* 390 */         apellido2 = rs.getString("ap2");
                  dni = rs.getString("HTE_DOC");
                  observaciones = rs.getString("observ");
                  provincia = rs.getString("PROVINCIA");
                  String auxNom = nombre;
                  String auxApe1 = apellido1;
                  String auxApe2 = apellido2;
                  String original = "·‡‰ÈËÎÌÏÔÛÚˆ˙˘u¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹Á«";
                  String ascii = "aaaeeeiiiooouuuAAAEEEIIIOOOUUUcCu";

                if ((nombre != null) && (!"".equals(nombre))) {
                    //Recorro la cadena y remplazo los caracteres originales por aquellos sin acentos
                    for (int i = 0; i < original.length(); i++ ) {
                        auxNom = auxNom.replace(original.charAt(i), ascii.charAt(i));
                    }
                }
                    
                if ((apellido1 != null) && (!"".equals(apellido1))) {
                    //Recorro la cadena y remplazo los caracteres originales por aquellos sin acentos
                    for (int i = 0; i < original.length(); i++ ) {
                        auxApe1 = auxApe1.replace(original.charAt(i), ascii.charAt(i));
                    }
                }
                    
                if ((apellido2 != null) && (!"".equals(apellido2))) {
                    //Recorro la cadena y remplazo los caracteres originales por aquellos sin acentos
                    for (int i = 0; i < original.length(); i++ ) {
                        auxApe2 = auxApe2.replace(original.charAt(i), ascii.charAt(i));
                    }
                }

                  
/*     */ 
/* 392 */         claveReg = rs.getString("CLAVE");//getValorTextoCampo(COD_CAMPO_CLAVE_REGISTRAL, numExp, con);
/* 393 */         sexo = getValorCampoDesplegableTercero(codTercero, COD_CAMPO_SEXO_TERCERO, con);
/* 394 */         if (sexo != null) {
/* 395 */           if (sexo.equals(rSexoHombre)) {
/* 396 */             sexo = "1";
/*     */           }
/* 398 */           else if (sexo.equals(rSexoMujer)) {
/* 399 */             sexo = "2";

/*     */           }
/*     */ 
/*     */         }
/*     */           this.log.debug("recogiendo datos funcion consultar()");
/* 404 */         fNac = getValorFechaTercero(codTercero, COD_CAMPO_FECHANAC_TERCERO, con);
/* 405 */         GeneralValueObject anotacion = new GeneralValueObject();
/*     */         try {
/* 407 */           anotacion = AnotacionRegistroDAO.getInstance().getAnotacionMasAntigua(numExp, con);
/*     */         } catch (AnotacionRegistroException ex) {
/* 409 */           java.util.logging.Logger.getLogger(ExpedienteImpresionDAO.class.getName()).log(Level.SEVERE, null, ex);
/*     */         } catch (TechnicalException ex) {
/* 411 */           java.util.logging.Logger.getLogger(ExpedienteImpresionDAO.class.getName()).log(Level.SEVERE, null, ex);
/*     */         }
/*     */ 
/* 414 */         fechExpediente = (String)rs.getString("FECPRES");
/* 415 */         //if (claveReg != null)
/* 416 */           //gVO.setClaveRegistral(claveReg);
/*     */         //else {
/* 418 */           //gVO.setClaveRegistral("");
/*     */         //}
                  gVO.setClaveRegistral(claveReg);
/* 420 */         if ((dni != null) && (!"".equals(dni))) gVO.setDni(dni); else gVO.setDni("");
                  if ((observaciones != null) && (!"".equals(observaciones))) gVO.setObserv(observaciones); else gVO.setObserv("");
/* 420 */         if ((nombre != null) && (!"".equals(nombre))) gVO.setNombreInteresado(auxNom); else gVO.setNombreInteresado("");
/* 421 */         if ((apellido1 != null) && (!"".equals(apellido1))) gVO.setApellido1Interesado(auxApe1); else gVO.setApellido1Interesado("");
/* 422 */         if ((apellido2 != null) && (!"".equals(apellido2))) gVO.setApellido2Interesado(auxApe2); else gVO.setApellido2Interesado("");
/* 423 */         if ((sexo != null) && (!"".equals(sexo))) gVO.setSexo(sexo); else gVO.setSexo("");
/* 424 */         if ((fNac != null) && (!"".equals(fNac))) gVO.setFechNacimiento(fNac); else gVO.setFechNacimiento("");
/* 425 */         if ((provincia != null) && (!"".equals(provincia))) gVO.setProvinciaInteresado(provincia); else gVO.setProvinciaInteresado("");
/*     */ 
/* 426 */         if ((fechExpediente != null) && (!"".equals(fechExpediente))) gVO.setFechaExpedicion(fechExpediente); else gVO.setFechaExpedicion("");
/* 427 */         gVO.setNumExpediente(numExp);

                    //LEIRE: aÒado la fecha de expediente y datos del centro
                  SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                  //String fechExp = formato.format(rs.getDate("EXP_FEI"));
                    gVO.setFechaExpediente(rs.getString("EXP_FEI"));
                    //centro
                    String centro = rs.getString("DESTINO");
                    this.log.debug("recogemos datos del destino: "+centro );
                    if(centro != null && centro.contains("#") && centro.length() > 2){
                        String []cent = centro.split("#");
                        gVO.setCodCentro(cent[0]);
                        gVO.setDesCentro(cent[1]);
                    }
                    else{
                        gVO.setCodCentro("0");
                        gVO.setDesCentro("FUERA DE LA CCAA");
                    }

                    //gVO.setDesCentro(rs.getString("DESCRIP"));
    /*     */ 
    /*     */           this.log.debug("antes de rellenarDatosCertificado funcion consultar()");
/* 429 */         gVO = rellenarDatosCertificado(usuario.getOrgCod(), gVO, con);
/* 430 */         lista.add(gVO);
/*     */       }
/* 432 */       this.log.debug("getExpedientesImpresion: lista resultante: " + lista.toString());
/*     */     }
                catch (Exception ex){
                    this.log.debug("Error en funcion consultar: " + ex);
                    
                }
                //catch (SQLException e) {
/* 434 */       //e.printStackTrace();
                //this.log.debug("Error en funcion consultar: " + e);
/*     */      // } 
               finally {
/*     */       try {
/* 437 */         if (st != null) st.close();
/* 438 */         if (rs != null) rs.close(); 
/*     */       this.log.error(" ****************** Fin de consulta datos para la generacion del excel ****************** " );
/*     */       }
/*     */       catch (SQLException e) {
/* 440 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 444 */     return lista;
/*     */   }
/*     */ 
/*     */   public int finalizarTramiteImpresion(AdaptadorSQLBD oad, Connection con, TramitacionExpedientesValueObject teVO)
/*     */     throws SQLException, TechnicalException, BDException
/*     */   {
/* 450 */     GeneralValueObject gVO = tramitacionExpedientesVO(teVO);
/*     */ 
/* 452 */     this.log.debug(TramitesExpedienteDAO.class.getName() + "--> finalizarTramiteImpresion");
/* 453 */     int resultado = TramitesExpedienteDAO.getInstance().actualizarTramite(oad, con, teVO);
/* 454 */     if (resultado > 0);
/* 457 */     this.log.debug(TramitesExpedienteDAO.class.getName() + "<-- finalizarTramiteImpresion");
/* 458 */     return resultado;
/*     */   }
/*     */ 
/*     */   private GeneralValueObject tramitacionExpedientesVO(TramitacionExpedientesValueObject teVO)
/*     */   {
/* 463 */     GeneralValueObject gVO = new GeneralValueObject();
/* 464 */     gVO.setAtributo("numeroRelacion", teVO.getNumeroRelacion());
/* 465 */     gVO.setAtributo("codMunicipio", teVO.getCodMunicipio());
/* 466 */     gVO.setAtributo("codProcedimiento", teVO.getCodProcedimiento());
/* 467 */     gVO.setAtributo("ejercicio", teVO.getEjercicio());
/* 468 */     gVO.setAtributo("numero", teVO.getNumero());
/* 469 */     gVO.setAtributo("ocurrenciaTramite", teVO.getOcurrenciaTramite());
/* 470 */     gVO.setAtributo("usuario", teVO.getCodUsuario());
/* 471 */     gVO.setAtributo("codEntidad", teVO.getCodEntidad());
/* 472 */     gVO.setAtributo("codOrganizacion", teVO.getCodOrganizacion());
/* 473 */     gVO.setAtributo("codTramite", teVO.getCodTramite());
/* 474 */     gVO.setAtributo("fechaInicio", teVO.getFechaInicio());
/* 475 */     gVO.setAtributo("fechaInicioPlazo", teVO.getFechaInicioPlazo());
/* 476 */     gVO.setAtributo("fechaFinPlazo", teVO.getFechaFinPlazo());
/* 477 */     gVO.setAtributo("fechaLimite", teVO.getFechaLimite());
/* 478 */     gVO.setAtributo("fechaFinTramite", teVO.getFechaFin());
/* 479 */     gVO.setAtributo("observaciones", teVO.getObservaciones());
/* 480 */     gVO.setAtributo("codUnidadOrganicaExp", teVO.getCodUnidadOrganicaExp());
/* 481 */     gVO.setAtributo("codUnidadTramitadoraTram", teVO.getCodUnidadTramitadoraTram());
/* 482 */     gVO.setAtributo("codUnidadTramitadoraManual", teVO.getCodUnidadTramitadoraManual());
/* 483 */     gVO.setAtributo("codUnidadTramitadoraUsu", teVO.getCodUnidadTramitadoraUsu());
/* 484 */     gVO.setAtributo("codUnidadOrganica", teVO.getCodUOR());
/* 485 */     gVO.setAtributo("codInteresados", teVO.getVectorCodInteresados());
/* 486 */     gVO.setAtributo("usuario", teVO.getCodUsuario());
/* 487 */     gVO.setAtributo("bloqueo", teVO.getBloqueo());
/* 488 */     return gVO;
/*     */   }
            
            public String getCertificadoDuplicidad(String numExpediente, Connection con)
/*     */   {
/* 314 */     Statement st = null;
/* 315 */     ResultSet rs = null;
/* 316 */     String certificado = "";
/*     */ 
/* 318 */     String sql = "select cod_certificado "
                + "from melanbide03_certificado "
                + "where num_expediente = "+numExpediente+"";
/* 319 */     this.log.debug(sql);
/*     */     try
/*     */     {
/* 322 */       st = con.createStatement();
/* 323 */       rs = st.executeQuery(sql);
/* 324 */       while (rs.next())
/* 325 */         certificado = rs.getString("cod_certificado");
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/* 329 */       e.printStackTrace();
/* 330 */       this.log.error("Error al recuperar el CERTIFICADO del proceso DUPLICIDAD: " + e.getMessage());
/*     */     } finally {
/*     */       try {
/* 333 */         if (st != null) st.close();
/* 334 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (SQLException e) {
/* 336 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 339 */     return certificado;
/*     */   }
            
            public String compruebaExpedientes(String numExp, String certif, Connection con) throws Exception
        {
            Statement st = null;
            ResultSet rs = null;
            String hay = "0";       

            try
            {

                String sql = "Select Ext_Num, Ter_Doc, Ter_Nom, Ter_Ap1, Ter_Ap2,\n" +
                            "TER_TLF, TER_DCE, TER_NOC, TER_TID,EXT_ROL, COD_CERTIFICADO  \n" +
                            "FROM E_EXT \n" +
                            "Inner Join T_Ter On Ext_Ter = Ter_Cod\n" +
                            "Inner Join E_Tdet On Tdet_Num = Ext_Num And Tdet_Pro = Ext_Pro  And Tdet_Cod = 'VALORACION2' \n" +
                            "Inner Join E_Tde On Tde_Num = Ext_Num And Tde_Cod= 'TIPOACREDITACION'\n" +
                            "left JOIN MELANBIDE03_CERTIFICADO ON NUM_EXPEDIENTE = EXT_NUM AND COD_PROCEDIMIENTO = EXT_PRO\n" +
                            "Where (Tdet_Valor = 'T') And Ext_Rol = 1\n" +
                            "And Tde_Valor = 'CP' And Cod_Procedimiento = 'CEPAP'\n" +
                            "And Ter_Doc In (\n" +
                            "  Select Ter_Doc\n" +
                            "  From T_Ter\n" +
                            "  Inner Join E_Ext On Ext_Ter = Ter_Cod \n" +
                            "  Where Ext_Num = "+numExp+"\n" +
                            ") and Ext_Num <> "+numExp+" and COD_CERTIFICADO = '"+certif+"'";

               log.debug("compruebaExpedientes: " + sql);


               st = con.createStatement();
               rs = st.executeQuery(sql);

               if(rs.next()){
                   hay = rs.getString("Ext_Num");
               }           
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }// finally
            return hay;
            }
            
            public boolean cerrarTramitesExpediente(String numExp, Connection con) throws Exception {
                Statement st = null;
                ResultSet rs = null;
                String query = "";
                try {
                    query = "UPDATE E_CRO "
                            + "SET CRO_FEF = SYSDATE, CRO_USF='5'  "
                            + "WHERE cro_num = "+ numExp +" "
                            + "AND CRO_FFP IS NULL";
                    if (log.isDebugEnabled()) {
                        log.debug("sql = " + query);
                    }
                    st = con.createStatement();
                    int insert = st.executeUpdate(query);
                    if (insert > 0) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (Exception ex) {
                    log.debug("Se ha producido un error al cerrar los tramites del expediente");
                    throw new Exception(ex);
                } finally {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Procedemos a cerrar el statement y el resultset");
                        }
                        if (st != null) {
                            st.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e) {
                        log.error("Se ha producido un error cerrando el statement y el resulset", e);
                        throw new Exception(e);
                    }
                }
            }
            
            public boolean cambioEstadoExpediente(String numExp, Connection con) throws Exception {
                Statement st = null;
                ResultSet rs = null;
                String query = "";
                try {
                    query = "UPDATE E_EXP "
                            + "SET EXP_EST = 1 "
                            + "WHERE EXP_NUM = "+ numExp +"";
                    if (log.isDebugEnabled()) {
                        log.debug("sql = " + query);
                    }
                    st = con.createStatement();
                    int insert = st.executeUpdate(query);
                    if (insert > 0) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (Exception ex) {
                    log.debug("Se ha producido un error al cambiar el estado al Expediente");
                    throw new Exception(ex);
                } finally {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Procedemos a cerrar el statement y el resultset");
                        }
                        if (st != null) {
                            st.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e) {
                        log.error("Se ha producido un error cerrando el statement y el resulset", e);
                        throw new Exception(e);
                    }
                }
            }
            
            public boolean cambioEstadoExpedienteInsert(String numExp, Connection con) throws Exception {
                Statement st = null;
                ResultSet rs = null;
                String query = "";
                try {
                    query = "Insert into e_expsit "
                            + "values("+ numExp +",'anulado por script',5,'ADMIN',1,substr("+ numExp +",0,4))";
                    if (log.isDebugEnabled()) {
                        log.debug("sql = " + query);
                    }
                    st = con.createStatement();
                    int insert = st.executeUpdate(query);
                    if (insert > 0) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (Exception ex) {
                    log.debug("Se ha producido un error al cambiar el estado al Expediente");
                    throw new Exception(ex);
                } finally {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Procedemos a cerrar el statement y el resultset");
                        }
                        if (st != null) {
                            st.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e) {
                        log.error("Se ha producido un error cerrando el statement y el resulset", e);
                        throw new Exception(e);
                    }
                }
            }
            
            public boolean cambioObservExpediente(String numExp, Connection con) throws Exception {
                Statement st = null;
                ResultSet rs = null;
                String query = "";
                try {
                    query = "UPDATE E_EXP "
                            + "SET EXP_OBS=EXP_OBS || chr(13) || 'ANULADO POR DUPLICIDAD' "
                            + "WHERE EXP_NUM="+ numExp +"";
                    if (log.isDebugEnabled()) {
                        log.debug("sql = " + query);
                    }
                    st = con.createStatement();
                    int insert = st.executeUpdate(query);
                    if (insert > 0) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (Exception ex) {
                    log.debug("Se ha producido un error al cambair las observaciones al Expediente");
                    throw new Exception(ex);
                } finally {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Procedemos a cerrar el statement y el resultset");
                        }
                        if (st != null) {
                            st.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Exception e) {
                        log.error("Se ha producido un error cerrando el statement y el resulset", e);
                        throw new Exception(e);
                    }
                }
            }
/*     */ 
/*     */   public Vector<SiguienteTramiteTO> getFlujoSalida(Connection con, String codOrg, String codProc, String codTram, int numCod)
/*     */     throws SQLException
/*     */   {
/* 495 */     PreparedStatement ps = null;
/* 496 */     ResultSet rs = null;
/* 497 */     Vector lista = new Vector();
/*     */     try
/*     */     {
/* 500 */       String sql = "SELECT FLS_CTS, FLS_NUS, TRA_COU, TRA_UTR, TML_VALOR FROM (E_FLS INNER JOIN E_TRA ON (FLS_MUN=TRA_MUN AND FLS_PRO=TRA_PRO AND FLS_CTS=TRA_COD AND TRA_FBA IS NULL)) LEFT JOIN E_TML ON (FLS_MUN=TML_MUN AND FLS_PRO=TML_PRO AND FLS_CTS=TML_TRA AND TML_CMP='NOM') WHERE FLS_MUN=? AND FLS_PRO=? AND FLS_TRA=? AND FLS_NUC=" + numCod;
/*     */ 
/* 506 */       if (this.log.isDebugEnabled()) this.log.debug(sql);
/*     */ 
/* 508 */       ps = con.prepareStatement(sql);
/* 509 */       int i = 1;
/* 510 */       ps.setInt(i++, Integer.parseInt(codOrg));
/* 511 */       ps.setString(i++, codProc);
/* 512 */       ps.setInt(i++, Integer.parseInt(codTram));
/* 513 */       rs = ps.executeQuery();
/*     */ 
/* 515 */       while (rs.next()) {
/* 516 */         SiguienteTramiteTO tramiteTO = new SiguienteTramiteTO();
/* 517 */         tramiteTO.setCodigoTramiteFlujoSalida(rs.getString("FLS_CTS"));
/* 518 */         tramiteTO.setNumeroSecuencia(rs.getString("FLS_NUS"));
/* 519 */         tramiteTO.setCodigoVisibleTramiteFlujoSalida(rs.getString("TRA_COU"));
/* 520 */         tramiteTO.setDescripcionTramiteFlujoSalida(rs.getString("TML_VALOR"));
/* 521 */         tramiteTO.setModoSeleccionUnidad(rs.getInt("TRA_UTR"));
/* 522 */         lista.addElement(tramiteTO);
/*     */       }
/*     */     } catch (SQLException e) {
/* 525 */       this.log.error("ERROR AL RECUPERAR EL FLUJO DE SALIDA DEL TR√ÅMITE: " + codTram + " DEL PROCEDIMIENTO: " + codProc + ":" + e.getMessage());
/* 526 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 529 */         if (rs != null) rs.close();
/* 530 */         if (ps != null) ps.close(); 
/*     */       }
/*     */       catch (SQLException e) {
/* 532 */         this.log.error("ERROR AL CERRAR RECURSOS ASOCIADOS A LA CONEXI√ìN A LA BBDD: " + e.getMessage());
/*     */       }
/*     */     }
/*     */       this.log.debug("getFlujoSalida. Rertornamos la lista");
/* 536 */     return lista;
/*     */   }
/*     */ 
/*     */   public byte[] getContenidoFichero(String fichero, Connection con)
/*     */   {
/* 542 */     byte[] contenido = null;
/* 543 */     PreparedStatement ps = null;
/* 544 */     ResultSet rs = null;
/*     */     try
/*     */     {
/* 548 */       String sql = "SELECT CONTENIDO FROM MELANBIDE03_IMPRESION_CEPAP WHERE NOMBRE_FICHERO=?";
/* 549 */       this.log.debug(sql);
/*     */ 
/* 551 */       ps = con.prepareStatement(sql);
/* 552 */       ps.setString(1, fichero);
/* 553 */       rs = ps.executeQuery();
/* 554 */       ByteArrayOutputStream ot = new ByteArrayOutputStream();
/*     */ 
/* 556 */       while (rs.next()) {
/* 557 */         InputStream st = rs.getBinaryStream("CONTENIDO");
/*     */         try
/*     */         {
/* 560 */           if (st != null)
/*     */           {
/*     */             int c;
/* 562 */             while ((c = st.read()) != -1) {
/* 563 */               ot.write(c);
/*     */             }
/* 565 */             ot.flush();
/* 566 */             contenido = ot.toByteArray();
/*     */           }
/*     */         } catch (IOException e) {
/* 569 */           this.log.error("Error al leer el contenido del fichero: " + e.getMessage());
/* 570 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SQLException e) {
/* 575 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 578 */         if (ps != null) ps.close();
/* 579 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (SQLException e) {
/* 581 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 585 */     return contenido;
/*     */   }
/*     */ 
/*     */   public TramitacionExpedientesValueObject getInfoTramite(String codProc, String lListaExpedientesSeleccionados, String codTramiteImpresion, Connection conexion)
/*     */   {
/* 591 */     TramitacionExpedientesValueObject tVO = new TramitacionExpedientesValueObject();
/* 592 */     ResultSet rs = null;
/* 593 */     Statement st = null;
/*     */     try
/*     */     {
/* 597 */       String SQL_OCC_TRAMITE = "SELECT CRO_OCU,CRO_UTR FROM E_CRO WHERE CRO_PRO ='" + codProc + "' AND CRO_NUM = '" + lListaExpedientesSeleccionados + "' AND CRO_TRA = '" + codTramiteImpresion + "'" + " AND CRO_FEF IS NULL " + "ORDER BY CRO_FEF DESC";
/*     */ 
/* 603 */       this.log.debug(SQL_OCC_TRAMITE);
/*     */ 
/* 605 */       st = conexion.createStatement();
/* 606 */       rs = st.executeQuery(SQL_OCC_TRAMITE);
/*     */ 
/* 608 */       if (rs.next()) {
/* 609 */         tVO.setOcurrenciaTramite(rs.getString("CRO_OCU"));
/* 610 */         tVO.setCodUnidadTramitadoraTram(rs.getString("CRO_UTR"));
/*     */       }
/*     */     }
/*     */     catch (SQLException e) {
/* 614 */       this.log.error("Error al recuperar ocurrencia del tr√°mite: " + e.getMessage());
/* 615 */       e.printStackTrace();
/*     */     } finally {
/*     */       try {
/* 618 */         if (st != null) st.close();
/* 619 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 622 */         this.log.error("Error al cerrar recursos asociados a la conexi√≥n a la BBDD: " + e.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 626 */     return tVO;
/*     */   }
/*     */ 
/*     */   public byte[] getContenidoDocumento(String nombre, Connection conexion)
/*     */   {
/* 632 */     byte[] contenido = null;
/* 633 */     ResultSet rs = null;
/* 634 */     Statement st = null;
/*     */     try
/*     */     {
/* 638 */       String SQL_OCC_TRAMITE = "SELECT CONTENIDO FROM MELANBIDE03_IMPRESION_CEPAP WHERE NOMBRE_FICHERO='" + nombre + "'";
/*     */ 
/* 642 */       this.log.debug(SQL_OCC_TRAMITE);
/*     */ 
/* 644 */       st = conexion.createStatement();
/* 645 */       rs = st.executeQuery(SQL_OCC_TRAMITE);
/*     */ 
/* 647 */       while (rs.next()) {
/* 648 */         InputStream is = rs.getBinaryStream("CONTENIDO");
/* 649 */         ByteArrayOutputStream ot = new ByteArrayOutputStream();
/*     */         int c;
/* 651 */         while ((c = is.read()) != -1) {
/* 652 */           ot.write(c);
/*     */         }
/* 654 */         ot.flush();
/* 655 */         contenido = ot.toByteArray();
/* 656 */         ot.close();
/* 657 */         is.close();
/*     */       }
/*     */     }
/*     */     catch (SQLException e) {
/* 661 */       this.log.error("Error al recuperar ocurrencia del tr√°mite: " + e.getMessage());
/* 662 */       e.printStackTrace();
/*     */     } catch (IOException e) {
/* 664 */       this.log.error("Error al leer el contenido del archivo: " + e.getMessage());
/* 665 */       e.printStackTrace();
/*     */     }
/*     */     finally {
/*     */       try {
/* 669 */         if (st != null) st.close();
/* 670 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (Exception e) {
/* 672 */         this.log.error("Error al cerrar recursos asociados a la conexi√≥n a la BBDD: " + e.getMessage());
/*     */       }
/*     */     }
/* 675 */     return contenido;
/*     */   }

  public ArrayList<String> getExpediente(String nombre, Connection conexion)
  {
     ArrayList<String> expedientes = new ArrayList<String>();
     ResultSet rs = null;
     Statement st = null;
     try
     {
       String SQL_OCC_TRAMITE = "SELECT NUM_EXPEDIENTE FROM MELANBIDE03_EXP_IMPR_CEPAP WHERE NOMBRE_FICHERO='" + nombre + "'";
 
       this.log.debug(SQL_OCC_TRAMITE);
 
       st = conexion.createStatement();
       rs = st.executeQuery(SQL_OCC_TRAMITE);
 
       while (rs.next()) {
         String is = rs.getString("NUM_EXPEDIENTE");
         expedientes.add(is);
       }
     }
     catch (SQLException e) {
       this.log.error("Error en getExpediente: " + e.getMessage());
       e.printStackTrace();
     }
     finally {
       try {
        if (st != null) st.close();
         if (rs != null) rs.close(); 
       }
       catch (Exception e) {
         this.log.error("Error al cerrar recursos asociados a la conexion a la BBDD: " + e.getMessage());
       }
     }
     return expedientes;
   }
  public ArrayList<Participantes> leerDatosParticipantes (String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        ArrayList<Participantes> parti = new ArrayList<Participantes>();
        try
        {            
            Participantes exp = null;
            String query = null;
            
                query = "SELECT EXT_NUM, TER_DOC, TER_NOM, TER_AP1, TER_AP2,"
                      + "TER_TLF, TER_DCE, TER_NOC, TER_TID, DNN_DMC"
                      + ",  PAI_COD, PRV_COD, MUN_COD, VIA_COD "
                      + ", PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD, EXT_ROL  " +
                        "FROM E_EXT " +
                        "INNER JOIN T_TER ON EXT_TER = TER_COD "
                      + "LEFT JOIN T_DOT ON TER_DOM = DOT_DOM AND TER_COD = DOT_TER and TER_DOM =T_DOT.DOT_DOM " +
                        "LEFT JOIN T_DNN ON DNN_DOM = DOT_DOM " +
                        "LEFT JOIN FLBGEN.T_PAI ON PAI_COD = DNN_PAI " +
                        "LEFT JOIN FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV " +
                        "LEFT JOIN FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD AND MUN_COD = DNN_MUN " +
                        "LEFT JOIN T_VIA ON VIA_PAI = PAI_COD AND VIA_PRV = PRV_COD AND VIA_MUN = MUN_COD AND VIA_COD = DNN_VIA " +
                        "WHERE EXT_NUM = '"+numExp+"'";

                if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            while(rs.next()){
                exp = new Participantes();
                exp.setNumExp(rs.getString("EXT_NUM"));
                exp.setNif(rs.getString("TER_DOC"));
                exp.setNombre(rs.getString("TER_NOM"));
                exp.setApe1(rs.getString("TER_AP1"));
                exp.setApe2(rs.getString("TER_AP2"));
                exp.setTlf(rs.getString("TER_TLF"));
                exp.setMail(rs.getString("TER_DCE"));
                exp.setNomC(rs.getString("TER_NOC"));
                exp.setTipoID(rs.getInt("TER_TID"));
                exp.setIdPais(rs.getString("PAI_COD"));
                exp.setIdProv(rs.getString("PRV_NOM"));
                exp.setIdMuni(rs.getString("MUN_COD"));
                exp.setIdCalle(rs.getString("VIA_COD"));
                exp.setPais(rs.getString("PAI_NOM"));
                exp.setProv(rs.getString("PRV_NOM"));
                exp.setMuni(rs.getString("MUN_NOM"));
                exp.setCalle(rs.getString("VIA_NOM"));
                exp.setNum(rs.getString("DNN_NUD"));
                exp.setLetra(rs.getString("DNN_LED"));
                //exp.setRol(rs.getString("EXT_ROL"));
                parti.add(exp);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();            
            log.error("Error en leerDatosParticipantes " + ex.getMessage());
            throw new Exception(ex);
        }
        finally
        {
             try
            {
                if(log.isDebugEnabled()) 
                    log.error("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return parti;
    }
/*     */ 
/*     */   private ArrayList<String> getListaInteresadosExpediente(int codOrganizacion, String numExpediente, int ejercicio, Connection con)
/*     */   {
/* 681 */     ResultSet rs = null;
/* 682 */     PreparedStatement ps = null;
/* 683 */     ArrayList salida = new ArrayList();
/*     */     try
/*     */     {
/* 687 */       String sql = "SELECT HTE_NOM,HTE_AP1,HTE_AP2 FROM E_EXT,T_HTE WHERE E_EXT.EXT_NUM=? AND E_EXT.EXT_EJE=? AND E_EXT.EXT_MUN=? AND E_EXT.EXT_TER=HTE_TER AND E_EXT.EXT_NVR=HTE_NVR";
/*     */ 
/* 691 */       //this.log.debug(sql);
/* 692 */       ps = con.prepareStatement(sql);
/* 693 */       int i = 1;
/* 694 */       ps.setString(i++, numExpediente);
/* 695 */       ps.setInt(i++, ejercicio);
/* 696 */       ps.setInt(i++, codOrganizacion);
/*     */ 
/* 698 */       rs = ps.executeQuery();
/*     */ 
/* 700 */       while (rs.next()) {
/* 701 */         String apellido2 = rs.getString("HTE_AP2");
/* 702 */         String apellido1 = rs.getString("HTE_AP1");
/* 703 */         String nombre = rs.getString("HTE_NOM");
/* 704 */         String nombreCompleto = null;
/*     */ 
/* 706 */         if ((apellido2 != null) && (!"".equals(apellido2)))
/* 707 */           nombreCompleto = apellido1 + " " + apellido2 + "," + nombre;
/*     */         else {
/* 709 */           nombreCompleto = apellido1 + "," + nombre;
/*     */         }
/* 711 */         salida.add(nombreCompleto);
/*     */       }
/*     */     }
/*     */     catch (SQLException e) {
/* 715 */       this.log.error("Error al recuperar la lista de interesados del expediente: " + e.getMessage());
/* 716 */       e.printStackTrace();
/*     */     }
/*     */     finally {
/*     */       try {
/* 720 */         if (ps != null) ps.close();
/* 721 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (Exception e) {
/* 723 */         this.log.error("Error al cerrar recursos asociados a la conexi√≥n a la BBDD: " + e.getMessage());
/*     */       }
/*     */     }
/* 726 */     return salida;
/*     */   }
/*     */ 
/*     */   public ArrayList<ImpresionExpedientesLanbideValueObject> getListaExpedientesDocumento(int codOrganizacion, String nombreFichero, Connection conexion)
/*     */   {
/* 732 */     ArrayList salida = new ArrayList();
/* 733 */     ResultSet rs = null;
/* 734 */     Statement st = null;
/*     */     try
/*     */     {
/* 738 */       String sql = "SELECT NUM_EXPEDIENTE FROM MELANBIDE03_EXP_IMPR_CEPAP WHERE NOMBRE_FICHERO='" + nombreFichero + "' " + "ORDER BY NUM_EXPEDIENTE DESC";
/*     */ 
/* 743 */       this.log.debug(sql);
/* 744 */       st = conexion.createStatement();
/* 745 */       rs = st.executeQuery(sql);
/*     */ 
/* 747 */       AnotacionRegistroDAO anotacionDAO = AnotacionRegistroDAO.getInstance();
/* 748 */       while (rs.next()) {
/* 749 */         String numExpediente = rs.getString("NUM_EXPEDIENTE");
/* 750 */         GeneralValueObject anotacion = anotacionDAO.getAnotacionMasAntigua(numExpediente, conexion);
/*     */ 
/* 752 */         String fechaEntrada = (String)anotacion.getAtributo("FECHA_ENTRADA_REGISTRO_ANOTACION");
/* 753 */         String numRegistro = (String)anotacion.getAtributo("NUMERO_REGISTRO_ANOTACION");
/*     */ 
/* 759 */         ImpresionExpedientesLanbideValueObject vo = new ImpresionExpedientesLanbideValueObject();
/* 760 */         vo.setNumExpediente(numExpediente);
/* 761 */         vo.setFechaPresentacionRegistroInicio(fechaEntrada);
/* 762 */         vo.setNumRegistroInicio(numRegistro);
/*     */ 
/* 766 */         salida.add(vo);
/*     */       }
/*     */ 
/* 769 */       for (int i = 0; i < salida.size(); i++) {
/* 770 */         ImpresionExpedientesLanbideValueObject exp = (ImpresionExpedientesLanbideValueObject)salida.get(i);
/* 771 */         String[] datos = exp.getNumExpediente().split("/");
/*     */ 
/* 773 */         ArrayList interesados = getListaInteresadosExpediente(codOrganizacion, exp.getNumExpediente(), Integer.parseInt(datos[0]), conexion);
/* 774 */         String interesado = "";
/* 775 */         for (int j = 0; j < interesados.size(); j++) {
/* 776 */           interesado = interesado + (String)interesados.get(j);
/* 777 */           if (interesados.size() - j <= 1) continue; interesado = interesado + "\\r\\n\\r\\n";
/*     */         }
/* 779 */         exp.setInteresados(interesado);
/*     */       }
/*     */     }
/*     */     catch (SQLException e) {
/* 783 */       this.log.error("Error al recuperar ocurrencia del tr√°mite: " + e.getMessage());
/* 784 */       e.printStackTrace();
/*     */     } catch (AnotacionRegistroException e) {
/* 786 */       e.printStackTrace();
/*     */     } catch (TechnicalException e) {
/* 788 */       e.printStackTrace();
/*     */     }
/*     */     finally {
/*     */       try {
/* 792 */         if (st != null) st.close();
/* 793 */         if (rs != null) rs.close(); 
/*     */       }
/*     */       catch (Exception e) {
/* 795 */         this.log.error("Error al cerrar recursos asociados a la conexi√≥n a la BBDD: " + e.getMessage());
/*     */       }
/*     */     }
/* 798 */     return salida;
/*     */   }
/*     */ 


    /*public void recuperaFichero(int codOrganizacion, String nombreFichero, Connection conexion)
   {
     ArrayList salida = new ArrayList();
     ResultSet rs = null;
     Statement st = null;
     try
     {
       String sql = "SELECT NOMBRE_FICHERO, CONTENIDO FROM MELANBIDE03_IMPRESION_CEPAP WHERE NOMBRE_FICHERO='" + nombreFichero + "' ";
 
       this.log.debug(sql);
       st = conexion.createStatement();
       rs = st.executeQuery(sql);
 
       //AnotacionRegistroDAO anotacionDAO = AnotacionRegistroDAO.getInstance();
       if (rs.next()) {
         String numExpediente = rs.getString("NUM_EXPEDIENTE");
         GeneralValueObject anotacion = anotacionDAO.getAnotacionMasAntigua(numExpediente, conexion);
 
         String fechaEntrada = (String)anotacion.getAtributo("FECHA_ENTRADA_REGISTRO_ANOTACION");
         String numRegistro = (String)anotacion.getAtributo("NUMERO_REGISTRO_ANOTACION");
 
         ImpresionExpedientesLanbideValueObject vo = new ImpresionExpedientesLanbideValueObject();
         vo.setNumExpediente(numExpediente);
         vo.setFechaPresentacionRegistroInicio(fechaEntrada);
         vo.setNumRegistroInicio(numRegistro);
 
         salida.add(vo);
       }
 
       for (int i = 0; i < salida.size(); i++) {
         ImpresionExpedientesLanbideValueObject exp = (ImpresionExpedientesLanbideValueObject)salida.get(i);
         String[] datos = exp.getNumExpediente().split("/");
 
         ArrayList interesados = getListaInteresadosExpediente(codOrganizacion, exp.getNumExpediente(), Integer.parseInt(datos[0]), conexion);
         String interesado = "";
         for (int j = 0; j < interesados.size(); j++) {
           interesado = interesado + (String)interesados.get(j);
           if (interesados.size() - j <= 1) continue; interesado = interesado + "\\r\\n\\r\\n";
         }
         exp.setInteresados(interesado);
       }
     }
     catch (SQLException e) {
       this.log.error("Error al recuperar ocurrencia del tr√°mite: " + e.getMessage());
       e.printStackTrace();
     } catch (AnotacionRegistroException e) {
       e.printStackTrace();
     } catch (TechnicalException e) {
       e.printStackTrace();
     }
     finally {
       try {
         if (st != null) st.close();
         if (rs != null) rs.close(); 
       }
       catch (Exception e) {
         this.log.error("Error al cerrar recursos asociados a la conexi√≥n a la BBDD: " + e.getMessage());
       }
     }
  }*/

 public List<String> getExpMismoCertificadoPorExp(String numExp, String certif, Connection con) throws Exception
        {
            Statement st = null;
            ResultSet rs = null;
            List<String> retList = new ArrayList<String>();

            try
            {

                String sql = "Select Ext_Num, Ter_Doc, Ter_Nom, Ter_Ap1, Ter_Ap2,\n" +
                            "TER_TLF, TER_DCE, TER_NOC, TER_TID,EXT_ROL, COD_CERTIFICADO  \n" +
                            "FROM E_EXT \n" +
                            "Inner Join T_Ter On Ext_Ter = Ter_Cod\n" +
                            "Inner Join E_Tdet On Tdet_Num = Ext_Num And Tdet_Pro = Ext_Pro  And Tdet_Cod = 'VALORACION2' \n" +
                            "Inner Join E_Tde On Tde_Num = Ext_Num And Tde_Cod= 'TIPOACREDITACION'\n" +
                            "left JOIN MELANBIDE03_CERTIFICADO ON NUM_EXPEDIENTE = EXT_NUM AND COD_PROCEDIMIENTO = EXT_PRO\n" +
                            "Where (Tdet_Valor = 'T') And Ext_Rol = 1\n" +
                            "And Tde_Valor = 'CP' And Cod_Procedimiento = 'CEPAP'\n" +
                            "And Ter_Doc In (\n" +
                            "  Select Ter_Doc\n" +
                            "  From T_Ter\n" +
                            "  Inner Join E_Ext On Ext_Ter = Ter_Cod \n" +
                            "  Where Ext_Num = "+numExp+"\n" +
                            ") and Ext_Num <> "+numExp+" and COD_CERTIFICADO = '"+certif+"'";

               log.debug("compruebaExpedientes: " + sql);


               st = con.createStatement();
               rs = st.executeQuery(sql);

               while(rs.next()){                  
                   retList.add(rs.getString("Ext_Num"));
               }           
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }// finally
            return retList;
            }
 }

/* Location:           C:\flexia\LCE_13.01\src\java\es\altia\flexia\integracion\lanbide\impresion\
 * Qualified Name:     es.altia.flexia.integracion.lanbide.impresion.ExpedienteImpresionDAO
 * JD-Core Version:    0.6.0
 */


