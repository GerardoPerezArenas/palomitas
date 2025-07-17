CREATE VIEW [E_PORTAFIRMAS]
(crd_mun, crd_pro, crd_eje, crd_num, crd_tra, crd_ocu, crd_nud, usu_cod, fir_est, fir, crd_fmo, fx_firma, observ, pml_valor, tml_valor, crd_des)
AS
   SELECT f.crd_mun, f.crd_pro, f.crd_eje, f.crd_num, f.crd_tra, f.crd_ocu, f.crd_nud, f.usu_cod, f.fir_est, f.fir, d.crd_fmo, f.fx_firma,
          f.observ, p.pml_valor, t.tml_valor, d.crd_des
   FROM e_crd d, e_crd_fir f, e_pml p, e_tml t
   WHERE (d.crd_mun = f.crd_mun)
   		AND (d.crd_pro = f.crd_pro)
      	AND (d.crd_eje = f.crd_eje)
      	AND (d.crd_num = f.crd_num)
      	AND (d.crd_tra = f.crd_tra)
      	AND (d.crd_ocu = f.crd_ocu)
      	AND (d.crd_nud = f.crd_nud)
      	AND (p.pml_mun = d.crd_mun)
      	AND (p.pml_cod = d.crd_pro)
      	AND (p.pml_leng = @idiomaDefecto)
      	AND (t.tml_mun = d.crd_mun)
      	AND (t.tml_pro = d.crd_pro)
      	AND (t.tml_tra = d.crd_tra)
      	AND (t.tml_leng = @idiomaDefecto);
GO
      	
      	

CREATE VIEW [dbo].[expedientes]
(cro_mun,
                                    cro_pro,
                                    cro_eje,
                                    cro_num,
                                    cro_fei,
                                    cro_fef,
                                    exp_fei,
                                    exp_fef,
                                    pro_tip,
                                    tra_cod,
                                    tra_cou,
                                    pro_are,
                                    uot_uor,
                                    tra_cls,
                                    cro_ocu,
                                    cro_utr
                                   )
AS
   SELECT exp_mun, exp_pro, exp_eje, exp_num, cro_fei, cro_fef, exp_fei,
          exp_fef, pro_tip, 999 tra_cod, 999 tra_cou, pro_are, NULL,
          999 tra_cls, 1 cro_ocu, cro_utr
     FROM e_pro, e_exp, e_cro
    WHERE pro_mun = exp_mun AND pro_cod = exp_pro and cro_num = exp_num
GO
   

CREATE VIEW [G_PORTAFIRMAS] (CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD, USU_COD, FIR_EST, FIR, CRD_FMO, FX_FIRMA, OBSERV, PML_VALOR, TML_VALOR, CRD_DES) 
AS 
	SELECT f.crd_mun, f.crd_pro, f.crd_eje, f.crd_num, f.crd_tra, f.crd_ocu, f.crd_nud, f.usu_cod, f.fir_est, f.fir, d.crd_fmo, f.fx_firma, f.observ, p.pml_valor, t.tml_valor, d.crd_des
	FROM g_crd d,
  		g_crd_fir f,
  		e_pml p,
  		e_tml t
	WHERE(d.crd_mun = f.crd_mun)
		AND(d.crd_pro = f.crd_pro)
		AND(d.crd_eje = f.crd_eje)
		AND(d.crd_num = f.crd_num)
		AND(d.crd_tra = f.crd_tra)
		AND(d.crd_ocu = f.crd_ocu)
		AND(d.crd_nud = f.crd_nud)
		AND(p.pml_mun = d.crd_mun)
		AND(p.pml_cod = d.crd_pro)
		AND(p.pml_leng = @idiomaDefecto)
		AND(t.tml_mun = d.crd_mun)
		AND(t.tml_pro = d.crd_pro)
		AND(t.tml_tra = d.crd_tra)
		AND(t.tml_leng = @idiomaDefecto);
GO


CREATE VIEW T_INT (INT_TER, INT_NVR,INT_NOC, INT_NOM, INT_AP1, INT_AP2, 
INT_DOC, INT_TID,INT_DOM, INT_DNN,INT_MUN, INT_TLFO, INT_PRV, INT_CPO)
AS
select HTE_TER, HTE_NVR,
    CAST(hte_nom AS VARCHAR) 
    + ' '
    +CAST(t_hte.hte_pa1 AS VARCHAR) 
    + ''
    + CAST(t_hte.hte_ap1 AS VARCHAR) 
    + (
    CASE
      WHEN t_hte.hte_pa1 IS NOT NULL
      OR t_hte.hte_ap1   IS NOT NULL
      THEN ' '
      ELSE ''
    END)
    + CAST(t_hte.hte_pa2 AS VARCHAR) 
    + ''
    + CAST(t_hte.hte_ap2 AS VARCHAR)  AS titular,
    hte_nom,
    hte_ap1,
    hte_ap2,
    t_hte.hte_doc,
     t_hte.hte_TID,
    NULL AS domicilio,
    (CASE 
				WHEN t_tvi.tvi_des IS NOT NULL AND t_tvi.tvi_des <> ' ' 
					THEN t_tvi.tvi_des + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_via.via_nom IS NOT NULL AND t_via.via_nom <> ' ' 
					THEN t_via.via_nom + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_dmc IS NOT NULL AND t_dnn.dnn_dmc <> ' ' 
					THEN t_dnn.dnn_dmc + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_nud IS NOT NULL 
					THEN CONVERT(varchar, t_dnn.dnn_nud)+ ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_led IS NOT NULL AND t_dnn.dnn_led <> ' ' 
					THEN t_dnn.dnn_led + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_nuh IS NOT NULL 
					THEN CONVERT(varchar, t_dnn.dnn_nuh) + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_leh IS NOT NULL AND t_dnn.dnn_leh <> ' ' 
					THEN t_dnn.dnn_leh + ' ' 
					ELSE '' END) 
			+ (CASE 
					WHEN t_dnn.dnn_blq IS NOT NULL AND t_dnn.dnn_blq <> ' ' 
						THEN ' Bl. ' + t_dnn.dnn_blq + ' ' 
						ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_por IS NOT NULL AND t_dnn.dnn_por <> ' ' 
					THEN ' Portal. ' + t_dnn.dnn_por + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_esc IS NOT NULL AND t_dnn.dnn_esc <> ' ' 
					THEN ' Esc. ' + t_dnn.dnn_esc + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_plt IS NOT NULL AND t_dnn.dnn_plt <> ' ' 
					THEN t_dnn.dnn_plt + 'บ ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_pta IS NOT NULL AND t_dnn.dnn_pta <> ' ' 
					THEN t_dnn.dnn_pta + ' ' 
					ELSE '' END ) AS domicilionn,
    t_mun.mun_nom,
    t_hte.hte_tlf ,
    t_prv.prv_nom,
    t_dnn.dnn_cpo
	from t_hte left join t_ter on (ter_cod=hte_ter) right join t_dot on (ter_dom=dot_dom and ter_cod=dot_ter)
	INNER JOIN t_dom ON 
			(t_dot.dot_dom = t_dom.dom_cod)
		INNER JOIN t_dnn ON 
			(dot_dom = dnn_dom)
		LEFT OUTER JOIN @sge_generico.dbo.t_mun t_mun ON 
			(t_dnn.dnn_pai = t_mun.mun_pai 
			AND t_dnn.dnn_prv = t_mun.mun_prv 
			AND t_dnn.dnn_mun = t_mun.mun_cod)
		LEFT OUTER JOIN t_via ON (t_dnn.dnn_pai = t_via.via_pai 
			AND t_dnn.dnn_prv = t_via.via_prv 
			AND t_dnn.dnn_mun = t_via.via_mun 
			AND t_dnn.dnn_via = t_via.via_cod)
		LEFT OUTER JOIN t_tvi ON 
			(t_via.via_tvi = t_tvi.tvi_cod)
		LEFT OUTER JOIN @sge_generico.dbo.t_prv t_prv ON 
			(t_dnn.dnn_pai = t_prv.prv_pai 
			AND t_dnn.dnn_prv = t_prv.prv_cod)
		LEFT OUTER JOIN t_esi ON 
			(t_dnn.dnn_pai = t_esi.esi_pai 
			AND t_dnn.dnn_mun = t_esi.esi_mun 
			AND t_dnn.dnn_prv = t_esi.esi_prv 
			AND t_dnn.dnn_esi = t_esi.esi_cod)
		LEFT OUTER JOIN t_eco ON 
			(t_esi.esi_pai = t_eco.eco_pai 
			AND t_esi.esi_mun = t_eco.eco_mun 
			AND t_esi.esi_prv = t_eco.eco_prv 
			AND t_esi.esi_eco = t_eco.eco_cod)		
GO
    
    
CREATE VIEW [T_LOC] (loc_cot, loc_tip, loc_cov, loc_via)
AS
	SELECT tvi_cod loc_cot, tvi_des loc_tip, via_cod loc_cov, via_nom loc_via
    FROM t_tvi, t_via
    WHERE via_tvi = tvi_cod;
