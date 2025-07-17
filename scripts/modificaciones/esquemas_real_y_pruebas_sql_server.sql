-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.00.00 --------------------------
-----------------------------------------------------------------------

-- Tareas #340541 [CONS] -ENI- Adaptacion y creacion de Script para SQLServer
-- Juan García
--Añde las tablas FIRMAS_ENI e INTERMEDIA_FIRMAS_ENI para almacenar en ellas las firmas de los documentos de expedientes ENI importados y la referencia a las mismas desde los documentos.
--Para guardar esa referencia, se añade la columna ID_INSERCION a todas las tablas de documentos afectadas por la importacion ENI (ADJUNTO_EXT_NOTIFICACION, E_CRD, 
--E_DOC_EXT, E_DOCS_PRESENTADOS, E_TFI, E_TFIT, NOTIFICACION). Tambien crea el procedimiento ENI de codigo PRENI y un tramite de inicio para este.

-- Declaracion de variables

DECLARE @codigoMunicipio AS int;
DECLARE @codigoPro AS varchar(5);
DECLARE        @codigoTipoPro AS int;
DECLARE        @codigoArea AS int;

-- Inicializacion de variables
SET @codigoPro = 'PRENI';
-- CODIGO MUNICIPIO
SET @codigoMunicipio = 0;
-- CODIGO MUNICIPIO
-- [PRUEBAS] SET @codigoMunicipio = 0;
-- [PRODUCCION] SET @ codigoMunicipio = 1;
-- [LANBIDE] SET @codigoMunicipio = 1;
-- [SANSE] SET @codigoMunicipio = 134;
-- [TVG] SET @codigoMunicipio = 134;
-- [USC] SET @codigoMunicipio = 134;
SET @codigoTipoPro = ( SELECT max(TPML_COD) FROM A_TPML WHERE TPML_VALOR='ENI');
SET @codigoArea = (SELECT max(AML_COD) FROM A_AML WHERE AML_VALOR='PROCEDIMIENTO ENI');

-- Creacion de la tabla de FIRMAS_ENI
CREATE TABLE FIRMAS_ENI (
	ID INT,
	CONTENIDO IMAGE,
	FORMATO_FIRMA_ENI VARCHAR(30) NOT NULL,
	TIPO_FIRMA_ENI VARCHAR(30) NOT NULL,
	CONSTRAINT FIRMAS_ENI_PK PRIMARY KEY(ID)
	);
EXEC sp_addextendedproperty 
	@name = N'ID_DESCRIPCION', @value = 'Identificador unico de la firma.',
	@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'FIRMAS_ENI', 
	@level2type = N'Column',   @level2name = 'ID';
EXEC sp_addextendedproperty 
	@name = N'CONTENIDO_DESCRIPCION', @value = 'Contenido binario de la firma.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'FIRMAS_ENI',
	@level2type = N'Column',   @level2name = 'CONTENIDO';
EXEC sp_addextendedproperty 
	@name = N'FORMATO_FIRMA_ENI_DESCRIPCION', @value = 'Formato de la firma. Puede ser: CSV, CADES_ATTACHED_EXPLICIT, CADES_ATTACHED_IMPLICIT, CADES_DETACHED_EXPLICIT, XADES_ENVELOPED, XADES_INTERNALLY_DETACHED, PADES.',
	@level0type = N'Schema', @level0name = 'dbo',
	@level1type = N'Table',  @level1name = 'FIRMAS_ENI', 
	@level2type = N'Column', @level2name = 'FORMATO_FIRMA_ENI';
EXEC sp_addextendedproperty 
	@name = N'TIPO_FIRMA_ENI_DESCRIPCION', 
		@value = 'Tipo de firma. Puede ser: CSV, CERTIFICADO_BINARIO, CERTIFICADO_XML, REFERENCIA.',
	@level0type = N'Schema',   @level0name = 'dbo',
		@level1type = N'Table',    @level1name = 'FIRMAS_ENI',
	@level2type = N'Column',   @level2name = 'TIPO_FIRMA_ENI';
