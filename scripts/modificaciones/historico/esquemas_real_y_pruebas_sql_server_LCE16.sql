-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.00.01 --------------------------
-----------------------------------------------------------------------



-- Tarea #247202 Gesti?n de unidades org?nicas dadas de baja
	
-- Adri?n (13/09/2016)
	
ALTER TABLE A_UOR ADD UOR_OCULTA CHAR(1) DEFAULT 'N' NOT NULL;

	
COMMIT;

--#253742-Milagros Noya Peña.


ALTER TABLE E_EXR ADD EXR_FECHAINSMOD DATETIME2;

COMMIT;


--Tarea #253692: Nuevo campo de expediente para ubicación de documentación. Parametrizable
--Milagros Noya (14/11/2016)
ALTER TABLE E_EXP 
ADD EXP_UBICACION_DOC VARCHAR(10) DEFAULT NULL NULL;

ALTER TABLE HIST_E_EXP 
ADD EXP_UBICACION_DOC VARCHAR(10) DEFAULT NULL NULL;

COMMIT;



--Tarea #234108: Registro - Obligatoriedad documento interesado seg?n asunto
	
--Milagros Noya (10/05/2016)
	
ALTER TABLE R_TIPOASUNTO ADD DOCINT_OBLIGATORIO DECIMAL(1,0) DEFAULT 0;
	
	
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
   ("R_DOC_APORTADOS_DEP" DECIMAL(3,0) NOT NULL , 
	"R_DOC_APORTADOS_UOR" DECIMAL(5,0) NOT NULL , 
	"R_DOC_APORTADOS_EJE" DECIMAL(4,0) NOT NULL , 
	"R_DOC_APORTADOS_NUM" DECIMAL(6,0) NOT NULL , 
	"R_DOC_APORTADOS_TIP" VARCHAR(1) NOT NULL , 
	"R_DOC_APORTADOS_NOM_DOC" VARCHAR(255) NOT NULL , 
	"R_DOC_APORTADOS_TIP_DOC" VARCHAR(100), 
	"R_DOC_APORTADOS_ORGANO" VARCHAR(100), 
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
RES_SGA_COD VARCHAR(12) DEFAULT NULL,
RES_SGA_EXP VARCHAR(30) DEFAULT NULL);

--Tarea #271924
ALTER TABLE A_PLT ADD (
PLT_VIS_EXT VARCHAR(2) DEFAULT 'SI' NOT NULL
CONSTRAINT PLT_VIS_EXT CHECK (PLT_VIS_EXT in ('SI','NO'))
);

 
 
 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.06 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.07 --------------------------
-----------------------------------------------------------------------

-- tarea 120581
 
Alter table R_TDO ADD (TDO_EST DECIMAL(1,0) DEFAULT 1);

Alter table R_TDO ADD (TDO_FEC DATE);


 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.07 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.08 --------------------------
-----------------------------------------------------------------------

ALTER TABLE T_TER MODIFY TER_DCE VARCHAR(100);
ALTER TABLE T_HTE MODIFY HTE_DCE VARCHAR(100);


 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.08 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.09 --------------------------
-----------------------------------------------------------------------