GO
  
  
CREATE VIEW [T_V01] (m1, l1, a1, a2, t1, t2, c1, c2, p1, p2, tp1, tp2, u1, u2, ID, mu) AS
SELECT
  p1.pro_mun m1, p2.pml_leng l1, a2.aml_cod a1, a2.aml_valor a2, t2.tml_tra t1, t2.tml_valor t2, c2.cml_cod c1, c2.cml_valor c2, 
  p2.pml_cod p1, p2.pml_valor p2, tp2.tpml_cod tp1, tp2.tpml_valor tp2, u2.uor_cod u1, u2.uor_nom u2, a2.aml_leng ID, p2.pml_mun mu          
FROM
  @sge_generico.dbo.a_aml a2, e_tml t2, @sge_generico.dbo.a_cls c1, @sge_generico.dbo.a_cml c2, e_pro p1, e_pml p2, @sge_generico.dbo.a_tpml tp2,      
  e_tra t1 LEFT JOIN a_uor u2 ON (t1.tra_utr = u2.uor_cod) 
WHERE
  p1.pro_mun = t1.tra_mun AND      
  p2.pml_mun = p1.pro_mun AND
  p2.pml_leng = a2.aml_leng AND    
  t2.tml_leng = p2.pml_leng AND    
  c2.cml_leng = p2.pml_leng AND    
  tp2.tpml_leng = p2.pml_leng AND  
  p1.pro_are = a2.aml_cod AND      
  t1.tra_mun = t2.tml_mun AND        
  t1.tra_pro = t2.tml_pro AND        
  t1.tra_cod = t2.tml_tra AND        
  t1.tra_cls = c1.cls_cod AND        
  c1.cls_cod = c2.cml_cod AND        
  p1.pro_cod = t1.tra_pro AND        
  p1.pro_mun = p2.pml_mun AND        
  p1.pro_cod = p2.pml_cod AND        
  p1.pro_tip = tp2.tpml_cod;
GO
  

CREATE VIEW [TAREAS]
AS
	SELECT cro_mun, cro_pro, cro_eje, cro_num, cro_fei, cro_fef, exp_fei,
	   (CASE WHEN exp_fef IS NOT NULL AND exp_num=cro_num THEN exp_fef ELSE NULL END) AS exp_fef, 
	   pro_tip, tra_cod, tra_cou, pro_are, tra_cls, cro_ocu, cro_utr
	FROM e_cro, e_pro, e_exp, e_tra
	WHERE cro_mun = exp_mun
		AND cro_pro = exp_pro
      	AND cro_eje = exp_eje
      	AND cro_num = exp_num
      	AND pro_mun = exp_mun
      	AND pro_cod = exp_pro
      	AND e_tra.tra_mun = e_cro.cro_mun
      	AND e_tra.tra_pro = e_cro.cro_pro
      	AND e_tra.tra_cod = e_cro.cro_tra
	UNION 
	SELECT exp_mun AS cro_mun, exp_pro AS cro_pro, exp_eje AS cro_eje, exp_num AS cro_num, exp_pend AS cro_fei, CAST(NULL AS datetime) cro_fef,
	  exp_fei, exp_fef, pro_tip, 999 tra_cod, 999 tra_cou, pro_are, 999 tra_cls, 1 cro_ocu, 999 cro_utr
	FROM e_exp, e_pro, @sge_generico.dbo.a_tpr
	WHERE exp_mun = pro_mun
    	AND exp_pro = pro_cod
      	AND pro_tip = tpr_cod
      	AND exp_pend IS NOT NULL;
GO
      

CREATE VIEW [V_CRO] ( CRO_OCU, PRO_COD,PRO_NOM,CRO_EJE,CRO_NUM,TRA_COD,TRA_NOM, CRO_FEI, CRO_FEF, USU_COD, USU_NOM, UTR_COD, 
							UTR_NOM, CRO_FIP, CRO_FLI, CRO_FFP, CRO_OBS, CRO_LENG, CRO_LOC, CRO_AYT, CRO_DAC, CRO_MAC, CRO_AAC, EXP_EST, 
							EXP_ASU, DNN_RCA, PRV_NOM, ESI_NOM, ECO_NOM )
AS
	SELECT E_CRO.CRO_OCU, E_CRO.CRO_PRO, E_PML.PML_VALOR, E_CRO.CRO_EJE, E_CRO.CRO_NUM, E_CRO.CRO_TRA, E_TML.TML_VALOR, 
			CONVERT(char, E_CRO.CRO_FEI,103) AS CRO_FEI, CONVERT(char, E_CRO.CRO_FEF,103) AS CRO_FEF,
			E_CRO.CRO_USU, A_USU.USU_LOG, E_CRO.CRO_UTR, A_UOR.UOR_NOM,
			CONVERT(char, E_CRO.CRO_FIP, 103) AS CRO_FIP, CONVERT(char, E_CRO.CRO_FLI, 103) AS CRO_FLI, 
			CONVERT(char, E_CRO.CRO_FFP, 103) AS CRO_FFP, E_EXP.EXP_OBS, E_PML.PML_LENG, E_EXP.EXP_LOC, T_MUN.MUN_NOM,
			SUBSTRING(CONVERT(char, GETDATE(), 103), 1, 2) AS CRO_DAC, SUBSTRING(CONVERT(char, GETDATE(), 103), 1, 2) AS CRO_MAC,
			SUBSTRING(CONVERT(char, GETDATE(), 103), 1, 4) AS CRO_AAC, E_EXP.EXP_EST, E_EXP.EXP_ASU, T_DNN.DNN_RCA, T_PRV.PRV_NOM,
			T_ESI.ESI_NOM, T_ECO.ECO_NOM
	FROM E_CRO 
		INNER JOIN E_TML ON (E_CRO.CRO_MUN=E_TML.TML_MUN AND E_CRO.CRO_PRO=E_TML.TML_PRO AND E_CRO.CRO_TRA=E_TML.TML_TRA)
		INNER JOIN E_PML ON (  E_CRO.CRO_MUN=E_PML.PML_MUN AND E_CRO.CRO_PRO=E_PML.PML_COD)
		INNER JOIN @sge_generico.dbo.A_USU A_USU ON (E_CRO.CRO_USU=A_USU.USU_COD)
		INNER JOIN A_UOR ON (E_CRO.CRO_UTR=A_UOR.UOR_COD)
		LEFT OUTER JOIN E_EXP ON (E_CRO.CRO_MUN=E_EXP.EXP_MUN AND E_CRO.CRO_PRO=E_EXP.EXP_PRO AND E_CRO.CRO_NUM=E_EXP.EXP_NUM)
		INNER JOIN @sge_generico.dbo.T_MUN T_MUN ON (E_CRO.CRO_MUN=T_MUN.MUN_COD)
		LEFT OUTER JOIN T_DNN ON (E_EXP.EXP_CLO=T_DNN.DNN_DOM)
		LEFT OUTER JOIN @sge_generico.dbo.T_PRV T_PRV ON (T_DNN.DNN_PAI=T_PRV.PRV_PAI AND T_DNN.DNN_PRV=T_PRV.PRV_COD)
		LEFT OUTER JOIN T_ESI ON (T_DNN.DNN_PAI = T_ESI.ESI_PAI AND T_DNN.DNN_MUN = T_ESI.ESI_MUN AND T_DNN.DNN_PRV = T_ESI.ESI_PRV AND T_DNN.DNN_ESI = T_ESI.ESI_COD)
		LEFT OUTER JOIN T_ECO ON (T_ESI.ESI_PAI = T_ECO.ECO_PAI AND T_ESI.ESI_MUN = T_ECO.ECO_MUN AND T_ESI.ESI_PRV = T_ECO.ECO_PRV AND T_ESI.ESI_ECO = T_ECO.ECO_COD)
	WHERE E_PML.PML_CMP='NOM'
		AND E_TML.TML_CMP='NOM'  
		AND E_PML.PML_LENG=E_TML.TML_LENG
		AND T_MUN.MUN_PAI=108
		AND T_MUN.MUN_PRV=5;
GO
		

CREATE VIEW [V_CROI] (cro_ocu, pro_cod, pro_nom, cro_eje, cro_num, tra_cod, tra_nom, cro_fei, cro_fef, usu_cod, usu_nom, utr_cod, utr_nom,
							cro_fip, cro_fli, cro_ffp, cro_obs, cro_leng, cro_int, cro_doc, cro_dom, cro_pbd, cro_rol, cro_loc, cro_ayt,
							cro_nor, cro_domnn, cro_pbdnn, cro_cin, cro_nvi, cro_dac, cro_mac, cro_aac, exp_asu, dnn_rca, prv_nom, esi_nom,
							eco_nom, dnn_cpo)
