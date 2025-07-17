-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.00.00 --------------------------
-----------------------------------------------------------------------

-- Tareas #340541 [CONS] -ENI- Adaptacion y creacion de Script para SQLServer
-- Alejandro Díaz
--Añde las tablas FIRMAS_ENI e INTERMEDIA_FIRMAS_ENI para almacenar en ellas las firmas de los documentos de expedientes ENI importados y la referencia a las mismas desde los documentos.
--Para guardar esa referencia, se añade la columna ID_INSERCION a todas las tablas de documentos afectadas por la importacion ENI (ADJUNTO_EXT_NOTIFICACION, E_CRD, 
--E_DOC_EXT, E_DOCS_PRESENTADOS, E_TFI, E_TFIT, NOTIFICACION). Tambien crea el procedimiento ENI de codigo PRENI y un tramite de inicio para este.

-- Declaracion de variables

VAR codigoMunicipio NUMBER;
VAR codigoPro VARCHAR2;
VAR codigoTipoPro NUMBER;
VAR codigoArea NUMBER;

-- Inicializacion de variables
exec :codigoPro := 'PRENI';
-- CODIGO MUNICIPIO
exec :codigoMunicipio := 0;
-- [PRUEBAS] exec :codigoMunicipio := 0;
-- [PRODUCCION] exec :codigoMunicipio := 1;
-- [LANBIDE] exec :codigoMunicipio := 1;
-- [SANSE] exec :codigoMunicipio := 134;
-- [TVG] exec :codigoMunicipio := 134;
-- [USC] exec :codigoMunicipio := 134;

-- Cambiar el nombre es esquema genérico, de pruebas o real por el que corresponda según el entorno
ALTER SESSION SET CURRENT_SCHEMA = FLEXIA_GENERICO;
BEGIN
  SELECT max(TPML_COD) INTO :codigoTipoPro FROM A_TPML WHERE TPML_VALOR='ENI';
  SELECT max(AML_COD) INTO :codigoArea FROM A_AML WHERE AML_VALOR='PROCEDIMIENTO ENI';
END;
/
ALTER SESSION SET CURRENT_SCHEMA = FLEXIA_PRUEBAS;

-- Creacion de la tabla de FIRMAS_ENI
CREATE TABLE "FIRMAS_ENI" (
  "ID" NUMBER(7, 0) PRIMARY KEY,
  "CONTENIDO" LONG RAW,
  "FORMATO_FIRMA_ENI" VARCHAR2(30 BYTE) NOT NULL,
  "TIPO_FIRMA_ENI" VARCHAR2(30 BYTE) NOT NULL
);

	COMMENT ON COLUMN "FIRMAS_ENI"."ID" IS 'Identificador unico de la firma.';
	COMMENT ON COLUMN "FIRMAS_ENI"."CONTENIDO" IS 'Contenido binario de la firma.';
	COMMENT ON COLUMN "FIRMAS_ENI"."FORMATO_FIRMA_ENI" IS 'Formato de la firma. Puede ser: CSV, CADES_ATTACHED_EXPLICIT, CADES_ATTACHED_IMPLICIT, CADES_DETACHED_EXPLICIT, XADES_ENVELOPED, XADES_INTERNALLY_DETACHED, PADES.';
	COMMENT ON COLUMN "FIRMAS_ENI"."TIPO_FIRMA_ENI" IS 'Tipo de firma. Puede ser: CSV, CERTIFICADO_BINARIO, CERTIFICADO_XML, REFERENCIA.';
	COMMENT ON TABLE "FIRMAS_ENI" IS 'Guarda las firmas de documentos ENI.';

CREATE SEQUENCE "FIRMAS_ENI_SECUENCIA"
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1;
-- Creacion de la tabla intermedia

CREATE TABLE "INTERMEDIA_FIRMAS_ENI" (
  "ID_INSERCION" NUMBER(7, 0) NOT NULL,
  "ID_FIRMA_ENI" NUMBER(7, 0) NOT NULL REFERENCES "FIRMAS_ENI"("ID"),
  PRIMARY KEY ("ID_INSERCION", "ID_FIRMA_ENI")
);
	COMMENT ON COLUMN "INTERMEDIA_FIRMAS_ENI"."ID_INSERCION" IS 'Identificador que relaciona una inserciÃ³n puntual con N firmas.';
	COMMENT ON COLUMN "INTERMEDIA_FIRMAS_ENI"."ID_FIRMA_ENI" IS 'Identificador de una firma ENI de la tabla FIRMAS_ENI.';
	COMMENT ON TABLE "INTERMEDIA_FIRMAS_ENI" IS 'Tabla intermedia que sirve para relacionar un id a mÃºltiples firmas.';
-- Insercion de columnas en las tablas relacionadas. Estas columnas son NULLABLE

ALTER TABLE "ADJUNTO_EXT_NOTIFICACION" ADD "ID_INSERCION" NUMBER(7, 0);
ALTER TABLE "ADJUNTO_EXT_NOTIFICACION" ADD UNIQUE ("ID_INSERCION");
	COMMENT ON COLUMN "ADJUNTO_EXT_NOTIFICACION"."ID_INSERCION" IS 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.';

ALTER TABLE "E_CRD" ADD "ID_INSERCION" NUMBER(7, 0);
ALTER TABLE "E_CRD" ADD UNIQUE ("ID_INSERCION");
	COMMENT ON COLUMN "E_CRD"."ID_INSERCION" IS 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.';

ALTER TABLE "E_DOC_EXT" ADD "ID_INSERCION" NUMBER(7, 0);
ALTER TABLE "E_DOC_EXT" ADD UNIQUE ("ID_INSERCION");
	COMMENT ON COLUMN "E_DOC_EXT"."ID_INSERCION" IS 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.';

ALTER TABLE "E_DOCS_PRESENTADOS" ADD "ID_INSERCION" NUMBER(7, 0);
ALTER TABLE "E_DOCS_PRESENTADOS" ADD UNIQUE ("ID_INSERCION");
	COMMENT ON COLUMN "E_DOCS_PRESENTADOS"."ID_INSERCION" IS 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.';

ALTER TABLE "E_TFI" ADD "ID_INSERCION" NUMBER(7, 0);
ALTER TABLE "E_TFI" ADD UNIQUE ("ID_INSERCION");
	COMMENT ON COLUMN "E_TFI"."ID_INSERCION" IS 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.';

ALTER TABLE "E_TFIT" ADD "ID_INSERCION" NUMBER(7, 0);
ALTER TABLE "E_TFIT" ADD UNIQUE ("ID_INSERCION");
	COMMENT ON COLUMN "E_TFIT"."ID_INSERCION" IS 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.';

ALTER TABLE "NOTIFICACION" ADD "ID_INSERCION" NUMBER(7, 0);
ALTER TABLE "NOTIFICACION" ADD UNIQUE ("ID_INSERCION");
	COMMENT ON COLUMN "NOTIFICACION"."ID_INSERCION" IS 'Identificador que relaciona un documento con N firmas con la tabla FIRMAS_ENI.';


-- PROCEDIMIENTO ENI
INSERT INTO E_PRO (PRO_COD,PRO_PLZ,PRO_SIL,PRO_TIP,PRO_DIN,PRO_UND,PRO_INI,PRO_EST,PRO_TIN,PRO_MUN,PRO_FLH,PRO_FLD,PRO_ARE,PRO_LOC,PRO_FBA,PRO_TRI,PRO_DES,PRO_PORCENTAJE,PLUGIN_ANULACION_NOMBRE,PLUGIN_ANULACION_IMPLCLASS,PRO_INTOBL,PLUGIN_RELAC_HISTORICO,PRO_SOLOWS,PRO_RESTRINGIDO,PRO_LIBRERIA,PRO_EXPNUMANOT) 
  VALUES (:codigoPro,null,null,:codigoTipoPro,'0',null,'0','0','0',:codigoMunicipio,null, to_date(sysdate),:codigoArea,'0',null,1,'Procedimiento para la gestion de expedientes ENI',null,null,null,'0',null,'0','0','0','0');

-- TRAMITE DE INICIO
INSERT INTO E_TRA (TRA_PRO,TRA_COD,TRA_PLZ,TRA_UND,TRA_OCU,TRA_MUN,TRA_VIS,TRA_UIN,TRA_UTR,TRA_CLS,TRA_ARE,TRA_FBA,TRA_COU,TRA_PRE,TRA_INS,TRA_WS_COD,TRA_WS_OB,TRA_UTI,TRA_UTF,TRA_USI,TRA_USF,TRA_INI,TRA_INF,TRA_WST_COD,TRA_WST_OB,TRA_WSR_COD,TRA_WSR_OB,TRA_PRR,TRA_CAR,TRA_FIN,TRA_GENERARPLZ,TRA_NOTCERCAFP,TRA_NOTFUERADP,TRA_TIPNOTCFP,TRA_TIPNOTFDP,TRA_NOTIFICACION_ELECTRONICA,TRA_COD_TIPO_NOTIFICACION,TRA_TIPO_USUARIO_FIRMA,TRA_OTRO_COD_USUARIO_FIRMA,TRA_TRAM_EXT_PLUGIN,TRA_TRAM_EXT_URL,TRA_TRAM_EXT_IMPLCLASS,TRA_TRAM_ID_ENLACE_PLUGIN,TRA_NOTIF_ELECT_OBLIG,TRA_NOTIF_FIRMA_CERT_ORG,COD_DEPTO_NOTIFICACION,TRA_NOTIFICADO,TRA_NOTIF_UITF,TRA_NOTIF_UIEI,TRA_NOTIF_UIEF,TRA_NOTIF_UITI,TRA_NOTIF_USUTRA_FINPLAZO,TRA_NOTIF_USUEXP_FINPLAZO,TRA_NOTIF_UOR_FINPLAZO) 
    VALUES (:codigoPro,'1',null,null,null,:codigoMunicipio,'0',null,'4','1',null,null,'1','0',null,null,null,'N','N','N','N','N','N',null,null,null,null,null,'0','0','0','0','0','0','0','0',null,null,null,null,null,null,null,'0','0',null,'0','N','N','N','N','N','N','N');

