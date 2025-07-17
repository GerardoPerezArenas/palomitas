-- #78018 Ivan. Procedimiento y sus funciones asociadas para rellenar el campo 
-- oficina de aquellas anotaciones en las que no se haya rellenado aún con la oficina
-- asociada al usuario que registra la anotacion

CREATE OR REPLACE
  FUNCTION getOficinaUsuario(
      codUsuario      IN NUMBER,
      codOrganizacion IN NUMBER)
    RETURN NUMBER
  IS
    oficina        NUMBER(5,0);
    numeroOficinas NUMBER(5,0);
  BEGIN
    SELECT COUNT(uou_uor)
    INTO numeroOficinas
    FROM FLBGEN.a_uou,
      a_uor
    WHERE uou_org = codOrganizacion
    AND uou_usu   = codUsuario
    AND uou_uor   = uor_cod
    AND uor_cod_vis LIKE 'OF%';
    IF numeroOficinas <> 1 THEN
      oficina         := NULL ;
    ELSE
      SELECT uou_uor
      INTO oficina
      FROM FLBGEN.a_uou,
        a_uor
      WHERE uou_org = codOrganizacion
      AND uou_usu   = codUsuario
      AND uou_uor   = uor_cod
      AND uor_cod_vis LIKE 'OF%';
    END IF;
    RETURN oficina;
  END;
  /
CREATE OR REPLACE
FUNCTION getOficinaAnotacion(
    uor_registro    IN NUMBER,
    tipo            IN VARCHAR2,
    ejercicio       IN NUMBER,
    numero          IN NUMBER,
    codOrganizacion IN NUMBER)
  RETURN NUMBER
IS
  usuario_registro NUMBER(4,0);
BEGIN
  SELECT res_usu
  INTO usuario_registro
  FROM r_res
  WHERE res_uor = uor_registro
  AND res_tip   = tipo
  AND res_eje   = ejercicio
  AND res_num   = numero ;
  RETURN getOficinaUsuario(usuario_registro, codOrganizacion);
END;
/
CREATE OR REPLACE
PROCEDURE update_oficina_anotaciones(
    codOrganizacion IN NUMBER)
AS
  oficina NUMBER;
BEGIN
  FOR r IN
  (SELECT res_uor, res_tip, res_eje, res_num FROM r_res WHERE res_ofi IS NULL
  )
  LOOP
    oficina:= getOficinaAnotacion(r.res_uor, r.res_tip, r.res_eje, r.res_num, codOrganizacion);
    UPDATE r_res
    SET res_ofi   = oficina
    WHERE res_uor = r.res_uor
    AND res_tip   = r.res_tip
    AND res_eje   =r.res_eje
    AND res_num   =r.res_num;
  END LOOP;
END;
/