AS      
	SELECT   e_cro.cro_ocu, e_cro.cro_pro, e_pml.pml_valor, e_cro.cro_eje, e_cro.cro_num, e_cro.cro_tra, e_tml.tml_valor,
            CONVERT (varchar, e_cro.cro_fei, 131) AS cro_fei, CONVERT (varchar, e_cro.cro_fef, 131) AS cro_fef, e_cro.cro_usu,
            a_usu.usu_log, e_cro.cro_utr, a_uor.uor_nom, CONVERT (varchar, e_cro.cro_fip, 131) AS cro_fip,
            CONVERT (varchar, e_cro.cro_fli, 131) AS cro_fli, CONVERT (varchar, e_cro.cro_ffp, 131) AS cro_ffp, e_cro.cro_obs,
            e_pml.pml_leng,  t_hte.hte_noc AS titular, t_hte.hte_doc,
			(CASE										
                   WHEN t_via.via_nom IS NOT NULL
                      THEN t_tvi.tvi_des
                   ELSE ''
                END
            )														
            + ' '
            + t_via.via_nom
            + ' '
            + CONVERT(varchar, t_dsu.dsu_nud)
            + ' '
            + t_dsu.dsu_led
            + ' '
            + CONVERT(varchar, t_dsu.dsu_nuh)
            + t_dsu.dsu_leh
            + (CASE
                   WHEN t_dsu.dsu_blq IS NOT NULL AND t_dsu.dsu_blq <> ' '
                      THEN ' Bl. '
                   ELSE ''
                END
               )
            + t_dsu.dsu_blq
            + (CASE
                   WHEN t_dsu.dsu_por IS NOT NULL AND t_dsu.dsu_por <> ' '
                      THEN ' Portal. '
                   ELSE ''
                END
               )
            + t_dsu.dsu_por
            + (CASE
                   WHEN t_dpo.dpo_esc IS NOT NULL AND t_dpo.dpo_esc <> ' '
                      THEN ' Esc. '
                   ELSE ''
                END
               )
            + t_dpo.dpo_esc
            + t_dpo.dpo_plt
            + (CASE
                   WHEN t_dpo.dpo_pta IS NOT NULL AND t_dpo.dpo_pta <> ' '
                      THEN ' บ '
                   ELSE ''
                END
               )
            + t_dpo.dpo_pta AS domicilio,
            mun1.mun_nom AS poblacion, e_rol.rol_des, e_exp.exp_loc,
            mun2.mun_nom AS ayuntamiento, t_dom.dom_nml,
            t_tvi2.tvi_des
	    + ' '
            + t_via2.via_nom 		
            + ' '
            + (CASE
                   WHEN t_dnn.dnn_dmc IS NOT NULL AND t_dnn.dnn_dmc <> ' '
                      THEN t_dnn.dnn_dmc+ ' '
                   ELSE ''
                END
               )
            + (CASE
                   WHEN t_dnn.dnn_nud IS NOT NULL
                      THEN CONVERT(varchar, t_dnn.dnn_nud)+ ' '
                   ELSE ''
                END
               )
            + (CASE
                   WHEN t_dnn.dnn_led IS NOT NULL AND t_dnn.dnn_led <> ' '
                      THEN t_dnn.dnn_led + ' '
                   ELSE ''
                END
               )
            + (CASE
                   WHEN t_dnn.dnn_nuh IS NOT NULL
                      THEN CONVERT(varchar, t_dnn.dnn_nuh)+ ' '
                   ELSE ''
                END
               )
            + (CASE
                   WHEN t_dnn.dnn_leh IS NOT NULL AND t_dnn.dnn_leh <> ' '
                      THEN t_dnn.dnn_leh+ ' '
                   ELSE ''
                END
               )
            + (CASE
                   WHEN t_dnn.dnn_blq IS NOT NULL AND t_dnn.dnn_blq <> ' '
                      THEN ' Bl. ' + t_dnn.dnn_blq
                   ELSE ''
                END
               )
           + (CASE
                   WHEN t_dnn.dnn_por IS NOT NULL AND t_dnn.dnn_por <> ' '
                      THEN ' Portal. ' + t_dnn.dnn_por
                   ELSE ''
                END
               )
            + (CASE
                   WHEN t_dnn.dnn_esc IS NOT NULL AND t_dnn.dnn_esc <> ' '
                      THEN ' Esc. ' + t_dnn.dnn_esc
                   ELSE ''
                END
               )
            + (CASE
                   WHEN t_dnn.dnn_plt IS NOT NULL AND t_dnn.dnn_plt <> ' '
                      THEN t_dnn.dnn_plt+ ' '
                   ELSE ''
                END
               )
            + (CASE
                   WHEN t_dnn.dnn_pta IS NOT NULL AND t_dnn.dnn_pta <> ' '
                      THEN ' บ ' + t_dnn.dnn_pta
                   ELSE ''
                END
               )
            AS domicilionn,
            mun3.mun_nom AS poblacionnn, e_ext.ext_ter, e_ext.ext_nvr,
            SUBSTRING(CONVERT(char, GETDATE(), 131), 1, 2) AS cro_dac,
            SUBSTRING(CONVERT(char, GETDATE(), 110), 1, 2) AS cro_mac,
            SUBSTRING(CONVERT(char, GETDATE(), 120), 1, 4) AS cro_aac, e_exp.exp_asu,
            t_dnn2.dnn_rca, t_prv.prv_nom, t_esi.esi_nom, t_eco.eco_nom, t_dnn.dnn_cpo
	FROM e_cro 	INNER JOIN e_pml ON (e_cro.cro_mun = e_pml.pml_mun AND e_cro.cro_pro = e_pml.pml_cod)
		INNER JOIN e_tml ON (e_cro.cro_mun = e_tml.tml_mun AND e_cro.cro_pro = e_tml.tml_pro AND e_cro.cro_tra = e_tml.tml_tra)
		INNER JOIN @sge_generico.dbo.a_usu a_usu ON (e_cro.cro_usu = a_usu.usu_cod)
		INNER JOIN a_uor ON (e_cro.cro_utr = a_uor.uor_cod)
		INNER JOIN @sge_generico.dbo.t_mun mun2 ON (e_cro.cro_mun = mun2.mun_cod)
		LEFT OUTER JOIN e_ext ON (e_cro.cro_mun = e_ext.ext_mun AND e_cro.cro_eje = e_ext.ext_eje AND e_cro.cro_num = e_ext.ext_num)
		LEFT OUTER JOIN t_hte ON (e_ext.ext_ter = t_hte.hte_ter AND e_ext.ext_nvr = t_hte.hte_nvr)
		LEFT OUTER JOIN t_dom ON (e_ext.ext_dot = t_dom.dom_cod)
		LEFT OUTER JOIN t_dpo ON (e_ext.ext_dot = t_dpo.dpo_dom)
		LEFT OUTER JOIN t_dsu ON (t_dpo.dpo_dsu = t_dsu.dsu_cod)
		LEFT OUTER JOIN @sge_generico.dbo.t_mun mun1 ON (T_DSU.DSU_PAI=MUN1.MUN_PAI AND T_DSU.DSU_PRV=MUN1.MUN_PRV AND T_DSU.DSU_MUN=MUN1.MUN_COD)
		LEFT OUTER JOIN t_via ON (t_dsu.dsu_pai = t_via.via_pai AND t_dsu.dsu_prv = t_via.via_prv AND t_dsu.dsu_mun = t_via.via_mun AND t_dsu.dsu_via = t_via.via_cod)
		LEFT OUTER JOIN t_tvi ON (t_via.via_tvi = t_tvi.tvi_cod)
		LEFT OUTER JOIN t_dnn ON (e_ext.ext_dot = t_dnn.dnn_dom)
		LEFT OUTER JOIN @sge_generico.dbo.t_mun mun3 ON (t_dnn.dnn_pai = mun3.mun_pai AND t_dnn.dnn_prv = mun3.mun_prv AND t_dnn.dnn_mun = mun3.mun_cod)
		LEFT OUTER JOIN t_via t_via2 ON (t_dnn.dnn_pai = t_via2.via_pai AND t_dnn.dnn_prv = t_via2.via_prv AND t_dnn.dnn_mun = t_via2.via_mun AND t_dnn.dnn_via = t_via2.via_cod)
		LEFT OUTER JOIN t_tvi t_tvi2 ON (t_dnn.dnn_tvi = t_tvi2.tvi_cod)
		LEFT OUTER JOIN e_exp ON (e_cro.cro_mun = e_exp.exp_mun AND e_cro.cro_pro = e_exp.exp_pro AND e_cro.cro_num = e_exp.exp_num)
		LEFT OUTER JOIN e_rol ON (e_ext.ext_pro = e_rol.rol_pro AND e_ext.ext_rol = e_rol.rol_cod)
		LEFT OUTER JOIN @sge_generico.dbo.t_prv t_prv ON (t_dnn.dnn_pai = t_prv.prv_pai AND t_dnn.dnn_prv = t_prv.prv_cod)
		LEFT OUTER JOIN t_esi ON (t_dnn.dnn_pai = t_esi.esi_pai AND t_dnn.dnn_mun = t_esi.esi_mun AND t_dnn.dnn_prv = t_esi.esi_prv AND t_dnn.dnn_esi = t_esi.esi_cod)
		LEFT OUTER JOIN t_eco ON ( t_esi.esi_pai = t_eco.eco_pai AND t_esi.esi_mun = t_eco.eco_mun AND t_esi.esi_prv = t_eco.eco_prv AND t_esi.esi_eco = t_eco.eco_cod)
		LEFT OUTER JOIN t_dnn t_dnn2 ON (e_exp.exp_clo = t_dnn2.dnn_dom)
	WHERE   e_pml.pml_cmp = 'NOM'
		AND e_tml.tml_cmp = 'NOM'
		AND e_pml.pml_leng = e_tml.tml_leng
		AND mun2.mun_pai = 108
		AND mun2.mun_prv = 19;