CREATE TABLE OPERACIONES_EXPEDIENTE (
	ID_OPERACION DECIMAL(19,0) IDENTITY NOT NULL,
	COD_MUNICIPIO DECIMAL(3,0) NOT NULL, 
	EJERCICIO DECIMAL(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR(30) NOT NULL,
	TIPO_OPERACION DECIMAL(2,0) NOT NULL,
	FECHA_OPERACION DATE NOT NULL,
	COD_USUARIO DECIMAL(4,0),
	DESCRIPCION_OPERACION VARCHAR(MAX) NOT NULL,
	CONSTRAINT PK_OPERACIONES_EXPEDIENTE PRIMARY KEY(ID_OPERACION)
);

COMMIT;


-- Tarea #214493: [CONS] Histórico de información movimientos expediente
-- Adrián Freixeiro
CREATE TABLE HIST_OPERACIONES_EXPEDIENTE (
	ID_PROCESO DECIMAL(19,0) NOT NULL,
	ID_OPERACION DECIMAL(19,0) NOT NULL,
	COD_MUNICIPIO DECIMAL(3,0) NOT NULL, 
	EJERCICIO DECIMAL(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR(30) NOT NULL,
	TIPO_OPERACION DECIMAL(2,0) NOT NULL,
	FECHA_OPERACION DATE NOT NULL,
	COD_USUARIO DECIMAL(4,0),
	DESCRIPCION_OPERACION VARCHAR(MAX) NOT NULL,
	CONSTRAINT PKH_OPERACIONES_EXPEDIENTE PRIMARY KEY(ID_OPERACION),
	CONSTRAINT "FKH_OPERACIONES_EXPEDIENTE_IDP" FOREIGN KEY("ID_PROCESO") REFERENCES "EXP_ENVIO_HISTORICO"("ID") ON DELETE CASCADE
);


COMMIT;




-- Tareas #208822 Desplegables externos: No se almacena el código del valor.
-- Milagros Noya (11/11/2015)
ALTER TABLE E_TDEX
ADD TDEX_CODSEL VARCHAR(50);
COMMIT;

COMMIT;

ALTER TABLE E_TDEXT
ADD TDEXT_CODSEL VARCHAR(50);
COMMIT;

COMMIT;

-- Tareas #208822 Desplegables externos: No se almacena el código del valor.
-- Milagros Noya (16/11/2015)
ALTER TABLE HIST_E_TDEX
ADD TDEX_CODSEL VARCHAR(50);
COMMIT;

COMMIT;

ALTER TABLE HIST_E_TDEXT
ADD TDEXT_CODSEL VARCHAR(50);
COMMIT;

COMMIT;
 
 
 


ALTER TABLE R_TIPOASUNTO ADD (
BLOQUEAR_DESTINO DECIMAL DEFAULT 0 NOT NULL
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
	
ALTER TABLE A_UOR MODIFY (UOR_NOM VARCHAR(120));
	
ALTER TABLE T_TER MODIFY (TER_NOM VARCHAR(120));

ALTER TABLE T_HTE MODIFY (HTE_NOM VARCHAR(120));

-- FIN Tareas #281299: USC- Ampliar tama?o descrici?n unidades a 120



 -----------------------------------------------------------------------
--------------------- FIN PARCHE 16.11 --------------------------
-----------------------------------------------------------------------


-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.12 --------------------------
-----------------------------------------------------------------------

--Tarea #281562: Notificaciones fin plazo trámite - Envío correos a usuario que inició el trámite o el expediente
ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UITI CHAR(1) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UITI CHECK (TRA_NOTIF_UITI in ('S','N')));

ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UITF CHAR(1) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UITF CHECK (TRA_NOTIF_UITF in ('S','N')));

ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UIEI CHAR(1) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UIEI CHECK (TRA_NOTIF_UIEI in ('S','N')));

ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UIEF CHAR(1) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UIEF CHECK (TRA_NOTIF_UIEF in ('S','N')));


-- Tareas #279909: [CONS] Notificaciones en ficha de expediente
	
-- Milagros Noya (08/06/2017)

ALTER TABLE E_TRA ADD TRA_NOTIFICADO DECIMAL(1,0) DEFAULT 0 NOT NULL;


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
DES_VAL_ESTADO VARCHAR(1) DEFAULT 'A' NOT NULL	
CONSTRAINT DES_VAL_ESTADO CHECK (DES_VAL_ESTADO in ('A','B'))	
);
	



-- Tarea #283061
	
ALTER TABLE E_TRA ADD( 
	TRA_NOTIF_USUEXP_FINPLAZO CHAR(1) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_USUEXP_FINPLAZO CHECK (TRA_NOTIF_USUEXP_FINPLAZO in ('S','N')));

ALTER TABLE E_TRA ADD( 
	TRA_NOTIF_USUTRA_FINPLAZO CHAR(1) DEFAULT 'N' NOT NULL
	CONSTRAINT TRA_NOTIF_USUTRA_FINPLAZO CHECK (TRA_NOTIF_USUTRA_FINPLAZO in ('S','N')));
	COMMENT ON COLUMN E_TRA.TRA_NOTIF_USUEXP_FINPLAZO IS 'Notificar por correo electronico al usuario que inicio el expediente en funcion de fin de plazo';		

	ALTER TABLE E_TRA ADD( 
TRA_NOTIF_UOR_FINPLAZO CHAR(1) DEFAULT 'N' NOT NULL
CONSTRAINT TRA_NOTIF_UOR_FINPLAZO CHECK (TRA_NOTIF_UOR_FINPLAZO in ('S','N')));

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
	ID DECIMAL(3,0) NOT NULL,
	CODIGO VARCHAR(4) NOT NULL,
	DESCRIPCION VARCHAR(255) NOT NULL,
	
	CONSTRAINT METADATO_ESTADO_ELABORACION_PK PRIMARY KEY (ID),
	CONSTRAINT METADATO_EST_ELAB_CODIGO_UK UNIQUE (CODIGO)
);