--Modificamos la tabla E_EXP para cargar la importancia del expediente por defecto
ALTER TABLE E_EXP MODIFY( EXP_IMP VARCHAR2(1) default 'N' );
COMMENT ON COLUMN E_EXP.EXP_IMP IS 'Columna que determina si el expediente se ha marcado como Importante N/S';



COMMIT;

-- Tareas #343011 [CONS] -SIR- Crear tarea Quartz para enviar asientos marcados
-- Kevin Rañales

--Modificamos la tabla R_RES para poder relacionarla con el JUSTIFICANTE_SIR
ALTER TABLE R_RES ADD COD_JUSTIFICANTE_SIR NUMBER(6,0);
	COMMENT ON COLUMN "R_RES"."COD_JUSTIFICANTE_SIR" IS 'Identificador del justificante, se utiliza para relacionar el registro con el justficante SIR';

-- Se crea la tabla Justificante para poder almacenar el justificante que recibimos del WebService
CREATE TABLE "COD_JUSTIFICANTE_SIR" (
	"ID_JUSTIFICANTE_SIR"			NUMBER(6, 0)			PRIMARY KEY,
	"CERTIFICADO"					CLOB,
	"CODIGO_INTERCAMBIO"			VARCHAR2(33 CHAR)		NOT NULL,
	"CODIGO_TIPO_DOCUMENTO"			VARCHAR2(2 CHAR)		NOT NULL,
	"CONTENIDO_JUSTIFICANTE"		BLOB,
	"CSV_JUSTIFICANTE"				VARCHAR2(100 CHAR),
	"FECHA_HORA_PRESENTACION"		DATE, 
	"FECHA_HORA_REGISTRO"			DATE,
	"FIRMA"							CLOB,
	"FIRMADO"						CHAR(1 CHAR)			DEFAULT 0,
	"NOMBRE_DOC_JUSTIFICANTE"		VARCHAR2(80 CHAR)		NOT NULL,
	"NUMERO_REGISTRO"				NUMBER(6,0),
	"HASH"							VARCHAR2(4000 CHAR),
	"ID_FICHERO_JUSTIFICANTE"		VARCHAR2(50 CHAR),
	"TIMESTAMP_ANEXO"				CLOB, 
	"TIPO_MIME"						VARCHAR2(240 CHAR),
	"VALIDACION_CERTIFICADO"		CLOB,
	"VALIDEZ_DOCUMENTO"				VARCHAR2(2 CHAR)
);

COMMENT ON TABLE "JUSTIFICANTE_SIR" IS 'Tabla para almacenar el justificante de envio del asiento SIR.';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."ID_JUSTIFICANTE_SIR"			IS 'Identificador del justificante, se utiliza la secuencia JUSTIFICANTE_SIR_SEQ';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."CERTIFICADO"					IS 'Certificado del fichero Justificante de Registro (parte pública)';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."CODIGO_INTERCAMBIO"			IS 'Código de Intercambio.';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."CODIGO_TIPO_DOCUMENTO"		IS 'Indica el tipo de documento. Siempre es "02" = Documento adjunto al formulario';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."CONTENIDO_JUSTIFICANTE"		IS 'Fichero Justificante de Registro codificado en Base64';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."CSV_JUSTIFICANTE"				IS 'Código CSV del fichero Justificante de Registro';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."FECHA_HORA_PRESENTACION"		IS 'Fecha y Hora de Presentación';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."FECHA_HORA_REGISTRO"			IS 'Fecha y Hora de Registro';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."FIRMA"						IS 'Firma electrónica del fichero Justificante de Registro';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."FIRMADO"						IS 'Indica si el Justificante de Registro se devuelve firmado o no';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."NOMBRE_DOC_JUSTIFICANTE"		IS 'Nombre del Fichero Justificante de Registro';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."NUMERO_REGISTRO"				IS 'Número de Registro';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."HASH"							IS 'Justificante de Registro';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."ID_FICHERO_JUSTIFICANTE"		IS 'Identificador del fichero Justificante de Registro (uso interno de la librería)';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."TIMESTAMP_ANEXO"				IS 'TimeStamp: Sello de tiempo del fichero Justificante de Registro';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."TIPO_MIME"					IS 'Tipo MIME del fichero Justificante de Registro';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."VALIDACION_CERTIFICADO"		IS 'Validación del certificado';
	COMMENT ON COLUMN "JUSTIFICANTE_SIR"."VALIDEZ_DOCUMENTO"			IS 'Indica la categoría de autenticidad del documento.Siempre es "04" Origina';
	
CREATE SEQUENCE "JUSTIFICANTE_SIR_SEQ" 
	START WITH 1 INCREMENT BY 1 MINVALUE 1;

COMMIT;

-- Tareas #345450 [CONS] -SIR- Preparar scripts SQL para el soporte del SIR.
-- Alejandro

-- Tabla ASIENTO_SIR
CREATE TABLE "ASIENTO_SIR" (
    "APP_VERSION"                       VARCHAR2(20 CHAR), 
    "COD_ASIENTO"                       NUMBER(6,0) PRIMARY KEY, 
    "COD_ASUNTO"                        VARCHAR2(10 CHAR) DEFAULT 'SIR',
    "COD_DOC_FISICA"                    VARCHAR2(1 CHAR), 
    "COD_ENT_REGISTRAL_DESTINO"         VARCHAR2(21 CHAR) NOT NULL,
    "COD_ENT_REGISTRAL_INICIO"          VARCHAR2(21 CHAR),
    "COD_ENT_REGISTRAL_ORIGEN"          VARCHAR2(21 CHAR),
    "COD_TIPO_ANOTACION"                VARCHAR2(2 CHAR) NOT NULL,
    "COD_TIPO_REGISTRO"                 VARCHAR2(1 CHAR) NOT NULL, 
    "COD_TIPO_TRANSPORTE"               VARCHAR2(2 CHAR), 
    "COD_UND_TRAMITADORA_DESTINO"       VARCHAR2(21 CHAR), 
    "COD_UND_TRAMITADORA_ORIGEN"        VARCHAR2(21 CHAR), 
    "CONTACTO_USUARIO"                  VARCHAR2(80 CHAR), 
    "DESC_ASUNTO"                       VARCHAR2(240 CHAR) NOT NULL, 
    "DESC_ENT_REGISTRAL_INICIO"         VARCHAR2(80 CHAR), 
    "DESC_ENT_REGISTRAL_DESTINO"        VARCHAR2(80 CHAR), 
    "DESC_ENT_REGISTRAL_ORIGEN"         VARCHAR2(80 CHAR), 
    "DESC_TIPO_ANOTACION"               VARCHAR2(80 CHAR), 
    "DESC_UND_TRAMITADORA_DESTINO"      VARCHAR2(80 CHAR), 
    "DESC_UND_TRAMITADORA_ORIGEN"       VARCHAR2(80 CHAR), 
    "EXPONE"                            VARCHAR2(4000 CHAR), 
    "FECHA_ENTRADA"                     DATE NOT NULL, 
    "NOMBRE_USUARIO"                    VARCHAR2(60 CHAR), 
    "NUM_EXPEDIENTE"                    VARCHAR2(80 CHAR), 
    "NUM_REGISTRO_ENTRADA"              VARCHAR2(20 CHAR) NOT NULL, 
    "NUM_TRANSPORTE"                    VARCHAR2(10 CHAR), 
    "OBSERVACIONES"                     VARCHAR2(50 CHAR), 
    "REFERENCIA_EXTERNA"                VARCHAR2(16 CHAR), 
    "SOLICITA"                          VARCHAR2(4000 CHAR), 
    "TIMESTAMP_ENTRADA"                 CLOB, 
    "NUM_REGISTRO"                      NUMBER(6,0) 
);

