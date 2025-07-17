--------------------------------------------------------------
----------------- inicio: Parche 13.01.01 --------------------
-- Script para añadir índices que faltan en instalaciones oracle. 
-- No afecta al funcionamiento. Se crean por mejora de rendimiento
-- Nota: En lanbide ya han sido creados
--------------------------------------------------------------

-- Tarea #115783 ----
-- Marcos Rama
---------------------


DEFINE nom_esquema_organizacion = FLEXIA_ORGANIZACION;
DEFINE nom_tablespace_organizacion = FLEXIA_ORGANIZACION;


CREATE INDEX &nom_esquema_organizacion..CAMPLIST_IDX1 ON A_CAMPLIST 
(CAMPLIST_CODLIST ASC)
TABLESPACE &nom_tablespace_organizacion
/

create index &nom_esquema_organizacion..ADJUNTO_COMUNICACION_idx1 on ADJUNTO_COMUNICACION
(ID_COMUNICACION  ASC)
TABLESPACE &nom_tablespace_organizacion
/

create index &nom_esquema_organizacion..ADJUNTO_EXT_NOTIFICACION_idx1 on ADJUNTO_EXT_NOTIFICACION 
(ID_NOTIFICACION   ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..ADJUNTO_NOTIFICACION_idx1 on ADJUNTO_NOTIFICACION  
(COD_MUNICIPIO   ASC,
COD_PROCEDIMIENTO ASC,
EJERCICIO ASC,
NUM_EXPEDIENTE ASC,
COD_TRAMITE ASC,
OCU_TRAMITE ASC
)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_DOC_EXT_IDX1 on E_DOC_EXT 
(DOC_EXT_MUN   ASC,
DOC_EXT_EJE ASC,
DOC_EXT_NUM ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_DOCS_FIRMAS_IDX1 on E_DOCS_FIRMAS  
(ID_DOC_PRESENTADO    ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_DOCS_PRESENTADOS_IDX1 on E_DOCS_PRESENTADOS  
(PRESENTADO_MUN     ASC,
PRESENTADO_EJE ASC,
PRESENTADO_NUM ASC,
PRESENTADO_PRO ASC,
PRESENTADO_COD_DOC ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_DOE_IDX1 on E_DOE   
(DOE_MUN     ASC,
DOE_PRO ASC,
DOE_COD ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_DOP_IDX1 on E_DOP    
(DOP_TDO      ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_DOT_IDX1 on E_DOT    
(DOT_TDO      ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_ENT_IDX1 on E_ENT     
(ENT_MUN       ASC,
ENT_PRO ASC,
ENT_DOC ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_EXT_IDX1 on E_EXT      
(EXT_TER       ASC,
EXT_NVR  ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..ENTIDADEAPLICACION_IDX1 on ENTIDADEAPLICACION      
(COD_ENTIDADEINFORME        ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..ENTSUBENT_IDX1 on ENTSUBENT       
(COD_ENTIDADEINFORME        ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..ENTSUBENT_IDX2 on ENTSUBENT       
(COD_PAI         ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..ETIQ_PLT_IDX1 on ETIQ_PLT        
(COD_CAMPOINFORME          ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..ETIQUETAXERADOR_IDX1 on ETIQUETAXERADOR        
(COD_ETIQUETA           ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_TRA_IDX1 on E_TRA        
(COD_DEPTO_NOTIFICACION           ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..E_TRA_UTR_IDX1 on E_TRA_UTR         
(TRA_UTR_COD           ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_DEFFORM_TRA_IDX1 on F_DEFFORM_TRA         
(DFT_MUN            ASC,
DFT_PRO ASC,
DFT_TRA)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_FIRMA_IDX1 on F_FIRMA         
(FF_UOR_FIRMA            ASC)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_FIRMA_IDX2 on F_FIRMA         
(FF_TRAFIRMA_MUN            ASC,
FF_TRAFIRMA_PRO ASC,
FF_TRAFIRMA_EJE ASC,
FF_TRAFIRMA_NUM ASC,
FF_TRAFIRMA_TRA ASC,
FF_TRAFIRMA_OCU ASC
)
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_PRECARGA_XML_IDX1 on F_PRECARGA_XML         
(FPX_FORM             ASC )
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_RESTRICCION_IDX1 on F_RESTRICCION         
(R_CAR              ASC )
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_RESTRICCION_IDX2 on F_RESTRICCION         
(R_UOR              ASC )
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_TRA_FORM_IDX1 on F_TRA_FORM         
(FT_DMTE               ASC )
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_TRA_FORM_IDX2 on F_TRA_FORM         
(FT_FDF                ASC )
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_TRA_FORM_IDX3 on F_TRA_FORM         
(FT_ESTADO                 ASC )
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_TRA_FORM_IDX4 on F_TRA_FORM         
(FT_TIPO                 ASC )
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_TRA_FORM_IDX5 on F_TRA_FORM         
(FT_RAIZ                  ASC )
TABLESPACE &nom_tablespace_organizacion
/
create index &nom_esquema_organizacion..F_TRAFORM_TRA_IDX1 on F_TRAFORM_TRA         
(TFT_TRA_MUN                   ASC,
TFT_TRA_PRO  ASC,
TFT_TRA_EJE ASC,
TFT_TRA_NUM ASC,
TFT_TRA_TRA ASC,
TFT_TRA_OCU ASC )
TABLESPACE &nom_tablespace_organizacion
/




CREATE INDEX &nom_esquema_organizacion..INF_CAMPOS_IDX1 ON &nom_esquema_organizacion..INF_CAMPOS
(  ORIGEN ASC
)
TABLESPACE &nom_tablespace_organizacion
/
 
/
  
CREATE INDEX &nom_esquema_organizacion..PLANT_INF_CAB_I_IDX1 ON &nom_esquema_organizacion..PLANT_INF_CAB_I
(  
PLANT_INF_CAB_I_TIPO ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..PLANT_INF_CAB_P_IDX1 ON &nom_esquema_organizacion..PLANT_INF_CAB_P
(  
PLANT_INF_CAB_P_TIPO ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..PLANT_INF_CRI_IDX1 ON &nom_esquema_organizacion..PLANT_INF_CRI
(  
PLANT_INF_CRI_CONDICION ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..PLANT_INFORMES_IDX1 ON &nom_esquema_organizacion..PLANT_INFORMES
(  
PLANT_ORIGEN ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..PLANT_INF_PIE_I_IDX1 ON &nom_esquema_organizacion..PLANT_INF_PIE_I
(  
PLANT_INF_PIE_I_TIPO ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..PLANT_INF_PIE_P_IDX1 ON &nom_esquema_organizacion..PLANT_INF_PIE_P
(  
PLANT_INF_PIE_P_TIPO ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..PLANT_INF_UOR_IDX1 ON &nom_esquema_organizacion..PLANT_INF_UOR
(  
PLANT_INF_UOR_ID ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..R_RES_IDX1 ON &nom_esquema_organizacion..R_RES
(  
	RES_UOC ASC,
	RES_TIC ASC,
	RES_EJC ASC,
	RES_NUC ASC,
	RES_DEC ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..R_RES_IDX2 ON &nom_esquema_organizacion..R_RES
(  
	PROCMUN ASC,
	PROCEDIMIENTO ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..TAREAS_PENDIENTES_INICIO_IDX1 ON &nom_esquema_organizacion..TAREAS_PENDIENTES_INICIO
(  
COD_OPERACION ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_CAMPOS_DESPLEGABLE_IDX1 ON &nom_esquema_organizacion..T_CAMPOS_DESPLEGABLE
(  
COD_TERCERO ASC
)	
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_CAMPOS_EXTRA_IDX1 ON &nom_esquema_organizacion..T_CAMPOS_EXTRA
(  
COD_PLANTILLA ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_CAMPOS_FECHA_IDX1 ON &nom_esquema_organizacion..T_CAMPOS_FECHA
(  
COD_TERCERO ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_CAMPOS_NUMERICO_IDX1 ON &nom_esquema_organizacion..T_CAMPOS_NUMERICO
(  
COD_TERCERO ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_CAMPOS_TEXTO_IDX1 ON &nom_esquema_organizacion..T_CAMPOS_TEXTO
(  
COD_TERCERO ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_CAMPOS_TEXTO_LARGO_IDX1 ON &nom_esquema_organizacion..T_CAMPOS_TEXTO_LARGO
(  
COD_TERCERO ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_CES_IDX1 ON &nom_esquema_organizacion..T_CES
(  
CES_PAI ASC,
CES_PRV ASC,
CES_MUN ASC,
CES_ESI ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_CNU_IDX1 ON &nom_esquema_organizacion..T_CNU
(  
CNU_PAI ASC,
CNU_PRV ASC,
CNU_MUN ASC,
CNU_ESI ASC,
CNU_NUC ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_CVE_IDX1 ON &nom_esquema_organizacion..T_CVE
(  
CVE_OEX ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_GRT_IDX1 ON &nom_esquema_organizacion..T_GRT
(  
GRT_TER ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_HDP_IDX1 ON &nom_esquema_organizacion..T_HDP
(  
HDP_TVV ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_HTE_IDX1 ON &nom_esquema_organizacion..T_HTE
(  
HTE_DOT ASC,
HTE_TER ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_HVI_IDX1 ON &nom_esquema_organizacion..T_HVI
(  
HVI_NAPA ASC,                                                                              
HVI_NAPR ASC,                                                                               
HVI_NAMU ASC,                                                                       
HVI_NAES ASC,                                                                              
HVI_NANU ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_MNZ_IDX1 ON &nom_esquema_organizacion..T_MNZ
(  
MNZ_CBJ ASC                                                                            
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_NDC_IDX1 ON &nom_esquema_organizacion..T_NDC
(  
NDC_TID ASC                                                                            
)
TABLESPACE &nom_tablespace_organizacion
/


   
CREATE INDEX &nom_esquema_organizacion..T_PAR_IDX2 ON &nom_esquema_organizacion..T_PAR
(  
PAR_CBJ ASC                                                                            
)
TABLESPACE &nom_tablespace_organizacion
/
  

CREATE INDEX &nom_esquema_organizacion..T_PAR_IDX3 ON &nom_esquema_organizacion..T_PAR
(  
PAR_SPA ASC,                                                                                
PAR_SPR ASC,                                                                             
PAR_SMU ASC,                                                                             
PAR_SDI ASC,                                                                              
PAR_SEC ASC,                                                                               
PAR_LET ASC                                                                           
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_PAR_IDX4 ON &nom_esquema_organizacion..T_PAR
(  
PAR_SSPA ASC,                                                                                
PAR_SSPR ASC,                                                                             
PAR_SSMU ASC,                                                                             
PAR_SSDI ASC,                                                                              
PAR_SSSE ASC,                                                                               
PAR_SSLE ASC ,
PAR_SSC ASC
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_PAR_IDX5 ON &nom_esquema_organizacion..T_PAR
(  
PAR_PHOJ ASC,                                                                                
PAR_PMNZ ASC,                                                                             
PAR_PPAR ASC                                                                          
)
TABLESPACE &nom_tablespace_organizacion
/
  
CREATE INDEX &nom_esquema_organizacion..T_PAR_IDX6 ON &nom_esquema_organizacion..T_PAR
(  
PAR_CPA ASC,                                                                                
PAR_CPR ASC,                                                                             
PAR_CMU ASC,
PAR_CPO ASC
)
TABLESPACE &nom_tablespace_organizacion
/

-- FIN Tarea #115783 ----
------------------------




--Tarea #115783 

CREATE INDEX &nom_esquema_organizacion..IDX_F_TRAFORM_ANEXO  ON &nom_esquema_organizacion..F_TRAFORM_ANEXO
(  
TFA_FORM ASC,
TFA_COD ASC,
TFA_DES ASC,
TFA_TIP ASC

)
TABLESPACE &nom_tablespace_organizacion
/



CREATE INDEX &nom_esquema_organizacion..IDX_F_TRA_FORM_1  ON &nom_esquema_organizacion..F_TRA_FORM
(  
FT_FBAJA ASC,
FT_ESTADO ASC,
FT_INST_MUN ASC,
FT_INST_EJE ASC,
FT_INST_NUM ASC
)
TABLESPACE &nom_tablespace_organizacion
/





-- Tarea #175287: Optimización de la carga de la ficha de expediente
-- 18/02/2015. Óscar Rodríguez
CREATE INDEX IDX_NOTIFICACION  ON NOTIFICACION
(  
NUM_EXPEDIENTE,
FIRMADA,
FECHA_ENVIO)
/


CREATE INDEX IDX_G_REL_BUSQUEDA  ON G_EXP
(  
EXP_MUN,
EXP_PRO,
EXP_EJE,
REL_NUM)
/


CREATE INDEX IDX_COMUNIACION  ON COMUNICACION
(
COD_ORGANIZACION,
EJERCICIO,
NUM_EXPEDIENTE)
/



CREATE INDEX IDX_DOC_PRESENTADO  ON E_DOCS_PRESENTADOS
(  
PRESENTADO_MUN,
PRESENTADO_EJE,
PRESENTADO_NUM,
PRESENTADO_PRO,
PRESENTADO_COD_DOC)
/


CREATE INDEX IDX3_E_TFE  ON E_TFE
(  
TFE_NUM
)
/


CREATE INDEX IDX_E_TCA  ON E_TCA
(  
TCA_MUN, 
TCA_PRO,
TCA_TRA,
TCA_ACTIVO
)
/


CREATE INDEX IDX_PCA_GROUP  ON E_PCA_GROUP
(  
PCA_PRO, 
PCA_ACTIVE
)
/

CREATE INDEX IDX_TCA  ON E_TCA
(  
TCA_PRO, 
TCA_ACTIVO,
TCA_VIS
)
/


CREATE INDEX IDX_PCA_2  ON E_PCA
(  
PCA_PRO, 
PCA_ACTIVO,
PCA_OCULTO,
PCA_POS_X
)
/


------------------------------------------------------------------
-------------------PUBLICACIÓN PARCHE 13.01.24--------------------
------------------------------------------------------------------
------------------------------------------------------------------


------------------------------------------------------------------
------------------- inicio PARCHE 13.01.25------------------------
------------------------------------------------------------------



------------------------------------------------------------------
------------------- inicio PARCHE 13.01.31------------------------
------------------------------------------------------------------

-- #206661: [OPTIMIZACION] Mejoras en la ejecución de ciertas consultas, tanto en Flexia como en los servicios web
CREATE INDEX IDX_E_EXR  ON E_EXR
(  
EXR_DEP, 
EXR_UOR,
EXR_TIP,
EXR_EJR,
EXR_NRE
)
/

CREATE INDEX INDEX_E_HIST_EXR  ON HIST_E_EXR
(  
EXR_DEP, 
EXR_UOR,
EXR_TIP,
EXR_EJR,
EXR_NRE
)
/



