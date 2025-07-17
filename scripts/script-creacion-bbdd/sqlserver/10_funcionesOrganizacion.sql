DECLARE @nom_generico varchar(19);
SET @jndi='sge_generico';


set ANSI_NULLS ON
set QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[lista_interesados_registro] (@uor int,
	@numero int,
	@ejercicio int,
	@tipo varchar(1))
RETURNS varchar(5000) AS
BEGIN
declare @retvalue varchar(5000)
set @retvalue=''
declare @linea char
set @linea = char(10)

select @retvalue = @retvalue + ltrim(rtrim(isnull(REG_INT,''))) + '- ' + ltrim(rtrim(isnull(reg_rol,'')))+ @linea
from (
SELECT 
      hte_nom + isnull(' ', '') + isnull(hte_pa1, '') + isnull(' ', '') + isnull(hte_ap1, '') + isnull(' ', '') + 
isnull(hte_pa2, '') + isnull(' ', '') + isnull(hte_ap2, '') AS REG_INT,         
      r_rol.rol_des AS REG_ROL,hte_doc as documento   
    FROM r_res
    LEFT JOIN e_exr
    ON (exr_dep =res_dep
    AND exr_uor =res_uor
    AND exr_tip =res_tip
    AND exr_ejr =res_eje
    AND exr_nre =res_num)
    LEFT JOIN r_ext
    ON r_ext.ext_uor  = r_res.res_uor
    AND r_ext.ext_num = r_res.res_num
    AND r_ext.ext_tip = r_res.res_tip
    AND r_ext.ext_eje = r_res.res_eje
    LEFT JOIN r_rol
    ON r_ext.ext_rol = r_rol.rol_cod
    LEFT JOIN t_hte
    ON r_ext.ext_ter  = t_hte.hte_ter
    AND r_ext.ext_nvr = t_hte.hte_nvr
    LEFT JOIN t_dnn
    ON r_ext.ext_dot = dnn_dom
    LEFT JOIN @nom_generico..dbo.t_mun
    ON dnn_pai                                                                   = mun_pai
    AND dnn_prv                                                                  = mun_prv
    AND dnn_mun                                                                  = mun_cod
    WHERE procedimiento                                                         IS NOT NULL
   AND res_uor                                                                  =@uor
and res_num=@numero and res_eje=@ejercicio
    AND res_tip                                                                  =@tipo
    UNION
    SELECT   
      hte_nom + isnull(' ', '') + isnull(hte_pa1, '') + isnull(' ', '') + isnull(hte_ap1, '') + 
isnull(' ', '') + isnull(hte_pa2, '') + isnull(' ', '') + isnull(hte_ap2, '') AS REG_INT,
      r_rol.rol_des AS REG_ROL,hte_doc as documento
      FROM r_res
    INNER JOIN e_exr
    ON (exr_dep =res_dep
    AND exr_uor =res_uor
    AND exr_tip =res_tip
    AND exr_ejr =res_eje
    AND exr_nre =res_num)   
    LEFT JOIN r_ext
    ON r_ext.ext_uor  = r_res.res_uor
    AND r_ext.ext_num = r_res.res_num
    AND r_ext.ext_tip = r_res.res_tip
    AND r_ext.ext_eje = r_res.res_eje
    LEFT JOIN r_rol
    ON r_ext.ext_rol = r_rol.rol_cod
    LEFT JOIN t_hte
    ON r_ext.ext_ter  = t_hte.hte_ter
    AND r_ext.ext_nvr = t_hte.hte_nvr
    LEFT JOIN t_dnn
    ON r_ext.ext_dot = dnn_dom
    LEFT JOIN @nom_generico..dbo.t_mun
    ON dnn_pai                                                                   = mun_pai
    AND dnn_prv                                                                  = mun_prv
    AND dnn_mun                                                                  = mun_cod
    WHERE procedimiento                                                         IS NULL
    AND res_uor                                                                  =@uor
and res_num=@numero and res_eje=@ejercicio
    AND res_tip                                                                  =@tipo
) as tmp_tbl

if (@retvalue!='')
begin
set @retvalue = substring(@retvalue,1,len(@retvalue)-1)
end
return @retvalue
END

set ANSI_NULLS ON
set QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[lista_anexos_registro] (@uor int,
	@numero int,
	@ejercicio int,
	@tipo varchar(1))
