CREATE OR REPLACE FUNCTION getUorCod(codUorVisible IN VARCHAR2)      
    RETURN NUMBER
  IS
    codigo NUMBER;
  BEGIN      
	-- Se recuperar el código interno de uor a partir del código visible de la misma.
	
	SELECT UOR_COD INTO codigo FROM A_UOR WHERE UOR_COD_VIS = codUorVisible;
	RETURN codigo;
  END;
/ 