GO
		

CREATE VIEW [V_ENT1] (ap1, ap2, nom, dat)
AS
	SELECT res_asu, res_tip, res_dil, res_fec
	FROM r_res;
GO
     
     		
CREATE VIEW [V_EXP] (exp_num, exp_eje, exp_fei, exp_fef, exp_est, exp_usu, exp_nusu, exp_uor, exp_nuor, exp_obs, exp_asu, exp_pro, exp_npro) 
AS
	SELECT e_exp.exp_num, e_exp.exp_eje, CONVERT (varchar, e_exp.exp_fei, 131) AS exp_fei, CONVERT (varchar, e_exp.exp_fef, 131) AS exp_fef,
			(CASE WHEN e_exp.exp_est = 0 THEN 'PENDIENTE' ELSE 'CERRADO' END), e_exp.exp_usu, @sge_generico.dbo.a_usu.usu_nom, e_exp.exp_uor, 
			a_uor.uor_nom, e_exp.exp_obs, e_exp.exp_asu, e_exp.exp_pro, e_pml.pml_valor
	FROM e_exp, @sge_generico.dbo.a_usu, a_uor, e_pro, e_pml
	WHERE e_exp.exp_usu = @sge_generico.dbo.a_usu.usu_cod 
		AND e_exp.exp_uor = a_uor.uor_cod 
		AND e_exp.exp_mun = e_pro.pro_mun 
		AND e_exp.exp_pro = e_pro.pro_cod 
		AND e_pro.pro_cod = e_pml.pml_cod 
		AND e_pml.pml_leng = @idiomaDefecto
		AND e_pml.pml_cmp = 'NOM';
GO
	  
	  
CREATE  VIEW [dbo].[V_EXP_INF] (EXP_LOC, EXP_FEI, EXP_FEF, EXP_ASU, EXP_NUM, EXP_EJE, EXP_USU_COD, EXP_USU_NOM, EXP_UOR_COD,
								EXP_UOR_NOM, EXP_OBS, EXP_PRO_COD, EXP_PRO_NOM, TRA_FEI, TRA_FEF, TRA_NOM, TRA_COD,TRA_OBS,
								TRA_UOR_COD, TRA_UOR_NOM, TRA_COU)
AS
SELECT     E_EXP.EXP_LOC, E_EXP.EXP_FEI, E_EXP.EXP_FEF, E_EXP.EXP_ASU, E_EXP.EXP_NUM, E_EXP.EXP_EJE,
                      @sge_generico.dbo.A_USU.USU_LOG AS EXP_USU_LOG, @sge_generico.dbo.A_USU.USU_NOM AS EXP_USU_NOM, A1.UOR_COD_VIS AS EXP_UOR_COD,
                      A1.UOR_NOM AS EXP_UOR_NOM, E_EXP.EXP_OBS, E_EXP.EXP_PRO AS EXP_PRO_COD, E_PML.PML_VALOR AS EXP_PRO_NOM,
                      E_CRO.CRO_FEI AS TRA_FEI, E_CRO.CRO_FEF AS TRA_FEF, E_TML.TML_VALOR AS TRA_NOM, E_TRA.TRA_COD,
                      E_CRO.CRO_OBS AS TRA_OBS, A2.UOR_COD_VIS AS TRA_UOR_COD, A2.UOR_NOM AS TRA_UOR_NOM, E_TRA.TRA_COU
FROM         E_CRO INNER JOIN
                      E_TML ON E_CRO.CRO_MUN = E_TML.TML_MUN AND E_CRO.CRO_PRO = E_TML.TML_PRO AND
                      E_CRO.CRO_TRA = E_TML.TML_TRA AND E_TML.TML_CMP = 'NOM' LEFT OUTER JOIN
                      E_EXP ON E_CRO.CRO_MUN = E_EXP.EXP_MUN AND E_CRO.CRO_PRO = E_EXP.EXP_PRO AND
                      E_CRO.CRO_NUM = E_EXP.EXP_NUM INNER JOIN
                      A_UOR AS A1 ON E_EXP.EXP_UOR = A1.UOR_COD INNER JOIN
                      A_UOR AS A2 ON E_CRO.CRO_UTR = A2.UOR_COD INNER JOIN
                      E_PML ON E_PML.PML_LENG = E_TML.TML_LENG AND E_PML.PML_CMP = 'NOM' AND
                      E_EXP.EXP_PRO = E_PML.PML_COD AND E_EXP.EXP_MUN = E_PML.PML_MUN INNER JOIN
                      @sge_generico.dbo.A_USU ON E_EXP.EXP_USU = @sge_generico.dbo.A_USU.USU_COD INNER JOIN
                      @sge_generico.dbo.T_MUN ON E_CRO.CRO_MUN = @sge_generico.dbo.T_MUN.MUN_COD AND @sge_generico.dbo.T_MUN.MUN_PAI = 108 AND
                      @sge_generico.dbo.T_MUN.MUN_PRV = 19 INNER JOIN
                      E_TRA ON E_CRO.CRO_TRA = E_TRA.TRA_COD AND E_CRO.CRO_PRO = E_TRA.TRA_PRO
GO


CREATE  VIEW [V_EXP_INT_INF] (EXP_LOC, EXP_FEI, EXP_FEF, EXP_ASU, EXP_NUM, EXP_EJE, EXP_USU_COD, EXP_USU_NOM, EXP_UOR_COD, EXP_UOR_NOM, 
									EXP_OBS, EXP_PRO_COD, EXP_PRO_NOM, TRA_FEI, TRA_FEF, TRA_NOM, TRA_COD, TRA_OBS, TRA_UOR_COD, TRA_UOR_NOM, 
									EXT_TER) 
AS
	SELECT E_EXP.EXP_LOC, E_EXP.EXP_FEI, E_EXP.EXP_FEF, E_EXP.EXP_ASU, E_EXP.EXP_NUM, E_EXP.EXP_EJE, E_EXP.EXP_USU,
			@sge_generico.dbo.A_USU.USU_NOM, A1.UOR_COD_VIS, A1.UOR_NOM, E_EXP.EXP_OBS, E_EXP.EXP_PRO, E_PML.PML_VALOR,
			E_CRO.CRO_FEI, E_CRO.CRO_FEF, E_TML.TML_VALOR, E_CRO.CRO_TRA, E_CRO.CRO_OBS, A2.UOR_COD_VIS, A2.UOR_NOM, EXT_TER
	FROM E_CRO 
	INNER JOIN E_TML ON 
		(E_CRO.CRO_MUN = E_TML.TML_MUN 
		AND E_CRO.CRO_PRO = E_TML.TML_PRO 
		AND E_CRO.CRO_TRA = E_TML.TML_TRA 
		AND E_TML.TML_CMP = 'NOM')
	LEFT OUTER JOIN E_EXP ON 
		(E_CRO.CRO_MUN = E_EXP.EXP_MUN 
		AND E_CRO.CRO_PRO = E_EXP.EXP_PRO 
		AND E_CRO.CRO_NUM = E_EXP.EXP_NUM)
	LEFT OUTER JOIN E_EXT ON 
		(E_CRO.CRO_MUN = E_EXT.EXT_MUN 
		AND E_CRO.CRO_PRO = E_EXT.EXT_PRO 
		AND E_CRO.CRO_NUM = E_EXT.EXT_NUM)
  	INNER JOIN A_UOR A1 ON 
  		(E_EXP.EXP_UOR = A1.UOR_COD)
  	INNER JOIN A_UOR A2 ON 
  		(E_CRO.CRO_UTR = A2.UOR_COD)
  	INNER JOIN E_PML ON 
  		(E_PML.PML_LENG = E_TML.TML_LENG 
  		AND E_PML.PML_CMP = 'NOM' 
  		AND E_EXP.EXP_PRO = E_PML.PML_COD 
  		AND E_EXP.EXP_MUN = E_PML.PML_MUN)
	INNER JOIN @sge_generico.dbo.A_USU ON 
		(E_EXP.EXP_USU = @sge_generico.dbo.A_USU.USU_COD)
	INNER JOIN @sge_generico.dbo.T_MUN ON 
		(E_CRO.CRO_MUN = @sge_generico.dbo.T_MUN.MUN_COD 
		AND @sge_generico.dbo.T_MUN.MUN_PAI = 108 
		AND @sge_generico.dbo.T_MUN.MUN_PRV = 19);
GO
		
		
		
CREATE VIEW [V_INT] (INT_MUN, INT_EJE, INT_NUM, INT_TER, INT_NVR, INT_CDT, INT_NOR, INT_NOC, INT_NOM, INT_AP1, INT_AP2, 
							INT_DOC, INT_DOMN, INT_DOMNN, INT_ROL, INT_RPD, INT_POB, INT_TLF, INT_RCA, INT_PRV, INT_ECO, INT_ESI, INT_CPO) 
