-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.02 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.02 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.03 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.03 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.04 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.04 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.05 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.05 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.06 --------------------------
-----------------------------------------------------------------------

-- #327859: [Sanse] Por defecto aparezca marcado el check de envio de notificaciones electrónicas en la ficha del expediente.
-- Mila Noya
-- Es posible que la instalación no lo requiera (Lanbide -> No; Sanse y Red -> Si; Otras -> ¿?)
ALTER TABLE E_EXT MODIFY  EXT_NOTIFICACION_ELECTRONICA VARCHAR2(1 BYTE) default 1;

commit;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.06 --------------------------
-----------------------------------------------------------------------
-----------------------------------------------------------------------
--------------------- INICIO PARCHE 17.00.07 --------------------------
-----------------------------------------------------------------------

-- #336204: [Sanse] Al enviar la notificación se seleccionan varios interesados pero se envía a uno solo.
-- Manuel José Méijome

CREATE TABLE NOTIFICACION_INDIVIDUAL
    (
    CODIGO_NOTIFICACION_INDIVIDUAL        NUMBER(4,0) NOT NULL ENABLE,
    CODIGO_NOTIFICACION                    NUMBER(4,0) NOT NULL ENABLE,
    TER_COD                             NUMBER(12, 0) NOT NULL ,
    ENVIADA                                VARCHAR2(1 BYTE) NOT NULL,
    FECHA_ACUSE                         TIMESTAMP (6),
    RESULTADO                             VARCHAR2(1 BYTE) DEFAULT NULL,

    CONSTRAINT NOTIFICACION_INDIVIDUAL_PK PRIMARY KEY (CODIGO_NOTIFICACION_INDIVIDUAL)
    );

COMMENT ON COLUMN NOTIFICACION_INDIVIDUAL.CODIGO_NOTIFICACION_INDIVIDUAL IS 'Código de la notificación individual';            
COMMENT ON COLUMN NOTIFICACION_INDIVIDUAL.CODIGO_NOTIFICACION IS 'Código de la notificación origen - TABLA: NOTIFICACION';
COMMENT ON COLUMN NOTIFICACION_INDIVIDUAL.TER_COD IS 'Código del interesado - TABLA: T_TER';
COMMENT ON COLUMN NOTIFICACION_INDIVIDUAL.ENVIADA  IS 'Estado comunicación: E- Enviada , P - Pendiente Envío';
COMMENT ON COLUMN NOTIFICACION_INDIVIDUAL.FECHA_ACUSE  IS 'Fecha acuse recibo notificación';
COMMENT ON COLUMN NOTIFICACION_INDIVIDUAL.RESULTADO  IS 'Resultado del acuse de la notificación: R - Rechazada, A - Aceptada';

CREATE SEQUENCE SEQ_NOTIFICACION_INDIVIDUAL START WITH 1 MAXVALUE 999999999999999999999999999 MINVALUE 1 NOCYCLE CACHE 20 NOORDER;


CREATE TABLE HIST_NOTIFICACION_INDIVIDUAL
    (
    ID_PROCESO                             NUMBER(19,0) NOT NULL ENABLE,
    CODIGO_NOTIFICACION_INDIVIDUAL        NUMBER(4,0) NOT NULL ENABLE,
    CODIGO_NOTIFICACION                    NUMBER(4,0) NOT NULL ENABLE,
    TER_COD                             NUMBER(12, 0) NOT NULL ,
    ENVIADA                                VARCHAR2(1 BYTE) NOT NULL,
    FECHA_ACUSE                         TIMESTAMP (6),
    RESULTADO                             VARCHAR2(1 BYTE) DEFAULT NULL,
    REGISTRO_RT                         VARCHAR2(225 BYTE) DEFAULT NULL,

    CONSTRAINT NOTIFICACION_INDIVIDUAL_PKH PRIMARY KEY (CODIGO_NOTIFICACION_INDIVIDUAL),
      CONSTRAINT FKH_NOTIFICACION_INDV_IDP FOREIGN KEY (ID_PROCESO) REFERENCES EXP_ENVIO_HISTORICO ("ID") ON DELETE CASCADE ENABLE
    );

COMMENT ON COLUMN HIST_NOTIFICACION_INDIVIDUAL.ID_PROCESO IS 'Identificador de ejecución del proceso';
COMMENT ON COLUMN HIST_NOTIFICACION_INDIVIDUAL.CODIGO_NOTIFICACION_INDIVIDUAL IS 'Código de la notificación individual';            
COMMENT ON COLUMN HIST_NOTIFICACION_INDIVIDUAL.CODIGO_NOTIFICACION IS 'Código de la notificación origen - TABLA: NOTIFICACION';
COMMENT ON COLUMN HIST_NOTIFICACION_INDIVIDUAL.TER_COD IS 'Código del interesado - TABLA: T_TER';
COMMENT ON COLUMN HIST_NOTIFICACION_INDIVIDUAL.ENVIADA  IS 'Estado comunicación: E- Enviada , P - Pendiente Envío';
COMMENT ON COLUMN HIST_NOTIFICACION_INDIVIDUAL.FECHA_ACUSE  IS 'Fecha acuse recibo notificación';
COMMENT ON COLUMN HIST_NOTIFICACION_INDIVIDUAL.RESULTADO  IS 'Resultado del acuse de la notificación: R - Rechazada, A - Aceptada';
COMMENT ON COLUMN HIST_NOTIFICACION_INDIVIDUAL.REGISTRO_RT  IS 'Número de registro en la anotación de salida en el Registro Telemático';

CREATE SEQUENCE SEQ_HIST_NOTIFICACION_INDV START WITH 1 MAXVALUE 999999999999999999999999999 MINVALUE 1 NOCYCLE CACHE 20 NOORDER;

commit;

-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.07 --------------------------
-----------------------------------------------------------------------