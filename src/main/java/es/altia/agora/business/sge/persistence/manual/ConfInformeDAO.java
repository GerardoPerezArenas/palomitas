package es.altia.agora.business.sge.persistence.manual;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.sge.ConfInformeValueObject;
import es.altia.agora.business.util.GlobalNames;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;

import java.sql.*;

import java.util.ArrayList;
import java.util.Vector;
import org.apache.log4j.Logger;

public class ConfInformeDAO {

  private String lStrClaveArea     = "ARE";
  private String lStrClaveUnidad   = "UTR";
  private String lStrClaveTipoProc = "TPR"; 
  private String lStrClaveProc     = "PRO";
  
  private String lStrClaveTipoTram = "CLS";
  private String lStrClaveTramite  = "TRA";  
  private Logger log = Logger.getLogger(ConfInformeDAO.class);

	//Para el fichero de configuracion tecnico.
    protected static Config m_ConfigTechnical;
	//Para los mensajes de error localizados.
    protected static Config m_ConfigError;
	//Para informacion de logs.
    protected static Log m_Log =
            LogFactory.getLog(ConfInformeDAO.class.getName());

    private static ConfInformeDAO instance = null;

	protected ConfInformeDAO() {
		super();
        //Queremos usar el fichero de configuracion techserver
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        //Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");

	}

	public static ConfInformeDAO getInstance() {
		//si no hay ninguna instancia de esta clase tenemos que crear una.
		if (instance == null) {
			// Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
			synchronized(ConfInformeDAO.class){
				if (instance == null)
					instance = new ConfInformeDAO();
			}
		}
		return instance;
	}	

	public Vector cargarCombos(ConfInformeValueObject ciVO, String[] params) throws TechnicalException {
		Vector opciones = new Vector();

         String lStrCombo = ciVO.getCombo();

         String lStrArea = ciVO.getArea();
         String lStrUnidad       = ciVO.getUnidad();
         String lStrTipoProc     = ciVO.getTipoProcedimiento();
         String lStrProc         = ciVO.getProcedimiento();
         String lStrTipoTram     = ciVO.getClasTramite();
         String lStrTramite      = ciVO.getTramite();

         String lStrValArea      = ciVO.getAreaD();
         String lStrValUnidad    = ciVO.getUnidadD();
         String lStrValTipoProc  = ciVO.getTipoProcedimientoD();
         String lStrValProc      = ciVO.getProcedimientoD();
         String lStrValTipoTram  = ciVO.getClasTramiteD();
         String lStrValTramite   = ciVO.getTramiteD();
         
         if (("".equals(lStrValArea))     || ("-1".equals(lStrValArea)))     lStrValArea     = "0";
         if (("".equals(lStrValUnidad))   || ("-1".equals(lStrValUnidad)))   lStrValUnidad   = "0";
         if (("".equals(lStrValTipoProc)) || ("-1".equals(lStrValTipoProc))) lStrValTipoProc = "0";
         if (("".equals(lStrValProc))     || ("-1".equals(lStrValProc)))     lStrValProc     = "0";
         if (("".equals(lStrValTipoTram)) || ("-1".equals(lStrValTipoTram))) lStrValTipoTram = "0";
         if (("".equals(lStrValTramite))  || ("-1".equals(lStrValTramite)))  lStrValTramite  = "0";

		AdaptadorSQLBD oad = null;
		Connection con = null;
         Statement st = null;
		ResultSet rs = null;
         ConfInformeValueObject listaVO = null;

		try{
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();	
              st = con.createStatement();

			// Creamos la select con los parametros adecuados.

              String lStrSQL = "";
              String lStrWHERE = "";

              lStrSQL  = "SELECT DISTINCT ";
              if (lStrCombo.equals(lStrClaveArea)){
                lStrSQL += "V.A1,  V.A2 ";
              }
              else {
                if (lStrCombo.equals(lStrClaveUnidad)){
                  lStrSQL += "V.U1,  V.U2 ";
                                  
                }
                else {
                  if (lStrCombo.equals(lStrClaveTipoProc)){
                    lStrSQL += "V.TP1, V.TP2 ";
                  }
                  else {
                    if (lStrCombo.equals(lStrClaveProc)){
                      lStrSQL += "V.P1,  V.P2 ";
                    }
                    else {
                      if (lStrCombo.equals(lStrClaveTipoTram)){
                        lStrSQL += "V.C1,  V.C2 ";
                      }
                      else {
                        lStrSQL += "V.T1,  V.T2 ";
                      }
                    }
                  }
                }
              }
              String lStrTrozo = "";

              lStrSQL += "FROM t_v01 V";
              lStrWHERE = "";

              if ((lStrClaveArea.equals(lStrArea)) && !("0".equals(lStrValArea))){
                lStrWHERE += "A1 = " + lStrValArea;
              }

              if ((lStrClaveUnidad.equals(lStrUnidad)) && !("0".equals(lStrValUnidad))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND U1 = " + lStrValUnidad;
                } else {
                	lStrWHERE += " U1 = " + lStrValUnidad;
                }
              }

              if ((lStrClaveTipoProc.equals(lStrTipoProc)) && !("0".equals(lStrValTipoProc))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND TP1 = " + lStrValTipoProc;
                } else {
                	lStrWHERE += " TP1 = " + lStrValTipoProc;
                }
              }