AS 
	SELECT e_ext.ext_mun,e_ext.ext_eje,e_ext.ext_num,e_ext.ext_ter,e_ext.ext_nvr,e_ext.ext_dot, t_dom.dom_nml,
			CAST(hte_nom AS VARCHAR) 
				+ ' ' 
				+ CAST(t_hte.hte_pa1 AS VARCHAR) 
				+ ' ' 
				+ CAST(t_hte.hte_ap1 AS VARCHAR) 
				+ ' ' 
				+ CAST(t_hte.hte_pa2 AS VARCHAR) 
				+ '' 
				+ CAST(t_hte.hte_ap2 AS VARCHAR) AS titular,
			 t_hte.hte_doc,hte_nom,hte_ap1,hte_ap2,
			 CAST(t_tvi.tvi_des AS VARCHAR) 
			 	+ ' ' 
			 	+ CAST(t_via.via_nom AS VARCHAR) 
			 	+ ' ' 
			 	+ CAST(t_dsu.dsu_nud AS VARCHAR) 
			 	+ ' ' 
			 	+ CAST(t_dsu.dsu_led AS VARCHAR) 
			 	+ ' ' 
			 	+ CAST(t_dsu.dsu_nuh AS VARCHAR) 
			 	+ CAST(t_dsu.dsu_leh AS VARCHAR) 
			 	+ CAST(
			 	(
				CASE
					 WHEN t_dsu.dsu_blq  IS NOT NULL
					 AND	t_dsu.dsu_blq  <> ' ' THEN ' Bl. '
					 ELSE ''
				 END) AS VARCHAR) 
				+ CAST(t_dsu.dsu_blq AS VARCHAR) 
				+ CAST(
				(
				CASE
					WHEN t_dsu.dsu_por  IS NOT NULL
					AND	t_dsu.dsu_por  <> ' ' THEN ' Portal. '
					ELSE ''
				END) AS VARCHAR) + CAST(t_dsu.dsu_por AS VARCHAR) 
				+ CAST(
				(
				CASE
					 WHEN t_dpo.dpo_esc  IS NOT NULL
					 AND	t_dpo.dpo_esc  <> ' ' THEN ' Esc. '
					 ELSE ''
				END) AS VARCHAR) 
				+ CAST(t_dpo.dpo_esc AS VARCHAR) 
				+ ' ' 
				+ CAST(t_dpo.dpo_plt AS VARCHAR) 
				+ CAST(
				(
				CASE
					WHEN t_dpo.dpo_pta  IS NOT NULL
					AND	t_dpo.dpo_pta  <> ' ' THEN ' ยบ '
					ELSE ''
				END) AS VARCHAR) + CAST(t_dpo.dpo_pta AS VARCHAR) AS domicilio,
			NULL AS domicilionn,
			e_rol.rol_des,e_rol.rol_pde,@sge_generico.dbo.t_mun.mun_nom,t_hte.hte_tlf,null as dnn_rca,
			@sge_generico.dbo.t_prv.prv_nom,t_eco.eco_nom,t_esi.esi_nom,
				(
				CASE
					WHEN t_dsu.dsu_cpo  IS NOT NULL
					AND	t_dsu.dsu_cpo  <> ' ' THEN t_dsu.dsu_cpo
					ELSE ''
				END)
	FROM  e_ext  
		LEFT OUTER JOIN  t_hte  ON  
			e_ext.ext_ter  = t_hte.hte_ter
			AND	e_ext.ext_nvr  = t_hte.hte_nvr   
		LEFT OUTER JOIN  e_rol  ON  
			e_ext.ext_pro  = e_rol.rol_pro
			AND	e_ext.ext_rol  = e_rol.rol_cod ,
		t_dsu  LEFT OUTER JOIN  @sge_generico.dbo.t_mun  ON  
			t_dsu.dsu_pai  = @sge_generico.dbo.t_mun.mun_pai
		 	AND	t_dsu.dsu_prv  = @sge_generico.dbo.t_mun.mun_prv
		 	AND	t_dsu.dsu_mun  = @sge_generico.dbo.t_mun.mun_cod   
		 LEFT OUTER JOIN  @sge_generico.dbo.t_prv  ON  
		 @sge_generico."dbo".t_mun.mun_pai  =@sge_generico.dbo.t_prv.prv_pai
		 	AND	@sge_generico.dbo.t_mun.mun_prv  = @sge_generico."dbo".t_prv.prv_cod   
		 LEFT OUTER JOIN  t_esi  ON  t_dsu.dsu_pai  = t_esi.esi_pai
		 	AND	t_dsu.dsu_prv  = t_esi.esi_prv
			AND	t_dsu.dsu_mun  = t_esi.esi_mun
			AND	t_dsu.dsu_esi  = t_esi.esi_cod   
		LEFT OUTER JOIN  t_eco  ON  
			t_esi.esi_pai  = t_eco.eco_pai
			AND	t_esi.esi_mun  = t_eco.eco_mun
			AND	t_esi.esi_prv  = t_eco.eco_prv
			AND	t_esi.esi_eco  = t_eco.eco_cod ,
		 t_dom, t_dpo, t_via, t_tvi 
	WHERE	 e_ext.ext_dot  = t_dom.dom_cod
		AND	e_ext.ext_dot  = t_dpo.dpo_dom
		AND	t_dpo.dpo_dsu  = t_dsu.dsu_cod
		AND	t_dsu.dsu_pai  = t_via.via_pai
		AND	t_dsu.dsu_prv  = t_via.via_prv
		AND	t_dsu.dsu_mun  = t_via.via_mun
		AND	t_dsu.dsu_via  = t_via.via_cod
		AND	t_via.via_tvi  = t_tvi.tvi_cod
	UNION	 	
	SELECT e_ext.ext_mun,e_ext.ext_eje,e_ext.ext_num,e_ext.ext_ter,e_ext.ext_nvr,e_ext.ext_dot,t_dom.dom_nml, hte_noc AS titular,
			hte_nom,hte_ap1,hte_ap2,hte_doc,NULL AS domicilio,
			(CASE 
				WHEN t_tvi.tvi_des IS NOT NULL AND t_tvi.tvi_des <> ' ' 
					THEN t_tvi.tvi_des + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_via.via_nom IS NOT NULL AND t_via.via_nom <> ' ' 
					THEN t_via.via_nom + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_dmc IS NOT NULL AND t_dnn.dnn_dmc <> ' ' 
					THEN t_dnn.dnn_dmc + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_nud IS NOT NULL 
					THEN CONVERT(varchar, t_dnn.dnn_nud)+ ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_led IS NOT NULL AND t_dnn.dnn_led <> ' ' 
					THEN t_dnn.dnn_led + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_nuh IS NOT NULL 
					THEN CONVERT(varchar, t_dnn.dnn_nuh) + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_leh IS NOT NULL AND t_dnn.dnn_leh <> ' ' 
					THEN t_dnn.dnn_leh + ' ' 
					ELSE '' END) 
			+ (CASE 
					WHEN t_dnn.dnn_blq IS NOT NULL AND t_dnn.dnn_blq <> ' ' 
						THEN ' Bl. ' + t_dnn.dnn_blq + ' ' 
						ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_por IS NOT NULL AND t_dnn.dnn_por <> ' ' 
					THEN ' Portal. ' + t_dnn.dnn_por + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_esc IS NOT NULL AND t_dnn.dnn_esc <> ' ' 
					THEN ' Esc. ' + t_dnn.dnn_esc + ' ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_plt IS NOT NULL AND t_dnn.dnn_plt <> ' ' 
					THEN t_dnn.dnn_plt + 'ยบ ' 
					ELSE '' END) 
			+ (CASE 
				WHEN t_dnn.dnn_pta IS NOT NULL AND t_dnn.dnn_pta <> ' ' 
					THEN t_dnn.dnn_pta + ' ' 
					ELSE '' END ) AS domicilionn,
			e_rol.rol_des,e_rol.rol_pde,t_mun.mun_nom,t_hte.hte_tlf ,t_dnn.dnn_rca,t_prv.prv_nom,t_eco.eco_nom,t_esi.esi_nom,
			(
				CASE
					WHEN t_dnn.dnn_cpo  IS NOT NULL
					AND	t_dnn.dnn_cpo  <> ' ' THEN t_dnn.dnn_cpo
					ELSE ''
				END)
	FROM e_ext	
		LEFT OUTER JOIN t_hte ON 
			(e_ext.ext_ter = t_hte.hte_ter 
			AND e_ext.ext_nvr = t_hte.hte_nvr)
		INNER JOIN t_dom ON 
			(e_ext.ext_dot = t_dom.dom_cod)
		INNER JOIN t_dnn ON 
			(e_ext.ext_dot = dnn_dom)
		LEFT OUTER JOIN e_rol ON 
			(e_ext.ext_pro = e_rol.rol_pro 
			AND e_ext.ext_rol = e_rol.rol_cod)
		LEFT OUTER JOIN @sge_generico.dbo.t_mun t_mun ON 
			(t_dnn.dnn_pai = t_mun.mun_pai 
			AND t_dnn.dnn_prv = t_mun.mun_prv 
			AND t_dnn.dnn_mun = t_mun.mun_cod)
		LEFT OUTER JOIN t_via ON (t_dnn.dnn_pai = t_via.via_pai 
			AND t_dnn.dnn_prv = t_via.via_prv 
			AND t_dnn.dnn_mun = t_via.via_mun 
			AND t_dnn.dnn_via = t_via.via_cod)
		LEFT OUTER JOIN t_tvi ON 
			(t_via.via_tvi = t_tvi.tvi_cod)
		LEFT OUTER JOIN @sge_generico.dbo.t_prv t_prv ON 
			(t_dnn.dnn_pai = t_prv.prv_pai 
			AND t_dnn.dnn_prv = t_prv.prv_cod)
		LEFT OUTER JOIN t_esi ON 
			(t_dnn.dnn_pai = t_esi.esi_pai 
			AND t_dnn.dnn_mun = t_esi.esi_mun 
			AND t_dnn.dnn_prv = t_esi.esi_prv 
			AND t_dnn.dnn_esi = t_esi.esi_cod)
		LEFT OUTER JOIN t_eco ON 
			(t_esi.esi_pai = t_eco.eco_pai 
			AND t_esi.esi_mun = t_eco.eco_mun 
			AND t_esi.esi_prv = t_eco.eco_prv 
			AND t_esi.esi_eco = t_eco.eco_cod);
