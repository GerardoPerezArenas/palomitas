-- Tarea #88327: [CONS] Script de alta de campo suplementario de tercero para guardar el n�mero de la seguridad social
-- �scar Rodr�guez (03/09/2012)
-- Este script se ejecuta contra cada esquema de organizaci�n existente. Habr� que modificar
-- el valor de la variable cod_organizacion seg�n la organizaci�n: 0 --> Pruebas, 1--> Real/Produccion
DEFINE cod_organizacion = 0;

INSERT INTO T_CAMPOS_EXTRA(COD_MUNICIPIO,COD_CAMPO,ROTULO,DESCRIPCION,COD_PLANTILLA,TIPO_DATO,
TAMANO,MASCARA,OBLIGATORIO,ORDEN,ACTIVO,DESPLEGABLE)
VALUES(&cod_organizacion,'TNUMSEGSOCIAL','N�MERO SEGURIDAD SOCIAL','N�MERO SEGURIDAD SOCIAL',1,2,15,NULL,0,1,'SI',NULL);
COMMIT;