EXEC sp_addextendedproperty 
	@name=N'TABLA_DESCRIPCION', 
	@value=N'Guarda las firmas de documentos ENI.' ,
	@level0type = N'Schema', @level0name = 'dbo',
	@level1type=N'TABLE',    @level1name=N'FIRMAS_ENI'
--CREATE SEQUENCE dbo.FIRMAS_ENI_SECUENCIA
-- START WITH 1 
-- INCREMENT BY 1;
-- Creacion de la tabla intermedia
CREATE TABLE INTERMEDIA_FIRMAS_ENI (
	ID_INSERCION INT,
	ID_FIRMA_ENI INT FOREIGN KEY REFERENCES FIRMAS_ENI(ID),
	CONSTRAINT PK_INTERMEDIA_FIRMAS PRIMARY KEY (ID_INSERCION,ID_FIRMA_ENI)
	);
EXEC sp_addextendedproperty 
	@name = N'ID_INSERCION_DESCRIPCION', 
		@value = 'Identificador que relaciona una inserci?n puntual con N firmas.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'INTERMEDIA_FIRMAS_ENI',
	@level2type = N'Column',   @level2name = 'ID_INSERCION';
EXEC sp_addextendedproperty 
	@name = N'ID_FIRMA_ENI_DESCRIPCION', 
		@value = 'Identificador de una firma ENI de la tabla FIRMAS_ENI.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'INTERMEDIA_FIRMAS_ENI',
	@level2type = N'Column',   @level2name = 'ID_FIRMA_ENI';
EXEC sp_addextendedproperty 
	@name=N'TABLA_DESCRIPCION',  @value=N'Tabla intermedia que sirve para relacionar un id a m?ltiples firmas.' ,
	@level0type = N'Schema',     @level0name = 'dbo',
	@level1type=N'TABLE',        @level1name=N'INTERMEDIA_FIRMAS_ENI'
ALTER TABLE ADJUNTO_EXT_NOTIFICACION ADD ID_INSERCION INT;
ALTER TABLE ADJUNTO_EXT_NOTIFICACION ADD UNIQUE (ID_INSERCION);
EXEC sp_addextendedproperty 
	@name = N'ID_INSERCION_DESCRIPCION', @value = 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.',
	@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'ADJUNTO_EXT_NOTIFICACION',
	@level2type = N'Column',   @level2name = 'ID_INSERCION';
ALTER TABLE E_CRD ADD ID_INSERCION INT;
ALTER TABLE E_CRD ADD UNIQUE (ID_INSERCION);
EXEC sp_addextendedproperty 
	@name = N'ID_INSERCION_DESCRIPCION', 
		@value = 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'E_CRD',
	@level2type = N'Column',   @level2name = 'ID_INSERCION';
ALTER TABLE E_DOC_EXT ADD ID_INSERCION INT;
ALTER TABLE E_DOC_EXT ADD UNIQUE (ID_INSERCION);
EXEC sp_addextendedproperty 
	@name = N'ID_INSERCION_DESCRIPCION', 
		@value = 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'E_DOC_EXT',
	@level2type = N'Column',   @level2name = 'ID_INSERCION';
ALTER TABLE E_DOCS_PRESENTADOS ADD ID_INSERCION INT;
ALTER TABLE E_DOCS_PRESENTADOS ADD UNIQUE (ID_INSERCION);
EXEC sp_addextendedproperty 
	@name = N'ID_INSERCION_DESCRIPCION', 
		@value = 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'E_DOCS_PRESENTADOS',
	@level2type = N'Column',   @level2name = 'ID_INSERCION';
ALTER TABLE E_TFI ADD ID_INSERCION INT;
ALTER TABLE E_TFI ADD UNIQUE (ID_INSERCION);
EXEC sp_addextendedproperty 
	@name = N'ID_INSERCION_DESCRIPCION', 
		@value = 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'E_TFI',
	@level2type = N'Column',   @level2name = 'ID_INSERCION';