CREATE TABLE METADATO_TIPO_DOCUMENTAL (
	ID DECIMAL(3,0) NOT NULL,
	CODIGO VARCHAR(4) NOT NULL,
	DESCRIPCION VARCHAR(255) NOT NULL,
	
	CONSTRAINT METADATO_TIPO_DOCUMENTAL_PK PRIMARY KEY (ID),
	CONSTRAINT METADATO_TIPO_DOC_CODIGO_UK UNIQUE (CODIGO)
);


CREATE TABLE METADATO_TIPO_FIRMA (
	ID DECIMAL(3,0) NOT NULL,
	CODIGO VARCHAR(4) NOT NULL,
	DESCRIPCION VARCHAR(255) NOT NULL,
	
	CONSTRAINT METADATO_TIPO_FIRMA_PK PRIMARY KEY (ID),
	CONSTRAINT METADATO_TIPO_FIRMA_CODIGO_UK UNIQUE (CODIGO)
);


CREATE TABLE METADATO_DOC_COTEJADOS (
	DEPARTAMENTO DECIMAL(3,0) NOT NULL,
	UOR DECIMAL(5,0) NOT NULL,
	EJERCICIO DECIMAL(4,0) NOT NULL,
	NUMERO DECIMAL(6,0) NOT NULL,
	TIPO_REGISTRO VARCHAR(1) NOT NULL, 
	NOMBRE_DOC VARCHAR(255) NOT NULL,
	VERSION_NTI VARCHAR(255) NOT NULL,
	ID_DOCUMENTO DECIMAL(30) IDENTITY NOT NULL,
	ORGANO VARCHAR(9) NOT NULL,
	FECHA_CAPTURA DATE NOT NULL,
	ORIGEN DECIMAL(1,0) NOT NULL,
	ESTADO_ELABORACION DECIMAL(3,0) NOT NULL,
	NOMBRE_FORMATO VARCHAR(255) NOT NULL,
	TIPO_DOCUMENTAL DECIMAL(3,0) NOT NULL,
	TIPO_FIRMA DECIMAL(3,0) NOT NULL,
	
	CONSTRAINT METADATO_DOC_COTEJADOS_PK PRIMARY KEY (DEPARTAMENTO, UOR, EJERCICIO, NUMERO, TIPO_REGISTRO, NOMBRE_DOC),
	CONSTRAINT METADATO_ESTADO_ELABORACION_FK FOREIGN KEY (ESTADO_ELABORACION) REFERENCES METADATO_ESTADO_ELABORACION (ID),
	CONSTRAINT METADATO_TIPO_DOCUMENTAL_FK FOREIGN KEY (TIPO_DOCUMENTAL) REFERENCES METADATO_TIPO_DOCUMENTAL (ID),
	CONSTRAINT METADATO_TIPO_FIRMA_FK FOREIGN KEY (TIPO_FIRMA) REFERENCES METADATO_TIPO_FIRMA (ID),
	CONSTRAINT DOC_COTEJADOS_TO_R_RED_FK FOREIGN KEY (DEPARTAMENTO, UOR, EJERCICIO, NUMERO, TIPO_REGISTRO, NOMBRE_DOC) REFERENCES R_RED (RED_DEP, RED_UOR, RED_EJE, RED_NUM, RED_TIP, RED_NOM_DOC)
);

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

--TABLAS:
CREATE TABLE METADATO_DOCUMENTO (
   ID_METADATO DECIMAL(10,0) IDENTITY NOT NULL,
   CSV VARCHAR(30),
   CSV_APLICACION VARCHAR(100),
   CSV_URI VARCHAR(512),

   CONSTRAINT METADATO_DOC_PK PRIMARY KEY (ID_METADATO)
);