COMMENT ON COLUMN "ASIENTO_SIR"."APP_VERSION" IS 'Identifica la aplicación y su versión emisora.';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_ASIENTO" IS 'Código identificativo del asiento, clave primaria.';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_ASUNTO" IS 'Código del asunto en destino. Siempre utilizaremos SIR.';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_DOC_FISICA" IS 'Código que indica si el fichero va acompañado de documentación basica ["1" = acopañada de documentación básica requerida, "2" = acompañada de documentación básica complementaria, "3" = no acompaña documentación básica ni otros soportes].';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_ENT_REGISTRAL_DESTINO" IS 'Código de la entidad registral de destino.';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_ENT_REGISTRAL_INICIO" IS 'Código de la entidad registral de inicio.';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_ENT_REGISTRAL_ORIGEN" IS 'Código de la entidad registral de origen.';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_TIPO_ANOTACION" IS 'Código que indica el motivo de la anotación ["01" = Pendiente (sin identificador de intercambio), "02" = Envío, "03" = Reenvío, "04" = Rechazo].';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_TIPO_REGISTRO" IS 'Tipo de registro ["0" = Registro de entrada, "1" = Registro de salida].';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_TIPO_TRANSPORTE" IS 'Código tipo transporte de entrada ["01" = Servicio de mensajeros,"02" = Correo postal,"03" = Correo postal certificado, "04" = Burofax, "05" = En mano, "06" = Fax, "07" = Otros].';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_UND_TRAMITADORA_DESTINO" IS 'Código de la unidad de tramitación de destino.';
COMMENT ON COLUMN "ASIENTO_SIR"."COD_UND_TRAMITADORA_ORIGEN" IS 'Código de la unidad de tramitación de origen.';
COMMENT ON COLUMN "ASIENTO_SIR"."CONTACTO_USUARIO" IS 'Contacto del usuario de origen (teléfono o dirección de correo electrónico).';
COMMENT ON COLUMN "ASIENTO_SIR"."DESC_ASUNTO" IS 'Descripción del asunto.';
COMMENT ON COLUMN "ASIENTO_SIR"."DESC_ENT_REGISTRAL_INICIO" IS 'Descripción de la entidad registral de inicio.';
COMMENT ON COLUMN "ASIENTO_SIR"."DESC_ENT_REGISTRAL_DESTINO" IS 'Descripción de la entidad registral de destino.';
COMMENT ON COLUMN "ASIENTO_SIR"."DESC_ENT_REGISTRAL_ORIGEN" IS 'Descripción de la entidad registral de origen.';
COMMENT ON COLUMN "ASIENTO_SIR"."DESC_TIPO_ANOTACION" IS 'Motivo del rechazo o del reenvío.';
COMMENT ON COLUMN "ASIENTO_SIR"."DESC_UND_TRAMITADORA_DESTINO" IS 'Descripción de la unidad de tramitación de destino.';
COMMENT ON COLUMN "ASIENTO_SIR"."DESC_UND_TRAMITADORA_ORIGEN" IS 'Descripción de la unidad de tramitación de origen.';
COMMENT ON COLUMN "ASIENTO_SIR"."EXPONE" IS 'Exposición de los hechos y antecedentes relacionados con el asiento.';
COMMENT ON COLUMN "ASIENTO_SIR"."FECHA_ENTRADA" IS 'Fecha y hora de entrada (en origen).';
COMMENT ON COLUMN "ASIENTO_SIR"."NOMBRE_USUARIO" IS 'Nombre de usuario que genera el asiento.';
COMMENT ON COLUMN "ASIENTO_SIR"."NUM_EXPEDIENTE" IS 'Número del expediente objeto de la tramitación administrativa.';
COMMENT ON COLUMN "ASIENTO_SIR"."NUM_REGISTRO_ENTRADA" IS 'Número de registro de entrada en la entidad registral de origen.';
COMMENT ON COLUMN "ASIENTO_SIR"."NUM_TRANSPORTE" IS 'Número de transporte de entrada.';
COMMENT ON COLUMN "ASIENTO_SIR"."OBSERVACIONES" IS 'Observaciones del registro de datos de intercambio recogidos por el funcionario de registro.';
COMMENT ON COLUMN "ASIENTO_SIR"."REFERENCIA_EXTERNA" IS 'Cualquier referencia que el destino precise conocer y sea conocida por el solicitante (matrícula de vehículo, número de recibo cuyo importe se reclama, etc.).';
COMMENT ON COLUMN "ASIENTO_SIR"."SOLICITA" IS 'Descripción de la solicitud.';
COMMENT ON COLUMN "ASIENTO_SIR"."TIMESTAMP_ENTRADA" IS 'Timestamp: sello de tiempo del registro de entrada en origen.';
COMMENT ON TABLE "ASIENTO_SIR"  IS 'Asientos recibidos a través de la plataforma CIR.';

CREATE SEQUENCE "SEQ_ASIENTO_SIR"
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1;

-- Tabla INTERCAMBIO_SIR
CREATE TABLE "INTERCAMBIO_SIR" (
    "COD_ASIENTO"                       NUMBER(6,0) REFERENCES "ASIENTO_SIR"("COD_ASIENTO") ON DELETE SET NULL,
    "COD_INTERCAMBIO"                   VARCHAR2(33 CHAR) PRIMARY KEY,
    "ESTADO"                            NUMBER(1,0) NOT NULL,
    "FECHA_RECHAZO"                     DATE, 
    "FICHERO_INTERCAMBIO"               CLOB,
    "MOTIVO_RECHAZO"                    VARCHAR(200 CHAR)
);

COMMENT ON COLUMN "INTERCAMBIO_SIR"."COD_ASIENTO" IS '[FK ASIENTO.COD_ASIENTO] Código de asiento al que hace referencia.';
COMMENT ON COLUMN "INTERCAMBIO_SIR"."COD_INTERCAMBIO" IS 'Identificador de intercambio unico de la operación de formato <Codigo Entidad Registral Origen>_<AA>_<Numero Secuencial>. Es clave primaria.';
COMMENT ON COLUMN "INTERCAMBIO_SIR"."ESTADO" IS 'Estado del asiento, [0 = faltan documentos, 1 = pendiente, 2 = rechazado, 3 = aceptado].';
COMMENT ON COLUMN "INTERCAMBIO_SIR"."FECHA_RECHAZO" IS 'Fecha del rechazo o reenvío del asiento.';
COMMENT ON COLUMN "INTERCAMBIO_SIR"."FICHERO_INTERCAMBIO" IS 'Fichero XML que representa el intercambio.';
COMMENT ON COLUMN "INTERCAMBIO_SIR"."MOTIVO_RECHAZO" IS 'Motivo por el que se rechaza o reenvía el asiento.';
COMMENT ON TABLE "INTERCAMBIO_SIR"  IS 'Información de los intercambios de asientos del SIR.';

CREATE SEQUENCE "SEQ_INTERCAMBIO_SIR"
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1;

-- Tabla ANEXO_SIR
CREATE TABLE "ANEXO_SIR" (
    "COD_ANEXO"                     NUMBER(3,0) NOT NULL, 
    "COD_ASIENTO"                   NUMBER(6,0) NOT NULL, 
    "CONTENIDO"                     BLOB,
    "IDENTIFICADOR_DOC_FIRMADO"     VARCHAR2(50 CHAR), 
    "IDENTIFICADOR_FICHERO"         VARCHAR2(50 CHAR), 
    "NOMBRE_FICHERO"                VARCHAR2(80 CHAR) NOT NULL, 
    "OBSERVACIONES"                 VARCHAR2(50 CHAR), 
    "TIMESTAMP"                     CLOB, 
    "TIPO_DOCUMENTO"                VARCHAR2(2 CHAR) NOT NULL,
    "TIPO_MIME"                     VARCHAR2(240 CHAR), 
    "VALIDEZ_DOCUMENTO"             VARCHAR2(2 CHAR),
    PRIMARY KEY ("COD_ANEXO","COD_ASIENTO"),
    FOREIGN KEY ("COD_ASIENTO") REFERENCES "ASIENTO_SIR"("COD_ASIENTO")
);

COMMENT ON COLUMN "ANEXO_SIR"."COD_ANEXO" IS 'Código de anexo, es clave primaria.';
COMMENT ON COLUMN "ANEXO_SIR"."COD_ASIENTO" IS '[FK ASIENTO.COD_ASIENTO] Código de asiento al que pertenece, es clave primaria.';
COMMENT ON COLUMN "ANEXO_SIR"."CONTENIDO" IS 'Contenido del anexo.';
COMMENT ON COLUMN "ANEXO_SIR"."IDENTIFICADOR_DOC_FIRMADO" IS 'Si el anexo es firma de otro documento se especifica el identificador de fichero objeto de la firma, este campo tomará el valor de sí mismo para indicar que contiene firma embebida.';
COMMENT ON COLUMN "ANEXO_SIR"."IDENTIFICADOR_FICHERO" IS 'Identificador fichero.';
COMMENT ON COLUMN "ANEXO_SIR"."NOMBRE_FICHERO" IS 'Nombre del fichero original.';
COMMENT ON COLUMN "ANEXO_SIR"."OBSERVACIONES" IS 'Observaciones del fichero adjunto.';
COMMENT ON COLUMN "ANEXO_SIR"."TIMESTAMP" IS 'Sello de tiempo del fichero anexo.';
COMMENT ON COLUMN "ANEXO_SIR"."TIPO_DOCUMENTO" IS 'Indica el tipo de documento: ["01" = Formulario, "02" = Documento adjunto al formulario, "03" = Fichero técnico interno].';
COMMENT ON COLUMN "ANEXO_SIR"."TIPO_MIME" IS 'Tipo del fichero anexo.';
COMMENT ON COLUMN "ANEXO_SIR"."VALIDEZ_DOCUMENTO" IS 'Indica la categoria de autenticidad del documento: ["01" = Copia, "02" = Copia compulsada, "03" = Copia original, "04" = Original].';
COMMENT ON TABLE "ANEXO_SIR" IS 'Documentos anexos relacionados con un asiento del SIR.';
 