RETURNS varchar(5000) AS
BEGIN
declare @retvalue varchar(5000)
set @retvalue=''
declare @linea char
set @linea = char(10)

select @retvalue = @retvalue + '- ' + ltrim(rtrim(isnull(red_nom_doc,''))) + @linea
from (select red_nom_doc from r_red as s where red_tip=@tipo and red_uor=@uor and red_doc is not null 
and red_eje=@ejercicio and red_num=@numero) as tmp_tbl

if (@retvalue!='')
begin
set @retvalue = substring(@retvalue,1,len(@retvalue)-1)
end
return @retvalue
END



set ANSI_NULLS ON
set QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[lista_documentos_interesado_registro] (@uor int,
	@numero int,
	@ejercicio int,
	@tipo varchar(1))
RETURNS varchar(5000) AS
BEGIN
declare @retvalue varchar(5000)
set @retvalue=''
declare @linea char
set @linea = char(10)

select @retvalue = @retvalue + ltrim(rtrim(isnull(HTE_DOC,'')))+ @linea
from (
SELECT 
      hte_doc
    FROM r_res
    LEFT JOIN e_exr
    ON (exr_dep =res_dep
    AND exr_uor =res_uor
    AND exr_tip =res_tip
    AND exr_ejr =res_eje
    AND exr_nre =res_num)
    LEFT JOIN r_ext
    ON r_ext.ext_uor  = r_res.res_uor
    AND r_ext.ext_num = r_res.res_num
    AND r_ext.ext_tip = r_res.res_tip
    AND r_ext.ext_eje = r_res.res_eje
    LEFT JOIN r_rol
    ON r_ext.ext_rol = r_rol.rol_cod
    LEFT JOIN t_hte
    ON r_ext.ext_ter  = t_hte.hte_ter
    AND r_ext.ext_nvr = t_hte.hte_nvr
    LEFT JOIN t_dnn
    ON r_ext.ext_dot = dnn_dom
    LEFT JOIN @nom_generico..dbo.t_mun
    ON dnn_pai                                                                   = mun_pai
    AND dnn_prv                                                                  = mun_prv
    AND dnn_mun                                                                  = mun_cod
    WHERE procedimiento                                                         IS NOT NULL
   AND res_uor                                                                  =@uor
and res_num=@numero and res_eje=@ejercicio
    AND res_tip                                                                  =@tipo
    UNION
    SELECT   
      hte_doc
      FROM r_res
    INNER JOIN e_exr
    ON (exr_dep =res_dep
    AND exr_uor =res_uor
    AND exr_tip =res_tip
    AND exr_ejr =res_eje
    AND exr_nre =res_num)   
    LEFT JOIN r_ext
    ON r_ext.ext_uor  = r_res.res_uor
    AND r_ext.ext_num = r_res.res_num
    AND r_ext.ext_tip = r_res.res_tip
    AND r_ext.ext_eje = r_res.res_eje
    LEFT JOIN r_rol
    ON r_ext.ext_rol = r_rol.rol_cod
    LEFT JOIN t_hte
    ON r_ext.ext_ter  = t_hte.hte_ter
    AND r_ext.ext_nvr = t_hte.hte_nvr
    LEFT JOIN t_dnn
    ON r_ext.ext_dot = dnn_dom
    LEFT JOIN @nom_generico..dbo.t_mun
    ON dnn_pai                                                                   = mun_pai
    AND dnn_prv                                                                  = mun_prv
    AND dnn_mun                                                                  = mun_cod
    WHERE procedimiento                                                         IS NULL
    AND res_uor                                                                  =@uor
and res_num=@numero and res_eje=@ejercicio
    AND res_tip                                                                  =@tipo
) as tmp_tbl

if (@retvalue!='')
begin
set @retvalue = substring(@retvalue,1,len(@retvalue)-1)
end
return @retvalue
END




set ANSI_NULLS ON
set QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[domicilio_registro] (@codigo int,
	@version int,
	@domicilio int)
RETURNS varchar(5000) AS
BEGIN
declare @retvalue varchar(5000)
set @retvalue=''