ALTER TABLE E_TFIT ADD ID_INSERCION INT;
ALTER TABLE E_TFIT ADD UNIQUE (ID_INSERCION);
EXEC sp_addextendedproperty 
	@name = N'ID_INSERCION_DESCRIPCION', 
		@value = 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'E_TFIT',
	@level2type = N'Column',   @level2name = 'ID_INSERCION';        
ALTER TABLE NOTIFICACION ADD ID_INSERCION INT;
ALTER TABLE NOTIFICACION ADD UNIQUE (ID_INSERCION);
EXEC sp_addextendedproperty 
	@name = N'ID_INSERCION_DESCRIPCION', 
		@value = 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'NOTIFICACION',
	@level2type = N'Column',   @level2name = 'ID_INSERCION';

  -- PROCEDIMIENTO ENI
INSERT INTO E_PRO (PRO_COD,PRO_PLZ,PRO_SIL,PRO_TIP,PRO_DIN,PRO_UND,PRO_INI,PRO_EST,PRO_TIN,PRO_MUN,PRO_FLH,PRO_FLD,PRO_ARE,PRO_LOC,PRO_FBA,PRO_TRI,PRO_DES,PRO_PORCENTAJE,PLUGIN_ANULACION_NOMBRE,PLUGIN_ANULACION_IMPLCLASS,PRO_INTOBL,PLUGIN_RELAC_HISTORICO,PRO_SOLOWS,PRO_RESTRINGIDO,PRO_LIBRERIA,PRO_EXPNUMANOT) 
	VALUES (@codigoPro,null,null,@codigoTipoPro,'0',null,'0','0','0',@codigoMunicipio,null,CONVERT (date, SYSDATETIME()),@codigoArea,'0',null,1,'Procedimiento para la gestion de expedientes ENI',null,null,null,'0',null,'0','0','0','0');
-- TRAMITE DE INICIO
INSERT INTO E_TRA (TRA_PRO,TRA_COD,TRA_PLZ,TRA_UND,TRA_OCU,TRA_MUN,TRA_VIS,TRA_UIN,TRA_UTR,TRA_CLS,TRA_ARE,TRA_FBA,TRA_COU,TRA_PRE,TRA_INS,TRA_WS_COD,TRA_WS_OB,TRA_UTI,TRA_UTF,TRA_USI,TRA_USF,TRA_INI,TRA_INF,TRA_WST_COD,TRA_WST_OB,TRA_WSR_COD,TRA_WSR_OB,TRA_PRR,TRA_CAR,TRA_FIN,TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,TRA_TIPNOTFDP,TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA,TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN,TRA_NOTIF_ELECT_OBLIG,TRA_NOTIF_FIRMA_CERT_ORG,COD_DEPTO_NOTIFICACION,TRA_NOTIFICADO,TRA_NOTIF_UITF,TRA_NOTIF_UIEI,TRA_NOTIF_UIEF,TRA_NOTIF_UITI,TRA_NOTIF_USUTRA_FINPLAZO,TRA_NOTIF_USUEXP_FINPLAZO,TRA_NOTIF_UOR_FINPLAZO) 
	VALUES (@codigoPro,'1',null,null,null,@codigoMunicipio,'0',null,'4','1',null,null,'1','0',null,null,null,'N','N','N','N','N','N',null,null,null,null,null,'0','0','0','0','0','0','0','0',null,null,null,null,null,null,null,'0','0',null,'0','N','N','N','N','N','N','N');

--Modificamos la tabla E_EXP para cargar la importancia del expediente por defecto
ALTER TABLE E_EXT MODIFY  EXP_IMP VARCHAR2(1) default 'N';
EXEC sp_addextendedproperty 
	@name = N'EXP_IMP_DESCRIPCION', 
		@value = 'Columna que determina si el expediente se ha marcado como Importante N/S.',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'E_EXP',
	@level2type = N'Column',   @level2name = 'EXP_IMP';

