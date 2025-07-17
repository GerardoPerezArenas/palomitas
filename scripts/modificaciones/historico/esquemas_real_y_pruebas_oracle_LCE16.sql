-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.00.01 --------------------------
-----------------------------------------------------------------------



-- Tarea #247202 Gesti?n de unidades org?nicas dadas de baja
	
-- Adri?n (13/09/2016)
	
ALTER TABLE A_UOR ADD UOR_OCULTA CHAR(1 BYTE) DEFAULT 'N' NOT NULL;

COMMENT ON COLUMN A_UOR.UOR_OCULTA IS 'Indica si se debe ocultar la unidad org?nica cuando est? de baja ("S") o si permanece visible en la aplicaci?n ("N")'
	
COMMIT;

--#253742-Milagros Noya Peña.


ALTER TABLE E_EXR ADD EXR_FECHAINSMOD TIMESTAMP;
COMMENT ON COLUMN E_EXR.EXR_FECHAINSMOD IS 'Fecha del sistema en el momento de la inserción o modificación';

COMMIT;


--Tarea #253692: Nuevo campo de expediente para ubicación de documentación. Parametrizable
--Milagros Noya (14/11/2016)
ALTER TABLE E_EXP 
ADD EXP_UBICACION_DOC VARCHAR2(10 BYTE) DEFAULT NULL NULL;
COMMENT ON COLUMN E_EXP.EXP_UBICACION_DOC IS 'Ubicación física de la documentación';

ALTER TABLE HIST_E_EXP 
ADD EXP_UBICACION_DOC VARCHAR2(10 BYTE) DEFAULT NULL NULL;
COMMENT ON COLUMN HIST_E_EXP.EXP_UBICACION_DOC IS 'Ubicación física de la documentación';

COMMIT;



--Tarea #234108: Registro - Obligatoriedad documento interesado seg?n asunto
	
--Milagros Noya (10/05/2016)
	
ALTER TABLE R_TIPOASUNTO ADD DOCINT_OBLIGATORIO NUMBER(1,0) DEFAULT 0;
	
COMMENT ON COLUMN R_TIPOASUNTO.DOCINT_OBLIGATORIO IS 'Indica si es obligatorio que el interesado tenga documento (tipo de documento distinto a "Sin documento"). 0:no; 1:si';
	
COMMIT;




-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.01 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.02 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.02 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.03 --------------------------
-----------------------------------------------------------------------

UPDATE A_APL SET APL_EXE='/jsp/portafirmas/documentoportafirmas/SearchDocumentoPortafirmasForm.jsp' WHERE APL_COD='11';



-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.03 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.04 --------------------------
-----------------------------------------------------------------------


--Tarea #262348: Lanbide - Registro de salida - Imprimir modelo petici?n de respuesta en consulta de salidas

UPDATE A_DOC SET DOC_NOM='MODPETICIONRPTAANOTACION' WHERE DOC_CEI=87;

COMMIT;

Insert into A_DOC (DOC_CEI,DOC_APL,DOC_NOM) values (88,1,'MODPETICIONRPTACONSULTA');

INSERT INTO ESTRUCTURAINFORME(COD_ESTRUCTURA,COD_ENTIDADEINFORME,CONSULTASQL,POSICION) /*En la consulta se hace un join con tablas del esquema gen?rico cuyo nombre puede cambiar en cada entorno*/
	
VALUES (88,20,'SELECT UOR_NOM,TER_NOM,TER_AP1,TER_PA1,TER_AP2,TER_PA2,TER_NOC,TVI_DES AS DESCTIPOVIA,VIA_NOM AS DESCVIA,DNN_DMC AS DESCDMC,DNN_NUD AS NUD,DNN_LED AS LED,DNN_NUH AS NUH,DNN_LEH AS LEH,DNN_BLQ AS BLQ,DNN_POR AS POR,DNN_ESC AS ESC,DNN_PLT AS PLT,DNN_PTA AS PTA,DNN_CPO AS CPO,DNN_LUG AS CAS,PAI_NOM,PRV_NOM,MUN_NOM FROM R_RES JOIN A_UOR ON RES_OFI=UOR_COD LEFT JOIN T_TER ON UOR_COD_VIS=TER_DOC LEFT JOIN T_DOM ON TER_DOM=DOM_COD LEFT JOIN T_DOT ON  TER_DOM=DOT_DOM AND TER_COD=DOT_TER LEFT JOIN T_DNN ON DOM_COD=DNN_DOM LEFT JOIN FLBGEN.T_MUN T_MUN ON DNN_PAI=MUN_PAI AND DNN_PRV=MUN_PRV AND DNN_MUN=MUN_COD LEFT JOIN FLBGEN.T_PAI T_PAI ON MUN_PAI=PAI_COD LEFT JOIN FLBGEN.T_PRV T_PRV ON MUN_PAI=PRV_PAI AND MUN_PRV=PRV_COD LEFT JOIN T_VIA ON DNN_VIA=VIA_COD AND DNN_VPA=VIA_PAI AND DNN_VPR=VIA_PRV AND DNN_VMU=VIA_MUN LEFT JOIN T_TVI ON DNN_TVI=TVI_COD WHERE UOR_ESTADO=''A'' AND (RES_EJE||''/0'')=? AND RES_UOR=? AND RES_TIP=? AND (TER_DOC=? OR TER_DOC IS NULL) AND (TER_TID=7 OR TER_TID IS NULL) AND (TER_SIT=''A'' OR TER_SIT IS NULL) AND (DOT_SIT=''A'' OR DOT_SIT IS NULL) AND (DOM_NML=2 OR DOM_NML IS NULL) AND TER_FBJ IS NULL',0);

COMMIT;


-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.04 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.05 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.05 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.06 --------------------------
-----------------------------------------------------------------------


CREATE TABLE "R_DOC_APORTADOS_ANTERIOR" 
   ("R_DOC_APORTADOS_DEP" NUMBER(3,0) NOT NULL ENABLE, 
	"R_DOC_APORTADOS_UOR" NUMBER(5,0) NOT NULL ENABLE, 
	"R_DOC_APORTADOS_EJE" NUMBER(4,0) NOT NULL ENABLE, 
	"R_DOC_APORTADOS_NUM" NUMBER(6,0) NOT NULL ENABLE, 
	"R_DOC_APORTADOS_TIP" VARCHAR2(1 BYTE) NOT NULL ENABLE, 
	"R_DOC_APORTADOS_NOM_DOC" VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	"R_DOC_APORTADOS_TIP_DOC" VARCHAR2(100 BYTE), 
	"R_DOC_APORTADOS_ORGANO" VARCHAR2(100 BYTE), 
	"R_DOC_APORTADOS_FEC_DOC" DATE
 );	
 
 
CREATE UNIQUE INDEX "PK_R_DOC_APORTADOS_ANTERIOR" ON "R_DOC_APORTADOS_ANTERIOR" ("R_DOC_APORTADOS_DEP", "R_DOC_APORTADOS_UOR", "R_DOC_APORTADOS_EJE", "R_DOC_APORTADOS_NUM", "R_DOC_APORTADOS_TIP", "R_DOC_APORTADOS_NOM_DOC");

 ALTER TABLE "R_DOC_APORTADOS_ANTERIOR" ADD CONSTRAINT "PK_R_DOC_APORTADOS_ANTERIOR" PRIMARY KEY ("R_DOC_APORTADOS_DEP", "R_DOC_APORTADOS_UOR", "R_DOC_APORTADOS_EJE", "R_DOC_APORTADOS_NUM", "R_DOC_APORTADOS_TIP", "R_DOC_APORTADOS_NOM_DOC");


 
 
 --Tarea #262531: Registro de salida - Imprimir salidas seleccionadas
	
--Milagros Noya (07/03/2017)

INSERT INTO A_CAMPLIST(CAMPLIST_COD,CAMPLIST_NOM,CAMPLIST_TAMANO,CAMPLIST_ACTIVO,CAMPLIST_CODLIST,CAMPLIST_ORDEN)
	
VALUES (13,'CHECK',3,1,3,-1);

COMMIT; 


