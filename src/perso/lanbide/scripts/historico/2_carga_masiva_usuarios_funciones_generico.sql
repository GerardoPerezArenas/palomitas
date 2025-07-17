DEFINE nombre_esquema = flexia_pruebas;
DEFINIE nombre_esquema_real = flexia_organizacion;

-- Funci�n de oracle que devuelve el c�digo interno de uor del esquema de pruebas
CREATE OR REPLACE FUNCTION getCodigoInternoUor(codUorVisible IN VARCHAR2)      
    RETURN NUMBER
  IS
    codigo NUMBER;
  BEGIN      
	-- Se recuperar el c�digo interno de uor a partir del c�digo visible de la misma.
	SELECT UOR_COD
	INTO codigo
	FROM &nombre_esquema..A_UOR
	WHERE UOR_COD_VIS=codUorVisible;	
	
	RETURN codigo;
  END;
/ 


-- Funci�n de oracle que devuelve el m�ximo c�digo de usuario de la tabla A_USU => OK
CREATE OR REPLACE FUNCTION getCodigoInternoUorReal(codUorVisible IN VARCHAR2)      
    RETURN NUMBER
  IS
    codigo NUMBER;
  BEGIN      
	-- Se recuperar el c�digo interno de uor a partir del c�digo visible de la misma.
	SELECT UOR_COD
	INTO codigo
	FROM &nombre_esquema_real..A_UOR
	WHERE UOR_COD_VIS=codUorVisible;	
	
	RETURN codigo;
  END;
/ 



-- Comprueba si existe una unidad org�nica con un determinado c�digo de uor visible
CREATE OR REPLACE FUNCTION existeCodigoUorVisible(codUorVisible IN VARCHAR2)      
    RETURN NUMBER
  IS
    codigo NUMBER;
  BEGIN      
	-- Se recuperar el c�digo interno de uor a partir del c�digo visible de la misma.
	SELECT COUNT(*)
	INTO codigo
	FROM &nombre_esquema..A_UOR
	WHERE UOR_COD_VIS=codUorVisible;	
	
	IF codigo>=1 THEN
		RETURN 1;
	ELSE 
		RETURN 0;
	END IF;
	
  END;
/ 


-- Comprueba si existe una unidad org�nica con un determinado c�digo de uor visible en el esquema de real
CREATE OR REPLACE FUNCTION existeCodigoUorVisibleReal(codUorVisible IN VARCHAR2)      
    RETURN NUMBER
  IS
    codigo NUMBER;
  BEGIN      
	-- Se recuperar el c�digo interno de uor a partir del c�digo visible de la misma.
	SELECT COUNT(*)
	INTO codigo
	FROM &nombre_esquema_real..A_UOR
	WHERE UOR_COD_VIS=codUorVisible;	
	
	IF codigo>=1 THEN
		RETURN 1;
	ELSE 
		RETURN 0;
	END IF;
	
  END;
/ 



-- Funci�n de oracle que devuelve el m�ximo c�digo de usuario de la tabla A_USU
CREATE OR REPLACE FUNCTION getMaxCodigoUsuario      
    RETURN NUMBER
  IS
    codigo_usuario NUMBER;
  BEGIN      
	-- Si el usuario existe se recupera su c�digo de usuario
	SELECT MAX(USU_COD)
	INTO codigo_usuario
	FROM A_USU;
	codigo_usuario:=codigo_usuario+1;
	RETURN codigo_usuario;
  END;
/  


-- Funci�n de oracle que devuelve el m�ximo c�digo de usuario de la tabla A_USU => OK
CREATE OR REPLACE FUNCTION getMaxCodigoRotacionPassword      
    RETURN NUMBER
  IS
    codigo NUMBER;
  BEGIN      
	-- Si el usuario existe se recupera su c�digo de usuario
	SELECT MAX(COD_PASS)
	INTO codigo
	FROM A_USU_ROTACION_PASS;
	codigo:=codigo+1;
	RETURN codigo;
  END;
/  


-- Funci�n de oracle que devuelve el m�ximo c�digo de usuario de la tabla A_USU => OK
CREATE OR REPLACE FUNCTION existeUsuario(login VARCHAR2)      
    RETURN NUMBER
  IS
    codigo NUMBER;
  BEGIN      
	-- Si el usuario existe se recupera su c�digo de usuario
	SELECT COUNT(*)
	INTO codigo
	FROM A_USU
	WHERE USU_LOG=login;
	
	IF codigo>=1 THEN
		RETURN 1;
	ELSE RETURN 0;
	END IF;
	
  END;
/  