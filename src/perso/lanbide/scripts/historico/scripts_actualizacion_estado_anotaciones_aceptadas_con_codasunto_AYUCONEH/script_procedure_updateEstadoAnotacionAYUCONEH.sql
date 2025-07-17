create or replace
PROCEDURE updateEstadoAnotacionAYUCONEH
AS
  codAsunto VARCHAR2(255BYTE);
BEGIN

	codAsunto:='AYUCONEH';

	DECLARE CURSOR anotaciones IS
		SELECT RES_UOR,RES_TIP,RES_EJE,RES_NUM,RES_DEP FROM R_RES WHERE ASUNTO=codAsunto AND RES_EST=1 AND RES_TIP='E'  AND NOT EXISTS(SELECT EXR_NRE FROM E_EXR WHERE RES_UOR=EXR_UOR AND RES_TIP=EXR_TIP AND RES_EJE=EXR_EJR AND RES_NUM=EXR_NRE AND RES_DEP=EXR_DEP);
	BEGIN
		FOR anotacion in anotaciones LOOP

			dbms_output.put_line('=========> Tratando la anotacion con unidad de registro: ' || anotacion.RES_EJE || '/' || anotacion.RES_NUM || ' con unidad registro:' || anotacion.RES_UOR || ' <==============');


			-- Se cambia el estado de la anotaci¿n a pendiente
			UPDATE R_RES SET RES_EST=0 WHERE RES_UOR=anotacion.RES_UOR AND RES_TIP=anotacion.RES_TIP AND RES_EJE=anotacion.RES_EJE
			AND RES_NUM=anotacion.RES_NUM AND RES_DEP=anotacion.RES_DEP;

			-- Se genera la entrada correspondiente en el hist¿rico de la anotaci¿n
			INSERTARMOVIMIENTOHISTORICO(anotacion.RES_DEP,anotacion.RES_UOR,anotacion.RES_TIP,anotacion.RES_EJE,anotacion.RES_NUM);


		END LOOP;
	END;
END;