--Tarea  #271557
ALTER TABLE R_RES ADD (
RES_SGA_COD varchar2(12 byte) DEFAULT NULL,
RES_SGA_EXP varchar2(30 byte) DEFAULT NULL);

--Tarea #271924
ALTER TABLE A_PLT ADD (
PLT_VIS_EXT varchar2(2 byte) DEFAULT 'SI' NOT NULL
CONSTRAINT PLT_VIS_EXT CHECK (PLT_VIS_EXT in ('SI','NO'))
);

 
 
 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.06 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.07 --------------------------
-----------------------------------------------------------------------

-- tarea 120581
 
Alter table R_TDO ADD (TDO_EST NUMBER(1,0) DEFAULT 1);

Alter table R_TDO ADD (TDO_FEC DATE);


 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.07 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.08 --------------------------
-----------------------------------------------------------------------

ALTER TABLE T_TER MODIFY TER_DCE Varchar2(100);
ALTER TABLE T_HTE MODIFY HTE_DCE Varchar2(100);


 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.08 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.09 --------------------------
-----------------------------------------------------------------------

CREATE SEQUENCE SEQ_OPERACIONES_EXPEDIENTE START WITH 1 MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 NOCACHE;

CREATE TABLE OPERACIONES_EXPEDIENTE (
	ID_OPERACION NUMBER(19,0) NOT NULL,
	COD_MUNICIPIO NUMBER(3,0) NOT NULL, 
	EJERCICIO NUMBER(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR2(30) NOT NULL,
	TIPO_OPERACION NUMBER(2,0) NOT NULL,
	FECHA_OPERACION DATE NOT NULL,
	COD_USUARIO NUMBER(4,0),
	DESCRIPCION_OPERACION CLOB NOT NULL,
	CONSTRAINT PK_OPERACIONES_EXPEDIENTE PRIMARY KEY(ID_OPERACION)
);

COMMENT ON COLUMN OPERACIONES_EXPEDIENTE.ID_OPERACION IS 'Clave autogenerada de la tabla';
COMMENT ON COLUMN OPERACIONES_EXPEDIENTE.COD_MUNICIPIO IS 'Código de municipio/organización';
COMMENT ON COLUMN OPERACIONES_EXPEDIENTE.EJERCICIO IS 'Ejercicio del expediente';
COMMENT ON COLUMN OPERACIONES_EXPEDIENTE.NUM_EXPEDIENTE IS 'Número del expediente';
COMMENT ON COLUMN OPERACIONES_EXPEDIENTE.TIPO_OPERACION IS 'Tipo de operación';
COMMENT ON COLUMN OPERACIONES_EXPEDIENTE.FECHA_OPERACION IS 'Fecha y hora en la que se realiza la operación';
COMMENT ON COLUMN OPERACIONES_EXPEDIENTE.COD_USUARIO IS 'Código del usuario de Flexia que ha realizado la operación';
COMMENT ON COLUMN OPERACIONES_EXPEDIENTE.DESCRIPCION_OPERACION IS 'Documento xml con los cambios realizados en la operación';
COMMENT ON TABLE OPERACIONES_EXPEDIENTE IS 'Historico de operaciones del expediente';
COMMIT;


-- Tarea #214493: [CONS] Histórico de información movimientos expediente
-- Adrián Freixeiro
CREATE TABLE HIST_OPERACIONES_EXPEDIENTE (
	ID_PROCESO NUMBER(19,0) NOT NULL,
	ID_OPERACION NUMBER(19,0) NOT NULL,
	COD_MUNICIPIO NUMBER(3,0) NOT NULL, 
	EJERCICIO NUMBER(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR2(30) NOT NULL,
	TIPO_OPERACION NUMBER(2,0) NOT NULL,
	FECHA_OPERACION DATE NOT NULL,
	COD_USUARIO NUMBER(4,0),
	DESCRIPCION_OPERACION CLOB NOT NULL,
	CONSTRAINT PKH_OPERACIONES_EXPEDIENTE PRIMARY KEY(ID_OPERACION),
	CONSTRAINT "FKH_OPERACIONES_EXPEDIENTE_IDP" FOREIGN KEY("ID_PROCESO") REFERENCES "EXP_ENVIO_HISTORICO"("ID") ON DELETE CASCADE
);


COMMENT ON COLUMN HIST_OPERACIONES_EXPEDIENTE.ID_PROCESO IS 'Identificador de ejecución del proceso de paso a histórico';
COMMENT ON COLUMN HIST_OPERACIONES_EXPEDIENTE.ID_OPERACION IS 'Clave autogenerada de la tabla';
COMMENT ON COLUMN HIST_OPERACIONES_EXPEDIENTE.COD_MUNICIPIO IS 'Código de municipio/organización';
COMMENT ON COLUMN HIST_OPERACIONES_EXPEDIENTE.EJERCICIO IS 'Ejercicio del expediente';
COMMENT ON COLUMN HIST_OPERACIONES_EXPEDIENTE.NUM_EXPEDIENTE IS 'Número del expediente';
COMMENT ON COLUMN HIST_OPERACIONES_EXPEDIENTE.TIPO_OPERACION IS 'Tipo de operación';
COMMENT ON COLUMN HIST_OPERACIONES_EXPEDIENTE.FECHA_OPERACION IS 'Fecha y hora en la que se realiza la operación';
COMMENT ON COLUMN HIST_OPERACIONES_EXPEDIENTE.COD_USUARIO IS 'Código del usuario de Flexia que ha realizado la operación';
COMMENT ON COLUMN HIST_OPERACIONES_EXPEDIENTE.DESCRIPCION_OPERACION IS 'Documento xml con los cambios realizados en la operación';
COMMENT ON TABLE HIST_OPERACIONES_EXPEDIENTE IS 'Operaciones de expedientes de histórico';
COMMIT;




-- Tareas #208822 Desplegables externos: No se almacena el código del valor.
-- Milagros Noya (11/11/2015)
ALTER TABLE E_TDEX
ADD TDEX_CODSEL VARCHAR2(50 BYTE);
COMMIT;

COMMENT ON COLUMN E_TDEX.TDEX_CODSEL IS 'Código correspondiente al valor seleccionado en el desplegable';
COMMIT;

ALTER TABLE E_TDEXT
ADD TDEXT_CODSEL VARCHAR2(50 BYTE);
COMMIT;

COMMENT ON COLUMN E_TDEXT.TDEXT_CODSEL IS 'Código correspondiente al valor seleccionado en el desplegable';
COMMIT;

-- Tareas #208822 Desplegables externos: No se almacena el código del valor.
-- Milagros Noya (16/11/2015)
ALTER TABLE HIST_E_TDEX
ADD TDEX_CODSEL VARCHAR2(50 BYTE);
COMMIT;

COMMENT ON COLUMN HIST_E_TDEX.TDEX_CODSEL IS 'Código correspondiente al valor seleccionado en el desplegable';
COMMIT;

ALTER TABLE HIST_E_TDEXT
ADD TDEXT_CODSEL VARCHAR2(50 BYTE);
COMMIT;

COMMENT ON COLUMN HIST_E_TDEXT.TDEXT_CODSEL IS 'Código correspondiente al valor seleccionado en el desplegable';
COMMIT;
 
 
 


ALTER TABLE R_TIPOASUNTO ADD (
BLOQUEAR_DESTINO NUMBER DEFAULT 0 NOT NULL
CONSTRAINT BLOQUEAR_DESTINO CHECK (BLOQUEAR_DESTINO in (0,1))
);


commit;





 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.09 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.10 --------------------------
-----------------------------------------------------------------------


 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.10 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.11 --------------------------
-----------------------------------------------------------------------



-- Tareas #281299: USC- Ampliar tama?o descrici?n unidades a 120

-- Jorge Garcia (20/06/2017)
	
ALTER TABLE A_UOR MODIFY (UOR_NOM VARCHAR2(120));
	
ALTER TABLE T_TER MODIFY (TER_NOM VARCHAR2(120));

ALTER TABLE T_HTE MODIFY (HTE_NOM VARCHAR2(120));

-- FIN Tareas #281299: USC- Ampliar tama?o descrici?n unidades a 120



 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.11 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.12 --------------------------
-----------------------------------------------------------------------

--Tarea #281562: Notificaciones fin plazo trámite - Envío correos a usuario que inició el trámite o el expediente
ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UITI CHAR(1 byte) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UITI CHECK (TRA_NOTIF_UITI in ('S','N')));
COMMENT ON COLUMN E_TRA.TRA_NOTIF_UITI IS 'Notificar por correo electrónico al usuario que inició el trámite al iniciar';

ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UITF CHAR(1 byte) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UITF CHECK (TRA_NOTIF_UITF in ('S','N')));
COMMENT ON COLUMN E_TRA.TRA_NOTIF_UITF IS 'Notificar por correo electrónico al usuario que inició el trámite al finalizar';

ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UIEI CHAR(1 byte) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UIEI CHECK (TRA_NOTIF_UIEI in ('S','N')));
COMMENT ON COLUMN E_TRA.TRA_NOTIF_UIEI IS 'Notificar por correo electrónico al usuario que inició el expediente al iniciar';

ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UIEF CHAR(1 byte) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UIEF CHECK (TRA_NOTIF_UIEF in ('S','N')));
COMMENT ON COLUMN E_TRA.TRA_NOTIF_UIEF IS 'Notificar por correo electrónico al usuario que inició el expediente al finalizar';


