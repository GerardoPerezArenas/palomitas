-- Tareas #80083: Creación de scripts de BBDD que permite cambiar la unidad de un expediente y las de sus trámites
-- Este procedimiento recibe un número de expediente y el código visible de la nueva uor
-- Óscar Rodríguez (29/05/2012)
create or replace
PROCEDURE updateUnidadInicioExpediente(
    numeroExpediente IN VARCHAR2,codigoUorVisible IN VARCHAR2)
AS
  codInternoUor     NUMBER;
BEGIN  
  SELECT MAX(uor_cod) INTO codInternoUor  FROM a_uor WHERE uor_cod_vis=codigoUorVisible;
  
  UPDATE E_EXP SET EXP_UOR=codInternoUor WHERE EXP_NUM=numeroExpediente;
  
  -- SE ACTUALIZAN LAS UNIDADES TRAMITADORAS DE LAS OCURRENCIAS DE TRÁMITE DEL EXPEDIENTE
  FOR instancia IN  
 (SELECT CRO_MUN,CRO_PRO,CRO_EJE,CRO_NUM,CRO_TRA,CRO_OCU FROM E_CRO WHERE CRO_NUM=numeroExpediente)  
  LOOP		
	UPDATE E_CRO set CRO_UTR=codInternoUor 
	WHERE CRO_MUN=instancia.CRO_MUN 
	AND CRO_PRO=instancia.CRO_PRO 
	AND CRO_EJE = instancia.CRO_EJE 
	AND CRO_NUM=numeroExpediente 
	AND CRO_TRA = instancia.CRO_TRA 
	AND CRO_OCU = instancia.CRO_OCU;  
  END LOOP;     
END;
/
	
		
	