CREATE SEQUENCE "SEQ_ANEXO_SIR"
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1;

-- Tabla DOMICILIO_SIR
CREATE TABLE "DOMICILIO_SIR" (
    "COD_DOMICILIO"     NUMBER(6,0) PRIMARY KEY,
    "COD_POSTAL"        VARCHAR2(5 CHAR), 
    "DIRECCION"         VARCHAR2(160 CHAR), 
    "MUNICIPIO"         VARCHAR2(5 CHAR), 
    "PAIS"              VARCHAR2(4 CHAR), 
    "PROVINCIA"         VARCHAR2(2 CHAR)
);

COMMENT ON COLUMN "DOMICILIO_SIR"."COD_DOMICILIO" IS 'Código de dirección, es clave primaria.';
COMMENT ON COLUMN "DOMICILIO_SIR"."COD_POSTAL" IS 'Código postal.';
COMMENT ON COLUMN "DOMICILIO_SIR"."DIRECCION" IS 'Dirección.';
COMMENT ON COLUMN "DOMICILIO_SIR"."MUNICIPIO" IS 'Código de municipio.';
COMMENT ON COLUMN "DOMICILIO_SIR"."PAIS" IS 'Código de país.';
COMMENT ON COLUMN "DOMICILIO_SIR"."PROVINCIA" IS 'Código de provincia.';
COMMENT ON TABLE "DOMICILIO_SIR" IS 'Domicilio de un interesado de un asiento del SIR.';

CREATE SEQUENCE "SEQ_DOMICILIO_SIR"
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1;

-- Tabla INTERESADO_SIR
CREATE TABLE "INTERESADO_SIR" (
    "CANAL_PREFERENTE_COMUNICACION"     VARCHAR2(2 CHAR), 
    "COD_ASIENTO"                       NUMBER(6,0) NOT NULL, 
    "COD_DOMICILIO"                     NUMBER(6,0),
    "COD_INTERESADO"                    NUMBER(3,0) NOT NULL, 
    "CORREO_ELECTRONICO"                VARCHAR2(160 CHAR), 
    "DIR_ELECTRONICA_HABILITADA"        VARCHAR2(160 CHAR), 
    "DOCUMENTO_IDENTIFICACION"          VARCHAR2(17 CHAR), 
    "NOMBRE_RAZON_SOCIAL"               VARCHAR2(80 CHAR), 
    "OBSERVACIONES"                     VARCHAR2(160 CHAR), 
    "PRIMER_APELLIDO"                   VARCHAR2(10 CHAR), 
    "SEGUNDO_APELLIDO"                  VARCHAR2(10 CHAR), 
    "TELEFONO"                          VARCHAR2(20 CHAR), 
    "TIPO_DOCUMENTO_IDENTIFICACION"     VARCHAR2(1 CHAR),
    PRIMARY KEY ("COD_INTERESADO", "COD_ASIENTO"),
    FOREIGN KEY ("COD_ASIENTO") REFERENCES "ASIENTO_SIR"("COD_ASIENTO"),
    FOREIGN KEY ("COD_DOMICILIO") REFERENCES "DOMICILIO_SIR"("COD_DOMICILIO")
    ON DELETE SET NULL
);

COMMENT ON COLUMN "INTERESADO_SIR"."CANAL_PREFERENTE_COMUNICACION" IS 'Canal de preferencia para el contacto de la administracion  ["01" = Dirección postal, "02" = Dirección electrónica habilitada, "03" = Comparecencia electrónica.].';
COMMENT ON COLUMN "INTERESADO_SIR"."COD_ASIENTO" IS '[FK ASIENTO.COD_ASIENTO] Código de asiento al que pertenece el interesado, es clave primaria.';
COMMENT ON COLUMN "INTERESADO_SIR"."COD_DOMICILIO" IS '[FK DOMICILIO.COD_DOMICILIO] Código de domicilio del interesado.';
COMMENT ON COLUMN "INTERESADO_SIR"."COD_INTERESADO" IS 'Código de interesado, clave primaria.';
COMMENT ON COLUMN "INTERESADO_SIR"."CORREO_ELECTRONICO" IS 'Correo electrónico.';
COMMENT ON COLUMN "INTERESADO_SIR"."DIR_ELECTRONICA_HABILITADA" IS 'Dirección electrónica en caso de no disponer de buzón de notificaciones telemáticas seguras.';
COMMENT ON COLUMN "INTERESADO_SIR"."DOCUMENTO_IDENTIFICACION" IS 'Documento de identificación.';
COMMENT ON COLUMN "INTERESADO_SIR"."NOMBRE_RAZON_SOCIAL" IS 'Nombre o razón social, dependiendo del TIPO_DOCUMENTO_IDENTIFICACION.';
COMMENT ON COLUMN "INTERESADO_SIR"."OBSERVACIONES" IS 'Observaciones del interesado.';
COMMENT ON COLUMN "INTERESADO_SIR"."PRIMER_APELLIDO" IS 'Primer apellido.';
COMMENT ON COLUMN "INTERESADO_SIR"."SEGUNDO_APELLIDO" IS 'Segundo apellido.';
COMMENT ON COLUMN "INTERESADO_SIR"."TELEFONO" IS 'Teléfono.';
COMMENT ON COLUMN "INTERESADO_SIR"."TIPO_DOCUMENTO_IDENTIFICACION" IS 'Tipo de identificación del interesado ["N" = NIF, "C" = CIF, "P" = Pasaporte, "E" = Documento de identificación de extranjeros, "X" = Otros de persona física, "O" = Código de Origen.].';
COMMENT ON TABLE "INTERESADO_SIR" IS 'Datos del interesado de un asiento del SIR.';

CREATE SEQUENCE "SEQ_INTERESADO_SIR"
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1;

-- Insercion Tipos de documento necesarios para el SIR
INSERT INTO T_TID (TID_COD, TID_COD_ACCEDE, TID_DES, TID_DG1, TID_DG2, TID_DG3, TID_DG4, TID_DG5, TID_DIN, TID_DUP, TID_LMX, TID_NML, TID_PFI, TID_TD1, TID_TD2, TID_TD3, TID_TD4, TID_TD5, TID_TVA) 
VALUES            (51, 51, 'Identificación de extranjeros', 10, NULL, NULL, NULL, NULL, 0, 0, 10, 0, 1, 'X', NULL, NULL, NULL, NULL, 0);

INSERT INTO T_TID (TID_COD, TID_COD_ACCEDE, TID_DES, TID_DG1, TID_DG2, TID_DG3, TID_DG4, TID_DG5, TID_DIN, TID_DUP, TID_LMX, TID_NML, TID_PFI, TID_TD1, TID_TD2, TID_TD3, TID_TD4, TID_TD5, TID_TVA) 
VALUES            (52, 52, 'Otros de persona física', 10, NULL, NULL, NULL, NULL, 0, 0, 10, 0, 1, 'X', NULL, NULL, NULL, NULL, 0);

INSERT INTO T_TID (TID_COD, TID_COD_ACCEDE, TID_DES, TID_DG1, TID_DG2, TID_DG3, TID_DG4, TID_DG5, TID_DIN, TID_DUP, TID_LMX, TID_NML, TID_PFI, TID_TD1, TID_TD2, TID_TD3, TID_TD4, TID_TD5, TID_TVA) 
VALUES            (53, 53, 'Código de Origen', 10, NULL, NULL, NULL, NULL, 0, 0, 10, 0, 1, 'X', NULL, NULL, NULL, NULL, 0);


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
  ID_PROCESO NUMBER(19,0) NOT NULL,
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

	Constraint Hist_Ecrdfirfirmantes_Pk Primary Key (Cod_Municipio, Cod_Procedimiento, Ejercicio, Num_Expediente, Cod_Tramite, Cod_Ocurrencia, Cod_Documento, Id_Usuario),
    Constraint Hist_Ecrdfirfirmantes_Fk Foreign Key (Cod_Municipio, Cod_Procedimiento, Ejercicio, Num_Expediente, Cod_Tramite, Cod_Ocurrencia, Cod_Documento)
        References Hist_E_Crd (Crd_Mun, Crd_Pro, Crd_Eje, Crd_Num, Crd_Tra, Crd_Ocu, Crd_Nud),
  CONSTRAINT Hist_Ecrdfirfirmantes_Fkh FOREIGN KEY (ID_PROCESO) REFERENCES EXP_ENVIO_HISTORICO ("ID") ON DELETE CASCADE
);