GO
-- Tareas #343011 [CONS] -SIR- Crear tarea Quartz para enviar asientos marcados
-- Kevin Rañales

--Modificamos la tabla R_RES para poder relacionarla con el JUSTIFICANTE_SIR
ALTER TABLE R_RES ADD COD_JUSTIFICANTE_SIR NUMBER(6,0);
EXEC sp_addextendedproperty 
	@name = N'COD_JUSTIFICANTE_SIR_DESCRIPCION', 
		@value = 'Identificador del justificante, se utiliza para relacionar el registro con el justficante SIR',
		@level0type = N'Schema',   @level0name = 'dbo',
	@level1type = N'Table',    @level1name = 'R_RES',
	@level2type = N'Column',   @level2name = 'COD_JUSTIFICANTE_SIR';

-- Se crea la tabla Justificante para poder almacenar el justificante que recibimos del WebService 
CREATE TABLE "JUSTIFICANTE_SIR" (
	"ID_JUSTIFICANTE_SIR"			NUMBER(3, 0)			PRIMARY KEY	COMMENT 'Identificador del justificante, se utiliza la secuencia JUSTIFICANTE_SIR_SEQ',
	"CERTIFICADO"					CLOB								COMMENT 'Certificado del fichero Justificante de Registro (parte pública)',
	"CODIGO_INTERCAMBIO"			VARCHAR2(33 CHAR)		NOT NULL	COMMENT 'Código de Intercambio.',
	"CODIGO_TIPO_DOCUMENTO"			VARCHAR2(2 CHAR)		NOT NULL	COMMENT 'Indica el tipo de documento. Siempre es "02" = Documento adjunto al formulario',
	"CONTENIDO_JUSTIFICANTE"		BLOB								COMMENT 'Fichero Justificante de Registro codificado en Base64',
	"CSV_JUSTIFICANTE"				VARCHAR2(100 CHAR)					COMMENT 'Código CSV del fichero Justificante de Registro',
	"FECHA_HORA_PRESENTACION"		DATE								COMMENT 'Fecha y Hora de Presentación',
	"FECHA_HORA_REGISTRO"			DATE								COMMENT 'Fecha y Hora de Registro',
	"FIRMA"							CLOB								COMMENT 'Firma electrónica del fichero Justificante de Registro',
	"FIRMADO"						CHAR(1 CHAR)			DEFAULT 0	COMMENT 'Indica si el Justificante de Registro se devuelve firmado o no',
	"NOMBRE_DOC_JUSTIFICANTE"		VARCHAR2(80 CHAR)		NOT NULL	COMMENT 'Nombre del Fichero Justificante de Registro',
	"NUMERO_REGISTRO"				NUMBER(6,0)							COMMENT 'Número de Registro',
	"HASH"							VARCHAR2(4000 CHAR)					COMMENT 'Justificante de Registro',
	"ID_FICHERO_JUSTIFICANTE"		VARCHAR2(50 CHAR)					COMMENT 'Identificador del fichero Justificante de Registro (uso interno de la librería)',
	"TIMESTAMP_ANEXO"				CLOB								COMMENT 'TimeStamp: Sello de tiempo del fichero Justificante de Registro', 
	"TIPO_MIME"						VARCHAR2(240 CHAR)					COMMENT 'Tipo MIME del fichero Justificante de Registro',
	"VALIDACION_CERTIFICADO"		CLOB								COMMENT 'Validación del certificado',
	"VALIDEZ_DOCUMENTO"				VARCHAR2(2 CHAR)					COMMENT 'Indica la categoría de autenticidad del documento.Siempre es "04" Origina'
);
COMMIT;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 18.00.00 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.01.00 --------------------------
-----------------------------------------------------------------------

