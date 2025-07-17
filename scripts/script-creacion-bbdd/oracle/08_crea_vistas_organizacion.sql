--------------------------------------------------------------------------
-- FLEXIA 10.4 
-- Altia Consultores 
-- Scripts creación BD flexia 
-- Oracle
-- Crea las vistas necesarias de la organización sobre el 
-- esquema generico
-- Ejecutar con usuario del esquema de la organizacion
--------------------------------------------------------------------------

spool 8_crea_vistas_organizacion.log
SET ECHO ON;

-- Definir las siguientes propiedades de la organización donde se ejecutará
DEFINE nom_esquema_generico = FLEXIA_GENERICO;
-- Pais de la organización
DEFINE cod_pais = 108;
-- MUnicipio de la organización
DEFINE cod_municipio = 15;
-- Idioma por defecto de la aplicación
DEFINE idiomaDefecto = 1;

CREATE OR REPLACE VIEW E_PORTAFIRMAS (CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD, USU_COD, FIR_EST, FIR, CRD_FMO, FX_FIRMA, OBSERV, PML_VALOR, TML_VALOR, CRD_DES) AS 
	SELECT f.crd_mun, f.crd_pro, f.crd_eje, f.crd_num, f.crd_tra, f.crd_ocu, f.crd_nud, f.usu_cod, f.fir_est, f.fir, d.crd_fmo, f.fx_firma, f.observ, p.pml_valor, t.tml_valor, d.crd_des
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
    		AND (p.pml_leng = &idiomaDefecto)
    		AND (t.tml_mun = d.crd_mun)
    		AND (t.tml_pro = d.crd_pro)
    		AND (t.tml_tra = d.crd_tra)
    		AND (t.tml_leng = &idiomaDefecto)
;

  

CREATE OR REPLACE VIEW EXPEDIENTES (CRO_MUN, CRO_PRO, CRO_EJE, CRO_NUM, CRO_FEI, CRO_FEF, EXP_FEI, EXP_FEF, PRO_TIP, TRA_COD, TRA_COU, PRO_ARE, UOT_UOR, TRA_CLS, CRO_OCU, CRO_UTR) AS 
	SELECT exp_mun, exp_pro, exp_eje, exp_num, exp_fei, exp_fef, exp_fei, exp_fef, pro_tip, 999 tra_cod, 999 tra_cou, pro_are, NULL, 999 tra_cls, 1 cro_ocu, exp_uor
	FROM e_pro, e_exp
	WHERE pro_mun = exp_mun AND pro_cod = exp_pro
;

 
 
CREATE OR REPLACE VIEW G_PORTAFIRMAS (CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD, USU_COD, FIR_EST, FIR, CRD_FMO, FX_FIRMA, OBSERV, PML_VALOR, TML_VALOR, CRD_DES) AS 
	SELECT f.crd_mun, f.crd_pro, f.crd_eje, f.crd_num, f.crd_tra, f.crd_ocu, f.crd_nud, f.usu_cod, f.fir_est, f.fir, d.crd_fmo, f.fx_firma, f.observ, p.pml_valor, t.tml_valor, d.crd_des
	FROM g_crd d, g_crd_fir f, e_pml p, e_tml t
	WHERE(d.crd_mun = f.crd_mun)
 		AND(d.crd_pro = f.crd_pro)
		AND(d.crd_eje = f.crd_eje)
		AND(d.crd_num = f.crd_num)
		AND(d.crd_tra = f.crd_tra)
		AND(d.crd_ocu = f.crd_ocu)
		AND(d.crd_nud = f.crd_nud)
		AND(p.pml_mun = d.crd_mun)
		AND(p.pml_cod = d.crd_pro)
		AND(p.pml_leng = &idiomaDefecto)
		AND(t.tml_mun = d.crd_mun)
		AND(t.tml_pro = d.crd_pro)
		AND(t.tml_tra = d.crd_tra)
		AND(t.tml_leng = &idiomaDefecto)
;
 
  

CREATE OR REPLACE FORCE VIEW T_INT (INT_TER, INT_NVR,INT_NOC, INT_NOM, INT_AP1, INT_AP2, 
INT_DOC, INT_TID,INT_DOM, INT_DNN,INT_MUN, INT_TLFO, INT_PRV, INT_CPO)
AS
select HTE_TER, HTE_NVR,
    hte_nom
    || ' '
    ||t_hte.hte_pa1
    || ''
    || t_hte.hte_ap1
    || (
    CASE
      WHEN t_hte.hte_pa1 IS NOT NULL
      OR t_hte.hte_ap1   IS NOT NULL
      THEN ' '
      ELSE ''
    END)
    || t_hte.hte_pa2
    || ''
    || t_hte.hte_ap2 AS titular,
    hte_nom,
    hte_ap1,
    hte_ap2,
    t_hte.hte_doc,
     t_hte.hte_TID,
    NULL AS domicilio,
    t_tvi.tvi_des
    || ' '
    || t_via.via_nom
    || ' '
    || t_dnn.dnn_dmc
    || ' '
    || t_dnn.dnn_nud
    || ' '
    || t_dnn.dnn_led
    || ' '
    || t_dnn.dnn_nuh
    || t_dnn.dnn_leh
    || (
    CASE
      WHEN t_dnn.dnn_blq IS NOT NULL
      AND t_dnn.dnn_blq  <> ' '
      THEN ' Bl. '
      ELSE ''
    END)
    || t_dnn.dnn_blq
    || (
    CASE
      WHEN t_dnn.dnn_por IS NOT NULL
      AND t_dnn.dnn_por  <> ' '
      THEN ' Portal. '
      ELSE ''
    END)
    || t_dnn.dnn_por
    || (
    CASE
      WHEN t_dnn.dnn_esc IS NOT NULL
      AND t_dnn.dnn_esc  <> ' '
      THEN ' Esc. '
      ELSE ''
    END)
    || t_dnn.dnn_esc
    || ' '
    || t_dnn.dnn_plt
    || (
    CASE
      WHEN t_dnn.dnn_pta IS NOT NULL
      AND t_dnn.dnn_pta  <> ' '
      THEN ' º '
      ELSE ''
    END)
    || t_dnn.dnn_pta AS domicilionn,
    &nom_esquema_generico..t_mun.mun_nom,
    t_hte.hte_tlf ,
    &nom_esquema_generico..t_prv.prv_nom,
    t_dnn.dnn_cpo
  FROM t_hte, 
	   t_ter,
       t_dom, 
       t_dnn, 
       &nom_esquema_generico..t_mun, 
       t_tvi, 
       t_via, 
       t_esi, 
       t_eco, 
       t_dot, 
       &nom_esquema_generico..t_prv 
 WHERE ter_dom = dot_dom (+) 
 and ter_cod=hte_ter(+)
 AND ter_cod       =dot_ter(+)
   and dot_dom = dom_cod (+) 
   and dom_cod = dnn_dom (+) 
   and t_dnn.dnn_pai   = t_mun.mun_pai(+)
  AND t_dnn.dnn_prv   = t_mun.mun_prv(+)
  AND t_dnn.dnn_mun   = t_mun.mun_cod(+)
  AND t_dnn.dnn_pai   = t_via.via_pai(+)
  AND t_dnn.dnn_prv   = t_via.via_prv(+)
  AND t_dnn.dnn_mun   = t_via.via_mun(+)
  AND t_dnn.dnn_via   = t_via.via_cod(+)
  AND t_via.via_tvi   = t_tvi.tvi_cod(+)
  AND t_dnn.dnn_pai   = t_prv.prv_pai(+)
  AND t_dnn.dnn_prv   = t_prv.prv_cod(+)
  AND t_dnn.dnn_pai   = t_esi.esi_pai(+)
  AND t_dnn.dnn_mun   = t_esi.esi_mun(+)
  AND t_dnn.dnn_prv   = t_esi.esi_prv(+)
  AND t_dnn.dnn_esi   = t_esi.esi_cod(+)
  AND t_esi.esi_pai   = t_eco.eco_pai(+)
  AND t_esi.esi_mun   = t_eco.eco_mun(+)
  AND t_esi.esi_prv   = t_eco.eco_prv(+)
  AND t_esi.esi_eco   = t_eco.eco_cod(+)