GO
			
CREATE VIEW  [V_REG] (REG_DEP, REG_UOR, REG_TIP, REG_EJE, REG_NUM, REG_EST, REG_FEC, REG_FED, REG_ASU, REG_INT, REG_DOM, REG_CTD, 
							REG_DTD, REG_MOD, REG_CTR, REG_DTR, REG_CTT, REG_DTT, REG_MUN, REG_DEST, REG_CAC, REG_DAC, REG_DIL) 
AS 
	SELECT DISTINCT res_dep, res_uor, res_tip, res_eje, res_num,
		(CASE 
			WHEN res_est = 0 
				THEN 'PENDIENTE' 
			WHEN res_est = 1 
				THEN 'ACEPTADA' 
			WHEN res_est = 2 
				THEN 'RECHAZADA' 
			WHEN res_est = 9 
				THEN 'ANULADA' 
			ELSE NULL END ) AS res_est,
        CONVERT (char, res_fec, 131) AS fechaentrada, CONVERT (char, res_fed, 131) AS fechadocum, res_asu,
        ( 
        (CASE 
        	WHEN hte_pa1 IS NULL OR hte_pa1 = ' ' 
        		THEN '' 
        		ELSE hte_pa1 + ' ' END ) 
        		+ 
        (CASE 
        	WHEN hte_ap1 IS NULL OR hte_ap1 = ' ' 
        		THEN '' 
        		ELSE hte_ap1 + ' ' END ) 
        		+ 
        (CASE 
        	WHEN hte_pa2 IS NULL OR hte_pa2 = ' ' 
        		THEN '' 
        		ELSE hte_pa2 + ' ' END ) 
        		+ 
        (CASE 
        	WHEN hte_ap2 IS NULL OR hte_ap2 = ' ' 
        		THEN '' 
        		ELSE hte_ap2 END ) 
        		+ ',' + hte_nom ) AS interesado,
        t_dnn.dnn_dmc + ' ' + CONVERT(char, t_dnn.dnn_nud) + ' ' + t_dnn.dnn_led + ' ' + CONVERT(char, t_dnn.dnn_nuh) + t_dnn.dnn_leh + 
        (CASE 
        	WHEN t_dnn.dnn_blq IS NOT NULL AND t_dnn.dnn_blq <> ' ' 
        		THEN ' Bl. ' 
        		ELSE '' END ) 
        		+ t_dnn.dnn_blq + 
        (CASE 
        	WHEN t_dnn.dnn_por IS NOT NULL AND t_dnn.dnn_por <> ' ' 
        		THEN ' Portal. ' 
        		ELSE '' END ) 
        		+ t_dnn.dnn_por + 
        (CASE 	
        	WHEN t_dnn.dnn_esc IS NOT NULL AND t_dnn.dnn_esc <> ' ' 
        		THEN ' Esc. ' 
        		ELSE '' END ) 
        		+ t_dnn.dnn_esc + t_dnn.dnn_plt + 
        (CASE 
        	WHEN t_dnn.dnn_pta IS NOT NULL AND t_dnn.dnn_pta <> ' ' 
        		THEN ' บ ' 
        		ELSE '' END ) + t_dnn.dnn_pta AS domicilio,
        tdo_cod, tdo_des, 'ORDINARIA' AS tipoent,  tpe_cod, tpe_des, ttr_cod, ttr_des,  mun_nom, uor_nom AS destino, act_cod,  act_des, dil_txt
	FROM r_res  
		INNER JOIN t_hte ON 
			(res_ter = hte_ter AND res_tnv = hte_nvr)
        INNER JOIN t_dnn ON 
        	(res_dom = dnn_dom)
        INNER JOIN @sge_generico.dbo.t_mun t_mun ON 
        	(dnn_pai = mun_pai 
        	AND dnn_prv = mun_prv 
        	AND dnn_mun = mun_cod)
        LEFT OUTER JOIN a_uor ON 
        	(res_uod = uor_cod)
        INNER JOIN r_tdo ON 
        	(res_tdo = tdo_ide)
        INNER JOIN r_tpe ON 
        	(res_tpe = tpe_ide)
        LEFT OUTER JOIN r_ttr ON 
        	(res_ttr = ttr_ide)
        LEFT OUTER JOIN r_act ON 
        	(res_act = act_ide)
        LEFT OUTER JOIN r_dil ON 
        	(res_dep = dil_dep 
        	AND res_uor = dil_uor 
        	AND res_tip = dil_tir 
        	AND res_fec = dil_fec)
	WHERE res_mod = 0
	UNION
	SELECT DISTINCT res_dep, res_uor, res_tip, res_eje, res_num,
		(CASE 
			WHEN res_est = 0 
				THEN 'PENDIENTE' 
			WHEN res_est = 1 
				THEN 'ACEPTADA' 
			WHEN res_est = 2 
				THEN 'RECHAZADA' 
			WHEN res_est = 9 
				THEN 'ANULADA' 
			ELSE NULL END ) AS res_est,
        CONVERT (char, res_fec, 131) AS fechaentrada, CONVERT (char, res_fed, 131) AS fechadocum, res_asu,
        ( 
        (CASE 
        	WHEN hte_pa1 IS NULL OR hte_pa1 = ' ' 
        		THEN '' 
        		ELSE hte_pa1 + ' ' END ) 
        + (CASE
        	WHEN hte_ap1 IS NULL OR hte_ap1 = ' ' 
        		THEN '' 
        		ELSE hte_ap1 + ' ' END ) 
        + (CASE 
        	WHEN hte_pa2 IS NULL OR hte_pa2 = ' ' 
        		THEN '' 
        		ELSE hte_pa2 + ' ' END ) 
        + (CASE 
        	WHEN hte_ap2 IS NULL OR hte_ap2 = ' ' 
        		THEN '' 
        		ELSE hte_ap2 END ) 
        + ',' + hte_nom ) AS interesado,
        t_tvi.tvi_des + ' ' + t_via.via_nom + ' ' + CONVERT(char, t_dsu.dsu_nud) + ' ' + t_dsu.dsu_led + ' ' + 
        CONVERT(char, t_dsu.dsu_nuh) + t_dsu.dsu_leh + 
        (CASE 
        	WHEN t_dsu.dsu_blq IS NOT NULL AND t_dsu.dsu_blq <> ' ' 
        		THEN ' Bl. ' 
        		ELSE '' END ) 
        + t_dsu.dsu_blq 
        + (CASE 
        	WHEN t_dsu.dsu_por IS NOT NULL AND t_dsu.dsu_por <> ' ' 
        		THEN ' Portal. ' 
        		ELSE '' END ) 
        + t_dsu.dsu_por 
        + (CASE 
        	WHEN t_dpo.dpo_esc IS NOT NULL AND t_dpo.dpo_esc <> ' ' 
        		THEN ' Esc. ' 
        		ELSE '' END ) 
        + t_dpo.dpo_esc + t_dpo.dpo_plt + 
        (CASE 
        	WHEN t_dpo.dpo_pta IS NOT NULL AND t_dpo.dpo_pta <> ' ' 
        		THEN ' บ ' 
        		ELSE '' END ) + t_dpo.dpo_pta AS domicilio,
        tdo_cod, tdo_des, 'ORDINARIA' AS tipoent, tpe_cod, tpe_des, ttr_cod, ttr_des, mun_nom, uor_nom AS destino, act_cod, act_des, dil_txt
	FROM r_res  
		INNER JOIN t_hte ON 
			(res_ter = hte_ter 
			AND res_tnv = hte_nvr)
        INNER JOIN t_dpo ON 
        	(res_dom = dpo_dom)
        INNER JOIN t_dsu ON 
        	(dpo_dsu = dsu_cod)
        INNER JOIN @sge_generico.dbo.t_mun t_mun ON 
        	(dsu_pai = mun_pai 
        	AND dsu_prv = mun_prv 
        	AND dsu_mun = mun_cod)
        LEFT OUTER JOIN t_via ON 
        	(t_dsu.dsu_pai = t_via.via_pai 
        	AND t_dsu.dsu_prv = t_via.via_prv 
        	AND t_dsu.dsu_mun = t_via.via_mun 
        	AND t_dsu.dsu_via = t_via.via_cod)
        LEFT OUTER JOIN t_tvi ON 
        	(t_via.via_tvi = t_tvi.tvi_cod)
        LEFT OUTER JOIN a_uor ON 
        	(res_uod = uor_cod)
        INNER JOIN r_tdo ON 
        	(res_tdo = tdo_ide)
        INNER JOIN r_tpe ON 
        	(res_tpe = tpe_ide)
        LEFT OUTER JOIN r_ttr ON 
        	(res_ttr = ttr_ide)
        LEFT OUTER JOIN r_act ON 
        	(res_act = act_ide)
        LEFT OUTER JOIN r_dil ON 
        	(res_dep = dil_dep 
        	AND res_uor = dil_uor 
        	AND res_tip = dil_tir 
        	AND res_fec = dil_fec)
	WHERE res_mod = 0
	UNION
	SELECT DISTINCT res_dep, res_uor, res_tip, res_eje, res_num,
		(CASE 
			WHEN res_est = 0 
				THEN 'PENDIENTE' 
			WHEN res_est = 1 
				THEN 'ACEPTADA' 
			WHEN res_est = 2 
				THEN 'RECHAZADA' 
			WHEN res_est = 9 
				THEN 'ANULADA' 
			ELSE NULL END ) AS res_est,
        CONVERT (char, res_fec, 131) AS fechaentrada, CONVERT (char, res_fed, 131) AS fechadocum, res_asu,
        ((CASE 
        	WHEN hte_pa1 IS NULL OR hte_pa1 = ' ' 
        		THEN '' 
        		ELSE hte_pa1 + ' ' END ) 
        + (CASE 
        	WHEN hte_ap1 IS NULL OR hte_ap1 = ' ' 
        		THEN '' 
        		ELSE hte_ap1 + ' ' END ) 
        + (CASE 
        	WHEN hte_pa2 IS NULL OR hte_pa2 = ' ' 
        		THEN '' 
        		ELSE hte_pa2 + ' ' END ) + 
        (CASE 
        	WHEN hte_ap2 IS NULL OR hte_ap2 = ' ' 
        		THEN '' 
        		ELSE hte_ap2 END ) + ',' + hte_nom ) AS interesado,
        t_dnn.dnn_dmc + ' ' + CONVERT(char, t_dnn.dnn_nud) + ' ' + t_dnn.dnn_led + ' ' + CONVERT(char, t_dnn.dnn_nuh) + t_dnn.dnn_leh + 
        (CASE 
        	WHEN t_dnn.dnn_blq IS NOT NULL AND t_dnn.dnn_blq <> ' ' 
        		THEN ' Bl. ' 
        		ELSE '' END ) + t_dnn.dnn_blq + 
        (CASE 
        	WHEN t_dnn.dnn_por IS NOT NULL AND t_dnn.dnn_por <> ' ' 
        		THEN ' Portal. ' 
        		ELSE '' END ) + t_dnn.dnn_por + 
        (CASE 
        	WHEN t_dnn.dnn_esc IS NOT NULL AND t_dnn.dnn_esc <> ' ' 
        		THEN ' Esc. ' 
        		ELSE '' END ) + t_dnn.dnn_esc + t_dnn.dnn_plt + 
        (CASE 
        	WHEN t_dnn.dnn_pta IS NOT NULL AND t_dnn.dnn_pta <> ' ' 
        		THEN ' บ ' 
        		ELSE '' END ) + t_dnn.dnn_pta AS domicilio,
        tdo_cod, tdo_des, 'DESTINO OTRO REGISTRO' AS tipoent, tpe_cod, tpe_des, ttr_cod, ttr_des, mun_nom, org_des AS destino, act_cod, act_des, dil_txt
	FROM r_res  
		INNER JOIN t_hte ON 
			(res_ter = hte_ter AND res_tnv = hte_nvr)
        INNER JOIN t_dnn ON 
        	(res_dom = dnn_dom)
        INNER JOIN @sge_generico.dbo.t_mun t_mun ON 
        	(dnn_pai = mun_pai 
        	AND dnn_prv = mun_prv 
        	AND dnn_mun = mun_cod)
        LEFT OUTER JOIN @sge_generico.dbo.a_org ON 
        	(res_ocd = org_cod)
        INNER JOIN r_tdo ON 
        	(res_tdo = tdo_ide)
        INNER JOIN r_tpe ON 
        	(res_tpe = tpe_ide)
        LEFT OUTER JOIN r_ttr ON 
        	(res_ttr = ttr_ide)
        LEFT OUTER JOIN r_act ON 
        	(res_act = act_ide)
        LEFT OUTER JOIN r_dil ON 
        	(res_dep = dil_dep 
        	AND res_uor = dil_uor 
        	AND res_tip = dil_tir 
        	AND res_fec = dil_fec)
	WHERE res_mod = 1
	UNION
    SELECT DISTINCT res_dep, res_uor, res_tip, res_eje, res_num,
    	(CASE 
    		WHEN res_est = 0 
    			THEN 'PENDIENTE' 
    		WHEN res_est = 1 
    			THEN 'ACEPTADA' 
    		WHEN res_est = 2 
    			THEN 'RECHAZADA' 
    		WHEN res_est = 9 
    			THEN 'ANULADA' ELSE NULL END ) AS res_est,
        CONVERT (char, res_fec, 131) AS fechaentrada, CONVERT (char, res_fed, 131) AS fechadocum, res_asu,
        ((CASE 
        	WHEN hte_pa1 IS NULL OR hte_pa1 = ' ' 
        		THEN '' 
        		ELSE hte_pa1 + ' ' END ) + 
		(CASE 
			WHEN hte_ap1 IS NULL OR hte_ap1 = ' ' 
        		THEN '' 
        		ELSE hte_ap1 + ' ' END ) + 
        (CASE
			WHEN hte_pa2 IS NULL OR hte_pa2 = ' ' 
        		THEN '' 
        		ELSE hte_pa2 + ' ' END ) + (CASE 
        	WHEN hte_ap2 IS NULL OR hte_ap2 = ' ' 
        		THEN '' 
        		ELSE hte_ap2 END ) + ',' + hte_nom ) AS interesado,
        t_tvi.tvi_des + ' ' + t_via.via_nom + ' ' + CONVERT(char, t_dsu.dsu_nud) + ' ' + t_dsu.dsu_led + ' ' + 
        CONVERT(char, t_dsu.dsu_nuh) + t_dsu.dsu_leh + 
        (CASE 
        	WHEN t_dsu.dsu_blq IS NOT NULL AND t_dsu.dsu_blq <> ' ' 
        		THEN ' Bl. ' 
        		ELSE '' END ) + t_dsu.dsu_blq + 
        (CASE 
        	WHEN t_dsu.dsu_por IS NOT NULL AND t_dsu.dsu_por <> ' ' 
        		THEN ' Portal. ' 
        		ELSE '' END ) + t_dsu.dsu_por + 
        (CASE 
        	WHEN t_dpo.dpo_esc IS NOT NULL AND t_dpo.dpo_esc <> ' ' 
        		THEN ' Esc. ' 
        		ELSE '' END ) + t_dpo.dpo_esc + t_dpo.dpo_plt + 
        (CASE 
        	WHEN t_dpo.dpo_pta IS NOT NULL AND t_dpo.dpo_pta <> ' ' 
        		THEN ' บ ' 
        		ELSE '' END ) + t_dpo.dpo_pta AS domicilio,
        tdo_cod, tdo_des, 'DESTINO OTRO REGISTRO' AS tipoent, tpe_cod, tpe_des, ttr_cod, ttr_des, mun_nom, org_des AS destino,
        act_cod, act_des, dil_txt
	FROM r_res  
		INNER JOIN t_hte ON 
			(res_ter = hte_ter 
			AND res_tnv = hte_nvr)
        INNER JOIN t_dpo ON 
        	(res_dom = dpo_dom)
        INNER JOIN t_dsu ON 
        	(dpo_dsu = dsu_cod)
        INNER JOIN @sge_generico.dbo.t_mun t_mun ON 
        	(dsu_pai = mun_pai 
        	AND dsu_prv = mun_prv 
        	AND dsu_mun = mun_cod)
        LEFT OUTER JOIN @sge_generico.dbo.a_org ON 
        	(res_ocd = org_cod)
        LEFT OUTER JOIN t_via ON 
        	(t_dsu.dsu_pai = t_via.via_pai 
        	AND t_dsu.dsu_prv = t_via.via_prv 
        	AND t_dsu.dsu_mun = t_via.via_mun 
        	AND t_dsu.dsu_via = t_via.via_cod)
        LEFT OUTER JOIN t_tvi ON 
        	(t_via.via_tvi = t_tvi.tvi_cod)
        INNER JOIN r_tdo ON 
        	(res_tdo = tdo_ide)
        INNER JOIN r_tpe ON 
        	(res_tpe = tpe_ide)
        LEFT OUTER JOIN r_ttr ON 
        	(res_ttr = ttr_ide)
        LEFT OUTER JOIN r_act ON 
        	(res_act = act_ide)
        LEFT OUTER JOIN r_dil ON 
        	(res_dep = dil_dep 
        	AND res_uor = dil_uor 
        	AND res_tip = dil_tir 
        	AND res_fec = dil_fec)
	WHERE res_mod = 1;
