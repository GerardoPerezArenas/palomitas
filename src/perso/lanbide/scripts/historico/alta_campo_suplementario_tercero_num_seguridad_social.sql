-- Tarea #88327: [CONS] Script de alta de campo suplementario de tercero para guardar el número de la seguridad social
-- Óscar Rodríguez (03/09/2012)
-- Este script se ejecuta contra cada esquema de organización existente. Habrá que modificar
-- el valor de la variable cod_organizacion según la organización: 0 --> Pruebas, 1--> Real/Produccion
DEFINE cod_organizacion = 0;

INSERT INTO T_CAMPOS_EXTRA(COD_MUNICIPIO,COD_CAMPO,ROTULO,DESCRIPCION,COD_PLANTILLA,TIPO_DATO,
TAMANO,MASCARA,OBLIGATORIO,ORDEN,ACTIVO,DESPLEGABLE)
VALUES(&cod_organizacion,'TNUMSEGSOCIAL','NÚMERO SEGURIDAD SOCIAL','NÚMERO SEGURIDAD SOCIAL',1,2,15,NULL,0,1,'SI',NULL);
COMMIT;