union
select HTE_TER, HTE_NVR,
    hte_nom
    || ' '
    ||t_hte.hte_pa1
    || ''
    || t_hte.hte_ap1
    || (
    CASE
      WHEN t_hte.hte_pa1 IS NOT NULL
      OR t_hte.hte_ap1   IS NOT NULL
      THEN ' '
      ELSE ''
    END)
    || t_hte.hte_pa2
    || ''
    || t_hte.hte_ap2 AS titular,
    hte_nom,
    hte_ap1,
    hte_ap2,
    t_hte.hte_doc,
     t_hte.hte_TID,
    t_tvi.tvi_des
    || ' '
    || t_via.via_nom
    || ' '
    || t_dsu.dsu_nud
    || ' '
    || t_dsu.dsu_led
    || ' '
    || t_dsu.dsu_nuh
    || t_dsu.dsu_leh
    || (
    CASE
      WHEN t_dsu.dsu_blq IS NOT NULL
      AND t_dsu.dsu_blq  <> ' '
      THEN ' Bl. '
      ELSE ''
    END)
    || t_dsu.dsu_blq
    || (
    CASE
      WHEN t_dsu.dsu_por IS NOT NULL
      AND t_dsu.dsu_por  <> ' '
      THEN ' Portal. '
      ELSE ''
    END)
    || t_dsu.dsu_por
    || (
    CASE
      WHEN t_dpo.dpo_esc IS NOT NULL
      AND t_dpo.dpo_esc  <> ' '
      THEN ' Esc. '
      ELSE ''
    END)
    || t_dpo.dpo_esc
    || ' '
    || t_dpo.dpo_plt
    || (
    CASE
      WHEN t_dpo.dpo_pta IS NOT NULL
      AND t_dpo.dpo_pta  <> ' '
      THEN ' º '
      ELSE ''
    END)
    || t_dpo.dpo_pta AS domicilio,
    NULL             AS domicilionn,
    t_mun.mun_nom,
    t_hte.hte_tlf ,
    &nom_esquema_generico..t_prv.prv_nom,
    t_dsu.dsu_cpo
  FROM t_hte,t_ter,
    t_dom,
    t_dsu,
    t_dpo,
    t_via,
    t_tvi,
    &nom_esquema_generico..t_mun,
    &nom_esquema_generico..t_prv,
    t_esi,
    t_eco
 WHERE ter_dom = dom_cod
 and ter_cod=hte_ter
 and ter_dom=dpo_dom
   and dpo_dsu=dsu_cod 
  AND t_dsu.dsu_pai   = t_via.via_pai
  AND t_dsu.dsu_prv   = t_via.via_prv
  AND t_dsu.dsu_mun   = t_via.via_mun
  AND t_dsu.dsu_via   = t_via.via_cod
  AND t_via.via_tvi   = t_tvi.tvi_cod
  AND t_dsu.dsu_pai   = t_mun.mun_pai(+)
  AND t_dsu.dsu_prv   = t_mun.mun_prv(+)
  AND t_dsu.dsu_mun   = t_mun.mun_cod(+)
  AND t_mun.mun_pai   = t_prv.prv_pai(+)
  AND t_mun.mun_prv   = t_prv.prv_cod(+)
  AND t_dsu.dsu_pai   = t_esi.esi_pai(+)
  AND t_dsu.dsu_prv   = t_esi.esi_prv(+)
  AND t_dsu.dsu_mun   = t_esi.esi_mun(+)
  AND t_dsu.dsu_esi   = t_esi.esi_cod(+)
  AND t_esi.esi_pai   = t_eco.eco_pai(+)
  AND t_esi.esi_mun   = t_eco.eco_mun(+)
  AND t_esi.esi_prv   = t_eco.eco_prv(+)
  AND t_esi.esi_eco   = t_eco.eco_cod(+);


  
CREATE OR REPLACE VIEW T_LOC (LOC_COT, LOC_TIP, LOC_COV, LOC_VIA) AS 
	SELECT tvi_cod loc_cot, tvi_des loc_tip, via_cod loc_cov, via_nom loc_via
	FROM t_tvi, t_via
	WHERE via_tvi = tvi_cod
;

 

CREATE OR REPLACE VIEW T_V01 (M1, L1, A1, A2, T1, T2, C1, C2, P1, P2, TP1, TP2, U1, U2, ID, MU) AS 
	SELECT p1.pro_mun m1, p2.pml_leng l1, a2.aml_cod a1, a2.aml_valor a2, t2.tml_tra t1, t2.tml_valor t2, c2.cml_cod c1, c2.cml_valor c2, p2.pml_cod p1, p2.pml_valor p2, tp2.tpml_cod tp1,	tp2.tpml_valor tp2, u2.uor_cod u1, u2.uor_nom u2, a2.aml_leng ID, p2.pml_mun mu 
	FROM &nom_esquema_generico..a_aml a2, e_tra t1, e_tml t2, &nom_esquema_generico..a_cls c1, &nom_esquema_generico..a_cml c2, e_pro p1, e_pml p2, &nom_esquema_generico..a_tpml tp2, a_uor u2
	WHERE p1.pro_mun = t1.tra_mun 
		AND p2.pml_mun = p1.pro_mun 
		AND p2.pml_leng = a2.aml_leng 
		AND t2.tml_leng = p2.pml_leng 
		AND c2.cml_leng = p2.pml_leng 
		AND tp2.tpml_leng = p2.pml_leng 
		AND p1.pro_are = a2.aml_cod 
		AND t1.tra_mun = t2.tml_mun 
		AND t1.tra_pro = t2.tml_pro 
		AND t1.tra_cod = t2.tml_tra 
		AND t1.tra_cls = c1.cls_cod 
		AND c1.cls_cod = c2.cml_cod 
		AND p1.pro_cod = t1.tra_pro 
		AND p1.pro_mun = p2.pml_mun 
		AND p1.pro_cod = p2.pml_cod 
		AND p1.pro_tip = tp2.tpml_cod 
		AND t1.tra_utr = u2.uor_cod(+)
;
 
    

CREATE OR REPLACE FORCE VIEW TAREAS ("CRO_MUN", "CRO_PRO", "CRO_EJE", "CRO_NUM", "CRO_FEI", "CRO_FEF", "EXP_FEI", "EXP_FEF", "PRO_TIP", "TRA_COD", "TRA_COU", "PRO_ARE", "TRA_CLS", "CRO_OCU", "CRO_UTR")
AS
  SELECT cro_mun,
    cro_pro,
    cro_eje,
    cro_num,
    cro_fei,
    cro_fef,
    exp_fei,
    (
    CASE
      WHEN exp_fef IS NOT NULL
      AND exp_num   = cro_num
      THEN exp_fef
      ELSE NULL
    END) AS exp_fef,
    pro_tip,
    tra_cod,
    tra_cou,
    pro_are,
    tra_cls,
    cro_ocu,
    cro_utr
  FROM e_cro,
    e_pro,
    e_exp,
    e_tra
  WHERE cro_mun     = exp_mun
  AND cro_pro       = exp_pro
  AND cro_eje       = exp_eje
  AND cro_num       = exp_num
  AND pro_mun       = exp_mun
  AND pro_cod       = exp_pro
  AND e_tra.tra_mun = e_cro.cro_mun
  AND e_tra.tra_pro = e_cro.cro_pro
  AND e_tra.tra_cod = e_cro.cro_tra
  UNION
  SELECT exp_mun AS cro_mun,
    exp_pro      AS cro_pro,
    exp_eje      AS cro_eje,
    exp_num      AS cro_num,
    exp_pend     AS cro_fei,
    TO_DATE (NULL) cro_fef,
    exp_fei,
    exp_fef,
    pro_tip,
    999 tra_cod,
    999 tra_cou,
    pro_are,
    999 tra_cls,
    1 cro_ocu,
    999 cro_utr
  FROM e_exp,
    e_pro,
    &nom_esquema_generico..a_tpr
  WHERE exp_mun = pro_mun
  AND exp_pro   = pro_cod
  AND pro_tip   = tpr_cod
  AND exp_pend IS NOT NULL;


      
CREATE OR REPLACE FORCE VIEW "V_CRO" ("CRO_OCU", "PRO_COD", "PRO_NOM", "CRO_EJE", "CRO_NUM", "TRA_COD", "TRA_NOM", "CRO_FEI", "CRO_FEF", "USU_COD", "USU_NOM", "UTR_COD", "UTR_NOM", "CRO_FIP", "CRO_FLI", "CRO_FFP", "CRO_OBS", "CRO_LENG", "CRO_LOC", "CRO_AYT", "CRO_DAC", "CRO_MAC", "CRO_AAC", "EXP_EST", "EXP_ASU", "DNN_RCA", "PRV_NOM", "ESI_NOM", "ECO_NOM", "EXP_OBS","COD_ORG")
AS
  SELECT E_CRO.CRO_OCU,
    E_CRO.CRO_PRO,
    E_PML.PML_VALOR,
    E_CRO.CRO_EJE,
    E_CRO.CRO_NUM,
    E_CRO.CRO_TRA,
    E_TML.TML_VALOR,
    TO_CHAR(E_CRO.CRO_FEI,'DD/MM/YYYY') AS CRO_FEI,
    TO_CHAR(E_CRO.CRO_FEF,'DD/MM/YYYY') AS CRO_FEF,
    E_CRO.CRO_USU,
    A_USU.USU_LOG,
    E_CRO.CRO_UTR,
    A_UOR.UOR_NOM,
    TO_CHAR(E_CRO.CRO_FIP, 'DD/MM/YYYY') AS CRO_FIP,
    TO_CHAR(E_CRO.CRO_FLI, 'DD/MM/YYYY') AS CRO_FLI,
    TO_CHAR(E_CRO.CRO_FFP, 'DD/MM/YYYY') AS CRO_FFP,
    E_CRO.CRO_OBS,
    E_PML.PML_LENG,
    E_EXP.EXP_LOC,
    T_MUN.MUN_NOM,
    TO_CHAR(SYSDATE, 'DD')   AS CRO_DAC,
    TO_CHAR(SYSDATE, 'MM')   AS CRO_MAC,
    TO_CHAR(SYSDATE, 'YYYY') AS CRO_AAC,
    E_EXP.EXP_EST,
    E_EXP.EXP_ASU,
    T_DNN.DNN_RCA,
    T_PRV.PRV_NOM,
    T_ESI.ESI_NOM,
    T_ECO.ECO_NOM,
    E_EXP.EXP_OBS,
	E_CRO.CRO_MUN	
  FROM E_CRO,
    E_TML,
    E_PML,
    &nom_esquema_generico..A_USU,
    A_UOR,
    E_EXP,
    &nom_esquema_generico..T_MUN,
    T_DNN,
    &nom_esquema_generico..T_PRV,
    T_ESI,
    T_ECO
  WHERE E_CRO.CRO_MUN=E_PML.PML_MUN
  AND E_CRO.CRO_PRO  =E_PML.PML_COD
  AND E_PML.PML_CMP  ='NOM'
  AND E_CRO.CRO_MUN  =E_TML.TML_MUN
  AND E_CRO.CRO_PRO  =E_TML.TML_PRO
  AND E_CRO.CRO_TRA  =E_TML.TML_TRA
  AND E_TML.TML_CMP  ='NOM'
  AND E_CRO.CRO_USU  =A_USU.USU_COD
  AND E_CRO.CRO_UTR  =A_UOR.UOR_COD
  AND E_PML.PML_LENG =E_TML.TML_LENG
  AND E_CRO.CRO_MUN  =E_EXP.EXP_MUN(+)
  AND E_CRO.CRO_PRO  =E_EXP.EXP_PRO(+)
  AND E_CRO.CRO_NUM  =E_EXP.EXP_NUM(+)
  AND E_CRO.CRO_MUN  =T_MUN.MUN_COD
  AND T_MUN.MUN_PAI  = &cod_pais
  AND T_MUN.MUN_PRV  = &cod_municipio
  AND E_EXP.EXP_CLO  = T_DNN.DNN_DOM(+)
  AND T_DNN.DNN_PAI  = T_PRV.PRV_PAI(+)
  AND T_DNN.DNN_PRV  = T_PRV.PRV_COD(+)
  AND T_DNN.DNN_PAI  = T_ESI.ESI_PAI(+)
  AND T_DNN.DNN_MUN  = T_ESI.ESI_MUN(+)
  AND T_DNN.DNN_PRV  = T_ESI.ESI_PRV(+)
  AND T_DNN.DNN_ESI  = T_ESI.ESI_COD(+)
  AND T_ESI.ESI_PAI  = T_ECO.ECO_PAI(+)
  AND T_ESI.ESI_MUN  = T_ECO.ECO_MUN(+)
  AND T_ESI.ESI_PRV  = T_ECO.ECO_PRV(+)
  AND T_ESI.ESI_ECO  = T_ECO.ECO_COD(+);
  