              if ((lStrClaveProc.equals(lStrProc)) && !("0".equals(lStrValProc))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND P1 = '" + lStrValProc + "'";
                } else {
                	lStrWHERE += " P1 = '" + lStrValProc + "'";
                }
              }

              if ((lStrClaveTipoTram.equals(lStrTipoTram)) && !("0".equals(lStrValTipoTram))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND C1 = " + lStrValTipoTram;
                } else {
                	lStrWHERE += " C1 = " + lStrValTipoTram;
                }
              }

              if ((lStrClaveTramite.equals(lStrTramite)) && !("0".equals(lStrValTramite))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND T1 = " + lStrValTramite;
                } else {
                	lStrWHERE += " T1 = " + lStrValTramite;
                }
              }

              /*m_Log.debug(lStrClaveArea     + " - " + lStrArea   +  " - " + lStrValArea);
              m_Log.debug(lStrClaveUnidad   + " - " + lStrUnidad   +  " - " + lStrValUnidad);
              m_Log.debug(lStrClaveTipoProc + " - " + lStrTipoProc +  " - " + lStrValTipoProc);
              m_Log.debug(lStrClaveProc     + " - " + lStrProc     +  " - " + lStrValProc);
              m_Log.debug(lStrClaveTipoTram + " - " + lStrTipoTram +  " - " + lStrValTipoTram);
              m_Log.debug(lStrClaveTramite  + " - " + lStrTramite  +  " - " + lStrValTramite);*/


              if (!"".equals(lStrWHERE)) lStrSQL += " WHERE " + lStrWHERE;
              
              lStrSQL += " ORDER BY 2";
              
              if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + lStrSQL);

              // La vista está bien para recuperar el resto de elementos filtrados desde informes de gestión.
              // Lo único que no es correcto es el filtrado por uors, por tanto, simplemente se modifica esto.

              if(!lStrCombo.equals(lStrClaveUnidad)){
                  log.debug("NO SE BUSCA POR UNIDADEDDDDDDDDD");
                  rs = st.executeQuery(lStrSQL);
                  String codigo = null;
                  while(rs.next()){
                    listaVO = new ConfInformeValueObject();
                    codigo = rs.getString(1);
                    if (codigo != null) {
                        listaVO.setTipoProcedimiento(codigo);
                       
                        if (lStrCombo.equals(lStrClaveProc)){
                            listaVO.setTipoProcedimientoD(rs.getString(2) + "(" + codigo + ")");
                        }
                        else{
                            listaVO.setTipoProcedimientoD(rs.getString(2));
                        }
                                                
                        opciones.addElement(listaVO);
                    }
                  }

                  rs.close();
                  st.close();              
              }
              else{
                  log.debug("SE BUSCA POR UNIDADEDDDDDDDDD DKDKDKKDKDKDKDKDKD");

                  log.debug(" BUSCAR UORS PROCEDIMIENTO: " + lStrProc);
                  log.debug(" BUSCAR UORS TRÁMITE: " + lStrTramite);
                  // Si se trata de cargar el combo de unidad, se comprueban si se recuperan los uors asociados
                  // a todos los expedientes o bien, las uors asociadas a los trámites
                  ArrayList<UORDTO> uors = new ArrayList<UORDTO>();
                  //Se comenta esta parte del codigo por #10661. Ahora siempre se recuperan las unidades de tramite
//                  if(!"".equals(lStrProc) && !"".equals(lStrTramite)){
//                      // Se filtra por procedimiento y trámite
//                      uors = getUorsTramite(params,lStrValProc,lStrValTramite,lStrValTipoTram,lStrValTipoProc,lStrValArea);
//                      for(int i=0;i<uors.size();i++){
//                          UORDTO uor = uors.get(i);
//                          listaVO = new ConfInformeValueObject();
//                          listaVO.setTipoProcedimiento(uor.getUor_cod());
//                          listaVO.setTipoProcedimientoD(uor.getUor_nom());
//                          opciones.addElement(listaVO);
//                      }// for
//                  }// if
//
//                   if(lStrProc!=null && "".equals(lStrTramite)){
//                      // Se filtra por procedimiento y trámite
//                      uors = getUorsExpediente(params,lStrValProc,lStrValTramite,lStrValTipoTram,lStrValTipoProc,lStrValArea);
//                      for(int i=0;i<uors.size();i++){
//                          UORDTO uor = uors.get(i);
//                          listaVO = new ConfInformeValueObject();
//                          listaVO.setTipoProcedimiento(uor.getUor_cod());
//                          listaVO.setTipoProcedimientoD(uor.getUor_nom());
//                          opciones.addElement(listaVO);
//                      }// for
//                  }// if
                  
                  //Aqui recupero las unidades de trámite
                      uors = getUorsTramite(params,lStrValProc,lStrValTramite,lStrValTipoTram,lStrValTipoProc,lStrValArea);
                      for(int i=0;i<uors.size();i++){
                          UORDTO uor = uors.get(i);
                          listaVO = new ConfInformeValueObject();
                          listaVO.setTipoProcedimiento(uor.getUor_cod());
                          listaVO.setTipoProcedimientoD(uor.getUor_nom());
                          opciones.addElement(listaVO);
                      }// for
              }// else 

		}catch (Exception e){
			m_Log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			//Aquí se pueden lanzar TechnicalException que no se capturan.
			try{
				oad.devolverConexion(con);
			}catch(BDException bde) {
				bde.getMensaje();
			}
			return opciones;
		}
	}


        /** Recupera las distintas unidades organizativas con la que se han iniciado los expedientes de
     * un determinado procedimiento
     * @param params: Parámetros de conexión a la base de datos
     * @param codPro: Código del procedimiento
     * @return ArrayList<UORDTO> con los códigos de las uors
     * @throws es.altia.common.exception.TechnicalException
     */
    public ArrayList<UORDTO> getUorsExpediente(String[] params,String codPro,String codTramite,String tipoTramite,String tipoProc,String area) throws TechnicalException{

        ArrayList<UORDTO> uors= new ArrayList<UORDTO>();
        Statement st = null;
        ResultSet rs =null;
        try{

            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            Connection con = oad.getConnection();


            String sql = "SELECT DISTINCT(EXP_UOR),UOR_NOM " +
                         "FROM " + GlobalNames.ESQUEMA_GENERICO + "A_AML," + GlobalNames.ESQUEMA_GENERICO + "A_TPR," +
                         GlobalNames.ESQUEMA_GENERICO + "A_CML,E_CRO,E_PRO,E_TRA,E_EXP,A_UOR " +
                         "WHERE AML_COD = PRO_ARE AND " +
                         "CML_COD = TRA_CLS AND " +
                         "TPR_COD = PRO_TIP AND " +
                         "TRA_PRO = PRO_COD AND " +
                         "CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD AND CRO_PRO = PRO_COD AND " +
                         "EXP_NUM = CRO_NUM AND EXP_PRO = CRO_PRO AND " +
                         "EXP_UOR = UOR_COD ";

             if(!"0".equals(codPro))
                sql += " AND CRO_PRO='" + codPro + "'";

            if(!"".equals(codTramite) && !"0".equals(codTramite))
                sql += " AND CRO_TRA=" + codTramite;

            if(!"".equals(tipoProc) && !"0".equals(tipoProc))
                sql += " AND PRO_TIP=" + tipoProc;

            if(!"".equals(area) && !"0".equals(area))
                sql += " AND PRO_ARE=" + area;

            if(!"".equals(tipoTramite) && !"0".equals(tipoTramite))
                sql += " AND TRA_CLS=" + tipoTramite;

            log.debug("getUorsExpediente sql: " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                UORDTO uor = new UORDTO();
                uor.setUor_cod(rs.getString("EXP_UOR"));
                uor.setUor_nom(rs.getString("UOR_NOM"));
                uors.add(uor);
            }

            SigpGeneralOperations.devolverConexion(oad, con);

        }
        catch(BDException e) {
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(),e);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(),e);
        }
        finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }
        return uors;
    }


    /** Recupera las distintas unidades organizativas con la que se han iniciado ocurrencias de trámites en
     * los expedientes de un determinado procedimiento
     * @param params: Parámetros de conexión a la base de datos
     * @param codPro: Código del procedimiento
     * @param codTramite: Código del trámite
     * @param tipoTramite: Tipo del trámite
     * @param tipoProc: Tipo del procedimiento
     * @param area: Área del procedimiento
     * @return ArrayList<UORDTO> con los códigos de las uors
     * @throws es.altia.common.exception.TechnicalException
     */
    public ArrayList<UORDTO> getUorsTramite(String[] params,String codPro,String codTramite,String tipoTramite,String tipoProc,String area) throws TechnicalException{

        ArrayList<UORDTO> uors= new ArrayList<UORDTO>();
        Statement st = null;
        ResultSet rs =null;
        try{

            AdaptadorSQLBD oad = new AdaptadorSQLBD(params);
            Connection con = oad.getConnection();
/*
            SELECT DISTINCT(CRO_UTR),UOR_NOM
FROM sge_gen_caixa.dbo.A_AML,sge_gen_caixa.dbo.A_TPML,
sge_gen_caixa.dbo.A_CML,E_PML,E_TML,E_PRO,E_CRO,A_UOR
                         WHERE AML_COD = PRO_ARE AND
                         PML_MUN = PRO_MUN AND
                         PML_LENG = AML_LENG AND
                         TML_LENG = PML_LENG AND
                         CML_LENG = PML_LENG AND
                         TPML_LENG= PML_LENG AND
						 PRO_COD = CRO_PRO AND
                         CRO_PRO='GD' AND CRO_TRA=4 AND CRO_UTR=UOR_COD;
            */

            String sql = "SELECT DISTINCT(CRO_UTR),UOR_NOM " +
                         "FROM " +  GlobalNames.ESQUEMA_GENERICO + "A_AML," + GlobalNames.ESQUEMA_GENERICO + "A_TPR," +
                         GlobalNames.ESQUEMA_GENERICO + "A_CML," +
                         "E_PRO,E_CRO,E_TRA,A_UOR " +
                         "WHERE AML_COD = PRO_ARE AND " + // Área procedimiento                         
                         "CML_COD = TRA_CLS AND " + // Clasificación trámite
                         "TPR_COD = PRO_TIP AND " + // Tipo procedimiento                         
                         "TRA_PRO = PRO_COD AND " + // join entre e_tra y e_pro
                         "CRO_PRO = TRA_PRO AND CRO_TRA = TRA_COD AND CRO_PRO = PRO_COD AND " +  // join entre e_cro y e_tra
                         "CRO_UTR = UOR_COD";       // join entre la tabla a_uor y la tabla e_cro
                        

                         //"PML_MUN = PRO_MUN AND " +
                         //"PML_LENG = AML_LENG AND " +
                         //"TML_LENG = PML_LENG AND " +
                         //"CML_LENG = PML_LENG AND " +
                         //"TPML_LENG= PML_LENG AND " +
                         //"PRO_COD  = CRO_PRO ";

            if(!"0".equals(codPro))
                sql += " AND CRO_PRO='" + codPro + "'";

            if(!"".equals(codTramite) && !"0".equals(codTramite))
                sql += " AND CRO_TRA=" + codTramite;

            if(!"".equals(tipoProc) && !"0".equals(tipoProc))
                sql += " AND PRO_TIP=" + tipoProc;

            if(!"".equals(area) && !"0".equals(area))
                sql += " AND PRO_ARE=" + area;

            if(!"".equals(tipoTramite) && !"0".equals(tipoTramite))
                sql += " AND TRA_CLS=" + tipoTramite;

            log.debug("getUorsTramite sql: " + sql);

            // Order by
            sql += " ORDER BY 2";
            /*
            String sql = "SELECT DISTINCT(CRO_UTR),UOR_NOM FROM E_CRO,A_UOR " +
                         "WHERE CRO_PRO=? AND CRO_TRA=? AND CRO_UTR=UOR_COD";
            */
            st = con.createStatement();
            
            rs = st.executeQuery(sql);
            while(rs.next()){
                UORDTO uor = new UORDTO();
                uor.setUor_cod(rs.getString("CRO_UTR"));
                uor.setUor_nom(rs.getString("UOR_NOM"));
                uors.add(uor);
            }

            SigpGeneralOperations.devolverConexion(oad, con);

        }
        catch(BDException e) {
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(),e);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new TechnicalException(e.getMessage(),e);
        }
        finally{
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);
        }
        return uors;
    }



    /**
     * Método encargado de recuperar el contenido de un determinado combo. Tiene en cuenta los procedimientos restringidos y los permisos que un
     * usuario pueda tener sobre ellos
     * @param ciVO ConfInformeValueObject que contiene la información necesaria para rellenar el combo
     * @param codUsuario: Código del usuario
     * @param codOrganizacion: Código de la organización
     * @param params: Parámetros de conexión a la BBDD
     * @return Vector
     * @throws es.altia.common.exception.TechnicalException
     */
    	public Vector cargarCombos(ConfInformeValueObject ciVO, String codUsuario, String codOrganizacion, String[] params) throws TechnicalException {
		Vector opciones = new Vector();

         String lStrCombo = ciVO.getCombo();

         String lStrArea = ciVO.getArea();
         String lStrUnidad       = ciVO.getUnidad();
         String lStrTipoProc     = ciVO.getTipoProcedimiento();
         String lStrProc         = ciVO.getProcedimiento();
         String lStrTipoTram     = ciVO.getClasTramite();
         String lStrTramite      = ciVO.getTramite();

         String lStrValArea      = ciVO.getAreaD();
         String lStrValUnidad    = ciVO.getUnidadD();
         String lStrValTipoProc  = ciVO.getTipoProcedimientoD();
         String lStrValProc      = ciVO.getProcedimientoD();
         String lStrValTipoTram  = ciVO.getClasTramiteD();
         String lStrValTramite   = ciVO.getTramiteD();

         if (("".equals(lStrValArea))     || ("-1".equals(lStrValArea)))     lStrValArea     = "0";
         if (("".equals(lStrValUnidad))   || ("-1".equals(lStrValUnidad)))   lStrValUnidad   = "0";
         if (("".equals(lStrValTipoProc)) || ("-1".equals(lStrValTipoProc))) lStrValTipoProc = "0";
         if (("".equals(lStrValProc))     || ("-1".equals(lStrValProc)))     lStrValProc     = "0";
         if (("".equals(lStrValTipoTram)) || ("-1".equals(lStrValTipoTram))) lStrValTipoTram = "0";
         if (("".equals(lStrValTramite))  || ("-1".equals(lStrValTramite)))  lStrValTramite  = "0";

		AdaptadorSQLBD oad = null;
		Connection con = null;
         Statement st = null;
		ResultSet rs = null;
         ConfInformeValueObject listaVO = null;

		try{
			oad = new AdaptadorSQLBD(params);
			con = oad.getConnection();
              st = con.createStatement();

			// Creamos la select con los parametros adecuados.

              String lStrSQL = "";
              String lStrWHERE = "";

              lStrSQL  = "SELECT DISTINCT ";
              if (lStrCombo.equals(lStrClaveArea)){
                lStrSQL += "V.A1,  V.A2 ";
              }
              else {
                if (lStrCombo.equals(lStrClaveUnidad)){
                  lStrSQL += "V.U1,  V.U2 ";

                }
                else {
                  if (lStrCombo.equals(lStrClaveTipoProc)){
                    lStrSQL += "V.TP1, V.TP2 ";
                  }
                  else {
                    if (lStrCombo.equals(lStrClaveProc)){
                      lStrSQL += "V.P1,  V.P2 ";
                    }
                    else {
                      if (lStrCombo.equals(lStrClaveTipoTram)){
                        lStrSQL += "V.C1,  V.C2 ";
                      }
                      else {
                        // Se recupera el código del trámite, su descripción y el código del procedimiento
                        lStrSQL += "V.T1,  V.T2 , V.P1 ";
                      }
                    }
                  }
                }
              }
              String lStrTrozo = "";

              lStrSQL += "FROM t_v01 V";
              lStrWHERE = "";

              if ((lStrClaveArea.equals(lStrArea)) && !("0".equals(lStrValArea))){
                lStrWHERE += "A1 = " + lStrValArea;
              }

              if ((lStrClaveUnidad.equals(lStrUnidad)) && !("0".equals(lStrValUnidad))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND U1 = " + lStrValUnidad;
                } else {
                	lStrWHERE += " U1 = " + lStrValUnidad;
                }
              }

              if ((lStrClaveTipoProc.equals(lStrTipoProc)) && !("0".equals(lStrValTipoProc))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND TP1 = " + lStrValTipoProc;
                } else {
                	lStrWHERE += " TP1 = " + lStrValTipoProc;
                }
              }

              if ((lStrClaveProc.equals(lStrProc)) && !("0".equals(lStrValProc))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND P1 = '" + lStrValProc + "'";
                } else {
                	lStrWHERE += " P1 = '" + lStrValProc + "'";
                }
              }

              if ((lStrClaveTipoTram.equals(lStrTipoTram)) && !("0".equals(lStrValTipoTram))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND C1 = " + lStrValTipoTram;
                } else {
                	lStrWHERE += " C1 = " + lStrValTipoTram;
                }
              }

              if ((lStrClaveTramite.equals(lStrTramite)) && !("0".equals(lStrValTramite))){
                lStrTrozo = lStrWHERE.trim();
                if (!"".equals(lStrTrozo)) {
                	lStrWHERE += " AND T1 = " + lStrValTramite;
                } else {
                	lStrWHERE += " T1 = " + lStrValTramite;
                }
              }

              /*m_Log.debug(lStrClaveArea     + " - " + lStrArea   +  " - " + lStrValArea);
              m_Log.debug(lStrClaveUnidad   + " - " + lStrUnidad   +  " - " + lStrValUnidad);
              m_Log.debug(lStrClaveTipoProc + " - " + lStrTipoProc +  " - " + lStrValTipoProc);
              m_Log.debug(lStrClaveProc     + " - " + lStrProc     +  " - " + lStrValProc);
              m_Log.debug(lStrClaveTipoTram + " - " + lStrTipoTram +  " - " + lStrValTipoTram);
              m_Log.debug(lStrClaveTramite  + " - " + lStrTramite  +  " - " + lStrValTramite);*/


              if (!"".equals(lStrWHERE)) lStrSQL += " WHERE " + lStrWHERE;

              lStrSQL += " ORDER BY 2";

              if(m_Log.isDebugEnabled()) m_Log.debug("sql: " + lStrSQL);

              // La vista está bien para recuperar el resto de elementos filtrados desde informes de gestión.
              // Lo único que no es correcto es el filtrado por uors, por tanto, simplemente se modifica esto.

              if(!lStrCombo.equals(lStrClaveUnidad)){
                  log.debug("NO SE BUSCA POR UNIDADEDDDDDDDDD");
                  rs = st.executeQuery(lStrSQL);
                  String codigo = null;
                  while(rs.next()){
                    listaVO = new ConfInformeValueObject();
                    codigo = rs.getString(1);
                    if (codigo != null) {
                        /*
                        listaVO.setTipoProcedimiento(codigo);
                        listaVO.setTipoProcedimientoD(rs.getString(2));
                        opciones.addElement(listaVO);
                        */
                       listaVO.setTipoProcedimiento(codigo);
                       if (lStrCombo.equals(lStrClaveProc)){
                            listaVO.setTipoProcedimientoD(rs.getString(2) + "(" + codigo + ")");
                        }
                        else{
                            listaVO.setTipoProcedimientoD(rs.getString(2));
                        }

                       String codProcComprobar = null;
                       if(lStrCombo.equals("PRO"))
                            codProcComprobar = codigo;
                       else
                       if(lStrCombo.equals("TRA"))
                           codProcComprobar = rs.getString(3);

                       if(codProcComprobar!=null){
                            log.debug("======> Comprobando si el procedimiento " + codProcComprobar  + " está restringodoa");
                            boolean estaRestringido = DefinicionProcedimientosDAO.getInstance().estaProcedimientoRestringido(codProcComprobar, con);
                            log.debug(" ======> El Procedimiento " + codigo + " está restringido " + estaRestringido);
                            if(estaRestringido){
                               boolean tienePermisoUsuario = PermisoProcRestringidoDAO.getInstance().tieneUsuarioPermisoSobreProcedimientoRestringido(codUsuario, codOrganizacion, codProcComprobar, con);
                               log.debug("======> El usuario " + codUsuario + " sobre el procedimiento restringido " + codigo + " tiene permiso  " + tienePermisoUsuario);

                               if(tienePermisoUsuario){
                                   opciones.addElement(listaVO);
                               }
                            }//if
                            else
                                opciones.addElement(listaVO);
                       }// if
                       else
                           opciones.addElement(listaVO);
                       
                    }//if
                  }// while

                  rs.close();
                  st.close();
              }
              else{
                  log.debug("SE BUSCA POR UNIDADEDDDDDDDDD DKDKDKKDKDKDKDKDKD");

                  log.debug(" BUSCAR UORS PROCEDIMIENTO: " + lStrProc);
                  log.debug(" BUSCAR UORS TRÁMITE: " + lStrTramite);
                  // Si se trata de cargar el combo de unidad, se comprueban si se recuperan los uors asociados
                  // a todos los expedientes o bien, las uors asociadas a los trámites
                  ArrayList<UORDTO> uors = new ArrayList<UORDTO>();
                  //Se comenta esta parte del codigo por #10661. Ahora siempre se recuperan las unidades de tramite
//                  if(!"".equals(lStrProc) && !"".equals(lStrTramite)){
//                      // Se filtra por procedimiento y trámite
//                      uors = getUorsTramite(params,lStrValProc,lStrValTramite,lStrValTipoTram,lStrValTipoProc,lStrValArea);
//                      for(int i=0;i<uors.size();i++){
//                          UORDTO uor = uors.get(i);
//                          listaVO = new ConfInformeValueObject();
//                          listaVO.setTipoProcedimiento(uor.getUor_cod());
//                          listaVO.setTipoProcedimientoD(uor.getUor_nom());
//                          opciones.addElement(listaVO);
//                      }// for
//                  }// if
//
//                   if(lStrProc!=null && "".equals(lStrTramite)){
//                      // Se filtra por procedimiento y trámite
//                      uors = getUorsExpediente(params,lStrValProc,lStrValTramite,lStrValTipoTram,lStrValTipoProc,lStrValArea);
//                      for(int i=0;i<uors.size();i++){
//                          UORDTO uor = uors.get(i);
//                          listaVO = new ConfInformeValueObject();
//                          listaVO.setTipoProcedimiento(uor.getUor_cod());
//                          listaVO.setTipoProcedimientoD(uor.getUor_nom());
//                          opciones.addElement(listaVO);
//                      }// for
//                  }// if

                  //Aqui recupero las unidades de trámite
                      uors = getUorsTramite(params,lStrValProc,lStrValTramite,lStrValTipoTram,lStrValTipoProc,lStrValArea);
                      for(int i=0;i<uors.size();i++){
                          UORDTO uor = uors.get(i);
                          listaVO = new ConfInformeValueObject();
                          listaVO.setTipoProcedimiento(uor.getUor_cod());
                          listaVO.setTipoProcedimientoD(uor.getUor_nom());
                          opciones.addElement(listaVO);
                      }// for
              }// else

		}catch (Exception e){
			m_Log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			//Aquí se pueden lanzar TechnicalException que no se capturan.
			try{
				oad.devolverConexion(con);
			}catch(BDException bde) {
				bde.getMensaje();
			}
			return opciones;
		}
	}

    
}