-- Tareas #279909: [CONS] Notificaciones en ficha de expediente
	
-- Milagros Noya (08/06/2017)

ALTER TABLE E_TRA ADD TRA_NOTIFICADO NUMBER(1,0) DEFAULT 0 NOT NULL;

COMMENT ON COLUMN E_TRA.TRA_NOTIFICADO IS 'Indica si el trámite acepta algún tipo de notificación, ya sea postal o electrónica. 0-NO; 1-SI';

COMMIT;

 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.12 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.13 --------------------------
-----------------------------------------------------------------------


create index OPERACIONES_EXPEDIENTE_IDX1 on OPERACIONES_EXPEDIENTE 
(COD_MUNICIPIO   ASC,
EJERCICIO ASC,
NUM_EXPEDIENTE ASC)


create index OPERACIONES_EXPEDIENTE_IDX2 on OPERACIONES_EXPEDIENTE 
(COD_MUNICIPIO   ASC,
NUM_EXPEDIENTE ASC)


 -- Tareas #245961: Baja lógica de valores de desplegables de campos suplementarios de expediente y tr?mite

ALTER TABLE E_DES_VAL ADD (	
DES_VAL_ESTADO varchar2(1 byte) DEFAULT 'A' NOT NULL	
CONSTRAINT DES_VAL_ESTADO CHECK (DES_VAL_ESTADO in ('A','B'))	
);
	
COMMENT ON COLUMN E_DES_VAL.DES_VAL_ESTADO IS 'Indica si el valor del campo desplegable esta de alta ("A") o se ha dado de baja lógica ("B")'



-- Tarea #283061
	
ALTER TABLE E_TRA ADD( 
	TRA_NOTIF_USUEXP_FINPLAZO CHAR(1 byte) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_USUEXP_FINPLAZO CHECK (TRA_NOTIF_USUEXP_FINPLAZO in ('S','N')));
COMMENT ON COLUMN E_TRA.TRA_NOTIF_USUEXP_FINPLAZO IS 'Notificar por correo electronico al usuario que inicio el tramite en funcion de fin de plazo';

ALTER TABLE E_TRA ADD( 
	TRA_NOTIF_USUTRA_FINPLAZO CHAR(1 byte) DEFAULT 'N' NOT NULL
	CONSTRAINT TRA_NOTIF_USUTRA_FINPLAZO CHECK (TRA_NOTIF_USUTRA_FINPLAZO in ('S','N')));
	COMMENT ON COLUMN E_TRA.TRA_NOTIF_USUEXP_FINPLAZO IS 'Notificar por correo electronico al usuario que inicio el expediente en funcion de fin de plazo';		

	ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UOR_FINPLAZO CHAR(1 byte) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UOR_FINPLAZO CHECK (TRA_NOTIF_UOR_FINPLAZO in ('S','N')));
COMMENT ON COLUMN E_TRA.TRA_NOTIF_UOR_FINPLAZO IS 'Notificar por correo electronico a la UOR en funcion fecha fin plazo';

UPDATE E_TRA SET TRA_NOTIF_UOR_FINPLAZO ='S' WHERE TRA_NOTCERCAFP=1 OR TRA_NOTFUERADP=1;


commit;



 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.13 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.14 --------------------------
-----------------------------------------------------------------------

-- #263634: Correcciones Flexia V16
	
-- Milagros Noya (07/09/2017)

COMMENT ON COLUMN R_RES.RES_ENR IS 'Entrada relacionada'



-- Tareas #282307: Lanbide - A?adir columna de asunto en buz?n de entradas
	
-- Jorge Garcia (29/06/2017)
	
INSERT INTO A_CAMPLIST (CAMPLIST_COD,CAMPLIST_NOM,CAMPLIST_TAMANO,CAMPLIST_ACTIVO,CAMPLIST_CODLIST,CAMPLIST_ORDEN,CAMPLIST_COLUMNA) VALUES (15,'CODIGO ASUNTO CODIFICADO',0,0,4,21,null);

INSERT INTO A_CAMPLIST (CAMPLIST_COD,CAMPLIST_NOM,CAMPLIST_TAMANO,CAMPLIST_ACTIVO,CAMPLIST_CODLIST,CAMPLIST_ORDEN,CAMPLIST_COLUMNA) VALUES (16,'DESCRIPCION ASUNTO CODIFICADO',0,0,4,22,null);

UPDATE A_CAMPLIST SET CAMPLIST_ORDEN = -1 WHERE CAMPLIST_COD=1 AND CAMPLIST_CODLIST=4;

UPDATE A_CAMPLIST SET CAMPLIST_ORDEN = 4 WHERE CAMPLIST_COD=6 AND CAMPLIST_CODLIST=4;

UPDATE A_CAMPLIST SET CAMPLIST_ORDEN = 14 WHERE CAMPLIST_COD=11 AND CAMPLIST_CODLIST=4;

UPDATE A_CAMPLIST SET CAMPLIST_ORDEN = -1 WHERE CAMPLIST_COD=12 AND CAMPLIST_CODLIST=4;

UPDATE A_CAMPLIST SET CAMPLIST_ORDEN = 13 WHERE CAMPLIST_COD=14 AND CAMPLIST_CODLIST=4;

-- FIN Tareas #282307: Lanbide - A?adir columna de asunto en buz?n de entradas




 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.14 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.15 --------------------------
-----------------------------------------------------------------------



 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.15 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.16 --------------------------
-----------------------------------------------------------------------
--Tarea 289525
INSERT INTO R_CLASIFICACION_ASUNTO (CODIGO, DESCRIPCION,UNIDAD_REGISTRO) VALUES ('0', 'ASUNTO POR DEFECTO','-1');  -- Para que funcione el alta de asuntos para TODAS las unidades en el admin local



 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.16 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.17 --------------------------
-----------------------------------------------------------------------

-- Tarea #294209 [CONS] Buzón de entrada Exportación a Excel
INSERT INTO A_CAMPLIST(CAMPLIST_COD,CAMPLIST_NOM,CAMPLIST_TAMANO,CAMPLIST_ACTIVO,CAMPLIST_CODLIST,CAMPLIST_ORDEN)    
VALUES (17,'CHECK',3,1,4,-1);


-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.17 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.18 --------------------------
-----------------------------------------------------------------------

-- Tarea #269404: Cotejo Electrónico
-- Jorge Garcia (20/03/2017)
--TABLAS:
CREATE TABLE METADATO_ESTADO_ELABORACION (
	ID NUMBER(3,0) NOT NULL,
	CODIGO VARCHAR2(4 BYTE) NOT NULL,
	DESCRIPCION VARCHAR2(255 BYTE) NOT NULL,
	
	CONSTRAINT METADATO_ESTADO_ELABORACION_PK PRIMARY KEY (ID),
	CONSTRAINT METADATO_EST_ELAB_CODIGO_UK UNIQUE (CODIGO)
);