CREATE OR REPLACE VIEW V_CROI (CRO_OCU, PRO_COD, PRO_NOM, CRO_EJE, CRO_NUM, TRA_COD, TRA_NOM, CRO_FEI, CRO_FEF, USU_COD, USU_NOM, UTR_COD, UTR_NOM, CRO_FIP, CRO_FLI, CRO_FFP, CRO_OBS, CRO_LENG, CRO_INT, CRO_DOC, CRO_DOM, CRO_PBD, CRO_ROL, CRO_LOC, CRO_AYT, CRO_NOR, CRO_DOMNN, CRO_PBDNN, CRO_CIN, CRO_NVI, CRO_DAC, CRO_MAC, CRO_AAC, EXP_ASU, DNN_RCA, PRV_NOM, ESI_NOM, ECO_NOM, DNN_CPO, EXP_OBS, CRO_DCE) AS 
	SELECT e_cro.cro_ocu, e_cro.cro_pro, e_pml.pml_valor, e_cro.cro_eje, e_cro.cro_num, e_cro.cro_tra, e_tml.tml_valor, TO_CHAR (e_cro.cro_fei, 'DD/MM/YYYY') AS cro_fei, TO_CHAR (e_cro.cro_fef, 'DD/MM/YYYY') AS cro_fef, e_cro.cro_usu, a_usu.usu_log, e_cro.cro_utr, a_uor.uor_nom, TO_CHAR (e_cro.cro_fip, 'DD/MM/YYYY') AS cro_fip, TO_CHAR (e_cro.cro_fli, 'DD/MM/YYYY') AS cro_fli, TO_CHAR (e_cro.cro_ffp, 'DD/MM/YYYY') AS cro_ffp, e_cro.cro_obs, e_pml.pml_leng, hte_nom || ' ' ||t_hte.hte_pa1 || '' || t_hte.hte_ap1 || (CASE WHEN t_hte.hte_pa1 IS NOT NULL OR t_hte.hte_ap1 IS NOT NULL THEN ' ' ELSE '' END) || t_hte.hte_pa2 || '' || t_hte.hte_ap2 AS titular, t_hte.hte_doc, (CASE WHEN t_via.via_nom IS NOT NULL THEN t_tvi.tvi_des ELSE '' END)	|| ' ' || t_via.via_nom || ' ' || t_dsu.dsu_nud || ' ' || t_dsu.dsu_led || ' ' || t_dsu.dsu_nuh || t_dsu.dsu_leh || (CASE WHEN t_dsu.dsu_blq IS NOT NULL AND t_dsu.dsu_blq <> ' ' THEN ' Bl. ' ELSE '' END) || t_dsu.dsu_blq || (CASE WHEN t_dsu.dsu_por IS NOT NULL AND t_dsu.dsu_por <> ' ' THEN ' Portal. ' ELSE '' END) || t_dsu.dsu_por || (CASE WHEN t_dpo.dpo_esc IS NOT NULL AND t_dpo.dpo_esc <> ' ' THEN ' Esc. ' ELSE '' END ) || t_dpo.dpo_esc || t_dpo.dpo_plt || (CASE WHEN t_dpo.dpo_pta IS NOT NULL AND t_dpo.dpo_pta <> ' ' THEN ' ? ' ELSE '' END) || t_dpo.dpo_pta AS domicilio, mun1.mun_nom AS poblacion, e_rol.rol_des, e_exp.exp_loc, mun2.mun_nom AS ayuntamiento, t_dom.dom_nml, t_tvi2.tvi_des || ' ' || t_via2.via_nom || ' ' || t_dnn.dnn_dmc || ' ' || t_dnn.dnn_nud || ' ' || t_dnn.dnn_led || ' ' || t_dnn.dnn_nuh || t_dnn.dnn_leh || (CASE WHEN t_dnn.dnn_blq IS NOT NULL AND t_dnn.dnn_blq <> ' ' THEN ' Bl. ' ELSE '' END) || t_dnn.dnn_blq || (CASE WHEN t_dnn.dnn_por IS NOT NULL AND t_dnn.dnn_por <> ' ' THEN ' Portal. ' ELSE '' END) || t_dnn.dnn_por || (CASE WHEN t_dnn.dnn_esc IS NOT NULL AND t_dnn.dnn_esc <> ' ' THEN ' Esc. ' ELSE '' END) || t_dnn.dnn_esc || t_dnn.dnn_plt || (CASE WHEN t_dnn.dnn_pta IS NOT NULL AND t_dnn.dnn_pta <> ' ' THEN ' ? ' ELSE '' END) || t_dnn.dnn_pta AS domicilionn, mun3.mun_nom AS poblacionnn, e_ext.ext_ter, e_ext.ext_nvr, TO_CHAR (SYSDATE, 'DD') AS cro_dac, TO_CHAR (SYSDATE, 'MM') AS cro_mac, TO_CHAR (SYSDATE, 'YYYY') AS cro_aac, e_exp.exp_asu, t_dnn2.dnn_rca, t_prv.prv_nom, t_esi.esi_nom, t_eco.eco_nom, t_dnn.dnn_cpo, E_EXP.EXP_OBS, hte_dce
	FROM e_cro, e_tml, e_pml, &nom_esquema_generico..a_usu, a_uor, e_ext, t_hte, t_dom, t_dsu, t_dpo, t_via, t_via t_via2, t_tvi, t_tvi t_tvi2, &nom_esquema_generico..t_mun mun1, e_rol, e_exp, &nom_esquema_generico..t_mun mun2, t_dnn, t_dnn t_dnn2, &nom_esquema_generico..t_prv, t_esi, t_eco, &nom_esquema_generico..t_mun mun3
	WHERE e_cro.cro_mun = e_pml.pml_mun 
		AND e_cro.cro_pro = e_pml.pml_cod 
		AND e_pml.pml_cmp = 'NOM' 
		AND e_cro.cro_mun = e_tml.tml_mun 
		AND e_cro.cro_pro = e_tml.tml_pro 
		AND e_cro.cro_tra = e_tml.tml_tra 
		AND e_tml.tml_cmp = 'NOM' 
		AND e_cro.cro_usu = a_usu.usu_cod 
		AND e_cro.cro_utr = a_uor.uor_cod 
		AND e_pml.pml_leng = e_tml.tml_leng 
		AND e_cro.cro_mun = e_ext.ext_mun(+) 
		AND e_cro.cro_eje = e_ext.ext_eje(+) 
		AND e_cro.cro_num = e_ext.ext_num(+) 
		AND e_ext.ext_ter = t_hte.hte_ter(+) 
		AND e_ext.ext_nvr = t_hte.hte_nvr(+) 
		AND e_ext.ext_dot = t_dom.dom_cod(+) 
		AND e_ext.ext_dot = t_dpo.dpo_dom(+) 
		AND t_dpo.dpo_dsu = t_dsu.dsu_cod(+) 
		AND T_DSU.DSU_PAI=MUN1.MUN_PAI(+) 
		AND T_DSU.DSU_PRV=MUN1.MUN_PRV(+) 
		AND T_DSU.DSU_MUN=MUN1.MUN_COD(+) 
		AND t_dsu.dsu_pai = t_via.via_pai(+) 
		AND t_dsu.dsu_prv = t_via.via_prv(+) 
		AND t_dsu.dsu_mun = t_via.via_mun(+) 
		AND t_dsu.dsu_via = t_via.via_cod(+) 
		AND t_via.via_tvi = t_tvi.tvi_cod(+) 
		AND t_dnn.dnn_pai = mun3.mun_pai(+) 
		AND t_dnn.dnn_prv = mun3.mun_prv(+) 
		AND t_dnn.dnn_mun = mun3.mun_cod(+) 
		AND t_dnn.dnn_pai = t_via2.via_pai(+) 
		AND t_dnn.dnn_prv = t_via2.via_prv(+) 
		AND t_dnn.dnn_mun = t_via2.via_mun(+) 
		AND t_dnn.dnn_via = t_via2.via_cod(+) 
		AND t_dnn.dnn_tvi = t_tvi2.tvi_cod(+) 
		AND e_cro.cro_mun = e_exp.exp_mun(+) 
		AND e_cro.cro_pro = e_exp.exp_pro(+) 
		AND e_cro.cro_num = e_exp.exp_num(+) 
		AND e_cro.cro_mun = mun2.mun_cod 
		AND mun2.mun_pai = &cod_pais
		AND mun2.mun_prv = &cod_municipio
		AND e_ext.ext_pro = e_rol.rol_pro(+) 
		AND e_ext.ext_rol = e_rol.rol_cod(+) 
		AND e_ext.ext_dot = t_dnn.dnn_dom(+) 
		AND t_dnn.dnn_pai = t_prv.prv_pai(+) 
		AND t_dnn.dnn_prv = t_prv.prv_cod(+) 
		AND t_dnn.dnn_pai = t_esi.esi_pai(+) 
		AND t_dnn.dnn_mun = t_esi.esi_mun(+) 
		AND t_dnn.dnn_prv = t_esi.esi_prv(+) 
		AND t_dnn.dnn_esi = t_esi.esi_cod(+) 
		AND t_esi.esi_pai = t_eco.eco_pai(+) 
		AND t_esi.esi_mun = t_eco.eco_mun(+) 
		AND t_esi.esi_prv = t_eco.eco_prv(+) 
		AND t_esi.esi_eco = t_eco.eco_cod(+) 
		AND e_exp.exp_clo = t_dnn2.dnn_dom(+)
	ORDER BY e_rol.rol_cod ASC