select @retvalue = int_dnn from (SELECT 
  (
  CASE
    WHEN t_tvi.tvi_des IS NOT NULL
    AND t_tvi.tvi_des  <> ' '
    THEN t_tvi.tvi_des + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_via.via_nom IS NOT NULL
    AND t_via.via_nom  <> ' '
    THEN t_via.via_nom + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_dmc IS NOT NULL
    AND t_dnn.dnn_dmc  <> ' '
    THEN t_dnn.dnn_dmc + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_nud IS NOT NULL
    THEN CONVERT(VARCHAR, t_dnn.dnn_nud) + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_led IS NOT NULL
    AND t_dnn.dnn_led  <> ' '
    THEN t_dnn.dnn_led + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_nuh IS NOT NULL
    THEN CONVERT(VARCHAR, t_dnn.dnn_nuh) + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_leh IS NOT NULL
    AND t_dnn.dnn_leh  <> ' '
    THEN t_dnn.dnn_leh + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_blq IS NOT NULL
    AND t_dnn.dnn_blq  <> ' '
    THEN ' Bl. ' + t_dnn.dnn_blq + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_por IS NOT NULL
    AND t_dnn.dnn_por  <> ' '
    THEN ' Portal. ' + t_dnn.dnn_por + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_esc IS NOT NULL
    AND t_dnn.dnn_esc  <> ' '
    THEN ' Esc. ' + t_dnn.dnn_esc + ' '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_plt IS NOT NULL
    AND t_dnn.dnn_plt  <> ' '
    THEN t_dnn.dnn_plt + 'º '
    ELSE ''
  END) + (
  CASE
    WHEN t_dnn.dnn_pta IS NOT NULL
    AND t_dnn.dnn_pta  <> ' '
    THEN t_dnn.dnn_pta + ' '
    ELSE ''
  END)              AS INT_DNN
FROM dbo.T_HTE
LEFT OUTER JOIN dbo.T_TER
ON dbo.T_TER.TER_COD = dbo.T_HTE.HTE_TER
RIGHT OUTER JOIN dbo.T_DOT
ON dbo.T_TER.TER_DOM  = dbo.T_DOT.DOT_DOM
AND dbo.T_TER.TER_COD = dbo.T_DOT.DOT_TER
INNER JOIN dbo.T_DOM
ON dbo.T_DOT.DOT_DOM = dbo.T_DOM.DOM_COD
INNER JOIN dbo.T_DNN
ON dbo.T_DOT.DOT_DOM = dbo.T_DNN.DNN_DOM
LEFT OUTER JOIN @nom_generico..dbo.T_MUN AS t_mun
ON dbo.T_DNN.DNN_PAI  = t_mun.MUN_PAI
AND dbo.T_DNN.DNN_PRV = t_mun.MUN_PRV
AND dbo.T_DNN.DNN_MUN = t_mun.MUN_COD
LEFT OUTER JOIN dbo.T_VIA
ON dbo.T_DNN.DNN_PAI  = dbo.T_VIA.VIA_PAI
AND dbo.T_DNN.DNN_PRV = dbo.T_VIA.VIA_PRV
AND dbo.T_DNN.DNN_MUN = dbo.T_VIA.VIA_MUN
AND dbo.T_DNN.DNN_VIA = dbo.T_VIA.VIA_COD
LEFT OUTER JOIN dbo.T_TVI
ON dbo.T_VIA.VIA_TVI = dbo.T_TVI.TVI_COD
LEFT OUTER JOIN @nom_generico..dbo.T_PRV AS t_prv
ON dbo.T_DNN.DNN_PAI  = t_prv.PRV_PAI
AND dbo.T_DNN.DNN_PRV = t_prv.PRV_COD
LEFT OUTER JOIN dbo.T_ESI
ON dbo.T_DNN.DNN_PAI  = dbo.T_ESI.ESI_PAI
AND dbo.T_DNN.DNN_MUN = dbo.T_ESI.ESI_MUN
AND dbo.T_DNN.DNN_PRV = dbo.T_ESI.ESI_PRV
AND dbo.T_DNN.DNN_ESI = dbo.T_ESI.ESI_COD
LEFT OUTER JOIN dbo.T_ECO
ON dbo.T_ESI.ESI_PAI  = dbo.T_ECO.ECO_PAI
AND dbo.T_ESI.ESI_MUN = dbo.T_ECO.ECO_MUN
AND dbo.T_ESI.ESI_PRV = dbo.T_ECO.ECO_PRV
AND dbo.T_ESI.ESI_ECO = dbo.T_ECO.ECO_COD
WHERE HTE_TER=@codigo AND HTE_NVR=@version AND DOM_COD=@domicilio) as tmp_tbl


