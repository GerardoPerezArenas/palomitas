-- Procedimiento para asignar una uor a los usuarios de una determinada oficina. 
-- Lanzarlo desde el esquema de organización. Es necesario dar permiso de insert temporalmente sobre el usuario generico

create or replace
PROCEDURE asigna_uor_usuarios(
    codVisibleOficinaUsuario IN VARCHAR2,
    codVisibleUorAsignar     IN VARCHAR2)
AS
  codInternoUorAsignar     NUMBER;
  codInternoOficinaUsuario NUMBER;  
  
BEGIN  
  SELECT MAX(uor_cod) INTO codInternoUorAsignar  FROM a_uor WHERE uor_cod_vis =codVisibleUorAsignar;
  SELECT MAX(uor_cod) INTO codInternoOficinaUsuario  FROM a_uor WHERE uor_cod_vis =codVisibleOficinaUsuario;
  FOR usuario      IN
  (SELECT distinct(uou_usu) FROM flbgen.a_uou  WHERE uou_uor = codInternoOficinaUsuario  
  MINUS
  SELECT distinct(uou_usu) FROM flbgen.a_uou  WHERE uou_uor = codInternoUorAsignar  
  )
  LOOP
  INSERT INTO flbgen.a_uou  (UOU_ORG,UOU_ENT,UOU_UOR,UOU_USU,UOU_CAR) VALUES(0,1,codInternoUorAsignar,usuario.uou_usu,NULL);
  END LOOP;
END;
/