Comment On Table Hist_E_Crd_Fir_Firmantes Is 'Usuarios firmantes de un documento de trámites';
Comment On Column Hist_E_Crd_Fir_Firmantes.Id_Proceso Is 'Identificador de ejecución del proceso de paso a histórico';
COMMENT ON COLUMN Hist_E_Crd_Fir_Firmantes.COD_MUNICIPIO IS 'Código municipio';
COMMENT ON COLUMN Hist_E_Crd_Fir_Firmantes.COD_PROCEDIMIENTO IS 'Código procedimiento';
COMMENT ON COLUMN Hist_E_Crd_Fir_Firmantes.EJERCICIO IS 'Ejercicio';
Comment On Column Hist_E_Crd_Fir_Firmantes.Num_Expediente Is 'Número de expediente';
COMMENT ON COLUMN Hist_E_Crd_Fir_Firmantes.COD_TRAMITE IS 'Código de trámite';
COMMENT ON COLUMN Hist_E_Crd_Fir_Firmantes.COD_OCURRENCIA IS 'Código de ocurrencia del trámite';
COMMENT ON COLUMN Hist_E_Crd_Fir_Firmantes.COD_DOCUMENTO IS 'Código del documento';
COMMENT ON COLUMN Hist_E_Crd_Fir_Firmantes.ID_USUARIO IS 'Identificador del usuario (esquema generico A_USU)';
COMMENT ON COLUMN Hist_E_Crd_Fir_Firmantes.ORDEN IS 'Orden que posee el usuario dentro del circuito de firmas';
COMMENT ON COLUMN Hist_E_Crd_Fir_Firmantes.ESTADO_FIRMA IS 'Estado de la firma (O = Pendiente, R = Rechazado, F = Firmado)';
Comment On Column Hist_E_Crd_Fir_Firmantes.Fecha_Firma Is 'Fecha en la que se ha realizado la firma';
Comment On Column Hist_E_Crd_Fir_Firmantes.Firma Is 'Firma del usuario';

commit;

-- Historificación de los flujos personalizados de trámites
Create Table Hist_E_Crd_Fir_Flujo (
  ID_PROCESO NUMBER(19,0) NOT NULL,
	COD_MUNICIPIO NUMBER(3,0) NOT NULL,
	COD_PROCEDIMIENTO VARCHAR2(5 BYTE) NOT NULL,
	EJERCICIO NUMBER(4,0) NOT NULL,
	NUM_EXPEDIENTE VARCHAR2(30 BYTE) NOT NULL,
	COD_TRAMITE NUMBER(4,0) NOT NULL,
	COD_OCURRENCIA NUMBER(4,0) NOT NULL,
	COD_DOCUMENTO NUMBER(4,0) NOT NULL,
	ID_TIPO_FIRMA NUMBER(9,0) NOT NULL,
	
	Constraint Hist_Ecrdfirflujo_Pk Primary Key (Cod_Municipio, Cod_Procedimiento, Ejercicio, Num_Expediente, Cod_Tramite, Cod_Ocurrencia, Cod_Documento),
    Constraint Hist_Ecrdfirflujo_Fk Foreign Key (Cod_Municipio, Cod_Procedimiento, Ejercicio, Num_Expediente, Cod_Tramite, Cod_Ocurrencia, Cod_Documento)
        References Hist_E_Crd (Crd_Mun, Crd_Pro, Crd_Eje, Crd_Num, Crd_Tra, Crd_Ocu, Crd_Nud),
	Constraint Hist_Ecrdfirflujo_Tipo_Fk Foreign Key (Id_Tipo_Firma) References Firma_Tipo (Id),
  CONSTRAINT Hist_Ecrdfirfliujo_Fkh FOREIGN KEY (ID_PROCESO) REFERENCES EXP_ENVIO_HISTORICO ("ID") ON DELETE CASCADE
);

Comment On Table Hist_E_Crd_Fir_Flujo Is 'Flujos de firmas personalizados de los documentos de trámites';
Comment On Column Hist_E_Crd_Fir_Firmantes.Id_Proceso Is 'Identificador de ejecución del proceso de paso a histórico';
COMMENT ON COLUMN Hist_E_Crd_Fir_Flujo.COD_MUNICIPIO IS 'Código municipio';
COMMENT ON COLUMN Hist_E_Crd_Fir_Flujo.COD_PROCEDIMIENTO IS 'Código procedimiento';
COMMENT ON COLUMN Hist_E_Crd_Fir_Flujo.EJERCICIO IS 'Ejercicio';
COMMENT ON COLUMN Hist_E_Crd_Fir_Flujo.NUM_EXPEDIENTE IS 'Número de expediente';
COMMENT ON COLUMN Hist_E_Crd_Fir_Flujo.COD_TRAMITE IS 'Código de trámite';
COMMENT ON COLUMN Hist_E_Crd_Fir_Flujo.COD_OCURRENCIA IS 'Código de ocurrencia del trámite';
Comment On Column Hist_E_Crd_Fir_Flujo.Cod_Documento Is 'Código del documento';
COMMENT ON COLUMN Hist_E_Crd_Fir_Flujo.ID_TIPO_FIRMA IS 'Id del tipo de firma';


commit;

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

COMMIT;

-- Tareas #426095: [LANBIDE] Proceso de carga masiva de registros
-- Mila Noya (04/05/2020)
Alter Table R_Doc_Aportados_Anterior
Modify R_Doc_Aportados_Nom_Doc Varchar2(300 Byte);

-- Tareas #430701 [LANBIDE] Parametrización del proceso de carga masiva de registros: migración de solicitudes del procedimiento ACASE
CREATE TABLE "EM_SOLICITUD_ARTISTAS" 
   (	"CORR_CONSULTA" NUMBER NOT NULL ENABLE, 
	"FECHA_ALTA" DATE NOT NULL, 
	"CIF" VARCHAR2(20),
	"SECC" VARCHAR2(2),
	"GRUPO" VARCHAR2(5) NOT NULL,
	"IMPORTE" NUMBER,
	"EMAIL" VARCHAR2(70) NOT NULL, 
	"NOMBRE" VARCHAR2(35) NOT NULL, 
	"APE1" VARCHAR2(40) NOT NULL, 
	"APE2" VARCHAR2(40),
	"FECENVIO" DATE DEFAULT SYSDATE, 
	"REGISTRO" VARCHAR2(10), 
	"ARCHIVO0" VARCHAR2(200), 
	"ARCHIVO1" VARCHAR2(200), 
	"ARCHIVO2" VARCHAR2(200), 
	"ARCHIVO3" VARCHAR2(200), 
	"ARCHIVO4" VARCHAR2(200), 
	"ARCHIVO5" VARCHAR2(200),
	"ARCHIVO6" VARCHAR2(200),
	"ARCHIVO7" VARCHAR2(200),
	"ARCHIVO8" VARCHAR2(200),
	"TFNO" VARCHAR2(30),
	"IDSOLICITUD" VARCHAR2(10), 
	"IDARCHIVO1" VARCHAR2(10),
	"IDARCHIVO2" VARCHAR2(10),
	"IDARCHIVO3" VARCHAR2(10),
	"IDARCHIVO4" VARCHAR2(10),
	"IDARCHIVO5" VARCHAR2(10),
	"IDARCHIVO6" VARCHAR2(10),
	"IDARCHIVO7" VARCHAR2(10),
	"IDARCHIVO8" VARCHAR2(10),
	"OBSER" VARCHAR2(400),
	 PRIMARY KEY ("CORR_CONSULTA")
   );
 
COMMENT ON TABLE EM_SOLICITUD_ARTISTAS IS 'Tabla donde se guardan las solicitudes de los artistas'; 
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.CORR_CONSULTA IS 'Correlativo del registro';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.FECHA_ALTA IS 'Fecha de garbación del registro';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.CIF IS 'Cif de la persona trabajadora';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.SECC IS 'sección del IAE';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.GRUPO IS 'Código de actividad Económica';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IMPORTE IS 'Importe';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.EMAIL IS 'Email de contacto';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.NOMBRE IS 'Nombre de la persona trabajadora';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.APE1 IS 'Apellido 1';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.APE2 IS 'Apellido 2';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.FECENVIO IS 'Fecha de envío';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.REGISTRO IS 'Código de registro en Lanbidenet';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.ARCHIVO0 IS 'Formulario de la solicitud';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.ARCHIVO1 IS 'Documento acreditativo de la constitución de la Comunidad de Bienes';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.ARCHIVO2 IS 'Copia completa de la declaración del IRPF del ejercicio 2019';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.ARCHIVO3 IS 'Certificado de regulación de cotizaciones referidas al Régimen General de la Seguridad Social';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.ARCHIVO4 IS 'Acreditación documental de la actividad en la que se encontraba de alta en esa fecha';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.ARCHIVO5 IS 'Copia de los contratos de trabajo o de prestación de actividad bajo contrato marco';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.ARCHIVO6 IS 'Derecho a no aportar documentos: acreditación del cumplimiento de obligaciones tributarias y de Seguridad Social';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.ARCHIVO7 IS 'Alta en el Registro de Terceros del Departamento de Hacienda y Economía';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.ARCHIVO8 IS 'Solicitud de reducción IRPF 7';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.TFNO IS 'Teléfono';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IDSOLICITUD IS 'Identificador de Solicitud';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IDARCHIVO1 IS 'Identificador documento 1';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IDARCHIVO2 IS 'Identificador documento 2';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IDARCHIVO3 IS 'Identificador documento 3';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IDARCHIVO4 IS 'Identificador documento 4';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IDARCHIVO5 IS 'Identificador documento 5';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IDARCHIVO6 IS 'Identificador documento 6';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IDARCHIVO7 IS 'Identificador documento 7';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.IDARCHIVO8 IS 'Identificador documento 8';
COMMENT ON COLUMN EM_SOLICITUD_ARTISTAS.OBSER IS 'Observaciones de solicitud telemáticas';