GO
	
CREATE VIEW [dbo].[V_REG_INF] ("REG_FEC", "REG_FED", "REG_ASU", "REG_UOR_VIS","REG_CODUOR", "REG_UOR", "REG_UOR_NOM",
                                          "REG_OBS", "REG_INT", "REG_TIP", "REG_DOC", "REG_DOC_NOM", "REG_REM",
                                          "REG_REM_NOM", "REG_TRP", "REG_TRP_NOM", "REG_USU","REG_NUM","EXR_NUM") AS
  SELECT ISNULL(CONVERT(char,RES_FEC,103),'')  AS RES_FEC,
        ISNULL(CONVERT(char,RES_FED,103),'')  AS RES_FED,
        RES_ASU,
        UOR_COD_VIS, RES_UOD, RES_UOR, UOR_NOM,
        RES_OBS,
        HTE_NOM + ISNULL(' ','') + ISNULL(HTE_PA1,'') + ISNULL(' ','') +  ISNULL(HTE_AP1,'') + ISNULL(' ','') + ISNULL(HTE_PA2,'') + ISNULL(' ','') +  ISNULL(HTE_AP2,''),
        RES_TIP,
        r_tdo.TDO_COD,r_tdo.TDO_DES,
        r_tpe.TPE_COD,r_tpe.TPE_DES,
        r_ttr.TTR_COD AS COD_TRANSP,r_ttr.TTR_DES, A_USU.USU_NOM, CONVERT(VARCHAR,R_RES.RES_EJE) + '/' +  CONVERT(VARCHAR,R_RES.RES_NUM),E_EXR.EXR_NUM
  FROM R_RES, R_TDO, R_TPE, R_TTR, A_UOR, T_HTE, @sge_generico.dbo.A_USU A_USU,E_EXR
  WHERE R_RES.RES_TDO *= R_TDO.TDO_IDE AND
      	R_RES.RES_TPE *= R_TPE.TPE_IDE AND
      	R_RES.RES_TTR *= R_TTR.TTR_IDE AND
      	RES_TER *= HTE_TER AND
		RES_TNV *= HTE_NVR AND
      	R_RES.RES_UOD = UOR_COD AND
		A_USU.USU_COD = R_RES.RES_USU
		AND R_RES.RES_DEP*=E_EXR.EXR_DEP
		AND R_RES.RES_UOR*=E_EXR.EXR_UOR
		AND R_RES.RES_TIP*=E_EXR.EXR_TIP
		AND R_RES.RES_EJE*=E_EXR.EXR_EJE
		AND R_RES.RES_NUM*=E_EXR.EXR_NRE
