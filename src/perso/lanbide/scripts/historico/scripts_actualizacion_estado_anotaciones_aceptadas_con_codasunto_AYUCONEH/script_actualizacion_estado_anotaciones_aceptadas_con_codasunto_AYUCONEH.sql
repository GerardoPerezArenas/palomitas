


-- Pone a pendientes las anotaciones de registro aceptadas que tengan como asunto codificado
-- AYUCONEH y que no hayan iniciado ni hayan sido adjuntadas a ningún expediente
create or replace
PROCEDURE updateEstadoAnotacionAYUCONEH
AS
  codAsunto VARCHAR2(255BYTE);    
  codigo NUMBER(10,0);
  usuario NUMBER(4,0);
  fecha DATE;
  tipoEntidad VARCHAR2(12BYTE);
  codEntidad VARCHAR2(30BYTE);
  tipoMov NUMBER(2,0);
  detalles VARCHAR(300BYTE);
BEGIN  
  
	codAsunto:='AYUCONEH';
	  
	DECLARE CURSOR anotaciones IS
		SELECT RES_UOR,RES_TIP,RES_EJE,RES_NUM,RES_DEP FROM R_RES WHERE RES_ASU=codAsunto AND RES_EST=1 AND RES_TIP='E'  AND NOT EXISTS(SELECT EXR_NRE FROM E_EXR WHERE RES_UOR=EXR_UOR AND RES_TIP=EXR_TIP AND RES_EJE=EXR_EJR AND RES_NUM=EXR_NRE AND RES_DEP=EXR_DEP);
	BEGIN		
		FOR anotacion in anotaciones LOOP
		
			dbms_output.put_line('=========> Tratando la anotacion con unidad de registro: ' || anotacion.RES_EJE || '/' || anotacion.RES_NUM || ' con unidad registro:' || anotacion.RES_UOR || ' <==============');  
		
		
			-- Se cambia el estado de la anotación a pendiente
			UPDATE R_RES SET RES_EST=0 WHERE RES_UOR=anotacion.RES_UOR AND RES_TIP=anotacion.RES_TIP AND RES_EJE=anotacion.RES_EJE
			AND RES_NUM=anotacion.RES_NUM AND RES_DEP=anotacion.RES_DEP;
		
			-- Se recupera el código para el historico
			codigo:=getMaximoCodigoHistorico;
			usuario:=5;
			fecha:=sysdate;
			tipoEntidad:='ANOTACION';
			codEntidad:=anotacion.RES_DEP || '/' || anotacion.RES_UOR || '/' || anotacion.RES_TIP || '/' || anotacion.RES_EJE || '/' || anotacion.RES_NUM;
			tipoMov:=9;			
			detalles:='<DescripcionMovimiento><RecuperarHistorico/></DescripcionMovimiento>';
			
			dbms_output.put_line('=========> codigo:' || codigo || ',usuario:' || usuario || ',fecha: ' || fecha || ',tipoEntidad: ' || tipoEntidad || ',codEntidad: ' || codEntidad || ',tipoMov: ' || tipoMov || ',detalles: ' || detalles);
			
			-- Se genera la entrada correspondiente en el histórico de la anotación			
			INSERT INTO R_HISTORICO(CODIGO, USUARIO, FECHA, TIPOENTIDAD,CODENTIDAD, TIPOMOV, DETALLES) VALUES (codigo,usuario,fecha,tipoEntidad,codEntidad,tipoMov,detalles);
                    
				  
		END LOOP;
	END;
END;
/




-- Esta función devuelve el máximo del campo codigo +1 de la tabla R_HISTORICO
create or replace
FUNCTION getMaximoCodigoHistorico    
    RETURN INT
  IS
    SALIDA INT;	
  BEGIN 
		
	SELECT MAX(CODIGO) INTO SALIDA FROM R_HISTORICO;
	RETURN SALIDA +1;	
   
  END;
/