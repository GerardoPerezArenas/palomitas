--------------------------------------------------------------------------
-- FLEXIA 10.4 
-- Altia Consultores 
-- Scripts creación BD flexia 
-- Oracle
-- Creación del esquema de una organización de producción
-- Ejecutar con usuario con permisos DBA
--------------------------------------------------------------------------

spool 2_crea_usuarios_organizacion.log
SET ECHO ON;

-- Esquema organización
DEFINE nom_esquema_generico = flexia_generico

create or replace
TYPE anotacion
AS
  OBJECT
  (
    reg_fec         VARCHAR2(16),
    reg_fed         VARCHAR2(16),
    reg_fich        VARCHAR2(2000),
    reg_asu         VARCHAR2(4000),    
    reg_uor_vis     VARCHAR2(6),
    reg_uor         NUMBER(5,0),
    reg_coduor      NUMBER(5,0),
    reg_uor_nom     VARCHAR2(80),
    reg_obs         VARCHAR2(2000),
    reg_int         VARCHAR2(300),
    reg_tip         VARCHAR2(1),
    reg_doc         VARCHAR2(5),
    reg_doc_nom     VARCHAR2(50),
    reg_rem         VARCHAR2(2),
    reg_rem_nom     VARCHAR2(45),
    reg_trp         VARCHAR2(2),
    REG_TRP_NOM     VARCHAR2(45),
    REG_USU         VARCHAR2(40),
    REG_NUM         VARCHAR2(81),
    REG_DOCSINT     VARCHAR2(25),
    REG_MOD         VARCHAR2(21),
    REG_TER         NUMBER(12,0),
    REG_INTERESADO  VARCHAR2(10),
    COD_TERCERO     NUMBER(12,0),
    VERSION_TERCERO NUMBER(3,0),
    REG_PRO	VARCHAR2(5),
    reg_exp varchar2(20),
    lista_interesados varchar2(1000),
    lista_documentos_interesado varchar2(500),
    domicilio_interesado varchar2(200)
);
/

create or replace
type salidaJustificante as table of anotacion;
/


create or replace
FUNCTION domicilio_registro ( codigo IN number, version IN number,domicilio IN number)
RETURN VARCHAR2 IS
   DIRECCION varchar2(2000);
begin
SELECT 
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
    || t_dnn.dnn_pta into direccion
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
  WHERE ter_dom     = dot_dom (+)
  AND ter_cod       =hte_ter(+)
  AND ter_cod       =dot_ter(+)
  AND dot_dom       = dom_cod (+)
  AND dom_cod       = dnn_dom (+)
  AND t_dnn.dnn_pai = t_mun.mun_pai(+)
  AND t_dnn.dnn_prv = t_mun.mun_prv(+)
  AND t_dnn.dnn_mun = t_mun.mun_cod(+)
  AND t_dnn.dnn_pai = t_via.via_pai(+)
  AND t_dnn.dnn_prv = t_via.via_prv(+)
  AND t_dnn.dnn_mun = t_via.via_mun(+)
  AND t_dnn.dnn_via = t_via.via_cod(+)
  AND t_via.via_tvi = t_tvi.tvi_cod(+)
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
  AND HTE_TER=CODIGO
  AND HTE_NVR=VERSION
  AND DOM_COD=DOMICILIO;

return DIRECCION;
end;
/


create or replace
FUNCTION lista_documentos_interesado ( uor IN number, numero IN number,ejercicio IN number, tipo in VARCHAR2)
RETURN VARCHAR2 IS
    documentos varchar2(2000); i number:= 0;
    v_salto VARCHAR2(10);
begin
v_salto :=CHR(10);
FOR r IN (SELECT hte_doc
  FROM r_res,
    t_hte,
    r_ext
  WHERE r_ext.ext_ter = t_hte.hte_ter(+)
  AND r_ext.ext_nvr   = t_hte.hte_nvr(+)
  AND r_ext.ext_uor   = r_res.RES_UOR
  AND r_ext.ext_EJE   = r_res.RES_EJE
  AND r_ext.ext_tip   = r_res.RES_TIP
  AND r_ext.ext_num   = r_res.RES_NUM
 AND res_uor         =uor
      AND res_eje=ejercicio and res_num=numero
      AND res_tip         =tipo) 
LOOP

  documentos := documentos||'*' ||r.hte_doc|| v_salto;
END LOOP ;
return documentos;
end;
/

create or replace
FUNCTION lista_interesados_registro ( uor IN number, numero IN number,ejercicio IN number, tipo in VARCHAR2)
RETURN VARCHAR2 IS
    interesados varchar2(2000); i number:= 0;
    v_salto VARCHAR2(10);
begin
v_salto :=CHR(10);
FOR r IN (SELECT hte_nom
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
    || t_hte.hte_ap2 as documento, r_rol.rol_des as rol
  FROM r_res,
    t_hte,
    t_dnn,
    r_ext,
    r_rol,
    e_exr
  WHERE r_ext.ext_ter = t_hte.hte_ter(+)
  AND r_ext.ext_dot   = dnn_dom(+)
  AND r_ext.ext_nvr   = t_hte.hte_nvr(+)
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
  and res_num=numero and res_tip=tipo and res_eje=ejercicio and res_uor=uor
  UNION
  SELECT hte_nom
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
    || t_hte.hte_ap2 as documento, e_rol.rol_des as rol
  FROM r_res,
    t_hte,
    t_dnn,
    r_ext,
    e_rol,
    e_exr
  WHERE r_ext.ext_ter = t_hte.hte_ter(+)
  AND r_ext.ext_dot   = dnn_dom(+)
  AND r_ext.ext_nvr   = t_hte.hte_nvr(+)
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
  AND procedimiento   =e_rol.rol_pro
  and res_num=numero and res_tip=tipo and res_eje=ejercicio and res_uor=uor) 