COMMIT;


-- Tarea #433557 [LANBIDE] Portafirmas - Consulta del estado de la firma de documentos de tramitación
CREATE TABLE "ESTADO_PORTAFIRMAS" 
   ( "ID_ESTADO_PORTAFIRMAS" NUMBER NOT NULL , 
	"DOCUMENTO_EXTENSION" VARCHAR2(5) NOT NULL, 
	"TASK_OID" VARCHAR2(50) DEFAULT '0' NOT NULL , 
	"TIPO_PLATEA" VARCHAR2(30) NOT NULL, 
	"SUB_TIPO_PLATEA" VARCHAR2(30), 
	"ESTADO" NUMBER DEFAULT 0 NOT NULL,  
	"BUZON" VARCHAR2(100) NOT NULL,
	"FIRMA_INFORMANTE" VARCHAR2(50), 
	"FECHA_PET" DATE NOT NULL, 
	 "FECH_ACT" DATE NOT NULL,
	CONSTRAINT "ESTADO_PORTAFIRMAS_PK" PRIMARY KEY ("ID_ESTADO_PORTAFIRMAS")
	 );
   
 CREATE SEQUENCE ID_ESTADO_PORTAFIRMAS_SEQ START WITH 1 INCREMENT BY 1;
   
COMMENT ON TABLE ESTADO_PORTAFIRMAS IS 'Tabla donde se guardan los estados de los documentos enviados al portafirmas'; 
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.ID_ESTADO_PORTAFIRMAS IS 'ID del estado del documento enviado al portafirmas';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.DOCUMENTO_EXTENSION IS 'Extensión del documento enviado al portafirmas';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.TASK_OID IS 'ID de la tarea';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.TIPO_PLATEA IS 'Tipo de platea: Puede ser FIRMAS o RECHAZO DE FIRMAS';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.SUB_TIPO_PLATEA IS 'Informa si ha ocurrido algún error: Puede ser OK o ERROR';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.ESTADO IS 'Estado de la firma: Puede ser 0 (INICIAL), 1 (SUGERIDA), 3(RECHAZADA), 4 (EJECUTADA)';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.BUZON IS 'Buzón de la persona a la que se ha sugerido la firma';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.FIRMA_INFORMANTE IS 'Buzón de firma';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.FECHA_PET IS 'Fecha de la petición';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS.FECH_ACT IS 'Fecha de modificación';

CREATE TABLE "ESTADO_PORTAFIRMAS_H" 
   (	"ID_ESTADO_PORTAFIRMAS_H" NUMBER NOT NULL , 
	"ESTADO_PORTAFIRMAS" NUMBER NOT NULL , 
	"TASK_OID" VARCHAR2(50) DEFAULT '0' NOT NULL , 
	"TIPO_PLATEA" VARCHAR2(30) NOT NULL, 
	"SUB_TIPO_PLATEA" VARCHAR2(30) NULL, 
	"ESTADO" NUMBER DEFAULT 0 NOT NULL, 
	"BUZON" VARCHAR2(100) NOT NULL,
	"FIRMA_INFORMANTE" VARCHAR2(50) NULL, 
	"FECH_ACT" DATE NOT NULL, 
	 CONSTRAINT ESTADO_PORTAFIRMAS_H_PK PRIMARY KEY ("ID_ESTADO_PORTAFIRMAS_H"),
	 CONSTRAINT ESTADO_PORTAFIRMAS_H_FK FOREIGN KEY (ESTADO_PORTAFIRMAS) REFERENCES ESTADO_PORTAFIRMAS(ID_ESTADO_PORTAFIRMAS)
   );
   
CREATE SEQUENCE ID_ESTADO_PORTAFIRMAS_H_SEQ START WITH 1 INCREMENT BY 1;
   
COMMENT ON TABLE ESTADO_PORTAFIRMAS_H IS 'Tabla donde se guarda un histórico de los cambios estados de los documentos enviados al portafirmas'; 
COMMENT ON COLUMN ESTADO_PORTAFIRMAS_H.ID_ESTADO_PORTAFIRMAS_H IS 'ID del estado histórico del documento enviado al portafirmas';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS_H.ESTADO_PORTAFIRMAS IS 'ID del estado del documento enviado al portafirmas';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS_H.TASK_OID IS 'ID de la tarea';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS_H.TIPO_PLATEA IS 'Tipo de platea: Puede ser FIRMAS o RECHAZO DE FIRMAS';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS_H.SUB_TIPO_PLATEA IS 'Informa si ha ocurrido algún error: Puede ser OK o ERROR';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS_H.ESTADO IS 'Estado de la firma: Puede ser 0 (INICIAL), 1 (SUGERIDA), 3(RECHAZADA), 4 (EJECUTADA)';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS_H.BUZON IS 'Buzón de la persona a la que se ha sugerido la firma';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS_H.FIRMA_INFORMANTE IS 'Buzón de firma';
COMMENT ON COLUMN ESTADO_PORTAFIRMAS_H.FECH_ACT IS 'Fecha de modificación';

ALTER TABLE "E_CRD_FIR" ADD "ESTADO_PORTAFIRMAS" NUMBER;
ALTER TABLE "E_CRD_FIR" ADD FOREIGN KEY (ESTADO_PORTAFIRMAS) REFERENCES ESTADO_PORTAFIRMAS(ID_ESTADO_PORTAFIRMAS);
COMMENT ON COLUMN E_CRD_FIR.ESTADO_PORTAFIRMAS IS 'ID del estado del documento enviado al portafirmas';

commit;

-- Tarea #487164 FLEXIA [LANBIDE] [CONS] - Evitar incidencias al grabar en trámites, ficheros con nombres más largos del permitido en BBDD
--26/04/2021
--Jesus Cordoba
--Se aumenta el tamanyo de los nombres de los ficheros para evitar incidencias
--INI
ALTER TABLE E_TFIT MODIFY TFIT_NOMFICH VARCHAR2(65) NOT NULL;
ALTER TABLE E_TFIT_VIEJA MODIFY TFIT_NOMFICH VARCHAR2(65) NOT NULL;
ALTER TABLE HIST_E_TFIT MODIFY TFIT_NOMFICH VARCHAR2(65) NOT NULL;
ALTER TABLE E_TFI MODIFY TFI_NOMFICH VARCHAR2(65);
ALTER TABLE E_TFI_VIEJA MODIFY TFI_NOMFICH VARCHAR2(65) NOT NULL;
ALTER TABLE HIST_E_TFI MODIFY TFI_NOMFICH VARCHAR2(65);

COMMIT;
--FIN

-- Tarea #501437 FLEXIA-PRO. [LANBIDE] [CONS] Portafirmas - [18164] - Registrar motivo de rechazo de firma en REGEXLAN
--13/07/2021
--Jesus Cordoba
--Se aumenta el tamanyo del campo Observaciones de la tabla E_CRD_FIR
--INI
ALTER TABLE E_CRD_FIR MODIFY OBSERV VARCHAR2(300);
COMMIT;
--FIN

-- Tarea #598918: [LANBIDE] [CONS] Adaptar interfaz de mantenimiento de desplegables externos.
--05/10/2022
--Milagros Noya
--Se anhade a DESPLEGABLES_EXTERNOS una columna para albergar el campo que tiene la descripcion en el idioma alternativo en la tabla externa
--INI
ALTER TABLE DESPLEGABLE_EXTERNO
ADD CAMPO_VALOR_IDIALT VARCHAR2(200 CHAR);
COMMIT;
--FIN


-- Tarea #599358: SIR - REGEXLAN - [23688] - Creación de registros de salida SIR - Gestion SVN Desarrollos
-- 26/12/2022
-- Milagros Noya
-- Creación de tablas
-- INI
-- Oficinas 
create table SIR_OFICINA(
    codigo varchar2(100 byte)
    ,nombre_ES varchar2(1500 byte)  not null
    ,nombre_EU varchar2(1500 byte)
);
comment on table SIR_OFICINA IS 'Listado de Oficinas Gestionadas en DIR3';
comment on column SIR_OFICINA.codigo IS 'Codigo Oficina ';
comment on column SIR_OFICINA.nombre_ES IS 'Nombre Oficina Idioma Castellano';
comment on column SIR_OFICINA.nombre_EU IS 'Nombre Oficina idioma Euskera';
commit;

-- Organismos 
create table SIR_ORGANISMO(
    codigo varchar2(100 byte) primary  key
    ,nombre_ES varchar2(1500 byte)  not null
    ,nombre_EU varchar2(1500 byte)
);
comment on table SIR_ORGANISMO IS 'Listado de Organismos a los que pertenece las Unidades Gestionadas en DIR3';
comment on column SIR_ORGANISMO.codigo IS 'Codigo Organismo ';
comment on column SIR_ORGANISMO.nombre_ES IS 'Nombre Organismo Idioma Castellano';
comment on column SIR_ORGANISMO.nombre_EU IS 'Nombre Organismo idioma Euskera';
commit;

