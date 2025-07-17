--------------------------------------------------------------------------
-- FLEXIA 10.4 
-- Altia Consultores 
-- Scripts creación BD flexia 
-- Oracle
-- Carga los usuarios base para la aplicacion
-- Ejecutar con el usuario del esquema genérico.
--------------------------------------------------------------------------

spool 9_0_carga_usuarios_base.log
SET ECHO ON;

-- Usuarios base para Flexia (SW.FORMULARIOS) y correspondientes permisos
INSERT INTO A_USU ( USU_COD, USU_IDI, USU_APL, USU_LOG, USU_PAS, USU_NOM, USU_ACT, USU_CON, USU_LIS, USU_TEB, USU_TDM, USU_ICO, USU_MPR, USU_IGS, USU_IGN, USU_IPS, USU_IPN, USU_CBQ, USU_ICB, USU_NIF, USU_FIRMANTE, USU_EMAIL, USU_FIRMA, USU_TIPO_FIRMA, USU_BLQ, USU_FBA) 
	VALUES ( 5, 1, NULL, 'ADMIN', 'b521caa6e1db82e5a01c924a419870cb72b81635', 'ADMINISTRADOR', 1, 1, 1, 1, 1, 1, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL, NULL, 0, NULL); 
INSERT INTO A_USU ( USU_COD, USU_IDI, USU_APL, USU_LOG, USU_PAS, USU_NOM, USU_ACT, USU_CON, USU_LIS, USU_TEB, USU_TDM, USU_ICO, USU_MPR, USU_IGS, USU_IGN, USU_IPS, USU_IPN, USU_CBQ, USU_ICB, USU_NIF, USU_FIRMANTE, USU_EMAIL, USU_FIRMA, USU_TIPO_FIRMA, USU_BLQ ) 
	VALUES ( 4, 1, NULL, 'SW.FORMULARIOS', 'a02f2e7643380d66b8506e3a3ea96e23b289641e', 'Usuario No Registrado', 1, 1, 1, 1, 1, 1, 1, NULL, NULL, NULL, NULL, 0, NULL, NULL, 0, NULL, NULL, NULL, 0); 
INSERT INTO A_USU_ROTACION_PASS (COD_PASS,USU_PASS, FEC_ALTA, USU_COD) VALUES (1,'b521caa6e1db82e5a01c924a419870cb72b81635',SYSDATE,5);
COMMIT;
-- Salida al sistema
spool off;
quit;