CREATE UNIQUE INDEX METADATO_DOC_CSV_UK ON METADATO_DOCUMENTO(CSV);


ALTER TABLE E_DOC_EXT ADD DOC_EXT_ID_METADATO DECIMAL(10,0);
ALTER TABLE E_TFI ADD TFI_ID_METADATO DECIMAL(10,0);
ALTER TABLE E_TFIT ADD TFIT_ID_METADATO DECIMAL(10,0);
ALTER TABLE E_CRD ADD CRD_ID_METADATO DECIMAL(10,0);
ALTER TABLE E_CRD MODIFY (CRD_DOT NULL);


--Indices
CREATE UNIQUE INDEX E_DOC_EXT_ID_METADATO_UK ON E_DOC_EXT (DOC_EXT_ID_METADATO);
CREATE UNIQUE INDEX E_TFI_ID_METADATO_UK ON E_TFI (TFI_ID_METADATO);
CREATE UNIQUE INDEX E_TFIT_ID_METADATO_UK ON E_TFIT (TFIT_ID_METADATO);
CREATE UNIQUE INDEX E_CRD_ID_METADATO_UK ON E_CRD (CRD_ID_METADATO);


ALTER TABLE R_RED ADD RED_ID_METADATO DECIMAL(10,0);

 CREATE UNIQUE INDEX R_RED_ID_METADATO_UK ON R_RED (RED_ID_METADATO);

-- Tarea #269406: [CONS] Integración con Port@firmas
-- Jorge Garcia (31/08/2017)

-- Firma tipo:
CREATE TABLE FIRMA_TIPO (
	ID DECIMAL(3,0) NOT NULL,
	NOMBRE VARCHAR(100) NOT NULL,
	
	CONSTRAINT FIRMA_TIPO_PK PRIMARY KEY (ID),
	CONSTRAINT FIRMA_TIPO_UK UNIQUE (NOMBRE)
);


INSERT INTO FIRMA_TIPO (ID, NOMBRE) VALUES (1, 'CASCADA');
INSERT INTO FIRMA_TIPO (ID, NOMBRE) VALUES (2, 'PARALELA');

-- Firma flujo
CREATE TABLE FIRMA_FLUJO (
	ID DECIMAL(9,0) IDENTITY NOT NULL,
	NOMBRE VARCHAR(100) NOT NULL,
	ID_TIPO DECIMAL(3,0) NOT NULL,
	ACTIVO DECIMAL(1,0) NOT NULL,
	
	CONSTRAINT FIRMA_FLUJO_PK PRIMARY KEY (ID),
	CONSTRAINT FIRMA_FLUJO_UK UNIQUE (NOMBRE),
	CONSTRAINT FIRMA_FLUJO_FIRMA_TIPO_FK FOREIGN KEY (ID_TIPO) REFERENCES FIRMA_TIPO (ID)
);


-- Circuito firmas
CREATE TABLE FIRMA_CIRCUITO (
	ID_FIRMA_FLUJO DECIMAL(9,0) NOT NULL,
	ID_USUARIO DECIMAL(4,0) NOT NULL,
	ORDEN DECIMAL(3, 0) NOT NULL,

	CONSTRAINT FIRMA_CIRCUITO_PK UNIQUE (ID_FIRMA_FLUJO, ID_USUARIO),
	CONSTRAINT FIRMA_CIRCUITO_FIRMA_FLUJO_FK FOREIGN KEY (ID_FIRMA_FLUJO) REFERENCES FIRMA_FLUJO (ID)
);


ALTER TABLE E_DOT_FIR ADD DOT_FLUJO DECIMAL(9);

