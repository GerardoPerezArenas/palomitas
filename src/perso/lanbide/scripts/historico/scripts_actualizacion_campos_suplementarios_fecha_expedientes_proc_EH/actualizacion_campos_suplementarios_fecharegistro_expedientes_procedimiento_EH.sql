


-- Procedimiento almacenado que actualiza los campos suplementarios de tipo fecha
create or replace
PROCEDURE updateCamposFechaProcEH
AS
  codCampoFecPresentacion VARCHAR2(255BYTE);
  codCampoFecMaxResolucion VARCHAR2(255BYTE);
  valorCampoFecPresentacion DATE;
  valorCampoFecMaxResolucion DATE;
  
  FECHA_PRESENTACION DATE;
BEGIN  
  
	codCampoFecPresentacion:='FECPRESEREGISTRO';
	codCampoFecMaxResolucion:='FECMAXIMARESOL';
  
	DECLARE CURSOR expedientes IS
		SELECT EXP_NUM,EXP_EJE,EXP_MUN FROM E_EXP WHERE EXP_PRO='EH' AND EXP_FEF IS NULL ORDER BY E_EXP.EXP_NUM DESC;
	BEGIN		
		FOR expediente in expedientes LOOP
		
			dbms_output.put_line('==============> Tratando el expediente: ' || expediente.EXP_NUM || ' <==============');  
			
			-- SE COMPRUEBA SI EL EXPEDIENTE TIENE VALOR EN EL CAMPO FECPRESEREGISTRO, SINO TIENE, ENTONCES SE RECUPERA LA FECHA DE LA 
			-- ANOTACIÓN QUE INICIÓ EL EXPEDIENTE Y SE GRABA ESA FECHA EN EL MENCIONADO CAMPO SUPLEMENTARIO EL VALOR DE DICHA FECHA
			valorCampoFecPresentacion:=getValorCampoFecha(expediente.EXP_MUN,expediente.EXP_EJE,expediente.EXP_NUM,codCampoFecPresentacion);
			      
			IF valorCampoFecPresentacion IS NULL THEN

				FECHA_PRESENTACION:= getFechaPresentacion(expediente.EXP_MUN,expediente.EXP_EJE,expediente.EXP_NUM);
         
				IF FECHA_PRESENTACION IS NOT NULL THEN
					dbms_output.put_line('El expediente: ' || expediente.EXP_NUM || ' en el campo "' || codCampoFecPresentacion || ' carece de valor, se procede a grabarlo con la fecha de presentación: ' || FECHA_PRESENTACION);  
					INSERT INTO E_TFE(TFE_MUN,TFE_EJE,TFE_NUM,TFE_COD,TFE_VALOR) VALUES(expediente.EXP_MUN,expediente.EXP_EJE,expediente.EXP_NUM,codCampoFecPresentacion,FECHA_PRESENTACION);
				END IF;       
        
			ELSE
				dbms_output.put_line('El expediente: ' || expediente.EXP_NUM || ' ya tiene un valor en el campo suplementario ' || codCampoFecPresentacion || ', no se hace nada');  
			END IF;
						
			
			-- SE COMPRUEBA SI EL EXPEDIENTE TIENE VALOR EN EL CAMPO FECMAXIMARESOL, SINO TIENE, ENTONCES SE RECUPERA LA FECHA DE LA 
			-- ANOTACIÓN QUE INICIÓ EL EXPEDIENTE Y SE GRABA ESA FECHA EN EL MENCIONADO CAMPO SUPLEMENTARIO EL VALOR DE DICHA FECHA
			valorCampoFecMaxResolucion:=getValorCampoFecha(expediente.EXP_MUN,expediente.EXP_EJE,expediente.EXP_NUM,codCampoFecMaxResolucion);			      
			IF valorCampoFecMaxResolucion IS NULL THEN

				FECHA_PRESENTACION:= getFechaPresentacion(expediente.EXP_MUN,expediente.EXP_EJE,expediente.EXP_NUM);
         
				IF FECHA_PRESENTACION IS NOT NULL THEN
					dbms_output.put_line('El expediente: ' || expediente.EXP_NUM || ' carece de valor en el campo "' || codCampoFecMaxResolucion || ', se procede a grabarlo con la fecha de presentación + 6 meses');  
					INSERT INTO E_TFE(TFE_MUN,TFE_EJE,TFE_NUM,TFE_COD,TFE_VALOR) VALUES(expediente.EXP_MUN,expediente.EXP_EJE,expediente.EXP_NUM,codCampoFecMaxResolucion,add_months(FECHA_PRESENTACION,6));
				END IF;       
        
			ELSE
				dbms_output.put_line('El expediente: ' || expediente.EXP_NUM || ' ya tiene valor en el campo suplementario ' || codCampoFecMaxResolucion || ',no se hace nada');  
				
			END IF;
					
				  
		END LOOP;
	END;
END;
/







-- Esta función devuelve el valor de un campo suplementario de tipo fecha a nivel de expediente para un determinado expediente o NULL
-- sino se ha podido recuperar
 create or replace
FUNCTION getValorCampoFecha(codOrganizacion IN NUMBER,ejercicio IN NUMBER ,numExpediente IN VARCHAR2,codCampo IN VARCHAR2)      
    RETURN DATE
  IS
    VALOR DATE;	
  BEGIN 
		
	SELECT TFE_VALOR INTO VALOR FROM E_TFE WHERE TFE_MUN=codOrganizacion AND TFE_EJE=ejercicio AND TFE_NUM=numExpediente AND TFE_COD=codCampo;	
	RETURN VALOR;
	
   EXCEPTION
      WHEN NO_DATA_FOUND THEN RETURN NULL;	
  END;  
 /

 
 
 
-- Esta función devuelve la fecha de presentación de la anotación de registro que ha iniciado el expediente o NULL sino lo tiene o 
-- no se ha podido recuperar
create or replace
FUNCTION getFechaPresentacion(codOrganizacion IN NUMBER,ejercicio IN NUMBER ,numExpediente IN VARCHAR2)      
    RETURN DATE
  IS
    VALOR DATE;	
  BEGIN      	
		
		SELECT RES_FEC INTO VALOR FROM E_EXR,R_RES WHERE EXR_NUM=numExpediente AND EXR_EJE=ejercicio AND EXR_MUN=codOrganizacion 
		AND EXR_TOP=0 AND EXR_NRE=R_RES.RES_NUM AND EXR_EJR=RES_EJE AND EXR_UOR=RES_UOR AND EXR_DEP=RES_DEP;
		
		RETURN VALOR;
   EXCEPTION
      WHEN NO_DATA_FOUND THEN RETURN NULL;		
  END;
 / 