return @retvalue
END



set ANSI_NULLS ON
set QUOTED_IDENTIFIER ON
GO
CREATE
  FUNCTION [dbo].[ejecutaJustificante](      
      @numero VARCHAR(50),
	  @uor    INT,
      @tipo VARCHAR(1)
) RETURNS TABLE
  AS
    RETURN
    ( SELECT DISTINCT CONVERT(VARCHAR, res_fec, 121)             AS reg_fec,
      dbo.lista_anexos_registro(res_uor,res_num,res_eje,res_tip) AS reg_fich,
	dbo.lista_interesados_registro(res_uor,res_num,res_eje,res_tip) AS lista_interesados,
dbo.lista_documentos_interesado_registro(res_uor,res_num,res_eje,res_tip) AS LISTA_DOCUMENTOS_INTERESADO,
      CONVERT(VARCHAR, res_fed, 121)                             AS reg_fed,
dbo.domicilio_registro(res_ter,res_tnv,res_dom) as domicilio_interesado,
      res_asu AS REG_ASU,
      uor_cod_vis AS REG_UOR_VIS,
      res_uod AS REG_UOR,
      res_uor AS REG_CODUOR,
      uor_nom AS REG_UOR_NOM,
      res_obs AS REG_OBS,
      hte_nom + isnull(' ', '') + isnull(hte_pa1, '') + isnull(' ', '') + isnull(hte_ap1, '') + isnull(' ', '') + isnull(hte_pa2, '') + isnull(' ', '') + isnull(hte_ap2, '') AS REG_INT,
      res_tip AS REG_TIP,
      r_tdo.tdo_cod AS REG_DOC,
      r_tdo.tdo_des AS REG_DOC_NOM,
      r_tpe.tpe_cod AS REG_REM,
      r_tpe.tpe_des AS REG_REM_NOM,
      r_ttr.ttr_cod AS REG_TRP,
      r_ttr.ttr_des AS REG_TRP_NOM,
      a_usu.usu_nom AS REG_USU,
      CONVERT(VARCHAR, r_res.res_eje) + '/' +CONVERT(VARCHAR, r_res.res_num) AS REG_NUM,
      hte_doc AS REG_DOCSINT,
      (
      CASE
        WHEN res_mod = 0
        THEN 'ORDINARIA'
        WHEN res_mod = 1
        THEN 'DESTINO OTRO REGISTRO'
        ELSE NULL
      END) AS REG_MOD,
      t_hte.hte_ter AS REG_TER,
      (
      CASE
        WHEN res_ter = hte_ter
        THEN 'PRINCIPAL'
        ELSE 'SECUNDARIO'
      END ) AS REG_INTERESADO,
      res_ter AS COD_TERCERO,
      res_tnv AS VERSION_TERCERO,
      procedimiento AS REG_PRO,
      exr_num AS REG_EXP
    FROM r_res
    LEFT JOIN e_exr
    ON (exr_dep =res_dep
    AND exr_uor =res_uor
    AND exr_tip =res_tip
    AND exr_ejr =res_eje
    AND exr_nre =res_num)
    RIGHT JOIN r_tdo
    ON res_tdo = tdo_ide
    LEFT JOIN r_tpe
    ON res_tpe = tpe_ide
    LEFT JOIN r_ttr
    ON res_ttr = ttr_ide
    LEFT JOIN a_uor
    ON res_uod = uor_cod
    LEFT JOIN @nom_generico..dbo.a_usu a_usu
    ON a_usu.usu_cod = r_res.res_usu
    LEFT JOIN r_ext
    ON r_ext.ext_uor  = r_res.res_uor
    AND r_ext.ext_num = r_res.res_num
    AND r_ext.ext_tip = r_res.res_tip
    AND r_ext.ext_eje = r_res.res_eje
AND R_EXT.EXT_TER=RES_TER AND R_EXT.EXT_NVR=R_RES.RES_TNV AND R_EXT.EXT_DOT=R_RES.RES_DOM    
    LEFT JOIN t_hte
    ON r_ext.ext_ter  = t_hte.hte_ter
    AND r_ext.ext_nvr = t_hte.hte_nvr
    where res_uor                                                                  =@uor
    AND (CONVERT(VARCHAR, r_res.res_eje) + '/' +CONVERT(VARCHAR, r_res.res_num)) =@numero
    AND res_tip                                                                  =@tipo
    )
GO