-- Flujos personalizados de trámites
CREATE TABLE E_CRD_FIR_FLUJO (
	COD_MUNICIPIO DECIMAL(3,0) NOT NULL,
	COD_PROCEDIMIENTO VARCHAR(5) NOT NULL,
	EJERCICIO DECIMAL(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR(30) NOT NULL,
	COD_TRAMITE DECIMAL(4,0) NOT NULL,
	COD_OCURRENCIA DECIMAL(4,0) NOT NULL,
	COD_DOCUMENTO DECIMAL(4,0) NOT NULL,
	ID_TIPO_FIRMA DECIMAL(9,0) NOT NULL,
	
	CONSTRAINT E_CRD_FIR_FLUJO_PK PRIMARY KEY (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO),
    CONSTRAINT E_CRD_FIR_FLUJO_FK FOREIGN KEY (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO)
        REFERENCES E_CRD (CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD),
	CONSTRAINT E_CRD_FIR_FLUJO_TIPO_FK FOREIGN KEY (ID_TIPO_FIRMA) REFERENCES FIRMA_TIPO (ID)
);


-- Circuitos de los flujos personalizados de trámites
CREATE TABLE E_CRD_FIR_FIRMANTES (
	COD_MUNICIPIO DECIMAL(3,0) NOT NULL,
	COD_PROCEDIMIENTO VARCHAR(5) NOT NULL,
	EJERCICIO DECIMAL(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR(30) NOT NULL,
	COD_TRAMITE DECIMAL(4,0) NOT NULL,
	COD_OCURRENCIA DECIMAL(4,0) NOT NULL,
	COD_DOCUMENTO DECIMAL(4,0) NOT NULL,
	ID_USUARIO DECIMAL(4,0) NOT NULL,
	ORDEN DECIMAL(3, 0) NOT NULL,
    ESTADO_FIRMA CHAR(1) DEFAULT 'O' NOT NULL,
    FECHA_FIRMA DATETIME2,
    FIRMA VARBINARY(MAX),

	CONSTRAINT E_CRD_FIR_FIRMANTES_PK PRIMARY KEY (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO, ID_USUARIO),
    CONSTRAINT E_CRD_FIR_FIRMANTES_FK FOREIGN KEY (COD_MUNICIPIO, COD_PROCEDIMIENTO, EJERCICIO, NUM_EXPEDIENTE, COD_TRAMITE, COD_OCURRENCIA, COD_DOCUMENTO)
        REFERENCES E_CRD (CRD_MUN, CRD_PRO, CRD_EJE, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD)
);


-- Tarea #287087: SEDE- SSREYES: WSRegistro. Insertar documento registro con CSV
-- Jorge Garcia (07/09/2017)

CREATE TABLE AUTH_EXTERNOS (
   ID decimal(5,0) NOT NULL,
   NOMBRE varchar(50) NOT NULL,
   USUARIO varchar(30) NOT NULL,
   PASSWORD varchar(50) NOT NULL,
   ESTADO decimal(1,0) NOT NULL,
   INTENTOS_FALLIDOS decimal(2,0) NOT NULL,
   FECHA_ALTA DATETIME2 NOT NULL,
   FECHA_MODIFICACION DATETIME2,
   FECHA_BAJA DATETIME2,
   CONSTRAINT AUTH_EXTERNOS_PK PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX AUTH_EXTERNOS_UK ON AUTH_EXTERNOS(USUARIO);


CREATE TABLE AUTH_TOKEN_EXTERNOS (
   ID decimal(9,0) IDENTITY NOT NULL,
   TOKEN varchar(30) NOT NULL,
   ID_USUARIO decimal(5,0) NOT NULL,
   FECHA_CADUCIDAD DATETIME2,

   CONSTRAINT AUTH_TOKEN_EXTERNOS_PK PRIMARY KEY (ID),
   CONSTRAINT AUTH_TOKEN_EXTERNOS_FK FOREIGN KEY (ID_USUARIO)
       REFERENCES AUTH_EXTERNOS (ID)
);

CREATE UNIQUE INDEX AUTH_TOKEN_EXTERNOS_UK ON AUTH_TOKEN_EXTERNOS(TOKEN);


CREATE TABLE CSV_PENDIENTES_PROCESAR (
   ID_TOKEN decimal(9,0) NOT NULL,
   CSV varchar(30) NOT NULL,

   CONSTRAINT CSV_PENDIENTES_PROCESAR_PK PRIMARY KEY (ID_TOKEN, CSV),
   CONSTRAINT CSV_PENDIENTES_PROCESAR_FK FOREIGN KEY (ID_TOKEN)
       REFERENCES AUTH_TOKEN_EXTERNOS (ID)
);

-- Tarea #303919 Lanbide - Problema con bloqueos de transacción en tramitación - Retardos en carga de datos
CREATE UNIQUE INDEX IDX_EXP_NEPM ON E_EXP (EXP_NUM, EXP_EJE, EXP_PRO, EXP_MUN);
CREATE INDEX IDX_CRO_NEPM ON E_CRO (CRO_NUM, CRO_EJE, CRO_PRO, CRO_MUN);

-- Tarea #302601 - Lanbide - Buzón de entrada - Alta de expediente con el año del registro que lo inicia
-- Mila Noya (06/02/2018)
ALTER TABLE E_PRO
ADD PRO_EXPNUMANOT DECIMAL(1,0) DEFAULT 0 NOT NULL;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.18 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.19 --------------------------
-----------------------------------------------------------------------

-- Tarea #287835: Lanbide - Marca de digitalización de documentos de registro por procedimiento

ALTER TABLE PLUGIN_DOC_PROCEDIMIENTO
ADD "DIGIT_DOC_REGISTRO" VARCHAR(2) DEFAULT 'NO' NOT NULL;
GO


--Tarea #300045: Compulsa - Registro - Botón de fin de digitalización de documentos
ALTER TABLE R_RES ADD FIN_DIGITALIZACION DECIMAL(1, 0) DEFAULT 1;

GO


-- Tarea #302264 Compulsa - Control de digitalización según unidad orgánica del usuario
ALTER TABLE A_UOR ADD UOR_DIGITALIZACION DECIMAL(1, 0) DEFAULT 0;


-- Tarea #318863: [Lanbide] - Compulsa - Modificación estructura de datos compulsa - V2
CREATE TABLE R_DOC_METADATOS (
    ID DECIMAL IDENTITY(10, 0) NOT NULL,
    RED_DOC_ID DECIMAL(10, 0) NOT NULL,
    ID_METADATO VARCHAR(150) NOT NULL,
    VALOR_METADATO VARCHAR(255),
    CONSTRAINT R_DOC_METADATOS_PK PRIMARY KEY (ID)
);
 
CREATE UNIQUE INDEX R_DOC_METADATOS_UK ON R_DOC_METADATOS (RED_DOC_ID, ID_METADATO);

GO
-- MODIFICACIONES EN R_RED
ALTER TABLE R_RED
ADD RED_IDDOC_GESTOR VARCHAR(200) NULL;

ALTER TABLE R_RED
ADD RED_DOCDIGIT DECIMAL(1,0) DEFAULT 0 NOT NULL;

ALTER TABLE R_RED
ADD RED_TIPODOC_ID DECIMAL(3,0) NULL;

GO

--1. Eliminar FK de METADATO_DOC_COTEJADOS que apunta a R_RED (¿Existe la tabla?)
ALTER TABLE METADATO_DOC_COTEJADOS
DROP CONSTRAINT DOC_COTEJADOS_TO_R_RED_FK;

--2. Eliminar PK de R_RED
ALTER TABLE R_RED
DROP PRIMARY KEY;

--3. Crear id secuencial  NULL -> cuando tenga datos pasarlo a NOT NULL
ALTER TABLE R_RED
ADD RED_DOC_ID DECIMAL IDENTITY(10,0) NULL;

--5. Actualizar id con valor de secuencia para todas las filas (SCRIPT ORACLE)
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
ALTER COLUMN RED_DOC_ID DECIMAL(10,0) NOT NULL;

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
ALTER TABLE R_RED ADD RED_OBSERV VARCHAR(1000);
GO
    
    -- Tareas: #330215 Compulsa - No llamar a retramitarDocumento() en registros migrados de RGI cuando se inicia expediente / asocia expediente
    -- David Guerrero (13/08/2018)
ALTER TABLE R_RED ADD (RED_MIGRA DECIMAL (1,0) Default 0);
GO

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.20 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 16.21 --------------------------
-----------------------------------------------------------------------

-- Tareas #320518 (0301218 Digitalización 2018): Compulsa - Mejoras en interfaz de catalogación 
-- José Ignacio Delgado (11/10/2018)
-- Esta tabla es propia de los entornos de Lanbide
ALTER TABLE MELANBIDE68_TIPDOC_LANBIDE ADD (COD_GRUPO_TIPDOC VARCHAR(8));
GO

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

GO

-----------------------------------------------------------------------
--------------------- FIN PARCHE 16.21 --------------------------
-----------------------------------------------------------------------