COMMENT ON TABLE METADATO_ESTADO_ELABORACION IS 'Las naturalezas de un documento firmado electrónicamente';

CREATE TABLE METADATO_TIPO_DOCUMENTAL (
	ID NUMBER(3,0) NOT NULL,
	CODIGO VARCHAR2(4 BYTE) NOT NULL,
	DESCRIPCION VARCHAR2(255 BYTE) NOT NULL,
	
	CONSTRAINT METADATO_TIPO_DOCUMENTAL_PK PRIMARY KEY (ID),
	CONSTRAINT METADATO_TIPO_DOC_CODIGO_UK UNIQUE (CODIGO)
);

COMMENT ON TABLE METADATO_TIPO_DOCUMENTAL IS 'Tipos documentales de un documento firmado electrónicamente';

CREATE TABLE METADATO_TIPO_FIRMA (
	ID NUMBER(3,0) NOT NULL,
	CODIGO VARCHAR2(4 BYTE) NOT NULL,
	DESCRIPCION VARCHAR2(255 BYTE) NOT NULL,
	
	CONSTRAINT METADATO_TIPO_FIRMA_PK PRIMARY KEY (ID),
	CONSTRAINT METADATO_TIPO_FIRMA_CODIGO_UK UNIQUE (CODIGO)
);

COMMENT ON TABLE METADATO_TIPO_FIRMA IS 'Tipos de firmas que avalan un documento firmado electrónicamente';

CREATE TABLE METADATO_DOC_COTEJADOS (
	DEPARTAMENTO NUMBER(3,0) NOT NULL,
	UOR NUMBER(5,0) NOT NULL,
	EJERCICIO NUMBER(4,0) NOT NULL,
	NUMERO NUMBER(6,0) NOT NULL,
	TIPO_REGISTRO VARCHAR2(1 BYTE) NOT NULL, 
	NOMBRE_DOC VARCHAR2(255 BYTE) NOT NULL,
	VERSION_NTI VARCHAR2(255 BYTE) NOT NULL,
	ID_DOCUMENTO NUMBER(30) NOT NULL,
	ORGANO VARCHAR2(9 BYTE) NOT NULL,
	FECHA_CAPTURA DATE NOT NULL,
	ORIGEN NUMBER(1,0) NOT NULL,
	ESTADO_ELABORACION NUMBER(3,0) NOT NULL,
	NOMBRE_FORMATO VARCHAR2(255 BYTE) NOT NULL,
	TIPO_DOCUMENTAL NUMBER(3,0) NOT NULL,
	TIPO_FIRMA NUMBER(3,0) NOT NULL,
	
	CONSTRAINT METADATO_DOC_COTEJADOS_PK PRIMARY KEY (DEPARTAMENTO, UOR, EJERCICIO, NUMERO, TIPO_REGISTRO, NOMBRE_DOC),
	CONSTRAINT METADATO_ESTADO_ELABORACION_FK FOREIGN KEY (ESTADO_ELABORACION) REFERENCES METADATO_ESTADO_ELABORACION (ID),
	CONSTRAINT METADATO_TIPO_DOCUMENTAL_FK FOREIGN KEY (TIPO_DOCUMENTAL) REFERENCES METADATO_TIPO_DOCUMENTAL (ID),
	CONSTRAINT METADATO_TIPO_FIRMA_FK FOREIGN KEY (TIPO_FIRMA) REFERENCES METADATO_TIPO_FIRMA (ID),
	CONSTRAINT DOC_COTEJADOS_TO_R_RED_FK FOREIGN KEY (DEPARTAMENTO, UOR, EJERCICIO, NUMERO, TIPO_REGISTRO, NOMBRE_DOC) REFERENCES R_RED (RED_DEP, RED_UOR, RED_EJE, RED_NUM, RED_TIP, RED_NOM_DOC)
);

COMMENT ON COLUMN METADATO_DOC_COTEJADOS.DEPARTAMENTO IS 'Departamento';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.UOR IS 'Departamento';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.EJERCICIO IS 'Ejercicio';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.NUMERO IS 'Numero';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.TIPO_REGISTRO IS 'Tipo de registro de entrada o de salida (E/S)';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.NOMBRE_DOC IS 'Nombre del documento';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.VERSION_NTI IS 'Versión de la Norma Técnica de Interoperabilidad de Documento electrónico conforme a la cual se estructura el documento electrónico';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.ID_DOCUMENTO IS 'ID específico del IDENTIFICADOR de los meta datos. Para construir el código completo: ES_<ORGANO>_<EJERCICIO>_<ID_DOCUMENTO>';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.ORGANO IS 'Identificador normalizado de la administración generadora del documento o que realiza la captura del mismo';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.FECHA_CAPTURA IS 'Fecha de captura del documento en el sistema';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.ORIGEN IS 'Indica si el contenido del documento fue creado por el ciudadano (0) o por una administración (1)';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.ESTADO_ELABORACION IS 'Indica la naturaleza del documento';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.NOMBRE_FORMATO IS 'Formato lógico del fichero de contenido del documento electrónico';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.TIPO_DOCUMENTAL IS 'Descripción del tipo documental del documento';
COMMENT ON COLUMN METADATO_DOC_COTEJADOS.TIPO_FIRMA IS 'Indica el tipo de firma que avala el documento';
COMMENT ON TABLE METADATO_DOC_COTEJADOS IS 'Metadatos de los documentos cotejados';

--Sequencia
CREATE SEQUENCE metadato_id_documento
START WITH     1
INCREMENT BY   1
NOCACHE
NOCYCLE;

--Valores de las tablas:
INSERT INTO METADATO_ESTADO_ELABORACION (ID, CODIGO, DESCRIPCION) VALUES (1, 'EE01', 'Original');
INSERT INTO METADATO_ESTADO_ELABORACION (ID, CODIGO, DESCRIPCION) VALUES (2, 'EE02', 'Copia electrónica auténtica con cambio de formato');
INSERT INTO METADATO_ESTADO_ELABORACION (ID, CODIGO, DESCRIPCION) VALUES (3, 'EE03', 'Copia electrónica auténtica de documento papel');
INSERT INTO METADATO_ESTADO_ELABORACION (ID, CODIGO, DESCRIPCION) VALUES (4, 'EE04', 'Copia electrónica parcial auténtica');
INSERT INTO METADATO_ESTADO_ELABORACION (ID, CODIGO, DESCRIPCION) VALUES (99, 'EE99', 'Otros');

INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (1, 'TD01', 'Resolución');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (2, 'TD02', 'Acuerdo');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (3, 'TD03', 'Contrato');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (4, 'TD04', 'Convenio');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (5, 'TD05', 'Declaración');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (6, 'TD06', 'Comunicación');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (7, 'TD07', 'Notificación');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (8, 'TD08', 'Publicación');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (9, 'TD09', 'Acuse de recibo');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (10, 'TD10', 'Acta');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (11, 'TD11', 'Certificado');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (12, 'TD12', 'Diligencia');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (13, 'TD13', 'Informe');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (14, 'TD14', 'Solicitud');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (15, 'TD15', 'Denuncia');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (16, 'TD16', 'Alegación');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (17, 'TD17', 'Recursos');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (18, 'TD18', 'Comunicación ciudadano');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (19, 'TD19', 'Factura');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (20, 'TD20', 'Otros incautados');
INSERT INTO METADATO_TIPO_DOCUMENTAL (ID, CODIGO, DESCRIPCION) VALUES (99, 'TD99', 'Otros');

INSERT INTO METADATO_TIPO_FIRMA (ID, CODIGO, DESCRIPCION) VALUES (1, 'TF01', 'CSV');
INSERT INTO METADATO_TIPO_FIRMA (ID, CODIGO, DESCRIPCION) VALUES (2, 'TF02', 'XAdES internally detached signature');
INSERT INTO METADATO_TIPO_FIRMA (ID, CODIGO, DESCRIPCION) VALUES (3, 'TF03', 'XAdES enveloped signature');
INSERT INTO METADATO_TIPO_FIRMA (ID, CODIGO, DESCRIPCION) VALUES (4, 'TF04', 'CAdES detached/explicit signature');
INSERT INTO METADATO_TIPO_FIRMA (ID, CODIGO, DESCRIPCION) VALUES (5, 'TF05', 'CAdES attached/implicit signature');
INSERT INTO METADATO_TIPO_FIRMA (ID, CODIGO, DESCRIPCION) VALUES (6, 'TF06', 'PAdES');