-- Tareas #340461: Integración Flexia16 - Flexia18
-- Milagros Noya (28/11/2018)
-- Historificación de los circuitos de flujos de firma personalizados de trámites
Create Table Hist_E_Crd_Fir_Firmantes (
  ID_PROCESO DECIMAL(19,0) NOT NULL,
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

	Constraint Hist_Ecrdfirfirmantes_Pk Primary Key (Cod_Municipio, Cod_Procedimiento, Ejercicio, Num_Expediente, Cod_Tramite, Cod_Ocurrencia, Cod_Documento, Id_Usuario),
    Constraint Hist_Ecrdfirfirmantes_Fk Foreign Key (Cod_Municipio, Cod_Procedimiento, Ejercicio, Num_Expediente, Cod_Tramite, Cod_Ocurrencia, Cod_Documento)
        References Hist_E_Crd (Crd_Mun, Crd_Pro, Crd_Eje, Crd_Num, Crd_Tra, Crd_Ocu, Crd_Nud),
  CONSTRAINT Hist_Ecrdfirfirmantes_Fkh FOREIGN KEY (ID_PROCESO) REFERENCES EXP_ENVIO_HISTORICO ("ID") ON DELETE CASCADE
);
GO

-- Historificación de los flujos personalizados de trámites
Create Table Hist_E_Crd_Fir_Flujo (
  ID_PROCESO DECIMAL(19,0) NOT NULL,
	COD_MUNICIPIO DECIMAL(3,0) NOT NULL,
	COD_PROCEDIMIENTO VARCHAR(5) NOT NULL,
	EJERCICIO DECIMAL(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR(30) NOT NULL,
	COD_TRAMITE DECIMAL(4,0) NOT NULL,
	COD_OCURRENCIA DECIMAL(4,0) NOT NULL,
	COD_DOCUMENTO DECIMAL(4,0) NOT NULL,
	ID_TIPO_FIRMA DECIMAL(9,0) NOT NULL,
	
	Constraint Hist_Ecrdfirflujo_Pk Primary Key (Cod_Municipio, Cod_Procedimiento, Ejercicio, Num_Expediente, Cod_Tramite, Cod_Ocurrencia, Cod_Documento),
    Constraint Hist_Ecrdfirflujo_Fk Foreign Key (Cod_Municipio, Cod_Procedimiento, Ejercicio, Num_Expediente, Cod_Tramite, Cod_Ocurrencia, Cod_Documento)
        References Hist_E_Crd (Crd_Mun, Crd_Pro, Crd_Eje, Crd_Num, Crd_Tra, Crd_Ocu, Crd_Nud),
	Constraint Hist_Ecrdfirflujo_Tipo_Fk Foreign Key (Id_Tipo_Firma) References Firma_Tipo (Id),
  CONSTRAINT Hist_Ecrdfirfliujo_Fkh FOREIGN KEY (ID_PROCESO) REFERENCES EXP_ENVIO_HISTORICO ("ID") ON DELETE CASCADE
);
GO

-- Tareas #345379: [PRE_Flexia] No se pueden dar de alta asuntos en el Registro
-- Mila Noya (10/12/2018)
-- BORRAR CONSTRAINTS
Alter Table R_Tipoasunto
Drop Constraint Fk_Clasificacion_Asunto;

Alter Table R_clasificacion_asunto
Drop Primary Key;

-- CREAR CONSTRAINTS
Alter Table R_Clasificacion_Asunto
ADD CONSTRAINT R_CLASIFICACION_ASUNTO_PK PRIMARY KEY (CODIGO,UNIDAD_REGISTRO);

Alter Table R_Tipoasunto
Add Constraint Fk_Clasificacion_Asunto Foreign Key (CODIGO_CLASIFICACION,Unidadregistro) REFERENCES R_Clasificacion_Asunto (CODIGO,UNIDAD_REGISTRO);

GO

-- Tareas #426095: [LANBIDE] Proceso de carga masiva de registros
-- Mila Noya (04/05/2020)
Alter Table R_Doc_Aportados_Anterior
Modify R_Doc_Aportados_Nom_Doc Varchar2(300 Byte);