;


CREATE OR REPLACE VIEW V_ENT1 (AP1, AP2, NOM, DAT) AS 
	SELECT res_asu, res_tip, res_dil, res_fec
	FROM r_res
;



CREATE OR REPLACE VIEW V_EXP (EXP_NUM, EXP_EJE, EXP_FEI, EXP_FEF, EXP_EST, EXP_USU, EXP_NUSU, EXP_UOR, EXP_NUOR, EXP_OBS, EXP_ASU, EXP_PRO, EXP_NPRO) AS 
	SELECT e_exp.exp_num, e_exp.exp_eje, TO_CHAR (e_exp.exp_fei, 'DD/MM/YYYY') AS exp_fei, TO_CHAR (e_exp.exp_fef, 'DD/MM/YYYY') AS exp_fef, (CASE WHEN e_exp.exp_est = 0 THEN 'PENDENTE' ELSE 'PECHADO' END), e_exp.exp_usu, a_usu.usu_nom, e_exp.exp_uor, a_uor.uor_nom, e_exp.exp_obs, e_exp.exp_asu, e_exp.exp_pro, e_pml.pml_valor
	FROM e_exp, &nom_esquema_generico..a_usu, a_uor, e_pro, e_pml
	WHERE e_exp.exp_usu = a_usu.usu_cod 
		AND e_exp.exp_uor = a_uor.uor_cod 	
		AND e_exp.exp_mun = e_pro.pro_mun 
		AND e_exp.exp_pro = e_pro.pro_cod 
		AND e_pro.pro_cod = e_pml.pml_cod 
		AND e_pml.pml_leng = &idiomaDefecto
		AND e_pml.pml_cmp = 'NOM'
;
	 
	 
DEFINE nom_esquema_generico = FLEXIA_GENERICO;
DEFINE cod_pais = codPais;
DEFINE cod_municipio = codMunicipio;

CREATE OR REPLACE FORCE VIEW V_EXP_INF 
(
  EXP_LOC, 
  EXP_FEI,
  EXP_FEF, 
  EXP_ASU, 
  EXP_NUM, 
  EXP_EJE,
  EXP_USU_COD,
  EXP_USU_NOM,
  EXP_USU_LOG, 
  EXP_UOR_COD,
  EXP_UOR_NOM,
  EXP_OBS,
  EXP_PRO_COD,
  EXP_PRO_NOM,
  TRA_FEI, 
  TRA_FEF, 
  TRA_NOM, 
  TRA_COD,
  TRA_OBS,
  TRA_UOR_COD,
  TRA_UOR_NOM, 
  TRA_COU
) 
AS

  SELECT e_exp.exp_loc, 
  e_exp.exp_fei, 
  e_exp.exp_fef,
  e_exp.exp_asu,
  e_exp.exp_num,
  e_exp.exp_eje,
  a_usu.usu_cod,
  a_usu.usu_nom,
  a_usu.usu_log,
  a1.uor_cod_vis,
  a1.uor_nom, 
  e_exp.exp_obs,
  e_exp.exp_pro,
  e_pml.pml_valor,
  e_cro.cro_fei,
  e_cro.cro_fef,
  e_tml.tml_valor,
  e_tra.tra_cod,
  e_cro.cro_obs,
  a2.uor_cod_vis,
  a2.uor_nom,
  e_tra.tra_cou 
  FROM e_exp, 
  e_cro, 
  e_tml,
  e_pml, 
  &nom_esquema_generico..A_USU,
  a_uor a1,
  a_uor a2,
  &nom_esquema_generico..t_mun,
  e_tra 
  WHERE e_exp.exp_mun = e_pml.pml_mun
  AND e_exp.exp_pro = e_pml.pml_cod
  AND e_pml.pml_cmp = 'NOM'
  AND e_cro.cro_mun = e_tml.tml_mun
  AND e_cro.cro_pro = e_tml.tml_pro
  AND e_cro.cro_tra = e_tml.tml_tra
  AND e_tml.tml_cmp = 'NOM'
  AND e_exp.exp_usu = a_usu.usu_cod
  AND e_exp.exp_uor = a1.uor_cod
  AND e_pml.pml_leng = e_tml.tml_leng
  AND e_cro.cro_mun = e_exp.exp_mun(+)
  AND e_cro.cro_pro = e_exp.exp_pro(+)
  AND e_cro.cro_num = e_exp.exp_num(+)
  AND e_cro.cro_mun = t_mun.mun_cod
  AND e_cro.cro_utr = a2.uor_cod
  AND t_mun.mun_pai = &cod_pais
  AND e_cro.cro_tra = e_tra.tra_cod(+) 
  AND e_cro.cro_pro = e_tra.tra_pro
  AND t_mun.mun_prv = &cod_municipio
;	 

  
CREATE OR REPLACE VIEW V_INT (INT_DCE,INT_MUN, INT_EJE, INT_NUM, INT_TER, INT_NVR, INT_CDT, INT_NOR, INT_NOC, INT_NOM, INT_AP1, INT_AP2, INT_TID,INT_DOC, INT_DOMN, INT_DOMNN, INT_ROL, INT_RPD, INT_POB, INT_TLF, INT_RCA, INT_PRV, INT_CPO, INT_CODEXTERNO) AS
SELECT hte_dce,e_ext.ext_mun, e_ext.ext_eje, e_ext.ext_num, e_ext.ext_ter, e_ext.ext_nvr, e_ext.ext_dot, t_dom.dom_nml, hte_nom || ' ' ||t_hte.hte_pa1 || '' || t_hte.hte_ap1 || (CASE WHEN t_hte.hte_pa1 IS NOT NULL OR t_hte.hte_ap1 IS NOT NULL THEN ' ' ELSE '' END) || t_hte.hte_pa2 || '' || t_hte.hte_ap2 AS titular, hte_nom, hte_ap1, hte_ap2,t_tid.tid_des, t_hte.hte_doc, t_tvi.tvi_des || ' ' || t_via.via_nom || ' ' || t_dsu.dsu_nud || ' ' || t_dsu.dsu_led || ' ' || t_dsu.dsu_nuh || t_dsu.dsu_leh || (CASE WHEN t_dsu.dsu_blq IS NOT NULL AND t_dsu.dsu_blq <> ' ' THEN ' Bl. ' ELSE '' END) || t_dsu.dsu_blq || (CASE WHEN t_dsu.dsu_por IS NOT NULL AND t_dsu.dsu_por <> ' ' THEN ' Portal. ' ELSE '' END) || t_dsu.dsu_por || (CASE WHEN t_dpo.dpo_esc IS NOT NULL AND t_dpo.dpo_esc <> ' ' THEN ' Esc. ' ELSE '' END) || t_dpo.dpo_esc || ' ' || t_dpo.dpo_plt || (CASE WHEN t_dpo.dpo_pta IS NOT NULL AND t_dpo.dpo_pta <> ' ' THEN ' º ' ELSE '' END) || t_dpo.dpo_pta AS domicilio, NULL AS domicilionn, e_rol.rol_des, e_rol.rol_pde, v_mpp.mun_nom, t_hte.hte_tlf ,null as dnn_rca, &nom_esquema_generico..v_mpp.prv_nom, t_dsu.dsu_cpo,t_ter.EXTERNAL_CODE
	FROM e_ext, t_hte,t_tid, t_dom, t_dsu, t_dpo, t_via, t_tvi, e_rol, &nom_esquema_generico..v_mpp, t_ter
	WHERE e_ext.ext_ter = t_hte.hte_ter(+)
		AND e_ext.ext_nvr = t_hte.hte_nvr(+)
    AND t_hte.hte_tid= t_tid.tid_cod
		AND e_ext.ext_dot = t_dom.dom_cod
		AND e_ext.ext_dot = t_dpo.dpo_dom
		AND t_dpo.dpo_dsu = t_dsu.dsu_cod
		AND t_dsu.dsu_pai = t_via.via_pai
		AND t_dsu.dsu_prv = t_via.via_prv
		AND t_dsu.dsu_mun = t_via.via_mun
		AND t_dsu.dsu_via = t_via.via_cod
		AND t_via.via_tvi = t_tvi.tvi_cod
		AND e_ext.ext_pro = e_rol.rol_pro(+)
		AND e_ext.ext_rol = e_rol.rol_cod(+)
		AND t_dsu.dsu_pai = v_mpp.pai_cod(+)
		AND t_dsu.dsu_prv = v_mpp.prv_cod(+)
		AND t_dsu.dsu_mun = v_mpp.mun_cod(+)
		AND t_ter.ter_cod = e_ext.ext_ter
