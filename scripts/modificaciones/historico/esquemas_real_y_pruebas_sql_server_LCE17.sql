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
ALTER TABLE E_EXT MODIFY  EXT_NOTIFICACION_ELECTRONICA CHAR(1) default 1;
GO

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
    CODIGO_NOTIFICACION_INDIVIDUAL        DECIMAL(4,0) IDENTITY NOT NULL ENABLE,
    CODIGO_NOTIFICACION                    DECIMAL(4,0) NOT NULL ENABLE,
    TER_COD                             DECIMAL(12, 0) NOT NULL ,
    ENVIADA                                CHAR(1) NOT NULL,
    FECHA_ACUSE                         DATETIME2 (6),
    RESULTADO                             CHAR(1) DEFAULT NULL,

    CONSTRAINT NOTIFICACION_INDIVIDUAL_PK PRIMARY KEY (CODIGO_NOTIFICACION_INDIVIDUAL)
    );


CREATE TABLE HIST_NOTIFICACION_INDIVIDUAL
    (
    ID_PROCESO                             DECIMAL(19,0) NOT NULL ENABLE,
    CODIGO_NOTIFICACION_INDIVIDUAL        DECIMAL(4,0) NOT NULL ENABLE,
    CODIGO_NOTIFICACION                    DECIMAL(4,0) NOT NULL ENABLE,
    TER_COD                             DECIMAL(12, 0) NOT NULL ,
    ENVIADA                                CHAR(1) NOT NULL,
    FECHA_ACUSE                         DATETIME2 (6),
    RESULTADO                             CHAR(1) DEFAULT NULL,
    REGISTRO_RT                         VARCHAR(225) DEFAULT NULL,

    CONSTRAINT NOTIFICACION_INDIVIDUAL_PKH PRIMARY KEY (CODIGO_NOTIFICACION_INDIVIDUAL),
      CONSTRAINT FKH_NOTIFICACION_INDV_IDP FOREIGN KEY (ID_PROCESO) REFERENCES EXP_ENVIO_HISTORICO ("ID") ON DELETE CASCADE ENABLE
    );

GO

-----------------------------------------------------------------------
--------------------- FIN PARCHE 17.00.07 --------------------------
-----------------------------------------------------------------------