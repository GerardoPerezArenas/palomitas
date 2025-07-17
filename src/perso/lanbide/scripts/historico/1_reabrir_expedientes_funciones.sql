-- Comprueba si existe un expediente
-- Devuelve 1 si existe y un 0 sino existe
CREATE OR REPLACE FUNCTION existeExpediente(numExpediente IN VARCHAR2)      
    RETURN NUMBER
  IS
    numero NUMBER;
  BEGIN      
	-- Se recuperar el código interno de uor a partir del código visible de la misma.
	SELECT COUNT(*)
	INTO numero
	FROM E_EXP
	WHERE EXP_NUM=numExpediente;	
	
	IF numero>=1 THEN
		RETURN 1;
	ELSE
		RETURN 0;
	END IF;	
  END;
/ 



-- Recupera el estado del expediente
-- Devuelve 1 si existe y un 0 sino existe
CREATE OR REPLACE FUNCTION getEstadoExpediente(numExpediente IN VARCHAR2)      
    RETURN NUMBER
  IS
    estado NUMBER;
  BEGIN      
	-- Se recuperar el código interno de uor a partir del código visible de la misma.
	SELECT EXP_EST
	INTO estado
	FROM E_EXP
	WHERE EXP_NUM=numExpediente;
	
	RETURN estado;
	
  END;
/ 


-- Reabre los trámites de un expediente anulado o finalizado de forma no convencional
CREATE OR REPLACE FUNCTION reabrirTramitesAnulados(numExpediente IN VARCHAR2)      
    RETURN NUMBER
  IS
    estado NUMBER;
  BEGIN      
	
	-- Si el expediente se anuló o finalizó de forma no convencional, se recuperan los trámites que
	-- fueron cerrados de forma no convencional	
	DECLARE CURSOR tramites is
		SELECT TRASIT_COD,TRASIT_SITUACION,TRASIT_PRO,TRASIT_EJE,TRASIT_MUN,TRASIT_NUM FROM E_TRASIT WHERE TRASIT_NUM=numExpediente AND TRASIT_SITUACION=1;					
	BEGIN    
	FOR tramite in tramites LOOP
					
		-- SE ACTUALIZAN LAS OCURRENCIA DE LOS TRÁMITES DEL EXPEDIENTE PARA REABRIRLOS
		UPDATE E_CRO SET cro_fef = null WHERE CRO_MUN=tramite.TRASIT_MUN AND CRO_PRO=tramite.TRASIT_PRO                    
		   AND CRO_EJE= tramite.TRASIT_EJE AND CRO_NUM=numExpediente                    
		   AND CRO_TRA= tramite.TRASIT_COD;
		   	
		-- SE ELIMINAN LOS TRÁMITES DE LA LISTA DE TRAMITES CERRADOS DE FORMA NO CONVENCIONAL	
		DELETE FROM E_TRASIT WHERE TRASIT_MUN=tramite.TRASIT_MUN AND TRASIT_COD=tramite.TRASIT_COD
		AND TRASIT_EJE=tramite.TRASIT_EJE AND TRASIT_NUM=numExpediente 
		AND TRASIT_PRO=tramite.TRASIT_PRO AND TRASIT_SITUACION=1;									
		
	END LOOP;
	END;				
	
	RETURN 1;
  END;
/ 





create or replace
PROCEDURE reabrirExpediente(
    numExpediente IN VARCHAR2)
AS
  estado NUMBER;  
  
BEGIN  
	IF existeExpediente(numExpediente) =1 THEN
		-- SI EXISTE EL EXPEDIENTE
		
		estado:= getEstadoExpediente(numExpediente);
		IF ESTADO = 1 OR ESTADO = 9 THEN
			-- EXPEDIENTE FINALIZADO O ANULADO
			
			-- Se reabre el expediente dejando en estado de pendiente
			UPDATE E_EXP SET EXP_FEF=null,EXP_EST=0 WHERE exp_num=numExpediente;		
			IF ESTADO = 1 THEN
				-- Expediente finalizado siguiente el flujo de tramitación
				estado:=reabrirTramitesAnulados(numExpediente);
				
				-- SE ELIMINA EL EXPEDIENTE DE LA TABLA E_EXPSIT PARA INDICAR QUE NO HA SIDO ANULADO
				DELETE FROM E_EXPSIT WHERE EXPSIT_NUM=numExpediente;
				
			ELSE
				-- El expediente había sido finalizado correctamente siguiendo el flujo de tramitación
				UPDATE E_CRO SET cro_fef = null WHERE CRO_NUM=numExpediente 
					AND CRO_FEF in (select  MAX(CRO_FEF)  from E_CRO where CRO_NUM=numExpediente);				
						
			END IF;
		
		END IF;
	
	END IF;
END;
/




