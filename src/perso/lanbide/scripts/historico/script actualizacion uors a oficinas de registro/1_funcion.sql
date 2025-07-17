CREATE OR REPLACE FUNCTION getUorCod(codUorVisible IN VARCHAR2)      
    RETURN NUMBER
  IS
    codigo NUMBER;
  BEGIN      
	-- Se recuperar el c�digo interno de uor a partir del c�digo visible de la misma.
	
	SELECT UOR_COD INTO codigo FROM A_UOR WHERE UOR_COD_VIS = codUorVisible;
	RETURN codigo;
  END;
/ 