union
SELECT hte_dce,e_ext.ext_mun, e_ext.ext_eje, e_ext.ext_num, e_ext.ext_ter, e_ext.ext_nvr, e_ext.ext_dot, t_dom.dom_nml, hte_nom || ' ' ||t_hte.hte_pa1 || '' || t_hte.hte_ap1 || (CASE WHEN t_hte.hte_pa1 IS NOT NULL OR t_hte.hte_ap1 IS NOT NULL THEN ' ' ELSE '' END) || t_hte.hte_pa2 || '' || t_hte.hte_ap2 AS titular, hte_nom, hte_ap1, hte_ap2,t_tid.tid_des, t_hte.hte_doc, NULL AS domicilio, t_tvi.tvi_des || ' ' || t_via.via_nom || ' ' || t_dnn.dnn_dmc || ' ' || t_dnn.dnn_nud || ' ' || t_dnn.dnn_led || ' ' || t_dnn.dnn_nuh || t_dnn.dnn_leh || (CASE WHEN t_dnn.dnn_blq IS NOT NULL AND t_dnn.dnn_blq <> ' ' THEN ' Bl. ' ELSE '' END) || t_dnn.dnn_blq || (CASE WHEN t_dnn.dnn_por IS NOT NULL AND t_dnn.dnn_por <> ' ' THEN ' Portal. ' ELSE '' END) || t_dnn.dnn_por || (CASE WHEN t_dnn.dnn_esc IS NOT NULL AND t_dnn.dnn_esc <> ' ' THEN ' Esc. ' ELSE '' END) || t_dnn.dnn_esc || ' ' || t_dnn.dnn_plt || (CASE WHEN t_dnn.dnn_pta IS NOT NULL AND t_dnn.dnn_pta <> ' ' THEN ' º ' ELSE '' END) || t_dnn.dnn_pta AS domicilionn, e_rol.rol_des, e_rol.rol_pde, &nom_esquema_generico..v_mpp.mun_nom, t_hte.hte_tlf ,t_dnn.dnn_rca, &nom_esquema_generico..v_mpp.prv_nom, t_dnn.dnn_cpo,t_ter.EXTERNAL_CODE
	FROM e_ext, t_hte,t_tid, t_dom, t_dnn, e_rol, &nom_esquema_generico..v_mpp, t_tvi, t_via,t_ter
	WHERE e_ext.ext_ter = t_hte.hte_ter(+)
		AND e_ext.ext_nvr = t_hte.hte_nvr(+)
    AND t_hte.hte_tid= t_tid.tid_cod
		AND e_ext.ext_dot = t_dom.dom_cod
		AND e_ext.ext_dot = dnn_dom
		AND e_ext.ext_pro = e_rol.rol_pro(+)
		AND e_ext.ext_rol = e_rol.rol_cod(+)
		AND t_dnn.dnn_pai = v_mpp.pai_cod(+)
		AND t_dnn.dnn_prv = v_mpp.prv_cod(+)
		AND t_dnn.dnn_mun = v_mpp.mun_cod(+)
		AND t_dnn.dnn_pai = t_via.via_pai(+)
		AND t_dnn.dnn_prv = t_via.via_prv(+)
		AND t_dnn.dnn_mun = t_via.via_mun(+)
		AND t_dnn.dnn_via = t_via.via_cod(+)
		AND t_via.via_tvi = t_tvi.tvi_cod(+)
		AND t_ter.ter_cod = e_ext.ext_ter
	ORDER BY 13
; 


  