-- Raiz
create table SIR_RAIZ(
    codigo varchar2(100 byte) primary key
    ,nombre_ES varchar2(1500 byte) not null
    ,nombre_EU varchar2(1500 byte)
);
comment on table SIR_RAIZ IS 'Listado de Raiz a los que pertenece las Unidades Gestionadas en DIR3';
comment on column SIR_RAIZ.codigo IS 'Codigo Raiz ';
comment on column SIR_RAIZ.nombre_ES IS 'Nombre Raiz Idioma Castellano';
comment on column SIR_RAIZ.nombre_EU IS 'Nombre Raiz idioma Euskera';
commit;

-- Nivel Administrativo
create table SIR_NIVEL_ADMINISTRATIVO(
    codigo varchar2(100 byte)  primary key
    ,nombre_ES varchar2(1500 byte)  not null
    ,nombre_EU varchar2(1500 byte) 
);
comment on table SIR_NIVEL_ADMINISTRATIVO IS 'Niveles administrativos a los que pertenece las Unidades Gestionadas en DIR3';
comment on column SIR_NIVEL_ADMINISTRATIVO.codigo IS 'Codigo  Nivel';
comment on column SIR_NIVEL_ADMINISTRATIVO.nombre_ES IS 'Nombre Nivel Idioma Castellano';
comment on column SIR_NIVEL_ADMINISTRATIVO.nombre_EU IS 'Nombre Nivel idioma Euskera';
commit;

create table SIR_UNIDAD_DIR3 (									
    codigoUnidad 	VARCHAR2 ( 100 BYTE) PRIMARY KEY
    ,nombreUnidad_ES VARCHAR2(1500 BYTE) not null
    ,nombreUnidad_EU VARCHAR2(1500 BYTE)
    ,codigoOficina	VARCHAR2 ( 100 BYTE)
    ,codigoOrganismo VARCHAR2 ( 100 BYTE)
    ,codigoRaiz VARCHAR2 ( 100 BYTE)
    ,codigoNivelAdministrativo VARCHAR2 ( 100 BYTE)
    ,codigoComunidadAutonoma VARCHAR2 ( 100 BYTE)
    ,codigoProvincia VARCHAR2 ( 100 BYTE)
    ,codVisibleUorFlexia varchar2(15 BYTE)
    ,fechaActivacion date
);
comment on table SIR_UNIDAD_DIR3 IS 'Lista Unidades Gestionadas en DIR3';
comment on column SIR_UNIDAD_DIR3.codigoUnidad IS 'Codigo  DIR3 Unidad';
comment on column SIR_UNIDAD_DIR3.nombreUnidad_ES IS 'Nombre Unidad Idioma Castellano';
comment on column SIR_UNIDAD_DIR3.nombreUnidad_EU IS 'Nombre Unidad idioma Euskera';
comment on column SIR_UNIDAD_DIR3.codigoOficina IS 'Codigo Oficina a la que pertenece la Unidad - Tabla SIR_OFICINA';
comment on column SIR_UNIDAD_DIR3.codigoOrganismo IS 'Codigo Organismo a la que pertenece la Unidad - Tabla SIR_ORGANISMO';
comment on column SIR_UNIDAD_DIR3.codigoRaiz IS 'Codigo Raiz a la que pertenece la Unidad - Tabla SIR_RAIZ';
comment on column SIR_UNIDAD_DIR3.codigoNivelAdministrativo IS 'Nivel Administrativo de la Unidad - Tabla SIR_NIVEL_ADMINISTRATIVO';
comment on column SIR_UNIDAD_DIR3.codigoComunidadAutonoma IS 'Codigo Comunidad Autonoma  a la que pertenece la Unidad - Tabla FLBGEN.T_AUT';
comment on column SIR_UNIDAD_DIR3.codigoProvincia IS 'Codigo Provincia a la que pertenece la Unidad - Tabla FLBGEN.T_PRV';
comment on column SIR_UNIDAD_DIR3.codVisibleUorFlexia IS 'Codigo Visible de la unidad si esta creada en el arbol de unidades organicas de Flexia - Lanbide - Debe ser un valor de Tabla a_uor campo UOR_COD_VIS';
comment on column SIR_UNIDAD_DIR3.fechaActivacion IS 'Fecha de activacion de la unidad';
commit;

-- Actualizamos el codigo de la unidad Origen 
UPDATE SIR_UNIDAD_DIR3 SET codVisibleUorFlexia='DIR3LSVE' WHERE codigoUnidad='A16007223';
commit;

-- Tabla relacion Salida Regexlan ==> Unidad Destido SIR
create table SIR_DESTINO_R_RES (
    RES_DEP     NUMBER(3)   
    ,RES_UOR    NUMBER(5)  
    ,RES_TIP     VARCHAR2(1) 
    ,RES_EJE    NUMBER(4)
    ,RES_NUM    NUMBER(6) 
    ,codigoUnidad    VARCHAR2 ( 100 BYTE) 
    ,oficinaRegistroSIR VARCHAR2 ( 100 BYTE) 
    ,numeroRegistroSIR VARCHAR2 ( 100 BYTE) 
    ,usuarioRegistroSIR VARCHAR2 ( 100 BYTE) 
    ,fechaRegistroSIR DATE
    , fechaRegistroSistemaSIR date
    ,fechaRegistro date DEFAULT SYSDATE  
    ,  CONSTRAINT PK_SIR_DESTINO_R_RES PRIMARY KEY   (RES_DEP, RES_UOR,RES_TIP,RES_EJE,RES_NUM,codigoUnidad)
    ,CONSTRAINT FK_SIR_DESTINO_R_RES FOREIGN KEY (codigoUnidad) REFERENCES SIR_UNIDAD_DIR3(codigoUnidad)
    ,CONSTRAINT UQ_SIR_DESTINO_R_RES UNIQUE (RES_DEP, RES_UOR,RES_TIP,RES_EJE,RES_NUM)
);
comment on table SIR_DESTINO_R_RES IS 'Relacion de Salidas de registro y codigos dir3 unidades Destino';
comment on column SIR_DESTINO_R_RES.RES_DEP IS 'Departamento Flexia - Clave primaria R_RES';
comment on column SIR_DESTINO_R_RES.RES_UOR IS 'Organizacion Flexia - Clave primaria R_RES';
comment on column SIR_DESTINO_R_RES.RES_TIP IS 'Tipo Registro (0=Entrada/1=Salida)  Flexia - Clave primaria R_RES';
comment on column SIR_DESTINO_R_RES.RES_EJE IS 'Ejercicio Registro Flexia - Clave primaria R_RES';
comment on column SIR_DESTINO_R_RES.RES_NUM IS 'Numero Registro Flexia - Clave primaria R_RES';
comment on column SIR_DESTINO_R_RES.codigoUnidad  IS 'Codigo  DIR3 Unidad Destino - Clave primaria  SIR_UNIDAD_DIR3';
comment on column SIR_DESTINO_R_RES.oficinaRegistroSIR  IS 'Oficina registro en SIR';
comment on column SIR_DESTINO_R_RES.numeroRegistroSIR  IS 'Numero registro en SIR';
comment on column SIR_DESTINO_R_RES.usuarioRegistroSIR  IS 'Usuario de registro en SIR';
comment on column SIR_DESTINO_R_RES.fechaRegistroSIR  IS 'Fecha de registro SIR';
comment on column SIR_DESTINO_R_RES.fechaRegistroSistemaSIR  IS 'Fecha de creacion registro en SIR';
comment on column SIR_DESTINO_R_RES.fechaRegistro  IS 'Fecha creacion registro en la tabla';
commit; 

-- FIN

-----------------------------------------------------------------------
--------------------- FIN PARCHE 18.01.00 --------------------------
-----------------------------------------------------------------------
---------------------.................----------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.03.00 --------------------------
-----------------------------------------------------------------------

-- Tarea #651021: [LANBIDE] Añadir columna con el valor del campo observaciones de registro
-- 13/04/2023
-- Milagros Noya
-- INI
-- Se mueven los campos de codigo 11 a 18 una posicion para dejar sitio al nuevo campo en la posicion 11
UPDATE A_CAMPLIST SET CAMPLIST_COD = CAMPLIST_COD +1 WHERE CAMPLIST_CODLIST=4 AND CAMPLIST_COD=18;
UPDATE A_CAMPLIST SET CAMPLIST_COD = CAMPLIST_COD +1 WHERE CAMPLIST_CODLIST=4 AND CAMPLIST_COD=17;
UPDATE A_CAMPLIST SET CAMPLIST_COD = CAMPLIST_COD +1 WHERE CAMPLIST_CODLIST=4 AND CAMPLIST_COD=16;
UPDATE A_CAMPLIST SET CAMPLIST_COD = CAMPLIST_COD +1 WHERE CAMPLIST_CODLIST=4 AND CAMPLIST_COD=15;
UPDATE A_CAMPLIST SET CAMPLIST_COD = CAMPLIST_COD +1 WHERE CAMPLIST_CODLIST=4 AND CAMPLIST_COD=14;
UPDATE A_CAMPLIST SET CAMPLIST_COD = CAMPLIST_COD +1 WHERE CAMPLIST_CODLIST=4 AND CAMPLIST_COD=13;
UPDATE A_CAMPLIST SET CAMPLIST_COD = CAMPLIST_COD +1 WHERE CAMPLIST_CODLIST=4 AND CAMPLIST_COD=12;
UPDATE A_CAMPLIST SET CAMPLIST_COD = CAMPLIST_COD +1 WHERE CAMPLIST_CODLIST=4 AND CAMPLIST_COD=11;
COMMIT;