CREATE SEQUENCE metadato_documento_id_metadato
START WITH     1
INCREMENT BY   1
NOCACHE
NOCYCLE;

--TABLAS:
CREATE TABLE METADATO_DOCUMENTO (
   ID_METADATO NUMBER(10,0) NOT NULL,
   CSV VARCHAR2(30),
   CSV_APLICACION VARCHAR2(100),
   CSV_URI VARCHAR2(512),

   CONSTRAINT METADATO_DOC_PK PRIMARY KEY (ID_METADATO)
);

CREATE UNIQUE INDEX METADATO_DOC_CSV_UK ON METADATO_DOCUMENTO(CSV);

COMMENT ON COLUMN METADATO_DOCUMENTO.ID_METADATO IS 'Identificador de los metadatos';
COMMENT ON COLUMN METADATO_DOCUMENTO.CSV IS 'Código seguro de verificación';
COMMENT ON COLUMN METADATO_DOCUMENTO.CSV_APLICACION IS 'Aplicación desde donde se ha generado el CSV';
COMMENT ON COLUMN METADATO_DOCUMENTO.CSV_URI IS 'URI de descarga del documento asociado al CSV';

ALTER TABLE E_DOC_EXT ADD DOC_EXT_ID_METADATO NUMBER(10,0);
ALTER TABLE E_TFI ADD TFI_ID_METADATO NUMBER(10,0);
ALTER TABLE E_TFIT ADD TFIT_ID_METADATO NUMBER(10,0);
ALTER TABLE E_CRD ADD CRD_ID_METADATO NUMBER(10,0);
ALTER TABLE E_CRD MODIFY (CRD_DOT NULL);

COMMENT ON COLUMN E_DOC_EXT.DOC_EXT_ID_METADATO IS 'Identificador de los metadatos de la tabla METADATO_DOCUMENTO';
COMMENT ON COLUMN E_TFI.TFI_ID_METADATO IS 'Identificador de los metadatos de la tabla METADATO_DOCUMENTO';
COMMENT ON COLUMN E_TFIT.TFIT_ID_METADATO IS 'Identificador de los metadatos de la tabla METADATO_DOCUMENTO';
COMMENT ON COLUMN E_CRD.CRD_ID_METADATO IS 'Identificador de los metadatos de la tabla METADATO_DOCUMENTO';

--Indices
CREATE UNIQUE INDEX E_DOC_EXT_ID_METADATO_UK ON E_DOC_EXT (DOC_EXT_ID_METADATO);
CREATE UNIQUE INDEX E_TFI_ID_METADATO_UK ON E_TFI (TFI_ID_METADATO);
CREATE UNIQUE INDEX E_TFIT_ID_METADATO_UK ON E_TFIT (TFIT_ID_METADATO);
CREATE UNIQUE INDEX E_CRD_ID_METADATO_UK ON E_CRD (CRD_ID_METADATO);


ALTER TABLE R_RED ADD RED_ID_METADATO NUMBER(10,0);
COMMENT ON COLUMN R_RED.RED_ID_METADATO IS 'Identificador de los metadatos de la tabla METADATO_DOCUMENTO';

 CREATE UNIQUE INDEX R_RED_ID_METADATO_UK ON R_RED (RED_ID_METADATO);

-- Tarea #269406: [CONS] Integración con Port@firmas
-- Jorge Garcia (31/08/2017)

-- Firma tipo:
CREATE TABLE FIRMA_TIPO (
	ID NUMBER(3,0) NOT NULL,
	NOMBRE VARCHAR2(100 BYTE) NOT NULL,
	
	CONSTRAINT FIRMA_TIPO_PK PRIMARY KEY (ID),
	CONSTRAINT FIRMA_TIPO_UK UNIQUE (NOMBRE)
);

COMMENT ON TABLE FIRMA_TIPO IS 'Tipos de firma';
COMMENT ON COLUMN FIRMA_TIPO.ID IS 'Identificador del tipo de firma';
COMMENT ON COLUMN FIRMA_TIPO.NOMBRE IS 'Nombre del tipo de firma';

INSERT INTO FIRMA_TIPO (ID, NOMBRE) VALUES (1, 'CASCADA');
INSERT INTO FIRMA_TIPO (ID, NOMBRE) VALUES (2, 'PARALELA');

-- Firma flujo
CREATE SEQUENCE firma_flujo_id
START WITH     1
INCREMENT BY   1
NOCACHE
NOCYCLE;

CREATE TABLE FIRMA_FLUJO (
	ID NUMBER(9,0) NOT NULL,
	NOMBRE VARCHAR2(100 BYTE) NOT NULL,
	ID_TIPO NUMBER(3,0) NOT NULL,
	ACTIVO NUMBER(1,0) NOT NULL,
	
	CONSTRAINT FIRMA_FLUJO_PK PRIMARY KEY (ID),
	CONSTRAINT FIRMA_FLUJO_UK UNIQUE (NOMBRE),
	CONSTRAINT FIRMA_FLUJO_FIRMA_TIPO_FK FOREIGN KEY (ID_TIPO) REFERENCES FIRMA_TIPO (ID)
);

COMMENT ON TABLE FIRMA_FLUJO IS 'Flujos de firma';
COMMENT ON COLUMN FIRMA_FLUJO.ID IS 'Identificador del flujo de firma';
COMMENT ON COLUMN FIRMA_FLUJO.NOMBRE IS 'Nombre del flujo de firma';
COMMENT ON COLUMN FIRMA_FLUJO.ID_TIPO IS 'Identificador del tipo de firma (FIRMA_TIPO)';
COMMENT ON COLUMN FIRMA_FLUJO.ACTIVO IS 'Determina si el flujo de firma esta activo o no';

-- Circuito firmas
CREATE TABLE FIRMA_CIRCUITO (
	ID_FIRMA_FLUJO NUMBER(9,0) NOT NULL,
	ID_USUARIO NUMBER(4,0) NOT NULL,
	ORDEN NUMBER(3, 0) NOT NULL,

	CONSTRAINT FIRMA_CIRCUITO_PK UNIQUE (ID_FIRMA_FLUJO, ID_USUARIO),
	CONSTRAINT FIRMA_CIRCUITO_FIRMA_FLUJO_FK FOREIGN KEY (ID_FIRMA_FLUJO) REFERENCES FIRMA_FLUJO (ID)
);

COMMENT ON TABLE FIRMA_CIRCUITO IS 'Circuitos de firma';
COMMENT ON COLUMN FIRMA_CIRCUITO.ID_FIRMA_FLUJO IS 'Identificador del flujo de firma (FIRMA_FLUJO)';
COMMENT ON COLUMN FIRMA_CIRCUITO.ID_USUARIO IS 'Identificador del usuario (esquema generico A_USU)';
COMMENT ON COLUMN FIRMA_CIRCUITO.ORDEN IS 'Orden que posee el usuario dentro del circuito de firmas';

ALTER TABLE E_DOT_FIR ADD DOT_FLUJO NUMBER(9);