CREATE OR REPLACE VIEW V_REG (REG_DEP, REG_UOR, REG_TIP, REG_EJE, REG_NUM, REG_EST, REG_FEC, REG_FED, REG_ASU, REG_INT, REG_DOM, REG_CTD, REG_DTD, REG_MOD, REG_CTR, REG_DTR, REG_CTT, REG_DTT, REG_MUN, REG_DEST, REG_CAC, REG_DAC, REG_DIL) AS 
	SELECT DISTINCT res_dep, res_uor, res_tip, res_eje, res_num, (CASE WHEN res_est = 0 THEN 'PENDIENTE' WHEN res_est = 1 THEN 'ACEPTADA' WHEN res_est = 2 THEN 'RECHAZADA' WHEN res_est = 9 THEN 'ANULADA' ELSE NULL END) AS res_est, TO_CHAR (res_fec, 'DD/MM/YYYY HH24:MI') AS fechaentrada, TO_CHAR (res_fed, 'DD/MM/YYYY HH24:MI') AS fechadocum, res_asu, ((CASE WHEN hte_pa1 IS NULL OR hte_pa1 = ' ' THEN '' ELSE hte_pa1 || ' ' END) || (CASE WHEN hte_ap1 IS NULL OR hte_ap1 = ' ' THEN '' ELSE hte_ap1 || ' ' END) || (CASE WHEN hte_pa2 IS NULL OR hte_pa2 = ' ' THEN '' ELSE hte_pa2 || ' ' END) || (CASE WHEN hte_ap2 IS NULL OR hte_ap2 = ' ' THEN '' ELSE hte_ap2 END) || ',' || hte_nom ) AS interesado, t_dnn.dnn_dmc || ' ' || t_dnn.dnn_nud || ' ' || t_dnn.dnn_led || ' ' || t_dnn.dnn_nuh || t_dnn.dnn_leh || (CASE WHEN t_dnn.dnn_blq IS NOT NULL AND t_dnn.dnn_blq <> ' ' THEN ' Bl. ' ELSE '' END) || t_dnn.dnn_blq || (CASE WHEN t_dnn.dnn_por IS NOT NULL AND t_dnn.dnn_por <> ' ' THEN ' Portal. ' ELSE '' END) || t_dnn.dnn_por || (CASE WHEN t_dnn.dnn_esc IS NOT NULL AND t_dnn.dnn_esc <> ' ' THEN ' Esc. ' ELSE '' END) || t_dnn.dnn_esc || t_dnn.dnn_plt || (CASE WHEN t_dnn.dnn_pta IS NOT NULL AND t_dnn.dnn_pta <> ' ' THEN ' ? ' ELSE '' END ) || t_dnn.dnn_pta AS domicilio, tdo_cod, tdo_des, 'ORDINARIA' AS tipoent, tpe_cod, tpe_des, ttr_cod, ttr_des, mun_nom, uor_nom AS destino, act_cod, act_des, dil_txt 
	FROM r_res, t_hte, t_dnn, &nom_esquema_generico..t_mun, a_uor, r_tdo, r_tpe, r_ttr, r_act, r_dil
	WHERE res_ter = hte_ter
		AND res_dom = dnn_dom
		AND res_tnv = hte_nvr
		AND dnn_pai = mun_pai
		AND dnn_prv = mun_prv
		AND dnn_mun = mun_cod
		AND res_mod = 0
		AND res_uod = uor_cod(+)
		AND res_tdo = tdo_ide
		AND res_tpe = tpe_ide
		AND res_ttr = ttr_ide(+)
		AND res_act = act_ide(+)
		AND res_dep = dil_dep(+)
		AND res_uor = dil_uor(+)
		AND res_tip = dil_tir(+)
		AND res_fec = dil_fec(+)
	UNION SELECT DISTINCT res_dep, res_uor, res_tip, res_eje, res_num, (CASE WHEN res_est = 0 THEN 'PENDIENTE' WHEN res_est = 1 THEN 'ACEPTADA' WHEN res_est = 2 THEN 'RECHAZADA' WHEN res_est = 9 THEN 'ANULADA' ELSE NULL END) AS res_est, TO_CHAR (res_fec, 'DD/MM/YYYY HH24:MI') AS fechaentrada, TO_CHAR (res_fed, 'DD/MM/YYYY HH24:MI') AS fechadocum, res_asu, ((CASE WHEN hte_pa1 IS NULL OR hte_pa1 = ' ' THEN '' ELSE hte_pa1 || ' ' END) || (CASE WHEN hte_ap1 IS NULL OR hte_ap1 = ' ' THEN '' ELSE hte_ap1 || ' ' END) || (CASE WHEN hte_pa2 IS NULL OR hte_pa2 = ' ' THEN '' ELSE hte_pa2 || ' ' END) || (CASE WHEN hte_ap2 IS NULL OR hte_ap2 = ' ' THEN '' ELSE hte_ap2 END) || ',' || hte_nom ) AS interesado, t_tvi.tvi_des || ' ' || t_via.via_nom || ' ' || t_dsu.dsu_nud || ' ' || t_dsu.dsu_led || ' ' || t_dsu.dsu_nuh || t_dsu.dsu_leh || (CASE WHEN t_dsu.dsu_blq IS NOT NULL AND t_dsu.dsu_blq <> ' ' THEN ' Bl. ' ELSE '' END) || t_dsu.dsu_blq || (CASE WHEN t_dsu.dsu_por IS NOT NULL AND t_dsu.dsu_por <> ' ' THEN ' Portal. ' ELSE '' END) || t_dsu.dsu_por || (CASE WHEN t_dpo.dpo_esc IS NOT NULL AND t_dpo.dpo_esc <> ' ' THEN ' Esc. ' ELSE '' END) || t_dpo.dpo_esc || t_dpo.dpo_plt || (CASE WHEN t_dpo.dpo_pta IS NOT NULL AND t_dpo.dpo_pta <> ' ' THEN ' ? ' ELSE '' END) || t_dpo.dpo_pta AS domicilio, tdo_cod, tdo_des, 'ORDINARIA' AS tipoent, tpe_cod, tpe_des, ttr_cod, ttr_des, mun_nom, uor_nom AS destino, act_cod, act_des, dil_txt
	FROM r_res, t_hte, t_dpo, t_dsu, t_tvi, t_via, &nom_esquema_generico..t_mun, a_uor, r_tdo, r_tpe, r_ttr, r_act, r_dil
	WHERE res_ter = hte_ter
		AND res_dom = dpo_dom
		AND res_tnv = hte_nvr
		AND dpo_dsu = dsu_cod
		AND dsu_pai = mun_pai
		AND dsu_prv = mun_prv
		AND dsu_mun = mun_cod
		AND t_dsu.dsu_pai = t_via.via_pai(+)
		AND t_dsu.dsu_prv = t_via.via_prv(+)
		AND t_dsu.dsu_mun = t_via.via_mun(+)
		AND t_dsu.dsu_via = t_via.via_cod(+)
		AND t_via.via_tvi = t_tvi.tvi_cod(+)
		AND res_mod = 0
		AND res_uod = uor_cod(+)
		AND res_tdo = tdo_ide
		AND res_tpe = tpe_ide
		AND res_ttr = ttr_ide(+)
		AND res_act = act_ide(+)
		AND res_dep = dil_dep(+)
		AND res_uor = dil_uor(+)
		AND res_tip = dil_tir(+)
		AND res_fec = dil_fec(+)
	UNION SELECT DISTINCT res_dep, res_uor, res_tip, res_eje, res_num, (CASE WHEN res_est = 0 THEN 'PENDIENTE' WHEN res_est = 1 THEN 'ACEPTADA' WHEN res_est = 2 THEN 'RECHAZADA' WHEN res_est = 9 THEN 'ANULADA' ELSE NULL END) AS res_est, TO_CHAR (res_fec, 'DD/MM/YYYY HH24:MI') AS fechaentrada, TO_CHAR (res_fed, 'DD/MM/YYYY HH24:MI') AS fechadocum, res_asu, ((CASE WHEN hte_pa1 IS NULL OR hte_pa1 = ' ' THEN '' ELSE hte_pa1 || ' ' END) || (CASE WHEN hte_ap1 IS NULL OR hte_ap1 = ' ' THEN '' ELSE hte_ap1 || ' ' END) || (CASE WHEN hte_pa2 IS NULL OR hte_pa2 = ' ' THEN '' ELSE hte_pa2 || ' ' END) || (CASE WHEN hte_ap2 IS NULL OR hte_ap2 = ' ' THEN '' ELSE hte_ap2 END) || ',' || hte_nom ) AS interesado, t_dnn.dnn_dmc || ' ' || t_dnn.dnn_nud || ' ' || t_dnn.dnn_led || ' ' || t_dnn.dnn_nuh || t_dnn.dnn_leh || (CASE WHEN t_dnn.dnn_blq IS NOT NULL AND t_dnn.dnn_blq <> ' ' THEN ' Bl. ' ELSE '' END) || t_dnn.dnn_blq || (CASE WHEN t_dnn.dnn_por IS NOT NULL AND t_dnn.dnn_por <> ' ' THEN ' Portal. ' ELSE '' END) || t_dnn.dnn_por || (CASE WHEN t_dnn.dnn_esc IS NOT NULL AND t_dnn.dnn_esc <> ' ' THEN ' Esc. ' ELSE '' END) || t_dnn.dnn_esc || t_dnn.dnn_plt || (CASE WHEN t_dnn.dnn_pta IS NOT NULL AND t_dnn.dnn_pta <> ' ' THEN ' ? ' ELSE '' END ) || t_dnn.dnn_pta AS domicilio, tdo_cod, tdo_des, 'DESTINO OTRO REGISTRO' AS tipoent, tpe_cod, tpe_des, ttr_cod, ttr_des, mun_nom, org_des AS destino, act_cod, act_des, dil_txt
	FROM r_res, t_hte, t_dnn, &nom_esquema_generico..t_mun, &nom_esquema_generico..a_org, r_tdo, r_tpe, r_ttr, r_act, r_dil
	WHERE res_ter = hte_ter
		AND res_dom = dnn_dom
		AND res_tnv = hte_nvr
		AND dnn_pai = mun_pai
		AND dnn_prv = mun_prv
		AND dnn_mun = mun_cod
		AND res_mod = 1
		AND res_ocd = org_cod(+)
		AND res_tdo = tdo_ide
		AND res_tpe = tpe_ide
		AND res_ttr = ttr_ide(+)
		AND res_act = act_ide(+)
		AND res_dep = dil_dep(+)
		AND res_uor = dil_uor(+)
		AND res_tip = dil_tir(+)
		AND res_fec = dil_fec(+)
	UNION SELECT DISTINCT res_dep, res_uor, res_tip, res_eje, res_num, (CASE WHEN res_est = 0 THEN 'PENDIENTE' WHEN res_est = 1 THEN 'ACEPTADA' WHEN res_est = 2 THEN 'RECHAZADA' WHEN res_est = 9 THEN 'ANULADA' ELSE NULL END) AS res_est, TO_CHAR (res_fec, 'DD/MM/YYYY HH24:MI') AS fechaentrada, TO_CHAR (res_fed, 'DD/MM/YYYY HH24:MI') AS fechadocum, res_asu, ((CASE WHEN hte_pa1 IS NULL OR hte_pa1 = ' ' THEN '' ELSE hte_pa1 || ' ' END) || (CASE WHEN hte_ap1 IS NULL OR hte_ap1 = ' ' THEN '' ELSE hte_ap1 || ' ' END) || (CASE WHEN hte_pa2 IS NULL OR hte_pa2 = ' ' THEN '' ELSE hte_pa2 || ' ' END) || (CASE WHEN hte_ap2 IS NULL OR hte_ap2 = ' ' THEN '' ELSE hte_ap2 END) || ',' || hte_nom ) AS interesado, t_tvi.tvi_des || ' ' || t_via.via_nom || ' ' || t_dsu.dsu_nud || ' ' || t_dsu.dsu_led || ' ' || t_dsu.dsu_nuh || t_dsu.dsu_leh || (CASE WHEN t_dsu.dsu_blq IS NOT NULL AND t_dsu.dsu_blq <> ' ' THEN ' Bl. ' ELSE '' END) || t_dsu.dsu_blq || (CASE WHEN t_dsu.dsu_por IS NOT NULL AND t_dsu.dsu_por <> ' ' THEN ' Portal. ' ELSE '' END) || t_dsu.dsu_por || (CASE WHEN t_dpo.dpo_esc IS NOT NULL AND t_dpo.dpo_esc <> ' ' THEN ' Esc. ' ELSE '' END) || t_dpo.dpo_esc || t_dpo.dpo_plt || (CASE WHEN t_dpo.dpo_pta IS NOT NULL AND t_dpo.dpo_pta <> ' ' THEN ' ? ' ELSE '' END) || t_dpo.dpo_pta AS domicilio, tdo_cod, tdo_des, 'DESTINO OTRO REGISTRO' AS tipoent, tpe_cod, tpe_des, ttr_cod, ttr_des, mun_nom, org_des AS destino, act_cod, act_des, dil_txt
	FROM r_res, t_hte, t_dpo, t_dsu, t_tvi, t_via, &nom_esquema_generico..t_mun, &nom_esquema_generico..a_org, r_tdo, r_tpe, r_ttr, r_act, r_dil
	WHERE res_ter = hte_ter
		AND res_dom = dpo_dom
		AND res_tnv = hte_nvr
		AND dpo_dsu = dsu_cod
		AND dsu_pai = mun_pai
		AND dsu_prv = mun_prv
		AND dsu_mun = mun_cod
		AND t_dsu.dsu_pai = t_via.via_pai(+)
		AND t_dsu.dsu_prv = t_via.via_prv(+)
		AND t_dsu.dsu_mun = t_via.via_mun(+)
		AND t_dsu.dsu_via = t_via.via_cod(+)
		AND t_via.via_tvi = t_tvi.tvi_cod(+)
		AND res_mod = 1
		AND res_ocd = org_cod(+)
		AND res_tdo = tdo_ide
		AND res_tpe = tpe_ide
		AND res_ttr = ttr_ide(+)
		AND res_act = act_ide(+)
		AND res_dep = dil_dep(+)
		AND res_uor = dil_uor(+)
		AND res_tip = dil_tir(+)
		AND res_fec = dil_fec(+)