-- Se modifica el ancho de la columna de extracto
UPDATE A_CAMPLIST SET CAMPLIST_TAMANO = 18 WHERE CAMPLIST_CODLIST=4 AND CAMPLIST_COD=10;
COMMIT;

--Se inserta la columna de observaciones en la posicion 11
Insert into A_CAMPLIST (CAMPLIST_COD,CAMPLIST_NOM,CAMPLIST_TAMANO,CAMPLIST_ACTIVO,CAMPLIST_CODLIST,CAMPLIST_ORDEN,CAMPLIST_COLUMNA) values ('11','OBSERVACIONES','18','1','4','-1',null);
COMMIT;
-- FIN
-----------------------------------------------------------------------
--------------------- FIN PARCHE 18.03.00 --------------------------
-----------------------------------------------------------------------
---------------------.................----------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 18.04.04 --------------------------
-----------------------------------------------------------------------

-- Tarea #712531: [LANBIDE] Se reciben errores al intentar meter en base de datos valores cuyo tamaño excede el valor permitido por el campo.
-- 07/11/2023
-- Milagros Noya
-- INI
-- Se modifica el tamaño permitido de los campos de nombre y nombre completo de tercero tanto en T_TER como en T_HTE
alter table t_ter modify ter_nom VARCHAR2(256 byte);
alter table t_ter modify ter_noc VARCHAR2(266 byte);
alter table t_hte modify hte_nom VARCHAR2(256 byte);
alter table t_hte modify hte_noc VARCHAR2(266 byte);
COMMIT;

-- Se modifica el tamaño permitido en el campo de descripción de vía en T_VIA
alter table t_via modify via_nom VARCHAR2(100 byte);
COMMIT;
-- FIN

-- Tarea #487164: FLEXIA [LANBIDE] [CONS] - Evitar incidencias al grabar en trámites, ficheros con nombres más largos del permitido en BBDD
-- 08/11/2023
-- Milagros Noya
-- INI
-- Se modifica el tamaño permitido en el campo de nombre de fichero en E_TFIT
alter table e_tfit modify tfit_nomfich VARCHAR2(100 byte);
COMMIT;
-- Se modifica el tamaño permitido en el campo de nombre de fichero de la tabla de histórico y también en las tablas equivalentes para campos suplementarios de expediente
alter table hist_e_tfit modify tfit_nomfich VARCHAR2(100 byte);
alter table e_tfi modify tfi_nomfich VARCHAR2(100 byte);
alter table hist_e_tfi modify tfi_nomfich VARCHAR2(100 byte);
COMMIT;
-- FIN

-- Tarea #719255: [LANBIDE] Nuevo campo BLOQUEO_PAC - Fase I
-- 27/11/2023
-- Milagros Noya
-- INI
-- Se anhade el nuevo campo 
ALTER TABLE R_TIPOASUNTO
ADD BLOQUEO_PAC NUMBER DEFAULT 0 NOT NULL;
COMMIT;
-- FIN

-- Tarea #817576: REGEXLAN - [37345] - Nueva vista V_REGISTRO_INFO
-- 24/10/2024
-- Milagros Noya
-- INI
-- Se crea la vista V_REGISTRO_INFO
CREATE OR REPLACE VIEW V_REGISTRO_INFO (REG_FEC, REG_FED, REG_EST, REG_FINDIGIT, REG_ASU, REG_UOR_VIS, REG_UOR, REG_CODUOR, REG_UOR_NOM, REG_OBS, REG_INT, REG_DOC_INT, REG_TIP, REG_DOC, REG_DOC_NOM, REG_REM, REG_REM_NOM, REG_TRP, REG_TRP_NOM, REG_USU, REG_NUM, EXR_NUM, UOR_REG_COD_VIS, UOR_NOM_REG, CODIGO_ASUNTO, DESCRIPCION_ASUNTO, RES_TER) AS 
	SELECT  RES_FEC,
	   RES_FED,
	   A.RES_EST,
	   A.FIN_DIGITALIZACION,
		A.RES_ASU,
		UOR_COD_VIS,
		A.RES_UOD,
		A.RES_UOR,
		UOR_NOM,
		A.RES_OBS,
		HTE_NOM
		|| NVL(' ','')
		|| NVL(HTE_PA1,'')
		|| NVL(' ','')
		|| NVL(HTE_AP1,'')
		|| NVL(' ','')
		|| NVL(HTE_PA2,'')
		|| NVL(' ','')
		|| NVL(HTE_AP2,''),
		HTE_DOC,
		A.RES_TIP,
		r_tdo.TDO_COD,
		r_tdo.TDO_DES,
		r_tpe.TPE_COD,
		r_tpe.TPE_DES,
		r_ttr.TTR_COD AS COD_TRANSP,
		r_ttr.TTR_DES,
		A_USU.USU_NOM,
		A.RES_EJE
		|| '/'
		|| A.RES_NUM,
		E_EXR.EXR_NUM,
		(SELECT UOR_COD_VIS
		FROM R_RES B,
		  A_UOR
		WHERE B.RES_UOR=A.RES_UOR
		AND B.RES_TIP  =A.RES_TIP
		AND B.RES_EJE  =A.RES_EJE
		AND B.RES_NUM  =A.RES_NUM
		AND B.RES_DEP  =A.RES_DEP
		AND B.RES_UOR  =A_UOR.UOR_COD
		) AS UOR_REG_COD_VIS,
		(SELECT UOR_NOM
		FROM R_RES B,
		  A_UOR
		WHERE B.RES_UOR=A.RES_UOR
		AND B.RES_TIP  =A.RES_TIP
		AND B.RES_EJE  =A.RES_EJE
		AND B.RES_NUM  =A.RES_NUM
		AND B.RES_DEP  =A.RES_DEP
		AND B.RES_UOR  =A_UOR.UOR_COD
		) AS UOR_NOM_REG,
		A.ASUNTO,
		R_TIPOASUNTO.DESCRIPCION,
		RES_TER
	FROM R_RES A,
		R_TDO,
		R_TPE,
		R_TTR,
		A_UOR,
		T_HTE,
		FLBGEN.A_USU A_USU,
		E_EXR,
		R_TIPOASUNTO
	WHERE A.RES_TDO   = R_TDO.TDO_IDE
		AND A.RES_TPE     = R_TPE.TPE_IDE(+)
		AND A.RES_TTR     = R_TTR.TTR_IDE(+)
		AND A.RES_TER     = HTE_TER(+)
		AND A.RES_TNV     = HTE_NVR(+)
		AND A.RES_UOD     = UOR_COD(+)
		AND A_USU.USU_COD = A.RES_USU
		AND A.RES_DEP     =E_EXR.EXR_DEP(+)
		AND A.RES_UOR     =E_EXR.EXR_UOR(+)
		AND A.RES_TIP     =E_EXR.EXR_TIP(+)
		AND A.RES_EJE     =E_EXR.EXR_EJE(+)
		AND A.RES_NUM     =E_EXR.EXR_NRE(+)
		AND A.ASUNTO      = R_TIPOASUNTO.CODIGO(+)
		AND A.RES_UOR     = R_TIPOASUNTO.UNIDADREGISTRO(+);
-- FIN

-- Tarea #819754: Terceros - Flexia - [37460] - Nuevo campo de tercero número de soporte
-- 08/01/2025
-- Milagros Noya
-- INI
-- Se crea la vista V_NUMSOPORTE_INT_REG
CREATE OR REPLACE FORCE EDITIONABLE VIEW "V_NUMSOPORTE_INT_REG"
(
"DEPARTAMENTO","UNIDAD","TIPO","EJERCICIO","NUMERO","COD_TERCERO","VER_TERCERO",
"COD_TIPDOC","DESC_TIPDOC","DOC_TERCERO","NUM_SOPORTE"
) AS
SELECT 
    R_EXT.EXT_DEP AS DEPARTAMENTO,
    R_EXT.EXT_UOR AS UNIDAD,
    R_EXT.EXT_TIP AS TIPO,
    R_EXT.EXT_EJE AS EJERCICIO,
    R_EXT.EXT_NUM AS NUMERO,
    R_EXT.EXT_TER AS COD_TERCERO,
    R_EXT.EXT_NVR AS VER_TERCERO,
    T_HTE.HTE_TID AS COD_TIPDOC,
    T_TID.TID_DES AS DESC_TIPDOC,
    T_HTE.HTE_DOC AS DOC_TERCERO,
    T_CAMPOS_TEXTO.VALOR AS NUM_SOPORTE
FROM 
    R_EXT
LEFT JOIN 
    T_CAMPOS_TEXTO 
    ON R_EXT.EXT_TER = T_CAMPOS_TEXTO.COD_TERCERO
    AND T_CAMPOS_TEXTO.COD_CAMPO = 'TNUMSOPORTE'
LEFT JOIN 
    T_HTE 
    ON R_EXT.EXT_TER = T_HTE.HTE_TER
    AND R_EXT.EXT_NVR = T_HTE.HTE_NVR
LEFT JOIN 
    T_TID 
    ON T_HTE.HTE_TID = T_TID.TID_COD;

-- FIN