GO	

  		
CREATE VIEW [dbo].[V_REG_JUS] (REG_FEC, REG_FED, REG_ASU, REG_UOR_VIS, REG_UOR, REG_CODUOR, REG_UOR_NOM, REG_OBS, REG_INT, REG_TIP, REG_DOC,
							REG_DOC_NOM, REG_REM, REG_REM_NOM, REG_TRP, REG_TRP_NOM, REG_USU, REG_NUM, REG_DOCSINT, 
REG_MOD, REG_TER, REG_ROL,
REG_INTERESADO, COD_TERCERO, VERSION_TERCERO, REG_PRO, REG_EXP)
AS
	SELECT DISTINCT CONVERT(VARCHAR,   res_fec,   121) AS res_fec,  CONVERT(VARCHAR,   res_fed,   121) AS res_fed, res_asu, uor_cod_vis,
  		res_uod, res_uor, uor_nom, res_obs, hte_nom + isnull(' ',   '') + isnull(hte_pa1,   '') + isnull(' ',   '') + isnull(hte_ap1,   '') +
  		isnull(' ',   '') + isnull(hte_pa2,   '') + isnull(' ',   '') + isnull(hte_ap2,   '') AS interesado, res_tip, r_tdo.tdo_cod, r_tdo.tdo_des,
  		r_tpe.tpe_cod, r_tpe.tpe_des, r_ttr.ttr_cod AS cod_transp, r_ttr.ttr_des, a_usu.usu_nom, CONVERT(CHAR,   r_res.res_eje) + '/' +
  		CONVERT(CHAR,   r_res.res_num) AS num, hte_doc,
  		(CASE
			WHEN res_mod = 0 THEN 'ORDINARIA'
			WHEN res_mod = 1 THEN 'DESTINO OTRO REGISTRO'
			ELSE NULL
			END) AS res_mod,
  		t_hte.hte_ter,  r_rol.rol_des,
(
    CASE
      WHEN res_ter = hte_ter
      THEN 'PRINCIPAL'
      ELSE 'SECUNDARIO'
    END ) AS TIPOINTERESADO,
    res_ter,
    res_tnv,
    procedimiento,
    exr_num
	FROM r_res
inner join e_exr on
(exr_dep         =res_dep
  AND exr_uor         =res_uor
  AND exr_tip         =res_tip
  AND exr_ejr         =res_eje
  AND exr_nre         =res_num)
	RIGHT JOIN r_tdo ON res_tdo = tdo_ide
	LEFT JOIN e_rol ON procedimiento = e_rol.rol_pro
	LEFT JOIN r_tpe ON res_tpe = tpe_ide
	LEFT JOIN r_ttr ON res_ttr = ttr_ide
	LEFT JOIN a_uor ON res_uod = uor_cod
	LEFT JOIN @sge_generico.dbo.a_usu a_usu ON a_usu.usu_cod = r_res.res_usu
	LEFT JOIN r_ext ON r_ext.ext_uor = r_res.res_uor
 		AND r_ext.ext_num = r_res.res_num
 		AND r_ext.ext_tip = r_res.res_tip
 		AND r_ext.ext_eje = r_res.res_eje
 	LEFT JOIN r_rol ON r_ext.ext_rol = r_rol.rol_cod
 	LEFT JOIN t_hte ON r_ext.ext_ter = t_hte.hte_ter
 		AND r_ext.ext_nvr = t_hte.hte_nvr
 	LEFT JOIN t_dnn ON r_ext.ext_dot = dnn_dom
 	LEFT JOIN @sge_generico.dbo.t_mun ON dnn_pai = mun_pai
 		AND dnn_prv = mun_prv
 		AND dnn_mun = mun_cod
	WHERE procedimiento IS NOT NULL
	UNION
	SELECT DISTINCT CONVERT(VARCHAR,   res_fec,   121) AS res_fec, CONVERT(VARCHAR,   res_fed,   121) AS res_fed, res_asu, uor_cod_vis,
  		res_uod,  res_uor, uor_nom, res_obs, hte_nom + isnull(' ',   '') + isnull(hte_pa1,   '') + isnull(' ',   '') + isnull(hte_ap1,   '') +
  		isnull(' ',   '') + isnull(hte_pa2,   '') + isnull(' ',   '') + isnull(hte_ap2,   '') AS interesado, res_tip, r_tdo.tdo_cod, r_tdo.tdo_des,
  		r_tpe.tpe_cod, r_tpe.tpe_des, r_ttr.ttr_cod AS cod_transp, r_ttr.ttr_des, a_usu.usu_nom, CONVERT(CHAR,   r_res.res_eje) + '/' +
  		CONVERT(CHAR,   r_res.res_num) AS num, hte_doc,
  		(CASE
			WHEN res_mod = 0 THEN 'ORDINARIA'
			WHEN res_mod = 1 THEN 'DESTINO OTRO REGISTRO'
			ELSE NULL
			END) AS res_mod,
  		t_hte.hte_ter,  r_rol.rol_des,
(
    CASE
      WHEN res_ter = hte_ter
      THEN 'PRINCIPAL'
      ELSE 'SECUNDARIO'
    END ) AS TIPOINTERESADO,
    res_ter,
    res_tnv,
    procedimiento,
    exr_num
	FROM r_res 
inner join e_exr on
(exr_dep         =res_dep
  AND exr_uor         =res_uor
  AND exr_tip         =res_tip
  AND exr_ejr         =res_eje
  AND exr_nre         =res_num)
RIGHT JOIN r_tdo ON (res_tdo = tdo_ide)
	LEFT JOIN r_tpe ON res_tpe = tpe_ide
	LEFT JOIN r_ttr ON res_ttr = ttr_ide
	LEFT JOIN a_uor ON res_uod = uor_cod
	LEFT JOIN @sge_generico.dbo.a_usu a_usu ON a_usu.usu_cod = r_res.res_usu
	LEFT JOIN r_ext ON r_ext.ext_uor = r_res.res_uor
		 AND r_ext.ext_num = r_res.res_num
		 AND r_ext.ext_tip = r_res.res_tip
		 AND r_ext.ext_eje = r_res.res_eje
	LEFT JOIN r_rol ON r_ext.ext_rol = r_rol.rol_cod
	LEFT JOIN t_hte ON r_ext.ext_ter = t_hte.hte_ter
		 AND r_ext.ext_nvr = t_hte.hte_nvr
	LEFT JOIN t_dnn ON r_ext.ext_dot = dnn_dom
	LEFT JOIN @sge_generico.dbo.t_mun ON dnn_pai = mun_pai
		 AND dnn_prv = mun_prv
		 AND dnn_mun = mun_cod
	WHERE procedimiento IS NULL;
GO