;


               
DEFINE nom_esquema_generico = flexia_generico;

CREATE OR REPLACE FORCE VIEW V_REG_INF
	(REG_FEC, REG_FED, REG_ASU, REG_UOR_VIS, REG_UOR,
	REG_CODUOR, REG_UOR_NOM, REG_OBS, REG_INT, REG_TIP,
	REG_DOC, REG_DOC_NOM, REG_REM, REG_REM_NOM, REG_TRP,
	REG_TRP_NOM, REG_USU, REG_NUM,EXR_NUM,UOR_REG_COD_VIS,UOR_NOM_REG,CODIGO_ASUNTO,DESCRIPCION_ASUNTO)
	AS
	SELECT  NVL(TO_CHAR(A.RES_FEC,'DD/MM/YYYY HH24:Mi:SS'),'')  AS RES_FEC, 
	NVL(TO_CHAR(A.RES_FED,'DD/MM/YYYY HH24:Mi:SS'),'')  AS RES_FED, 
	A.RES_ASU, 
	UOR_COD_VIS, 
	A.RES_UOD,
	A.RES_UOR, 
	UOR_NOM,  
	A.RES_OBS, 
	HTE_NOM || NVL(' ','') || NVL(HTE_PA1,'') || NVL(' ','') ||  NVL(HTE_AP1,'') || NVL(' ','') || NVL(HTE_PA2,'') || NVL(' ','') ||  NVL(HTE_AP2,''), 
	A.RES_TIP, 
	r_tdo.TDO_COD,
	r_tdo.TDO_DES, 
	r_tpe.TPE_COD,
	r_tpe.TPE_DES, 
	r_ttr.TTR_COD AS COD_TRANSP, 
	r_ttr.TTR_DES, 
	A_USU.USU_NOM, 
	A.RES_EJE || '/' || A.RES_NUM, 
	E_EXR.EXR_NUM,
	(SELECT UOR_COD_VIS FROM R_RES B,A_UOR WHERE B.RES_UOR=A.RES_UOR AND B.RES_TIP=A.RES_TIP AND B.RES_EJE=A.RES_EJE AND B.RES_NUM=A.RES_NUM AND B.RES_DEP=A.RES_DEP  
	AND B.RES_UOR=A_UOR.UOR_COD) AS UOR_REG_COD_VIS,
	(SELECT UOR_NOM FROM R_RES B,A_UOR WHERE B.RES_UOR=A.RES_UOR AND B.RES_TIP=A.RES_TIP AND B.RES_EJE=A.RES_EJE AND B.RES_NUM=A.RES_NUM AND B.RES_DEP=A.RES_DEP  
	AND B.RES_UOR=A_UOR.UOR_COD) AS UOR_NOM_REG,
	A.ASUNTO, 
	R_TIPOASUNTO.DESCRIPCION
	FROM R_RES A, R_TDO, R_TPE, R_TTR, A_UOR, T_HTE, &nom_esquema_generico.A_USU A_USU, E_EXR, R_TIPOASUNTO
	WHERE A.RES_TDO = R_TDO.TDO_IDE
		AND A.RES_TPE = R_TPE.TPE_IDE(+)
		AND A.RES_TTR = R_TTR.TTR_IDE(+)
		AND A.RES_TER = HTE_TER(+)
		AND A.RES_TNV = HTE_NVR(+)
		AND A.RES_UOD = UOR_COD(+)
		AND A_USU.USU_COD = A.RES_USU
		AND A.RES_DEP=E_EXR.EXR_DEP(+)
		AND A.RES_UOR=E_EXR.EXR_UOR(+)
		AND A.RES_TIP=E_EXR.EXR_TIP(+)
		AND A.RES_EJE=E_EXR.EXR_EJE(+)
		AND A.RES_NUM=E_EXR.EXR_NRE(+)
		AND A.ASUNTO = R_TIPOASUNTO.CODIGO(+)
		AND A.RES_UOR = R_TIPOASUNTO.UNIDADREGISTRO(+);
   
commit;



CREATE OR REPLACE VIEW V_REG_DOC ( REG_NOM_DOC ,  res_num ,  res_tip ,  res_eje ,  res_dep ,  res_uor ,  res_ter ) AS
	select max(ltrim(sys_connect_by_path(red_nom_doc,','),',')) nombre, res_num, res_tip, res_eje, res_dep, res_uor, res_ter
	from (select res_num, res_tip, res_eje, res_dep, res_uor, res_ter, red_nom_doc, row_number() over(partition by res_num, res_tip, res_eje, res_dep, res_uor order by red_nom_doc) rn
	from r_red, r_res WHERE red_dep = res_dep
		AND red_uor=res_uor
		and red_eje=res_eje
		and red_num=res_num
		and red_tip=res_tip)
	start with rn = 1
	connect by prior rn = rn -1
	and prior res_num = res_num AND res_tip = res_tip AND res_eje = res_eje AND res_dep = res_dep AND res_uor = res_uor
	group by res_num, res_tip, res_eje, res_dep, res_uor, res_ter
;


CREATE OR REPLACE FORCE VIEW v_reg_jus ("REG_FEC", "REG_FED", "REG_ASU", "REG_UOR_VIS", "REG_UOR", "REG_CODUOR", "REG_UOR_NOM", "REG_OBS", "REG_INT", "REG_TIP", "REG_DOC", "REG_DOC_NOM", "REG_REM", "REG_REM_NOM", "REG_TRP", "REG_TRP_NOM", "REG_USU", "REG_NUM", "REG_DOCSINT", "REG_MOD", "REG_TER", "REG_ROL", "REG_INTERESADO", "COD_TERCERO", "VERSION_TERCERO", "REG_PRO", "REG_EXP")
                                                          AS