-- Flujos personalizados de trámites
CREATE TABLE E_CRD_FIR_FLUJO (
	COD_MUNICIPIO NUMBER(3,0) NOT NULL,
	COD_PROCEDIMIENTO VARCHAR2(5 BYTE) NOT NULL,
	EJERCICIO NUMBER(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR2(30 BYTE) NOT NULL,
	COD_TRAMITE NUMBER(4,0) NOT NULL,
	COD_OCURRENCIA NUMBER(4,0) NOT NULL,
	COD_DOCUMENTO NUMBER(4,0) NOT NULL,
	ID_TIPO_FIRMA NUMBER(9,0) NOT NULL,
	
	CONSTRAINT E_CRD_FIR_FLUJO_PK PRIMARY KEY (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO),
    CONSTRAINT E_CRD_FIR_FLUJO_FK FOREIGN KEY (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO)
        REFERENCES E_CRD (CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD),
	CONSTRAINT E_CRD_FIR_FLUJO_TIPO_FK FOREIGN KEY (ID_TIPO_FIRMA) REFERENCES FIRMA_TIPO (ID)
);

COMMENT ON TABLE E_CRD_FIR_FLUJO IS 'Flujos de firmas personalizados de los documentos de trámites (E_CRD)';
COMMENT ON COLUMN E_CRD_FIR_FLUJO.COD_MUNICIPIO IS 'Código municipio';
COMMENT ON COLUMN E_CRD_FIR_FLUJO.COD_PROCEDIMIENTO IS 'Código procedimiento';
COMMENT ON COLUMN E_CRD_FIR_FLUJO.EJERCICIO IS 'Ejercicio';
COMMENT ON COLUMN E_CRD_FIR_FLUJO.NUM_EXPEDIENTE IS 'Número de expediente';
COMMENT ON COLUMN E_CRD_FIR_FLUJO.COD_TRAMITE IS 'Código de trámite';
COMMENT ON COLUMN E_CRD_FIR_FLUJO.COD_OCURRENCIA IS 'Código de ocurrencia del trámite';
COMMENT ON COLUMN E_CRD_FIR_FLUJO.COD_DOCUMENTO IS 'Código del documento';
COMMENT ON COLUMN E_CRD_FIR_FLUJO.ID_TIPO_FIRMA IS 'Id del tipo de firma';

-- Circuitos de los flujos personalizados de trámites
CREATE TABLE E_CRD_FIR_FIRMANTES (
	COD_MUNICIPIO NUMBER(3,0) NOT NULL,
	COD_PROCEDIMIENTO VARCHAR2(5 BYTE) NOT NULL,
	EJERCICIO NUMBER(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR2(30 BYTE) NOT NULL,
	COD_TRAMITE NUMBER(4,0) NOT NULL,
	COD_OCURRENCIA NUMBER(4,0) NOT NULL,
	COD_DOCUMENTO NUMBER(4,0) NOT NULL,
	ID_USUARIO NUMBER(4,0) NOT NULL,
	ORDEN NUMBER(3, 0) NOT NULL,
    ESTADO_FIRMA CHAR(1) DEFAULT 'O' NOT NULL,
    FECHA_FIRMA TIMESTAMP,
    FIRMA BLOB,

	CONSTRAINT E_CRD_FIR_FIRMANTES_PK PRIMARY KEY (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO, ID_USUARIO),
    CONSTRAINT E_CRD_FIR_FIRMANTES_FK FOREIGN KEY (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO)
        REFERENCES E_CRD (CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD)
);

COMMENT ON TABLE E_CRD_FIR_FIRMANTES IS 'Usuarios firmantes de un documento de trámites';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.COD_MUNICIPIO IS 'Código municipio';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.COD_PROCEDIMIENTO IS 'Código procedimiento';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.EJERCICIO IS 'Ejercicio';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.NUM_EXPEDIENTE IS 'Número de expediente';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.COD_TRAMITE IS 'Código de trámite';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.COD_OCURRENCIA IS 'Código de ocurrencia del trámite';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.COD_DOCUMENTO IS 'Código del documento';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.ID_USUARIO IS 'Identificador del usuario (esquema generico A_USU)';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.ORDEN IS 'Orden que posee el usuario dentro del circuito de firmas';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.ESTADO_FIRMA IS 'Estado de la firma (O = Pendiente, R = Rechazado, F = Firmado)';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.FECHA_FIRMA IS 'Fecha en la que se ha realizado la firma';
COMMENT ON COLUMN E_CRD_FIR_FIRMANTES.FIRMA IS 'Firma del usuario';

-- Tarea #287087: SEDE- SSREYES: WSRegistro. Insertar documento registro con CSV
-- Jorge Garcia (07/09/2017)

CREATE TABLE AUTH_EXTERNOS (
   ID decimal(5,0) NOT NULL,
   NOMBRE varchar(50 BYTE) NOT NULL,
   USUARIO varchar(30) NOT NULL,
   PASSWORD varchar(50) NOT NULL,
   ESTADO decimal(1,0) NOT NULL,
   INTENTOS_FALLIDOS decimal(2,0) NOT NULL,
   FECHA_ALTA timestamp NOT NULL,
   FECHA_MODIFICACION timestamp,
   FECHA_BAJA timestamp,
   CONSTRAINT AUTH_EXTERNOS_PK PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX AUTH_EXTERNOS_UK ON AUTH_EXTERNOS(USUARIO);

COMMENT ON TABLE AUTH_EXTERNOS IS 'Credenciales de usuarios/aplicaciones externos que accedan a la aplicación';
COMMENT ON COLUMN AUTH_EXTERNOS.ID IS 'Identificador del usuario/aplicacion';
COMMENT ON COLUMN AUTH_EXTERNOS.NOMBRE IS 'Nombre del usuario/aplicacion';
COMMENT ON COLUMN AUTH_EXTERNOS.USUARIO IS 'Login del usuario/aplicacion';
COMMENT ON COLUMN AUTH_EXTERNOS.PASSWORD IS 'Contraseña del usuario/aplicacion';
COMMENT ON COLUMN AUTH_EXTERNOS.ESTADO IS 'Estado del usuario: 0 = NO ACTIVO, 1 = ACTIVO, 2 = BLOQUEADO ';
COMMENT ON COLUMN AUTH_EXTERNOS.INTENTOS_FALLIDOS IS 'Número de intentos de acceso fallidos';
COMMENT ON COLUMN AUTH_EXTERNOS.FECHA_ALTA IS 'Fecha de alta del usuario/aplicacion';
COMMENT ON COLUMN AUTH_EXTERNOS.FECHA_MODIFICACION IS 'Fecha de modificacion del usuario/aplicacion';
COMMENT ON COLUMN AUTH_EXTERNOS.FECHA_BAJA IS 'Fecha de baja del usuario/aplicacion';

CREATE SEQUENCE auth_token_externos_id
START WITH     1
INCREMENT BY   1
NOCACHE
NOCYCLE;

CREATE TABLE AUTH_TOKEN_EXTERNOS (
   ID decimal(9,0) NOT NULL,
   TOKEN varchar(30 BYTE) NOT NULL,
   ID_USUARIO decimal(5,0) NOT NULL,
   FECHA_CADUCIDAD timestamp,

   CONSTRAINT AUTH_TOKEN_EXTERNOS_PK PRIMARY KEY (ID),
   CONSTRAINT AUTH_TOKEN_EXTERNOS_FK FOREIGN KEY (ID_USUARIO)
       REFERENCES AUTH_EXTERNOS (ID)
);

CREATE UNIQUE INDEX AUTH_TOKEN_EXTERNOS_UK ON AUTH_TOKEN_EXTERNOS(TOKEN);

COMMENT ON TABLE AUTH_TOKEN_EXTERNOS IS 'Tokens generados para las operaciones a realizar por los usuarios que accedan a la aplicación';
COMMENT ON COLUMN AUTH_TOKEN_EXTERNOS.TOKEN IS 'Token generado';
COMMENT ON COLUMN AUTH_TOKEN_EXTERNOS.ID_USUARIO IS 'Identificador del usuario/aplicacion';
COMMENT ON COLUMN AUTH_TOKEN_EXTERNOS.FECHA_CADUCIDAD IS 'Fecha de caducidad del token';

CREATE TABLE CSV_PENDIENTES_PROCESAR (
   ID_TOKEN decimal(9,0) NOT NULL,
   CSV varchar(30 BYTE) NOT NULL,

   CONSTRAINT CSV_PENDIENTES_PROCESAR_PK PRIMARY KEY (ID_TOKEN, CSV),
   CONSTRAINT CSV_PENDIENTES_PROCESAR_FK FOREIGN KEY (ID_TOKEN)
       REFERENCES AUTH_TOKEN_EXTERNOS (ID)
);

COMMENT ON TABLE CSV_PENDIENTES_PROCESAR IS 'Codigos seguros de verificación pendientes de procesar';
COMMENT ON COLUMN CSV_PENDIENTES_PROCESAR.ID_TOKEN IS 'Identificador del token de autenticación';
COMMENT ON COLUMN CSV_PENDIENTES_PROCESAR.CSV IS 'Código seguro de verificación';

-- Tarea #303919 Lanbide - Problema con bloqueos de transacción en tramitación - Retardos en carga de datos
CREATE UNIQUE INDEX IDX_EXP_NEPM ON E_EXP (EXP_NUM, EXP_EJE, EXP_PRO, EXP_MUN);
CREATE INDEX IDX_CRO_NEPM ON E_CRO (CRO_NUM, CRO_EJE, CRO_PRO, CRO_MUN);

-- Tarea #302601 - Lanbide - Buzón de entrada - Alta de expediente con el año del registro que lo inicia
-- Mila Noya (06/02/2018)
ALTER TABLE E_PRO
ADD PRO_EXPNUMANOT NUMBER(1,0) DEFAULT 0 NOT NULL;
COMMENT ON COLUMN E_PRO.PRO_EXPNUMANOT IS 'Indica si los expedientes del procedimiento iniciados desde el buzón toman el año de esta para obtener la numeración del expediente. 1:si; 0:no';

COMMIT;


-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.18 --------------------------
-----------------------------------------------------------------------

-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.19 --------------------------
-----------------------------------------------------------------------

-- Tarea #287835: Lanbide - Marca de digitalización de documentos de registro por procedimiento

ALTER TABLE PLUGIN_DOC_PROCEDIMIENTO
ADD "DIGIT_DOC_REGISTRO" VARCHAR2(2 BYTE) DEFAULT 'NO' NOT NULL;
COMMIT;


--Tarea #300045: Compulsa - Registro - Botón de fin de digitalización de documentos
ALTER TABLE R_RES ADD FIN_DIGITALIZACION NUMBER(1, 0) DEFAULT 1;
COMMENT ON COLUMN R_RES.FIN_DIGITALIZACION IS 'Determina si se ha finalizado con la digitalización de documentos. 1 = finalizado, 0 = no finalizado';




-- Tarea #302264 Compulsa - Control de digitalización según unidad orgánica del usuario
ALTER TABLE A_UOR ADD UOR_DIGITALIZACION NUMBER(1, 0) DEFAULT 0;
COMMENT ON COLUMN A_UOR.UOR_DIGITALIZACION IS 'Indica si la UOR admite digitalización 1 = admite digitalización , 0 = No admite digitalización'


-- Tarea #318863: [Lanbide] - Compulsa - Modificación estructura de datos compulsa - V2
CREATE TABLE R_DOC_METADATOS (
    ID NUMBER(10, 0) NOT NULL,
    RED_DOC_ID NUMBER(10, 0) NOT NULL,
    ID_METADATO VARCHAR2(150 BYTE) NOT NULL,
    VALOR_METADATO VARCHAR2(255 CHAR),
    CONSTRAINT R_DOC_METADATOS_PK PRIMARY KEY (ID)
);
 
CREATE UNIQUE INDEX R_DOC_METADATOS_UK ON R_DOC_METADATOS (RED_DOC_ID, ID_METADATO);

COMMENT ON COLUMN R_DOC_METADATOS.ID IS 'Identificador secuencial y clave primaria de la tabla';
COMMENT ON COLUMN R_DOC_METADATOS.RED_DOC_ID IS 'Identificador del documento con el que esta relacionado el metadato';
COMMENT ON COLUMN R_DOC_METADATOS.ID_METADATO IS 'Identificador del metadato';
COMMENT ON COLUMN R_DOC_METADATOS.VALOR_METADATO IS 'Valor del metadato';
COMMENT ON TABLE R_DOC_METADATOS IS 'Datos de catalogación de los documentos de registro';

CREATE SEQUENCE SEQ_R_DOC_METADATOS INCREMENT BY 1 MAXVALUE 999999999999999999999999999 MINVALUE 1 CACHE 20;

-- MODIFICACIONES EN R_RED
ALTER TABLE R_RED
ADD RED_IDDOC_GESTOR VARCHAR2(200 BYTE) NULL;

COMMENT ON COLUMN R_RED.RED_IDDOC_GESTOR IS 'Id del documento en el gestor documental externo.';

ALTER TABLE R_RED
ADD RED_DOCDIGIT NUMBER(1,0) DEFAULT 0 NOT NULL;

COMMENT ON COLUMN R_RED.RED_DOCDIGIT IS 'Indica si el documento ha sido digitalizado (1) o si no (0).';

ALTER TABLE R_RED
ADD RED_TIPODOC_ID NUMBER(3,0) NULL;

COMMENT ON COLUMN R_RED.RED_TIPODOC_ID IS 'Identificador del tipo documental con el que se cataloga el documento.';

--1. Eliminar FK de METADATO_DOC_COTEJADOS que apunta a R_RED (¿Existe la tabla?)
ALTER TABLE METADATO_DOC_COTEJADOS
DROP CONSTRAINT DOC_COTEJADOS_TO_R_RED_FK;

--2. Eliminar PK de R_RED
ALTER TABLE R_RED
DROP PRIMARY KEY;

--3. Crear id secuencial  NULL -> cuando tenga datos pasarlo a NOT NULL
ALTER TABLE R_RED
ADD RED_DOC_ID NUMBER(10,0) NULL;

COMMENT ON COLUMN R_RED.RED_DOC_ID IS 'Identificador secuencial del documento y clave primaria de la tabla';

--4. Crear secuencia para ese id
CREATE SEQUENCE SEQ_RED_DOC_ID INCREMENT BY 1 MAXVALUE 999999999999999999999999999 MINVALUE 1 CACHE 20;

--5. Actualizar id con valor de secuencia para todas las filas
DECLARE
	CURSOR listaDocs IS
		select ROWID from R_RED;
BEGIN
  FOR doc IN listaDocs LOOP
	UPDATE R_RED SET RED_DOC_ID=SEQ_RED_DOC_ID.NEXTVAL WHERE ROWID=doc.ROWID;
  END LOOP;
  COMMIT;
END;
/

--6. Establecer id a NOT NULL y como PK
ALTER TABLE R_RED
MODIFY RED_DOC_ID NUMBER(10,0) NOT NULL;

ALTER TABLE R_RED
ADD CONSTRAINT R_RED_PK PRIMARY KEY (RED_DOC_ID);

--7. crear unique index para los campos de la pk de antes
CREATE UNIQUE INDEX R_RED_UK ON R_RED (RED_DEP, RED_UOR, RED_TIP, RED_EJE, RED_NUM, RED_NOM_DOC);

ALTER TABLE R_RED
ADD CONSTRAINT R_RED_UK_CONST
UNIQUE (RED_DEP, RED_UOR, RED_TIP, RED_EJE, RED_NUM, RED_NOM_DOC);

--8. rehacer fk de METADATO_DOC_COTEJADOS (¿Existe la tabla?)
ALTER TABLE METADATO_DOC_COTEJADOS
ADD CONSTRAINT DOC_COTEJADOS_TO_R_RED_FK
FOREIGN KEY (DEPARTAMENTO,UOR,TIPO_REGISTRO,EJERCICIO,NUMERO,NOMBRE_DOC) REFERENCES R_RED (RED_DEP, RED_UOR, RED_TIP, RED_EJE, RED_NUM, RED_NOM_DOC);

--9. Crear trigger para r_red para que antes de insertar obtenga el valor de la secuencia
CREATE OR REPLACE TRIGGER TR_RED_SEQID 
BEFORE INSERT ON R_RED 
FOR EACH ROW

BEGIN
 SELECT SEQ_RED_DOC_ID.NEXTVAL
 INTO   :new.RED_DOC_ID
 FROM   dual;
END;
/

--10. Crear fk para R_DOC_METADATOS a R_RED
ALTER TABLE R_DOC_METADATOS
ADD CONSTRAINT DOC_METADATOS_TO_R_RED_FK
FOREIGN KEY (RED_DOC_ID) REFERENCES R_RED (RED_DOC_ID);

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.19 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.20 --------------------------
-----------------------------------------------------------------------

-- Tareas #325617: [Lanbide] Compulsa y catalogación - desarrollos Altia Vitoria
    -- Tareas #323256: Compulsa - Mejoras en interfaz de catalogación (II)
    -- José Ignacio Zomosa (11/07/2018)
ALTER TABLE R_RED ADD RED_OBSERV VARCHAR2(1000 BYTE);
COMMENT ON COLUMN R_RED.RED_OBSERV IS 'Observaciones Documento';

COMMIT;
    
    -- Tareas: #330215 Compulsa - No llamar a retramitarDocumento() en registros migrados de RGI cuando se inicia expediente / asocia expediente
    -- David Guerrero (13/08/2018)
ALTER TABLE R_RED ADD (RED_MIGRA NUMBER (1) Default 0);
COMMENT ON COLUMN R_RED.RED_MIGRA IS 'Indica si el documento se ha migrado desde una aplicación externa.';

COMMIT;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.20 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.21 --------------------------
-----------------------------------------------------------------------

-- Tareas #320518 (0301218 Digitalización 2018): Compulsa - Mejoras en interfaz de catalogación 
-- José Ignacio Delgado (11/10/2018)
-- Esta tabla es propia de los entornos de Lanbide
ALTER TABLE MELANBIDE68_TIPDOC_LANBIDE ADD (COD_GRUPO_TIPDOC VARCHAR2(8 BYTE));
COMMENT ON COLUMN MELANBIDE68_TIPDOC_LANBIDE.COD_GRUPO_TIPDOC IS 'Código de Grupo de Tipo Documental';
COMMIT;

-- Tareas #344300: [Lanbide] Registro - Justificante - A�adir campo de asunto al justificante
-- Mila Noya (13/12/2018)
-- eliminamos funcion ejecutaJustificante
drop function ejecutaJustificante;

-- eliminamos tipo salidaJustificante
Drop Type Salidajustificante;

-- modificamos tipo anotacion
create or replace
TYPE anotacion
AS
  OBJECT
  (
    reg_fec         VARCHAR2(16),
    reg_fed         VARCHAR2(16),
    reg_fich        VARCHAR2(2000),
    Reg_Asu         Varchar2(4000),
    reg_codtipasu   varchar2(10),
    reg_tipasu      varchar2(100),
    reg_uor_vis     VARCHAR2(8),
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
    Domicilio_Interesado Varchar2(200)
);

-- creamos tipo salidaJustificante
Create Or Replace
Type Salidajustificante As Table Of Anotacion;

-- creamos funcion ejecutaJustificante
create or replace
FUNCTION ejecutaJustificante  (
      numero IN varchar2,
      uor    IN NUMBER,
      tipo   IN VARCHAR2) 
      RETURN salidaJustificante pipelined
      is


TYPE t_ref_cursor IS REF CURSOR;    
Cursorregistro        T_Ref_Cursor;
Lr_Out_Rec          Anotacion := Anotacion( NULL, Null, Null, Null, Null, Null, Null,Null, Null,Null, Null,Null, Null,Null, Null,
NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,NULL, NULL,null,null,null);

BEGIN

OPEN cursorRegistro FOR 
        SELECT DISTINCT TO_CHAR (res_fec, 'DD/MM/YYYY HH24:MI')               AS RES_FEC,
    TO_CHAR (res_fed, 'DD/MM/YYYY HH24:MI')                             AS RES_FED,
        lista_anexos_registro (res_uor,r_res.RES_NUM,r_res.RES_EJE,res_tip) AS anexos,
    Res_Asu,
    r_res.asunto,
    --r_res.asunto || ' - ' || tipoasu.descripcion as tipoasunto,
    tipoasu.descripcion as tipoasunto,
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
    Res_Tnv,
    r_res.procedimiento,
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
  flexia_generico.A_USU A_USU,
  R_Ext,
  E_Exr,
  r_tipoasunto tipoasu
WHERE r_ext.ext_ter = t_hte.hte_ter(+)
AND r_ext.ext_nvr   = t_hte.hte_nvr(+)
AND res_uod         = uor_cod(+)
AND res_tdo         = tdo_ide(+)
AND res_tpe         = tpe_ide(+)
AND res_ttr         = ttr_ide(+)
And A_Usu.Usu_Cod   = R_Res.Res_Usu
and r_res.asunto    = tipoasu.codigo
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
            Into 
                Lr_Out_Rec.Reg_Fec,    
                Lr_Out_Rec.Reg_Fed,
                Lr_Out_Rec.Reg_Fich,
                Lr_Out_Rec.Reg_Asu,
                lr_out_rec.reg_codtipasu,
                lr_out_rec.reg_tipasu,
                Lr_Out_Rec.Reg_Uor_Vis,
                Lr_Out_Rec.Reg_Uor,
                Lr_Out_Rec.Reg_Coduor,
                Lr_Out_Rec.Reg_Uor_Nom,
                Lr_Out_Rec.Reg_Obs,
                Lr_Out_Rec.Reg_Int,
                Lr_Out_Rec.Reg_Tip,
                Lr_Out_Rec.Reg_Doc,
                Lr_Out_Rec.Reg_Doc_Nom,
                Lr_Out_Rec.Reg_Rem,
                Lr_Out_Rec.Reg_Rem_Nom,
                Lr_Out_Rec.Reg_Trp,
                Lr_Out_Rec.Reg_Trp_Nom,
                Lr_Out_Rec.Reg_Usu,
                Lr_Out_Rec.Reg_Num,
                Lr_Out_Rec.Reg_Docsint,
                Lr_Out_Rec.Reg_Mod,
                Lr_Out_Rec.Reg_Ter,
                Lr_Out_Rec.Reg_Interesado,
                Lr_Out_Rec.Cod_Tercero,
                Lr_Out_Rec.Version_Tercero,
                Lr_Out_Rec.Reg_Pro,
                Lr_Out_Rec.Reg_Exp,
                lr_out_rec.lista_interesados,
                lr_out_rec.lista_documentos_interesado,
                lr_out_rec.domicilio_interesado;
            
            EXIT WHEN cursorRegistro%NOTFOUND;
            PIPE ROW(lr_out_rec);
        END LOOP;
    CLOSE cursorRegistro;
    RETURN;

END ejecutaJustificante;

-- modificamos la sql que devuelve los datos que se necesitan en la plantilla jasper para generar el justificante
Update Estructurainforme 
Set Consultasql = 'SELECT REG_TIP AS TIPOREGISTRO, REG_FICH AS LISTAANEXOS,REG_INT AS NOMBREINTERESADO, REG_DOCSINT AS DOCUMENTOINTERESADO,LISTA_INTERESADOS AS LISTADOINTERESADOS,DOMICILIO_INTERESADO AS DOMICILIOINTERESADO, LISTA_DOCUMENTOS_INTERESADO AS DOCSINT, REG_CODTIPASU AS CODASUNTO, REG_TIPASU AS TIPOASUNTO, REG_ASU AS EXTRACTO,REG_UOR_VIS AS CODIGOVISIBLEUOR,REG_FEC AS FECHAENTRADA,REG_FED AS FECHAPRESENTACION,REG_USU AS NOMBREUSUARIORALIZOALTA,REG_UOR_NOM AS NOMBREUORDESTINO,REG_NUM AS NUMEROANOTACION,REG_DOC_NOM AS NOMBREDOCUMENTO,REG_REM_NOM AS NOMTIPOREMITENTE,REG_TRP_NOM AS NOMTIPOTRANSPORTE,REG_DOCSINT AS DOCSINT,REG_MOD AS MODO,REG_OBS AS OBSERVACIONES,REG_PRO AS PROCEDIMIENTO,REG_EXP AS EXPEDIENTE FROM table(ejecutaJustificante (?,?,?))' 
Where Cod_Estructura=86;
commit;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.21 --------------------------
-----------------------------------------------------------------------