LOOP

  interesados := interesados||'*' ||r.documento|| ' - ' || r.rol || v_salto;
END LOOP ;
return interesados;
end;
/

create or replace
FUNCTION lista_anexos_registro ( uor IN number, numero IN number,ejercicio IN number, tipo in VARCHAR2)
RETURN VARCHAR2 IS
    ficheros varchar2(2000); i number:= 0;
    v_salto VARCHAR2(10);
begin
v_salto :=CHR(10);
FOR r IN (select red_nom_doc from r_red where red_tip=tipo
and red_num=numero and red_uor=uor 
and red_eje=ejercicio and red_doc is not null) 
LOOP

  ficheros := ficheros||'- ' ||r.red_nom_doc|| v_salto;
END LOOP ;
return ficheros;
end;
/

create or replace FUNCTION ejecutaJustificante  (
      numero IN varchar2,
      uor    IN NUMBER,
      tipo   IN VARCHAR2) 
      RETURN salidaJustificante pipelined
      is


TYPE t_ref_cursor IS REF CURSOR;    
cursorRegistro        t_ref_cursor;
lr_out_rec          anotacion := anotacion( NULL,NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,
NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,null,null,null);

BEGIN

OPEN cursorRegistro FOR 
        SELECT DISTINCT TO_CHAR (res_fec, 'DD/MM/YYYY HH24:MI')               AS RES_FEC,
    TO_CHAR (res_fed, 'DD/MM/YYYY HH24:MI')                             AS RES_FED,
        lista_anexos_registro (res_uor,r_res.RES_NUM,r_res.RES_EJE,res_tip) AS anexos,
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
    (
    CASE
      WHEN res_ter = hte_ter
      THEN 'PRINCIPAL'
      ELSE 'SECUNDARIO'
    END ) AS TIPOINTERESADO,
    res_ter,
    res_tnv,
    procedimiento,
    exr_num,
    lista_interesados_registro (res_uor, res_num, res_eje, res_tip) as lista_interesados,
    lista_documentos_interesado(res_uor, res_num, res_eje, res_tip) as lista_documentos_interesado,
    domicilio_registro (res_ter, res_tnv, res_dom) as domicilio_interesado
  FROM r_res,
  t_hte,
  a_uor,
  r_tdo,
  r_tpe,
  r_ttr,
  &nom_esquema_generico..A_USU A_USU,
  r_ext,
  e_exr
WHERE r_ext.ext_ter = t_hte.hte_ter(+)
AND r_ext.ext_nvr   = t_hte.hte_nvr(+)
AND res_uod         = uor_cod(+)
AND res_tdo         = tdo_ide(+)
AND res_tpe         = tpe_ide(+)
AND res_ttr         = ttr_ide(+)
AND A_USU.USU_COD   = R_RES.RES_USU
AND r_ext.ext_uor   = r_res.RES_UOR
AND r_ext.ext_EJE   = r_res.RES_EJE
AND r_ext.ext_tip   = r_res.RES_TIP
AND r_ext.ext_num   = r_res.RES_NUM
AND R_EXT.EXT_TER   =RES_TER
AND R_EXT.EXT_NVR   =R_RES.RES_TNV
AND R_EXT.EXT_DOT   =R_RES.RES_DOM
AND exr_dep(+)      =res_dep
AND exr_uor(+)      =res_uor
AND exr_tip(+)      =res_tip
AND exr_ejr(+)      =res_eje
AND exr_nre(+)      =res_num
AND res_uor         =uor
      AND (r_res.RES_EJE
    || '/'
    || r_res.RES_NUM)         =numero
      AND res_tip         =tipo;
  
        LOOP
            FETCH cursorRegistro 
            INTO 
                lr_out_rec.reg_fec,    
                lr_out_rec.reg_fed,
                lr_out_rec.reg_fich,
                lr_out_rec.reg_asu,
                lr_out_rec.reg_uor_vis,
                lr_out_rec.reg_uor,
                lr_out_rec.reg_coduor,
                lr_out_rec.reg_uor_nom,
                lr_out_rec.reg_obs,
                lr_out_rec.reg_int,
                lr_out_rec.reg_tip,
                lr_out_rec.reg_doc,
                lr_out_rec.reg_doc_nom,
                lr_out_rec.reg_rem,
                lr_out_rec.reg_rem_nom,
                lr_out_rec.reg_trp,
                lr_out_rec.reg_trp_nom,
                lr_out_rec.reg_usu,
                lr_out_rec.reg_num,
                lr_out_rec.REG_DOCSINT,
                lr_out_rec.reg_mod,
                lr_out_rec.reg_ter,
                lr_out_rec.reg_interesado,
                lr_out_rec.cod_tercero,
                lr_out_rec.version_tercero,
                lr_out_rec.reg_pro,
                lr_out_rec.reg_exp,
                lr_out_rec.lista_interesados,
                lr_out_rec.lista_documentos_interesado,
                lr_out_rec.domicilio_interesado;
            
            EXIT WHEN cursorRegistro%NOTFOUND;
            PIPE ROW(lr_out_rec);
        END LOOP;
    CLOSE cursorRegistro;
    RETURN;

END ejecutaJustificante;
/



-- Salida al sistema
spool off;
quit;