SELECT DISTINCT TO_CHAR (res_fec, 'DD/MM/YYYY HH24:MI') AS RES_FEC, 
    TO_CHAR (res_fed, 'DD/MM/YYYY HH24:MI')               AS RES_FED,
    res_asu,
    UOR_COD_VIS,
    RES_UOD,
    RES_UOR,
    UOR_NOM,
    RES_OBS,
    HTE_NOM
    || NVL(' ','')
    || NVL(HTE_PA1,'')
    || NVL(' ','')
    || NVL(HTE_AP1,'')
    || NVL(' ','')
    || NVL(HTE_PA2,'')
    || NVL(' ','')
    || NVL(HTE_AP2,'') AS interesado,
    RES_TIP,
    r_tdo.TDO_COD,
    r_tdo.TDO_DES,
    r_tpe.TPE_COD,
    r_tpe.TPE_DES,
    r_ttr.TTR_COD AS COD_TRANSP,
    r_ttr.TTR_DES,
    A_USU.USU_NOM,
    r_res.RES_EJE
    || '/'
    || r_res.RES_NUM AS NUM,
    HTE_DOC,
    (
    CASE
      WHEN res_mod = 0
      THEN 'ORDINARIA'
      WHEN res_mod = 1
      THEN 'DESTINO OTRO REGISTRO'
      ELSE NULL
    END ) AS res_mod,
    t_hte.HTE_TER,
    rol_des,
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
  FROM r_res,
    t_hte,
    t_dnn,
    &nom_esquema_generico..t_mun,
    a_uor,
    r_tdo,
    r_tpe,
    r_ttr,
    &nom_esquema_generico..A_USU A_USU,
    r_ext,
    r_rol,
    e_exr
  WHERE r_ext.ext_ter = t_hte.hte_ter(+)
  AND r_ext.ext_dot   = dnn_dom(+)
  AND r_ext.ext_nvr   = t_hte.hte_nvr(+)
  AND dnn_pai         = mun_pai
  AND dnn_prv         = mun_prv
  AND dnn_mun         = mun_cod
  AND res_uod         = uor_cod(+)
  AND res_tdo         = tdo_ide(+)
  AND res_tpe         = tpe_ide(+)
  AND res_ttr         = ttr_ide(+)
  AND A_USU.USU_COD   = R_RES.RES_USU
  AND r_ext.ext_uor   = r_res.RES_UOR
  AND r_ext.ext_EJE   = r_res.RES_EJE
  AND r_ext.ext_tip   = r_res.RES_TIP
  AND r_ext.ext_num   = r_res.RES_NUM
  AND r_ext.EXT_ROL   =r_rol.rol_cod
  AND exr_dep(+)      =res_dep
  AND exr_uor(+)      =res_uor
  AND exr_tip(+)      =res_tip
  AND exr_ejr(+)      =res_eje
  AND exr_nre(+)      =res_num
  AND procedimiento  IS NULL
  UNION
  SELECT DISTINCT TO_CHAR (res_fec, 'DD/MM/YYYY HH24:MI') AS RES_FEC, 
    TO_CHAR (res_fed, 'DD/MM/YYYY HH24:MI')               AS RES_FED,
    res_asu,
    UOR_COD_VIS,
    RES_UOD,
    RES_UOR,
    UOR_NOM,
    RES_OBS,
    HTE_NOM
    || NVL(' ','')
    || NVL(HTE_PA1,'')
    || NVL(' ','')
    || NVL(HTE_AP1,'')
    || NVL(' ','')
    || NVL(HTE_PA2,'')
    || NVL(' ','')
    || NVL(HTE_AP2,'') AS interesado,
    RES_TIP,
    r_tdo.TDO_COD,
    r_tdo.TDO_DES,
    r_tpe.TPE_COD,
    r_tpe.TPE_DES,
    r_ttr.TTR_COD AS COD_TRANSP,
    r_ttr.TTR_DES,
    A_USU.USU_NOM,
    r_res.RES_EJE
    || '/'
    || r_res.RES_NUM AS NUM,
    HTE_DOC,
    (
    CASE
      WHEN res_mod = 0
      THEN 'ORDINARIA'
      WHEN res_mod = 1
      THEN 'DESTINO OTRO REGISTRO'
      ELSE NULL
    END ) AS res_mod,
    t_hte.HTE_TER,
    rol_des,
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
  FROM r_res,
    t_hte,
    t_dnn,
    &nom_esquema_generico..t_mun,
    a_uor,
    r_tdo,
    r_tpe,
    r_ttr,
    &nom_esquema_generico..A_USU A_USU,
    r_ext,
    e_rol,
    e_exr
  WHERE r_ext.ext_ter = t_hte.hte_ter(+)
  AND r_ext.ext_dot   = dnn_dom(+)
  AND r_ext.ext_nvr   = t_hte.hte_nvr(+)
  AND dnn_pai         = mun_pai
  AND dnn_prv         = mun_prv
  AND dnn_mun         = mun_cod
  AND res_uod         = uor_cod(+)
  AND res_tdo         = tdo_ide(+)
  AND res_tpe         = tpe_ide(+)
  AND res_ttr         = ttr_ide(+)
  AND A_USU.USU_COD   = R_RES.RES_USU
  AND r_ext.ext_uor   = r_res.RES_UOR
  AND r_ext.ext_EJE   = r_res.RES_EJE
  AND r_ext.ext_tip   = r_res.RES_TIP
  AND r_ext.ext_num   = r_res.RES_NUM
  AND r_ext.EXT_ROL   =e_rol.rol_cod
  AND exr_dep(+)      =res_dep
  AND exr_uor(+)      =res_uor
  AND exr_tip(+)      =res_tip
  AND exr_ejr(+)      =res_eje
  AND exr_nre(+)      =res_num
  AND procedimiento  IS NOT NULL
  AND procedimiento   =e_rol.rol_pro;

  
  
  
 CREATE OR REPLACE VIEW V_EXP_INT_INF (EXP_LOC, EXP_FEI, EXP_FEF, EXP_ASU, EXP_NUM, EXP_EJE, EXP_USU_COD, EXP_USU_NOM, EXP_UOR_COD, EXP_UOR_NOM, EXP_OBS, EXP_PRO_COD, EXP_PRO_NOM, TRA_FEI, TRA_FEF, TRA_NOM, TRA_COD, TRA_OBS, TRA_UOR_COD, TRA_UOR_NOM, EXT_TER) AS
	SELECT E_EXP.EXP_LOC, E_EXP.EXP_FEI, E_EXP.EXP_FEF, E_EXP.EXP_ASU, E_EXP.EXP_NUM, E_EXP.EXP_EJE, E_EXP.EXP_USU, A_USU.USU_NOM, A1.UOR_COD_VIS, A1.UOR_NOM, E_EXP.EXP_OBS, E_EXP.EXP_PRO, E_PML.PML_VALOR, E_CRO.CRO_FEI, E_CRO.CRO_FEF, E_TML.TML_VALOR, E_CRO.CRO_TRA, E_CRO.CRO_OBS, A2.UOR_COD_VIS, A2.UOR_NOM, EXT_TER
	FROM E_EXP, E_CRO, E_TML, E_PML, &nom_esquema_generico..A_USU, A_UOR A1, A_UOR A2, &nom_esquema_generico..T_MUN, E_EXT
	WHERE E_EXP.EXP_MUN = E_PML.PML_MUN
		AND E_EXP.EXP_PRO = E_PML.PML_COD
		AND E_PML.PML_CMP = 'NOM'
		AND E_CRO.CRO_MUN = E_TML.TML_MUN
		AND E_CRO.CRO_PRO = E_TML.TML_PRO
		AND E_CRO.CRO_TRA = E_TML.TML_TRA
		AND E_TML.TML_CMP = 'NOM'
		AND E_EXP.EXP_USU = A_USU.USU_COD
		AND E_EXP.EXP_UOR = A1.UOR_COD
		AND E_PML.PML_LENG = E_TML.TML_LENG
		AND E_CRO.CRO_MUN = E_EXP.EXP_MUN(+)
		AND E_CRO.CRO_PRO = E_EXP.EXP_PRO(+)
		AND E_CRO.CRO_NUM = E_EXP.EXP_NUM(+)
		AND E_CRO.CRO_MUN = E_EXT.EXT_MUN(+)
		AND E_CRO.CRO_PRO = E_EXT.EXT_PRO(+)
		AND E_CRO.CRO_NUM = E_EXT.EXT_NUM(+)
		AND E_CRO.CRO_MUN = T_MUN.MUN_COD
		AND E_CRO.CRO_UTR = A2.UOR_COD
		AND T_MUN.MUN_PAI = &cod_pais
		AND T_MUN.MUN_PRV = &cod_municipio
;




CREATE OR REPLACE FORCE VIEW "V_EXP_INF" ("EXP_LOC", "EXP_FEI", "EXP_FEF", "EXP_ASU", "EXP_NUM", "EXP_EJE", "EXP_USU_COD", "EXP_USU_NOM", "EXP_USU_LOG", "EXP_UOR_COD", "EXP_UOR_NOM", "EXP_OBS", "EXP_PRO_COD", "EXP_PRO_NOM", "TRA_FEI", "TRA_FEF", "TRA_NOM", "TRA_COD", "TRA_OBS", "TRA_UOR_COD", "TRA_UOR_NOM", "TRA_COU") AS   
SELECT e_exp.exp_loc, 
  e_exp.exp_fei, 
  e_exp.exp_fef,
  e_exp.exp_asu,
  e_exp.exp_num,
  e_exp.exp_eje,
  a_usu.usu_cod,
  a_usu.usu_nom,
  a_usu.usu_log,
  a1.uor_cod_vis,
  a1.uor_nom, 
  e_exp.exp_obs,
  e_exp.exp_pro,
  e_pml.pml_valor,
  e_cro.cro_fei,
  e_cro.cro_fef,
  e_tml.tml_valor,
  e_tra.tra_cod,
  e_cro.cro_obs,
  a2.uor_cod_vis,
  a2.uor_nom,
  e_tra.tra_cou 
  FROM e_exp, 
  e_cro, 
  e_tml,
  e_pml, 
  sge_generico.A_USU,
  a_uor a1,
  a_uor a2,  
  e_tra 
  WHERE e_exp.exp_mun = e_pml.pml_mun
  AND e_exp.exp_pro = e_pml.pml_cod
  AND e_pml.pml_cmp = 'NOM'
  AND e_cro.cro_mun = e_tml.tml_mun
  AND e_cro.cro_pro = e_tml.tml_pro
  AND e_cro.cro_tra = e_tml.tml_tra
  AND e_tml.tml_cmp = 'NOM'
  AND e_exp.exp_usu = a_usu.usu_cod
  AND e_exp.exp_uor = a1.uor_cod
  AND e_pml.pml_leng = e_tml.tml_leng
  AND e_cro.cro_mun = e_exp.exp_mun(+)
  AND e_cro.cro_pro = e_exp.exp_pro(+)
  AND e_cro.cro_num = e_exp.exp_num(+)  
  AND e_cro.cro_utr = a2.uor_cod
  AND e_cro.cro_tra = e_tra.tra_cod(+) 
  AND e_cro.cro_pro = e_tra.tra_pro;
  
  
  
  
  CREATE OR REPLACE FORCE VIEW "V_REG_INICIO_EXP" ("NUMERO_REGISTRO", "FECHA_PRESENTACION", "NUM_EXPEDIENTE")
AS
  SELECT res_eje
    || '/'
    || res_num                            AS NUMERO_REGISTRO,
    TO_CHAR(res_fec,'dd/mm/yyyy HH24:MI') AS FECHA_PRESENTACION,
    EXR_NUM                               AS NUM_EXPEDIENTE
  FROM r_res,
    e_exr
  WHERE exr_nre=res_num
  AND exr_eje  =res_eje
  AND exr_dep  = res_dep
  AND exr_uor  = res_uor
  AND exr_top  =0
  AND RES_TIP='E'
  ORDER BY res_eje DESC,
    res_num DESC;

-- Salida al sistema
spool off;
quit;
